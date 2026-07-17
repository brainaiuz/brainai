package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;

import java.util.List;


/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 09-Nov-2010
 * Time: 17:42:58
 */
public abstract class AbstractCustomField {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    protected static final String FIELD_WIDTH = "80px";

    protected String CUSTOM_WIDGET_STYLE;

    private WfmCustomFieldsForm wfmFrom;
    protected List<String> fieldCodeName;
    protected CompanyCustomFieldItem customFieldItem;
    protected WfmForm.Field customWfmFormField;
    protected String localeCode = null;

    public boolean isRequiredField = false;
    public Label label = null;
    public Widget customWidget = null;

    // Custom fields Forms
    public AbstractCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName) {
        this.wfmFrom = wfmForm;
        this.fieldCodeName = fieldCodeName;
        this.customFieldItem = customFieldItem;
        initilazation();
    }

    // Custom fields Forms
    public AbstractCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        this.wfmFrom = wfmForm;
        this.fieldCodeName = fieldCodeName;
        this.customFieldItem = customFieldItem;
        this.CUSTOM_WIDGET_STYLE = customWidgetStyle;
        initilazation();
    }

    // Custom fields Forms with locale
    public AbstractCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle, String localeCode) {
        this.wfmFrom = wfmForm;
        this.fieldCodeName = fieldCodeName;
        this.customFieldItem = customFieldItem;
        this.CUSTOM_WIDGET_STYLE = customWidgetStyle;
        this.localeCode = localeCode;
        initilazation();
    }

    // Custom Fields Widget
    public AbstractCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        this.fieldCodeName = fieldCodeName;
        this.customFieldItem = customFieldItem;
    }

    public abstract void initilazation();

    public abstract void setValue(CompanyCustomFieldItem customFields);

    public abstract CompanyCustomFieldItem getValue();

    public abstract Widget getCustomFieldWidget();

    public abstract CompanyCustomFieldItem getWidgetValue();

    public abstract boolean validateField();

    protected void addField(String fieldName, Widget customWidget) {
        this.customWidget = customWidget;
        if (customFieldItem.getCustomLogicField() != null) {
            customWidget.setVisible(false);
            label = new Label(fieldName);
            label.setVisible(false);
        }
        isRequiredField = customFieldItem.isRequired();
        if (fieldCodeName != null && fieldCodeName.indexOf(customFieldItem.getColumnCode()) != -1) {
            customWfmFormField = wfmFrom.addField(fieldCodeName.indexOf(customFieldItem.getColumnCode()), fieldName, customWidget, isRequiredField, label);
        } else {
            customWfmFormField = wfmFrom.addField(wfmFrom.getFields().size(), fieldName, customWidget, isRequiredField, label);
        }
    }

    public Label getLabel() {
        return label;
    }

    public Widget getCustomWidget() {
        return customWidget;
    }

    public String getColumnCode() {
        return customFieldItem != null ? customFieldItem.getColumnCode() : null;
    }

    protected CompanyCustomFieldItem clone() {
        CompanyCustomFieldItem fieldItem = new CompanyCustomFieldItem();
        fieldItem.setDataType(customFieldItem.getDataType());
        fieldItem.setColumnCode(customFieldItem.getColumnCode());
        return fieldItem;
    }
}
