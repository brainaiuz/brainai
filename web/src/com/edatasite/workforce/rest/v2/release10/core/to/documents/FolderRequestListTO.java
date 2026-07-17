package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListSearchData;

/**
 * Created by Dilsh0d on 10/27/2017.
 */
public class FolderRequestListTO extends RequestListSearchData {
    private Integer folder_id;

    public Integer getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Integer folder_id) {
        this.folder_id = folder_id;
    }
}
