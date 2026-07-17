package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 10/27/2017.
 */
public class FolderUpdateTO extends ResponseData {
    private Integer folder_id;
    private String folder_name;

    public Integer getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Integer folder_id) {
        this.folder_id = folder_id;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }
}
