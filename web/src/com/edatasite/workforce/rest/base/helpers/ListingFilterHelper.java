package com.edatasite.workforce.rest.base.helpers;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.rest.base.to.PanelColumnTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umidbek on 02.02.2015.
 */
public class ListingFilterHelper {

    public static final Integer ITEMS_PER_PAGE = 20;

    public static ArrayList<String> getTaskColumnNames() {
        return new ArrayList<String>() {{
            this.add(TaskListItem.NAME);
            this.add(TaskListItem.NUMBER);
            this.add(TaskListItem.DESCRIPTION);
            this.add(TaskListItem.STATUS_NAME);
            this.add(TaskListItem.OVERALL_STATUS_NAME);
            this.add(TaskListItem.PRIORITY_NAME);
            this.add(TaskListItem.PROJECT_NAME);
            this.add(TaskListItem.PROJECT_NUMBER);
            this.add(TaskListItem.PROJECT_MANAGER_NAME);
            this.add(TaskListItem.LAST_MODIFIED_BY);
            this.add(TaskListItem.LAST_MODIFIED);
            this.add(TaskListItem.CREATION_DATE);
            this.add(TaskListItem.START_DATE);
            this.add(TaskListItem.END_DATE);
            this.add(TaskListItem.ESTIMATED);
            this.add(TaskListItem.ASSIGNED_TO);
            this.add(TaskListItem.COMPLETE);
            this.add(TaskListItem.ACTUAL_HOURS_SPENT);
            this.add(TaskListItem.HOUR_SPENT);
            this.add(TaskListItem.DUE_DATE);
            this.add(TaskListItem.CLIENT);
            this.add(TaskListItem.TASK_RELATED_CLIENT);
            this.add(TaskListItem.STRING_VALUE);
            this.add(TaskListItem.DATE_VALUE);
            this.add(TaskListItem.NUMBER_VALUE);
            this.add(TaskListItem.PARENT_WORKSTREAM_NAME);
            this.add(TaskListItem.BILLABLE);
            this.add(TaskListItem.ACTUAL_TIME);
            this.add(TaskListItem.ACTUAL_START_DATE);
            this.add(TaskListItem.ACTUAL_END_DATE);
            this.add(TaskListItem.WAITING_HOURS);
            this.add(TaskListItem.REJECTED_HOURS);
        }};
    }

    public static ArrayList<String> getProjectColumnNames() {
        return new ArrayList<String>() {{
            this.add(ProjectListItem.CLIENT);
            this.add(ProjectListItem.DESCRIPTION);
            this.add(ProjectListItem.END_DATE);
            this.add(ProjectListItem.HEAD_COUNT);
            this.add(ProjectListItem.ACTUAL_TIME_SPENT);
            this.add(ProjectListItem.HOURS_SPENT);
            this.add(ProjectListItem.INVOICES);
            this.add(ProjectListItem.LAST_UPDATE);
            this.add(ProjectListItem.LOCATION);
            this.add(ProjectListItem.MANAGER);
            this.add(ProjectListItem.BACKUP_MANAGER);
            this.add(ProjectListItem.NAME);
            this.add(ProjectListItem.NUMBER);
            this.add(ProjectListItem.NUMBER_OF_TASKS);
            this.add(ProjectListItem.PERCENT_COMPLETED);
            this.add(ProjectListItem.STATUS);
            this.add(ProjectListItem.START_DATE);
            this.add(ProjectListItem.TEAMS);
            this.add(ProjectListItem.ESTIMATED_TIME);
            this.add(ProjectListItem.PLANED_INCOME);
            this.add(ProjectListItem.INCOME);
            this.add(ProjectListItem.PLANED_COST);
            this.add(ProjectListItem.COST);
            this.add(ProjectListItem.PLANED_PROFIT);
            this.add(ProjectListItem.PROFIT);
            this.add(ProjectListItem.DIFFERENCE);
            this.add(ProjectListItem.WAITING_HOURS);
            this.add(ProjectListItem.REJECTED_HOURS);
        }};
    }

