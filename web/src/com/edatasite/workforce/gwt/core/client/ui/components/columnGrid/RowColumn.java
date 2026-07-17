package com.edatasite.workforce.gwt.core.client.ui.components.columnGrid;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;


public class RowColumn extends Composite implements Constants {

    interface RowColumnUiBinder extends UiBinder<Widget, RowColumn> {
    }

    private static RowColumnUiBinder ourUiBinder = GWT.create(RowColumnUiBinder.class);

    @UiField
    Heading label;
    @UiField
    Div component;

    public RowColumn() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public RowColumn(String label, Widget widget) {
        this();
        this.setLabel(label);
        this.setComponent(widget);
    }

    public RowColumn(Widget widget, String label) {
        this(label, widget);
    }

    public RowColumn(Widget widget, String label, boolean required) {
        this();
        this.setComponent(widget);
        if (required) {
            this.label.add(new HTML(label + "<em class=\"redTitle\">*</em>:"));
        } else {
            this.setLabel(label);
        }
    }

    public void setLabel(String text) {
        label.getElement().setInnerHTML(text);
    }

    public void setComponent(Widget widget) {
        component.clear();
        component.add(widget);
    }

    public void addComponent(Widget widget) {
        component.add(widget);
    }
}
