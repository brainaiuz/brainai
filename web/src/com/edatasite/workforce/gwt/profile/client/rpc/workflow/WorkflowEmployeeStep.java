/*
package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Set;

*/
/**
 * Created by Azazello on 8/6/15.
 *//*

public class WorkflowEmployeeStep implements IsSerializable {
    private Integer objectID;
    private Integer workflowID;
    private Integer stepID;
    private String stepName;
    private Integer statusID;
    private String statusName;
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;
    private SelectItem[] onboardingSteps;
    private SelectItem[] statuses;
    private String formID;

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

    public Integer getStepID() {
        return stepID;
    }

    public void setStepID(Integer stepID) {
        this.stepID = stepID;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public SelectItem[] getApproverModules() {
        return onboardingSteps;
    }

    public void setOnboardingSteps(SelectItem[] onboardingSteps) {
        this.onboardingSteps = onboardingSteps;
    }

    public static ArrayList<Integer> getIDsOnly(Set<WorkflowEmployeeStep> selectedItems) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (selectedItems != null && selectedItems.size() > 0) {
            for (WorkflowEmployeeStep item : selectedItems) {
                if (item.getObjectID() != null) {
                    result.add(item.getObjectID());
                }
            }
        }
        return result;
    }

    public void setStatuses(SelectItem[] statuses) {
        this.statuses = statuses;
    }

    public SelectItem[] getStatuses() {
        return statuses;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getFormID() {
        return formID;
    }
}
*/
