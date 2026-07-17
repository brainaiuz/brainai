package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class PseudoContainerRPC implements IsSerializable {
    private ArrayList<PseudoMenuItem> accountingPseudoMenuItems = new ArrayList<>();
    private ArrayList<PseudoMenuItem> pmPseudoMenuItems = new ArrayList<>();
    private ArrayList<PseudoMenuItem> crmPseudoMenuItems = new ArrayList<>();
    private ArrayList<PseudoMenuItem> hrmsPseudoMenuItems = new ArrayList<>();
    private ArrayList<PseudoMenuItem> payrollPseudoMenuItems = new ArrayList<>();
    private ArrayList<PseudoMenuItem> reportingPseudoMenuItems = new ArrayList<>();
    private ArrayList<PseudoMenuItem> docsPseudoMenuItems = new ArrayList<>();
    private ArrayList<PseudoMenuItem> logisticsPseudoMenuItems = new ArrayList<>();
    private ArrayList<PseudoMenuItem> tcPseudoMenuItems = new ArrayList<>();
    private ArrayList<PseudoMenuItem> settingsPseudoMenuItems = new ArrayList<>();

    public ArrayList<PseudoMenuItem> getAccountingPseudoMenuItems() {
        return accountingPseudoMenuItems;
    }

    public void setAccountingPseudoMenuItems(ArrayList<PseudoMenuItem> accountingPseudoMenuItems) {
        this.accountingPseudoMenuItems = accountingPseudoMenuItems;
    }

    public ArrayList<PseudoMenuItem> getPmPseudoMenuItems() {
        return pmPseudoMenuItems;
    }

    public void setPmPseudoMenuItems(ArrayList<PseudoMenuItem> pmPseudoMenuItems) {
        this.pmPseudoMenuItems = pmPseudoMenuItems;
    }

    public ArrayList<PseudoMenuItem> getCrmPseudoMenuItems() {
        return crmPseudoMenuItems;
    }

    public void setCrmPseudoMenuItems(ArrayList<PseudoMenuItem> crmPseudoMenuItems) {
        this.crmPseudoMenuItems = crmPseudoMenuItems;
    }

    public ArrayList<PseudoMenuItem> getHrmsPseudoMenuItems() {
        return hrmsPseudoMenuItems;
    }

    public void setHrmsPseudoMenuItems(ArrayList<PseudoMenuItem> hrmsPseudoMenuItems) {
        this.hrmsPseudoMenuItems = hrmsPseudoMenuItems;
    }

    public ArrayList<PseudoMenuItem> getPayrollPseudoMenuItems() {
        return payrollPseudoMenuItems;
    }

    public void setPayrollPseudoMenuItems(ArrayList<PseudoMenuItem> payrollPseudoMenuItems) {
        this.payrollPseudoMenuItems = payrollPseudoMenuItems;
    }

    public ArrayList<PseudoMenuItem> getReportingPseudoMenuItems() {
        return reportingPseudoMenuItems;
    }

    public void setReportingPseudoMenuItems(ArrayList<PseudoMenuItem> reportingPseudoMenuItems) {
        this.reportingPseudoMenuItems = reportingPseudoMenuItems;
    }

    public ArrayList<PseudoMenuItem> getDocsPseudoMenuItems() {
        return docsPseudoMenuItems;
    }

    public void setDocsPseudoMenuItems(ArrayList<PseudoMenuItem> docsPseudoMenuItems) {
        this.docsPseudoMenuItems = docsPseudoMenuItems;
    }

    public ArrayList<PseudoMenuItem> getLogisticsPseudoMenuItems() {
        return logisticsPseudoMenuItems;
    }

    public void setLogisticsPseudoMenuItems(ArrayList<PseudoMenuItem> logisticsPseudoMenuItems) {
        this.logisticsPseudoMenuItems = logisticsPseudoMenuItems;
    }

    public ArrayList<PseudoMenuItem> getTcPseudoMenuItems() {
        return tcPseudoMenuItems;
    }

    public void setTcPseudoMenuItems(ArrayList<PseudoMenuItem> tcPseudoMenuItems) {
        this.tcPseudoMenuItems = tcPseudoMenuItems;
    }

    public ArrayList<PseudoMenuItem> getSettingsPseudoMenuItems() {
        return settingsPseudoMenuItems;
    }

    public void setSettingsPseudoMenuItems(ArrayList<PseudoMenuItem> settingsPseudoMenuItems) {
        this.settingsPseudoMenuItems = settingsPseudoMenuItems;
    }
}
