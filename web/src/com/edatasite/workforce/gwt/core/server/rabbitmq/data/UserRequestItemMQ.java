package com.edatasite.workforce.gwt.core.server.rabbitmq.data;

import java.io.Serializable;
import java.util.Date;

public class UserRequestItemMQ implements Serializable {

    private String methodName;
    private Integer userId;
    private String sessionId;
    private Date requestDate;

    public UserRequestItemMQ(String methodName, Integer userId, String sessionId, Date requestDate) {
        this.methodName = methodName;
        this.userId = userId;
        this.sessionId = sessionId;
        this.requestDate = requestDate;
    }

    public UserRequestItemMQ() {
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return "UserRequestItemMQ{" +
                "methodName='" + methodName + '\'' +
                ", userId=" + userId +
                ", sessionId='" + sessionId + '\'' +
                ", requestDate=" + requestDate +
                '}';
    }
}
