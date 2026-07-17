package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Button.
 *
 * @param payload The developer-defined payload for the button when a business account sends interactive messages.
 * @param text    The button text
 */
public record Button(

        @JsonProperty("payload") String payload,

        @JsonProperty("text") String text

) {
}