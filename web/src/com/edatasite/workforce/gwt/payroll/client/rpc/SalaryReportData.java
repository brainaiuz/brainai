package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/5/16
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalaryReportData implements IsSerializable {
    private ArrayList<SalaryReportItem> salaryReportItems;
    private Integer totalCount;

    public SalaryReportData(){
    }

    public SalaryReportData(ArrayList<SalaryReportItem> salaryReportItems, Integer totalCount){
        this.salaryReportItems = salaryReportItems;
        this.totalCount = totalCount;
    }

    public ArrayList<SalaryReportItem> getSalaryReportItems() {
        return salaryReportItems;
    }

    public void setSalaryReportItems(ArrayList<SalaryReportItem> salaryReportItems) {
        this.salaryReportItems = salaryReportItems;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
