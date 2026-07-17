package com.edatasite.workforce.aspects;

import java.util.concurrent.atomic.AtomicLong;

public class ExecutionData {
    private AtomicLong sum;
    private AtomicLong count;

    public ExecutionData(AtomicLong sum, AtomicLong count) {
        this.sum = sum;
        this.count = count;
    }

    public ExecutionData accept(Long value) {
        sum.addAndGet(value);
        count.incrementAndGet();
        return this;
    }

    public Integer avg() {
        return sum.intValue() / count.intValue();
    }

    public AtomicLong getSum() {
        return sum;
    }

    public AtomicLong getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "ExecutionData{" +
                "sum=" + sum +
                ", count=" + count +
                '}';
    }
}
