package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/17/12
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewProjectBudgetData implements IsSerializable {

    private Integer projectID;
    private String projectName;
    private String customerName;

    private ArrayList<DateNonConvertable[]> monthIntervalList;

    private NewProjectBudgetRowItem[] revenues;
    private NewProjectBudgetRowItem employeeCost;
    private NewProjectBudgetRowItem[] expenses;
    private NewProjectBudgetRowItem purchases;
    private NewProjectBudgetRowItem[] detailedPurchases;

    private Boolean detailedPurchasesEnabled;

    public NewProjectBudgetData() {
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<DateNonConvertable[]> getMonthIntervalList() {
        return monthIntervalList;
    }

    public void setMonthIntervalList(ArrayList<DateNonConvertable[]> monthIntervalList) {
        this.monthIntervalList = monthIntervalList;
    }

    public NewProjectBudgetRowItem[] getRevenues() {
        return revenues;
    }

    public void setRevenues(NewProjectBudgetRowItem[] revenues) {
        this.revenues = revenues;
    }

    public NewProjectBudgetRowItem[] getExpenses() {
        return expenses;
    }

    public void setExpenses(NewProjectBudgetRowItem[] expenses) {
        this.expenses = expenses;
    }

    public NewProjectBudgetRowItem getPurchases() {
        return purchases;
    }

    public void setPurchases(NewProjectBudgetRowItem purchases) {
        this.purchases = purchases;
    }

    public NewProjectBudgetRowItem[] getDetailedPurchases() {
        return detailedPurchases;
    }

    public void setDetailedPurchases(NewProjectBudgetRowItem[] detailedPurchases) {
        this.detailedPurchases = detailedPurchases;
    }

    public Boolean isDetailedPurchasesEnabled() {
        return detailedPurchasesEnabled != null ? detailedPurchasesEnabled : false;
    }

    public void setDetailedPurchasesEnabled(Boolean detailedPurchasesEnabled) {
        this.detailedPurchasesEnabled = detailedPurchasesEnabled;
    }

    public NewProjectBudgetRowItem getEmployeeCost() {
        return employeeCost;
    }

    public void setEmployeeCost(NewProjectBudgetRowItem employeeCost) {
        this.employeeCost = employeeCost;
    }
}
