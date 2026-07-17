package com.edatasite.workforce.gwt.project.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsBillOfMaterial;
import com.edatasite.workforce.core.domain.EdsBookingItem;
import com.edatasite.workforce.core.domain.EdsBookingItemReservation;
import com.edatasite.workforce.core.domain.EdsCheckInLocation;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsContract;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsItemReminder;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsNoteComment;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsPositionTask;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectBudget;
import com.edatasite.workforce.core.domain.EdsProjectBudgetItem;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsProjectEmployeeWageClientRateHistory;
import com.edatasite.workforce.core.domain.EdsProjectPosition;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransferItem;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournalItem;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.analyzer.EdsSolrDbConsistency;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.customfields.EdsBookingItemCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsContractCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsProjectCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsProjectItemTableCF;
import com.edatasite.workforce.core.domain.customform.EdsProjectCustomItemTable;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.solr.component.OpportunitySolrComponent;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.core.solr.document.ProjectSolrDoc;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.BillOfMaterialItem;
import com.edatasite.workforce.gwt.core.client.rpc.BookingReservationItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardIssues;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardTasks;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.CheckInLocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.PathFinder;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import com.edatasite.workforce.gwt.core.server.app.RolePermissionServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.edatasite.workforce.gwt.core.server.app.WfmTreeItemFactory;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.BookingItemManager;
import com.edatasite.workforce.gwt.core.server.db.BookingItemReservationManager;
import com.edatasite.workforce.gwt.core.server.db.CheckInLocationManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ContractManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ItemReminderManager;
import com.edatasite.workforce.gwt.core.server.db.LayoutManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.NoteCommentManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectBudgetManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectItemTableCFManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectPositionManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentItemManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.UnitMeasurementManager;
import com.edatasite.workforce.gwt.core.server.db.analyzer.SolrDbConsistencyManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.BookingItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ContractCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ProjectCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.ListingObjectItem;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.AccountingProjectSolrEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ProjectBackupManagerEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ProjectDocumentsReIndexEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ProjectEmployeeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ProjectEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ProjectFolderEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ProjectManagerEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ProjectSolrSensitiveDataChangeListener;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ProjectStatusEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.TaskCloneListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.TaskSolrEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.project.client.rpc.BookingItemsItem;
import com.edatasite.workforce.gwt.project.client.rpc.CloneProjectItem;
import com.edatasite.workforce.gwt.project.client.rpc.ContractListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ContractSingleItem;
import com.edatasite.workforce.gwt.project.client.rpc.ContractViewItem;
import com.edatasite.workforce.gwt.project.client.rpc.EditContract;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.NearbyProjectDto;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetCellItem;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetData;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetRowItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudget;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudgetItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectEmployeeWageClientHistoryItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectExpenseReportsListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectInvoice;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectLabourCosts;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import com.edatasite.workforce.gwt.project.client.rpc.WageTaxItem;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeEntriesItem;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import net.sf.mpxj.Duration;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectHeader;
import net.sf.mpxj.RelationType;
import net.sf.mpxj.Resource;
import net.sf.mpxj.Task;
import net.sf.mpxj.TaskType;
import net.sf.mpxj.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.params.CommonParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.TAX_CALCULATION_INCLUSIVE;

/**
 * Created by IntelliJ IDEA. User: Anvarbek Date: 07.01.2008 Time: 14:24:02 To
 * change this template use File | Settings | File Templates.
 */
@Transactional
@Service("projectService")
public class ProjectServiceImpl implements ProjectService, ProjectServiceLocal, CommandConstants, Constants, SchedulerConstant {

