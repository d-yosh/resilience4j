package io.github.resilience4j.bulkhead.monitoring.endpoint;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.monitoring.model.BulkheadEndpointResponse;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

@Endpoint(id = "bulkheads")
public class BulkheadEndpoint {
    private final BulkheadRegistry bulkheadRegistry;

    public BulkheadEndpoint(BulkheadRegistry bulkheadRegistry) {
        this.bulkheadRegistry = bulkheadRegistry;
    }

    @ReadOperation
    public BulkheadEndpointResponse getAllBulkheads() {
        return new BulkheadEndpointResponse(
                bulkheadRegistry.getAllBulkheads().map(Bulkhead::getName).sorted().toJavaList());
    }
}
