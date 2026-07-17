package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * Created by Hayot on 3/15/14.
 */
public class WorkflowAlert implements IsSerializable {
    private Integer objectID;
    private String recepient;
    private String toCC;
    private String subject;
    private String content;
    private String replyTo;
    private String toBCC;
    private Integer emailSettingID;
    private String fromEmail;
    private String fromName;
    private SelectItem emailTemplate;
    private WorkflowRule workflowRule;
    private HashMap<String, String> extraKeyValues = new HashMap<>();
    private SelectItem[] emailTemplates;
    private SelectItem[] fromUsers;
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;
    private boolean includeAttachment;
    private boolean dynamicRecipient = false;
    private String dynamicRecipientQuery;


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

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getRecepient() {
        return recepient;
    }

    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }

    public String getToCC() {
        return toCC;
    }

    public void setToCC(String toCC) {
        this.toCC = toCC;
    }

    public String getContent() {
        return content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getToBCC() {
        return toBCC;
    }

    public void setToBCC(String toBCC) {
        this.toBCC = toBCC;
    }

    public HashMap<String, String> getExtraKeyValues() {
        return extraKeyValues;
    }

    public void setExtraKeyValues(HashMap<String, String> extraKeyValues) {
        this.extraKeyValues = extraKeyValues;
    }

    public void add(String key, String value) {
        getExtraKeyValues().put(key, value);
    }

    public SelectItem getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(SelectItem emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public WorkflowRule getWorkflowRule() {
        return workflowRule;
    }

    public void setWorkflowRule(WorkflowRule workflowRule) {
        this.workflowRule = workflowRule;
    }

    public SelectItem[] getEmailTemplates() {
        return emailTemplates;
    }

    public void setEmailTemplates(SelectItem[] emailTemplates) {
        this.emailTemplates = emailTemplates;
    }

    public Integer getEmailSettingID() {
        return emailSettingID;
    }

    public void setEmailSettingID(Integer emailSettingID) {
        this.emailSettingID = emailSettingID;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public SelectItem[] getFromUsers() {
        return fromUsers;
    }

    public void setFromUsers(SelectItem[] fromUsers) {
        this.fromUsers = fromUsers;
    }

    public boolean isIncludeAttachment() {
        return includeAttachment;
    }

    public void setIncludeAttachment(boolean includeAttachment) {
        this.includeAttachment = includeAttachment;
    }

    public static ArrayList<Integer> getIDsOnly(Set<WorkflowAlert> selectedItems) {
        ArrayList<Integer> result = new ArrayList<>();
        if (selectedItems != null && selectedItems.size() > 0) {
            for (WorkflowAlert item : selectedItems) {
                if (item.getObjectID() != null) {
                    result.add(item.getObjectID());
                }
            }
        }
        return result;
    }

    public boolean isDynamicRecipient() {
        return dynamicRecipient;
    }

    public void setDynamicRecipient(boolean dynamicRecipient) {
        this.dynamicRecipient = dynamicRecipient;
    }

    public String getDynamicRecipientQuery() {
        return dynamicRecipientQuery;
    }

    public void setDynamicRecipientQuery(String dynamicRecipientQuery) {
        this.dynamicRecipientQuery = dynamicRecipientQuery;
    }
}
