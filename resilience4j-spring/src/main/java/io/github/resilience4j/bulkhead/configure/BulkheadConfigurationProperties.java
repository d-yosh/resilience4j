package io.github.resilience4j.bulkhead.configure;

import io.github.resilience4j.bulkhead.BulkheadConfig;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;

public class BulkheadConfigurationProperties {

    private Map<String, BulkheadProperties> bulkheads = new HashMap<>();
    private int bulkheadAspectOrder = Integer.MAX_VALUE - 2;

    public Map<String, BulkheadProperties> getBulkheads() {
        return bulkheads;
    }

    public void setBulkheads(Map<String, BulkheadProperties> bulkheads) {
        this.bulkheads = bulkheads;
    }

    public int getBulkheadAspectOrder() {
        return bulkheadAspectOrder;
    }

    public void setBulkheadAspectOrder(int bulkheadAspectOrder) {
        this.bulkheadAspectOrder = bulkheadAspectOrder;
    }

    public BulkheadProperties getBulkheadProperties(String name) {
        return this.bulkheads.get(name);
    }

    public BulkheadConfig createBulkheadConfig(String name) {
        return createBulkheadConfig(getBulkheadProperties(name));
    }

    public BulkheadConfig createBulkheadConfig(BulkheadProperties properties) {
        return buildBulkheadConfig(properties).build();
    }

    public BulkheadConfig.Builder buildBulkheadConfig(BulkheadProperties properties) {
        if(properties == null) {
            return new BulkheadConfig.Builder();
        }

        BulkheadConfig.Builder builder = BulkheadConfig.custom();

        if(properties.getMaxConcurrentCalls() != null) {
            builder.maxConcurrentCalls(properties.maxConcurrentCalls);
        }

        if(properties.getMaxWaitTime() != null) {
            builder.maxWaitTime(properties.maxWaitTime);
        }

        return builder;
    }

    /**
     * Class storing property values for configuring {@link io.github.resilience4j.bulkhead.Bulkhead} instances.
     */
    public static class BulkheadProperties {
        @Min(1)
        private Integer maxConcurrentCalls;

        @Min(0)
        private Long maxWaitTime;

        private boolean registeredHealthIndicator = true;

        /**
         * @return the maxConcurrentCalls
         */
        public Integer getMaxConcurrentCalls() {
            return maxConcurrentCalls;
        }

        /**
         * @param maxConcurrentCalls the maxConcurrentCalls to set
         */
        public void setMaxConcurrentCalls(Integer maxConcurrentCalls) {
            this.maxConcurrentCalls = maxConcurrentCalls;
        }

        /**
         * @return the maxWaitTime
         */
        public Long getMaxWaitTime() {
            return maxWaitTime;
        }

        /**
         * @param maxWaitTime the maxWaitTime to set
         */
        public void setMaxWaitTime(Long maxWaitTime) {
            this.maxWaitTime = maxWaitTime;
        }

        /**
         * @return the registeredHealthIndicator
         */
        public boolean isRegisteredHealthIndicator() {
            return registeredHealthIndicator;
        }

        /**
         * @param registeredHealthIndicator the registeredHealthIndicator to set
         */
        public void setRegisteredHealthIndicator(boolean registeredHealthIndicator) {
            this.registeredHealthIndicator = registeredHealthIndicator;
        }
    }
}
