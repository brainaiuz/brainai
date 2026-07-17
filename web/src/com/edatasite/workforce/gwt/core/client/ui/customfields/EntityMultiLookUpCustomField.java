package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EntityCustomFieldMultiLookUp;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class EntityMultiLookUpCustomField extends AbstractCustomField {

    private EntityCustomFieldMultiLookUp entityCustomFieldMultiLookUp;

    public EntityMultiLookUpCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public EntityMultiLookUpCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }


    @Override
    public void initilazation() {
        entityCustomFieldMultiLookUp = new EntityCustomFieldMultiLookUp(customFieldItem.getQuery());
        entityCustomFieldMultiLookUp.setWidth("200px");
        addField(customFieldItem.getFieldName(), entityCustomFieldMultiLookUp);
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (customFields.getSelectItems() != null && customFields.getSelectItems().size() > 0) {
            entityCustomFieldMultiLookUp.setSelectedItems((ArrayList) customFields.getSelectItems());
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {

        if (entityCustomFieldMultiLookUp.getSelectedItems() != null && entityCustomFieldMultiLookUp.getSelectedItems().size() > 0) {
            customFieldItem.setSelectItems(entityCustomFieldMultiLookUp.getSelectedItems());
        }
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {

        entityCustomFieldMultiLookUp = new EntityCustomFieldMultiLookUp(customFieldItem.getQuery());
        return entityCustomFieldMultiLookUp;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        if (entityCustomFieldMultiLookUp.getSelectedItems() != null && entityCustomFieldMultiLookUp.getSelectedItems().size() > 0) {
            customFieldItem.setSelectItems(entityCustomFieldMultiLookUp.getSelectedItems());
        }
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        return !(isRequiredField && !Validation.validateMultiSelectRequired(entityCustomFieldMultiLookUp, customWfmFormField, wfmStrings.pleaseSelect()));
    }

}
