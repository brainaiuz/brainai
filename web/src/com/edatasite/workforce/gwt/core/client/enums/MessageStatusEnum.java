package com.edatasite.workforce.gwt.core.client.enums;

/**
 * Created by Azazello on 6/21/2017.
 */
public enum MessageStatusEnum {
    PENDING("Pending"),
    SENT("Sent"),
    BOUNCED("Bounced"),
    IN_PROGRESS("In Progress"),
    DRAFT("Draft"),
    FAILED("Failed");

    String code;

    MessageStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
