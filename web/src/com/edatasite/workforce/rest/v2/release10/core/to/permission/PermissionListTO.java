package com.edatasite.workforce.rest.v2.release10.core.to.permission;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 10/18/2017.
 */
public class PermissionListTO extends ResponseData {
    private ArrayList<PermissionTO> permissions;

    public ArrayList<PermissionTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<PermissionTO> permissions) {
        this.permissions = permissions;
    }
}
