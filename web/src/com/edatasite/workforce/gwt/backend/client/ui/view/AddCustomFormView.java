package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormValidation;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ValidationType;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.cell.TextBoxCell;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 2/21/12
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddCustomFormView extends CustomForm implements CustomFormConstants, Colapse {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final BackendServiceAsync service = BackendService.App.get();
    private static final String FIELD_CODE = "Field Code";
    private static final String WIDGET_TYPE = "Widget Type";
    private static final String VALIDATION_NAME = "Validation Name";
    private static final String JOINED_FIELD_CODE = "Joined Field";

    private TextBox title;
    private DataListBox formID;
    private FlexTable formTypes;
    private KpiCheckBox addForm;
    private KpiCheckBox editForm;
    private KpiCheckBox viewForm;
    private KpiCheckBox importForm;
    private KpiSwitcher active;
    private KpiEditor customHTML;
    private TextArea customCss;
    private EditableTable validationTable;
    private WfmButton2 save;

    private final Integer companyID;
    private final Integer objectID;
    private LayoutRPC item;

    public AddCustomFormView(Integer objectID, Integer companyID) {
        super("customform", backendStrings.addCustomFormHTML());
        this.objectID = objectID;
        this.companyID = companyID == null || companyID < 1 ? null : companyID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        formID = new DataListBox();
        formID.setItems(LayoutRPC.getFormIDs());
        title = new TextBox();

        formTypes = new FlexTable();
        formTypes.setCellSpacing(5);
        formTypes.setCellPadding(5);
        addForm = new KpiCheckBox(wfmStrings.add());
        editForm = new KpiCheckBox(wfmStrings.edit());
        viewForm = new KpiCheckBox(wfmStrings.summaryView());
        importForm = new KpiCheckBox(wfmStrings.importString());
        formTypes.setWidget(0, 0, addForm);
        formTypes.setWidget(0, 1, editForm);
        formTypes.setWidget(0, 2, viewForm);
        formTypes.setWidget(0, 3, importForm);
        active = new KpiSwitcher();
        active.setWidth("70px");

        customHTML = new KpiEditor();
        customCss = new TextArea();
        customCss.setHeight("200px");

        validationTable = new EditableTable(getValidationColumns());
        validationTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                validationTable.addRow(getValidationWidgets(null));
            }

            @Override
            public void removeRow() {
            }
        });

        addTitleField(CustomFormConstants.DETAILS, backendStrings.formInformation());
        addField(CustomFormConstants.FORM_ID, formID, getTitle(wfmStrings.form(), true));
        addField(CustomFormConstants.TITLE, title, getTitle(wfmStrings.title(), true));
        addField(CustomFormConstants.TYPE, formTypes, getTitle(wfmStrings.type()));
        addField(CustomFormConstants.ACTIVE, active, getTitle(wfmStrings.active()));
        addTitleField(CustomFormConstants.CONTENT, wfmStrings.content());
        addField(CustomFormConstants.CUSTOM_HTML, customHTML, getTitle(backendStrings.customHTML(), true));
        addField(CustomFormConstants.CUSTOM_CSS, customCss, getTitle(wfmStrings.customCss()));
        addField(CustomFormConstants.MULTI_TABLE_PANEL, validationTable, getTitle(backendStrings.validations()));
        show();
    }

    @Override
    protected void addButtons() {
        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(save);
        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> closeTab()));
    }

    @Override
    protected void getDataToFillFields() {
        service.getCustomForm(companyID, objectID, new AsyncCallback<LayoutRPC>() {
            @Override
            public void onFailure(Throwable caught) {
                item = null;
            }

            @Override
            public void onSuccess(LayoutRPC result) {
                item = result;
                fillFields();
            }
        });
    }

    private void fillFields() {
        if (item == null) {
            item = new LayoutRPC();
        }
        formID.setSelectedByValue(item.getFormID());
        title.setText(item.getTitle());
        addForm.setValue(item.isAddForm());
        editForm.setValue(item.isEditForm());
        viewForm.setValue(item.isViewForm());
        importForm.setValue(item.isImportForm());
        active.setValue(item.isActive());
        customHTML.setData(item.getLayout() != null ? item.getLayout() : "");
        customCss.setText(item.getCustomCss());
        validationTable.addRow(getValidationWidgets(null));
        if (item.getValidations() != null && item.getValidations().size() > 0) {
            for (CustomFormValidation validation : item.getValidations()) {
                validationTable.addRow(getValidationWidgets(validation));
            }
        }
    }

    private boolean validate() {
        int errors = 0;
        formTypes.removeStyleName(Constants.ERROR_FORM_STYLE);
        validationTable.setValidRows(0);
        for (int i = 0; i < validationTable.getRowCount(); i++) {
            TextBoxCell fieldCode = (TextBoxCell) validationTable.getColumnById(i, FIELD_CODE);
            DataListBox dwTypes = (DataListBox) validationTable.getColumnById(i, VALIDATION_NAME);
            if (fieldCode != null && fieldCode.getValue() != null && !"".equals(fieldCode.getValue().toString()) && !(dwTypes.getSelectedId() != null && dwTypes.getSelectedId() > 0)) {
                validationTable.setColumnValid(VALIDATION_NAME);
                errors++;
            }

            if (errors > 0) {
                if (errors == validationTable.getRequiredFieldCount()) {
                    validationTable.setItemValid(i, false);
                    errors = 0;
                } else {
                    validationTable.setItemValid(i, false);
                    errors++;
                }
            } else {
                validationTable.setItemValid(i, true);
                validationTable.incValidRow();
                errors = 0;
            }
        }
        if(!Validation.validateTextBoxRequired(title)){
            errors++;
        }
        if(!Validation.validateDataListBoxRequired(formID)){
            errors++;
        }
        if(!Validation.validateEditorRequired(customHTML)){
            errors++;
        }
        if(!(addForm.getValue() || editForm.getValue() || viewForm.getValue() || importForm.getValue())){
            formTypes.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if(errors>0){
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    private void setValues() {
        item = item == null ? new LayoutRPC() : item;
        item.setFormID(formID.getSelectedItem(true).getName());
        item.setTitle(title.getText());
        item.setAddForm(addForm.getValue());
        item.setEditForm(editForm.getValue());
        item.setViewForm(viewForm.getValue());
        item.setImportForm(importForm.getValue());
        item.setActive(active.getValue());
        item.setLayout(customHTML.getData());
        item.setCustomCss(customCss.getText());
        item.getValidations().clear();
        for (int i = 0; i < validationTable.getRowCount(); i++) {
            if (validationTable.isItemValid(i)) {
                CustomFormValidation validation = new CustomFormValidation();
                TextBoxCell fieldCode = (TextBoxCell) validationTable.getColumnById(i, FIELD_CODE);
                DataListBox widgetTypes = (DataListBox) validationTable.getColumnById(i, WIDGET_TYPE);
                DataListBox dwTypes = (DataListBox) validationTable.getColumnById(i, VALIDATION_NAME);
                TextBoxCell dwJoinedField = (TextBoxCell) validationTable.getColumnById(i, JOINED_FIELD_CODE);
                if (fieldCode != null && fieldCode.getValue() != null && !"".equals(fieldCode.getValue().toString()) && widgetTypes != null && widgetTypes.getSelectedId(true) != null) {
                    validation.setFieldCode(fieldCode.getValue() != null ? fieldCode.getValue().toString() : null);
                    validation.setValidationTypeID(dwTypes.getSelectedId());
                    validation.setWidgetType(widgetTypes.getSelectedItem(true).getName());
                    validation.setJoinedFieldCode(dwJoinedField.getValue() != null ? dwJoinedField.getValue().toString() : null);
                    item.getValidations().add(validation);
                }
            }
        }
    }

    private void save() {
        if(!validate()){
            return;
        }
        setValues();
        service.saveCustomForm(companyID, item, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer result) {
                closeTab();
                Info.show(backendStrings.customFormAdded(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FORM_ADD, item, AddCustomFormView.this);
            }
        });
    }

    private Object[] getValidationWidgets(CustomFormValidation item) {
        Object[] objects = new Object[4];
        int index = 0;

        final DataListBox validationTypes = new DataListBox();
        validationTypes.setSelectedNullLabel();
        validationTypes.setItems(getValidationTypes());
        if (item != null) {
            validationTypes.setSelected(item.getValidationTypeID());
        }

        final DataListBox widgetTypes = new DataListBox();
        widgetTypes.setWithoutNullLabel(true);
        widgetTypes.setItems(getWidgetTypes());
        if (item != null && item.getWidgetType() != null) {
            widgetTypes.setSelectedByValue(item.getWidgetType());
        }

        objects[index++] = item != null ? item.getFieldCode() : "";
        objects[index++] = widgetTypes;
        objects[index++] = validationTypes;
        objects[index++] = item != null ? item.getJoinedFieldCode() : "";

        return objects;
    }

    private SelectItem[] getValidationTypes() {
        SelectItem[] types = new SelectItem[4];
        int index = 0;
        types[index++] = new SelectItem(ValidationType.BeforeDate.getId(), ValidationType.BeforeDate.getTitle());
        types[index++] = new SelectItem(ValidationType.AfterRequired.getId(), ValidationType.AfterRequired.getTitle());
        types[index++] = new SelectItem(ValidationType.IsEmail.getId(), ValidationType.IsEmail.getTitle());
        types[index++] = new SelectItem(ValidationType.IsEmpty.getId(), ValidationType.IsEmpty.getTitle());
        return types;
    }

    private SelectItem[] getWidgetTypes() {
        return new SelectItem[]{new SelectItem(0, Constants.UI_TYPE_TEXTBOX),
                new SelectItem(1, Constants.UI_TYPE_DROPDOWN),
                new SelectItem(2, Constants.UI_TYPE_DATEPICKER),
                new SelectItem(3, Constants.UI_TYPE_LOOKUP),
                new SelectItem(4, Constants.UI_TYPE_CHECKBOX),
                new SelectItem(5, Constants.UI_TYPE_RADIOBUTTON),
                new SelectItem(6, Constants.UI_TYPE_MULTITABLE),
                new SelectItem(7, Constants.UI_TYPE_PHONENUMBER),
                new SelectItem(8, Constants.UI_TYPE_FILE_UPLOAD_WIDGET),
                new SelectItem(9, Constants.UI_TYPE_FILE_UPLOAD_ITEM),
                new SelectItem(10, Constants.UI_TYPE_PROFILE_IMAGE_WIDGET)
        };
    }

    private ColumnConfig[] getValidationColumns() {
        int index = 0;
        ColumnConfig[] columns = new ColumnConfig[4];
        columns[index] = new ColumnConfig(TextBoxCell.class, FIELD_CODE, 250, true);
        columns[index++].setTitle(FIELD_CODE);

        columns[index] = new ColumnConfig(CustomCell.class, WIDGET_TYPE, 250, true);
        columns[index++].setTitle(WIDGET_TYPE);

        columns[index] = new ColumnConfig(CustomCell.class, VALIDATION_NAME, 250, true);
        columns[index++].setTitle(VALIDATION_NAME);

        columns[index] = new ColumnConfig(TextBoxCell.class, JOINED_FIELD_CODE, 250, false);
        columns[index++].setTitle(JOINED_FIELD_CODE);

        return columns;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CUSTOM_FORM_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}