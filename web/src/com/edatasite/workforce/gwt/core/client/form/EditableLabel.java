package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialPanel;

public class EditableLabel extends Composite implements HasValueChangeHandlers<String> {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TextBox changeText;
    private Label text;
    private String originalText;
    private WfmButton2 button;
    private WfmButton2 cancel;
    protected MaterialPanel instance;

    public EditableLabel(String labelText) {
        createEditableLabel(labelText);
    }

    private void createEditableLabel(String labelText) {
        instance = new MaterialPanel("edit-section--inline");

        text = new Label(labelText);
        text.addClickHandler(clickEvent -> changeTextLabel());

        changeText = new TextBox();
        changeText.setText(labelText);

        button = new WfmButton2("", "btn--icon");
        SvgIcon check = new SvgIcon("check");
        button.add(check);
        button.addClickHandler(click -> setTextLabel());

        cancel = new WfmButton2("", "btn--icon");
        SvgIcon cancelSvg = new SvgIcon(SvgEnum.x);
        cancel.add(cancelSvg);
        cancel.addClickHandler(event -> cancelLabelChange());

        changeText.addKeyDownHandler(keyDownEvent -> {
            changeText.removeStyleName(Constants.ERROR_FORM_STYLE);
            if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                setTextLabel();
            } else if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                cancelLabelChange();
            }
        });

        instance.add(text);
        instance.add(changeText);
        instance.add(button);
        instance.add(cancel);

        restoreVisibility(true);

        initWidget(instance);
    }

    private void changeTextLabel() {
        originalText = text.getText();
        changeText.setText(originalText);
        restoreVisibility(false);
        changeText.setFocus(true);
    }

    private void restoreVisibility(boolean visible) {
        text.setVisible(visible);
        changeText.setVisible(!visible);
        button.setVisible(!visible);
        cancel.setVisible(!visible);
        if (visible) {
            instance.removeStyleName("edit-section—editable");
        } else {
            instance.addStyleName("edit-section—editable");
        }
    }

    private void setTextLabel() {
        if (changeText.getText() != null &&
                !changeText.getText().isEmpty() &&
                !changeText.getText().equalsIgnoreCase(originalText)) {
            text.setText(changeText.getText());
            ValueChangeEvent.fire(this, changeText.getText());
        } else {
            Info.warn("You cannot use empty or same text again");
            changeText.setStyleName(Constants.ERROR_FORM_STYLE);
        }
        restoreVisibility(true);
    }

    public void cancelLabelChange() {
        text.setText(originalText);
        restoreVisibility(true);
    }

    public String getText() {
        return text.getText();
    }

    public void setText(String newText) {
        text.setText(newText);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
