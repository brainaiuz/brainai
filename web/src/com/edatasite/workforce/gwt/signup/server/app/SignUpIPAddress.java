package com.edatasite.workforce.gwt.signup.server.app;

import java.io.Serializable;

public class SignUpIPAddress implements Serializable {
    public static final int IP_LIFE_TIME = 1800;//life time is in seconds
    public static final int MAX_ATTEMPTS = 5;

    private String ipAddress;
    private Integer attemts;

    public SignUpIPAddress() {
    }

    public SignUpIPAddress(String ipAddress, Integer attemts) {
        this.ipAddress = ipAddress;
        this.attemts = attemts;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getAttemts() {
        return attemts;
    }

    public void setAttemts(Integer attemts) {
        this.attemts = attemts;
    }
}
