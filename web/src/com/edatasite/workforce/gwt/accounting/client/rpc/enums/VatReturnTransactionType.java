package com.edatasite.workforce.gwt.accounting.client.rpc.enums;

public enum VatReturnTransactionType {
    SALES_INVOICE("Sales Invoice"),
    CREDIT_NOTE("Credit Note"),
    PURCHASE_INVOICE("Purchase Invoice"),
    DEBIT_NOTE("Debit Note"),
    EXPENSE("Expense Claim"),
    BILL_OF_ENTRY("Bill of Entry"),
    REVERSE_CHARGE("Reverse charge"),
    CASH_RECEIPT("Cash Receipt"),
    CASH_PAYMENT("Cash Payment"),
    RECEIVE_MONEY("Bank Receipt"),
    SPEND_MONEY("Bank Payment"),
    ADJUSTMENT("Adjustment");

    private String title;

    VatReturnTransactionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
