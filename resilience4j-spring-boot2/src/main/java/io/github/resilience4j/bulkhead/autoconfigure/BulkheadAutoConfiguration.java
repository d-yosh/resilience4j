package io.github.resilience4j.bulkhead.autoconfigure;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.configure.BulkheadConfiguration;
import io.github.resilience4j.bulkhead.configure.BulkheadConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(Bulkhead.class)
@EnableConfigurationProperties(BulkheadConfigurationProperties.class)
@Import(BulkheadConfiguration.class)
public class BulkheadAutoConfiguration {
}
