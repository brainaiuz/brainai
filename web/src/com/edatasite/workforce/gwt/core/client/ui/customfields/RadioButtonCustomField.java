package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 09-Nov-2010
 * Time: 17:39:13
 */
public class RadioButtonCustomField extends AbstractCustomField {

    private static final String multiName = "multi-radiobutton";
    private static int num = 0;

    private String name;
    private String selectValue;

    private Map<String, KpiRadioButton> mapRadioButton;

    public RadioButtonCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public RadioButtonCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }

    public RadioButtonCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmform, List<String> fieldCodeName, String customWidgetStyle, String localeCode) {
        super(customFieldItem, wfmform, fieldCodeName, customWidgetStyle, localeCode);
    }

    @Override
    public void initilazation() {
        name = multiName + num++;
        mapRadioButton = new HashMap<>();
        FlowPanel flowPanel = new FlowPanel();
        setSelectValue(flowPanel);
        if (CUSTOM_WIDGET_STYLE != null) {
            flowPanel.setStyleName(CUSTOM_WIDGET_STYLE);
            flowPanel.removeStyleName("form-control");
        }
        addField(customFieldItem.getFieldName(), flowPanel);
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (customFields.getFieldStringValue() != null) {
            if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
                Double dv = Double.valueOf(customFields.getFieldStringValue());
                for (String key : mapRadioButton.keySet()) {
                    if (dv.equals(Double.valueOf(key))) {
                        mapRadioButton.get(key).setValue(true);
                        break;
                    }
                }
            } else {
                if (mapRadioButton.containsKey(customFields.getFieldStringValue())) {
                    mapRadioButton.get(customFields.getFieldStringValue()).setValue(true);
                    selectValue = mapRadioButton.get(customFields.getFieldStringValue()).getText();
                }
            }
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        customFieldItem.setFieldStringValue(selectValue);
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        name = multiName + num++;
        mapRadioButton = new HashMap<>();
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setWidth(FIELD_WIDTH);
        setSelectValue(flowPanel);
        return flowPanel;
    }

    private void setSelectValue(FlowPanel flowPanel) {
        if (customFieldItem.getPredefinedValues() != null) {
            for (String aSplitName : customFieldItem.getPredefinedValues()) {
                final KpiRadioButton radioButton = new KpiRadioButton(name, aSplitName, true);
                radioButton.addClickHandler(event -> {
                    if (radioButton.getValue()) {
                        selectValue = radioButton.getText();
                    }
                });
                radioButton.getElement().getStyle().setPaddingRight(10, Style.Unit.PX);
                flowPanel.add(radioButton);
                mapRadioButton.put(aSplitName, radioButton);
            }
        }
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        customFieldItem.setFieldStringValue(selectValue);
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        return true;
    }
}
