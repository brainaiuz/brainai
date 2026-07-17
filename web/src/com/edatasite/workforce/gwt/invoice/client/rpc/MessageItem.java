package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jun 11, 2009
 * Time: 2:00:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageItem extends Relational implements IsSerializable {
    private Integer invoiceID;
    private String toEmail;
    private String toEmails;
    private String firstName;
    private String lastName;
    private boolean access;
    private String mailContent;
    private boolean sendCopyToMe;
    private boolean isClient;
    private boolean isReceipt;
    private Integer contactId;
    private String subject;
    private String type;
    private Integer accountId;

    //For Recurring Invoice only
    private Integer emailTemplateID;
    private Integer pdfTemplateID;
    private Integer senderID;
    private String cc;
    private String bcc;
    private String fromEmail;
    private ArrayList<FileResource> fileResources;
    private String replyTo;
    private boolean includeSubAccountTransaction;

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getToEmails() {
        return toEmails;
    }

    public void setToEmails(String toEmails) {
        this.toEmails = toEmails;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public boolean isSendCopyToMe() {
        return sendCopyToMe;
    }

    public void setSendCopyToMe(boolean sendCopyToMe) {
        this.sendCopyToMe = sendCopyToMe;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public boolean isReceipt() {
        return isReceipt;
    }

    public void setReceipt(boolean receipt) {
        isReceipt = receipt;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getEmailTemplateID() {
        return emailTemplateID;
    }

    public void setEmailTemplateID(Integer emailTemplateID) {
        this.emailTemplateID = emailTemplateID;
    }

    public Integer getPdfTemplateID() {
        return pdfTemplateID;
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        this.pdfTemplateID = pdfTemplateID;
    }

    public Integer getSenderID() {
        return senderID;
    }

    public void setSenderID(Integer senderID) {
        this.senderID = senderID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public ArrayList<FileResource> getFileResources() {
        return fileResources;
    }

    public void setFileResources(ArrayList<FileResource> fileResources) {
        this.fileResources = fileResources;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public boolean isIncludeSubAccountTransaction() {
        return includeSubAccountTransaction;
    }

    public void setIncludeSubAccountTransaction(boolean includeSubAccountTransaction) {
        this.includeSubAccountTransaction = includeSubAccountTransaction;
    }

    public Email getAsEmailObject(){
        Email email = new Email();
        email.setFromEmail(getFromEmail());
        email.setSubject(getSubject());
        email.setContent(getMailContent());
        email.setToEmails(getToEmails());
        email.setCc(getCc());
        email.setBcc(getBcc());
        email.setAttachments(getFileResources());
        email.setReplyTo(getReplyTo());

        return email;
    }

    @Override
    public Integer getRelationID() {
        return null;
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_EMAIL_TRACKER;
    }

    @Override
    public String getRelationName() {
        return null;
    }
}
