package com.edatasite.workforce.gwt.profile.client.ui;

/**
 * User: Ilhombek
 * Date: 04.11.2010
 * Time: 11:48:29
 */
public interface EmailNotificationConstants {

    ////////////////////////////////////////////////////CATEGORY RELATED////////////////////////////////////////////////////
    String CATEGORY_CALENDAR = "CATEGORY_CALENDAR";
    String CATEGORY_CRM = "CATEGORY_CRM";
    String CATEGORY_PM = "CATEGORY_PM";
    String CATEGORY_COO = "CATEGORY_COO";
    String CATEGORY_HRMS = "CATEGORY_HRMS";
    String CATEGORY_ACCOUNTING = "CATEGORY_ACCOUNTING";

    ////////////////////////////////////////////////////CRM CASE RELATED////////////////////////////////////////////////////
    String CRM_CASE_UPDATE_NOTIFICATION = "CRM_CASE_UPDATE_NOTIFICATION";
    String CRM_CASE_ASSIGNEE_NOTIFICATION = "CRM_CASE_ASSIGNEE_NOTIFICATION";
    String CRM_CASE_ASSIGNEE_CHANGE_NOTIFICATION = "CRM_CASE_ASSIGNEE_CHANGE_NOTIFICATION";
    String CRM_CASE_RESOLVER_NOTIFICATION = "CRM_CASE_RESOLVER_NOTIFICATION";
    String CRM_CASE_RESOLVER_CHANGE_NOTIFICATION = "CRM_CASE_RESOLVER_CHANGE_NOTIFICATION";
    String CRM_CASE_CLOSE_NOTIFICATION = "CRM_CASE_CLOSE_NOTIFICATION";
    String CRM_CASE_REOPEN_NOTIFICATION = "CRM_CASE_REOPEN_NOTIFICATION";
    String CRM_CASE_REMOVAL_NOTIFICATION = "CRM_CASE_REMOVAL_NOTIFICATION";
    String CRM_CASE_ADD_NOTE_NOTIFICATION = "CRM_CASE_ADD_NOTE_NOTIFICATION";
    String CASE_CLOSED_SEND_TO_REPORTER = "CASE_CLOSED_SEND_TO_REPORTER";
    String CASE_RESOLVED_SEND_TO_REPORTER = "CASE_RESOLVED_SEND_TO_REPORTER";
    //Case description
    String CRM_CASE_UPDATE_DESCRIPTION = "Case Update";

    ////////////////////////////////////////////////////CALENDAR RELATED////////////////////////////////////////////////////
    String CALENDAR_ADD_EVENT_NOTIFICATION = "CALENDAR_ADD_EVENT_NOTIFICATION";
    //Event add description
    String CALENDAR_ADD_EVENT_DESCRIPTION = "Add Event";

    String CALENDAR_SHARE_EVENT_NOTIFICATION = "CALENDAR_SHARE_EVENT_NOTIFICATION";
    //Event share description
    String CALENDAR_SHARE_EVENT_DESCRIPTION = "Share Event";

    String CALENDAR_UPDATE_EVENT_NOTIFICATION = "CALENDAR_UPDATE_EVENT_NOTIFICATION";
    //Event update description
    String CALENDAR_UPDATE_EVENT_DESCRIPTION = "Update Event";

    String CALENDAR_DELETE_EVENT_NOTIFICATION = "CALENDAR_DELETE_EVENT_NOTIFICATION";
    //Event delete description
    String CALENDAR_DELETE_EVENT_DESCRIPTION = "Delete Event";

    ////////////////////////////////////////////////////DEPARTMENT RELATED////////////////////////////////////////////////////
    String DEPARTMENT_ADD_NOTIFICATION = "DEPARTMENT_ADD_NOTIFICATION";
    //Department add description
    String DEPARTMENT_ADD_DESCRIPTION = "Add Department";

    String DEPARTMENT_EMPLOYEE_ASSIGN_NOTIFICATION = "DEPARTMENT_EMPLOYEE_ASSIGN_NOTIFICATION";
    //Department employee assign description
    String DEPARTMENT_EMPLOYEE_ASSIGN_DESCRIPTION = "Department Employee Assignment";

    String DEPARTMENT_LEADER_ASSIGN_NOTIFICATION = "DEPARTMENT_LEADER_ASSIGN_NOTIFICATION";
    //Department leader assign description
    String DEPARTMENT_LEADER_ASSIGN_DESCRIPTION = "Department Leader Assignment";

    ////////////////////////////////////////////////////PROJECT RELATED////////////////////////////////////////////////////
    String PROJECT_ADD_NOTIFICATION = "PROJECT_ADD_NOTIFICATION";
    //Project delete
    String PROJECT_DELETE_NOTIFICATION = "PROJECT_DELETE_NOTIFICATION";
    String PROJECT_UPDATE_NOTIFICATION = "PROJECT_UPDATE_NOTIFICATION";
    //Project add description
    String PROJECT_ADD_DESCRIPTION = "Add Project";

    String PROJECT_ASSIGN_NOTIFICATION = "PROJECT_ASSIGN_NOTIFICATION";
    //Project assign description
    String PROJECT_ASSIGN_DESCRIPTION = "Project Assignment";

