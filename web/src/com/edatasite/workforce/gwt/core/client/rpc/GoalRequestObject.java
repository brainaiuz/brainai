package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.HashMap;

public class GoalRequestObject extends RequestObject {

    private String type;

    public GoalRequestObject(){

    }

    public GoalRequestObject(Integer objectId) {
        this.setObjectID(objectId);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("objectID", getObjectID() == null ? "" : getObjectID().toString());
        parametersMap.put("type", getType() == null ? "" : getType());
        return parametersMap;
    }
}
