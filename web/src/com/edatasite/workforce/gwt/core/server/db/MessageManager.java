package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsBackupsEmployee;
import com.edatasite.workforce.core.domain.EdsBugReport;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsContract;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.core.domain.EdsMessage;
import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsShift;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsSuperMessage;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTaskHistory;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.assessment.EdsAppraisalApproval;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.EdsSmsSendItem;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettingsTemplate;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTask;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanPrice;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetItem;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MessageManager extends Manager<EdsMessage> {

    void sendAssessmentInitiateNotification(EdsEmployeeAssessment employeeAssessment, ByteArrayOutputStream baos, boolean sendEmailToEmployee) throws EdsDbException;

    void sendDeleteAssessmentNotification(EdsEmployeeAssessment employeeAssessment);

    void sendAssessment360InitiateNotification(EdsEmployeeAssessment employeeAssessment, List<EdsEmployeeAssessment> collaboratorsList) throws EdsDbException;

    void sendAssessmentRateNotification(EdsEmployeeAssessment employeeAssessment, Integer loggedUserId, ByteArrayOutputStream baos) throws EdsDbException;

    void sendAssessmentApproveNotification(EdsEmployeeAssessment employeeAssessment, ByteArrayOutputStream baos) throws EdsDbException;

    void sendAssessment360ApproveNotification(EdsEmployeeAssessment employeeAssessment, ByteArrayOutputStream baos) throws EdsDbException;

    void sendAssessmentReviewNotification(EdsEmployeeAssessment employeeAssessment, ByteArrayOutputStream baos) throws EdsDbException;

    void sendAssessment360ReviewNotificationForCollaborator(EdsEmployeeAssessment employeeAssessment) throws EdsDbException;

    void sendAssessment360ReviewNotification(EdsEmployeeAssessment employeeAssessment) throws EdsDbException;

    void sendCompletedPredTaskNotification(EdsEmployeeTask employeeTask, EdsUser user, String predTask) throws EdsDbException;

    void sendTaskCompletedNotification(Integer receiverID, Integer updaterID, EdsTask task) throws EdsDbException;

    void sendOneOffAssessment360ReviewNotification(EdsUser oneoffUser, EdsEmployeeAssessment employeeAssessment, ByteArrayOutputStream baos) throws EdsDbException;

    void sendOneOffPeerAssessmentReviewNotification(EdsUser oneoffUser, EdsEmployeeAssessment employeeAssessment) throws EdsDbException;

    void sendOneOffClientAssessmentReviewNotification(EdsUser oneoffUser, EdsEmployeeAssessment employeeAssessment) throws EdsDbException;

    void sendOneOffManagerAssessmentReviewNotification(EdsUser oneoffUser, EdsEmployeeAssessment employeeAssessment) throws EdsDbException;

    void sendMessage(String to, String subject, String text, String replyTo, Boolean hasAttachment, List<Integer> fileIDs,
                     String displaySubject, Integer companyId) throws EdsDbException;

    void sendProjectAssignNotification(EdsProjectEmployee projectEmployee, EdsUser user) throws EdsDbException;

    void sendTaskAssignNotification(EdsEmployeeTask employeeTask, EdsUser user) throws EdsDbException;

    void sendGoalAssignNotification(EdsGoal goal, EdsEmployee employee, List<EdsEmployee> goalAssigns) throws EdsDbException;

    void sendProductStockNotification(EdsItem item, EdsUser user, Integer companyId, List<String[]> items) throws EdsDbException;

    void sendMultiTaskAssignNotification(EdsUser creator, EdsEmployee employee, HashSet<EdsTask> tasks) throws EdsDbException;

    void sendMultiAssignLeadNotification(Integer assignId, Integer assignCount) throws EdsDbException;

    void sendTaskUpdateNotificationForClient(EdsUser receiver, EdsUser user, EdsTask task, EdsTaskHistory oldTask, String category, String subjectType, String template) throws EdsDbException;

    void sendTaskNotification(EdsEmployeeTask employeeTask, EdsUser user, String category, String subjectType, String template) throws EdsDbException;

    void sendTaskNotification(EdsEmployeeTask employeeTask, EdsTaskHistory oldTask, EdsUser user, String category, String subjectType, String template) throws EdsDbException;

    void sendProjectDeleteNotification(EdsProject project, EdsUser recipient, EdsUser user);

    void sendProjectUpdateNotification(EdsProject project, EdsUser recipient, EdsUser user);

    void sendTaskAddNotification(EdsTask task, EdsUser taskCreator, EdsEmployee recipient, boolean subjectWithCustomFields) throws EdsDbException;

    String mergeAssignees(Set<EdsEmployeeTask> assignees);

    void sendTeamAddNotification(EdsDepartment team, EdsUser user) throws EdsDbException;

    void sendTeamAddNotification(EdsDepartment team, EdsEmployee recipient, EdsUser user) throws EdsDbException;

    void sendTeamAssignNotification(EdsEmployeeDepartment teamEmployee, EdsUser user) throws EdsDbException;

    void sendTeamLeaderAssignNotification(EdsDepartment team, EdsUser user) throws EdsDbException;

    void sendCompanyRegistrationNotification(EdsUser user, EdsCompany company, String remoteAddr, boolean sendRegistrationNotification, boolean isIncludeActivationLink) throws EdsDbException;

//    void sendFromMobileCompanyRegistrationNotification(EdsUser administrator, Map<String, Object> companyInfo, boolean hasAccount) throws EdsDbException;

    void sendParamsForGMPLogin(StringBuffer stringBuffer, String param) throws EdsDbException;

    void sendCompanyRegistrationNotificationOnlySupport(EdsUser user, EdsCompany company, String remoteAddr, String subjectTitle) throws EdsDbException;

    void resendCompanyRegistrationNotification(List<EdsEmployee> adminList, EdsCompany company, String remoteAddr) throws EdsDbException;

    void sendPasswordChangedNotification(EdsUser user, boolean isResetPassword) throws EdsDbException;

    void sendIssueAddNotification(EdsIssue issue, EdsUser recipient, EdsUser user) throws EdsDbException;

    void sendIssueAddNotificationToClient(EdsIssue issue, EdsUser recipient, EdsUser user) throws EdsDbException;

    void sendIssueAssignNotification(EdsIssue issue, EdsUser recipient, EdsUser user) throws EdsDbException;

    void sendIssueDeleteNotification(EdsIssue issue, EdsEmployee recipient, EdsUser user) throws EdsDbException;

    void sendIssueUpdateNotification(EdsIssue issue, EdsUser recipient, EdsUser user) throws EdsDbException;

    void sendIssueUpdateNotificationToClient(EdsIssue issue, EdsUser recipient, EdsUser user) throws EdsDbException;

    void sendEmployeeAddNotification(EdsEmployee employee, EdsUser admin) throws EdsDbException;

    void sendEmployeeAddActivationLink(EdsEmployee employee, EdsUser admin) throws EdsDbException;

    void sendEmployeeAddNotificationForExistingUserName(EdsEmployee employee, EdsUser user) throws EdsDbException;

    String formatDate(Date date, EdsCompany company);

    void sendToClient(EdsClientContact client, EdsUser creator, String subject) throws EdsDbException, EdsTemplateException;

    void sendToClientWithoutActivationLink(EdsClientContact client, EdsUser creator, String subject) throws EdsDbException, EdsTemplateException;

    void sendToClientUsernamePassword(EdsClientContact client, String subject) throws EdsDbException, EdsTemplateException;

    void sendToClientWithoutUsernamePassword(EdsClientContact client, String subject) throws EdsDbException, EdsTemplateException;

    void sendDailyReportNotification() throws EdsDbException, EdsTemplateException;

    void sendSubscriptionExpirationReportNotification(EdsCompany company, EdsEmployee receiver, int days, Date expireDate, String subject) throws EdsTemplateException;

    void sendReferSomeoneMessage(EdsCompany company, EdsUser sender, String receiver, String content) throws EdsTemplateException;

    void sendUserPostedToFacebookMessage(EdsCompany company, EdsUser sender, String receiver, String content) throws EdsTemplateException;

    void sendSubscriptionExpiredReportNotification(EdsCompany company, EdsEmployee receiver, String subject) throws EdsTemplateException;

    void sendInsufficientFundsNotification(EdsUser receiver) throws EdsTemplateException;

    void sendFreeTrialExpirationReportNotification(EdsCompany company, EdsEmployee receiver, int days, Date expireDate, String subject) throws EdsTemplateException;

    void sendInvoiceQuoteToManager(String fromEmail, EdsUser user, String cc, String bcc, NewInvoice invoiceData, String clientContactName, List<Integer> fileIds, String type, String link, String replyTo) throws EdsDbException, EdsTemplateException;

    void sendEmployeeActivationMessage(EdsEmployee employee) throws EdsDbException, EdsTemplateException;

    void resendEmployeesRegistrationNotification(List<EdsEmployee> employeesList, EdsEmployee admin) throws EdsDbException, EdsTemplateException;

    void sendBugReport(EdsUser user, String messageContent, String reportText, String viewSection, Date creationTime, Boolean hasAttachment, List<Integer> fileIDs) throws EdsDbException, EdsTemplateException;

    void sendForgotPasswordNotification(EdsUser user, Map<Boolean, CompanyDomain> isKpi) throws EdsDbException;

    void reportSubmittedToEmployee(EdsExpenseReport report, Date submitedDate, ByteArrayOutputStream pdfStream);

    void reportSubmittedToApprover(EdsExpenseReport report, Date submitedDate, ByteArrayOutputStream pdfStream);

    void reportDeclinedToEmployee(EdsExpenseReport report, Date declinedDate);

    void reportApprovedToEmployee(EdsExpenseReport report, Date approvedDate, ByteArrayOutputStream pdfStream);

    void reportResubmittedToApprover(EdsExpenseReport report, Date resubmittedDate, Date declinedDate, ByteArrayOutputStream pdfStream);

    void reportResubmittedToApprover(EdsExpenseReport report, String text, Date resubmittedDate, ByteArrayOutputStream pdfStream);

    void reportSubmittedToApprover(EdsExpenseReport report, String text, Date submitedDate, ByteArrayOutputStream pdfStream);

    void sendCalendarShareEventNotification(EdsEmployeeEvent employeeEvent, ArrayList<EdsUser> attendees, boolean onlyShare) throws EdsDbException;

    void sendCalendarDeleteEventNotification(EdsEvent event, EdsUser attendee, boolean deleteWithNotify) throws EdsDbException;

    void sendNotificationToCalendarEventGuests(EdsUser user, EdsEvent event, String guestsEmail, String guestNames, String action) throws EdsDbException;

    String getEventGuestName(String guestsEmail, Integer companyID);

    void sendMessageFromUser(String fromEmail, String to, String cc, String bcc, String subject, String text, Boolean attachment, String replyTo, List<Integer> fileIDs, boolean isFileBody, String displaySubject, Integer companyId, EdsUser from) throws EdsDbException;

    void sendMessageFromUser(String fromEmail, String to, String cc, String bcc, String subject, String text, Boolean attachment, String replyTo, List<Integer> fileIDs, boolean isFileBody, String displaySubject, Integer companyId, EdsUser from, boolean isTest) throws EdsDbException;

    void sendMessageForPayrun(String fromUserName, String fromEmail,  String to, String subject, String text, List<Integer> fileIDs, Integer companyId) throws EdsDbException;

    void sendPayPalNotification(String mes, String subject);

    void sendPayPalNotification(String mes, String sub, String to);

    void sendWorldPayNotification(String mes, String sub);

    void sendGoogleCheckoutNotification(String mes, String subject);

    void sendDailyNonActivateLinksNotification() throws EdsDbException;

    void send360ReviewReminederNotification(EdsEmployeeAssessment employeeAssessment, String messageContent) throws EdsDbException;

    void sendBugReportChangeNotification(EdsBugReport bugReport, boolean bugStatusChanged) throws EdsDbException;

    void sendSickRequestNotificationToSelectedEmployee(EdsUser toUser, EdsSickRequest request) throws EdsTemplateException;

    void sendTimeSheetForApprovalToManager(EdsTimeSheetApprovalSession timeSheetApproval, EdsEmployee manager) throws EdsDbException;

    void sendTimeSheetProceededNotification(EdsTimeSheetApprovalSession timeSheetApproval, Set<EdsTimeSheet> rejectedEntries, EdsUser manager) throws EdsDbException;

    void sendOverdueInvoiceReminder(InvoiceList data, EdsUser user, String baseCurrency) throws EdsDbException;

    void sendOverdueInvoiceReminderForEveryClient(Map<String, List<NewInvoice>> data, EdsUser user) throws EdsDbException;

    void sendTimesheetReminder(Map<String, List<TimesheetItem>> data, EdsUser user, Date startDate, Date endDate) throws EdsDbException;

    void sendEventReminder(EdsEmployeeEvent employeeEvent) throws EdsDbException;

    void sendEventReminderSms(EdsEmployeeEvent employeeEvent) throws EdsDbException;

    void sendEventReminderNotification(EdsEmployeeEvent employeeEvent) throws EdsDbException;

    void sendReport(EdsReport report, EdsUser user, Integer uploadId) throws EdsDbException, EdsTemplateException;

    void sendWeeklySubscriptionReportMessage(ByteArrayOutputStream baos) throws EdsDbException;

    void sendUnProceedEventsNotification(ArrayList<EdsBusinessEvent> unproceedEvents) throws EdsDbException;

    void sendTestEmail(EmailTemplateItem item) throws EdsDbException;

    void registerInternalMessageBasic(String to, String subject, String text, Integer companyId) throws EdsDbException;

    void registerInternalMessageBasic(String fromEmail, String to, String subject, String text, Integer companyId) throws EdsDbException;

    void sendEmployeeAddNotificationFromGoogleMarket(EdsEmployee employee, EdsUser admin) throws EdsDbException;

    void sendTaskOverDueDateReminder(EdsTask task, EdsEmployeeTask employeeTask) throws EdsDbException;

    void sendWorkstreamOverDueDateReminder(EdsWorkStream task, EdsEmployee employeeTask) throws EdsDbException;

    void sendContractOverDueReminder(EdsContract contract, EdsEmployee employee, EmailTemplateItem templateItem);

    void sendTaskOverDueReminder(EdsTask task, EdsEmployeeTask employeeTask, EmailTemplateItem templateItem);

    void sendProjectOverDueDateReminder(EdsProject project, EdsProjectEmployee employeeProject) throws EdsDbException;

    void sendSalesQuoteCancelledMessage(String quoteNumber, EdsCrmContact crmContact);

    void sendToStoreFrontClient(EdsClientContact client, EdsUser creator, String subject) throws EdsDbException, EdsTemplateException;

    void sendToAddNoteToTaskAndProject(EdsUser from, EdsUser to, String noteBody, boolean isTask, Integer taskOrProjectID) throws EdsTemplateException;

    void sendMailToOwnerAboutGuestsStatus(Integer eventId, String email, String answer);

    void sendPurchaseOrderApprovedOrDeclinedMessage(EdsPurchaseOrder order);

    String getWebContentByUrl(String url);

    Boolean generateAndSendSms(EdsSmsSendItem item);

    String smsTemplateGenerateText(String value, ContactListItem lead, EmployeeListItem employee);

    String smsTemplateGenerateTextForSalesInvoice(String value, Integer saleInvoiceId);

    String generateEmployeeEventTemplate(String templateContent, EmployeeListItem employee);


    Appointment generateCandidateEventTemplate(String templateContent, ContactListItem lead, boolean isSubject, String... template);

    String crmAccountSmsTemplateGenerateText(String value, CrmAccountItem crmAccount);

    void sendSalesQuotePingPongNotificationIfEnabled(EdsSaleQuote saleQuote, String rejectionReason);

    void sendDocumentsUploadNotification(String to, String subject, String text, Integer receiverID, Integer companyID, String notificationType) throws EdsDbException;

    void sendTopicCreatedToExpertNotificationForCOO(EdsUser expert, String topicName, Integer companyId) throws EdsDbException;

    void sendFixedAssetCountResult(FixedAssetList fixedAssetList);

    String getEventGuests(EdsEvent event);

    void sendToManagerForCourseBookingConfirm(EdsCourseBooking courseBooking, Integer userID, String toEmail);

    void sendToStudentsForCourseBookingConfirm(List<EdsCourseScheduleStudent> courseScheduleStudentList, Integer userID, String subject);

    void sendPeriodAppraisalNotification(EdsAppraisalApproval edsAppraisalApproval);

    void sendNotificationToHrRecurrently(Map<EdsDepartment, List<EdsEmployee>> data);

    void sendEmployeeVisaExpirationDateReminder(Integer recurrenceID, EdsEmployee receiver, EdsEmployee employee, Integer employeeProfileID) throws EdsDbException;

    void sendAddOnRequestMessage(EdsGenericSettings addOnSettings);

    void sendOpportunityAssigned(EdsOpportunity opportunity, EdsEmployee assignee, boolean created, Integer userID);

    void sendToStudentsForScheduleCourseConfirm(EdsCourseScheduleStudent courseScheduleStudent, Integer sourceID);

    void sendScheduledTaskAsMail(EdsTCScheduledTask scheduledTask);

    void sendRFPEmailRequest(MessageItem messageItem);

    void internalMailSender(EdsMessage message, Integer companyId) throws Exception;

    EdsEmailSetting wrapEdsCompanySystemSettings(Integer companyId, String fromEmail);

    void sendPayslipToManager(EdsPayslipTable payslip) throws Exception;

    void sendSinglePayrunToManager(EdsPayslipTableItem singlePayrun) throws Exception;

    void sendPayslipToEmployees(Integer payslipTableID, boolean sendNotification) throws Exception;

    void sendPayslipToEmployees(Integer payslipTableID) throws Exception;

    void sendSinglePayrunToEmployee(EdsPayslipTableItem item) throws Exception;

    void sendCashAdvanceRequestToApprover(EdsCashAdvance cashAdvance, EdsCurrency currency) throws Exception;

    void sendCashAdvanceRejectMessageToEmployee(EdsCashAdvance cashAdvance) throws Exception;

    void sendAdditionalPaymentToApprover(EdsAdditionalPayment additionalPayment) throws Exception;

    void sendAdditionalPaymentToEmployee(EdsEmployee employee, EdsAdditionalPayment additionalPayment) throws Exception;

    void sentActualTimeReachedNotifation(EdsTask updateTask) throws Exception;

    void setHrReminderNotification(EdsEmailTemplate emailTemplate, String fieldValue, String reminderdate, EdsUser user, List<EdsUser> employees, List<EdsUser> recipents, Integer companyId);

    void setHrReminderNotificationTeamLeaderSpesific(EdsEmailTemplate emailTemplate, String fieldValue, String reminderdate, EdsUser user, Map<Integer, ArrayList<EdsUser>> teamLEaderMap, Integer companyId);

    void sendMeetingMinutesNotification(EdsUser creator, EdsMeetingMinutes meetingMinutes, EdsUser attendeesEmployee, String attende) throws EdsDbException;
//
//    void sendPenaltyPromotionNotification(EdsEmployeePenaltiesPromotions penaltiesPromotions, String typeCode) throws EdsDbException;

    void sendBenefitRequestNotification(String eventType, Integer entityID, Integer sourceID);

    void sendToAddNoteMessage(HistoryListItem item, EdsUser user, EdsNoteHistory noteHistory, EdsEmployee employee);

    void sendProjectClientSubmitMessage(EdsUser creator, CrmAccountItem client, Map<String, String> emails, EdsProject project, String url);

    void sendProjectClientApprovalMessage(EdsUser user, CrmAccountItem client, EdsProject project, List<EdsCrmContact> contactList, List<Integer> employees, String description, String url, String template);

    void sendEmployeeTemplateToApprover(EdsEmployeePayrollSettingsTemplate employeePayrollSettingsTemplate) throws Exception;

    void sendImportReportMessage(Integer importFileID);

    void sendNewsNotificationByLocation(EdsNews news, Integer updaterID, Integer userID, String subject);

    void sendOffice365EmailVerification(String email, String url);

    void sendMessageToOwnerAutoSync(Integer userID);

    void sendWorkflowRecurrenceRunNotification(EdsWorkflowRule workflowRule);

    void baseMailSender(EdsSuperMessage message);

    void setCustomerBalanceEmail(MessageItem messageItem, DateNonConvertable fromDatNC, DateNonConvertable toDateNC);

    void sentEmailAccountInactivationEmail(String email, String userName);

    void sendBillOfMaterialsNotification(EdsUser creator, EdsProject project, String status);

    void sendStripeWebhookPaymentNotification(UsagePlanItem usagePlanItem, UsagePlanPrice usagePlanPrice);

    void sendStripeOneTimeChargePaymentNotification(String subscriptionOperation, UsagePlanItem usagePlanItem, UsagePlanPrice usagePlanPrice, UsagePlanItem prevUsagePlan, UsagePlanPrice prevUsagePlanPrice);

    void sendIncorrectReportBalanceEmail(Integer transactionId);

    void sendEmailUserVisitToPage(ByteArrayOutputStream pdfStream);

    void sendShiftToApprover(EdsShift shift) throws Exception;

    void sendShiftToEmployee(EdsEmployee employee, EdsShift shift) throws Exception;

    void sendBackupsEmployeeToApprover(EdsBackupsEmployee backupsEmployee) throws Exception;

    void sendBackupsEmployeeToEmployee(EdsEmployee employee, EdsBackupsEmployee backupsEmployee) throws Exception;
}
