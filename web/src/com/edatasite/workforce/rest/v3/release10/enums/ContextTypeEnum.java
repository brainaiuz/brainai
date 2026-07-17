package com.edatasite.workforce.rest.v3.release10.enums;

public enum ContextTypeEnum {
    EXPENSE("EXPENSE", "EXPENSE_STATUS"),
    LEAVE_REQUEST("LEAVE_REQUEST", "_SICK_STATUS"),
    BENEFIT_REQUEST("BENEFIT_REQUEST", "_BENEFIT_REQUEST_STATUSES"),
    INVOICE("INVOICE", "INVOICE_STATUS"),
    TASK("TASK", "_TASK_STATUS"),
    TIME_SHEET("TIME_SHEET", "_TIME_SHEET_APPROVAL_SESSION_STATUS"),
    CRM_TASK("CRM_TASK", "_CRM_TASK_STATUS"),
    PLACEMENT("PLACEMENT", "_PLACEMENT_STATUS"),
    PROJECT("PROJECT", "_PROJECT_STATUS"),
    LEAD("LEAD", "_LEAD_STATUS"),
    RFQ("RFQ", "RFQ_STATUS"),
    EMPLOYEE("EMPLOYEE", "_EMPLOYEE_STATUS"),
    CANDIDATE("CANDIDATE", "_CANDIDATE_STATUS"),
    VACANCY("VACANCY", "VACANCY_STATUSES"),
    PAYMENT("PAYMENT", "_PAYMENT_STATUS");

    private String code;
    private String parentStatusCode;

    ContextTypeEnum(String code, String parentStatusCode) {
        this.code = code;
        this.parentStatusCode = parentStatusCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentStatusCode() {
        return parentStatusCode;
    }

    public void setParentStatusCode(String parentStatusCode) {
        this.parentStatusCode = parentStatusCode;
    }
}
