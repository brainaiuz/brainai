package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ExtendedDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedHashMap;

public class OvertimeSummaryView extends CustomForm2 implements Colapse {
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final ListingFilterParameter filterParameter = new ListingFilterParameter();
    private final String type;
    private final Integer objectId;
    private final Boolean isEmployeeType;
    private OvertimeObject data;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private Command changeDepartmentLookUp;
    private Command changeEmployeeLookUp;
    private HTML departmentLookUp, employeeLookUp, categoryLookUp, datePicker, approvers, groupTypeLookUp, defaultHours, overtimeCode;
    private EditableTable overtimeItemTable;
    private WfmButton2 saveButton;
    private FormHasCustomField customFieldUtil;
    private String statusCode;
    private boolean isCurrentApprover;
    private WfmButton2 submitButton, approveButton, declineButton, editButton;


    public OvertimeSummaryView(String type, Integer objectId) {
        super("summaryOvertime", "overtimeSummary");
        this.type = type;
        this.objectId = objectId;
        this.isEmployeeType = OvertimeListView.OVERTIME_EMPLOYEE_TYPE.equals(type);
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Overtime, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                OvertimeSummaryView.super.onInitialize();
            }
        });
        return null;
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
        editButton = addButton(wfmStrings.edit(), WfmButton2.BTN_WHITE_OUTLINE, clickEvent -> {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("overtime|add/edit/" + type + "/" + data.getId());
        });
        editButton.setVisible(false);

        approveButton = addButton(wfmStrings.approve(), WfmButton2.BTN_PRIMARY, clickEvent -> save(Constants.OVERTIME_APPROVED));
        approveButton.setVisible(false);

        declineButton = addButton(wfmStrings.reject(), Constants.BTN_DEFAULT_OUTLINE, clickEvent -> save(Constants.OVERTIME_REJECTED));
        declineButton.setVisible(false);

        submitButton = addButton(Constants.OVERTIME_REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), wfmStrings.submitForApproval(), Constants.BTN_DEFAULT_OUTLINE, clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.OVERTIME_SUBMITTED);
        });
        submitButton.setVisible(false);
    }

    @Override
    protected void getDataToFillFields() {
        if (objectId == null) {
            return;
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(objectId);
        LoadingPanel.loading(true);
        PayrollService.App.get().getOvertimeObject(objectId, false, new AsyncCallback<OvertimeObject>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(OvertimeObject overtimeObject) {
                LoadingPanel.loading(false);
                data = overtimeObject;
                setDataToFields();
                getCustomFieldUtil().fillCustomFieldsWithData(overtimeObject.getCustomFieldItems(), true);
                initButtons();
            }
        });
    }

    private void initButtons() {
        if (data.isApprover()) {
            Integer currentApproverId = data.getApproverEmployee() != null ? data.getApproverEmployee().getId() : null;
            Integer currentUserId = Utils.getUserID();
            if (Constants.OVERTIME_SUBMITTED.equals(statusCode) && currentUserId.equals(currentApproverId)) {
                approveButton.setVisible(true);
                declineButton.setVisible(true);
                submitButton.setVisible(false);
            }

            editButton.setVisible(
                    Utils.hasPermission(PermissionConstants.PAYROLL_OVERTIME_EDIT)
                            && !(Constants.OVERTIME_APPROVED.equals(statusCode)) && (currentUserId.equals(data.getCreator().getId())));


            if (Constants.OVERTIME_REJECTED.equals(statusCode) && data.getCreator() != null && currentUserId.equals(data.getCreator().getId())) {
                submitButton.setVisible(true);
            }
        } else {
            editButton.setVisible(Utils.hasPermission(PermissionConstants.PAYROLL_OVERTIME_EDIT)
                    && !(Constants.OVERTIME_APPROVED.equals(statusCode)));
        }

    }

    private void save(String statusCode) {
        data.setStatusCode(statusCode);
        LoadingPanel.loading(true);
        PayrollService.App.get().updateOvertimeItemsAndStatus(data, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void objectId) {
                closeTab();
                LoadingPanel.loading(false);
            }
        });
    }

    private void setDataToFields() {
        overtimeItemTable.removeAllRows();
        if (isEmployeeType) {
            employeeLookUp.setText(data.getSelectedEmployee() != null ? data.getSelectedEmployee().getName() : "");
        } else if (OvertimeListView.OVERTIME_DEPARTMENT_TYPE.equals(type)) {
            departmentLookUp.setText(data.getSelectedDepartment() != null ? data.getSelectedDepartment().getName() : "");
        } else if (OvertimeListView.OVERTIME_GROUP_EMPLOYEE_TYPE.equals(type)) {
            groupTypeLookUp.setText(data.getPayrollBatch() != null ? data.getPayrollBatch().getName() : "");
        }
        isCurrentApprover = data.getCurrentApproverAsSelectItem() != null ? Utils.getUserID().equals(data.getCurrentApproverAsSelectItem().getId()) : false;
        datePicker.setText(DateUtils.getDateFormatShort(data.getDate().getDate()));
        categoryLookUp.setText(data.getCategory() != null ? data.getCategory().getName() : "");
        if (data.getApproverEmployee() != null) {
            approvers.setText(data.getApproverEmployee().getName());
        }

        if (data.getItems() != null && data.getItems().size() > 0) {
            for (OvertimeObjectData dataObject : data.getItems()) {
                overtimeItemTable.addRow(getWidgets(dataObject));
            }
        }
        if (data.getOverallStatus() != null) {
            statusCode = data.getOverallStatus().getCode();
        }
        if (data.getDefaultHours() != null) {
            defaultHours.setText(PayrollClientUtils.format(data.getDefaultHours()));
        }
        if (data.getCode() != null) {
            overtimeCode.setText(data.getCode());
        }
    }

    private void initTable() {


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
        date.setDate(objectData.getDate().getDate());

        if (objectData.getOvertimeHours() != null) {
            textBox.setText(PayrollClientUtils.format(objectData.getOvertimeHours()));
        }
        employeeWithCode.setEnabled(false);
        date.setEnabled(false);
        textBox.setEnabled(false);
        categoryLookUp.setEnabled(false);
        return new Widget[]{employeeWithCode, date, textBox, categoryLookUp};
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
        datePicker = initHTML();
        categoryLookUp = initHTML();
        departmentLookUp = initHTML();
        employeeLookUp = initHTML();
        groupTypeLookUp = initHTML();
        defaultHours = initHTML();
        overtimeCode = initHTML();
        overtimeItemTable = new EditableTable(getColumns(), false, false, false);
        approvers = initHTML();
        drawFields();
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        show();
    }

    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private ColumnConfig[] getColumns() {
        ColumnConfig[] columnConfig = new ColumnConfig[4];
        columnConfig[0] = new ColumnConfig(LookUpCell.class, ItemTableConstants.EMPLOYEE, wfmStrings.employee(), 60, false, Constants.LEFT_ALIGN_CELL);
        columnConfig[1] = new ColumnConfig(CustomCell.class, ItemTableConstants.PAYMENT_DATE, wfmStrings.date(), 60, false, Constants.LEFT_ALIGN_CELL);
        columnConfig[2] = new ColumnConfig(CustomCell.class, ItemTableConstants.OVERTIME_HOURS, wfmStrings.overtimeHours(), 60, false, Constants.LEFT_ALIGN_CELL);
        columnConfig[3] = new ColumnConfig(LookUpCell.class, ItemTableConstants.CATEGORY, wfmStrings.category(), 60, false, Constants.LEFT_ALIGN_CELL);

        return columnConfig;
    }

    private void drawFields() {
        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());
        String name;

        if (isEmployeeType) {
            name = formPropertyMap.get(DEPARTMENT) != null && formPropertyMap.get(DEPARTMENT).isChanged() ? formPropertyMap.get(DEPARTMENT).getTitle() : wfmStrings.employee();
            addField(DEPARTMENT, employeeLookUp, getTitle(name, false));
        } else if (OvertimeListView.OVERTIME_DEPARTMENT_TYPE.equals(type)) {
            name = formPropertyMap.get(DEPARTMENT) != null && formPropertyMap.get(DEPARTMENT).isChanged() ? formPropertyMap.get(DEPARTMENT).getTitle() : wfmStrings.department();
            addField(DEPARTMENT, departmentLookUp, getTitle(name, false));
        } else if (OvertimeListView.OVERTIME_GROUP_EMPLOYEE_TYPE.equals(type)) {
            name = formPropertyMap.get(DEPARTMENT) != null && formPropertyMap.get(DEPARTMENT).isChanged() ? formPropertyMap.get(DEPARTMENT).getTitle() : wfmStrings.group();
            addField(DEPARTMENT, groupTypeLookUp, getTitle(name, false));
        }


        addField(CATEGORY, categoryLookUp, getTitle(formPropertyMap.get(CATEGORY) != null && formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category(), false));

        addField(DATE, datePicker, getTitle(formPropertyMap.get(DATE) != null && formPropertyMap.get(DATE).isChanged() ? formPropertyMap.get(DATE).getTitle() : wfmStrings.date(), false));

        addField(APPROVERS, approvers, getTitle(formPropertyMap.get(APPROVERS) != null && formPropertyMap.get(APPROVERS).isChanged() ? formPropertyMap.get(APPROVERS).getTitle() : wfmStrings.approvers(), false));

        addField(DEFAULT_HOUR, defaultHours, getTitle(formPropertyMap.get(DEFAULT_HOUR) != null && formPropertyMap.get(DEFAULT_HOUR).isChanged() ? formPropertyMap.get(DEFAULT_HOUR).getTitle() : wfmStrings.hours(), false));

        addField(OVERTIME_NUMBER, overtimeCode, getTitle(formPropertyMap.get(OVERTIME_NUMBER) != null && formPropertyMap.get(OVERTIME_NUMBER).isChanged() ? formPropertyMap.get(OVERTIME_NUMBER).getTitle() : wfmStrings.overtimeCode(), false));

        addField(OVERTIME_ITEM_TABLE, overtimeItemTable, null, false);

    }

    @Override
    protected void initPredefinedValues() {

    }
}