    public static ArrayList<String> getEmployeeColumnNames() {
        return new ArrayList<String>() {{
            this.add(EmployeeListItem.ACTION);
            this.add(EmployeeListItem.EMPLOYEE_NUMBER);
            this.add(EmployeeListItem.FIRST_NAME);
            this.add(EmployeeListItem.LAST_NAME);
            this.add(EmployeeListItem.EMAIL);
            this.add(EmployeeListItem.START_DATE);
            this.add(EmployeeListItem.STATUS);
            this.add(EmployeeListItem.ROLE);
            this.add(EmployeeListItem.POSITION);
            this.add(EmployeeListItem.PHONE_NUMBER);
            this.add(EmployeeListItem.LAST_UPDATE);
            this.add(EmployeeListItem.LOCATION);
            this.add(EmployeeListItem.MOBILE);
            this.add(EmployeeListItem.DEPARTMENT);
            this.add(EmployeeListItem.SALARY_AMOUNT);
            this.add(EmployeeListItem.DRIVER_ID);
            this.add(EmployeeListItem.PASSPORT_NUMBER);
            this.add(EmployeeListItem.PASSPORT_ISSUE_DATE);
            this.add(EmployeeListItem.PASSPORT_ISSUE_BY);
            this.add(EmployeeListItem.PASSPORT_EXPIRE_DATE);
            this.add(EmployeeListItem.INSURANCE_NUMBER);
            this.add(EmployeeListItem.VISA_NUMBER);
            this.add(EmployeeListItem.VISA_ISSUE_DATE);
            this.add(EmployeeListItem.VISA_EXPIRATION_DATE);
        }};
    }

    public static ArrayList<String> getCaseColumnNames() {
        return new ArrayList<String>() {{
            this.add(CaseItem.CASE_ID);
            this.add(CaseItem.SUBJECT);
            this.add(CaseItem.PRIORITY);
            this.add(CaseItem.CASE_TYPE);
            this.add(CaseItem.REPORTED_BY);
            this.add(CaseItem.CREATED_DATE);
            this.add(CaseItem.LAST_UPDATED_DATE);
            this.add(CaseItem.ASSIGNED_TO);
            this.add(CaseItem.RESOLVER);
            this.add(CaseItem.STATUS);
            this.add(CaseItem.CASE_REASON);
            this.add(CaseItem.BILLABLE);
        }};
    }

    public static ArrayList<String> getContactColumnNames() {
        return new ArrayList<String>() {{
            this.add(ContactListItem.FIRST_NAME);
            this.add(ContactListItem.LAST_NAME);
            this.add(ContactListItem.PHONE);
            this.add(ContactListItem.MOBILE);
            this.add(ContactListItem.EMAIL);
            this.add(ContactListItem.STREET);
            this.add(ContactListItem.CITY);
            this.add(ContactListItem.COUNTRY);
            this.add(ContactListItem.STATE);
            this.add(ContactListItem.POST_CODE);
            this.add(ContactListItem.CATEGORIES);
        }};
    }

    public static ArrayList<String> getClientColumnNames() {
        return new ArrayList<String>() {{
            this.add(CrmAccountItem.ACCOUNT_NAME);
            this.add(CrmAccountItem.ACCOUNT_NUMBER);
        }};
    }


    public static ArrayList<String> getLeadColumnNames() {
        return new ArrayList<String>() {{
            this.add(ContactListItem.CONTACT_NAME);
            this.add(ContactListItem.CRM_ACCOUNT);
            this.add(ContactListItem.EMAIL);
            this.add(ContactListItem.PHONE);
            this.add(ContactListItem.STATUS);
            this.add(ContactListItem.OWNER);
            this.add(ContactListItem.FIRST_NAME);
            this.add(ContactListItem.LAST_MODIFIED);
            this.add(ContactListItem.JOB_TITLE);
        }};
    }

    public static ArrayList<String> getSaleOrderColumnNames() {
        return new ArrayList<String>() {{
            this.add(AccountingConstants.INVOICE_NUMBER_COLUMN);
            this.add(AccountingConstants.INVOICE_DATE_COLUMN);
            this.add(AccountingConstants.DUE_DATE_COLUMN);
            this.add(AccountingConstants.CLIENT_COLUMN);
            this.add(AccountingConstants.CURRENCY_COLUMN);
            this.add(AccountingConstants.DUE_AMOUNT_COLUMN);
            this.add(AccountingConstants.PAID_AMOUNT_COLUMN);
            this.add(AccountingConstants.STATUS_COLUMN);
        }};
    }

