package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.BackupEmployeeNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableForLeaveRequest;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.BackupsEmployeeObject;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BACKUPS_EMPLOYEE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PERCENTAGE;

public class BackupsEmployeeAddEditView extends CustomForm2 implements Colapse {

    private final WfmStrings wfmStrings = WfmStrings.App.get();

    private Integer objectId;
    private BackupsEmployeeObject objectData;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private NumberData numberData;
    private Numbering numbering;
    private EmployeeLookUpWithCode employeeLookUp;
    private DepartmentLookUp departmentLookUp;
    private PositionLookUp positionLookUp;

    private WfmButton2 approveButton, submitButton;
    private ChosenApproversWidget approvers;
    private FormHasCustomField customFieldUtil;
    private MultiTableForLeaveRequest backupEmployeeTable;
    private BackupEmployeeNavBox backupEmployeeNavBox;
    private TextArea2 description;
    private DataListBox percentage;
    private KpiRadioButton yesSignature;
    private KpiRadioButton noSignature;
    private FlexTable signature;
    private DataListBox reason;

    public BackupsEmployeeAddEditView(Integer id) {
        super("addBackupsEmployee", "backupsEmployeeAdd ");
        this.objectId = id;
    }

    public BackupsEmployeeAddEditView() {
        super("addBackupsEmployee", "backupsEmployeeAdd");
    }

