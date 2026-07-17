package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Hurshid on 10/2/2017.
 */
public class LeaveBalanceReport implements IsSerializable {

    public static final String EMPLOYEE_NAME = "employee_name";
    public static final String EMPLOYEE_NUMBER = "employee_number";
    public static final String OPENING_DATE = "opening_date";
    public static final String OPENING_BALANCE = "opening_balance";
    public static final String EFFECTIVE_STARTDATE = "effective_startdate";
    public static final String EFFECTIVE_ENDDATE = "effective_enddate";
    public static final String HIRE_DATE = "hire_date";
    public static final String RESIGN_DATE = "resign_date";
    public static final String STATUS = "status";
    public static final String DEPARTMENT = "department";
    public static final String LEAVE_ALLOWANCE_DAYS = "leave_allowance_days";
    public static final String TAKEN_DAYS = "taken_days";
    public static final String UNPAID_DAYS = "unpaid_days";
    public static final String WORKED_DAYS = "worked_days";
    public static final String CURRENT_BALANCE = "current_balance";

    private Integer employeeID;
    private String employeeName;
    private String employeeNumber;
    private DateNonConvertable openingDate;
    private Double openingBalance;
    private DateNonConvertable effectiveStartDate;
    private DateNonConvertable effectiveEndDate;
    private DateNonConvertable hireDate;
    private DateNonConvertable resignDate;
    private String status;
    private String department;
    private double leaveAllowanceDays;
    private double takenDays;
    private double unpaidDays;
    private double annualNonpaid;
    private double workedDays;
    private double currentBalance;

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public DateNonConvertable getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(DateNonConvertable openingDate) {
        this.openingDate = openingDate;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public DateNonConvertable getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(DateNonConvertable effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public DateNonConvertable getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(DateNonConvertable effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public DateNonConvertable getHireDate() {
        return hireDate;
    }

    public void setHireDate(DateNonConvertable hireDate) {
        this.hireDate = hireDate;
    }

    public DateNonConvertable getResignDate() {
        return resignDate;
    }

    public void setResignDate(DateNonConvertable resignDate) {
        this.resignDate = resignDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getLeaveAllowanceDays() {
        return leaveAllowanceDays;
    }

    public void setLeaveAllowanceDays(double leaveAllowanceDays) {
        this.leaveAllowanceDays = leaveAllowanceDays;
    }

    public double getTakenDays() {
        return takenDays;
    }

    public void setTakenDays(double takenDays) {
        this.takenDays = takenDays;
    }

    public double getUnpaidDays() {
        return unpaidDays;
    }

    public void setUnpaidDays(double unpaidDays) {
        this.unpaidDays = unpaidDays;
    }

    public double getWorkedDays() {
        return workedDays;
    }

    public void setWorkedDays(double workedDays) {
        this.workedDays = workedDays;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public double getAnnualNonpaid() {
        return annualNonpaid;
    }

    public void setAnnualNonpaid(double annualNonpaid) {
        this.annualNonpaid = annualNonpaid;
    }
}
