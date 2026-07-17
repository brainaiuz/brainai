package com.edatasite.workforce.gwt.core.server.servlets;

import org.springframework.web.multipart.MultipartFile;

public class WfmMultipartFile {

    private String description;
    private MultipartFile file;
    private String uploadType;

    public WfmMultipartFile(String description, MultipartFile file) {
        this(description, file, null);
    }

    public WfmMultipartFile(String description, MultipartFile file, String uploadType) {
        this.description = description;
        this.file = file;
        this.uploadType = uploadType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }
}
