package com.edatasite.workforce.rest.v3.release10.core.to;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RelationDto {
    @NotNull(message = "type is required")
    @Pattern(regexp = "PROJECT|CONTRACT|TASK|ISSUE|event|contact|lead|account|opportunity|case|EMAIL_TRACKER|MEETING_MINUTES|BOOKING|salequote|saleorder|REQUEST_FOR_PURCHASE|STOCK_TRANSFER|requestforquote|saleinvoice|product|candidate|COURCE_SCHEDULE|EMAIL_FILTER|EMPLOYEE|DEPARTMENT|client|SUPPLIER|TYPE_PURCHASE_ORDER|TYPE_GDN|TYPE_SHIPPING_DATA|TYPE_PURCHASE_INVOICE|profit|cost|STUDENT|LEAVE_REQUEST|EXPENSE_CLAIM|ADDITIONAL_PAYMENT|campaign|WORKFLOWWORKFLOW_ALERT|WORKFLOW_SMS_ALERT|TYPE_WORKFLOW_TELEGRAM_ALERT|WORKFLOW_EMPLOYEE|WORKFLOW_INVOICE|PAYRUN|CASH_ADVANCE|CERTIFICATE|CERTIFICATE_OF_EMPLOYMENT|CS_STUDENT|SMS|EMPLOYEE_STEP|VACANCY|PLACEMENT|manualjournal|BANK_TRANSFER|BATCH_PAYMENT|PRE_PAYMENT|CUSTOM_FORM_ITEM|personal|business|department|project|company|GROUP_GOAL",
            message = "type must be one of PROJECT/CONTRACT/TASK/ISSUE/event/contact/lead/account/opportunity/case/EMAIL_TRACKER/MEETING_MINUTES/BOOKING/salequote/saleorder/REQUEST_FOR_PURCHASE/STOCK_TRANSFER/requestforquote/saleinvoice/product/candidate/COURCE_SCHEDULE/EMAIL_FILTER/EMPLOYEE/DEPARTMENT/client/SUPPLIER/TYPE_PURCHASE_ORDER/TYPE_GDN/TYPE_SHIPPING_DATA/TYPE_PURCHASE_INVOICE/profit/cost/STUDENT/LEAVE_REQUEST/EXPENSE_CLAIM/ADDITIONAL_PAYMENT/campaign/WORKFLOWWORKFLOW_ALERT/WORKFLOW_SMS_ALERT/TYPE_WORKFLOW_TELEGRAM_ALERT/WORKFLOW_EMPLOYEE/WORKFLOW_INVOICE/PAYRUN/CASH_ADVANCE/CERTIFICATE/CERTIFICATE_OF_EMPLOYMENT/CS_STUDENT/SMS/EMPLOYEE_STEP/VACANCY/PLACEMENT/manualjournal/BANK_TRANSFER/BATCH_PAYMENT/PRE_PAYMENT/CUSTOM_FORM_ITEM/personal/business/department/project/company/GROUP_GOAL")
    private String type;
    @NotNull(message = "item is required")
    private ItemDto item;

    public RelationDto() {
    }

    public RelationDto(String type, ItemDto item) {
        this.type = type;
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ItemDto getItem() {
        return item;
    }

    public void setItem(ItemDto item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelationDto)) return false;

        RelationDto that = (RelationDto) o;

        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getItem() != null ? !getItem().equals(that.getItem()) : that.getItem() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getItem() != null ? getItem().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RelationDto{" +
                "type='" + type + '\'' +
                ", item=" + item +
                '}';
    }
}
