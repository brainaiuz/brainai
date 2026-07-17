package com.edatasite.workforce.core.enums;

public enum ContextCode {
    WORKSPACE("WORKSPACE"),
    REPORTING("REPORTING"),
    PM("PM"),
    CRM("CRM"),
    DASHBOARD("DASHBOARD"),
    PAYROLL("PAYROLL"),
    DOCUMENTS("DOCUMENTS"),
    ACCOUNTING("ACCOUNTING"),
    SETTINGS("SETTINGS"),
    TRAININGCENTER("TRAININGCENTER"),
    HRMS("HRMS"),
    MYACCOUNT("MYACCOUNT"),
    LOGISTICS("LOGISTICS"),
    DEVELOPMENT("DEVELOPMENT"),
    MYWORKSPACE("MYWORKSPACE");

    String code;

    ContextCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
