package com.edatasite.workforce.gwt.core.client.enums;



public enum MessengerType{

    WHATSAPP("WHATSAPP");

    String code;

    MessengerType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