    public static final DecimalFormat decimalFormat = new DecimalFormat("0000");
    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private ContractManager contractManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    @Qualifier("taskService")
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    @Qualifier("emailTemplateService")
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private WfmJpaOperations jpaTemplate;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private ProjectCFManager projectCFManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private NoteCommentManager noteCommentManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private SolrDbConsistencyManager solrDbConsistencyManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    ManualJournalManager manualJournalManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    @Qualifier("rolePermissionService")
    private RolePermissionServiceLocal rolePermissionServiceLocal;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private ProjectBudgetManager projectBudgetManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private ItemReminderManager itemReminderManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private LayoutManager layoutManager;
    @Autowired
    private BookingItemManager bookingItemManager;
    @Autowired
    private BookingItemReservationManager bookingItemReservationManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private ProjectItemTableManager projectItemTableManager;
    @Autowired
    private ProjectItemTableCFManager projectItemTableCFManager;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneService;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private ProjectPositionManager projectPositionManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private SpendReceiveMoneyManager spendReceiveMoneyManager;
    @Autowired
    private ContractCFManager contractCFManager;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    private StockAdjustmentItemManager stockAdjustmentItemManager;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    protected UnitMeasurementManager unitMeasurementManager;
    @Autowired
    private RFPManager rfpManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ProfileServiceLocal profileService;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private OpportunitySolrComponent opportunitySolrComponent;
    @Autowired
    private BookingItemCFManager bookingItemCFManager;
    @Autowired
    private CheckInLocationManager checkInLocationManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectViewItem viewProject(Integer objectID) {
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsProject.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Project view");

        EdsProject project = this.projectManager.get(objectID);
        ProjectViewItem projectViewItem = new ProjectViewItem();
        EdsUser user = this.employeeManager.getUser();
        projectViewItem.setObjectID(objectID);
        EdsProject defaultProject = user.getCompany().getDefaultProject();
        EdsProject crmProject = this.projectManager.getCrmProject();
        if (defaultProject != null) {
            projectViewItem.setDefaultProjectID(defaultProject.getObjectID());
        }
        if (crmProject != null) {
            projectViewItem.setCrmProjectID(crmProject.getObjectID());
        }

        projectViewItem.setPermissions(this.getProjectSpecificPermissions(user, project));

        projectViewItem.setName(project.getName());
        if (project.getNumber() != null) {
            NumberData numberData = new NumberData();
            numberData.setNumberString(project.getNumber());
            projectViewItem.setNumberData(numberData);
        }

        EdsContract contract = this.contractManager.getContractByProjectId(project.getObjectID());
        if (contract != null && !contract.getDeleted()) {
            projectViewItem.setContractName(contract.getNumber());
            projectViewItem.setContractID(contract.getObjectID());
        }
        projectViewItem.setDescription(project.getDescription());
        ArrayList<SelectItem> backupMangers = new ArrayList<>();
        for (EdsEmployee backupManager : project.getBackupManagers()) {
            SelectItem item = new SelectItem();
            item.setId(backupManager.getObjectID());
            item.setName(backupManager.getName());
            backupMangers.add(item);
        }
        backupMangers.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        projectViewItem.setBackupManagers(backupMangers);
        List<CheckInLocationItem> locations = new ArrayList<>();
        for (EdsCheckInLocation checkInLocation : project.getCheckInLocations()) {
            locations.add(new CheckInLocationItem(checkInLocation.getObjectID(),checkInLocation.getLatitude().toString(), checkInLocation.getLongitude().toString(), checkInLocation.getRadius().toString()));
        }
        projectViewItem.setCheckInLocations(locations);
        if (project.getClients() != null && !project.getClients().isEmpty()) {
            ArrayList<SelectItem> clients = new ArrayList<>();
            for (EdsCrmAccount client : project.getClients()) {
                clients.add(client.getAsSelectItem());
            }
            projectViewItem.setClients(clients.toArray(new SelectItem[]{}));
        } else if (project.getClient() != null) {
            EdsCrmAccount client = this.clientManager.get(project.getClient().getObjectID());
            if (client != null && !client.isDeleted()) {
                projectViewItem.setClient(project.getClient().getName());
                projectViewItem.setClientId(project.getClient().getObjectID());

                ArrayList<Integer> ids = new ArrayList<>();
                ids.add(client.getObjectID());
                projectViewItem.setClientBalance(this.crmAccountManager.getClientBalance(client.getObjectID()).doubleValue());

                List<EdsInvoicePayment> paymentList = this.invoicePaymentManager.getAccountPrePaymentsWithoutReversed(client.getObjectID(), AccountingConstants.RECEIVABLE_PREPAYMENT, project.getObjectID());
                if (paymentList != null && !paymentList.isEmpty()) {
                    BigDecimal totalRetainers = BigDecimal.ZERO;
                    for (EdsInvoicePayment prePayment : paymentList) {
                        BigDecimal baseAmount = prePayment.getBaseAmount();
                        if (baseAmount == null) {
                            baseAmount = prePayment.getAmount().divide(prePayment.getExchangeRate(), 5, RoundingMode.HALF_UP);
                        }
                        totalRetainers = totalRetainers.add(baseAmount.subtract(this.invoicePaymentManager.getAppliedPrePaymentAmountInBase(client.getObjectID(), prePayment.getObjectID(), AccountingConstants.RECEIVABLE_PREPAYMENT)));
                    }
                    projectViewItem.setClientRetainers(totalRetainers.doubleValue());
                }
            } else if (client != null && client.isDeleted()) {
                projectViewItem.setClient(this.commonLocalizer.localize("notAvailable", "N/A"));
            }
        }
        SelectItem[] statusesCount = this.projectManager.getTasksCountByProject(project.getObjectID());
        for (SelectItem item : statusesCount) {
            String status = item.getName();
            int count = item.getId() != null ? item.getId() : 0;
            switch (status) {
                case EdsTask.CANCELLED -> projectViewItem.setCancelledTasks(count);
                case EdsTask.IN_PROGRESS -> projectViewItem.setInProgressTasks(count);
                case EdsTask.NOT_STARTED -> projectViewItem.setNotStartedTasks(count);
                case EdsTask.COMPLETED -> projectViewItem.setCompletedTasks(count);
                case EdsTask.WAITING_FOR_SOMEONE_ELSE -> projectViewItem.setWaitingTasks(count);
                case EdsTask.CLOSED -> projectViewItem.setClosedTasks(count);
            }
        }
        if (project.getStatus() != null) {
            projectViewItem.setStatus(this.referenceWfmMessageSource.localize(project.getStatus().getCode(), project.getStatus().getName()));
            projectViewItem.setStatusID(project.getStatus().getObjectID());
            projectViewItem.setStatusCode(project.getStatus().getCode());
        } else {
            projectViewItem.setStatus(this.commonLocalizer.localize("notAvailable", "N/A"));
        }

        DecimalFormat df = new DecimalFormat("0.00");
        Double[] projectCostAndTimeSpent = this.timeSheetManager.getProjectCostAndTimeSpent(objectID, null);
        HashMap<Integer, Double> waitingHoursMap = this.timeSheetManager.getProjectTimeSpents(objectID.toString(), EdsTimeSheet._WAITING);
        HashMap<Integer, Double> rejectedHoursMap = this.timeSheetManager.getProjectTimeSpents(objectID.toString(), EdsTimeSheet._REJECT);

        Double actualProjectExpense = this.projectBudgetManager.getProjectExpense(objectID).doubleValue();
        Double actualProjectCost = (projectCostAndTimeSpent != null ? (projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_COST] != null ? projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_COST] : 0d) : 0d);
        Double estimatedProjectExpense = this.projectBudgetManager.getProjectPlanedExpense(objectID).doubleValue();
        Double estimateProjectCost = projectCostAndTimeSpent != null ? (projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_COST] != null ? projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_COST] : 0d) : 0d;
        projectViewItem.setHoursSpent(projectCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_TIME_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_TIME_SPENT].intValue() : 0) : "00:00");
        projectViewItem.setTimeSpent(projectCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(projectCostAndTimeSpent[Constants.PROJECT_HOURS_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_HOURS_SPENT].intValue() : 0) : "00:00");
        projectViewItem.setActualCost(df.format(actualProjectExpense + actualProjectCost));
        projectViewItem.setEstimatedTime(projectCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_TIME_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_TIME_SPENT].intValue() : 0) : "00:00");
        projectViewItem.setWaitingHours(waitingHoursMap.get(objectID) != null ? ServerUtils.getTimeSpentHM(waitingHoursMap.get(objectID).intValue()) : "00:00");
        projectViewItem.setRejectedHours(rejectedHoursMap.get(objectID) != null ? ServerUtils.getTimeSpentHM(rejectedHoursMap.get(objectID).intValue()) : "00:00");
        projectViewItem.setEstimatedCost(df.format(estimatedProjectExpense + estimateProjectCost));
        projectViewItem.setManager(project.getManager() != null ? project.getManager().getFullName() : this.commonLocalizer.localize("notAvailable", "N/A"));
        projectViewItem.setManagerId(project.getManager() != null ? project.getManager().getObjectID() : null);
        projectViewItem.setCreator(project.getCreator() != null ? project.getCreator().getFullName() : this.commonLocalizer.localize("notAvailable", "N/A"));
        projectViewItem.setCreatorID(project.getCreator() != null ? project.getCreator().getObjectID() : null);
        projectViewItem.setCreationDate(project.getCreationTime());
        projectViewItem.setLastUpdaterName(project.getUpdater() != null ? project.getUpdater().getFullName() : this.commonLocalizer.localize("notAvailable", "N/A"));
        projectViewItem.setLastUpdateTime(project.getLastUpdateTime());
        projectViewItem.setStartDate(project.getStartDate() != null ? new Date(project.getStartDate().getTime()) : null);
        projectViewItem.setEndDate(project.getEndDate() != null ? new Date(project.getEndDate().getTime()) : null);
        projectViewItem.setDueDate(project.getDueDate() != null ? new Date(project.getDueDate().getTime()) : null);
        projectViewItem.setEncryptedID(EncryptionHelper.encryptURL("project/" + projectViewItem.getObjectID()));
        projectViewItem.setActualStartDate(this.taskManager.getFirstProjectTask(objectID));
        projectViewItem.setActualEndDate(this.taskManager.getLastExistingProjectTask(objectID));
        projectViewItem.setEmployeeAssignment(project.getEmployeeAssignment());
        projectViewItem.setBillable(project.getBillable());
        EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
        Float f;
        if (settings != null && settings.isAutomatic()) {
            Float timesheet = projectCostAndTimeSpent != null && projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_TIME_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_TIME_SPENT].floatValue() : 0;
            Float estimatedtime = projectCostAndTimeSpent != null && projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_TIME_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_TIME_SPENT].floatValue() : 0;
            f = estimatedtime != 0 ? new BigDecimal(timesheet * 100 / estimatedtime).setScale(2, RoundingMode.HALF_UP).floatValue() : 0f;
            projectViewItem.setComplete(estimatedtime != 0 ? new BigDecimal(timesheet * 100 / estimatedtime).setScale(2, RoundingMode.HALF_UP).toString() : "0.0");
        } else {
            f = project.getProjectTasksAveragePercentCompleted();
        }
        projectViewItem.setComplete(String.valueOf(((f > 100f && !this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) ? 100f : f)));

        if (EmployeeAssignmentEnum.BY_POSITION.equals(project.getEmployeeAssignment())) {
            List<EdsProjectEmployee> projectEmployeeList = this.projectEmployeeManager.getProjectEmployees(project);

            if (projectEmployeeList != null && !projectEmployeeList.isEmpty()) {
                ArrayList<PositionsSelectItem> positionsSelectItemList = new ArrayList<>();
                HashMap<Integer, EdsProjectPosition> map = new HashMap<>();

                for (EdsProjectEmployee projectEmployee : projectEmployeeList) {
                    EdsEmployee employee = projectEmployee.getEmployeeDepartment().getEmployee();

                    PositionsSelectItem positionsSelectItem = new PositionsSelectItem();
                    positionsSelectItem.setId(employee.getObjectID());
                    positionsSelectItem.setName(employee.getName());
                    if (employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null) {
                        positionsSelectItem.setEmployeeNumber(employee.getProfile().getEmployeeCode());
                    }
                    positionsSelectItem.setStartDate(projectEmployee.getContractStartDate() != null ? new DateNonConvertable(projectEmployee.getContractStartDate()) : null);
                    positionsSelectItem.setEndDate(projectEmployee.getContractEndDate() != null ? new DateNonConvertable(projectEmployee.getContractEndDate()) : null);

                    if (projectEmployee.getPosition() != null) {
                        positionsSelectItem.setPositionName(projectEmployee.getPosition().getName());

                        EdsProjectPosition projectPosition = map.computeIfAbsent(projectEmployee.getPosition().getObjectID(), k -> this.projectPositionManager.getProjectPosition(project.getObjectID(), projectEmployee.getPosition().getObjectID()));

                        if (projectPosition.getRPC() != null) {
                            positionsSelectItem.setProjectPosition(projectPosition.getRPC());
                        }
                    }

                    positionsSelectItemList.add(positionsSelectItem);
                }

                projectViewItem.setProjectEmployees(positionsSelectItemList.toArray(new PositionsSelectItem[]{}));
            }
        } else {
            List<ProjectMember> members = this.projectEmployeeManager.getProjectEmployeesInfo(project.getObjectID());
            if (members != null) {
                PositionsSelectItem[] projectEmployees = new PositionsSelectItem[members.size()];
                int j = 0;
                for (ProjectMember member : members) {
                    projectEmployees[j] = new PositionsSelectItem();
                    projectEmployees[j].setId(member.getId());
                    projectEmployees[j].setName(member.getName());
                    projectEmployees[j].setEmployeeNumber(member.getEmployeeNumber());
                    projectEmployees[j].setTime(member.getEstimatedTime());
                    projectEmployees[j].setTimeSpent(member.getTimeSpent());
                    projectEmployees[j].setActualTime(member.getActualTime());
                    projectEmployees[j].setPositionName(member.getPosititon());
                    projectEmployees[j].setDepartmentName(member.getTeamName());
                    projectEmployees[j].setEmployeeId(member.getProjectEmployeeId());
                    Float percent = 0f;
                    if (settings != null && settings.isAutomatic()) {
                        if (this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                            percent = 100 * (member.getEstimatedTime().floatValue() != 0 ? member.getActualTime().floatValue() / member.getEstimatedTime().floatValue() : 0);
                        } else if (member.getEstimatedTime() != null && member.getEstimatedTime() != 0 && member.getActualTime() != null && member.getActualTime() != 0) {
                            percent = 0.0f;
                            if (member.getEstimatedTime() != 0) {
                                percent = 100 * member.getActualTime().floatValue() / member.getEstimatedTime().floatValue();
                                /*getProjectEmployeePercentCompletedNewLogic(project, member.getId());*/
                            }
                        }
                    } else {
                        percent = (member.getTaskCount() != null && member.getTaskCount() != 0) ? BigDecimal.valueOf(member.getPercentSum() / member.getTaskCount()).setScale(2, RoundingMode.HALF_UP).floatValue() : 0f;
                    }
                    projectEmployees[j].setPercent((percent > 100f && !this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) ? 100f : percent);
                    j++;
                }
                projectViewItem.setProjectEmployees(projectEmployees);
            }
        }

        if (project.getProjectLocation() != null) {
            EdsLocation location = project.getProjectLocation();
            projectViewItem.setProjectLocation(location.getCountry().getName() + "," + location.getCity());
            projectViewItem.setLocationID(location.getObjectID());
        } else {
            projectViewItem.setProjectLocation(this.commonLocalizer.localize("notAvailable", "N/A"));
        }
        FileResource[] attachments = this.getProjectAttachments(project);
        if (attachments.length > 0) {
            projectViewItem.setProjectAttachments(attachments);
        } else {
            projectViewItem.setProjectAttachments(new FileResource[0]);
        }

        if (project != null) {
            Set<EdsProjectCustomItemTable> itemTables = project.getItemTables();

            HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

            if (itemTables != null || itemTables.size() > 0) {

                for (EdsProjectCustomItemTable itemTable : itemTables) {
                    CustomTableRpc rpc = itemTable.getRpc();

                    rpc.setItemCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                            commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.ProjectItemTable, rpc.getUuid())));

                    map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                }
                projectViewItem.setCustomTableItems(map);
            }
            HashMap<String, ArrayList<CustomTableRpc>> tableItems = projectViewItem.getCustomTableItems();


            for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
            }
        }
        projectViewItem.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_PROJECT, project.getObjectID())));
        projectViewItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(project.getProjectCustomFields(), this.commonService.getCompanyCustomFields(ViewName.Project)));
        projectViewItem.setSupplier(user.hasRole(Constants.SUPPLIER));
        return projectViewItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedList<WfmTreeItem> getTeams() {
        return ListUtils.createTreeItemArray(this.departmentManager.list(),
                new WfmTreeItemFactory<EdsDepartment>() {
                    public WfmTreeItem createItem(EdsDepartment o) {
                        WfmTreeItem result = new WfmTreeItem(o.getObjectID(), o
                                .getName());
                        result.setChildren(true);
                        return result;

                    }
                });

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getClients() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(EdsRole.DR);
        SelectItem[] clients = this.clientManager.list(fp);
        Arrays.sort(clients, Comparator.comparing(SelectItem::getName));
        return clients;

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProjectStatuses() {
        EdsReference allSt = this.referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ALL);
        List<EdsReference> statuses = this.referenceManager.listReferences(EdsProject.PROJECT_STATUS);
        EdsUser currentUser = this.userManager.getUser();
        boolean isGermano = (currentUser.getCompany().getObjectID().equals(8032) || currentUser.getCompany().getObjectID().equals(20738));
        if (isGermano && !currentUser.hasRole(this.roleManager.get(EdsRole.ADMIN))) {
            EdsReference completedST = this.referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.COMPLETED);
            statuses.remove(completedST);
        }
        statuses.remove(allSt);
        return this.reference2SelectItem(statuses);
    }

    private SelectItem[] reference2SelectItem(List<EdsReference> references) {
        SelectItem[] selectItems = new SelectItem[references.size()];
        int i = 0;
        for (EdsReference status : references) {
            selectItems[i] = new SelectItem();
            selectItems[i].setId(status.getObjectID());
            selectItems[i].setDescription(status.getCode());
            String value = this.referenceWfmMessageSource.localize(status.getCode(), status.getName());
            selectItems[i].setName(value);
            i++;
        }
        return selectItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EditProject getProjectForEdit(Integer projectId, Date date, Integer clientID) {
        EditProject projectItem = new EditProject();
        projectItem.setNumberData(this.generateProjectNumber(date, clientID, projectId));

        EdsProject project = this.projectManager.get(projectId);
        EdsUser user = this.employeeManager.getUser();

        if (project != null) {
            projectItem.setObjectId(project.getObjectID());
            projectItem.getNumberData().setNumberString(project.getNumber());
            projectItem.getNumberData().setIntNumber(project.getIntNumber());
            projectItem.setReminders(this.itemReminderManager.getReminders(projectId, Constants.PROJECT_REMINDER));

            projectItem.setNumber(project.getNumber());
            projectItem.setName(project.getName());
            projectItem.setDescription(project.getDescription());
            projectItem.setComplete(project.getPercent() != null ? project.getPercent() + " %" : null);
            projectItem.setEndDate(project.getEndDate() != null ? new Date(project.getEndDate().getTime()) : null);
            projectItem.setManagerName(project.getManager() != null ? project.getManager().getFullName() : null);
            projectItem.setManagerId(project.getManager() != null ? project.getManager().getObjectID() : null);
            projectItem.setBackupManagerIDs(project.getBackupManagerIDs());
            projectItem.setEmployeeAssignment(project.getEmployeeAssignment());
            projectItem.setBillable(project.getBillable());

            StringBuilder names = new StringBuilder();
            for (EdsEmployee backupManager : project.getBackupManagers()) {
                if (names.toString().equals("")) {
                    names.append(backupManager.getFullName());
                } else {
                    names.append(", ").append(backupManager.getFullName());
                }
            }
            projectItem.setBackupManagerName(names.toString());
            List<CheckInLocationItem> locations = new ArrayList<>();
            for (EdsCheckInLocation checkInLocation : project.getCheckInLocations()) {
                locations.add(new CheckInLocationItem(checkInLocation.getObjectID(),checkInLocation.getLatitude().toString(), checkInLocation.getLongitude().toString(), checkInLocation.getRadius().toString()));
            }
            projectItem.setCheckInLocations(locations);
            if (project.getClient() != null && !project.getClient().isDeleted()) {
                projectItem.setClientId(project.getClient().getObjectID());
                projectItem.setClientName(project.getClient().getName());
                projectItem.setClientContactEmail(project.getClient().getEmail());
                projectItem.setClientContactId(project.getClient().getObjectID());

            }
            if (project.getClients() != null && !project.getClients().isEmpty()) {
                ArrayList<SelectItem> clients = new ArrayList<>();
                for (EdsCrmAccount client : project.getClients()) {
                    clients.add(client.getAsSelectItem());
                }
                projectItem.setClients(clients.toArray(new SelectItem[]{}));
            }
            projectItem.setStatusId(project.getStatus() != null ? project.getStatus().getObjectID() : null);
            projectItem.setStartDate(project.getStartDate() != null ? new Date(project.getStartDate().getTime()) : null);
            projectItem.setDueDate(project.getDueDate() != null ? new Date(project.getDueDate().getTime()) : null);
            projectItem.setLastUpdate(project.getLastUpdateTime() != null ? new Date(project.getLastUpdateTime().getTime()) : null);
            projectItem.setLocationId(project.getProjectLocation() != null ? project.getProjectLocation().getObjectID() : null);
            projectItem.setParentId(project.getParent() != null ? project.getParent().getObjectID() : null);
            ArrayList<CompanyCustomFieldItem> customFieldsItems = this.commonService.getCompanyCustomFields(ViewName.Project);
            projectItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(project.getProjectCustomFields(), customFieldsItems));
            projectItem.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_PROJECT, project.getObjectID())));
            projectItem.setHoursSpent(project.getTimeSpentHM());
            projectItem.setTaskCount((long) project.getTasks().size());
            projectItem.setCrmActivityProject(project.isCrmActivityProject());
            projectItem.setDefaultProject(user.getCompany().getDefaultProject() != null && user.getCompany().getDefaultProject().getObjectID().equals(projectId));

            // Sets editable if user is PM or Project Backup Manager or Company Director, or Company Administrator
            if (!user.isClientContact() && (project.getManager().getObjectID().equals(user.getObjectID()) || (project.isUserBackupManager(user.getObjectID()))
                    || user.hasRole(this.roleManager.get(EdsRole.DR)) || user.hasRole(this.roleManager.get(EdsRole.ADMIN)))) {
                projectItem.setPermission(Constants.EDIT);
            } else {
                projectItem.setPermission(Constants.READ);
            }

            if (EmployeeAssignmentEnum.BY_POSITION.equals(project.getEmployeeAssignment())) {
                projectItem.setProjectPositions(this.getProjectPositions(project.getObjectID()));
            }

            boolean isEmployeeAssignmentEnable = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE);
            if (isEmployeeAssignmentEnable) {
                projectItem.setManagers(this.getManagers());
            }
        }

        if (project != null) {
            Set<EdsProjectCustomItemTable> itemTables = project.getItemTables();

            HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

            if (itemTables != null || itemTables.size() > 0) {

                for (EdsProjectCustomItemTable itemTable : itemTables) {
                    CustomTableRpc rpc = itemTable.getRpc();

                    rpc.setItemCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                            commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.ProjectItemTable, rpc.getUuid())));

                    map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                }
                projectItem.setCustomTableItems(map);
            }
            HashMap<String, ArrayList<CustomTableRpc>> tableItems = projectItem.getCustomTableItems();


            for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
            }
        }
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsProject.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(projectId);
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Get project for edit");

        return projectItem;
    }

    @Transactional
    public EditProject getProjectDetailsFrom(String projectFrom, Integer projectFromID) {
        EditProject editProject = new EditProject();
        if (projectFrom != null && projectFromID != null) {
            if (Constants.OPPORTUNITY.equals(projectFrom)) {
                EdsOpportunity opportunity = this.jpaTemplate.find(EdsOpportunity.class, projectFromID);
                if (opportunity != null) {
                    editProject.setName(opportunity.getName());
                    if (opportunity.getAssignee() != null) {
                        editProject.setManagerId(opportunity.getAssignee().getObjectID());
                        editProject.setManagerName(opportunity.getAssignee().getFullName());
                    }
                    EdsCrmAccount crmAccount = opportunity.getCrmAccount();
                    if (crmAccount != null) {
                        crmAccount.addAccountType(this.referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
                        this.crmAccountManager.update(crmAccount, true);
                        editProject.setClientId(opportunity.getCrmAccount().getObjectID());
                        editProject.setClientName(opportunity.getCrmAccount().getName());
                    }
                }
            }
        }
        return editProject;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] searchClientsByProjectId(Integer projectId, String searchKey) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(EdsRole.DR);
        fp.setSearchKey(searchKey);
        fp.setLookUp(true);
        fp.setProjectId(projectId);
        return this.clientManager.list(fp);
    }

    @Transactional
    public void updateProject(EditProject editProject) throws NumberExistingException {
        EdsProject project = this.projectManager.get(editProject.getObjectId());
        NumberData numberData = editProject.getNumberData();

        boolean isProjectNameChanged = project.getName() != null && editProject.getName() != null && !project.getName().trim().equals(editProject.getName().trim());

        if ("".equals(project.getNumber())) {
            project.setNumber(null);
        }

        if (project.getNumber() != null && (numberData == null || numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim()))) {
            throw new NumberExistingException("Incorrect project number format.");
        }

        try {
            Date timer = new Date();
            EdsUser user = this.employeeManager.getUser();
            EdsCompany company = user.getCompany();

            project.enableTaskChangeListener(new EdsProject.ChangeListener() {
                @Override
                public void onManagerChange(EdsEmployee manager) {
                    if (manager != null) {
                        ProjectServiceImpl.this.roleManager.addRole(manager, Constants.PM);
                        ProjectServiceImpl.this.baseEventPostProcessor.registerEvent(ProjectManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
                    }
                }

                @Override
                public void onBackupManagerChange(EdsEmployee backupManager) {
                    if (backupManager != null) {
                        ProjectServiceImpl.this.roleManager.addRole(backupManager, Constants.PM);
                        ProjectServiceImpl.this.baseEventPostProcessor.registerEvent(ProjectBackupManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user, backupManager);
                    }
                }

                @Override
                public void onClientChange(EdsCrmAccount client) {

                }

                @Override
                public void onNameChange(String name) {

                }
            });
            project.clear();
            project.setObjectID(editProject.getObjectId());

            if (numberData == null || numberData.getNumberString() == null || numberData.getNumberString().isEmpty() || this.projectManager.isProjectNumberExists(numberData.getNumberString(), editProject.getObjectId())) {
                numberData = this.generateProjectNumber(editProject.getStartDate(), editProject.getClientId(), null);
            }
            String oldprintnumber = project.getIntNumber() != null ? project.getIntNumber().toString() : "0";
            String currentnumber = numberData.getIntNumber() != null ? numberData.getIntNumber().toString() : "0";
            if (numberData != null) {
                project.setNumber("".equals(numberData.getNumberString()) ? null : numberData.getNumberString());
                project.setSavedNumberFormula(editProject.getNumberData().getSavedNumberFormula());
                project.setIntNumber(numberData.getIntNumber());
            }
            if (numberData.getIntNumber() != null && !"".equals(numberData.getIntNumber())) {
                EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
                if (settings != null && !oldprintnumber.equals(currentnumber) && (settings.getProjectLastIntNumber() == null || numberData.getIntNumber() >= settings.getProjectLastIntNumber())) {
                    settings.setProjectLastIntNumber(numberData.getIntNumber() + 1);
                    this.numberingSettingsManager.createOrUpdate(settings);
                }
            }

            project.setName(editProject.getName());
            project.setDescription(editProject.getDescription());
            project.setEndDate(editProject.getDueDate());
            project.setDueDate(editProject.getDueDate());
            project.setStartDate(editProject.getStartDate());
            project.setLastUpdateTime(new Date());
            project.setUpdater(user);
            project.setBillable(editProject.isBillable());

            if (editProject.getCheckInLocations().isEmpty() && project.getCheckInLocations() != null && !project.getCheckInLocations().isEmpty()) {
                for (EdsCheckInLocation checkInLocation : project.getCheckInLocations()) {
                    checkInLocationManager.delete(checkInLocation);
                }
            }
            Set<EdsCheckInLocation> checkInLocations = new HashSet<>();
            for (CheckInLocationItem checkInLocation : editProject.getCheckInLocations()) {
                EdsCheckInLocation edsCheckInLocation = checkInLocation.getId() != null ? checkInLocationManager.get(checkInLocation.getId()) : new EdsCheckInLocation();
                edsCheckInLocation.setLatitude(Double.valueOf(checkInLocation.getLatitude()));
                edsCheckInLocation.setLongitude(Double.valueOf(checkInLocation.getLongitude()));
                edsCheckInLocation.setRadius(Integer.valueOf(checkInLocation.getRadius()));
                edsCheckInLocation.setProject(project);
                checkInLocations.add(edsCheckInLocation);
            }

            project.setCheckInLocations(checkInLocations);
            if (editProject.getNotes() != null) {
                for (HistoryListItem note : editProject.getNotes()) {
                    note.setRelatedToId(F_PROJECT_ROOT);
                    note.setRelatedId(project.getObjectID());
                    crmServiceLocal.saveCrmNote(Constants.PROJECT, project.getObjectID(), note);
                }
            }

            if (editProject.getParentId() != null && editProject.getParentId() != 0) {
                project.setParent(this.projectManager.get(editProject.getParentId()));
            }
            boolean newMemberAdded = false;
            boolean memberDeleted = false;
            boolean managerChanged = false;
            boolean backupManagerChanged = true;
            if (editProject.getManagerId() != null && editProject.getManagerId() != 0) {
                if (!project.getManager().getObjectID().equals(editProject.getManagerId())) {
                    EdsEmployee manager = this.employeeManager.get(editProject.getManagerId());
                    project.setManager(manager);
                    managerChanged = true;
                }
            }

            List<Integer> backupManagersBeforeEdit = project.getBackupManagerIDs();
            this.assignBackupMangers(project, editProject.getBackupManagerIDs());
            if (new HashSet<>(backupManagersBeforeEdit).containsAll(project.getBackupManagerIDs()) && project.getBackupManagerIDs().containsAll(backupManagersBeforeEdit)) {
                backupManagerChanged = false;
            }

            for (HashMap.Entry<String, ArrayList<CustomTableRpc>> map : editProject.getCustomTableItems().entrySet()) {
                List<CustomTableRpc> values = map.getValue();

                for (CustomTableRpc rpc : values) {
                    EdsProjectCustomItemTable customItemTable = new EdsProjectCustomItemTable();
                    customItemTable.setUuid(map.getKey());
                    customItemTable.setName(rpc.getItemName());
                    customItemTable.setDescription(rpc.getDescription());
                    customItemTable.setCustomFields(saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()));
                    customItemTable.setProject(project);
                    projectItemTableManager.createOrUpdate(customItemTable);

                }
            }


            if (editProject.getClientId() != null && editProject.getClientId() != 0) {
                project.setClient(this.clientManager.get(editProject.getClientId()));
            } else {
                //if Edit Project setClientID null clear project client ID;
                project.setClient(null);
            }
            project.getClients().clear();
            if (editProject.getClients() != null) {
                editProject.getClients();
                for (SelectItem client : editProject.getClients()) {
                    project.getClients().add(this.clientManager.get(client.getId()));
                }
            }
            if (editProject.getStatusId() != null && editProject.getStatusId() != 0) {
                EdsReference status = this.referenceManager.get(editProject.getStatusId());
                project.setStatus(status);
                if (status != null && status.getCode().equals(EdsProject.COMPLETED)) {
                    project.setCompletedDate(new Date());
                } else {
                    project.setCompletedDate(null);
                }
                if (editProject.isChangeTaskStatus()) {
                    this.baseEventPostProcessor.registerEvent(ProjectStatusEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
                }
            }
            if (editProject.getLocationId() != null && editProject.getLocationId() != 0) {
                project.setProjectLocation(this.locationManager.get(editProject.getLocationId()));
            }

            EdsProjectCustomFields edsProjectCustomFields = this.createProjectCustomFields(editProject.getCustomFieldItems());
            project.setProjectCustomFields(edsProjectCustomFields);

            HashMap<Integer, EdsProjectEmployee> peMap = this.projectEmployeeManager.getProjectEmployeesAsMap(project);

            if (editProject.getMembers() != null && editProject.getMembers().length > 0 && (editProject.getMembers()[0].getId() != null)) {

                List<ProjectMember> newMembers = new ArrayList<>();

                for (int j = 0; j < editProject.getMembers().length; j++) {

                    ProjectMember member = editProject.getMembers()[j];

                    if (peMap.get(member.getProjectEmployeeId()) == null) {
                        newMembers.add(member);
                    } else {
                        EdsProjectEmployee pemployee = peMap.get(member.getProjectEmployeeId());
                        peMap.remove(member.getProjectEmployeeId());

                        if (!pemployee.getWageRate().equals(member.getWageRate())
                                || !pemployee.getClientChargeRate().equals(member.getClientChargeRate())
                                || (member.getWorkloadPercentage() != null && !pemployee.getWorkloadPercentage().equals(member.getWorkloadPercentage())) ||
                                (pemployee.getWageClientRatesHistory() == null || pemployee.getWageClientRatesHistory().size() == 0)) {

                            if (member.getWageRate() != null) {
                                pemployee.setWageRate(member.getWageRate());
                            }
                            if (member.getClientChargeRate() != null) {
                                pemployee.setClientChargeRate(member.getClientChargeRate());
                            }
                            if (member.getWorkloadPercentage() != null) {
                                pemployee.setWorkloadPercentage(member.getWorkloadPercentage());
                            }

                            EdsProjectEmployeeWageClientRateHistory hist = new EdsProjectEmployeeWageClientRateHistory();

                            hist.setChangeDate(company.getCompanyDate());
                            hist.setWageRate(pemployee.getWageRate());
                            hist.setClientChargeRate(pemployee.getClientChargeRate());
                            hist.setWorkloadPercentage(pemployee.getWorkloadPercentage());
                            hist.setProjectEmployee(pemployee);

                            this.projectManager.updateEmployeeWageClientRateHistorybyDate(hist);
                            this.projectManager.updateTimesheetWageRates(hist.getWageRate(), hist.getClientChargeRate(), member.getId(), project.getObjectID(), hist.getChangeDate(), null);
                            this.projectManager.updateProjectEmployeeOb(hist.getWageRate(), hist.getClientChargeRate(), member.getId(), project.getObjectID());
                        }

                        if (member.getPositionId() != null && (pemployee.getPosition() == null || !pemployee.getPosition().getObjectID().equals(member.getPositionId()))) {
                            pemployee.setPosition(this.positionManager.get(member.getPositionId()));
                        }
                        if (member.getContractEnd() != null && !member.getContractEnd().getNonConvertedDate().equals(pemployee.getContractEndDate())) {
                            pemployee.setContractEndDate(member.getContractEnd().getNonConvertedDate());
                        }
                        if (member.getContractStart() != null && !member.getContractStart().getNonConvertedDate().equals(pemployee.getContractStartDate())) {
                            pemployee.setContractStartDate(member.getContractStart().getNonConvertedDate());
                        }

                        this.projectEmployeeManager.update(pemployee);
                    }
                }

                //adding new employee to the project
                if (!newMembers.isEmpty()) {
                    newMemberAdded = true;
                    if (editProject.isCopyNewEmployeesToProjectTasks()) {
                        timer = new Date();
                        this.taskServiceLocal.addNewProjectMembersAndAssignTasks(editProject.getObjectId(), newMembers.toArray(new ProjectMember[]{}));
                        System.out.println("---->> Robert Members Took: " + ((new Date()).getTime() - timer.getTime()));
                    } else {
                        this.addMembers(editProject.getObjectId(), newMembers.toArray(new ProjectMember[]{}));
                    }
                }

                //deleting old employees
                if (!peMap.isEmpty()) {
                    memberDeleted = true;

                    for (EdsProjectEmployee pe : peMap.values()) {
                        pe.setDeleted(true);

                        this.baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, pe, user);
                        this.deleteTaskAssignees(pe);
                    }
                }

                System.out.println("-->> 1. Project Members Took: " + ((new Date()).getTime() - timer.getTime()));

                if (EmployeeAssignmentEnum.BY_POSITION.equals(editProject.getEmployeeAssignment()) && editProject.getProjectPositions() != null) {
                    this.updateProjectPositions(editProject, project, user);
                }
            }
            if (editProject.getProjectPositions() != null && editProject.getProjectPositions().length > 0 && editProject.getMembers() != null && editProject.getMembers().length == 0) {
                List<EdsProjectEmployee> projectEmployee = this.projectManager.getProjectInvolvedEmployees(this.projectManager.get(editProject.getObjectId()));

                this.updateProjectPositions(editProject, project, user);

                for (EdsProjectEmployee employees : projectEmployee) {
                    employees.setDeleted(true);
                }
            }

            if (editProject.getProjectPositions() != null && editProject.getProjectPositions().length == 0) {
                List<EdsProjectPosition> edsProjectPositions = this.projectManager.getProjectPositions(editProject.getObjectId());
                for (EdsProjectPosition projectPosition : edsProjectPositions) {
                    projectPosition.setDeleted(true);
                    projectPosition.setUpdatedDate(new Date());
                    projectPosition.setUpdater(user);
                }
            }

            if (memberDeleted || managerChanged || backupManagerChanged || (newMemberAdded && editProject.isCopyNewEmployeesToProjectTasks())) {
                List<EdsTask> tasks = this.taskManager.getProjectTasks(project);
                int k = 0;
                for (EdsTask itask : tasks) {
                    EdsTask task = this.taskManager.get(itask.getObjectID());
                    this.taskRbacManager.addRbacEntries(task);
                    if (!project.isSolrSensitiveFieldsChanged()) {// if project changed some filed projectsolrsensitivedatachangelistener will reindex all task anyway
                        // so there is no need to reindex it
                        this.baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
                    }

                    if (k++ >= 10) {
                        k = 0;
                        this.taskRbacManager.flushAndClear();
                    }
                }
            }

            if (isProjectNameChanged) {
                this.baseEventPostProcessor.registerEvent(AccountingProjectSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
            }

            //Reindex to projectFolderRbac
            this.reIndexProjectDocuments(project, user);
            this.baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
            if (project.isSolrSensitiveFieldsChanged()) {
                this.baseEventPostProcessor.registerEvent(ProjectSolrSensitiveDataChangeListener.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
            }

            if (project.getObjectID() != null && editProject.isRelationChanged()) {
                this.allInOneService.saveRelations(RelationItem.TYPE_PROJECT, project.getObjectID(),
                        project.getName(), editProject.getRelations());
            }

            this.saveProjectReminder(editProject.getObjectId(), user.getCompany(), editProject.getReminders());

            this.updateProjectStatus(project);

            if (editProject.getAttachments() != null && editProject.getAttachments().length > 0) {
                //Create a new folder related to EdsProject.
                this.commonServiceLocal.createProjectFolder(project.getObjectID());

                // ---- with Document Management logic ---------------------------
                this.saveProjectAttachments(editProject.getAttachments(), project);
            }

            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
            workflowEvent.setEntityType(RelationItem.TYPE_PROJECT);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

    }

    private void updateProjectPositions(EditProject editProject, EdsProject project, EdsUser user) {
        List<EdsProjectPosition> edsProjectPositions = this.projectManager.getProjectPositions(editProject.getObjectId());
        ArrayList<ProjectPosition> newProjectPositions = new ArrayList<>();

        for (ProjectPosition projectPosition : editProject.getProjectPositions()) {
            boolean isNewPosition = true;

            for (EdsProjectPosition edsProjectPosition : edsProjectPositions) {
                if (edsProjectPosition.getPosition().getObjectID().equals(projectPosition.getPositionId())) {
                    isNewPosition = false;
                    edsProjectPosition.setUnitPrice(projectPosition.getUnitPrice());
                    edsProjectPosition.setOvertimeRate(projectPosition.getOvertimeRate());
                    edsProjectPosition.setWeekendOvertimeRate(projectPosition.getWeekendOvertimeRate());
                    edsProjectPosition.setHolidayOvertimeRate(projectPosition.getHolidayOvertimeRate());
                    edsProjectPosition.setContractStartDate(projectPosition.getContractStart().getNonConvertedDate());
                    edsProjectPosition.setContractEndDate(projectPosition.getContractEnd() != null ? projectPosition.getContractEnd().getNonConvertedDate() : null);
                    edsProjectPosition.setNumberOfWorker(projectPosition.getNumberOfWorker());
                    edsProjectPosition.setUpdatedDate(new Date());
                    edsProjectPosition.setUpdater(user);
                    break;
                }
            }

            if (isNewPosition) {
                newProjectPositions.add(projectPosition);
            }
        }

        for (EdsProjectPosition edsProjectPosition : edsProjectPositions) {
            boolean isExistingPosition = false;

            for (ProjectPosition projectPosition : editProject.getProjectPositions()) {
                if (edsProjectPosition.getPosition().getObjectID().equals(projectPosition.getPositionId())) {
                    isExistingPosition = true;
                    break;
                }
            }

            if (!isExistingPosition) {
                edsProjectPosition.setDeleted(true);
                edsProjectPosition.setUpdatedDate(new Date());
                edsProjectPosition.setUpdater(user);
            }
        }

        for (ProjectPosition projectPosition : newProjectPositions) {
            EdsProjectPosition pp = new EdsProjectPosition();
            pp.setPosition(this.positionManager.get(projectPosition.getPositionId()));
            pp.setContractStartDate(projectPosition.getContractStart().getNonConvertedDate());
            pp.setContractEndDate(projectPosition.getContractEnd() != null ? projectPosition.getContractEnd().getNonConvertedDate() : null);
            pp.setUnitPrice(projectPosition.getUnitPrice());
            pp.setOvertimeRate(projectPosition.getOvertimeRate());
            pp.setWeekendOvertimeRate(projectPosition.getWeekendOvertimeRate());
            pp.setHolidayOvertimeRate(projectPosition.getHolidayOvertimeRate());
            pp.setNumberOfWorker(projectPosition.getNumberOfWorker());
            pp.setProject(project);
            pp.setCreationDate(new Date());
            pp.setCreator(user);
            project.getProjectPositions().add(pp);

            if (project.getContractId() != null) {
                EdsContract edsContract = this.contractManager.get(project.getContractId());
                if (edsContract != null) {
                    boolean isExistingPosition = false;
                    if (edsContract.getProjectPositions() != null && edsContract.getProjectPositions().size() > 0) {
                        for (EdsProjectPosition contractPosition : edsContract.getProjectPositions()) {
                            if (contractPosition.getPosition() != null
                                    && contractPosition.getPosition().getObjectID().equals(projectPosition.getPositionId())) {
                                isExistingPosition = true;
                                break;
                            }
                        }
                    }
                    if (!isExistingPosition) {
                        pp.setContract(edsContract);
                        edsContract.getProjectPositions().add(pp);
                    }
                }
            }
        }
    }

    private void updateProjectStatus(EdsProject project) {
        EdsReference notStartedReference = this.referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
        EdsReference ongoingReference = this.referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ONGOING);
        if (project != null) {
            EdsProject parentProject = project.getParent();
            if (parentProject != null && parentProject.getStatus().equals(notStartedReference)) {
                parentProject.setStatus(ongoingReference);
                parentProject.setCompletedDate(null);
                this.projectManager.update(parentProject);
                this.baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, parentProject, this.roleManager.getUser());
            }
        }
    }

    private void reIndexProjectDocuments(EdsProject project, EdsUser user) {
        this.baseEventPostProcessor.registerEvent(ProjectDocumentsReIndexEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, user);
    }

    /**
     * Related Employee Tasks delete
     *
     * @param pEmpl
     */
    private void deleteTaskAssignees(EdsProjectEmployee pEmpl) {
        List<EdsEmployeeTask> empTasks = this.employeeTaskManager.getEmployeeTask(pEmpl);
        for (EdsEmployeeTask emplTask : empTasks) {
            emplTask.setDeleted(true);

            //clear task from the employee items
            //this history for the calculation PROJECT COST
            EdsTask task = emplTask.getTask();

            task.setEstimatedTime(task.getEstimatedTime() - emplTask.getEstimatedTime());
            task.setChangedCalculationFields(true);
            task.setLastUpdateTime(new Date());
        }
    }

    public void deleteAttachment(Integer attachmentId) {
        EdsAttachment projectAttachment = this.attachmentManager.get(attachmentId);
        this.attachmentManager.delete(projectAttachment);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileResource[] getProjectAttachments(EdsProject project) {
        List<FileResource> taskAttachments = this.attachmentUtilsManager.getAttachments(Constants.F_PROJECT, project.getObjectID(), project.getObjectID());
        return taskAttachments.toArray(new FileResource[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileResource[] getProjectAttachments(Integer projectID) {
        EdsProject project = this.projectManager.get(projectID);
        if (project != null) {
            return this.getProjectAttachments(project);
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProjectsList(Integer viewAs) {
        ListingFilterParameter fp = new ListingFilterParameter(null, null, null, null, viewAs);
        List<EdsProject> projects = this.projectManager.list(fp);
        SelectItem[] result = new SelectItem[projects.size()];
        int i = 0;
        for (EdsProject project : projects) {
            result[i] = new SelectItem();
            result[i].setId(project.getObjectID());
            result[i].setName(project.getName());
            i++;
        }
        return result;
    }

    public ProjectMember[] getProjectEmployees(Integer companyID, Integer projectID) {
        return this.getProjectEmployees(projectID); // for GanttChart / MultiSchema
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectMember[] getProjectEmployees(Integer projectID) {
        List<EdsProjectEmployee> projectEmployees = this.projectManager.getEmployeesByProject(projectID);
        List<ProjectMember> proMembers = new ArrayList<>();
        Double[] projectCostAndTimeSpent;
        for (EdsProjectEmployee projectEmployee : projectEmployees) {
            if (projectEmployee.getEmployeeDepartment() != null) {
                EdsEmployee empl = projectEmployee.getEmployeeDepartment().getEmployee();
                if (empl != null) {
                    projectCostAndTimeSpent = this.timeSheetManager.getProjectCostAndTimeSpent(projectID, empl.getObjectID());
                    String teamName = "";
                    if (empl.getEmployeeTeam() != null && empl.getEmployeeTeam().getTeam() != null) {
                        teamName = empl.getEmployeeTeam().getTeam().getName();
                    }
                    ProjectMember projectMember = new ProjectMember(empl.getObjectID(), empl.getName(), teamName);
                    projectMember.setProjectEmployeeId(projectEmployee.getObjectID());
                    projectMember.setPosititon(empl.getPosition() != null ? empl.getPosition().getName() : "");
                    projectMember.setTimeSpent(projectCostAndTimeSpent != null ? projectCostAndTimeSpent[Constants.PROJECT_HOURS_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_HOURS_SPENT].intValue() : 0 : 0);
                    projectMember.setEstimatedTime(projectCostAndTimeSpent != null ? projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_TIME_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_TIME_SPENT].intValue() : 0 : 0);
                    projectMember.setActualTime(projectCostAndTimeSpent != null ? projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_TIME_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_TIME_SPENT].intValue() : 0 : 0);
                    proMembers.add(projectMember);
                }
            }
        }

        proMembers.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return proMembers.toArray(new ProjectMember[]{});
    }

    public ProjectPosition[] getProjectPositions(Integer projectID) {
        ArrayList<ProjectPosition> list = new ArrayList<>();
        Map<Integer, ArrayList<ProjectMember>> peMap = new HashMap<>();
        List<EdsProjectEmployee> pemployees = this.projectManager.getProjectInvolvedEmployees(this.projectManager.get(projectID));

        if (pemployees != null && !pemployees.isEmpty()) {
            for (EdsProjectEmployee item : pemployees) {
                Integer positionID = item.getPosition() != null ? item.getPosition().getObjectID() : 0;

                ProjectMember projectMember = new ProjectMember();
                projectMember.setId(item.getEmployeeDepartment().getEmployee().getObjectID());
                projectMember.setName(item.getEmployeeDepartment().getEmployee().getName());
                projectMember.setPositionId(positionID);
                projectMember.setPosititon(item.getPosition() != null ? item.getPosition().getName() : "");
                projectMember.setWageRate(item.getWageRate());
                projectMember.setClientChargeRate(item.getClientChargeRate());
                projectMember.setContractStart(item.getContractStartDate() != null ? new DateNonConvertable(item.getContractStartDate()) : null);
                projectMember.setContractEnd(item.getContractEndDate() != null ? new DateNonConvertable(item.getContractEndDate()) : null);
                projectMember.setProjectEmployeeId(item.getObjectID());
                projectMember.setCreateDate(item.getCreationdate());

                if (item.getEmployeeDepartment().getEmployee() != null) {
                    projectMember.setEmployeeNumber(item.getEmployeeDepartment().getEmployee().getProfile().getEmployeeCode());
                }

                if (peMap.get(positionID) == null) {
                    ArrayList<ProjectMember> al = new ArrayList<>();
                    al.add(projectMember);
                    peMap.put(positionID, al);
                } else {
                    peMap.get(positionID).add(projectMember);
                }
            }
        }

        List<EdsProjectPosition> projectPositions = this.projectManager.getProjectPositions(projectID);

        if (projectPositions != null && !projectPositions.isEmpty()) {
            for (EdsProjectPosition pp : projectPositions) {
                ProjectPosition item = new ProjectPosition();
                item.setObjectID(pp.getObjectID());
                item.setPositionId(pp.getPosition().getObjectID());
                item.setContractStart(new DateNonConvertable(pp.getContractStartDate()));
                item.setContractEnd(pp.getContractEndDate() != null ? new DateNonConvertable(pp.getContractEndDate()) : null);
                item.setUnitPrice(pp.getUnitPrice());
                item.setPriceType(pp.getPriceType());
                item.setOvertimeRate(pp.getOvertimeRate());
                item.setWeekendOvertimeRate(pp.getWeekendOvertimeRate());
                item.setHolidayOvertimeRate(pp.getHolidayOvertimeRate());
                item.setNumberOfWorker(pp.getNumberOfWorker());

                if (peMap.get(item.getPositionId()) != null) {
                    item.setMembers(peMap.get(item.getPositionId()).toArray(new ProjectMember[]{}));
                }
                list.add(item);
            }
        }

        return list.toArray(new ProjectPosition[]{});
    }

    @Override
    public ListResult<ContractListItem> getContractList(ListingFilterParameter fp) {
        int totalCount = this.contractManager.listCount(fp);
        ArrayList<ContractListItem> contractList = new ArrayList<>();
        if (totalCount > 0) {
            long startedAt = System.currentTimeMillis();
            System.out.println("Get Contract list from database started at:===========================" + new Date() + "===========================");
            List<Object[]> edsContracts = this.contractManager.getList(fp);
            System.out.println("It took to get Case List from database:===========================" + (System.currentTimeMillis() - startedAt) + "===========================");
            long started = System.currentTimeMillis();
            System.out.println("Fill Contract list with data started at:===========================" + new Date() + "===========================");
            ArrayList<CompanyCustomFieldItem> itemArrayList = commonService.getCompanyCustomFields(ViewName.Contract);
            HashMap<Integer, EdsContract> contracts = new HashMap<>();
            edsContracts.forEach(t -> contracts.put((Integer) t[0], this.contractManager.get((Integer) t[0])));
            for (Object[] item : edsContracts) {
                ContractListItem listItem = new ContractListItem();
                listItem.setObjectId((Integer) item[0]);
                listItem.setAllowanceByClient((String) item[1]);
                if (item[2] != null) {
                    listItem.setClient((String) item[2]);
                }
                listItem.setNumber((String) item[3]);
                if (item[4] != null) {
                    listItem.setProject((String) item[4]);
                }
                if (item[5] != null) {
                    listItem.setContractBeginDate(new DateNonConvertable((Date) item[5]));
                }
                if (item[6] != null) {
                    listItem.setContractEndDate(new DateNonConvertable((Date) item[6]));
                }
                if (item[7] != null) {
                    listItem.setCreationTime(new DateNonConvertable((Date) item[7]));
                }
                if (item.length > 8 && item[8] != null) {
                    listItem.setLastNoteComment((String) item[8]);
                }

                listItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(contracts.get((Integer) item[0]).getContractCustomFields(), itemArrayList));
                contractList.add(listItem);
            }
            System.out.println("It took to Fill Contract list with data:===========================" + (System.currentTimeMillis() - started) + "===========================");
        }

        return new ListResult<>(contractList, totalCount);
    }

    @Override
    public void deleteContract(Integer objectId) {
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsContract.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(objectId);
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Project deleted");
        EdsContract contract = this.contractManager.get(objectId);
        this.contractManager.deleteContract(contract);
        if (objectId != null) {
            this.itemReminderManager.deleteItemReminders(objectId, Constants.CONTRACT_REMINDER);
        }
    }

    @Override
    public Integer saveContract(ContractSingleItem item) {
        EdsUser user = this.employeeManager.getUser();
        EdsContract contract = new EdsContract();
        contract.setCreator(user);
        contract.setNumber(item.getNumber());
        contract.setCreationTime(new Date());
        contract.setLastUpdateTime(new Date());
        contract.setIsAccomodation(item.getAccomudation());
        contract.setIsFoot(item.getFood());
        if (item.getStartDate() != null) {
            contract.setStartDate(item.getStartDate().getNonConvertedDate());
        } else {
            contract.setStartDate(null);
        }
        if (item.getDueDate() != null) {
            contract.setDueDate(item.getDueDate().getNonConvertedDate());
        } else {
            contract.setDueDate(null);
        }
        contract.setContractCustomFields(this.createContractCustomFields(item.getCustomFieldItems()));
        if (item.getClientId() != 0) {
            EdsCrmAccount client = this.clientManager.get(item.getClientId());
            if (client != null) {
                contract.setClient(client);
                this.clientManager.update(client);
            }
        }
        this.contractManager.create(contract);

        this.crmServiceLocal.saveCrmNotes(RelationItem.TYPE_CONTRACT, contract.getObjectID(), item.getNotes());
        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            this.saveContractAttachments(item.getAttachments(), contract);
        }
        if (item.getProjectPositions() != null) {
            item.getProjectPositions();
            for (ProjectPosition projectPosition : item.getProjectPositions()) {
                EdsProjectPosition pp = new EdsProjectPosition();
                pp.setPosition(this.positionManager.get(projectPosition.getPositionId()));
                pp.setContractStartDate(projectPosition.getContractStart().getNonConvertedDate());
                pp.setContractEndDate(projectPosition.getContractEnd() != null ? projectPosition.getContractEnd().getNonConvertedDate() : null);
                pp.setNumberOfWorker(projectPosition.getNumberOfWorker());
                pp.setPriceType(projectPosition.getPriceType());
                pp.setUnitPrice(projectPosition.getUnitPrice());
                pp.setUnitQTY(projectPosition.getUnitQTY());
                pp.setTotalCharge(projectPosition.getTotalCharge());
                pp.setContract(contract);
                pp.setCreationDate(new Date());
                pp.setCreator(user);
                contract.getProjectPositions().add(pp);
            }
        }

        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsContract.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        if (contract.getObjectID() != null) {
            kpiLog.setEntityId(contract.getObjectID());
        }
        this.saveContractReminder(contract, user.getCompany(), item.getReminder());
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "New Contract saved");
        return contract.getObjectID();
    }

    @Transactional
    public EdsContractCustomFields createContractCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsContractCustomFields edsContractCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                edsContractCustomFields = this.contractCFManager.get(customFieldItems.get(0).getObjectId());
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
                edsContractCustomFields = new EdsContractCustomFields();
                this.contractCFManager.create(edsContractCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsContractCustomFields, customFieldItems);
            return edsContractCustomFields;
        }
        return null;
    }

    @Override
    public void updateContract(EditContract editContract) {
        EdsContract contract = this.contractManager.get(editContract.getObjectId());
        try {
            contract.setObjectID(editContract.getObjectId());
            contract.setNumber(editContract.getNumber());
            contract.setIsAccomodation(editContract.getIsAccomodation());
            contract.setIsFoot(editContract.getIsFoot());
            EdsUser user = this.employeeManager.getUser();
            contract.setUpdater(user);
            if (editContract.getStartDate() != null && editContract.getStartDate().getNonConvertedDate() != null) {
                contract.setStartDate(editContract.getStartDate().getNonConvertedDate());
            } else {
                contract.setStartDate(null);
            }
            if (editContract.getDueDate() != null && editContract.getDueDate().getNonConvertedDate() != null) {
                contract.setDueDate(editContract.getDueDate().getNonConvertedDate());
            } else {
                contract.setDueDate(null);
            }
            contract.setLastUpdateTime(new Date());
            if (editContract.getClientId() != null && editContract.getClientId() != 0) {
                contract.setClient(this.clientManager.get(editContract.getClientId()));
            } else {
                contract.setClient(null);
            }
            EdsContractCustomFields contractCustomFields = this.createContractCustomFields(editContract.getCustomFieldItems());
            contract.setContractCustomFields(contractCustomFields);
            if (editContract.getProjectPositions() != null && editContract.getProjectPositions().length > 0) {
                List<EdsProjectPosition> edsProjectPositions = this.contractManager.getContractPositions(editContract.getObjectId());
                ArrayList<ProjectPosition> newProjectPositions = new ArrayList<>();

                for (ProjectPosition projectPosition : editContract.getProjectPositions()) {
                    boolean isNewPosition = true;

                    for (EdsProjectPosition edsProjectPosition : edsProjectPositions) {
                        if (edsProjectPosition.getPosition().getObjectID().equals(projectPosition.getPositionId())) {
                            isNewPosition = false;
                            edsProjectPosition.setContractStartDate(projectPosition.getContractStart().getNonConvertedDate());
                            edsProjectPosition.setContractEndDate(projectPosition.getContractEnd() != null ? projectPosition.getContractEnd().getNonConvertedDate() : null);
                            edsProjectPosition.setNumberOfWorker(projectPosition.getNumberOfWorker());
                            edsProjectPosition.setPriceType(projectPosition.getPriceType());
                            edsProjectPosition.setUnitPrice(projectPosition.getUnitPrice());
                            edsProjectPosition.setUnitQTY(projectPosition.getUnitQTY());
                            edsProjectPosition.setTotalCharge(projectPosition.getTotalCharge());
                            edsProjectPosition.setUpdatedDate(new Date());
                            edsProjectPosition.setUpdater(user);
                            break;
                        }
                    }

                    if (isNewPosition) {
                        newProjectPositions.add(projectPosition);
                    }
                }

                for (EdsProjectPosition edsProjectPosition : edsProjectPositions) {
                    boolean isExistingPosition = false;

                    for (ProjectPosition projectPosition : editContract.getProjectPositions()) {
                        if (edsProjectPosition.getPosition().getObjectID().equals(projectPosition.getPositionId())) {
                            isExistingPosition = true;
                            break;
                        }
                    }

                    if (!isExistingPosition) {
                        edsProjectPosition.setDeleted(true);
                        edsProjectPosition.setUpdatedDate(new Date());
                        edsProjectPosition.setUpdater(user);
                    }
                }

                for (ProjectPosition projectPosition : newProjectPositions) {
                    EdsProjectPosition pp = new EdsProjectPosition();
                    pp.setPosition(this.positionManager.get(projectPosition.getPositionId()));
                    pp.setContractStartDate(projectPosition.getContractStart().getNonConvertedDate());
                    pp.setContractEndDate(projectPosition.getContractEnd() != null ? projectPosition.getContractEnd().getNonConvertedDate() : null);
                    pp.setNumberOfWorker(projectPosition.getNumberOfWorker());
                    pp.setPriceType(projectPosition.getPriceType());
                    pp.setUnitPrice(projectPosition.getUnitPrice());
                    pp.setUnitQTY(projectPosition.getUnitQTY());
                    pp.setTotalCharge(projectPosition.getTotalCharge());
                    pp.setContract(contract);
                    pp.setCreationDate(new Date());
                    pp.setCreator(user);
                    contract.getProjectPositions().add(pp);
                }
                this.saveContractReminder(contract, user.getCompany(), editContract.getReminder());
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public EditContract getContractForEdit(Integer contractId) {
        EditContract contractItem = new EditContract();

        EdsContract contract = this.contractManager.get(contractId);

        if (contract != null) {
            contractItem.setObjectId(contract.getObjectID());
            contractItem.setNumber(contract.getNumber());
            contractItem.setIsAccomodation(contract.getIsAccomodation());
            contractItem.setIsFoot(contract.getIsFoot());
            if (contract.getStartDate() != null) {
                contractItem.setStartDate(new DateNonConvertable(contract.getStartDate()));
            }
            if (contract.getDueDate() != null) {
                contractItem.setDueDate(new DateNonConvertable(contract.getDueDate()));
            }
            if (contract.getClient() != null && !contract.getClient().isDeleted()) {
                contractItem.setClientId(contract.getClient().getObjectID());
                contractItem.setClientName(contract.getClient().getName());
            }
            if (contract.getCreationTime() != null) {
                contractItem.setCreationTime(new DateNonConvertable(contract.getCreationTime()));
            }
            contractItem.setProjectPositions(this.getContractPositions(contract.getObjectID()));
            contractItem.setReminder(this.itemReminderManager.getReminders(contractId, Constants.CONTRACT_REMINDER));
            EdsContractCustomFields edsContractCustomFields = contract.getContractCustomFields();
            ArrayList<CompanyCustomFieldItem> customFieldsItems = this.commonService.getCompanyCustomFields(ViewName.Contract);
            contractItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsContractCustomFields, customFieldsItems));
        }
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsContract.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(contractId);
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Get project for edit");
        return contractItem;
    }

    @Override
    public ContractViewItem viewContract(Integer objectID) {

        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsContract.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Contract view");

        EdsContract contract = this.contractManager.get(objectID);
        ContractViewItem contractViewItem = new ContractViewItem();
        contractViewItem.setObjectID(objectID);
        contractViewItem.setNumber(contract.getNumber());
        contractViewItem.setAccomodation(contract.getIsAccomodation());
        contractViewItem.setFood(contract.getIsFoot());
        if (contract.getStartDate() != null) {
            contractViewItem.setStartDate(new DateNonConvertable(contract.getStartDate()));
        }
        if (contract.getDueDate() != null) {
            contractViewItem.setDueDate(new DateNonConvertable(contract.getDueDate()));
        }

        if (contract.getClient() != null) {
            EdsCrmAccount client = this.clientManager.get(contract.getClient().getObjectID());
            if (client != null && !client.isDeleted()) {
                contractViewItem.setClient(contract.getClient().getName());
                contractViewItem.setClientId(contract.getClient().getObjectID());
            } else if (client != null && client.isDeleted()) {
                contractViewItem.setClient(this.commonLocalizer.localize("notAvailable", "N/A"));
            }
        }
        if (contract.getProject() != null) {
            EdsProject project = this.projectManager.get(contract.getProject().getObjectID());
            if (project != null && (project.getDeleted() == null || !project.getDeleted())) {
                contractViewItem.setProject(project.getName());
                if (project.getParent() != null) {
                    contractViewItem.setProjectParentId(project.getParent().getObjectID());
                }
                if (project.getStatus() != null) {
                    contractViewItem.setProjectStatusCode(project.getStatus().getCode());
                }
                contractViewItem.setProjectId(project.getObjectID());
            } else if (project != null && (project.getDeleted() != null && project.getDeleted())) {
                contractViewItem.setProject(this.commonLocalizer.localize("notAvailable", "N/A"));
            }
        }

        contractViewItem.setProjectPositions(this.getContractPositions(contract.getObjectID()));
        contractViewItem.setCreator(contract.getCreator() != null ? contract.getCreator().getFullName() : this.commonLocalizer.localize("notAvailable", "N/A"));
        contractViewItem.setCreatorID(contract.getCreator() != null ? contract.getCreator().getObjectID() : null);
        contractViewItem.setLastUpdaterName(contract.getUpdater() != null ? contract.getUpdater().getFullName() : this.commonLocalizer.localize("notAvailable", "N/A"));
        if (contract.getCreationTime() != null) {
            contractViewItem.setCreationTime(new DateNonConvertable(contract.getCreationTime()));
        }

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_RELATED_CASE_IN_CONTRACT)) {
            List<EdsCase> edsCaseList = contractManager.getRelatedCases(contract.getObjectID());
            for (EdsCase edsCase : edsCaseList) {
                contractViewItem.getRelatedCases().add(edsCase.getAsSelectItem());
            }
        }

        EdsContractCustomFields edsContractCustomFields = contract.getContractCustomFields();
        ArrayList<CompanyCustomFieldItem> customFieldsItems = this.commonService.getCompanyCustomFields(ViewName.Contract);
        contractViewItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsContractCustomFields, customFieldsItems));

        return contractViewItem;
    }

    private ProjectPosition[] getContractPositions(Integer contractID) {
        ArrayList<ProjectPosition> list = new ArrayList<>();
        List<EdsProjectPosition> projectPositions = this.contractManager.getContractPositions(contractID);
        if (projectPositions != null && !projectPositions.isEmpty()) {
            for (EdsProjectPosition pp : projectPositions) {
                ProjectPosition item = new ProjectPosition();
                item.setObjectID(pp.getObjectID());
                item.setPositionId(pp.getPosition().getObjectID());
                item.setPositionName(pp.getPosition().getName());
                item.setContractStart(new DateNonConvertable(pp.getContractStartDate()));
                item.setContractEnd(pp.getContractEndDate() != null ? new DateNonConvertable(pp.getContractEndDate()) : null);
                item.setNumberOfWorker(pp.getNumberOfWorker());
                item.setPriceType(pp.getPriceType());
                item.setUnitPrice(pp.getUnitPrice());
                item.setUnitQTY(pp.getUnitQTY());
                item.setTotalCharge(pp.getTotalCharge());
                list.add(item);
            }
        }

        return list.toArray(new ProjectPosition[]{});
    }

    public void addMembers(Integer projectId, ProjectMember[] members) {
        ProjectServiceImpl.log.info("** addMembers method was called **");
        EdsProject project = this.projectManager.get(projectId);
        EdsUser user = this.employeeManager.getUser();
        for (ProjectMember member : members) {
            EdsEmployee employee = this.employeeManager.get(member.getId());
            EdsEmployeeDepartment employeeDepartment = employee.getEmployeeTeam();

            EdsProjectEmployee existingPE;

            if (EmployeeAssignmentEnum.BY_POSITION.equals(project.getEmployeeAssignment()) && member.getContractStart() != null) {
                existingPE = this.projectEmployeeManager.getProjectEmployee(employee, project, member.getContractStart().getNonConvertedDate());
            } else {
                existingPE = this.projectEmployeeManager.getProjectEmployee(employee, project);
            }

            if (employeeDepartment != null && existingPE == null) {
                EdsProjectEmployee pe = new EdsProjectEmployee(employeeDepartment, project);
                pe.setClientChargeRate(member.getClientChargeRate());
                pe.setWageRate(member.getWageRate());
                pe.setWorkloadPercentage(member.getWorkloadPercentage());

                if (member.getPositionId() != null) {
                    EdsPosition position = this.positionManager.get(member.getPositionId());
                    pe.setPosition(position);
                    pe.setWageRate(position.getWageRate() != null ? position.getWageRate().doubleValue() : 0d);
                    pe.setContractStartDate(member.getContractStart().getNonConvertedDate());
                    pe.setContractEndDate(member.getContractEnd() != null ? member.getContractEnd().getNonConvertedDate() : null);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(pe.getContractEndDate() != null ? (Date) pe.getContractEndDate().clone() : (Date) pe.getContractStartDate().clone());
                    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
                    this.updateEmployeePreviousConstractPeriodByNew(member.getId(), member.getContractStart().getNonConvertedDate(), pe.getContractEndDate() != null ? member.getContractEnd().getNonConvertedDate() : cal.getTime(), null);
                }

                EdsProjectEmployeeWageClientRateHistory prate = new EdsProjectEmployeeWageClientRateHistory();
                prate.setChangeDate(user.getCompany().getCompanyDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(prate.getChangeDate());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                prate.setChangeDate(cal.getTime());
                prate.setClientChargeRate(member.getClientChargeRate());
                prate.setWageRate(member.getWageRate());
                prate.setWorkloadPercentage(member.getWorkloadPercentage());
                prate.setProjectEmployee(pe);

                pe.getWageClientRatesHistory().add(prate);
                this.projectEmployeeManager.create(pe);
                ProjectServiceImpl.log.info("** project employee was created with wage: " + prate.getWageRate() + " and with client charge rate: " + prate.getClientChargeRate());
                this.baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pe, user);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem getClient(Integer clientID) {
        EdsCrmAccount edsClient = this.clientManager.get(clientID);
        return new SelectItem(edsClient.getObjectID(), edsClient.getName().trim());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectMember[] getCompanyEmployees() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(EdsRole.DR);
        List<EdsEmployee> employees = this.employeeManager.list(fp);
        ProjectMember[] projectMembers = new ProjectMember[employees.size()];
        int i = 0;
        for (EdsEmployee employee : employees) {
            if (employee.getEmployeeTeam() == null || employee.getEmployeeTeam().getTeam() == null) {
                continue;
            }
            String teamName = employee.getEmployeeTeam() != null && employee.getEmployeeTeam().getTeam() != null ? employee.getEmployeeTeam().getTeam().getName() : "";
            projectMembers[i] = new ProjectMember(employee.getObjectID(), employee.getName(), teamName);
            i++;
        }
        return projectMembers;
    }

    public ProjectServiceImpl() {
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectLabourCosts[] getProjectLabourCostsItems(Integer projectId) {
        List<EdsWorkStream> workstreams = this.workStreamManager.findOrphanWorkstreams(projectId);
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setProjectId(projectId);
        fp.setViewAsId(EdsRole.DR);
        List<EdsTask> tasks = this.taskManager.findOrphanTasks(projectId);
        List<ProjectLabourCosts> workStreamResult = this.createWorkstreamResult(workstreams);
        List<ProjectLabourCosts> taskResult = this.createTaskResult(tasks);
        workStreamResult.addAll(taskResult);
        return workStreamResult.toArray(new ProjectLabourCosts[]{});
    }

    public ProjectViewItem getProjectCostItems(Integer projectID) {
        ProjectViewItem projectViewItem = new ProjectViewItem();
        Double[] projectCostAndTimeSpent = this.timeSheetManager.getProjectCostAndTimeSpent(projectID, null);
        Double actualProjectExpense = this.projectBudgetManager.getProjectExpense(projectID).doubleValue();
        Double actualProjectCost = (projectCostAndTimeSpent != null ? (projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_COST] != null ? projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_COST] : 0d) : 0d);
        Double estimatedProjectExpense = this.projectBudgetManager.getProjectPlanedExpense(projectID).doubleValue();
        Double estimateProjectCost = projectCostAndTimeSpent != null ? (projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_COST] != null ? projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_COST] : 0d) : 0d;
        DecimalFormat df = new DecimalFormat("0.00");
        projectViewItem.setHoursSpent(projectCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_TIME_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_TIME_SPENT].intValue() : 0) : "00:00");
        projectViewItem.setActualCost(df.format(actualProjectExpense + actualProjectCost));
        projectViewItem.setEstimatedTime(projectCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_TIME_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_TIME_SPENT].intValue() : 0) : "00:00");
        projectViewItem.setEstimatedCost(df.format(estimatedProjectExpense + estimateProjectCost));
        return projectViewItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectLabourCosts[] getProjectLabourCostsSubItems(Integer workStreamId) {
        EdsWorkStream workstream = this.workStreamManager.get(workStreamId);
        Set<EdsWorkStream> removed = new HashSet<>();

        Set<EdsWorkStream> subWorkStreams = workstream.getSubWorkStreams();
        for (EdsWorkStream undeletedWorkstream : subWorkStreams) {
            if (undeletedWorkstream.isDeleted()) {
                removed.add(undeletedWorkstream);
            }
        }
        subWorkStreams.removeAll(removed);
        List<EdsWorkStream> workStreamList = new ArrayList<>(subWorkStreams);
        List<ProjectLabourCosts> workStreamResult = this.createWorkstreamResult(workStreamList);
        List<EdsTask> tasksList = new ArrayList<>(workstream.getTasks());
        List<ProjectLabourCosts> taskResult = this.createTaskResult(tasksList);
        workStreamResult.addAll(taskResult);
        return workStreamResult.toArray(new ProjectLabourCosts[]{});
    }

    private List<ProjectLabourCosts> createWorkstreamResult(List<EdsWorkStream> workstreams) {
        if (workstreams.isEmpty()) {
            return new ArrayList<>();
        }
        return ListUtils.createTreeItemList(workstreams, new WfmTreeItemFactory<EdsWorkStream>() {
            public ProjectLabourCosts createItem(EdsWorkStream workstream) {
                ProjectLabourCosts costs = new ProjectLabourCosts(workstream.getObjectID(), workstream.getName(), ProjectLabourCosts.WORKSTREAM);
                costs.setChildren(!workstream.getSubWorkStreams().isEmpty() || !workstream.getTasks().isEmpty());
                costs.setEstimatedTime(workstream.getEstimatedTime() != null ? workstream.getEstimatedTime() : 0);
                costs.setTimspent(workstream.getActualTime());
                costs.setWageAmmount(workstream.getWageAmmount() != null ? workstream.getWageAmmount() : 0);
                costs.setClientWageAmmount(workstream.getClientChargeAmmount() != null ? workstream.getClientChargeAmmount() : 0);
                costs.setActualWageAmount(workstream.getActualWageAmount() != null ? workstream.getActualWageAmount() : 0);
                costs.setActualClientChargeAmount(workstream.getActualClientChargeAmount() != null ? workstream.getActualClientChargeAmount() : 0);
                costs.setPlannedWageAmount(workstream.getPlannedWageAmount() != null ? workstream.getPlannedWageAmount() : 0);
                costs.setPlannedClientChargeAmount(workstream.getPlannedClientChargeAmount() != null ? workstream.getPlannedClientChargeAmount() : 0);

                //workStream children
                List<ProjectLabourCosts> workStreamTaskResultCosts = new ArrayList<>();
                List<EdsTask> workStreamTaskList = ProjectServiceImpl.this.taskManager.getWorkStreamTasksOrderBy(workstream.getObjectID(), null);
                if (workStreamTaskList != null && workStreamTaskList.size() > 0) {
                    List<ProjectLabourCosts> workStreamTaskResult = ProjectServiceImpl.this.createTaskResult(workStreamTaskList);
                    workStreamTaskResultCosts.addAll(workStreamTaskResult);
                    //
                    if (workStreamTaskResultCosts.size() > 0) {
                        int childrenTaskWorkStreamEstimatedTime = 0;
                        int childrenTaskWorkStreamTimeSpent = 0;
                        double childrenTaskWorkStreamWageAmount = 0;
                        double childrenTaskWorkStreamClientWageAmount = 0;
                        double childrenTaskWorkStreamActualWageAmount = 0;
                        double childrenTaskWorkStreamActualClientChargeAmount = 0;
                        double childrenTaskWorkStreamPlannedWageAmount = 0;
                        double childrenTaskWorkStreamPlannedClientChargeAmount = 0;

                        for (ProjectLabourCosts childTaskWorkStream : workStreamTaskResultCosts) {
                            childrenTaskWorkStreamEstimatedTime += childTaskWorkStream.getEstimatedTime();
                            childrenTaskWorkStreamTimeSpent += childTaskWorkStream.getTimspent();
                            childrenTaskWorkStreamWageAmount += childTaskWorkStream.getWageAmmount();
                            childrenTaskWorkStreamClientWageAmount += childTaskWorkStream.getClientWageAmmount();
                            childrenTaskWorkStreamActualWageAmount += childTaskWorkStream.getActualWageAmount();
                            childrenTaskWorkStreamActualClientChargeAmount += childTaskWorkStream.getActualClientChargeAmount();
                            childrenTaskWorkStreamPlannedWageAmount += childTaskWorkStream.getPlannedWageAmount();
                            childrenTaskWorkStreamPlannedClientChargeAmount += childTaskWorkStream.getPlannedClientChargeAmount();
                        }
                        //
                        if (childrenTaskWorkStreamEstimatedTime > costs.getEstimatedTime()) {
                            costs.setEstimatedTime(childrenTaskWorkStreamEstimatedTime);
                        }
                        if (childrenTaskWorkStreamTimeSpent > costs.getTimspent()) {
                            costs.setTimspent(childrenTaskWorkStreamTimeSpent);
                        }
                        if (childrenTaskWorkStreamWageAmount > costs.getWageAmmount()) {
                            costs.setWageAmmount(childrenTaskWorkStreamWageAmount);
                        }
                        if (childrenTaskWorkStreamClientWageAmount > costs.getClientWageAmmount()) {
                            costs.setClientWageAmmount(childrenTaskWorkStreamClientWageAmount);
                        }
                        if (childrenTaskWorkStreamActualWageAmount > costs.getActualWageAmount()) {
                            costs.setActualWageAmount(childrenTaskWorkStreamActualWageAmount);
                        }
                        if (childrenTaskWorkStreamActualClientChargeAmount > costs.getActualClientChargeAmount()) {
                            costs.setActualClientChargeAmount(childrenTaskWorkStreamActualClientChargeAmount);
                        }
                        if (childrenTaskWorkStreamPlannedWageAmount > costs.getPlannedWageAmount()) {
                            costs.setPlannedWageAmount(childrenTaskWorkStreamPlannedWageAmount);
                        }
                        if (childrenTaskWorkStreamPlannedClientChargeAmount > costs.getPlannedClientChargeAmount()) {
                            costs.setPlannedClientChargeAmount(childrenTaskWorkStreamPlannedClientChargeAmount);
                        }
                    }
                }
                return costs;
            }
        });
    }

    private void saveContractAttachments(FileItem[] attachments, EdsContract contract) {
        this.attachmentUtilsManager.saveAttachments(Constants.F_CONTRACT, contract.getObjectID(), contract.getObjectID(), attachments);
    }


    private List<ProjectLabourCosts> createTaskResult(List<EdsTask> tasks) {
        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }
        return ListUtils.createTreeItemList(tasks, new WfmTreeItemFactory<EdsTask>() {
            public ProjectLabourCosts createItem(EdsTask task) {
                String[] assignees = new String[task.getUnDeletedAssignments().size() + task.getPositions().size()];
                int i = 0;
                //apply task assignments
                for (EdsEmployeeTask etask : task.getUnDeletedAssignments()) {
                    assignees[i] = etask.getProjectEmployee().getName();
                    i++;
                }

                //apply task postions
                for (EdsPositionTask ptask : task.getPositions()) {
                    assignees[i] = ptask.getPosition().getName();
                    i++;
                }
                ProjectLabourCosts costs = new ProjectLabourCosts(task.getObjectID(), task.getName(), ProjectLabourCosts.TASK);
                costs.setEstimatedTime(task.getEstimatedTime() != null ? task.getEstimatedTime() : 0);
                costs.setActualWageAmount(task.getActualWageAmount()/*actualWageAmount*/);
                costs.setTimspent(task.getTimespent()/*actualTimeSpent*/);
                costs.setActualClientChargeAmount(task.getActualClientChargeAmount()/*actualClientChargeAmount*/);
                costs.setPlannedWageAmount(task.getPlannedWageAmount()/*plannedWageAmount*/);
                costs.setPlannedClientChargeAmount(task.getPlannedClientChargeAmount()/*plannedClientChargeAmount*/);
                costs.setWageAmmount(task.getRemainingWageAmount() + task.getActualWageAmount()/*wageAmmount*/);
                costs.setClientWageAmmount(task.getRemainingClientChargeAmount() + task.getActualClientChargeAmount()/*clientWageAmmount*/);
                costs.setAssignees(assignees);
                return costs;
            }
        });
    }

    public void saveProjectWageRates(ProjectMember[] members) {

        for (ProjectMember member : members) {
            EdsProjectEmployee pemployee = this.projectManager.getProjectEmployee(member.getProjectEmployeeId());

            if (!pemployee.getWageRate().equals(member.getWageRate()) || !pemployee.getClientChargeRate().equals(member.getClientChargeRate()) || !pemployee.getWorkloadPercentage().equals(member.getWorkloadPercentage())) {
                if (member.getWageRate() != null) {
                    pemployee.setWageRate(member.getWageRate());
                }
                if (member.getClientChargeRate() != null) {
                    pemployee.setClientChargeRate(member.getClientChargeRate());
                }
                if (member.getWorkloadPercentage() != null) {
                    pemployee.setWorkloadPercentage(member.getWorkloadPercentage());
                }
                EdsProjectEmployeeWageClientRateHistory hist = new EdsProjectEmployeeWageClientRateHistory();
                hist.setChangeDate(pemployee.getProject().getCompany().getCompanyDate());
                hist.setWageRate(pemployee.getWageRate());
                hist.setClientChargeRate(pemployee.getClientChargeRate());
                hist.setWorkloadPercentage(pemployee.getWorkloadPercentage());
                hist.setProjectEmployee(pemployee);
                this.projectManager.updateEmployeeWageClientRateHistorybyDate(hist);
            }
            this.projectEmployeeManager.update(pemployee);
        }
    }

    public Integer saveProject(ProjectSingleItem item) throws NumberExistingException {
        return this.saveProject(item, false);
    }

    @Transactional
    public Integer saveProject(ProjectSingleItem item, boolean fromCsvImport) throws NumberExistingException {
        boolean isEmployeeAssignmentEnable = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE);
        boolean isEnableMultiClientToProject = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT);

        NumberData numberData = item.getNumberData();
        if (numberData == null || numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim())/* || EdsNumberingSettings.validateNumberData(numberData) == null*/) {
            throw new NumberExistingException("Incorrect project number format.");
        }

        EdsUser user = this.employeeManager.getUser();
        EdsEmployee manager = this.employeeManager.get(item.getManagerId());
        EdsProject project = new EdsProject();
        project.setName(item.getName());
        project.setContractId(item.getContractId());
        project.setDescription(item.getDescription());
        project.setStartDate(item.getStartDate());
        project.setEndDate(item.getEndDate());
        project.setDueDate(item.getEndDate());
        project.setManager(manager);
        EdsReference status = this.referenceManager.get(item.getStatusId());
        project.setStatus(status);
        if (status != null && status.getCode().equals(EdsProject.COMPLETED)) {
            project.setCompletedDate(new Date());
        } else {
            project.setCompletedDate(null);
        }
        project.setCreator(user);
        project.setBillable(item.isBillable());

        if (isEmployeeAssignmentEnable) {
            project.setEmployeeAssignment(item.getEmployeeAssignment());
        }

        if (item.getParentId() != null) {
            project.setParent(this.projectManager.get(item.getParentId()));
        }

        this.assignBackupMangers(project, item.getBackupManagerIDs());

        if (item.getCheckInLocations() != null && !item.getCheckInLocations().isEmpty()) {
            Set<EdsCheckInLocation> checkInLocations = new HashSet<>();
            for (CheckInLocationItem checkInLocation : item.getCheckInLocations()) {
                EdsCheckInLocation edsCheckInLocation = new EdsCheckInLocation();
                edsCheckInLocation.setLatitude(Double.valueOf(checkInLocation.getLatitude()));
                edsCheckInLocation.setLongitude(Double.valueOf(checkInLocation.getLongitude()));
                edsCheckInLocation.setRadius(Integer.valueOf(checkInLocation.getRadius()));
                edsCheckInLocation.setProject(project);
                checkInLocations.add(edsCheckInLocation);
            }
            project.setCheckInLocations(checkInLocations);
        }
        if (item.getClientId() != null && item.getClientId() != 0) {
            EdsCrmAccount client = this.clientManager.get(item.getClientId());
            if (client != null && client.isClient()) {
                project.setClient(client);
                this.clientManager.update(client);
            }
        } else if (item.getClientName() != null && !item.getClientName().trim().isEmpty()) {
            List<EdsCrmAccount> clients = this.clientManager.getClientByName(item.getClientName());
            if (!clients.isEmpty() && clients.get(0).isClient()) {
                project.setClient(clients.get(0));
            }
        }

        if (isEnableMultiClientToProject) {
            project.getClients().clear();

            if (item.getClients() != null) {
                item.getClients();
                for (SelectItem client : item.getClients()) {
                    project.getClients().add(this.clientManager.get(client.getId()));
                }
            }

            if (project.getClient() != null) {
                project.getClients().add(project.getClient());
            }
        }
        if (item.getLocationId() != 0) {
            project.setProjectLocation(this.locationManager.get(item.getLocationId()));
        }

        if (this.projectManager.isProjectNumberExists(item.getNumberData().getNumberString(), null)) {
            numberData = this.generateProjectNumber(item.getStartDate(), item.getClientId(), null);
            project.setNumber(numberData.getNumberString());
        } else {
            project.setNumber(numberData.getNumberString());
        }
        project.setIntNumber(numberData.getIntNumber());
        project.setSavedNumberFormula(item.getNumberData().getSavedNumberFormula());
        if (numberData.getIntNumber() != null && !"".equals(numberData.getIntNumber())) {
            EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
            if (settings != null && (settings.getProjectLastIntNumber() == null || numberData.getIntNumber() >= settings.getProjectLastIntNumber())) {
                settings.setProjectLastIntNumber(numberData.getIntNumber() + 1);
                this.numberingSettingsManager.createOrUpdate(settings);
            }
        }
        if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0 && project.getProjectCustomFields() != null) {
            StringBuilder changes = new StringBuilder();
            for (CompanyCustomFieldItem cit : item.getCustomFieldItems()) {
                changes.append(project.getProjectCustomFields() != null && CustomFieldsUtils.getObjectValue(project.getProjectCustomFields(), cit.getColumnCode()) != null ? this.getChanges(CustomFieldsUtils.getObjectValue(project.getProjectCustomFields(), cit.getColumnCode()), cit) : (cit.getColumnCode() + ","));
            }
            if (!"".contentEquals(changes)) {
                project.addCustomFieldChanges(changes.toString());
            }
        }
        EdsProjectCustomFields edsProjectCustomFields = this.createProjectCustomFields(item.getCustomFieldItems());
        project.setProjectCustomFields(edsProjectCustomFields);

        if (item.getProjectSource() != null) {
            project.setProjectSource(item.getProjectSource());
        }
        for (HashMap.Entry<String, ArrayList<CustomTableRpc>> map : item.getCustomTableItems().entrySet()) {
            List<CustomTableRpc> values = map.getValue();

            for (CustomTableRpc rpc : values) {
                EdsProjectCustomItemTable customItemTable = new EdsProjectCustomItemTable();
                customItemTable.setUuid(map.getKey());
                customItemTable.setName(rpc.getItemName());
                customItemTable.setDescription(rpc.getDescription());
                customItemTable.setCustomFields(saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()));
                customItemTable.setProject(project);
                projectItemTableManager.createOrUpdate(customItemTable);

            }
        }

        this.projectManager.create(project);

        this.saveProjectReminder(project.getObjectID(), user.getCompany(), item.getReminder());

        if (item.getContractId() != null) {
            EdsContract contract = this.contractManager.get(item.getContractId());
            if (contract != null) {
                contract.setProject(project);
                this.contractManager.update(contract);
            }
        }

        EdsBusinessEvent projectBusinessEvent = this.baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, user);
        this.baseEventPostProcessor.registerEvent(ProjectManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, user);
        for (EdsEmployee backupManager : project.getBackupManagers()) {
            this.baseEventPostProcessor.registerEvent(ProjectBackupManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, user, backupManager);
        }

        if (item.getProjectPositions() != null) {
            item.getProjectPositions();
            for (ProjectPosition projectPosition : item.getProjectPositions()) {
                EdsProjectPosition pp = new EdsProjectPosition();

                if (projectPosition.getObjectID() != null) {
                    pp = this.projectPositionManager.get(projectPosition.getObjectID());
                }
                pp.setPosition(this.positionManager.get(projectPosition.getPositionId()));
                pp.setContractStartDate(projectPosition.getContractStart().getNonConvertedDate());
                pp.setContractEndDate(projectPosition.getContractEnd() != null ? projectPosition.getContractEnd().getNonConvertedDate() : null);
                pp.setUnitPrice(projectPosition.getUnitPrice());
                pp.setOvertimeRate(projectPosition.getOvertimeRate());
                pp.setWeekendOvertimeRate(projectPosition.getWeekendOvertimeRate());
                pp.setHolidayOvertimeRate(projectPosition.getHolidayOvertimeRate());
                pp.setNumberOfWorker(projectPosition.getNumberOfWorker());
                pp.setProject(project);
                pp.setCreationDate(new Date());
                pp.setCreator(user);
                project.getProjectPositions().add(pp);

                if (project.getContractId() != null) {
                    EdsContract edsContract = this.contractManager.get(project.getContractId());
                    if (edsContract != null) {
                        boolean isExistingPosition = false;
                        if (edsContract.getProjectPositions() != null && edsContract.getProjectPositions().size() > 0) {
                            for (EdsProjectPosition contractPosition : edsContract.getProjectPositions()) {
                                if (contractPosition.getPosition() != null
                                        && contractPosition.getPosition().getObjectID().equals(projectPosition.getPositionId())) {
                                    isExistingPosition = true;
                                    break;
                                }
                            }
                        }
                        if (!isExistingPosition) {
                            pp.setContract(edsContract);
                            edsContract.getProjectPositions().add(pp);
                        }
                    }
                }
            }
        }

        if (item.getProjectMembers() != null) {
            for (ProjectMember member : item.getProjectMembers()) {
                EdsEmployee employee = this.employeeManager.get(member.getId());
                EdsProjectEmployee pe = new EdsProjectEmployee();
                pe.setProject(project);
                pe.setEmployeeDepartment(employee.getEmployeeTeam());
                pe.setWageRate(member.getWageRate());
                pe.setClientChargeRate(member.getClientChargeRate());
                pe.setWorkloadPercentage(member.getWorkloadPercentage());

                if (member.getPositionId() != null) {
                    EdsPosition position = this.positionManager.get(member.getPositionId());
                    pe.setPosition(position);
                    pe.setWageRate(position.getWageRate() != null ? position.getWageRate().doubleValue() : 0d);
                    pe.setContractStartDate(member.getContractStart() != null ? member.getContractStart().getNonConvertedDate() : null);
                    pe.setContractEndDate(member.getContractEnd() != null ? member.getContractEnd().getNonConvertedDate() : null);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(pe.getContractEndDate() != null ? (Date) pe.getContractEndDate().clone() : (Date) pe.getContractStartDate().clone());
                    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
                    this.updateEmployeePreviousConstractPeriodByNew(member.getId(), member.getContractStart().getNonConvertedDate(), pe.getContractEndDate() != null ? member.getContractEnd().getNonConvertedDate() : cal.getTime(), null);
                }
                EdsProjectEmployeeWageClientRateHistory history = new EdsProjectEmployeeWageClientRateHistory();
                history.setProjectEmployee(pe);
                history.setChangeDate(new Date());
                history.setWageRate(member.getWageRate());
                history.setClientChargeRate(member.getClientChargeRate());
                history.setWorkloadPercentage(member.getWorkloadPercentage());

                this.projectManager.updateEmployeeWageClientRateHistorybyDate(history);

                this.projectEmployeeManager.create(pe);
                if (!pe.getEmployeeDepartment().getEmployee().equals(project.getManager())
                        && !project.isUserBackupManager(pe.getEmployeeDepartment().getEmployee().getObjectID())
                        && !pe.getEmployeeDepartment().getEmployee().getAccountStatus().getCode().equals(Constants.EMPLOYEE_STATUS_NO_ACCCESS)) {
                    this.baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pe, user);

                }
            }
        }

        this.roleManager.addRole(manager, Constants.PM);

        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            //Create a new folder related to EdsProject.
            this.commonServiceLocal.createProjectFolder(project.getObjectID());

