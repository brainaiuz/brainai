package com.edatasite.workforce.gwt.core.client.rpc.resourceUtil;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Created by FARRUH on 18-Sep-14.
 */
public class ExportToExcelItem implements IsSerializable {

    private LinkedHashMap<String, BigDecimal[][]> employeeSum = new LinkedHashMap<>();
    private LinkedHashMap<String, BigDecimal[]> projectSum = new LinkedHashMap<>();
    private LinkedHashMap<String, BigDecimal[][]> tasksSummary = new LinkedHashMap<>();
    private LinkedHashMap<String, BigDecimal[][]> leaveTimeslotSum = new LinkedHashMap<>();
    private LinkedHashMap<String, String> printOrderWithNames = new LinkedHashMap<>();

    public LinkedHashMap<String, BigDecimal[][]> getEmployeeSum() {
        return employeeSum;
    }

    public void setEmployeeSum(LinkedHashMap<String, BigDecimal[][]> employeeSum) {
        this.employeeSum = employeeSum;
    }

    public LinkedHashMap<String, BigDecimal[]> getProjectSum() {
        return projectSum;
    }

    public void setProjectSum(LinkedHashMap<String, BigDecimal[]> projectSum) {
        this.projectSum = projectSum;
    }

    public LinkedHashMap<String, BigDecimal[][]> getTasksSummary() {
        return tasksSummary;
    }

    public void setTasksSummary(LinkedHashMap<String, BigDecimal[][]> tasksSummary) {
        this.tasksSummary = tasksSummary;
    }

    public LinkedHashMap<String, BigDecimal[][]> getLeaveTimeslotSum() {
        return leaveTimeslotSum;
    }

    public void setLeaveTimeslotSum(LinkedHashMap<String, BigDecimal[][]> leaveTimeslotSum) {
        this.leaveTimeslotSum = leaveTimeslotSum;
    }

    public LinkedHashMap<String, String> getPrintOrderWithNames() {
        return printOrderWithNames;
    }

    public void setPrintOrderWithNames(LinkedHashMap<String, String> printOrderWithNames) {
        this.printOrderWithNames = printOrderWithNames;
    }
}
