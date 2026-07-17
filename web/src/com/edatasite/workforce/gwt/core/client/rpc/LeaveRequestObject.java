package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.HashMap;

/**
 * Created by Azam on 8/16/2017.
 */
public class LeaveRequestObject extends RequestObject {

    private Integer pdfTemplateID;
    private String svg;
    public LeaveRequestObject() {
    }

    public LeaveRequestObject(Integer objectID) {
        super(objectID);
    }

    public LeaveRequestObject(Integer objectID, Integer userID) {
        super(objectID, userID);
    }

    public LeaveRequestObject(Integer objectID, Integer userID, Integer pdfTemplateID) {
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

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }
}
