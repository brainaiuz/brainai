package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class FileItem implements IsSerializable {
    private Integer id;
    private String fileName;
    private String description;
    private Long size;
    private Integer attachmentId;
    private String uploadType;
    private String googleDocumentLink;
    private String officeDocumentLink;
    private Date date;
    private String addedBy;
    private String projectName;
    private String taskName;
    private Integer projectID;
    private Integer taskID;
    private String contentType;
    private String issueName;
    private Integer companyID;
    private String companyName;
    private String amazonLink;
    private Integer emailID;
    private String documentID;
    private String documentOpenID;
    private Integer bodyId;
    private Integer typeId;
    private DateNonConvertable expireDate;
    private DateNonConvertable issuedDate;

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName == null ? "noname" : fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getGoogleDocumentLink() {
        return googleDocumentLink;
    }

    public void setGoogleDocumentLink(String googleDocumentLink) {
        this.googleDocumentLink = googleDocumentLink;
    }

    public String getOfficeDocumentLink() {
        return officeDocumentLink;
    }

    public void setOfficeDocumentLink(String officeDocumentLink) {
        this.officeDocumentLink = officeDocumentLink;
    }

    public String getAmazonLink() {
        return amazonLink;
    }

    public void setAmazonLink(String amazonLink) {
        this.amazonLink = amazonLink;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getEmailID() {
        return emailID;
    }

    public void setEmailID(Integer emailID) {
        this.emailID = emailID;
    }

    public String getDownloadUrl() {
        return getDownloadUrl(null);
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public DateNonConvertable getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(DateNonConvertable expireDate) {
        this.expireDate = expireDate;
    }

    public DateNonConvertable getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(DateNonConvertable issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getDocumentOpenID() {
        return documentOpenID;
    }

    public void setDocumentOpenID(String documentOpenID) {
        this.documentOpenID = documentOpenID;
    }

    public Integer getBodyId() {
        return bodyId;
    }

    public void setBodyId(Integer bodyId) {
        this.bodyId = bodyId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getDownloadUrl(String startsWith) {
        String url = ((startsWith != null) ? startsWith : "");
//        return Constants.AMAZON.equals(getUploadType()) ? startsWith + (isDownloadFromEmailServer() ? "downloadEmailAttachment?link=" + encryptedLinkAttribute : "downloadFile?id=" + getAttachmentId().toString()) : getGoogleDocumentLink();
        if (Constants.AMAZON.equals(getUploadType())) {
            return getAmazonLink();
        } else if (Constants.GOOGLE.equals(getUploadType())) {
            url = getGoogleDocumentLink();
        } else if (Constants.OFFICE_365.equals(getUploadType()) || Constants.OFFICE_365_SHARE_POINT.equals(getUploadType())) {
            url = getOfficeDocumentLink();
        } else {
            url = getAmazonLink();
            if (url == null || "".equals(url)) {
                url = CommandConstants.COMMON_URL + "/downloadFile?id=" + getAttachmentId();
            }
        }
        return url;
    }
}
