package com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils;

import com.google.gwt.event.dom.client.ClickHandler;
import gwt.material.design.client.ui.html.Div;

public class WrappedButton extends Div {
    private WfmButton2 wfmButton2;

    public WrappedButton(String html, String styleName) {
        this(html, styleName, null);
    }

    public WrappedButton(String html, String styleName, ClickHandler handler) {
        super();
        wfmButton2 = new WfmButton2(html, styleName, handler);
        add(wfmButton2);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        wfmButton2.setEnabled(false);
    }
}
