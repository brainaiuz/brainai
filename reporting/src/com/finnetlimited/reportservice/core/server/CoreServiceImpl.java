package com.finnetlimited.reportservice.core.server;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDynamicQuery;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsReportingExecuteTime;
import com.edatasite.workforce.core.domain.EdsReportingPermission;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsRolePermission;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadAmazonSettings;
import com.edatasite.workforce.core.domain.EdsUploadSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmEntityMailList;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsReportDataCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.core.domain.dashboard.EdsDefaultComponents;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipPayments;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.reporting.EdsChartConfig;
import com.edatasite.workforce.core.domain.reporting.EdsCompanyFavouriteReportTemplates;
import com.edatasite.workforce.core.domain.reporting.EdsKpiWidget;
import com.edatasite.workforce.core.domain.reporting.EdsKpiWidgetFilter;
import com.edatasite.workforce.core.domain.reporting.EdsReportTemplate;
import com.edatasite.workforce.core.domain.reporting.EdsReportTemplateCategory;
import com.edatasite.workforce.core.domain.reporting.EdsReportingDBUrl;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetFilterItem;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetItem;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelatedLinkRPC;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingDBUrlListItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RoleListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SavedReportTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.website.WidgetConstants;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvancePayment;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CrmEntityMailListManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormLocalizationManager;
import com.edatasite.workforce.gwt.core.server.db.DynamicQueryManager;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.MailListManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.ReportingExecuteTimeManager;
import com.edatasite.workforce.gwt.core.server.db.ReportingPermissionManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TelegramChatManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import com.edatasite.workforce.gwt.core.server.db.chart.ChartConfigManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ReportDataCFManager;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DefaultComponentsManager;
import com.edatasite.workforce.gwt.core.server.db.kpiWidget.KpiWidgetFilterManager;
import com.edatasite.workforce.gwt.core.server.db.kpiWidget.KpiWidgetManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipPaymentsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportTemplateCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportingDBUrlManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmMailingListEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.server.GwtUploadServlet;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowTelegramAlert;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.FolderType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.OperationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FilterRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.MailListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportDirectoryPathRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCustomizeFilter;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingRolePermissionItem;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.TableRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml.RpcConvertToXmlLocal;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.server.ReportItem;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SearchPeopleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.reporting.ReportData;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.ListItem;
import com.finnetlimited.reportservice.core.client.gwtrpc.ReportGenerateTableRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ReportTemplateCategoryRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ReportTemplateItem;
import com.finnetlimited.reportservice.core.client.gwtrpc.UserSecuritryRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.db.schema.CustomHtmlManager;
import com.finnetlimited.reportservice.core.server.db.schema.CustomizeReportManager;
import com.finnetlimited.reportservice.core.server.db.schema.FoldersManager;
import com.finnetlimited.reportservice.core.server.db.schema.ReportingManager;
import com.finnetlimited.reportservice.core.server.db.schema.TelegramReportingRecurrenceManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsCustomHtml;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsCustomizeReport;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsFolders;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsTelegramReportingScheduleRule;
import com.finnetlimited.reportservice.core.server.handler.ExcelReportHandler;
import com.finnetlimited.reportservice.core.server.parser.HTMLParser;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.service.TelegramReportingRecurrenceService;
import com.finnetlimited.reportservice.core.server.telegram.recurrence.utils.MessageContentDetails;
import com.finnetlimited.reportservice.core.server.utils.JdbcUtil;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import com.finnetlimited.reportservice.core.server.utils.StrUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.gwtwidgets.server.spring.ServletUtils;
import org.hibernate.Hibernate;
import org.jooq.impl.DSL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType.Between;
import static com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType.SamePeriodLastYear;

/**
 * User: Dilsh0d
 * Date: 04-Mar-2010
 * Time: 16:21:24
 */
@Service
@Transactional
public class CoreServiceImpl implements CoreServiceLocal, CoreService, Constants {

    private static final String ColumnFormat_NUMBER = "number";
    private static final String ColumnFormat_DOUBLE = "double";
    private static final String ColumnFormat_PERCENT = "percent";
    private static final String ColumnFormat_MONEY = "money";
    private static final String ColumnFormat_TIME = "time";
    private static final Logger log = LoggerFactory.getLogger(CoreServiceImpl.class);
    @Autowired
    protected UploadManager uploadManager;
    @Autowired
    protected ReferenceManager referenceManager;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    protected WfmMessageSource referenceWfmMessageSource;
    @Autowired
    CustomizeReportManager customizeReportManager;
    @Autowired
    TelegramChatService telegramChatService;
    @Autowired
    OpportunityManager opportunityManager;
    @Autowired
    CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    EventManager eventManager;
    @Autowired
    PayrollService payrollService;
    @Autowired
    PayrollServiceLocal payrollServiceLocal;
    @Autowired
    PayslipPaymentsManager payslipPaymentsManager;
    private String customDbUrl;
    @Qualifier("dataSourceXLS")
    private DataSource dataSourceXLS;
    @Autowired
    @Qualifier("wfmLocalizer")
    private WfmMessageSource wfmLocalizer;
    @Qualifier("slaveDataSource")
    private DataSource dataSourceWFM;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CompanyAttachmentManager companyAttachmentManager;
    @Autowired
    private FoldersManager foldersManager;
    @Autowired
    private ReportingManager reportingManager;
    @Autowired
    private ChartConfigManager chartConfigManager;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ReportTemplateManager reportTemplateManager;
    @Autowired
    private ReportTemplateCategoryManager reportTemplateCategoryManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private ReportingDBUrlManager reportingDBUrlManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CrmEntityMailListManager crmEntityMailListManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private MailListManager mailListManager;
    @Autowired
    private ReportingPermissionManager reportingPermissionManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private DefaultComponentsManager defaultComponentsManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private KpiWidgetManager kpiWidgetManager;
    @Autowired
    private KpiWidgetFilterManager kpiWidgetFilterManager;
    @Autowired
    private ReportingService reportingService;
    @Autowired
    private ReportingRecurrencePdfService recurrencePdfService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ReportDataCFManager reportDataCFManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private DynamicQueryManager dynamicQueryManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private TelegramChatManager telegramChatManager;
    @Autowired
    @Qualifier("reportingLocalizer")
    private WfmMessageSource reportingLocalizer;
    @Autowired
    private TelegramReportingRecurrenceManager telegramReportingRecurrenceManager;
    @Autowired
    private TelegramReportingRecurrenceService telegramReportingRecurrenceService;
    @Autowired
    private CustomFormLocalizationManager customFormLocalizationManager;
    @Autowired
    private ReportingExecuteTimeManager reportingExecuteTimeManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    CustomHtmlManager customHtmlManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;

    public static String replace(String text, String[] repl, String[] with) {

        StringBuilder buf = new StringBuilder(text);
        for (int i = 0; i < repl.length; i++) {
            text = buf.toString();
            if (text.contains(repl[i])) {
                buf.delete(0, buf.length());
                String[] arrays = text.split(repl[i]);
                for (String item : arrays) {
                    buf.append(item).append(with[i]);
                }
            }
        }
        return buf.toString();
    }

    public void setDataSourceXLS(DataSource dataSource) {
        this.dataSourceXLS = dataSource;
    }

