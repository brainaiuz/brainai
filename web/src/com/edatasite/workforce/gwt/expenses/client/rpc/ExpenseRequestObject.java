package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11/12/13
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseRequestObject extends RequestObject {
    private Integer objectID;
    private boolean onlyImageLinkShow;
    private Integer templateId;

    public ExpenseRequestObject() {
    }

    public ExpenseRequestObject(String ids, Integer templateID) {
        this.ids = ids;
        this.templateId = templateID;
    }

    public ExpenseRequestObject(Integer objectID, boolean onlyImageLinkShow) {
        this.objectID = objectID;
        this.onlyImageLinkShow = onlyImageLinkShow;
    }


    @Override
    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();

        parametersMap.put("objectID", objectID == null ? "" : objectID.toString());
        parametersMap.put("onlyImageLinkShow", onlyImageLinkShow ? "TRUE" : "FALSE");
        parametersMap.put("templateId", templateId != null ? templateId.toString() : "");
        parametersMap.put("ids", ids != null ? ids : "");
        return parametersMap;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public boolean isgetOnlyImageLinkShow() {
        return onlyImageLinkShow;
    }

    public void setOnlyImageLinkShow(boolean onlyImageLinkShow) {
        this.onlyImageLinkShow = onlyImageLinkShow;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }
}
