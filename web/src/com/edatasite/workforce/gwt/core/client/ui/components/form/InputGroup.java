package com.edatasite.workforce.gwt.core.client.ui.components.form;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

public class InputGroup extends Div {

    public InputGroup() {
        super("input-group");
    }

    public InputGroup(Widget...widgets) {
        this();

        clear();

        if (widgets != null && widgets.length > 0) {
            for (Widget widget : widgets)
                add(widget);
        }
    }

    public Div add(Widget child, boolean asGroupContent) {
        if(asGroupContent){
            Div a = wrapIntoGroupContent(child);
            add(a);
            return a;
        } else {
            add(child);
        }
        return this;
    }

    public static Div wrapIntoGroupText(Widget child){
        Div a = new Div("input-group-text");
        a.add(child);
        return a;
    }

    public static Div wrapIntoGroupContent(String child){
        return wrapIntoGroupContent(new HTML(child));
    }

    public static Div wrapIntoGroupContent(Widget child){
        Div a = new Div("input-group-content");
        a.add(child);
        return a;
    }
}
