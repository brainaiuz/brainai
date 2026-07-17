package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessToken(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") String expiresIn

) {
}
