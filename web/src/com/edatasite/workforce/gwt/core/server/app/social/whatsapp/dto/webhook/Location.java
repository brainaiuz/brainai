package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Location.
 */
public record Location(

        @JsonProperty("address") String address,

        @JsonProperty("latitude") double latitude,

        @JsonProperty("name") String name,

        @JsonProperty("longitude") double longitude,

        @JsonProperty("url") String url

) {
}