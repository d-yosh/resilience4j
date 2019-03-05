package io.github.resilience4j.bulkhead.configure;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link org.springframework.context.annotation.Configuration
 * Configuration} for resilience4j-bulkhead.
 */
@Configuration
public class BulkheadConfiguration {

    @Bean
    public BulkheadRegistry bulkheadRegistry(BulkheadConfigurationProperties bulkheadConfigurationProperties) {
        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.ofDefaults();
        bulkheadConfigurationProperties.getBulkheads().forEach((name, properties)-> {
            BulkheadConfig bulkheadConfig = bulkheadConfigurationProperties.createBulkheadConfig(name);
            bulkheadRegistry.bulkhead(name, bulkheadConfig);
        });

        return bulkheadRegistry;
    }

    @Bean
    public BulkheadAspect bulkheadAspect(BulkheadConfigurationProperties bulkheadConfigurationProperties, BulkheadRegistry bulkheadRegistry) {
        return new BulkheadAspect(bulkheadConfigurationProperties, bulkheadRegistry);
    }
}
