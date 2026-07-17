package com.edatasite.workforce.gwt.profile.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.shared.mail.EdsMailer;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCompanyEmail;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsContainer;
import com.edatasite.workforce.core.domain.EdsContainerItem;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCountryZone;
import com.edatasite.workforce.core.domain.EdsCustomFieldListener;
import com.edatasite.workforce.core.domain.EdsCustomFieldValidation;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsDropdownValueEmployee;
import com.edatasite.workforce.core.domain.EdsDropdownValueRole;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeAsterisk;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsEntityType;
import com.edatasite.workforce.core.domain.EdsGoogleCalendar;
import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsLocale;
import com.edatasite.workforce.core.domain.EdsMessage;
import com.edatasite.workforce.core.domain.EdsMinimumWage;
import com.edatasite.workforce.core.domain.EdsModuleLocalize;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsOnboardingStep;
import com.edatasite.workforce.core.domain.EdsPermission;
import com.edatasite.workforce.core.domain.EdsProfileIm;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsRolePermissionHistory;
import com.edatasite.workforce.core.domain.EdsServerContacts;
import com.edatasite.workforce.core.domain.EdsSinxDocuments;
import com.edatasite.workforce.core.domain.EdsSinxDocumentsSettings;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.EdsSuperMessage;
import com.edatasite.workforce.core.domain.EdsTelegramChat;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.core.domain.EdsWageRate;
import com.edatasite.workforce.core.domain.EdsWageRateItem;
import com.edatasite.workforce.core.domain.EdsWebHookHistory;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsIntegrationSettings;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmploymentType;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCompanySettingsCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCFItemTableSetting;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormAttributes;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormSection;
import com.edatasite.workforce.core.domain.customform.EdsModel;
import com.edatasite.workforce.core.domain.customform.EdsModelCustom;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.customform.EdsModelFieldCustom;
import com.edatasite.workforce.core.domain.customform.EdsModelFieldDefault;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.domain.pdf.EdsPdfFonts;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.settings.EdsCompanyEmailNotificationSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsDocumentIntegration;
import com.edatasite.workforce.core.domain.settings.EdsEmailFilter;
import com.edatasite.workforce.core.domain.settings.EdsEmailNotificationSettings;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.core.domain.settings.EdsPayrollZone;
import com.edatasite.workforce.core.domain.settings.EdsQuickAddSettings;
import com.edatasite.workforce.core.domain.settings.EdsRecruitmentIntegration;
import com.edatasite.workforce.core.domain.settings.EdsSMSTemplates;
import com.edatasite.workforce.core.domain.settings.EdsSignature;
import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHook;
import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHookBody;
import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHookParameter;
import com.edatasite.workforce.core.domain.webhook.EdsPublicWebhookAttribute;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.core.domain.workflow.EdsWebHookBody;
import com.edatasite.workforce.core.domain.workflow.EdsWebHookParameter;
import com.edatasite.workforce.core.domain.workflow.EdsWebHookResponse;
import com.edatasite.workforce.core.domain.workflow.EdsWebhookAttribute;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowAction;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowActionItem;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowAlert;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowCondition;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowEmployee;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowInvoice;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowInvoiceField;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowPush;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowSMSAlert;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowTelegramAlert;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowUpdateField;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowWebHook;
import com.edatasite.workforce.core.domain.workflow.WebHookBodyType;
import com.edatasite.workforce.core.domain.workflow.WebHookMethod;
import com.edatasite.workforce.core.domain.workflow.WebHookParameterType;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeStepSolrComponent;
import com.edatasite.workforce.core.tools.EdsSchemaUpdater;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.core.tools.WfmGetConnection;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyData;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldSettingItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.IpAddressRange;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.LRSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.PredefinedValueItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionHistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SignatureItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettings;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WebhookRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowCondition;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowWebHookItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowWebHookListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateUtils;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.MagentoService;
import com.edatasite.workforce.gwt.core.server.app.RolePermissionServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentTypeManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CompanySettingsCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.PayrollZoneManager;
import com.edatasite.workforce.gwt.core.server.db.settings.QuickAddSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CompanySettingsEventListener;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CustomFieldEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.CustomFieldCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.MultiCurrencyItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.eml.CreateEMLFile;
import com.edatasite.workforce.gwt.core.server.servlets.eml.CreateZipFile;
import com.edatasite.workforce.gwt.core.server.servlets.eml.EML;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.server.app.ItemTableSettingsServiceLocal;
import com.edatasite.workforce.gwt.messagecenter.server.MessageCenterServiceLocal;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.profile.client.rpc.AlternativeCalendarEnum;
import com.edatasite.workforce.gwt.profile.client.rpc.CompanyInfoForTelegram;
import com.edatasite.workforce.gwt.profile.client.rpc.CompanyOpportunitySettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ConsolidationCompanyItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ConsolidationCompanyList;
import com.edatasite.workforce.gwt.profile.client.rpc.ConsolidationCompanySaveItem;
import com.edatasite.workforce.gwt.profile.client.rpc.CredentialsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.EmailFilter;
import com.edatasite.workforce.gwt.profile.client.rpc.ImportLogItem;
import com.edatasite.workforce.gwt.profile.client.rpc.IntegrationItem;
import com.edatasite.workforce.gwt.profile.client.rpc.IntegrationSettingsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.MagentoSettingsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.PermissionColumnsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileImItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.RecruitmentIntegrationItem;
import com.edatasite.workforce.gwt.profile.client.rpc.SMSTemplateItem;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAction;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowActionItem;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAlert;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowEmployee;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowInvoice;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowInvoiceField;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowPush;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowSMSAlert;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowTelegramAlert;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowUpdateField;
import com.edatasite.workforce.gwt.profile.client.ui.EmailNotificationConstants;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;
import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.signup.server.app.SignUpServiceLocal;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetItem;
import com.edatasite.workforce.gwt.timesheet.server.app.TimesheetServiceLocal;
import com.edatasite.workforce.rest.base.enums.NameOrder;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gdata.util.ServiceException;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule.*;

@Transactional
@Service("profileService")
public class ProfileServiceImpl implements ProfileService, ProfileServiceLocal, Constants, EmailNotificationConstants, EmailTemplateConstants {

    public static final DecimalFormat decimalFormat = new DecimalFormat("0000");
    private static final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);
    private static final String EMAIL_TEMPLATE_CATEGORY = "_EMAIL_TEMPLATE_CATEGORY";
    private static final Map<String, ComparatorFactory<EdsEmailTemplate>> comparatorFactoriesEmailTemplateList = new HashMap<>();

    static {
        comparatorFactoriesEmailTemplateList.put(EmailTemplateItem.TEMPLATE_NAME, sortOrder -> new AbstractComparator<EdsEmailTemplate>() {
            public int compare(EdsEmailTemplate o1, EdsEmailTemplate o2) {
                return internalCompare(o2.getName(), o1.getName(), sortOrder);
            }
        });
        comparatorFactoriesEmailTemplateList.put(EmailTemplateItem.TEMPLATE_SUBJECT, sortOrder -> new AbstractComparator<EdsEmailTemplate>() {
            public int compare(EdsEmailTemplate o1, EdsEmailTemplate o2) {
                return internalCompare(o1.getSubject(), o2.getSubject(), sortOrder);
            }
        });
        comparatorFactoriesEmailTemplateList.put(EmailTemplateItem.TEMPLATE_CATEGORY, sortOrder -> new AbstractComparator<EdsEmailTemplate>() {
            public int compare(EdsEmailTemplate o1, EdsEmailTemplate o2) {
                return internalCompare(o1.getTemplateCategory().getName(), o2.getTemplateCategory().getName(), sortOrder);
            }
        });
        comparatorFactoriesEmailTemplateList.put(EmailTemplateItem.TEMPLATE_IS_DEFAULT, sortOrder -> new AbstractComparator<EdsEmailTemplate>() {
            public int compare(EdsEmailTemplate o1, EdsEmailTemplate o2) {
                return internalCompare(o1.isDefault(), o2.isDefault(), sortOrder);
            }
        });
        comparatorFactoriesEmailTemplateList.put(EmailTemplateItem.TEMPLATE_ONLY_MINE, sortOrder -> new AbstractComparator<EdsEmailTemplate>() {
            public int compare(EdsEmailTemplate o1, EdsEmailTemplate o2) {
                return internalCompare(o1.getUserID(), o2.getUserID(), sortOrder);
            }
        });
    }

    @Autowired
    protected InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    protected AccountingManager accountingManager;
    @Autowired
    protected PropertManager propertManager;
    @Autowired
    protected ContainerManager containerManager;
    @Autowired
    protected RolePermissionHistoryManager rolePermissionHistoryManager;
    @Autowired
    protected ContainerItemManager containerItemManager;
    @Autowired
    protected ListPanelSettingsManager listPanelSettingsManager;
    @Autowired
    protected CustomFormManager customFormManager;
    @Autowired
    protected EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    protected EmployeeAsteriskManager employeeAsteriskManager;
    @Autowired
    protected TelegramChatManager telegramChatManager;
    @Autowired
    protected EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    protected ModuleLocalizeManager moduleLocalizeManager;
    @Autowired
    protected CustomFormItemManager customFormItemManager;
    @Autowired
    EntityTypeManager entityTypeManager;
    RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private WorkflowRuleManager workflowRuleManager;
    @Autowired
    private WorkflowAlertManager workflowAlertManager;
    @Autowired
    private WorkflowSMSAlertManager workflowSMSAlertManager;
    @Autowired
    private WorkflowTelegramAlertManager workflowTelegramAlertManager;
    @Autowired
    private WorkflowUpdateFieldManager workflowUpdateFieldManager;
    @Autowired
    private WorkflowConditionManager workflowConditionManager;
    @Autowired
    private WorkflowPushManager workflowPushManager;
    @Autowired
    private WorkflowEmployeeManager workflowEmployeeManager;
    @Autowired
    private WorkflowInvoiceManager workflowInvoiceManager;
    @Autowired
    private WorkflowActionManager workflowActionManager;
    @Autowired
    private WorkflowActionItemManager workflowActionItemManager;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private ContactService contactService;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ProfileImManager profileImManager;
    @Autowired
    private TimeZoneManager zoneManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private LocaleManager localeManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    private SMSTemplateManager smsTemplateManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private WorkflowMessageManager workflowMessageManager;
    @Autowired
    @Qualifier("companyCFSettingsManager")
    private CompanyCustomFieldsManager companyCFManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private EmailSettingsManager emailSettingsManager;
    @Autowired
    private GenericSettingsManager generalSettingsManager;
    @Autowired
    private EmailFilterManager emailFilterManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private GoogleCalendarManager googleCalendarManager;
    @Autowired
    private GoogleContactsManager googleContactsManager;
    @Autowired
    private GoogleAnalyticsManager googleAnalyticsManager;
    @Autowired
    private GoogleDocumentsManager googleDocumentsManager;
    @Autowired
    private SinxDocumentsSettingsManager sinxDocumentsSettingsManager;
    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private CompanySettingsManager companySettingsManager;
    @Autowired
    private UserBankAccountManager userBankAccountManager;
    @Autowired
    private EmailNotificationSettingsManager emailNotificationSettingsManager;
    @Autowired
    private CompanyEmailManager companyEmailManager;
    @Autowired
    private CompanyEmailNotificationSettingsManager companyEmailNotificationSettingsManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private CompanyPdfFontsManager companyPdfFontsManager;
    @Autowired
    private SmsManager smsManager;
    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    @Qualifier("rolePermissionService")
    private RolePermissionServiceLocal rolePermissionServiceLocal;
    @Autowired
    private CrmContactItemParamsManager contactItemParamsManager;
    @Autowired
    private PayrollService payrollService;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("accountingLocalizer")
    private WfmMessageSource accountingLocalizer;
    @Autowired
    @Qualifier("hrmsLocalizer")
    private WfmMessageSource hrmsLocalizer;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    @Qualifier("signUpService")
    private SignUpServiceLocal signupServiceLocal;
    @Autowired
    @Qualifier("backendService")
    private BackendServiceLocal backendServiceLocal;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    @Qualifier("accountingService")
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    @Qualifier("timesheetService")
    private TimesheetServiceLocal timesheetServiceLocal;
    @Autowired
    private SignatureManager signatureManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private UserSessionManager userSessionManager;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private OnboardingStepManager onboardingStepManager;
    @Autowired
    private StepEmployeeManager stepEmployeeManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private RecurrenceJobManager recurrenceJobManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private ModelManager modelManager;
    @Autowired
    private CustomFormAttributeManager customFormAttributeManager;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private PayrollCategoryManager payrollCategoryManager;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private CompanySettingsCFManager companySettingsCFManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private Office365AuthService office365AuthService;
    @Autowired
    private MagentoService magentoService;
    @Autowired
    private IntegrationSettingsManager integrationSettingsManager;
    @Autowired
    private MessageCenterServiceLocal messageCenterServiceLocal;
    @Autowired
    private ItemTableSettingsServiceLocal itemTableSettingsServiceLocal;
    @Autowired
    private ItemTableSettingService itemTableSettingService;
    @Autowired
    private CertificateOfEmploymentTypeManager certificateOfEmploymentTypeManager;
    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private ImportFileManager importFileManager;
    @Autowired
    private CustomFormSectionManager customFormSectionManager;
    @Autowired
    private WorkflowWebHookManager workflowWebHookManager;
    @Autowired
    private CustomFormLocalizationManager customFormLocalizationManager;
    @Autowired
    private WebHookParameterManager webHookParameterManager;
    @Autowired
    private WebHookBodyManager webHookBodyManager;
    @Autowired
    private WebHookResponseManager webHookResponseManager;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private RecruitmentIntegrationManager recruitmentIntegrationManager;
    @Autowired
    private QuickAddSettingsManager quickAddSettingsManager;
    @Autowired
    private WebhookAttributeManager webhookAttributeManager;
    @Autowired
    private CFItemTableSettingmanager cfItemTableSettingmanager;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;
    @Autowired
    private EmployeeStepSolrComponent employeeStepSolrComponent;
    @Autowired
    private DropdownValueRoleManager dropdownValueRoleManager;
    @Autowired
    private DropdownValueEmployeeManager dropdownValueEmployeeManager;
    @Autowired
    private PayrollZoneManager payrollZoneManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private MinimumWageManager minimumWageManager;
    @Autowired
    private WageRateManager wageRateManager;
    @Autowired
    private WageRateItemManager wageRateItemManager;
    @Autowired
    private PdfTemplateTableSettingsManager pdfTemplateTableSettingsManager;
    @Autowired
    private WebHookHistoryManager webHookHistoryManager;
    @Autowired
    private PublicWebhookManager publicWebhookManager;
    @Autowired
    private PublicWebhookBodyManager publicWebhookBodyManager;
    @Autowired
    private PublicWebhookParameterManager publicWebhookParameterManager;
    @Autowired
    private PublicWebhookAttributeManager publicWebhookAttributeManager;
    @Autowired
    private DocumentIntegrationManager documentIntegrationManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PermissionColumnsItem getRolesPermissions(boolean isSuperUser) {
        return rolePermissionServiceLocal.getRolesPermissions(isSuperUser);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailFilter getEmailFilter(String filterType, Integer objectID) {
        EmailFilter filter = new EmailFilter();
        if (objectID != null) {
            EdsEmailFilter edsFilter = emailFilterManager.get(objectID);
            filter = edsFilter.getRPC(filter);
        }
        fillNamesByIDs(filter);
        filter.setEmailTemplates(getEmailTemplates());
        filter.setProjects(getProjectsListAsSelectItem());
        filter.setProjectTemplateEnabled(generalSettingsManager.isSettingsEnabled(GenericSettingsEnum.COPY_PROJECT_TEMPLATE_ENABLED));
        filter.setEmailFilters(EdsEmailFilter.asSelectItems(emailFilterManager.getParentsOnly()).toArray(new SelectItem[]{}));
        if (filter.getSubFilters() != null && !filter.getSubFilters().isEmpty()) {
            for (EmailFilter subFilter : filter.getSubFilters()) {
                subFilter.setRelationItems(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_EMAIL_FILTER, subFilter.getObjectID())));
            }
        }
        return filter;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProjectsListAsSelectItem() {
        List<EdsProject> projects = projectManager.getCompanyProjects();
        SelectItem[] projectsList = new SelectItem[0];
        if (projects != null && projects.size() > 0) {
            projectsList = new SelectItem[projects.size()];
            int i = 0;
            for (EdsProject project : projects) {
                projectsList[i] = new SelectItem(project.getObjectID(), project.getName());
                i++;
            }
        }
        return projectsList;
    }

    private void fillNamesByIDs(EmailFilter filter) {
        if (filter.getDepartmentID() != null) {
            EdsDepartment department = departmentManager.get(filter.getDepartmentID());
            if (department != null) {
                filter.setDepartmentName(department.getName());
            }
        }
        if (filter.getAssigneeID() != null) {
            EdsUser employee = userManager.get(filter.getAssigneeID());
            if (employee != null) {
                filter.setAssigneeName(employee.getName());
            }
        }
        if (filter.getResolverID() != null) {
            EdsUser resolver = userManager.get(filter.getResolverID());
            if (resolver != null) {
                filter.setResolverName(resolver.getName());
            }
        }
        if (filter.getSubFilters().size() > 0) {
            for (EmailFilter subFilter : filter.getSubFilters()) {
                fillNamesByIDs(subFilter);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmailTemplates() {
        List<EdsEmailTemplate> emailTemplates = emailTemplateManager.getCompanyAutoResponseTemplates();
        EdsEmailTemplate defaultAutoRespTemlate = emailTemplateManager.getDefaultEmailTemplateByCategory(CASE_AUTO_RESPONSE_CATEGORY);
        if (defaultAutoRespTemlate != null) {
            emailTemplates.add(defaultAutoRespTemlate);
        }
        SelectItem[] emailTemplatesSI = new SelectItem[0];
        if (emailTemplates != null && emailTemplates.size() > 0) {
            emailTemplatesSI = new SelectItem[emailTemplates.size()];
            int i = 0;
            for (EdsEmailTemplate emailTemplate : emailTemplates) {
                emailTemplatesSI[i] = new SelectItem(emailTemplate.getObjectID(), emailTemplate.getName());
                i++;
            }
        }
        return emailTemplatesSI;
    }

    @Transactional
    public Integer saveEmailFilter(EmailFilter item) {
        EdsEmailFilter edsEmailFilter = item.getObjectID() != null ? emailFilterManager.get(item.getObjectID()) : new EdsEmailFilter();
        edsEmailFilter.setName(item.getName());
        edsEmailFilter.setParent(item.isParent());
        edsEmailFilter.setRule(item.isRule());
        if (edsEmailFilter.isParent() || edsEmailFilter.isRule()) {
            edsEmailFilter.setRules(item.getRulesAsString());
            if (edsEmailFilter.isRule() && (edsEmailFilter.getRules() == null || "".equals(edsEmailFilter.getRules()))) {
                return null;
            }
        }
        if (!edsEmailFilter.isParent()) {
            edsEmailFilter.setParent(item.getParent() != null && item.getParent().getId() != null ? emailFilterManager.get(item.getParent().getId()) : null);
            edsEmailFilter.setType(item.getType());
            edsEmailFilter.setParametrs(item.getDefaultActions());
            if (!edsEmailFilter.isRule()) {
                if (edsEmailFilter.getSubFilters() != null && edsEmailFilter.getSubFilters().size() > 0) {
                    for (EdsEmailFilter filter : edsEmailFilter.getSubFilters()) {
                        filter.setDeleted(true);
                    }
                }
                emailFilterManager.createOrUpdate(edsEmailFilter);
                if (item.getSubFilters().size() > 0) {
                    for (EmailFilter subFilter : item.getSubFilters()) {
                        subFilter.setRule(true);
                        subFilter.setParent(edsEmailFilter.getAsSelectItem());
                        Integer ruleID = saveEmailFilter(subFilter);
                        if (subFilter.getObjectID() == null) {
                            allInOneService.saveRelations(RelationItem.TYPE_EMAIL_FILTER, ruleID, edsEmailFilter.getName(), subFilter.getRelationItems(), true);
                        }
                    }
                }
            }
        }
        emailFilterManager.createOrUpdate(edsEmailFilter);
        return edsEmailFilter.getObjectID();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<EmailFilter> getEmailFilters(ListingFilterParameter filterParametr) {
        ArrayList<EmailFilter> list = emailFilterManager.list(filterParametr).stream().map(l -> l.getRPC(null)).collect(Collectors.toCollection(ArrayList::new));
        return new ListResult<>(list, list.size());
    }

    public void updateProfile(ProfileItem editProfile) {
        try {
            EdsEmployeeProfile employeeProfile = profileManager.getProfile();
            EdsEmployee employee = (EdsEmployee) referenceManager.getUser();
            editProfile.getCrmAccount().setName(employee.getCompany().getName());
            editProfile.setEntityContactID(editProfile.getObjectId());
            Integer contactID = saveContact(editProfile.getContactID(), editProfile);
            EdsCrmContact contact = null;
            if (contactID != null) {
                contact = crmContactManager.get(contactID);
            }
            employeeProfile.setContact(contact);
            EdsEmployee profileEmployee = employeeManager.getEmployeeByProfileID(employeeProfile.getObjectID());
            profileEmployee.setFirstName(editProfile.getFirstName());
            profileEmployee.setMiddleName(editProfile.getMiddleName());
            profileEmployee.setLastName(editProfile.getLastName());
            ContactListItem contactItem = new ContactListItem();
            contactItem = contact.getRPC(null, contactItem);
            if (employeeProfile.getContact() != null) {
                if (employeeProfile.getContact().getPrimaryEmail() != null && !"".equals(employeeProfile.getContact().getPrimaryEmail())) {
                    employee.setEmail(employeeProfile.getContact().getPrimaryEmail());
                }
            }

            if (editProfile.getCareerLevelId() != null) {
                employeeProfile.setCareerLevel(referenceManager.get(editProfile.getCareerLevelId()));
            }
            if (editProfile.getExperienceId() != null) {
                employeeProfile.setExperience(referenceManager.get(editProfile.getExperienceId()));
            }
            if (editProfile.getEducationLevelId() != null) {
                employeeProfile.setEducationLevel(referenceManager.get(editProfile.getEducationLevelId()));
            }
            if (editProfile.getManagementExperienceId() != null) {
                employeeProfile.setManagementExperience(referenceManager.get(editProfile.getManagementExperienceId()));
            }
            if (editProfile.getProjectLeadershipExperienceId() != null) {
                employeeProfile.setProjectLeadershipExperience(referenceManager.get(editProfile.getProjectLeadershipExperienceId()));
            }
            if (editProfile.getLocaleId() != null) {
                profileEmployee.setLocale(localeManager.get(editProfile.getLocaleId()));
            }
            //user bank account details;
            EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
            if (editProfile.getBankAccountData() != null) {
                if (userBankAccount == null) {
                    userBankAccount = new EdsUserBankAccount();
                    userBankAccount.setUser(employee);
                }
                userBankAccount.setBankName(editProfile.getBankAccountData().getBankName());
                userBankAccount.setBankAddress(editProfile.getBankAccountData().getBankAddress());
                userBankAccount.setAccountNumber(editProfile.getBankAccountData().getAccountNumber());
                userBankAccount.setAccountName(editProfile.getBankAccountData().getAccountName());
                userBankAccount.setSwiftCode(editProfile.getBankAccountData().getSwiftCode());
                userBankAccount.setSortCode(editProfile.getBankAccountData().getSortCode());
                userBankAccount.setIbanCode(editProfile.getBankAccountData().getIbanCode());

                if (userBankAccount.getObjectID() != null) {
                    userBankAccountManager.update(userBankAccount);
                } else {
                    userBankAccountManager.create(userBankAccount);
                }
            } else {
                if (userBankAccount != null) {
                    userBankAccountManager.delete(userBankAccount);
                }
            }
            //user's password is not getting changed
            userManager.saveUserAuthenticationData(employee, SecurityContext.getCompanyID(), false, false);
        } catch (Throwable throwable) {
            log.error("Unexpected exception:", throwable);
            throw new RuntimeException(throwable);
        }
    }

    private Integer saveContact(Integer contactID, ContactListItem contact) {
        contact.setObjectId(contactID);
        return contactService.saveContact(contact, null, true);
    }

    private ProfileImItem getProfileImItem(EdsProfileIm profileIm) {
        if (profileIm == null) {
            return null;
        }

        ProfileImItem profileImItem = new ProfileImItem();
        profileImItem.setAccount(profileIm.getAccount());
        profileImItem.setIm(profileIm.getIm().getName());
        profileImItem.setObjectId(profileIm.getObjectID());
        return profileImItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountries() {
        return commonService.getCountries();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRegions(Integer countryId) {
        List<EdsRegion> regions = regionManager.listByCountry(countryId);

        SelectItem[] result = new SelectItem[regions.size()];

        int i = 0;
        for (EdsRegion region : regions) {
            result[i] = new SelectItem(region.getObjectID(), region.getName());
            i++;
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getAccount(Integer imId) {
        List<EdsProfileIm> ims = profileImManager.accountListByImId(imId);
        SelectItem[] result = new SelectItem[ims.size()];
        int i = 0;
        for (EdsProfileIm im : ims) {
            result[i] = new SelectItem(im.getIm().getObjectID(), im.getAccount());
            i++;
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProfileItem getProfile() {
        ProfileItem profileItem = new ProfileItem();
        EdsEmployeeProfile profile = profileManager.getProfile();
        if (profile != null) {
            EdsEmployee profileEmployee = employeeManager.getEmployeeByProfileID(profile.getObjectID());
            if (profileEmployee != null) {
                EdsEmployee employee = (EdsEmployee) referenceManager.getUser();
                profileItem = profile.getRPC(profileItem);
                profileItem.setCountries(commonService.getCountries());
                profileItem.setStates(commonService.getRegions());
                profileItem.setFirstName(profileEmployee.getFirstName() == null ? "N/A" : profileEmployee.getFirstName());
                profileItem.setMiddleName(profileEmployee.getMiddleName() == null ? "N/A" : profileEmployee.getMiddleName());
                profileItem.setLastName(profileEmployee.getLastName() == null ? "N/A" : profileEmployee.getLastName());
                profileItem.setLocaleId(profileEmployee == null || profileEmployee.getLocale() == null ? null : profileEmployee.getLocale().getObjectID());
                profileItem.setCareerLevel(profile.getCareerLevel() == null ? "N/A" : referenceWfmMessageSource.localizeRef(profile.getCareerLevel()));
                profileItem.setCareerLevelId(profile.getCareerLevel() == null ? null : profile.getCareerLevel().getObjectID());
                profileItem.setExperience(profile.getExperience() == null ? null : referenceWfmMessageSource.localizeRef(profile.getExperience()));
                profileItem.setExperienceId(profile.getExperience() == null ? null : profile.getExperience().getObjectID());
                profileItem.setEducationLevel(profile.getEducationLevel() == null ? "N/A" : referenceWfmMessageSource.localizeRef(profile.getEducationLevel()));
                profileItem.setEducationLevelId(profile.getEducationLevel() == null ? null : profile.getEducationLevel().getObjectID());
                profileItem.setManagementExperience(profile.getManagementExperience() == null ? "N/A" : referenceWfmMessageSource.localizeRef(profile.getManagementExperience()));
                profileItem.setManagementExperienceId(profile.getManagementExperience() == null ? null : profile.getManagementExperience().getObjectID());
                profileItem.setProjectLeadershipExperience(profile.getProjectLeadershipExperience() == null ? "N/A" : referenceWfmMessageSource.localizeRef(profile.getProjectLeadershipExperience()));
                profileItem.setProjectLeadershipExperienceId(profile.getProjectLeadershipExperience() == null ? null : profile.getProjectLeadershipExperience().getObjectID());
                if (employee.getPhoto() != null) {
                    profileItem.setEmployeeImageUrl(getImageUrl(employee.getPhoto().getObjectID()));
                } else {
                    profileItem.setEmployeeImageUrl(null);
                }

                EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                if (userBankAccount != null) {
                    UserBankAccountData bankAccountData = new UserBankAccountData();
                    bankAccountData.setBankName(userBankAccount.getBankName());
                    bankAccountData.setBankAddress(userBankAccount.getBankAddress());
                    bankAccountData.setAccountNumber(userBankAccount.getAccountNumber());
                    bankAccountData.setAccountName(userBankAccount.getAccountName());
                    bankAccountData.setSwiftCode(userBankAccount.getSwiftCode());
                    bankAccountData.setSortCode(userBankAccount.getSortCode());
                    bankAccountData.setIbanCode(userBankAccount.getIbanCode());
                    bankAccountData.setAgentID(userBankAccount.getAgentID());

                    profileItem.setBankAccountData(bankAccountData);
                }
            }
        } else {
            return null;
        }
        return profileItem;
    }

    public SettingsData getCompanyInfoSwitchvox() {
        EdsCompany company = profileManager.getUser().getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        SettingsData data = new SettingsData();
        if (companySettings != null) {
            data.setSwitchvoxUserName(companySettings.getSwitchvoxUserName() != null ? companySettings.getSwitchvoxUserName() : "");
            data.setSwitchvoxPassword(companySettings.getSwitchvoxPassword() != null ? companySettings.getSwitchvoxPassword() : "");
            data.setSwitchvoxServerID(companySettings.getSwitchvoxServerId() != null ? companySettings.getSwitchvoxServerId() : "");
        }
        return data;
    }

    public void updateCompanyInfoSwitchvox(SettingsData data) {
        EdsCompany company = profileManager.getUser().getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        if (companySettings != null) {
            companySettings.setSwitchvoxUserName(data.getSwitchvoxUserName());
            companySettings.setSwitchvoxPassword(data.getSwitchvoxPassword());
            companySettings.setSwitchvoxServerId(data.getSwitchvoxServerID());
            companySettingsManager.update(companySettings);
        }
    }

    public void updateCompanyInfo(SettingsData data) {
        EdsUser currentUser = profileManager.getUser();
        EdsCompany company = currentUser.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        if (companySettings != null) {
            if (data.getShortDateFormat() != null && !"".equals(data.getShortDateFormat())) {
                companySettings.setShortDateFormat(data.getShortDateFormat());
            }
            if (data.getLongDateFormat() != null && !"".equals(data.getLongDateFormat())) {
                companySettings.setLongDateFormat(data.getLongDateFormat());
            }

            companySettings.setPdfFontID(data.getPdfFontID());

            if (data.getPdfLimit() != null) {
                companySettings.setPdfLimit(data.getPdfLimit());
            }

            if (data.getExcelLimit() != null) {
                companySettings.setExcelLimit(data.getExcelLimit());
            }
            if (data.getEnableUploadTypes() != null && !"".equals(data.getEnableUploadTypes())) {
                companySettings.setEnableUploadTypes(data.getEnableUploadTypes());
            }
            if (data.getSharePointSiteUrls() != null && !"".equals(data.getSharePointSiteUrls())) {
                companySettings.setSharePointSiteUrls(data.getSharePointSiteUrls());
            }
            if (data.getSharePointClientId() != null && !"".equals(data.getSharePointClientId())) {
                companySettings.setSharePointClientId(data.getSharePointClientId());
            }
            if (data.getSharePointClientSecret() != null && !"".equals(data.getSharePointClientSecret())) {
                companySettings.setSharePointClientSecret(data.getSharePointClientSecret());
            }
            if (data.getHexColor() != null && !"".equals(data.getHexColor()) && data.getHexColor().length() == 6) {
                companySettings.setPdfStyleColor(data.getHexColor());
            } else {
                companySettings.setPdfStyleColor(DEFAULT_FONT_COLOR);
            }
            companySettings.setShowAccountingSettings(data.isShowAccountingSettings());
            companySettingsManager.update(companySettings);
        } else {
            companySettings = new EdsCompanySettings();
            if (data.getShortDateFormat() != null && !"".equals(data.getShortDateFormat())) {
                companySettings.setShortDateFormat(data.getShortDateFormat());
            }
            if (data.getLongDateFormat() != null && !"".equals(data.getLongDateFormat())) {
                companySettings.setLongDateFormat(data.getLongDateFormat());
            }

            companySettings.setPdfFontID(data.getPdfFontID());

            if (data.getPdfLimit() != null && !"".equals(data.getPdfLimit())) {
                companySettings.setPdfLimit(data.getPdfLimit());
            }

            if (data.getExcelLimit() != null && !"".equals(data.getExcelLimit())) {
                companySettings.setExcelLimit(data.getExcelLimit());
            }
            if (data.getEnableUploadTypes() != null && !"".equals(data.getEnableUploadTypes())) {
                companySettings.setEnableUploadTypes(data.getEnableUploadTypes());
            }
            if (data.getSharePointSiteUrls() != null && !"".equals(data.getSharePointSiteUrls())) {
                companySettings.setSharePointSiteUrls(data.getSharePointSiteUrls());
            }
            if (data.getSharePointClientId() != null && !"".equals(data.getSharePointClientId())) {
                companySettings.setSharePointClientId(data.getSharePointClientId());
            }
            if (data.getSharePointClientSecret() != null && !"".equals(data.getSharePointClientSecret())) {
                companySettings.setSharePointClientSecret(data.getSharePointClientSecret());
            }

            companySettingsManager.create(companySettings);
            company.setCompanySettings(companySettings);
        }
        company.setName(data.getCompanyName());
        company.setSameAsBilling(data.isSameAsBill());
        crmServiceLocal.updateAddresses(data.getBillAddresses(), company, EdsAddress.BILLING_ADDRESS, false);
        crmServiceLocal.updateAddresses(data.getMailAddresses(), company, EdsAddress.MAILING_ADDRESS, false);
        company.setCountryZone(data.getTimeZoneID() != null ? zoneManager.getCountryZone(data.getTimeZoneID()) : (data.getCountryID() != null ? zoneManager.getCountryZones(countryManager.get(data.getCountryID())).get(0) : null));
        company.setCountryRegion(data.getStateID() != null ? regionManager.get(data.getStateID()) : null);
        company.setPhone(data.getOfficeNumber());
        company.setMobilePhone(data.getMobileNumber());
        company.setFaxNumber(data.getFaxNumber());
        company.setWebsite(data.getWebsite());
        if (data.getEmail() != null) {
            company.setEmail(data.getEmail());
        }
        EdsCompanyEmail companyEmail = companyEmailManager.getByCompanyID(company.getObjectID());
        if (companyEmail == null) {
            companyEmail = new EdsCompanyEmail();
            companyEmail.setCompany(company);
        }
        companyEmail.setEmail(data.getBccEmail());
        companyEmailManager.createOrUpdate(companyEmail);

        EdsCompanySettingsCustomFields settingsCustomFields = createCompanySettingsCustomFields(data.getCustomFieldItems());
        companySettings.setCompanySettingsCustomFields(settingsCustomFields);

        EdsCrmAccount account = crmAccountManager.get(1);
        if (account != null && !account.isDeleted()) {
            List<EdsAddress> addresses = account.getAddresses();
            //Biling addresses
            int adresCount = 0;
            if (addresses != null && !addresses.isEmpty()) {
                for (EdsAddress address : addresses) {
                    if (address.isPrimary()) {
                        if (EdsAddress.BILLING_ADDRESS.equals(address.getRelationType())) {
                            address.setAddress(company.getAddress1());
                            address.setAddressb(company.getBillAddress2());
                            address.setCity(company.getCity());
                            if (company.getCountryZone() != null) {
                                address.setCountry(company.getCountryZone().getCountry());
                            }
                            address.setState(company.getCountryRegion());
                            address.setZipCode(company.getPostCode());
                            adresCount++;
                        } else if (address.isPrimary() && EdsAddress.MAILING_ADDRESS.equals(address.getRelationType())) {
                            address.setAddress(company.getAddress2());
                            address.setAddressb(company.getMailAddress2());
                            address.setCity(company.getMailingCity());
                            if (company.getMailingCountry() != null) {
                                address.setCountry(company.getMailingCountry());
                            }
                            address.setState(company.getMailingCountryRegion());
                            address.setZipCode(company.getMailingPostCode());
                            adresCount++;
                        }
                    }
                    if (adresCount == 2) {
                        break;
                    }
                }
            } else {
                EdsAddress billingAddress = new EdsAddress();
                billingAddress.setAddress(company.getAddress1());
                billingAddress.setAddressb(company.getBillAddress2());
                billingAddress.setCity(company.getCity());
                billingAddress.setState(company.getCountryRegion());
                if (company.getCountryZone() != null) {
                    billingAddress.setCountry(company.getCountryZone().getCountry());
                }
                billingAddress.setZipCode(company.getPostCode());
                billingAddress.setRelationType(EdsAddress.BILLING_ADDRESS);
                billingAddress.setPrimary(true);
                addressManager.create(billingAddress);
                account.setBillingAddress(billingAddress);

                EdsAddress mailingAddress = new EdsAddress();
                mailingAddress.setAddress(company.getAddress2());
                mailingAddress.setAddressb(company.getMailAddress2());
                mailingAddress.setCity(company.getMailingCity());
                if (company.getMailingCountry() != null) {
                    billingAddress.setCountry(company.getMailingCountry());
                }
                mailingAddress.setState(company.getMailingCountryRegion());
                mailingAddress.setZipCode(company.getMailingPostCode());
                mailingAddress.setRelationType(EdsAddress.MAILING_ADDRESS);
                mailingAddress.setPrimary(true);
                addressManager.create(mailingAddress);
                account.setMailingAddress(mailingAddress);
            }
        }

        String allowanceLimit2 = "";
        String wpsNo2 = "";
        KeyValueStruct[] keyValueStruct = data.getPayrollSettings().getSettings();
        if (keyValueStruct != null) {
            for (KeyValueStruct k : keyValueStruct) {
                if (k.getKey().equals(VISA_ALLOWANCE_LIMITS)) {
                    allowanceLimit2 = k.getValue();
                } else if (k.getKey().equals(WPS_NO)) {
                    wpsNo2 = k.getValue();
                }
            }
        }
        String allowanceLimit1 = payrollService.getCompanyPayrollSettings(VISA_ALLOWANCE_LIMITS);
        String wpsNo1 = payrollService.getCompanyPayrollSettings(WPS_NO);
        if (!ServerUtils.equalsString(allowanceLimit1, allowanceLimit2)) {
            companySettings.addHistoryChange(VISA_ALLOWANCE_LIMITS, allowanceLimit1, allowanceLimit2);
        }
        if (!ServerUtils.equalsString(wpsNo1, wpsNo2)) {
            companySettings.addHistoryChange(WPS_NO, wpsNo1, wpsNo2);
        }
        if (data.getPayrollSettings() != null) {
            payrollService.saveCompanyPayrollSettings(data.getPayrollSettings());
        }

        company.setShowWorkforceLogoOnPDF(data.isShowWorkforceLogoOnPDF());
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(company.getObjectID());
        StringBuilder ipRanges = new StringBuilder();
        if (data.getIPRanges() != null) {
            for (IpAddressRange ipAddressRange : data.getIPRanges()) {
                if ("".equals(ipAddressRange.getFromIP()) && "".equals(ipAddressRange.getToIP())) {
                    continue;
                }
                ipRanges.append(ipAddressRange.getFromIP()).append(';').append(ipAddressRange.getToIP()).append('|');
            }
        }
        companySystemSettings.setIpRanges(ipRanges.toString());
        companySystemSettings.setPasswordExpirationDayCount(data.getPasswordExpirationDayCount());
        companySystemSettings.setOverallDatePickerWeekStart(data.getOverallDatePickerWeekStart());
        companySystemSettings.setAlternativeCalendarEnum(AlternativeCalendarEnum.getByIdEnum(data.getAlternativeCalendarId()));
        companySystemSettings.setNameOrder(NameOrder.fromCode(data.getNameFormat()));
        ServerUtils.invalidateNameFormatCache();
        companySystemSettingsManager.update(companySystemSettings);
        // Enable/Disable VAT RETURN calculation for country (enable for GB)
        EdsCountry country = new EdsCountry();
        if (company.getCountryZone() != null) {
            country = zoneManager.getCountryZone(company.getCountryZone().getObjectID()).getCountry();
        }
        String value = EdsGenericSettings.NO;

        if (ServerUtils.isArabicCompany(company) || "GB".equals(country.getCode())) {
            value = EdsGenericSettings.YES;
        }
        genericSettingsManager.saveGenericSettings(company.getObjectID(), GenericSettingsEnum.VAT_RETURN_ENABLE, value);
        //save company language for all users
        if (!StringUtil.isEmpty(data.getInternationalization())) {
            saveLanguageForUser(data.getInternationalization(), true);
            company.setLocale(data.getInternationalization());
            companyManager.update(company);
        }
        if (!StringUtil.isEmpty(data.getThemeStyle())) {
            saveThemeForTheSystem(data.getThemeStyle());
        }
        CompanyData companyData = new CompanyData();
        companyData.setCompanyId(company.getObjectID());
        companyData.setName(data.getCompanyName());
        Integer leadSignupCompany = EdsContextParams.getLeadSignUpCompany();
        if (leadSignupCompany != null) {
            rabbitMQService.sendCompanySettingsUpdate(companyData, leadSignupCompany);
        }
        if (company.isHasTelegramBot() != null && company.isHasTelegramBot()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            CompanyInfoForTelegram request = new CompanyInfoForTelegram(company.getObjectID(), company.getPhone(),
                    company.getMobilePhone(), company.getFaxNumber(), company.getEmail(), payrollService.getCompanyPayrollSettings(WEBSITE));
            HttpEntity<CompanyInfoForTelegram> httpRequest = new HttpEntity<>(request, httpHeaders);
            restTemplate.postForObject("https://kpitgbot.kpi.com/api/v1/company", httpRequest, Boolean.class);
        }
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, companySettings, currentUser);
        workflowEvent.setEntityType(RelationItem.TYPE_COMPANY_SETTINGS);
        baseEventPostProcessor.registerEvent(CompanySettingsEventListener.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, companySettings, currentUser);
    }

    public Boolean deleteCurrentCompany() {
        EdsUser user = userManager.getUser();
        if (user != null && user.hasRole(EdsRole.ADMIN_CODE)) {
            try {
                EdsCompany company = companyManager.get(userManager.getUser().getCompany().getObjectID());
                company.setDeleted(true);
                company.setDeletedBy(user.getName());
                company.setDeletedTime(new Date());
                companyManager.createOrUpdate(company);
                companyManager.flushAndClear();
                return Boolean.TRUE;
            } catch (Exception e) {
                log.error("", e);
                return Boolean.FALSE;
            }
        } else {
            return Boolean.FALSE;
        }
    }

    public void updateSignUpCompanyInfo(SettingsData data) {
        EdsCompany company = userManager.getUser().getCompany();
        if (DEFAULT_COMPANY_NAME.equals(company.getName())) {
            EdsCrmAccount crmAccount = crmAccountManager.get(1);
            if (crmAccount != null) {
                crmAccount.setName(data.getCompanyName());
                crmAccountManager.createOrUpdate(crmAccount);
                try {
                    crmAccountSolrComponent.index(crmAccount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        company.setName(data.getCompanyName());
        if (!StringUtil.isEmpty(data.getInternationalization())) {
            saveLanguageForUser(data.getInternationalization(), true);
            company.setLocale(data.getInternationalization());
            companyManager.update(company);
        }
        company.setAnyDataMissing(false);
        Integer companyID = company.getObjectID();
        companyManager.createOrUpdate(company);
        companyManager.flushAndClear();

        Integer leadCompanyID = EdsContextParams.getLeadSignUpCompany();

        if (leadCompanyID != null) {
            Integer leadUserId = 1;
            // Lead copy qilish KPI Software Inc ga ko'chirildi Munir so'ragan

//            if (leadCompanyID == 3737) {
//                EdsCountryZone countryZone = company.getCountryZone();
//                if (countryZone != null) {
//                    EdsCountry country = countryZone.getCountry();
//                    if (country != null && "AE".equals(country.getCode())) {
//                        leadCompanyID = 42453;//mobile cloud Doni so'ragan.
//                        leadUserId = 2;//don.maughan@kpi.com
//                    }
//                }
//            }
            if (leadCompanyID == 100042) {//1erp.sa companyid 100030, userid = 4
                leadUserId = 4;//shahir@sahara.com
            }
            setSignupperCrmAccountName(leadUserId, companyID, leadCompanyID, data);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setSignupperCrmAccountName(Integer leadUserId, Integer companyID, Integer leadCompanyId, SettingsData data) {
        if (leadCompanyId != null) {
            String type = globalAuthJdbcSpringManager.getCompanyClusterType(leadCompanyId);
            String url = EdsSchemaUpdater.getClusterUrl(type);
            try (Connection con = WfmGetConnection.getConnection(url)) {
                String sql = "update \"" + leadCompanyId + "\".crmaccount set name = ? where signupcompanyid = " + companyID;
                try (PreparedStatement stmt = con.prepareStatement(sql)) {
                    stmt.setString(1, data.getCompanyName());
                    stmt.executeUpdate();

                    Thread thread = new Thread(() -> {
                        try {
                            ServerSecurityContext.getInstance().setCompanyId(leadCompanyId);
                            ServerSecurityContext.getInstance().setDatabase(type);

                            EdsCrmAccount edsCrmAccount = crmAccountManager.getSignupLeadByCompanyId(companyID);
                            try {
                                crmAccountSolrComponent.index(edsCrmAccount);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            if (edsCrmAccount != null && !edsCrmAccount.getCrmContacts().isEmpty()) {
                                Optional<EdsCrmContact> firstContact = edsCrmAccount.getCrmContacts().stream().findFirst();
                                if (firstContact.isPresent()) {
                                    contactSolrComponent.index(firstContact.get());
                                }
                            }
                            ServerSecurityContext.getInstance().removeCompanyId();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        Thread.yield();
                    });
                    thread.setPriority(Thread.MAX_PRIORITY);
                    thread.setDaemon(true);
                    thread.start();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private EdsCompanySettingsCustomFields createCompanySettingsCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsCompanySettingsCustomFields companySettingsCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                companySettingsCustomFields = companySettingsCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                companySettingsCustomFields = new EdsCompanySettingsCustomFields();
                companySettingsCFManager.create(companySettingsCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(companySettingsCustomFields, customFieldItems);
            return companySettingsCustomFields;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountryTimezone(Integer countryID) {
        List<EdsCountryZone> timezone = zoneManager.getCountryZones(countryManager.get(countryID));
        SelectItem[] result = new SelectItem[timezone.size()];
        int i = 0;
        for (EdsCountryZone item : timezone) {
            result[i] = new SelectItem(item.getObjectID(), item.getZone().getName());
            i++;
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getMultipleCountryTimezones(ArrayList<Integer> countries) {
        List<EdsCountryZone> cZones = zoneManager.getCountryZones(countries);
        ArrayList<SelectItem> result = new ArrayList<>();
        for (EdsCountryZone cZone : cZones) {
            SelectItem zone = new SelectItem(cZone.getObjectID(), cZone.getZone().getName());
            zone.setEntityId(cZone.getCountry().getObjectID());
            result.add(zone);
        }
        return result.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SettingsData getCompanySettings(boolean isAccountingGettingStarted) {
        SettingsData result = new SettingsData();
        EdsUser loggedUser = profileManager.getUser();
        EdsCompany company = loggedUser.getCompany();
        result.setCompanyID(company.getObjectID());
        result.setCompanyName(company.getName());
        result.setIndustries(getAsSelectItem(referenceManager.listReferences("_COMPANY_INDUSTRY"), 10));
        result.setNumberOfEmployees(getAsSelectItem(referenceManager.listReferences("CONTACT_NUMBER_OF_EMPLOYEES"), 10));
        result.setAccountingFrequency(payrollService.getCompanyPayrollSettings(PAY_FREQUENCY));
        result.setCompanyAddress(company.getAddress1());
        result.setCompanyAddress2(company.getBillAddress2());
        Optional.ofNullable(company.getLocale()).flatMap(x -> Optional.ofNullable(localeManager.getLocaleBylanguageCode(x))).ifPresent(locale -> result.setLocaleID(locale.getObjectID()));
        EdsCompanySettings companySettings = company.getCompanySettings();
        if (companySettings == null) {
            companySettings = new EdsCompanySettings();
        }
        result.setShortDateFormat(companySettings.getShortDateFormat());
        result.setLongDateFormat(companySettings.getLongDateFormat());
        result.setLongDateFormats(companySettings.getLongDateFormats());
        result.setShortDateFormats(companySettings.getShortDateFormats());
        result.setEnableUploadTypes(companySettings.getEnableUploadTypes());
        result.setSharePointSiteUrls(companySettings.getSharePointSiteUrls());
        result.setSharePointClientId(companySettings.getSharePointClientId());
        result.setSharePointClientSecret(companySettings.getSharePointClientSecret());
        result.setPdfFontID(companySettings.getPdfFontID());
        result.setHexColor(companySettings.getPdfStyleColor() != null ? companySettings.getPdfStyleColor() : DEFAULT_FONT_COLOR);
        result.setShowAccountingSettings(companySettings.isShowAccountingSettings());
        result.setCity(company.getCity());
        result.setCountry(getCountries());
        result.setState(commonService.getRegions());

        if (company.getCountryZone() != null) {
            result.setCountryID(company.getCountryZone().getCountry().getObjectID());
            result.setMainCountry(new SelectItem(company.getCountryZone().getCountry().getObjectID(), company.getCountryZone().getCountry().getName()));
            result.setTimeZoneID(company.getCountryZone().getObjectID());
        }
        if (company.getCountryRegion() != null && company.getCountryRegion().getObjectID() != null) {
            result.setStateID(company.getCountryRegion().getObjectID());
        }
        result.setPostCode(company.getPostCode());

        result.setSameAsBill(company.getSameAsBilling());

        result.setMailingAddress(company.getAddress2());
        result.setMailingAddress2(company.getMailAddress2());
        result.setMailingCity(company.getMailingCity());
        if (company.getMailingCountry() != null) {
            result.setMailingCountryId(company.getMailingCountry().getObjectID());
        }
        if (company.getMailingCountryRegion() != null) {
            result.setMailingStateId(company.getMailingCountryRegion().getObjectID());
        }
        result.setMailingPostCode(company.getMailingPostCode());
        result.setOfficeNumber(company.getPhone());
        result.setMobileNumber(company.getMobilePhone());
        result.setFaxNumber(company.getFaxNumber());
        result.setEmail(company.getEmail());
        result.setBccEmail(companyEmailManager.getCompanyEmail(company.getObjectID()));

        if (companySettings.getPdfLimit() != null) {
            result.setPdfLimit(companySettings.getPdfLimit());
        }

        if (companySettings.getExcelLimit() != null) {
            result.setExcelLimit(companySettings.getExcelLimit());
        }
        result.setShowWorkforceLogoOnPDF(company.getShowWorkforceLogoOnPDF());
        result.setThemeStyle(companySettings.getThemeForSystem() != null ? companySettings.getThemeForSystem() : EdsContextParams.getDefaultTheme());

        if (isAccountingGettingStarted) { //Accounting Getting Started parameters
            result.setAccountingGettingStarted(isAccountingGettingStarted);
            result.setCurrency(currencyService.getCurrencies(false));

            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            if (financialSettings != null) {
                result.setCurrencyID(financialSettings.getCurrency() != null ? financialSettings.getCurrency().getObjectID() : null);

                if (financialSettings.getFinancialYearEnd() != null) {
                    result.setFinancialYearEnd(new DateNonConvertable(financialSettings.getFinancialYearEnd()));
                }
                result.setConversionDate(financialSettings.getConversionDate());
            }
        }

        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(loggedUser);
        result.setInternationalization(!StringUtil.isEmpty(userSettings.getInternationalization()) ? userSettings.getInternationalization() : UiSettings.ENGLISH);
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(company.getObjectID());

        String ips = companySystemSettings.getIpRanges();
        ArrayList<IpAddressRange> ipAddressRanges = new ArrayList<>();
        if (ips != null) {
            for (String ipRange : ips.split("\\|")) {
                String[] ipArray = ipRange.split(";");
                if (ipArray.length == 1) {
                    ipAddressRanges.add(new IpAddressRange(ipArray[0], ""));
                } else {
                    ipAddressRanges.add(new IpAddressRange(ipArray[0], ipArray[1]));
                }

            }
        }
        result.setIPRanges(ipAddressRanges);
        if (companySystemSettings.getNameOrder() != null) {
            result.setNameFormat(companySystemSettings.getNameOrder().getCode());
        }
        result.setPasswordExpirationDayCount(companySystemSettings.getPasswordExpirationDayCount());
        result.setOverallDatePickerWeekStart(companySystemSettings.getOverallDatePickerWeekStart());
        result.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(companySettings.getCompanySettingsCustomFields(), commonService.getCompanyCustomFields(ViewName.CompanySettings)));

        ArrayList<Address> billingAddresses = new ArrayList<>();
        for (EdsAddress address : company.getBillingAddresses()) {
            if (address.isDeleted())
                continue;
            billingAddresses.add(address.getRPC());
        }
        result.setBillAddresses(billingAddresses.toArray(new Address[0]));
        ArrayList<Address> shippingAddresses = new ArrayList<>();
        for (EdsAddress address : company.getMailingAddresses()) {
            if (address.isDeleted())
                continue;
            shippingAddresses.add(address.getRPC());
        }
        result.setMailAddresses(shippingAddresses.toArray(new Address[0]));
        result.setReasons(availabilityService.getReasons(loggedUser.getObjectID(), true));
        return result;
    }

    private SelectItem[] getAsSelectItem(List listOfObject, final int type) {
        return ServerUtils.getAsSelectItem(listOfObject, type);
    }

    @Override
    public SelectItem[] getPayFrequencies() {
        final SelectItem[] frequencies = new SelectItem[Frequency.values().length - 1];
        int i = 0;
        for (Frequency frequency : Frequency.values()) {
            if (Frequency.DAILY.equals(frequency)) {
                continue;
            }
            frequencies[i] = new SelectItem(frequency.getId(), referenceWfmMessageSource.localize(frequency.getCode(), frequency.getName()));
            i++;
        }
        return frequencies;
    }

    @Override
    public EmployerSettings getCompanyPayrollSettings() {
        return payrollService.getCompanyPayrollSettings();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPdfFonts() {
        List<EdsPdfFonts> pdfFontsList = companyPdfFontsManager.getPdfFonts();
        SelectItem[] items = new SelectItem[companyPdfFontsManager.getPdfFonts().size()];
        int i = 0;
        for (EdsPdfFonts pd : pdfFontsList) {
            if (pd.getObjectID() != null && pd.getFontName() != null) {
                items[i] = new SelectItem(pd.getObjectID(), pd.getFontName());
                i++;
            }
        }
        return items;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CompanyOpportunitySettings getCompanyOpportunitySettings() {
        EdsCompanySettings settings = profileManager.getUser().getCompany().getCompanySettings();
        final EdsNumberingSettings edsSettings = numberingSettingsManager.getNumberingSetting();
        CompanyOpportunitySettings opportunitySettings = new CompanyOpportunitySettings();
        opportunitySettings.setFillOpportunitItems(settings.isFillOpportunityItemWithInventory());
        opportunitySettings.setRequireContractUpload(settings.getOpportunityRequireContractUpload());
        opportunitySettings.setJoinOpportunityToExpenseClaim(settings.getJoinOpportunityToExpenseClaim());
        opportunitySettings.setEmailAutoLinking(settings.getEmailAutoLinking());
        opportunitySettings.setGenerateCrmAccountNumber(settings.getGenerateCrmAccountNumbering());
        opportunitySettings.setOpportunityNumberingSettings(edsSettings.getOpportunityNumberingFormat());
        opportunitySettings.setPrefix(edsSettings.getTrackerPrefix());
        opportunitySettings.setImportPreference(settings.getImportPreference());
        opportunitySettings.setContactTypeId(settings.getSipuniContactType());
        opportunitySettings.setSource(settings.getOpportunitySourceId() != null ? referenceManager.get(settings.getOpportunitySourceId()).getAsSelectItem() : null);
        opportunitySettings.setStage(settings.getOpportunityStageId() != null ? referenceManager.get(settings.getOpportunityStageId()).getAsSelectItem() : null);
        opportunitySettings.setConvertsTo(settings.getConvertsTo());
        return opportunitySettings;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SettingsData getInvoiceSettings() {
        return invoiceService.getInvoiceSettings();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CredentialsItem getCredentials() {
        EdsUser user = referenceManager.getUser();
        CredentialsItem credentialsItem = new CredentialsItem();
        credentialsItem.setLogin(user.getUserName());
        credentialsItem.setRegistrationType(user.getRegistrationType());
        if (user.isClientContact()) {
            EdsClientContact clientContact = user.getClientContact();
            if (clientContact != null && clientContact.getCrmContact() != null) {
                credentialsItem.setEmail(clientContact.getCrmContact().getPrimaryEmail());
            } else {
                credentialsItem.setEmail(user.getEmail());
            }
        } else if (user.isEmployee()) {
            EdsEmployee employee = user.getEmployee();
            if (employee != null && employee.getContact() != null) {
                credentialsItem.setEmail(employee.getContact().getPrimaryEmail());
            } else {
                credentialsItem.setEmail(user.getEmail());
            }
        } else {
            credentialsItem.setEmail(user.getEmail());
        }
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        credentialsItem.setInternationalization(!StringUtil.isEmpty(userSettings.getInternationalization()) ? userSettings.getInternationalization() : UiSettings.ENGLISH);

        credentialsItem.setCountry(getCountries());
        EdsCountryZone countryZone = user.getCountryZone();
        if (countryZone != null) {
            credentialsItem.setCountryID(countryZone.getCountry().getObjectID());
            credentialsItem.setTimeZoneId(countryZone.getObjectID());
        }
        credentialsItem.setStartPage(userSettings.getStartPage());
        List<EdsPermission> mainMenu = permissionManager.listContext();
        List<SelectItem> items = new ArrayList<>();
        int i = 0;
        for (EdsPermission item : mainMenu) {
            if (!PermissionConstants.SETTINGS_MAIN_MENU.equals(item.getCode()) && PermissionConstants.ContextUrl.containsKey(item.getCode())) {
                if (ServerUtils.hasPermission(item.getCode())) {
                    SelectItem selItem = new SelectItem();
                    selItem.setId(i++);
                    selItem.setName(PermissionConstants.ContextUrl.get(item.getCode()));
                    selItem.setDescription(item.getCode());
                    items.add(selItem);
                }
            }
        }
        List<UserCompanyDTO> companyList = globalAuthJdbcSpringManager.getUserCompanyByEmail(null, user.getUserName());
        StringBuilder companyIds = new StringBuilder();
        for (int in = 0; in < companyList.size(); in++) {
            companyIds.append(companyList.get(in).getCompanyID());
            if (in < companyList.size() - 1) {
                companyIds.append(",");
            }
        }
        credentialsItem.setStartPageLists(items.toArray(new SelectItem[0]));
        credentialsItem.setAdvancedPasswordEnabled(globalAuthJdbcSpringManager.isEnabledAdvancedPassword(companyIds.toString()));
        return credentialsItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmailTemplateCategories(Integer moduleID) {
        if (moduleID == null) {
            return new SelectItem[0];
        }
        SelectItem[] selectItems = commonServiceLocal.convertReference2SelectItem(referenceManager.getReference(moduleID).getCode(), false, null);
        if (selectItems != null) {
            Arrays.sort(selectItems, Comparator.comparing(SelectItem::getName));
        }
        return selectItems != null ? selectItems : new SelectItem[0];
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmailTemplateCategoriesByList(ListingFilterParameter fp) {
        HashMap<EdsReference, Integer> resultMap = new LinkedHashMap<>();
        List<EdsReference> list = referenceManager.listReferences(EMAIL_TEMPLATE_CATEGORY);
        List<EdsEmailTemplate> emailTemplates = emailTemplateManager.getCompanyEmailTemplates(fp);

        for (EdsReference category : list) {
            resultMap.put(category, 0);
        }
        for (EdsEmailTemplate template : emailTemplates) {
            EdsReference category = template.getTemplateCategory();
            if (resultMap.containsKey(category)) {
                int value = resultMap.get(category);
                resultMap.put(category, value + 1);
            } else {
                resultMap.put(category, 1);
            }
        }
        List<SelectItem> categorySelectItems = new ArrayList<>();
        for (Map.Entry<EdsReference, Integer> entry : resultMap.entrySet()) {
            SelectItem item = new SelectItem();
            if (entry.getKey() != null) {
                String categoryName = entry.getKey().getName();
                String categoryCode = entry.getKey().getCode();
                String localize = referenceWfmMessageSource.localize(categoryCode, categoryName != null ? categoryName : categoryCode);
                item.setName(localize != null ? localize : categoryName != null ? categoryName : categoryCode + " (" + entry.getValue() + ")");
                item.setDescription(categoryCode);
            }
            categorySelectItems.add(item);
        }
        return categorySelectItems.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCurrentOrSomeUsers() {
        List<EdsUser> list = userManager.getUsers();
        List<SelectItem> items = new ArrayList<>();
        for (EdsUser us : list) {
            if (us.hasRole(roleManager.get(EdsRole.ADMIN)) || us.hasRole(roleManager.get(EdsRole.DR)) ||
                    us.hasRole(roleManager.get(EdsRole.ACCOUNTANT)) || us.hasRole(roleManager.get(EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE))) {
                items.add(new SelectItem(us.getObjectID(), us.getName()));
            }
        }
        items.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return items.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getUsers(boolean isEditForm) {
        List<EdsEmployee> list = employeeManager.getCompanyEmployees();
        List<EdsSignature> signatures = signatureManager.getSignatures(null);
        List<SelectItem> items = new ArrayList<>();
        if (!isEditForm) {
            for (EdsSignature signature : signatures) {
                list.remove(signature.getUser());
            }
        }
        for (EdsEmployee user : list) {
            items.add(new SelectItem(user.getObjectID(), user.getName()));
        }
        items.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return items.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<EmailTemplateItem> getEmailTemplateList(ListingFilterParameter fp) {
        List<EdsEmailTemplate> list = emailTemplateManager.getCompanyEmailTemplates(fp);
        int totalCount = list.size();
        if (fp.getLimit() > 0) {
            list = ListUtils.getSublist(list, fp.getStart(), fp.getLimit());
        }

        ArrayList<EmailTemplateItem> items = new ArrayList<>();
        for (EdsEmailTemplate emailTemplate : list) {
            EmailTemplateItem item = new EmailTemplateItem();
            item.setObjectId(emailTemplate.getObjectID());
            item.setName(emailTemplate.getName());
            item.setCategoryName(emailTemplate.getTemplateCategory() != null ? referenceWfmMessageSource.localizeRef(emailTemplate.getTemplateCategory()) : null);
            item.setDefault(emailTemplate.isDefault());
            item.setSubject(emailTemplate.getSubject());
            item.setCompanyEmailTemplate(emailTemplate.getCompanyEmailTemplate());
            item.setOnlyMine(emailTemplate.getUserID() != null);
            item.setModule(emailTemplate.getModule() != null ? referenceWfmMessageSource.localizeRef(emailTemplate.getModule()) : null);
            items.add(item);
        }
        return new ListResult<>(items, totalCount);
    }

    @Override
    public ListResult<SMSTemplateItem> getSMSTemplateList(ListingFilterParameter fp) {
        List<EdsSMSTemplates> list = smsTemplateManager.getSMSTemplates(fp);
        int totalCount = list.size();
        if (fp.getLimit() > 0) {
            list = ListUtils.getSublist(list, fp.getStart(), fp.getLimit());
        }
        ArrayList<SMSTemplateItem> items = new ArrayList<>();
        for (EdsSMSTemplates sms : list) {
            items.add(sms.getRPC(new SMSTemplateItem()));
        }
        return new ListResult<>(items, totalCount);
    }

    /**
     * Related EML Templates saved with ZIP file and sended user email
     *
     * @param fp
     */
    public String saveEMLTemplatesWithZIPFile(ListingFilterParameter fp) {
        EdsUser user = userManager.getUser();
        final Date currentDate = new Date();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        List<EdsEmailTemplate> templateList = emailTemplateManager.getCompanyEmailTemplates(fp);
        for (EdsEmailTemplate emailTemplate : templateList) {
            EML eml = new EML();
            if (emailTemplate.getMessageHTML().trim().startsWith("<html>") && emailTemplate.getMessageHTML().trim().endsWith("</html>")) {
                eml.set_HTMLBody(emailTemplate.getMessageHTML());
            } else {
                String htmlBody = "<html>" +
                        "<head>" +
                        "<meta http-equiv=\"Content-type\" content=\"text/html; charset=UTF-8\">" +
                        "</head>" +
                        "<body>" +

                        emailTemplate.getMessageHTML() +

                        "</body>" +
                        "</html>";
                eml.set_HTMLBody(htmlBody);
            }
            eml.set_Subject(emailTemplate.getSubject() != null ? emailTemplate.getSubject() : "");
            eml.set_From((emailTemplate.getFromEmail() != null && !"".equals(emailTemplate.getFromEmail().trim())) ? emailTemplate.getFromEmail() : user.getEmail()/*"ilhombeks@gmail.com"*/);
            eml.set_To((emailTemplate.getSendEmail() != null && !"".equals(emailTemplate.getSendEmail().trim())) ? emailTemplate.getSendEmail() : user.getEmail()/*"ilhombeks@gmail.com"*/);
            eml.set_FileName(emailTemplate.getName().trim());

            eml.set_Template_Name(emailTemplate.getName());
            eml.set_Category_Id((emailTemplate.getTemplateCategory() != null ? emailTemplate.getTemplateCategory().getObjectID().toString() : Integer.toString(-3)));//Integer.valueOf(-3) -- if not category
            eml.set_Is_Default((emailTemplate.isDefault() != null ? emailTemplate.isDefault().toString() : "false"));
            eml.set_Company_Id(/*emailTemplate.getCompany() != null ? emailTemplate.getCompany().getObjectID().toString() : Integer.valueOf(-2).toString()*/
                    (user != null && user.getCompany() != null ? user.getCompany().getObjectID().toString() : Integer.toString(-2)));//Integer.valueOf(-2) -- if not company id
            eml.set_Category_Name((emailTemplate.getTemplateCategory() != null ? emailTemplate.getTemplateCategory().getName() : ""));
            eml.set_From_User_Id((emailTemplate.getFromUser() != null ? emailTemplate.getFromUser().toString() : Integer.toString(-1)));//Integer.valueOf(-1) -- if not from user id
            eml.set_From_User_Name((emailTemplate.getFromUser() != null ? (userManager.get(emailTemplate.getFromUser()) != null ? userManager.get(emailTemplate.getFromUser()).getName() : "") : ""));
            //WFT_Templates_FirstNameLastName20102109.zip
            new CreateEMLFile(eml, (user.getObjectID() + "#eml#" + EML.EMAIL_TEMPLATE_FOLDER_NAME + user.getFirstName() + user.getLastName() + dateFormat.format(currentDate)));
        }

        File folderName = new File(CreateEMLFile.directoryFile.getPath());

        CreateZipFile newZipFile = new CreateZipFile(folderName);
        if (newZipFile.isZippedFiles) {
            try {
                File zipFile = new File(EML.getEMLFileDirectory() + folderName.getName() + ".zip");
                Integer zipFileId = commonServiceLocal.saveZipFileForAttachment(zipFile);
                if (zipFileId != null) {

                    String url = commonServiceLocal.getFileUrl(zipFileId, ZIP_WITH_EML_FILE, true);
                    boolean sendMessage = sendEmailZipFile(zipFile, user, url);
                    if (sendMessage) {
                        for (File file : folderName.listFiles()) {
                            if (file.isDirectory()) {
                                for (File f : file.listFiles()) {
                                    System.out.println("Deleted: " + f.getName() + " file");
                                    f.delete();
                                }
                            } else {
                                System.out.println("Deleted: " + file.getName() + " file");
                                file.delete();
                            }
                        }
                        System.out.println("Deleted: " + folderName.getName() + " folder");
                        folderName.delete();
                        System.out.println("Path - " + zipFile.getAbsolutePath());
                        System.out.println("Deleted - " + zipFile.delete());
                    }
                } else {
                    return "";
                }

            } catch (Exception e) {
                System.out.println("Message has not sent[TO:" + user.getEmail() + "]");
                return "";
            }
            return user.getEmail();
        }
        return "";
    }

    private boolean sendEmailZipFile(File zipFile, EdsUser edsUser, String url) throws Exception {
        InputStream source = new FileInputStream(zipFile);
        String subject = EdsContextParams.getProductName() + " Email Templates";
        String userName = edsUser.getName();
        String toEmail = edsUser.getEmail();
        EdsCompany company = edsUser.getCompany();
        Format formatter;
        if (company != null && company.getCompanySettings() != null) {
            formatter = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat(), Locale.ENGLISH);
        } else {
            formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        }

        String message = "<html>" +
                "<body>" +
                "Dear  " + userName + "," +
                "<BR><BR>You had requested an export of email templates from " + EdsContextParams.getProductName() + " on \"" + formatter.format(new Date(zipFile.lastModified())) + "\"." +
                "<BR><BR>This is to confirm that we have successfully exported email templates in eml format and archived to package, which is available within 30 days to download:" +
                "<BR><BR>" +
                "<A HREF=\" " + url + "\">" + removeUserId(zipFile.getName()) + "</A>" +
                "</body>" +
                "<html>";

        EdsMailer mailer;
        try {
            EdsMessage edsMessage = new EdsMessage();
            edsMessage.setSubject(subject);
            edsMessage.setText(message);
            if (company != null) {
                edsMessage.setCompanyID(company.getObjectID());
                edsMessage.addBcc(companyEmailManager.getCompanyEmail(company.getObjectID()));
            }
            edsMessage.setTo(toEmail);
            mailer = EdsMailer.getNewInstance(edsMessage, null);
        } catch (Exception ex) {
            System.out.println("Cannot get instance of EdsMailer, exception: " + ex);
            return false;
        }
        mailer.addAttachment(removeUserId(zipFile.getName()), "application/x-zip-compressed", source);
        mailer.sendSynchronized();
        System.out.println("Message sent[TO:" + toEmail + ", SUBJECT:" + subject + "]");
        source.close();
        return true;
    }

    private String removeUserId(final String zipFileName) {
        int t = zipFileName.lastIndexOf("#eml#");//
        return zipFileName.substring(t).replace("#eml#", "");
    }

    /**
     * Sorted email template categories
     *
     * @param categoryId
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<String> getEmailTemplateCategoryFields(Integer categoryId) {
        EdsReference category = referenceManager.get(categoryId);
        String code = category.getCode();
        Map<String, String> personalAttrMap = getPersonalCategories(code);
        ArrayList<String> result = new ArrayList<>(personalAttrMap.keySet());
        return result;
    }

    @Override
    public ArrayList<String> getEmailTemplateModuleAttributes(Integer moduleID) {
        ArrayList<String> result = new ArrayList<>();
        String moduleCode = referenceManager.get(moduleID).getCode();
        if (ET_CASE_MODULE.equals(moduleCode)) {
            result = getCaseAttributes();
        } else if (ET_LEAD_MODULE.equals(moduleCode)) {
            result = getLeadAttributes();
        } else if (ET_CONTACT_MODULE.equals(moduleCode)) {
            result = getContactAttributes();
        } else if (ET_EVENT_MODULE.equals(moduleCode)) {
            result = getEventAttributes();
        } else if (ET_CALL_MODULE.equals(moduleCode)) {
            result = getCallAttributes();
        } else if (ET_EMPLOYEE_MODULE.equals(moduleCode)) {
            result = getEmployeeAttributes();
        } else if (ET_SALES_QUOTE_MODULE.equals(moduleCode)) {
            result = getSalesQuoteAttributes();
        } else if (ET_RFP_MODULE.equals(moduleCode)) {
            result = getRfpAttributes();
        } else if (ET_PROJECT_MODULE.equals(moduleCode)) {
            result = getProjectAttributes();
        } else if (ET_TASK_MODULE.equals(moduleCode)) {
            result = getTaskAttributes();
        } else if (ET_RFQ_MODULE.equals(moduleCode)) {
            result = getRFQAttributes();
        } else if (ET_CANDIDATE_MODULE.equals(moduleCode)) {
            result = getCandidateAttributes();
        } else if (ET_OPPORTUNITY_MODULE.equals(moduleCode)) {
            result = getOpportunityAttributes();
        }
        result.sort(String::compareToIgnoreCase);
        for (String string : result) {
            string.replaceAll(EmailTemplateConstants.CUSTOM_FIELD_PREFIX, "");
        }
        return result;
    }

    private ArrayList<String> getEventAttributes() {
        ArrayList<String> result = new ArrayList<>();
        SelectItem[] items = CRM.EVENT.EVENT_ATTRIBUTES;
        for (SelectItem item : items) {
            result.add(item.getDescription());
        }
        result.add(CRM.EVENT.START_DATE);
        result.add(ET_END_DATE);
        result.add(ET_SUBJECT);
        result.add(ET_DESCRIPTION);
        result.add(ET_SIGNATURE);
        return result;
    }

    private ArrayList<String> getCallAttributes() {
        ArrayList<String> result = new ArrayList<>();
        SelectItem[] items = CRM.EVENT.CALL_ATTRIBUTES;
        for (SelectItem item : items) {
            result.add(item.getDescription());
        }
        result.add(CRM.EVENT.START_DATE);
        result.add(ET_END_DATE);
        result.add(ET_SUBJECT);
        result.add(ET_DESCRIPTION);
        result.add(ET_SIGNATURE);
        return result;
    }

    private ArrayList<String> getOpportunityAttributes() {
        ArrayList<String> result = new ArrayList<>();
        SelectItem[] items = CRM.OPPORTUNITY.OPPORTUNITY_ATTRIBUTES;
        for (SelectItem item : items) {
            result.add(item.getDescription());
        }
        return result;
    }

    private ArrayList<String> getEmployeeAttributes() {
        ArrayList<String> result = new ArrayList<>();
        List<ModelField> fields = modelFieldManager.getFields(LayoutRPC.HRMS_EMPLOYEE_FORM);
        if (fields != null && fields.size() > 0) {
            for (ModelField field : fields) {
                if (field.getField_ID() != null && field.isUsableByWorkflow() && !field.isIsCustomField()) {
                    result.add("${" + field.getField_ID().toLowerCase() + "}");
                }
            }
        }
        result.add(ET_SIGNATURE);
        result.add(ET_LINK);
        result.add(EmailTemplateConstants.HRMS.EMPLOYEE.LOGIN_LINK_WITH_ACCESS);
        result.add(EmailTemplateConstants.HRMS.EMPLOYEE.SUPERVISOR_LOGIN_LINK_WITH_ACCESS);
        result.add(EmailTemplateConstants.HRMS.EMPLOYEE.ADDITIONAL_PAYMENT_LINK_WITH_ACCESS);
        result.add(EmailTemplateConstants.HRMS.EMPLOYEE.SUPERVISOR_EMAIL);
        result.add(EmailTemplateConstants.HRMS.EMPLOYEE.SUPERVISOR_PHONE);
        SelectItem[] reasons = allInOneService.getReasons(userManager.getUser().getObjectID());
        for (SelectItem reason : reasons) {
            String code = reason.getDescription().toLowerCase().
                    replaceAll("а", "a").
                    replaceAll("б", "b").
                    replaceAll("в", "v").
                    replaceAll("г", "g").
                    replaceAll("д", "d").
                    replaceAll("е", "e").
                    replaceAll("ё", "yo").
                    replaceAll("ж", "zh").
                    replaceAll("з", "z").
                    replaceAll("и", "i").
                    replaceAll("й", "j").
                    replaceAll("к", "k").
                    replaceAll("л", "l").
                    replaceAll("м", "m").
                    replaceAll("н", "n").
                    replaceAll("о", "o").
                    replaceAll("п", "p").
                    replaceAll("р", "r").
                    replaceAll("с", "s").
                    replaceAll("т", "t").
                    replaceAll("у", "u").
                    replaceAll("ф", "f").
                    replaceAll("х", "h").
                    replaceAll("ц", "c").
                    replaceAll("ч", "ch").
                    replaceAll("ш", "sh").
                    replaceAll("щ", "sch").
                    replaceAll("ъ", "j").
                    replaceAll("ы", "i").
                    replaceAll("ь", "j").
                    replaceAll("э", "e").
                    replaceAll("ю", "yu").
                    replaceAll("я", "ya");
            result.add("${" + code + "_link}");
            result.add("${" + code + "_link_with_access}");
        }
        List<EdsCustomForm> customForms = customFormManager.list(new ListingFilterParameter());
        if (customForms != null && !customForms.isEmpty()) {
            customForms.forEach(cf -> {
                result.add("${" + cf.getFormID().toLowerCase().replaceAll("[()]", "") + "_link}");
                result.add("${" + cf.getFormID().toLowerCase().replaceAll("[()]", "") + "_link_with_access}");
            });
        }
        result.add(EmailTemplateConstants.HRMS.EMPLOYEE.CURRENT_USER);
        return result;
    }

    private ArrayList<String> getProjectAttributes() {
        ArrayList<String> result = new ArrayList<>();
        List<ModelField> fields = modelFieldManager.getFields(LayoutRPC.PROJECT_FORM);
        if (fields != null && fields.size() > 0) {
            for (ModelField field : fields) {
                if (field.getField_ID() != null && field.isUsableByWorkflow() && !field.isIsCustomField()) {
                    result.add("${" + field.getField_ID().toLowerCase() + "}");
                }
            }
        }
        for (SelectItem item : EmailTemplateConstants.PROJECT.ATTRIBUTES) {
            result.add(item.getDescription());
        }
        result.add(ET_SIGNATURE);
        return result;
    }

    private ArrayList<String> getTaskAttributes() {
        ArrayList<String> result = new ArrayList<>();
        List<ModelField> fields = modelFieldManager.getFields(LayoutRPC.TASK_MAX_FORM);
        if (fields != null && fields.size() > 0) {
            for (ModelField field : fields) {
                if (field.getField_ID() != null && field.isUsableByWorkflow() && !field.isIsCustomField()) {
                    result.add("${" + field.getField_ID().toLowerCase() + "}");
                }
            }
        }
        SelectItem[] items = EmailTemplateConstants.TASK.ATTRIBUTES;
        for (SelectItem item : items) {
            result.add(item.getDescription());
        }
        return result;
    }

    private ArrayList<String> getCaseAttributes() {
        ArrayList<String> result = new ArrayList<>();
        SelectItem[] items = CRM.CRM_CASE.CASE_ATTRIBUTES;
        for (SelectItem item : items) {
            result.add(item.getDescription());
        }
        result.add(ET_CASE_ASSIGNEE);
        result.add(CRM.CRM_CASE.CASE_RESOLVER);
        result.add(ET_CASE_SUBJECT);
        result.add(ET_CASE_ORIGIN);
        result.add(ET_CASE_REASON);
        result.add(ET_STATUS);
        result.add(CRM.TYPE);
        result.add(ET_PRIORITY);
        result.add(ET_SIGNATURE);
        return result;
    }

    private ArrayList<String> getLeadAttributes() {
        ArrayList<String> result = new ArrayList<>();
        SelectItem[] items = CRM.CRM_LEAD.LEAD_ATTRIBUTES;
        for (SelectItem item : items) {
            result.add(item.getDescription());
        }
        result.add(CRM.CRM_LEAD.LEAD_ADDRESS);
        result.add(ET_CASE_ASSIGNEE);
        result.add(CRM.CRM_LEAD.LEAD_BACKUP_ASSIGNEE);
        result.add(CRM.CRM_LEAD.LEAD_CAMPAIGN_NAME);
        result.add(CRM.CRM_LEAD.LEAD_COMPANY_NAME);
        result.add(ET_FIRST_NAME);
        result.add(ET_LAST_NAME);
        result.add(CRM.CRM_LEAD.LEAD_JOB_TITLE);
        result.add(CRM.CRM_LEAD.LEAD_OWNER);
        result.add(CRM.CRM_LEAD.LEAD_RATING);
        result.add(CRM.CRM_LEAD.LEAD_SOURCE);
        result.add(ET_STATUS);
        result.add(ET_SIGNATURE);

        List<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Lead);
        for (CompanyCustomFieldItem customFieldsItem : customFieldsItems) {
            result.add(CUSTOM_FIELD_PREFIX + ServerUtils.getStringAsAttribute(customFieldsItem.getFieldName().replaceAll("\\s+", "")));
        }
        return result;
    }

    private ArrayList<String> getContactAttributes() {
        ArrayList<String> result = new ArrayList<>();
        SelectItem[] items = CRM.CRM_CONTACT.CONTACT_ATTRIBUTES;
        for (SelectItem item : items) {
            result.add(item.getDescription());
        }
        result.add(CRM.CRM_CONTACT.CONTACT_ADDRESS);
        result.add(CRM.CRM_CONTACT.CONTACT_CAMPAIGN_NAME);
        result.add(CRM.CRM_CONTACT.CONTACT_COMPANY_NAME);
        result.add(ET_EMAIL);
        result.add(ET_FIRST_NAME);
        result.add(ET_LAST_NAME);
        result.add(CRM.CRM_CONTACT.CONTACT_JOB_TITLE);
        result.add(CRM.CRM_CONTACT.CONTACT_OWNER);
        result.add(ET_SIGNATURE);
        result.add(OBJECT_KEY);

        List<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Contact);
        for (CompanyCustomFieldItem customFieldsItem : customFieldsItems) {
            result.add(CUSTOM_FIELD_PREFIX + ServerUtils.getStringAsAttribute(customFieldsItem.getFieldName().replaceAll("\\s+", "")));
        }

        return result;
    }

    private ArrayList<String> getSalesQuoteAttributes() {
        ArrayList<String> result = new ArrayList<>();
        SelectItem[] items = ACCOUNTING.SALE_QUOTE_ATTRIBUTES;
        for (SelectItem item : items) {
            result.add(item.getDescription());
        }
        result.add(ET_CUSTOMER);
        result.add(ACCOUNTING.PROJECT);
        result.add(ET_START_DATE);
        result.add(ET_DUE_DATE);
        result.add(ACCOUNTING.REFERENCE);
        result.add(ACCOUNTING.PO_NUMBER);
        result.add(ACCOUNTING.SHIP_VIA);
        result.add(ACCOUNTING.TOTAL);
        result.add(ACCOUNTING.SUB_TOTAL);
        result.add(ACCOUNTING.TOTAL_INVOICE_CURRENCY);
        result.add(ACCOUNTING.TOTAL_DISCOUNT);
        result.add(ET_STATUS);
        result.add(ET_SIGNATURE);
        return result;
    }

    private ArrayList<String> getRfpAttributes() {
        ArrayList<String> result = new ArrayList<>();
        SelectItem[] items = ACCOUNTING.RFP_ATTRIBUTES;
        for (SelectItem item : items) {
            result.add(item.getDescription());
        }
        result.add(ET_CUSTOMER);
        result.add(ACCOUNTING.PROJECT);
        result.add(ET_DUE_DATE);
        result.add(ET_STATUS);
        result.add(ET_SIGNATURE);
        return result;
    }

    private ArrayList<String> getCandidateAttributes() {
        ArrayList<String> result = new ArrayList<>();
        result.add(EmailTemplateConstants.HRMS.CANDIDATE.ATTACHMENTS);
        result.add(EmailTemplateConstants.HRMS.CANDIDATE.MATCHED_VACANCIES);
        result.add(EmailTemplateConstants.HRMS.CANDIDATE.PROJECT_NUMBER);
        result.add(EmailTemplateConstants.HRMS.CANDIDATE.VACANCY_MANAGER);
        result.add(EmailTemplateConstants.HRMS.CANDIDATE.VACANCY_MANAGER_EMAIL);
        result.add(EmailTemplateConstants.HRMS.CANDIDATE.VACANCY_MANAGER_TELEGRAM);
        result.add(EmailTemplateConstants.HRMS.CANDIDATE.VACANCY_MANAGER_PHONE);
        result.add(EmailTemplateConstants.CRM.OWNER_EMAIL);
        result.add(EmailTemplateConstants.CRM.PHONE);
        result.add(EmailTemplateConstants.CRM.MOBILE);
        result.add(EmailTemplateConstants.CRM.CREATION_DATE);
        result.add(EmailTemplateConstants.CRM.MODIFIED_DATE);
        result.add(EmailTemplateConstants.ET_OWNER_TELEGRAM);
        result.add(EmailTemplateConstants.OBJECT_KEY);
        return result;
    }

    private ArrayList<String> getRFQAttributes() {
        ArrayList<String> result = new ArrayList<>();
        result.add(ET_LINK);
        result.add(ET_COMPANY_NAME);
        result.add(ET_CUSTOMER);
        result.add(ACCOUNTING.PROJECT);
        result.add(ET_DUE_DATE);
        result.add(ET_START_DATE);
        result.add(ET_NUMBER);
        return result;
    }

    /**
     * Handles the email template categories
     *
     * @param code
     * @return
     */
    private Map<String, String> getPersonalCategories(String code) {
        Map<String, String> personalAttrMap;
        switch (code) {
            case RECEIPT_CATEGORY -> personalAttrMap = EmailTemplateUtils.getReceiptCategoryFields();
            case HR_REMINDERS_CATEGORY -> personalAttrMap = EmailTemplateUtils.getHRReminderCategoryFields();
            case PURCHASE_ORDER_CATEGORY -> personalAttrMap = EmailTemplateUtils.getPuchaseOrderCategoryFields();
            case PURCHASE_ORDER_MANAGER_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getPuchaseOrderManagerCategoryFields();
            case SALES_INVOICE_CATEGORY -> personalAttrMap = EmailTemplateUtils.getSaleInvoiceCategoryFields();
            case SALES_QUOTE_CATEGORY -> personalAttrMap = EmailTemplateUtils.getSaleQuoteCategoryFields();
            case SALES_QUOTE_MANAGER_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getSaleQuoteManagerCategoryFields();
            case SALES_ORDER_CATEGORY -> personalAttrMap = EmailTemplateUtils.getSaleOrderCategoryFields();
            case EXPENSE_CLAIM_CATEGORY_SUBMIT -> personalAttrMap = EmailTemplateUtils.getExpencesClaimCategoryFields();
            case EXPENSE_CLAIM_CATEGORY_RESUBMIT ->
                    personalAttrMap = EmailTemplateUtils.getExpencesClaimCategoryFields();
            case RECEIVE_PAYMENT_CATEGORY -> personalAttrMap = EmailTemplateUtils.getBatchPaymentCategoryFields();
            case CALENDAR_EVENT_ADD_CATEGORY -> personalAttrMap = EmailTemplateUtils.getCalendarEventCategoryFields();
            case CALENDAR_EVENT_EDIT_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getCalendarEventUpdateCategoryFields();
            case CALENDAR_EVENT_DELETE_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getCalendarEventDeletedCategoryFields();
            case CALENDAR_EVENT_SHARE_EDIT_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getCalendarEventUpdateCategoryFields();
            case CALENDAR_EVENT_REMINDER_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getCalendarEventReminderCategoryFields();
            case CALENDAR_EVENT_SHARE_CATEGORY -> personalAttrMap = EmailTemplateUtils.getCalendarEventCategoryFields();
            case TASK_ASSIGN_CATEGORY, TASK_COMPLETED_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getTaskAssignCategoryFields();
            case PROJECT_ADD_CATEGORY -> personalAttrMap = EmailTemplateUtils.getProjectAssignCategoryFields();
            case PROJECT_ASSIGN_CATEGORY -> personalAttrMap = EmailTemplateUtils.getProjectAssignCategoryFields();
            case BACKUP_MANAGER_ASSIGN_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getProjectAssignCategoryFields();
            case PROJECT_MANAGER_ASSIGN_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getProjectAssignCategoryFields();
            case PROJECT_CLIENT_APPROVE_CATEGORY, PROJECT_CLIENT_REJECT_CATEGORY, PROJECT_CLIENT_SUBMIT_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getProjectClientApproveCategoryFields();
            case MESSAGE_CENTER_CATEGORY -> personalAttrMap = EmailTemplateUtils.getMessageCenterCategoryFields();
            case GOOGLE_CONTACT_SYNC_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getGoogleContactSynCategoryFields();
            case CLIENT_ACTIVATION_NEW_USER_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getClientActivationNewUserCategoryFields();
            case CLIENT_ACTIVATION_EXISTING_USER_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getClientActivationExistingUserCategoryFields();
            case EMPLOYEE_ACTIVATION_NEW_USER_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getEmployeeActivationNewUserCategoryFields();
            case EMPLOYEE_ACTIVATION_EXISTING_USER_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getEmployeeActivationExistingUserCategoryFields();
            case USER_ACCOUNT_CONFIRMATION_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getUserAccountConfirmationCategoryFields();
            case EMPLOYEE_ACTIVATED_BY_MANAGER_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getEmployeeActivatedByManagerCategoryFields();
            case OVERDUE_INVOICE_REMINDER_FOR_CLIENT_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getOverdueReminderCategoryForClientFields();
            case ISSUE_ADD_CATEGORY -> personalAttrMap = EmailTemplateUtils.getIssueAddAssignUpdateCategoryFields();
            case ISSUE_ASSIGN_CATEGORY -> personalAttrMap = EmailTemplateUtils.getIssueAddAssignUpdateCategoryFields();
            case ISSUE_UPDATE_CATEGORY -> personalAttrMap = EmailTemplateUtils.getIssueAddAssignUpdateCategoryFields();
            case ISSUE_DELETE_CATEGORY -> personalAttrMap = EmailTemplateUtils.getIssueDeleteCategoryFields();
            case CRM_WEB_FORM_CATEGORY -> personalAttrMap = EmailTemplateUtils.getWebFormCategoryFields();
            case MULTI_TASK_ASSIGN_CATEGORY -> personalAttrMap = EmailTemplateUtils.getMultTaskAssignCategoryFields();
            case ACTUAL_TIME_REACHED_TO_ESTIMATED -> personalAttrMap = EmailTemplateUtils.getActualTimeReachedFields();
            case CALENDAR_INVITATION_TO_GUESTS_ADD_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getCalendarInvitationAddCategoryFields();
            case CALENDAR_INVITATION_TO_GUESTS_EDIT_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getCalendarInvitationEditCategoryFields();
            case CALENDAR_INVITATION_TO_GUESTS_DELETE_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getCalendarInvitationDeleteCategoryFields();
            case SMS_TEMPLATE_CATEGORY -> personalAttrMap = EmailTemplateUtils.getSmsTemplate();
            case CREDIT_NOTE_CATEGORY -> personalAttrMap = EmailTemplateUtils.getCreditNoteCategoryFields();
            case DOC_UPLOAD_TO_TASK_CATEGORY, DOC_UPLOAD_TO_PROJECT_CATEGORY, DOC_UPLOAD_TO_ISSUE_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getDocumentUploadCategoryFields();
            case CRM_MASS_MAILING_CATEGORY -> personalAttrMap = EmailTemplateUtils.getCrmMailListCategoryFields();
            case CUSTOMER_BALANCE_CATEGORY -> personalAttrMap = EmailTemplateUtils.getCustomerBalanceFields();
            case SUPPLIER_BALANCE_CATEGORY -> personalAttrMap = EmailTemplateUtils.getSupplierBalanceFields();
            case CASE_REPLIED_CATEGORY, CASE_AUTO_RESPONSE_CATEGORY, CASE_CLOSE_NOTIFICATION_CATEGORY,
                 CASE_CLOSE_NOTIFICATION_CATEGORY_FOR_REPORTER ->
                    personalAttrMap = EmailTemplateUtils.getCaseModuleFields();
            case COURSE_SCHEDULE_CATEGORY -> personalAttrMap = EmailTemplateUtils.getCourseScheduleFields();
            case COURSE_BOOKING_CONFIRMATION_CATEGORY ->
                    personalAttrMap = EmailTemplateUtils.getCourseBookingConfirmationFields();
            case MEETING_MINUTES_NOTIFICATION ->
                    personalAttrMap = EmailTemplateUtils.getMeetingMinutesNotificationFields();
            case CATEGORY_PENALTY -> personalAttrMap = EmailTemplateUtils.getPenaltyFields();
            case CATEGORY_PROMOTION -> personalAttrMap = EmailTemplateUtils.getPromotionFields();
            case PM_CONTRACT_REMINDER -> personalAttrMap = EmailTemplateUtils.getContractReminderAttributes();
            case TASK_REMINDER -> personalAttrMap = EmailTemplateUtils.getTaskReminderAttributes();
            case PAYSLIP_APPROVED_TO_EMPLOYEE -> personalAttrMap = EmailTemplateUtils.getPayslipApprovedAttributes();
            case SMS_TEMPLATE_CUSTOMER_BALANSE ->
                    personalAttrMap = EmailTemplateUtils.getCustomerSupplierBalanceCategoryFields();
            case SMS_TEMPLATE_SUPPLIER_BALANSE ->
                    personalAttrMap = EmailTemplateUtils.getSupplierBalanceCategoryFields();
            case BILL_OF_MATERIALS_APPROVED, BILL_OF_MATERIALS_REJECTED, BILL_OF_MATERIALS_SUBMITTED ->
                    personalAttrMap = EmailTemplateUtils.getBillOfMaterialsAttributes();
            case EMPLOYEE_EVENT_CATEGORY -> {
                List<SelectItem> list = new ArrayList<>(Arrays.asList(EmailTemplateConstants.HRMS.EMPLOYEE.ATTRIBUTES));
                List<EdsCustomForm> customForms = customFormManager.list(new ListingFilterParameter());
                if (customForms != null && !customForms.isEmpty()) {
                    int id = 3;
                    for (EdsCustomForm cf : customForms) {
                        list.add(new SelectItem(id, "Custom Form " + cf.getName() + " Link", "${" + cf.getFormID().toLowerCase().replaceAll("[()]", "") + "_link}"));
                        id++;
                        list.add(new SelectItem(id, "Custom Form " + cf.getName() + " Link With Access", "${" + cf.getFormID().toLowerCase().replaceAll("[()]", "") + "_link_with_access}"));
                        id++;
                    }
                }
                List<EdsModelField> fields = this.modelFieldManager.getFieldsForWorkflowAlert(LayoutRPC.HRMS_EMPLOYEE_FORM);
                if (fields != null && fields.size() > 0) {
                    for (EdsModelField field : fields) {
                        String name = (field.getField_ID().contains("string_value") || field.getField_ID().contains("double_value") || field.getField_ID().contains("date_value") ? field.getLabel() : field.getField_ID());
                        String description = field.getField_ID() != null ? ("${" + field.getField_ID().toLowerCase() + "}") : field.getField_ID();
                        list.add(new SelectItem(field.getObjectID(), name, description));
                    }
                }
                personalAttrMap = new LinkedHashMap<>();
                for (SelectItem item : list) {
                    personalAttrMap.put(item.getName() + " -> " + item.getDescription(), item.getDescription());
                }
            }
            default -> personalAttrMap = new LinkedHashMap<>();
        }
        return personalAttrMap;
    }

    @Transactional
    public Integer[] createEmailTemplate(EmailTemplateItem[] templateItems) {
        List<Integer> templateIds = new ArrayList<>();

        for (EmailTemplateItem item : templateItems) {
            Integer templateId = createUpdateEmailTemplate(item);
            if (templateId != null) {
                templateIds.add(templateId);
            }
        }
        return templateIds.toArray(new Integer[]{});
    }

    @Transactional
    public Integer createUpdateEmailTemplate(EmailTemplateItem templateItem) {
        EdsEmailTemplate emailTemplate;
        boolean isUpdate = false;
        if (templateItem.getObjectId() != null) {
            emailTemplate = emailTemplateManager.get(templateItem.getObjectId());
            if (DEFAULT_EMAIL_TEMPLATE.equals(emailTemplate.getCompanyEmailTemplate())) {
                emailTemplate = new EdsEmailTemplate();
            } else {
                isUpdate = true;
            }
        } else {
            emailTemplate = new EdsEmailTemplate();
        }

        emailTemplate.setName(templateItem.getName());
        emailTemplate.setModule(referenceManager.get(templateItem.getModuleID()));
        emailTemplate.setTemplateCategory(templateItem.getCategoryId() != null ? referenceManager.get(templateItem.getCategoryId()) : null);
        if (templateItem.isDefault() && emailTemplate.getTemplateCategory() != null) {
            emailTemplateManager.updateDefaultTemplate(emailTemplate.getObjectID(), emailTemplate.getTemplateCategory().getObjectID());
            emailTemplate.setDefault(true);
        }
        if (templateItem.isOnlyMine() && userManager.getUser() != null) {
            emailTemplate.setUserID(userManager.getUser().getObjectID());
        }
        emailTemplate.showInMessageCenter(templateItem.showInMessageCenter());
        emailTemplate.setSubject(templateItem.getSubject());
        emailTemplate.setReplyTo(templateItem.getReplyTo());
        emailTemplate.setFromEmail(userManager.get(templateItem.getFromUserID()) != null ? userManager.get(templateItem.getFromUserID()).getEmail() : "");
        emailTemplate.setFromUserName(templateItem.getFromUserName() != null ? templateItem.getFromUserName() : "");
        emailTemplate.setSendEmail(templateItem.getTestEmail());
        emailTemplate.setMessageHTML(templateItem.getMessageHTML());
        emailTemplate.setFromUser(templateItem.getFromUserID());
        emailTemplate.setReplyTo(templateItem.getReplyTo());
        emailTemplate.setSendSummaryPdf(templateItem.isSendSummaryPdf());
        emailTemplate.setPdfTemplateId(templateItem.getPdfTemplateId());
        if (templateItem.getLocaleCode() != null) {
            emailTemplate.setLocale(new Locale(templateItem.getLocaleCode()));
        }
        if (isUpdate) {
            if (emailTemplate.getCompanyEmailTemplate() == null) {
                emailTemplate.setCompanyEmailTemplate(COMPANY_EMAIL_TEMPLATE);
            }
            emailTemplateManager.update(emailTemplate);
        } else {
            emailTemplateManager.create(emailTemplate);
        }

        if (templateItem.getAttachments() != null && templateItem.getAttachments().length > 0) {
            attachmentUtilsManager.saveAttachments(F_EMAIL_TEMPLATE, emailTemplate.getObjectID(), emailTemplate.getObjectID(), templateItem.getAttachments());
        }

        return emailTemplate.getObjectID();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem getEmailTemplate(Integer objectId) {
        EmailTemplateItem templateItem = new EmailTemplateItem();
        if (objectId != null) {
            EdsEmailTemplate emailTemplate = emailTemplateManager.get(objectId);
            templateItem.setObjectId(emailTemplate.getObjectID());
            templateItem.setName(emailTemplate.getName());
            if (emailTemplate.getModule() != null) {
                templateItem.setModuleID(emailTemplate.getModule().getObjectID());
                templateItem.setModule(referenceWfmMessageSource.localizeRef(emailTemplate.getModule()));
            }
            templateItem.setDefault(emailTemplate.isDefault());
            if (emailTemplate.getLocale() != null) {
                EdsLocale localeBylanguageCode = localeManager.getLocaleBylanguageCode(emailTemplate.getLocale().getLanguage());
                templateItem.setLanguage(new SelectItem(localeBylanguageCode.getObjectID(), localeBylanguageCode.getLanguageCode()));
            }
            templateItem
                    .setOnlyMine(emailTemplate.getUserID() != null);
            templateItem.setFromEmail((emailTemplate.getFromEmail() != null && !emailTemplate.getFromEmail().equals("")) ?
                    emailTemplate.getFromEmail() : userManager.getUser().getEmail());
            templateItem.setMessageHTML(emailTemplate.getMessageHTML());
            templateItem.setTestEmail(emailTemplate.getSendEmail());
            templateItem.setCategoryName(emailTemplate.getTemplateCategory() != null ? referenceWfmMessageSource.localizeRef(emailTemplate.getTemplateCategory()) : null);
            templateItem.setSubject(emailTemplate.getSubject());
            templateItem.setReplyTo(emailTemplate.getReplyTo());
            templateItem.setCategoryId(emailTemplate.getTemplateCategory() != null ? emailTemplate.getTemplateCategory().getObjectID() : null);
            templateItem.setSendSummaryPdf(emailTemplate.getSendSummaryPdf());
            templateItem.setPdfTemplateId(emailTemplate.getPdfTemplateId());
            Locale locale = emailTemplate.getLocale();
            String localeCode = ServerUtils.getUserLocale().getLanguage();
            String localeCountry = ServerUtils.getUserLocale().getCountry();

            if (localeCode != null && !StringUtils.isEmpty(localeCountry)) {
                localeCode = localeCode + "_" + localeCountry;
            }
            if ("en_gb".equalsIgnoreCase(localeCode)) {
                templateItem.setLocaleCode(localeCode);
            } else {
                if (locale != null) {
                    templateItem.setLocaleCode(locale.getLanguage());
                }
            }

            templateItem.setFromUserID(emailTemplate.getFromUser() != null ?
                    (userManager.get(emailTemplate.getFromUser()) != null ?
                            userManager.get(emailTemplate.getFromUser()).getObjectID() : emailTemplate.getFromUser()) : Integer.valueOf(-1));
            if (emailTemplate.getTemplateCategory() != null) {
                templateItem.setFromUserName(emailTemplate.getFromUserName() != null ? emailTemplate.getFromUserName() : "");
            } else {
                templateItem.setFromUserName(emailTemplate.getFromUser() != null ?
                        (userManager.get(emailTemplate.getFromUser()) != null ? userManager.get(emailTemplate.getFromUser()).getName() : "") : "");
            }
            templateItem.setCompanyEmailTemplate(emailTemplate.getCompanyEmailTemplate());
            List<FileResource> emailTemplateAttachments = attachmentUtilsManager.getAttachments(F_EMAIL_TEMPLATE, emailTemplate.getObjectID(), emailTemplate.getObjectID());
            FileItem[] fileItems = getEmailTemplateAttachments(emailTemplateAttachments);
            templateItem.setAttachments(fileItems);
            templateItem.setFileResources((ArrayList<FileResource>) emailTemplateAttachments);
            templateItem.showInMessageCenter(emailTemplate.showInMessageCenter());
        }
        SelectItem[] languages = localeManager.list().stream()
                .map(edsLocale -> new SelectItem(edsLocale.getObjectID(), edsLocale.getLanguageCode()))
                .toArray(SelectItem[]::new);
        templateItem.setLangugages(languages);
        List<EdsReference> references = referenceManager.listReferences(_EMAIL_TEMPLATE_MODULE);
        references.sort(Comparator.comparing(EdsReference::getName));
        templateItem.setModules(ServerUtils.getAsSelectItem(references, ServerUtils.REFERENCE));
        templateItem.setFromUsers(getCurrentOrSomeUsers());
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCompanyID(SecurityContext.getCompanyID());
        fp.setRelationType("PLACEMENT");
        templateItem.setPdfTemplates(companyPdfTemplateManager.getCompanyPDFTemplates(fp).stream()
                .map(p -> new SelectItem(p.getObjectID(), p.getName())).toList().toArray(new SelectItem[]{}));

        return templateItem;
    }

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
                    case OFFICE_365, OFFICE_365_SHARE_POINT -> {
                        fileItem.setDocumentID(fileResource.getDocumentID());
                        fileItem.setDocumentOpenID(fileResource.getDocumentOpenID());
                        fileItem.setOfficeDocumentLink(fileResource.getOfficeDownloadLink());
                    }
                    default -> fileItem.setAmazonLink(fileResource.getAmazonLink());
                }
                fileItems[i] = fileItem;
            }
        }
        return fileItems;
    }

    @Transactional
    public void deleteEmailTemplate(Integer objectId) {
        EdsEmailTemplate template = emailTemplateManager.get(objectId);
        template.setDeleted(true);
        emailTemplateManager.update(template);
        //Update default email template
        if (template.getTemplateCategory() != null) {
            Long countNonDeletedEmailTemplates = emailTemplateManager.getCountNonDeletedEmailTemplates(template.getTemplateCategory().getObjectID());
            EdsEmailTemplate defaultTemplate = emailTemplateManager.getEmailTemplateByCategory(template.getTemplateCategory().getCode());
            boolean isDefaultAnd = defaultTemplate != null && template.isDefault();
            if (countNonDeletedEmailTemplates.intValue() >= 1) {
                if (isDefaultAnd) {
                    defaultTemplate.setDefault(true);

                    //delete email template attachments
                    List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_EMAIL_TEMPLATE, objectId, objectId);
                    List<Integer> emailTemplateAttachmentIds = new ArrayList<>();
                    for (FileResource emailTemplateAttachment : attachments) {
                        emailTemplateAttachmentIds.add(emailTemplateAttachment.getObjectId());
                    }
                    commonServiceLocal.deleteFiles(emailTemplateAttachmentIds);
                }
            }
        }
    }

    public String sendTestEmail(EmailTemplateItem templateItem) {
        try {
            Number testEmailCount = (Number) messageManager.findNativeSingle("SELECT count(m.*) as total FROM message m " +
                    "WHERE (m.status='" + MessageStatusEnum.SENT + "' OR m.status='" + MessageStatusEnum.PENDING + "') AND m.is_test=true AND" +
                    " m.creationdate > ?", DateUtils.addDays(new Date(), -1));

            Integer count = testEmailCount.intValue();
            if (count != null && count < 24) {
                templateItem.setTest(true);
                messageManager.sendTestEmail(templateItem);
                return "sent";
            } else {
                return "limit_exceeded";
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
            return "failed";
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmailTemplates(String templateCategory) {
        List<EdsEmailTemplate> emailTemplates = emailTemplateManager.getEmailTemplatesByCategory(templateCategory);
        EdsEmailTemplate defaultEmailTemplate = emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(templateCategory);
        boolean isSystemTemplate = false;

        if (defaultEmailTemplate == null) {
            defaultEmailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(templateCategory);
            isSystemTemplate = true;
        }
        int i = 0;
        int size = emailTemplates.size();
        if (isSystemTemplate && defaultEmailTemplate != null) {
            size++;
        }

        SelectItem[] items = new SelectItem[size];

        if (defaultEmailTemplate != null) {
            items[i] = new SelectItem(defaultEmailTemplate.getObjectID(), defaultEmailTemplate.getName());
            i++;
        }

        for (EdsEmailTemplate emailTemplate : emailTemplates) {
            if (defaultEmailTemplate != null && !defaultEmailTemplate.equals(emailTemplate)) {
                items[i] = new SelectItem(emailTemplate.getObjectID(), emailTemplate.getName());
                i++;
            }
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SignatureItem getSignature(Integer objectID) {
        SignatureItem signature = new SignatureItem();
        if (objectID != null) {
            EdsSignature edsSignature = signatureManager.get(objectID);
            signature.setUserName(edsSignature.getUser() == null ? "" : edsSignature.getUser().getName());
            signature.setUserID(edsSignature.getUser().getObjectID());
            signature.setSignature(edsSignature.getSignature());
            signature.setShowSignatureOnTop(edsSignature.isShowSignatureOnTop() != null && edsSignature.isShowSignatureOnTop());
        }
        return signature;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<SignatureItem> getSignatureList(ListingFilterParameter fp) {
        List<EdsSignature> list = signatureManager.getSignatures(fp);
        int totalCount = list.size();
        if (fp.getLimit() > 0) {
            list = ListUtils.getSublist(list, fp.getStart(), fp.getLimit());
        }
        ArrayList<SignatureItem> signatureList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            int i = 0;
            SignatureItem[] signatures = new SignatureItem[list.size()];
            for (EdsSignature signature : list) {
                signatures[i] = new SignatureItem();
                signatures[i].setObjectID(signature.getObjectID());
                signatures[i].setUserName(signature.getUser() != null ? signature.getUser().getName() : "");
                signatures[i].setSignature(signature.getSignature() != null && !"".equals(signature.getSignature()) ? signature.getSignature() : "");
                signatureList.add(signatures[i]);
                i++;
            }
        }
        return new ListResult<>(signatureList, totalCount);
    }

    @Transactional
    public Integer saveSignature(SignatureItem signature) {
        EdsSignature edsSignature = signature.getObjectID() != null ? signatureManager.get(signature.getObjectID()) : new EdsSignature();
        EdsUser edsUser = userManager.getUser();
        if (edsSignature == null) {
            edsSignature = new EdsSignature();
        }
        if (edsUser.hasRole(ADMIN_CODE)) {
            edsSignature.setUser(userManager.getUserByUserID(signature.getUserID()));
        } else {
            edsSignature.setUser(edsUser);
        }
        edsSignature.setSignature(signature.getSignature());
        edsSignature.setShowSignatureOnTop(signature.isShowSignatureOnTop());
        signatureManager.createOrUpdate(edsSignature);
        return edsSignature.getObjectID();
    }

    @Transactional
    public void deleteSignature(Integer objectId) {
        EdsSignature signature = signatureManager.get(objectId);
        signature.setDeleted(true);
    }

    @Transactional
    public Boolean saveCredentials(CredentialsItem item) {
        EdsUser user = referenceManager.getUser();
        String oldPassword = user.getPassword();

        List<UserCompanyDTO> companyList = globalAuthJdbcSpringManager.getAuthInfoByUsernameAndPassword(EdsContextParams.getHostname(), user.getUserName(), item.getCurrentPass());
        if ((item.getCurrentPass() != null && !"".equals(item.getCurrentPass())) && (companyList == null || companyList.size() == 0)) {//if current password is wrong return false
            EdsUserSession userSession = userSessionManager.getUserSession(ServerSecurityContext.getInstance().getSessionId());
            if ((userSession != null && !userSession.isOpenIDUser())) {
                return false;
            }
        }
        String primaryEmail = item.getEmail();
        if (primaryEmail != null) {
            primaryEmail = primaryEmail.toLowerCase();
        }
        if (user.isClientContact()) {
            EdsClientContact clientContact = user.getClientContact();
            if (clientContact != null && clientContact.getCrmContact() != null) {
                clientContact.getCrmContact().setPrimaryEmail(primaryEmail);
                contactItemParamsUpdate(clientContact.getCrmContact());
            }
        } else if (user.isEmployee()) {
            EdsEmployee employee = user.getEmployee();
            if (employee != null && employee.getContact() != null) {
                employee.getContact().setPrimaryEmail(primaryEmail);
                contactItemParamsUpdate(employee.getContact());
            }
        }
        user.setEmail(primaryEmail);
        //save user internationalisation
        if (item.getInternationalization() != null && !"".equals(item.getInternationalization())) {
            saveLanguageForUser(item.getInternationalization(), false);
        }
        //save user timezone
        if (item.getTimeZoneId() != null && !"".equals(item.getTimeZoneId())) {
            user.setCountryZone(zoneManager.getCountryZone(item.getTimeZoneId()));
        }

        boolean encrypt = false;
        if (item.getNewPass() != null && !"".equals(item.getNewPass())) {
            user.setPassword(item.getNewPass());
            encrypt = true;
        }

        if (item.getStartPage() != null && !"".equals(item.getStartPage())) {
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
            userSettings.setStartPage(item.getStartPage());
            userEmailSettingsManager.update(userSettings);
        }
        boolean passwordChanged = item.getCurrentPass() != null && !item.getCurrentPass().isEmpty() && !item.getCurrentPass().equals(item.getNewPass());
        if (passwordChanged){
            user.setOldPassword(oldPassword);
            sessionService.expireMobileUserSessionsAcrossCompanies(user.getUserName());
        }
        userManager.saveUserAuthenticationData(user, user.getCompany().getObjectID(), encrypt, passwordChanged);
        return true;
    }

    private void contactItemParamsUpdate(EdsCrmContact contact) {
        List<EdsCrmContactItemParams> emails = contact.getItemParams(EdsCrmContactItemParams.EMAIL);
        boolean emailFoundInExistings = false;
        if (emails != null && emails.size() > 0) {
            for (EdsCrmContactItemParams contactItemParams : emails) {
                emailFoundInExistings = contactItemParams.getValue() != null && contactItemParams.getValue().equalsIgnoreCase(contact.getPrimaryEmail());
            }
        }
        if (!emailFoundInExistings) {
            EdsCrmContactItemParams email = new EdsCrmContactItemParams(EdsCrmContactItemParams.EMAIL);
            email.setContact(contact);
            email.setValue(contact.getPrimaryEmail());
            email.setRelation(EdsCrmContactItemParams.HOME);
            contactItemParamsManager.create(email);
            contact.getItemParams().add(email);
        }
        crmContactManager.update(crmContactManager.get(contact.getObjectID()), true);
    }

    public String getImageUrl(Integer id) {
        return commonService.getImageUrl(id);
    }

    public void saveCustomFields(Integer companyID, CompanyCustomFieldItem itemCompany, boolean isItemTableField) {
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }

        EdsCompanyCustomFieldsSettings edsCompanyCustomFieldsSettings = null;

        if (itemCompany.getUiType().equals(UI_TYPE_APPROVAL_PROCESS)) {
            EdsModel model = this.modelManager.getCustomFormModel(itemCompany.getEntityName());

            if (model != null) {
                EdsCustomFormAttributes customFormAttributes = customFormAttributeManager.findByFieldTypeAndFieldID(itemCompany.getColumnCode(), itemCompany.getUiType());
                customFormAttributes.setLabel(itemCompany.getFieldName());

                EdsModelField modelField = modelFieldManager.getByFieldID(model.getFormID(), itemCompany.getColumnCode());
                modelField.setLabel(itemCompany.getFieldName());
                modelField.setFsection(itemCompany.getSection());

                if (modelField.getObjectID() != null) {
                    modelFieldManager.update(modelField);
                } else {
                    modelFieldManager.create(modelField);
                }
            }
            if (itemCompany.getObjectId() != null) {
                edsCompanyCustomFieldsSettings = companyCFManager.get(itemCompany.getObjectId());
                if (edsCompanyCustomFieldsSettings != null) {
                    setCompanyCustomFieldSettingValues(edsCompanyCustomFieldsSettings, itemCompany);
                }
            }
        } else {
            if (itemCompany.getObjectId() != null) {
                edsCompanyCustomFieldsSettings = companyCFManager.get(itemCompany.getObjectId());

                if ("FileUploadWidget".equals(itemCompany.getUiType())) { // Custom form 2 section rename
                    List<EdsModel> modelForms = modelManager.getModelList(itemCompany.getEntityName());
                    for (EdsModel modelForm : modelForms) {
                        EdsCustomFormSection customizeFormSection = customFormSectionManager.getCustomizeFormSection(modelForm.getFormID(), edsCompanyCustomFieldsSettings.getFieldName());
                        if (customizeFormSection != null) {
                            customizeFormSection.setSection(itemCompany.getFieldName());
                        }
                        List<EdsModelFieldCustom> edsModelFieldCustoms = modelFieldManager.getModelFieldsBySection(modelForm.getFormID(), edsCompanyCustomFieldsSettings.getFieldName());
                        if (edsModelFieldCustoms != null && !edsModelFieldCustoms.isEmpty()) {
                            edsModelFieldCustoms.forEach(edsModelFieldCustom -> {
                                if (edsModelFieldCustom != null) {
                                    edsModelFieldCustom.setFsection(itemCompany.getFieldName());
                                    edsModelFieldCustom.setLabel(itemCompany.getFieldName());
                                }
                            });
                        }
                    }
                }
            }

            if (edsCompanyCustomFieldsSettings == null) {
                edsCompanyCustomFieldsSettings = new EdsCompanyCustomFieldsSettings();
            }

            EdsUser loggedUser = userManager.getUser();
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(loggedUser);
            EdsCustomFormLocalization edsCustomFormLocalization = null;
            if (edsCompanyCustomFieldsSettings.getCustomFormlocalization() != null) {
                edsCustomFormLocalization = edsCompanyCustomFieldsSettings.getCustomFormlocalization();
                edsCustomFormLocalization.setDefaultName(itemCompany.getFieldName());
            } else {
                edsCustomFormLocalization = new EdsCustomFormLocalization();
                edsCustomFormLocalization.setDefaultName(itemCompany.getFieldName());
                edsCustomFormLocalization.setEnglishName(itemCompany.getFieldName());
                edsCustomFormLocalization.setArabicName(itemCompany.getFieldName());
                edsCustomFormLocalization.setRussianName(itemCompany.getFieldName());
                edsCustomFormLocalization.setUzbekName(itemCompany.getFieldName());
                edsCustomFormLocalization.setType(isItemTableField ? Constants.PREDEFINED : Constants.FIELD);
                edsCustomFormLocalization.setSection(itemCompany.getSection());
                edsCustomFormLocalization.setFormId(itemCompany.getForm());

                if (isItemTableField && itemCompany.getParentFieldName() != null) {
                    EdsCustomFormLocalization parentFieldLocalization = customFormLocalizationManager.getByName(itemCompany.getParentFieldName(), edsCustomFormLocalization.getFormId());
                    edsCustomFormLocalization.setParent(parentFieldLocalization);
                }
                customFormLocalizationManager.create(edsCustomFormLocalization);
            }

            if ((UI_TYPE_DROPDOWN.equals(itemCompany.getUiType()) || UI_TYPE_RADIOBUTTON.equals(itemCompany.getUiType()) || UI_TYPE_CHECKBOX.equals(itemCompany.getUiType())) && itemCompany.getPredefinedValues() != null) {
                /*customFormLocalizationManager.deleteChildByParentId(edsCustomFormLocalization.getObjectID());*/
                List<Integer> ids = new ArrayList<>();
                if (itemCompany.getPredefinedValues() != null) {
                    for (String value : itemCompany.getPredefinedValues()) {
                        EdsCustomFormLocalization item;
                        if (itemCompany.getObjectId() != null && edsCompanyCustomFieldsSettings.getCustomFormlocalization() != null) {
                            item = customFormLocalizationManager.getByNameAndParent(value, edsCompanyCustomFieldsSettings.getCustomFormlocalization().getObjectID());
                            if (item == null) {
                                item = new EdsCustomFormLocalization();
                                item.setDefaultName(value);
                                item.setEnglishName(value);
                                item.setArabicName(value);
                                item.setRussianName(value);
                                item.setUzbekName(value);
                                item.setType(isItemTableField ? Constants.ITEM_FIELD_PREDEFINED : Constants.PREDEFINED);
                                item.setSection(edsCustomFormLocalization.getSection());
                                item.setFormId(edsCustomFormLocalization.getFormId());
                                item.setParent(edsCustomFormLocalization);
                                customFormLocalizationManager.create(item);
                            }
                        } else {
                            item = new EdsCustomFormLocalization();
                            item.setDefaultName(value);
                            item.setEnglishName(value);
                            item.setArabicName(value);
                            item.setRussianName(value);
                            item.setUzbekName(value);
                            item.setType(isItemTableField ? Constants.ITEM_FIELD_PREDEFINED : Constants.PREDEFINED);
                            item.setSection(edsCustomFormLocalization.getSection());
                            item.setFormId(edsCustomFormLocalization.getFormId());
                            item.setParent(edsCustomFormLocalization);
                            customFormLocalizationManager.create(item);
                        }
                        ids.add(item.getObjectID());
                    }
                    if (ids != null) {
                        customFormLocalizationManager.deleteChildrenExceptGivenIds(ids, edsCustomFormLocalization.getObjectID());
                    }
                }
            }
            customFormLocalizationManager.update(edsCustomFormLocalization);

            edsCompanyCustomFieldsSettings.setCustomFormlocalization(edsCustomFormLocalization);
            boolean requiredChanged = (edsCompanyCustomFieldsSettings.getRequired() == null && itemCompany.isRequired())
                    || !edsCompanyCustomFieldsSettings.getRequired().equals(itemCompany.isRequired());
            setCompanyCustomFieldSettingValues(edsCompanyCustomFieldsSettings, itemCompany);

            if (Constants.QUICK_ADD_FORMS.contains(itemCompany.getEntityName()) && requiredChanged) {
                addOrRemoveCFFromQuickAdd(QuickAddSettingsForm.getByFormId(itemCompany.getEntityName()), itemCompany.getFieldName(), itemCompany.getColumnCode(), itemCompany.isRequired());
            }

        }
        if (isItemTableField && itemCompany.isRequired()) {
            itemTableSettingsServiceLocal.updateItemTableSettings(itemCompany.getColumnCode(), itemCompany.getFieldName(), itemCompany.getEntityName());
        }
    }

    @Override
    public void addOrRemoveCFFromQuickAdd(QuickAddSettingsForm form, String name, String columnCode, boolean required) {
        if (form == null || columnCode == null) {
            return;
        }
        EdsQuickAddSettings settings = quickAddSettingsManager.getByForm(form);
        if (settings == null || StringUtils.isBlank(settings.getSettingsJSONData())) {
            return;
        }
        Gson gson = new Gson();
        List<QuickAddColumnConfigs> columns = new ArrayList<>(Arrays.asList(gson.fromJson(settings.getSettingsJSONData(), QuickAddColumnConfigs[].class)));
        if (required) {
            columns.add(new QuickAddColumnConfigs(columnCode, name, true, true, columns.get(columns.size() - 1).getOrder() + 1));
        } else {
            for (QuickAddColumnConfigs column : columns) {
                if (column.getCode().equals(columnCode)) {
                    columns.remove(column);
                    break;
                }
            }
        }
        settings.setSettingsJSONData(gson.toJson(columns));
        quickAddSettingsManager.update(settings);
    }

    private void setCompanyCustomFieldSettingValues(EdsCompanyCustomFieldsSettings edsCompanyCustomFieldsSettings, CompanyCustomFieldItem itemCompany) {

        edsCompanyCustomFieldsSettings.setColumnCode(itemCompany.getColumnCode());
        edsCompanyCustomFieldsSettings.setEntityName(itemCompany.getEntityName());
        edsCompanyCustomFieldsSettings.setEntityCategoryName(itemCompany.getEntityCategoryName());
        edsCompanyCustomFieldsSettings.setEntityCategoryAlias(itemCompany.getEntityCategoryAlias());
        edsCompanyCustomFieldsSettings.setFieldName(itemCompany.getFieldName());
        edsCompanyCustomFieldsSettings.setAliasName(itemCompany.getAliasName());
        edsCompanyCustomFieldsSettings.setDataType(itemCompany.getDataType());
        edsCompanyCustomFieldsSettings.setUiType(itemCompany.getUiType());
        edsCompanyCustomFieldsSettings.setMinChar(itemCompany.getMinChar());
        edsCompanyCustomFieldsSettings.setRelationFieldId(itemCompany.getRelationFieldId());
        edsCompanyCustomFieldsSettings.setRelationFieldValues(itemCompany.getRelationFieldValues());
        edsCompanyCustomFieldsSettings.setQuizFormScoreValues(itemCompany.getQuizFormScoreValues());
        edsCompanyCustomFieldsSettings.setNumberMinValue(itemCompany.getNumberMinValue());
        if (UI_TYPE_TEXTBOX.equals(itemCompany.getUiType()) || UI_TYPE_TEXTAREA.equals(itemCompany.getUiType()) || UI_TYPE_DATEPICKER.equals(itemCompany.getUiType())) {
            edsCompanyCustomFieldsSettings.setPredefinedValues(null);
            edsCompanyCustomFieldsSettings.setPredefinedValuesWithSorting(null);
        } else {
            edsCompanyCustomFieldsSettings.setPredefinedValues(itemCompany.getPredefinedValues());
            edsCompanyCustomFieldsSettings.setPredefinedValuesWithSorting(itemCompany.getPredefinedValuesWithSorting());
        }
        edsCompanyCustomFieldsSettings.setShowInListing(itemCompany.isShowInListing());
        edsCompanyCustomFieldsSettings.setClickable(itemCompany.isClickable());
        edsCompanyCustomFieldsSettings.setShowInFilterGrouping(itemCompany.isShowInFilterGrouping());
        edsCompanyCustomFieldsSettings.setFacetable(itemCompany.isFacetable());
        edsCompanyCustomFieldsSettings.setRequired(itemCompany.isRequired());
        edsCompanyCustomFieldsSettings.setDisabled(itemCompany.isDisabled());
        edsCompanyCustomFieldsSettings.setAddTab(itemCompany.isAddTab());
        edsCompanyCustomFieldsSettings.setSeeOwnPermission(itemCompany.isSeeOwnPermission());
        edsCompanyCustomFieldsSettings.setQuery(itemCompany.getQuery());
        edsCompanyCustomFieldsSettings.setLookUpType(itemCompany.getLookUpTypeEnum());
        edsCompanyCustomFieldsSettings.setPrefix(itemCompany.getPrefix());
        edsCompanyCustomFieldsSettings.setActive(itemCompany.isActive());
        edsCompanyCustomFieldsSettings.setScale(itemCompany.getScale());
        edsCompanyCustomFieldsSettings.setMinHeight(itemCompany.getMinHeight());
        edsCompanyCustomFieldsSettings.setUseInPermission(itemCompany.isUseInPermission());
        if (itemCompany.getReferenceItem() != null) {
            EdsReference edsReference = referenceManager.getReference(itemCompany.getReferenceItem().getId());
            edsCompanyCustomFieldsSettings.setReference(edsReference);
        }
        if (itemCompany.getEntityType() != null) {
            edsCompanyCustomFieldsSettings.setEntityType(entityTypeManager.get(itemCompany.getEntityType().getId()));
        } else {
            edsCompanyCustomFieldsSettings.setEntityType(null);
        }
        if (edsCompanyCustomFieldsSettings.getObjectID() == null) {
            edsCompanyCustomFieldsSettings.setRelationship(itemCompany.getRelationship());
        }
        //audit info
        EdsAuditInfo info = edsCompanyCustomFieldsSettings.getAuditInfo();
        EdsUser currentUser = companyCFManager.getUser();
        if (info != null) {
            //created by
            if (info.getCreatedBy() == null) {
                if (currentUser != null) {
                    info.setCreatedBy(currentUser);
                }
            }
            //creation date
            if (info.getCreationDate() == null) {
                info.setCreationDate(new Date());
            }
            //last updated date
            info.setModificationDate(new Date());
            //last updated by
            if (currentUser != null) {
                info.setModifiedBy(currentUser);
            }
        } else {
            info = new EdsAuditInfo();
            //created by
            if (info.getCreatedBy() == null) {
                if (currentUser != null) {
                    info.setCreatedBy(currentUser);
                }
            }
            //creation date
            if (info.getCreationDate() == null) {
                info.setCreationDate(new Date());
            }
            //last update date
            info.setModificationDate(new Date());
            //last updated by
            if (currentUser != null) {
                info.setModifiedBy(currentUser);
            }
            edsCompanyCustomFieldsSettings.setAuditInfo(info);
        }

        if (edsCompanyCustomFieldsSettings.getObjectID() != null) {
            companyCFManager.update(edsCompanyCustomFieldsSettings);
            baseEventPostProcessor.registerEvent(CustomFieldEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsCompanyCustomFieldsSettings, userManager.getUser());
            updateModelField(edsCompanyCustomFieldsSettings, itemCompany.getDefaultValue());
        } else {
            companyCFManager.create(edsCompanyCustomFieldsSettings);
            baseEventPostProcessor.registerEvent(CustomFieldEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsCompanyCustomFieldsSettings, userManager.getUser());
            createModelField(edsCompanyCustomFieldsSettings, itemCompany.getSection());
        }
        edsCompanyCustomFieldsSettings.getAllowedRoles().clear();
        if (itemCompany.getAllowedRoles() != null && !itemCompany.getAllowedRoles().isEmpty()) {
            for (Integer roleID : itemCompany.getAllowedRoles()) {
                if (roleID != null) {
                    edsCompanyCustomFieldsSettings.getAllowedRoles().add(roleManager.get(roleID));
                }
            }
        }
        edsCompanyCustomFieldsSettings.getEditFieldRoles().clear();
        if (itemCompany.getRoleEdit() != null && !itemCompany.getRoleEdit().isEmpty()) {
            for (Integer roleID : itemCompany.getRoleEdit()) {
                if (roleID != null) {
                    edsCompanyCustomFieldsSettings.getEditFieldRoles().add(roleManager.get(roleID));
                }
            }
        }
        if (itemCompany.getObjectId() == null && "File Upload".equals(itemCompany.getDataType())) {
            commonServiceLocal.createCustomFieldFolder(edsCompanyCustomFieldsSettings.getObjectID());
        }
        if (ViewName.ProductCategory.name().equals(edsCompanyCustomFieldsSettings.getEntityName()) && edsCompanyCustomFieldsSettings.getRelationship() != null) {
            EdsProductCategory productCategory = productCategoryManager.get(edsCompanyCustomFieldsSettings.getRelationship());
            productCategory.setLastUpdateDate(new Date());
            productCategoryManager.update(productCategory);
        }
        if (itemCompany.getCustomLogicField() != null && itemCompany.getCustomLogicValue() != null) {
            EdsCompanyCustomFieldsSettings cf = companyCFManager.get(itemCompany.getCustomLogicField().getId());
            edsCompanyCustomFieldsSettings.setCustomLogicField(cf);
            edsCompanyCustomFieldsSettings.setCustomLogicValue(itemCompany.getCustomLogicValue());
        } else {
            edsCompanyCustomFieldsSettings.setCustomLogicValue(null);
            edsCompanyCustomFieldsSettings.setCustomLogicField(null);
        }
        companyCFManager.update(edsCompanyCustomFieldsSettings);
    }

    private void createModelField(EdsCompanyCustomFieldsSettings customFieldsSettings, String defaultSection) {
        String viewName = customFieldsSettings.getEntityName();
        if (("OnboardingStep".equals(viewName) || ViewName.CustomFormItems.name().equals(viewName))
                && (customFieldsSettings.getEntityCategoryName() != null && customFieldsSettings.getEntityCategoryName().length() > 0)) {
            viewName = customFieldsSettings.getEntityCategoryName();
        }
        List<EdsModel> modelForms = modelManager.getModelList(viewName);

        for (EdsModel modelForm : modelForms) {

            if (LayoutRPC.PROJECT_SUMMARY_FORM.equals(modelForm.getFormID()) || LayoutRPC.TASK_SUMMARY_FORM.equals(modelForm.getFormID())) {
                continue;
            }

            if (modelForm != null) {
                Integer sorder = modelFieldManager.getMaxSortOrder(modelForm.getFormID());
                if (sorder == null) {
                    sorder = 0;
                }
                EdsModelFieldCustom modelFieldForCustomField = new EdsModelFieldCustom();
                modelFieldForCustomField.setForm_ID(modelForm.getFormID());
                modelFieldForCustomField.setField_ID(customFieldsSettings.getColumnCode());
//                modelFieldForCustomField.setSystemMandatory(customFieldsSettings.getRequired() != null && customFieldsSettings.getRequired());
                modelFieldForCustomField.setMandatory(customFieldsSettings.getRequired() != null && customFieldsSettings.getRequired());
                modelFieldForCustomField.setCustomField(true);


                if ("FileUploadWidget".equals(customFieldsSettings.getUiType())) {
                    EdsCustomFormSection section = customFormSectionManager.getSectionByName(modelForm.getFormID(), customFieldsSettings.getFieldName());
                    if (section == null) {
                        createCustomFormSection(modelForm.getFormID(), customFieldsSettings.getFieldName());
                    }
                    modelFieldForCustomField.setFsection(customFieldsSettings.getFieldName());

                } else {
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(defaultSection)) {
                        modelFieldForCustomField.setFsection(defaultSection);
                    } else {
                        EdsCustomFormSection customFormSection = customFormSectionManager.getCustomizeFormSection(modelForm.getFormID(), CustomFormConstants.ADDITIONAL_INFORMATION);
                        if (customFormSection == null) {
                            createCustomFormSection(modelForm.getFormID(), CustomFormConstants.ADDITIONAL_INFORMATION);
                        }

                        modelFieldForCustomField.setFsection(CustomFormConstants.ADDITIONAL_INFORMATION);
                    }
                }

//                modelFieldForCustomField.setSorder(++sorder);
                modelFieldForCustomField.setForder(++sorder);
                modelFieldForCustomField.setWidget(customFieldsSettings.getUiType());
                modelFieldForCustomField.setType(customFieldsSettings.getDataType());
                modelFieldForCustomField.setLabel(customFieldsSettings.getFieldName());
                modelFieldForCustomField.setNoLabelFor("FileUploadWidget".equals(customFieldsSettings.getUiType()) ? "addForm,editForm,viewForm" : null);
                modelFieldForCustomField.setUsableByWorkflow(validateUITypeForWorkflow(customFieldsSettings));
                modelFieldForCustomField.setReference(customFieldsSettings.getReferenceEntity());
                modelFieldForCustomField.setCustomFormLocalization(customFieldsSettings.getCustomFormlocalization());
                if (customFieldsSettings.getPredefinedValues() != null) {
                    modelFieldForCustomField.setSource(String.join("-:-", customFieldsSettings.getPredefinedValues()));
                }
                modelFieldManager.createOrUpdate(modelFieldForCustomField);
            }
        }
    }

    private void createCustomFormSection(String formID, String section) {
        Integer sorder = customFormSectionManager.getSorder(formID);

        EdsCustomFormSection customFormSection = new EdsCustomFormSection();
        customFormSection.setActive(true);
        customFormSection.setCustom(false);
        customFormSection.setSorder(sorder != null ? sorder + 1 : 0);
        customFormSection.setForm_ID(formID);
        customFormSection.setSection(section);
        customFormSectionManager.createOrUpdate(customFormSection);
    }

    private boolean validateUITypeForWorkflow(EdsCompanyCustomFieldsSettings customFieldsSettings) {
        String uiType = customFieldsSettings.getUiType();
        return ("TextBox".equals(uiType) || "RadioButton".equals(uiType) || "CheckBox".equals(uiType) || "DropDown".equals(uiType) || Constants.UI_TYPE_DATEPICKER.equals(uiType) || "FileUploadWidget".equals(uiType) || "FileUploadItem".equals(uiType) || "DateTime".equals(uiType) || Constants.UI_TYPE_LOOKUP.equals(uiType) || Constants.UI_TYPE_MULTI_LOOKUP.equals(uiType) || Constants.TYPE_ENTITY_LOOKUP.equals(uiType) || Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(uiType) || "TextArea".equals(uiType));
    }

    private void updateModelField(EdsCompanyCustomFieldsSettings customFieldsSettings, String defaultValue) {

        String viewName = customFieldsSettings.getEntityName();

        if (("OnboardingStep".equals(viewName) || ViewName.CustomFormItems.name().equals(viewName))
                && (customFieldsSettings.getEntityCategoryName() != null && customFieldsSettings.getEntityCategoryName().length() > 0)) {

            viewName = customFieldsSettings.getEntityCategoryName();
        }
        List<EdsModel> modelForms = modelManager.getModelList(viewName);

        for (EdsModel modelForm : modelForms) {

            if (LayoutRPC.PROJECT_SUMMARY_FORM.equals(modelForm.getFormID()) || LayoutRPC.TASK_SUMMARY_FORM.equals(modelForm.getFormID())) {
                continue;
            }

            if (modelForm instanceof EdsModelCustom) {
                EdsModelField field = modelFieldManager.getByFieldID(modelForm.getFormID(), customFieldsSettings.getColumnCode());
                if (field != null) {
                    field.setDefaultValue(defaultValue);
                    field.setMandatory(customFieldsSettings.getRequired() != null && customFieldsSettings.getRequired());
                    field.setLabel(customFieldsSettings.getFieldName());
                    field.setWidget(customFieldsSettings.getUiType());
                    field.setType(customFieldsSettings.getDataType());
                    field.setNoLabelFor("FileUploadWidget".equals(customFieldsSettings.getUiType()) ? "addForm,editForm,viewForm" : null);
                    field.setUsableByWorkflow(validateUITypeForWorkflow(customFieldsSettings));
                    field.setSource("DropDown".equals(customFieldsSettings.getUiType()) || "RadioButton".equals(customFieldsSettings.getUiType()) || "CheckBox".equals(customFieldsSettings.getUiType()) ? String.join("-:-", customFieldsSettings.getPredefinedValues()) : null);
                    field.setReference(customFieldsSettings.getReferenceEntity());
                    modelFieldManager.update(field);
                }
            }
        }
    }

    private void removeModelField(EdsCompanyCustomFieldsSettings customFieldsSettings) {
        boolean isCustomView = "OnboardingStep".equals(customFieldsSettings.getEntityName()) || ViewName.CustomFormItems.name().equals(customFieldsSettings.getEntityName());
        String viewName = isCustomView && customFieldsSettings.getEntityCategoryName() != null ?
                customFieldsSettings.getEntityCategoryName() : customFieldsSettings.getEntityName();

        List<EdsModel> modelForms = modelManager.getModelList(viewName);

        for (EdsModel modelForm : modelForms) {

            if (LayoutRPC.PROJECT_SUMMARY_FORM.equals(modelForm.getFormID()) || LayoutRPC.TASK_SUMMARY_FORM.equals(modelForm.getFormID())) {
                continue;
            }

            if (modelForm instanceof EdsModelCustom) {
                EdsModelField field = modelFieldManager.getByFieldID(modelForm.getFormID(), customFieldsSettings.getColumnCode());
                if (field != null) {
                    allInOneServiceLocal.removeCFsFromWorkflows(field);
                    field.setDeleted(true);
                    modelFieldManager.update(field);
                }
            }
        }
    }

    private void removePdfTableColumns(EdsCompanyCustomFieldsSettings customFieldsSettings) {
        PdfTemplateTypeEnum typeEnum = null;
        if (ViewName.SaleInvoiceItem.name().equals(customFieldsSettings.getEntityName())) {
            typeEnum = PdfTemplateTypeEnum.SALES_INVOICE;
        } else if (ViewName.SaleQuoteItem.name().equals(customFieldsSettings.getEntityName())) {
            typeEnum = PdfTemplateTypeEnum.SALES_QUOTE;
        } else if (ViewName.PurchaseInvoiceItem.name().equals(customFieldsSettings.getEntityName())) {
            typeEnum = PdfTemplateTypeEnum.PURCHASE_INVOICE;
        } else if (ViewName.PurchaseOrderItem.name().equals(customFieldsSettings.getEntityName())) {
            typeEnum = PdfTemplateTypeEnum.PURCHASE_ORDER;
        } else if (ViewName.ExpenseReportItem.name().equals(customFieldsSettings.getEntityName())) {
            typeEnum = PdfTemplateTypeEnum.EXPENSE_REPORT;
        }
        if (typeEnum != null) {
            pdfTemplateTableSettingsManager.deleteByTypeAndColumnCode(typeEnum, customFieldsSettings.getFieldName());
        }
    }

    @Override
    public void saveCustomFieldValidations(Integer companyID, CompanyCustomFieldItem item) {
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }

        if (item.getObjectId() != null) {
            EdsCompanyCustomFieldsSettings customFieldsSetting = companyCFManager.get(item.getObjectId());
            companyCFManager.deleteCustomFieldValidations(customFieldsSetting.getObjectID());

            if (item.getValidations() != null && item.getValidations().length > 0) {
                ArrayList<EdsCustomFieldValidation> validations = new ArrayList<>();
                for (CustomFieldSettingItem validation : item.getValidations()) {
                    EdsCustomFieldValidation v = new EdsCustomFieldValidation();
                    v.setCustomfield(customFieldsSetting);

                    if (validation.getJoinedFieldID() != null) {
                        v.setJoinedField(companyCFManager.get(validation.getJoinedFieldID()));
                    }
                    v.setValidationCodeID(validation.getValidationCodeID());
                    v.setRegexCode(validation.getRegex());
                    validations.add(v);
                }

                customFieldsSetting.getValidations().addAll(validations);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<Integer, String[]> getExistingCustomFields(Integer companyID, String entityName, Integer relationship) {
        return getExistingCustomFields(companyID, entityName, null, relationship, null);
    }

    @Override
    public HashMap<Integer, String[]> getExistingCustomFields(Integer companyID, String entityName, String entityCategoryName, Integer relationship, Integer objectID) {

        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }
        List<EdsCompanyCustomFieldsSettings> companyCFSText = companyCFManager.getCompanyCustomFields(entityName, Constants.DATA_TYPE_TEXT, entityCategoryName, relationship, objectID, null, Boolean.TRUE);
        List<EdsCompanyCustomFieldsSettings> companyCFSNumber = companyCFManager.getCompanyCustomFields(entityName, Constants.DATA_TYPE_NUMBER, entityCategoryName, relationship, objectID, null, Boolean.TRUE);
        List<EdsCompanyCustomFieldsSettings> companyCFSDate = companyCFManager.getCompanyCustomFields(entityName, Constants.DATA_TYPE_DATE, entityCategoryName, relationship, objectID, null, Boolean.TRUE);
        List<EdsCompanyCustomFieldsSettings> companyCFSFileUpload = companyCFManager.getCompanyCustomFields(entityName, Constants.DATA_TYPE_FILE_UPLOAD, entityCategoryName, relationship, objectID, null, Boolean.TRUE);
        List<EdsCompanyCustomFieldsSettings> companyCFSProfileImage = companyCFManager.getCompanyCustomFields(entityName, Constants.DATA_TYPE_PROFILE_IMAGE, entityCategoryName, relationship, objectID, null, Boolean.TRUE);
        String[] cfTextItems = new String[companyCFSText.size()];
        String[] cfNumberItems = new String[companyCFSNumber.size() + companyCFSFileUpload.size() + companyCFSProfileImage.size()];
        String[] cfDateItems = new String[companyCFSDate.size()];
        HashMap<Integer, String[]> resultMap = new HashMap<>();
        int i = 0;
        for (EdsCompanyCustomFieldsSettings item : companyCFSText) {
            cfTextItems[i] = item.getColumnCode();
            i++;
        }
        i = 0;
        for (EdsCompanyCustomFieldsSettings item : companyCFSNumber) {
            cfNumberItems[i] = item.getColumnCode();
            i++;
        }
        for (EdsCompanyCustomFieldsSettings item : companyCFSFileUpload) {
            cfNumberItems[i] = item.getColumnCode();
            i++;
        }
        for (EdsCompanyCustomFieldsSettings item : companyCFSProfileImage) {
            cfNumberItems[i] = item.getColumnCode();
            i++;
        }
        i = 0;
        for (EdsCompanyCustomFieldsSettings item : companyCFSDate) {
            cfDateItems[i] = item.getColumnCode();
            i++;
        }
        resultMap.put(0, cfTextItems);
        resultMap.put(1, cfNumberItems);
        resultMap.put(2, cfDateItems);
        return resultMap;
    }

    public void saveCompanyEmailNotifications(HashMap<String, HashSet<SelectItem>> selectedItems, boolean isApplyExistingUsers) {
        for (Map.Entry<String, HashSet<SelectItem>> entry : selectedItems.entrySet()) {
            for (SelectItem s : entry.getValue()) {
                String notificationName = s.getName();
                List<EdsCompanyEmailNotificationSettings> companyEmailNotificationSettings =
                        companyEmailNotificationSettingsManager.getCompanyEmailNotification(notificationName);
                for (EdsCompanyEmailNotificationSettings cENs : companyEmailNotificationSettings) {
                    if (cENs != null && !cENs.isForClient()) {
                        cENs.setEnabled(s.isNewItem());
                        companyEmailNotificationSettingsManager.update(cENs);
                    }
                }
                //apply existing users;
                if (isApplyExistingUsers) {
                    List<EdsUser> existingUsers = userManager.getUsers();
                    for (EdsUser existUser : existingUsers) {
                        if (existUser.isEmployee()) {
                            EdsEmailNotificationSettings existNotificationSettings =
                                    emailNotificationSettingsManager.getUserEmailNotification(existUser.getObjectID(), notificationName);
                            if (existNotificationSettings != null) {
                                existNotificationSettings.setEnabled(s.isNewItem());
                                emailNotificationSettingsManager.update(existNotificationSettings);
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveUserEmailNotifications(HashMap<String, HashSet<SelectItem>> newEventSelectItems) {
        EdsUser user = userManager.getUser();
        EdsEmailNotificationSettings emailNotificationSettings;
        for (Map.Entry<String, HashSet<SelectItem>> entry : newEventSelectItems.entrySet()) {
            String category = entry.getKey();
            for (SelectItem s : entry.getValue()) {
                String notificationType = s.getName();
                emailNotificationSettings = emailNotificationSettingsManager.getUserEmailNotification(user.getObjectID(), notificationType);
                if (emailNotificationSettings != null) {
                    emailNotificationSettings.setEnabled(s.isNewItem());
                    emailNotificationSettingsManager.update(emailNotificationSettings);
                } else {
                    emailNotificationSettings = new EdsEmailNotificationSettings();
                    emailNotificationSettings.setUser(user);
                    emailNotificationSettings.setNotificationType(notificationType);
                    emailNotificationSettings.setEnabled(s.isNewItem());
                    emailNotificationSettings.setCategory(category);
                    emailNotificationSettingsManager.create(emailNotificationSettings);
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<String, HashSet<SelectItem>> getCompanyEmailNotificationSettings() {
        HashMap<String, HashSet<SelectItem>> companyEmailNotifications = new HashMap<>();

        EdsUser user = userManager.getUser();
        StringBuilder groupIds = new StringBuilder();
        boolean isOne = false;
        Set<EdsGroup> userGroups = user.getMembershipGroups();
        for (EdsGroup group : userGroups) {
            if (!isOne) {
                groupIds.append(group.getObjectID());
                isOne = true;
            } else {
                groupIds.append(",");
                groupIds.append(group.getObjectID());
            }
        }
        List<EdsCompanyEmailNotificationSettings> companyEmailNotificationSettings =
                companyEmailNotificationSettingsManager.getCompanyEmailNotificationSettingsWithGroup(groupIds.toString());
        Map<EdsGroup, Set<EdsCompanyEmailNotificationSettings>> companyEmailNotSettingsMap = new HashMap<>();
        for (EdsCompanyEmailNotificationSettings cENotS : companyEmailNotificationSettings) {
            companyEmailNotSettingsMap.computeIfAbsent(cENotS.getRoleGroup(), k -> new HashSet<>());
            companyEmailNotSettingsMap.get(cENotS.getRoleGroup()).add(cENotS);
        }
        for (EdsGroup userGroup : userGroups) {
            if (companyEmailNotSettingsMap.containsKey(userGroup)) {
                Set<EdsCompanyEmailNotificationSettings> cEN = companyEmailNotSettingsMap.get(userGroup);
                for (EdsCompanyEmailNotificationSettings comENS : cEN) {
                    SelectItem s = new SelectItem();
                    s.setName(comENS.getNotificationName());
                    s.setDescription(commonLocalizer.localize(comENS.getNotificationName(), comENS.getDescription()));
                    s.setNewItem(comENS.isEnabled());
                    companyEmailNotifications.computeIfAbsent(comENS.getCategory(), k -> new HashSet<>());
                    companyEmailNotifications.get(comENS.getCategory()).add(s);
                }
            }
        }
        return companyEmailNotifications;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<String, HashSet<SelectItem>> getUserEmailNotificationSettings() {

        HashMap<String, HashSet<SelectItem>> companyUserEmailNotifications = new HashMap<>();

        EdsUser user = userManager.getUser();
        Set<EdsGroup> userGroups = user.getMembershipGroups();

        StringBuilder groupIds = new StringBuilder();
        boolean isOne = false;
        for (EdsGroup gro : userGroups) {
            if (!isOne) {
                groupIds.append(gro.getObjectID());
                isOne = true;
            } else {
                groupIds.append("," + gro.getObjectID());
            }
        }
        List<EdsCompanyEmailNotificationSettings> companyEmailNotificationSettings =
                companyEmailNotificationSettingsManager.getCompanyEmailNotificationSettingsWithGroup(groupIds.toString());
        List<EdsEmailNotificationSettings> userEmailNotificationSettings = emailNotificationSettingsManager.getUserEmailNotifications(user.getObjectID());

        Map<EdsGroup, Set<EdsCompanyEmailNotificationSettings>> companyEmailNotificationSettingsMap =
                new HashMap<>();

        for (EdsCompanyEmailNotificationSettings cENot : companyEmailNotificationSettings) {
            companyEmailNotificationSettingsMap.computeIfAbsent(cENot.getRoleGroup(), k -> new HashSet<>());
            companyEmailNotificationSettingsMap.get(cENot.getRoleGroup()).add(cENot);
        }

        for (EdsGroup userGroup : userGroups) {
            if (companyEmailNotificationSettingsMap.containsKey(userGroup)) {
                Set<EdsCompanyEmailNotificationSettings> cEN = companyEmailNotificationSettingsMap.get(userGroup);
                for (EdsCompanyEmailNotificationSettings cenS : cEN) {
                    SelectItem s = new SelectItem();
                    s.setName(cenS.getNotificationName());
                    s.setDescription(commonLocalizer.localize(cenS.getNotificationName(), cenS.getDescription()));
                    s.setNewItem(cenS.isEnabled());
                    companyUserEmailNotifications.computeIfAbsent(cenS.getCategory(), k -> new HashSet<>());
                    companyUserEmailNotifications.get(cenS.getCategory()).add(s);
                }
            }
        }

        for (EdsEmailNotificationSettings userEmNot : userEmailNotificationSettings) {
            String category = userEmNot.getCategory();
            for (SelectItem s : companyUserEmailNotifications.get(category)) {
                if (userEmNot.getNotificationType().equals(s.getName())) {
                    s.setNewItem(userEmNot.isEnabled());
                }
            }
        }

        return companyUserEmailNotifications;
    }

    public void sendTimeSheetReminder(Integer employeeID, Integer recurrenceID, Integer type, String when) {
        Date endDate = new Date();
        Date startDate = new Date();
        if (type == SchedulerConstant.RECURRENCE_TYPE_DAILY) {
            if (SchedulerConstant.FORPREVIOUS.equals(when)) {
                startDate = DateUtil.addDays(startDate, -1);
                endDate = DateUtil.addDays(endDate, -1);
            }
        } else if (type == SchedulerConstant.RECURRENCE_TYPE_WEEKLY) {
            while (startDate.getDay() != 1) {
                startDate = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate() - 1);
            }
            while (endDate.getDay() > 0) {
                endDate = new Date(endDate.getYear(), endDate.getMonth(), endDate.getDate() + 1);
            }
            if (SchedulerConstant.FORPREVIOUS.equals(when)) {
                startDate = DateUtil.addDays(startDate, -7);
                endDate = DateUtil.addDays(endDate, -7);
            }
        } else if (type == SchedulerConstant.RECURRENCE_TYPE_MONTHLY) {
            startDate = new Date(startDate.getYear(), startDate.getMonth(), 1);
            endDate = DateUtil.getMonthLastDate(endDate);
            if (SchedulerConstant.FORPREVIOUS.equals(when)) {
                startDate = DateUtil.addMonths(startDate, -1);
                endDate = DateUtil.getMonthLastDate(startDate);
            }
        }
        EdsNumberingSettings edsSettings = numberingSettingsManager.getNumberingSetting();
        if (recurrenceID == null || (edsSettings != null && edsSettings.getDefaultReminderID() != null && edsSettings.getDefaultReminderID().equals(recurrenceID))) {
            ArrayList<Integer> requiredEmployeeIDs = employeeManager.getTimesheetRequiredEmployeesID();
            if (employeeID != null) {
                requiredEmployeeIDs.remove(employeeID);
            }
            if (requiredEmployeeIDs != null && requiredEmployeeIDs.size() > 0) {
                for (Integer requiredEmployeeID : requiredEmployeeIDs) {
                    getTimeSheetDataAndSentToEmployee(requiredEmployeeID, endDate, startDate);
                }
            }
        }
        if (employeeID != null) {
            getTimeSheetDataAndSentToEmployee(employeeID, endDate, startDate);
        }
    }

    private void getTimeSheetDataAndSentToEmployee(Integer employeeId, Date enddate, Date startdate) {
        EdsUser employee = employeeManager.get(employeeId);
        Map<String, List<TimesheetItem>> data = timesheetServiceLocal.getTimesheetData(startdate, enddate, employee);
        if (data != null && data.size() > 0) {
            timesheetServiceLocal.sendMailToAccountants(data, employee, startdate, enddate);
        }
    }

    public void saveRecurrenceJob(RecurrenceJobItem item) {
        Integer recurInteger = recurrenceService.saveRecurrenceJob(item);

        if (item.getJobType() != null && item.getJobType() == SchedulerConstant.TIMESHEET_REMINDER &&
                item.getDefaultReminder() != null && item.getDefaultReminder() && recurInteger != null) {
            //
            EdsNumberingSettings edsSettings = numberingSettingsManager.getNumberingSetting();
            if (edsSettings == null) {
                edsSettings = new EdsNumberingSettings();
            }
            edsSettings.setDefaultReminderID(recurInteger);
            numberingSettingsManager.createOrUpdate(edsSettings);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceJobItem getJob(Integer jobType, boolean defaultTimeSheetReminder) {
        return recurrenceService.getJob(jobType, defaultTimeSheetReminder);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean validateGoogleCalendarUser() {
        return googleCalendarManager.validateUser(employeeManager.getUser());
    }

    public void deleteGoogleCalendarToken() {
        EdsUser user = employeeManager.getUser();
        try {
            googleCalendarManager.deleteGoogleCalendar(user.getEmployee(), true);
        } catch (IOException | GeneralSecurityException | ServiceException e) {
            deleteGoogleCalendar(user);
        }

        deleteGoogleCalendar(user);
    }

    private void deleteGoogleCalendar(EdsUser user) {
        EdsGoogleCalendar edsGoogleCalendar = googleCalendarManager.getGoogleCalendar(user, true);
        if (edsGoogleCalendar != null) {
            googleCalendarManager.delete(edsGoogleCalendar);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean validateGoogleContactUser() {
        return googleContactsManager.validateUser(googleContactsManager.getUser());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean validateGoogleAnalytics() {
        return googleAnalyticsManager.validateUser(googleContactsManager.getUser());
    }

    public void deleteGoogleContactToken(boolean isOffice) {
        EdsUser user = employeeManager.getUser();
        EdsServerContacts edsServerContacts = googleContactsManager.getGoogleContact(user, true, isOffice);
        if (edsServerContacts != null) {
            googleContactsManager.delete(edsServerContacts);
        }
        crmContactManager.clearGoogleIdFromContact(user.getObjectID());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean validateGoogleDocumentUser() {
        return googleDocumentsManager.validateUser();
    }

    /**
     * deleted google documents
     * deleted google documents settings
     */
    public void deleteGoogleDocumentsToken() {
        EdsSinxDocuments googleDocuments = googleDocumentsManager.getGoogleDocuments(employeeManager.getUser(), true);
        List<EdsSinxDocumentsSettings> docSettings = sinxDocumentsSettingsManager.getGoogleDocSettings(googleDocuments);
        if (docSettings != null && !docSettings.isEmpty()) {
            for (EdsSinxDocumentsSettings googleDocsSett : docSettings) {
                if (googleDocsSett.getUpload() != null && googleDocsSett.getUpload() instanceof EdsFileBody fb) {
                    if (fb.getHeader() != null) {
                        try {
                            documentsService.deleteFile(fb.getHeader().getObjectID());
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            e.printStackTrace();
                        }
                    }
                }
                sinxDocumentsSettingsManager.delete(googleDocsSett);
            }
        }
        googleDocumentsManager.delete(googleDocuments);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PMNumberingSettings getPMNumberingSettings() {
        final EdsNumberingSettings edsSettings = numberingSettingsManager.getNumberingSetting();
        final PMNumberingSettings settings = new PMNumberingSettings();

        if (edsSettings != null) {
            settings.setObjectID(edsSettings.getObjectID());
            settings.setProjectNumberingFormat(edsSettings.getProjectNumberingFormat());
            settings.setTaskNumberingFormat(edsSettings.getTaskNumberingFormat());
            settings.setWorkstreamNumberingFormat(edsSettings.getWorkstreamNumberingFormat());
            settings.setEmployeeNumberingFormat(edsSettings.getEmployeeNumberingFormat());
            settings.setDepartmentNumberingFormat(edsSettings.getDepartmentNumberingFormat());
            settings.setLeaveRequestNumberingFormat(edsSettings.getLeaveRequestNumberingFormat());
            settings.setPositionNumberingFormat(edsSettings.getPositionNumberingFormat());
            settings.setPersonalGoalNumberingFormat(edsSettings.getPersonalGoalNumberingFormat());
            settings.setProjectGoalNumberingFormat(edsSettings.getProjectGoalNumberingFormat());
            settings.setPlacementNumberingFormat(edsSettings.getPlacementNumberingFormat());
            settings.setOpportunityNumberingFormat(edsSettings.getOpportunityNumberingFormat());
            settings.setTrackerPrefix(edsSettings.getTrackerPrefix());
            settings.setProductNumberingFormat(edsSettings.getProductNumberingFormat());
            settings.setRentalOrderNumberingFormat(edsSettings.getRentalOrderNumberingFormat());
            settings.setProductCategoryNumberingFormat(edsSettings.getProductCategoryNumberingFormat());
            settings.setFixedAssetNumberingFormat(edsSettings.getFixedAssetNumberingFormat());
            settings.setRfpNumberingFormat(edsSettings.getRfpNumberingFormat());
            settings.setAutomatic(edsSettings.isAutomatic());
            settings.setAutomaticApproval(edsSettings.isAutomaticApproval());
            settings.setWaitingForApproval(edsSettings.isWaitingForApproval());
            settings.setValidateTaskStart(edsSettings.isValidateTaskStart());
            settings.setValidateTaskEnd(edsSettings.isValidateTaskEnd());
            settings.setValidateMaximumHours(edsSettings.isValidateMaximumHours());
            settings.setValidateDayOff(edsSettings.isValidateDayOff());
            settings.setProjectNumberRestartDate(edsSettings.getProjectNumberRestartDate());
            settings.setLeaveRequestNumberRestartDate(edsSettings.getLeaveRequestNumberRestartDate());
            settings.setDelimetrProject(edsSettings.getDelimetrProject());
            settings.setDelimetrTask(edsSettings.getDelimetrTask());
            settings.setDelimetrWorkstream(edsSettings.getDelimetrWorkstream());
            settings.setDelimetrEmployeeNumbering(edsSettings.getDelimetrEmployeeNumbering());
            settings.setDelimetrDepartmentFormat(edsSettings.getDelimetrDepartmentNumbering());
            settings.setDelimetrLeaveRequestNumbering(edsSettings.getDelimetrLeaveRequestNumbering());
            settings.setDelimetrPositionNumbering(edsSettings.getDelimetrPositionNumbering());
            settings.setDelimetrPersonalGoalNumbering(edsSettings.getDelimetrPersonalGoalNumbering());
            settings.setDelimetrProjectGoalNumbering(edsSettings.getDelimetrProjectGoalNumbering());
            settings.setDelimetrPlacementNumbering(edsSettings.getDelimetrPlacementNumbering());
            settings.setBarcodeNumbering(edsSettings.getBarcodeNumbering());
            settings.setBarcodeType(edsSettings.getBarcodeType());
            if (edsSettings.isValidateMaximumHours()) {
                settings.setValidateTimeslot(edsSettings.isValidateTimeslot());
                settings.setMaximumHours(edsSettings.getMaximumHours());
            }
            settings.setValidatePastTimesheet(edsSettings.isValidatePastTimesheet());
            if (edsSettings.isValidatePastTimesheet()) {
                settings.setPastTimesheetDays(edsSettings.getPastTimesheetDays());
            }
            settings.setValidateFutureTimesheet(edsSettings.isValidateFutureTimesheet());
            if (edsSettings.isValidateFutureTimesheet()) {
                settings.setFutureTimesheetDays(edsSettings.getFutureTimesheetDays());
            }
            settings.setValidateHoliday(edsSettings.isValidateHoliday());
            settings.setValidateLeaveRequest(edsSettings.isValidateLeaveRequest());
            settings.setShowTaskRelated(companySystemSettingsManager.showTaskRelated());
            settings.setTimesheetWeekStart(edsSettings.getTimesheetWeekStart());
            settings.setShowCompletedTasks(edsSettings.getShowCompletedTasks());
            settings.setTimesheetCommentRequired(edsSettings.getTimesheetCommentRequired());
            settings.setTimesheetApprovalCommentRequired(edsSettings.getTimesheetApprovalCommentRequired());
            settings.setDailyFillTimesheetFromResUtilRequired(edsSettings.getDailyFillTimesheetFromResUtilRequired());
            settings.setShowToDoListTasks(edsSettings.getShowToDoListTasks());
            settings.setShowTimesheetHourTypes(edsSettings.getShowTimesheetHourTypes());
            settings.setEnableMultipleTimerInstances(edsSettings.getEnableMultipleTimerInstances());
            settings.setSaveTimerIntoTimesheetAutomatically(edsSettings.getSaveTimerIntoTimesheetAutomatically());
            settings.setSortTimesheetByTaskName(edsSettings.getSortTimesheetByTaskName() != null ? edsSettings.getSortTimesheetByTaskName() : false);
            settings.setTimesheetDateFormat(edsSettings.getTimesheetDateFormat());
            settings.setValidateTimesheetEstimate(edsSettings.isValidateTimesheetEstimate());
            settings.setExpenseNumberingFormat(edsSettings.getExpenseNumberingFormat());
            settings.setMtNumberingFormat(edsSettings.getMtNumberingFormat());
            settings.setGrnNumberFormat(edsSettings.getGrnNumberFormat());
            settings.setGdnNumberFormat(edsSettings.getGdnNumberFormat());
            settings.setBpNumberingFormat(edsSettings.getSmNumberingFormat());
            settings.setBrNumberingFormat(edsSettings.getRmNumberingFormat());
            settings.setCpNumberingFormat(edsSettings.getCpNumberingFormat());
            settings.setCrNumberingFormat(edsSettings.getCrNumberingFormat());
            settings.setPrNumberingFormat(edsSettings.getPrNumberingFormat());
            settings.setScNumberingFormat(edsSettings.getScNumberingFormat());
            settings.setSrfNumberingFormat(edsSettings.getSrfNumberingFormat());
            settings.setCrfNumberingFormat(edsSettings.getCrfNumberingFormat());
            settings.setCaNumberingFormat(edsSettings.getCashAdvanceNumberFormat());
            settings.setMcaNumberingFormat(edsSettings.getMultiCashAdvanceNumberFormat());
            settings.setStNumberingFormat(edsSettings.getStockTransferNumberingFormat());
            //check for enable Receive Payments and Pay Invoices numbering settings
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS)) {
                settings.setRpNumberingFormat(edsSettings.getRpNumberingFormat());
                settings.setPbNumberingFormat(edsSettings.getPbNumberingFormat());
            }
        }
        final EdsCompany company = invoicingSettingsManager.getUser().getCompany();
        final EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);

        if (invSettings != null) {
            settings.setInvoiceNumberingFormat(invSettings.getInvoiceNumberingFormat());
            settings.setSalesQuoteNumberingFormat(invSettings.getSalesQuoteNumberingFormat());
            settings.setSalesOrderNumberingFormat(invSettings.getSalesOrderNumberingFormat());
            settings.setPurchaseOrderNumberingFormat(invSettings.getPurchaseOrderNumberingFormat());
            settings.setPiNumberingFormat(invSettings.getPiNumberingFormat());
            settings.setCnNumberingFormat(invSettings.getCnNumberingFormat());
            settings.setNumberingRestartEnabled(invSettings.isNumberingRestartEnabled());
            settings.setNumberingRestartDate(invSettings.getNumberingRestartDate());
            settings.setNumberingRestartMonth(invSettings.getNumberingRestartMonth());
        }
        if (ServerUtils.isNullOrEmpty(settings.getTrackerPrefix())) {
            settings.setTrackerPrefix(ServerSecurityContext.getInstance().getCompanyId() + "-");
        }
        return settings;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PMNumberingSettings getPayrollNumberingSettings() {
        EdsNumberingSettings edsSettings = numberingSettingsManager.getNumberingSetting();
        PMNumberingSettings settings = new PMNumberingSettings();
        if (edsSettings != null) {
            settings.setObjectID(edsSettings.getObjectID());
            settings.setCaNumberingFormat(edsSettings.getCashAdvanceNumberFormat());
            settings.setMcaNumberingFormat(edsSettings.getMultiCashAdvanceNumberFormat());
            settings.setSaNumberingFormat(edsSettings.getEndOfServiceNumberingFormat());
        }
        return settings;
    }

    @Override
    public Integer savePayrollNumberingSettings(PMNumberingSettings settings) {
        EdsNumberingSettings edsSettings;
        if (settings.getObjectID() != null) {
            edsSettings = numberingSettingsManager.get(settings.getObjectID());
        } else {
            edsSettings = numberingSettingsManager.getNumberingSetting();
        }
        if (edsSettings == null) {
            edsSettings = new EdsNumberingSettings();
        }
        if (settings.getCaNumberingFormat() != null && !settings.getCaNumberingFormat().isEmpty()) {
            edsSettings.setCashAdvanceNumberFormat(settings.getCaNumberingFormat());
        }
        if (settings.getMcaNumberingFormat() != null && !settings.getMcaNumberingFormat().isEmpty()) {
            edsSettings.setMultiCashAdvanceNumberFormat(settings.getMcaNumberingFormat());
        }
        if (settings.getSaNumberingFormat() != null && !settings.getSaNumberingFormat().isEmpty()) {
            edsSettings.setEndOfServiceNumberingFormat(settings.getSaNumberingFormat());
        }
        numberingSettingsManager.createOrUpdate(edsSettings);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        if (settings.getObjectID() != null) {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        } else {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
        }
        kpiLog.setEntityId(edsSettings.getObjectID());
        kpiLog.setEntityName(EdsNumberingSettings.class.getSimpleName());
        kpiLog.setEntityType(Constants.NUMBER);
        ServerUtils.kpiLog(log, kpiLog, edsSettings.getCashAdvanceNumberFormat());

        return edsSettings.getObjectID();
    }

    @Override
    public Integer savePMNumberingSettings(PMNumberingSettings settings, String view) {
        EdsNumberingSettings edsSettings;

        if (settings.getObjectID() != null) {
            edsSettings = numberingSettingsManager.get(settings.getObjectID());
        } else {
            edsSettings = numberingSettingsManager.getNumberingSetting();
        }
        if (edsSettings == null) {
            edsSettings = new EdsNumberingSettings();
        }
        if (!StringUtil.isEmpty(settings.getProjectNumberingFormat())) {
            edsSettings.setProjectNumberingFormat(settings.getProjectNumberingFormat());
            edsSettings.setDelimetrProject(settings.getDelimetrProject());
            edsSettings.setProjectNumberRestartDate(settings.getProjectNumberRestartDate());
            edsSettings.setProjectIntNumber(settings.getProjectIntNumber());
            edsSettings.setProjectLastIntNumber(settings.getProjectIntNumber());
        }
        if (!StringUtil.isEmpty(settings.getTaskNumberingFormat())) {
            edsSettings.setTaskNumberingFormat(settings.getTaskNumberingFormat());
            edsSettings.setDelimetrTask(settings.getDelimetrTask());
        }
        if (!StringUtil.isEmpty(settings.getWorkstreamNumberingFormat())) {
            edsSettings.setWorkstreamNumberingFormat(settings.getWorkstreamNumberingFormat());
            edsSettings.setDelimetrWorkstream(settings.getDelimetrWorkstream());
        }
        if (!StringUtil.isEmpty(settings.getEmployeeNumberingFormat())) {
            edsSettings.setEmployeeNumberingFormat(settings.getEmployeeNumberingFormat());
            edsSettings.setDelimetrEmployeeNumbering(settings.getDelimetrEmployeeNumbering());
        }
        if (!StringUtil.isEmpty(settings.getDepartmentNumberingFormat())) {
            edsSettings.setDepartmentNumberingFormat(settings.getDepartmentNumberingFormat());
            edsSettings.setDelimetrDepartmentNumbering(settings.getDelimetrDepartmentFormat());
        }
        if (!StringUtil.isEmpty(settings.getLeaveRequestNumberingFormat())) {
            edsSettings.setLeaveRequestNumberingFormat(settings.getLeaveRequestNumberingFormat());
            edsSettings.setDelimetrLeaveRequestNumbering(settings.getDelimetrLeaveRequestNumbering());
            edsSettings.setLeaveRequestNumberRestartDate(settings.getLeaveRequestNumberRestartDate());
            edsSettings.setLeaveRequestLastIntNumber(settings.getLeaveRequestLastIntNumber());
            edsSettings.setLeaveRequestIntNumber(settings.getLeaveRequestIntNumber());
        }
        if (!StringUtil.isEmpty(settings.getPositionNumberingFormat())) {
            edsSettings.setPositionNumberingFormat(settings.getPositionNumberingFormat());
            edsSettings.setDelimetrPositionNumbering(settings.getDelimetrPositionNumbering());
        }
        if (!StringUtil.isEmpty(settings.getPlacementNumberingFormat())) {
            edsSettings.setPlacementNumberingFormat(settings.getPlacementNumberingFormat());
            edsSettings.setDelimetrPlacementNumbering(settings.getDelimetrPlacementNumbering());
        }
        if (!StringUtil.isEmpty(settings.getOpportunityNumberingFormat())) {
            edsSettings.setOpportunityNumberingFormat(settings.getOpportunityNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getProductNumberingFormat())) {
            edsSettings.setProductNumberingFormat(settings.getProductNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getRentalOrderNumberingFormat())) {
            edsSettings.setRentalOrderNumberingFormat(settings.getRentalOrderNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getProductCategoryNumberingFormat())) {
            edsSettings.setProductCategoryNumberingFormat(settings.getProductCategoryNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getTrackerPrefix())) {
            edsSettings.setTrackerPrefix(settings.getTrackerPrefix());
        }
        if (!StringUtil.isEmpty(settings.getFixedAssetNumberingFormat())) {
            edsSettings.setFixedAssetNumberingFormat(settings.getFixedAssetNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getRfpNumberingFormat())) {
            edsSettings.setRfpNumberingFormat(settings.getRfpNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getExpenseNumberingFormat())) {
            edsSettings.setExpenseNumberingFormat(settings.getExpenseNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getMtNumberingFormat())) {
            edsSettings.setMtNumberingFormat(settings.getMtNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getGrnNumberFormat())) {
            edsSettings.setGrnNumberFormat(settings.getGrnNumberFormat());
        }
        if (!StringUtil.isEmpty(settings.getGdnNumberFormat())) {
            edsSettings.setGdnNumberFormat(settings.getGdnNumberFormat());
        }
        if (!StringUtil.isEmpty(settings.getBpNumberingFormat())) {
            edsSettings.setSmNumberingFormat(settings.getBpNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getBrNumberingFormat())) {
            edsSettings.setRmNumberingFormat(settings.getBrNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getCrNumberingFormat())) {
            edsSettings.setCrNumberingFormat(settings.getCrNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getCpNumberingFormat())) {
            edsSettings.setCpNumberingFormat(settings.getCpNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getPersonalGoalNumberingFormat())) {
            edsSettings.setPersonalGoalNumberingFormat(settings.getPersonalGoalNumberingFormat());
            edsSettings.setDelimetrPersonalGoalNumbering(settings.getDelimetrPersonalGoalNumbering());
        }
        if (!StringUtil.isEmpty(settings.getProjectGoalNumberingFormat())) {
            edsSettings.setProjectGoalNumberingFormat(settings.getProjectGoalNumberingFormat());
            edsSettings.setDelimetrProjectGoalNumbering(settings.getDelimetrProjectGoalNumbering());
        }
        //check for enable Receive Payments and Pay Invoices numbering settings
        final boolean isEnableRPAndPINumbering = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_RP_AND_PI_NUMBERING_SETTINGS);

        if (isEnableRPAndPINumbering) {
            if (!StringUtil.isEmpty(settings.getRpNumberingFormat())) {
                edsSettings.setRpNumberingFormat(settings.getRpNumberingFormat());
            }
            if (!StringUtil.isEmpty(settings.getPbNumberingFormat())) {
                edsSettings.setPbNumberingFormat(settings.getPbNumberingFormat());
            }
        }
        if (!StringUtil.isEmpty(settings.getPrNumberingFormat())) {
            edsSettings.setPrNumberingFormat(settings.getPrNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getCrfNumberingFormat())) {
            edsSettings.setCrfNumberingFormat(settings.getCrfNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getSrfNumberingFormat())) {
            edsSettings.setSrfNumberingFormat(settings.getSrfNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getSaNumberingFormat())) {
            edsSettings.setStockAdjustmentNumberingFormat(settings.getSaNumberingFormat());
        }
        if (!StringUtil.isEmpty(settings.getScNumberingFormat())) {
            edsSettings.setScNumberingFormat(settings.getScNumberingFormat());
        }
        if (settings.isAutomatic() != null) {
            edsSettings.setAutomatic(settings.isAutomatic());
        }
        if (settings.isAutomaticApproval() != null) {
            edsSettings.setAutomaticApproval(settings.isAutomaticApproval());
        }
        if (settings.isWaitingForApproval() != null) {
            edsSettings.setWaitingForApproval(settings.isWaitingForApproval());
        }
        if (settings.getValidateTaskStart() != null) {
            edsSettings.setValidateTaskStart(settings.getValidateTaskStart());
        }
        if (settings.getValidateMaximumHours() != null) {
            edsSettings.setValidateMaximumHours(settings.getValidateMaximumHours());
        }
        if (settings.getValidateMaximumHours()) {
            edsSettings.setValidateTimeslot(settings.getValidateTimeslot());
            edsSettings.setMaximumHours(settings.getMaximumHours());
        }
        if (settings.getValidateDayOff() != null) {
            edsSettings.setValidateDayOff(settings.getValidateDayOff());
        }
        if (settings.getValidatePastTimesheet() != null) {
            edsSettings.setValidatePastTimesheet(settings.getValidatePastTimesheet());
        }
        if (settings.getValidatePastTimesheet()) {
            edsSettings.setPastTimesheetDays(settings.getPastTimesheetDays());
        }
        if (settings.getValidateFutureTimesheet() != null) {
            edsSettings.setValidateFutureTimesheet(settings.getValidateFutureTimesheet());
        }
        if (settings.getValidateFutureTimesheet()) {
            edsSettings.setFutureTimesheetDays(settings.getFutureTimesheetDays());
        }
        if (settings.getValidateTaskEnd() != null) {
            edsSettings.setValidateTaskEnd(settings.getValidateTaskEnd());
        }
        if (settings.getValidateHoliday() != null) {
            edsSettings.setValidateHoliday(settings.getValidateHoliday());
        }
        if (settings.getValidateLeaveRequest() != null) {
            edsSettings.setValidateLeaveRequest(settings.getValidateLeaveRequest());
        }
        if (settings.getTimesheetWeekStart() != null) {
            edsSettings.setTimesheetWeekStart(settings.getTimesheetWeekStart());
        }
        if (settings.getShowCompletedTasks() != null) {
            edsSettings.setShowCompletedTasks(settings.getShowCompletedTasks());
        }
        if (settings.getTimesheetCommentRequired() != null) {
            edsSettings.setTimesheetCommentRequired(settings.getTimesheetCommentRequired());
        }
        if (settings.getTimesheetApprovalCommentRequired() != null) {
            edsSettings.setTimesheetApprovalCommentRequired(settings.getTimesheetApprovalCommentRequired());
        }
        if (settings.getDailyFillTimesheetFromResUtilRequired() != null) {
            edsSettings.setDailyFillTimesheetFromResUtilRequired(settings.getDailyFillTimesheetFromResUtilRequired());
            createDailyFillTimesheetFromResUtilTrigger(settings.getDailyFillTimesheetFromResUtilRequired());
        }
        if (settings.getShowToDoListTasks() != null) {
            edsSettings.setShowToDoListTasks(settings.getShowToDoListTasks());
        }
        if (settings.getShowTimesheetHourTypes() != null) {
            edsSettings.setShowTimesheetHourTypes(settings.getShowTimesheetHourTypes());
        }
        if (settings.getEnableMultipleTimerInstances() != null) {
            edsSettings.setEnableMultipleTimerInstances(settings.getEnableMultipleTimerInstances());
        }
        if (settings.getSaveTimerIntoTimesheetAutomatically() != null) {
            edsSettings.setSaveTimerIntoTimesheetAutomatically(settings.getSaveTimerIntoTimesheetAutomatically());
        }
        if (settings.getSortTimesheetByTaskName() != null) {
            edsSettings.setSortTimesheetByTaskName(settings.getSortTimesheetByTaskName());
        }
        if (!StringUtil.isEmpty(settings.getTimesheetDateFormat())) {
            edsSettings.setTimesheetDateFormat(settings.getTimesheetDateFormat());
        }
        edsSettings.setBarcodeNumbering(settings.getBarcodeNumbering());
        edsSettings.setBarcodeType(settings.getBarcodeType());
        edsSettings.setValidateTimesheetEstimate(settings.isValidateTimesheetEstimate());

        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(userManager.getUser().getCompany().getObjectID());
        if (companySystemSettings != null) {
            companySystemSettings.setShowTaskRelated(settings.getShowTaskRelated());
            companySystemSettingsManager.update(companySystemSettings);
        }
        if (Constants.PRODUCT_NUMBERING_SETTINGS_FORM.equals(view)) {
            EdsCompany company = invoicingSettingsManager.getUser().getCompany();
            EdsInvoicingSettings invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
            if (invoiceSettings == null) {
                invoiceSettings = new EdsInvoicingSettings();
            }
            //Invoice Numbering Format

            invoiceSettings.setInvoiceNumberingFormat(settings.getInvoiceNumberingFormat());
            invoiceSettings.setInvoiceCreditNoteNumberingFormat(settings.getInvoiceCreditNoteNumberingFormat());
            invoiceSettings.setSalesQuoteNumberingFormat(settings.getSalesQuoteNumberingFormat());
            invoiceSettings.setSalesOrderNumberingFormat(settings.getSalesOrderNumberingFormat());
            invoiceSettings.setPurchaseOrderNumberingFormat(settings.getPurchaseOrderNumberingFormat());
            invoiceSettings.setPiNumberingFormat(settings.getPiNumberingFormat());
            invoiceSettings.setCnNumberingFormat(settings.getCnNumberingFormat());
            invoiceSettings.setDnNumberingFormat(settings.getDnNumberingFormat());
            if (settings.isNumberingRestartEnabled()) {
                invoiceSettings.setNumberingRestartEnabled(true);
                invoiceSettings.setNumberingRestartDate(settings.getNumberingRestartDate());
                invoiceSettings.setNumberingRestartMonth(settings.getNumberingRestartMonth());
            } else {
                invoiceSettings.setNumberingRestartEnabled(false);
                invoiceSettings.setNumberingRestartDate(null);
                invoiceSettings.setNumberingRestartMonth(null);
            }

            invoicingSettingsManager.createOrUpdate(invoiceSettings);
        } else if (PAYROLL_NUMBERING_SETTINGS_FORM.equals(view)) {
            if (settings.getCaNumberingFormat() != null)
                edsSettings.setCashAdvanceNumberFormat(settings.getCaNumberingFormat());
        }
        numberingSettingsManager.createOrUpdate(edsSettings);
        if (edsSettings != null && settings.getProjectNumberRestartDate() != null) {
            restartPMNumber(edsSettings, settings.getProjectNumberRestartDate());
        }
        if (edsSettings != null && settings.getLeaveRequestNumberRestartDate() != null) {
            restartLRNumber(edsSettings, settings.getLeaveRequestNumberRestartDate());
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        if (settings.getObjectID() != null) {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        } else {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
        }
        kpiLog.setEntityId(edsSettings.getObjectID());
        kpiLog.setEntityName(EdsNumberingSettings.class.getSimpleName());
        kpiLog.setEntityType(Constants.NUMBER);
        ServerUtils.kpiLog(log, kpiLog, edsSettings.getProjectNumberingFormat());

        return edsSettings.getObjectID();
    }

    private void createDailyFillTimesheetFromResUtilTrigger(boolean dailyFillTimesheetFromResUtilRequired) {
        Integer objId = 3885181;
        EdsRecurrence existingRecurrence = recurrenceService.getRecurrenceByJobId(objId, SchedulerConstant.DAILY_FILL_TIMESHEET_FROM_RES_UTIL);

        if (existingRecurrence == null && dailyFillTimesheetFromResUtilRequired) {
            RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
            recurrenceJobItem.setEnabled(true);
            recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_DAILY);
            recurrenceJobItem.setJobType(SchedulerConstant.DAILY_FILL_TIMESHEET_FROM_RES_UTIL);
            recurrenceJobItem.setBusObjectId(objId);
            recurrenceJobItem.setBusObjectParams("DAILY_FILL_TIMESHEET_FROM_RES_UTIL");
            recurrenceJobItem.setInterval(1);
            recurrenceJobItem.setDailyPatternOptions(SchedulerConstant.DAILY_PATTERN_OPTION_INTERVAL);
            recurrenceJobItem.setEndType(SchedulerConstant.NO_END_DATE);

            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            calendar.setTime(date);
            Date userDate = new Date(calendar.getTime().getTime() - userManager.getUser().getUserTimezone().getRawOffset());
            userDate.setHours(23);
            userDate.setMinutes(59);
            recurrenceJobItem.setStartDate(userDate);
            recurrenceService.saveRecurrenceJob(recurrenceJobItem);
        } else if (!dailyFillTimesheetFromResUtilRequired && existingRecurrence != null) {
            recurrenceService.deleteRecurrence(objId, SchedulerConstant.DAILY_FILL_TIMESHEET_FROM_RES_UTIL);
        }
    }

    private void restartPMNumber(EdsNumberingSettings edsSettings, Date projectRestartDate) {
        RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
        recurrenceJobItem.setEnabled(true);
        recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_YEARLY);
        recurrenceJobItem.setJobType(SchedulerConstant.RECURRING_PROJECT_NUMBER);
        recurrenceJobItem.setBusObjectId(edsSettings.getObjectID());
        recurrenceJobItem.setInterval(1);
        recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
        recurrenceJobItem.setEndType(SchedulerConstant.NO_END_DATE);
        recurrenceJobItem.setStartDate(projectRestartDate);
        recurrenceJobItem.setYearlyMonth(projectRestartDate.getMonth() + 1);
        recurrenceJobItem.setMonthlyOrYearlyDay(projectRestartDate.getDate());
        recurrenceJobItem.setStartDate(projectRestartDate);
        recurrenceJobItem.setYearlyMonth(projectRestartDate.getMonth() + 1);
        recurrenceJobItem.setMonthlyOrYearlyDay(projectRestartDate.getDate());
        recurrenceService.saveRecurrenceJob(recurrenceJobItem);
    }

    private void restartLRNumber(EdsNumberingSettings edsSettings, Date leaveReqRestartDate) {
        RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
        recurrenceJobItem.setEnabled(true);
        recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_YEARLY);
        recurrenceJobItem.setJobType(SchedulerConstant.RECURRING_LEAVE_REQUEST_NUMBER);
        recurrenceJobItem.setBusObjectId(edsSettings.getObjectID());
        recurrenceJobItem.setInterval(1);
        recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
        recurrenceJobItem.setEndType(SchedulerConstant.NO_END_DATE);
        recurrenceJobItem.setStartDate(leaveReqRestartDate);
        recurrenceJobItem.setYearlyMonth(leaveReqRestartDate.getMonth() + 1);
        recurrenceJobItem.setMonthlyOrYearlyDay(leaveReqRestartDate.getDate());
        recurrenceJobItem.setStartDate(leaveReqRestartDate);
        recurrenceJobItem.setYearlyMonth(leaveReqRestartDate.getMonth() + 1);
        recurrenceJobItem.setMonthlyOrYearlyDay(leaveReqRestartDate.getDate());
        recurrenceService.saveRecurrenceJob(recurrenceJobItem);
    }

    private void saveThemeForTheSystem(String theme) {
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        if (companySettings == null) {
            companySettings = new EdsCompanySettings();
        }
        companySettings.setThemeForSystem(theme);
    }

    public void saveLanguageForUser(String language, boolean applyAllUsers) {
        EdsUser loggedUser = userManager.getUser();
        if (applyAllUsers) {
            List<EdsUser> existingUsers = userManager.getUsers();

            if (existingUsers == null || existingUsers.size() < 1) {
                EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(loggedUser);
                userSettings.setInternationalization(language);
            }

            if (existingUsers != null) {
                userEmailSettingsManager.updateUserSettings(language);
            }
        } else {
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(loggedUser);
            userSettings.setInternationalization(language);
        }
    }

    @Override
    public String getWebContentByUrl(String url) {
        return messageManager.getWebContentByUrl(url);
    }

    @Override
    public ListResult<SmsSettings> getSmsSettingList(ListingFilterParameter fp) {
        EdsUser user = smsManager.getUser();
        fp.setCompanyID(user != null && user.getCompany() != null ? user.getCompany().getObjectID() : null);
        List<EdsSmsSettings> list = smsManager.list(fp);
        int totalCount = list.size();
        if (fp.getLimit() > 0) {
            list = ListUtils.getSublist(list, fp.getStart(), fp.getLimit());
        }
        ArrayList<SmsSettings> items = new ArrayList<>();
        for (EdsSmsSettings sms : list) {
            items.add(sms.getRPC());
        }
        return new ListResult<>(items, totalCount);
    }

    @Override
    public SmsSettings getSmsSetting(Integer objectID) {
        EdsSmsSettings sms = objectID != null ? smsManager.get(objectID) : new EdsSmsSettings();
        return sms.getRPC();
    }

    @Transactional
    @Override
    public void saveSmsSettings(SmsSettings smsSettings) {
        EdsSmsSettings edsSms = smsSettings.getObjectID() != null ? smsManager.get(smsSettings.getObjectID()) : new EdsSmsSettings();
        edsSms.setCompany(smsManager.getUser().getCompany());
        edsSms.setName(smsSettings.getName());
        edsSms.setSmsProviderID(smsSettings.getProviderID());
        edsSms.setKeyValues(smsSettings.getKeyValues());
        smsManager.createOrUpdate(edsSms);
    }

    @Override
    public void deleteSMSSettings(Integer objectID) {
        EdsSmsSettings smsSettings = smsManager.get(objectID);
        smsSettings.setDeleted(true);
        smsManager.update(smsSettings);

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setProviderID(objectID);
        List<EdsWorkflowSMSAlert> smsAlerts = workflowSMSAlertManager.list(fp);
        for (EdsWorkflowSMSAlert smsAlert : smsAlerts) {
            smsAlert.setProvider(null);
            workflowSMSAlertManager.update(smsAlert);
        }
    }

    @Override
    public void updateCompanyOpportunitySettings(CompanyOpportunitySettings opportunitySettings) {
        EdsCompanySettings settings = profileManager.getUser().getCompany().getCompanySettings();
        if (opportunitySettings != null) {
            settings.setFillOpportunityItemWithInventory(opportunitySettings.isFillOpportunitItems());
            settings.setOpportunityRequireContractUpload(opportunitySettings.isRequireContractUpload());
            settings.setJoinOpportunityToExpenseClaim(opportunitySettings.isJoinOpportunityToExpenseClaim());
            settings.setEmailAutoLinking(opportunitySettings.isEmailAutoLinking());
            settings.setGenerateCrmAccountNumbering(opportunitySettings.isGenerateCrmAccountNumber());
            settings.setImportPreference(opportunitySettings.getImportPreference());
            settings.setOverwritePreference(opportunitySettings.getOverwritePreference());
            settings.setSipuniContactType(opportunitySettings.getContactType() != null ? opportunitySettings.getContactType().getId() : null);
            settings.setOpportunitySourceId(opportunitySettings.getOpportunitySourceId());
            settings.setOpportunityStageId(opportunitySettings.getOpportunityStageId());
            settings.setConvertsTo(opportunitySettings.getConvertsTo());
            companySettingsManager.update(settings);
        }
        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        numberingSettings = numberingSettings == null ? new EdsNumberingSettings() : numberingSettings;
        if (!StringUtils.isEmpty(opportunitySettings.getOpportunityNumberingSettings())) {
            numberingSettings.setOpportunityNumberingFormat(opportunitySettings.getOpportunityNumberingSettings());
        }
        if (!StringUtils.isEmpty(opportunitySettings.getPrefix())) {
            numberingSettings.setTrackerPrefix(opportunitySettings.getPrefix());
        }
        numberingSettingsManager.createOrUpdate(numberingSettings);
    }

    public void saveCustomFormCustomFieldSettings(CompanyCustomFieldItem item) {
        EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCFManager.get(item.getObjectId());
        companyCustomFieldsSettings.setColumnWidth(item.getColumnWidth());
        companyCFManager.update(companyCustomFieldsSettings);
    }

    @Override
    public ListResult<CompanyCustomFieldItem> getCustomFields(ListingFilterParameter filterParameter) {

        if (filterParameter.getCompanyID() != null) {
            ServerSecurityContext.getInstance().setCompanyId(filterParameter.getCompanyID());
        }

        List<EdsCompanyCustomFieldsSettings> customFieldsSettings = companyCFManager.getCustomFields(filterParameter);
        Integer totalCount = companyCFManager.getCustomFieldsCount(filterParameter);

        CompanyCustomFieldItem[] customFieldItems = new CompanyCustomFieldItem[customFieldsSettings.size()];
        for (int i = 0; i < customFieldsSettings.size(); i++) {
            customFieldItems[i] = new CompanyCustomFieldItem();
            customFieldItems[i].setObjectId(customFieldsSettings.get(i).getObjectID());
            customFieldItems[i].setColumnCode(customFieldsSettings.get(i).getColumnCode());
            customFieldItems[i].setEntityName(customFieldsSettings.get(i).getEntityName());
            customFieldItems[i].setFieldName(customFieldsSettings.get(i).getFieldName());
            customFieldItems[i].setAliasName(customFieldsSettings.get(i).getAliasName());
            customFieldItems[i].setUiType(customFieldsSettings.get(i).getUiType());
            customFieldItems[i].setColumnWidth(customFieldsSettings.get(i).getColumnWidth());
            customFieldItems[i].setDataType(customFieldsSettings.get(i).getDataType());
            customFieldItems[i].setPredefinedValues(customFieldsSettings.get(i).getPredefinedValues());
            customFieldItems[i].setRequired(customFieldsSettings.get(i).getRequired() != null && customFieldsSettings.get(i).getRequired());
            customFieldItems[i].setActive(customFieldsSettings.get(i).isActive());
            customFieldItems[i].setSystemField(customFieldsSettings.get(i).getDataType() != null && customFieldsSettings.get(i).getDataType().equals(Constants.SYSTEM));
            customFieldItems[i].setMinChar(customFieldsSettings.get(i).getMinChar());

            EdsAuditInfo auditInfo = customFieldsSettings.get(i).getAuditInfo();
            if (auditInfo != null) {
                customFieldItems[i].setCreatedBy(auditInfo.getCreatedBy() != null ? auditInfo.getCreatedBy().getName() : "");
                customFieldItems[i].setCreationDate(auditInfo.getCreationDate());
                customFieldItems[i].setLastUpdatedBy(auditInfo.getModifiedBy() != null ? auditInfo.getModifiedBy().getName() : "");
                customFieldItems[i].setLastUpdatedDate(auditInfo.getModificationDate());
            }

            if (customFieldsSettings.get(i).getRelationship() != null && filterParameter.getEntityName() != null && filterParameter.getEntityName().contains(ViewName.ProductCategory.name())) {
                EdsProductCategory productCategory = productCategoryManager.get(customFieldsSettings.get(i).getRelationship());
                if (productCategory != null) {
                    customFieldItems[i].setRelationshipName(productCategory.getName());
                }
            }
        }

        ServerSecurityContext.getInstance().removeCompanyId();

        return new ListResult<CompanyCustomFieldItem>(new ArrayList<>(Arrays.asList(customFieldItems)), totalCount);
    }

    @Override
    public SelectItem[] getCustomFieldsAsSelectItem(ListingFilterParameter fp) {
        if (fp.getCompanyID() != null) {
            ServerSecurityContext.getInstance().setCompanyId(fp.getCompanyID());
        }

        List<EdsCompanyCustomFieldsSettings> customFieldsSettings = companyCFManager.getCustomFields(fp);
        if (customFieldsSettings != null && customFieldsSettings.size() > 0) {
            SelectItem[] items = new SelectItem[customFieldsSettings.size()];

            int index = 0;
            for (EdsCompanyCustomFieldsSettings field : customFieldsSettings) {
                items[index] = new SelectItem(field.getObjectID(), field.getFieldName());
                index++;
            }

            return items;
        }
        return new SelectItem[0];
    }

    @Override
    public SMSTemplateItem getSMSTemplateForWorkflow(Integer templateID) {
        SMSTemplateItem item = new SMSTemplateItem();
        if (templateID != null) {
            EdsSMSTemplates sms = smsTemplateManager.get(templateID);
            item = sms.getRPC(item);
        }
        item.setModules(ServerUtils.getAsSelectItem(referenceManager.listReferences(WorkflowRule._WORKFLOW_MODULE), ServerUtils.REFERENCE));
        return item;
    }

    @Override
    public SMSTemplateItem getSMSTemplate(Integer templateID) {
        SMSTemplateItem item = new SMSTemplateItem();
        if (templateID != null) {
            EdsSMSTemplates sms = smsTemplateManager.get(templateID);
            item = sms.getRPC(item);
        }
        List<EdsReference> workflowModuleList = referenceManager.listReferences(WorkflowRule._WORKFLOW_MODULE);
        List<EdsReference> smsModuleList = referenceManager.listReferences(_SMS_TEMPLATE);

        if (workflowModuleList != null && smsModuleList != null) {
            workflowModuleList.addAll(smsModuleList);
        }

        item.setModules(ServerUtils.getAsSelectItem(workflowModuleList, ServerUtils.REFERENCE));
        return item;
    }

    @Override
    public void saveSMSTemplate(SMSTemplateItem templateItem) {
        EdsSMSTemplates sms = templateItem.getObjectID() == null ? new EdsSMSTemplates() : smsTemplateManager.get(templateItem.getObjectID());
        sms.setName(templateItem.getName());
        sms.setModule(referenceManager.get(templateItem.getModuleID()));
        sms.setContent(templateItem.getContent());
        sms.setIsDefault(templateItem.isDefault());
        smsTemplateManager.createOrUpdate(sms);
    }

    @Override
    public void deleteSMSTemplate(Integer objectID) {
        EdsSMSTemplates smsTemplates = smsTemplateManager.get(objectID);
        smsTemplates.setDeleted(true);
        smsTemplateManager.update(smsTemplates);
    }

    @Override
    public EmployeeStepItem getWorkflowStep(Integer stepEmployeeID, Integer workflowID) {
        EmployeeStepItem step = new EmployeeStepItem();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setAllByFilter(true);
        step.setOnboardingSteps(getApproverModules(fp));
        if (stepEmployeeID != null) {
            EdsStepEmployee stepEmployee = stepEmployeeManager.get(stepEmployeeID);
            step.setObjectID(stepEmployee.getObjectID());
            step.setStepID(stepEmployee.getOnboardingStep().getObjectID());
            step.setStepName(stepEmployee.getOnboardingStep().getName());
            step.setStatuses(getStepStatuses(stepEmployee.getOnboardingStep().getObjectID()));
            if (stepEmployee.getStatus() != null) {
                step.setStatusID(stepEmployee.getStatus().getObjectID());
            }
        }
        return step;
    }

    @Override
    public void saveWorkflowStep(EmployeeStepItem step) {
        EdsStepEmployee stepEmployee = new EdsStepEmployee();
        if (step.getObjectID() != null) {
            stepEmployee = stepEmployeeManager.get(step.getObjectID());
        }
        if (step.getStatusID() != null) {
            stepEmployee.setEntityStatus(referenceManager.get(step.getStatusID()));
        }
        stepEmployee.setWorkflowID(step.getWorkflowID());
        stepEmployee.setOnboardingStep(onboardingStepManager.get(step.getStepID()));
        stepEmployee.setWorkflowItem(true);
        stepEmployeeManager.createOrUpdate(stepEmployee);
        try {
            employeeStepSolrComponent.index(stepEmployee);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWorkflowStep(Integer stepID) {
        EdsStepEmployee stepEmployee = stepEmployeeManager.get(stepID);
        stepEmployee.setDeleted(true);
        stepEmployeeManager.update(stepEmployee);
        try {
            employeeStepSolrComponent.index(stepEmployee);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWorkflowSteps(ArrayList<Integer> stepIDs) {
        if (stepIDs != null && stepIDs.size() > 0) {
            stepEmployeeManager.removeByIDs(stepIDs);
        }
    }

    @Override
    public ListResult<EmployeeStepItem> listWorkflowSteps(ListingFilterParameter fp) {
        ListResult<EmployeeStepItem> items = hrmsService.getEmployeeStepList(fp);
        ArrayList<EmployeeStepItem> steps = new ArrayList<>();
        if (items.getList() != null && items.getList().size() > 0) {
            for (EmployeeStepItem step : items.getList()) {
                EmployeeStepItem item = new EmployeeStepItem();
                item.setObjectID(step.getObjectID());
                item.setStepID(step.getStepID());
                item.setFormID(step.getFormID());
                item.setStepName(step.getStepName());
                item.setStatusID(step.getStatusID());
                item.setStatusName(step.getStatusName());
                steps.add(item);
            }
        }
        return new ListResult<>(steps, items.getTotal());
    }

    @Override
    public SelectItem[] getStepStatuses(Integer stepID) {
        if (stepID != null) {
            EdsOnboardingStep onboardingStep = onboardingStepManager.get(stepID);
            ArrayList<ReferenceItem> statusItems = new ArrayList<>();
            if (onboardingStep.getStatus() != null) {
                List<EdsReference> references = referenceManager.listReferences(onboardingStep.getStatus().getCode());
                for (EdsReference reference : references) {
                    statusItems.add(reference.getRPC());
                }
            }
            return statusItems.toArray(new SelectItem[]{});
        }
        return null;
    }

    @Override
    public CompanyCustomFieldItem getCustomFieldData(Integer objectID, Integer companyID) {
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }

        EdsCompanyCustomFieldsSettings settings = companyCFManager.get(objectID);
        if (settings != null) {
            CompanyCustomFieldItem customFieldItem = new CompanyCustomFieldItem();
            customFieldItem.setObjectId(settings.getObjectID());
            customFieldItem.setColumnCode(settings.getColumnCode());
            customFieldItem.setEntityName(settings.getEntityName());
            customFieldItem.setFieldName(settings.getFieldName());
            customFieldItem.setAliasName(settings.getAliasName());
            customFieldItem.setDataType(settings.getDataType());
            customFieldItem.setUiType(settings.getUiType());
            customFieldItem.setLookUpTypeEnum(settings.getLookUpType());
            customFieldItem.setPrefix(settings.getPrefix());
            customFieldItem.setScale(settings.getScale());
            customFieldItem.setRelationFieldId(settings.getRelationFieldId());
            customFieldItem.setActive(settings.isActive());
            customFieldItem.setRelationFieldValues(settings.getRelationFieldValues());
            customFieldItem.setQuizFormScoreValues(settings.getQuizFormScoreValues());
            if (!UI_TYPE_TEXTBOX.equals(settings.getUiType()) &&
                    !UI_TYPE_TEXTAREA.equals(settings.getUiType()) &&
                    !UI_TYPE_HTML_TEXTAREA.equals(settings.getUiType()) &&
                    !UI_TYPE_DATEPICKER.equals(settings.getUiType())) {

                customFieldItem.setPredefinedValues(settings.getPredefinedValues());
                customFieldItem.setPredefinedValuesWithSorting(settings.getPredefinedValuesWithSorting());
            }
            customFieldItem.setShowInListing(settings.isShowInListing());
            customFieldItem.setDisabled(settings.getDisabled());
            customFieldItem.setClickable(settings.isClickable());
            customFieldItem.setShowInFilterGrouping(settings.isShowInFilterGrouping());
            customFieldItem.setFacetable(settings.getFacetable());
            customFieldItem.setRequired(settings.getRequired());
            customFieldItem.setMinChar(settings.getMinChar());
            customFieldItem.setRelationship(settings.getRelationship());
            customFieldItem.setQuery(settings.getQuery());
            customFieldItem.setEntityCategoryName(settings.getEntityCategoryName());
            customFieldItem.setEntityCategoryAlias(settings.getEntityCategoryAlias());
            customFieldItem.setReferenceItem(settings.getReference());
            customFieldItem.setEntityType(settings.getEntityType() != null ? settings.getEntityType().getAsSelectItem() : null);
            ArrayList<Integer> roleList = new ArrayList<>();
            for (EdsRole role : settings.getAllowedRoles()) {
                roleList.add(role.getObjectID());
            }
            customFieldItem.setAllowedRoles(roleList);

            if (settings.getValidations() != null && settings.getValidations().size() > 0) {
                CustomFieldSettingItem[] validations = new CustomFieldSettingItem[settings.getValidations().size()];
                int index = 0;
                for (EdsCustomFieldValidation validation : settings.getValidations()) {
                    validations[index] = new CustomFieldSettingItem();
                    validations[index].setObjectID(validation.getObjectID());
                    validations[index].setCustomFieldID(validation.getCustomfield().getObjectID());
                    validations[index].setCustomFieldName(validation.getCustomfield().getFieldName());
                    validations[index].setValidationCodeID(validation.getValidationCodeID());

                    if (validation.getJoinedField() != null) {
                        validations[index].setJoinedFieldID(validation.getJoinedField().getObjectID());
                        validations[index].setJoinedFieldName(validation.getJoinedField().getFieldName());
                        validations[index].setJoinedColumnCode(validation.getJoinedField().getColumnCode());
                    }
                    validations[index].setRegex(validation.getRegexCode());

                    index++;
                }

                customFieldItem.setValidations(validations);
            }

            if (settings.getListeners() != null && settings.getListeners().size() > 0) {
                CustomFieldSettingItem[] listeners = new CustomFieldSettingItem[settings.getListeners().size()];
                int index = 0;
                for (EdsCustomFieldListener listener : settings.getListeners()) {
                    listeners[index] = new CustomFieldSettingItem();
                    listeners[index].setObjectID(listener.getObjectID());
                    listeners[index].setCustomFieldID(listener.getCustomfield().getObjectID());
                    listeners[index].setCustomFieldName(listener.getCustomfield().getFieldName());
                    listeners[index].setCode(listener.getListenerCode());
                    listeners[index].setJoinedFieldID(listener.getJoinedfield().getObjectID());
                    listeners[index].setJoinedFieldName(listener.getJoinedfield().getFieldName());

                    index++;
                }

                customFieldItem.setListeners(listeners);
            }
            if (settings.getCustomFormlocalization() != null) {
                customFieldItem.setLocalization(settings.getCustomFormlocalization().getRPC());
            }

            if (companyID != null) {
                ServerSecurityContext.getInstance().removeCompanyId();
            }
            return customFieldItem;
        } else {
            return null;
        }
    }

    @Override
    public CompanyCustomFieldItem getCustomFieldByAlias(String entityName, String alias) {
        EdsCompanyCustomFieldsSettings cf = companyCFManager.getByAliasName(entityName, alias);
        return getCustomFieldData(cf.getObjectID(), null);
    }

    @Override
    public void deleteCustomField(Integer objectID, Integer companyID) {
        deleteCustomField(objectID, companyID, null);
    }

    @Override
    public void deleteCustomField(Integer objectID, Integer companyID, String form_id) {
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }

        if (objectID != null) {
            EdsCompanyCustomFieldsSettings customFieldsSettings = companyCFManager.get(objectID);
            // BU EVENT RUN BULGANDA FAQAT CUSTOM FIELD NI DELETED VALUESINI TRUE QILIB QUYADI
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(CustomFieldEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, customFieldsSettings, userManager.getUser());
            event.setCustomStringField(customFieldsSettings.getFieldName());
            removeModelField(customFieldsSettings);
            removePdfTableColumns(customFieldsSettings);

//            String INDEX_TO_SOLR_TYPE = null;
//
//            String customFieldTableName = (String) EdsCompanyCustomFieldsSettings.getCustomFieldTables().get(customFieldsSettings.getEntityName());
//            if (customFieldTableName == null && customFieldsSettings.getEntityCategoryName() != null) {
//                if (customFieldsSettings.getEntityCategoryName().startsWith(Constants.CUSTOM_VIEW)) {
//                    customFieldTableName = "customform_customfields";
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.CustomFormItemTable.name())
//                        && customFieldsSettings.getEntityCategoryName().startsWith(Constants.ITEM_TABLE)) {
//                    customFieldTableName = "custom_item_table_customfields";
//                }
//            }
            // BIZDA CUSTOM FIELD TABLEDAN DATA UCHIRMASLIGI KERE FAQATGINA CUSTOM FIELD LIMIT TULGANDAN KN UCHIRADI HOLOS
            // SHUNING UCHUN CUSTONFIELD TABLEDAN DATA UCHIRADIGAN METHODLARNI KAMENTARIYAGA OLIB QUYDIM
//            if (customFieldTableName != null && !customFieldTableName.isEmpty() && customFieldsSettings.getColumnCode() != null) {
//                if (customFieldsSettings.getEntityName().equals(ViewName.ProductCategory.name()) || customFieldsSettings.getEntityName().equals(ViewName.ProductServiceView.name())) {
////                    companyCFManager.deleteProductCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), customFieldsSettings.getRelationship());
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.Lead.name())) {
////                    companyCFManager.deleteLeadCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
//                    INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CRM_LEAD_TO_SOLR;
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.Candidate.name())) {
////                    companyCFManager.deleteCandidateCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
//                    INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CRM_CANDIDATE_TO_SOLR;
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.Opportunity.name())) {
////                    companyCFManager.deleteOpportunityCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
//                    INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_OPPORTUNITY_TO_SOLR;
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.CrmAccount.name())) {
////                    companyCFManager.deleteCrmAccountCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
//                    INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CRM_ACCOUNT_TO_SOLR;
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.Contact.name())) {
////                    companyCFManager.deleteContactCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
//                    INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CRM_CONTACT_TO_SOLR;
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.CrmCase.name())) {
////                    companyCFManager.deleteCrmCaseCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
//                    INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CRM_CASE_TO_SOLR;
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.SaleInvoice.name())) {
////                    companyCFManager.deleteInvoiceCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), RECEIVABLE);
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.PurchaseInvoice.name())) {
//                    companyCFManager.deleteInvoiceCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), PAYABLE);
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.SaleQuote.name())) {
////                    companyCFManager.deleteQuoteCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), RECEIVABLE, false);
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.SaleOrder.name())) {
////                    companyCFManager.deleteQuoteCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), RECEIVABLE, true);
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.PurchaseOrder.name())) {
////                    companyCFManager.deleteQuoteCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), PAYABLE, null);
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.CustomFormItems.name()) && form_id != null) {
////                    companyCFManager.deleteCustomFormCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), form_id);
//                    INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CUSTOM_FORM_ITEM_TO_SOLR;
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.CustomFormItemTable.name())) {
//                    companyCFManager.deleteCustomFormItemTableCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), customFieldsSettings.getEntityCategoryName());
//                    INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CUSTOM_FORM_ITEM_TO_SOLR;
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.Prepayment.name())) {
////                    companyCFManager.deletePrepaymentCustomFieldValues(customFieldTableNcustomFieldsSettingsame, .getColumnCode(), "RECEIVABLE_PREPAYMENT");
//                } else if (customFieldsSettings.getEntityName().equals(ViewName.Supplier.name())) {
////                    companyCFManager.deletePrepaymentCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), "PAYABLE_SUPPLIER_CREDIT");
//                } else {
//                    companyCFManager.deleteCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
//
//                    if (customFieldsSettings.getEntityName().equals(ViewName.Task.name())) {
//                        INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_TASK_TO_SOLR;
//                    } else if (customFieldsSettings.getEntityName().equals(ViewName.Project.name())) {
//                        INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_PROJECT_TO_SOLR;
//                    }
//                }
//
//                // SOLRGA HAM REINDEX BERISH KEREMAS SHU NARSA HAM HAR SAFAR CUSTOM FIELD QUSHIB YOKI DELTE QILGANDA SYSTEMANI OSILTIRYAPTI
//                // DELETE CUSTOM FIELD BULGANDA FAQAT DELETED TRUE QILIB QUYAMIZ
////                baseEventPostProcessor.registerEvent(CustomFieldCustomEventListenerImpl.TYPE, INDEX_TO_SOLR_TYPE, null, userManager.getUser());
//            }
            if (Constants.QUICK_ADD_FORMS.contains(customFieldsSettings.getEntityName()) && customFieldsSettings.getRequired() != null && customFieldsSettings.getRequired()) {
                addOrRemoveCFFromQuickAdd(QuickAddSettingsForm.getByFormId(customFieldsSettings.getEntityName()), null, customFieldsSettings.getColumnCode(), false);
            }
        }
    }

    @Override
    public void deleteCustomField(String formID, Integer objectID, Integer companyID) {
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }

        if (objectID != null) {
            EdsCompanyCustomFieldsSettings customFieldsSettings = companyCFManager.get(objectID);
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(CustomFieldEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, customFieldsSettings, userManager.getUser());
            event.setCustomStringField(customFieldsSettings.getFieldName());
            removeModelField(customFieldsSettings);
            removePdfTableColumns(customFieldsSettings);


//            String customFieldTableName = (String) EdsCompanyCustomFieldsSettings.getCustomFieldTables().get(customFieldsSettings.getEntityName());
//            if (customFieldTableName == null) {
//                if (customFieldsSettings.getEntityCategoryName() != null && customFieldsSettings.getEntityCategoryName().startsWith(Constants.CUSTOM_VIEW)) {
//                    customFieldTableName = "customform_customfields";
//                }
//            }
//            if (customFieldTableName != null && !customFieldTableName.isEmpty() && customFieldsSettings.getColumnCode() != null) {
//
//                companyCFManager.deleteCustomFormCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), formID);
//
//                EdsBusinessEvent event1 = baseEventPostProcessor.registerEvent(CustomFieldCustomEventListenerImpl.TYPE, CustomFieldCustomEventListenerImpl.EVENT_INDEX_CUSTOM_FORM_ITEM_TO_SOLR, null, userManager.getUser());
//                event1.setCustomStringField(formID);
//            }
        }
    }

    @Override
    public void clearFromDbDeletedCustomFieldsByFormId(String formId, String entityCategoryName, Boolean withSolrReindex) {
        System.out.println("================================= " + formId + " ==================================== ");
        if (formId == null) {
            return;
        }
        EdsModel model = modelManager.get(formId);
        String viewName = null;
        if (model != null) {
            viewName = model.isCustomForm() ? ViewName.CustomFormItems.name() : model.getViewName();
        }
        if (model == null) {
            return;
        }
        modelFieldManager.clearAllDeletedModelFields(formId);

        String INDEX_TO_SOLR_TYPE = null;

        String customFieldTableName = (String) EdsCompanyCustomFieldsSettings.getCustomFieldTables().get(viewName);

        if (customFieldTableName == null && entityCategoryName != null) {
            if (entityCategoryName.startsWith(Constants.CUSTOM_VIEW)) {
                customFieldTableName = "customform_customfields";
                viewName = ViewName.CustomFormItems.name();
            }
        }

        List<EdsCompanyCustomFieldsSettings> deletedItemsList = null;
        if (model.isCustomForm()) {
            deletedItemsList = companyCFManager.getAllDeletedCustomFieldsByViewName(ViewName.CustomFormItems.name(), model.getViewName());
        } else {
            deletedItemsList = companyCFManager.getAllDeletedCustomFieldsByViewName(viewName, null);
        }

        if (viewName.equals(ViewName.ProductCategory.name()) || viewName.equals(ViewName.ProductServiceView.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteProductCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), customFieldsSettings.getRelationship());
            }
        } else if (viewName.equals(ViewName.Lead.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteLeadCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
            }
            INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CRM_LEAD_TO_SOLR;
        } else if (viewName.equals(ViewName.Candidate.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteCandidateCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
            }
            INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CRM_CANDIDATE_TO_SOLR;
        } else if (viewName.equals(ViewName.Opportunity.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteOpportunityCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
            }
            INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_OPPORTUNITY_TO_SOLR;
        } else if (viewName.equals(ViewName.CrmAccount.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteCrmAccountCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
            }
            INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CRM_ACCOUNT_TO_SOLR;
        } else if (viewName.equals(ViewName.Contact.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteContactCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
            }
            INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CRM_CONTACT_TO_SOLR;
        } else if (viewName.equals(ViewName.CrmCase.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteCrmCaseCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
            }
            INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CRM_CASE_TO_SOLR;
        } else if (viewName.equals(ViewName.SaleInvoice.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteInvoiceCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), RECEIVABLE);
            }
        } else if (viewName.equals(ViewName.PurchaseInvoice.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteInvoiceCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), PAYABLE);
            }
        } else if (viewName.equals(ViewName.SaleQuote.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteQuoteCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), RECEIVABLE, false);
            }
        } else if (viewName.equals(ViewName.SaleOrder.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteQuoteCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), RECEIVABLE, true);
            }
        } else if (viewName.equals(ViewName.PurchaseOrder.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteQuoteCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), PAYABLE, null);
            }
        } else if (viewName.equals(ViewName.CustomFormItems.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteCustomFormCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), formId);
            }
            INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_CUSTOM_FORM_ITEM_TO_SOLR;
        } else if (viewName.equals(ViewName.Prepayment.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deletePrepaymentCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), "RECEIVABLE_PREPAYMENT");
            }
        } else if (viewName.equals(ViewName.Supplier.name())) {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deletePrepaymentCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode(), "PAYABLE_SUPPLIER_CREDIT");
            }
        } else {
            for (EdsCompanyCustomFieldsSettings customFieldsSettings : deletedItemsList) {
                companyCFManager.deleteCustomFieldValues(customFieldTableName, customFieldsSettings.getColumnCode());
            }
            if (viewName.equals(ViewName.Task.name())) {
                INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_TASK_TO_SOLR;
            } else if (viewName.equals(ViewName.Project.name())) {
                INDEX_TO_SOLR_TYPE = CustomFieldCustomEventListenerImpl.EVENT_INDEX_PROJECT_TO_SOLR;
            }
        }
        clearDeletedCustomFields(deletedItemsList, false);

        removeCustomItemTableDeletedCFItems(formId, withSolrReindex);

        if (withSolrReindex) {
            baseEventPostProcessor.registerEvent(CustomFieldCustomEventListenerImpl.TYPE, INDEX_TO_SOLR_TYPE, null, userManager.getUser());
        }
    }

    private void removeCustomItemTableDeletedCFItems(String formId, Boolean withSolrReindex) {
        List<EdsCFItemTableSetting> itsList = this.cfItemTableSettingmanager.findByFormId(formId);
        for (EdsCFItemTableSetting itemTableSettigns : itsList) {
            List<EdsCompanyCustomFieldsSettings> deletedItemsList = companyCFManager.getAllDeletedCustomFieldsByViewName(ViewName.CustomFormItemTable.name(), itemTableSettigns.getUuid());
            if (Utils.isOk(deletedItemsList)) {
                clearDeletedCustomFields(deletedItemsList, true);
            }
        }
        if (withSolrReindex && Utils.isOk(itsList)) {
            baseEventPostProcessor.registerEvent(CustomFieldCustomEventListenerImpl.TYPE, CustomFieldCustomEventListenerImpl.EVENT_INDEX_CUSTOM_FORM_ITEM_TO_SOLR, null, userManager.getUser());
        }
    }

    private void clearDeletedCustomFields(List<EdsCompanyCustomFieldsSettings> deletedItemsList, boolean fromItemTable) {
        List<Integer> deletedCustomFieldIds = deletedItemsList.stream().map(customField -> {
            if (fromItemTable) {
                companyCFManager.deleteCustomFormItemTableCustomFieldValues("custom_item_table_customfields", customField.getColumnCode(), customField.getEntityCategoryName());
            }
            return customField.getObjectID();
        }).collect(Collectors.toList());
        companyCFManager.clearAllDeletedFields(deletedCustomFieldIds);
    }

    @Override
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployees() {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> employeeList = new LinkedHashMap<>();
        List<EdsEmployee> employees;
        KpiTreeInfo key;
        boolean team;
        employees = employeeManager.getCompanyEmployees();
        for (EdsEmployee employee : employees) {
            team = false;
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(employee);
            key = new KpiTreeInfo();
            key.setId(employee.getObjectID() != null ? employee.getObjectID() : 0);
            key.setEmployeeId(employee.getObjectID() != null ? employee.getObjectID() : 0);
            key.setName(employee.getName() != null ? employee.getName() : "");
            if (employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getObjectID() != null) {
                key.setDepartmentId(employee.getTeam().getObjectID());
                key.setDepartmentName(employee.getTeam().getName() != null ? employee.getTeam().getName() : "");
                key.setSelected(userSettings.isTimesheetrequired());

                for (KpiTreeInfo s : employeeList.keySet()) {
                    if (s.getId().equals(employee.getTeam().getObjectID())) {
                        team = true;
                        employeeList.get(s).add(key);
                        break;
                    }
                }

                if (!team) {
                    KpiTreeInfo department = new KpiTreeInfo(employee.getTeam().getObjectID(), employee.getTeam().getName());
                    ArrayList<KpiTreeInfo> list = new ArrayList<>();
                    list.add(key);
                    employeeList.put(department, list);
                }
            }
        }
        return employeeList;
    }

    @Override
    public Boolean saveTimesheetRequired(ArrayList<KpiTreeInfo> selectedItems) {
        StringBuilder buffer = new StringBuilder();
        for (KpiTreeInfo selectedItem : selectedItems) {
            buffer.append(selectedItem.getEmployeeId()).append(",");
        }
        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
            employeeManager.updateRequired(buffer.toString());
        } else if (buffer.length() == 0) {
            employeeManager.updateAllRequired();
        }
        return true;
    }

    @Override
    public String getProjectLastIntNumber() {
        Integer intNumber = projectManager.getProjectLastIntNumber();
        if (intNumber == null) {
            intNumber = 0;
        }
        return decimalFormat.format(intNumber);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ConsolidationCompanyItem getConsolidationCompanyItems() {
        ConsolidationCompanyItem consolidationCompanyItem = new ConsolidationCompanyItem();
        consolidationCompanyItem.setCountryItem(commonService.getCountries());
        consolidationCompanyItem.setCurrencyItem(invoiceService.getCurrencies(null));
        consolidationCompanyItem.setBaseCurrency(invoiceService.getBaseCurrency());
        SelectItem[] stateItems = commonService.getRegions();
        for (SelectItem item : stateItems) {
            Integer key = Integer.valueOf(item.getDescription());
            if (!consolidationCompanyItem.getStatesMap().containsKey(key)) {
                consolidationCompanyItem.getStatesMap().put(key, new ArrayList<>());
            }
            consolidationCompanyItem.getStatesMap().get(key).add(item);
        }
        return consolidationCompanyItem;
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public KeyValueStruct saveConsolidationCompany(ConsolidationCompanySaveItem consolidationCompanySaveItem) {
        KeyValueStruct result = new KeyValueStruct();
        NewCompany newCompany = new NewCompany();
        newCompany.setAdminFName(consolidationCompanySaveItem.getFirstName());
        newCompany.setAdminLName(consolidationCompanySaveItem.getLastName());
        newCompany.setAdminEmail(consolidationCompanySaveItem.getEmail());
        newCompany.setName(consolidationCompanySaveItem.getCompanyName());
        newCompany.setPhone(consolidationCompanySaveItem.getPhone());
        newCompany.setCountryID(consolidationCompanySaveItem.getCountryId());
        newCompany.setActive(true);
        if (consolidationCompanySaveItem.getBaseCurrency() != null) {
            newCompany.setCurrencyID(consolidationCompanySaveItem.getBaseCurrency().getId());
        }
        newCompany.setHost(consolidationCompanySaveItem.getHost());
        newCompany.setCompanySignedUpFrom(SIGNED_UP_FROM_SUBSIDIARIES);
        newCompany.setLocale(Locale.ENGLISH.getLanguage());

        EdsFinancialSettings edsFinancialSettings = financialSettingsManager.getFinancialSettings();
        if (edsFinancialSettings == null) {
            result.setKey(FINANCIALSETTINGS_NOT_FOUND);
            result.setType(ERROR);
        }
        EdsCompany parentCompany = userManager.getUser().getCompany();
        Integer parentCompanyId = parentCompany.getObjectID();
        String parentClusterType = SecurityContext.getInstance().getDatabase();

        // Subsidiaries Company List
        List<SelectItem> subsidiariesCompanyList = accountingServiceLocal.getSubsidiariesCompanyList();
        subsidiariesCompanyList.add(new SelectItem(parentCompany.getObjectID(), parentCompany.getName(), edsFinancialSettings.getCurrency().getObjectID().toString()));

        // multi currency option setup to parent company
        backendServiceLocal.enableDisableGenericSettings(parentCompanyId, GenericSettingsEnum.MULTICURRENCY_ENABLED, true);

        log.info("Free SigpUp Subsidiaries Company :>>> " + newCompany);
        SecurityContext.getInstance().removeCompanyId();
        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);

        Integer objectID = signupServiceLocal.getCompany();
        newCompany.setCompanyId(objectID);

        try {
            backendServiceLocal.createSchemaByID(objectID, null);
        } catch (Exception e) {
            log.error("", e);
        }

        CreatedCompany comID = signupServiceLocal.createCompany(newCompany);
        if (comID != null && comID.getCompanyId() != null) {
            try {
                SecurityContext.getInstance().setDatabase(Constants.DATABASE_PAID);
                crmServiceLocal.createLeadFromSignUpper(NewCompany.toString(newCompany));
            } catch (Exception e) {
                log.error("", e);
            }
        }

        if (comID != null && comID.getCompanyId() != null) {
            SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
            SecurityContext.getInstance().setCompanyId(comID.getCompanyId());
            SecurityContext.getInstance().setStaticUserID(comID.getAdminId());

            // subsidirie company id,name
            SelectItem currenctSubsidirieCompany = new SelectItem(comID.getCompanyId(), consolidationCompanySaveItem.getCompanyName(), consolidationCompanySaveItem.getBaseCurrency().getId().toString());
            SettingsData settingsData = consolidationCompanySaveItem.getSettingsData();
            settingsData.setOverallDatePickerWeekStart(2);
            // save company infomation data
            commonServiceLocal.saveCompanyParent(comID.getCompanyId(), parentCompanyId);
            // accounting getting started
            settingsData.setCompanyName(consolidationCompanySaveItem.getCompanyName());
            settingsData.setCurrencyID(consolidationCompanySaveItem.getBaseCurrency().getId());
            settingsData.setFinancialYearEnd(new DateNonConvertable(edsFinancialSettings.getFinancialYearEnd()));
            settingsData.setConversionDate(edsFinancialSettings.getConversionDate());
            accountingServiceLocal.completeAccountingGettingStarted(settingsData, true);
            // send Multi currency to parent company
            List<Integer> currencyIdList = new ArrayList<>();
            for (SelectItem currency : consolidationCompanySaveItem.getOperatingCurrencies()) {
                currencyIdList.add(currency.getId());
                // create child company new currencies
                if (!currency.getId().equals(consolidationCompanySaveItem.getBaseCurrency().getId())) {
                    currencyService.createCurrency(currency.getId());
                }
            }
            //currencyIdList.add(consolidationCompanySaveItem.getBaseCurrency().getId());
            // create company multi currency
            accountingServiceLocal.createCompanyMultiCurrency(currencyIdList, null);
            // multi currency option setup
            backendServiceLocal.enableDisableGenericSettings(comID.getCompanyId(), GenericSettingsEnum.MULTICURRENCY_ENABLED, true);
            // set parentId to child company
            globalAuthJdbcSpringManager.updateClusterCompanyParent(comID.getCompanyId(), parentCompanyId);

            // send to active mq syncronize data with parent table
            rabbitMQService.sendMultiCurrency(new MultiCurrencyItemMQ(comID.getCompanyId(), currencyIdList), parentCompanyId, parentClusterType);
            // send to activemq subsidiries companay
            rabbitMQService.sendSubsidiaries(subsidiariesCompanyList, currenctSubsidirieCompany, parentCompanyId);
        }
        List<SelectItem> currenctSubsidiaryList = new ArrayList<>();
        currenctSubsidiaryList.add(new SelectItem(comID.getCompanyId(), consolidationCompanySaveItem.getCompanyName(), consolidationCompanySaveItem.getBaseCurrency().getId().toString()));
        SecurityContext.getInstance().setDatabase(parentClusterType);
        SecurityContext.getInstance().setCompanyId(parentCompanyId);
        accountingServiceLocal.saveSubsidiariesCompany(currenctSubsidiaryList);
        result.setType(SUCCESS);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ConsolidationCompanyList> getSubsidiariesCompanyList(ListingFilterParameter filterParametrs) {
        EdsCompany edsCompany = userManager.getUser().getCompany();
        List<ConsolidationCompanyList> companyConsolidations = globalAuthJdbcSpringManager.getSubsidiariesCompany(edsCompany.getObjectID());
        Map<String, StringBuffer> companyClusterMap = new HashMap<>();
        Map<Integer, ConsolidationCompanyList> companyCosolidationMap = new HashMap<>();
        for (ConsolidationCompanyList consolidation : companyConsolidations) {
            if (!companyClusterMap.containsKey(consolidation.getDataBaseName())) {
                companyClusterMap.put(consolidation.getDataBaseName(), new StringBuffer());
            }
            if (!"".contentEquals(companyClusterMap.get(consolidation.getDataBaseName()))) {
                companyClusterMap.get(consolidation.getDataBaseName()).append(",");
            }
            companyClusterMap.get(consolidation.getDataBaseName()).append(consolidation.getCompanyId());
            companyCosolidationMap.put(consolidation.getCompanyId(), consolidation);
        }
        String currentCompanyID = SecurityContext.getInstance().getCompanyId();
        String currentCompanyDataBase = SecurityContext.getInstance().getDatabase();
        SecurityContext.getInstance().removeCompanyId();
        List<ConsolidationCompanyList> consolidationCompanyLists = new ArrayList<>();
        for (String keyDBcluster : companyClusterMap.keySet()) {
            SecurityContext.getInstance().setDatabase(keyDBcluster);
            List<ConsolidationCompanyList> list = commonServiceLocal.getConsolidationCompanyList(companyCosolidationMap, companyClusterMap.get(keyDBcluster).toString(), keyDBcluster);
            if (list != null && list.size() > 0) {
                consolidationCompanyLists.addAll(list);
            }
        }
        SecurityContext.getInstance().setCompanyId(currentCompanyID);
        SecurityContext.getInstance().setDatabase(currentCompanyDataBase);
        Collections.sort(consolidationCompanyLists);
        int start = filterParametrs.getStart();
        int end = Math.min(filterParametrs.getLimit() + start, consolidationCompanyLists.size());
        return new ListResult<>(new ArrayList<>(consolidationCompanyLists.subList(start, end)), companyConsolidations.size());
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public Boolean activeSubsidiarieCompany(ConsolidationCompanyList rowValue) {
        SecurityContext.getInstance().removeCompanyId();
        SecurityContext.getInstance().setDatabase(rowValue.getDataBaseName());
        commonServiceLocal.activeCompany(rowValue);
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCustomFieldDataByQuery(Integer companyID, String query) {
        return companyCFManager.getCustomFieldDataByQuery(companyID, query);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<SelectItem> getRoles() {
        ArrayList<SelectItem> result = new ArrayList<>();
        List<EdsRole> roles = roleManager.list();
        for (EdsRole role : roles) {
            role.setName(commonLocalizer.localize(role.getCode(), role.getName()));
            result.add(role.getAsSelectItem());
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<WorkflowRule> listWorkflowRules(ListingFilterParameter fp) {
        List<EdsWorkflowRule> list = workflowRuleManager.list(fp);
        Integer totalCount = workflowRuleManager.listCount(fp);
        ArrayList<WorkflowRule> results = new ArrayList<>();
        for (EdsWorkflowRule rule : list) {
            results.add(rule.getRPC(null));
        }
        return new ListResult<>(results, totalCount == null ? 0 : totalCount);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WorkflowRule editWorkflowRule(Integer objectID) {
        EdsWorkflowRule workflowRule = workflowRuleManager.get(objectID);
        if (workflowRule == null) {
            workflowRule = new EdsWorkflowRule();
        }
        WorkflowRule item = new WorkflowRule();
        List<EdsReference> modules = referenceManager.listReferences(WorkflowRule._WORKFLOW_MODULE);
        modules.forEach(m -> m.setName(localizeWorkflowModule(m.getCode(), m.getName())));
        modules.sort(Comparator.comparing(EdsReference::getName));
        item.setModules(ServerUtils.getAsSelectItem(modules, ServerUtils.REFERENCE));
        if (workflowRule.getRecurrenceID() != null) {
            EdsRecurrence recurrence = recurrenceManager.get(workflowRule.getRecurrenceID());
            if (recurrence != null) {
                item.setRecurrenceID(recurrence.getObjectID());
                RecurrenceJobItem recurrenceJobItem = recurrence.createRecurrenceItem(SchedulerConstant.RECURRING_WORKFLOW);
                recurrenceJobItem.setEnabled(true);
                item.setRecurrenceJobItem(recurrenceJobItem);
            }
        }
        item = workflowRule.getRPC(item);
        boolean notForStep = WorkflowRule._WORKFLOW_MODULE_CASE.equals(item.getModule()) ||
                _WORKFLOW_MODULE_LEAD.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_CONTACT.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_ACTIVITY.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_LOGACALL.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_CS_STUDENT.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_SCHEDULED_COURSE.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_SALEQUOTE.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_PURCHASE.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_PURCHASEORDER.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_OPPORTUNITY.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_PROJECT.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_ACCOUNT.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_QUOTE.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_PURCHASE_INVOICE.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_SALEORDER.equals(item.getModule()) ||
                WorkflowRule._WORKFLOW_MODULE_VACANCY.equals(item.getModule());
        if (!notForStep) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setAllByFilter(true);
            item.setOnboardingSteps(getApproverModules(fp));
        }
        return item;
    }

    private String localizeWorkflowModule(String code, String defaultName) {
        switch (code) {
            case _WORKFLOW_MODULE_LEAD -> {
                return commonLocalizer.localize("lead");
            }
            case _WORKFLOW_MODULE_CANDIDATE -> {
                return commonLocalizer.localize("candidate");
            }
            case _WORKFLOW_MODULE_CONTACT -> {
                return commonLocalizer.localize("contact");
            }
            case _WORKFLOW_MODULE_CASE -> {
                return referenceWfmMessageSource.localize("ET_CASE_MODULE");
            }
            case _WORKFLOW_MODULE_ACTIVITY -> {
                return commonLocalizer.localize("activity");
            }
            case _WORKFLOW_MODULE_LOGACALL -> {
                return commonLocalizer.localize("logCall");
            }
            case _WORKFLOW_MODULE_CS_STUDENT -> {
                return commonLocalizer.localize("student");
            }
            case _WORKFLOW_MODULE_HRMS_EMPLOYEE -> {
                return commonLocalizer.localize("employee");
            }
            case _WORKFLOW_MODULE_SALE_INVOICE -> {
                return commonLocalizer.localize("saleinvoice");
            }
            case _WORKFLOW_MODULE_SALEQUOTE -> {
                return commonLocalizer.localize("saleQuoteOrder");
            }
            case _WORKFLOW_MODULE_REQUEST_FOR_PURCHASE -> {
                return commonLocalizer.localize("requestForPurchase");
            }
            case _WORKFLOW_MODULE_PURCHASEORDER -> {
                return commonLocalizer.localize("purchaseorder");
            }
            case _WORKFLOW_MODULE_PAYRUN -> {
                return accountingLocalizer.localize("singlePayruns");
            }
            case _WORKFLOW_MODULE_CASH_ADVANCE -> {
                return commonLocalizer.localize("cashAdvance");
            }
            case _WORKFLOW_MODULE_SICK_REQUEST -> {
                return commonLocalizer.localize("leaveRequest");
            }
            case _WORKFLOW_MODULE_OPPORTUNITY -> {
                return commonLocalizer.localize("opportunity");
            }
            case _WORKFLOW_MODULE_EXPENSE_CLAIM -> {
                return commonLocalizer.localize("expenseClaim");
            }
            case _WORKFLOW_MODULE_ADDITIONAL_PAYMENT -> {
                return commonLocalizer.localize("additionalPayment");
            }
            case _WORKFLOW_MODULE_CERTIFICATE -> {
                return commonLocalizer.localize("certificate");
            }
            case _WORKFLOW_MODULE_PROJECT -> {
                return commonLocalizer.localize("project");
            }
            case _WORKFLOW_MODULE_PRODUCT -> {
                return accountingLocalizer.localize("productOrService");
            }
            case _WORKFLOW_MODULE_ACCOUNT -> {
                return commonLocalizer.localize("accountS");
            }
            case _WORKFLOW_MODULE_MANUAL_JOURNAL -> {
                return commonLocalizer.localize("manualEntry");
            }
            case _WORKFLOW_MODULE_GDN -> {
                return commonLocalizer.localize("gdn");
            }
            case _WORKFLOW_MODULE_PICKLIST -> {
                return commonLocalizer.localize("picklist");
            }
            case _WORKFLOW_MODULE_TASK -> {
                return commonLocalizer.localize("task");
            }
            case _WORKFLOW_MODULE_REQUEST_FOR_QUOTE -> {
                return commonLocalizer.localize("requestForQuotes");
            }
            case _WORKFLOW_MODULE_PURCHASE_INVOICE -> {
                return commonLocalizer.localize("purchaseinvoice");
            }
            case _WORKFLOW_MODULE_GROUP_GOAL -> {
                return hrmsLocalizer.localize("groupGoal");
            }
            case _WORKFLOW_MODULE_SALEORDER -> {
                return commonLocalizer.localize("saleorder");
            }
            case _WORKFLOW_MODULE_STOCK_TRANSFER -> {
                return commonLocalizer.localize("stockTransfer");
            }
            case _WORKFLOW_MODULE_STOCK_ADJUSTMENT -> {
                return commonLocalizer.localize("stockAdjustment");
            }
            case _WORKFLOW_MODULE_VACANCY -> {
                return commonLocalizer.localize("vacancy");
            }
            case _WORKFLOW_MODULE_DEPARTMENT -> {
                return commonLocalizer.localize("department");
            }
            case _WORKFLOW_MODULE_POSITION -> {
                return commonLocalizer.localize("position");
            }
            case _WORKFLOW_MODULE_RENTAL_ORDER -> {
                return commonLocalizer.localize("rentalOrders");
            }
            case _WORKFLOW_MODULE_BUILD_ASSEMBLY -> {
                return commonLocalizer.localize("buildAssembly");
            }
            case _WORKFLOW_MODULE_RENTAL_PRODUCT -> {
                return commonLocalizer.localize("rentalProducts");
            }
            case _WORKFLOW_MODULE_RECEIVE_PAYMENT -> {
                return commonLocalizer.localize("recivePayments");
            }
            case _WORKFLOW_MODULE_PAY_INVOICE -> {
                return commonLocalizer.localize("payBill");
            }
            case _WORKFLOW_MODULE_INCIDENT -> {
                return commonLocalizer.localize("incident");
            }
            case _WORKFLOW_MODULE_PLACEMENT -> {
                return commonLocalizer.localize("placement");
            }
            case _WORKFLOW_MODULE_SHIFT -> {
                return commonLocalizer.localize("shift");
            }
            case _WORKFLOW_MODULE_ROTATION -> {
                return hrmsLocalizer.localize("rotations");
            }
            case _WORKFLOW_MODULE_PRODUCT_CATEGORY -> {
                return commonLocalizer.localize("productCategory");
            }
            case _WORKFLOW_MODULE_OVERTIME -> {
                return commonLocalizer.localize("overtime");
            }
            case _WORKFLOW_MODULE_CREDIT_NOTE -> {
                return accountingLocalizer.localize("creditNote");
            }
            case _WORKFLOW_MODULE_DEBIT_NOTE -> {
                return accountingLocalizer.localize("debitNote");
            }
            case _WORKFLOW_MODULE_BACKUP_EMPLOYEE -> {
                return accountingLocalizer.localize("backupEmployee");
            }
            default -> {
                return defaultName;
            }
        }
    }

    @Override
    @Transactional
    public Integer saveWorkflowRule(WorkflowRule item) {
        EdsWorkflowRule workflowRule = item.getObjectID() != null ? workflowRuleManager.get(item.getObjectID()) : new EdsWorkflowRule();
        if (workflowRule.getCreator() == null) {
            workflowRule.setCreator(userManager.getUser());
        }
        workflowRule.setName(item.getName());
        workflowRule.setModule(item.getModule());
        workflowRule.setActive(item.isActive());
        workflowRule.setDescription(item.getDescription());
        workflowRule.setExecutionCriteria(item.getExecutionCriteria());
        workflowRule.setExecutionCriteriaUpdateField(item.getExecutionCriteriaUpdateField());
        workflowRule.setRuleCriteria(item.getRuleCriteria());
        workflowRule.getConditions().clear();
        workflowRule.setPattern(item.getPattern());
        workflowRule.setDynamicCondition(item.isDynamicCondition());
        workflowRule.setDynamicConditionQuery(item.getDynamicConditionQuery());
        workflowRuleManager.createOrUpdate(workflowRule);
        if (WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_RECURRENCE.equals(item.getExecutionCriteria())) {
            if (item.getRecurrenceID() != null) {
                EdsRecurrence oldRecurrence = recurrenceManager.get(item.getRecurrenceID());
                if (item.getRecurrenceJobItem() != null) {
                    if (oldRecurrence != null) {
                        recurrenceService.wrapRecurrenceJobItemToEdsRecurrence(item.getRecurrenceJobItem(), oldRecurrence, recurrenceJobManager.get(SchedulerConstant.RECURRING_WORKFLOW));
                        recurrenceManager.update(oldRecurrence);
                    }
                }
            } else {
                RecurrenceJobItem jobItem = item.getRecurrenceJobItem();
                if (jobItem != null) {
                    jobItem.setBusObjectId(workflowRule.getObjectID());
                    jobItem.setJobType(SchedulerConstant.RECURRING_WORKFLOW);
                    workflowRule.setRecurrenceID(recurrenceService.saveRecurrenceJob(jobItem));
                }
            }
        }
        if (item.getConditions().size() > 0) {
            for (Map.Entry<Integer, WorkflowCondition> entry : item.getConditions().entrySet()) {
                if (entry.getValue() != null) {
                    EdsWorkflowCondition edsWorkflowCondition = EdsWorkflowCondition.fromRPC(entry.getValue().getObjectID() != null ? workflowConditionManager.get(entry.getValue().getObjectID()) : new EdsWorkflowCondition(), entry.getValue());
                    edsWorkflowCondition.setWorkflow(workflowRule);
                    workflowConditionManager.createOrUpdate(edsWorkflowCondition);
                    workflowRule.getConditions().add(edsWorkflowCondition);
                }
            }
            workflowRule.setPattern(item.getPattern());
        }
        return workflowRule.getObjectID();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<WorkflowAlert> listWorkflowAlerts(ListingFilterParameter filterParametr) {
        List<EdsWorkflowAlert> alerts = workflowAlertManager.list(filterParametr);
        ArrayList<WorkflowAlert> result = new ArrayList<>();
        if (alerts != null && alerts.size() > 0) {
            for (EdsWorkflowAlert alert : alerts) {
                if (alert != null) {
                    result.add(alert.getRPC(null));
                }
            }
        }
        return new ListResult<>(result, result.size());
    }

    @Override
    public ListResult<WorkflowSMSAlert> listWorkflowSMSAlerts(ListingFilterParameter filterParametr) {
        List<EdsWorkflowSMSAlert> alerts = workflowSMSAlertManager.list(filterParametr);
        ArrayList<WorkflowSMSAlert> result = new ArrayList<>();
        if (alerts != null && alerts.size() > 0) {
            for (EdsWorkflowSMSAlert alert : alerts) {
                if (alert != null) {
                    result.add(alert.getRPC(null));
                }
            }
        }
        return new ListResult<>(result, result.size());
    }


    @Override
    public ListResult<WorkflowTelegramAlert> listWorkflowTelegramAlerts(ListingFilterParameter filterParametr) {
        List<EdsWorkflowTelegramAlert> alerts = workflowTelegramAlertManager.list(filterParametr);
        ArrayList<WorkflowTelegramAlert> result = new ArrayList<>();
        if (alerts != null && alerts.size() > 0) {
            for (EdsWorkflowTelegramAlert alert : alerts) {
                if (alert != null) {
                    WorkflowTelegramAlert telegramAlert = alert.toRPC();
                    telegramAlert.setTelegramBot(globalAuthJdbcSpringManager.getTelegramSettingsItem(alert.getTelegramBotId()));
                    result.add(telegramAlert);
                }
            }
        }
        return new ListResult<>(result, result.size());
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<WorkflowUpdateField> listWorkflowUpdateFields(ListingFilterParameter filterParametr) {
        List<EdsWorkflowUpdateField> alerts = workflowUpdateFieldManager.list(filterParametr);
        ArrayList<WorkflowUpdateField> result = new ArrayList<>();
        if (alerts != null && alerts.size() > 0) {
            for (EdsWorkflowUpdateField alert : alerts) {
                if (alert != null) {
                    result.add(alert.getRPC(null));
                }
            }
        }
        return new ListResult<>(result, result.size());
    }

    @Override
    public ListResult<WorkflowAction> listWorkflowActions(ListingFilterParameter filterParametr) {
        List<EdsWorkflowAction> actions = workflowActionManager.list(filterParametr);
        ArrayList<WorkflowAction> result = new ArrayList<>();
        if (actions != null && actions.size() > 0) {
            for (EdsWorkflowAction action : actions) {
                if (action != null) {
                    result.add(action.getRPC(null));
                }
            }
        }
        return new ListResult<>(result, result.size());
    }

    @Override
    public ListResult<WorkflowWebHookListItem> getWorkflowWebHooks(ListingFilterParameter fp) {
        List<EdsWorkflowWebHook> webHooks = workflowWebHookManager.list(fp);
        ArrayList<WorkflowWebHookListItem> result = new ArrayList<>();
        if (webHooks != null && webHooks.size() > 0) {
            for (EdsWorkflowWebHook webHook : webHooks) {
                if (webHook != null) {
                    result.add(webHook.getRpc(false, null, null));
                }
            }
        }
        return new ListResult<>(result, result.size());
    }

    @Override
    public WorkflowAlert editWorkflowAlert(Integer objectID, Integer workflowID) {
        EdsWorkflowAlert alert = objectID != null ? workflowAlertManager.get(objectID) : new EdsWorkflowAlert();
        if (alert == null) {
            alert = new EdsWorkflowAlert();
        }
        if (alert.isNew()) {
            alert.setWorkflow(workflowID != null ? workflowRuleManager.get(workflowID) : null);
        }
        WorkflowAlert item = new WorkflowAlert();
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        ArrayList<SelectItem> emailTemplates = new ArrayList<>();
        if (alert.getWorkflow() != null && alert.getWorkflow().getModule() != null) {
            if (WorkflowRule._WORKFLOW_MODULE_CASE.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_CASE_MODULE);
            } else if (_WORKFLOW_MODULE_LEAD.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_LEAD_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_CONTACT.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_CONTACT_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_ACTIVITY.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_EVENT_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_LOGACALL.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_CALL_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_PAYRUN.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_PAYROLL_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_CASH_ADVANCE.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_CASH_ADVANCE_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_INVOICE_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_SALEQUOTE.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_SALES_QUOTE_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_PURCHASE.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_RFP_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_QUOTE.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_RFQ_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_PURCHASEORDER.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_PURCHASE_ORDER_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_EMPLOYEE_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_OPPORTUNITY.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_OPPORTUNITY_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_EXPENSE_CLAIM.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_EXPENSE_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_ADDITIONAL_PAYMENT.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_ADDITIONAL_PAYMENT_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_PROJECT.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_PROJECT_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_ACCOUNT.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_CRM_ACCOUNT_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_TASK.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_TASK_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_SICK_REQUEST.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_LR_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_CANDIDATE.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule(ET_CANDIDATE_MODULE);
            } else if (WorkflowRule._WORKFLOW_MODULE_PLACEMENT.equals(alert.getWorkflow().getModule())) {
                filterParameter.setModule("ET_PLACEMENT_MODULE");
            }
            if (filterParameter.getModule() != null) {
                List<EdsEmailTemplate> templates = emailTemplateManager.getCompanyEmailTemplates(filterParameter);
                if (templates != null && templates.size() > 0) {
                    for (EdsEmailTemplate template : templates) {
                        emailTemplates.add(template.getAsSelectItem());
                    }
                }
            }
        }
        item.setEmailTemplates(emailTemplates.toArray(new SelectItem[]{}));
        item.setFromUsers(messageCenterServiceLocal.getUserEmailAccounts(true));
        return alert.getRPC(item);
    }

    @Override
    public WorkflowSMSAlert getWorkflowSMSAlert(Integer objectID, Integer workflowID) {
        EdsWorkflowSMSAlert alert = objectID != null ? workflowSMSAlertManager.get(objectID) : new EdsWorkflowSMSAlert();
        if (alert.isNew()) {
            alert.setWorkflow(workflowID != null ? workflowRuleManager.get(workflowID) : null);
        }
        WorkflowSMSAlert item = new WorkflowSMSAlert();
        if (alert.getWorkflow() != null && alert.getWorkflow().getModule() != null) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setModule(alert.getWorkflow().getModule());
            List<EdsSMSTemplates> templates = smsTemplateManager.getSMSTemplates(filterParameter);
            item.setSmsTemplates(templates.stream().map(EdsSMSTemplates::getAsSelectItem).toList().toArray(new SelectItem[]{}));
        }
        List<EdsSmsSettings> smsSettingses = smsManager.list(new ListingFilterParameter());
        item.setProviders(smsSettingses.stream().map(EdsSmsSettings::getAsSelectItem).toList().toArray(new SelectItem[]{}));
        return alert.getRPC(item);
    }

    @Override
    public WorkflowTelegramAlert getWorkflowTelegramAlert(Integer objectID, Integer workflowID) {
        EdsWorkflowTelegramAlert alert = objectID != null ? workflowTelegramAlertManager.get(objectID) : new EdsWorkflowTelegramAlert();
        if (alert != null && alert.isNew()) {
            alert.setWorkflow(workflowID != null ? workflowRuleManager.get(workflowID) : null);
        }
        WorkflowTelegramAlert item = alert.toRPC();
        item.setTelegramBot(globalAuthJdbcSpringManager.getTelegramSettingsItem(alert.getTelegramBotId()));
        SelectItem[] telegramBots = globalAuthJdbcSpringManager.getTelegramSettingItems(new ListingFilterParameter())
                .getList()
                .stream()
                .map(x -> new SelectItem(x.getId(), x.getBotName(), x.getToken()))
                .toArray(SelectItem[]::new);
        item.setTelegramBots(telegramBots);
        item.setReceiverAttributes(alert.getReceiverAttributes());

        return item;
    }

    @Override
    public WorkflowWebHookItem getWorkflowWebHook(WebhookRequestItem item) {
        if (item == null)
            item = new WebhookRequestItem();
        if (item.isPublic()) {
            if (item.getId() == null) {
                return new WorkflowWebHookItem();
            }
            return publicWebhookManager.get(item.getId()).getRpc(true);
        } else {
            EdsWorkflowWebHook webHook = item.getId() != null ? workflowWebHookManager.get(item.getId()) : new EdsWorkflowWebHook();
            if (item.getWorkflowId() != null) {
                webHook.setWorkflow(item.getWorkflowId() != null ? workflowRuleManager.get(item.getWorkflowId()) : null);
            }
            List<EdsPublicWebHook> publicWebHooks = publicWebhookManager.list(null);
            List<SelectItem> templates = new ArrayList<>();
            if (publicWebHooks != null) {
                templates = publicWebHooks.stream().map(w -> new SelectItem(w.getObjectID(), w.getName())).collect(Collectors.toList());
            }
            List<String> itemTableColumns = null;
            if (item.isItemTable()) {
                if (item.getUuid() != null) {
                    EdsCFItemTableSetting setting = cfItemTableSettingmanager.findByUUID(item.getUuid());
                    webHook.setForm((EdsModelCustom) modelManager.get(setting.getCustomForm(), true));
                } else {
                    String formId = null;
                    switch (ItemTableEnum.valueOf(item.getFormId())) {
                        case SALE_INVOICE_ITEM, CREDIT_NOTE_ITEM, DEBIT_NOTE_ITEM ->
                                formId = LayoutRPC.SALEINVOICE_FORM;
                        case SALE_ORDER_ITEM -> formId = LayoutRPC.SALEORDER_FORM;
                        case SALE_QUOTE_ITEM -> formId = LayoutRPC.SALEQUOTE_FORM;
                        case PURCHASE_INVOICE_ITEM -> formId = LayoutRPC.PURCHASEINVOICE_FORM;
                        case PURCHASE_ORDER_ITEM -> formId = LayoutRPC.PURCHASEORDER_FORM;
                        case OPPORTUNITY_SUB_ITEM -> formId = LayoutRPC.OPPORTUNITY_FORM;
                        case RENTAL_ORDER_ITEM -> formId = LayoutRPC.RENTAL_ORDER_FORM;
                        case EXPENSE_CLAIM_ITEM -> formId = LayoutRPC.EXPENSE_CLAIM_FORM;
                        case RFQ_ITEM -> formId = LayoutRPC.REQUEST_FOR_QUOTE_FORM;
                        case RFP_ITEM -> formId = LayoutRPC.REQUEST_FOR_PURCHASE_FORM;
                        case CLIENT_ITEM -> formId = LayoutRPC.CLIENT_FORM;
                        case SUPPLIER_ITEM -> formId = LayoutRPC.SUPPLIER_FORM;
                        case MANUAL_JOURNAL_ITEM -> formId = LayoutRPC.MANUAL_JOURNAL_FORM;
                        case BANK_PAYMENT_ITEM, BANK_RECEIPT_ITEM, CASH_PAYMENT_ITEM, CASH_RECEIPT_ITEM ->
                                formId = LayoutRPC.BANK_TRANSACTION_FORM;
                        case LEAD_ITEM -> formId = LayoutRPC.LEAD_FORM;
                        case VACANCY_ITEM -> formId = LayoutRPC.VACANCY_FORM;
                        case DEPARTMENT_ITEM -> formId = LayoutRPC.DEPARTMENT_FORM;
                        case POSITION_ITEM -> formId = LayoutRPC.POSITION_FORM;
                        case ADDITIONAL_PAYMENT_ITEM -> formId = LayoutRPC.ADDITIONAL_PAYMENT_FORM;
                        case ROTATION_ITEM_TABLE -> formId = LayoutRPC.ROTATION_FORM;
                        case GROUP_PLACEMENT_ITEM_TABLE -> formId = LayoutRPC.GROUP_PLACEMENT_FORM;
                        case EXPERIENCE_ITEM_TABLE -> formId = LayoutRPC.HRMS_EMPLOYEE_FORM;
                        case PICKLIST -> formId = LayoutRPC.PICKLIST_FORM;
                    }
                    webHook.setForm((EdsModelCustom) modelManager.get(formId, true));
                }
                ItemTableSettingsItem settingsItem = itemTableSettingService.getTableSettingsColumnConfigsNew(ItemTableEnum.valueOf(item.getFormId()), item.getUuid());
                if (settingsItem != null && settingsItem.getAllColumns() != null) {
                    itemTableColumns = Arrays.stream(settingsItem.getAllColumns()).map(ColumnConfigs::getCode).collect(Collectors.toList());
                }
            } else if (item.getFormId() != null) {
                webHook.setForm((EdsModelCustom) modelManager.get(item.getFormId(), true));
            }
            return webHook.getRpc(item.getId() != null, itemTableColumns, templates);
        }
    }

    @Override
    public Integer deleteWorkflowTelegramAlert(Integer objectID) {
        EdsWorkflowTelegramAlert alert = workflowTelegramAlertManager.get(objectID);
        if (alert != null) {
            alert.setDeleted(true);
            workflowTelegramAlertManager.update(alert);
            return alert.getObjectID();
        }
        return 0;
    }

    @Override
    public Integer saveWorkflowUpdateField(WorkflowUpdateField item) {
        EdsWorkflowUpdateField edsWorkflowUpdateField = new EdsWorkflowUpdateField();
        if (item != null && item.getObjectID() != null) {
            edsWorkflowUpdateField = workflowUpdateFieldManager.get(item.getObjectID());
        }
        edsWorkflowUpdateField = EdsWorkflowUpdateField.fromRPC(edsWorkflowUpdateField, item);
        workflowUpdateFieldManager.createOrUpdate(edsWorkflowUpdateField);
        return edsWorkflowUpdateField.getObjectID();
    }

    @Override
    public Integer saveWorkflowAction(WorkflowAction action) {
        EdsWorkflowAction edsWorkflowAction = null;
        if (action.getId() != null) {
            edsWorkflowAction = workflowActionManager.get(action.getId());
        }
        edsWorkflowAction = EdsWorkflowAction.fromRPC(edsWorkflowAction, action);
        workflowActionManager.createOrUpdate(edsWorkflowAction);
        for (WorkflowActionItem item : action.getItems()) {
            EdsWorkflowActionItem edsWorkflowActionItem = new EdsWorkflowActionItem();
            if (item.getObjectId() != null) {
                edsWorkflowActionItem = workflowActionItemManager.get(item.getObjectId());
            }
            edsWorkflowActionItem = EdsWorkflowActionItem.fromRPC(edsWorkflowActionItem, item);
            edsWorkflowActionItem.setWorkflowAction(edsWorkflowAction);
            workflowActionItemManager.createOrUpdate(edsWorkflowActionItem);
        }

        return edsWorkflowAction.getObjectID();
    }

    @Override
    public WorkflowAction editWorkflowAction(Integer objectId, Integer workflowId) {
        WorkflowAction action = new WorkflowAction();
        if (workflowId != null) {
            action.setWorkflowId(workflowId);
            EdsWorkflowRule workflowRule = workflowRuleManager.get(workflowId);
            String form_id = null;
            if (WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL.equals(workflowRule.getModule())) {
                form_id = LayoutRPC.MANUAL_JOURNAL_FORM;
            } else if (WorkflowRule._WORKFLOW_MODULE_TASK.equals(workflowRule.getModule())) {
                form_id = LayoutRPC.TASK_MAX_FORM;
            } else {
                form_id = workflowRule.getModule().replace("_WORKFLOW_MODULE_", "") + "_FORM";
            }
            action.setFormId(form_id);

            List<EdsModelField> fields = modelFieldManager.getFieldsForWorkflowUpdate(form_id);
            for (EdsModelField field : fields) {
                action.addField(field.getRPC(null));
            }
        }

        if (objectId != null) {
            EdsWorkflowAction edsWorkflowAction = workflowActionManager.get(objectId);
            edsWorkflowAction.getRPC(action);
        }
        return action;
    }

    @Override
    public WorkflowUpdateField editWorkflowUpdateField(Integer objectID, Integer workflowID) {
        WorkflowUpdateField item = new WorkflowUpdateField();
        if (workflowID != null) {
            item.setWorkflowID(workflowID);
            EdsWorkflowRule rule = workflowRuleManager.get(workflowID);
            String form_id = null;
            if (WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL.equals(rule.getModule())) {
                form_id = LayoutRPC.MANUAL_JOURNAL_FORM;
            } else if (WorkflowRule._WORKFLOW_MODULE_TASK.equals(rule.getModule())) {
                form_id = LayoutRPC.TASK_MAX_FORM;
            } else {
                form_id = rule.getModule().replace("_WORKFLOW_MODULE_", "") + "_FORM";
            }
            item.setFormID(form_id);
            ArrayList<ModelField> modelFields = new ArrayList<>();
            List<EdsModelField> fields = modelFieldManager.getFieldsForWorkflowUpdate(form_id);
            //here we get Annual Leave Allowance by field code and name, this name is dynamic field name
            Map<String, String> leaveReasonMap = allInOneServiceLocal.getLeaveReasonMap(form_id);
            int i = 0;
            for (EdsModelField f : fields) {
                if (f.getObjectID() > i) {
                    i = f.getObjectID();
                }
                if (!leaveReasonMap.containsKey(f.getField_ID())) {
                    modelFields.add(f.getRPC(null));
                } else {
                    modelFields.add(f.getRPC(null, leaveReasonMap.get(f.getField_ID())));
                }
            }
            if (LayoutRPC.HRMS_EMPLOYEE_FORM.equals(form_id)) {
                List<EdsPayrollCategory> pdItems = payrollCategoryManager.list(true);
                for (EdsPayrollCategory it : pdItems) {
                    ModelField f = new ModelField();
                    f.setObjectID(++i);
                    f.setWidget(UI_TYPE_TEXTBOX);
                    f.setType(DATA_TYPE_NUMBER);
                    f.setField_ID((EdsPayrollCategory.PAYMENT.equals(it.getType()) ? "EPC_" : "EDC_") + it.getCode());
                    f.setDynamicLabel((EdsPayrollCategory.PAYMENT.equals(it.getType()) ? "EPC_" : "EDC_") + it.getName());
                    modelFields.add(f);
                }
            }
            item.setFields(modelFields);
            ArrayList<SelectItem> modules = new ArrayList<>();
            EdsModel form = modelManager.get(form_id);
            modules.add(new SelectItem(form.getObjectID(), referenceWfmMessageSource.localize(form.getTitle().replace(" ", ""), form.getTitle()), form.getFormID()));
            if (LayoutRPC.ACTIVITY_FORM.equals(form_id)) {
                EdsModel candidateForm = modelManager.get(LayoutRPC.CANDIDATE_FORM);
                modules.add(new SelectItem(candidateForm.getObjectID(), referenceWfmMessageSource.localize(candidateForm.getTitle().replace(" ", ""), candidateForm.getTitle()), candidateForm.getFormID()));
            }
            if (LayoutRPC.HRMS_EMPLOYEE_FORM.equals(form_id) || LayoutRPC.CANDIDATE_FORM.equals(form_id)) {
                List<EdsModel> stepForms = modelManager.getStepForms(null, false);
                if (stepForms != null && stepForms.size() > 0) {
                    for (EdsModel m : stepForms) {
                        modules.add(new SelectItem(m.getObjectID(), m.getTitle(), m.getFormID()));
                    }
                }
            } else if (form.isStepForm()) {
                List<EdsModel> stepForms = modelManager.getStepForms(form_id, true);
                if (stepForms != null && stepForms.size() > 0) {
                    for (EdsModel m : stepForms) {
                        modules.add(new SelectItem(m.getObjectID(), m.getTitle(), m.getFormID()));
                    }
                }
            } else if (LayoutRPC.SALEQUOTE_FORM.equals(form_id) || "SALEORDER_FORM".equals(form_id) || LayoutRPC.PURCHASEORDER_FORM.equals(form_id)) {
                EdsModel oppForm = modelManager.get(LayoutRPC.OPPORTUNITY_FORM);
                modules.add(new SelectItem(oppForm.getObjectID(), referenceWfmMessageSource.localize(oppForm.getTitle().replace(" ", ""), oppForm.getTitle()), oppForm.getFormID()));
            }
            item.setModules(modules.toArray(new SelectItem[]{}));
        }
        if (objectID != null) {
            EdsWorkflowUpdateField edsWorkflowUpdateField = workflowUpdateFieldManager.get(objectID);
            item = edsWorkflowUpdateField != null ? edsWorkflowUpdateField.getRPC(item) : item;
        }
        return item;
    }

    @Override
    public ArrayList<ModelField> getModelFields(String formID) {
        ArrayList<ModelField> modelFields = new ArrayList<>();
        List<EdsModelField> fields = modelFieldManager.getFieldsForWorkflowUpdate(formID);
        for (EdsModelField f : fields) {
            modelFields.add(f.getRPC(null));
        }
        return modelFields;
    }

    @Override
    public Integer saveWorkflowAlert(WorkflowAlert item) {
        if (item != null) {
            EdsWorkflowAlert alert = item.getObjectID() != null ? workflowAlertManager.get(item.getObjectID()) : new EdsWorkflowAlert();
            if (alert == null) {
                alert = new EdsWorkflowAlert();
            }
            alert = EdsWorkflowAlert.fromRPC(alert, item);
            if (alert.isNew()) {
                if (item.getWorkflowRule() == null || item.getWorkflowRule().getObjectID() == null) {
                    return null;
                }
                alert.setWorkflow(workflowRuleManager.get(item.getWorkflowRule().getObjectID()));
            }
            alert.setEmailSetting(item.getEmailSettingID() != null && item.getEmailSettingID() > 0 ? emailSettingsManager.get(item.getEmailSettingID()) : null);
            alert.setEmailTemplate(item.getEmailTemplate() != null && item.getEmailTemplate().getId() != null ? emailTemplateManager.get(item.getEmailTemplate().getId()) : null);
            workflowAlertManager.createOrUpdate(alert);
            return alert.getObjectID();
        }
        return null;
    }

    @Override
    public void saveWorkflowSMSAlert(WorkflowSMSAlert item) {
        EdsWorkflowSMSAlert smsAlert = item.getObjectID() != null ? workflowSMSAlertManager.get(item.getObjectID()) : new EdsWorkflowSMSAlert();
        if (smsAlert.isNew()) {
            if (item.getWorkflow() == null || item.getWorkflow().getObjectID() == null) {
                return;
            }
            smsAlert.setWorkflow(workflowRuleManager.get(item.getWorkflow().getObjectID()));
        }
        if (item.getProviderID() != null) {
            smsAlert.setProvider(smsManager.get(item.getProviderID()));
        }
        if (item.getTemplateID() != null) {
            smsAlert.setSmsTemplate(smsTemplateManager.get(item.getTemplateID()));
        }
        smsAlert.setPhone(item.getPhone());
        smsAlert.setContent(item.getContent());
        smsAlert.setRecipientType(item.getTaskSMSrecipientType());
        smsAlert.setWorkflowActionTimeBased(item.isWorkflowActionTimeBased());
        smsAlert.setWorkflowActionStartTime(item.getWorkflowActionStartTime());
        smsAlert.setWorkflowActionStartTimeUnit(item.getWorkflowActionStartTimeUnit());
        smsAlert.setWorkflowActionStartTimeGranularity(item.getWorkflowActionStartTimeGranularity());
        workflowSMSAlertManager.createOrUpdate(smsAlert);
    }

    @Override
    public void saveWorkflowTelegramAlert(WorkflowTelegramAlert item) {
        EdsWorkflowTelegramAlert telegramAlert = item.getObjectId() != null ? workflowTelegramAlertManager.get(item.getObjectId()) : new EdsWorkflowTelegramAlert();

        if (telegramAlert.isNew()) {
            if (item.getWorkflowRule() == null || item.getWorkflowRule().getObjectID() == null) {
                return;
            }
            telegramAlert.setWorkflow(workflowRuleManager.get(item.getWorkflowRule().getObjectID()));
        }
        if (item.getTelegramBot() != null) {
            telegramAlert.setTelegramBotId(item.getTelegramBot().getId());
        }
        if (item.getTelegramChatListItems() != null && item.getTelegramChatListItems().size() > 0) {
            Set<EdsTelegramChat> edsTelegramChats = new HashSet<>();
            for (TelegramChatListItem telegramChatListItem : item.getTelegramChatListItems()) {
                if (telegramChatListItem.getObjectId() != null) {
                    EdsTelegramChat edsTelegramChat = telegramChatManager.get(telegramChatListItem.getObjectId());
                    edsTelegramChats.add(edsTelegramChat);
                }
            }
            telegramAlert.setTelegramChats(edsTelegramChats);
        }
        telegramAlert.setMessage(item.getMessage());
        telegramAlert.setWorkflowActionTimeBased(item.isWorkflowActionTimeBased());
        telegramAlert.setWorkflowActionStartTime(item.getWorkflowActionStartTime());
        telegramAlert.setWorkflowActionStartTimeUnit(item.getWorkflowActionStartTimeUnit());
        telegramAlert.setWorkflowActionStartTimeGranularity(item.getWorkflowActionStartTimeGranularity());
        telegramAlert.setReceiverAttributes(item.getReceiverAttributes());

        workflowTelegramAlertManager.createOrUpdate(telegramAlert);

        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            attachmentUtilsManager.saveAttachments(F_TELEGRAM, telegramAlert.getObjectID(), telegramAlert.getObjectID(), item.getAttachments());
        }
    }

    @Override
    @Transactional
    public ArrayList<Integer> deleteWorkflows(ArrayList<Integer> objectIds) {
        EdsUser user = workflowRuleManager.getUser();
        ArrayList<Integer> result = new ArrayList<>();
        if (objectIds != null && objectIds.size() > 0) {
            for (Integer objectId : objectIds) {
                EdsWorkflowRule workflowRule = workflowRuleManager.get(objectId);
                if (workflowRule != null) {
                    workflowRule.setActive(false);
                    workflowRule.setDeleted(true);
                    workflowRule.getConditions().clear();
                    workflowRuleManager.update(workflowRule);
                    EdsRecurrence recurrence = recurrenceManager.getRecurrencesByUser(objectId, SchedulerConstant.RECURRING_WORKFLOW, user);
                    recurrenceService.updateRecurrence(recurrence, true, true);
                    continue;
                }
                result.add(objectId);
            }
        }
        return result;
    }

    @Override
    public WorkflowRule getWorkflowRuleForEvent(Integer workflowID) {
        if (workflowID != null) {
            EdsWorkflowRule workflowRule = workflowRuleManager.get(workflowID);
            if (workflowRule != null) {
                return workflowRule.getRPC(null);
            }
        }
        return null;
    }

    @Override
    public ListResult<WorkflowPush> getWorkflowPushList(ListingFilterParameter fp) {
        List<EdsWorkflowPush> pushList = workflowPushManager.list(fp);
        int totalCount = workflowPushManager.getTotalCount(fp);
        ArrayList<WorkflowPush> items = new ArrayList<>();
        for (EdsWorkflowPush item : pushList) {
            items.add(item.getRPC(null));
        }
        return new ListResult<>(items, totalCount);
    }

    @Override
    public WorkflowPush getWorkflowPush(Integer objectID, Integer workflowId) {
        EdsWorkflowPush push = objectID != null ? workflowPushManager.get(objectID) : new EdsWorkflowPush();
        push.setWorkflow(workflowId != null ? workflowRuleManager.get(workflowId) : null);
        WorkflowPush workflowPush = new WorkflowPush();
        workflowPush.setAllRoles(roleManager.list().stream().map(x -> new SelectItem(x.getObjectID(), x.getName())).collect(Collectors.toCollection(ArrayList::new)));
        return push.getRPC(workflowPush);
    }

    @Override
    public void saveWorkflowPush(WorkflowPush pushItem) {
        EdsWorkflowPush workflowPush = pushItem.getObjectID() != null ? workflowPushManager.get(pushItem.getObjectID()) : new EdsWorkflowPush();
        workflowPush.setSubject(pushItem.getSubject());
        workflowPush.setRecipient(pushItem.getRecipient());

        Set<EdsRole> selectedRoles = new HashSet<>();
        for (SelectItem selectedRole : pushItem.getSelectedRoles()) {
            selectedRoles.add(roleManager.get(selectedRole.getId()));
        }
        workflowPush.setSelectedRoles(selectedRoles);
        if (workflowPush.isNew()) {
            if (pushItem.getWorkflowID() == null) {
                return;
            }
            workflowPush.setWorkflow(workflowRuleManager.get(pushItem.getWorkflowID()));
        }
        workflowPushManager.createOrUpdate(workflowPush);
    }

    @Override
    public void deleteWorkflowPush(Integer pushID) {
        EdsWorkflowPush workflowPush = workflowPushManager.get(pushID);
        workflowPush.setDeleted(true);
        workflowPushManager.update(workflowPush);
    }

    @Override
    public void deleteWorkflowPushes(ArrayList<Integer> ids) {
        if (ids != null && ids.size() > 0) {
            workflowPushManager.deletePushs(ids);
        }
    }

    @Override
    public ListResult<WorkflowRule> getWorkflowActivitiesList(ListingFilterParameter fp) {
        ArrayList<WorkflowRule> results = new ArrayList<>();
        int totalCount = workflowRuleManager.getActivitiesListCount();
        if (totalCount > 0) {
            List<Object[]> activities = workflowRuleManager.getListForActivities(fp);
            for (Object[] item : activities) {
                WorkflowRule result = new WorkflowRule();
                if (item[1] != null) {
                    result.setObjectID((Integer) item[0]);
                    result.setEntityId((Integer) item[1]);
                    result.setName((String) item[2]);
                    result.setExecutionDate((Date) item[3]);
                    result.setExecutionCriteria(WorkflowExecutionCriteriaEnum.valueOf((String) item[4]));
                    result.setModule((String) item[5]);
                    if (item[6] != null) {
                        result.setCreator(userManager.get((Integer) item[6]) != null ? userManager.get((Integer) item[6]).getFullName() : null);
                    }
                    String type = (String) item[7];
                    if (type != null) {
                        switch (type) {
                            case WorkflowRule.WORKFLOW_ALERT -> result.setActivitiesType(type);
                            case WorkflowRule.WORKFLOW_EVENT -> result.setActivitiesType(type);
                            case WorkflowRule.WORKFLOW_TASK -> result.setActivitiesType(type);
                            case WorkflowRule.WORKFLOW_SMS_ALERT -> result.setActivitiesType(type);
                            case WorkflowRule.WORKFLOW_ONBOARDING_STEP -> result.setActivitiesType(type);
                        }
                    }
                    if (item[8] != null) {
                        int eventType = (Integer) item[8];
                        if (eventType == 2) {
                            result.setCallLog(true);
                        }
                    }
                    if (item[9] != null) {
                        result.setRecurrenceID((Integer) item[9]);
                    }
                    if (item[10] != null) {
                        result.setActivitiesRuleName((String) item[10]);
                    }
                }
                results.add(result);
            }
        }

        return new ListResult<>(results, totalCount);
    }

    @Override
    public boolean deleteCertificateType(Integer objectId) {
        return hrmsService.deleteCertificateType(objectId);
    }

    public void stopUpcomingRecurrence(Integer recurrenceID) {
        if (recurrenceID != null) {
            recurrenceManager.nativelyRemoveRecurrence(recurrenceID);
        }
    }

    @Override
    public SelectItem[] getApproverModules(ListingFilterParameter fp) {
        fp.setShowInListing(true);
        List<EdsOnboardingStep> onboardingSteps = onboardingStepManager.getOnboardingStepList(fp);
        List<EdsCertificateOfEmploymentType> certificateTypes = null;
        certificateTypes = certificateOfEmploymentTypeManager.getCertificateTypeList(fp);

        ArrayList<SelectItem> steps = new ArrayList<>();
        if (!fp.isAllByFilter()) {
            steps.add(new SelectItem(0, commonLocalizer.localize("leaveRequest"), RelationItem.TYPE_LEAVE_REQUEST, RelationItem.TYPE_LEAVE_REQUEST));
            steps.add(new SelectItem(0, commonLocalizer.localize("cashAdvance"), RelationItem.TYPE_CASH_ADVANCE, RelationItem.TYPE_CASH_ADVANCE));
            steps.add(new SelectItem(0, commonLocalizer.localize("expenseClaim"), RelationItem.TYPE_EXPENSE_CLAIM, RelationItem.TYPE_EXPENSE_CLAIM));
            steps.add(new SelectItem(0, commonLocalizer.localize("additionalPayment"), RelationItem.TYPE_ADDITIONAL_PAYMENT, RelationItem.TYPE_ADDITIONAL_PAYMENT));
            steps.add(new SelectItem(0, commonLocalizer.localize("salesQuote"), RelationItem.TYPE_SALEQUOTE, RelationItem.TYPE_SALEQUOTE));
            steps.add(new SelectItem(0, commonLocalizer.localize("salesOrder"), RelationItem.TYPE_SALEORDER, RelationItem.TYPE_SALEORDER));
            steps.add(new SelectItem(0, commonLocalizer.localize("salesInvoice"), RelationItem.TYPE_SALEINVOICE, RelationItem.TYPE_SALEINVOICE));
            steps.add(new SelectItem(0, commonLocalizer.localize("creditNote"), RelationItem.TYPE_CREDIT_NOTE, RelationItem.TYPE_CREDIT_NOTE));
            steps.add(new SelectItem(0, commonLocalizer.localize("purchaseinvoice"), RelationItem.TYPE_PURCHASE_INVOICE, RelationItem.TYPE_PURCHASE_INVOICE));
            steps.add(new SelectItem(0, commonLocalizer.localize("purchaseorder"), RelationItem.TYPE_PURCHASE_ORDER, RelationItem.TYPE_PURCHASE_ORDER));
            steps.add(new SelectItem(0, commonLocalizer.localize("requestForPurchase"), RelationItem.REQUEST_FOR_PURCHASE, RelationItem.REQUEST_FOR_PURCHASE));
            steps.add(new SelectItem(0, commonLocalizer.localize("manualEntry"), RelationItem.TYPE_MANUAL_JOURNAL, RelationItem.TYPE_MANUAL_JOURNAL));
            steps.add(new SelectItem(0, commonLocalizer.localize("requestForQuote"), RelationItem.TYPE_REQUEST_FOR_QUOTE, RelationItem.TYPE_REQUEST_FOR_QUOTE));
            steps.add(new SelectItem(0, commonLocalizer.localize("stockTransfer"), RelationItem.TYPE_STOCK_TRANSFER, RelationItem.TYPE_STOCK_TRANSFER));
            steps.add(new SelectItem(0, commonLocalizer.localize("stockAdjustment"), RelationItem.TYPE_STOCK_ADJUSTMENT, RelationItem.TYPE_STOCK_ADJUSTMENT));
            steps.add(new SelectItem(0, commonLocalizer.localize("placement"), RelationItem.TYPE_PLACEMENT, RelationItem.TYPE_PLACEMENT));
            steps.add(new SelectItem(0, commonLocalizer.localize("opportunity"), RelationItem.TYPE_OPPORTUNITY, RelationItem.TYPE_OPPORTUNITY));
            steps.add(new SelectItem(0, commonLocalizer.localize("groupGoal"), RelationItem.TYPE_GROUP_GOAL, RelationItem.TYPE_GROUP_GOAL));
            steps.add(new SelectItem(0, commonLocalizer.localize("vacancy"), RelationItem.TYPE_VACANCY, RelationItem.TYPE_VACANCY));
            steps.add(new SelectItem(0, commonLocalizer.localize("shift"), RelationItem.TYPE_SHIFT, RelationItem.TYPE_SHIFT));
            steps.add(new SelectItem(0, commonLocalizer.localize("rotation"), RelationItem.TYPE_ROTATION, RelationItem.TYPE_ROTATION));
            steps.add(new SelectItem(0, commonLocalizer.localize("groupPlacement"), RelationItem.TYPE_GROUP_PLACEMENT, RelationItem.TYPE_GROUP_PLACEMENT));
            steps.add(new SelectItem(0, commonLocalizer.localize("debitNote"), RelationItem.TYPE_DEBIT_NOTE, RelationItem.TYPE_DEBIT_NOTE));
            steps.add(new SelectItem(0, commonLocalizer.localize("backupEmployee"), RelationItem.TYPE_BACKUPS_EMPLOYEE, RelationItem.TYPE_BACKUPS_EMPLOYEE));
            steps.add(new SelectItem(0, commonLocalizer.localize("rentalOrder"), RelationItem.TYPE_RENTAL_ORDER, RelationItem.TYPE_RENTAL_ORDER));
            steps.add(new SelectItem(0, commonLocalizer.localize("buildAssembly"), RelationItem.TYPE_BUILD_ASSEMBLY, RelationItem.TYPE_BUILD_ASSEMBLY));
        }
        for (EdsOnboardingStep onboardingStep : onboardingSteps) {
            steps.add(new SelectItem(onboardingStep.getObjectID(), onboardingStep.getName(), onboardingStep.getFormID(), RelationItem.TYPE_EMPLOYEE_STEP));
        }
        List<EdsCustomForm> edsCustomForms = customFormManager.getForms();
        if (!edsCustomForms.isEmpty()) {
            edsCustomForms.forEach(edsCustomForm -> steps.add(new SelectItem(edsCustomForm.getObjectID(),
                    edsCustomForm.getName(),
                    edsCustomForm.getFormID(),
                    RelationItem.TYPE_CUSTOM_FORM_ITEM)));
        }
        for (EdsCertificateOfEmploymentType edsEmploymentType : certificateTypes) {
            steps.add(new SelectItem(edsEmploymentType.getObjectID(), edsEmploymentType.getName(), edsEmploymentType.getFormID(), RelationItem.TYPE_CERTIFICATE_OF_EMPLOYMENT));
        }
        steps.sort(Comparator.comparing(SelectItem::getName));
        return steps.toArray(new SelectItem[0]);
    }

    @Override
    public WorkflowEmployee getWorkflowEmployee(Integer objectID, Integer workflowID) {
        WorkflowEmployee item = new WorkflowEmployee();
        EdsWorkflowRule rule = workflowRuleManager.get(workflowID);
        item.setWorkflowModule(rule.getModule());
        item.setWorkflowID(workflowID);
        List<EdsModelField> fields = modelFieldManager.getFieldsForWorkflowEmployee(LayoutRPC.CANDIDATE_FORM);
        if (fields != null && fields.size() > 0) {
            for (EdsModelField field : fields) {
                item.getFields().add(field.getRPC(null));
            }
        }
        if (objectID != null) {
            EdsWorkflowEmployee workflowEmployee = workflowEmployeeManager.get(objectID);
            return workflowEmployee.getRPC(item);
        }
        return item;
    }

    @Override
    public void saveWorkflowEmployee(WorkflowEmployee item) {
        EdsWorkflowEmployee workflowEmployee = item.getObjectID() != null ? workflowEmployeeManager.get(item.getObjectID()) : new EdsWorkflowEmployee();
        workflowEmployee.setWorkflowID(item.getWorkflowID());
        workflowEmployee.setValues(item.getValuesAsString());
        workflowEmployeeManager.createOrUpdate(workflowEmployee);
    }

    @Override
    public ListResult<WorkflowEmployee> getWorkflowEmployeeList(ListingFilterParameter fp) {
        fp = fp == null ? new ListingFilterParameter() : fp;
        List<EdsWorkflowEmployee> workflowEmployees = workflowEmployeeManager.list(fp);
        ArrayList<WorkflowEmployee> results = new ArrayList<>();
        for (EdsWorkflowEmployee em : workflowEmployees) {
            WorkflowEmployee we = new WorkflowEmployee();
            we.setWorkflowID(em.getWorkflowID());
            we.setObjectID(em.getObjectID());
            results.add(we);
        }
        return new ListResult<>(results, results.size());
    }

    @Override
    public void deleteWorkflowEmployees(ArrayList<Integer> ids) {
        if (ids.size() > 0) {
            if (ids.size() > 1) {
                workflowEmployeeManager.deleteByIDs(ServerUtils.getAsCommoDelimited(ids, "0", ","));
            } else {
                EdsWorkflowEmployee workflowEmployee = workflowEmployeeManager.get(ids.get(0));
                workflowEmployeeManager.delete(workflowEmployee);
            }
        }
    }

    @Override
    public HashMap<String, Boolean> getEnableUploadTypes() {
        HashMap<String, Boolean> map = new HashMap<>();
        EdsCompanySettings settings = companySettingsManager.getCompanySettings(SecurityContext.getCompanyID());
        if (settings != null && (settings.getEnableUploadTypes() != null && !"".equals(settings.getEnableUploadTypes()))) {
            String[] s = settings.getEnableUploadTypes().split(";");
            setEnableTypes(map, s);
        } else {
            String[] s = "true;true;".split(";");
            setEnableTypes(map, s);
        }
        return map;
    }

    private void setEnableTypes(HashMap<String, Boolean> map, String[] s) {
        if (s.length > 0) {
            map.put(AMAZON, Boolean.valueOf(s[0]));
        }
        if (s.length > 1) {
            map.put(GOOGLE, Boolean.valueOf(s[1]));
        }
        if (s.length > 2) {
            map.put(OFFICE_365, Boolean.valueOf(s[2]));
        }
        if (s.length > 3) {
            map.put(UPLOAD_SHARE_POINT, Boolean.valueOf(s[3]));
        }
        if (s.length > 4) {
            map.put(LINK_TO_SHARE_POINT, Boolean.valueOf(s[4]));
        }
        if (s.length > 5) {
            map.put(MINIO, Boolean.valueOf(s[5]));
        }
        if (s.length > 6) {
            map.put(LOCAL, Boolean.valueOf(s[6]));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceJobItem getJob() {
        return recurrenceService.getJob();
    }

    @Override
    public void deleteOfficeToken(String storageType) {
        EdsUser user = employeeManager.getUser();
        EdsEmployee employee = employeeManager.get(user.getObjectID());
        office365AuthService.deleteOfficeCalendar(employee, true, storageType);
    }

    @Override
    public boolean validateOffice365(String storageType) {
        return office365AuthService.isUserLinked(storageType);
    }

    @Override
    public WorkflowInvoice getWorkflowInvoice(Integer objectID, Integer workflowID) {
        WorkflowInvoice item = new WorkflowInvoice();
        item.setWorkflowID(workflowID);
        List<ModelField> fields = modelFieldManager.getFields(LayoutRPC.SALEINVOICE_FORM);
        if (fields != null && fields.size() > 0) {
            for (ModelField field : fields) {
                item.getFields().add(field);
            }
        }
        if (objectID != null) {
            EdsWorkflowInvoice workflowInvoice = workflowInvoiceManager.get(objectID);
            return workflowInvoice.getRPC(item);
        }
        return item;
    }

    @Override
    public void saveWorkflowInvoice(WorkflowInvoice item) {
        EdsWorkflowInvoice workflowInvoice = item.getObjectID() != null ? workflowInvoiceManager.get(item.getObjectID()) : new EdsWorkflowInvoice();
        workflowInvoice.setWorkflowID(item.getWorkflowID());
        workflowInvoice.getInvoiceFields().clear();
        for (WorkflowInvoiceField f : item.getInvoiceFields()) {
            EdsWorkflowInvoiceField field = new EdsWorkflowInvoiceField();
            field.setCustomFieldID(f.getCustomFieldID());
            field.setValue(f.getValue());
            field.setAction(f.getAction());
            field.setPercentage(f.isPercentage());
            field.setDemandOn(f.isDemandOn());
            if (f.getField() != null && f.getField().getForm_ID() != null && f.getField().getField_ID() != null) {
                EdsModelField modelField = modelFieldManager.getByFieldID(f.getField().getForm_ID(), f.getField().getField_ID());
                if (modelField != null) {
                    if (modelField instanceof EdsModelFieldCustom) {
                        field.setField((EdsModelFieldCustom) modelField);
                    } else if (modelField instanceof EdsModelFieldDefault) {
                        field.setFieldDefault((EdsModelFieldDefault) modelField);
                    }
                }
            }
            workflowInvoice.getInvoiceFields().add(field);
        }
        workflowInvoiceManager.createOrUpdate(workflowInvoice);
    }

    @Override
    public ListResult<WorkflowInvoice> getWorkflowInvoiceList(ListingFilterParameter fp) {
        fp = fp == null ? new ListingFilterParameter() : fp;
        List<EdsWorkflowInvoice> workflowInvoices = workflowInvoiceManager.list(fp);
        ArrayList<WorkflowInvoice> results = new ArrayList<>();
        for (EdsWorkflowInvoice em : workflowInvoices) {
            WorkflowInvoice wi = new WorkflowInvoice();
            wi.setWorkflowID(em.getWorkflowID());
            wi.setObjectID(em.getObjectID());
            results.add(wi);
        }
        return new ListResult<>(results, results.size());
    }

    @Override
    public void deleteWorkflowInvoice(Integer objectID) {
        EdsWorkflowInvoice workflowInvoice = workflowInvoiceManager.get(objectID);
        workflowInvoiceManager.delete(workflowInvoice);
    }

    public MagentoSettingsItem getMagentoSettings() {
        MagentoSettingsItem magentoSettingsItem = magentoService.getMagentoSettings();
        EdsUser user = userManager.getUser();
        Integer jobType = SchedulerConstant.SYNCHRONIZE_MAGENTO_CATALOG;
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(jobType, user.getObjectID(), user.getCompany().getObjectID());
        if (recurrence != null) {
            RecurrenceJobItem result = recurrence.createRecurrenceItem(jobType);
            magentoSettingsItem.setRecurrenceJobItem(result);
        }
        return magentoSettingsItem;
    }

    public Integer saveMagentoSettings(MagentoSettingsItem magentoSettings) {
        magentoService.saveMagentoSettings(magentoSettings);
        if (magentoSettings.getRecurrenceJobItem() != null) {
            return recurrenceService.saveRecurrenceJob(magentoSettings.getRecurrenceJobItem());
        } else {
            EdsUser user = userManager.getUser();
            Integer jobType = SchedulerConstant.SYNCHRONIZE_MAGENTO_CATALOG;
            recurrenceService.deleteRecurrence(user.getObjectID(), jobType);
        }
        return null;
    }

    public Integer synchronizeWithMagentoCatalog() {
        EdsUser user = userManager.getUser();
        Integer jobType = SchedulerConstant.SYNCHRONIZE_MAGENTO_CATALOG;
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(jobType, user.getObjectID(), user.getCompany().getObjectID());
        if (recurrence != null && SchedulerConstant.IN_PROGRESS.equals(recurrence.getStatus())) {
            return 1;
        }
        magentoService.synchronizeWithMagentoCatalog();
        return 0;
    }

    @Override
    public Integer resetMagentoSynchronization() {
        EdsUser user = userManager.getUser();
        Integer jobType = SchedulerConstant.SYNCHRONIZE_MAGENTO_CATALOG;
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(jobType, user.getObjectID(), user.getCompany().getObjectID());
        if (recurrence != null && SchedulerConstant.IN_PROGRESS.equals(recurrence.getStatus())) {
            return 1;
        }
        magentoService.resetMagentoSynchronization();
        return 0;
    }

    @Override
    public void saveIntegrationSettins(IntegrationSettingsItem settingsItem) {
        for (Map.Entry<String, String> entry : settingsItem.getSettings().entrySet()) {
            EdsIntegrationSettings setting = new EdsIntegrationSettings();
            setting.setKey(entry.getKey());
            setting.setValue(entry.getValue());
            integrationSettingsManager.createOrUpdate(setting);
        }
    }

    @Override
    public IntegrationSettingsItem getIntegrationSettings() {
        IntegrationSettingsItem settingsItem = new IntegrationSettingsItem();
        List<EdsIntegrationSettings> settings = integrationSettingsManager.getIntegrationSettings();

        for (EdsIntegrationSettings setting : settings) {
            if (!setting.getKey().endsWith("_PASSWORD"))
                settingsItem.addSetting(setting.getKey(), setting.getValue());
            else
                settingsItem.addSetting(setting.getKey(), EncryptionHelper.md5(setting.getValue()).substring(0, 5));
        }
        return settingsItem;
    }

    @Override
    public IntegrationSettingsItem getIntegrationSettingsWithPass() {
        IntegrationSettingsItem settingsItem = new IntegrationSettingsItem();
        List<EdsIntegrationSettings> settings = integrationSettingsManager.getIntegrationSettings();

        for (EdsIntegrationSettings setting : settings) {
            settingsItem.addSetting(setting.getKey(), setting.getValue());
        }
        return settingsItem;
    }

    public SelectItem[] getEntityTypes() {
        List<EdsEntityType> entityTypes = entityTypeManager.list();
        ArrayList<SelectItem> result = new ArrayList<>();
        for (EdsEntityType entityType : entityTypes) {
            result.add(entityType.getAsSelectItem());
        }
        return result.toArray(new SelectItem[]{});
    }

    public TestRPC saveLrSettingsItem(LRSettingsItem lrSettingsItem) {
        TestRPC result = new TestRPC();
        EdsCompanyPayrollSettings allowCopy = companyPayrollSettingsManager.getCompanySettingValue(ALLOW_COPY_ALLOWANCE_FROM_PREVIOUS);
        EdsCompanyPayrollSettings copyPercentage = companyPayrollSettingsManager.getCompanySettingValue(PREVIOUS_ALLOWANCE_COPY_PCT);
        EdsCompanyPayrollSettings usageDeadline = companyPayrollSettingsManager.getCompanySettingValue(USAGE_DEADLINE);
        EdsCompanyPayrollSettings payRemainingAllowances = companyPayrollSettingsManager.getCompanySettingValue(PAY_REMAINING_ALLOWANCE);
        if (lrSettingsItem.getCopyPreviousYearAllowances()) {
            if (allowCopy == null) {
                allowCopy = new EdsCompanyPayrollSettings();
            }
            allowCopy.setKey(ALLOW_COPY_ALLOWANCE_FROM_PREVIOUS);
            allowCopy.setValue("true");
            companyPayrollSettingsManager.createOrUpdate(allowCopy);

            if (lrSettingsItem.getPrevYearAllowanceCopyPercent() != null) {
                if (copyPercentage == null) {
                    copyPercentage = new EdsCompanyPayrollSettings();
                }
                copyPercentage.setKey(PREVIOUS_ALLOWANCE_COPY_PCT);
                copyPercentage.setValue(String.valueOf(lrSettingsItem.getPrevYearAllowanceCopyPercent()));
                companyPayrollSettingsManager.createOrUpdate(copyPercentage);
            } else {
                if (copyPercentage != null) {
                    companyPayrollSettingsManager.delete(copyPercentage);
                }
            }

        } else {
            if (allowCopy != null) {
                companyPayrollSettingsManager.delete(allowCopy);
            }
            if (copyPercentage != null) {
                companyPayrollSettingsManager.delete(copyPercentage);
            }
        }

        if (lrSettingsItem.getPayremainingallowance()) {
            if (payRemainingAllowances == null) {
                payRemainingAllowances = new EdsCompanyPayrollSettings();
            }
            payRemainingAllowances.setKey(PAY_REMAINING_ALLOWANCE);
            payRemainingAllowances.setValue("true");
            companyPayrollSettingsManager.createOrUpdate(payRemainingAllowances);
        } else {
            if (payRemainingAllowances != null) {
                companyPayrollSettingsManager.delete(payRemainingAllowances);
            }
        }
        if (lrSettingsItem.getUsageDeadline() != null) {
            if (usageDeadline == null) {
                usageDeadline = new EdsCompanyPayrollSettings();
            }
            usageDeadline.setKey(USAGE_DEADLINE);
            usageDeadline.setValue(String.valueOf(lrSettingsItem.getUsageDeadline()));
            companyPayrollSettingsManager.createOrUpdate(usageDeadline);
        } else {
            if (usageDeadline != null) {
                companyPayrollSettingsManager.delete(usageDeadline);
            }
        }
        return result;
    }

    public LRSettingsItem getLrSettingsItem() {
        LRSettingsItem lrSettingsItem = new LRSettingsItem();
        EdsCompanyPayrollSettings allowCopy = companyPayrollSettingsManager.getCompanySettingValue(ALLOW_COPY_ALLOWANCE_FROM_PREVIOUS);
        EdsCompanyPayrollSettings copyPercentage = companyPayrollSettingsManager.getCompanySettingValue(PREVIOUS_ALLOWANCE_COPY_PCT);
        EdsCompanyPayrollSettings usageDeadline = companyPayrollSettingsManager.getCompanySettingValue(USAGE_DEADLINE);
        EdsCompanyPayrollSettings payRemainingAllowances = companyPayrollSettingsManager.getCompanySettingValue(PAY_REMAINING_ALLOWANCE);
        String allowCopyValue = allowCopy != null ? allowCopy.getValue() : null;
        if (allowCopy != null && ("true".equalsIgnoreCase(allowCopyValue) || "false".equalsIgnoreCase(allowCopyValue))) {
            lrSettingsItem.setCopyPreviousYearAllowances(Boolean.parseBoolean(allowCopyValue));
        }
        String copyPercentageValue = copyPercentage != null ? copyPercentage.getValue() : null;
        if (copyPercentage != null && copyPercentageValue != null && copyPercentageValue.matches("^\\d+(\\.\\d{1,2})?")) {
            lrSettingsItem.setPrevYearAllowanceCopyPercent(new BigDecimal(copyPercentageValue));
        }
        String usageDeadLineValue = usageDeadline != null ? usageDeadline.getValue() : null;
        if (usageDeadline != null && NumberUtils.isDigits(usageDeadLineValue)) {
            long usageDeadline_ = Long.parseLong(usageDeadLineValue);
            Date date = new Date(usageDeadline_);
            date.setYear(new Date().getYear());
            lrSettingsItem.setUsageDeadline(date.getTime());
        }
        String payRemainingValue = payRemainingAllowances != null ? payRemainingAllowances.getValue() : null;
        if (payRemainingAllowances != null && ("true".equalsIgnoreCase(payRemainingValue) || "false".equalsIgnoreCase(payRemainingValue))) {
            lrSettingsItem.setPayremainingallowance(Boolean.parseBoolean(payRemainingValue));
        }
        return lrSettingsItem;
    }

    @Override
    public ListResult<ImportLogItem> getImportLogs(ListingFilterParameter filterParameter) {
        Integer totalCount = importFileManager.getImportEventsCount(filterParameter);
        List<EdsImportFile> importFiles = importFileManager.getImportEventList(filterParameter);
        ArrayList<ImportLogItem> result = importFiles.stream().map(importFile -> {
            ImportLogItem item = new ImportLogItem();
            item.setType(importFile.getType());
            item.setStatus(importFile.getStatus());
            item.setDate(importFile.getCreatedDate());
            item.setRequestedRows(importFile.getCsvColumns());
            item.setRejectedRows(importFile.getIgnoredColumns());
            item.setImportedRows(importFile.getImportedColumns());
            item.setSkippedRows(importFile.getSkippedColumns());
            item.setOverwrittenRows(importFile.getOverwrittenColumns());
            item.setErrorMessage(importFile.getException());
            if (importFile.getFileID() != null) {
                EdsUpload upload;
                if (ImportTypeEnum.BATCH_SALES_INVOICE.equals(importFile.getType()) ||
                        ImportTypeEnum.BATCH_SALES_INVOICE_PAYMENT.equals(importFile.getType()) ||
                        ImportTypeEnum.BATCH_SALES_ORDER.equals(importFile.getType())) {
                    EdsFileHeader fileHeader = fileHeaderManager.get(importFile.getFileID());
                    upload = fileHeader.getBodies().get(0);
                } else {
                    upload = attachmentManager.get(importFile.getFileID());
                }
                if (upload != null) {
                    item.setImportFile(new SelectItem(upload.getObjectID(), upload.getOriginalName(), uploadManager.getFileURL(upload)));
                }
            }
            if (importFile.getRejectedRecords() != null) {
                EdsUpload upload = importFile.getRejectedRecords();
                item.setLogFile(new SelectItem(upload.getObjectID(), upload.getOriginalName(), uploadManager.getFileURL(upload)));
            }
            return item;
        }).collect(Collectors.toCollection(ArrayList::new));
        return new ListResult<>(result, totalCount);
    }

    @Override
    public ListResult<MessageItem> getMessages(ListingFilterParameter filterParameter, boolean isWorkflowMessages) {
        Integer totalCount = workflowMessageManager.getTotalCount(filterParameter, isWorkflowMessages);
        List<EdsSuperMessage> messages = workflowMessageManager.getList(filterParameter, isWorkflowMessages);
        ArrayList<MessageItem> result = messages.stream().map(message -> {
            MessageItem item = new MessageItem();
            item.setFrom(message.getFromEmail());
            item.setTo(message.getTo());
            item.setSubject(message.getSubject());
            item.setStatus(message.getStatus());
            item.setAttempts(message.getAttempts());
            item.setCreationDate(message.getCreationDate());
            item.setSentDate(message.getSentDate());
            return item;
        }).collect(Collectors.toCollection(ArrayList::new));
        return new ListResult<>(result, totalCount);
    }

    @Override
    @Transactional(readOnly = true)
    public IntegrationItem getPaymentGatewayItem() {
        IntegrationItem result = new IntegrationItem();
        result.setMastercardPaymentEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MASTERCARD_PAYMENT_ENABLED));
        result.setElavonPaymentEnabled(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ELAVON_PAYMENT_ENABLED));

        EdsCompany company = invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
        if (invSettings != null) {
            result.setObjectID(invSettings.getObjectID());
            result.setPayPalMerchant(invSettings.getPayPalAccount());
            if (invSettings.getPayPalPaymentAccount() != null) {
                result.setPayPalBankAccount(invSettings.getPayPalPaymentAccount().getAsSelectItem());
            }
            //Set Stripe account details
            result.setStripeUserId(invSettings.getStripeUserId());
            if (invSettings.getStripePaymentAccount() != null) {
                result.setStripeBankAccount(invSettings.getStripePaymentAccount().getAsSelectItem());
            }

            result.setGoogleCheckoutMerchant(invSettings.getMerchantId());
            if (invSettings.getGoogleCheckoutPaymentAccount() != null) {
                result.setGoogleCheckoutBankAccount(invSettings.getGoogleCheckoutPaymentAccount().getAsSelectItem());
            }

            result.setMastercardMerchantID(invSettings.getMasterCardMerchandID());
            result.setMastercardAccessCode(invSettings.getMasterCardAccessCode());
            result.setMastercardSecretKey(invSettings.getMasterCardSecretKey());
            if (invSettings.getMasterCardPaymentAccount() != null) {
                result.setMastercardBankAccount(invSettings.getMasterCardPaymentAccount().getAsSelectItem());
            }

            result.setMastercardMerchantID(invSettings.getMasterCardMerchandID());
            result.setMastercardAccessCode(invSettings.getMasterCardAccessCode());
            result.setMastercardSecretKey(invSettings.getMasterCardSecretKey());
            if (invSettings.getMasterCardPaymentAccount() != null) {
                result.setMastercardBankAccount(invSettings.getMasterCardPaymentAccount().getAsSelectItem());
            }

            result.setElavonMerchantID(invSettings.getElavonMerchandID());
            result.setElavonUserID(invSettings.getElavonUserID());
            result.setElavonPIN(invSettings.getElavonPIN());
            if (invSettings.getElavonPaymentAccount() != null) {
                result.setElavonBankAccount(invSettings.getElavonPaymentAccount().getAsSelectItem());
            }
            result.setPayMeMerchantId(invSettings.getPayMeMerchantId());
            result.setPaymeServiceId(invSettings.getPaymeServiceId());
            result.setPaymeServiceFee(invSettings.getPaymeServiceFee());
            result.setClickMerchantId(invSettings.getClickMerchantId());
            result.setClickServiceId(invSettings.getClickServiceId());
            result.setRevolutEmail(invSettings.getRevolutEmail());
            result.setRevolutSecretApiKey(invSettings.getRevolutSecretApiKey());
            Optional.ofNullable(invSettings.getPaymePaymentAccount()).ifPresent(paymePaymentAccount -> result.setPaymeBankAccount(paymePaymentAccount.getAsSelectItem()));
            Optional.ofNullable(invSettings.getPaymeExpenseAccount()).ifPresent(paymeExpenseAccount -> result.setPaymeExpenseAccount(paymeExpenseAccount.getAsSelectItem()));
            Optional.ofNullable(invSettings.getClickPaymentAccount()).ifPresent(clickPaymentAccount -> result.setClickBankAccount(clickPaymentAccount.getAsSelectItem()));
            Optional.ofNullable(invSettings.getRevolutPaymentAccount()).ifPresent(revolutPaymentAccount -> result.setRevolutBankAccount(revolutPaymentAccount.getAsSelectItem()));
            Optional.ofNullable(invSettings.getRevolutExpenseAccount()).ifPresent(revolutExpenseAccount -> result.setRevolutExpenseAccount(revolutExpenseAccount.getAsSelectItem()));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public IntegrationItem getIntegrationItem() {
        IntegrationItem result = new IntegrationItem();
        result.setRecurrenceItem(this.getJob());
        result.setUserOffice365Linked(office365AuthService.isUserLinked(Constants.OFFICE_365));
        result.setUserOffice365AccessToken(office365AuthService.getUserAccessToken(EdsContextParams.getHost(), userManager.getUser(), Constants.OFFICE_365) != null);
        result.setUserOffice365Validated(googleCalendarManager.validateOfficeUser(userManager.getUser()));
        result.setUserGoogleValidated(googleCalendarManager.validateUser(userManager.getUser()));
        return result;
    }

    @Override
    @Transactional
    public void saveIntegrationItem(IntegrationItem item) {
        EdsCompany company = invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invoiceSettings = null;
        if (item.getObjectID() != null) {
            invoiceSettings = invoicingSettingsManager.get(item.getObjectID());
        } else if (invoicingSettingsManager.getInvoiceSettings(company) != null) {
            invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
        }
        if (invoiceSettings == null) {
            invoiceSettings = new EdsInvoicingSettings();
        }

        //Set Paypal account details
        if (item.isPayPal()) {
            invoiceSettings.setPayPalAccount(item.getPayPalMerchant());
            if (item.getPayPalBankAccount() != null && item.getPayPalBankAccount().getId() != null) {
                invoiceSettings.setPayPalPaymentAccount(accountingManager.get(item.getPayPalBankAccount().getId()));
            } else {
                invoiceSettings.setPayPalPaymentAccount(null);
            }
        } else {
            //Set Stripe account details
            if (item.getStripeBankAccount() != null && item.getStripeBankAccount().getId() != null) {
                invoiceSettings.setStripePaymentAccount(accountingManager.get(item.getStripeBankAccount().getId()));
            } else {
                invoiceSettings.setStripePaymentAccount(null);
            }
        }

        invoiceSettings.setMerchantId(item.getGoogleCheckoutMerchant());
        if (item.getGoogleCheckoutBankAccount() != null && item.getGoogleCheckoutBankAccount().getId() != null) {
            invoiceSettings.setGoogleCheckoutPaymentAccount(accountingManager.get(item.getGoogleCheckoutBankAccount().getId()));
        } else {
            invoiceSettings.setGoogleCheckoutPaymentAccount(null);
        }

        //Mastercard
        invoiceSettings.setMasterCardMerchandID(item.getMastercardMerchantID());
        invoiceSettings.setMasterCardAccessCode(item.getMastercardAccessCode());
        invoiceSettings.setMasterCardSecretKey(item.getMastercardSecretKey());
        if (item.getMastercardBankAccount() != null && item.getMastercardBankAccount().getId() != null) {
            invoiceSettings.setMasterCardPaymentAccount(accountingManager.get(item.getMastercardBankAccount().getId()));
        } else {
            invoiceSettings.setMasterCardPaymentAccount(null);
        }

        if (StringUtils.isNotBlank(item.getPayMeMerchantId())) {
            invoiceSettings.setPayMeMerchantId(item.getPayMeMerchantId());
        }

        if (StringUtils.isNotBlank(item.getPaymeServiceId())) {
            invoiceSettings.setPaymeServiceId(item.getPaymeServiceId());
        } else {
            invoiceSettings.setPaymeServiceId(null);
        }

        if (item.getPaymeServiceFee() != null) {
            invoiceSettings.setPaymeServiceFee(item.getPaymeServiceFee());
        } else {
            invoiceSettings.setPaymeServiceFee(null);
        }

        if (StringUtils.isNotBlank(item.getClickMerchantId())) {
            invoiceSettings.setClickMerchantId(item.getClickMerchantId());
        } else {
            invoiceSettings.setClickMerchantId(null);
        }

        if (StringUtils.isNotBlank(item.getClickServiceId())) {
            invoiceSettings.setClickServiceId(item.getClickServiceId());
        } else {
            invoiceSettings.setClickServiceId(null);
        }


        if (item.getPaymeBankAccount() != null && item.getPaymeBankAccount().getId() != null) {
            invoiceSettings.setPaymePaymentAccount(accountingManager.get(item.getPaymeBankAccount().getId()));
        } else {
            invoiceSettings.setPaymePaymentAccount(null);
        }

        if (item.getPaymeExpenseAccount() != null && item.getPaymeExpenseAccount().getId() != null) {
            invoiceSettings.setPaymeExpenseAccount(accountingManager.get(item.getPaymeExpenseAccount().getId()));
        } else {
            invoiceSettings.setPaymeExpenseAccount(null);
        }

        if (item.getClickBankAccount() != null && item.getClickBankAccount().getId() != null) {
            invoiceSettings.setClickPaymentAccount(accountingManager.get(item.getClickBankAccount().getId()));
        } else {
            invoiceSettings.setClickPaymentAccount(null);
        }

        if (StringUtils.isNotBlank(item.getRevolutEmail())) {
            invoiceSettings.setRevolutEmail(item.getRevolutEmail());
        } else {
            invoiceSettings.setRevolutEmail(null);
        }

        if (StringUtils.isNotBlank(item.getRevolutSecretApiKey())) {
            invoiceSettings.setRevolutSecretApiKey(item.getRevolutSecretApiKey());
        } else {
            invoiceSettings.setRevolutSecretApiKey(null);
        }

        if (item.getRevolutBankAccount() != null && item.getRevolutBankAccount().getId() != null) {
            invoiceSettings.setRevolutPaymentAccount(accountingManager.get(item.getRevolutBankAccount().getId()));
        } else {
            invoiceSettings.setRevolutPaymentAccount(null);
        }

        if (item.getRevolutExpenseAccount() != null && item.getRevolutExpenseAccount().getId() != null) {
            invoiceSettings.setRevolutExpenseAccount(accountingManager.get(item.getRevolutExpenseAccount().getId()));
        } else {
            invoiceSettings.setRevolutExpenseAccount(null);
        }

        //Elavon
        invoiceSettings.setElavonMerchandID(item.getElavonMerchantID());
        invoiceSettings.setElavonUserID(item.getElavonUserID());
        invoiceSettings.setElavonPIN(item.getElavonPIN());
        if (item.getElavonBankAccount() != null && item.getElavonBankAccount().getId() != null) {
            invoiceSettings.setElavonPaymentAccount(accountingManager.get(item.getElavonBankAccount().getId()));
        } else {
            invoiceSettings.setElavonPaymentAccount(null);
        }
        if (item.getObjectID() != null) {
            invoicingSettingsManager.update(invoiceSettings);
        } else {
            invoicingSettingsManager.create(invoiceSettings);
        }
    }

    @Override
    public ListResult<PropertyItem> getPropertyItems(ListingFilterParameter filterParameter) {
        List<EdsProperty> items = propertManager.list(filterParameter);
        EdsUser user = userManager.getUser();
        String host = EdsContextParams.getHost(user.getCompany().getObjectID());

        ArrayList<PropertyItem> list = items.stream()
                .map(x -> {
                    PropertyItem property = x.toItem(true);
                    if (property.isCustom()) {
                        String url = "";
                        String urlWithAccess = "";
                        ModuleEnum module = ModuleEnum.getModule(property.getModule());
                        if (Constants.PAGE.equals(property.getType())) {
                            List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(property.getfID());
                            if (customFormItems != null && !customFormItems.isEmpty()) {
                                property.setSelectedItemID(customFormItems.get(0));
                            }
                            if (property.getSelectedItemID() != null) {
                                url = host + "/" + (module != null ? module.getUrl() : "") + "#" + Constants.ITEM_LIST + "|summary/" + property.getSelectedItemID() + "/" + property.getfID() + "/" + property.getFormID() + "/" + property.getPlural() + "/PAGE";
                                urlWithAccess = host + "/" + (module != null ? module.getUrl() : "") + "?link=" +
                                        EncryptionHelper.encryptURL("#" + Constants.ITEM_LIST + "|summary/" + property.getSelectedItemID() + "/" + property.getfID() + "/" + property.getFormID() + "/" + property.getPlural() + "/PAGE") +
                                        "&" + Constants.C_ID + "=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString()) + "&" + Constants.U_ID + "=" + EncryptionHelper.encryptURL(user.getObjectID().toString());
                            }
                        } else {
                            String section = containerItemManager.getContainer(property.getId(), property.getModule());
                            url = host + "/" + (module != null ? module.getUrl() : "") + "#" + section + "|" + "custom_form_" + property.getfID();
                            urlWithAccess = host + "/" + (module != null ? module.getUrl() : "") + "?link=" + EncryptionHelper.encryptURL("#" + section + "|" + "custom_form_" + property.getfID()) + "&" + Constants.C_ID + "=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString()) + "&" + Constants.U_ID + "=" + EncryptionHelper.encryptURL(user.getObjectID().toString());
                        }
                        property.setLink(url);
                        property.setLinkWithAccess(urlWithAccess);
                    }
                    return property;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        int totalCount = propertManager.count(filterParameter);

        return new ListResult<>(list, totalCount);
    }

    @Override
    public PropertyItem getPropertyItem(Integer objectID, String moduleCode) {
        if (objectID == null) {
            return null;
        }
        EdsProperty property = propertManager.get(objectID);

        PropertyItem propertyItem = property.toItem(true);

        LinkedList<SelectItem> containerList = new LinkedList<>();

        if (moduleCode != null) {
            List<EdsContainer> containers = containerManager.getContainerBySorder(moduleCode);
            if (containers != null && containers.size() > 0) {
                for (EdsContainer container : containers) {
                    SelectItem containerItem = new SelectItem();
                    containerItem.setId(container.getObjectID());
                    containerItem.setName(container.isChanged() ? container.getDefaultName() : commonLocalizer.localize(container.getDefaultName()));
                    containerItem.setOrderId(container.getSorder());
                    containerList.add(containerItem);
                }
            }
            propertyItem.setSections(containerList);

            EdsContainerItem containerItem = containerItemManager.getItem(property.getObjectID(), moduleCode);
            if (containerItem != null && containerItem.getContainer() != null) {
                SelectItem selectItem = new SelectItem();
                selectItem.setId(containerItem.getContainer().getObjectID());
                selectItem.setName(containerItem.getContainer().isChanged() ? containerItem.getContainer().getDefaultName() : commonLocalizer.localize(containerItem.getContainer().getDefaultName()));
                selectItem.setOrderId(containerItem.getContainer().getSorder());
                propertyItem.setContainer(selectItem);
                propertyItem.setContainerItemId(containerItem.getObjectID());
            } else {
                containerItem = new EdsContainerItem();
                containerItem.setProperty(property);
                containerItem.setModuleCode(moduleCode);
                containerItemManager.create(containerItem);
                propertyItem.setContainerItemId(containerItem.getObjectID());
            }
        }

        return propertyItem;
    }

    @Override
    public Integer saveProperty(PropertyItem item) {
        if (item.getId() == null) {
            return null;
        }

        Boolean isNameChanged = Boolean.FALSE;
        EdsProperty property = propertManager.get(item.getId());
        isNameChanged = (property.getSingular() != null && !property.getSingular().equals(item.getSingular())) || (property.getPlural() != null && !property.getPlural().equals(item.getPlural())) || (property.getShortcut() != null && !property.getShortcut().equals(item.getShortcut()));
        property.setSingular(item.getSingularMain());
        property.setPlural(item.getPluralMain());
        property.setShortcut(item.getShortcut());
        if (isNameChanged) {
            property.setActive(isNameChanged);
        }
        property.setLastModifiedDate(new Date());
        property.setUserId(userManager.getUser().getObjectID());
        Gson gson = new Gson();
        property.setConvertItems(gson.toJson(item.getConvertItems()));

        if (item.getContainerItemId() != null && item.getContainer() != null) {
            EdsContainer container = containerManager.get(item.getContainer().getId());

            EdsContainerItem containerItem = containerItemManager.get(item.getContainerItemId());
            if (containerItem != null && container != null) {
                Integer maxSorder = containerItemManager.getMaxSorderByContainer(containerItem.getModuleCode(), container.getObjectID());

                containerItem.setContainer(containerManager.get(item.getContainer().getId()));
                containerItem.setSorder(maxSorder != null ? maxSorder + 1 : 0);

                containerItemManager.update(containerItem);
            }
        }
        property.setLName(savePropertyItemLocalization(item.getlName()));
        property.setlPlural(savePropertyItemLocalization(item.getlPlural()));
        propertManager.update(property);

        return item.getId();
    }

    private EdsCustomFormLocalization savePropertyItemLocalization(CustomFormLocalization localization) {
        if (localization == null) {
            return null;
        }
        EdsCustomFormLocalization cfLoc = customFormLocalizationManager.get(localization.getId());
        if (cfLoc == null) {
            cfLoc = new EdsCustomFormLocalization();
        }
        cfLoc.setArabicName(localization.getArabicName());
        cfLoc.setUzbekName(localization.getUzbekName());
        cfLoc.setEnglishName(localization.getEnglishName());
        cfLoc.setRussianName(localization.getRussianName());
        cfLoc.setDefaultName(localization.getDefaultName());
        customFormLocalizationManager.createOrUpdate(cfLoc);
        return cfLoc;
    }

    @Override
    public int updatePropertyStatus(Integer id) {
        if (id == null) {
            return 0;
        }
        EdsProperty property = propertManager.get(id);
        boolean status = property.getActive() == null ? Boolean.FALSE : property.getActive();
        property.setActive(!status);
        property.setLastModifiedDate(new Date());
        property.setUserId(userManager.getUser().getObjectID());
        propertManager.update(property);
        containerItemManager.updateByProperty(property.getObjectID(), !status);
        return id;
    }

    @Override
    public void resetFormProperty(PropertyItem propertyItem) {
        if (propertyItem != null && propertyItem.getId() != null) {
            EdsProperty property = propertManager.get(propertyItem.getId());
            if (property != null) {
                EdsProperty zeroProperty = propertManager.zeroSchemaProperty(property.getObjectName());
                if (zeroProperty != null) {
                    property.setActive(false);
                    property.setDefaultName(zeroProperty.getDefaultName());
                    property.setSingular(zeroProperty.getSingular());
                    property.setPlural(zeroProperty.getPlural());
                    property.setShortcut(zeroProperty.getShortcut());
                    property.setConvertItems(zeroProperty.getConvertItems());
                    property.setLastModifiedDate(new Date());
                    property.setUser(userManager.getUser());
                    propertManager.update(property);
                }
            }
        }
    }

    @Override
    public Integer deleteForm(Integer id) {

        EdsCustomForm customForm = customFormManager.get(id);
        if (customForm == null) {
            return 0;
        }

        String formId = customForm.getFormID();

        EdsModel model = modelManager.getCustomForm(formId);

        List<EdsCompanyCustomFieldsSettings> cfS = companyCFManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItems.name(), model.getViewName());
        for (EdsCompanyCustomFieldsSettings cf : cfS) {
            deleteCustomField(cf.getObjectID(), null, model.getFormID());
        }
        modelManager.delete(model);
        permissionManager.deletePermissionForCustomForm(formId);
        modelFieldManager.deleteFieldsByFormID(formId);

        EdsProperty property = customForm.getProperty();

        customForm.getRoles().clear();

        customFormManager.deleteCustom(customForm.getObjectID(), customForm.getFormID());

        if (property != null) {
            List<EdsContainerItem> containerItems = containerItemManager.getItemsByProperty(property.getObjectID());
            if (containerItems != null && !containerItems.isEmpty()) {
                for (EdsContainerItem containerItem : containerItems) {
                    containerItem = containerItemManager.merge(containerItem);
                    containerItemManager.delete(containerItem);
                }
            }
            customForm.setProperty(null);
            propertManager.delete(property);
        }

        listPanelSettingsManager.deleteListPanelSettings(formId);

        customFormManager.delete(customForm);

        return 1;
    }

    @Override
    public LinkedHashMap<SelectItem, LinkedList<PropertyItem>> loadAllListingsByModule(String section) {

        List<EdsContainer> containers = containerManager.getContainerBySorder(section);

        if (containers != null && containers.size() > 0) {
            LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap = new LinkedHashMap<>();
            for (EdsContainer container : containers) {

                LinkedList<PropertyItem> propertyItemList = new LinkedList<>();
                List<EdsContainerItem> containerItemList = containerItemManager.getItemsByContainer(container.getObjectID(), true);
                if (containerItemList != null && containerItemList.size() > 0) {
                    for (EdsContainerItem containerItem : containerItemList) {
                        PropertyItem propertyItem = containerItem.toItem();
                        propertyItemList.add(propertyItem);
                    }
                }
                SelectItem selectItem = new SelectItem();
                selectItem.setId(container.getObjectID());
                selectItem.setOrderId(container.getSorder());
                selectItem.setName(container.isChanged() ? container.getDefaultName() : commonLocalizer.localize(container.getDefaultName()));
                selectItem.setCode(container.getCode());
                selectItem.setSelected(container.getCustom());
                selectItem.setDescription(section);
                propertyListingsMap.put(selectItem, propertyItemList);
            }
            return propertyListingsMap;
        }
        return null;
    }

    @Override
    public void saveModuleSettings(String moduleName, LinkedHashMap<SelectItem, LinkedList<PropertyItem>> items) {

        for (LinkedList<PropertyItem> propertyItems : items.values()) {
            int i = 0;
            for (PropertyItem item : propertyItems) {
                EdsContainerItem containerItem = containerItemManager.get(item.getContainerItemId());
                if (containerItem != null) {
                    containerItem.setSorder(i);
                    containerItem.setActive(item.isActiveModule());
                    if (item.isActiveModule() && containerItem.getModule() != null) {
                        containerItem.getModule().setActive(true);
                    }
                    i++;
                    containerItemManager.update(containerItem);
                }
            }
        }
    }

    @Override
    public void deleteTab(Integer containerId, String name) {
        EdsContainer container = containerManager.get(containerId);
        if (container != null && name.equals(container.getModuleCode())) {
            containerManager.delete(container);
        }
    }

    @Override
    public void renameTabName(String name, Integer containerId) {
        EdsContainer container = containerManager.get(containerId);
        if (container != null) {
            container.setDefaultName(name);
            container.setChanged(true);
            containerManager.update(container);
        }
    }

    @Override
    public ListResult<RolePermissionHistoryItem> getPermissionLogHistoryList(ListingFilterParameter listingFilterParameter) {
        List<EdsRolePermissionHistory> historyList = rolePermissionHistoryManager.getHistoryList(listingFilterParameter, false);

        Integer total = rolePermissionHistoryManager.getHistoryList(listingFilterParameter, true).size();

        ArrayList<RolePermissionHistoryItem> list = new ArrayList<>();
        for (EdsRolePermissionHistory history : historyList) {
            RolePermissionHistoryItem historyItem = new RolePermissionHistoryItem();

            historyItem.setPermissionName(history.getPermissionName());
            historyItem.setModuleName(history.getContext());
            historyItem.setOldValue(history.getOldValue());
            historyItem.setNewValue(history.getNewValue());
            historyItem.setRoleName(history.getRoleName());
            if (history.getUpdater() != null) {
                historyItem.setUserID(history.getUpdater().getObjectID());
                historyItem.setUserName(history.getUpdater().getFullName());
            }
            if (history.getLastUpdateTime() != null) {
                historyItem.setUpdatedDate(history.getLastUpdateTime());
            }
            list.add(historyItem);
        }
        return new ListResult<>(list, total);
    }

    @Override
    public SelectItem saveNewTab(String moduleName, String name) {


        String tabCode = name.
                replaceAll("а", "a").
                replaceAll("б", "b").
                replaceAll("в", "v").
                replaceAll("г", "g").
                replaceAll("д", "d").
                replaceAll("е", "e").
                replaceAll("ё", "yo").
                replaceAll("ж", "zh").
                replaceAll("з", "z").
                replaceAll("и", "i").
                replaceAll("й", "j").
                replaceAll("к", "k").
                replaceAll("л", "l").
                replaceAll("м", "m").
                replaceAll("н", "n").
                replaceAll("о", "o").
                replaceAll("п", "p").
                replaceAll("р", "r").
                replaceAll("с", "s").
                replaceAll("т", "t").
                replaceAll("у", "u").
                replaceAll("ф", "f").
                replaceAll("х", "h").
                replaceAll("ц", "c").
                replaceAll("ч", "ch").
                replaceAll("ш", "sh").
                replaceAll("щ", "sch").
                replaceAll("ъ", "j").
                replaceAll("ы", "i").
                replaceAll("ь", "j").
                replaceAll("э", "e").
                replaceAll("ю", "yu").
                replaceAll("я", "ya").
                replaceAll("А", "A").
                replaceAll("Б", "B").
                replaceAll("В", "V").
                replaceAll("Г", "G").
                replaceAll("Д", "D").
                replaceAll("Е", "E").
                replaceAll("Ё", "Yo").
                replaceAll("Ж", "Zh").
                replaceAll("З", "Z").
                replaceAll("И", "I").
                replaceAll("Й", "J").
                replaceAll("К", "K").
                replaceAll("Л", "L").
                replaceAll("М", "M").
                replaceAll("Н", "N").
                replaceAll("О", "O").
                replaceAll("П", "P").
                replaceAll("Р", "R").
                replaceAll("С", "S").
                replaceAll("Т", "T").
                replaceAll("У", "U").
                replaceAll("Ф", "F").
                replaceAll("Х", "H").
                replaceAll("Ц", "C").
                replaceAll("Ч", "Ch").
                replaceAll("Ш", "Sh").
                replaceAll("Щ", "Sch").
                replaceAll("Ъ", "J").
                replaceAll("Ы", "I").
                replaceAll("Ь", "J").
                replaceAll("Э", "E").
                replaceAll("Ю", "Yu").
                replaceAll("Я", "Ya").
                replaceAll(" ", "_").
                replaceAll("()", "").
                toUpperCase();

        String code = tabCode.toUpperCase() + "_TAB";

        Integer maxSorder = containerManager.getMaxSorderByModule(moduleName);

        EdsCustomFormLocalization localization = new EdsCustomFormLocalization();
        localization.setDefaultName(name);
        localization.setEnglishName(name);
        localization.setArabicName(name);
        localization.setRussianName(name);
        localization.setUzbekName(name);
        customFormLocalizationManager.createOrUpdate(localization);

        EdsContainer container = new EdsContainer();
        container.setCode(code);
        container.setChanged(true);
        container.setDefaultName(name);
        container.setModuleCode(moduleName);
        container.setCustom(true);
        container.setSorder(maxSorder != null ? maxSorder + 1 : 0);
        container.setLocalization(localization);
        containerManager.createOrUpdate(container);

        SelectItem selectItem = new SelectItem();
        selectItem.setId(container.getObjectID());
        selectItem.setOrderId(container.getSorder());
        selectItem.setName(container.isChanged() ? container.getDefaultName() : commonLocalizer.localize(container.getDefaultName()));
        selectItem.setCode(container.getCode());
        selectItem.setSelected(container.getCustom());
        selectItem.setDescription(moduleName);

        return selectItem;
    }


    @Override
    public ListResult<EmployeeListItem> getAsteriskEmployeeList(Integer asteriskSettingId, ListingFilterParameter filterParametrs) {
        ListResult<EmployeeListItem> employeeList = employeeServiceLocal.getEmployeeList(filterParametrs);
        for (EmployeeListItem employeeListItem : employeeList.getList()) {
            EdsEmployeeAsterisk edsEmployeeAsterisk = employeeAsteriskManager.getEmployeeAsteriskSettings(asteriskSettingId, employeeListItem.getObjectID());
            if (edsEmployeeAsterisk != null && StringUtils.isNotBlank(edsEmployeeAsterisk.getUsername()) && StringUtils.isNotBlank(edsEmployeeAsterisk.getPassword())) {
                if (edsEmployeeAsterisk.isActive()) {
                    employeeListItem.setStatus("Connected");
                } else {
                    employeeListItem.setStatus("Not Connected");
                }
                employeeListItem.setAsteriskUsername(edsEmployeeAsterisk.getUsername());
            } else {
                employeeListItem.setStatus("Not Connected");
            }
        }
        return employeeList;
    }

    @Override
    public AsteriskSettings getAsteriskSettings(Integer employeeId, Integer asteriskSettingsId) {
        AsteriskSettings result = new AsteriskSettings();

        EdsEmployeeAsterisk edsEmployeeAsterisk = employeeAsteriskManager.getEmployeeAsteriskSettings(asteriskSettingsId, employeeId);
        if (edsEmployeeAsterisk != null) {
            result.setId(edsEmployeeAsterisk.getObjectID());
            result.setAsteriskUsername(edsEmployeeAsterisk.getUsername());
            result.setAsteriskPassword(edsEmployeeAsterisk.getPassword());
        }
        EdsEmployee employee = employeeManager.get(employeeId);
        if (employee != null) {
            EmployeeListItem user = new EmployeeListItem();
            user.setObjectID(employee.getObjectID());
            if (employee.getProfile() != null) {
                user.setEmployeeNumber(employee.getProfile().getEmployeeCode());
            }
            user.setFirstName(employee.getFirstName());
            user.setLastName(employee.getLastName());
            user.setEmail(employee.getEmail());
            user.setPhoneNumber(employee.getPrimaryPhone());
            if (employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getName() != null) {
                user.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
            }
            if (employee.getPosition() != null && employee.getPosition().getName() != null) {
                user.setPosition(employee.getPosition().getName());
            }
            result.setUser(user);
        }
        return result;
    }

    @Override
    public Integer saveEmployeeAsteriskSettings(AsteriskSettings item, Boolean active) {
        EdsEmployeeAsterisk employeeAsterisk = item != null && item.getId() != null ? employeeAsteriskManager.get(item.getId()) : new EdsEmployeeAsterisk();
        if (employeeAsterisk == null) {
            employeeAsterisk = new EdsEmployeeAsterisk();
        }
        if (employeeAsterisk.getAsteriskSettingsId() == null) {
            employeeAsterisk.setAsteriskSettingsId(item.getAsteriskSettingsId());
        }
        if (employeeAsterisk.getUserId() == null) {
            employeeAsterisk.setUserId(item.getUserId());
        }
        employeeAsterisk.setUsername(item.getAsteriskUsername());
        employeeAsterisk.setPassword(item.getAsteriskPassword());
        employeeAsterisk.setActive(active);
        employeeAsteriskManager.createOrUpdate(employeeAsterisk);
        return employeeAsterisk.getObjectID();
    }

    @Override
    public SelectItem getModuleLocalizeData(String section) {
        if (section != null) {
            EdsModuleLocalize edsModuleLocalize = moduleLocalizeManager.getByModuleCode(section);
            if (edsModuleLocalize != null) {
                return new SelectItem(edsModuleLocalize.getObjectID(), edsModuleLocalize.getName(), edsModuleLocalize.getModuleCode());
            }

        }
        return null;
    }

    @Override
    public void renameModuleName(String value, String section) {
        if (section != null) {
            EdsModuleLocalize edsModuleLocalize = moduleLocalizeManager.getByModuleCode(section);
            if (edsModuleLocalize == null) {
                edsModuleLocalize = new EdsModuleLocalize();
                edsModuleLocalize.setModuleCode(section);
            }
            edsModuleLocalize.setActive(true);
            edsModuleLocalize.setName(value);
            moduleLocalizeManager.createOrUpdate(edsModuleLocalize);
        }
    }

    @Override
    public void saveWorkflowWebHook(WorkflowWebHookItem item) {
        if (item.isPublic()) {
            savePublicWebHook(item);
        } else {
            EdsWorkflowWebHook entity;
            if (item.getId() != null) {
                entity = workflowWebHookManager.get(item.getId());
                webHookParameterManager.deleteAllParametersByWebHook(entity);
                webhookAttributeManager.deleteAllAttributeByWebHook(entity);
            } else {
                entity = new EdsWorkflowWebHook();
            }
            entity.setName(item.getName());
            entity.setDescription(item.getDescription());
            entity.setWorkflow(item.getWorkflow() != null ? workflowRuleManager.get(item.getWorkflow().getObjectID()) : null);
            if (item.isItemTable()) {
                entity.setItemTableForm(item.getFormId() != null ? ItemTableEnum.valueOf(item.getFormId()) : null);
                entity.setItemTableUuid(item.getUuid());
            } else {
                entity.setForm(item.getFormId() != null ? (EdsModelCustom) modelManager.get(item.getFormId(), true) : null);
            }
            entity.setResponseQuery(item.getQueryResponse());
            entity.setResponsedValue(item.getResponsedValue());
            entity.setMethod(WebHookMethod.valueOf(item.getMethod()));
            entity.setUrl(item.getRequestUrl());
            entity.setDateFormat(item.getDateFormat());
            entity.setSaveIntegrationId(item.isSaveIntegrationId());
            entity.setTableFieldName(item.getTableFieldName());

            EdsWebHookBody body;
            if (item.getId() != null) {
                body = entity.getBody();
            } else {
                body = new EdsWebHookBody();
            }
            body.setType(WebHookBodyType.valueOf(item.getBodyType()));
            body.setFormat(item.getRawDataFormat());
            body.setRawText(item.getRawDataText());
            webHookBodyManager.createOrUpdate(body);
            entity.setBody(body);

            workflowWebHookManager.createOrUpdate(entity);

            if (item.getHeaders().size() > 0) {
                for (String key : item.getHeaders().keySet()) {
                    EdsWebHookParameter header = new EdsWebHookParameter();
                    header.setWebHook(entity);
                    header.setType(WebHookParameterType.HEADER);
                    header.setName(key);
                    header.setValue(item.getHeaders().get(key));
                    webHookParameterManager.create(header);
                }
            }

            if (item.getFormDataParams() != null && !item.getFormDataParams().isEmpty()) {
                for (String key : item.getFormDataParams().keySet()) {
                    EdsWebHookParameter parameter = new EdsWebHookParameter();
                    parameter.setWebHookBody(entity.getBody());
                    parameter.setType(WebHookParameterType.BODY);
                    parameter.setName(key);
                    parameter.setValue(item.getFormDataParams().get(key));
                    webHookParameterManager.create(parameter);
                }
            }

            HashMap<String, String> queryAtributes = item.getQueryAtributes();

            if (item.getResponseAttributes() != null && !item.getResponseAttributes().isEmpty()) {
                for (String key : item.getResponseAttributes().keySet()) {
                    if (item.getResponseAttributes().get(key) != null && !"".equals(item.getResponseAttributes().get(key))) {
                        EdsWebhookAttribute attribute = new EdsWebhookAttribute();
                        attribute.setWebhookId(entity.getObjectID());
                        attribute.setKey(key);
                        if (queryAtributes != null && queryAtributes.get(key) != null && !queryAtributes.get(key).isEmpty()) {
                            attribute.setQueryValue(queryAtributes.get(key));
                        }
                        attribute.setValue(item.getResponseAttributes().get(key));
                        webhookAttributeManager.create(attribute);
                    }
                }
            }
            EdsWebHookHistory edsWebHookHistory = new EdsWebHookHistory();
            edsWebHookHistory.setWebHookId(entity.getObjectID());
            edsWebHookHistory.setName(entity.getName());
            edsWebHookHistory.setDescription(entity.getDescription());
            edsWebHookHistory.setWorkflowId(entity.getWorkflow() != null ? entity.getWorkflow().getObjectID() : null);
            edsWebHookHistory.setMethod(entity.getMethod().name());
            edsWebHookHistory.setUrl(entity.getUrl());
            edsWebHookHistory.setDateFormat(entity.getDateFormat());
            edsWebHookHistory.setShortText(body.getRawText());
            edsWebHookHistory.setBodyType(body.getType().name());
            edsWebHookHistory.setBodyFormat(body.getFormat());
            edsWebHookHistory.setSaveIntegrationId(entity.isSaveIntegrationId());
            edsWebHookHistory.setResponseQuery(entity.getResponseQuery());
            edsWebHookHistory.setResponsedValue(entity.getResponsedValue());
            edsWebHookHistory.setTableFieldName(entity.getTableFieldName());
            edsWebHookHistory.setUserId(userManager.getUser().getObjectID());
            edsWebHookHistory.setDate(new Date());
            webHookHistoryManager.create(edsWebHookHistory);
        }
    }

    private void savePublicWebHook(WorkflowWebHookItem item) {
        EdsPublicWebHook entity;
        if (item.getId() != null) {
            entity = publicWebhookManager.get(item.getId());
            publicWebhookParameterManager.deleteAllParametersByWebHook(entity);
            publicWebhookAttributeManager.deleteAllAttributeByWebHook(entity);
        } else {
            entity = new EdsPublicWebHook();
        }
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());
        entity.setResponseQuery(item.getQueryResponse());
        entity.setResponsedValue(item.getResponsedValue());
        entity.setMethod(WebHookMethod.valueOf(item.getMethod()));
        entity.setUrl(item.getRequestUrl());
        entity.setSaveIntegrationId(item.isSaveIntegrationId());

        EdsPublicWebHookBody body;
        if (item.getId() != null) {
            body = entity.getBody();
        } else {
            body = new EdsPublicWebHookBody();
        }
        body.setType(WebHookBodyType.valueOf(item.getBodyType()));
        body.setFormat(item.getRawDataFormat());
        body.setRawText(item.getRawDataText());
        publicWebhookBodyManager.createOrUpdate(body);
        entity.setBody(body);

        publicWebhookManager.createOrUpdate(entity);

        if (!item.getHeaders().isEmpty()) {
            for (String key : item.getHeaders().keySet()) {
                EdsPublicWebHookParameter header = new EdsPublicWebHookParameter();
                header.setWebHook(entity);
                header.setType(WebHookParameterType.HEADER);
                header.setName(key);
                header.setValue(item.getHeaders().get(key));
                publicWebhookParameterManager.create(header);
            }
        }

        if (item.getFormDataParams() != null && !item.getFormDataParams().isEmpty()) {
            for (String key : item.getFormDataParams().keySet()) {
                EdsPublicWebHookParameter parameter = new EdsPublicWebHookParameter();
                parameter.setWebHookBody(entity.getBody());
                parameter.setType(WebHookParameterType.BODY);
                parameter.setName(key);
                parameter.setValue(item.getFormDataParams().get(key));
                publicWebhookParameterManager.create(parameter);
            }
        }

        HashMap<String, String> queryAtributes = item.getQueryAtributes();

        if (item.getResponseAttributes() != null && !item.getResponseAttributes().isEmpty()) {
            for (String key : item.getResponseAttributes().keySet()) {
                if (item.getResponseAttributes().get(key) != null && !"".equals(item.getResponseAttributes().get(key))) {
                    EdsPublicWebhookAttribute attribute = new EdsPublicWebhookAttribute();
                    attribute.setWebhookId(entity.getObjectID());
                    attribute.setKey(key);
                    if (queryAtributes != null && queryAtributes.get(key) != null && !queryAtributes.get(key).isEmpty()) {
                        attribute.setQueryValue(queryAtributes.get(key));
                    }
                    attribute.setValue(item.getResponseAttributes().get(key));
                    publicWebhookAttributeManager.create(attribute);
                }
            }
        }
    }

    @Override
    public ListResult<SelectItem> getWebHookResponses(ListingFilterParameter fp) {
        List<EdsWebHookResponse> responses = webHookResponseManager.list(fp);
        EdsUser user = userManager.getUser();
        ListResult<SelectItem> result = new ListResult<>();
//        if (responses != null) {
//            result.setTotal(responses.size());
//            result.setList((ArrayList<SelectItem>) responses.stream().map(r -> new SelectItem(null, r.getResponse(), ServerUtils.longDateFormat(r.getCreatedDate(), user, true))).collect(Collectors.toList()));
//        }
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        if (responses != null) {
            for (EdsWebHookResponse webhookResponse : responses) {
                EdsWorkflowWebHook webhook = new EdsWorkflowWebHook();
                if (webhookResponse.getWebHook() != null && webhookResponse.getWebHook().getObjectID() != null) {
                    webhook = workflowWebHookManager.get(webhookResponse.getWebHook().getObjectID());
                }
                SelectItem selectItem = new SelectItem();
                selectItem.setId(webhookResponse.getObjectID());
                selectItem.setName(webhookResponse.getResponse());
                selectItem.setDescription(ServerUtils.longDateFormat(webhookResponse.getCreatedDate(), user, true));
                selectItem.setCategory(webhook.getName());
                selectItem.setColorId(webhook.getObjectID());
                selectItem.setWorkflowRuleName(webhook.getWorkflow() != null ? webhook.getWorkflow().getName() : "");
                selectItem.setNumber(webhookResponse.getBody());
                selectItem.setParam(webhookResponse.getStatus());

                selectItems.add(selectItem);
            }
            result.setTotal(webHookResponseManager.getUnDeletedItemCount(fp.getRelationID()));
            result.setList(selectItems);
        }
        return result;
    }

    public ListResult<SelectItem> getWebHookResponsesByType(Integer typeId, String type) {
        List<EdsWebHookResponse> responses = webHookResponseManager.listByType(typeId, type);
        EdsUser user = userManager.getUser();
        ListResult<SelectItem> result = new ListResult<>();
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        if (responses != null) {
            for (EdsWebHookResponse webhookResponse : responses) {
                EdsWorkflowWebHook webhook = new EdsWorkflowWebHook();
                if (webhookResponse.getWebHook() != null && webhookResponse.getWebHook().getObjectID() != null) {
                    webhook = workflowWebHookManager.get(webhookResponse.getWebHook().getObjectID());
                }
                SelectItem selectItem = new SelectItem();
                selectItem.setName(webhookResponse.getResponse());
                selectItem.setDescription(ServerUtils.longDateFormat(webhookResponse.getCreatedDate(), user, true));
                selectItem.setCategory(webhook.getName());
                selectItem.setColorId(webhook.getObjectID());
                selectItem.setWorkflowRuleName(webhook.getWorkflow() != null ? webhook.getWorkflow().getName() : "");
                selectItem.setParam(webhookResponse.getStatus());
                selectItem.setNumber(webhookResponse.getBody());
                selectItems.add(selectItem);
            }
            result.setTotal(responses.size());
            result.setList(selectItems);
        }
        return result;
    }

    @Override
    public WorkflowTelegramAlert getWorkflowTelegramAlert() {
        EdsWorkflowTelegramAlert alert = new EdsWorkflowTelegramAlert();
        alert.setWorkflow(null);
        WorkflowTelegramAlert item = alert.toRPC();
        item.setTelegramBot(globalAuthJdbcSpringManager.getTelegramSettingsItem(alert.getTelegramBotId()));
        SelectItem[] telegramBots = globalAuthJdbcSpringManager.getTelegramSettingItems(new ListingFilterParameter())
                .getList()
                .stream()
                .map(x -> new SelectItem(x.getId(), x.getBotName(), x.getToken()))
                .toArray(SelectItem[]::new);
        item.setTelegramBots(telegramBots);
        item.setReceiverAttributes(alert.getReceiverAttributes());

        return item;
    }

    @Override
    public void saveRecruitmentIntegrationItem(RecruitmentIntegrationItem item) {
        EdsRecruitmentIntegration credentials = recruitmentIntegrationManager.getCompanyCredentials();
        if (credentials == null) {
            credentials = new EdsRecruitmentIntegration();
        }
        if (item.getHhClientId() != null && item.getHhClientSecret() != null) {
            credentials.setHhClientId(item.getHhClientId());
            credentials.setHhClientSecret(item.getHhClientSecret());
        }
        if (item.getZoomClientId() != null && item.getZoomClientSecret() != null) {
            credentials.setZoomClientId(item.getZoomClientId());
            credentials.setZoomClientSecret(item.getZoomClientSecret());
        }


        if (item.getBotToken() != null && item.getBotUsername() != null) {

            String url = "https://kpitgbot.kpi.com/api/v1/company/saveCompanyBot";
//            String url = "https://3a40-195-158-24-170.ngrok-free.app/api/v1/company/saveCompanyBot";
            // Build the JSON payload
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("kpiAuth", ServerSecurityContext.getInstance().getSessionId());
            requestBody.put("token", item.getBotToken());
            requestBody.put("username", item.getBotUsername());

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Wrap body and headers in HttpEntity
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            // Send request
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            // Optional: Handle response
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ Telegram bot credentials sent successfully");
                credentials.setTelegramBotToken(item.getBotToken());
                credentials.setTelegramBotUsername(item.getBotUsername());
            } else {
                log.error("❌ Failed to send credentials: {}", response.getStatusCode());
            }
        }

        recruitmentIntegrationManager.createOrUpdate(credentials);
    }

    @Override
    public RecruitmentIntegrationItem getRecruitmentIntegrationItem() {
        EdsRecruitmentIntegration credentials = recruitmentIntegrationManager.getCompanyCredentials();
        RecruitmentIntegrationItem item = null;
        if (credentials != null) {
            item = new RecruitmentIntegrationItem();
            item.setHhClientId(credentials.getHhClientId());
            item.setHhClientSecret(credentials.getHhClientSecret());
            item.setZoomClientId(credentials.getZoomClientId());
            item.setZoomClientSecret(credentials.getZoomClientSecret());
            item.setBotToken(credentials.getTelegramBotToken());
            item.setBotUsername(credentials.getTelegramBotUsername());
        }
        return item;
    }

    @Override
    public void retryWebhook(Integer id) {
        EdsWebHookResponse response = webHookResponseManager.get(id);
        if (response != null) {
            EdsTraceable object = allInOneServiceLocal.getTraceableDomainObject(response.getTypeId(), response.getType());
            allInOneServiceLocal.runWebHooks(response.getWebHook().getWorkflow(), response.getType(), object, true, response);
        }
    }

    @Override
    public PredefinedValueItem getPredefinedValueRoles(Integer customFieldId, String value) {
        EdsCompanyCustomFieldsSettings cf = companyCFManager.get(customFieldId);
        PredefinedValueItem item = new PredefinedValueItem(customFieldId, value);
        if (cf != null) {
            item.setViewRoles(cf.getPredefinedValueViewRoles() != null ? cf.getPredefinedValueViewRoles().stream().filter(r -> r.getValue().equals(value)).map(r -> new SelectItem(r.getRole().getObjectID(), r.getRole().getName())).toList() : null);
            item.setViewEmployees(cf.getPredefinedValueViewEmployees() != null ? cf.getPredefinedValueViewEmployees().stream().filter(r -> r.getValue().equals(value)).map(r -> new SelectItem(r.getEmployee().getObjectID(), r.getEmployee().getFullName())).toList() : null);
            item.setChangeRoles(cf.getPredefinedValueChangeRoles() != null ? cf.getPredefinedValueChangeRoles().stream().filter(r -> r.getValue().equals(value)).map(r -> new SelectItem(r.getRole().getObjectID(), r.getRole().getName())).toList() : null);
            item.setChangeEmployees(cf.getPredefinedValueChangeEmployees() != null ? cf.getPredefinedValueChangeEmployees().stream().filter(r -> r.getValue().equals(value)).map(r -> new SelectItem(r.getEmployee().getObjectID(), r.getEmployee().getFullName())).toList() : null);
        }
        item.setAllRoles(getRoles());
        return item;
    }

    @Override
    public void savePredefinedValueRoles(PredefinedValueItem item) {
        EdsCompanyCustomFieldsSettings cf = companyCFManager.get(item.getId());
        if (cf != null) {
            dropdownValueRoleManager.deleteAllRoles(cf.getObjectID(), item.getName());
            dropdownValueEmployeeManager.deleteAllEmployees(cf.getObjectID(), item.getName());
            if (item.getViewRoles() != null) {
                item.getViewRoles().forEach(r -> {
                    EdsDropdownValueRole viewRole = new EdsDropdownValueRole();
                    viewRole.setValue(item.getName());
                    viewRole.setType("VIEW");
                    viewRole.setRole(roleManager.get(r.getId()));
                    viewRole.setCustomField(cf);
                    dropdownValueRoleManager.create(viewRole);
                });
            }
            if (item.getViewEmployees() != null) {
                item.getViewEmployees().forEach(r -> {
                    EdsDropdownValueEmployee viewEmp = new EdsDropdownValueEmployee();
                    viewEmp.setValue(item.getName());
                    viewEmp.setType("VIEW");
                    viewEmp.setEmployee(employeeManager.get(r.getId()));
                    viewEmp.setCustomField(cf);
                    dropdownValueEmployeeManager.create(viewEmp);
                });
            }
            if (item.getChangeRoles() != null) {
                item.getChangeRoles().forEach(r -> {
                    EdsDropdownValueRole viewRole = new EdsDropdownValueRole();
                    viewRole.setValue(item.getName());
                    viewRole.setType("CHANGE");
                    viewRole.setRole(roleManager.get(r.getId()));
                    viewRole.setCustomField(cf);
                    dropdownValueRoleManager.create(viewRole);
                });
            }
            if (item.getChangeEmployees() != null) {
                item.getChangeEmployees().forEach(r -> {
                    EdsDropdownValueEmployee viewEmp = new EdsDropdownValueEmployee();
                    viewEmp.setValue(item.getName());
                    viewEmp.setType("CHANGE");
                    viewEmp.setEmployee(employeeManager.get(r.getId()));
                    viewEmp.setCustomField(cf);
                    dropdownValueEmployeeManager.create(viewEmp);
                });
            }
        }
    }

    @Override
    public void savePayrollZone(SelectItem item) {
        EdsPayrollZone zone = item.getId() != null ? payrollZoneManager.getZone(item.getId()) : new EdsPayrollZone();
        zone.setName(item.getName());
        if (item.getRelatedItems() != null) {
            zone.setLocations(Arrays.stream(item.getRelatedItems()).map(l -> locationManager.get(l.getId())).collect(Collectors.toSet()));
        }
        payrollZoneManager.createOrUpdate(zone);
    }

    @Override
    public void deletePayrollZone(Integer id) {
        EdsPayrollZone zone = payrollZoneManager.getZone(id);
        zone.setDeleted(true);
        payrollZoneManager.update(zone);
    }

    @Override
    public SelectItem getPayrollZone(Integer id) {
        return Optional.ofNullable(payrollZoneManager.getZone(id)).map(EdsPayrollZone::getAsSelectItem).orElse(null);
    }

    @Override
    public ListResult<SelectItem> getMinimumWages(ListingFilterParameter fp) {
        List<EdsMinimumWage> wages = Optional.ofNullable(minimumWageManager.findMinimumWages()).orElse(new ArrayList<>());
        return new ListResult<>(wages.stream().map(EdsMinimumWage::getAsSelectItem).collect(Collectors.toCollection(ArrayList::new)), wages.size());
    }

    @Override
    public void saveMinimumWage(SelectItem item) {
        EdsMinimumWage wage = item.getId() != null ? minimumWageManager.getMinimumWage(item.getId()) : new EdsMinimumWage();
        wage.setAmount(item.getQtyAmount());
        wage.setEffectiveDate(item.getDate().getNonConvertedDate());
        wage.setType(item.getCode());
        minimumWageManager.createOrUpdate(wage);
    }

    @Override
    public void deleteMinimumWage(Integer id) {
        EdsMinimumWage wage = minimumWageManager.getMinimumWage(id);
        wage.setDeleted(true);
        minimumWageManager.update(wage);
    }

    @Override
    public SelectItem getMinimumWage(Integer id) {
        return Optional.ofNullable(minimumWageManager.getMinimumWage(id)).map(EdsMinimumWage::getAsSelectItem).orElse(null);
    }

    @Override
    public ListResult<SelectItem> getWageRates(ListingFilterParameter fp) {
        List<EdsWageRate> rates = Optional.ofNullable(wageRateManager.findWageRates()).orElse(new ArrayList<>());
        return new ListResult<>(rates.stream().map(w -> w.toRpc(false)).collect(Collectors.toCollection(ArrayList::new)), rates.size());
    }

    @Override
    public void saveWageRate(SelectItem item) {
        EdsWageRate rate = item.getId() != null ? wageRateManager.getWageRate(item.getId()) : new EdsWageRate();
        rate.setEffectiveDate(item.getDate().getNonConvertedDate());
        rate.setZone(item.getEntityId() != null ? payrollZoneManager.getZone(item.getEntityId()) : null);
        if (item.getId() != null) {
            wageRateItemManager.deleteWageRateItems(item.getId());
        }
        if (item.getRelatedItems() != null) {
            List<EdsWageRateItem> itemList = new ArrayList<>();
            for (SelectItem items : item.getRelatedItems()) {
                EdsWageRateItem rateItem = new EdsWageRateItem();
                rateItem.setRate(items.getQtyAmount());
                rateItem.setPosition(items.getEntityId() != null ? referenceManager.get(items.getEntityId()) : null);
                rateItem.setWageRate(rate);
                itemList.add(rateItem);
            }
            rate.setItems(itemList);
        }
        wageRateManager.createOrUpdate(rate);
    }

    @Override
    public void deleteWageRate(Integer id) {
        EdsWageRate wage = wageRateManager.getWageRate(id);
        wage.setDeleted(true);
        wageRateManager.update(wage);
    }

    @Override
    public SelectItem getWageRate(Integer id) {
        return Optional.ofNullable(wageRateManager.getWageRate(id)).map(w -> w.toRpc(true)).orElse(null);
    }

    @Override
    public ArrayList<UserCompanyDTO> getUserCompanies() {
        EdsUser user = userManager.getUser();
        String email = user.getEmail();
        if (email == null || email.isEmpty()) return new ArrayList<>();
        ArrayList<Integer> companyIds = (ArrayList<Integer>) globalAuthJdbcSpringManager.getUserCompanyIdsByEmailAndUserId(email, user.getObjectID());
        if (companyIds == null || companyIds.isEmpty()) return new ArrayList<>();

        List<EdsCompany> companies = companyManager.getCompaniesByIDs(ServerUtils.convertToString(companyIds));
        if (companies == null || companies.isEmpty()) return new ArrayList<>();
        Integer companyID = SecurityContext.getCompanyID();
        companies.removeIf(company -> company.getObjectID().equals(companyID));
        ArrayList<UserCompanyDTO> companyDTOs = new ArrayList<>();
        for (EdsCompany company : companies)
            companyDTOs.add(new UserCompanyDTO(company.getObjectID(), company.getName(), company.getActive()));

        return companyDTOs;
    }

    @Override
    public void updateNameFormat(ArrayList<Integer> companyIds, String format) {
        if (companyIds == null || companyIds.isEmpty() || format == null || format.isBlank()) return;
        companyIds.add(SecurityContext.getCompanyID());
        String splitedIds = ServerUtils.convertToString(companyIds);
        companySystemSettingsManager.updateNameFormat(splitedIds, format);

        for (Integer key : companyIds)
            RedisClient.setKey(String.valueOf(key), format, 18000);

        log.info("Name formta updated for this compnaies: {}", companyIds);
    }

    @Override
    public String updateDidoxCredentials(String inn, String password) throws Exception {
        String url = "https://api-partners.didox.uz/v1/auth/" + inn + "/password/uz";
        String request = String.format("{\"password\":\"%s\"}", password);

        HttpHeaders headers = new HttpHeaders() {{
            setContentType(MediaType.APPLICATION_JSON);
        }};
        String token;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), String.class);
            JSONObject response = new JSONObject(responseEntity.getBody());
            token = response.getString("token");
        } catch (Exception e) {
            throw e;
        }
        EdsDocumentIntegration credentials = documentIntegrationManager.getCompanyCredentials();
        if (credentials == null) {
            credentials = new EdsDocumentIntegration();
        }
        credentials.setDidoxInn(inn);
        credentials.setDidoxPassword(password);
        credentials.setDidoxToken(token);
        credentials.setDidoxTokenExpire(LocalDateTime.now().plusHours(6));
        documentIntegrationManager.createOrUpdate(credentials);
        return token;
    }

    @Override
    public String getDidoxInn() {
        EdsDocumentIntegration credentials = documentIntegrationManager.getCompanyCredentials();
        return credentials != null ? credentials.getDidoxInn() : null;
    }
}
