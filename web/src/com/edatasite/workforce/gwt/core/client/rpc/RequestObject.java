package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * User: Admin
 * Date: 10.10.2008
 * Time: 17:25:28
 */
public class RequestObject implements IsSerializable {

    private Integer objectID;
    private Integer userID;
    private Boolean IS_LANDSCAPE;
    protected String ids;
    public RequestObject() {
    }

    public RequestObject(Integer objectID) {
        this.objectID = objectID;
    }

    public RequestObject(Integer objectID, Integer userID) {
        this.objectID = objectID;
        this.userID = userID;
    }

    public RequestObject(Integer objectID, Integer userID, Boolean isLandscape) {
        this.objectID = objectID;
        this.userID = userID;
        this.IS_LANDSCAPE = isLandscape;
    }

    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();

        parametersMap.put("objectID", objectID == null ? "" : objectID.toString());
        parametersMap.put("userID", userID == null ? "" : userID.toString());
        parametersMap.put("IS_LANDSCAPE", getIS_LANDSCAPE().toString());
        parametersMap.put("ids", getIds() + "");
        return parametersMap;
    }

    public Boolean getIS_LANDSCAPE() {
        return IS_LANDSCAPE != null && IS_LANDSCAPE;
    }

    public void setIS_LANDSCAPE(Boolean IS_LANDSCAPE) {
        this.IS_LANDSCAPE = IS_LANDSCAPE;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}