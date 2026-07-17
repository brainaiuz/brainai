package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.PercentageWidget;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EntityCustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EntityCustomFieldMultiLookUp;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Div;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Mar 30, 2010
 * Time: 5:35:58 PM
 * To change this template use File | Settings | File Templates.
 */

public class CustomFieldPreviewTab extends CustomTabWidget implements Constants {
    private HorizontalPanel mainPanel;
    private String uiType;
    private String dataType;
    private String title;
    private String query;
    private String lookUpType;
    private Integer referenceId;
    private String[] predefinedValues;
    private SelectItem[] predefinedEntityValues;
    private static final String DEFAULTWIDTH = "150px";
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public CustomFieldPreviewTab(String tabName) {
        super(tabName);
    }

    public void initData(String query) {
        this.query = query;
        initData();
    }

    public void initData() {
        clear();
        mainPanel = new HorizontalPanel();
        mainPanel.setSpacing(1);

        WfmForm table = new WfmForm(new String[]{"50%", "50%"});
        if (uiType != null) {
            switch (uiType) {
                case UI_TYPE_TEXTBOX:
                    TextBox prevTB = new TextBox();
                    prevTB.addStyleName("custom-text");
                    prevTB.setWidth(DEFAULTWIDTH);
                    if (dataType != null && DATA_TYPE_NUMBER.equals(dataType)) {
                        Validation.addNumericKeyboardListener(prevTB);
                    }
                    WfmForm.Field textField = table.addField(title, prevTB);
                    textField.addStyleName("form-label");
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
                case UI_TYPE_PERCENTAGE:

                    PercentageWidget percentageTB = new PercentageWidget();
                    percentageTB.getTextBox().addStyleName("custom-text");
                    percentageTB.getTextBox().setWidth("70px");

                    WfmForm.Field percentageField = table.addField(title, percentageTB);
                    percentageField.addStyleName("form-label");
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
                case UI_TYPE_TEXTBOX_EMAIL:
                    TextBox prevTBE = new TextBox();
                    prevTBE.addStyleName("custom-text");
                    prevTBE.setWidth(DEFAULTWIDTH);
                    Validation.validateEmailRequired(prevTBE);
                    WfmForm.Field emailField = table.addField(title, prevTBE);
                    emailField.addStyleName("form-label");
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
                case UI_TYPE_URL:
                    TextBox prevTBU = new TextBox();
                    prevTBU.addStyleName("custom-text");
                    prevTBU.setWidth(DEFAULTWIDTH);
                    Validation.validateUrl(prevTBU, null);
                    WfmForm.Field urlField = table.addField(title, prevTBU);
                    urlField.addStyleName("form-label");
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
                case UI_TYPE_DROPDOWN: {
                    DataListBox prevDLB = new DataListBox();
                    prevDLB.addStyleName("custom-dropdown");
                    prevDLB.setWidth(DEFAULTWIDTH);
                    prevDLB.setNullLabel(wfmStrings.pleaseSelect());
                    if (predefinedValues != null) {
                        for (int i = 0; i < predefinedValues.length; i++) {
                            prevDLB.addListItem(new SelectItem(i, predefinedValues[i]));
                        }
                    }
                    WfmForm.Field dropdownField = table.addField(title, prevDLB);
                    dropdownField.addStyleName("form-label");
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
                }
                case UI_TYPE_ENTITY_DROPDOWN: {
                    DataListBox prevDLB = new DataListBox();
                    prevDLB.addStyleName("custom-entity_dropdown");
                    prevDLB.setWidth(DEFAULTWIDTH);
                    prevDLB.setNullLabel(wfmStrings.pleaseSelect());
                    if (predefinedEntityValues != null) {
                        prevDLB.setItems(predefinedEntityValues);
                    }
                    WfmForm.Field entityDropField = table.addField(title, prevDLB);
                    entityDropField.addStyleName("form-label");
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
                }
                case TYPE_ENTITY_LOOKUP:
                    EntityCustomFieldLookUp prevEntityLookUp = new EntityCustomFieldLookUp(query);
                    prevEntityLookUp.addStyleName("custom-lookUp");
//                    prevEntityLookUp.setWidth(DEFAULTWIDTH);
                    WfmForm.Field entityLookField = table.addField(title, prevEntityLookUp);
                    entityLookField.addStyleName("form-label");
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
                case TYPE_ENTITY_MULTI_LOOKUP:
                    EntityCustomFieldMultiLookUp prevEntityMultiLookUp = new EntityCustomFieldMultiLookUp(query);
                    prevEntityMultiLookUp.addStyleName("custom-lookUp");
//                    prevEntityLookUp.setWidth(DEFAULTWIDTH);
                    WfmForm.Field entityMultiLookField = table.addField(title, prevEntityMultiLookUp);
                    entityMultiLookField.addStyleName("form-label");
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
                case UI_TYPE_CHECKBOX: {
//                    VerticalPanel vp = new VerticalPanel();
//                    vp.add(new HTML("<div style='margin:5px 0 0 10px;'><b class=customTitle form-label><font size=2>" + title + ":</font></b></div>"));
//                    if (predefinedValues != null) {
//                        for (int i = 0; i < predefinedValues.length; i++) {
//                            KpiCheckBox prevCHB = new KpiCheckBox("     " + predefinedValues[i]);
//                            table.addField(wfmStrings.option() + (i + 1), prevCHB);
//                        }
//                        vp.add(table);
//                    }
//                    mainPanel.clear();
//                    mainPanel.add(vp);
                    Div container = new Div("customwidget custom-checkbox");
                    if (predefinedValues != null) {
                        int i = 0;
                        for (String predefinedValue : predefinedValues) {
                            KpiCheckBox prevRB = new KpiCheckBox(predefinedValue);
                            prevRB.addStyleName("custom-checkbox_" + i);
                            i++;
                            container.add(prevRB);
                        }
                    }
                    WfmForm.Field radioButtonsField = table.addField(title, container);
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
                }
                case UI_TYPE_RADIOBUTTON: {
//                    VerticalPanel vp = new VerticalPanel();
//                    vp.add(new HTML("<div style='margin:5px 0 0 10px;'><b class=customTitle form-label><font size=2>" + title + ":</font></b></div>"));
//                    if (predefinedValues != null) {
//                        for (String predefinedValue : predefinedValues) {
//                            RadioButton prevRB = new RadioButton(predefinedValue);
//                            prevRB.setName("rb");
//                            table.addField(predefinedValue, prevRB);
//                        }
//                        vp.add(table);
//                    } else {
//                    }
//                    mainPanel.clear();
//                    mainPanel.add(vp);
                    Div container = new Div("customwidget custom-radios");
                    if (predefinedValues != null) {
                        int i = 0;
                        for (String predefinedValue : predefinedValues) {
                            KpiRadioButton prevRB = new KpiRadioButton("rb", predefinedValue);
                            prevRB.addStyleName("custom-checkbox_" + i);
                            i++;
                            container.add(prevRB);
                        }
                    }
                    WfmForm.Field radioButtonsField = table.addField(title, container);
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
                }
                case UI_TYPE_LOOKUP:
                    CustomFieldLookUp lookUp = new CustomFieldLookUp(CustomFieldLookUpTypeEnum.get(this.lookUpType), referenceId);
                    lookUp.addStyleName("custom-lookUp");
                    WfmForm.Field lookUpField = table.addField(title, lookUp);
                    lookUpField.addStyleName("form-label");
                    mainPanel.clear();
                    mainPanel.add(table);
                    break;
            }

            if (uiType.equals(UI_TYPE_DATEPICKER)) {
                DatePicker prevDP = new DatePicker();
                prevDP.addStyleName("custom-date");
                prevDP.setWidth(DEFAULTWIDTH);
                WfmForm.Field dateField = table.addField(title, prevDP);
                dateField.addStyleName("form-label");
                mainPanel.clear();
                mainPanel.add(table);
            }
            if (uiType.equals(UI_TYPE_DATEPICKER_TIME)) {
                DatePicker prevDP = new DatePicker();
                prevDP.addStyleName("custom-date");
                prevDP.setWidth(DEFAULTWIDTH);

                KpiTimePicker time = new KpiTimePicker(true);
                time.setMarginTop(0);
                time.setPaddingLeft(8);
                time.setWidth("20%");
                time.setStyleName("timepicker input-group-content");

                WfmForm.Field dateField = table.addField(title, new InputGroup(prevDP, time));
                dateField.addStyleName("form-label");
                mainPanel.clear();
                mainPanel.add(table);
            }
            if (uiType.equals(UI_TYPE_FILE_UPLOAD_WIDGET)) {
                GeneralFileUpload fileUploadField = new GeneralFileUpload(F_CUSTOM_FIELD_ITEM, null, null);
                fileUploadField.setStyleName("pg_custom__preview_upload_table custom-fileUpload");
                WfmForm.Field uploadField = table.addField(title, fileUploadField);
                uploadField.addStyleName("form-label");
                mainPanel.clear();
                mainPanel.add(table);
            }

            if (uiType.equals(UI_TYPE_TEXTAREA)) {
                TextArea prevTextArea = new TextArea();
                prevTextArea.addStyleName("custom-textarea");
                WfmForm.Field textareaField = table.addField(title, prevTextArea);
                textareaField.addStyleName("form-label");
                mainPanel.clear();
                mainPanel.add(table);
            }
            if (uiType.equals(UI_TYPE_HTML_TEXTAREA)) {
                KpiEditor prevHtmlTextArea = new KpiEditor();
                prevHtmlTextArea.addStyleName("custom-textarea");
                WfmForm.Field htmlTextareaField = table.addField(title, prevHtmlTextArea);
                htmlTextareaField.addStyleName("form-label");
                mainPanel.clear();
                mainPanel.add(table);
            }

        } else {
            mainPanel.clear();
        }
        add(mainPanel);
    }

    public void viewShow() {

    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setPredefinedValues(String[] predefinedValues) {
        this.predefinedValues = predefinedValues;
    }

    public SelectItem[] getPredefinedEntityValues() {
        return predefinedEntityValues;
    }

    public void setPredefinedEntityValues(SelectItem[] predefinedEntityValues) {
        this.predefinedEntityValues = predefinedEntityValues;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setLookUpType(String lookUpType) {
        this.lookUpType = lookUpType;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }
}
