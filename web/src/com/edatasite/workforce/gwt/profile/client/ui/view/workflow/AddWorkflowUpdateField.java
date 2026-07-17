package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowUpdateField;
import com.edatasite.workforce.gwt.profile.client.ui.DynamicInputWidget;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Hayot on 6/6/2014.
 */
public class AddWorkflowUpdateField extends KpiModal implements Constants {
    protected static final ProfileServiceAsync profileService = ProfileService.App.get();
    private LinkedHashMap<String, ModelField> fields = new LinkedHashMap<>();
    private final LinkedHashMap<String, ModelField> sourceFields = new LinkedHashMap<>();
    private final SelectItem[] addSubtractDateItems = new SelectItem[]{new SelectItem(1, wfmStrings.add(), ADD),
            new SelectItem(2, wfmStrings.subtract(), SUBTRACT)};
    private final SelectItem[] addSubtractAmountItems = new SelectItem[]{new SelectItem(1, wfmStrings.add(), ADD),
            new SelectItem(2, wfmStrings.subtract(), SUBTRACT),
            new SelectItem(3, wfmStrings.copy(), Constants.COPY)};
    private WorkflowUpdateField item;
    private final Localize localize;
    private final Integer workflowID;
    private final Integer objectID;
    
    private DataListBox modules;
    private DataListBox columns;
    private DynamicInputWidget newValue;
    private WfmButton2 saveButton;

    private FlexTable additionalDateTable;
    private KpiCheckBox advancedOptions;
    private DataListBox addSubtractBox;
    private TextBox dayBox;
    private KpiRadioButton current;
    private KpiRadioButton custom;
    private DataListBox dateColumns;

    private HorizontalPanel additionalFieldTable;
    private KpiCheckBox dependOn;
    private DataListBox fieldColumns;

    private FlexTable additionalAmountTable;
    private KpiCheckBox advancedAmountOptions;
    private DataListBox addSubtractAmountBox;
    private TextBox dayAmountBox;
    private KpiRadioButton currentAmount;
    private KpiRadioButton customAmount;
    private DataListBox amountColumns;
    private DataListBox conditions;
    private FormGroup conditionsGroup;
    private FormGroup newValueGroup;

    private final String debug_id = "workflow_update_field_";

    public AddWorkflowUpdateField(Integer objectID, Integer workflowID) {
        this.workflowID = workflowID;
        this.objectID = objectID;
        localize = new Localize();
        setTitle(wfmStrings.updateField());
        setWidth("400px");
        initialize();
        getDataToFillFields();
    }

