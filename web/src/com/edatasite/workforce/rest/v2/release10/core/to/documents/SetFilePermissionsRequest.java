package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 11/18/2017.
 */
public class SetFilePermissionsRequest extends ResponseData {

    private ArrayList<PermissionHolderTO> permissions;
    private Integer file_id;

    public SetFilePermissionsRequest() {
    }

    public ArrayList<PermissionHolderTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<PermissionHolderTO> permissions) {
        this.permissions = permissions;
    }

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }
}

