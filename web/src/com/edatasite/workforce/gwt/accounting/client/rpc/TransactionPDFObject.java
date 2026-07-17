package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;

import java.util.HashMap;

/**
 * Created by Dilshod Madrahimov on 6/16/15 8:20 PM
 */
public class TransactionPDFObject extends RequestObject {

    private Integer objectID;
    private Integer templateID;
    private String viewName;
    private Integer transferType;


    public TransactionPDFObject() {
    }

    public TransactionPDFObject(Integer objectID) {
        super(objectID);
        this.objectID = objectID;
    }

    public TransactionPDFObject(Integer objectID, Integer templateID) {
        super(objectID);
        this.objectID = objectID;
        this.templateID = templateID;
    }

    public TransactionPDFObject(Integer objectID, Integer templateID, String viewName, Integer transferType) {
        super(objectID);
        this.objectID = objectID;
        this.templateID = templateID;
        this.viewName = viewName;
        this.transferType = transferType;
    }

    public TransactionPDFObject(String ids, Integer templateID, String viewName, Integer transferType) {
        this.ids = ids;
        this.objectID = objectID;
        this.templateID = templateID;
        this.viewName = viewName;
        this.transferType = transferType;
    }


    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("objectID", objectID == null ? "" : objectID.toString());
        parametersMap.put("templateID", templateID == null ? "" : templateID.toString());
        parametersMap.put("viewName", viewName == null ? "" : viewName);
        parametersMap.put("transferType", transferType == null ? "" : transferType.toString());
        parametersMap.put("ids", ids == null ? "" : ids);

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

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }
}
