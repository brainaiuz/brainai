package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type;

/**
 * The enum Address type.
 */
public enum AddressType {
    /**
     * Home address type.
     */
    HOME("HOME"),
    /**
     * Work address type.
     */
    WORK("WORK");

    private final String value;

    AddressType(String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
