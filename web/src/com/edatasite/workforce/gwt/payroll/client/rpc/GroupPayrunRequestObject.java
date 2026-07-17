package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;

import java.util.HashMap;

/**
 * User: Abror Abdukadirov
 * Date: 15.12.2016 16:44
 */
public class GroupPayrunRequestObject extends RequestObject {
    private Integer templateID;
    private String viewType;
    private Boolean IS_LANDSCAPE;

    public GroupPayrunRequestObject() {
    }

    public GroupPayrunRequestObject(Integer objectID, String viewType, Integer templateID) {
        super(objectID);
        this.viewType = viewType;
        this.templateID = templateID;
    }

    @Override
    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();

        parametersMap.put("objectID", getObjectID() == null ? "" : getObjectID().toString());
        parametersMap.put("viewType", viewType == null ? "" : viewType);
        parametersMap.put("templateID", templateID == null ? "" : templateID.toString());
        parametersMap.put("IS_LANDSCAPE", getIS_LANDSCAPE() + "");

        return parametersMap;
    }

    public Integer getTemplateID() {
        return templateID;
    }

    public void setTemplateID(Integer templateID) {
        this.templateID = templateID;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    @Override
    public Boolean getIS_LANDSCAPE() {
        return IS_LANDSCAPE;
    }

    @Override
    public void setIS_LANDSCAPE(Boolean IS_LANDSCAPE) {
        this.IS_LANDSCAPE = IS_LANDSCAPE;
    }
}
