package com.edatasite.workforce.gwt.documents.server.app;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

import java.io.InputStream;

/**
 * User: Sherali
 * Date: 09.06.2010
 * Time: 17:51:37
 */
public class DocumentItem implements Constants {

    public static final String OK = "OK";

    private String status;

    private Integer objectId;

    private Integer folderId;

    private String name;

    private byte[] content;

    private InputStream inputStream;

    private String contentType;

    private String description;

    private String driveFolderId;
    private String driveFolderName;

    private long size = 0;
    private boolean doNotAddToIndex = false;
    private boolean employeeDoc = false;

    private String sourceKey;
    private String sourceBucketName;
    private String destinationBucketName;
    private Long duration;
    private boolean isDownloadable = true;


    public boolean isDoNotAddToIndex() {
        return doNotAddToIndex;
    }

    public void setDoNotAddToIndex(boolean doNotAddToIndex) {
        this.doNotAddToIndex = doNotAddToIndex;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDriveFolderId() {
        return driveFolderId;
    }

    public void setDriveFolderId(String driveFolderId) {
        this.driveFolderId = driveFolderId;
    }

    public String getDriveFolderName() {
        return driveFolderName;
    }

    public void setDriveFolderName(String driveFolderName) {
        this.driveFolderName = driveFolderName;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getSourceBucketName() {
        return sourceBucketName;
    }

    public void setSourceBucketName(String sourceBucketName) {
        this.sourceBucketName = sourceBucketName;
    }

    public String getDestinationBucketName() {
        return destinationBucketName;
    }

    public void setDestinationBucketName(String destinationBucketName) {
        this.destinationBucketName = destinationBucketName;
    }

    public boolean isEmployeeDoc() {
        return employeeDoc;
    }

    public void setEmployeeDoc(boolean employeeDoc) {
        this.employeeDoc = employeeDoc;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public boolean isDownloadable() {
        return isDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        isDownloadable = downloadable;
    }
}
