package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Azazello on 4/23/15.
 */
public class WorkflowSMSAlert implements IsSerializable {
    public static final String RECIPIENT = "RECIPIENT";
    public static final String PROVIDER = "PROVIDER";
    public static final String TEMPLATE = "TEMPLATE";
    private Integer objectID;
    private WorkflowRule workflow;
    private Integer templateID;
    private String templateName;
    private Integer providerID;
    private String providerName;
    private String content;
    private String phone;
    private String taskSMSrecipientType;
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;
    private SelectItem[] smsTemplates;
    private SelectItem[] providers;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public WorkflowRule getWorkflow() {
        return workflow;
    }

    public void setWorkflow(WorkflowRule workflow) {
        this.workflow = workflow;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getTemplateID() {
        return templateID;
    }

    public void setTemplateID(Integer templateID) {
        this.templateID = templateID;
    }

    public Integer getProviderID() {
        return providerID;
    }

    public void setProviderID(Integer providerID) {
        this.providerID = providerID;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public static ArrayList<Integer> getIDsOnly(Set<WorkflowSMSAlert> selectedItems) {
        ArrayList<Integer> result = new ArrayList<>();
        if (selectedItems != null && selectedItems.size() > 0) {
            for (WorkflowSMSAlert item : selectedItems) {
                if (item.getObjectID() != null) {
                    result.add(item.getObjectID());
                }
            }
        }
        return result;
    }

    public SelectItem[] getSmsTemplates() {
        return smsTemplates;
    }

    public void setSmsTemplates(SelectItem[] smsTemplates) {
        this.smsTemplates = smsTemplates;
    }

    public SelectItem[] getProviders() {
        return providers;
    }

    public void setProviders(SelectItem[] providers) {
        this.providers = providers;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTaskSMSrecipientType() {
        return taskSMSrecipientType;
    }

    public void setTaskSMSrecipientType(String taskSMSrecipientType) {
        this.taskSMSrecipientType = taskSMSrecipientType;
    }
}
