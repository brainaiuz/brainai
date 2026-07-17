package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/5/16
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalaryReportItem implements IsSerializable {

    private String employeeName;
    private String month;
    private String year;
    private BigDecimal basicSalary;
    private BigDecimal allowance;
    private BigDecimal expensePayment;
    private BigDecimal expenseDeduction;
    private BigDecimal deduction;
    private BigDecimal pensionAmount;
    private BigDecimal total;
    private String employeeCode;
    private String currency;

    public SalaryReportItem(){

    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public BigDecimal getExpensePayment() {
        return expensePayment;
    }

    public void setExpensePayment(BigDecimal expensePayment) {
        this.expensePayment = expensePayment;
    }

    public BigDecimal getExpenseDeduction() {
        return expenseDeduction;
    }

    public void setExpenseDeduction(BigDecimal expenseDeduction) {
        this.expenseDeduction = expenseDeduction;
    }

    public BigDecimal getPensionAmount() {
        return pensionAmount;
    }

    public void setPensionAmount(BigDecimal pensionAmount) {
        this.pensionAmount = pensionAmount;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
