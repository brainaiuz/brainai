package com.edatasite.workforce.gwt.webforms.client.forms;

import com.edatasite.workforce.gwt.contact.client.ui.DOBWidget;
import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.TextBox2;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.MatrixTable;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.crm.client.ui.CheckboxMailingListDataGrid;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUpload;
import com.edatasite.workforce.gwt.documents.client.upload.WebFormsFileUpload;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.WebFormsService;
import com.edatasite.workforce.gwt.webforms.client.WebFormsServiceAsync;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * User: Hayot
 * Date: 08.09.2010
 * Time: 9:53:46
 */
public abstract class AbstractAddFormView extends Composite {
    protected static final WebFormsServiceAsync webFormsService = WebFormsService.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected WebForm webForm;
    protected VerticalPanel container = new VerticalPanel();
    protected HashMap<Integer, List> customFields;

    protected Command addedSuccessfully;
    protected Command errorOn;
    protected HashSet<Integer> errors = new HashSet<>();
    protected HashSet<Integer> customFieldsErrors = new HashSet<>();
    private HashSet<Widget> errorWidgets = new HashSet<>();
    protected WfmForm table;
    private VerticalPanel antibotPanel;
    protected SelectItem[] caseReasons;
    protected DataListBox types;
    protected DataListBox caseReason;

    protected AbstractAddFormView(WebForm webForm, VerticalPanel antibotPanel) {
        initWidget(container);
        this.webForm = webForm;
        setWebFormID(webForm.getObjectId());
        this.antibotPanel = antibotPanel;
        init();
    }

    protected abstract void setWebFormID(Integer formID);

    public abstract void save(String antibot);

    public abstract void setAddedSuccessfully(Command addedSuccessfully);

    public abstract void setErrorOn(Command errorOn);

    protected abstract void fillItem(WebField webField, Object value, Widget widget);

    protected abstract void addField(WebField webField);

    protected abstract void addListeners();

    protected void init() {
        table = new WfmForm(new String[]{"50%", "50%"});
        for (WebField webField : webForm.getWebFields()) {
            if (webField.isShowInForm()) {
                if (webField.getGroupTitle() != null && !"".equals(webField.getGroupTitle())) {
                    table.addTitleField(webField.getGroupTitle());
                }
                if (webField.isMandatory()) {
                    addErrors(webField, webField.getSavingField());
                }
                addField(webField);
                if (webField.isDrawLine()) {
                    table.addHorizontalLine();
                }
            } else {
                if (webField.getDefaultValue() != null) {
                    fillItem(webField, webField.getDefaultValue(), null);
                }
            }
        }
        if (webForm.getUseCatpcha()) {
            table.addField(webForm.getCaptchaLabel() != null && !"".equals(webForm.getCaptchaLabel()) ? webForm.getCaptchaLabel() : "Word Verification", antibotPanel);
        }
        table.addStyleName("webForm");
        container.setWidth("100%");
        container.add(table);
        if (WebFormConstants.CASE_FORM.equals(webForm.getWebFormType(true))) {
            addListeners();
        }
    }

    public void addCustomFieldToMap(WebField webField) {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        List<Object> fieldAndCustomField = new ArrayList<>();
        CompanyCustomFieldItem companyCustomField = new CompanyCustomFieldItem(webField.getSavingField());
        if (webField.getValues() != null && webField.getValues().length > 0) {
            companyCustomField.setPredefinedValues(Arrays.stream(webField.getValues()).map(SelectItem::getName).collect(Collectors.toList()).toArray(new String[]{}));
        }
        if (webField.getType().equals(WebFormConstants.INPUT_DATEPICKER)) {
            companyCustomField.setFieldDateNonConvertedValue(new DateNonConvertable((Date) webField.getDefaultValue()));
        } else {
            companyCustomField.setFieldStringValue((String) webField.getDefaultValue(true));
        }
        WfmForm.Field field = addFieldToForm(webField);
        if (webField.isMandatory() && webField.isShowInForm() && (webField.getDefaultValue(true) == null || "".equals(webField.getDefaultValue(true)))) {
            addErrors(webField, webField.getSavingField());
            errorWidgets.add(field.getWidgets()[0]);
        } else {
            removeError(webField, webField.getSavingField());
            errorWidgets.remove(field.getWidgets()[0]);
        }
        fieldAndCustomField.add(field);
        fieldAndCustomField.add(companyCustomField);
        fieldAndCustomField.add(field.getWidgets());
        customFields.put(webField.getSavingField(), fieldAndCustomField);
    }

