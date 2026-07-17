package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * User: Azamjon
 * Date: 10 Nov 2019
 */
public class HTMLTextAreaCustomField extends AbstractCustomField {

    private KpiEditor kpiEditor;

    public HTMLTextAreaCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public HTMLTextAreaCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }

    @Override
    public void initilazation() {
        kpiEditor = new KpiEditor();
        if (CUSTOM_WIDGET_STYLE != null) {
            kpiEditor.setStyleName(CUSTOM_WIDGET_STYLE);
        } else {
            kpiEditor.addStyleName(DEFAULT_WIDTH);
        }
        addField(customFieldItem.getFieldName(), kpiEditor);
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (customFields.getFieldStringValue() != null && !"".equals(customFields.getFieldStringValue())) {
            kpiEditor.setData(customFields.getFieldStringValue());
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        customFieldItem.setFieldStringValue(kpiEditor.getData());
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        kpiEditor = new KpiEditor();
        kpiEditor.setWidth(FIELD_WIDTH);
        return kpiEditor;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        customFieldItem.setFieldStringValue(kpiEditor.getData());
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        return !isRequiredField || Validation.validateHTMLTextAreaRequired(kpiEditor, customWfmFormField);
    }
}