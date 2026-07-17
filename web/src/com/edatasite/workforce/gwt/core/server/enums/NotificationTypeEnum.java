package com.edatasite.workforce.gwt.core.server.enums;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;

/**
 * Created by dilsh0d on 09.07.15.
 */
public enum NotificationTypeEnum implements Constants{
    LeaveRequests(1, true, MODULE_HRMS, ActionOnEntityEnum.WAIT_APPROVAL, ActionOnEntityEnum.APPROVED, ActionOnEntityEnum.DENIED), //HR
    TimeSheetApproval(2, true, MODULE_PM, ActionOnEntityEnum.WAIT_APPROVAL, ActionOnEntityEnum.APPROVED, ActionOnEntityEnum.REJECTED), //HR
    ExpenseClaim(3, true, MODULE_ACCOUNTING, ActionOnEntityEnum.WAIT_APPROVAL, ActionOnEntityEnum.APPROVED, ActionOnEntityEnum.DECLINE, ActionOnEntityEnum.REMOVE), //HR
    GoogleCalendarEvent(4, true, MODULE_CRM, ActionOnEntityEnum.EVENT), //HR
    TaskDueReminder(5, true, MODULE_PM, ActionOnEntityEnum.TASK_REMINDER), //HR
    MeetingMinutesNotification(6, true, MODULE_HRMS, ActionOnEntityEnum.MEETING_MINUTES), //HR
    BenefitRequest(7, true, MODULE_HRMS, ActionOnEntityEnum.WAIT_APPROVAL, ActionOnEntityEnum.APPROVED, ActionOnEntityEnum.REJECTED), //HR
    ProjectDueReminder(8, true, MODULE_PM, ActionOnEntityEnum.PROJECT_REMINDER), //HR
    WorkstreamDueReminder(9, true, MODULE_PM, ActionOnEntityEnum.WORKSTREAM_REMINDER), //HR
    CRMCase(10, true, MODULE_CRM, ActionOnEntityEnum.ASSIGNED),
    TimeSheetDueReminder(11, false, MODULE_PM, ActionOnEntityEnum.TIMESHEET_REMINDER), //HR
    InvoiceDueReminder(12, false, MODULE_ACCOUNTING, ActionOnEntityEnum.INVOICE_REMINDER),
    TaskAssignee(13, false, MODULE_PM, ActionOnEntityEnum.ASSIGNED), //HR
    LeadAssignee(14, true, MODULE_CRM, ActionOnEntityEnum.ASSIGNED),
    SaleInvoiceApproval(15, false, MODULE_ACCOUNTING, ActionOnEntityEnum.APPROVED),
    CRMContact(16, true, MODULE_CRM),
    Employee(17, true, MODULE_HRMS), //HR
    ProjectApproval(18, true, MODULE_PM, ActionOnEntityEnum.APPROVED, ActionOnEntityEnum.REJECTED), //HR
    PurchaseOrder(19, true, MODULE_ACCOUNTING),
    Candidate(20, true, MODULE_HRMS),//if you add a new type, pls don't forget add it notification.properties and notificationShort.properties. Otherwise we'll get No message found under code {NewType} for locale 'en'.
    CrmOpportunity(21, true, MODULE_CRM),
    CashAdvance(22, true, MODULE_PAYROLL), //HR
    OnboardingStep(23, true, MODULE_HRMS),
    SalesQuote(24, true, MODULE_ACCOUNTING),
    CustomFormItem(25, true, MODULE_ACCOUNTING),
    SalesOrder(26, true, MODULE_ACCOUNTING),
    SalesInvoice(27, true, MODULE_ACCOUNTING),
    PurchaseInvoice(28, true, MODULE_ACCOUNTING),
    Customer(29, true, MODULE_ACCOUNTING),
    TaskCreated(30, true, MODULE_PM),
    ReceivePayment(31, true, MODULE_ACCOUNTING),
    PayInvoice(32, true, MODULE_ACCOUNTING),
    Placement(33, true, MODULE_HRMS),
    Rotation(34, true, MODULE_HRMS),
    GroupPlacement(35, true, MODULE_HRMS),
    ProductCategory(36, true, MODULE_SETTINGS),
    WhatsApp(37, true, WHATSAPP),
    FixedAssets(38, true,MODULE_ACCOUNTING),
    CompanySettings(39, true, MODULE_SETTINGS),
    EmployeeDocuments(40, true, MODULE_HRMS);

    NotificationTypeEnum(int id, boolean defaultSentEvent, String module, ActionOnEntityEnum... actions) {
        this.id = id;
        this.defaultSentEvent = defaultSentEvent;
        this.module = module;
        this.actions = actions;
    }


    private final int id;
    private final boolean defaultSentEvent;
    private String module;
    private final ActionOnEntityEnum[] actions;

    public int getId() {
        return id;
    }

    public NotificationTypeEnum setModule(String module) {
        this.module = module;
        return this;
    }

    public ActionOnEntityEnum[] getActions() {
        return actions;
    }

    public boolean isDefaultSentEvent() {
        return defaultSentEvent;
    }

    public String getModule() {
        return module;
    }


    public static NotificationTypeEnum getByIdType(Integer id){
        if (id != null) {
            for (NotificationTypeEnum type : NotificationTypeEnum.values()) {
                if (type.getId() == id) {
                    return type;
                }
            }
        }
        return null;
    }

    public static boolean isMobileEvent(NotificationTypeEnum event) {
        return NotificationTypeEnum.LeaveRequests.equals(event)
                || NotificationTypeEnum.ProjectDueReminder.equals(event)
                || NotificationTypeEnum.TaskDueReminder.equals(event)
                || NotificationTypeEnum.TimeSheetDueReminder.equals(event)
                || NotificationTypeEnum.InvoiceDueReminder.equals(event)
                || NotificationTypeEnum.TaskAssignee.equals(event)
                || NotificationTypeEnum.GoogleCalendarEvent.equals(event)
                || NotificationTypeEnum.CRMCase.equals(event)
                || NotificationTypeEnum.LeadAssignee.equals(event)
                || NotificationTypeEnum.CRMContact.equals(event)
                || NotificationTypeEnum.SaleInvoiceApproval.equals(event)
                || NotificationTypeEnum.ExpenseClaim.equals(event);
    }
}
