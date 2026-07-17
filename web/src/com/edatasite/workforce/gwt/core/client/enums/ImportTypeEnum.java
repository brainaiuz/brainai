package com.edatasite.workforce.gwt.core.client.enums;

import java.io.Serializable;

public enum ImportTypeEnum implements Serializable {
    BATCH_SALES_ORDER("Batch Sales Order"),
    BATCH_SALES_INVOICE("Batch Sales Invoice"),
    BATCH_SALES_INVOICE_PAYMENT("Batch Sales Invoice Payment"),
    EMPLOYEE("Employee"),
    CANDIDATE("Candidate"),
    CRM_ACCOUNT("Crm Account"),
    CUSTOMER("Customer"),
    SUPPLIER("Supplier"),
    VCARD_CONTACT("VCard Contact"),
    CONTACT("Contact"),
    LEAD("Lead"),
    OPPORTUNITY("Opportunity"),
    NIMBLE_COMMERCE("Nimble Commerce"),
    CUSTOM_INVOICE("Custom Invoice"),
    CHART_OF_ACCOUNTS("Chart of Account"),
    PRODUCT_FROM_PARENT("Product from parent"),
    MANUAL_TRANSACTION("Manual Transaction"),
    MANUAL_TRANSACTION_TALLY("Manual Transaction Tally"),
    ADDITIONAL_PAYMENT("Additional Payment"),
    BANK_TRANSFER_TRANSACTION("Bank Transfer Transaction"),
    IMPORT_TRANSACTIONS("Import Transactions"),
    TRANSACTION("Transaction"),
    BUDGET_MANAGER("Budget Manager"),
    EXPENSE("Expense"),
    COMPANY_EXPENSE("Company Expense"),
    PROJECT("Project"),
    PRODUCT("Product"),
    CASH_RECEIPT("Cash Receipt"),
    CASH_PAYMENT("Cash Payment"),
    BANK_RECEIPT("Bank Receipt"),
    BANK_PAYMENT("Bank Payment"),
    INVENTORY_ITEMS("Inventory Items"),
    ASSEMBLY_ITEMS("Assembly Items"),
    MONTHLY_TIMESHEET("Monthly Timesheet"),
    SCHEDULED_COURSE("Course Schedule"),
    REPORT_DATA("Report data"),
    GROUP_PAYRUN("Group Payrun"),
    PAYMENT("Payment"),
    DEDUCTION("Deduction"),
    PRODUCT_CATEGORIES("Product Categories"),
    BRAND("Brand"),
    PURCHASE_ORDER("PURCHASE_ORDER"),
    ANNUAL_ALLOWANCE("ANNUAL_ALLOWANCE"),
    POSITION("Position"),
    DEPARTMENT("Deprtment"),
    FIXED_ASSETS("Fixed Assets");

    String code;

    ImportTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
