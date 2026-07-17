package com.workforcetrack.mobile.rpc.messageCenter;

import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 03.09.11
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MUserMailMessage {

    private String objectID;
    private boolean isCorporate = true;
    private boolean inbox;
    private String folderName;//Could be INBOX, OUTBOX, CHAT HISTORY, etcetera...
    private String subject;
    private String from;//Could be used in case of inbox messages. FirstName and LastName of the from user.
    private String fromEmail;//From user's email.
    private Date date;
    private Integer creatorID;
    private long messageUID;
    private String messageID;
    private String trackerID;
    private boolean read;
    private String to;//Could be used in case of outbox messages. Contacts's emails will be set through commas.
    private String toEmails;//Some messages have several recepients (groups and members). These are those emails.
    private ArrayList<MMailMessageAttachment> attachments;


    private String bcc;//Contacts' emails will be set through commas.
    private String cc;//Contacts's emails will be set through commas.
    private String content;


    private List<MSelectItem> relation;


/*
    private Integer messageCenterID;
    private Integer messageCenterInboxID;


    private boolean asDraft;

    */

    /**
     * We need to keep  which exact mail is in  current position the message center
     * listing. Then we will access them when 'newer' or 'older' links are clicked.
     *//*

    private int position;

    private boolean starred;
    private boolean isAttachment = false;

    private boolean isDeleted = false;
*/
    public MUserMailMessage() {
    }

    public MUserMailMessage(Email userMailMessage) {
        this.objectID = userMailMessage.getObjectID();
        this.isCorporate = userMailMessage.isCorporate();
        this.inbox = userMailMessage.isInbox();
        this.folderName = userMailMessage.getFolderName();
        this.subject = userMailMessage.getSubject();
        this.from = userMailMessage.getFromName();
        this.fromEmail = userMailMessage.getFromEmail();
        this.date = userMailMessage.getDate();
        this.messageUID = userMailMessage.getMessageUID();
        this.read = userMailMessage.isSeen();
        this.to = userMailMessage.getToEmails();
        this.toEmails = userMailMessage.getToEmails();
        this.bcc = userMailMessage.getBcc();
        this.cc = userMailMessage.getCc();
        this.content = userMailMessage.getContent();
        this.messageID = userMailMessage.getMessageId();

        if (userMailMessage.getAttachments() != null) {
            attachments = new ArrayList<>();
            for (FileResource mailMessageAttachment : userMailMessage.getAttachments()) {
                attachments.add(new MMailMessageAttachment(mailMessageAttachment));
            }
        }

        relation = WebServiceUtils.getAsMSelectItemList((List) userMailMessage.getRelations());
    }

    public List<MSelectItem> getRelation() {
        return relation;
    }

    public void setRelation(List<MSelectItem> relation) {
        this.relation = relation;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getTrackerID() {
        return trackerID;
    }

    public void setTrackerID(String trackerID) {
        this.trackerID = trackerID;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public boolean isCorporate() {
        return isCorporate;
    }

    public void setCorporate(boolean corporate) {
        isCorporate = corporate;
    }

    public boolean isInbox() {
        return inbox;
    }

    public void setInbox(boolean inbox) {
        this.inbox = inbox;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public long getMessageUID() {
        return messageUID;
    }

    public void setMessageUID(long messageUID) {
        this.messageUID = messageUID;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToEmails() {
        return toEmails;
    }

    public void setToEmails(String toEmails) {
        this.toEmails = toEmails;
    }

    public ArrayList<MMailMessageAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<MMailMessageAttachment> attachments) {
        this.attachments = attachments;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
