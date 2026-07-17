package com.edatasite.workforce.gwt.core.client.ui.communication;

import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Anvar Akramov on 27/6/2020.
 */
public class AsteriskSettings implements IsSerializable {

    public static final String NUMBER = "NUMBER";
    public static final String ASTERISK_HOST = "ASTERISK_HOST";
    public static final String ASTERISK_PORT = "ASTERISK_PORT";

    private Integer id;
    private Integer asteriskSettingsId;
    private Integer userId;
    private EmployeeListItem user;
    private String asteriskNumber;
    private String asteriskHost;
    private String asteriskPort;
    private String asteriskUsername;
    private String asteriskPassword;

    public AsteriskSettings() {
    }

    public AsteriskSettings(Integer id, String asteriskNumber, String asteriskHost, String asteriskPort, String asteriskUsername, String asteriskPassword) {
        this.id = id;
        this.asteriskNumber = asteriskNumber;
        this.asteriskHost = asteriskHost;
        this.asteriskPort = asteriskPort;
        this.asteriskUsername = asteriskUsername;
        this.asteriskPassword = asteriskPassword;
    }

    public AsteriskSettings(String asteriskNumber, String asteriskHost, String asteriskPort, String asteriskUsername, String asteriskPassword) {
        this(null, asteriskNumber, asteriskHost, asteriskPort, asteriskUsername, asteriskPassword);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAsteriskNumber() {
        return asteriskNumber;
    }

    public void setAsteriskNumber(String asteriskNumber) {
        this.asteriskNumber = asteriskNumber;
    }

    public String getAsteriskHost() {
        return asteriskHost;
    }

    public void setAsteriskHost(String asteriskHost) {
        this.asteriskHost = asteriskHost;
    }

    public String getAsteriskPort() {
        return asteriskPort;
    }

    public void setAsteriskPort(String asteriskPort) {
        this.asteriskPort = asteriskPort;
    }

    public String getAsteriskUsername() {
        return asteriskUsername;
    }

    public void setAsteriskUsername(String asteriskUsername) {
        this.asteriskUsername = asteriskUsername;
    }

    public String getAsteriskPassword() {
        return asteriskPassword;
    }

    public void setAsteriskPassword(String asteriskPassword) {
        this.asteriskPassword = asteriskPassword;
    }

    public Integer getAsteriskSettingsId() {
        return asteriskSettingsId;
    }

    public void setAsteriskSettingsId(Integer asteriskSettingsId) {
        this.asteriskSettingsId = asteriskSettingsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public EmployeeListItem getUser() {
        return user;
    }

    public void setUser(EmployeeListItem user) {
        this.user = user;
    }
}
