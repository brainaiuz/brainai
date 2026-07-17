package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages;

import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type.ParameterType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Text parameter.
 * Required for URL buttons.
 * Developer-provided suffix that is appended to the predefined prefix URL in the template.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextParameter extends Parameter {
    @JsonProperty("text")
    private final String text;


    /**
     * Instantiates a new Text parameter.
     *
     * @param text the text
     */
    public TextParameter(String text) {
        super(ParameterType.TEXT);
        this.text = text;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

}
