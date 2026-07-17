package com.edatasite.workforce.gwt.core.client.ui.enums;

public enum TextAlign {
    START("start"),
    END("end"),
    CENTER("center"),
    JUSTIFY("justify"),
    LEFT("left"),
    RIGHT("right"),
    JUSTIFY_ALL("justify-all"),
    MATCH_PARENT("match-parent");

    public final String value;

    TextAlign(String value) {
        this.value = value;
    }
}
