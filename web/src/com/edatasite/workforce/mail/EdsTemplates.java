package com.edatasite.workforce.mail;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.utils.EdsContextParams;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.NumberTool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_CREDIT_NOTE;
import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_SALEINVOICE;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.INVOICE_ITEMS;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.INVOICE_ITEM_DISCOUNT;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.INVOICE_ITEM_FAI_CATEGORY;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.INVOICE_ITEM_NAME;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.INVOICE_ITEM_PRICE;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.INVOICE_ITEM_QUANTITY;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.INVOICE_ITEM_VAT_RATE_ID;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.IS_CREDIT_NOTE;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.OBJECT_CLASS;

/**
 * Created by IntelliJ IDEA.
 * User: Iskandar
 * Date: 11-Aug-2007
 * Time: 09:22:54
 * To change this template use File | Settings | File Templates.
 */

// Static class launching templates

public class EdsTemplates {

    private static final Logger log = LoggerFactory.getLogger(EdsTemplates.class);

    public static final String ASSIGN_TO_PROJECT = "/project/assign.vt";

    public static final String ASSIGN_TO_TEAM = "/team/assign.vt";
    public static final String TEAM_ADD = "/team/add.vt";
    public static final String TEAM_ASSIGN_TEAMLEADER = "/team/assign_teamleader.vt";

    public static final String ASSIGN_TO_TASK = "/task/assign.vt";
    public static final String ASSIGN_TO_MULTI_TASK = "/task/multitaskassign.vt";
    public static final String ASSIGN_TO_MULTI_LEAD = "/task/multiAssignLead.vt";
    public static final String TASK_ACTUAL_TIME_REACHED = "/task/actualtimereached.vt";
    public static final String DELETE_TO_TASK = "/task/delete.vt";
    public static final String TASK_ADD = "/task/add.vt";
    public static final String TASK_UPDATE = "/task/update.vt";
    public static final String TASK_UPDATE_FOR_CLIENT = "/task/updateForClient.vt";
    public static final String TASK_COMPLETED_PREDECESSOR = "/task/completed_pred_task.vt";
    public static final String TASK_STATUS_COMPLETED = "/task/status_completed_task.vt";
    public static final String SEND_ADD_NOTE_TO_TASK_PROJECT = "/task/addnotetotask.vt";

    public static final String ASSIGN_TO_DEPARTMENT_GOAL = "/goal/departmentassign.vt";
    public static final String ASSIGN_TO_PROJECT_GOAL = "/goal/projectassign.vt";
    public static final String ASSIGN_TO_BUSINESS_GOAL = "/goal/businessassign.vt";

    public static final String PRODUCT_STOCK_NOTIFICATION = "/product/product_stock.vt";

    public static final String PROJECT_DELETE = "/project/delete.vt";
    public static final String PROJECT_UPDATE = "/project/update.vt";

    //Genesis Templates
    public static final String PROJECT_CLIENT_APPROVE = "/project/client_approve.vt";
    public static final String PROJECT_CLIENT_REJECT = "/project/client_reject.vt";
    public static final String PROJECT_SUBMIT_TO_CLIENT = "/project/client_submit.vt";

    public static final String ISSUE_ADD = "/issue/issueadd.vt";
    public static final String ISSUE_ADD_FOR_CLIENT = "/issue/issueaddToClient.vt";
    public static final String ISSUE_ASSIGN = "/issue/issueassign.vt";
    public static final String ISSUE_DELETE = "/issue/issuedelete.vt";
    public static final String ISSUE_UPDATE = "/issue/issueupdate.vt";
    public static final String ISSUE_UPDATE_FOR_CLIENT = "/issue/issueupdateToClient.vt";

    public static final String BENEFIT_REQUEST_SUBMITTED = "/benefit/benefitRequestSubmitted.vt";
    public static final String BENEFIT_REQUEST_APPROVED = "/benefit/benefitRequestApproved.vt";
    public static final String BENEFIT_REQUEST_REJECTED = "/benefit/benefitRequestRejected.vt";
    public static final String NEWS_NOTIFICATION = "/news/news_notification.vt";
    public static final String NEWS_UPDATE_NOTIFICATION = "/news/news_update_notification.vt";

