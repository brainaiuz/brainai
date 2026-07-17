package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListSearchData;

/**
 * Created by Dilsh0d on 10/27/2017.
 */
public class RootFolderRequestTO extends RequestListSearchData {
    private String root_type;

    public String getRoot_type() {
        return root_type;
    }

    public void setRoot_type(String root_type) {
        this.root_type = root_type;
    }
}
