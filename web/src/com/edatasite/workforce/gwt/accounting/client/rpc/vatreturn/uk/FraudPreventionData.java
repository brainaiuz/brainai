/*
 * Copyright (c) 2023.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

public class FraudPreventionData implements IsSerializable {
    private String govClientConnectionMethod = "WEB_APP_VIA_SERVER";
    private Boolean govClientBrowserDoNotTrack = false;
    private String govClientBrowserJSUserAgent;
    private String govClientDeviceID;
    private String govClientPublicIP;
    private Integer govClientPublicPort;
    private String govClientPublicIpTimestamp;
    private String govClientScreens;
    private String govClientTimezone;
    private String govClientWindowSize;
    private String govVendorPublicIP;
    private String govClientUserIDs;
    private String govVendorForwarded;
    private String govVendorProductName;

    public String getGovClientConnectionMethod() {
        return govClientConnectionMethod;
    }

    public void setGovClientConnectionMethod(String govClientConnectionMethod) {
        this.govClientConnectionMethod = govClientConnectionMethod;
    }

    public Boolean getGovClientBrowserDoNotTrack() {
        return govClientBrowserDoNotTrack;
    }

    public void setGovClientBrowserDoNotTrack(Boolean govClientBrowserDoNotTrack) {
        this.govClientBrowserDoNotTrack = govClientBrowserDoNotTrack;
    }

    public String getGovClientBrowserJSUserAgent() {
        return govClientBrowserJSUserAgent;
    }

    public void setGovClientBrowserJSUserAgent(String govClientBrowserJSUserAgent) {
        this.govClientBrowserJSUserAgent = govClientBrowserJSUserAgent;
    }

    public String getGovClientDeviceID() {
        return govClientDeviceID;
    }

    public void setGovClientDeviceID(String govClientDeviceID) {
        this.govClientDeviceID = govClientDeviceID;
    }

    public String getGovClientPublicIP() {
        return govClientPublicIP;
    }

    public void setGovClientPublicIP(String govClientPublicIP) {
        this.govClientPublicIP = govClientPublicIP;
    }

    public Integer getGovClientPublicPort() {
        return govClientPublicPort;
    }

    public void setGovClientPublicPort(Integer govClientPublicPort) {
        this.govClientPublicPort = govClientPublicPort;
    }

    public String getGovClientPublicIpTimestamp() {
        return govClientPublicIpTimestamp;
    }

    public void setGovClientPublicIpTimestamp(String govClientPublicIpTimestamp) {
        this.govClientPublicIpTimestamp = govClientPublicIpTimestamp;
    }

    public String getGovClientScreens() {
        return govClientScreens;
    }

    public void setGovClientScreens(String govClientScreens) {
        this.govClientScreens = govClientScreens;
    }

    public String getGovClientTimezone() {
        return govClientTimezone;
    }

    public void setGovClientTimezone(String govClientTimezone) {
        this.govClientTimezone = govClientTimezone;
    }

    public String getGovClientWindowSize() {
        return govClientWindowSize;
    }

    public void setGovClientWindowSize(String govClientWindowSize) {
        this.govClientWindowSize = govClientWindowSize;
    }

    public String getGovVendorPublicIP() {
        return govVendorPublicIP;
    }

    public void setGovVendorPublicIP(String govVendorPublicIP) {
        this.govVendorPublicIP = govVendorPublicIP;
    }

    public String getGovClientUserIDs() {
        return govClientUserIDs;
    }

    public void setGovClientUserIDs(String govClientUserIDs) {
        this.govClientUserIDs = govClientUserIDs;
    }

    public String getGovVendorForwarded() {
        return govVendorForwarded;
    }

    public void setGovVendorForwarded(String govVendorForwarded) {
        this.govVendorForwarded = govVendorForwarded;
    }

    public String getGovVendorProductName() {
        return govVendorProductName;
    }

    public void setGovVendorProductName(String govVendorProductName) {
        this.govVendorProductName = govVendorProductName;
    }

    public HashMap<String, String> getValuesAsMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("Gov-Client-Connection-Method", getGovClientConnectionMethod());
        result.put("Gov-Client-Browser-Do-Not-Track", String.valueOf(getGovClientBrowserDoNotTrack()));
        result.put("Gov-Client-Browser-JS-User-Agent", getGovClientBrowserJSUserAgent());
        result.put("Gov-Client-Device-ID", getGovClientDeviceID());
        result.put("Gov-Client-Public-IP", getGovClientPublicIP());
        result.put("Gov-Client-Public-IP-Timestamp", getGovClientPublicIpTimestamp());
        result.put("Gov-Client-Public-Port", String.valueOf(getGovClientPublicPort()));
        result.put("Gov-Client-Screens", getGovClientScreens());
        result.put("Gov-Client-Timezone", getGovClientTimezone());
        result.put("Gov-Client-User-IDs", getGovClientUserIDs());
        result.put("Gov-Client-Window-Size", getGovClientWindowSize());
        result.put("Gov-Vendor-Forwarded", getGovVendorForwarded());
        result.put("Gov-Vendor-Product-Name", getGovVendorProductName());
        result.put("Gov-Vendor-Public-IP", getGovVendorPublicIP());
        result.put("Gov-Vendor-Version", "frontend-app=v1.2.1&server-app=v1.0.1");
        return result;
    }
}