    public void setCustomFieldValues(WfmForm.Field wfmField, WebField webField, final CompanyCustomFieldItem customField, Widget[] widgets) {
        Integer type = webField.getType();
        if (widgets != null && widgets.length > 0) {
            if (type.equals(WebFormConstants.INPUT_TEXTBOX)) {
                TextBox textBox = (TextBox) widgets[0];
                if (textBox != null) {
                    customField.setFieldStringValue(textBox.getText());
                }
            } else if (type.equals(WebForm.INPUT_DATEPICKER)) {
                DatePicker datePicker = (DatePicker) widgets[0];
                if (datePicker != null) {
                    customField.setFieldDateNonConvertedValue(new DateNonConvertable(datePicker.getDate()));
                }
            } else if (type.equals(WebForm.INPUT_DROPDOWN)) {
                DataListBox dataListBox = (DataListBox) widgets[0];
                if (dataListBox != null) {
                    customField.setFieldStringValue(dataListBox.getSelectedItem() != null ? dataListBox.getSelectedItem().getName() : null);
                }
            } else if (type.equals(WebForm.INPUT_CHECKBOX)) {
                KpiCheckBox[] checkBoxes = (KpiCheckBox[]) (widgets.length > 0 && widgets[0] instanceof VerticalPanel ? getWidgets(widgets, WebForm.INPUT_CHECKBOX) : widgets);
                if (checkBoxes != null && checkBoxes.length > 0) {
                    customField.setFieldStringValue((String) null);
                    for (KpiCheckBox checkBox : checkBoxes) {
                        if (checkBox.getValue() != null && checkBox.getValue() && checkBox.getText() != null && !"".equals(checkBox.getText())) {
                            customField.setFieldStringValue(checkBox.getText(), true, true);
                        }
                    }
                }
            } else if (type.equals(WebForm.INPUT_RADIO_BUTTON)) {
                RadioButton[] radioButtons = (RadioButton[]) (widgets.length > 0 && widgets[0] instanceof VerticalPanel ? getWidgets(widgets, WebForm.INPUT_RADIO_BUTTON) : widgets);
                if (radioButtons != null && radioButtons.length > 0) {
                    for (RadioButton radioButton : radioButtons) {
                        if (radioButton.getValue() != null && radioButton.getValue() && radioButton.getText() != null && !"".equals(radioButton.getText())) {
                            customField.setFieldStringValue(radioButton.getText());
                        }
                    }
                }
            } else if (type.equals(WebForm.INPUT_ATTACHMENT)) {
                WebFormsFileUpload fileUpload = (WebFormsFileUpload) widgets[0];
                fileUpload.addStyleName(DEFAULT_WIDTH);
                customField.setAttachments(fileUpload.getAttachedFiles());
            }
            if (webField.isMandatory() && webField.isShowInForm()) {
                validate(wfmField, webField, widgets[0]);
            }
        }
    }

    protected WfmForm.Field addFieldToForm(WebField webField) {
        Widget[] widgets = getFieldWidget(webField, webField.getDefaultValue(), webField.getValues());
        return table.addField(webField.getLabel(), widgets, webField.isMandatory());
    }

