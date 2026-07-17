package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 5/5/15 7:46 PM
 */
public class AttachmentTO implements IsSerializable {
    Integer id;
    Integer bodyId;
    String name;
    String description;
    Long creationDate;
    String contentType;
    Long contentLength;
    String uploadType;
    String amazonLink;
    String path;
    String downloadLink;
    Long duration;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBodyId() {
        return bodyId;
    }

    public void setBodyId(Integer bodyId) {
        this.bodyId = bodyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getAmazonLink() {
        return amazonLink;
    }

    public void setAmazonLink(String amazonLink) {
        this.amazonLink = amazonLink;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public AttachmentTO() {
    }

    public AttachmentTO(FileResource item) {
        this.id = item.getObjectId();
        this.bodyId = item.getBodyId();
        this.name = item.getFileName();
        this.description = item.getDescription();
        this.creationDate = WrapUtils.dateToLong(item.getCreationDate());
        this.contentType = item.getContentType();
        this.contentLength = item.getContentLength();
        this.uploadType = item.getUploadType();
        this.amazonLink = item.getAmazonLink();
        this.path = item.getFilePath();
        this.downloadLink = item.getDownloadUrl();
    }

    public FileResource wrap(AttachmentTO attachmentTO) {
        FileResource item = new FileResource();
        item.setObjectId(attachmentTO.getId());
        item.setBodyId(attachmentTO.getBodyId());
        item.setName(attachmentTO.getName());
        item.setDescription(attachmentTO.getDescription());
        item.setCreationDate(WrapUtils.longToDate(attachmentTO.getCreationDate()));
        item.setContentType(attachmentTO.getContentType());
        item.setContentLength(attachmentTO.getContentLength());
        item.setUploadType(attachmentTO.getUploadType());
        item.setAmazonLink(attachmentTO.getAmazonLink());
        item.setPath(attachmentTO.getPath());
        return item;
    }


}
