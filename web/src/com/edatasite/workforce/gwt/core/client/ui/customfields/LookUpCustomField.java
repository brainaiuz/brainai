package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: July 26 2016
 * Time: 17:37:28
 */
public class LookUpCustomField extends AbstractCustomField {

    private CustomFieldLookUp customFieldLookUp;

    public LookUpCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public LookUpCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }


    @Override
    public void initilazation() {
        customFieldLookUp = new CustomFieldLookUp(customFieldItem);
        customFieldLookUp.setWidth("200px");
        addField(customFieldItem.getFieldName(), customFieldLookUp);
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (customFields.getFieldStringValue() != null) {
            customFieldLookUp.getSuggestBox().showSuggestions(customFields.getFieldStringValue());
            customFieldLookUp.getSuggestBox().setText(customFields.getFieldStringValue());
            SelectItem item = new SelectItem(customFields.getSelectedId(), customFields.getFieldStringValue());
            customFieldLookUp.addItem(item);
            customFieldLookUp.setSelected(item);
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        if (customFieldLookUp.getSelectedItem() != null) {
            customFieldItem.setSelectedId(customFieldLookUp.getSelectedItemID());
            customFieldItem.setFieldStringValue(customFieldLookUp.getSelectedItem().getName());
        }
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        customFieldLookUp = new CustomFieldLookUp(customFieldItem);
        return customFieldLookUp;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        if (customFieldLookUp.getSelectedItem() != null) {
            customFieldItem.setSelectedId(customFieldLookUp.getSelectedItem().getId());
            customFieldItem.setFieldStringValue(customFieldLookUp.getSelectedItem().getName());
        }
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        return !(isRequiredField && !Validation.validateListBoxRequired(customFieldLookUp, customWfmFormField, wfmStrings.pleaseSelect()));
    }

}
