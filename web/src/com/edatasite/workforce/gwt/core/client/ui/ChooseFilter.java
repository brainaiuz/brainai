/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/6 4:27:31                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.client.ui;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

public class ChooseFilter extends Composite implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public DataListBox getTask() {
        return task;
    }

    public void setTask(DataListBox task) {
        this.task = task;
    }

    private ListingFilterParameter filterParameter;
    private DatePicker fromDate = new DatePicker();
    private DatePicker toDate = new DatePicker();
    private boolean reset = false;

    private DataListBox client = null;
    private DataListBox invoiceClient = null;
    private DataListBox project = null;
    private DataListBox viewAs = null;
    private DataListBox department = null;
    private DataListBox employee = null;
    private DataListBox task = null;
    private DataListBox taskStatus = null;
    private DataListBox projectStatus = null;
    private DataListBox locations = null;
    private DataListBox leadStatus;
    private DataListBox activityStatus;
    private DataListBox opportunityStages;
    private DataListBox countries;
    private DataListBox campaign;
    private DataListBox campaignStatus;
    private DataListBox contactVisibility;
    private DataListBox invoiceStatus = null;
    private DataListBox taskPriority = null;
    private DataListBox issueStatus = null;
    private DataListBox issuePriority = null;
    private KpiCheckBox plannedStart = new KpiCheckBox();
    private KpiCheckBox plannedEnd = new KpiCheckBox();
    private KpiCheckBox actualStart = new KpiCheckBox();
    private KpiCheckBox actualEnd = new KpiCheckBox();

    private TextBox inputStartQuantity = new TextBox();
    private TextBox inputEndQuantity = new TextBox();

    private TextBox inputStartPrice = new TextBox();
    private TextBox inputEndPrice = new TextBox();

    private DataListBox backendUsers = null;

    private DataListBox bugStatusList = null;
    private DataListBox bugAssigneeList = null;

    private DataListBox timeSheetApprovalListApprovers;
    private DataListBox timeSheetApprovalStatus;
    //------------------------------------------

    public final static long CLIENT = 1;
    public final static long PROJECT = 2;
    public final static long DEPARTMENT = 4;
    public final static long EMPLOYEE = 8;
    public final static long TASK = 16;
    public final static long TASK_STATUS = 32;
    public final static long PROJECT_STATUS = 64;
    public final static long INVOICE = 128;
    public final static long DATE = 256;
    public final static long INVOICE_CLIENT = 512;
    public final static long TASK_PRIORITY = 1024;
    public final static long EXTENDED_DATE = 2048;
    public final static long BACKEND_USERS = 32768;
    public final static long TIMESHEET_APPROVAL_APPROVERS = 65536;
    public final static long BUG_LIST = 131072;
    public final static long LEAD_LIST = 262144;
    public final static long SUPPLIER = 524288;
    public final static long TIMESHEET_APPROVAL_STATUS = 1048576;
    public final static long LOCATION_LIST = 2097152;
    public final static long COUNTRY_LIST = 4194304;
    public final static long LEAD_DATES = 8388608;
    public final static long CAMPAIGN = 16777216;
    public final static long OPPORTUNITY_LIST = 33554432;
    public final static long ACTIVITY_LIST = 67108864;
    public final static long CAMPAIGN_LIST = 134217728;
    public final static long CONTACT_LIST = 268435456;
    public final static long ISSUE_STATUS = 536870912;
    public final static long ISSUE_PRIORITY = 1073741824;
    public final static long INVOICE_FILTER = INVOICE_CLIENT + INVOICE + CLIENT + DATE;
    public final static long INVOICE_FILTER_PROJECT = INVOICE_CLIENT + INVOICE + CLIENT + DATE + PROJECT;
    public final static long PURCHASE_INVOICE_ORDER = SUPPLIER + INVOICE + DATE;
    public final static long DEFAULT = CLIENT + PROJECT + DEPARTMENT + EMPLOYEE + TASK;
    public final static long EXPENSE_LIST = EMPLOYEE;
    public final static long TASK_LIST = CLIENT + PROJECT + DEPARTMENT + EMPLOYEE + TASK + TASK_STATUS + TASK_PRIORITY + DATE + EXTENDED_DATE;
    public final static long ISSUE_LIST = CLIENT + PROJECT + DEPARTMENT + EMPLOYEE + TASK + ISSUE_STATUS + ISSUE_PRIORITY;
    public final static long TIMESHEET_APPROVAL_LIST = TIMESHEET_APPROVAL_APPROVERS + DEPARTMENT + EMPLOYEE + TIMESHEET_APPROVAL_STATUS;
    public final static long WAREHOUSE = 3;
    public final static long TASK_EMPLOYEE = 5;
    public final static long TIME_ENTRIES_FILTER = TASK_EMPLOYEE;
    private Button applyButton = new Button(wfmStrings.apply());


    private Integer getSelectedItemId(DataListBox box) {
        Integer id = null;
        if (box != null && box.getSelectedItem() != null) {
            id = box.getSelectedItem().getId();
        }
        return id;
    }

    public ListingFilterParameter applyFilter() {
        filterParameter = new ListingFilterParameter();

        if (getSelectedItemId(viewAs) != null) {
            filterParameter.setViewAsId(getSelectedItemId(viewAs));
        }

        if (getSelectedItemId(client) != null) {
            filterParameter.setClientId(getSelectedItemId(client));
        }

        if (getSelectedItemId(project) != null) {
            filterParameter.setProjectId(getSelectedItemId(project));
        }

        if (getSelectedItemId(department) != null) {
            filterParameter.setDepartmentId(getSelectedItemId(department));
        }

        if (getSelectedItemId(employee) != null) {
            filterParameter.setEmployeeId(getSelectedItemId(employee));
        }

        if (getSelectedItemId(taskStatus) != null) {
            filterParameter.setTaskStatusId(getSelectedItemId(taskStatus));
        }
        if (getSelectedItemId(projectStatus) != null) {
            filterParameter.setProjectStatusId(getSelectedItemId(projectStatus));
        }
        if (getSelectedItemId(locations) != null) {
            filterParameter.setLocationId(getSelectedItemId(locations));
        }
        if (getSelectedItemId(countries) != null) {
            filterParameter.setCountryId(getSelectedItemId(countries));
        }
        if (getSelectedItemId(leadStatus) != null) {
            filterParameter.setStatusValues(getSelectedItemId(leadStatus).toString());
        }
        if (getSelectedItemId(activityStatus) != null) {
            filterParameter.setStatusValues(getSelectedItemId(activityStatus).toString());
        }
        if (getSelectedItemId(opportunityStages) != null) {
            filterParameter.setStatusValues(getSelectedItemId(opportunityStages).toString());
        }
        if (getSelectedItemId(invoiceStatus) != null) {
            filterParameter.setInvoiceStatusId(getSelectedItemId(invoiceStatus));
        }

        if (getSelectedItemId(invoiceClient) != null) {
            filterParameter.setInvoiceClientId(getSelectedItemId(invoiceClient));
        }

        if (getSelectedItemId(backendUsers) != null) {
            filterParameter.setBackendUsersId(getSelectedItemId(backendUsers));
        }

        if (getSelectedItemId(bugStatusList) != null) {
            filterParameter.setBugStatusId(getSelectedItemId(bugStatusList));
            filterParameter.setStatusValues(bugStatusList.getSelectedItem().getDescription());
        }

        if (getSelectedItemId(bugAssigneeList) != null) {
            filterParameter.setBugAssigneeId(getSelectedItemId(bugAssigneeList));
        }

        if (getSelectedItemId(timeSheetApprovalListApprovers) != null) {
            filterParameter.setUserID(getSelectedItemId(timeSheetApprovalListApprovers));
        }
        if (getSelectedItemId(timeSheetApprovalStatus) != null) {
            filterParameter.setStatusID(getSelectedItemId(timeSheetApprovalStatus));
        }

        if (getSelectedItemId(taskPriority) != null) {
            filterParameter.setTaskPriorityId(getSelectedItemId(taskPriority));
        }

        if (getSelectedItemId(issueStatus) != null) {
            filterParameter.setIssueStatusId(getSelectedItemId(issueStatus));
        }

        if (getSelectedItemId(issuePriority) != null) {
            filterParameter.setIssuePriorityId(getSelectedItemId(issuePriority));
        }

        if (getSelectedItemId(campaign) != null) {
            filterParameter.setCampaignID(getSelectedItemId(campaign));
        }

        if (getSelectedItemId(contactVisibility) != null) {
            filterParameter.setStatusValues(getSelectedItemId(contactVisibility).toString());
        }

        if (getSelectedItemId(campaignStatus) != null) {
            filterParameter.setStatusValues(getSelectedItemId(campaignStatus).toString());
        }

        if (fromDate != null) {
            if (fromDate.getDate() != null && fromDate.getText().length() != 0 && !wfmStrings.pleaseSelect().equals(fromDate.getText())) {
                filterParameter.setStartDate(fromDate.getDate());
            } else {
                filterParameter.setStartDate(null);
            }
        }
        if (toDate != null) {
            if (toDate.getDate() != null && toDate.getText().length() != 0 && !wfmStrings.pleaseSelect().equals(toDate.getText())) {
                filterParameter.setDueDate(toDate.getDate());
            } else {
                filterParameter.setDueDate(null);
            }
        }

        if (actualEnd != null) {
            filterParameter.setActualDue(actualEnd.getValue());
        }
        if (actualStart != null) {
            filterParameter.setActualStart(actualStart.getValue());
        }
        if (plannedStart != null) {
            filterParameter.setPlannedStart(plannedStart.getValue());
        }
        if (plannedEnd != null) {
            filterParameter.setPlannedDue(plannedEnd.getValue());
        }

        if (inputStartQuantity != null) {
            if (inputStartQuantity.getText() != null && inputStartQuantity.getText().length() != 0) {
                filterParameter.setQuantityStartValue(inputStartQuantity.getText());
            } else {
                filterParameter.setQuantityStartValue(null);
            }
        }
        if (inputEndQuantity != null) {
            if (inputEndQuantity.getText() != null && inputEndQuantity.getText().length() != 0) {
                filterParameter.setQuantityEndValue(inputEndQuantity.getText());
            } else {
                filterParameter.setQuantityEndValue(null);
            }
        }
        if (inputStartPrice != null) {
            if (inputStartPrice.getText() != null && inputStartPrice.getText().length() != 0) {
                filterParameter.setPriceStartValue(inputStartPrice.getText());
            } else {
                filterParameter.setPriceStartValue(null);
            }
        }
        if (inputEndPrice != null) {
            if (inputEndPrice.getText() != null && inputEndPrice.getText().length() != 0) {
                filterParameter.setPriceEndValue(inputEndPrice.getText());
            } else {
                filterParameter.setPriceEndValue(null);
            }
        }

        return filterParameter;
    }

    public ChooseFilter() {
        super();
        fromDate.setDate(null);
        toDate.setDate(null);

    }

    public Button getApplyButton() {
        return applyButton;
    }

    public void setApplyButton(Button applyButton) {
        this.applyButton = applyButton;
    }

}
