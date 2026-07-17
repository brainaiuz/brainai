package com.edatasite.workforce.gwt.core.client.rpc;

/**
 * User: Abror Abdukadirov
 * Date: 14.01.2019 19:15
 */
public enum PdfTemplateTypeEnum {
    SALES_INVOICE,
    SALES_QUOTE,
    SALES_ORDER,
    PURCHASE_INVOICE,
    PURCHASE_ORDER,
    EXPENSE_REPORT,
    MANUAL_ENTRY,
    RECEIVABLE_CREDIT_NOTE,
    PAYABLE_CREDIT_NOTE,
    BATCH_RECEIVE_PAYMENT,
    BATCH_PAY_BILL,
    RFQ,
    RFP,
    BANK_RECEIPT,
    BANK_PAYMENT,
    CASH_RECEIPT,
    CASH_PAYMENT,
    GOODS_RECEIVED_NOTES,
    GOODS_DELIVERED_NOTES;

    public static PdfTemplateTypeEnum get(String type) {
        if (type == null) {
            return null;
        }
        for (PdfTemplateTypeEnum value : PdfTemplateTypeEnum.values()) {
            if (type.equals(value.name())) {
                return value;
            }
        }
        return null;
    }
}
