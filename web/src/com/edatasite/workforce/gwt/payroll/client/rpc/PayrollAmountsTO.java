package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * User: Akhror
 * Date: 16/9/21 11:26 AM
 */
public class PayrollAmountsTO implements IsSerializable {
    private BigDecimal basicSalary;
    private BigDecimal allowance;
    private BigDecimal pension;
    private BigDecimal deduction;
    private BigDecimal tax;

    private BigDecimal employerContribution;
    private BigDecimal expense;

    public PayrollAmountsTO() {
    }

    public PayrollAmountsTO(BigDecimal basicSalary, BigDecimal allowance, BigDecimal pension, BigDecimal deduction, BigDecimal tax, BigDecimal employerContribution, BigDecimal expense) {
        this.basicSalary = basicSalary;
        this.allowance = allowance;
        this.pension = pension;
        this.deduction = deduction;
        this.tax = tax;
        this.employerContribution = employerContribution;
        this.expense = expense;
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

    public BigDecimal getPension() {
        return pension;
    }

    public void setPension(BigDecimal pension) {
        this.pension = pension;
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

    public BigDecimal getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(BigDecimal employerContribution) {
        this.employerContribution = employerContribution;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }
}
