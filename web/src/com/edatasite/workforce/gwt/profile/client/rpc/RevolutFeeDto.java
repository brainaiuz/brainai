package com.edatasite.workforce.gwt.profile.client.rpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RevolutFeeDto {
    private String type;
    private RevolutAmountDto amount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RevolutAmountDto getAmount() {
        return amount;
    }

    public void setAmount(RevolutAmountDto amount) {
        this.amount = amount;
    }
}
