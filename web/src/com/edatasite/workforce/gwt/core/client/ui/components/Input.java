package com.edatasite.workforce.gwt.core.client.ui.components;

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;

public class Input extends MaterialWidget {
    public Input() {
        super(Document.get().createTextInputElement());
    }

    public Input(String... initialClass) {
        super(Document.get().createTextInputElement(), initialClass);
    }
}
