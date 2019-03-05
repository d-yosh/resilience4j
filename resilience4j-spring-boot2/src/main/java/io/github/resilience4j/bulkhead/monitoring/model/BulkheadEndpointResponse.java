package io.github.resilience4j.bulkhead.monitoring.model;

import java.util.List;

public class BulkheadEndpointResponse {
    private List<String> bulkheads;

    public BulkheadEndpointResponse() {
    }

    public BulkheadEndpointResponse(List<String> bulkheadNames) {
        this.bulkheads = bulkheadNames;
    }

    /**
     * @return the bulkheadNames
     */
    public List<String> getBulkheads() {
        return bulkheads;
    }

    /**
     * @param bulkheadNames the bulkheadNames to set
     */
    public void setBulkheadNames(List<String> bulkheadNames) {
        this.bulkheads = bulkheadNames;
    }
}
