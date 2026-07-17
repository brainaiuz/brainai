package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.HashMap;

/**
 * User: Abror Abdukadirov
 * Date: 21.09.2018 14:21
 */
public class CrmAccountRequestObject extends RequestObject {

    private String type;
    private Integer pdfTemplateId;

    public CrmAccountRequestObject() {
    }

    public CrmAccountRequestObject(Integer objectId) {
        this.setObjectID(objectId);
    }

    public CrmAccountRequestObject(Integer objectId, Integer pdfTemplateId) {
        this.setObjectID(objectId);
        this.pdfTemplateId = pdfTemplateId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPdfTemplateId() {
        return pdfTemplateId;
    }

    public void setPdfTemplateId(Integer pdfTemplateId) {
        this.pdfTemplateId = pdfTemplateId;
    }

    @Override
    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("objectID", getObjectID() == null ? "" : getObjectID().toString());
        parametersMap.put("userID", getUserID() == null ? "" : getUserID().toString());
        parametersMap.put("type", getType() == null ? "" : getType());
        parametersMap.put("pdfTemplateID", getPdfTemplateId() != null ? getPdfTemplateId().toString() : "");
        return parametersMap;
    }
}
