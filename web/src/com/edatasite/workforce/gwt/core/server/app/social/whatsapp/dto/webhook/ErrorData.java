package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Error data.
 */
public record ErrorData(

        @JsonProperty("details")
        String details
) {
}