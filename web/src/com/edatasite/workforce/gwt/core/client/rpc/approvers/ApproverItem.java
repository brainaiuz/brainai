package com.edatasite.workforce.gwt.core.client.rpc.approvers;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Hayot on 2/3/2016.
 */
public class ApproverItem extends ApproverItemMini implements IsSerializable {
    public static final Integer SEND_TO_NEXT_APPROVER = 1;
    public static final Integer SEND_TO_PREV_APPROVER = 2;
    public static final Integer SEND_TO_CREATOR = 3;
    public static final Integer SEND_TO_DIRECTORS = 4;
    public static final Integer MARK_AS_APPROVED = 5;
    public static final Integer MARK_AS_REJECTED = 6;
    public static final Integer MARK_AS_PENDING = 7;
    private ArrayList<SelectItem> roles;
    private ArrayList<SelectItem> employees;
    private ArrayList<SelectItem> dynamicQueries;
    private WorkflowRule onApproveWorkflowRule;
    private WorkflowRule onRejectWorkflowRule;
    private boolean is_default;
    private String entityType;//form_ID
    private Integer entityID;
    private Integer onApprovedAction;
    private Integer onRejectedAction;
    private Boolean isLocation;
    private String name;


    public ApproverItem clone() {
        ApproverItem clone = new ApproverItem();
        clone.setObjectID(getObjectID());
        clone.setApproverOrder(getApproverOrder());
        clone.setEntityType(getEntityType());
        clone.setEntityID(getEntityID());
        clone.setStatus(getStatus());
        clone.setOnApprovedAction(getOnApprovedAction());
        clone.setOnRejectedAction(getOnRejectedAction());
        clone.setOnApproveWorkflowRule(getOnApproveWorkflowRule());
        clone.setOnRejectWorkflowRule(getOnRejectWorkflowRule());
        clone.setRoles(getRoles());
        clone.setEmployees(getEmployees());
        clone.setName(getName());
        return clone;
    }

    public Integer getOnRejectedAction() {
        return onRejectedAction;
    }

    public void setOnRejectedAction(Integer onRejectedAction) {
        this.onRejectedAction = onRejectedAction;
    }

    public Integer getOnApprovedAction() {
        return onApprovedAction;
    }

    public void setOnApprovedAction(Integer onApprovedAction) {
        this.onApprovedAction = onApprovedAction;
    }

    public ArrayList<SelectItem> getRoles() {
        if(roles == null){
            roles = new ArrayList<>();
        }
        return roles;
    }

    public void setRoles(ArrayList<SelectItem> roles) {
        this.roles = roles;
    }

    public ArrayList<SelectItem> getEmployees() {
        if(employees == null){
            employees = new ArrayList<>();
        }
        return employees;
    }

    public void setEmployees(ArrayList<SelectItem> employees) {
        this.employees = employees;
    }

    public WorkflowRule getOnApproveWorkflowRule() {
        return onApproveWorkflowRule;
    }

    public void setOnApproveWorkflowRule(WorkflowRule onApproveWorkflowRule) {
        this.onApproveWorkflowRule = onApproveWorkflowRule;
    }

    public WorkflowRule getOnRejectWorkflowRule() {
        return onRejectWorkflowRule;
    }

    public void setOnRejectWorkflowRule(WorkflowRule onRejectWorkflowRule) {
        this.onRejectWorkflowRule = onRejectWorkflowRule;
    }

    public boolean is_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public ArrayList<SelectItem> getDynamicQueries() {
        if (dynamicQueries == null) {
            dynamicQueries = new ArrayList<>();
        }
        return dynamicQueries;
    }

    public void setDynamicQueries(ArrayList<SelectItem> dynamicQueries) {
        this.dynamicQueries = dynamicQueries;
    }

    public Boolean isLocation() {
        return isLocation;
    }

    public void setLocation(Boolean location) {
        isLocation = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
