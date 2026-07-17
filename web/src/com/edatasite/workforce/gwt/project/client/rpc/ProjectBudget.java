package com.edatasite.workforce.gwt.project.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 8/6/11
 * Time: 11:07 AM
 */
public class ProjectBudget implements IsSerializable {

//    private ProjectBudgetItem[] revenue;
    private ProjectBudgetItem[] salesQuotes;
    private ProjectBudgetItem[] salesOrders;
    private ProjectBudgetItem[] salesInvoices;

    private ProjectBudgetItem[] employeeCosts;
    private ProjectBudgetItem[] expenseClaims;

    private ProjectBudgetItem[] bankReceipts;
    private ProjectBudgetItem[] cashReceipts;
    private ProjectBudgetItem[] bankPayments;
    private ProjectBudgetItem[] cashPayments;
    private ProjectBudgetItem[] ManualEntryRevenue;

//    private ProjectBudgetItem[] productCost;
    private ProjectBudgetItem[] purchaseOrders;
    private ProjectBudgetItem[] purchaseInvoices;
    private ProjectBudgetItem[] stockAdjustments;
    private ProjectBudgetItem[] ManualEntryExpense;

    private ProjectBudgetItem totalProjectCost = new ProjectBudgetItem();
    private ProjectBudgetItem totalProfit = new ProjectBudgetItem();
    private ProjectBudgetItem totalProjectRevenue = new ProjectBudgetItem();

    private ProjectBudgetItem subTotalRevenue = new ProjectBudgetItem();
    private ProjectBudgetItem subTotalExpences = new ProjectBudgetItem();
    private ProjectBudgetItem subTotalEmployees = new ProjectBudgetItem();
    private ProjectBudgetItem subTotalPurchases = new ProjectBudgetItem();
    private ProjectBudgetItem subTotalBankPayments = new ProjectBudgetItem();
    private ProjectBudgetItem subTotalCashPayments = new ProjectBudgetItem();

    private Integer clientID;
    private String projectName;

    public ProjectBudgetItem[] getSalesQuotes() {
        return salesQuotes;
    }

    public void setSalesQuotes(ProjectBudgetItem[] salesQuotes) {
        this.salesQuotes = salesQuotes;
    }

    public ProjectBudgetItem[] getSalesInvoices() {
        return salesInvoices;
    }

    public void setSalesInvoices(ProjectBudgetItem[] salesInvoices) {
        this.salesInvoices = salesInvoices;
    }

    public ProjectBudgetItem[] getEmployeeCosts() {
        return employeeCosts;
    }

    public void setEmployeeCosts(ProjectBudgetItem[] employeeCosts) {
        this.employeeCosts = employeeCosts;
    }

    public ProjectBudgetItem[] getExpenseClaims() {
        return expenseClaims;
    }

    public void setExpenseClaims(ProjectBudgetItem[] expenseClaims) {
        this.expenseClaims = expenseClaims;
    }

    public ProjectBudgetItem[] getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(ProjectBudgetItem[] purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public ProjectBudgetItem[] getPurchaseInvoices() {
        return purchaseInvoices;
    }

    public void setPurchaseInvoices(ProjectBudgetItem[] purchaseInvoices) {
        this.purchaseInvoices = purchaseInvoices;
    }

    public ProjectBudgetItem getTotalProjectCost() {
        return totalProjectCost;
    }

    public void setTotalProjectCost(ProjectBudgetItem totalProjectCost) {
        this.totalProjectCost = totalProjectCost;
    }

    public ProjectBudgetItem getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(ProjectBudgetItem totalProfit) {
        this.totalProfit = totalProfit;
    }

    public ProjectBudgetItem getTotalProjectRevenue() {
        return totalProjectRevenue;
    }

    public void setTotalProjectRevenue(ProjectBudgetItem totalProjectRevenue) {
        this.totalProjectRevenue = totalProjectRevenue;
    }

    public ProjectBudgetItem getSubTotalRevenue() {
        return subTotalRevenue;
    }

    public void setSubTotalRevenue(ProjectBudgetItem subTotalRevenue) {
        this.subTotalRevenue = subTotalRevenue;
    }

    public ProjectBudgetItem getSubTotalExpences() {
        return subTotalExpences;
    }

    public void setSubTotalExpences(ProjectBudgetItem subTotalExpences) {
        this.subTotalExpences = subTotalExpences;
    }

    public ProjectBudgetItem getSubTotalEmployees() {
        return subTotalEmployees;
    }

    public void setSubTotalEmployees(ProjectBudgetItem subTotalEmployees) {
        this.subTotalEmployees = subTotalEmployees;
    }

    public ProjectBudgetItem getSubTotalPurchases() {
        return subTotalPurchases;
    }

    public void setSubTotalPurchases(ProjectBudgetItem subTotalPurchases) {
        this.subTotalPurchases = subTotalPurchases;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ProjectBudgetItem[] getSalesOrders() {
        return salesOrders;
    }

    public void setSalesOrders(ProjectBudgetItem[] salesOrders) {
        this.salesOrders = salesOrders;
    }

    public ProjectBudgetItem[] getBankReceipts() {
        return bankReceipts;
    }

    public void setBankReceipts(ProjectBudgetItem[] bankReceipts) {
        this.bankReceipts = bankReceipts;
    }

    public ProjectBudgetItem[] getCashReceipts() {
        return cashReceipts;
    }

    public void setCashReceipts(ProjectBudgetItem[] cashReceipts) {
        this.cashReceipts = cashReceipts;
    }

    public ProjectBudgetItem[] getBankPayments() {
        return bankPayments;
    }

    public void setBankPayments(ProjectBudgetItem[] bankPayments) {
        this.bankPayments = bankPayments;
    }

    public ProjectBudgetItem[] getCashPayments() {
        return cashPayments;
    }

    public void setCashPayments(ProjectBudgetItem[] cashPayments) {
        this.cashPayments = cashPayments;
    }

    public ProjectBudgetItem getSubTotalBankPayments() {
        return subTotalBankPayments;
    }

    public void setSubTotalBankPayments(ProjectBudgetItem subTotalBankPayments) {
        this.subTotalBankPayments = subTotalBankPayments;
    }

    public ProjectBudgetItem getSubTotalCashPayments() {
        return subTotalCashPayments;
    }

    public void setSubTotalCashPayments(ProjectBudgetItem subTotalCashPayments) {
        this.subTotalCashPayments = subTotalCashPayments;
    }

    public ProjectBudgetItem[] getStockAdjustments() {
        return stockAdjustments;
    }

    public void setStockAdjustments(ProjectBudgetItem[] stockAdjustments) {
        this.stockAdjustments = stockAdjustments;
    }

    public ProjectBudgetItem[] getManualEntryRevenue() {
        return this.ManualEntryRevenue;
    }

    public void setManualEntryRevenue(final ProjectBudgetItem[] manualEntryRevenue) {
        this.ManualEntryRevenue = manualEntryRevenue;
    }

    public ProjectBudgetItem[] getManualEntryExpense() {
        return this.ManualEntryExpense;
    }

    public void setManualEntryExpense(final ProjectBudgetItem[] manualEntryExpense) {
        this.ManualEntryExpense = manualEntryExpense;
    }
}
