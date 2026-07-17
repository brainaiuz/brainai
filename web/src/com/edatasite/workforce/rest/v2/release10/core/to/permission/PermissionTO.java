package com.edatasite.workforce.rest.v2.release10.core.to.permission;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 10/18/2017.
 */
public class PermissionTO extends ResponseData {
    private String name;
    private String level;

    public PermissionTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