    public static final String FREE_TRIAL = "/subscription/freetrial.vt";
    public static final String WEEKLY_SUBSCRIPTION = "/subscription/weeklyreport.vt";
    public static final String PAYMENT_SUBSCRIPTION_NOTIFICATION = "/subscription/payment_subscription.vt";
    public static final String STRIPE_ONE_TIME_CHARGE_PAYMENT_NOTIFICATION = "/subscription/stripe_one_time_charge_payment.vt";
    public static final String INSUFFICIENT_FUNDS = "/subscription/insufficient_funds.vt";
    public static final String PRICING_PAGE_VIEW_NOTIFICATION = "/subscription/pricing_page_view.vt";

    public static final String NOTIFICATION = "/signUp/notification.vt";
    public static final String NOTIFICATION_FROM_MOBILE = "/signUp/notification_from_mobile.vt";
    public static final String SIGN_UP_FOR_ADMINISTRATOR = "/signUp/for_administrator.vt";
    public static final String SIGN_UP_FOR_ADMINISTRATOR_WITHOUT_ACTIVATION_LINK_FROM_MOBILE = "/signUp/for_administrator_without_activation_link_from_mobile.vt";
    public static final String RESEND_SIGN_UP_FOR_ADMINS = "/signUp/resend_activation_link.vt";
    public static final String PASSWORD_CHANGED = "/signUp/passwordChanged.vt";
    public static final String SOLR_OPTIMIZE = "/issue/solrOptimize.vt";
    public static final String DAILYREPORT_SEND = "/progress/dailyReport.vt";
    public static final String NEW_SUBSCRIPTION_EXPIRATION_REPORT = "/progress/NewSubscriptionExpirationReport.vt";
    public static final String SUBSCRIPTION_EXPIRATION_REPORT_ONE_DAY = "/progress/subscriptionExpirationReportOneDay.vt";
    public static final String FREE_TRIAL_EXPIRATION_REPORT = "/progress/freeTrialExpirationReport.vt";
    public static final String SUBSCRIPTION_EXPIRED_REPORT = "/progress/subscriptionExpiredReport.vt";
    public static final String REFER_SOMEONE = "/refer_someone/referSomeone.vt";
    public static final String POSTED_TO_FACEBOOK = "/refer_someone/postedToFacebook.vt";
    public static final String SIGN_UP_FOR_ADMINISTRATOR_WITHOUT_ACTIVATION_LINK = "/signUp/for_administrator_without_activation_link.vt";

    public static final String EMPLOYEE_ADD = "/employee/addEmployee.vt";
    public static final String EMPLOYEE_ADD_FOR_EXISTING_USERNAME = "/employee/addEmployeeAgain.vt";
    public static final String EMPLOYEE_ADD_FROM_GOOGLE_MARKET = "/employee/addEmployeeFromGoogleMarket.vt";

    public static final String EMPLOYEE_ACTIVATE = "/employee/activateEmployee.vt";
    public static final String CLIENT_CONTACT = "/client/client.vt";
    public static final String CLIENT_CONTACT_WITHOUT_ACTIVATION_LINK = "/client/client_without_activation_link.vt";
    public static final String STOREFRONT_CLIENT_CONTACT = "/client/storefront_client.vt";
    public static final String CLIENT_WITH_USERNAME_PASSWORD = "/client/client_with_username_password.vt";
    public static final String CLIENT_WITHOUT_USERNAME_PASSWORD = "/client/client_without_username_password.vt";

    public static final String SENDER_INVOICING_MESSAGE = "/invoice/invoice_manager.vt";
    public static final String QUOTE_MANAGER_APPROVE_MESSAGE = "/invoice/quote_manager_approve.vt";
    public static final String QUOTE_MANAGER_REJECT_MESSAGE = "/invoice/quote_manager_reject.vt";
    public static final String QUOTE_CLIENT_APPROVE_MESSAGE = "/invoice/quote_client_approve.vt";
    public static final String QUOTE_CLIENT_REJECT_MESSAGE = "/invoice/quote_client_reject.vt";
    public static final String QUOTE_CANCELLED_MESSAGE = "/invoice/quote_cancelled.vt";
    public static final String PO_APPROVED_MESSAGE = "/invoice/purchase_order_approve.vt";
    public static final String PO_REJECTED_MESSAGE = "/invoice/purchase_order_reject.vt";
    public static final String ADDON_REQUEST_MESSAGE = "/invoice/addon_request.vt";
    public static final String RFQ_REQUEST_MESSAGE = "/invoice/rfq_request.vt";
    public static final String RFQ_SUPPLIER_BID_MESSAGE = "/invoice/rfq_supplierbid.vt";
    public static final String CONSOLIDATED_INVOICE_MESSAGE = "/invoice/consolidated_invoice.vt";
    public static final String INCORRECT_REPORT_BALANCE = "/invoice/incorrect_report_balance.vt";

