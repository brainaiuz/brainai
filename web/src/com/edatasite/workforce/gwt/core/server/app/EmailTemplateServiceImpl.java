package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.components.SessionCryptor;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsContract;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsExpenseHistory;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsGoogleCalendarEventGuests;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBatchPayment;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.domain.settings.EdsSignature;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ContractManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarEventGuestsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.SignatureManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BatchPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.MessageManagerImpl;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;
import com.edatasite.workforce.mail.EdsSubjects;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.mail.EdsTemplates;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.ACCOUNTING.PAID_AMOUNT_INVOICE;
import static com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants.CONTRACT_REMINDER_VALUES.CC_CONTRACT_CUSTOMER;

/**
 * User: Ilhombek
 * Date: 21.07.2010
 * Time: 19:50:55
 */
@Transactional
@Service("emailTemplateService")
public class EmailTemplateServiceImpl implements EmailTemplateService, EmailTemplateServiceLocal, EmailTemplateConstants, Constants {

    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private EmailSettingsManager emailSettingsManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private RFQManager rfqManager;
    @Autowired
    private GoogleCalendarEventGuestsManager eventGuestsManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private SignatureManager signatureManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    private ContractManager contractManager;
    @Autowired
    private BatchPaymentManager batchPaymentManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private OpportunityManager opportunityManager;

