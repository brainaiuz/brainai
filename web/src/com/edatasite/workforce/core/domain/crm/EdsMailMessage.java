/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:33:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Aziz
 * Date: 25.01.2010
 * Time: 13:17:53
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "mailmessage",
        indexes = {
                @Index(columnList = "campaignID", name = "mailmessage_campaignid_idx"),
                @Index(columnList = "smsSettingsID", name = "mailmessage_smssettingsid_idx")
        })
public class EdsMailMessage extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaignID")
    private EdsCampaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "smsSettingsID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EdsSmsSettings smsSettings;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "preheader", length = 100)
    private String preheader;

    @Column(name = "fromEmail")
    private String fromemail;

    @Column(name = "content")
    @Type(type = "text")
    private String content;

    @Column(name = "relinked_message")
    @Type(type = "text")
    private String relinkedMessage;

    @Column(name = "isHtml", columnDefinition = "boolean default false")
    private boolean isHtml = false;

    @Enumerated(EnumType.STRING)
    private MessageStatusEnum statusCode;

    @Column(name = "scheduled")
    private Date scheduled;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted = false;

    @Column(name = "issmsmessage", columnDefinition = "boolean default false")
    private boolean isSmsMessage = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    private EdsUser creator;

    @Column(name = "replyTo")
    private String replyTo;

    public MailMessageItem getRPC(MailMessageItem item) {
        item = item == null ? new MailMessageItem() : item;
        item.setObjectID(getObjectID());
        item.setFrom(getFromemail());
        item.setFullName(getFullName());
        item.setReplyTo(getReplyTo());
        item.setContent(getContent());
        item.setSubject(getSubject());
        item.setPreheader(getPreheader());
        item.setScheduled(getScheduled());
        item.setIsHtml(isHtml());
        item.setStatus(getStatusCode());
        item.setCreationTime(getCreationTime());
        item.setUpdatedTime(getLastUpdateTime());
        item.setSmsMessage(isSmsMessage());
        if (getSmsSettings() != null) {
            item.setSenderID(getSmsSettings().getObjectID());
            item.setSenderName(getSmsSettings().getName());
        }
        if (getCampaign() != null) {
            item.setCampaignId(getCampaign().getObjectID());
            item.setCampaignName(getCampaign().getName());
        }
        return item;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(EdsCampaign campaign) {
        this.campaign = campaign;
    }

    public EdsSmsSettings getSmsSettings() {
        return smsSettings;
    }

    public void setSmsSettings(EdsSmsSettings smsSettings) {
        this.smsSettings = smsSettings;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPreheader() {
        return preheader;
    }

    public void setPreheader(String preheader) {
        this.preheader = preheader;
    }

    public String getFromemail() {
        return fromemail;
    }

    public void setFromemail(String fromemail) {
        this.fromemail = fromemail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRelinkedMessage() {
        return relinkedMessage;
    }

    public void setRelinkedMessage(String relinkedMessage) {
        this.relinkedMessage = relinkedMessage;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    public MessageStatusEnum getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(MessageStatusEnum statusCode) {
        this.statusCode = statusCode;
    }

    public Date getScheduled() {
        return scheduled;
    }

    public void setScheduled(Date scheduled) {
        this.scheduled = scheduled;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isSmsMessage() {
        return isSmsMessage;
    }

    public void setSmsMessage(boolean smsMessage) {
        isSmsMessage = smsMessage;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
}