    //////////////////////AVAILABILITY////////////////////////////////////////////
    public static final String SEND_TO_SELECTED_EMPLOYEE_ADD_REQUEST = "/requests/request_comment.vt";

    public static final String FORGOT_PASSWORD = "/signUp/forgotPassword.vt";
    public static final String WFP_FORGOT_PASSWORD = "/signUp/wfpForgotPassword.vt";
    public static final String FORGOT_WITH_ACTIVATION_LINK = "/signUp/forgot_with_activation_link.vt";

    // BRAIN Templates
    public static final String FORGOT_PASSWORD_BRAINUZ = "/signUp/forgotPassword_brainuz.vt";
    public static final String FORGOT_WITH_ACTIVATION_LINK_BRAINUZ = "/signUp/forgot_with_activation_link_brainuz.vt";
    public static final String RESEND_ACTIVATION_LINK_BRAINUZ = "/signUp/resend_activation_link_brainuz.vt";
    public static final String EMPLOYEE_ADD_BRAINUZ = "/employee/addEmployeeBrainUz.vt";


    public static final String SEND_BUG_REPORT = "/issue/bug_report.vt";
    public static final String SEND_BUG_REPORT_CHANGED = "/issue/bug_report_changed.vt";

    public static final String EXPENSE_SUBMITTED_TO_EMPLOYEE = "/expense/submittedToEmployee.vt";
    public static final String EXPENSE_SUBMITTED_TO_APPROVER = "/expense/submittedToApprover.vt";
    public static final String EXPENSE_DECLINED_TO_EMPLOYEE = "/expense/declinedToEmployee.vt";
    public static final String EXPENSE_APPROVED_TO_EMPLOYEE = "/expense/approvedToEmployee.vt";
    public static final String EXPENSE_RESUBMITTED_TO_APPROVER = "/expense/resubmittedToApprover.vt";

    public static final String BATCH_PAYMENT_SEND_TO_CLIENT = "/accounting/sendToClient.vt";

    public static final String FIXED_ASSET_COUNT_RESULT = "/fixedasset/fixedassetcountresult.vt";

    //Reply message,which will sended second time
    public static final String REPLY_EMPLOYEE_ADD = "/employee/replyaddEmloyee.vt";
    public static final String REPLY_CLIENT_CONTACT = "/client/reply_client.vt";

    ////////////////CALENDAR EVENT////////////////////////////////////////////
    public static final String CALENDAR_SHARE_EVENT = "/calendarevent/shareevent.vt";
    public static final String CALENDAR_DELETE_EVENT = "/calendarevent/cancelevent.vt";
    public static final String CALENDAR_SHARE_EVENT_FOR_EMPLOYEES = "/calendarevent/shareeventtoemployees.vt";
    public static final String CALENDAR_DELETE_EVENT_FROM_EMPLOYEE = "/calendarevent/canceleventfromemployee.vt";
    public static final String CALENDAR_EVENT_INVITATION_FOR_GUESTS = "/calendarevent/invitationtoeventguests.vt";
    public static final String CALENDAR_EVENT_INVITATION_RESULT_FOR_OWNER = "/calendarevent/invitationResponce.vt";
    public static final String CALENDAR_ADD_EVENT_REMINDER = "/calendarevent/eventreminder.vt";
    //////////////MEETING MINUTES////////////////////////////////////////////////////////////////
    public static final String MEETING_MINUTES = "/meetingminutes/meetingminutes.vt";

    /////////////////REPORT/////////////////////////////////////
    public static final String REPORT_SCHEDULED_REPORT = "/report/scheduledreport.vt";

