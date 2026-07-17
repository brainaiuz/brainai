package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Farrukh Abdurakhmonov on 02/27/201.
 */
public class RemoveFilePermissionTO extends ResponseData {
    private Integer file_id;
    private Integer permission_holder_id;

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }

    public Integer getPermission_holder_id() {
        return permission_holder_id;
    }

    public void setPermission_holder_id(Integer permission_holder_id) {
        this.permission_holder_id = permission_holder_id;
    }
}
