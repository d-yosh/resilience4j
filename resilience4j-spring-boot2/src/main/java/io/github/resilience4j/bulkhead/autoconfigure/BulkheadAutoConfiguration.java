package io.github.resilience4j.bulkhead.autoconfigure;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.configure.BulkheadConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} for resilience4j-bulkhead.
 */
@Configuration
@ConditionalOnClass(Bulkhead.class)
@EnableConfigurationProperties(BulkheadProperties.class)
@Import(BulkheadConfiguration.class)
public class BulkheadAutoConfiguration {
}
