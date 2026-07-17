package com.edatasite.workforce.gwt.core.client.ui.components;

import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.RadioButton;

public class KpiRadioButton extends RadioButton{

    @UiConstructor
    public KpiRadioButton(String name) {
        super(name);
    }

    public KpiRadioButton(String name, SafeHtml label) {
        super(name, "<span>" + label + "</span>");
    }

    public KpiRadioButton(String name, SafeHtml label, HasDirection.Direction dir) {
        super(name, "<span>" + label + "</span>", dir);
    }

    public KpiRadioButton(String name, SafeHtml label, DirectionEstimator directionEstimator) {
        super(name, "<span>" + label + "</span>", directionEstimator);
    }

    public KpiRadioButton(String name, String label) {
        super(name, "<span>" + label + "</span>", true);
    }

    public KpiRadioButton(String name, String label, HasDirection.Direction dir) {
        super(name, "<span>" + label + "</span>", dir);
    }

    public KpiRadioButton(String name, String label, DirectionEstimator directionEstimator) {
        super(name, "<span>" + label + "</span>", directionEstimator);
    }

    public KpiRadioButton(String name, String label, boolean asHTML) {
        super(name, "<span>" + label + "</span>", asHTML);
    }

    @Override
    public void setText(String text) {
        super.setHTML("<span>"+text+"</span>");
    }

    @Override
    public void setText(String text, HasDirection.Direction dir) {
        super.setHTML(SafeHtmlUtils.fromString("<span>"+text+"</span>"), dir);
    }
}
