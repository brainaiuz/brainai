package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ReportData;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/22/11
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "reportData")
public class MReportData {

    @XmlElementWrapper(name = "accountList")
    @XmlElement(name = "account")
    private List<MAccountItem> accounts;

    @XmlElementWrapper(name = "approverList")
    @XmlElement(name = "approver")
    private List<MSelectItem> approvers;
    @XmlElementWrapper(name = "categoryList")
    @XmlElement(name = "category")
    private List<MSelectItem> categories;

    @XmlElementWrapper(name = "currencyList")
    @XmlElement(name = "currency")
    private List<MSelectItem> currencies;


    @XmlElementWrapper(name = "projectList")
    @XmlElement(name = "project")
    private List<MSelectItem> projects;

    private MCurrencyItem baseCurrency;
    private MExpenseReportsListItem report;

    public MReportData() {
    }

    public MReportData(ReportData reportData) {
        if (reportData != null) {
            this.categories = WebServiceUtils.getAsMSelectItemList(reportData.getCategories());
            this.currencies = WebServiceUtils.getAsMSelectItemList(reportData.getCurrencies());
            this.projects = WebServiceUtils.getAsMSelectItemList(new SelectItem[]{}/*reportData.getProjects()*/);
//            this.approvers = WebServiceUtils.getAsMSelectItemList(reportData.getApprovers());

            if (reportData.getAccounts() != null) {
                this.accounts = new ArrayList<>();
                for (AccountItem accountItem : reportData.getAccounts()) {
                    this.accounts.add(new MAccountItem(accountItem));
                }
            }

            this.baseCurrency = new MCurrencyItem(reportData.getBaseCurrency());
            this.report = new MExpenseReportsListItem(reportData.getReport());
        }
    }


    public List<MSelectItem> getCategories() {
        return categories;
    }

    public void setCategories(List<MSelectItem> categories) {
        this.categories = categories;
    }

    public List<MSelectItem> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<MSelectItem> currencies) {
        this.currencies = currencies;
    }

    public List<MSelectItem> getProjects() {
        return projects;
    }

    public void setProjects(List<MSelectItem> projects) {
        this.projects = projects;
    }

    public List<MSelectItem> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<MSelectItem> approvers) {
        this.approvers = approvers;
    }

    public List<MAccountItem> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<MAccountItem> accounts) {
        this.accounts = accounts;
    }

    public MCurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(MCurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public MExpenseReportsListItem getReport() {
        return report;
    }

    public void setReport(MExpenseReportsListItem report) {
        this.report = report;
    }
}