    /////////////////TIMESHEET APPROVAL/////////////////////////////////////
    public static final String TIMESHEET_APPROVAL_FOR_MANAGER = "/timesheetapproval/timesheetforapproval.vt";
    public static final String TIMESHEET_PROCEEDED = "/timesheetapproval/timesheetproceedednotification.vt";

    //////////////////PAYROLL//////////////////////////////////////////////
    public static final String PAYSLIP = "/payroll/payslip.vt";
    public static final String PAYSLIP_SUMBIT_TO_MANAGER = "/payroll/payslip_submit_to_manager.vt";
    public static final String SINGLE_PAYRUN_SUBMIT_FOR_APPROVAL = "/payroll/singlePayrunSubmitForApproval.vt";
    public static final String PAYSLIP_APPROVED_TO_EMPLOYEE = "/payroll/approvedToEmployee.vt";
    public static final String PAYSLIP_CASH_ADVANCED_TO_MANAGER = "/payroll/cashAdvanceSubmitToManager.vt";
    public static final String PAYSLIP_CASH_ADVANCED_REJECT_TO_EMPLOYEE = "/payroll/cashAdvanceRejectMessageToEmployee.vt";
    public static final String PAYSLIP_EMPLOYEE_TEMPLATE_TO_MANAGER = "/payroll/employeeTemplateSubmitToManager.vt";
    public static final String ADDITIONAL_PAYMENT_TO_APPROVER = "/payroll/additionalPaymentToApprover.vt";
    public static final String ADDITIONAL_PAYMENT_TO_EMPLOYEE = "/payroll/additionalPaymentToEmployee.vt";

    ////////////////CRM CASE////////////////////////////////////////////
    public static final String OPPORTUNITY_CREATED = "/crm/opportunityCreated.vt";
    public static final String OPPORTUNITY_ASSIGNED = "/crm/opportunityAssigned.vt";

    public static final String EVENTS_REPORT = "/progress/eventList.vt";

    ////////////////OVERDUE INVOICES FOR CRON/////////////////////////////////////
    public static final String OVERDUE_INVOICES_FOR_CRON = "/crontemplate/invoiceoverdue.vt";

    ////////////////OVERDUE INVOICES FOR CRON For Client /////////////////////////////////////
    public static final String OVERDUE_INVOICES_FOR_CRON_FOR_CLIENT = "/crontemplate/invoiceoverdue_client.vt";

    ////////////////TIMESHEET REMINDER/////////////////////////////////////
    public static final String TIMESHEET_REMINDER = "/crontemplate/timesheetreminder.vt";

    /////////////////////////////TASK OVER DUE DATE REMINDER/////////////////////////////
    public static final String TASK_ADD_DUE_DATE_REMINDER = "/crontemplate/taskduedatereminder.vt";

    /////////////////////////////PROJECT OVER DUE DATE REMINDER/////////////////////////////
    public static final String PROJECT_ADD_DUE_DATE_REMINDER = "/crontemplate/projectduedatereminder.vt";

    /////////////////////////////WORKSTREAM OVER DUE DATE REMINDER/////////////////////////////
    public static final String WORKSTREAM_ADD_DUE_DATE_REMINDER = "/crontemplate/workstreamduedatereminder.vt";

    /////////////////////////////EMPLOYEE VISA EXPIRATION DATE REMINDER/////////////////////////////
    public static final String EMPLOYEE_VISA_EXPIRATION_DATE_REMINDER = "/crontemplate/employeevisaexpirationdatereminder.vt";


    public static final String GOOGLE_CONTACT_SYNC = "/googleContact/googleContactSync.vt";

    public static final String IMPORT_CUSTOM_EVENT = "/crm/importCustomEvent.vt";

    public static final String POST_QUESTION_TO_EXPERT = "/expert/expertQuestion.vt";

    public static final String DOCUMENT_UPLOAD_TO_TASK = "/document/docUploadToTask.vt";
    public static final String DOCUMENT_UPLOAD_TO_PROJECT = "/document/docUploadToProject.vt";
    public static final String DOCUMENT_UPLOAD_TO_ISSUE = "/document/docUploadToIssue.vt";

