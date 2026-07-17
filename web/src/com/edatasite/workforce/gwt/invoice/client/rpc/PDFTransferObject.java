package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.PostFormPanel;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.math.BigDecimal;

//import static com.edatasite.workforce.gwt.accounting.client.AccountingUtils.get().getMoneyFormat;

public class PDFTransferObject {

    private String GETResultString;

//	static NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    public static final String PDF_TEMPLATE_ID = "PDF_TEMPLATE_ID";

    public static final String INVOICE_ID = "INVOICE_ID";
    public static final String INVOICE_NUMBER = "INVOICE_NUMBER";
//    public static final String INVOICE_NUMBER_YEAR = "INVOICE_NUMBER_YEAR";
//    public static final String INVOICE_NUMBER_MONTH = "INVOICE_NUMBER_MONTH";
//    public static final String INVOICE_NUMBER_DAY = "INVOICE_NUMBER_DAY";
//    public static final String INVOICE_NUMBER_CLIENT = "INVOICE_NUMBER_CLIENT";

    public static final String INVOICE_TYPE = "INVOICE_TYPE";//0 = Product Invoice, 1 = Service Invoice

    public static final String CONVERTED_ITEM_ID = "CONVERTED_ITEM_ID";
    public static final String CONVERTION_PERCENT = "CONVERTION_PERCENT";
    public static final String PROGRESS_INVOICING = "PROGRESS_INVOICING";

    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String CLIENT_CONTACT_ID = "CLIENT_CONTACT_ID";
    public static final String BILL_ADDRESS_ID = "BILL_ADDRESS_ID";
    public static final String MAIL_ADDRESS_ID = "MAIL_ADDRESS_ID";
    //    public static final String CONSIGNOR_ID = "CONSIGNOR_ID";
//    public static final String CONSIGNEE_ID = "CONSIGNEE_ID";
    public static final String CLIENT_ID_PURCHASE = "CLIENT_ID_PURCHASE";
    public static final String PUR_CL_BILL_ADDR_ID = "PUR_CL_BILL_ADDR_ID";
    public static final String PUR_CL_MAIL_ADDR_ID = "PUR_CL_MAIL_ADDR_ID";
    //    public static final String MANAGER_ID = "MANAGER_ID";
    public static final String CURRENT_APPROVER_ID = "CURRENT_APPROVER_ID";
    public static final String PROJECT_ID = "PROJECT_ID";
    public static final String PROJECT_NAME = "PROJECT_NAME";
    public static final String QUOTE_NUMBER = "QUOTE_NUMBER";
    public static final String PO_NUMBER = "PO_NUMBER";
    public static final String NUMBER = "NUMBER";
    public static final String DATE = "DATE";
    public static final String ADJUSTMENT_ACCOUNT_NAME = "ADJUSTMENT_ACCOUNT_NAME";
    public static final String MEMO = "MEMO";
    public static final String REFERENCE = "REFERENCE";
    public static final String PERIOD_START = "PERIOD_START";
    public static final String PERIOD_END = "PERIOD_END";
    public static final String DUE_DATE = "DUE_DATE";
    public static final String INVOICE_DATE = "INVOICE_DATE";
    public static final String CANCEL_DATE = "CANCEL_DATE";
    //    public static final String WAREHOUSE_ID = "WAREHOUSE_ID";
    public static final String PREVIOUS_BALANCE = "PREVIOUS_BALANCE";
    public static final String PAYMENTS_RECEIVED = "PAYMENTS_RECEIVED";
    public static final String INVOICE_DUE_TERMS_ID = "INVOICE_DUE_TERMS_ID";

