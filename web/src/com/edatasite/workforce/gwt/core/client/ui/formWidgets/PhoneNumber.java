package com.edatasite.workforce.gwt.core.client.ui.formWidgets;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: 12.03.2010
 * Time: 14:26:31
 * To change this template use File | Settings | File Templates.
 */
public class PhoneNumber extends MaterialPanel implements CustomCellInterface {
    private TextBox phoneFeild;
    private String phone;

    public PhoneNumber(String phone) {
        this.phone = phone;
        refreshFields();
    }

    private void refreshFields() {
        if (phoneFeild == null) {
            phoneFeild = new TextBox();
        }
        phoneFeild.addKeyDownHandler(event -> setValueToVariable());
        phoneFeild.addKeyPressHandler(event -> setValueToVariable());
        phoneFeild.addKeyUpHandler(event -> setValueToVariable());
        phoneFeild.addBlurHandler(event -> setValueToVariable());
        phoneFeild.addStyleName("phoneField");
        if (!Utils.isNullOrEmpty(phone)) {
            phoneFeild.setText(phone);
        }
        phoneFeild.ensureDebugId("phoneFeild");
        phoneFeild.addStyleName("text-input");
    }

    private void setValueToVariable() {
        if (!Utils.isNullOrEmpty(phoneFeild.getText())) {
            phone = phoneFeild.getText();
        }
    }

    @Override
    public String toString() {
        return phoneFeild.getText();
    }

    public MaterialPanel getField() {
        refreshFields();

        MaterialPanel phonePanel = new MaterialPanel("input-group__input");
        MaterialPanel phoneInnerPanel = new MaterialPanel("input-field-wrapper"); // HRMS https://prnt.sc/skx0zy
        phoneInnerPanel.add(phoneFeild);
        phonePanel.add(phoneInnerPanel);
        add(phonePanel);
        addStyleName("input-group fieldset__phone-number");

        return this;
    }

    public void setData(String phone) {
        this.phone = phone;
        this.phoneFeild.setText(phone);
    }

    public TextBox getPhoneFeild() {
        return phoneFeild;
    }

    public void onlyExternal(boolean isTrue) {
        phoneFeild.setMaxLength(isTrue ? 4 : 50);
    }

    public void setEnabled(boolean readOnly) {
        phoneFeild.setEnabled(readOnly);
    }

    public void clearPhoneData() {
        phoneFeild.setText("");
    }

    @Override
    public String getDisplayValue() {
        return toString();
    }

    @Override
    public void setItemValue(Object value) {
        setData(toString());
    }

    @Override
    public void setItemFocus(boolean focused) {
        phoneFeild.setFocus(focused);
    }
}
