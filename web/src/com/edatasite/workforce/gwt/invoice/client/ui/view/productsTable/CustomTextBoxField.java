package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.google.gwt.user.client.ui.TextBox;

import java.math.BigDecimal;

public class CustomTextBoxField extends TextBox implements CustomCellInterface, CustomFieldInterface {
    private CompanyCustomFieldItem customFieldItem;
    private boolean isNumberwidget;

    public CustomTextBoxField(CompanyCustomFieldItem customFieldItem) {
        super();
        this.customFieldItem = customFieldItem;
        if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
            Integer calcScale = customFieldItem.getScale() != null ? customFieldItem.getScale() : Utils.getAccountingCalculationScale();
            Validation.addNumericKeyboardListener(this, calcScale != null ? calcScale : 2, false);
            setTextAlignment(ALIGN_RIGHT);
            isNumberwidget = true;
        }
        setEnabled(!customFieldItem.isDisabled());
    }

    @Override
    public String getDisplayValue() {
        if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType()) && Utils.isEnableAccountingModule() && getText() != null) {
            String format = (getText().indexOf(':') == -1 ? getText() : getHourValue(getText()));
            BigDecimal price = AccountingUtils.get().parsePriceToBigDecimal(format);
            return AccountingUtils.get().formatCustomPrice(price, customFieldItem.getScale());
        } else {
            return getText();
        }
    }

    private String getHourValue(String text) {
        String[] time = text.split(":");
        return (Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60) + "";
    }


    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }

    @Override
    public CompanyCustomFieldItem getFieldItem() {
        if (customFieldItem != null) {
            customFieldItem.setFieldStringValue(getText());
        }
        return customFieldItem;
    }

    @Override
    public void setFieldItem(CompanyCustomFieldItem fieldItem) {
        if (customFieldItem != null && fieldItem != null) {
            customFieldItem.setObjectId(fieldItem.getObjectId());
            setText(fieldItem.getFieldStringValue() != null ? fieldItem.getFieldStringValue() : "");
        }
    }

    public boolean isNumberwidget() {
        return this.isNumberwidget;
    }

    public void setNumberwidget(final boolean numberwidget) {
        this.isNumberwidget = numberwidget;
    }
}
