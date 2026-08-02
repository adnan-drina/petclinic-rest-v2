package com.demo.util;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Quarkus redesign of Spring CallMonitoringAspect (JMX @ManagedResource).
 * Preserves legacy public method names (O-REDESIGNSIG). Uses in-process
 * counters; scrapeable process metrics remain via quarkus-smallrye-metrics.
 */
@ApplicationScoped
public class CallMonitoringAspect {

    private final AtomicBoolean enabled = new AtomicBoolean(true);
    private final AtomicInteger callCount = new AtomicInteger();
    private final AtomicLong accumulatedCallTime = new AtomicLong();

    public boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public void reset() {
        this.callCount.set(0);
        this.accumulatedCallTime.set(0);
    }

    public int getCallCount() {
        return callCount.get();
    }

    public long getCallTime() {
        int count = this.callCount.get();
        if (count > 0) {
            return this.accumulatedCallTime.get() / count;
        }
        return 0;
    }

    public Object invoke(Callable<Object> joinPoint) throws Exception {
        if (!this.enabled.get()) {
            return joinPoint.call();
        }
        long start = System.nanoTime();
        try {
            return joinPoint.call();
        } finally {
            long millis = (System.nanoTime() - start) / 1_000_000L;
            this.callCount.incrementAndGet();
            this.accumulatedCallTime.addAndGet(millis);
        }
    }
}
