package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 22.08.11
 * Time: 12:30
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement
public class MMessageItem {

    private String subject;
    private boolean isClient;
    private Integer invoiceID;
    private boolean sendCopyToMe;
    private String mailContent;
    private Integer contactID;
    private boolean isReceipt;
    private Integer emailTemplateID;
    private boolean access;
    //private String type;


    public MMessageItem() {

    }

    public MMessageItem(MessageItem messageItem) {
        if (messageItem != null) {
            this.subject = messageItem.getSubject();
            this.isClient = messageItem.isClient();
            this.invoiceID = messageItem.getInvoiceID();
            this.sendCopyToMe = messageItem.isSendCopyToMe();
            this.mailContent = messageItem.getMailContent();
            this.contactID = messageItem.getContactId();
            this.isReceipt = messageItem.isReceipt();
            this.emailTemplateID = messageItem.getEmailTemplateID();
            this.access = messageItem.isAccess();
        }
    }

    public MessageItem convertToMessageItem() {

        MessageItem messageItem = new MessageItem();
        messageItem.setSubject(this.subject);
        messageItem.setClient(this.isClient);
        messageItem.setInvoiceID(this.invoiceID);
        messageItem.setSendCopyToMe(this.sendCopyToMe);
        messageItem.setMailContent(this.mailContent);
        messageItem.setContactId(this.contactID);
        messageItem.setReceipt(this.isReceipt);
        messageItem.setEmailTemplateID(this.emailTemplateID);
        messageItem.setAccess(this.access);

        return messageItem;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public boolean isSendCopyToMe() {
        return sendCopyToMe;
    }

    public void setSendCopyToMe(boolean sendCopyToMe) {
        this.sendCopyToMe = sendCopyToMe;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public boolean isReceipt() {
        return isReceipt;
    }

    public void setReceipt(boolean receipt) {
        isReceipt = receipt;
    }

    public Integer getEmailTemplateID() {
        return emailTemplateID;
    }

    public void setEmailTemplateID(Integer emailTemplateID) {
        this.emailTemplateID = emailTemplateID;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }
}
