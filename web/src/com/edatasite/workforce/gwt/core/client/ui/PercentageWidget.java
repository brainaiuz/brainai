package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Div;

public class PercentageWidget extends Composite {
    private TextBox textBox;

    public PercentageWidget() {
        init();
    }

    public void init() {
        textBox = new TextBox();
        Validation.addPercentageNumericKeyboardListener(textBox, 100, (double) 100);

        Div inputGroup = new Div("input-group");
        inputGroup.add(textBox);
        Div append = new Div("input-group-append");
        inputGroup.add(append);
        Div appendedText = new Div("input-group-text");
        SvgIcon percentageIcon = new SvgIcon("growthchart");
        appendedText.add(percentageIcon);
        append.add(appendedText);


        initWidget(inputGroup);
    }

    public void setText(final String value) {
        textBox.setText(value);
    }

    public String getText() {
        return textBox.getText();
    }

    public TextBox getTextBox() {
        return textBox;
    }

    public void setEnabled(boolean enabled) {
        textBox.setEnabled(enabled);
    }
}
