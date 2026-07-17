package com.edatasite.workforce.aspects;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MethodExecutionStatisticService {

    private Map<String, ExecutionData> statData = new ConcurrentHashMap<>();

    public MethodExecutionStatisticService() {

    }

    public void accept(String key, Long value) {
        statData.compute(key, (k, v) -> v == null ? new ExecutionData(new AtomicLong(value), new AtomicLong(1)) : v.accept(value));
    }

    public Map<String, Integer> getStatData() {
        return
                statData.entrySet().stream()
                        .sorted((o1, o2) -> o2.getValue().avg().compareTo(o1.getValue().avg()))
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().avg(), (u, v) -> u, LinkedHashMap::new));
    }

}
