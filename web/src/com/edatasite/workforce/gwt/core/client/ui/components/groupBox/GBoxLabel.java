package com.edatasite.workforce.gwt.core.client.ui.components.groupBox;

import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class GBoxLabel extends MaterialPanel {
    public GBoxLabel() {
        super("group-box__item-label");
    }
    public GBoxLabel(String label) {
        this();
        add(new MaterialLabel(label));
    }

}
