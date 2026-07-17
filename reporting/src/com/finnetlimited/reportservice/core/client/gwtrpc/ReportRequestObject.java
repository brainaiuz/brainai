package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SecuritryType;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Apr 21, 2011
 * Time: 1:30:37 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ReportRequestObject extends RequestObject {
    private Integer userID;
    private String xml;

    public ReportRequestObject(Integer objectID, Integer userID) {
        super(objectID);
        this.userID = userID;
    }

    public ReportRequestObject(Integer userID) {
        this.userID = userID;
    }

    public ReportRequestObject() {
    }

    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();

        parametersMap.put("objectID", getObjectID() == null ? "" : getObjectID().toString());
        parametersMap.put("userID", userID == null ? "" : userID.toString());
        parametersMap.put(SecuritryType.ReportXmlString.name(), xml);

        return parametersMap;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
