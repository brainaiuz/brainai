package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.BackupEmployeeNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableForLeaveRequest;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.BackupsEmployeeObject;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ADMIN;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BACKUPS_EMPLOYEE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BACKUPS_EMPLOYEE_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PERCENTAGE;

public class BackupsEmployeeSummaryView extends CustomForm2 implements Colapse {

    private final WfmStrings wfmStrings = WfmStrings.App.get();

    private final Integer objectId;

    private BackupsEmployeeObject objectData;

    private LinkedHashMap<String, FormProperty> formPropertyMap;

    private HTML employeeLookUp, approver, positionLookUp, departmentLookUp, percentage, reason;

    private FormHasCustomField customFieldUtil;

    private String statusCode;

    private SplitButton printPdfSplitButton;

    private WfmButton2 submitButton, approveButton, declineButton, editButton;

    private MultiTableForLeaveRequest backupEmployeeTable;

    private BackupEmployeeNavBox backupEmployeeNavBox;

    private TextArea2 description;

    private KpiRadioButton yesSignature;

    private KpiRadioButton noSignature;

    private FlexTable signature;

    public BackupsEmployeeSummaryView(Integer objectId) {
        super("backupsEmployee", "backupsEmployeeSummery");
        this.objectId = objectId;
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
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                BackupsEmployeeSummaryView.super.onInitialize();
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
            SinksContainerFactory.entryPoint.onHistoryChanged("backupsEmployee|add/edit/" + objectData.getId());
        });
        editButton.setVisible(false);

        declineButton = addButton(wfmStrings.reject(), Constants.BTN_REJECT, clickEvent -> save(Constants.BACKUPS_EMPLOYEE_REJECTED));
        declineButton.setVisible(false);

        submitButton = addButton(Constants.BACKUPS_EMPLOYEE_REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), wfmStrings.submitForApproval(), Constants.BTN_DEFAULT_OUTLINE, clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.BACKUPS_EMPLOYEE_SUBMITTED);
        });
        submitButton.setVisible(false);

        approveButton = addButton(wfmStrings.approve(), WfmButton2.BTN_PRIMARY, clickEvent -> save(Constants.BACKUPS_EMPLOYEE_APPROVED));
        approveButton.setVisible(false);