    protected void validate(final WfmForm.Field field, WebField webField, Widget widget, boolean... isEmails) {
        boolean isEmail = isEmails != null && isEmails.length > 0 && isEmails[0];
        Integer errorField = webField.getSavingField();
        Integer type = webField.getType();
        String errorMessage = "";
        if (field != null) {
            field.setErrorMessage(null, null);
        }
        removeError(webField, errorField);
        errorWidgets.remove(widget);
        if (field != null && widget != null) {
            switch (type) {
                case WebFormConstants.INPUT_TEXTBOX:
                    if (isEmail) {
                        if (!Validation.validateEmailRequired((TextBox) widget, field)) {
                            addErrors(webField, errorField);
                            errorWidgets.add(widget);
                        }
                    } else if (!validateTextBox(field, (TextBox) widget)) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_TEXTBOX2:
                    if (isEmail) {
                        if (!Validation.validateEmailRequired((TextBox) widget, field)) {
                            addErrors(webField, errorField);
                            errorWidgets.add(widget);
                        }
                    } else if (!validateTextBox(field, (TextBox2) widget)) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_TEXTAREA:
                    if (!validateTextArea(field, (TextArea) widget)) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_TEXTAREA2:
                    if (!validateTextArea(field, (TextArea2) widget)) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_DROPDOWN:
                    if (!validateDropDown(field, (DataListBox) widget, errorMessage)) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_DATEPICKER:
                    if (!validateDatePicker(field, (DatePicker) widget)) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_PHONENUMBER:
                    if (!validatePhoneNumber(field, (PhoneNumber) widget, "")) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_DOB:
                    if (!validateDOB(field, (DOBWidget) widget)) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_CHECKBOX:
                    if ((widget instanceof KpiCheckBox && !validateCheckBox(field, errorMessage, (KpiCheckBox) widget)) || (widget instanceof VerticalPanel && !validateCheckBox(field, errorMessage, (KpiCheckBox[]) getWidgets(new Widget[]{widget}, WebFormConstants.INPUT_CHECKBOX)))) {
                        addErrors(webField, errorField);
                    }
                    break;
                case WebFormConstants.INPUT_RADIO_BUTTON:
                    if ((widget instanceof RadioButton && !validateRadioButton(field, errorMessage, (RadioButton) widget)) || (widget instanceof VerticalPanel && !validateRadioButton(field, errorMessage, (RadioButton[]) getWidgets(new Widget[]{widget}, WebFormConstants.INPUT_RADIO_BUTTON)))) {
                        addErrors(webField, errorField);
                    }
                    break;
                case WebFormConstants.INPUT_ATTACHMENT:
                    if (!validateUploadForm((WebFormsFileUpload) widget)) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_MAILING_LIST:
                    if (widget instanceof CheckboxMailingListDataGrid && (((CheckboxMailingListDataGrid) widget).getSelectedIdsList() == null || ((CheckboxMailingListDataGrid) widget).getSelectedIdsList().size() == 0)) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
            }
        } else {
            switch (type) {
                case WebFormConstants.INPUT_TEXTBOX:
                    if ("".equals(((TextBox) widget).getText())) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_TEXTBOX2:
                    if ("".equals(((TextBox2) widget).getText())) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_TEXTAREA:
                    if ("".equals(((TextArea) widget).getText())) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_TEXTAREA2:
                    if ("".equals(((TextArea2) widget).getText())) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_DROPDOWN:
                    if (((DataListBox) widget).getSelectedItem() == null) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_DATEPICKER:
                    if (((DatePicker) widget).getDate() == null) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_PHONENUMBER:
                    if ("".equals(widget.toString())) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_CHECKBOX:
                    if (!validateCheckBox(null, errorMessage, (KpiCheckBox[]) getWidgets(new Widget[]{widget}, WebFormConstants.INPUT_CHECKBOX))) {
                        addErrors(webField, errorField);
                    }
                    break;
                case WebFormConstants.INPUT_RADIO_BUTTON:
                    if (!validateRadioButton(null, errorMessage, (RadioButton[]) getWidgets(new Widget[]{widget}, WebFormConstants.INPUT_RADIO_BUTTON))) {
                        addErrors(webField, errorField);
                    }
                    break;
                case WebFormConstants.INPUT_ATTACHMENT:
                    if (((WebFormsFileUpload) widget).getAttachedFiles() == null || ((WebFormsFileUpload) widget).getAttachedFiles().length == 0) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
                case WebFormConstants.INPUT_DOB:
                    if (!validateDOB(null, (DOBWidget) widget)) {
                        addErrors(webField, errorField);
                        errorWidgets.add(widget);
                    }
                    break;
            }
        }
    }

