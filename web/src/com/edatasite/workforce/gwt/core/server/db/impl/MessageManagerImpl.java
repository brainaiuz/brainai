package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.components.SessionCryptor;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.mail.EdsMailer;
import com.edatasite.shared.mail.Upload;
import com.edatasite.shared.sms.SmsProvider;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsBackupsEmployee;
import com.edatasite.workforce.core.domain.EdsBenefitRequest;
import com.edatasite.workforce.core.domain.EdsBugReport;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsContract;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsGoogleCalendarEventGuests;
import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.core.domain.EdsMessage;
import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsShift;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.core.domain.EdsSuperMessage;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTaskHistory;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserActivation;
import com.edatasite.workforce.core.domain.EdsUserContact;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.assessment.EdsAppraisalApproval;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.EdsSmsSendItem;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettingsTemplate;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTask;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetList;
import com.edatasite.workforce.gwt.assessment.client.rpc.DepartmentPeriodAppraisalItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.MessageTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.PaymentTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AdditionalPaymentRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.PayslipTableRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.AssessmentUtils;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.ZoomService;
import com.edatasite.workforce.gwt.core.server.db.ActivationLinkManager;
import com.edatasite.workforce.gwt.core.server.db.AssessmentManager;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.db.BlackListManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyEmailManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmailNotificationSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeEventManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarEventGuestsManager;
import com.edatasite.workforce.gwt.core.server.db.HostBasedSettingManager;
import com.edatasite.workforce.gwt.core.server.db.ImportFileManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.SmsManager;
import com.edatasite.workforce.gwt.core.server.db.SmsSendItemManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.UserActivationManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TCScheduledTaskManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.BenefitRequestEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanPrice;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.profile.client.ui.EmailNotificationConstants;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetItem;
import com.edatasite.workforce.mail.EdsSubjects;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.mail.EdsTemplates;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.CRM.CRM_CONTACT.EVENT_END_DATE;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.CRM.CRM_CONTACT.EVENT_START_DATE;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.CRM.CRM_CONTACT.ZOOM_LINK;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.OBJECT_KEY;

@Transactional
@Repository("messageManager")
public class MessageManagerImpl extends BaseManager<EdsMessage> implements MessageManager, EdsSubjects, Constants, Comparator {

    private static final Logger log = LoggerFactory.getLogger(MessageManagerImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    @Qualifier("payrollService")
    private PayrollService payrollService;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private CompanyEmailManager companyEmailManager;
    @Autowired
    private UserSessionManager userSessionManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private AssessmentManager assessmentManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    @Qualifier("fixedAssetsCountPDFHandler")
    private IPostPDFHandler fixedAssetsCountPdfHandler;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    private UserActivationManager userActivationManager;
    @Autowired
    private BlackListManager blackListManager;
    @Autowired
    private EmailNotificationSettingsManager emailNotificationSettingsManager;
    @Autowired
    private EmailSettingsManager emailSettingsManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    @Qualifier("emailTemplateService")
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private GoogleCalendarEventGuestsManager eventGuestsManager;
    @Autowired
    private EmployeeEventManager employeeEventManager;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    private TCScheduledTaskManager tcScheduledTaskManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;
    @Autowired
    private EmailAttachmentManager emailAttachmentManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    @Qualifier("singlePayrunPdfHandler")
    private IPostPDFHandler singlePayrunPdfHandler;
    @Autowired
    @Qualifier("additionalPaymentItemPdfHandler")
    private IPostPDFHandler additionalPaymentItemPdfHandler;
    @Autowired
    private PayslipTableManager payslipTableManager;
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private NotificationMsgManager notificationMsgManager;
    @Autowired
    private BenefitRequestManager benefitRequestManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ImportFileManager importFileManager;
    @Autowired
    @Qualifier("crmAccountBalancePDFHandler")
    private IPostPDFHandler customerBalancePdfHandler;
    @Autowired
    private ActivationLinkManager activationLinkManager;
    @Autowired
    private SmsManager smsManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private ZoomService zoomService;
    @Autowired
    private SmsSendItemManager smsSendItemManager;
    @Autowired
    private HostBasedSettingManager hostBasedSettingManager;

    public MessageManagerImpl() {
        super(EdsMessage.class);
    }

    public String formatDate(Date date, EdsCompany company) {
        Format formatter;
        if (company.getCompanySettings() != null) {
            formatter = new SimpleDateFormat(company.getCompanySettings().getLongDateFormat());
        } else {
            formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);
        }
        return formatter.format(date);
    }

    public String simpleDateFormat(Date date, EdsCompany company, Boolean isShortDateFormat) {
        if (isShortDateFormat) {
            return ServerUtils.shortDateFormat(date, company, true);
        } else {
            return ServerUtils.longDateFormat(date, company, true);
        }
    }

    public String formatDateShort(Date date, EdsCompany company) {
        Format formatter;
        if (company.getCompanySettings() != null) {
            formatter = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat());
        } else {
            formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        }
        return formatter.format(date);
    }

    public static String defaultShortDateFormat(Date date) {
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }

    public static String defaultLongDateFormat(Date date) {
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);
        return formatter.format(date);
    }

    public static String shortSimpleDateFormat(Date date) {
        Format formatter = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }

    public String mergeAssignees(Set<EdsEmployeeTask> assignees) {

        StringBuilder buf = new StringBuilder();
        Iterator<EdsEmployeeTask> iter = employeeTaskManager.sort(assignees).iterator();
        if (iter.hasNext()) {
            buf.append(iter.next().getProjectEmployee().getEmployeeDepartment().getEmployee().getName());
        }
        while (iter.hasNext()) {
            buf.append(',');
            buf.append(iter.next().getProjectEmployee().getEmployeeDepartment().getEmployee().getName());
        }
        return buf.toString();
    }

    public void reportSubmittedToEmployee(EdsExpenseReport report, Date submitedDate, ByteArrayOutputStream pdfStream) {
        if (report.getReporter() != null && emailNotificationSettingsManager.hasEmailNotification(report.getReporter().getObjectID(), EmailNotificationConstants.EXPENSE_CLAIM_EMAIL)) {
            EdsEmployee approver = getPreferredApprover(report);
            String email = report.getReporter().getEmail();
            String subject = commonLocalizer.localize(EdsSubjects.EXPENSE_REPORT_SUBMITTED_FOR) + " " + approver.getFullName() + "'" + commonLocalizer.localize(EdsSubjects.S_ATTENTION);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Map<String, Object> values = new TreeMap<>();
            values.put("USER", report.getReporter());
            values.put("APPROVER", approver);

            if (report.getReporter() != null) {
                values.put("SUBMIT_DATE", Utils.formatDate(submitedDate, report.getReporter().getCompany()));
            } else {
                values.put("SUBMIT_DATE", defaultShortDateFormat(submitedDate));
            }

            try {
                baos.write(pdfStream.toByteArray());
                String text = EdsTemplates.processTemplate(report.getReporter().getCreator(), values, EdsTemplates.EXPENSE_SUBMITTED_TO_EMPLOYEE);
                if (!report.getReporter().getDeleted()) {
                    send(report.getObjectID(), report.getTitle() + "_" + ServerUtils.shortDateFormat(submitedDate, report.getReporter()) + ".pdf", "application/pdf", email, subject, text, approver.getEmail(), baos);
                }
                baos.flush();
                baos.close();
            } catch (EdsTemplateException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                log.error("Unable to work with PDF Stream.", ex);
            }
        }
    }

    public void reportSubmittedToApprover(EdsExpenseReport report, Date submitedDate, ByteArrayOutputStream pdfStream) {
        EdsEmployee approver = getPreferredApprover(report);
        if (approver != null && emailNotificationSettingsManager.hasEmailNotification(approver.getObjectID(), EmailNotificationConstants.EXPENSE_CLAIM_EMAIL)) {
            String toEmail = approver.getEmail();
            EdsEmployee reporter = report.getReporter();

            String subject = reporter.getFullName() + " " + commonLocalizer.localize(EdsSubjects.SUBMITTED_AN_EXPENSE_CLAIM);
            String link = EncryptionHelper.encryptURL("expenseReports|previewReport/" + report.getObjectID() + "/" + Constants.EXPENSE_EDIT);
            String userUrl = "";
            if (report.getCurrentApprover() != null && report.getCurrentApprover().getExactEmployee() != null) {
                userUrl = EncryptionHelper.encryptURL(report.getCurrentApprover().getExactEmployee().getObjectID().toString());
            }
            EdsCompany company = report.getCurrentApprover().getExactEmployee().getCompany();
            String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());
            Map<String, Object> values = new TreeMap<>();

            values.put("USER", approver);
            values.put("REPORTER", report.getReporter());
            if (approver != null) {
                values.put("SUBMIT_DATE", Utils.formatDate(submitedDate, company));
                values.put("REPORT_START_DATE", Utils.formatDate(report.getStartDate(), company));
            } else {
                values.put("SUBMIT_DATE", defaultShortDateFormat(submitedDate));
                values.put("REPORT_START_DATE", defaultShortDateFormat(report.getStartDate()));
            }
            values.put("REPORT_TITLE", report.getTitle());
            values.put("REPORT_AMOUNT", Utils.formatDouble(report.getBaseTotal().doubleValue()));
            values.put("HOST", EdsContextParams.getHost(company.getObjectID()));
            values.put("LINK", link);
            values.put("companyid", companyid);
            values.put("uid", userUrl);

            try {
                String text = EdsTemplates.processTemplate(report.getReporter().getCreator(), values, EdsTemplates.EXPENSE_SUBMITTED_TO_APPROVER);
                if (approver != null && !approver.getDeleted()) {
                    sendMessageFormUser(toEmail, report.getTitle() + "_" + formatDateShort(submitedDate, company) + ".pdf", subject, text, reporter, pdfStream);
                } else {
                    sendMessageFormUser(toEmail, report.getTitle() + "_" + defaultShortDateFormat(submitedDate) + ".pdf", subject, text, reporter, pdfStream);
                }
            } catch (EdsTemplateException ex) {
                ex.printStackTrace();
            }
        }
    }

    private EdsEmployee getPreferredApprover(EdsExpenseReport report) {
        if (report.getCurrentApprover() != null && report.getCurrentApprover().getExactEmployee() != null && report.getCurrentApprover().getExactEmployee().getEmployee() != null) {
            return report.getCurrentApprover().getExactEmployee().getEmployee();
        } else {
            return null;
        }
    }

    public void reportSubmittedToApprover(EdsExpenseReport report, String text, Date submitedDate, ByteArrayOutputStream pdfStream) {
        EdsEmployee edsEmployee = getPreferredApprover(report);
        if (edsEmployee != null && emailNotificationSettingsManager.hasEmailNotification(edsEmployee.getObjectID(), EmailNotificationConstants.EXPENSE_CLAIM_EMAIL)) {
            String originalName = report.getTitle();
            String toEmail = edsEmployee.getEmail();
            EdsEmployee reporter = report.getReporter();
            String subject = reporter.getFullName() + " " + commonLocalizer.localize(EdsSubjects.SUBMITTED_AN_EXPENSE_CLAIM);
            List<Integer> uploads = getAttachments(report.getObjectID());
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(pdfStream.toByteArray());
                ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
                if (reporter != null) {
                    originalName += "_" + formatDateShort(submitedDate, reporter.getCompany());
                } else {
                    originalName += "_" + defaultShortDateFormat(submitedDate);
                }
                EdsUpload upload = createUpload(inputStream, originalName, "application/pdf");
                uploads.add(upload.getObjectID());
                if (!getPreferredApprover(report).getDeleted()) {
                    sendMessageFromUser(null, toEmail, null, null, subject, text, true, null, uploads, false, null, null, reporter);
                }
                inputStream.close();
                baos.flush();
                baos.close();
            } catch (IOException ex) {
                log.error("Unable to work with PDF Stream.", ex);
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
        }
    }

    public void reportDeclinedToEmployee(EdsExpenseReport report, Date declinedDate) {
        if (report.getReporter() != null && emailNotificationSettingsManager.hasEmailNotification(report.getReporter().getObjectID(), EmailNotificationConstants.EXPENSE_CLAIM_EMAIL)) {
            String toEmail = report.getReporter().getEmail();
            EdsEmployee approver = null;
            if (report.getCurrentApprover() != null && report.getCurrentApprover().getExactEmployee() != null) {
                approver = (EdsEmployee) report.getCurrentApprover().getExactEmployee();
            } else {
                approver = userManager.getUser().getEmployee();
            }
            String subject = approver.getFullName() + " " + commonLocalizer.localize(EdsSubjects.DECLINED_YOUR_EXPENSE_CLAIN);
            String link = EncryptionHelper.encryptURL("expenseReports|edit/" + report.getObjectID());
            String userUrl = EncryptionHelper.encryptURL(report.getReporter().getObjectID().toString());
            EdsCompany company = report.getReporter().getCompany();
            String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());

            Map<String, Object> values = new TreeMap<>();

            values.put("USER", report.getReporter());
            values.put("APPROVER", approver);
            if (report.getReporter() != null) {
                values.put("DECLINE_DATE", Utils.formatDate(declinedDate, company));
            } else {
                values.put("DECLINE_DATE", defaultShortDateFormat(declinedDate));
            }
            values.put("HOST", EdsContextParams.getHost(company.getObjectID()));
            values.put("LINK", link);
            values.put("companyid", companyid);
            values.put("uid", userUrl);
            values.put("rejectNote", report.getRejectionNote());

            try {
                String text = EdsTemplates.processTemplate(report.getReporter().getCreator(), values, EdsTemplates.EXPENSE_DECLINED_TO_EMPLOYEE);
                if (!report.getReporter().getDeleted()) {
                    sendMessageFromUser(null, toEmail, null, null, subject, text, false, approver.getEmail(), null, false, null, null, approver);
                }
            } catch (EdsTemplateException | EdsDbException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void reportApprovedToEmployee(EdsExpenseReport report, Date approvedDate, ByteArrayOutputStream pdfStream) {
        if (report.getReporter() != null && emailNotificationSettingsManager.hasEmailNotification(report.getReporter().getObjectID(), EmailNotificationConstants.EXPENSE_CLAIM_EMAIL)) {
            String toEmail = report.getReporter().getEmail();
            EdsEmployee reporter = report.getReporter();
            String originalName = report.getTitle();
            EdsEmployee approver = null;
            if (report.getCurrentApprover() != null && report.getCurrentApprover().getExactEmployee() != null) {
                approver = report.getCurrentApprover().getExactEmployee().getEmployee();
            }
            String subject = approver.getFullName() + " " + commonLocalizer.localize(EdsSubjects.APPROVED_YOUR_EXPENSE_CLAIN);
            String link = EncryptionHelper.encryptURL("expenseReports|previewReport/" + report.getObjectID() + "/" + Constants.EXPENSE_VIEW);
            String userUrl = EncryptionHelper.encryptURL(reporter.getObjectID().toString());
            EdsCompany company = reporter.getCompany();
            String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());
            Map<String, Object> values = new TreeMap<>();

            values.put("USER", report.getReporter());
            values.put("APPROVER", approver);
            values.put("REPORT_TITLE", report.getTitle());
            if (report.getReporter() != null) {
                values.put("APPROVE_DATE", Utils.formatDate(approvedDate, report.getReporter().getCompany()));
            } else {
                values.put("APPROVE_DATE", defaultShortDateFormat(approvedDate));
            }
            values.put("HOST", EdsContextParams.getHost(company.getObjectID()));
            values.put("LINK", link);
            values.put("companyid", companyid);
            values.put("uid", userUrl);

            List<Integer> uploads = getAttachments(report.getObjectID());
            try {
                String text = EdsTemplates.processTemplate(reporter.getCreator(), values, EdsTemplates.EXPENSE_APPROVED_TO_EMPLOYEE);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write(pdfStream.toByteArray());
                ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
                if (reporter != null) {
                    originalName += "_" + formatDateShort(approvedDate, reporter.getCompany());
                } else {
                    originalName += "_" + defaultShortDateFormat(approvedDate);
                }
                EdsUpload upload = createUpload(inputStream, originalName + ".pdf", "application/pdf");
                uploads.add(upload.getObjectID());
                if (!report.getReporter().getDeleted()) {
                    sendMessageFromUser(null, toEmail, null, null, subject, text, true, null, uploads, false, null, null, reporter);
                }
                inputStream.close();
                baos.flush();
                baos.close();
            } catch (EdsTemplateException | EdsDbException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                log.error("Unable to work with PDF Stream.", ex);
            }
        }
    }

    public void reportResubmittedToApprover(EdsExpenseReport report, Date resubmittedDate, Date declinedDate, ByteArrayOutputStream pdfStream) {
        EdsEmployee approver = getPreferredApprover(report);
        if (approver != null && emailNotificationSettingsManager.hasEmailNotification(approver.getObjectID(), EmailNotificationConstants.EXPENSE_CLAIM_EMAIL)) {
            String toEmail = approver.getEmail();
            EdsEmployee reporter = report.getReporter();
            String subject = reporter.getFullName() + " " + commonLocalizer.localize(EdsSubjects.RESUBMITTED_THE_EXPENSE_CLAIN);
            String link = EncryptionHelper.encryptURL("expenseReports|previewReport/" + report.getObjectID() + "/" + Constants.EXPENSE_EDIT);
            String userUrl = EncryptionHelper.encryptURL(approver.getObjectID().toString());
            EdsCompany company = approver.getCompany();
            String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());
            Map<String, Object> values = new TreeMap<>();
            values.put("USER", approver);
            values.put("REPORTER", report.getReporter());
            if (report.getCurrentApprover() != null && report.getCurrentApprover().getExactEmployee() != null) {
                values.put("RESUBMIT_DATE", Utils.formatDate(resubmittedDate, company));
                values.put("DECLINE_DATE", Utils.formatDate(declinedDate, company));
                values.put("REPORT_START_DATE", Utils.formatDate(report.getStartDate(), company));
            } else {
                values.put("RESUBMIT_DATE", defaultShortDateFormat(resubmittedDate));
                values.put("DECLINE_DATE", defaultShortDateFormat(declinedDate));
                values.put("REPORT_START_DATE", defaultShortDateFormat(report.getStartDate()));
            }

            values.put("REPORT_TITLE", report.getTitle());
            values.put("REPORT_AMOUNT", Utils.formatDouble(report.getBaseTotal().doubleValue()));
            values.put("HOST", EdsContextParams.getHost(company.getObjectID()));
            values.put("LINK", link);
            values.put("companyid", companyid);
            values.put("uid", userUrl);

            try {
                String text = EdsTemplates.processTemplate(report.getReporter().getCreator(), values, EdsTemplates.EXPENSE_RESUBMITTED_TO_APPROVER);
                if (reporter != null && !approver.getDeleted()) {
                    sendMessageFormUser(toEmail, report.getTitle() + "_" + formatDateShort(resubmittedDate, reporter.getCompany()) + ".pdf", subject, text, reporter, pdfStream);
                } else {
                    sendMessageFormUser(toEmail, report.getTitle() + "_" + defaultShortDateFormat(resubmittedDate) + ".pdf", subject, text, reporter, pdfStream);
                }
            } catch (EdsTemplateException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void reportResubmittedToApprover(EdsExpenseReport report, String text, Date resubmittedDate, ByteArrayOutputStream pdfStream) {
        EdsEmployee edsEmployee = getPreferredApprover(report);
        if (edsEmployee != null && emailNotificationSettingsManager.hasEmailNotification(edsEmployee.getObjectID(), EmailNotificationConstants.EXPENSE_CLAIM_EMAIL)) {
            String toEmail = edsEmployee.getEmail();
            EdsEmployee reporter = report.getReporter();
            String subject = reporter.getFullName() + " " + commonLocalizer.localize(EdsSubjects.RESUBMITTED_THE_EXPENSE_CLAIN);
            if (reporter != null && !getPreferredApprover(report).getDeleted()) {
                sendMessageFormUser(toEmail, report.getTitle() + "_" + formatDateShort(resubmittedDate, reporter.getCompany()) + ".pdf", subject, text, reporter, pdfStream);
            } else {
                sendMessageFormUser(toEmail, report.getTitle() + "_" + defaultShortDateFormat(resubmittedDate) + ".pdf", subject, text, reporter, pdfStream);
            }
        }
    }

    public void sendPayPalNotification(String mes, String sub) {
        sendPayPalNotification(mes, sub, null);
    }

    public void sendPayPalNotification(String mes, String sub, String to) {
        if (to == null || to.isEmpty()) {
            to = defaultSupportEmail + ',' + EdsContextParams.getSupportEmail() + ", anvar@kpi.com";
        }

        String subject = commonLocalizer.localize(EdsSubjects.PAYPAL_SERVICE_NOTIFICATION) + " - " + sub;
        try {
            sendMessage(to, subject, mes, null, false, null, null, null);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void sendWorldPayNotification(String mes, String sub) {

        String subject = "WorldPay notification - " + sub;
        try {
            sendMessage(defaultSupportEmail + ',' + EdsContextParams.getSupportEmail(), subject, mes, null, false, null, null, null);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void sendGoogleCheckoutNotification(String mes, String sub) {
        String to = defaultSupportEmail + ',' + EdsContextParams.getSupportEmail();
        String subject = commonLocalizer.localize(EdsSubjects.GOOGLE_CHECKOUT_NOTIFICATION) + " - " + sub;
        try {
            sendMessage(to, subject, mes, null, false, null, null, null);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public int differenceOfTwoDates(Date startDate, Date endDate) {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(startDate);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(endDate);
        long difference = Math.abs(cal2.getTime().getTime() - cal1.getTime().getTime());
        long days = difference / (1000 * 60 * 60 * 24);
        return (int) days;
    }

    public void sendAssessmentInitiateNotification(EdsEmployeeAssessment employeeAssessment, ByteArrayOutputStream stream, boolean sendEmailToEmployee) throws EdsDbException {
        try {
            String to = employeeAssessment.getEmployee().getEmail();
            String toReviewer = employeeAssessment.getAssessment().getReviewer().getEmail();
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_INITIATE_NOTIFICATION);
            Map<String, Object> values = new TreeMap<>();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
            }

            String textReviewer = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.INITIATE_ASSESSMENT_FOR_INITIATOR);
            String originalName = employeeAssessment.getEmployee().getFirstName() + "_" + employeeAssessment.getEmployee().getLastName() + "_" + "Initiated_By_" + employeeAssessment.getAssessment().getReviewer().getFirstName() + "_" + employeeAssessment.getAssessment().getReviewer().getLastName() + "_" + employeeAssessment.getAssessment().getInititateDate() + ".pdf";
            String initiator = employeeAssessment.getAssessment().getInitiator().getEmail();

            // send email to employee
            if (sendEmailToEmployee) {
                values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getEmployee().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
                String text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.INITIATE_ASSESSMENT);
                if (stream == null && !employeeAssessment.getEmployee().getDeleted()) {
                    sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, employeeAssessment.getAssessment().getReviewer());
                } else {
                    sendMessageFromUser(originalName, to, subject, text, employeeAssessment.getAssessment().getReviewer(), stream);
                }
            }
            if (stream == null) {
                sendMessage(toReviewer, subject, textReviewer, null, false, null, null, null);
            } else {
                send(originalName, "application/pdf", toReviewer, subject, textReviewer, stream);
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        } finally {
            try {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendDeleteAssessmentNotification(EdsEmployeeAssessment employeeAssessment) {
        try {
            Map<String, Object> values = new TreeMap<>();
            values.put("employeeAssessment", employeeAssessment);
            String dateAsString = employeeAssessment.getAssessment().getInititateDate() != null ?
                    formatDateShort(employeeAssessment.getAssessment().getInititateDate(), employeeAssessment.getEmployee().getCompany()) : " ";
            values.put("date", dateAsString);
            String text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.DELETE_ASSESSMENT);
            if (!employeeAssessment.getEmployee().getDeleted()) {
                sendMessage(employeeAssessment.getEmployee().getEmail(), commonLocalizer.localize(EdsSubjects.ASSESSMENT_DELETE_NOTIFICATION), text, null, false, null, null, null);
            }
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void send360ReviewReminederNotification(EdsEmployeeAssessment employeeAssessment, String messageContent) throws EdsDbException {
        try {
            EdsEmployee employee = employeeAssessment.getEmployee();
            EdsCompany company = employee.getCompany();
            String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());
            Map<String, Object> values = new TreeMap<>();
            values.put("message", messageContent);
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString()) + "&" + C_ID + "=" + companyid);

            String to = employeeAssessment.getEmployee().getEmail();
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_REVIEW_NOTIFICATION);
            String text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.INITIATE_360_REMINDER);
            if (!employeeAssessment.getEmployee().getDeleted()) {
                sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, employeeAssessment.getAssessment().getReviewer());
            }

        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendAssessment360InitiateNotification(EdsEmployeeAssessment employeeAssessment, List<EdsEmployeeAssessment> collaboratorsList) throws EdsDbException {
        try {
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());

            Map<String, Object> values = new TreeMap<>();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
            }

            List<CollaboratorEmployee> collaborators = new ArrayList<>();
            for (EdsEmployeeAssessment emplassessment : collaboratorsList) {
                if (emplassessment.getCollaborator() != null) {
                    if (emplassessment.isClient()) {
                        collaborators.add(new CollaboratorEmployee(emplassessment.getCollaborator().getName(), CLIENT));
                    } else if (emplassessment.isPeer()) {
                        collaborators.add(new CollaboratorEmployee(emplassessment.getCollaborator().getName(), PEER));
                    } else if (emplassessment.isManager()) {
                        collaborators.add(new CollaboratorEmployee(emplassessment.getCollaborator().getName(), MANAGER));
                    }
                }
            }
            values.put("collaborators", collaborators);

            values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getEmployee().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
            String textEmployee = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.INITIATE_360_ASSESSMENT);

            values.put("link", AssessmentUtils.getReviewLinkForMailForManager(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getReviewer().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
            String textManager = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.INITIATE_360_ASSESSMENT_FOR_REVIEWER);

            String to = employeeAssessment.getEmployee().getEmail();
            String toReviewer = employeeAssessment.getAssessment().getReviewer().getEmail();
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_INITIATE_NOTIFICATION_360);
            if (!employeeAssessment.getEmployee().getDeleted()) {
                sendMessageFromUser(null, to, null, null, subject, textEmployee, false, null, null, false, null, null, employeeAssessment.getAssessment().getInitiator());
            }
            if (!employeeAssessment.getAssessment().getReviewer().getDeleted()) {
                sendMessage(toReviewer, subject, textManager, null, false, null, null, null);
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendAssessmentReviewNotification(EdsEmployeeAssessment employeeAssessment, ByteArrayOutputStream stream) throws EdsDbException {
        try {
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_REVIEW_NOTIFICATION);
            String to;
            String toEmployee;
            Map<String, Object> values = new TreeMap<>();
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
            }
            String text;
            String textEmployee;
            String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());
            values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getReviewer().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
            text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.REVIEW_ASSESSMENT);
            values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getEmployee().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
            textEmployee = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.REVIEW_ASSESSMENT_FOR_EMPLOYEE);
            to = employeeAssessment.getAssessment().getReviewer().getEmail();
            toEmployee = employeeAssessment.getEmployee().getEmail();
            EdsUser from = employeeAssessment.getEmployee();

            if (stream == null) {
                sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, from);
                sendMessage(toEmployee, subject, textEmployee, null, false, null, null, null);
            } else {
                String originalName = employeeAssessment.getEmployee().getFirstName() + "_" + employeeAssessment.getEmployee().getLastName() + "_" + "Initiated_By_" + employeeAssessment.getAssessment().getReviewer().getFirstName() + "_" + employeeAssessment.getAssessment().getReviewer().getLastName() + "_" + employeeAssessment.getAssessment().getInititateDate() + ".pdf";
                try {
                    sendMessageFromUser(originalName, to, subject, text, from, stream);
                    send(originalName, "application/pdf", toEmployee, subject, textEmployee, stream);
                } finally {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendAssessment360ReviewNotificationForCollaborator(EdsEmployeeAssessment employeeAssessment) throws EdsDbException {
        try {
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_REVIEW_NOTIFICATION);
            String to;
            Map<String, Object> values = new TreeMap<>();
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            values.put("companyName", company.getName());
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
            }
            String text = null;
            if (employeeAssessment.getAssessment().is360() && employeeAssessment.getCollaborator() != null) {//in case the reviewer is initiator(TL)
                String companyid = EncryptionHelper.encryptURL(employeeAssessment.getCollaborator().getCompany().getObjectID().toString());
                values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getCollaborator().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
                if (employeeAssessment.isPeer()) {
                    text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.PEER_360_REVIEW);
                } else if (employeeAssessment.isManager()) {
                    text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.MANAGER_360_REVIEW);
                } else if (employeeAssessment.isClient()) {
                    text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.CLIENT_360_REVIEW);
                }
                to = employeeAssessment.getCollaborator().getEmail();

                sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, employeeAssessment.getAssessment().getReviewer());
            } else {
                String companyid = EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getReviewer().getCompany().getObjectID().toString());
                values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getReviewer().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
                text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.REVIEW_ASSESSMENT);
                to = employeeAssessment.getAssessment().getReviewer().getEmail();

                sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, employeeAssessment.getEmployee());
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendAssessment360ReviewNotification(EdsEmployeeAssessment employeeAssessment) throws EdsDbException {
        try {
            EdsUser from;
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_REVIEW_NOTIFICATION_360);
            String to;
            Map<String, Object> values = new TreeMap<>();
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
            }

            String text = null;

            if (employeeAssessment.getAssessment().is360() && employeeAssessment.getCollaborator() != null) {//in case the reviewer is initiator(TL)
                List<CollaboratorEmployee> collaborators = new ArrayList<>();
                for (EdsEmployeeAssessment emplassessment : employeeAssessment.getAssessment().getEmployeeAssessments()) {
                    if (!emplassessment.getObjectID().equals(employeeAssessment.getObjectID())) {
                        if (emplassessment.getStatus() != null && emplassessment.getStatus().getCode().equals(INITIATED)) {
                            if (emplassessment.getCollaborator() != null) {
                                if (emplassessment.isClient()) {
                                    collaborators.add(new CollaboratorEmployee(emplassessment.getCollaborator().getName(), CLIENT));
                                } else if (emplassessment.isPeer()) {
                                    collaborators.add(new CollaboratorEmployee(emplassessment.getCollaborator().getName(), PEER));
                                } else if (emplassessment.isManager()) {
                                    collaborators.add(new CollaboratorEmployee(emplassessment.getCollaborator().getName(), MANAGER));
                                }
                            }
                        }
                    }
                }
                CollaboratorEmployee collaborator;
                if (employeeAssessment.isPeer()) {
                    collaborator = new CollaboratorEmployee(employeeAssessment.getCollaborator().getName(), PEER);
                } else if (employeeAssessment.isClient()) {
                    collaborator = new CollaboratorEmployee(employeeAssessment.getCollaborator().getName(), CLIENT);
                } else {
                    collaborator = new CollaboratorEmployee(employeeAssessment.getCollaborator().getName(), MANAGER);
                }
                String companyid = EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getReviewer().getCompany().getObjectID().toString());
                values.put("collaborator", collaborator);
                values.put("collaborators", collaborators);
                values.put("link", AssessmentUtils.getReviewLinkForMailForManager(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getReviewer().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
                text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.COLLABORATOR_360_REVIEW);
                to = employeeAssessment.getAssessment().getReviewer().getEmail();
                from = employeeAssessment.getCollaborator();
            } else {
                String companyid = EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getReviewer().getCompany().getObjectID().toString());
                values.put("link", AssessmentUtils.getReviewLinkForMailForManager(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getReviewer().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
                text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.EMPLOYEE_360_REVIEW);
                to = employeeAssessment.getAssessment().getReviewer().getEmail();
                from = employeeAssessment.getEmployee();

            }
            sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, from);

        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }

    }


    public void sendOneOffAssessment360ReviewNotification(EdsUser oneoffUser, EdsEmployeeAssessment employeeAssessment, ByteArrayOutputStream stream) throws EdsDbException {
        try {
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_REVIEW_NOTIFICATION);
            String to;
            String toEmployee;
            Map<String, Object> values = new TreeMap<>();
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(employeeAssessment.getEmployee().getCompany().getCompanyDate(), employeeAssessment.getEmployee().getCompany()));
            } else {
                values.put("date", defaultShortDateFormat(employeeAssessment.getEmployee().getCompany().getCompanyDate()));
            }
            String text = null;
            String textEmployee = "";
            text = EdsTemplates.processTemplate(oneoffUser, values, EdsTemplates.ONE_OFF_360_COLLABORATOR_REVIEW);
            to = employeeAssessment.getCollaborator().getEmail();
            EdsUser from = employeeAssessment.getAssessment().getReviewer();
            if (stream == null) {
                sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, from);
            } else {
                String originalName = employeeAssessment.getEmployee().getFirstName() + "_" + employeeAssessment.getEmployee().getLastName() + "_" + "Initiated_By_" + employeeAssessment.getAssessment().getReviewer().getFirstName() + "_" + employeeAssessment.getAssessment().getReviewer().getLastName() + "_" + employeeAssessment.getAssessment().getInititateDate() + ".pdf";
                try {
                    sendMessageFromUser(originalName, to, subject, text, from, stream);
                } finally {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }

    }

    public void sendOneOffPeerAssessmentReviewNotification(EdsUser oneoffUser, EdsEmployeeAssessment employeeAssessment) throws EdsDbException {
        try {
            Map<String, Object> values = new TreeMap<>();
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
            }
            values.put("link", AssessmentUtils.getReviewLinkForMailForOneOffUser(oneoffUser, employeeAssessment));

            String text = EdsTemplates.processTemplate(oneoffUser, values, EdsTemplates.ONE_OFF_360_PEER_REVIEW);
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_REVIEW_NOTIFICATION);
            String to = employeeAssessment.getCollaborator().getEmail();

            sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, employeeAssessment.getAssessment().getReviewer());
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendOneOffClientAssessmentReviewNotification(EdsUser oneoffUser, EdsEmployeeAssessment employeeAssessment) throws EdsDbException {
        try {
            Map<String, Object> values = new TreeMap<>();
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
            }
            values.put("company", company);
            values.put("link", AssessmentUtils.getReviewLinkForMailForOneOffUser(oneoffUser, employeeAssessment));

            String text = EdsTemplates.processTemplate(oneoffUser, values, EdsTemplates.ONE_OFF_360_CLIENT_REVIEW);
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_REVIEW_NOTIFICATION);
            String to = employeeAssessment.getCollaborator().getEmail();

            sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, employeeAssessment.getAssessment().getReviewer());
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendOneOffManagerAssessmentReviewNotification(EdsUser oneoffUser, EdsEmployeeAssessment employeeAssessment) throws EdsDbException {
        try {
            Map<String, Object> values = new TreeMap<>();
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
            }
            values.put("link", AssessmentUtils.getReviewLinkForMailForOneOffUser(oneoffUser, employeeAssessment));

            String text = EdsTemplates.processTemplate(oneoffUser, values, EdsTemplates.ONE_OFF_360_MANAGER_REVIEW);
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_REVIEW_NOTIFICATION);
            String to = employeeAssessment.getCollaborator().getEmail();

            sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, employeeAssessment.getAssessment().getReviewer());
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    /**
     * Method used to send messages with attachment.
     *
     * @param originalName desired file name.
     * @param contentType  type of your attachment file. ("application/pdf" for example)
     * @param to
     * @param subject
     * @param text
     * @param baos         Attachment in the form of ByteArrayOutputStream
     */
    private void send(String originalName, String contentType, String to, String subject, String text, ByteArrayOutputStream baos) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            EdsUpload upload = new EdsUpload();
            upload.setContentType(contentType);
            upload.setOriginalName(originalName);
            upload.setType(referenceManager.findReference(Constants._UPLOAD_TYPE, EdsContextParams.getUploadType()));
            upload.setInputStream(inputStream);

            uploadManager.create(upload);
            sendMessageFromUser(originalName, to, subject, text, null, baos);
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void sendMessageFromUser(String originalName, String fromEmail, String to, String subject, String text, EdsUser from, String fromUserName, ByteArrayOutputStream baos) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            EdsUpload upload = createUpload(inputStream, originalName, "application/pdf");
            ArrayList<Integer> fileIds = new ArrayList<>();
            fileIds.add(upload.getObjectID());
            sendMessageForPayrun(fromUserName, fromEmail, to, subject, text, fileIds, from.getCompany().getObjectID());
            inputStream.close();
        } catch (EdsDbException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessageFromUser(String originalName, String to, String subject, String text, EdsUser from, ByteArrayOutputStream baos) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            EdsUpload upload = createUpload(inputStream, originalName, "application/pdf");
            ArrayList<Integer> fileIds = new ArrayList<>();
            fileIds.add(upload.getObjectID());
            sendMessageFromUser(null, to, null, null, subject, text, true, null, fileIds, false, null, null, from);
            inputStream.close();
        } catch (EdsDbException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private void send(Integer reportID, String originalName, String contentType, String to, String subject, String text, String replyTo, ByteArrayOutputStream baos) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            EdsUpload upload = createUpload(inputStream, originalName, contentType);
            List<Integer> uploads = getAttachments(reportID);
            uploads.add(upload.getObjectID());
            sendMessageFromUser(null, to, null, null, subject, text, true, replyTo, uploads, false, null, null, null);
            inputStream.close();
        } catch (EdsDbException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private List<Integer> getAttachments(Integer reportID) {
        List<Integer> result = new ArrayList<>();
        List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_EXP_DOC, reportID, reportID);
        FileItem[] fileItems = new FileItem[attachments.size()];
        for (FileResource fileResource : attachments) {
            result.add(fileResource.getBodyId());
        }
        return result;
    }

    private EdsUpload createUpload(InputStream inputStream, String originalName, String contentType) {
        EdsUpload upload = new EdsUpload();
        upload.setContentType(contentType);
        upload.setOriginalName(originalName);
        upload.setType(referenceManager.findReference(Constants._UPLOAD_TYPE, EdsContextParams.getUploadType()));
        upload.setInputStream(inputStream);
        uploadManager.create(upload);
        return upload;
    }

    public void sendAssessmentRateNotification(EdsEmployeeAssessment employeeAssessment, Integer loggedUserId, ByteArrayOutputStream stream) throws EdsDbException {
        try {
            String to = employeeAssessment.getEmployee().getEmail();
            EdsUser reviewer = employeeAssessment.getAssessment().getReviewer();

            String toReviewer = reviewer.getEmail();
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_RATE_NOTIFICATION);
            Map<String, Object> values = new TreeMap<>();
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
                values.put("initDate", formatDateShort(employeeAssessment.getAssessment().getInititateDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
                values.put("initDate", defaultShortDateFormat(employeeAssessment.getAssessment().getInititateDate()));
            }
            String text = null;
            String textReviewer = "";
            if (employeeAssessment.getAssessment().is360()) {
                if (employeeAssessment.getCollaborator() != null) {
                    if (employeeAssessment.getCollaborator() instanceof EdsUserContact) {
                        text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.ONE_OFF_360_COLLABORATOR_REVIEW);
                    } else {
                        if (employeeAssessment.isPeer()) {
                            values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getInitiator().getObjectID().toString()));
                            text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.PEER_360_RATE);
                        } else if (employeeAssessment.isManager()) {
                            values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getInitiator().getEmployee().getObjectID().toString()));
                            text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.MANAGER_360_RATE);
                        } else if (employeeAssessment.isClient()) {
                            values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getInitiator().getObjectID().toString()));
                            text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.CLIENT_360_RATE);
                        }
                    }
                } else {
                    if (loggedUserId.equals(employeeAssessment.getEmployee().getObjectID())) {
                        values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getInitiator().getObjectID().toString()));
                        text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.EMPLOYEE_RESPONSE_360);
                    } else {
                        values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getReviewer().getObjectID().toString()));
                        text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.EMPLOYEE_360_REVIEW);
                    }
                }
            } else {
                values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getEmployee().getObjectID().toString()));
                text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.RATE_ASSESSMENT);
                values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getReviewer().getObjectID().toString()));
                textReviewer = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.RATE_ASSESSMENT_FOR_INITIATOR);
            }
            if (stream == null) {
                sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, employeeAssessment.getAssessment().getInitiator());
                if (!employeeAssessment.getAssessment().is360()) {
                    sendMessage(toReviewer, subject, textReviewer, null, false, null, null, null);
                }
            } else {
                String originalName = employeeAssessment.getEmployee().getFirstName() + "_" + employeeAssessment.getEmployee().getLastName() + "_" + "Initiated_By_" + employeeAssessment.getAssessment().getReviewer().getFirstName() + "_" + employeeAssessment.getAssessment().getReviewer().getLastName() + "_" + employeeAssessment.getAssessment().getInititateDate() + ".pdf";

                try {
                    sendMessageFromUser(originalName, to, subject, text, reviewer, stream);
                    if (!employeeAssessment.getAssessment().is360()) {
                        send(originalName, "application/pdf", toReviewer, subject, textReviewer, stream);
                    }
                } finally {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendAssessmentApproveNotification(EdsEmployeeAssessment employeeAssessment, ByteArrayOutputStream stream) throws EdsDbException {
        try {
            String to = employeeAssessment.getEmployee().getEmail();
            EdsUser from = employeeAssessment.getAssessment().getReviewer();

            String toReviewer = employeeAssessment.getAssessment().getReviewer().getEmail();
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_APPROVE_NOTIFICATION);
            Map<String, Object> values = new TreeMap<>();
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
                values.put("initDate", formatDateShort(employeeAssessment.getAssessment().getInititateDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
                values.put("initDate", defaultShortDateFormat(employeeAssessment.getAssessment().getInititateDate()));
            }
            values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getEmployee().getObjectID().toString()));
            String text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.APPROVE_ASSESSMENT);
            values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getInitiator().getObjectID().toString()));
            String textReviewer = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.APPROVE_ASSESSMENT_FOR_INITIATOR);
            if (stream == null) {
                sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, from);
                sendMessage(toReviewer, subject, textReviewer, null, false, null, null, null);
            } else {
                String originalName = employeeAssessment.getEmployee().getFirstName() + "_" + employeeAssessment.getEmployee().getLastName() + "_" + "Initiated_By_" + employeeAssessment.getAssessment().getReviewer().getFirstName() + "_" + employeeAssessment.getAssessment().getReviewer().getLastName() + "_" + employeeAssessment.getAssessment().getInititateDate() + ".pdf";

                try {
                    sendMessageFromUser(originalName, to, subject, text, from, stream);
                    send(originalName, "application/pdf", toReviewer, subject, textReviewer, stream);
                } finally {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendAssessment360ApproveNotification(EdsEmployeeAssessment employeeAssessment, ByteArrayOutputStream stream) throws EdsDbException {
        try {
            String to = employeeAssessment.getEmployee().getEmail();
            EdsUser from = employeeAssessment.getAssessment().getReviewer();

            String toReviewer = from.getEmail();
            String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_APPROVE_NOTIFICATION);
            Map<String, Object> values = new TreeMap<>();
            EdsCompany company = employeeAssessment.getEmployee().getCompany();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("employeeAssessment", employeeAssessment);
            if (employeeAssessment.getEmployee() != null) {
                values.put("date", formatDateShort(company.getCompanyDate(), company));
                values.put("initDate", formatDateShort(employeeAssessment.getAssessment().getInititateDate(), company));
            } else {
                values.put("date", defaultShortDateFormat(company.getCompanyDate()));
                values.put("initDate", defaultShortDateFormat(employeeAssessment.getAssessment().getInititateDate()));
            }
            values.put("link", AssessmentUtils.getReviewLinkForMailForEmployee(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getEmployee().getObjectID().toString()));
            String text = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.APPROVE_ASSESSMENT360_FOR_EMPLOYEE);
//            values.remove("link");
            values.put("link", AssessmentUtils.getReviewLinkForMailForManager(employeeAssessment) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeAssessment.getAssessment().getInitiator().getObjectID().toString()));
            String textReviewer = EdsTemplates.processTemplate(employeeAssessment.getCollaborator(), values, EdsTemplates.APPROVE_ASSESSMENT360_FOR_INITIATOR);
            if (stream == null) {
                sendMessageFromUser(null, to, null, null, subject, text, false, null, null, false, null, null, from);
                sendMessage(toReviewer, subject, textReviewer, null, false, null, null, null);
            } else {
                String originalName = employeeAssessment.getEmployee().getFirstName() + "_" + employeeAssessment.getEmployee().getLastName() + "_" + "Initiated_By_" + employeeAssessment.getAssessment().getReviewer().getFirstName() + "_" + employeeAssessment.getAssessment().getReviewer().getLastName() + "_" + employeeAssessment.getAssessment().getInititateDate() + ".pdf";

                try {
                    sendMessageFromUser(originalName, to, subject, text, from, stream);
                    send(originalName, "application/pdf", toReviewer, subject, textReviewer, stream);
                } finally {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendParamsForGMPLogin(StringBuffer stringBuffer, String param) throws EdsDbException {
        try {
            String subject = commonLocalizer.localize(EdsSubjects.GOOGLE_APPS) + " " + param;
            String text = "Following user tried to " + param + " to system: <br/>" +
                    "List of details: <br/>" +
                    stringBuffer + "\n" +
                    "Method: handleUserInfo(UserInfo userInfo, HttpServletRequest request, HttpServletResponse response)" + "<br/>" +
                    "Class: GoogleMarketplaceLoginController.java";
            ServerSecurityContext.getInstance().setCompanyId("0");
            sendMessage(defaultSupportEmail, subject, text, null, false, null, null, null);
        } catch (Exception ex) {
            throw new EdsDbException(ex);
        }
    }

    @Override
    public void sendCompanyRegistrationNotificationOnlySupport(EdsUser administrator, EdsCompany company, String remoteAddr, String subjectTitle) throws EdsDbException {
        try {
            String subject = EdsContextParams.getProductName() + commonLocalizer.localize(EdsSubjects.SIGN_UP) + ": " + subjectTitle;
            Map<String, Object> adminValues = new TreeMap<>();
            String dateFormat = "dd.MM.yyyy";
            SimpleDateFormat df;
            df = new SimpleDateFormat(dateFormat);

            String id = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(administrator.getObjectID().toString()));

            adminValues.put("HOST", EdsContextParams.getHost(company.getObjectID()));
            adminValues.put("productName", EdsContextParams.getProductName());
            adminValues.put("user", administrator);
            adminValues.put("uid", id);
            adminValues.put("TIME", df.format(new Date()));
            adminValues.put("remoteAddr", remoteAddr);
            adminValues.put("companyName", company.getName());
            String dirText = EdsTemplates.processTemplate(administrator, adminValues, EdsTemplates.NOTIFICATION);
            sendMessage(defaultSupportEmail, subject, dirText, null, false, null, null, company.getObjectID());
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendCompanyRegistrationNotification(EdsUser administrator, EdsCompany company, String remoteAddr, boolean sendRegistrationNotification, boolean isIncludeActivationLink) throws EdsDbException {
//        sendCompanyRegistrationNotificationByOtherSupport(administrator, company, remoteAddr, false, null, sendRegistrationNotification, isIncludeActivationLink);
    }


    public void sendPasswordChangedNotification(EdsUser user, boolean isResetPassword) throws EdsDbException {
        try {

            String supportEmail = EdsContextParams.getSupportEmail();
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generateUserAccountConfirmationEmailTemplate(user);
            if (templateItem != null && !user.getDeleted()) {
                sendMessage(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), supportEmail, false, null, null, null);
            } else {
                String userEmail = user.getEmail();
                String subject = "";
                if (isResetPassword) {
                    subject = user.getCompany().getObjectID() + ": " + commonLocalizer.localize(PASSWORD_RESET_NOTIFICATION);
                } else {
                    subject = user.getCompany().getObjectID() + ": " + commonLocalizer.localize(EMPLOYEE_ADD_NOTIFICATION);
                }
                Map<String, Object> requestValues = new TreeMap<>();
                EdsCompany company = user.getCompany();
                requestValues.put("userName", user.getName());
                requestValues.put("userEmail", user.getEmail());
                requestValues.put("company", company.getName());
                requestValues.put("host", EdsContextParams.getHost(company.getObjectID()));
                requestValues.put("logo", EdsContextParams.getLogoWithHost(company.getObjectID()));
                requestValues.put("productName", EdsContextParams.getProductName());
                requestValues.put("companyId", company.getObjectID());
                requestValues.put("currentYear", LocalDateTime.now().getYear());

                String QAEmail = "munir@kpi.com";
                String requestText = EdsTemplates.processTemplate(user, requestValues, EdsTemplates.PASSWORD_CHANGED);
                sendMessageWithBCC(userEmail, QAEmail, subject, requestText, supportEmail, false, null, null, company.getObjectID());
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendEmployeeAddNotificationForExistingUserName(EdsEmployee employee, EdsUser admin) throws EdsDbException {
        try {
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmployeeActivationExistingUserEmailTemplate(employee, admin);
            if (templateItem != null) {
                registerInternalMessage(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), admin.getEmail(), false, null, false, templateItem.getSubject(), employee.getCompany().getObjectID());
            } else {
                String receiverEmail = employee.getEmail();
                String subject = commonLocalizer.localize(EMPLOYEE_ADD_NOTIFICATION);
                Map<String, Object> requestValues = new TreeMap<>();
                EdsCompany company = employee.getCompany();
                String dateFormat = "dd.MM.yyyy";
                SimpleDateFormat df;
                df = new SimpleDateFormat(dateFormat);
                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(company.getCountryZone().getZone().getZoneID()));

                requestValues.put("host", EdsContextParams.getHost(company.getObjectID()));
                requestValues.put("logo", EdsContextParams.getLogoWithHost(company.getObjectID()));
                if (company != null) {
                    requestValues.put("TIME", formatDateShort(calendar.getTime(), company));
                } else {
                    requestValues.put("TIME", defaultShortDateFormat(calendar.getTime()));
                }
                requestValues.put("admin", admin);
                requestValues.put("user", employee);
                requestValues.put("currentYear", LocalDateTime.now().getYear());

                String displaySubject = admin.getFullName() + " " + commonLocalizer.localize(CREATED_AN_ACCOUNT_FOR_YOU_AT) + " " + EdsContextParams.getProductName();
                String requestText = EdsTemplates.processTemplate(admin, requestValues, EdsTemplates.EMPLOYEE_ADD_FOR_EXISTING_USERNAME);

                registerInternalMessage(receiverEmail, subject, requestText, admin.getEmail(), false, null, false, displaySubject, company.getObjectID());
            }
        } catch (EdsTemplateException ex) {
            throw new EdsDbException(ex);

        }
    }
    // employee add - activation email sending
    public void sendEmployeeAddNotification(EdsEmployee employee, EdsUser admin) throws EdsDbException {
        try {
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmployeeActivationNewUserEmailTemplate(employee, admin);
            if (templateItem != null) {
                registerInternalMessage(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), admin.getEmail(), false, null, false, templateItem.getSubject(), employee.getCompany().getObjectID(), false, true);
            } else {
                String receiverEmail = employee.getEmail();
                Map<String, Object> requestValues = new TreeMap<>();
                EdsCompany company = employee.getCompany();
                String subject = "id: " + company.getObjectID() + ": " + admin.getName() + " " + commonLocalizer.localize(USER_SENT_INVITATION);
                String dateFormat = "dd.MM.yyyy";
                SimpleDateFormat df;
                df = new SimpleDateFormat(dateFormat);
                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(company.getCountryZone().getZone().getZoneID()));

                String id = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(employee.getObjectID().toString()));
                String companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(company.getObjectID().toString()));

                List<UserCompanyDTO> companyList = globalAuthJdbcSpringManager.getUserCompanyByEmail(null, employee.getUserName());
                StringBuilder companyIds = new StringBuilder();
                for (int in = 0; in < companyList.size(); in++) {
                    companyIds.append(companyList.get(in).getCompanyID());
                    if (in < companyList.size() - 1) {
                        companyIds.append(",");
                    }
                }

                requestValues.put("advancedPassEnabled", globalAuthJdbcSpringManager.isEnabledAdvancedPassword(companyIds.toString()));
                requestValues.put("host", EdsContextParams.getHost(company.getObjectID()));
                requestValues.put("logo", EdsContextParams.getLogoWithHost(company.getObjectID()));
                if (company != null) {
                    requestValues.put("TIME", formatDateShort(calendar.getTime(), company));
                } else {
                    requestValues.put("TIME", defaultShortDateFormat(calendar.getTime()));
                }
                requestValues.put("admin", admin);
                requestValues.put("user", employee);
                requestValues.put("companyid", companyid);
                requestValues.put("uid", id);
                final String keyValue = activationLinkManager.saveActivationLink(company.getObjectID(), employee.getObjectID(), null);
                requestValues.put("keyValue", keyValue);
                requestValues.put("currentYear", LocalDateTime.now().getYear());

                String displaySubject = admin.getFullName() + " " + commonLocalizer.localize(CREATED_AN_ACCOUNT_FOR_YOU_AT) + " " + EdsContextParams.getProductName();

                String requestText = "";
                if (EdsContextParams.getHostname().contains(BRAIN_UZ_DOMAIN) || EdsContextParams.getHostname().contains(BRAIN_UZ2_DOMAIN)) {
                    requestText = EdsTemplates.processTemplateWhiteLabel(admin, requestValues, EdsTemplates.EMPLOYEE_ADD_BRAINUZ);
                } else {
                    requestText = EdsTemplates.processTemplate(admin, requestValues, EdsTemplates.EMPLOYEE_ADD);
                }
                registerInternalMessage(receiverEmail, subject, requestText, admin.getEmail(), false, null, false, displaySubject, company.getObjectID(), false, true);
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendEmployeeAddNotificationFromGoogleMarket(EdsEmployee employee, EdsUser admin) throws EdsDbException {
        try {
            EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(employee.getCompany().getObjectID());

            String receiverEmail = employee.getEmail();
            String subject = commonLocalizer.localize(ACCOUNT_CREATION_NOTIFICATION);
            Map<String, Object> requestValues = new TreeMap<>();
            EdsCompany company = employee.getCompany();
            String dateFormat = "dd.MM.yyyy";
            SimpleDateFormat df;
            df = new SimpleDateFormat(dateFormat);
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(company.getCountryZone().getZone().getZoneID()));

            requestValues.put("HOST", EdsContextParams.getHost(company.getObjectID()));
            if (company != null) {
                requestValues.put("TIME", formatDateShort(calendar.getTime(), company));
            } else {
                requestValues.put("TIME", defaultShortDateFormat(calendar.getTime()));
            }
            requestValues.put("admin", admin);
            requestValues.put("user", employee);
            requestValues.put("googleappsdomain", settings.getGoogleAppDomain());
            String displaySubject = admin.getFullName() + " " + commonLocalizer.localize(CREATED_AN_ACCOUNT_FOR_YOU_AT) + " " + EdsContextParams.getProductName();
            String requestText = EdsTemplates.processTemplate(admin, requestValues, EdsTemplates.EMPLOYEE_ADD_FROM_GOOGLE_MARKET);

            registerInternalMessage(receiverEmail, subject, requestText, admin.getEmail(), false, null, false, displaySubject, company.getObjectID());

        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }


    public void sendEmployeeAddActivationLink(EdsEmployee employee, EdsUser admin) throws EdsDbException {
        sendEmployeeAddNotificationByOtherSupport(employee, admin, false, null);
    }

    private void sendEmployeeAddNotificationByOtherSupport(EdsEmployee employee, EdsUser admin, boolean isHaveOther, String otherSupport) throws EdsDbException {
        try {
            String receiverEmail = employee.getEmail();
            Map<String, Object> requestValues = new TreeMap<>();
            EdsCompany company = employee.getCompany();
            String subject = "id: " + company.getObjectID() + ": " + admin.getName() + " " + commonLocalizer.localize(USER_SENT_INVITATION);
            String dateFormat = "dd.MM.yyyy";
            SimpleDateFormat df;
            df = new SimpleDateFormat(dateFormat);
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(company.getCountryZone().getZone().getZoneID()));

            String id = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(employee.getObjectID().toString()));
            String companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(company.getObjectID().toString()));

            requestValues.put("host", EdsContextParams.getHost(company.getObjectID()));
            requestValues.put("logo", EdsContextParams.getLogoWithHost(company.getObjectID()));
            if (company != null) {
                requestValues.put("TIME", formatDateShort(calendar.getTime(), company));
            } else {
                requestValues.put("TIME", defaultShortDateFormat(calendar.getTime()));
            }
            requestValues.put("productName", EdsContextParams.getProductName());
            requestValues.put("admin", admin);
            requestValues.put("user", employee);
            requestValues.put("uid", id);
            requestValues.put("companyid", companyid);
            final String keyValue = this.activationLinkManager.getOrCreate(company.getObjectID(), employee.getObjectID(), null);

            requestValues.put("keyValue", keyValue);
            requestValues.put("currentYear", LocalDateTime.now().getYear());

            String displaySubject = admin.getFullName() + " " + commonLocalizer.localize(CREATED_AN_ACCOUNT_FOR_YOU_AT) + " " + EdsContextParams.getProductName();

            String requestText = "";
            if (EdsContextParams.getHostname().contains(BRAIN_UZ_DOMAIN) || EdsContextParams.getHostname().contains(BRAIN_UZ2_DOMAIN)) {
                requestText = EdsTemplates.processTemplateWhiteLabel(admin, requestValues, EdsTemplates.EMPLOYEE_ADD_BRAINUZ);
            } else {
                requestText = EdsTemplates.processTemplate(admin, requestValues, EdsTemplates.EMPLOYEE_ADD);
            }

            if (isHaveOther) {
                sendMessage(otherSupport, subject, EdsTemplates.processTemplate(admin, requestValues, EdsTemplates.REPLY_EMPLOYEE_ADD),
                        receiverEmail, false, null, displaySubject, company.getObjectID());
            } else {
                sendMessage(receiverEmail, subject, requestText, admin.getEmail(), false, null, displaySubject, company.getObjectID());
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendIssueAddNotification(EdsIssue issue, EdsUser recipient, EdsUser user) throws EdsDbException {
        try {
            sendIssueNotifications(issue, recipient, user, EmailNotificationConstants.ISSUE_ADD_NOTIFICATION, ISSUE_ADD_CATEGORY, commonLocalizer.localize(EdsSubjects.ISSUE_ADD_NOTIFICATION), EdsTemplates.ISSUE_ADD);
        } catch (EdsDbException ex) {
            ex.printStackTrace();
        }
    }

    public void sendIssueAddNotificationToClient(EdsIssue issue, EdsUser recipient, EdsUser user) throws EdsDbException {
        try {
            sendIssueNotifications(issue, recipient, user, EmailNotificationConstants.ISSUE_ADD_NOTIFICATION, ISSUE_ADD_CATEGORY, commonLocalizer.localize(EdsSubjects.ISSUE_ADD_NOTIFICATION), EdsTemplates.ISSUE_ADD_FOR_CLIENT);
        } catch (EdsDbException ex) {
            ex.printStackTrace();
        }
    }

    public void sendIssueAssignNotification(EdsIssue issue, EdsUser recipient, EdsUser user) throws EdsDbException {
        try {
            sendIssueNotifications(issue, recipient, user, EmailNotificationConstants.ISSUE_ASSIGN_NOTIFICATION, ISSUE_ASSIGN_CATEGORY, commonLocalizer.localize(EdsSubjects.ISSUE_ASSIGN_NOTIFICATION), EdsTemplates.ISSUE_ASSIGN);
        } catch (EdsDbException ex) {
            ex.printStackTrace();
        }
    }

    public void sendIssueDeleteNotification(EdsIssue issue, EdsEmployee recipient, EdsUser user) throws EdsDbException {
        try {
            sendIssueNotifications(issue, recipient, user, EmailNotificationConstants.ISSUE_DELETE_NOTIFICATION, ISSUE_DELETE_CATEGORY, commonLocalizer.localize(EdsSubjects.ISSUE_DELETE_NOTIFICATION), EdsTemplates.ISSUE_DELETE);
        } catch (EdsDbException ex) {
            ex.printStackTrace();
        }
    }

    public void sendIssueUpdateNotification(EdsIssue issue, EdsUser recipient, EdsUser user) throws EdsDbException {
        try {
            sendIssueNotifications(issue, recipient, user, EmailNotificationConstants.ISSUE_UPDATE_NOTIFICATION, ISSUE_UPDATE_CATEGORY, commonLocalizer.localize(EdsSubjects.ISSUE_UPDATE_NOTIFICATION), EdsTemplates.ISSUE_UPDATE);
        } catch (EdsDbException ex) {
            ex.printStackTrace();
        }
    }

    public void sendIssueUpdateNotificationToClient(EdsIssue issue, EdsUser recipient, EdsUser user) throws EdsDbException {
        try {
            sendIssueNotifications(issue, recipient, user, EmailNotificationConstants.ISSUE_UPDATE_NOTIFICATION, ISSUE_UPDATE_CATEGORY, commonLocalizer.localize(EdsSubjects.ISSUE_UPDATE_NOTIFICATION), EdsTemplates.ISSUE_UPDATE_FOR_CLIENT);
        } catch (EdsDbException ex) {
            ex.printStackTrace();
        }
    }

    private void sendIssueNotifications(EdsIssue issue, EdsUser recipient, EdsUser user, String notificationType, String category, String subject, String template) throws EdsDbException {
        try {
            if (recipient != null) {
                Integer userCompanyID = user.getCompany().getObjectID();
                boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(recipient.getObjectID(), notificationType);
                if (emailNotificationSettings) {
                    EmailTemplateItem templateItem = emailTemplateServiceLocal.generateIssueTemplateItem(issue, recipient, user, category);
                    if (templateItem != null) {
                        registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), /*replyTo,*/ userCompanyID);
                    } else {
                        String to = recipient.getEmail();
                        String companyID = EncryptionHelper.encryptURL(userCompanyID.toString());
                        Date creationDate = issue.getLastUpdateTime() != null ? issue.getLastUpdateTime() : user.getCompany().getCompanyDate();
                        Map<String, Object> values = new TreeMap<>();
                        values.put("issueNumber", issue.getNumber() != null ? issue.getNumber() : "");
                        values.put("recipientName", recipient.getName() != null ? recipient.getName() : "");
                        values.put("userName", user.getName() != null ? user.getName() : "");
                        values.put("issueName", issue.getName() != null ? issue.getName() : "");
                        values.put("description", issue.getDescription() != null ? issue.getDescription() : "");
                        values.put("visibility", issue.getAccess() != null ? (Constants.PUBLIC_ISSUE.equals(issue.getAccess()) ? commonLocalizer.localize("pub") :
                                (Constants.INTERNAL_ISSUE.equals(issue.getAccess()) ? commonLocalizer.localize("internal") : commonLocalizer.localize("priv"))) : "");
                        values.put("priority", issue.getPriority() != null ? issue.getPriority().getName() : "");
                        values.put("timesheet", issue.getShowInTimesheet() != null ? issue.getShowInTimesheet() ? commonLocalizer.localize("enabled") : commonLocalizer.localize("disabled") : "");
                        values.put("projectName", issue.getProject() != null ? issue.getProject().getName() : "");
                        values.put("creator", issue.getCreator() != null ? issue.getCreator().getFullName() : "");
                        values.put("reportedBy", issue.getReportedBy() != null ? issue.getReportedBy().getName() : "");
                        values.put("resolver", issue.getResolver() != null ? issue.getResolver().getName() : "");
                        values.put("period", (formatDateShort(user.getUserDate(issue.getStartDate()), user.getCompany()) + " - " + formatDateShort(user.getUserDate(issue.getDueDate()), user.getCompany())));
                        values.put("date", formatDateShort(user.getUserDate(creationDate), user.getCompany()));
                        values.put("status", issue.getIssueStatus() != null ? issue.getIssueStatus().getName() : "");
                        String assigns = "";
                        if (issue.getAssignments() != null && issue.getAssignments().size() > 0) {
                            for (EdsEmployeeTask item : issue.getAssignments()) {
                                assigns = assigns + (!"".equals(assigns) ? ", " + item.getName() : item.getName());
                            }
                        }
                        values.put("assignees", assigns);
                        values.put("host", EdsContextParams.getHost(userCompanyID));
                        values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("issue/" + issue.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(recipient.getObjectID().toString()) + "&" + C_ID + "=" + companyID);

                        String text = EdsTemplates.processTemplate(user, values, template);
                        if (!recipient.getDeleted()) {
                            registerInternalMessageBasic(to, subject, text, /*replyTo,*/ userCompanyID);
                        }
                    }
                }
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    private void setMessageType(String subject, EdsMessage message) {
        if (subject != null && (subject.contains(commonLocalizer.localize(EdsSubjects.SIGN_UP)) || commonLocalizer.localize(EdsSubjects.EMPLOYEE_ADD_NOTIFICATION).equals(subject) || commonLocalizer.localize(EdsSubjects.BUG_REPORT_TITLE).equals(subject)
                || "support@workforcetrack.com".equals(message.getTo()) || subject.contains(commonLocalizer.localize(EdsSubjects.SUBSCRIPTION_NON_PAID_NOTIFICATION)) ||
                subject.contains(EdsSubjects.SUBSCRIPTION_FREE_NOTIFICATION) || commonLocalizer.localize(EdsSubjects.GOOGLE_CONTACT_SYNC).equals(subject))) {
            message.setType(MessageTypeEnum.PREFERRED);
        } else {
            message.setType(MessageTypeEnum.NON_PREFERRED);
        }
    }

    public void sendMessage(String to, String subject, String text, String replyTo, Boolean hasAttachment, List<Integer> fileIDs,
                            String displaySubject, Integer companyId) throws EdsDbException {
        sendMessageFromUser(null, to, null, null, subject, text, hasAttachment, replyTo, fileIDs, false, displaySubject, companyId, null);

    }

    public void sendMessageWithBCC(String to, String bcc, String subject, String text, String replyTo, Boolean hasAttachment, List<Integer> fileIDs,
                                   String displaySubject, Integer companyId) throws EdsDbException {
        sendMessageFromUser(null, to, null, bcc, subject, text, hasAttachment, replyTo, fileIDs, false, displaySubject, companyId, null);
    }

    public void sendProjectAssignNotification(EdsProjectEmployee projectEmployee, EdsUser user) throws EdsDbException {

        try {
            Integer companyID = projectEmployee.getEmployeeDepartment().getEmployee().getCompany().getObjectID();
            if (projectEmployee.getEmployeeDepartment() != null && projectEmployee.getEmployeeDepartment().getEmployee() != null) {
                EdsProject edsProject = projectEmployee.getProject();
                if (!projectEmployee.getEmployeeDepartment().getEmployee().equals(user)) {
                    boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(
                            projectEmployee.getEmployeeDepartment().getEmployee().getObjectID(), EmailNotificationConstants.PROJECT_ASSIGN_NOTIFICATION);
                    if (emailNotificationSettings) {
                        String subjectPlusProjectNumberingViaName = " [" + edsProject.getNumber() + "] " + edsProject.getName();

                        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateProjectAssignTemplateItem(projectEmployee, user, PROJECT_ASSIGN_CATEGORY);
                        if (templateItem != null && !user.getDeleted()) {
                            String replyTo = edsProject.getManager().getEmail();
                            registerInternalMessage(templateItem.getToEmail(), (templateItem.getSubject() + subjectPlusProjectNumberingViaName), templateItem.getMessageHTML(), replyTo, false, null, false, null, companyID);
                        } else {
                            String to = projectEmployee.getEmployeeDepartment().getEmployee().getEmail();
                            String companyId = EncryptionHelper.encryptURL(companyID.toString());
                            String replyTo = edsProject.getManager().getEmail();
                            String subject = commonLocalizer.localize(EdsSubjects.PROJECT_ASSIGN_NOTIFICATION) + subjectPlusProjectNumberingViaName;
                            Map<String, Object> values = new TreeMap<>();
                            values.put("host", EdsContextParams.getHost(companyID));
                            values.put("projectEmployee", projectEmployee);
                            values.put("project", edsProject);
                            values.put("user", user);
                            values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("project/" + edsProject.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(projectEmployee.getEmployeeDepartment().getEmployee().getObjectID().toString()) + "&" + C_ID + "=" + companyId);
                            if (user != null) {
                                values.put("date", formatDate(user.getUserDate(user.getCompany().getCompanyDate()), user.getCompany()));
                                if (edsProject.getStartDate() != null) {
                                    values.put("startdate", formatDateShort(user.getUserDate(edsProject.getStartDate()), user.getCompany()));
                                } else {
                                    values.put("startdate", "");
                                }
                                if (edsProject.getEndDate() != null) {
                                    values.put("duedate", formatDateShort(user.getUserDate(edsProject.getEndDate()), user.getCompany()));
                                } else {
                                    values.put("duedate", "");
                                }

                            } else {
                                values.put("date", defaultLongDateFormat(user.getCompany().getCompanyDate()));
                                if (edsProject.getStartDate() != null) {
                                    values.put("startdate", defaultShortDateFormat(edsProject.getStartDate()));
                                } else {
                                    values.put("startdate", "");
                                }
                                if (edsProject.getEndDate() != null) {
                                    values.put("duedate", defaultShortDateFormat(edsProject.getEndDate()));
                                } else {
                                    values.put("duedate", "");
                                }
                            }

                            //project custom fields
                            List<CompanyCustomFieldItem> projectCustomFieldItems = getAllowedCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(edsProject.getProjectCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Project)), user);
                            values.put("projectCustomFields", getCastCustomFields(projectCustomFieldItems, user));
                            values.put("hasProjectCustomFields", (projectCustomFieldItems != null && projectCustomFieldItems.size() > 0 ? "true" : "false"));

                            values.put("clientname", edsProject.getClient() != null && edsProject.getClient().getName() != null ? edsProject.getClient().getName() : " ");

                            values.put("percent", edsProject.getPercent() != null ? edsProject.getPercent() : Float.valueOf("0.0"));
                            String text = EdsTemplates.processTemplate(user, values, EdsTemplates.ASSIGN_TO_PROJECT);
                            if (!projectEmployee.getEmployeeDepartment().getEmployee().getDeleted()) {
                                registerInternalMessage(to, subject, text, replyTo, false, null, false, null, companyID);
                            }
                        }
                    }
                }
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendSickRequestNotificationToSelectedEmployee(EdsUser toUser, EdsSickRequest request) throws EdsTemplateException {
        String name = "";
        if (request.getLeaveReason() != null) {
            name = referenceWfmMessageSource.localizeRef(request.getLeaveReason());
        }
        final String requestType = name.contains("request") || name.contains("Request") ? name : name + " request";
        final String subject = requestType + " " + commonLocalizer.localize(NOTIFICATION);

        EdsCompany company = toUser.getCompany();

        Map<String, Object> values = new HashMap<>();
        values.put("userTo", toUser.getName());
        values.put("LREmployee", request.getEmployee().getName());
        values.put("LRfromDate", formatDate(request.getStartDate(), toUser.getCompany()));
        values.put("LRendDate", formatDate(request.getEndDate(), toUser.getCompany()));
        values.put("NotificationBody", request.getDescription());

        values.put("host", EdsContextParams.getHost(company.getObjectID()));

        String messageContent = EdsTemplates.processTemplate(values, EdsTemplates.SEND_TO_SELECTED_EMPLOYEE_ADD_REQUEST);
        try {
            if (!toUser.getDeleted()) {
                registerInternalMessageBasic(toUser.getEmail(), subject, messageContent, company.getObjectID());
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void sendTaskAddNotification(EdsTask task, EdsUser taskCreator, EdsEmployee recipient, boolean subjectWithCustomFields) throws EdsDbException {
        try {
            String subject = commonLocalizer.localize(EdsSubjects.TASK_ADD_NOTIFIATION) + " [" + task.getNumber() + "] " + task.getName();
            List<CompanyCustomFieldItem> taskCustomFieldItems = getAllowedCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(task.getTaskCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Task)), recipient);
            if (subjectWithCustomFields) {
                String customFieldsT = getCastDepartmentCustomFieldForTaskFor31733Company(taskCustomFieldItems);
                subject = commonLocalizer.localize(NEW_TASK) + ": " + customFieldsT + " [" + task.getNumber() + "]";
            }
            Integer companyID = recipient.getCompany().getObjectID();
            String companyid = EncryptionHelper.encryptURL(companyID.toString());
            String to = recipient.getEmail();
            Map<String, Object> values = new TreeMap<>();
            if (task.getStatus() != null) {
                values.put("status", referenceWfmMessageSource.localizeRef(task.getStatus()));
            }
            if (task.getPriority() != null) {
                values.put("priority", referenceWfmMessageSource.localizeRef(task.getPriority()));
            }
            values.put("host", EdsContextParams.getHost(taskCreator.getCompany().getObjectID()));
            values.put("task", task);
            values.put("taskDescription", task.getDescription() != null ? task.getDescription() : "");
            values.put("recipient", recipient);
            values.put("user", taskCreator);
            values.put("creator", task.getCreator());
            values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task/" + task.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(recipient.getObjectID().toString()) + "&" + C_ID + "=" + companyid);
            values.put("assignees", mergeAssignees(task.getUnDeletedAssignments()));
            //task custom fields
            values.put("taskCustomFields", getCastCustomFields(taskCustomFieldItems, recipient));
            values.put("hasTaskCustomFields", (taskCustomFieldItems != null && taskCustomFieldItems.size() > 0 ? "true" : "false"));

            //
            if (recipient != null) {
                values.put("date", formatDate(recipient.getUserDate(recipient.getCompany().getCompanyDate()), recipient.getCompany()));
                if (task.getStartDate() != null) {
                    values.put("startdate", task.isAllDay() != null && task.isAllDay() ?
                            formatDateShort(recipient.getUserDate(task.getStartDate()), recipient.getCompany()) :
                            formatDate(recipient.getUserDate(task.getStartDate()), recipient.getCompany()));
                } else {
                    values.put("startdate", "");
                }
                if (task.getDueDate() != null) {
                    values.put("duedate", task.isAllDay() != null && task.isAllDay() ?
                            formatDateShort(recipient.getUserDate(task.getDueDate()), recipient.getCompany()) :
                            formatDate(recipient.getUserDate(task.getDueDate()), recipient.getCompany()));
                } else {
                    values.put("duedate", "");
                }
            } else {
                values.put("date", defaultLongDateFormat(recipient.getCompany().getCompanyDate()));
                values.put("startdate", task.getStartDate() != null ? defaultLongDateFormat(task.getStartDate()) : "");
                values.put("duedate", task.getDueDate() != null ? defaultLongDateFormat(task.getDueDate()) : "");
            }
            values.put("estimatedtime", task.getEstimatedTime() != null ? Utils.timeSpentToString(task.getEstimatedTime()) : "");
            values.put("completed", getCompletedPercent(task.getPercent() != null ? task.getPercent().intValue() : 0));
            values.put("clientname", task.getProject().getClient() != null ? task.getProject().getClient().getName() : "");
            values.put("description", task.getProject().getDescription() != null ? task.getProject().getDescription() : "");

            String text = EdsTemplates.processTemplate(taskCreator, values, EdsTemplates.TASK_ADD);
            if (!recipient.getDeleted() && !task.getProject().getManager().getDeleted()) {
                registerInternalMessage(to, subject, text, task.getProject().getManager().getEmail(), false, null, false, null, companyID);
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    private ArrayList<SelectItem> getCastCustomFields(List<CompanyCustomFieldItem> customFieldItems, EdsUser user) {
        ArrayList<SelectItem> castCustomFieldItems = new ArrayList<>();
        if (customFieldItems != null && customFieldItems.size() > 0) {
            SelectItem castCFItem;
            int i = 0;
            for (CompanyCustomFieldItem customF : customFieldItems) {
                castCFItem = new SelectItem(i++, customF.getFieldName());
                if (DATA_TYPE_DATE.equals(customF.getDataType())) {
                    castCFItem.setDescription(customF.getFieldDateNonConvertedValue() != null ? formatDateShort(user.getUserDate(customF.getFieldDateNonConvertedValue().getNonConvertedDate()), user.getCompany()) : "");
                } else {
                    castCFItem.setDescription(customF.getFieldStringValue() != null ? customF.getFieldStringValue() : "");
                }
                castCustomFieldItems.add(castCFItem);
            }
        }
        return castCustomFieldItems;
    }

    private ArrayList<CompanyCustomFieldItem> getAllowedCustomFields(List<CompanyCustomFieldItem> customFields, EdsUser user) {
        ArrayList<CompanyCustomFieldItem> allowedCustomFieldItems = new ArrayList<>();
        if (customFields != null && customFields.size() > 0) {
            for (CompanyCustomFieldItem allow : customFields) {
                if ((allow.getAllowedRoles() == null || allow.getAllowedRoles().size() == 0) || user.hasEitherRoles(allow.getAllowedRoles().toArray(new Integer[allow.getAllowedRoles().size()]))) {
                    allowedCustomFieldItems.add(allow);
                }
            }
        }
        return allowedCustomFieldItems;
    }

    /**
     * Generate department name custom field for 31733 company
     *
     * @param customFieldItems - task custom fields
     * @return - custom field - department name
     */
    private String getCastDepartmentCustomFieldForTaskFor31733Company(List<CompanyCustomFieldItem> customFieldItems) {
        String departmentNameCustomField = "";
        if (customFieldItems != null && customFieldItems.size() > 0) {
            for (CompanyCustomFieldItem customF : customFieldItems) {
                if ("Department".equals(customF.getFieldName()) && !DATA_TYPE_DATE.equals(customF.getDataType())) {
                    departmentNameCustomField = customF.getFieldStringValue();
                    break;
                }
            }
        }
        return departmentNameCustomField;
    }

    /**
     * send task assignees notification
     *
     * @param employeeTask
     * @param user
     * @throws EdsDbException
     */
    public void sendTaskAssignNotification(EdsEmployeeTask employeeTask, EdsUser user) throws EdsDbException {
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateForTask(employeeTask, user, TASK_ASSIGN_CATEGORY);
        Integer companyID = user.getCompany().getObjectID();
        EdsTask edsTask = employeeTask.getTask();
        String subjectPlusTaskNumberingViaName = " [" + edsTask.getNumber() + "] " + edsTask.getName();
        if (templateItem != null) {
            registerInternalMessage(templateItem.getToEmail(), (templateItem.getSubject() + subjectPlusTaskNumberingViaName), templateItem.getMessageHTML(), user.getEmail(), false, null, false, null, companyID);
        } else {
            try {
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                String to = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getEmail();
                String subject = commonLocalizer.localize(EdsSubjects.TASK_ASSIGN_NOTIFICATION) + subjectPlusTaskNumberingViaName;
                Map<String, Object> values = new TreeMap<>();
                if (employeeTask.getTask().getStatus() != null) {
                    values.put("status", referenceWfmMessageSource.localizeRef(employeeTask.getTask().getStatus()));
                }
                if (employeeTask.getTask().getPriority() != null) {
                    values.put("priority", referenceWfmMessageSource.localizeRef(employeeTask.getTask().getPriority()));
                }
                values.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
                values.put("employeeTask", employeeTask);
                values.put("taskDescription", edsTask.getDescription() != null ? edsTask.getDescription().replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>") : "");
                values.put("user", edsTask.getCreator());
                values.put("projectname", edsTask.getProject().getName());
                values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task/" + edsTask.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
                values.put("assignees", mergeAssignees(edsTask.getUnDeletedAssignments()));
                //task custom fields
                List<CompanyCustomFieldItem> taskCustomFieldItems = getAllowedCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(edsTask.getTaskCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Task)), user);
                values.put("taskCustomFields", getCastCustomFields(taskCustomFieldItems, user));
                values.put("hasTaskCustomFields", (taskCustomFieldItems != null && taskCustomFieldItems.size() > 0 ? "true" : "false"));

                if (user != null) {
                    values.put("date", formatDate(user.getUserDate(user.getCompany().getCompanyDate()), user.getCompany()));
                    if (edsTask.getStartDate() != null) {
                        values.put("startdate", edsTask.isAllDay() != null && edsTask.isAllDay() ?
                                formatDateShort(user.getUserDate(edsTask.getStartDate()), user.getCompany()) :
                                formatDate(user.getUserDate(edsTask.getStartDate()), user.getCompany()));
                    } else {
                        values.put("startdate", "");
                    }
                    if (edsTask.getDueDate() != null) {
                        values.put("duedate", edsTask.isAllDay() != null && edsTask.isAllDay() ?
                                formatDateShort(user.getUserDate(edsTask.getDueDate()), user.getCompany()) :
                                formatDate(user.getUserDate(edsTask.getDueDate()), user.getCompany()));
                    } else {
                        values.put("duedate", "");
                    }
                } else {
                    values.put("date", defaultLongDateFormat(user.getCompany().getCompanyDate()));
                    values.put("startdate", edsTask.getStartDate() != null ? defaultLongDateFormat(edsTask.getStartDate()) : "");
                    values.put("duedate", edsTask.getDueDate() != null ? defaultLongDateFormat(edsTask.getDueDate()) : "");
                }
                values.put("estimatedtime", edsTask.getEstimatedTime() != null ? Utils.timeSpentToString(edsTask.getEstimatedTime()) : "");
                values.put("completed", getCompletedPercent(edsTask.getPercent() != null ? edsTask.getPercent().intValue() : 0));
                values.put("clientname", edsTask.getProject().getClient() != null ? edsTask.getProject().getClient().getName() : "");
                values.put("description", edsTask.getProject().getDescription() != null ? edsTask.getProject().getDescription() : "");
                String text = EdsTemplates.processTemplate(user, values, EdsTemplates.ASSIGN_TO_TASK);
                if (!employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getDeleted() && !user.getDeleted()) {
                    registerInternalMessage(to, subject, text, user.getEmail(), false, null, false, null, companyID);
                }

            } catch (EdsTemplateException tex) {
                throw new EdsDbException(tex);
            }
        }
    }

    /**
     * Send multi task assign notification
     *
     * @param creator
     * @param employee
     * @param tasks
     * @throws EdsDbException
     */

    public void sendMultiTaskAssignNotification(EdsUser creator, EdsEmployee employee, HashSet<EdsTask> tasks) throws EdsDbException {
        try {
            Integer companyID = creator.getCompany().getObjectID();
            String companyid = EncryptionHelper.encryptURL(companyID.toString());
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateItemForMultiTaskAssign(creator, employee, tasks);
            if (templateItem != null) {
                registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), companyID);
            } else {
                ArrayList<TaskSingleItem> taskLists = new ArrayList<>();//faqat danniylarni set qilish uchun TaskListItem RPC dan foydalanildi!!!!!!!
                for (EdsTask task : tasks) {
                    TaskSingleItem taskL = new TaskSingleItem();
                    taskL.setName(task.getName());
                    taskL.setObjectID(task.getObjectID());
                    String link = EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task/" + task.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString()) + "&" + C_ID + "=" + companyid;
                    taskL.setAction(link);
                    taskLists.add(taskL);
                }
                String to = employee.getEmail();
                String subject = commonLocalizer.localize(EdsSubjects.TASK_ASSIGN_NOTIFICATION);
                Map<String, Object> values = new TreeMap<>();
                values.put("recipientName", employee.getName());
                values.put("creatorName", creator.getName());
                values.put("date", formatDate(employee.getUserDate(employee.getCompany().getCompanyDate()), employee.getCompany()));
                values.put("tasks", taskLists);
                values.put("host", EdsContextParams.getHost(companyID));

                String text = EdsTemplates.processTemplate(creator, values, EdsTemplates.ASSIGN_TO_MULTI_TASK);
                if (!employee.getDeleted()) {
                    registerInternalMessage(to, subject, text, creator.getEmail(), false, null, false, null, companyID);
                }
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendMultiAssignLeadNotification(Integer assignId, Integer assignCount) throws EdsDbException {
        try {
            EdsUser creator = userManager.getUser();
            Integer companyID = creator.getCompany().getObjectID();
            String companyid = EncryptionHelper.encryptURL(companyID.toString());

            String subject = "Leads Assigned Notification";

            EdsEmployee employee = employeeManager.get(assignId);
            String to = employee.getEmail();

            Map<String, Object> values = new TreeMap<>();
            values.put("recipientName", employee.getName());
            values.put("assignCount", assignCount);
            values.put("creatorName", creator.getName());
            values.put("date", formatDate(employee.getUserDate(employee.getCompany().getCompanyDate()), employee.getCompany()));
            values.put("host", EdsContextParams.getHost(companyID));

            String text = EdsTemplates.processTemplate(creator, values, EdsTemplates.ASSIGN_TO_MULTI_LEAD);
            if (!employee.getDeleted()) {
                registerInternalMessage(to, subject, text, creator.getEmail(), false, null, false, null, companyID);
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendTaskUpdateNotificationForClient(EdsUser receiver, EdsUser user, EdsTask task, EdsTaskHistory oldTask, String category, String subjectType, String template) throws EdsDbException {
        try {
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateForTaskUpdate(receiver, user, task, category);
            Integer companyID = user.getCompany().getObjectID();
            String subjectPlusTaskNumberingViaName = " [" + task.getNumber() + "] " + task.getName();

            if (templateItem != null && !user.getDeleted()) {
                registerInternalMessage(templateItem.getToEmail(), (templateItem.getSubject() + subjectPlusTaskNumberingViaName), templateItem.getMessageHTML(), user.getEmail(), false, null, false, null, companyID);
            } else {
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                String to = receiver.getEmail();
                Map<String, Object> values = new TreeMap<>();
                values.put("recipientName", receiver.getName());
                values.put("userName", user.getName());
                values.put("taskName", task.getName());
                values.put("date", formatDate(receiver.getUserDate(receiver.getCompany().getCompanyDate()), receiver.getCompany()));
                values.put("description", task.getDescription() != null ? task.getDescription().replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>") : "");
                if (task.getStartDate() != null) {
                    values.put("startdate", task.isAllDay() != null && task.isAllDay() ?
                            formatDateShort(user.getUserDate(task.getStartDate()), user.getCompany()) :
                            formatDate(user.getUserDate(task.getStartDate()), user.getCompany()));
                } else {
                    values.put("startdate", "");
                }
                if (task.getDueDate() != null) {
                    values.put("duedate", task.isAllDay() != null && task.isAllDay() ?
                            formatDateShort(user.getUserDate(task.getDueDate()), user.getCompany()) :
                            formatDate(user.getUserDate(task.getDueDate()), user.getCompany()));
                } else {
                    values.put("duedate", "");
                }
                values.put("assignees", mergeAssignees(task.getUnDeletedAssignments()));

                values.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
                values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task/" + task.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(receiver.getObjectID().toString()) + "&cid=" + companyid);

                if (oldTask != null && !task.getName().equals(oldTask.getName())) {
                    values.put("oldTaskName", oldTask.getName() != null ? oldTask.getName() : "");
                } else {
                    values.put("oldTaskName", "");
                }
                if (oldTask != null && !task.getDescription().equals(oldTask.getDescription())) {
                    values.put("oldTaskDescription", oldTask.getDescription() != null ? oldTask.getDescription().replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>") : "");
                } else {
                    values.put("oldTaskDescription", "");
                }

                if (oldTask != null && !task.getStartDate().equals(oldTask.getStartDate())) {
                    values.put("oldTaskStartDate", task.isAllDay() != null && task.isAllDay() ?
                            formatDateShort(user.getUserDate(oldTask.getStartDate()), user.getCompany()) :
                            formatDate(user.getUserDate(oldTask.getStartDate()), user.getCompany()));
                } else {
                    values.put("oldTaskStartDate", "");
                }

                if (oldTask != null && !task.getDueDate().equals(oldTask.getDueDate())) {
                    values.put("oldTaskDueDate", task.isAllDay() != null && task.isAllDay() ?
                            formatDateShort(user.getUserDate(oldTask.getDueDate()), user.getCompany()) :
                            formatDate(user.getUserDate(oldTask.getDueDate()), user.getCompany()));
                } else {
                    values.put("oldTaskDueDate", "");
                }

                String text = EdsTemplates.processTemplate(user, values, template);
                if (!user.getDeleted()) {
                    registerInternalMessage(to, (subjectType + subjectPlusTaskNumberingViaName), text, user.getEmail(), false, null, false, null, companyID);
                }
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendTaskNotification(EdsEmployeeTask employeeTask, EdsUser user, String category, String subjectType, String template) throws EdsDbException {
        sendTaskNotification(employeeTask, null, user, category, subjectType, template);
    }

    public void sendTaskNotification(EdsEmployeeTask employeeTask, EdsTaskHistory employeeOldTask, EdsUser user, String category, String subjectType, String template) throws EdsDbException {
        EdsUser empUser = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateForTask(employeeTask, user, category);
        Integer companyID = user.getCompany().getObjectID();
        EdsTask edsTask = employeeTask.getTask();
        String subjectPlusTaskNumberingViaName = " [" + edsTask.getNumber() + "] " + edsTask.getName();

        if (templateItem != null && !user.getDeleted()) {
            registerInternalMessage(templateItem.getToEmail(), (templateItem.getSubject() + subjectPlusTaskNumberingViaName), templateItem.getMessageHTML(), user.getEmail(), false, null, false, null, companyID);
        } else {
            try {
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                String to = empUser.getEmail();
                Map<String, Object> values = new TreeMap<>();
                values.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
                values.put("employeeTask", employeeTask);
                String employeeTaskStatus = referenceWfmMessageSource.localizeRef(employeeTask.getStatus());
                String employeeTaskPriority = referenceWfmMessageSource.localizeRef(employeeTask.getTask().getPriority());
                values.put("employeeTaskStatus", employeeTaskStatus);
                values.put("employeeTaskPriority", employeeTaskPriority);

                values.put("taskDescription", edsTask.getDescription() != null ? edsTask.getDescription().replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>") : "");
                values.put("user", user);
                values.put("creator", edsTask.getCreator());
                values.put("projectname", edsTask.getProject().getName());
                values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task/" + edsTask.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(empUser.getObjectID().toString()) + "&cid=" + companyid);
                values.put("assignees", mergeAssignees(edsTask.getUnDeletedAssignments()));
                //task custom fields
                List<CompanyCustomFieldItem> taskCustomFieldItems = getAllowedCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(edsTask.getTaskCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Task)), user);
                values.put("taskCustomFields", getCastCustomFields(taskCustomFieldItems, user));
                values.put("hasTaskCustomFields", (taskCustomFieldItems != null && taskCustomFieldItems.size() > 0 ? "true" : "false"));
                values.put("date", formatDate(empUser.getUserDate(empUser.getCompany().getCompanyDate()), empUser.getCompany()));
                if (edsTask.getStartDate() != null) {
                    values.put("startdate", edsTask.isAllDay() != null && edsTask.isAllDay() ?
                            formatDateShort(user.getUserDate(edsTask.getStartDate()), user.getCompany()) :
                            formatDate(user.getUserDate(edsTask.getStartDate()), user.getCompany()));
                } else {
                    values.put("startdate", "");
                }
                if (edsTask.getDueDate() != null) {
                    values.put("duedate", edsTask.isAllDay() != null && edsTask.isAllDay() ?
                            formatDateShort(user.getUserDate(edsTask.getDueDate()), user.getCompany()) :
                            formatDate(user.getUserDate(edsTask.getDueDate()), user.getCompany()));
                } else {
                    values.put("duedate", "");
                }
                values.put("estimatedtime", edsTask.getEstimatedTime() != null ? Utils.timeSpentToString(edsTask.getEstimatedTime()) : "");
                values.put("completed", getCompletedPercent(edsTask.getPercent() != null ? edsTask.getPercent().intValue() : 0));
                values.put("clientname", edsTask.getProject().getClient() != null ? edsTask.getProject().getClient().getName() : "");
                values.put("description", edsTask.getProject().getDescription() != null ? edsTask.getProject().getDescription() : "");
                //task history

                if (employeeOldTask != null && !edsTask.getName().equals(employeeOldTask.getName())) {
                    values.put("oldTaskName", employeeOldTask.getName() != null ? employeeOldTask.getName() : "");
                } else {
                    values.put("oldTaskName", "");
                }

                if (employeeOldTask != null && !edsTask.getNumber().equals(employeeOldTask.getNumber())) {
                    values.put("oldTaskNumber", employeeOldTask.getNumber() != null ? employeeOldTask.getNumber() : "");
                } else {
                    values.put("oldTaskNumber", "");
                }
                if (employeeOldTask != null && !edsTask.getDescription().equals(employeeOldTask.getDescription())) {
                    values.put("oldTaskDescription", employeeOldTask.getDescription() != null ? employeeOldTask.getDescription().replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>") : "");
                } else {
                    values.put("oldTaskDescription", "");
                }
                if (employeeOldTask != null && !edsTask.getCreator().equals(employeeOldTask.getCreator())) {
                    values.put("oldTaskCreator", employeeOldTask.getCreator());
                } else {
                    values.put("oldTaskCreator", "");
                }

                if (employeeOldTask != null && !edsTask.getProject().getName().equals(employeeOldTask.getProject().getName())) {
                    values.put("oldTaskProjectName", employeeOldTask.getProject().getName());
                } else {
                    values.put("oldTaskProjectName", "");
                }

                if (employeeOldTask != null && !edsTask.getStartDate().equals(employeeOldTask.getStartDate())) {
                    values.put("oldTaskStartDate", edsTask.isAllDay() != null && edsTask.isAllDay() ?
                            formatDateShort(user.getUserDate(employeeOldTask.getStartDate()), user.getCompany()) :
                            formatDate(user.getUserDate(employeeOldTask.getStartDate()), user.getCompany()));
                } else {
                    values.put("oldTaskStartDate", "");
                }

                if (employeeOldTask != null && !edsTask.getDueDate().equals(employeeOldTask.getDueDate())) {
                    values.put("oldTaskDueDate", edsTask.isAllDay() != null && edsTask.isAllDay() ?
                            formatDateShort(user.getUserDate(employeeOldTask.getDueDate()), user.getCompany()) :
                            formatDate(user.getUserDate(employeeOldTask.getDueDate()), user.getCompany()));
                } else {
                    values.put("oldTaskDueDate", "");
                }
                if (employeeOldTask != null && !edsTask.getEstimatedTime().equals(employeeOldTask.getEstimatedTime())) {
                    values.put("oldTaskEstimatedTime", Utils.timeSpentToString(employeeOldTask.getEstimatedTime()));
                } else {
                    values.put("oldTaskEstimatedTime", "");
                }
                if (employeeOldTask != null && !edsTask.getPercent().equals(employeeOldTask.getPercent())) {
                    values.put("oldTaskCompleted", getCompletedPercent(employeeOldTask.getPercent() != null ? employeeOldTask.getPercent().intValue() : 0));
                } else {
                    values.put("oldTaskCompleted", "");
                }
                if (employeeOldTask != null && !edsTask.getStatus().getName().equals(employeeOldTask.getStatus().getName())) {
                    values.put("oldTaskStatus", employeeOldTask.getStatus() != null ? referenceWfmMessageSource.localizeRef(employeeOldTask.getStatus()) : "");
                } else {
                    values.put("oldTaskStatus", "");
                }
                if (employeeOldTask != null && !edsTask.getPriority().getName().equals(employeeOldTask.getPriority().getName())) {
                    values.put("oldTaskPriority", employeeOldTask.getPriority() != null ? referenceWfmMessageSource.localizeRef(employeeOldTask.getPriority()) : "");
                } else {
                    values.put("oldTaskPriority", "");
                }


                String text = EdsTemplates.processTemplate(user, values, template);
                if (!user.getDeleted()) {
                    registerInternalMessage(to, (subjectType + subjectPlusTaskNumberingViaName), text, user.getEmail(), false, null, false, null, companyID);
                }

            } catch (EdsTemplateException tex) {
                throw new EdsDbException(tex);
            }
        }
    }

    public void sendCompletedPredTaskNotification(EdsEmployeeTask employeeTask, EdsUser user, String predTask) throws EdsDbException {
        EdsUser empUser = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateForTask(employeeTask, user, TASK_COMPLETED_PREDECESSOR_CATEGORY);
        Integer companyID = user.getCompany().getObjectID();
        if (templateItem != null && !user.getDeleted()) {
            registerInternalMessage(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), user.getEmail(), false, null, false, null, companyID);
        } else {
            try {
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                String to = empUser.getEmail();
                Map<String, Object> values = new TreeMap<>();
                values.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
                values.put("employeeTask", employeeTask);
                values.put("complatedTask", predTask);
                values.put("user", user);
                values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task/" + employeeTask.getTask().getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(empUser.getObjectID().toString()) + "&cid=" + companyid);
                values.put("assignees", mergeAssignees(employeeTask.getTask().getUnDeletedAssignments()));
                values.put("date", formatDate(empUser.getUserDate(empUser.getCompany().getCompanyDate()), empUser.getCompany()));

                String text = EdsTemplates.processTemplate(user, values, EdsTemplates.TASK_COMPLETED_PREDECESSOR);
                String subject = commonLocalizer.localize(EdsSubjects.TASK_COMPLETED_PREDECESSOR_NOTIFICATION);
                if (!user.getDeleted()) {
                    registerInternalMessage(to, subject, text, user.getEmail(), false, null, false, null, companyID);
                }

            } catch (EdsTemplateException tex) {
                throw new EdsDbException(tex);
            }
        }
    }

    public void sendTaskCompletedNotification(Integer receiverID, Integer updaterID, EdsTask task) throws EdsDbException {
        EdsUser receiver = userManager.get(receiverID);
        EdsUser updater = userManager.get(updaterID);
        Integer companyID = updater.getCompany().getObjectID();
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateForTaskUpdate(receiver, updater, task, TASK_COMPLETED_CATEGORY);
        if (templateItem != null && !receiver.getDeleted()) {
            registerInternalMessage(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), updater.getEmail(), false, null, false, null, companyID);
        } else {
            try {
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                String toEmail = receiver.getEmail();
                Map<String, Object> values = new TreeMap<>();
                values.put("host", EdsContextParams.getHost(companyID));
                values.put("task", task);
                values.put("oldTaskName", "");
                values.put("oldTaskNumber", "");
                values.put("oldTaskDescription", "");
                values.put("taskPriority", task.getPriority() != null ? referenceWfmMessageSource.localize(task.getPriority().getCode(), task.getPriority().getName(), ServerUtils.getUserLocale()) : "");
                values.put("oldTaskPriority", "");
                values.put("taskStatus", task.getStatus() != null ? referenceWfmMessageSource.localize(task.getStatus().getCode(), task.getStatus().getName(), ServerUtils.getUserLocale()) : "");
                values.put("oldTaskStatus", "");
                values.put("completed", getCompletedPercent(task.getPercent() != null ? task.getPercent().intValue() : 0));
                values.put("oldTaskCompleted", "");
                values.put("estimatedtime", task.getEstimatedTime() != null ? Utils.timeSpentToString(task.getEstimatedTime()) : "");
                values.put("oldTaskEstimatedTime", "");

                //task custom fields
                List<CompanyCustomFieldItem> taskCustomFieldItems = getAllowedCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(task.getTaskCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Task)), updater);
                values.put("taskCustomFields", getCastCustomFields(taskCustomFieldItems, updater));
                values.put("hasTaskCustomFields", (taskCustomFieldItems != null && taskCustomFieldItems.size() > 0 ? "true" : "false"));
                values.put("creator", task.getCreator());
                values.put("oldTaskCreator", "");
                values.put("clientname", task.getProject().getClient() != null ? task.getProject().getClient().getName() : "");
                values.put("oldTaskProjectName", "");

                values.put("description", task.getDescription() != null ? task.getDescription().replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>") : "");
                values.put("receiver", receiver);
                values.put("updater", updater);
                values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task/" + task.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(receiver.getObjectID().toString()) + "&cid=" + companyid);
                values.put("assignees", mergeAssignees(task.getUnDeletedAssignments()));
                values.put("date", formatDate(receiver.getUserDate(receiver.getCompany().getCompanyDate()), receiver.getCompany()));
                if (task.getStartDate() != null) {
                    values.put("startdate", task.isAllDay() != null && task.isAllDay() ? formatDateShort(updater.getUserDate(task.getStartDate()), updater.getCompany()) : formatDate(updater.getUserDate(task.getStartDate()), updater.getCompany()));
                } else {
                    values.put("startdate", "");
                }
                values.put("oldTaskStartDate", "");
                if (task.getDueDate() != null) {
                    values.put("duedate", task.isAllDay() != null && task.isAllDay() ? formatDateShort(updater.getUserDate(task.getDueDate()), updater.getCompany()) : formatDate(updater.getUserDate(task.getDueDate()), updater.getCompany()));
                } else {
                    values.put("duedate", "");
                }
                values.put("oldTaskDueDate", "");

                String text = EdsTemplates.processTemplate(updater, values, EdsTemplates.TASK_STATUS_COMPLETED);
                String subject = commonLocalizer.localize(EdsSubjects.TASK_COMPLETED_NOTIFICATION);
                if (!receiver.getDeleted()) {
                    registerInternalMessage(toEmail, subject, text, updater.getEmail(), false, null, false, null, companyID);
                }
            } catch (EdsTemplateException ex) {
                throw new EdsDbException(ex);
            }
        }
    }

    public void sendWorkstreamOverDueDateReminder(EdsWorkStream workstream, EdsEmployee assignEmployee) throws EdsDbException {
        try {
            String subject = commonLocalizer.localize(WORKSTREAM_OVERDUE_REMINDER) + ": " + "[" + workstream.getNumber() + "] " + workstream.getName();
            String to = assignEmployee.getEmployeeDepartment().getEmployee().getEmail();
            String companyid = EncryptionHelper.encryptURL(ServerSecurityContext.getInstance().getCompanyId());
            EdsUser user = workstream.getCreator();
            EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.WORKSTREAM_OVERDUE_REMINDER, workstream.getObjectID(), user.getCompany().getObjectID());
            Integer workstreamReminderTime = Integer.parseInt(recurrence.getBusObjectParams());

            String reminderTime = "";
            if (workstreamReminderTime < 60) {
                reminderTime = workstreamReminderTime + "minutes";
            } else if (workstreamReminderTime == 60) {
                reminderTime = "1 hour";
            } else if (workstreamReminderTime == 60 * 2) {
                reminderTime = "2 hours";
            } else if (workstreamReminderTime == 60 * 3) {
                reminderTime = "3 hours";
            } else if (workstreamReminderTime == 60 * 12) {
                reminderTime = "12 hours";
            } else if (workstreamReminderTime == 60 * 24) {
                reminderTime = "1 day";
            } else if (workstreamReminderTime == 60 * 24 * 3) {
                reminderTime = "3 days";
            } else if (workstreamReminderTime == 60 * 24 * 5) {
                reminderTime = "5 days";
            } else if (workstreamReminderTime == 60 * 24 * 7) {
                reminderTime = "1 week";
            } else if (workstreamReminderTime == 60 * 24 * 7 * 2) {
                reminderTime = "2 weeks";
            }

            Map<String, Object> values = new TreeMap<>();
            values.put("overtime", reminderTime);
            values.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
            values.put("employeeName", assignEmployee.getEmployee().getName());
            values.put("user", user);
            values.put("workstreamname", workstream.getName());
            values.put("description", workstream.getDescription());
            if (user != null) {
                values.put("date", formatDate(user.getUserDate(user.getCompany().getCompanyDate()), user.getCompany()));
                values.put("startdate", workstream.getStartDate() != null ? formatDate(user.getUserDate(workstream.getStartDate()), user.getCompany()) : "");
                values.put("duedate", workstream.getEndDate() != null ? formatDate(user.getUserDate(workstream.getEndDate()), user.getCompany()) : "");
            } else {
                values.put("date", defaultLongDateFormat(user.getCompany().getCompanyDate()));
                values.put("startdate", workstream.getStartDate() != null ? defaultLongDateFormat(workstream.getStartDate()) : "");
                values.put("duedate", workstream.getEndDate() != null ? defaultLongDateFormat(workstream.getEndDate()) : "");
            }
            values.put("completed", getCompletedPercent(workstream.getPercent() != null ? workstream.getPercent().intValue() : 0));
            values.put("clientname", workstream.getProject().getClient() != null ? workstream.getProject().getClient().getName() : "");
            String text = EdsTemplates.processTemplate(workstream.getCreator(), values, EdsTemplates.WORKSTREAM_ADD_DUE_DATE_REMINDER);
            if (!assignEmployee.getEmployeeDepartment().getEmployee().getDeleted()) {
                registerInternalMessage(to, subject, text, user.getEmail(), false, null, false, null, user.getCompany().getObjectID());
                notificationMsgManager.createWorkstreamOverDueDateReminderNotification(workstream, assignEmployee.getEmployeeDepartment().getEmployee());
            }
        } catch (EdsTemplateException e) {
            throw new EdsDbException(e);
        }
    }

    public void sendProjectOverDueDateReminder(EdsProject project, EdsProjectEmployee employeeProject) throws EdsDbException {
        try {
            String subject = commonLocalizer.localize(PROJECT_OVERDUE_REMINDER) + ": " + "[" + project.getNumber() + "] " + project.getName();
            String to = employeeProject.getEmployeeDepartment().getEmployee().getEmail();
            String companyid = EncryptionHelper.encryptURL(ServerSecurityContext.getInstance().getCompanyId());
            EdsUser user = project.getCreator();
            EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.PROJECT_OVERDUE_REMINDER, project.getObjectID(), user.getCompany().getObjectID());
            Integer projectReminderTime = Integer.parseInt(recurrence.getBusObjectParams());

            List<EdsProjectEmployee> projectEmployees = projectManager.getEmployeesByProject(project.getObjectID());
            String reminderTime = "";
            if (projectReminderTime < 60) {
                reminderTime = projectReminderTime + "minutes";
            } else if (projectReminderTime == 60) {
                reminderTime = "1 hour";
            } else if (projectReminderTime == 60 * 2) {
                reminderTime = "2 hours";
            } else if (projectReminderTime == 60 * 3) {
                reminderTime = "3 hours";
            } else if (projectReminderTime == 60 * 12) {
                reminderTime = "12 hours";
            } else if (projectReminderTime == 60 * 24) {
                reminderTime = "1 day";
            } else if (projectReminderTime == 60 * 24 * 3) {
                reminderTime = "3 days";
            } else if (projectReminderTime == 60 * 24 * 5) {
                reminderTime = "5 days";
            } else if (projectReminderTime == 60 * 24 * 7) {
                reminderTime = "1 week";
            } else if (projectReminderTime == 60 * 24 * 7 * 2) {
                reminderTime = "2 weeks";
            }

            Map<String, Object> values = new TreeMap<>();
            values.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
            values.put("user", user.getName());
            values.put("employeeName", employeeProject.getEmployeeDepartment().getEmployee().getFullName());
            values.put("employeeProject", employeeProject);
            values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("project/" + project.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeProject.getObjectID().toString()) + "&" + C_ID + "=" + companyid);
            if (user != null) {
                values.put("date", formatDate(user.getUserDate(user.getCompany().getCompanyDate()), user.getCompany()));
                values.put("startdate", project.getStartDate() != null ? formatDate(user.getUserDate(project.getStartDate()), user.getCompany()) : "");
                values.put("duedate", project.getDueDate() != null ? formatDate(user.getUserDate(project.getDueDate()), user.getCompany()) : "");
            } else {
                values.put("date", defaultLongDateFormat(user.getCompany().getCompanyDate()));
                values.put("startdate", project.getStartDate() != null ? defaultLongDateFormat(project.getStartDate()) : "");
                values.put("duedate", project.getDueDate() != null ? defaultLongDateFormat(project.getDueDate()) : "");
            }
            values.put("completed", getCompletedPercent(project.getPercent() != null ? project.getPercent().intValue() : 0));
            values.put("clientname", project.getClient() != null ? project.getClient().getName() : "");
            StringBuilder employees = new StringBuilder();
            if (projectEmployees != null && projectEmployees.size() > 0) {
                for (EdsProjectEmployee employee : projectEmployees) {
                    if (employee != null && employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getEmployee() != null) {
                        employees.append(employees.toString() != "" ? "," + employee.getEmployeeDepartment().getEmployee().getFullName() : employee.getEmployeeDepartment().getEmployee().getFullName());
                    }
                }
            }
            values.put("projectEmployeeList", employees.toString());
            String text = EdsTemplates.processTemplate(project.getCreator(), values, EdsTemplates.PROJECT_ADD_DUE_DATE_REMINDER);
            if (!employeeProject.getEmployeeDepartment().getEmployee().getDeleted()) {
                registerInternalMessage(to, subject, text, user.getEmail(), false, null, false, null, user.getCompany().getObjectID());
                EdsEmployee projectUser = employeeProject.getEmployeeDepartment().getEmployee();
                log.info(NotificationTypeEnum.ProjectDueReminder.name() + ", Username:" + projectUser.getFullName() + "," + projectUser.getDeviceToken() + "," + projectUser.getDeviceType());
                notificationMsgManager.createProjectOverDueDateReminderNotification(project, employeeProject.getEmployeeDepartment().getEmployee());
            }
        } catch (EdsTemplateException e) {
            throw new EdsDbException(e);
        }
    }

    public void sendTaskOverDueDateReminder(EdsTask task, EdsEmployeeTask employeeTask) throws EdsDbException {
        try {
            String subject = commonLocalizer.localize(TASK_DUE_REMINDER) + ": " + "[" + task.getNumber() + "] " + task.getName();
            String to = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getEmail();
            String companyid = EncryptionHelper.encryptURL(ServerSecurityContext.getInstance().getCompanyId());
            EdsUser user = task.getCreator();
            EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.TASK_OVERDUE_REMINDER, task.getObjectID(), user.getCompany().getObjectID());
            Integer taskReminderTime = Integer.parseInt(recurrence.getBusObjectParams());

            String reminderTime = "";
            if (taskReminderTime < 60) {
                reminderTime = taskReminderTime + "minutes";
            } else if (taskReminderTime == 60) {
                reminderTime = "1 hour";
            } else if (taskReminderTime == 60 * 2) {
                reminderTime = "2 hours";
            } else if (taskReminderTime == 60 * 3) {
                reminderTime = "3 hours";
            } else if (taskReminderTime == 60 * 12) {
                reminderTime = "12 hours";
            } else if (taskReminderTime == 60 * 24) {
                reminderTime = "1 day";
            } else if (taskReminderTime == 60 * 24 * 3) {
                reminderTime = "3 days";
            } else if (taskReminderTime == 60 * 24 * 5) {
                reminderTime = "5 days";
            } else if (taskReminderTime == 60 * 24 * 7) {
                reminderTime = "1 week";
            } else if (taskReminderTime == 60 * 24 * 7 * 2) {
                reminderTime = "2 weeks";
            }

            Map<String, Object> values = new TreeMap<>();
            if (employeeTask.getTask().getStatus() != null) {
                values.put("status", referenceWfmMessageSource.localizeRef(employeeTask.getTask().getStatus()));
            }
            if (employeeTask.getTask().getPriority() != null) {
                values.put("priority", referenceWfmMessageSource.localizeRef(employeeTask.getTask().getPriority()));
            }
            values.put("overtime", reminderTime);
            values.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
            values.put("employeeTask", employeeTask);
            values.put("user", user);
            EdsTask employeeTaskTask = employeeTask.getTask();
            values.put("projectname", employeeTaskTask.getProject().getName());
            values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task/" + employeeTaskTask.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
            values.put("assignees", mergeAssignees(employeeTaskTask.getUnDeletedAssignments()));
            if (user != null) {
                values.put("date", formatDate(user.getUserDate(user.getCompany().getCompanyDate()), user.getCompany()));
                values.put("startdate", employeeTaskTask.getStartDate() != null ? formatDate(user.getUserDate(employeeTaskTask.getStartDate()), user.getCompany()) : "");
                values.put("duedate", employeeTaskTask.getDueDate() != null ? formatDate(user.getUserDate(employeeTaskTask.getDueDate()), user.getCompany()) : "");
            } else {
                values.put("date", defaultLongDateFormat(user.getCompany().getCompanyDate()));
                values.put("startdate", employeeTaskTask.getStartDate() != null ? defaultLongDateFormat(employeeTaskTask.getStartDate()) : "");
                values.put("duedate", employeeTaskTask.getDueDate() != null ? defaultLongDateFormat(employeeTaskTask.getDueDate()) : "");
            }
            values.put("estimatedtime", employeeTaskTask.getEstimatedTime() != null ? Utils.timeSpentToString(employeeTaskTask.getEstimatedTime()) : "");
            values.put("completed", getCompletedPercent(employeeTaskTask.getPercent() != null ? employeeTaskTask.getPercent().intValue() : 0));
            values.put("clientname", employeeTaskTask.getProject().getClient() != null ? employeeTaskTask.getProject().getClient().getName() : "");
            String text = EdsTemplates.processTemplate(task.getCreator(), values, EdsTemplates.TASK_ADD_DUE_DATE_REMINDER);
            if (!employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getDeleted()) {
                registerInternalMessage(to, subject, text, user.getEmail(), false, null, false, null, user.getCompany().getObjectID());
                EdsEmployee taskUser = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
                log.info(NotificationTypeEnum.TaskDueReminder.name() + ", Username:" + taskUser.getFullName() + "," + taskUser.getDeviceToken() + "," + taskUser.getDeviceType());
                notificationMsgManager.createTaskOverDueDateReminderNotification(task, employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee());
            }
        } catch (EdsTemplateException e) {
            throw new EdsDbException(e);
        }
    }

    private String getCompletedPercent(Integer percent) {
        return " " + percent + "%";
    }

    public void sendTeamAddNotification(EdsDepartment team, EdsUser user) throws EdsDbException {

        for (EdsEmployee dir : employeeManager.getDirectors()) {
            sendTeamAddNotification(team, dir, user);
        }
        for (EdsEmployee hr : employeeManager.getHRManagers()) {
            sendTeamAddNotification(team, hr, user);
        }
        for (EdsEmployee admin : employeeManager.getAdministrators()) {
            sendTeamAddNotification(team, admin, user);
        }
    }

    public void sendTeamAssignNotification(EdsEmployeeDepartment teamEmployee, EdsUser user) throws EdsDbException {
        try {
            EdsCompany company = user.getCompany();
            if (!teamEmployee.getEmployee().getDeleted() && !user.getDeleted()) {
                String to = teamEmployee.getEmployee().getEmail();
                String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());
                String subject = commonLocalizer.localize(EdsSubjects.TEAM_ASSIGN_NOTIFICATION);
                HashMap<String, Object> values = new HashMap<>();
                values.put("host", EdsContextParams.getHost(company.getObjectID()));
                values.put("teamEmployee", teamEmployee);
                values.put("recipient", teamEmployee.getEmployee());
                values.put("team", teamEmployee.getTeam());
                values.put("user", user);
                values.put("link", EncryptionHelper.encryptURL("department/" + teamEmployee.getTeam().getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(teamEmployee.getEmployee().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
                values.put("date", formatDate(company.getCompanyDate(), company));
                String text = EdsTemplates.processTemplate(user, values, EdsTemplates.ASSIGN_TO_TEAM);
                registerInternalMessage(to, subject, text, (teamEmployee.getTeam().getLeader() != null ? teamEmployee.getTeam().getLeader().getEmail() : null), false, null, false, null, company.getObjectID());
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendTeamLeaderAssignNotification(EdsDepartment team, EdsUser user) throws EdsDbException {
        try {
            EdsCompany company = user.getCompany();
            String to = team.getLeader().getEmail();
            String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());
            String subject = commonLocalizer.localize(EdsSubjects.TEAN_LEADER_ASSIGN_NOTIFICATION);
            Map<String, Object> values = new TreeMap<>();
            values.put("host", EdsContextParams.getHost(company.getObjectID()));
            values.put("team", team);
            values.put("user", user);
            values.put("link", EncryptionHelper.encryptURL("department/" + team.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(team.getLeader().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
            if (user != null) {
                values.put("date", formatDate(company.getCompanyDate(), company));
            } else {
                values.put("date", defaultLongDateFormat(company.getCompanyDate()));
            }
            String text = EdsTemplates.processTemplate(user, values, EdsTemplates.TEAM_ASSIGN_TEAMLEADER);
            if (!user.getDeleted()) {
                registerInternalMessageBasic(to, subject, text, company.getObjectID());
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendProjectDeleteNotification(EdsProject project, EdsUser recipient, EdsUser user) {
        try {
            sendProjectDeleteOrUpdateNotification(project, recipient, user, EmailNotificationConstants.PROJECT_DELETE_NOTIFICATION, PROJECT_DELETE_CATEGORY, commonLocalizer.localize(EdsSubjects.PROJECT_DELETE_NOTIFICATION), EdsTemplates.PROJECT_DELETE);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void sendProjectUpdateNotification(EdsProject project, EdsUser recipient, EdsUser user) {
        try {
            sendProjectDeleteOrUpdateNotification(project, recipient, user, EmailNotificationConstants.PROJECT_UPDATE_NOTIFICATION, PROJECT_UPDATE_CATEGORY, commonLocalizer.localize(EdsSubjects.PROJECT_UPDATE_NOTIFICATION, "Update Project"), EdsTemplates.PROJECT_UPDATE);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    private void sendProjectDeleteOrUpdateNotification(EdsProject project, EdsUser recipient, EdsUser user, String notificationType, String category, String subject, String template) throws EdsDbException {
        try {
            Integer companyID = user.getCompany().getObjectID();
            String subjectPlusProjectNumberingViaName = " [" + project.getNumber() + "] " + project.getName();
            boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(recipient.getObjectID(), notificationType);
            if (emailNotificationSettings) {
                EmailTemplateItem templateItem = emailTemplateServiceLocal.generateProjectTemplateItem(project, recipient, user, category);
                if (templateItem != null) {
                    registerInternalMessage(templateItem.getToEmail(), (templateItem.getSubject() + subjectPlusProjectNumberingViaName), templateItem.getMessageHTML(), project.getManager().getEmail(), false, null, false, null, companyID);
                } else {
                    String to = recipient.getEmail();
                    String companyid = EncryptionHelper.encryptURL(companyID.toString());
                    Map<String, Object> values = new TreeMap<>();
                    values.put("host", EdsContextParams.getHost(companyID));
                    values.put("project", project);
                    values.put("recipient", recipient);
                    values.put("user", user);
                    values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("project/" + project.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(recipient.getObjectID().toString()) + "&cid=" + companyid);
                    Date date = project.getLastUpdateTime() != null ? project.getLastUpdateTime() : recipient.getCompany().getCompanyDate();
                    if (user != null) {
                        values.put("date", date != null ? formatDate(user.getUserDate(date), user.getCompany()) : "");
                        values.put("startdate", project.getStartDate() != null ? formatDateShort(user.getUserDate(project.getStartDate()), user.getCompany()) : "");
                        values.put("duedate", project.getEndDate() != null ? formatDateShort(user.getUserDate(project.getEndDate()), user.getCompany()) : "");
                    } else {
                        values.put("date", date != null ? defaultLongDateFormat(date) : "");
                        values.put("startdate", project.getStartDate() != null ? defaultShortDateFormat(project.getStartDate()) : "");
                        values.put("duedate", project.getEndDate() != null ? defaultShortDateFormat(project.getEndDate()) : "");
                    }
                    values.put("clientname", project.getClient() != null ? project.getClient().getName() : " ");
                    List<EdsProjectEmployee> projectEmployees = projectEmployeeManager.getProjectEmployees(project);
                    String employees = "";
                    if (projectEmployees != null && projectEmployees.size() > 0) {
                        for (EdsProjectEmployee item : projectEmployees) {
                            employees += !"".equals(employees) ? ", " + item.getName() : item.getName();
                        }
                    }
                    values.put("projectEmployees", employees);
                    //project custom fields
                    List<CompanyCustomFieldItem> projectCustomFieldItems = getAllowedCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(project.getProjectCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Project)), user);
                    values.put("projectCustomFields", getCastCustomFields(projectCustomFieldItems, user));
                    values.put("hasProjectCustomFields", (projectCustomFieldItems != null && projectCustomFieldItems.size() > 0 ? "true" : "false"));
                    values.put("percent", project.getPercent() != null ? project.getPercent() : Float.valueOf("0.0"));
                    String text = EdsTemplates.processTemplate(user, values, template);

                    registerInternalMessage(to, (subject + subjectPlusProjectNumberingViaName), text, project.getManager().getEmail(), false, null, false, null, companyID);
                }
            }

        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendTeamAddNotification(EdsDepartment team, EdsEmployee recipient, EdsUser user) throws EdsDbException {
        boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(
                recipient.getObjectID(), EmailNotificationConstants.DEPARTMENT_ADD_NOTIFICATION);
        if (emailNotificationSettings) {
            try {
                if (!recipient.equals(user)) {
                    String to = recipient.getEmail();
                    Integer companyId = user.getCompany().getObjectID();
                    String companyid = EncryptionHelper.encryptURL(companyId.toString());
                    String subject = commonLocalizer.localize(EdsSubjects.TEAM_ADD_NOTIFICATION);
                    Map<String, Object> values = new TreeMap<>();
                    values.put("host", EdsContextParams.getHost(companyId));
                    values.put("team", team);
                    values.put("recipient", recipient);
                    values.put("user", user);
                    values.put("link", EncryptionHelper.encryptURL("department/" + team.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(recipient.getObjectID().toString()) + "&" + C_ID + "=" + companyid);
                    values.put("date", formatDate(user.getCompany().getCompanyDate(), user.getCompany()));
                    String text = EdsTemplates.processTemplate(user, values, EdsTemplates.TEAM_ADD);
                    if (!user.getDeleted()) {
                        registerInternalMessageBasic(to, subject, text, companyId);
                    }
                }
            } catch (EdsTemplateException tex) {
                throw new EdsDbException(tex);
            }
        }
    }

    public void sendToClient(EdsClientContact client, EdsUser creator, String subject) throws EdsDbException, EdsTemplateException {
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateClientActivationNewUserEmailTemplate(client, creator, subject);
        if (templateItem != null) {
            EdsUser user = creator != null ? creator : getUser();
            registerInternalMessage(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), user.getEmail(), false, null, false, null, templateItem.getCompanyId());
        } else {
            Map<String, Object> mapValues = new TreeMap<>();
            String uid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(client.getObjectID().toString()));
            Integer companyID = client.getCompany().getObjectID();
            String companyName = client.getCompany().getName();
            String companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(companyID.toString()));
            mapValues.put("HOST", EdsContextParams.getHost(companyID));
            mapValues.put("user", client);
            mapValues.put("CLIENTNAME", StringUtils.capitalize(client.getFirstName()) + " " + client.getLastName());
            mapValues.put("id", uid);
            mapValues.put("companyid", companyid);
            mapValues.put("companyName", companyName);
            mapValues.put("ACCESS_TYPE", client.getAccessType().toLowerCase());
            final String keyValue = activationLinkManager.saveActivationLink(companyID, client.getObjectID(), null);
            mapValues.put("keyValue", keyValue);

            String receiverEmail = client.getEmail();

            EdsUser user = creator != null ? creator : getUser();
            mapValues.put("MANAGER", StringUtils.capitalize(user.getFirstName()) + " " + user.getLastName());
            String requestText;
            requestText = EdsTemplates.processTemplate(creator, mapValues, EdsTemplates.CLIENT_CONTACT);

            registerInternalMessage(receiverEmail, subject, requestText, user.getEmail(), false, null, false, null, companyID);
        }
    }

    public void sendToStoreFrontClient(EdsClientContact client, EdsUser creator, String subject) throws EdsDbException, EdsTemplateException {
        Map<String, Object> mapValues = new TreeMap<>();
        String id = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(client.getObjectID().toString()));
        Integer companyID = client.getCompany().getObjectID();
        String companyName = client.getCompany().getName();
        String companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(companyID.toString()));
        String serviceid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(SpringPropertiesUtil.getProperty("kpi.discovery.service-id")));
        mapValues.put("HOST", EdsContextParams.getHost(companyID));
        mapValues.put("user", client);
        mapValues.put("CLIENTNAME", StringUtils.capitalize(client.getFirstName()) + " " + client.getLastName());
        mapValues.put("id", id);
        mapValues.put("companyid", companyid);
        mapValues.put("serviceid", serviceid);
        mapValues.put("companyName", companyName);

        String receiverEmail = client.getEmail();

        EdsUser user = creator != null ? creator : getUser();
        mapValues.put("MANAGER", StringUtils.capitalize(user.getFirstName()) + " " + user.getLastName());
        String requestText;
        requestText = EdsTemplates.processTemplate(creator, mapValues, EdsTemplates.STOREFRONT_CLIENT_CONTACT);

        registerInternalMessage(receiverEmail, subject, requestText, user.getEmail(), false, null, false, null, companyID);
    }

    public void sendToClientWithoutActivationLink(EdsClientContact client, EdsUser creator, String subject) throws EdsDbException, EdsTemplateException {
        Map<String, Object> mapValues = new TreeMap<>();
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateClientActivationExistingUserEmailTemplate(client, creator, subject);
        if (templateItem != null) {
            EdsUser user = creator != null ? creator : getUser();
            registerInternalMessage(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), user.getEmail(), false, null, false, null, templateItem.getCompanyId());
        } else {
            String uid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(client.getObjectID().toString()));

            EdsCompany company = client.getCompany();
            String companyName = company.getName();
            mapValues.put("HOST", EdsContextParams.getHost(company.getObjectID()));
            mapValues.put("user", client);
            mapValues.put("CLIENTNAME", StringUtils.capitalize(client.getFirstName()) + " " + client.getLastName());
            mapValues.put("id", uid);
            mapValues.put("companyName", companyName);
            mapValues.put("ACCESS_TYPE", client.getAccessType().toLowerCase());

            String receiverEmail = client.getEmail();

            EdsUser user = creator != null ? creator : getUser();
            mapValues.put("MANAGER", StringUtils.capitalize(user.getFirstName()) + " " + user.getLastName());
            String requestText;
            requestText = EdsTemplates.processTemplate(creator, mapValues, EdsTemplates.CLIENT_CONTACT_WITHOUT_ACTIVATION_LINK);
            registerInternalMessage(receiverEmail, subject, requestText, user.getEmail(), false, null, false, null, company.getObjectID());
        }
    }

    @Override
    public void sendToClientUsernamePassword(EdsClientContact client, String subject) throws EdsDbException, EdsTemplateException {
        Map<String, Object> mapValues = new TreeMap<>();
        String username = client.getUserName();
        String password = client.getPassword();
        Integer companyID = client.getCompany().getObjectID();
        String companyName = client.getCompany().getName();

        mapValues.put("CLIENTNAME", StringUtils.capitalize(client.getFirstName()) + " " + client.getLastName());
        mapValues.put("USERNAME", username);
        mapValues.put("PASSWORD", password);
        mapValues.put("companyName", companyName);

        String receiverEmail = client.getEmail();

        EdsUser user = getUser();

        String requestText;
        requestText = EdsTemplates.processTemplate(user, mapValues, EdsTemplates.CLIENT_WITH_USERNAME_PASSWORD);

        registerInternalMessage(receiverEmail, subject, requestText, user != null ? user.getEmail() : "", false, null, false, null, companyID);
    }

    @Override
    public void sendToClientWithoutUsernamePassword(EdsClientContact client, String subject) throws EdsDbException, EdsTemplateException {
        Map<String, Object> mapValues = new TreeMap<>();

        Integer companyID = client.getCompany().getObjectID();
        String companyName = client.getCompany().getName();

        mapValues.put("CLIENTNAME", StringUtils.capitalize(client.getFirstName()) + " " + client.getLastName());
        mapValues.put("companyName", companyName);

        String receiverEmail = client.getEmail();

        EdsUser user = getUser();

        String requestText;
        requestText = EdsTemplates.processTemplate(user, mapValues, EdsTemplates.CLIENT_WITHOUT_USERNAME_PASSWORD);

        registerInternalMessage(receiverEmail, subject, requestText, user.getEmail(), false, null, false, null, companyID);
    }

    private void sendToClientByOtherSupport(EdsClientContact client, EdsUser creator, boolean isHaveOther, String otherSupport, boolean clientContactExist) throws EdsDbException, EdsTemplateException {
        Map<String, Object> mapValues = new TreeMap<>();
        String uid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(client.getObjectID().toString()));
        EdsCompany company = client.getCompany();
        String companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(company.getObjectID().toString()));
        String serviceid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(SpringPropertiesUtil.getProperty("kpi.discovery.service-id")));
        String companyName = company.getName();

        mapValues.put("HOST", EdsContextParams.getHost(company.getObjectID()));
        mapValues.put("productName", EdsContextParams.getProductName());
        mapValues.put("user", client);
        mapValues.put("CLIENTNAME", StringUtils.capitalize(client.getFirstName()) + " " + client.getLastName());
        mapValues.put("uid", uid);
        mapValues.put("companyid", companyid);
        mapValues.put("serviceid", serviceid);
        mapValues.put("companyName", companyName);
        mapValues.put("ACCESS_TYPE", client.getAccessType().toLowerCase());

        String receiverEmail = client.getEmail();
        String subject = commonLocalizer.localize(EMPLOYEE_ADD_NOTIFICATION);
        if (isHaveOther) {
            mapValues.put("COMPANY", company.getName());
            sendMessage(otherSupport, subject, EdsTemplates.processTemplate(mapValues, EdsTemplates.REPLY_CLIENT_CONTACT), receiverEmail, false, null, null, null);
        } else {
            EdsUser user = creator != null ? creator : getUser();
            mapValues.put("MANAGER", StringUtils.capitalize(user.getFirstName()) + " " + user.getLastName());
            String requestText;
            if (clientContactExist) {
                requestText = EdsTemplates.processTemplate(user, mapValues, EdsTemplates.CLIENT_CONTACT_WITHOUT_ACTIVATION_LINK);
            } else {
                final String keyValue = activationLinkManager.saveActivationLink(company.getObjectID(), client.getObjectID(), null);
                mapValues.put("keyValue", keyValue);

                requestText = EdsTemplates.processTemplate(user, mapValues, EdsTemplates.CLIENT_CONTACT);
            }
            sendMessage(receiverEmail, subject, requestText, user.getEmail(), false, null, null, null);
        }
    }

    @Override
    public int compare(Object o1, Object o2) {
        EdsTimeSheet ts1 = (EdsTimeSheet) o1;
        EdsTimeSheet ts2 = (EdsTimeSheet) o2;
        if (ts1.getDate().before(ts2.getDate())) {
            return -1;
        } else {
            return 1;
        }
    }

    public static class CompanyLogRecord {
        private EdsCompany company;

        public EdsCompany getCompany() {
            return company;
        }

        public void setCompany(EdsCompany company) {
            this.company = company;
        }

        public List<EdsEmployee> getCompanyAdmin() {
            return companyAdmin;
        }

        public void setCompanyAdmin(List<EdsEmployee> companyAdmin) {
            this.companyAdmin = companyAdmin;
        }

        private List<EdsEmployee> companyAdmin;
    }

    public static class CompLogRecord {
        private EdsCompany company;
        private Integer projects;
        private Integer tasks;
        private Integer employees;
        private Integer teams;
        private Integer clients;
        private Integer issues;
        private String timesheet;
        private List<String> sections;
        private Integer taskForTimesheet = 0;
        private List<EdsEmployee> companyAdmin;


        public Integer getTeams() {
            return teams;
        }

        public void setTeams(Integer teams) {
            this.teams = teams;
        }

        public Integer getClients() {
            return clients;
        }

        public void setClients(Integer clients) {
            this.clients = clients;
        }

        public Integer getIssues() {
            return issues;
        }

        public void setIssues(Integer issues) {
            this.issues = issues;
        }

        public List<EdsEmployee> getCompanyAdmin() {
            return companyAdmin;
        }

        public void setCompanyAdmin(List<EdsEmployee> companyAdmin) {
            this.companyAdmin = companyAdmin;
        }

        public Integer getTaskForTimesheet() {
            return taskForTimesheet;
        }

        public void setTaskForTimesheet(Integer taskForTimesheet) {
            this.taskForTimesheet = taskForTimesheet;
        }

        public String getTimesheet() {
            return timesheet;
        }

        public void setTimesheet(String timesheet) {
            this.timesheet = timesheet;
        }

        public List<String> getSections() {
            return sections;
        }

        public void setSections(List<String> sections) {
            this.sections = sections;
        }

        public Integer getEmployees() {
            return employees;
        }

        public void setEmployees(Integer employees) {
            this.employees = employees;
        }

        public EdsCompany getCompany() {
            return company;
        }

        public void setCompany(EdsCompany company) {
            this.company = company;
        }

        public Integer getProjects() {
            return projects;
        }

        public void setProjects(Integer projects) {
            this.projects = projects;
        }

        public Integer getTasks() {
            return tasks;
        }

        public void setTasks(Integer tasks) {
            this.tasks = tasks;
        }
    }

    public static class CompanyPARecord {
        private EdsCompany company;
        private Integer initiated;
        private Integer reviewed;
        private Integer rated;
        private Integer approved;

        public EdsCompany getCompany() {
            return company;
        }

        public void setCompany(EdsCompany company) {
            this.company = company;
        }

        public Integer getInitiated() {
            return initiated;
        }

        public void setInitiated(Integer initiated) {
            this.initiated = initiated;
        }

        public Integer getReviewed() {
            return reviewed;
        }

        public void setReviewed(Integer reviewed) {
            this.reviewed = reviewed;
        }

        public Integer getRated() {
            return rated;
        }

        public void setRated(Integer rated) {
            this.rated = rated;
        }

        public Integer getApproved() {
            return approved;
        }

        public void setApproved(Integer approved) {
            this.approved = approved;
        }

    }

    public void sendDailyReportNotification() throws EdsDbException, EdsTemplateException {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH);
        Date date = new Date();
        Date startTime = new Date();
        Date endTime = new Date();

        startTime.setHours(0);
        startTime.setMinutes(0);
        startTime.setSeconds(0);
        endTime.setHours(23);
        endTime.setMinutes(59);
        endTime.setSeconds(59);

        List<EdsCompany> newSignUpers = companyManager.getCompaniesByRegDate(startTime, endTime);
        Integer usersUsedSystemCount = userSessionManager.getUserIdsByAccDate(startTime, endTime).size();
        Integer empAddedCompaniesCount = 0;
        List<EdsEmployee> createdEmployees = employeeManager.getEmployeesByRegDate(startTime, endTime, null, false);
        Integer projectAddedCompaniesCount = projectManager.getCompaniesByProjectRegDate(startTime, endTime).size();
        List<EdsProject> createdProjects = projectManager.getProjectsByRegDate(startTime, endTime, null, false);
        Integer taskAddedCompaniesCount = taskManager.getCompaniesByTaskRegDate(startTime, endTime).size();
        List<EdsTask> createdTasks = taskManager.getTasksByRegDate(startTime, endTime, null, false);
        Integer timesheetUsedTasksCount = timeSheetManager.getTimeSheetTasksByRegDate(startTime, endTime).size();
        Integer timesheetUsedCompaniesCount = timeSheetManager.getCompaniesByTSDate(startTime, endTime).size();

        List<CompanyLogRecord> records = new LinkedList<>();
        for (EdsCompany company : newSignUpers) {
            CompanyLogRecord record = new CompanyLogRecord();
            record.setCompany(company);
            record.setCompanyAdmin(employeeManager.getAdministrators());
            records.add(record);
        }
        List<CompLogRecord> compsLoggedAndUsedSystem = new LinkedList<>();
        List<CompLogRecord> onlyLoginUsedUsers = new LinkedList<>();
        List<EdsCompany> compsUsedSystemToday = companyManager.getCompaniesUsedSystemByDate(startTime, endTime);
        for (EdsCompany company : compsUsedSystemToday) {
            CompLogRecord logRecord = new CompLogRecord();
            logRecord.setEmployees(employeeManager.getEmployeesByRegDate(startTime, endTime, company, false).size());
            logRecord.setProjects(projectManager.getProjectsByRegDate(startTime, endTime, company, false).size());
            logRecord.setTasks(taskManager.getTasksByRegDate(startTime, endTime, company, false).size());
            logRecord.setTeams(departmentManager.getDepartmentsByRegDate(startTime, endTime, company, false).size());
            logRecord.setClients(clientManager.getClientsByRegDate(startTime, endTime, company).size());
            logRecord.setIssues(0);
            logRecord.setCompany(company);
            Integer ts = timeSheetManager.getCompanyTimeSpentByDate(company, startTime, endTime);
            Integer h = ts / 60;
            Integer m = ts % 60;
            logRecord.setTimesheet(h + ":" + m);
            if (logRecord.getEmployees() > 0 || logRecord.getProjects() > 0 || logRecord.getTasks() > 0 || logRecord.getClients() > 0 || logRecord.getIssues() > 0) {
                List<String> sections = new LinkedList<>();
                if (employeeManager.getEmployeesByRegDate(startTime, endTime, company, true).size() > 0) {
                    sections.add("employee");
                }
                if (projectManager.getProjectsByRegDate(startTime, endTime, company, true).size() > 0) {
                    sections.add("project");
                }
                if (taskManager.getTasksByRegDate(startTime, endTime, company, true).size() > 0) {
                    sections.add("task");
                }
                if (logRecord.getClients() > 0) {
                    sections.add("client");
                }
                if (logRecord.getIssues() > 0) {
                    sections.add("issue");
                }
                logRecord.setSections(sections);
                compsLoggedAndUsedSystem.add(logRecord);
            } else {
                onlyLoginUsedUsers.add(logRecord);
            }
        }

        List<EdsCompany> paCompanies = assessmentManager.getCompaniesUsedPAByDate(startTime, endTime);
        List<CompanyPARecord> paRecords = new LinkedList<>();
        CompanyPARecord paRecord;
        for (EdsCompany company : paCompanies) {
            paRecord = new CompanyPARecord();
            int initiated = 0;
            int reviewed = 0;
            int approved = 0;
            int rated = 0;
            for (EdsEmployeeAssessment emplAssess : assessmentManager.getAssessmentsByCompanyAndDate(startTime, endTime, company)) {
                if (Constants.INITIATED.equals(emplAssess.getStatus().getCode())) {
                    initiated++;
                }
                if (Constants.REVIEWED_BY_MANAGER.equals(emplAssess.getStatus().getCode())) {
                    reviewed++;
                }
                if (Constants.RATED.equals(emplAssess.getStatus().getCode())) {
                    rated++;
                }
                if (Constants.APPROVED.equals(emplAssess.getStatus().getCode())) {
                    approved++;
                }
            }
            paRecord.setCompany(company);
            paRecord.setInitiated(initiated);
            paRecord.setReviewed(reviewed);
            paRecord.setRated(rated);
            paRecord.setApproved(approved);
            paRecords.add(paRecord);
        }
        Map<String, Object> mapValues = new TreeMap<>();
        mapValues.put("today", format.format(date));
        mapValues.put("newSignUpersCount", newSignUpers.size());
        mapValues.put("totalUsers", usersUsedSystemCount);
        mapValues.put("empAddedCompaniesCount", empAddedCompaniesCount);
        mapValues.put("companyAddedEmployeesCount", createdEmployees.size());
        mapValues.put("projectAddedCompaniesCount", projectAddedCompaniesCount);
        mapValues.put("companyAddedProjectsCount", createdProjects.size());
        mapValues.put("taskAddedCompaniesCount", taskAddedCompaniesCount);
        mapValues.put("companyAddedTasksCount", createdTasks.size());
        mapValues.put("timesheetUsedCompaniesCount", timesheetUsedCompaniesCount);
        mapValues.put("timesheetUsedTasksCount", timesheetUsedTasksCount);
        mapValues.put("newSignUPers", records);
        mapValues.put("logReport", compsLoggedAndUsedSystem);
        mapValues.put("cPAReport", paRecords);
        mapValues.put("onlyLoginUsedUsers", onlyLoginUsedUsers);

        String requestText = EdsTemplates.processTemplate(mapValues, EdsTemplates.DAILYREPORT_SEND);
        String subject = EdsContextParams.getHostname() + " " + commonLocalizer.localize(DAILY_REPORT);

        sendMessage(defaultSupportEmail, subject, requestText, null, false, null, null, null);
    }

    public void sendSubscriptionExpirationReportNotification(EdsCompany company, EdsEmployee receiver, int days, Date expireDate, String subject) throws EdsTemplateException {
        try {
            if (receiver != null && !receiver.getDeleted()) {
                Integer companyID = company.getObjectID();
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                String accountName = company.getName();
                String toEmail = receiver.getEmail();
                //
                Map<String, Object> values = new TreeMap<>();
                values.put("userName", receiver.getName());
                String receivedMessageCount = days == 30 ? "first" : days == 14 ? "second" : days == 7 ? "third" : "";
                values.put("receivedMessageCount", receivedMessageCount);
                String daysCount = (days == 1 ? "a day" : days + " days");
                values.put("daysCount", daysCount);
                values.put("host", EdsContextParams.getHost(companyID));
                values.put("logo", EdsContextParams.getLogoWithHost(companyID));
                values.put("expireDateSimple", shortSimpleDateFormat(expireDate));
                values.put("supportEmail", EdsContextParams.getSupportEmail());
                String accountNameAndId = "id: " + companyID + ", " + accountName;
                values.put("accountNameAndId", accountNameAndId);
                values.put("currentYear", LocalDateTime.now().getYear());
                String afterSendEmailDate = days == 30 ? "2 weeks" : days == 14 ? "1 week" : days == 7 ? "1 day" : "";
                values.put("afterSendEmailDate", afterSendEmailDate);
                String link = EncryptionHelper.encodeURL("#" + EncryptionHelper.encryptURL("settings|CurrentUsagePlanView")) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(receiver.getObjectID().toString()) + "&" + C_ID + "=" + companyid;
                values.put("link", link);
                String QAEmail = "munir@kpi.com";
                String requestText = EdsTemplates.processTemplate(values, EdsTemplates.NEW_SUBSCRIPTION_EXPIRATION_REPORT);
                sendMessageWithBCC(toEmail, QAEmail, subject, requestText, null, false, null, null, companyID);
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void sendInsufficientFundsNotification(EdsUser receiver) throws EdsTemplateException {
        try {
            if (receiver != null && !receiver.getDeleted()) {
                EdsCompany company = receiver.getCompany();
                Integer companyID = company.getObjectID();
                String toEmail = "munir@kpi.com";
                String subject = company.getName() + "(" + companyID + "): Your card has insufficient funds.";
                //
                Map<String, Object> values = new TreeMap<>();
                values.put("userName", receiver.getName());
                values.put("supportEmail", EdsContextParams.getSupportEmail());
                values.put("userEmail", receiver.getEmail());
                values.put("companyId", companyID);
                values.put("companyName", company.getName());

                String requestText = EdsTemplates.processTemplate(values, EdsTemplates.INSUFFICIENT_FUNDS);
                sendMessage(toEmail, subject, requestText, null, false, null, null, companyID);
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void sendFreeTrialExpirationReportNotification(EdsCompany company, EdsEmployee receiver, int days, Date expireDate, String subject) throws EdsTemplateException {
        try {
            if (receiver != null && !receiver.getDeleted()) {
                Integer companyID = company.getObjectID();
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                String accountName = company.getName();
                String toEmail = receiver.getEmail();
                //
                Map<String, Object> values = new TreeMap<>();
                values.put("userName", receiver.getName());
                String daysCount = (days == 1 ? "a day" : days + " days");
                values.put("daysCount", daysCount);
                values.put("host", EdsContextParams.getHost(companyID));
                values.put("logo", EdsContextParams.getLogoWithHost(companyID));
                values.put("expireDateSimple", shortSimpleDateFormat(expireDate));
                values.put("supportEmail", EdsContextParams.getSupportEmail());
                String accountNameAndId = "id: " + companyID + ", " + accountName;
                values.put("accountNameAndId", accountNameAndId);
                values.put("currentYear", LocalDateTime.now().getYear());
                String link = EncryptionHelper.encodeURL("#" + EncryptionHelper.encryptURL("settings|CurrentUsagePlanView")) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(receiver.getObjectID().toString()) + "&" + C_ID + "=" + companyid;
                values.put("link", link);

                String requestText = EdsTemplates.processTemplate(values, EdsTemplates.FREE_TRIAL_EXPIRATION_REPORT);
                sendMessage(toEmail, subject, requestText, null, false, null, null, companyID);
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void sendSubscriptionExpiredReportNotification(EdsCompany company, EdsEmployee receiver, String subject) throws EdsTemplateException {
        try {
            if (receiver != null && !receiver.getDeleted()) {
                Integer companyID = company.getObjectID();
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                String accountName = company.getName();
                String toEmail = receiver.getEmail();
                //
                Map<String, Object> values = new TreeMap<>();
                values.put("userName", receiver.getName());
                values.put("host", EdsContextParams.getHost(companyID));
                values.put("logo", EdsContextParams.getLogoWithHost(companyID));
                values.put("supportEmail", EdsContextParams.getSupportEmail());
                values.put("currentYear", LocalDateTime.now().getYear());
                values.put("companyName", accountName);
                values.put("companyId", companyID);
                String link = EncryptionHelper.encodeURL("#" + EncryptionHelper.encryptURL("settings|CurrentUsagePlanView")) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(receiver.getObjectID().toString()) + "&" + C_ID + "=" + companyid;
                values.put("link", link);
                String requestText = EdsTemplates.processTemplate(values, EdsTemplates.SUBSCRIPTION_EXPIRED_REPORT);
                sendMessageWithBCC(toEmail, "munir@kpi.com", subject, requestText, null, false, null, null, companyID);
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void sendReferSomeoneMessage(EdsCompany company, EdsUser sender, String receiver, String content) throws EdsTemplateException {
        try {
            if (receiver != null && !receiver.isEmpty()) {
                String subject = sender.getEmail() + " shared with you";
                Integer companyID = company.getObjectID();
                String companyName = company.getName();

                Map<String, Object> values = new TreeMap<>();
                values.put("senderName", sender.getFullName());
                values.put("senderEmail", sender.getEmail());
                values.put("content", content);
                values.put("companyName", companyName);
                values.put("productName", EdsContextParams.getProductName());
                values.put("supportEmail", EdsContextParams.getSupportEmail());
                values.put("currentYear", LocalDateTime.now().getYear());
                values.put("logo", EdsContextParams.getLogoWithHost(companyID));
                values.put("host", EdsContextParams.getHost(companyID));
                values.put("promoCode", companyID);
                String link = "https://www.kpi.com";
                values.put("link", link);

                String requestText = EdsTemplates.processTemplate(values, EdsTemplates.REFER_SOMEONE);
                sendMessage(receiver, subject, requestText, sender.getEmail(), false, null, null, companyID);
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void sendUserPostedToFacebookMessage(EdsCompany company, EdsUser sender, String receiver, String content) throws EdsTemplateException {
        try {
            if (receiver != null && !receiver.isEmpty()) {
                String subject = "Facebook - Client Shared";
                Integer companyID = company.getObjectID();
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                String companyName = company.getName();

                Map<String, Object> values = new TreeMap<>();
                values.put("senderName", sender.getFullName());
                values.put("senderEmail", sender.getEmail());
                values.put("companyName", companyName);
                values.put("content", content);
                values.put("currentYear", LocalDateTime.now().getYear());
                values.put("logo", EdsContextParams.getHost(companyID));
                values.put("host", EdsContextParams.getLogoWithHost(companyID));

                String requestText = EdsTemplates.processTemplate(values, EdsTemplates.POSTED_TO_FACEBOOK);
                sendMessage(receiver, subject, requestText, sender.getEmail(), false, null, null, companyID);
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

//    public void sendFreeTrialExpirationReportNotification(EdsCompany company, EdsEmployee receiver) throws EdsTemplateException {
//        try {
//            if (true) {
//                Integer companyID = company.getObjectID();
//                String companyid = EncryptionHelper.encryptURL(companyID.toString());
//                String accountName = "Kpi Software Inc";
//                String toEmail = "munir@kpi.com,bilol@kpi.com";
//                int days = 15;
//                Date expireDate = new Date();
//                receiver.setObjectID(1);
//
//                Map<String, Object> values = new TreeMap<>();
//                values.put("userName", "Serdar Karaliev");
//                String receivedMessageCount = days == 30 ? "first" : days == 14 ? "second" : days == 7 ? "third" : "";
//                values.put("receivedMessageCount", receivedMessageCount);
//                String daysCount = (days == 1 ? "a day" : days + " days");
//                values.put("daysCount", daysCount);
//                values.put("host", EdsContextParams.getHost(companyID));
//                values.put("expireDateSimple", shortSimpleDateFormat(expireDate));
//                values.put("supportEmail", EdsContextParams.getSupportEmail());
//                String accountNameAndId = "id: " + companyID + ", " + accountName;
//                values.put("accountNameAndId", accountNameAndId);
//                values.put("companyName", accountName);
//                values.put("companyId", companyID);
//                values.put("currentYear", LocalDateTime.now().getYear());
//                values.put("companyLogo", EdsContextParams.getLogoImage());///customisation/kpi.com/images/kpilogo.png
//                String afterSendEmailDate = days == 30 ? "2 weeks" : days == 14 ? "1 week" : days == 7 ? "1 day" : "";
//                values.put("afterSendEmailDate", afterSendEmailDate);
//                String link = EncryptionHelper.encodeURL("#" + EncryptionHelper.encryptURL("settings|CurrentUsagePlanView")) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(receiver.getObjectID().toString()) + "&" + C_ID + "=" + companyid;
//                values.put("link", link);
//
//                String requestText = EdsTemplates.processTemplate(values, EdsTemplates.SUBSCRIPTION_EXPIRED_REPORT);
//                sendMessage(toEmail, "Expiration", requestText, null, false, null, null, companyID);
//            }
//        } catch (EdsDbException e) {
//            e.printStackTrace();
//        }
//    }

    public void sendInvoiceQuoteToManager(String fromEmail, EdsUser user, String cc, String bcc, NewInvoice invoiceData, String clientContactName, List<Integer> fileIds, String type, String link, String replyTo) throws EdsDbException, EdsTemplateException {
        sendBaseInvoiceToManager(fromEmail, user, cc, bcc, invoiceData, clientContactName, fileIds, EdsTemplates.SENDER_INVOICING_MESSAGE, type, link, replyTo);
    }

    private String getTypeString(String type) {
        String typeString = "";
        if ("Invoice".equals(type)) {
            typeString = "Invoice Statement";
        } else if ("Quote".equals(type)) {
            typeString = "Sales Quote";
        } else if ("Order".equals(type)) {
            typeString = "Purchase Order";
        } else if ("Sales Order".equals(type)) {
            typeString = "Sales Order";
        } else if ("Receipt Invoice".equals(type)) {
            typeString = "Sales Receipt";
        } else if ("Credit Note".equals(type)) {
            typeString = "Credit Note";
        } else {
            typeString = "Invoice Statement";
        }
        return typeString;
    }

    @Override
    public void sendMessageForPayrun(String fromUserName, String fromEmail, String to, String subject, String text, List<Integer> fileIDs, Integer companyId) throws EdsDbException {
        EdsMessage message = new EdsMessage();
        message.setTo(to);
        message.setFromName(fromUserName);
        message.setFromEmail(fromEmail);
        message.setSubject(subject);
        message.setText(text);
        message.setAttachment(true);
        convertFileIDsToObjects(fileIDs, message);
        setMessageType(message.getSubject(), message);
        if (getUser() != null && getUser().getCompany() != null) {
            message.setCompanyID(getUser().getCompany().getObjectID());
        } else {
            message.setCompanyID(companyId);
        }
        if (blackListManager.isEmailValid(message.getTo())) {
            message.setStatus(MessageStatusEnum.PENDING);
            message.setAttempts(0);
            Date date = new Date();
            message.setCreationDate(date);
            create(message);
        } else {
            log.error("Unable to create message with this email >>>>>> " + message.getTo() + ("[C_ID=" + SecurityContext.getCompanyID() + "]"));
        }
    }

    @Override
    public void sendMessageFromUser(String fromEmail, String to, String cc, String bcc, String subject, String text, Boolean attachment, String replyTo,
                                    List<Integer> fileIDs, boolean isFileBody, String displaySubject, Integer companyId, EdsUser from) throws EdsDbException {
        //send message as istest=false
        sendMessageFromUser(fromEmail, to, cc, bcc, subject, text, attachment, replyTo,
                fileIDs, isFileBody, displaySubject, companyId, from, Boolean.FALSE);
    }

    @Override
    public void sendMessageFromUser(String fromEmail, String to, String cc, String bcc, String subject, String text, Boolean attachment, String replyTo,
                                    List<Integer> fileIDs, boolean isFileBody, String displaySubject, Integer companyId, EdsUser from, boolean isTest) throws EdsDbException {
        sendMessageFromUser(fromEmail, to, cc, bcc, subject, text, attachment, replyTo, fileIDs, isFileBody, displaySubject, companyId, from, isTest, false);
    }

    private void sendMessageFromUser(String fromEmail, String to, String cc, String bcc, String subject, String text, Boolean attachment, String replyTo,
                                     List<Integer> fileIDs, boolean isFileBody, String displaySubject, Integer companyId, EdsUser from, boolean isTest, boolean isSystem) throws EdsDbException {
        EdsMessage message = new EdsMessage();
        message.setTo(to);
        message.setCc(cc);
        message.setBcc(bcc);
        message.setTest(isTest);
        message.setSystem(isSystem);
        if (from != null) {
            message.setFromName(from.getName());
            message.setFromEmail((fromEmail != null && !"".equals(fromEmail)) ? fromEmail : from.getEmail());
        } else {
            message.setFromName(replyTo);
            message.setFromEmail((fromEmail != null && !"".equals(fromEmail)) ? fromEmail : replyTo);
        }
        message.setSubject(subject);
        message.setText(text);
        convertFileIDsToObjects(fileIDs, message);
        message.setAttachment(attachment);
        if (replyTo != null) {
            message.setReplyTo(replyTo);
        }
        setMessageType(message.getSubject(), message);
        if (companyId != null) {
            message.setCompanyID(companyId);
        } else if (getUser() != null && getUser().getCompany() != null) {
            message.setCompanyID(getUser().getCompany().getObjectID());
        } else if (from != null && from.getCompany() != null) {//Don't remove this part. It is used for recurring invoices. (Sherzod)
            message.setCompanyID(from.getCompany().getObjectID());
        }
        if (blackListManager.isEmailValid(message.getTo())) {
            message.setStatus(MessageStatusEnum.PENDING);
            message.setAttempts(0);
            Date date = new Date();
            message.setCreationDate(date);
            create(message);
        } else {
            log.error("Unable to create message with this email >>>>>> " + message.getTo() + ("[C_ID=" + SecurityContext.getCompanyID() + "]"));
        }
    }

    private void sendEmployeePayslipTemplate(String fromEmail, String to, String originalName, String subject, String text, String fromName, ByteArrayOutputStream pdfStream) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(pdfStream.toByteArray());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            EdsUpload upload = createUpload(inputStream, originalName, "application/pdf");
            ArrayList<Integer> fileIDs = new ArrayList<>();
            fileIDs.add(upload.getObjectID());
            EdsMessage message = new EdsMessage();
            message.setTo(to);
            message.setFromName(fromName);
            message.setSubject(subject);
            message.setText(text);
            convertFileIDsToObjects(fileIDs, message);
            message.setAttachment(true);
            setMessageType(message.getSubject(), message);
            if (getUser() != null && getUser().getCompany() != null) {
                message.setCompanyID(getUser().getCompany().getObjectID());
            }
            if (blackListManager.isEmailValid(message.getTo())) {
                message.setStatus(MessageStatusEnum.PENDING);
                message.setAttempts(0);
                Date date = new Date();
                message.setCreationDate(date);
                create(message);
            } else {
                log.error("Unable to create message with this email >>>>>> " + message.getTo() + ("[C_ID=" + SecurityContext.getCompanyID() + "]"));
            }
            inputStream.close();
            baos.flush();
            baos.close();
        } catch (Exception e) {
            log.error("Unable to work with PDF Stream.", e);
        }
    }

    private void sendMessageFormUser(String to, String originalName, String subject, String text, EdsUser from, ByteArrayOutputStream pdfStream) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(pdfStream.toByteArray());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            EdsUpload upload = createUpload(inputStream, originalName, "application/pdf");
            if (from != null) {
                ArrayList<Integer> fileIds = new ArrayList<>();
                fileIds.add(upload.getObjectID());
                sendMessageFromUser(null, to, null, null, subject, text, true, from.getEmail(), fileIds, false, null, null, from);
            }
            inputStream.close();
            baos.flush();
            baos.close();
        } catch (IOException ex) {
            log.error("Unable to work with PDF Stream.", ex);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    private void sendBaseInvoiceToManager(String fromEmail, EdsUser user, String cc, String bcc, NewInvoice invoiceData, String clientContactName, List<Integer> fileIDs, String message, String type, String link, String replyTo) throws EdsDbException, EdsTemplateException {
        Map<String, Object> mapValues = new TreeMap<>();
        Date date = invoiceData.getInvoiceDate().getNonConvertedDate();
        mapValues.put("user", user);
        mapValues.put("INVOICE_TYPE", getTypeString(type));
        mapValues.put("INVOICE_NUMBER", invoiceData.getInvoiceNumber());
        mapValues.put("CLIENT_CONTACT_NAME", clientContactName);
        String typeString = "";
        if ("Invoice".equals(type)) {
            typeString = " an invoice statement ";
        } else if ("Quote".equals(type)) {
            typeString = " a sales quote ";
        } else if ("Order".equals(type)) {
            typeString = " a purchase order ";
        } else if ("Sales Order".equals(type)) {
            typeString = " a sales order ";
        } else {
            typeString = " an invoice statement ";
        }
        mapValues.put("TYPE", typeString);
        if (user.getCompany() != null) {
            mapValues.put("DATE", formatDateShort(date, user.getCompany()));
        } else {
            mapValues.put("DATE", defaultShortDateFormat(date));
        }
        String text = invoiceData.getClientMessage();
        text = link != null ? text.replaceAll(EmailTemplateConstants.ET_QUOTE_VIEW_LINK, link) : text;
        mapValues.put("MESSAGE", text);
        String requestText = EdsTemplates.processTemplate(user, mapValues, message);
        String subject = (!"Receipt Invoice".equals(type) ? commonLocalizer.localize(NEW_SUBJECT) + " " : "") + getTypeString(type) + " " + commonLocalizer.localize(FROM) + " " + user.getCompany().getName();
        sendMessageFromUser(fromEmail, user.getEmail(), cc, bcc, subject, requestText, true, replyTo, fileIDs, false, null, null, user);
    }

    public void resendCompanyRegistrationNotification(List<EdsEmployee> adminList, EdsCompany company, String remoteAddr) throws EdsDbException {

        try {
            String adminEmail = null;
            String subject = EdsContextParams.getProductName() + commonLocalizer.localize(EdsSubjects.SIGN_UP);
            Map<String, Object> adminValues = new TreeMap<>();
            String dateFormat = "dd.MM.yyyy";
            SimpleDateFormat df;
            df = new SimpleDateFormat(dateFormat);
            String username = null;
            String password = null;
            String id = null;
            String companyid = null;
            String serviceid = null;
            Date regDate = company.getCreationTime();
            String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(regDate);
            adminValues.put("HOST", EdsContextParams.getHost(company.getObjectID()));
            String supportEmail = EdsContextParams.getSupportEmail();
            for (EdsEmployee admin : adminList) {
                adminEmail = admin.getEmail();
                id = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(admin.getObjectID().toString()));
                companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(company.getObjectID().toString()));
                serviceid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(SpringPropertiesUtil.getProperty("kpi.discovery.service-id")));

                adminValues.put("user", admin);
                adminValues.put("uid", id);
                adminValues.put("companyid", companyid);
                adminValues.put("serviceid", serviceid);

                adminValues.put("regDate", date);


                String adminText = "";
                if (EdsContextParams.getHostname().contains(BRAIN_UZ_DOMAIN) || EdsContextParams.getHostname().contains(BRAIN_UZ2_DOMAIN)) {
                    adminText = EdsTemplates.processTemplate(admin.getCreator(), adminValues, EdsTemplates.RESEND_ACTIVATION_LINK_BRAINUZ);
                } else {
                    adminText = EdsTemplates.processTemplate(admin.getCreator(), adminValues, EdsTemplates.RESEND_SIGN_UP_FOR_ADMINS);
                }
                sendMessage(adminEmail, subject, adminText, supportEmail, false, null, null, null);
            }

        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void sendEmployeeActivationMessage(EdsEmployee employee) throws EdsDbException, EdsTemplateException {
        try {
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmployeeActivatedByManagerEmailTemplate(employee);
            if (templateItem != null) {
                sendMessage(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), employee.getCreator().getEmail(), false, null, null, null);
            } else {
                String subject = commonLocalizer.localize(EdsSubjects.EMPLOYEE_ACTIVATION_NOTIFICATION);
                Map<String, Object> requestValues = new TreeMap<>();
                String fullName = employee.getFullName();
                String userName = employee.getUserName();
                String employeeEmail = employee.getEmail();
                requestValues.put("HOST", EdsContextParams.getHost(employee.getCompany().getObjectID()));
                requestValues.put("userName", userName);
                requestValues.put("name", fullName);
                String empText = EdsTemplates.processTemplate(employee.getCreator(), requestValues, EdsTemplates.EMPLOYEE_ACTIVATE);
                sendMessage(employeeEmail, subject, empText, EdsContextParams.getSupportEmail(), false, null, null, null);
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void resendEmployeesRegistrationNotification(List<EdsEmployee> employeesList, EdsEmployee admin) throws EdsDbException, EdsTemplateException {

        String receiverEmail = null;
        String subject = admin.getName() + " " + commonLocalizer.localize(USER_SENT_INVITATION);
        Map<String, Object> requestValues = new TreeMap<>();
        Calendar calendar = null;
        String dateFormat = "dd.MM.yyyy";
        SimpleDateFormat df;
        df = new SimpleDateFormat(dateFormat);
        String username = null;
        String password = null;
        String id = null;
        String companyid = null;
        try {
            for (EdsEmployee employee : employeesList) {
                receiverEmail = employee.getEmail();
                EdsCompany company = employee.getCompany();
                if (calendar == null) {
                    calendar = new GregorianCalendar(TimeZone.getTimeZone(company.getCountryZone().getZone().getZoneID()));
                }
                id = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(employee.getObjectID().toString()));
                companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(company.getObjectID().toString()));

                requestValues.put("host", EdsContextParams.getHost(company.getObjectID()));
                requestValues.put("logo", EdsContextParams.getLogoWithHost(company.getObjectID()));
                if (admin != null) {
                    requestValues.put("TIME", formatDate(calendar.getTime(), admin.getCompany()));
                } else {
                    requestValues.put("TIME", defaultLongDateFormat(calendar.getTime()));
                }
                requestValues.put("admin", admin);
                requestValues.put("user", employee);
                requestValues.put("companyid", companyid);
                requestValues.put("uid", id);
                final String keyValue = this.activationLinkManager.getOrCreate(company.getObjectID(), employee.getObjectID(), null);

                requestValues.put("keyValue", keyValue);
                requestValues.put("currentYear", LocalDateTime.now().getYear());

                String displaySubject = admin.getName() + " " + commonLocalizer.localize(CREATED_AN_ACCOUNT_FOR_YOU_AT) + " " + EdsContextParams.getProductName();
                String requestText = EdsTemplates.processTemplate(employee.getCreator(), requestValues, EdsTemplates.EMPLOYEE_ADD);
                sendMessage(receiverEmail, subject, requestText, admin.getEmail(), false, null, displaySubject, company.getObjectID());
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    @Transactional
    public void sendForgotPasswordNotification(EdsUser user, Map<Boolean, CompanyDomain> isKpi) throws EdsDbException {
        try {
            if (EMPLOYEE_STATUS_INACTIVE.equals(user.getAccountStatus().getCode()) || EMPLOYEE_STATUS_PENDING.equals(user.getAccountStatus().getCode())) {
                sendForgotWithActionLink(user);
            } else if (EMPLOYEE_STATUS_ACTIVE.equals(user.getAccountStatus().getCode())) {
                String subject = commonLocalizer.localize(PASSWORD_RESET);
                EdsHostBasedSetting hostBasedSetting = hostBasedSettingManager.getLinksByHostName(EdsContextParams.getHostname());
                Map<String, Object> requestValues = new TreeMap<>();

                List<UserCompanyDTO> companyList = globalAuthJdbcSpringManager.getUserCompanyByEmail(null, user.getUserName());
                StringBuilder companyIds = new StringBuilder();
                for (int in = 0; in < companyList.size(); in++) {
                    companyIds.append(companyList.get(in).getCompanyID());
                    if (in < companyList.size() - 1) {
                        companyIds.append(",");
                    }
                }

                String uid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(user.getObjectID().toString()));
                String companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(user.getCompany().getObjectID().toString()));
                String serviceid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(SpringPropertiesUtil.getProperty("kpi.discovery.service-id")));

                requestValues.put("fullname", user.getFullName());
                requestValues.put("username", user.getUserName());
                requestValues.put("uid", uid);
                requestValues.put("companyid", companyid);
                requestValues.put("serviceid", serviceid);
                requestValues.put("currentYear", LocalDateTime.now().getYear());
                requestValues.put("advancedPassEnabled", globalAuthJdbcSpringManager.isEnabledAdvancedPassword(companyIds.toString()));
                requestValues.put("email", hostBasedSetting.getEmail());
                requestValues.put("description", hostBasedSetting.getDescription());
                requestValues.put("productName", hostBasedSetting.getProductName());
                requestValues.put("website", hostBasedSetting.getWebsite());




                String requestText = null;
                for (Map.Entry<Boolean, CompanyDomain> entry : isKpi.entrySet()) {
                    if (entry.getKey().equals(true)) {
                        requestValues.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
                        requestValues.put("logo", EdsContextParams.getLogoWithHost(user.getCompany().getObjectID()));
                        if (EdsContextParams.getHostname().contains(BRAIN_UZ_DOMAIN) || EdsContextParams.getHostname().contains(BRAIN_UZ2_DOMAIN)) {
                            requestText = EdsTemplates.processTemplate(user, requestValues, EdsTemplates.FORGOT_PASSWORD_BRAINUZ);
                        }else {
                            requestText = EdsTemplates.processTemplate(user, requestValues, EdsTemplates.FORGOT_PASSWORD);
                        }
                    } else {
                        requestValues.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
                        if (entry.getValue() != null) {
                            requestValues.put("host", entry.getValue().getDomain());
                        }
                        requestText = EdsTemplates.processTemplate(user, requestValues, EdsTemplates.WFP_FORGOT_PASSWORD);
                    }

                }
                sendMessageFromUser(Constants.defaultSupportEmail, user.getEmail(), null, null, subject, requestText, false, EdsContextParams.getSupportEmail(), null, false, null, null, null, false, true);
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    private void sendForgotWithActionLink(EdsUser user) throws EdsDbException, EdsTemplateException {
        try {
            String subject = commonLocalizer.localize(EMPLOYEE_ADD_NOTIFICATION);
            Map<String, Object> adminValues = new TreeMap<>();
            EdsCompany company = user.getCompany();
            String uid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(user.getObjectID().toString()));
            String companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(company.getObjectID().toString()));
            String serviceid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(SpringPropertiesUtil.getProperty("kpi.discovery.service-id")));

            adminValues.put("host", EdsContextParams.getHost(company.getObjectID()));
            adminValues.put("logo", EdsContextParams.getLogoWithHost(company.getObjectID()));
            adminValues.put("user", user);
            adminValues.put("uid", uid);
            adminValues.put("companyid", companyid);
            adminValues.put("serviceid", serviceid);
            adminValues.put("currentYear", LocalDateTime.now().getYear());

            String requestText = "";
            if (EdsContextParams.getHostname().contains(BRAIN_UZ_DOMAIN) || EdsContextParams.getHostname().contains(BRAIN_UZ2_DOMAIN)) {
                requestText = EdsTemplates.processTemplateWhiteLabel(user, adminValues, EdsTemplates.FORGOT_WITH_ACTIVATION_LINK_BRAINUZ);
            } else {
                EdsTemplates.processTemplate(user, adminValues, EdsTemplates.FORGOT_WITH_ACTIVATION_LINK);
            }

            sendMessageFromUser(null, user.getEmail(), null, null, subject, requestText, false, EdsContextParams.getSupportEmail(), null, false, null, null, null, false, true);
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    private static final Integer DAILY = 1;
    private static final Integer WEEKLY = 2;
    private static final Integer MONTHLY = 3;
    private static final Integer PM = 1;
    private static final Integer BM = 2;
    private static final Integer CL = 3;

    public void sendBugReport(EdsUser user, String reportText, String subjectText, String viewSection, Date creationTime, Boolean hasAttachment, List<Integer> fileIDs) throws EdsDbException, EdsTemplateException {
        String to = "";
        String uncaught = reportText.toLowerCase();

        if (uncaught.contains("uncaughtexception") && uncaught.contains("implicit")) {
            to = "dev@workforcetrack.com";
        } else {
            to = EdsContextParams.getSupportEmail();//support@kpi.com
        }

        String subject = commonLocalizer.localize(EdsSubjects.BUG_REPORT_TITLE);
        if (subjectText != null && !"".equals(subjectText))
            subject = subjectText;

        Map<String, Object> values = new TreeMap<>();
        EdsCompany company = user.getCompany();
        values.put("company", (company.getName() + "(CompanyID=" + company.getObjectID() + ")"));
        values.put("host", EdsContextParams.getHost());
        values.put("userName", user.getName());
        values.put("userEmail", user.getEmail());
        values.put("reportText", reportText);
        if (user != null) {
            values.put("date", formatDate(creationTime, company));
        } else {
            values.put("date", defaultLongDateFormat(creationTime));
        }
        values.put("section", viewSection);
        values.put("keywords", extractKeys(reportText, viewSection));
        String text = EdsTemplates.processTemplate(user, values, EdsTemplates.SEND_BUG_REPORT);
        registerInternalMessage(to, subject, text, user.getEmail(), hasAttachment, fileIDs, false, null, company.getObjectID());
    }

    private String extractKeys(String... words) {
        String delimitr = ",";
        StringBuilder result = new StringBuilder();
        if (words != null && words.length > 0) {
            boolean first = true;
            for (String word : words) {
                if (word != null && !"".equals(word)) {
                    for (String w : word.split("[\\s,]")) {
                        if (w != null && !"".equals(w) && Constants.KEY_WORDS.contains("," + w.toLowerCase() + ",")) {
                            result.append(!first ? delimitr : "").append(w);
                            first = false;
                        }
                    }
                }
            }
        }
        return result.toString();
    }

    public void sendBugReportChangeNotification(EdsBugReport bugReport, boolean bugStatusChanged) throws EdsDbException {
        try {
            String to = "dev@workforcetrack.com";
            String subject = "";
            if (bugStatusChanged) {
                subject = commonLocalizer.localize(EdsSubjects.BUG_STATUS_CHANGED);
            } else {
                subject = commonLocalizer.localize(EdsSubjects.BUG_REPORT_TITLE);
            }

            Map<String, Object> values = new TreeMap<>();
            values.put("report", bugReport);
            if (bugReport.getCompany() != null) {
                EdsCompany company = companyManager.get(bugReport.getCompany());
                values.put("date", (company != null ? formatDate(bugReport.getCreationTime(), company) : defaultLongDateFormat(bugReport.getCreationTime())));
                values.put("companyName", bugReport.getCompanyName());
                EdsUser creator = userManager.getUserInSchema(bugReport.getCreator(), bugReport.getCompany() + "");
                values.put("name", (creator != null ? creator.getName() : bugReport.getCreatorName()));
            } else {
                values.put("date", defaultLongDateFormat(bugReport.getCreationTime()));
                values.put("companyName", "N/A");
                values.put("name", "N/A");
            }
            EdsUser fromUser = userManager.getUserInSchema(bugReport.getCreator(), bugReport.getCompany() + "");

            String text = EdsTemplates.processTemplate(fromUser, values, EdsTemplates.SEND_BUG_REPORT_CHANGED);
            sendMessage(to, subject, text, null, false, null, null, null);

        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }

    }

    public void sendDailyNonActivateLinksNotification() throws EdsDbException {
        createPendingUsers();

        Date date = new Date();
        date.setDate(date.getDate() - 1);
        List<EdsUserActivation> users = userActivationManager.getNonMailSentUsers(date);

        String support = "nilufar.nurmanova@workforcetrack.com";

        try {
            for (EdsUserActivation u : users) {
                EdsUser employee = u.getUser();
                if (employee != null) {
                    EdsCompany company = employee.getCompany();
                    if (!company.getTestCompany()) {
                        if (employee.isEmployee()) {
                            if ((employee.hasRole(roleManager.get(EdsRole.ADMIN)))) {
//                                sendCompanyRegistrationNotificationByOtherSupport(employee, employee.getCompany(), "remote address", true, support, true, true);
                                updateSentCount(u);
                            } else {
                                sendEmployeeAddNotificationByOtherSupport(getEmployee(employee), getEmployee(employee).getCreator(), true, support);
                                updateSentCount(u);
                            }
                        } else if (employee.isClientContact()) {
                            sendToClientByOtherSupport(clientContactManager.get(employee.getObjectID()), null, true, support, false);
                            updateSentCount(u);
                        }
                    }
                }
            }
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    @Transactional
    public void updateSentCount(EdsUserActivation u) {
        u.setSentCount(1);
        userActivationManager.update(u);
    }

    @Transactional
    public void createPendingUsers() {
        List<EdsUser> users = userActivationManager.getPandingUsers();
        for (EdsUser user : users) {

            EdsUserActivation userActivation = new EdsUserActivation();
            userActivation.setUser(user);
            userActivation.setType(referenceManager.findReference(EdsUserActivation._SENT_TYPE, EdsUserActivation.ACTIVATION));
            EdsEmployee employee = getEmployee(user);
            if (employee != null && employee.getCreationTime() != null) {
                userActivation.setLastSentDate(getEmployee(user).getCreationTime());
            } else {
                userActivation.setLastSentDate(new Date());
            }
            userActivationManager.create(userActivation);
        }
    }

    private EdsEmployee getEmployee(EdsUser user) {
        return employeeManager.get(user.getObjectID());
    }

    public static String PEER = "Peer";
    public static String MANAGER = "Manager";
    public static String CLIENT = "Client";

    public static class CollaboratorEmployee {

        private String employee;
        private String role;

        public CollaboratorEmployee(String employee, String role) {
            this.employee = employee;
            this.role = role;
        }

        public String getEmployee() {
            return employee;
        }

        public void setEmployee(String employee) {
            this.employee = employee;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    public void sendTimeSheetForApprovalToManager(EdsTimeSheetApprovalSession timeSheetApproval, EdsEmployee manager) throws EdsDbException {
        boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(
                manager.getObjectID(), EmailNotificationConstants.TIMESHEET_FOR_APPROVAL_TO_MANAGER_NOTIFICATION);
        if (emailNotificationSettings) {
            String subject = timeSheetApproval.getEmployee().getName() + " " + commonLocalizer.localize(SUBMITTED_TIMESHEER_FOR_APPROVAL);
            Map<String, Object> values = new TreeMap<>();
            values.put("host", EdsContextParams.getHost(manager.getCompany().getObjectID()));
            values.put("timesheetapprovalsession", timeSheetApproval);
            values.put("managername", manager.getName());
            if (manager != null) {
                values.put("startdate", formatDateShort(timeSheetApproval.getStartDate(), manager.getCompany()));
                values.put("enddate", formatDateShort(timeSheetApproval.getEndDate(), manager.getCompany()));
            } else {
                values.put("startdate", defaultShortDateFormat(timeSheetApproval.getStartDate()));
                values.put("enddate", defaultShortDateFormat(timeSheetApproval.getEndDate()));
            }
            String companyid = EncryptionHelper.encryptURL(manager.getCompany().getObjectID().toString());
            values.put("link", EncryptionHelper.encryptURL(MYWORKSPACE + "|" + TIMESHEET_APPROVAL_LIST) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(manager.getObjectID().toString()) + "&" + C_ID + "=" + companyid);
            try {
                String text = EdsTemplates.processTemplate(manager.getCreator(), values, EdsTemplates.TIMESHEET_APPROVAL_FOR_MANAGER);
                String to = manager.getEmail();
                registerInternalMessageBasic(to, subject, text, manager.getCompany().getObjectID());
            } catch (EdsTemplateException e) {
                throw new EdsDbException(e);
            }
        }
    }


    public void sendTimeSheetProceededNotification(EdsTimeSheetApprovalSession timeSheetApproval, Set<EdsTimeSheet> rejectedEntries, EdsUser manager) throws EdsDbException {
        EdsUser edsUser = timeSheetApproval.getEmployee();
        boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(
                edsUser.getObjectID(), EmailNotificationConstants.TIMESHEET_FOR_APPROVAL_TO_USER_NOTIFICATION);
        if (emailNotificationSettings) {
            String subject = manager.getName() + " " + commonLocalizer.localize(REVIEWED_YOUR_TIMESHEET_ENTIRIS, "reviewed your timesheet entries");
            String to;

            Map<String, Object> values = new TreeMap<>();
            values.put("productName", EdsContextParams.getProductName());
            values.put("timesheetapprovalsession", timeSheetApproval);
            values.put("managername", manager.getName());

            List<TimeSheetItem> approved = new ArrayList<>();
            List<TimeSheetItem> rejected = new ArrayList<>();
            int i = 1;
            int j = 1;
            Set<EdsTimeSheet> treeSet = new TreeSet(new MessageManagerImpl());
            treeSet.addAll(timeSheetApproval.getTimeentries());
            for (EdsTimeSheet ts : treeSet) {
                if (ts.getStatus() != null) {
                    TimeSheetItem ti = new TimeSheetItem();
                    if (manager != null) {
                        ti.setDate(formatDateShort(ts.getDate(), manager.getCompany()));
                    } else {
                        ti.setDate(defaultShortDateFormat(ts.getDate()));
                    }
                    ti.setComment(ts.getManagerComment());
                    ti.setTaskName(ts.getEmployeeTask().getTask().getName());
                    ti.setTimespent(ts.getTimeSpentHM());
                    ti.setNumber(j + "");
                    j++;
                    approved.add(ti);
                }
            }
            for (EdsTimeSheet ts : rejectedEntries) {
                if (ts.getStatus() != null) {
                    TimeSheetItem ti = new TimeSheetItem();
                    if (manager != null) {
                        ti.setDate(formatDateShort(ts.getDate(), manager.getCompany()));
                    } else {
                        ti.setDate(defaultShortDateFormat(ts.getDate()));
                    }
                    ti.setComment(ts.getManagerComment());
                    ti.setTaskName(ts.getEmployeeTask().getTask().getName());
                    ti.setTimespent(ts.getTimeSpentHM());
                    ti.setNumber(i + "");
                    i++;
                    rejected.add(ti);
                }
            }
            values.put("approved", approved);
            values.put("rejected", rejected);
            try {
                String text = EdsTemplates.processTemplate(values, EdsTemplates.TIMESHEET_PROCEEDED);
                to = timeSheetApproval.getEmployee().getEmail();

                registerInternalMessageBasic(to, subject, text, manager.getCompany().getObjectID());

            } catch (EdsTemplateException e) {
                throw new EdsDbException(e);
            }
        }
    }

    public void sendOverdueInvoiceReminder(InvoiceList data, EdsUser user, String baseCurrency) throws EdsDbException {
        if (data != null && data.getList() != null && !data.getList().isEmpty()) {
            String subject = commonLocalizer.localize(INVOICE_OVERDUE_REMINDER);
            String to;
            String companyId = EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
            Map<String, Object> values = new TreeMap<>();
            String host = "";
            host = EdsContextParams.getHost(user.getCompany().getObjectID());
            values.put("host", host);
            values.put("username", user.getFullName());
            values.put("baseCurrencyName", baseCurrency);

            Integer calculationScale = 2;
            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            if (fs != null && fs.getCalculationScale() != null) {
                calculationScale = fs.getCalculationScale();
            }

            DecimalFormat decimalFormat = ServerUtils.getDecimalFormat(calculationScale);

            List<NewInvoice> invoices = new ArrayList<>();
            int j = 1;
            for (NewInvoice invoice : data.getList()) {
                if (!invoice.isCreditNote()) {
                    invoice.setInvoiceDateAsString(formatDateShort(invoice.getInvoiceDate().getNonConvertedDate(), user.getCompany()));
                    invoice.setDueDateAsString(formatDateShort(invoice.getDueDate().getNonConvertedDate(), user.getCompany()));

                    if (invoice.getCurrencySymbol() != null) {
                        invoice.setCurrencySymbol(invoice.getCurrencySymbol());
                    } else {
                        invoice.setCurrencySymbol("");
                    }

                    BigDecimal exchangeRate = invoice.getExchageRate() != null && invoice.getExchageRate().compareTo(BigDecimal.ZERO) != 0 ? invoice.getExchageRate() : new BigDecimal("1.00");
                    BigDecimal totalAmount = BigDecimal.ZERO;
                    BigDecimal totalInInvCurrencyAmount = BigDecimal.ZERO;
                    if (invoice.getTotalInInvoiceCurrency() != null && invoice.getTotalInInvoiceCurrency().compareTo(BigDecimal.ZERO) != 0) {
                        totalInInvCurrencyAmount = invoice.getTotalInInvoiceCurrency();
                    } else if (invoice.getTotal() != null) {
                        totalAmount = invoice.getTotal().multiply(exchangeRate);
                    }

                    BigDecimal paidAmount = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO;

                    String currency = invoice.getCurrencySymbol() != null && !"".equals(invoice.getCurrencySymbol()) ? invoice.getCurrencySymbol() : invoice.getCurrencyName();

                    invoice.setTotalAsString(totalAmount != null ? decimalFormat.format(totalAmount) + (baseCurrency != null ? " " + baseCurrency : "") : "0.00");
                    invoice.setTotalInInvoiceAsString(totalInInvCurrencyAmount != null ? decimalFormat.format(totalInInvCurrencyAmount) + (currency != null ? " " + currency : "") : "0.00");
                    invoice.setPaymentsAsString(paidAmount != null ? decimalFormat.format(paidAmount) + (currency != null ? " " + currency : "") : "0.00");
                    invoice.setInvoiceURL(EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("saleinvoice|summary/" + invoice.getID())) + "&cid=" + companyId);
                    invoices.add(invoice);
                    log.info(NotificationTypeEnum.InvoiceDueReminder.name() + ", Username:" + user.getFullName() + "," + user.getDeviceToken() + "," + user.getDeviceType());
                    notificationMsgManager.createInvoiceOverDueNotificationEvent(invoice.getID(), user, ActionOnEntityEnum.INVOICE_REMINDER);
                }
            }

            values.put("invoices", invoices);
            values.put("link", (EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("accounting|saleinvoice")) + "&cid=" + companyId));
            try {
                String text = EdsTemplates.processTemplate(user, values, EdsTemplates.OVERDUE_INVOICES_FOR_CRON);
                to = user.getEmail();
                registerInternalMessageBasic(to, subject, text, user.getCompany().getObjectID());

            } catch (EdsTemplateException e) {
                throw new EdsDbException(e);
            }
        }
    }

    public void sendOverdueInvoiceReminderForEveryClient(Map<String, List<NewInvoice>> datas, EdsUser user) throws EdsDbException {

        Integer calculationScale = 2;
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        if (fs != null && fs.getCalculationScale() != null) {
            calculationScale = fs.getCalculationScale();
        }
        DecimalFormat decimalFormat = ServerUtils.getDecimalFormat(calculationScale);

        for (Map.Entry<String, List<NewInvoice>> entry : datas.entrySet()) {
            String subject = commonLocalizer.localize(INVOICE_OVERDUE_REMINDER);
            String to = entry.getKey();
            Map<String, Object> values = new TreeMap<>();
            SimpleDateFormat df = new SimpleDateFormat("MMM d", Locale.US);
            String host = "";
            host = EdsContextParams.getHost(user.getCompany().getObjectID());

            values.put("host", host);
            values.put("username", entry.getValue().get(0).getClientName());
            values.put("company", user.getCompany().getName());

            List<NewInvoice> invoices_ = entry.getValue();
            List<NewInvoice> invoices = new ArrayList<>();
            int j = 1;
            for (NewInvoice invoice : invoices_) {
                if (user != null) {
                    invoice.setInvoiceDateAsString(formatDateShort(invoice.getInvoiceDate().getNonConvertedDate(), user.getCompany()));
                    invoice.setDueDateAsString(formatDateShort(invoice.getDueDate().getNonConvertedDate(), user.getCompany()));
                } else {
                    invoice.setInvoiceDateAsString(defaultShortDateFormat(invoice.getInvoiceDate().getNonConvertedDate()));
                    invoice.setDueDateAsString(defaultShortDateFormat(invoice.getDueDate().getNonConvertedDate()));
                }
                if (invoice.getCurrencySymbol() != null) {
                    invoice.setCurrencySymbol(invoice.getCurrencySymbol());
                } else {
                    invoice.setCurrencySymbol("");
                }
                invoice.setTotalInInvoiceAsString(decimalFormat.format(invoice.getTotalInInvoiceCurrency() != null ? invoice.getTotalInInvoiceCurrency() : BigDecimal.ZERO) + " " + invoice.getCurrencyName());
                invoice.setPaymentsAsString(decimalFormat.format(invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO) + " " + invoice.getCurrencyName());
                invoices.add(invoice);
            }

            values.put("invoices", invoices);
            try {
                String text = EdsTemplates.processTemplate(user, values, EdsTemplates.OVERDUE_INVOICES_FOR_CRON_FOR_CLIENT);
                if (invoices != null && invoices.size() > 0) {
                    Integer clientID = invoices.get(0).getClientID();
                    if (clientID != null) {
                        List<EdsCrmContact> contacts = crmContactManager.getContactsByCrmAccount(clientID);
                        if (contacts != null && contacts.size() > 0) {
                            for (EdsCrmContact contact : contacts) {
                                if (contact.getPrimaryContact()) {
                                    to = contact.getPrimaryEmail();
                                }
                            }
                        }
                    }
                }
                registerInternalMessageBasic(to, subject, text, user.getCompany().getObjectID());

            } catch (EdsTemplateException e) {
                throw new EdsDbException(e);
            }
        }
    }

    public void sendTimesheetReminder(Map<String, List<TimesheetItem>> data, EdsUser user, Date startDate, Date endDate) throws EdsDbException {
        if (data != null && !data.isEmpty()) {
            String subject = commonLocalizer.localize(TIMESHEET_REMINDER);
            Map<String, Object> values = new TreeMap<>();
            values.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
            values.put("username", user.getFullName());
            values.put("timesheets", data);
            if (DateUtil.countDays(startDate, endDate) == 1) {
                if (user != null) {
                    values.put("time", formatDateShort(startDate, user.getCompany()));
                } else {
                    values.put("time", defaultShortDateFormat(startDate));
                }
            } else {
                if (user != null) {
                    values.put("time", formatDateShort(startDate, user.getCompany()) + " - " + formatDateShort(endDate, user.getCompany()));
                } else {
                    values.put("time", defaultShortDateFormat(startDate) + " - " + defaultShortDateFormat(endDate));
                }
            }
            values.put("year", 1900 + endDate.getYear());
            int daysCount = DateUtil.countDays(startDate, endDate);
            String[] days = new String[daysCount];
            if (user != null) {
                for (int i = 0;
                     i < daysCount;
                     i++) {
                    days[i] = formatDateShort(DateUtil.addDays(startDate, i), user.getCompany());
                }
            } else {
                for (int i = 0;
                     i < daysCount;
                     i++) {
                    days[i] = defaultShortDateFormat(DateUtil.addDays(startDate, i));
                }
            }

            values.put("days", days);
            values.put("isWeeklyOrMonthly", (days.length > 1 ? "true" : "false"));
            String companyId = EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
            values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("dashboard|customreport")) + "&cid=" + companyId);
            try {
                String text = EdsTemplates.processTemplate(user, values, EdsTemplates.TIMESHEET_REMINDER);
                String to = user.getEmail();
                if (to != null && !"".equals(to.trim())) {
                    sendMessage(to, subject, text, null, false, null, null, null);
                }
            } catch (EdsTemplateException e) {
                throw new EdsDbException(e);
            }
        }
    }

    /**
     * Sender Event Reminder
     *
     * @param employeeEvent
     */

    public void sendEventReminder(EdsEmployeeEvent employeeEvent) throws EdsDbException {
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generatedEmailTemplateItems(employeeEvent, null, CALENDAR_EVENT_REMINDER_CATEGORY);
        if (templateItem != null) {
            registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), employeeEvent.getEmployee().getCompany().getObjectID());
        } else {
            if (StringUtils.isNotEmpty(employeeEvent.getEmployee().getEmail())) {
                EdsEmailSetting companyEmailSetting = emailSettingsManager.getCompanyEmailSetting(employeeEvent.getEmployee().getCompany().getObjectID());
                EdsUser owner = employeeEvent.getEmployee();
                EdsEvent event = employeeEvent.getEvent();
                String subject = commonLocalizer.localize(EVENT_REMINDER) + ": " + employeeEvent.getEvent().getSubject() + " - " + getDate(employeeEvent.getEvent(), owner);
                Map<String, Object> values = new TreeMap<>();
                EdsEmployee employee = employeeManager.get(employeeEvent.getEmployee().getObjectID());
                values.put("CREATOR", employee);
                values.put("EVENT_NAME", employeeEvent.getEvent().getSubject());
                values.put("DATE", getDate(employeeEvent.getEvent(), owner));
                values.put("WHERE", getLocation(employeeEvent.getEvent().getVenue()));
                values.put("DESCRIPTION", getDescription(employeeEvent.getEvent().getDescription()));
                String guests = getEventGuests(event);
                values.put("GUESTS", !"".equals(guests) ? "<p>" + commonLocalizer.localize("guests") + " : " + guests + "</p>" : "");
                values.put("LINK", EdsContextParams.getHost(owner.getCompany().getObjectID()) + "/Crm.html#event|summary/" + employeeEvent.getEvent().getObjectID().toString());

                try {
                    String text = EdsTemplates.processTemplate(owner, values, EdsTemplates.CALENDAR_ADD_EVENT_REMINDER);
                    registerInternalMessageBasic(companyEmailSetting.getEmail(), owner.getEmail(), subject, text, employeeEvent.getEmployee().getCompany().getObjectID());
                } catch (EdsTemplateException e) {
                    throw new EdsDbException(e);
                }
            }
        }
    }

    @Override
    public void sendEventReminderSms(EdsEmployeeEvent employeeEvent) {
        EdsSmsSendItem smsSendItem = new EdsSmsSendItem();
        if (employeeEvent.getEmployee().getEmployee() != null && employeeEvent.getEmployee().getEmployee().getContact() != null && employeeEvent.getEmployee().getEmployee().getContact().getPrimaryPhone() != null) {
            smsSendItem.setUserID(employeeEvent.getEmployee().getObjectID());
            smsSendItem.setToNumber(employeeEvent.getEmployee().getEmployee().getContact().getPrimaryPhone());
            smsSendItem.setSentDate(new Date());
            smsSendItem.setMessageText(employeeEvent.getEvent().getSubject() + " starts on " + employeeEvent.getEvent().getStartDate());
            smsSendItem.setEntityID(employeeEvent.getObjectID());
            smsSendItem.setProvider(smsManager.getDefault());
            generateAndSendSms(smsSendItem);
        }
    }

    @Override
    public void sendEventReminderNotification(EdsEmployeeEvent employeeEvent) {
        notificationMsgManager.updateClickedNotificationEvent(employeeEvent.getObjectID(), NotificationTypeEnum.GoogleCalendarEvent, ActionOnEntityEnum.EVENT);
        notificationMsgManager.createEventNotification(ActionOnEntityEnum.EVENT, employeeEvent);
        WebSocketServerObject message = new WebSocketServerObject();
        message.setEventType(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT);
        Integer userId = employeeEvent.getEmployee().getObjectID();
        message.setUserId(userId);
        rabbitMQService.sendWebPushNotification(message);
    }

    /**
     * Sender Event Reminder
     *
     * @param creator
     * @param meetingMinutes
     * @param attendeesEmployee
     */

    public void sendMeetingMinutesNotification(EdsUser creator, EdsMeetingMinutes meetingMinutes, EdsUser attendeesEmployee, String attende) throws EdsDbException {
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateItemForMeetingMinutesNotification(creator, meetingMinutes, attendeesEmployee, attende);
        if (templateItem != null) {
            registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), creator.getCompany().getObjectID());
            if (attendeesEmployee != null) {
                notificationMsgManager.createMeetingMinutesNotification(meetingMinutes, creator, attendeesEmployee);
            }
        } else {
            String subject = meetingMinutes.getTitle() + " meeting notification";
            String to = attendeesEmployee.getEmail();
            Map<String, Object> values = new TreeMap<>();
            values.put("EMPLOYEE", attendeesEmployee.getFullName());
            values.put("DESCRIPTION", commonLocalizer.localize(MEETING_MINUTES_ATTENDEESEMPLOYEE) + " " + "<b>" + creator.getFullName() + "</b> " +
                    commonLocalizer.localize(MEETING_MINUTES_ADDED_TO_MEETING) + " " + meetingMinutes.getTitle() + " " +
                    commonLocalizer.localize(EdsSubjects.MEETING_MINUTES));
            values.put("MEETING_DETAILS", "<b>" + commonLocalizer.localize(MEETING_MINUTES_DETAILS) + ":</b>");
            values.put("MEETING_ID", meetingMinutes.getMeetingNumber());
            values.put("TITLE", meetingMinutes.getTitle());
            values.put("MEETING_PURPOSE", meetingMinutes.getPurpose());
            values.put("LOCATION", getLocation(meetingMinutes.getLocation()));
            values.put("START_DATE", meetingMinutes.getStartDate() != null ? formatDate(creator.getUserDate(meetingMinutes.getStartDate()), creator.getCompany()) : "");
            values.put("END_DATE", meetingMinutes.getDueDate() != null ? formatDate(creator.getUserDate(meetingMinutes.getDueDate()), creator.getCompany()) : "");
            String hostName = EdsContextParams.getHost(attendeesEmployee.getCompany().getObjectID());
            String companyid = EncryptionHelper.encryptURL(attendeesEmployee.getCompany().getObjectID().toString());
            values.put("LINK", hostName + "/Hrms.html#meetingMinutes|summary/" + meetingMinutes.getObjectID().toString());

            try {
                String text = EdsTemplates.processTemplate(attendeesEmployee, values, EdsTemplates.MEETING_MINUTES);
                if (to != null && !"".equals(to.trim()) && !attendeesEmployee.getDeleted()) {
                    registerInternalMessageBasic(to, subject, text, attendeesEmployee.getCompany().getObjectID());
                    if (attendeesEmployee != null) {
                        notificationMsgManager.createMeetingMinutesNotification(meetingMinutes, creator, attendeesEmployee);
                    }

                }
            } catch (EdsTemplateException e) {
                throw new EdsDbException(e);
            }
        }
    }

    @Override
    public void sendContractOverDueReminder(EdsContract contract, EdsEmployee employee, EmailTemplateItem emailTemplaeItem) {
        String pojectName = "";
        System.out.println("Sending message to: " + employee.getFullName());

        String to = employee.getEmail();
        if (emailTemplaeItem != null) {
            try {
                registerInternalMessageBasic(emailTemplaeItem.getToEmail(), emailTemplaeItem.getSubject(), emailTemplaeItem.getMessageHTML(), contract.getCreator().getCompany().getObjectID());
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendTaskOverDueReminder(EdsTask task, EdsEmployeeTask employeeTask, EmailTemplateItem templateItem) {
        if (!employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getDeleted()) {
            EdsUser user = task.getCreator();
            try {
                registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), user.getCompany().getObjectID());
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
            EdsEmployee taskUser = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
            log.info(NotificationTypeEnum.TaskDueReminder.name() + ", Username:" + taskUser.getFullName() + "," + taskUser.getDeviceToken() + "," + taskUser.getDeviceType());
            notificationMsgManager.createTaskOverDueDateReminderNotification(task, employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee());
        }
    }
//
//    @Override
//    public void sendPenaltyPromotionNotification(EdsEmployeePenaltiesPromotions penaltyPromotion, String typeCode) throws EdsDbException {
//        EdsUser creator = userManager.get(penaltyPromotion.getCreator_id());
//        EdsUser receiver = userManager.get(penaltyPromotion.getEmployee_id());
//        String pojectName = "";
//        if (penaltyPromotion.getProject_id() != null) {
//            int projectId = penaltyPromotion.getProject_id();
//            EdsProject project = projectManager.get(projectId);
//            pojectName = project.getName() == null ? "" : project.getName(); //project name can be null
//        }
//        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateItemForPenaltyPromotion(creator, receiver, pojectName, penaltyPromotion, typeCode);
//        String to = receiver.getEmail();
//        if (templateItem != null) {
//            registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), creator.getCompany().getObjectID());
//        }
//    }

    /**
     * Sender Event Reminder
     *
     * @param report
     */
    @Transactional
    public void sendReport(EdsReport report, EdsUser user, Integer uploadId) throws EdsDbException, EdsTemplateException {
        List<Integer> fileIDs = new ArrayList<>();
        fileIDs.add(uploadId);
        StringBuilder toEmail = new StringBuilder();
        EmailTemplateItem templateItem = null;
        String subject = "";
        String messageHTML = "";
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String startDate = report.getStartDate();
        String endDate = report.getEndDate();
        String email = user.getEmail();

        log.info("***************** Generating Message to Send ********************");
        if (report.getEmailTemplate() != null) {
            boolean first = true;
            for (EdsUser employee : report.getTargetUsers()) {
                if (first) {
                    toEmail.append(employee.getEmail());
                    first = false;
                } else {
                    toEmail.append(", ").append(employee.getEmail());
                }
                /*if (employee.getObjectID().equals(user.getObjectID())) {
                    firstName = user.getFirstName();
                    lastName = user.getLastName();
                    startDate = report.getStartDate();
                    endDate = report.getEndDate();
                    email = user.getEmail();
                }*/
            }
            log.info("***************** To: " + toEmail + " *************************");
            templateItem = emailTemplateServiceLocal.generatedReportTemplateItems(report, user, REPORT_REMINDER_CATEGORY);
            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateUtils.ET_NAME, report.getName());
            values.put(EmailTemplateUtils.ET_DESCRIPTION, report.getDescription());
            values.put(EmailTemplateUtils.ET_COMPANY_NAME, user.getCompany().getName());
            values.put(EmailTemplateUtils.ET_START_DATE, startDate);
            values.put(EmailTemplateUtils.ET_DUE_DATE, endDate);
            values.put(EmailTemplateUtils.ET_EMAIL, email);
            values.put(EmailTemplateUtils.ET_LAST_NAME, lastName);
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(firstName));
            messageHTML = EdsTemplates.evaluateTemplate(values, templateItem.getMessageHTML());
            subject = EdsTemplates.evaluateTemplate(values, templateItem.getSubject());
        }

        if (toEmail.toString().isEmpty()) {
            log.info("***************** There are no <<to emails>>. So the report email is not sent *************************");
            return;
        }
        if (report.getEmailTemplate() != null) {
            registerInternalMessage(toEmail.toString(), subject, messageHTML, null, true, fileIDs, false, null, user.getCompany().getObjectID());
        } else if (templateItem != null && report.getEmailTemplate() == null) {
            registerInternalMessage(toEmail.toString(), subject, messageHTML, null, true, fileIDs, false, null, user.getCompany().getObjectID());
        } else {
            subject = commonLocalizer.localize(SCHEDULED_REPORT) + ": " + report.getName();
            toEmail.append(user.getEmail());
            Map<String, Object> values = new TreeMap<>();
            values.put("REPORT_NAME", report.getName());
            values.put("DESCRIPTION", report.getDescription());

            try {
                log.debug("******************* Sending Email now ********************************");
                String text = EdsTemplates.processTemplate(user, values, EdsTemplates.REPORT_SCHEDULED_REPORT);
                if (!"".equals(toEmail.toString().trim())) {
                    registerInternalMessage(toEmail.toString(), subject, text, null, true, fileIDs, false, null, user.getCompany().getObjectID());
                }
                log.debug("******************** Report sends to Email successfull ******************");
            } catch (EdsTemplateException e) {
                log.debug("*************** Failed send email *******************************");
                e.printStackTrace();
                throw new EdsDbException(e);
            }
        }
    }


    private List<FileResource> getEventAttachments(EdsEvent event, EdsCompany company) {
        EdsFolder eventFolder = folderManager.getFolderByFolderType(EdsFolder.F_EVENT);
        if (eventFolder != null) {
            return attachmentUtilsManager.getAttachments(F_EVENT, eventFolder.getObjectID(), event.getObjectID(), event.getOwner());
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getEventGuests(EdsEvent event) {
        Integer companyID = event.getOwner().getCompany().getObjectID();
        List<EdsGoogleCalendarEventGuests> guests = eventGuestsManager.getEventGuests(event.getObjectID());
        if (guests != null && !guests.isEmpty()) {
            StringBuilder guestString = new StringBuilder();
            for (EdsGoogleCalendarEventGuests guest : guests) {
                if (guestString.length() > 0) {
                    guestString.append(", ");
                }
                guestString.append(getEventGuestName(guest.getEmail(), companyID));
            }
            return guestString.toString();
        }
        return "";
    }

    @Override
    public void sendPeriodAppraisalNotification(EdsAppraisalApproval edsAppraisalApproval) {
        EdsAssessment[] assessment = edsAppraisalApproval.getAssessments().toArray(new EdsAssessment[edsAppraisalApproval.getAssessments().size()]);
        ArrayList<EdsAssessment> assessmentList = new ArrayList<>(Arrays.asList(assessment));
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        String companyid = EncryptionHelper.encryptURL(company.getObjectID().toString());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String subject = null;
        Map<String, Object> values = new TreeMap<>();
        values.put("departmentLeader", edsAppraisalApproval.getDepartment().getLeader().getName());
        values.put("host", EdsContextParams.getHost(company.getObjectID()));
        values.put("assesmentList", assessmentList);
        values.put("dateFormat", dateFormat);
        String statusCode = edsAppraisalApproval.getStatus().getCode();

        if ((!DepartmentPeriodAppraisalItem.PERIOD_SENT_FOR_APPROVAL.equals(statusCode)) && user.hasRole(HR_CODE)) {
            if (DepartmentPeriodAppraisalItem.PERIOD_APPROVED.equals(edsAppraisalApproval.getStatus().getCode())) {
                subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_APPROVE_NOTIFICATION_BY_HR);
            } else if (DepartmentPeriodAppraisalItem.PERIOD_REJECTED.equals(statusCode)) {
                subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_REJECT_NOTIFICATION_BY_HR);
            }
            String link = EncryptionHelper.encodeURL("#" + EncryptionHelper.encryptURL(PA_CONATAINER_NAME + "|" + PA_PERIOD_LIST)) +
                    "&" + U_ID + "=" + EncryptionHelper.encryptURL(user.getObjectID().toString()) + "&" + C_ID + "=" + companyid;
            values.put("link", link);
            values.put("status", edsAppraisalApproval.getStatus().getName());
            values.put("hr", user.getName());
            try {
                String textReviewer = EdsTemplates.processTemplate(user, values, EdsTemplates.ASSESSMENT_APPROVAL_OR_REJECT_NOTIFICATION_BY_HR);
                sendPeriodAppraisalMessage(edsAppraisalApproval.getDepartment().getLeader().getEmail(), subject, textReviewer);
            } catch (EdsTemplateException e) {
                e.printStackTrace();
            }
        } else {
            List<EdsEmployee> hrs = employeeManager.getEmployeeByRole(EdsRole.HR);
            subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_APPROVE_NOTIFICATION_FOR_HR);
            for (EdsEmployee hrManager : hrs) {
                String link = EncryptionHelper.encodeURL("#" + EncryptionHelper.encryptURL(PA_CONATAINER_NAME + "|" + PA_APPROVAL_LIST)) +
                        "&" + U_ID + "=" + EncryptionHelper.encryptURL(hrManager.getObjectID().toString()) + "&" + C_ID + "=" + companyid;
                values.put("hr", hrManager.getName());
                values.put("link", link);
                try {
                    String textReviewer = EdsTemplates.processTemplate(user, values, EdsTemplates.ASSESSMENT_APPROVE_NOTIFICATION_FOR_HR);
                    sendPeriodAppraisalMessage(hrManager.getEmail(), subject, textReviewer);
                } catch (EdsTemplateException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void sendNotificationToHrRecurrently(Map<EdsDepartment, List<EdsEmployee>> data) {
        List<EdsEmployee> hrs = employeeManager.getEmployeeByRole(EdsRole.HR);

        String companyStringID = ServerSecurityContext.getInstance().getCompanyId();
        Integer companyID = Integer.valueOf(companyStringID);
        EdsCompany company = companyManager.get(companyID);

        String encryptedCompanyId = EncryptionHelper.encryptURL(companyStringID);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Object> values = new TreeMap<>();
        values.put("departmentEmployees", data);
        values.put("host", EdsContextParams.getHost(company.getObjectID()));

        String subject = commonLocalizer.localize(EdsSubjects.ASSESSMENT_RECURRENT_NOTIFICATION_FOR_HR);
        for (EdsEmployee hrManager : hrs) {
            String link = EncryptionHelper.encodeURL("#" + EncryptionHelper.encryptURL(PA_CONATAINER_NAME + "|" + PA_ARCHIVE)) +
                    "&" + U_ID + "=" + EncryptionHelper.encryptURL(hrManager.getObjectID().toString()) + "&" + C_ID + "=" + encryptedCompanyId;
            values.put("hr", hrManager.getName());
            values.put("link", link);
            try {
                String textReviewer = EdsTemplates.processTemplate(hrManager, values, EdsTemplates.ASSESSMENT_RECURRENT_NOTIFICATION_FOR_HR);
                sendPeriodAppraisalMessage(hrManager.getEmail(), subject, textReviewer);
            } catch (EdsTemplateException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendEmployeeVisaExpirationDateReminder(Integer recurrenceID, EdsEmployee receiver, EdsEmployee employee, Integer employeeProfileID) throws EdsDbException {
        try {
            String subject = commonLocalizer.localize(EMPLOYEE_VISA_EXPIRATION_DATE);
            String to = receiver.getEmail();
            EdsRecurrence recurrence = recurrenceManager.get(recurrenceID);
            Integer employeeVisaExpirationDateReminderTime = Integer.parseInt(recurrence.getBusObjectParams());

            String reminderTime = "";
            if (employeeVisaExpirationDateReminderTime == 60 * 24) {
                reminderTime = commonLocalizer.localize(PdfLocalizationName.oneDay, "1 day");        //one day    //1
            } else if (employeeVisaExpirationDateReminderTime == 60 * 24 * 2) {
                reminderTime = commonLocalizer.localize(PdfLocalizationName.twoDays, "2 days");      //two days   //2
            } else if (employeeVisaExpirationDateReminderTime == 60 * 24 * 3) {
                reminderTime = commonLocalizer.localize(PdfLocalizationName.threeDays, "3 days");    //three days //3
            } else if (employeeVisaExpirationDateReminderTime == 60 * 24 * 5) {
                reminderTime = commonLocalizer.localize(PdfLocalizationName.fiveDays, "5 days");     //five days  //4
            } else if (employeeVisaExpirationDateReminderTime == 60 * 24 * 7) {
                reminderTime = commonLocalizer.localize(PdfLocalizationName.oneWeek, "1 week");      //one week   //5
            } else if (employeeVisaExpirationDateReminderTime == 60 * 24 * 7 * 2) {
                reminderTime = commonLocalizer.localize(PdfLocalizationName.twoWeeks, "2 weeks");    //two weeks  //6
            } else if (employeeVisaExpirationDateReminderTime == 60 * 24 * 30) {
                reminderTime = commonLocalizer.localize(PdfLocalizationName.oneMonth, "1 Month");    //one month  //7
            } else if (employeeVisaExpirationDateReminderTime == 60 * 24 * 45) {
                reminderTime = "45 days";    //one month  //7
            } else if (employeeVisaExpirationDateReminderTime == 60 * 24 * 60) {
                reminderTime = "2 Month";    //one month  //7
            } else if (employeeVisaExpirationDateReminderTime == 60 * 24 * 90) {
                reminderTime = commonLocalizer.localize(PdfLocalizationName.threeMonth, "3 Month");    //one month  //7
            }

            Map<String, Object> values = new TreeMap<>();
            values.put("overTime", reminderTime);
            values.put("receiverName", receiver.getName());
            values.put("employeeName", employee.getName());

            String text = EdsTemplates.processTemplate(receiver, values, EdsTemplates.EMPLOYEE_VISA_EXPIRATION_DATE_REMINDER);

            registerInternalMessageBasic(to, subject, text, employee.getCompany().getObjectID());
        } catch (EdsTemplateException e) {
            throw new EdsDbException(e);
        }
    }


    private void sendPeriodAppraisalMessage(String to, String subject, String content) {
        EdsMessage message = new EdsMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setType(MessageTypeEnum.PREFERRED);
        create(message);
        try {
            EdsMailer mailer = EdsMailer.getNewInstance(message, wrapEdsCompanySystemSettings(null, null));
            mailer.sendSynchronized();

            log.info("Message sent[TO:" + to + ", SUBJECT:" + subject + "]");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getEventAttendees(EdsEvent event, Integer ownerID) {
        List<EdsUser> attendees = employeeEventManager.getEventAttendees(event);
        if (attendees != null && !attendees.isEmpty()) {
            StringBuilder guestString = new StringBuilder();
            for (EdsUser attendee : attendees) {
                if (!ownerID.equals(attendee.getObjectID())) {
                    if (guestString.length() > 0) {
                        guestString.append(", ");
                    }
                    guestString.append(attendee.getFullName());
                }
            }
            return guestString.toString();
        }
        return "";
    }

    /**
     * Send calendar share event notification
     *
     * @throws EdsTemplateException
     */
    public void sendCalendarShareEventNotification(EdsEmployeeEvent employeeEvent, ArrayList<EdsUser> attendees, boolean onlyShare) throws EdsDbException {
        boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(
                employeeEvent.getEmployee().getObjectID(), EmailNotificationConstants.CALENDAR_SHARE_EVENT_NOTIFICATION);
        if (emailNotificationSettings) {
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generatedEmailTemplateItems(employeeEvent, attendees,
                    ((employeeEvent.isOwner() && !employeeEvent.isShared()) || !onlyShare) ? CALENDAR_EVENT_ADD_CATEGORY : CALENDAR_EVENT_SHARE_CATEGORY);
            if (templateItem != null) {
                registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(),
                        templateItem.getMessageHTML(), employeeEvent.getEmployee().getCompany().getObjectID());
            } else {
                Integer companyID = employeeEvent.getEmployee().getCompany().getObjectID();
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                EdsEmployee employee = employeeManager.get(employeeEvent.getEmployee().getObjectID());
                EdsUser from = employeeManager.getUser();
                EdsUser owner = employeeEvent.getEvent().getOwner();
                String to = employee.getEmail();

                StringBuilder sharedEmployees = new StringBuilder();
                for (EdsUser sharedEmployee : attendees) {
                    if (!sharedEmployee.getDeleted()) {
                        sharedEmployees.append("<b>" + sharedEmployee.getName() + "</b><br>");
                    }
                }
                EdsEvent event = employeeEvent.getEvent();
                String textNotification = "";
                if (Appointment.CALL_LOG == event.getActivityType()) {
                    textNotification = commonLocalizer.localize(CALENDAR_UPDATE_NOTIFICATION);
                } else {
                    textNotification = commonLocalizer.localize(EVENT_NOTIFICATION);
                }

                String subject = commonLocalizer.localize(CALENDAR) + (onlyShare ? " " + commonLocalizer.localize(SHARE) : " " + commonLocalizer.localize(EdsSubjects.ADD)) + " " + textNotification + ": " + employeeEvent.getEvent().getSubject();
                Map<String, Object> values = new TreeMap<>();
                values.put("USER", employee);
                if (owner != null) {
                    values.put("SHARED_DATE", formatDate(employee.getUserDate(employeeEvent.getLastModifiedDate() != null ? employeeEvent.getLastModifiedDate() : new Date()), owner.getCompany()));
                } else {
                    values.put("SHARED_DATE", defaultLongDateFormat(employee.getUserDate(employeeEvent.getLastModifiedDate() != null ? employeeEvent.getLastModifiedDate() : new Date())));
                }
                values.put("EVENT_NAME", employeeEvent.getEvent().getSubject());
                values.put("SHARED_OR_ADDED", (onlyShare ? " shared" : " added"));
                values.put("WHERE", getLocation(employeeEvent.getEvent().getVenue()));
                values.put("DESCRIPTION", getDescription(employeeEvent.getEvent().getDescription()));
                values.put("DATE", getDate(employeeEvent.getEvent(), owner));
                values.put("SHARED_EMPLOYEES", sharedEmployees.toString());
                String guests = getEventGuests(event);
                values.put("GUESTS", !"".equals(guests) ? "<p>Guests: " + guests + "</p>" : "");
                String hostName = EdsContextParams.getHost(companyID);
                StringBuffer link = new StringBuffer(hostName + "/Hrms.html?link=");
                link.append(EncryptionHelper.encryptURL("event/" + event.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString()) + "&" + C_ID + "=" + companyid);
                values.put("LINK", link);

                EdsCompany company = employeeEvent.getEmployee().getCompany();
                List<FileResource> eventAttachments = getEventAttachments(event, company);
                boolean withAttachment = false;
                List<Integer> fileIDs = new ArrayList<>();
                if (eventAttachments != null && !eventAttachments.isEmpty() && event.getIncludeAttachments() != null && event.getIncludeAttachments()) {
                    for (FileResource file : eventAttachments) {
                        fileIDs.add(file.getObjectId());
                    }
                    withAttachment = true;
                }
                if (to != null && !"".equals(to.trim())) {
                    String text = "";
                    try {
                        if (employeeEvent.isOwner() && !employeeEvent.isShared()) {
                            text = EdsTemplates.processTemplate(values, EdsTemplates.CALENDAR_SHARE_EVENT);
                        } else {
                            values.put("CREATOR_NAME", owner.getName());
                            text = EdsTemplates.processTemplate(from, values, EdsTemplates.CALENDAR_SHARE_EVENT_FOR_EMPLOYEES);
                        }
                    } catch (EdsTemplateException e) {
                        e.printStackTrace();
                    }
                    if (!employee.getDeleted()) {
                        registerInternalMessage(to, subject, text, null, withAttachment, fileIDs, true, null, company.getObjectID());
                    }
                }
            }
        }
    }

    private void convertFileIDsToObjects(List<Integer> fileIDs, EdsMessage message) {
        if (fileIDs != null && !fileIDs.isEmpty()) {
            List<EdsUpload> uploads = new ArrayList<>();
            EdsUpload upload = null;
            for (Integer fileID : fileIDs) {
                upload = (EdsUpload) uploadManager.get(fileID);
                uploads.add(upload);
            }
            if (upload != null) {
                message.setFileIDs(uploads);
            }
        }
    }

    /**
     * Send calendar delete event notification
     *
     * @throws EdsTemplateException
     */
    public void sendCalendarDeleteEventNotification(EdsEvent event, EdsUser attendee, boolean deleteWithNotify) throws EdsDbException {
        boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(
                (attendee != null ? attendee.getObjectID() : event.getOwner().getObjectID()), EmailNotificationConstants.CALENDAR_DELETE_EVENT_NOTIFICATION);
        if (emailNotificationSettings) {
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generatedEmailTemplateItems(event, attendee, CALENDAR_EVENT_DELETE_CATEGORY);
            if (templateItem != null) {
                registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(),
                        templateItem.getMessageHTML(), attendee.getCompany().getObjectID());
            } else {
                EdsUser owner = event.getOwner();
                EdsUser from = employeeManager.getUser();
                String subject = commonLocalizer.localize(CALENDAR_DELETE_NOTIFICATION) + ": " + event.getSubject();
                Map<String, Object> values = new TreeMap<>();
                values.put("EVENT_NAME", event.getSubject());
                values.put("DESCRIPTION", getDescription(event.getDescription()));
                values.put("WHERE", getLocation(event.getVenue()));
                values.put("DATE", getDate(event, owner));
                String attendeeList = getEventAttendees(event, owner.getObjectID());
                values.put("SHARED_WITH", !"".equals(attendeeList) ? "<p>" + commonLocalizer.localize("sharedWith") + ": " + attendeeList + "</p>" : "");
                try {
                    String to, text;
                    if (attendee != null) {
                        values.put("USER", attendee.getFullName());
                        values.put("CREATOR_NAME", owner.getName());
                        to = attendee.getEmail();
                        text = EdsTemplates.processTemplate(attendee, values, EdsTemplates.CALENDAR_DELETE_EVENT_FROM_EMPLOYEE);
                    } else {
                        values.put("USER", owner);
                        to = owner.getEmail();
                        text = EdsTemplates.processTemplate(attendee, values, EdsTemplates.CALENDAR_DELETE_EVENT);
                    }
                    registerInternalMessageBasic(to, subject, text, owner.getCompany().getObjectID());
                } catch (EdsTemplateException e) {
                    throw new EdsDbException(e);
                }
            }
        }
    }

    private String getDate(EdsEvent event, EdsUser owner) {
        EdsCompany company = owner.getCompany();
        TimeZone timeZone = owner.getUserTimezone();
        Date start = (Date) event.getStartDate().clone();
        Date end = (Date) event.getEndDate().clone();
        Date startDate = new Date(start.getYear(), start.getMonth(), start.getDate(), start.getHours(), start.getMinutes() + (timeZone.getRawOffset() / 60000), start.getSeconds());
        Date endDate = new Date(end.getYear(), end.getMonth(), end.getDate(), end.getHours(), end.getMinutes() + (timeZone.getRawOffset() / 60000), end.getSeconds() + 1);
        final EdsCompanySettings edsCompanySettings = company.getCompanySettings();
        SimpleDateFormat longDateFormat = new SimpleDateFormat(edsCompanySettings != null ? edsCompanySettings.getLongDateFormat() : "MMM dd, yyyy [HH:mm]");
        SimpleDateFormat shortDateFormat = new SimpleDateFormat(edsCompanySettings != null ? edsCompanySettings.getShortDateFormat() : "MMM dd, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String startTime = timeFormat.format(startDate).toLowerCase();
        String endTime = timeFormat.format(endDate).toLowerCase();
        String dateString = "";
        if (event.isMultiDayAppointment()) {
            if (event.isAllDay()) {
                dateString = shortDateFormat.format(startDate) + " - " + shortDateFormat.format(endDate);
            } else {
                dateString = longDateFormat.format(startDate) + " - " + longDateFormat.format(endDate);
            }
        } else {
            if (event.isAllDay()) {
                dateString = shortDateFormat.format(startDate);
            } else {
                dateString = shortDateFormat.format(startDate) + "," + startTime + " - " + endTime;
            }
        }
        return dateString + " (" + owner.getUserTimezone().getID() + ")";
    }

    private String getDescription(String description) {
        return description != null ? description : "<i>You have no description for this event.</i>";
    }

    private String getLocation(String location) {
        return location != null ? location : "<i>No location appointment for this event.</i>";
    }

    public void sendWeeklySubscriptionReportMessage(ByteArrayOutputStream baos) throws EdsDbException {
        String subject = commonLocalizer.localize(WEEKLY_USERS_SIGN_UP_REPORT);
        String to = "doniyor.mahkamov@finnetlimited.com";
        Format formatter = new SimpleDateFormat("MM-dd-yyyy");
        String originalName = "Weekly_Subscription_report_" + formatter.format(new Date().getTime()) + ".xls";
        Map<String, Object> values = new TreeMap<>();
        values.put("productName", EdsContextParams.getProductName());
        try {
            String text = EdsTemplates.processTemplate(values, EdsTemplates.WEEKLY_SUBSCRIPTION);
            send(originalName, DOC_EXCEL, to, subject, text, baos);

        } catch (EdsTemplateException e) {
            throw new EdsDbException(e);
        }
    }

    public void sendUnProceedEventsNotification(ArrayList<EdsBusinessEvent> unproceedEvents) throws EdsDbException {
        try {
            String to = "normurod@kpi.com";
            Map<String, Object> values = new TreeMap<>();
            values.put("eventlist", unproceedEvents);
            String text = EdsTemplates.processTemplate(values, EdsTemplates.EVENTS_REPORT);
            sendMessage(to, "Unproceed Events List", text, null, false, null, null, null);
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    public void sendToAddNoteToTaskAndProject(EdsUser from, EdsUser to, String noteBody, boolean isTask, Integer taskOrProjectID) throws EdsTemplateException {
        String subject = commonLocalizer.localize(ADD_NOTE_TO) + " " + (isTask ? commonLocalizer.localize(TASK_LO) : commonLocalizer.localize(PROJECT_LO)) + " " + commonLocalizer.localize(NOTIFICATION);
        EdsCompany company = to.getCompany();
        String taskOrProjectName;
        String taskOrProjectNumber;
        if (isTask) {
            EdsTask task = taskManager.get(taskOrProjectID);
            taskOrProjectName = task.getName();
            taskOrProjectNumber = task.getNumber();
        } else {
            EdsProject project = projectManager.get(taskOrProjectID);
            taskOrProjectName = project.getName();
            taskOrProjectNumber = project.getNumber();
        }
        Map<String, Object> values = new HashMap<>();
        values.put("userTo", to.getName());
        values.put("creatorNote", from.getName());
        values.put("taskOrProjectName", taskOrProjectName);
        values.put("taskOrProject", (isTask ? "task" : "project"));
        values.put("taskOrProjectNumber", taskOrProjectNumber);
        values.put("createdDate", formatDate(to.getUserDate(company.getCompanyDate()), to.getCompany()));//new Date();
        values.put("noteBody", noteBody);
        String taskOrProjectNotes = isTask ? "task|taskNotes/" : "project|projectNotes/";
        String link = EncryptionHelper.encryptURL(taskOrProjectNotes + taskOrProjectID.toString()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(to.getObjectID().toString()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(to.getCompany().getObjectID().toString());
        values.put("link", link);
        values.put("host", EdsContextParams.getHost(company.getObjectID()));

        String updatedMessage = EdsTemplates.processTemplate(values, EdsTemplates.SEND_ADD_NOTE_TO_TASK_PROJECT);
        try {
            if (!to.getDeleted() && !from.getDeleted()) {
                registerInternalMessageBasic(to.getEmail(), subject, updatedMessage, from.getCompany().getObjectID());
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    /**
     * ******************** END Send Messages for Network System ***************************************
     */
    public void sendTestEmail(EmailTemplateItem item) throws EdsDbException {
        registerInternalMessageBasic(item.getTestEmail(), item.getSubject(), item.getMessageHTML(), SecurityContext.getCompanyID() == null ? 0 : SecurityContext.getCompanyID(), true);
    }

    public static class TimeSheetItem {
        private String number;
        private String date;
        private String taskName;
        private String timespent;
        private String comment;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getTimespent() {
            return timespent;
        }

        public void setTimespent(String timespent) {
            this.timespent = timespent;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    public void registerInternalMessageBasic(String to, String subject, String text, Integer companyId) throws EdsDbException {
        registerInternalMessage(to, subject, text, null, null, null, false, null, companyId);
    }

    public void registerInternalMessageBasic(String fromEmail, String to, String subject, String text, Integer companyId) throws EdsDbException {
        registerInternalMessage(fromEmail, to, subject, text, null, null, null, false, null, companyId);
    }

    public void registerInternalMessageBasic(String to, String subject, String text, Integer companyId, boolean isTest) throws EdsDbException {
        registerInternalMessage(to, subject, text, null, null, null, false, null, companyId, isTest);
    }

    private void registerInternalMessage(String fromEmail, String to, String subject, String text, String replyTo, Boolean hasAttachment, List<Integer> fileIDs, boolean isFileBody, String displaySubject, Integer companyId) throws EdsDbException {
        sendMessageFromUser(fromEmail, to, null, null, subject, text, hasAttachment, replyTo, fileIDs, isFileBody, displaySubject, companyId, null);
    }

    private void registerInternalMessage(String to, String subject, String text, String replyTo, Boolean hasAttachment, List<Integer> fileIDs, boolean isFileBody, String displaySubject, Integer companyId) throws EdsDbException {
        sendMessageFromUser(null, to, null, null, subject, text, hasAttachment, replyTo, fileIDs, isFileBody, displaySubject, companyId, null);
    }

    private void registerInternalMessage(String to, String subject, String text, String replyTo, Boolean hasAttachment, List<Integer> fileIDs, boolean isFileBody, String displaySubject, Integer companyId, boolean isTest) throws EdsDbException {
        sendMessageFromUser(null, to, null, null, subject, text, hasAttachment, replyTo, fileIDs, isFileBody, displaySubject, companyId, null, isTest);
    }

    private void registerInternalMessage(String to, String subject, String text, String replyTo, Boolean hasAttachment, List<Integer> fileIDs, boolean isFileBody, String displaySubject, Integer companyId, boolean isTest, boolean isSystem) throws EdsDbException {
        sendMessageFromUser(null, to, null, null, subject, text, hasAttachment, replyTo, fileIDs, isFileBody, displaySubject, companyId, null, isTest, isSystem);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void baseMailSender(EdsSuperMessage message) {
        EdsEmailSetting settings = null;
        if (message.getCompanyID() != null) {
            if (message.getAttachment() != null && message.getAttachment()) {
                message.getFileIDs().forEach(upload -> {
                    Upload uploadItem = new Upload();
                    InputStream inputStream = uploadManager.getInputStream(upload);
                    uploadItem.setFileName(upload.getOriginalName());
                    uploadItem.setContentType(upload.getContentType());
                    uploadItem.setInputStream(inputStream);
                    message.getUploads().add(uploadItem);
                });
            }

            if (!message.getSystem()) {
                settings = wrapEdsCompanySystemSettings(message.getCompanyID(), message.getFromEmail());
                if (settings == null) {
                    message.setAttempts(Optional.ofNullable(message.getAttempts()).orElse(0) + 1);
                    message.setStatus(message.getAttempts() >= 2 ? MessageStatusEnum.FAILED : MessageStatusEnum.PENDING);
                    log.error("Corporate email is not configured for company: [" + message.getCompanyID() + "]");
                    return;
                }
            } else {
                message.setFromEmail(Constants.defaultSupportEmail);
            }
            if (MessageTypeEnum.PREFERRED.equals(message.getType()) && message.getAttempts() == 1) {
                String companyEmail = companyEmailManager.getCompanyEmail(message.getCompanyID());
                if (StringUtils.isNotEmpty(companyEmail)) {
                    message.addBcc(companyEmail);
                }
            }
        }
        try {
            if (message != null && StringUtils.isNotBlank(message.getTo())) {
                EdsMailer mailer = EdsMailer.getNewInstance(message, settings);
                mailer.sendSynchronized();
                message.setStatus(MessageStatusEnum.SENT);
                log.info("Message sent[TO:" + message.getTo() + ",SUBJECT:" + message.getSubject() + "]");
            } else {
                throw new Exception("Email sending failed: TO=" + (message != null ? message.getTo() : "null"));
            }
        } catch (Exception e) {
            message.setStatus(message.getAttempts() >= 2 ? MessageStatusEnum.FAILED : MessageStatusEnum.PENDING);
            log.error("Error sending email[MESSAGE_ID:" + (message.getObjectID() != null ? message.getObjectID().toString() : "message id is null") + "]:", e);
        }
    }

    @Override
    public void setCustomerBalanceEmail(MessageItem messageItem, DateNonConvertable fromDatNC, DateNonConvertable toDateNC) {
        EdsUser user = userManager.getUser();
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setCrmAccountId(messageItem.getAccountId());
        filterParameter.setAccountType(Constants.SUPPLIER_BALANCE_CATEGORY.equals(messageItem.getType()) ? EdsCrmAccount.SUPPLIER : EdsCrmAccount.CUSTOMER);
        filterParameter.setFromDate(fromDatNC.getNonConvertedDate().getTime());
        filterParameter.setToDate(toDateNC.getNonConvertedDate().getTime());
        filterParameter.setShowSubAccountTransaction(messageItem.isIncludeSubAccountTransaction());

        SimpleDateFormat filterDateFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
        filterParameter.setStartDateNC(filterDateFormat.format(fromDatNC.getNonConvertedDate()));
        filterParameter.setEndDateNC(filterDateFormat.format(toDateNC.getNonConvertedDate()));

        org.apache.commons.io.output.ByteArrayOutputStream baos = customerBalancePdfHandler.getPDFStream(filterParameter);

        ArrayList<Integer> fielIDs = new ArrayList<>();
        EdsUpload upload = new EdsUpload();
        upload.setContentType("application/pdf");
        String filename = Constants.SUPPLIER_BALANCE_CATEGORY.equals(messageItem.getType()) ? "Supplier Balance" : "Customer Balance";
        filename += "-" + userManager.getUser().getCompany().getName();
        filename += "-" + dateFormat.format(userManager.getUser().getCompany().getCompanyDate()) + ".pdf";
        filename = ServerUtils.normalizeFileNameT(filename);
        upload.setOriginalName(filename);
        upload.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
        uploadManager.create(upload);
        fielIDs.add(upload.getObjectID());
        try {
            sendMessageFromUser(messageItem.getFromEmail(), messageItem.getToEmails(), messageItem.getCc(), messageItem.getBcc(),
                    messageItem.getSubject(), messageItem.getMailContent(), true, null, fielIDs, false, null, null, user);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    /**
     * immediately sends a mail
     * if message sending is failed then added to DB for standby mail service
     *
     * @param message
     */
    public void internalMailSender(EdsMessage message, Integer companyId) throws Exception {
        if (companyId != null) ServerSecurityContext.getInstance().setCompanyId(companyId);
        try {
            if (message.getAttachment() != null && message.getAttachment()) {
                message.getFileIDs().forEach(upload -> {
                    Upload uploadItem = new Upload();
                    InputStream inputStream = uploadManager.getInputStream(upload);
                    uploadItem.setFileName(upload.getOriginalName());
                    uploadItem.setContentType(upload.getContentType());
                    uploadItem.setInputStream(inputStream);
                    message.getUploads().add(uploadItem);
                });
            }
            processMailsSender(message, wrapEdsCompanySystemSettings(companyId, message.getFromEmail()));
            log.info("Message sent[TO:" + message.getTo() + ",SUBJECT:" + message.getSubject() + "]");
            message.setStatus(MessageStatusEnum.SENT);
            update(message);
            flush();
        } catch (Exception e) {
            log.error("Error sending email[MESSAGE_ID:" + (message.getObjectID() != null ? message.getObjectID().toString() : "message id is null") + "]:", e);
            throw new RuntimeException(e);
        }
    }

    public EdsEmailSetting wrapEdsCompanySystemSettings(Integer companyId, String fromEmail) {
        EdsEmailSetting emailSetting = null;
        if (StringUtils.isNotEmpty(fromEmail)) {
            emailSetting = emailSettingsManager.getActiveEmailSetting(fromEmail);
        }
        if (emailSetting == null) {
            emailSetting = emailSettingsManager.getCompanyEmailSetting(companyId);
        }
        return emailSetting;
    }

    private void processMailsSender(EdsMessage message, EdsEmailSetting settingsItem) throws Exception {
        EdsMailer mailer;
        try {
            mailer = EdsMailer.getNewInstance(message, settingsItem);
        } catch (Exception e) {
            log.error("Cannot get instance of EdsMailer, exception: " + e);
            throw new RuntimeException(e);
        }
        try {
            mailer.sendSynchronized();
        } catch (Exception e) {
            if (e instanceof AddressException) {
                message.setStatus(MessageStatusEnum.FAILED);
            } else if (e instanceof MessagingException) {
                message.setStatus(MessageStatusEnum.PENDING);
                message.setAttempts(message.getAttempts() != null && message.getAttempts() >= 1 ? message.getAttempts() - 1 : message.getAttempts());
            }
            update(message);
            flush();
            ServerSecurityContext.getInstance().removeCompanyId();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendSalesQuoteCancelledMessage(String quoteNumber, EdsCrmContact crmContact) {
        try {
            EdsUser user = getUser();
            Map<String, Object> values = new HashMap<>();
            values.put("CONTACT_NAME", crmContact != null ? crmContact.getName() : "");
            values.put("QUOTE_NUMBER", quoteNumber);
            values.put("COMPANY", user.getCompany().getName());
            values.put("USER_NAME", user.getName());

            String text = EdsTemplates.processTemplate(values, EdsTemplates.QUOTE_CANCELLED_MESSAGE);
            sendMessage(crmContact.getPrimaryEmail(), "Sales Quote " + quoteNumber + " cancelled", text, null, false, null, null, null);
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void sendNotificationToCalendarEventGuests(EdsUser user, EdsEvent event, String guestsEmail, String guestNames, String action) throws EdsDbException {
        if (event != null) {
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generateCalendarInvitationGuests(user, event, guestsEmail, guestNames, action);
            if (templateItem != null && !user.getDeleted()) {
                registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), user.getCompany().getObjectID());
            } else {
                EdsCompany company = user.getCompany();
                Map<String, Object> values = new TreeMap<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM");
                String userFullName = getEventGuestName(guestsEmail, company.getObjectID());
                values.put("USER", !"".equals(userFullName.trim()) ? userFullName : guestsEmail.contains("@") ? guestsEmail.substring(0, guestsEmail.indexOf("@")) : guestsEmail);
                values.put("EVENT_NAME", event.getSubject());
                values.put("WHERE", getLocation(event.getVenue()));
                values.put("DESCRIPTION", getDescription(event.getDescription()));
                String dateString = getEventDates(event, user);
                values.put("DATE", dateString);
                String host = EdsContextParams.getHost(company.getObjectID());
                values.put("CREATOR_NAME", user.getFullName());
                values.put("SHARED_WITH", "<p>" + commonLocalizer.localize("sharedWith") + ": " + getEventAttendees(event, user.getObjectID()) + "</p>");
                String subject = "";

                // Attachments
                EdsFolder eventFolder = folderManager.getFolderByFolderType(EdsFolder.F_EVENT);
                List<FileResource> eventAttachments = null;
                if (eventFolder != null) {
                    eventAttachments = attachmentUtilsManager.getAttachments(F_EVENT, eventFolder.getObjectID(), event.getObjectID(), user);
                }
                String dataBaseName = EncryptionHelper.encrypt(SecurityContext.getInstance().getDatabase());
                String companyID = SessionCryptor.encrypt(company.getObjectID().toString());
                String eventID = SessionCryptor.encrypt(event.getObjectID().toString());
                try {
                    String subjectAdd = commonLocalizer.localize(PdfLocalizationName.eventInvitation, "Event Invitation:");
                    String subjectEdit = commonLocalizer.localize(PdfLocalizationName.updatedInvitation, "Updated Invitation:");
                    String subjectDelete = commonLocalizer.localize(PdfLocalizationName.canceledEvent, "Canceled Event:");
                    String eventUpdatedAction = commonLocalizer.localize(PdfLocalizationName.eventUpdatedAction, "Updated");
                    String eventDeleteAction = commonLocalizer.localize(PdfLocalizationName.eventDeleted, "Deleted");
                    String pleaseBeInformed = commonLocalizer.localize(PdfLocalizationName.pleaseBeInformed, "Please, be informed");
                    String messageEdit = commonLocalizer.localize(PdfLocalizationName.updatedThisEvent, "updated this event");
                    String messagedelete = commonLocalizer.localize(PdfLocalizationName.hasDeletedTheFollowingEventThatYouHaveBeenInvited, "has <b>deleted</b> the following event that you have been invited:");
                    String gusetsLocalize = commonLocalizer.localize(PdfLocalizationName.guests, "Guests");
                    String going = commonLocalizer.localize(PdfLocalizationName.going, "Going?");
                    String yes = commonLocalizer.localize(PdfLocalizationName.Yes, "Yes");
                    String no = commonLocalizer.localize(PdfLocalizationName.No, "No");
                    String maybe = commonLocalizer.localize(PdfLocalizationName.goingMaybe, "Maybe");

                    if ("add".equals(action)) {
                        subject = subjectAdd + " " + event.getSubject() + " " + dateString;
                        values.put("BODY", "<p>" + pleaseBeInformed + " " + event.getSubject() + "</p>");
                        values.put("UPDATED", "");
                        values.put("GUESTS", "<p>" + gusetsLocalize + " " + getEventGuests(event) + "</p>");
                        values.put("ACTION", "<p><b>" + going + "</b> <a href=\"" + host + "/eventGuest.html?dtype=" + dataBaseName + "&cid=" + companyID + "&id=" + eventID + "&email=" + guestsEmail + "&answer=Accepted\">" + yes + "</a> - <a href=\"" + host + "/eventGuest.html?dtype=" + dataBaseName + "&cid=" + companyID + "&id=" + eventID + "&email=" + guestsEmail + "&answer=Tentatively\">\n" +
                                "" + maybe + "</a> - <a href=\"" + host + "/eventGuest.html?dtype=" + dataBaseName + "&cid=" + companyID + "&id=" + eventID + "&email=" + guestsEmail + "&answer=Declined\">" + no + "</a></p>");
                    } else if ("edit".equals(action)) {
                        subject = subjectEdit + " " + event.getSubject() + " " + dateString;
                        values.put("BODY", "<p>" + pleaseBeInformed + " " + user.getFullName() + " " + messageEdit + "</p>");
                        values.put("UPDATED", eventUpdatedAction);
                        values.put("GUESTS", "<p>" + gusetsLocalize + " " + getEventGuests(event) + "</p>");
                        values.put("ACTION", "<p><b>" + going + "</b> <a href=\"" + host + "/eventGuest.html?dtype=" + dataBaseName + "&cid=" + companyID + "&id=" + eventID + "&email=" + guestsEmail + "&answer=Accepted\">" + yes + "</a> - <a href=\"" + host + "/eventGuest.html?dtype=" + dataBaseName + "&cid=" + companyID + "&id=" + eventID + "&email=" + guestsEmail + "&answer=Tentatively\">\n" +
                                "" + maybe + "</a> - <a href=\"" + host + "/eventGuest.html?dtype=" + dataBaseName + "&cid=" + companyID + "&id=" + eventID + "&email=" + guestsEmail + "&answer=Declined\">" + no + "</a></p>");
                    } else if ("delete".equals(action)) {
                        subject = subjectDelete + " " + event.getSubject();
                        values.put("BODY", "<p>" + pleaseBeInformed + " " + event.getOwner().getFullName() + " " + messagedelete + "</p>");
                        values.put("UPDATED", eventDeleteAction);
                        values.put("GUESTS", "");
                        values.put("ACTION", "");
                    }
                    ArrayList<Integer> fileIDs = null;
                    if (eventAttachments != null && !eventAttachments.isEmpty() && event.getIncludeAttachments() != null && event.getIncludeAttachments()) {
                        fileIDs = new ArrayList<>();
                        for (FileResource file : eventAttachments) {
                            fileIDs.add(file.getObjectId());
                        }
                    }
                    String text = EdsTemplates.processTemplate(values, EdsTemplates.CALENDAR_EVENT_INVITATION_FOR_GUESTS);
                    registerInternalMessage(guestsEmail, subject, text, null, true, fileIDs, true, null, company.getObjectID());
                } catch (EdsTemplateException e) {
                    throw new EdsDbException(e);
                }
            }
        }
    }

    public void sendMailToOwnerAboutGuestsStatus(Integer eventId, String email, String answer) {
        EdsEvent event = eventManager.get(eventId);
        EdsUser owner = event.getOwner();
        String guestName = getEventGuestName(email, owner.getCompany().getObjectID());

        String dateString = getEventDates(event, owner);
        String subject = "";
        String action = "";
        if ("Accepted".equals(answer)) {
            subject = commonLocalizer.localize(INVITATION_ACCEPTED_SUBJECT) + ": " + guestName + " - " + event.getSubject() + " " + dateString;
            action = "has accepted";
        } else if ("Tentatively".equals(answer)) {
            subject = commonLocalizer.localize(TENTATIVELY_ACCEPTED_SUBJECT) + ": " + guestName + " - " + event.getSubject() + " " + dateString;
            action = "has replied \"Maybe\" to";
        } else if ("Declined".equals(answer)) {
            subject = commonLocalizer.localize(INVITATION_DECLINED_SUBJECT) + ": " + guestName + " - " + event.getSubject() + " " + dateString;
            action = "has declined";
        }
        event.setInvitationResponse(answer);

        eventManager.update(event, true);

        Map<String, Object> values = new TreeMap<>();
        values.put("OWNER", owner.getName());
        values.put("GUEST", guestName);
        values.put("ACTION", action);
        values.put("EVENT_NAME", event.getSubject());
        values.put("GUESTS", "<p>Guests: " + getEventGuests(event) + "</p>");
        values.put("SHARED_WITH", "<p>Shared with: " + getEventAttendees(event, owner.getObjectID()) + "</p>");
        values.put("DESCRIPTION", getDescription(event.getDescription()));
        values.put("WHERE", getLocation(event.getVenue()));
        values.put("DATE", dateString);

        String text = null;
        try {
            text = EdsTemplates.processTemplate(values, EdsTemplates.CALENDAR_EVENT_INVITATION_RESULT_FOR_OWNER);
        } catch (EdsTemplateException e) {
            e.printStackTrace();
        }
        try {
            registerInternalMessage(owner.getEmail(), subject, text, null, true, null, false, null, owner.getCompany().getObjectID());
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPurchaseOrderApprovedOrDeclinedMessage(EdsPurchaseOrder order) {
        Map<String, Object> values = new TreeMap<>();
        values.put("SUBMITTER", order.getCreator().getName());
        values.put("APPROVER", order.getApprover().getName());
        values.put("PONUMBER", order.getNumber());
        values.put("CREATION_DATE", Utils.formatDate(order.getCreationDate(), order.getCompany()));
        String link = EdsContextParams.getFullHost() + "Accounting.html?link=" + EncryptionHelper.encryptURL("purchaseorder|summary/" + order.getObjectID())
                + "&uid=" + EncryptionHelper.encryptURL(order.getCreator().getObjectID().toString())
                + "&cid=" + EncryptionHelper.encryptURL(order.getCompany().getObjectID().toString());
        values.put("LINK", link);
        String text = "", subject = "";
        try {
            if (APPROVE.equals(order.getStatus().getCode())) {
                subject = commonLocalizer.localize(PURCHASE_ORDER_APPROVEL);
                text = EdsTemplates.processTemplate(values, EdsTemplates.PO_APPROVED_MESSAGE);
            } else if (REJECT.equals(order.getStatus().getCode())) {
                subject = commonLocalizer.localize(PURCHASE_ORDER_REJECTED);
                text = EdsTemplates.processTemplate(values, EdsTemplates.PO_REJECTED_MESSAGE);
            }
            sendMessageFromUser(null, order.getCreator().getEmail(), null, null, subject, text, false, null, null, false, null, null, order.getApprover());
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getWebContentByUrl(String url) {
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String res = in.readLine();
            in.close();
            return res;
        } catch (Exception exp) {
            return exp.getMessage();
        }
    }

    @Override
    public String smsTemplateGenerateText(String value, ContactListItem lead, EmployeeListItem employee) {
        EdsTraceable object = null;
        if (lead != null) {
            object = crmContactManager.get(lead.getObjectId());
        } else if (employee != null) {
            object = employeeManager.get(employee.getObjectID());
        } else {
            return null;
        }
        Map<String, Object> keyValues = object.getFieldValueAsMap(modelFieldManager.getFieldIDs(employee != null ? LayoutRPC.HRMS_EMPLOYEE_FORM : lead != null && lead.isLeadContact() ? LayoutRPC.LEAD_FORM : LayoutRPC.CONTACT_FORM));
        keyValues = allInOneServiceLocal.getAdditionalFieldValuesAsMap(employee != null ? RelationItem.TYPE_EMPLOYEE : lead.isLeadContact() ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT, object, keyValues, userManager.getUser());
        try {
            return EdsTemplates.evaluateTemplate(keyValues, value);
        } catch (EdsTemplateException e) {
            return null;
        }
    }

    @Override
    public String smsTemplateGenerateTextForSalesInvoice(String value, Integer saleInvoiceId) {
        EdsTraceable object = null;
        if (saleInvoiceId != null) {
            object = invoiceManager.getSaleInvoice(saleInvoiceId);
        }
        Map<String, Object> keyValues = object.getFieldValueAsMap(modelFieldManager.getFieldIDs(LayoutRPC.SALEINVOICE_FORM));
        keyValues = allInOneServiceLocal.getAdditionalFieldValuesAsMap(RelationItem.TYPE_SALEINVOICE, object, keyValues, userManager.getUser());
        try {
            return EdsTemplates.evaluateTemplate(keyValues, value);
        } catch (EdsTemplateException e) {
            return null;
        }
    }

    @Override
    public String generateEmployeeEventTemplate(String templateContent, EmployeeListItem employee) {
        EdsTraceable object = employeeManager.get(employee.getObjectID());

        Map<String, Object> keyValues = object.getFieldValueAsMap(modelFieldManager.getFieldIDs(LayoutRPC.HRMS_EMPLOYEE_FORM));
        keyValues = allInOneServiceLocal.getAdditionalFieldValuesAsMap(RelationItem.TYPE_EMPLOYEE, object, keyValues, userManager.getUser());
        try {
            return EdsTemplates.evaluateTemplate(keyValues, templateContent);
        } catch (EdsTemplateException e) {
            return null;
        }
    }

    @Override
    public Appointment generateCandidateEventTemplate(String templateContent, ContactListItem lead, boolean isSubject, String... subject) {
        EdsUser user = userManager.getUser();
        EdsTraceable object = crmContactManager.get(lead.getObjectId());
        Map<String, Object> keyValues = object.getFieldValueAsMap(modelFieldManager.getFieldIDs(LayoutRPC.CANDIDATE_FORM));

        keyValues.put(EVENT_START_DATE, ServerUtils.longDateFormat(ServerUtils.getCompanyDate(lead.getEventStartDate(), user.getCompany()), user));
        keyValues.put(EVENT_END_DATE, ServerUtils.longDateFormat(ServerUtils.getCompanyDate(lead.getEventEndDate(), user.getCompany()), user));
        if (object instanceof EdsCrmContact) {
            keyValues.put(OBJECT_KEY, ((EdsCrmContact) object).getObjectKey() != null ? ((EdsCrmContact) object).getObjectKey() : "");
        }

        EdsEvent event = new EdsEvent();
        event.setStartDate(lead.getEventStartDate());
        event.setEndDate(lead.getEventEndDate());
        event.setSubject(String.join(" ", subject));

        Appointment item = new Appointment();
        String zoomLink = lead.getCandidateZoomLink();
        if (!isSubject) {
            if (zoomLink == null) {
                Appointment meeting = zoomService.createMeeting(event);
                item.setZoomLink(meeting.getLinkURL());
                item.setZoomObjectId(meeting.getZoomObjectId());
                keyValues.put(ZOOM_LINK, meeting.getLinkURL());
            } else {
                event.setDescription(zoomLink);
                item.setZoomLink(zoomLink);
                zoomService.updateMeeting(event);
                keyValues.put(ZOOM_LINK, zoomLink);
            }
        }


        keyValues = allInOneServiceLocal.getAdditionalFieldValuesAsMap(RelationItem.TYPE_CANDIDATE, object, keyValues, user);

        try {
            item.setTemplateValue(EdsTemplates.evaluateTemplate(keyValues, templateContent));
            return item;
        } catch (EdsTemplateException e) {
            return null;
        }
    }

    @Override
    public String crmAccountSmsTemplateGenerateText(String value, CrmAccountItem crmAccount) {
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(crmAccount.getObjectId());
        Map<String, Object> keyValues = getCrmAccountAttributes(edsCrmAccount);
        try {
            return EdsTemplates.evaluateTemplate(keyValues, value);
        } catch (EdsTemplateException e) {
            return null;
        }
    }

    private Map<String, Object> getCrmAccountAttributes(EdsCrmAccount edsCrmAccount) {
        Map<String, Object> keyValues = new HashMap<>();
        if (edsCrmAccount != null) {
            EdsUser from = companyManager.getUser();
            String senderFirstName = from.getFirstName() != null ? from.getFirstName() : "";
            String senderLastName = from.getLastName() != null ? from.getLastName() : "";
            String senderEmail = from.getEmail();
            String senderCompanyName = from.getCompany().getName() != null ? from.getCompany().getName() : "";
            EdsEmployee employee = employeeManager.get(from.getObjectID());
            String senderPrimaryPhone = "";
            if (from.isClientContact()) {
                if (from.getClientContact() != null && from.getClientContact().getCrmContact().getPrimaryPhone() != null) {
                    senderPrimaryPhone = from.getClientContact().getCrmContact().getPrimaryPhone();
                } else if (from.getClientContact() != null && from.isEmployee()) {
                    senderPrimaryPhone = from.getClientContact().getEmployee().getPrimaryPhone() != null ? from.getClientContact().getEmployee().getPrimaryPhone() : "";
                }
            } else {
                senderPrimaryPhone = employee != null && employee.getPrimaryPhone() != null ? employee.getPrimaryPhone() : "";
            }
            String senderMobilePhone = "";
            if (from.isClientContact()) {
                if (from.getClientContact() != null && from.getClientContact().getCrmContact() != null) {
                    List<EdsCrmContactItemParams> numbers = from.getClientContact().getCrmContact().getItemParams(EdsCrmContactItemParams.PHONE);
                    for (EdsCrmContactItemParams phone : numbers) {
                        senderMobilePhone = phone.getRelation() == EdsCrmContactItemParams.MOBILE ? phone.getValue() : "";
                        if (phone.getRelation() == EdsCrmContactItemParams.MOBILE) {
                            break;
                        }
                    }
                }
            } else {
                if (employee != null && employee.getContact() != null) {
                    List<EdsCrmContactItemParams> numbers = employee.getContact().getItemParams(EdsCrmContactItemParams.PHONE);
                    for (EdsCrmContactItemParams phone : numbers) {
                        senderMobilePhone = phone.getRelation() == EdsCrmContactItemParams.MOBILE ? phone.getValue() : "";
                        if (phone.getRelation() == EdsCrmContactItemParams.MOBILE) {
                            break;
                        }
                    }
                }
            }
            String senderJobTitle = from.getEmployee().getContact().getJobTitles() != null ? from.getEmployee().getContact().getJobTitles() : "";
            EdsCrmContact edsCrmContact = edsCrmAccount.getPrimaryContact();
            String recipientFirstName = "";
            String recipientLastName = "";
            String recipientEmail = "";
            String recipientCompanyName = "";
            if (edsCrmContact != null) {
                recipientFirstName = edsCrmContact.getFirstName() == null ? " " : edsCrmContact.getFirstName();
                recipientLastName = edsCrmContact.getLastName() == null ? " " : edsCrmContact.getLastName();
                recipientEmail = edsCrmContact.getPrimaryEmail();
                if (edsCrmContact.getCrmAccount() != null) {
                    recipientCompanyName = edsCrmAccount.getName();
                }
            }
            if (edsCrmAccount.getName() != null) {
                recipientCompanyName = edsCrmAccount.getName();
            }
            if (recipientFirstName == null && recipientEmail != null && !"".equals(recipientEmail)) {
                if (recipientEmail.contains("<")) {
                    recipientFirstName = recipientEmail.substring(0, recipientEmail.indexOf("<"));
                } else {
                    recipientFirstName = recipientEmail.substring(0, recipientEmail.indexOf("@"));
                }
            }
            Integer calculationScale = 2;
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            if (financialSettings != null && financialSettings.getCalculationScale() != null) {
                calculationScale = financialSettings.getCalculationScale();
            }
            DecimalFormat decimalFormat = ServerUtils.getDecimalFormat(calculationScale);

            EdsCurrency baseCurrency = financialSettings != null ? financialSettings.getCurrency() : new EdsCurrency();
            BigDecimal clientBalance = crmAccountManager.getClientBalance(edsCrmAccount.getObjectID(), baseCurrency.equals(edsCrmAccount.getCurrency()));
            BigDecimal supplierBalance = crmAccountManager.getSupplierBalance(edsCrmAccount.getObjectID(), baseCurrency.equals(edsCrmAccount.getCurrency()));

            keyValues.put(EmailTemplateUtils.ET_RECIPIENT_FIRST_NAME, StringUtils.capitalize(recipientFirstName));
            keyValues.put(EmailTemplateUtils.ET_RECIPIENT_LAST_NAME, recipientLastName);
            keyValues.put(EmailTemplateUtils.ET_RECIPIENT_EMAIL, recipientEmail);
            keyValues.put(EmailTemplateUtils.ET_RECIPIENT_COMPANY_NAME, recipientCompanyName);
            keyValues.put(EmailTemplateUtils.ET_SENDER_FIRST_NAME, StringUtils.capitalize(senderFirstName));
            keyValues.put(EmailTemplateUtils.ET_SENDER_LAST_NAME, senderLastName);
            keyValues.put(EmailTemplateUtils.ET_SENDER_EMAIL, senderEmail);
            keyValues.put(EmailTemplateUtils.ET_SENDER_COMPANY_NAME, senderCompanyName);
            keyValues.put(EmailTemplateUtils.ET_SENDER_PRIMARY_PHONE, senderPrimaryPhone);
            keyValues.put(EmailTemplateUtils.ET_SENDER_MOBILE_PHONE, senderMobilePhone);
            keyValues.put(EmailTemplateUtils.ET_SENDER_JOB_TITLE, senderJobTitle);
            keyValues.put(EmailTemplateUtils.ET_SUPPLIER_BALANCE, supplierBalance != null ? decimalFormat.format(supplierBalance) : "0.00");
            keyValues.put(EmailTemplateUtils.ET_CUSTOMER_BALANCE, clientBalance != null ? decimalFormat.format(clientBalance) : "0.00");
        }
        return keyValues;
    }

    @Override
    public Boolean generateAndSendSms(EdsSmsSendItem sms) {
        if (sms != null && sms.getProvider() != null) {
            EdsSmsSettings edsSmsSetting = sms.getProvider();
            SmsProvider provider = edsSmsSetting.initProvider(null);
            if (provider.send(sms.getMessageText(), sms.getToNumber())) {
                return true;
            } else {
                String response = provider.getResponse();
                if (response != null && response.contains(":")) {
                    String s = response.split(":")[1];
                    s = s.substring((s.indexOf(",") + 1));
                    sms.setMessageText(s);
                } else {
                    sms.setMessageText(response);
                }
                smsSendItemManager.update(sms);
                return false;
            }
        }
        return false;
    }

    public String getEventGuestName(String guestsEmail, Integer companyID) {
        String email = guestsEmail;
        String userFullName = "";
        EdsCrmContact contact = crmContactManager.getContactByEmail(email, companyID);
        if (contact != null) {
            userFullName = contact.getName();
        }
        return "".equals(userFullName.trim()) ? email : userFullName;
    }

    private String getEventDates(EdsEvent event, EdsUser owner) {
        Format formatter = new SimpleDateFormat("MMM dd, yyyy [HH:mm]");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat();
        dateFormat1.setTimeZone(owner.getUserTimezone());
        String start = dateFormat1.format(event.getStartDate());
        String end = dateFormat1.format(event.getEndDate());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat();
        Date formattedDate1 = null;
        try {
            formattedDate1 = dateFormat2.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date formattedDate2 = null;
        try {
            formattedDate2 = dateFormat2.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String startDate = formatter.format(formattedDate1);
        String endDate = formatter.format(formattedDate2);
        String date = startDate + " - " + endDate;
        return date + " (" + owner.getUserTimezone().getID() + ")";
    }

    public void sendSalesQuotePingPongNotificationIfEnabled(EdsSaleQuote saleQuote, String rejectionReason) {
        String status = saleQuote.getStatus().getCode();
        EdsUser user = getUser();
        if (user != null) {
            //If Client Contact
            if (user.isClientContact()) {
                if (CLIENT_APPROVE.equals(status)) {
                    sendSalesQuoteApprovedMessage(saleQuote);
                } else if (REJECT.equals(status)) {
                    sendSalesQuoteRejectedMessage(saleQuote, rejectionReason);
                }
                saleQuote.setMailSender(null);
                saleQuote.setUpdatedDate(new Date());
            }
        }
    }

    private void sendSalesQuoteApprovedMessage(EdsSaleQuote saleQuote) {
        String status = saleQuote.getStatus().getCode();

        EdsUser loggedUser = getUser();
        EdsUser userForSending;
        if (saleQuote.getMailSender() != null) {
            userForSending = saleQuote.getMailSender();
        } else {
            userForSending = saleQuote.getCreator();
        }

        Map<String, Object> values = new TreeMap<>();
        values.put("TO_USER_NAME", userForSending.getName());
        values.put("FROM_USER_NAME", loggedUser.getName());
        values.put("QUOTE_NUMBER", saleQuote.getNumber());
        values.put("APPROVED_DATE", ServerUtils.shortDateFormat(loggedUser.getUserDate(), loggedUser));
        String link = EdsContextParams.getFullHost() + "Accounting.html?link=" + EncryptionHelper.encryptURL("salequote|summary/" + saleQuote.getObjectID())
                + "&uid=" + EncryptionHelper.encryptURL(userForSending.getObjectID().toString())
                + "&cid=" + EncryptionHelper.encryptURL(userForSending.getCompany().getObjectID().toString());
        values.put("LINK", link);

        String subject = commonLocalizer.localize(SALES_QUOTE) + " " + saleQuote.getNumber() + commonLocalizer.localize(HAS_BEEN_APPROVED_BY) + " " + loggedUser.getName();
        try {
            String text = "";
            if (APPROVE.equals(status)) {
                text = EdsTemplates.processTemplate(values, EdsTemplates.QUOTE_MANAGER_APPROVE_MESSAGE);
            } else if (CLIENT_APPROVE.equals(status)) {
                text = EdsTemplates.processTemplate(values, EdsTemplates.QUOTE_CLIENT_APPROVE_MESSAGE);
            }
            sendMessageFromUser(null, userForSending.getEmail(), null, null, subject, text, false, null, null, false, null, null, loggedUser);
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    private void sendSalesQuoteRejectedMessage(EdsSaleQuote saleQuote, String rejectionReason) {
        String status = saleQuote.getStatus().getCode();

        EdsUser loggedUser = getUser();
        EdsUser userForSending;
        if (saleQuote.getMailSender() != null) {
            userForSending = saleQuote.getMailSender();
        } else {
            userForSending = saleQuote.getCreator();
        }

        Map<String, Object> values = new TreeMap<>();
        values.put("TO_USER_NAME", userForSending.getName());
        values.put("FROM_USER_NAME", loggedUser.getName());
        values.put("QUOTE_NUMBER", saleQuote.getNumber());
        values.put("APPROVED_DATE", ServerUtils.shortDateFormat(loggedUser.getUserDate(), loggedUser));
        if (rejectionReason != null && !"".equals(rejectionReason.trim())) {
            values.put("REJECTION_REASON", rejectionReason);
        }
        String link = EdsContextParams.getFullHost() + "Accounting.html?link=" + EncryptionHelper.encryptURL("salequote|summary/" + saleQuote.getObjectID())
                + "&uid=" + EncryptionHelper.encryptURL(userForSending.getObjectID().toString())
                + "&cid=" + EncryptionHelper.encryptURL(userForSending.getCompany().getObjectID().toString());
        values.put("LINK", link);

        String subject = commonLocalizer.localize(SALES_QUOTE) + " " + saleQuote.getNumber() + " " + commonLocalizer.localize(HAS_BEEN_REJECTED_BY) + " " + loggedUser.getName();
        try {
            String text = "";
            if (MANAGER_REJECT.equals(status)) {
                text = EdsTemplates.processTemplate(values, EdsTemplates.QUOTE_MANAGER_REJECT_MESSAGE);
            } else if (REJECT.equals(status)) {
                text = EdsTemplates.processTemplate(values, EdsTemplates.QUOTE_CLIENT_REJECT_MESSAGE);
            }
            sendMessageFromUser(null, userForSending.getEmail(), null, null, subject, text, false, null, null, false, null, null, loggedUser);
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void sendDocumentsUploadNotification(String to, String subject, String text, Integer receiverID, Integer companyID, String notificationType) throws EdsDbException {
        try {
            boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(receiverID, notificationType);
            if (emailNotificationSettings) {
                registerInternalMessageBasic(to, subject, text, companyID);
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }


    public void sendTopicCreatedToExpertNotificationForCOO(EdsUser expert, String topicName, Integer companyId) throws EdsDbException {

        try {
            boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(expert.getObjectID(), EmailNotificationConstants.COO_QUESTIONS_ASKED_NOTIFICATION);
            if (emailNotificationSettings) {
                String subject = commonLocalizer.localize(NEW_QUESTION_POSTED);
                String to = expert.getEmail();
                Map<String, Object> values = new TreeMap<>();
                values.put("username", expert.getFullName());
                values.put("topicname", topicName);
                String linkURL = EdsContextParams.getHost(companyId) + "/CooWorkspace.html";
                values.put("link", linkURL);

                String text = EdsTemplates.processTemplate(values, EdsTemplates.POST_QUESTION_TO_EXPERT);
                registerInternalMessageBasic(to, subject, text, companyId);

            }
        } catch (EdsTemplateException | EdsDbException ex) {
            ex.printStackTrace();
        }
    }

    /*@test AccoutingTests - testFixedAssetsCountMessageSending() */
    public void sendFixedAssetCountResult(FixedAssetList fixedAssetList) {
        try {
            EdsUser user = getUser();
            String subject = commonLocalizer.localize(FIXED_ASSET_COUNT_RESULT);
            String to = user.getCompany().getCreator().getEmail();
            Map<String, Object> values = new TreeMap<>();
            values.put("COMPANY_NAME", user.getCompany().getName());
            values.put("CREATOR", StringUtils.capitalize(user.getCompany().getCreator().getFirstName()) + " " + user.getCompany().getCreator().getLastName());
            values.put("USER", StringUtils.capitalize(user.getFirstName()) + " " + user.getLastName());
            values.put("DATE", ServerUtils.shortDateFormat(user.getUserDate(), user));
            values.put("FIXED_ASSETS", fixedAssetList.getItems());
            String text = EdsTemplates.processTemplate(values, EdsTemplates.FIXED_ASSET_COUNT_RESULT);
            EdsUpload upload = new EdsUpload();
            ByteArrayInputStream input = new ByteArrayInputStream(fixedAssetsCountPdfHandler.getPDFStream(fixedAssetList.getItems()).toByteArray());
            upload.setInputStream(input);
            upload.setContentType("application/pdf");
            upload.setOriginalName("FixedAssetsCount.pdf");
            uploadManager.create(upload);
            ArrayList<Integer> fileIds = new ArrayList<>();
            fileIds.add(upload.getObjectID());
            sendMessage(to, subject, text, user.getEmail(), true, fileIds, null, null);
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendToManagerForCourseBookingConfirm(EdsCourseBooking courseBooking, Integer userID, String toEmail) {
        EdsUser user = userManager.get(userID);
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateCourseBookingConfirm(courseBooking, user, toEmail);

        if (templateItem != null) {
            try {
                sendMessage(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), user.getEmail(), false, null, null, null);
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendAddOnRequestMessage(EdsGenericSettings addOnSettings) {
        try {
            EdsUser user = getUser();
            EdsCompany company = user.getCompany();

            String toEmail = EdsContextParams.getSupportEmail();
            if (toEmail == null) {
                toEmail = "support@kpi.com";
            }

            String subject = commonLocalizer.localize(ADD_ON_REQUEST_FOR) + " " + company.getName();

            Map<String, Object> values = new TreeMap<>();
            values.put("COMPANY_NAME", company.getName());
            values.put("COMPANY_ID", company.getObjectID());
            values.put("CONTACT_PERSON", user.getName());
            values.put("ADDON_NAME", addOnSettings.getName());

            String text = EdsTemplates.processTemplate(values, EdsTemplates.ADDON_REQUEST_MESSAGE);
            sendMessageFromUser(null, toEmail, null, null, subject, text, false, null, null, false, null, null, user);
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendOpportunityAssigned(EdsOpportunity opportunity, EdsEmployee assignee, boolean created, Integer userID) {
        try {
            EdsUser user = getUser() == null ? userManager.get(userID) : getUser();
            if (assignee != null) {
                EdsEmailTemplate template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(created ? OPPORTUNITY_CREATED_CATEGORY : OPPORTUNITY_ASSIGNED_CATEGORY);
                String toEmail = assignee.getEmail();
                String subject = commonLocalizer.localize(OPPORTUNITY_SUBJECT) + " " + (created ? " " + commonLocalizer.localize(CREATED) : " " + commonLocalizer.localize(ASSIGNED));
                Map<String, Object> values = new TreeMap<>();
                values.put(EmailTemplateConstants.USER, user.getName());
                values.put(EmailTemplateConstants.ET_LINK, EncryptionHelper.encryptURL("opportunity|summary/" + opportunity.getObjectID()));
                values.put(EmailTemplateConstants.CRM.ASSIGNEE, assignee.getName());
                values.put(EmailTemplateConstants.CRM.CRM_ACCOUNT, opportunity.getCrmAccount() != null ? opportunity.getCrmAccount().getName() : "N/A");
                values.put(EmailTemplateConstants.DATE_CURRENT, dateFormat.format(new Date()));
                values.put(EmailTemplateConstants.CRM.OPPORTUNITY.NUMBER, opportunity.getNumber());
                values.put(EmailTemplateConstants.CRM.OPPORTUNITY.NAME, opportunity.getName());
                if (template != null) {
                    EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateItem(template, values, toEmail, user.getObjectID(), user.getEmail());
                    sendMessageFromUser(null, templateItem.getToEmail(), null, null, templateItem.getSubject(), templateItem.getMessageHTML(), false, null, null, false, null, null, user);
                } else {
                    String text = EdsTemplates.processTemplate(values, created ? EdsTemplates.OPPORTUNITY_CREATED : EdsTemplates.OPPORTUNITY_ASSIGNED);
                    System.out.println(text);
                    sendMessageFromUser(null, toEmail, null, null, subject, text, false, null, null, false, null, null, user);
                }

            }
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendToStudentsForCourseBookingConfirm(List<EdsCourseScheduleStudent> courseScheduleStudentList, Integer userID, String subject) {
        EdsUser user = userManager.get(userID);

        if (courseScheduleStudentList != null && courseScheduleStudentList.size() > 0) {
            for (EdsCourseScheduleStudent courseScheduleStudent : courseScheduleStudentList) {
                EmailTemplateItem templateItem = emailTemplateServiceLocal.generateStudentCourseBookingConfirm(courseScheduleStudent, user);
                String cc = null;
                EdsCourseBooking courseBooking = courseScheduleStudent.getCourseBooking();
                if (courseBooking.getContact() != null) {
                    cc = courseBooking.getContact().getPrimaryEmail();
                } else if (courseBooking.getCustomer() != null && courseBooking.getCustomer().getPrimaryContact() != null) {
                    EdsCrmContact contact = courseBooking.getCustomer().getPrimaryContact();
                    cc = contact.getPrimaryEmail();
                }

                try {
                    sendMessageFromUser("info@kg.om", templateItem.getToEmail(), cc, null, subject, templateItem.getMessageHTML(), false, "info@kg.om", null, false, null, null, user);
                    flush();
                } catch (EdsDbException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void sendToStudentsForScheduleCourseConfirm(EdsCourseScheduleStudent courseScheduleStudent, Integer userID) {
        EdsUser user = userManager.get(userID);
        if (courseScheduleStudent != null) {
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generateStudentCourseBookingConfirm(courseScheduleStudent, user);

            String cc = null;
            if (courseScheduleStudent.getCourseBooking().getContact() != null) {
                cc = courseScheduleStudent.getCourseBooking().getContact().getPrimaryEmail();
            }

            try {
                sendMessageFromUser("info@kg.om", templateItem.getToEmail(), cc, null, commonLocalizer.localize(KGONLINE_RESCHEDULED_COURSE_CONFIRMATION),
                        templateItem.getMessageHTML(), false, "info@kg.om", null, false, null, null, user);
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendScheduledTaskAsMail(EdsTCScheduledTask scheduledTask) {
        try {

            EdsUser user = userManager.get(scheduledTask.getUserID());
            SimpleDateFormat dateFormat = getCompanyShortDateFormat(user.getCompany());
            String customerName = clientManager.get(scheduledTask.getCustomerID()).getName();
            String period = dateFormat.format(user.getUserDate(scheduledTask.getPeriodStart())) + " - " + dateFormat.format(user.getUserDate(scheduledTask.getPeriodEnd()));
            String subject = commonLocalizer.localize(CONSOLIDATED_INVOICE_FOR) + " " + customerName + " " + commonLocalizer.localize(FOR_THE_PERIOD) + " " + period;

            File zipFile = new File(scheduledTask.getZipFileURL());
            FileInputStream fileInputStream = new FileInputStream(zipFile);
            EdsUpload upload = createUpload(fileInputStream, ServerUtils.normalizeFileName("Consolidated_Invoice_" + customerName + "_" + period) + ".zip", "application/zip");
            fileInputStream.close();

            Map<String, Object> values = new TreeMap<>();
            values.put("USER", user.getName());
            values.put("CUSTOMER", customerName);
            values.put("PERIOD", period);
            String text = EdsTemplates.processTemplate(values, EdsTemplates.CONSOLIDATED_INVOICE_MESSAGE);
            ArrayList<Integer> fileIds = new ArrayList<>();
            fileIds.add(upload.getObjectID());
            sendMessageFromUser(null, user.getEmail(), null, null, subject, text, true, user.getEmail(), fileIds, false, null, null, user);

            scheduledTask.setStatus(EdsTCScheduledTask.STATUS_COMPLETED);
            tcScheduledTaskManager.update(scheduledTask);

            if (zipFile.exists() && zipFile.delete()) {
                log.info("Zip file deleted");
            }
        } catch (IOException ex) {
            log.error("Unable to work with File Stream.", ex);
        } catch (EdsDbException | EdsTemplateException e) {
            e.printStackTrace();
        }
    }

    public SimpleDateFormat getCompanyShortDateFormat(EdsCompany company) {
        SimpleDateFormat shortDateFormat;
        if (company.getCompanySettings() != null && company.getCompanySettings().getShortDateFormat() != null) {
            shortDateFormat = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat());
        } else {
            shortDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        }
        return shortDateFormat;
    }

    public void sendRFPEmailRequest(MessageItem messageItem) {
        try {
            sendMessageFromUser(messageItem.getFromEmail(), messageItem.getToEmail(), messageItem.getCc(), messageItem.getBcc(), messageItem.getSubject(), messageItem.getMailContent(), false, null, null, false, null, null, userManager.getUser());
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPayslipToManager(EdsPayslipTable payslipTable) throws Exception {
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateSendPayslipToManagerEmailTemplate(payslipTable);
        if (templateItem != null) {
            try {
                sendMessageFromUser(null, templateItem.getToEmail(), null, null, templateItem.getSubject(), templateItem.getMessageHTML(), false, employeeManager.getUser().getEmail(), null, false, null, null, employeeManager.getUser());
                registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), employeeManager.getUser().getCompany().getObjectID());
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
        } else {
            final EdsCompany company = userManager.getUser().getCompany();
            final EdsEmployee creator = payslipTable.getPreparer();
            final EdsEmployee approver = payslipTable.getApprover();
            final Integer fromId = creator.getObjectID();
            final String from = creator.getEmail();
            final String to = approver.getEmail();
            String subject = commonLocalizer.localize(PAYSLIP_FOR) + " " + creator.getFullName();
            String url = EdsContextParams.getHost(company.getObjectID()) + "/Payroll.html?link=" + EncryptionHelper.encryptURL("payslipTable|summary/"
                    + payslipTable.getObjectID() + "/" + payslipTable.getStatus().getName()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(approver.getObjectID().toString());
            final Map<String, Object> values = new TreeMap<>();
            values.put("managername", approver != null ? approver.getName() : "");
            values.put("companyname", company != null ? company.getName() : "");
            values.put("creator", creator != null ? creator.getFullName() : "");
            values.put("date", formatDateShort(payslipTable.getCreationDate(), company));
            values.put("url", url);
            String text = EdsTemplates.processTemplate(values, EdsTemplates.PAYSLIP_SUMBIT_TO_MANAGER);
            Integer companyId = null;
            if (employeeManager.getUser() != null) {
                companyId = employeeManager.getUser().getCompany().getObjectID();
            } else if (creator != null) {
                companyId = creator.getCompany().getObjectID();
            }
            registerInternalMessageBasic(to, subject, text, companyId);
        }
    }

    @Override
    public void sendSinglePayrunToManager(EdsPayslipTableItem singlePayrun) throws Exception {
        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateSendSinglePayrunToManagerEmailTemplate(singlePayrun);
        if (templateItem != null) {
            try {
                sendMessageFromUser(null, templateItem.getToEmail(), null, null, templateItem.getSubject(), templateItem.getMessageHTML(), false, employeeManager.getUser().getEmail(), null, false, null, null, employeeManager.getUser());
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
        } else {
            final EdsCompany company = userManager.getUser().getCompany();
            final EdsEmployee creator = singlePayrun.getPreparer();
            final EdsEmployee approver = singlePayrun.getApprover();
            final EdsEmployee employee = singlePayrun.getEmployee();

            String subject = commonLocalizer.localize(PAYSLIP_FOR) + " " + employee.getFullName();
            String url = EdsContextParams.getHost(company.getObjectID()) + "/Payroll.html?link=" + EncryptionHelper.encryptURL("singlePayrun|viewPayslip/"
                    + singlePayrun.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(approver.getObjectID().toString());
            final Map<String, Object> values = new TreeMap<>();
            values.put("managername", approver.getName());
            values.put("companyname", company.getName());
            values.put("creator", creator.getFullName());
            values.put("employee", employee.getFullName());
            values.put("date", singlePayrun.getCreationDate() != null ? formatDateShort(singlePayrun.getCreationDate(), company) : "");
            values.put("url", url);
            String text = EdsTemplates.processTemplate(values, EdsTemplates.SINGLE_PAYRUN_SUBMIT_FOR_APPROVAL);
            sendMessageFromUser(null, approver.getEmail(), null, null, subject, text, false, employeeManager.getUser().getEmail(), null, false, null, null, employeeManager.getUser());
        }
    }

    @Override
    public void sendPayslipToEmployees(Integer payslipTableID, boolean sendNotification) throws Exception {
        for (EdsPayslipTableItem item : payslipTableItemManager.getPayslipTableItemsByTableID(payslipTableID)) {
            if (sendNotification && item.isApproved() && !item.isSendEmail()) {
                sendSinglePayrunToEmployee(item);
            }
        }
    }

    @Override
    public void sendPayslipToEmployees(Integer payslipTableID) throws Exception {
        for (EdsPayslipTableItem item : payslipTableItemManager.getPayslipTableItemsByTableID(payslipTableID, true)) {
            sendSinglePayrunToEmployee(item);
        }
    }

    @Override
    public void sendSinglePayrunToEmployee(EdsPayslipTableItem item) throws Exception {
        PayslipTableRequestObject requestObject;
        requestObject = payrollService.getSinglePayrunPdfData(item.getObjectID());
        EdsUser user = employeeManager.getUser();
        EdsEmployee employee = item.getEmployee();
        EdsEmployee approver = item.getApprover();
        EdsCompany company = userManager.getUser().getCompany();
        String subject = commonLocalizer.localize(PAYSLIP_FOR) + " " + item.getMonth();

        String originalName = "Payslip_for_" + item.getMonth() + ".pdf";
        Calendar cal = Calendar.getInstance();
        cal.setTime(item.getCreationDate());
        Integer year = cal.get(Calendar.YEAR);
        final Map<String, Object> values = new TreeMap<>();
        values.put("USER", employee);
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.EMP_FIRST_NAME, StringUtils.capitalize(employee.getFirstName()));
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.EMP_LAST_NAME, employee.getLastName());
        values.put("managername", approver != null ? approver.getName() : "");
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.APPROVER_FIRST_NAME, approver != null ? StringUtils.capitalize(approver.getFirstName()) : "");
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.APPROVER_LAST_NAME, approver != null ? approver.getLastName() : "");
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.APPROVED_DATE, item.getApprovedDate() != null ? formatDateShort(item.getApprovedDate(), company) : "");
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.MONTH, item.getMonth());
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.YEAR, year);

        EdsEmailTemplate template = emailTemplateManager.getDefaultEmailTemplateByCategory(PAYSLIP_APPROVED_TO_EMPLOYEE);
        if (template != null && template.getFromEmail() != null && !StringUtils.isEmpty(template.getFromEmail())) {
            EdsUser tUser = userManager.get(template.getFromUser());
            EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateItem(template, values, employee.getEmail(), user.getObjectID(), user.getEmail());
            String fromUserName = templateItem.getFromUserName() != null && !StringUtils.isEmpty(templateItem.getFromUserName()) ? templateItem.getFromUserName() : tUser != null ? tUser.getFullName() : user.getFullName();
            String fromEmail = template.getFromEmail();
            sendMessageFromUser(originalName, fromEmail, employee.getEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), user, fromUserName, singlePayrunPdfHandler.getPDFStream(requestObject));
        } else {
            String text = EdsTemplates.processTemplate(values, EdsTemplates.PAYSLIP_APPROVED_TO_EMPLOYEE);
            sendMessageFromUser(originalName, employee.getEmail(), subject, text, user, singlePayrunPdfHandler.getPDFStream(requestObject));
        }
        item.setSendEmail(true);
        payslipTableItemManager.update(item);
    }

    public Boolean isArabicCompany() {
        EdsCompany company = userManager.getUser().getCompany();
        String companyCode = company.getCountryZone().getCountry().getCode();
        return ("AE".equals(companyCode) || "SA".equals(companyCode) || "OM".equals(companyCode));
    }

    @Override
    public void sendCashAdvanceRequestToApprover(EdsCashAdvance cashAdvance, EdsCurrency currency) throws Exception {

        NumberToWord numberToWordConverter = new NumberToWord_en();
        String totalWord = numberToWordConverter.toWord(cashAdvance.getTotalAmount());
        EdsCompany company = userManager.getUser().getCompany();

        String url = EdsContextParams.getHost(company.getObjectID()) + "/Payroll.html?link=" + EncryptionHelper.encryptURL("cashAdvance|summary/view/"
                + cashAdvance.getObjectID() + "/" + cashAdvance.getStatus().getCode()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(cashAdvance.getCurrentApprover().getExactEmployee().getObjectID().toString());

        final Map<String, Object> values = new TreeMap<>();
        values.put("managername", cashAdvance.getCurrentApprover().getExactEmployee().getFullName());
        values.put("currencyname", currency != null ? currency.getName() : "");
        values.put("currencysign", currency != null ? currency.getSymbol() != null ? currency.getSymbol() : currency.getName() : "");
        values.put("requestedamount", cashAdvance.getTotalAmount().setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        values.put("purpose", cashAdvance.getPurpose());
        values.put("totalword", totalWord);
        values.put("url", url);
        values.put("requestername", cashAdvance.getEmployee() != null ? cashAdvance.getEmployee().getFullName() : "I");


        String subject = "Cash Advance Request";
        String text = EdsTemplates.processTemplate(values, EdsTemplates.PAYSLIP_CASH_ADVANCED_TO_MANAGER);
        sendMessageFromUser(null, cashAdvance.getCurrentApprover().getExactEmployee().getEmail(), null, null, subject, text, false, employeeManager.getUser().getEmail(), null, false, null, null, employeeManager.getUser());
    }

    @Override
    public void sendCashAdvanceRejectMessageToEmployee(EdsCashAdvance cashAdvance) throws Exception {
        final Map<String, Object> values = new TreeMap<>();
        values.put("username", cashAdvance.getEmployee().getFullName());
        values.put("managername", cashAdvance.getCurrentApprover().getExactEmployee().getFullName());

        String subject = "Cash Advance Rejected";
        String text = EdsTemplates.processTemplate(values, EdsTemplates.PAYSLIP_CASH_ADVANCED_REJECT_TO_EMPLOYEE);
        sendMessage(cashAdvance.getEmployee().getEmail(), subject, text, null, false, null, null, null);
    }

    @Override
    public void sendAdditionalPaymentToApprover(EdsAdditionalPayment additionalPayment) throws Exception {
        final EdsCompany company = userManager.getUser().getCompany();
        final EdsEmployee creator = additionalPayment.getCreator();
        final EdsUser approver = additionalPayment.getCurrentApprover().getExactEmployee();

        String subject = commonLocalizer.localize(PAYSLIP_FOR) + " " + additionalPayment.getMonth();
        String url = EdsContextParams.getHost(company.getObjectID()) + "/Payroll.html?link=" + EncryptionHelper.encryptURL("additionalPayment|view/"
                + additionalPayment.getObjectID()) + "/" + additionalPayment.getOverallStatus().getName()
                + "&" + U_ID + "=" + EncryptionHelper.encryptURL(approver.getObjectID().toString());
        final Map<String, Object> values = new TreeMap<>();
        values.put("managername", approver.getName());
        values.put("companyname", company.getName());
        values.put("creator", creator.getFullName());
        values.put("date", additionalPayment.getCreationDate() != null ? formatDateShort(additionalPayment.getCreationDate(), company) : "");
        values.put("url", url);
        String text = EdsTemplates.processTemplate(values, EdsTemplates.ADDITIONAL_PAYMENT_TO_APPROVER);
        sendMessageFromUser(null, approver.getEmail(), subject, text, null, null, false, employeeManager.getUser().getEmail(), null, false, null, null, employeeManager.getUser());
    }

    @Override
    public void sendAdditionalPaymentToEmployee(EdsEmployee employee, EdsAdditionalPayment additionalPayment) throws Exception {
        AdditionalPaymentRequestObject requestObject = new AdditionalPaymentRequestObject(additionalPayment.getObjectID(), employee.getObjectID());
        EdsUser user = employeeManager.getUser();
        EdsUser approver = additionalPayment.getCurrentApprover().getExactEmployee();
        EdsCompany company = userManager.getUser().getCompany();
        String subject = "";
        String originalName = "";
        if (BY_COMMISION_TYPE.equals(additionalPayment.getType())) {
            subject = commonLocalizer.localize(COMMISSION_FOR) + " " + (additionalPayment.getMonth() != null ? additionalPayment.getMonth() : "");
            originalName = "Commission_for_" + (additionalPayment.getMonth() != null ? additionalPayment.getMonth() : "") + ".pdf";
        } else {
            subject = commonLocalizer.localize(PAYMENT_FOR) + " " + (additionalPayment.getMonth() != null ? additionalPayment.getMonth() : "");
            originalName = "Payment_for_" + (additionalPayment.getMonth() != null ? additionalPayment.getMonth() : "") + ".pdf";
        }

        final Map<String, Object> values = new TreeMap<>();
        values.put("USER", employee);
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.EMP_FIRST_NAME, StringUtils.capitalize(employee.getFirstName()));
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.EMP_LAST_NAME, employee.getLastName());
        values.put("managername", approver != null ? approver.getName() : "");
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.APPROVER_FIRST_NAME, approver != null ? StringUtils.capitalize(approver.getFirstName()) : "");
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.APPROVER_LAST_NAME, approver != null ? approver.getLastName() : "");
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.MONTH, additionalPayment.getMonth());
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.YEAR, additionalPayment.getYear());
        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.TYPE, additionalPayment.getType());

        String text = EdsTemplates.processTemplate(values, EdsTemplates.ADDITIONAL_PAYMENT_TO_EMPLOYEE);
        sendMessageFromUser(originalName, employee.getEmail(), subject, text, user, additionalPaymentItemPdfHandler.getPDFStream(requestObject));
    }

    @Override
    public void sentActualTimeReachedNotifation(EdsTask task) throws EdsDbException {
        try {
            boolean isSentEmail = false;
            List<EdsEmployee> backupManagers = task.getProject().getBackupManagers();
            backupManagers.add(task.getProject().getManager());
            for (EdsEmployee backupManager : backupManagers) {
                EdsUser user = userManager.getUser();
                EdsUser receiver = userManager.get(backupManager.getObjectID());
                Integer companyID = user.getCompany().getObjectID();
                boolean existUserNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(backupManager.getObjectID(), ACTUAL_TIME_REACHED_TO_ESTIMATED);
                if (!existUserNotificationSettings) {
                    return;
                }
                EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateItemForActualTimeReached(user, receiver, task);
                if (templateItem != null) {
                    registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), companyID);
                    isSentEmail = true;
                } else {
                    String to = receiver.getEmail();
                    String subject = task.getName() + " - actual time reached to estimated";
                    Map<String, Object> values = new TreeMap<>();
                    values.put("recipientName", receiver.getFullName());
                    values.put("taskNumber", task.getNumber());
                    values.put("taskName", task.getName());
                    values.put("date", formatDate(receiver.getUserDate(receiver.getCompany().getCompanyDate()), receiver.getCompany()));
                    values.put("host", EdsContextParams.getHost(companyID));
                    String taskURL = EdsContextParams.getHost(companyID) + "/ProjectManagement.html?link=" + EncryptionHelper.encryptURL("task|summary/" + task.getObjectID() + "/true") + "&" + U_ID + "=" + EncryptionHelper.encryptURL(receiver.getObjectID().toString()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(companyID.toString());
                    values.put("link", taskURL);
                    String text = EdsTemplates.processTemplate(user, values, EdsTemplates.TASK_ACTUAL_TIME_REACHED);
                    if (!receiver.getDeleted()) {
                        registerInternalMessage(to, subject, text, null, false, null, false, null, companyID);
                        isSentEmail = true;
                    }
                }
            }
            if (isSentEmail) {
                task.setSentActualTimeReachedNotifation(true);
//                taskManager.update(task);
            }

        } catch (EdsTemplateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setHrReminderNotification(EdsEmailTemplate emailTemplate, String fieldValue, String reminderdate, EdsUser user, List<EdsUser> employees, List<EdsUser> recipents, Integer companyId) {
        ArrayList<Integer> sentUsersId = new ArrayList<>();
        for (EdsUser receiver : recipents) {
            if (receiver.getAccountStatus() != null && !EMPLOYEE_STATUS_RESIGNED.equals(receiver.getAccountStatus().getCode())) {
                if (!sentUsersId.contains(receiver.getObjectID())) {
                    sentUsersId.add(receiver.getObjectID());
                    EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateItemForHrReminder(user, receiver, emailTemplate, fieldValue, reminderdate, employees, companyId);
                    if (templateItem != null) {
                        try {
                            if (templateItem.getSubject().contains("Your ")) {
                                if (employees.contains(receiver)) {
                                    registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), companyId);
                                }
                            } else {
                                registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), companyId);
                            }
                        } catch (EdsDbException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setHrReminderNotificationTeamLeaderSpesific(EdsEmailTemplate emailTemplate, String fieldValue, String reminderdate, EdsUser user, Map<Integer, ArrayList<EdsUser>> teamLEaderMap, Integer companyId) {
        ArrayList<Integer> sentUsersId = new ArrayList<>();
        for (Integer receiverID : teamLEaderMap.keySet()) {
            EdsUser receiver = userManager.get(receiverID);
            ArrayList<EdsUser> employees = teamLEaderMap.get(receiverID);
            if (receiver.getAccountStatus() != null && !EMPLOYEE_STATUS_RESIGNED.equals(receiver.getAccountStatus().getCode())) {
                if (!sentUsersId.contains(receiver.getObjectID())) {
                    sentUsersId.add(receiver.getObjectID());
                    EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateItemForHrReminder(user, receiver, emailTemplate, fieldValue, reminderdate, employees, companyId);
                    if (templateItem != null) {
                        try {
                            if (templateItem.getSubject().contains("Your ")) {
                                if (employees.contains(receiver)) {
                                    registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), companyId);
                                }
                            } else {
                                registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), companyId);
                            }
                        } catch (EdsDbException e) {
                        }
                    }
                }
            }
        }
    }

    @Override
    public void sendGoalAssignNotification(EdsGoal goal, EdsEmployee employee, List<EdsEmployee> goalAssigns) throws EdsDbException {
        String type = PERSONAL_GOAL_ASSIGN_CATEGORY;
        String edsSubject = EdsSubjects.PERSONAL_GOAL_ASSIGN_NOTIFICATION;
        String edsTemplate = EdsTemplates.ASSIGN_TO_DEPARTMENT_GOAL;
        if (goal.getGoalCategory() != null) {
            if (DEPARTMENT_GOAL.toLowerCase().equals(goal.getGoalCategory().getCode())) {
                type = DEPARTMENT_GOAL_ASSIGN_CATEGORY;
                edsSubject = EdsSubjects.DEPARTMENT_GOAL_ASSIGN_NOTIFICATION;
                edsTemplate = EdsTemplates.ASSIGN_TO_DEPARTMENT_GOAL;
            } else if (PROJECT_GOAL.toLowerCase().equals(goal.getGoalCategory().getCode())) {
                type = PROJECT_GOAL_ASSIGN_CATEGORY;
                edsSubject = EdsSubjects.PROJECT_GOAL_ASSIGN_NOTIFICATION;
                edsTemplate = EdsTemplates.ASSIGN_TO_PROJECT_GOAL;
            } else if (BUSINESS_GOAL.toLowerCase().equals(goal.getGoalCategory().getCode())) {
                type = BUSINESS_GOAL_ASSIGN_CATEGORY;
                edsSubject = EdsSubjects.BUSINESS_GOAL_ASSIGN_NOTIFICATION;
                edsTemplate = EdsTemplates.ASSIGN_TO_BUSINESS_GOAL;
            }

        }

        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateForPersonalGoal(goal, employee, type, goalAssigns);
        EdsUser user = goal.getCreator();
        Integer companyID = user.getCompany().getObjectID();
        EdsCompany company = companyManager.get(companyID);
        String goalSubject = goal.getName();
        if (templateItem != null) {
            registerInternalMessage(templateItem.getToEmail(), (templateItem.getSubject() + goalSubject), templateItem.getMessageHTML(), user.getEmail(), false, null, false, null, companyID);
        } else {
            try {
                String companyid = EncryptionHelper.encryptURL(companyID.toString());
                String to = employee.getEmail();
                String subject = commonLocalizer.localize(edsSubject) + " " + goalSubject;
                Map<String, Object> values = new TreeMap<>();
                values.put("host", EdsContextParams.getHost(user.getCompany().getObjectID()));
                values.put("employeeName", employee.getFullName());
                values.put("creator", user.getFullName());
                values.put("date", formatDate(new Date(), company));
                values.put("goalName", goal.getTitle());
                values.put("description", goal.getDescription());
                values.put("status", goal.getStatus() != null ? goal.getStatus().getName() : "");
                values.put("startdate", formatDateShort(goal.getFromDate(), company));
                values.put("duedate", formatDateShort(goal.getToDate(), company));
                values.put("validityPeriod", goal.getValidityPeriod() != null ? goal.getValidityPeriod().getName() : "");
                values.put("measurementUnit", goal.getMeasurementUnit() != null ? goal.getMeasurementUnit().getName() : "");
                values.put("actionSteps", goal.getActionSteps());
                StringBuilder empList = new StringBuilder();
                for (EdsEmployee emp : goalAssigns) {
                    empList.append(empList.toString() == "" ? "" : ", ").append(emp.getFullName());
                }
                values.put("assignees", empList.toString());
                values.put("link", EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("goal/" + goal.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString()) + "&" + C_ID + "=" + companyid);
                String text = EdsTemplates.processTemplate(user, values, edsTemplate);
                registerInternalMessage(to, subject, text, user.getEmail(), false, null, false, null, companyID);

            } catch (EdsTemplateException tex) {
                throw new EdsDbException(tex);
            }
        }
    }

    @Override
    public void sendProductStockNotification(EdsItem item, EdsUser user, Integer companyId, List<String[]> items) throws EdsDbException {
        String edsTemplate = EdsTemplates.PRODUCT_STOCK_NOTIFICATION;
        StringBuilder sb = new StringBuilder();
        for (String[] itemStock : items) {
            sb.append("Item Number: ").append(itemStock[0]).append("<br>");
            sb.append("Item Name: ").append(itemStock[1]).append("<br>");
            sb.append("Description: ").append(itemStock[2]).append("<br>");
            sb.append("Stock Amount: ").append(itemStock[3]).append("<br>");
            sb.append("Warehouse: ").append(itemStock[4]).append("<br><br>");
        }

        EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateForProductStock(item, user, sb.toString());
        String itemSubject = item.getName();
        log.info("START COMPANY REORDER POINT INFO: CompanyID=" + companyId + "--- UserID=" + (user != null ? user.getObjectID() : "") + " --- " + (user != null ? user.getEmail() : ""));
        if (templateItem != null) {
            registerInternalMessage(templateItem.getToEmail(), (templateItem.getSubject() + (itemSubject != null ? itemSubject : "")), templateItem.getMessageHTML(), user.getEmail(), false, null, false, null, companyId);
        } else {
            try {
                String to = user.getEmail();
                String subject = commonLocalizer.localize(EdsSubjects.PRODUCT_STOCK_NOTIFICATION) + " " + item.getName();
                Map<String, Object> values = new TreeMap<>();
                values.put(EmailTemplateUtils.ET_USER_NAME, StringUtils.capitalize(user.getFirstName()));
                values.put(EmailTemplateUtils.ET_ITEMS, items);
                String goalURL = EdsContextParams.getHost(user.getCompany().getObjectID()) + "/Accounting.html";
                values.put(EmailTemplateUtils.ET_URL, goalURL);
                String text = EdsTemplates.processTemplate(user, values, edsTemplate);
                registerInternalMessage(to, subject, text, user.getEmail(), false, null, false, null, companyId);

            } catch (EdsTemplateException tex) {
                throw new EdsDbException(tex);
            }
        }
        log.info("END COMPANY REORDER POINT INFO: CompanyID=" + companyId + "--- UserID=" + (user != null ? user.getObjectID() : "") + " --- " + (user != null ? user.getEmail() : ""));
    }

    @Override
    public void sendBenefitRequestNotification(String s, Integer requestID, Integer userID) {
        log.info("***************** Generating Benefit Request Notification to Send ********************");
        EdsBenefitRequest request = benefitRequestManager.get(requestID);
        EdsUser updater = userManager.get(userID);
        EdsUser recepient = null;
        String template = null;
        String toEmail = request.getRequester() != null ? request.getRequester().getEmail() : null;
        String subject = "Benefit Request Notification";
        String link = "";
        if (BenefitRequestEventListenerImpl.BR_SUBMITTED.equals(s)) {
            template = EdsTemplates.BENEFIT_REQUEST_SUBMITTED;
            recepient = request.getApprover();
            subject = request.getBenefit().getName() + " request notification";
        } else if (BenefitRequestEventListenerImpl.BR_APPROVED.equals(s)) {
            template = EdsTemplates.BENEFIT_REQUEST_APPROVED;
            recepient = request.getRequester();
            subject = request.getBenefit().getName() + " request approval notification";
        } else if (BenefitRequestEventListenerImpl.BR_REJECTED.equals(s)) {
            template = EdsTemplates.BENEFIT_REQUEST_REJECTED;
            recepient = request.getRequester();
            subject = request.getBenefit().getName() + " request rejection notification";
        }
        Map<String, Object> values = new TreeMap<>();
        values.put("recepient", recepient != null ? recepient.getName() : "");
        values.put("benefitType", request.getBenefit() != null ? request.getBenefit().getName() : "");
        EdsReference qtyType = request.getBenefit().getQtytype();
        EdsCurrency currency = request.getBenefit().getCurrency();
        values.put("unitType", qtyType != null ? "CURRENCY".equals(qtyType.getCode()) ? currency != null ? currency.getName() : "" : qtyType.getName() : "");
        if ("CURRENCY".equals(qtyType.getCode())) {
            values.put("requestedQuantity", Utils.formatDouble(request.getRequestedQuantity()));
        } else if (request.getRequestedQuantity() != null) {
            values.put("requestedQuantity", request.getRequestedQuantity().intValue());
        }
        values.put("description", request.getDescription());
        values.put("date", request.getDate() != null ? formatDateShort(request.getDate(), recepient.getCompany()) : "");
        values.put("requester", request.getRequester() != null ? request.getRequester().getName() : "");
        values.put("rejector", updater.getName() != null ? updater.getName() : "");
        values.put("rejectionReason", request.getRejectionReason() != null ? request.getRejectionReason() : "");
        if (recepient != null) {
            link = EdsContextParams.getHost(recepient.getCompany().getObjectID()) + "/Hrms.html?link=" + EncryptionHelper.encryptURL("benefitRequest|add/" + request.getObjectID()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(recepient.getCompany().getObjectID().toString());
        }
        if (recepient != null) {
            toEmail = recepient.getEmail();
        }
        values.put("link", link);
        try {
            log.debug("******************* Sending Benefit Request Email now ********************************");
            String text = EdsTemplates.processTemplate(updater, values, template);
            if (toEmail != null && !"".equals(toEmail.trim())) {
                sendMessage(toEmail, subject, text, EdsContextParams.getSupportEmail(), false, null, null, null);
            }
            log.debug("******************** Benefit Request Email sent successfully ******************");
        } catch (EdsTemplateException e) {
            log.info("***************** To: " + toEmail + " *************************");
            e.printStackTrace();
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendToAddNoteMessage(HistoryListItem item, EdsUser user, EdsNoteHistory noteHistory, EdsEmployee employee) {
        String subject = item.getSubject();
        EdsCompany company = user.getCompany();

        Map<String, Object> values = new HashMap<>();
        values.put("userTo", employee.getName());
        values.put("creatorNote", user.getName());
        values.put("taskOrProject", item.getRelatedToName() != null && !"".equals(item.getRelatedToName()) ? item.getRelatedToName() : "");
        values.put("createdDate", formatDate(user.getUserDate(company.getCompanyDate()), user.getCompany()));
        values.put("noteBody", noteHistory.getComment());
        String link = EncryptionHelper.encryptURL("notelist|summary/" + noteHistory.getObjectID() + "/" + employee.equals(noteHistory.getEmployee())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(employee.getCompany().getObjectID().toString());
        values.put("link", link);
        values.put("host", EdsContextParams.getHost(company.getObjectID()));

        String updatedMessage = null;
        try {
            updatedMessage = EdsTemplates.processTemplate(values, EdsTemplates.SEND_ADD_NOTE_MESSAGE);
        } catch (EdsTemplateException e) {
            e.printStackTrace();
        }
        try {
            if (!employee.getDeleted() && !user.getDeleted()) {
                registerInternalMessageBasic(employee.getEmail(), subject, updatedMessage, user.getCompany().getObjectID());
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendProjectClientSubmitMessage(EdsUser user, CrmAccountItem client, Map<String, String> emails, EdsProject project, String url) {
        String toEmail = emails.get("TO_EMAIL");
        String toName = emails.get("TO_NAME");
        String ccEmails = emails.get("CC_EMAIL");
        String subject = project.getName() + " " + commonLocalizer.localize(PROJECT_SUBMIT_FOR_APPROVAL);
        Map<String, Object> values = new HashMap<>();
        values.put("projectnumber", project.getNumber());
        values.put("projectname", project.getName());
        values.put("name", client.getName());
        values.put("recipient", toName);
        values.put("creator", user.getFullName());
        values.put("date", new Date());
        values.put("url", url);

        EdsEmailSetting emailSetting = emailSettingsManager.getCompanyEmailSetting(user.getCompany().getObjectID());
        String fromName = "Genesis Gifts";
        if (emailSetting != null && emailSetting.getFromName() != null && emailSetting.getFromName().length() != 0) {
            fromName = emailSetting.getFromName();
        }
        try {
            String content = EdsTemplates.processTemplate(user, values, EdsTemplates.PROJECT_SUBMIT_TO_CLIENT);
            sendMessageFromUser(fromName, toEmail, ccEmails, null, subject, content, false, null, null, false, null, null, user);
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendProjectClientApprovalMessage(EdsUser user, CrmAccountItem client, EdsProject project, List<EdsCrmContact> contactList, List<Integer> employees, String description, String url, String template) {
        EdsCrmContact contact = contactList.get(0);
        String subject = project.getName() + " ";
        ActionOnEntityEnum actionOnEntityEnum;
        if (EdsTemplates.PROJECT_CLIENT_APPROVE.equals(template)) {
            subject = subject.concat(commonLocalizer.localize(PROJECT_APPROVED_BY_CLIENT));
            actionOnEntityEnum = ActionOnEntityEnum.APPROVED;
        } else {
            subject = subject.concat(commonLocalizer.localize(PROJECT_REJECTED_BY_CLIENT));
            actionOnEntityEnum = ActionOnEntityEnum.REJECTED;
        }
        Map<String, Object> values = new HashMap<>();
        values.put("projectnumber", project.getNumber());
        values.put("projectname", project.getName());
        values.put("name", client.getName());
        values.put("recipient", contact.getFullName());
        values.put("description", description);
        values.put("creator", user.getFullName());
        values.put("date", new Date());
        values.put("url", url);
        try {
            String content = EdsTemplates.processTemplate(user, values, template);
            sendMessage(user.getEmail(), subject, content, null, false, null, null, user.getCompany().getObjectID());

            for (Integer employee : employees) {
//                log.info("|============================" + NotificationTypeEnum.ProjectApproval.name() + "============================|");
                notificationMsgManager.createProjectApprovalNotificationEvent(project.getObjectID(), employee, employee, actionOnEntityEnum);
            }
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmployeeTemplateToApprover(EdsEmployeePayrollSettingsTemplate employeePayrollSettingsTemplate) throws Exception {

        EdsCompany company = userManager.getUser().getCompany();

        String url = EdsContextParams.getHost(company.getObjectID()) + "/Payroll.html?link=" + EncryptionHelper.encryptURL("starter|summary/"
                + employeePayrollSettingsTemplate.getObjectID() + "/fromTemplate/view/Submitted") + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeePayrollSettingsTemplate.getApprover().getObjectID().toString());

        final Map<String, Object> values = new TreeMap<>();
        values.put("managername", employeePayrollSettingsTemplate.getApprover().getFullName());
        values.put("sender", employeePayrollSettingsTemplate.getSender().getFullName());
        values.put("employee", employeePayrollSettingsTemplate.getFullName());
        values.put("url", url);

        String subject = "Employee Payroll Settings Request";

        String text = EdsTemplates.processTemplate(values, EdsTemplates.PAYSLIP_EMPLOYEE_TEMPLATE_TO_MANAGER);
        sendMessageFromUser(null, employeePayrollSettingsTemplate.getApprover().getEmail(), null, null, subject, text,
                false, employeePayrollSettingsTemplate.getSender().getEmail(), null, false, null, null, employeePayrollSettingsTemplate.getSender());
    }

    @Override
    public void sendImportReportMessage(Integer importFileID) {
        EdsImportFile importFile = importFileManager.get(importFileID);
        if (importFile.getOwner() != null && StringUtils.isNotBlank(importFile.getOwner().getEmail())) {
            String to = importFile.getOwner().getName() + "<" + importFile.getOwner().getEmail() + ">";
            String subject = importFile.getType() != null ? importFile.getType().getCode() + " imported successfully" : "Imported Sucessfully";
            Map<String, Object> values = new HashMap<>();
            values.put("USER_NAME", importFile.getOwner().getName());
            values.put("IMPORT_TYPE", importFile.getType() != null ? importFile.getType().getCode() : "");
            values.put("PRODUCT_NAME", EdsContextParams.getProductName());
            Date importDate = ServerUtils.getCompanyDate(importFile.getCreatedDate() != null ? importFile.getCreatedDate() : new Date(), importFile.getOwner().getUserTimezone().getID());
            values.put("IMPORT_DATE", ServerUtils.longDateFormat(importDate, importFile.getOwner().getCompany()));
            values.put("SUPPORT", EdsContextParams.getSupportEmail());
            values.put("REQUESTED_COUNT", importFile.getCsvColumns() != null ? importFile.getCsvColumns() : 0);
            values.put("IMPORTED_COUNT", importFile.getImportedColumns() != null ? importFile.getImportedColumns() : 0);
            values.put("IGNORED_COUNT", importFile.getIgnoredColumns());
            values.put("SKIPPED_COUNT", importFile.getSkippedColumns());
            values.put("OVERWRITTEN_COUNT", importFile.getOverwrittenColumns());
            List<Integer> fileIDs = new ArrayList<>();
            if (importFile.getRejectedRecords() != null) {
                fileIDs.add(importFile.getRejectedRecords().getObjectID());
            }
            try {
                String content = EdsTemplates.processTemplate(values, EdsTemplates.IMPORT_CUSTOM_EVENT);
                registerInternalMessage(to, subject, content, null, true, fileIDs, false, null, SecurityContext.getCompanyID());
            } catch (Exception e) {
                log.error("_-+-_ _-+-_ _-+-_ _-+-_ _-+-_ _-+-_ _-+-_ SEND 'IMPORTED MESSAGE' FAILED _-+-_ _-+-_ _-+-_ _-+-_ _-+-_ _-+-_ _-+-_ ");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendNewsNotificationByLocation(EdsNews news, Integer updaterID, Integer userID, String subject) {
        EdsUser updater = userManager.get(updaterID);
        EdsUser recepient = userManager.get(userID);
        String template;
        String link = "", toEmail = "";
        if (subject == "News create notification") {
            template = EdsTemplates.NEWS_NOTIFICATION;
        } else {
            template = EdsTemplates.NEWS_UPDATE_NOTIFICATION;
        }

        Map<String, Object> values = new TreeMap<>();
        values.put("recepient", recepient != null ? recepient.getName() : "");
        values.put("date", news.getDate() != null ? formatDateShort(news.getDate(), recepient.getCompany()) : "");
        values.put("updater", updater != null ? updater.getName() : "");
        values.put("news_subject", news.getSubject());
        if (recepient != null) {
            link = EdsContextParams.getHost(recepient.getCompany().getObjectID()) + "/Hrms.html?link=" + EncryptionHelper.encryptURL("news|summary/" + news.getObjectID()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(recepient.getCompany().getObjectID().toString());
        }
        if (recepient != null) {
            toEmail = recepient.getEmail();
        }
        values.put("link", link);
        try {
            String text = EdsTemplates.processTemplate(updater, values, template);
            if (toEmail != null && !"".equals(toEmail.trim())) {
                sendMessage(toEmail, subject, text, updater.getEmail(), false, null, null, null);
            }
        } catch (EdsTemplateException | EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendOffice365EmailVerification(String email, String url) {
        String subject = "Verify your Office 365 Email";
        String serviceid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(SpringPropertiesUtil.getProperty("kpi.discovery.service-id")));

        Map<String, Object> context = new TreeMap<>();
        context.put("url", url);
        context.put("serviceid", serviceid);
        try {
            String requestText = EdsTemplates.processTemplate(context, EdsTemplates.FORGOT_WITH_ACTIVATION_LINK);
            this.sendMessage(email, subject, requestText, EdsContextParams.getSupportEmail(), false, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessageToOwnerAutoSync(Integer userID) {
        if (userID != null) {
            EdsUser user = userManager.get(userID);
            if (user != null) {
                Map<String, Object> context = new TreeMap<>();
                context.put("USER_NAME", user.getName());
                try {
                    String requestText = EdsTemplates.processTemplate(context, EdsTemplates.WARNING_YOUR_CALENDAR_AUTO_SYNC);
                    this.sendMessage(user.getEmail(), "Your calendar auto-sync failed", requestText, EdsContextParams.getSupportEmail(), false, null, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void sendWorkflowRecurrenceRunNotification(EdsWorkflowRule workflowRule) {
        String toEmail = workflowRule.getCreator().getEmail();
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("CREATOR", workflowRule.getCreator().getName());
        valueMap.put("NAME", workflowRule.getName());
        valueMap.put("MODULE", workflowRule.getModuleAsString());
        try {
            String requestText = EdsTemplates.processTemplate(valueMap, EdsTemplates.RECURRENCE_WORKFLOW_RUN);
            sendMessage(toEmail, "Recurrence Workflow Automation has run", requestText, EdsContextParams.getSupportEmail(), false, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sentEmailAccountInactivationEmail(String email, String userName) {
        EdsMessage message = new EdsMessage();
        message.setStatus(MessageStatusEnum.PENDING);
        message.setType(MessageTypeEnum.PREFERRED);
        message.setAttempts(0);
        message.setCreationDate(new Date());
        message.setSubject("Message Center email account is Inactive");
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("EMAIL", email);
        valueMap.put("USER", userName);
        valueMap.put("HOST", EdsContextParams.getHost(SecurityContext.getCompanyID()));
        try {
            message.setText(EdsTemplates.processTemplate(valueMap, EdsTemplates.EMAIL_ACCOUNT_INACTIVATION));
        } catch (EdsTemplateException e) {
            message.setText("Your email account <b>(" + email + ")</b> has inactivated due to connection error with your email server. Please check your account settings.");
        }
        message.setTo(email);
        message.setCc(EdsContextParams.getSupportEmail());
        message.setCompanyID(SecurityContext.getCompanyID());
        create(message);
    }

    @Override
    public void sendBillOfMaterialsNotification(EdsUser creator, EdsProject project, String status) {
        List<EdsEmployee> employees = null;
        if (APPROVED.equals(status) || REJECTED.equals(status)) {
            employees = employeeManager.getEmployeesByPermissionCode(PermissionConstants.BILL_OF_MATERIAL_SUBMIT_TO_MANAGER);
        } else if (SUBMITTED_TO_MANAGER.equals(status)) {
            employees = employeeManager.getEmployeesByPermissionCode(PermissionConstants.BILL_OF_MATERIAL_APPROVE_REJECT);
        }
        if (employees == null || employees.isEmpty()) return;
        employees.stream()
                .filter(x -> !x.getObjectID().equals(creator.getObjectID()))
                .forEach(x -> {
                    EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateItemForBillOfMaterials(creator, project, status, x);
                    if (templateItem != null) {
                        try {
                            registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), SecurityContext.getCompanyID());
                        } catch (EdsDbException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void sendStripeWebhookPaymentNotification(UsagePlanItem usagePlanItem, UsagePlanPrice usagePlanPrice) {
        log.info("======================== Send Stripe Payment Notification ===============================");
        log.info("User ID:" + usagePlanItem.getUserId());
        log.info("User Name:" + usagePlanItem.getUserName());
        log.info("User Email (to Email):" + usagePlanItem.getUserEmail());
        String subject = "";
        if (PaymentTypeEnum.STRIPE.getCode().equals(usagePlanItem.getPaymentType().getCode())) {
            subject = usagePlanItem.getUsageMonth() == 1 ? "Monthly Stripe Payment Notification" : "Yearly Stripe Payment Notification";
        } else {
            subject = commonLocalizer.localize(PAYPAL_PAYMENT_SUBJECT);
        }
        if (StringUtils.isNotBlank(usagePlanItem.getCompanyName())) {
            subject += " - ";
            subject += usagePlanItem.getCompanyName();
        }
        String bcc = "support@kpi.com,sales@kpi.com,munir@kpi.com,anvar@kpi.com";

        int appCount = 0;
        StringBuilder apps = new StringBuilder();
        if (usagePlanItem.isAccountsModule()) {
            apps.append("Accounting").append(", ");
            appCount++;
        }
        if (usagePlanItem.isSalesModule()) {
            apps.append("Sales").append(", ");
            appCount++;
        }
        if (usagePlanItem.isHumansModule()) {
            apps.append("Humans").append(", ");
            appCount++;
        }
        if (usagePlanItem.isProjectModule()) {
            apps.append("Projects").append(", ");
            appCount++;
        }
        if (usagePlanItem.isPayrollModule()) {
            apps.append("Payroll").append(", ");
            appCount++;
        }
        apps.deleteCharAt(apps.lastIndexOf(","));

        Map<String, Object> context = new HashMap<>();

        context.put(EmailTemplateConstants.PAYMENT_VALUES.CUSTOMER, usagePlanItem.getUserName());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.INVOICE_TOTAL_AMOUNT, ServerUtils.decimalPrecision(usagePlanPrice.getTotalSubscription() + usagePlanPrice.getAddonPrice(), 2));
        context.put(EmailTemplateConstants.PAYMENT_VALUES.CURRENCY, usagePlanItem.getCurrency());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.SUPPORT_EMAIL, EdsContextParams.getSupportEmail());

        context.put(EmailTemplateConstants.PAYMENT_VALUES.USERS_COUNT, usagePlanItem.getUserCount());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.USERS_PRICE, ServerUtils.decimalPrecision(usagePlanPrice.getFullUsersPrice(), 2));

        context.put(EmailTemplateConstants.PAYMENT_VALUES.ESS_USERS_COUNT, usagePlanItem.getEssUserCount());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.ESS_USERS_PRICE, ServerUtils.decimalPrecision(usagePlanPrice.getEssUsersPrice(), 2));

        context.put(EmailTemplateConstants.PAYMENT_VALUES.NON_USERS_COUNT, usagePlanItem.getNonAccessUserCount());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.NON_USERS_PRICE, ServerUtils.decimalPrecision(usagePlanPrice.getNonUsersPrice(), 2));

        context.put(EmailTemplateConstants.PAYMENT_VALUES.USERS_DISCOUNT, ServerUtils.decimalPrecision(usagePlanPrice.getTotalDiscount(), 2));
        context.put(EmailTemplateConstants.PAYMENT_VALUES.TOTAL_SUBSCRIPTION, ServerUtils.decimalPrecision(usagePlanPrice.getTotalSubscription(), 2));
        context.put(EmailTemplateConstants.PAYMENT_VALUES.TOTAL_ADD_ONS, ServerUtils.decimalPrecision(usagePlanPrice.getAddonPrice(), 2));
        context.put(EmailTemplateConstants.PAYMENT_VALUES.APPS_COUNT, appCount);
        context.put(EmailTemplateConstants.PAYMENT_VALUES.PAYMENT_DATE, new SimpleDateFormat("dd MMM, yyyy").format(new Date()));
        context.put(EmailTemplateConstants.PAYMENT_VALUES.CURRENT_YEAR, LocalDateTime.now().getYear());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.LOGO, "https://apps.kpi.com/customisation/kpi.com/images/kpilogo.png");
        context.put(EmailTemplateConstants.PAYMENT_VALUES.HOST, "https://apps.kpi.com");
        context.put(EmailTemplateConstants.PAYMENT_VALUES.APPS, apps.toString().trim());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.COMPANY_NAME, usagePlanItem.getCompanyName());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.COMPANY_ID, usagePlanItem.getCompanyID());

        try {
            String requestText = EdsTemplates.processTemplate(context, EdsTemplates.PAYMENT_SUBSCRIPTION_NOTIFICATION);
            this.sendMessageFromUser(null, usagePlanItem.getUserEmail(), null, bcc, subject, requestText, false, null, null, false, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendStripeOneTimeChargePaymentNotification(String subscriptionOperation, UsagePlanItem usagePlanItem, UsagePlanPrice usagePlanPrice, UsagePlanItem prevUsagePlan, UsagePlanPrice prevUsagePlanPrice) {
        log.info("======================== Send Stripe One Time Charge Payment Notification ===============================");
        log.info("User ID:" + usagePlanItem.getUserId());
        log.info("User Name:" + usagePlanItem.getUserName());
        log.info("User Email (to Email):" + usagePlanItem.getUserEmail());
        String subject = SUBSCRIPTION_ADD.equals(subscriptionOperation) ? "One Time Charge Stripe Payment Notification" : "Upgrade Stripe Payment Notification";
        if (StringUtils.isNotBlank(usagePlanItem.getCompanyName())) {
            subject += " - ";
            subject += usagePlanItem.getCompanyName();
        }
        String bcc = "support@kpi.com,sales@kpi.com";

        int appCount = 0;
        StringBuilder apps = new StringBuilder();
        if (usagePlanItem.isAccountsModule()) {
            apps.append("Accounting").append(", ");
            appCount++;
        }
        if (usagePlanItem.isSalesModule()) {
            apps.append("Sales").append(", ");
            appCount++;
        }
        if (usagePlanItem.isHumansModule()) {
            apps.append("Humans").append(", ");
            appCount++;
        }
        if (usagePlanItem.isProjectModule()) {
            apps.append("Projects").append(", ");
            appCount++;
        }
        if (usagePlanItem.isPayrollModule()) {
            apps.append("Payroll").append(", ");
            appCount++;
        }
        apps.deleteCharAt(apps.lastIndexOf(","));

        Map<String, Object> context = new HashMap<>();

        context.put(EmailTemplateConstants.PAYMENT_VALUES.CUSTOMER, usagePlanItem.getUserName());
        if (SUBSCRIPTION_ADD.equalsIgnoreCase(subscriptionOperation)) {
            context.put(EmailTemplateConstants.PAYMENT_VALUES.INVOICE_TOTAL_AMOUNT, ServerUtils.decimalPrecision(usagePlanPrice.getTotalSubscription() + usagePlanPrice.getAddonPrice(), 2));
        } else {
            context.put(EmailTemplateConstants.PAYMENT_VALUES.INVOICE_TOTAL_AMOUNT, ServerUtils.decimalPrecision(usagePlanPrice.getTotalAmount() + usagePlanPrice.getAddonPrice(), 2));
        }
        context.put(EmailTemplateConstants.PAYMENT_VALUES.CURRENCY, usagePlanItem.getCurrency());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.SUPPORT_EMAIL, EdsContextParams.getSupportEmail());

        if (prevUsagePlan != null && prevUsagePlan.isPaid()) {
            context.put(EmailTemplateConstants.PAYMENT_VALUES.USERS_COUNT, (usagePlanItem.getUserCount() - prevUsagePlan.getUserCount()));
            context.put(EmailTemplateConstants.PAYMENT_VALUES.ESS_USERS_COUNT, (usagePlanItem.getEssUserCount() - prevUsagePlan.getEssUserCount()));
            context.put(EmailTemplateConstants.PAYMENT_VALUES.NON_USERS_COUNT, (usagePlanItem.getNonAccessUserCount() - prevUsagePlan.getNonAccessUserCount()));
        } else {
            context.put(EmailTemplateConstants.PAYMENT_VALUES.USERS_COUNT, usagePlanItem.getUserCount());
            context.put(EmailTemplateConstants.PAYMENT_VALUES.ESS_USERS_COUNT, usagePlanItem.getEssUserCount());
            context.put(EmailTemplateConstants.PAYMENT_VALUES.NON_USERS_COUNT, usagePlanItem.getNonAccessUserCount());
        }


        if (prevUsagePlanPrice != null && prevUsagePlan != null && prevUsagePlan.isPaid()) {
            context.put(EmailTemplateConstants.PAYMENT_VALUES.USERS_PRICE, ServerUtils.decimalPrecision(usagePlanPrice.getFullUsersPrice() - prevUsagePlanPrice.getFullUsersPrice(), 2));
            context.put(EmailTemplateConstants.PAYMENT_VALUES.ESS_USERS_PRICE, ServerUtils.decimalPrecision(usagePlanPrice.getEssUsersPrice() - prevUsagePlanPrice.getEssUsersPrice(), 2));
            context.put(EmailTemplateConstants.PAYMENT_VALUES.NON_USERS_PRICE, ServerUtils.decimalPrecision(usagePlanPrice.getNonUsersPrice() - prevUsagePlanPrice.getNonUsersPrice(), 2));
            context.put(EmailTemplateConstants.PAYMENT_VALUES.USERS_DISCOUNT, ServerUtils.decimalPrecision(usagePlanPrice.getTotalDiscount() - prevUsagePlanPrice.getTotalDiscount(), 2));
            context.put(EmailTemplateConstants.PAYMENT_VALUES.TOTAL_SUBSCRIPTION, ServerUtils.decimalPrecision(usagePlanPrice.getTotalAmount() - prevUsagePlanPrice.getTotalAmount(), 2));
        } else {
            context.put(EmailTemplateConstants.PAYMENT_VALUES.USERS_PRICE, ServerUtils.decimalPrecision(usagePlanPrice.getFullUsersPrice(), 2));
            context.put(EmailTemplateConstants.PAYMENT_VALUES.ESS_USERS_PRICE, ServerUtils.decimalPrecision(usagePlanPrice.getEssUsersPrice(), 2));
            context.put(EmailTemplateConstants.PAYMENT_VALUES.NON_USERS_PRICE, ServerUtils.decimalPrecision(usagePlanPrice.getNonUsersPrice(), 2));
            context.put(EmailTemplateConstants.PAYMENT_VALUES.USERS_DISCOUNT, ServerUtils.decimalPrecision(usagePlanPrice.getTotalDiscount(), 2));
            context.put(EmailTemplateConstants.PAYMENT_VALUES.TOTAL_SUBSCRIPTION, ServerUtils.decimalPrecision(usagePlanPrice.getTotalAmount(), 2));
        }

        context.put(EmailTemplateConstants.PAYMENT_VALUES.TOTAL_ADD_ONS, ServerUtils.decimalPrecision(usagePlanPrice.getAddonPrice(), 2));
        context.put(EmailTemplateConstants.PAYMENT_VALUES.APPS_COUNT, appCount);
        context.put(EmailTemplateConstants.PAYMENT_VALUES.PAYMENT_DATE, new SimpleDateFormat("dd MMM, yyyy").format(new Date()));
        context.put(EmailTemplateConstants.PAYMENT_VALUES.CURRENT_YEAR, LocalDateTime.now().getYear());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.HOST, EdsContextParams.getHost(companyManager.getUser().getCompany().getObjectID()));
        context.put(EmailTemplateConstants.PAYMENT_VALUES.LOGO, EdsContextParams.getLogoWithHost(companyManager.getUser().getCompany().getObjectID()));
        context.put(EmailTemplateConstants.PAYMENT_VALUES.APPS, apps.toString().trim());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.COMPANY_NAME, usagePlanItem.getCompanyName());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.COMPANY_ID, usagePlanItem.getCompanyID());
        context.put(EmailTemplateConstants.PAYMENT_VALUES.SUBSCRIPTION_OPERATION, subscriptionOperation);
        //ADD-ONS
        if (usagePlanItem.getAddOnsItem() != null) {
            if (usagePlanItem.getAddonOnlineTraining() != null && usagePlanItem.getAddonOnlineTraining() > 0d) {
                context.put(EmailTemplateConstants.PAYMENT_VALUES.ONLINE_TRAINING_QTY, usagePlanItem.getAddOnsItem().getOnlineTraining().getDescription());
                context.put(EmailTemplateConstants.PAYMENT_VALUES.ONLINE_TRAINING_TOTAL, ServerUtils.decimalPrecision(usagePlanItem.getAddonOnlineTraining(), 2));
            }
            if (usagePlanItem.getAddonInitialSetup() != null && usagePlanItem.getAddonInitialSetup() > 0d) {
                context.put(EmailTemplateConstants.PAYMENT_VALUES.INITIAL_SETUP_QTY, usagePlanItem.getAddOnsItem().getInitialSetup().getDescription());
                context.put(EmailTemplateConstants.PAYMENT_VALUES.INITIAL_SETUP_TOTAL, ServerUtils.decimalPrecision(usagePlanItem.getAddonInitialSetup(), 2));
            }
            if (usagePlanItem.getAddonDedicatedAccountManager() != null && usagePlanItem.getAddonDedicatedAccountManager() > 0d) {
                context.put(EmailTemplateConstants.PAYMENT_VALUES.DEDICATED_ACCOUNT_MANAGER_QTY, usagePlanItem.getAddOnsItem().getDedicatedAccountManager().getDescription());
                context.put(EmailTemplateConstants.PAYMENT_VALUES.DEDICATED_ACCOUNT_MANAGER_TOTAL, ServerUtils.decimalPrecision(usagePlanItem.getAddonDedicatedAccountManager(), 2));
            }
            if (usagePlanItem.getAddonCustomPDFTemplate() != null && usagePlanItem.getAddonCustomPDFTemplate() > 0d) {
                context.put(EmailTemplateConstants.PAYMENT_VALUES.CUSTOM_PDF_TEMPLATE_QTY, usagePlanItem.getAddOnsItem().getCustomPdfTemplate().getDescription());
                context.put(EmailTemplateConstants.PAYMENT_VALUES.CUSTOM_PDF_TEMPLATE_TOTAL, ServerUtils.decimalPrecision(usagePlanItem.getAddonCustomPDFTemplate(), 2));
            }
            if (usagePlanItem.getAddonExtraStorage() != null && usagePlanItem.getAddonExtraStorage() > 0d) {
                context.put(EmailTemplateConstants.PAYMENT_VALUES.EXTRA_STORAGE_QTY, usagePlanItem.getAddOnsItem().getExtraStorage().getDescription());
                context.put(EmailTemplateConstants.PAYMENT_VALUES.EXTRA_STORAGE_TOTAL, ServerUtils.decimalPrecision(usagePlanItem.getAddonExtraStorage(), 2));
            }
            if (usagePlanItem.getAddonDedicatedDeveloper() != null && usagePlanItem.getAddonDedicatedDeveloper() > 0d) {
                context.put(EmailTemplateConstants.PAYMENT_VALUES.DEDICATED_DEVELOPER_QTY, usagePlanItem.getAddOnsItem().getDedicatedDeveloper().getDescription());
                context.put(EmailTemplateConstants.PAYMENT_VALUES.DEDICATED_DEVELOPER_TOTAL, ServerUtils.decimalPrecision(usagePlanItem.getAddonDedicatedDeveloper(), 2));
            }
        }

        try {
            String requestText = EdsTemplates.processTemplate(context, EdsTemplates.STRIPE_ONE_TIME_CHARGE_PAYMENT_NOTIFICATION);
            this.sendMessageFromUser(null, usagePlanItem.getUserEmail(), null, bcc, subject, requestText, false, null, null, false, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error occurred while sending stripe upgrade payment notification ", e);
        }
    }

    @Override
    public void sendIncorrectReportBalanceEmail(Integer transactionId) {
        EdsUser user = userManager.getUser();
        String subject = user.getCompany().getObjectID() + "_Incorrect Balance";
        String to = "munir@kpi.com" + ',' + "kamron@kpi.com";

        Map<String, Object> context = new HashMap<>();
        context.put(EmailTemplateConstants.ET_COMPANY_ID, user.getCompany().getObjectID());
        context.put(EmailTemplateConstants.ET_COMPANY_NAME, user.getCompany().getName());
        context.put(EmailTemplateConstants.ET_USER_NAME, user.getEmail());
        context.put(EmailTemplateConstants.ET_EXPENSE_HOST, EdsContextParams.getHost(user.getCompany().getObjectID()));
        context.put(EmailTemplateConstants.ET_DATE, new SimpleDateFormat("dd MMM, yyyy").format(new Date()));
        context.put(EmailTemplateConstants.ET_TRANSACTION_ID, transactionId);

        try {
            String requestText = EdsTemplates.processTemplate(context, EdsTemplates.INCORRECT_REPORT_BALANCE);
            this.sendMessage(to, subject, requestText, null, false, null, null, user.getCompany().getObjectID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmailUserVisitToPage(ByteArrayOutputStream pdfStream) {
        EdsUser user = userManager.getUser();
        Date currentDate = new Date();
        String subject;
        if (user.getCompany() != null) {
            subject = "Order #: " + dateFormat.format(currentDate) + " - " + user.getCompany().getObjectID() + " - " + user.getCompany().getName() + " - Order Summary";
        } else {
            subject = "Order #: " + dateFormat.format(currentDate) + " - Order Summary";
        }

        String to = "setup@kpi.com";

        Map<String, Object> context = new HashMap<>();
        context.put("${user}", user.getName());
        context.put("${userName}", user.getUserName());
        if (user.getEmployee() != null && StringUtils.isNotBlank(user.getEmployee().getPrimaryPhone())) {
            context.put("${userPhone}", user.getEmployee().getPrimaryPhone());
        } else {
            context.put("${userPhone}", "");
        }
        context.put("${userEmail}", user.getEmail());
        if (user.getCountryZone() != null && user.getCountryZone().getCountry() != null) {
            context.put("${country}", user.getCountryZone().getCountry().getName());
        } else {
            context.put("${country}", "");
        }
        context.put("${currentDate}", formatDate(currentDate, user.getCompany()));

        String companyName = (user.getCompany() != null && StringUtils.isNotBlank(user.getCompany().getName())) ? user.getCompany().getName() : "";
        String fileName = companyName.concat("_").concat(ServerUtils.shortDateFormat(user.getUserDate(), user)).concat(".pdf");

        try {
            String requestText = EdsTemplates.processTemplate(context, EdsTemplates.PRICING_PAGE_VIEW_NOTIFICATION);
            send(fileName, DOC_PDF, to, subject, requestText, pdfStream);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void sendShiftToApprover(EdsShift shift) throws Exception {
//        final EdsCompany company = userManager.getUser().getCompany();
//        final EdsEmployee creator = shift.getCreator().getEmployee();
//        final EdsUser approver = shift.getCurrentApprover().getExactEmployee();
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(shift.getDate());
//        String subject = commonLocalizer.localize(PAYSLIP_FOR) + " " + calendar.get(calendar.MONTH);
//        String url = EdsContextParams.getHost(company.getObjectID()) + "/Hrms.html?link=" + EncryptionHelper.encryptURL("shift|summary/"
//                + shift.getObjectID()) + "/" + shift.getOverallStatus().getName()
//                + "&" + U_ID + "=" + EncryptionHelper.encryptURL(approver.getObjectID().toString());
//        final Map<String, Object> values = new TreeMap<>();
//        values.put("managername", approver.getName());
//        values.put("companyname", company.getName());
//        values.put("creator", creator.getFullName());
//        values.put("date", shift.getCreationDate() != null ? formatDateShort(shift.getCreationDate(), company) : "");
//        values.put("url", url);
//        String text = EdsTemplates.processTemplate(values, EdsTemplates.ADDITIONAL_PAYMENT_TO_APPROVER);
//        sendMessageFromUser(null, approver.getEmail(), subject, text, null, null, false, employeeManager.getUser().getEmail(), null, false, null, null, employeeManager.getUser());
    }

    @Override
    public void sendShiftToEmployee(EdsEmployee employee, EdsShift shift) throws Exception {
//        EdsUser user = employeeManager.getUser();
//        EdsUser approver = shift.getCurrentApprover().getExactEmployee();
//        EdsCompany company = userManager.getUser().getCompany();
//        String subject = "";
//        String originalName = "";
//        if (BY_COMMISION_TYPE.equals(overtimeObject.getType())) {
//            subject = commonLocalizer.localize(COMMISSION_FOR) + " " + (overtimeObject.getMonth() != null ? overtimeObject.getMonth() : "");
//            originalName = "Commission_for_" + (overtimeObject.getMonth() != null ? overtimeObject.getMonth() : "") + ".pdf";
//        } else {
//            subject = commonLocalizer.localize(PAYMENT_FOR) + " " + (additionalPayment.getMonth() != null ? additionalPayment.getMonth() : "");
//            originalName = "Payment_for_" + (additionalPayment.getMonth() != null ? additionalPayment.getMonth() : "") + ".pdf";
//        }

//        final Map<String, Object> values = new TreeMap<>();
//        values.put("USER", employee);
//        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.EMP_FIRST_NAME, StringUtils.capitalize(employee.getFirstName()));
//        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.EMP_LAST_NAME, employee.getLastName());
//        values.put("managername", approver != null ? approver.getName() : "");
//        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.APPROVER_FIRST_NAME, approver != null ? StringUtils.capitalize(approver.getFirstName()) : "");
//        values.put(EmailTemplateConstants.PAYROLL.PAYSLIP_APPROVED.APPROVER_LAST_NAME, approver != null ? approver.getLastName() : "");
//        values.put(EmailTemplateConstants.PAYROLL.DATE, shift.getDate());
//
//        String text = EdsTemplates.processTemplate(values, EdsTemplates.ADDITIONAL_PAYMENT_TO_EMPLOYEE);
//        sendMessageFromUser(originalName, employee.getEmail(), subject, text, user, null);
    }

    @Override
    public void sendBackupsEmployeeToApprover(EdsBackupsEmployee backupsEmployee) throws Exception {

    }

    @Override
    public void sendBackupsEmployeeToEmployee(EdsEmployee employee, EdsBackupsEmployee backupsEmployee) throws Exception {

    }

    @Override
    public void create(EdsMessage obj) {
        createNative(obj);
        //TODO : Adding to queue
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNative(EdsMessage obj) {
        super.create(obj);
    }
}
