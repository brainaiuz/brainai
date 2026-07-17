package com.edatasite.workforce.gwt.core.client.rpc.websocket;

public class WebSocketServerObject {
    private Integer eventType;
    private Integer userId;
    private String data;
    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
