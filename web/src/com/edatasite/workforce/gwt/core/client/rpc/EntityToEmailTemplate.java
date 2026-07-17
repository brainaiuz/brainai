package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: muratov
 * Date: Mar 22, 2010
 * Time: 6:47:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityToEmailTemplate implements IsSerializable {

    Integer entityId;
    String entityType;
    Integer emailTemplateId;

    Integer mailReceiverId;
    String mailReceiverFirstName;
    String mailReceiverCompanyName;
    String mailReceiverLastName;
    String mailReceiverName;
    String mailReceiverEmail;

    Integer mailSenderId;
    String mailSenderCompanyName;
    String mailSenderFirstName;
    String mailSenderLastName;
    String mailSenderName;
    String mailSenderEmail;


    public EntityToEmailTemplate() {
    }

    public EntityToEmailTemplate(Integer entityId, Integer emailTemplateId, String entityType) {
        this.entityId = entityId;
        this.emailTemplateId = emailTemplateId;
        this.entityType = entityType;
    }


    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(Integer emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

    public Integer getMailReceiverId() {
        return mailReceiverId;
    }

    public void setMailReceiverId(Integer mailReceiverId) {
        this.mailReceiverId = mailReceiverId;
    }

    public String getMailReceiverFirstName() {
        return mailReceiverFirstName;
    }

    public void setMailReceiverFirstName(String mailReceiverFirstName) {
        this.mailReceiverFirstName = mailReceiverFirstName;
    }

    public String getMailReceiverCompanyName() {
        return mailReceiverCompanyName;
    }

    public void setMailReceiverCompanyName(String mailReceiverCompanyName) {
        this.mailReceiverCompanyName = mailReceiverCompanyName;
    }

    public String getMailReceiverLastName() {
        return mailReceiverLastName;
    }

    public void setMailReceiverLastName(String mailReceiverLastName) {
        this.mailReceiverLastName = mailReceiverLastName;
    }

    public String getMailReceiverName() {
        return mailReceiverName;
    }

    public void setMailReceiverName(String mailReceiverName) {
        this.mailReceiverName = mailReceiverName;
    }

    public String getMailReceiverEmail() {
        return mailReceiverEmail;
    }

    public void setMailReceiverEmail(String mailReceiverEmail) {
        this.mailReceiverEmail = mailReceiverEmail;
    }

    public Integer getMailSenderId() {
        return mailSenderId;
    }

    public void setMailSenderId(Integer mailSenderId) {
        this.mailSenderId = mailSenderId;
    }

    public String getMailSenderCompanyName() {
        return mailSenderCompanyName;
    }

    public void setMailSenderCompanyName(String mailSenderCompanyName) {
        this.mailSenderCompanyName = mailSenderCompanyName;
    }

    public String getMailSenderFirstName() {
        return mailSenderFirstName;
    }

    public void setMailSenderFirstName(String mailSenderFirstName) {
        this.mailSenderFirstName = mailSenderFirstName;
    }

    public String getMailSenderLastName() {
        return mailSenderLastName;
    }

    public void setMailSenderLastName(String mailSenderLastName) {
        this.mailSenderLastName = mailSenderLastName;
    }

    public String getMailSenderName() {
        return mailSenderName;
    }

    public void setMailSenderName(String mailSenderName) {
        this.mailSenderName = mailSenderName;
    }

    public String getMailSenderEmail() {
        return mailSenderEmail;
    }

    public void setMailSenderEmail(String mailSenderEmail) {
        this.mailSenderEmail = mailSenderEmail;
    }
}
