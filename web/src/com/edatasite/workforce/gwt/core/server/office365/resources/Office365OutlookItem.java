package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.enums.Office365ItemImportanceType;
import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by umakarimov on 9/30/15.
 */
public abstract class Office365OutlookItem extends Office365BaseItem {
    private String subject;

    private String bodyPreview;
    private Office365OutlookItemBody body;

    private Date dateTimeCreated;
    private Date dateTimeLastModified;

    private String changeKey;
    private Office365ItemImportanceType importance;

    private ArrayList<String> categories;

    private Boolean hasAttachments;
    private ArrayList<Office365Attachment> attachments;

    private String webLink;

    public Office365OutlookItem() {
    }

    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#RESTAPIResourcesEvent
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#RESTAPIResourcesMessage
     */
    public Office365OutlookItem(JSONObject data) {
        this.subject = this.getString(data, "Subject");
        this.bodyPreview = this.getString(data, "BodyPreview");
        this.body = new Office365OutlookItemBody((JSONObject) data.get("Body"));

        this.changeKey = this.getString(data, "ChangeKey");
        this.importance = Office365ItemImportanceType.valueOf(this.getString(data, "Importance"));

        this.dateTimeCreated = this.getDate(this.getString(data, "DateTimeCreated"));
        this.dateTimeLastModified = this.getDate(this.getString(data, "DateTimeLastModified"));

        this.categories = this.getArrayList(data, "Categories", Office365BaseItem.stringMapper);

        this.hasAttachments = this.getBoolen(data, "HasAttachments");

        this.attachments = this.getArrayList(data, "Attachments", new FieldMapper<Office365Attachment>() {
            @Override
            public Office365Attachment map(Object item) {
                return new Office365Attachment((JSONObject) item);
            }
        });

        this.webLink = this.getString(data, "WebLink");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("Subject", this.getSubject());
        json.put("BodyPreview", this.getBodyPreview());
        json.put("Body", this.getBody().toJSON());
        json.put("ChangeKey", this.getChangeKey());
        json.put("Importance", this.getImportance().name());
        json.put("DateTimeCreated", this.formatDate(this.getDateTimeCreated()));
        json.put("DateTimeLastModified", this.formatDate(this.getDateTimeLastModified()));

        json.put("Categories", this.getJSONArray(this.getCategories()));

        json.put("HasAttachments", this.getHasAttachments());
        json.put("Attachments", this.getJSONArray(this.getAttachments()));

        json.put("WebLink", this.getWebLink());

        return json;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyPreview() {
        return bodyPreview;
    }

    public void setBodyPreview(String bodyPreview) {
        this.bodyPreview = bodyPreview;
    }

    public Office365OutlookItemBody getBody() {
        return body;
    }

    public void setBody(Office365OutlookItemBody body) {
        this.body = body;
    }

    public Date getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(Date dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public Date getDateTimeLastModified() {
        return dateTimeLastModified;
    }

    public void setDateTimeLastModified(Date dateTimeLastModified) {
        this.dateTimeLastModified = dateTimeLastModified;
    }

    public String getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(String changeKey) {
        this.changeKey = changeKey;
    }

    public Office365ItemImportanceType getImportance() {
        return importance;
    }

    public void setImportance(Office365ItemImportanceType importance) {
        this.importance = importance;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public Boolean getHasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(Boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public ArrayList<Office365Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Office365Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
}
