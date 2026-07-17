package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.payroll.EndOfServiceRules;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12.05.14
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class EndOfServiceData implements IsSerializable {

    private Integer objectID;
    private String countryCode;
    private String currencyCode;
    private Integer payType;
    private List<PaymentDeductionSelectItem> allowances;
    private EndOfServiceRules[] rules;
    private boolean includeLeaveAllowances;
    private boolean includeBenefitPayments;
    private boolean allAllowanceFromLastPayment;
    private boolean fromLastPayment;
    private BankTransferNumberData numberData;
    private Boolean useMonthPayment;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public EndOfServiceRules[] getRules() {
        return rules;
    }

    public void setRules(EndOfServiceRules[] rules) {
        this.rules = rules;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<PaymentDeductionSelectItem> getAllowances() {
        if(allowances == null){
            allowances = new ArrayList<>();
        }
        return allowances;
    }

    public void setAllowances(List<PaymentDeductionSelectItem> allowances) {
        this.allowances = allowances;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean isIncludeLeaveAllowances() {
        return includeLeaveAllowances;
    }

    public void setIncludeLeaveAllowances(boolean includeLeaveAllowances) {
        this.includeLeaveAllowances = includeLeaveAllowances;
    }

    public boolean isIncludeBenefitPayments() {
        return includeBenefitPayments;
    }

    public void setIncludeBenefitPayments(boolean includeBenefitPayments) {
        this.includeBenefitPayments = includeBenefitPayments;
    }

    public boolean isAllAllowanceFromLastPayment() {
        return allAllowanceFromLastPayment;
    }

    public void setAllAllowanceFromLastPayment(boolean allAllowanceFromLastPayment) {
        this.allAllowanceFromLastPayment = allAllowanceFromLastPayment;
    }

    public boolean isFromLastPayment() {
        return fromLastPayment;
    }

    public void setFromLastPayment(boolean fromLastPayment) {
        this.fromLastPayment = fromLastPayment;
    }

    public BankTransferNumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(BankTransferNumberData numberData) {
        this.numberData = numberData;
    }

    public Boolean isUseMonthPayment() {
        return useMonthPayment != null ? useMonthPayment : false;
    }

    public void setUseMonthPayment(Boolean useMonthPayment) {
        this.useMonthPayment = useMonthPayment;
    }
}
