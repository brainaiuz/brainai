package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/22/15
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollBatchData implements IsSerializable {

    private Integer objectID;
    private String name;
    private String description;
    private Integer type; //0-by Department, 1-by Position
    private CurrencyItem[] currencies;
    private CurrencyItem currency;
    private HashSet<Integer> selectedEmployeeIds;
    private Boolean enabledMultiCurrency;
    private SelectItem[] managers;
    private Integer employeesAmount;
    private SelectItem client;
    private SelectItem project;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public CurrencyItem[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(CurrencyItem[] currencies) {
        this.currencies = currencies;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public HashSet<Integer> getSelectedEmployeeIds() {
        return selectedEmployeeIds;
    }

    public void setSelectedEmployeeIds(HashSet<Integer> selectedEmployeeIds) {
        this.selectedEmployeeIds = selectedEmployeeIds;
    }

    public Boolean isEnabledMultiCurrency() {
        return enabledMultiCurrency != null && enabledMultiCurrency;
    }

    public void setEnabledMultiCurrency(Boolean enabledMultiCurrency) {
        this.enabledMultiCurrency = enabledMultiCurrency;
    }

    public SelectItem[] getManagers() {
        return managers;
    }

    public void setManagers(SelectItem[] managers) {
        this.managers = managers;
    }

    public Integer getEmployeesAmount() {
        return employeesAmount;
    }

    public void setEmployeesAmount(Integer employeesAmount) {
        this.employeesAmount = employeesAmount;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }
}
