package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Shohruh on 19-Jan-17.
 */
public class WpsReportItem implements IsSerializable {

    Integer payslipId;
    Integer empId;
    String employeeName;
    String creator;
    String approver;
    String month;
    Integer monthId;
    Integer year;
    String wpsNumber;
    String bankCode;
    String ibanNumber;
    Date fromDate;
    Date toDate;
    Integer workedDays;
    BigDecimal recurringPayments;
    BigDecimal total;
    Integer leaveDays;


    public WpsReportItem() {
    }

    public Integer getPayslipId() {
        return payslipId;
    }

    public void setPayslipId(Integer payslipId) {
        this.payslipId = payslipId;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public Integer getMonthId() {
        return monthId;
    }

    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getWpsNumber() {
        return wpsNumber;
    }

    public void setWpsNumber(String wpsNumber) {
        this.wpsNumber = wpsNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getIbanNumber() {
        return ibanNumber;
    }

    public void setIbanNumber(String ibanNumber) {
        this.ibanNumber = ibanNumber;
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

    public Integer getWorkedDays() {
        return workedDays;
    }

    public void setWorkedDays(Integer workedDays) {
        this.workedDays = workedDays;
    }

    public BigDecimal getRecurringPayments() {
        return recurringPayments;
    }

    public void setRecurringPayments(BigDecimal recurringPayments) {
        this.recurringPayments = recurringPayments;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
    }
}
