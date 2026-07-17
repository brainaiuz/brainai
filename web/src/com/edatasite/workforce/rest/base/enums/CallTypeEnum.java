package com.edatasite.workforce.rest.base.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 3/31/15.
 */
public enum CallTypeEnum implements IsSerializable {

    INBOUND("INBOUND", "Inbound"),
    OUTBOUND("OUTBOUND", "Outbound"),
    INCOMING("INCOMING", "Incoming"),
    OUTGOING("OUTGOING", "Outgoing"),
    REJECTED("REJECTED", "Rejected"),
    UNKNOWN("UNKNOWN", "Unknown");

    private String code;
    private String name;

    CallTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
