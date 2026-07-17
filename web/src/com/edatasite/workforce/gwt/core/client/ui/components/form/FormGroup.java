package com.edatasite.workforce.gwt.core.client.ui.components.form;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

public class FormGroup extends Div {

    private Div groupLabel;
    private Div groupContent;


    public FormGroup() {
        super("form-group");

        groupLabel = new Div("form-group__label");
        groupContent = new Div("form-group__content");

        add(groupLabel);
        add(groupContent);
    }

    public FormGroup (Widget widget) {
        this((String) null, widget);
    }

    public FormGroup(String label, Widget widget) {
        this(label, widget, false);
    }

    public FormGroup(String label, Widget widget, boolean required) {
        this();

        if (!Utils.isNullOrEmpty(label)) {
            groupLabel.getElement().setInnerHTML(label);
            if (required) {
                groupLabel.addStyleName("form-label--required");
            }
        }
        groupContent.add(widget);
    }

    public FormGroup(String label, Widget widget1, Widget widget2) {
        this();

        if (!Utils.isNullOrEmpty(label)) {
            groupLabel.getElement().setInnerHTML(label);
        }
        groupContent.add(widget1);
        groupContent.add(widget2);
    }

    public void setLabelGroupStyle(boolean addStyleName) {
        if (groupLabel == null) {
            return;
        }
        if (addStyleName) {
            groupLabel.addStyleName("label-group");
        } else {
            groupLabel.removeStyleName("label-group");
        }
    }

    public FormGroup(Widget label, Widget widget) {
        this();

        if (label != null)
            groupLabel.add(label);

        groupContent.add(widget);
    }

    public void setLabel(String label) {
        this.setLabel(label, false);
    }

    public void setLabel(String label, boolean required) {
        if (!Utils.isNullOrEmpty(label)) {
            groupLabel.getElement().setInnerHTML(label);
            if (required) {
                groupLabel.addStyleName("form-label--required");
            }
        }
    }

    public void addToContent(Widget widget) {
        groupContent.add(widget);
    }

    public void addToContentRow(Widget dropDown, Widget datePicker) {
        groupContent.add(dropDown);
        groupContent.add(datePicker);
    }

    public void setContent(Widget widget) {
        groupContent.clear();
        groupContent.add(widget);
    }

    public Div getGroupLabel() {
        return groupLabel;
    }

    public Div getGroupContent() {
        return groupContent;
    }

    public void setAutocompleteOff(){
       getElement().setAttribute("autocomplete", "off");
    }
}