    ////////////////////Training Center templates////////////////////////////////////////
    public static final String TRAINING_COURSE_SMS_SEND = "/trainingcenter/course_sms.vt";

    public static final String SEND_ADD_NOTE_MESSAGE = "/note/workspaceNote.vt";

    public static final String WARNING_YOUR_CALENDAR_AUTO_SYNC = "/crm/warningYourCalendarAutoSync.vt";
    public static final String RECURRENCE_WORKFLOW_RUN = "/workflow/recurrenceWorkflowRun.vt";
    public static final String EMAIL_ACCOUNT_INACTIVATION = "/crm/emailAccountInactivation.vt";

    public static final String INITIATE_ASSESSMENT = "/assessment/initiate.vt";
    public static final String INITIATE_ASSESSMENT_FOR_INITIATOR = "/assessment/initiateForInitiator.vt";
    public static final String INITIATE_360_ASSESSMENT = "/assessment/initiate360.vt";
    public static final String INITIATE_360_ASSESSMENT_FOR_REVIEWER = "/assessment/initiate360initiator.vt";
    public static final String REVIEW_ASSESSMENT = "/assessment/review.vt";
    public static final String REVIEW_ASSESSMENT_FOR_EMPLOYEE = "/assessment/reviewForEmployee.vt";
    public static final String RATE_ASSESSMENT = "/assessment/rate.vt";
    public static final String RATE_ASSESSMENT_FOR_INITIATOR = "/assessment/rateForInitiator.vt";
    public static final String APPROVE_ASSESSMENT = "/assessment/approve.vt";
    public static final String APPROVE_ASSESSMENT360_FOR_EMPLOYEE = "/assessment/approve360ForEmployee.vt";
    public static final String APPROVE_ASSESSMENT_FOR_INITIATOR = "/assessment/approveForInitator.vt";
    public static final String APPROVE_ASSESSMENT360_FOR_INITIATOR = "/assessment/approve360ForInitator.vt";
    public static final String INITIATE_360_REMINDER = "/assessment/initiateReminder.vt";
    public static final String DELETE_ASSESSMENT = "/assessment/deleteAssessment.vt";
    public static final String ASSESSMENT_APPROVE_NOTIFICATION_FOR_HR = "/assessment/assessmentApproveNotificationHR.vt";
    public static final String ASSESSMENT_APPROVAL_OR_REJECT_NOTIFICATION_BY_HR = "/assessment/assessmentApprovalOrRejectByHrNotification.vt";
    public static final String ASSESSMENT_RECURRENT_NOTIFICATION_FOR_HR = "/assessment/recurrentAssessmentNotification.vt";

    public static final String PEER_360_RATE = "/assessment/peer360rate.vt";
    public static final String MANAGER_360_RATE = "/assessment/manager360rate.vt";
    public static final String CLIENT_360_RATE = "/assessment/client360rate.vt";
    public static final String EMPLOYEE_360_REVIEW = "/assessment/employee360review.vt";
    public static final String EMPLOYEE_RESPONSE_360 = "/assessment/employeeResponse360.vt";

    public static final String COLLABORATOR_360_REVIEW = "/assessment/collaborator360review.vt";
    public static final String PEER_360_REVIEW = "/assessment/peer360review.vt";
    public static final String MANAGER_360_REVIEW = "/assessment/manager360review.vt";
    public static final String CLIENT_360_REVIEW = "/assessment/client360review.vt";
    public static final String ONE_OFF_360_MANAGER_REVIEW = "/assessment/initiate360OneOffManager.vt";
    public static final String ONE_OFF_360_CLIENT_REVIEW = "/assessment/initiate360OneOffClient.vt";
    public static final String ONE_OFF_360_PEER_REVIEW = "/assessment/initiate360OneOffPeer.vt";
    public static final String ONE_OFF_360_COLLABORATOR_REVIEW = "/assessment/oneoff360collaboratorReview.vt";

