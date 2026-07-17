package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class MessageItem implements IsSerializable {
    public static final String FROM = "FROM";
    public static final String TO = "TO";
    public static final String SUBJECT = "SUBJECT";
    public static final String STATUS = "STATUS";
    public static final String CREATION_DATE = "CREATION_DATE";
    public static final String SENT_DATE = "SENT_DATE";
    public static final String ATTEMPTS = "ATTEMPTS";
    private String from;
    private String to;
    private String subject;
    private MessageStatusEnum status;
    private Date creationDate;
    private Date sentDate;
    private Integer attempts;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    public MessageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MessageStatusEnum status) {
        this.status = status;
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

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }
}
