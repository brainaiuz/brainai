package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/4/16
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class CashAdvanceReportData implements IsSerializable {
    private ArrayList<CashAdvanceReportItem> cashAdvanceReportItems;
    private Integer totalCount;

    public CashAdvanceReportData(){
    }

    public CashAdvanceReportData(ArrayList<CashAdvanceReportItem> cashAdvanceReportItems, Integer totalCount){
        this.cashAdvanceReportItems = cashAdvanceReportItems;
        this.totalCount = totalCount;
    }

    public ArrayList<CashAdvanceReportItem> getCashAdvanceReportItems() {
        return cashAdvanceReportItems;
    }

    public void setCashAdvanceReportItems(ArrayList<CashAdvanceReportItem> cashAdvanceReportItems) {
        this.cashAdvanceReportItems = cashAdvanceReportItems;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
