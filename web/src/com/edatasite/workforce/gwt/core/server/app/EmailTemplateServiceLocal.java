package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsContract;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 21.07.2010
 * Time: 20:10:01
 */
public interface EmailTemplateServiceLocal {

    EmailTemplateItem generateReplyToReporterCaseItem(EntityToEmailTemplate emailTemplate, Integer autoResponseID);

    EmailTemplateItem generateEmailTemplateData(EntityToEmailTemplate entityToEmailTemplate, Integer senderID);

    EmailTemplateItem generateEmailTemplateForAccountingComposeView(EntityToEmailTemplate entityToEmailTemplate, Integer senderID);

    EmailTemplateItem generateEmailTemplateForTask(EdsEmployeeTask employeeTask, EdsUser user, String entityType);

    EmailTemplateItem generateEmailTemplateForTaskUpdate(EdsUser receiver, EdsUser user, EdsTask task, String entityType);

    EmailTemplateItem generateEmailTemplateItemForMultiTaskAssign(EdsUser creator, EdsEmployee employee, HashSet<EdsTask> tasks);

    EmailTemplateItem generatedEmailTemplateItems(EdsEmployeeEvent employeeEvent, ArrayList<EdsUser> attendees, String entityType);

    EmailTemplateItem generatedReportTemplateItems(EdsReport report, EdsUser user, String entityType);

    EmailTemplateItem generatedEmailTemplateItems(EdsEvent event, EdsUser attendee, String entityType);

    EmailTemplateItem generateProjectTemplateItem(EdsProject project, EdsUser employee, EdsUser user, String category);

    EmailTemplateItem generateProjectAssignTemplateItem(EdsProjectEmployee projectEmployee, EdsUser user, String entityType);

    EmailTemplateItem generateProjectManagerAssignTemplateItem(EdsProject project, EdsEmployee employee, EdsUser user, String entityType);

    EmailTemplateItem generateMessageCenterTemplateItem(EntityToEmailTemplate entityToEmailTemplate, Integer rfqId, Integer employeeId, Integer opportunityId);

    EmailTemplateItem generateSickRequestTemplateTo_Admin(EdsUser sender, EdsUser receiver, EdsSickRequest request, String templateCategory);

    EmailTemplateItem generateSickRequestTemplateTo_Employee(EdsUser sender, EdsUser receiver, EdsUser employee, String status, EdsSickRequest request, String templateCategory);

    EmailTemplateItem generateClientActivationNewUserEmailTemplate(EdsClientContact client, EdsUser creator, String subject);

    EmailTemplateItem generateClientActivationExistingUserEmailTemplate(EdsClientContact client, EdsUser creator, String subject);

    EmailTemplateItem generateEmployeeActivationNewUserEmailTemplate(EdsEmployee employee, EdsUser admin);

    EmailTemplateItem generateEmployeeActivationExistingUserEmailTemplate(EdsEmployee employee, EdsUser admin);

    EmailTemplateItem generateUserAccountConfirmationEmailTemplate(EdsUser user);

    EmailTemplateItem generateEmployeeActivatedByManagerEmailTemplate(EdsEmployee employee);

    EmailTemplateItem getOverdueReminderForClientTemplateItem(EdsEmailTemplate companyEmailTemplate, NewInvoice overdueInvoice, EdsUser user);

    EmailTemplateItem generateIssueTemplateItem(EdsIssue issue, EdsUser employee, EdsUser user, String category);

    EmailTemplateItem getDocumentUploadTemplateItem(EdsUser user, EdsEmployee receiver, String creatorName, String fileName, String description, String relatedToName, String creationDate, String linkURL, String templateCategoryType);

    EmailTemplateItem generateWebFormThankYouEmailTemplateItem(Integer emailTemplateID, String toEmail, EdsUser user);

    EmailTemplateItem generateCalendarInvitationGuests(EdsUser user, EdsEvent event, String guestsEmail, String guestNames, String action);

    EmailTemplateItem generateCourseBookingConfirm(EdsCourseBooking courseBooking, EdsUser user, String toEmail);

    EmailTemplateItem generateStudentCourseBookingConfirm(EdsCourseScheduleStudent courseScheduleStudent, EdsUser user);

    ArrayList<SmsSendItem> generateSmsMessagesForCourseBooking(List<EdsCourseScheduleStudent> courseScheduleStudents, String smsSubject);

    EmailTemplateItem generateEmailTemplateItem(EdsEmailTemplate template, Map<String, Object> values, String toEmail, Integer fromUserID, String fromEmail);

    SelectItem[] getMessageCenterEmailTemplates(ArrayList<String> templateModules);

    SelectItem[] getEmailTemplates(String templateCategory);

    EmailTemplateItem generateSendPayslipToManagerEmailTemplate(EdsPayslipTable payslipTable);

    EmailTemplateItem generateSendSinglePayrunToManagerEmailTemplate(EdsPayslipTableItem singlePayrun);

    EmailTemplateItem generateEmailTemplateForPersonalGoal(EdsGoal goal, EdsEmployee employee, String entityType, List<EdsEmployee> goalAssigns);

    EmailTemplateItem generateEmailTemplateForProductStock(EdsItem item, EdsUser user, String items);

    EmailTemplateItem generateEmailTemplateItemForActualTimeReached(EdsUser user, EdsUser receiver, EdsTask edsTask);

    EmailTemplateItem generateEmailTemplateItemForHrReminder(EdsUser user, EdsUser receiver, EdsEmailTemplate emailTemplate, String fieldValue, String reminderdate, List<EdsUser> employees, Integer companyId);

    EmailTemplateItem generateEmailTemplateItemForMeetingMinutesNotification(EdsUser creator, EdsMeetingMinutes meetingMinutes, EdsUser attendeesEmployee, String attendee);
//
//    EmailTemplateItem generateEmailTemplateItemForPenaltyPromotion(EdsUser creator, EdsUser receiver, String projectName, EdsEmployeePenaltiesPromotions penaltyPromotion, String categoryCode);

    EmailTemplateItem generateEmailTemplateItemForContractReminder(EdsUser creator, EdsUser receiver, EdsContract contract, EdsEmailTemplate edsEmailTemplate, EdsRecurrence recurrence);

    EmailTemplateItem generateEmailTemplateItemForTaskReminder(EdsTask task, EdsEmployeeTask employeeTask, EdsEmailTemplate emailTemplate);

    String getReplyToById(Integer id);

    EmailTemplateItem generateEmailTemplateItemForBillOfMaterials(EdsUser creator, EdsProject project, String status, EdsEmployee user);
}
