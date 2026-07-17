package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ExtendedDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollBatchLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollCategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.rpc.OvertimeObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.OvertimeObjectData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OvertimeAddEditView extends CustomForm2 implements Colapse {

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private String type;
    private Integer objectId;
    private OvertimeObject data;
    private Boolean isEmployeeType;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private NumberData numberData;

    private Command changeDepartmentAndGroupLookUp;
    private Command changeEmployeeLookUp;
    private DepartmentLookUp departmentLookUp;
    private EmployeeLookUpWithCode employeeLookUp;
    private PayrollBatchLookUp groupTypeLookUp;
    private PayrollCategoryLookUp categoryLookUp;
    private DatePicker datePicker;
    private EditableTable overtimeItemTable;
    private WfmButton2 draftButton;
    private WfmButton2 approveButton;
    private WfmButton2 submitButton;
    private TextBox defaultHours;
    private Numbering numbering;

    private ChosenApproversWidget approvers;
    private FormHasCustomField customFieldUtil;

    private KpiCheckBox applyForSubDepartment;

    public OvertimeAddEditView() {
        super("addOvertime", "overtimeAdd ");
    }

    public OvertimeAddEditView(String type) {
        this(type, null);
    }

    public OvertimeAddEditView(String type, Integer objectId) {
        super("addOvertime", "overtimeAdd ");
        this.objectId = objectId;
        this.type = type;
        this.isEmployeeType = OvertimeListView.OVERTIME_EMPLOYEE_TYPE.equals(type);
    }

    protected Widget onInitialize() {
        initializeLookUpChange();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Overtime, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                formPropertyMap = result.getFormPropertyMap();
                if (getCustomFieldUtil() != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                }
                OvertimeAddEditView.super.onInitialize();
            }
        });
        return null;
    }

    private void initializeLookUpChange() {
        changeDepartmentAndGroupLookUp = () -> {
            LoadingPanel.loading(true);
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            if (departmentLookUp.getSelectedItemID() != null) {
                filterParameter.setDepartmentId(departmentLookUp.getSelectedItemID());
                filterParameter.setApplyForSubDepartment(applyForSubDepartment.getValue());
            } else if (groupTypeLookUp.getSelectedItemID() != null) {
                filterParameter.setObjectId(groupTypeLookUp.getSelectedItemID());
            }
            PayrollService.App.get().getOvertimeEmployees(filterParameter, new AsyncCallback<List<SelectItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(List<SelectItem> selectItems) {
                    LoadingPanel.loading(false);
                    overtimeItemTable.removeAllRows();
                    for (SelectItem item : selectItems) {
                        overtimeItemTable.addRow(getWidgets(setDataToObject(item)));
                    }
                }
            });
        };

        changeEmployeeLookUp = () -> {
            overtimeItemTable.removeAllRows();
            overtimeItemTable.addRow(getWidgets(setDataToObject(employeeLookUp.getSelectedItem())));
        };

    }

    private OvertimeObjectData setDataToObject(SelectItem employee) {
        OvertimeObjectData objectData = new OvertimeObjectData();
        if (employee == null) return objectData;
        objectData.setEmployee(employee);
        objectData.setCategory(categoryLookUp.getSelectedItem());
        return objectData;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private Widget[] getWidgets(OvertimeObjectData objectData) {
        EmployeeLookUp employeeWithCode = new EmployeeLookUp(true, false, false);
        PayrollCategoryLookUp categoryLookUp = new PayrollCategoryLookUp("Payment");
        ExtendedDatePicker date = new ExtendedDatePicker();
        CustomCellTextBox textBox = new CustomCellTextBox();


        employeeWithCode.setStyleName(Constants.DEFAULT_WIDTH);
        categoryLookUp.setStyleName(Constants.DEFAULT_WIDTH);
        date.setStyleName(Constants.DEFAULT_WIDTH);
        textBox.setStyleName(Constants.DEFAULT_WIDTH);

        Validation.addNumericKeyboardListener(textBox, 2);

        employeeWithCode.setSelected(objectData.getEmployee());
        categoryLookUp.setSelected(objectData.getCategory());
        if (objectData.getDate() == null) {
            date.setDate(datePicker.getDate());
        } else {
            date.setDate(objectData.getDate().getDate());
        }
        if (objectData.getOvertimeHours() != null) {
            textBox.setText(PayrollClientUtils.format(objectData.getOvertimeHours()));
        }
        return new Widget[]{employeeWithCode, date, textBox, categoryLookUp};
    }

    private void drawFields() {
        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());
        String name = null;
        Boolean isRequired = false;

        isRequired = formPropertyMap.get(DEPARTMENT) != null && formPropertyMap.get(DEPARTMENT).isRequired();
        if (OvertimeListView.OVERTIME_EMPLOYEE_TYPE.equals(type)) {
            name = formPropertyMap.get(DEPARTMENT) != null && formPropertyMap.get(DEPARTMENT).isChanged() ? formPropertyMap.get(DEPARTMENT).getTitle() : wfmStrings.employee();
            addField(DEPARTMENT, employeeLookUp, getTitle(name != null ? name : wfmStrings.employee(), isRequired));
        } else if (OvertimeListView.OVERTIME_DEPARTMENT_TYPE.equals(type)) {
            name = formPropertyMap.get(DEPARTMENT) != null && formPropertyMap.get(DEPARTMENT).isChanged() ? formPropertyMap.get(DEPARTMENT).getTitle() : wfmStrings.department();
            addField(DEPARTMENT, departmentLookUp, getTitle(name != null ? name : wfmStrings.department(), isRequired));
            addField(APPLY_SUB_DEPARTMENT, applyForSubDepartment, getTitle(formPropertyMap.get(APPLY_SUB_DEPARTMENT) != null && formPropertyMap.get(APPLY_SUB_DEPARTMENT).isChanged() ? formPropertyMap.get(APPLY_SUB_DEPARTMENT).getTitle() : wfmStrings.showSubDepartmentEmployees(), formPropertyMap.get(APPLY_SUB_DEPARTMENT) != null && formPropertyMap.get(APPLY_SUB_DEPARTMENT).isRequired()));
        } else if (OvertimeListView.OVERTIME_GROUP_EMPLOYEE_TYPE.equals(type)) {
            name = formPropertyMap.get(DEPARTMENT) != null && formPropertyMap.get(DEPARTMENT).isChanged() ? formPropertyMap.get(DEPARTMENT).getTitle() : wfmStrings.group();
            addField(DEPARTMENT, groupTypeLookUp, getTitle(name != null ? name : wfmStrings.group(), isRequired));
        }

        addField(CATEGORY, categoryLookUp, getTitle(formPropertyMap.get(CATEGORY) != null && formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category(), formPropertyMap.get(CATEGORY) != null && formPropertyMap.get(CATEGORY).isRequired()));

        addField(DATE, datePicker, getTitle(formPropertyMap.get(DATE) != null && formPropertyMap.get(DATE).isChanged() ? formPropertyMap.get(DATE).getTitle() : wfmStrings.date(), formPropertyMap.get(DATE) != null && formPropertyMap.get(DATE).isRequired()));

        addField(APPROVERS, approvers, getTitle(formPropertyMap.get(APPROVERS) != null && formPropertyMap.get(APPROVERS).isChanged() ? formPropertyMap.get(APPROVERS).getTitle() : wfmStrings.approvers(), formPropertyMap.get(APPROVERS) != null && formPropertyMap.get(APPROVERS).isRequired()));

        addField(DEFAULT_HOUR, defaultHours, getTitle(formPropertyMap.get(DEFAULT_HOUR) != null && formPropertyMap.get(DEFAULT_HOUR).isChanged() ? formPropertyMap.get(DEFAULT_HOUR).getTitle() : wfmStrings.hours(), formPropertyMap.get(DEFAULT_HOUR) != null && formPropertyMap.get(DEFAULT_HOUR).isRequired()));

        addField(OVERTIME_NUMBER, numbering, getTitle(formPropertyMap.get(OVERTIME_NUMBER) != null && formPropertyMap.get(OVERTIME_NUMBER).isChanged() ? formPropertyMap.get(OVERTIME_NUMBER).getTitle() : wfmStrings.overtimeCode(), formPropertyMap.get(OVERTIME_NUMBER) != null && formPropertyMap.get(OVERTIME_NUMBER).isRequired()));

        addField(OVERTIME_ITEM_TABLE, overtimeItemTable, null, false);

    }

    private void generateNewNumber() {
        PayrollService.App.get().generateOvertimeCode(new AbstractAsyncCallback<NumberData>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(NumberData result) {
                if (result != null) {
                    numberData = result;
                    if (numbering != null) {
                        numbering.setNumberData(numberData);
                    }
                }
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected void addButtons() {

    }

    private void save(String status) {
        if (!validation(status)) {
            Info.warn(wfmStrings.fillAllRequiredFields());
            return;
        }

        setOvertimeValues();
        data.setStatusCode(status);
        LoadingPanel.loading(true);
        PayrollService.App.get().saveOvertimeItem(data, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer objectId) {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("overtime|add/summary/" + type + "/" + objectId);
                LoadingPanel.loading(false);
            }
        });
    }

    private void initButtonsPanel() {
        draftButton = new WfmButton2(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE);
        draftButton.addClickHandler(event -> save(Constants.OVERTIME_DRAFT));
        draftButton.setVisible(false);
        addRightButton(draftButton);

        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
        approveButton.addClickHandler(clickEvent -> save(Constants.OVERTIME_APPROVED));
        approveButton.setVisible(false);
        addRightButton(approveButton);


        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.addClickHandler(clickEvent -> save(Constants.OVERTIME_SUBMITTED));
        submitButton.setVisible(false);
        addRightButton(submitButton);

        approvers = new ChosenApproversWidget(RelationItem.TYPE_OVERTIME, data.getApproverEmployee() != null ? objectId : null);

        if (data.isApprover()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVERS) != null) {
                addField(CustomFormConstants.APPROVERS, approvers, getTitle(formPropertyMap.get(CustomFormConstants.APPROVERS).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVERS).getTitle() : wfmStrings.approver(), formPropertyMap.get(CustomFormConstants.APPROVERS).isRequired()));
                approvers.setEnabled(!formPropertyMap.get(CustomFormConstants.APPROVERS).isDisabled());
            } else {
                addField(APPROVERS, approvers, getTitle(wfmStrings.approver(), true));
            }
            if (objectId != null) {
                if (Constants.OVERTIME_DRAFT.equals(data.getStatusCode())) {
                    draftButton.setVisible(true);
                } else if (Constants.OVERTIME_SUBMITTED.equals(data.getStatusCode()) ||
                        Constants.OVERTIME_APPROVED.equals(data.getStatusCode())) {
                    draftButton.setVisible(false);
                }
            } else {
                draftButton.setVisible(true);
            }
        } else {
            approveButton.setVisible(false);
            draftButton.setVisible(true);
            if (Constants.OVERTIME_SUBMITTED.equals(data.getStatusCode()) ||
                    Constants.OVERTIME_APPROVED.equals(data.getStatusCode())) {
                draftButton.setVisible(false);
            }
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, OvertimeAddEditView.this, (sender, args) -> {
            if (approvers.getFirstApproverLookUp() != null) {
                approvers.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    Integer itemId = item != null ? item.getId() : null;
                    if (itemId != null && Utils.getUserID().equals(itemId)) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        submitButton.setVisible(true);
                        approveButton.setVisible(false);
                    }
                });
                if (approveButton != null && submitButton != null && approvers.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            }
            if (Constants.OVERTIME_APPROVED.equals(data.getStatusCode())) {
                approveButton.setVisible(false);
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(objectId);
        LoadingPanel.loading(true);
        PayrollService.App.get().getOvertimeObject(objectId, true, new AsyncCallback<OvertimeObject>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(OvertimeObject overtimeObject) {
                LoadingPanel.loading(false);
                data = overtimeObject;
                initButtonsPanel();
                setDataToFields();
                if (overtimeObject != null) {
                    OvertimeAddEditView.this.getCustomFieldUtil().setCompanyCustomFieldItems(overtimeObject.getCustomFieldItems());
                }
            }
        });
    }

    private void setDataToFields() {
        overtimeItemTable.removeAllRows();
        if (isEmployeeType) {
            employeeLookUp.setSelected(data.getSelectedEmployee());
        } else if (OvertimeListView.OVERTIME_DEPARTMENT_TYPE.equals(type)) {
            departmentLookUp.setSelected(data.getSelectedDepartment());
        } else if (OvertimeListView.OVERTIME_GROUP_EMPLOYEE_TYPE.equals(type)) {
            groupTypeLookUp.setSelected(data.getPayrollBatch());
        }

        applyForSubDepartment.setValue(data.getApplyForSubDepartment());

        if (data.getDate() != null) {
            datePicker.setDate(data.getDate().getDate());
        }
        if (data.getNumberData() != null) {
            numbering.setNumberData(data.getNumberData());
        }
        categoryLookUp.setSelected(data.getCategory());
        if (data.getItems() != null && data.getItems().size() > 0) {
            for (OvertimeObjectData dataObject : data.getItems()) {
                overtimeItemTable.addRow(getWidgets(dataObject));
            }
        } else {
            overtimeItemTable.addRow(getDefaultWidgets());
        }
        if (data.getDefaultHours() != null) {
            defaultHours.setText(PayrollClientUtils.format(data.getDefaultHours()));
        }
//        if (data.getNumberData() == null) {
//            generateNewNumber();
//        }
        getCustomFieldUtil().fillCustomFieldsWithData(data.getCustomFieldItems());
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.OVERTIME_FORM;
    }

    @Override
    protected String getFormType() {
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void registerFields() {
        String debugId = "addEditOvertimeView";
        datePicker = new DatePicker();
        datePicker.ensureDebugId(debugId + "datePicker");
        categoryLookUp = new PayrollCategoryLookUp("Payment");
        categoryLookUp.setEnsureDebugId(debugId + "categoryLookUp");
        departmentLookUp = new DepartmentLookUp();
        departmentLookUp.setEnsureDebugId(debugId + "departmentLookUp");
        employeeLookUp = new EmployeeLookUpWithCode();
        employeeLookUp.setEnsureDebugId(debugId + "employeeLookUp");
        groupTypeLookUp = new PayrollBatchLookUp();
        groupTypeLookUp.setEnsureDebugId(debugId + "groupTypeLookUp");
        approvers = new ChosenApproversWidget(RelationItem.TYPE_OVERTIME, objectId);
        approvers.ensureDebugId(debugId + " approvers");
        defaultHours = new TextBox();
        defaultHours.ensureDebugId(debugId + " defaultHours");
        numbering = new Numbering();
        numbering.setEnabled(false);
        if (objectId == null) {
            generateNewNumber();
        }
        numbering.ensureDebugId(debugId + " numbering");

        Validation.addNumericKeyboardListener(defaultHours, 2);

        applyForSubDepartment = new KpiCheckBox();
        applyForSubDepartment.addValueChangeHandler(event -> {
            if (event.getValue()) {
                if (departmentLookUp.isSelected()) {
                    changeDepartmentAndGroupLookUp.execute();
                }
            }
        });

        datePicker.addChangeHandler(changeEvent -> {
            for (int i = 0; i < overtimeItemTable.getRowCount(); i++) {
                ExtendedDatePicker date = (ExtendedDatePicker) overtimeItemTable.getColumnById(i, ItemTableConstants.PAYMENT_DATE);
                date.setDate(datePicker.getDate());
                overtimeItemTable.getGrid().getModel().update(i, overtimeItemTable.getColumnId(ItemTableConstants.PAYMENT_DATE), date);
            }
        });
        categoryLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
            for (int i = 0; i < overtimeItemTable.getRowCount(); i++) {
                PayrollCategoryLookUp category = (PayrollCategoryLookUp) overtimeItemTable.getColumnById(i, ItemTableConstants.CATEGORY);
                category.setSelected(categoryLookUp.getSelectedItem());
                overtimeItemTable.getGrid().getModel().update(i, overtimeItemTable.getColumnId(ItemTableConstants.CATEGORY), category);
            }
        });
        departmentLookUp.getSuggestBox().addSelectionHandler(event -> {
            applyForSubDepartment.setValue(false);
            changeDepartmentAndGroupLookUp.execute();
        });
        employeeLookUp.getSuggestBox().addSelectionHandler(event -> {
            changeEmployeeLookUp.execute();
        });
        groupTypeLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
            changeDepartmentAndGroupLookUp.execute();
        });

        defaultHours.addKeyUpHandler(event -> {
            for (int i = 0; i < overtimeItemTable.getRowCount(); i++) {
                CustomCellTextBox hour = (CustomCellTextBox) overtimeItemTable.getColumnById(i, ItemTableConstants.OVERTIME_HOURS);
                hour.setText(defaultHours.getText());
                overtimeItemTable.getGrid().getModel().update(i, overtimeItemTable.getColumnId(ItemTableConstants.OVERTIME_HOURS), hour);
            }
        });
        if (isEmployeeType) {
            overtimeItemTable = new EditableTable(getColumns(), true, true);
            overtimeItemTable.setListener(new EditableTableListener() {
                @Override
                public void addRow() {
                    overtimeItemTable.addRow(getDefaultWidgets());
                }

                @Override
                public void removeRow() {
                }
            });
        } else {
            overtimeItemTable = new EditableTable(getColumns());
            overtimeItemTable.setListener(new EditableTableListener() {
                @Override
                public void addRow() {
                }

                @Override
                public void removeRow() {
                }
            });
        }
        this.getCustomFieldUtil().drawCustomFields(this, this.objectId);
        drawFields();
        show();
        overtimeItemTable.addRow(getDefaultWidgets());
    }

    private void getDepartmentEmployee() {

    }

    private ColumnConfig[] getColumns() {
        ColumnConfig[] columnConfig = new ColumnConfig[4];
        columnConfig[0] = new ColumnConfig(LookUpCell.class, ItemTableConstants.EMPLOYEE, wfmStrings.employee(), 60, false, Constants.LEFT_ALIGN_CELL);
        columnConfig[1] = new ColumnConfig(CustomCell.class, ItemTableConstants.PAYMENT_DATE, wfmStrings.date(), 60, false, Constants.LEFT_ALIGN_CELL);
        columnConfig[2] = new ColumnConfig(CustomCell.class, ItemTableConstants.OVERTIME_HOURS, wfmStrings.overtimeHours(), 60, false, Constants.LEFT_ALIGN_CELL);
        columnConfig[3] = new ColumnConfig(LookUpCell.class, ItemTableConstants.CATEGORY, wfmStrings.category(), 60, false, Constants.LEFT_ALIGN_CELL);

        return columnConfig;
    }

    private Widget[] getDefaultWidgets() {
        EmployeeLookUp employeeWithCode = new EmployeeLookUp(true, false, false);
        PayrollCategoryLookUp categoryLookUp = new PayrollCategoryLookUp("Payment");
        ExtendedDatePicker date = new ExtendedDatePicker();
        CustomCellTextBox textBox = new CustomCellTextBox();
        Validation.addNumericKeyboardListener(textBox, 2);
        date.setDate(null);
        return new Widget[]{employeeWithCode, date, textBox, categoryLookUp};
    }

    private boolean validation(String status) {
        int error = 0;
        boolean isValid = true;
        boolean isStatusDraft = Constants.OVERTIME_DRAFT.equals(status);
        isValid = formPropertyMap != null && formPropertyMap.get(DEPARTMENT) != null && formPropertyMap.get(DEPARTMENT).isRequired() && !isStatusDraft;
        if (isEmployeeType) {
            error += markAsError(employeeLookUp, isValid && !Validation.validateLookUpRequired(employeeLookUp));
        } else if (OvertimeListView.OVERTIME_DEPARTMENT_TYPE.equals(type)) {
            error += markAsError(departmentLookUp, isValid && !Validation.validateLookUpRequired(departmentLookUp));
        } else if (OvertimeListView.OVERTIME_GROUP_EMPLOYEE_TYPE.equals(type)) {
            error += markAsError(groupTypeLookUp, isValid && !Validation.validateLookUpRequired(groupTypeLookUp));
        }
        isValid = formPropertyMap != null && formPropertyMap.get(DATE) != null && formPropertyMap.get(DATE).isRequired() && !isStatusDraft;
        error += markAsError(datePicker, isValid && !Validation.validateDate(datePicker));
        error += markAsError(applyForSubDepartment, isValid && !Validation.validateCheckBoxRequired(applyForSubDepartment, null));
        isValid = formPropertyMap != null && formPropertyMap.get(CATEGORY) != null && formPropertyMap.get(CATEGORY).isRequired() && !isStatusDraft;
        error += markAsError(categoryLookUp, isValid && !Validation.validateLookUpRequired(categoryLookUp));
        if (!isStatusDraft) {
            error += this.getCustomFieldUtil().validateCustomFields();
        }
        if (!isStatusDraft) {
            error += validateItemTable();
        }
        return error == 0;
    }

    private int validateItemTable() {
        int error = 0;
        for (int i = 0; i < overtimeItemTable.getRowCount(); i++) {
            this.overtimeItemTable.resetValidation(i);
            EmployeeLookUp employeeWithCode = (EmployeeLookUp) overtimeItemTable.getColumnById(i, ItemTableConstants.EMPLOYEE);
            PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) overtimeItemTable.getColumnById(i, ItemTableConstants.CATEGORY);
            ExtendedDatePicker date = (ExtendedDatePicker) overtimeItemTable.getColumnById(i, ItemTableConstants.PAYMENT_DATE);
            CustomCellTextBox textBox = (CustomCellTextBox) overtimeItemTable.getColumnById(i, ItemTableConstants.OVERTIME_HOURS);
            if (employeeWithCode != null && employeeWithCode.getSelectedItem() != null) {
                error += markAsError(employeeWithCode, (employeeWithCode.getSelectedItem() == null || employeeWithCode.getSelectedItem().getName().isEmpty()));
                error += markAsError(categoryLookUp, (categoryLookUp.getSelectedItem() == null || categoryLookUp.getSelectedItem().getName().isEmpty()));
                error += markAsError(date, (date.getDate() == null));
                error += markAsError(textBox, (textBox.getText() == null || textBox.getText().isEmpty()));
            }
        }
        return error;
    }

    private void setOvertimeValues() {
        if (data == null) {
            data = new OvertimeObject();
        }
        data.setId(objectId);
        data.setCategory(categoryLookUp.getSelectedItem());
        if (OvertimeListView.OVERTIME_EMPLOYEE_TYPE.equals(type)) {
            data.setSelectedEmployee(employeeLookUp.getSelectedItem());
        } else if (OvertimeListView.OVERTIME_DEPARTMENT_TYPE.equals(type)) {
            data.setSelectedDepartment(departmentLookUp.getSelectedItem());
        } else if (OvertimeListView.OVERTIME_GROUP_EMPLOYEE_TYPE.equals(type)) {
            data.setPayrollBatch(groupTypeLookUp.getSelectedItem());
        }
        data.setApplyForSubDepartment(applyForSubDepartment.getValue());
        data.setApprovers(approvers.getChosenApprovers());
        data.setDate(new DateNonConvertable(datePicker.getDate()));
        data.setItems(getItems());
        data.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        data.setOvertimeType(type);
        data.setDefaultHours(defaultHours.getText() != null && !defaultHours.getText().isEmpty() ? PayrollClientUtils.parseToBigDecimal(defaultHours.getText()) : null);
        data.setNumberData(numbering.getNumberData(false));
    }

    private List<OvertimeObjectData> getItems() {
        List<OvertimeObjectData> items = new ArrayList<>();
        for (int i = 0; i < overtimeItemTable.getRowCount(); i++) {
            EmployeeLookUp employeeWithCode = (EmployeeLookUp) overtimeItemTable.getColumnById(i, ItemTableConstants.EMPLOYEE);
            PayrollCategoryLookUp categoryLookUp = (PayrollCategoryLookUp) overtimeItemTable.getColumnById(i, ItemTableConstants.CATEGORY);
            ExtendedDatePicker date = (ExtendedDatePicker) overtimeItemTable.getColumnById(i, ItemTableConstants.PAYMENT_DATE);
            CustomCellTextBox textBox = (CustomCellTextBox) overtimeItemTable.getColumnById(i, ItemTableConstants.OVERTIME_HOURS);

            if (employeeWithCode != null && employeeWithCode.getSelectedItem() != null) {
                OvertimeObjectData data = new OvertimeObjectData();
                data.setEmployee(employeeWithCode.getSelectedItem());
                data.setCategory(categoryLookUp.getSelectedItem());
                data.setDate(new DateNonConvertable(datePicker.getDate()));
                data.setOvertimeHours(PayrollClientUtils.parseToBigDecimal(textBox.getText()).setScale(2, RoundingMode.HALF_UP));
                items.add(data);
            }
        }
        return items;
    }

    @Override
    protected void initPredefinedValues() {

    }
}