    public Widget[] getWidgets(Widget[] widgets, int type) {
        if (widgets instanceof RadioButton[] || widgets instanceof KpiCheckBox[]) {
            return widgets;
        } else if (widgets != null && widgets.length > 0 && widgets[0] instanceof VerticalPanel) {
            VerticalPanel panel = (VerticalPanel) widgets[0];
            Widget[] newWidgets = null;
            if (WebForm.INPUT_RADIO_BUTTON == type) {
                newWidgets = new RadioButton[panel.getWidgetCount()];
            } else if (WebForm.INPUT_CHECKBOX == type) {
                newWidgets = new KpiCheckBox[panel.getWidgetCount()];
            }
            for (int i = 0; i < panel.getWidgetCount(); i++) {
                if (panel.getWidget(i) != null && newWidgets != null && newWidgets.length > i) {
                    newWidgets[i] = panel.getWidget(i);
                }
            }
            return newWidgets;
        }
        return null;
    }

    public void addErrors(WebField webField, Integer errorField) {
        if (webField != null) {
            if (webField.isCustomField()) {
                customFieldsErrors.add(errorField);
            } else {
                errors.add(errorField);
            }
        } else {
            errors.add(errorField);
        }
    }

    public void removeError(WebField webField, Integer errorField) {
        if (webField != null) {
            if (webField.isCustomField()) {
                customFieldsErrors.remove(errorField);
            } else {
                errors.remove(errorField);
            }
        } else {
            errors.remove(errorField);
        }
    }

    public boolean haveErrors() {
        if (errorWidgets.size() > 0) {
            for (Widget widget : errorWidgets) {
                widget.addStyleName(Constants.ERROR_FORM_STYLE);
            }
        }
        if (errors.size() > 0) {
            return true;
        }
        return customFieldsErrors.size() > 0;
    }

