package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 09-Jul-2009
 * Time: 18:22:40
 * To change this template use File | Settings | File Templates.
 */
public class MailMessageItem implements IsSerializable {
    public static final String ACTION = "action";
    public static final String SUBJECT = "subject";
    public static final String ID = "id";
    public static final String STATUS = "status";
    public static final String FROM = "fromemail";
    public static final String VIEW_COUNT = "viewcount";
    public static final String CLICK_COUNT = "clickCount";
    public static final String SCHEDULED = "scheduled";
    public static final String CREATED = "createdDate";
    public static final String UPDATED = "updated";
    public static final String IS_SMS_MESSAGE = "isSmsMessage";
    public static final String RECIPIENT = "recipient";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String COUNTRY = "country";
    public static final String LINK = "link";

    //Message details
    private Integer objectID;
    private String subject;
    private String preheader;
    private String from;
    private String fullName;
    private String replyTo;
    private String content;
    private String subscribedLists;
    private Integer campaignId;
    private String campaignName;
    private FileItem[] attachments;
    private ArrayList<FileResource> templateAttachments;
    private boolean smsMessage;
    private boolean isHtml;
    private Date creationTime;
    private Date updatedTime;
    private Integer recurrenceId;
    private Date scheduled;
    private Integer senderID;
    private String senderName;
    private MessageStatusEnum status;
    //Message Statistics
    private Long entitiesCount = 0L;
    private Long sentCount = 0L;
    private Long unsubscribedCount = 0L;
    private Long deliveryCount = 0L;
    private String deliveryRate;
    private Long bouncedCount = 0L;
    private String bouncedRate;
    private Long viewCount = 0L;
    private String viewRate;
    private Long clickCount = 0L;
    private String clickRate;
    //Dropdown Items
    private ArrayList<String> personalAttributes;
    private SelectItem[] senders;
    private SelectItem[] templates;

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

    public String getPreheader() {
        return preheader;
    }

    public void setPreheader(String preheader) {
        this.preheader = preheader;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubscribedLists() {
        return subscribedLists;
    }

    public void setSubscribedLists(String subscribedLists) {
        this.subscribedLists = subscribedLists;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public ArrayList<FileResource> getTemplateAttachments() {
        return templateAttachments;
    }

    public void setTemplateAttachments(ArrayList<FileResource> templateAttachments) {
        this.templateAttachments = templateAttachments;
    }

    public boolean isSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(boolean smsMessage) {
        this.smsMessage = smsMessage;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setIsHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getRecurrenceId() {
        return recurrenceId;
    }

    public void setRecurrenceId(Integer recurrenceId) {
        this.recurrenceId = recurrenceId;
    }

    public Date getScheduled() {
        return scheduled;
    }

    public void setScheduled(Date scheduled) {
        this.scheduled = scheduled;
    }

    public Integer getSenderID() {
        return senderID;
    }

    public void setSenderID(Integer senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public MessageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MessageStatusEnum status) {
        this.status = status;
    }

    public Long getEntitiesCount() {
        return entitiesCount;
    }

    public void setEntitiesCount(Long entitiesCount) {
        this.entitiesCount = entitiesCount;
    }

    public Long getSentCount() {
        return sentCount;
    }

    public void setSentCount(Long sentCount) {
        this.sentCount = sentCount;
    }

    public Long getUnsubscribedCount() {
        return unsubscribedCount;
    }

    public void setUnsubscribedCount(Long unsubscribedCount) {
        this.unsubscribedCount = unsubscribedCount;
    }

    public Long getDeliveryCount() {
        return deliveryCount;
    }

    public void setDeliveryCount(Long deliveryCount) {
        this.deliveryCount = deliveryCount;
    }

    public String getDeliveryRate() {
        return deliveryRate;
    }

    public void setDeliveryRate(String deliveryRate) {
        this.deliveryRate = deliveryRate;
    }

    public Long getBouncedCount() {
        return bouncedCount;
    }

    public void setBouncedCount(Long bouncedCount) {
        this.bouncedCount = bouncedCount;
    }

    public String getBouncedRate() {
        return bouncedRate;
    }

    public void setBouncedRate(String bouncedRate) {
        this.bouncedRate = bouncedRate;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public String getViewRate() {
        return viewRate;
    }

    public void setViewRate(String viewRate) {
        this.viewRate = viewRate;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public String getClickRate() {
        return clickRate;
    }

    public void setClickRate(String clickRate) {
        this.clickRate = clickRate;
    }

    public ArrayList<String> getPersonalAttributes() {
        return personalAttributes;
    }

    public void setPersonalAttributes(ArrayList<String> personalAttributes) {
        this.personalAttributes = personalAttributes;
    }

    public void setSenders(SelectItem[] senders) {
        this.senders = senders;
    }

    public SelectItem[] getSenders() {
        return senders;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }
}