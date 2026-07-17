package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Author: Azazello
 * Date: 4/10/2018
 * Time: 5:44 PM
 */
public class CustomFieldForm extends Composite implements Constants {
    interface CustomFieldFormUiBinder extends UiBinder<HTMLPanel, CustomFieldForm> {
    }

    private static final CustomFieldFormUiBinder ourUiBinder = GWT.create(CustomFieldFormUiBinder.class);

    protected static final CommonServiceAsync commonService = CommonService.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    @UiField
    HTMLPanel panel;
    @UiField
    Label entityTypeLabel;
    @UiField
    DataListBox entityType;
    @UiField
    Label nameLabel;
    @UiField
    TextBox name;
    @UiField
    Label scaleLabel;
    @UiField
    TextBox scale;
    @UiField
    Label aliasLabel;
    @UiField
    TextBox alias;
    @UiField
    Label dataTypeLabel;
    @UiField
    DataListBox dataType;
    @UiField
    Label fieldTypeLabel;
    @UiField
    DataListBox fieldType;
    @UiField
    Label requiredLabel;
    @UiField
    KpiSwitcher required;
    @UiField
    Label disabledLabel;
    @UiField
    KpiSwitcher disabled;
    @UiField
    HTMLPanel predValuesPanel;
    @UiField
    Label predValuesLabel;
    @UiField
    HTMLPanel queryPanel;
    @UiField
    Label queryFieldLabel;
    @UiField
    TextArea queryField;
    @UiField
    HTMLPanel lookupTypePanel;
    @UiField
    Label lookupTypeLabel;
    @UiField
    DataListBox lookupType;
    @UiField
    HTMLPanel referencePanel;
    @UiField
    HTMLPanel disabledPanel;
    @UiField
    HTMLPanel requiredPanel;
    @UiField
    Panel showToDiv;
    @UiField
    HTMLPanel scalePanel;
    @UiField
    Label minValueLabel;
    @UiField
    TextBox minValue;
    @UiField
    HTMLPanel minValuePanel;

    private Command command;
    private Command buttonCommand;
    private final CustomFieldSection section;
    private final ItemTableEnum itemTableEnum;
    private final Integer objectID;
    private CompanyCustomFieldItem companyCustomField;
    private HorizontalPanelDiv pvPanel;
    private TextBox predValuesBox;
    private KpiDataGrid<SelectItem> valueTable;
    private String uiTypeValue;
    private SelectItem[] uiTypes;
    private final ArrayList<SelectItem> values = new ArrayList<>();
    private String[] stringItems;
    private String[] numberItems;
    private String[] dateItems;
    private String[] allItems;
    private final String entityCategoryName;
    private ReferenceLookUp referenceLookUp;
    private MultiTable showTo;
    private ArrayList<SelectItem> rolesList = new ArrayList<>();
    private final String formId;
    private final String fieldSection;
    private final String itemTableName;