    protected Widget[] getFieldWidget(final WebField webField, Object defaultValue, SelectItem[] values) {
        Integer type = webField.getType();
        Widget widget = null;
        Widget[] widgets = null;
        if (type.equals(WebFormConstants.INPUT_TEXTBOX)) {
            final TextBox textBox = new TextBox();
            textBox.addStyleName(DEFAULT_WIDTH);
            if (defaultValue != null && defaultValue instanceof String) {
                textBox.setValue((String) defaultValue);
                fillItem(webField, textBox.getText(), textBox);
            } else if (webField.isMandatory()) {
                errorWidgets.add(textBox);
            }
            textBox.addBlurHandler(event -> {
                if (webField != null && webField.isOnlyIntegerAllowed()) {
                    textBox.setText(removeOtherThen(textBox.getText(), "^[0-9]$", "^\\.$"));
                }
                fillItem(webField, textBox.getText(), textBox);
            });
            widget = textBox;
        } else if (type.equals(WebFormConstants.INPUT_TEXTBOX2)) {
            final TextBox2 textBox2 = new TextBox2();
            textBox2.addStyleName(DEFAULT_WIDTH);
            if (defaultValue != null && defaultValue instanceof String) {
                textBox2.setText((String) defaultValue);
                fillItem(webField, textBox2.getText(), textBox2);
            } else if (webField.isMandatory()) {
                errorWidgets.add(textBox2);
            }
            textBox2.addBlurHandler(event -> {
                if (webField != null && webField.isOnlyIntegerAllowed()) {
                    textBox2.setText(removeOtherThen(textBox2.getText(), "^[0-9]$", "^\\.$"));
                }
                fillItem(webField, textBox2.getText(), textBox2);
            });
            widget = textBox2;
        } else if (type.equals(WebFormConstants.INPUT_TEXTAREA)) {
            final TextArea textArea = new TextArea();
            textArea.addStyleName(DEFAULT_WIDTH);
            if (defaultValue != null && defaultValue instanceof String) {
                textArea.setText((String) defaultValue);
                fillItem(webField, textArea.getText(), textArea);
            } else if (webField.isMandatory()) {
                errorWidgets.add(textArea);
            }
            textArea.addBlurHandler(event -> fillItem(webField, textArea.getText(), textArea));
            widget = textArea;
        } else if (type.equals(WebFormConstants.INPUT_TEXTAREA2)) {
            final TextArea2 textArea2 = new TextArea2(1000);
            textArea2.addStyleName(DEFAULT_WIDTH);
            if (defaultValue != null && defaultValue instanceof String) {
                textArea2.setText((String) defaultValue);
                fillItem(webField, textArea2.getText(), textArea2);
            } else if (webField.isMandatory()) {
                errorWidgets.add(textArea2);
            }
            textArea2.addBlurHandler(event -> fillItem(webField, textArea2.getText(), textArea2));
            widget = textArea2;
        } else if (type.equals(WebFormConstants.INPUT_DROPDOWN)) {
            final DataListBox dataListBox = new DataListBox();
            dataListBox.addStyleName(DEFAULT_WIDTH);
            if (values != null) {
                dataListBox.setItems(values);
            }
            if (defaultValue != null) {
                if (defaultValue instanceof Integer) {
                    if (webField.isCustomField()) {
                        dataListBox.setSelectedByValue(String.valueOf(defaultValue));
                    } else {
                        dataListBox.setSelected((Integer) defaultValue);
                    }
                } else if (defaultValue instanceof SelectItem) {
                    dataListBox.setSelected((SelectItem) defaultValue);
                } else if (defaultValue instanceof String) {
                    dataListBox.setSelectedByValue((String) defaultValue);
                }
                fillItem(webField, dataListBox.getSelectedItem(), dataListBox);
            } else if (webField.isMandatory()) {
                errorWidgets.add(dataListBox);
            }
            dataListBox.addBlurHandler(event -> fillItem(webField, dataListBox.getSelectedItem(), dataListBox));
            widget = dataListBox;
            if (WebFormConstants.CASE_FORM.equals(webForm.getWebFormType(true))) {
                if (CaseField.FIELD_TYPE == webField.getSavingField()) {
                    types = dataListBox;
                } else if (CaseField.FIELD_CASE_REASON == webField.getSavingField()) {
                    caseReason = dataListBox;
                    caseReasons = values;
                }
            }
        } else if (type.equals(WebFormConstants.INPUT_DATEPICKER)) {
            final DatePicker datePicker = new DatePicker();
            datePicker.setDefaultPosition(true);
            datePicker.addStyleName(DEFAULT_WIDTH);
            if (defaultValue != null) {
                if (defaultValue instanceof Date) {
                    datePicker.setDate((Date) defaultValue);
                }
                fillItem(webField, datePicker.getDate(), datePicker);
            } else if (webField.isMandatory()) {
                errorWidgets.add(datePicker);
            }
            datePicker.addChangeHandler(event -> fillItem(webField, datePicker.getDate(), datePicker));
            widget = datePicker;
        } else if (type.equals(WebFormConstants.INPUT_PHONENUMBER)) {
            final PhoneNumber phoneNumber = defaultValue != null ? new PhoneNumber((String) defaultValue) : new PhoneNumber("");
            if (defaultValue != null) {
                fillItem(webField, phoneNumber.toString(), phoneNumber);
            } else if (webField.isMandatory()) {
                errorWidgets.add(phoneNumber);
            }
            phoneNumber.addBlurHandler(event -> fillItem(webField, phoneNumber.toString(), phoneNumber));
            widget = phoneNumber.getField();
        } else if (type.equals(WebFormConstants.INPUT_DOB)) {
            final DOBWidget dobWidget = new DOBWidget();
//            dobWidget.setWidth("50px", "107px", "70px");
            if (defaultValue != null) {
                if (defaultValue instanceof Date) {
                    Date defaultValueD = (Date) defaultValue;
                    dobWidget.setSelected(defaultValueD.getDate(), defaultValueD.getMonth(), defaultValueD.getYear());
                } else if (defaultValue instanceof DateNonConvertable) {
                    DateNonConvertable defaultValueDN = (DateNonConvertable) defaultValue;
                    if (defaultValueDN.getDate() != null) {
                        dobWidget.setSelected(defaultValueDN.getDate().getDate(), defaultValueDN.getDate().getMonth(), defaultValueDN.getDate().getYear());
                    }
                }
                fillItem(webField, dobWidget.getConvertableDOBDate(), dobWidget);
            } else if (webField.isMandatory()) {
                errorWidgets.add(dobWidget);
            }
            dobWidget.addBlurHandler(event -> fillItem(webField, dobWidget.getConvertableDOBDate(), dobWidget));
            widget = dobWidget;
        } else if (type.equals(WebFormConstants.INPUT_VACANCIES)) {
            final MatrixTable vacanciesT = new MatrixTable(1);
            if (values != null) {
                if (values.length > 0) {
                    vacanciesT.addItems(CRMUtils.getSelectItemsAsCheckBoxMap(false, values), true);
                } else {
                    vacanciesT.clear();
                }
                fillItem(webField, vacanciesT, vacanciesT);
            } else if (webField.isMandatory()) {
                errorWidgets.add(vacanciesT);
            }
            vacanciesT.addBlurHandler(event -> fillItem(webField, vacanciesT, vacanciesT));
            widget = vacanciesT;
        } else if (type.equals(WebFormConstants.INPUT_CHECKBOX)) {
            if (webField.isCustomField() && webField.getValues() != null) {
                SelectItem[] defaultValues = Utils.getAsSelectItem((String) webField.getDefaultValue(true), webField.getDefaultValue() != null && ((String) webField.getDefaultValue(true)).contains(",") ? "," : "'");
                List<KpiCheckBox> checkBoxes = new ArrayList<>();
                for (SelectItem predefinedValue : webField.getValues()) {
                    if (predefinedValue != null) {
                        final KpiCheckBox checkBox = new KpiCheckBox(predefinedValue.getName());
                        checkBox.setName(predefinedValue.getName());
                        if (defaultValue != null) {
                            if (defaultValues != null && defaultValues.length > 0) {
                                for (SelectItem defaultValue_ : defaultValues) {
                                    if (defaultValue_ != null && defaultValue_.getName() != null && predefinedValue.getName() != null && predefinedValue.getName().equals(defaultValue_.getName())) {
                                        checkBox.setValue(true);
                                    }
                                }
                            }
                            fillItem(webField, checkBox.getText(), checkBox);
                        } else if (webField.isMandatory()) {
                            errorWidgets.add(checkBox);
                        }
                        checkBox.addBlurHandler(event -> fillItem(webField, checkBox.getText(), checkBox));
                        checkBox.addClickHandler(event -> fillItem(webField, checkBox.getText(), checkBox));
                        checkBoxes.add(checkBox);
                    }
                }
                widgets = checkBoxes.toArray(new KpiCheckBox[]{});
            } else {
                final KpiCheckBox checkBox = new KpiCheckBox();
                if (defaultValue != null) {
                    checkBox.setValue((Boolean) defaultValue);
                    fillItem(webField, checkBox.getValue(), checkBox);
                } else if (webField.isMandatory()) {
                    errorWidgets.add(checkBox);
                }
                checkBox.addBlurHandler(event -> fillItem(webField, checkBox.getValue(), checkBox));
                checkBox.addClickHandler(event -> fillItem(webField, checkBox.getValue(), checkBox));
                widget = checkBox;
            }
        } else if (type.equals(WebFormConstants.INPUT_RADIO_BUTTON)) {
            if (webField.isCustomField() && webField.getValues() != null) {
                int i = 0;
                List<RadioButton> radioButtons = new ArrayList<>();
                SelectItem[] defaultValues = Utils.getAsSelectItem((String) webField.getDefaultValue(true), "'");
                String radioButtonName = "rb" + (new Date()).getTime();
                for (SelectItem predefinedValue : webField.getValues()) {
                    if (predefinedValue != null) {
                        final RadioButton radioButton = new KpiRadioButton(predefinedValue.getName());
                        radioButton.setText(predefinedValue.getName());
                        radioButton.setName(radioButtonName);
                        if (defaultValue != null) {
                            if (defaultValues != null && defaultValues.length > 0) {
                                for (SelectItem defaultValue_ : defaultValues) {
                                    if (defaultValue_ != null && defaultValue_.getName() != null && predefinedValue.getName() != null && predefinedValue.getName().equals(defaultValue_.getName())) {
                                        radioButton.setValue(true);
                                    }
                                }
                            }
                            fillItem(webField, radioButton.getText(), radioButton);
                        } else if (webField.isMandatory()) {
                            errorWidgets.add(radioButton);
                        }
                        radioButton.addBlurHandler(event -> fillItem(webField, radioButton.getText(), radioButton));
                        radioButton.addClickHandler(event -> fillItem(webField, radioButton.getText(), radioButton));
                        radioButtons.add(radioButton);
                    }
                }
                widgets = radioButtons.toArray(new RadioButton[]{});
            } else {
                final RadioButton radioButton = new KpiRadioButton(webField.getLabel());
                if (defaultValue != null) {
                    radioButton.setValue((Boolean) defaultValue);
                    fillItem(webField, radioButton.getValue(), radioButton);
                } else if (webField.isMandatory()) {
                    errorWidgets.add(radioButton);
                }
                radioButton.addBlurHandler(event -> fillItem(webField, radioButton.getValue(), radioButton));
                radioButton.addClickHandler(event -> fillItem(webField, radioButton.getValue(), radioButton));
                widget = radioButton;
            }
        } else if (type.equals(WebFormConstants.INPUT_ATTACHMENT)) {
            final WebFormsFileUpload fileUpload = new WebFormsFileUpload(true);
            fileUpload.addStyleName(DEFAULT_WIDTH);
            Command command = () -> fillItem(webField, fileUpload.getAttachedFiles(), fileUpload);
            if (webField.isMandatory()) {
                errorWidgets.add(fileUpload);
            }
            fileUpload.addUploadListener(command, GWTFileUpload.UploadFileEvents.FILE_UPLOADED, GWTFileUpload.UploadFileEvents.FILE_REMOVED);
            widget = fileUpload;
        } else if (type.equals(WebFormConstants.INPUT_MAILING_LIST)) {
            final CheckboxMailingListDataGrid mailingListTable = new CheckboxMailingListDataGrid(null, false, (String) defaultValue);
            mailingListTable.addStyleName(DEFAULT_WIDTH);
            mailingListTable.setCheckBoxCellFieldUpdater((index, object, value) -> {
                mailingListTable.updateChecked(object, value);
                fillItem(webField, mailingListTable.getSelectedIdsList(), mailingListTable);
            });
            if (defaultValue != null && !"".equals(defaultValue)) {
//                mailingListTable.setSelectedSubscriptionList((String) defaultValue);
                ArrayList<Integer> ids = new ArrayList<>();
                for (String id : ((String) defaultValue).split(",")) {
                    if (id != null && id.matches(Constants.REGEX_INTEGER)) {
                        ids.add(Integer.valueOf(id));
                    }
                }
                fillItem(webField, ids.size() > 0 ? ids : null, mailingListTable);
            } else if (webField.isMandatory()) {
                errorWidgets.add(mailingListTable);
            }
            widget = mailingListTable;
        }
        VerticalPanel vp = new VerticalPanel();
        if (widgets != null && widgets.length > 0) {
            for (Widget w : widgets) {
                vp.add(w);
            }
        }
        return new Widget[]{widgets != null && widgets.length > 0 ? vp : widget};
    }

