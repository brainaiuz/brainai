package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.TextBox;

import java.math.BigDecimal;

public class CustomPercentageField extends TextBox implements CustomCellInterface, CustomFieldInterface {
    private CompanyCustomFieldItem customFieldItem;

    public CustomPercentageField(CompanyCustomFieldItem customFieldItem) {
        super();
        this.customFieldItem = customFieldItem;

        addKeyPressHandler(new HandlesAllKeyEvents() {
            @Override
            public void onKeyUp(KeyUpEvent event) {

            }

            @Override
            public void onKeyDown(KeyDownEvent event) {

            }

            @Override
            public void onKeyPress(KeyPressEvent event) {

                char key = event.getCharCode();
                if (Utils.isArabicLanguage()) {
                    return;
                }

                if (key == (char) 0) {
                    return;
                }

                if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                        && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                        && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                        && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                        && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                        && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                    ((TextBox) event.getSource()).cancelKey();
                }
                if (getText() != null && getText().indexOf('.') != -1 && key == '.') {
                    ((TextBox) event.getSource()).cancelKey();
                }
                if (getText() != null && key == '\'') {
                    ((TextBox) event.getSource()).cancelKey();
                }

                String validateString = getText().substring(getText().lastIndexOf('.') + 1, getText().length());
                if (getText().contains(".") && (key == '.' || ((key != (char) KeyCodes.KEY_BACKSPACE)
                        && (getCursorPos() > getText().lastIndexOf('.') && validateString.length() >= 2)))) {
                    ((TextBox) event.getSource()).cancelKey();
                    return;
                }
            }
        });

    }

    @Override
    public String getDisplayValue() {
        if (Utils.isEnableAccountingModule() && getText() != null) {
            String format = (getText().indexOf(':') == -1 ? getText() : getHourValue(getText()));
            BigDecimal price = AccountingUtils.get().parsePriceToBigDecimal(format);
            return AccountingUtils.get().formatCustomPrice(price);
        } else {
            return "";
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
}
