package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.math.BigDecimal;

/**
 * User: Murad Satimov
 * Date: 2/9/18 5:04 PM
 */
public class PayrolTableItemListResult extends ListResult<SinglePayrunItem> {
    private PayrollTotalTO totalTO;
    private CurrencyItem currency;
    private BigDecimal basicSalary;
    private BigDecimal allowance;
    private BigDecimal pensionAmount;
    private BigDecimal employeeContribution;
    private BigDecimal deduction;
    private BigDecimal tax;
    private BigDecimal employeeExpenses;
    private Boolean enabledMultiCurrency;

    public PayrollTotalTO getTotalTO() {
        return totalTO;
    }

    public void setTotalTO(PayrollTotalTO totalTO) {
        this.totalTO = totalTO;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getPensionAmount() {
        return pensionAmount;
    }

    public void setPensionAmount(BigDecimal pensionAmount) {
        this.pensionAmount = pensionAmount;
    }

    public BigDecimal getEmployeeContribution() {
        return employeeContribution;
    }

    public void setEmployeeContribution(BigDecimal employeeContribution) {
        this.employeeContribution = employeeContribution;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getEmployeeExpenses() {
        return employeeExpenses;
    }

    public void setEmployeeExpenses(BigDecimal employeeExpenses) {
        this.employeeExpenses = employeeExpenses;
    }

    public Boolean getEnabledMultiCurrency() {
        return enabledMultiCurrency != null && enabledMultiCurrency;
    }

    public void setEnabledMultiCurrency(Boolean enabledMultiCurrency) {
        this.enabledMultiCurrency = enabledMultiCurrency;
    }
}