    private String removeOtherThen(String text, String regex, String regex2OnlyOnceTimeMustBeAdded) {
        if (text != null && !"".equals(text)) {
            StringBuilder newText = new StringBuilder();
            boolean pointAdded = false;
            for (int i = 0; i < text.length(); i++) {
                String s = text.substring(i, i + 1);
                if (s.matches(regex)) {
                    newText.append(s);
                }
                if (s.matches(regex2OnlyOnceTimeMustBeAdded) && !pointAdded) {
                    newText.append(s);
                    pointAdded = true;
                }
            }
            return newText.toString();
        }
        return null;
    }

    private boolean validateUploadForm(final WebFormsFileUpload uploadField) {
        return uploadField != null && uploadField.getAttachedFiles() != null && uploadField.getAttachedFiles().length > 0;
    }

    private boolean validateRadioButton(final WfmForm.Field field, String errorMessage, final CheckBox... radioButtons) {
        boolean foundAndSet = false;
        if (radioButtons != null && radioButtons.length > 0) {
            for (CheckBox checkBox : radioButtons) {
                foundAndSet = checkBox.getValue();
                if (foundAndSet) {
                    break;
                }
            }
        }
        if (!foundAndSet) {
            if (field != null) {
                field.setErrorMessage(errorMessage, null);
            }
        }
        return foundAndSet;
    }

