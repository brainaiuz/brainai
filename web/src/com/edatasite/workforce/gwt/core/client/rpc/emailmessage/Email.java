package com.edatasite.workforce.gwt.core.client.rpc.emailmessage;

import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 18, 2010
 * Time: 1:04:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Email extends Relational implements IsSerializable {
    //do not change strings, they're used in queries
    public static final String ID = "id";
    public static final String SUBJECT = "subject";
    public static final String FOLDER = "folder";
    public static final String FROM_EMAIL = "from";
    public static final String TO_EMAIL = "to";
    public static final String CREATED_DATE = "createdDate";
    public static final String FETCHED_DATE = "fetchedDate";

    private String objectID;
    private ArrayList<FileResource> attachments = new ArrayList<>();
    private FileItem[] files;
    private String bcc;//Contacts' emails will be set through commas.
    private String cc;//Contacts's emails will be set through commas.
    private String content;
    private Date date;
    private Date receivedDate;
    private String fromName;//Could be used in case of inbox messages. FirstName and LastName of the from user.
    private String fromEmail;//From user's email.
    private long messageUID;
    private String messageUIDHex;
    private String messageId;
    private String replyTo;
    private boolean seen;
    private boolean isAttachment = false;
    private boolean isCorporate = true;
    private boolean isDeleted = false;
    private String subject;
    private String toEmails;//Some messages have several recepients (groups and members). These are those emails.
    private Integer folderID;
    private String folderName;
    private ArrayList<SelectItem> relationType;//id = reletionID it could be taskID, projectID...; name= relation name it could be task name, project name...; description = relation Type it could be TASK, PROJECT...
    private Integer trackerID;
    private String toEmail;
    private String toName;
    private MCFolderType type;
    private long generatedGoogleID = 0;
    private Integer templateId;
    private Integer caseID;
    private Integer settingID;
    private boolean isInvisibleTrackerInSubject;
    private boolean forward;
    private boolean unreadStatusChanged;
    private String clusterType;

    public Email(String toEmail) {
        this();
        this.toEmails = toEmail;
        this.toEmail = toEmail;
    }

    public Email(String toEmail, String subject, String content) {
        this();
        this.toEmails = toEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.content = content;
    }

    public Email() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public MCFolderType getType() {
        return type;
    }

    public void setType(MCFolderType type) {
        this.type = type;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void setToEmailWithName(String toEmailWithName) {
        if (toEmailWithName != null) {
            if (toEmailWithName.contains("<") && toEmailWithName.contains(">")) {
                setToEmail(toEmailWithName.substring(toEmailWithName.indexOf("<") + 1, toEmailWithName.indexOf(">")).trim());
                setToName(toEmailWithName.substring(0, toEmailWithName.indexOf("<")).trim());
            } else {
                setToEmail(toEmailWithName);
            }
        } else {
            setToEmail(null);
            setToName(null);
        }
    }

    public String getMessageUIDHex() {
        return messageUIDHex;
    }

    public void setMessageUIDHex(String messageUIDHex) {
        this.messageUIDHex = messageUIDHex;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ArrayList<FileResource> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<FileResource> attachments) {
        this.attachments = attachments;
    }

    public FileItem[] getFiles() {
        return files;
    }

    public void setFiles(FileItem[] files) {
        this.files = files;
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

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFromEmailWithName() {
        return fromName != null ? fromName + "<" + fromEmail + ">" : fromEmail;
    }

    public void setFromEmailWithName(String fromEmailWithName) {
        if (fromEmailWithName != null) {
            if (fromEmailWithName.contains("<") && fromEmailWithName.contains(">") && fromEmailWithName.indexOf("<") < fromEmailWithName.indexOf(">")) {
                this.fromEmail = fromEmailWithName.substring(fromEmailWithName.indexOf("<") + 1, fromEmailWithName.indexOf(">")).trim();
                this.fromName = fromEmailWithName.substring(0, fromEmailWithName.indexOf("<")).trim();
            } else {
                this.fromEmail = fromEmailWithName;
            }
        } else {
            this.fromEmail = null;
            this.fromName = null;
        }
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public boolean hasAttachments() {
        return attachments.size() > 0;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public boolean isInbox() {
        return MCFolderType.INBOX.equals(getType());
    }

    public long getMessageUID() {
        return messageUID;
    }

    public void setMessageUID(long messageUID) {
        this.messageUID = messageUID;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isAttachment() {
        return isAttachment;
    }

    public void setAttachment(boolean attachment) {
        isAttachment = attachment;
    }

    public boolean isCorporate() {
        return isCorporate;
    }

    public void setCorporate(boolean corporate) {
        isCorporate = corporate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return toEmails;
    }

    public String getToEmails() {
        return toEmails;
    }

    public void setToEmails(String toEmails) {
        this.toEmails = toEmails;
    }

    public Integer getFolderID() {
        return folderID;
    }

    public void setFolderID(Integer folderID) {
        this.folderID = folderID;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public Integer getRelationID() {
        return getTrackerID();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_EMAIL_TRACKER;
    }

    public ArrayList<SelectItem> getRelationTypes() {
        return relationType;
    }

    @Override
    public String getRelationName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setRelationType(ArrayList<SelectItem> relationType) {
        this.relationType = relationType;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public Integer getTrackerID() {
        return trackerID;
    }

    public void setTrackerID(Integer trackerID) {
        this.trackerID = trackerID;
    }

    public long getGeneratedGoogleID() {
        return generatedGoogleID;
    }

    public void setGeneratedGoogleID(String generatedGoogleID) {
        if (generatedGoogleID != null && !"".equals(generatedGoogleID)) {
            this.generatedGoogleID = Long.valueOf(generatedGoogleID);
        }
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public Integer getSettingID() {
        return settingID;
    }

    public void setSettingID(Integer settingID) {
        this.settingID = settingID;
    }

    public boolean isInvisibleTrackerInSubject() {
        return isInvisibleTrackerInSubject;
    }

    public void setIsInvisibleTrackerInSubject(boolean isInvisibleTrackerInSubject) {
        this.isInvisibleTrackerInSubject = isInvisibleTrackerInSubject;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public boolean isUnreadStatusChanged() {
        return unreadStatusChanged;
    }

    public void setUnreadStatusChanged(boolean unreadStatusChanged) {
        this.unreadStatusChanged = unreadStatusChanged;
    }

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType;
    }
}
