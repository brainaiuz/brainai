package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Ula
 * Date: 04-Dec-2009
 * Time: 18:38:36
 * To change this template use File | Settings | File Templates.
 */
public class IncomeTaxObject extends PaymentDeductionObject {

    private BigDecimal payAdjustment;

    private String taxCode;

    private BigDecimal totalTaxDueToDate;
    private BigDecimal totalAdditionalPay;
    private BigDecimal regulatoryLimit;
    private BigDecimal taxDueEndCurrPeriod;
    private BigDecimal taxNotDeducted;

    private BigDecimal totalFreePay;

    private BigDecimal totalTaxablePayToDate;

    private BigDecimal totalPayToDate;

    private BigDecimal taxDeductedInTheWeek;

    private BigDecimal tax;

    private BigDecimal payInPeriod;

    public BigDecimal getPayAdjustment() {
        return payAdjustment;
    }

    public void setPayAdjustment(BigDecimal payAdjustment) {
        this.payAdjustment = payAdjustment;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public BigDecimal getTotalAdditionalPay() {
        return totalAdditionalPay == null ? BigDecimal.ZERO : totalAdditionalPay;
    }

    public BigDecimal getRegulatoryLimit() {
        return regulatoryLimit;
    }

    public BigDecimal getTaxDueEndCurrPeriod() {
        return taxDueEndCurrPeriod;
    }

    public BigDecimal getTaxNotDeducted() {
        return taxNotDeducted;
    }

    public void setTotalAdditionalPay(BigDecimal totalAdditionalPay) {
        this.totalAdditionalPay = totalAdditionalPay;
    }

    public void setRegulatoryLimit(BigDecimal regulatoryLimit) {
        this.regulatoryLimit = regulatoryLimit;
    }

    public void setTaxNotDeducted(BigDecimal taxNotDeducted) {
        this.taxNotDeducted = taxNotDeducted;
    }

    public void setTaxDueEndCurrPeriod(BigDecimal taxDueEndCurrPeriod) {
        this.taxDueEndCurrPeriod = taxDueEndCurrPeriod;
    }

    public BigDecimal getTotalFreePay() {
        return totalFreePay == null ? BigDecimal.ZERO : totalFreePay;
    }

    public void setTotalFreePay(BigDecimal totalFreePay) {
        this.totalFreePay = totalFreePay;
    }

    public BigDecimal getTotalTaxablePayToDate() {
        return totalTaxablePayToDate;
    }

    public void setTotalTaxablePayToDate(BigDecimal totalTaxablePayToDate) {
        this.totalTaxablePayToDate = totalTaxablePayToDate;
    }

    public BigDecimal getTaxDeductedInTheWeek() {
        return taxDeductedInTheWeek;
    }

    public void setTaxDeductedInTheWeek(BigDecimal taxDeductedInTheWeek) {
        this.taxDeductedInTheWeek = taxDeductedInTheWeek;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotalTaxDueToDate() {
        return totalTaxDueToDate;
    }

    public void setTotalTaxDueToDate(BigDecimal totalTaxDueToDate) {
        this.totalTaxDueToDate = totalTaxDueToDate;
    }

    public BigDecimal getTotalPayToDate() {
        return totalPayToDate;
    }

    public void setTotalPayToDate(BigDecimal totalPayToDate) {
        this.totalPayToDate = totalPayToDate;
    }

    public BigDecimal getPayInPeriod() {
        return payInPeriod;
    }

    public void setPayInPeriod(BigDecimal payInPeriod) {
        this.payInPeriod = payInPeriod;
    }
}
