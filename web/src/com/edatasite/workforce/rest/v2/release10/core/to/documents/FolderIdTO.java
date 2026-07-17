package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Abdurakhmonov Farrukh on 01/20/2018.
 */
public class FolderIdTO extends ResponseData {
    private Integer folder_id;

    public FolderIdTO() {
    }

    public FolderIdTO(Integer folder_id) {
        this.folder_id = folder_id;
    }

    public Integer getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Integer folder_id) {
        this.folder_id = folder_id;
    }
}
