package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.HashMap;

/**
 * Created by Azam on 7/25/2017.
 */
public class AdditionalPaymentRequestObject extends RequestObject {

    private Integer pdfTemplateID;

    public AdditionalPaymentRequestObject () {
    }

    public AdditionalPaymentRequestObject(Integer objectID) {
        super(objectID);
    }

    public AdditionalPaymentRequestObject(Integer objectID, Integer userID) {
        super(objectID, userID);
    }

    public AdditionalPaymentRequestObject(Integer objectID, Integer userID, Integer pdfTemplateID) {
        super(objectID, userID);
        this.pdfTemplateID = pdfTemplateID;
    }

    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();

        parametersMap.put("objectID", getObjectID() != null ? getObjectID().toString() : "");
        parametersMap.put("userID", getUserID() != null ? getUserID().toString() : "");
        parametersMap.put("pdfTemplateID", getPdfTemplateID() != null ? getPdfTemplateID().toString() : "");

        return parametersMap;
    }

    public Integer getPdfTemplateID() {
        return pdfTemplateID;
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        this.pdfTemplateID = pdfTemplateID;
    }
}
