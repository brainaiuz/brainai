package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EndOfServiceRules;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 05.05.14
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "eos_rules")
public class EdsRuleEosSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String rule;

    private Integer ruleType; // 0 - Unlimited, 1 - Limited

    private String ruleCode;

    private Integer days;

    private Integer months;

    private String reasonCode;

    private String paymentAward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settings_id")
    private EndOfServiceSettings settings;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getRuleType() {
        return ruleType != null ? ruleType : 0;
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

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer month) {
        this.months = month;
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

    public EndOfServiceSettings getSettings() {
        return settings;
    }

    public void setSettings(EndOfServiceSettings settings) {
        this.settings = settings;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public EndOfServiceRules getRPC() {
        EndOfServiceRules rule = new EndOfServiceRules();
        rule.setObjectID(getObjectID());
        rule.setDays(getDays());
        rule.setMonths(getMonths());
        rule.setReasonCode(getReasonCode());
        rule.setRule(getRule());
        rule.setRuleType(getRuleType());
        rule.setRuleCode(getRuleCode());
        rule.setPaymentAward(getPaymentAward());
        return rule;
    }
}
