package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Ban info.
 */
public record BanInfo(@JsonProperty("waba_ban_state") String wabaBanState,

                      @JsonProperty("waba_ban_date") String wabaBanDate

) {
}
