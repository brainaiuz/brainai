package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 05.07.11
 * Time: 18:52
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MEntityToEmailTemplate {

    private Integer objectID; //Expense objectID
    private String entityType; // .EXPENSE_CLAIM_CATEGORY_SUBMIT or .EXPENSE_CLAIM_CATEGORY_RESUBMIT
    private Integer emailTemplateID;
    private Integer mailReceiverId;

    public MEntityToEmailTemplate() {
    }

    public MEntityToEmailTemplate(EntityToEmailTemplate entityToEmailTemplate) {
        if (entityToEmailTemplate != null) {
            this.objectID = entityToEmailTemplate.getEntityId();
            this.entityType = entityToEmailTemplate.getEntityType();
            this.emailTemplateID = entityToEmailTemplate.getEmailTemplateId();
            this.mailReceiverId = entityToEmailTemplate.getMailReceiverId();
        }
    }

    public EntityToEmailTemplate convertToEntityToEmailTemplate(EntityToEmailTemplate entity) {
        if (entity == null)
            entity = new EntityToEmailTemplate();

        entity.setEntityId(this.objectID);
        entity.setEntityType(this.entityType);
        entity.setEmailTemplateId(this.emailTemplateID);
        entity.setMailReceiverId(this.mailReceiverId);

        return entity;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getEmailTemplateID() {
        return emailTemplateID;
    }

    public void setEmailTemplateID(Integer emailTemplateID) {
        this.emailTemplateID = emailTemplateID;
    }

    public Integer getMailReceiverId() {
        return mailReceiverId;
    }

    public void setMailReceiverId(Integer mailReceiverId) {
        this.mailReceiverId = mailReceiverId;
    }
}
