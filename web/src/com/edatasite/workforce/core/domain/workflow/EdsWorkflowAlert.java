package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAlert;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by Hayot on 3/13/14.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflow_alerts")
public class EdsWorkflowAlert extends EdsObject {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emailTemplateId")
    private EdsEmailTemplate emailTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emailSetting")
    private EdsEmailSetting emailSetting;

    @Column(name = "fromName")
    private String fromName;

    @Column(name = "recepient")
    private String recepient;

    @Column(name = "subject")
    private String subject;

    @Column(name = "ccemails")
    @Type(type = "text")
    private String toCC;

    @Column(name = "content")
    @Type(type = "text")
    private String content;

    @Column(name = "extrakeyvalues")
    @Type(type = "text")
    private String extraKeyValues;

    @Column(name = "replyto")
    private String replyTo;

    @Column(name = "toBCC")
    private String toBCC;

    @Column(name = "includeAttachment", columnDefinition = "boolean default false")
    private boolean includeAttachment = false;

    @Column(name = "includeICal", columnDefinition = "boolean default false")
    private boolean includeICal = false;

    @Column(name = "deleted")
    private Boolean deleted = false;
    @Column(name = "isworkflowactionTimeBased", columnDefinition = "boolean default false")
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;

    @Column(name = "dynamicRecipient", columnDefinition = " boolean default false")
    private boolean dynamicRecipient = false;

    private String dynamicRecipientQuery;

    public EdsWorkflowRule getWorkflow() {
        return workflow;
    }

    public void setWorkflow(EdsWorkflowRule workflow) {
        this.workflow = workflow;
    }

    public EdsEmailSetting getEmailSetting() {
        return emailSetting;
    }

    public void setEmailSetting(EdsEmailSetting emailSetting) {
        this.emailSetting = emailSetting;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public EdsEmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(EdsEmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
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

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtraKeyValues() {
        return extraKeyValues;
    }

    public void setExtraKeyValues(String extraKeyValues) {
        this.extraKeyValues = extraKeyValues;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public boolean isIncludeAttachment() {
        return includeAttachment;
    }


    public boolean isDynamicRecipient() {
        return dynamicRecipient;
    }

    public void setDynamicRecipient(boolean dynamicCondition) {
        this.dynamicRecipient = dynamicCondition;
    }

    public String getDynamicRecipientQuery() {
        return dynamicRecipientQuery;
    }

    public void setDynamicRecipientQuery(String dynamicConditionQuery) {
        this.dynamicRecipientQuery = dynamicConditionQuery;
    }

    public void setIncludeAttachment(boolean includeAttachment) {
        this.includeAttachment = includeAttachment;
    }

    public WorkflowAlert getRPC(WorkflowAlert item) {
        item = item == null ? new WorkflowAlert() : item;
        item.setObjectID(getObjectID());
        item.setContent(getContent());
        item.setSubject(getSubject());
        item.setReplyTo(getReplyTo());
        item.setToBCC(getToBCC());
        item.setToCC(getToCC());
        item.setRecepient(getRecepient());
        item.setDynamicRecipient(isDynamicRecipient());
        item.setDynamicRecipientQuery(getDynamicRecipientQuery());
        item.setEmailSettingID(getEmailSetting() != null ? getEmailSetting().getObjectID() : null);
        item.setFromEmail(getEmailSetting() != null ? getEmailSetting().getEmail() : null);
        item.setFromName(getFromName());
        item.setEmailTemplate(getEmailTemplate() != null ? getEmailTemplate().getAsSelectItem() : null);
        item.setIncludeAttachment(isIncludeAttachment());
        item.setWorkflowRule(getWorkflow() != null ? getWorkflow().getRPC(null) : null);
        item.setWorkflowActionTimeBased(isWorkflowActionTimeBased());
        item.setWorkflowActionStartTime(getWorkflowActionStartTime());
        item.setWorkflowActionStartTimeUnit(getWorkflowActionStartTimeUnit());
        item.setWorkflowActionStartTimeGranularity(getWorkflowActionStartTimeGranularity());
        if (getExtraKeyValues() != null && !"".equals(getExtraKeyValues())) {
            String[] keyValues = getExtraKeyValues().split("||");
            if (keyValues.length > 0) {
                for (String keyValue : keyValues) {
                    String[] keyValue_ = keyValue.split("::");
                    if (keyValue_.length > 0) {
                        item.add(keyValue_[0], keyValue_.length > 1 ? keyValue_[1] : "");
                    }
                }
            }
        }
        return item;
    }

    public static EdsWorkflowAlert fromRPC(EdsWorkflowAlert workflowAlert, WorkflowAlert item) {
        workflowAlert = workflowAlert == null ? new EdsWorkflowAlert() : workflowAlert;
        workflowAlert.setRecepient(item.getRecepient());
        workflowAlert.setDynamicRecipient(item.isDynamicRecipient());
        workflowAlert.setDynamicRecipientQuery(item.getDynamicRecipientQuery());
        workflowAlert.setReplyTo(item.getReplyTo());
        workflowAlert.setSubject(item.getSubject());
        workflowAlert.setFromName(item.getFromName());
        workflowAlert.setToCC(item.getToCC());
        workflowAlert.setToBCC(item.getToBCC());
        workflowAlert.setContent(item.getContent());
        workflowAlert.setIncludeAttachment(item.isIncludeAttachment());
        workflowAlert.setWorkflowActionTimeBased(item.isWorkflowActionTimeBased());
        workflowAlert.setWorkflowActionStartTime(item.getWorkflowActionStartTime());
        workflowAlert.setWorkflowActionStartTimeUnit(item.getWorkflowActionStartTimeUnit());
        workflowAlert.setWorkflowActionStartTimeGranularity(item.getWorkflowActionStartTimeGranularity());
        if (item.getExtraKeyValues().size() > 0) {
            StringBuilder buffer = new StringBuilder();
            boolean skip = true;
            for (Map.Entry<String, String> entry : item.getExtraKeyValues().entrySet()) {
                if (skip) {
                    skip = false;
                } else {
                    buffer.append("||");
                }
                buffer.append(entry.getKey()).append("::").append(entry.getValue());
            }
            workflowAlert.setExtraKeyValues(buffer.toString());
        }
        return workflowAlert;
    }
}
