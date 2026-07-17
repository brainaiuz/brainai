package com.edatasite.workforce.gwt.profile.client.rpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RevolutPaymentDto {
    private List<RevolutFeeDto> fees;

    public List<RevolutFeeDto> getFees() {
        return fees;
    }

    public void setFees(List<RevolutFeeDto> fees) {
        this.fees = fees;
    }
}
