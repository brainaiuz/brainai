package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

public class PercentageCustomField extends AbstractCustomField {
    private TextBox textBox;

    public PercentageCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public PercentageCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }

    @Override
    public void initilazation() {
        textBox = new TextBox();
        textBox.addStyleName("custom-text");

        Div inputGroup = new Div("input-group");
        inputGroup.add(textBox);
        Div append = new Div("input-group-append");
        inputGroup.add(append);
        Div appendedText = new Div("input-group-text");
        SvgIcon percentageIcon = new SvgIcon("growthchart");
        appendedText.add(percentageIcon);
        append.add(appendedText);

        if (CUSTOM_WIDGET_STYLE != null) {
            textBox.setStyleName(CUSTOM_WIDGET_STYLE);
        } else {
            textBox.addStyleName(DEFAULT_WIDTH);
        }
        addField(customFieldItem.getFieldName(), inputGroup);
        textBox.addKeyPressHandler(new HandlesAllKeyEvents() {
            @Override
            public void onKeyUp(KeyUpEvent event) {

            }

            @Override
            public void onKeyDown(KeyDownEvent event) {

            }

            @Override
            public void onKeyPress(KeyPressEvent event) {

                char key = event.getCharCode();
                if (Utils.isArabicLanguage()) {
                    return;
                }

                if (key == (char) 0) {
                    return;
                }

                if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                        && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                        && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                        && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                        && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                        && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                    ((TextBox) event.getSource()).cancelKey();
                }
                if (textBox.getText() != null && textBox.getText().indexOf('.') != -1 && key == '.') {
                    ((TextBox) event.getSource()).cancelKey();
                }
                if (textBox.getText() != null && key == '\'') {
                    ((TextBox) event.getSource()).cancelKey();
                }

                String validateString = textBox.getText().substring(textBox.getText().lastIndexOf('.') + 1, textBox.getText().length());
                if (textBox.getText().contains(".") && (key == '.' || ((key != (char) KeyCodes.KEY_BACKSPACE)
                        && (textBox.getCursorPos() > textBox.getText().lastIndexOf('.') && validateString.length() >= 2)))) {
                    ((TextBox) event.getSource()).cancelKey();
                    return;
                }
//                if (Character.isDigit(key)) {
//                    boolean isTrue = Double.valueOf(textBox.getValue() + key).compareTo((double) 100) <= 0;
//                    if (!isTrue) {
//                        ((TextBox) event.getSource()).cancelKey();
//                    }
//                }
            }
        });
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (customFields.getFieldStringValue() != null && customFields.getFieldStringValue().trim().length() > 0) {
            textBox.setText(customFields.getFieldStringValue());
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        customFieldItem.setFieldStringValue(textBox.getText());
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        textBox = new TextBox();
        textBox.setWidth(FIELD_WIDTH);
        if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
            textBox.addKeyPressHandler(new HandlesAllKeyEvents() {
                @Override
                public void onKeyUp(KeyUpEvent event) {

                }

                @Override
                public void onKeyDown(KeyDownEvent event) {

                }

                @Override
                public void onKeyPress(KeyPressEvent event) {
                    char key = event.getCharCode();
                    if (Utils.isArabicLanguage()) {
                        return;
                    }

                    if (key == (char) 0) {
                        return;
                    }

                    if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                            && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                            && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                            && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                            && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                            && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                        ((TextBox) event.getSource()).cancelKey();
                    }
                    if (textBox.getText() != null && textBox.getText().indexOf('.') != -1 && key == '.') {
                        ((TextBox) event.getSource()).cancelKey();
                    }
                    if (textBox.getText() != null && key == '\'') {
                        ((TextBox) event.getSource()).cancelKey();
                    }

                    String validateString = textBox.getText().substring(textBox.getText().lastIndexOf('.') + 1, textBox.getText().length());
                    if (textBox.getText().contains(".") && (key == '.' || ((key != (char) KeyCodes.KEY_BACKSPACE)
                            && (textBox.getCursorPos() > textBox.getText().lastIndexOf('.') && validateString.length() >= 2)))) {
                        ((TextBox) event.getSource()).cancelKey();
                        return;
                    }
                    if (Character.isDigit(key)) {
                        boolean isTrue = Double.valueOf(textBox.getValue() + key).compareTo((double) 100) <= 0;
                        if (!isTrue) {
                            ((TextBox) event.getSource()).cancelKey();
                        }
                    }
                }
            });
        }
        return textBox;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        customFieldItem.setFieldStringValue(textBox.getText());
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        if (isRequiredField) {
            boolean validate = Validation.validateIntegerTextBoxRequired(textBox);
            if (validate) {
                if (textBox.getText() != null && textBox.getText().trim().length() > 0 && Double.valueOf(textBox.getValue()).compareTo((double) 100) > 0) {
                    textBox.addStyleName("x-form-invalid");
                    validate = false;
                } else {
                    textBox.removeStyleName("x-form-invalid");
                }
            }
            return validate;
        } else {
            if (textBox.getText() != null && textBox.getText().trim().length() > 0 && Double.valueOf(textBox.getValue()).compareTo((double) 100) > 0) {
                textBox.addStyleName("x-form-invalid");
                return false;
            }
        }
        return true;
    }
}