    CustomFieldForm(CustomFieldSection section, ItemTableEnum itemTableEnum, String entityCategoryName, Integer objectID, String formId, String fieldSection, String itemTableName) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.section = section;
        this.itemTableEnum = itemTableEnum;
        this.objectID = objectID;
        this.entityCategoryName = entityCategoryName;
        this.formId = formId;
        this.fieldSection = fieldSection;
        this.itemTableName = itemTableName;
        initialize();
    }

    private static String getLookUpName(CustomFieldLookUpTypeEnum typeEnum) {
        switch (typeEnum) {
            case SALES_INVOICE:
                return wfmStrings.salesInvoice();
            case CURRENCY:
                return wfmStrings.currency();
            case COUNTRY:
                return wfmStrings.country();
            case TERMS:
                return wfmStrings.terms();
            case UNIT_MEASUREMENT:
                return wfmStrings.measurement();
            case PROJECT:
                return wfmStrings.project();
            case PRODUCT:
                return wfmStrings.product();
            case CUSTOMER:
                return Property.get(Constants.CLIENT_LIST, wfmStrings.customer());
            case SUPPLIER:
                return Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier());
            case LEAD:
                return wfmStrings.lead();
            case SALES_QUOTE:
                return Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote());
            case SALES_ORDER:
                return wfmStrings.salesOrders();
            case PURCHASE_ORDER:
                return wfmStrings.purchaseorder();
            case PURCHASE_INVOICE:
                return wfmStrings.purchaseinvoice();
            case EMPLOYEE:
                return wfmStrings.employee();
            case CASE:
                return Property.getPluralWithObjectCode(Constants.CASE_LIST, wfmStrings.cases());
            case TASK:
                return wfmStrings.task();
            case CONTACT:
                return wfmStrings.contact();
            case PERSONAL_GOAL:
                return hrmsStrings.personalGoal();
            case DEPARTMENT_GOAL:
                return Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.departmentGoal(), wfmStrings.department());
            case PROJECT_GOAL:
                return hrmsStrings.projectgoal();
            case BUSINESS_GOAL:
                return hrmsStrings.businessGoal();
            case COMPANY_GOAL:
                return hrmsStrings.companyGoal();
            case OPPORTUNITY:
                return wfmStrings.opportunity();
            case OPPORTUNITY_NAME:
                return Property.get(Constants.Opportunities, wfmStrings.opportunityName(), wfmStrings.opportunity());
            case PRODUCT_CATEGORY:
                return wfmStrings.productCategory();
            case REFERENCE:
                return wfmStrings.reference();
            case VACANCY:
                return wfmStrings.vacancy();
            case PAYMENT_METHOD:
                return wfmStrings.paymentMethod();
            case CANDIDATE:
                return wfmStrings.candidate();
            case DEPARTMENT:
                return wfmStrings.department();
            case POSITION:
                return wfmStrings.position();
            case LOCATION:
                return Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location());
            case TIMESLOT:
                return wfmStrings.timeslot();
            case CONTRACT:
                return wfmStrings.contract();
        }
        return typeEnum.name();
    }

    private void setRoleItemsToListBox() {
        for (int i = 0; i < showTo.getWidgetsMaps().size(); i++) {
            for (Widget widget : showTo.getWidgetsMaps().get(i).getWidgets()) {
                DataListBox listBox = (DataListBox) widget;
                listBox.clear();
                listBox.setItems(rolesList.toArray(new SelectItem[]{}));
            }
        }
    }

    private void onSelectedDataType(String dataType) {
        switch (dataType) {
            case DATA_TYPE_TEXT:
                fieldType.setItems(uiTypes);
                fieldType.removeListItem(uiTypes[2]);
                fieldType.removeListItem(uiTypes[3]);
                fieldType.removeListItem(uiTypes[4]);
                fieldType.removeListItem(uiTypes[6]);
                fieldType.removeListItem(uiTypes[7]);
                fieldType.removeListItem(uiTypes[8]);
                fieldType.removeListItem(uiTypes[9]);
                fieldType.removeListItem(uiTypes[12]);
                fieldType.setSelectedNullLabel();
                break;
            case DATA_TYPE_NUMBER:
                fieldType.setItems(uiTypes);
                fieldType.removeListItem(uiTypes[2]);
                fieldType.removeListItem(uiTypes[3]);
                fieldType.removeListItem(uiTypes[4]);
                fieldType.removeListItem(uiTypes[5]);
                fieldType.removeListItem(uiTypes[6]);
                fieldType.removeListItem(uiTypes[7]);
                fieldType.removeListItem(uiTypes[8]);
                fieldType.removeListItem(uiTypes[9]);
                fieldType.removeListItem(uiTypes[10]);
                fieldType.removeListItem(uiTypes[11]);
                fieldType.removeListItem(uiTypes[13]);
                fieldType.removeListItem(uiTypes[14]);
                fieldType.removeListItem(uiTypes[15]);
                if (entityCategoryName != null) {
                    fieldType.removeListItem(uiTypes[16]);
                }
                fieldType.setSelectedNullLabel();
                setKeyPressHandler(predValuesBox);
                break;
            case DATA_TYPE_DATE:
                fieldType.setItems(uiTypes);
                fieldType.removeListItem(uiTypes[0]);
                fieldType.removeListItem(uiTypes[1]);
                fieldType.removeListItem(uiTypes[2]);
                fieldType.removeListItem(uiTypes[3]);
                fieldType.removeListItem(uiTypes[5]);
                fieldType.removeListItem(uiTypes[6]);
                fieldType.removeListItem(uiTypes[7]);
                fieldType.removeListItem(uiTypes[8]);
                fieldType.removeListItem(uiTypes[10]);
                fieldType.removeListItem(uiTypes[11]);
                fieldType.removeListItem(uiTypes[12]);
                fieldType.removeListItem(uiTypes[13]);
                fieldType.removeListItem(uiTypes[14]);
                fieldType.removeListItem(uiTypes[15]);
                if (entityCategoryName != null) {
                    fieldType.removeListItem(uiTypes[16]);
                }
                fieldType.setSelectedNullLabel();
                break;
        }
    }

    private SelectItem[] getUiTypes() {

        SelectItem[] uiTypes = new SelectItem[Utils.isSuperUser() && entityCategoryName != null ? 18 : (Utils.isSuperUser() || entityCategoryName != null) ? 17 : 16];
        uiTypes[0] = new SelectItem(0, wfmStrings.textBox(), UI_TYPE_TEXTBOX);
        uiTypes[1] = new SelectItem(1, wfmStrings.dropDown(), UI_TYPE_DROPDOWN);
        uiTypes[2] = new SelectItem(2, wfmStrings.checkBox(), UI_TYPE_CHECKBOX);
        uiTypes[3] = new SelectItem(3, wfmStrings.radioButton(), UI_TYPE_RADIOBUTTON);
        uiTypes[4] = new SelectItem(4, wfmStrings.datePicker(), UI_TYPE_DATEPICKER);
        uiTypes[5] = new SelectItem(5, wfmStrings.textArea(), UI_TYPE_TEXTAREA);
        uiTypes[6] = new SelectItem(6, wfmStrings.fileUpload(), UI_TYPE_FILE_UPLOAD_ITEM);
        uiTypes[7] = new SelectItem(7, wfmStrings.fileUploadItem(), UI_TYPE_FILE_UPLOAD_WIDGET);
        uiTypes[8] = new SelectItem(8, wfmStrings.profileImage(), UI_TYPE_PROFILE_IMAGE_WIDGET);
        uiTypes[9] = new SelectItem(9, wfmStrings.dateOrTime(), UI_TYPE_DATEPICKER_TIME);
        uiTypes[10] = new SelectItem(10, wfmStrings.email(), UI_TYPE_TEXTBOX_EMAIL);
        uiTypes[11] = new SelectItem(11, wfmStrings.lookUp(), UI_TYPE_LOOKUP);
        uiTypes[12] = new SelectItem(12, wfmStrings.percentage(), UI_TYPE_PERCENTAGE);
        uiTypes[13] = new SelectItem(13, wfmStrings.urlname(), UI_TYPE_URL);
        uiTypes[14] = new SelectItem(14, wfmStrings.multiSelectLookUp(), UI_TYPE_MULTI_LOOKUP);
        uiTypes[15] = new SelectItem(15, wfmStrings.item(), UI_TYPE_ITEM_WITH_DESCRIPTION);
        if (entityCategoryName != null) {
            uiTypes[16] = new SelectItem(16, wfmStrings.currency(), UI_TYPE_CURRENCY);
        }
        if (Utils.isSuperUser()) {
            if (entityCategoryName != null) {
                uiTypes[17] = new SelectItem(17, wfmStrings.entityLookUp(), TYPE_ENTITY_LOOKUP);
            } else {
                uiTypes[16] = new SelectItem(16, wfmStrings.entityLookUp(), TYPE_ENTITY_LOOKUP);
            }
        }
        return uiTypes;
    }

    private void addButtonClick() {
        if (predValuesBox.getText() != null && !"".equals(predValuesBox.getText())) {
            valueTable.removeStyleName(ERROR_FORM_STYLE);
            addValueToTable(predValuesBox.getText());
            predValuesBox.setText("");
        }
    }

    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> null;

    public void getQuickData() {
        if (objectID != null) {
            LoadingPanel.loading(true, panel);
            commonService.getCustomFieldData(objectID, new AbstractAsyncCallback<CompanyCustomFieldItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false, panel);
                    throwable.printStackTrace();
                }

                @Override
                public void success(CompanyCustomFieldItem cf) {
                    companyCustomField = cf;
                    if (companyCustomField != null) {
                        name.setValue(companyCustomField.getFieldName());
                        alias.setValue(companyCustomField.getAliasName());
                        fieldType.setSelectedByDescription(companyCustomField.getUiType());
                        onFieldTypeChanged();
                        uiTypeValue = companyCustomField.getUiType();
                        required.setValue(companyCustomField.isRequired());
                        queryField.setValue(companyCustomField.getQuery());
                        disabledPanel.setVisible(UI_TYPE_TEXTBOX.equals(fieldType.getSelectedItem().getDescription()) || UI_TYPE_TEXTAREA.equals(fieldType.getSelectedItem().getDescription()) || UI_TYPE_PERCENTAGE.equals(fieldType.getSelectedItem().getDescription()) || UI_TYPE_LOOKUP.equals(fieldType.getSelectedItem().getDescription()) || UI_TYPE_DROPDOWN.equals(fieldType.getSelectedItem().getDescription()));
                        disabled.setValue(companyCustomField.isDisabled());
                        if (!UI_TYPE_CURRENCY.equals(companyCustomField.getUiType())) {
                            requiredPanel.setVisible(true);
                        }
                        if (companyCustomField.getScale() != null) {
                            scale.setText(companyCustomField.getScale().toString());
                            scalePanel.setVisible(true);
                        }
                        if (companyCustomField.getNumberMinValue() != null) {
                            minValue.setText(companyCustomField.getNumberMinValue().toString());
                            minValuePanel.setVisible(true);
                        }
                        onSelectedDataType(companyCustomField.getDataType());
                        getExistingCustomFieds(companyCustomField.getEntityName(), companyCustomField.getDataType());

                        String[] val = companyCustomField.getPredefinedValues() != null ? companyCustomField.getPredefinedValues() : new String[0];
                        SelectItem[] valSorting = companyCustomField.getPredefinedValuesWithSorting() != null ? companyCustomField.getPredefinedValuesWithSorting() : new SelectItem[0];
                        if (valSorting != null && valSorting.length > 0) {
                            for (SelectItem aVal : valSorting) {
                                addValueToTable(aVal);
                            }
                        } else if (val != null) {
                            for (String aVal : val) {
                                addValueToTable(aVal);
                            }
                        }
                        if (uiTypeValue.equals(UI_TYPE_DROPDOWN)) {
                            predValuesPanel.setVisible(true);
                        } else if (UI_TYPE_LOOKUP.equals(uiTypeValue) || UI_TYPE_MULTI_LOOKUP.equals(uiTypeValue)) {
                            if (companyCustomField.getLookUpTypeEnum() != null) {
                                lookupType.setSelectedByDescription(companyCustomField.getLookUpTypeEnum().name());
                            }
                            lookupTypePanel.setVisible(true);
                        } else {
                            predValuesPanel.setVisible(false);
                            predValuesBox.setText("");
                        }
                        fireEntityNameChange(companyCustomField != null ? companyCustomField.getDataType() : null);
                        fieldType.setSelectedByDescription(uiTypeValue);

                        if (companyCustomField.getAllowedRoles() != null && !companyCustomField.getAllowedRoles().isEmpty()) {
                            showTo.removeAllRows();
                            showTo.getWidgetsMaps().clear();
                            for (Integer roleID : companyCustomField.getAllowedRoles()) {
                                showTo.onAddLinkClicked();
                            }
                            for (int i = 0; i < companyCustomField.getAllowedRoles().size(); i++) {
                                ((DataListBox) showTo.getWidgetsMaps().get(i).getWidgets()[0]).setSelected(companyCustomField.getAllowedRoles().get(i));
                            }
                        }

                        if (referenceLookUp != null && companyCustomField.getReferenceItem() != null) {
                            referenceLookUp.setSelected(companyCustomField.getReferenceItem());
                            referenceLookUp.setEnabled(false);
                        }
                        referencePanel.setVisible(UI_TYPE_LOOKUP.equals(fieldType.getSelectedItem().getDescription()) && lookupType.getSelectedItem() != null && CustomFieldLookUpTypeEnum.REFERENCE.name().equals(lookupType.getSelectedItem().getDescription()));

                    }
                    LoadingPanel.loading(false, panel);
                }
            });
        }
    }

    private void addValueToTable(String value) {
        SelectItem selectItem;
        String[] val = value.split("=");
        Integer valSize = values.size();
        if (val.length > 1) {
            Integer sortVal = null;
            try {
                sortVal = val[1] != null && !"".equals(val[1]) ? Integer.parseInt(val[1]) : valSize;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            selectItem = new SelectItem(sortVal != null ? sortVal : valSize, val[0], valSize.toString());
        } else {
            Integer sumValSize = values.size() + 1;
            selectItem = new SelectItem(sumValSize, val[0], valSize.toString());
        }
        addValueToTable(selectItem);
    }

    private void addValueToTable(SelectItem value) {
        values.add(value);
        valueTable.supplyProvider(values);
        valueTable.refresh();
    }

    public boolean validate() {
        int errors = 0;
        valueTable.removeStyleName(ERROR_FORM_STYLE);
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(alias)) {
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(dataType)) {
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(fieldType)) {
            errors++;
        }
        if (fieldType.getSelectedItem() != null) {
            if (fieldType.getSelectedItem().getDescription().equals(UI_TYPE_DROPDOWN)
                    && getPredefinedValues().length == 0) {
                Validation.validateTextBoxRequired(predValuesBox);
                valueTable.addStyleName(ERROR_FORM_STYLE);
                errors++;
            } else if (UI_TYPE_LOOKUP.equals(fieldType.getSelectedItem().getDescription()) || UI_TYPE_MULTI_LOOKUP.equals(fieldType.getSelectedItem().getDescription())) {
                if (!Validation.validateDataListBoxRequired(lookupType)) {
                    errors++;
                }

                if (lookupType.getSelectedItem() != null && CustomFieldLookUpTypeEnum.REFERENCE.name().equals(lookupType.getSelectedItem().getDescription())) {
                    if (!Validation.validateLookUpRequired(referenceLookUp)) {
                        errors++;
                    }
                }

            } else if (TYPE_ENTITY_LOOKUP.equals(fieldType.getSelectedItem().getDescription())) {
                if (!Validation.validateTextAreaRequired(queryField)) {
                    errors++;
                }
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private String[] getPredefinedValues() {
        if (!values.isEmpty()) {
            String[] strings = new String[values.size()];
            int i = 0;
            values.sort(Comparator.comparing(SelectItem::getId));
            for (SelectItem selectItem : values) {
                strings[i++] = selectItem.getName();
            }
            return strings;
        } else {
            return null;
        }
    }

    public void save() {
        String entityName = entityType.getSelectedItem() != null ? entityType.getSelectedItem().getDescription() : "";
        commonService.checkCFNameExists(entityName, entityCategoryName, name.getText(), alias.getText(), objectID, true, itemTableEnum.getTitle(), new AbstractAsyncCallback<CompanyCustomFieldItem>() {
            @Override
            public void failure(Throwable throwable) {
                if (buttonCommand != null) {
                    buttonCommand.execute();
                }
            }

            @Override
            public void success(CompanyCustomFieldItem result) {
                if (!result.isFieldNameExists() && !result.isAliasNameExists()) {
                    saveCustomFields();
                } else if (result.isFieldNameExists() || result.isAliasNameExists()) {
                    if (buttonCommand != null) {
                        buttonCommand.execute();
                    }
                    Info.show(wfmStrings.withTheSameNameAlreadyExist(), Info.Type.WARNING);
                }
            }
        });
    }

    private void initialize() {

        MultiTableWidgets multiTableWidgets = new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                WidgetsMap widgetsMap = new WidgetsMap();
                DataListBox rolesListBox = new DataListBox();
                rolesListBox.addStyleName(DEFAULT_WIDTH);
                widgetsMap.addWidgets(rolesListBox);
                return widgetsMap;
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        };

        showTo = new MultiTable(multiTableWidgets);
        Command command = () -> setRoleItemsToListBox();
        showTo.setOnLinesAdded(command);

        AllInOneService.App.get().getRoles(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(ArrayList<SelectItem> result) {
                rolesList = result;
                setRoleItemsToListBox();
            }
        });
        showToDiv.add(new FormGroup(wfmStrings.visibleTo(), showTo));

        referenceLookUp = new ReferenceLookUp(null);

        entityTypeLabel.setText(wfmStrings.relatedTo());
        nameLabel.setText(wfmStrings.fieldName());
        scaleLabel.setText(wfmStrings.decimalPlaces());
        minValueLabel.setText(wfmStrings.minValue());
        aliasLabel.setText(wfmStrings.aliasName());
        dataTypeLabel.setText(wfmStrings.dataType());
        fieldTypeLabel.setText(wfmStrings.fieldType());
        lookupType.setEnabled(objectID == null);
        predValuesLabel.setText(wfmStrings.predefinedValues());
        queryFieldLabel.setText(wfmStrings.queryPanel());
        requiredLabel.setText(wfmStrings.required());
        disabledLabel.setText(wfmStrings.disabled());
        lookupTypeLabel.setText(wfmStrings.value());
        referencePanel.add(new FormGroup(wfmStrings.referencces(), referenceLookUp));
        //Entity Type
        SelectItem[] entityTypes = new SelectItem[1];
        entityTypes[0] = new SelectItem(1, section.getTitle(), section.name());
        entityType.setItems(entityTypes);
        entityType.setSelected(1);
        entityType.setEnabled(false);
        requiredPanel.setVisible(true);
        //Data Type
        dataType.addValueChangeHandler(changeEvent -> {
            if (dataType.getSelectedItem() != null) {
                fieldType.clear();
                fieldType.setEnabled(objectID == null);
                uiTypeValue = null;
                switch (dataType.getSelectedItem().getName()) {
                    case DATA_TYPE_TEXT:
                        fieldType.setItems(uiTypes);
                        fieldType.removeListItem(uiTypes[2]);
                        fieldType.removeListItem(uiTypes[3]);
                        fieldType.removeListItem(uiTypes[4]);
                        fieldType.removeListItem(uiTypes[6]);
                        fieldType.removeListItem(uiTypes[7]);
                        fieldType.removeListItem(uiTypes[8]);
                        fieldType.removeListItem(uiTypes[9]);
                        fieldType.removeListItem(uiTypes[12]);
                        fieldType.setSelectedNullLabel();
                        break;
                    case DATA_TYPE_NUMBER:
                        fieldType.setItems(uiTypes);
                        fieldType.removeListItem(uiTypes[2]);
                        fieldType.removeListItem(uiTypes[3]);
                        fieldType.removeListItem(uiTypes[4]);
                        fieldType.removeListItem(uiTypes[5]);
                        fieldType.removeListItem(uiTypes[6]);
                        fieldType.removeListItem(uiTypes[7]);
                        fieldType.removeListItem(uiTypes[8]);
                        fieldType.removeListItem(uiTypes[9]);
                        fieldType.removeListItem(uiTypes[10]);
                        fieldType.removeListItem(uiTypes[11]);
                        fieldType.removeListItem(uiTypes[13]);
                        fieldType.removeListItem(uiTypes[14]);
                        if (entityCategoryName != null) {
                            fieldType.removeListItem(uiTypes[15]);
                        }
                        fieldType.setSelectedNullLabel();
                        setKeyPressHandler(predValuesBox);
                        break;
                    case DATA_TYPE_DATE:
                        fieldType.setItems(uiTypes);
                        fieldType.removeListItem(uiTypes[0]);
                        fieldType.removeListItem(uiTypes[1]);
                        fieldType.removeListItem(uiTypes[2]);
                        fieldType.removeListItem(uiTypes[3]);
                        fieldType.removeListItem(uiTypes[5]);
                        fieldType.removeListItem(uiTypes[6]);
                        fieldType.removeListItem(uiTypes[7]);
                        fieldType.removeListItem(uiTypes[8]);
                        fieldType.removeListItem(uiTypes[10]);
                        fieldType.removeListItem(uiTypes[11]);
                        fieldType.removeListItem(uiTypes[12]);
                        fieldType.removeListItem(uiTypes[13]);
                        fieldType.removeListItem(uiTypes[14]);
                        if (entityCategoryName != null) {
                            fieldType.removeListItem(uiTypes[15]);
                        }
                        fieldType.setSelectedNullLabel();
                        break;
                }
                predValuesPanel.setVisible(false);
                predValuesBox.setText("");
                lookupTypePanel.setVisible(false);
                referencePanel.setVisible(false);
                disabledPanel.setVisible(false);
                scalePanel.setVisible(false);
                minValuePanel.setVisible(false);
                if (objectID != null) {
                    fieldType.setSelectedByValue(companyCustomField.getUiType());
                    fieldType.fireEvent(new OurChangeEvent());
                } else {
                    values.clear();
                    valueTable.supplyProvider(values);
                    valueTable.refresh();
                }
            } else {
                fieldType.clear();
                fieldType.setEnabled(false);
                predValuesPanel.setVisible(false);
                predValuesBox.setText("");
                lookupTypePanel.setVisible(false);
                disabledPanel.setVisible(false);
                scalePanel.setVisible(false);
                minValue.setVisible(false);
                values.clear();
                valueTable.supplyProvider(values);
                valueTable.refresh();
                uiTypeValue = null;
            }
        });
        dataType.setEnabled(objectID == null);
        fieldType.setEnabled(objectID == null);
        //Field Type
        uiTypes = getUiTypes();
        fieldType.setItems(uiTypes);
        fieldType.addValueChangeHandler(changeEvent -> onFieldTypeChanged());
        fieldType.setEnabled(false);
        //Predefined Values
        predValuesBox = new TextBox();
        predValuesBox.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyPress(Widget sender, char key, int modifiers) {
                if (key == (char) KEY_ENTER) {
                    if (!Utils.isNullOrEmpty(predValuesBox.getText())) {
                        valueTable.removeStyleName(ERROR_FORM_STYLE);
                        addValueToTable(predValuesBox.getText());
                        predValuesBox.setText("");
                    }
                }
            }
        });
        lookupType.setItems(CustomFieldForm.getLookUpTypes());

        lookupType.addValueChangeHandler(look -> {
            referencePanel.setVisible(lookupType.getSelectedItem() != null && CustomFieldLookUpTypeEnum.REFERENCE.name().equals(lookupType.getSelectedItem().getDescription()));
        });

        Validation.addNumericKeyboardListener(scale);
        scale.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(minValue, 3, false);
        minValue.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        pvPanel = new HorizontalPanelDiv();
        Div buttonDiv = new Div();
        WfmButton2 addValue = new WfmButton2(wfmStrings.addValue(), WfmButton2.BTN_SECONDARY, clickEvent -> {
            addButtonClick();
        });
        buttonDiv.setStyleName("input-group-append");
        buttonDiv.add(addValue);

        pvPanel.add(predValuesBox);
        pvPanel.add(buttonDiv);
        pvPanel.setStyleName("input-group");
        predValuesPanel.add(pvPanel);
        HorizontalPanel space = new HorizontalPanel();
        space.setHeight("4px");
        predValuesPanel.add(space);
        valueTable = new KpiDataGrid<>(KEY_PROVIDER);
        valueTable.setStyleName("cellBasedWidget-mod");
        valueTable.setSize("100%", "300px");

        Column<SelectItem, String> objectIdCell = new Column<SelectItem, String>(new TextCell()) {

            @Override
            public String getValue(final SelectItem object) {
                return object.getDescription();
            }
        };
        valueTable.addColumn(objectIdCell, "Id");
        valueTable.setColumnWidth(objectIdCell, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<SelectItem, String> value = new Column<SelectItem, String>(new TextCell()) {

            @Override
            public String getValue(final SelectItem object) {
                return object.getName();
            }
        };
        valueTable.addColumn(value, wfmStrings.value());
        valueTable.setColumnWidth(value, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        final TextInputCell inputCell = new TextInputCell();
        inputCell.setWidth("100%");
        Column<SelectItem, String> sorder = new Column<SelectItem, String>(inputCell) {
            @Override
            public String getValue(SelectItem object) {
                return object.getId() + "";
            }
        };
        sorder.setFieldUpdater((i, object, value12) -> {
            try {
                if (!value12.equals("0") && !value12.equals("")) {
                    TextBox textBox = new TextBox();
                    textBox.setText(value12);
                    Validation.numberValidationWithoutDot(textBox);
                    if (textBox != null && "".equals(textBox.getText())) {
                        inputCell.clearViewData(KEY_PROVIDER.getKey(object));
                        valueTable.redraw();
                    }
                    object.setId(Integer.parseInt(textBox.getText()));
                } else {
                    object.setId(Integer.parseInt(value12));
                }
            } catch (NumberFormatException ex) {
                object.setId(1);
                valueTable.redraw();
            }
            valueTable.redraw();
        });
        valueTable.addColumn(sorder, wfmStrings.sortOrder());
        valueTable.setColumnWidth(sorder, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        final Column<SelectItem, String> action = new Column<SelectItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final SelectItem object) {
                return wfmStrings.delete();
            }
        };
        action.setFieldUpdater((index, object, value1) -> {
            values.remove(object);
            valueTable.supplyProvider(values);
            valueTable.refresh();
        });
        valueTable.addColumn(action, wfmStrings.action());
        valueTable.setColumnWidth(action, 25, com.google.gwt.dom.client.Style.Unit.PCT);
        predValuesPanel.add(valueTable);

        if (objectID == null) {
            fireEntityNameChange(null);
        }
    }

    private void initCustomFieldColumnCode() {
        int LIMIT_COUNT = FIELD_LIMIT;
        String value = "";
        if (dataType.getSelectedItem().getName().equals(DATA_TYPE_TEXT)) {
            value = "string_value";
            allItems = stringItems;
            LIMIT_COUNT = STRING_FIELD_LIMIT;
        }
        if (dataType.getSelectedItem().getName().equals(DATA_TYPE_NUMBER) || dataType.getSelectedItem().getName().equals(DATA_TYPE_FILE_UPLOAD) || dataType.getSelectedItem().getName().equals(DATA_TYPE_PROFILE_IMAGE)) {
            value = "double_value";
            allItems = numberItems;
            LIMIT_COUNT = DOULE_FIELD_LIMIT;
        }
        if (dataType.getSelectedItem().getName().equals(DATA_TYPE_DATE)) {
            value = "date_value";
            allItems = dateItems;
        }
        if (allItems != null) {
            for (int i = 1; i <= LIMIT_COUNT; i++) {
                String fieldname = value + i;
                int k = 0;
                for (String allItem : allItems) {
                    if (fieldname.equals(allItem)) {
                        k++;
                        break;
                    }
                }
                if (k == 0) {
                    companyCustomField.setColumnCode(fieldname);
                    break;
                }
            }
        } else {
            companyCustomField.setColumnCode(value + 1);
        }
    }

    private SelectItem[] getPredefinedValuesWithSorting() {
        SelectItem[] strings = new SelectItem[values.size()];
        int i = 0;
        int valSize = values.size() + 1;
        for (SelectItem selectItem : values) {
            if (selectItem.getId() == null) {
                selectItem.setId(valSize);
            }
            strings[i++] = selectItem;
        }
        return strings;
    }

    private HandlerRegistration setKeyPressHandler(TextBox textBox) {
        return textBox.addKeyPressHandler(event -> {
            if (dataType.getSelectedItem().getName().equals(DATA_TYPE_NUMBER)) {
                char key = event.getCharCode();
                if (key == (char) 0) {
                    return;
                }
                if ((!Character.isDigit(key)) && (key != (char) KeyCodes.KEY_TAB)
                        && (key != (char) KeyCodes.KEY_BACKSPACE)
                        && (key != (char) KeyCodes.KEY_DELETE) && (key != (char) KeyCodes.KEY_ENTER)
                        && (key != (char) KeyCodes.KEY_HOME) && (key != (char) KeyCodes.KEY_END)
                        && (key != (char) KeyCodes.KEY_LEFT) && (key != (char) KeyCodes.KEY_UP)
                        && (key != (char) KeyCodes.KEY_RIGHT) && (key != (char) KeyCodes.KEY_DOWN)) {
                    ((TextBox) event.getSource()).cancelKey();
                }
            }
        });
    }

    private class OurChangeEvent extends ChangeEvent {
    }

    private void saveCustomFields() {
        if (companyCustomField == null) {
            companyCustomField = new CompanyCustomFieldItem();
        }
        companyCustomField.setEntityName(entityType.getSelectedItem().getDescription());
        companyCustomField.setEntityCategoryName(entityCategoryName);
        companyCustomField.setFieldName(name.getText());
        companyCustomField.setAliasName(alias.getText());
        companyCustomField.setDataType(dataType.getSelectedItem().getName());
        companyCustomField.setUiType(fieldType.getSelectedItem().getDescription());
        if (scale.getText() != null && !scale.getText().isEmpty()) {
            companyCustomField.setScale(Integer.valueOf(scale.getText()));
        }
        if (minValue.getText() != null && !minValue.getText().isEmpty()) {
            companyCustomField.setNumberMinValue(Utils.universalParse(NumberFormat.getFormat(",##0.#"), minValue.getText()));
        }
        if (referencePanel.isVisible()) {
            companyCustomField.setReferenceItem(referenceLookUp.getSelectedItem());
        }

        if (fieldType.getSelectedItem() != null) {
            switch (fieldType.getSelectedItem().getDescription()) {
                case UI_TYPE_DROPDOWN:
                    companyCustomField.setPredefinedValues(getPredefinedValues());
                    companyCustomField.setPredefinedValuesWithSorting(getPredefinedValuesWithSorting());
                    break;
                case UI_TYPE_LOOKUP:
                case UI_TYPE_MULTI_LOOKUP:
                    companyCustomField.setLookUpTypeEnum(lookupType.getSelectedItem() != null
                            ? CustomFieldLookUpTypeEnum.get(lookupType.getSelectedItem().getDescription())
                            : null);
                    break;
                case UI_TYPE_ITEM_WITH_DESCRIPTION:
                    companyCustomField.setLookUpTypeEnum(CustomFieldLookUpTypeEnum.PRODUCT);
                    break;
                case TYPE_ENTITY_LOOKUP:
                    companyCustomField.setQuery(queryField.getValue());
                    break;
            }
        }
        companyCustomField.setRequired(!UI_TYPE_CURRENCY.equals(fieldType.getSelectedItem().getDescription()) && required.getValue());
        companyCustomField.setDisabled(disabled.getValue());
        companyCustomField.setShowInListing(true);
        companyCustomField.setClickable(true);
        companyCustomField.setShowInFilterGrouping(true);
        companyCustomField.setFacetable(true);
        companyCustomField.setEntityType(null);
        companyCustomField.setForm(formId);
        companyCustomField.setSection(fieldSection);
        companyCustomField.setParentFieldName(itemTableName);
        ArrayList<Integer> roles = null;
        if (!showTo.getWidgetsMaps().isEmpty()) {
            roles = new ArrayList<>();
            for (WidgetsMap widgetsMap : showTo.getWidgetsMaps()) {
                if (widgetsMap.getWidgets() != null) {
                    widgetsMap.getWidgets();
                    for (Widget widget : widgetsMap.getWidgets()) {
                        if (((DataListBox) widget).isSomethingSelected()) {
                            roles.add(((DataListBox) widget).getSelectedId());
                        }
                    }
                }
            }
        }
        roles = roles.isEmpty() ? null : roles;
        companyCustomField.setAllowedRoles(roles);
        if (objectID == null) {
            initCustomFieldColumnCode();
        }
        LoadingPanel.loading(true, panel);
        commonService.saveCustomFields(companyCustomField, true, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                if (buttonCommand != null) {
                    buttonCommand.execute();
                }
                LoadingPanel.loading(false, panel);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void aVoid) {
                if (command != null) {
                    command.execute();
                }
                LoadingPanel.loading(false, panel);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.customField()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FIELD_ADD, null, null);
            }
        });
    }

    private void getExistingCustomFieds(String entityName, final String dataType1) {
        LoadingPanel.loading(true, panel);
        commonService.getExistingCustomFields(entityName, entityCategoryName, objectID, new AbstractAsyncCallback<HashMap<Integer, String[]>>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void success(HashMap<Integer, String[]> integerMap) {
                LoadingPanel.loading(false, panel);
                if (dataType1 == null) {
                    uiTypeValue = null;
                }
                if (dataType.getItems() != null && dataType.getItems().length != 0) {
                    if (dataType.getItems() != null && dataType.getItems().length > 0) {
                        SelectItem textItem = dataType.getItems()[0];
                        dataType.removeListItem(textItem);
                    }
                    if (dataType.getItems() != null && dataType.getItems().length > 0) {
                        SelectItem numItem = dataType.getItems()[0];
                        dataType.removeListItem(numItem);
                    }
                    if (dataType.getItems() != null && dataType.getItems().length > 0) {
                        SelectItem dateItem = dataType.getItems()[0];
                        dataType.removeListItem(dateItem);
                    }
                    if (dataType.getItems() != null && dataType.getItems().length > 0) {
                        SelectItem uploadItem = dataType.getItems()[0];
                        dataType.removeListItem(uploadItem);
                    }
                }
                if (integerMap.get(0).length < STRING_FIELD_LIMIT) {
                    stringItems = integerMap.get(0);
                    dataType.addListItem(new SelectItem(0, DATA_TYPE_TEXT));
                } else {
                    stringItems = null;
                }
                if (integerMap.get(1).length < DOULE_FIELD_LIMIT) {
                    numberItems = integerMap.get(1);
                    dataType.addListItem(new SelectItem(1, DATA_TYPE_NUMBER));
                } else {
                    numberItems = null;
                }
                if (integerMap.get(2).length < FIELD_LIMIT) {
                    dateItems = integerMap.get(2);
                    dataType.addListItem(new SelectItem(2, DATA_TYPE_DATE));
                } else {
                    dateItems = null;
                }
                if (dataType1 != null) {
                    dataType.setSelectedByValue(dataType1);
                    dataType.fireEvent(new OurChangeEvent());
                }
            }
        });
    }

    public static SelectItem[] getLookUpTypes() {
        SelectItem[] items = new SelectItem[CustomFieldLookUpTypeEnum.values().length];
        for (int i = 0; i < CustomFieldLookUpTypeEnum.values().length; i++) {
            items[i] = new SelectItem(i + 1, getLookUpName(CustomFieldLookUpTypeEnum.values()[i]), CustomFieldLookUpTypeEnum.values()[i].name());
        }
        return items;
    }

    private void fireEntityNameChange(String dataType) {
        fieldType.setSelectedNullLabel();
        getExistingCustomFieds(entityType.getSelectedItem().getDescription(), dataType);
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    void setButtonCommand(Command buttonCommand) {
        this.buttonCommand = buttonCommand;
    }

    private void onFieldTypeChanged() {
        if (fieldType.getSelectedItem() != null && dataType.getSelectedItem() != null) {
            uiTypeValue = fieldType.getSelectedItem().getDescription();
            predValuesBox.setText("");

            predValuesPanel.setVisible(UI_TYPE_DROPDOWN.equals(fieldType.getSelectedItem().getDescription()));
            lookupTypePanel.setVisible(UI_TYPE_LOOKUP.equals(fieldType.getSelectedItem().getDescription()) || UI_TYPE_MULTI_LOOKUP.equals(fieldType.getSelectedItem().getDescription()));
            disabledPanel.setVisible(UI_TYPE_TEXTBOX.equals(fieldType.getSelectedItem().getDescription()) || UI_TYPE_TEXTAREA.equals(fieldType.getSelectedItem().getDescription()) || UI_TYPE_PERCENTAGE.equals(fieldType.getSelectedItem().getDescription()) || UI_TYPE_LOOKUP.equals(fieldType.getSelectedItem().getDescription()) || UI_TYPE_DROPDOWN.equals(fieldType.getSelectedItem().getDescription()));
            requiredPanel.setVisible(!UI_TYPE_CURRENCY.equals(fieldType.getSelectedItem().getDescription()));
            queryPanel.setVisible(TYPE_ENTITY_LOOKUP.equals(fieldType.getSelectedItem().getDescription()));
            referencePanel.setVisible(UI_TYPE_LOOKUP.equals(fieldType.getSelectedItem().getDescription()) && lookupType.getSelectedItem() != null && CustomFieldLookUpTypeEnum.REFERENCE.name().equals(lookupType.getSelectedItem().getDescription()));
            scalePanel.setVisible(DATA_TYPE_NUMBER.equals(dataType.getSelectedItem().getName()));
            minValuePanel.setVisible(DATA_TYPE_NUMBER.equals(dataType.getSelectedItem().getName()));
        } else {
            predValuesPanel.setVisible(false);
            predValuesBox.setText("");
            lookupTypePanel.setVisible(false);
            disabledPanel.setVisible(false);
            queryPanel.setVisible(false);
            uiTypeValue = null;
            referencePanel.setVisible(false);
            scalePanel.setVisible(false);
            minValuePanel.setVisible(false);
        }
    }
}