    /**
     * Generate email template for compose view
     *
     * @param emailTemplate - email template
     * @return emailTemplateItem
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplate(EntityToEmailTemplate emailTemplate) {
        EdsEmailTemplate template = emailTemplateManager.get(emailTemplate.getEmailTemplateId());
        String recipientFirstName = "";
        String recipientLastName = "";
        String recipientEmail = "";
        String recipientFullName = "";
        String recipientCompanyName = "";

        if (emailTemplate.getEntityId() != null && !"".equals(emailTemplate.getEntityId())) {
            if (RelationItem.TYPE_LEAD.equals(emailTemplate.getEntityType()) || RelationItem.TYPE_CONTACT.equals(emailTemplate.getEntityType())) {//entityType == lead
                EdsCrmContact cl = crmContactManager.get(emailTemplate.getEntityId());
                if (cl != null) {
                    recipientFirstName = cl.getFirstName() == null ? " " : cl.getFirstName();
                    recipientLastName = cl.getLastName() == null ? " " : cl.getLastName();
                    recipientFullName = recipientFirstName + recipientLastName;
                    recipientEmail = cl.getPrimaryEmail();
                    recipientCompanyName = cl.getCrmAccount() != null ? cl.getCrmAccount().getName() : "";
                }
            } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(emailTemplate.getEntityType())) {
                EdsCrmAccount crmAccount = crmAccountManager.get(emailTemplate.getEntityId());
                if (crmAccount != null) {
                    recipientFirstName = crmAccount.getName();
                    recipientFullName = recipientFirstName;
                    recipientCompanyName = crmAccount.getName();
                }
            } /*else {//or entityType == email
                crmEmail = emailRepository.findOne(emailTemplate.getEntityId());
            }*/
        }
        EdsUser reporter = getReporterEmail(template);
        if (template.getTemplateCategory() == null || !CRM_MASS_MAILING_CATEGORY.equals(template.getTemplateCategory().getCode())) {
            if ("".equals(recipientFirstName)) {
                recipientFirstName = reporter.getFirstName();
            }
            if ("".equals(recipientLastName)) {
                recipientLastName = reporter.getLastName();
            }
        }

        EdsUser sender;
        if (template.getFromUser() != null && !template.getFromUser().equals(-1)) {
            sender = userManager.get(template.getFromUser());
        } else {
            sender = userManager.getUser();
        }
        if (sender == null) {
            List<EdsEmployee> admins = userManager.getAdmins(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
            if (admins != null && admins.size() > 0) {
                sender = admins.get(0);
            }
        }
        String senderEmail = sender.getEmail();
        String senderPrimaryPhone;
        String senderMobilePhone = "";
        EdsEmployee employee = employeeManager.get(sender.getObjectID());
        if (sender.isClientContact()) {
            senderPrimaryPhone = sender.getClientContact() != null ? (sender.getClientContact().getCrmContact() != null ?
                    (sender.getClientContact().getCrmContact().getPrimaryPhone() != null ? sender.getClientContact().getEmployee().getPrimaryPhone() : "") : "") : "";
        } else {
            senderPrimaryPhone = employee != null ? (employee.getPrimaryPhone() != null ? employee.getPrimaryPhone() : "") : "";
        }
        if (sender.isClientContact()) {
            if (sender.getClientContact() != null && sender.getClientContact().getCrmContact() != null) {
                List<EdsCrmContactItemParams> numbers = sender.getClientContact().getCrmContact().getItemParams(EdsCrmContactItemParams.PHONE);
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
        String jobTitle = employee.getContact().getJobTitles() != null ? employee.getContact().getJobTitles() : "";
        String title = employee != null && employee.getContact() != null && employee.getContact().getTitle() != null ? employee.getContact().getTitle() : "";
        if (title != null) {
            if (employee.getContact().getTitleRef() != null && !"other".equalsIgnoreCase(employee.getContact().getTitleRef().getName())) {
                title = referenceWfmMessageSource.localizeRef(employee.getContact().getTitleRef());
            }
        }
        Map<String, Object> values = new TreeMap<>();//generate template values
        values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(sender));
        if (!"".equals(recipientFirstName)) {
            values.put(EmailTemplateUtils.ET_RECIPIENT_FIRST_NAME, StringUtils.capitalize(recipientFirstName));
        }
        if (!"".equals(recipientLastName)) {
            values.put(EmailTemplateUtils.ET_RECIPIENT_LAST_NAME, recipientLastName);
        }
        if (!"".equals(recipientFullName)) {
            values.put(EmailTemplateUtils.ET_RECIPIENT_FULL_NAME, recipientFullName);
        }
        if (!"".equals(recipientEmail)) {
            values.put(EmailTemplateUtils.ET_RECIPIENT_EMAIL, recipientEmail);
        }
        if (!"".equals(recipientCompanyName)) {
            values.put(EmailTemplateUtils.ET_RECIPIENT_COMPANY_NAME, recipientCompanyName);
        }
        values.put(EmailTemplateUtils.ET_SENDER_TITLE, title);
        values.put(EmailTemplateUtils.ET_SENDER_FIRST_NAME, StringUtils.capitalize(sender.getFirstName()));
        values.put(EmailTemplateUtils.ET_SENDER_LAST_NAME, sender.getLastName());
        values.put(EmailTemplateUtils.ET_SENDER_EMAIL, senderEmail);
        values.put(EmailTemplateUtils.ET_SENDER_COMPANY_NAME, sender.getCompany().getName());
        values.put(EmailTemplateUtils.ET_SENDER_PRIMARY_PHONE, senderPrimaryPhone);
        values.put(EmailTemplateUtils.ET_SENDER_MOBILE_PHONE, senderMobilePhone);
        values.put(EmailTemplateUtils.ET_SENDER_JOB_TITLE, jobTitle);
        //for crm mass mail
        values.put(EmailTemplateUtils.ET_TITLE, title);
        values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(sender.getFirstName()));
        values.put(EmailTemplateUtils.ET_LAST_NAME, sender.getLastName());
        values.put(EmailTemplateUtils.ET_COMPANY_NAME, sender.getCompany() != null ? sender.getCompany().getName() : "");
        values.put(EmailTemplateUtils.ET_EMAIL, senderEmail);
        values.put(EmailTemplateUtils.ET_MOBILE, senderMobilePhone);

        return generateEmailTemplateItem(template, values, reporter.getEmail(), sender.getObjectID(), senderEmail);
    }

    /**
     * Client activation email template
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateClientActivationNewUserEmailTemplate(EdsClientContact client, EdsUser creator, String subject) {
        EdsEmailTemplate template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(CLIENT_ACTIVATION_NEW_USER_CATEGORY);
        if (template != null) {
            String toEmail = client.getEmail();
            Integer userId = creator.getObjectID();
            String fromEmail = creator.getEmail();

            String id = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(client.getObjectID().toString()));
            Integer companyID = client.getCompany().getObjectID();

            String firstName = client.getFirstName();
            String lastName = client.getLastName();
            String managerName = creator.getFullName();
            String companyName = client.getCompany().getName();

            String host = EdsContextParams.getHost(companyID);

            String companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(companyID.toString()));
            String activationLink = host + "/account?uid=" + id + "&cid=" + companyid;
            String productName = EdsContextParams.getProductName();

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(template.getFromUser())));
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(firstName));
            values.put(EmailTemplateUtils.ET_LAST_NAME, lastName);
            values.put(EmailTemplateUtils.ET_MANAGER_NAME, managerName);
            values.put(EmailTemplateUtils.ET_ACTIVATION_LINK, activationLink);
            values.put(EmailTemplateUtils.ET_COMPANY_NAME, companyName);
            values.put(EmailTemplateUtils.ET_PRODUCT_NAME, productName);

            values.put(EmailTemplateUtils.ET_ACCESS_TYPE, client.getAccessType());

            return generateEmailTemplateItem(template, values, toEmail, userId, fromEmail);
        } else {
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateClientActivationExistingUserEmailTemplate(EdsClientContact client, EdsUser creator, String subject) {
        EdsEmailTemplate template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(CLIENT_ACTIVATION_EXISTING_USER_CATEGORY);
        if (template != null) {
            String toEmail = client.getEmail();
            Integer userId = creator.getObjectID();
            String fromEmail = creator.getEmail();

            String firstName = client.getFirstName();
            String lastName = client.getLastName();
            String managerName = creator.getFullName();
            String companyName = client.getCompany().getName();
            String host = EdsContextParams.getHost(client.getCompany().getObjectID());
            String productName = EdsContextParams.getProductName();

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(template.getFromUser())));
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(firstName));
            values.put(EmailTemplateUtils.ET_LAST_NAME, lastName);
            values.put(EmailTemplateUtils.ET_MANAGER_NAME, managerName);
            values.put(EmailTemplateUtils.ET_LOGIN_PAGE_LINK, host);
            values.put(EmailTemplateUtils.ET_COMPANY_NAME, companyName);
            values.put(EmailTemplateUtils.ET_PRODUCT_NAME, productName);

            return generateEmailTemplateItem(template, values, toEmail, userId, fromEmail);
        } else {
            return null;
        }
    }

    /**
     * Employee activation email template
     */

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmployeeActivatedByManagerEmailTemplate(EdsEmployee employee) {
        EdsEmailTemplate template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(EMPLOYEE_ACTIVATED_BY_MANAGER_CATEGORY);

        if (template != null) {
            String toEmail = employee.getEmail();
            Integer userId = employee.getUpdater().getObjectID();
            String fromEmail = employee.getUpdater().getEmail();

            String managerFullName = employee.getUpdater().getFullName();
            String firstName = employee.getFirstName();
            String lastName = employee.getLastName();
            String companyName = employee.getCompany().getName();
            String userName = employee.getUserName();
            String id = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(employee.getObjectID().toString()));
            String companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(employee.getCompany().getObjectID().toString()));

            String activationLink = EdsContextParams.getHost() + "/account?uid=" + id + "&cid=" + companyid + "&chpass=true";

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(template.getFromUser())));
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(firstName));
            values.put(EmailTemplateUtils.ET_LAST_NAME, lastName);
            values.put(EmailTemplateUtils.ET_MANAGER_NAME, managerFullName);
            values.put(EmailTemplateUtils.ET_USER_NAME, userName);
            values.put(EmailTemplateUtils.ET_ACTIVATION_LINK, activationLink);
            values.put(EmailTemplateUtils.ET_COMPANY_NAME, companyName);

            return generateEmailTemplateItem(template, values, toEmail, userId, fromEmail);
        } else {
            return null;
        }
    }


    /**
     * Employee password changed email template
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateUserAccountConfirmationEmailTemplate(EdsUser user) {
        EdsEmailTemplate template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(USER_ACCOUNT_CONFIRMATION_CATEGORY);
        if (template != null) {
            String toEmail = user.getEmail();

            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String link = EdsContextParams.getHost(user.getCompany().getObjectID());
            String companyName = user.getCompany().getName();
            String userName = user.getUserName();
            String productName = EdsContextParams.getProductName();

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(template.getFromUser())));
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(firstName));
            values.put(EmailTemplateUtils.ET_LAST_NAME, lastName);
            values.put(EmailTemplateUtils.ET_USER_NAME, userName);
            values.put(EmailTemplateUtils.ET_LOGIN_PAGE_LINK, link);
            values.put(EmailTemplateUtils.ET_COMPANY_NAME, companyName);
            values.put(EmailTemplateUtils.ET_PRODUCT_NAME, productName);

            return generateEmailTemplateItem(template, values, toEmail, -1, EdsContextParams.getSupportEmail());
        } else {
            return null;
        }
    }

    /**
     * New Employee add Email template
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmployeeActivationNewUserEmailTemplate(EdsEmployee employee, EdsUser admin) {
        EdsEmailTemplate template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(EMPLOYEE_ACTIVATION_NEW_USER_CATEGORY);

        if (template != null) {
            String toEmail = employee.getEmail();
            String fromEmail = admin.getEmail();
            Integer userId = admin.getObjectID();

            String firstName = employee.getFirstName();
            String lastName = employee.getLastName();
            String managerName = admin.getFullName();
            String companyName = employee.getCompany().getName();
            Integer companyID = employee.getCompany().getObjectID();

            String host = EdsContextParams.getHost(companyID);
            String id = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(employee.getObjectID().toString()));
            String companyid = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(companyID.toString()));

            String activationLink = host + "/account?uid=" + id + "&cid=" + companyid;
            String productName = EdsContextParams.getProductName();

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(template.getFromUser())));
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(firstName));
            values.put(EmailTemplateUtils.ET_LAST_NAME, lastName);
            values.put(EmailTemplateUtils.ET_MANAGER_NAME, managerName);
            values.put(EmailTemplateUtils.ET_ACTIVATION_LINK, activationLink);
            values.put(EmailTemplateUtils.ET_COMPANY_NAME, companyName);
            values.put(EmailTemplateUtils.ET_LOGIN_PAGE_LINK, host);
            values.put(EmailTemplateUtils.ET_PRODUCT_NAME, productName);

            return generateEmailTemplateItem(template, values, toEmail, userId, fromEmail);
        } else {
            return null;
        }
    }

    /**
     * Employee add for existing user name email template
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmployeeActivationExistingUserEmailTemplate(EdsEmployee employee, EdsUser admin) {
        EdsEmailTemplate template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(EMPLOYEE_ACTIVATION_EXISTING_USER_CATEGORY);

        if (template != null) {
            String toEmail = employee.getEmail();
            String subject = template.getSubject();
            String fromEmail = admin.getEmail();
            Integer userId = admin.getObjectID();

            String firstName = employee.getFirstName();
            String lastName = employee.getLastName();
            String managerName = admin.getFullName();
            String host = EdsContextParams.getHost(admin.getCompany().getObjectID());

            String companyName = admin.getCompany().getName();

            String productName = EdsContextParams.getProductName();

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(template.getFromUser())));
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(firstName));
            values.put(EmailTemplateUtils.ET_LAST_NAME, lastName);
            values.put(EmailTemplateUtils.ET_MANAGER_NAME, managerName);
            values.put(EmailTemplateUtils.ET_LOGIN_PAGE_LINK, host);
            values.put(EmailTemplateUtils.ET_COMPANY_NAME, companyName);
            values.put(EmailTemplateUtils.ET_PRODUCT_NAME, productName);

            return generateEmailTemplateItem(template, values, toEmail, userId, fromEmail);
        } else {
            return null;
        }
    }

    /**
     * leave request emailtemplate for admin
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateSickRequestTemplateTo_Admin(EdsUser sender, EdsUser receiver, EdsSickRequest request, String templateCategory) {
        EdsEmailTemplate template = emailTemplateManager.getEmailTemplateByCategory(templateCategory);
        if (template != null) {
            Integer senderId = sender.getEmployee().getObjectID();
            String senderEmail = sender.getEmail();
            String receiverEmail = receiver.getEmail();

            String requestEmployeeFullname = request.getEmployee().getFullName();
            String senderEmployeeFullname = sender.getEmployee().getFullName();
            String requestApproverFullName = request.getApproverName(request.getCurrentApprover(), request.getFirstApprover());
            String companyName = sender.getCompany().getName();
            String name = "";
            if (request.getLeaveReason() != null) {
                name = referenceWfmMessageSource.localizeRef(request.getLeaveReason());
            } else if (request.getOtherReason() != null) {
                name = "Request";
            }
            String requestType = name.contains("request") || name.contains("Request") ? name : name + " request";

            String requstReasonName = request.getLeaveReason() != null ? referenceWfmMessageSource.localizeRef(request.getLeaveReason()) : request.getOtherReason();
            String requstOtherName = request.getOtherReason() != null ? request.getOtherReason() : requestType;
            String requestDescription = request.getDescription();
            String startDate = formatDate(request.getStartDate());
            String endDate = formatDate(request.getEndDate());
            String confirmDate = formatDate(new Date());
            String host = EdsContextParams.getHost(request.getEmployee().getCompany().getObjectID());

            String companyId = EncryptionHelper.encryptURL(request.getEmployee().getCompany().getObjectID().toString());

            String link = EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("leaverequest|/" + request.getObjectID() + "/" + request.getEmployee().getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(request.getEmployee().getObjectID().toString()) + "&" + C_ID + "=" + companyId;
            link = host + "/Hrms.html?link=" + link;
            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(template.getFromUser())));
            values.put(EmailTemplateUtils.ET_EXPENSE_HOST, host);
            values.put(EmailTemplateUtils.ET_REQUEST_EMPLOYEE_FULLNAME, requestEmployeeFullname);
            values.put(EmailTemplateUtils.ET_SENDER_EMPLOYEE_FULLNAME, senderEmployeeFullname);
            values.put(EmailTemplateUtils.ET_REQUEST_APPROVER_FULLNAME, requestApproverFullName);
            values.put(EmailTemplateUtils.ET_RECEIVER_FULL_NAME, receiver.getFullName());
            values.put(EmailTemplateUtils.ET_QUEST_TYPE, requestType);
            values.put(EmailTemplateUtils.ET_REQUEST_REASON_NAME, requstReasonName);
            values.put(EmailTemplateUtils.ET_REQUEST_OTHEER_REASON, requstOtherName);
            values.put(EmailTemplateUtils.ET_REQUEST_DESCRIPTION, requestDescription);
            values.put(EmailTemplateUtils.ET_START_DATE, startDate);
            values.put(EmailTemplateUtils.ET_END_DATE, endDate);
            values.put(EmailTemplateUtils.ET_CONFIRMDATE, confirmDate);
            values.put(EmailTemplateUtils.ET_LINK, link);
            values.put(EmailTemplateUtils.ET_COMPANY_NAME, companyName);

            EmailTemplateItem templateItem = generateEmailTemplateItem(template, values, receiverEmail, senderId, senderEmail);
            templateItem.setCompanyEmailTemplate(template.getCompanyEmailTemplate());
            templateItem.setDefault(template.isDefault() != null ? template.isDefault() : false);
            return templateItem;
//
        } else {
            return null;
        }
    }

    /**
     * Leave Requests email template
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateSickRequestTemplateTo_Employee(EdsUser sender, EdsUser receiver, EdsUser employee, String status, EdsSickRequest request, String templateCategory) {
        EdsEmailTemplate template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(templateCategory);
        if (template != null) {
            String requestEmployeeFullname = request.getEmployee().getFullName();
            String senderEmployeeFullname = sender.getEmployee().getFullName();
            String requestApproverFullName = request.getApproverName(request.getCurrentApprover(), request.getFirstApprover(), request.getLastApprover());
            String companyName = sender.getCompany().getName();
            String name = "";
            if (request.getLeaveReason() != null) {
                name = referenceWfmMessageSource.localizeRef(request.getLeaveReason());
            } else if (request.getOtherReason() != null) {
                name = "Request";
            }
            String requestType = name.contains("request") || name.contains("Request") ? name : name + " request";
            String requstReasonName = request.getLeaveReason() != null ? request.getLeaveReason().getName() : request.getOtherReason();
            String requstOtherName = request.getOtherReason() != null ? request.getOtherReason() : requestType;
            String requestDescription = request.getDescription();
            String startDate = formatDate(request.getStartDate());
            String endDate = formatDate(request.getEndDate());
            String confirmDate = formatDate(new Date());
            String host = EdsContextParams.getHost(request.getEmployee().getCompany().getObjectID());

            String link = host + "/Hrms.html?link=" + EncryptionHelper.encryptURL("availability|availabilityHome");

            String receiverEmail = receiver.getEmail();
            final Map<String, Object> requestValues = new TreeMap<>();
            requestValues.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(template.getFromUser())));
            requestValues.put(EmailTemplateUtils.ET_EXPENSE_HOST, EdsContextParams.getHost(employee.getCompany().getObjectID()));
            requestValues.put(EmailTemplateUtils.ET_SENDER, sender.getFullName());
            requestValues.put(EmailTemplateUtils.ET_EMPLOYEE, employee.getFullName());
            requestValues.put(EmailTemplateUtils.ET_STATUS, status.toLowerCase());
            requestValues.put(EmailTemplateUtils.ET_LINK, link);

            requestValues.put(EmailTemplateUtils.ET_REQUEST_EMPLOYEE_FULLNAME, requestEmployeeFullname);
            requestValues.put(EmailTemplateUtils.ET_SENDER_EMPLOYEE_FULLNAME, senderEmployeeFullname);
            requestValues.put(EmailTemplateUtils.ET_REQUEST_APPROVER_FULLNAME, requestApproverFullName);
            requestValues.put(EmailTemplateUtils.ET_QUEST_TYPE, requestType);
            requestValues.put(EmailTemplateUtils.ET_REQUEST_REASON_NAME, requstReasonName);
            requestValues.put(EmailTemplateUtils.ET_REQUEST_OTHEER_REASON, requstOtherName);
            requestValues.put(EmailTemplateUtils.ET_REQUEST_DESCRIPTION, requestDescription);
            requestValues.put(EmailTemplateUtils.ET_START_DATE, startDate);
            requestValues.put(EmailTemplateUtils.ET_END_DATE, endDate);
            requestValues.put(EmailTemplateUtils.ET_CONFIRMDATE, confirmDate);
            requestValues.put(EmailTemplateUtils.ET_COMPANY_NAME, companyName);

            EmailTemplateItem templateItem = generateEmailTemplateItem(template, requestValues, receiverEmail, sender.getObjectID(), sender.getEmail());
            templateItem.setCompanyEmailTemplate(template.getCompanyEmailTemplate());
            return templateItem;
        } else {
            return null;
        }
    }

    /**
     * Related reporter email
     *
     * @param emailTemplate
     * @return
     */
    private EdsUser getReporterEmail(EdsEmailTemplate emailTemplate) {
        EdsUser reporterEmail = null;
        if (emailTemplate != null && emailTemplate.getFromUser() != null && !"".equals(emailTemplate.getFromUser())) {
            EdsUser user = userManager.get(emailTemplate.getFromUser());
            if (user != null) {
                reporterEmail = user;
            } else {
                user = userManager.getUser();
                if (user != null) {
                    reporterEmail = user;
                }
            }
        } else {
            EdsUser user = userManager.getUser();
            if (user != null) {
                reporterEmail = user;
            }
        }
        return reporterEmail;
    }

    /**
     * Generate Email Template item
     *
     * @param template
     * @param values
     * @param toEmail
     * @param fromUserID
     * @param fromEmail
     * @return
     * @throws EdsTemplateException
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateItem(EdsEmailTemplate template, Map<String, Object> values, String toEmail, Integer fromUserID, String fromEmail) {
        String generateMessage = null;
        String generateSubject = null;
        if (fromUserID == null) {
            fromUserID = userManager.getUser().getObjectID();
        }
        try {
            generateMessage = EdsTemplates.evaluateTemplate(values, template.getMessageHTML());//generate message
            generateSubject = EdsTemplates.evaluateTemplate(values, template.getSubject());// generate subject
        } catch (EdsTemplateException ex) {
            ex.printStackTrace();
        }
        EmailTemplateItem templateItem = new EmailTemplateItem();
        templateItem.setFromEmail((template.getFromEmail() != null && !template.getFromEmail().equals("")) ? template.getFromEmail() : fromEmail);
        templateItem.setFromUserID((template.getFromUser() != null && template.getFromUser() != -1) ? template.getFromUser() : fromUserID);
        templateItem.setMessageHTML(generateMessage);
        templateItem.setSubject(generateSubject);
        templateItem.setToEmail(toEmail);
        templateItem.setFromUserName(template.getFromUserName());
        EdsUser user = userManager.getUser();
        String companyID = ServerSecurityContext.getInstance().getCompanyId();
        if (!(companyID == null || "".equals(companyID))) {
            user = userManager.getUserByUserIdAndCompanyId(fromUserID, Integer.valueOf(companyID));
        }
        if (user != null) {
            List<FileResource> emailTemplateAttachments = attachmentUtilsManager.getAttachments(F_EMAIL_TEMPLATE, template.getObjectID(), template.getObjectID(), user);
            FileItem[] fileItems = getEmailTemplateAttachments(emailTemplateAttachments);
            templateItem.setAttachments(fileItems);

            emailTemplateAttachments.forEach(item -> item.setEmailTemplateAttachment(true));
            templateItem.setFileResources((ArrayList<FileResource>) emailTemplateAttachments);
        }

        return templateItem;
    }

    public EmailTemplateItem generateEmailTemplate(EdsEmailTemplate template, Map<String, Object> values, String toEmail, Integer fromUserID, String fromEmail) {
        String generateMessage = null;
        String generateSubject = null;
        try {
            generateMessage = EdsTemplates.evaluateTemplate(values, template.getMessageHTML());//generate message
            generateSubject = EdsTemplates.evaluateTemplate(values, template.getSubject());// generate subject
        } catch (EdsTemplateException ex) {
            ex.printStackTrace();
        }
        EmailTemplateItem templateItem = new EmailTemplateItem();
        templateItem.setFromEmail((template.getFromEmail() != null && !template.getFromEmail().equals("")) ? template.getFromEmail() : fromEmail);
        templateItem.setFromUserID((template.getFromUser() != null && template.getFromUser() != -1) ? template.getFromUser() : fromUserID);
        templateItem.setMessageHTML(generateMessage);
        templateItem.setSubject(generateSubject);
        templateItem.setToEmail(toEmail);
        return templateItem;
    }

    /**
     * Generate email template attachments
     *
     * @param emailTemplateAttachments - email tempalte attachments list
     * @return - e.t. attachments
     */
    private FileItem[] getEmailTemplateAttachments(List<FileResource> emailTemplateAttachments) {
        FileItem[] fileItems = {};
        if (emailTemplateAttachments != null && emailTemplateAttachments.size() > 0) {
            fileItems = new FileItem[emailTemplateAttachments.size()];
            for (int i = 0; i < emailTemplateAttachments.size(); i++) {
                FileResource fileResource = emailTemplateAttachments.get(i);
                FileItem fileItem = new FileItem();
                fileItem.setAttachmentId(fileResource.getBodyId());
                fileItem.setId(fileResource.getObjectId());
                fileItem.setFileName(fileResource.getEncodedName());
                fileItem.setDescription(fileResource.getDescription());
                fileItem.setSize(fileResource.getContentLength());
                fileItem.setUploadType(fileResource.getUploadType());
                fileItem.setDate(fileResource.getCreationDate());
                switch (fileResource.getUploadType()) {
                    case GOOGLE -> fileItem.setGoogleDocumentLink(fileResource.getGoogleDownloadLink());
                    case OFFICE_365, OFFICE_365_SHARE_POINT ->
                            fileItem.setOfficeDocumentLink(fileResource.getOfficeDownloadLink());
                    default -> fileItem.setAmazonLink(fileResource.getAmazonLink());
                }
                fileItems[i] = fileItem;
            }
        }
        return fileItems;
    }

    /**
     * Generate email template for reply to reporter case
     *
     * @param emailTemplate
     * @param autoResponseID
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateReplyToReporterCaseItem(EntityToEmailTemplate emailTemplate, Integer autoResponseID) {
        EdsEmailTemplate replyToTemplate = emailTemplateManager.get(emailTemplate.getEmailTemplateId());

        EdsCase crmCase = null;
        if (!"".equals(emailTemplate.getEntityType()) || !"case".equals(emailTemplate.getEntityType())) {
            crmCase = caseManager.get(emailTemplate.getEntityId());
        }

        String reporterName = crmCase.getReportedBy();
        String reporterEmail = crmCase.getReplyTo() != null && !"".equals(crmCase.getReplyTo()) ? crmCase.getReplyTo() : crmCase.getEmail();
        EdsUser replier = getReplier(replyToTemplate, autoResponseID);
        if (replier == null) {
            replier = userManager.getUser();
        }
        String replierFirstName = replier.getFirstName();
        String replierLastName = replier.getLastName();
        String replierCompanyName = replier.getCompany().getName();
        String replierEmail = replier.getEmail();
        if (autoResponseID != null || replier != null) {
            EdsEmailSetting emailSettings = autoResponseID != null ? emailSettingsManager.get(autoResponseID) : emailSettingsManager.getCompanyEmailSetting(null);
            if (emailSettings != null && emailSettings.isActive()) {
                replierEmail = emailSettings.getEmail();
            }
        }

        String number = crmCase.getCaseNumberString();
        String subject = crmCase.getSubject() != null ? crmCase.getSubject() : "";
        String description = crmCase.getDescription() != null ? crmCase.getDescription() : "";
        String type = crmCase.getType() != null ? referenceWfmMessageSource.localizeRef(crmCase.getType()) : "";
        String origin = crmCase.getCaseOrigion() != null ? referenceWfmMessageSource.localizeRef(crmCase.getCaseOrigion()) : "";
        String reason = crmCase.getCaseReason() != null ? referenceWfmMessageSource.localizeRef(crmCase.getCaseReason()) : "";
        String caseAssignName = crmCase.getAssignee() != null ? crmCase.getAssignee().getName() : "";

        Map<String, Object> values = new TreeMap<>();
        values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(replyToTemplate.getFromUser())));
        values.put(EmailTemplateUtils.ET_EXPENSE_HOST, EdsContextParams.getHost(replier.getCompany().getObjectID()));
        values.put(EmailTemplateUtils.ET_CASE_NUMBER, number);
        values.put(EmailTemplateUtils.ET_CASE_SUBJECT, subject);
        values.put(EmailTemplateUtils.ET_DESCRIPTION, description);
        values.put(EmailTemplateUtils.ET_CASE_TYPE, type);
        values.put(EmailTemplateUtils.ET_CASE_ORIGIN, origin);
        values.put(EmailTemplateUtils.ET_CASE_REASON, reason);
        values.put(EmailTemplateUtils.ET_CASE_ASSIGNEE, caseAssignName);
        values.put(EmailTemplateUtils.ET_CASE_REPORTER, reporterName);
        values.put(EmailTemplateUtils.ET_CASE_REPLIER_FIRST_NAME, StringUtils.capitalize(replierFirstName));
        values.put(EmailTemplateUtils.ET_CASE_REPLIER_LAST_NAME, replierLastName);
        values.put(EmailTemplateUtils.ET_CASE_REPLIER_COMPANY, replierCompanyName);
        values.put(EmailTemplateUtils.ET_CASE_REPLIER_EMAIL, replierEmail);

        return generateEmailTemplateItem(replyToTemplate, values, reporterEmail, userManager.getUser() != null ? userManager.getUser().getObjectID() : replier.getObjectID(), replierEmail);
    }

    /**
     * Related EdsUser
     *
     * @param autoResponseID
     * @param replyToTemplate
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EdsUser getReplier(EdsEmailTemplate replyToTemplate, Integer autoResponseID) {
        EdsUser replier;
        if (replyToTemplate.getFromUser() != null && replyToTemplate.getFromUser() != -1) {
            replier = userManager.get(replyToTemplate.getFromUser());
        } else {
            replier = userManager.getUser();
        }
        if (replier == null) {
            if (autoResponseID != null) {
                EdsEmailSetting emailSettings = emailSettingsManager.get(autoResponseID);
                if (emailSettings != null) {
                    replier = emailSettings.getUser();
                }
            }
        }
        return replier;
    }

    /**
     * Generate email template for Recurring invoices
     *
     * @param entityToEmailTemplate
     * @param senderID
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateForAccountingComposeView(EntityToEmailTemplate entityToEmailTemplate, Integer senderID) {
        if (REQUEST_FOR_QUOTE_CATEGORY.equals(entityToEmailTemplate.getEntityType())) {
            return generateRFQEmailTemplateData(entityToEmailTemplate, senderID);
        } else if (CUSTOMER_BALANCE_CATEGORY.equals(entityToEmailTemplate.getEntityType()) || SUPPLIER_BALANCE_CATEGORY.equals(entityToEmailTemplate.getEntityType())) {
            return generateCustomerAndSupplierBalanceEmailTemplateData(entityToEmailTemplate, senderID);
        } else if (RECEIVE_PAYMENT_CATEGORY.equals(entityToEmailTemplate.getEntityType())) {
            return generateBatchPaymentTemplateItem(entityToEmailTemplate);
        } else {
            return generateEmailTemplateData(entityToEmailTemplate, senderID);
        }
    }

    public EmailTemplateItem generateCustomerAndSupplierBalanceEmailTemplateData(EntityToEmailTemplate entityToEmailTemplate, Integer senderID) {
        EdsEmailTemplate edsEmailTemplate = emailTemplateManager.get(entityToEmailTemplate.getEmailTemplateId());
        DecimalFormat numFormat = new DecimalFormat(",##0.00");

        String clietName = "";
        String toEmail = "";
        String customerBalance = "";
        String supplierBalance = "";
        EdsUser sender = (senderID != null ? userManager.get(senderID) : invoiceManager.getUser());
        String companyName = sender.getCompany().getName();
        String companyContactEmail = sender.getEmail();

        EdsCrmContact crmContact = crmContactManager.get(entityToEmailTemplate.getMailReceiverId());
        if (crmContact != null) {
            toEmail = crmContact.getPrimaryEmail();
        }

        EdsCrmAccount crmAccount = crmAccountManager.get(entityToEmailTemplate.getEntityId());
        if (crmAccount != null) {
            clietName = crmAccount.getName();
//            if (!crmAccount.getBalanceCalculated()) {
            customerBalance = numFormat.format(crmAccountManager.getClientBalance(crmAccount.getObjectID()));
            supplierBalance = numFormat.format(crmAccountManager.getSupplierBalance(crmAccount.getObjectID()));
//            } else {
//                customerBalance = numFormat.format(crmAccount.getClientBalance());
//                supplierBalance = numFormat.format(crmAccount.getSupplierBalance());
//            }
        }

        Map<String, Object> values = new TreeMap<>();
        values.put(ET_COMPANY_NAME, companyName);
        values.put(ET_CUSTOMER, clietName);
        values.put(ET_SUPPLIER, clietName);
        values.put(ET_CUSTOMER_BALANCE, customerBalance);
        values.put(ET_SUPPLIER_BALANCE, supplierBalance);

        return generateEmailTemplateItem(edsEmailTemplate, values, toEmail, sender.getObjectID(), companyContactEmail);
    }

    public EmailTemplateItem generateRFQEmailTemplateData(EntityToEmailTemplate entityToEmailTemplate, Integer senderID) {
        EdsEmailTemplate edsEmailTemplate = emailTemplateManager.get(entityToEmailTemplate.getEmailTemplateId());

        String clientName = "";
        String projectName = "";
        String dueDate = "";
        String startDate = "";
        String rfqNumber = "";
        String toEmail = "";
        String rfqLink = "";

        EdsUser sender = (senderID != null ? userManager.get(senderID) : invoiceManager.getUser());
        String companyName = sender.getCompany().getName();
        String companyContactEmail = sender.getEmail();

        EdsCrmContact crmContact = crmContactManager.get(entityToEmailTemplate.getMailReceiverId());

        if (crmContact != null) {
            toEmail = crmContact.getPrimaryEmail();
        }

        EdsRFQ edsRFQ = rfqManager.get(entityToEmailTemplate.getEntityId());

        if (edsRFQ != null) {
            clientName = edsRFQ.getClient() != null ? edsRFQ.getClient().getName() : "";
            projectName = edsRFQ.getProject() != null ? edsRFQ.getProject().getName() : "";
            dueDate = Utils.formatDate(edsRFQ.getValidUntil(), sender.getCompany());
            startDate = Utils.formatDate(edsRFQ.getDate(), sender.getCompany());
            rfqNumber = edsRFQ.getNumber();
            String host = EdsContextParams.getHost(userManager.getUser().getCompany().getObjectID());
            rfqLink = host + "/Accounting.html?link=" + EncryptionHelper.encryptURL("requestforquote|summary/" + edsRFQ.getObjectID()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(userManager.getUser().getCompany().getObjectID().toString());
        }


        Map<String, Object> values = new TreeMap<>();
        values.put(ET_COMPANY_NAME, companyName);
        values.put(ET_CUSTOMER, clientName);
        values.put(ACCOUNTING.PROJECT, projectName);
        values.put(ET_DUE_DATE, dueDate);
        values.put(ET_START_DATE, startDate);
        values.put(ET_NUMBER, rfqNumber);
        values.put(ET_LINK, rfqLink);

        return generateEmailTemplateItem(edsEmailTemplate, values, toEmail, sender.getObjectID(), companyContactEmail);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateData(EntityToEmailTemplate entityToEmailTemplate, Integer senderID) {
        String invoiceCategory = entityToEmailTemplate.getEntityType();
        EdsEmailTemplate edsEmailTemplate = emailTemplateManager.get(entityToEmailTemplate.getEmailTemplateId());
        DecimalFormat numFormat = new DecimalFormat(",##0.00");

        Integer calculationScale = 2;
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings != null && financialSettings.getCalculationScale() != null) {
            calculationScale = financialSettings.getCalculationScale();
        }
        DecimalFormat decimalFormat = ServerUtils.getDecimalFormat(calculationScale);

        String invoiceDate = "";
        BigDecimal paidAmount = null;
        String invoiceDueDate = "";
        String clientName = "";
        String clientEmail = "";
        String invoiceNumber = "";
        String projectNumber = "";
        String supplierBalance = "";
        String totalAmount = "";
        String validUntil = "";
        String poNumber = "";
        String reference = "";
        String total = "";
        String totalInvoice = "";
        String paidInvoice = "";
        String currency = "";
        String clientPhone = "";
        String clientCountry = "";
        String productName = EdsContextParams.getProductName();
        String clickInvoicePaymentLink = "";
        String paymeInvoicePaymentLink = "";
        String paypalInvoicePaymentLink = "";
        String revolutInvoicePaymentLink = "";
        boolean isAccess = false;
        String clientContactFirstName = "", clientContactLastName = "", toEmail = "";
        if ((PURCHASE_ORDER_MANAGER_CATEGORY.equals(entityToEmailTemplate.getEntityType())
                || SALES_QUOTE_MANAGER_CATEGORY.equals(entityToEmailTemplate.getEntityType())
                || SALES_ORDER_CATEGORY.equals(entityToEmailTemplate.getEntityType()))
                && entityToEmailTemplate.getMailReceiverId() != null) {

            EdsUser user = userManager.get(entityToEmailTemplate.getMailReceiverId());
            clientContactFirstName = user != null ? user.getFirstName() : "";
            clientContactLastName = user != null ? user.getLastName() : "";
            toEmail = user != null ? user.getEmail() : "";
            isAccess = EMPLOYEE_STATUS_ACTIVE.equals(user != null ? user.getAccountStatus().getCode() : "");
        } else if (entityToEmailTemplate.getMailReceiverId() != null) {
            EdsCrmContact crmContact = crmContactManager.get(entityToEmailTemplate.getMailReceiverId());
            if (crmContact != null) {
                clientContactFirstName = crmContact.getFirstName();
                clientContactLastName = crmContact.getLastName();
                isAccess = crmContact.isAccessEnabled();
                toEmail = crmContact.getPrimaryEmail();
            }
        }

        EdsUser sender = (senderID != null ? userManager.get(senderID) : invoiceManager.getUser());
        String companyContactName = sender.getName();
        String companyName = sender.getCompany().getName();
        String companyContactEmail = sender.getEmail();
        String saleQuoteLink = "";
        String purchaseOrderLink = "";

        //invoice details
        if (invoiceCategory.equals(SALES_INVOICE_CATEGORY) || invoiceCategory.equals(RECEIPT_CATEGORY) || invoiceCategory.equals(CREDIT_NOTE_CATEGORY) || invoiceCategory.equals(PROJECT_BASE_INVOICE_CATEGORY)) {
            EdsInvoice edsInvoice = invoiceManager.get(entityToEmailTemplate.getEntityId());
            if (sender.getCompany() != null) {
                if (invoiceCategory.equals(RECEIPT_CATEGORY)) {
                    invoiceDate = Utils.formatDate(sender.getUserDate(edsInvoice.getPaidDate()), sender.getCompany());
                    paidAmount = edsInvoice.getFullPayments() != null ? edsInvoice.getFullPayments() : BigDecimal.ZERO;
                } else {
                    invoiceDate = Utils.formatDate(edsInvoice.getInvoiceDate(), sender.getCompany());
                }
                invoiceDueDate = Utils.formatDate(edsInvoice.getDueDate(), sender.getCompany());
            } else {
                if (invoiceCategory.equals(RECEIPT_CATEGORY)) {
                    invoiceDate = defaultShortDateFormat(sender.getUserDate(edsInvoice.getPaidDate()));
                } else {
                    invoiceDate = defaultShortDateFormat(sender.getUserDate(edsInvoice.getInvoiceDate()));
                }
                invoiceDueDate = defaultShortDateFormat(edsInvoice.getDueDate());
            }

            invoiceNumber = edsInvoice.getNumber();
            if (edsInvoice.getClientOrSupplier() != null) {
                clientName = edsInvoice.getClientOrSupplier().getName();
                clientEmail = edsInvoice.getClientOrSupplier().getEmail() != null ? edsInvoice.getClientOrSupplier().getEmail() : " ";
                clientPhone = edsInvoice.getClientOrSupplier().getPhone() != null ? edsInvoice.getClientOrSupplier().getPhone() : " ";
                clientCountry = edsInvoice.getClientOrSupplier().getPlaceOfSupplyCountry() != null ? edsInvoice.getClientOrSupplier().getPlaceOfSupplyCountry().getName() : " ";

            }
            poNumber = edsInvoice.getPoNumber();
            reference = edsInvoice.getReference();
            total = edsInvoice.getTotal() != null ? edsInvoice.getTotal().toString() : " ";
            totalInvoice = edsInvoice.getTotalInInvoiceCurrency() != null ? edsInvoice.getTotalInInvoiceCurrency().toString() : " ";
            paidInvoice = edsInvoice.getFullPaymentsInBase() != null ? edsInvoice.getFullPaymentsInBase().toString() : " ";
            currency = edsInvoice.getCurrency().getName() != null ? edsInvoice.getCurrency().getName() : " ";

        } else {
            EdsQuote edsQuote = quoteManager.get(entityToEmailTemplate.getEntityId());
            if (edsQuote != null) {
                totalAmount = numFormat.format(edsQuote.getTotal() != null ? edsQuote.getTotal() : BigDecimal.ZERO);
                poNumber = edsQuote.getPoNumber();
                if (sender.getCompany() != null) {
                    invoiceDate = Utils.formatDate(sender.getUserDate(edsQuote.getInvoiceDate()), sender.getCompany());
                    invoiceDueDate = Utils.formatDate(edsQuote.getDueDate(), sender.getCompany());
                } else {
                    invoiceDate = defaultShortDateFormat(sender.getUserDate(edsQuote.getInvoiceDate()));
                    invoiceDueDate = defaultShortDateFormat(edsQuote.getDueDate());
                }
                invoiceNumber = edsQuote.getNumber();
                clientName = edsQuote.getClientOrSupplier().getName();
                reference = edsQuote.getReference();
                if (edsQuote.getRelatedProject() != null) {
                    projectNumber = edsQuote.getRelatedProject().getNumber();
                }
//                if (!edsQuote.getClientOrSupplier().getBalanceCalculated()) {
                supplierBalance = numFormat.format(crmAccountManager.getSupplierBalance(edsQuote.getClientOrSupplier().getObjectID()));
//                } else {
//                    supplierBalance = numFormat.format(edsQuote.getClientOrSupplier().getSupplierBalance());
//                }
                EdsUser edsUser = userManager.getUser();
                if (edsUser != null && edsUser.getCompany() != null) {
                    String host = EdsContextParams.getHost(edsUser.getCompany().getObjectID());
                    if (edsQuote instanceof EdsSaleQuote) {
                        saleQuoteLink = host + "/Accounting.html?link=" + EncryptionHelper.encryptURL((invoiceCategory.equals(SALES_ORDER_CATEGORY) ? "saleorder|summary/" : "salequote|summary/") + edsQuote.getObjectID()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(edsUser.getCompany().getObjectID().toString());
                    } else if (edsQuote instanceof EdsPurchaseOrder) {
                        purchaseOrderLink = host + "/Accounting.html?link=" + EncryptionHelper.encryptURL("purchaseorder|summary/" + edsQuote.getObjectID()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(edsUser.getCompany().getObjectID().toString());
                    }
                }
            }
        }

        EdsInvoice edsInvoice = invoiceManager.get(entityToEmailTemplate.getEntityId());
        if (edsInvoice != null) {
            NewInvoice newInvoice = EdsInvoice.getInvoiceData(edsInvoice);
            clickInvoicePaymentLink = invoiceCircularResolver.getClickInvoicePaymentLink(newInvoice);
            paymeInvoicePaymentLink = invoiceCircularResolver.getPayMeInvoicePaymentLink(newInvoice);
            paypalInvoicePaymentLink = invoiceCircularResolver.getOrderPaymentLink(edsInvoice.getObjectID(), newInvoice, userManager.getUser().getCompany().getObjectID());
            if (edsInvoice instanceof EdsSaleInvoice) {
                revolutInvoicePaymentLink = ((EdsSaleInvoice) edsInvoice).getRevolutUrl() != null ? ((EdsSaleInvoice) edsInvoice).getRevolutUrl() : invoiceCircularResolver.getRevolutInvoicePaymentLink(newInvoice);
            }
        }

        // evaluating message text using template
        Map<String, Object> values = new TreeMap<>();
        values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(edsEmailTemplate.getFromUser())));
        values.put(EmailTemplateUtils.ET_FIRST_NAME, clientContactFirstName != null ? StringUtils.capitalize(clientContactFirstName) : "");
        values.put(EmailTemplateUtils.ET_LAST_NAME, clientContactLastName != null ? clientContactLastName : "");
        values.put(EmailTemplateUtils.ET_COMPANY_NAME, companyName != null ? companyName : "");
        values.put(EmailTemplateUtils.ET_CUSTOMER, clientName != null ? clientName : "");
        values.put(EmailTemplateUtils.ET_EMAIL, companyContactEmail != null ? companyContactEmail : "");
        values.put(EmailTemplateUtils.ET_START_DATE, invoiceDate != null ? invoiceDate : "");
        values.put(EmailTemplateUtils.ET_DUE_DATE, invoiceDueDate != null ? invoiceDueDate : "");
        values.put(EmailTemplateUtils.ET_NAME, companyContactName != null ? companyContactName : "");
        values.put(EmailTemplateUtils.ET_NUMBER, invoiceNumber != null ? invoiceNumber : "");
        values.put(EmailTemplateUtils.ET_PRODUCT_NAME, productName != null ? productName : "");
        values.put(EmailTemplateUtils.ET_SUPPLIER_BALANCE, supplierBalance != null ? supplierBalance : "");
        values.put(EmailTemplateUtils.ET_TOTAL_AMOUNT, totalAmount != null ? totalAmount : "");
        values.put(ACCOUNTING.REFERENCE, reference != null ? reference : "");
        values.put(EmailTemplateUtils.ET_LINK, purchaseOrderLink);
        values.put(EmailTemplateUtils.ET_PAID_AMOUNT, paidAmount != null ? decimalFormat.format(paidAmount) : "0.00");
        values.put("${type}", entityToEmailTemplate.getEntityType().equals(SALES_ORDER_CATEGORY) ? "Order" : "Quote");
        values.put(EmailTemplateConstants.CLICK_LINK, clickInvoicePaymentLink != null ? clickInvoicePaymentLink : "");
        values.put(EmailTemplateConstants.PAYME_LINK, paymeInvoicePaymentLink != null ? paymeInvoicePaymentLink : "");
        values.put(ACCOUNTING.CUSTOMER_EMAIL, clientEmail != null ? clientEmail : "");
        values.put(ACCOUNTING.CURRENCY, currency != null ? currency : "");
        values.put(ACCOUNTING.TOTAL, total != null ? total : "");
        values.put(ACCOUNTING.TOTAL_INVOICE, totalInvoice != null ? totalInvoice : "");
        values.put(ACCOUNTING.CUSTOMER_PHONE_NUMBER, clientPhone != null ? clientPhone : "");
        values.put(ACCOUNTING.CUSTOMER_COUNTRY, clientCountry != null ? clientCountry : "");
        values.put(PAYPAL_LINK, paypalInvoicePaymentLink != null ? paypalInvoicePaymentLink : "");
        values.put(REVOLUT_LINK, revolutInvoicePaymentLink != null ? revolutInvoicePaymentLink : "");
        values.put(PAID_AMOUNT_INVOICE, paidInvoice != null ? paidInvoice : "");
        if (poNumber != null && !"".equals(poNumber)) {
            values.put(ACCOUNTING.PO_NUMBER, poNumber);
        }
        if (projectNumber != null) {
            values.put(EmailTemplateUtils.ET_PROJECT_NUMBER, projectNumber);
        }
        if (isAccess) {
            String hasAccessLink = "";
            if (SALES_QUOTE_CATEGORY.equals(entityToEmailTemplate.getEntityType())) {
                hasAccessLink = "<p>You can either approve or reject the sales quote by clicking <a href='" + EmailTemplateConstants.ET_QUOTE_VIEW_LINK + "'>here</a>\n" +
                        " and logging in with your " + EdsContextParams.getProductName() + " username and password details. Once approved or rejected, a notification will be sent\n" +
                        " to your supplier. PDF version of the sales quote is attached for your attention.</p>";
            } else if (SALES_QUOTE_MANAGER_CATEGORY.equals(entityToEmailTemplate.getEntityType())) {
                hasAccessLink = "<p> Click <a href='" + saleQuoteLink + "'>here</a> to view the Sales Quote where you can approve or reject it.</p>";
            } else if (PURCHASE_ORDER_MANAGER_CATEGORY.equals(entityToEmailTemplate.getEntityType())) {
                hasAccessLink = "<p> Click <a href='" + purchaseOrderLink + "'>here</a> to view the Purchase Order where you can approve or reject it.</p>";
            } else if (SALES_ORDER_CATEGORY.equals(entityToEmailTemplate.getEntityType())) {
                hasAccessLink = "<p> Click <a href='" + saleQuoteLink + "'>here</a> to view the Sales Order where you can approve or reject it.</p>";
            }
            values.put(EmailTemplateUtils.ET_QUOTE_HAS_ACCESS_LINK, hasAccessLink);
            values.put(EmailTemplateConstants.ACCOUNTING.SALE_QUOTE_LINK, saleQuoteLink);

        } else {
            values.put(EmailTemplateUtils.ET_QUOTE_HAS_ACCESS_LINK, "");
            values.put(EmailTemplateConstants.ACCOUNTING.SALE_QUOTE_LINK, "");
        }

        return generateEmailTemplateItem(edsEmailTemplate, values, toEmail, sender.getObjectID(), companyContactEmail);
    }

    public static String defaultShortDateFormat(Date date) {
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }

    /**
     * Generate Email Template for Task assign
     *
     * @param employeeTask
     * @param user
     * @param entityType
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateForTask(EdsEmployeeTask employeeTask, EdsUser user, String entityType) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(entityType/*, user*/);//default email template
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = user;
            }
            EdsEmployee edsEmployee = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
            String to = edsEmployee.getEmail();
            Map<String, Object> values = new TreeMap<>();//generate template values
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_TASK_NUMBER, employeeTask.getTask().getNumber() != null ? employeeTask.getTask().getNumber() : "");
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(edsEmployee.getFirstName()));
            values.put(EmailTemplateUtils.ET_LAST_NAME, edsEmployee.getLastName());
            values.put(EmailTemplateUtils.ET_TASK_NAME, employeeTask.getTask().getName());
            values.put(EmailTemplateUtils.ET_DESCRIPTION, employeeTask.getTask().getDescription());
            values.put(EmailTemplateUtils.ET_PRIORITY, employeeTask.getTask().getPriority() != null ? referenceWfmMessageSource.localizeRef(employeeTask.getTask().getPriority()) : "");
            values.put(EmailTemplateUtils.ET_STATUS, employeeTask.getTask().getStatus() != null ? referenceWfmMessageSource.localizeRef(employeeTask.getTask().getStatus()) : "");
            values.put(EmailTemplateUtils.ET_COMPLETED, getCompletedPercent(employeeTask.getTask().getPercent() != null ? employeeTask.getTask().getPercent().intValue() : 0));
            values.put(EmailTemplateUtils.ET_CREATOR, employeeTask.getTask().getCreator().getName());
            values.put(EmailTemplateUtils.ET_PROJECT_NAME, employeeTask.getTask().getProject().getName());
            values.put(EmailTemplateUtils.ET_CUSTOMER, employeeTask.getTask().getProject().getClient() != null ? employeeTask.getTask().getProject().getClient().getName() : "");
            values.put(EmailTemplateUtils.ET_ASSIGNEES, mergeAssignees(employeeTask.getTask().getUnDeletedAssignments()));
            values.put(EmailTemplateUtils.ET_START_DATE, employeeTask.getTask().getStartDate() != null ? ServerUtils.shortDateFormat(employeeTask.getTask().getStartDate(), user) : "");
            values.put(EmailTemplateUtils.ET_DUE_DATE, employeeTask.getTask().getDueDate() != null ? ServerUtils.shortDateFormat(employeeTask.getTask().getDueDate(), user) : "");
            values.put(EmailTemplateUtils.ET_DATE, formatDate(user.getCompany().getCompanyDate()));
            values.put(EmailTemplateUtils.ET_ESTIMATED_TIME, employeeTask.getTask().getEstimatedTime() != null ? Utils.timeSpentToString(employeeTask.getTask().getEstimatedTime()) : "");
            String taskURL = EdsContextParams.getHost(edsEmployee.getCompany().getObjectID()) + "/ProjectManagement.html?link=" + EncryptionHelper.encryptURL("task/" + employeeTask.getTask().getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(edsEmployee.getObjectID().toString());
            values.put(EmailTemplateUtils.ET_URL, taskURL);

            return generateEmailTemplateItem(emailTemplate, values, to, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    /**
     * Generate Email Template for Task Update
     *
     * @param receiver   - receiver
     * @param user       - user
     * @param entityType - entityType
     * @return - Object
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateForTaskUpdate(EdsUser receiver, EdsUser user, EdsTask task, String entityType) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(entityType);//default email template
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = user;
            }
            String to = receiver.getEmail();
            Map<String, Object> values = new TreeMap<>();//generate template values
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_TASK_NUMBER, task.getNumber() != null ? task.getNumber() : "");
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(receiver.getFirstName()));
            values.put(EmailTemplateUtils.ET_LAST_NAME, receiver.getLastName());
            values.put(EmailTemplateUtils.ET_TASK_NAME, task.getName());
            values.put(EmailTemplateUtils.ET_DESCRIPTION, task.getDescription());
            values.put(EmailTemplateUtils.ET_PRIORITY, task.getPriority() != null ? referenceWfmMessageSource.localizeRef(task.getPriority()) : "");
            values.put(EmailTemplateUtils.ET_STATUS, task.getStatus() != null ? referenceWfmMessageSource.localizeRef(task.getStatus()) : "");
            values.put(EmailTemplateUtils.ET_COMPLETED, getCompletedPercent(task.getPercent() != null ? task.getPercent().intValue() : 0));
            values.put(EmailTemplateUtils.ET_CREATOR, task.getCreator().getName());
            values.put(EmailTemplateUtils.ET_PROJECT_NAME, task.getProject().getName());
            values.put(EmailTemplateUtils.ET_CUSTOMER, task.getProject().getClient() != null ? task.getProject().getClient().getName() : "");
            values.put(EmailTemplateUtils.ET_ASSIGNEES, mergeAssignees(task.getUnDeletedAssignments()));
            values.put(EmailTemplateUtils.ET_START_DATE, task.getStartDate() != null ? ServerUtils.shortDateFormat(task.getStartDate(), user) : "");
            values.put(EmailTemplateUtils.ET_DUE_DATE, task.getDueDate() != null ? ServerUtils.shortDateFormat(task.getDueDate(), user) : "");
            values.put(EmailTemplateUtils.ET_DATE, formatDate(user.getCompany().getCompanyDate()));
            values.put(EmailTemplateUtils.ET_ESTIMATED_TIME, task.getEstimatedTime() != null ? Utils.timeSpentToString(task.getEstimatedTime()) : "");
            Integer companyID = receiver.getCompany().getObjectID();
            String taskURL = EdsContextParams.getHost(companyID) + "/ProjectManagement.html?link=" + EncryptionHelper.encryptURL("task/" + task.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(receiver.getObjectID().toString()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(companyID.toString());
            values.put(EmailTemplateUtils.ET_URL, taskURL);

            return generateEmailTemplateItem(emailTemplate, values, to, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    private String getSignature(EdsUser user) {
        EdsSignature signature = signatureManager.getByUser(user);
        return signature != null ? signature.getSignature() : "";
    }

    /**
     * Related Multi Task assign
     *
     * @param creator  - creator
     * @param employee - employee
     * @param tasks    - tasks
     * @return EmailTemplateItem
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateItemForMultiTaskAssign(EdsUser creator, EdsEmployee employee, HashSet<EdsTask> tasks) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(MULTI_TASK_ASSIGN_CATEGORY);
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = creator;
            }
            String to = employee.getEmail();
            Integer companyID = creator.getCompany().getObjectID();
            Map<String, Object> values = new TreeMap<>();//generate template values
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(employee.getFirstName()));
            values.put(EmailTemplateUtils.ET_LAST_NAME, employee.getLastName());
            values.put(EmailTemplateUtils.ET_EMAIL, employee.getEmail());
            values.put(EmailTemplateUtils.ET_CREATOR, creator.getName());
            values.put(EmailTemplateUtils.ET_DATE, formatDate(employee.getUserDate(employee.getCompany().getCompanyDate())));

            StringBuilder taskNames = new StringBuilder();
            boolean isFirstName = true;
            for (EdsTask task : tasks) {
                String link = EncryptionHelper.encryptURL("task/" + task.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(companyID.toString());
                String hostLink = "<a href='" + EdsContextParams.getHost(companyID) + "/ProjectManagement.html?link=" + link + "' title='View task'>" + task.getName() + "</a>";
                if (isFirstName) {
                    taskNames.append(hostLink);
                    isFirstName = false;
                } else {
                    taskNames.append("<br>").append(hostLink);
                }
            }
            values.put(EmailTemplateUtils.ET_TASKS, taskNames.toString());
            return generateEmailTemplateItem(emailTemplate, values, to, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    /**
     * Related date format
     *
     * @param date
     * @return
     */
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMMM, yyyy", Locale.ENGLISH);
        return dateFormat.format(date);
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

    public static String defaultLongDateFormat(Date date) {
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);
        return formatter.format(date);
    }

    /**
     * Related percent completed format
     *
     * @param percent
     * @return
     */
    private String getCompletedPercent(Integer percent) {
        return " " + percent + "%";
    }

    /**
     * Related assignees
     *
     * @param assignees
     * @return
     */
    private String mergeAssignees(Set<EdsEmployeeTask> assignees) {
        StringBuilder stringBuffer = new StringBuilder();
        Iterator<EdsEmployeeTask> iterator = employeeTaskManager.sort(assignees).iterator();
        if (iterator.hasNext()) {
            stringBuffer.append(iterator.next().getProjectEmployee().getEmployeeDepartment().getEmployee().getName());
        }
        while (iterator.hasNext()) {
            stringBuffer.append(',');
            stringBuffer.append(iterator.next().getProjectEmployee().getEmployeeDepartment().getEmployee().getName());
        }
        return stringBuffer.toString();
    }

    /**
     * Related Date format
     *
     * @param event
     * @param owner
     * @return
     */
    private String getDate(EdsEvent event, EdsUser owner) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM");
        SimpleDateFormat hourFormat = new SimpleDateFormat("k:mm");
        String startDate = dateFormat.format(owner.getUserDate(event.getStartDate()));
        String startHour = hourFormat.format(owner.getUserDate(event.getStartDate()));
        startHour = startHour.equals("24:00") ? "00:00" : startHour;
        String endDate = dateFormat.format(owner.getUserDate(event.getEndDate()));
        String endHour = hourFormat.format(owner.getUserDate(event.getEndDate()));
        endHour = endHour.equals("24:00") ? "00:00" : endHour;
        String date = "";
        if (event.isAllDay() == null || event.isAllDay()) {
            date = startDate;
        } else if (startDate.equals(endDate)) {
            date = startDate + " " + startHour + " - " + endHour;
        } else {
            date = startDate + " " + startHour + " - " + endDate + " " + endHour;
        }

        return date + " (" + owner.getUserTimezone().getID() + ")";
    }

    /**
     * Related if not description message
     *
     * @param description
     * @return
     */
    private String getDescription(String description) {
        return description != null ? description : "<i>You have no description for this event.</i>";
    }

    /**
     * Related event attendees
     *
     * @param event   - event
     * @param ownerID - owner id
     * @return
     */
    private String getEventAttendees(EdsEvent event, Integer ownerID) {

        return "";
    }

    /**
     * Related event guests
     *
     * @param event - event
     * @return
     */
    private String getEventGuests(EdsEvent event) {
        if (event != null) {
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
        }
        return "";
    }

    /**
     * Related event guest email
     *
     * @param guestEmail - guest email
     * @param companyID  - company id
     * @return
     */
    private String getEventGuestName(String guestEmail, Integer companyID) {
        String userName = "";
        EdsCrmContact contact = crmContactManager.getContactByEmail(guestEmail, companyID);
        if (contact != null) {
            userName = contact.getName();
        }
        return !"".equals(userName) ? userName : guestEmail;
    }

    //    /**

    private String getLocation(String location) {
        return location != null ? location : "<i>No location appointment for this event.</i>";
    }

    /**
     * Generate Email Template for Calendar Events -- send reminder or add event or share event
     *
     * @param employeeEvent
     * @param attendees
     * @param entityType
     * @return EmailTemplateItem
     * @throws EdsTemplateException
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generatedEmailTemplateItems(EdsEmployeeEvent employeeEvent, ArrayList<EdsUser> attendees, String entityType) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(entityType);//default email template
        if (emailTemplate != null) {
            EdsUser edsUser = employeeEvent.getEmployee();
            EdsEvent event = employeeEvent.getEvent();
            EdsUser fromUser;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                fromUser = userManager.get(emailTemplate.getFromUser());
            } else if (CALENDAR_EVENT_REMINDER_CATEGORY.equals(entityType)) {
                fromUser = event.getOwner().getEmployee();
            } else {
                fromUser = employeeEvent.getEmployee();
            }
            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_USER_NAME, event.getOwner().getName());
            values.put(EmailTemplateUtils.ET_NAME, event.getSubject());
            values.put(EmailTemplateUtils.ET_DESCRIPTION, getDescription(event.getDescription()));
            values.put(EmailTemplateUtils.ET_LOCATION, getLocation(event.getVenue()));
            values.put(EmailTemplateUtils.ET_WHEN, getDate(event, edsUser));
            values.put(EmailTemplateUtils.ET_CREATOR, edsUser.getName());
            String toEmail = edsUser.getEmail();
            if (attendees != null) {
                StringBuilder sharedEmployees = new StringBuilder();
                for (EdsUser sharedEmployee : attendees) {
                    sharedEmployees.append("<b>" + sharedEmployee.getName() + "</b><br>");
                }
                values.put(EmailTemplateUtils.ET_SHARED_EMPLOYEES, sharedEmployees.toString());
            } else {
                values.put(EmailTemplateUtils.ET_SHARED_EMPLOYEES, "");
            }
            String guests = getEventGuests(event);
            values.put(EmailTemplateUtils.ET_GUESTS, StringUtils.isNotEmpty(guests) ? "<p>Guests: " + guests + "</p>" : "");
            String calendarURL = EdsContextParams.getHost(edsUser.getCompany().getObjectID()) + "/Crm.html#event|summary/" + event.getObjectID().toString();
            values.put(EmailTemplateUtils.ET_URL, calendarURL);

            return generateEmailTemplateItem(emailTemplate, values, toEmail, fromUser.getObjectID(), fromUser.getEmail());
        } else {
            return null;
        }
    }

    /**
     * Generate Email Template for Calendar Events -- update or delete calendar events
     *
     * @param event
     * @param attendee
     * @param entityType
     * @return EmailTemplateItem
     * @throws EdsTemplateException
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generatedEmailTemplateItems(EdsEvent event, EdsUser attendee, String entityType) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(entityType);//default email template
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = userManager.getUser();
            }
            String guests = getEventGuests(event);
            EdsEmployee owner = event.getOwner();
            String attendeeList = getEventAttendees(event, owner.getObjectID());
            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_USER_NAME, owner.getName());
            values.put(EmailTemplateUtils.ET_NAME, event.getSubject());
            values.put(EmailTemplateUtils.ET_DESCRIPTION, getDescription(event.getDescription()));
            values.put(EmailTemplateUtils.ET_LOCATION, getLocation(event.getVenue()));
            values.put(EmailTemplateUtils.ET_WHEN, getDate(event, owner));
            values.put(EmailTemplateUtils.ET_CREATOR, owner.getName());
            values.put(EmailTemplateUtils.ET_GUESTS, !"".equals(guests) ? "<p>Guests: " + guests + "</p>" : "");
            values.put(EmailTemplateUtils.ET_SHARED_EMPLOYEES, !"".equals(attendeeList) ? "<p>Shared with: " + attendeeList + "</p>" : "");
            String toEmail = null;
            toEmail = Objects.requireNonNullElse(attendee, owner).getEmail();
            String calendarURL = EdsContextParams.getHost(owner.getCompany().getObjectID()) + "/Crm.html#event|summary/" + event.getObjectID().toString();
            values.put(EmailTemplateUtils.ET_URL, calendarURL);

            return generateEmailTemplateItem(emailTemplate, values, toEmail, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generatedReportTemplateItems(EdsReport report, EdsUser attendee, String entityType) {
        EdsEmailTemplate emailTemplate = report.getEmailTemplate();
        if (emailTemplate == null) {
            emailTemplate = emailTemplateManager.getEmailTemplateByCategory(entityType);
        }
        if (emailTemplate != null) {
            EdsUser fromUser = null;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                fromUser = userManager.get(emailTemplate.getFromUser());
            }

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_NAME, report.getName());
            values.put(EmailTemplateUtils.ET_DESCRIPTION, report.getDescription());
            String toEmail = attendee.getEmail();
            Integer fromUserID = fromUser != null ? fromUser.getObjectID() : attendee.getObjectID();
            String fromUserEmail = fromUser != null ? fromUser.getEmail() : EdsContextParams.getSupportEmail();
            return generateEmailTemplateItem(emailTemplate, values, toEmail, fromUserID, fromUserEmail);
        } else {
            return null;
        }
    }

    /**
     * Generate Email Template for Expense Claim
     *
     * @param entityToEmailTemplate
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateExpenseClaimTemplateItem(EntityToEmailTemplate entityToEmailTemplate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        EdsEmailTemplate expenseTemplate = emailTemplateManager.get(entityToEmailTemplate.getEmailTemplateId());
        EdsExpenseReport expenseReport = expenseReportManager.get(entityToEmailTemplate.getEntityId());

        EdsUser approver = null;
        if (expenseReport.getCurrentApprover() != null && expenseReport.getCurrentApprover().getExactEmployee() != null) {
            approver = expenseReport.getCurrentApprover().getExactEmployee();
        }
        if (approver == null) {
            approver = userManager.getUser().getEmployee();
        }

        String approverFirstName = approver.getFirstName();
        String approverLastName = approver.getLastName();

        EdsUser sender = emailTemplateManager.getUser();
        String senderCompanyName = sender.getCompany().getName();
        String senderEmail = sender.getEmail();

        String expenseStartDate = dateFormat.format(new DateNonConvertable(expenseReport.getStartDate()).getNonConvertedDate());
        String expenseEndDate = "";
        String expenseAmount = expenseAmountTot(expenseReport);
        String expenseReportTitle = expenseReport.getTitle();

        String statusCode = expenseReport.getStatus() == null ? "" : expenseReport.getStatus().getCode();
        EdsExpenseHistory exHistory = expenseServiceLocal.getEventLastHistoryRecord(statusCode, expenseReport.getObjectID());
        Date submitDate = exHistory != null ? exHistory.getEventDate() : new Date();

        /*submit or resubmit date*/
        EdsExpenseHistory expenseDeclined = expenseServiceLocal.getEventLastHistoryRecord(EXPENSE_DECLINED, expenseReport.getObjectID());
        Date declineDate = expenseDeclined != null ? expenseDeclined.getEventDate() : null;
        String expenseSubmitDate;
        String expenseResubmitDate;
        String expenseDeclineDate;
        if (sender != null) {
            expenseSubmitDate = Utils.formatDate(submitDate, sender.getCompany());
            expenseResubmitDate = Utils.formatDate(submitDate, sender.getCompany());
            expenseDeclineDate = declineDate != null ? Utils.formatDate(declineDate, sender.getCompany()) : "";
        } else {
            expenseSubmitDate = MessageManagerImpl.defaultShortDateFormat(submitDate);
            expenseResubmitDate = MessageManagerImpl.defaultShortDateFormat(submitDate);
            expenseDeclineDate = declineDate != null ? MessageManagerImpl.defaultShortDateFormat(declineDate) : "";
        }

        String productName = EdsContextParams.getProductName();

        Map<String, Object> values = new TreeMap<>();
        values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(expenseTemplate.getFromUser())));
        values.put(EmailTemplateUtils.ET_EXPENSE_REPORTER_FIRST_NAME, StringUtils.capitalize(expenseReport.getReporter().getFirstName()));
        values.put(EmailTemplateUtils.ET_EXPENSE_REPORTER_LAST_NAME, expenseReport.getReporter().getLastName());
        values.put(EmailTemplateUtils.ET_EXPENSE_APPROVER_FIRST_NAME, StringUtils.capitalize(approverFirstName));
        values.put(EmailTemplateUtils.ET_EXPENSE_APPROVER_LAST_NAME, approverLastName);
        values.put(EmailTemplateUtils.ET_EXPENSE_REPORT_TITLE, expenseReportTitle);
        values.put(EmailTemplateUtils.ET_EXPENSE_REPORT_AMOUNT, expenseAmount);
        values.put(EmailTemplateUtils.ET_START_DATE, expenseStartDate);
        values.put(EmailTemplateUtils.ET_END_DATE, expenseEndDate);

        values.put(EmailTemplateUtils.ET_COMPANY_NAME, senderCompanyName);
        values.put(EmailTemplateUtils.ET_EXPENSE_SUBMIT_DATE, expenseSubmitDate);
        values.put(EmailTemplateUtils.ET_EXPENSE_RESUBMIT_DATE, expenseResubmitDate);
        values.put(EmailTemplateUtils.ET_EXPENSE_DECLINE_DATE, expenseDeclineDate);
        values.put(EmailTemplateUtils.ET_PRODUCT_NAME, productName);

        return generateEmailTemplateItem(expenseTemplate, values, approver.getEmail(), sender.getObjectID(), senderEmail);
    }

    /**
     * Related Expense Amount
     *
     * @param expenseReport - expenseReport
     * @return - expense Amount Total
     */
    private String expenseAmountTot(EdsExpenseReport expenseReport) {
        String baseCurrencySymbol = expenseReport.getBaseCurrency() != null && expenseReport.getBaseCurrency().getSymbol() != null ? expenseReport.getBaseCurrency().getSymbol() : "";
        String baseCurrencyName = expenseReport.getBaseCurrency() != null && expenseReport.getBaseCurrency().getName() != null ? expenseReport.getBaseCurrency().getName() : "";
        return baseCurrencySymbol + " " + (expenseReport.getBaseTotal() != null ? Utils.formatDouble(expenseReport.getBaseTotal().doubleValue()) : "") + " " + baseCurrencyName;
    }

    /**
     * Generate Email Template for Batch Payment
     *
     * @param entityToEmailTemplate
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateBatchPaymentTemplateItem(EntityToEmailTemplate entityToEmailTemplate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        EdsEmailTemplate batchPaymentTemplate = emailTemplateManager.get(entityToEmailTemplate.getEmailTemplateId());
        EdsBatchPayment batchPayment = batchPaymentManager.get(entityToEmailTemplate.getEntityId());

        EdsUser approver = userManager.getUser().getEmployee();
        String approverFirstName = approver.getFirstName();
        String approverLastName = approver.getLastName();

        String clientName = "";
        String to = "";
        if (batchPayment.getCrmAccount() != null) {
            EdsCrmAccount account = batchPayment.getCrmAccount();
            clientName = account.getName();
            to = account.getEmail();
        }

        EdsUser sender = emailTemplateManager.getUser();
        String senderCompanyName = sender.getCompany().getName();
        String senderEmail = sender.getEmail();

        String paymentDate = dateFormat.format(new DateNonConvertable(batchPayment.getDate()).getNonConvertedDate());
        String paymentAmount = paymentAmountTot(batchPayment);
        String accountName = "";
        if (batchPayment.getAccount() != null) {
            EdsAccount account = batchPayment.getAccount();
            accountName = account.getName();
        }
        String type = batchPayment.getType();
        String number = batchPayment.getNumber();
        String link = EncryptionHelper.encryptURL("receivepayment|summary/" + batchPayment.getObjectID() + "/" + RECEIVABLE);
        String productName = EdsContextParams.getProductName();

        Map<String, Object> values = new TreeMap<>();
        values.put(EmailTemplateUtils.ET_PAYMENT_APPROVER_FIRST_NAME, approverFirstName);
        values.put(EmailTemplateUtils.ET_PAYMENT_APPROVER_LAST_NAME, approverLastName);
        values.put(EmailTemplateUtils.ET_PAYMENT_APPROVER_EMAIL, senderEmail);
        values.put(EmailTemplateUtils.ET_PAYMENT_DATE, paymentDate);
        values.put(EmailTemplateUtils.ET_PAYMENT_AMOUNT, paymentAmount);
        values.put(EmailTemplateUtils.ET_PAYMENT_TYPE, type);
        values.put(EmailTemplateUtils.ET_PAYMENT_NUMBER, number);
        values.put(EmailTemplateUtils.ET_PAYMENT_LINK, link);
        values.put(EmailTemplateUtils.ET_PAYMENT_PRODUCT, productName);
        values.put(EmailTemplateUtils.ET_PAYMENT_CUSTOMER, clientName);
        values.put(EmailTemplateUtils.ET_PAYMENT_COMPANY_NAME, senderCompanyName);

        return generateEmailTemplateItem(batchPaymentTemplate, values, to, sender.getObjectID(), senderEmail);
    }

    /**
     * Related Batch Bayment Amount
     *
     * @param batchPayment - batchPayment
     * @return - payment amount total
     */
    private String paymentAmountTot(EdsBatchPayment batchPayment) {
        String baseCurrencySymbol = batchPayment.getCurrency() != null && batchPayment.getCurrency().getSymbol() != null ? batchPayment.getCurrency().getSymbol() : "";
        String baseCurrencyName = batchPayment.getCurrency() != null && batchPayment.getCurrency().getName() != null ? batchPayment.getCurrency().getName() : "";
        return baseCurrencySymbol + " " + (batchPayment.getTotalAmount() != null ? Utils.formatDouble(batchPayment.getTotalAmount().doubleValue()) : "") + " " + baseCurrencyName;
    }

    /**
     * Generate Email Template for Project create
     *
     * @param project  - project
     * @param employee - employee
     * @param user     - user
     * @return - EmailTemplateItem
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateProjectTemplateItem(EdsProject project, EdsUser employee, EdsUser user, String category) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(category/*, user*/);//default email template
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = user;
            }
            String to = employee.getEmail();
            Map<String, Object> values = new TreeMap<>();//generate template values
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_PROJECT_NUMBER, project.getNumber() != null ? project.getNumber() : "");
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(employee.getFirstName()));
            values.put(EmailTemplateUtils.ET_LAST_NAME, employee.getLastName());
            values.put(EmailTemplateUtils.ET_PROJECT_NAME, project.getName());
            values.put(EmailTemplateUtils.ET_MANAGER_NAME, project.getManager().getName());
            values.put(EmailTemplateUtils.ET_CREATOR, user.getName());
            values.put(EmailTemplateUtils.ET_DATE, formatDate(user.getCompany().getCompanyDate()));
            String projectURL = EdsContextParams.getHost(employee.getCompany().getObjectID()) + "/ProjectManagement.html?link=" + EncryptionHelper.encryptURL("project/" + project.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString());
            values.put(EmailTemplateUtils.ET_URL, projectURL);

            return generateEmailTemplateItem(emailTemplate, values, to, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    /**
     * Generate Email Template for Project assign
     *
     * @param projectEmployee - projectEmployee
     * @param user            - user
     * @param entityType      - entityType
     * @return - EmailTemplateItem
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateProjectAssignTemplateItem(EdsProjectEmployee projectEmployee, EdsUser user, String entityType) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(entityType/*, user*/);//default email template
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = user;
            }
            EdsEmployee edsEmployee = projectEmployee.getEmployeeDepartment().getEmployee();
            String to = edsEmployee.getEmail();
            Map<String, Object> values = new TreeMap<>();//generate template values
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_PROJECT_NUMBER, projectEmployee.getProject().getNumber() != null ? projectEmployee.getProject().getNumber() : "");
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(edsEmployee.getFirstName()));
            values.put(EmailTemplateUtils.ET_LAST_NAME, edsEmployee.getLastName());
            values.put(EmailTemplateUtils.ET_PROJECT_NAME, projectEmployee.getProject().getName());
            values.put(EmailTemplateUtils.ET_MANAGER_NAME, projectEmployee.getProject().getManager().getName());
            values.put(EmailTemplateUtils.ET_CREATOR, user.getName());
            values.put(EmailTemplateUtils.ET_DATE, formatDate(user.getCompany().getCompanyDate()));
            String projectURL = EdsContextParams.getHost(edsEmployee.getCompany().getObjectID()) + "/ProjectManagement.html?link=" + EncryptionHelper.encryptURL("project/" + projectEmployee.getProject().getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(edsEmployee.getObjectID().toString());
            values.put(EmailTemplateUtils.ET_URL, projectURL);

            return generateEmailTemplateItem(emailTemplate, values, to, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    /**
     * Generate Email Template for Project manager/backup manager assign
     *
     * @param project    - project
     * @param employee   - employee
     * @param user       - user
     * @param entityType - entityType
     * @return - EmailTemplateItem
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateProjectManagerAssignTemplateItem(EdsProject project, EdsEmployee employee, EdsUser user, String entityType) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(entityType/*, user*/);//default email template
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = user;
            }
            String to = employee.getEmail();
            Map<String, Object> values = new TreeMap<>();//generate template values
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_PROJECT_NUMBER, project.getNumber() != null ? project.getNumber() : "");
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(employee.getFirstName()));
            values.put(EmailTemplateUtils.ET_LAST_NAME, employee.getLastName());
            values.put(EmailTemplateUtils.ET_PROJECT_NAME, project.getName());
            values.put(EmailTemplateUtils.ET_MANAGER_NAME, project.getManager().getName());
            values.put(EmailTemplateUtils.ET_CREATOR, user.getName());
            values.put(EmailTemplateUtils.ET_DATE, formatDate(user.getCompany().getCompanyDate()));
            String projectURL = EdsContextParams.getHost(employee.getCompany().getObjectID()) + "/ProjectManagement.html?link=" + EncryptionHelper.encryptURL("project/" + project.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString());
            values.put(EmailTemplateUtils.ET_URL, projectURL);

            return generateEmailTemplateItem(emailTemplate, values, to, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    /**
     * Generate Email Template for Message Center
     *
     * @param entityToEmailTemplate - entityToEmailTemplate
     * @return - EmailTemplateItem
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateMessageCenterTemplateItem(EntityToEmailTemplate entityToEmailTemplate, Integer rfqId, Integer employeeId, Integer opportunityId) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.get(entityToEmailTemplate.getEmailTemplateId());//default email template
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                if (entityToEmailTemplate.getMailSenderId() != null) {
                    from = userManager.get(entityToEmailTemplate.getMailSenderId());
                } else {
                    from = userManager.getUser();
                }
            }
            String recipientFirstName = entityToEmailTemplate.getMailReceiverFirstName();
            String recipientLastName = entityToEmailTemplate.getMailReceiverLastName();
            String recipientEmail = entityToEmailTemplate.getMailReceiverEmail();
            if (recipientEmail != null && recipientEmail.contains("<")) {
                recipientEmail = recipientEmail.substring(recipientEmail.indexOf("<") + 1, recipientEmail.lastIndexOf(">"));
            }
            String recipientCompanyName = entityToEmailTemplate.getMailReceiverCompanyName();
            EdsCrmContact cl = crmContactManager.getOneByPrimaryEmail(recipientEmail);
            if (cl != null) {
                recipientFirstName = cl.getFirstName() == null ? " " : cl.getFirstName();
                recipientLastName = cl.getLastName() == null ? " " : cl.getLastName();
                recipientEmail = cl.getPrimaryEmail();
                if (cl.getCrmAccount() != null) {
                    recipientCompanyName = cl.getCrmAccount().getName();
                }
            }

            if (recipientFirstName == null && recipientEmail != null && !"".equals(recipientEmail)) {
                if (recipientEmail.contains("<")) {
                    recipientFirstName = recipientEmail.substring(0, recipientEmail.indexOf("<"));
                } else {
                    recipientFirstName = recipientEmail.substring(0, recipientEmail.indexOf("@"));
                }
            }
            String senderFirstName = (entityToEmailTemplate.getMailSenderFirstName() != null && !"".equals(entityToEmailTemplate.getMailSenderFirstName())) ?
                    entityToEmailTemplate.getMailSenderFirstName() : from.getFirstName();
            String senderLastName = (entityToEmailTemplate.getMailSenderLastName() != null && !"".equals(entityToEmailTemplate.getMailSenderLastName())) ?
                    entityToEmailTemplate.getMailSenderLastName() : from.getLastName();
            String senderEmail = (entityToEmailTemplate.getMailSenderEmail() != null && !"".equals(entityToEmailTemplate.getMailSenderEmail())) ?
                    entityToEmailTemplate.getMailSenderEmail() : from.getEmail();
            String senderCompanyName = (entityToEmailTemplate.getMailSenderCompanyName() != null && !"".equals(entityToEmailTemplate.getMailSenderCompanyName())) ?
                    entityToEmailTemplate.getMailSenderCompanyName() : from.getCompany().getName();
            String senderPrimaryPhone;
            String senderMobilePhone = "";
            EdsEmployee employee = employeeManager.get(from.getObjectID());
            if (from.isClientContact()) {
                senderPrimaryPhone = from.getClientContact() != null ? (from.getClientContact().getCrmContact() != null ?
                        (from.getClientContact().getCrmContact().getPrimaryPhone() != null ? from.getClientContact().getEmployee().getPrimaryPhone() : "") : "") : "";
            } else {
                senderPrimaryPhone = employee != null ? (employee.getPrimaryPhone() != null ? employee.getPrimaryPhone() : "") : "";
            }
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

            String jobTitle = employee.getContact().getJobTitles() != null ? employee.getContact().getJobTitles() : "";

            Map<String, Object> values = new TreeMap<>();//generate template values

            if (emailTemplate.getModule() != null) {
                if (emailTemplate.getModule().getCode() != null && ET_LEAD_MODULE.equals(emailTemplate.getModule().getCode()) && cl != null && cl.getContactType() == EdsCrmContact.LEAD_CONTACT) {
                    values = generateTemplateForLead(cl, from);
                } else if (emailTemplate.getModule().getCode() != null && ET_LEAD_MODULE.equals(emailTemplate.getModule().getCode()) && cl != null && cl.getContactType() == EdsCrmContact.CRM_CONTACT) {
                    values = generateTemplateForContact(cl, from);
                } else if (emailTemplate.getModule().getCode() != null && ET_RFQ_MODULE.equals(emailTemplate.getModule().getCode()) && rfqId != null) {
                    EdsRFQ rfq = rfqManager.get(rfqId);
                    values = generateTemplateForRFQ(cl, rfq, from);
                } else if (emailTemplate.getModule().getCode() != null && ET_CANDIDATE_MODULE.equals(emailTemplate.getModule().getCode()) && cl != null && cl.getContactType() == EdsCrmContact.CANDIDATE) {
                    values = generateTemplateForCandidate(cl, from);
                } else if (emailTemplate.getModule().getCode() != null && ET_OPPORTUNITY_MODULE.equals(emailTemplate.getModule().getCode())) {
                    EdsOpportunity edsOpportunity = opportunityManager.get(opportunityId);
                    values = allInOneServiceLocal.getAdditionalFieldValuesAsMap(RelationItem.TYPE_OPPORTUNITY, edsOpportunity, values, userManager.getUser());
                } else {
                    if (ET_EMPLOYEE_MODULE.equals(emailTemplate.getModule().getCode()) && employeeId != null) {
                        EdsEmployee emp = employeeManager.get(employeeId);
                        values = emp.getFieldValueAsMap(modelFieldManager.getFieldIDs(LayoutRPC.getFormIDByRelationType(RelationItem.TYPE_EMPLOYEE)));
                        values = allInOneServiceLocal.getAdditionalFieldValuesAsMap(RelationItem.TYPE_EMPLOYEE, emp, values, userManager.getUser());
                    }
                    values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
                    values.put(EmailTemplateUtils.ET_RECIPIENT_FIRST_NAME, StringUtils.capitalize(recipientFirstName));
                    values.put(EmailTemplateUtils.ET_RECIPIENT_LAST_NAME, recipientLastName);
                    values.put(EmailTemplateUtils.ET_RECIPIENT_EMAIL, recipientEmail);
                    values.put(EmailTemplateUtils.ET_RECIPIENT_COMPANY_NAME, recipientCompanyName);
                    values.put(EmailTemplateUtils.ET_SENDER_FIRST_NAME, StringUtils.capitalize(senderFirstName));
                    values.put(EmailTemplateUtils.ET_SENDER_LAST_NAME, senderLastName);
                    values.put(EmailTemplateUtils.ET_SENDER_EMAIL, senderEmail);
                    values.put(EmailTemplateUtils.ET_SENDER_COMPANY_NAME, senderCompanyName);
                    values.put(EmailTemplateUtils.ET_SENDER_PRIMARY_PHONE, senderPrimaryPhone);
                    values.put(EmailTemplateUtils.ET_SENDER_MOBILE_PHONE, senderMobilePhone);
                    values.put(EmailTemplateUtils.ET_SENDER_JOB_TITLE, jobTitle);
                }
            } else {

                values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
                values.put(EmailTemplateUtils.ET_RECIPIENT_FIRST_NAME, StringUtils.capitalize(recipientFirstName));
                values.put(EmailTemplateUtils.ET_RECIPIENT_LAST_NAME, recipientLastName);
                values.put(EmailTemplateUtils.ET_RECIPIENT_EMAIL, recipientEmail);
                values.put(EmailTemplateUtils.ET_RECIPIENT_COMPANY_NAME, recipientCompanyName);
                values.put(EmailTemplateUtils.ET_SENDER_FIRST_NAME, StringUtils.capitalize(senderFirstName));
                values.put(EmailTemplateUtils.ET_SENDER_LAST_NAME, senderLastName);
                values.put(EmailTemplateUtils.ET_SENDER_EMAIL, senderEmail);
                values.put(EmailTemplateUtils.ET_SENDER_COMPANY_NAME, senderCompanyName);
                values.put(EmailTemplateUtils.ET_SENDER_PRIMARY_PHONE, senderPrimaryPhone);
                values.put(EmailTemplateUtils.ET_SENDER_MOBILE_PHONE, senderMobilePhone);
                values.put(EmailTemplateUtils.ET_SENDER_JOB_TITLE, jobTitle);

            }
            return generateEmailTemplateItem(emailTemplate, values, recipientEmail, from.getObjectID(), senderEmail);
        } else {
            return null;
        }
    }

    private Map<String, Object> generateTemplateForContact(EdsCrmContact contact, EdsUser user) {
        Map<String, Object> values = new TreeMap<>();
        if (contact != null) {
            values.put(EmailTemplateConstants.CRM.OWNER_EMAIL, contact.getOwner() != null && contact.getOwner().getEmail() != null ? contact.getOwner().getEmail() : "");
            values.put(EmailTemplateConstants.CRM.PHONE, contact.getPrimaryPhone() != null ? contact.getPrimaryPhone().trim().replaceAll("[|w+]", "") : "");
            values.put(EmailTemplateConstants.CRM.MOBILE, contact.getRelatedPhone(EdsCrmContactItemParams.MOBILE) != null ? contact.getRelatedPhone(EdsCrmContactItemParams.MOBILE).trim().replaceAll("[|w+]", "") : "");
            values.put(EmailTemplateConstants.CRM.CREATION_DATE, ServerUtils.shortDateFormat(ServerUtils.getCompanyDate(contact.getAuditInfo().getCreationDate(), user.getCompany()), user));
            values.put(EmailTemplateConstants.CRM.MODIFIED_DATE, ServerUtils.shortDateFormat(ServerUtils.getCompanyDate(contact.getAuditInfo().getModificationDate(), user.getCompany()), user));


            values.put(CRM.CRM_CONTACT.CONTACT_ADDRESS, contact.getEdsPrimaryAddressFromAll() != null ? contact.getEdsPrimaryAddressFromAll().getAddress() : "");
            values.put(CRM.CRM_CONTACT.CONTACT_CAMPAIGN_NAME, contact.getCampaign() != null ? contact.getCampaign().getName() != null ? contact.getCampaign().getName() : "" : "");
            values.put(CRM.CRM_CONTACT.CONTACT_COMPANY_NAME, user.getCompany().getName());
            values.put(ET_EMAIL, contact.getPrimaryEmail() != null ? contact.getPrimaryEmail() : "");
            values.put(ET_FIRST_NAME, contact.getFirstName() != null ? StringUtils.capitalize(contact.getFirstName()) : "");
            values.put(ET_LAST_NAME, contact.getLastName() != null ? contact.getLastName() : "");
            values.put(CRM.CRM_CONTACT.CONTACT_JOB_TITLE, contact.getJobTitles() != null ? contact.getJobTitles() : "");
            values.put(CRM.CRM_CONTACT.CONTACT_OWNER, contact.getOwner() != null ? contact.getOwner().getFullName() : "");
            values.put(ET_SIGNATURE, "");
            ArrayList<CompanyCustomFieldItem> ccfis = CustomFieldsUtils.setRPCCustomFieldItems(contact.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Contact));
            for (CompanyCustomFieldItem ccfi : ccfis) {
                if (ccfi.getFieldDateNonConvertedValue() != null) {
                    values.put(ServerUtils.getStringAsAttribute(ccfi.getFieldName().replaceAll("\\s+", "")), ServerUtils.shortDateFormat(user.getUserDate(ccfi.getFieldDateNonConvertedValue().getNonConvertedDate()), user.getCompany()));
                } else {
                    values.put(ServerUtils.getStringAsAttribute(ccfi.getFieldName().replaceAll("\\s+", "")), ccfi.getFieldStringValue() != null ? ccfi.getFieldStringValue() : "");
                }
            }
        }
        return values;
    }

    private Map<String, Object> generateTemplateForCandidate(EdsCrmContact contact, EdsUser user) {
        Map<String, Object> keyValues = new HashMap<>();
        if (contact != null) {
            StringBuilder vacancies = null;
            StringBuilder vacancyManager = null;
            StringBuilder vacancyManagerEmail = null;
            StringBuilder vacancyManagerTelegram = null;
            StringBuilder vacancyManagerPhone = null;

            if (contact.getVacancies() != null) {
                for (EdsVacancy vacancy : contact.getVacancies()) {
                    if (vacancies == null) {
                        vacancies = new StringBuilder();
                        vacancies.append(vacancy.getJobTitle());
                    } else {
                        vacancies.append(", " + vacancy.getJobTitle());
                    }
                    if (vacancy.getManager() != null && vacancy.getManager().getEmployee() != null) {
                        if (vacancyManager == null) {
                            vacancyManager = new StringBuilder();
                            vacancyManagerEmail = new StringBuilder();
                            vacancyManagerPhone = new StringBuilder();
                            vacancyManagerTelegram = new StringBuilder();
                            vacancyManager.append(vacancy.getManager().getEmployee().getFullName());
                            vacancyManagerEmail.append(vacancy.getManager().getEmployee().getEmail());
                            vacancyManagerPhone.append(vacancy.getManager().getEmployee().getPrimaryPhone());
                            vacancyManagerTelegram.append(vacancy.getManager().getEmployee().getPrimaryTelegram());
                        } else {
                            vacancyManager.append(vacancyManager.append(", " + vacancy.getManager().getEmployee().getFullName()));
                            vacancyManagerEmail.append(vacancyManagerEmail.append(", " + vacancy.getManager().getEmployee().getEmail()));
                            vacancyManagerPhone.append(vacancyManagerPhone.append(", " + vacancy.getManager().getEmployee().getPrimaryPhone()));
                            vacancyManagerTelegram.append(vacancyManagerTelegram.append(", " + vacancy.getManager().getEmployee().getPrimaryTelegram()));
                        }
                    }
                }
            }
            final List<FileResource> attachments = this.attachmentUtilsManager.getAttachments(Constants.F_CANDIDATE, contact.getObjectID(), contact.getObjectID(), user);
            final StringBuilder attachmentResult = new StringBuilder();
            if (attachments != null && !attachments.isEmpty()) {
                for (int i = 0; i < attachments.size(); i++) {
                    attachmentResult.append("<a href=\"").append(attachments.get(i).getDownloadUrl()).append("\">")
                            .append(this.commonLocalizer.localize("link")).append("_").append(i + 1).append("</a>");
                    if ((i + 1) % 4 == 0) {
                        attachmentResult.append("\n");
                    } else {
                        attachmentResult.append(", ");
                    }
                }
            }
            keyValues.put(EmailTemplateConstants.OBJECT_KEY, contact.getObjectKey());
            keyValues.put(EmailTemplateConstants.HRMS.CANDIDATE.ATTACHMENTS, attachmentResult.toString());
            keyValues.put(EmailTemplateConstants.HRMS.CANDIDATE.MATCHED_VACANCIES, vacancies != null ? vacancies : "");
            keyValues.put(EmailTemplateConstants.HRMS.CANDIDATE.PROJECT_NUMBER, contact.getCandidateProject() != null && contact.getCandidateProject().getNumber() != null ? contact.getCandidateProject().getNumber() : "");
            keyValues.put(EmailTemplateConstants.HRMS.CANDIDATE.VACANCY_MANAGER, vacancyManager != null ? vacancyManager : "");
            keyValues.put(EmailTemplateConstants.HRMS.CANDIDATE.VACANCY_MANAGER_EMAIL, vacancyManagerEmail != null ? vacancyManagerEmail : "");
            keyValues.put(EmailTemplateConstants.HRMS.CANDIDATE.VACANCY_MANAGER_TELEGRAM, vacancyManagerTelegram != null ? vacancyManagerTelegram : "");
            keyValues.put(EmailTemplateConstants.HRMS.CANDIDATE.VACANCY_MANAGER_PHONE, vacancyManagerPhone != null ? vacancyManagerPhone : "");
            keyValues.put(EmailTemplateConstants.CRM.OWNER_EMAIL, contact.getOwner() != null && contact.getOwner().getEmail() != null ? contact.getOwner().getEmail() : "");
            keyValues.put(EmailTemplateConstants.CRM.PHONE, contact.getPrimaryPhone() != null ? contact.getPrimaryPhone().trim().replaceAll("[|w+]", "") : "");
            keyValues.put(EmailTemplateConstants.CRM.MOBILE, contact.getRelatedPhone(EdsCrmContactItemParams.MOBILE) != null ? contact.getRelatedPhone(EdsCrmContactItemParams.MOBILE).trim().replaceAll("[|w+]", "") : "");
            keyValues.put(EmailTemplateConstants.CRM.CREATION_DATE, ServerUtils.shortDateFormat(ServerUtils.getCompanyDate(contact.getAuditInfo().getCreationDate(), user.getCompany()), user));
            keyValues.put(EmailTemplateConstants.CRM.MODIFIED_DATE, ServerUtils.shortDateFormat(ServerUtils.getCompanyDate(contact.getAuditInfo().getModificationDate(), user.getCompany()), user));

            StringBuilder ownerTelegramChats = new StringBuilder();
            List<EdsCrmContactItemParams> ownerItemParams = null;
            if (contact.getOwner() != null && contact.getOwner().getEmployee() != null && contact.getOwner().getEmployee().getContact() != null) {
                ownerItemParams = contact.getOwner().getEmployee().getContact().getItemParams(CONTACT_TELEGRAMS);
            }
            if (ownerItemParams != null && !ownerItemParams.isEmpty()) {
                for (EdsCrmContactItemParams param : ownerItemParams) {
                    if (ownerTelegramChats.toString().equals("")) {
                        ownerTelegramChats = new StringBuilder(param.getRelation() + ":" + param.getValue());
                    } else {
                        ownerTelegramChats.append(",").append(param.getRelation()).append(":").append(param.getValue());
                    }
                }
            }
            keyValues.put(EmailTemplateConstants.ET_OWNER_TELEGRAM, ownerTelegramChats.toString());
        }
        return keyValues;
    }

    private Map<String, Object> generateTemplateForRFQ(EdsCrmContact contact, EdsRFQ rfq, EdsUser user) {
        Map<String, Object> values = new TreeMap<>();
        if (rfq != null) {

            values.put(EmailTemplateConstants.ET_COMPANY_NAME, user.getCompany().getName());
            values.put(EmailTemplateConstants.ET_CUSTOMER, rfq.getClient() != null ? rfq.getClient().getName() : "N/A");
            values.put(EmailTemplateConstants.ACCOUNTING.PROJECT, rfq.getProject() != null ? rfq.getProject().getName() : "N/A");
            values.put(EmailTemplateConstants.ET_NUMBER, rfq.getNumber() != null ? rfq.getNumber() : "N/A");
            DateNonConvertable dueDate = new DateNonConvertable(rfq.getValidUntil());
            values.put(EmailTemplateConstants.ET_DUE_DATE, ServerUtils.shortDateFormat(ServerUtils.getCompanyDate(dueDate.getNonConvertedDate(), user.getCompany()), user));

            DateNonConvertable rfqDate = new DateNonConvertable(rfq.getDate());
            values.put(EmailTemplateConstants.ET_START_DATE, ServerUtils.longDateFormat(ServerUtils.getCompanyDate(rfqDate.getNonConvertedDate(), user.getCompany()), user));

            ArrayList<CompanyCustomFieldItem> ccfis = CustomFieldsUtils.setRPCCustomFieldItems(rfq.getCustomFields(), commonService.getCompanyCustomFields(ViewName.RequestForQuote));
            for (CompanyCustomFieldItem ccfi : ccfis) {
                if (ccfi.getFieldDateNonConvertedValue() != null) {
                    values.put(ServerUtils.getStringAsAttribute(ccfi.getFieldName().replaceAll("\\s+", "")), ServerUtils.shortDateFormat(user.getUserDate(ccfi.getFieldDateNonConvertedValue().getNonConvertedDate()), user.getCompany()));
                } else {
                    values.put(ServerUtils.getStringAsAttribute(ccfi.getFieldName().replaceAll("\\s+", "")), ccfi.getFieldStringValue() != null ? ccfi.getFieldStringValue() : "");
                }
            }
        }
        return values;
    }

    private Map<String, Object> generateTemplateForLead(EdsCrmContact contact, EdsUser user) {
        Map<String, Object> values = new TreeMap<>();
        if (contact != null) {
            values.put(EmailTemplateConstants.CRM.OWNER_EMAIL, contact.getOwner() != null && contact.getOwner().getEmail() != null ? contact.getOwner().getEmail() : "");
            values.put(EmailTemplateConstants.CRM.ASSIGNEE_EMAIL, contact.getLeadAssignee() != null && contact.getLeadAssignee().getEmail() != null ? contact.getLeadAssignee().getEmail() : "");
            values.put(EmailTemplateConstants.ET_EMAIL, contact.getPrimaryEmail() != null ? contact.getPrimaryEmail() : "");
            values.put(EmailTemplateConstants.CRM.PHONE, contact.getPrimaryPhone() != null ? contact.getPrimaryPhone().trim().replaceAll("[|w+]", "") : "");
            values.put(EmailTemplateConstants.CRM.MOBILE, contact.getRelatedPhone(EdsCrmContactItemParams.MOBILE) != null ? contact.getRelatedPhone(EdsCrmContactItemParams.MOBILE).trim().replaceAll("[|w+]", "") : "");
            values.put(EmailTemplateConstants.CRM.CREATION_DATE, ServerUtils.shortDateFormat(ServerUtils.getCompanyDate(contact.getAuditInfo().getCreationDate(), user.getCompany()), user));
            values.put(EmailTemplateConstants.CRM.MODIFIED_DATE, ServerUtils.shortDateFormat(ServerUtils.getCompanyDate(contact.getAuditInfo().getModificationDate(), user.getCompany()), user));
            values.put(CRM.CRM_LEAD.LEAD_ADDRESS, contact.getEdsPrimaryAddressFromAll() != null ? contact.getEdsPrimaryAddressFromAll().getAddress() : "");
            values.put(ET_CASE_ASSIGNEE, contact.getLeadAssignee() != null ? (contact.getLeadAssignee().getFullName()) : "");
            values.put(CRM.CRM_LEAD.LEAD_BACKUP_ASSIGNEE, contact.getLeadBackupAssignee() != null ? contact.getLeadBackupAssignee().getFullName() : "");

            values.put(CRM.CRM_LEAD.LEAD_CAMPAIGN_NAME, contact.getCampaign() != null ? contact.getCampaign().getName() != null ? contact.getCampaign().getName() : "" : "");
            //values.put(CRM.CRM_LEAD.LEAD_COMPANY_NAME, contact.getCrmAccount() != null ? contact.getCrmAccount().getCompany() != null ? contact.getCrmAccount().getCompany().getName() : "" : "");
            values.put(CRM.CRM_LEAD.LEAD_COMPANY_NAME, user.getCompany().getName());
            values.put(ET_FIRST_NAME, contact.getFirstName() != null ? StringUtils.capitalize(contact.getFirstName()) : "");
            values.put(ET_LAST_NAME, contact.getLastName() != null ? contact.getLastName() : "");
            values.put(CRM.CRM_LEAD.LEAD_JOB_TITLE, contact.getJobTitles() != null ? contact.getJobTitles() : "");
            values.put(CRM.CRM_LEAD.LEAD_OWNER, contact.getOwner() != null ? contact.getOwner().getFullName() : "");
            values.put(CRM.CRM_LEAD.LEAD_RATING, contact.getLeadRating() != null ? contact.getLeadRating().getName() : "");
            values.put(CRM.CRM_LEAD.LEAD_SOURCE, contact.getLeadSource() != null ? contact.getLeadSource().getName() : "");
            values.put(CRM.CRM_LEAD.LEAD_LINK, "");
            values.put(ET_STATUS, contact.getLeadStatus() != null ? contact.getLeadStatus().getName() : "");
            values.put(ET_SIGNATURE, "");
            ArrayList<CompanyCustomFieldItem> ccfis = CustomFieldsUtils.setRPCCustomFieldItems(contact.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Lead));
            for (CompanyCustomFieldItem ccfi : ccfis) {
                if (ccfi.getFieldDateNonConvertedValue() != null) {
                    values.put(ServerUtils.getStringAsAttribute(ccfi.getFieldName().replaceAll("\\s+", "")), ServerUtils.shortDateFormat(user.getUserDate(ccfi.getFieldDateNonConvertedValue().getNonConvertedDate()), user.getCompany()));
                } else {
                    values.put(ServerUtils.getStringAsAttribute(ccfi.getFieldName().replaceAll("\\s+", "")), ccfi.getFieldStringValue() != null ? ccfi.getFieldStringValue() : "");
                }
            }
        }
        return values;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getMessageCenterEmailTemplates(ArrayList<String> templateModules) {
        List<EdsEmailTemplate> emailTemplates = null;
        if (!templateModules.isEmpty()) {

            if (templateModules.contains(ET_RFQ_MODULE) || templateModules.contains(ET_EMPLOYEE_MODULE) || templateModules.contains(ET_OPPORTUNITY_MODULE) || templateModules.contains(ET_EVENT_MODULE)) {
                emailTemplates = emailTemplateManager.getEmailTemplates(templateModules);

            } else {
                emailTemplates = emailTemplateManager.getEmailTemplatesForMessageCenter(templateModules);
            }
            emailTemplates.addAll(emailTemplateManager.getEmailTemplatesByCategory(MESSAGE_CENTER_CATEGORY));
        } else {
            emailTemplates = emailTemplateManager.getEmailTemplatesByCategory(MESSAGE_CENTER_CATEGORY);
        }
        return emailTemplates.stream().map(et -> new SelectItem(et.getObjectID(), et.getName())).toList().toArray(new SelectItem[]{});
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmailTemplates(String templateCategory) {
        List<EdsEmailTemplate> emailTemplates = StringUtils.isEmpty(templateCategory) ? emailTemplateManager.getCompanyEmailTemplates(new ListingFilterParameter()) : emailTemplateManager.getEmailTemplatesByCategory(templateCategory);
        return emailTemplates.stream().map(et -> new SelectItem(et.getObjectID(), et.getName(), null, et.isDefault() != null ? et.isDefault() : false)).toList().toArray(new SelectItem[]{});
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateSendPayslipToManagerEmailTemplate(EdsPayslipTable payslipTable) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(NEW_PAYSLIP_CATEGORY);
        if (emailTemplate == null) {
            emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(NEW_PAYSLIP_CATEGORY);
        }

        if (emailTemplate != null) {

            final EdsCompany company = userManager.getUser().getCompany();
            final EdsEmployee creator = payslipTable.getPreparer();
            final EdsEmployee approver = payslipTable.getApprover();
            final Integer fromId = creator.getObjectID();
            final String from = creator.getEmail();
            final String to = approver.getEmail();
            String url = EdsContextParams.getHost(company.getObjectID()) + "/Payroll.html?link=" + EncryptionHelper.encryptURL("#payslipTable|summary/"
                    + payslipTable.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(approver.getObjectID().toString());
            final Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(ET_MANAGER_NAME, approver != null ? approver.getName() : "");
            values.put(ET_COMPANY_NAME, company != null ? company.getName() : "");
            values.put(ET_CREATOR, creator != null ? creator.getFullName() : "");
            values.put(ET_DATE, formatDateShort(payslipTable.getCreationDate(), company));
            values.put(ET_URL, url);

            return generateEmailTemplateItem(emailTemplate, values, to, fromId, from);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateSendSinglePayrunToManagerEmailTemplate(EdsPayslipTableItem singlePayrun) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(NEW_PAYSLIP_CATEGORY);
        if (emailTemplate == null) {
            emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(NEW_PAYSLIP_CATEGORY);
        }

        if (emailTemplate != null) {

            final EdsCompany company = userManager.getUser().getCompany();
            final EdsEmployee creator = singlePayrun.getPreparer();
            final EdsEmployee approver = singlePayrun.getApprover();
            final Integer fromId = creator.getObjectID();
            final String from = creator.getEmail();
            final String to = approver.getEmail();
            String url = EdsContextParams.getHost(company.getObjectID()) + "/Payroll.html?link=" + EncryptionHelper.encryptURL("#singlePayrun|viewPayslip/"
                    + singlePayrun.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(approver.getObjectID().toString());
            final Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(ET_MANAGER_NAME, approver != null ? approver.getName() : "");
            values.put(ET_COMPANY_NAME, company != null ? company.getName() : "");
            values.put(ET_CREATOR, creator != null ? creator.getFullName() : "");
            values.put(ET_DATE, singlePayrun.getCreationDate() != null ? formatDateShort(singlePayrun.getCreationDate(), company) : "");
            values.put(ET_URL, url);

            return generateEmailTemplateItem(emailTemplate, values, to, fromId, from);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getReplyToById(Integer id) {
        EdsEmailTemplate template = emailTemplateManager.get(id);
        if (template != null) {
            return template.getReplyTo();
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem getEmailTemplateItemForRFP(EntityToEmailTemplate item, String rfpStatus) {
        EdsEmailTemplate edsEmailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(item.getEntityType());
        if (edsEmailTemplate == null) {
            System.out.println("--------------------------------");
            System.out.println("--------------------------------Email template not founr-------------------------------------------");
            System.out.println("--------------------------------");
            return null;
        }

        EdsUser sender = userManager.getUser();
        String senderFullName = sender.getFullName();

        EdsUser receiver = userManager.get(item.getMailReceiverId());
        if (receiver == null) {
            System.out.println("--------------------------------");
            System.out.println("--------------------------------Receiver not founr-------------------------------------------");
            System.out.println("--------------------------------");
            return null;
        }
        String receiverFullName = receiver.getFullName();

        String toEmail = receiver.getEmail();
        String fromEmail = sender.getEmail();
        Integer senderID = sender.getObjectID();

        String url = EdsContextParams.getHost(sender.getCompany().getObjectID()) + "/Accounting.html?link=" + EncryptionHelper.encryptURL("#requestforpurchase|summary/"
                + item.getEntityId()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(receiver.getObjectID().toString());


        Map<String, Object> values = new TreeMap<>();
        values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(edsEmailTemplate.getFromUser())));
        values.put(EmailTemplateUtils.ET_USER_NAME, receiverFullName);//${username}
        values.put(EmailTemplateUtils.ET_CREATOR, senderFullName);//"${creator}";
        values.put(EmailTemplateUtils.ET_STATUS, rfpStatus);
        values.put(EmailTemplateUtils.ET_URL, url);

        return generateEmailTemplateItem(edsEmailTemplate, values, toEmail, senderID, fromEmail);
    }

    @Override
    public EmailTemplateItem generateEmailTemplateForBalance(EntityToEmailTemplate entityToEmailTemplate) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.get(entityToEmailTemplate.getEmailTemplateId());
        if (emailTemplate != null) {
            EdsUser from = companyManager.getUser();
            String senderFirstName = from.getFirstName() != null ? from.getFirstName() : "";
            String senderLastName = from.getLastName() != null ? from.getLastName() : "";
            String senderEmail = from.getEmail();
            String senderCompanyName = from.getCompany().getName() != null ? from.getCompany().getName() : "";
            EdsEmployee employee = employeeManager.get(from.getObjectID());
            String senderPrimaryPhone = "";
            if (from.isClientContact()) {
                senderPrimaryPhone = from.getClientContact() != null ? (from.getClientContact().getCrmContact() != null ?
                        (from.getClientContact().getCrmContact().getPrimaryPhone() != null ? from.getClientContact().getEmployee().getPrimaryPhone() : "") : "") : "";
            } else {
                senderPrimaryPhone = employee != null ? (employee.getPrimaryPhone() != null ? employee.getPrimaryPhone() : "") : "";
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

            EdsCrmContact contact = null;
            EdsCrmAccount crmAccount = crmAccountManager.get(entityToEmailTemplate.getMailReceiverId());
            if (entityToEmailTemplate.getEntityType() != null) {
                contact = crmAccount.getPrimaryContact();
            } else {
                contact = crmAccount.getPrimaryContact();
            }
            String recipientFirstName = "";
            String recipientLastName = "";
            String recipientEmail = "";
            String recipientCompanyName = "";
            if (contact != null) {
                recipientFirstName = contact.getFirstName() == null ? " " : contact.getFirstName();
                recipientLastName = contact.getLastName() == null ? " " : contact.getLastName();
                recipientEmail = contact.getPrimaryEmail();
                if (contact.getCrmAccount() != null) {
                    recipientCompanyName = crmAccount.getName();
                }
            }
            if (crmAccount.getName() != null) {
                recipientCompanyName = crmAccount.getName();
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

            BigDecimal clientBalance = crmAccountManager.getClientBalance(crmAccount.getObjectID());
            BigDecimal supplierBalance = crmAccountManager.getClientBalance(crmAccount.getObjectID());

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_RECIPIENT_FIRST_NAME, StringUtils.capitalize(recipientFirstName));
            values.put(EmailTemplateUtils.ET_RECIPIENT_LAST_NAME, recipientLastName);
            values.put(EmailTemplateUtils.ET_RECIPIENT_EMAIL, recipientEmail);
            values.put(EmailTemplateUtils.ET_RECIPIENT_COMPANY_NAME, recipientCompanyName);
            values.put(EmailTemplateUtils.ET_SENDER_FIRST_NAME, StringUtils.capitalize(senderFirstName));
            values.put(EmailTemplateUtils.ET_SENDER_LAST_NAME, senderLastName);
            values.put(EmailTemplateUtils.ET_SENDER_EMAIL, senderEmail);
            values.put(EmailTemplateUtils.ET_SENDER_COMPANY_NAME, senderCompanyName);
            values.put(EmailTemplateUtils.ET_SENDER_PRIMARY_PHONE, senderPrimaryPhone);
            values.put(EmailTemplateUtils.ET_SENDER_MOBILE_PHONE, senderMobilePhone);
            values.put(EmailTemplateUtils.ET_SENDER_JOB_TITLE, senderJobTitle);
            values.put(EmailTemplateUtils.ET_SUPPLIER_BALANCE, supplierBalance != null ? decimalFormat.format(supplierBalance) : "0.00");
            values.put(EmailTemplateUtils.ET_CUSTOMER_BALANCE, clientBalance != null ? decimalFormat.format(clientBalance) : "0.00");
            return generateEmailTemplateItem(emailTemplate, values, recipientEmail, from.getObjectID(), senderEmail);
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem getOverdueReminderForClientTemplateItem(EdsEmailTemplate companyEmailTemplate, NewInvoice overdueInvoice, EdsUser user) {
        if (companyEmailTemplate != null) {
            String toEmail = overdueInvoice.getClientContactEmail();
            Integer clientID = overdueInvoice.getClientID();
            if (clientID != null) {
                List<EdsCrmContact> contacts = crmContactManager.getContactsByCrmAccount(clientID);
                if (contacts != null && contacts.size() > 0) {
                    for (EdsCrmContact contact : contacts) {
                        if (contact.getPrimaryContact()) {
                            toEmail = contact.getPrimaryEmail();
                        }
                    }
                }
            }
            String host = EdsContextParams.getHost(user.getCompany().getObjectID());

            Integer calculationScale = 2;
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            if (financialSettings != null && financialSettings.getCalculationScale() != null) {
                calculationScale = financialSettings.getCalculationScale();
            }
            DecimalFormat decimalFormat = ServerUtils.getDecimalFormat(calculationScale);

            BigDecimal exchangeRate = overdueInvoice.getExchageRate() != null && overdueInvoice.getExchageRate().compareTo(BigDecimal.ZERO) != 0 ? overdueInvoice.getExchageRate() : new BigDecimal("1.00");
            BigDecimal totalAmount = BigDecimal.ZERO;
            if (overdueInvoice.getTotalInInvoiceCurrency() != null && overdueInvoice.getTotalInInvoiceCurrency().compareTo(BigDecimal.ZERO) != 0) {
                totalAmount = overdueInvoice.getTotalInInvoiceCurrency();
            } else if (overdueInvoice.getTotal() != null) {
                totalAmount = overdueInvoice.getTotal().multiply(exchangeRate);
            }

            BigDecimal paidAmount = overdueInvoice.getPaidAmount() != null ? overdueInvoice.getPaidAmount() : BigDecimal.ZERO;

            String currency = overdueInvoice.getCurrencySymbol() != null ? overdueInvoice.getCurrencySymbol() : overdueInvoice.getCurrencyName();

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(companyEmailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_EXPENSE_HOST, host);
            values.put(EmailTemplateUtils.ET_CUSTOMER, overdueInvoice.getClientName());//client name
            values.put(EmailTemplateUtils.ET_COMPANY_NAME, user.getCompany().getName());//company name
            values.put(EmailTemplateUtils.ACCOUNTING.INVOICE_NUMBER, overdueInvoice.getInvoiceNumber());//invoice number
            values.put(EmailTemplateUtils.ACCOUNTING.INVOICE_DATE, Utils.formatDate(overdueInvoice.getInvoiceDate().getNonConvertedDate(), user.getCompany()));//invoice date
            values.put(EmailTemplateUtils.ET_DUE_DATE, Utils.formatDate(overdueInvoice.getDueDate().getNonConvertedDate(), user.getCompany()));//invoice due date
            values.put(EmailTemplateUtils.ACCOUNTING.TOTAL, totalAmount != null ? decimalFormat.format(totalAmount) + (currency != null ? " " + currency : "") : "0.00");//total amount
            values.put(EmailTemplateUtils.ET_PAID_AMOUNT, paidAmount != null ? decimalFormat.format(paidAmount) + (currency != null ? " " + currency : "") : "0.00");//paid amount
            return generateEmailTemplateItem(companyEmailTemplate, values, toEmail, 0, "");
        } else {
            return null;
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

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateIssueTemplateItem(EdsIssue issue, EdsUser employee, EdsUser user, String category) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(category);
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get((emailTemplate.getFromUser()));
            } else {
                from = user;
            }
            String toEmail = employee.getEmail();
            Integer userCompanyID = user.getCompany().getObjectID();
            String companyID = EncryptionHelper.encryptURL(userCompanyID.toString());
            Date creationDate = issue.getLastUpdateTime() != null ? issue.getLastUpdateTime() : user.getCompany().getCompanyDate();

            Map<String, Object> values = new TreeMap<>();
            String url = EdsContextParams.getHost(employee.getCompany().getObjectID()) + "/ProjectManagement.html?link=" + EncryptionHelper.encryptURL("issue/" + issue.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString()) + "&" + C_ID + "=" + companyID;
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_URL, url);
            values.put(EmailTemplateUtils.ET_ISSUE_NUMBER, issue.getNumber() != null ? issue.getNumber() : "");
            values.put(EmailTemplateUtils.ET_USER_NAME, employee.getName());
            values.put(EmailTemplateUtils.ET_RESOLVER_NAME, issue.getResolver() != null ? issue.getResolver().getName() : "");
            values.put(EmailTemplateUtils.ET_CREATOR, issue.getCreator() != null ? issue.getCreator().getName() : "");
            values.put(EmailTemplateUtils.ET_ISSUE_NAME, issue.getName() != null ? issue.getName() : "");
            values.put(EmailTemplateUtils.ET_DESCRIPTION, issue.getDescription() != null ? issue.getDescription() : "");
            values.put(EmailTemplateUtils.ET_START_DATE, Utils.formatDate(user.getUserDate(issue.getStartDate()), user.getCompany()));
            values.put(EmailTemplateUtils.ET_END_DATE, Utils.formatDate(user.getUserDate(issue.getDueDate()), user.getCompany()));
            values.put(EmailTemplateUtils.ET_DATE, Utils.formatDate(user.getUserDate(creationDate), user.getCompany()));
            values.put(EmailTemplateUtils.ET_PERIOD, (Utils.formatDate(user.getUserDate(issue.getStartDate()), user.getCompany()) + " - " + Utils.formatDate(user.getUserDate(issue.getDueDate()), user.getCompany())));
            values.put(EmailTemplateUtils.ET_STATUS, referenceWfmMessageSource.localizeRef(issue.getIssueStatus()));
            values.put(EmailTemplateUtils.ET_PRIORITY, referenceWfmMessageSource.localizeRef(issue.getPriority()));
            values.put(EmailTemplateUtils.ET_RELATED_TO, "");//
            values.put(EmailTemplateUtils.ET_REPORTED_BY, issue.getReportedBy() != null ? issue.getReportedBy().getName() : "");

            return generateEmailTemplateItem(emailTemplate, values, toEmail, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem getDocumentUploadTemplateItem(EdsUser user, EdsEmployee receiver, String creatorName, String fileName, String description, String relatedToName, String creationDate, String linkURL, String templateCategoryType) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(templateCategoryType);
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get((emailTemplate.getFromUser()));
            } else {
                from = user;
            }
            String toEmail = receiver.getEmail();

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateUtils.ET_USER_NAME, receiver.getName());
            values.put(EmailTemplateUtils.ET_CREATOR, creatorName);
            values.put(EmailTemplateUtils.ET_DATE, creationDate);
            values.put(EmailTemplateUtils.ET_FILE_NAME, fileName);
            values.put(EmailTemplateUtils.ET_DESCRIPTION, description);
            values.put(EmailTemplateUtils.ET_RELATED_TO, relatedToName);
            values.put(EmailTemplateUtils.ET_URL, linkURL);

            return generateEmailTemplateItem(emailTemplate, values, toEmail, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateWebFormThankYouEmailTemplateItem(Integer emailTemplateID, String toEmail, EdsUser user) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.get(emailTemplateID);
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get((emailTemplate.getFromUser()));
            } else {
                from = user;
            }
            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(from.getFirstName()));
            values.put(EmailTemplateUtils.ET_LAST_NAME, from.getLastName());
            values.put(EmailTemplateUtils.ET_EMAIL, from.getEmail());
            values.put(EmailTemplateUtils.ET_COMPANY_NAME, from.getCompany().getName());
            EmailTemplateItem emailTemplateItem = generateEmailTemplateItem(emailTemplate, values, toEmail, from.getObjectID(), from.getEmail());
            emailTemplateItem.setCompanyId(from.getCompany().getObjectID());
            return emailTemplateItem;
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateCalendarInvitationGuests(EdsUser user, EdsEvent event, String guestsEmail, String guestNames, String action) {
        EdsEmailTemplate template = null;
        if ("add".equals(action)) {
            template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(CALENDAR_INVITATION_TO_GUESTS_ADD_CATEGORY);
        } else if ("edit".equals(action)) {
            template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(CALENDAR_INVITATION_TO_GUESTS_EDIT_CATEGORY);
        } else if ("delete".equals(action)) {
            template = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(CALENDAR_INVITATION_TO_GUESTS_DELETE_CATEGORY);
        }
        if (template != null) {
            EdsCompany company = user.getCompany();
            Map<String, Object> values = new TreeMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM");
            //-------------------------------------------------------------------------------
            String host = EdsContextParams.getHost();
            String userFullName = getEventGuestName(guestsEmail, company.getObjectID());
            String dateString = getEventDates(event, user);
            String databaseType = SessionCryptor.encrypt(SecurityContext.getInstance().getDatabase().equals(Constants.DATABASE_FREE) ? "1" : "2");
            String companyID = SessionCryptor.encrypt(company.getObjectID().toString());
            String eventID = SessionCryptor.encrypt(event.getObjectID().toString());
            String subject = "";
            //-------------------------------------------------------------------------------
            values.put("${USER}", !"".equals(userFullName.trim()) ? userFullName : guestsEmail.contains("@") ? guestsEmail.substring(0, guestsEmail.indexOf("@")) : guestsEmail);
            values.put("${EVENT_NAME}", event.getSubject());
            values.put("${WHERE}", getLocation(event.getVenue()));
            values.put("${DESCRIPTION}", getDescription(event.getDescription()));
            values.put("${DATE}", dateString);
            values.put("${CREATOR_NAME}", user.getFullName());
            values.put("${SHARED_WITH}", getEventAttendees(event, user.getObjectID()));
            values.put("${HOST}", host);
            values.put("${GUESTSEMAIL}", guestsEmail);
            values.put("${DATABASE_TYPE}", databaseType);
            values.put("${COMPANY_ID}", companyID);
            values.put("${EVENT_ID}", eventID);

            if ("add".equals(action)) {
                values.put("${UPDATED}", "");
                values.put("${GUESTS}", getEventGuests(event));
            } else if ("edit".equals(action)) {
                values.put("${UPDATED}", "Updated");
                values.put("${GUESTS}", getEventGuests(event));
            } else if ("delete".equals(action)) {
                values.put("${UPDATED}", "Deleted");
                values.put("${GUESTS}", "");
            }

            return generateEmailTemplateItem(template, values, guestsEmail, user.getObjectID(), user.getEmail());
        } else {
            return null;
        }

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

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateCourseBookingConfirm(EdsCourseBooking courseBooking, EdsUser user, String toEmail) {
        EdsEmailTemplate template = emailTemplateManager.getEmailTemplateByCategory(COURSE_BOOKING_CONFIRMATION_CATEGORY);

        if (template != null && courseBooking.getContact() != null) {
            EdsCompany company = user.getCompany();
            EdsCrmContact contact = courseBooking.getContact();

            String cid = EncryptionHelper.encryptURL(String.valueOf(company.getObjectID()));
            String bid = EncryptionHelper.encryptURL(String.valueOf(courseBooking.getObjectID()));
            String host = EdsContextParams.getHost();
            String link = "${host}/bookingConfirmation.html?cid=${cid}&bid=${bid}";

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(template.getFromUser())));
            values.put("${course_booking_link}", link);
            values.put("${cid}", cid);
            values.put("${bid}", bid);
            values.put("${host}", host);
            values.put("${fullname}", contact.getFullName());

            return generateEmailTemplateItem(template, values, toEmail, user.getObjectID(), user.getEmail());
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateStudentCourseBookingConfirm(EdsCourseScheduleStudent courseScheduleStudent, EdsUser user) {
        EdsLocation location = courseScheduleStudent.getCourseScheduleBooking() != null ? courseScheduleStudent.getCourseScheduleBooking().getLocation() : null;
        EdsEmailTemplate template = null;
        if (location != null && location.getCity() != null && "Coastal".equals(location.getCity())) {
            template = emailTemplateManager.getEmailTemplateByCategory(STUDENT_COURSE_BOOKING_CONFIRMATION_CATEGORY);
        } else {
            template = emailTemplateManager.getEmailTemplateByCategory(STUDENT_COURSE_BOOKING_CONFIRMATION_CATEGORY_WITHOUT_MAP);
        }

        String host = EdsContextParams.getHost();

        if (template != null) {
            EdsCompany company = user.getCompany();
            EdsStudent student = courseScheduleStudent.getStudent();
            EdsCourseSchedule courseSchedule = courseScheduleStudent.getCourseScheduleBooking();
            EdsCourse course = courseSchedule.getCourse();

            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(template.getFromUser())));
            values.put("${hos}", host);
            values.put("${number}", courseSchedule.getNumber());
            values.put("${fullname}", student.getFullName());
            values.put("${companyname}", student.getCustomer() != null ? student.getCustomer().getName() : "N/A");
            values.put("${companynumber}", student.getCustomer() != null ? student.getCustomer().getRegistrationNumber() : "N/A");
            values.put("${course}", course.getName());
            values.put("${coursecode}", course.getNumber());
            values.put("${location}", courseSchedule.getLocationAsString());


            Date startDate = new Date(courseSchedule.getStartDate().getTime() + company.getTimeZone().getRawOffset());
            Date endDate = new Date(courseSchedule.getEndDate().getTime() + company.getTimeZone().getRawOffset());
            values.put("${startdate}", ServerUtils.dateFormat(startDate, "dd MMM yyyy HH:mm"));
            values.put("${enddate}", ServerUtils.dateFormat(endDate, "dd MMM yyyy HH:mm"));

            values.put("${language}", courseSchedule.getLanguage().getName());
            values.put("${prerequisites}", course.getPreRequisiteAsString() != null ? course.getPreRequisiteAsString() : "N/A");
            values.put("${otherprerequisites}", course.getOtherPreRequisites() != null ? course.getOtherPreRequisites() : "N/A");

            return generateEmailTemplateItem(template, values, student.getEmail(), user.getObjectID(), user.getEmail());
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<SmsSendItem> generateSmsMessagesForCourseBooking(List<EdsCourseScheduleStudent> courseScheduleStudents, String smsSubject) {
        ArrayList<SmsSendItem> smsSendItems = new ArrayList<>();
        Map<String, Object> values = new TreeMap<>();

        for (EdsCourseScheduleStudent courseScheduleStudent : courseScheduleStudents) {
            EdsStudent student = courseScheduleStudent.getStudent();

            if (student.getContact().getPrimaryPhone() != null && !student.getContact().getPrimaryPhone().isEmpty()) {
                EdsCourseSchedule courseSchedule = courseScheduleStudent.getCourseScheduleBooking();

                String ppMandatoryCourses = "IFR, H2S, FW, SCBA, CHA, AGT, NORMA, NORMS, SCAP, SCIN";// for only these course PP is mondatory @Knowledge Grid
                values.put("${smsSubject}", smsSubject);
                values.put("${cnumber}", courseSchedule.getCourse().getNumber());
                values.put("${scnumber}", courseSchedule.getNumber());
                values.put("${ppmondatory}", ppMandatoryCourses.contains(courseSchedule.getCourse().getNumber()) ? "PPE is Mandatory." : "");

                EdsCompany company = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
                Date startDate = new Date(courseSchedule.getStartDate().getTime() + company.getTimeZone().getRawOffset());
                values.put("${startdate}", ServerUtils.dateFormat(startDate, "dd MMM yyyy HH:mm"));

                String smsMessage = null;
                try {
                    smsMessage = EdsTemplates.processTemplate(values, EdsTemplates.TRAINING_COURSE_SMS_SEND);
                } catch (EdsTemplateException e) {
                    e.printStackTrace();
                }

                String pPhone = student.getContact() != null ? student.getContact().getPrimaryPhone() : null;
                if (pPhone == null || pPhone.length() < 4) {
                    continue;
                }

                String toNumber = Utils.cleanPhoneNumber(pPhone);
                if (smsMessage != null) {
                    SmsSendItem smsSendItem = new SmsSendItem();
                    smsSendItem.setToNumber(toNumber);
                    smsSendItem.setMessageText(smsMessage);
                    smsSendItems.add(smsSendItem);
                }
            }
        }

        return smsSendItems;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateForPersonalGoal(EdsGoal goal, EdsEmployee employee, String entityType, List<EdsEmployee> goalAssigns) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(entityType);
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = goal.getCreator();
            }
            EdsUser user = employeeManager.get(employee.getObjectID());
            String to = employee.getEmail();
            Map<String, Object> values = new TreeMap<>();//generate template values
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_FIRST_NAME, StringUtils.capitalize(employee.getFirstName()));
            values.put(EmailTemplateUtils.ET_LAST_NAME, employee.getLastName());
            values.put(EmailTemplateUtils.ET_TITLE, goal.getTitle());
            values.put(EmailTemplateUtils.ET_DESCRIPTION, goal.getDescription());
            values.put(EmailTemplateUtils.ET_STATUS, goal.getStatus() != null ? referenceWfmMessageSource.localizeRef(goal.getStatus()) : "");
            values.put(EmailTemplateUtils.ET_CREATOR, goal.getCreator().getName());
            values.put(EmailTemplateUtils.ET_START_DATE, goal.getFromDate() != null ? ServerUtils.shortDateFormat(goal.getFromDate(), user) : "");
            values.put(EmailTemplateUtils.ET_DUE_DATE, goal.getToDate() != null ? ServerUtils.shortDateFormat(goal.getToDate(), user) : "");
            values.put(EmailTemplateUtils.ET_DATE, formatDate(user.getCompany().getCompanyDate()));
            values.put(EmailTemplateUtils.ET_VALIDITY_PERIOD, goal.getValidityPeriod() != null ? goal.getValidityPeriod().getName() : "");
            String goalURL = EdsContextParams.getHost(employee.getCompany().getObjectID()) + "/Hrms.html?link=" + EncryptionHelper.encryptURL("goal/" + goal.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString());
            values.put(EmailTemplateUtils.ET_URL, goalURL);
            StringBuilder empList = new StringBuilder();
            for (EdsEmployee emp : goalAssigns) {
                empList.append(empList.toString() == "" ? "" : ", ").append(emp.getFullName());
            }
            values.put(EmailTemplateUtils.ET_ASSIGNEES, empList.toString());
            return generateEmailTemplateItem(emailTemplate, values, to, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateForProductStock(EdsItem item, EdsUser user, String items) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(PRODUCT_STOCK_CATEGORY);
        if (emailTemplate != null) {
            String fromEmail = "";
            Integer fromEmailId = null;
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
                fromEmailId = from.getObjectID();
                fromEmail = from.getEmail();
            } else {
                Properties props = new Properties();
                fromEmail = props.getProperty("mail.login");
            }

            String to = user.getEmail();
            Map<String, Object> values = new TreeMap<>();//generate template values
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_USER_NAME, user.getFullName() != null ? user.getFullName() : user.getUserName() != null ? user.getUserName() : "");
            values.put(EmailTemplateUtils.ET_ITEMS, items);
            String goalURL = EdsContextParams.getHost(user.getCompany().getObjectID()) + "/Accounting.html";
            values.put(EmailTemplateUtils.ET_URL, goalURL);
            return generateEmailTemplate(emailTemplate, values, to, fromEmailId, fromEmail);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateItemForActualTimeReached(EdsUser user, EdsUser receiver, EdsTask task) {
        EdsEmailTemplate emailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(ACTUAL_TIME_REACHED_TO_ESTIMATED);
        if (emailTemplate != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = user;
            }
            String to = receiver.getEmail();
            Integer companyID = user.getCompany().getObjectID();
            Map<String, Object> values = new TreeMap<>();//generate template values
            values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(emailTemplate.getFromUser())));
            values.put(EmailTemplateUtils.ET_TASK_NUMBER, task.getNumber());
            values.put(EmailTemplateUtils.ET_TASK_NAME, task.getName());
            values.put(EmailTemplateUtils.ET_RECEIVER_FULL_NAME, receiver.getFullName());
            values.put(EmailTemplateUtils.ET_DESCRIPTION, task.getDescription());
            values.put(EmailTemplateUtils.ET_PRIORITY, task.getPriority().getName());
            values.put(EmailTemplateUtils.ET_STATUS, task.getStatus().getName());
            Float average;
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                average = task.getTaskAveragePercentCompletedNewLogic();
            } else {
                average = task.getTaskAveragePercentCompleted();
            }
            values.put(EmailTemplateUtils.ET_COMPLETED, average);
            values.put(EmailTemplateUtils.ET_CREATOR, task.getCreator().getName());
            values.put(EmailTemplateUtils.ET_PROJECT_NAME, task.getProject().getName());
            values.put(EmailTemplateUtils.ET_CUSTOMER, task.getProject().getClient().getName());
            values.put(EmailTemplateUtils.ET_START_DATE, task.getStartDate());
            values.put(EmailTemplateUtils.ET_DUE_DATE, task.getDueDate());
            values.put(EmailTemplateUtils.ET_DATE, formatDate(receiver.getUserDate(receiver.getCompany().getCompanyDate())));
            values.put(EmailTemplateUtils.ET_ESTIMATED_TIME, task.getEstimatedTime());
            String taskURL = EdsContextParams.getHost(companyID) + "/ProjectManagement.html?link=" + EncryptionHelper.encryptURL("task|summary/" + task.getObjectID() + "/true") + "&" + U_ID + "=" + EncryptionHelper.encryptURL(receiver.getObjectID().toString()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(companyID.toString());
            values.put(EmailTemplateUtils.ET_URL, taskURL);
            return generateEmailTemplateItem(emailTemplate, values, to, from.getObjectID(), from.getEmail());
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateItemForHrReminder(EdsUser user, EdsUser receiver, EdsEmailTemplate emailTemplate, String fieldValue, String reminderdate, List<EdsUser> employees, Integer companyId) {
        if (emailTemplate != null) {
            try {
                EdsUser from;
                if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                    from = userManager.get(emailTemplate.getFromUser());
                } else {
                    from = user;
                }

                String to = receiver.getEmail();
                Map<String, Object> values = new TreeMap<>();//generate template values
                values.put(EmailTemplateConstants.ET_SIGNATURE, getSignature(userManager.getUserByUserID(from.getObjectID())));
                values.put(EmailTemplateUtils.REMINDER_TYPE, fieldValue);
                values.put(EmailTemplateUtils.REMINDER_FIELD_VALUE, reminderdate);
                if (employees != null) {
                    StringBuilder reminderTable = getReminderEmployeesTable(employees);
                    values.put(EmailTemplateUtils.REMINDER_EMPLOYEES, reminderTable.toString());
                } else {
                    values.put(EmailTemplateUtils.REMINDER_EMPLOYEES, "");

                }
                values.put(EmailTemplateUtils.ET_RECEIVER_FULL_NAME, receiver.getFullName());
                values.put(EmailTemplateUtils.ET_RECIPIENT_EMAIL, receiver.getEmail());
                EdsCompany company = null;
                if (companyId != null) {
                    company = companyManager.get(companyId);
                }
                values.put(EmailTemplateUtils.ET_COMPANY_NAME, company != null ? company.getName() : "");
                return generateEmailTemplateItem(emailTemplate, values, to, from.getObjectID(), from.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateItemForMeetingMinutesNotification(EdsUser creator, EdsMeetingMinutes meetingMinutes, EdsUser attendeesEmployee, String attendee) {
        EdsEmailTemplate emailTemplate;
        if (meetingMinutes.getEmailTemplateID() != null) {
            emailTemplate = emailTemplateManager.get(meetingMinutes.getEmailTemplateID());
        } else {
            emailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(MEETING_MINUTES_NOTIFICATION);
        }
        if (emailTemplate != null) {

            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = creator;
            }
            String to = attendeesEmployee != null ? attendeesEmployee.getEmail() : attendee;
            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.MEETING_MINUTES.MT_TITLE, meetingMinutes.getTitle());
            String calledBy = meetingMinutes.getCalledBy() != null ? meetingMinutes.getCalledBy().getFullName() : "";
            values.put(EmailTemplateConstants.MEETING_MINUTES.MT_CALLEDBY, calledBy);
            values.put(EmailTemplateConstants.MEETING_MINUTES.MT_LOCATION, meetingMinutes.getLocation());
            values.put(EmailTemplateConstants.MEETING_MINUTES.MT_PURPOSE, meetingMinutes.getPurpose());
            values.put(EmailTemplateConstants.ET_START_DATE, meetingMinutes.getStartDate() != null ? formatDate(creator.getUserDate(meetingMinutes.getStartDate()), creator.getCompany()) : "");
            values.put(EmailTemplateConstants.ET_END_DATE, meetingMinutes.getDueDate() != null ? formatDate(creator.getUserDate(meetingMinutes.getDueDate()), creator.getCompany()) : "");
            values.put(EmailTemplateConstants.ET_NUMBER, meetingMinutes.getMeetingNumber());
            if (attendeesEmployee != null) {
                values.put(EmailTemplateConstants.MEETING_MINUTES.MT_RECIPIENT, attendeesEmployee.getFullName());
            } else if (attendee != null && !"".equals(attendee)) {
                if (attendee.contains("<") && attendee.contains(">")) {
                    attendee = attendee.substring(0, attendee.indexOf("<")).trim();
                }
                values.put(EmailTemplateConstants.MEETING_MINUTES.MT_RECIPIENT, attendee);
            }
            String url = EdsContextParams.getHost(creator.getCompany().getObjectID()) + "/Hrms.html#meetingMinutes|summary/" + meetingMinutes.getObjectID().toString();
            values.put(EmailTemplateConstants.ET_LINK, url);
            return generateEmailTemplate(emailTemplate, values, to, from.getObjectID(), from.getEmail());
        }
        return null;
    }

//    @Override
//    public EmailTemplateItem generateEmailTemplateItemForPenaltyPromotion(EdsUser creator, EdsUser receiver, String projectName, EdsEmployeePenaltiesPromotions penaltyPromotion, String categoryCode) {
//        EdsEmailTemplate emailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(categoryCode);
//        if (emailTemplate != null) {
//
//            EdsUser from;
//            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
//                from = userManager.get(emailTemplate.getFromUser());
//            } else {
//                from = creator;
//            }
//            Map<String, Object> values = new TreeMap<>();
//            values.put(PENALTIES_PROMOTIONS.PP_EMPLOYEE_NAME, receiver.getName());
//            values.put(PENALTIES_PROMOTIONS.PP_CREATOR_NAME, creator.getName());
//            values.put(PENALTIES_PROMOTIONS.PP_PROJECT_NAME, projectName);
//            values.put(PENALTIES_PROMOTIONS.PP_POINT_AMOUNT, penaltyPromotion.getNumberOfPoints());
//            values.put(PENALTIES_PROMOTIONS.PP_EFFECTIVE_DATE, formatDate(penaltyPromotion.getExceptionalDate()));
//            values.put(PENALTIES_PROMOTIONS.PP_MYEMAIL, creator.getEmail());
//            values.put(PENALTIES_PROMOTIONS.PP_ASSIGNED_DATE, ServerUtils.shortDateFormat(penaltyPromotion.getUpdateTime(), creator));
//            values.put(PENALTIES_PROMOTIONS.PP_AMOUNT, penaltyPromotion.getAmountOfPenalty());
//            values.put(PENALTIES_PROMOTIONS.PP_ASSIGNED_DATE, formatDate(creator.getCompany().getCompanyDate()));
//            values.put(PENALTIES_PROMOTIONS.PP_PENALTY_PROMOTION_TYPE_NAME, penaltyPromotion.getParent().getType().getName());
//            String fromEmail = "";
//            Integer fromUser = emailTemplate.getFromUser();
//            if (fromUser != null) {
//                EdsUser tempUser = userManager.get(fromUser);
//                if (tempUser != null) {
//                    fromEmail = tempUser.getEmail();
//                }
//            }
//            values.put(PENALTIES_PROMOTIONS.PP_REPLYEMAIL, fromEmail);
//            values.put(PENALTIES_PROMOTIONS.PP_NAME, penaltyPromotion.getParent().getName());
//            String url = EdsContextParams.getHost(creator.getCompany().getObjectID()) + "/Hrms.html?link=" + EncryptionHelper.encryptURL("employeePenaltiesPromotions|summary/" + penaltyPromotion.getObjectID().toString() + "/" + penaltyPromotion.getRPC().isPunishments());
//            values.put(ET_LINK, url);
//            return generateEmailTemplate(emailTemplate, values, receiver.getEmail(), from.getObjectID(), from.getEmail());
//        }
//        return null;
//    }

    @Override
    public EmailTemplateItem generateEmailTemplateItemForContractReminder(EdsUser creator, EdsUser receiver, EdsContract contract, EdsEmailTemplate emailTemplate, EdsRecurrence recurrence) {
        if (emailTemplate != null && contract != null) {
            EdsUser from;
            if (emailTemplate.getFromUser() != null && emailTemplate.getFromUser() != -1) {
                from = userManager.get(emailTemplate.getFromUser());
            } else {
                from = creator;
            }

            Integer reminderTime = Integer.valueOf(recurrence.getBusObjectParams());

            Date contactEndDate = contract.getDueDate() != null ? contract.getDueDate() : contractManager.getContractMaxEndDate(contract.getObjectID());
            Map<String, Object> values = new HashMap<>();
            values.put(CONTRACT_REMINDER_VALUES.CC_CONTRACT_CREATOR, from.getFullName());
            values.put(CONTRACT_REMINDER_VALUES.CC_CONTRACT_NUMBER, contract.getNumber());
            values.put(CONTRACT_REMINDER_VALUES.CC_CONTRACT_LEFT_TIME_TO_DUE_DATE, ServerUtils.getReminderTimeAsString(reminderTime));
            values.put(CONTRACT_REMINDER_VALUES.CC_EMPLOYEE_NAME, receiver.getFullName());
            values.put(CONTRACT_REMINDER_VALUES.CC_CONTRACT_DUE_DATE, ServerUtils.shortDateFormat(contactEndDate, creator));
            String url = EdsContextParams.getHost(creator.getCompany().getObjectID()) + "/ProjectManagement.html?link=" + EncryptionHelper.encryptURL("contract|summary/" + contract.getObjectID().toString());
            values.put(ET_LINK, url);
            values.put(CC_CONTRACT_CUSTOMER, contract.getClient() != null ? contract.getClient().getName() : "");
            return generateEmailTemplate(emailTemplate, values, receiver.getEmail(), from.getObjectID(), from.getEmail());
        }
        return null;
    }

    @Override
    public EmailTemplateItem generateEmailTemplateItemForTaskReminder(EdsTask task, EdsEmployeeTask employeeTask, EdsEmailTemplate emailTemplate) {
        if (task != null && employeeTask != null && emailTemplate != null) {

            String subject = commonLocalizer.localize(EdsSubjects.TASK_DUE_REMINDER) + ": " + "[" + task.getNumber() + "] " + task.getName();
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
                values.put(ET_STATUS, referenceWfmMessageSource.localizeRef(employeeTask.getTask().getStatus()));
            }
            if (employeeTask.getTask().getPriority() != null) {
                values.put(ET_PRIORITY, referenceWfmMessageSource.localizeRef(employeeTask.getTask().getPriority()));
            }
            values.put(TASK_REMINDER_VALUES.OVERTIME, reminderTime);
            values.put(TASK_REMINDER_VALUES.HOST, EdsContextParams.getHost(user.getCompany().getObjectID()));
            values.put(TASK_REMINDER_VALUES.EMPLOYEE_TASK, employeeTask);
            values.put(TASK_REMINDER_VALUES.USER, user.getFullName());

            EdsTask employeeTaskTask = employeeTask.getTask();
            values.put(TASK_REMINDER_VALUES.PROJECT_NAME, employeeTaskTask.getProject().getName());
            values.put(ET_LINK, EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task/" + employeeTaskTask.getObjectID())) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID().toString()) + "&" + C_ID + "=" + companyid);
            values.put(TASK_REMINDER_VALUES.ASSIGNEES, mergeAssignees(employeeTaskTask.getUnDeletedAssignments()));
            if (user != null) {
                values.put(TASK_REMINDER_VALUES.DATE, formatDate(user.getUserDate(user.getCompany().getCompanyDate()), user.getCompany()));
                values.put(ET_START_DATE, employeeTaskTask.getStartDate() != null ? formatDate(user.getUserDate(employeeTaskTask.getStartDate()), user.getCompany()) : "");
                values.put(ET_DUE_DATE, employeeTaskTask.getDueDate() != null ? formatDate(user.getUserDate(employeeTaskTask.getDueDate()), user.getCompany()) : "");
            } else {
                values.put(TASK_REMINDER_VALUES.DATE, defaultLongDateFormat(user.getCompany().getCompanyDate()));
                values.put(ET_START_DATE, employeeTaskTask.getStartDate() != null ? defaultLongDateFormat(employeeTaskTask.getStartDate()) : "");
                values.put(ET_DUE_DATE, employeeTaskTask.getDueDate() != null ? defaultLongDateFormat(employeeTaskTask.getDueDate()) : "");
            }

            values.put(TASK_REMINDER_VALUES.ESTIMATED_TIME, employeeTaskTask.getEstimatedTime() != null ? Utils.timeSpentToString(employeeTaskTask.getEstimatedTime()) : "");
            values.put(TASK_REMINDER_VALUES.COMPLETED, getCompletedPercent(employeeTaskTask.getPercent() != null ? employeeTaskTask.getPercent().intValue() : 0));
            values.put(ET_CUSTOMER, employeeTaskTask.getProject().getClient() != null ? employeeTaskTask.getProject().getClient().getName() : "");
            return generateEmailTemplate(emailTemplate, values, to, task.getCreator().getObjectID(), task.getCreator().getEmail());
        }
        return null;
    }

    private StringBuilder getReminderEmployeesTable(List<EdsUser> employees) {
        StringBuilder reminderTable = new StringBuilder();
        reminderTable.append("<table border=\"0\" width=\"100%\" cellspacing=\"0\" style=\"border: solid 1px #365F91;color:#365F91;margin-top:5px;\">");
        reminderTable.append("<tr>");
        reminderTable.append("<th bgcolor=\"#FFFFFF\" style=\"border: solid 1pt; width:150px; \"><b>Employee Number</b></th>");
        reminderTable.append("<th bgcolor=\"#FFFFFF\" style=\"border: solid 1pt; width:150px; \"><b>Employee Name</b></th>");
        reminderTable.append("<th bgcolor=\"#FFFFFF\" style=\"border: solid 1pt; width:150px; \"><b>Department</b></th>");
        reminderTable.append("<th bgcolor=\"#FFFFFF\" style=\"border: solid 1pt; width:150px; \"><b>Position</b></th>");
        reminderTable.append("</tr>");
        for (EdsUser hrReminderUser : employees) {
            SelectItem hrReminderEmplyee = employeeManager.getEmployeesById(hrReminderUser.getObjectID());
            if (hrReminderEmplyee != null) {
                reminderTable.append("<tr>");
                reminderTable.append("  <td bgcolor=\"#FFFFFF\" style=\"border: solid 1pt; width:150px; \">");
                reminderTable.append(hrReminderEmplyee.getCategory() != null ? hrReminderEmplyee.getCategory() : "");
                reminderTable.append("  </td>");
                reminderTable.append("  <td bgcolor=\"#FFFFFF\" style=\"border: solid 1pt; width:150px; \">");
                reminderTable.append(hrReminderUser.getFullName());
                reminderTable.append("  </td>");
                reminderTable.append("  <td bgcolor=\"#FFFFFF\" style=\"border: solid 1pt; width:150px; \">");
                reminderTable.append(hrReminderEmplyee.getName() != null ? hrReminderEmplyee.getName() : "");
                reminderTable.append("  </td>");
                reminderTable.append("  <td bgcolor=\"#FFFFFF\" style=\"border: solid 1pt; width:150px; \">");
                reminderTable.append(hrReminderEmplyee.getDescription() != null ? hrReminderEmplyee.getDescription() : "");
                reminderTable.append("  </td>");
                reminderTable.append("</tr>");
            }
        }
        reminderTable.append("</table>");
        return reminderTable;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem generateEmailTemplateItemForBillOfMaterials(EdsUser creator, EdsProject project, String status, EdsEmployee employee) {
        EdsEmailTemplate emailTemplate = null;
        if (APPROVED.equals(status)) {
            emailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(BILL_OF_MATERIALS_APPROVED);
        } else if (REJECTED.equals(status)) {
            emailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(BILL_OF_MATERIALS_REJECTED);
        } else if (SUBMITTED_TO_MANAGER.equals(status)) {
            emailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(BILL_OF_MATERIALS_SUBMITTED);
        }
        if (emailTemplate != null) {
            String to = employee.getEmail();
            Map<String, Object> values = new TreeMap<>();
            values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.CURRENT_USER, creator.getName());
            values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.RECEPIENT, employee.getName());
            values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.DATE, creator.getUserDate(new Date()));
            values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.PROJECT_NAME, project.getName());
            values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.REJECTION_REASON, project.getRejectionReason());
            values.put(EmailTemplateConstants.BILL_OF_MATERIALS_VALUES.START_DATE, creator.getUserDate(project.getStartDate()));

            String url = EdsContextParams.getHost(creator.getCompany().getObjectID()) + "/ProjectManagement.html?link=" + EncryptionHelper.encryptURL("project|billofmaterial/" + project.getObjectID());

            values.put(EmailTemplateUtils.ET_LINK, url);
            return generateEmailTemplateItem(emailTemplate, values, to, creator.getObjectID(), creator.getEmail());
        }
        return null;
    }
}
