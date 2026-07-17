package com.edatasite.workforce.gwt.profile.client.rpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RevolutOrderDto {
    private List<RevolutPaymentDto> payments;

    public List<RevolutPaymentDto> getPayments() {
        return payments;
    }

    public void setPayments(List<RevolutPaymentDto> payments) {
        this.payments = payments;
    }
}
