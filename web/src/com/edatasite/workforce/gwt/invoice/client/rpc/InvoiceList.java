package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;

public class InvoiceList extends ListResult<NewInvoice> {

    public static final String ID = "Id";
    public static final String INVOICE_NUMBER = "invoiceNumber";
    public static final String INVOICE_DATE = "invoiceDate";
    public static final String DUE_DATE = "dueDate";
    public static final String CLIENT = "client";
    public static final String CURRENCY = "currency";
    public static final String DUE_AMOUNT = "dueAmount";
    public static final String PAID_AMOUNT = "paidAmount";
    public static final String ORIGINAL_AMOUNT = "originalAmount";
    public static final String STATUS = "status";
    public static final String INTRODUCTION = "introduction";
    public static final String RELATED_PROJECT = "relatedProject";
    public static final String CREATOR = "creator";
    public static final String MANAGER = "manager";
    public static final String SUPPLIER = "supplier";
    public static final String REFERENCE = "reference";
    public static final String PO_NUMBER = "ponumber";
    public static final String OPPORTUNITY_NUMBER = "opportunity";
    public static final String QUOTE_NUMBER = "quotenumber";
    public static final String SUB_TOTAL = "subTotal";
    public static final String TAX_TOTAL = "taxTotal";
    public static final String BASE_TOTAL = "baseTotal";
    public static final String NET_AMOUNT_TOTAL = "netAmountTotal";
    public static final String CUSTOMER = "customer";
    public static final String OPPORTUNITY = "opportunity";
    public static final String IN_TARGET = "inTarget";
    public static final String REMAINING_BALANCE = "remainingBalance";
    public static final String CLIENT_VAT_NUMBER = "clientVatNumber";
    public static final String CREATED_DATE = "createdDate";
    public static final String UPDATED_DATE = "updatedDate";
    public static final String ZATCA_STATUS = "zatcaStatus";
    private boolean nimbleCommerceEnabled;
    private boolean customInvoiceImportEnabled;

    public InvoiceList() {
    }

    public InvoiceList(ArrayList<NewInvoice> list, int total) {
        super(list, total);
    }

    public TypeItem[] getAsTypeItem(int tmp) {
        ArrayList<TypeItem> items = new ArrayList<>();
        if (getList() != null && getList().size() > 0) {
            for (NewInvoice invoice : getList()) {
                items.add(invoice.getAsTypeItem());
            }
        }
        return items.toArray(new TypeItem[]{});
    }

    public boolean isNimbleCommerceEnabled() {
        return nimbleCommerceEnabled;
    }

    public void setNimbleCommerceEnabled(boolean nimbleCommerceEnabled) {
        this.nimbleCommerceEnabled = nimbleCommerceEnabled;
    }

    public boolean isCustomInvoiceImportEnabled() {
        return customInvoiceImportEnabled;
    }

    public void setCustomInvoiceImportEnabled(boolean customInvoiceImportEnabled) {
        this.customInvoiceImportEnabled = customInvoiceImportEnabled;
    }
}
