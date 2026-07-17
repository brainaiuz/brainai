package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 3/1/13
 * Time: 3:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class TextAreaCustomField extends AbstractCustomField {

    private TextArea textArea;

    public TextAreaCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public TextAreaCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }

    @Override
    public void initilazation() {
        textArea = new TextArea();
        if (CUSTOM_WIDGET_STYLE != null) {
            textArea.setStyleName(CUSTOM_WIDGET_STYLE);
        } else {
            textArea.addStyleName(DEFAULT_WIDTH);
            textArea.setStyleName("file--TextAreaCustomField");
        }
        addField(customFieldItem.getFieldName(), textArea);
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (customFields.getFieldStringValue() != null && !"".equals(customFields.getFieldStringValue())) {
            textArea.setText(customFields.getFieldStringValue());
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        customFieldItem.setFieldStringValue(textArea.getText());
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        textArea = new TextArea();
        textArea.setWidth(FIELD_WIDTH);
        return textArea;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        customFieldItem.setFieldStringValue(textArea.getText());
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        return !isRequiredField || Validation.validateTextAreaRequired(textArea, customWfmFormField);
    }
}
