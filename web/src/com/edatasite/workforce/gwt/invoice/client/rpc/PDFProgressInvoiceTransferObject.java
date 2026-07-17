package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.PostFormPanel;
import com.google.gwt.core.client.GWT;

import java.math.BigDecimal;

/**
 * Created by Azam on 02/20/20.
 */

public class PDFProgressInvoiceTransferObject {

    public static final String PERCENTAGE = "PERCENTAGE";
    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String CLIENT_CONTACT_ID = "CLIENT_CONTACT_ID";
    public static final String BILL_ADDRESS_ID = "BILL_ADDRESS_ID";
    public static final String MAIL_ADDRESS_ID = "MAIL_ADDRESS_ID";
    public static final String PROJECT_NAME = "PROJECT_NAME";
    public static final String INVOICE_DATE = "INVOICE_DATE";
    public static final String DUE_DATE = "DUE_DATE";
    public static final String TOTAL = "TOTAL";
    public static final String QOUTE_TOTAL = "QOUTE_TOTAL";
    public static final String SUBTOTAL = "SUBTOTAL";
    public static final String ITEM_NAME = "ITEM_NAME";
    public static final String QTY = "QTY";
    public static final String UNIT_PRICE = "UNIT_PRICE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String TOTAL_AMOUNT = "TOTAL_AMOUNT";
    public static final String LENGTH = "LENGTH";
    public static final String PROGRESS_INVOICING_TYPE = "PROGRESS_INVOICING_TYPE";
    public static final String QUOTE_NUMBER = "QUOTE_NUMBER";
    public static final String PDF_TEMPLATE_ID = "PDF_TEMPLATE_ID";
    public static final String PROJECT_ID = "PROJECT_ID";

    public PDFProgressInvoiceTransferObject() {
    }

    public PDFProgressInvoiceTransferObject(PostFormPanel panel, NewInvoice[] invoiceData, NewInvoice newInvoice) {
        setTransferData(panel, invoiceData, newInvoice);
    }

    private void setTransferData(PostFormPanel panel, NewInvoice[] invoiceData, NewInvoice newInvoice) {
        Integer itemsCount = 0;
        for (Integer i = 0; i <= invoiceData.length - 1; i++) {
            NewInvoice item = invoiceData[i];
            itemsCount++;
            panel.setParameter(INVOICE_DATE + i, DateUtils.format(item.getInvoiceDate()));
            panel.setParameter(DUE_DATE + i, DateUtils.format(item.getDueDate()));
            if (item.getConvertedPercent() != null) {
                panel.setParameter(PERCENTAGE + i, getMoneyFormat(item.getConvertedPercent()));
            }
            if (item.getSubtotal() != null) {
                panel.setParameter(SUBTOTAL + i, getMoneyFormat(item.getSubtotal()));
            }
            if (item.getTotal() != null) {
                panel.setParameter(TOTAL + i, getMoneyFormat(item.getTotal()));
            }
            if (item.getTotalInInvoiceCurrency() != null) {
                panel.setParameter(TOTAL_AMOUNT + i, getMoneyFormat(item.getTotalInInvoiceCurrency()));
            }

            NewInvoiceItem invoiceItem = item.getItems()[0];
            if (invoiceItem != null) {
                panel.setParameter(ITEM_NAME + i, invoiceItem.getItemName() != null ? invoiceItem.getItemName() : "");
                panel.setParameter(DESCRIPTION + i, invoiceItem.getDescription() != null ? invoiceItem.getDescription() : "");
                panel.setParameter(QTY + i, invoiceItem.getQuantity() != null ? getMoneyFormat(invoiceItem.getQuantity()) : "");
                panel.setParameter(UNIT_PRICE + i, invoiceItem.getUnitPrice() != null ? getMoneyFormat(invoiceItem.getUnitPrice()) : "");
                panel.setParameter(PROJECT_NAME + i, invoiceItem.getProject() != null && invoiceItem.getProject().getName() != null ? invoiceItem.getProject().getName() : "");
            }
        }

        panel.setParameter(LENGTH, itemsCount.toString());
        for (NewInvoice invoice : invoiceData) {
            if (invoice.getClientID() != null) {
                panel.setParameter(CLIENT_ID, invoice.getClientID() + "");
            }
            if (invoice.getClientContactID() != null) {
                panel.setParameter(CLIENT_CONTACT_ID, invoice.getClientContactID() + "");
            }
            if (invoice.getBillAddressID() != null) {
                panel.setParameter(BILL_ADDRESS_ID, invoice.getBillAddressID().toString());
            }
            if (invoice.getMailAddressID() != null) {
                panel.setParameter(MAIL_ADDRESS_ID, invoice.getMailAddressID().toString());
            }
            panel.setParameter(PROGRESS_INVOICING_TYPE, invoice.getProgressInvoicingType());
        }
        if (newInvoice.getProgressInvoicingType() != null && !newInvoice.getProgressInvoicingType().isEmpty()) {
            panel.setParameter(PROGRESS_INVOICING_TYPE, newInvoice.getProgressInvoicingType());
        }

        if (newInvoice.getTotal() != null) {
            panel.setParameter(QOUTE_TOTAL, getMoneyFormat(newInvoice.getTotal()));
        }
        if (newInvoice.getInvoiceNumber() != null) {
            panel.setParameter(QUOTE_NUMBER, newInvoice.getInvoiceNumber());
        }
        if (newInvoice.getRelatedProjectID() != null) {
            panel.setParameter(PROJECT_ID, String.valueOf(newInvoice.getRelatedProjectID()));
        }
        panel.setParameter(PDF_TEMPLATE_ID, newInvoice.getPdfTemplateID() != null ? newInvoice.getPdfTemplateID().toString() : "");
    }

    private String getMoneyFormat(BigDecimal value) {
        return String.valueOf(value.setScale(Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }
}
