package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages;


import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type.ParameterType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * The type Parameter.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Parameter {
    @JsonProperty("type")
    private final ParameterType type;

    /**
     * Instantiates a new Parameter.
     *
     * @param type the type
     */
    protected Parameter(ParameterType type) {
        this.type = type;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public ParameterType getType() {
        return type;
    }
}
