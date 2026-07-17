package com.edatasite.workforce.rest.v3.release10.core.to.crm;

public class WebhookEventDTO {
    private WebhookEventDataDTO webhook;
    private MyCallsEventDTO event;

    // Getters and setters
    public WebhookEventDataDTO getWebhook() {
        return webhook;
    }

    public void setWebhook(WebhookEventDataDTO webhook) {
        this.webhook = webhook;
    }

    public MyCallsEventDTO getEvent() {
        return event;
    }

    public void setEvent(MyCallsEventDTO event) {
        this.event = event;

    }

}
