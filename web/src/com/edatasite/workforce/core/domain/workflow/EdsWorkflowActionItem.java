package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowActionItem;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by shohruh on 25-Mar-17.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflow_action_item")
public class EdsWorkflowActionItem extends EdsObject{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer fieldId;
    private String fieldIdString;
    private Integer mappedId;
    private Integer defaultId;
    private String defaultText;

    @Column(precision = 14, scale = 4)
    private BigDecimal defaultNumeric;
    private Date defaultDate;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "workflow_action_id")
    private EdsWorkflowAction workflowAction;

    @Override
    public Integer getObjectID() {
        return objectID;
    }


    public EdsWorkflowAction getWorkflowAction() {
        return workflowAction;
    }

    public void setWorkflowAction(EdsWorkflowAction workflowAction) {
        this.workflowAction = workflowAction;
    }

    public WorkflowActionItem getRPC(WorkflowActionItem item) {
        if (item == null) {
            item = new WorkflowActionItem();
        }
        item.setObjectId(getObjectID());
        item.setFieldId(getFieldId());
        item.setFieldIdString(getFieldIdString());
        item.setMappedId(getMappedId());
        item.setDefaultId(getDefaultId());
        item.setDefaultText(getDefaultText());
        item.setDefaultNumeric(getDefaultNumeric());
        item.setDefaultDate(getDefaultDate());

        return item;
    }

    public static EdsWorkflowActionItem fromRPC(EdsWorkflowActionItem item, WorkflowActionItem rpc) {
        if (item == null) {
            item = new EdsWorkflowActionItem();
        }
        if (rpc == null) {
            rpc = new WorkflowActionItem();
        }
        item.setFieldId(rpc.getFieldId());
        item.setFieldIdString(rpc.getFieldIdString());
        item.setMappedId(rpc.getMappedId());
        item.setDefaultId(rpc.getDefaultId());
        item.setDefaultText(rpc.getDefaultText());
        item.setDefaultNumeric(rpc.getDefaultNumeric());
        item.setDefaultDate(rpc.getDefaultDate());

        return item;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldIdString() {
        return fieldIdString;
    }

    public void setFieldIdString(String fieldIdString) {
        this.fieldIdString = fieldIdString;
    }

    public Integer getMappedId() {
        return mappedId;
    }

    public void setMappedId(Integer mappedId) {
        this.mappedId = mappedId;
    }

    public Integer getDefaultId() {
        return defaultId;
    }

    public void setDefaultId(Integer defaultId) {
        this.defaultId = defaultId;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public BigDecimal getDefaultNumeric() {
        return defaultNumeric;
    }

    public void setDefaultNumeric(BigDecimal defaultNumeric) {
        this.defaultNumeric = defaultNumeric;
    }

    public Date getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
    }

    public Object getDefault() {
        if (defaultId != null) return new SelectItem(defaultId, defaultText);
        else if (defaultDate != null) return defaultDate;
        else if (defaultNumeric != null) return defaultNumeric;
        return defaultText;
    }
}
