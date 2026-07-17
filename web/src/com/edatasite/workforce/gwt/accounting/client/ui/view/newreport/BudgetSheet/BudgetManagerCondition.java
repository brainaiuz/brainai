package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BudgetManagerCondition implements IsSerializable {
    private String column;
    private String operator;
    private String operand;
    private String value;
    private String customFieldName;
    private Integer conditionID;
    private Integer objectID;
    private String type;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCustomFieldName() {
        return customFieldName;
    }

    public void setCustomFieldName(String customFieldName) {
        this.customFieldName = customFieldName;
    }

    public Integer getConditionID() {
        return conditionID;
    }

    public void setConditionID(Integer conditionID) {
        this.conditionID = conditionID;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}