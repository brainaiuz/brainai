package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.EdsEmployee;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: Aug 25, 2009
 * Time: 3:12:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class P11StructInternal {

    private EdsEmployee emloyee;
    private int frequency;
    private int payPeriod;
    private BigDecimal tax;

    private String taxCode;
    private int year;
    private BigDecimal totalFreePay;
    private BigDecimal totalTaxablePay;
    private BigDecimal taxDeductedInTheWeek;
    private BigDecimal totalPayToDate;
    private BigDecimal totalAdditionalPay;
    private BigDecimal totalTaxDue;
    private BigDecimal taxDueEndCurrPeriod;
    private BigDecimal regulatoryLimit;
    private BigDecimal taxDeductedRefunded;
    private BigDecimal taxNotDeducted;


    public BigDecimal getTaxDeductedInTheWeek() {
        return taxDeductedInTheWeek;
    }

    public void setTaxDueEndCurrPeriod(BigDecimal taxDueEndCurrPeriod) {
        this.taxDueEndCurrPeriod = taxDueEndCurrPeriod;
    }/*temporary until refactoring */

    public void setRegulatoryLimit(BigDecimal regulatoryLimit) {
        this.regulatoryLimit = regulatoryLimit;
    }

    public void setTaxDeductedRefunded(BigDecimal taxDeductedRefunded) {
        this.taxDeductedRefunded = taxDeductedRefunded;
    }

    public void setTaxNotDeducted(BigDecimal taxNotDeducted) {
        this.taxNotDeducted = taxNotDeducted;
    }

    public BigDecimal getTaxNotDeducted() {
        return taxNotDeducted;
    }

    public BigDecimal getTotalAdditionalPay() {
        return totalAdditionalPay;
    }

    public void setTotalAdditionalPay(BigDecimal totalAdditionalPay) {
        this.totalAdditionalPay = totalAdditionalPay;
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

    private BigDecimal payInPeriod;

    public BigDecimal getTotalPayToDate() {
        return totalPayToDate;
    }


    public BigDecimal getTotalTaxDue() {
        return totalTaxDue;
    }

    public BigDecimal getTaxDueEndCurrPeriod() {
        return taxDueEndCurrPeriod;
    }


    public BigDecimal getRegulatoryLimit() {
        return regulatoryLimit;
    }


    public void setTotalTaxDue(BigDecimal totalTaxDue) {
        this.totalTaxDue = totalTaxDue;
    }


    public BigDecimal getTaxDeductedRefunded() {
        return taxDeductedRefunded;
    }


    public EdsEmployee getEmloyee() {
        return emloyee;
    }

    public void setEmloyee(EdsEmployee emloyee) {
        this.emloyee = emloyee;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(int payPeriod) {
        this.payPeriod = payPeriod;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getTotalFreePay() {
        return totalFreePay;
    }

    public void setTotalFreePay(BigDecimal totalFreePay) {
        this.totalFreePay = totalFreePay;
    }

    public BigDecimal getTotalTaxablePay() {
        return totalTaxablePay;
    }

    public void setTotalTaxablePay(BigDecimal totalTaxablePay) {
        this.totalTaxablePay = totalTaxablePay;
    }
//    public BigDecimal gettaxDeductedInTheWeek() {
//        return taxDeductedInTheWeek;
//    }

    public void setTaxDeductedInTheWeek(BigDecimal taxDeductedInTheWeek) {

        this.taxDeductedInTheWeek = taxDeductedInTheWeek;
    }


}
