package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilshod Madrahimov on 02/28/2018.
 */
public class RemoveFolderPermissionTO extends ResponseData {
    private Integer folder_id;
    private Integer permission_holder_id;

    public Integer getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Integer folder_id) {
        this.folder_id = folder_id;
    }

    public Integer getPermission_holder_id() {
        return permission_holder_id;
    }

    public void setPermission_holder_id(Integer permission_holder_id) {
        this.permission_holder_id = permission_holder_id;
    }
}