    public static List<PanelColumnTO> getContactColumns() {
        List<PanelColumnTO> panelColumns = new ArrayList<>();

        panelColumns.add(new PanelColumnTO("First Name", ContactListItem.FIRST_NAME, true));
        panelColumns.add(new PanelColumnTO("Last Name", ContactListItem.LAST_NAME));
        panelColumns.add(new PanelColumnTO("Primary Phone", ContactListItem.PHONE));
        panelColumns.add(new PanelColumnTO("Mobile", ContactListItem.MOBILE));
        panelColumns.add(new PanelColumnTO("Email", ContactListItem.EMAIL));
        panelColumns.add(new PanelColumnTO("Address", ContactListItem.STREET));
        panelColumns.add(new PanelColumnTO("City", ContactListItem.CITY));
        panelColumns.add(new PanelColumnTO("Country", ContactListItem.COUNTRY));
        panelColumns.add(new PanelColumnTO("State", ContactListItem.STATE));
        panelColumns.add(new PanelColumnTO("Post Code", ContactListItem.POST_CODE));
        panelColumns.add(new PanelColumnTO("Category", ContactListItem.CATEGORIES));

        return panelColumns;
    }

    public static List<PanelColumnTO> getTaskColumns(List<SelectItemTO> statusTypeList, List<SelectItemTO> priorityList) {
        List<PanelColumnTO> panelColumns = new ArrayList<>();

        panelColumns.add(new PanelColumnTO("Number", TaskListItem.NUMBER, true));
        panelColumns.add(new PanelColumnTO("Task Name", TaskListItem.NAME, true));
        panelColumns.add(new PanelColumnTO("Description", TaskListItem.DESCRIPTION));
        panelColumns.add(new PanelColumnTO("Client", TaskListItem.CLIENT));
        panelColumns.add(new PanelColumnTO("Project Name", TaskListItem.PROJECT_NAME));
        panelColumns.add(new PanelColumnTO("Project Number", TaskListItem.PROJECT_NUMBER));
        panelColumns.add(new PanelColumnTO("Assigned To", TaskListItem.ASSIGNED_TO));
        panelColumns.add(new PanelColumnTO("Priority", TaskListItem.PRIORITY_NAME, priorityList));

        panelColumns.add(new PanelColumnTO("Status", TaskListItem.OVERALL_STATUS_NAME));

        panelColumns.add(new PanelColumnTO("%Completed", TaskListItem.COMPLETE));
        panelColumns.add(new PanelColumnTO("Assignee status", TaskListItem.STATUS_NAME, statusTypeList));

        panelColumns.add(new PanelColumnTO(PanelColumnTO.TYPE_DATE, "Start Date", TaskListItem.START_DATE));
        panelColumns.add(new PanelColumnTO(PanelColumnTO.TYPE_DATE, "Due Date", TaskListItem.DUE_DATE));

        panelColumns.add(new PanelColumnTO("Estimated Time", TaskListItem.ESTIMATED));
        panelColumns.add(new PanelColumnTO("Time Spent", TaskListItem.HOUR_SPENT));
        panelColumns.add(new PanelColumnTO("Actual Time Spent", TaskListItem.ACTUAL_HOURS_SPENT));
        panelColumns.add(new PanelColumnTO("Actual Start Date", TaskListItem.ACTUAL_START_DATE));
        panelColumns.add(new PanelColumnTO("Actual Completed Date", TaskListItem.END_DATE));
        panelColumns.add(new PanelColumnTO("Project Manager", TaskListItem.PROJECT_MANAGER_NAME));
        panelColumns.add(new PanelColumnTO("Last Modified By", TaskListItem.LAST_MODIFIED_BY));
        panelColumns.add(new PanelColumnTO("Last Modified", TaskListItem.LAST_MODIFIED));
        panelColumns.add(new PanelColumnTO("Workstream", TaskListItem.PARENT_WORKSTREAM_NAME));
        panelColumns.add(new PanelColumnTO("Billable", TaskListItem.BILLABLE));

        return panelColumns;
    }

