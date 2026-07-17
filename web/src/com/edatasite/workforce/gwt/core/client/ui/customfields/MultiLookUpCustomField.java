package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectCustomFieldLookUp;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class MultiLookUpCustomField extends AbstractCustomField {

    private MultiSelectCustomFieldLookUp multiSelectCustomFieldLookUp;

    public MultiLookUpCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public MultiLookUpCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }


    @Override
    public void initilazation() {
        multiSelectCustomFieldLookUp = new MultiSelectCustomFieldLookUp(customFieldItem.getLookUpTypeEnum());
        multiSelectCustomFieldLookUp.setWidth("200px");
        addField(customFieldItem.getFieldName(), multiSelectCustomFieldLookUp);
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());

        if (customFields.getSelectItems() != null && customFields.getSelectItems().size() > 0) {
            multiSelectCustomFieldLookUp.setSelectedItems((ArrayList<SelectItem>) customFields.getSelectItems());
        }
    }


    @Override
    public CompanyCustomFieldItem getValue() {
        if (multiSelectCustomFieldLookUp.getSelectedItems() != null && multiSelectCustomFieldLookUp.getSelectedItems().size() > 0) {
            customFieldItem.setSelectItems(multiSelectCustomFieldLookUp.getSelectedItems());
        }
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        multiSelectCustomFieldLookUp = new MultiSelectCustomFieldLookUp(customFieldItem.getLookUpTypeEnum());
        return multiSelectCustomFieldLookUp;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        if (multiSelectCustomFieldLookUp.getSelectedItems() != null && multiSelectCustomFieldLookUp.getSelectedItems().size() > 0) {
            customFieldItem.setSelectItems(multiSelectCustomFieldLookUp.getSelectedItems());
        }
        return customFieldItem;
    }

    @Override
    public boolean validateField() {

        return !(isRequiredField && !Validation.validateMultiSelectRequired(multiSelectCustomFieldLookUp, customWfmFormField, wfmStrings.pleaseSelect()));
    }
}
