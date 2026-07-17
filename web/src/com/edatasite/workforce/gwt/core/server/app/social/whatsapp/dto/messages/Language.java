package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.LanguageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @param code Language code. See {@link LanguageType}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Language(@JsonProperty("code") LanguageType code) {
}
