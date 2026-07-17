package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 09/10/2014
 * Time: 23:37:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollGlobalSettingsData implements IsSerializable {

    private Integer objectID;
    private Integer categoryType; //0-Payment, 1-Deduction, 2 - Basic Salary
    private Integer settingsType; //0-by PayrollBatch, 1-by Position, 2 - by Department
    private Integer payType;
    private Integer[] selectedEmployeeIds;

    private ArrayList<Integer> deletedCategories;
    private ArrayList<PaymentDeductionObject> payments;
    private ArrayList<PaymentDeductionObject> deductions;
    private ArrayList<PaymentDeductionObject> taxes;
    private ArrayList<PaymentDeductionObject> employerContributions;

    private PaymentDeductionSelectItem salaryCategory;
    private BigDecimal salary;
    private String rateType;

    private String regularOvertimeRateType;
    private String weekendOvertimeRateType;
    private String holidayOvertimeRateType;

    private BigDecimal regularOvertimeRate;
    private BigDecimal weekendOvertimeRate;
    private BigDecimal holidayOvertimeRate;

    private CurrencyItem[] currencies;
    private CurrencyItem currency;
    private SelectItem batchItem;

    private boolean enabledMultiCurrency;

    private Date lastUpdate;

    private String name;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getSettingsType() {
        return settingsType;
    }

    public void setSettingsType(Integer settingsType) {
        this.settingsType = settingsType;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public ArrayList<Integer> getDeletedCategories() {
        return deletedCategories;
    }

    public void setDeletedCategories(ArrayList<Integer> deletedCategories) {
        this.deletedCategories = deletedCategories;
    }

    public ArrayList<PaymentDeductionObject> getPayments() {
        if (payments == null) {
            payments = new ArrayList<>();
        }
        return payments;
    }

    public void setPayments(ArrayList<PaymentDeductionObject> payments) {
        this.payments = payments;
    }

    public ArrayList<PaymentDeductionObject> getDeductions() {
        if (deductions == null) {
            deductions = new ArrayList<>();
        }
        return deductions;
    }

    public void setDeductions(ArrayList<PaymentDeductionObject> deductions) {
        this.deductions = deductions;
    }

    public ArrayList<PaymentDeductionObject> getTaxes() {
        if (taxes == null) {
            taxes = new ArrayList<>();
        }
        return taxes;
    }

    public void setTaxes(ArrayList<PaymentDeductionObject> taxes) {
        this.taxes = taxes;
    }

    public PaymentDeductionSelectItem getSalaryCategory() {
        return salaryCategory;
    }

    public void setSalaryCategory(PaymentDeductionSelectItem salaryCategory) {
        this.salaryCategory = salaryCategory;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public Integer[] getSelectedEmployeeIds() {
        return selectedEmployeeIds;
    }

    public void setSelectedEmployeeIds(Integer[] selectedEmployeeIds) {
        this.selectedEmployeeIds = selectedEmployeeIds;
    }

    public String getRegularOvertimeRateType() {
        return regularOvertimeRateType;
    }

    public void setRegularOvertimeRateType(String regularOvertimeRateType) {
        this.regularOvertimeRateType = regularOvertimeRateType;
    }

    public String getWeekendOvertimeRateType() {
        return weekendOvertimeRateType;
    }

    public void setWeekendOvertimeRateType(String weekendOvertimeRateType) {
        this.weekendOvertimeRateType = weekendOvertimeRateType;
    }

    public String getHolidayOvertimeRateType() {
        return holidayOvertimeRateType;
    }

    public void setHolidayOvertimeRateType(String holidayOvertimeRateType) {
        this.holidayOvertimeRateType = holidayOvertimeRateType;
    }

    public BigDecimal getRegularOvertimeRate() {
        return regularOvertimeRate;
    }

    public void setRegularOvertimeRate(BigDecimal regularOvertimeRate) {
        this.regularOvertimeRate = regularOvertimeRate;
    }

    public BigDecimal getWeekendOvertimeRate() {
        return weekendOvertimeRate;
    }

    public void setWeekendOvertimeRate(BigDecimal weekendOvertimeRate) {
        this.weekendOvertimeRate = weekendOvertimeRate;
    }

    public BigDecimal getHolidayOvertimeRate() {
        return holidayOvertimeRate;
    }

    public void setHolidayOvertimeRate(BigDecimal holidayOvertimeRate) {
        this.holidayOvertimeRate = holidayOvertimeRate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
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

    public SelectItem getBatchItem() {
        return batchItem;
    }

    public void setBatchItem(SelectItem batchItem) {
        this.batchItem = batchItem;
    }

    public boolean isEnabledMultiCurrency() {
        return enabledMultiCurrency;
    }

    public void setEnabledMultiCurrency(boolean enabledMultiCurrency) {
        this.enabledMultiCurrency = enabledMultiCurrency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PaymentDeductionObject> getEmployerContributions() {
        if (employerContributions == null) {
            employerContributions = new ArrayList<>();
        }
        return employerContributions;
    }

    public void setEmployerContributions(ArrayList<PaymentDeductionObject> employerContributions) {
        this.employerContributions = employerContributions;
    }
}

