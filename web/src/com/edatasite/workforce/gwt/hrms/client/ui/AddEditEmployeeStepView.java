package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.hrms.client.localization.HrmsMessages;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.edatasite.workforce.gwt.profile.client.ui.view.WorkflowDateSelecter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Azazello on 7/19/15.
 */
public class AddEditEmployeeStepView extends CustomForm2 implements CustomFormConstants, Constants, Colapse {
    private static final HrmsServiceAsync hrmsService = HrmsService.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final HrmsMessages hrmsMessages = HrmsMessages.App.get();
    private final Integer objectID;
    private final Integer stepID;
    private Integer lastEmployeeID;
    private final String formID;
    private String stepName;
    private Integer workflowID;
    private EmployeeStepItem item;
    private KpiRadioButton employeeButton;
    private KpiRadioButton candidateButton;
    private HorizontalPanel typePanel;
    private CRMLookUp employee;
    private ChosenApproversWidget approver;
    private DataListBox status;
    private VerticalPanel expenses;
    private VerticalPanel linkPanel;
    private VerticalPanel linkPanelCerts;
    private final String addEditEmployee_StepView = "add_edit_employee_step_view_";
    private boolean isApprove = true;
    private WfmButton2 approveButton;
    private WfmButton2 rejectButton;
    private FormHasCustomField customFieldUtil;
    private KpiCheckBox workflowTimeBasedAction;
    private WorkflowDateSelecter workflowTimeBasedActionDate;

    public AddEditEmployeeStepView(Integer objectID, Integer stepID, String formID) {
        super("addemployeestep", hrmsStrings.employeeStep());
        this.objectID = objectID;
        this.stepID = stepID;
        this.formID = formID;
    }

