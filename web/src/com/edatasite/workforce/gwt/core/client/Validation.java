package com.edatasite.workforce.gwt.core.client;


import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.KpiDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.TextBox2;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm.Field;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectVacancyLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.ui.MaterialTextBox;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Jan 16, 2008
 * Time: 12:54:08 PM
 * To change this template use File | Settings | File Templates.
 */

public class Validation implements Constants {


    public static final WfmMessages wfmMessages = WfmMessages.App.get();
    public static WfmStrings wfmStrings = WfmStrings.App.get();

    public static boolean validateTextBoxRequired(TextBoxBase textBox, final WfmForm.Field field) {
        if (textBox.getText() == null || "".equals(textBox.getText())) {
            field.setErrorMessage(wfmStrings.pleaseEnterValue(), ERROR_FORM_STYLE);
            textBox.addKeyDownHandler(event -> {
                if (((TextBox) event.getSource()).getText().length() < 1) {
                    field.setErrorMessage(wfmStrings.pleaseEnterValue(), ERROR_FORM_STYLE);
                } else {
                    field.setErrorMessage(null, "");
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateTextBoxRequiredAndCharLimit(String fieldName, TextBoxBase textBox, String charLimit) {
        if (textBox.getText() == null || "".equals(textBox.getText()) || "".equals(textBox.getText().trim())) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.addKeyDownHandler(event -> {
                TextBox textbox = (TextBox) event.getSource();
                if (textbox.getText().length() < 1) {
                    textbox.addStyleName(ERROR_FORM_STYLE);
                } else {
                    if (!"".equals(textbox.getStyleName())) {
                        textbox.removeStyleName(ERROR_FORM_STYLE);
                    }
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        if (charLimit != null && !"".equals(charLimit) && textBox.getText().trim().length() != Integer.parseInt(charLimit)) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.addKeyDownHandler(event -> {
                TextBox textbox = (TextBox) event.getSource();
                if (textbox.getText().trim().length() != Integer.parseInt(charLimit)) {
                    textbox.addStyleName(ERROR_FORM_STYLE);
                } else {
                    if (!"".equals(textbox.getStyleName())) {
                        textbox.removeStyleName(ERROR_FORM_STYLE);
                    }
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            Info.warn(wfmMessages.allowedCharLimit(fieldName, charLimit));
            return false;
        }
        return true;
    }

    public static boolean validateUserInput(TextBox textBox) {
        if (!(textBox.getText().matches("[a-zA-Z0-9\\s+_()-]+"))) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.addKeyDownHandler(event -> {
                TextBox textbox = (TextBox) event.getSource();
                if (!(textBox.getText().matches("[a-zA-Z0-9\\s+_()-]+"))) {
                    textbox.addStyleName(ERROR_FORM_STYLE);
                } else {
                    if ((textBox.getText().matches("[a-zA-Z0-9\\s+_()-]+"))) {
                        textbox.removeStyleName(ERROR_FORM_STYLE);
                    }
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateUserInputWithCyrilic(TextBox textBox) {
        if (!(textBox.getText().matches("[а-яА-Яa-zA-Z\\s\\d\\W]+"))) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.addKeyDownHandler(event -> {
                TextBox textbox = (TextBox) event.getSource();
                if (!(textBox.getText().matches("[а-яА-Яa-zA-Z\\s\\d\\W]+"))) {
                    textbox.addStyleName(ERROR_FORM_STYLE);
                } else {
                    if ((textBox.getText().matches("[а-яА-Яa-zA-Z\\s\\d\\W]+"))) {
                        textbox.removeStyleName(ERROR_FORM_STYLE);
                    }
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateTextBoxRequired(TextBox2 textBox, final WfmForm.Field field) {
        if (textBox.getText() == null || "".equals(textBox.getText())) {
            field.setErrorMessage(wfmStrings.pleaseEnterValue(), "");
            textBox.getTextBox().addKeyDownHandler(event -> {
                if (((TextBox) event.getSource()).getText().length() < 1) {
                    field.setErrorMessage(wfmStrings.pleaseEnterValue(), "");
                } else {
                    field.setErrorMessage(null, "");
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateIntegerTextBoxRequired(TextBoxBase textBox) {
        if (textBox.getText() == null || "".equals(textBox.getText().trim()) || !textBox.getText().trim().matches("^\\d+(\\.\\d{1,2})?")) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.addKeyDownHandler(event -> {
                TextBox textbox = (TextBox) event.getSource();
                if (textbox.getText().length() < 1) {
                    textbox.addStyleName(ERROR_FORM_STYLE);
                } else {
                    if (!"".equals(textbox.getStyleName())) {
                        textbox.removeStyleName(ERROR_FORM_STYLE);
                    }
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateAmountTextBoxRequired(TextBoxBase textBox) {
        if (textBox.getText() == null || "0.00".equals(textBox.getText().trim()) || "".equals(textBox.getText().trim())) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.addKeyDownHandler(event -> {
                TextBox textbox = (TextBox) event.getSource();
                if ("0.00".equals(textBox.getText().trim())) {
                    textbox.addStyleName(ERROR_FORM_STYLE);
                } else if (textbox.getText().length() < 1) {
                    textbox.addStyleName(ERROR_FORM_STYLE);
                } else {
                    if (!"".equals(textbox.getStyleName())) {
                        textbox.removeStyleName(ERROR_FORM_STYLE);
                    }
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateTextBoxRequired(TextBoxBase textBox) {
        return validateTextBoxRequired(textBox, false);
    }

    public static boolean validateTextBoxRequired(TextBoxBase textBox, boolean withoutTrim) {
        if (textBox.getText() == null || "".equals(withoutTrim ? textBox.getText() : textBox.getText().trim())) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.addKeyDownHandler(event -> {
                TextBox textbox = (TextBox) event.getSource();
                if (textbox.getText().length() < 1) {
                    textbox.addStyleName(ERROR_FORM_STYLE);
                } else {
                    if (!"".equals(textbox.getStyleName())) {
                        textbox.removeStyleName(ERROR_FORM_STYLE);
                    }
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateTextBoxRequired(MaterialTextBox textBox) {
        if (textBox.getText() == null || "".equals(textBox.getText().trim())) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.addKeyDownHandler(event -> {
                MaterialTextBox textbox = (MaterialTextBox) event.getSource();
                if (textbox.getText().length() < 1) {
                    textbox.addStyleName(ERROR_FORM_STYLE);
                } else {
                    if (!"".equals(textbox.getStyleName())) {
                        textbox.removeStyleName(textbox.getStyleName());
                    }
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateEditorRequired(KpiEditor editor) {
        if (Utils.isNullOrEmpty(editor.getData())) {
            editor.getRichEditor().addStyleName(ERROR_FORM_STYLE);
            editor.getRichEditor().addKeyDownHandler(event -> {
                MaterialRichEditor richTextArea = (MaterialRichEditor) event.getSource();
                if (editor.getData().length() < 1) {
                    richTextArea.setStyleName(ERROR_FORM_STYLE);
                } else {
                    if (!"".equals(richTextArea.getStyleName())) {
                        richTextArea.removeStyleName(richTextArea.getStyleName());
                    }
                }
            });
            Utils.scrollIntoView(editor.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateDate(final KpiDatePicker datePicker, final WfmForm.Field field, boolean required) {
        final Date date = datePicker.getDate();
        if (date != null) {
            datePicker.setStyleName("");
        }
        if (required && date == null) {
            field.setErrorMessage(wfmStrings.pleaseSpecifyDate(), "");
            Utils.scrollIntoView(datePicker.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateDate(final KpiDatePicker datePicker, final HTML errorLabel, boolean required) {
        Date date = datePicker.getDate();
        if (date != null) {
            removeWidgetStyle(datePicker);
        } else {
            datePicker.addStyleName(ERROR_FORM_STYLE);
        }
        datePicker.addValueChangeHandler(dateValueChangeEvent -> {
            try {
                DateUtils.parse(datePicker.getText(), DateUtils.getFormat());
                if (errorLabel != null) {
                    errorLabel.setHTML("");
                }
                removeWidgetStyle(datePicker);
            } catch (DateFormatException e) {
                if (errorLabel != null) {
                    errorLabel.setHTML("<font color='red'>" + wfmStrings.pleaseSelectDate() + "</font>");
                }
            }
        });

        if (required && date == null) {
            if (errorLabel != null) {
                errorLabel.setHTML("<font color='red'>" + wfmStrings.pleaseSelectDate() + "</font>");
            }
            Utils.scrollIntoView(datePicker.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateDate(final DatePicker datePicker) {
        if (datePicker.getDate() == null) {
            datePicker.addStyleName(ERROR_FORM_STYLE);
            datePicker.addChangeHandler(event -> {
                try {
                    DateUtils.parse(datePicker.getText(), DateUtils.getFormat());
                    removeWidgetStyle(datePicker);
                } catch (DateFormatException e) {
                }
            });
            Utils.scrollIntoView(datePicker.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateDateTime(DateTimeWidget dateTimeWidget) {
        if (dateTimeWidget.getDateField().getDate() == null) {
            dateTimeWidget.getDateField().addStyleName(ERROR_FORM_STYLE);
            dateTimeWidget.getDateField().addValueChangeHandler(event -> {
                DateUtils.getDateAndTimeFormatShort2(dateTimeWidget.getDateTime());
                removeWidgetStyle(dateTimeWidget.getDateField());
            });
            Utils.scrollIntoView(dateTimeWidget.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateStartEndDate(final DatePicker startDate, final DatePicker endDate) {
        boolean valid = true;
        final Date start = startDate.getDate();
        final Date end = endDate.getDate();
        if (start != null) {
            startDate.setStyleName("");
        } else {
            startDate.addStyleName(ERROR_FORM_STYLE);
            Utils.scrollIntoView(startDate.getElement());
            valid = false;
        }
        if (end != null) {
            endDate.setStyleName("");
        } else {
            endDate.addStyleName(ERROR_FORM_STYLE);
            Utils.scrollIntoView(endDate.getElement());
            valid = false;
        }
        if (valid && end.compareTo(start) < 0) {
            startDate.addStyleName(ERROR_FORM_STYLE);
            Utils.scrollIntoView(startDate.getElement());
            endDate.addStyleName(ERROR_FORM_STYLE);
            Utils.scrollIntoView(endDate.getElement());
            valid = false;
        }
        return valid;
    }

    public static boolean validateDate(final com.google.gwt.user.datepicker.client.DatePicker datePicker, final WfmForm.Field field, boolean required) {
        final Date date = datePicker.getValue();
        if (date != null) {
            datePicker.setStyleName("");
        }
        if (required && date == null) {
            field.setErrorMessage(wfmStrings.pleaseSpecifyDate(), "");
            Utils.scrollIntoView(datePicker.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateDate(final DatePicker datePicker, final WfmForm.Field field, boolean required) {
        Date date = datePicker.getDate();
        datePicker.addChangeHandler(event -> {
            try {
                DateUtils.parse(datePicker.getText(), DateUtils.getFormat());
                field.setErrorMessage(null, "");
            } catch (DateFormatException e) {
                field.setErrorMessage(wfmStrings.pleaseSpecifyDate(), "");
            }
        });

        if (required && date == null) {
            field.setErrorMessage(wfmStrings.pleaseSpecifyDate(), "");
            Utils.scrollIntoView(datePicker.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateDate(final DatePicker datePicker, final HTML errorLabel, boolean required) {
        Date date = datePicker.getDate();
        if (date != null) {
            removeWidgetStyle(datePicker);
        } else {
            datePicker.addStyleName(ERROR_FORM_STYLE);
        }
        datePicker.addChangeHandler(event -> {
            try {
                DateUtils.parse(datePicker.getText(), DateUtils.getFormat());
                if (errorLabel != null) {
                    errorLabel.setHTML("");
                }
                removeWidgetStyle(datePicker);
            } catch (DateFormatException e) {
                if (errorLabel != null) {
                    errorLabel.setHTML("<font color='red'>" + wfmStrings.pleaseSelectDate() + "</font>");
                }
            }
        });

        if (required && date == null) {
            if (errorLabel != null) {
                errorLabel.setHTML("<font color='red'>" + wfmStrings.pleaseSelectDate() + "</font>");
            }
            Utils.scrollIntoView(datePicker.getElement());
            return false;
        }
        return true;
    }

    private static void removeWidgetStyle(Widget widget) {
        if (!Utils.isNullOrEmpty(widget.getStyleName())) {
            widget.removeStyleName(ERROR_FORM_STYLE);
        }
    }

    public static boolean validateDateOrder(final WfmForm.Field field, final DatePicker startPicker, final DatePicker endPicker) {
        if ((startPicker.getDate() == null && endPicker.getDate() == null)) {
            return false;
        }
        if ((startPicker.getDate() != null && endPicker.getDate() != null)) {
            if (startPicker.getDate().after(endPicker.getDate())) {
                if (field != null) {
                    field.setErrorMessage(wfmStrings.startDateNotLaterDueDate()/*"End date is before start date"*/, "");
                    Utils.scrollIntoView(field.getElement());
                }
                return false;
            }
        }
        return true;
    }

    public static boolean validateListBoxRequired(DataListBox listBox, final WfmForm.Field field, String message) {

        if (getDataListBoxValue(listBox) == null) {
            if (field != null) {
                field.setErrorMessage(message, "");
            }
            listBox.addValueChangeHandler(event -> {
                DataListBox listbox = (DataListBox) event.getSource();

                if (getDataListBoxValue(listbox) != null) {
                    if (field != null) {
                        field.setErrorMessage(null, "");
                    }
                }
            });
            Utils.scrollIntoView(listBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateListBoxRequired(final LookUp lookUp, final WfmForm.Field field, String message) {
        if (lookUp.getSelectedItem() == null || lookUp.getSelectedItemID() == null) {
            if (field != null) {
                field.setErrorMessage(message, "");
            }
            lookUp.getSuggestBox().addValueChangeHandler(event -> {
                if (field != null) {
                    field.setErrorMessage(null, "");
                    lookUp.getSuggestBox().removeStyleName(ERROR_FORM_STYLE);
                }
            });
            Utils.scrollIntoView(lookUp.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateMultiSelectRequired(final MultiSelectLookUp lookUp, final WfmForm.Field field, String message) {

        if (lookUp.getSelectedItems() == null || (lookUp.getSelectedItems() != null && lookUp.getSelectedItems().size() == 0)) {
            if (field != null) {
                field.setErrorMessage(message, "");
            }
            lookUp.addStyleName(ERROR_FORM_STYLE);
            Utils.scrollIntoView(lookUp.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateListBoxRequired(ListBox listBox, String errorMessage, StringBuffer messages) {
        if (listBox.isEnabled() && (getListBoxValue(listBox) == null || getListBoxValue(listBox).equals(0))) {
            messages.append(errorMessage);
            Utils.scrollIntoView(listBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateListBoxRequired(DataListBox listBox, String errorMessage, StringBuffer messages) {
        if (listBox.isEnabled() && (getDataListBoxValue(listBox) == null || getDataListBoxValue(listBox).equals(0))) {
            messages.append(errorMessage);
            Utils.scrollIntoView(listBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateDataListBoxRequired(DataListBox listBox, String errorMessage, StringBuffer messages) {
        if (listBox.isEnabled() && (getDataListBoxValue(listBox) == null || getDataListBoxValue(listBox).equals(0))) {
            messages.append(errorMessage);
            Utils.scrollIntoView(listBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateListBoxRequired(ListBox listBox, final HTML errorField, String message) {
        if (getListBoxValue(listBox) == null) {
            if (errorField != null) {
                errorField.setHTML("<font color='red'>" + message + "</font>");
            }
            listBox.setStyleName(ERROR_FORM_STYLE);
            listBox.addChangeHandler(event -> {
                ListBox listbox = (ListBox) event.getSource();
                if (getListBoxValue(listbox) != null) {
                    if (errorField != null) {
                        errorField.setHTML("");
                    }
                    removeWidgetStyle(listbox);
                }
            });
            Utils.scrollIntoView(listBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateListBoxRequired(DataListBox listBox, final HTML errorField, String message) {
        if (getDataListBoxValue(listBox) == null) {
            if (errorField != null) {
                errorField.setHTML("<font color='red'>" + message + "</font>");
            }
            listBox.addStyleName(ERROR_FORM_STYLE);
            listBox.addValueChangeHandler(event -> {
                DataListBox listbox = (DataListBox) event.getSource();

                if (getDataListBoxValue(listbox) != null) {
                    if (errorField != null) {
                        errorField.setHTML("");
                    }
                    removeWidgetStyle(listbox);
                }
            });
            Utils.scrollIntoView(listBox.getElement());
            return false;
        }
        return true;
    }

    /*public static boolean validateListBoxRequired(ListBox listBox) {
        if (getListBoxValue(listBox) == null) {
            listBox.setStyleName(ERROR_FORM_STYLE);
            listBox.addChangeHandler(event -> {
                ListBox listbox = (ListBox) event.getSource();
                if (getListBoxValue(listbox) != null) {
                    removeWidgetStyle(listbox);
                }
            });
            Utils.scrollIntoView(listBox.getElement());
            return false;
        }
        return true;
    }*/

    public static boolean validateKpiTimePickerRequired(KpiTimePicker timePicker) {
        if (timePicker.getValue() == null || Arrays.equals(timePicker.getValue(), new int[]{0, 0})) {
            timePicker.addStyleName(ERROR_FORM_STYLE);
            timePicker.addClickHandler(event -> {
                KpiTimePicker kpiTimePicker = (KpiTimePicker) event.getSource();
                if (kpiTimePicker != null) {
                    kpiTimePicker.removeStyleName(ERROR_FORM_STYLE);
                }
            });
            Utils.scrollIntoView(timePicker.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateListBoxRequired(DataListBox listBox) {
        if (getDataListBoxValue(listBox) == null) {
            listBox.addValueChangeHandler(event -> {
                DataListBox listbox = (DataListBox) event.getSource();

                if (getDataListBoxValue(listbox) != null) {
                    removeWidgetStyle(listbox);
                }
            });
            Utils.scrollIntoView(listBox.getElement());
            return false;
        }
        return true;
    }

    private static Integer getListBoxValue(ListBox listBox) {
        String value = "";
        if (listBox.getSelectedIndex() > -1) {
            value = listBox.getValue(listBox.getSelectedIndex());
        }
        if ("".equals(value)) {
            return null;
        }
        return Integer.valueOf(value);
    }

    private static Integer getDataListBoxValue(DataListBox listBox) {
        /*String value = "";
        if (listBox.getSelectedIndex() > -1) {
//            value = listBox.getValue(listBox.getSelectedIndex()).getName();
            return listBox.getValue(listBox.getSelectedIndex()).getId();
        }
        if ("".equals(value)) {
            return null;
        }*/
        return listBox.getSelectedId();
    }

    public static boolean validateDataListBoxRequired(DataListBox listBox) {
        if (/*!listBox.isSomethingSelected()*/getDataListBoxValue(listBox) == null) {
            listBox.addStyleName(ERROR_FORM_STYLE);
            listBox.addValueChangeHandler(event -> {
                if (event.getValue() != null) {
                    listBox.removeStyleName(ERROR_FORM_STYLE);
                }
            });
            Utils.scrollIntoView(listBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateFileUploadRequired(FileUpload fileUpload, final HTML errorField, String message) {
        if (fileUpload.getFilename() == null || "".equals(fileUpload.getFilename())) {
            if (errorField != null) {
                errorField.setHTML("<font color='red'>" + message + "</font>");
            }
            fileUpload.setStyleName(ERROR_FORM_STYLE);
            fileUpload.addChangeHandler(event -> {
                FileUpload fileUpload1 = (FileUpload) event.getSource();
                if (fileUpload1.getFilename() != null && !"".equals(fileUpload1.getFilename())) {
                    if (errorField != null) {
                        errorField.setHTML("");
                    }
                    removeWidgetStyle(fileUpload1);
                }
            });
            Utils.scrollIntoView(fileUpload.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateFileUploadRequired(final LookUp lookUp, final HTML errorField, String message) {
        if (lookUp.getSelectedItem() == null || lookUp.getSelectedItemID() == null) {
            if (errorField != null) {
                errorField.setHTML("<font color='red'>" + message + "</font>");
            }
            lookUp.getSuggestBox().setStyleName(ERROR_FORM_STYLE);
            lookUp.getSuggestBox().addValueChangeHandler(event -> {
                if (errorField != null) {
                    errorField.setHTML("");
                }
                lookUp.getSuggestBox().removeStyleName(ERROR_FORM_STYLE);
            });
            Utils.scrollIntoView(lookUp.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateWfmDropdownRequired(final WfmDropdown dropdown, final WfmForm.Field field, String message) {
        if (!dropdown.isValid()) {
            field.setErrorMessage(message, "");
            dropdown.addEventHandler(new DropdownListener() {
                public void itemSelected() {
                    if (dropdown.isValid()) {
                        dropdown.removeStyle();
                        field.setErrorMessage(null, "");
                    }
                }

                public void saveNewItem() {
                }
            });
            Utils.scrollIntoView(dropdown.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateLookUpRequired(final LookUp lookUp, final WfmForm.Field field, String message) {
        if (lookUp.existsOracle(lookUp.getText()) || lookUp.getSelectedItemID() != null) {
            return true;
        }
        field.setErrorMessage(message != null && !"".equals(message) ? message : wfmStrings.fieldIsRequired(), null);
        Utils.scrollIntoView(lookUp.getElement());
        return false;
    }

    public static boolean validateLookUpRequired(final LookUp lookUp) {
        if (lookUp.existsOracle(lookUp.getText()) || lookUp.getSelectedItemID() != null) {
            return true;
        }
        lookUp.getSuggestBox().addStyleName(ERROR_FORM_STYLE);
        lookUp.getSuggestBox().addSelectionHandler(event -> removeWidgetStyle(lookUp.getSuggestBox()));

        Utils.scrollIntoView(lookUp.getElement());
        return false;
    }

    public static boolean validateEmployeeMultiSelectLookUpRequired(final MultiSelectEmployeeLookUp lookUp) {
        if (lookUp.getSelectedItemsIdsAsString() != null) {
            return true;
        }
        lookUp.getSuggestBox().addStyleName(ERROR_FORM_STYLE);
        lookUp.getSuggestBox().addSelectionHandler(event -> removeWidgetStyle(lookUp.getSuggestBox()));

        Utils.scrollIntoView(lookUp.getElement());
        return false;
    }

    public static boolean validateVacancyMultiSelectLookUpRequired(final MultiSelectVacancyLookUp lookUp) {
        if (lookUp.getSelectedItemsIdsAsString() != null) {
            return true;
        }
        lookUp.getSuggestBox().addStyleName(ERROR_FORM_STYLE);
        lookUp.getSuggestBox().addSelectionHandler(event -> removeWidgetStyle(lookUp.getSuggestBox()));

        Utils.scrollIntoView(lookUp.getElement());
        return false;
    }

    /*public static boolean validateLookUpStyled(final LookUp lookUp) {
        if (lookUp.existsOracle(lookUp.getText()) || lookUp.getSelectedItemID() != null) {
            return true;
        }
        lookUp.getSuggestBox().addStyleName(ERROR_FORM_STYLE);
        lookUp.getSuggestBox().addSelectionHandler(event -> removeWidgetStyle(lookUp.getSuggestBox()));

        Utils.scrollIntoView(lookUp.getElement());
        return false;
    }*/

    public static boolean validateEmailRequired(final TextBoxBase textBox) {
        return validateEmailRequired(textBox, null);
    }

    public static boolean validateEmailRequired(final TextBoxBase textBox, final WfmForm.Field field) {
        String email = textBox.getText();
        if (!Utils.isNullOrEmpty(email)) {
            email = email.trim();
            textBox.setText(email);
        } else {
            if (field != null) {
                field.setErrorMessage(wfmStrings.provideCorrectEmailAddress2(), "");
            } else {
                textBox.addStyleName(ERROR_FORM_STYLE);
                textBox.setTitle(wfmStrings.provideCorrectEmailAddress2());
            }
            textBox.addKeyPressHandler(event -> {
                if (field != null) {
                    field.setErrorMessage(null, "");
                } else {
                    textBox.removeStyleName(ERROR_FORM_STYLE);
                    textBox.setTitle("");
                }
            });
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        email = email.trim();
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == ' ') {
                if (field != null) {
                    field.setErrorMessage(wfmStrings.pleaseEnterCorrectEmailAddress(), "");
                } else {
                    textBox.addStyleName(ERROR_FORM_STYLE);
                    textBox.setTitle(wfmStrings.pleaseEnterCorrectEmailAddress());
                }
                Utils.scrollIntoView(textBox.getElement());
                return false;
            }
        }
        if (!Utils.validateEmail(email, false)) {
            if (field != null) {
                field.setErrorMessage(wfmStrings.pleaseEnterCorrectEmailAddress(), "");
            } else {
                textBox.addStyleName(ERROR_FORM_STYLE);
                textBox.setTitle(wfmStrings.pleaseEnterCorrectEmailAddress());
            }
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }

        textBox.setText(email);
        return true;
    }

    public static boolean validateUrl(final TextBoxBase textBox, final WfmForm.Field field) {

        if (textBox.getText() == null || "".equals(textBox.getText())) {
            if (field != null) {
                field.setErrorMessage(wfmStrings.pleaseEnterValue(), ERROR_FORM_STYLE);
                textBox.addKeyDownHandler(event -> {
                    if (((TextBox) event.getSource()).getText().length() < 1) {
                        field.setErrorMessage(wfmStrings.pleaseEnterValue(), ERROR_FORM_STYLE);
                    } else {
                        field.setErrorMessage(null, "");
                    }
                });
            } else {
                textBox.addStyleName(ERROR_FORM_STYLE);
            }
            Utils.scrollIntoView(textBox.getElement());
            return false;
        } else {
            boolean isHavePoint = textBox.getText().contains(".");
            boolean isValidate = false;
            if (isHavePoint) {
                Integer indexOfpoint = textBox.getText().indexOf(".");
                if (textBox.getText().length() > indexOfpoint) {
                    String afterPoint = textBox.getText().substring(indexOfpoint + 1);
                    isValidate = afterPoint.length() > 1;
                }
            }

            if (!(textBox.getText().length() > 3 && isHavePoint && isValidate)) {
                if (field != null) {
                    field.setErrorMessage(wfmStrings.pleaseEnterCorrectEmailAddress(), "");
                } else {
                    textBox.addStyleName(ERROR_FORM_STYLE);
                    textBox.setTitle(wfmStrings.pleaseEnterCorrectEmailAddress());
                }
                Utils.scrollIntoView(textBox.getElement());
                return false;
            }
        }
        return true;

    }

    public static boolean validateUserCredentialsRequired(TextBoxBase textBox, String
            blankErroMessage, StringBuffer message) {
        String name = textBox.getText();
        if (name == null || name.equals("")) {
            message.append(blankErroMessage);
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }

        name = name.trim();

        if (name.length() == 0) {
            message.append(blankErroMessage);
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }

        String a = name.substring(0, 1);
        a = a.toUpperCase();
        char[] names = name.toCharArray();
        names[0] = a.charAt(0);
        textBox.setText(new String(names));
        return true;
    }

    public static boolean validateEmailRequired(TextBoxBase textBox, final HTML errorLabel, boolean isMultiEmail) {
        String email = textBox.getText();
        if (Utils.isNullOrEmpty(email)) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.addKeyPressHandler(event -> {
                TextBox textbox = (TextBox) event.getSource();
                if (errorLabel != null) {
                    errorLabel.setHTML("");
                }
                if (!"".equals(textbox.getStyleName())) {
                    textbox.removeStyleName(textbox.getStyleName());
                }
            });
            if (errorLabel != null) {
                errorLabel.setHTML("<font color='red'>" + wfmStrings.pleaseEnterEmail() + "</font>");
            }
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }

        boolean matches = validEmailFormat(email, isMultiEmail);
        if (!matches) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            errorLabel.setHTML("<font color='red'>" + wfmStrings.pleaseEnterCorrectEmailAddress() + "</font>");
            Utils.scrollIntoView(textBox.getElement());
        }
        textBox.setText(email);
        return matches;
    }

    public static boolean validateUserCredentialsRequired(TextBoxBase textBox, final WfmForm.Field field,
                                                          final String blankErroMessage, String blankSpaceErrorMessage) {
        String name = textBox.getText();
        if (name == null || name.equals("")) {
            textBox.addKeyPressHandler(event -> field.setErrorMessage(blankErroMessage, null));

            field.setErrorMessage(blankErroMessage, null);
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }

        name = name.trim();

        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ') {
                field.setErrorMessage(blankSpaceErrorMessage, null);
                Utils.scrollIntoView(textBox.getElement());
                return false;
            }
        }

        String a = name.substring(0, 1);
        a = a.toUpperCase();
        textBox.setText(name.replace(name.charAt(0), a.charAt(0)));
        return true;
    }

    public static boolean validateEmailRequired(TextBoxBase textBox, String blankError, String
            incorrectError, StringBuffer errorMessage) {
        String email = textBox.getText();

        if (email == null || email.equals("")) {
            errorMessage.append(blankError);
            Utils.scrollIntoView(textBox.getElement());
            return false;
        }
        boolean matches = validEmailFormat(email, false);
        if (!matches) {
            errorMessage.append(incorrectError);
            Utils.scrollIntoView(textBox.getElement());
        }
        textBox.setText(email);
        return matches;
    }

    public static boolean validEmailFormat(String email, boolean isMultyEmail) {
        if (Utils.isNullOrEmpty(email)) {
            return false;
        }
        email = email.trim();
        return Utils.validateEmail(email, isMultyEmail);
    }

    public static boolean validateSuggestBoxExist(final MultiWordSuggestOracle oracle,
                                                  final SuggestBox suggestBox, final Field field) {
        if ((oracle.exists(suggestBox.getText()) && suggestBox.getText() != null && !"".equals(suggestBox.getText()))) {
            return true;
        }
        if (field != null) {
            field.setErrorMessage(wfmStrings.noExistingData(), "");
        }
        Utils.scrollIntoView(suggestBox.getElement());
        return false;
    }

    public static boolean validateSuggestBoxExist(final LookUp suggestBox, final Field field) {
        if ((suggestBox.existsOracle(suggestBox.getText()) && suggestBox.getText() != null && !"".equals(suggestBox.getText()))) {
            return true;
        }
        if (field != null) {
            field.setErrorMessage(wfmStrings.noExistingData(), "");
        }
        Utils.scrollIntoView(suggestBox.getElement());
        return false;
    }

    public static boolean validateTextAreaRequired(TextArea area, final WfmForm.Field field) {
        if (area.getText() == null || "".equals(area.getText())) {
            field.setErrorMessage(wfmStrings.pleaseEnterValue(), "");
            area.addKeyPressHandler(event -> field.setErrorMessage(null, ""));
            Utils.scrollIntoView(area.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateHTMLTextAreaRequired(KpiEditor kpiEditor, final WfmForm.Field field) {
        if (kpiEditor.getData() == null || "".equals(kpiEditor.getData())) {
            field.setErrorMessage(wfmStrings.pleaseEnterValue(), "");
            kpiEditor.addClickHandler(event -> field.setErrorMessage(null, ""));
            Utils.scrollIntoView(kpiEditor.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateTextAreaRequired(TextArea2 area, final WfmForm.Field field) {
        if (area.getText() == null || "".equals(area.getText())) {
            field.setErrorMessage(wfmStrings.pleaseEnterValue(), "");
            area.addKeyPressHandler(event -> field.setErrorMessage(null, ""));
            Utils.scrollIntoView(area.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateTextAreaRequired(final TextArea area) {
        if (Utils.isNullOrEmpty(area.getText())) {
            area.setStyleName(ERROR_FORM_STYLE);
            area.addKeyPressHandler(event -> {
                if (!"".equals(area.getStyleName())) {
                    area.removeStyleName(area.getStyleName());
                }
            });
            Utils.scrollIntoView(area.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateHTMLTextAreaRequired(final KpiEditor kpiEditor) {
        if (Utils.isNullOrEmpty(kpiEditor.getData())) {
            kpiEditor.setStyleName(ERROR_FORM_STYLE);
            kpiEditor.addClickHandler(event -> {
                if (!"".equals(kpiEditor.getStyleName())) {
                    kpiEditor.removeStyleName(kpiEditor.getStyleName());
                }
            });
            Utils.scrollIntoView(kpiEditor.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateTextAreaRequired(final TextArea2 area) {
        if (area.getText() == null || "".equals(area.getText()) || "".equals(area.getText().trim())) {
            area.addStyleName(ERROR_FORM_STYLE);
            area.addKeyPressHandler(event -> {
                if (!"".equals(area.getStyleName())) {
                    area.removeStyleName(ERROR_FORM_STYLE);
                }
            });
            Utils.scrollIntoView(area.getElement());
            return false;
        }

        return true;
    }

    public static boolean validateTextOutOfArea(RichTextArea area, final Field field) {
        if (area.getHTML().length() > 1500) {
            field.setErrorMessage(wfmStrings.descriptionNotLonger(), "");
            area.addKeyPressHandler(event -> {
                RichTextArea area1 = (RichTextArea) event.getSource();
                if (area1.getHTML().length() < 1500) {
                    field.setErrorMessage(null, "");
                }
            });
            Utils.scrollIntoView(area.getElement());
            return false;
        }

        return true;
    }

    public static boolean validateWfmDropdown(WfmDropdown dropdown, Field field, boolean required) {
        if (required) {
            if (!dropdown.isValid()) {
                field.setErrorMessage(wfmStrings.pleaseSelectValue(), "");
                Utils.scrollIntoView(dropdown.getElement());
                return false;
            }
        }

        return true;
    }

    public static boolean validateTextIP(String ip, Field field) {
        if (!ip.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")) {
            field.setErrorMessage(wfmStrings.provideValidIP(), "");
            return false;
        }
        return true;
    }

    public static boolean validateTextIP(String ip, TextBoxBase widget) {
        if (!ip.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")) {
            widget.addStyleName(ERROR_FORM_STYLE);
            widget.addKeyDownHandler(event -> {
                TextBox textbox = (TextBox) event.getSource();
                if (textbox.getText().length() < 1) {
                    textbox.setStyleName(ERROR_FORM_STYLE);
                } else {
                    if (!"".equals(textbox.getStyleName())) {
                        textbox.removeStyleName(textbox.getStyleName());
                    }
                }
            });
            Utils.scrollIntoView(widget.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateTextIP(String ip) {
        return ip.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    }

    public static boolean validateTextIPRange(String lowerIPRange, String higherIPRange) {
        if (lowerIPRange.equals("") || higherIPRange.equals("")) {
            return false;
        } else {
            if (!validateTextIP(lowerIPRange)) {
                return false;
            }
            if (!validateTextIP(higherIPRange)) {
                return false;
            }
            String[] ipFrom = lowerIPRange.split("\\.");
            String[] ipTo = higherIPRange.split("\\.");
            for (int i = 0; i < 4; i++) {
                if (Integer.valueOf(ipFrom[i]) > Integer.valueOf(ipTo[i]) || (i == 3 && Integer.valueOf(ipFrom[i]) >= Integer.valueOf(ipTo[i]))) {
                    return false;
                } else if (Integer.valueOf(ipTo[i]) > Integer.valueOf(ipFrom[i])) {
                    return true;
                }
            }
            return true;
        }
    }

    public static boolean validateWfmDropdown(final WfmDropdown dropdown) {
        if (!dropdown.isValid()) {
            dropdown.addStyleName(ERROR_FORM_STYLE);
            dropdown.addEventHandler(new DropdownListener() {
                public void itemSelected() {
                    if (dropdown.getSelectedId() != null) {
                        dropdown.removeStyle();
                    }
                }

                public void saveNewItem() {

                }
            });
            Utils.scrollIntoView(dropdown.getElement());
            return false;
        }
        return true;
    }

    public static void addNumericKeyboardListener(TextBox textBox) {
        if (textBox == null) {
            return;
        }
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();

            if (Utils.isArabicLanguage()) {
                return;
            }

            if (key == (char) 0) {
                return;
            }

            if ((!Character.isDigit(key)) && key != '-' && (key != (char) KeyCodes.KEY_TAB)
                    && (key != (char) KeyCodes.KEY_BACKSPACE) && (key != (char) KeyCodes.KEY_ENTER)) {
                ((TextBox) event.getSource()).cancelKey();
            }
        });
    }

    public static void limitMaxBox(TextBox input, TextBox maxBox) {
        input.addKeyUpHandler(event -> {
            TextBox box = (TextBox) event.getSource();
            String txt = box.getText().trim();

            if (txt.isEmpty()) {
                txt = "0";
            }

            double value = Double.parseDouble(txt);
            double max = Double.parseDouble(maxBox.getText().isEmpty() ? "0" : maxBox.getText());

            if (value > max) {
                box.setText(maxBox.getText());
            }
        });
    }


    public static void addPositiveNumericKeyboardListener(TextBox textBox) {
        if (textBox == null) {
            return;
        }
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();

            if (Utils.isArabicLanguage() || key == (char) 0) {
                return;
            }

            if ((!Character.isDigit(key)) && (key != (char) KeyCodes.KEY_TAB)
                    && (key != (char) KeyCodes.KEY_BACKSPACE) && (key != (char) KeyCodes.KEY_ENTER)) {
                ((TextBox) event.getSource()).cancelKey();
            }
        });
    }

    public static void addPositiveNonZeroNumericKeyboardListener(TextBox textBox) {
        if (textBox == null) {
            return;
        }
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();

            if (Utils.isArabicLanguage() || key == (char) 0 || key == '0' && textBox.getValue().isEmpty()) {
                ((TextBox) event.getSource()).cancelKey();
            }

            if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_TAB
                    && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_ENTER) {
                ((TextBox) event.getSource()).cancelKey();
            }
        });
    }

    public static void addPercentageNumericKeyboardListener(TextBox textBox, int scale, Double maxLength) {
        if (textBox == null) {
            return;
        }
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();
            if (Utils.isArabicLanguage()) {
                return;
            }

            if (key == (char) 0) {
                return;
            }
            if (key == '.' && scale <= 0) {
                ((TextBox) event.getSource()).cancelKey();
                return;
            }
            if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                ((TextBox) event.getSource()).cancelKey();
            }
            if (textBox.getText() != null && textBox.getText().indexOf('.') != -1 && key == '.') {
                ((TextBox) event.getSource()).cancelKey();
            }
            if (textBox.getText() != null && key == '\'') {
                ((TextBox) event.getSource()).cancelKey();
            }

            String validateString = textBox.getText().substring(textBox.getText().lastIndexOf('.') + 1);
            if (textBox.getText().contains(".") && (key == '.' || ((key != (char) KeyCodes.KEY_BACKSPACE)
                    && (textBox.getCursorPos() > textBox.getText().lastIndexOf('.') && validateString.length() >= scale)))) {
                ((TextBox) event.getSource()).cancelKey();
            }
            /*if (Character.isDigit(key)) {
                boolean isTrue = Double.valueOf(textBox.getValue() + key).compareTo(maxLength) <= 0;
                if (!isTrue) {
                    ((TextBox) event.getSource()).cancelKey();
                }
            }*/
        });
    }

    public static void addNumericKeyboardListener(TextArea textArea) {
        textArea.addKeyPressHandler(event -> {
            char key = event.getCharCode();

            if (Utils.isArabicLanguage()) {
                return;
            }

            if (key == (char) 0) {
                return;
            }

            if ((!Character.isDigit(key)) && (key != (char) KeyCodes.KEY_TAB)
                    && (key != (char) KeyCodes.KEY_BACKSPACE) && (key != (char) KeyCodes.KEY_ENTER)) {
                ((TextArea) event.getSource()).cancelKey();
            }
        });
    }

    public static void addPhoneNumberKeyboardListener(TextBox textBox) {
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();

            if (Utils.isArabicLanguage()) {
                return;
            }

            if (key == (char) 0) {
                return;
            }

            if (!(key >= '0' && key <= '9')) {
                ((TextBox) event.getSource()).cancelKey();
            }
        });
    }

    public static void addNumericKeyboardListener(final TextBox textBox, final int scale) {
        addNumericKeyboardListener(textBox, scale, false, false);
    }

    private static native void preventPasteEvent(Element element) /*-{
        $wnd.jQuery(element).on('paste', function (event) {
            event.preventDefault();
            return false;
        });
    }-*/
    ;

    public static void addNumericKeyboardListener(TextBox textBox, final int scale,
                                                  final boolean enableNegativeAmount) {
        addNumericKeyboardListener(textBox, scale, enableNegativeAmount, false);
    }

    public static void addNumericKeyboardListener(TextBox textBox, final int scale,
                                                  final boolean enableNegativeAmount, boolean preventPasteEvent) {
        if (preventPasteEvent) {
            preventPasteEvent(textBox.getElement());
        }
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();
            if (Utils.isArabicLanguage()) {
                return;
            }

            if (!enableNegativeAmount && key == '-') {
                ((TextBox) event.getSource()).cancelKey();
                return;
            }

            if (key == (char) 0) {
                return;
            }
            if (key == '.' && scale <= 0) {
                ((TextBox) event.getSource()).cancelKey();
                return;
            }
            String validateString = textBox.getText().substring(textBox.getText().lastIndexOf('.') + 1);
            if (textBox.getText().contains(".") && (key == '.' || ((key != (char) KeyCodes.KEY_BACKSPACE)
                    && (textBox.getCursorPos() > textBox.getText().lastIndexOf('.') && validateString.length() >= scale)))) {
                ((TextBox) event.getSource()).cancelKey();
                return;
            }

            if ((!Character.isDigit(key)) && (key != (char) KeyCodes.KEY_TAB)
                    && (key != (char) KeyCodes.KEY_BACKSPACE) && (key != (char) KeyCodes.KEY_ENTER)
                    && (key != '.') && (key != ',') && (key != '-')) {
                ((TextBox) event.getSource()).cancelKey();
            }
        });
    }

    /**
     * Checks and replaces all chars to space besides numbers and '.'.
     *
     * @param textBox
     */
    public static void numberValidation(TextBox textBox) {
        String text = textBox.getText();
        textBox.setText(text.replaceAll("[^0-9.]", ""));
        int oneMorePoint = text.indexOf('.', text.indexOf('.') + 1);
        if (oneMorePoint != -1) {
            textBox.setText(text.substring(0, oneMorePoint));
        }
    }

    public static void numberValidationWithoutDot(TextBox textBox) {
        String text = textBox.getText();
        int oneMorePoint = text.indexOf('.');
        if (oneMorePoint != -1) {
            textBox.setText(text.substring(0, oneMorePoint));
        }
        text = textBox.getText();
        textBox.setText(text.replaceAll("[^0-9]", ""));
    }

    public static void addAutoResizeListenerToTextArea(TextArea textArea) {
        textArea.addKeyPressHandler(keyPressEvent -> {
            if (keyPressEvent.getCharCode() == (char) KeyCodes.KEY_ENTER) {
                resizeElementHeight(textArea.getElement());
            }
        });


        textArea.addFocusHandler(focusEvent -> {
            resizeElementHeight(textArea.getElement());
        });
    }


    public static void addAutoResizeListenerToVacancyAddForm(TextArea textArea) {
        textArea.addKeyPressHandler(keyPressEvent -> {
            if (keyPressEvent.getCharCode() == (char) KeyCodes.KEY_ENTER) {
                resizeElementHeightForAddVacancyFrom(textArea.getElement());
            }
        });


        textArea.addChangeHandler(focusEvent -> {
            resizeElementHeightForAddVacancyFrom(textArea.getElement());
        });
    }

    public static boolean validateNumber(final TextBox textBox, final Field field) {
        boolean flag = true;
        if (!textBox.getText().equals("")) {
            String text = textBox.getText();

            for (int i = 0; i < text.length(); i++) {
                //If we find a non-digit character we return false.
                if (!Character.isDigit(text.charAt(i))) {
                    textBox.setStyleName(ERROR_FORM_STYLE);
                    if (field != null) {
                        field.setErrorMessage(wfmStrings.pleaseEnterNumericValues(), "");
                    }
                    Utils.scrollIntoView(textBox.getElement());
                    flag = false;
                }
            }

            textBox.addKeyDownHandler(event -> {
                textBox.removeStyleName(ERROR_FORM_STYLE);
                if (field != null) {
                    field.setErrorMessage(null, "");
                }
            });
        }

        return flag;
    }

    public static void checkToFocusTextBox(TextBox textBox, final String text) {
        textBox.addFocusListener(new FocusListener() {
            public void onFocus(Widget sender) {
                TextBox textbox = (TextBox) sender;
                if (textbox.getText().equals(text)) {
                    textbox.setText("");
                }
            }

            public void onLostFocus(Widget sender) {
                TextBox textbox = (TextBox) sender;
                if (textbox.getText().equals("")) {
                    textbox.setText(text);
                }
            }
        });
    }

    public static boolean validateRadioButtonRequired(RadioButton radioButton) {
        if (!radioButton.getValue()) {
            radioButton.addStyleName(ERROR_FORM_STYLE);
            radioButton.addClickHandler(event -> radioButton.removeStyleName(ERROR_FORM_STYLE));
            Utils.scrollIntoView(radioButton.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateRadioButtonRequired(RadioButton radioButton, final WfmForm.Field field) {
        if (!radioButton.getValue()) {
            field.setErrorMessage(wfmStrings.pleaseSelect(), "");
            radioButton.addKeyDownHandler(event -> {
                if (!((RadioButton) event.getSource()).getValue()) {
                    field.setErrorMessage(wfmStrings.pleaseSelect(), "");
                } else {
                    field.setErrorMessage(null, "");
                }
            });
            Utils.scrollIntoView(radioButton.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateCheckBoxRequired(KpiCheckBox checkBox, final WfmForm.Field field) {
        if (!checkBox.getValue()) {
            field.setErrorMessage(wfmStrings.pleaseSelect(), "");
            checkBox.addKeyDownHandler(event -> {
                if (!((RadioButton) event.getSource()).getValue()) {
                    field.setErrorMessage(wfmStrings.pleaseSelect(), "");
                } else {
                    field.setErrorMessage(null, "");
                }
            });
            Utils.scrollIntoView(checkBox.getElement());
            return false;
        }
        return true;
    }

    public static boolean checkForEquality(TextBox password, TextBox confirmPassword) {
        if (password != null && confirmPassword != null) {
            if (password.getText() != null && !"".equals(password.getText())) {
                if (confirmPassword.getText() != null && !"".equals(confirmPassword.getText())) {
                    return password.getText().equals(confirmPassword.getText());
                } else {
                    return false;
                }
            }
        }
        Utils.scrollIntoView(password.getElement());
        return false;
    }

    public static boolean validateDateOrder(DatePicker date1, DatePicker date2) {
        if (date1.getDate().after(date2.getDate())) {
            date1.addStyleName(ERROR_FORM_STYLE);
            date1.addChangeHandler(event -> {
                try {
                    DateUtils.parse(date1.getText(), DateUtils.getFormat());
                    removeWidgetStyle(date1);
                } catch (DateFormatException e) {
                }
            });
            Utils.scrollIntoView(date1.getElement());
            Info.show(wfmStrings.enterCorrectDate(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public static boolean validateDateOrder(Date date1, Date date2) {
        return date2 != null && date1 != null && date1.before(date2);
    }

    public static boolean validateDateEqualOrAfter(Date date1, Date date2, Boolean isAllDay) {
        return date2 != null && date1 != null && (date1.before(date2) || (date1.equals(date2) && isAllDay));
    }

    public static boolean validateDateOrder(Date date1, Date date2, String message, Boolean isAllDay) {
        if (date1.getTime() > date2.getTime() || (date1.getTime() == date2.getTime() && !isAllDay)) {
            if (message != null) {
                Info.show(message, Info.Type.WARNING);
            } else {
                Info.show(wfmStrings.enterCorrectDate(), Info.Type.WARNING);
            }
            return false;
        }
        return true;
    }

    public static boolean validateSelectPanel(SelectPanel panel) {
        if (panel.getSelectedItems().length == 0) {
            panel.addTreePanelStyle(ERROR_FORM_STYLE);
            panel.expandTreeView();
            Utils.scrollIntoView(panel.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateLocalizationsDynamicValues(String defaultText, TextBoxBase textBox,
                                                             final WfmForm.Field field) {

        boolean isEnd = false;
        boolean value = true;
        int i = 0;
        if (!"".equals(textBox.getText().trim())) {
            while (!isEnd) {
                if (defaultText.contains("{" + i + "}")) {
                    if ("".equals(textBox.getText().trim()) || !textBox.getText().contains("{" + i + "}")) {
                        isEnd = true;
                        value = false;
                        field.setErrorMessage("You do not enter valid value", "");
                    }
                } else {
                    isEnd = true;
                    field.setErrorMessage(null, "");
                }
                i++;
            }
        }
        return value;
    }

    public static boolean validateMaterialEditorRequired(KpiEditor editor) {
        if (editor.getData() == null || "".equals(editor.getData().trim())) {
            editor.setTitle(wfmStrings.pleaseEnterValue());
            Utils.scrollIntoView(editor.getElement());
            return false;
        }
        return true;
    }

    public static boolean validateWeight(TextBox personalWeight, TextBox personalAvailableWeight) {
        if ((Double.valueOf(personalWeight.getText()) < 0) || (Double.valueOf(personalWeight.getText()) > Double.valueOf(personalAvailableWeight.getText()))) {
            personalWeight.addStyleName(ERROR_FORM_STYLE);
            return false;
        }

        return true;
    }

    private static void resizeElementHeight(com.google.gwt.dom.client.Element element) {
        element.setAttribute("style", "height:100px; padding:0;");
        element.setAttribute("style", "height:" + (element.getScrollHeight() + 4) + "px;");
    }

    private static void resizeElementHeightForAddVacancyFrom(com.google.gwt.dom.client.Element element) {
        element.setAttribute("style", "height:400px; padding:0;");
        element.setAttribute("style", "height:" + (element.getScrollHeight() + 4) + "px;");
    }

    public static boolean validateTextAreaRequired(TextBox ruleName) {
        if (ruleName.getText() == null || "".equals(ruleName.getText()) || "".equals(ruleName.getText().trim())) {
            ruleName.addStyleName(ERROR_FORM_STYLE);
            ruleName.addKeyPressHandler(event -> {
                if (!"".equals(ruleName.getStyleName())) {
                    ruleName.removeStyleName(ERROR_FORM_STYLE);
                }
            });
            Utils.scrollIntoView(ruleName.getElement());
            return false;
        }

        return true;
    }

    public static boolean itemTableNumericCFMinValueValidate(EditableTable itemTable, Collection<CompanyCustomFieldItem> customFieldItems) {
        boolean errorFound = true;
        for (int i = 0; i < itemTable.getRowCount(); i++) {
            for (CompanyCustomFieldItem item : customFieldItems) {
                errorFound &= (validateNumberField(i, item, itemTable) == 0);
            }
        }
        return errorFound;
    }

    private static int validateNumberField(int rowID, CompanyCustomFieldItem fieldItem, EditableTable itemTable) {
        int err = 0;
        if (!fieldItem.getColumnCode().contains("double_value") || fieldItem == null) {
            return err;
        }

        String selectedValue = "";
        if (fieldItem.getUiType().equals(Constants.UI_TYPE_TEXTBOX)) {
            TextBox t = (TextBox) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
            if (t != null && t.getText() != null) {
                selectedValue = t.getText();
            }
//            case Constants.UI_TYPE_DROPDOWN: {
//                CustomDropDownField dd = (CustomDropDownField) itemTable.getColumnById(rowID, fieldItem.getColumnCode());
//                if (dd != null && dd.getSelectedItemText() != null) {
//                    selectedValue = dd.getSelectedItemText();
//                }
//                break;
//            }
        } else {
            selectedValue = "";
        }

        if (!selectedValue.isEmpty()) {
            try {
                double d = Utils.universalParse(NumberFormat.getFormat(",##0.#"), selectedValue);
                if (fieldItem.getNumberMinValue() != null && d < fieldItem.getNumberMinValue()) {
                    itemTable.setColumnValid(fieldItem.getColumnCode());
                    itemTable.notValid(rowID, fieldItem.getColumnCode());
                    err++;
                }
            } catch (Exception ex) {
                ex.getStackTrace();
            }
        }
        return err;
    }
}
