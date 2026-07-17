package com.edatasite.workforce.gwt.webforms.client.forms.customform;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.TextBox2;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.crm.client.ui.CheckboxMailingListDataGrid;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUpload;
import com.edatasite.workforce.gwt.documents.client.upload.WebFormsFileUpload;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.WebFormsService;
import com.edatasite.workforce.gwt.webforms.client.WebFormsServiceAsync;
import com.edatasite.workforce.gwt.webforms.client.forms.WebField;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 2/17/12
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAddFormViewWithCustomForm extends Composite {

    protected Command addedSuccessfully;
    protected Command errorOn;
    protected Set<Integer> errors = new HashSet<>();
    protected Set<Integer> customFieldsErrors = new HashSet<>();
    protected VerticalPanel antibotPanel;
    protected WebForm webForm;
    protected static final WebFormsServiceAsync webFormsService = WebFormsService.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    protected abstract void setWebFormID(Integer formID);

    public abstract void save(String antibot);

    public abstract Command getAddedSuccessfully();

    public abstract void setAddedSuccessfully(Command addedSuccessfully);

    public abstract Command getErrorOn();

    public abstract void setErrorOn(Command errorOn);

    protected abstract void fillItem(WebField webField, Object value, Widget widget);

    protected abstract void addField(WebField webField);

    protected AbstractAddFormViewWithCustomForm(WebForm webForm, VerticalPanel antibotPanel) {
        initForm(webForm.getCustomForm());
        this.webForm = webForm;
        setWebFormID(webForm.getObjectId());
        this.antibotPanel = antibotPanel;
        init();
    }

    protected void init() {
        setWebFormID(webForm.getObjectId());
        if (webForm.getWebFields() != null) {
            for (WebField webField : webForm.getWebFields()) {
                if (webField.isShowInForm()) {
                    if (webField.isMandatory()) {
                        addErrors(webField, webField.getSavingField());
                    }
                    addField(webField);
                } else {
                    if (webField.getDefaultValue() != null) {
                        fillItem(webField, webField.getDefaultValue(), null);
                    }
                }
            }
        }
    }

    protected Widget addFieldToForm(WebField webField) {
        Widget widget = getFieldWidget(webField, webField.getDefaultValue(), webField.getValues());
        addField((webField.isCustomField() ? "CF_" : "SF_") + webField.getSavingField(), widget, webField.getLabel());
        return widget;
    }

    protected void validate(WebField webField, Widget widget, boolean... isEmails) {
        if (widget == null) {
            return;
        }
        boolean isEmail = isEmails != null && isEmails.length > 0 && isEmails[0];
        Integer errorField = webField.getSavingField();
        Integer type = webField.getType();
        String errorMessage = "";
        removeError(webField, errorField);
        switch (type) {
            case WebFormConstants.INPUT_TEXTBOX:
            case WebFormConstants.INPUT_TEXTBOX2:
                if (isEmail) {
                    if (!Validation.validateEmailRequired((TextBox) widget, null, false)) {
                        addErrors(webField, errorField);
                    }
                } else if ("".equals(((TextBox) widget).getText())) {
                    addErrors(webField, errorField);
                }
                break;
            case WebFormConstants.INPUT_TEXTAREA:
                if ("".equals(((TextArea) widget).getText())) {
                    addErrors(webField, errorField);
                }
                break;
            case WebFormConstants.INPUT_TEXTAREA2:
                if ("".equals(((TextArea2) widget).getText())) {
                    addErrors(webField, errorField);
                }
                break;
            case WebFormConstants.INPUT_DROPDOWN:
                if (((DataListBox) widget).getSelectedItem() == null) {
                    addErrors(webField, errorField);
                }
                break;
            case WebFormConstants.INPUT_DATEPICKER:
                if (((DatePicker) widget).getDate() == null) {
                    addErrors(webField, errorField);
                }
                break;
            case WebFormConstants.INPUT_PHONENUMBER:
                if ("".equals(widget.toString())) {
                    addErrors(webField, errorField);
                }
                break;
            case WebFormConstants.INPUT_CHECKBOX:
                if (!validateCheckBox(null, errorMessage, (KpiCheckBox[]) getWidgets(widget, WebFormConstants.INPUT_CHECKBOX))) {
                    addErrors(webField, errorField);
                }
                break;
            case WebFormConstants.INPUT_RADIO_BUTTON:
                if (!validateRadioButton(null, errorMessage, (RadioButton[]) getWidgets(widget, WebFormConstants.INPUT_RADIO_BUTTON))) {
                    addErrors(webField, errorField);
                }
                break;
            case WebFormConstants.INPUT_ATTACHMENT:
                if (((WebFormsFileUpload) widget).getAttachedFiles() == null || ((WebFormsFileUpload) widget).getAttachedFiles().length == 0) {
                    addErrors(webField, errorField);
                }
                break;
            case WebFormConstants.INPUT_MAILING_LIST:
                if (widget instanceof CheckboxMailingListDataGrid && (((CheckboxMailingListDataGrid) widget).getSelectedIdsList() == null || ((CheckboxMailingListDataGrid) widget).getSelectedIdsList().size() == 0)) {
                    addErrors(webField, errorField);
                }
                break;
        }
    }


    public Widget[] getWidgets(Widget widgets, int type) {
        if (widgets != null && widgets instanceof VerticalPanel) {
            VerticalPanel panel = (VerticalPanel) widgets;
            Widget[] newWidgets = null;
            if (WebForm.INPUT_RADIO_BUTTON == type) {
                newWidgets = new RadioButton[panel.getWidgetCount()];
            } else if (WebForm.INPUT_CHECKBOX == type) {
                newWidgets = new KpiCheckBox[panel.getWidgetCount()];
            }
            for (int i = 0; i < panel.getWidgetCount(); i++) {
                if (panel.getWidget(i) != null) {
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
        if (errors.size() > 0) {
            return true;
        }
        return customFieldsErrors.size() > 0;
    }

    protected Widget getFieldWidget(final WebField webField, Object defaultValue, SelectItem[] values) {
        Integer type = webField.getType();
        Widget widget = null;
        Widget[] widgets = null;
        if (type.equals(WebFormConstants.INPUT_TEXTBOX)) {
            final TextBox textBox = new TextBox();
            textBox.addStyleName(DEFAULT_WIDTH);
            if (defaultValue != null && defaultValue instanceof String) {
                textBox.setValue((String) defaultValue);
                fillItem(webField, textBox.getText(), textBox);
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
            }
            textArea.addBlurHandler(event -> fillItem(webField, textArea.getText(), textArea));
            widget = textArea;
        } else if (type.equals(WebFormConstants.INPUT_TEXTAREA2)) {
            final TextArea2 textArea2 = new TextArea2(1000);
            textArea2.addStyleName(DEFAULT_WIDTH);
            if (defaultValue != null && defaultValue instanceof String) {
                textArea2.setText((String) defaultValue);
                fillItem(webField, textArea2.getText(), textArea2);
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
                    dataListBox.setSelected((Integer) defaultValue);
                } else if (defaultValue instanceof SelectItem) {
                    dataListBox.setSelected((SelectItem) defaultValue);
                } else if (defaultValue instanceof String) {
                    dataListBox.setSelectedByValue((String) defaultValue);
                }
                fillItem(webField, dataListBox.getSelectedItem(), dataListBox);
            }
            dataListBox.addBlurHandler(event -> fillItem(webField, dataListBox.getSelectedItem(), dataListBox));
            widget = dataListBox;
        } else if (type.equals(WebFormConstants.INPUT_DATEPICKER)) {
            final DatePicker datePicker = new DatePicker();
            datePicker.setDefaultPosition(true);
            datePicker.addStyleName(DEFAULT_WIDTH);
            if (defaultValue != null) {
                if (defaultValue instanceof Date) {
                    datePicker.setDate((Date) defaultValue);
                }
                fillItem(webField, datePicker.getDate(), datePicker);
            }
            datePicker.addChangeHandler(event -> fillItem(webField, datePicker.getDate(), datePicker));

            datePicker.addChangeHandler(event -> fillItem(webField, datePicker.getDate(), datePicker));
            widget = datePicker;
        } else if (type.equals(WebFormConstants.INPUT_PHONENUMBER)) {
            final PhoneNumber phoneNumber = defaultValue != null ? new PhoneNumber((String) defaultValue) : new PhoneNumber("");
            if (defaultValue != null) {
                fillItem(webField, phoneNumber.toString(), phoneNumber);
            }
            phoneNumber.addBlurHandler(event -> fillItem(webField, phoneNumber.toString(), phoneNumber));
            widget = phoneNumber.getField();
        } else if (type.equals(WebFormConstants.INPUT_CHECKBOX)) {
            if (webField.isCustomField() && webField.getValues() != null) {
                SelectItem[] defaultValues = Utils.getAsSelectItem((String) webField.getDefaultValue(true), "'");
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
                        }
                        radioButton.addBlurHandler(event -> fillItem(webField, radioButton.getText(), radioButton));
                        radioButton.addClickHandler(event -> fillItem(webField, radioButton.getText(), radioButton));
                        radioButtons.add(radioButton);
                    }
                }
                widgets = radioButtons.toArray(new KpiCheckBox[]{});
            } else {
                final RadioButton radioButton = new KpiRadioButton(webField.getLabel());
                if (defaultValue != null) {
                    radioButton.setValue((Boolean) defaultValue);
                    fillItem(webField, radioButton.getValue(), radioButton);
                }
                radioButton.addBlurHandler(event -> fillItem(webField, radioButton.getValue(), radioButton));
                radioButton.addClickHandler(event -> fillItem(webField, radioButton.getValue(), radioButton));
                widget = radioButton;
            }
        } else if (type.equals(WebFormConstants.INPUT_ATTACHMENT)) {
            final WebFormsFileUpload fileUpload = new WebFormsFileUpload(true);
            fileUpload.addStyleName(DEFAULT_WIDTH);
            Command command = () -> fillItem(webField, fileUpload.getAttachedFiles(), fileUpload);
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
            }
            widget = mailingListTable;
        }
        VerticalPanel vp = new VerticalPanel();
        if (widgets != null && widgets.length > 0) {
            for (Widget w : widgets) {
                vp.add(w);
            }
        }

        return widgets != null && widgets.length > 0 ? vp : widget;
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

    private boolean validateUploadForm(final WfmForm.Field field, final WebFormsFileUpload uploadField, String errorMessage) {
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

    private boolean validateDatePicker(final WfmForm.Field field, final DatePicker datePicker, String errorMessage) {
        return Validation.validateDate(datePicker, field, true);
    }

    private boolean validateDropDown(final WfmForm.Field field, final DataListBox dataListBox, String message) {
        return Validation.validateListBoxRequired(dataListBox, field, message);
    }

    protected boolean validateTextBox(final WfmForm.Field field, final TextBox textBox, String errorMessage) {
        return Validation.validateTextBoxRequired(textBox, field);
    }

    protected boolean validateTextBox(final WfmForm.Field field, final TextBox2 textBox, String errorMessage) {
        return Validation.validateTextBoxRequired(textBox, field);
    }

    protected boolean validateTextArea(final WfmForm.Field field, final TextArea textArea, String errorMessage) {
        return Validation.validateTextAreaRequired(textArea, field);
    }

    protected boolean validateTextArea(final WfmForm.Field field, final TextArea2 textArea, String errorMessage) {
        return Validation.validateTextAreaRequired(textArea, field);
    }

    protected Double getStringAsDouble(String value) {
        value = value.replaceAll("[^0-9.]", "");
        int oneMorePoint = value.indexOf('.', value.indexOf('.') + 1);
        if (oneMorePoint != -1) {
            value = value.substring(0, oneMorePoint);
        }
        if (!"".equals(value)) {
            return Double.parseDouble(value);
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


    protected HTMLPanel panel;
    private boolean isReadyToReplace;
    private List<Field> widgetsInQueue = new ArrayList<>();
    private LayoutRPC formRPC;
    private boolean show;

    protected void initForm(LayoutRPC formRpc) {
        this.formRPC = formRpc;
        panel = new HTMLPanel(formRPC.getLayout());
        System.out.println(formRpc.getLayout());
        initWidget(panel);
        panel.setVisible(false);// we must not show form until its filled with widgets ...
        isReadyToReplace = false;
        onReadyToReplaceFields();
    }

    public void onReadyToReplaceFields() {
        if (isReadyToReplace && widgetsInQueue.size() > 0) {
            for (Field field : widgetsInQueue) {
                addFieldToPanel(field);
            }
            widgetsInQueue.clear();
        }
    }

    private void addFieldToPanel(Field field) {
        if (show && panel != null && !panel.isVisible()) {
            panel.setVisible(true);
        }
        if (!field.isUseless()) {
            if (field.getTitle() != null) {
                Element element = DOM.getElementById(field.getId() + "_title");
                if (element != null) {
                    panel.addAndReplaceElement(new HTML(field.getTitle()), element);
                }
            }
            Element element = DOM.getElementById(field.getId());
            if (element != null) {
                panel.addAndReplaceElement(field.getWidget(), element);
            }
        }
    }


    protected abstract String getFormID();

    public void addField(String id, Widget widget, String title) {
        new Field(id, title, widget);
    }

    public void show() {
        show = true;
        isReadyToReplace = true;
        getDataToFillFields();
    }

    protected abstract void getDataToFillFields();

    private class Field {
        private Field(String id, String title, Widget widget) {
            this.id = id;
            this.title = title;
            this.widget = widget;
            if (!isReadyToReplace) {
                widgetsInQueue.add(this);
            } else {
                addFieldToPanel(this);
            }
        }

        private String id;
        private String title;
        private Widget widget;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Widget getWidget() {
            return widget;
        }

        public void setWidget(Widget widget) {
            this.widget = widget;
        }

        public boolean isUseless() {
            return widget == null || id == null || "".equals(id);
        }
    }
}
