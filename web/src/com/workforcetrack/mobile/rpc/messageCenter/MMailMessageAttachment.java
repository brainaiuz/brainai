package com.workforcetrack.mobile.rpc.messageCenter;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 03.09.11
 * Time: 16:14
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MMailMessageAttachment {

    private String contentType;// Extension of the current attachment;
    private String description;
    private String downloadUrl;// Current url provides downloading of whole attachment;
    private String fileName;// Original name of the current attachment;
    private Integer attachmentID;
    private long size;


    public MMailMessageAttachment() {

    }

    public MMailMessageAttachment(FileResource mailMessageAttachment) {
        this.contentType = mailMessageAttachment.getContentType();
        this.description = mailMessageAttachment.getDescription();
        this.downloadUrl = mailMessageAttachment.getEncryptedLinkAttribute();
        this.fileName = mailMessageAttachment.getName();
        this.attachmentID = mailMessageAttachment.getEntityID();
        this.size = mailMessageAttachment.getContentLength() != null ? mailMessageAttachment.getContentLength() : 0L;
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(Integer attachmentID) {
        this.attachmentID = attachmentID;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
