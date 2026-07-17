package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shohruh on 23-Mar-17.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflow_action")
public class EdsWorkflowAction extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private String name;
    private Integer actionType;
    private Date createdDate;
    private Integer workflowId;
    private String formId;

    @OneToMany
    @JoinColumn(name = "workflow_action_id")
    private List<EdsWorkflowActionItem> items;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public List<EdsWorkflowActionItem> getItems() {
        if (items == null) items = new ArrayList<>();
        return items;
    }

    public void setItems(List<EdsWorkflowActionItem> fields) {
        this.items = fields;
    }

    public static EdsWorkflowAction fromRPC(EdsWorkflowAction item, WorkflowAction rpc) {
        if (item == null) {
            item = new EdsWorkflowAction();
            item.setCreatedDate(new Date());
        }
        if (rpc == null) {
            rpc = new WorkflowAction();
        }
        item.setName(rpc.getName());
        item.setActionType(rpc.getActionType());
        item.setFormId(rpc.getFormId());
        item.setWorkflowId(rpc.getWorkflowId());
        return item;
    }

    public WorkflowAction getRPC(WorkflowAction action) {
        if (action == null) {
            action = new WorkflowAction();
        }
        action.setId(getObjectID());
        action.setName(getName());
        action.setActionType(getActionType());
        action.setCreatedDate(getCreatedDate());
        action.setWorkflowId(getWorkflowId());

        for (EdsWorkflowActionItem item : getItems()) {
            action.addItem(item.getRPC(null));
        }
        return action;
    }
}