    public static final String PRODUCT_DISCOUNT = "PRODUCT_DISCOUNT";
    public static final String PRODUCT_DISCOUNT_FIXED = "PRODUCT_DISCOUNT_FIXED";
    public static final String PRODUCT_DOUBLE_DISCOUNT = "PRODUCT_DOUBLE_DISCOUNT";
    public static final String PRODUCT_DOUBLE_DISCOUNT_FIXED = "PRODUCT_DOUBLE_DISCOUNT_FIXED";
    public static final String EXCHANGE_RATE = "EXCHANGE_RATE";
    public static final String CURRENCY = "CURRENCY";
    //    public static final String AMOUNT_TYPE = "AMOUNT_TYPE";
    public static final String NOTES = "NOTES";
    public static final String NOTES_EMPLOYEE = "NOTES_EMPLOYEE";
    public static final String NOTES_DATE = "NOTES_DATE";
    public static final String NOTES_LENGTH = "NOTES_LENGTH";
    public static final String PAYMENT_INSTRUCTION = "PAYMENT_INSTRUCTION";
    public static final String PAYMENT_TERMS = "PAYMENT_TERMS";
    public static final String PAYMENT_TYPE = "PAYMENT_TYPE";
    public static final String SHIPPING_METHOD_ID = "SHIPPING_METHOD_ID";
    public static final String SHIPPING_TERMS = "SHIPPING_TERMS";
    public static final String REQUESTIONED_BY = "REQUESTIONED_BY";
    public static final String INVOICE_STATUS = "INVOICE_STATUS";
    public static final String BANK_ACCOUNT_ID = "BANK_ACCOUNT_ID";
    public static final String TOTAL = "TOTAL";
    public static final String TOTAL_DISCOUNT = "TOTAL_DISCOUNT";
    public static final String TOTAL_IN_INVOICE_CURRENCY = "TOTAL_IN_INVOICE_CURRENCY";
    public static final String TOTAL_TAXES = "TOTAL_TAXES";
    public static final String SUBTOTAL = "SUBTOTAL";
    public static final String INTRODUCTION = "INTRODUCTION";
    public static final String IS_PROJECT_BASED_INVOICE = "IS_PROJECT_BASED_INVOICE";
    public static final String BILL_EXP_TOTAL = "BILL_EXP_TOTAL";
    public static final String BILL_EXP_TAX_TOTAL = "BILL_EXP_TAX_TOTAL";

    public static final String ITEM_ID = "ITEM_ID";
    public static final String ITEM_NAME = "ITEM_NAME";
    public static final String BRAND_NAME = "BRAND_NAME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String ITEM_TYPE = "ITEM_TYPE";
    public static final String QTY = "QTY";
    public static final String UNIT_MEASUREMENT = "UNIT_MEASUREMENT";
    public static final String UNIT_MEASUREMENT_DESCRIPTION = "UNIT_MEASUREMENT_DESCRIPTION";
    public static final String UNIT_PRICE = "UNIT_PRICE";
    public static final String ORIGINAL_PRICE = "ORIGINAL_PRICE";
    public static final String NET = "NET";
    public static final String NET_AMOUNT = "NET_AMOUNT";
    public static final String VAT_ID = "VAT_ID";
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String TAX_AMOUNT = "TAX_AMOUNT";
    public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
    public static final String ITEM_CATEGORY = "ITEM_CATEGORY";
    public static final String ITEM_WAREHOUSE = "ITEM_WAREHOUSE";
    public static final String ITEM_USED_QTY = "ITEM_USED_QTY";
    public static final String ITEM_NEW_QTY = "ITEM_NEW_QTY";
    public static final String ITEM_TOTAL_QTY = "ITEM_TOTAL_QTY";
    public static final String ATTACHMENT = "ATTACHMENT";
    public static final String QUOTE_ITEM_ID = "QUOTE_ITEM_ID";

    public static final String LENGTH = "LENGTH";

