package com.edatasite.workforce.rest.v3.release10.crm.dto.lead;

import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;

public class OpportunityConvertTO {
    private String name;
    private Double amount;
    private IdNameTO assignee;
    private IdNameTO stage;
    private boolean copyLeadDetails = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public IdNameTO getAssignee() {
        return assignee;
    }

    public void setAssignee(IdNameTO assignee) {
        this.assignee = assignee;
    }

    public IdNameTO getStage() {
        return stage;
    }

    public void setStage(IdNameTO stage) {
        this.stage = stage;
    }

    public boolean isCopyLeadDetails() {
        return copyLeadDetails;
    }

    public void setCopyLeadDetails(boolean copyLeadDetails) {
        this.copyLeadDetails = copyLeadDetails;
    }
}
