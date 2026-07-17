package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Error.
 */
public record Error(

        @JsonProperty("code")
        int code,

        @JsonProperty("title")
        String title,

        @JsonProperty("message")
        String message,

        @JsonProperty("error_data")
        ErrorData errorData,
        @JsonProperty("href")
        String href
) {
}