    public static List<PanelColumnTO> getProjectColumns(List<SelectItemTO> statusTypeList) {
        List<PanelColumnTO> panelColumns = new ArrayList<>();

        panelColumns.add(new PanelColumnTO("Project", ProjectListItem.NAME, true));
        panelColumns.add(new PanelColumnTO("PM", ProjectListItem.MANAGER));
        panelColumns.add(new PanelColumnTO("Headcount", ProjectListItem.HEAD_COUNT));
        panelColumns.add(new PanelColumnTO("Status", ProjectListItem.STATUS, statusTypeList));
        panelColumns.add(new PanelColumnTO(PanelColumnTO.TYPE_DATE, "Start date", ProjectListItem.START_DATE));
        panelColumns.add(new PanelColumnTO(PanelColumnTO.TYPE_DATE, "End date", ProjectListItem.END_DATE));

        return panelColumns;
    }

    public static List<PanelColumnTO> getVisible(List<PanelColumnTO> columns, List<String> columnCodeNames) {
        ArrayList<PanelColumnTO> result = new ArrayList<>();

        for (PanelColumnTO column : columns) {
            if (columnCodeNames.contains(column.getCode())) {
                result.add(column);
            }
        }

        return result;
    }

    public static ListingFilterParameter createFilterParameter(HttpServletRequest servletRequest, ListPanelType type) {
        return fillFilterParameter(new ListingFilterParameter(), servletRequest, type);
    }

    public static ListingFilterParameter fillFilterParameter(
            ListingFilterParameter listingParameter, HttpServletRequest servletRequest, ListPanelType type) {

        Integer start = WrapUtils.getInteger(servletRequest.getParameter("start"), 0);
        Integer limit = WrapUtils.getInteger(servletRequest.getParameter("limit"), ITEMS_PER_PAGE);
        Integer filterId = WrapUtils.getInteger(servletRequest.getParameter("filterId"));

        String searchKey = servletRequest.getParameter("searchKey");
        String sortField = servletRequest.getParameter("sortField");
        if (sortField != null &&
                (sortField.toLowerCase().contains("string_value")
                        || sortField.toLowerCase().contains("double_value")
                        || sortField.toLowerCase().contains("date_value"))) {
            sortField=sortField.toLowerCase();
        }

        Boolean forLookup = WrapUtils.getBoolean(servletRequest.getParameter("forLookup"), false);
        Boolean sortAscending = WrapUtils.getBoolean(servletRequest.getParameter("sortAscending"), false);

        ArrayList<String> visibleColumns = WrapUtils.getStringArray(servletRequest.getParameter("visibleColumns"));

        /**
         * Custom fields
         */

        Integer projectId = WrapUtils.getInteger(servletRequest.getParameter("projectId"));

        /**
         * Columns Validation
         */

        if (start < 0) {
            start = 0;
        }

        if (limit > ITEMS_PER_PAGE) {
            limit = ITEMS_PER_PAGE;
        }

        /**
         * Fill params
         */

        if (listingParameter == null) {
            listingParameter = new ListingFilterParameter();
        }

        listingParameter.setStart(start);
        listingParameter.setLimit(limit);
        listingParameter.setLookUp(forLookup);

        listingParameter.setProjectId(projectId);

        if (searchKey != null) {
            listingParameter.setSearchKey(searchKey);
            listingParameter.setSearchButton(true);
        }

        if (sortField != null) {
            listingParameter.setSortField(sortField);
            listingParameter.setAscending(sortAscending);
        }

        if (visibleColumns != null) {
            ListPanelToolRpc listPanelToolRpc = new ListPanelToolRpc();
            listPanelToolRpc.setColumnCodeName(visibleColumns);
            listingParameter.setListPanelTool(listPanelToolRpc);
        }

        listingParameter.setFacetFilter(FacetFilterHelper.fillFacetFilter(listingParameter.getFacetFilter(), servletRequest, type));

        if (filterId != null) {
            listingParameter.getFacetFilter().setObjectID(filterId);
        }

        return listingParameter;
    }
}
