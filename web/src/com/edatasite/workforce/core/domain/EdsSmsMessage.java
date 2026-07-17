package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 22-Aug-2012
 * Time: 07:21:07
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "smsmessage")
public class EdsSmsMessage extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String fromName;
    private String phoneNumber;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text")
    private String text;

    @Enumerated(EnumType.STRING)
    private MessageStatusEnum status;

    @Column(name = "attempts")
    private Integer attempts;

    @Column(name = "companyId")
    private Integer companyID;

    @Column(name = "templateId")
    private Integer templateID;

    @Column(name = "settingId")
    private Integer settingID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public MessageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MessageStatusEnum status) {
        this.status = status;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Integer getSettingID() {
        return settingID;
    }

    public void setSettingID(Integer settingID) {
        this.settingID = settingID;
    }

    public Integer getTemplateID() {
        return templateID;
    }

    public void setTemplateID(Integer templateID) {
        this.templateID = templateID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }
}
