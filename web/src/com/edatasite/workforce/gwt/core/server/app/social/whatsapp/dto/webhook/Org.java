package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Org.
 */
public record Org(

        @JsonProperty("company") String company,

        @JsonProperty("department") String department,

        @JsonProperty("title") String title) {

}