package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created Abdurakhmonov Farrukh on 15.01.2018.
 */
public enum ApprovalStatusEnum {
    LEAVE("LEAVE"),
    BENEFIT("BENEFIT"),
    OTHER("OTHER"),
    CASH_ADVANCED("CASH_ADVANCED"),
    EXPENSES_CLAIM("EXPENSES_CLAIM");

    private String status;

    ApprovalStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ApprovalStatusEnum getStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return ApprovalStatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
