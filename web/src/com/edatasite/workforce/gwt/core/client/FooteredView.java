package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class FooteredView extends View {

    protected HashMap<String, Widget> systemCustomFieldsMap = new HashMap<>();
    protected List<CompanyCustomFieldItem> systemCustomFields;
    protected static AccountingStrings accountingStrings = AccountingStrings.App.get();

    public FooteredView(String name, String description) {
        super(name, description);
    }

    public FooteredView(String name) {
        super(name);
    }

    public FooteredView() {
    }

    protected Widget getWidgetAsFormControl(String value) {
        HTML formControl = new HTML();
        formControl.setStyleName("form-control");

        if (value != null && !value.isEmpty()) {
            formControl.setHTML(value);
        } else {
            formControl.setHTML("");
        }
        return formControl;
    }

    protected Widget wrapWidgetToFormControl(Widget widget) {

        if (widget != null) {
            widget.addStyleName("form-control");
        }
        return widget;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        RootPanel.get().addStyleName("has-frame__info");
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        RootPanel.get().removeStyleName("has-frame__info");
    }

    protected boolean validateSystemCustomFields() {
        int errors = 0;
        if (systemCustomFields != null && !systemCustomFields.isEmpty()) {
            for (CompanyCustomFieldItem companyCustomFieldItem : systemCustomFields) {
                if (companyCustomFieldItem.isActive() && companyCustomFieldItem.isRequired()) {
                    if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType())) {
                        TextBox textBox = (TextBox) systemCustomFieldsMap.get(companyCustomFieldItem.getColumnCode());
                        errors += !Validation.validateTextBoxRequired(textBox) ? 1 : 0;
                    } else if (Constants.UI_TYPE_CURRENCY.equals(companyCustomFieldItem.getUiType())) {
                        CurrencyWidget currencyWidget = (CurrencyWidget) systemCustomFieldsMap.get(companyCustomFieldItem.getColumnCode());
                        errors += !Validation.validateDataListBoxRequired(currencyWidget.getCurrencyListBox()) ? 1 : 0;
                    }
                }
            }
        }
        return errors == 0;
    }

    protected void addSystemCustomFields(HashMap<String, Widget> widgetsMap) {
        if (systemCustomFields != null && !systemCustomFields.isEmpty()) {
            for (CompanyCustomFieldItem companyCustomFieldItem : systemCustomFields) {
                if (companyCustomFieldItem.isActive()) {
                    String columnCode = companyCustomFieldItem.getColumnCode();
                    Widget widget = systemCustomFieldsMap.get(columnCode);
                    String fieldName = companyCustomFieldItem.getFieldName();
                    if (Objects.equals("inputshowmore", columnCode) || Objects.equals("inputprogressinvoicing", companyCustomFieldItem.getColumnCode())) {
                        fieldName = "";
                    } else if (Objects.equals("inputexrate", columnCode)) {
                        fieldName = wfmStrings.currency();
                    } else if (Objects.equals("inputexptitle", columnCode)) {
                        fieldName = wfmStrings.reportTitle();
                    } else if (Objects.equals("inputtaxcalc", columnCode)) {
                        fieldName = accountingStrings.amounts();
                    }

                    widgetsMap.put(companyCustomFieldItem.getColumnCode(), new FormGroup(fieldName, widget, companyCustomFieldItem.isRequired()));
                    if (widget instanceof TextBox) {
                        ((TextBox) widget).setEnabled(!companyCustomFieldItem.isDisabled());
                    }
                }
            }
        }
    }
}
