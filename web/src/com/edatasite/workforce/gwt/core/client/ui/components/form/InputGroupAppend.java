package com.edatasite.workforce.gwt.core.client.ui.components.form;

import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

public class InputGroupAppend extends Div {

    public InputGroupAppend(Widget widget) {
        this(widget, true);
    }

    public InputGroupAppend(Widget widget, boolean appendInputGroupText) {
        super("input-group-append");
        if(appendInputGroupText){
            Div content = new Div("input-group-text");
            content.add(widget);
            super.add(content);
        } else {
            super.add(widget);
        }
    }

    public void add(Widget appendWidget) {
        add(appendWidget, true);
    }

    public void add(Widget appendWidget, boolean appendInputGroupText) {
        if(appendInputGroupText){
            Div content = new Div("input-group-text");
            content.add(appendWidget);
            super.add(content);
        } else {
            super.add(appendWidget);
        }
    }
}
