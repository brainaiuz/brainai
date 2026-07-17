package com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.tag.InvoiceDate;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.tag.InvoiceTaxAmount;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.tag.InvoiceTotalAmount;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.tag.Seller;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.tag.TaxNumber;

import java.util.Base64;

public class QRBarcodeEncoder {

    public static String encode(
            Seller seller,
            TaxNumber taxNumber,
            InvoiceDate invoiceDate,
            InvoiceTotalAmount invoiceTotalAmount,
            InvoiceTaxAmount invoiceTaxAmount) {
        return toBase64(toTLV(seller, taxNumber, invoiceDate, invoiceTotalAmount, invoiceTaxAmount));
    }

    private static String toTLV(
            Seller seller,
            TaxNumber taxNumber,
            InvoiceDate invoiceDate,
            InvoiceTotalAmount invoiceTotalAmount,
            InvoiceTaxAmount invoiceTaxAmount) {
        return seller.toString()
                + taxNumber.toString()
                + invoiceDate.toString()
                + invoiceTotalAmount.toString()
                + invoiceTaxAmount.toString();
    }

    private static String toBase64(String tlvString) {
        return Base64.getEncoder().encodeToString(tlvString.getBytes());
    }

}
