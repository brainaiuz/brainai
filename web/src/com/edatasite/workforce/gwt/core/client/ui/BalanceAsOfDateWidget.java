package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Akhror
 * Date: 12/11/2020
 * Time: 19:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class BalanceAsOfDateWidget extends Composite {
    private static final DateTimeFormat format = DateTimeFormat.getFormat("dd/MM/yyyy");
    private DatePicker dateField;
    private TextBox balanceField;


    public BalanceAsOfDateWidget() {
        init();
    }

    private void init() {

        dateField = new DatePicker(format);
        dateField.addStyleName("width250");
        dateField.setEnabled(false);

        balanceField = new TextBox();

        initWidget(new InputGroup(balanceField, dateField));
    }

    public Date getDate() {
        if (dateField.getDate() != null) {
            return dateField.getDate();
        }
        return null;
    }

    public void setDate(Date date) {
        dateField.setDate(date);
    }

    public TextBox getBalanceField() {
        return balanceField;
    }

    public DatePicker getDateField() {
        return dateField;
    }

    public void setEnabled(boolean enable) {
        dateField.setEnabled(enable);
        balanceField.setEnabled(enable);
    }

    public String getText() {
        return balanceField.getText();
    }

    public void setText(String balance) {
        balanceField.setText(balance);
    }

    public boolean isDateFieldEnabled() {
        return dateField.isEnabled();
    }

}
