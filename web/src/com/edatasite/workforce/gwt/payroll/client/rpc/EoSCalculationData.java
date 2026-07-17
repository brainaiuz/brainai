package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 13.05.14
 * Time: 0:44
 * To change this template use File | Settings | File Templates.
 */

public class EoSCalculationData implements IsSerializable {

    private Integer objectID;
    private SelectItem employee;
    private String employeeCode;
    private String paymentNumber;
    private String reasonCode;
    private SelectItem creator;
    private DateNonConvertable date;
    private DateNonConvertable hireDate;
    private DateNonConvertable resignationDate;
    private Integer totalWorkedDays;
    private Integer employeeContractType;
    private BigDecimal basicSalary;
    private BigDecimal eosAmount;
    private String eosReasonString;
    private BigDecimal lastPaymentsTotal;
    private BigDecimal leaveAllowanceTotal;
    private BigDecimal leftLeaveDays;
    private BigDecimal benefitPaymentTotal;
    private CurrencyItem currency;
    private BigDecimal exchangeRate;
    private Double numberOfWorkDay;
    private boolean enabledMultiCurrency;
    private BankTransferNumberData numberData;
    private SinglePayrunItem payrunItem;
    private String employeeSalaryCurrency;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public DateNonConvertable getHireDate() {
        return hireDate;
    }

    public void setHireDate(DateNonConvertable hireDate) {
        this.hireDate = hireDate;
    }

    public DateNonConvertable getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(DateNonConvertable resignationDate) {
        this.resignationDate = resignationDate;
    }

    public Integer getTotalWorkedDays() {
        return totalWorkedDays;
    }

    public void setTotalWorkedDays(Integer totalWorkedDays) {
        this.totalWorkedDays = totalWorkedDays;
    }

    public Integer getEmployeeContractType() {
        return employeeContractType;
    }

    public void setEmployeeContractType(Integer employeeContractType) {
        this.employeeContractType = employeeContractType;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getEosAmount() {
        return eosAmount;
    }

    public void setEosAmount(BigDecimal eosAmount) {
        this.eosAmount = eosAmount;
    }

    public String getEosReasonString() {
        return eosReasonString;
    }

    public void setEosReasonString(String eosReasonString) {
        this.eosReasonString = eosReasonString;
    }

    public BigDecimal getLastPaymentsTotal() {
        return lastPaymentsTotal;
    }

    public void setLastPaymentsTotal(BigDecimal lastPaymentsTotal) {
        this.lastPaymentsTotal = lastPaymentsTotal;
    }

    public BigDecimal getLeaveAllowanceTotal() {
        return leaveAllowanceTotal;
    }

    public void setLeaveAllowanceTotal(BigDecimal leaveAllowanceTotal) {
        this.leaveAllowanceTotal = leaveAllowanceTotal;
    }

    public BigDecimal getLeftLeaveDays() {
        return leftLeaveDays;
    }

    public void setLeftLeaveDays(BigDecimal lefLeaveDays) {
        this.leftLeaveDays = lefLeaveDays;
    }

    public BigDecimal getBenefitPaymentTotal() {
        return benefitPaymentTotal;
    }

    public void setBenefitPaymentTotal(BigDecimal benefitPaymentTotal) {
        this.benefitPaymentTotal = benefitPaymentTotal;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getNumberOfWorkDay() {
        return numberOfWorkDay;
    }

    public void setNumberOfWorkDay(Double numberOfWorkDay) {
        this.numberOfWorkDay = numberOfWorkDay;
    }

    public boolean isEnabledMultiCurrency() {
        return enabledMultiCurrency;
    }

    public void setEnabledMultiCurrency(boolean enabledMultiCurrency) {
        this.enabledMultiCurrency = enabledMultiCurrency;
    }

    public void setNumberData(BankTransferNumberData numberData) {
        this.numberData = numberData;
    }

    public BankTransferNumberData getNumberData() {
        return numberData;
    }

    public SinglePayrunItem getPayrunItem() {
        return payrunItem;
    }

    public void setPayrunItem(SinglePayrunItem payrunItem) {
        this.payrunItem = payrunItem;
    }

    public void setEmployeeSalaryCurrency(String employeeSalaryCurrency) {
        this.employeeSalaryCurrency = employeeSalaryCurrency;
    }

    public String getEmployeeSalaryCurrency() {
        return employeeSalaryCurrency;
    }
}
