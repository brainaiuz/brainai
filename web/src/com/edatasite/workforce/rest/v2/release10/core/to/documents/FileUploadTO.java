package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class FileUploadTO extends ResponseData {
    private Integer folder_id;
    private String description;

    public FileUploadTO() {
    }

    public Integer getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Integer folder_id) {
        this.folder_id = folder_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