    private boolean validateCheckBox(final WfmForm.Field field, String errorMessage, final KpiCheckBox... checkBoxs) {
        return validateRadioButton(field, errorMessage, checkBoxs);
    }

    private boolean validatePhoneNumber(final WfmForm.Field field, final PhoneNumber phoneNumber, String errorMessage) {
        if (!"".equals(phoneNumber.toString())) {
            return true;
        } else {
            field.setErrorMessage(errorMessage, "");
            return false;
        }
    }

    private boolean validateDOB(final WfmForm.Field field, final DOBWidget dobWidget) {
        return !dobWidget.box_validate(field != null ? field.getRequired() : Boolean.FALSE);
    }

    private boolean validateDatePicker(final WfmForm.Field field, final DatePicker datePicker) {
        return Validation.validateDate(datePicker, field, true);
    }

    private boolean validateDropDown(final WfmForm.Field field, final DataListBox dataListBox, String message) {
        return Validation.validateListBoxRequired(dataListBox, field, message);
    }

    protected boolean validateTextBox(final WfmForm.Field field, final TextBox textBox) {
        return Validation.validateTextBoxRequired(textBox, field);
    }

    protected boolean validateTextBox(final WfmForm.Field field, final TextBox2 textBox) {
        return Validation.validateTextBoxRequired(textBox, field);
    }

