package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Widget;

import java.util.Arrays;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER_TIME;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 09-Nov-2010
 * Time: 17:41:25
 */
public class DatePickerCustomField extends AbstractCustomField {

    private DatePicker datePicker;
    private DateTimeWidget dateTimeCustomFields;
    private static final DateTimeFormat format = DateTimeFormat.getFormat("HH:mm");

    private boolean isDateNonConvertable;

    public DatePickerCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public DatePickerCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }

    @Override
    public void initilazation() {
        if (UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
            dateTimeCustomFields = new DateTimeWidget(28);
            addField(customFieldItem.getFieldName(), dateTimeCustomFields);
        } else {
            datePicker = new DatePicker();
            datePicker.setDefaultValue();
            if (CUSTOM_WIDGET_STYLE == null) {
                datePicker.setWidth("140px");
            }
            addField(customFieldItem.getFieldName(), datePicker);
        }

        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
            if (isDateNonConvertable) {
                if (customFields.getFieldDateNonConvertedValue() != null) {
                    dateTimeCustomFields.getDateField().setDate(customFields.getFieldDateNonConvertedValue().getNonConvertedDate());

                    String str = format.format(customFields.getFieldDateNonConvertedValue().getNonConvertedDate());
                    String[] arr = str.split(":");
                    dateTimeCustomFields.getTime().setValue(Arrays.stream(arr).mapToInt(Integer::parseInt).toArray());
                }
            } else {
                if (customFields.getFieldDateNonConvertedValue() != null) {
                    datePicker.setDate(customFields.getFieldDateNonConvertedValue().getNonConvertedDate());
                }
            }
        } else {
            if (isDateNonConvertable) {
                if (customFields.getFieldDateNonConvertedValue() != null) {
                    datePicker.setDate(customFields.getFieldDateNonConvertedValue().getNonConvertedDate());
                }
            } else {
                if (customFields.getFieldDateNonConvertedValue() != null) {
                    datePicker.setDate(customFields.getFieldDateNonConvertedValue().getNonConvertedDate());
                }
            }
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        if (UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
            customFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(dateTimeCustomFields.getDateTime()));
            if (isDateNonConvertable) {
                customFieldItem.setFieldDateNonConvertedValue(dateTimeCustomFields.getDateTime() != null ? new DateNonConvertable(dateTimeCustomFields.getDateTime()) : null);
            }
        } else {
            customFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(datePicker.getDate()));
            if (isDateNonConvertable) {
                customFieldItem.setFieldDateNonConvertedValue(datePicker.getDate() != null ? new DateNonConvertable(datePicker.getDate()) : null);
            }
        }
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {

        if (UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
            dateTimeCustomFields = new DateTimeWidget(28);
            return dateTimeCustomFields;
        } else {
            datePicker = new DatePicker();
            datePicker.setDefaultValue();
            datePicker.setWidth(FIELD_WIDTH);
            return datePicker;
        }
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        if (UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
            customFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(dateTimeCustomFields.getDateTime()));
            if (isDateNonConvertable) {
                customFieldItem.setFieldDateNonConvertedValue(dateTimeCustomFields.getDateTime() != null ? new DateNonConvertable(dateTimeCustomFields.getDateTime()) : null);
            }
        } else {
            customFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(datePicker.getDate()));
            if (isDateNonConvertable) {
                customFieldItem.setFieldDateNonConvertedValue(datePicker.getDate() != null ? new DateNonConvertable(datePicker.getDate()) : null);
            }
        }
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        if (UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
            return !isRequiredField || Validation.validateDateTime(dateTimeCustomFields);
        } else {
            return !isRequiredField || Validation.validateDate(datePicker, customWfmFormField, true);
        }
    }

    public void setDateNonConvertable(boolean isDateNonConvertable) {
        this.isDateNonConvertable = isDateNonConvertable;
    }
}
