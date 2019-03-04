package io.github.resilience4j.bulkhead.configure;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
