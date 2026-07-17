package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Optional.
 *
 * @param name Specifies the sender's profile name.
 */
public record Profile(

        @JsonProperty("name") String name

) {
}