package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/13/11
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "invoiceNumberData")
public class MInvoiceNumberData {

    private String prefix = "";
    private boolean withDate;
    private boolean withCode;
    private String fourDigitNumber;

    private String date = "";
    private String code = "";

    public MInvoiceNumberData() {
    }

    public MInvoiceNumberData(InvoiceNumberData invoiceNumberData) {

        if (invoiceNumberData != null) {
            this.prefix = invoiceNumberData.getPrefix();
            this.withDate = invoiceNumberData.isWithDate();
            this.withCode = invoiceNumberData.isWithClient();
            this.fourDigitNumber = invoiceNumberData.getFourDigitNumber();

            this.date = invoiceNumberData.getDate();
            this.code = invoiceNumberData.getClientCode();
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isWithDate() {
        return withDate;
    }

    public void setWithDate(boolean withDate) {
        this.withDate = withDate;
    }

    public boolean isWithCode() {
        return withCode;
    }

    public void setWithCode(boolean withCode) {
        this.withCode = withCode;
    }

    public String getFourDigitNumber() {
        return fourDigitNumber;
    }

    public void setFourDigitNumber(String fourDigitNumber) {
        this.fourDigitNumber = fourDigitNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
