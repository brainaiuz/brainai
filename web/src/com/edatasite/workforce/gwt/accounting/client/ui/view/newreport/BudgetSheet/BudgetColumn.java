package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet;

import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowCondition;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.HashMap;

public class BudgetColumn implements IsSerializable, Serializable {

    private String name;
    private String code;
    private String type;
    private String reportType;
    private Integer reportId;
    private HashMap<Integer, WorkflowCondition> conditions;
    private boolean dynamicCondition = false;
    private String dynamicConditionQuery;
    private String pattern;
    private String periodField;
    private String calculationType;
    private String calculationField;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getReportType() {
        return this.reportType;
    }

    public void setReportType(final String reportType) {
        this.reportType = reportType;
    }

    public Integer getReportId() {
        return this.reportId;
    }

    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
    }

    public HashMap<Integer, WorkflowCondition> getConditions() {
        return conditions;
    }

    public void setConditions(HashMap<Integer, WorkflowCondition> conditions) {
        this.conditions = conditions;
    }

    public boolean isDynamicCondition() {
        return dynamicCondition;
    }

    public void setDynamicCondition(boolean dynamicCondition) {
        this.dynamicCondition = dynamicCondition;
    }

    public String getDynamicConditionQuery() {
        return dynamicConditionQuery;
    }

    public void setDynamicConditionQuery(String dynamicConditionQuery) {
        this.dynamicConditionQuery = dynamicConditionQuery;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPeriodField() {
        return periodField;
    }

    public void setPeriodField(String periodField) {
        this.periodField = periodField;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    public String getCalculationField() {
        return calculationField;
    }

    public void setCalculationField(String calculationField) {
        this.calculationField = calculationField;
    }
}
