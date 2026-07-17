package com.edatasite.workforce.gwt.core.server.controllers.hmrc.dto;

import com.edatasite.workforce.gwt.core.server.controllers.hmrc.HmrcUserType;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.List;

public class HmrcAgentToClientRequest extends ResponseData {

    List<String> service;// ["MTD-IT"]
    HmrcUserType clientType;//"personal"
    String clientIdType; // "ni"
    String clientId;// "AA999999A"
    String knownFact;// "AA11 1AA"

    public HmrcAgentToClientRequest() {
    }

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
    }

    public HmrcUserType getClientType() {
        return clientType;
    }

    public void setClientType(HmrcUserType clientType) {
        this.clientType = clientType;
    }

    public String getClientIdType() {
        return clientIdType;
    }

    public void setClientIdType(String clientIdType) {
        this.clientIdType = clientIdType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getKnownFact() {
        return knownFact;
    }

    public void setKnownFact(String knownFact) {
        this.knownFact = knownFact;
    }
}