// --------------------- with Document Management logic ---------------------------
            this.saveProjectAttachments(item.getAttachments(), project);
        } else {
            if (fromCsvImport) {
                this.baseEventPostProcessor.registerEvent(ProjectFolderEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, user);
            } else {
                this.commonServiceLocal.createProjectFolder(project.getObjectID());
            }
        }

        if (project.getObjectID() != null && item.isRelationChanged()) {
            this.allInOneService.saveRelations(RelationItem.TYPE_PROJECT, project.getObjectID(),
                    project.getName(), item.getRelations());
        }
        this.updateProjectStatus(project);

        EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, user);
        workflowEvent.setEntityType(RelationItem.TYPE_PROJECT);
        try {
//            this.solrManager.indexAddProject(project, Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
            projectSolrComponent.index(project);
            projectBusinessEvent.setSolrIndexed(true);
        } catch (Exception e) {
            projectBusinessEvent.setSolrIndexed(false);
            e.printStackTrace();
        }
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsProject.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        if (project.getObjectID() != null) {
            kpiLog.setEntityId(project.getObjectID());
        }
        if (item.getNotes() != null) {
            for (HistoryListItem note : item.getNotes()) {
                note.setRelatedToId(F_PROJECT_ROOT);
                note.setRelatedId(project.getObjectID());
                crmServiceLocal.saveCrmNote(Constants.PROJECT, project.getObjectID(), note);
            }
        }
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "New project saved");

        return project.getObjectID();
    }

    public EdsProjectItemTableCF saveCustomTableFields(EdsProjectItemTableCF customfField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (customfField == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && fieldItem.getFieldStringValue().length() > 0)
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || fieldItem.getProfielImageId() != null
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                customfField = new EdsProjectItemTableCF();
                projectItemTableCFManager.create(customfField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customfField, customFieldItems);
            return customfField;
        }
        return null;
    }

    private String getChanges(Object ob, CompanyCustomFieldItem item) {
        if (ob != null) {
            if (Constants.DATA_TYPE_TEXT.equals(item.getDataType())) {
                String text = (String) ob;
                return !text.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (Constants.DATA_TYPE_NUMBER.equals(item.getDataType())) {
                String s = String.valueOf(((Double) ob).intValue());
                return !s.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (Constants.DATA_TYPE_DATE.equals(item.getDataType())) {
                Date date = (Date) ob;
                return !date.equals(item.getFieldDateNonConvertedValue().getNonConvertedDate()) ? (item.getColumnCode() + ",") : "";
            }
        }
        return "";
    }

    @Transactional
    public EdsProjectCustomFields createProjectCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsProjectCustomFields edsProjectCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                edsProjectCustomFields = this.projectCFManager.get(customFieldItems.get(0).getObjectId());
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
                edsProjectCustomFields = new EdsProjectCustomFields();
                this.projectCFManager.create(edsProjectCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsProjectCustomFields, customFieldItems);
            return edsProjectCustomFields;
        }
        return null;
    }

    public void sendContractOverDueEmailNotification(Integer contractID, Integer companyID, EdsRecurrence recurrence) {
        try {
            EdsContract contract = this.contractManager.get(contractID);
            if (contract != null && !contract.getDeleted()) {
                List<EdsEmployee> employees = this.employeeManager.getEmployeesByPermissionCode(PermissionConstants.PM_CONRACT_REMINDER);

                if (employees != null && employees.size() > 0) {
                    EmailTemplateItem emailTemplateItem;
                    EdsEmailTemplate edsEmailTemplate = this.emailTemplateManager.getDefaultEmailTemplateByCategory(Constants.PM_CONTRACT_REMINDER);
                    for (EdsEmployee employee : employees) {
                        if (employee != null && employee.getCompany().getActive() && !employee.getDeleted() && edsEmailTemplate != null) {
                            emailTemplateItem = this.emailTemplateServiceLocal.generateEmailTemplateItemForContractReminder(contract.getCreator(), employee, contract, edsEmailTemplate, recurrence);
                            if (emailTemplateItem != null) {
                                this.messageManager.sendContractOverDueReminder(contract, employee, emailTemplateItem);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailNotification(Integer contractID, Integer companyId) {
        try {
            KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsContract.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.SEND);
            kpiLog.setEntityId(contractID);
            ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Sending Contract Reminder");

            EdsProject project = this.projectManager.get(contractID);
            if (project != null && !project.getDeleted()) {
                List<EdsProjectEmployee> projectEmployees = this.projectManager.getEmployeesByProject(contractID);
                if (projectEmployees != null && projectEmployees.size() > 0) {
                    for (EdsProjectEmployee employeeProject : projectEmployees) {
                        EdsEmployee employee = employeeProject.getEmployeeDepartment().getEmployee();
                        if (employee != null && employee.getCompany().getActive() && !employee.getDeleted() && !employeeProject.getDeleted()) {
                            this.messageManager.sendProjectOverDueDateReminder(project, employeeProject);
                        }
                    }
                }
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    private void saveContractReminder(EdsContract contract, EdsCompany company, ArrayList<CalendarEventReminder> reminders) {
        if (contract != null) {
            if (contract.getDueDate() != null) {
                Integer contractID = contract.getObjectID();

                if (reminders != null && !reminders.isEmpty()) {
                    this.itemReminderManager.deleteItemReminders(contractID, Constants.CONTRACT_REMINDER);
                    List<EdsRecurrence> recurrenceList = this.recurrenceManager.getRecurrenceJobList(SchedulerConstant.CONTRACT_OVERDUE_REMINDER, contractID, company.getObjectID());
                    if (recurrenceList != null && !recurrenceList.isEmpty()) {
                        for (EdsRecurrence rec : recurrenceList) {
                            this.recurrenceService.updateRecurrence(rec, true, true);
                        }
                    }
                    RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
                    recurrenceJobItem.setEnabled(true);
                    recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_MINUTELY);
                    recurrenceJobItem.setJobType(SchedulerConstant.CONTRACT_OVERDUE_REMINDER);
                    recurrenceJobItem.setBusObjectId(contractID);
                    recurrenceJobItem.setInterval(5);
                    recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                    recurrenceJobItem.setEndType(SchedulerConstant.END_AFTER_OCCURRENCES);
                    for (CalendarEventReminder eventReminder : reminders) {
                        Date contractDueDate = contract.getDueDate();
                        if (eventReminder.getReminderTimes() >= (60 * 12)) {
                            contractDueDate = DateUtil.resetTime(contract.getDueDate());
                        }
                        Date recStartDate = DateUtil.addMinutes(contractDueDate, (-1) * eventReminder.getReminderTimes());
                        if (recStartDate.after(new Date())) {
                            recurrenceJobItem.setEndDate(DateUtil.addMinutes(recStartDate, 5));
                            recurrenceJobItem.setStartDate(recStartDate);
                            recurrenceJobItem.setBusObjectParams(eventReminder.getReminderTimes().toString());
                            recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                            recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());

                            if (Integer.valueOf(1).equals(eventReminder.getValue())) {
                                recurrenceJobItem.setStartDate(recStartDate);
                                recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                                recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());
                                this.recurrenceService.saveRecurrenceJob(recurrenceJobItem);
                            }

                            EdsItemReminder reminder = new EdsItemReminder();
                            reminder.setItem(contractID);
                            reminder.setItemType(Constants.CONTRACT_REMINDER);
                            reminder.setReminderType(eventReminder.getValue());
                            reminder.setMinutes(eventReminder.getReminderTimes());
                            this.itemReminderManager.create(reminder);
                        }
                    }
                } else {
                    this.itemReminderManager.deleteItemReminders(contractID, Constants.CONTRACT_REMINDER);
                }
            }
        }
    }


    private void saveProjectReminder(Integer projectID, EdsCompany company, ArrayList<CalendarEventReminder> reminders) {
        if (reminders != null && !reminders.isEmpty()) {
            this.itemReminderManager.deleteItemReminders(projectID, Constants.PROJECT_REMINDER);
            EdsProject project = this.projectManager.get(projectID);
            if (project != null && project.getDueDate() == null) {
                return;
            }
            List<EdsRecurrence> recurrenceList = this.recurrenceManager.getRecurrenceJobList(SchedulerConstant.PROJECT_OVERDUE_REMINDER, projectID, company.getObjectID());
            if (recurrenceList != null && !recurrenceList.isEmpty()) {
                for (EdsRecurrence rec : recurrenceList) {
                    this.recurrenceService.updateRecurrence(rec, true, true);
                }
            }
            RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
            recurrenceJobItem.setEnabled(true);
            recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_YEARLY);
            recurrenceJobItem.setJobType(SchedulerConstant.PROJECT_OVERDUE_REMINDER);
            recurrenceJobItem.setBusObjectId(projectID);
            recurrenceJobItem.setInterval(1);
            recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
            recurrenceJobItem.setEndType(SchedulerConstant.END_BY_DATE);
            for (CalendarEventReminder eventReminder : reminders) {
                Date recStartDate = DateUtil.addMinutes(project.getDueDate(), (-1) * eventReminder.getReminderTimes());
                if (recStartDate.after(new Date())) {
                    recurrenceJobItem.setEndDate(DateUtil.addMinutes(recStartDate, 5));
                    recurrenceJobItem.setStartDate(recStartDate);
                    recurrenceJobItem.setBusObjectParams(eventReminder.getReminderTimes().toString());
                    recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                    recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());
                    if (Integer.valueOf(1).equals(eventReminder.getValue())) {
                        recurrenceJobItem.setStartDate(recStartDate);
                        recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                        recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());
                        this.recurrenceService.saveRecurrenceJob(recurrenceJobItem);
                    }

                    EdsItemReminder reminder = new EdsItemReminder();
                    reminder.setItem(projectID);
                    reminder.setItemType(Constants.PROJECT_REMINDER);
                    reminder.setReminderType(eventReminder.getValue());
                    reminder.setMinutes(eventReminder.getReminderTimes());
                    this.itemReminderManager.create(reminder);
                }
            }
        }
    }

    private void saveProjectAttachments(FileItem[] attachments, EdsProject project) {
        this.attachmentUtilsManager.saveAttachments(Constants.F_PROJECT, project.getObjectID(), project.getObjectID(), attachments);
    }

    public Integer saveCloneProject(CloneProjectItem cloneProjectItem) throws NumberExistingException {
        long begin = System.currentTimeMillis();
        boolean isEnableMultiClientToProject = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT);
        if (cloneProjectItem.getNumberData() != null && cloneProjectItem.getNumberData().getNumberString() != null
                && !"".equals(cloneProjectItem.getNumberData().getNumberString().trim())
                && this.projectManager.isProjectNumberExists(cloneProjectItem.getNumberData().getNumberString(), null)) {
            throw new NumberExistingException("Project with number " + cloneProjectItem.getNumberData().getNumberString() + " already exists.");
        }

        EdsUser user = this.projectManager.getUser();
        EdsNumberingSettings edsSettings = this.numberingSettingsManager.getNumberingSetting();
        EdsProject project = this.projectManager.get(cloneProjectItem.getProjectId());
        EdsProject cloneProject = new EdsProject();

        //Copy custom fields too
        /*EdsProjectCustomFields projectCustomFields = project.getProjectCustomFields();
        if(projectCustomFields!=null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(projectCustomFields*//*project.getProjectCustomFields()*//*, commonService.getCompanyCustomFields(ViewName.Project));
            customFieldItems.forEach(cf-> cf.setObjectId(null));
            cloneProjectItem.setCustomFieldItems(customFieldItems);
        }*/

        cloneProject.setName(cloneProjectItem.getProjectName());
        cloneProject.setContractId(cloneProjectItem.getContractId());
        cloneProject.setDescription(cloneProjectItem.getProjectDescription());
        cloneProject.setBillable(cloneProjectItem.isBillable());
        Date sd1 = new Date(cloneProjectItem.getStartDate().getTime());
        Date sd2 = new Date(project.getStartDate().getTime());
        cloneProject.setStartDate(new Date(sd1.getYear(), sd1.getMonth(), sd1.getDate(), sd2.getHours(), sd2.getMinutes(), sd2.getSeconds()));
        if (cloneProjectItem.getDueDate() != null) {
            Date ed1 = new Date(cloneProjectItem.getDueDate().getTime());
            cloneProject.setEndDate(new Date(ed1.getYear(), ed1.getMonth(), ed1.getDate(), 0, 0, 0));
        }
        cloneProject.setDueDate(cloneProject.getEndDate());

        cloneProject.setCreator(user);
        EdsReference status = this.referenceManager.get(cloneProjectItem.getStatusId());
        cloneProject.setStatus(status);
        if (status != null && status.getCode().equals(EdsProject.COMPLETED)) {
            cloneProject.setCompletedDate(new Date());
        } else {
            cloneProject.setCompletedDate(null);
        }
        if (cloneProjectItem.getParentId() != null) {
            cloneProject.setParent(this.projectManager.get(cloneProjectItem.getParentId()));
        }

        List<EdsProjectEmployee> projectEmployees = this.projectEmployeeManager.getProjectEmployees(project);
        Map<Integer, ProjectMember> members = new HashMap<>();
        ProjectMember[] pMembers = cloneProjectItem.getMembers();
        List<Integer> allEmployees = new ArrayList<>();
        for (ProjectMember mem : pMembers) {
            members.put(mem.getId(), mem);
            allEmployees.add(mem.getId());
        }

        if (cloneProjectItem.getClientId() != null && cloneProjectItem.getClientId() != 0) {
            EdsCrmAccount client = this.clientManager.get(cloneProjectItem.getClientId());
            if (client != null) {
                cloneProject.setClient(client);
                this.clientManager.update(client);
            }
        }

        if (isEnableMultiClientToProject) {
            cloneProject.getClients().clear();
            if (cloneProjectItem.getClients() != null) {
                cloneProjectItem.getClients();
                for (SelectItem client : cloneProjectItem.getClients()) {
                    cloneProject.getClients().add(this.clientManager.get(client.getId()));
                }
            }
            if (cloneProject.getClient() != null) {
                cloneProject.getClients().add(project.getClient());
            }
        }

        if (this.employeeManager.get(cloneProjectItem.getManager()) != null &&
                allEmployees.contains(this.employeeManager.get(cloneProjectItem.getManager()).getObjectID())) {
            cloneProject.setManager(this.employeeManager.get(cloneProjectItem.getManager()));
        } else {
            cloneProject.setManager(user.getEmployee());
        }
        this.assignBackupMangers(cloneProject, cloneProjectItem.getBackupManagerIDs());

        if (cloneProjectItem.getLocationId() != null) {
            cloneProject.setProjectLocation(this.locationManager.get(cloneProjectItem.getLocationId()));
        }
        if (cloneProjectItem.getNumberData() != null) {
            cloneProject.setNumber(cloneProjectItem.getNumberData().getNumberString());
            cloneProject.setSavedNumberFormula(cloneProjectItem.getNumberData().getSavedNumberFormula());
            cloneProject.setIntNumber(cloneProjectItem.getNumberData().getIntNumber());
            if (edsSettings != null && (edsSettings.getProjectLastIntNumber() == null || cloneProjectItem.getNumberData().getIntNumber() >= edsSettings.getProjectLastIntNumber())) {
                edsSettings.setProjectLastIntNumber(cloneProjectItem.getNumberData().getIntNumber() + 1);
                this.numberingSettingsManager.createOrUpdate(edsSettings);
            }
        }

        if (cloneProjectItem.getProjectSource() != null) {
            cloneProject.setProjectSource(cloneProjectItem.getProjectSource());
        }
        EdsProjectCustomFields edsProjectCustomFields = this.createProjectCustomFields(cloneProjectItem.getCustomFieldItems());
        cloneProject.setProjectCustomFields(edsProjectCustomFields);
        this.projectManager.create(cloneProject);

        if (cloneProjectItem.getContractId() != null) {
            EdsContract contract = this.contractManager.get(cloneProjectItem.getContractId());
            if (contract != null) {
                contract.setProject(cloneProject);
                this.contractManager.update(contract);
            }
        }

        EdsBusinessEvent projectBusinessEvent = this.baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, cloneProject, user);
        this.baseEventPostProcessor.registerEvent(ProjectManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, cloneProject, user);
        for (EdsEmployee backupManager : cloneProject.getBackupManagers()) {
            this.baseEventPostProcessor.registerEvent(ProjectBackupManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, cloneProject, user, backupManager);
        }

        List<Integer> employeeIds = new ArrayList<>();
        List<EdsProjectEmployee> clonedProjectMembers = new ArrayList<>();
        if (cloneProjectItem.isCopyAssignments()) {
            for (EdsProjectEmployee member : projectEmployees) {
                Integer employeeID = member.getEmployeeDepartment().getEmployee().getObjectID();
                if (members.containsKey(employeeID)) {
                    employeeIds.add(employeeID);
                    EdsProjectEmployee pe = new EdsProjectEmployee();
                    pe.setProject(cloneProject);
                    pe.setEmployeeDepartment(member.getEmployeeDepartment());
                    pe.setWageRate(members.get(employeeID).getWageRate());
                    pe.setClientChargeRate(members.get(employeeID).getClientChargeRate());
                    pe.setWorkloadPercentage(members.get(employeeID).getWorkloadPercentage());
                    EdsProjectEmployeeWageClientRateHistory history = new EdsProjectEmployeeWageClientRateHistory();
                    history.setProjectEmployee(pe);
                    history.setChangeDate(new Date());
                    history.setWageRate(members.get(employeeID).getWageRate());
                    history.setClientChargeRate(members.get(employeeID).getClientChargeRate());
                    history.setWorkloadPercentage(members.get(employeeID).getWorkloadPercentage());

                    this.projectManager.updateEmployeeWageClientRateHistorybyDate(history);

                    this.projectEmployeeManager.create(pe);
                    if (!pe.getEmployeeDepartment().getEmployee().equals(project.getManager())
                            && !project.isUserBackupManager(pe.getEmployeeDepartment().getEmployee().getObjectID()) && allEmployees.contains(pe.getEmployeeDepartment().getEmployee().getObjectID())
                            && !pe.getEmployeeDepartment().getEmployee().getAccountStatus().getCode().equals(Constants.EMPLOYEE_STATUS_NO_ACCCESS)) {
                        this.baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pe, user);

                    }
                    clonedProjectMembers.add(pe);
                }
            }

            List<Integer> newMembers = new ArrayList<>();
            for (ProjectMember mem : pMembers) {
                if (!employeeIds.contains(mem.getId())) {
                    newMembers.add(mem.getId());
                }
            }
            for (Integer newMember : newMembers) {
                ProjectMember pMember = members.get(newMember);
                EdsEmployee employee = this.employeeManager.get(newMember);
                EdsProjectEmployee pEmployee = new EdsProjectEmployee();
                pEmployee.setEmployeeDepartment(employee.getEmployeeTeam());
                pEmployee.setProject(cloneProject);
                pEmployee.setWageRate(pMember.getWageRate());
                pEmployee.setClientChargeRate(pMember.getClientChargeRate());
                pEmployee.setWorkloadPercentage(pMember.getWorkloadPercentage());
                EdsProjectEmployeeWageClientRateHistory history = new EdsProjectEmployeeWageClientRateHistory();
                history.setProjectEmployee(pEmployee);
                history.setChangeDate(new Date());
                history.setWageRate(pMember.getWageRate());
                history.setClientChargeRate(pMember.getClientChargeRate());
                history.setWorkloadPercentage(pMember.getWorkloadPercentage());
                this.projectManager.updateEmployeeWageClientRateHistorybyDate(history);

                this.projectEmployeeManager.create(pEmployee);
                if (!pEmployee.getEmployeeDepartment().getEmployee().equals(cloneProject.getManager())
                        && !cloneProject.isUserBackupManager(pEmployee.getEmployeeDepartment().getEmployee().getObjectID())
                        && !pEmployee.getEmployeeDepartment().getEmployee().getAccountStatus().getCode().equals(Constants.EMPLOYEE_STATUS_NO_ACCCESS)) {
                    this.baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pEmployee, user);
                }
                clonedProjectMembers.add(pEmployee);
            }

        } else {
            cloneProject.setManager(this.employeeManager.get(cloneProjectItem.getManager()));
            this.assignBackupMangers(cloneProject, cloneProjectItem.getBackupManagerIDs());
            Set<Integer> existingEmployees = new HashSet<>();
            for (ProjectMember member : pMembers) {
                if (existingEmployees.contains(member.getId())) {
                    continue;
                }
                existingEmployees.add(member.getId());
                EdsProjectEmployee pe = new EdsProjectEmployee();
                pe.setProject(cloneProject);
                pe.setEmployeeDepartment(this.employeeManager.get(member.getId()).getEmployeeTeam());
                pe.setWageRate(member.getWageRate());
                pe.setClientChargeRate(member.getClientChargeRate());
                pe.setWorkloadPercentage(member.getWorkloadPercentage());
                EdsProjectEmployeeWageClientRateHistory history = new EdsProjectEmployeeWageClientRateHistory();
                history.setProjectEmployee(pe);
                history.setChangeDate(new Date());
                history.setWageRate(member.getWageRate());
                history.setClientChargeRate(member.getClientChargeRate());
                history.setWorkloadPercentage(member.getWorkloadPercentage());
                this.projectManager.updateEmployeeWageClientRateHistorybyDate(history);
                this.projectEmployeeManager.create(pe);
                if (!pe.getEmployeeDepartment().getEmployee().equals(cloneProject.getManager())
                        && !cloneProject.isUserBackupManager(pe.getEmployeeDepartment().getEmployee().getObjectID())
                        && !pe.getEmployeeDepartment().getEmployee().getAccountStatus().getCode().equals(Constants.EMPLOYEE_STATUS_NO_ACCCESS)) {
                    this.baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pe, user);
                }
                clonedProjectMembers.add(pe);
            }

        }

        this.roleManager.addRole(cloneProject.getManager(), Constants.PM);


        if (cloneProjectItem.isCopyTasks() || cloneProjectItem.isCopyAssignmentsToAllProjectMembers()) {
            //Clone Tasks and Workstreams
            EdsBusinessEvent event = this.baseEventPostProcessor.registerEvent(TaskCloneListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, cloneProject, user);
            event.setCustomStringField(new Gson().toJson(cloneProjectItem));
        }


        this.commonServiceLocal.createProjectFolder(cloneProject.getObjectID());

        if (cloneProjectItem.getAttachments() != null && cloneProjectItem.getAttachments().length > 0) {
            this.saveProjectAttachments(cloneProjectItem.getAttachments(), cloneProject);
        }

        if (cloneProject.getObjectID() != null && cloneProjectItem.isRelationChanged()) {
            this.allInOneService.saveRelations(RelationItem.TYPE_PROJECT, cloneProject.getObjectID(),
                    cloneProject.getName(), cloneProjectItem.getRelations());
        }

        try {
//            this.solrManager.indexAddProject(cloneProject, Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
            projectSolrComponent.index(cloneProject);
            projectBusinessEvent.setSolrIndexed(true);
        } catch (Exception e) {
            projectBusinessEvent.setSolrIndexed(false);
            e.printStackTrace();
        }
        EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, cloneProject, user);
        workflowEvent.setEntityType(RelationItem.TYPE_PROJECT);
        return cloneProject.getObjectID();
    }

    private EdsReference getTaskStatus(CloneProjectItem cloneProjectItem) {
        if (cloneProjectItem.getTaskItem().getStatus() == null) {
            return this.referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
        } else {
            return this.referenceManager.get(cloneProjectItem.getTaskItem().getStatus());
        }
    }

    private void assignBackupMangers(EdsProject project, List<Integer> backupManagers) {
        int index = 1;
        project.setBackupManagersBeforeEdit(project.getBackupManagerIDs());
        if (backupManagers == null) {
            backupManagers = new ArrayList<>();
        }
        for (Integer backupManagerID : backupManagers) {
            EdsEmployee backupManager = this.employeeManager.get(backupManagerID);
            switch (index) {
                case 1 -> project.setBackupManager(backupManager);
                case 2 -> project.setBackupManager2(backupManager);
                case 3 -> project.setBackupManager3(backupManager);
                case 4 -> project.setBackupManager4(backupManager);
                case 5 -> project.setBackupManager5(backupManager);
                case 6 -> project.setBackupManager6(backupManager);
                case 7 -> project.setBackupManager7(backupManager);
                case 8 -> project.setBackupManager8(backupManager);
                case 9 -> project.setBackupManager9(backupManager);
                case 10 -> project.setBackupManager10(backupManager);
            }
            this.roleManager.addRole(backupManager, Constants.PM);
            index++;
        }
        project.clearProjectManagers(backupManagers.size());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public KpiTreeInfo[] getProjectEmployeesForView(Integer projectID) {
        List<EdsProjectEmployee> pemployees = this.projectManager.getEmployeesByProject(projectID);
        EdsDepartment department;
        KpiTreeInfo[] employees = null;
        if (pemployees != null) {
            employees = new KpiTreeInfo[pemployees.size()];
            int count = 0;
            for (EdsProjectEmployee prEmp : pemployees) {
                department = prEmp.getEmployeeDepartment().getEmployee().getTeam();
                EdsUser emp = prEmp.getEmployeeDepartment().getEmployee();
                employees[count] = new KpiTreeInfo();
                employees[count].setId(emp.getObjectID());
                employees[count].setName(emp.getName());
                employees[count].setEmployeeId(emp.getObjectID());
                employees[count].setDepartmentId(department.getObjectID());
                employees[count].setDepartmentName(department.getName());
                employees[count].setSelected(true);
                count++;
            }
        }
        return employees;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public KpiTreeInfo[] getProjectEmployeesHistory(Integer projectID) {
        List<EdsProjectEmployee> pEmployees = this.projectManager.getProjectEmployeesHistoryByProject(projectID);
        EdsDepartment department;
        KpiTreeInfo[] employees = null;
        if (pEmployees != null) {
            employees = new KpiTreeInfo[pEmployees.size()];
            int count = 0;
            for (EdsProjectEmployee prEmp : pEmployees) {
                department = prEmp.getEmployeeDepartment().getEmployee().getTeam();
                EdsUser emp = prEmp.getEmployeeDepartment().getEmployee();
                employees[count] = new KpiTreeInfo();
                employees[count].setId(emp.getObjectID());
                employees[count].setName(emp.getName());
                employees[count].setEmployeeId(emp.getObjectID());
                employees[count].setWageRate(prEmp.getWageRate());
                employees[count].setClientChargeRate(prEmp.getClientChargeRate());
                employees[count].setWorkloadPercentage(prEmp.getWorkloadPercentage());
                employees[count].setDepartmentId(department.getObjectID());
                employees[count].setDepartmentName(department.getName());
                employees[count].setStartDate(prEmp.getStartDate());
                employees[count].setEndDate(prEmp.getEndDate());
                EdsAuditInfo auditInfo = prEmp.getAuditInfo();
                if (auditInfo != null) {
                    employees[count].setLastUpdateDate(auditInfo.getModificationDate());
                }

                employees[count].setSelected(true);
                count++;
            }
        }
        return employees;
    }

    /**
     * <h1>... Project List Rpc object fill with solr data ...</h1>
     * <br/>
     * <h2>... Write developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {17:00 06/06/2011} ...</h3>
     *
     * @param resp
     * @param fp
     * @param enableMultiClientToProject
     * @return
     */
    private ListResult<ProjectListItem> getProjectListInSolr(Page<ProjectSolrDoc> resp, ListingFilterParameter fp, boolean enableMultiClientToProject) {
        //If the Project Cost has been enabled to the company, then project cost calculation functional include to the method logic
        boolean enableProjectCostInListing = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_COST_IN_LISTING_ENABLED);
        boolean enableProjectCostInListingFromBudget = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_COST_IN_LISTING_FROM_BUDGET_ENABLED);

        HashMap<Integer, Double> projectPlannedExpenseMap = new HashMap<>();
        HashMap<Integer, Double> projectPlannedIncomeMap = new HashMap<>();
        HashMap<Integer, BigDecimal> projectActualExpenseMap = new HashMap<>();
        HashMap<Integer, BigDecimal> projectActualIncomeMap = new HashMap<>();
        HashMap<Integer, Double[]> projectCostAndTimeMap = new HashMap<>();

        HashMap<Integer, Double> projectWaitingTimeMap = new HashMap<>();
        HashMap<Integer, Double> projectRejectedTimeMap = new HashMap<>();

        ListPanelToolRpc panelSettings = fp.getListPanelTool();
        if (panelSettings == null) {//Default View Column Code Name
            panelSettings = ListPanelToolRpc.createIntance();
            panelSettings.setColumnCodeName(new ArrayList<>(Arrays.asList(ProjectListItem.NUMBER, ProjectListItem.NAME,
                    ProjectListItem.MANAGER, ProjectListItem.CLIENT,
                    ProjectListItem.HEAD_COUNT, ProjectListItem.STATUS,
                    ProjectListItem.START_DATE, ProjectListItem.END_DATE)));
        }
        int totalCount = (int) resp.getTotalElements();
        EdsProject edsDefaultProject = this.userManager.getUser().getCompany().getDefaultProject();
        EdsProject crmProject = this.projectManager.getCrmProject();
        ArrayList<ProjectListItem> projectList = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        resp.getContent().forEach(e -> ids.add(e.getProjectId()));
        Map<Integer, Long> projectTaskCounts = projectManager.getProjectTaskCounts(ids);
        List<Integer> existingProjectIDs = projectManager.getProjectIDsByProjectIDs(ids);

        String projectIDs = ServerUtils.getAsCommoDelimited(existingProjectIDs, "0");

        //If the Project Cost has been enabled, then let's do it...
        if (enableProjectCostInListing) {
            if (!enableProjectCostInListingFromBudget) {
                projectPlannedExpenseMap = this.projectBudgetManager.getPlannedExpenseByProjectIDs(projectIDs);
                projectPlannedIncomeMap = this.projectBudgetManager.getPlannedIncomeByProjectIDs(projectIDs);
                projectActualExpenseMap = this.projectBudgetManager.getActualExpenseByProjectIDs(projectIDs);
                projectActualIncomeMap = this.projectBudgetManager.getActualIncomeByProjectIDs(projectIDs);
            } else {
                EdsAccount vatLiability = this.accountingManager.getAccountByKey(EdsAccount.VAT_PAYABLE);
                boolean isAgencyFees = vatLiability != null && vatLiability.getAccountType() != null && Constants.REVENUE.equals(vatLiability.getAccountType().getCategory());

                projectPlannedExpenseMap = this.projectBudgetManager.getPlannedExpenseFromBudgetByProjectIDs(projectIDs, isAgencyFees);
                projectPlannedIncomeMap = this.projectBudgetManager.getPlannedIncomeFromBudgetByProjectIDs(projectIDs, isAgencyFees);
                projectActualExpenseMap = this.projectBudgetManager.getActualExpenseFromBudgetByProjectIDs(projectIDs, isAgencyFees);
                projectActualIncomeMap = this.projectBudgetManager.getActualIncomeFromBudgetByProjectIDs(projectIDs, isAgencyFees);
            }
        }

        ArrayList<String> activeColumns = fp.getListPanelTool() != null ? fp.getListPanelTool().getColumnCodeName() : new ArrayList<>();
        if (activeColumns.contains(ProjectListItem.ACTUAL_TIME_SPENT)
                || activeColumns.contains(ProjectListItem.HOURS_SPENT)
                || activeColumns.contains(ProjectListItem.ESTIMATED_TIME)
                || activeColumns.contains(ProjectListItem.PLANED_COST)
                || activeColumns.contains(ProjectListItem.COST)) {
            projectCostAndTimeMap = this.timeSheetManager.getCostAndTimeSpentOnProjects(projectIDs);
        }
        if (activeColumns.contains(ProjectListItem.WAITING_HOURS)) {
            projectWaitingTimeMap = this.timeSheetManager.getProjectTimeSpents(projectIDs, EdsTimeSheet._WAITING);
        }
        if (activeColumns.contains(ProjectListItem.REJECTED_HOURS)) {
            projectRejectedTimeMap = this.timeSheetManager.getProjectTimeSpents(projectIDs, EdsTimeSheet._REJECT);
        }

        ProjectListItem projectTotalCalculationItem = new ProjectListItem();
        for (ProjectSolrDoc solrDoc : resp.getContent()) {
            ProjectListItem projectRpc = new ProjectListItem();
            Integer projectId = solrDoc.getProjectId();
            Double[] projectCostAndTimeSpent = projectCostAndTimeMap.get(projectId);

            EdsContract contract = this.contractManager.getContractByProjectId(projectId);

            BigDecimal projectPlanedIncome;
            BigDecimal projectPlanedExpense;

            BigDecimal projectIncome;
            BigDecimal projectExpense;

            if (existingProjectIDs.contains(projectId)) {
                projectRpc.setObjectId(projectId);
                if (edsDefaultProject != null) {
                    projectRpc.setDefaultProjectId(edsDefaultProject.getObjectID());
                }
                if (crmProject != null) {
                    projectRpc.setCrmProjectId(crmProject.getObjectID());
                }
                projectRpc.setProjectCreatorID(solrDoc.getProjectCreatorId());
                projectRpc.setNumber(solrDoc.getProjectNumber());
                projectRpc.setName(solrDoc.getProjectName());
                if (fp.isLookUp()) {
                    projectList.add(projectRpc);
                    continue;
                }
                if (contract != null && !contract.getDeleted()) {
                    projectRpc.setContractName(contract.getNumber());
                    projectRpc.setContractId(contract.getObjectID());
                }
                projectRpc.setDescription(solrDoc.getDescription());
                projectRpc.setManager(solrDoc.getManagerName());
                projectRpc.setManagerId(solrDoc.getManagerId());
                projectRpc.setBackupManager(ServerUtils.asListToString(solrDoc.getBackupManagerName()));
                projectRpc.setBackupManagerIDs(solrDoc.getBackupManagerId());
                projectRpc.setClient(enableMultiClientToProject ? ServerUtils.asListToString(solrDoc.getProjectMultiClientName()) : solrDoc.getClientName());
                projectRpc.setActualHoursSpent(projectCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_TIME_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_TIME_SPENT].intValue() : 0) : "00:00");
                projectRpc.setHoursSpent(projectCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(projectCostAndTimeSpent[Constants.PROJECT_HOURS_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_HOURS_SPENT].intValue() : 0) : "00:00");
                projectRpc.setWaitingHours(projectWaitingTimeMap.get(projectId) != null ? ServerUtils.getTimeSpentHM(projectWaitingTimeMap.get(projectId).intValue()) : "00:00");
                projectRpc.setRejectedHours(projectRejectedTimeMap.get(projectId) != null ? ServerUtils.getTimeSpentHM(projectRejectedTimeMap.get(projectId).intValue()) : "00:00");
                projectRpc.setStatus(referenceWfmMessageSource.localize(solrDoc.getStatusCode(), solrDoc.getStatusName()));
                projectRpc.setStatusCode(solrDoc.getStatusCode());
                projectRpc.setStatusId(solrDoc.getStatusId());
                projectRpc.setStartDate(solrDoc.getStartDate());
                projectRpc.setEndDate(solrDoc.getDueDate());
                projectRpc.setDueDate(projectRpc.getEndDate());
                projectRpc.setInvoiceNumber(solrDoc.getInvoice());
                projectRpc.setProjectLocation(solrDoc.getLocationName());
                projectRpc.setHeadCount(solrDoc.getUserId().size());
                String createdBy = solrDoc.getProjectCreator();
                projectRpc.setCreatedBy(createdBy != null ? createdBy : this.commonLocalizer.localize("notAvailable", "N/A"));
                projectRpc.setEstimatedTime(projectCostAndTimeSpent != null ? (projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_TIME_SPENT] != null ? projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_TIME_SPENT].intValue() : 0) : 0);

                Date createdDate = solrDoc.getProjectCreatedDate();
                projectRpc.setCreatedDate(createdDate);

                String modifiedBy = solrDoc.getProjectModifiedBy();
                projectRpc.setModifiedBy(modifiedBy != null ? modifiedBy : this.commonLocalizer.localize("not available", "N/A"));

                Date modifiedDate = solrDoc.getProjectModifiedDate();
                projectRpc.setModifiedDate(modifiedDate);

                //project calculation field logic
                projectPlanedIncome = BigDecimal.valueOf(projectPlannedIncomeMap.get(projectId) != null ? projectPlannedIncomeMap.get(projectId) : 0.0);
                projectPlanedExpense = BigDecimal.valueOf(projectPlannedExpenseMap.get(projectId) != null ? projectPlannedExpenseMap.get(projectId) : 0.0);

                projectIncome = projectActualIncomeMap.get(projectId) != null ? projectActualIncomeMap.get(projectId) : BigDecimal.ZERO;
                projectExpense = projectActualExpenseMap.get(projectId) != null ? projectActualExpenseMap.get(projectId) : BigDecimal.ZERO;

                BigDecimal projectPlanedWageAmount = BigDecimal.valueOf(projectCostAndTimeSpent != null ? (projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_COST] != null ? projectCostAndTimeSpent[Constants.PROJECT_ESTIMATED_COST] : 0d) : 0d);
                projectPlanedExpense = projectPlanedWageAmount != null ? projectPlanedExpense.add(projectPlanedWageAmount) : projectPlanedExpense;

                BigDecimal projectActualWageAmount = BigDecimal.valueOf(projectCostAndTimeSpent != null ? (projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_COST] != null ? projectCostAndTimeSpent[Constants.PROJECT_ACTUAL_COST] : 0d) : 0d);
                projectExpense = projectActualWageAmount != null ? projectExpense.add(projectActualWageAmount) : projectActualWageAmount;

                projectRpc.setPlanedIncome(projectPlanedIncome);
                projectRpc.setIncome(projectIncome);
                projectRpc.setPlanedCost(projectPlanedExpense);
                projectRpc.setCost(projectExpense);

                BigDecimal difference;
                BigDecimal planedProfit;
                BigDecimal profit = null;

                if (projectPlanedIncome != null && projectPlanedExpense != null) {
                    planedProfit = projectPlanedIncome.subtract(projectPlanedExpense);
                    projectRpc.setPlanedProfit(planedProfit);
                }

                if (projectIncome != null && projectExpense != null) {
                    profit = projectIncome.subtract(projectExpense);
                    projectRpc.setProfit(profit);
                }

                if (profit != null && projectIncome != null && projectIncome.compareTo(BigDecimal.ZERO) > 0) {
                    difference = profit.divide(projectIncome, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2, RoundingMode.UP);
                    projectRpc.setDifference(difference);
                } else {
                    projectRpc.setDifference(BigDecimal.ZERO);
                }

                if (solrDoc.getCompleted() != null) {
                    Float aDouble = solrDoc.getCompleted();
                    float str = new BigDecimal(aDouble).setScale(2, RoundingMode.HALF_UP).floatValue();
                    projectRpc.setComplete(String.valueOf(((str > 100f && !this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) ? 100f : str)));
                } else {
                    projectRpc.setComplete("0.0");
                }
                projectRpc.setTaskCount(projectTaskCounts.get(projectId));
                if (panelSettings != null) {
                    projectRpc.setCustomFields(CustomFieldsUtils.getBaseSolrDocDynamicFields(solrDoc, panelSettings.getColumnCodeName()));
                }
                projectRpc.setRelationValueMap(SolrRelationUtils.getRelationBaseSolrDocValue(solrDoc, EdsRelation.TYPE_PROJECT));
                projectRpc.setBillable(solrDoc.getBillible());

                projectList.add(projectRpc);

                projectTotalCalculationItem.setPlanedIncome(projectTotalCalculationItem.getPlanedIncome().add(projectRpc.getPlanedIncome()));
                projectTotalCalculationItem.setIncome(projectTotalCalculationItem.getIncome().add(projectRpc.getIncome()));
                projectTotalCalculationItem.setPlanedCost(projectTotalCalculationItem.getPlanedCost().add(projectRpc.getPlanedCost()));
                projectTotalCalculationItem.setCost(projectTotalCalculationItem.getCost().add(projectRpc.getCost()));
            }
        }
        if (projectTotalCalculationItem != null) {
            projectTotalCalculationItem.setPlanedProfit(projectTotalCalculationItem.getPlanedIncome().subtract(projectTotalCalculationItem.getPlanedCost()));
            projectTotalCalculationItem.setProfit(projectTotalCalculationItem.getIncome().subtract(projectTotalCalculationItem.getCost()));

            if (projectTotalCalculationItem.getProfit() != null && projectTotalCalculationItem.getIncome().compareTo(BigDecimal.ZERO) > 0) {
                projectTotalCalculationItem.setDifference(projectTotalCalculationItem.getProfit().divide(projectTotalCalculationItem.getIncome(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            } else {
                projectTotalCalculationItem.setDifference(BigDecimal.ZERO);
            }
        }
        return new ListResult<>(projectList, totalCount, projectTotalCalculationItem);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getDefaultProjectID() {
        EdsUser user = this.employeeManager.getUser();
        if (user.getCompany().getDefaultProject() != null) {
            return user.getCompany().getDefaultProject().getObjectID();
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DashboardTasks[] getProjectResourceLoad(ListingFilterParameter fp, int period) {
        List projectTasksList = this.employeeTaskManager.getProjectResourceLoad(fp);


        List<DashboardTasks> dashBoaardTaskList = new ArrayList<>();
        DashboardTasks dashboardTasks = null;
        Integer employeeId = null;
        for (Object aProjectTasksList : projectTasksList) {
            Object[] object = (Object[]) aProjectTasksList;
            Integer id = (Integer) object[0];
            BigInteger notStarted = (BigInteger) object[1];
            BigInteger inProgress = (BigInteger) object[2];
            BigInteger completed = (BigInteger) object[3];
            BigInteger waiting = (BigInteger) object[4];
            BigInteger closed = (BigInteger) object[5];
            if (employeeId == null || !employeeId.equals(id)) {
                employeeId = id;
                dashboardTasks = new DashboardTasks();
                dashboardTasks.setEmployeeId(id);
                dashboardTasks.setEmployeeName(this.employeeManager.get(id).getName());
                dashBoaardTaskList.add(dashboardTasks);
            }
            if (notStarted != null) {
                dashboardTasks.setNotStarted(notStarted.intValue());
            } else if (inProgress != null) {
                dashboardTasks.setInProgress(inProgress.intValue());
            } else if (completed != null) {
                dashboardTasks.setCompleted(completed.intValue());
            } else if (waiting != null) {
                dashboardTasks.setWaiting_for(waiting.intValue());
            } else if (closed != null) {
                dashboardTasks.setClosed(closed.intValue());
            }
        }


        dashBoaardTaskList.sort(Comparator.comparing(DashboardTasks::getEmployeeName));

        if (period < 0) {  //need to fix call from resoureceWorkLoad

            List tasksList = this.employeeTaskManager.getProjectTasksResourceLoad(fp);

            BigInteger notStarted = new BigInteger("0");
            BigInteger inProgress = new BigInteger("0");
            BigInteger completed = new BigInteger("0");
            BigInteger waiting = new BigInteger("0");
            BigInteger closed = new BigInteger("0");

            for (Object aTasksList : tasksList) {
                Object[] object = (Object[]) aTasksList;

                if (object[1] != null) {
                    notStarted = notStarted.add((BigInteger) object[1]);
                } else if (object[2] != null) {
                    inProgress = inProgress.add((BigInteger) object[2]);
                } else if (object[3] != null) {
                    completed = completed.add((BigInteger) object[3]);
                } else if (object[4] != null) {
                    waiting = waiting.add((BigInteger) object[4]);
                } else if (object[5] != null) {
                    closed = closed.add((BigInteger) object[5]);
                }
            }
            DashboardTasks totalTaskDashboard = new DashboardTasks();
            totalTaskDashboard.setNotStarted(notStarted.intValue());
            totalTaskDashboard.setInProgress(inProgress.intValue());
            totalTaskDashboard.setCompleted(completed.intValue());
            totalTaskDashboard.setWaiting_for(waiting.intValue());
            totalTaskDashboard.setClosed(closed.intValue());
            dashBoaardTaskList.add(totalTaskDashboard);
        }
        return dashBoaardTaskList.toArray(new DashboardTasks[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DashboardIssues[] getProjectDashboardIssues(ListingFilterParameter fp, int period) {
        List issuesList = this.issueManager.getProjectIssue(fp);

        List<DashboardIssues> issues86List = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        int k = -1;
        DashboardIssues issue = null;
        Integer employeeId = null;
        for (Object anIssuesList : issuesList) {
            Object[] object = (Object[]) anIssuesList;
            Integer id = (Integer) object[0];
            BigInteger neww = (BigInteger) object[1];
            BigInteger open = (BigInteger) object[2];
            BigInteger under = (BigInteger) object[3];
            BigInteger inprogress = (BigInteger) object[4];
            BigInteger review = (BigInteger) object[5];
            BigInteger resolved = (BigInteger) object[6];
            BigInteger closed = (BigInteger) object[7];
            if (employeeId == null || !employeeId.equals(id)) {
                k++;
                employeeId = id;
                issue = new DashboardIssues();
                issue.setEmployeeId(id);
                issue.setEmployeeName(this.employeeManager.get(id).getName());
                if (map.size() != 0 && map.containsKey(id)) {
                    issue = issues86List.get(map.get(id));
                    k--;
                } else {
                    map.put(id, k);
                    issues86List.add(issue);
                }
            }
            if (neww != null) {
                issue.setNeww(issue.getNeww() + neww.intValue());
            } else if (open != null) {
                issue.setOpen(issue.getOpen() + open.intValue());
            } else if (under != null) {
                issue.setUnder(issue.getUnder() + under.intValue());
            } else if (inprogress != null) {
                issue.setInProgress(issue.getInProgress() + inprogress.intValue());
            } else if (review != null) {
                issue.setReview(issue.getReview() + review.intValue());
            } else if (resolved != null) {
                issue.setResolved(issue.getResolved() + resolved.intValue());
            } else if (closed != null) {
                issue.setClosed(issue.getClosed() + closed.intValue());
            }
        }

        issues86List.sort(Comparator.comparing(DashboardIssues::getEmployeeName));


        if (period < 0) { //need to fix call from workLoad

            List totalIssuesList = this.issueManager.getProjectIssueStatistic(fp);

            BigInteger neww = new BigInteger("0");
            BigInteger open = new BigInteger("0");
            BigInteger under = new BigInteger("0");
            BigInteger inprogress = new BigInteger("0");
            BigInteger review = new BigInteger("0");
            BigInteger resolved = new BigInteger("0");
            BigInteger closed = new BigInteger("0");

            for (Object aTotalIssuesList : totalIssuesList) {

                Object[] object = (Object[]) aTotalIssuesList;

                if (object[1] != null) {
                    neww = neww.add((BigInteger) object[1]);
                } else if (object[2] != null) {
                    open = open.add((BigInteger) object[2]);
                } else if (object[3] != null) {
                    under = under.add((BigInteger) object[3]);
                } else if (object[4] != null) {
                    inprogress = inprogress.add((BigInteger) object[4]);
                } else if (object[5] != null) {
                    review = review.add((BigInteger) object[5]);
                } else if (object[6] != null) {
                    resolved = resolved.add((BigInteger) object[6]);
                } else if (object[7] != null) {
                    closed = closed.add((BigInteger) object[7]);
                }
            }

            DashboardIssues totalIssue = new DashboardIssues();
            totalIssue.setNeww(neww.intValue());
            totalIssue.setOpen(open.intValue());
            totalIssue.setUnder(under.intValue());
            totalIssue.setInProgress(inprogress.intValue());
            totalIssue.setReview(review.intValue());
            totalIssue.setResolved(resolved.intValue());
            totalIssue.setClosed(closed.intValue());
            issues86List.add(totalIssue);
        }


        return issues86List.toArray(new DashboardIssues[]{});

    }

    /*Delete function*/

    public void deleteProject(Integer projectId) {
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsProject.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(projectId);
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Project deleted");

        EdsProject project = this.projectManager.get(projectId);
        EdsProject defaultProject = this.employeeManager.getUser().getCompany().getDefaultProject();
        /*if project equals default project that don't delete project*/
        if (project.equals(defaultProject)) {
            return;
        }
        EdsUser user = this.employeeManager.getUser();
        //Who is deleted this project
        project.setUpdater(user);
        project.setLastUpdateTime(user.getCompany().getCompanyDate());

        EdsContract contract = this.contractManager.getContractByProjectId(project.getObjectID());
        if (contract != null) {
            contract.setProject(null);
        }

        //Delete project
        this.projectManager.deleteProject(project);
        this.baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, project, user);

        //Delete all members
        for (EdsProjectEmployee projectEmployee : this.projectEmployeeManager.getProjectEmployees(project)) {
            if (projectEmployee.getEmployeeDepartment().getEmployee().equals(project.getManager())) {
                this.baseEventPostProcessor.registerEvent(ProjectManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, project, user);
            } else if (project.getBackupManagers().size() > 0 && project.getBackupManagers().contains(projectEmployee.getEmployeeDepartment().getEmployee())) {
                this.baseEventPostProcessor.registerEvent(ProjectBackupManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, project, user, projectEmployee.getEmployeeDepartment().getEmployee());
            } else {
                this.baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, projectEmployee, user);
            }
        }
        this.projectEmployeeManager.deleteProjectInPE(project);


        //update linked SQ and SI
        this.baseEventPostProcessor.registerEvent(AccountingProjectSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, project, user);

        //update converted opportunities
        if (project.getProjectSource() != null && project.getProjectSource().startsWith(Constants.PROJECT_SOURCE_CONVERT_FROM_OPPORTUNITY)) {
            try {
                Integer opportunityID = Integer.parseInt(project.getProjectSource().replace(Constants.PROJECT_SOURCE_CONVERT_FROM_OPPORTUNITY, ""));
                EdsOpportunity opportunity = this.opportunityManager.get(opportunityID);
                if (opportunity != null) {
                    opportunity.setConvertedToProject(false);
                    this.opportunityManager.update(opportunity);
                    opportunitySolrComponent.index(opportunity);
                }
            } catch (NumberFormatException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Delete project all tasks
        this.taskManager.deleteProjectTasks(project);
        this.baseEventPostProcessor.registerEvent(ProjectSolrSensitiveDataChangeListener.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, project, user);

        FolderResource folderResource = this.commonServiceLocal.getFolderResource(Constants.F_PROJECT, project.getObjectID());
        if (folderResource != null) {
            try {
                this.commonServiceLocal.deleteFolder(folderResource.getObjectId());
            } catch (InsufficientPermissionsException | ObjectNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Delete project Sub projects
        List<EdsProject> subProjectList = this.projectManager.getSubProjects(project);
        for (EdsProject subProject : subProjectList) {
            this.deleteProject(subProject.getObjectID());
        }
        EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, project, user);
        workflowEvent.setEntityType(RelationItem.TYPE_PROJECT);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ProjectInvoice> getInvoiceList(Integer projectId, ListingFilterParameter fp) {
        ListingObjectItem invoices = this.projectManager.getInvoiceList(projectId, fp);
        ArrayList<ProjectInvoice> result = new ArrayList<>();
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<EdsSaleInvoice> saleInvoices = (List<EdsSaleInvoice>) invoices.getItems();
        for (EdsSaleInvoice invoice : saleInvoices) {
            ProjectInvoice items = new ProjectInvoice();
            items.setID(invoice.getObjectID());
            items.setInvoiceNumber(invoice.getNumber());
            items.setDueDate(new DateNonConvertable(invoice.getDueDate()));
            items.setInvoiceDate(new DateNonConvertable(invoice.getInvoiceDate()));
            items.setClientName(invoice.getClientOrSupplier().getName());
            items.setStatus(this.referenceWfmMessageSource.localize(invoice.getStatus().getCode(), invoice.getStatus().getName()));
            items.setTotal(invoice.getTotal() != null ? invoice.getTotal().doubleValue() : 0d);
            items.setCreatorName(invoice.getCreator() != null ? invoice.getCreator().getName() : "");
            items.setClientName(invoice.getClient() != null ? invoice.getClient().getName() : "");
            items.setCurrencyName(invoice.getCurrency() != null ? invoice.getCurrency().getName() : "");
            items.setTotalInInvoiceCurrency(invoice.getTotalInInvoiceCurrency());
            items.setFullPayment(invoice.getFullPayments());
            items.setFullPaymentInBase(invoice.getFullPaymentsInBase());
            items.setPoNumber(invoice.getPoNumber());
            if (invoice.getCustomFields() != null && panelTools != null) {
                items.setCustomFields(CustomFieldsUtils.getRPCCustomFields(invoice.getCustomFields(), panelTools.getColumnCodeName()));
            }
            result.add(items);
        }
        return new ListResult<>(result, invoices.getTotalCount());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HistoryListItem[] getProjectNotes(Integer projectID, Integer limit) {
        return this.getProjectNotes(projectID, limit, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HistoryListItem[] getProjectNotes(Integer projectID, Integer limit, boolean withAllTaskNotes) {
        EdsProject project = this.projectManager.get(projectID);
        HistoryListItem[] projectNotes;
        if (project != null) {
            ListingFilterParameter filterParametrs = new ListingFilterParameter();
            filterParametrs.setProjectId(projectID);
            if (withAllTaskNotes) {
                List<EdsTask> projectTasks = this.taskManager.listByProjectAndEmployee(projectID);
                String projectTasksIdsAsCommoDelimited = ServerUtils.getAsCommoDelimited(projectTasks, "(0)");
                filterParametrs.setTaskIds(projectTasksIdsAsCommoDelimited);
                filterParametrs.setWithAllTaskNotes(withAllTaskNotes);
            }
            if (limit != null && limit != 0) {
                filterParametrs.setLimit(limit);
            }
            EdsNoteHistory[] projectNote = this.noteHistoryManager.getNoteList(filterParametrs).toArray(new EdsNoteHistory[]{});
            projectNotes = new HistoryListItem[projectNote.length];
            EdsUser user = this.employeeManager.getUser();
            int i = 0;
            for (EdsNoteHistory notes : projectNote) {
                HistoryListItem items = new HistoryListItem();
                items.setObjectID(notes.getObjectID());
                items.setEmployee(notes.getEmployee().getName());
                items.setSubject(notes.getSubject());
                boolean isTaskRelatedE = EdsNoteHistory.TASK == notes.getRelatedTo() && notes.getRelatedId() != null;
                items.setRelatedToId(notes.getRelatedTo());
                if (withAllTaskNotes && isTaskRelatedE) {
                    EdsTask task = this.taskManager.get(notes.getRelatedId());
                    items.setRelatedName(task.getName());
                    items.setRelatedToName("Task");
                    String taskID = "task|summary/" + task.getObjectID();
                    items.setSectionLink("ProjectManagement.html?link=");
                    items.setRelatedToLink(taskID);
                }
                items.setComment(notes.getComment());
                items.setVisibility(notes.isVisibility());
                items.setEventDate(notes.getEventDate() != null ? new Date(notes.getEventDate().getTime()) : null);
                items.setEditable(user.equals(notes.getEmployee()) && !isTaskRelatedE);
                NewsComment[] noteComments = this.getProjectNoteComments(notes.getObjectID());
                if (noteComments.length > 0) {
                    items.setNotesComments(noteComments);
                } else {
                    items.setNotesComments(new NewsComment[0]);
                }
                projectNotes[i++] = items;
            }
            return projectNotes;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewsComment[] getProjectNoteComments(Integer noteID) {
        return this.commonService.getNotecomments(noteID);
    }

    public NewsComment saveProjectNoteComments(NewsComment data) {
        return this.commonService.saveNoteComment(data);
    }

    @Override
    public void deleteProjectNoteComment(Integer commentId) {
        EdsNoteComment noteComment = this.noteCommentManager.get(commentId);
        this.noteCommentManager.delete(noteComment);
    }

    @Transactional
    public void indexProjectTasks(Integer projectID) {
        this.taskRbacManager.removeProjectRelatedEntriesNative(projectID);
        EdsProject project = this.projectManager.get(projectID);
        EdsCompany company = this.companyManager.get(Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
        try {
            this.solrManager.removeProjectRelatedAllTaskRbacRecords(project, company);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        Integer start = 0;
        while (start != -1) {
            start = this.taskServiceLocal.indexProjectTasks(projectID, start, 20);
        }
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     * indexes company tasks
     *
     * @param solrReindexRpc
     */
    public void indexCompanyTasks(SolrReindexRpc solrReindexRpc) {
        ServerSecurityContext.getInstance().setCompanyId(solrReindexRpc.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindexRpc.getCompanyId()));
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.TASK_MAX_FORM, null, false);
        this.solrDbConsistencyManager.removeInconsistences(solrReindexRpc.getCompanyId(), EdsSolrDbConsistency.TASK);
        this.solrDbConsistencyManager.flushAndClear();
        try {
            if (solrReindexRpc.isAllReindex()) {
                this.solrManager.removeCompanyTasks(solrReindexRpc.getCompanyId());
            } else if (solrReindexRpc.getLastUpdateTime() != null) {
                List<Integer> deleteTaskIds = this.taskManager.getCompanyDeleteTasksForSolr(solrReindexRpc);
                this.solrManager.removeCompanyTasksbyIds(deleteTaskIds.toArray(new Integer[]{}));
            }
        } catch (SolrServerException | IOException e) {
            log.error("Error Task Index. Company ID : {} , Message : {} ", solrReindexRpc.getCompanyId(), e.getMessage());
        }
        int start = 0;
        int limit = 1000;

        try {
            List<EdsTask> tasks = taskManager.getCompanyTasksForSolr(solrReindexRpc, start, limit);
            while (!tasks.isEmpty()) {
                taskSolrComponent.indexConcurrently(tasks);
                taskManager.flushAndClear();
                start++;
                tasks = taskManager.getCompanyTasksForSolr(solrReindexRpc, (start * limit), limit);
            }
        } catch (Exception e) {
            log.error("Error Task Index. Company ID : {} , Message : {} ", solrReindexRpc.getCompanyId(), e.getMessage());
        }

        solrDbConsistencyManager.flushAndClear();
    }

    @Transactional
    public void indexCompanyProjects(SolrReindexRpc solrReindexRpc) {
        ServerSecurityContext.getInstance().setCompanyId(solrReindexRpc.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindexRpc.getCompanyId()));
        profileService.clearFromDbDeletedCustomFieldsByFormId(LayoutRPC.PROJECT_FORM, null, false);
        this.solrDbConsistencyManager.removeInconsistences(solrReindexRpc.getCompanyId(), EdsSolrDbConsistency.PROJECT);
        this.solrDbConsistencyManager.flushAndClear();

        try {
            if (solrReindexRpc.isAllReindex()) {
                this.solrManager.removeCompanyProjects(solrReindexRpc.getCompanyId());
            } else if (solrReindexRpc.getLastUpdateTime() != null) {
                List<Integer> deleteProjectIds = this.projectManager.getCompanyDeleteProjectsForSolr(solrReindexRpc);
                this.solrManager.removeCompanyProjectsById(deleteProjectIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Project Index. Company ID : {} , Message : {} ", solrReindexRpc.getCompanyId(), e.getMessage());
        }

        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit

        List<EdsProject> projects = projectManager.getCompanyProjectsForSolr(solrReindexRpc, start, limit);
        while (!projects.isEmpty()) {
            try {
                projectSolrComponent.indexConcurrently(projects);
            } catch (SolrServerException | IOException | InterruptedException e) {
                log.error("Error Project Index. Company ID : {} , Message : {} ", solrReindexRpc.getCompanyId(), e.getMessage());
            }
            this.projectManager.flushAndClear();
            start++;
            projects = projectManager.getCompanyProjectsForSolr(solrReindexRpc, (start * limit), limit);
        }
        this.projectManager.flushAndClear();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ProjectInvoice> getPurchaseOrderList(Integer projectId, ListingFilterParameter fp) {
        ListingObjectItem invoices = this.projectManager.getpurchaseOrderList(projectId, fp);
        ArrayList<ProjectInvoice> result = new ArrayList<>();
        for (EdsBaseInvoice baseInvoice : (List<EdsPurchaseOrder>) invoices.getItems()) {
            EdsPurchaseOrder purchaseOrder = this.quoteManager.getPurchaseOrderByID(baseInvoice.getObjectID());
            ProjectInvoice items = new ProjectInvoice();
            items.setID(baseInvoice.getObjectID());
            items.setInvoiceNumber(baseInvoice.getNumber());
            items.setDueDate(new DateNonConvertable(baseInvoice.getDueDate()));
            items.setInvoiceDate(new DateNonConvertable(baseInvoice.getInvoiceDate()));
            items.setClientName(baseInvoice.getClientOrSupplier().getName());
            items.setCurrencyName(baseInvoice.getCurrency() != null ? (baseInvoice.getCurrency().getName() != null ? baseInvoice.getCurrency().getName() : "") : "");
            items.setStatus(this.referenceWfmMessageSource.localizeRef(baseInvoice.getStatus()));
            items.setTotal(baseInvoice.getTotal().doubleValue());
            items.setCreatorName(baseInvoice.getCreator() != null ? baseInvoice.getCreator().getName() : "");
            items.setSubtotal(purchaseOrder.getSubtotal());
            items.setTotalTaxes(purchaseOrder.getTotalTaxes());
            if (purchaseOrder.getCreator() != null) {
                items.setCreatorName(purchaseOrder.getCreator().getName());
            }
            if (purchaseOrder.getApprover() != null) {
                items.setManagerName(purchaseOrder.getApprover().getName());
            }
            items.setTotalInInvoiceCurrency(baseInvoice.getTotalInInvoiceCurrency() != null ? baseInvoice.getTotalInInvoiceCurrency() :
                    baseInvoice.getTotal().multiply(baseInvoice.getExchangeRate()));
            result.add(items);
        }
        return new ListResult<>(result, invoices.getTotalCount());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ProjectInvoice> getPurchaseInvoiceList(Integer projectId, ListingFilterParameter fp) {
        ListingObjectItem invoices = this.projectManager.getPurchaseInvoiceList(projectId, fp);
        ArrayList<ProjectInvoice> result = new ArrayList<>();
        for (EdsBaseInvoice baseInvoice : (List<EdsPurchaseInvoice>) invoices.getItems()) {
            ProjectInvoice items = new ProjectInvoice();
            items.setID(baseInvoice.getObjectID());
            items.setInvoiceNumber(baseInvoice.getNumber());
            items.setDueDate(new DateNonConvertable(baseInvoice.getDueDate()));
            items.setInvoiceDate(new DateNonConvertable(baseInvoice.getInvoiceDate()));
            items.setClientName(baseInvoice.getClientOrSupplier().getName());
            items.setCurrencyName(baseInvoice.getCurrency() != null ? (baseInvoice.getCurrency().getName() != null ? baseInvoice.getCurrency().getName() : "") : "");
            items.setStatus(this.referenceWfmMessageSource.localizeRef(baseInvoice.getStatus()));
            items.setTotal(baseInvoice.getTotal().doubleValue());
            items.setTotalInInvoiceCurrency(baseInvoice.getTotalInInvoiceCurrency() != null ? baseInvoice.getTotalInInvoiceCurrency() :
                    baseInvoice.getTotal().multiply(baseInvoice.getExchangeRate()));

            EdsInvoice invoice = (EdsInvoice) baseInvoice;
            BigDecimal fullPayment = BigDecimal.ZERO;
            for (EdsInvoicePayment payment : invoice.getPayments()) {
                if (!(payment.getStatus() != null && EdsInvoicePayment.REVERSED.equals(payment.getStatus().getCode()))) {
                    fullPayment = fullPayment.add(payment.getAmount());
                }
            }
            items.setPayments(fullPayment);
            items.setCreatorName(baseInvoice.getCreator() != null ? baseInvoice.getCreator().getName() : "");
            items.setTotalTaxes(baseInvoice.getTotalTaxes());
            items.setExchageRate(baseInvoice.getExchangeRate());
            items.setTotalInInvoiceCurrency(items.getTotalInInvoiceCurrency().subtract(items.getPayments()));
            result.add(items);
        }
        return new ListResult<>(result, invoices.getTotalCount());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ProjectExpenseReportsListItem> getExpenseReportList(Integer projectId, ListingFilterParameter fp) {
        ListingObjectItem expenses = this.projectManager.getExpenseReportList(projectId, fp);
        ArrayList<ProjectExpenseReportsListItem> result = new ArrayList<>();
        for (EdsExpenseReport expense : (List<EdsExpenseReport>) expenses.getItems()) {
            ProjectExpenseReportsListItem items = new ProjectExpenseReportsListItem();
            items.setId(expense.getObjectID());
            items.setNumber(expense.getNumber());
            items.setTitle(expense.getTitle());
            items.setDescription(expense.getDescription());
            if (expense.getReporter() != null) {
                items.setReporterId(expense.getReporter().getObjectID());
                items.setReporterName(expense.getReporter().getName());
            }

            if (expense.getCurrentApprover() != null && expense.getCurrentApprover().getExactEmployee() != null) {
                items.setApproverSelectItem(new SelectItem(expense.getCurrentApprover().getExactEmployee().getObjectID(), expense.getCurrentApprover().getExactEmployee().getName()));
            }
            if (expense.getProject() != null) {
                items.setProjectId(expense.getProject().getObjectID());
                items.setProjectName(expense.getProject().getName());
            }
            if (expense.getStatus() != null) {
                items.setStatusId(expense.getStatus().getObjectID());
                items.setStatusCode(expense.getStatus().getCode());
                items.setStatusName(this.referenceWfmMessageSource.localize(expense.getStatus().getCode(), expense.getStatus().getName()));
            }
            if (expense.getBaseTotal() != null) {
                items.setTotal(expense.getBaseTotal().doubleValue());
            }
            items.setStartDate(expense.getStartDate());
            result.add(items);
        }
        return new ListResult<>(result, expenses.getTotalCount());
    }

    public Boolean isDateExistsInNumbering() {
        boolean dateContainsInNumber = false;

        EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
        if (settings != null && settings.getProjectLastIntNumber() != null && settings.getProjectNumberingFormat() != null && !"".equals(settings.getProjectNumberingFormat()) && settings.getProjectNumberingFormat().contains(Constants.WIDGET_PREFIX)) {
            String[] numberingFormats = settings.getProjectNumberingFormat().split("/");
            for (String value : numberingFormats) {
                String[] split = value.split(":");
                switch (split[0]) {
                    case Constants.WIDGET_DATE_YEAR -> {
                        if (!"".equals(split[1]) && "true".equals(split[1])) {
                            dateContainsInNumber = true;
                        } else {
                            continue;
                        }
                    }
                    case Constants.WIDGET_DATE_MONTH -> {
                        if (!"".equals(split[1]) && "true".equals(split[1])) {
                            dateContainsInNumber = true;
                        } else {
                            continue;
                        }
                    }
                    case Constants.WIDGET_DATE_DAY -> {
                        if (!"".equals(split[1]) && "true".equals(split[1])) {
                            dateContainsInNumber = true;
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
        return dateContainsInNumber;
    }

    public Boolean isClientExistsInNumbering() {
        boolean clientContainsInNumber = false;

        EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
        if (settings != null && settings.getProjectLastIntNumber() != null && settings.getProjectNumberingFormat() != null && !"".equals(settings.getProjectNumberingFormat()) && settings.getProjectNumberingFormat().contains(Constants.WIDGET_PREFIX)) {
            String[] numberingFormats = settings.getProjectNumberingFormat().split("/");
            for (String value : numberingFormats) {
                String[] split = value.split(":");
                if (Constants.WIDGET_CLIENT_CODE.equals(split[0])) {
                    if (!"".equals(split[1]) && "true".equals(split[1])) {
                        clientContainsInNumber = true;
                    } else {
                        continue;
                    }
                }
            }
        }
        return clientContainsInNumber;
    }

    @Override
    public NumberData generateProjectNumber(Date date, Integer clientId, Integer objectID) {
        if ("22240".equals(ServerSecurityContext.getInstance().getCompanyId())) {//sorry, majburman shunaqa musir code yoziwga!
            return this.crmServiceLocal.generateOpportunityNumber();
        }
        EdsUser user = this.userManager.getUser();
        if (user == null) {
            user = this.userManager.get(ServerSecurityContext.getInstance().getStaticUserID());
        }
        if (date != null && user != null) {
            date = user.getUserDate(date);
        }
        EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
        Integer intNumber = this.projectManager.getProjectLastIntNumber();
        if (settings != null && settings.getProjectLastIntNumber() != null && settings.getProjectNumberingFormat() != null && !"".equals(settings.getProjectNumberingFormat()) && settings.getProjectNumberingFormat().contains(Constants.WIDGET_PREFIX)) {
            intNumber = settings.getProjectLastIntNumber();
        }
        String clientCode = null;
        if (clientId != null) {
            List<String> clientcodeList = this.crmAccountManager.getCrmAccountNumberById(clientId);
            if (clientcodeList != null && clientcodeList.size() > 0) {
                clientCode = this.crmAccountManager.getCrmAccountNumberById(clientId).get(0);
            }
        }
        if (settings != null && settings.getProjectNumberingFormat() != null) {
            if (objectID != null) {
                String savedNumberFormat = this.projectManager.getSavedNumberformat(objectID);
                return settings.parsNumberDataForEdit(intNumber, savedNumberFormat, settings.getProjectNumberingFormat());
            }
            return settings.parseNumberDataForALL(intNumber, settings.getProjectNumberingFormat(), settings.getDelimetrProject(), date, clientCode, null, "project");
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_PROJ_PREFIX /*true*/);
        }
    }

    @Override
    public void mergeProjectAccounts(Integer objectID, ArrayList<Integer> otherObjectIDs) {
        EdsCrmAccount edsCrmAccount = this.crmAccountManager.get(objectID);
        EdsCompany edsCompany = this.userManager.getUser().getCompany();
        if (otherObjectIDs != null) {
            for (Integer clientId : otherObjectIDs) {
                List<EdsProject> edsProjects = this.projectManager.getProjectClients(clientId);
                for (EdsProject project : edsProjects) {
                    project.setClient(edsCrmAccount);
                    this.projectManager.update(project);
                    try {
                        projectSolrComponent.index(project);
                    } catch (SolrServerException | IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * <h1>... This is method Fill Project List Data ...</h1>
     * <br/>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated {18:05 06/06/2011} ...</h3>
     *
     * @param fp
     * @return
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ListResult<ProjectListItem> getProjectList(ListingFilterParameter fp) {
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsProject.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Get project list");
        FacetFilterRpc projectFacetFilter = fp.getFacetFilter();
        if (projectFacetFilter != null && !projectFacetFilter.isFilterChanges()) {
            projectFacetFilter = this.commonServiceLocal.getUserFacetFilter(projectFacetFilter);
        }

        EdsUser edsUser = this.projectManager.getUser();
        if (edsUser == null && ServerSecurityContext.getInstance().getStaticUserID() != null) {
            edsUser = this.userManager.get(ServerSecurityContext.getInstance().getStaticUserID());
        }
        if (fp.getEmployeeId() != null && fp.isLookUp()) {
            EdsUser user = this.userManager.get(fp.getEmployeeId());
            edsUser = user != null ? user : edsUser;
        }
        EdsCompany edsCompany = edsUser.getCompany();
        Set<Integer> roles = edsUser.getRoleIds();

        if (edsUser.hasRole(Constants.SUPPLIER) && !roles.contains(EdsRole.CLIENT)) {
            QueryBuilderForSolr.supplierRelationForProjectList(projectFacetFilter, edsUser);
        }


        StringBuilder solrQuery = new StringBuilder();
        List<Integer> crmAccountIDs = null;
        if (fp.getClientId() != null) {
            crmAccountIDs = this.relationManager.getRelationIDsByType(fp.getClientId(), null, RelationItem.TYPE_CRM_ACCOUNT, RelationItem.TYPE_PROJECT);
        }
        solrQuery.append(QueryBuilderForSolr.getProjectSolrQuery(fp, edsUser, edsCompany, roles, crmAccountIDs));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(projectFacetFilter, edsCompany, SolrTaskRepresenter.FIELD_START_DATE, SolrTaskRepresenter.FIELD_DUE_DATE));

        return this.getProjectListResponse(fp, solrQuery.toString());
    }

    /**
     * <h1>... This is method cretae project solr response ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Creatde date {21:59 08/06/2011} ...</h3>
     *
     * @param fp
     * @param solrQuery
     * @return
     */
    private ListResult<ProjectListItem> getProjectListResponse(ListingFilterParameter fp, String solrQuery) {
        boolean enableMultiClientToProject = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT);
//        SolrClient server = WfmJpaTemplate.getSolrServerForCore(Constants.SOLR_PROJECT_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(this.getProjectSolrQuery(fp, solrQuery, false, enableMultiClientToProject), SolrRequest.METHOD.POST);
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
//        return this.getProjectListInSolr(resp, fp, enableMultiClientToProject);

        Page<ProjectSolrDoc> projectSolrDocs = projectSolrComponent.getList(fp, solrQuery, enableMultiClientToProject);
        return getProjectListInSolr(projectSolrDocs, fp, enableMultiClientToProject);
    }

    /**
     * <h1>... This is method generate project solr query ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Cretade date {22:09 08/06/2011} ...</h3>
     *
     * @param fp
     * @param solrQuery
     * @return
     */
    private SolrQuery getProjectSolrQuery(ListingFilterParameter fp, String solrQuery, boolean isAllItems, boolean enableMultiClientToProject) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);

        if (!isAllItems) {
            query.setStart(fp.getStart());
            query.setParam(CommonParams.ROWS, String.valueOf(fp.getLimit()));
        }

        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                SolrQuery.ORDER order = fp.isAscending() ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc;
                if (ProjectListItem.NUMBER.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.SORTABLE_PROJECT_NUMBER, order);
                } else if (ProjectListItem.NAME.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.SORTABLE_PROJECT_NAME, order);
                } else if (ProjectListItem.DESCRIPTION.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.SORTABLE_PROJECT_DESCRIPTION, order);
                } else if (ProjectListItem.MANAGER.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.SORTABLE_PROJECT_MANAGER, order);
                } else if (ProjectListItem.BACKUP_MANAGER.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.SORTABLE_PROJECT_BACKUP_MANAGER, order);
                } else if (ProjectListItem.CLIENT.equals(fp.getSortField())) {
                    if (enableMultiClientToProject) {
                        query.setSort(SolrProjectListRepresenter.SORTABLE_CLIENT_NAME_SORT, order);
                    } else {
                        query.setSort(SolrProjectListRepresenter.SORTABLE_PROJECT_CLIENT, order);
                    }
                } else if (ProjectListItem.STATUS.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_SORDER, order);
                } else if (ProjectListItem.PERCENT_COMPLETED.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.FIELD_PROJECT_COMPLETED, order);
                } else if (ProjectListItem.START_DATE.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.FIELD_START_DATE, order);
                } else if (ProjectListItem.END_DATE.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.FIELD_DUE_DATE, order);
                } else if (ProjectListItem.INVOICES.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.SORTABLE_PROJECT_INVOICE, order);
                } else if (ProjectListItem.ACTUAL_TIME_SPENT.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.SORTABLE_PROJECT_HOUR_SPENT, order);
                } else if (ProjectListItem.CREATED_DATE.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.FIELD_PROJECT_CREATED_DATE, order);
                } else if (ProjectListItem.MODIFIED_BY.equals(fp.getSortField())) {
                    query.setSort(SolrProjectListRepresenter.FIELD_PROJECT_MODIFIED_BY, order);
                }
                CustomFieldsUtils.setCustomFieldsSortableNameToSolr(fp.getSortField(), !fp.isAscending(), query, true);
            } else {
                query.setSort(SolrProjectListRepresenter.FIELD_LAST_UPDATE_DATE, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    public void saveProjectEditCellValue(ProjectListItem rowValue, String columnCodeName, boolean changeTaskStatus) {
        EdsUser user = this.projectManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        try {
            EdsProject project = this.projectManager.get(rowValue.getObjectId());
            project.clear();
            if (ProjectListItem.START_DATE.equals(columnCodeName)) {
                project.setStartDate(rowValue.getStartDate());
            } else if (ProjectListItem.END_DATE.equals(columnCodeName)) {
                project.setEndDate(rowValue.getEndDate());
                project.setDueDate(rowValue.getEndDate());
            } else if (ProjectListItem.STATUS.equals(columnCodeName)) {
                EdsReference status = this.referenceManager.get(rowValue.getStatusId());
                project.setStatus(status);
                if (status.getCode().equals(EdsProject.COMPLETED)) {
                    project.setCompletedDate(new Date());
                } else {
                    project.setCompletedDate(null);
                }
                if (changeTaskStatus) {
                    this.baseEventPostProcessor.registerEvent(ProjectStatusEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
                }
            } else {
                EdsProjectCustomFields edsProjectCustomFields = project.getProjectCustomFields();
                if (edsProjectCustomFields == null) {
                    edsProjectCustomFields = new EdsProjectCustomFields();
                    this.projectCFManager.create(edsProjectCustomFields);
                    project.setProjectCustomFields(edsProjectCustomFields);
                }
                Object ob = CustomFieldsUtils.getObjectValue(edsProjectCustomFields, columnCodeName);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(rowValue.getCustomFields().get(columnCodeName))) {
                            project.addChange(columnCodeName);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(rowValue.getCustomFields().get(columnCodeName))) {
                            project.addChange(columnCodeName);
                        }
                    } else if (ob instanceof Date date) {
                        if (!date.equals(rowValue.getCustomFields().get(columnCodeName))) {
                            project.addChange(columnCodeName);
                        }
                    }
                } else {
                    project.addChange(columnCodeName);
                }
                CustomFieldsUtils.setDomenObjectFieldChange(edsProjectCustomFields, rowValue.getCustomFields(), columnCodeName);
            }
            project.setLastUpdateTime(new Date());
            project.setUpdater(this.userManager.getUser());
            EdsBusinessEvent edsBusinessEvent = this.baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, this.userManager.getUser());
            edsBusinessEvent.setSolrIndexed(true);
            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
            workflowEvent.setEntityType(RelationItem.TYPE_PROJECT);
            try {
//                this.solrManager.indexAddProject(project, edsCompany.getObjectID());
                projectSolrComponent.index(project);
            } catch (Exception e) {
                edsBusinessEvent.setSolrIndexed(false);
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Task List Edit Cell Column Code :" + columnCodeName);
        }
    }

    private Object[] createProject(String name, String locationName, String clientName, EdsUser user) {
        ArrayList<EdsProject> projectList = null;
        EdsProject undefinedProject = null;
        ProjectSingleItem newProject = new ProjectSingleItem();
        newProject.setName(name);
        newProject.setDescription(name);
        Integer clientID = null;
        if (clientName != null) {
            EdsCrmAccount crmAccount = this.crmAccountManager.getCrmAccountByName(clientName, null);
            if (crmAccount != null) {
                clientID = crmAccount.getObjectID();
                newProject.setClientId(clientID);
            }
        }
        newProject.setStartDate(new Date());
        Date endDate = new Date();
        endDate.setMonth(endDate.getMonth() + 1);
        newProject.setEndDate(endDate);
        ProjectMember[] members = new ProjectMember[1];
        EdsEmployee employee = user.getEmployee();
        members[0] = new ProjectMember(employee.getObjectID(), employee.getFullName(), null);
        newProject.setProjectMembers(members);
        newProject.setManagerId(user.getObjectID());
        EdsLocation edsLocation;
        if (locationName != null) {
            edsLocation = this.locationManager.getLocationByName(locationName);
            if (edsLocation != null) {
                newProject.setLocationId(edsLocation.getObjectID());
            }
        }
        newProject.setNumberData(this.generateProjectNumber(new Date(), clientID, null));
        newProject.setStatusId(this.referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED).getObjectID());
        try {
            Integer projectID = this.saveProject(newProject);
            undefinedProject = this.projectManager.get(projectID);
            System.out.println("Create project with name: " + undefinedProject.getName() + "; ObjectID: " + projectID);
            projectList = new ArrayList<>();
            projectList.add(undefinedProject);
        } catch (NumberExistingException e) {
            System.out.println("Project already exists with name: " + newProject.getNumberData().getNumberString());
            e.printStackTrace();
        }
        Object[] result = new Object[2];
        result[0] = projectList;
        result[1] = undefinedProject;
        return result;
    }

    public void updateProjectEmployeeWageClientHistory(ProjectEmployeeWageClientHistoryItem[] hist, Integer projectEmployeeId, Integer projectId) {

        int index = 0;
        Integer employeeId = this.projectEmployeeManager.get(projectEmployeeId).getEmployeeDepartment().getEmployee().getObjectID();
        this.projectManager.updateTimesheetWageRates(employeeId, projectId); /*set 0 to all */
        for (ProjectEmployeeWageClientHistoryItem itm : hist) {

            index++;
            EdsProjectEmployeeWageClientRateHistory edsHis = new EdsProjectEmployeeWageClientRateHistory();

            edsHis.setObjectID(itm.getObjectId());
            edsHis.setWageRate(itm.getWageRate());
            edsHis.setChangeDate(itm.getChangeDate() != null ? itm.getChangeDate().getNonConvertedDate() : null);
            edsHis.setClientChargeRate(itm.getClientChargeRate());
            edsHis.setWorkloadPercentage(itm.getWorkloadPercentage());

            this.projectManager.updateEmployeeWageClientRateHistory(edsHis);

            this.projectManager.updateTimesheetWageRates(edsHis.getWageRate(), edsHis.getClientChargeRate(), employeeId, projectId,
                    edsHis.getChangeDate(), index == hist.length ? null : new Date());
            if (index == hist.length) {
                // UPDATE CURRENT ON EMPLOYEEMANAGER
                this.projectManager.updateProjectEmployeeOb(itm.getWageRate(), itm.getClientChargeRate(), employeeId, projectId);
            }
        }

        EdsEmployee employee = this.employeeManager.get(employeeId);
        EdsProject project = this.projectManager.get(projectId);
        EdsProjectEmployee projectEmployee = this.projectEmployeeManager.getProjectEmployee(employee, project);
        List<Integer> peIds = new ArrayList<>();
        peIds.add(projectEmployee.getObjectID());
        this.employeeTaskManager.updateTaskForReCalculationPE(peIds);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectEmployeeWageClientHistoryItem[] getProjectEmployeeWageClientHistory(Integer projectEmployeeId) {
        List<EdsProjectEmployeeWageClientRateHistory> hist = this.projectEmployeeManager.getProjectEmployeeWageClientRateHistory(projectEmployeeId);

        ProjectEmployeeWageClientHistoryItem[] items = null;
        if (hist != null && hist.size() > 0) {
            items = new ProjectEmployeeWageClientHistoryItem[hist.size()];

            for (int j = 0; j < hist.size(); j++) {

                ProjectEmployeeWageClientHistoryItem item = new ProjectEmployeeWageClientHistoryItem();
                item.setObjectId(hist.get(j).getObjectID());
                item.setChangeDate(new DateNonConvertable(hist.get(j).getChangeDate()));
                item.setClientChargeRate(hist.get(j).getClientChargeRate());
                item.setWageRate(hist.get(j).getWageRate());
                item.setWorkloadPercentage(hist.get(j).getWorkloadPercentage());
                item.setCurrent(j == hist.size() - 1);
                items[j] = item;
            }
        }
        return items;
    }

    public void deleteProjectEmployeeWageClientRateHistory(Integer historyId) {
        //get the employee wage rate history item
        //get the next wage rate and apply it to the old wage rate applied timesheets
        EdsProjectEmployeeWageClientRateHistory deletedWageRate = this.projectManager.getProjectEmployeeWageClientRateHistory(historyId);
        EdsProjectEmployeeWageClientRateHistory nextWageRate = this.projectManager.getNextProjectEmployeeWageClientRateHistory(historyId);
        this.projectManager.updateTimesheetWageRates(nextWageRate, deletedWageRate);
        nextWageRate.setChangeDate(deletedWageRate.getChangeDate());
        this.projectManager.deleteEmployeeWageClientRateHistory(historyId);
    }

    @Override
    public void calculateProjectBudgets(Integer objectID, Boolean isClearAndReCalculate) {
        //objectID can not be null
        if (objectID == null) {
            return;
        }

        Date startPCCTime = new Date();
        System.out.println("=== Start Project Cost Calculation ===");

        EdsUser user = this.projectManager.getUser();
        EdsCompany company = user.getCompany();
        EdsProject project = this.projectManager.get(objectID);

        //recalculation param of the Project Actual Time Spent
        Integer projectActualTimeSpent = 0;
        Double projectActualWageAmount = 0.0;
        Double projectActualClientChargeAmount = 0.0;
        Double projectPlanedWageAmount = 0.0;
        Double projectPlanedClientChargeAmount = 0.0;

        if (isClearAndReCalculate) {
            //clear all calculated items of the Project
            this.projectManager.clearProjectBudgetCalculatedItems(objectID);
        }

        //Calculation for Project Work Stream Items
        List<EdsWorkStream> projectParentWorkstreams = this.workStreamManager.findOrphanWorkstreams(objectID);
        for (EdsWorkStream workstream : projectParentWorkstreams) {
            this.calculateProjectWSBudgets(workstream);

            projectActualTimeSpent += workstream.getActualTime();
            projectActualWageAmount += workstream.getActualWageAmount();
            projectActualClientChargeAmount += workstream.getActualClientChargeAmount();
            projectPlanedWageAmount += workstream.getPlannedWageAmount();
            projectPlanedClientChargeAmount += workstream.getPlannedClientChargeAmount();
        }

        //Calculation for Project Task without Work Stream
        int listLimit = 100; //list counter
        int listIndex = 1;
        List<EdsTask> projectTaskList;
        do {
            int listStart = (listIndex - 1) * listLimit;
            projectTaskList = this.taskManager.getProjectTasksByIntervalWithoutWS(objectID, listStart, listLimit);

            if (projectTaskList != null && projectTaskList.size() > 0) {
                for (EdsTask task : projectTaskList) {

                    if (task.isChangedCalculationFields()) {

                        //calculate task budgets by changed calculation fields
                        this.taskServiceLocal.calculateTaskBudgets(task);

                        this.employeeTaskManager.deleteEmployeeTaskHistory(task.getObjectID());

                        //this task is calculated
                        task.setCalculated(true);

                        //update task by calculation changes
                        this.taskManager.update(task);

                        try {
                            taskSolrComponent.index(task);
                        } catch (Exception e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                        this.taskManager.flush();
                    }
                    projectActualTimeSpent += task.getTimespent();
                    projectActualWageAmount += task.getActualWageAmount();
                    projectActualClientChargeAmount += task.getActualClientChargeAmount();
                    projectPlanedWageAmount += task.getPlannedWageAmount();
                    projectPlanedClientChargeAmount += task.getPlannedClientChargeAmount();
                }
            }

            listIndex++;
        } while (projectTaskList != null && projectTaskList.size() > 0);

        project.setTimespent(projectActualTimeSpent);

        project.setPlanedWageAmount(projectPlanedWageAmount);
        project.setPlanedClientChargeAmount(projectPlanedClientChargeAmount);
        project.setPlanedExpensesAmount(this.projectBudgetManager.getProjectPlanedExpense(project.getObjectID()).doubleValue());
        project.setPlanedIncomeAmount(this.projectBudgetManager.getProjectPlanedIncome(project.getObjectID()).doubleValue());

        project.setActualWageAmount(projectActualWageAmount);
        project.setActualClientChargeAmount(projectActualClientChargeAmount);
        project.setExpensesAmount(this.projectBudgetManager.getProjectExpense(project.getObjectID()).doubleValue());
        project.setIncomeAmount(this.projectBudgetManager.getProjectIncome(project.getObjectID()).doubleValue());

        if (EdsProject.COMPLETED.equals(project.getStatus().getCode())) {
            project.setCalculationCompleted(true);
        }

        try {
            projectSolrComponent.index(project);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Date endPCCTime = new Date();
        System.out.println("=== End Project Cost Calculation ===");
        System.out.println("=== Time Spend for PCC : " + (endPCCTime.getTime() - startPCCTime.getTime()) + " sec");
    }

    private void calculateProjectWSBudgets(EdsWorkStream workstream) {
        if (workstream == null) {
            return;
        }

        if (workstream.getSubWorkStreams() != null && workstream.getSubWorkStreams().size() > 0) {
            this.taskServiceLocal.calculateWorkStreamBudgets(workstream.getObjectID());

            Date startDate = this.workStreamManager.getWSStartDateByTask(workstream.getObjectID());
            Date endDate = this.workStreamManager.getWSEndDAteByTask(workstream.getObjectID());

            for (EdsWorkStream sw : workstream.getSubWorkStreams()) {
                this.calculateProjectWSBudgets(sw);

                Date swStartDate = this.workStreamManager.getWSStartDateByTask(sw.getObjectID());
                if (startDate != null && swStartDate != null) {
                    startDate = (startDate.compareTo(swStartDate) >= 0) ? swStartDate : startDate;
                }

                Date swEndDate = this.workStreamManager.getWSEndDAteByTask(sw.getObjectID());
                if (endDate != null && swEndDate != null) {
                    endDate = (endDate.compareTo(swEndDate) <= 0) ? swEndDate : endDate;
                }
            }

            if (startDate != null) {
                workstream.updateStartDate(startDate);
            }

            if (endDate != null) {
                workstream.updateEndDate(endDate);
            }

            this.workStreamManager.update(workstream);
        } else {
            this.taskServiceLocal.calculateWorkStreamBudgets(workstream.getObjectID());

            Date startDate = this.workStreamManager.getWSStartDateByTask(workstream.getObjectID());
            Date endDate = this.workStreamManager.getWSEndDAteByTask(workstream.getObjectID());

            if (startDate != null) {
                workstream.updateStartDate(startDate);
            }

            if (endDate != null) {
                workstream.updateEndDate(endDate);
            }

            this.workStreamManager.update(workstream);
        }
    }

    /**
     * <h1>... This is method get user projects only parent is null and fill rpc object ...</h1>
     * <br/>
     * <h2>... Write developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {16:43 24/05/2011} ...</h3>
     *
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getParentIsNullProjects(Integer projectId) {
        List<EdsProject> projectList = this.projectManager.getProjectsParentIsNull(projectId);
        int k = 0;
        SelectItem[] items = new SelectItem[projectList.size()];
        for (EdsProject project : projectList) {
            items[k++] = new SelectItem(project.getObjectID(), project.getName());
        }
        return items;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectBudget getProjectBudgetItems(Integer projectID, boolean withTax) {
        ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
        listingFilterParameter.setProjectId(projectID);
        listingFilterParameter.setViewType(Constants.PM_CODE);
        listingFilterParameter.setExcludedType(Constants.DRAFT + "," + Constants.MANAGER_REJECT + "," + Constants.REJECT);
        listingFilterParameter.setFromBudgetSheet(true);
        ProjectBudget projectBudget = new ProjectBudget();
        EdsProject project = this.projectManager.get(projectID);

        ArrayList<Integer> projectIDs = (ArrayList<Integer>) this.projectManager.getSubProjectIDs(projectID);
        projectIDs.add(projectID);
        listingFilterParameter.setProjectIdList(projectIDs);

        if (project != null && project.getClient() != null && project.getClient().getObjectID() != null) {
            projectBudget.setClientID(project.getClient().getObjectID());
        }
        boolean projectInLineItemEnable = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        BigDecimal totalPlanned = new BigDecimal(0);
        BigDecimal totalActual = new BigDecimal(0);

        EdsFinancialSettings fs = this.financialSettingsManager.getFinancialSettings();
        Integer calcScale = fs != null ? fs.getAccountingCalculationScale() : 2;

        boolean isAgencyFees = false;

        ////START REVENUE////
        //Sales Quote
        InvoiceList salesQuotesList = this.invoiceCircularResolver.getSaleQuoteData(listingFilterParameter, null);
        ProjectBudgetItem[] salesQuotes = new ProjectBudgetItem[salesQuotesList.getList().size()];
        int i = 0;
        for (NewInvoice saleQuote : salesQuotesList.getList()) {
            salesQuotes[i] = new ProjectBudgetItem();
            WageTaxItem wageTaxItem = this.getBaseTotal(saleQuote, projectIDs, isAgencyFees, projectInLineItemEnable);
            wageTaxItem.setWithTax(withTax);
            salesQuotes[i].setPlannedWageAmount(wageTaxItem.getTotal());
            salesQuotes[i].setName(saleQuote.getInvoiceNumber());
            salesQuotes[i].setAction("salequote|summary/" + saleQuote.getID());
            if (saleQuote.getClientID() != null && saleQuote.getClientName() != null) {
                salesQuotes[i].setVendor(new SelectItem(saleQuote.getClientID(), saleQuote.getClientName()));
            }
            totalPlanned = totalPlanned.add(salesQuotes[i].getPlannedWageAmount(calcScale));
            i++;
        }
        projectBudget.setSalesQuotes(salesQuotes);

        //Sales Order
        InvoiceList salesOrderList = this.invoiceCircularResolver.getSaleOrderData(listingFilterParameter, null);
        ProjectBudgetItem[] salesOrders = new ProjectBudgetItem[salesOrderList.getList().size()];
        i = 0;
        for (NewInvoice saleOrder : salesOrderList.getList()) {
            salesOrders[i] = new ProjectBudgetItem();
            WageTaxItem wageTaxItem = this.getBaseTotal(saleOrder, projectIDs, isAgencyFees, projectInLineItemEnable);
            wageTaxItem.setWithTax(withTax);
            salesOrders[i].setPlannedWageAmount(wageTaxItem.getTotal());
            salesOrders[i].setName(saleOrder.getInvoiceNumber());
            salesOrders[i].setAction("saleorder|summary/" + saleOrder.getID());
            if (saleOrder.getClientID() != null && saleOrder.getClientName() != null) {
                salesOrders[i].setVendor(new SelectItem(saleOrder.getClientID(), saleOrder.getClientName()));
            }
            totalPlanned = totalPlanned.add(salesOrders[i].getPlannedWageAmount(calcScale));
            i++;
        }
        projectBudget.setSalesOrders(salesOrders);

        projectBudget.getSubTotalRevenue().setPlannedWageAmount(totalPlanned);

        //Sales Invoices
        List<EdsBaseSaleInvoice> invoiceList = this.invoiceManager.getSaleInvoiceList(listingFilterParameter);
        ProjectBudgetItem[] salesInvoices = new ProjectBudgetItem[invoiceList.size()];
        i = 0;
        for (EdsBaseSaleInvoice saleInvoice : invoiceList) {
            NewInvoice newInvoice = EdsInvoice.getInvoiceData(saleInvoice);
            salesInvoices[i] = new ProjectBudgetItem();
            WageTaxItem wageTaxItem = this.getBaseTotal(newInvoice, projectIDs, isAgencyFees, projectInLineItemEnable);
            wageTaxItem.setWithTax(withTax);
            salesInvoices[i].setName(saleInvoice.getNumber());
            if (saleInvoice.isCreditNote()) {
                salesInvoices[i].setAction("receivablecreditnote|summary/" + saleInvoice.getObjectID());
                salesInvoices[i].setActualWageAmount(BigDecimal.ZERO.subtract(wageTaxItem.getTotal()));
            } else {
                salesInvoices[i].setAction("saleinvoice|summary/" + saleInvoice.getObjectID());
                salesInvoices[i].setActualWageAmount(wageTaxItem.getTotal());
            }
            if (saleInvoice.getClient() != null && saleInvoice.getClient().getName() != null) {
                salesInvoices[i].setVendor(new SelectItem(saleInvoice.getClient().getObjectID(), saleInvoice.getClient().getName()));
            }
            totalActual = totalActual.add(salesInvoices[i].getActualWageAmount(calcScale));
            i++;
        }
        projectBudget.setSalesInvoices(salesInvoices);

        // Bank Receipt
        listingFilterParameter.setType(0);
        List<EdsBankTransfer> bankReceiptList = this.spendReceiveMoneyManager.getBankTransferList(listingFilterParameter, Constants.REVENUE);
        ProjectBudgetItem[] bankReceipts = new ProjectBudgetItem[bankReceiptList.size()];
        i = 0;
        for (EdsBankTransfer bankTransfer : bankReceiptList) {
            bankReceipts[i] = new ProjectBudgetItem();
            WageTaxItem wageTaxItem = this.getBankTransferBaseTotal(bankTransfer, projectIDs, projectInLineItemEnable, false);
            wageTaxItem.setWithTax(withTax);
            bankReceipts[i].setName(bankTransfer.getNumber() + " -> " + bankTransfer.getName());
            bankReceipts[i].setActualWageAmount(wageTaxItem.getTotal());
            bankReceipts[i].setAction("spendreceivemoney|summary/" + bankTransfer.getObjectID() + "/" + "RECEIVE_MONEY");
            totalActual = totalActual.add(bankReceipts[i].getActualWageAmount(calcScale));
            i++;
        }
        projectBudget.setBankReceipts(bankReceipts);

        //Cash Receipt
        listingFilterParameter.setType(2);
        List<EdsBankTransfer> cashReceiptList = this.spendReceiveMoneyManager.getBankTransferList(listingFilterParameter, Constants.REVENUE);
        ProjectBudgetItem[] cashReceipts = new ProjectBudgetItem[cashReceiptList.size()];
        i = 0;
        for (EdsBankTransfer bankTransfer : cashReceiptList) {
            cashReceipts[i] = new ProjectBudgetItem();
            WageTaxItem wageTaxItem = this.getBankTransferBaseTotal(bankTransfer, projectIDs, projectInLineItemEnable, false);
            wageTaxItem.setWithTax(withTax);
            cashReceipts[i].setName(bankTransfer.getNumber() + " -> " + bankTransfer.getName());
            cashReceipts[i].setActualWageAmount(wageTaxItem.getTotal());
            cashReceipts[i].setAction("spendreceivemoney|summary/" + bankTransfer.getObjectID() + "/" + "CASH_RECEIPT");
            totalActual = totalActual.add(cashReceipts[i].getActualWageAmount());
            i++;
        }
        projectBudget.setCashReceipts(cashReceipts);

        //Manual Entry Revenue
        List<EdsManualJournalItem> manualJournalItemList = this.manualJournalManager.getManualJournalItemList(listingFilterParameter);
        manualJournalItemList.removeIf(Objects::isNull);
        ProjectBudgetItem[] manualJournals;
        if (manualJournalItemList != null && manualJournalItemList.size() > 0) {
            HashMap<String, SelectItem> itemValue = new HashMap<>();
            for (EdsManualJournalItem manualJournal : manualJournalItemList) {
                if ((Constants.REVENUE.equals(manualJournal.getAccount().getAccountType().getCategory()) && manualJournal.getCreditInBase() != null && manualJournal.getCreditInBase().compareTo(BigDecimal.ZERO) > 0)
                        || (Constants.EXPENSES.equals(manualJournal.getAccount().getAccountType().getCategory()) && manualJournal.getCreditInBase() != null && manualJournal.getCreditInBase().compareTo(BigDecimal.ZERO) > 0)) {
                    if (itemValue.containsKey(manualJournal.getManualTransfer().getNumber())) {
                        Double amount = itemValue.get(manualJournal.getManualTransfer().getNumber()).getTotalAmount() != null ? itemValue.get(manualJournal.getManualTransfer().getNumber()).getTotalAmount() : 0;
                        amount += manualJournal.getCreditInBase().doubleValue();
                        itemValue.get(manualJournal.getManualTransfer().getNumber()).setTotalAmount(amount);
                    } else {
                        SelectItem item = new SelectItem();
                        item.setTotalAmount(manualJournal.getCreditInBase().doubleValue());
                        item.setId(manualJournal.getManualTransfer().getObjectID());
                        item.setNumber(manualJournal.getManualTransfer().getNumber());
                        itemValue.put(manualJournal.getManualTransfer().getNumber(), item);
                    }
                }
            }

            manualJournals = new ProjectBudgetItem[itemValue.size()];
            i = 0;
            for (SelectItem manualJournalItem : itemValue.values()) {

                manualJournals[i] = new ProjectBudgetItem();
                manualJournals[i].setName(manualJournalItem.getNumber());
                manualJournals[i].setActualWageAmount(BigDecimal.valueOf(manualJournalItem.getTotalAmount()));
                manualJournals[i].setAction("manual|summary/" + manualJournalItem.getId());
                totalActual = totalActual.add(manualJournals[i].getActualWageAmount());
                i++;
            }
        } else {
            manualJournals = new ProjectBudgetItem[0];
        }
        projectBudget.setManualEntryRevenue(manualJournals);

        projectBudget.getSubTotalRevenue().setActualWageAmount(totalActual);
        projectBudget.getSubTotalRevenue().setVarianceAmount(projectBudget.getSubTotalRevenue().getActualWageAmount(calcScale).subtract(projectBudget.getSubTotalRevenue().getPlannedWageAmount(calcScale)));
        if (projectBudget.getSubTotalRevenue().getPlannedWageAmount(calcScale).doubleValue() != 0d) {
            projectBudget.getSubTotalRevenue().setVariancePerCent(projectBudget.getSubTotalRevenue().getVarianceAmount().divide(projectBudget.getSubTotalRevenue().getPlannedWageAmount(calcScale), 4, RoundingMode.HALF_UP));
        }

        ////END REVENUE////

        //Expenses
        {//Employee costs
            List<EdsProjectEmployee> employeeProjects = this.projectManager.getEmployeesByProjectAll(projectID);
            totalPlanned = new BigDecimal(0);
            totalActual = new BigDecimal(0);
            Map<String, ProjectBudgetItem> budgetItems = new HashMap<>();
            Map<String, Boolean> employeeProjectDeletedCheck = new HashMap<>();
            Map<String, Double[]> employeeCostAndTimeSpentOnProjectMap = this.timeSheetManager.getEmployeeCostAndTimeSpentOnProjects(projectID);
            EdsReference inactiveSts = this.referenceManager.findReference(Constants.EMPLOYEE_STATUS, Constants.EMPLOYEE_STATUS_RESIGNED);
            for (EdsProjectEmployee employeePro : employeeProjects) {
                Integer estimatedTime = this.timeSheetManager.getEstimatedTime(employeePro.getObjectID(), projectID);
                Double plannedAmount = 0d;
                if (estimatedTime != null && employeePro.getWageRate() != null) {
                    Double estimated = estimatedTime / 60.00;
                    plannedAmount = (estimated * employeePro.getWageRate());
                }
                ProjectBudgetItem employeeCost;
                String uniqueKey = employeePro.getEmployeeDepartment().getEmployee().getObjectID() + "/" + employeePro.getProject().getObjectID();
                Double[] employeeCostAndTimeSpentOnProject = employeeCostAndTimeSpentOnProjectMap.get(uniqueKey);
                if (budgetItems.containsKey(uniqueKey)) {
                    employeeCost = budgetItems.get(uniqueKey);
                } else {
                    employeeCost = new ProjectBudgetItem();
                }
                if (employeePro.getEmployeeDepartment() == null) {
                    continue;
                }
                String employeeName = employeePro.getEmployeeDepartment().getEmployee().getFullName();
                if (employeePro.getEmployeeDepartment().getEmployee().getAccountStatus().getObjectID().equals(inactiveSts.getObjectID())) {
                    employeeName = employeeName + " (" + this.commonLocalizer.localize("resigned", "Resigned") + ")";
                }
                employeeCost.setName(employeeName);
                if (employeeCostAndTimeSpentOnProject != null && !employeeProjectDeletedCheck.containsKey(uniqueKey)) {
                    employeeCost.setActualWageAmount(BigDecimal.valueOf(employeeCostAndTimeSpentOnProject[Constants.PROJECT_ACTUAL_COST] != null ? employeeCostAndTimeSpentOnProject[Constants.PROJECT_ACTUAL_COST] : 0d).add(employeeCost.getActualWageAmount(calcScale)));
                    totalActual = totalActual.add(BigDecimal.valueOf(employeeCostAndTimeSpentOnProject[Constants.PROJECT_ACTUAL_COST] != null ? employeeCostAndTimeSpentOnProject[Constants.PROJECT_ACTUAL_COST] : 0d));
                }
                employeeCost.setPlannedWageAmount(BigDecimal.valueOf(plannedAmount).add(employeeCost.getPlannedWageAmount(calcScale)));
                totalPlanned = totalPlanned.add(BigDecimal.valueOf(plannedAmount));
                employeeCost.setVarianceAmount(employeeCost.getPlannedWageAmount(calcScale).subtract(employeeCost.getActualWageAmount(calcScale)));
                if (employeeCost.getActualWageAmount(calcScale).doubleValue() != 0d) {
                    employeeCost.setVariancePerCent(employeeCost.getVarianceAmount().divide(employeeCost.getActualWageAmount(calcScale), 4, RoundingMode.HALF_UP));
                }

                budgetItems.put(uniqueKey, employeeCost);
                if (!employeeProjectDeletedCheck.containsKey(uniqueKey)) {
                    employeeProjectDeletedCheck.put(uniqueKey, (employeePro.getDeleted() &&
                            !employeePro.getEmployeeDepartment().getEmployee().getAccountStatus().getObjectID().equals(inactiveSts.getObjectID())));
                } else if (!employeePro.getDeleted()) {
                    employeeProjectDeletedCheck.put(uniqueKey, (employeePro.getDeleted() &&
                            !employeePro.getEmployeeDepartment().getEmployee().getAccountStatus().getObjectID().equals(inactiveSts.getObjectID())));
                }
            }
            for (String key : employeeProjectDeletedCheck.keySet()) {
                Boolean isAllEmployeeProjectsDeleted = employeeProjectDeletedCheck.get(key);
                if (budgetItems.containsKey(key) && isAllEmployeeProjectsDeleted && budgetItems.get(key).getActualWageAmount(calcScale).intValue() == 0) {
                    totalPlanned = totalPlanned.subtract(budgetItems.get(key).getPlannedWageAmount(calcScale));
                    budgetItems.remove(key);
                }
            }
            projectBudget.setEmployeeCosts(budgetItems.values().toArray(new ProjectBudgetItem[]{}));
            projectBudget.getSubTotalEmployees().setActualWageAmount(totalActual);
            projectBudget.getSubTotalEmployees().setPlannedWageAmount(totalPlanned);
            projectBudget.getSubTotalEmployees().setVarianceAmount(projectBudget.getSubTotalEmployees().getPlannedWageAmount(calcScale).subtract(projectBudget.getSubTotalEmployees().getActualWageAmount(calcScale)));
            if (projectBudget.getSubTotalEmployees().getActualWageAmount(calcScale).doubleValue() != 0d) {
                projectBudget.getSubTotalEmployees().setVariancePerCent(projectBudget.getSubTotalEmployees().getVarianceAmount().divide(projectBudget.getSubTotalEmployees().getActualWageAmount(calcScale), 4, RoundingMode.HALF_UP));
            }
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setFromBudgetSheet(true);
        {//Expense Reports/Claims
            filterParameter.setProjectId(projectID);
            Integer[] statusIDs = new Integer[5];
            statusIDs[0] = this.referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_APPROVED).getObjectID();
            statusIDs[1] = this.referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_PAID).getObjectID();
            statusIDs[2] = this.referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_SUBMITTED).getObjectID();
            statusIDs[3] = this.referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_CLOSED).getObjectID();
            statusIDs[4] = this.referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.PARTIALLY_PAID).getObjectID();

            filterParameter.setStatusIDs(statusIDs);
            List<EdsExpenseReport> expenseClaims = this.projectManager.getAllExpenseReports(filterParameter);
            ProjectBudgetItem[] expense = new ProjectBudgetItem[expenseClaims.size()];
            i = 0;
            totalPlanned = new BigDecimal(0);
            totalActual = new BigDecimal(0);
            for (EdsExpenseReport expenseReport : expenseClaims) {
                expense[i] = new ProjectBudgetItem();
                WageTaxItem item = this.getBaseTotal(expenseReport, projectIDs);
                item.setWithTax(withTax);
                expense[i].setName(expenseReport.getNumber());
                expense[i].setPlannedWageAmount(item.getTotal());
                if (!Constants.EXPENSE_SUBMITTED.equals(expenseReport.getStatus().getCode())) {
                    expense[i].setActualWageAmount(expense[i].getPlannedWageAmount());
                }
                expense[i].setAction("expenseReports|previewReport/" + expenseReport.getObjectID() + "/" + Constants.EXPENSE_VIEW);
                expense[i].setVendor(expenseReport.getSupplier() != null ? expenseReport.getSupplier().getAsSelectItem() : null);
                totalPlanned = totalPlanned.add(expense[i].getPlannedWageAmount(calcScale));
                totalActual = totalActual.add(expense[i].getActualWageAmount(calcScale));
                i++;
            }
            projectBudget.setExpenseClaims(expense);
            projectBudget.getSubTotalExpences().setPlannedWageAmount(totalPlanned);
            projectBudget.getSubTotalExpences().setActualWageAmount(totalActual);
            projectBudget.getSubTotalExpences().setVarianceAmount(projectBudget.getSubTotalExpences().getPlannedWageAmount(calcScale).subtract(projectBudget.getSubTotalExpences().getActualWageAmount(calcScale)));
            if (projectBudget.getSubTotalExpences().getActualWageAmount(calcScale).doubleValue() != 0d) {
                projectBudget.getSubTotalExpences().setVariancePerCent(projectBudget.getSubTotalExpences().getVarianceAmount().divide(projectBudget.getSubTotalExpences().getActualWageAmount(calcScale), 4, RoundingMode.HALF_UP));
            }
        }

        {// Bank Payments
            listingFilterParameter.setType(1);
            List<EdsBankTransfer> bankPaymentList = this.spendReceiveMoneyManager.getBankTransferList(listingFilterParameter, Constants.EXPENSES);

            ProjectBudgetItem[] bankPayments = new ProjectBudgetItem[bankPaymentList.size()];
            i = 0;
            totalActual = new BigDecimal(0);
            for (EdsBankTransfer bankTransfer : bankPaymentList) {
                bankPayments[i] = new ProjectBudgetItem();
                WageTaxItem item = this.getBankTransferBaseTotal(bankTransfer, projectIDs, projectInLineItemEnable, true);
                item.setWithTax(withTax);
                bankPayments[i].setName(bankTransfer.getNumber() + " -> " + bankTransfer.getName());
                bankPayments[i].setActualWageAmount(item.getTotal());
                bankPayments[i].setAction("spendreceivemoney|summary/" + bankTransfer.getObjectID() + "/" + "SPEND_MONEY");
                totalActual = totalActual.add(bankPayments[i].getActualWageAmount(calcScale));
                i++;
            }
            projectBudget.setBankPayments(bankPayments);
            projectBudget.getSubTotalBankPayments().setActualWageAmount(totalActual);
        }

        {//Cash Payment
            listingFilterParameter.setType(3);
            List<EdsBankTransfer> cashPaymentList = this.spendReceiveMoneyManager.getBankTransferList(listingFilterParameter, Constants.EXPENSES);
            ProjectBudgetItem[] cashPaymants = new ProjectBudgetItem[cashPaymentList.size()];
            i = 0;
            totalActual = new BigDecimal(0);
            for (EdsBankTransfer bankTransfer : cashPaymentList) {
                cashPaymants[i] = new ProjectBudgetItem();
                WageTaxItem item = this.getBankTransferBaseTotal(bankTransfer, projectIDs, projectInLineItemEnable, true);
                item.setWithTax(withTax);
                cashPaymants[i].setName(bankTransfer.getNumber() + " -> " + bankTransfer.getName());
                cashPaymants[i].setActualWageAmount(item.getTotal());
                cashPaymants[i].setAction("spendreceivemoney|summary/" + bankTransfer.getObjectID() + "/" + "CASH_PAYMENT");
                totalActual = totalActual.add(cashPaymants[i].getActualWageAmount());
                i++;
            }
            projectBudget.setCashPayments(cashPaymants);
            projectBudget.getSubTotalCashPayments().setActualWageAmount(totalActual);
        }

        {//Purchase Order
            InvoiceList purchaseOrderList = this.invoiceCircularResolver.getPurchaseOrderData(listingFilterParameter, null);
            ProjectBudgetItem[] purchaseOrders = new ProjectBudgetItem[purchaseOrderList.getList().size()];
            i = 0;
            totalPlanned = new BigDecimal(0);
            for (NewInvoice purchaseOrder : purchaseOrderList.getList()) {
                purchaseOrders[i] = new ProjectBudgetItem();
                WageTaxItem item = this.getBaseTotal(purchaseOrder, projectIDs, isAgencyFees, projectInLineItemEnable);
                item.setWithTax(withTax);
                purchaseOrders[i].setPlannedWageAmount(item.getTotal());
                purchaseOrders[i].setName(purchaseOrder.getInvoiceNumber());
                purchaseOrders[i].setAction("purchaseorder|summary/" + purchaseOrder.getID());
                if (purchaseOrder.getClientID() != null && purchaseOrder.getClientName() != null) {
                    purchaseOrders[i].setVendor(new SelectItem(purchaseOrder.getClientID(), purchaseOrder.getClientName()));
                }
                totalPlanned = totalPlanned.add(purchaseOrders[i].getPlannedWageAmount(calcScale));
                i++;
            }
            projectBudget.setPurchaseOrders(purchaseOrders);
            projectBudget.getSubTotalPurchases().setPlannedWageAmount(totalPlanned);
        }

        {//Purchase Invoices
            InvoiceList purchaseInvoiceList = this.invoiceCircularResolver.getPurchaseInvoiceData(filterParameter);
            ProjectBudgetItem[] purchaseInvoices = new ProjectBudgetItem[purchaseInvoiceList.getList().size()];
            i = 0;
            totalActual = new BigDecimal(0);
            for (NewInvoice purchaseInvoice : purchaseInvoiceList.getList()) {
                purchaseInvoices[i] = new ProjectBudgetItem();
                WageTaxItem item = this.getBaseTotal(purchaseInvoice, projectIDs, isAgencyFees, projectInLineItemEnable);
                item.setWithTax(withTax);
                purchaseInvoices[i].setName(purchaseInvoice.getInvoiceNumber());
                if (purchaseInvoice.isCreditNote()) {
                    purchaseInvoices[i].setAction("payablecreditnote|summary/" + purchaseInvoice.getID());
                    purchaseInvoices[i].setActualWageAmount(BigDecimal.ZERO.subtract(item.getTotal()));
                } else {
                    purchaseInvoices[i].setAction("purchaseinvoice|summary/" + purchaseInvoice.getID());
                    purchaseInvoices[i].setActualWageAmount(item.getTotal());
                }
                if (purchaseInvoice.getClientID() != null && purchaseInvoice.getClientName() != null) {
                    purchaseInvoices[i].setVendor(new SelectItem(purchaseInvoice.getClientID(), purchaseInvoice.getClientName()));
                }
                totalActual = totalActual.add(purchaseInvoices[i].getActualWageAmount(calcScale));
                i++;
            }
            projectBudget.setPurchaseInvoices(purchaseInvoices);

            projectBudget.getSubTotalPurchases().setActualWageAmount(totalActual);
            projectBudget.getSubTotalPurchases().setVarianceAmount(projectBudget.getSubTotalPurchases().getPlannedWageAmount(calcScale).subtract(projectBudget.getSubTotalPurchases().getActualWageAmount(calcScale)));
            if (projectBudget.getSubTotalPurchases().getActualWageAmount(calcScale).doubleValue() != 0d) {
                projectBudget.getSubTotalPurchases().setVariancePerCent(projectBudget.getSubTotalPurchases().getVarianceAmount().divide(projectBudget.getSubTotalPurchases().getActualWageAmount(calcScale), 4, RoundingMode.HALF_UP));
            }
        }

        {// Stock Adjustments
            projectBudget.setStockAdjustments(this.stockAdjustmentItemManager.getStockAdjustmentItems(filterParameter).toArray(new ProjectBudgetItem[]{}));
            totalActual = BigDecimal.ZERO;
            for (ProjectBudgetItem stockAdj : projectBudget.getStockAdjustments()) {
                totalActual = totalActual.add(stockAdj.getActualWageAmount());
            }
        }

        //Manual Entry Expenses
        ProjectBudgetItem[] manualJournalExpenses;
        if (manualJournalItemList != null && manualJournalItemList.size() > 0) {
            HashMap<String, SelectItem> itemValue = new HashMap<>();
            for (EdsManualJournalItem manualJournal : manualJournalItemList) {
                if ((Constants.EXPENSES.equals(manualJournal.getAccount().getAccountType().getCategory()) && manualJournal.getDebitInBase() != null && manualJournal.getDebitInBase().compareTo(BigDecimal.ZERO) > 0)
                        || (Constants.REVENUE.equals(manualJournal.getAccount().getAccountType().getCategory()) && manualJournal.getDebitInBase() != null && manualJournal.getDebitInBase().compareTo(BigDecimal.ZERO) > 0)) {
                    if (itemValue.containsKey(manualJournal.getManualTransfer().getNumber())) {
                        Double amount = itemValue.get(manualJournal.getManualTransfer().getNumber()).getTotalAmount();
                        amount += manualJournal.getDebitInBase().doubleValue();
                        itemValue.get(manualJournal.getManualTransfer().getNumber()).setTotalAmount(amount);
                    } else {
                        SelectItem item = new SelectItem();
                        item.setTotalAmount(manualJournal.getDebitInBase().doubleValue());
                        item.setId(manualJournal.getManualTransfer().getObjectID());
                        item.setNumber(manualJournal.getManualTransfer().getNumber());
                        itemValue.put(manualJournal.getManualTransfer().getNumber(), item);
                    }
                }
            }

            manualJournalExpenses = new ProjectBudgetItem[itemValue.size()];
            i = 0;
            for (SelectItem manualJournalItem : itemValue.values()) {

                manualJournalExpenses[i] = new ProjectBudgetItem();
                manualJournalExpenses[i].setName(manualJournalItem.getNumber());
                manualJournalExpenses[i].setActualWageAmount(BigDecimal.valueOf(manualJournalItem.getTotalAmount()));
                manualJournalExpenses[i].setAction("manual|summary/" + manualJournalItem.getId());
                totalActual = totalActual.add(manualJournalExpenses[i].getActualWageAmount());
                i++;
            }
        } else {
            manualJournalExpenses = new ProjectBudgetItem[0];
        }
        projectBudget.setManualEntryExpense(manualJournalExpenses);


        /////TOTALS/////
        //Project Cost//
        totalPlanned = projectBudget.getSubTotalExpences().getPlannedWageAmount(calcScale).add(projectBudget.getSubTotalEmployees().getPlannedWageAmount(calcScale));
        totalActual = projectBudget.getSubTotalExpences().getActualWageAmount(calcScale).add(projectBudget.getSubTotalEmployees().getActualWageAmount(calcScale)).add(totalActual.setScale(calcScale, RoundingMode.HALF_UP));
        projectBudget.getTotalProjectCost().setPlannedWageAmount(totalPlanned.add(projectBudget.getSubTotalPurchases().getPlannedWageAmount(calcScale)));
        projectBudget.getTotalProjectCost().setActualWageAmount(totalActual.add(projectBudget.getSubTotalPurchases().getActualWageAmount(calcScale)).add(projectBudget.getSubTotalBankPayments().getActualWageAmount(calcScale)).add(projectBudget.getSubTotalCashPayments().getActualWageAmount(calcScale)));
        projectBudget.getTotalProjectCost().setVarianceAmount(projectBudget.getTotalProjectCost().getPlannedWageAmount(calcScale).subtract(projectBudget.getTotalProjectCost().getActualWageAmount(calcScale)));

        if (projectBudget.getTotalProjectCost().getActualWageAmount(calcScale).doubleValue() != 0d) {
            projectBudget.getTotalProjectCost().setVariancePerCent(projectBudget.getTotalProjectCost().getVarianceAmount().divide(projectBudget.getTotalProjectCost().getActualWageAmount(calcScale), 4, RoundingMode.HALF_UP));
        }
        ////
        projectBudget.getTotalProfit().setPlannedWageAmount(projectBudget.getSubTotalRevenue().getPlannedWageAmount(calcScale).subtract(projectBudget.getTotalProjectCost().getPlannedWageAmount(calcScale)));
        projectBudget.getTotalProfit().setActualWageAmount(projectBudget.getSubTotalRevenue().getActualWageAmount(calcScale).subtract(projectBudget.getTotalProjectCost().getActualWageAmount(calcScale)));
        projectBudget.getTotalProfit().setVarianceAmount(projectBudget.getTotalProfit().getActualWageAmount(calcScale).subtract(projectBudget.getTotalProfit().getPlannedWageAmount(calcScale)));
        if (projectBudget.getTotalProfit().getPlannedWageAmount(calcScale).doubleValue() != 0d) {
            projectBudget.getTotalProfit().setVariancePerCent(projectBudget.getTotalProfit().getVarianceAmount().divide(projectBudget.getTotalProfit().getPlannedWageAmount(calcScale), 4, RoundingMode.HALF_UP));
        }
        projectBudget.setProjectName(project != null ? project.getName() : "");
        return projectBudget;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectBudget getEmployeeCostClientCharge(Integer projectID) {
        ProjectBudget projectBudget = new ProjectBudget();
        ////Employee costs////
        List<EdsProjectEmployee> employeeProjects = this.projectManager.getEmployeesByProjectAll(projectID);
        BigDecimal totalPlanned = new BigDecimal(0);
        BigDecimal totalActual = new BigDecimal(0);
        Map<String, ProjectBudgetItem> budgetItems = new HashMap<>();
        Map<String, Boolean> employeeProjectDeletedCheck = new HashMap<>();
        Map<String, Double[]> employeeCostAndTimeSpentOnProjectMap = this.timeSheetManager.getEmployeeCostAndTimeSpentOnProjects(projectID);
        for (EdsProjectEmployee employeePro : employeeProjects) {
            String uniqueKey = employeePro.getEmployeeDepartment().getEmployee().getObjectID() + "/" + employeePro.getProject().getObjectID();
            Double[] employeeCostAndTimeSpentOnProject = employeeCostAndTimeSpentOnProjectMap.get(uniqueKey);
            ProjectBudgetItem employeeCost;
            if (budgetItems.containsKey(uniqueKey)) {
                employeeCost = budgetItems.get(uniqueKey);
            } else {
                employeeCost = new ProjectBudgetItem();
            }
            String employeeName = employeePro.getEmployeeDepartment().getEmployee().getFullName();
            EdsReference inactiveSts = this.referenceManager.findReference(Constants.EMPLOYEE_STATUS, Constants.EMPLOYEE_STATUS_RESIGNED);
            if (employeePro.getEmployeeDepartment().getEmployee().getAccountStatus().getObjectID().equals(inactiveSts.getObjectID())) {
                employeeName = employeeName + " (" + this.commonLocalizer.localize("resigned", "Resigned") + ")";
            }
            employeeCost.setName(employeeName);
            if (employeeCostAndTimeSpentOnProject != null && !employeeProjectDeletedCheck.containsKey(uniqueKey)) {
                employeeCost.setActualWageAmount(BigDecimal.valueOf(employeeCostAndTimeSpentOnProject[Constants.PROJECT_ACTUAL_CLIENT_CHARGE] != null ? employeeCostAndTimeSpentOnProject[Constants.PROJECT_ACTUAL_CLIENT_CHARGE] : 0d).add(employeeCost.getActualWageAmount()));
                totalActual = totalActual.add(BigDecimal.valueOf(employeeCostAndTimeSpentOnProject[Constants.PROJECT_ACTUAL_CLIENT_CHARGE] != null ? employeeCostAndTimeSpentOnProject[Constants.PROJECT_ACTUAL_CLIENT_CHARGE] : 0d));
                employeeCost.setPlannedWageAmount(BigDecimal.valueOf(employeeCostAndTimeSpentOnProject[Constants.PROJECT_ESTIMATED_CLIENT_CHARGE] != null ? employeeCostAndTimeSpentOnProject[Constants.PROJECT_ESTIMATED_CLIENT_CHARGE] : 0d).add(employeeCost.getPlannedWageAmount()));
                totalPlanned = totalPlanned.add(BigDecimal.valueOf(employeeCostAndTimeSpentOnProject[Constants.PROJECT_ESTIMATED_CLIENT_CHARGE] != null ? employeeCostAndTimeSpentOnProject[Constants.PROJECT_ESTIMATED_CLIENT_CHARGE] : 0d));
            }
            employeeCost.setVarianceAmount(employeeCost.getPlannedWageAmount().subtract(employeeCost.getActualWageAmount()));
            if (employeeCost.getActualWageAmount() != null && employeeCost.getActualWageAmount().doubleValue() != 0d) {
                employeeCost.setVariancePerCent(employeeCost.getVarianceAmount().divide(employeeCost.getActualWageAmount(), 4, RoundingMode.HALF_UP));
            }

            budgetItems.put(uniqueKey, employeeCost);
            if (!employeeProjectDeletedCheck.containsKey(uniqueKey)) {
                employeeProjectDeletedCheck.put(uniqueKey, (employeePro.getDeleted() &&
                        !employeePro.getEmployeeDepartment().getEmployee().getAccountStatus().getObjectID().equals(inactiveSts.getObjectID())));
            } else if (!employeePro.getDeleted()) {
                employeeProjectDeletedCheck.put(uniqueKey, (employeePro.getDeleted() &&
                        !employeePro.getEmployeeDepartment().getEmployee().getAccountStatus().getObjectID().equals(inactiveSts.getObjectID())));
            }
        }
        for (String key : employeeProjectDeletedCheck.keySet()) {
            Boolean isAllEmployeeProjectsDeleted = employeeProjectDeletedCheck.get(key);
            if (budgetItems.containsKey(key) && isAllEmployeeProjectsDeleted && budgetItems.get(key).getActualWageAmount().intValue() == 0) {
                budgetItems.remove(key);
            }
        }
        projectBudget.setEmployeeCosts(budgetItems.values().toArray(new ProjectBudgetItem[]{}));
        projectBudget.getSubTotalEmployees().setActualWageAmount(totalActual);
        projectBudget.getSubTotalEmployees().setPlannedWageAmount(totalPlanned);
        projectBudget.getSubTotalEmployees().setVarianceAmount(projectBudget.getSubTotalEmployees().getPlannedWageAmount().subtract(projectBudget.getSubTotalEmployees().getActualWageAmount()));
        if (projectBudget.getSubTotalEmployees().getActualWageAmount() != null && projectBudget.getSubTotalEmployees().getActualWageAmount().doubleValue() != 0d) {
            projectBudget.getSubTotalEmployees().setVariancePerCent(projectBudget.getSubTotalEmployees().getVarianceAmount().divide(projectBudget.getSubTotalEmployees().getActualWageAmount(), 4, RoundingMode.HALF_UP));
        }


        return projectBudget;
    }

    private WageTaxItem getBaseTotal(EdsExpenseReport er, List<Integer> projectIDs) {
        boolean projectInLineItemEnable = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        boolean addMarkUP = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ADD_EXPENSE_MARKUP_TO_PROJECT);

        BigDecimal total = new BigDecimal("0.00");
        BigDecimal tax = new BigDecimal("0.00");
//        BigDecimal exchangeRate = er.getExchangeRate() != null ? er.getExchangeRate() : BigDecimal.ONE;
        for (EdsExpense p : er.getExpenses()) {
            if (!projectInLineItemEnable || (projectInLineItemEnable && p.getProject() != null && projectIDs.contains(p.getProject().getObjectID()))) {
                BigDecimal net = p.getUnits().multiply(p.getCostPerUnit());
                BigDecimal markUpAmount = p.getMarkupAmount() != null ? p.getMarkupAmount() : BigDecimal.ZERO;
                BigDecimal taxAmount = p.getTaxAmount() != null ? p.getTaxAmount() : BigDecimal.ZERO;
                BigDecimal doubleTaxAmount = p.getDoubleTaxAmount() != null ? p.getDoubleTaxAmount() : BigDecimal.ZERO;

                net = net.divide(er.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                taxAmount = taxAmount.add(doubleTaxAmount).divide(er.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                markUpAmount = markUpAmount.divide(er.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

//                BigDecimal markUpTaxAmount = p.getMarkupTaxAmount() != null ? p.getMarkupTaxAmount() : BigDecimal.ZERO;
                if (TAX_CALCULATION_INCLUSIVE.equals(er.getTaxCalculationType())) {
                    net = net.subtract(taxAmount);
                }
                total = total.add(net.add(addMarkUP ? markUpAmount : BigDecimal.valueOf(0.00)));
                tax = tax.add(taxAmount);
            }
        }
        return new WageTaxItem(tax, total);
    }

    private WageTaxItem getBaseTotal(NewInvoice invoice, List<Integer> projectIDs, boolean isAgencyFees, boolean projectInLineItemEnable) {
        BigDecimal total = new BigDecimal("0.00");
        BigDecimal tax = new BigDecimal("0.00");
        if (projectInLineItemEnable) {
            for (NewInvoiceItem item : invoice.getItems()) {
                if (item.getProject() != null && projectIDs.contains(item.getProject().getId())) {
                    BigDecimal net = item.getNet() != null ? item.getNet() : BigDecimal.ZERO;
                    BigDecimal allocatedExpense = item.getAllocatedExpense() != null ? item.getAllocatedExpense() : BigDecimal.ZERO;
                    BigDecimal taxAmount = item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO;
                    BigDecimal doubleTaxAmount = item.getDoubleTaxAmount() != null ? item.getDoubleTaxAmount() : BigDecimal.ZERO;
                    taxAmount = taxAmount.add(doubleTaxAmount).divide(invoice.getExchageRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

                    if (TAX_CALCULATION_INCLUSIVE.equals(invoice.getTaxCalculationType())) {
                        net = net.subtract(taxAmount);
                    }
                    total = total.add(net.add(allocatedExpense)/*.add(priceLevelAmount)*/);
                    tax = tax.add(taxAmount);
                }
            }
            total = total.subtract(invoice.getBillableExpenseTaxAmount() != null ? invoice.getBillableExpenseTaxAmount().divide(invoice.getExchageRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP) : BigDecimal.ZERO);
            tax = tax.add(invoice.getBillableExpenseTaxAmount() != null ? invoice.getBillableExpenseTaxAmount().divide(invoice.getExchageRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP) : BigDecimal.ZERO);
        } else {
            total = invoice.getBaseTotalWithoutTaxes(isAgencyFees);
            total = total.subtract(invoice.getBillableExpenseTaxAmount() != null ? invoice.getBillableExpenseTaxAmount().divide(invoice.getExchageRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP) : BigDecimal.ZERO);

            tax = invoice.getTotalTaxes();
            tax = tax.add(invoice.getBillableExpenseTaxAmount() != null ? invoice.getBillableExpenseTaxAmount().divide(invoice.getExchageRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP) : BigDecimal.ZERO);
        }
        return new WageTaxItem(tax, total);
    }

    private WageTaxItem getBankTransferBaseTotal(EdsBankTransfer bankTransfer, List<Integer> projectIDs, boolean projectInLineItemEnable, boolean payments) {
        BigDecimal total = new BigDecimal("0.00");
        BigDecimal tax = new BigDecimal("0.00");
        BigDecimal taxAmount;
        Integer taxCalcType = bankTransfer.getTaxCalculationType();
//        if (projectInLineItemEnable) {
        for (EdsBankTransferItem item : bankTransfer.getItems()) {
            if (((projectInLineItemEnable && item.getProject() != null && projectIDs.contains(item.getProject().getObjectID())) ||
                    (!projectInLineItemEnable && bankTransfer.getProject() != null && projectIDs.contains(bankTransfer.getProject().getObjectID()))) &&
                    (payments ? Constants.EXPENSES.equals(item.getAccount().getAccountType().getCategory()) : Constants.REVENUE.equals(item.getAccount().getAccountType().getCategory()))) {
                BigDecimal amount = item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO;
                BigDecimal taxRate = item.getTax() != null ? item.getTax().getTaxRateAsBigDecimal() : BigDecimal.ZERO;


                if (TAX_CALCULATION_INCLUSIVE.equals(taxCalcType)) {
                    taxAmount = item.getAmount().multiply(taxRate).divide(new BigDecimal("100.00").add(taxRate), 8, RoundingMode.HALF_UP);
                    amount = amount.subtract(taxAmount);
                } else {
                    taxAmount = item.getAmount().multiply(taxRate).divide(new BigDecimal("100.00"), 8, RoundingMode.HALF_UP);
                }
                amount = amount.divide(bankTransfer.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                taxAmount = taxAmount.divide(bankTransfer.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

                total = total.add(amount);
                tax = tax.add(taxAmount);
            }
        }
//        } else {
//            total = bankTransfer.getSubtotal().divide(bankTransfer.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
//            tax = bankTransfer.getTaxTotal();
//
//            if (taxCalcType != null && TAX_CALCULATION_INCLUSIVE.equals(taxCalcType)) {
//                total = total.subtract(tax);
//            }
//        }
        return new WageTaxItem(tax, total);
    }

    public SelectItem[] getTimeSheetClients() {
        EdsEmployee employee = (EdsEmployee) this.taskManager.getUser();
        ArrayList<SelectItem> clients = new ArrayList<>();
        List<EdsProjectEmployee> projectEmployees = this.projectManager.getEmployeeNotStartedOnGoingProjects(employee);
        for (EdsProjectEmployee projectEmployee : projectEmployees) {
            EdsProject domainProject = projectEmployee.getProject();
            if (domainProject == null) {
                continue;
            }
            EdsCrmAccount client = domainProject.getClient();

            if (client != null) {
                SelectItem cItem = new SelectItem(client.getObjectID(), client.getName());
                if (!clients.contains(cItem)) {
                    clients.add(cItem);
                }
            }
        }
        SelectItem[] clientItems = clients.toArray(new SelectItem[]{});
        Arrays.sort(clientItems, Comparator.comparing(SelectItem::getName));
        return clientItems;
    }

    @Transactional
    public SelectItem[] getLookUpItems(ListingFilterParameter filterParameters, int type) {
        SelectItem[] items;
        filterParameters.setLookUp(true);
        items = switch (type) {
            case LookUpConstants.PM_PROJECT_ID -> this.getPMLookNames(filterParameters, LookUpConstants.PROJECT);
            case LookUpConstants.PM_TASK_ID -> this.getPMLookNames(filterParameters, LookUpConstants.TASK);
            case LookUpConstants.PM_EMPLOYEE_ID -> this.getPMLookNames(filterParameters, LookUpConstants.EMPLOYEE);
            case LookUpConstants.PM_DEPARTMENT_ID -> this.getPMLookNames(filterParameters, LookUpConstants.DEPARTMENT);
            case LookUpConstants.PM_ISSUE_ID -> this.getPMLookNames(filterParameters, LookUpConstants.ISSUE);
            case LookUpConstants.PM_TASK_ASSIGNEE_ID ->
                    this.getPMLookNames(filterParameters, LookUpConstants.TASK_ASSIGNEE);
            default -> null;
        };
        return items;
    }

    @Transactional
    public ListResult<SelectItem> getProjectLookUp(ListingFilterParameter filterParameter) {
        ListResult<ProjectListItem> projectResultList = this.getProjectList(filterParameter);
        ArrayList<SelectItem> projectList = new ArrayList<>();
        projectResultList.getList().forEach(project -> projectList.add(new SelectItem(project.getObjectId(), project.getName())));

        return new ListResult<>(projectList, projectResultList.getTotal());
    }

    private SelectItem[] getPMLookNames(ListingFilterParameter filterParameters, String type) {
        switch (type) {
            case LookUpConstants.PROJECT:
                if (filterParameters.isNewType()) {
                    List<EdsProject> projects = this.projectManager.projectsList(filterParameters);
                    return projects.stream()
                            .map(project -> new SelectItem(project.getObjectID(), (project.getNumber() != null ? project.getNumber() + " - " : "") + project.getName()))
                            .toArray(SelectItem[]::new);
                } else {
                    ListResult<ProjectListItem> projectList = this.getProjectList(filterParameters);
                    if (projectList != null) {
                        return projectList.getList().stream()
                                .map(listItem -> new SelectItem(listItem.getObjectId(), (listItem.getNumber() != null ? listItem.getNumber() + " - " : "") + listItem.getName()))
                                .toArray(SelectItem[]::new);
                    }
                }
                break;

            case LookUpConstants.TASK:
                TaskList taskList = this.taskServiceLocal.getTaskList(filterParameters);
                if (taskList != null && !taskList.getList().isEmpty()) {
                    return taskList.getList().stream()
                            .map(taskListItem -> new SelectItem(taskListItem.getObjectID(), (filterParameters.isExcludeNumber() ? "" : taskListItem.getNumber() + " - ") + taskListItem.getName()))
                            .toArray(SelectItem[]::new);
                }
                break;

            case LookUpConstants.EMPLOYEE:
                return this.allInOneService.getEmployeesAsSelectItem(filterParameters);

            case LookUpConstants.DEPARTMENT:
                EdsUser user = employeeManager.getUser();
                if (filterParameters.getViewAsId() == null) {
                    EdsRole maximumRole = user.getRolesSortedByPattern().get(0);
                    filterParameters.setViewAsId(maximumRole.getObjectID());
                }
                List<EdsDepartment> departments = this.departmentManager.list(filterParameters);
                return departments.stream()
                        .map(department -> new SelectItem(department.getObjectID(), department.getName()))
                        .sorted(Comparator.comparing(SelectItem::getName))
                        .toArray(SelectItem[]::new);

            case LookUpConstants.TASK_ASSIGNEE:
                List<EdsEmployee> employeeList = this.taskManager.getTasksAssigneeByDate(filterParameters);
                return employeeList.stream()
                        .map(employee -> new SelectItem(employee.getObjectID(), employee.getName()))
                        .toArray(SelectItem[]::new);

            case LookUpConstants.ISSUE:
                List<EdsIssue> issueList = this.issueManager.list(filterParameters);
                if (issueList != null && !issueList.isEmpty()) {
                    return issueList.stream()
                            .map(issue -> new SelectItem(issue.getObjectID(), issue.getName()))
                            .sorted(Comparator.comparing(SelectItem::getName))
                            .toArray(SelectItem[]::new);
                }
                break;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<BookingItemsItem> getBookingItems(ListingFilterParameter fp) {
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsBookingItem.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Get booking item list");

        List<EdsBookingItem> bookingItems = this.bookingItemManager.getBookingItemList(fp);
        Integer totalCount = this.bookingItemManager.getBookingItemTotalCount(fp);
        ArrayList<BookingItemsItem> items = new ArrayList<>();
        ListPanelToolRpc panelSettings = fp.getListPanelTool();

        for (EdsBookingItem bookItem : bookingItems) {
            BookingItemsItem item = new BookingItemsItem();
            item.setObjectID(bookItem.getObjectID());
            item.setItemName(bookItem.getName());
            item.setItemNumber(bookItem.getItemNumber());
            item.setDescription(bookItem.getDescription());
            item.setCategory(bookItem.getCategory().getAsSelectItem());
            List<EdsBookingItemReservation> validationReservation = this.bookingItemReservationManager.getReservationStatus(bookItem.getObjectID());
            if (validationReservation != null && validationReservation.size() > 0) {
                item.setStatus("Not Available");
            } else {
                item.setStatus("Available");
            }

            if (bookItem.getLocation() != null) {
                this.initBookingItemLocation(bookItem.getLocation(), item);
            }
            if (panelSettings != null) {
                HashMap<String, Object> map = CustomFieldsUtils.getRPCCustomFields(bookItem.getCustomFields(), panelSettings.getColumnCodeName());
                item.setCustomFieldValuesItems(commonServiceLocal.getLocaledCustomFiledMap(map, panelSettings.getListViewCustomFields()));
            }
            items.add(item);
        }
        return new ListResult<>(items, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BookingItemsItem getBookingItemsData(Integer bookingItemsId) {
        if (bookingItemsId != null) {
            KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsBookingItem.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            kpiLog.setEntityId(bookingItemsId);
            ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "View booking item");
        }
        BookingItemsItem bookingItems = new BookingItemsItem();
        bookingItems.setNumberData(this.generateBookingItemNumber());
        if (bookingItemsId != null) {
            EdsBookingItem edsbookingItems = this.bookingItemManager.get(bookingItemsId);
            if (edsbookingItems != null) {
                bookingItems.setItemName(edsbookingItems.getName());
                bookingItems.setDescription(edsbookingItems.getDescription());
                bookingItems.setObjectID(edsbookingItems.getObjectID());
                if (edsbookingItems.getItemNumber() != null) {
                    bookingItems.setItemNumber(edsbookingItems.getItemNumber());
                    bookingItems.getNumberData().setNumberString(edsbookingItems.getItemNumber());
                    bookingItems.getNumberData().setIntNumber(edsbookingItems.getIntNumber());
                }
                bookingItems.setCategory(edsbookingItems.getCategory().getAsSelectItem());
                if (edsbookingItems.getLocation() != null) {
                    this.initBookingItemLocation(edsbookingItems.getLocation(), bookingItems);
                }
            }
            List<EdsBookingItemReservation> reservationByBookingid = this.bookingItemReservationManager.getReservationByBookingid(bookingItemsId);
            ArrayList<BookingReservationItem> result = new ArrayList<>();
            for (EdsBookingItemReservation edsBookingItemReservation : reservationByBookingid) {
                BookingReservationItem items = new BookingReservationItem();
                items.setObjectID(edsBookingItemReservation.getObjectID());
                if (edsBookingItemReservation.getReservedBy() != null) {
                    items.setSelectedReservedById(edsBookingItemReservation.getReservedBy().getAsSelectItem());
                }
                if (edsBookingItemReservation.getFrom() != null) {
                    items.setFromDate(edsBookingItemReservation.getFrom());
                }
                if (edsBookingItemReservation.getFrom() != null) {
                    items.setToDate(edsBookingItemReservation.getTo());
                }

                result.add(items);
            }
            bookingItems.setBookingReservationItemList(result);
            List<EdsBookingItemReservation> validationReservation = this.bookingItemReservationManager.getReservationStatus(bookingItems.getObjectID());
            if (validationReservation != null && validationReservation.size() > 0) {
                bookingItems.setStatus("Not Available");
            } else {
                bookingItems.setStatus("Available");
            }
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.BookingItemsView);
            bookingItems.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsbookingItems.getCustomFields(), customFieldsItems));
        }
        List<EdsReference> itemCategories = this.referenceManager.listReferences(Constants.BOOKING_ITEM_CATEGORY);
        if (itemCategories != null) {
            SelectItem[] categoriesItems = new SelectItem[itemCategories.size()];
            int i = 0;
            for (EdsReference reference : itemCategories) {
                categoriesItems[i++] = reference.getAsSelectItem();
            }
            bookingItems.setCategories(categoriesItems);
        }
        bookingItems.setLocations(this.locationManager.getLocationsAsSelectItems(new ListingFilterParameter()));
        bookingItems.setLayoutHTML(PathFinder.getLayoutHTML("BOOKINGITEMS"));
        return bookingItems;
    }

    @Override
    public SelectItem[] getProjectClients(Integer projectID) {
        EdsProject project = this.projectManager.get(projectID);

        if (project != null) {
            ArrayList<SelectItem> items = new ArrayList<>();

            if (project.getClients() != null && !project.getClients().isEmpty()) {
                for (EdsCrmAccount client : project.getClients()) {
                    items.add(client.getAsSelectItem());
                }
            } else if (project.getClient() != null) {
                items.add(project.getClient().getAsSelectItem());
            }

            if (!items.isEmpty()) {
                return items.toArray(new SelectItem[]{});
            }
        }
        return new SelectItem[0];
    }

    @Override
    public void deleteBookingItem(Integer id) {
        EdsBookingItem bookingItem = this.bookingItemManager.get(id);
        bookingItem.setDeleted(true);
    }

    private void initBookingItemLocation(EdsLocation location, BookingItemsItem bookingItems) {
        bookingItems.setLocationID(location.getObjectID());
        String _location = location.getCountry().getName();

        if (location.getState() != null) {
            _location += " " + location.getState().getName();
        }

        if (location.getCity() != null && !location.getCity().isEmpty()) {
            _location += " " + location.getCity();
        }
        bookingItems.setLocation(_location);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BookingReservationItem getBookingItemsReservationData(Integer reservationItem) {

        BookingReservationItem reservationItems = new BookingReservationItem();
        EdsBookingItemReservation reservation = null;
        List<EdsUser> usersList = this.userManager.getUsers();
        if (usersList != null) {
            SelectItem[] userItems = new SelectItem[usersList.size()];
            int i = 0;
            for (EdsUser user : usersList) {
                userItems[i++] = user.getAsSelectItem();
            }
            reservationItems.setReservedByIds(userItems);
            reservationItems.setSelectedReservedById(this.userManager.getUser().getAsSelectItem());
        }

        List<EdsReference> categoriesItemsList = this.referenceManager.listReferences("_BOOKING_ITEM_CATEGORY");
        if (categoriesItemsList != null) {
            SelectItem[] categoriesItems = new SelectItem[categoriesItemsList.size()];
            int i = 0;
            for (EdsReference reference : categoriesItemsList) {
                categoriesItems[i++] = reference.getAsSelectItem();
            }
            reservationItems.setCategories(categoriesItems);

            if (reservationItem != null) {
                if (this.bookingItemManager.getBookingItemById(reservationItem).size() > 0) {
                    reservationItems.setSelectedCategoryId(this.bookingItemManager.getBookingItemById(reservationItem).get(0).getCategory().getAsSelectItem());
                }
                reservation = this.bookingItemReservationManager.get(reservationItem);
                if (reservation != null) {
                    reservationItems.setSelectedReservedById(reservation.getReservedBy().getAsSelectItem());
                    reservationItems.setSelectedCategoryId(reservation.getBookingItem().getCategory().getAsSelectItem());
                    reservationItems.setFromDate(reservation.getFrom());
                    reservationItems.setToDate(reservation.getTo());
                }
            }
        }

        List<EdsBookingItem> bookingItemList = this.bookingItemManager.getBookingItemList();
        if (bookingItemList != null) {
            SelectItem[] bookingItems = new SelectItem[bookingItemList.size()];
            int i = 0;
            for (EdsBookingItem item : bookingItemList) {
                bookingItems[i++] = item.getAsSelectItem();
            }
            reservationItems.setBookingItems(bookingItems);
            if (reservation != null) {
                reservationItems.setSelectedBookingItemId(reservation.getBookingItem().getAsSelectItem());
            }
        }
        reservationItems.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_BOOKING, reservationItem)));

        reservationItems.setLayoutHTML(PathFinder.getLayoutHTML("BOOKINGITEMSRESERVATION"));
        return reservationItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BookingReservationItem getBookingItemReservation(Integer reservationID) {

        BookingReservationItem reservationItem = new BookingReservationItem();
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsBookingItemReservation.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(reservationID);
        ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "View booking item reservation");
        EdsBookingItemReservation reservation = this.bookingItemReservationManager.get(reservationID);
        reservationItem.setLayoutHTML(this.layoutManager.getLayout("BOOKINGITEMSRESERVATION", LayoutRPC.VIEW).getLayout());
        reservationItem.setSelectedReservedById(reservation.getReservedBy().getAsSelectItem());
        reservationItem.setSelectedCategoryId(reservation.getBookingItem().getCategory().getAsSelectItem());
        reservationItem.setBookingItemName(reservation.getBookingItem().getName());
        reservationItem.setFromDate(reservation.getFrom());
        reservationItem.setToDate(reservation.getTo());
        reservationItem.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_BOOKING, reservationID)));
        return reservationItem;
    }

    @Override
    public Boolean isProjectNumberExists(String numberString, Integer projectID) {
        if (numberString == null || "".equals(numberString)) {
            return false;
        } else {
            return this.projectManager.isProjectNumberExists(numberString, projectID);
        }
    }

    @Override
    public Integer saveBookingItem(BookingItemsItem item) {
        EdsBookingItem newItems = new EdsBookingItem();
        boolean isNew = true;
        if (item.getObjectID() != null) {
            newItems = this.bookingItemManager.get(item.getObjectID());
            isNew = false;
        }
        newItems.setName(item.getItemName());
        newItems.setItemNumber(item.getItemNumber());
        newItems.setIntNumber(item.getNumberData().getIntNumber());
        newItems.setDescription(item.getDescription());

        if (item.getCategory() != null) {
            newItems.setCategory(this.referenceManager.get(item.getCategory().getId()));
        } else {
            newItems.setCategory(null);
        }
        if (item.getLocationID() != null) {
            newItems.setLocation(this.locationManager.get(item.getLocationID()));
        } else {
            newItems.setLocation(null);
        }
        newItems.setCustomFields(createBookingItemCustomFields(newItems.getCustomFields(), item.getCustomFieldItems()));
        this.bookingItemManager.createOrUpdate(newItems);
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsBookingItem.class.getSimpleName());
        if (newItems.getObjectID() != null) {
            kpiLog.setEntityId(newItems.getObjectID());
        }
        if (isNew) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Add booking item");
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Update booking item");
        }

        return newItems.getObjectID();
    }

    private EdsBookingItemCustomFields createBookingItemCustomFields(EdsBookingItemCustomFields edsCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            if (edsCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsCustomFields = new EdsBookingItemCustomFields();
                bookingItemCFManager.create(edsCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsCustomFields, customFieldItems);
            return edsCustomFields;
        }
        return null;
    }

    @Override
    public Integer saveBookingItemReservation(BookingReservationItem item) {
        EdsBookingItemReservation reservation = new EdsBookingItemReservation();

        if (item.getObjectID() != null) {
            reservation = this.bookingItemReservationManager.get(item.getObjectID());
        }

        if (item.getSelectedBookingItemId() != null) {
            reservation.setBookingItem(this.bookingItemManager.get(item.getSelectedBookingItemId().getId()));
        }
        if (item.getSelectedReservedById() != null) {
            reservation.setReservedBy(this.userManager.get(item.getSelectedReservedById().getId()));
        }
        reservation.setFrom(item.getFromDate());
        reservation.setTo(item.getToDate());
        Integer checkResult = this.validateBookingItemReservation(item);
        if (checkResult != null && checkResult < 1) {
            this.bookingItemReservationManager.createOrUpdate(reservation);
            if (item.isRelationChanged()) {
                this.allInOneService.saveRelations(RelationItem.TYPE_OPPORTUNITY, reservation.getObjectID(), reservation.getName(), item.getRelations());
            }
            if (reservation.getObjectID() != null) {
                KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsBookingItemReservation.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.ADD);
                kpiLog.setEntityId(reservation.getObjectID());
                ServerUtils.kpiLog(ProjectServiceImpl.log, kpiLog, "Add booking item reservation");
            }
            return reservation.getObjectID();
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer validateBookingItemReservation(BookingReservationItem item) {
        if (item.getSelectedBookingItemId() != null && item.getSelectedBookingItemId().getId() != null) {
            List<EdsBookingItemReservation> validationReservation = this.bookingItemReservationManager.getValidationReservation(item.getSelectedBookingItemId().getId(), item.getFromDate(), item.getToDate());
            if (validationReservation.size() > 0) {
                int i = 0;
                for (EdsBookingItemReservation reservation : validationReservation) {
                    if (item.getObjectID() == null || reservation.getObjectID() != item.getObjectID()) {
                        Date from = reservation.getFrom();
                        if (item.getFromDate().getTime() >= from.getTime() && item.getFromDate().getTime() < reservation.getTo().getTime()) {
                            i++;
                        } else if (item.getToDate().getTime() > from.getTime() && item.getToDate().getTime() <= reservation.getTo().getTime()) {
                            i++;
                        } else if (item.getFromDate().getTime() <= from.getTime() && item.getToDate().getTime() >= reservation.getTo().getTime()) {
                            i++;
                        }
                    }
                }
                return i;
            }
            return 0;
        } else {
            return 0;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<BookingReservationItem> getBookingItemsReservationHistory(Integer bookingItemId) {

        List<EdsBookingItemReservation> reservationByBookingid = this.bookingItemReservationManager.getReservationByBookingid(bookingItemId);
        ArrayList<BookingReservationItem> result = new ArrayList<>();
        for (EdsBookingItemReservation edsBookingItemReservation : reservationByBookingid) {
            BookingReservationItem items = new BookingReservationItem();
            items.setObjectID(edsBookingItemReservation.getObjectID());
            if (edsBookingItemReservation.getReservedBy() != null) {
                items.setSelectedReservedById(edsBookingItemReservation.getReservedBy().getAsSelectItem());
            }
            if (edsBookingItemReservation.getFrom() != null) {
                items.setFromDate(edsBookingItemReservation.getFrom());
            }
            if (edsBookingItemReservation.getFrom() != null) {
                items.setToDate(edsBookingItemReservation.getTo());
            }

            result.add(items);
        }

        return new ListResult<>(result, reservationByBookingid.size());
    }

    public ArrayList<BookingReservationItem> getBookingItemsReservationHistoryList(Integer bookingItemId) {

        List<EdsBookingItemReservation> reservationByBookingid = this.bookingItemReservationManager.getReservationByBookingid(bookingItemId);
        ArrayList<BookingReservationItem> result = new ArrayList<>();
        for (EdsBookingItemReservation edsBookingItemReservation : reservationByBookingid) {
            BookingReservationItem items = new BookingReservationItem();
            items.setObjectID(edsBookingItemReservation.getObjectID());
            if (edsBookingItemReservation.getReservedBy() != null) {
                items.setSelectedReservedById(edsBookingItemReservation.getReservedBy().getAsSelectItem());
            }
            if (edsBookingItemReservation.getFrom() != null) {
                items.setFromDate(edsBookingItemReservation.getFrom());
            }
            if (edsBookingItemReservation.getFrom() != null) {
                items.setToDate(edsBookingItemReservation.getTo());
            }

            result.add(items);
        }

        return result;
    }

    public NumberData generateBookingItemNumber() {
        EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
        Integer intNumber = this.bookingItemManager.getBookingItemLastIntNumber();
        if (settings != null && settings.getProductNumberingFormat() != null) {
            return settings.parseNumberData(intNumber, settings.getProductNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_BOOKING_ITEM_PREFIX);
        }
    }

    @Override
    public Date[] getProjectPeriod(Integer projectID) {
        EdsProject project = this.projectManager.get(projectID);
        Date startDate = project.getStartDate();
        Date endDate = project.getEndDate();
        if (startDate == null) {
            startDate = ServerUtils.getYearStartDate(Calendar.getInstance().get(Calendar.YEAR));
        }
        if (endDate == null) {
            endDate = ServerUtils.getYearEndDate(Calendar.getInstance().get(Calendar.YEAR));
        }
        return new Date[]{startDate, endDate};
    }

    @Override
    public NewProjectBudgetData getNewProjectBudgetData(Integer projectID, DateNonConvertable startDate, DateNonConvertable endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");

        EdsProject project = this.projectManager.get(projectID);

        boolean isDetailedPurchasesEnabled = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_BUDGET_DETAILED_PURCHASE_ENABLED);

        ArrayList<DateNonConvertable[]> monthIntervalsList = this.getMonthIntervalsList(startDate, endDate);

        NewProjectBudgetData budgetData = new NewProjectBudgetData();
        int compId = this.userManager.getUser().getCompany().getObjectID();
        if ((compId == 33012) || (compId == 33011) || (compId == 31733) || (compId == 36962)) {
            budgetData.setProjectName(project.getNumber() + ": " + project.getName());
        } else {
            budgetData.setProjectName(project.getName());
        }
        budgetData.setCustomerName(project.getClient() != null ? project.getClient().getName() : null);
        budgetData.setMonthIntervalList(monthIntervalsList);
        budgetData.setDetailedPurchasesEnabled(isDetailedPurchasesEnabled);

        NewProjectBudgetRowItem employeeCost = new NewProjectBudgetRowItem();
        employeeCost.setAccount(new SelectItem(-1, "Employee Cost"));
        budgetData.setEmployeeCost(employeeCost);

        NewProjectBudgetRowItem purchaseRowItem = new NewProjectBudgetRowItem();
        purchaseRowItem.setAccount(new SelectItem(-2, "Purchases"));
        budgetData.setPurchases(purchaseRowItem);


        BigDecimal totalEmployeeCostBudget = this.projectBudgetManager.getProjectEmployeeBudget(projectID);
        BigDecimal monthlyEmployeeCostBudget = monthIntervalsList.size() > 0 ? totalEmployeeCostBudget.divide(new BigDecimal(monthIntervalsList.size()), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        for (DateNonConvertable[] monthInterval : monthIntervalsList) {
            String monthKey = dateFormat.format(monthInterval[0].getNonConvertedDate());
            NewProjectBudgetCellItem empCostCellItem = new NewProjectBudgetCellItem();
            empCostCellItem.setBudget(monthlyEmployeeCostBudget);
            empCostCellItem.setActual(this.projectBudgetManager.getProjectEmployeeCostByMonth(projectID, monthInterval[0].getNonConvertedDate(), monthInterval[1].getNonConvertedDate()));
            employeeCost.getCellDataMap().put(monthKey, empCostCellItem);

            NewProjectBudgetCellItem purchaseCellItem = new NewProjectBudgetCellItem();
            purchaseCellItem.setBudget(BigDecimal.ZERO);
            purchaseCellItem.setActual(this.projectBudgetManager.getProjectPurchasesByMonth(projectID, monthInterval[0].getNonConvertedDate(), monthInterval[1].getNonConvertedDate()));
            purchaseRowItem.getCellDataMap().put(monthKey, purchaseCellItem);

        }
        NewProjectBudgetCellItem totalEmpCostCellItem = new NewProjectBudgetCellItem();
        totalEmpCostCellItem.setBudget(totalEmployeeCostBudget);
        employeeCost.getCellDataMap().put(Constants.TOTAL_BUDGET, totalEmpCostCellItem);

        EdsProjectBudget projectBudget = this.projectBudgetManager.getBudgetByProject(projectID);
        if (projectBudget != null) {
            HashMap<Integer, HashMap<String, EdsProjectBudgetItem>> budgetItemsAsMap = this.projectBudgetManager.getProjectBudgetItems(null, projectBudget, isDetailedPurchasesEnabled);
            List<EdsAccount> revenueBudgetAccounts = this.projectBudgetManager.getBudgetAccounts(projectBudget, EdsProjectBudgetItem.REVENUE);
            List<EdsAccount> expenseBudgetAccounts = this.projectBudgetManager.getBudgetAccounts(projectBudget, EdsProjectBudgetItem.EXPENSE);
            List<EdsAccount> assetBudgetAccounts = this.projectBudgetManager.getBudgetAccounts(projectBudget, EdsProjectBudgetItem.ASSET);
            List<EdsAccount> purchaseBudgetAccounts = this.projectBudgetManager.getBudgetAccounts(projectBudget, EdsProjectBudgetItem.PURCHASE);

            List<EdsAccount> expenseAssetBudgetAccounts = new ArrayList<>(expenseBudgetAccounts.size() + assetBudgetAccounts.size());
            expenseAssetBudgetAccounts.addAll(expenseBudgetAccounts);
            expenseAssetBudgetAccounts.addAll(assetBudgetAccounts);
            NewProjectBudgetRowItem[] revenueRowItems = new NewProjectBudgetRowItem[revenueBudgetAccounts.size()];
            NewProjectBudgetRowItem[] expenseRowItems = new NewProjectBudgetRowItem[expenseBudgetAccounts.size() + assetBudgetAccounts.size()];
            NewProjectBudgetRowItem[] purchaseRowItems = new NewProjectBudgetRowItem[purchaseBudgetAccounts.size()];

            for (DateNonConvertable[] monthInterval : monthIntervalsList) {
                String monthKey = dateFormat.format(monthInterval[0].getNonConvertedDate());
                HashMap<Integer, BigDecimal> revenueExpenseActuals = this.projectBudgetManager.getAccountsActualByProjectAndMonth(null, projectID, null, monthInterval[0].getNonConvertedDate(), monthInterval[1].getNonConvertedDate());
                this.setBudgetTableMonthlyColumnData(budgetItemsAsMap, revenueBudgetAccounts, revenueRowItems, monthKey, revenueExpenseActuals, EdsProjectBudgetItem.REVENUE);
                this.setBudgetTableMonthlyColumnData(budgetItemsAsMap, expenseAssetBudgetAccounts, expenseRowItems, monthKey, revenueExpenseActuals, EdsProjectBudgetItem.EXPENSE);

                if (isDetailedPurchasesEnabled) {
                    HashMap<Integer, BigDecimal> purchaseActuals = this.projectBudgetManager.getAccountsActualByProjectAndMonth(null, projectID, Constants.PURCHASES_STR, monthInterval[0].getNonConvertedDate(), monthInterval[1].getNonConvertedDate());
                    this.setBudgetTableMonthlyColumnData(budgetItemsAsMap, purchaseBudgetAccounts, purchaseRowItems, monthKey, purchaseActuals, EdsProjectBudgetItem.PURCHASE);
                }

                if (budgetItemsAsMap.get(-2) != null && budgetItemsAsMap.get(-2).get(monthKey) != null) {
                    purchaseRowItem.getCellDataMap().get(monthKey).setBudget(budgetItemsAsMap.get(-2).get(monthKey).getAmount());
                }
            }

            if (budgetItemsAsMap.get(-2) != null && budgetItemsAsMap.get(-2).get(Constants.TOTAL_BUDGET) != null) {
                NewProjectBudgetCellItem totalItem = new NewProjectBudgetCellItem();
                totalItem.setBudget(budgetItemsAsMap.get(-2).get(Constants.TOTAL_BUDGET).getAmount());
                purchaseRowItem.getCellDataMap().put(Constants.TOTAL_BUDGET, totalItem);
            }

            budgetData.setProjectID(projectID);
            budgetData.setRevenues(revenueRowItems);
            budgetData.setEmployeeCost(employeeCost);
            budgetData.setExpenses(expenseRowItems);
            budgetData.setDetailedPurchases(purchaseRowItems);
        }
        return budgetData;
    }

    private ArrayList<DateNonConvertable[]> getMonthIntervalsList(DateNonConvertable startDate, DateNonConvertable endDate) {
        Calendar startDateCal = new GregorianCalendar();
        startDateCal.setTime(startDate.getNonConvertedDate());

        Calendar endDateCal = new GregorianCalendar();
        endDateCal.setTime(endDate.getNonConvertedDate());

        startDateCal.set(Calendar.DATE, 1);
        endDateCal.set(Calendar.DATE, endDateCal.getActualMaximum(Calendar.DATE));
        ServerUtils.setBeginningOfTheDay(startDateCal);
        ServerUtils.setEndOfTheDay(endDateCal);

        ArrayList<DateNonConvertable[]> monthIntervalList = new ArrayList<>();

        while (startDateCal.getTime().before(endDateCal.getTime())) {
            GregorianCalendar monthEndCal = new GregorianCalendar();
            monthEndCal.setTime(startDateCal.getTime());
            monthEndCal.set(Calendar.DATE, monthEndCal.getActualMaximum(Calendar.DATE));
            ServerUtils.setEndOfTheDay(monthEndCal);

            DateNonConvertable[] dates = new DateNonConvertable[2];
            dates[0] = new DateNonConvertable(startDateCal.getTime());
            dates[1] = new DateNonConvertable(monthEndCal.getTime());
            monthIntervalList.add(dates);

            startDateCal.set(Calendar.MONTH, startDateCal.get(Calendar.MONTH) + 1);
        }

        return monthIntervalList;
    }

    private void setBudgetTableMonthlyColumnData(HashMap<Integer, HashMap<String, EdsProjectBudgetItem>> budgetItemsAsMap, List<EdsAccount> budgetAccounts,
                                                 NewProjectBudgetRowItem[] rowItems, String monthKey, HashMap<Integer, BigDecimal> monthlyActuals, String type) {
        int row = 0;
        for (EdsAccount acc : budgetAccounts) {
            HashMap<String, EdsProjectBudgetItem> budgetItemMap = budgetItemsAsMap.get(acc.getObjectID());
            if (rowItems[row] == null) {
                rowItems[row] = new NewProjectBudgetRowItem();
                rowItems[row].setAccount(acc.getAsSelectItem());

                if (budgetItemMap != null && budgetItemMap.get(Constants.TOTAL_BUDGET) != null) {
                    NewProjectBudgetCellItem totalItem = new NewProjectBudgetCellItem();
                    totalItem.setBudget(budgetItemMap.get(Constants.TOTAL_BUDGET).getAmount());
                    rowItems[row].getCellDataMap().put(Constants.TOTAL_BUDGET, totalItem);
                }
            }

            boolean isBudgetExist = (budgetItemMap != null && budgetItemMap.get(monthKey) != null);
            BigDecimal amount = monthlyActuals.get(acc.getObjectID());
            if (isBudgetExist || amount != null) {
                NewProjectBudgetCellItem cellItem = new NewProjectBudgetCellItem();
                if (isBudgetExist) {
                    cellItem.setBudget(budgetItemMap.get(monthKey).getAmount());
                }
                if (EdsProjectBudgetItem.REVENUE.equals(type) || EdsProjectBudgetItem.PURCHASE.equals(type)) {
                    cellItem.setActual(amount);
                } else {
                    if (amount != null) {
                        cellItem.setActual(amount);
                    }
                }
                rowItems[row].getCellDataMap().put(monthKey, cellItem);
            }
            row++;
        }
    }

    public NewProjectBudgetRowItem getProjectBudgetRowDataByAccount(Integer projectID, Integer accountID, ArrayList<DateNonConvertable[]> monthIntervalsList, String type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
        NewProjectBudgetRowItem rowItem = new NewProjectBudgetRowItem();

        for (DateNonConvertable[] monthInterval : monthIntervalsList) {
            String monthKey = dateFormat.format(monthInterval[0].getNonConvertedDate());
            HashMap<Integer, BigDecimal> monthlyActuals = this.projectBudgetManager.getAccountsActualByProjectAndMonth(accountID, projectID, type, monthInterval[0].getNonConvertedDate(), monthInterval[1].getNonConvertedDate());

            if (monthlyActuals.get(accountID) != null) {
                NewProjectBudgetCellItem cellItem = new NewProjectBudgetCellItem();
                cellItem.setActual((Constants.REVENUE.equals(type) || Constants.PURCHASES_STR.equals(type)) ? monthlyActuals.get(accountID) : BigDecimal.ZERO.subtract(monthlyActuals.get(accountID)));
                rowItem.getCellDataMap().put(monthKey, cellItem);
            }
        }
        return rowItem;
    }

    @Override
    public void saveProjectBudgetData(NewProjectBudgetData budgetData) {
        EdsProjectBudget projectBudget = this.projectBudgetManager.getBudgetByProject(budgetData.getProjectID());
        if (projectBudget == null) {
            projectBudget = new EdsProjectBudget();
            projectBudget.setProject(this.projectManager.get(budgetData.getProjectID()));
        } else {
            this.projectBudgetManager.deleteProjectBudgetItems(projectBudget.getObjectID());
        }

        LinkedList<EdsProjectBudgetItem> budgetItems = new LinkedList<>();

        this.addBudgetItems(budgetData.getRevenues(), budgetItems, projectBudget, EdsProjectBudgetItem.REVENUE);
        this.addBudgetItems(budgetData.getExpenses(), budgetItems, projectBudget, EdsProjectBudgetItem.EXPENSE);
        if (this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_BUDGET_DETAILED_PURCHASE_ENABLED)) {
            this.addBudgetItems(budgetData.getDetailedPurchases(), budgetItems, projectBudget, EdsProjectBudgetItem.PURCHASE);
        } else {
            this.addBudgetItems(new NewProjectBudgetRowItem[]{budgetData.getPurchases()}, budgetItems, projectBudget, EdsProjectBudgetItem.PURCHASE);
        }

        projectBudget.setItems(budgetItems);
        this.projectBudgetManager.createOrUpdate(projectBudget);
    }

    private void addBudgetItems(NewProjectBudgetRowItem[] rowItems, LinkedList<EdsProjectBudgetItem> budgetItems, EdsProjectBudget projectBudget, String type) {
        if (rowItems != null) {
            for (NewProjectBudgetRowItem ri : rowItems) {
                EdsAccount account = ri.getAccount().getId() != -1 ? this.accountingManager.get(ri.getAccount().getId()) : null;
                LinkedHashMap<String, NewProjectBudgetCellItem> cellDataMap = ri.getCellDataMap();
                if (cellDataMap != null && cellDataMap.size() > 0) {
                    Collection<NewProjectBudgetCellItem> cellDataList = cellDataMap.values();
                    for (NewProjectBudgetCellItem mi : cellDataList) {
                        EdsProjectBudgetItem pbi = new EdsProjectBudgetItem();
                        pbi.setAccount(account);
                        if (ri.getAccount().getId() == -1) {
                            pbi.setType(EdsProjectBudgetItem.EMPLOYEE_COST);
                        } else if (ri.getAccount().getId() == -2) {
                            pbi.setType(EdsProjectBudgetItem.PURCHASE);
                        } else {
                            pbi.setType(type);
                        }
                        pbi.setAmount(mi.getBudget());
                        pbi.setYear(mi.getYear());
                        pbi.setMonth(mi.getMonth());
                        pbi.setProjectBudget(projectBudget);
                        pbi.setTotal(mi.isTotal());
                        budgetItems.add(pbi);
                    }
                }
            }
        }
    }

    @Override
    public SelectItem[] getAccountsForProjectBudget(ListingFilterParameter filterParametrs) {
        List<EdsAccount> accounts = this.projectBudgetManager.getAccountsForProjectBudget(filterParametrs);
        SelectItem[] accountItems = new SelectItem[accounts.size()];
        int i = 0;
        for (EdsAccount acc : accounts) {
            accountItems[i++] = acc.getAsSelectItem();
        }
        return accountItems;
    }

    public ListResult<ProjectInvoice> getSaleQuoteList(HashMap<String, Object> paramMap) {
        if (paramMap == null || !paramMap.containsKey("projectId") || !paramMap.containsKey("fp")) {
            return null;
        }
        return this.getSaleQuoteList((Integer) paramMap.get("projectId"), (ListingFilterParameter) paramMap.get("fp"));
    }

    @Override
    public ListResult<ProjectInvoice> getSaleQuoteList(Integer projectId, ListingFilterParameter fp) {
        ListingObjectItem invoices = this.projectManager.getSaleQuoteList(projectId, fp);
        ListPanelToolRpc listPanelTool = fp.getListPanelTool();
        ArrayList<ProjectInvoice> result = new ArrayList<>();
        for (EdsBaseInvoice baseInvoice : (List<EdsSaleQuote>) invoices.getItems()) {
            EdsSaleQuote saleQuote = this.quoteManager.getSaleQuote(baseInvoice.getObjectID());
            ProjectInvoice items = new ProjectInvoice();
            items.setID(baseInvoice.getObjectID());
            items.setInvoiceNumber(baseInvoice.getNumber());
            items.setDueDate(new DateNonConvertable(baseInvoice.getDueDate()));
            items.setInvoiceDate(new DateNonConvertable(baseInvoice.getInvoiceDate()));
            items.setClientName(baseInvoice.getClientOrSupplier().getName());
            items.setCurrencyName(baseInvoice.getCurrency() != null ? (baseInvoice.getCurrency().getName() != null ? baseInvoice.getCurrency().getName() : "") : "");
            items.setStatus(this.referenceWfmMessageSource.localizeRef(baseInvoice.getStatus()));
            items.setTotal(baseInvoice.getTotal().doubleValue());
            items.setCreatorName(saleQuote.getCreator() != null ? saleQuote.getCreator().getName() : "");
            items.setManagerName(saleQuote.getCurrentApprover() != null
                    && saleQuote.getCurrentApprover().getExactEmployee() != null
                    ? saleQuote.getCurrentApprover().getExactEmployee().getName()
                    : "");
            items.setReference(saleQuote.getReference());
            items.setPoNumber(saleQuote.getPoNumber());
            if (saleQuote.getOpportunityID() != null) {
                EdsOpportunity opportunity = this.opportunityManager.get(saleQuote.getOpportunityID());
                if (opportunity != null) {
                    items.setOpportunity(opportunity.getNumber());
                }
            }
            items.setTotalTaxes(saleQuote.getTotalTaxes());
            items.setSubtotal(saleQuote.getSubtotal());
            items.setTotalInInvoiceCurrency(baseInvoice.getTotalInInvoiceCurrency() != null ? baseInvoice.getTotalInInvoiceCurrency() :
                    baseInvoice.getTotal().multiply(baseInvoice.getExchangeRate()));
            if (saleQuote.getCustomFields() != null && listPanelTool != null && listPanelTool.getColumnCodeName() != null) {
                items.setCustomFields(CustomFieldsUtils.getRPCCustomFields(saleQuote.getCustomFields(), listPanelTool.getColumnCodeName()));
            }
            result.add(items);
        }
        return new ListResult<>(result, invoices.getTotalCount());
    }

    public HashSet<String> getProjectSpecificPermissions(Integer projectID) {
        EdsUser user = this.employeeManager.getUser();
        EdsProject project = this.projectManager.get(projectID);
        return this.getProjectSpecificPermissions(user, project);
    }

    public HashSet<String> getProjectSpecificPermissions(EdsUser user, EdsProject project) {
        if (project.getManager() != null && user.getObjectID().equals(project.getManager().getObjectID())) {
            user.addArtificialRole(this.roleManager.getByCode(Constants.PMOFPR));
        }
        if (project.getBackupManager() != null && (user.getObjectID().equals(project.getBackupManager().getObjectID()) || project.getBackupManagerIDs().contains(user.getObjectID()))) {

            user.addArtificialRole(this.roleManager.getByCode(Constants.BMOFPR));
        }
        return this.rolePermissionServiceLocal.getPermissionList(PermissionConstants.PM_CONTEXT, user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem getCalendarEventById(Integer eventID) {
        EdsEvent edsEvent = this.eventManager.get(eventID);
        return new SelectItem(edsEvent.getObjectID(), edsEvent.getName());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getBookingItemsByCategoryId(Integer categoryId) {
        List<EdsBookingItem> bookingItems = this.bookingItemManager.getBookingItemListByCategoryId(categoryId);
        SelectItem[] result = new SelectItem[bookingItems.size()];
        int i = 0;
        for (EdsBookingItem item : bookingItems) {
            result[i] = new SelectItem();
            result[i].setId(item.getObjectID());
            result[i].setName(item.getName());
            i++;
        }
        return result;
    }

    @Override
    public void restartProectNumber() {
        EdsNumberingSettings restartDate = this.numberingSettingsManager.getNumberingSetting();
        EdsNumberingSettings numberingSettings = new EdsNumberingSettings();
        numberingSettings.setProjectLastIntNumber(restartDate.getProjectIntNumber());
        this.numberingSettingsManager.update(numberingSettings);
    }

    public ProjectFile exportToMSProject(Integer projectID) {
        HashMap<Integer, Task> predTasksMap = new HashMap<>();
        HashMap<Task, ArrayList<EdsTask>> succTasksMap = new HashMap<>();

        EdsUser user = this.userManager.getUser();
        EdsProject project = this.projectManager.get(projectID);
        ProjectFile file = new ProjectFile();
        file.setAutoTaskID(true);
        file.setAutoResourceUniqueID(true);
        file.setAutoOutlineLevel(true);
        file.setAutoOutlineNumber(true);
        file.setAutoWBS(true);
        file.setAutoCalendarUniqueID(true);

        EdsTimeSlot defaultTimeSlot = user.getCompany().getDefaultTimeSlot();

//        getting calendar
        ProjectCalendar pc = this.commonServiceLocal.createProjectCalendar(file, defaultTimeSlot, "Company default calendar");
        pc.setName(ProjectCalendar.DEFAULT_BASE_CALENDAR_NAME);
        file.setCalendar(pc);

        ProjectHeader header = file.getProjectHeader();
        header.setName(project.getName());
        header.setSubject(project.getDescription());
        header.setAuthor(user.getFullName());
        header.setCalendarName(pc.getName());
        header.setStartDate(user.getUserDate(project.getStartDate()));
//        header.setFinishDate(user.getUserDate(project.getDueDate()));
        header.setAdminProject(true);
        header.setAuthor(user.getFullName());
        header.setCompany(user.getCompany().getName());
        header.setDefaultTaskType(TaskType.FIXED_DURATION);
        header.setManager(project.getManager().getFullName());
        header.setShowProjectSummaryTask(true);

        HashMap<Integer, Resource> projectAssignees = new HashMap<>();
        // Creating project's workstreams
        List<EdsWorkStream> workStreams = this.workStreamManager.findOrphanWorkstreams(projectID);
        if (workStreams != null && !workStreams.isEmpty()) {
            for (EdsWorkStream workStream : workStreams) {
                this.createMSProjectWorkstream(file, user, workStream, projectAssignees, predTasksMap, succTasksMap);
            }
        }
        // Creating project's tasks
        List<EdsTask> projectTasks = this.taskManager.getProjectTasksOrderByDate(project);
        if (projectTasks != null && !projectTasks.isEmpty()) {
            for (EdsTask task : projectTasks) {
                if ((task.getIssue() == null || task.getIssue() != null && !task.getIssue()) && task.getParentWS() == null) {
                    this.createMSProjectTask(file, null, user, task, projectAssignees, predTasksMap, succTasksMap);
                }
            }
        }
        // link pred/succ tasks
        if (!succTasksMap.isEmpty()) {
            for (Task task : succTasksMap.keySet()) {
                ArrayList<EdsTask> predTasks = succTasksMap.get(task);
                for (EdsTask predTask : predTasks) {
                    task.addPredecessor(predTasksMap.get(predTask.getObjectID()), RelationType.FINISH_START, null);
                }
            }
        }
        if (projectTasks == null || projectTasks.isEmpty()) {
            Resource resource = file.addResource();
            resource.setName(project.getManager().getFullName());
        }
        return file;
    }

    private void createMSProjectWorkstream(ProjectFile file, EdsUser user, EdsWorkStream wftWorkStream, HashMap<Integer, Resource> projectAssignees, HashMap<Integer, Task> predTasksMap, HashMap<Task, ArrayList<EdsTask>> succTasksMap) {
        Task wsTask = file.addTask();
        wsTask.setName(wftWorkStream.getName());
        wsTask.setActualStart(user.getUserDate(wftWorkStream.getStartDate()));
        wsTask.setActualFinish(user.getUserDate(wftWorkStream.getEndDate()));
        wsTask.setNotes(wftWorkStream.getDescription());
        wsTask.setPercentageComplete(wftWorkStream.getPercent());
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setWorkstreamID(wftWorkStream.getObjectID());
        List<EdsTask> workStreamTasks = this.taskManager.getOrderByTask(filterParameter);
        if (workStreamTasks != null && !workStreamTasks.isEmpty()) {
            for (EdsTask task : workStreamTasks) {
                this.createMSProjectTask(file, wsTask, user, task, projectAssignees, predTasksMap, succTasksMap);
            }
        }
        Set<EdsWorkStream> subWorkStreams = wftWorkStream.getSubWorkStreams();
        if (subWorkStreams != null && !subWorkStreams.isEmpty()) {
            for (EdsWorkStream subWorkStream : subWorkStreams) {
                this.createMSProjectWorkstream(file, user, subWorkStream, projectAssignees, predTasksMap, succTasksMap);
            }
        }
    }

    private void createMSProjectTask(ProjectFile file, Task workStream, EdsUser user, EdsTask edsTask, HashMap<Integer, Resource> projectAssignees, HashMap<Integer, Task> predTasksMap, HashMap<Task, ArrayList<EdsTask>> succTasksMap) {
        Task mspTask = workStream != null ? workStream.addTask() : file.addTask();
        mspTask.setName(edsTask.getName());
        mspTask.setUniqueID(edsTask.getObjectID());
        mspTask.setNotes(edsTask.getDescription());
        mspTask.setCalendar(file.getCalendar());
        mspTask.setEstimated(edsTask.isCalculated());
        if (edsTask.getStartDate() != null) {
            mspTask.setActualStart(user.getUserDate(edsTask.getStartDate()));
        }
        if (edsTask.getDueDate() != null) {
            mspTask.setActualFinish(user.getUserDate(edsTask.getDueDate()));
        }
        long workTime = 0;
        if (edsTask.getDueDate() != null && edsTask.getStartDate() != null) {
            workTime = (edsTask.getDueDate().getTime() - edsTask.getStartDate().getTime()) / (1000 * 60 * 60);
        }
        mspTask.setDuration(workTime >= 24 ? Duration.getInstance((double) workTime / 24 + 1, TimeUnit.DAYS) : Duration.getInstance(workTime, TimeUnit.HOURS));
        mspTask.setPercentageComplete(edsTask.getPercent() != null ? edsTask.getPercent() : Float.valueOf("0.0"));
        mspTask.setCalendar(file.getCalendar());
        Set<EdsEmployeeTask> assignments = edsTask.getUnDeletedAssignments();
        if (assignments != null && !assignments.isEmpty()) {
            for (EdsEmployeeTask assignee : assignments) {
                EdsEmployee employee = assignee.getProjectEmployee().getEmployeeDepartment().getEmployee();
                if (!projectAssignees.containsKey(employee.getObjectID())) {
                    Resource resource = file.addResource();
                    resource.setName(employee.getFullName());
                    projectAssignees.put(employee.getObjectID(), resource);
                }
                // Assign resources to task
                Resource resource = projectAssignees.get(employee.getObjectID());
                resource.setName(employee.getFullName());
                mspTask.addResourceAssignment(resource);
            }
        }
        if (edsTask.getSuccessors() != null && !edsTask.getSuccessors().isEmpty()) {
            predTasksMap.put(edsTask.getObjectID(), mspTask);
        }
        if (edsTask.getPredecessors() != null && !edsTask.getPredecessors().isEmpty()) {
            ArrayList<EdsTask> preds = new ArrayList<>();
            for (EdsTask predTask : edsTask.getPredecessors()) {
                if (predTasksMap.containsKey(predTask.getObjectID())) {
                    mspTask.addPredecessor(predTasksMap.get(predTask.getObjectID()), RelationType.FINISH_START, null);
                } else {
                    preds.add(predTask);
                    succTasksMap.put(mspTask, preds);
                }
            }
        }
    }

    public void deleteReservation(Integer attachmentId) {
        EdsBookingItemReservation reservation = this.bookingItemReservationManager.get(attachmentId);
        this.bookingItemReservationManager.deleteReservation(reservation);
    }

    public void updateProjectStatus(HashSet<ProjectListItem> projectListItems, SelectItem status, boolean changeTaskStatuses) {
        EdsUser user = this.projectManager.getUser();
        List<EdsProject> projects = new ArrayList<>();
        for (ProjectListItem projectListItem : projectListItems) {
            EdsProject project = this.projectManager.get(projectListItem.getObjectId());
            if (project != null) {
                EdsReference pStatus = this.referenceManager.get(status.getId());
                project.setStatus(pStatus);
                if (pStatus != null && pStatus.getCode().equals(EdsProject.COMPLETED)) {
                    project.setCompletedDate(new Date());
                } else {
                    project.setCompletedDate(null);
                }
                this.updateProjectStatus(project);
                if (changeTaskStatuses) {
                    this.baseEventPostProcessor.registerEvent(ProjectStatusEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
                }
            }
            projects.add(project);
        }
        try {
            projectSolrComponent.indexes(projects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEmployeePreviousConstractPeriodByNew(Integer employeeId, Date contractStart, Date contractEnd, Integer projectID) {
        List<ProjectMember> projectMembers = this.projectEmployeeManager.getProjectEmployeesByContract(employeeId, contractStart, contractEnd, projectID);
        HashMap<Integer, ArrayList<Integer>> memForDelete = new HashMap<>();

        if (projectMembers != null && !projectMembers.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            EdsUser user = this.projectEmployeeManager.getUser();

            for (ProjectMember projectMember : projectMembers) {
                EdsProjectEmployee projectEmployee = this.projectEmployeeManager.get(projectMember.getProjectEmployeeId());

                if (projectMember.getContractStart().getNonConvertedDate().compareTo(contractStart) < 0
                        && (projectMember.getContractEnd() == null || projectMember.getContractEnd().getNonConvertedDate().compareTo(contractStart) >= 0)) {
                    cal.setTime(contractStart);
                    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
                    projectEmployee.setContractEndDate((Date) cal.getTime().clone());
                } else if (contractStart.compareTo(projectMember.getContractStart().getNonConvertedDate()) <= 0 && contractEnd.compareTo(projectMember.getContractStart().getNonConvertedDate()) >= 0) {
                    if (projectMember.getContractEnd() == null || projectMember.getContractEnd().getNonConvertedDate().compareTo(contractEnd) > 0) {
                        cal.setTime(contractEnd);
                        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
                        projectEmployee.setContractStartDate((Date) cal.getTime().clone());
                    } else {
                        projectEmployee.setDeleted(true);
                        this.baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, projectEmployee, user);
                        this.deleteTaskAssignees(projectEmployee);
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<RejectedImportRecord[]> importProjects(ImportFile importFile, List<String[]> data) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;
        EdsProject edsProject = null;

        Integer FIELD_NUMBER = importFile.getColumnID(ImportField.ProjectFields.FIELD_NUMBER);
        Integer FIELD_NAME = importFile.getColumnID(ImportField.ProjectFields.FIELD_NAME);
        Integer FIELD_DESCRIPTION = importFile.getColumnID(ImportField.ProjectFields.FIELD_DESCRIPTION);
        Integer FIELD_START_DATE = importFile.getColumnID(ImportField.ProjectFields.FIELD_START_DATE);
        Integer FIELD_DUE_DATE = importFile.getColumnID(ImportField.ProjectFields.FIELD_DUE_DATE);
        Integer FIELD_CLIENT = importFile.getColumnID(ImportField.ProjectFields.FIELD_CLIENT);
        Integer FIELD_MANAGER = importFile.getColumnID(ImportField.ProjectFields.FIELD_MANAGER);
        Integer FIELD_STATUS = importFile.getColumnID(ImportField.ProjectFields.FIELD_STATUS);
        Integer FIELD_ASSIGNEES = importFile.getColumnID(ImportField.ProjectFields.FIELD_ASSIGNEE);

        boolean hasHeader = importFile.isHasHeader();
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_PATTERN);
        formatter.setLenient(false);

        Map<String, EdsEmployee> employeeMap = Maps.newHashMap();
        Map<String, EdsCrmAccount> customerMap = Maps.newHashMap();
        EdsReference notStartedStatus = this.referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
        List<EdsReference> statusList = this.referenceManager.listReferences(EdsProject.PROJECT_STATUS, false);
        HashMap<String, Integer> statusMap = new HashMap<>();
        for (EdsReference rf : statusList) {
            statusMap.put(rf.getName().toLowerCase().trim(), rf.getObjectID());
        }

        for (String[] row : data) {
            boolean isUpdate = false;
            rejectedRow = new RejectedImportRecord[row.length];
            boolean isValid = true;

            boolean rowIsEmpty = true;
            Integer rowID = 0;
            for (String str : row) {
                rejectedRow[rowID++] = new RejectedImportRecord(str);
                if (rowIsEmpty && StringUtils.isNotBlank(str)) {
                    rowIsEmpty = false;
                }
            }
            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
                continue;
            }
            if (rowIsEmpty) {
                continue;
            }

            ProjectSingleItem project = new ProjectSingleItem();
            if (FIELD_NUMBER > -1 && row != null && StringUtils.isNotBlank(row[FIELD_NUMBER])) {
                edsProject = this.projectManager.getProjectByNumber(row[FIELD_NUMBER].trim());
                if (edsProject != null) {
                    isUpdate = true;

                }
            }

            ArrayList<CompanyCustomFieldItem> customFields = new ArrayList<>();
            int columnID = 0;

            HashMap<Integer, ProjectMember> projectAssigneesMap = new HashMap<>();

            for (String columnValue : row) {

                if (FIELD_NAME.equals(columnID)) {
                    if (StringUtils.isNotBlank(columnValue)) {
                        project.setName(columnValue.trim());
                    } else {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                        isValid = false;
                    }
                }

                if (FIELD_START_DATE.equals(columnID)) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                        isValid = false;
                    } else {
                        Date startDate = null;
                        try {
                            startDate = formatter.parse(columnValue);
                        } catch (ParseException e) {
                            rejectedRow[columnID].setErrorComment(this.commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        project.setStartDate(startDate);
                    }
                }

                if (FIELD_DUE_DATE.equals(columnID)) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                        isValid = false;
                    } else {
                        Date dueDate = null;
                        try {
                            dueDate = formatter.parse(columnValue);
                        } catch (Exception e) {
                            rejectedRow[columnID].setErrorComment(this.commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        project.setEndDate(dueDate);
                    }
                }

                if (FIELD_MANAGER.equals(columnID)) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                        isValid = false;
                    } else {
                        EdsEmployee manager = StringUtils.isNotBlank(columnValue) ? employeeMap.get(columnValue.toLowerCase()) : null;
                        if (manager == null) {
                            manager = StringUtils.isNotBlank(columnValue) ? this.employeeManager.getEmployeeByNumber(columnValue.toLowerCase()) : null;
                            if (manager != null) {
                                employeeMap.put(columnValue.toLowerCase(), manager);
                            } else {
                                rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                isValid = false;
                            }
                        }
                        project.setManagerId(manager != null ? manager.getObjectID() : null);
                    }
                }

                if (FIELD_ASSIGNEES.equals(columnID)) {
                    if (StringUtils.isBlank(columnValue)) {
                        if (project.getManagerId() == null) {
                            rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                            isValid = false;
                        }
                    } else {
                        String[] members = columnValue.toLowerCase().split(Constants.MULTIVALUE_SEPARATOR);
                        for (String member1 : members) {
                            EdsEmployee member = employeeMap.get(member1);
                            if (member == null) {
                                member = this.employeeManager.getEmployeeByNumber(member1.trim());
                                if (member != null) {
                                    employeeMap.put(member1, member);
                                }
                            }
                            if (member != null) {
                                EdsProjectEmployee projectEmployee = null;
                                if (isUpdate) {
                                    projectEmployee = this.projectEmployeeManager.getProjectEmployee(member, edsProject);
                                }
                                ProjectMember projectMember = new ProjectMember();
                                projectMember.setId(member.getObjectID());
                                projectMember.setProjectEmployeeId(projectEmployee != null ? projectEmployee.getObjectID() : null);
                                projectMember.setDepartmentId(member.getEmployeeDepartment() != null ? member.getEmployeeDepartment().getTeam().getObjectID() : null);
                                projectMember.setWageRate(member.getWageRate());
                                projectMember.setClientChargeRate(member.getClientChargeRate());
                                projectAssigneesMap.put(member.getObjectID(), projectMember);
                            }
                        }
                        if (projectAssigneesMap.isEmpty()) {
                            rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            isValid = false;
                        }
                    }
                }

                if (StringUtils.isNotBlank(columnValue)) {
                    columnValue = columnValue.trim();

                    if (FIELD_NUMBER.equals(columnID)) {
                        String projectNumber = columnValue;
                        boolean hasPrefix = false;
                        if (columnValue.contains("&")) {
                            columnValue = columnValue.replace("&", "");
                            hasPrefix = true;
                        }
                        if (!isUpdate && this.projectManager.isProjectNumberExists(columnValue, null)) {
                            break;
                        }
                        NumberData numberData = new NumberData();
                        if (hasPrefix) {
                            numberData.setNumberString(columnValue);
                            String[] projectNumbers = projectNumber.split("&");
                            if (projectNumbers != null && projectNumbers.length > 0) {
                                String intNumber = projectNumbers[projectNumbers.length - 1];
                                if (intNumber != null && intNumber.matches(Constants.REGEX_INTEGER_POSITIVE)) {
                                    try {
                                        numberData.setIntNumber(Integer.valueOf(intNumber));
                                    } catch (NumberFormatException e) {
                                        System.out.print(e.getMessage());
                                    }
                                }
                            }
                        } else {
//                            numberData = generateProjectNumber();
                            numberData.setNumberString(columnValue.trim());
                            if (columnValue.matches(Constants.REGEX_INTEGER_POSITIVE)) {
                                try {
                                    numberData.setIntNumber(Integer.valueOf(columnValue));
                                } catch (NumberFormatException e) {
                                    System.out.print(e.getMessage());
                                }
                            }
                        }
                        project.setNumberData(numberData);
                    }
                    if (FIELD_DESCRIPTION.equals(columnID)) {
                        project.setDescription(columnValue);
                    }

                    if (FIELD_CLIENT.equals(columnID)) {
                        EdsCrmAccount account = customerMap.get(columnValue);
                        if (account == null) {
                            List<EdsCrmAccount> clients = this.clientManager.getClientByName(columnValue);
                            if (!clients.isEmpty()) {
                                account = clients.get(0);
                                customerMap.put(columnValue, account);
                            } else {
                                rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                isValid = false;
                            }
                        }
                        if (account != null) {
                            project.setClientId(account.getObjectID());
                        }
                    }

                    if (FIELD_STATUS.equals(columnID)) {
                        Integer statusID = statusMap.get(columnValue.toLowerCase());
                        if (statusID != null) {
                            project.setStatusId(statusID);
                        } else {
                            rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            isValid = false;
                        }
                    }

                    if (importFile.getExtraColumns() != null && importFile.getExtraColumns().size() > 0) {
                        for (Map.Entry<Integer, String> extraColumnEntry : importFile.getExtraColumns().entrySet()) {
                            if (!importFile.getExtraColumnID(extraColumnEntry.getValue()).equals(columnID) || extraColumnEntry.getKey() < ImportField.ProjectFields.FIELD_CUSTOM_FIELD_START_NUMBER) {
                                continue;
                            }
                            CompanyCustomFieldItem customField = this.commonServiceLocal.getValidCustomFieldItem(extraColumnEntry, columnID, columnValue, rejectedRow, rejectedRecords.get(0)[columnID].getData());

                            if (customField == null) {
                                isValid = false;
                                break;
                            }

                            customFields.add(customField);
                        }
                    }
                }
                columnID++;
            }

            if (project.getNumberData() == null) {
                project.setNumberData(this.generateProjectNumber(new Date(), null, null));
            }
            Integer managerID = project.getManagerId();
            if (managerID != null && managerID > 0) {
                EdsEmployee mngr = this.employeeManager.get(managerID);
                ProjectMember projectManager = new ProjectMember();
                projectManager.setId(managerID);
                EdsProjectEmployee projectEmployee = null;
                if (isUpdate) {
                    projectEmployee = this.projectEmployeeManager.getProjectEmployee(mngr, edsProject);
                }
                projectManager.setProjectEmployeeId(projectEmployee != null ? projectEmployee.getObjectID() : null);
                projectManager.setDepartmentId(mngr.getEmployeeDepartment() != null ? mngr.getEmployeeDepartment().getTeam().getObjectID() : null);
                projectManager.setWageRate(mngr.getWageRate());
                projectManager.setClientChargeRate(mngr.getClientChargeRate());
                projectAssigneesMap.put(managerID, projectManager);
            }
            project.setProjectMembers(projectAssigneesMap.values().toArray(new ProjectMember[]{}));

            if (projectAssigneesMap.size() < 1 && StringUtils.isBlank(rejectedRow[FIELD_ASSIGNEES].getComment())) {
                rejectedRow[FIELD_ASSIGNEES].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Assignees"));
                isValid = false;
            }

            if (isValid) {
                project.setCustomFieldItems(customFields);

                if (project.getStatusId() == 0) {
                    project.setStatusId(notStartedStatus.getObjectID());
                }

                try {
                    if (isUpdate) {
                        this.updateProject(this.wrap(project, edsProject.getObjectID()));
                    } else {
                        this.saveProject(project);
                    }
                } catch (NumberExistingException e) {
                    e.printStackTrace();
                }

                importFile.setImportedColumns(importFile.getImportedColumns() + 1);
                if (isUpdate) {
                    importFile.setOverwrittenColumns(importFile.getOverwrittenColumns() + 1);
                } else {
                    importFile.setNewColumns(importFile.getNewColumns() + 1);
                }

                this.projectManager.flushAndClear();
            } else {
                rejectedRecords.add(rejectedRow);
                importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
            }

            hasHeader = false;
        }
        return rejectedRecords;
    }

    private EditProject wrap(ProjectSingleItem project, Integer objectID) {
        EditProject editProject = new EditProject();
        editProject.setObjectId(objectID);
        editProject.setName(project.getName());
        editProject.setNumberData(project.getNumberData());
        editProject.setDescription(project.getDescription());
        editProject.setStartDate(project.getStartDate());
        editProject.setDueDate(project.getEndDate());
        editProject.setClientId(project.getClientId());
        editProject.setStatusId(project.getStatusId());
        editProject.setManagerId(project.getManagerId());
        editProject.setMembers(project.getProjectMembers());
        editProject.setCustomFieldItems(project.getCustomFields());
        return editProject;
    }

    @Override
    public TaskTimeEntriesItem[] getProjectTimesheets(Integer projectID) {
        List<TaskTimeEntriesItem> list = this.timeSheetManager.getProjectTimeEntiries(projectID);

        if (list != null && !list.isEmpty()) {
            return list.toArray(new TaskTimeEntriesItem[]{});
        }
        return new TaskTimeEntriesItem[0];
    }

    @Override
    public SelectItem getProjectAsLookupItem(Integer id) {
        EdsProject project = this.projectManager.get(id);
        return new SelectItem(project.getObjectID(), (project.getNumber() != null ? project.getNumber() + " - " : "") + project.getName());
    }

    @Override
    public HashSet<SelectItem> getManagers() {
        HashSet<SelectItem> items = new HashSet<>();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setRoles(Constants.PM_CODE);
        fp.setResignedEmployeesIncluded(false);
        fp.setSortField(EmployeeListItem.FIRST_NAME);
        fp.setLimit(200);
        ListResult<EmployeeListItem> managers = this.employeeServiceLocal.getEmployeeList(fp);
        if (managers == null || managers.getList() == null || managers.getList().size() == 0) {
            return new HashSet<>();
        }
        managers.getList().stream()
                .filter(item -> !Constants.EMPLOYEE_STATUS_RESIGNED.equals(item.getStatusCode()))
                .forEach(item -> items.add(new SelectItem(item.getObjectID(), item.getFullName())));
        return items;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BillOfMaterialItem[] fillBillOfMaterialItemsWithInventory(Integer projectID) {
        EdsProject project = this.projectManager.get(projectID);
        if (project.getBillOfMaterials() != null && project.getBillOfMaterials().size() > 0) {
            String itemStatus = project.getBillOfMaterialStatus() != null ? project.getBillOfMaterialStatus().getName() : null;
            BillOfMaterialItem[] items = new BillOfMaterialItem[project.getBillOfMaterials().size()];
            int index = 0;

            Map<Integer, BigDecimal> requestedQtys = this.rfpManager.getRequestedRFPItems(project.getBillOfMaterials().stream()
                    .map(EdsBillOfMaterial::getObjectID)
                    .collect(Collectors.toList()));

            for (EdsBillOfMaterial it : project.getBillOfMaterials()) {
                items[index] = new BillOfMaterialItem();
                items[index].setObjectID(it.getObjectID());
                items[index].setItemID(it.getItem() != null ? it.getItem().getObjectID() : null);
                items[index].setItemName(it.getItem() != null ? it.getItem().getName() : it.getItemName());
                items[index].setItemNumber(it.getItem() != null ? it.getItem().getProductNumber() : "");
                items[index].setOnHand(it.getItem() != null && it.getItem().getQty() != null ? it.getItem().getQty() : BigDecimal.ZERO);
                items[index].setQty(it.getQty());
                if (requestedQtys != null && !requestedQtys.isEmpty()) {
                    items[index].setRequestedQqty(requestedQtys.get(it.getObjectID()));
                }
                items[index].setDescription(it.getDescription());
                items[index].setPrice(it.getPrice());
                if (it.getUnitMeasurement() != null) {
                    items[index].setUnitMeasurement(it.getUnitMeasurement().getAsSelectItem());
                }
                items[index].setSupplierID(it.getSupplierID());
                items[index].setStatus("Submitted".equals(itemStatus) ? "Submitted for approval" : itemStatus);
                index++;
            }
            return items;
        } else {
            ListingFilterParameter fp = new ListingFilterParameter();
//            fp.setShowOnOpportunity(true);
            List<EdsItem> selectItems = this.itemManager.getItems(fp);
            if (selectItems != null && !selectItems.isEmpty()) {
                BillOfMaterialItem[] items = new BillOfMaterialItem[selectItems.size()];
                int index = 0;
                for (EdsItem it : selectItems) {
                    items[index] = new BillOfMaterialItem();
                    items[index].setItemID(it.getObjectID());
                    items[index].setItemName(it.getName());
                    items[index].setItemNumber(it.getProductNumber());
                    items[index].setQty(it.getQty());
                    items[index].setDescription(it.getDescription());
                    items[index].setPrice(it.getUnitPrice());
                    if (it.getUnitMeasurement() != null) {
                        items[index].setUnitMeasurement(it.getUnitMeasurement().getAsSelectItem());
                    }
                    index++;
                }
                return items;
            }
        }
        return new BillOfMaterialItem[0];
    }

    @Override
    public Integer saveBillOfMaterialItems(Integer projectID, String status, BillOfMaterialItem[] items, String message) {
        EdsProject project = this.projectManager.get(projectID);

        EdsReference reference = this.referenceManager.getByCode(status);
        project.setBillOfMaterialStatus(reference);
        if (Constants.REJECTED.equals(status)) {
            project.setRejectionReason(message);
        }

        Map<Integer, EdsBillOfMaterial> materialMap = this.projectManager.getBomAsMap(projectID);

        List<Integer> deletedIds = new ArrayList<>(materialMap.keySet());

        if (items != null && items.length > 0) {
            int i = 0;
            for (BillOfMaterialItem item : items) {

                EdsBillOfMaterial billOfMaterial = null;

                if (item.getObjectID() != null) {
                    deletedIds.remove(item.getObjectID());

                    billOfMaterial = materialMap.get(item.getObjectID());
                }

                if (billOfMaterial == null) {
                    billOfMaterial = new EdsBillOfMaterial();
                }

                billOfMaterial.setProject(project);
                if (item.getItemID() != null) {
                    billOfMaterial.setItem(this.itemManager.get(item.getItemID()));
                }
                billOfMaterial.setItemName(item.getItemName());
                billOfMaterial.setDescription(item.getDescription());
                billOfMaterial.setQty(item.getQty());
                billOfMaterial.setPrice(item.getPrice());
                if (item.getUnitMeasurement() != null && item.getUnitMeasurement().getId() != null) {
                    billOfMaterial.setUnitMeasurement(this.unitMeasurementManager.get(item.getUnitMeasurement().getId()));
                }
                billOfMaterial.setSorder(i++);
                project.getBillOfMaterials().add(billOfMaterial);
            }

            if (deletedIds.size() > 0) {
                this.projectManager.deleteBillOfItems(deletedIds);
            }
        }
        EdsBusinessEvent event = this.baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, ProjectEventListenerImpl.BILL_OF_MATERIALS, project, this.userManager.getUser());
        event.setCustomStringField(status);
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String[] getBillOfItemsStatus(Integer projectID) {
        String[] name = new String[2];
        EdsProject project = this.projectManager.get(projectID);
        EdsReference ref = project.getBillOfMaterialStatus();
        name[0] = ref != null ? ref.getCode() : Constants.DRAFT;
        name[1] = (StringUtils.isNotBlank(project.getNumber()) ? project.getNumber() + " - " : "") + project.getName();
        return name;
    }

    @Override
    public String unfreezeBOM(Integer projectID) {
        EdsProject project = this.projectManager.get(projectID);
        EdsReference reference = this.referenceManager.getByCode(Constants.DRAFT);
        project.setBillOfMaterialStatus(reference);
        this.projectManager.update(project);
        return Constants.DRAFT;
    }

    @Override
    public Integer saveRequestedBillOfMaterial(Integer projectID, ArrayList<BillOfMaterialItem> items) {
        EdsUser user = this.userManager.getUser();

        String key = CacheConstants.REQUESTED_BILL_OF_MATERIALS + "_" + user.getCompany().getObjectID() + "_" + projectID + "_" + user.getObjectID();
//        Map<Integer, BigDecimal> map = ApplicationCache.getInstance().getMap(key + CacheConstants.REQUESTED_BILL_OF_MATERIALS);
//        map.clear();
        RedisClient.removeKey(key);

        if (items != null && !items.isEmpty()) {
//            map.putAll(items.stream()
//                    .collect(Collectors.toMap(BillOfMaterialItem::getObjectID, BillOfMaterialItem::getRequestedQqty)));
            Map<Integer, BigDecimal> map = items.stream()
                    .collect(Collectors.toMap(BillOfMaterialItem::getObjectID, BillOfMaterialItem::getRequestedQqty));

            RedisClient.setKey(key, map, map.getClass());
        }
        return Constants.SUCCESS;
    }

    @Override
    @Transactional
    public ArrayList<SelectItem> getReferenceByCode(String referenceCode) {
        ArrayList<SelectItem> names = new ArrayList<>();
        if (referenceCode != null && !referenceCode.isEmpty()) {
            List<String> nameList = referenceManager.getFieldNamesByCode(referenceCode);
            for (String name : nameList) {
                SelectItem selectItem = new SelectItem();
                selectItem.setName(name);
                names.add(selectItem);
            }
        }
        return names;
    }

    @Override
    public List<NearbyProjectDto> getNearbyProjects(Double latitude, Double longitude, Integer radius, List<Integer> assigneeIds, List<String> statusCodes, List<String> excludeStatusCodes) {
        return projectManager.getNearbyProjects(latitude, longitude, radius, assigneeIds, statusCodes, excludeStatusCodes);
    }
}