    protected boolean validateTextArea(final WfmForm.Field field, final TextArea textArea) {
        return Validation.validateTextAreaRequired(textArea, field);
    }

    protected boolean validateTextArea(final WfmForm.Field field, final TextArea2 textArea) {
        return Validation.validateTextAreaRequired(textArea, field);
    }

    protected Double getStringAsDouble(Object value) {
        if (value instanceof String) {
            String valueS = (String) value;
            valueS = valueS.replaceAll("[^0-9.]", "");
            int oneMorePoint = valueS.indexOf('.', valueS.indexOf('.') + 1);
            if (oneMorePoint != -1) {
                valueS = valueS.substring(0, oneMorePoint);
            }
            if (!"".equals(valueS)) {
                return Double.parseDouble(valueS);
            }
        } else if (value instanceof Double) {
            return (Double) value;
        }
        return null;
    }

    protected boolean getAsBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }

    protected String getPhoneAsString(Object value) {
        if (value instanceof String) {
            if (!"".equals(value)) {
                return new PhoneNumber((String) value).toString();
            }
        }
        return null;
    }

    protected String getAsString(Object value) {
        if (value instanceof String) {
            if (!"".equals(value)) {
                return (String) value;
            }
        }
        return null;
    }

    protected SelectItem getAsSelectItem(Object value) {
        if (value != null) {
            if (value instanceof SelectItem) {
                return (SelectItem) value;
            } else if (value instanceof Integer) {
                return new SelectItem((Integer) value);
            }
        }
        return new SelectItem();
    }

    protected DateNonConvertable getNonConvertable(Object value) {
        if (value != null) {
            if (value instanceof Date) {
                return new DateNonConvertable((Date) value);
            } else if (value instanceof DateNonConvertable) {
                return (DateNonConvertable) value;
            }
        }
        return null;
    }

    protected ArrayList<SelectItem> getAsSelectItemArray(Object value) {
        if (value != null) {
            if (value instanceof MatrixTable) {
                MatrixTable matrixTable = (MatrixTable) value;
                if (matrixTable.getValuesMap() != null && matrixTable.getValuesMap().size() > 0) {
                    return CRMUtils.asArrayList((SelectItem[]) matrixTable.getValuesMap().keySet().toArray(new SelectItem[]{}));
                }
            } else if (value instanceof Map) {
                Map<Object, Object> v = (Map<Object, Object>) value;
                if (v.size() > 0) {
                    return CRMUtils.asArrayList((SelectItem[]) v.keySet().toArray(new SelectItem[]{}));
                }
            }
        }
        return null;
    }
}