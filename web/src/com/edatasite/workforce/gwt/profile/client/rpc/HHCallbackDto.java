package com.edatasite.workforce.gwt.profile.client.rpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HHCallbackDto {
    private String id;
    private String action_type;
    private HHPayloadDto payload;
    private String subscription_id;
    private String user_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public HHPayloadDto getPayload() {
        return payload;
    }

    public void setPayload(HHPayloadDto payload) {
        this.payload = payload;
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
