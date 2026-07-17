package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.core.domain.settings.EdsSMSTemplates;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowSMSAlert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Azazello on 4/23/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflow_sms_alerts")
public class EdsWorkflowSMSAlert extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow")
    private EdsWorkflowRule workflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsSmsSettings provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "smsTemplate")
    private EdsSMSTemplates smsTemplate;

    @Column(name = "content", length = 5000)
    private String content;

    @Column(name = "phone")
    private String phone;

    @Column(name = "recipientType")
    private String recipientType;

    @Column(name = "deleted")
    private Boolean deleted = false;
    @Column(name = "isworkflowactionTimeBased", columnDefinition = "boolean default false")
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsWorkflowRule getWorkflow() {
        return workflow;
    }

    public void setWorkflow(EdsWorkflowRule workflow) {
        this.workflow = workflow;
    }

    public EdsSmsSettings getProvider() {
        return provider;
    }

    public void setProvider(EdsSmsSettings provider) {
        this.provider = provider;
    }

    public EdsSMSTemplates getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(EdsSMSTemplates smsTemplate) {
        this.smsTemplate = smsTemplate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public WorkflowSMSAlert getRPC(WorkflowSMSAlert smsAlert) {
        smsAlert = smsAlert == null ? new WorkflowSMSAlert() : smsAlert;
        smsAlert.setObjectID(getObjectID());
        if (getProvider() != null) {
            smsAlert.setProviderID(getProvider().getObjectID());
            smsAlert.setProviderName(getProvider().getName());
        }
        if (getSmsTemplate() != null) {
            smsAlert.setTemplateID(getSmsTemplate().getObjectID());
            smsAlert.setTemplateName(getSmsTemplate().getName());
        }
        smsAlert.setContent(getContent());
        smsAlert.setPhone(getPhone());
        smsAlert.setTaskSMSrecipientType(getRecipientType());
        smsAlert.setWorkflow(getWorkflow() != null ? getWorkflow().getRPC(null) : null);
        smsAlert.setWorkflowActionTimeBased(isWorkflowActionTimeBased());
        smsAlert.setWorkflowActionStartTime(getWorkflowActionStartTime());
        smsAlert.setWorkflowActionStartTimeUnit(getWorkflowActionStartTimeUnit());
        smsAlert.setWorkflowActionStartTimeGranularity(getWorkflowActionStartTimeGranularity());
        return smsAlert;
    }
}
