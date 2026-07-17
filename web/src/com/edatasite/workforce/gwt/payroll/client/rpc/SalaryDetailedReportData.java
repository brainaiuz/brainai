package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

public class SalaryDetailedReportData implements IsSerializable {

    private Integer employeeId;
    private String employeeCode;
    private String employeeName;

    private HashMap<String, ArrayList<SalaryDetailedReportItem>> payments;
    private HashMap<String, ArrayList<SalaryDetailedReportItem>> deductions;
    private HashMap<String, ArrayList<SalaryDetailedReportItem>> employerContribution;

    public SalaryDetailedReportData() {
    }

    public SalaryDetailedReportData(Integer employeeId, String employeeCode, String employeeName) {
        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public HashMap<String, ArrayList<SalaryDetailedReportItem>> getPayments() {
        if (payments == null) {
            payments = new HashMap<>();
        }
        return payments;
    }

    public void setPayments(HashMap<String, ArrayList<SalaryDetailedReportItem>> payments) {
        this.payments = payments;
    }

    public HashMap<String, ArrayList<SalaryDetailedReportItem>> getDeductions() {
        if (deductions == null) {
            deductions = new HashMap<>();
        }
        return deductions;
    }

    public void setDeductions(HashMap<String, ArrayList<SalaryDetailedReportItem>> deductions) {
        this.deductions = deductions;
    }

    public HashMap<String, ArrayList<SalaryDetailedReportItem>> getEmployerContribution() {
        if (employerContribution == null) {
            employerContribution = new HashMap<>();
        }
        return employerContribution;
    }

    public void setEmployerContribution(HashMap<String, ArrayList<SalaryDetailedReportItem>> employerContribution) {
        this.employerContribution = employerContribution;
    }
}
