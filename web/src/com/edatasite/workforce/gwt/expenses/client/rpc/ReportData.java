package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 23.11.2008
 * Time: 12:59:32
 * To change this template use File | Settings | File Templates.
 */
public class ReportData implements IsSerializable {

    private SelectItem[] categories;
    private SelectItem[] currencies;
    //    private SelectItem[] approvers;
//    private SelectItem[] approvers2;
    private AccountItem[] accounts;
    private CurrencyItem baseCurrency;
    private ExpenseReportsListItem report;
    private String layoutHTML;
    private Boolean isDoubleTaxEnabled;
    private Boolean isOnlyLinksShow;
    private SelectItem[] taxTreatments;

    public SelectItem[] getCategories() {
        return categories;
    }

    public void setCategories(SelectItem[] categories) {
        this.categories = categories;
    }

    public SelectItem[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(SelectItem[] currencies) {
        this.currencies = currencies;
    }

//    public SelectItem[] getApprovers() {
//        return approvers;
//    }
//
//    public void setApprovers(SelectItem[] approvers) {
//        this.approvers = approvers;
//    }
//
//    public SelectItem[] getApprovers2() {
//        return approvers2;
//    }
//
//    public void setApprovers2(SelectItem[] approvers2) {
//        this.approvers2 = approvers2;
//    }

    public AccountItem[] getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountItem[] accounts) {
        this.accounts = accounts;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public ExpenseReportsListItem getReport() {
        return report;
    }

    public void setReport(ExpenseReportsListItem report) {
        this.report = report;
    }

    public String getLayoutHTML() {
        return layoutHTML;
    }

    public void setLayoutHTML(String layoutHTML) {
        this.layoutHTML = layoutHTML;
    }

    public Boolean isDoubleTaxEnabled() {
        return isDoubleTaxEnabled != null ? isDoubleTaxEnabled : false;
    }

    public void setDoubleTaxEnabled(Boolean doubleTaxEnabled) {
        isDoubleTaxEnabled = doubleTaxEnabled;
    }

    public Boolean isOnlyLinksShow() {
        return isOnlyLinksShow;
    }

    public void setOnlyLinksShow(Boolean onlyLinksShow) {
        isOnlyLinksShow = onlyLinksShow;
    }

    public SelectItem[] getTaxTreatments() {
        return taxTreatments;
    }

    public void setTaxTreatments(SelectItem[] taxTreatments) {
        this.taxTreatments = taxTreatments;
    }
}
