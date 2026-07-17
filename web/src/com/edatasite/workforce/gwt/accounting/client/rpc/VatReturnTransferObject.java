package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.06.2010
 * Time: 20:49:47
 * To change this template use File | Settings | File Templates.
 */
public class VatReturnTransferObject implements IsSerializable, AccountingConstants {

    private Integer objectID;

    //Vat Return Details
    private String registrationNumber;                     //Registration Number
    private String vatScheme;                              //VAT Scheme
    private String periodCovered;                          //Period covered by the return
    private DateNonConvertable from;                                   //From
    private DateNonConvertable to;                                     //To
    private DateNonConvertable paymentDueDate;                           //This return and any payment are due

    //VAT Calculations
    private BigDecimal vatOnSalesAndOutputs = ZERO; //Box1. VAT due this period on sales and other outputs
    private BigDecimal vatFromECMemberStates = ZERO; //Box2. VAT due in this period on acquisitions from other EC Member States
    private BigDecimal totalVatDue = ZERO; //Box3. Total VAT due (the sum of boxes 1 and 2)
    private BigDecimal vatOnPurchaseAndInputs = ZERO; //Box4. VAT reclaimed in this period on purchases and other inputs (including acquisitions from EC)
    private BigDecimal vatToReclaimFromCustoms = ZERO; //Box5. VAT to Reclaim from Customs

    //Sales and Purchases Excluding VAT
    private BigDecimal totalSalesAndOutputs = ZERO; //Box6. Total value of sales and all other outputs excluding VAT (including supplies to EC)
    private BigDecimal totalPurchasesAndInputs = ZERO; //Box7. Total value of purchases and all other inputs excluding VAT (including acquisitions from EC)

    //EC Supplies and Purchases Excluding VAT
    private BigDecimal totalSupplies = ZERO; //Box8. Total value of all supplies of goods, excluding any VAT, to other EC Member States
    private BigDecimal totalAcquisitions = ZERO; //Box9. Total value of all acquisitions of goods, excluding any VAT, from EC Member States
    private BigDecimal flatRateSchemeVatDifference = ZERO;   //Flat Rate Scheme VAT Difference

    private HashMap<Integer, LinkedList<VatReturnDetailItem>> details;

    private String responseContent;
    private Integer status;

    private BigDecimal paidAmount;

    private boolean flatRate;

    public VatReturnTransferObject() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getVatScheme() {
        return vatScheme;
    }

    public void setVatScheme(String vatScheme) {
        this.vatScheme = vatScheme;
    }

    public String getPeriodCovered() {
        return periodCovered;
    }

    public void setPeriodCovered(String periodCovered) {
        this.periodCovered = periodCovered;
    }

    public DateNonConvertable getFrom() {
        return from;
    }

    public void setFrom(DateNonConvertable from) {
        this.from = from;
    }

    public DateNonConvertable getTo() {
        return to;
    }

    public void setTo(DateNonConvertable to) {
        this.to = to;
    }

    public DateNonConvertable getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(DateNonConvertable paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public BigDecimal getVatOnSalesAndOutputs() {
        return vatOnSalesAndOutputs;
    }

    public void setVatOnSalesAndOutputs(BigDecimal vatOnSalesAndOutputs) {
        this.vatOnSalesAndOutputs = vatOnSalesAndOutputs;
    }

    public BigDecimal getVatFromECMemberStates() {
        return vatFromECMemberStates;
    }

    public void setVatFromECMemberStates(BigDecimal vatFromECMemberStates) {
        this.vatFromECMemberStates = vatFromECMemberStates;
    }

    public BigDecimal getTotalVatDue() {
        return totalVatDue;
    }

    public void setTotalVatDue(BigDecimal totalVatDue) {
        this.totalVatDue = totalVatDue;
    }

    public BigDecimal getVatOnPurchaseAndInputs() {
        return vatOnPurchaseAndInputs;
    }

    public void setVatOnPurchaseAndInputs(BigDecimal vatOnPurchaseAndInputs) {
        this.vatOnPurchaseAndInputs = vatOnPurchaseAndInputs;
    }

    public BigDecimal getVatToReclaimFromCustoms() {
        return vatToReclaimFromCustoms;
    }

    public void setVatToReclaimFromCustoms(BigDecimal vatToReclaimFromCustoms) {
        this.vatToReclaimFromCustoms = vatToReclaimFromCustoms;
    }

    public BigDecimal getTotalSalesAndOutputs() {
        return totalSalesAndOutputs;
    }

    public void setTotalSalesAndOutputs(BigDecimal totalSalesAndOutputs) {
        this.totalSalesAndOutputs = totalSalesAndOutputs;
    }

    public BigDecimal getTotalPurchasesAndInputs() {
        return totalPurchasesAndInputs;
    }

    public void setTotalPurchasesAndInputs(BigDecimal totalPurchasesAndInputs) {
        this.totalPurchasesAndInputs = totalPurchasesAndInputs;
    }

    public BigDecimal getTotalSupplies() {
        return totalSupplies;
    }

    public void setTotalSupplies(BigDecimal totalSupplies) {
        this.totalSupplies = totalSupplies;
    }

    public BigDecimal getTotalAcquisitions() {
        return totalAcquisitions;
    }

    public void setTotalAcquisitions(BigDecimal totalAcquisitions) {
        this.totalAcquisitions = totalAcquisitions;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public boolean isFlatRate() {
        return flatRate;
    }

    public void setFlatRate(boolean flatRate) {
        this.flatRate = flatRate;
    }

    public BigDecimal getFlatRateSchemeVatDifference() {
        return flatRateSchemeVatDifference;
    }

    public void setFlatRateSchemeVatDifference(BigDecimal flatRateSchemeVatDifference) {
        this.flatRateSchemeVatDifference = flatRateSchemeVatDifference;
    }

    public HashMap<Integer, LinkedList<VatReturnDetailItem>> getDetails() {
        return details;
    }

    public void setDetails(HashMap<Integer, LinkedList<VatReturnDetailItem>> details) {
        this.details = details;
    }
}
