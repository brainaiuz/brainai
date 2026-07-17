package com.edatasite.workforce.gwt.client.client.rpc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 18.04.14
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
public class EmployeePayslipItem implements Serializable {

    private Integer objectID;
    private String status;
    private Integer statusId;
    private String creator;
    private String approver;
    private String month;
    private Integer monthID;
    private String employee;
    private Date fromDate;
    private Date toDate;
    private Integer daysWorked;
    private String description;
    private BigDecimal basicSalary;
    private BigDecimal dailyRate;
    private BigDecimal actualMonthPay;
    private BigDecimal allowance;
    private BigDecimal additionalPay;
    private BigDecimal deduction;
    private BigDecimal expense;
    private BigDecimal total;
    private Integer year;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(Integer daysWorked) {
        this.daysWorked = daysWorked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public BigDecimal getActualMonthPay() {
        return actualMonthPay;
    }

    public void setActualMonthPay(BigDecimal actualMonthPay) {
        this.actualMonthPay = actualMonthPay;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getAdditionalPay() {
        return additionalPay;
    }

    public void setAdditionalPay(BigDecimal additionalPay) {
        this.additionalPay = additionalPay;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getStatusId() {
        return this.statusId;
    }

    public void setStatusId(final Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getMonthID() {
        return this.monthID;
    }

    public void setMonthID(final Integer monthID) {
        this.monthID = monthID;
    }
}
