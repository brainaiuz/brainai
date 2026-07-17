package com.edatasite.workforce.gwt.core.server.app.social.revolut.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gwt.user.client.rpc.IsSerializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RevolutResponseDto implements IsSerializable {
    private String id;
    private String public_id;
    private String state;
    private String checkout_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublic_id() {
        return public_id;
    }

    public void setPublic_id(String public_id) {
        this.public_id = public_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCheckout_url() {
        return checkout_url;
    }

    public void setCheckout_url(String checkout_url) {
        this.checkout_url = checkout_url;
    }
}