    //EXPENSE REPORT Table
    public static final String ITEM_EXPENSE_CATEGORY = "ITEM_EXPENSE_CATEGORY";
    public static final String ITEM_EXPENSE_DESCRIPTION = "ITEM_EXPENSE_DESCRIPTION";
    public static final String ITEM_EXPENSE_TOTAL_AMOUNT = "ITEM_EXPENSE_TOTAL_AMOUNT";
    public static final String ITEM_EXPENSE_TOTAL_WITH_MARKUP = "ITEM_EXPENSE_TOTAL_WITH_MARKUP";
    public static final String ITEM_EXPENSE_MARKUP_AMOUNT = "ITEM_EXPENSE_MARKUP_AMOUNT";
    public static final String ITEM_EXPENSE_MARKUP_TAX_AMOUNT = "ITEM_EXPENSE_MARKUP_TAX_AMOUNT";
    public static final String EXPENSE_LENGTH = "EXPENSE_LENGTH";

    public static final String TOTALTAX_AMOUNT = "TOTALTAX_AMOUNT";
    public static final String TOTALTAX_NAME = "TOTALTAX_NAME";
    public static final String TOTALTAX_ID = "TOTALTAX_ID";
    public static final String TOTALTAX_PERCENT = "TOTALTAX_PERCENT";
    public static final String TOTALTAX_LENGHT = "TOTALTAX_LENGHT";

    public static final String FIELD_DATA_TYPE = "FIELD_DATA_TYPE";
    public static final String FIELD_NAME = "FIELD_NAME";
    public static final String FIELD_STRING_VALUE = "FIELD_STRING_VALUE";
    public static final String FIELD_SELECTED_ID = "FIELD_SELECTED_ID";
    public static final String FIELD_UI_TYPE = "FIELD_UI_TYPE";
    public static final String FIELD_DATA_VALUE = "FIELD_DATA_VALUE";
    public static final String CUSTOM_FIELDS_SIZE = "CUSTOM_FIELD_SIZE";
    public static final String ITEM_FIELD_DATA_TYPE = "ITEM_FIELD_DATA_TYPE";
    public static final String ITEM_FIELD_NAME = "ITEM_FIELD_NAME";
    public static final String ITEM_FIELD_STRING_VALUE = "ITEM_FIELD_STRING_VALUE";
    public static final String ITEM_FIELD_DATA_VALUE = "ITEM_FIELD_DATA_VALUE";
    public static final String ITEM_CUSTOM_FIELDS_SIZE = "ITEM_CUSTOM_FIELD_SIZE";
    public static final String ORDER_BASEINVOICE_ORDER_IDS = "ORDER_BASEINVOICE_ORDER_IDS";

    public PDFTransferObject() {

    }

    public PDFTransferObject(PostFormPanel panel, NewInvoice invoiceData) {
        setTransferData(panel, invoiceData);
    }

    public PDFTransferObject(PostFormPanel panel, AdjustmentItem adjustmentItem) {
        setTransferData(panel, adjustmentItem);
    }

