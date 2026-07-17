package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Aug 13, 2009
 * Time: 6:17:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCommentList implements IsSerializable {
    private Integer objectId;
    private ProductCommentItem[] items;
    private Integer userId;
    private String userFullName;
    private String userPictureUrl;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public ProductCommentItem[] getItems() {
        return items;
    }

    public void setItems(ProductCommentItem[] items) {
        this.items = items;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserPictureUrl() {
        return userPictureUrl;
    }

    public void setUserPictureUrl(String userPictureUrl) {
        this.userPictureUrl = userPictureUrl;
    }
}