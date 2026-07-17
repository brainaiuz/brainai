package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldArea;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.CustomiseFieldsPositionPopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * User: Normurod Buriev
 * Date: 7/22/11
 * Time: 3:08 PM
 */
public class EditCustomFieldView extends AbstractAddCustomFieldsView implements Colapse {


    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public EditCustomFieldView(String companyID, String customFieldArea) {
        super("addcustomfield", wfmStrings.customFields());
        this.companyID = companyID != null && !companyID.isEmpty() ? Integer.valueOf(companyID) : null;
        this.customFieldArea = (customFieldArea != null && customFieldArea.equals("null")) ? null : customFieldArea;
    }

    public EditCustomFieldView(Integer relationship) {
        super("addcustomfield", wfmStrings.customFields());
        this.relationship = relationship;
    }

    public EditCustomFieldView(Integer objectID, String companyID) {
        super("editcustomfield", wfmStrings.customFields());
        this.objectID = objectID;
        this.companyID = companyID != null && !companyID.isEmpty() ? Integer.valueOf(companyID) : null;
    }

    @Override
    protected String getFormName() {
        return this.objectID != null ? settingsStrings.editCustomField() : wfmStrings.addCustomField();
    }

    @Override
    protected SelectItem[] getRelatesToNames() {
        int index = 0;

        ArrayList<SelectItem> items = new ArrayList<>();

        if (relationship == null) {
            if (CustomFieldArea.CRM.name().equals(customFieldArea)) {
                items.add(new SelectItem(index++, Property.get(Constants.EVENT_LIST, wfmStrings.activity()), CustomFieldSection.Activity.name()));
                items.add(new SelectItem(index++, wfmStrings.contact(), CustomFieldSection.Contact.name()));
                items.add(new SelectItem(index++, wfmStrings.crmAccount(), CustomFieldSection.CrmAccount.name()));
                items.add(new SelectItem(index++, wfmStrings.crmCase(), CustomFieldSection.CrmCase.name()));
                items.add(new SelectItem(index++, settingsStrings.estimate(), CustomFieldSection.Estimate.name()));
                items.add(new SelectItem(index++, wfmStrings.lead(), CustomFieldSection.Lead.name()));
                items.add(new SelectItem(index++, Property.get(Constants.LOGACALL, wfmStrings.logCall()), CustomFieldSection.LogACall.name()));
                items.add(new SelectItem(index++, wfmStrings.opportunity(), CustomFieldSection.Opportunity.name()));
            } else if (CustomFieldArea.PM.name().equals(customFieldArea)) {
                if (Utils.isMonthlyTimeSheetEnable()) {
                    items.add(new SelectItem(index++, CustomFieldSection.Contract.getTitle(), CustomFieldSection.Contract.name()));
                }
                items.add(new SelectItem(index++, wfmStrings.issue(), CustomFieldSection.Issues.name()));
                items.add(new SelectItem(index++, wfmStrings.project(), CustomFieldSection.Project.name()));
                items.add(new SelectItem(index++, wfmStrings.task(), CustomFieldSection.Task.name()));
            } else if (CustomFieldArea.HRMS.name().equals(customFieldArea)) {
                items.add(new SelectItem(index++, hrmsStrings.businessGoal(), CustomFieldSection.BusinessGoal.name()));
                if (Utils.hasPermission(PermissionConstants.HRMS_RECRUITMENT)) {
                    items.add(new SelectItem(index++, wfmStrings.candidate(), CustomFieldSection.Candidate.name()));
                }
                items.add(new SelectItem(index++, hrmsStrings.companyGoal(), CustomFieldSection.CompanyGoal.name()));
                items.add(new SelectItem(index++, Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.departmentGoal(), wfmStrings.department()), CustomFieldSection.DepartmentGoal.name()));
                items.add(new SelectItem(index++, settingsStrings.dependent1(), CustomFieldSection.Dependent.name()));
                items.add(new SelectItem(index++, wfmStrings.employee(), CustomFieldSection.Employee.name()));
                items.add(new SelectItem(index++, wfmStrings.leaveRequest(), CustomFieldSection.LeaveRequest.name()));
                items.add(new SelectItem(index++, "BenefitRequest", CustomFieldSection.BenefitRequest.name()));
                items.add(new SelectItem(index++, wfmStrings.meetingMinutes(), CustomFieldSection.MeetingMInutesView.name()));
                items.add(new SelectItem(index++, wfmStrings.onboardingStep(), CustomFieldSection.OnboardingStep.name()));
                items.add(new SelectItem(index++, hrmsStrings.personalGoal(), CustomFieldSection.PersonalGoal.name()));
                if (Utils.hasPermission(PermissionConstants.HRMS_RECRUITMENT)) {
                    items.add(new SelectItem(index++, wfmStrings.placement(), CustomFieldSection.Placement.name()));
                }
                items.add(new SelectItem(index++, hrmsStrings.projectgoal(), CustomFieldSection.ProjectGoal.name()));
                if (Utils.hasPermission(PermissionConstants.HRMS_RECRUITMENT)) {
                    items.add(new SelectItem(index++, wfmStrings.vacancy(), CustomFieldSection.Vacancy.name()));
                }
                items.add(new SelectItem(index++, wfmStrings.certificates(), CustomFieldSection.Certificates.name()));
                items.add(new SelectItem(index++, wfmStrings.education(), CustomFieldSection.TalentProfileView.name()));
            } else if (CustomFieldArea.ACCOUNTING.name().equals(customFieldArea)) {
                items.add(new SelectItem(index++, wfmStrings.bankAccount(), CustomFieldSection.BankAccounts.name()));
                items.add(new SelectItem(index++, settingsStrings.bankTransfer(), CustomFieldSection.BankTransferList.name()));
                items.add(new SelectItem(index++, wfmStrings.prepayments(), CustomFieldSection.CustomerPrepayment.name()));
                items.add(new SelectItem(index++, wfmStrings.expenseReporting(), ViewName.ExpenceReportView.name()));
                items.add(new SelectItem(index++, wfmStrings.fixedAsset(), CustomFieldSection.FixedAsset.name()));
                items.add(new SelectItem(index++, settingsStrings.invoicePayment(), CustomFieldSection.BatchInvoicePaymentView.name()));
                items.add(new SelectItem(index++, settingsStrings.paidBills(), CustomFieldSection.BatchPayBillView.name()));
                items.add(new SelectItem(index++, wfmStrings.product(), CustomFieldSection.ProductServiceView.name()));
                items.add(new SelectItem(index++, wfmStrings.productCategory(), CustomFieldSection.ProductCategoryStoreFront.name()));
                items.add(new SelectItem(index++, wfmStrings.purchaseinvoice(), CustomFieldSection.PurchaseInvoice.name()));
                items.add(new SelectItem(index++, wfmStrings.purchaseorder(), CustomFieldSection.PurchaseOrder.name()));
                items.add(new SelectItem(index++, wfmStrings.requestForPurchase(), CustomFieldSection.RequestForPurchase.name()));
                items.add(new SelectItem(index++, wfmStrings.requestForQuote(), CustomFieldSection.RequestForQuote.name()));
                items.add(new SelectItem(index++, wfmStrings.salesInvoice(), CustomFieldSection.SaleInvoice.name()));
                items.add(new SelectItem(index++, accountingStrings.salesOrder(), CustomFieldSection.SaleOrder.name()));
                items.add(new SelectItem(index++, wfmStrings.salesQuote(), CustomFieldSection.SaleQuote.name()));
                items.add(new SelectItem(index++, wfmStrings.supplierCredits(), CustomFieldSection.SupplierPrepayment.name()));
                items.add(new SelectItem(index++, wfmStrings.rentalProducts(), CustomFieldSection.RentalProductsView.name()));
                items.add(new SelectItem(index++, wfmStrings.rentalOrders(), CustomFieldSection.RentalOrdersView.name()));
            } else if (CustomFieldArea.SETTINGS.name().equals(customFieldArea)) {
                items.add(new SelectItem(index++, wfmStrings.companySettings(), CustomFieldSection.CompanySettings.name()));
                items.add(new SelectItem(index++, wfmStrings.department(), CustomFieldSection.Department.name()));
                items.add(new SelectItem(index++, wfmStrings.position(), CustomFieldSection.Positions.name()));
                items.add(new SelectItem(index++, Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), CustomFieldSection.Location.name()));
                items.add(new SelectItem(index++, wfmStrings.brand(), CustomFieldSection.Brand.name()));
            } else if (CustomFieldArea.PAYROLL.name().equals(customFieldArea)) {
                items.add(new SelectItem(index++, wfmStrings.payslip(), CustomFieldSection.SinglePayrun.name()));
                items.add(new SelectItem(index++, wfmStrings.cashAdvance(), CustomFieldSection.CashAdvanceView.name()));
            }
        }

        if (objectID != null || relationship != null) {
            items.add(new SelectItem(index++, CustomFieldSection.ProductCategory.getTitle(), CustomFieldSection.ProductCategory.name()));
        }

        return items.toArray(new SelectItem[]{});
    }

    @Override
    protected void drawViewCustomFields(SelectItem nowSelect, SelectItem prevSelect) {
        if (nowSelect != null) {
            removeWidgets(prevSelect);
            addWidgets(nowSelect);
        } else {
            removeWidgets(prevSelect);
        }
    }

    /**
     * Remove widget add checked in Entity Type
     *
     * @param item - item
     */
    private void removeWidgets(SelectItem item) {
        if (item != null) {
            customFieldsContent.clear();
        }
    }

    /**
     * Add widget checked in Entity type
     *
     * @param item - item
     */
    private void addWidgets(SelectItem item) {
        if (customFieldArea == null) {//from backend
            if (ViewName.Task.name().equals(item.getName())) {
                getTaskWidgets();
            } else if (ViewName.Project.name().equals(item.getName())) {
                getProjectWidgets();
            }
        }
    }

    /**
     * Project Add Widget checked in entity Type
     */
    private void getProjectWidgets() {
        WfmButton2 addProjectFieldsPosition = new WfmButton2(wfmStrings.addProjectFieldsPosition());
        addProjectFieldsPosition.addClickHandler(be -> {
            CustomiseFieldsPositionPopup projectAddSortablePopup = new CustomiseFieldsPositionPopup(companyID, ViewAddFiledsCodeName.ProjectAdd, getProjectAddViewFieldsName(), true);
            projectAddSortablePopup.center();
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(10);
        buttonPanel.add(addProjectFieldsPosition);
        WfmForm customFieldsForm = new WfmForm(new String[]{"100%"});
        customFieldsForm.addTitleField(settingsStrings.addProjectViewFields());
        customFieldsForm.addWidget(buttonPanel);

        customFieldsContent.add(customFieldsForm);
    }

    /**
     * Add Project View Fields Names
     *
     * @return - fields
     */
    private ArrayList<String> getProjectAddViewFieldsName() {
        ArrayList<String> fieldsName = new ArrayList<>();
        fieldsName.add(0, wfmStrings.projectNumber());
        fieldsName.add(1, wfmStrings.projectParent());
        fieldsName.add(2, wfmStrings.projectName());
        fieldsName.add(3, wfmStrings.projectDescription());
        fieldsName.add(4, wfmStrings.startDate());
        fieldsName.add(5, wfmStrings.dueDate());
        fieldsName.add(6, wfmStrings.members());
        fieldsName.add(7, wfmStrings.projectManager());
        fieldsName.add(8, wfmStrings.backupManagers());
        fieldsName.add(9, Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        fieldsName.add(10, wfmStrings.status());
        fieldsName.add(11, wfmStrings.attachments());
        return fieldsName;
    }

    /**
     * Task Add Widget checked in entity Type
     */
    private void getTaskWidgets() {
        WfmButton2 addTaskFieldsPosition = new WfmButton2(wfmStrings.addTaskFieldsPosition());
        addTaskFieldsPosition.addClickHandler(be -> {
            CustomiseFieldsPositionPopup taskAddSortablePopup = new CustomiseFieldsPositionPopup(companyID, ViewAddFiledsCodeName.TaskAdd, getTaskAddViewFieldsName(), true);
            taskAddSortablePopup.center();
        });

        WfmButton2 addMultiTaskFieldsPosition = new WfmButton2(wfmStrings.addMultiTaskFieldsPosition());
        addMultiTaskFieldsPosition.addClickHandler(be -> {
            CustomiseFieldsPositionPopup multiTaskAddSortablePopup = new CustomiseFieldsPositionPopup(companyID, ViewAddFiledsCodeName.MultiTaskAdd, getMultiTaskAddViewFieldsName(), false);
            multiTaskAddSortablePopup.center();
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(10);
        buttonPanel.add(addTaskFieldsPosition);
        buttonPanel.add(addMultiTaskFieldsPosition);
        WfmForm customFieldsForm = new WfmForm(new String[]{"30%", "70%"});
        customFieldsForm.addTitleField(settingsStrings.addTaskViewFieldsSortable());
        customFieldsForm.addWidget(buttonPanel);

        customFieldsContent.add(customFieldsForm);
    }

    /**
     * Add Task View Fields Name
     *
     * @return - fields
     */
    private ArrayList<String> getTaskAddViewFieldsName() {
        ArrayList<String> fieldsName = new ArrayList<>();
        fieldsName.add(0, wfmStrings.project());
        fieldsName.add(1, wfmStrings.number());
        fieldsName.add(2, wfmStrings.taskName());
        fieldsName.add(3, wfmStrings.taskDescription());
        fieldsName.add(4, wfmStrings.startDate());
        fieldsName.add(5, wfmStrings.dueDate());
        fieldsName.add(6, wfmStrings.assignees());
        fieldsName.add(7, wfmStrings.billable());
        fieldsName.add(8, wfmStrings.priority());
        fieldsName.add(9, wfmStrings.status());
        fieldsName.add(10, wfmStrings.timeSpent());
        fieldsName.add(11, wfmStrings.attachment());
        return fieldsName;
    }

    /**
     * Add Multi Task View
     *
     * @return - fields
     */
    private ArrayList<String> getMultiTaskAddViewFieldsName() {
        ArrayList<String> fieldsName = new ArrayList<>();
        fieldsName.add(0, wfmStrings.taskName());
        fieldsName.add(1, wfmStrings.taskDescription());
        fieldsName.add(2, wfmStrings.assignees());
        fieldsName.add(3, wfmStrings.startDate());
        fieldsName.add(4, wfmStrings.dueDate());
        fieldsName.add(5, wfmStrings.priority());
        fieldsName.add(6, wfmStrings.billable());
        fieldsName.add(7, wfmStrings.attachment());
        return fieldsName;
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