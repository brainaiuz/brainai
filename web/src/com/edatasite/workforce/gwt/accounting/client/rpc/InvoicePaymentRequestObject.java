package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;

import java.util.HashMap;

/**
 * Created by Omonullo on 5/2/2017.
 */
public class InvoicePaymentRequestObject extends RequestObject {
    private Integer objectID;
    private Integer templateID;
    private String viewName;


    public InvoicePaymentRequestObject() {
    }

    public InvoicePaymentRequestObject(Integer objectID) {
        super(objectID);
        this.objectID = objectID;
    }

    public InvoicePaymentRequestObject(Integer objectID, Integer templateID) {
        super(objectID);
        this.objectID = objectID;
        this.templateID = templateID;
    }

    public InvoicePaymentRequestObject(Integer objectID, Integer templateID, String viewName) {
        super(objectID);
        this.objectID = objectID;
        this.templateID = templateID;
        this.viewName = viewName;
    }


    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("objectID", objectID == null ? "" : objectID.toString());
        parametersMap.put("templateID", templateID == null ? "" : templateID.toString());
        parametersMap.put("viewName", viewName == null ? "" : viewName);

        return parametersMap;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getTemplateID() {
        return templateID;
    }

    public void setTemplateID(Integer templateID) {
        this.templateID = templateID;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
