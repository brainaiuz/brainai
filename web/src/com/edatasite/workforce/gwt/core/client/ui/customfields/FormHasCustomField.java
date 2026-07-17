package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldSettingItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.BaseQuickAddView;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidgetCustomField;
import com.edatasite.workforce.gwt.core.client.ui.PercentageWidget;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.UL;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EntityCustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EntityCustomFieldMultiLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectCustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUploadItem;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusListenerAdapter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PRODUCT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_QUOTE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_QUOTE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PRODUCT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PURCHASE_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PURCHASE_ORDER_SUMMARY;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 3/21/12
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class FormHasCustomField implements Constants, Serializable {
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    public static final WfmMessages wfmMessages = WfmMessages.App.get();
    private ArrayList<CompanyCustomFieldItem> companyCustomFieldItems;
    public HashMap<String, Object> tbValues = new HashMap<>();
    public HashMap<String, Widget> validationObjects = new HashMap<>();
    ArrayList<Widget> errorWidgets = new ArrayList<>();
    private final Map<Integer, String> customFieldsCode = new HashMap<>();
    private final Integer objectId = null;
    public Command lookUpSelection;
    public ArrayList<SelectItem> selectedItems;
    public ArrayList<SelectItem> triggersLookUpTypeName;
    private Map<String, List<CompanyCustomFieldItem>> customLogicFieldMap = new HashMap<>();

    public void drawCustomFields(WfmForm table) {
        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
            table.addHorizontalLine();
            table.addTitleField(wfmStrings.customFields() + ":");
            for (CompanyCustomFieldItem customField : companyCustomFieldItems) {
                Widget widget = null;
                customField.setFileUploadFieldId(customField.getObjectId());
                customField.setObjectId(null);
                if (UI_TYPE_TEXTBOX.equals(customField.getUiType())) {
                    TextBox textField = new TextBox();
                    textField.addStyleName("width250");
                    textField.setEnabled(!customField.isDisabled());
                    if (DATA_TYPE_NUMBER.equals(customField.getDataType())) {
                        if (customField.getScale() != null) {
                            Validation.addNumericKeyboardListener(textField, customField.getScale());
                        } else {
                            Validation.addNumericKeyboardListener(textField);
                        }
                        textField.addValueChangeHandler(handler -> {
                            Double d = null;
                            try {
                                d = Double.valueOf(handler.getValue());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            if (customField.getNumberMinValue() != null) {
                                if (customField.getNumberMinValue() > d)
                                    Info.show("you need to add sjdlansdiuabsdnubdn");
                            }
                        });
                    }
                    tbValues.put(customField.getColumnCode(), textField);
                    widget = textField;
                } else if (UI_TYPE_DROPDOWN.equals(customField.getUiType())) {

                    KpiSelect2 select2 = new KpiSelect2();
                    select2.addStyleName("width250");

                    if (customField.getPredefinedValues() != null) {

                        AtomicInteger counter = new AtomicInteger(0);
                        String[] values = customField.getPredefinedValues();

                        Arrays.sort(values, String::compareTo);

                        ArrayList<SelectItem> v = Arrays.stream(values)
                                .map(item -> new SelectItem(counter.getAndIncrement(), item))
                                .collect(Collectors.toCollection(ArrayList::new));

                        select2.setItems(v);
                    }

                    tbValues.put(customField.getColumnCode(), select2);
                    widget = select2;
                } else if (UI_TYPE_CHECKBOX.equals(customField.getUiType())) {
                    if (customField.getPredefinedValues() != null) {
                        HorizontalPanel hp = new HorizontalPanel();
                        VerticalPanel vp = new VerticalPanel();
                        String[] s = customField.getPredefinedValues();
                        KpiCheckBox[] checkbox = new KpiCheckBox[s.length];
                        for (
                                int j = 0;
                                j < s.length;
                                j++) {
                            checkbox[j] = new KpiCheckBox("     " + s[j]);
                            checkbox[j].setName(s[j]);
                            hp.add(checkbox[j]);
                            hp.add(new HTML("&nbsp &nbsp &nbsp"));
                        }
                        tbValues.put(customField.getColumnCode(), checkbox);
                        vp.add(hp);
                        widget = vp;
                    }
                } else if (UI_TYPE_RADIOBUTTON.equals(customField.getUiType())) {
                    HorizontalPanel hp = new HorizontalPanel();
                    VerticalPanel vp = new VerticalPanel();
                    String[] s = customField.getPredefinedValues();
                    RadioButton[] radioButton = new RadioButton[s.length];
                    for (
                            int j = 0;
                            j < s.length;
                            j++) {
                        radioButton[j] = new KpiRadioButton(s[j]);
                        radioButton[j].setText(s[j]);
                        radioButton[j].setName("rb_" + customField.getColumnCode() + "");
                        hp.add(radioButton[j]);
                        hp.add(new HTML("&nbsp &nbsp &nbsp"));
                    }
                    tbValues.put(customField.getColumnCode(), radioButton);
                    vp.add(hp);
                    widget = vp;
                } else if (UI_TYPE_DATEPICKER.equals(customField.getUiType())) {
                    DatePicker dateField = new DatePicker();
                    dateField.addStyleName("width250");
                    tbValues.put(customField.getColumnCode(), dateField);
                    widget = dateField;
                } else if (UI_TYPE_FILE_UPLOAD_WIDGET.equals(customField.getUiType())) {
                    GeneralFileUpload fileUploadField = new GeneralFileUpload(F_CUSTOM_FIELD_ITEM, objectId, objectId);
                    tbValues.put(customField.getColumnCode(), fileUploadField);
                    widget = fileUploadField;
                } else if (UI_TYPE_FILE_UPLOAD_ITEM.equals(customField.getUiType())) {
                    GeneralFileUploadItem fileUploadFieldItem = new GeneralFileUploadItem(F_CUSTOM_FIELD_ITEM);
                    tbValues.put(customField.getColumnCode(), fileUploadFieldItem);
                    widget = fileUploadFieldItem;
                } else if (UI_TYPE_PROFILE_IMAGE_WIDGET.equals(customField.getUiType())) {
                    ProfileImage profileImage = new ProfileImage();
                    profileImage.getImageById(customField.getProfielImageId(), "Profile", "Image", false);
                    tbValues.put(customField.getColumnCode(), profileImage);
                    widget = profileImage;
                } else if (UI_TYPE_ENTITY_DROPDOWN.equals(customField.getUiType())) {
                    DataListBox numberField = new DataListBox();
                    numberField.addStyleName("width250");
                    if (customField.getQueryItems() != null) {
                        numberField.setItems(customField.getQueryItems());
                    }
                    tbValues.put(customField.getColumnCode(), numberField);
                    widget = numberField;
                } else if (TYPE_ENTITY_LOOKUP.equals(customField.getUiType())) {
                    EntityCustomFieldLookUp entityCustomFieldLookUp = new EntityCustomFieldLookUp(customField.getQuery());
                    entityCustomFieldLookUp.setWidth("150");
                    tbValues.put(customField.getColumnCode(), entityCustomFieldLookUp);
                    widget = entityCustomFieldLookUp;
                } else if (TYPE_ENTITY_MULTI_LOOKUP.equals(customField.getUiType())) {
                    EntityCustomFieldMultiLookUp entityCustomFieldMultiLookUp = new EntityCustomFieldMultiLookUp(customField.getQuery());
                    entityCustomFieldMultiLookUp.setWidth("150");
                    tbValues.put(customField.getColumnCode(), entityCustomFieldMultiLookUp);
                    widget = entityCustomFieldMultiLookUp;
                } else if (UI_TYPE_TEXTAREA.equals(customField.getUiType())) {
                    TextArea textAreaField = new TextArea();
                    textAreaField.setEnabled(!customField.isDisabled());
                    tbValues.put(customField.getColumnCode(), textAreaField);
                    widget = textAreaField;
                } else if (UI_TYPE_HTML_TEXTAREA.equals(customField.getUiType())) {
                    KpiEditor htmlTextAreaField = new KpiEditor();
                    tbValues.put(customField.getColumnCode(), htmlTextAreaField);
                    widget = htmlTextAreaField;
                } else if (UI_TYPE_COMMITBOX.equals(customField.getUiType())) {
                    NoteWidgetCustomField commentNoteWidget = new NoteWidgetCustomField(objectId, customField.getForm(), customField);
                    commentNoteWidget.setCustomFieldItem(customField);
                    tbValues.put(customField.getColumnCode(), commentNoteWidget);
                    widget = commentNoteWidget;
                }
                if (widget != null) {
                    if (customField.getFieldName() != null && customField.getFieldName().length() > 0) {
                        String className = "cf_" + customField.getFieldName().replaceAll("[^a-zA-Z]+", "_").toLowerCase();
                        widget.addStyleName(className);
                    }
                    table.addField(customField.getFieldName(), widget, customField.isRequired());
                }
            }
            table.addHorizontalLine();
        }
    }

    private String getHourValue(String text) {
        String[] time = text.split(":");
        return (Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60) + "";
    }

    public boolean hasCustomFields() {
        return companyCustomFieldItems != null && !companyCustomFieldItems.isEmpty();
    }

    public void fillCustomFieldsWithData(ArrayList<CompanyCustomFieldItem> datas, boolean... viewMode) {
        boolean isViewMode = viewMode != null && viewMode.length > 0 && viewMode[0];
        if (!hasCustomFields()) {
            return;
        }
        if (datas == null || datas.isEmpty()) {
            return;
        }
        for (CompanyCustomFieldItem data : datas) {
            if (data == null) {
                continue;
            }
            CompanyCustomFieldItem companyCustomFieldItem = getCustomFieldsAsMap().get(data.getColumnCode());
            if (companyCustomFieldItem != null && data.getColumnCode() != null && data.getColumnCode().equals(companyCustomFieldItem.getColumnCode())) {
                companyCustomFieldItem.setObjectId(data.getObjectId());
                if (isViewMode) {
                    if (UI_TYPE_FILE_UPLOAD_WIDGET.equals(companyCustomFieldItem.getUiType())) {
                        GeneralFileUpload fileUploadField = (GeneralFileUpload) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getFieldStringValue() != null) {
                            fileUploadField.setCustomFieldData(Double.valueOf(data.getFieldStringValue()).intValue(), data.getObjectId());
                        }
                    } else if (UI_TYPE_FILE_UPLOAD_ITEM.equals(companyCustomFieldItem.getUiType())) {
                        GeneralFileUploadItem fileUploadFieldItem = (GeneralFileUploadItem) tbValues.get(companyCustomFieldItem.getColumnCode());
                        fileUploadFieldItem.setViewMode();
                        if (data.getFieldStringValue() != null) {
                            fileUploadFieldItem.setFiles(Double.valueOf(data.getFieldStringValue()).intValue(), data.getObjectId(), true);
                        }
                    } else if (UI_TYPE_PROFILE_IMAGE_WIDGET.equals(companyCustomFieldItem.getUiType())) {
                        ProfileImage profileImage = (ProfileImage) tbValues.get(companyCustomFieldItem.getColumnCode());
                        profileImage.getImageById(data.getProfielImageId(), "Profile", "Image", false);
                    } else if (UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                        TextArea textAreaField = (TextArea) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (textAreaField != null) {
                            textAreaField.setEnabled(false);
                            textAreaField.setText(data.getFieldStringValue());
                        }
                    } else if (UI_TYPE_HTML_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                        KpiEditor htmlTextAreaField = (KpiEditor) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (htmlTextAreaField != null) {
                            htmlTextAreaField.setEnabled(false);
                            htmlTextAreaField.setData(data.getFieldStringValue());
                        }
                    } else if (tbValues.get(companyCustomFieldItem.getColumnCode()) instanceof HTML) {
                        HTML html = (HTML) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) && data.getFieldDateNonConvertedValue() != null) {
                            html.setHTML(DateUtils.format(data.getFieldDateNonConvertedValue().getNonConvertedDate()));
                        } else if (UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType()) && data.getFieldDateNonConvertedValue() != null) {
                            html.setHTML(DateUtils.formatInternal(data.getFieldDateNonConvertedValue().getNonConvertedDate()));
                        } else if (UI_TYPE_ENTITY_DROPDOWN.equals(companyCustomFieldItem.getUiType()) || TYPE_ENTITY_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                            Integer id = null;
                            try {
                                id = Integer.valueOf(data.getFieldStringValue());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            if (companyCustomFieldItem.getQueryItems() != null) {
                                for (SelectItem selectItem : companyCustomFieldItem.getQueryItems()) {
                                    if (selectItem.getId().equals(id)) {
                                        html.setHTML(selectItem.getName());
                                        break;
                                    }
                                }
                            }

                        } else if (TYPE_ENTITY_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                            HTML entityMultiLookUp = (HTML) tbValues.get(companyCustomFieldItem.getColumnCode());
                            StringBuilder finalValue = new StringBuilder();
                            if (data.getSelectItems() != null && data.getSelectItems().size() > 0) {
                                for (SelectItem item : data.getSelectItems()) {
                                    finalValue.append(item.getName()).append("; ");
                                }
                            }
                            entityMultiLookUp.setHTML(finalValue.toString());
                        } else if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && companyCustomFieldItem.getLookUpTypeEnum() != null) {
                            switch (companyCustomFieldItem.getLookUpTypeEnum()) {
                                case PROJECT:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("ProjectManagement.html#project|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case PRODUCT:
                                    if (data.getSelectedId() != null && !data.isDeleted() && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_SUMMARY : ACCOUNTING_PRODUCT_SUMMARY)) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Accounting.html#product|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case TASK:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("ProjectManagement.html#task|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case OPPORTUNITY:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Crm.html#opportunity|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case OPPORTUNITY_NAME:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Crm.html#opportunity|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case PURCHASE_ORDER:
                                    if (data.getSelectedId() != null && !data.isDeleted() && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_ORDER_SUMMARY : ACCOUNTING_PURCHASE_ORDER_SUMMARY)) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Accounting.html#purchaseorder|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case PURCHASE_INVOICE:
                                    if (data.getSelectedId() != null && !data.isDeleted() && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_INVOICE_SUMMARY : ACCOUNTING_PURCHASE_INVOICE_SUMMARY)) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Accounting.html#purchaseinvoice|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case SALES_ORDER:
                                    if (data.getSelectedId() != null && !data.isDeleted() && Utils.isCRM() ? Utils.hasPermission(CRM_SALES_ORDER_SUMMARY) : (Utils.hasPermission(ACCOUNTING_SALES_ORDER_SUMMARY))) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Accounting.html#saleorder|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case SALES_QUOTE:
                                    if (data.getSelectedId() != null && !data.isDeleted() && Utils.isCRM() ? Utils.hasPermission(CRM_SALES_QUOTE_SUMMARY) : (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_SUMMARY))) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Accounting.html#salequote|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case SALES_INVOICE:
                                    if (data.getSelectedId() != null && !data.isDeleted() && Utils.isCRM() ? Utils.hasPermission(CRM_SALES_INVOICE_SUMMARY) : (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SUMMARY))) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Accounting.html#saleinvoice|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case CASE:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Crm.html#case|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case CONTACT:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Crm.html#contact|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case EMPLOYEE:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Hrms.html#employeeProfile|employeeProfileView/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case LEAD:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Crm.html#lead|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case CUSTOMER:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> {
                                            if (Utils.getPathName().contains("ProjectManagement.html")) {
                                                SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + data.getSelectedId());
                                            } else {
                                                Utils.openURLCurrentTab("Accounting.html#client|summary/" + data.getSelectedId());
                                            }
                                        });
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case SUPPLIER:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Accounting.html#suppliersummary|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case PRODUCT_CATEGORY:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Settings.html#productcategory|edit/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case CANDIDATE:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Hrms.html#candidate|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                case DEPARTMENT:
                                    if (data.getSelectedId() != null && !data.isDeleted()) {
                                        html.addClickHandler(click -> Utils.openURLCurrentTab("Settings.html#department|summary/" + data.getSelectedId()));
                                        html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                    } else {
                                        html.setHTML(data.getFieldStringValue());
                                    }
                                    break;
                                default:
                                    html.setHTML(data.getFieldStringValue());
                                    break;
                            }

                        } else {
                            if (DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType())
                                    && (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType())
                                    || UI_TYPE_PERCENTAGE.equals(companyCustomFieldItem.getUiType()))) {
                                Double number = null;
                                if (data.getFieldStringValue() != null) {
                                    try {
                                        number = Double.valueOf(data.getFieldStringValue());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
                                html.setHTML(data.getFieldStringValue());
                                if (number != null) {
                                    if (UI_TYPE_PERCENTAGE.equals(companyCustomFieldItem.getUiType())) {
                                        String format = (data.getFieldStringValue().indexOf(':') == -1 ? data.getFieldStringValue() : getHourValue(data.getFieldStringValue()));
                                        BigDecimal price = parsePriceToBigDecimal(format);
                                        html.setHTML(formatCustomPrice(price, companyCustomFieldItem.getScale()) + " %");
                                    } else {
                                        if (data.getFieldStringValue().indexOf(':') == -1 &&
                                                (LocaleInfo.getCurrentLocale().getLocaleName().equals("es") ||
                                                        LocaleInfo.getCurrentLocale().getLocaleName().equals("de") ||
                                                        LocaleInfo.getCurrentLocale().getLocaleName().equals("it"))) {
                                            String nText = data.getFieldStringValue();
                                            nText = nText.replace(".", LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator());

                                            NumberFormat priceFormat = NumberFormat.getFormat(",##0.00000");
                                            html.setHTML(formatCustomPrice(BigDecimal.valueOf(priceFormat.parse(nText)), companyCustomFieldItem.getScale()));
                                        } else {
                                            String format = (data.getFieldStringValue().indexOf(':') == -1
                                                    ? data.getFieldStringValue()
                                                    : getHourValue(data.getFieldStringValue()));
                                            BigDecimal price = parsePriceToBigDecimal(format);
                                            html.setHTML(formatCustomPrice(price, companyCustomFieldItem.getScale()));
                                        }

                                    }
                                }
                            } else if (UI_TYPE_URL.equals(companyCustomFieldItem.getUiType())) {
                                if (data.getFieldStringValue() != null) {
                                    html.addClickHandler(click -> {
                                        String url = "";
                                        if (data.getFieldStringValue().contains("https://")) {
                                            url = data.getFieldStringValue().split("https://")[1];
                                        } else if (data.getFieldStringValue().contains("http://")) {
                                            url = data.getFieldStringValue().split("http://")[1];
                                        } else {
                                            url = data.getFieldStringValue();
                                        }

                                        Window.open("//" + url, "_blank", null);
                                    });
                                    html.setHTML(data.getFieldStringValue() != null ? "<a href=\"javascript:\">" + data.getFieldStringValue() + "</a>" : "");
                                }
                            } else {
                                if (data.getFieldStringValue() != null) {
                                    StringBuilder s = new StringBuilder();
                                    if (data.getFieldStringValue().contains("-:-") && !UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) && !UI_TYPE_HTML_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                        for (String value : data.getFieldStringValue().split("-:-")) {
                                            if (value != null && !"".equals(value.trim()) && data.getPredefinedValues() != null) {
                                                data.getPredefinedValues();
                                                for (String pv : data.getPredefinedValues()) {
                                                    if (value.equals(pv.toLowerCase())) {
                                                        s.append(", ").append(value.trim());
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        s.append(data.getFieldStringValue());
                                    }
                                    html.setHTML(s.toString().startsWith(",")
                                            ? s.substring(1, s.length())
                                            : s.toString());
                                }
                            }
                        }
                    }
                } else {
                    if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_URL.equals(companyCustomFieldItem.getUiType())) {
                        if (DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType())) {
                            TextBox textField = (TextBox) tbValues.get(companyCustomFieldItem.getColumnCode());
                            Double number = null;
                            if (data.getFieldStringValue() != null) {
                                if (data.getFieldStringValue().contains(",")) {
                                    data.setFieldStringValue(data.getFieldStringValue().replace(",", ""));
                                }
                                try {
                                    number = Double.valueOf(data.getFieldStringValue());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (number != null) {
                                if (data.getFieldStringValue().indexOf(':') == -1 &&
                                        (LocaleInfo.getCurrentLocale().getLocaleName().equals("es") ||
                                                LocaleInfo.getCurrentLocale().getLocaleName().equals("de") ||
                                                LocaleInfo.getCurrentLocale().getLocaleName().equals("it"))) {
                                    String nText = data.getFieldStringValue();
                                    nText = nText.replace(".", LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator());

                                    NumberFormat priceFormat = NumberFormat.getFormat(",##0.00000");
                                    textField.setText(formatCustomPrice(BigDecimal.valueOf(priceFormat.parse(nText)), companyCustomFieldItem.getScale()));
                                } else {
                                    String format = (data.getFieldStringValue().indexOf(':') == -1 ? data.getFieldStringValue() : getHourValue(data.getFieldStringValue()));
                                    BigDecimal price = parsePriceToBigDecimal(format);
                                    textField.setText(formatCustomPrice(price, companyCustomFieldItem.getScale()));
                                }

                            }
                        } else {
                            TextBox textField = (TextBox) tbValues.get(companyCustomFieldItem.getColumnCode());
                            textField.setText(data.getFieldStringValue());
                        }
                    } else if (UI_TYPE_PERCENTAGE.equals(companyCustomFieldItem.getUiType())) {
                        PercentageWidget percentageWidget = (PercentageWidget) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getFieldStringValue() != null) {
                            String format = (data.getFieldStringValue().indexOf(':') == -1 ? data.getFieldStringValue() : getHourValue(data.getFieldStringValue()));
                            BigDecimal price = parsePriceToBigDecimal(format);
                            percentageWidget.setText(formatCustomPrice(price, companyCustomFieldItem.getScale()));
                        }

                    } else if (UI_TYPE_URL.equals(companyCustomFieldItem.getUiType())) {
                        TextBox urlWidget = (TextBox) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getFieldStringValue() != null) {
                            urlWidget.setText(data.getFieldStringValue());
                        }

                    } else if (UI_TYPE_AUTONUMBER.equals(companyCustomFieldItem.getUiType())) {
                        AutoNumberCustomField autoNumber = (AutoNumberCustomField) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getFieldStringValue() != null) {
                            autoNumber.setText(data.getFieldStringValue());
                        }
                    } else if (UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                        KpiSelect2 textField = (KpiSelect2) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getPredefinedValues() != null) {
                            String[] values = data.getPredefinedValues();

                            if (data.getPredefinedValuesWithSorting() == null) {
                                Arrays.sort(values, String::compareTo);
                            }
                            if (companyCustomFieldItem.getRelationFieldId() == null) {
                                for (int m = 0; m < values.length; m++) {
                                    if (data.getFieldStringValue() != null && values[m] != null && values[m].trim().replace(" ", "").equals(data.getFieldStringValue().trim().replace(" ", ""))) {
                                        textField.setSelected(m);
                                    }
                                }
                            } else {
                                KpiSelect2 parentDD = (KpiSelect2) tbValues.get(customFieldsCode.get(companyCustomFieldItem.getRelationFieldId()));
                                SelectItem itemP = parentDD.getSelectedItem();
                                if (itemP == null) {
                                    continue;
                                } else {
                                    String selectedVal = itemP.getName();
                                    List<SelectItem> selectItemList = companyCustomFieldItem.getRelationItemsMap().get(selectedVal) != null ? companyCustomFieldItem.getRelationItemsMap().get(selectedVal) : new ArrayList<>();
                                    for (int m = 0; m < selectItemList.size(); m++) {
                                        String stringNameVal = selectItemList.get(m).getName();
                                        if (data.getFieldStringValue() != null && stringNameVal != null && stringNameVal.trim().replace(" ", "").equals(data.getFieldStringValue().trim().replace(" ", ""))) {
                                            textField.setSelected(selectItemList.get(m).getId());
                                        }
                                    }
                                }
                            }
                            if (customFieldsMap != null && customFieldsMap.get(companyCustomFieldItem.getColumnCode()) != null) {
                                List<CompanyCustomFieldItem> companyCustomFieldItemsList = customLogicFieldMap.get(companyCustomFieldItem.getColumnCode()) != null ? customLogicFieldMap.get(companyCustomFieldItem.getColumnCode()) : null;
                                if (companyCustomFieldItemsList != null && !companyCustomFieldItemsList.isEmpty()) {
                                    for (CompanyCustomFieldItem cfItem : companyCustomFieldItemsList) {
                                        if (companyCustomFieldItem.getFieldStringValue() != null && companyCustomFieldItem.getFieldStringValue().equals(cfItem.getCustomLogicValue())) {
                                            ignoreValidationList.remove(cfItem.getColumnCode());
                                        }
                                        ((Widget) tbValues.get(cfItem.getColumnCode())).setVisible(cfItem.getCustomLogicValue().equals(companyCustomFieldItem.getFieldStringValue()));
                                    }
                                }
                            }
                        }
                    } else if (UI_TYPE_CHECKBOX.equals(companyCustomFieldItem.getUiType())) {
                        KpiCheckBox[] checkbox = (KpiCheckBox[]) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getFieldStringValue() != null && data.getPredefinedValues() != null) {
                            String[] values = data.getPredefinedValues();
                            String[] fsvalues = data.getFieldStringValue().split("-:-");
                            for (String value : values) {
                                for (String fsvalue : fsvalues) {
                                    if (value.equals(fsvalue)) {
                                        for (KpiCheckBox aCheckbox : checkbox) {
                                            if (aCheckbox.getName().equals(fsvalue)) {
                                                aCheckbox.setChecked(true);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (UI_TYPE_RADIOBUTTON.equals(companyCustomFieldItem.getUiType())) {
                        RadioButton[] radiobutton = (RadioButton[]) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getFieldStringValue() != null && data.getPredefinedValues() != null) {
                            String[] values = data.getPredefinedValues();
                            String fsvalues = data.getFieldStringValue();
                            for (int k = 0; k < values.length; k++) {
                                if (values[k].equals(fsvalues)) {
                                    for (RadioButton aRadiobutton : radiobutton) {
                                        if (aRadiobutton.getText().equals(fsvalues)) {
                                            aRadiobutton.setValue(true);
                                            k = values.length;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (customFieldsMap != null && customFieldsMap.get(companyCustomFieldItem.getColumnCode()) != null) {
                                List<CompanyCustomFieldItem> companyCustomFieldItemList = customLogicFieldMap.get(companyCustomFieldItem.getColumnCode()) != null ? customLogicFieldMap.get(companyCustomFieldItem.getColumnCode()) : null;
                                if (companyCustomFieldItemList != null && !companyCustomFieldItemList.isEmpty()) {
                                    for (CompanyCustomFieldItem cfItem : companyCustomFieldItemList) {
                                        if (companyCustomFieldItem.getFieldStringValue() != null && companyCustomFieldItem.getFieldStringValue().equals(cfItem.getCustomLogicValue())) {
                                            ignoreValidationList.remove(cfItem.getColumnCode());
                                        }
                                        ((Widget) tbValues.get(cfItem.getColumnCode())).setVisible(cfItem.getCustomLogicValue().equals(companyCustomFieldItem.getFieldStringValue()));
                                    }
                                }
                            }
                        }
                    } else if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
                        DatePicker dateField = (DatePicker) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getFieldDateNonConvertedValue() != null) {
                            dateField.setDate(data.getFieldDateNonConvertedValue().getNonConvertedDate());
                        }
                    } else if (UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                        DateTimeWidget dateField = (DateTimeWidget) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getFieldDateNonConvertedValue() != null) {
                            dateField.setDateTime(data.getFieldDateNonConvertedValue().getNonConvertedDate());
                        }
                    } else if (UI_TYPE_FILE_UPLOAD_WIDGET.equals(companyCustomFieldItem.getUiType())) {
                        GeneralFileUpload fileUploadField = (GeneralFileUpload) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getFieldStringValue() != null) {
                            fileUploadField.setCustomFieldData(Double.valueOf(data.getFieldStringValue()).intValue(), data.getObjectId());
                        }
                    } else if (UI_TYPE_FILE_UPLOAD_ITEM.equals(companyCustomFieldItem.getUiType())) {
                        GeneralFileUploadItem fileUploadFieldItem = (GeneralFileUploadItem) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getFieldStringValue() != null) {
                            fileUploadFieldItem.setFiles(Double.valueOf(data.getFieldStringValue()).intValue(), data.getObjectId(), false);
                        }
                    } else if (UI_TYPE_PROFILE_IMAGE_WIDGET.equals(companyCustomFieldItem.getUiType())) {
                        ProfileImage profileImage = (ProfileImage) tbValues.get(companyCustomFieldItem.getColumnCode());
                        profileImage.setImageID(data.getProfielImageId());
                        profileImage.getImageById(data.getProfielImageId(), "Profile", "Image", true);
                    } else if (UI_TYPE_ENTITY_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                        DataListBox textField = (DataListBox) tbValues.get(companyCustomFieldItem.getColumnCode());
                        Integer id = null;
                        try {
                            id = Integer.valueOf(data.getFieldStringValue());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (id != null && companyCustomFieldItem.getQueryItems() != null) {
                            for (SelectItem selectItem : companyCustomFieldItem.getQueryItems()) {
                                if (selectItem.getId().equals(id)) {
                                    textField.setSelected(new SelectItem(id, selectItem.getName()));
                                    break;
                                }
                            }
                        }
                    } else if (TYPE_ENTITY_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                        EntityCustomFieldLookUp lookUpField = (EntityCustomFieldLookUp) tbValues.get(companyCustomFieldItem.getColumnCode());
                        Integer id = null;
                        try {
                            id = Integer.valueOf(data.getFieldStringValue());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (id != null && companyCustomFieldItem.getQueryItems() != null) {
                            for (SelectItem selectItem : companyCustomFieldItem.getQueryItems()) {
                                if (selectItem.getId().equals(id)) {
                                    lookUpField.setSelected(new SelectItem(id, selectItem.getName()));
                                    break;
                                }
                            }
                        }
                    } else if (UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                        TextArea textAreaField = (TextArea) tbValues.get(companyCustomFieldItem.getColumnCode());
                        textAreaField.setText(data.getFieldStringValue());
                    } else if (UI_TYPE_HTML_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                        KpiEditor htmlTextAreaField = (KpiEditor) tbValues.get(companyCustomFieldItem.getColumnCode());
                        htmlTextAreaField.setData(data.getFieldStringValue());
                    } else if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                        CustomFieldLookUp lookUpField = (CustomFieldLookUp) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getSelectedId() != null) {
                            lookUpField.addItem(new SelectItem(data.getSelectedId(), data.getFieldStringValue()));
                        }
                    } else if (UI_TYPE_CURRENCY.equals(companyCustomFieldItem.getUiType())) {
                        CurrencyWidget currency = (CurrencyWidget) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getSelectedId() != null) {
                            currency.setCurrency(data.getSelectedId());
                        }
                    } else if (UI_TYPE_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                        MultiSelectCustomFieldLookUp multiLookUp = (MultiSelectCustomFieldLookUp) tbValues.get(companyCustomFieldItem.getColumnCode());
                        if (data.getSelectItems() != null && !data.getSelectItems().isEmpty()) {
                            multiLookUp.setSelectedItems((ArrayList<SelectItem>) data.getSelectItems());
                        }
                    } else if (TYPE_ENTITY_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                        EntityCustomFieldMultiLookUp multiLookUp = (EntityCustomFieldMultiLookUp) tbValues.get(companyCustomFieldItem.getColumnCode());

                        if (data.getSelectItems() != null && !data.getSelectItems().isEmpty()) {
                            multiLookUp.setSelectedItems((ArrayList<SelectItem>) data.getSelectItems());
                        }
                    }
                }
            }
        }
    }

    HashMap<String, CompanyCustomFieldItem> customFieldsMap;

    private HashMap<String, CompanyCustomFieldItem> getCustomFieldsAsMap() {
        if (customFieldsMap == null || customFieldsMap.size() > 0) {
            customFieldsMap = new HashMap<>();
            if (hasCustomFields()) {
                for (CompanyCustomFieldItem item : companyCustomFieldItems) {
                    customFieldsMap.put(item.getColumnCode(), item);
                }
            }
        }
        return customFieldsMap;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldsValue() {
        ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();
        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
            if (tbValues.size() > 0) {
                for (CompanyCustomFieldItem companyCustomFieldItem : companyCustomFieldItems) {
                    if (tbValues.containsKey(companyCustomFieldItem.getColumnCode())) {
                        CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                        resultItem.setObjectId(companyCustomFieldItem.getObjectId());
                        resultItem.setDataType(companyCustomFieldItem.getDataType());
                        resultItem.setColumnCode(companyCustomFieldItem.getColumnCode());
                        resultItem.setFieldName(companyCustomFieldItem.getFieldName());
                        resultItem.setAliasName(companyCustomFieldItem.getAliasName());
                        resultItem.setFileUploadFieldId(companyCustomFieldItem.getFileUploadFieldId());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());
                        resultItem.setEntityCategoryName(companyCustomFieldItem.getEntityCategoryName());
                        resultItem.setPrefix(companyCustomFieldItem.getPrefix());
                        resultItem.setScale(companyCustomFieldItem.getScale());

                        CustomFieldWidget customFieldWidget = getCustomFieldValue(companyCustomFieldItem.getUiType(), companyCustomFieldItem.getColumnCode(), false, companyCustomFieldItem.isRequired());
                        Object customFieldValue = customFieldWidget.getValue();
                        if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
                            resultItem.setFieldDateNonConvertedValue(new DateNonConvertable((Date) customFieldValue));
                            resultItem.setFieldDateNonConvertedValue(customFieldValue != null ? new DateNonConvertable((Date) customFieldValue) : null);
                        } else if (UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                            resultItem.setFieldDateNonConvertedValue(new DateNonConvertable((Date) customFieldValue));
                            resultItem.setFieldDateNonConvertedValue(customFieldValue != null ? new DateNonConvertable((Date) customFieldValue) : null);
                        } else if (UI_TYPE_FILE_UPLOAD_WIDGET.equals(companyCustomFieldItem.getUiType())) {
                            resultItem.setAttachments((FileItem[]) customFieldValue);
                        } else if (UI_TYPE_FILE_UPLOAD_ITEM.equals(companyCustomFieldItem.getUiType())) {
                            resultItem.setAttachments((FileItem[]) customFieldValue);
                        } else if (UI_TYPE_PROFILE_IMAGE_WIDGET.equals(companyCustomFieldItem.getUiType())) {
                            resultItem.setProfielImageId((Integer) customFieldValue);
                            resultItem.setFieldStringValue(String.valueOf(customFieldValue));
                        } else if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                            if (customFieldWidget.getStringValue() != null) {
                                // Todo: need to add Position id 
                                if (companyCustomFieldItem.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.DEPARTMENT)
                                        || companyCustomFieldItem.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.POSITION))
                                    resultItem.setFieldStringValue((String) customFieldWidget.getValue());
                                else
                                    resultItem.setFieldStringValue(customFieldWidget.getStringValue());
                            }
                            if (customFieldValue != null) {
                                resultItem.setSelectedId(Integer.parseInt((String) customFieldValue));
                            }
                        } else if (UI_TYPE_CURRENCY.equals(companyCustomFieldItem.getUiType())) {
                            if (customFieldWidget.getStringValue() != null) {
                                resultItem.setFieldStringValue(customFieldWidget.getStringValue());
                            }
                            if (customFieldValue != null) {
                                resultItem.setSelectedId(Integer.parseInt((String) customFieldValue));
                            }
                        } else if (TYPE_ENTITY_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_MULTI_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                            if (customFieldWidget.getValue() != null) {
                                resultItem.setSelectItems((ArrayList<SelectItem>) customFieldWidget.getValue());
                            }
                        } else if ((DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType())
                                && (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType())))
                                || UI_TYPE_PERCENTAGE.equals(companyCustomFieldItem.getUiType())) {
                            if (customFieldWidget.getValue() != null) {
                                BigDecimal bigDecimal = parsePriceToBigDecimal((String) customFieldWidget.getValue());
                                resultItem.setFieldStringValue(bigDecimal.toString());

                            }
                        } else if (UI_TYPE_HTML_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                            resultItem.setFieldStringValue((String) customFieldValue != null && !((String) customFieldValue).isEmpty() ? Utils.encrypt((String) customFieldValue) : "");
                        } else if (UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_RADIOBUTTON.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_CHECKBOX.equals(companyCustomFieldItem.getUiType())) {
                            resultItem.setFieldStringValue((String) getDefultValueOfField(customFieldValue, companyCustomFieldItem));
                        } else {
                            resultItem.setFieldStringValue((String) customFieldValue);
                        }
                        resultItemList.add(resultItem);
                    }
                }
            }
        }
        return resultItemList;
    }

    private Object getDefultValueOfField(Object customFieldValue, CompanyCustomFieldItem fieldItem) {
        if (!(customFieldValue instanceof String)) {
            return customFieldValue;
        }
        if (fieldItem.getLocalization() != null && fieldItem.getLocalization().getChildren() != null && fieldItem.getUserLocale() != null) {
            switch (fieldItem.getUserLocale()) {
                case "en":
                    for (CustomFormLocalization localization : fieldItem.getLocalization().getChildren()) {
                        if (customFieldValue.equals(localization.getEnglishName())) {
                            customFieldValue = localization.getDefaultName();
                        }
                    }
                    break;
                case "ar":
                    for (CustomFormLocalization localization : fieldItem.getLocalization().getChildren()) {
                        if (customFieldValue.equals(localization.getArabicName())) {
                            customFieldValue = localization.getDefaultName();
                        }
                    }
                    break;
                case "ru":
                    for (CustomFormLocalization localization : fieldItem.getLocalization().getChildren()) {
                        if (customFieldValue.equals(localization.getRussianName())) {
                            customFieldValue = localization.getDefaultName();
                        }
                    }
                    break;
                case "uz":
                    for (CustomFormLocalization localization : fieldItem.getLocalization().getChildren()) {
                        if (customFieldValue.equals(localization.getUzbekName())) {
                            customFieldValue = localization.getDefaultName();
                        }
                    }
                    break;
            }
            return customFieldValue;
        } else {
            return customFieldValue;
        }
    }

    public int validateCustomFields() {
        int errors = 0;
        StringBuilder validationMessages = new StringBuilder();
        boolean hasMessage = false;

        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
            for (CompanyCustomFieldItem customField : companyCustomFieldItems) {
                String columnCode = customField.getColumnCode();
                Widget widget = null;
                if (tbValues.containsKey(columnCode) && customField.isRequired() && !ignoreValidationList.contains(columnCode)) {
                    if (DATA_TYPE_FILE_UPLOAD.equals(customField.getDataType()) && !(((FileItem[]) getCustomFieldValue(customField.getUiType(), columnCode, true, customField.isRequired()).getValue()).length > 0)) {
                        errors++;
                        widget = getCustomFieldValue(customField.getUiType(), columnCode, true, true).getWidget();
                        markAsError(widget, true);
                    } else if (UI_TYPE_TEXTBOX_EMAIL.equals(customField.getUiType()) &&
                            (getCustomFieldValue(customField.getUiType(), columnCode, true, true).getValue() == null
                                    || (getCustomFieldValue(customField.getUiType(), columnCode, true, true).getValue() != null &&
                                    !Validation.validateEmailRequired((TextBoxBase) getCustomFieldValue(customField.getUiType(), columnCode, true, true).getWidget())))) {
                        widget = getCustomFieldValue(customField.getUiType(), columnCode, true, true).getWidget();
                        markAsError(widget, true);
                        errors++;
                    } else if (UI_TYPE_URL.equals(customField.getUiType()) &&
                            (getCustomFieldValue(customField.getUiType(), columnCode, true, true).getValue() == null
                                    || (getCustomFieldValue(customField.getUiType(), columnCode, true, true).getValue() != null &&
                                    !Validation.validateUrl((TextBoxBase) getCustomFieldValue(customField.getUiType(), columnCode, true, true).getWidget(), null)))) {
                        widget = getCustomFieldValue(customField.getUiType(), columnCode, true, true).getWidget();
                        markAsError(widget, true);
                        errors++;
                    } else if (UI_TYPE_DROPDOWN.equals(customField.getUiType()) &&
                            getCustomFieldValue(customField.getUiType(), columnCode, true, true).getValue() != null &&
                            getCustomFieldValue(customField.getUiType(), columnCode, true, true).getValue().equals(wfmStrings.pleaseSelect())) {
                        widget = getCustomFieldValue(customField.getUiType(), columnCode, true, true).getWidget();
                        markAsError(widget, true);
                        errors++;
                    } else if (UI_TYPE_RADIOBUTTON.equals(customField.getUiType()) &&
                            getCustomFieldValue(customField.getUiType(), columnCode, true, true).getValue() != null &&
                            getCustomFieldValue(customField.getUiType(), columnCode, true, true).getValue().equals(wfmStrings.pleaseSelect())) {
                        widget = getCustomFieldValue(customField.getUiType(), columnCode, true, true).getWidget();
                        markAsError(widget, true);
                        errors++;
                    } else if (getCustomFieldValue(customField.getUiType(), columnCode, true, true).getValue() == null) {
                        errors++;
                    }
                }

                if (!customField.isRequired() && UI_TYPE_TEXTBOX_EMAIL.equals(customField.getUiType())) {
                    if (getCustomFieldValue(customField.getUiType(), columnCode, true, false).getValue() != null && !Validation.validateEmailRequired((TextBoxBase) getCustomFieldValue(customField.getUiType(), columnCode, true, customField.isRequired()).getWidget())) {
                        errors++;
                        widget = getCustomFieldValue(customField.getUiType(), columnCode, true, customField.isRequired()).getWidget();
                        markAsError(widget, true);
                    }
                }

                if (!customField.isRequired() && UI_TYPE_URL.equals(customField.getUiType())) {
                    if (getCustomFieldValue(customField.getUiType(), columnCode, true, false).getValue() != null && !Validation.validateUrl((TextBoxBase) getCustomFieldValue(customField.getUiType(), columnCode, true, customField.isRequired()).getWidget(), null)) {
                        errors++;
                        widget = getCustomFieldValue(customField.getUiType(), columnCode, true, customField.isRequired()).getWidget();
                        markAsError(widget, true);
                    }
                }
                if (UI_TYPE_LOOKUP.equals(customField.getUiType())) {
                    CustomFieldLookUp customFieldLookUp = (CustomFieldLookUp) tbValues.get(columnCode);
                    widget = customFieldLookUp;
                    errors += markAsError(customFieldLookUp, customFieldLookUp.getSelectedItemID() == null && customFieldLookUp.getText().trim() != null && customFieldLookUp.getText().trim().length() > 0 && !LookUp.wfmStrings.searchTypeMessage().equals(customFieldLookUp.getText()));
                }
                if (customField.isRequired() && widget != null) {
                    validationObjects.put(columnCode, widget);
                }

                if (UI_TYPE_TEXTBOX.equals(customField.getUiType()) && DATA_TYPE_NUMBER.equals(customField.getDataType())) {
                    TextBox number = ((TextBox) tbValues.get(columnCode));
                    if (number == null) {
                        continue;
                    }
                    if (customField.getNumberMinValue() != null && parsePriceToBigDecimal(number.getText()).doubleValue() < customField.getNumberMinValue()) {
                        widget = getCustomFieldValue(customField.getUiType(), columnCode, true, true).getWidget();
                        markAsError(widget, true);
                        errors++;
                    } else {
                        if (customField.isRequired() && getCustomFieldValue(customField.getUiType(), columnCode, true, true).getValue() == null) {
                            widget = getCustomFieldValue(customField.getUiType(), columnCode, true, true).getWidget();
                            markAsError(widget, true);
                            errors++;
                        }
                    }
                }


                if (customField.getValidations() != null) {
                    customField.getValidations();
                    for (CustomFieldSettingItem validationItem : customField.getValidations()) {
                        //custom field date validation
                        if (ValidationType.BeforeDate.getId().equals(validationItem.getValidationCodeID())) {
                            CustomFieldWidget cfField = getCustomFieldValue(customField.getUiType(), customField.getColumnCode(), false, customField.isRequired());
                            CustomFieldWidget joinedField = getCustomFieldValue(validationItem.getJoinedColumnUIType(), validationItem.getJoinedColumnCode(), false, customField.isRequired());

                            Date cfFieldValue = (Date) cfField.getValue();
                            Date joinedFieldValue = (Date) joinedField.getValue();
                            if (cfFieldValue != null && joinedFieldValue != null && cfFieldValue.after(joinedFieldValue)) {
                                errors++;
                                markAsError(cfField.getWidget(), true);
                                validationObjects.put(customField.getColumnCode(), cfField.getWidget());
                                if (!validationMessages.toString().isEmpty()) {
                                    validationMessages.append(", ");
                                }
                                validationMessages.append(customField.getFieldName()).append(" can not be later than ").append(validationItem.getJoinedFieldName());
                            }
                        }

                        //custom field after required validation
                        if (ValidationType.AfterRequired.getId().equals(validationItem.getValidationCodeID())) {
                            CustomFieldWidget cfField = getCustomFieldValue(customField.getUiType(), customField.getColumnCode(), false, customField.isRequired());
                            CustomFieldWidget joinedField = getCustomFieldValue(validationItem.getJoinedColumnUIType(), validationItem.getJoinedColumnCode(), false, customField.isRequired());

                            Object cfFieldValue = cfField.getValue();
                            Object joinedFieldValue = joinedField.getValue();
                            if (joinedFieldValue != null && cfFieldValue == null) {
                                errors++;
                                markAsError(cfField.getWidget(), true);
                                validationObjects.put(customField.getColumnCode(), cfField.getWidget());
                            }
                        }

                        //custom field "is email" field validation
                        if (ValidationType.IsEmail.getId().equals(validationItem.getValidationCodeID())) {
                            CustomFieldWidget cfField = getCustomFieldValue(customField.getUiType(), customField.getColumnCode(), false, customField.isRequired());
                            String cfFieldValue = (String) cfField.getValue();
                            if (cfFieldValue != null && Utils.validateEmail(cfFieldValue, false)) {
                                errors++;
                                markAsError(cfField.getWidget(), true);
                                validationObjects.put(customField.getColumnCode(), cfField.getWidget());
                                if (!validationMessages.toString().isEmpty()) {
                                    validationMessages.append(", ");
                                }
                                validationMessages.append(customField.getFieldName()).append(" must be email format.");
                            }
                        }
                    }
                }
            }
        }

        if (errors > 0 && !validationMessages.toString().isEmpty()) {
            Info.show(validationMessages.toString(), Info.Type.WARNING);
        }
        return errors;
    }

    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldItems() {
        return companyCustomFieldItems;
    }

    public void setCompanyCustomFieldItems(ArrayList<CompanyCustomFieldItem> companyCustomFieldItems) {
        this.companyCustomFieldItems = companyCustomFieldItems;
        customLogicFieldMap = companyCustomFieldItems
                .stream()
                .filter(cf -> cf.getCustomLogicField() != null)
                .collect(Collectors.groupingBy(
                        cf -> cf.getCustomLogicField().getDescription(),
                        Collectors.toList()
                ));
    }

    public void drawCustomFields(CustomForm customForm, Integer objectId, boolean... viewMode) {
        drawCustomFields(customForm, objectId, null, viewMode);
    }

    public void drawCustomFields(CustomForm customForm, Integer objectId, BaseQuickAddView quickAdd, boolean... viewMode) {
        boolean isViewMode = (viewMode != null && viewMode.length > 0 && viewMode[0]);
        if (getCompanyCustomFieldItems() != null && getCompanyCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem customField : getCompanyCustomFieldItems()) {
                Widget widget = null;
                customField.setFileUploadFieldId(customField.getObjectId());
                customField.setObjectId(null);
                if (isViewMode) {
                    switch (customField.getUiType()) {
                        case UI_TYPE_FILE_UPLOAD_WIDGET:
                            GeneralFileUpload fileUploadField = new GeneralFileUpload(F_CUSTOM_FIELD_ITEM, objectId, objectId);
                            tbValues.put(customField.getColumnCode(), fileUploadField);
                            widget = fileUploadField;
                            break;
                        case UI_TYPE_FILE_UPLOAD_ITEM:
                            GeneralFileUploadItem fileUploadFieldItem = new GeneralFileUploadItem(F_CUSTOM_FIELD_ITEM);
                            tbValues.put(customField.getColumnCode(), fileUploadFieldItem);
                            widget = fileUploadFieldItem;
                            break;
                        case UI_TYPE_PROFILE_IMAGE_WIDGET:
                            ProfileImage profileImage = new ProfileImage();
                            profileImage.getImageById(customField.getProfielImageId(), "Profile", "Image", false);
                            tbValues.put(customField.getColumnCode(), profileImage);
                            widget = profileImage;
                            break;
                        case UI_TYPE_TEXTAREA:
                            TextArea textAreaField = new TextArea();
                            tbValues.put(customField.getColumnCode(), textAreaField);
                            widget = textAreaField;
                            break;
                        case UI_TYPE_HTML_TEXTAREA:
                            KpiEditor htmlTextAreaField = new KpiEditor(false, true, "CUSTOM_FORM");
                            if (customField.getMinHeight() != null) {
                                htmlTextAreaField.getRichEditor().setHeight(("".equals(customField.getMinHeight()) ? "0" : customField.getMinHeight()) + "px");
                            }
                            tbValues.put(customField.getColumnCode(), htmlTextAreaField);
                            widget = htmlTextAreaField;
                            break;
                        case UI_TYPE_COMMITBOX:
                            NoteWidgetCustomField commentNoteWidget = new NoteWidgetCustomField(objectId, customField.getForm(), customField);
                            commentNoteWidget.setCustomFieldItem(customField);
                            tbValues.put(customField.getColumnCode(), commentNoteWidget);
                            widget = commentNoteWidget;
                            break;
                        default:
                            widget = new HTML();
                            widget.addStyleName("width250");
                            tbValues.put(customField.getColumnCode(), widget);
                            break;
                    }
                } else {
                    if (UI_TYPE_TEXTBOX.equals(customField.getUiType())) {
                        TextBox textField = new TextBox();
                        textField.addStyleName("width250");
                        textField.setEnabled(!customField.isDisabled());
                        if (DATA_TYPE_NUMBER.equals(customField.getDataType())) {
                            textField.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                            Integer calcScale = customField.getScale() != null ? customField.getScale() : Utils.getAccountingCalculationScale();
                            Validation.addNumericKeyboardListener(textField, calcScale != null ? calcScale : 2, customField != null && customField.getNumberMinValue() != null && customField.getNumberMinValue() >= 0);
                            textField.addFocusListener(new FocusListenerAdapter() {
                                public void onLostFocus(Widget widget) {
                                    String format = (textField.getText().indexOf(':') == -1 ? textField.getText() : getHourValue(textField.getText()));
                                    BigDecimal price = parsePriceToBigDecimal(format);
                                    textField.setText(formatCustomPrice(price, customField.getScale()));
                                }
                            });
                        }
                        tbValues.put(customField.getColumnCode(), textField);
                        widget = textField;
                    }
                    if (UI_TYPE_PERCENTAGE.equals(customField.getUiType())) {
                        PercentageWidget percentageWidget = new PercentageWidget();
                        percentageWidget.setEnabled(!customField.isDisabled());
                        percentageWidget.getTextBox().setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                        if (percentageWidget.getTextBox().getText() != null) {
                            percentageWidget.getTextBox().addFocusListener(new FocusListenerAdapter() {
                                public void onLostFocus(Widget widget) {
                                    String format = (percentageWidget.getTextBox().getText().indexOf(':') == -1 ? percentageWidget.getTextBox().getText() : getHourValue(percentageWidget.getTextBox().getText()));
                                    BigDecimal price = parsePriceToBigDecimal(format);
                                    percentageWidget.getTextBox().setText(formatCustomPrice(price, customField.getScale()));
                                }
                            });
                        }
                        tbValues.put(customField.getColumnCode(), percentageWidget);
                        widget = percentageWidget;
                    }
                    if (UI_TYPE_URL.equals(customField.getUiType())) {
                        TextBox urlWidget = new TextBox();
                        urlWidget.setEnabled(!customField.isDisabled());
                        urlWidget.addStyleName("width250");
                        tbValues.put(customField.getColumnCode(), urlWidget);
                        widget = urlWidget;
                    }
                    if (UI_TYPE_TEXTBOX_EMAIL.equals(customField.getUiType())) {
                        TextBox textField = new TextBox();
                        textField.setEnabled(!customField.isDisabled());
                        textField.addStyleName("width250");
                        tbValues.put(customField.getColumnCode(), textField);
                        widget = textField;
                    }
                    if (UI_TYPE_DROPDOWN.equals(customField.getUiType())) {
                        customFieldsCode.put(customField.getEntityId(), customField.getColumnCode());
                        KpiSelect2 numberField = new KpiSelect2();
                        numberField.setEnabled(!customField.isDisabled());
                        if (customField.getRelationFieldId() != null) {
                            numberField.setEnabled(false);
                        }
                        numberField.addStyleName("width250");
                        if (customField.getPredefinedValues() != null) {
                            AtomicInteger counter = new AtomicInteger(0);
                            String[] values = customField.getPredefinedValues();

                            if (customField.getPredefinedValuesWithSorting() == null) {
                                Arrays.sort(values, String::compareTo);
                            }
                            ArrayList<SelectItem> v = Arrays.stream(values)
                                    .map(item -> new SelectItem(counter.getAndIncrement(), item))
                                    .collect(Collectors.toCollection(ArrayList::new));

                            numberField.setItems(v);
                            numberField.onValueChangeHandler(event -> {
                                if (customLogicFieldMap.get(customField.getColumnCode()) != null) {
                                    List<CompanyCustomFieldItem> companyCustomFieldItemsList = customLogicFieldMap.get(customField.getColumnCode()) != null ? customLogicFieldMap.get(customField.getColumnCode()) : null;
                                    if (companyCustomFieldItemsList != null && !companyCustomFieldItemsList.isEmpty()) {
                                        for (CompanyCustomFieldItem cfItem : companyCustomFieldItemsList) {
                                            if (numberField.getSelectedItem().getName().equals(cfItem.getCustomLogicValue())) {
                                                ignoreValidationList.remove(cfItem.getColumnCode());
                                            } else {
                                                ignoreValidationList.add(cfItem.getColumnCode());
                                            }
                                            ((Widget) tbValues.get(cfItem.getColumnCode())).setVisible(cfItem.getCustomLogicValue().equals(numberField.getSelectedItem().getName()));
                                            customForm.showTitle(cfItem.getColumnCode(), cfItem.getCustomLogicValue().equals(numberField.getSelectedItem().getName()));
                                        }
                                    }
                                }
                            });
                        }
                        tbValues.put(customField.getColumnCode(), numberField);
                        if (customForm != null) {
                            customForm.addPredefinedValues(customField.getColumnCode(), numberField.getItems());
                        }
                        widget = numberField;
                    }
                    if (UI_TYPE_CHECKBOX.equals(customField.getUiType())) {
                        if (customField.getPredefinedValues() != null) {
                            UL ul = new UL();
                            ul.addStyleName("customFieldUL");
                            int count = 0;
                            String[] s = customField.getPredefinedValues();
                            KpiCheckBox[] checkbox = new KpiCheckBox[s.length];
                            do {
                                checkbox[count] = new KpiCheckBox("     " + s[count]);
                                checkbox[count].setName(s[count]);
                                UL.LI li = new UL.LI();
                                li.addStyleName("customFieldLI");
                                li.add(checkbox[count]);
                                ul.add(li);
                                count++;
                            } while (count < s.length);
                            tbValues.put(customField.getColumnCode(), checkbox);
                            widget = ul;
                        }
                    }
                    if (UI_TYPE_RADIOBUTTON.equals(customField.getUiType())) {
                        UL ul = new UL();
                        ul.addStyleName("customFieldUL");
                        int count = 0;
                        String[] s = customField.getPredefinedValues() != null ? customField.getPredefinedValues() : new String[0];
                        RadioButton[] radioButton = new RadioButton[s.length];
                        do {
                            radioButton[count] = new KpiRadioButton(s[count]);
                            radioButton[count].setText(s[count]);
                            radioButton[count].setName("rb_" + customField.getColumnCode() + "");
                            UL.LI li = new UL.LI();
                            li.addStyleName("customFieldLI");
                            li.add(radioButton[count]);
                            ul.add(li);
                            count++;
                        } while (count < s.length);
                        tbValues.put(customField.getColumnCode(), radioButton);
                        widget = ul;
                    }
                    if (customField.getUiType().equals(UI_TYPE_DATEPICKER)) {
                        DatePicker dateField = new DatePicker();
                        dateField.setEnabled(!customField.isDisabled());
                        dateField.addStyleName("width250");
                        tbValues.put(customField.getColumnCode(), dateField);
                        widget = dateField;
                    }
                    if (UI_TYPE_DATEPICKER_TIME.equals(customField.getUiType())) {
                        DateTimeWidget dateField = new DateTimeWidget(28);
                        dateField.setEnabled(!customField.isDisabled());
                        tbValues.put(customField.getColumnCode(), dateField);
                        widget = dateField;

                    }
                    if (customField.getUiType().equals(UI_TYPE_FILE_UPLOAD_WIDGET)) {
                        GeneralFileUpload fileUploadField = new GeneralFileUpload(F_CUSTOM_FIELD_ITEM, objectId, objectId);
                        tbValues.put(customField.getColumnCode(), fileUploadField);
                        widget = fileUploadField;
                    }
                    if (customField.getUiType().equals(UI_TYPE_FILE_UPLOAD_ITEM)) {
                        GeneralFileUploadItem fileUploadFieldItem = new GeneralFileUploadItem(F_CUSTOM_FIELD_ITEM);
                        tbValues.put(customField.getColumnCode(), fileUploadFieldItem);
                        widget = fileUploadFieldItem;
                        widget.addStyleName("gwt-FileUpload-container");
                    }
                    if (customField.getUiType().equals(UI_TYPE_PROFILE_IMAGE_WIDGET)) {
                        ProfileImage profileImage = new ProfileImage();
                        profileImage.getImageById(customField.getProfielImageId(), "Profile", "Image", true);
                        tbValues.put(customField.getColumnCode(), profileImage);
                        widget = profileImage;
                    }
                    if (UI_TYPE_ENTITY_DROPDOWN.equals(customField.getUiType())) {
                        DataListBox numberField = new DataListBox();
                        numberField.addStyleName("width250");
                        if (customField.getQueryItems() != null) {
                            numberField.setItems(customField.getQueryItems());
                        }

                        tbValues.put(customField.getColumnCode(), numberField);
                        widget = numberField;
                    }
                    if (TYPE_ENTITY_LOOKUP.equals(customField.getUiType())) {
                        EntityCustomFieldLookUp entityCustomFieldLookUp = new EntityCustomFieldLookUp(customField.getQuery());
                        entityCustomFieldLookUp.setWidth("150");

                        tbValues.put(customField.getColumnCode(), entityCustomFieldLookUp);
                        widget = entityCustomFieldLookUp;
                    }
                    if (TYPE_ENTITY_MULTI_LOOKUP.equals(customField.getUiType())) {
                        EntityCustomFieldMultiLookUp entityCustomFieldMultiLookUp = new EntityCustomFieldMultiLookUp(customField.getQuery());
                        entityCustomFieldMultiLookUp.setWidth("150");
                        tbValues.put(customField.getColumnCode(), entityCustomFieldMultiLookUp);
                        widget = entityCustomFieldMultiLookUp;
                    }
                    if (customField.getUiType().equals(UI_TYPE_TEXTAREA)) {
                        TextArea textAreaField = new TextArea();
                        textAreaField.setEnabled(!customField.isDisabled());
                        tbValues.put(customField.getColumnCode(), textAreaField);
                        widget = textAreaField;
                    }
                    if (customField.getUiType().equals(UI_TYPE_HTML_TEXTAREA)) {
                        KpiEditor htmlTextAreaField = new KpiEditor();
                        if (!Utils.isNullOrEmpty(customField.getMinHeight())) {
                            htmlTextAreaField.getRichEditor().setHeight(customField.getMinHeight() + "px");
                        }
                        tbValues.put(customField.getColumnCode(), htmlTextAreaField);
                        widget = htmlTextAreaField;
                    }
                    if (UI_TYPE_LOOKUP.equals(customField.getUiType())) {
                        CustomFieldLookUp customFieldLookUp = new CustomFieldLookUp(customField);
                        customFieldLookUp.getSuggestBox().addSelectionHandler(e -> {
                            lookUpSelectionHandler(customField, customFieldLookUp);
                        });
                        customFieldLookUp.setWidth("150");
                        customFieldLookUp.setEnabled(!customField.isDisabled());
                        tbValues.put(customField.getColumnCode(), customFieldLookUp);
                        widget = customFieldLookUp;
                    }
                    if (UI_TYPE_AUTONUMBER.equals(customField.getUiType())) {
                        AutoNumberCustomField autoNumber = new AutoNumberCustomField(customField, objectId != null);
                        tbValues.put(customField.getColumnCode(), autoNumber);
                        widget = autoNumber;
                    }
                    if (UI_TYPE_CURRENCY.equals(customField.getUiType())) {
                        CurrencyWidget currencyWidget = new CurrencyWidget(true);
                        currencyWidget.setEnabled(!customField.isDisabled());
                        tbValues.put(customField.getColumnCode(), currencyWidget);
                        widget = currencyWidget;
                    }
                    if (UI_TYPE_MULTI_LOOKUP.equals(customField.getUiType())) {
                        MultiSelectCustomFieldLookUp multiLookup = new MultiSelectCustomFieldLookUp(customField);
                        multiLookup.setWidth("150");
                        tbValues.put(customField.getColumnCode(), multiLookup);
                        widget = multiLookup;
                    }
                    if (UI_TYPE_COMMITBOX.equals(customField.getUiType())) {
                        NoteWidgetCustomField commentNoteWidget = new NoteWidgetCustomField(objectId, customField.getForm(), customField);
                        commentNoteWidget.setCustomFieldItem(customField);
                        tbValues.put(customField.getColumnCode(), commentNoteWidget);
                        widget = commentNoteWidget;
                    }
                }
                if (widget != null) {
                    String className = "";
                    if (customField.getFieldName() != null && customField.getFieldName().length() > 0) {
                        className += " cf_" + customField.getFieldName().replaceAll("[^a-zA-Z]+", "_").toLowerCase();
                    }
                    widget.addStyleName(className);
                    if (customField.getCustomLogicField() != null && !isViewMode) {
                        ignoreValidationList.add(customField.getColumnCode());
                        widget.setVisible(false);
                    }
                    if (quickAdd != null) {
                        quickAdd.addField(customField.getColumnCode(), widget, customField.getUiType().equals(UI_TYPE_FILE_UPLOAD_WIDGET) ? null : customField.getFieldName());
                    } else {
                        customForm.addField(customField.getColumnCode(), widget, customField.getUiType().equals(UI_TYPE_FILE_UPLOAD_WIDGET) ? null : customField.getFieldName() + (customField.isRequired() && (viewMode == null || viewMode.length == 0 || !viewMode[0]) ? "<em class='redTitle'>*</em>:" : ":"), false, false, customField.getCustomLogicField() != null && !isViewMode);
                    }
                }
            }
            if (!isViewMode) {
                for (CompanyCustomFieldItem customField : getCompanyCustomFieldItems()) {
                    Integer relationId = customField.getRelationFieldId();
                    if (UI_TYPE_DROPDOWN.equals(customField.getUiType()) && relationId != null) {
                        String parentColumnCode = customFieldsCode.get(relationId);
                        KpiSelect2 parent = (KpiSelect2) tbValues.get(parentColumnCode);
                        KpiSelect2 child = (KpiSelect2) tbValues.get(customField.getColumnCode());
                        if (parent != null && child != null) {
                            parent.onValueChangeHandler(event -> {
                                if ((parent.getSelectedItem() != null) && (parent.getSelectedItem().getId() != -1)) {
                                    ArrayList<SelectItem> list = customField.getRelationItemsMap() != null ? customField.getRelationItemsMap().get(parent.getSelectedItem().getName()) : new ArrayList<>();
                                    child.clear();
                                    child.setItems(list);
                                    child.setEnabled(true);
                                } else {
                                    child.setEnabled(false);
                                }
                            });
                            tbValues.put(customField.getColumnCode(), child);

                            tbValues.put(parentColumnCode, parent);
                        }
                    }
                }
            }
        }
        if (customForm != null) {
            ignoreValidationList.addAll(customForm.getIgnoreValidation());
        }
    }

    private CustomFieldWidget getCustomFieldValue(String UIType, String columnCode, boolean isWrong, boolean isRequired) {
        Widget widget = null;
        Object value = null;
        String stringValue = null;
        getCustomFieldsAsMap();
        if (UI_TYPE_TEXTBOX.equals(UIType)) {
            widget = (TextBox) tbValues.get(columnCode);
            value = ((TextBox) tbValues.get(columnCode)).getText();
            stringValue = ((TextBox) tbValues.get(columnCode)).getValue();
            Integer lenth;
            String defaultValue = ((TextBox) tbValues.get(columnCode)).getText();
            lenth = ((String) value).length();

            if (defaultValue == null || defaultValue.isEmpty()) {
                value = null;
            } else if (customFieldsMap.get(columnCode).getMinChar() != null && !customFieldsMap.get(columnCode).getMinChar().equals("")) {
                if (DATA_TYPE_NUMBER.equalsIgnoreCase(customFieldsMap.get(columnCode).getDataType())) {
                    lenth = String.valueOf(parsePriceToBigDecimal(defaultValue).longValue()).length();
                }
                if (lenth != Integer.parseInt(customFieldsMap.get(columnCode).getMinChar())) {
                    Info.warn(wfmMessages.allowedCharLimit(customFieldsMap.get(columnCode).getFieldName(), customFieldsMap.get(columnCode).getMinChar()));
                    value = null;
                }
            }
        } else if (UI_TYPE_PERCENTAGE.equals(UIType)) {
            widget = (PercentageWidget) tbValues.get(columnCode);
            value = ((PercentageWidget) tbValues.get(columnCode)).getText();
            String defaultValue = ((PercentageWidget) tbValues.get(columnCode)).getText();
            if (defaultValue == null || defaultValue.isEmpty()) {
                value = null;
            } else if (customFieldsMap.get(columnCode).getMinChar() != null && !customFieldsMap.get(columnCode).getMinChar().equals("") && defaultValue.trim().length() != Integer.parseInt(customFieldsMap.get(columnCode).getMinChar())) {
                Info.warn(wfmMessages.allowedCharLimit(customFieldsMap.get(columnCode).getFieldName(), customFieldsMap.get(columnCode).getMinChar()));
                value = null;
            }
        } else if (UI_TYPE_AUTONUMBER.equals(UIType)) {
            widget = (AutoNumberCustomField) tbValues.get(columnCode);
            value = ((AutoNumberCustomField) widget).getText();
            value = String.valueOf(value);
            if (value != null && ((String) value).isEmpty()) {
                value = null;
            } else if (customFieldsMap.get(columnCode).getMinChar() != null && !customFieldsMap.get(columnCode).getMinChar().equals("") && ((AutoNumberCustomField) widget).getText().trim().length() != Integer.parseInt(customFieldsMap.get(columnCode).getMinChar())) {
                Info.warn(wfmMessages.allowedCharLimit(customFieldsMap.get(columnCode).getFieldName(), customFieldsMap.get(columnCode).getMinChar()));
                value = null;
            }
        } else if (UI_TYPE_CURRENCY.equals(UIType)) {
            widget = (CurrencyWidget) tbValues.get(columnCode);
            value = ((CurrencyWidget) widget).getCurrencyID();
            stringValue = ((CurrencyWidget) widget).getCurrencyName();

            value = String.valueOf(value);
            if (value != null && ((String) value).isEmpty()) {
                value = null;
            }
        } else if (UI_TYPE_TEXTBOX_EMAIL.equals(UIType)) {
            widget = (TextBox) tbValues.get(columnCode);
            value = ((TextBox) tbValues.get(columnCode)).getText();

            if (((TextBox) tbValues.get(columnCode)).getText() == null || ((TextBox) tbValues.get(columnCode)).getText().isEmpty()) {
                value = null;
            } else if (customFieldsMap.get(columnCode).getMinChar() != null && !customFieldsMap.get(columnCode).getMinChar().equals("") && ((TextBox) tbValues.get(columnCode)).getText().trim().length() != Integer.parseInt(customFieldsMap.get(columnCode).getMinChar())) {
                Info.warn(wfmMessages.allowedCharLimit(customFieldsMap.get(columnCode).getFieldName(), customFieldsMap.get(columnCode).getMinChar()));
                value = null;
            }
        } else if (UI_TYPE_URL.equals(UIType)) {
            widget = (TextBox) tbValues.get(columnCode);
            value = ((TextBox) tbValues.get(columnCode)).getText();

            if (((TextBox) tbValues.get(columnCode)).getText() == null || ((TextBox) tbValues.get(columnCode)).getText().isEmpty()) {
                value = null;
            } else if (customFieldsMap.get(columnCode).getMinChar() != null && !customFieldsMap.get(columnCode).getMinChar().equals("") && ((TextBox) tbValues.get(columnCode)).getText().trim().length() != Integer.parseInt(customFieldsMap.get(columnCode).getMinChar())) {
                Info.warn(wfmMessages.allowedCharLimit(customFieldsMap.get(columnCode).getFieldName(), customFieldsMap.get(columnCode).getMinChar()));
                value = null;
            }
        } else if (UI_TYPE_DROPDOWN.equals(UIType)) {
            widget = (KpiSelect2) tbValues.get(columnCode);
            if (((KpiSelect2) tbValues.get(columnCode)).getSelectedItem() != null) {
                value = ((KpiSelect2) tbValues.get(columnCode)).getSelectedItem().getName();
            } else {
                value = null;
            }
        } else if (UI_TYPE_CHECKBOX.equals(UIType)) {
            KpiCheckBox[] checkboxes = (KpiCheckBox[]) tbValues.get(columnCode);
            StringBuilder values = new StringBuilder();
            if (checkboxes.length > 0) {
                for (KpiCheckBox checkboxe1 : checkboxes) {
                    if (checkboxe1.getValue()) {
                        if (!values.toString().isEmpty()) {
                            values.append("-:-");
                        }

                        values.append(checkboxe1.getName());
                    }
                }
                if (values.length() == 0) {
                    for (KpiCheckBox checkboxe : checkboxes) {
                        markAsError(checkboxe, isWrong);
                        validationObjects.put(columnCode, checkboxe);
                    }
                }
                value = values.toString();
                if (values.toString().isEmpty()) {
                    value = null;
                }
            }
        } else if (UI_TYPE_RADIOBUTTON.equals(UIType)) {
            RadioButton[] radiobutton = (RadioButton[]) tbValues.get(columnCode);
            String values = null;
            for (RadioButton aRadiobutton : radiobutton) {
                if (aRadiobutton.getValue()) {
                    values = aRadiobutton.getText();
                    break;
                }
            }
            if (values == null) {
                for (RadioButton aRadiobutton : radiobutton) {
                    markAsError(aRadiobutton, isWrong);
                    validationObjects.put(columnCode, aRadiobutton);
                }
            }
            value = values;
        } else if (UI_TYPE_DATEPICKER.equals(UIType)) {
            widget = ((DatePicker) tbValues.get(columnCode));
            value = ((DatePicker) tbValues.get(columnCode)).getDate();
        } else if (UI_TYPE_DATEPICKER_TIME.equals(UIType)) {
            widget = ((DateTimeWidget) tbValues.get(columnCode));
            value = ((DateTimeWidget) tbValues.get(columnCode)).getDateTime();
        } else if (UI_TYPE_FILE_UPLOAD_WIDGET.equals(UIType)) {
            widget = ((GeneralFileUpload) tbValues.get(columnCode));
            value = ((GeneralFileUpload) tbValues.get(columnCode)).getAttachedFiles1();
        } else if (UI_TYPE_FILE_UPLOAD_ITEM.equals(UIType)) {
            widget = ((GeneralFileUploadItem) tbValues.get(columnCode));
            value = ((GeneralFileUploadItem) tbValues.get(columnCode)).getAttachedFiles1();
        } else if (UI_TYPE_PROFILE_IMAGE_WIDGET.equals(UIType)) {
            widget = ((ProfileImage) tbValues.get(columnCode));
            value = ((ProfileImage) tbValues.get(columnCode)).getImageID();
        } else if (UI_TYPE_ENTITY_DROPDOWN.equals(UIType)) {
            widget = (DataListBox) tbValues.get(columnCode);
            if (((DataListBox) tbValues.get(columnCode)).getSelectedItem() != null) {
                Integer id = ((DataListBox) tbValues.get(columnCode)).getSelectedItem().getId();
                if (id != null) {
                    value = String.valueOf(id);
                }
            }
        } else if (TYPE_ENTITY_LOOKUP.equals(UIType)) {
            widget = (EntityCustomFieldLookUp) tbValues.get(columnCode);
            if (((EntityCustomFieldLookUp) tbValues.get(columnCode)).getSelectedItem() != null) {
                Integer id = ((EntityCustomFieldLookUp) tbValues.get(columnCode)).getSelectedItem().getId();
                if (id != null) {
                    value = String.valueOf(id);
                }
            }
        } else if (TYPE_ENTITY_MULTI_LOOKUP.equals(UIType)) {
            widget = (EntityCustomFieldMultiLookUp) tbValues.get(columnCode);
            if (((EntityCustomFieldMultiLookUp) tbValues.get(columnCode)).getSelectedItems() != null
                    && ((EntityCustomFieldMultiLookUp) tbValues.get(columnCode)).getSelectedItems().size() > 0) {
                value = ((EntityCustomFieldMultiLookUp) tbValues.get(columnCode)).getSelectedItems();
            }
        } else if (UI_TYPE_TEXTAREA.equals(UIType)) {
            widget = ((TextArea) tbValues.get(columnCode));
            value = ((TextArea) tbValues.get(columnCode)).getText();
            if (((TextArea) tbValues.get(columnCode)).getText() == null || ((TextArea) tbValues.get(columnCode)).getText().isEmpty()) {
                value = null;
            } else if (customFieldsMap.get(columnCode).getMinChar() != null && !customFieldsMap.get(columnCode).getMinChar().equals("") && ((TextArea) tbValues.get(columnCode)).getText().trim().length() != Integer.parseInt(customFieldsMap.get(columnCode).getMinChar())) {
                Info.warn(wfmMessages.allowedCharLimit(customFieldsMap.get(columnCode).getFieldName(), customFieldsMap.get(columnCode).getMinChar()));
                value = null;
            }
        } else if (UI_TYPE_HTML_TEXTAREA.equals(UIType)) {
            widget = ((KpiEditor) tbValues.get(columnCode));
            value = ((KpiEditor) tbValues.get(columnCode)).getData();
            if (((KpiEditor) tbValues.get(columnCode)).getData() == null || ((KpiEditor) tbValues.get(columnCode)).getData().isEmpty()) {
                value = null;
            }
        } else if (UI_TYPE_LOOKUP.equals(UIType)) {
            widget = (CustomFieldLookUp) tbValues.get(columnCode);
            if (((CustomFieldLookUp) tbValues.get(columnCode)).getSelectedItem() != null) {
                Integer id = ((CustomFieldLookUp) tbValues.get(columnCode)).getSelectedItem().getId();
                stringValue = ((CustomFieldLookUp) tbValues.get(columnCode)).getSelectedItem().getName();
                if (id != null) {
                    value = String.valueOf(id);
                }
            }
        } else if (UI_TYPE_MULTI_LOOKUP.equals(UIType)) {
            widget = (MultiSelectCustomFieldLookUp) tbValues.get(columnCode);
            if (((MultiSelectCustomFieldLookUp) tbValues.get(columnCode)).getSelectedItems() != null
                    && ((MultiSelectCustomFieldLookUp) tbValues.get(columnCode)).getSelectedItems().size() > 0) {
                value = ((MultiSelectCustomFieldLookUp) tbValues.get(columnCode)).getSelectedItems();
            }
        } else if (UI_TYPE_CURRENCY.equals(UIType)) {
            widget = (CurrencyWidget) tbValues.get(columnCode);
            if (((CurrencyWidget) tbValues.get(columnCode)).getCurrencyID() != null) {
                Integer id = ((CurrencyWidget) tbValues.get(columnCode)).getCurrencyID();
                stringValue = ((CurrencyWidget) tbValues.get(columnCode)).getCurrencyName();
                if (id != null) {
                    value = String.valueOf(id);
                }
            }
        }
        if (isRequired && value == null && widget != null) {
            markAsError(widget, isWrong);
            if (isWrong)
                validationObjects.put(columnCode, widget);
        }

        return new CustomFieldWidget(widget, value, stringValue);
    }

    private ArrayList<String> ignoreValidationList = new ArrayList<>();

    public int markAsError(String id, Widget widget, boolean isWrong) {
        if (id == null || !ignoreValidationList.contains(id)) {
            return markAsError(widget, isWrong);
        }
        return 0;
    }

    public int markAsError(Widget widget, boolean isWrong) {
        if (widget != null && isWrong) {
            errorWidgets.add(widget);
            if (widget instanceof KpiSelect2) {
                KpiSelect2 select2 = (KpiSelect2) widget;
                select2.validate(Constants.ERROR_FORM_STYLE, true);
            } else {
                widget.addStyleName(Constants.ERROR_FORM_STYLE);
            }
            Utils.openParentSection(widget);
            return 1;
        }
        return 0;
    }

    public void clearErrorMarks() {
        if (errorWidgets != null && errorWidgets.size() > 0) {
            for (Widget widget : errorWidgets) {
                if (widget != null) {
                    if (widget instanceof KpiSelect2) {
                        KpiSelect2 select2 = (KpiSelect2) widget;
                        select2.validate(Constants.ERROR_FORM_STYLE, false);
                    } else {
                        widget.removeStyleName(Constants.ERROR_FORM_STYLE);
                    }
                }
            }
        }
    }

    public BigDecimal parsePriceToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            NumberFormat priceFormat = NumberFormat.getFormat(",##0.00000");
            return BigDecimal.valueOf(Utils.universalParse(priceFormat, text));
        }
        return BigDecimal.ZERO;
    }

    public static String formatCustomPrice(BigDecimal price, Integer scale) {
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_FORMAT_WITH_CALCULATION_SCALE)) {
            return Utils.getCalculationNumberFormat().format(price.setScale(Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2, RoundingMode.HALF_UP).doubleValue());
        }
        if (scale != null) {
            return Utils.getCalculationNumberFormatWithCustomScale(scale).format(price.setScale(scale, RoundingMode.HALF_UP).doubleValue());
        }
        NumberFormat unitPriceNumberFormat = NumberFormat.getFormat(",##0.00##");
        return unitPriceNumberFormat.format(price.stripTrailingZeros().doubleValue());
    }

    class CustomFieldWidget {
        private Widget widget;
        private Object value;
        private final String stringValue;

        public CustomFieldWidget(Widget widget, Object value, String stringValue) {
            this.widget = widget;
            this.value = value;
            this.stringValue = stringValue;
        }

        public Widget getWidget() {
            return widget;
        }

        public void setWidget(Widget widget) {
            this.widget = widget;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getStringValue() {
            return stringValue;
        }
    }

    public void setFormItemIdToTheCommitBoxFields(Integer formItemTd) {
        for (CompanyCustomFieldItem item : getCustomFieldsValue()) {
            if (Constants.UI_TYPE_COMMITBOX.equals(item.getUiType()) && (tbValues.get(item.getColumnCode()) instanceof NoteWidgetCustomField)) {
                NoteWidgetCustomField noteWidgetCustomField = (NoteWidgetCustomField) tbValues.get(item.getColumnCode());
                noteWidgetCustomField.setFormItemIdToAllCommitOfThisCF(formItemTd);
            }
        }
    }

    private void lookUpSelectionHandler(CompanyCustomFieldItem customField, CustomFieldLookUp customFieldLookUp) {
        selectedItems = new ArrayList<>();
        triggersLookUpTypeName.forEach(item -> {
            if (customField.getLookUpTypeEnum() != null && item.getItemTableEntity() != null && item.getItemTableEntity().equals(customField.getLookUpTypeEnum().name()) && lookUpSelection != null) {
                SelectItem selectedItem = new SelectItem();
                selectedItem.setId(customFieldLookUp.getSelectedItemID());
                selectedItem.setName(customFieldLookUp.getSelectedItem().getName());
                selectedItem.setItemTableRelation(item.getItemTableRelation());
                selectedItem.setItemTableUuid(item.getItemTableUuid());
                selectedItem.setItemTableEntity(item.getItemTableEntity());
                selectedItems.add(selectedItem);
            }
        });
        lookUpSelection.execute();
    }

    public void setLookUpSelection(Command lookUpSelection) {
        this.lookUpSelection = lookUpSelection;
    }

    public void setTriggersLookUpTypeName(ArrayList<SelectItem> triggersLookUpTypeName) {
        this.triggersLookUpTypeName = triggersLookUpTypeName;
    }
}