    public AddEditEmployeeStepView(Integer objectID, Integer stepID, String formID, Integer workflowID) {
        super("addemployeestep", hrmsStrings.employeeStep());
        this.objectID = objectID;
        this.stepID = stepID;
        this.formID = formID;
        this.workflowID = workflowID;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyStepCategoryCustomFields(stepID, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                AddEditEmployeeStepView.super.onInitialize();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                }
                AddEditEmployeeStepView.super.onInitialize();
            }
        });
        return null;
    }

    protected void registerFields() {
        //Type
        employeeButton = new KpiRadioButton("type");
        employeeButton.setText(wfmStrings.employee());
        employeeButton.setValue(true);
        employeeButton.addValueChangeHandler(e -> {
            employee.clearAndClearItems();
            employee.setType(LookUpConstants.EMPLOYEE_ID);
            employee.getFilterParametrs().setCRM(false);
            employee.getFilterParametrs().setHRMS(true);
            employee.refreshOracle(true);
        });
        candidateButton = new KpiRadioButton("type");
        candidateButton.setText(wfmStrings.candidate());
        candidateButton.addValueChangeHandler(e -> {
            employee.clearAndClearItems();
            employee.setType(LookUpConstants.CANDIDATE_ID);
            employee.getFilterParametrs().setCRM(true);
            employee.getFilterParametrs().setHRMS(false);
            employee.refreshOracle(true);
            lastEmployeeID = null;
            linkPanel.clear();
        });
        typePanel = new HorizontalPanel();
        typePanel.setSpacing(3);
        typePanel.addStyleName(DEFAULT_WIDTH);
        typePanel.add(employeeButton);
        typePanel.add(candidateButton);
        employee = new CRMLookUp(LookUpConstants.HRMS_EMPLOYEE);
//        employee.setBeforeSearch(new Command() {
//            @Override
//            public void execute() {
//                employee.getFilterParametrs().setStepID(stepID);
//                employee.getFilterParametrs().setObjectId(objectID);
//            }
//        });
        employee.addStyleName(DEFAULT_WIDTH);
        employee.ensureDebugId(addEditEmployee_StepView + "employee");
        employee.getSuggestBox().addSelectionHandler(e -> {
            if (employeeButton.getValue()) {
                if (e.getSelectedItem() != null) {
                    Integer id = employee.getOracle().getItemID(e.getSelectedItem().getDisplayString());
                    if (lastEmployeeID == null || !lastEmployeeID.equals(id)) {
                        lastEmployeeID = id;
                        linkPanel.clear();
                    }
                } else {
                    lastEmployeeID = null;
                    linkPanel.clear();
                }
                approver.updateLookUps(lastEmployeeID);
            }
        });
        employee.getSuggestBox().addBlurHandler(e -> {
            if (employeeButton.getValue()) {
                if (employee.getOracle().getItemID(employee.getText()) != null) {
                    Integer id = employee.getOracle().getItemID(employee.getText());
                    if (lastEmployeeID == null || !lastEmployeeID.equals(id)) {
                        lastEmployeeID = id;
                        linkPanel.clear();
                    }
                } else {
                    lastEmployeeID = null;
                    linkPanel.clear();
                }
                approver.updateLookUps(lastEmployeeID);
            }
        });

        approver = new ChosenApproversWidget(formID, objectID);
        approver.ensureDebugId(addEditEmployee_StepView + "approver");
        approver.addStyleName(DEFAULT_WIDTH);

        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
        status.ensureDebugId(addEditEmployee_StepView + "status");

        workflowTimeBasedAction = new KpiCheckBox(wfmStrings.executionTime());
        workflowTimeBasedActionDate = new WorkflowDateSelecter(false, true);
        workflowTimeBasedActionDate.setVisible(false);
        workflowTimeBasedActionDate.setWidth("500px");
        workflowTimeBasedAction.addValueChangeHandler(e -> workflowTimeBasedActionDate.setVisible(e.getValue()));
        VerticalPanel vPanel = new VerticalPanel();
        vPanel.add(Utils.getInHorizontalPanel(5, 100, false, workflowTimeBasedAction, workflowTimeBasedActionDate));
        if (workflowID != null) {
            addField(WORKFLOW_ACTIONS, vPanel, getTitle(wfmStrings.addOnboardingStepWorkflow()));
        }


        expenses = new VerticalPanel();
        expenses.setSpacing(2);

        WfmButton2 addExpense = new WfmButton2(wfmStrings.addMess());
        addExpense.addClickHandler(e -> {
            if (employeeButton.getValue() || candidateButton.getValue()) {
                if (employee.getSelectedItem() == null) {
                    Info.show("Choose an employee please", Info.Type.WARNING);
                } else {
                    String candidate = candidateButton.getValue() ? "/candidate" : "";
                    SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|add/add/HRMS_LIST/" + employee.getSelectedItemID() + "/" + employee.getSelectedItem().getName() + candidate);
                }
            } else {
                Info.show("Choose an employee type please", Info.Type.WARNING);
            }
        });
        expenses.add(addExpense);
        linkPanel = new VerticalPanel();
        expenses.add(linkPanel);
        linkPanelCerts = new VerticalPanel();

        addTitleField(GENERAL_INFORMATION, wfmStrings.generalInformation());
        if (Utils.hasPermission(HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION)) {
            if (workflowID == null) {
                addField(TYPE, typePanel, getTitle(wfmStrings.type(), true));
                addField(EMPLOYEE, employee, getTitle(wfmStrings.employee(), true));
                addField(EXPENSE_CLAIM, expenses, getTitle(wfmStrings.expenseClaims()));
            }
            addField(STATUS, status, getTitle(wfmStrings.status(), true));
        }
        addField(APPROVERS, approver, getTitle(wfmStrings.approver()));
        getCustomFieldUtil().drawCustomFields(this, objectID);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_STEP_EXPENSEREPORT_SAVED, AddEditEmployeeStepView.this, (sender, args) -> {
            ExpenseReportsListItem item = (ExpenseReportsListItem) args;
            final SimpleLink link = new SimpleLink(item.getTitle());
            link.setLayoutData(item.getId());
            link.addClickHandler(e -> SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + link.getLayoutData() + "/" + Constants.EXPENSE_VIEW));
            linkPanel.add(link);
        });
        show();
        if (workflowID != null) {
            Element element = DOM.getElementById("gwt-debug-Upload buttom");
            if (element != null) {
                element.setAttribute("style", "display: none;");
            }
        }
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(STATUS, item.getStatuses());
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            if (GENERAL_INFORMATION.equals(fieldID)) {
                return wfmStrings.generalInformation();
            } else if (TYPE.equals(fieldID)) {
                return wfmStrings.type();
            } else if (STATUS.equals(fieldID)) {
                return wfmStrings.status();
            } else if (WORKFLOW_ACTIONS.equals(fieldID) && workflowID != null) {
                return wfmStrings.addOnboardingStepWorkflow();
            } else if (EMPLOYEE.equals(fieldID)) {
                return wfmStrings.employee();
            } else if (EXPENSE_CLAIM.equals(fieldID)) {
                return wfmStrings.expenseClaims();
            } else if (ADDITIONAL_INFORMATION.equals(fieldID)) {
                return wfmStrings.additionalInformation();
            } else if (LINKED_CERTIFICATES.equals(fieldID)) {
                return wfmStrings.certificates();
            }
        }

        return null;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), e -> save(false));

        if (objectID != null && workflowID == null) {
            WfmButton2 pdf = new WfmButton2(wfmStrings.pdfVersion(), null, "icon-pdf");
            pdf.addClickHandler(e -> save(true));
            addButton(pdf);

            approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_SUCCESS);
            approveButton.setVisible(false);
            approveButton.addClickHandler(e -> {
                isApprove = true;
                save(false);
            });
            addButton(approveButton);

            rejectButton = new WfmButton2(wfmStrings.reject(), WfmButton2.BTN_REJECT);
            rejectButton.setVisible(false);
            rejectButton.addClickHandler(e -> {
                isApprove = false;
                save(false);
            });
            addButton(rejectButton);

        }

    }

    private void save(final boolean pdf) {
        if (!validate()) {
            return;
        }
        item = item == null ? new EmployeeStepItem() : item;
        item.setEmployeeID(employee.getSelectedItemID());
        item.setApprovers(approver.getChosenApprovers());
        item.setTypeCode(employeeButton.getValue() ? EmployeeStepItem.EMPLOYEE_TYPE : EmployeeStepItem.CANDIDATE_TYPE);
        item.setStatusID(status.getSelectedId());
        if (item.isHasApprover()) {
            if ((item.getAppoveStatusId() != null && item.getRejectStatusId() != null) && item.isCanApprove()) {
                if (isApprove) {
                    item.setStatusID(item.getAppoveStatusId());
                } else {
                    item.setStatusID(item.getRejectStatusId());
                }
            }
        }

        item.setWorkflowID(workflowID);

        item.setWorkflowActionTimeBased(workflowTimeBasedAction.getValue());
        item.setWorkflowActionStartTime(workflowTimeBasedActionDate.getWorkflowStartDate());
        item.setWorkflowActionStartTimeUnit(workflowTimeBasedActionDate.getWorkflowDueDateUnit());
        item.setWorkflowActionStartTimeGranularity(workflowTimeBasedActionDate.getWorkflowDueDateGranularity());

        item.getExpenses().clear();
        if (linkPanel.getWidgetCount() > 0) {
            for (int i = 0; i < linkPanel.getWidgetCount(); i++) {
                SimpleLink link = (SimpleLink) linkPanel.getWidget(i);
                item.getExpenses().add(new SelectItem((Integer) link.getLayoutData(), link.getText()));
            }
        }
        item.setStepID(stepID);
        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        LoadingPanel.loading(true);
        hrmsService.saveEmployeeStep(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                Info.show(hrmsMessages.hasBeenAddedSucc(stepName), Info.Type.INFO);
                if (pdf) {
                    String pdfURL = CommandConstants.PDF_URL + "/employeeStepViewPDFHandler";
                    RequestObject requestObject = new RequestObject(objectID);
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
                }
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_STEP_ADD_EDIT_DELETE, result, AddEditEmployeeStepView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_EMPLOYEE_STEP_UPDATE, result, AddEditEmployeeStepView.this);
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (workflowID == null) {
            errors += markAsError(EMPLOYEE, employee, employee.getSelectedItem() == null);
            errors += getCustomFieldUtil().validateCustomFields();
        }
        if (item.isHasApprover()) {
            if (!approver.isValid()) {
                errors++;
            }
        } else {
            errors += markAsError(STATUS, status, status.getSelectedId() == null);
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        hrmsService.getEmployeeStep(objectID, stepID, new AbstractAsyncCallback<EmployeeStepItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(EmployeeStepItem result) {
                LoadingPanel.loading(false);
                item = result;
                stepName = item.getStepName();
                fillFiels();
                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());
                if (workflowID != null) {
                    Element element = DOM.getElementById("gwt-debug-Upload buttom");
                    if (element != null) {
                        element.setAttribute("style", "display: none;");
                    }
                }
            }
        });

    }

    private void fillFiels() {
        initPredefinedValues();
        status.setEnabled(!item.isHasApprover());
        if (item.isCanApprove() && workflowID == null) {
            approveButton.setVisible(item.getStatusID() == null || !item.getStatusID().equals(item.getAppoveStatusId()));
            rejectButton.setVisible(item.getStatusID() == null || !item.getStatusID().equals(item.getRejectStatusId()));
        }
        status.setItems(item.getStatuses());

        if (item.getStatuses().length == 1) {
            status.setSelected(item.getStatuses()[0]);
        }

        workflowTimeBasedAction.setValue(item.isWorkflowActionTimeBased(), true);
        workflowTimeBasedActionDate.setStartDate(item.getWorkflowActionStartTime());
        workflowTimeBasedActionDate.setDueDate(item.getWorkflowActionStartTimeUnit());
        workflowTimeBasedActionDate.setDueDateGranularity(item.getWorkflowActionStartTimeGranularity());

        if (item.getTypeID() != null && item.getTypeCode() != null && EmployeeStepItem.CANDIDATE_TYPE.equals(item.getTypeCode())) {
            employeeButton.setValue(false);
            candidateButton.setValue(true);
            employee.clearAndClearItems();
            employee.setType(LookUpConstants.CANDIDATE_ID);
            employee.getFilterParametrs().setCRM(true);
            employee.getFilterParametrs().setHRMS(false);
            employee.refreshOracle(true);
        }
        if (item.getEmployeeID() != null) {
            employee.setSelected(item.getEmployeeID(), Utils.getUserID().equals(item.getEmployeeID()) && EmployeeStepItem.EMPLOYEE_TYPE.equals(item.getTypeCode()) ? item.getEmployeeName() + " (" + wfmStrings.myself() + ")" : item.getEmployeeName());
        } else if (objectID == null && item.getCurrentUserID() != null){
            employee.setSelected(item.getCurrentUserID(), item.getCurrentUserName());
            approver.updateLookUps(item.getCurrentUserID());
        } else if (objectID == null && Utils.getUserID() != null){
            employee.setSelected(Utils.getUserID(), Utils.getUserFullName());
            approver.updateLookUps(Utils.getUserID());
        }
        if (item.getObjectID() != null && item.getStatusID() != null) {
            status.setSelected(item.getStatusID());
        }
        if (item.getExpenses().size() > 0) {
            for (SelectItem it : item.getExpenses()) {
                final SimpleLink link = new SimpleLink(it.getName());
                link.setLayoutData(it.getId());
                link.addClickHandler(e -> SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + link.getLayoutData() + "/" + Constants.EXPENSE_VIEW));
                linkPanel.add(link);
            }
        }
        for (SelectItem it : item.getLinkedCertificates()) {
            final SimpleLink link = new SimpleLink(it.getName());
            link.setLayoutData(it.getId());
            link.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("certificate|summary/" + it.getId()));
            linkPanelCerts.add(link);
        }
        if (Utils.hasPermission(HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION) && item.getLinkedCertificates().size() > 0) {
            addField(LINKED_CERTIFICATES, linkPanelCerts, getTitle(wfmStrings.certificates()));
        }
    }

    @Override
    protected String getFormID() {
        return formID;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
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
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
