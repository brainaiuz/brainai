package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook.type.RestrictionType;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Restriction info.
 */
public record RestrictionInfo(

        @JsonProperty("restriction_type") RestrictionType restrictionType,

        @JsonProperty("expiration") String expiration


) {
}