    private void setTransferData(PostFormPanel panel, NewInvoice invoiceData) {

        panel.setParameter(PDF_TEMPLATE_ID, invoiceData.getPdfTemplateID() != null ? invoiceData.getPdfTemplateID().toString() : "");

        panel.setParameter(INVOICE_ID, invoiceData.getID() != null ? String.valueOf(invoiceData.getID()) : "");
        panel.setParameter(CONVERTED_ITEM_ID, invoiceData.getConvertedItemID() != null ? String.valueOf(invoiceData.getConvertedItemID()) : "");
        if (invoiceData.getConvertedPercent() != null) {
            panel.setParameter(CONVERTION_PERCENT, getMoneyFormat(invoiceData.getConvertedPercent()));
            panel.setParameter(PROGRESS_INVOICING, String.valueOf(invoiceData.isProgressInvoicing()));
        }
        panel.setParameter(NET_AMOUNT, getMoneyFormat(invoiceData.getSubtotal().subtract(invoiceData.getTotalTaxes()).subtract(invoiceData.getTotalDiscount())));
        panel.setParameter(INVOICE_NUMBER, invoiceData.getInvoiceNumber());
        panel.setParameter(CLIENT_ID, invoiceData.getClientID() + "");

        if (invoiceData.getInvoiceType() != null) {
            panel.setParameter(INVOICE_TYPE, invoiceData.getInvoiceType().toString());
        }

        if (invoiceData.getBillAddressID() != null) {
            panel.setParameter(BILL_ADDRESS_ID, invoiceData.getBillAddressID().toString());
        }
        if (invoiceData.getOrderBaseinvoicedOrderIds() != null) {
            panel.setParameter(ORDER_BASEINVOICE_ORDER_IDS, invoiceData.getOrderBaseinvoicedOrderIds());
        }
        if (invoiceData.getMailAddressID() != null) {
            panel.setParameter(MAIL_ADDRESS_ID, invoiceData.getMailAddressID().toString());
        }

        if (invoiceData.getClientItem() != null) {
            if (invoiceData.getClientItem().getId() != null) {
                panel.setParameter(CLIENT_ID_PURCHASE, invoiceData.getClientItem().getId().toString());
            }
            if (invoiceData.getClientItem().getBillAddressID() != null) {
                panel.setParameter(PUR_CL_BILL_ADDR_ID, invoiceData.getClientItem().getBillAddressID().toString());
            }
            if (invoiceData.getClientItem().getMailAddressID() != null) {
                panel.setParameter(PUR_CL_MAIL_ADDR_ID, invoiceData.getClientItem().getMailAddressID().toString());
            }
        }

        panel.setParameter(PO_NUMBER, invoiceData.getPoNumber());
        panel.setParameter(QUOTE_NUMBER, invoiceData.getQuoteNumber());
        panel.setParameter(REFERENCE, invoiceData.getReference() != null ? URL.encodeQueryString(invoiceData.getReference()) : invoiceData.getReference());
        panel.setParameter(PERIOD_START, Utils.getStartDateNCForFilter(invoiceData.getPeriodStart()));
        panel.setParameter(PERIOD_END, Utils.getEndDateNCForFilter(invoiceData.getPeriodEnd()));
        panel.setParameter(INVOICE_DATE, Utils.getStartDateNCForFilter(invoiceData.getInvoiceDate()));
        panel.setParameter(DUE_DATE, Utils.getEndDateNCForFilter(invoiceData.getDueDate()));
        panel.setParameter(EXCHANGE_RATE, getExtendedMoneyFormat(invoiceData.getExchageRate(), AccountingUtils.getExRateScale()));
        panel.setParameter(CURRENCY, invoiceData.getCurrencyID() + "");

        if (invoiceData.getInvoiceTermsItem() != null && invoiceData.getInvoiceTermsItem().getId() != null) {
            panel.setParameter(INVOICE_DUE_TERMS_ID, String.valueOf(invoiceData.getInvoiceTermsItem().getId()));
        }
        if (invoiceData.getRelatedProjectID() != null) {
            panel.setParameter(PROJECT_ID, String.valueOf(invoiceData.getRelatedProjectID()));
        }
        if (invoiceData.getShippingMethodID() != null) {
            panel.setParameter(SHIPPING_METHOD_ID, String.valueOf(invoiceData.getShippingMethodID()));
        }
        if (invoiceData.getPreviosBalance() != null) {
            panel.setParameter(PREVIOUS_BALANCE, getMoneyFormat(invoiceData.getPreviosBalance()));
        }
        if (invoiceData.getPaymentsReceived() != null) {
            panel.setParameter(PAYMENTS_RECEIVED, getMoneyFormat(invoiceData.getPaymentsReceived()));
        }
        if (invoiceData.getCancelDate() != null) {
            panel.setParameter(CANCEL_DATE, Utils.getEndDateNCForFilter(invoiceData.getCancelDate()));
        }
//        if (invoiceData.getManager() != null) {
//            panel.setParameter(MANAGER_ID, String.valueOf(invoiceData.getManager().getId()));
//        }
        if (invoiceData.getCurrentApprover() != null) {
            panel.setParameter(CURRENT_APPROVER_ID, String.valueOf(invoiceData.getCurrentApproverSelectItem().getId()));
        }
        if (invoiceData.getClientContactID() != null) {
            panel.setParameter(CLIENT_CONTACT_ID, String.valueOf(invoiceData.getClientContactID()));
        }
        int fieldCount = 0;
        if (invoiceData.getCustomFieldItems() != null && invoiceData.getCustomFieldItems().size() > 0) {
            for (Integer i = 0; i < invoiceData.getCustomFieldItems().size(); i++) {
                CompanyCustomFieldItem item = invoiceData.getCustomFieldItems().get(i);
                if (item != null) {
                    fieldCount++;
                    panel.setParameter(FIELD_DATA_TYPE + i, item.getDataType());
                    panel.setParameter(FIELD_NAME + i, item.getFieldName());
                    panel.setParameter(FIELD_STRING_VALUE + i, item.getFieldStringValue());
                    panel.setParameter(FIELD_SELECTED_ID + i, item.getSelectedId() + "");
                    panel.setParameter(FIELD_UI_TYPE + i, item.getUiType() + "");

                    if (item.getFieldDateNonConvertedValue() != null) {
                        panel.setParameter(FIELD_DATA_VALUE + i, Utils.getStartDateNCForFilter(item.getFieldDateNonConvertedValue().getNonConvertedDate()));
                    }
                }
            }
            panel.setParameter(CUSTOM_FIELDS_SIZE, String.valueOf(fieldCount));
        }

        panel.setParameter(PAYMENT_INSTRUCTION, invoiceData.getPaymentInstruction() != null ? URL.encodeQueryString(invoiceData.getPaymentInstruction()) : invoiceData.getPaymentInstruction());
        if (invoiceData.getPaymentMethodID() != null) {
            panel.setParameter(PAYMENT_TYPE, String.valueOf(invoiceData.getPaymentMethodID()));
        }
        panel.setParameter(PAYMENT_TERMS, invoiceData.getPaymentTerms() != null ? URL.encodeQueryString(invoiceData.getPaymentTerms()) : invoiceData.getPaymentTerms());
        panel.setParameter(SHIPPING_TERMS, invoiceData.getShippingTerms() != null ? URL.encodeQueryString(invoiceData.getShippingTerms()) : invoiceData.getShippingTerms());
        if (invoiceData.getRequisitionedBy() != null) {
            panel.setParameter(REQUESTIONED_BY, String.valueOf(invoiceData.getRequisitionedBy().getId()));
        }
        panel.setParameter(INVOICE_STATUS, invoiceData.getStatusCode());
        panel.setParameter(INTRODUCTION, invoiceData.getIntroduction() != null ? URL.encodeQueryString(invoiceData.getIntroduction()) : invoiceData.getIntroduction());
        panel.setParameter(IS_PROJECT_BASED_INVOICE, String.valueOf(invoiceData.isProjectBasedInvoice()));
        panel.setParameter(TOTAL, getMoneyFormat(invoiceData.getTotal()));
        if (invoiceData.getTotalDiscount() != null) {
            panel.setParameter(TOTAL_DISCOUNT, getMoneyFormat(invoiceData.getTotalDiscount()));
        }
        if (invoiceData.getTotalInInvoiceCurrency() != null) {
            panel.setParameter(TOTAL_IN_INVOICE_CURRENCY, getMoneyFormat(invoiceData.getTotalInInvoiceCurrency()));
        }
        if (invoiceData.getBankAccount() != null && invoiceData.getBankAccount().getId() != null) {
            panel.setParameter(BANK_ACCOUNT_ID, String.valueOf(invoiceData.getBankAccount().getId()));
        }
        panel.setParameter(BILL_EXP_TOTAL, invoiceData.getBillableExpenseAmount() != null ? getMoneyFormat(invoiceData.getBillableExpenseAmount()) : "0");
        panel.setParameter(BILL_EXP_TAX_TOTAL, invoiceData.getBillableExpenseTaxAmount() != null ? getMoneyFormat(invoiceData.getBillableExpenseTaxAmount()) : "0");
        panel.setParameter(TOTAL_TAXES, getMoneyFormat(invoiceData.getTotalTaxes()));
        panel.setParameter(SUBTOTAL, getMoneyFormat(invoiceData.getSubtotal()));


        Integer itemsCount = 0;

        for (Integer i = 0; i <= invoiceData.getItems().length - 1; i++) {
            NewInvoiceItem item = invoiceData.getItems()[i];
            if (item != null) {
                itemsCount++;
                if (item.getItemID() != null && item.getItemID() > 0) {
                    panel.setParameter(ITEM_ID + i, String.valueOf(item.getItemID()));
                }
                panel.setParameter(ITEM_NAME + i, item.getItemName() != null ? URL.encodeQueryString(item.getItemName()) : item.getItemName());
                panel.setParameter(BRAND_NAME + i, item.getProductBrand() != null ? URL.encodeQueryString(item.getProductBrand()) : "");
                panel.setParameter(DESCRIPTION + i, item.getDescription() != null ? URL.encodeQueryString(item.getDescription()) : item.getDescription());
                panel.setParameter(ITEM_TYPE + i, item.getProductType() != null ? String.valueOf(item.getProductType().intValue()) : "");
                panel.setParameter(QTY + i, item.getQuantity().compareTo(AccountingConstants.ZERO) == 0 ? "null" : getExtendedMoneyFormat(item.getQuantity(), AccountingUtils.getQtyScale()));
                panel.setParameter(UNIT_MEASUREMENT + i, item.getMeasurement() != null ? item.getMeasurement().getName() : " ");
                panel.setParameter(UNIT_MEASUREMENT_DESCRIPTION + i, item.getMeasurement() != null ? item.getMeasurement().getDescription() : "");
                panel.setParameter(UNIT_PRICE + i, item.getUnitPrice().compareTo(AccountingConstants.ZERO) == 0 ? "null" : getExtendedMoneyFormat(item.getUnitPrice(), AccountingUtils.getUnitPriceScale()));
                panel.setParameter(ORIGINAL_PRICE + i, item.getItemOriginalPrice() == null || item.getItemOriginalPrice().compareTo(AccountingConstants.ZERO) == 0 ? "null" : getExtendedMoneyFormat(item.getItemOriginalPrice()));
                panel.setParameter(PRODUCT_DISCOUNT + i, item.getDiscountPercent() != null ? String.valueOf(item.getDiscountPercent().doubleValue()) : "null");
                panel.setParameter(PRODUCT_DISCOUNT_FIXED + i, item.getDiscountAmount() != null ? String.valueOf(item.getDiscountAmount().doubleValue()) : "null");
                panel.setParameter(PRODUCT_DOUBLE_DISCOUNT + i, item.getDoubleDiscountPercent() != null ? String.valueOf(item.getDoubleDiscountPercent().doubleValue()) : "null");
                panel.setParameter(PRODUCT_DOUBLE_DISCOUNT_FIXED + i, item.getDoubleDiscountAmount() != null ? String.valueOf(item.getDoubleDiscountAmount().doubleValue()) : "null");
                panel.setParameter(NET + i, item.getNet() != null ? getExtendedMoneyFormat(item.getNet()) : "0");
                panel.setParameter(VAT_ID + i, (item.getTaxItem() != null && item.getTaxItem().getId() != null) ? String.valueOf(item.getTaxItem().getId()) : "null");
                panel.setParameter(ACCOUNT_NAME + i, item.getAccountName());
                panel.setParameter(TAX_AMOUNT + i, item.getTaxAmount() != null ? getMoneyFormat(item.getTaxAmount()) : "0");
                panel.setParameter(TOTAL_AMOUNT + i, item.getTotalAmount() != null ? getMoneyFormat(item.getTotalAmount()) : "0");
                panel.setParameter(QUOTE_ITEM_ID + i, item.getQuoteItemId() != null ? String.valueOf(item.getQuoteItemId()) : "null");

                if (Utils.isProjectInLineItemEnable()) {
                    panel.setParameter(PROJECT_NAME + i, item.getProject() != null ? item.getProject().getName() : "");
                }
                if (item.getAttachments() != null && !item.getAttachments().isEmpty()) {
                    panel.setParameter(ATTACHMENT + i, item.getAttachments().get(0).getDownloadUrl());
                } else {
                    panel.setParameter(ATTACHMENT + i, null);
                }
                int counter = 0;
                if (item.getCustomFieldItems() != null) {
                    for (CompanyCustomFieldItem item1 : item.getCustomFieldItems()) {
                        if (item1 != null) {
                            panel.setParameter(ITEM_FIELD_DATA_TYPE + i + counter, item1.getDataType());
                            panel.setParameter(ITEM_FIELD_NAME + i + counter, item1.getFieldName());
                            panel.setParameter(ITEM_FIELD_STRING_VALUE + i + counter, item1.getFieldStringValue());
                            if (item1.getFieldDateNonConvertedValue() != null) {
                                panel.setParameter(ITEM_FIELD_DATA_VALUE + i + counter, Utils.getStartDateNCForFilter(item1.getFieldDateNonConvertedValue().getNonConvertedDate()));
                            }
                            counter++;
                        }
                    }
                }
                panel.setParameter(ITEM_CUSTOM_FIELDS_SIZE, String.valueOf(counter));
            }
        }

        panel.setParameter(LENGTH, itemsCount.toString());

        //for Expense
        if (invoiceData.getExpenses() != null) {
            Integer expenseCount = 0;

            for (Integer i = 0; i < invoiceData.getExpenses().size(); i++) {
                BillableExpenseItem item = invoiceData.getExpenses().get(i);
                if (item != null) {
                    expenseCount++;

                    BigDecimal amount = invoiceData.getCurrencyID().equals(item.getCurrencyID()) ? item.getAmountInCurrency() : item.getAmountInBase().multiply(invoiceData.getExchageRate());
                    BigDecimal markUpAmount = invoiceData.getCurrencyID().equals(item.getCurrencyID()) ? item.getMarkupAmount() : item.getMarkupAmountInBase().multiply(invoiceData.getExchageRate());

                    panel.setParameter(ITEM_EXPENSE_CATEGORY + i, item.getAccount() != null ? URL.encodeQueryString(item.getAccount().getName()) : "");
                    panel.setParameter(ITEM_EXPENSE_DESCRIPTION + i, item.getDescription() != null ? URL.encodeQueryString(item.getDescription()) : item.getDescription());
                    panel.setParameter(ITEM_EXPENSE_TOTAL_AMOUNT + i, getExtendedMoneyFormat(amount));
                    panel.setParameter(ITEM_EXPENSE_MARKUP_AMOUNT + i, getExtendedMoneyFormat(markUpAmount));
                    panel.setParameter(ITEM_EXPENSE_TOTAL_WITH_MARKUP + i, getExtendedMoneyFormat(amount.add(markUpAmount)));
                    panel.setParameter(ITEM_EXPENSE_MARKUP_TAX_AMOUNT + i, getExtendedMoneyFormat(invoiceData.getCurrencyID().equals(item.getCurrencyID()) ? item.getMarkupTaxAmount() : item.getMarkupTaxAmountInBase().multiply(invoiceData.getExchageRate())));

                }
            }

            panel.setParameter(EXPENSE_LENGTH, expenseCount.toString());
        }


        if (invoiceData.getHistoryList() != null && invoiceData.getHistoryList().length > 0) {
            itemsCount = 0;
            HistoryListItem[] notes = invoiceData.getHistoryList();
            for (Integer i = 0; i < notes.length; i++) {
                HistoryListItem note = notes[i];
                if (note != null) {
                    itemsCount++;
                    panel.setParameter(NOTES + i, note.getComment() != null ? URL.encodeQueryString(note.getComment()) : note.getComment());
                    panel.setParameter(NOTES_EMPLOYEE + i, note.getEmployee());
                    panel.setParameter(NOTES_DATE + i, Utils.getStartDateNCForFilter(note.getEventDate()));
                }
            }
            panel.setParameter(NOTES_LENGTH, itemsCount.toString());
        }

        int count = 0;
        for (Integer i = 0; i < invoiceData.getTotalTaxItems().length; i++) {
            TotalTaxItem item = invoiceData.getTotalTaxItems()[i];
            panel.setParameter(TOTALTAX_AMOUNT + i, getMoneyFormat(item.getTaxAmount()));
            panel.setParameter(TOTALTAX_NAME + i, item.getTaxItem().getName());
            panel.setParameter(TOTALTAX_PERCENT + i, String.valueOf(item.getTaxItem().getTaxPercent()));
            panel.setParameter(TOTALTAX_ID + i, String.valueOf(item.getTaxItem().getId()));
            count++;
        }
        panel.setParameter(TOTALTAX_LENGHT, String.valueOf(count));
    }

