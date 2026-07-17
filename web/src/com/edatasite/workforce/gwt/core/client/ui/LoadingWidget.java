package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by dilshod on 12/24/2015.
 */
public class LoadingWidget extends Widget {
    private Element span;

    public LoadingWidget(String code) {
        span = DOM.createSpan();
        span.setClassName(Utils.getThemeName().toLowerCase() + " widget-loading--svg widget-loading");
        if (DOM.getElementById(code) != null) {
            DOM.getElementById(code).appendChild(span);
        }
    }

    public void hide() {
        span.getStyle().setVisibility(Style.Visibility.HIDDEN);
    }

    public void show() {
        span.getStyle().setVisibility(Style.Visibility.VISIBLE);
    }
}
