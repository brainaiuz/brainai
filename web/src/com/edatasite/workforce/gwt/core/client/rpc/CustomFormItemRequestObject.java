package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.HashMap;

/**
 * Created by Azam on 9/28/2019
 */
public class CustomFormItemRequestObject extends RequestObject {

    private Integer pdfTemplateID;
    private Integer fid;
    private boolean isNotForQRCode = true;
    public CustomFormItemRequestObject() {
    }

    public CustomFormItemRequestObject(Integer objectID) {
        super(objectID);
    }

    public CustomFormItemRequestObject(Integer objectID, Integer userID) {
        super(objectID, userID);
    }

    public CustomFormItemRequestObject(Integer objectID, Integer userID, Integer pdfTemplateID) {
        super(objectID, userID);
        this.pdfTemplateID = pdfTemplateID;
    }

    public Integer getPdfTemplateID() {
        return pdfTemplateID;
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        this.pdfTemplateID = pdfTemplateID;
    }

    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<String, String>();

        parametersMap.put("objectID", getObjectID() != null ? getObjectID().toString() : "");
        parametersMap.put("userID", getUserID() != null ? getUserID().toString() : "");
        parametersMap.put("pdfTemplateID", getPdfTemplateID() != null ? getPdfTemplateID().toString() : "");

        return parametersMap;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public boolean isNotForQRCode() {
        return isNotForQRCode;
    }

    public void setIsNotForQRCode(boolean isNotForQRCode) {
        this.isNotForQRCode = isNotForQRCode;
    }
}
