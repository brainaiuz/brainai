package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 09-Nov-2010
 * Time: 17:38:57
 */
public class CheckBoxCustomField extends AbstractCustomField {

    private static final String multiName = "multi-checkbox";
    private static int num = 0;

    private String name;
    private Map<String, KpiCheckBox> mapCheckBox;

    public CheckBoxCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public CheckBoxCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }

    @Override
    public void initilazation() {
        name = multiName + num++;
        mapCheckBox = new HashMap<>();
        FlowPanel flowPanel = new FlowPanel();
        setValues(flowPanel);
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
            String[] splitValue = customFields.getFieldStringValue().split(",");
            if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
                for (String aSplitValue : splitValue) {
                    Double dv = Double.valueOf(aSplitValue);
                    for (String key : mapCheckBox.keySet()) {
                        if (dv.equals(Double.valueOf(key))) {
                            mapCheckBox.get(key).setValue(true);
                        }
                    }
                }
            } else {
                for (String aSplitValue : splitValue) {
                    if (mapCheckBox.containsKey(aSplitValue)) {
                        mapCheckBox.get(aSplitValue).setValue(true);
                    }
                }
            }
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        String values = "";
        for (KpiCheckBox checkBox : mapCheckBox.values()) {
            if (checkBox.getValue()) {
                if (!"".equals(values)) {
                    values = values + ",";
                }
                values = values + checkBox.getText();
            }
        }
        customFieldItem.setFieldStringValue(values);
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        name = multiName + num++;
        mapCheckBox = new HashMap<>();
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setWidth(FIELD_WIDTH);
        setValues(flowPanel);
        return flowPanel;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        String values = "";
        for (KpiCheckBox checkBox : mapCheckBox.values()) {
            if (checkBox.getValue()) {
                if (!"".equals(values)) {
                    values = values + ",";
                }
                values = values + checkBox.getText();
            }
        }
        customFieldItem.setFieldStringValue(values);
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        boolean isChecked = !isRequiredField;
        if (isRequiredField) {
            for (KpiCheckBox checkBox : mapCheckBox.values()) {
                if (checkBox.getValue()) {
                    isChecked = true;
                    break;
                } else {
                    checkBox.addStyleName(ERROR_FORM_STYLE);
                }
            }
        }
        return isChecked;
    }

    private void setValues(FlowPanel flowPanel) {
        if (customFieldItem.getPredefinedValues() != null) {
            for (String aSplitName : customFieldItem.getPredefinedValues()) {
                KpiCheckBox checkBox = new KpiCheckBox(aSplitName, true);
                checkBox.setName(name);
                checkBox.getElement().getStyle().setPaddingRight(10, Style.Unit.PX);
                flowPanel.add(checkBox);
                mapCheckBox.put(aSplitName, checkBox);
            }
        }
    }
}
