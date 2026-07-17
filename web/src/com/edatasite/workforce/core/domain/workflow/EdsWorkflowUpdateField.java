package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.DateTermsEnum;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowUpdateField;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Hayot on 6/6/2014.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflow_update_field")
public class EdsWorkflowUpdateField extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private Integer workflowID;
    private String formID;
    private String customFormID;
    private String fieldID;
    private String value;
    private String label;
    private Date dateValue;

    @Enumerated(EnumType.STRING)
    private DateTermsEnum condition;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getFieldID() {
        return fieldID;
    }

    public void setFieldID(String fieldID) {
        this.fieldID = fieldID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCustomFormID() {
        return customFormID;
    }

    public void setCustomFormID(String customFormID) {
        this.customFormID = customFormID;
    }

    public DateTermsEnum getCondition() {
        return condition;
    }

    public void setCondition(DateTermsEnum condition) {
        this.condition = condition;
    }

    public WorkflowUpdateField getRPC(WorkflowUpdateField item) {
        if (item == null) {
            item = new WorkflowUpdateField();
        }
        item.setObjectID(getObjectID());
        item.setFormID(getFormID());
        item.setCustomFormID(getCustomFormID());
        item.setFieldID(getFieldID());
        item.setWorkflowID(getWorkflowID());
        item.setValue(getValue());
        item.setDateValue(getDateValue());
        item.setLabel(getLabel());
        item.setConditionId(getCondition() != null ? getCondition().getId() : null);
        return item;
    }

    public static EdsWorkflowUpdateField fromRPC(EdsWorkflowUpdateField item, WorkflowUpdateField rpc) {
        if (item == null) {
            item = new EdsWorkflowUpdateField();
        }
        if (rpc == null) {
            rpc = new WorkflowUpdateField();
        }
        item.setFormID(rpc.getFormID());
        item.setCustomFormID(rpc.getCustomFormID());
        item.setFieldID(rpc.getFieldID());
        item.setWorkflowID(rpc.getWorkflowID());
        item.setValue(rpc.getValue());
        item.setDateValue(rpc.getDateValue());
        item.setLabel(rpc.getLabel());
        item.setCondition(DateTermsEnum.getById(rpc.getConditionId()));
        return item;
    }
}
