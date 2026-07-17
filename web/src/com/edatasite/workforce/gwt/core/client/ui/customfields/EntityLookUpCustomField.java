package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EntityCustomFieldLookUp;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: July 26 2016
 * Time: 17:37:28
 */
public class EntityLookUpCustomField extends AbstractCustomField {

    private EntityCustomFieldLookUp entityCustomFieldLookUp;

    public EntityLookUpCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public EntityLookUpCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }


    @Override
    public void initilazation() {
        entityCustomFieldLookUp = new EntityCustomFieldLookUp(customFieldItem.getQuery());
        entityCustomFieldLookUp.setWidth("200px");
        addField(customFieldItem.getFieldName(), entityCustomFieldLookUp);
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (customFields.getFieldStringValue() != null) {
            entityCustomFieldLookUp.getSuggestBox().showSuggestions(customFields.getFieldStringValue());
            entityCustomFieldLookUp.getSuggestBox().setText(customFields.getFieldStringValue());
            SelectItem item = new SelectItem(customFields.getSelectedId(), customFields.getFieldStringValue());
            entityCustomFieldLookUp.addItem(item);
            entityCustomFieldLookUp.setSelected(item);
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        if (entityCustomFieldLookUp.getSelectedItem() != null) {
            customFieldItem.setSelectedId(entityCustomFieldLookUp.getSelectedItem().getId());
            customFieldItem.setFieldStringValue(entityCustomFieldLookUp.getSelectedItem().getName());
        }
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        entityCustomFieldLookUp = new EntityCustomFieldLookUp(customFieldItem.getQuery());
        return entityCustomFieldLookUp;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        if (entityCustomFieldLookUp.getSelectedItem() != null) {
            customFieldItem.setFieldStringValue(entityCustomFieldLookUp.getSelectedItem().getName());
        }
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        return !(isRequiredField && !Validation.validateListBoxRequired(entityCustomFieldLookUp, customWfmFormField, wfmStrings.pleaseSelect()));
    }

}
