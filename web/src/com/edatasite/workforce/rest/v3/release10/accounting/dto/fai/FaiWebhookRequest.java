package com.edatasite.workforce.rest.v3.release10.accounting.dto.fai;

public class FaiWebhookRequest {
    private String event;
    private FaiWebhookPayload payload;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public FaiWebhookPayload getPayload() {
        return payload;
    }

    public void setPayload(FaiWebhookPayload payload) {
        this.payload = payload;
    }
}

