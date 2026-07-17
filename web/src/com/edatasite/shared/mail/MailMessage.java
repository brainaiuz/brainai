package com.edatasite.shared.mail;

import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Sep 22, 2010
 * Time: 4:00:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class MailMessage extends Relational implements IsSerializable {

    private ArrayList<Upload> attachments = new ArrayList<>();
    private ArrayList<String> contactsBcc = new ArrayList<>();
    private ArrayList<String> contactsCc = new ArrayList<>();
    private ArrayList<String> contactsTo = new ArrayList<>();
    private ArrayList<String> contactsReplyTo = new ArrayList<>();

    private Integer objectID;
    private String content;
    private String fromEmail;
    private String fromUserFullName;
    private boolean savedAsDraft;
    private String subject;
    private String messageID;
    private boolean invisibleTrackerInSubject;
    private Integer trackerID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getTrackerID() {
        return trackerID;
    }

    public void setTrackerID(Integer trackerID) {
        this.trackerID = trackerID;
    }

    public void addAttachment(Upload attachment) {
        attachments.add(attachment);
    }

    public void addContactBcc(String bcc) {
        contactsBcc.add(bcc);
    }

    public void addContactsCc(String cc) {
        contactsCc.add(cc);
    }

    public void addContactsReplyTo(String replyTo) {
        contactsReplyTo.add(replyTo);
    }

    public void addContactsTo(String to) {
        contactsTo.add(to);
    }

    public void addAllContacts(Collection<String> tos) {
        contactsTo.addAll(tos);
    }

    public ArrayList<Upload> getAttachments() {
        return attachments;
    }

    public ArrayList<String> getContactsBcc() {
        return contactsBcc;
    }

    public ArrayList<String> getContactsCc() {
        return contactsCc;
    }

    public ArrayList<String> getContactsReplyTo() {
        return contactsReplyTo;
    }

    public ArrayList<String> getContactsTo() {
        return contactsTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSavedAsDraft() {
        return savedAsDraft;
    }

    public void setSavedAsDraft(boolean savedAsDraft) {
        this.savedAsDraft = savedAsDraft;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromUserFullName() {
        return fromUserFullName;
    }

    public void setFromUserFullName(String fromUserFullName) {
        this.fromUserFullName = fromUserFullName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
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
        return getSubject();
    }

    public boolean isInvisibleTrackerInSubject() {
        return invisibleTrackerInSubject;
    }

    public void setInvisibleTrackerInSubject(boolean invisibleTrackerInSubject) {
        this.invisibleTrackerInSubject = invisibleTrackerInSubject;
    }
}