    public void setDataSourceWFM(DataSource dataSource) {
        this.dataSourceWFM = dataSource;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FolderRpc getFolder(Integer id) {
        EdsFolders folder = foldersManager.get(id);
        FolderRpc folderRpc = new FolderRpc();
        if (folder.getCompanyid() != null) {
            folderRpc.setCompanyId(folder.getCompanyid().toString());
        }
        if (folder.getCategoryCode() != null && !"".equals(folder.getCategoryCode())) {
            EdsReportTemplateCategory category = reportTemplateCategoryManager.getReportTemplateCategoryByCode(folder.getCategoryCode());
            folderRpc.setCategoryId(category.getObjectID());
            folderRpc.setCategoryName(referenceWfmMessageSource.localize(category.getCode()));
        }
        folderRpc.setId(folder.getObjectID());
        folderRpc.setName(folder.getName());
        folderRpc.setType(folder.getType());
        folderRpc.setDescription(folder.getDescription());

        return folderRpc;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FolderRpc getFolderByReportId(Integer id) {
        EdsReport report = reportingManager.get(id);
        FolderRpc folder = new FolderRpc();
        SelectListRpc select = new SelectListRpc();
        folder.setName(report.getFolder().getName());
        select.setName(report.getName());
        if (report.getDescription() != null) {
            select.setDescription(report.getDescription());
        } else {
            select.setDescription("");
        }

        RecurrenceJobItem recurrenceJobItem = recurrenceService.createRecurrenceItemByUser(report.getObjectID(), SchedulerConstant.RECURRING_REPORT, userManager.getUser());
        if (recurrenceJobItem != null) {
            folder.setRecurrenceJobItem(recurrenceJobItem);
            folder.setRecurrenceId(recurrenceJobItem.getObjectId());
        }

        ArrayList<SelectListRpc> reportData = new ArrayList<>();
        reportData.add(select);
        folder.setReports(reportData);

        return folder;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<SelectListRpc> getFolderList() {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();   //view single task
        kpiLog.setEntityName(EdsFolders.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get folder list");

        UserSecuritryRpc user = getUser();
        List<EdsFolders> list = foldersManager.list(user.getDomainName(), user.getCompanyId(), user.getUserId());
        ArrayList<Integer> has = new ArrayList<>();
        ArrayList<SelectListRpc> selectList = new ArrayList<>();
        for (EdsFolders folder : list) {
            if (has.contains(folder.getObjectID()) || folder.getCategoryCode() != null) {
                continue;
            }
            has.add(folder.getObjectID());
            SelectListRpc select = new SelectListRpc();
            select.setId(folder.getObjectID());
            select.setName(folder.getName());
            select.setType(folder.getType());
            selectList.add(select);
        }
        return selectList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getFolderTypeByReportId(Integer reportId) {
        EdsReport report = reportingManager.get(reportId);

        return report.getFolder().getType();
    }

    @Transactional
    public boolean saveFolder(FolderRpc folder) {
        UserSecuritryRpc user = getUser();
        boolean p = foldersManager.isFolderYes(folder, user.getCompanyId(), true);
        if (p) {
            EdsFolders edsFolders = new EdsFolders();
            edsFolders.setName(folder.getName());
            edsFolders.setDescription(folder.getDescription());
            String inputType = folder.getType();
            if ("Общее".equalsIgnoreCase(inputType) || "Ommaviy".equalsIgnoreCase(inputType)) {
                folder.setType(FolderType.Public.name());
            } else if ("Личное".equalsIgnoreCase(inputType) || "Shaxsiy".equalsIgnoreCase(inputType)) {
                folder.setType(FolderType.Private.name());
            } else {
                folder.setType(inputType);
            }
            edsFolders.setType(folder.getType());
            edsFolders.setDate(new Date());
            edsFolders.setDomainName(user.getDomainName());
            edsFolders.setUserid(user.getUserId());
            edsFolders.setCompanyid(user.getCompanyId());
            edsFolders.getAuditInfo().setCreatedBy(foldersManager.getUser());
            edsFolders.getAuditInfo().setCreationDate(new Date());
            edsFolders.getAuditInfo().setModificationDate(new Date());
            if (folder.getCategoryId() != null) {
                EdsReportTemplateCategory category = reportTemplateCategoryManager.get(folder.getCategoryId());
                edsFolders.setCategoryCode(category.getCode());
            }
            foldersManager.create(edsFolders);

            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(edsFolders.getObjectID());
            kpiLog.setEntityName(EdsFolders.class.getSimpleName());
            kpiLog.setEntityType(edsFolders.getName());
            ServerUtils.kpiLog(log, kpiLog, "Added New Folder");
        }
        return p;
    }

    @Transactional
    public boolean updateFolder(FolderRpc folder) {
        UserSecuritryRpc user = getUser();
        boolean p = foldersManager.isFolderYes(folder, user.getCompanyId(), false);
        if (p) {
            EdsFolders edsFolders = foldersManager.get(folder.getId());
            edsFolders.setName(folder.getName());
            edsFolders.setDescription(folder.getDescription());
            edsFolders.setType(folder.getType());
            if (edsFolders.getAuditInfo().getCreationDate() == null) {
                edsFolders.getAuditInfo().setModificationDate(new Date());
            }
            edsFolders.getAuditInfo().setModificationDate(new Date());
            edsFolders.getAuditInfo().setModifiedBy(foldersManager.getUser());
            edsFolders.setDomainName(user.getDomainName());
            edsFolders.setUserid(user.getUserId());
            edsFolders.setCompanyid(user.getCompanyId());
            foldersManager.update(edsFolders);

            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(edsFolders.getObjectID());
            kpiLog.setEntityName(EdsFolders.class.getSimpleName());
            kpiLog.setEntityType(edsFolders.getName());
            ServerUtils.kpiLog(log, kpiLog, "Update Folder");
        }
        return p;
    }

    @Transactional
    public boolean deleteFolder(Integer id) {
        Boolean isDeleted = false;
        EdsFolders folder = foldersManager.get(id);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(folder.getObjectID());
        kpiLog.setEntityName(EdsFolders.class.getSimpleName());
        kpiLog.setEntityType(folder.getName());
        ServerUtils.kpiLog(log, kpiLog, "Delete Folder");

        if ("System".equals(folder.getType()) && "1".equals(getUser().getCompanyId().toString()) && getUser().getUserRoles().contains("5")) {
            isDeleted = true;
        } else if ("Private".equals(folder.getType()) && folder.getUserid().equals(getUser().getUserId())) {
            isDeleted = true;
        } else if ("Public".equals(folder.getType()) && getUser().getUserRoles().contains("5")) {
            isDeleted = true;
        }
        if (isDeleted) {
            List<EdsReport> reportList = reportingManager.getFolderReports(folder.getObjectID());
            for (EdsReport report : reportList) {
                if (report.getAuditInfo().getCreationDate() == null) {
                    report.getAuditInfo().setModificationDate(new Date());
                }
                report.getAuditInfo().setModificationDate(new Date());
                report.getAuditInfo().setModifiedBy(reportingManager.getUser());
                report.setDeleted(Boolean.TRUE);
                report.setFolder(null);
                reportingManager.update(report);
            }
            if (folder.getAuditInfo().getCreationDate() == null) {
                folder.getAuditInfo().setModificationDate(new Date());
            }
            folder.getAuditInfo().setModificationDate(new Date());
            folder.getAuditInfo().setModifiedBy(foldersManager.getUser());
            folder.setDeleted(Boolean.TRUE);
            foldersManager.update(folder);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportRpc getReport(Integer id) {
        return getReport(id, true);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int deleteRecurrence(Integer reportId, String ruleName) {
        return deleteTelegramRecurrence(reportId, ruleName);
    }

    private int deleteTelegramRecurrence(Integer reportId, String ruleName) {
        EdsReport report = reportingManager.get(reportId);
        if (report != null) {
            //Deleting recurrence
            EdsTelegramReportingScheduleRule edsTelegramReportingScheduleRule = telegramReportingRecurrenceManager.getRuleByReportIdAndName(reportId, ruleName);
            telegramReportingRecurrenceManager.delete(edsTelegramReportingScheduleRule);
            recurrenceService.deleteRecurrence(report.getObjectID(), SchedulerConstant.RECURRING_REPORT);

            //removing rule from telegram chat
            telegramChatManager.deleteTelegramChatsRuleIds(edsTelegramReportingScheduleRule.getObjectID());
        }
        return 1;
    }

    @Override
    public ReportData getReportDateForApi(MListingFilterParameter filterParameter) {
        try {
            ReportRpc reportRpc = getReport(filterParameter.getObjectId());
            reportRpc.setPosition(filterParameter.getStart() + 1);
            reportRpc.setLimit(filterParameter.getLimit());
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(reportRpc.getViewCode());
            reportRpc.setNoTimeZone(viewRpc.isNoTimezone());
            ResultSet resultSet = getTabularReportResult(reportRpc, null, false, filterParameter.getCustomReplacements());
            String resultSetToString = DSL.using(getDataSourceConnection(reportRpc)).fetch(resultSet).formatJSON();
            JSONObject parentJSONObject = (JSONObject) new JSONParser().parse(resultSetToString);
            ReportData data = new ReportData();
            data.setReportId(reportRpc.getId());
            data.setReportLink("Reporting.html#reporting|stepControl/" + reportRpc.getId() + "/savedreport/" + ServerUtils.encrypt(reportRpc.getName()));
            data.setReportType(reportRpc.getTableType());
            data.setReportData(parentJSONObject);
            if (!resultSet.isClosed()) {
                resultSet.close();
            }return data;

        } catch (SQLException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportRpc getReport(Integer id, boolean isRecurrence) {
        return getReport(id, null, isRecurrence, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportRpc getReport(Integer id, boolean isRecurrence, EdsUser user) {
        return getReport(id, user.getCompany().getObjectID(), isRecurrence, user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportRpc getReport(Integer id, Integer schemaCompanyId) {
        return getReport(id, schemaCompanyId, true, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportRpc getReport(Integer reportID, Integer schemaCompanyId, boolean isRecurrence, EdsUser user) {

        if (schemaCompanyId != null) {
            ServerSecurityContext.getInstance().setCompanyId(schemaCompanyId);
        }

        EdsReport report = reportingManager.get(reportID);

        if (report == null) {
            return new ReportRpc();
        }

        ReportRpc reportRpc = report.toRPC();
        HashMap<String, ColumnRpc> map = reportRpc.getColumnMap();
        if (getUser() != null && getUser().getUserId() != null
                && (report.getAuditInfo().getCreatedBy() != null && getUser().getUserId().equals(report.getAuditInfo().getCreatedBy().getObjectID())
                || report.getFolder() != null && getUser().getUserId().equals(report.getFolder().getObjectID()))
        ) {
            reportRpc.setOwner(true);
        }
        EdsReportTemplate edsReportTemplate = reportTemplateManager.getByCode(report.getViewCode());
        if (!(null == edsReportTemplate || null == edsReportTemplate.getName() || "".equals(edsReportTemplate.getName()))) {
            reportRpc.setViewName(edsReportTemplate.getName());
            reportRpc.setLibrary(edsReportTemplate.getLibrary());
        }
        if (isRecurrence && schemaCompanyId == null) {
            RecurrenceJobItem recurrenceJobItem = recurrenceService.createRecurrenceItemByUser(report.getObjectID(), SchedulerConstant.RECURRING_REPORT, user);
            if (recurrenceJobItem != null) {
                reportRpc.setRecurrenceJobItem(recurrenceJobItem);
                reportRpc.setRecurrenceId(recurrenceJobItem.getObjectId());
            }
        }
        for (int k = 0; k < reportRpc.getSelectedColumns().size(); k++) {
            ColumnRpc columnRpc = map.get(reportRpc.getSelectedColumns().get(k).getName());

            if (columnRpc != null) {
                if (reportRpc.getSelectedColumns().get(k).getDrillDownReport() != null) {
                    columnRpc.setDrillDownReport(reportRpc.getSelectedColumns().get(k).getDrillDownReport());
                }
                if (reportRpc.getSelectedColumns().get(k).getLinkedReportId() != null) {
                    columnRpc.setLinkedReportId(reportRpc.getSelectedColumns().get(k).getLinkedReportId());
                }
                if (reportRpc.getSelectedColumns().get(k).getFilterParametr() != null) {
                    columnRpc.setFilterParametr(reportRpc.getSelectedColumns().get(k).getFilterParametr());
                }
            }
        }

        if (report.getStandartFilterColumn() != null) {
            reportRpc.setSntFilterTitle(map.get(report.getStandartFilterColumn()).getTitle());
        }
        if (schemaCompanyId == null) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            kpiLog.setEntityId(reportRpc.getId());
            kpiLog.setEntityName(EdsReport.class.getSimpleName());
            kpiLog.setEntityType(reportRpc.getViewName() + "/" + reportRpc.getName());
            ServerUtils.kpiLog(log, kpiLog, "Get Report");
        }

        ArrayList<String> ruleNames = telegramReportingRecurrenceManager.getAllRuleNames(reportRpc.getId());
        reportRpc.setRuleNames(ruleNames);

        if (reportRpc.getChartConf() != null && userManager.getUser() != null) {
            reportRpc.getChartConf().setHasPermission(ServerUtils.hasReportingPermission(report.getPermissionCode(), userManager.getUser()));
        } else if (reportRpc.getKpiWidgetItem() != null && userManager.getUser() != null) {
            ChartConfItem chartConfItem = new ChartConfItem();
            chartConfItem.setHasPermission(ServerUtils.hasReportingPermission(report.getPermissionCode(), userManager.getUser()));
            reportRpc.setChartConf(chartConfItem);
        }

        return reportRpc;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<ReportDirectoryPathRpc> getReportTemplateList(ListingFilterParameter filter) {

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityId(null);
        kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
        kpiLog.setEntityType(null);
        ServerUtils.kpiLog(log, kpiLog, "Get Report List");

        Integer companyId = SecurityContext.getCompanyID();
        ArrayList<ReportDirectoryPathRpc> directoryList = new ArrayList<>();
        ArrayList<EdsReportTemplateCategory> templateCategories = reportTemplateCategoryManager.getReportTemplateCategoryList();
        if (templateCategories != null) {
            String categoryFromFilter = null;
            if (filter.getCategoryID() != null) {
                categoryFromFilter = reportTemplateCategoryManager.get(filter.getCategoryID()) != null ? reportTemplateCategoryManager.get(filter.getCategoryID()).getCode() : null;
            }
            ReportDirectoryPathRpc directoryPathRpc;
            ArrayList<SelectItem> fileList;
            ArrayList<EdsReportTemplate> templates = reportTemplateManager.getReportTemplateList(categoryFromFilter, userManager.getUser().getRolesCodeAsString(), companyId);
            if (templates != null && templates.size() > 0) {
                HashMap<String, ArrayList<EdsReportTemplate>> hashMap = new HashMap<>();
                for (EdsReportTemplate edsReportTemplate : templates) {
                    String categoryCode = edsReportTemplate.getCategoryCode();
                    hashMap.computeIfAbsent(categoryCode, k -> new ArrayList<>());
                    hashMap.get(categoryCode).add(edsReportTemplate);
                }

                if (hashMap.size() > 0) {
                    for (EdsReportTemplateCategory category : templateCategories) {
                        directoryPathRpc = new ReportDirectoryPathRpc();
                        directoryPathRpc.setDirectoryName(category.getName());
                        directoryPathRpc.setId(category.getObjectID());
                        fileList = new ArrayList<>();
                        if (hashMap.get(category.getCode()) != null) {
                            for (EdsReportTemplate template : hashMap.get(category.getCode())) {
                                SelectItem templateitem = new SelectItem(template.getObjectID(), reportingLocalizer.localize(template.getCode().trim().replace(" ", "_"), template.getName()));
                                templateitem.setNewItem(template.getAttachmentId() != null);
                                fileList.add(templateitem);
                            }
                            if (fileList.size() > 0) {
                                directoryPathRpc.setFiles(fileList);
                                directoryList.add(directoryPathRpc);
                            }
                        }
                    }
                }
            }
        }
        return directoryList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<SelectItem> getCustomItems(String query, ReportRpc report) {

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityId(report.getId());
        kpiLog.setEntityName(EdsReport.class.getSimpleName());
        kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
        ServerUtils.kpiLog(log, kpiLog, "Get custom items");
        if (report.getViewCode() != null) {
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
            report.setFromKpi(viewRpc.isFromKpi());
        }
        ArrayList<SelectItem> list = new ArrayList<>();
        try {
            Connection connection = getDataSourceConnection(report);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SelectItem item = new SelectItem();
                item.setId(resultSet.getInt("id"));
                item.setName(resultSet.getString("name"));
                list.add(item);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<FolderRpc> getReportListByUser() {

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityId(null);
        kpiLog.setEntityName(EdsReport.class.getSimpleName());
        kpiLog.setEntityType(null);
        ServerUtils.kpiLog(log, kpiLog, "Get report list by user");

        UserSecuritryRpc user = getUser();
        ArrayList<FolderRpc> folderList = new ArrayList<>();
        ArrayList<SelectListRpc> list = null;
        Integer folderId = null;
        String rolesCodeAsString = userManager.getUser().getRolesCodeAsString();
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setSubscriptionTypeName(getUser().getDomainName());
        filter.setCompanyID(getUser().getCompanyId());
        filter.setUserID(getUser().getUserId());
        filter.setRoles(rolesCodeAsString);
        List<Object[]> items = reportingManager.listObject(filter);
        for (Object[] item : items) {
            ReportItem reportItem = new ReportItem(item).invoke();

            if (folderId == null || !Objects.equals(reportItem.getFolderId(), folderId)) {
                folderId = reportItem.getFolderId();
                FolderRpc folder = new FolderRpc();
                folder.setId(folderId);
                folder.setName(reportItem.getFolderName());
                folder.setType(reportItem.getFolderType());
                list = new ArrayList<>();
                folder.setReports(list);
                folder.setCompanyId(user.getCompanyId().toString());
                folderList.add(folder);

            }
            SelectListRpc selectList = new SelectListRpc();
            selectList.setId(reportItem.getReportId());
            selectList.setType(reportItem.getReportType());
            selectList.setName(reportItem.getReportName());
            selectList.setDescription(reportItem.getReportDescription());
            list.add(selectList);
        }
        return folderList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<SelectItem> getCustomItems(String query) {
        return getCustomItems(query, null);
    }

    /**
     * Parsing xml template for drawing custom filter table.
     *
     * @return Transfer object {@link ViewRpc}
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ViewRpc getReportStructure(String viewCode) {
        return SqlQueryUtil.getViewParser(viewCode);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ReportingListItem> getReportingTemplateList(ListingFilterParameter filterParametrs) {

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityId(null);
        kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
        kpiLog.setEntityType(null);
        ServerUtils.kpiLog(log, kpiLog, "Get Report Template List");

        if ("XLS".equals(filterParametrs.getFolderName())) {
            if (filterParametrs.getCompanyID() != null) {
                ServerSecurityContext.getInstance().setCompanyId(filterParametrs.getCompanyID());
            }
            List<EdsReport> reportList = reportingManager.findAll();
            if (filterParametrs.getCompanyID() != null) {
                ServerSecurityContext.getInstance().removeCompanyId();
            }

            int totalCount = reportList.size();
            if (filterParametrs.getLimit() > 0) {
                reportList = ListUtils.getSublist(reportList, filterParametrs.getStart(), filterParametrs.getLimit());
            }
            ArrayList<ReportingListItem> resultList = new ArrayList<>();

            for (EdsReport report : reportList) {
                ReportingListItem listItem = new ReportingListItem();
                listItem.setReportId(report.getObjectID());
                listItem.setReportName(report.getName());
                listItem.setTemplateId(report.getPdftemplate() != null ? report.getPdftemplate().getObjectID() : 0);
                listItem.setTemplateName(report.getPdftemplate() != null ? report.getPdftemplate().getName() : "");
                listItem.setExceltemplateId(report.getExcelTemplateId());
                resultList.add(listItem);
            }
            return new ListResult<>(resultList, totalCount);
        } else {
            return reportTemplateManager.getReportingXMLTemplateList(filterParametrs);
        }
    }

    @Transactional
    public void sendToClient(Integer reportID, Integer userId, Integer companyId, String category, Integer recurrenceId) {
        String reportName = "";
        String templateCode = "";
        try {
            ServerSecurityContext.getInstance().setCompanyId(companyId);
            System.out.print("CompanyID=" + companyId + " userID=" + userId + " reportingID=" + reportID);
            EdsUser user = userManager.getUserByUserIdAndCompanyId(userId, companyId);
            EdsReport edsReport = reportingManager.get(reportID);
            if (edsReport != null) {
                reportName = edsReport.getName();
                templateCode = edsReport.getViewCode();
                System.out.print("reportingName=" + reportName + " in " + edsReport.getViewName());

            } else {
                System.out.print("reporting is null");
            }
            ReportRpc reportRpc = getReport(reportID, false, user);
            reportRpc.setUserID(userId);
            reportRpc.setCompanyId(companyId);
            reportRpc.setPosition(1);
            reportRpc.setLimit(64000);
            if (reportRpc.getMaxExcelRowCount() != null && reportRpc.getMaxExcelRowCount() > 0) {
                reportRpc.setLimit(reportRpc.getMaxExcelRowCount());
            }

            if (reportRpc.getBrowserTimeZone() == null || "".equals(reportRpc.getBrowserTimeZone())) {
                reportRpc.setBrowserTimeZone(user.getTimezone());
                System.out.print("browserTimeZone=" + reportRpc.getBrowserTimeZone());
            }
            ResultSet resultSet;
            if (reportRpc.getTableType().equals(ReportType.TABULAR.name()) || reportRpc.getGroupColumns() == null || reportRpc.getGroupColumns().isEmpty()) {
                resultSet = getTabularReportResult(reportRpc, userId);
            } else {
                resultSet = getSummaryReportResult(reportRpc, userId);
            }
            if (resultSet != null) {
                if (category != null && category.equals("TELEGRAM_RECURRENCE") && recurrenceService.getRecurrence(recurrenceId) != null) {
                    synchronized (this) {
                        MessageContentDetails messageContentDetails = new MessageContentDetails(reportRpc, edsReport, companyId, user, resultSet);
                        telegramReportingRecurrenceService.initialize(recurrenceId, messageContentDetails);
                    }
                } else {
                    RpcConvertToXmlLocal rpcToXml = new RpcConvertToXmlLocal(reportRpc);
                    String xmlText = rpcToXml.generate();
                    System.out.print("----------------------------excel-----------------------------------");
                    ExcelReportHandler excelReportHandler = (ExcelReportHandler) ApplicationContextProvider.applicationContext.getBean("excelReportHandler");
                    initDataForSending(edsReport, user, excelReportHandler.run(xmlText, companyId, user), false);
                }
                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setActionType(KpiLog.ActionType.SEND);
                kpiLog.setEntityId(reportRpc.getId());
                kpiLog.setEntityName(EdsReport.class.getSimpleName());
                kpiLog.setEntityType(reportRpc.getViewName() + "/" + reportRpc.getName());
                ServerUtils.kpiLog(log, kpiLog, "Send to client");
            }
        } catch (Exception e) {
            String logs = "sendToClient problem" +
                    "\n companyID=" + companyId +
                    "\n userID=" + userId +
                    "\n reportID=" + reportID +
                    "\n report_name=" + reportName +
                    "\n template_code=" + templateCode;
            log.error(logs, e);
        }
    }

    private String getHighChartType(ChartData chartData) {
        String highChartType = null;
        String type = chartData.getConf().getType().getTitle();

        if ("Vertical Bar".equals(type)) {
            highChartType = "column";
        } else if ("Horizontal Bar".equals(type)) {
            highChartType = "bar";
        } else if ("Line".equals(type)) {
            highChartType = "line";
        } else if ("Area".equals(type)) {
            highChartType = "area";
        } else if ("Pie".equals(type)) {
            highChartType = "pie";
        } else if ("Donut".equals(type)) {
            highChartType = "donutpie";
        } else if ("Funnel".equals(type)) {
            highChartType = "funnel";
        } else if ("Gauge".equals(type)) {
            highChartType = "gauge";
        } else if ("Semi Circle".equals(type)) {
            highChartType = "semipie";
        }

        return highChartType;
    }


    private byte[] processChart(ChartData chartData, EdsTelegramReportingScheduleRule reportingSettings) {
        String chartType = getHighChartType(chartData);
        if (chartType != null) {
            System.out.println("\n //////////////////////////////" + chartType + "///////////////////////////// \n");
            JSONObject jsonObject = new JSONObject();
            JSONObject infile = new JSONObject();

            JSONObject chart = new JSONObject();
            chart.put("type", chartType);
            infile.put("chart", chart);

            JSONObject credits = new JSONObject();
            credits.put("enabled", false);
            infile.put("credits", credits);

            JSONObject title = new JSONObject();
            title.put("text", chartData.getConf().getTitle());
            infile.put("title", title);

            JSONObject subtitle = new JSONObject();
            if (reportingSettings != null && reportingSettings.getLocale() != null) {
//                locale = new Locale(reportingSettings.getLocale());
            }
//            subtitle.put("text", wfmLocalizer.localize("total", "Total", locale).concat(" : ").concat(chartData.getTotal() != null ? numberWithCommas(chartData.getTotal().doubleValue(), reportingSettings) : ""));
            infile.put("subtitle", subtitle);

            JSONObject plotOptions = new JSONObject();
            JSONObject pSeries = new JSONObject();
            JSONObject dataLabels = new JSONObject();

            dataLabels.put("enabled", true);

            if (chartType.equals("line") || chartType.equals("area")) {
                JSONObject xAxis = new JSONObject();
                JSONObject xAxisTitle = new JSONObject();
                xAxisTitle.put("text", chartData.getConf().getxAxis().getColumnTitle());
                xAxis.put("title", xAxisTitle);
                infile.put("xAxis", xAxis);

                JSONArray series = new JSONArray();
                for (SerieData serieData : chartData.getSeries()) {
                    JSONObject data = new JSONObject();
                    data.put("name", serieData.getName());
                    JSONArray values = new JSONArray();
                    for (Number number : serieData.getValues()) {
                        values.add(number.longValue());
                    }
                    data.put("data", values);
                    series.add(data);
                }
                infile.put("series", series);
                jsonObject.put("infile", infile);
            } else if (chartType.equals("funnel")) {
                JSONArray centerArray = new JSONArray();
                centerArray.add("40%");
                centerArray.add("50%");
                pSeries.put("center", centerArray);
                pSeries.put("neckWidth", "30%");
                pSeries.put("neckHeight", "25%");
                pSeries.put("width", "80%");

                JSONArray series = new JSONArray();
                JSONObject data = new JSONObject();
                JSONArray dataArray = new JSONArray();
                for (int i = 0; i < chartData.getCategories().size(); i++) {
                    JSONArray arr = new JSONArray();
                    arr.add(chartData.getCategories().get(i));
                    arr.add(chartData.getSeries().get(0).getValues()[i]);
                    dataArray.add(arr);
                }
                data.put("data", dataArray);
                series.add(data);

                infile.put("series", series);
                jsonObject.put("infile", infile);
            } else if (chartType.equals("column") || chartType.equals("bar")) {
                JSONObject xAxis = new JSONObject();
                JSONArray categories = new JSONArray();
                if (chartData.getConf().getStacked() != null) {
                    pSeries.put("stacking", "normal");
                    pSeries.put("size", "110%");
                }
                if (chartData.getCategories().size() > 10) {
                    JSONObject legend = new JSONObject();
                    legend.put("enabled", true);
                    infile.put("legend", legend);
                }
                categories.addAll(chartData.getCategories());
                xAxis.put("categories", categories);
                infile.put("xAxis", xAxis);

                JSONArray series = new JSONArray();
                for (SerieData serieData : chartData.getSeries()) {
                    JSONObject serieInfo = new JSONObject();
                    serieInfo.put("name", serieData.getName());
                    JSONArray arr = new JSONArray();
                    for (Number number : serieData.getValues()) {
                        arr.add(number != null ? number.longValue() : BigDecimal.valueOf(0));
                    }
                    serieInfo.put("data", arr);
                    series.add(serieInfo);
                }

                infile.put("series", series);
                jsonObject.put("infile", infile);
            } else if (chartType.equals("pie")) {
                dataLabels.put("format", "<b>{point.name}</b><br>: {point.y} (<br>{point.percentage:.1f}%)");

                JSONArray series = new JSONArray();
                JSONObject pObj = new JSONObject();
                JSONArray data = new JSONArray();
                for (int i = 0; i < chartData.getCategories().size(); i++) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", chartData.getCategories().get(i));
                    obj.put("y", chartData.getSeries().get(0).getValues()[i].longValue());
                    data.add(obj);
                }
                pObj.put("data", data);
                series.add(pObj);
                infile.put("series", series);
                jsonObject.put("infile", infile);
            } else if (chartType.equals("donutpie")) {
                chartType = "pie";
                chart.put("type", chartType);
                infile.put("chart", chart);
                dataLabels.put("format", "<b>{point.name}</b><br>: {point.y} (<br>{point.percentage:.1f}%)");

                JSONArray series = new JSONArray();
                JSONObject pObj = new JSONObject();
                JSONArray data = new JSONArray();
                for (int i = 0; i < chartData.getCategories().size(); i++) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", chartData.getCategories().get(i));
                    obj.put("y", chartData.getSeries().get(0).getValues()[i].longValue());
                    data.add(obj);
                }
                pObj.put("data", data);
                pObj.put("size", "60%");
                pObj.put("innerSize", "70%");
                series.add(pObj);
                infile.put("series", series);
                jsonObject.put("infile", infile);
            } else if (chartType.equals("semipie")) {
                chartType = "pie";
                chart.put("type", chartType);
                infile.put("chart", chart);
                title.clear();
//                title.put("text", numberWithCommas(chartData.getTotal().longValue(), reportingSettings));
                subtitle.clear();
                subtitle.put("text", chartData.getConf().getTitle());
                infile.put("subtitle", subtitle);
                title.put("align", "center");
                title.put("verticalAlign", "middle");
                title.put("y", 70);
                infile.put("title", title);

                dataLabels.put("enabled", true);
                pSeries.put("dataLabels", dataLabels);
                pSeries.put("startAngle", -90);
                pSeries.put("endAngle", 90);
                pSeries.put("size", "110%");
                JSONArray arr = new JSONArray();
                arr.add("50%");
                arr.add("75%");
                pSeries.put("center", arr);

                JSONArray series = new JSONArray();
                JSONObject pObj = new JSONObject();
                JSONArray data = new JSONArray();
                pObj.put("type", "pie");
                pObj.put("innerSize", "70%");
                for (int i = 0; i < chartData.getCategories().size(); i++) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", chartData.getCategories().get(i));
                    obj.put("y", chartData.getSeries().get(0).getValues()[i].longValue());
                    obj.put("dataLabels", dataLabels);
                    data.add(obj);
                }
                pObj.put("data", data);
                series.add(pObj);
                infile.put("series", series);
                jsonObject.put("infile", infile);
            } else if (chartType.equals("gauge")) {
                subtitle.clear();
                subtitle.put("text", chartData.getConf().getTitle() != null ? chartData.getConf().getTitle() : "");
                chart.put("type", "solidgauge");
                infile.put("chart", chart);
                infile.put("title", null);
                JSONArray center = new JSONArray();
                center.add("50%");
                center.add("85%");
                JSONObject background = new JSONObject();
                background.put("innerRadius", "60%");
                background.put("outerRadius", "100%");
                background.put("shape", "arc");
                JSONObject pane = new JSONObject();
                pane.put("center", center);
                pane.put("size", "140%");
                pane.put("startAngle", -90);
                pane.put("endAngle", 90);
                pane.put("background", background);

                infile.put("pane", pane);

                JSONObject yAxis = new JSONObject();
                JSONObject titleN = new JSONObject();
                JSONObject labels = new JSONObject();
                titleN.put("y", -70);
                labels.put("y", 16);
                yAxis.put("minorTickInterval", null);
                yAxis.put("tickWidth", 0);
                yAxis.put("title", titleN);
                yAxis.put("labels", labels);
                yAxis.put("min", chartData.getGaugeMinValue() != null ? chartData.getGaugeMinValue() : 0);
                yAxis.put("max", chartData.getGaugeMaxValue() != null ? chartData.getGaugeMaxValue() : 0);
                infile.put("yAxis", yAxis);
                dataLabels.put("y", 0);
                dataLabels.put("borderWidth", 0);
                dataLabels.put("useHTML", true);
                JSONArray series = new JSONArray();
                JSONArray data = new JSONArray();
                data.add(chartData.getGaugeActValue().longValue());
                JSONObject pObject = new JSONObject();
                JSONObject p2Object = new JSONObject();
                pObject.put("data", data);
                pObject.put("name", "");
                pObject.put("type", "solidgauge");
                p2Object.put("data", data);
                p2Object.put("name", "");
                p2Object.put("type", "gauge");
                series.add(pObject);
                series.add(p2Object);
                infile.put("series", series);
                chartType = "solidgauge";
                pSeries.put("dataLabels", dataLabels);
                plotOptions.put(chartType, pSeries);
                infile.put("plotOptions", plotOptions);
                jsonObject.put("infile", infile);
            }

            pSeries.put("dataLabels", dataLabels);
            plotOptions.put(chartType, pSeries);
            infile.put("plotOptions", plotOptions);
            jsonObject.put("infile", infile);

            try {
                HttpPost post = new HttpPost("http://export.highcharts.com/");
                post.addHeader("content-type", "application/json");
                HttpClient client = HttpClientBuilder.create().build();
                post.setEntity(new StringEntity(jsonObject.toJSONString(), ContentType.APPLICATION_JSON));
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toByteArray(response.getEntity());
                } else {
                    log.error("STATUS CODE" + response.getStatusLine().getStatusCode());
                    log.error("STATUS PHRASE" + response.getStatusLine().getReasonPhrase());
                }
            } catch (Exception e) {
                log.error("SARDOR HIGHCHART API YEDIRIBSIZKU" + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    @Transactional
    public void initDataForSending(EdsReport report, EdsUser user, ByteArrayOutputStream baos, Boolean isCsv) {
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try {
            baos.flush();
            baos.close();
        } catch (Exception ex) {
            System.out.print("......................................");
        }
        System.out.print("*****************Upload to Amazon S3 server****************");
        EdsUpload upload = new EdsUpload();
        if (isCsv) {
            upload.setContentType("application/csv");
            upload.setOriginalName("Report.csv");
        } else {
            upload.setContentType("application/xls");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM_dd_yyyy_HH_mm");
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(user.getTimezone()));
            String filename;
            filename = "Report_" + clearSpaces(user.getCompany().getName()) + "_" + dateFormat.format(calendar.getTime()) + ".xls";
            filename = ServerUtils.normalizeFileNameT(filename);
            upload.setOriginalName(filename);
        }
        upload.setType(referenceManager.findReference(_UPLOAD_TYPE, EdsContextParams.getUploadType()));
        upload.setInputStream(bais);
        try {
            uploadManager.create(upload);
            System.out.print("****************File Uploaded******************");
        } catch (Exception ex) {
            log.error("****************Failed to Upload File******************", ex);
        }
        try {
            System.out.print("*****************************Sending Report to Target User***********************");
            messageManager.sendReport(report, user, upload.getObjectID());
        } catch (EdsTemplateException ex) {
            log.error("Error while sending email.", ex);
        } catch (Exception ex) {
            log.error("Error while sending email.", ex);
        }
        try {
            bais.close();
        } catch (IOException ex) {
            log.error("Unable to close stream", ex);
        }
    }

    @Transactional
    public Integer saveReport(ReportRpc report) {
        return saveReport(report, null, null);
    }

    @Transactional
    public Integer saveReport(ReportRpc report, Integer companyId, WorkflowTelegramAlert telegramAlert) {

        EdsReport reporting;

        if (companyId != null && companyId != 0) {
            ServerSecurityContext.getInstance().setCompanyId(companyId);
        }

        if (report.getId() != null) {
            reporting = reportingManager.get(report.getId());
        } else {
            reporting = new EdsReport();
            reporting.setCode(report.getCode());
        }
        reporting.setName(report.getName());
        boolean bool = reportingManager.hasReport(reporting.getCode(), report.getId());
        if (bool) {
            return 0;
        }

        boolean isRecurring = (report.getRecurrenceId() != null || (report.getRecurrenceJobItem() != null && report.getRecurrenceJobItem().isEnabled()));
        if (report.getXmlTemplateId() != null) {
            reporting.setXmlTemplateId(report.getXmlTemplateId());
        }
        if (report.getViewCode() != null) {
            reporting.setViewCode(report.getViewCode());
        }
        if (report.getIsDetailed() != null) {
            reporting.setDetailed(report.getIsDetailed());
        }
        if (report.isShowRowCount() != null) {
            reporting.setShowRowCount(report.isShowRowCount());
        }
        if (report.getFilterPattern() != null) {
            reporting.setFilterPattern(report.getFilterPattern());
        }
        if (report.isTransposed()) {
            reporting.setTransposed(report.isTransposed());
        }

        reporting.setDescription(report.getDiscreption());
        if (report.getFolderId() != null) {
            reporting.setFolder(foldersManager.get(report.getFolderId()));
        }
        if (report.getEmailTemplateItem() != null && report.getEmailTemplateItem().getId() != null) {
            EdsEmailTemplate emailTemplate = emailTemplateManager.get(report.getEmailTemplateItem().getId());
            reporting.setEmailTemplate(emailTemplate);
        }
        StringBuilder ids = new StringBuilder();
        boolean isFirst = true;
        if (report.getTargetUsers() != null) {
            for (Integer userID : report.getTargetUsers()) {
                if (isFirst) {
                    ids.append(userID);
                    isFirst = false;
                } else {
                    ids.append(",").append(userID);
                }
            }
            List<EdsEmployee> employees = employeeManager.getEmployeesByIds("" + ids);
            reporting.getTargetUsers().clear();
            reporting.getTargetUsers().addAll(employees);
        }
        reporting.setViewName(report.getViewName());
        reporting.setTableType(report.getTableType());

        String columns = "";
        StringBuilder formats = new StringBuilder();

        StringBuilder drillDownReport = new StringBuilder();
        Integer index = 0;
        for (ColumnRpc column : report.getSelectedColumns()) {
            if (!"".equals(columns)) {
                columns = columns + "#";
                formats.append("#");
                if (column.getDrillDownReport()) {
                    drillDownReport.append("#");
                }
            }
            columns = columns + column.getName();
            formats.append(column.getType());
            if (column.getDrillDownReport() != null && column.getDrillDownReport()) {
                drillDownReport.append(index).append("<->").append(column.getLinkedReportId().toString()).append("<->").append(column.getFilterParametr().toString());
            }
            index++;
        }

        if (!"".equals(drillDownReport.toString())) {
            reporting.setDrilDownReport(drillDownReport.toString());
        }

        //Custom filter parameters saving
        if (report.getCustomFilter() != null && !report.getCustomFilter().isEmpty()) {
            StringBuilder variables = new StringBuilder();
            StringBuilder values = new StringBuilder();
            for (Map.Entry<String, String> entrySet : report.getCustomFilter().entrySet()) {
                variables.append(entrySet.getKey()).append("|");
                values.append(entrySet.getValue()).append("|");
            }
            reporting.setCustomValue(values.toString());
            reporting.setCustomVariable(variables.toString());
        }

        reporting.setSelectColumns(columns);
        reporting.setColumnFormats(formats.toString());

        String sum = "";
        String avg = "";
        String largest = "";
        String smallest = "";
        String count = "";
        for (ColumnRpc column : report.getSumaries()) {
            if (column.isSum()) {
                if (!"".equals(sum)) {
                    sum = sum + "#";
                }
                sum = sum + column.getName();
            }
            if (column.isAvg()) {
                if (!"".equals(avg)) {
                    avg = avg + "#";
                }
                avg = avg + column.getName();
            }
            if (column.isLargest()) {
                if (!"".equals(largest)) {
                    largest = largest + "#";
                }
                largest = largest + column.getName();
            }
            if (column.isSmallest()) {
                if (!"".equals(smallest)) {
                    smallest = smallest + "#";
                }
                smallest = smallest + column.getName();
            }
            if (column.isCount()) {
                if (!"".equals(count)) {
                    count = count + "#";
                }
                count = count + column.getName();
            }
        }

        reporting.setSumValues(sum);
        reporting.setAvgValues(avg);
        reporting.setLargestValues(largest);
        reporting.setSmallestValues(smallest);
        reporting.setCountValues(count);

        if (!StrUtils.isEmpty(report.getSortTableByColumn())) {
            reporting.setOrderbycolumn(report.getSortTableByColumn());
        }

        if (!StrUtils.isEmpty(report.getSortTableByColumnType())) {
            reporting.setOrderbycolumntype(report.getSortTableByColumnType());
        }

        if (ReportType.SUMMARY.name().equals(report.getTableType())) {
            String groupString = "";
            StringBuilder sortableString = new StringBuilder();
            StringBuilder rangeString = new StringBuilder();
            for (int i = 0; i < report.getGroupColumns().size(); i++) {
                if (!"".equals(groupString)) {
                    groupString = groupString + "#";
                    sortableString.append("#");
                    rangeString.append("#");
                }
                groupString = groupString + report.getGroupColumns().get(i).getName();
                sortableString.append(report.getSortTypes().get(i));
                if (SqlColumnType.DATE.getName().equals(report.getGroupColumns().get(i).getType())) {
                    rangeString.append(report.getRangeType().get(i));
                } else {
                    rangeString.append(" ");
                }
            }
            reporting.setGroupColumns(groupString);
            reporting.setSortOrders(sortableString.toString());
            reporting.setGroupRange(rangeString.toString());

            StringBuilder viewTypes = new StringBuilder();
            for (int i = 0; i < report.getViewTypes().size(); i++) {
                if (!"".equals(viewTypes.toString())) {
                    viewTypes.append("#");
                }
                viewTypes.append(report.getViewTypes().get(i));
            }
            reporting.setViewTypes(viewTypes.toString());
        }

        String sett = "";
        StringBuilder fieldd = new StringBuilder();
        StringBuilder operator = new StringBuilder();
        StringBuilder value = new StringBuilder();
        StringBuilder comparators = new StringBuilder();
        StringBuilder promtByInput = new StringBuilder();
        for (int i = 0; i < report.getValues().size(); i++) {
            if (!"".equals(sett)) {
                sett = sett + "#";
                fieldd.append("#");
                operator.append("#");
                value.append("#");
                comparators.append("#");
                promtByInput.append("#");
            }
            if (report.getFieldd().get(i).isListFilter()) {
                continue;
            }
            sett = sett + report.getSett().get(i);
            fieldd.append(report.getFieldd().get(i).getName());
            operator.append(report.getOperators().get(i));
            value.append(report.getValues().get(i));
            if (report.getPromtList() != null && report.getPromtList().size() > i) {
                promtByInput.append(report.getPromtList().get(i).toString());
            }
            if (i < report.getBoolType().size() - 1) {
                comparators.append(report.getBoolTypeAt(i));
            }
        }

        reporting.setBinds(sett);
        reporting.setArrColumn(fieldd.toString());
        reporting.setArrOperators(operator.toString());
        reporting.setArrValues(value.toString());
        reporting.setComparators(comparators.toString());
        reporting.setArrPromtByInputs(promtByInput.toString());
        if (report.getSntFilterName() != null) {
            reporting.setFilterName(report.getSntFilterName());
            reporting.setDuration(report.getDurationType());
            reporting.setStartDate(report.getStartDate());
            reporting.setEndDate(report.getEndDate());
        }
        if (report.getStartDate() != null && report.getEndDate() != null) {
            reporting.setEndDate(report.getEndDate());
            reporting.setStartDate(report.getStartDate());
        }

        if (report.getLimit() != -1 && !ReportType.SUMMARY.name().equals(report.getTableType())) {
            reporting.setQueryLimit(report.getLimit());
        }

        if (report.getChartConf() != null) {
            ChartConfItem confItem = report.getChartConf();
            EdsChartConfig chartConfig;

            if (reporting.getChartConfig() != null) {
                chartConfig = reporting.getChartConfig();
            } else {
                chartConfig = new EdsChartConfig();
            }

            if (report.isSaveAs() && confItem.getLocalization() != null) {
                EdsCustomFormLocalization localization = customFormLocalizationManager.get(confItem.getLocalization().getId());
                EdsCustomFormLocalization edsCustomFormLocalization = localization.cloneShallow();
                edsCustomFormLocalization.setChildren(new ArrayList<>());
                customFormLocalizationManager.create(edsCustomFormLocalization);
                chartConfig.setCustomFormLocalization(edsCustomFormLocalization);
            } else if (confItem.getLocalization() != null) {
                EdsCustomFormLocalization localization = customFormLocalizationManager.get(confItem.getLocalization().getId());
                chartConfig.setCustomFormLocalization(localization);
            }

            chartConfig.setChartViewOption(confItem.getChartViewOption());
            chartConfig.setChartViewOptionType(confItem.getChartViewOptionType());
            chartConfig.setType(confItem.getType());
            chartConfig.setTotalFieldName(confItem.getTotalFieldName());
            chartConfig.setAgrigateItemCode(confItem.getAgrigateItemCode());
            chartConfig.setBenchmarkAggFuncVal(confItem.getBenchmarkAggFuncVal());
            chartConfig.setGradientColor(confItem.getGradientColor());
            chartConfig.setDrillxAxis(confItem.getDrillxAxis());
            chartConfig.setShowPieChart(confItem.isShowPieChart());
            chartConfig.setPieChartPosition(confItem.getPieChartPosition());
            //If a chart config doesn't fill out then you have to set {NONE} type to chart type
            if (ChartTypeEnum.GAUGE_CHART.equals(confItem.getType())) {

                if (confItem.getGaugeConfig() == null)
                    chartConfig.setType(ChartTypeEnum.NONE);
            } else if ((confItem.getxAxis() == null || confItem.getSeries() == null)) {
                chartConfig.setType(ChartTypeEnum.NONE);
            }

            if (!ChartTypeEnum.NONE.equals(chartConfig.getType())) {
                chartConfig.setTitle(confItem.getTitle());
                chartConfig.setScale(confItem.getScale());


                if (ChartTypeEnum.GAUGE_CHART.equals(confItem.getType())) {
                    chartConfig.setGaugeConfig(confItem.getGaugeConfig());
                } else {
                    chartConfig.setPageSize(confItem.getPageSize());
                    chartConfig.setSortBy(confItem.getSortBy());
                    chartConfig.setSortType(confItem.getSortType());
                    chartConfig.setDateSortPeriodType(confItem.getDateSortPeriodType());
                    chartConfig.setSplitBy(confItem.getSplitBy());
                    chartConfig.setCustomSorderColumn(confItem.getCustomSortColumn());
                    chartConfig.setBenchmarkValue(confItem.getBenchmarkValue());
                    chartConfig.setxAxis(confItem.getxAxis());
                    chartConfig.setSerieConfs(confItem.getSeries());
                    chartConfig.setShowLabel(confItem.isShowLabel());
                    chartConfig.setShowSerie(confItem.isShowSerie());
                    chartConfig.setShowStacked(confItem.isShowStacked());
                    chartConfig.setStacked(confItem.getStacked());
                    chartConfig.setLegend(confItem.getLegend());
                }

                chartConfig.setModules(confItem.getModules());
            } else if (chartConfig.getObjectID() != null) {

                //clear previous chart configs
                chartConfig.setTitle(null);
                chartConfig.setPageSize(null);
                chartConfig.setSortBy(null);
                chartConfig.setSortType(null);
                chartConfig.setDateSortPeriodType(null);
                chartConfig.setSplitBy(null);
                chartConfig.setCustomSorderColumn(null);
                chartConfig.setBenchmarkValue(null);
                chartConfig.setxAxis(null);
                chartConfig.setSerieConfs(null);
                chartConfig.setModules(null);
                chartConfig.setStacked(null);
                chartConfig.setLegend(null);
                chartConfig.setGaugeConfig(null);
            }


            chartConfigManager.createOrUpdate(chartConfig);
            reporting.setChartConfig(chartConfig);

        } else if (reporting.getChartConfig() != null) {

            //delete chart config from saved report
            reporting.setChartConfig(null);
            chartConfigManager.delete(reporting.getChartConfig());
        }

        {
            KpiWidgetItem kpiWidgetItem = report.getKpiWidgetItem();
            saveKpiWidget(reporting, kpiWidgetItem, report.isSaveAs());

        }

        if (report.getShowActions() != null) {
            reporting.setShowActions(report.getShowActions());
        }

        if (report.enableAddNewAction() != null) {
            reporting.setEnableAddNewAction(report.enableAddNewAction());
        }
        if (report.enableViewAction() != null) {
            reporting.setEnableViewAction(report.enableViewAction());
        }
        if (report.enableEditAction() != null) {
            reporting.setEnableEditAction(report.enableEditAction());
        }
        if (report.enableDeleteAction() != null) {
            reporting.setEnableDeleteAction(report.enableDeleteAction());
        }
        if (report.getShowActionsIcon() != null) {
            reporting.setShowActionsIcon(report.getShowActionsIcon());
        }


        if (report.getShowDrillReports() != null) {
            reporting.setShowDrillReport(report.getShowDrillReports());
        }
        ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
        if (viewRpc.getConditionCode() != null) {
            reporting.setConditionCode(viewRpc.getConditionCode());
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsReport.class.getSimpleName());
        kpiLog.setEntityType(report.getViewName() + "/" + report.getName());

        if (report.getId() != null) {
            if (reporting.getAuditInfo().getCreationDate() == null) {
                reporting.getAuditInfo().setModificationDate(new Date());
            }
            reporting.getAuditInfo().setModificationDate(new Date());
            reporting.getAuditInfo().setModifiedBy(reportingManager.getUser());
            reportingManager.update(reporting);
            report.setCode(reporting.getCode());
            report.setId(reporting.getObjectID());
            String reportKey = ServerSecurityContext.getInstance().getCompanyId() + "_" + reporting.getObjectID() + "_" + report.getCode();
            String key = RedisClient.getKey(reportKey);
            Integer cachingTimeByReportCode = companySystemSettingsManager.getReportingCacheTime() != null ? companySystemSettingsManager.getReportingCacheTime() : 1800;
            if (key != null) {
                RedisClient.setKey(reportKey, key, cachingTimeByReportCode);
            }
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(reporting.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Updated Report");
        } else {
            reporting.getAuditInfo().setCreationDate(new Date());
            reporting.getAuditInfo().setModificationDate(new Date());
            reporting.getAuditInfo().setCreatedBy(reportingManager.getUser());
            reportingManager.create(reporting);
            report.setCode(reporting.getCode());
            report.setId(reporting.getObjectID());

            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(reporting.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Added New Report");
        }

        //create dashboard component from chart config
        if (reporting.getChartConfig() != null) {
            EdsChartConfig chartConfig = reporting.getChartConfig();
            EdsDefaultComponents dcomponent = defaultComponentsManager.getByReportId(reporting.getObjectID(), null);

            if (ChartTypeEnum.NONE.equals(chartConfig.getType())) {
                defaultComponentsManager.deleteComponentByReportId(reporting.getObjectID(), null);
            } else if (chartConfig.getModules() != null) {

                if (dcomponent == null) {
                    dcomponent = new EdsDefaultComponents();
                }
                dcomponent.setReport(reporting);
                dcomponent.setComponentCode(reporting.getObjectID() + "_" + reporting.getCode().replace(" ", "_"));
                dcomponent.setComponentName(chartConfig.getTitle());

                ArrayList<String> modules = new ArrayList<>();
                chartConfig.getModules().forEach(m -> modules.add(m.name()));
                dcomponent.setModules(modules);

                defaultComponentsManager.createOrUpdate(dcomponent);
            } else if (dcomponent != null) {
                dcomponent.setModules(new ArrayList<>());
            }

        }
        if (reporting.getKpiWidget() != null) {
            EdsKpiWidget kpiWidget = reporting.getKpiWidget();
            EdsDefaultComponents dcomponent = defaultComponentsManager.getByReportId(reporting.getObjectID(), kpiWidget.getObjectID());

            if (kpiWidget.getModules() != null) {

                if (dcomponent == null) {
                    dcomponent = new EdsDefaultComponents();
                }
                dcomponent.setReport(reporting);
                dcomponent.setKpiWidget(kpiWidget);
                dcomponent.setComponentCode(reporting.getObjectID() + "_" + kpiWidget.getObjectID() + "_" + reporting.getCode().replace(" ", "_"));
                dcomponent.setComponentName(kpiWidget.getTitle());

                ArrayList<String> modules = new ArrayList<>();
                kpiWidget.getModules().forEach(m -> modules.add(m.name()));
                dcomponent.setModules(modules);

                defaultComponentsManager.createOrUpdate(dcomponent);
            } else if (dcomponent != null) {
                dcomponent.setModules(new ArrayList<>());
            }
        }

        if (isRecurring) {
            RecurrenceJobItem jobItem = report.getRecurrenceJobItem();
            if (jobItem != null) {
                jobItem.setBusObjectId(reporting.getObjectID());
                jobItem.setJobType(SchedulerConstant.RECURRING_REPORT);
                recurrenceService.saveRecurrenceJob(jobItem);
            }
        }

        if (companyId != null && companyId != 0) {
            ServerSecurityContext.getInstance().removeCompanyId();
        }
        if (!"Private".equals(reporting.getFolder().getType())) {
            saveReportPermission(reporting);
        }
        WebSocketServerObject message = new WebSocketServerObject();
        message.setEventType(WfmUiEventType.REPORTING_REPOT_SAVED);
        if (reporting.getFolder().getCategoryCode() != null) {
            EdsReportTemplateCategory categoy = reportTemplateCategoryManager.getReportTemplateCategoryByCode(reporting.getFolder().getCategoryCode());
            if (categoy != null) {
                message.setData(String.valueOf(categoy.getObjectID()));
            }
        }
        try {
            Integer userId = ((EdsUser) SecurityContext.getInstance().getUser()).getObjectID();
            message.setUserId(userId);
            rabbitMQService.sendWebPushNotification(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reporting.getObjectID();
    }

    private void saveKpiWidget(EdsReport reporting, KpiWidgetItem kpiWidgetItem, boolean isSaveAs) {

        EdsKpiWidget kpiWidget = reporting.getKpiWidget();
        boolean isRankingWidget = !ChartTypeEnum.RANKING_KPI.equals(kpiWidgetItem.getType()) && (kpiWidgetItem.getKpiWidgetMetric() == null || kpiWidgetItem.getKpiWidgetMetric().getSerieColumn().getColumn() == null);
        if (kpiWidgetItem == null ||
                ((kpiWidgetItem.getKpiWidgetTitle() == null || kpiWidgetItem.getKpiWidgetTitle().isEmpty()) && isRankingWidget)) {
            if (kpiWidget != null) {
                defaultComponentsManager.deleteComponentByReportId(reporting.getObjectID(), kpiWidget.getObjectID());
                if (kpiWidget.getWidgetFilterList() != null && kpiWidget.getWidgetFilterList().size() > 0) {
                    for (EdsKpiWidgetFilter widgetFilter : kpiWidget.getWidgetFilterList()) {
                        kpiWidgetFilterManager.delete(widgetFilter);
                    }
                }
                kpiWidgetManager.delete(kpiWidget);
                reporting.setKpiWidget(null);
            }
            return;
        } else if (kpiWidget == null) {
            kpiWidget = new EdsKpiWidget();
        }
        if (kpiWidgetItem.getLocalization() != null) {
            EdsCustomFormLocalization localization = customFormLocalizationManager.load(kpiWidgetItem.getLocalization().getId());
            if (isSaveAs) {
                EdsCustomFormLocalization newLocalization = localization.cloneShallow();
                newLocalization.setChildren(Collections.emptyList());
                customFormLocalizationManager.create(newLocalization);
                kpiWidget.setCustomFormLocalization(newLocalization);

                if (kpiWidgetItem.getOldLocalization() != null){
                    CustomFormLocalization oldLocalization = kpiWidgetItem.getOldLocalization();
                    localization.setEnglishName(oldLocalization.getEnglishName());
                    localization.setRussianName(oldLocalization.getRussianName());
                    localization.setArabicName(oldLocalization.getArabicName());
                    localization.setUzbekName(oldLocalization.getUzbekName());
                }
            } else {
                kpiWidget.setCustomFormLocalization(localization);
            }
        }

        kpiWidget.setTitle(kpiWidgetItem.getKpiWidgetTitle());
        kpiWidget.setScale(kpiWidgetItem.getKpiWidgetScale());
        kpiWidget.setSuffix(kpiWidgetItem.getKpiWidgetSuffix());
        kpiWidget.setSuffixLocalization(l(kpiWidgetItem.getSuffixLocalization()));
        kpiWidget.setTitleColor(kpiWidgetItem.getKpiWidgetTitleColor());
        kpiWidget.setGroupingColumn(kpiWidgetItem.getGroupingColumn());
        kpiWidget.setSortBy(kpiWidgetItem.getSortBy());
        kpiWidget.setSortType(kpiWidgetItem.getSortType());
        kpiWidget.setDateSortPeriodType(kpiWidgetItem.getDateSortPeriodType());
        kpiWidget.setPageSizeType(kpiWidgetItem.getPageSizeType());
        kpiWidget.setPageSize(kpiWidgetItem.getPageSize());
        kpiWidget.setCustomPageSize(kpiWidgetItem.getCustomPageSize());
        kpiWidget.setOtherItems(kpiWidgetItem.isOtherItems());
        kpiWidget.setNegAndPosType(kpiWidgetItem.getNegAndPosType());
        kpiWidget.setDifferentTitle(kpiWidgetItem.getDifferentTitle());
        kpiWidget.setDifferenceLocalization(l(kpiWidgetItem.getDifferenceLocalization()));
        kpiWidget.setShowDifferent(kpiWidgetItem.isShowDifferent());
        kpiWidget.setComparisionText(kpiWidgetItem.getComparisionText());
        kpiWidget.setComparisonLocalization(l(kpiWidgetItem.getComparisonLocalization()));
        kpiWidget.setIncreaseColor(kpiWidgetItem.getIncreaseColor());
        LinkedList<SerieConfItem> serieConfs = new LinkedList<>();
        serieConfs.add(kpiWidgetItem.getKpiWidgetMetric());
        kpiWidget.setSerieConfs(serieConfs);
        kpiWidget.setModules(kpiWidgetItem.getModules());
        kpiWidget.setType(kpiWidgetItem.getType());

        if (kpiWidget.getWidgetFilterList() != null && kpiWidget.getWidgetFilterList().size() > 0) {
            kpiWidget.getWidgetFilterList().clear();
        }
        kpiWidgetManager.createOrUpdate(kpiWidget);
        reporting.setKpiWidget(kpiWidget);

        KpiWidgetFilterItem kpiWidgetFilterItemOne = kpiWidgetItem.getKpiWidgetFilterItemOne();
        KpiWidgetFilterItem kpiWidgetFilterItemTwo = kpiWidgetItem.getKpiWidgetFilterItemTwo();

        saveKpiWidgetFilter(kpiWidget, kpiWidgetFilterItemOne, 1);
        saveKpiWidgetFilter(kpiWidget, kpiWidgetFilterItemTwo, 2);
    }

    private EdsCustomFormLocalization l (CustomFormLocalization localizationCF) {
        return localizationCF != null ? customFormLocalizationManager.load(localizationCF.getId()) : null;
    }

    private void saveKpiWidgetFilter(EdsKpiWidget kpiWidget, KpiWidgetFilterItem kpiWidgetFilterItem, int filterType) {

        EdsKpiWidgetFilter edsKpiWidgetFilter = kpiWidgetFilterManager.getKpiWidetFilterByType(kpiWidget.getObjectID(), filterType);

        if (filterType == 2 && ChartTypeEnum.BASIC_KPI.equals(kpiWidget.getType())) {
            if (edsKpiWidgetFilter != null) {
                kpiWidgetFilterManager.delete(edsKpiWidgetFilter);
            }
            return;
        } else if (edsKpiWidgetFilter == null) {
            edsKpiWidgetFilter = new EdsKpiWidgetFilter();
        }
        String sett = "";
        StringBuilder fieldd = new StringBuilder();
        StringBuilder operator = new StringBuilder();
        StringBuilder value = new StringBuilder();
        StringBuilder comparators = new StringBuilder();
        for (int i = 0; i < kpiWidgetFilterItem.getValues().size(); i++) {
            if (!"".equals(sett)) {
                sett = sett + "#";
                fieldd.append("#");
                operator.append("#");
                value.append("#");
                comparators.append("#");
            }
            if (kpiWidgetFilterItem.getFieldd().get(i).isListFilter()) {
                continue;
            }
            sett = sett + kpiWidgetFilterItem.getSett().get(i);
            fieldd.append(kpiWidgetFilterItem.getFieldd().get(i).getName());
            operator.append(kpiWidgetFilterItem.getOperators().get(i));
            value.append(kpiWidgetFilterItem.getValues().get(i));
            if (i < kpiWidgetFilterItem.getBoolType().size() - 1) {
                comparators.append(kpiWidgetFilterItem.getBoolTypeAt(i));
            }
            if (kpiWidgetFilterItem.getFieldd().get(i).isSamePeriodLastYear()) {
                edsKpiWidgetFilter.setLastYearIndex(i);
            } else {
                edsKpiWidgetFilter.setLastYearIndex(null);
            }
        }

        edsKpiWidgetFilter.setBinds(sett);
        edsKpiWidgetFilter.setArrColumn(fieldd.toString());
        edsKpiWidgetFilter.setArrOperators(operator.toString());
        edsKpiWidgetFilter.setArrValues(value.toString());
        edsKpiWidgetFilter.setComparators(comparators.toString());
        if (kpiWidgetFilterItem.getFilterPattern() != null) {
            edsKpiWidgetFilter.setFilterPattern(kpiWidgetFilterItem.getFilterPattern());
        }
        edsKpiWidgetFilter.setFilterType(filterType);
        edsKpiWidgetFilter.setKpiWidgetId(kpiWidget);
        kpiWidgetFilterManager.create(edsKpiWidgetFilter);
    }

    @Transactional
    public boolean deleteReport(Integer id) {

        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        EdsReport reporting = reportingManager.getByCompany(id, companyID);
        String templateName = reporting.getViewName();
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_REPORT, reporting.getObjectID(), companyID);
        if (recurrence != null) {
            recurrenceService.updateRecurrence(recurrence, true, true);
        }
        if (reporting.getAuditInfo().getCreationDate() == null) {
            reporting.getAuditInfo().setCreationDate(new Date());
        }

        reportingManager.deleteWithRelation(reporting.getPermissionCode(), reporting.getCode(), id, companyID);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(id);
        kpiLog.setEntityName(EdsReport.class.getSimpleName());
        kpiLog.setEntityType(templateName + "/" + reporting.getName());
        ServerUtils.kpiLog(log, kpiLog, "Delete Report");

        return true;
    }

    @Override
    public boolean updateReportTemplate(ReportingListItem report) {
        if (report.getCompanyId() != null) {
            ServerSecurityContext.getInstance().setCompanyId(report.getCompanyId());
        }
        try {

            EdsReport reporting = reportingManager.get(report.getReportId());
            if (report.getTemplateId() != null) {
                EdsCompanyPdfTemplate pdfTemplate = companyPdfTemplateManager.get(report.getTemplateId());
                reporting.setPdftemplate(pdfTemplate);
            }
            if (report.getExceltemplateId() != null) {
                reporting.setExcelTemplateId(report.getExceltemplateId());
            }
            if (report.getMaxRowCount() != null) {
                reporting.setMaxExcelRowCount(report.getMaxRowCount());
            } else {
                reporting.setMaxExcelRowCount(Constants.REPORTING_DEFAULT_EXCELMAXROWCOUNT);
            }
            if (reporting.getAuditInfo().getCreationDate() == null) {
                reporting.getAuditInfo().setModificationDate(new Date());
            }
            reporting.getAuditInfo().setModificationDate(new Date());
            reportingManager.update(reporting);
            if (report.getCompanyId() == null) {
                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                kpiLog.setEntityId(reporting.getObjectID());
                kpiLog.setEntityName(EdsReport.class.getSimpleName());
                kpiLog.setEntityType(reporting.getViewName() + "/" + reporting.getName());
                ServerUtils.kpiLog(log, kpiLog, "Updated Report Excel/Pdf Template");
            }

            return true;
        } catch (Exception e) {
            log.error("updateReportTemplate problem", e);
            return false;
        }
    }

    @Transactional
    public ReportGenerateTableRpc getReportResult(ReportRpc report) {
        ReportGenerateTableRpc reportGenerateTable = new ReportGenerateTableRpc();
        Integer companyId = report.getCompanyId();
        if (companyId == null && ServerSecurityContext.getInstance() != null && ServerSecurityContext.getInstance().getCompanyId() != null && !ServerSecurityContext.getInstance().getCompanyId().isEmpty()) {
            companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
            report.setCompanyId(companyId);
        }
        Integer userId;
        UserSecuritryRpc userSecuritryRpc = getUser();
        if (userSecuritryRpc != null) {
            userId = userSecuritryRpc.getUserId();
        } else {
            userId = 0;
        }
        if (report != null && report.getViewCode() != null) {
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
            report.setFromKpi(viewRpc.isFromKpi());
        }
        report.setUserID(userId);
        if (report.getBrowserTimeZone() == null || report.getBrowserTimeZone().isEmpty()) {
            report.setBrowserTimeZone(getUser().getTimezone());
        }

        if (report.getId() != null && report.getViewName() == null) {
            String browserTimeZone = report.getBrowserTimeZone();
            report = getReport(report.getId(), report.getXmlTemplateId());
            if (browserTimeZone != null && !browserTimeZone.isEmpty()) {
                report.setBrowserTimeZone(browserTimeZone);
            }
            report.setFolderType(getFolderTypeByReportId(report.getId()));
        }
        if ((report.getBrowserTimeZone() == null || report.getBrowserTimeZone().isEmpty()) && userId != null) {
            EdsUser user = userManager.get(userId);
            if (user != null && user.getTimezone() != null && !"".equals(user.getTimezone())) {
                report.setBrowserTimeZone(user.getTimezone());
            }
        }

        if (report.getId() != null || report.getXmlTemplateId() != null) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            if (report.getId() == null) {
                kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
                kpiLog.setEntityId(report.getXmlTemplateId());
                kpiLog.setEntityType(report.getViewName());
                ServerUtils.kpiLog(log, kpiLog, "Run Report Template");
            } else {
                kpiLog.setEntityName(EdsReport.class.getSimpleName());
                kpiLog.setEntityId(report.getId());
                kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
                ServerUtils.kpiLog(log, kpiLog, "Run Saved Report");
            }
        }
        return reportGenerateTable;
    }

    @Override
    public ChartData getReportChartData(ReportRpc report, boolean isFromRefresh) {
        boolean isdrillDownChart = report.getChartConf().getDrillxAxis() != null;

        if (report.getChartConf() == null || ChartTypeEnum.NONE.equals(report.getChartConf().getType())) {
            return null;
        }

        boolean fromSqlServer;
        Integer companyId = report.getCompanyId();
        String key = null;
        ChartData data = null;
        if (report.getId() != null && report.getCode() != null) {
            key = ServerSecurityContext.getInstance().getCompanyId() + "_" + report.getId().toString() + "_" + report.getCode();
            data = RedisClient.getKeyForR(key, ChartData.class);
        }
        if (data == null || isFromRefresh || report.isFromRunButton()) {

            if (companyId == null && ServerSecurityContext.getInstance() != null && ServerSecurityContext.getInstance().getCompanyId() != null && !ServerSecurityContext.getInstance().getCompanyId().isEmpty()) {
                companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
                report.setCompanyId(companyId);
            }
            Integer userId;
            UserSecuritryRpc userSecuritryRpc = getUser();

            if (userSecuritryRpc != null) {
                userId = userSecuritryRpc.getUserId();
            } else {
                userId = 0;
            }
            if (report != null && report.getViewCode() != null) {
                ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
                report.setFromKpi(viewRpc.isFromKpi());
            }
            report.setUserID(userId);

            if (report.getBrowserTimeZone() == null || report.getBrowserTimeZone().isEmpty()) {
                report.setBrowserTimeZone(getUser().getTimezone());
            }

            if (report.getId() != null && report.getViewName() == null) {
                String browserTimeZone = report.getBrowserTimeZone();
                report = getReport(report.getId(), report.getXmlTemplateId());

                if (browserTimeZone != null && !"".equals(browserTimeZone)) {
                    report.setBrowserTimeZone(browserTimeZone);
                }
                report.setFolderType(getFolderTypeByReportId(report.getId()));
            }
            if ((report.getBrowserTimeZone() == null || report.getBrowserTimeZone().isEmpty()) && userId != null) {
                EdsUser user = userManager.get(userId);
                if (user != null && user.getTimezone() != null && !"".equals(user.getTimezone())) {
                    report.setBrowserTimeZone(user.getTimezone());
                }
            }

            Connection conn = null;
            PreparedStatement preparedStatement;
            ResultSet resultSetChart;
            ResultSet resultSetDrillChart = null;
            ChartData chartData = null;
            String sqlQuery = "";
            PreparedStatement drillpreparedStatement = null;
            String drillDownsqlQuery = "";


            try {
                conn = getDataSourceConnection(report);
                ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
                report.setNoTimeZone(viewRpc.isNoTimezone());
                fromSqlServer = customDbUrl != null && customDbUrl.contains(SQLSERVER);

                if (report.getChartConf() != null) {
                    fixErrors(report);
                    report.setSqlServer(fromSqlServer);

                    if (ChartTypeEnum.GAUGE_CHART.equals(report.getChartConf().getType())) {
                        sqlQuery = SqlQueryUtil.getChartQueryForGauge(companyId, report);
                    } else {
                        sqlQuery = SqlQueryUtil.getChartQuery(companyId, report);
                    }
                    if (isdrillDownChart) {
                        drillDownsqlQuery = SqlQueryUtil.getDrillChartQuery(companyId, report, sqlQuery);
                    }

                    preparedStatement = conn.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    if (isdrillDownChart) {
                        drillpreparedStatement = conn.prepareStatement(drillDownsqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    }

                    if (fromSqlServer) {
                        sqlQuery = getNormalizedQueryForSqlServer(sqlQuery);
                        if (isdrillDownChart) {
                            drillDownsqlQuery = getNormalizedQueryForSqlServer(drillDownsqlQuery);
                        }
                    } else {
                        preparedStatement.setPoolable(true);
                        if (isdrillDownChart) {
                            drillpreparedStatement.setPoolable(true);
                        }
                    }

                    if (report.getValues().size() > 0) {
                        int reSetCount = viewRpc.getQueries().split("\\{where2}").length;
                        setParametersToStatement(reSetCount, 1, report, preparedStatement);
                    }

                    resultSetChart = preparedStatement.executeQuery();
                    if (isdrillDownChart) {
                        resultSetDrillChart = drillpreparedStatement.executeQuery();
                    }

                    if (ChartTypeEnum.GAUGE_CHART.equals(report.getChartConf().getType())) {
                        chartData = JdbcUtil.getChartDataForGauge(resultSetChart, report);
                    } else {
                        if (report.getChartConf().getSplitBy() != null) {
                            chartData = JdbcUtil.getSplitedChartData(resultSetChart, report);
                        } else {
                            chartData = JdbcUtil.getChartData(resultSetChart, report, resultSetDrillChart);
                        }
                    }
                    resultSetChart.close();
                    if (isdrillDownChart) {
                        resultSetDrillChart.close();
                    }
                    System.out.println("Chart process successful complated...");
                }
            } catch (SQLException e) {
                logError(e, companyId, userId, report.getId(), report.getName(), report.getViewName(), "******************Connection refused  to xls/csv***************");
                System.out.println(sqlQuery);
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ec) {
                    logError(ec, companyId, userId, report.getId(), report.getName(), report.getViewName(), "******************Connection to xls/csv does not closed ***************");
                }
            }

            Integer cachingTimeByReportCode = companySystemSettingsManager.getReportingCacheTime() != null ? companySystemSettingsManager.getReportingCacheTime() : 1800;

            if (key != null && !key.isEmpty()) {
                RedisClient.setKeyForR(key, chartData, ChartData.class, cachingTimeByReportCode);
            }

            return chartData;
        } else {
            return data;
        }
    }

    private void setParametersToStatement(int reSetCount, int j, ReportRpc report, PreparedStatement
            preparedStatement) throws SQLException {
        for (int i = 0; i < report.getValues().size(); i++) {
            String value = report.getValues().get(i).trim();
            String columnType = report.getFieldd().get(i).getType();
            if (columnType != null && !(columnType.equals(SqlColumnType.DATE.getName()) || "n/a".equals(value.trim()))) {
                if (columnType.equals(SqlColumnType.NUMBER.getName())) {
                    preparedStatement.setInt(j, Integer.parseInt(value));
                } else if (columnType.equals(SqlColumnType.MONEY.getName())) {
                    preparedStatement.setBigDecimal(j, new BigDecimal(value));
                } else {
                    final OperationType operationType = OperationType.getByCode(report.getOperators().get(i));
                    String correctrostring = "";
                    if (OperationType.StartsWith.equals(operationType)
                            || OperationType.Contains.equals(operationType)
                            || OperationType.DoesNoTContain.equals(operationType)
                            || OperationType.EndsWith.equals(operationType)) {
                        correctrostring = "@";
                    }
                    preparedStatement.setString(j, correctrostring + value);
                }
                j++;
            }
        }
        reSetCount -= 1;
        if (reSetCount > 1) {
            setParametersToStatement(reSetCount, j, report, preparedStatement);
        }
    }

    private Connection getDataSourceConnection(ReportRpc report) throws SQLException {
        Connection conn;
        customDbUrl = null;
        ViewRpc view = null;
        if (report != null)
            view = SqlQueryUtil.getViewParser(report.getViewCode());
        if (view != null && view.getCustomUrl() != null && view.getCustomUsername() != null && view.getCustomPassword() != null) {
            conn = DriverManager.getConnection(view.getCustomUrl(), view.getCustomUsername(), view.getCustomPassword());
            customDbUrl = view.getCustomUrl();
        } else if (report != null && report.getViewName().contains("(xls)") && Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()) != 24101) {
            conn = dataSourceXLS.getConnection();
        } else {
            EdsReportingDBUrl edsReportingDBUrl = reportingDBUrlManager.getByCompanyID(ServerSecurityContext.getInstance().getCompanyId());
            if (edsReportingDBUrl != null && report != null && !report.isFromKpi()) {
                conn = DriverManager.getConnection(edsReportingDBUrl.getDbUrl(), edsReportingDBUrl.getUserName(), edsReportingDBUrl.getPassword());
                customDbUrl = edsReportingDBUrl.getDbUrl();
            } else {
                conn = dataSourceWFM.getConnection();
            }
        }
        conn.setReadOnly(true);
        return conn;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedList<SelectItem> getFilterSelectItems(String searchKey, ReportRpc report, ColumnRpc column) {
        return getFilterSelectItems(searchKey, report, column, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedList<SelectItem> getFilterSelectItems(String searchKey, ReportRpc report, ColumnRpc column,
                                                       boolean fullSearchKey) {
        LinkedList<SelectItem> list = new LinkedList<>();
        Connection conn = null;
        String query = "";
        try {
            UserSecuritryRpc user = getUser();
            if (user == null) {
                user = new UserSecuritryRpc();
                user.setSiteName("wfm");
                user.setUserId(0);
                user.setCompanyId(SecurityContext.getCompanyID() != null ? SecurityContext.getCompanyID() : 0);
                user.setClientId(0);
                user.setMaxUserRole(0);
            }
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
            if (report.getViewCode() != null) {
                report.setFromKpi(viewRpc.isFromKpi());
            }
            conn = getDataSourceConnection(report);

            PreparedStatement st;

            query = SqlQueryUtil.getReportFilterLists(user.getUserId(), user.getCompanyId(), searchKey, report, column, fullSearchKey);
            st = conn.prepareStatement(query);
            int i = 1;
            if (searchKey != null) {
                st.setString(i, searchKey);
                i++;
                int reSetCount = viewRpc.getQueries().split("\\{where2}").length;
                for (int j = 2; j < reSetCount; j++) {
                    st.setString(i, searchKey);
                    i++;
                }
            }

            ResultSet rs = st.executeQuery();
            System.out.println("-------------------RUN BOLDI------------" + searchKey);
            list = JdbcUtil.getFilterSelectList(rs);
            rs.close();
        } catch (SQLException e) {
            logError(e, getUser().getCompanyId(), getUser().getUserId(), report.getId(), report.getName(), report.getViewName(), "throw exception in getFilterSelectItems method ");
            System.out.println(query);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logError(e, getUser().getCompanyId(), getUser().getUserId(), report.getId(), report.getName(), report.getViewName(), "in getFilterSelectItems method connection close problem ");
            }
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityName(EdsReport.class.getSimpleName());
        if (report.getId() == null) {
            kpiLog.setEntityId(report.getXmlTemplateId());
            kpiLog.setEntityType(report.getViewName());
        } else {
            kpiLog.setEntityId(report.getId());
            kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
        }
        ServerUtils.kpiLog(log, kpiLog, "Report Selected Filter");
        return list;
    }

    private void logError(Exception e, Integer companyId, Integer userId, Integer reportID, String
            reportName, String templateName, String title) {
        String logs = "\n Exception Name=" + title +
                "\n DataBase=" + ServerSecurityContext.getInstance().getDatabase() +
                "\n companyID=" + companyId +
                "\n userID=" + userId +
                "\n reportID=" + reportID +
                "\n report_name=" + reportName +
                "\n template_code=" + templateName;
        log.error(logs, e);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<TableRpc> getTableColumns(ReportRpc reportRpc) {

        ViewRpc viewRpc = SqlQueryUtil.getViewParser(reportRpc.getViewCode());
        reportRpc.setEnabledFilterWidget(viewRpc.getEnabledFilterWidget());
        if (!reportRpc.getSelectedColumns().isEmpty()) {
            Map<String, ColumnRpc> map = new HashMap<>();
            for (int k = 0; k < viewRpc.getTables().size(); k++) {
                for (int s = 0; s < viewRpc.getTables().get(k).getColumns().size(); s++) {
                    map.put(viewRpc.getTables().get(k).getColumns().get(s).getName(), viewRpc.getTables().get(k).getColumns().get(s));
                }
            }

            for (int k = 0; k < reportRpc.getSelectedColumns().size(); k++) {
                ColumnRpc columnRpc = map.get(reportRpc.getSelectedColumns().get(k).getName());
                if (columnRpc != null) {
                    columnRpc.setChecked(Boolean.TRUE);
                }
            }
        } else {
            for (int k = 0; k < viewRpc.getTables().size(); k++) {
                for (int s = 0; s < viewRpc.getTables().get(k).getColumns().size(); s++) {
                    viewRpc.getTables().get(k).getColumns().get(s).setChecked(Boolean.TRUE);
                }
            }
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        if (reportRpc.getId() == null) {
            kpiLog.setEntityId(reportRpc.getXmlTemplateId());
            kpiLog.setEntityType(reportRpc.getViewName());
            kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
        } else {
            kpiLog.setEntityName(EdsReport.class.getSimpleName());
            kpiLog.setEntityId(reportRpc.getId());
            kpiLog.setEntityType(reportRpc.getViewName() + "/" + reportRpc.getName());
        }
        ServerUtils.kpiLog(log, kpiLog, "Get Report Columns");
        return viewRpc.getTables();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<ColumnRpc> getSummariesColumns(ReportRpc report) {
        ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
        Map<String, ColumnRpc> mapSum = new HashMap<>();
        for (int i = 0; i < report.getSumaries().size(); i++) {
            mapSum.put(report.getSumaries().get(i).getName(), report.getSumaries().get(i));
        }

        ArrayList<ColumnRpc> columns = new ArrayList<>();
        if (report.getSelectedColumns().isEmpty()) {
            for (int i = 0; i < viewRpc.getTables().size(); i++) {
                for (int j = 0; j < viewRpc.getTables().get(i).getColumns().size(); j++) {
                    ColumnRpc column = viewRpc.getTables().get(i).getColumns().get(j);
                    if (mapSum.containsKey(column.getName())) {
                        ColumnRpc selectColumn = mapSum.get(column.getName());
                        column.setCount(selectColumn.isCount());
                        column.setSum(selectColumn.isSum());
                        column.setAvg(selectColumn.isAvg());
                        column.setLargest(selectColumn.isLargest());
                        column.setSmallest(selectColumn.isSmallest());
                    }
                    columns.add(column);
                }
            }
        } else {
            for (int i = 0; i < report.getSelectedColumns().size(); i++) {
                ColumnRpc column = report.getSelectedColumns().get(i);
                if (mapSum.containsKey(column.getName())) {
                    ColumnRpc selectColumn = mapSum.get(column.getName());
                    column.setCount(selectColumn.isCount());
                    column.setSum(selectColumn.isSum());
                    column.setAvg(selectColumn.isAvg());
                    column.setLargest(selectColumn.isLargest());
                    column.setSmallest(selectColumn.isSmallest());
                }
                columns.add(column);
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        if (report.getId() == null) {
            kpiLog.setEntityId(report.getXmlTemplateId());
            kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
            kpiLog.setEntityType(report.getViewName());
        } else {
            kpiLog.setEntityId(report.getId());
            kpiLog.setEntityName(EdsReport.class.getSimpleName());
            kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
        }
        ServerUtils.kpiLog(log, kpiLog, "Get Summary Report Columns");

        return columns;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedList<ColumnRpc> getSelectedColumns(ReportRpc report) {
        ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
        LinkedList<ColumnRpc> selectedList = new LinkedList<>();
        for (int k = 0; k < viewRpc.getTables().size(); k++) {
            selectedList.addAll(viewRpc.getTables().get(k).getColumns());
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        if (report.getId() == null) {
            kpiLog.setEntityId(report.getXmlTemplateId());
            kpiLog.setEntityType(report.getViewName());
            kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
        } else {
            kpiLog.setEntityId(report.getId());
            kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
            kpiLog.setEntityName(EdsReport.class.getSimpleName());
        }
        ServerUtils.kpiLog(log, kpiLog, "Get Report Selected Columns");
        return selectedList;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedList<ColumnRpc> getFilterColumns(ReportRpc report) {
        ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityName(EdsReport.class.getSimpleName());
        if (report.getId() == null) {
            kpiLog.setEntityId(report.getXmlTemplateId());
            kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
            kpiLog.setEntityType(report.getViewName());
        } else {
            kpiLog.setEntityName(EdsReport.class.getSimpleName());
            kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
            kpiLog.setEntityId(report.getId());
        }
        ServerUtils.kpiLog(log, kpiLog, "Get Report Filter Columns");
        return viewRpc.getFilterColumns();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserSecuritryRpc getUser() {
        return getUser(null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserSecuritryRpc getUser(Integer userId) {
        try {
            EdsUser user;
            if (userId != null) {
                user = userManager.get(userId);
            } else {
                user = (EdsUser) ServerSecurityContext.getInstance().getUser();
            }

            if (user != null) {
                user = userManager.get(user.getObjectID());
                UserSecuritryRpc userSecuritry = new UserSecuritryRpc();
                userSecuritry.setUserId(user.getObjectID());
                userSecuritry.setUserRoles(user.getRolesAsIntegersString());
                userSecuritry.setMaxUserRole(ServerUtils.getMaxRoleID(user.getRolesAsIntegersString()));
                if (userSecuritry.getMaxUserRole().equals(7)) {
                    userSecuritry.setClientId(user.getClientContact().getClientID());
                }
                userSecuritry.setUserFullName(user.getFullName());
                userSecuritry.setSiteName("wfm");
                userSecuritry.setDomainName("#");
                userSecuritry.setCompanyId(user.getCompany().getObjectID());
                userSecuritry.setCompanyName(user.getCompany() != null && user.getCompany().getName() != null ? user.getCompany().getName() : "");
                userSecuritry.setTimezone(user.getTimezone());
                return userSecuritry;
            }
        } catch (EntityNotFoundException exception) {
            return null;
        }
        return null;
    }

    @Transactional
    @Override
    public Boolean createFavouriteReportTemplate(Integer reportid) {

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsReport.class.getSimpleName());
        kpiLog.setEntityId(reportid);
        boolean iscreated;
        EdsCompanyFavouriteReportTemplates element = reportingManager.getFavouriteReportTemplate(getUser().getUserId(), reportid, getUser().getCompanyId());
        if (element == null) {
            reportingManager.createFavouriteReportTemplate(getUser().getUserId(), reportid, getUser().getCompanyId());
            element = reportingManager.getFavouriteReportTemplate(getUser().getUserId(), reportid, getUser().getCompanyId());
            if (element.getReporting() != null) {
                kpiLog.setEntityType(element.getReporting().getViewName() + "/" + element.getReporting().getName());
            }
            iscreated = true;
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Added New Favorite Report");
        } else {
            if (element.getReporting() != null) {
                kpiLog.setEntityType(element.getReporting().getViewName() + "/" + element.getReporting().getName());
            }
            reportingManager.deleteFavouriteReportTemplate(getUser().getUserId(), reportid, getUser().getCompanyId());
            iscreated = false;
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            ServerUtils.kpiLog(log, kpiLog, "Deleted Favorite Report");
        }
        return iscreated;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<ListItem> getFavReports() {
        ArrayList<ListItem> reportList = new ArrayList<>();
        for (EdsReport report : reportingManager.getFavReports(getUser().getUserId())) {
            if (report != null) {
                reportList.add(report.toListItem());
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsReport.class.getSimpleName());
        kpiLog.setEntityId(null);
        kpiLog.setEntityName(null);
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Favorite Reports");
        return reportList;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getThemeForSystem() {
        final EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        return user.getCompany().getCompanySettings().getThemeForSystem() != null ? user.getCompany().getCompanySettings().getThemeForSystem() : EdsContextParams.getDefaultTheme();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String[] getUserNameAndCompanyName() {
        UserSecuritryRpc user = getUser();
        String[] strings = new String[4];
        if (user != null) {
            EdsCompany company = userManager.getUser().getCompany();
            strings[0] = companyAttachmentManager.getCompanyLogoUrl(company, CommandConstants.FOR_EMPLOYEES);
            strings[1] = user.getUserFullName() != null ? user.getUserFullName() : "";
            strings[2] = user.getCompanyName() != null ? user.getCompanyName() : "";
            if (user.getDomainName() != null) {
                strings[3] = ("#".equals(user.getDomainName()) ? null : user.getDomainName());
            }
        }
        return strings;
    }

    @Transactional
    @Override
    public Integer saveOrUpdateReportTemplate(ListingFilterParameter filterParameter) {
        try {
            Integer categoryID = filterParameter.getCategoryID();
            EdsReportTemplate reportTemplate;
            if (filterParameter.getObjectId() != null) {
                reportTemplate = reportTemplateManager.get(filterParameter.getObjectId());
                reportTemplate.getAuditInfo().setModificationDate(new Date());
            } else {
                reportTemplate = new EdsReportTemplate();
                reportTemplate.getAuditInfo().setCreationDate(new Date());
                reportTemplate.getAuditInfo().setModificationDate(new Date());
            }
            reportTemplate.setName(filterParameter.getName());
            reportTemplate.setBody(filterParameter.getDescription());
            reportTemplate.setCustom(filterParameter.isSelected());
            reportTemplate.setStepId(filterParameter.getStepID());
            reportTemplate.setAttachmentId(filterParameter.getAttachmentId());
            reportTemplate.setSimlified(filterParameter.isSimpilifiedReportTemplate());
            {
                EdsReportTemplateCategory category = null;
                if (categoryID != null) {
                    category = reportTemplateCategoryManager.get(categoryID);
                }
                if (category != null) {
                    reportTemplate.setCategoryCode(category.getCode());
                }
            }
            reportTemplate.setLibrary(filterParameter.getLibrary());

            reportTemplateManager.createOrUpdate(reportTemplate);

            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityType(reportTemplate.getName());
            kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());

            if (!filterParameter.isSelected()) {
                reportTemplate.setCustomReportTemplates(null);
                reportTemplateManager.createOrUpdate(reportTemplate);

                StringBuilder patchRole = new StringBuilder();
                patchRole
                        .append("delete from \"public\".customreporttemplate where reportcode is null; ")
                        .append("delete from customreporttemplate where reportcode =E'").append(reportTemplate.getCode()).append("'; \n");
                reportTemplateManager.updateNative(patchRole.toString());

                for (String companyID : companyManager.getExistingSchemas()) {
                    saveReportTemplatePermission(reportTemplate, filterParameter, Integer.valueOf(companyID));

                }

                kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                kpiLog.setEntityId(reportTemplate.getObjectID());
                ServerUtils.kpiLog(log, kpiLog, "Updated Report Template");

            } else {
                //Custom
                reportTemplate.getCustomReportTemplates().clear();
                StringBuilder patchRole = new StringBuilder(100);
                patchRole.append("delete from \"public\".customreporttemplate where reportcode is null; ");
                List<String> deleteRange = companyManager.getExistingSchemas();
                List<String> selectedCompanies = new ArrayList<>(filterParameter.getCompaines().length);
                for (Integer item : filterParameter.getCompaines()) {
                    selectedCompanies.add("" + item);
                }
                {
                    String templateCode = PermissionConstants.REPORTING_TEMPLATE + "_" + reportTemplate.getCode();
                    for (String companyID : deleteRange) {
                        patchRole.append("delete from \"").append(companyID).append("\".rolepermission where permissioncode=E'").append(templateCode).append("'; \n");
                        patchRole.append("delete from \"").append(companyID).append("\".reportingpermission where code=E'").append(templateCode).append("'; \n");
                    }
                }
                deleteRange.removeAll(selectedCompanies);
                for (String companyID : deleteRange) {
                    String templateCode = PermissionConstants.REPORTING_TEMPLATE + "_" + reportTemplate.getCode() + (companyID == null ? "" : "_" + companyID);
                    patchRole.append("delete from \"").append(companyID).append("\".rolepermission where permissioncode=E'").append(templateCode).append("'; \n");
                    patchRole.append("delete from \"").append(companyID).append("\".reportingpermission where code=E'").append(templateCode).append("'; \n");
                }
                reportTemplateManager.updateNative(patchRole.toString());
                patchRole = new StringBuilder();
                for (Integer companyID : filterParameter.getCompaines()) {
                    patchRole.append("insert into \"public\".customReportTemplate(companyId,reportCode) select ").append(companyID).append(",E'").append(reportTemplate.getCode()).append("';\n ");
                    saveReportTemplatePermission(reportTemplate, filterParameter, companyID);

                }
                reportTemplateManager.updateNative(patchRole.toString());

                reportTemplateManager.createOrUpdate(reportTemplate);

                kpiLog.setActionType(KpiLog.ActionType.ADD);
                kpiLog.setEntityId(reportTemplate.getObjectID());
                ServerUtils.kpiLog(log, kpiLog, "Added New Report Template");

            }
            return reportTemplate.getObjectID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveReportTemplatePermission(EdsReportTemplate reportTemplate, ListingFilterParameter
            filterParameter, Integer companyID) {
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }
        EdsReportingPermission parentCategory = null;
        if (StringUtils.isNotBlank(reportTemplate.getCategoryCode())) {
            parentCategory = switch (reportTemplate.getCategoryCode()) {
                case "ACCOUNTING" ->
                        reportingPermissionManager.findByCode(companyID, PermissionConstants.REPORTING_TEMPLATE_CATEGORY_ACCOUNTING, PermissionConstants.REPORTING);
                case "CRM" ->
                        reportingPermissionManager.findByCode(companyID, PermissionConstants.REPORTING_TEMPLATE_CATEGORY_CRM, PermissionConstants.REPORTING);
                case "PM" ->
                        reportingPermissionManager.findByCode(companyID, PermissionConstants.REPORTING_TEMPLATE_CATEGORY_PM, PermissionConstants.REPORTING);
                case "HRMS" ->
                        reportingPermissionManager.findByCode(companyID, PermissionConstants.REPORTING_TEMPLATE_CATEGORY_HRMS, PermissionConstants.REPORTING);
                case "PAYROLL" ->
                        reportingPermissionManager.findByCode(companyID, PermissionConstants.REPORTING_TEMPLATE_CATEGORY_PAYROLL, PermissionConstants.REPORTING);
                case "CUSTOM" ->
                        reportingPermissionManager.findByCode(companyID, PermissionConstants.REPORTING_TEMPLATE_CATEGORY_CUSTOM, PermissionConstants.REPORTING);
                case "SYSTEM" ->
                        reportingPermissionManager.findByCode(companyID, PermissionConstants.REPORTING_TEMPLATE_CATEGORY_SYSTEM, PermissionConstants.REPORTING);
                default ->
                        reportingPermissionManager.findByCode(companyID, PermissionConstants.REPORTING_SAVED_REPORT_CATEGORY_CUSTOM, PermissionConstants.REPORTING);
            };
        }
        if (parentCategory == null) {
            parentCategory = reportingPermissionManager.findByCode(companyID, PermissionConstants.REPORTING_TEMPLATE, PermissionConstants.REPORTING);
        }

        EdsReportTemplateCategory templateCategory = reportTemplateCategoryManager.getReportTemplateCategoryByCode(reportTemplate.getCategoryCode());
        String categoryCode = PermissionConstants.REPORTING_TEMPLATE_CATEGORY + "_" + templateCategory.getName().toUpperCase();
        EdsReportingPermission category = reportingPermissionManager.findByCode(companyID, categoryCode, PermissionConstants.REPORTING);
        StringBuilder patchRole = new StringBuilder();
        if (category == null) {
            patchRole.append("insert into \"").append(companyID).append("\".reportingpermission (code,iscore,context,parent,name,sorder,modulecode,companyid) ");
            patchRole.append(" values (");
            patchRole.append("'").append(categoryCode).append("', ");
            patchRole.append("'").append(false).append("', ");
            patchRole.append("'").append(PermissionConstants.REPORTING).append("', ");
            patchRole.append(parentCategory.getObjectID()).append(", ");
            patchRole.append("'").append(templateCategory.getName()).append("',");
            patchRole.append(templateCategory.getObjectID()).append(",");
            patchRole.append("'").append(PermissionConstants.REPORTING_SYSTEM).append("', ");
            patchRole.append(companyID != null ? companyID : ServerSecurityContext.getInstance().getCompanyId()).append("); ");

            reportTemplateManager.updateNative(patchRole.toString());
            category = reportingPermissionManager.findByCode(companyID, categoryCode, PermissionConstants.REPORTING);
            patchRole = new StringBuilder();
        }
        String templateCode = PermissionConstants.REPORTING_TEMPLATE + "_" + reportTemplate.getCode() + (companyID == null ? "" : "_" + companyID);
        EdsReportingPermission permissionItem = reportingPermissionManager.findByCode(companyID, templateCode, PermissionConstants.REPORTING);
        if (permissionItem == null) {
            patchRole.append("insert into \"").append(companyID).append("\".reportingpermission (code,iscore,context,parent,name,sorder,modulecode,companyid) ");
            patchRole.append(" values (");
            patchRole.append("'").append(templateCode).append("', ");
            patchRole.append("'").append(false).append("', ");
            patchRole.append("'").append(PermissionConstants.REPORTING).append("', ");
            patchRole.append(category != null ? category.getObjectID() : 0).append(", ");
            patchRole.append("'").append(reportTemplate.getName()).append("',");
            patchRole.append(reportTemplate.getObjectID()).append(",");
            patchRole.append("'").append(PermissionConstants.REPORTING_SYSTEM).append("', ");
            patchRole.append(companyID != null ? companyID : ServerSecurityContext.getInstance().getCompanyId()).append("); \n ");
        }

        patchRole.append("delete from \"").append(companyID).append("\".rolepermission where permissioncode like E'").append(templateCode).append("%';\n ");
        if (filterParameter != null && filterParameter.getColumnsOfListing() != null && !"".equals(filterParameter.getColumnsOfListing())) {
            for (String role : filterParameter.getColumnsOfListing()) {
                patchRole.append("insert into \"").append(companyID).append("\".rolepermission(permissioncode,rolecode,access) ");
                patchRole.append(" select distinct E'").append(templateCode).append("',E'").append(role).append("','ALLOW' ").append(" where 0!=( select count(id) from \"").append(companyID).append("\".rolepermission ").append(" where permissioncode != E'").append(templateCode).append("' and rolecode != '").append(role).append("');\n ");
            }
        }
        reportTemplateManager.updateNative(patchRole.toString());
        reportTemplateManager.flushAndClear();
    }

    @Transactional
    @Override
    public void saveReportPermission(EdsReport report) {
        if (report.getFolder() == null || "Private".equals(report.getFolder().getType()) || null == report.getViewCode()) {
            return;
        }
        EdsReportTemplate template = reportTemplateManager.getByCode(report.getViewCode());
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        if (companyId == null || template == null || Boolean.TRUE.equals(template.getCustom()) && !template.getCustomCompany().contains(Integer.valueOf(companyId)) && !template.getLibrary()) {
            return;
        }

        String parentCode = PermissionConstants.REPORTING_TEMPLATE + "_" + report.getViewCode();
        EdsReportingPermission parentCategory = reportingPermissionManager.findByCode(Integer.valueOf(companyId), parentCode, PermissionConstants.REPORTING);
        if (parentCategory == null) {
            parentCode = PermissionConstants.REPORTING_TEMPLATE + "_" + report.getViewCode() + "_" + companyId;
            parentCategory = reportingPermissionManager.findByCode(Integer.valueOf(companyId), parentCode, PermissionConstants.REPORTING);
        }

        EdsReportTemplateCategory templateCategory = reportTemplateCategoryManager.getReportTemplateCategoryByCode(template.getCategoryCode());
        String categoryCode = PermissionConstants.REPORTING_SAVED_REPORT_CATEGORY + "_" + templateCategory.getName().toUpperCase();
        EdsReportingPermission category = reportingPermissionManager.findByCode(Integer.valueOf(companyId), categoryCode, PermissionConstants.REPORTING);
        if (category == null) {
            category = new EdsReportingPermission();
            category.setModuleCode(PermissionConstants.REPORTING_SYSTEM);
        }
        category.setCode(categoryCode);
        category.setCore(false);
        category.setContext(PermissionConstants.REPORTING);
        category.setParent(parentCategory != null ? parentCategory.getObjectID() : null);
        category.setName(templateCategory.getName());
        category.setSorder(templateCategory.getObjectID());
        reportingPermissionManager.createOrUpdate(category);

        String reportCode = PermissionConstants.REPORTING_SAVED_REPORT + "_" +
                report.getCode() + (template.getLibrary() ? "" : ("_" + companyId));
        EdsReportingPermission permissionItem = reportingPermissionManager.findByCode(Integer.valueOf(companyId), reportCode, PermissionConstants.REPORTING);
        if (permissionItem == null) {
            permissionItem = new EdsReportingPermission();
            permissionItem.setCode(reportCode);
            category.setModuleCode(PermissionConstants.REPORTING_SYSTEM);
        }
        if (!template.getLibrary()) {
            permissionItem.setCompanyId(Integer.valueOf(companyId));
        }
        permissionItem.setModuleCode(PermissionConstants.REPORTING_SYSTEM);
        permissionItem.setCore(false);
        permissionItem.setContext(PermissionConstants.REPORTING);
        permissionItem.setParent(category.getObjectID());
        permissionItem.setName(report.getName());
        permissionItem.setSorder(report.getObjectID());
        if (permissionItem.getObjectID() == null) {
            reportingPermissionManager.persist(permissionItem);
        }

        String roles = ADMIN_CODE + "," + DR_CODE + "," + ACCOUNTANT_CODE;
        List<String> roleList = rolePermissionManager.getRolesByPermissionCode(permissionItem.getCode());
        for (String role : roles.split(",")) {
            if (!roleList.contains(role)) {
                EdsRolePermission rolePermission = new EdsRolePermission();
                rolePermission.setPermissioncode(permissionItem.getCode());
                rolePermission.setPriviledgeCode(PermissionConstants.ALLOW);
                rolePermission.setRole(roleManager.getByCode(role));
                reportingPermissionManager.createOrUpdate(permissionItem);
            }
        }
        report.setPermissionCode(permissionItem.getCode());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportTemplateItem getReportTemplate(Integer objectID) {
        ReportTemplateItem reportTemplateItem = new ReportTemplateItem();
        if (objectID != null) {
            EdsReportTemplate reportTemplate = reportTemplateManager.get(objectID);
            if (reportTemplate != null) {
                reportTemplateItem.setId(reportTemplate.getObjectID());
                reportTemplateItem.setName(reportTemplate.getName());
                reportTemplateItem.setBody(reportTemplate.getBody());
                reportTemplateItem.setStepId(reportTemplate.getStepId());
                reportTemplateItem.setCustom(reportTemplate.getCustom());
                reportTemplateItem.setLibrary(reportTemplate.getLibrary());
                reportTemplateItem.setSimplified(reportTemplate.getSimlified());
                if (reportTemplate.getCategoryCode() != null && !"".equals(reportTemplate.getCategoryCode())) {
                    EdsReportTemplateCategory templateCategory = reportTemplateCategoryManager.getReportTemplateCategoryByCode(reportTemplate.getCategoryCode());
                    reportTemplateItem.setCategoryId(templateCategory.getObjectID());
                }
            }
        }
        ArrayList<EdsReportTemplateCategory> templateCategoryList = reportTemplateCategoryManager.getReportTemplateCategoryList();
        if (templateCategoryList != null) {
            ArrayList<SelectItem> categories = new ArrayList<>();
            for (EdsReportTemplateCategory category : templateCategoryList) {
                categories.add(category.getAsSelectItem());
            }
            reportTemplateItem.setCategories(categories.toArray(new SelectItem[0]));
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
        kpiLog.setEntityType(reportTemplateItem.getName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(log, kpiLog, "Get Single Report Template");
        return reportTemplateItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ReportingListItem> getReportingXMLTemplateList(ListingFilterParameter filterParameter) {
        return reportTemplateManager.getReportingXMLTemplateList(filterParameter);
    }

    @Transactional
    @Override
    public void deleteReportingXMLTemplateFromCompany(Integer objectID, Integer companyID) {
        // agar companyID null bo`lsa template to`liq o`chiriladi, aks holda templateni shu companydan olib tashlaydi
        EdsReportTemplate template = reportTemplateManager.get(objectID);
        if (companyID != null) {
            template.getCustomReportTemplates().clear();
            reportTemplateManager.update(template);
        } else {
            template.getCustomReportTemplates().clear();
            reportTemplateManager.update(template);
            reportTemplateManager.delete(template);
        }
        String permissionCode = PermissionConstants.REPORTING_TEMPLATE + "_" + template.getCode();
        StringBuilder patchRole = new StringBuilder(100);
        for (String companyId : companyManager.getExistingSchemas()) {
            patchRole.append("delete from \"").append(companyId).append("\".rolepermission where permissioncode=E'").append(permissionCode).append("';\n ");
            patchRole.append("delete from \"").append(companyId).append("\".reportingpermission where code='").append(permissionCode).append("' and context='").append(PermissionConstants.REPORTING).append("'; ");
        }
        executeNative(patchRole.toString());

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
        kpiLog.setEntityType(template.getName());
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(log, kpiLog, "Delete Report Template From Company");
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<TeamEmployees> getCompanies(Integer templateID) {
        ArrayList<TeamEmployees> result = new ArrayList<>();
        WfmTreeItem team = new WfmTreeItem(null, "Companies");
        LinkedList<WfmTreeItem> companies = new LinkedList<>();
        EdsReportTemplate template = templateID != null ? reportTemplateManager.get(templateID) : null;
        List<Object[]> companies2 = companyManager.getSchemaList(new ListingFilterParameter());
        List<Integer> list = null;
        if (template != null) {
            list = template.getCustomCompany();
        }
        if (companies2 != null && companies2.size() > 0) {
            for (Object[] c : companies2) {
                Integer company_id = Integer.valueOf(c[0].toString());
                boolean isChecked = template != null && list != null && list.contains(company_id);
                companies.add(new WfmTreeItem(company_id, c[1] + "(" + company_id + ")", isChecked));
            }
            result.add(new TeamEmployees(team, companies));
        }
        return result;
    }

    @Transactional
    @Override
    public void saveOrUpdateReportTemplateCategory(SelectItem categoryItem) {
        EdsReportTemplateCategory category = new EdsReportTemplateCategory();
        if (categoryItem.getId() != null) {
            category = reportTemplateCategoryManager.get(categoryItem.getId());
        }
        category.setName(categoryItem.getName());
        reportTemplateCategoryManager.createOrUpdate(category);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsReportTemplateCategory.class.getSimpleName());
        kpiLog.setEntityType(category.getName());
        kpiLog.setEntityId(categoryItem.getId());
        if (categoryItem.getId() == null) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add New Report Template Category");
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update Report Template Category");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem getReportTemplateCategory(Integer objectID) {
        EdsReportTemplateCategory category = reportTemplateCategoryManager.get(objectID);
        return category != null ? category.getAsSelectItem() : null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<SelectListRpc> getReportTemplateCategories() {
        SelectListRpc temp;
        ArrayList<EdsReportTemplateCategory> reportTemplateCategories = reportTemplateCategoryManager.getReportTemplateCategoryList();
        ArrayList<SelectListRpc> categories = new ArrayList<>();
        for (EdsReportTemplateCategory reportTemplateCategoryategory : reportTemplateCategories) {
            temp = new SelectListRpc();
            temp.setId(reportTemplateCategoryategory.getObjectID());
            temp.setName(reportTemplateCategoryategory.getName());
            categories.add(temp);
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityId(null);
        kpiLog.setEntityName(EdsReportTemplateCategory.class.getSimpleName());
        kpiLog.setEntityType(null);
        ServerUtils.kpiLog(log, kpiLog, "Get Report Template Categories");

        return categories;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<Integer> getEmployeeIDsByReportID(Integer reportID) {
        return reportingManager.getEmployeeIDsByReportID(reportID);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportRpc getReport(Integer id, Integer companyID, Integer userID) {
        return getReport(id, true, userManager.getUserByUserIdAndCompanyId(userID, companyID));
    }

    @Override
    @Transactional
    public void makeTestingReportSchema(Integer mySchema) {
        if (reportingManager.makeTestingReportSchema(mySchema)) {
            executeNative("ALTER TABLE \"" + mySchema + "\".reporting ADD COLUMN companyid integer, ADD COLUMN last_exception text, add column issuccess boolean");
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<String[]> getReportsNative(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null || filterParametrs.getCompanyID() == null) {
            return null;
        }
        String text = "SELECT r.id,r.name,r.companyid,r.last_exception,r.issuccess,r.viewname,t.islibrary isdefault " + getReportNativeSQL(filterParametrs);
        text += " order by ";
        if (filterParametrs.getSortField() != null) {
            text += ("viewName".equals(filterParametrs.getSortField())) ? " r.viewname " : ("reportName".equals(filterParametrs.getSortField())) ? " r.name " :
                    ("companyid".equals(filterParametrs.getSortField())) ? " r.companyid " : ("status".equals(filterParametrs.getSortField())) ? " r.issuccess " :
                            ("exception".equals(filterParametrs.getSortField())) ? " r.last_exception " : ("isDefault".equals(filterParametrs.getSortField())) ? " t.isdefault " : " id ";
            text += (filterParametrs.isAscending()) ? " ASC " : " DESC ";
        } else {
            text += (filterParametrs.isAscending()) ? "r.id ASC " : "id DESC ";
        }
        text += " offset " + filterParametrs.getStart() + " limit " + filterParametrs.getLimit();
        return getDataTable(text);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getReportsNativeCount(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null || filterParametrs.getCompanyID() == null) {
            return null;
        }
        String text = "select count(r.id) " + getReportNativeSQL(filterParametrs);
        return Integer.valueOf(getValue(text));
    }

    private String getReportNativeSQL(ListingFilterParameter filterParametrs) {
        return " FROM \"" + filterParametrs.getCompanyID() + "\".reporting r inner join reporttemplate t on r.viewcode =t.code ";
    }

    @Transactional
    @Override
    public void runReport(ArrayList<Integer> integers, Integer companyid) {
        Integer i = 0;
        for (Integer id : integers) {
            try {
                runSingleReport(companyid, id);
            } catch (Exception e) {
            }
            if (i++ % 10 == 0) {
                reportingManager.flushAndClear();
            }
        }
    }

    @Override
    @Transactional
    public void runSingleReport(Integer companyid, Integer id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", "" + id);
        try {
            ReportRpc report = getReport(id, false);
            report.setBrowserTimeZone("GMT+05:00");
            try {
                ResultSet result;
                ReportGenerateTableRpc reportGenerateTableRpc = new ReportGenerateTableRpc();
                try {
                    if (ReportType.TABULAR.name().equals(report.getTableType())) {
                        result = getTabularReportResult(report, userManager.getSchemaAllUsers("" + companyid, 1).get(0).getObjectID());
                    } else {
                        result = getSummaryReportResult(report, userManager.getSchemaAllUsers("" + companyid, 1).get(0).getObjectID());
                    }
                    result.next();
                } catch (Exception exp) {
                    reportGenerateTableRpc.setTextExceptionLog(getLog(exp));
                }
                if (reportGenerateTableRpc.getTextExceptionLog() != null) {
                    map.put("issuccess", "false");
                    map.put("last_exception", "<br/>" + reportGenerateTableRpc.getTextExceptionLog());
                } else {
                    map.put("issuccess", "true");
                    map.put("last_exception", "<br/>" + "success");
                    System.out.print(report.getName() + " Success");
                }
            } catch (Exception exp) {
                map.put("issuccess", "false");
                map.put("last_exception", exp + "<br/>" + getLog(exp));
            }
        } catch (Exception ex) {
            map.put("issuccess", "false");
            map.put("last_exception", ex + "<br/>" + getLog(ex));
        }
        executeNative(reportingManager.setParametersNative(map, companyid));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<ReportTemplateCategoryRpc> getReportListUser() {
        HashMap<String, ReportTemplateCategoryRpc> temp = new HashMap<>();
        ReportTemplateCategoryRpc myCategory = new ReportTemplateCategoryRpc();
        myCategory.setId(0);
        myCategory.setLibrary(false);
        myCategory.setName("My Library");

        ReportTemplateCategoryRpc myFavourites = new ReportTemplateCategoryRpc();
        myFavourites.setId(1000);
        myFavourites.setLibrary(false);
        myFavourites.setAsStarred(true);
        myFavourites.setName("My Favourites");
        UserSecuritryRpc user = getUser();
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setSubscriptionTypeName(user.getDomainName());
        filter.setCompanyID(user.getCompanyId());
        filter.setUserID(user.getUserId());
        filter.setRoles(userManager.getUser().getRolesCodeAsString());
        List<Object[]> list = reportingManager.listObject(filter);
        for (Object[] item : list) {

            ReportItem reportItem = new ReportItem(item).invoke();
            if (reportItem.getFakeReport()) {
                continue;
            }
            if (reportItem.getFavouriteId() != null) {
                ListItem listItem = reportItem.toListItem();
                listItem.setAsStarred(true);
                myFavourites.addReportRpc(listItem);
            }
            if (!Boolean.TRUE.equals(reportItem.getIsLibrary())) {
                ListItem listItem = reportItem.toListItem();
                listItem.setLibrary(false);
                myCategory.addReportRpc(listItem);
            } else {
                if (temp.get(reportItem.getCategoryName()) == null) {
                    temp.put(reportItem.getCategoryName(), new ReportTemplateCategoryRpc());
                    temp.get(reportItem.getCategoryName()).setLibrary(true);
                    temp.get(reportItem.getCategoryName()).setName(reportItem.getCategoryName());
                    temp.get(reportItem.getCategoryName()).setId(reportItem.getCategoryId());
                }
                ListItem listItem = reportItem.toListItem();
                if (reportItem.getFavouriteId() != null) {
                    listItem.setAsStarred(true);
                }
                listItem.setLibrary(true);
                temp.get(reportItem.getCategoryName()).addReportRpc(listItem);
            }
        }
        ArrayList<ReportTemplateCategoryRpc> savedReportCategoryList = new ArrayList<>(temp.values());
        savedReportCategoryList.add(myCategory);
        savedReportCategoryList.add(myFavourites);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityId(null);
        kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
        kpiLog.setEntityType(null);
        ServerUtils.kpiLog(log, kpiLog, "Get Report List By User");

        return savedReportCategoryList;
    }

    @Transactional
    @Override
    public String getValue(String sqlQuery) {
        String value = "";
        Connection conn = null;
        try {
            conn = dataSourceWFM.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                value = resultSet.getString(1);
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException el) {
                el.printStackTrace();
            }
            e.printStackTrace();
        }
        return value;
    }

    private String getValue(String sqlQuery, Connection conn) {
        String value = "";
        Boolean isRun = null;
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sqlQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                value = resultSet.getString(1);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception el) {
                el.printStackTrace();
            }
            e.printStackTrace();
        }
        return value;
    }

    @Transactional
    @Override
    public ArrayList<String[]> getDataTable(String sqlQuery) {
        ArrayList<String[]> arrayList = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dataSourceWFM.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ResultSet resultSet = st.executeQuery();
            int n = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                String[] temp = new String[n];
                for (int i = 1; i < n + 1; i++) {
                    temp[i - 1] = resultSet.getString(i);
                }
                arrayList.add(temp);
            }
            try {
                conn.close();
            } catch (SQLException e) {
                try {
                    conn.close();
                } catch (SQLException el) {
                    el.printStackTrace();
                }
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @Transactional
    @Override
    public Boolean executeNative(String sqlQuery) {
        Connection conn = null;
        Boolean isRun = null;
        try {
            conn = dataSourceWFM.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            isRun = st.execute();
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return isRun;
    }

    private Boolean executeNative(String sqlQuery, Connection conn) {
        Boolean isRun = null;
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            isRun = st.execute();
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException el) {
                el.printStackTrace();
            }
            e.printStackTrace();
        }
        return isRun;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Boolean getReportStar(Integer reportid) {
        return reportingManager.getReportStar(reportid, getUser().getUserId());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public String getInsertCommand(Integer[] IDs) {
        StringBuilder stringBuilder = new StringBuilder();
        if (IDs != null) {
            for (Integer ID : IDs) {
                stringBuilder.append(reportTemplateManager.get(ID).generateInsertCommand());
            }
        }
        return stringBuilder.toString();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<SelectItem> getTemplateRoles(Integer companyID, Integer objectID) {
        companyID = companyID == null ? userManager.getUser().getCompany().getObjectID() : companyID;
        List<String> items = new ArrayList<>();
        if (objectID != null) {
            EdsReportTemplate reportTemplate = reportTemplateManager.get(objectID);
            String templateCode = PermissionConstants.REPORTING_TEMPLATE + "_" + reportTemplate.getCode() + (companyID == null ? "" : "_" + companyID);
            EdsReportingPermission reportingpermission = reportingPermissionManager.findByCode(companyID, templateCode, PermissionConstants.REPORTING);
            if (reportingpermission != null) {
                List<EdsRolePermission> rolePermissions = rolePermissionManager.getPermissionRolePermissions(companyID, reportingpermission.getCode());
                for (EdsRolePermission item : rolePermissions) {
                    if (item.getRole() != null) {
                        items.add(item.getRole().getCode());
                    }
                }
            }
        }
        ArrayList<SelectItem> roles = new ArrayList<>();
        for (EdsRole edsRole : roleManager.getRoleListByCompany(companyID)) {
            if (Boolean.TRUE.equals(edsRole.getSystem())) {
                SelectItem role = new SelectItem(null, edsRole.getName(), edsRole.getCode());
                if (objectID != null) {
                    role.setSelected(items.contains(edsRole.getCode()));
                }
                roles.add(role);
            }
        }
        return roles;
    }

    @Override
    @Transactional
    public void deleteOnboardingReportTemplate(String code, Integer companyID) {
        EdsReportTemplate template = reportTemplateManager.getByCode(code);
        if (template != null) {
            deleteReportingXMLTemplateFromCompany(template.getObjectID(), companyID);
        }
    }

    @Override
    public ListResult<SearchPeopleTO> searchPeople(ListingFilterParameter fp) {
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());

        StringBuilder sql = new StringBuilder();
        sql.append("select t.*,");
        sql.append("(case when lower(t.name) like lower('").append(fp.getSearchKey()).append("') then 1");
        sql.append(" when lower(t.name) like lower('").append(fp.getSearchKey()).append("%') then 2");
        sql.append(" when lower(t.name) like lower('%").append(fp.getSearchKey()).append("%') then 3");
        sql.append(" else 4 end) rank");
        sql.append(" from (");

        //Employee
        sql.append(" select myuser.id item_id, (myuser.firstname||' '||myuser.lastname) AS name,contact.primaryphone mobile,contact.primaryemail email, 'EMPLOYEE' AS item_type");
        sql.append(" from \"").append(companyID).append("\".employee employee");
        sql.append(" INNER JOIN \"").append(companyID).append("\".myuser myuser ON employee.id = myuser.id");
        sql.append(" LEFT JOIN \"").append(companyID).append("\".employeeprofile profile ON profile.id = employee.profileid");
        sql.append(" LEFT JOIN \"").append(companyID).append("\".crmContact contact ON contact.id = profile.contact_id");
        sql.append(" WHERE contact.primaryemail IS NOT NULL and contact.primaryemail !=''");
        sql.append(" AND coalesce(lower(myuser.firstname),'')||' '||coalesce(lower(myuser.lastname),'')||' '||coalesce(lower(contact.primaryphone),'')||' '||coalesce(lower(contact.primaryemail),'')  like lower('%").append(fp.getSearchKey()).append("%')");

        //Customer and Supplier

        sql.append(" UNION ALL");
        sql.append(" select crmAccount.id AS item_id,crmAccount.name,crmAccount.phone mobile,crmAccount.email,");
        sql.append(" (CASE WHEN reference.code = '").append(EdsCrmAccount.CUSTOMER).append("'").append(" THEN 'CUSTOMER' ELSE 'SUPPLIER' END) as item_type");
        sql.append(" from \"").append(companyID).append("\".crmaccount crmAccount");
        sql.append(" LEFT JOIN \"").append(companyID).append("\".crmAccount_types accountType ON crmAccount.id = accountType.crmaccount_id");
        sql.append(" LEFT JOIN \"").append(companyID).append("\".reference reference ON reference.id = accountType.type_id");
        sql.append("  WHERE crmAccount.email IS NOT NULL and crmAccount.email != ''");
        sql.append(" AND reference.code in('").append(EdsCrmAccount.CUSTOMER).append("',").append("'").append(EdsCrmAccount.SUPPLIER).append("')");
        sql.append(" AND coalesce(lower(crmAccount.name),'')||' '||coalesce(lower(crmAccount.phone),'')||' '||coalesce(lower(crmAccount.email),'') like lower('%").append(fp.getSearchKey()).append("%')");


        //Contact and Lead
        sql.append(" UNION ALL");
        sql.append(" select crmContact.id AS item_id,(crmContact.firstname||' '||crmContact.lastname) AS name,crmContact.primaryphone mobile,crmContact.primaryemail email,");
        sql.append(" (CASE WHEN crmContact.contactType = ").append(EdsCrmContact.CRM_CONTACT).append(" THEN 'CONTACT' ELSE 'LEAD' END) as item_type");
        sql.append(" from \"").append(companyID).append("\".crmcontact crmContact ");
        sql.append(" where crmContact.contactType in(").append(EdsCrmContact.CRM_CONTACT).append(",").append(EdsCrmContact.LEAD_CONTACT).append(")").append(" AND crmContact.primaryemail IS NOT NULL and crmContact.primaryemail != ''");
        sql.append(" AND coalesce(lower(crmContact.firstname),'')||' '||coalesce(lower(crmContact.lastname),'')||' '||coalesce(lower(crmContact.primaryphone),'')||' '||coalesce(lower(crmContact.primaryemail),'')  like lower('%").append(fp.getSearchKey()).append("%')");
        sql.append(") t order by rank");

        //Find total count before paging
        Integer total = jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SearchPeopleTO.class)).size();

        sql.append(" offset ").append(fp.getStart()).append(" limit ").append(fp.getLimit());

        ArrayList<SearchPeopleTO> list = (ArrayList<SearchPeopleTO>) jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SearchPeopleTO.class));

        return new ListResult<>(list, total);
    }

    @Override
    @Transactional
    public void setDailyRateRate() {
        int companyID = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());

        //Find total count before paging
        String sql = "SELECT 1 FROM pg_catalog.pg_tables  WHERE schemaname = '" + companyID + "' and tablename  = 'balancedaterates'";
        Object tbcount = userManager.findNativeSingle(sql);
        if (tbcount == null) {
            sql = "CREATE TABLE \"" + companyID + "\".balancedaterates\n" +
                    "(  id serial,\n" +
                    "   currencyid integer,\n" +
                    "   balancedate date,\n" +
                    "   rate numeric(25,15),\n" +
                    "   CONSTRAINT balancedaterates_pkey PRIMARY KEY (id)) WITH (OIDS = FALSE);\n" +
                    "ALTER TABLE \""+companyID+"\".balancedaterates OWNER TO wfmtest;";
            userManager.updateNative(sql);
            return;
        }
        sql = " select date_value1,(select balancedate from \"" + companyID + "\".balancedaterates limit 1) " +
                " from \"" + companyID + "\".custom_form_item cfi " +
                " JOIN \"" + companyID + "\".customform_customfields cfcf on cfi.form_customfieldsid=cfcf.id " +
                " where cfi.form_id='BALANCEDATEEXCHRATES_FORM' order by cfcf.id desc limit 1 ";
        List<Object[]> balanceData = null;
        try {
            balanceData = userManager.findNative(sql);
        } catch (Exception e) {
            return;
        }
        if (balanceData != null && !balanceData.isEmpty()) {
            Date balanceDate = (Date) balanceData.get(0)[0];
            Date lastDate = (Date) balanceData.get(0)[1];
            StringBuilder query = new StringBuilder("delete from \"" + companyID + "\".balancedaterates;");
            if (lastDate != null && lastDate.equals(balanceDate)) {
                return;
            }
            query.append("insert into \"" + companyID + "\".balancedaterates (balancedate,currencyid,rate) values");
            CurrencyItem[] currencies = currencyService.getCurrencies();
            String delemite = "";
            for (CurrencyItem currency : currencies) {
                CurrencyListItem item = currencyService.getCurrencyRateByDate(currency.getId(), new DateNonConvertable(balanceDate));
                BigDecimal exchangeRate = BigDecimal.valueOf(item.getExchangeRate()).setScale(Utils.getAccountingCustomExRateScale(), RoundingMode.HALF_UP);
                query.append(delemite);
                query.append("('" + balanceDate + "'," + currency.getId() + "," + exchangeRate + ")");
                delemite = ",";
            }
            query.append(";");
            userManager.updateNative(query.toString());
        }
    }

    @Override
    public SelectItem[] dynamicLookUpResult(String queryName, String searchKey, Integer limit) {
        if (queryName == null || queryName.isEmpty())
            return new SelectItem[0];
        EdsDynamicQuery dynamicQuery = dynamicQueryManager.getQueryByName(queryName);
        if (dynamicQuery == null)
            return new SelectItem[0];
        if (limit == null)
            limit = 20;
        if (searchKey == null)
            searchKey = "";

        String query = dynamicQuery.getQuery_text().replace("anv", ServerSecurityContext.getInstance().getCompanyId());
        final List<Object[]> items = userManager.findNativeLimited(query, limit, searchKey);
        final List<SelectItem> selectItems = new ArrayList<>();
        for (final Object[] objects : items) {
            String nameField = "", valueField = "";
            if (objects.length > 1) {
                nameField = String.valueOf(objects[1]);
            }
            if (objects.length > 2) {
                valueField = String.valueOf(objects[2]);
            }
            final SelectItem selectItem = new SelectItem(Integer.valueOf(objects[0] + ""), nameField, valueField);
            selectItem.setSelectedId(Integer.valueOf(objects[0] + ""));
            selectItems.add(selectItem);
        }
        return selectItems.toArray(new SelectItem[0]);
    }

    @Transactional
    @Override
    public Boolean saveMailList(MailListRpc item, ReportRpc report) {
        EdsMailList edsMailList = new EdsMailList();
        EdsUser user = userManager.getUser();
        ArrayList<Integer> leadIds = new ArrayList<>();
        ColumnRpc column = new ColumnRpc();
        if ("CRM LEADS".equals(item.getMailListType())) {
            column.setName("ld.id");
        } else {
            column.setName("id");
        }
        column.setColumnFormat("integer");
        report.setShowMailingList(true);
        LinkedList<SelectItem> ids = getFilterSelectItems("", report, column);

        edsMailList.setCreationTime(new Date());
        edsMailList.setName(item.getName());
        edsMailList.setDescription(item.getDescription());
        edsMailList.setActive(item.isActive());
        baseEventPostProcessor.registerEvent(CrmMailingListEventListenerImpl.TYPE, (!mailListManager.createOrUpdate(edsMailList) ? BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT : BaseEventsPostProcessorImpl.EVENT_TYPE_ADD), edsMailList, user);

        try {
            EdsCrmEntityMailList crmEntityMailList;
            if (ids != null && ids.size() > 0)
                for (SelectItem id : ids) {
                    crmEntityMailList = new EdsCrmEntityMailList();
                    crmEntityMailList.setMailList(edsMailList);
                    Integer contactID = Integer.parseInt(id.getName());
                    crmEntityMailList.setEntity(crmContactManager.get(contactID));
                    crmEntityMailListManager.create(crmEntityMailList);
                    crmEntityMailListManager.flushAndClear();

                }
        } catch (Exception e) {
            log.error("******* COMPANY: " + user.getCompany().getObjectID() + " COULD NOT SAVE MAILLIST CONTACTS SUCCESSFULLY!!! Contacts amount: " + leadIds.size() + "||||| EXCEPTION IS: " + e.getMessage() + "******");
            return false;
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsReport.class.getSimpleName());
        if (report.getId() == null) {
            kpiLog.setEntityType(report.getViewName());
        } else {
            kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
        }
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(report.getId());
        ServerUtils.kpiLog(log, kpiLog, "Add Mailing List For Report");

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<RoleListItem> getCompanyRoles() {
        return allInOneService.getCompanyRoles();
    }

    @Override
    public ArrayList<RejectedImportRecord[]> importReportDataFromCSV(EdsAttachment attachment, ImportFile
            importFile, List<String[]> listOfRows) {
        boolean hasHeader = importFile.isHasHeader();
        ArrayList<RejectedImportRecord[]> rejectedRows = new ArrayList<>();
        for (String[] row : listOfRows) {
            if (!hasHeader) {
                boolean rowIsEmpty = true;
                for (String cellValue : row) {
                    if (StringUtils.isNotBlank(cellValue)) {
                        rowIsEmpty = false;
                        break;
                    }
                }
                if (rowIsEmpty) {
                    continue;
                }
                ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();
                int cellIndex = 0;
                for (String columnValue : row) {
                    CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                    SelectItem rowItem = importFile.getDynamicColumns()[cellIndex];
                    resultItem.setColumnCode(rowItem.getReferenceCode());
                    switch (rowItem.getSelectedId()) {
                        case 4, 5 -> {
                            resultItem.setDataType(Constants.DATA_TYPE_DATE);
                            Date dateColumn = getDateFromStringValue(columnValue, rowItem.getCode());
                            if (dateColumn != null) {
                                resultItem.setFieldDateNonConvertedValue(new DateNonConvertable(dateColumn));
                            }
                        }
                        default -> {
                            resultItem.setFieldStringValue(columnValue);
                            if (rowItem.getSelectedId().equals(2) || rowItem.getSelectedId().equals(3)) {
                                resultItem.setDataType(Constants.DATA_TYPE_NUMBER);
                            }
                        }
                    }
                    cellIndex++;
                    resultItemList.add(resultItem);
                }

                EdsReportDataCustomFields edsProjectCustomFields = createProjectCustomFields(resultItemList);

                attachment.getReportDataCustomFields().add(edsProjectCustomFields);

            } else {
                hasHeader = false;
                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];
                int cellIndex = 0;
                for (String cellValue : row) {
                    rejectedCells[cellIndex++] = new RejectedImportRecord(cellValue);
                }
                rejectedRows.add(rejectedCells);
            }

        }
//        createReportXmlTemplate(attachment, importFile);

        return rejectedRows;
    }

    private Date getDateFromStringValue(String columnValue, String columntype) {
        EdsCompanySettings companySettings = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())).getCompanySettings();
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat longdateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        if (companySettings.getShortDateFormat() != null) {
            shortDateFormat = new SimpleDateFormat(companySettings.getShortDateFormat());
        }
        if (companySettings.getLongDateFormat() != null) {
            longdateFormat = new SimpleDateFormat(companySettings.getLongDateFormat());
        }
        try {
            return (columntype.equals("long") ? longdateFormat : shortDateFormat).parse(columnValue);
        } catch (ParseException e3) {
            return null;
        }
    }

    @Override
    public ArrayList<RejectedImportRecord[]> importReportDataExcel(EdsAttachment attachment, ImportFile
            importFile, InputStream inputStream) {
        boolean hasHeader = importFile.isHasHeader();
        ArrayList<RejectedImportRecord[]> rejectedRows = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet)
                if (!hasHeader) {
                    ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();
                    int cellIndex = 0;
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                        SelectItem rowItem = importFile.getDynamicColumns()[cellIndex];
                        resultItem.setColumnCode(rowItem.getReferenceCode());
                        String columnValue = "";
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_BOOLEAN -> columnValue = String.valueOf(cell.getBooleanCellValue());
                            case Cell.CELL_TYPE_NUMERIC -> columnValue = String.valueOf(cell.getNumericCellValue());
                            case Cell.CELL_TYPE_STRING -> columnValue = cell.getStringCellValue();
                            default -> {
                                try {
                                    columnValue = cell.getStringCellValue();
                                } catch (Exception e) {
                                    try {
                                        columnValue = String.valueOf(cell.getNumericCellValue());
                                    } catch (Exception e1) {
                                        columnValue = String.valueOf(cell.getBooleanCellValue());
                                    }
                                }
                            }
                        }
                        switch (rowItem.getSelectedId()) {
                            case 4, 5 -> {
                                resultItem.setDataType(Constants.DATA_TYPE_DATE);
                                Date dateColumn = getDateFromStringValue(columnValue, rowItem.getCode());
                                if (dateColumn != null) {
                                    resultItem.setFieldDateNonConvertedValue(new DateNonConvertable(dateColumn));
                                }
                            }
                            default -> {
                                resultItem.setFieldStringValue(columnValue);
                                if (rowItem.getSelectedId().equals(2) || rowItem.getSelectedId().equals(3)) {
                                    resultItem.setDataType(Constants.DATA_TYPE_NUMBER);
                                }
                            }
                        }
                        cellIndex++;
                        resultItemList.add(resultItem);
                    }

                    EdsReportDataCustomFields edsProjectCustomFields = createProjectCustomFields(resultItemList);

                    attachment.getReportDataCustomFields().add(edsProjectCustomFields);

                } else {
                    hasHeader = false;
                    RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.getPhysicalNumberOfCells()];
                    int cellIndex = 0;
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        rejectedCells[cellIndex++] = new RejectedImportRecord(cell.getStringCellValue());
                    }
                    rejectedRows.add(rejectedCells);
                }

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

//        createReportXmlTemplate(attachment, importFile);

        return rejectedRows;
    }

    @Override
    @Transactional
    public Integer createReportXmlTemplate(ImportFile importFile) {
        EdsAttachment attachment = attachmentManager.get(importFile.getFileID());
        Integer companyId = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        int categoryId = importFile.getColumnID(ImportField.ReportDataImportFields.CATEGORY_ID);
        EdsReportTemplateCategory templateCategory = reportTemplateCategoryManager.get(categoryId);
        if (templateCategory == null) {
            templateCategory = reportTemplateCategoryManager.getReportTemplateCategory("Custom");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        Integer[] companyIDs = new Integer[1];
        companyIDs[0] = companyId;
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSelected(true);
        filterParameter.setName(attachment.getOriginalName() + " (" + dateFormat.format(new Date()) + ")");
        filterParameter.setDescription(createTemplateBody(attachment, importFile));
        if (templateCategory != null) {
            filterParameter.setCategoryID(templateCategory.getObjectID());
        }
        if (templateCategory != null) {
            filterParameter.setCategory(templateCategory.getCode());
        }
        filterParameter.setCompaines(companyIDs);
        filterParameter.setLibrary(false);
        filterParameter.setDeleted(true);
        filterParameter.setColumnsOfListing(new ArrayList<>());
        filterParameter.setIsSimpilifiedReportTemplate(false);
        filterParameter.setAttachmentId(attachment.getObjectID());

        ArrayList<String> checkedRoles = new ArrayList<>();
        checkedRoles.add("ADMIN");
        checkedRoles.add("DR");
        checkedRoles.add("SALESMAN");
        checkedRoles.add("ACCOUNTANT");
        filterParameter.setColumnsOfListing(checkedRoles);

        return saveOrUpdateReportTemplate(filterParameter);
    }

    private String createTemplateBody(EdsAttachment attachment, ImportFile importFile) {
        StringBuffer body = new StringBuffer();
        body.append("<report> \n");
        body.append("<view> \n");
        body.append("<title>").append(attachment.getOriginalName()).append("</title> \n");
        body.append("<name>").append(attachment.getOriginalName()).append("</name> \n");
        body.append("<category>").append(attachment.getOriginalName()).append("</category> \n");
        body.append("<sqlquery> \n");
        body.append("<select> \n");
        body.append("<table name=\"").append(attachment.getOriginalName()).append("\"> \n");
        for (SelectItem dynamicColumn : importFile.getDynamicColumns()) {
            if (!dynamicColumn.isSelected())
                continue;
            body.append("<column name=\"").append(dynamicColumn.getReferenceCode()).append("\" ");
            body.append(" title=\"").append(dynamicColumn.getName()).append("\" ");
            body.append(" type=\"").append(dynamicColumn.getCode().toLowerCase()).append("\" ");
            body.append(" formattype=\"").append(dynamicColumn.getCode().toLowerCase()).append("\" ");
            if (dynamicColumn.getParam() != null && !dynamicColumn.getParam().isEmpty()) {
                body.append(" customDateFormat=\"").append(dynamicColumn.getParam()).append("\" /> \n");
            } else {
                body.append(" /> \n ");
            }

        }
        body.append("</table>");
        body.append("</select>");
        body.append("<from> \n");
        body.append(" \"$\".reportdatacustomfields rdcf ");
        body.append(" join \"$\".attachment_fields atf  on rdcf.id=atf.customFieldid ");
        body.append("</from> \n");
        body.append("<where> \n");
        body.append("<terms id=\"base\" value=\"atf.attachmentId =" + attachment.getObjectID() + "\" />\n");
        body.append("</where>\n");
        body.append("</sqlquery>\n");
        body.append("</view>\n");
        body.append("</report>");

        return body.toString();
    }

    private EdsReportDataCustomFields createProjectCustomFields
            (ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsReportDataCustomFields edsReportDataCustomFields = new EdsReportDataCustomFields();
            reportDataCFManager.create(edsReportDataCustomFields);
            CustomFieldsUtils.setDomenObjectCustomFields(edsReportDataCustomFields, customFieldItems);
            return edsReportDataCustomFields;
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedList<SelectItem> getUserReportList(Integer reportid, Boolean islibrary) {

        Integer categoryid = reportingManager.getCategoryByReport(reportid, getUser().getCompanyId());
        LinkedList<SelectItem> reportRpcs = new LinkedList<>();
        UserSecuritryRpc user = getUser();
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setSubscriptionTypeName(user.getDomainName());
        filter.setCompanyID(user.getCompanyId());
        filter.setUserID(user.getUserId());
        filter.setRoles(userManager.getUser().getRolesCodeAsString());
        List<Object[]> list = reportingManager.listObject(filter);
        for (Object[] item : list) {
            ReportItem reportItem = new ReportItem(item).invoke();
            SelectItem reportRpc = new SelectItem(reportItem.getReportId(), reportItem.getReportName(), reportItem.getTemplateCode());
            reportRpc.setParam("" + reportItem.getXmlTemplateId());

            if (islibrary && reportItem.getIsLibrary()) {
                if (categoryid.equals(reportItem.getCategoryId())) {
                    reportRpc.setSelected(true);//setLibrary
                    reportRpcs.add(reportRpc);
                }
            } else if (!islibrary && !reportItem.getIsLibrary()) {
                reportRpc.setSelected(false);//setLibrary
                reportRpcs.add(reportRpc);
            }
        }

        reportRpcs.sort(Comparator.comparing(SelectItem::getName));
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityId(null);
        kpiLog.setEntityName(EdsReport.class.getSimpleName());
        kpiLog.setEntityType(null);
        ServerUtils.kpiLog(log, kpiLog, "Get User Role List");

        return reportRpcs;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<TeamEmployees> getReportingDBUrlCompanies(Integer objectID) {
        ArrayList<TeamEmployees> result;
        WfmTreeItem team = new WfmTreeItem(null, "Companies");
        LinkedList<WfmTreeItem> companies = new LinkedList<>();
        EdsReportingDBUrl reportingDBUrl = objectID != null ? reportingDBUrlManager.get(objectID) : null;
        result = new ArrayList<>();
        List<EdsCompany> companyList = companyManager.getOccupiedCompanies();
        if (companyList != null) {
            if (reportingDBUrl == null || reportingDBUrl.getCompanies() == null || reportingDBUrl.getCompanies().size() == 0) {
                for (EdsCompany company : companyList) {
                    companies.add(new WfmTreeItem(company.getObjectID(), company.getName(), true));
                }
            } else {
                for (EdsCompany company : companyList) {
                    boolean isChecked = reportingDBUrl.getCompanies().contains(company);
                    companies.add(new WfmTreeItem(company.getObjectID(), company.getName(), isChecked));
                }
            }
            result.add(new TeamEmployees(team, companies));
        }
        return result;
    }

    @Transactional
    @Override
    public void saveReportTemplate(ReportingListItem rowValue) {
        EdsReportTemplate edsReportTemplate = reportTemplateManager.get(rowValue.getTemplateId());
        edsReportTemplate.setOrder_number(rowValue.getOrder());
        reportTemplateManager.update(edsReportTemplate);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
        kpiLog.setEntityName(edsReportTemplate.getName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(edsReportTemplate.getObjectID());
        ServerUtils.kpiLog(log, kpiLog, "Updated report template");
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ListResult<ReportingDBUrlListItem> getReportDBUrlList(ListingFilterParameter filterParameter) {
        Integer totalCount = reportingDBUrlManager.listCount();
        ArrayList<ReportingDBUrlListItem> items = new ArrayList<>();
        for (EdsReportingDBUrl reportingDBUrl : reportingDBUrlManager.list(filterParameter)) {
            ReportingDBUrlListItem item = new ReportingDBUrlListItem();
            item.setId(reportingDBUrl.getObjectID());
            item.setDbUrl(reportingDBUrl.getDbUrl());
            item.setUserName(reportingDBUrl.getUserName());
            item.setPassword(reportingDBUrl.getPassword());
            items.add(item);
        }
        return new ListResult<>(items, totalCount);
    }

    @Transactional
    @Override
    public void exportSavedReport(Integer schema, ArrayList<EdsReport> reports,
                                  HashMap<Integer, EdsChartConfig> chartHashMap,
                                  HashMap<Integer, EdsUpload> uploadHashMap,
                                  HashMap<Integer, EdsUploadSettings> uploadSettingsHashMap,
                                  HashMap<Integer, EdsKpiWidget> kpiWidgetMap, boolean withPermission) {
        try {
            ServerSecurityContext.getInstance().setCompanyId(schema);
            for (EdsReport edsReport : reports) {
                Integer uploadID = edsReport.getExcelTemplateId();
                Integer chartConfigId = edsReport.getTemp();
                EdsChartConfig edsChartConfig = null;
                EdsReport report = reportingManager.getByCode(edsReport.getCode());
                if (report != null) {
                    Hibernate.initialize(report.getChartConfig());
                    Hibernate.initialize(report.getKpiWidget());
                }
                if (chartConfigId != null) {
                    if (chartHashMap != null && chartHashMap.get(chartConfigId) != null) {
                        if (report == null || report.getChartConfig() == null) {
                            edsChartConfig = chartHashMap.get(chartConfigId).getNew(null);
                            chartConfigManager.persist(edsChartConfig);
                            reportingManager.flushAndClear();
                        } else {
                            edsChartConfig = chartHashMap.get(chartConfigId).getNew(report.getChartConfig());
                        }
                    } else if (edsReport.getChartConfig() != null) {
                        if (report == null || report.getChartConfig() == null) {
                            edsChartConfig = edsReport.getChartConfig().getNew(null);
                            chartConfigManager.persist(edsChartConfig);
                            reportingManager.flushAndClear();
                        } else {
                            edsChartConfig = edsReport.getChartConfig().getNew(report.getChartConfig());
                        }
                    }
                }

                Integer kpiWidgetId = edsReport.getTempWidgetId();
                EdsKpiWidget kpiWidget = null;
                EdsKpiWidgetFilter kpiWidgetFilter;
                if (kpiWidgetId != null) {
                    if (kpiWidgetMap != null && kpiWidgetMap.get(kpiWidgetId) != null) {
                        if (report == null || report.getKpiWidget() == null) {
                            kpiWidget = kpiWidgetMap.get(kpiWidgetId).getNew(null);
                            kpiWidgetManager.persist(kpiWidget);
                            List<EdsKpiWidgetFilter> widgetFilters = kpiWidgetMap.get(kpiWidgetId).getWidgetFilterList();
                            if (widgetFilters != null && widgetFilters.size() > 0) {
                                for (EdsKpiWidgetFilter edsKpiWidgetFilter : widgetFilters) {
                                    kpiWidgetFilter = edsKpiWidgetFilter.getNew(null);
                                    kpiWidgetFilter.setKpiWidgetId(kpiWidget);
                                    kpiWidgetManager.persist(kpiWidgetFilter);
                                }
                            }
                        } else {
                            kpiWidget = kpiWidgetMap.get(kpiWidgetId).getNew(report.getKpiWidget());
                        }
                    } else if (edsReport.getKpiWidget() != null) {
                        if (report == null || report.getKpiWidget() == null) {
                            kpiWidget = edsReport.getKpiWidget().getNew(null);
                            kpiWidgetManager.persist(kpiWidget);
                            List<EdsKpiWidgetFilter> widgetFilters = kpiWidgetMap.get(kpiWidgetId).getWidgetFilterList();
                            if (widgetFilters != null && widgetFilters.size() > 0) {
                                for (EdsKpiWidgetFilter edsKpiWidgetFilter : widgetFilters) {
                                    kpiWidgetFilter = edsKpiWidgetFilter.getNew(null);
                                    kpiWidgetFilter.setKpiWidgetId(kpiWidget);
                                    kpiWidgetManager.persist(kpiWidgetFilter);
                                }
                            }
                        } else {
                            kpiWidget = edsReport.getKpiWidget().getNew(report.getKpiWidget());
                        }
                    }
                }

                EdsUpload edsUpload = null;
                try {
                    if (uploadID != null && uploadHashMap.get(uploadID) != null && uploadSettingsHashMap.get(uploadID) != null) {
                        EdsReference ref = referenceManager.findReference(_UPLOAD_TYPE, EdsContextParams.getUploadType());

                        if (report == null || report.getExcelTemplateId() == null || uploadManager.get(report.getExcelTemplateId()) == null) {
                            edsUpload = uploadHashMap.get(uploadID).cloneShallow();
                            edsUpload.setType(ref);
                            uploadManager.createBlank(edsUpload);

                            EdsUploadSettings edsUploadSettings = uploadSettingsHashMap.get(uploadID).cloneShallow();
                            edsUploadSettings.setUpload(edsUpload);
                            uploadManager.persistFile(edsUploadSettings);
                        } else {
                            edsUpload = (EdsUpload) uploadManager.get(report.getExcelTemplateId());
                            EdsUpload upload = uploadHashMap.get(uploadID);
                            edsUpload.setContentType(upload.getContentType());
                            edsUpload.setDriveFolderId(upload.getDriveFolderId());
                            edsUpload.setOriginalName(upload.getOriginalName());
                            edsUpload.setFileType(upload.getFileType());
                            edsUpload.setDriveFolderName(upload.getDriveFolderName());
                            edsUpload.setImageSize(upload.getImageSize());
                            edsUpload.setLocalPath(upload.getLocalPath());
                            edsUpload.setOriginalName(upload.getOriginalName());
                            edsUpload.setType(ref);
                            edsUpload.setImageSize(upload.getImageSize());

                            EdsUploadSettings edsUploadSettings = uploadManager.getUploadSettings(edsUpload);
                            EdsUploadAmazonSettings uploadAmazonSettings = uploadSettingsHashMap.get(uploadID).cloneShallow();
                            edsUploadSettings.setFileType(uploadAmazonSettings.getFileType());
                            edsUploadSettings.setAccessKey(uploadAmazonSettings.getAccessKey());
                            edsUploadSettings.setExpireDate(uploadAmazonSettings.getExpireDate());
                            edsUploadSettings.setFileLink(uploadAmazonSettings.getFileLink());
                            uploadManager.persistFile(edsUploadSettings);
                        }

                    }
                } catch (Exception e) {
                    e = e;
                }
                report = edsReport.getNew(report);
                EdsFolders folder;
                if (edsReport.getFolder() == null) {
                    folder = foldersManager.getSystemFolder();
                } else if (edsReport.getFolder().getObjectID() == null) {
                    EdsFolders edsFolders = foldersManager.getByName(edsReport.getFolder().getName());
                    if (edsFolders != null) {
                        folder = edsReport.getFolder().getNew(edsFolders);
                    } else {
                        folder = edsReport.getFolder().getNew(new EdsFolders());
                    }
                    if (folder.getCategoryCode() != null) {
                        folder.setCategoryCode(folder.getCategoryCode());
                    }
                } else {
                    folder = edsReport.getFolder().getNew(foldersManager.getByName(edsReport.getFolder().getName()));
                }
                foldersManager.createOrUpdate(folder);
                report.setFolder(folder);
                report.setExcelTemplateId(edsUpload == null ? null : edsUpload.getObjectID());
                report.setChartConfig(edsChartConfig);
                report.setKpiWidget(kpiWidget);
                if (report.getObjectID() == null) {
                    report.getAuditInfo().setCreationDate(new Date());
                    report.getAuditInfo().setModificationDate(new Date());
                    try {
                        reportingManager.persist(report);
                    } catch (Exception e) {
                        System.out.println("Report = " + report.getName() + " yedi");
                        throw e;
                    }
                } else {
                    if (report.getAuditInfo().getCreationDate() == null) {
                        report.getAuditInfo().setModificationDate(new Date());
                    }
                    report.getAuditInfo().setModificationDate(new Date());
                    reportingManager.update(report);
                }
                if (Boolean.TRUE.equals(!withPermission)) {
                    saveReportPermission(report);
                }
                reportingManager.flushAndClear();
                Thread.yield();
            }
        } catch (Exception e) {
            logError(e, schema, 0, 0, "N/A", null, "*************************************Export Saved Report Yedi=*************************************");
        }
        reportingManager.flushAndClear();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ReportingDBUrlListItem getReportingDBUrl(Integer objectID) {
        if (objectID == null) {
            return null;
        }
        EdsReportingDBUrl edsReportingDBUrl = reportingDBUrlManager.get(objectID);
        ReportingDBUrlListItem item = null;
        if (edsReportingDBUrl != null) {
            item = edsReportingDBUrl.getRPC();
        }
        return item;
    }

    @Transactional
    @Override
    public void saveReportingDBUrl(ReportingDBUrlListItem item) {
        EdsReportingDBUrl edsReportingDBUrl;
        edsReportingDBUrl = item.getId() == null ? new EdsReportingDBUrl() : reportingDBUrlManager.get(item.getId());
        edsReportingDBUrl.setDbUrl(item.getDbUrl());
        edsReportingDBUrl.setUserName(item.getUserName());
        edsReportingDBUrl.setPassword(item.getPassword());
        edsReportingDBUrl.setEncrypt(true);
        edsReportingDBUrl.getCompanies().clear();

        List<EdsCompany> companyList = companyManager.getOccupiedCompanies();
        if (!(item.getCompany() == null || companyList == null || item.getCompany().size() == 0)) {
            if (item.getCompany().size() != companyList.size()) {
                StringBuilder compaiStringBuffer = new StringBuilder();
                for (SelectItem company : item.getCompany()) {
                    compaiStringBuffer.append(",").append(company.getId());
                }
                compaiStringBuffer.delete(0, 1);
                edsReportingDBUrl.getCompanies().addAll(companyManager.getCompaniesByIDs(compaiStringBuffer.toString()));
            }
        }
        reportingDBUrlManager.createOrUpdate(edsReportingDBUrl);
    }

    @Transactional
    @Override
    public void deleteReportingDBUrl(Integer id) {
        EdsReportingDBUrl edsReportingDBUrl = reportingDBUrlManager.get(id);
        edsReportingDBUrl.getCompanies().clear();
        reportingDBUrlManager.update(edsReportingDBUrl);
        reportingDBUrlManager.delete(reportingDBUrlManager.get(id));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ListResult<SelectListRpc> getCompanyReportList(ListingFilterParameter filterParameter) {
        if (filterParameter.getCompanyID() == null || filterParameter.getCompanyID() < 0) {
            return null;
        }
        ServerSecurityContext.getInstance().setCompanyId(filterParameter.getCompanyID());
        Integer totalCount = reportingManager.getReportListCount(filterParameter);
        ArrayList<SelectListRpc> items = new ArrayList<>();
        for (Object[] obj : reportingManager.getReportList(filterParameter)) {
            EdsReport edsReport = (EdsReport) obj[0];
            EdsReportTemplate edsReportTemplate = (EdsReportTemplate) obj[1];
            SelectListRpc item = edsReport.toSelectListRpc();
            if (edsReportTemplate != null) {
                item.setDescription(edsReportTemplate.getName());
                if (edsReportTemplate.getLibrary()) {
                    item.setLibrary(edsReportTemplate.getLibrary());
                }
            }
            items.add(item);
        }
        return new ListResult<>(items, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public String getSavedReportInsertQuery(ListingFilterParameter filterParameter) {
        Integer companyID = filterParameter.getCompanyID();
        Integer[] objectIDs = filterParameter.getCategories();

        ServerSecurityContext.getInstance().setCompanyId(companyID);
        String columns = EdsReport.wrapper();
        StringBuilder stringBuilder = new StringBuilder();

        for (Integer objectID : objectIDs) {
            String chartid = getValue("select chartid from \"" + companyID + "\".reporting where id=" + objectID);
            String rowString = "select " + columns + " from \"" + companyID + "\".reporting where id=" + objectID;

            ArrayList<String[]> data = getDataTable(rowString);
            if (data == null || data.size() < 1) {
                return null;
            }
            if (!(null == chartid || "".equals(chartid))) {
                stringBuilder.append("insert into \"anv\".reporting(").append(columns).append(",folderid,chartid) select ");
            } else {
                stringBuilder.append("insert into \"anv\".reporting(").append(columns).append(",folderid) select ");
            }
            for (String cell : data.get(0)) {
                String temp = cell == null ? "null" : "E'" + cell.replace("'", "''") + "'";
                stringBuilder.append(temp).append(",");
            }
            if (!(null == chartid || "".equals(chartid))) {
                stringBuilder.append("f.id,(select max(id) from \"anv\".chart) from \"anv\".folders f where f.name='").append(FolderType.System.name()).append("' limit 1;");
            } else {
                stringBuilder.append("id from \"anv\".folders where name='").append(FolderType.System.name()).append("' limit 1;");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    @Transactional
    public String updateSavedReport(ListingFilterParameter filterParametrs) {
        try {
            if (filterParametrs == null || filterParametrs.getCompanyID() == null || filterParametrs.getObjectId() == null || filterParametrs.getParams() == null || "".equals(filterParametrs.getParams())) {
                return "Error";
            }
            String updateCommand = reportingManager.getGenerateUpdateCommand(filterParametrs);
            if (updateCommand != null) {
                executeNative(updateCommand);
            }
            return "Success";
        } catch (Exception exp) {
            return getLog(exp).toString();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public String getSavedReportUpdateCommand(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null || filterParametrs.getCompanyID() == null || filterParametrs.getObjectId() == null) {
            return null;
        }
        String columns = EdsReport.wrapper();
        StringBuilder stringBuilder = new StringBuilder();
        String rowString = "select " + columns + " from \"" + filterParametrs.getCompanyID() + "\".reporting where id=" + filterParametrs.getObjectId();
        ArrayList<String[]> data = getDataTable(rowString);
        if (data == null || data.size() < 1) {
            return null;
        }
        String[] cells = data.get(0);
        String[] columnList = columns.split(",");
        for (int i = 0; i < columnList.length; i++) {

            String temp = cells[i] == null ? "null" : "'" + cells[i].replace("'", "''") + "'";
            stringBuilder.append("  ").append(columnList[i]).append("=").append(temp).append(",");
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return stringBuilder.toString();
    }

    @Transactional
    @Override
    public void deleteReportsByCompany(ListingFilterParameter filterParametrs) {
        ServerSecurityContext.getInstance().setCompanyId(filterParametrs.getCompanyID());
        for (Integer objectID : filterParametrs.getCategories()) {
            deleteReport(objectID);
        }
    }

    /**
     * Report RUN method
     *
     * @param report
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ReportRpc getReportStructure(ReportRpc report, Integer userId) {
        Integer companyId = report.getCompanyId();
        if (companyId == null && ServerSecurityContext.getInstance() != null && ServerSecurityContext.getInstance().getCompanyId() != null && !ServerSecurityContext.getInstance().getCompanyId().isEmpty()) {
            companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
            report.setCompanyId(companyId);
        }
        if (userId == null) {
            UserSecuritryRpc userSecuritryRpc = getUser();
            if (userSecuritryRpc != null) {
                userId = userSecuritryRpc.getUserId();
            } else {
                userId = 0;
            }
        }
        report.setUserID(userId);
        ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());

        Boolean isClonable = report.getClonable();
        if (report.getRunFromFirstStep()) {
            if (report.getId() == null) {
                LinkedList<ColumnRpc> columns = new LinkedList<>();
                if (report.getSelectedColumns().size() == 0) {
                    for (int j = 0; j < viewRpc.getTables().size(); j++) {
                        columns.addAll(viewRpc.getTables().get(j).getColumns());
                    }
                    report.setSelectedColumns(columns);
                }
            } else {
                //??????? ????????? ?????? ?
                report = reportingManager.get(report.getId()).toRPC();
                Boolean isLibrary = reportTemplateManager.getByCode(report.getViewCode()).getLibrary();
                report.setLibrary(isLibrary);
                report.setFolderType(getFolderTypeByReportId(report.getId()));
            }
        }
        //Default Filter Column
        report.setFilterColumn(viewRpc.getFilterColumn());
        report.setFilterOperation(viewRpc.getFilterOperation());
        report.setFilterValue(viewRpc.getFilterValue());

        report.setClonable(isClonable);
        report.setUserID(userId);
        report.setCompanyId(companyId);
        if (report.getBrowserTimeZone() == null && userId != null) {
            EdsUser user = userManager.get(userId);
            if (user != null && user.getTimezone() != null && !"".equals(user.getTimezone())) {
                report.setBrowserTimeZone(user.getTimezone());
            }
        }
        EdsReport edsReport = null;
        if (report.getId() != null) {
            edsReport = reportingManager.get(report.getId());
        } else if (report.getCode() != null && !report.getCode().isEmpty()) {
            edsReport = reportingManager.getByCode(report.getCode());
        }
        if (userId != null && edsReport != null &&
                (edsReport.getAuditInfo().getCreatedBy() != null && getUser().getUserId().equals(edsReport.getAuditInfo().getCreatedBy().getObjectID())
                        || edsReport.getFolder() != null && getUser().getUserId().equals(edsReport.getFolder().getObjectID()))
        ) {
            report.setOwner(true);
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsReportTemplate.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        if (report.getId() == null) {
            kpiLog.setEntityId(report.getXmlTemplateId());
            kpiLog.setEntityType(report.getViewName());
        } else {
            kpiLog.setEntityId(report.getId());
            kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
        }
        ServerUtils.kpiLog(log, kpiLog, "Get Report Structure");

        return report;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getReportTemplates(ListingFilterParameter filterParams) {
        ArrayList<EdsReportTemplate> edsReportTemplates = reportTemplateManager.getReportTemplateList(null);
        SelectItem[] items = new SelectItem[edsReportTemplates.size()];
        int i = 0;
        for (EdsReportTemplate edsReportTemplate : edsReportTemplates) {
            items[i++] = new SelectItem(edsReportTemplate.getObjectID(), edsReportTemplate.getName());
        }
        return items;
    }

    @Override
    @Transactional
    public SavedReportTemplate savedReportChange(SavedReportTemplate item) {
        item.setViewCode(reportTemplateManager.get(item.getObjectID()).getCode());
        if (item.getCompanies() == null || item.getCompanies().length == 0 || "".equals(item.getCompanies()[0])) {
            item.setCompanies(companyManager.getExistingSchemas().toArray(new String[]{}));
        }
        for (String companyID : item.getCompanies()) {
            try {
                if (!(companyID == null || "".equals(companyID))) {
                    item.setCompanyID(companyID);
                    item.setResponse(item.getResponse() + "<br/>" + reportingManager.changeColumnNamePatch(item));
                }
            } catch (Exception exp) {
                logError(exp, Integer.valueOf(companyID), null, null, null, " NONE ", "Change Report Template in Backend");
            }
        }
        return item;
    }

    @Override
    public boolean isComplate(String typeString, Integer entityID) {
        if (entityID == null)
            return false;
        if ("PROJECT".equals(typeString)) {
            EdsReference completeProject = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.COMPLETED);
            return completeProject.equals(projectManager.get(entityID).getStatus());
        } else if ("TASK".equals(typeString)) {
            EdsReference completeTask = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED);
            return completeTask.equals(taskManager.get(entityID).getStatus());
        } else if ("WORKSTREAM".equals(typeString)) {
            EdsReference completeProject = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.COMPLETED);
            return completeProject.equals(workStreamManager.get(entityID).getProject().getStatus());
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ResultSet getSummaryReportResult(ReportRpc report, Integer userId) {
        return getSummaryReportResult(report, userId, false);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ResultSet getSummaryReportResult(ReportRpc report, Integer userId, boolean isForExport) {
        if (report.getGroupColumns().isEmpty()) {
            return getTabularReportResult(report, userId, isForExport);
        }
        boolean fromSqlServer = false;
        Integer companyId = report.getCompanyId();
        if (companyId == null && ServerSecurityContext.getInstance() != null && ServerSecurityContext.getInstance().getCompanyId() != null && !ServerSecurityContext.getInstance().getCompanyId().isEmpty()) {
            companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
            report.setCompanyId(companyId);
        }
        if (userId == null) {
            UserSecuritryRpc userSecuritryRpc = getUser();
            if (userSecuritryRpc != null) {
                userId = userSecuritryRpc.getUserId();
            } else {
                userId = 0;
            }
        }
        report.setUserID(userId);
        if (report.getBrowserTimeZone() == null) {
            report.setBrowserTimeZone(getUser().getTimezone());
        }

        if (report.getId() != null && report.getViewName() == null) {
            String browserTimeZone = report.getBrowserTimeZone();
            report = getReport(report.getId(), report.getXmlTemplateId());
            if (browserTimeZone != null && !"".equals(browserTimeZone)) {
                report.setBrowserTimeZone(browserTimeZone);
            }
            report.setFolderType(getFolderTypeByReportId(report.getId()));
        }
        if (report.getBrowserTimeZone() == null && userId != null) {
            EdsUser user = userManager.get(userId);
            if (user != null && user.getTimezone() != null && !"".equals(user.getTimezone())) {
                report.setBrowserTimeZone(user.getTimezone());
            }
        }
        if (report.getViewCode() != null) {
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
            report.setFromKpi(viewRpc.isFromKpi());
        }
        Connection conn = null;
        ResultSet resultSet = null;
        String sqlQuery = "";

        try {
            conn = getDataSourceConnection(report);
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
            setCustomReplacementItems(viewRpc);
            report.setNoTimeZone(viewRpc.isNoTimezone());
            fromSqlServer = customDbUrl != null && customDbUrl.contains(SQLSERVER);
            fixErrors(report);
            report.setSqlServer(fromSqlServer);
            sqlQuery = SqlQueryUtil.getSummaryReportQuery(companyId, report, null, isForExport);
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (fromSqlServer) {
                sqlQuery = getNormalizedQueryForSqlServer(sqlQuery);
            } else {
                preparedStatement.setPoolable(true);
            }

            if (!report.getValues().isEmpty()) {
                int reSetCount = viewRpc.getQueries().split("\\{where2}").length;
                setParametersToStatement(reSetCount, 1, report, preparedStatement);
            }

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            logError(e, companyId, userId, report.getId(), report.getName(), report.getViewName(), "******************Connection refused  to xls/csv***************");
            System.out.println(sqlQuery);
        }


        if (report.getId() != null || report.getXmlTemplateId() != null) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsReport.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            if (report.getId() != null) {
                kpiLog.setEntityId(report.getId());
                kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
                ServerUtils.kpiLog(log, kpiLog, "Generated Summary Report");
            } else {
                kpiLog.setEntityId(report.getXmlTemplateId());
                kpiLog.setEntityType(report.getViewName());
                ServerUtils.kpiLog(log, kpiLog, "Generated Summary Report Template");
            }
        }
        return resultSet;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ResultSet getTabularReportResult(ReportRpc report, Integer userId) {
        return getTabularReportResult(report, userId, false);
    }

    @Override
    public ResultSet getTabularReportResult(ReportRpc report, Integer userId, boolean isForExport) {
        return getTabularReportResult(report, userId, isForExport, null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ResultSet getTabularReportResult(ReportRpc report, Integer userId, boolean isForExport, DynamicDto customReplacements) {
        boolean fromSqlServer = false;
        Integer companyId = report.getCompanyId();
        if (companyId == null && ServerSecurityContext.getInstance() != null && ServerSecurityContext.getInstance().getCompanyId() != null && !ServerSecurityContext.getInstance().getCompanyId().isEmpty()) {
            companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
            report.setCompanyId(companyId);
        }
        if (userId == null) {
            UserSecuritryRpc userSecuritryRpc = getUser();
            if (userSecuritryRpc != null) {
                userId = userSecuritryRpc.getUserId();
            } else {
                userId = 0;
            }
        }
        report.setUserID(userId);
        if (report.getBrowserTimeZone() == null || report.getBrowserTimeZone().isEmpty()) {
            report.setBrowserTimeZone(getUser().getTimezone());
        }

        if (report.getId() != null && report.getViewName() == null) {
            String browserTimeZone = report.getBrowserTimeZone();
            report = getReport(report.getId(), report.getXmlTemplateId());
            if (browserTimeZone != null && !"".equals(browserTimeZone)) {
                report.setBrowserTimeZone(browserTimeZone);
            }
            report.setFolderType(getFolderTypeByReportId(report.getId()));
        }
        if ((report.getBrowserTimeZone() == null || report.getBrowserTimeZone().isEmpty()) && userId != null) {
            EdsUser user = userManager.get(userId);
            if (user != null && user.getTimezone() != null && !"".equals(user.getTimezone())) {
                report.setBrowserTimeZone(user.getTimezone());
            }
        }

        Connection conn = null;
        ResultSet resultSet = null;
        String sqlQuery = "";
        try {
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());

            setCustomReplacementItems(viewRpc);
            if (customReplacements != null && !customReplacements.getProperties().isEmpty()) {
                if (viewRpc.getCustomReplacements() == null)
                    viewRpc.setCustomReplacements(new HashMap<>());
                Map<String, String> customMap = new HashMap<>();
                customReplacements.getProperties().keySet().forEach(k -> customMap.put(k, (String) customReplacements.getProperties().get(k)));
                viewRpc.getCustomReplacements().putAll(customMap);
            }

            report.setFromKpi(viewRpc.isFromKpi());
            conn = getDataSourceConnection(report);
            report.setNoTimeZone(viewRpc.isNoTimezone());
            fromSqlServer = customDbUrl != null && customDbUrl.contains(SQLSERVER);
            fixErrors(report);
            report.setSqlServer(fromSqlServer);
            sqlQuery = SqlQueryUtil.getTabularReportQuery(companyId, report, viewRpc, isForExport);
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (fromSqlServer) {
                sqlQuery = getNormalizedQueryForSqlServer(sqlQuery);
            } else {
                preparedStatement.setPoolable(true);
            }

            if (!report.getValues().isEmpty()) {
                int reSetCount = viewRpc.getQueries().split("\\{where2}").length;
                setParametersToStatement(reSetCount, 1, report, preparedStatement);
            }

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            logError(e, companyId, userId, report.getId(), report.getName(), report.getViewName(), "******************Connection refused  to xls/csv***************");
            System.out.println(sqlQuery);
        }

        if (report.getId() != null || report.getXmlTemplateId() != null) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsReport.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            if (report.getId() != null) {
                kpiLog.setEntityId(report.getId());
                kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
                ServerUtils.kpiLog(log, kpiLog, "Generated Tabular Report");
            } else {
                kpiLog.setEntityId(report.getXmlTemplateId());
                kpiLog.setEntityType(report.getViewName());
                ServerUtils.kpiLog(log, kpiLog, "Generated Tabular Report Template");
            }
        }
        return resultSet;
    }

    private void setCustomReplacementItems(ViewRpc viewRpc) {
        SelectItem[] replacementElements = dynamicLookUpResult("select_replacement_items", "1", 1);
        if (replacementElements != null) {
            for (SelectItem replacementElement : replacementElements) {
                if (viewRpc.getCustomReplacements() == null)
                    viewRpc.setCustomReplacements(new HashMap<>());
                viewRpc.getCustomReplacements().put(replacementElement.getName(), replacementElement.getDescription());
            }
        }
    }

    private String getNormalizedQueryForSqlServer(String sqlQuery) {
        sqlQuery = sqlQuery.replace("to_char", "format");
        sqlQuery = sqlQuery.replace("::timestamp", "");
        sqlQuery = sqlQuery.replace("::TIMESTAMP", "");
        sqlQuery = sqlQuery.replace("::varchar", "");
        sqlQuery = sqlQuery.replace("999,999,999,990.00", "##################");
        sqlQuery = sqlQuery.replace("999999999990.00", "###############");
        sqlQuery = sqlQuery.replace("99999999999.0", "############");
        sqlQuery = sqlQuery.replace("999,999,999,999", "############");
        sqlQuery = sqlQuery.replace("999,999,999", "############");
        sqlQuery = sqlQuery.replace("trim", "rtrim");
        sqlQuery = sqlQuery.replace("Mon DD YYYY HH24:MI", "d");
        sqlQuery = sqlQuery.replace("Mon DD YYYY", "d");
        if (sqlQuery.contains("BETWEEN") && sqlQuery.contains(" + interval '1 days - 1 ms'")) {
            sqlQuery = sqlQuery.replace(" + interval '1 days - 1 ms'", "");
        }
        sqlQuery = sqlQuery.replace("currentmonthfirstday()", "dateadd(m, datediff(m, 1, getDate()), 0)");
        sqlQuery = sqlQuery.replace("currentmonthlastday() + interval '1 day - 1 ms'", "dateadd(s,-1,dateadd(mm, datediff(m,0,getDate())+1,0))");
        sqlQuery = sqlQuery.replace("currentmonthlastday()", "dateadd(s,-1,dateadd(mm, datediff(m,0,getDate())+1,0))");
        sqlQuery = sqlQuery.replace("currentweekfirstday() - interval '1 week'", "dateadd(week, datediff(week, -1, getDate())-1, -1)");
        sqlQuery = sqlQuery.replace("currentweekfirstday() - interval '2 week'", "dateadd(week, datediff(week, -1, getDate())-2, -1)");
        sqlQuery = sqlQuery.replace("currentweekfirstday()", "dateadd(week, datediff(week, -1, getDate()), -1)");
        sqlQuery = sqlQuery.replace("currenttimstamp() - interval '3 hour'", "dateadd(hh, -3, getDate())");
        sqlQuery = sqlQuery.replace("currenttimstamp() - interval '2 hour'", "dateadd(hh, -2, getDate())");
        sqlQuery = sqlQuery.replace("currenttimstamp() - interval '1 hour'", "dateadd(hh, -1, getDate())");

        sqlQuery = sqlQuery.replace("currenttimstamp()", "getDate()");

        sqlQuery = sqlQuery.replace("currentdate() + interval '1 day - 1 ms' + interval '1 day'", "dateadd(ms, -3, dateadd(hh,0, datediff(dd,0, dateadd(dd, 2, getDate()))))");
        sqlQuery = sqlQuery.replace("currentdate() + interval '1 day - 1 ms' - interval '1 day'", "dateadd(ms, -3, dateadd(hh,0, datediff(dd,0, dateadd(dd, 0, getDate()))))");
        sqlQuery = sqlQuery.replace("currentdate() + interval '1 day - 1 ms'", "dateadd(ms, -3, dateadd(hh,0, datediff(dd,0, dateadd(dd, 1, getDate()))))");
        sqlQuery = sqlQuery.replace("currentdate() + interval '1 day'", "dateadd(hh,0, datediff(dd,0, dateadd(dd, 1, getDate())))");
        sqlQuery = sqlQuery.replace("currentdate() - interval '1 day'", "dateadd(hh,0, datediff(dd,0, dateadd(dd, -1, getDate())))");
        sqlQuery = sqlQuery.replace("currentdate() - interval '7 days'", "dateadd(hh,0, datediff(dd,0, dateadd(dd, -7, getDate())))");
        sqlQuery = sqlQuery.replace("currentdate() - interval '30 days'", "dateadd(hh,0, datediff(dd,0, dateadd(dd, -30, getDate())))");
        sqlQuery = sqlQuery.replace("currentdate() - interval '60 days'", "dateadd(hh,0, datediff(dd,0, dateadd(dd, -60, getDate())))");
        sqlQuery = sqlQuery.replace("currentdate() - interval '90 days'", "dateadd(hh,0, datediff(dd,0, dateadd(dd, -90, getDate())))");
        sqlQuery = sqlQuery.replace("currentdate() - interval '120 days'", "dateadd(hh,0, datediff(dd,0, dateadd(dd, -120, getDate())))");

        sqlQuery = sqlQuery.replace("currentdate()", "dateadd(hh,0, datediff(dd,0, dateadd(dd, 0, getDate())))");
        sqlQuery = sqlQuery.replace("date(enddate)", "convert(varchar, cast(enddate as date))");

        return sqlQuery;
    }

    @Override
    @Transactional
    public void exportReportTemplates(LinkedHashMap<String, EdsReportTemplate> map) {
        for (String code : map.keySet()) {
            EdsReportTemplate changeTemplate = map.get(code);
            Integer value = reportTemplateManager.getIdByCode(code);
            if (value != null) {
                reportTemplateManager.updateTemplate(code, changeTemplate);
            } else {
                reportTemplateManager.insertTemplate(code, changeTemplate);
            }
            if (!Boolean.TRUE.equals(changeTemplate.getCustom()) && !Boolean.TRUE.equals(changeTemplate.getLibrary())) {
                EdsReportTemplate reportTemplate = reportTemplateManager.getByCode(code);
                if (reportTemplate != null) {
                    saveReportTemplatePermission(reportTemplate, null, null);
                }
            }
        }
    }

    @Override
    @Transactional
    public void deleteRolePermission(String code) {
        StringBuilder queryBuilder = new StringBuilder(100);
        for (String companyId : companyManager.getExistingSchemas()) {
            queryBuilder.append("DELETE FROM \"").append(companyId).append("\".rolepermission where permissioncode='").append(code).append("'; ");
        }
        executeNative(queryBuilder.toString());
    }

    //---------------Additional --------------

    @Override
    @Transactional
    public ReportingCustomizeFilter getCustomizeFilter(ReportRpc reportRpc) {
        ReportingCustomizeFilter filter = null;

        EdsCustomizeReport edsCustomizeReport = customizeReportManager.getByCode(WidgetConstants.WFP_REPORTING_DASHLET, reportRpc.getCode());
        if (edsCustomizeReport != null) {
            filter = edsCustomizeReport.toRPC();
        }
        if (filter == null || ServerUtils.isNullOrEmpty(filter.getReportCode())) {
            filter = new ReportingCustomizeFilter();
            filter.setReportCode(reportRpc.getCode());
            filter.setName(reportRpc.getName());
            filter.setId(reportRpc.getId());
            filter.setSortColumnName(reportRpc.getSortTableByColumn());
            filter.setSortType(reportRpc.getSortTableByColumnType());
            filter.setRowCount(reportRpc.getLimit());
        }

        EdsUser edsUser = userManager.getUser();
        EdsCompany company = edsUser.getCompany();
        String rolesCodeAsString = edsUser.getRolesCodeAsString();
        ViewRpc viewRpc = SqlQueryUtil.getViewParser(reportRpc.getViewCode());

        LinkedHashMap<String, ReportingRolePermissionItem> items = viewRpc.getRolePermissionFilterString();
        int i = 0;
        for (String item : items.keySet()) {

            ReportingRolePermissionItem permissionItem = items.get(item);
            List<String> roles = rolePermissionManager.getRolesByPermissionCode(permissionItem.getRole());
            for (String role : rolesCodeAsString.replace("'", "").split(",")) {

                if (roles.contains(role)) {
                    EdsReportingPermission reportingpermission = reportingPermissionManager.findByCode(company.getObjectID(), permissionItem.getRole(), PermissionConstants.WORKSPACE_CONTEXT);
                    if (reportingpermission.getParent() != null) {
                        EdsReportingPermission parent = reportingPermissionManager.get(reportingpermission.getParent());
                        ReportingRolePermissionItem listItem = new ReportingRolePermissionItem(++i, reportingpermission.getName(), reportingpermission.getCode(), permissionItem.getValue(), parent != null ? parent.getCode() : null);
                        if (filter.getViewAs().isEmpty()) {
                            listItem.setSelected(true);
                        }
                        filter.getViewAs().add(listItem);
                    }
                    break;
                }
            }
        }

        i = 0;

        for (ColumnRpc rpc : reportRpc.getSelectedColumns()) {
            SelectItem selectItem = new SelectItem();
            selectItem.setId(i++);
            selectItem.setName(reportRpc.getColumnMap().get(rpc.getName()).getTitle());
            selectItem.setDescription(rpc.getName());
            if (!(reportRpc.getSortTableByColumn() == null || "".equals(reportRpc.getSortTableByColumn())) && reportRpc.getSortTableByColumn().equals(rpc.getColumn())) {
                selectItem.setSelected(true);
            }
            if (!(reportRpc.getSortTableByColumnType() == null || "".equals(reportRpc.getSortTableByColumnType()))) {
                filter.setSortType(reportRpc.getSortTableByColumnType());
            }
            filter.getSelectedColumns().add(selectItem);
        }
        i = 0;

        for (ColumnRpc rpc : reportRpc.getFieldd()) {
            FilterRpc filterRpc = new FilterRpc();
            filterRpc.setColumn(rpc.getName());
            filterRpc.setValue(reportRpc.getValues().get(i));
            filterRpc.setOperation(reportRpc.getOperators().get(i));
            if (i > 0) {
                filterRpc.setAndOr(reportRpc.getBoolTypeAt(i - 1));
            }
            i++;
            filter.getFilterRpcs().add(filterRpc);
        }
        EdsCustomizeReport.appendFilterRpc(reportRpc, filter, edsCustomizeReport);

        filter.getColumnsMap().put(ReportingCustomizeFilter.ALL_COLUMNS, new LinkedList<>(reportRpc.getColumnMap().values()));

        return filter;
    }

    private StringBuffer getLog(Exception ex) {
        StringBuffer sb = new StringBuffer();
        for (StackTraceElement error : ex.getStackTrace()) {
            sb.append(error).append("<br/>");
        }
        return sb;
    }

    private String clearSpaces(String s) {
        if ("".equals(s)) {
            return "";
        }
        return s.replace(" ", "_");
    }

    private void fixErrors(ReportRpc reportRpc) {
        if (!reportRpc.getColumnMap().isEmpty()) {
            Iterator<ColumnRpc> iterator = reportRpc.getSelectedColumns().iterator();
            while (iterator.hasNext()) {
                ColumnRpc column = iterator.next();
                if (!reportRpc.getColumnMap().containsKey(column.getName())) {
                    iterator.remove();
                }
            }
            iterator = reportRpc.getGroupColumns().iterator();
            while (iterator.hasNext()) {
                ColumnRpc column = iterator.next();
                if (!reportRpc.getColumnMap().containsKey(column.getName())) {
                    iterator.remove();
                }
            }
            if (reportRpc.getFilterColumns() != null) {
                iterator = reportRpc.getFilterColumns().iterator();
                while (iterator.hasNext()) {
                    ColumnRpc column = iterator.next();
                    if (!reportRpc.getColumnMap().containsKey(column.getName())) {
                        iterator.remove();
                    }
                }
            }
            iterator = reportRpc.getSumaries().iterator();
            while (iterator.hasNext()) {
                ColumnRpc column = iterator.next();
                if (!reportRpc.getColumnMap().containsKey(column.getName())) {
                    iterator.remove();
                }
            }
            iterator = reportRpc.getFieldd().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                ColumnRpc column = iterator.next();
                if (!reportRpc.getColumnMap().containsKey(column.getName())) {
                    iterator.remove();
                    if (reportRpc.getValues().size() > i) {
                        reportRpc.getValues().remove(i);
                        reportRpc.getSett().remove(i);
                    }
                }
                i++;
            }
        }
    }

    /**
     * Searches from relations table
     * returned relations are separated into Map<String, List<Integer>>, where String is as relationtype
     * and List of integers is List of Ids of related item
     *
     * @param objectId
     * @param viewType
     * @return
     */
    @Override
    public ArrayList<RelatedLinkRPC> getRelatedLinks(Integer objectId, String viewType) {
        ArrayList<RelatedLinkRPC> result = new ArrayList<>();
        List<EdsRelation> relations = null;
        if (Constants.PURCHASE_ORDER.equals(viewType)) {
            relations = relationManager.getRelationsByRelationTypeToID(RelationItem.TYPE_PURCHASE_ORDER, objectId);
        } else if (Constants.SALE_QUOTE.equals(viewType)) {
            relations = relationManager.getRelationsByRelationTypeToID(RelationItem.TYPE_SALEQUOTE, objectId);
        } else if (AccountingConstants.PRODUCT.equals(viewType)) {
            relations = relationManager.getRelationsByRelationTypeToID(RelationItem.TYPE_PRODUCT, objectId);
        }
        if (relations != null && !relations.isEmpty()) {
            HashMap<String, List<Integer>> relationIdsByType = new HashMap<>();
            for (EdsRelation relation : relations) {
                String fromType = relation.getFromType();
                if (fromType != null && !fromType.isEmpty() && relation.getFromID() != null) {
                    if (relationIdsByType.containsKey(fromType)) {
                        relationIdsByType.get(fromType).add(relation.getFromID());
                    } else {
                        List<Integer> values = new ArrayList<>();
                        values.add(relation.getFromID());
                        relationIdsByType.put(fromType, values);
                    }
                }
            }
            for (String key : relationIdsByType.keySet()) {
                String ids = ServerUtils.getAsCommoDelimited(relationIdsByType.get(key), "0");
                if (RelationItem.TYPE_EMAIL_TRACKER.equals(key)) {
//                    List<EdsEmail> emails = emailFetchingService.getEmailsByIDs(relationIdsByType.get(key));todo
//                    result.addAll(getEmailRelatedLinks(emails));
                } else if (RelationItem.TYPE_TASK.equals(key)) {
                    List<EdsTask> tasks = taskManager.getTaskByIds(ids);
                    result.addAll(getTaskRelatedLinks(tasks));
                } else if (RelationItem.TYPE_OPPORTUNITY.equals(key)) {
                    List<EdsOpportunity> opportunities = opportunityManager.getOpportunityByIds(ids);
                    result.addAll(getOpportunityRelatedLinks(opportunities));
                } else if (RelationItem.TYPE_EVENT.equals(key)) {
                    List<EdsEvent> events = eventManager.getEventsByIDs(ids);
                    result.addAll(getEventRelatedLinks(events));
                }
            }
        }
        return result;
    }

    private List<RelatedLinkRPC> getEmailRelatedLinks(List<EdsEmail> emails) {
        List<RelatedLinkRPC> result = new ArrayList<>();
        if (emails != null && emails.size() > 0) {
            for (EdsEmail email : emails) {
                RelatedLinkRPC rlp = new RelatedLinkRPC();
                Email mail = email.getRPC();
                rlp.setFromtype(RelationItem.TYPE_EMAIL_TRACKER);
                rlp.setInnerHTML(mail.getSubject() != null ? mail.getSubject() : mail.getFromEmailWithName() != null ? mail.getFromEmailWithName() : mail.getToEmails());
                rlp.setHref("Crm.html#" + "messagecenter|messagecenter" + mail.getObjectID() + "/" + mail.getObjectID() + "/" + mail.getFolderName() + "/" + mail.getMessageUID() + "/" + mail.isCorporate() + "/" + mail.getTrackerID());
                result.add(rlp);
            }
        }
        return result;
    }

    private List<RelatedLinkRPC> getTaskRelatedLinks(List<EdsTask> tasks) {
        List<RelatedLinkRPC> result = new ArrayList<>();
        if (tasks != null && !tasks.isEmpty()) {
            for (EdsTask task : tasks) {
                RelatedLinkRPC rlp = new RelatedLinkRPC();
                rlp.setHref("ProjectManagement.html#" + "task|summary/" + task.getObjectID() + "/" + "false"); //false - not editable
                rlp.setFromtype(RelationItem.TYPE_TASK);
                rlp.setInnerHTML(task.getName() != null ? task.getName() : "<Empty Named Task>");
                result.add(rlp);
            }
        }
        return result;
    }

    private List<RelatedLinkRPC> getOpportunityRelatedLinks(List<EdsOpportunity> opportunities) {
        List<RelatedLinkRPC> result = new ArrayList<>();
        if (opportunities != null && !opportunities.isEmpty()) {
            for (EdsOpportunity opportunity : opportunities) {
                OpportunityListItem oli = opportunity.getRPC(new OpportunityListItem());
                RelatedLinkRPC rlp = new RelatedLinkRPC();
                rlp.setFromtype(RelationItem.TYPE_OPPORTUNITY);
                rlp.setInnerHTML(opportunity.getName() != null ? opportunity.getName() : "<Empty Opportunity Name>");
                rlp.setHref("Crm.html#" + "opportunity|summary/" + oli.getObjectId() + "/" + oli.isConvertedLead() + "/" + oli.getContactId() + "/" + oli.getAccountId());
                result.add(rlp);
            }
        }
        return result;
    }

    private List<RelatedLinkRPC> getEventRelatedLinks(List<EdsEvent> events) {
        List<RelatedLinkRPC> result = new ArrayList<>();
        if (events != null && !events.isEmpty()) {
            for (EdsEvent event : events) {
                RelatedLinkRPC rlp = new RelatedLinkRPC();
                rlp.setFromtype(RelationItem.TYPE_EVENT);
                rlp.setInnerHTML(event.getSubject());
                rlp.setHref("Crm.html#" + "event|summary/" + event.getObjectID());
                result.add(rlp);
            }
        }
        return result;
    }

    @Override
    public PaymentDeductionSelectItem[] getCategoriesForLookUp(ListingFilterParameter filterParameter) {
        return payrollServiceLocal.getCategoriesForLookUp(filterParameter);
    }

    public ListResult<CashAdvanceItem> getCashAdvanceList(ListingFilterParameter filterParametrs) {
        return payrollService.getCashAdvanceList(filterParametrs);
    }

    public boolean deleteCashAdvance(Integer objectId) {
        return payrollService.deleteCashAdvance(objectId);
    }

    public ArrayList<MyUpdateItem> getCashAdvanceUpdates(Integer objectId) {
        return payrollService.getCashAdvanceUpdates(objectId);
    }

    @Override
    public ReportRpc getQueryTotalResult(ReportRpc report, Integer userId) {
        boolean fromSqlServer;
        Integer companyId = report.getCompanyId();
        if (companyId == null && ServerSecurityContext.getInstance() != null && ServerSecurityContext.getInstance().getCompanyId() != null && !ServerSecurityContext.getInstance().getCompanyId().isEmpty()) {
            companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
            report.setCompanyId(companyId);
        }
        if (userId == null) {
            UserSecuritryRpc userSecuritryRpc = getUser();
            if (userSecuritryRpc != null) {
                userId = userSecuritryRpc.getUserId();
            } else {
                userId = 0;
            }
        }
        report.setUserID(userId);
        if (report.getBrowserTimeZone() == null || report.getBrowserTimeZone().isEmpty()) {
            report.setBrowserTimeZone(getUser().getTimezone());
        }

        if (report.getId() != null && report.getViewName() == null) {
            String browserTimeZone = report.getBrowserTimeZone();
            report = getReport(report.getId(), report.getXmlTemplateId());
            if (browserTimeZone != null && !"".equals(browserTimeZone)) {
                report.setBrowserTimeZone(browserTimeZone);
            }
            report.setFolderType(getFolderTypeByReportId(report.getId()));
        }
        EdsUser user = userManager.get(userId);
        if ((report.getBrowserTimeZone() == null || report.getBrowserTimeZone().isEmpty()) && userId != null) {
            if (user != null && user.getTimezone() != null && !"".equals(user.getTimezone())) {
                report.setBrowserTimeZone(user.getTimezone());
            }
        }

        Connection conn = null;
        ResultSet resultSet;
        String sqlQuery = "";
        try {
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
            setCustomReplacementItems(viewRpc);
            report.setFromKpi(viewRpc.isFromKpi());
            conn = getDataSourceConnection(report);
            report.setNoTimeZone(viewRpc.isNoTimezone());
            fromSqlServer = customDbUrl != null && customDbUrl.contains(SQLSERVER);
            fixErrors(report);
            report.setSqlServer(fromSqlServer);

            sqlQuery = SqlQueryUtil.getTotalCountQuery(companyId, report, viewRpc);

            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (fromSqlServer) {
                sqlQuery = getNormalizedQueryForSqlServer(sqlQuery);
            } else {
                preparedStatement.setPoolable(true);
            }

            if (report.getValues().size() > 0) {
                int reSetCount = viewRpc.getQueries().split("\\{where2}").length;
                setParametersToStatement(reSetCount, 1, report, preparedStatement);
            }

            EdsReportingExecuteTime reportingExecuteTime = new EdsReportingExecuteTime();
            reportingExecuteTime.setRequestDate(new Date());
            long startDate = System.currentTimeMillis();

            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int totalcount = resultSet.getInt(1);
            resultSet.close();

            long resultDate = System.currentTimeMillis() - startDate;
            reportingExecuteTime.setReportName(report.getViewName());
            reportingExecuteTime.setUser(user);
            reportingExecuteTime.setMillisSecund(resultDate);
            reportingExecuteTimeManager.create(reportingExecuteTime);

            report.setAllCount(totalcount);
            if ((report.getPosition() + report.getLimit() > totalcount && totalcount != 0)) {
                report.setNowPosition(1);
                report.setNowLastPosition(totalcount);
                if (report.getPosition() + report.getLimit() > totalcount) {
                    report.setNowPosition(report.getPosition());
                }
            } else {
                report.setNowPosition(report.getPosition());
                report.setNowLastPosition(report.getPosition() - 1 + report.getLimit());
            }

            if (report.getGroupColumns() != null && report.getGroupColumns().size() > 0) {
                report.setTableType(ReportType.SUMMARY.name());
            }
        } catch (SQLException e) {
            logError(e, companyId, userId, report.getId(), report.getName(), report.getViewName(), "******************Connection refused  to xls/csv***************");
            System.out.println(sqlQuery);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ec) {
                logError(ec, companyId, userId, report.getId(), report.getName(), report.getViewName(), "******************Connection to xls/csv does not closed ***************");
            }
        }

        return report;
    }

    @Override
    public CashAdvanceItem getCashAdvancedItem(ListingFilterParameter fp) {
        return payrollService.getCashAdvancedItem(fp);
    }

    @Override
    public TestRPC saveCashAdvance(CashAdvanceItem cashAdvanceItem) {
        return payrollService.saveCashAdvance(cashAdvanceItem);
    }

    @Override
    public SelectItem[] getDriversForLookUp(ListingFilterParameter filterParameter) {
        return payrollService.getDriversForLookUp(filterParameter);
    }

    @Override
    public TestRPC saveCashAdvancePayment(CashAdvancePayment cap) {
        return payrollService.saveCashAdvancePayment(cap);
    }

    @Override
    public ListResult<CashAdvancePayment> getCashAdvancePayments(ListingFilterParameter filter) {
        List<EdsPayslipPayments> payments = payslipPaymentsManager.getCashAdvancePayments(filter);
        ArrayList<CashAdvancePayment> list = new ArrayList<>();
        for (EdsPayslipPayments payment : payments) {
            CashAdvancePayment item = new CashAdvancePayment();
            item.setId(payment.getObjectID());
            item.setReference(payment.getReference());
            item.setPaymentAmount(payment.getPaymentTotal());
            if (payment.getPaymentDate() != null) {
                item.setPaymentDate(new DateNonConvertable(payment.getPaymentDate()));
            }
            EdsPayslipTableItem payslipItem = payslipTableItemManager.get(payment.getPayslipItemID());
            item.setPeriod(payslipItem != null ? payslipItem.getMonth() + " " + payslipItem.getYear() : "");
            list.add(item);
        }
        return new ListResult<>(list, payslipPaymentsManager.getCashAdvancePaymentAmount(filter));
    }

    @Override
    public TestRPC deleteCashAdvancePayment(Integer cashAdvanceId, Integer paymentId) {
        return payrollServiceLocal.deleteCashAdvancePayment(cashAdvanceId, paymentId);
    }

    @Override
    public KpiWidgetData getKpiWidgetData(ReportRpc report, boolean isFromRefresh) {
        Integer companyId = report.getCompanyId();
        String key = null;
        KpiWidgetData data = null;
        if (report.getId() != null) {
            key = companyId.toString() + "_" + report.getId().toString() + "_" + report.getCode();
            data = RedisClient.getKeyForR(key, KpiWidgetData.class);
        }
        if (data == null || isFromRefresh) {
            if (companyId == null && ServerSecurityContext.getInstance() != null && ServerSecurityContext.getInstance().getCompanyId() != null && !ServerSecurityContext.getInstance().getCompanyId().isEmpty()) {
                companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
                report.setCompanyId(companyId);
            }
            Integer userId;
            UserSecuritryRpc userSecuritryRpc = getUser();

            if (userSecuritryRpc != null) {
                userId = userSecuritryRpc.getUserId();
            } else {
                userId = 0;
            }

            ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());

            if (report.getViewCode() != null) {

                report.setFromKpi(viewRpc.isFromKpi());
            }
            report.setUserID(userId);

            if (report.getBrowserTimeZone() == null || report.getBrowserTimeZone().isEmpty()) {
                report.setBrowserTimeZone(getUser().getTimezone());
            }

            if (report.getId() != null && report.getViewName() == null) {
                String browserTimeZone = report.getBrowserTimeZone();
                report = getReport(report.getId(), report.getXmlTemplateId());

                if (browserTimeZone != null && !browserTimeZone.isEmpty()) {
                    report.setBrowserTimeZone(browserTimeZone);
                }
                report.setFolderType(getFolderTypeByReportId(report.getId()));
            }
            if ((report.getBrowserTimeZone() == null || report.getBrowserTimeZone().isEmpty()) && userId != null) {
                EdsUser user = userManager.get(userId);
                if (user != null && user.getTimezone() != null && !"".equals(user.getTimezone())) {
                    report.setBrowserTimeZone(user.getTimezone());
                }
            }

            Connection conn = null;


            KpiWidgetData kpiWidgetData = new KpiWidgetData();

            try {
                conn = getDataSourceConnection(report);
                report.setNoTimeZone(viewRpc.isNoTimezone());

                KpiWidgetItem kpiWidgetItem = report.getKpiWidgetItem();
                if (kpiWidgetItem != null) {
                    fixErrors(report);

                    EdsUser user = userManager.getUser();
                    EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
                    if (userSettings != null && userSettings.getInternationalization() != null) {
                        String userl = userSettings.getInternationalization();
                        if (kpiWidgetItem.getLocalization() != null) {
                            switch (userl) {
                                case "en" -> kpiWidgetData.setChartDataTitle(kpiWidgetItem.getLocalization().getEnglishName());
                                case "ar" -> kpiWidgetData.setChartDataTitle(kpiWidgetItem.getLocalization().getArabicName());
                                case "ru" -> kpiWidgetData.setChartDataTitle(kpiWidgetItem.getLocalization().getRussianName());
                                case "uz" -> kpiWidgetData.setChartDataTitle(kpiWidgetItem.getLocalization().getUzbekName());
                                default -> kpiWidgetData.setChartDataTitle(kpiWidgetItem.getKpiWidgetTitle());
                            }
                        } else {
                            kpiWidgetData.setChartDataTitle(kpiWidgetItem.getKpiWidgetTitle());
                        }
                        if (kpiWidgetItem.getSuffixLocalization() != null) {
                            switch (userl) {
                                case "en" -> kpiWidgetData.setChartDataSuffix(kpiWidgetItem.getSuffixLocalization().getEnglishName());
                                case "ar" -> kpiWidgetData.setChartDataSuffix(kpiWidgetItem.getSuffixLocalization().getArabicName());
                                case "ru" -> kpiWidgetData.setChartDataSuffix(kpiWidgetItem.getSuffixLocalization().getRussianName());
                                case "uz" -> kpiWidgetData.setChartDataSuffix(kpiWidgetItem.getSuffixLocalization().getUzbekName());
                                default -> kpiWidgetData.setChartDataSuffix(kpiWidgetItem.getKpiWidgetSuffix());
                            }
                        } else {
                            kpiWidgetData.setChartDataSuffix(kpiWidgetItem.getKpiWidgetSuffix());
                        }
                        if (kpiWidgetItem.getDifferenceLocalization() != null) {
                            switch (userl) {
                                case "en" -> kpiWidgetData.setDifferentTitle(kpiWidgetItem.getDifferenceLocalization().getEnglishName());
                                case "ar" -> kpiWidgetData.setDifferentTitle(kpiWidgetItem.getDifferenceLocalization().getArabicName());
                                case "ru" -> kpiWidgetData.setDifferentTitle(kpiWidgetItem.getDifferenceLocalization().getRussianName());
                                case "uz" -> kpiWidgetData.setDifferentTitle(kpiWidgetItem.getDifferenceLocalization().getUzbekName());
                                default -> kpiWidgetData.setDifferentTitle(kpiWidgetItem.getDifferentTitle());
                            }
                        } else {
                            kpiWidgetData.setDifferentTitle(kpiWidgetItem.getDifferentTitle());
                        }
                        if (kpiWidgetItem.getComparisonLocalization() != null) {
                            switch (userl) {
                                case "en" -> kpiWidgetData.setComparisionText(kpiWidgetItem.getComparisonLocalization().getEnglishName());
                                case "ar" -> kpiWidgetData.setComparisionText(kpiWidgetItem.getComparisonLocalization().getArabicName());
                                case "ru" -> kpiWidgetData.setComparisionText(kpiWidgetItem.getComparisonLocalization().getRussianName());
                                case "uz" -> kpiWidgetData.setComparisionText(kpiWidgetItem.getComparisonLocalization().getUzbekName());
                                default -> kpiWidgetData.setComparisionText(kpiWidgetItem.getComparisionText());
                            }
                        } else {
                            kpiWidgetData.setComparisionText(kpiWidgetItem.getComparisionText());
                        }
                    }
                    kpiWidgetData.setChartDataScale(kpiWidgetItem.getKpiWidgetScale());
                    kpiWidgetData.setChartDataTitleColor(kpiWidgetItem.getKpiWidgetTitleColor());
                    kpiWidgetData.setIncreaseColor(kpiWidgetItem.getIncreaseColor());
                    kpiWidgetData.setType(kpiWidgetItem.getType());
                    kpiWidgetData.setNegAndPosType(kpiWidgetItem.getNegAndPosType());
                    kpiWidgetData.setShowDifferent(kpiWidgetItem.isShowDifferent());
                    if (kpiWidgetItem.getLocalization() != null){
                        kpiWidgetData.setLocalization(kpiWidgetItem.getLocalization());
                    }
                    if (kpiWidgetItem.getSuffixLocalization() != null){
                        kpiWidgetData.setSuffixLocalization(kpiWidgetItem.getSuffixLocalization());
                    }
                    if (kpiWidgetItem.getDifferenceLocalization() != null){
                        kpiWidgetData.setDifferenceLocalization(kpiWidgetItem.getDifferenceLocalization());
                    }
                    if (kpiWidgetItem.getComparisonLocalization() != null){
                        kpiWidgetData.setComparisonLocalization(kpiWidgetItem.getComparisonLocalization());
                    }
                    if (kpiWidgetItem.getKpiWidgetMetric() != null) {
                        kpiWidgetData.setColorList(kpiWidgetItem.getKpiWidgetMetric().getColorList());
                    }
                    fillKpiWidgetData(report, conn, kpiWidgetData, true, viewRpc);
                    if (ChartTypeEnum.STANDARD_KPI.equals(kpiWidgetItem.getType()) || ChartTypeEnum.GROWTH_KPI.equals(kpiWidgetItem.getType())) {
                        fillKpiWidgetData(report, conn, kpiWidgetData, false, viewRpc);
//                        if (kpiWidgetData.getPercentVal() != null && !BigDecimal.ZERO.equals(kpiWidgetData.getPercentVal()) && kpiWidgetData.getCurrent() != null && BigDecimal.ZERO.compareTo(kpiWidgetData.getCurrent()) > 0) {
//                            kpiWidgetData.setPercentVal(kpiWidgetData.getPercentVal().multiply(BigDecimal.valueOf(-1)));
//                        }
                    } else {
                        kpiWidgetData.setComparision(BigDecimal.ZERO);
                        kpiWidgetData.setPercentVal(JdbcUtil.getPercentValue(kpiWidgetData));
                    }

                }
            } catch (SQLException e) {
                logError(e, companyId, userId, report.getId(), report.getName(), report.getViewName(), "******************Connection refused  to xls/csv***************");
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ec) {
                    logError(ec, companyId, userId, report.getId(), report.getName(), report.getViewName(), "******************Connection to xls/csv does not closed ***************");
                }
            }


            kpiWidgetData.setDifference(kpiWidgetData.getCurrent().subtract(kpiWidgetData.getComparision()));
            if (key != null) {
                Integer cachingTimeByReportCode = companySystemSettingsManager.getReportingCacheTime() != null ? companySystemSettingsManager.getReportingCacheTime() : 1800;
                RedisClient.setKeyForR(key, kpiWidgetData, KpiWidgetData.class, cachingTimeByReportCode);
            }
            return kpiWidgetData;
        } else {
            return data;
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    @Override
    public void createXmlBackupFile() {
        ArrayList<Integer> templateIds = reportTemplateManager.getReportTemplateIdsForBackup();
        ArrayList<String> companies = companyManager.getXmlBackupEnableCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        String dbName = SpringPropertiesUtil.getProperty("bg_databaseType");
        if (dbName == null) {
            dbName = ServerSecurityContext.getInstance().getDatabase();
        }

        for (String companyStrinId : companies) {
            for (Integer templateId : templateIds) {
                if (schemas.contains(companyStrinId)) {
                    ServerSecurityContext.getInstance().setDatabase(dbName);
                    ServerSecurityContext.getInstance().setCompanyId(companyStrinId);
                    EdsUser user = userManager.getAdmin(Integer.valueOf(companyStrinId));
                    SecurityContext.getInstance().setStaticUserID(user.getObjectID());

                    createXmlFileFromTemplate(templateId);
                }
            }
        }
    }

    @Transactional
    public FolderResource createFolder(Integer parentId, String name) throws
            InsufficientPermissionsException, DuplicateNameException, ObjectNotFoundException {
        return documentsService.createFolder(parentId, name);
    }

    @Transactional
    public FolderResource getFolderResource(int folderType, Integer entityID) {
        return documentsService.getFolderResource(folderType, entityID);
    }

    @Transactional
    public ArrayList<FileResource> saveXhrFile(ArrayList<FileResource> files, FolderResource folder, String
            description) {
        return documentsService.saveXhrFile(files, folder, description);
    }

    @Transactional(propagation = Propagation.NEVER)
    public void createXmlFileFromTemplate(Integer templateId) {
        try {
            ReportRpc reportRpc = reportingService.getReportStructure(templateId);
            reportRpc.setPosition(1);
            reportRpc.setLimit(1000000);
            ViewRpc viewRpc = SqlQueryUtil.getViewParser(reportRpc.getViewCode());
            reportRpc.setNoTimeZone(viewRpc.isNoTimezone());
            ResultSet resultSet = getTabularReportResult(reportRpc, null);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();

            resultSet.next();
            char groupingSeparator = getGroupingSeparator();
            resultSet.next();
            int x = 1 + (viewRpc.getHiddenColumnCount() > 0 ? reportRpc.getSelectedColumns().size() + viewRpc.getHiddenColumnCount() : reportRpc.getSelectedColumns().size());
            int id = 1 + (Math.max(viewRpc.getHiddenColumnCount(), 0));

            Element root = document.createElement(viewRpc.getEntityName());
            document.appendChild(root);

            Element idElement = null;
            boolean first = true;
            while (resultSet.next()) {
                if (first) {
                    first = false;

                    if (viewRpc.getHiddenColumnCount() > 0) {
                        idElement = document.createElement(viewRpc.getId());
                        root.appendChild(idElement);

                        Attr attr = document.createAttribute("value");
                        attr.setValue(resultSet.getString(1));
                        idElement.setAttributeNode(attr);
                    } else {
                        String value = "elementValue";
                        if (reportRpc.getSelectedColumns().get(0).getName() != null) {
                            value = reportRpc.getSelectedColumns().get(0).getName().replace(" ", "_")
                                    .replace("\\.", "_");
                        }
                        idElement = document.createElement(value);
                        root.appendChild(idElement);

                        Attr attr = document.createAttribute("value");
                        attr.setValue(resultSet.getString(1));
                        idElement.setAttributeNode(attr);
                    }
                }
                for (int i = id; i < x; i++) {
                    String value = resultSet.getString(i);
                    if (value == null) {
                        value = "n/a";
                    }
                    value = replace(value, new String[]{"(?s)<!--.*?-->", "\\<[^>]*>"}, new String[]{"", ""});
                    value = replace(HTMLParser.getText(value), new String[]{"&nbsp;", "&quot;"}, new String[]{"\n", "\""}).trim();
                    String columnFormat = (columnFormat = reportRpc.getSelectedColumns().get(i - id).getColumnFormat()) != null ? columnFormat : "";
                    switch (columnFormat) {
                        case ColumnFormat_NUMBER, ColumnFormat_DOUBLE, ColumnFormat_MONEY, ColumnFormat_PERCENT, ColumnFormat_TIME -> {
                            if (value.equals("n/a") || value.equals("")) {
                                value = "0";
                            }
                            value = value.replace("" + groupingSeparator, "");
                        }
                    }
                    String elementvalue = "elementValue";
                    if (reportRpc.getSelectedColumns().get(i - id).getName() != null) {
                        elementvalue = reportRpc.getSelectedColumns().get(i - id).getName()
                                .replace(" ", "_")
                                .replace("\\.", "_");
                    }
                    Element dataElement = document.createElement(elementvalue);
                    dataElement.appendChild(document.createTextNode(value));
                    idElement.appendChild(dataElement);
                }

            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM_dd_yyyy_HH_mm");
            String fileNameDate = dateFormat.format(new Date());
            String filename = clearSpaces(viewRpc.getEntityName()) + "_" + fileNameDate;
            filename = ServerUtils.normalizeFileNameT(filename);

            if (GwtUploadServlet.realPath == null) {
                GwtUploadServlet.realPath = servletContext.getRealPath("uploads") + "/";
            }
            File folder = new File(GwtUploadServlet.realPath + "xmlBuckups/");
            folder.mkdirs();
            final File file = new File(folder.getAbsolutePath() + "/" + filename + ".xml");
            file.createNewFile();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            FileInputStream stream = new FileInputStream(file);
            uploadManager.saveXmlBackup(stream, file.getName());
            stream.close();
            file.deleteOnExit();
        } catch (ParserConfigurationException | TransformerException | NoSuchAlgorithmException | IOException | SQLException ignored) {

        }
    }

    private char getGroupingSeparator() {
        char groupingSeparator = ',';
        try {
            if (ServletUtils.getRequest() != null && ServletUtils.getRequest().getServerName() != null) {
                Locale locale = EdsContextParams.getDefaultLocale(ServletUtils.getRequest().getServerName());
                groupingSeparator = DecimalFormatSymbols.getInstance(locale).getGroupingSeparator();
                groupingSeparator = !("".equals("" + groupingSeparator)) ? groupingSeparator : ',';
            }
        } catch (Exception e) {
            groupingSeparator = DecimalFormatSymbols.getInstance(Locale.getDefault()).getGroupingSeparator();
            groupingSeparator = !("".equals("" + groupingSeparator)) ? groupingSeparator : ',';
        }
        return groupingSeparator;
    }

    private void fillKpiWidgetData(ReportRpc report, Connection conn, KpiWidgetData kpiWidgetData,
                                   boolean isCurrent, ViewRpc viewRpc) {
        KpiWidgetFilterItem widgetFilterItem = isCurrent ? report.getKpiWidgetItem().getKpiWidgetFilterItemOne() : report.getKpiWidgetItem().getKpiWidgetFilterItemTwo();
        report.getFieldd().clear();
        report.getValues().clear();
        report.clearBoolType();
        report.getOperators().clear();
        report.getSett().clear();

        if (!isCurrent) {
            int index = widgetFilterItem.getOperators().indexOf(SamePeriodLastYear.getName());
            if (index != -1) {
                if (report.getKpiWidgetItem().getKpiWidgetFilterItemOne().getOperators().get(index).equals(Between.getName())) {
                    String updatedDateRangeStr = getUpdatedDateRangeStr(report.getKpiWidgetItem().getKpiWidgetFilterItemOne().getValues().get(index));
                    widgetFilterItem.getValues().set(index, updatedDateRangeStr);
                    widgetFilterItem.getOperators().set(index, Between.getName());

                } else {
                    widgetFilterItem.getValues().set(index, getSamePeriodValue(report.getKpiWidgetItem().getKpiWidgetFilterItemOne().getOperators().get(index)));
                }
            }
        }

        if (widgetFilterItem != null && widgetFilterItem.getValues().size() > 0) {
            report.setFilterPattern(widgetFilterItem.getFilterPattern());
            report.getFieldd().addAll(widgetFilterItem.getFieldd());
            report.getValues().addAll(widgetFilterItem.getValues());
            report.setBoolType(widgetFilterItem.getBoolType());
            report.getOperators().addAll(widgetFilterItem.getOperators());
            report.getSett().addAll(widgetFilterItem.getSett());
        }


        String sqlQuery = SqlQueryUtil.getKpiWidgetQuery(report.getCompanyId(), viewRpc, report);

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setPoolable(true);

            if (report.getValues().size() > 0) {
                int reSetCount = viewRpc.getQueries().split("\\{where2}").length;
                setParametersToStatement(reSetCount, 1, report, preparedStatement);
            }

            ResultSet rs = preparedStatement.executeQuery();
            if (!ChartTypeEnum.RANKING_KPI.equals(report.getKpiWidgetItem().getType())) {
                String color = null;
                if (isCurrent) {
                    kpiWidgetData.setCurrent(JdbcUtil.getKpiWidgetData(rs));
                    setPercentValueWhenCurrentValueIsLessThanHundred(kpiWidgetData);
                    color = selectWidgetDataColor(kpiWidgetData.getCurrent(), report.getKpiWidgetItem());
                } else {
                    kpiWidgetData.setComparision(JdbcUtil.getKpiWidgetData(rs));
                    JdbcUtil.getActualPercentValue(kpiWidgetData);
                    setPercentValueWhenCurrentValueIsLessThanHundred(kpiWidgetData);
                    BigDecimal val = kpiWidgetData.getPercentVal();
                    if (ChartTypeEnum.STANDARD_KPI.equals(report.getKpiWidgetItem().getType())) {
                        val = kpiWidgetData.getCurrent();
                    }
                    color = selectWidgetDataColor(val, report.getKpiWidgetItem());
                }
                kpiWidgetData.setChartDataTitleColor(color);
//                kpiWidgetData.setIncreaseColor(color);
            } else {
                kpiWidgetData.setCurrent(BigDecimal.ZERO);
                kpiWidgetData.setComparision(BigDecimal.ZERO);
                kpiWidgetData.setTableData(JdbcUtil.getKpiRankingData(rs, report.getKpiWidgetItem()));
            }
            rs.close();

        } catch (SQLException e) {
            logError(e, report.getCompanyId(), report.getUserID(), report.getId(), report.getName(), report.getViewName(), "******************Connection refused  to xls/csv***************");
            System.out.println(sqlQuery);
        }
        if (kpiWidgetData.getTableData() == null) {
            kpiWidgetData.setTableData(new ArrayList<>());
        }
        System.out.println("Kpi Widget process successful complated...");
    }

    private void setPercentValueWhenCurrentValueIsLessThanHundred(KpiWidgetData kpiWidgetData) {
        if (kpiWidgetData.getPercentVal() != null && !BigDecimal.ZERO.equals(kpiWidgetData.getPercentVal()) && kpiWidgetData.getCurrent() != null && BigDecimal.ZERO.compareTo(kpiWidgetData.getCurrent()) > 0) {
            kpiWidgetData.setPercentVal(kpiWidgetData.getPercentVal().multiply(BigDecimal.valueOf(-1)));
        }
    }

    private String selectWidgetDataColor(BigDecimal current, KpiWidgetItem kpiWidgetItem) {
        String finalcolorCode = null;
        if (kpiWidgetItem != null && kpiWidgetItem.getKpiWidgetMetric() != null && kpiWidgetItem.getKpiWidgetMetric().getColorList() != null) {
            for (ColumnColor columnColor : kpiWidgetItem.getKpiWidgetMetric().getColorList()) {
                String colorCode = JdbcUtil.getColorByPoint(columnColor, current);
                if (colorCode != null) {
                    finalcolorCode = colorCode;
                }
            }
        }
        return finalcolorCode;
    }

    private String getSamePeriodValue(String operator) {
        String functionName = switch (operator) {
            case "Before3Week" -> "currentdate() - INTERVAL '3 week' - INTERVAL '1 year'";
            case "Before1Week" -> "currentweekfirstday() - INTERVAL '1 year'";
            case "BeforeMonth" -> "currentmonthfirstday() - INTERVAL '1 year'";
            case "Yesterday" ->
                    "currentdate() - INTERVAL '1 day' - INTERVAL '1 year'_currentdate() + INTERVAL '1 day - 1 ms' - INTERVAL '1 day' - INTERVAL '1 year'";
            case "Today" ->
                    "currentdate() - INTERVAL '1 year'_currentdate() + INTERVAL '1 day - 1 ms' - INTERVAL '1 year'";
            case "Tomorrow" ->
                    "currentdate() + INTERVAL '1 day' - INTERVAL '1 year' _currentdate() + INTERVAL '1 day - 1 ms' + INTERVAL '1 day' - INTERVAL '1 year'";
            case "Last3Hour" ->
                    "currenttimstamp() - INTERVAL '3 hour' - INTERVAL '1 year'_currenttimstamp() - INTERVAL '1 year'";
            case "Last2Hour" ->
                    "currenttimstamp() - INTERVAL '2 hour' - INTERVAL '1 year'_currenttimstamp() - INTERVAL '1 year'";
            case "Last1Hour" ->
                    "currenttimstamp() - INTERVAL '1 hour' - INTERVAL '1 year'_currenttimstamp() - INTERVAL '1 year'";
            case "ThisAndLastMonth" ->
                    "(currentmonthfirstday() - interval '1 year'  - interval '1 month' ) _ (currentmonthlastday() - interval '1 year + 1 day - 1 ms')";
            case "LastMonth" ->
                    "currentmonthfirstday() - INTERVAL '1 month' - INTERVAL '1 year'_currentmonthfirstday() - INTERVAL '1 ms' - INTERVAL '1 year'";
            case "ThisMonth" ->
                    "(currentmonthfirstday() - interval '1 year') _ (currentmonthlastday() - interval '1 year' + interval '1 day - 1 ms')";
            case "NextMonth" ->
                    " (currentmonthfirstday() - INTERVAL '1 year') + INTERVAL '1 month' _ (currentmonthfirstday() - INTERVAL '1 year') + INTERVAL '2 month' - INTERVAL '1 ms'";
            case "ThisAndNextMonth" ->
                    "currentmonthfirstday() - INTERVAL '1 year'_currentmonthfirstday() + interval '2 month' - interval '1 ms' - INTERVAL '1 year'";
            case "ThisandLastQuarter" ->
                    "(currentquarterfirstday() - INTERVAL '1 year 3 month') _ (currentquarterlastday() - INTERVAL '1 year'+ INTERVAL '1 day - 1 ms')";
            case "LastQuarter" ->
                    "currentquarterfirstday() - interval '1 year 3 month' _ currentquarterfirstday() - interval '1 year 1 sec'";
            case "ThisQuarter" ->
                    "currentquarterfirstday() - interval '1 year' _ currentquarterlastday() + interval '1 day - 1 ms' - interval '1 year'";
            case "NextQuarter" ->
                    "currentquarterlastday() + interval '1 day - 1 ms' + interval '1 sec' - interval '1 year' _ (currentquarterlastday() + interval '1 day - 1 ms' + interval '1 sec' + interval '3 month' - interval '1 sec') - interval '1 year'";
            case "ThisandNextQuarter" ->
                    "currentquarterfirstday() - INTERVAL '1 year' _ currentquarterlastday() + INTERVAL '1 day - 1 ms' + INTERVAL '1 sec' + INTERVAL '3 month' - INTERVAL '1 sec' - INTERVAL '1 year'";
            case "ThisAndLastTwoYears" ->
                    "currentyearfirstday() - INTERVAL '2 year' _ currentyearlastday() + INTERVAL '1 day - 1 ms' - INTERVAL '2 year'";
            case "LastTwoYears" ->
                    "currentyearfirstday() - INTERVAL '3 year' _ currentyearlastday() + INTERVAL '1 day - 1 ms' - INTERVAL '2 year'";
            case "ThisAndLastYear" ->
                    "currentyearfirstday() - interval '2 years' _ (currentyearlastday() + interval '1 day - 1 ms') - interval '1 year'";
            case "LastYear" ->
                    "currentyearfirstday() - INTERVAL '2 year' _ currentyearlastday() + INTERVAL '1 day - 1 ms' - INTERVAL '2 year'";
            case "Last3Week" ->
                    "currentdate() - INTERVAL '3 week' - INTERVAL '1 year' _ currentdate() - INTERVAL '1 year'";
            case "LastWeek" ->
                    "currentweekfirstday() - interval '1 year 1 week' _ currentweeklastday() + interval '1 day - 1 ms' - interval ' 1 year 1 week'";
            case "ThisWeek" ->
                    "currentweekfirstday() - INTERVAL '1 year' _ currentweeklastday() + INTERVAL '1 day - 1 ms' - INTERVAL '1 year'";
            case "ThisYear" ->
                    "currentyearfirstday() - INTERVAL '1 year' _ currentyearlastday() + INTERVAL '1 day - 1 ms' - INTERVAL '1 year'";
            case "NextWeek" ->
                    "(currentweekfirstday() - INTERVAL '1 YEAR') + INTERVAL '1 WEEK' _ (currentweeklastday() + INTERVAL '1 DAY - 1 MS' + INTERVAL '1 WEEK' - INTERVAL '1 YEAR')";
            case "NextYear" -> "currentyearfirstday() _ currentyearlastday() + INTERVAL '1 day - 1 ms'";
            case "ThisAndNextYear" ->
                    "(currentyearfirstday() - INTERVAL '1 YEAR') _ (currentyearlastday() + INTERVAL '1 DAY - 1 MS')";
            case "TwoYearsAgo" ->
                    "currentyearfirstday() - INTERVAL '3 year'_currentyearlastday() + INTERVAL '1 day - 1 ms' - INTERVAL '3 year'";
            default -> null;
        };
        return functionName;
    }

    private String getUpdatedDateRangeStr(String dateRangeStr) {
        String[] dateRangeParts = dateRangeStr.split("_");

        // Define a custom date format with a three-letter month abbreviation
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH);

        LocalDate startDate = LocalDate.parse(dateRangeParts[0], formatter).minusYears(1);
        LocalDate endDate = LocalDate.parse(dateRangeParts[1], formatter).minusYears(1);

        return startDate.format(formatter) + "_" + endDate.format(formatter);
    }
    @Override
    public String createOrUpdateCustomHtml(String text, Integer reportId) {
        EdsCustomHtml edsCustomHtml= customHtmlManager.getCustomHtmlByReportId(reportId);
        if(edsCustomHtml==null){
            edsCustomHtml = new EdsCustomHtml();
            EdsReport edsReport = reportingManager.get(reportId);
            edsCustomHtml.setHtmlCode(text);
            edsCustomHtml.setEdsReport(edsReport);
            customHtmlManager.create(edsCustomHtml);

            return  "Created";}
        else {
            edsCustomHtml.setHtmlCode(text);
            customHtmlManager.update(edsCustomHtml);

        }
        return "Updated";
    }

    @Override
    public String getCustomHtmlCodeByReportId(Integer id) {
        EdsCustomHtml edsCustomHtml=customHtmlManager.getCustomHtmlByReportId(id);
        if(edsCustomHtml!=null) {
            return edsCustomHtml.getHtmlCode();
        }
        return null;
    }

    @Override
    public String getDefaultHtmlCode() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("/template/reporting_system.html" );
        String htmlCode ="";
        try {
            htmlCode = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  htmlCode;

    }
}