//        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
//        addRightButton(printPdfSplitButton);

    }

    @Override
    protected void getDataToFillFields() {
        if (objectId == null) {
            return;
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(objectId);
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
                statusCode = objectData.getStatusCode();
                setDataToFields();
                getCustomFieldUtil().fillCustomFieldsWithData(backupsEmployeeObject.getCustomFieldItems(), true);
                initButtons();
                pdfTool(backupsEmployeeObject);
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
    protected void registerFields() {
        employeeLookUp = initHTML();
        departmentLookUp = initHTML();
        positionLookUp = initHTML();
        percentage = initHTML();
        reason = initHTML();

        description = new TextArea2(wfmStrings.description());
        description.setEnabled(false);

        backupEmployeeNavBox = new BackupEmployeeNavBox();

        yesSignature = new KpiRadioButton("signature", wfmStrings.yes());
        noSignature = new KpiRadioButton("signature", wfmStrings.no());
        noSignature.setEnabled(false);
        yesSignature.setEnabled(false);

        signature = new FlexTable();
        signature.addStyleName(DEFAULT_WIDTH + " " + "options-row");
        signature.setWidget(0, 0, yesSignature);
        signature.setWidget(0, 1, noSignature);


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
        backupEmployeeTable.setViewMode(false);
        backupEmployeeTable.setSummaryForm(true);
        backupEmployeeNavBox.setSummaryForm(true);
        backupEmployeeTable.isAddValidate(true);
        backupEmployeeNavBox.isValidate(false);
        backupEmployeeNavBox.popupForBackupEmployee.isVisibleMinusAndPlus(false);
        approver = initHTML();
        drawFields();
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        show();
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

    private void drawFields() {
        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());
        addField(BACKUPS_EMPLOYEE, employeeLookUp, wfmStrings.employee());
        addField(BACKUP_EMPLOYEE, backupEmployeeTable, wfmStrings.backupEmployee());
        addField(APPROVERS, approver, wfmStrings.approvers());
        addField(POSITION, positionLookUp, wfmStrings.position());
        addField(DEPARTMENT, departmentLookUp, wfmStrings.department());
        addField(DESCRIPTION, description, null);
        addField(PERCENTAGE, percentage, wfmStrings.percentage());
        addField(REASON, reason, wfmStrings.reason());
        addField(GRANT_SIGNING_AUTHORITY, signature, wfmStrings.grantSigningAuthority());
    }


    @Override
    protected void initPredefinedValues() {

    }

    private void setDataToFields() {
        employeeLookUp.setText(objectData.getSelectedEmployee() != null ? objectData.getSelectedEmployee().getName() : "");
        departmentLookUp.setText(objectData.getDepartment() != null ? objectData.getDepartment().getName() : "");
        positionLookUp.setText(objectData.getPosition() != null ? objectData.getPosition().getName() : "");
        description.setText(objectData.getDescription() != null ? objectData.getDescription() : "");
        percentage.setText(objectData.getPercentage() != null ? objectData.getPercentage() : "");
        if (objectData.getCustomReasonId() != null && objectData.getSelectedReason() == null) {
            reason.setText(getCustomReason(objectData.getCustomReasonId()));
        } else {
            reason.setText(objectData.getSelectedReason() != null ? objectData.getSelectedReason().getName() : "");
        }
        if (objectData.getIsNeedSignature() != null) {
            if (objectData.getIsNeedSignature().equals(Constants.YES)) {
                yesSignature.setVisible(true);
                yesSignature.setValue(true);
            } else {
                noSignature.setVisible(true);
                noSignature.setValue(true);
            }
        }
        if (objectData.getApproverEmployee() != null) {
            approver.setText(objectData.getApproverEmployee().getName());
        }
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

    private void initButtons() {
        if (objectData.isApprover()) {
            Info.show("THis is approver");
            Integer currentApproverId = objectData.getApproverEmployee() != null ? objectData.getApproverEmployee().getId() : null;
            Integer currentUserId = Utils.getUserID();
            if (BACKUPS_EMPLOYEE_APPROVED.equalsIgnoreCase(statusCode) || Constants.BACKUPS_EMPLOYEE_REJECTED.equals(statusCode)) {
                approveButton.setVisible(false);
                declineButton.setVisible(false);
                submitButton.setVisible(false);
            } else if (Utils.hasRole(ADMIN) || (Constants.BACKUPS_EMPLOYEE_SUBMITTED.equals(statusCode) && currentUserId.equals(currentApproverId))) {
                approveButton.setVisible(true);
                declineButton.setVisible(true);
                submitButton.setVisible(false);
            }

            editButton.setVisible(
                    Utils.hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_EDIT)
                            && !(Constants.BACKUPS_EMPLOYEE_APPROVED.equals(statusCode)) && (currentUserId.equals(objectData.getCreator().getId())));


            if (Constants.BACKUPS_EMPLOYEE_REJECTED.equals(statusCode) && objectData.getCreator() != null && currentUserId.equals(objectData.getCreator().getId())) {
                submitButton.setVisible(true);
            }
        } else {
            Info.warn("This is not approver");
            editButton.setVisible(Utils.hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_EDIT)
                    && !(Constants.BACKUPS_EMPLOYEE_APPROVED.equals(statusCode)));
        }
    }


    private void save(String statusCode) {
        objectData.setStatusCode(statusCode);
        deleteDublicateEmployee(objectData);
        LoadingPanel.loading(true);
        HrmsService.App.get().updateBackupsEmployeeItemsAndStatus(objectData, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Boolean updated) {
                if (!updated) {
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                    return;
                }
                closeTab();
                LoadingPanel.loading(false);
            }
        });
    }

    private void deleteDublicateEmployee(BackupsEmployeeObject employeeObject) {
        List<BackupEmployeeItem> backupEmployeeItems = new ArrayList<>();
        ArrayList<ApproverItemMini> childs = new ArrayList<>();

        for (BackupEmployeeItem backupEmployeeItem : employeeObject.getBackupsEmployees()) {
            ApproverItemMini parentBackupEmployee = backupEmployeeItem.getParentBackupEmployee();
            BackupEmployeeItem newItem = new BackupEmployeeItem();
            newItem.setParentBackupEmployee(parentBackupEmployee);
            for (ApproverItemMini approverItemMini : backupEmployeeItem.getChildList()) {
                if (!parentBackupEmployee.getExactEmployee().getId().equals(approverItemMini.getExactEmployee().getId())) {
                    childs.add(approverItemMini);
                }
            }
            newItem.setChildList(childs);
            backupEmployeeItems.add(newItem);
        }
        this.objectData.setBackupsEmployees(backupEmployeeItems);
    }


    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    public String getCustomReason(Integer id) {
        GWT.log("id: " + id);
        String name = "";
        if (id == null) {
            return name;
        }
        ArrayList<SelectItem> customReasons = new ArrayList<>(Collections.singletonList(
                new SelectItem(-99, wfmStrings.businessTrip())
        ));
        for (SelectItem item : customReasons) {
            if (Objects.equals(item.getId(), id)) {
                name = item.getName();
                break;
            }
        }
        return name;
    }

    public void pdfTool(BackupsEmployeeObject result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectId);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/backupEmployeeViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }
}
