package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 8/8/12
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class AgingSummaryItem implements IsSerializable {


    private Integer customerOrSupplierObjectId;
    private String customerOrSupplier;
    private String accountType;
    private ArrayList<AgingSummaryInvoiceItem> invoiceList;
    private BigDecimal balanceForSummaryView;
    private BigDecimal balaceForDetailView;
    private DateNonConvertable openingBalanceDate;
    private BigDecimal manualTransactionsAmount;

    public AgingSummaryItem() {

    }


    public String getCustomerOrSupplier() {
        return customerOrSupplier;
    }

    public void setCustomerOrSupplier(String customerOrSupplier) {
        this.customerOrSupplier = customerOrSupplier;
    }

    public DateNonConvertable getOpeningBalanceDate() {
        return openingBalanceDate;
    }

    public void setOpeningBalanceDate(DateNonConvertable openingBalanceDate) {
        this.openingBalanceDate = openingBalanceDate;
    }

    public ArrayList<AgingSummaryInvoiceItem> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(ArrayList<AgingSummaryInvoiceItem> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public BigDecimal getBalanceForSummaryView() {
        return balanceForSummaryView;
    }

    public void setBalanceForSummaryView(BigDecimal balanceForSummaryView) {
        this.balanceForSummaryView = balanceForSummaryView;
    }

    public BigDecimal getBalaceForDetailView() {
        return balaceForDetailView;
    }

    public void setBalaceForDetailView(BigDecimal balaceForDetailView) {
        this.balaceForDetailView = balaceForDetailView;
    }

    public BigDecimal getManualTransactionsAmount() {
        return manualTransactionsAmount;
    }

    public void setManualTransactionsAmount(BigDecimal manualTransactionsAmount) {
        this.manualTransactionsAmount = manualTransactionsAmount;
    }

    public Integer getCustomerOrSupplierObjectId() {
        return customerOrSupplierObjectId;
    }

    public void setCustomerOrSupplierObjectId(Integer customerOrSupplierObjectId) {
        this.customerOrSupplierObjectId = customerOrSupplierObjectId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
