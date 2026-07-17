package com.workforcetrack.mobile.rpc.crm;

import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 24.01.12
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MEmailItem {

    private String objectID;
    private String subject;
    private String from;
    private String to;
    private Date createdDate;
    private Date startDate;

    private String content;
    private String replyTo;
    private String cc;
    private String bcc;
    private Integer fromUserID;
    private String fromUser;

    private Date date;
    private String userEmail;
    private String toEmail;
    private String fromEmail;
    private String fromName;

    public static MEmailItem convertToMobileCaseInfo(Email email) {
        MEmailItem item = new MEmailItem();
        if (email != null) {
            item.setDate(email.getDate());
            item.setCc(email.getCc());
            item.setBcc(email.getBcc());
            item.setContent(email.getContent());
            item.setToEmail(email.getToEmail());
            item.setFromEmail(email.getFromEmail());
            item.setFromName(email.getFromName());
            item.setSubject(email.getSubject());
        }

        return item;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
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

    public Integer getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(Integer fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
}
