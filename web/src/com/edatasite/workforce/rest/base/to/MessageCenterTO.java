package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov on 4/14/15 1:45 AM
 */
public class MessageCenterTO implements IsSerializable {

    Integer id;
    String subject;
    String content;
    String fromEmail;
    Integer relationId;
    String relationType;
    SelectItemTO emailTemplate;
    ArrayList<String> toEmail;
    ArrayList<String> bcc;
    ArrayList<String> cc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public ArrayList<String> getToEmail() {
        return toEmail;
    }

    public void setToEmail(ArrayList<String> toEmail) {
        this.toEmail = toEmail;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public SelectItemTO getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(SelectItemTO emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public ArrayList<String> getBcc() {
        return bcc;
    }

    public void setBcc(ArrayList<String> bcc) {
        this.bcc = bcc;
    }

    public ArrayList<String> getCc() {
        return cc;
    }

    public void setCc(ArrayList<String> cc) {
        this.cc = cc;
    }
}
