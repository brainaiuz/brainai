package com.edatasite.workforce.gwt.core.client.ui.components.groupBox;

import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
@Deprecated
public class GBoxComponent extends MaterialPanel {
    @Deprecated
    public GBoxComponent() {
        super("group-box__item-content");
    }
    @Deprecated
    public GBoxComponent(Widget widget) {
        this();
        add(widget);
    }
}
