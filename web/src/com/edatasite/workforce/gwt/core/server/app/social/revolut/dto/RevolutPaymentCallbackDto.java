package com.edatasite.workforce.gwt.core.server.app.social.revolut.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RevolutPaymentCallbackDto {
    private String event;
    private String order_id;

    public String getEvent() {
        return event;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
