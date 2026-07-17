package com.edatasite.workforce.gwt.core.client.ui.components.form;

import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

public class InputGroupPrepend extends Div {

    public InputGroupPrepend(Widget widget, boolean wrapIntoGroupText) {
        super("input-group-prepend");
        if(wrapIntoGroupText){
            Div content = new Div("input-group-text");
            content.add(widget);
            add(content);
        } else{
            add(widget);
        }
    }
}
