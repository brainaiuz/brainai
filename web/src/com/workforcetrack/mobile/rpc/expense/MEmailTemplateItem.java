package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 05.07.11
 * Time: 14:08
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "emailTemplateItem")
public class MEmailTemplateItem {

    public static final String TEMPLATE_NAME = "templateName";
    public static final String TEMPLATE_SUBJECT = "templateSubject";
    public static final String TEMPLATE_CATEGORY = "templateCategory";
    public static final String TEMPLATE_IS_DEFAULT = "templateIsDefault";
    private Integer objectID;
    private String name;
    private String subject;
    //private boolean isDefault = false;
    //private Integer categoryID;
    //private String categoryName;
    private String fromEmail;
    private String messageHTML;
    //private String testEmail;
    //private Integer fromUserID;
    //private String fromUserName;
    private String toEmail;
    //private Integer companyID;
    //private String isCompanyEmailTemplate;

    public MEmailTemplateItem() {

    }

    public MEmailTemplateItem(EmailTemplateItem emailTemplateItem) {
        if (emailTemplateItem != null) {
            this.objectID = emailTemplateItem.getObjectId();
            this.name = emailTemplateItem.getName();
            this.subject = emailTemplateItem.getSubject();
            this.fromEmail = emailTemplateItem.getFromEmail();
            this.messageHTML = emailTemplateItem.getMessageHTML();
            this.toEmail = emailTemplateItem.getToEmail();
        }
    }

    public EmailTemplateItem convertToEmailTemplateItem(EmailTemplateItem emailTemplateItem) {
        if (emailTemplateItem == null) {
            emailTemplateItem = new EmailTemplateItem();
        }

        emailTemplateItem.setObjectId(this.objectID);
        emailTemplateItem.setName(this.name);

        return emailTemplateItem;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getMessageHTML() {
        return messageHTML;
    }

    public void setMessageHTML(String messageHTML) {
        this.messageHTML = messageHTML;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }
}
