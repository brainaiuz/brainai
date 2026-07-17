package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.google.gwt.user.client.ui.HorizontalPanel;
import gwt.material.design.client.ui.MaterialLink;

public class DateAndOptionsWidget extends HorizontalPanel implements AccountingConstants {

    private DatePicker datePicker;
    private DataListBox dataListBox;

    private FormGroup formGroup;
    private MaterialLink dateLink;
    private MaterialLink optionsLink;

    public DateAndOptionsWidget() {
    }

    private void initialize() {

    }
}
