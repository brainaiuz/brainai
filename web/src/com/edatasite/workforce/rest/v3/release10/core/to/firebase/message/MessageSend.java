package com.edatasite.workforce.rest.v3.release10.core.to.firebase.message;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageSend {
    @JsonAlias("validate_only")
    @JsonProperty("validate_only")
    private boolean validateOnly;
    private Message message;

    public MessageSend() {
    }

    public MessageSend(boolean validateOnly, Message message) {
        this.validateOnly = validateOnly;
        this.message = message;
    }

    public boolean isValidateOnly() {
        return validateOnly;
    }

    public Message getMessage() {
        return message;
    }
}
