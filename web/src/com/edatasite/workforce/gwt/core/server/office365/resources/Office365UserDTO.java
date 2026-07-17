package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by umakarimov on 9/21/15.
 */
public class Office365UserDTO implements IsSerializable {
    private Integer id;
    private Integer userId;
    private String objectId;

    public Office365UserDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
