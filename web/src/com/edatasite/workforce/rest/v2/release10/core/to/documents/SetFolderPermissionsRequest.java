package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 11/18/2017.
 */
public class SetFolderPermissionsRequest extends ResponseData {

    private ArrayList<PermissionHolderTO> permissions;
    private Integer folder_id;

    public SetFolderPermissionsRequest() {
    }

    public ArrayList<PermissionHolderTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<PermissionHolderTO> permissions) {
        this.permissions = permissions;
    }

    public Integer getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Integer folder_id) {
        this.folder_id = folder_id;
    }
}

