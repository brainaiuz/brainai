package com.edatasite.workforce.gwt.core.client.enums;

/**
 * Created by Dilshod Madrahimov on 9/13/15 5:45 PM
 */
public enum ImportStatusEnum {
    IN_PROCESS("In Process"),
    FAILED("Failed"),
    COMPLETED("Completed");

    String code;

    ImportStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
