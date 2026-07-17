package com.edatasite.workforce.core.kafka.data;

public class EventData {
    private final String type;
    private final Integer id;

    public EventData(String type, Integer id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }
}