    String PROJECT_MANAGER_ASSIGN_NOTIFICATION = "PROJECT_MANAGER_ASSIGN_NOTIFICATION";
    //Project manager assign description
    String PROJECT_MANAGER_ASSIGN_DESCRIPTION = "Project Manager Assignment";

    String PROJECT_BACKUP_MANAGER_ASSIGN_NOTIFICATION = "PROJECT_BACKUP_MANAGER_ASSIGN_NOTIFICATION";
    //Project backup manager description
    String PROJECT_BACKUP_MANAGER_ASSIGN_DESCRIPTION = "Project Backup Manager Assignment";

    String PROJECT_GOAL_ASSIGNEE_NOTIFICATION = "PROJECT_GOAL_ASSIGNEE_NOTIFICATION";
    String DEPARTMENT_GOAL_ASSIGNEE_NOTIFICATION = "DEPARTMENT_GOAL_ASSIGNEE_NOTIFICATION";
    String PERSONAL_GOAL_ASSIGNEE_NOTIFICATION = "PERSONAL_GOAL_ASSIGNEE_NOTIFICATION";
    String BUSINESS_GOAL_ASSIGNESS_NOTIFICATION = "BUSINESS_GOAL_ASSIGNESS_NOTIFICATION";

    String PRODUCT_STOCK_NOTIFICATION = "PRODUCT_STOCK_NOTIFICATION";

    ////////////////////////////////////////////////////TASK RELATED////////////////////////////////////////////////////
    String TASK_ADD_NOTIFICATION = "TASK_ADD_NOTIFICATION";
    String TASK_ASSIGN_NOTIFICATION = "TASK_ASSIGN_NOTIFICATION";
    String TASK_DELETE_NOTIFICATION = "TASK_DELETE_NOTIFICATION";
    String TASK_UPDATE_NOTIFICATION = "TASK_UPDATE_NOTIFICATION";
    String SUCCESSOR_TASK_COMPLETED_NOTIFICATION = "SUCCESSOR_TASK_COMPLETED_NOTIFICATION";
    String TASK_COMPLETED_NOTIFICATION = "TASK_COMPLETED_NOTIFICATION";
    //Task assign description
    String TASK_ASSIGN_DESCRIPTION = "Task Assignment";

    ////////////////////////////////////////////////////TIMESHEET RELATED////////////////////////////////////////////////////
    String TIMESHEET_FOR_APPROVAL_TO_MANAGER_NOTIFICATION = "TIMESHEET_FOR_APPROVAL_TO_MANAGER_NOTIFICATION";
    //TimeSheet approval to manager description
    String TIMESHEET_FOR_APPROVAL_TO_MANAGER_DESCRIPTION = "Submit Timesheet For Manager's Approval";

    String TIMESHEET_FOR_APPROVAL_TO_USER_NOTIFICATION = "TIMESHEET_FOR_APPROVAL_TO_USER_NOTIFICATION";
    //TimeSheet approval to user description
    String TIMESHEET_FOR_APPROVAL_TO_USER_DESCRIPTION = "Timesheet Approved or Rejected Notification";

    ////////////////////////////////////////////////////ISSUE RELATED////////////////////////////////////////////////////
    String ISSUE_ADD_NOTIFICATION = "ISSUE_ADD_NOTIFICATION";
    String ISSUE_ASSIGN_NOTIFICATION = "ISSUE_ASSIGN_NOTIFICATION";
    String ISSUE_DELETE_NOTIFICATION = "ISSUE_DELETE_NOTIFICATION";
    String ISSUE_UPDATE_NOTIFICATION = "ISSUE_UPDATE_NOTIFICATION";

    ////////////////////////////////////////////// COO RELATED ///////////////////////////////////////////////////////////
    //new polls posted, new articles, and questioned asked in expert panels
    String COO_ARTICLES_ADD_NOTIFICATION = "COO_ARTICLES_ADD_NOTIFICATION";
    String COO_POLLS_ADD_NOTIFICATION = "COO_POLLS_ADD_NOTIFICATION";
    String COO_QUESTIONS_ASKED_NOTIFICATION = "COO_QUESTIONS_ASKED_NOTIFICATION";

    ///////////////////////////////////////////// DOCUMENT UPLOAD RELATED ////////////////////////////////////////////////
    //for PM category
    String DOC_UPLOAD_TO_TASK_NOTIFICATION = "DOC_UPLOAD_TO_TASK_NOTIFICATION";
    String DOC_UPLOAD_TO_PROJECT_NOTIFICATION = "DOC_UPLOAD_TO_PROJECT_NOTIFICATION";
    String DOC_UPLOAD_TO_ISSUE_NOTIFICATION = "DOC_UPLOAD_TO_ISSUE_NOTIFICATION";
    //for CRM category
    String DOC_UPLOAD_TO_CASE_NOTIFICATION = "DOC_UPLOAD_TO_CASE_NOTIFICATION";

    //for Accounting category
    String SALES_QUOTE_EMAIL = "SALES_QUOTE_EMAIL";
    String SALES_ORDER_EMAIL = "SALES_ORDER_EMAIL";
    String SALES_INVOICE_EMAIL = "SALES_INVOICE_EMAIL";
    String PURCHASE_ORDER_EMAIL = "PURCHASE_ORDER_EMAIL";
    String EXPENSE_CLAIM_EMAIL = "EXPENSE_CLAIM_EMAIL";
}