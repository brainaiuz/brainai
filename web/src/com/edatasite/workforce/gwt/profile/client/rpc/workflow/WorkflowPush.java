package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Azazello on 10/15/15.
 */
public class WorkflowPush implements IsSerializable {
    public static final String RECIPIENT = "RECIPIENT";
    public static final String SUBJECT = "SUBJECT";
    private Integer objectID;
    private Integer workflowID;
    private String subject;
    private String recipient;
    private WorkflowRule workflow;
    private ArrayList<SelectItem> allRoles;
    private ArrayList<SelectItem> selectedRoles;

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public WorkflowRule getWorkflow() {
        return workflow;
    }

    public void setWorkflow(WorkflowRule workflow) {
        this.workflow = workflow;
    }

    public ArrayList<SelectItem> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(ArrayList<SelectItem> allRoles) {
        this.allRoles = allRoles;
    }

    public ArrayList<SelectItem> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(ArrayList<SelectItem> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public static ArrayList<Integer> getIDsOnly(Set<WorkflowPush> selectedItems) {
        ArrayList<Integer> result = new ArrayList<>();
        if (selectedItems != null && selectedItems.size() > 0) {
            for (WorkflowPush item : selectedItems) {
                if (item.getObjectID() != null) {
                    result.add(item.getObjectID());
                }
            }
        }
        return result;
    }
}