    private void initialize() {
        modules = new DataListBox();
        modules.ensureDebugId(debug_id + "modules");
        modules.setWithoutNullLabel(true);
        modules.addValueChangeHandler(changeEvent -> notifyOnModuleSelected(((DataListBox) changeEvent.getSource()).getSelectedItem().getReferenceCode(), false));

        columns = new DataListBox();
        columns.ensureDebugId(debug_id + "columns");
        columns.setItems(getColumnsAsReferenceItems());
        columns.addValueChangeHandler(changeEvent -> notifyOnColumnSelected(((DataListBox) changeEvent.getSource()).getSelectedItem()));

        newValue = new DynamicInputWidget(null);
        newValue.setEnabled(false);
        newValue.ensureDebugId(debug_id + "newValue");

        newValueGroup = new FormGroup(wfmStrings.value(), newValue);

        conditions = new DataListBox();
        conditions.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.custom()),
                new SelectItem(1, wfmStrings.today()),
                new SelectItem(2, wfmStrings.yesterday())
        });
        conditions.setSelected(0);
        conditions.addValueChangeHandler(changeEvent -> onConditionChanged(changeEvent.getValue().getId()));

        conditionsGroup = new FormGroup(wfmStrings.condition(), conditions);
        conditionsGroup.setVisible(false);


        initAdditionalDateTable();
        initAdditionalFieldTable();
        initAdditionalAmountTable();

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        saveButton.ensureDebugId(debug_id + "save");

        addWidget(modules, wfmStrings.apps());
        addWidget(columns, wfmStrings.field());
        add(conditionsGroup);
        add(newValueGroup);
        add(additionalFieldTable);
        add(additionalDateTable);
        add(additionalAmountTable);

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        addButton(saveButton);

        open();
    }

    private void getDataToFillFields() {
        LoadingPanel.loading(true);
        profileService.editWorkflowUpdateField(objectID, workflowID, new AbstractAsyncCallback<WorkflowUpdateField>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(WorkflowUpdateField result) {
                LoadingPanel.loading(false);
                item = result;
                fields.clear();
                sourceFields.clear();
                if (item.getFields() != null && item.getFields().size() > 0) {
                    for (ModelField field : item.getFields()) {
                        fields.put(field.getField_ID(), field);
                        sourceFields.put(field.getField_ID(), field);
                    }
                }
                modules.setItems(item.getModules());
                if (item.getCustomFormID() != null) {
                    notifyOnModuleSelected(item.getCustomFormID(), true);
                } else {
                    modules.setSelectedByCode(item.getFormID());
                    setAdditionalTableColumns();
                    setValuesToWidgets();
                }
            }
        });
    }

    private void setValuesToWidgets() {
        if (item.getFieldID() != null) {
            columns.setSelectedByCode(item.getFieldID());
            notifyOnColumnSelected(columns.getSelectedItem());
            if (additionalDateTable != null && additionalDateTable.isVisible() && item.getValue().contains("@")) {
                advancedOptions.setValue(true);
                newValue.setEnabled(false);
                addSubtractBox.setEnabled(true);
                dayBox.setEnabled(true);
                current.setEnabled(true);
                custom.setEnabled(true);
                parseAndSetValue(item.getValue(), AdvancedOptionType.DATE);
            } else if (additionalAmountTable != null && additionalAmountTable.isVisible() && item.getValue().contains("@")) {
                advancedAmountOptions.setValue(true);
                newValue.setEnabled(false);
                addSubtractAmountBox.setEnabled(true);
                dayAmountBox.setEnabled(true);
                currentAmount.setEnabled(true);
                customAmount.setEnabled(true);
                parseAndSetValue(item.getValue(), AdvancedOptionType.AMOUNT);
            } else if (additionalFieldTable != null && additionalFieldTable.isVisible() && item.getCustomFormID() != null) {
                dependOn.setValue(true);
                newValue.setEnabled(false);
                fieldColumns.setEnabled(true);
                fieldColumns.setSelectedByCode(item.getValue());
            } else {
                newValue.setEnabled(true);
                if (item.getConditionId() != null) {
                    conditions.setSelected(item.getConditionId());
                    onConditionChanged(item.getConditionId());
                }
                if (item.getDateValue() != null) {
                    newValue.setValue(item.getDateValue());
                } else {
                    newValue.setValue(item.getValue());
                }
            }
        }
    }

    public SelectItem[] getColumnsAsReferenceItems() {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields != null && fields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                String name = getLocalizedLabel(entry.getValue());
                ReferenceItem referenceItem = entry.getValue().asReferenceItem();
                referenceItem.setName(name != null ? name : referenceItem.getName());
                result.add(referenceItem);
            }
            result.sort(Comparator.comparing(SelectItem::getName));
        }
        return result.toArray(new SelectItem[]{});
    }

    public SelectItem[] getDateColumnsAsReferenceItems() {
        ArrayList<SelectItem> result = new ArrayList<>();
        result.add(new ReferenceItem(-2, wfmStrings.createdDate(), CustomFormConstants.CREATED_DATE));
        result.add(new ReferenceItem(-1, wfmStrings.modifiedDate(), CustomFormConstants.UPDATED_DATE));
        if (sourceFields != null && sourceFields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : sourceFields.entrySet()) {
                if ((UI_TYPE_DATEPICKER.equals(entry.getValue().getWidget()) || DATA_TYPE_DATE.equals(entry.getValue().getType())) &&
                        (columns == null || columns.getSelectedItem() == null ||
                                (modules.getSelectedItem().getReferenceCode() != item.getFormID() || !entry.getValue().getField_ID().equals(columns.getSelectedItem().getReferenceCode()))
                        )) {
                    String localized = localize.localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                    String name = localized != null ? localized : (entry.getValue().getField_ID().contains("string_value") || entry.getValue().getField_ID().contains("double_value") || entry.getValue().getField_ID().contains("date_value") ? entry.getValue().getLabel() : entry.getValue().getField_ID());
                    ReferenceItem referenceItem = entry.getValue().asReferenceItem();
                    referenceItem.setName(name != null ? name : referenceItem.getName());
                    result.add(referenceItem);
                }
            }
        }
        return result.toArray(new SelectItem[]{});
    }

    public SelectItem[] getAmountColumnsAsReferenceItems() {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (sourceFields != null && sourceFields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : sourceFields.entrySet()) {
                if (DATA_TYPE_NUMBER.equals(entry.getValue().getType()) &&
                        (columns == null || columns.getSelectedItem() == null
                                || modules.getSelectedItem().getReferenceCode() != item.getFormID()
                                || !entry.getValue().getField_ID().equals(columns.getSelectedItem().getReferenceCode()))) {
                    String name = getLocalizedLabel(entry.getValue());
                    ReferenceItem referenceItem = entry.getValue().asReferenceItem();
                    referenceItem.setName(name != null ? name : referenceItem.getName());
                    result.add(referenceItem);
                }
            }
        }
        return result.toArray(new SelectItem[]{});
    }

    public SelectItem[] getSourceItems(ModelField field) {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (sourceFields != null && sourceFields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : sourceFields.entrySet()) {
                ModelField f = entry.getValue();
                if ((UI_TYPE_FILE_UPLOAD_WIDGET.equals(f.getWidget()) || UI_TYPE_FILE_UPLOAD_ITEM.equals(f.getWidget()) || UI_TYPE_PROFILE_IMAGE_WIDGET.equals(f.getWidget())) &&
                        UI_TYPE_FILE_UPLOAD_WIDGET.equals(field.getWidget()) || field.getWidget().equals(f.getWidget())) {
                    String localized = localize.localizeByFieldID(f.getForm_ID(), f.getField_ID());
                    String name = localized != null ? localized : (f.getField_ID().contains("string_value") || f.getField_ID().contains("double_value") || f.getField_ID().contains("date_value") ? f.getLabel() : f.getField_ID());
                    ReferenceItem referenceItem = f.asReferenceItem();
                    referenceItem.setName(name != null ? name : referenceItem.getName());
                    result.add(referenceItem);
                }
            }
        }
        return result.toArray(new SelectItem[]{});
    }

    private void initAdditionalDateTable() {
        additionalDateTable = new FlexTable();
        advancedOptions = new KpiCheckBox(wfmStrings.advancedOptions());
        advancedOptions.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue() != null && booleanValueChangeEvent.getValue()) {
                newValue.setEnabled(false);
                addSubtractBox.setEnabled(true);
                dayBox.setEnabled(true);
                current.setEnabled(true);
                custom.setEnabled(true);
            } else {
                newValue.setEnabled(true);
                addSubtractBox.setEnabled(false);
                dayBox.setEnabled(false);
                current.setEnabled(false);
                custom.setEnabled(false);
            }
        });
        addSubtractBox = new DataListBox();
        addSubtractBox.setWithoutNullLabel(true);
        addSubtractBox.setItems(addSubtractDateItems);
        addSubtractBox.setSelectedByCode(ADD);
        addSubtractBox.setEnabled(false);
        addSubtractBox.setWidth("120px");
        dayBox = new TextBox();
        dayBox.setWidth("70px");
        Validation.addNumericKeyboardListener(dayBox);
        dayBox.setEnabled(false);
        HTML days = new HTML(wfmStrings.days());
        current = new KpiRadioButton("currentButton");
        current.setText(wfmStrings.toCurrentValue());
        current.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue() != null && booleanValueChangeEvent.getValue()) {
                custom.setValue(false);
                dateColumns.setEnabled(false);
            }
        });
        current.setValue(true);
        current.setEnabled(false);
        custom = new KpiRadioButton("customButton");
        custom.setText(wfmStrings.dependingOn());
        custom.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue() != null && booleanValueChangeEvent.getValue()) {
                current.setValue(false);
                dateColumns.setEnabled(true);
            }
        });
        custom.setEnabled(false);
        dateColumns = new DataListBox();
        dateColumns.setEnabled(false);
        dateColumns.setWidth("120px");

        additionalDateTable.setWidget(0, 0, advancedOptions);
        additionalDateTable.getFlexCellFormatter().setColSpan(0, 0, 3);
        additionalDateTable.setWidget(1, 0, addSubtractBox);
        additionalDateTable.setWidget(1, 1, dayBox);
        additionalDateTable.setWidget(1, 2, days);
        additionalDateTable.setWidget(2, 0, current);
        additionalDateTable.getFlexCellFormatter().setColSpan(2, 0, 3);
        additionalDateTable.setWidget(3, 0, custom);
        additionalDateTable.setWidget(3, 1, dateColumns);
        additionalDateTable.getFlexCellFormatter().setColSpan(3, 1, 2);
        additionalDateTable.setVisible(false);
    }

    private void initAdditionalFieldTable() {
        additionalFieldTable = new HorizontalPanel();
        dependOn = new KpiCheckBox(wfmStrings.dependingOn());
        dependOn.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue() != null && booleanValueChangeEvent.getValue()) {
                newValue.setEnabled(false);
                fieldColumns.setEnabled(true);
            } else {
                newValue.setEnabled(true);
                fieldColumns.setEnabled(false);
            }
        });
        fieldColumns = new DataListBox();
        fieldColumns.setEnabled(false);
        fieldColumns.setWidth("120px");

        additionalFieldTable.add(dependOn);
        additionalFieldTable.add(fieldColumns);
        additionalFieldTable.setVisible(false);
    }

    private void initAdditionalAmountTable() {
        additionalAmountTable = new FlexTable();
        advancedAmountOptions = new KpiCheckBox(wfmStrings.advancedOptions());
        advancedAmountOptions.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue() != null && booleanValueChangeEvent.getValue()) {
                newValue.setEnabled(false);
                addSubtractAmountBox.setEnabled(true);
                dayAmountBox.setEnabled(true);
                currentAmount.setEnabled(true);
                customAmount.setEnabled(true);
            } else {
                newValue.setEnabled(true);
                addSubtractAmountBox.setEnabled(false);
                dayAmountBox.setEnabled(false);
                currentAmount.setEnabled(false);
                customAmount.setEnabled(false);
            }
        });
        addSubtractAmountBox = new DataListBox();
        addSubtractAmountBox.setWithoutNullLabel(true);
        addSubtractAmountBox.setItems(addSubtractAmountItems);
        addSubtractAmountBox.setSelectedByCode(ADD);
        addSubtractAmountBox.setEnabled(false);
        addSubtractAmountBox.setWidth("120px");
        addSubtractAmountBox.addValueChangeHandler(changeEvent -> dayAmountBox.setEnabled(!Constants.COPY.equals(addSubtractAmountBox.getSelectedItem().getReferenceCode())));
        dayAmountBox = new TextBox();
        dayAmountBox.setWidth("70px");
        Validation.addNumericKeyboardListener(dayBox);
        dayAmountBox.setEnabled(false);
        currentAmount = new KpiRadioButton("currentAmountButton");
        currentAmount.setText(wfmStrings.toCurrentValue());
        currentAmount.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue() != null && booleanValueChangeEvent.getValue()) {
                customAmount.setValue(false);
                dayAmountBox.setEnabled(true);
                amountColumns.setEnabled(false);
            }
        });
        currentAmount.setValue(true);
        currentAmount.setEnabled(false);
        customAmount = new KpiRadioButton("customAmountButton");
        customAmount.setText(wfmStrings.dependingOn());
        customAmount.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue() != null && booleanValueChangeEvent.getValue()) {
                currentAmount.setValue(false);
                dayAmountBox.setEnabled(false);
                amountColumns.setEnabled(true);
            }
        });
        customAmount.setEnabled(false);
        amountColumns = new DataListBox();
        amountColumns.setEnabled(false);
        amountColumns.setWidth("120px");

        additionalAmountTable.setWidget(0, 0, advancedAmountOptions);
        additionalAmountTable.getFlexCellFormatter().setColSpan(0, 0, 3);
        additionalAmountTable.setWidget(1, 0, addSubtractAmountBox);
        additionalAmountTable.setWidget(1, 1, dayAmountBox);
        additionalAmountTable.setWidget(2, 0, currentAmount);
        additionalAmountTable.getFlexCellFormatter().setColSpan(2, 0, 3);
        additionalAmountTable.setWidget(3, 0, customAmount);
        additionalAmountTable.setWidget(3, 1, amountColumns);
        additionalAmountTable.getFlexCellFormatter().setColSpan(3, 1, 2);
        additionalAmountTable.setVisible(false);
    }

    private void parseAndSetValue(String value, AdvancedOptionType type) {
        String[] s = value.split("@");
        switch (type) {
            case DATE:
                parse(addSubtractBox, dayBox, current, custom, dateColumns, s, false);
                break;
            case AMOUNT:
                parse(addSubtractAmountBox, dayAmountBox, currentAmount, customAmount, amountColumns, s, true);
                break;
        }
    }

    private void parse(DataListBox addSubtractBox, TextBox dayBox, KpiRadioButton current, KpiRadioButton custom, DataListBox dateColumns, String[] s, boolean amountTable) {
        if (CURRENT.equals(s[2])) {
            current.setValue(true);
            custom.setValue(false);
            dateColumns.setEnabled(false);
        } else {
            current.setValue(false);
            custom.setValue(true);
            dateColumns.setEnabled(true);
            dateColumns.setSelectedByCode(s[2]);
        }
        if (!"".equals(s[0])) {
            addSubtractBox.setSelectedByDescription(s[0]);
            dayBox.setEnabled(!Constants.COPY.equals(s[0]));
        }
        if (!"".equals(s[1])) {
            dayBox.setText(s[1]);
        }
    }

    public boolean validate() {
        int error = 0;
        newValue.removeStyleName(ERROR_FORM_STYLE);
        if (!Validation.validateDataListBoxRequired(columns)) {
            error++;
        }
        if (additionalDateTable.isVisible() && advancedOptions.getValue() != null && advancedOptions.getValue()) {
            if (custom.getValue() != null && custom.getValue() && !Validation.validateDataListBoxRequired(dateColumns)) {
                error++;
            }
        } else if (additionalAmountTable.isVisible() && advancedAmountOptions.getValue() != null && advancedAmountOptions.getValue()) {
            if (customAmount.getValue() != null && customAmount.getValue() && !Validation.validateDataListBoxRequired(amountColumns)) {
                error++;
            }
        } else if (additionalFieldTable.isVisible() && dependOn.getValue() != null && dependOn.getValue() && !Validation.validateDataListBoxRequired(fieldColumns)) {
            error++;
        } else if (!Validation.validateDataListBoxRequired(conditions)) {
            error++;
        } else if (conditions.getSelectedId() == 0 && newValue.getValue() == null) {
            newValue.addStyleName(ERROR_FORM_STYLE);
            error++;
        }
        if(error > 0){
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void fillRPC() {
        item = item == null ? new WorkflowUpdateField() : item;
        item.setCustomFormID(modules.getSelectedItem() != null && !item.getFormID().equals(modules.getSelectedItem().getReferenceCode()) ? modules.getSelectedItem().getReferenceCode() : null);
        item.setFieldID(columns.getSelectedItem().getReferenceCode());
        item.setLabel(columns.getSelectedItem().getName());
        if (additionalDateTable.isVisible() && advancedOptions.getValue() != null && advancedOptions.getValue()) {
            String value = "";
            value += (addSubtractBox.getSelectedItem() != null ? addSubtractBox.getSelectedItem().getDescription() : "") + "@";
            value += (dayBox.getText() != null ? dayBox.getText() : "") + "@";
            value += current.getValue() != null && current.getValue() ? CURRENT : dateColumns.getSelectedItem().getDescription();
            item.setValue(value);
        } else if (additionalAmountTable.isVisible() && advancedAmountOptions.getValue() != null && advancedAmountOptions.getValue()) {
            String value = "";
            value += (addSubtractAmountBox.getSelectedItem() != null ? addSubtractAmountBox.getSelectedItem().getDescription() : "") + "@";
            value += (dayAmountBox.getText() != null ? dayAmountBox.getText() : "") + "@";
            value += currentAmount.getValue() != null && currentAmount.getValue() ? CURRENT : amountColumns.getSelectedItem().getDescription();
            item.setValue(value);
        } else if (additionalFieldTable.isVisible() && dependOn.getValue() != null && dependOn.getValue()) {
            item.setValue(fieldColumns.getSelectedItem().getReferenceCode());
        } else {
            item.setDateValue(newValue.getDateValue());
            item.setConditionId(conditions.getSelectedId());
            if (conditions.getSelectedItem() != null && newValue.getValue() == null) {
                item.setValue(conditions.getSelectedItem().getName());
            } else {
                item.setValue(newValue.getValue());
            }
        }
    }

    private void save() {
        if (validate()) {
            saveButton.setEnabled(false);
            fillRPC();
            LoadingPanel.loading(true);
            profileService.saveWorkflowUpdateField(item, new AbstractAsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    saveButton.setEnabled(false);
                }

                @Override
                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false);
                    saveButton.setEnabled(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_UPDATE_FIELD_UPDATE, item, AddWorkflowUpdateField.this);
                    close();
                }
            });
        }
    }

    private void notifyOnColumnSelected(SelectItem item1) {
        if (item1 != null && item1.getReferenceCode() != null && fields.get(item1.getReferenceCode()) != null) {
            ModelField f = fields.get(item1.getReferenceCode());
            newValue.setField(f);
            if ((f.getSource() == null || "".equals(f.getSource())) && f.getWidget() != null && !"UNKNOWN".equals(f.getWidget()) && f.getType() != null) {
                if (UI_TYPE_DATEPICKER.equals(f.getWidget()) || DATA_TYPE_DATE.equals(f.getType())) {
                    showAdditionalTable(AdvancedOptionType.DATE);
                    if (dateColumns != null) {
                        dateColumns.clear();
                        dateColumns.setItems(getDateColumnsAsReferenceItems());
                    }
                } else if (DATA_TYPE_NUMBER.equals(f.getType())) {
                    showAdditionalTable(AdvancedOptionType.AMOUNT);
                    if (amountColumns != null) {
                        amountColumns.clear();
                        amountColumns.setItems(getAmountColumnsAsReferenceItems());
                    }
                } else if (modules.getSelectedItem() != null && !modules.getSelectedItem().getReferenceCode().equals(item.getFormID())) {
                    showAdditionalTable(AdvancedOptionType.FIELD);
                    if (fieldColumns != null) {
                        fieldColumns.clear();
                        fieldColumns.setItems(getSourceItems(f));
                    }
                } else {
                    hideAllAdditionalTables();
                }
            } else {
                hideAllAdditionalTables();
            }
        }
    }

    private void showAdditionalTable(AdvancedOptionType type) {
        hideAllAdditionalTables();
        switch (type) {
            case AMOUNT:
                additionalAmountTable.setVisible(true);
                break;
            case DATE:
                additionalDateTable.setVisible(true);
                conditionsGroup.setVisible(true);
                conditions.setSelected(0);
                break;
            case FIELD:
                additionalFieldTable.setVisible(true);
                break;
        }
    }

    private void hideAllAdditionalTables() {
        additionalFieldTable.setVisible(false);
        additionalDateTable.setVisible(false);
        additionalAmountTable.setVisible(false);
        conditionsGroup.setVisible(false);
    }

    private void notifyOnModuleSelected(final String formID, final boolean setValues) {
        columns.clear();
        newValue.clear();
        hideAllAdditionalTables();
        if (formID != null && !formID.equals(item.getFormID())) {
            profileService.getModelFields(formID, new AbstractAsyncCallback<ArrayList<ModelField>>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(ArrayList<ModelField> result) {
                    fields = new LinkedHashMap<>();
                    if (result.size() > 0) {
                        for (ModelField field : result) {
                            fields.put(field.getField_ID(), field);
                        }
                    }
                    setAdditionalTableColumns();
                    if (setValues) {
                        modules.setSelectedByCode(formID);
                        setValuesToWidgets();
                    }
                }
            });
        } else {
            fields = sourceFields;
            setAdditionalTableColumns();
        }
    }

    private void setAdditionalTableColumns() {
        columns.setItems(getColumnsAsReferenceItems());
        dateColumns.setItems(getDateColumnsAsReferenceItems());
        amountColumns.setItems(getAmountColumnsAsReferenceItems());
    }

    private void onConditionChanged(Integer selectedId) {
        if (selectedId == 0) {
            newValueGroup.setVisible(true);
            additionalDateTable.setVisible(true);
        } else {
            newValueGroup.setVisible(false);
            additionalDateTable.setVisible(false);
        }
    }

    private String getLocalizedLabel(ModelField modelField) {
        String name = "";
        String localized = localize.localizeByFieldID(modelField.getForm_ID(), modelField.getField_ID());
        if (localized != null) {
            name = localized;
        } else if (modelField.getField_ID().contains("string_value") || modelField.getField_ID().contains("double_value") || modelField.getField_ID().contains("date_value")) {
            name = modelField.getLabel();
        } else {
            if (modelField.getDynamicLabel() != null && !modelField.getDynamicLabel().equals("")) {
                name = modelField.getDynamicLabel();
            } else {
                name = modelField.getField_ID();
            }
        }
        return name;
    }

    private enum AdvancedOptionType {
        DATE, AMOUNT, FIELD
    }
}
