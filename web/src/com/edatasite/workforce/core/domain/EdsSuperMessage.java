package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.mail.Upload;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.MessageTypeEnum;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Azazello on 7/11/2017.
 */
@MappedSuperclass
public class EdsSuperMessage extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "toEmail")
    @Type(type = "text")
    private String to;
    private String fromName;
    private String fromEmail;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, columnDefinition = "timestamp DEFAULT current_timestamp")
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp DEFAULT current_timestamp")
    private Date sentDate;

    @Column(name = "cc")
    @Type(type = "text")
    private String cc;

    @Column(name = "bcc")
    @Type(type = "text")
    private String bcc;

    @Column(name = "subject")
    @Type(type = "text")
    private String subject;

    @Column(name = "text")
    @Type(type = "text")
    private String text;

    @Column(name = "invitation_content")
    @Type(type = "text")
    private String invitationContent;

    @Enumerated(EnumType.STRING)
    private MessageStatusEnum status;

    @Enumerated(EnumType.STRING)
    private MessageTypeEnum type;

    @Column(name = "attempts")
    private Integer attempts;

    @Column(name = "displaySubject")
    @Type(type = "text")
    private String displaySubject;

    @Column(name = "replyTo")
    private String replyTo;

    @Column(name = "companyId")
    private Integer companyID;

    private Boolean attachment = false;

    @Column(name = "is_system", columnDefinition = "boolean default false")
    private Boolean isSystem = false;

    @Column(name = "is_test")
    private Boolean isTest = false;

    @Transient
    List<Upload> uploads = new ArrayList<>();

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public List<Upload> getUploads() {
        return uploads;
    }

    public void setUploads(List<Upload> uploads) {
        this.uploads = uploads;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getInvitationContent() {
        return invitationContent;
    }

    public void setInvitationContent(String invitationContent) {
        this.invitationContent = invitationContent;
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

    public void addBcc(String bcc) {
        if (bcc != null && !"".equals(bcc)) {
            if (this.bcc == null) {
                this.bcc = bcc;
            } else {
                this.bcc += "," + bcc;
            }
        }
    }

    public MessageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MessageStatusEnum status) {
        this.status = status;
        if (MessageStatusEnum.SENT.equals(status)) {
            setSentDate(new Date());
        }
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public List<EdsUpload> getFileIDs() {
        return null;
    }

    public void setFileIDs(List<EdsUpload> fileIDs) {
    }

    public Boolean getAttachment() {
        return attachment;
    }

    public void setAttachment(Boolean attachment) {
        this.attachment = attachment;
    }

    public String getDisplaySubject() {
        return displaySubject;
    }

    public void setDisplaySubject(String displaySubject) {
        this.displaySubject = displaySubject;
    }

    public MessageTypeEnum getType() {
        return type;
    }

    public void setType(MessageTypeEnum type) {
        this.type = type;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
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

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Boolean getTest() {
        return isTest;
    }

    public void setTest(Boolean test) {
        isTest = test;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }
}
