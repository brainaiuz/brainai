package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HMRCAuthSettingsItem implements IsSerializable {
    private Boolean submitVatManually;
    private Boolean isAgent;
    private String agentNumber;

    public Boolean getSubmitVatManually() {
        return submitVatManually;
    }

    public void setSubmitVatManually(Boolean submitVatManually) {
        this.submitVatManually = submitVatManually;
    }

    public Boolean getAgent() {
        return isAgent;
    }

    public void setAgent(Boolean agent) {
        isAgent = agent;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }
}
