package com.edatasite.workforce.gwt.core.server.office365.resources;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Message extends Office365OutlookItem {
    private Office365Recipient from;
    private Office365Recipient sender;
    private ArrayList<Office365Recipient> replyTo = new ArrayList<>();
    private ArrayList<Office365Recipient> toRecipients = new ArrayList<>();
    private ArrayList<Office365Recipient> ccRecipients = new ArrayList<>();
    private ArrayList<Office365Recipient> bccRecipients = new ArrayList<>();

    private Office365OutlookItemBody uniqueBody;

    private String conversationId;
    private String parentFolderId;

    private Date dateTimeSent;
    private Date dateTimeReceived;

    private Boolean isDeliveryReceiptRequested;
    private Boolean isDraft;
    private Boolean isRead;
    private Boolean isReadReceiptRequested;

    private final static FieldMapper<Office365Recipient> recipientMapper = new FieldMapper<Office365Recipient>() {
        @Override
        public Office365Recipient map(Object item) {
            return new Office365Recipient();
        }
    };

    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#RESTAPIResourcesMessage
     */
    public Office365Message(JSONObject data) {
        super(data);

        this.conversationId = this.getString(data, "ConversationId");
        this.parentFolderId = this.getString(data, "ParentFolderId");

        this.dateTimeSent = this.getDate(this.getString(data, "DateTimeSent"));
        this.dateTimeReceived = this.getDate(this.getString(data, "DateTimeReceived"));

        this.from = new Office365Recipient();
        this.sender = new Office365Recipient();

        this.replyTo = this.getArrayList(data, "ReplyTo", Office365Message.recipientMapper);
        this.toRecipients = this.getArrayList(data, "ToRecipients", Office365Message.recipientMapper);
        this.ccRecipients = this.getArrayList(data, "CcRecipients", Office365Message.recipientMapper);
        this.bccRecipients = this.getArrayList(data, "BccRecipients", Office365Message.recipientMapper);

        this.uniqueBody = new Office365OutlookItemBody((JSONObject) data.get("UniqueBody"));

        this.isDeliveryReceiptRequested = this.getBoolen(data, "IsDeliveryReceiptRequested");
        this.isDraft = this.getBoolen(data, "IsDraft");
        this.isRead = this.getBoolen(data, "IsRead");
        this.isReadReceiptRequested = this.getBoolen(data, "IsReadReceiptRequested");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();

        json.put("ConversationId", this.getConversationId());
        json.put("ParentFolderId", this.getParentFolderId());

        json.put("DateTimeSent", this.formatDate(this.getDateTimeSent()));
        json.put("DateTimeReceived", this.formatDate(this.getDateTimeReceived()));

        json.put("From", this.getFrom().toJSON());
        json.put("Sender", this.getSender().toJSON());

        json.put("ReplyTo", this.getJSONArray(this.getReplyTo()));
        json.put("ToRecipients", this.getJSONArray(this.getToRecipients()));
        json.put("CcRecipients", this.getJSONArray(this.getCcRecipients()));
        json.put("BccRecipients", this.getJSONArray(this.getBccRecipients()));

        json.put("UniqueBody", this.getUniqueBody());

        json.put("IsDeliveryReceiptRequested", this.getDeliveryReceiptRequested());
        json.put("IsDraft", this.getDraft());
        json.put("IsRead", this.getRead());
        json.put("IsReadReceiptRequested", this.getReadReceiptRequested());

        return json;
    }

    public Office365Recipient getFrom() {
        return from;
    }

    public void setFrom(Office365Recipient from) {
        this.from = from;
    }

    public Office365Recipient getSender() {
        return sender;
    }

    public void setSender(Office365Recipient sender) {
        this.sender = sender;
    }

    public ArrayList<Office365Recipient> getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(ArrayList<Office365Recipient> replyTo) {
        this.replyTo = replyTo;
    }

    public ArrayList<Office365Recipient> getToRecipients() {
        return toRecipients;
    }

    public void setToRecipients(ArrayList<Office365Recipient> toRecipients) {
        this.toRecipients = toRecipients;
    }

    public ArrayList<Office365Recipient> getCcRecipients() {
        return ccRecipients;
    }

    public void setCcRecipients(ArrayList<Office365Recipient> ccRecipients) {
        this.ccRecipients = ccRecipients;
    }

    public ArrayList<Office365Recipient> getBccRecipients() {
        return bccRecipients;
    }

    public void setBccRecipients(ArrayList<Office365Recipient> bccRecipients) {
        this.bccRecipients = bccRecipients;
    }

    public Office365OutlookItemBody getUniqueBody() {
        return uniqueBody;
    }

    public void setUniqueBody(Office365OutlookItemBody uniqueBody) {
        this.uniqueBody = uniqueBody;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public Date getDateTimeSent() {
        return dateTimeSent;
    }

    public void setDateTimeSent(Date dateTimeSent) {
        this.dateTimeSent = dateTimeSent;
    }

    public Date getDateTimeReceived() {
        return dateTimeReceived;
    }

    public void setDateTimeReceived(Date dateTimeReceived) {
        this.dateTimeReceived = dateTimeReceived;
    }

    public Boolean getDeliveryReceiptRequested() {
        return isDeliveryReceiptRequested;
    }

    public void setDeliveryReceiptRequested(Boolean deliveryReceiptRequested) {
        isDeliveryReceiptRequested = deliveryReceiptRequested;
    }

    public Boolean getDraft() {
        return isDraft;
    }

    public void setDraft(Boolean draft) {
        isDraft = draft;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Boolean getReadReceiptRequested() {
        return isReadReceiptRequested;
    }

    public void setReadReceiptRequested(Boolean readReceiptRequested) {
        isReadReceiptRequested = readReceiptRequested;
    }

    public static FieldMapper<Office365Recipient> getRecipientMapper() {
        return recipientMapper;
    }
}
