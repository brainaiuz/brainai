package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 07.05.2010
 * Time: 19:56:52
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceQuoteRequestObject extends RequestObject {
    private Integer userID;
    private Integer templateID;
    private Integer contactID;
    private Boolean IS_LANDSCAPE;

    public InvoiceQuoteRequestObject() {
    }

    public InvoiceQuoteRequestObject(Integer objectID) {
        super(objectID);
    }

    public InvoiceQuoteRequestObject(Integer objectID, Integer templateID, Integer userID) {
        super(objectID);
        this.templateID = templateID;
        this.userID = userID;
    }

    public InvoiceQuoteRequestObject(String ids, Integer templateID) {
        this.ids = ids;
        this.templateID = templateID;
    }

    public InvoiceQuoteRequestObject(Integer objectID, Integer templateID, Integer userID, Integer contactID) {
        super(objectID);
        this.userID = userID;
        this.templateID = templateID;
        this.contactID = contactID;
    }

    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();

        parametersMap.put("objectID", getObjectID() == null ? "" : getObjectID().toString());
        parametersMap.put("templateID", templateID == null ? "" : templateID.toString());
        parametersMap.put("userID", userID == null ? "" : userID.toString());
        parametersMap.put("contactID", contactID == null ? "" : contactID.toString());
        parametersMap.put("ids", ids == null ? "" : ids);
        parametersMap.put("IS_LANDSCAPE", getIS_LANDSCAPE() + "");

        return parametersMap;
    }

    @Override
    public Boolean getIS_LANDSCAPE() {
        return IS_LANDSCAPE;
    }

    @Override
    public void setIS_LANDSCAPE(Boolean IS_LANDSCAPE) {
        this.IS_LANDSCAPE = IS_LANDSCAPE;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getTemplateID() {
        return templateID;
    }

    public void setTemplateID(Integer templateID) {
        this.templateID = templateID;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }
}