    private void setTransferData(PostFormPanel panel, AdjustmentItem adjustmentItem) {


        panel.setParameter(NUMBER, adjustmentItem.getNumber());
        panel.setParameter(DATE, DateTimeFormat.getLongDateFormat().format(adjustmentItem.getDate().getNonConvertedDate()));
        panel.setParameter(ADJUSTMENT_ACCOUNT_NAME, adjustmentItem.getAccount().getName());
        panel.setParameter(MEMO, adjustmentItem.getMemo());

        Integer itemsCount = 0;

        for (Integer i = 0; i <= adjustmentItem.getProductItems().length - 1; i++) {
            ProductItem item = adjustmentItem.getProductItems()[i];
            if (item != null) {
                itemsCount++;
                panel.setParameter(ITEM_NAME + i, item.getName() != null ? item.getName() : "");
                panel.setParameter(DESCRIPTION + i, item.getDescription() != null ? item.getDescription() : item.getDescription());
                panel.setParameter(ITEM_WAREHOUSE + i, item.getWarehouseName() != null ? item.getWarehouseName() : "");
                panel.setParameter(QTY + i, item.getCurrentQty().compareTo(AccountingConstants.ZERO) == 0 ? "null" : getExtendedMoneyFormat(item.getCurrentQty()));
                panel.setParameter(ITEM_USED_QTY + i, item.getUsedQty().compareTo(AccountingConstants.ZERO) == 0 ? "null" : getExtendedMoneyFormat(item.getUsedQty()));
                panel.setParameter(ITEM_NEW_QTY + i, item.getNewQty().compareTo(AccountingConstants.ZERO) == 0 ? "null" : getExtendedMoneyFormat(item.getNewQty()));
                panel.setParameter(ITEM_TOTAL_QTY + i, item.getTotalQty().compareTo(AccountingConstants.ZERO) == 0 ? "null" : getExtendedMoneyFormat(item.getTotalQty()));
                panel.setParameter(UNIT_PRICE + i, item.getUnitpPrice() != null ? item.getUnitpPrice().toString() : "");
                panel.setParameter(PROJECT_ID + i, item.getProjectName() != null ? item.getProjectName() : "");
            }
        }
        panel.setParameter(LENGTH, itemsCount.toString());
    }

    private String getMoneyFormat(BigDecimal value) {
        return String.valueOf(value.setScale(Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    private String getExtendedMoneyFormat(BigDecimal value) {
        return getExtendedMoneyFormat(value, Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2);
    }

    private String getExtendedMoneyFormat(BigDecimal value, int scale) {
        return String.valueOf(value.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
    }
}
