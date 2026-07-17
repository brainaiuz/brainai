package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Name.
 */
public record Name(

        @JsonProperty("prefix") String prefix,

        @JsonProperty("last_name") String lastName,

        @JsonProperty("middle_name") String middleName,

        @JsonProperty("suffix") String suffix,

        @JsonProperty("first_name") String firstName,

        @JsonProperty("formatted_name") String formattedName

) {
}