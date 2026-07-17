package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IpAddressRange implements IsSerializable {

    private String fromIP = "";
    private String toIP = "";

    public IpAddressRange() {
    }

    public IpAddressRange(String from, String to) {
            fromIP = from.trim();
            toIP = to.trim();
    }

    public String getFromIP() {
        return fromIP;
    }

    public void setFromIP(String fromIP) {
        this.fromIP = fromIP;
    }

    public String getToIP() {
        return toIP;
    }

    public void setToIP(String toIP) {
        this.toIP = toIP;
    }
}