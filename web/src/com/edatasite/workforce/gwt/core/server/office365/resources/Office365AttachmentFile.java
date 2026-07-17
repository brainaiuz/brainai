package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365AttachmentFile extends Office365BaseItem {

    private String contentId;
    private String contentType;
    private String contentLocation;

    private byte[] contentBytes;

    private boolean isContactPhoto;

    public Office365AttachmentFile() {
    }

    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#RESTAPIResourcesFileAttachment
     */
    public Office365AttachmentFile(JSONObject data) {
        this.contentId = this.getString(data, "ContentId");
        this.contentType = this.getString(data, "ContentType");
        this.contentLocation = this.getString(data, "ContentLocation");

        this.contentBytes = DatatypeConverter.parseBase64Binary(this.getString(data, "ContentBytes"));

        this.isContactPhoto = this.getBoolen(data, "IsContactPhoto");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("ContentId", this.getContentId());
        json.put("ContentType", this.getContentType());
        json.put("ContentLocation", this.getContentLocation());

        json.put("ContentBytes", Base64.getEncoder().encode(this.getContentBytes()));

        json.put("IsContactPhoto", this.isContactPhoto());

        return json;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentLocation() {
        return contentLocation;
    }

    public void setContentLocation(String contentLocation) {
        this.contentLocation = contentLocation;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public boolean isContactPhoto() {
        return isContactPhoto;
    }

    public void setContactPhoto(boolean contactPhoto) {
        isContactPhoto = contactPhoto;
    }
}