    public static String processTemplate(Map<String, Object> context, String name) throws EdsTemplateException {
        try {
            String language = "";
            UserEmailSettingsManager userEmailSettingsManager = (UserEmailSettingsManager) ApplicationContextProvider.applicationContext.getBean("userEmailSettingsManager");
            EdsUser currentUser = userEmailSettingsManager.getUser();
            String hostName = EdsContextParams.getHostname();
            if (currentUser != null) {
                language = userEmailSettingsManager.getUserSettings(currentUser).getInternationalization();
            }
            language = !StringUtil.isEmpty(language) ? language : EdsContextParams.getDefaultLocale(hostName).toString();

            if (!language.equals("") && (language.equals("ru") || language.equals("nl") || language.equals("uz"))) {
                name = "/localization/" + language + name;
                log.info("Sent Message Language:  " + language);
            } else {
                log.info("Sent Message Language:   English");
            }
            name = getTemplateName(name);
            //boolean resourceExists = Velocity.resourceExists(name);
            Template template = Velocity.getTemplate(name);
            StringWriter writer = new StringWriter();
            EdsVelocityContext con = new EdsVelocityContext();
            context.put("companyInfo", EdsContextParams.getCompanyInfo());
            context.put("numberFormatter", new NumberTool());
            if (!context.containsKey("helphost")) {
                context.put("helphost", EdsContextParams.getHelpHost());
            }
            if (!context.containsKey("productname")) {
                context.put("productname", EdsContextParams.getProductName());
            }
            if (!context.containsKey("hostname")) {
                context.put("hostname", EdsContextParams.getHostname());
            }
            if (!context.containsKey("HOST")) {
                context.put("HOST", EdsContextParams.getHost());
            }
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                con.put(entry.getKey().replaceAll("[{}$]", ""), entry.getValue()); //replaceAllni Hayot qo'ydi. o'zi javob beradi(xato chiqsa)(Ilhom J.).
            }
            template.merge(con, writer);
            return writer.toString();
        } catch (Throwable t) {
            log.error("Failed to load template:", t);
            throw new EdsTemplateException(t);
        }
    }

    public static String processTemplate(EdsUser currentUser, Map<String, Object> context, String name) throws EdsTemplateException {
        try {
            String language = "";
            String hostName = EdsContextParams.getHostname();
            if (currentUser != null) {
                UserEmailSettingsManager userEmailSettingsManager = (UserEmailSettingsManager) ApplicationContextProvider.applicationContext.getBean("userEmailSettingsManager");
                language = userEmailSettingsManager.getUserSettings(currentUser).getInternationalization();
            }
            language = !StringUtil.isEmpty(language) ? language : EdsContextParams.getDefaultLocale(hostName).toString();

            if (!language.equals("") && (language.equals("ru") || language.equals("nl") || language.equals("uz"))) {
                name = "/localization/" + language + name;
                log.info("Sent Message Language:  " + language);
            } else {
                log.info("Sent Message Language:   English");
            }
            name = getTemplateName(name);
            log.info("=== Sent Message From Default Method ===");

//            name = "/localization/" + language + name;
            Template template = Velocity.getTemplate(name);
            StringWriter writer = new StringWriter();
            EdsVelocityContext con = new EdsVelocityContext();
            context.put("companyInfo", EdsContextParams.getCompanyInfo());
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                con.put(entry.getKey(), entry.getValue());
            }
            template.merge(con, writer);
            return writer.toString();
        } catch (Throwable t) {
            log.error("Failed to load template:", t);
            throw new EdsTemplateException(t);
        }
    }

    public static String processTemplateWhiteLabel(EdsUser currentUser, Map<String, Object> context, String name) throws EdsTemplateException {
        try {
            String language = "uz";
            if (currentUser != null) {
                EdsCompany company = currentUser.getCompany();
                language = company.getLocale() != null ? company.getLocale() : "en";
            }

            if ((language.equals("ru") || language.equals("en") || language.equals("uz"))) {
                name = "/localization/" + language + name;
                log.info("Sent Message Language:  {}", language);
            } else {
                log.info("Sent Message Language: English");
            }
            log.info("=== Sent Message From White Label ===");

            Template template = Velocity.getTemplate(name);
            StringWriter writer = new StringWriter();
            EdsVelocityContext con = new EdsVelocityContext();
            context.put("companyInfo", EdsContextParams.getCompanyInfo());
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                con.put(entry.getKey(), entry.getValue());
            }
            template.merge(con, writer);
            return writer.toString();
        } catch (Throwable t) {
            log.error("Failed to load template:", t);
            throw new EdsTemplateException(t);
        }
    }


    public static String processTemplate(Map<String, Object> context, String name, String locale) throws EdsTemplateException {
        try {
            String hostName = EdsContextParams.getHostname();
            locale = !StringUtil.isEmpty(locale) ? locale : EdsContextParams.getDefaultLocale(hostName).toString();

            if (!locale.equals("") && (locale.equals("ru") || locale.equals("nl") || locale.equals("uz"))) {
                name = "/localization/" + locale + name;
                log.info("Sent Message Language:  " + locale);
            } else {
                log.info("Sent Message Language:   English");
            }
            name = getTemplateName(name);

            Template template = Velocity.getTemplate(name);
            StringWriter writer = new StringWriter();
            EdsVelocityContext con = new EdsVelocityContext();
            context.put("companyInfo", EdsContextParams.getCompanyInfo());
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                con.put(entry.getKey(), entry.getValue());
            }
            template.merge(con, writer);
            return writer.toString();
        } catch (Throwable t) {
            log.error("Failed to load template:", t);
            throw new EdsTemplateException(t);
        }
    }

    public static String evaluateTemplate(Map<String, Object> map, String inputString) throws EdsTemplateException {
        try {
            VelocityContext context = new VelocityContext();
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    context.put(entry.getKey().substring(2, entry.getKey().length() - 1), entry.getValue());
                }
            }
            context.put("companyInfo", EdsContextParams.getCompanyInfo());
            StringWriter writer = new StringWriter();
            if (inputString != null) {
                Velocity.evaluate(context, writer, "LOG", inputString);
            }

            return writer.toString();
        } catch (Throwable t) {
            log.error("Failed to evalute :", t);
            throw new EdsTemplateException(t);
        }
    }

    public static String evaluateTemplateForWebHook(Map<String, Object> map, String inputString) throws EdsTemplateException {
        try {
            VelocityContext context = new VelocityContext();
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof String valueStr) {
                        JSONParser parser = new JSONParser();
                        JSONObject json = null;
                        JSONArray jsonArray = null;
                        if (valueStr.contains("{")) {
                            try {
                                if (((String) value).startsWith("[")) {
                                    jsonArray = (JSONArray) parser.parse(((String) "[" + value + "]"));
                                } else if (((String) value).startsWith("{")) {
                                    json = (JSONObject) parser.parse(((String) value));
                                } else {
                                    json = (JSONObject) parser.parse(((String) "{" + value + "}"));
                                }
                            } catch (Exception e) {
                                log.error("*******************************Json Parse Error : {}", ((String) value));
                            }
                        }

                        value = ((String) value).replace("\n", " ");
                        value = ((String) value).replace("\t", "  ");
                        if (json == null && jsonArray == null) {
                            if (((String) value).contains("\\")) {
                                value = ((String) value).replace("\\", "");
                            } else {
                                value = ((String) value).replace("\"", "\\\"");
                            }
                        }
                    }
                    context.put(entry.getKey().substring(2, entry.getKey().length() - 1), value);
                }
            }
            context.put("companyInfo", EdsContextParams.getCompanyInfo());

            if (inputString != null && inputString.contains("#forbegin") && inputString.contains("#forend")) {
                String forPattern = "#forbegin(.*?)#forend";
                Pattern pattern = Pattern.compile(forPattern, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(inputString);

                while (matcher.find()) {
                    String loopBlock = matcher.group(1).trim();
                    StringBuilder itemsBuilder = new StringBuilder();

                    if (map.containsKey(INVOICE_ITEMS) && map.get(INVOICE_ITEMS) instanceof List) {
                        List<EdsInvoiceItem> invoiceItems = (List<EdsInvoiceItem>) map.get(INVOICE_ITEMS);

                        for (EdsInvoiceItem item : invoiceItems) {
                            String itemBlock = loopBlock;
                            Integer faiVat;
                            BigDecimal priceWithNoTax;
                            Integer faiCategoryId;
                            if (item.getTaxCalculationType() == 1) {
                                BigDecimal taxPerItem = item.getTaxAmount().divide(item.getQty(), 9, RoundingMode.FLOOR);
                                priceWithNoTax = item.getUnitPrice().subtract(taxPerItem);
                                faiCategoryId = item.getFaiCategory() != null ? item.getFaiCategory().getSorder() : null;
                                faiVat = Optional.ofNullable(item.getVat())
                                        .map(edsVat -> getVatByObjectClass(edsVat, map.get(OBJECT_CLASS)))
                                        .map(EdsReference::getSorder)
                                        .orElse(null);
                            } else if (item.getTaxCalculationType() == 2) {
                                priceWithNoTax = item.getUnitPrice();
                                faiCategoryId = item.getFaiCategory() != null ? item.getFaiCategory().getSorder() : null;
                                faiVat = Optional.ofNullable(item.getVat())
                                        .map(edsVat -> getVatByObjectClass(edsVat, map.get(OBJECT_CLASS)))
                                        .map(EdsReference::getSorder)
                                        .orElse(null);
                            } else {
                                priceWithNoTax = item.getUnitPrice();
                                faiCategoryId = 6;
                                faiVat = TYPE_SALEINVOICE.equals(map.get(OBJECT_CLASS)) || TYPE_CREDIT_NOTE.equals(map.get(OBJECT_CLASS)) ? 145 : 135; // 0% vat rate, when selected tax type is NO_TAX
                            }
                            if (itemBlock.contains(INVOICE_ITEM_PRICE) && item.getUnitPrice() != null)
                                itemBlock = itemBlock.replace(INVOICE_ITEM_PRICE, String.valueOf(priceWithNoTax));
                            if (itemBlock.contains(INVOICE_ITEM_QUANTITY) && item.getQty() != null)
                                itemBlock = itemBlock.replace(INVOICE_ITEM_QUANTITY, String.valueOf(item.getQty()));
                            if (itemBlock.contains(INVOICE_ITEM_VAT_RATE_ID))
                                itemBlock = itemBlock.replace(INVOICE_ITEM_VAT_RATE_ID, String.valueOf(Boolean.TRUE.equals(map.get(IS_CREDIT_NOTE)) ? faiVat + 1 : faiVat));
                            if (itemBlock.contains(INVOICE_ITEM_FAI_CATEGORY) && faiCategoryId != null) {
                                itemBlock = itemBlock.replace(INVOICE_ITEM_FAI_CATEGORY, String.valueOf(faiCategoryId));
                            }
                            if (itemBlock.contains(INVOICE_ITEM_DISCOUNT)) {
                                BigDecimal discount = item.getItemCalculatedDiscountAmount();
                                itemBlock = itemBlock.replace(INVOICE_ITEM_DISCOUNT, discount != null ? String.valueOf(discount) : "0");
                            }
                            if (itemBlock.contains(INVOICE_ITEM_NAME))
                                itemBlock = itemBlock.replace(INVOICE_ITEM_NAME, item.getName() != null ? item.getName() : "");
                            itemsBuilder.append(itemBlock).append(",");
                        }

                        if (!itemsBuilder.isEmpty()) {
                            itemsBuilder.setLength(itemsBuilder.length() - 1);
                        }

                        inputString = matcher.replaceFirst(itemsBuilder.toString());
                        matcher = pattern.matcher(inputString);
                    }
                }
            }

            StringWriter writer = new StringWriter();
            if (inputString != null) {
                Velocity.evaluate(context, writer, "LOG", inputString);
            }

            return writer.toString();
        } catch (Throwable t) {
            log.error("Failed to evalute :", t);
            throw new EdsTemplateException(t);
        }
    }

    private static EdsReference getVatByObjectClass(EdsVat edsVat, Object objectClass) {
        return TYPE_SALEINVOICE.equals(objectClass) || TYPE_CREDIT_NOTE.equals(objectClass) ? edsVat.getFaiVat() : edsVat.getFaiPurchaseVat();
    }


    private static String getTemplateName(String name) {
        if (EdsContextParams.isCustomMailTemplate()) {
            String prevName = name;
            name = "/_custom_templates/" + EdsContextParams.getProductName().toLowerCase() + name;
            boolean resourceExists = Velocity.resourceExists(name);
            if (!resourceExists) {
                name = prevName;
            }
        }
        return name;
    }
}
