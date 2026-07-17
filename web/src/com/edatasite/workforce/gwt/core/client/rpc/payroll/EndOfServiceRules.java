package com.edatasite.workforce.gwt.core.client.rpc.payroll;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 12.05.14
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
public class EndOfServiceRules implements IsSerializable {

    private Integer objectID;
    private String rule;
    private Integer ruleType;
    private String ruleCode;
    private Integer days;
    private String reasonCode;
    private String paymentAward;
    private Integer months;
    private boolean useMonthPayment;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getPaymentAward() {
        return paymentAward;
    }

    public void setPaymentAward(String paymentAward) {
        this.paymentAward = paymentAward;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }

    public Integer getMonths() {
        return months;
    }

    public void setUseMonthPayment(boolean useMonthPayment) {
        this.useMonthPayment = useMonthPayment;
    }

    public boolean isUseMonthPayment() {
        return useMonthPayment;
    }
}