    @Override
    protected void registerFields() {

        String debugId = "addEditBackupsEmployeeView";

        employeeLookUp = new EmployeeLookUpWithCode();
        employeeLookUp.setEnsureDebugId(debugId + "employeeLookUp");
        employeeLookUp.getSuggestBox().addSelectionHandler(event -> {
            if (employeeLookUp.isSelected()) {
                refreshReasos(employeeLookUp.getSelectedItemID());
            }
        });

        departmentLookUp = new DepartmentLookUp();
        departmentLookUp.setEnsureDebugId(debugId + "departmentLookUp");

        positionLookUp = new PositionLookUp();
        positionLookUp.setEnsureDebugId(debugId + "positionLookUp");

        approvers = new ChosenApproversWidget(RelationItem.TYPE_BACKUPS_EMPLOYEE, objectId);
        approvers.ensureDebugId(debugId + "approvers");

        backupEmployeeNavBox = new BackupEmployeeNavBox();
        backupEmployeeTable = new MultiTableForLeaveRequest(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgets(null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> widgetsMap : backupEmployeeTable.getWidgets()) {
                    EmployeeLookUpWithCode backupEmployee = (EmployeeLookUpWithCode) widgetsMap.get(MultiTable.LOOK_UP_BOX);
                    if (backupEmployee.getSelectedItem() != null) {
                        return true;
                    }
                }
                return false;
            }
        }, false, backupEmployeeNavBox);
        backupEmployeeTable.isAddValidate(true);
        backupEmployeeNavBox.isValidate(true);
        backupEmployeeNavBox.popupForBackupEmployee.isVisibleMinusAndPlus(false);
        numbering = new Numbering();
        numbering.setEnabled(false);
        if (objectId == null) {
            generateNewNumber();
        }
        numbering.ensureDebugId(debugId + " numbering");

        percentage = new DataListBox();
        SelectItem[] percentages = new SelectItem[7];
        percentages[0] = new SelectItem(1, "0%");
        percentages[1] = new SelectItem(2, "10%");
        percentages[2] = new SelectItem(3, "20%");
        percentages[3] = new SelectItem(4, "30%");
        percentages[4] = new SelectItem(5, "40%");
        percentages[5] = new SelectItem(6, "50%");
        percentage.setItems(percentages);
        percentage.setSelected(percentages[3]);

        description = new TextArea2(5000, wfmStrings.description());

        yesSignature = new KpiRadioButton("signature", wfmStrings.yes());
        noSignature = new KpiRadioButton("signature", wfmStrings.no());

        signature = new FlexTable();
        signature.addStyleName(DEFAULT_WIDTH + " " + "options-row");
        signature.setWidget(0, 0, yesSignature);
        signature.setWidget(0, 1, noSignature);

        reason = new DataListBox();

        this.getCustomFieldUtil().drawCustomFields(this, this.objectId);

        initButtonsPanel();

        initHandlers();
        drawFields();
        show();
    }

    private void initHandlers() {

        if (approvers.getApproversSize()==0){
            Info.show(wfmStrings.pleaseConfigureApproverProccess());
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, BackupsEmployeeAddEditView.this, (sender, args) -> {
            if (approvers.getFirstApproverLookUp() != null) {
                approvers.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        GWT.log("event: approver event");
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        GWT.log("event: submitter event");
                        submitButton.setVisible(true);
                        approveButton.setVisible(false);
                    }
                });
                if (approvers.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        GWT.log("init: approver");
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        GWT.log("init: submitter");
                        approveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            } else {
                approveButton.setVisible(false);
                submitButton.setVisible(true);
            }
        });
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getBackupsEmployee(objectId, new AsyncCallback<BackupsEmployeeObject>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(BackupsEmployeeObject backupsEmployeeObject) {
                LoadingPanel.loading(false);
                objectData = backupsEmployeeObject;
                setDataToFields();
            }
        });

    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BACKUPS_EMPLOYEE_FORM;
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
    public String getIconStyle() {
        return null;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void drawFields() {

        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());

        boolean isRequired = formPropertyMap.get(EMPLOYEE) != null && formPropertyMap.get(EMPLOYEE).isRequired();
        boolean isBackupEmployeeRequired = formPropertyMap.get(EMPLOYEES) != null && formPropertyMap.get(EMPLOYEES).isRequired();
        boolean isPositionRequired = formPropertyMap.get(POSITION) != null && formPropertyMap.get(POSITION).isRequired();
        boolean isDepartmentRequired = formPropertyMap.get(DEPARTMENT) != null && formPropertyMap.get(DEPARTMENT).isRequired();
        boolean isPercentageRequired = formPropertyMap.get(PERCENTAGE) != null && formPropertyMap.get(PERCENTAGE).isRequired();
        boolean isDescriptionRequired = formPropertyMap.get(DESCRIPTION) != null && formPropertyMap.get(DESCRIPTION).isRequired();
        boolean isSignatureRequired = formPropertyMap.get(GRANT_SIGNING_AUTHORITY) != null && formPropertyMap.get(GRANT_SIGNING_AUTHORITY).isRequired();
        boolean isReasonRequired = formPropertyMap.get(REASON) != null && formPropertyMap.get(REASON).isRequired();


        addField(BACKUPS_EMPLOYEE, employeeLookUp, getTitle(wfmStrings.employee(), isRequired));
        addField(BACKUP_EMPLOYEE, backupEmployeeTable, wfmStrings.backupEmployee(), isBackupEmployeeRequired);
        addField(APPROVERS, approvers, wfmStrings.approver());
        addField(POSITION, positionLookUp, getTitle(wfmStrings.position(), isPositionRequired));
        addField(DEPARTMENT, departmentLookUp, getTitle(wfmStrings.department(), isDepartmentRequired));
        addField(PERCENTAGE, percentage, getTitle(wfmStrings.percentage(), isPercentageRequired));
        addField(DESCRIPTION, description, null, isDescriptionRequired);
        addField(GRANT_SIGNING_AUTHORITY, signature, wfmStrings.grantSigningAuthority(), isSignatureRequired);
        addField(REASON, reason, wfmStrings.reason(), isReasonRequired);


    }

    private void save(String status) {
        if (!validation()) {
            Info.warn(wfmStrings.fillAllRequiredFields());
            return;
        }

        setValues();
        objectData.setStatusCode(status);
        LoadingPanel.loading(true);
        HrmsService.App.get().saveBackupsEmployee(objectData, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer objectId) {
                closeTab();
//                SinksContainerFactory.entryPoint.onHistoryChanged("backupsEmployee|add/summary/" + objectId);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.backupEmployee()));
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BACKUPS_EMPLOYEE_ADD, objectData, BackupsEmployeeAddEditView.this);
            }
        });
    }

    private WidgetsMap getWidgets(ApproverItemMini employee) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final TextBox id = new TextBox();
        final EmployeeLookUpWithCode backupEmployees = new EmployeeLookUpWithCode();
        id.setVisible(false);
        if (employee != null) {
            backupEmployees.setSelected(employee.getExactEmployee().getId());
            id.setText(employee.getObjectID().toString());
        }
        widgetsMap.add("id", id);
        widgetsMap.add("employee", backupEmployees);
        return widgetsMap;
    }

    private void setValues() {
        if (objectData == null) {
            objectData = new BackupsEmployeeObject();
        }
        objectData.setId(objectId);
        objectData.setSelectedEmployee(employeeLookUp.getSelectedItem());
        objectData.setPosition(positionLookUp.getSelectedItem());
        objectData.setDepartment(departmentLookUp.getSelectedItem());
        objectData.setApprovers(approvers.getChosenApprovers());
        objectData.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        objectData.setNumberData(numbering.getNumberData(false));
        List<BackupEmployeeItem> allBackupEmployeeData = backupEmployeeNavBox.getAllBackupEmployeeData();
        if (allBackupEmployeeData != null && allBackupEmployeeData.size() > 0) {
            objectData.setBackupsEmployees(allBackupEmployeeData);
        }
        objectData.setDescription(description.getText());
        if (percentage.isSomethingSelected()) {
            objectData.setPercentage(percentage.getSelectedValue().getName());
        }
        if (yesSignature.getValue()) {
            objectData.setIsNeedSignature(Constants.YES);
        }
        if (noSignature.getValue()) {
            objectData.setIsNeedSignature(Constants.No);
        }
            objectData.setReasonsId(reason.getSelectedId());
    }

    private void initButtonsPanel() {
        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.addClickHandler(clickEvent -> save(Constants.BACKUPS_EMPLOYEE_SUBMITTED));
        submitButton.setVisible(false);
        addRightButton(submitButton);

        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
        approveButton.addClickHandler(clickEvent -> save(Constants.BACKUPS_EMPLOYEE_APPROVED));
        approveButton.setVisible(false);
        addRightButton(approveButton);
    }

    private boolean validation() {

        int error = 0;
        boolean isValidEmployee = formPropertyMap != null && formPropertyMap.get(EMPLOYEE) != null && formPropertyMap.get(EMPLOYEE).isRequired();
        boolean isValidBackup = formPropertyMap != null && formPropertyMap.get(EMPLOYEES) != null && formPropertyMap.get(EMPLOYEES).isRequired();
        boolean isValidDepartment = formPropertyMap != null && formPropertyMap.get(DEPARTMENT) != null && formPropertyMap.get(DEPARTMENT).isRequired();
        boolean isValidPosition = formPropertyMap != null && formPropertyMap.get(POSITION) != null && formPropertyMap.get(POSITION).isRequired();
        boolean isPercentageRequired = formPropertyMap != null && formPropertyMap.get(PERCENTAGE) != null && formPropertyMap.get(PERCENTAGE).isRequired();
        boolean isDescriptionRequired = formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null && formPropertyMap.get(DESCRIPTION).isRequired();
        boolean isSignatureRequired = formPropertyMap != null && formPropertyMap.get(GRANT_SIGNING_AUTHORITY) != null && formPropertyMap.get(GRANT_SIGNING_AUTHORITY).isRequired();
        boolean isReasonRequired = formPropertyMap != null && formPropertyMap.get(REASON) != null && formPropertyMap.get(REASON).isRequired();

        for (WidgetsMap widgetsMap : backupEmployeeTable.getWidgetsMaps()) {
            EmployeeLookUpWithCode employeeLookUp = (EmployeeLookUpWithCode) widgetsMap.getWidget("employee");
            error += markAsError(employeeLookUp, isValidBackup && !Validation.validateLookUpRequired(employeeLookUp));
        }
        error += markAsError(employeeLookUp, isValidEmployee && !Validation.validateLookUpRequired(employeeLookUp));
        error += markAsError(departmentLookUp, isValidDepartment && !Validation.validateLookUpRequired(departmentLookUp));
        error += markAsError(positionLookUp, isValidPosition && !Validation.validateLookUpRequired(positionLookUp));
        error += markAsError(percentage, isPercentageRequired && !Validation.validateDataListBoxRequired(percentage));
        error += markAsError(reason, isReasonRequired && !Validation.validateDataListBoxRequired(reason));
        error += markAsError(description, isDescriptionRequired && !Validation.validateTextAreaRequired(description));
        error += markAsError(signature, isSignatureRequired && !yesSignature.getValue() && !noSignature.getValue());
        error += this.getCustomFieldUtil().validateCustomFields();
        if (!validateBackupEmployee()) {
            return false;
        }
        return error == 0;
    }

    private boolean validateBackupEmployee() {
        boolean validated = true;
        if (backupEmployeeTable != null) {
            for (HashMap<String, Widget> widget : backupEmployeeTable.getWidgets()) {
                EmployeeLookUpWithCode employee = (EmployeeLookUpWithCode) widget.get("employee");
                if (employee == null || employee.getSelectedItemID() == null) return false;
                if (employee.getSelectedItemID() != null && objectId == null) {
                    if (!backupEmployeeNavBox.validateFields(true)) {
                        validated = false;
                    }
                }
            }
        }
        return validated;
    }

    private void generateNewNumber() {
        HrmsService.App.get().generateBackupsEmployeeCode(new AbstractAsyncCallback<NumberData>() {
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

    private void setDataToFields() {
        positionLookUp.setSelected(objectData.getPosition());
        departmentLookUp.setSelected(objectData.getDepartment());
        description.setText(objectData.getDescription());
        SelectItem selected = new SelectItem(null, objectData.getPercentage());
        percentage.setSelected(selected);
        if (objectData.getIsNeedSignature() != null) {
            if (objectData.getIsNeedSignature().equals(Constants.YES)) {
                yesSignature.setVisible(true);
                yesSignature.setValue(true);
            } else {
                noSignature.setVisible(true);
                noSignature.setValue(true);
            }
        }
        reason.setItems(objectData.getReasons());
        reason.addListItem(new SelectItem(-99, wfmStrings.businessTrip())); //for custom reason
        if (objectData.getCustomReasonId() != null && objectData.getSelectedReason() == null) {
            reason.setSelected(getCustomReason(objectData.getCustomReasonId()));
        } else {
            reason.setSelected(objectData.getSelectedReason());
        }

        getCustomFieldUtil().fillCustomFieldsWithData(objectData.getCustomFieldItems());

        if (objectData.getBackupsEmployees() != null) {
            backupEmployeeNavBox.setLeaveRequestSummaryValues(objectData.getBackupsEmployees());
            backupEmployeeTable.removeAllRows();
            int index = 0;
            for (BackupEmployeeItem item : objectData.getBackupsEmployees()) {
                ApproverItemMini parent = item.getParentBackupEmployee();
                EmployeeLookUpWithCode withCode = new EmployeeLookUpWithCode();
                WidgetsMap widgetsMap = new WidgetsMap();
                widgetsMap.addWidgets(withCode);
                widgetsMap.add("employee", withCode);
                backupEmployeeTable.addWidgets(widgetsMap);
                EmployeeLookUpWithCode c = (EmployeeLookUpWithCode) backupEmployeeTable.getWidgetsMaps().get(index).getWidget("employee");
                c.setSelected(parent.getExactEmployee());
                index++;
            }
        }
    }

    private void refreshReasos(Integer employeeId) {
        AvailabilityService.App.get().getReasons(employeeId, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                if (result != null && result.length > 0) {
                    reason.setItems(result);
                }
            }
        });
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
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.BackupsEmployee, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (getCustomFieldUtil() != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
                BackupsEmployeeAddEditView.super.onInitialize();
            }
        });
        return null;
    }

    public SelectItem getCustomReason(Integer id) {
        GWT.log("id: " + id);
        SelectItem result = new SelectItem();
        if (id == null) {
            return result;
        }
        ArrayList<SelectItem> customReasons = new ArrayList<>(Collections.singletonList(
                new SelectItem(-99, wfmStrings.businessTrip())
        ));
        for (SelectItem item : customReasons) {
            if (Objects.equals(item.getId(), id)) {
                result = new SelectItem(item.getId(), item.getName());
                break;
            }
        }
        return result;
    }


}
