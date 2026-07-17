package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.google.gwt.event.dom.client.HandlesAllKeyEvents;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 09-Nov-2010
 * Time: 17:37:08
 */
public class TextBoxCustomField extends AbstractCustomField {

    private TextBox textBox;
    private static final NumberFormat numberFormat = NumberFormat.getFormat("###.##");

    public TextBoxCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public TextBoxCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }

    @Override
    public void initilazation() {
        textBox = new TextBox();
        if (CUSTOM_WIDGET_STYLE != null) {
            textBox.setStyleName(CUSTOM_WIDGET_STYLE);
        } else {
            textBox.addStyleName(DEFAULT_WIDTH);
        }
        addField(customFieldItem.getFieldName(), textBox);
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
                    if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                            && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                            && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                            && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                            && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                            && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                        textBox.cancelKey();
                    }
                    if (textBox.getText() != null && textBox.getText().indexOf('.') != -1 && key == '.') {
                        textBox.cancelKey();
                    }
                    if (textBox.getText() != null && key == '\'') {
                        textBox.cancelKey();
                    }
                }
            });
        }
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (customFields.getFieldStringValue() != null && !"".equals(customFields.getFieldStringValue())) {
            if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
                try {
                    textBox.setText(numberFormat.format(Double.valueOf(customFields.getFieldStringValue())));
                } catch (NumberFormatException e) {
                }
            } else {
                textBox.setText(customFields.getFieldStringValue());
            }
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
                    if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                            && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                            && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                            && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                            && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                            && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                        textBox.cancelKey();
                    }
                    if (textBox.getText() != null && textBox.getText().indexOf('.') != -1 && key == '.') {
                        textBox.cancelKey();
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
        if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(customFieldItem.getUiType()) && ((!isRequiredField && textBox.getText().trim().length() > 0) || isRequiredField)) {
            return Validation.validateEmailRequired(textBox, customWfmFormField);
        } else if (Constants.UI_TYPE_URL.equals(customFieldItem.getUiType()) && ((!isRequiredField && textBox.getText().trim().length() > 0) || isRequiredField)) {
            return Validation.validateUrl(textBox, customWfmFormField);
        } else {
            return !isRequiredField || Validation.validateTextBoxRequired(textBox, customWfmFormField);
        }
    }
}
