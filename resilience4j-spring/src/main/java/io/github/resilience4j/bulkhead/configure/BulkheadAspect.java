package io.github.resilience4j.bulkhead.configure;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.utils.BulkheadUtils;

import java.lang.reflect.Method;

/**
 * This Spring AOP aspect intercepts all methods which are annotated with a {@link Bulkhead} annotation.
 * The aspect protects an annotated method with a Bulkhead. The BulkheadRegistry is used to retrieve an instance of a Bulkhead for
 * a specific name.
 */
@Aspect
public class BulkheadAspect implements Ordered {
    private static final String BULKHEAD_RECEIVED = "Created or retrieved bulkhead '{}' with max amount of concurrent call: '{}'; max wait time: '{}'; method: '{}'";
    private static final Logger logger = LoggerFactory.getLogger(BulkheadAspect.class);

    private final BulkheadConfigurationProperties bulkheadConfigurationProperties;
    private final BulkheadRegistry bulkheadRegistry;

    public BulkheadAspect(BulkheadConfigurationProperties bulkheadConfigurationProperties,
                          BulkheadRegistry bulkheadRegistry) {
        this.bulkheadConfigurationProperties = bulkheadConfigurationProperties;
        this.bulkheadRegistry = bulkheadRegistry;
    }

    @Pointcut(value = "@within(bulkhead) || @annotation(bulkhead)", argNames = "bulkhead")
    public void matchAnnotatedClassOrMethod(Bulkhead bulkhead) {
    }

    @Around(value = "matchAnnotatedClassOrMethod(bulkheadAnnotation)", argNames = "proceedingJoinPoint, bulkheadAnnotation")
    public Object bulkheadAroundAdvice(ProceedingJoinPoint proceedingJoinPoint, Bulkhead bulkheadAnnotation)
            throws Throwable {
        if (bulkheadAnnotation == null) {
            bulkheadAnnotation = getBulkheadAnnotation(proceedingJoinPoint);
        }
        String bulkheadName = bulkheadAnnotation.name();
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        String methodName = method.getDeclaringClass().getName() + "#" + method.getName();

        io.github.resilience4j.bulkhead.Bulkhead bulkhead = getOrCreateBulkhead(methodName, bulkheadName);
        return handleJoinPoint(proceedingJoinPoint, bulkhead, methodName);
    }

    private Object handleJoinPoint(ProceedingJoinPoint proceedingJoinPoint, io.github.resilience4j.bulkhead.Bulkhead bulkhead, String methodName) throws Throwable {
        BulkheadUtils.isCallPermitted(bulkhead);
        try {
            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Invocation of method '" + methodName + "' failed!", e);
            }
            throw e;
        } finally {
            bulkhead.onComplete();
        }
    }

    private io.github.resilience4j.bulkhead.Bulkhead getOrCreateBulkhead(String methodName, String bulkheadName) {
        io.github.resilience4j.bulkhead.Bulkhead bulkhead = bulkheadRegistry.bulkhead(bulkheadName);
        if (logger.isDebugEnabled()) {
            BulkheadConfig bulkheadConfig = bulkhead.getBulkheadConfig();
            logger.debug(BULKHEAD_RECEIVED, bulkheadName, bulkheadConfig.getMaxConcurrentCalls(), bulkheadConfig.getMaxWaitTime(), methodName);
        }
        return bulkhead;
    }

    private Bulkhead getBulkheadAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        Bulkhead bulkhead = null;
        Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
        if (targetClass.isAnnotationPresent(Bulkhead.class)) {
            bulkhead = targetClass.getAnnotation(Bulkhead.class);
            if (bulkhead == null) {
                bulkhead = targetClass.getDeclaredAnnotation(Bulkhead.class);
            }
            if (bulkhead == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("TargetClass has no declared annotation 'Bulkhead'");
                }
            }
        }
        return bulkhead;
    }

    @Override
    public int getOrder() {
        return bulkheadConfigurationProperties.getBulkheadAspectOrder();
    }
}
