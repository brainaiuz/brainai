package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 11.07.2009
 * Time: 15:32:00
 * To change this template use File | Settings | File Templates.
 */
public class QuoteSummary implements IsSerializable {

    private String customerName = "";
    private String quoteNumber = "";
    private Date date;
    private String status = "";
    private String prospectAmount = "";
    private static int nextId = 0;

    public QuoteSummary() {
        nextId++;
    }

    public QuoteSummary(String customerName, String quoteNumber, Date date, String status, String prospectAmount) {
        this.customerName = customerName;
        this.quoteNumber = quoteNumber;
        this.date = date;
        this.status = status;
        this.prospectAmount = prospectAmount;
        nextId++;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProspectAmount() {
        return prospectAmount;
    }

    public void setProspectAmount(String prospectAmount) {
        this.prospectAmount = prospectAmount;
    }

    public static int getNextId() {
        return nextId;
    }
}
