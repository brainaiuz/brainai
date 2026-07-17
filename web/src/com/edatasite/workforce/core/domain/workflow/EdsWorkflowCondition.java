package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowCondition;

import javax.persistence.*;

/**
 * Created by Hayot on 3/13/14.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflow_condition")
public class EdsWorkflowCondition extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow")
    private EdsWorkflowRule workflow;

    @Column(name = "column_id")
    private String column;
    private String operator;//and or or
    private String operand;
    private String value;
    private Integer conditionID;
    private String customFieldName;

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

    public Integer getConditionID() {
        return conditionID;
    }

    public void setConditionID(Integer conditionID) {
        this.conditionID = conditionID;
    }

    public String getCustomFieldName() {
        return customFieldName;
    }

    public void setCustomFieldName(String customFieldName) {
        this.customFieldName = customFieldName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public EdsWorkflowRule getWorkflow() {
        return workflow;
    }

    public void setWorkflow(EdsWorkflowRule workflow) {
        this.workflow = workflow;
    }

    public WorkflowCondition getRPC(WorkflowCondition item) {
        item = item == null ? new WorkflowCondition() : item;
        item.setColumn(getColumn());
        item.setConditionID(getConditionID());
        item.setOperand(getOperand());
        item.setValue(getValue());
        item.setOperator(getOperator());
        item.setCustomFieldName(getCustomFieldName());
        return item;
    }

    public static EdsWorkflowCondition fromRPC(EdsWorkflowCondition condition, WorkflowCondition item) {
        condition = condition == null ? new EdsWorkflowCondition() : condition;
        condition.setColumn(item.getColumn());
        condition.setConditionID(item.getConditionID());
        condition.setOperand(item.getOperand());
        condition.setValue(item.getValue());
        condition.setOperator(item.getOperator());
        condition.setCustomFieldName(item.getCustomFieldName());
        return condition;
    }
}
