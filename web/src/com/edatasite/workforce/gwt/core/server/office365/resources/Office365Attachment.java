package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

import java.util.Date;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Attachment extends Office365BaseItem {
    private String id;
    private String name;
    private String contentType;

    private Boolean isInline;

    private Long size;

    private Date dateTimeLastModified;

    private Office365OutlookItem item;
    private Office365AttachmentFile file;

    public Office365Attachment() {
    }

    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#RESTAPIResourcesItemAttachment
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#RESTAPIResourcesFileAttachment
     */
    public Office365Attachment(JSONObject data) {
        this.id = this.getString(data, "Id");
        this.name = this.getString(data, "Name");
        this.contentType = this.getString(data, "ContentType");

        this.isInline = this.getBoolen(data, "IsInline");

        this.size = this.getLong(data, "Size");
        this.dateTimeLastModified = this.getDate(this.getString(data, "DateTimeLastModified"));

        if (data.containsKey("Item")) { // email or event
            JSONObject item = (JSONObject) data.get("Item");

            if (item.containsKey("From")) { // email
                this.item = new Office365Message(item);
            } else { // event
//                this.item = new Office365Event(item);
            }
        } else { // file
            this.file = new Office365AttachmentFile(data);
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("Id", this.id);
        json.put("Name", this.id);
        json.put("ContentType", this.id);
        json.put("IsInline", this.id);
        json.put("Size", this.size);
        json.put("DateTimeLastModified", this.formatDate(this.dateTimeLastModified));

        if (this.item != null) {
            json.put("@odata.type", "#Microsoft.OutlookServices.FileAttachment");
            json.put("Item", this.item.toJSON());
        } else if (this.file != null) {
            json.put("@odata.type", "#Microsoft.OutlookServices.ItemAttachment");
            json.putAll(this.file.toJSON());
        }

        return json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean getInline() {
        return isInline;
    }

    public void setInline(Boolean inline) {
        isInline = inline;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getDateTimeLastModified() {
        return dateTimeLastModified;
    }

    public void setDateTimeLastModified(Date dateTimeLastModified) {
        this.dateTimeLastModified = dateTimeLastModified;
    }

    public Office365OutlookItem getItem() {
        return item;
    }

    public void setItem(Office365OutlookItem item) {
        this.item = item;
    }

    public Office365AttachmentFile getFile() {
        return file;
    }

    public void setFile(Office365AttachmentFile file) {
        this.file = file;
    }
}
