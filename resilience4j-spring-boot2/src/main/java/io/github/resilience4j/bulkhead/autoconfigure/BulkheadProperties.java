package io.github.resilience4j.bulkhead.autoconfigure;

import io.github.resilience4j.bulkhead.configure.BulkheadConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "resilience4j.bulkhead")
public class BulkheadProperties extends BulkheadConfigurationProperties {
}
