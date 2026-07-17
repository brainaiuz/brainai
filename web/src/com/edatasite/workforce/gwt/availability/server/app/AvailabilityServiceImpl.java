package com.edatasite.workforce.gwt.availability.server.app;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsAnnualLeaveAllowance;
import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsAttendanceTerminal;
import com.edatasite.workforce.core.domain.EdsBackupEmployee;
import com.edatasite.workforce.core.domain.EdsBenefit;
import com.edatasite.workforce.core.domain.EdsBenefitRequest;
import com.edatasite.workforce.core.domain.EdsBrigada;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsDate;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsDynamicQuery;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeBenefitAllowance;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.core.domain.EdsHolidayHistory;
import com.edatasite.workforce.core.domain.EdsLabourPeriod;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsLeaveReasonHistory;
import com.edatasite.workforce.core.domain.EdsLeaveReasonRelation;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsMultiLeave;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsShiftSettings;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsSickRequestComment;
import com.edatasite.workforce.core.domain.EdsSickRequestDuration;
import com.edatasite.workforce.core.domain.EdsSickRequestForPeriod;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotHistory;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrint;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrintAdjustment;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.customfields.BenefitRequestCFManager;
import com.edatasite.workforce.core.domain.customfields.EdsBenefitRequestCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsSickRequestCustomFields;
import com.edatasite.workforce.core.domain.enums.FingerprintSource;
import com.edatasite.workforce.core.domain.goal.EdsGoalHistory;
import com.edatasite.workforce.core.domain.settings.EdsHRSettings;
import com.edatasite.workforce.core.solr.component.LeaveRequestSolrComponent;
import com.edatasite.workforce.core.solr.document.LeaveRequestSolrDoc;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.availability.client.rpc.AttendanceMarkListItem;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeLeaveStatusListItem;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.availability.client.rpc.FingerprintTimeDto;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayHistoryList;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.availability.client.rpc.InOutPairDto;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveBalanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestComment;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveSettingsItem;
import com.edatasite.workforce.gwt.availability.client.rpc.NewLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.TeammatesAvailability;
import com.edatasite.workforce.gwt.availability.client.rpc.TimeSlot;
import com.edatasite.workforce.gwt.availability.client.rpc.TimeSlotHistoryList;
import com.edatasite.workforce.gwt.availability.client.rpc.TimeslotSetting;
import com.edatasite.workforce.gwt.availability.server.enums.GoalCategoryEnum;
import com.edatasite.workforce.gwt.availability.server.pojo.Holiday;
import com.edatasite.workforce.gwt.availability.server.pojo.HolidayIndicator;
import com.edatasite.workforce.gwt.contact.client.rpc.AnnualLeaveItem;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.enums.AttendanceHoursType;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.LeaveReasonType;
import com.edatasite.workforce.gwt.core.client.enums.TypeOption;
import com.edatasite.workforce.gwt.core.client.enums.UnitType;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CalendarItemRpc;
import com.edatasite.workforce.gwt.core.client.rpc.CalendarItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.Departments;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeProjectsListItem;
import com.edatasite.workforce.gwt.core.client.rpc.ExceptionalTimeSlotItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.LRSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ShiftSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.InOutItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.LeaveRequestChartRpc;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.laborPeriod.MultiLeaveDTO;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.AnnualLeaveAllowanceManager;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.AttendanceTerminalManager;
import com.edatasite.workforce.gwt.core.server.db.BackupEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.db.BrigadaManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.DateManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.DynamicQueryManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeBenefitAllowanceManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.HolidayHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.HolidayManager;
import com.edatasite.workforce.gwt.core.server.db.LabourPeriodManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonRelationManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.MultiLeaveManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestCommentManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestDurationManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotItemManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.TimeTrackManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintmanager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.benefit.BenefitManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.SickRequestCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.db.impl.ListingObjectItem;
import com.edatasite.workforce.gwt.core.server.db.payroll.SalaryHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.HRSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.HolidayEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.LeaveRequestEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.TimeslotEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.BenefitRequestEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.dashboard.client.rpc.DashboardService;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeManagedDepartment;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
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
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;
import static com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT;

@Transactional
@Service("availabilityService")
public class AvailabilityServiceImpl implements AvailabilityService, AvailabilityServiceLocal, Constants, SchedulerConstant {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilityServiceImpl.class);
    private static final NumberFormat df = new DecimalFormat("#0.00");

    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private SickRequestCFManager sickRequestCFManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private GoalManager goalManager;
    @Autowired
    private GoalHistoryManager goalHistoryManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private LeaveReasonRelationManager leaveReasonRelationManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private BackupEmployeeManager backupEmployeeManager;
    @Autowired
    private TimeSlotItemManager timeSlotItemManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private ShiftSettingsManager shiftSettingsManager;
    @Autowired
    private TimeSlotHistoryManager timeSlotHistoryManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private HolidayManager holidayManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonService;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private SickRequestCommentManager sickRequestCommentManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserFingerPrintmanager userFingerPrintmanager;
    @Autowired
    private TimeTrackManager timeTrackManager;
    @Autowired
    private DateManager dateManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource wfmMessageSource;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    @Qualifier("availabilityCircularResolver")
    private AvailabilityCircularResolver availabilityCircularResolver;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private AnnualLeaveAllowanceManager annualLeaveAllowanceManager;
    @Autowired
    private EmployeeBenefitAllowanceManager employeeBenefitAllowanceManager;
    @Autowired
    private BenefitManager benefitManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private ShiftManager shiftManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private SickRequestDurationManager sickRequestDurationManager;
    @Autowired
    private GlobalAuthJdbcSpringManager jdbcSpringManager;
    @Autowired
    private BenefitRequestManager benefitRequestManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private HolidayHistoryManager holidayHistoryManager;
    @Autowired
    private ReportService reportService;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private HRSettingsManager hrSettingsManager;
    @Autowired
    @Qualifier("accountingLocalizer")
    private WfmMessageSource accountingLocalizer;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    private static final Integer timeslotStart = 0;
    @Autowired
    private LabourPeriodManager labourPeriodManager;
    @Autowired
    private LeaveReasonHistoryManager leaveReasonHistoryManager;
    @Autowired
    private MultiLeaveManager multiLeaveManager;
    @Autowired
    private BrigadaManager brigadaManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private LeaveRequestSolrComponent leaveRequestSolrComponent;
    @Autowired
    private DynamicQueryManager dynamicQueryManager;
    @Autowired
    private BenefitRequestCFManager benefitRequestCFManager;
    @Autowired
    private UserFingerPrintAdjustmentManager userFingerPrintAdjustmentManager;
    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private AttendanceTerminalManager attendanceTerminalManager;
    @Autowired
    private SalaryHistoryManager salaryHistoryManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<TeammatesAvailability> getTeamMates(ListingFilterParameter filterParameter) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsTimeSlot.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(LOGGER, kpiLog, "Get Attendance tracking list");
        ArrayList<TeammatesAvailability> teamMatesList = new ArrayList<>();
        filterParameter.setViewAsId(EdsRole.DR);
        ListingObjectItem<Object[]> teamEmployees = employeeManager.getTeamOrAllEmployees(filterParameter);
        if (teamEmployees == null || teamEmployees.getTotalCount() <= 0) {
            return new ListResult<>(new ArrayList<>(), 0);
        }
        for (Object[] employeeObj : teamEmployees.getItems()) {
            if (employeeObj == null) {
                continue;
            }
            EdsEmployee employee = employeeManager.get((Integer) employeeObj[0]);
            if (employee == null) {
                continue;
            }
            String[] timeS = getTimeSlotByEmployee(employee);
            TeammatesAvailability teamMatesAvailability = new TeammatesAvailability();
            teamMatesAvailability.setEmployeeId(String.valueOf(employee.getObjectID()));
            if (employeeObj[1] != null && !"".equals(employeeObj[1])) {
                teamMatesAvailability.setEmployee((String) employeeObj[1]);
            } else {
                teamMatesAvailability.setEmployee(employee.getFullName());
            }
            if (employeeObj[2] != null && employeeObj[3] != null && !"".equals(employeeObj[3])) {
                teamMatesAvailability.setDepartment((String) employeeObj[3]);
                teamMatesAvailability.setDepartmentId((Integer) employeeObj[2]);
            } else if (employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                EdsDepartment department = employee.getEmployeeDepartment().getTeam();
                teamMatesAvailability.setDepartment(department.getName());
                teamMatesAvailability.setDepartmentId(department.getObjectID());
            }
            if (employeeObj[4] != null && !"".equals(employeeObj[4])) {
                teamMatesAvailability.setTimeslot((String) employeeObj[4]);
            } else {
                teamMatesAvailability.setTimeslot(getTimeSlot(employee).getName());
            }
            teamMatesAvailability.setFrom(timeS[0]);
            teamMatesAvailability.setTo(timeS[1]);
            if (employeeObj[5] != null && !"".equals(employeeObj[5])) {
                teamMatesAvailability.setStatus((String) employeeObj[5]);
            } else {
                teamMatesAvailability.setStatus("Out");
            }
            teamMatesList.add(teamMatesAvailability);
        }
        return new ListResult<>(teamMatesList, teamEmployees.getTotalCount());
    }

    private EdsTimeSlot getTimeSlot(EdsEmployee employee) {
        Integer timeSlotId;
        if (employee == null) {
            EdsTimeSlot defaultTimeSlot = companyManager.get(SecurityContext.getCompanyID()).getDefaultTimeSlot();
            timeSlotId = defaultTimeSlot.getObjectID();
        } else if (employee.getTimeSlot() != null) {
            timeSlotId = employee.getTimeSlot().getObjectID();
        } else {
            timeSlotId = employee.getCompany().getDefaultTimeSlot().getObjectID();
        }
        if (timeSlotItemManager.getTimeSlotItems(timeSlotManager.get(timeSlotId)).isEmpty()) {
            timeSlotId = 1;
        }
        return timeSlotManager.get(timeSlotId);
    }

    private String[] getTimeSlotByEmployee(EdsEmployee employee) {
        String startTime;
        String endTime;
        Calendar cal = new GregorianCalendar(employee.getCompany().getTimeZone());
        cal.setTime(employee.getCompany().getCompanyDate());
        List<EdsTimeSlotItem> tsi = getTimeSlotItems(employee);
        Map<Integer, EdsTimeSlotItem> timeSlotItemMap = new HashMap<>();
        for (EdsTimeSlotItem item : tsi) {
            timeSlotItemMap.put(item.getDay(), item);
        }
        long start = 0;
        long end = 0;
        if (tsi.size() >= cal.get(Calendar.DAY_OF_WEEK)) {
            start = timeSlotItemMap.get(cal.get(Calendar.DAY_OF_WEEK) - 1).getStartTime();
            end = timeSlotItemMap.get(cal.get(Calendar.DAY_OF_WEEK) - 1).getEndTime();
        }
        startTime = getTimeslot(start / 60) + ":" + getTimeslot(start % 60);
        endTime = getTimeslot(end / 60) + ":" + getTimeslot(end % 60);
        return new String[]{startTime, endTime};
    }

    private String getTimeslot(long time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }
        return String.valueOf(time);
    }

    private List<EdsTimeSlotItem> getTimeSlotItems(EdsEmployee employee) {
        return timeSlotItemManager.getTimeSlotItems(getTimeSlot(employee));
    }

    private boolean prorataEnabled(EdsLeaveReason reason) {
        return reason != null && reason.hasProrata() && CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(reason.getCode());
    }

    private boolean prorataEnabledWithAnnualAndSickLeave(EdsLeaveReason reason) {
        return reason != null && reason.hasProrata() && (CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(reason.getCode()) || CustomFormConstants.LR_TYPE_SICK_LEAVE.equals(reason.getCode()));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LeaveRequestChartRpc getLeaveRequestChartData(ListingFilterParameter fp) {
        List<EdsLeaveReason> reasons = new ArrayList<>();
        if (fp.getReasonID() != null) {
            reasons.add(leaveReasonManager.get(fp.getReasonID()));
        } else {
            reasons = getUserLeaveReasonList(fp.getEmployeeId(), true);
        }

        Map<String, Double> allowanceMap = annualLeaveAllowanceManager.getLeaveEmployeeAllowance(fp.getYear(), fp.getEmployeeId());
        if (allowanceMap.get(CustomFormConstants.LR_TYPE_ANNUAL_LEAVE) != null && fp.getYear() != null && hasOpeningBalance(null, fp.getYear())) {
            EdsEmployee edsEmployee = employeeManager.get(fp.getEmployeeId());
            allowanceMap.put(CustomFormConstants.LR_TYPE_ANNUAL_LEAVE, edsEmployee.getOpeningBalanceDays());
        }

        List<String> reasonCodeList = reasons.stream().map(EdsLeaveReason::getCode).collect(Collectors.toList());

        Map<String, Double[]> takendays = null;
        if (!reasonCodeList.isEmpty()) {
            takendays = getEmployeeTakenDaysByReasons(fp, reasonCodeList);
        }

        LinkedHashMap<String, String> reasonNameList = new LinkedHashMap<>();

        Map<String, Double[]> finalTakendays = takendays;

        reasons.forEach(r -> {
            double allowance = allowanceMap.get(r.getCode()) != null ? allowanceMap.get(r.getCode()) : 0d;
            Double[] takenForThisType = finalTakendays.get(r.getCode()) != null ? finalTakendays.get(r.getCode()) : null;

            if (allowance > 0d || (takenForThisType != null && takenForThisType.length > 0 && !Arrays.stream(takenForThisType).allMatch(n -> n == 0))) {
                reasonNameList.put(r.getName(), r.getCode());
            } else {
                allowanceMap.remove(r.getName());
            }
        });

        int size = reasonNameList.size();

        Double[] paid = new Double[size];
        Double[] nonPaid = new Double[size];
        Double[] left = new Double[size];
        Double[] exceeded = new Double[size];

        int counter = 0;
        for (int i = 0; i < reasons.size(); i++) {
            String code = reasons.get(i).getCode();
            if (reasonNameList.containsValue(code)) {
                fp.setReasonCode(code);

                boolean isAnnual = prorataEnabledWithAnnualAndSickLeave(reasons.get(i));
                double allowance = allowanceMap.get(code) != null ? allowanceMap.get(code) : 0d;

                if (!isAnnual) {
                    left[counter] = allowance;
                }
                if (takendays != null && !takendays.isEmpty()) {
                    Double[] dd = takendays.get(code);

                    if (dd != null && dd.length > 0) {
                        paid[counter] = dd[0];
                        if (allowance > 0d && paid[counter] > allowance && !isAnnual) {
                            exceeded[counter] = paid[counter] - allowance;
                            paid[counter] = allowance;
                        }
                        nonPaid[counter] = dd[1];
                        left[counter] = allowance - dd[0];
                        if (isAnnual && dd.length > 2) {
                            double balance = dd[2];
                            if (balance > 0) {
                                left[counter] = balance;
                            } else {
                                left[counter] = 0d;
                                exceeded[counter] = -1 * balance;
                            }
                        }
                    }
                }
                counter++;
            }
        }
        EdsUser user = userManager.get(fp.getEmployeeId());

        String title = user.getFullName();
        if (fp.getReasonID() != null) {
            title = accountingLocalizer.localize("asOf", "As of") + " " + ServerUtils.shortDateFormat(fp.getStartDate(), user.getCompany(), true);
        }
        LeaveRequestChartRpc chartRpc = new LeaveRequestChartRpc(title);
        chartRpc.setTopNames(reasonNameList);
        chartRpc.setNonPaid(nonPaid);
        chartRpc.setPaid(paid);
        chartRpc.setLeft(left);
        chartRpc.setExceeded(exceeded);
        return chartRpc;
    }

    @Override
    public void createOrUpdateLeaveAllowance(Integer id) {
        List<EdsLeaveReason> reasons = leaveReasonManager.listActiveReasons();
        if (reasons == null || reasons.isEmpty()) {
            return;
        }
        for (EdsLeaveReason reason : reasons) {
            createOrUpdateLeaveAllowance(id, reason, null);
        }
    }

    @Override
    public void createOrUpdateLeaveAllowance(Integer id, EdsLeaveReason reason, Date effectiveDate) {
        Calendar calendar = Calendar.getInstance();
        if (reason == null) {
            reason = leaveReasonManager.findByCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
        }
        EdsAnnualLeaveAllowance allowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(calendar.get(Calendar.YEAR), id, reason.getCode(), effectiveDate);
        if (allowance == null) {
            allowance = new EdsAnnualLeaveAllowance();
        }
        allowance.setAllowanceDays(reason.getLeaveDays());
        allowance.setAllowanceYear(calendar.get(Calendar.YEAR));
        allowance.setEmployee(employeeManager.get(id));
        allowance.setReasonCode(reason.getCode());
        allowance.setEffectiveDate(effectiveDate);
        annualLeaveAllowanceManager.createOrUpdate(allowance);
    }

    @Override
    public void createOrUpdateLeaveAllowanceWithDays(Integer id, EdsLeaveReason reason, Date effectiveDate, Double leaveDays) {
        Calendar calendar = Calendar.getInstance();
        EdsAnnualLeaveAllowance allowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(calendar.get(Calendar.YEAR), id, reason.getCode(), effectiveDate);
        if (allowance == null) {
            allowance = new EdsAnnualLeaveAllowance();
        }
        allowance.setAllowanceDays(leaveDays);
        allowance.setAllowanceYear(calendar.get(Calendar.YEAR));
        allowance.setEmployee(employeeManager.get(id));
        allowance.setReasonCode(reason.getCode());
        allowance.setEffectiveDate(effectiveDate);
        annualLeaveAllowanceManager.createOrUpdate(allowance);
    }

    public Integer getLRYear(Integer employeeID, Integer currentYear) {
        EdsUser user = userManager.getUser();
        EdsEmployee employee;
        if (employeeID != null) {
            employeeID = user.getObjectID();
        }
        employee = employeeManager.get(employeeID);
        if (currentYear == null) {
            currentYear = Calendar.getInstance().get(Calendar.YEAR);
        }
        Date startYearDate = ServerUtils.getYearStartDate(currentYear);
        Date endYearDate = ServerUtils.getYearEndDate(currentYear);

        List<EdsSickRequest> lrs = sickRequestManager.getSickRequestByEmployeeAndPeriod(employee, startYearDate, endYearDate);
        if (lrs == null || lrs.isEmpty()) {
            startYearDate = ServerUtils.getYearStartDate(currentYear + 1);
            endYearDate = ServerUtils.getYearEndDate(currentYear + 1);
            List<EdsSickRequest> lrs2 = sickRequestManager.getSickRequestByEmployeeAndPeriod(employee, startYearDate, endYearDate);
            if (lrs2 == null || lrs2.isEmpty()) {
                return currentYear;
            } else {
                return currentYear + 1;
            }
        } else {
            return currentYear;
        }
    }

    @Override
    public Integer getLeaveRequestListCount(ListingFilterParameter fp) {
        return sickRequestManager.getLeaveRequestListCount(fp);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String hasAccessInsertSickRequestsByPeriod(Integer employeeId, NewLeaveRequest leaveRequest, boolean recalculate) {
        int error = 0;
        String hasAccess = null;
        for (Map.Entry<String, ArrayList<MultiLeaveDTO>> entry : leaveRequest.getMultiLeaveValues().entrySet()) {
            for (MultiLeaveDTO dto : entry.getValue()) {
                NewLeaveRequest lr = leaveRequest;
                lr.setAllDay(false);
                lr.setStartNonConverable(leaveRequest.getStartNonConverable());
                lr.setEndNonConverable(leaveRequest.getEndNonConverable());
                lr.setTakeByMoney(!dto.getSickRequestType().equals(Constants.DAY));
                lr.setMultiLeaveDTO(dto);
                hasAccess = hasAccessInsertRequest(employeeId, lr, lr.getStartNonConverable(), lr.getEndNonConverable(), recalculate);
                if (!hasAccess.equals(Constants.TRUE)) {
                    error++;
                }
            }
        }
        return error == 0 ? "TRUE" : hasAccess;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String hasAccessInsertRequest(Integer employeeId, NewLeaveRequest leaveRequest, DateNonConvertable startDate, DateNonConvertable endDate, boolean recalculate) {

        int userId;
        EdsUser user = employeeManager.getUser();
        if (employeeId != null) {
            userId = employeeId;
        } else {
            userId = user.getObjectID();
        }
        String username;
        if (user.getObjectID().equals(employeeId)) {
            username = "Sorry, you've";
        } else {
            user = userManager.get(employeeId);
            username = "Sorry, " + user.getFullName() + " has";
        }

        List<EdsSickRequest> requests;
        if (recalculate) {
            requests = sickRequestManager.findApprovedLeavesByExcludeSick(leaveRequest.getObjectID(), userId, startDate.getNonConvertedDate(), endDate.getNonConvertedDate());
        } else {
            requests = sickRequestManager.findApprovedLeaveRequestsByUserId(userId, startDate.getNonConvertedDate(), endDate.getNonConvertedDate());
        }

        if (requests != null && !requests.isEmpty() && requests.get(0).getLeaveReason() != null) {
            boolean dayAfterMoney = false;
            boolean moneyAfterDay = false;
            if (leaveRequest != null) {
                dayAfterMoney = requests.get(0).isTakeByMoney().equals(true) && leaveRequest.isTakeByMoney().equals(false);
                moneyAfterDay = requests.get(0).isTakeByMoney().equals(false) && leaveRequest.isTakeByMoney().equals(true);
            }
            if ((dayAfterMoney || moneyAfterDay) && requests.size() < 2) {
                return Constants.TRUE;
            }
            boolean markAsDraft = true;
            for (EdsSickRequest r : requests) {
                if (r.getLeaveReason() != null && !r.getLeaveReason().getMarkAsDraft()) {
                    markAsDraft = false;
                    break;
                }
            }
            if (!markAsDraft) {
                EdsSickRequest request = requests.get(0);
                EdsLeaveReason reason = null;
                if (leaveRequest != null && leaveRequest.getReasonId() != null) {
                    reason = leaveReasonManager.get(leaveRequest.getReasonId());
                }
                if (reason != null && CustomFormConstants.LR_TYPE_SICK_LEAVE.equals(reason.getCode()) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_RETURN_LEAVE_REQUEST)) {
                    for (EdsSickRequest item : requests) {
                        if (item.getLeaveReason() != null && CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(item.getLeaveReason().getCode())) {
                            return CustomFormConstants.LR_TYPE_ANNUAL_LEAVE;
                        }
                    }
                }
                return username + " already applied for " + request.getLeaveReason().getName() + " on " + ServerUtils.shortDateFormat(request.getStartDate(), user);
            }
        }

        if (leaveRequest != null && leaveRequest.getObjectID() != null) {
            List<EdsSickRequest> sameRequests = sickRequestManager.findSameLeaveRequests(leaveRequest.getObjectID(), userId, startDate.getNonConvertedDate(), endDate.getNonConvertedDate(), leaveRequest.getReasonId());
            if (sameRequests != null && !sameRequests.isEmpty()) {
                return Constants.HAS_THE_SAME_LR;
            }
        }
        return Constants.TRUE;
    }

    @Transactional
    public Integer createLeaveRequest(NewLeaveRequest leaveRequest) {
        EdsUser registered = leaveRequest.getCreatorId() != null ? employeeManager.get(leaveRequest.getCreatorId()) : employeeManager.getUser();
        EdsUser firstApprover = null;
        EdsUser user;
        EdsLeaveReason reason = null;
        if (leaveRequest.getEmployee() != null) {
            user = employeeManager.get(leaveRequest.getEmployee());
        } else {
            user = registered;
        }
        if (user != null) {
            user.clear();
        }
        EdsEmployee employee = user.getEmployee();
        if (leaveRequest.getReasonId() != null) {
            reason = leaveReasonManager.get(leaveRequest.getReasonId());
        }
        if (reason != null && CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(reason.getCode()) && employee.getStartDate() != null) {
            int day = ServerUtils.getDayCount(employee.getStartDate(), user.getUserDate(leaveRequest.getStartNonConverable().getNonConvertedDate()));
            if (employee.getProbationDays() > day) {
                return VALIDATION;
            }
        }
        if (leaveRequest.getNumberData() == null) {
            leaveRequest.setNumberData(generateLeaveRequestNumber());
        }

        EdsSickRequest sickRequest = null;
        if (leaveRequest.getObjectID() != null) {
            sickRequest = sickRequestManager.get(leaveRequest.getObjectID());
            if (sickRequest != null) {
                if (sickRequest.getNumberData() != null && !sickRequest.getNumberData().equals(leaveRequest.getNumberData().getNumberString())) {
                    Boolean code = sickRequestManager.getLeaveRequestByCode(leaveRequest.getNumberData().getNumberString(), null);
                    if (code) {
                        return Errors.THIS_NAME_IS_ALREADY_EXIST;
                    }
                }
            }
        } else {
            Boolean code = sickRequestManager.getLeaveRequestByCode(leaveRequest.getNumberData().getNumberString(), null);
            if (code) {
                leaveRequest.setNumberData(generateLeaveRequestNumber());
            }
        }
        if (sickRequest == null) {
            sickRequest = new EdsSickRequest();
        }
        sickRequestManager.createOrUpdate(createOrUpdateSickRequest(leaveRequest, registered, employee, user, reason, sickRequest));

        createLeaveRequestHistory(sickRequest.getObjectID(), new HistoryListItem(leaveRequest.getObjectID() != null ? "updated" : "created"));

        if (!leaveRequest.isRecalculate()) {
            initApprovers(leaveRequest, sickRequest, reason);
        }

        if (leaveRequest.getPeriodList() != null && !leaveRequest.getPeriodList().isEmpty()) {
            List<EdsLabourPeriod> labourPeriods = labourPeriodManager.sickRequestPeriods(sickRequest.getObjectID(), false);
            if (labourPeriods != null) {
                sickRequestDurationManager.deleteDurationBySickId(sickRequest.getObjectID());
            }
            Double leaveDays = 0d, moneyDays = 0d;

            Calendar periodStartDate = Calendar.getInstance();
            periodStartDate.setTime(leaveRequest.getStartNonConverable().getNonConvertedDate());
            LinkedHashMap<String, String> dateByPeriodMap = new LinkedHashMap<>();
            for (LaborPeriodRequest request : leaveRequest.getPeriodList()) {
                if (request.getMultiLeaveList() != null && request.getMultiLeaveList().size() > 0) {
                    for (MultiLeaveDTO multileave : request.getMultiLeaveList()) {
                        leaveDays += multileave.getSickRequestDuration();
                        if (Constants.MONEY.equals(multileave.getSickRequestType())) {
                            moneyDays += multileave.getSickRequestDuration();
                        }
                        putDaysToPeriods(employee.getObjectID(), periodStartDate, (double) multileave.getSickRequestDuration(), reason.getCode(), request.getPeriodID(), dateByPeriodMap, multileave.getSickRequestType());
                    }
                } else if (request.getTakenDays() != null && request.getTakenDays() > 0) {
                    leaveDays += request.getTakenDays();
                    putDaysToPeriods(employee.getObjectID(), periodStartDate, request.getTakenDays(), reason.getCode(), request.getPeriodID(), dateByPeriodMap, Constants.DAY);
                }
            }
            Date leaveEndDateWithoutMoneyDays = null;
            if (leaveDays != 0d) {
                sickRequest.setStartDate(leaveRequest.getStartNonConverable().getNonConvertedDate());
                leaveEndDateWithoutMoneyDays = setEndTime(employee.getObjectID(), leaveRequest.getStartNonConverable().getNonConvertedDate(), leaveDays - moneyDays, reason.getCode());
                sickRequest.setEndDate(setEndTime(employee.getObjectID(), leaveRequest.getStartNonConverable().getNonConvertedDate(), leaveDays, reason.getCode()));
            } else {
                sickRequest.setStartDate(leaveRequest.getStartNonConverable().getNonConvertedDate());
                sickRequest.setEndDate(leaveRequest.getStartNonConverable().getNonConvertedDate());
            }
            sickRequest.setUsedExperienceDays(leaveRequest.getUsedExperienceDays());
            sickRequestManager.createOrUpdate(sickRequest);

            populateLeaveHoursToAttendanceRawData(sickRequest, dateByPeriodMap, null, true, null);
            if (moneyDays > 0) {
                sickRequest.setEndDate(leaveEndDateWithoutMoneyDays);
            }
        } else {
            populateLeaveHoursToAttendanceRawData(sickRequest, null, null, true, null);
        }

        if (leaveRequest.isSelfApprover()) {
            updateApprove(Constants.LR_STATUS_SS_APPROVED, sickRequest.getObjectID(), false);
        }

        if (sickRequest.getObjectID() != null && leaveRequest.getAttachments() != null && leaveRequest.getAttachments().length > 0) {
            attachmentUtilsManager.saveAttachments(F_LEAVE_REQUEST, sickRequest.getObjectID(), sickRequest.getObjectID(), leaveRequest.getAttachments());
        }
        //send message to selected employee
        if (sickRequest.getObjectID() != null && leaveRequest.getEmployeeIds() != null && leaveRequest.getEmployeeIds().size() > 0) {
            for (Integer selectedEmployeeID : leaveRequest.getEmployeeIds()) {
                if (firstApprover != null && !firstApprover.getObjectID().equals(selectedEmployeeID)) {
                    EdsUser selectedToUser = userManager.get(selectedEmployeeID);
                    if (selectedToUser != null) {
                        sendMessageToEmployeeAddLeaveRequest(selectedToUser, sickRequest);
                    }
                }
            }
        }

        /* Register to notification table*/
        baseEventPostProcessor.registerEvent(LeaveRequestEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, sickRequest, user);
        /* add workflow event to start workflow rule... */
        String workflowEventType = BaseEventsPostProcessorImpl.EVENT_TYPE_ADD;
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, workflowEventType, sickRequest, user);
        workflowEvent.setEntityType(RelationItem.TYPE_LEAVE_REQUEST);
        /* add workflow event to start workflow rule... */
        EdsBusinessEvent workflowEvent2 = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), sickRequest, user);
        workflowEvent2.setEntityType(RelationItem.TYPE_LEAVE_REQUEST);

        updateEmployeeLeaveDuration(user); //for workflow

        return addToIndex(sickRequest);
    }

    private void putDaysToPeriods(Integer employeeID, Calendar periodStartDate, Double leaveDays, String reasonCode, Integer periodID, LinkedHashMap<String, String> dateByPeriodMap, String dayType) {
        Date enddate = getEndDate(employeeID, periodStartDate.getTime(), leaveDays, reasonCode);
        while (!periodStartDate.getTime().after(enddate)) {
            dateByPeriodMap.put(new SimpleDateFormat(Constants.DATE_PATTERN).format(periodStartDate.getTime()), periodID + "@" + dayType);
            periodStartDate.add(Calendar.DATE, 1);
        }
    }

    private void initApprovers(NewLeaveRequest leaveRequest, EdsSickRequest sickRequest, EdsLeaveReason reason) {
        EdsReference approvedStatus = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);
        EdsReference submittedStatus = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.NOT_DEFINED);

        if (isOk(leaveRequest.getApprovers()) && !GenericSettingsEnum.ATTENDANCE_REPORT_BETA.name().equals(leaveRequest.getFrom())) {
            List<ApproverItemMini> approvers = leaveRequest.getApprovers();

            int countApprovers = approvers.size();
            if (isOk(leaveRequest.getBackupEmployee())) {
                approvers = new ArrayList<>();

                Integer clonedFromId = leaveRequest.getApprovers().get(0).getClonedFrom();
                int index = 1;
                for (BackupEmployeeItem backupEmployeeItem : leaveRequest.getBackupEmployee()) {
                    ApproverItemMini parentBackupEmployee = backupEmployeeItem.getParentBackupEmployee();
                    parentBackupEmployee.setClonedFrom(clonedFromId);
                    parentBackupEmployee.setBackup(true);
                    parentBackupEmployee.setApproverOrder(index);
                    approvers.add(parentBackupEmployee);
                    index++;
                    if (!CollectionUtils.isEmpty(backupEmployeeItem.getChildList())) {
                        for (ApproverItemMini childBackupEmployeeItem : backupEmployeeItem.getChildList()) {
                            childBackupEmployeeItem.setClonedFrom(clonedFromId);
                            childBackupEmployeeItem.setBackup(true);
                            childBackupEmployeeItem.setApproverOrder(index);
                            approvers.add(childBackupEmployeeItem);
                            index++;
                        }
                    }
                }
                for (ApproverItemMini approver : leaveRequest.getApprovers()) {
                    approver.setApproverOrder(index);
                    approvers.add(approver);
                    index++;
                }
                approvers.sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            }

            boolean isFirstApprover = true;
            int i = 0;
            for (final ApproverItemMini approverItem : approvers) {
                final EdsApprover _edsApprover = this.approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    this.approverManager.update(_edsApprover);
                    if (sickRequest.getCurrentApprover() != null && leaveRequest.getStatusCode() != null && isFirstApprover) {
                        sickRequest.getCurrentApprover().setStatus(this.referenceManager.findReference(EdsSickRequest._SICK_STATUS, leaveRequest.getStatusCode()));
                        sickRequest.setEntityStatus(submittedStatus);
                        isFirstApprover = false;
                    } else if (sickRequest.getCurrentApprover() != null && leaveRequest.getStatusCode() != null) {
                        sickRequest.getCurrentApprover().setStatus(submittedStatus);
                    }
                    if (leaveRequest.getStatusCode() != null && (!Constants.APPROVED.equals(leaveRequest.getStatusCode()) || leaveRequest.isFromApi())) {
                        sickRequest.setEntityStatus(referenceManager.findReference(EdsSickRequest._SICK_STATUS, leaveRequest.getStatusCode()));
                    }
                    if (sickRequest.isCurrentApproverRejected()) {
                        sickRequest.setEntityStatus(sickRequest.getCurrentApprover().getStatus());
                    }
                    if (reason.getAutoApprove()) {
                        sickRequest.getCurrentApprover().setStatus(approvedStatus);
                        sickRequest.setEntityStatus(approvedStatus);
                    }
                    continue;
                }
                final EdsApprover edsApprover = _edsApprover.cloneShallow();
                if (!CollectionUtils.isEmpty(leaveRequest.getBackupEmployee()) && approvers.size() > countApprovers && i + 1 < approvers.size()) {
                    edsApprover.setOnApprovedAction(1);
                    i++;
                }
                edsApprover.setObjectID(null);
                edsApprover.setBackup(approverItem.isBackup());
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(sickRequest.getObjectID());
                edsApprover.setIs_default(false);
                edsApprover.setApproverOrder(approverItem.getApproverOrder());

                if (reason.getAutoApprove()) {
                    edsApprover.setStatus(approvedStatus);
                } else if (leaveRequest.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(this.referenceManager.findReference(EdsSickRequest._SICK_STATUS, leaveRequest.getStatusCode()));
                    if (Constants.DRAFT.equals(leaveRequest.getStatusCode())) {
                        sickRequest.setEntityStatus(this.referenceManager.findReference(EdsSickRequest._SICK_STATUS, leaveRequest.getStatusCode()));
                    } else {
                        sickRequest.setEntityStatus(submittedStatus);
                    }
                    isFirstApprover = false;
                } else if (leaveRequest.getStatusCode() != null) {
                    edsApprover.setStatus(submittedStatus);
                }
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                    edsApprover.setExactEmployee(user_);
                }
                edsApprover.setApproverRoles(new HashSet<>());
                edsApprover.setApproverEmployees(new HashSet<>());
                edsApprover.setDynamicQueries(new HashSet<>());
                this.approverManager.createOrUpdate(edsApprover);

                for (final EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                    edsApprover.getApproverRoles().add(roleapp);
                }

                for (final EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                    edsApprover.getApproverEmployees().add(ucerapp);
                }

                if (sickRequest.getCurrentApprover() == null) {
                    sickRequest.setCurrentApprover(edsApprover);
                }
                sickRequest.getApprovers().add(edsApprover);
            }
        } else {
            atendanceReportLeave(sickRequest, approvedStatus, submittedStatus);
        }
        if (reason.getAutoApprove()) {
            sickRequest.setOverallStatus(approvedStatus);
        }
        sickRequestManager.createOrUpdate(sickRequest);


        if (isOk(leaveRequest.getBackupEmployee())) {
            if (leaveRequest.getObjectID() != null) {
                backupEmployeeManager.deleteBySickRequestId(leaveRequest.getObjectID());
            }
            saveBackupEmployees(sickRequest, leaveRequest.getBackupEmployee());
        }
    }

    private void saveBackupEmployees(EdsSickRequest sickRequest, List<BackupEmployeeItem> backupEmployeeItemList) {
        EdsSickRequest finalSickRequest = sickRequest;
        backupEmployeeItemList.forEach((value) -> {
            ApproverItemMini parentBackupEmployeeItem = value.getParentBackupEmployee();
            EdsBackupEmployee parentBackupEmployee = new EdsBackupEmployee();
            parentBackupEmployee.setStartDate(setStartTime(sickRequest.getEmployee().getObjectID(), parentBackupEmployeeItem.getFromBackupEmployeeDate().getNonConvertedDate()));
            parentBackupEmployee.setDueDate(setEndTime(sickRequest.getEmployee().getObjectID(), parentBackupEmployeeItem.getDueBackupEmployeeDate().getNonConvertedDate()));
            parentBackupEmployee.setEmployees(employeeManager.get(parentBackupEmployeeItem.getExactEmployee().getId()));
            parentBackupEmployee.setSickRequest(finalSickRequest);
            parentBackupEmployee.setParentId(null);
            backupEmployeeManager.create(parentBackupEmployee);
            value.getChildList().forEach((child -> {
                EdsBackupEmployee childBackupEmployee = new EdsBackupEmployee();
                childBackupEmployee.setStartDate(child.getFromBackupEmployeeDate().getNonConvertedDate());
                childBackupEmployee.setDueDate(child.getDueBackupEmployeeDate().getNonConvertedDate());
                childBackupEmployee.setEmployees(employeeManager.get(child.getExactEmployee().getId()));
                childBackupEmployee.setSickRequest(finalSickRequest);
                childBackupEmployee.setParentId(parentBackupEmployee.getObjectID());
                backupEmployeeManager.create(childBackupEmployee);
            }));
        });
    }

    private EdsSickRequest createChildSickRequest(NewLeaveRequest leaveRequest, EdsSickRequest parent, EdsSickRequest child, EdsUser registered, EdsEmployee employee, EdsUser user, EdsLeaveReason reason) {
        child.setParent(parent);
        child.setRegisteredBy(registered.getEmployee());
        child.setDescription(leaveRequest.getDescription());
        child.setCreatedDate(leaveRequest.getStartDate() != null ? leaveRequest.getStartDate() : new Date());
        child.setEmployee(employee);

        child.setFromApi(leaveRequest.getFrom() == null);
        child.setUpdater(user);

        child.setLeaveReason(reason);

        if (reason != null) {
            child.setIncludeDayOff(reason.getIncludeDayOffs() != null ? reason.getIncludeDayOffs() : false);
        }
        if (leaveRequest.getType() != null) {
            child.setType(referenceManager.findReference(EdsSickRequest._SICK_TYPE, leaveRequest.getType())); //TODO check this
        } else {
            child.setType(null);
        }
        child.setTakeByMoney(leaveRequest.isTakeByMoney());
        child.setToTakeFromAllowance(EdsSickRequest.PAID.equals(leaveRequest.getType()));
        child.setCustomFields(saveCustomFields(child.getCustomFields(), leaveRequest.getCustomFields()));
        if (leaveRequest.getStatusCode() == null) {
            child.setOverallStatus(null);
            child.setCurrentApprover(null);
        }
        child.setLaborPeriod(leaveRequest.getMultiLeaveDTO().getLaborPeriod());
        child.setMoneyDays(Integer.valueOf(leaveRequest.getDay()));
        initApprovers(leaveRequest, child, reason);
        return child;
    }

    private Integer addToIndex(EdsSickRequest sickRequest) {
        try {
            leaveRequestSolrComponent.index(sickRequest);
        } catch (InterruptedException | SolrServerException | IOException e) {
            e.printStackTrace();
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSickRequest.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(sickRequest.getObjectID());
        ServerUtils.kpiLog(LOGGER, kpiLog, "Add leave request");
        return sickRequest.getObjectID();
    }

    private EdsSickRequest createOrUpdateSickRequest(NewLeaveRequest leaveRequest, EdsUser registered, EdsEmployee employee, EdsUser user, EdsLeaveReason reason, EdsSickRequest sickRequest) {

        if (leaveRequest.getObjectID() != null) {
            sickRequest.setObjectID(leaveRequest.getObjectID());
        }
        if (leaveRequest.getNumberData() != null) {
            sickRequest.setIntNumber(leaveRequest.getNumberData().getIntNumber());
            sickRequest.setNumberData(leaveRequest.getNumberData().getNumberString());
            EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
            numberingSettings.setLeaveRequestLastIntNumber((leaveRequest.getNumberData().getIntNumber() != null ? leaveRequest.getNumberData().getIntNumber() : 1) + 1);
            numberingSettingsManager.update(numberingSettings);
        }
        sickRequest.setRegisteredBy(registered.getEmployee());
        sickRequest.setDescription(leaveRequest.getDescription());
        sickRequest.setCreatedDate(leaveRequest.getStartDate() != null ? leaveRequest.getStartDate() : new Date());
        sickRequest.setEmployee(employee);

        sickRequest.setFromApi(leaveRequest.getFrom() == null);
        sickRequest.setUpdater(user);

        sickRequest.setLeaveReason(reason);

        if (reason != null && LR_TYPE_UNAUTHORIZED_LEAVE.equals(reason.getCode())) {
            leaveRequest.setType(EdsSickRequest.NON_PAID);
        }
        if (reason != null) {
            sickRequest.setIncludeDayOff(reason.getIncludeDayOffs() != null ? reason.getIncludeDayOffs() : false);
        }
        if (LayoutRPC.LEAVE_REQUEST_FORM.equals(leaveRequest.getFrom())) {
            sickRequest.setStartDate(leaveRequest.getStartNonConverable().getNonConvertedDate());
            sickRequest.setEndDate(leaveRequest.getEndNonConverable().getNonConvertedDate());
        } else {//for api, old logic
            Calendar sDate = Calendar.getInstance();
            sDate.setTime(leaveRequest.getStartNonConverable().getNonConvertedDate());
            sDate.set(Calendar.HOUR_OF_DAY, leaveRequest.getStartHour());
            sDate.set(Calendar.MINUTE, leaveRequest.getStartMinut());
            sDate.set(Calendar.SECOND, 0);
            sDate.set(Calendar.MILLISECOND, 0);
            sickRequest.setStartDate(sDate.getTime());

            Calendar eDate = Calendar.getInstance();
            eDate.setTime(leaveRequest.getEndNonConverable().getNonConvertedDate());
            eDate.set(Calendar.HOUR_OF_DAY, leaveRequest.getEndHour());
            eDate.set(Calendar.MINUTE, leaveRequest.getEndMinut());
            eDate.set(Calendar.SECOND, 0);
            eDate.set(Calendar.MILLISECOND, 0);
            sickRequest.setEndDate(eDate.getTime());
        }
        if (leaveRequest.getType() != null) {
            sickRequest.setType(referenceManager.findReference(EdsSickRequest._SICK_TYPE, leaveRequest.getType())); //TODO check this
        } else {
            sickRequest.setType(null);
        }
        sickRequest.setTakeByMoney(leaveRequest.isTakeByMoney());
        sickRequest.setToTakeFromAllowance(EdsSickRequest.PAID.equals(leaveRequest.getType()));
        sickRequest.setCustomFields(saveCustomFields(sickRequest.getCustomFields(), leaveRequest.getCustomFields()));
        if (leaveRequest.getStatusCode() == null) {
            sickRequest.setOverallStatus(null);
            sickRequest.setCurrentApprover(null);
        }
        return sickRequest;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NumberData generateLeaveRequestNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = sickRequestManager.getLeaveRequestLastIntNumber();
        if (settings != null && settings.getLeaveRequestLastIntNumber() != null && settings.getLeaveRequestNumberingFormat() != null
                && !"".equals(settings.getLeaveRequestNumberingFormat()) && settings.getLeaveRequestNumberingFormat().contains(Constants.WIDGET_PREFIX)) {
            intNumber = settings.getLeaveRequestLastIntNumber();
        }
        if (settings != null && settings.getLeaveRequestNumberingFormat() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getLeaveRequestNumberingFormat(), settings.getDelimetrLeaveRequestNumbering(), null, null, null, "leaveRequest");
            numberData.setDelimiter(settings.getDelimetrLeaveRequestNumbering());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_LEAVE_REQUEST_PREFIX /*true*/);
        }
    }

    @Override
    public void restartLeaveRequestNumber() {
        EdsNumberingSettings restartDate = this.numberingSettingsManager.getNumberingSetting();
        restartDate.setLeaveRequestLastIntNumber(restartDate.getLeaveRequestIntNumber());
        this.numberingSettingsManager.update(restartDate);
    }

    @Override
    public void leaveRequestCorrection(String reasonShortName) {
        EdsLeaveReason reason = leaveReasonManager.getReasonByShortName(reasonShortName);

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setReasonID(reason.getObjectID());
        fp.setStart(0);


        List<EdsSickRequest> leaveRequests = sickRequestManager.getList(fp);

        System.out.println("-------------Leave size - : - " + leaveRequests.size());
        int i = 0, j = 0;
        for (EdsSickRequest sickRequest : leaveRequests) {
            i++;
            if (sickRequest.getSickRequestForPeriodList() != null && sickRequest.getSickRequestForPeriodList().size() > 0) {
                List<EdsLabourPeriod> labourPeriods = labourPeriodManager.sickRequestPeriods(sickRequest.getObjectID(), false);
                if (labourPeriods != null) {
                    sickRequestDurationManager.deleteDurationBySickId(sickRequest.getObjectID());
                }
                Double leaveDays = 0d, moneyDays = 0d;

                Calendar periodStartDate = Calendar.getInstance();
                periodStartDate.setTime(sickRequest.getStartDate());
                LinkedHashMap<String, String> dateByPeriodMap = new LinkedHashMap<>();
                for (EdsSickRequestForPeriod request : sickRequest.getSickRequestForPeriodList()) {
                    if (request.getDays() != null && request.getDays() > 0) {
                        leaveDays += request.getDays();
                        putDaysToPeriods(sickRequest.getEmployee().getObjectID(), periodStartDate, request.getDays(), reason.getCode(), request.getPeriod().getObjectID(), dateByPeriodMap, Constants.DAY);
                    }
                }
                Date leaveEndDateWithoutMoneyDays = null;
                if (leaveDays != 0d) {
                    leaveEndDateWithoutMoneyDays = setEndTime(sickRequest.getEmployee().getObjectID(), sickRequest.getStartDate(), leaveDays - moneyDays, reason.getCode());
                    sickRequest.setEndDate(setEndTime(sickRequest.getEmployee().getObjectID(), sickRequest.getStartDate(), leaveDays, reason.getCode()));
                } else {
                    sickRequest.setEndDate(sickRequest.getStartDate());
                }
                sickRequestManager.createOrUpdate(sickRequest);

                populateLeaveHoursToAttendanceRawData(sickRequest, dateByPeriodMap, null, true, null);
                if (moneyDays > 0) {
                    sickRequest.setEndDate(leaveEndDateWithoutMoneyDays);
                }
            }

            if (i == 10) {
                j++;
                sickRequestManager.flushAndClear();
                System.out.println("-------------Cleared -------- " + (i * j));
                i = 0;
            }
        }
        System.out.println("-------------Done -------- ");
    }

    public NumberData generateGoalNumber(String categoryType) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = goalManager.getGoalLastIntNumber(categoryType);
        if (intNumber == null) {
            intNumber = 0;
        }
        if (settings != null && settings.getGoalNumberingFormat(categoryType) != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getGoalNumberingFormat(categoryType), settings.getDelimetrGoalNumbering(categoryType), null, null, null, "projectgoal");
            numberData.setDelimiter(settings.getDelimetrGoalNumbering(categoryType));
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, GoalCategoryEnum.getByValue(categoryType));
        }
    }

    @Transactional
    public void deleteSickRequestListByParent(Integer parentID) {
        EdsSickRequest sickRequest = sickRequestManager.get(parentID);
        if (sickRequest != null && sickRequest.getChildlist() != null && sickRequest.getChildlist().size() > 0) {
            for (EdsSickRequest child : sickRequest.getChildlist()) {
                deleteRequest(child.getObjectID());
            }
        }
        deleteRequest(parentID);
    }

    @Transactional
    public void deleteRequest(Integer id) {
        EdsSickRequest sickRequest = sickRequestManager.get(id);

        if (sickRequest == null) {
            return;
        }
        //ATTENDANCERAWDATA >> LEAVE REQUEST DELETE
        if (sickRequest.getStartDate() != null && sickRequest.getEndDate() != null) {
            List<EdsAttendanceRawData> attendanceRawDataList = attendanceRawDataManager.getAttendanceRawDataByDates(sickRequest.getStartDate(), sickRequest.getEndDate(), sickRequest.getEmployee().getObjectID());
            for (EdsAttendanceRawData attendanceRawData : attendanceRawDataList) {
                //Leave time for that day for given leave request
                int leaveMinutes = sickRequestDurationManager.getLeaveMinutes(sickRequest.getObjectID(), attendanceRawData.getDate());

                //Overall Leave time for that day
                Integer leaveM = attendanceRawData.getLeave();
                if (isOk(sickRequest.getOverallStatus()) && EdsSickRequest.NOT_DEFINED.equals(sickRequest.getOverallStatus().getCode())) {
                    //pending
                    Integer leavePending = attendanceRawData.getLeavePending();
                    attendanceRawData.setLeavePending(leavePending < leaveMinutes ? 0 : leavePending - leaveMinutes);
                } else if (isOk(sickRequest.getOverallStatus()) && EdsSickRequest.APPROVED.equals(sickRequest.getOverallStatus().getCode())) {
                    attendanceRawData.setLeave(leaveM < leaveMinutes ? 0 : leaveM - leaveMinutes);
                    //approved
                    Integer existFromAnnualLeaveTime = attendanceRawData.getFromAnnualLeaveTime();
                    int fromAnnualLeaveTime = existFromAnnualLeaveTime - (sickRequest.getToTakeFromAllowance() ? leaveMinutes : 0);
                    attendanceRawData.setFromAnnualLeaveTime(Math.max(fromAnnualLeaveTime, 0));

                    Integer existPaidTime = attendanceRawData.getPaidTime();
                    int paidTime = existPaidTime - (sickRequest.getType() != null && EdsSickRequest.PAID.equals(sickRequest.getType().getCode()) ? leaveMinutes : 0);
                    attendanceRawData.setPaidTime(Math.max(paidTime, 0));

                } else if (isOk(sickRequest.getOverallStatus()) && EdsSickRequest.DENIED.equals(sickRequest.getOverallStatus().getCode())) {
                    //rejected
                    Integer leaveDenied = attendanceRawData.getLeaveDenied();
                    attendanceRawData.setLeaveDenied(leaveDenied < leaveMinutes ? 0 : leaveDenied - leaveMinutes);
                }

            }

            //delete leave request attachments
            List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_LEAVE_REQUEST, id, id);
            List<Integer> leaveRequestAttachmentIds = new ArrayList<>();
            for (FileResource leaveRequestAttachment : attachments) {
                leaveRequestAttachmentIds.add(leaveRequestAttachment.getObjectId());
            }
            try {
                commonService.deleteFiles(leaveRequestAttachmentIds);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //register postProcessor for my Updates
            EdsUser user = userManager.getUser();
            String customFieldForSickRequest = (sickRequest.getDescription() != null ? (sickRequest.getDescription().length() > 10 ? sickRequest.getDescription().substring(0, 10) + "..." : sickRequest.getDescription()) : "") + " (" + ServerUtils.getDateAsString(sickRequest.getStartDate(), true) + "-" + ServerUtils.getDateAsString(sickRequest.getEndDate(), true) + ") ";
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(LeaveRequestEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, sickRequest, user);
            event.setCustomStringField(customFieldForSickRequest);
        }
        if (multiLeaveManager.getBySickRequest(id) != null) {
            multiLeaveManager.deleteBySickRequestForSickId(id);
        }

        List<EdsBackupEmployee> backupEmployeesBySickRequestId = backupEmployeeManager.getBackupEmployeesBySickRequestId(sickRequest.getObjectID());
        if (backupEmployeesBySickRequestId != null) {
            backupEmployeeManager.deleteBySickRequestId(sickRequest.getObjectID());
        }

        sickRequestManager.delete(sickRequest);

        if (sickRequest.getOverallStatus() == null || !EdsSickRequest.DENIED.equals(sickRequest.getOverallStatus().getCode())) {
            sickRequest.getEmployee().clear();
            updateEmployeeLeaveDuration(sickRequest.getEmployee());
        }

        try {
            solrManager.removeLeaveRequestByIds(sickRequest.getObjectID());
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSickRequest.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(id);
        ServerUtils.kpiLog(LOGGER, kpiLog, "Delete leave request");
    }

    public NewLeaveRequest validateAllowanceLimit(NewLeaveRequest sickRequest) {
        sickRequest.setValid(true);
        sickRequest.setErrorMessage("");
        EdsUser user;
        if (sickRequest.getEmployee() != null) {
            user = employeeManager.get(sickRequest.getEmployee());
        } else {
            user = employeeManager.getUser();
        }
        EdsLeaveReason reason = leaveReasonManager.get(sickRequest.getReasonId());

        if (reason == null) {
            sickRequest.setValid(false);
            sickRequest.setErrorMessage("Please select reason!");
            return sickRequest;
        }

        if (prorataEnabled(reason)) {
            EdsEmployee employee = user.getEmployee();
            if (employee.getStartDate() == null) {
                sickRequest.setValid(false);
                sickRequest.setErrorMessage("If you do not set employee hire date, the current leave balance will not be updated");
            }
        }

        if (LayoutRPC.LEAVE_REQUEST_FORM.equals(sickRequest.getFrom())) {
            sickRequest.setStartDate(sickRequest.getStartNonConverable().getNonConvertedDate());
            sickRequest.setEndDate(sickRequest.getEndNonConverable().getNonConvertedDate());
        } else {
            Calendar sDate = Calendar.getInstance();
            sDate.setTime(sickRequest.getStartNonConverable().getNonConvertedDate());
            sDate.set(Calendar.HOUR_OF_DAY, sickRequest.getStartHour());
            sDate.set(Calendar.MINUTE, sickRequest.getStartMinut());
            sDate.set(Calendar.SECOND, 0);
            sDate.set(Calendar.MILLISECOND, 0);
            sickRequest.setStartDate(sDate.getTime());

            Calendar eDate = Calendar.getInstance();
            eDate.setTime(sickRequest.getEndNonConverable().getNonConvertedDate());
            eDate.set(Calendar.HOUR_OF_DAY, sickRequest.getEndHour());
            eDate.set(Calendar.MINUTE, sickRequest.getEndMinut());
            eDate.set(Calendar.SECOND, 0);
            eDate.set(Calendar.MILLISECOND, 0);
            sickRequest.setEndDate(eDate.getTime());
        }

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(user.getObjectID());
        fp.setStartDate(sickRequest.getStartDate());
        fp.setEndDate(sickRequest.getEndDate());
        fp.setIncludeDayOff(reason.getIncludeDayOffs());

        //Key is year, Value is day required
        Map<Integer, Double> leaveDuration = getLeaveDuration(sickRequest, fp);//get current leave request required days

        EdsReference approvedStatus = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);
        EdsReference pendingStatus = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.NOT_DEFINED);
        fp.setStatusIDs(new Integer[]{approvedStatus.getObjectID(), pendingStatus.getObjectID()});
        fp.setAnnualLeave(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE.equals(reason.getCode()));
        fp.setReasonCode(reason.getCode());

        for (Map.Entry<Integer, Double> entry : leaveDuration.entrySet()) {
            int year = entry.getKey();
            Double requireDays = entry.getValue();
            fp.setYear(year);
            TypeOption typeOption = reason.getTypeOption();

            double leftPaidDays = getLeftPaidDays(fp);//left days in this year
            double diff = leftPaidDays - requireDays;
            if (diff < 0 && TypeOption.NOT_ALLOW_EXCEED_ALLOWANCE.equals(typeOption)) {
                DecimalFormat df = new DecimalFormat("0.0#");
                sickRequest.setYear(year);
                sickRequest.setValid(false);
                sickRequest.setDay(df.format(diff * (-1)) + " days");
                return sickRequest;
            }
        }
        return sickRequest;
    }

    private double getLeftPaidDaysByPeriod(Integer employeeID, String reasonCode) {
        List<EdsLabourPeriod> periodList = labourPeriodManager.periodListByEmployeeId(employeeID);
        double totalAnualLeaveByPeriod = 0d;
        double totalApprovedLeaveDaysByPeriod = 0d;
        if (periodList != null && periodList.size() > 0) {
            for (EdsLabourPeriod item : periodList) {
                EdsAnnualLeaveAllowance annualAllowence = annualLeaveAllowanceManager.getLeaveAllowanceByPeriodStartDate(employeeID, item.getStartDate(), reasonCode);
                Double totalApprovedLeaveDays = labourPeriodManager.getTotalTakenLeaveDaysByPeriodId(item.getObjectID(), true);

                totalAnualLeaveByPeriod += annualAllowence != null ? annualAllowence.getAllowanceDays() : 0d;
                totalApprovedLeaveDaysByPeriod += totalApprovedLeaveDays != null ? totalApprovedLeaveDays : 0d;
            }
        }
        return totalAnualLeaveByPeriod - totalApprovedLeaveDaysByPeriod;
    }

    private double getLeftPaidDays(ListingFilterParameter fp) {
        EdsLeaveReason reason = leaveReasonManager.findByCode(fp.getReasonCode());
        fp.setStatusID(referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED).getObjectID());
        if (prorataEnabled(reason)) {
            fp.setDate(fp.getStartDate());
            fp.setLimit(1);
            ArrayList<LeaveBalanceReport> reportList = annualLeaveAllowanceManager.getAnnnualLeaveBalanceReport(fp);

            return reportList.size() == 0 ? 0 : reportList.get(0).getCurrentBalance();

        } else {
            EdsAnnualLeaveAllowance annualAllowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(fp.getYear(), fp.getEmployeeId(), fp.getReasonCode(), null);
            double annualAllowanceDays = 0;
            if (annualAllowance != null) {

                if (EdsSickRequest.LR_TYPE_ANNUAL_LEAVE.equals(fp.getReasonCode()) && hasOpeningBalance(null, fp.getYear())) {
                    EdsEmployee employee = employeeManager.get(fp.getEmployeeId());
                    annualAllowanceDays = employee.getOpeningBalanceDays();
                } else {
                    annualAllowanceDays = annualAllowance.getAllowanceDays();
                }

            }
            Double stats = sickRequestDurationManager.getUserSpentPaidAllowance(fp);
            return annualAllowanceDays - (stats != null ? stats : 0d);
        }
    }

    private Map<String, Double[]> getEmployeeTakenDaysByReasons(ListingFilterParameter fp, List<String> reasons) {
        Map<String, Double[]> map = new HashMap<>();

        Integer statusId = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED).getObjectID();
        fp.setStatusID(statusId);
        boolean isAnnaul = false;
        boolean isSick = false;
        for (String reason : reasons) {
            if ((CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(reason) || CustomFormConstants.LR_TYPE_SICK_LEAVE.equals(reason))) {
                EdsLeaveReason annualAndSickReason = leaveReasonManager.findByCode(reason);
                if (annualAndSickReason != null) {
                    fp.setDate(fp.getStartDate());
                    fp.setLimit(1);
                    fp.setReasonCode(reason);
                    ArrayList<LeaveBalanceReport> reportList = annualLeaveAllowanceManager.getAnnnualLeaveBalanceReport(fp);

                    if (reportList != null && reportList.size() > 0) {
                        Double[] d = new Double[3];
                        d[0] = reportList.get(0).getTakenDays();
                        d[1] = reportList.get(0).getAnnualNonpaid();
                        d[2] = reportList.get(0).getCurrentBalance();
                        map.put(reason, d);
                    }
                    if (CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(reason)) {
                        isAnnaul = true;
                    }
                    if (CustomFormConstants.LR_TYPE_SICK_LEAVE.equals(reason)) {
                        isSick = true;
                    }
                }
            }
        }
        if (isAnnaul) {
            reasons.remove(CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
        }
        if (isSick) {
            reasons.remove(CustomFormConstants.LR_TYPE_SICK_LEAVE);
        }
        if (reasons.size() > 0) {
            Map<String, Double[]> stats = sickRequestDurationManager.getUserTakenDays(fp, reasons);
            map.putAll(stats);
        }
        return map;
    }

    private Map<Integer, Double> getLeaveDuration(NewLeaveRequest sickRequest, ListingFilterParameter fp) {
        HashMap<Integer, Double> leaveDuration = new HashMap<>();

        EdsEmployee edsEmployee = employeeManager.get(fp.getEmployeeId());

        HashMap<Date, EdsTimeSlotItem> exceptionalTimeSlotItem = edsEmployee.getTimeSlot().getExceptionalTimeSlotItem();

        Map<Integer, Integer[]> timeSlotItemMap = getTimeslotMinutes(edsEmployee, fp.isIncludeDayOff(), fp.getReasonCode());

        Calendar sickRequestStart = GregorianCalendar.getInstance();
        sickRequestStart.setTime(sickRequest.getStartDate());

        List<Date> holidays = attendanceRawDataManager.getHolidayDays(fp);

        boolean includeDayOff = false;
        if (fp.getReasonCode() != null) {
            EdsLeaveReason reason = leaveReasonManager.getReasonByName(null, fp.getReasonCode());
            includeDayOff = reason != null && reason.getIncludeDayOffs();
        }
        while (sickRequestStart.getTime().getTime() <= sickRequest.getEndDate().getTime()) {
            boolean isHoliday = false;
            for (Date holiday : holidays) {
                if (ServerUtils.dateEqual(holiday, sickRequestStart.getTime()) && !includeDayOff) {
                    isHoliday = true;
                }
            }
            int dayOfWeek = sickRequestStart.get(Calendar.DAY_OF_WEEK) - 1;

            Calendar calendar = (Calendar) sickRequestStart.clone();
            ServerUtils.setBeginningOfTheDay(calendar);

            boolean isExceptionalDate = false;
            int exceptionalStartDay = 0;
            int exceptionalEndDay = 0;
            int exceptionalLunchStartDay = 0;
            int exceptionalLunchEndDay = 0;
            int exceptionalCoffeeStartDay = 0;
            int exceptionalCoffeeEndDay = 0;

            if (exceptionalTimeSlotItem.containsKey(calendar.getTime())) {
                isExceptionalDate = true;
                exceptionalStartDay = exceptionalTimeSlotItem.get(calendar.getTime()).getStartTime();
                exceptionalEndDay = exceptionalTimeSlotItem.get(calendar.getTime()).getEndTime();
                exceptionalLunchStartDay = exceptionalTimeSlotItem.get(calendar.getTime()).getLunchStart();
                exceptionalLunchEndDay = exceptionalTimeSlotItem.get(calendar.getTime()).getLunchEnd();
                exceptionalCoffeeStartDay = exceptionalTimeSlotItem.get(calendar.getTime()).getCoffeeStart();
                exceptionalCoffeeEndDay = exceptionalTimeSlotItem.get(calendar.getTime()).getCoffeeEnd();
            }

            if (!isHoliday) {
                Double requiredLeaveDays = 0d;
                boolean startTimeDayNotEqualEndTimeDay = isExceptionalDate ? (exceptionalStartDay != exceptionalEndDay) : !timeSlotItemMap.get(dayOfWeek)[timeslotStart].equals(timeSlotItemMap.get(dayOfWeek)[timeslotEnd]);

                if (startTimeDayNotEqualEndTimeDay) {

                    if (sickRequest.isAllDay()) {
                        requiredLeaveDays = 1d;
                    } else {
                        int timeSlotStartTime = isExceptionalDate ? exceptionalStartDay : timeSlotItemMap.get(dayOfWeek)[timeslotStart];
                        int timeSlotEndTime = isExceptionalDate ? exceptionalEndDay : timeSlotItemMap.get(dayOfWeek)[timeslotEnd];
                        int lunchStartTime = isExceptionalDate ? exceptionalLunchStartDay : timeSlotItemMap.get(dayOfWeek)[lunchStart];
                        int lunchEndTime = isExceptionalDate ? exceptionalLunchEndDay : timeSlotItemMap.get(dayOfWeek)[lunchEnd];
                        int coffeeStartTime = isExceptionalDate ? exceptionalCoffeeStartDay : timeSlotItemMap.get(dayOfWeek)[coffeeStart];
                        int coffeeEndTime = isExceptionalDate ? exceptionalCoffeeEndDay : timeSlotItemMap.get(dayOfWeek)[coffeeEnd];

                        Calendar timeSlotStart = (Calendar) sickRequestStart.clone();
                        timeSlotStart.set(Calendar.HOUR_OF_DAY, timeSlotStartTime / 60);
                        timeSlotStart.set(Calendar.MINUTE, timeSlotStartTime % 60);

                        Calendar timeSlotEnd = (Calendar) sickRequestStart.clone();
                        timeSlotEnd.set(Calendar.HOUR_OF_DAY, timeSlotEndTime / 60);
                        timeSlotEnd.set(Calendar.MINUTE, timeSlotEndTime % 60);

                        Calendar lunchStart = (Calendar) sickRequestStart.clone();
                        lunchStart.set(Calendar.HOUR_OF_DAY, lunchStartTime / 60);
                        lunchStart.set(Calendar.MINUTE, lunchStartTime % 60);
                        Calendar lunchEnd = (Calendar) sickRequestStart.clone();
                        lunchEnd.set(Calendar.HOUR_OF_DAY, lunchEndTime / 60);
                        lunchEnd.set(Calendar.MINUTE, lunchEndTime % 60);

                        Calendar coffeeStart = (Calendar) sickRequestStart.clone();
                        coffeeStart.set(Calendar.HOUR_OF_DAY, coffeeStartTime / 60);
                        coffeeStart.set(Calendar.MINUTE, coffeeStartTime % 60);
                        Calendar coffeeEnd = (Calendar) sickRequestStart.clone();
                        coffeeEnd.set(Calendar.HOUR_OF_DAY, coffeeEndTime / 60);
                        coffeeEnd.set(Calendar.MINUTE, coffeeEndTime % 60);

                        Calendar sickRequestEnd = Calendar.getInstance();
                        if (sickRequest.getEndDate().getTime() >= timeSlotEnd.getTime().getTime()) {
                            sickRequestEnd = (Calendar) timeSlotEnd.clone();
                        } else {
                            sickRequestEnd.setTime(sickRequest.getEndDate());
                        }
                        int requiredLeaveMinutes = getLeaveMinutes(sickRequestStart, timeSlotStart, timeSlotEnd, lunchStart, lunchEnd, coffeeStart, coffeeEnd, sickRequestEnd);
                        int timeSlot = timeSlotEndTime - timeSlotStartTime - (coffeeEndTime - coffeeStartTime) - (lunchEndTime - lunchStartTime);
                        requiredLeaveDays = (double) requiredLeaveMinutes / timeSlot; //in days
                    }
                }
                int year = sickRequestStart.get(Calendar.YEAR);
                if (leaveDuration.containsKey(year)) {
                    leaveDuration.put(year, leaveDuration.get(year) + requiredLeaveDays);
                } else {
                    leaveDuration.put(sickRequestStart.get(Calendar.YEAR), requiredLeaveDays);
                }
            }

            incrementDay(sickRequestStart, exceptionalTimeSlotItem, timeSlotItemMap);
        }
        return leaveDuration;
    }

    /**
     * Get new Leave Request parameters
     *
     * @param employeeID - leave request owner's ID
     * @return - new Leave Request parameters
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewLeaveRequest getLeaveRequestRPC(Integer employeeID) {

        NewLeaveRequest leaveRequest = new NewLeaveRequest();
        if (employeeID == null || employeeID == 0) {
            employeeID = userManager.getUser().getObjectID();
        }
        EdsEmployee employee = employeeManager.get(employeeID);
        leaveRequest.setEmployee(employeeID);
        leaveRequest.setEmployeeStartDate(employee.getStartDate());
        String code = employee.getProfile().getEmployeeCode();
        String employeeName = (code != null && !"".equals(code) ? code + " - " : "") + employee.getName();
        leaveRequest.setEmployeeName(employeeName);
        leaveRequest.setNumberData(generateLeaveRequestNumber());
        TimeSlot employeeTimeSlot = getEmployeeTimeSlot(employeeID, null);
        leaveRequest.setTimeSlot(employeeTimeSlot);

        boolean isPolicyEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_LEAVE_REQUEST_POLICY);
        if (isPolicyEnabled) {
            EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(employee.getCompany().getObjectID());
            leaveRequest.setPolicy(settings.getLrPolicy());
        }

        leaveRequest.setReasons(getReasons(employeeID));

        return leaveRequest;
    }

    private static final Integer timeslotEnd = 1;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getReasons(Integer userId) {
        return getReasons(userId, true);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getReasons(Integer userId, boolean withDrafts) {
        List<SelectItem> list = new ArrayList<>();
        List<EdsLeaveReason> leaveReasons = getUserLeaveReasonList(userId, withDrafts);
        for (EdsLeaveReason reason : leaveReasons) {
            if (!reason.getCode().equals("LR_TYPE_RESIGNED") && !reason.getCode().equals("LR_TYPE_HOLIDAY") && !reason.getCode().equals("LR_TYPE_DAY_OFF") && !reason.getCode().equals("TIMESLOT_NOT_STARTED") && !reason.getCode().equals("NO_CHECK_IN") && !reason.getCode().equals("LATE") && !reason.getCode().equals("EARLY_LEAVE")) {
                String value = reason.getName() != null ? reason.getName() : reason.getRealName();
                SelectItem item = new SelectItem(reason.getObjectID(), value, reason.getCode(), reason.getRedirectUrl());
                String color = reason.getColor();
                if (color != null) {
                    item.setColorHex(reason.getColor().replace("#", ""));
                }
                list.add(item);
            }
        }
        return list.toArray(new SelectItem[0]);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getReasonsWithLimit(Integer userId, boolean withDrafts, String searchText, Integer limit) {
        List<SelectItem> list = new ArrayList<>();
        List<EdsLeaveReason> leaveReasons = getUserLeaveReasonList(userId, withDrafts, searchText, limit);
        for (EdsLeaveReason reason : leaveReasons) {
            if (!reason.getCode().equals("LR_TYPE_RESIGNED") && !reason.getCode().equals("LR_TYPE_HOLIDAY") && !reason.getCode().equals("LR_TYPE_DAY_OFF") && !reason.getCode().equals("TIMESLOT_NOT_STARTED") && !reason.getCode().equals("NO_CHECK_IN") && !reason.getCode().equals("LATE") && !reason.getCode().equals("EARLY_LEAVE")) {
                String value = reason.getName() != null ? reason.getName() : reason.getRealName();
                SelectItem item = new SelectItem(reason.getObjectID(), value, reason.getCode(), reason.getRedirectUrl());
                String color = reason.getColor();
                if (reason.getUnitType() != null) {
                    item.setParam(reason.getUnitType().getName());
                }
                if (color != null) {
                    item.setColorHex(reason.getColor().replace("#", ""));
                }
                list.add(item);
            }
        }
        return list.toArray(new SelectItem[0]);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getReasons(Integer userId, Integer year) {
        EdsUser user = userManager.get(userId);
        EdsEmployee employee = user.getEmployee();
        List<SelectItem> list = new ArrayList<>();
        List<EdsLeaveReason> leaveReasons = getUserLeaveReasonList(userId, true);
        Map<String, Double> allowanceMap = annualLeaveAllowanceManager.getLeaveEmployeeAllowance(year, employee.getObjectID());

        leaveReasons.forEach(r -> {
            if (allowanceMap.get(r.getCode()) != null && allowanceMap.containsKey(r.getCode()) && allowanceMap.get(r.getCode()) <= 0d) {
                allowanceMap.remove(r.getName());
            }
        });

        for (EdsLeaveReason reason : leaveReasons) {
            if (!reason.getCode().equals("LR_TYPE_RESIGNED") && !reason.getCode().equals("LR_TYPE_HOLIDAY") && !reason.getCode().equals("TIMESLOT_NOT_STARTED") && !reason.getCode().equals("NO_CHECK_IN") && !reason.getCode().equals("LATE") && !reason.getCode().equals("EARLY_LEAVE") && !reason.getCode().equals("LR_TYPE_DAY_OFF")
                    && allowanceMap.get(reason.getCode()) != null && allowanceMap.get(reason.getCode()) > 0d) {
                String value = reason.getName() != null ? reason.getName() : reason.getRealName();
                SelectItem item = new SelectItem(reason.getObjectID(), value, reason.getCode(), reason.getRedirectUrl());
                if (reason.getColor() != null) {
                    item.setColorHex(reason.getColor().replace("#", ""));
                }
                if (reason.getCode() != null) {
                    item.setCode(reason.getCode());
                }
                list.add(item);
            }
        }
        return list.toArray(new SelectItem[0]);
    }

    private List<EdsLeaveReason> getUserLeaveReasonList(Integer userId, boolean withDrafts, String searchText, Integer limit) {
        Integer currentUserId = userManager.getUser() != null ? userManager.getUser().getObjectID() : userId;
        if (userId == null) {
            userId = currentUserId;
        }

        ArrayList<EdsLeaveReason> result = new ArrayList<>();
        EdsEmployee creator = employeeManager.get(currentUserId);
        EdsEmployee user = employeeManager.get(userId);


        List<EdsLeaveReason> leaveReasons = withDrafts ? leaveReasonManager.listActiveReasonsByLimit(user.getProfile().getGender() != null ? user.getProfile().getGender().toUpperCase() : null, searchText, limit) :
                leaveReasonManager.listActiveReasonsByGenderWithoutDrafts(user.getProfile().getGender() != null ? user.getProfile().getGender().toUpperCase() : null);
        Map<String, List<EdsLeaveReasonRelation>> leaveReasonRelations = leaveReasonRelationManager.getRelationsAsMap();
        for (EdsLeaveReason reason : leaveReasons) {

            boolean hasAccess = true;

            List<EdsLeaveReasonRelation> relations = leaveReasonRelations.get(reason.getCode());
            if (relations != null) {
                relations = relations.stream()
                        .filter(r -> !LeaveReasonType.LOCATION.equals(r.getRelatedType()))
                        .collect(java.util.stream.Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(relations)) {
                hasAccess = getReasonAccess(reason, relations, creator, user);
            }
            if (hasAccess) {
                if (!reason.getCode().equals("LR_TYPE_RESIGNED") && !reason.getCode().equals("LR_TYPE_HOLIDAY") && !reason.getCode().equals("LR_TYPE_DAY_OFF") && !reason.getCode().equals("TIMESLOT_NOT_STARTED") && !reason.getCode().equals("NO_CHECK_IN") && !reason.getCode().equals("LATE") && !reason.getCode().equals("EARLY_LEAVE")) {
                    result.add(reason);
                }
            }
        }

        return result;
    }

    private List<EdsLeaveReason> getUserLeaveReasonList(Integer userId, boolean withDrafts) {
        Integer currentUserId = userManager.getUser() != null ? userManager.getUser().getObjectID() : userId;
        if (userId == null) {
            userId = currentUserId;
        }

        ArrayList<EdsLeaveReason> result = new ArrayList<>();
        EdsEmployee creator = employeeManager.get(currentUserId);
        EdsEmployee user = employeeManager.get(userId);

        List<EdsLeaveReason> leaveReasons = withDrafts ? leaveReasonManager.listActiveReasonsByGender(user.getProfile().getGender() != null ? user.getProfile().getGender().toUpperCase() : null) :
                leaveReasonManager.listActiveReasonsByGenderWithoutDrafts(user.getProfile().getGender() != null ? user.getProfile().getGender().toUpperCase() : null);
        Map<String, List<EdsLeaveReasonRelation>> leaveReasonRelations = leaveReasonRelationManager.getRelationsAsMap();
        for (EdsLeaveReason reason : leaveReasons) {

            boolean hasAccess = true;

            List<EdsLeaveReasonRelation> relations = leaveReasonRelations.get(reason.getCode());
            if (relations != null) {
                relations = relations.stream()
                        .filter(r -> !LeaveReasonType.LOCATION.equals(r.getRelatedType()))
                        .collect(java.util.stream.Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(relations)) {
                hasAccess = getReasonAccess(reason, relations, creator, user);
            }
            if (hasAccess) {
                if (!reason.getCode().equals("LR_TYPE_RESIGNED") && !reason.getCode().equals("LR_TYPE_HOLIDAY") && !reason.getCode().equals("LR_TYPE_DAY_OFF") && !reason.getCode().equals("TIMESLOT_NOT_STARTED") && !reason.getCode().equals("NO_CHECK_IN") && !reason.getCode().equals("LATE") && !reason.getCode().equals("EARLY_LEAVE")) {
                    result.add(reason);
                }
            }
        }

        return result;
    }

    private boolean getReasonAccess(EdsLeaveReason leaveReason, List<EdsLeaveReasonRelation> relations, EdsEmployee creator, EdsEmployee user) {
        Set<String> roleList = creator.getRoleCODEs();
        boolean hasRoleAccess = false;
        boolean hasAccess = false;

        if (relations != null && relations.size() > 0) {
            for (EdsLeaveReasonRelation relation : relations) {
                { //Contains role and employee
                    if (LeaveReasonType.ROLE.equals(relation.getRelatedType())) {
                        EdsRole role = roleManager.get(relation.getRelationId());
                        if (role != null && role.isActive() && roleList.contains(role.getCode())) {
                            hasRoleAccess = true;
                            continue;
                        }
                    }
                    if (LeaveReasonType.EMPLOYEE.equals(relation.getRelatedType())) {
                        EdsEmployee edsEmployee = employeeManager.get(relation.getRelationId());
                        if (edsEmployee != null && edsEmployee.equals(user)) {
                            hasRoleAccess = true;
                            continue;
                        }
                    }
                }
                {
                    if (LeaveReasonType.DEPARTMENT.equals(relation.getRelatedType())) {
                        EdsDepartment department = departmentManager.get(relation.getRelationId());
                        if (department != null && !department.getDeleted() && department.equals(user.getTeam())) {
                            hasAccess = true;
                            continue;
                        }
                    }
                    if (LeaveReasonType.POSITION.equals(relation.getRelatedType())) {
                        EdsPosition position = positionManager.get(relation.getRelationId());
                        if (position != null && !position.isDeleted() && position.equals(user.getPosition())) {
                            hasAccess = true;
                            continue;
                        }
                    }

                }
            }
        }
        return hasRoleAccess || hasAccess;
    }

    @Override
    public HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> getEmployeesMap(ListingFilterParameter filterParameter) {
        return reportService.getEmployeesMap(filterParameter, LayoutRPC.LEAVE_REQUEST_FORM);
    }

    private void atendanceReportLeave(EdsSickRequest sickRequest, EdsReference approvedStatus, EdsReference submittedStatus) {
        EdsApprover edsApprover = new EdsApprover();
        edsApprover.setObjectID(null);
        edsApprover.setEntityID(sickRequest.getObjectID());
        edsApprover.setIs_default(false);
        if (sickRequest.getLeaveReason() != null && (sickRequest.getLeaveReason().getAutoApprove() || EdsSickRequest.LR_TYPE_UNAUTHORIZED_LEAVE.equals(sickRequest.getLeaveReason().getCode()))) {
            edsApprover.setStatus(approvedStatus);
            sickRequest.setEntityStatus(approvedStatus);
        } else {
            edsApprover.setStatus(submittedStatus);
            sickRequest.setEntityStatus(submittedStatus);
        }
        edsApprover.setExactEmployee(userManager.getUser());
        edsApprover.setApproverRoles(new HashSet<>());
        edsApprover.setApproverEmployees(new HashSet<>());
        edsApprover.setDynamicQueries(new HashSet<>());
        approverManager.create(edsApprover);
        if (sickRequest.getCurrentApprover() == null) {
            sickRequest.setCurrentApprover(edsApprover);
        }
        sickRequest.getApprovers().add(edsApprover);
    }

    @Transactional
    public EdsSickRequestCustomFields saveCustomFields(EdsSickRequestCustomFields edsSickRequestCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsSickRequestCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue())) || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0) || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsSickRequestCustomFields = new EdsSickRequestCustomFields();
                sickRequestCFManager.create(edsSickRequestCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsSickRequestCustomFields, customFieldItems);
            return edsSickRequestCustomFields;
        }
        return null;
    }

    public void updateEmployeeLeaveDuration(EdsUser user) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(user.getObjectID());
        Calendar c = GregorianCalendar.getInstance();
        fp.setYear(c.get(Calendar.YEAR));
        HashMap<Integer, Double> duration = sickRequestDurationManager.getEmployeeLeaveDurations(fp);
        Double durations = duration.get(user.getEmployee().getObjectID());
        if (durations != null) {
            user.getEmployee().setLeaveDurationDay(durations);
            employeeManager.update(user.getEmployee());
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, EVENT_TYPE_EDIT, user.getEmployee(), employeeManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_EMPLOYEE);
        }
    }

    /**
     * Register add leave request message sent to employee
     *
     * @param toUser  - to user
     * @param request - leave request
     */
    private void sendMessageToEmployeeAddLeaveRequest(EdsUser toUser, EdsSickRequest request) {
        try {
            messageManager.sendSickRequestNotificationToSelectedEmployee(toUser, request);
        } catch (EdsTemplateException e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public StatisticsLeaveRequest getLeaveRequest(Integer requestID) {
        EdsSickRequest sickRequest = requestID != null ? sickRequestManager.get(requestID) : null;
        if (sickRequest == null) {
            return null;
        }
        Date endDateByChild = null;
        List<EdsSickRequest> childList = sickRequestManager.getLeaveRequestByParentId(requestID);
        if (childList != null && childList.size() > 0) {
            for (EdsSickRequest child : childList) {
                if (!child.isTakeByMoney()) {
                    endDateByChild = child.getEndDate();
                }
            }
        }
        EdsUser user = employeeManager.getUser();
        Map<Integer, Double[]> duration = sickRequestDurationManager.getEmployeesLeaveRequestsDuration(Collections.singletonList(sickRequest));
        Double[] stat = duration.get(sickRequest.getObjectID());

        EdsEmployee sickRequestEmployee = sickRequest.getEmployee();
        StatisticsLeaveRequest leaveRequest = new StatisticsLeaveRequest();
        leaveRequest.setEmployeeStartDate(sickRequestEmployee.getStartDate());
        leaveRequest.setObjectID(sickRequest.getObjectID());
        leaveRequest.setFrom(sickRequest.getFromApi());
        leaveRequest.setApproverName(sickRequest.getCurrentApprover() != null ? sickRequest.getCurrentApprover().getExactEmployee().getName() : "");
        if (sickRequest.getBackupEmployees() != null) {
            ArrayList<BackupEmployeeItem> backupEmployeeItemList = new ArrayList<>();
            List<EdsBackupEmployee> backupEmployeesBySickRequestId = backupEmployeeManager.getBackupEmployeesBySickRequestId(sickRequest.getObjectID());
            List<EdsBackupEmployee> parentBackups = new ArrayList<>();
            for (EdsBackupEmployee backupEmployee : backupEmployeesBySickRequestId) {
                if (backupEmployee.getParentId() == null) {
                    parentBackups.add(backupEmployee);
                }
            }
            for (EdsBackupEmployee parentBackup : parentBackups) {
                BackupEmployeeItem backupEmployeeItem = new BackupEmployeeItem();
                ApproverItemMini parent = getApproverItemByBackupEmployee(parentBackup);
                backupEmployeeItem.setParentBackupEmployee(parent);
                List<EdsBackupEmployee> childrensByParentId = backupEmployeeManager.getChildrensByParentId(parentBackup.getObjectID());
                backupEmployeeItem.getChildList().add(parent);
                for (EdsBackupEmployee backupEmployee : childrensByParentId) {
                    backupEmployeeItem.getChildList().add(getApproverItemByBackupEmployee(backupEmployee));
                }
                backupEmployeeItemList.add(backupEmployeeItem);

            }
            leaveRequest.setBackupEmployee(backupEmployeeItemList);
        }
        leaveRequest.setCreator(sickRequest.getRegisteredBy() != null ? sickRequest.getRegisteredBy().getFullName() : "");
        leaveRequest.setCreatorPosition(sickRequest.getRegisteredBy() != null && sickRequest.getRegisteredBy().getPosition() != null ? sickRequest.getRegisteredBy().getPosition().getName() : "");
        if (sickRequest.getRegisteredBy() != null && sickRequest.getRegisteredBy().getEmployeeDepartment() != null && sickRequest.getRegisteredBy().getEmployeeDepartment().getTeam() != null) {
            leaveRequest.setCreatorDepartment(sickRequest.getRegisteredBy().getEmployeeDepartment().getTeam().getName());
        }
        if (sickRequest.getCurrentApprover() != null && sickRequest.getCurrentApprover().getExactEmployee() != null) {
            EdsUser approver = sickRequest.getCurrentApprover().getExactEmployee();
            leaveRequest.setCurrectApproverId(approver.getObjectID());
        }
        leaveRequest.setTakeByMoney(sickRequest.isTakeByMoney());

        if (sickRequest.getNumberData() != null) {
            EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();

            int intNumber = sickRequest.getIntNumber() != null ? sickRequest.getIntNumber() : 1;

            NumberData numberData = numberingSettings.parseNumberDataForALL(intNumber, numberingSettings.getLeaveRequestNumberingFormat(), numberingSettings.getDelimetrLeaveRequestNumbering(), null, null, null, "leaveRequest");
            numberData.setNumberString(sickRequest.getNumberData());
            numberData.setNumberFormat(
                    numberingSettings.getLeaveRequestNumberingFormat() != null ?
                            numberingSettings.getLeaveRequestNumberingFormat() : EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_LEAVE_REQUEST_PREFIX).getNumberFormat());
            numberData.setIntNumber(intNumber);
            leaveRequest.setNumberData(numberData);
        }
        if (sickRequest.getOverallStatus() != null) {
            leaveRequest.setOverallStatus(sickRequest.getOverallStatus().getRPC());
        }
        {
            if (sickRequest.getCurrentApprover() != null) {
                Set<String> roles = user.getRoleCODEs();
                if (roles.contains(EdsRole.TL_CODE)) {
                    roles.add(Constants.DLOFPR);
                }
                if (roles.contains(EdsRole.PM_CODE)) {
                    roles.add(Constants.PMOFPR);
                    roles.add(Constants.BMOFPR);
                }
                for (EdsApprover approver : sickRequest.getApprovers()) {
                    for (EdsApproverRoles edsApproverRoles : approver.getApproverRoles()) {
                        EdsRole edsRole = edsApproverRoles.getRole();
                        if (roles.contains(edsRole.getCode())) {
                            if (edsApproverRoles.getApproveForAll()) {
                                leaveRequest.setApproveForAll(true);
                                break;
                            }
                        }
                    }
                }
                if (!leaveRequest.isApproveForAll()) {
                    for (EdsApprover approver : sickRequest.getApprovers()) {
                        for (EdsApproverEmployees edsApproverEmployees : approver.getApproverEmployees()) {
                            if (user.equals(edsApproverEmployees.getEmployee()) && edsApproverEmployees.getApproveForAll()) {
                                leaveRequest.setApproveForAll(true);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (sickRequest.getType() != null) {
            leaveRequest.setTypeId(sickRequest.getType().getObjectID());
            leaveRequest.setTypeCode(sickRequest.getType().getCode());
        }
        leaveRequest.setEmployee(sickRequestEmployee.getFullName());
        leaveRequest.setEmployeeId(sickRequestEmployee.getObjectID());
        leaveRequest.setDepartment((sickRequestEmployee.getEmployeeTeam() != null && sickRequestEmployee.getEmployeeTeam().getTeam() != null) ? sickRequestEmployee.getEmployeeTeam().getTeam().getName() : "");

        if (sickRequest.getLeaveReason() != null) {
            leaveRequest.setReason(sickRequest.getLeaveReason().getName());
            leaveRequest.setReasonId(sickRequest.getLeaveReason().getObjectID());
            leaveRequest.setReasonCode(sickRequest.getLeaveReason().getCode());
            leaveRequest.setIncludeDayOffs(sickRequest.getLeaveReason().getIncludeDayOffs());
        }
        leaveRequest.setReasons(getReasons(sickRequestEmployee.getObjectID()));
        leaveRequest.setDescription(sickRequest.getDescription());

        boolean hideTime = false;
        if (stat != null && stat.length > 0) {
            hideTime = (stat[1] % 1) == 0 && stat[1] != 0d;
        }
        String paidDays = ServerUtils.getLeaveDayFormat(stat, paid);
        String nonPaid = ServerUtils.getLeaveDayFormat(stat, non_paid);
        String leaveDays = "";
        if (StringUtils.isNotBlank(paidDays)) {
            leaveDays = paidDays;
            leaveRequest.setType(commonLocalizer.localize("paid"));
        }
        if (StringUtils.isNotBlank(nonPaid)) {
            if (StringUtils.isNotBlank(leaveDays)) {
                leaveDays = leaveDays + "/" + nonPaid;
                leaveRequest.setType(commonLocalizer.localize("paidNonPaid"));
            } else {
                leaveDays = nonPaid;
                leaveRequest.setType(commonLocalizer.localize("nonPaid"));
            }
        }
        leaveRequest.setDuration(leaveDays);
        leaveRequest.setHideTime(hideTime);
        leaveRequest.setCreatedDate(sickRequest.getCreatedDate());
        leaveRequest.setStartDDate(sickRequest.getStartDate() != null ? new DateNonConvertable(sickRequest.getStartDate()) : null);
        leaveRequest.setEndDDate(endDateByChild != null ? new DateNonConvertable(endDateByChild) : sickRequest.getEndDate() != null ? new DateNonConvertable(sickRequest.getEndDate()) : null);
        leaveRequest.setRecallDDate(sickRequest.getRecallDate() != null ? new DateNonConvertable(sickRequest.getRecallDate()) : null);
        if (roleManager.hasRoles(user, EdsRole.DR) || roleManager.hasRoles(user, EdsRole.ADMIN) || (sickRequest.getCurrentApprover() != null && sickRequest.getCurrentApprover().getExactEmployee() != null && sickRequest.getCurrentApprover().getExactEmployee().getObjectID().equals(user.getObjectID()))) {
            leaveRequest.setAction(!isOk(sickRequest.getOverallStatus()) || !(sickRequest.getOverallStatus().getCode().equals(EdsSickRequest.APPROVED) && sickRequest.getOverallStatus().getCode().equals(EdsSickRequest.DENIED)));
        } else {
            leaveRequest.setAction(false);
        }

        sickRequest.initApproverData(leaveRequest);

        FileResource leaveRequestAttachment = null;
        List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_LEAVE_REQUEST, requestID, requestID);
        if (attachments != null && attachments.size() > 0) {
            leaveRequestAttachment = attachments.get(0);
        }
        leaveRequest.setFileResource(leaveRequestAttachment);
        leaveRequest.setLeaveRequestComment(getComments(sickRequest.getObjectID()));
        leaveRequest.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(sickRequest.getCustomFields(), commonService.getCompanyCustomFields(ViewName.LeaveRequest)));
        ArrayList<MultiLeaveDTO> list = new ArrayList<>();
        List<Object[]> bySickRequest = labourPeriodManager.getDayTypesByPeriod(requestID);
        MultiLeaveDTO dto;
        for (Object[] item : bySickRequest) {
            dto = new MultiLeaveDTO();
            dto.setPeriodId((Integer) item[0]);
            dto.setSickRequestType((String) item[1]);
            dto.setSickRequestDuration(((BigDecimal) item[2]).intValue());
            dto.setSickID(requestID);
            EdsLabourPeriod period = labourPeriodManager.get((Integer) item[0]);
            String periodStartDate = ServerUtils.shortDateFormat(period.getStartDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
            String periodEndDate = ServerUtils.shortDateFormat(period.getEndDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
            String periodItem = String.format((Locale) null, "%1$s%n%2$s", periodStartDate, periodEndDate);
            dto.setLaborPeriod(periodItem);

            list.add(dto);
        }
        leaveRequest.setMultiLeaveList(list);
        return leaveRequest;
    }

    private ApproverItemMini getApproverItemByBackupEmployee(EdsBackupEmployee backupEmployee) {
        ApproverItemMini child = new ApproverItemMini();
        child.setObjectID(backupEmployee.getObjectID());
        child.setVozlojeniya(true);
        child.setExactEmployee(backupEmployee.getEmployee().getAsSelectItem());
        child.setFromBackupEmployeeDate(new DateNonConvertable(backupEmployee.getStartDate()));
        child.setDueBackupEmployeeDate(new DateNonConvertable(backupEmployee.getDueDate()));
        return child;
    }

    public void updateMultipleRequests(String code, ArrayList<Integer> selectItems, String rejectionReason) {
        for (Integer id : selectItems) {
            updateApprove(code, id, false, rejectionReason);
        }
    }

    public void updateApprove(String status, Integer requestID, boolean approvalForAll) {
        updateApprove(status, requestID, approvalForAll, null);
    }

    public void updateApprove(String status, Integer requestID, boolean approvalForAll, String rejectionReason) {
        updateApprove(status, requestID, approvalForAll, rejectionReason, false);
    }

    @Transactional
    public void updateApprove(String status, Integer requestID, boolean approvalForAll, String rejectionReason, boolean fromApi) {
        EdsSickRequest sickRequest = sickRequestManager.get(requestID);
        List<EdsSickRequest> childSickRequestList = sickRequestManager.getLeaveRequestByParentId(requestID);
        if (sickRequest.getOverallStatus() != null && !fromApi) {
            if (!sickRequest.getOverallStatus().getCode().equals(LR_STATUS_NOT_DEFINED)) {
                return;
            }
        }

        EdsReference statusReference = referenceManager.findReference(EdsSickRequest._SICK_STATUS, status);
        if (statusReference.getCode().equals(LR_STATUS_SS_APPROVED) && approvalForAll) {
            sickRequest.setOverallStatus(statusReference);
            if (childSickRequestList != null && childSickRequestList.size() > 0) {
                for (EdsSickRequest request : childSickRequestList) {
                    EdsMultiLeave multiLeave = multiLeaveManager.getBySickRequest(request.getObjectID());
                    if (multiLeave.getSickRequestType() != null) {
                        EdsSickRequest childSickRequest = sickRequestManager.get(request.getObjectID());
                        childSickRequest.setOverallStatus(statusReference);
                        sickRequestManager.update(childSickRequest);
                    }
                }
            }
        }
        if (childSickRequestList != null && childSickRequestList.size() > 0) {
            for (EdsSickRequest request : childSickRequestList) {
                EdsMultiLeave multiLeave = multiLeaveManager.getBySickRequest(request.getObjectID());
                if (multiLeave.getSickRequestType() != null) {
                    EdsSickRequest childSickRequest = sickRequestManager.get(request.getObjectID());
                    childSickRequest.setOverallStatus(statusReference);
                    sickRequestManager.update(childSickRequest);
                }
            }
        }
        sickRequest.updateStatus(statusReference);
        sickRequest.setRejectionReason(rejectionReason);

        sickRequestManager.update(sickRequest);

        allInOneServiceLocal.approvedOrRejected(RelationItem.TYPE_LEAVE_REQUEST, sickRequest.getObjectID(), null);
        if (isOk(sickRequest.getOverallStatus()) && EdsSickRequest.APPROVED.equals(sickRequest.getOverallStatus().getCode())) {
            if (childSickRequestList != null && childSickRequestList.size() > 0) {
                for (EdsSickRequest request : childSickRequestList) {
                    EdsMultiLeave multiLeave = multiLeaveManager.getBySickRequest(request.getObjectID());
                    if (multiLeave.getSickRequestType() != null && !Constants.MONEY.equals(multiLeave.getSickRequestType())) {
                        updateAttendanceRawDataLR(request);
                    }
                }
            }
            updateAttendanceRawDataLR(sickRequest);
            if (sickRequest.getLeaveReason() != null && CustomFormConstants.LR_TYPE_SICK_LEAVE.equals(sickRequest.getLeaveReason().getCode()) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_RETURN_LEAVE_REQUEST)) {
                deleteLeaveRequestDuration(sickRequest);
                updateEmployeeLeaveDuration(sickRequest.getEmployee());
            }
        }
        if (LR_STATUS_SS_DENIED.equals(status)) {
            if (childSickRequestList != null && childSickRequestList.size() > 0) {
                for (EdsSickRequest request : childSickRequestList) {
                    EdsMultiLeave multiLeave = multiLeaveManager.getBySickRequest(request.getObjectID());
                    if (multiLeave.getSickRequestType() != null && !Constants.MONEY.equals(multiLeave.getSickRequestType())) {
                        request.getEmployee().clear();
                        updateEmployeeLeaveDuration(request.getEmployee());
                    }
                }
            }
            sickRequest.getEmployee().clear();
            updateEmployeeLeaveDuration(sickRequest.getEmployee());
        }
        createLeaveRequestHistory(requestID, new HistoryListItem(status.equals(LR_STATUS_SS_APPROVED) ? "approved" : "rejectionReason:" + rejectionReason));

        try {
//            solrManager.addleaveRequestToIndex(sickRequest);
            leaveRequestSolrComponent.index(sickRequest);
        } catch (InterruptedException | SolrServerException | IOException e) {
            e.printStackTrace();
        }

        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, EVENT_TYPE_EDIT, sickRequest, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_LEAVE_REQUEST);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSickRequest.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(sickRequest.getObjectID());
        ServerUtils.kpiLog(LOGGER, kpiLog, "Update leave request status");
    }

    private void deleteLeaveRequestDuration(EdsSickRequest request) { // this method deletes leave request duration only particular rows
        if (request.getStartDate() != null && request.getEndDate() != null && request.getEmployee().getObjectID() != null) {
            Date startDate = request.getStartDate();
            Date endDate;
            HashMap<Integer, Double> requestDurations = sickRequestDurationManager.getDurationByDateAndEmployeeId(request.getStartDate(), request.getEndDate(), request.getEmployee().getObjectID());
            for (Integer requestId : requestDurations.keySet()) {
                EdsMultiLeave child = multiLeaveManager.getBySickRequest(requestId);
                Double count = requestDurations.get(requestId);
                endDate = ServerUtils.addDays(startDate, count.intValue());

                if (child != null && !child.getSickRequestType().equals(Constants.MONEY)) {
                    Double totalDaysFromChild = child.getSickRequestDuration();
                    if (totalDaysFromChild > count) {
                        EdsMultiLeave leave = multiLeaveManager.get(child.getObjectID());
                        leave.setSickRequestDuration(totalDaysFromChild - count);
                        multiLeaveManager.createOrUpdate(leave);
                    } else {
                        EdsMultiLeave leave = multiLeaveManager.get(child.getObjectID());
                        leave.setSickRequestDuration(0d);
                        multiLeaveManager.createOrUpdate(leave);
                    }
                }
                if (child != null) {
                    if (!child.getSickRequestType().equals(Constants.MONEY)) {
                        sickRequestDurationManager.deleteDurationByDateAndEmployeeId(startDate, endDate, request.getEmployee().getObjectID());
                    }
                } else {
                    sickRequestDurationManager.deleteDurationByDateAndEmployeeId(startDate, endDate, request.getEmployee().getObjectID());
                }
                startDate = ServerUtils.addDays(endDate, 1);
            }
        }
    }

    private void updateAttendanceRawDataLR(EdsSickRequest sickRequest) {
        List<EdsAttendanceRawData> attendanceRawDataList = attendanceRawDataManager.getAttendanceRawDataByDates(sickRequest.getStartDate(), sickRequest.getEndDate(), sickRequest.getEmployee().getObjectID());
        for (EdsAttendanceRawData attendanceRawData : attendanceRawDataList) {
            int leaveMinutes = sickRequestDurationManager.getLeaveMinutes(sickRequest.getObjectID(), attendanceRawData.getDate());
            Integer leaveM = attendanceRawData.getLeave();
            attendanceRawData.setLeave(leaveM != null ? leaveM + leaveMinutes : leaveMinutes);
            attendanceRawDataManager.update(attendanceRawData);
        }
    }

    private static final Integer lunchStart = 2;
    private static final Integer lunchEnd = 3;

    private void incrementDay(Calendar start, HashMap<Date, EdsTimeSlotItem> exceptionalTimeSlotItem, Map<Integer, Integer[]> timeSlotItemMap) {
        int dayOfWeek = start.get(Calendar.DAY_OF_WEEK) - 1;
        start.add(Calendar.DAY_OF_YEAR, 1);

        boolean isNextExceptionalDate = false;
        int exceptionalNextStartDay = 0;

        Calendar calendar = (Calendar) start.clone();
        ServerUtils.setBeginningOfTheDay(calendar);

        if (exceptionalTimeSlotItem.containsKey(calendar.getTime())) {
            isNextExceptionalDate = true;
            exceptionalNextStartDay = exceptionalTimeSlotItem.get(calendar.getTime()).getStartTime();
        }

        if (timeSlotItemMap.get(dayOfWeek + 1) != null) {
            start.set(Calendar.HOUR_OF_DAY, (isNextExceptionalDate ? exceptionalNextStartDay : timeSlotItemMap.get(dayOfWeek + 1)[timeslotStart]) / 60);
            start.set(Calendar.MINUTE, (isNextExceptionalDate ? exceptionalNextStartDay : timeSlotItemMap.get(dayOfWeek + 1)[timeslotStart]) % 60);
        } else {
            start.set(Calendar.HOUR_OF_DAY, (isNextExceptionalDate ? exceptionalNextStartDay : timeSlotItemMap.get(0)[timeslotStart]) / 60);
            start.set(Calendar.MINUTE, (isNextExceptionalDate ? exceptionalNextStartDay : timeSlotItemMap.get(0)[timeslotStart]) % 60);
        }
    }

    private void setLeaveMinutes(HashMap<AttendanceKey, AttendanceValue> attendanceMap) {
        for (Map.Entry<AttendanceKey, AttendanceValue> attendanceEntry : attendanceMap.entrySet()) {
            EdsAttendanceRawData attendanceRawData = attendanceRawDataManager.getAttendanceRawDataByDate(attendanceEntry.getKey().getDate(), attendanceEntry.getKey().getEmployeeId());
            if (attendanceRawData != null) {
                if (attendanceEntry.getValue().getSetTimeSlot()) {
                    attendanceRawData.setTimeSlot(attendanceEntry.getValue().getTimeSlot());
                }

                attendanceRawData.setLeave(attendanceEntry.getValue().getLeaveApproved());
                attendanceRawData.setLeavePending(attendanceEntry.getValue().getLeavePending());
                attendanceRawData.setLeaveDenied(attendanceEntry.getValue().getLeaveDenied());

                attendanceRawData.setFromAnnualLeaveTime(attendanceEntry.getValue().getFromAnnualLeaveTime());
                attendanceRawData.setPaidTime(attendanceEntry.getValue().getPaidTime());
            } else {
                LOGGER.info(">>>> skipping ARD Leave minutes setting for employee: " + attendanceEntry.getKey().getEmployeeId() + " date: " + attendanceEntry.getKey().getDate());
            }
        }
    }

    private static final Integer coffeeStart = 4;
    private static final Integer coffeeEnd = 5;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<LaborPeriodRequest> getPeriodList(Integer employeeID, Integer requestID, String reasonCode, boolean isSummary, boolean isRecalculate) {
        if (employeeManager.get(employeeID).getStartDate() == null) {
            return null;
        }
        LinkedHashMap<Integer, Double> getTakenDaysBySick = null;
        List<EdsLabourPeriod> periodList;
        EdsSickRequest sickRequest = null;
        if (isSummary && requestID != null) {
            sickRequest = sickRequestManager.get(requestID);
            periodList = labourPeriodManager.sickRequestPeriods(requestID, false);
        } else if (requestID != null) {
            sickRequest = sickRequestManager.get(requestID);
            getTakenDaysBySick = labourPeriodManager.getSickDaysByPeriod(requestID);
            periodList = labourPeriodManager.periodListByEmployeeId(employeeID);
        } else {
            periodList = labourPeriodManager.periodListByEmployeeId(employeeID);
        }
        Double experienceDays = 0d;
        if (sickRequest != null) {
            experienceDays = getaExperienceDays(employeeID, sickRequest.getStartDate());
        }
        ArrayList<LaborPeriodRequest> requestList = new ArrayList<>();
        if (periodList != null && periodList.size() > 0) {
            for (EdsLabourPeriod item : periodList) {
                EdsLeaveReasonHistory history = leaveReasonHistoryManager.leaveReasonHistoryByReasonCode(reasonCode, item.getStartDate());
                Double totalSubmittedLeaveDays = labourPeriodManager.getTotalTakenLeaveDaysByPeriodId(item.getObjectID(), false);
                Double totalApprovedLeaveDays;
                if (isRecalculate) {
                    totalApprovedLeaveDays = labourPeriodManager.getLeaveDaysByPeriodIdAndExcludeSick(item.getObjectID(), true, true, requestID);
                } else {
                    totalApprovedLeaveDays = labourPeriodManager.getTotalTakenLeaveDaysByPeriodId(item.getObjectID(), true);
                }
                if (history != null) {
                    LaborPeriodRequest request = item.toRpc();
                    String periodStartDate = ServerUtils.shortDateFormat(item.getStartDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
                    String periodEndDate = ServerUtils.shortDateFormat(item.getEndDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
                    String period = String.format((Locale) null, "%1$s%n%2$s", periodStartDate, periodEndDate);
                    request.setLaborPeriod(period);
                    if (getTakenDaysBySick != null && getTakenDaysBySick.get(item.getObjectID()) != null) {
                        request.setCurrentLeaveDays(getTakenDaysBySick.get(item.getObjectID()));
                    }
                    request.setAllowance(item.getAllowance() != null ? item.getAllowance() : 0);
                    request.setMinLeaveDays(history.getMinLeaveDays() != null ? history.getMinLeaveDays() : Double.valueOf(0));
                    request.setOverAllSubmittedLeaveDays(totalSubmittedLeaveDays != null ? totalSubmittedLeaveDays : 0);
                    request.setApprovedTakenDays(totalApprovedLeaveDays != null ? totalApprovedLeaveDays + (item.getOutOfSystemDays() != null ? item.getOutOfSystemDays() : 0) : 0 + (item.getOutOfSystemDays() != null ? item.getOutOfSystemDays() : 0));
                    if (new Date().after(item.getEndDate())) {
                        if (request.getAllowance() - request.getApprovedTakenDays() > 0) {
                            request.setExperienceDays(experienceDays);
                        }
                    } else {
                        request.setExperienceDays(experienceDays);
                    }
                    requestList.add(request);
                }
            }
        }
        return requestList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<LaborPeriodRequest> getTakenDaysByPeriod(Integer periodID) {
        ArrayList<LaborPeriodRequest> list = new ArrayList<>();
        EdsLabourPeriod labourPeriod = labourPeriodManager.get(periodID);
        String startDate = ServerUtils.shortDateFormat(labourPeriod.getStartDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
        String endDate = ServerUtils.shortDateFormat(labourPeriod.getEndDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);

        List<Object[]> leaveList = labourPeriodManager.getPeriodLeavesData(periodID, false);
        LaborPeriodRequest periodRequest;
        if (labourPeriod.getOutOfSystemDays() != null && labourPeriod.getOutOfSystemDays() > 0) {
            periodRequest = new LaborPeriodRequest();
            periodRequest.setApprovedTakenDays(labourPeriod.getOutOfSystemDays());
            periodRequest.setCreatedDate("n/a");
            periodRequest.setLeavePeriod("Before using the system");
            periodRequest.setLaborPeriod(startDate + " - " + endDate);
            periodRequest.setLeaveRequestNumber(commonLocalizer.localize("adjusted", "Adjusted Days"));
            periodRequest.setLeaveRequestStatus("n/a");
            periodRequest.setLeaveRequestStatusCode(Constants.LR_STATUS_SS_APPROVED);
            if (labourPeriod.getEmployee() != null) {
                periodRequest.setEmployeeID(labourPeriod.getEmployee().getObjectID());
            }
            periodRequest.setLeaveRequestID(-1);
            list.add(periodRequest);
        }

        for (Object[] sickData : leaveList) {
            EdsSickRequest edsLeave = sickRequestManager.get((Integer) sickData[0]);
            periodRequest = new LaborPeriodRequest();
            List<EdsMultiLeave> leaveRequestByParentId = multiLeaveManager.getMultiLeaveListBySickForPeriodID(periodID);
            String leaveStartDate = ServerUtils.shortDateFormat(edsLeave.getStartDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
            String leaveEndDate;
            if (edsLeave.getRecallDate() != null) {
                leaveEndDate = ServerUtils.shortDateFormat(edsLeave.getRecallDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
            } else if (leaveRequestByParentId != null && leaveRequestByParentId.size() > 0) {
                Optional<Date> sickEndDate = leaveRequestByParentId.stream()
                        .filter(sr -> Constants.DAY.equals(sr.getSickRequestType()))
                        .map(EdsMultiLeave::getChildSickRequest)
                        .map(EdsSickRequest::getEndDate)
                        .findFirst();

                leaveEndDate = ServerUtils.shortDateFormat(sickEndDate.get(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
            } else {
                leaveEndDate = ServerUtils.shortDateFormat(edsLeave.getEndDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
            }
            periodRequest.setApprovedTakenDays(((BigDecimal) sickData[1]).doubleValue());
            periodRequest.setCreatedDate(ServerUtils.shortDateFormat(edsLeave.getCreatedDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true));
            periodRequest.setLeavePeriod(leaveStartDate + " - " + leaveEndDate);
            periodRequest.setLeaveRequestNumber(edsLeave.getNumberData());
            periodRequest.setLaborPeriod(startDate + " - " + endDate);
            periodRequest.setLeaveRequestStatus(edsLeave.getOverallStatus().getLocalizedName());
            periodRequest.setLeaveRequestStatusCode(edsLeave.getOverallStatus().getCode());
            periodRequest.setEmployeeID(edsLeave.getEmployee().getObjectID());
            periodRequest.setLeaveRequestID(edsLeave.getObjectID());
            list.add(periodRequest);
        }

        return list;
    }


    public void updateLRHours(EdsEmployee employee, Date updateLRFrom, Date to) {
        List<EdsSickRequest> sickRequests = sickRequestManager.getSickRequestByEmployeeAndPeriod(employee, updateLRFrom, to);
        LOGGER.info("<<<<" + sickRequests.size() + " sick requests are about to be updated for employee " + employee.getObjectID() + " at " + new Date());
        for (EdsSickRequest sickRequest : sickRequests) {
            if (sickRequest.getStartDate().getTime() < updateLRFrom.getTime()) {
                populateLeaveHoursToAttendanceRawData(sickRequest, null, updateLRFrom, false, null);
            } else {
                populateLeaveHoursToAttendanceRawData(sickRequest, null, null, false, null);
            }
        }
        LOGGER.info("<<<<" + sickRequests.size() + " sick requests were updated at " + new Date());
    }

    private void populateLeaveHoursToAttendanceRawData(EdsSickRequest sickRequest, LinkedHashMap<String, String> dateByPeriodMap, Date populateFromDate, boolean validate, EdsLabourPeriod period) {
        //validate means - we don't need to validate already created leave request duration. For example - in timeslot update/create

        if (validate) {
            int yearDiffrence = sickRequest.getEndDate().getYear() - new Date().getYear();
            //create new one year if today's date is not in datejoin table
            Integer companyID = sickRequest.getEmployee().getCompany().getObjectID();
            dashboardService.lastEnteredDate(yearDiffrence, companyID);

            //create attendance raw data for the current year and employee, if there is none
            createAttendaceRawDataRecords(sickRequest.getEmployee().getObjectID(), yearDiffrence);
        }

        //create list of leave minutes for each day
        Calendar sickRequestStart = Calendar.getInstance();
        if (populateFromDate == null) {
            sickRequestStart.setTime(sickRequest.getStartDate());
        } else {
            sickRequestStart.setTime(populateFromDate);
        }

        EdsEmployee edsEmployee = sickRequest.getEmployee();

        HashMap<Date, EdsTimeSlotItem> exceptionalTimeSlotItem = edsEmployee.getTimeSlot().getExceptionalTimeSlotItem();

        Map<Integer, Integer[]> timeSlotItemMap = getTimeslotMinutes(edsEmployee, sickRequest.getIncludeDayOff(), sickRequest.getLeaveReason() != null ? sickRequest.getLeaveReason().getCode() : null);

        int i = 1;
        int oldCurrentYear = 0;
        double leftPaidDays = 0;
        boolean isPaid = sickRequest.getType() != null && EdsSickRequest.PAID.equals(sickRequest.getType().getCode());

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(edsEmployee.getObjectID());
        fp.setStartDate(sickRequest.getStartDate());
        fp.setEndDate(sickRequest.getEndDate());
        fp.setReasonCode(sickRequest.getLeaveReason() != null ? sickRequest.getLeaveReason().getCode() : null);

        HashMap<AttendanceKey, AttendanceValue> attendanceMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        EdsLeaveReason reason = sickRequest.getLeaveReason();
        boolean asNonPaid = reason != null && reason.getTypeOption().equals(TypeOption.ALLOW_AS_NON_PAID);

        Date today = sickRequest.getEmployee().getUserDate(new Date());
        Integer periodId = null;
        String dayType = Constants.DAY;
        while (sickRequestStart.getTime().getTime() <= sickRequest.getEndDate().getTime()) {
            if (validate) {
                calendar.setTime(sickRequestStart.getTime());
                int currentYear = calendar.get(Calendar.YEAR);
                if (period != null) {
                    leftPaidDays = getLeftPaidDaysByPeriod(edsEmployee.getObjectID(), sickRequest.getLeaveReason().getCode());
                    i = 1;
                } else {
                    if (currentYear != oldCurrentYear && isPaid) {
                        fp.setYear(currentYear);
                        oldCurrentYear = currentYear;
                        leftPaidDays = getLeftPaidDays(fp);
                        i = 1;
                    }
                }
                if (i > leftPaidDays && asNonPaid) {
                    isPaid = false;
                }
                if (dateByPeriodMap != null) {
                    String periodWithType = dateByPeriodMap.get(new SimpleDateFormat(Constants.DATE_PATTERN).format(sickRequestStart.getTime()));
                    if (periodWithType != null) {
                        periodId = Integer.valueOf(periodWithType.split("@")[0]);
                        if (periodWithType.split("@").length > 1) dayType = periodWithType.split("@")[1];
                    }
                }
                i = i + calculateLeaveMinutes(sickRequest, edsEmployee, attendanceMap, sickRequestStart, periodId, dayType, exceptionalTimeSlotItem, timeSlotItemMap, isPaid, validate);
            } else if (sickRequestStart.getTime().before(today)) { // taken leave's shouldn't be updated
                incrementDay(sickRequestStart, exceptionalTimeSlotItem, timeSlotItemMap);
            } else {
                calculateLeaveMinutes(sickRequest, edsEmployee, attendanceMap, sickRequestStart, null, Constants.DAY, exceptionalTimeSlotItem, timeSlotItemMap, isPaid, validate);
            }
        }

        setLeaveMinutes(attendanceMap);
    }

    private Map<Integer, Integer[]> getTimeslotMinutes(EdsEmployee edsEmployee, boolean includeDayOffs, String reasonCode) {
        EdsTimeSlotItem startOfWeek = null;
        EdsTimeSlot timeSlot = edsEmployee.getTimeSlot();
        Set<EdsTimeSlotItem> timeSlotItems = timeSlot.getItems();
        EdsLeaveReason reason = null;
        boolean enabledExceptionalTimeslotByReason = false;
        if (reasonCode != null) {
            reason = leaveReasonManager.getReasonByName(null, reasonCode);
        }
        if (timeSlot != null && timeSlot.getSelectedLeaveReasons() != null && timeSlot.getSelectedLeaveReasons().size() > 0) {
            for (EdsLeaveReason leaveReason : timeSlot.getSelectedLeaveReasons()) {
                if (reasonCode != null && reasonCode.equals(leaveReason.getCode())) {
                    enabledExceptionalTimeslotByReason = true;
                    break;
                }
            }
        }
        Map<Integer, Integer[]> timeSlotItemMap = new HashMap<>();

        if (includeDayOffs) {
            EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(edsEmployee.getCompany().getObjectID());
            startOfWeek = timeSlotItemManager.getTimeSlotItemByDay(companySystemSettings.getOverallDatePickerWeekStart(), timeSlot.getObjectID());
        }
        for (EdsTimeSlotItem timeSlotItem : timeSlotItems) {
            Integer[] hourAndMinute = new Integer[6];
            if (includeDayOffs && timeSlotItem.getStartTime().equals(timeSlotItem.getEndTime())) {
                hourAndMinute[timeslotStart] = (startOfWeek != null && startOfWeek.getStartTime() != 0) ? startOfWeek.getStartTime() : 540;
                hourAndMinute[timeslotEnd] = (startOfWeek != null && startOfWeek.getEndTime() != 0) ? startOfWeek.getEndTime() : 1080;
                hourAndMinute[lunchStart] = startOfWeek != null ? startOfWeek.getLunchStart() : 0;
                hourAndMinute[lunchEnd] = startOfWeek != null ? startOfWeek.getLunchEnd() : 0;
                hourAndMinute[coffeeStart] = startOfWeek != null ? startOfWeek.getCoffeeStart() : 0;
                hourAndMinute[coffeeEnd] = startOfWeek != null ? startOfWeek.getCoffeeEnd() : 0;
            } else if (enabledExceptionalTimeslotByReason && timeSlot != null && timeSlot.getAdditionalLeaveDays() != null) {
                boolean leaveDayContains = timeSlot.getAdditionalLeaveDays().contains(timeSlotItem.getDay());
                final Integer[] startTime = new Integer[1];
                final Integer[] endTime = new Integer[1];
                if (leaveDayContains) {
                    timeSlotItems.forEach(e -> {
                        if (e.getDay().equals(1)) {
                            startTime[0] = (e.getStartTime());
                            endTime[0] = (e.getEndTime());
                        }
                    });
                }
                hourAndMinute[timeslotStart] = leaveDayContains ? startTime[0] : timeSlotItem.getStartTime();
                hourAndMinute[timeslotEnd] = leaveDayContains ? endTime[0] : timeSlotItem.getEndTime();
                hourAndMinute[lunchStart] = timeSlotItem.getLunchStart();
                hourAndMinute[lunchEnd] = timeSlotItem.getLunchEnd();
                hourAndMinute[coffeeStart] = timeSlotItem.getCoffeeStart();
                hourAndMinute[coffeeEnd] = timeSlotItem.getCoffeeEnd();
            } else {
                hourAndMinute[timeslotStart] = timeSlotItem.getStartTime();
                hourAndMinute[timeslotEnd] = timeSlotItem.getEndTime();
                hourAndMinute[lunchStart] = timeSlotItem.getLunchStart();
                hourAndMinute[lunchEnd] = timeSlotItem.getLunchEnd();
                hourAndMinute[coffeeStart] = timeSlotItem.getCoffeeStart();
                hourAndMinute[coffeeEnd] = timeSlotItem.getCoffeeEnd();
            }
            timeSlotItemMap.put(timeSlotItem.getDay(), hourAndMinute);
        }
        return timeSlotItemMap;
    }

    private int getLeaveMinutes(Calendar sickRequestStart, Calendar timeSlotStart, Calendar timeSlotEnd, Calendar lunchStart, Calendar lunchEnd, Calendar coffeeStart, Calendar coffeeEnd, Calendar sickRequestEnd) {
        int leaveMinutes = 0;
        if (sickRequestStart.getTime().getTime() <= timeSlotStart.getTime().getTime() && sickRequestEnd.getTime().getTime() >= timeSlotEnd.getTime().getTime()) {
            //----SRS--o-----------o---SRE----
            leaveMinutes = (int) ((timeSlotEnd.getTime().getTime() - timeSlotStart.getTime().getTime()) - (lunchEnd.getTime().getTime() - lunchStart.getTime().getTime()) - (coffeeEnd.getTime().getTime() - coffeeStart.getTime().getTime())) / 60000;
        } else if (sickRequestStart.getTime().getTime() <= timeSlotStart.getTime().getTime() && sickRequestEnd.getTime().getTime() < timeSlotEnd.getTime().getTime() && sickRequestEnd.getTime().getTime() > timeSlotStart.getTime().getTime()) {
            //----SRS--o-----SRE---o----------
            leaveMinutes = getPlannedWorkMinutes(timeSlotStart.getTime().getTime(), sickRequestEnd.getTime().getTime(), lunchStart.getTime().getTime(), lunchEnd.getTime().getTime(), coffeeStart.getTime().getTime(), coffeeEnd.getTime().getTime());
        } else if (sickRequestStart.getTime().getTime() > timeSlotStart.getTime().getTime() && sickRequestEnd.getTime().getTime() < timeSlotEnd.getTime().getTime()) {
            //--------o--SRS--SRE--o----------
            leaveMinutes = getPlannedWorkMinutes(sickRequestStart.getTime().getTime(), sickRequestEnd.getTime().getTime(), lunchStart.getTime().getTime(), lunchEnd.getTime().getTime(), coffeeStart.getTime().getTime(), coffeeEnd.getTime().getTime());
        } else if (sickRequestStart.getTime().getTime() > timeSlotStart.getTime().getTime() && sickRequestEnd.getTime().getTime() >= timeSlotEnd.getTime().getTime() && sickRequestStart.getTime().getTime() < timeSlotEnd.getTime().getTime()) {
            //--------o-----SRS---o---SRE----
            leaveMinutes = getPlannedWorkMinutes(sickRequestStart.getTime().getTime(), timeSlotEnd.getTime().getTime(), lunchStart.getTime().getTime(), lunchEnd.getTime().getTime(), coffeeStart.getTime().getTime(), coffeeEnd.getTime().getTime());
        }
        return leaveMinutes;
    }

    private int calculateLeaveMinutes(EdsSickRequest sickRequest, EdsEmployee employee, HashMap<AttendanceKey, AttendanceValue> attendanceMap, Calendar sickRequestStart, Integer periodId, String dayType, HashMap<Date, EdsTimeSlotItem> exceptionalTimeSlotItem, Map<Integer, Integer[]> timeSlotItemMap, boolean leftPaidDays, boolean validate) {
        int count = 0;
        int dayOfWeek = sickRequestStart.get(Calendar.DAY_OF_WEEK) - 1;

        Calendar calendar = (Calendar) sickRequestStart.clone();
        ServerUtils.setBeginningOfTheDay(calendar);

        boolean isExceptionalDate = false;
        int exceptionalStartDay = 0;
        int exceptionalEndDay = 0;
        int exceptionalLunchStartDay = 0;
        int exceptionalLunchEndDay = 0;
        int exceptionalCoffeeStartDay = 0;
        int exceptionalCoffeeEndDay = 0;

        if (exceptionalTimeSlotItem.containsKey(calendar.getTime())) {
            isExceptionalDate = true;
            exceptionalStartDay = exceptionalTimeSlotItem.get(calendar.getTime()).getStartTime();
            exceptionalEndDay = exceptionalTimeSlotItem.get(calendar.getTime()).getEndTime();
            exceptionalLunchStartDay = exceptionalTimeSlotItem.get(calendar.getTime()).getLunchStart();
            exceptionalLunchEndDay = exceptionalTimeSlotItem.get(calendar.getTime()).getLunchEnd();
            exceptionalCoffeeStartDay = exceptionalTimeSlotItem.get(calendar.getTime()).getCoffeeStart();
            exceptionalCoffeeEndDay = exceptionalTimeSlotItem.get(calendar.getTime()).getCoffeeEnd();
        }
        boolean startEqualToEnd = !timeSlotItemMap.get(dayOfWeek)[timeslotStart].equals(timeSlotItemMap.get(dayOfWeek)[timeslotEnd]);
        boolean exceptionalTimeSlot = false;
        EdsTimeSlot timeSlot = sickRequest.getEmployee().getTimeSlot();
        if (timeSlot.getSelectedLeaveReasons() != null && timeSlot.getSelectedLeaveReasons().size() > 0) {
            for (EdsLeaveReason leaveReason : timeSlot.getSelectedLeaveReasons()) {
                if (sickRequest != null && leaveReason.getCode().equals(sickRequest.getLeaveReason().getCode())) {
                    exceptionalTimeSlot = true;
                    break;
                }
            }
        }

        if (exceptionalTimeSlot && !startEqualToEnd && employee.getTimeSlot().getAdditionalLeaveDays() != null) {
            startEqualToEnd = employee.getTimeSlot().getAdditionalLeaveDays().contains(dayOfWeek);
        }

        boolean startTimeDayNotEqualEndTimeDay = isExceptionalDate ? (exceptionalStartDay != exceptionalEndDay) : startEqualToEnd;

        if (startTimeDayNotEqualEndTimeDay) {

            int timeSlotStartTime = isExceptionalDate ? exceptionalStartDay : timeSlotItemMap.get(dayOfWeek)[timeslotStart].equals(0) && startEqualToEnd ? timeSlotItemMap.get(1)[timeslotStart] : timeSlotItemMap.get(dayOfWeek)[timeslotStart];
            int timeSlotEndTime = isExceptionalDate ? exceptionalEndDay : timeSlotItemMap.get(dayOfWeek)[timeslotEnd].equals(0) && startEqualToEnd ? timeSlotItemMap.get(1)[timeslotEnd] : timeSlotItemMap.get(dayOfWeek)[timeslotEnd];
            int lunchStartTime = isExceptionalDate ? exceptionalLunchStartDay : timeSlotItemMap.get(dayOfWeek)[lunchStart];
            int lunchEndTime = isExceptionalDate ? exceptionalLunchEndDay : timeSlotItemMap.get(dayOfWeek)[lunchEnd];
            int coffeeStartTime = isExceptionalDate ? exceptionalCoffeeStartDay : timeSlotItemMap.get(dayOfWeek)[coffeeStart];
            int coffeeEndTime = isExceptionalDate ? exceptionalCoffeeEndDay : timeSlotItemMap.get(dayOfWeek)[coffeeEnd];

            Calendar timeSlotStart = (Calendar) sickRequestStart.clone();
            timeSlotStart.set(Calendar.HOUR_OF_DAY, timeSlotStartTime / 60);
            timeSlotStart.set(Calendar.MINUTE, timeSlotStartTime % 60);

            Calendar timeSlotEnd = (Calendar) sickRequestStart.clone();
            timeSlotEnd.set(Calendar.HOUR_OF_DAY, timeSlotEndTime / 60);
            timeSlotEnd.set(Calendar.MINUTE, timeSlotEndTime % 60);

            Calendar lunchStart = (Calendar) sickRequestStart.clone();
            lunchStart.set(Calendar.HOUR_OF_DAY, lunchStartTime / 60);
            lunchStart.set(Calendar.MINUTE, lunchStartTime % 60);
            Calendar lunchEnd = (Calendar) sickRequestStart.clone();
            lunchEnd.set(Calendar.HOUR_OF_DAY, lunchEndTime / 60);
            lunchEnd.set(Calendar.MINUTE, lunchEndTime % 60);

            Calendar coffeeStart = (Calendar) sickRequestStart.clone();
            coffeeStart.set(Calendar.HOUR_OF_DAY, coffeeStartTime / 60);
            coffeeStart.set(Calendar.MINUTE, coffeeStartTime % 60);
            Calendar coffeeEnd = (Calendar) sickRequestStart.clone();
            coffeeEnd.set(Calendar.HOUR_OF_DAY, coffeeEndTime / 60);
            coffeeEnd.set(Calendar.MINUTE, coffeeEndTime % 60);

            Calendar sickRequestEnd = Calendar.getInstance();
            if (sickRequest.getEndDate().getTime() >= timeSlotEnd.getTime().getTime()) {
                sickRequestEnd = (Calendar) timeSlotEnd.clone();
            } else {
                sickRequestEnd.setTime(sickRequest.getEndDate());
            }
            int leaveMinutes = getLeaveMinutes(sickRequestStart, timeSlotStart, timeSlotEnd, lunchStart, lunchEnd, coffeeStart, coffeeEnd, sickRequestEnd);
            int workingMinutes = timeSlotEndTime - timeSlotStartTime - (coffeeEndTime - coffeeStartTime) - (lunchEndTime - lunchStartTime);
            getLeaveMinutesMap(sickRequest, attendanceMap, sickRequestStart, workingMinutes, leaveMinutes);

            EdsAttendanceRawData attendanceRawData = attendanceRawDataManager.getAttendanceRawDataByDate(sickRequestStart.getTime(), employee.getObjectID());
            if (attendanceRawData == null) {
                Calendar startDate = (Calendar) sickRequestStart.clone();
                ServerUtils.setBeginningOfTheDay(startDate);

                Calendar to = Calendar.getInstance();
                to.setTime(sickRequest.getEndDate());
                ServerUtils.setBeginningOfTheDay(to);

                insertARD(startDate, to, employee);

                attendanceRawData = attendanceRawDataManager.getAttendanceRawDataByDate(sickRequestStart.getTime(), employee.getObjectID());
            }
            //CREATE/UPDATE SICK_REQUEST_DURATION
            List<EdsSickRequestDuration> otherDuration = sickRequestDurationManager.getDurationByDateAndEmployeeId(calendar.getTime(), sickRequest);
            for (EdsSickRequestDuration othSD : otherDuration) {
                othSD.setDayType(Constants.USED_ANOHTER_LEAVE_OR_RECALL);
            }
            EdsSickRequestDuration duration = sickRequestDurationManager.getSickRequestDurationT(calendar.getTime(), sickRequest.getObjectID(), periodId, dayType);

            Integer durationTime = validate ? leaveMinutes : Double.valueOf(duration.getDay() * workingMinutes).intValue();
            double day = validate ? (double) leaveMinutes / workingMinutes : duration.getDay();
            double leaveDay = "LR_TYPE_ANNUAL_LEAVE".equals(sickRequest.getLeaveReason().getCode()) && !UnitType.HOURLY.equals(sickRequest.getLeaveReason().getUnitType()) ? 1d : day;

            duration.setDurationTime(durationTime);
            duration.setDay(leaveDay);
            duration.setTimeSlot(workingMinutes);
            duration.setDayOff(attendanceRawData.getDayOff());
            duration.setHoliday(attendanceRawData.getHoliday());
            duration.setHolidayFromAnnualLeave(attendanceRawData.getHolidayFromAnnualLeave());
            duration.setPaid(leftPaidDays);
            if (!attendanceRawData.getHoliday()) {
                count = 1;
            }
        } else {
            //reset everything to zero
            AttendanceKey attendanceKey = new AttendanceKey(ServerUtils.getDayStartTime(sickRequestStart.getTime()), sickRequest.getEmployee().getObjectID());
            AttendanceValue attendanceValue = new AttendanceValue(0, 0, 0, 0, 0, 0, true);
            if (attendanceMap.containsKey(attendanceKey)) {
                attendanceMap.get(attendanceKey).add(attendanceValue);
            } else {
                attendanceMap.put(attendanceKey, attendanceValue);
            }
        }
        incrementDay(sickRequestStart, exceptionalTimeSlotItem, timeSlotItemMap);
        return count;
    }

    private int getPlannedWorkMinutes(long timeStart, long timeEnd, long lunchStart, long lunchEnd, long coffeeStart, long coffeeEnd) {
        long lunchTime = 0;
        if (timeStart <= lunchStart && timeEnd >= lunchEnd) {
            lunchTime = lunchEnd - lunchStart;
        } else {
            if (timeStart > lunchStart && timeStart < lunchEnd && timeEnd >= lunchEnd) {
                lunchTime = lunchEnd - timeStart;
            } else {
                if (timeStart <= lunchStart && timeEnd > lunchStart && timeEnd < lunchEnd) {
                    lunchTime = timeEnd - lunchStart;
                }
            }
        }
        long coffeeTime = 0;
        if (timeStart <= coffeeStart && timeEnd >= coffeeEnd) {
            coffeeTime = coffeeEnd - coffeeStart;
        } else {
            if (timeStart > coffeeStart && timeStart < coffeeEnd && timeEnd >= coffeeEnd) {
                coffeeTime = coffeeEnd - timeStart;
            } else {
                if (timeStart <= coffeeStart && timeEnd > coffeeStart && timeEnd < coffeeEnd) {
                    coffeeTime = timeEnd - coffeeStart;
                }
            }
        }

        return (int) ((timeEnd - timeStart) - lunchTime - coffeeTime) / 60000;
    }

    @Deprecated
    public String[] getLeaveRequestStats(EdsSickRequest request) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(request.getEmployee().getObjectID());
        fp.setStartDate(request.getStartDate());
        fp.setEndDate(request.getEndDate());
        fp.setObjectId(request.getObjectID());
        fp.setAnnualLeave(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE.equals(request.getLeaveReason().getCode()));

        Double[] leaveRequestMinutes = sickRequestDurationManager.getLeaveRequestMinutes(fp);

        String[] stat = new String[4];
        stat[0] = (leaveRequestMinutes[DAYS_APPROVED] == null ? "0" : df.format(leaveRequestMinutes[DAYS_APPROVED]));
        stat[1] = (leaveRequestMinutes[HOURS_APPROVED] == null ? "0" : df.format(leaveRequestMinutes[HOURS_APPROVED]));
        stat[2] = (leaveRequestMinutes[2] == null ? "0" : df.format(leaveRequestMinutes[2]));
        stat[3] = (leaveRequestMinutes[3] == null ? "0" : df.format(leaveRequestMinutes[3]));
        return stat;
    }

    public void setComment(Integer leaveRequestID, String comment) {
        if (leaveRequestID != null) {
            Date commentDate = new Date();
            EdsSickRequestComment edsSickRequestComment = new EdsSickRequestComment();
            edsSickRequestComment.setCreationDate(commentDate);
            edsSickRequestComment.setText(comment);
            edsSickRequestComment.setSickRequest(sickRequestManager.get(leaveRequestID));
            edsSickRequestComment.setUser(userManager.getUser());
            edsSickRequestComment.setSuperUser(ServerUtils.isSuperUser());
            sickRequestCommentManager.create(edsSickRequestComment);
        }
    }

    public Integer createLeaveRequestHistory(Integer leaveRequestId, HistoryListItem hisItem) {
        if (leaveRequestId != null && hisItem != null) {
            EdsUser user = userManager.getUser();
            if (user instanceof EdsEmployee) {
                user = userManager.get(user.getObjectID());
            }
            EdsSickRequestComment leaveRequestHistory = new EdsSickRequestComment();
            leaveRequestHistory.setSickRequest(sickRequestManager.get(leaveRequestId));
            leaveRequestHistory.setCreationDate(new Date());
            leaveRequestHistory.setUser(user);
            leaveRequestHistory.setSuperUser(ServerUtils.isSuperUser());
            leaveRequestHistory.setText(hisItem.getComment());

            sickRequestCommentManager.create(leaveRequestHistory);
            return leaveRequestHistory.getObjectID();
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HistoryNote> loadLeaveRequestHistory(Integer reportId) {
        List<EdsSickRequestComment> historyList = sickRequestCommentManager.getComments(reportId);
        if (historyList == null) {
            historyList = new ArrayList<>();
        }

        List<HistoryNote> noteItemsList = new ArrayList<>();
        for (EdsSickRequestComment item : historyList) {
            if (StringUtils.isNotBlank(item.getText())) {
                HistoryListItem historyListItem = new HistoryListItem();
                historyListItem.setObjectID(item.getObjectID());
                if (item.isSuperUser()) {
                    historyListItem.setEmployee(Constants.defaultSupportName);
                } else {
                    historyListItem.setEmployee(item.getUser().getFullName());
                }
                historyListItem.setEmployeeID(item.getUser().getObjectID());
                if (item.getText().split(":").length > 1 && item.getText().split(":")[0].equals("rejectionReason")) { // For: Rejection Reason
                    historyListItem.setComment(commonLocalizer.localize(PdfLocalizationName.rejectionReason, "Rejection Reason") + ": " + item.getText().split(":")[1]);
                } else {
                    historyListItem.setComment(commonLocalizer.localize(item.getText().toLowerCase(), item.getText()));
                }
                historyListItem.setEventDate(item.getCreationDate());

                noteItemsList.add(historyListItem);
            }
        }
        return noteItemsList;
    }

    public void deleteLeaveRequestComment(Integer commentID) {
        EdsSickRequestComment requestComment = sickRequestCommentManager.get(commentID);
        sickRequestCommentManager.delete(requestComment);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LeaveRequestComment[] getComments(Integer leaveRequestID) {
        EdsSickRequest sickRequest = sickRequestManager.get(leaveRequestID);
        EdsUser currentUser = userManager.getUser();
        LeaveRequestComment[] leaveRequestComments;
        if (sickRequest != null) {
            EdsSickRequestComment[] result = sickRequest.getSickRequestComments().toArray(new EdsSickRequestComment[]{});
            if (result != null && result.length > 0) {
                leaveRequestComments = new LeaveRequestComment[result.length];
                for (int i = 0; i < result.length; i++) {
                    EdsSickRequestComment sickRequestComment = result[i];

                    LeaveRequestComment comment = new LeaveRequestComment();
                    comment.setObjectID(sickRequestComment.getObjectID());
                    comment.setEditable(currentUser.getObjectID().equals(sickRequestComment.getUser().getObjectID()));
                    if (sickRequestComment.isSuperUser()) {
                        comment.setUser(Constants.defaultSupportName);
                    } else {
                        comment.setUser(sickRequestComment.getUser() != null ? sickRequestComment.getUser().getFullName() : null);
                    }
                    comment.setCreationDate(sickRequestComment.getCreationDate() != null ? new Date(sickRequestComment.getCreationDate().getTime()) : null);
                    if (sickRequestComment.getText().split(":").length > 1 && sickRequestComment.getText().split(":")[0].equals("rejectionReason")) { // For: Rejection Reason
                        comment.setText(commonLocalizer.localize(PdfLocalizationName.rejectionReason, "Rejection Reason") + ": " + sickRequestComment.getText().split(":")[1]);
                    } else {
                        try {
                            comment.setText(commonLocalizer.localize(sickRequestComment.getText().toLowerCase()));
                        } catch (Exception e) {
                            comment.setText(sickRequestComment.getText());
                        }
                    }
                    leaveRequestComments[i] = comment;
                }
                return leaveRequestComments;
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<TimeslotItem> getTimeslots(ListingFilterParameter fp) {
        List<Object[]> timeslots = timeSlotManager.getTimeslotsForListing(fp);
        int totalCount = timeslots.size();
        timeslots = ListUtils.getSublist(timeslots, fp.getStart(), fp.getLimit());
        ArrayList<TimeslotItem> itemList = new ArrayList<>();
        for (Object[] object : timeslots) {
            TimeslotItem item = new TimeslotItem();
            item.setObjectID((Integer) object[0]);
            item.setName((String) object[1]);
            item.setDescription((String) object[2]);
            item.setShortName((String) object[3]);
            boolean isShift = object[4] != null ? (Boolean) object[4] : Boolean.FALSE;
            item.setShift((Boolean) object[4]);
            itemList.add(item);
        }
        return new ListResult<>(itemList, totalCount);
    }

    @Override
    public void createTimeslot(TimeslotItem item) {
        EdsUser user = employeeManager.getUser();

        EdsTimeSlot timeslot = new EdsTimeSlot();

        Set<EdsTimeSlotItem> timeslotitems = new HashSet<>();
        timeslot.setName(item.getName());
        timeslot.setShortName(item.getShortName());
        timeslot.setHexColor(item.getHexColor());
        timeslot.setDescription(item.getDescription());
        timeslot.setEffectiveFrom(item.getEffectiveDate());
        timeslot.setLateMinutes(item.getLateMinutes());
        timeslot.setEarlyLeaveMinutes(item.getEarlyMinutes());
        timeslot.setValidInStart(item.getValidInStart());
        timeslot.setValidInEnd(item.getValidInEnd());
        timeslot.setValidOutStart(item.getValidOutStart());
        timeslot.setValidOutEnd(item.getValidOutEnd());
        timeslot.setAutoInOutEnabled(item.isAutoInOutEnabled());
        SettingsData companySettings = profileService.getCompanySettings(false);
        Integer weekStart = companySettings.getOverallDatePickerWeekStart();

        EdsTimeSlotItem timeslotitem = new EdsTimeSlotItem();
        timeslotitem.setDay(0);
        timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 0 : weekStart.equals(FROM_SATURDAY) ? 1 : 6);
        timeslotitem.setStartTime(item.getSunday()[0]);
        timeslotitem.setEndTime(item.getSunday()[1]);
        timeslotitem.setLunchStart(item.getLunchSu()[0]);
        timeslotitem.setLunchEnd(item.getLunchSu()[1]);
        timeslotitem.setCoffeeStart(item.getCoffeeSu()[0]);
        timeslotitem.setCoffeeEnd(item.getCoffeeSu()[1]);
        timeslotitem.setTimeSlot(timeslot);
        timeslotitems.add(timeslotitem);
        timeSlotItemManager.create(timeslotitem);

        timeslotitem = new EdsTimeSlotItem();
        timeslotitem.setDay(1);
        timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 1 : weekStart.equals(FROM_SATURDAY) ? 2 : 0);
        timeslotitem.setStartTime(item.getMonday()[0]);
        timeslotitem.setEndTime(item.getMonday()[1]);
        timeslotitem.setLunchStart(item.getLunchMo()[0]);
        timeslotitem.setLunchEnd(item.getLunchMo()[1]);
        timeslotitem.setCoffeeStart(item.getCoffeeMo()[0]);
        timeslotitem.setCoffeeEnd(item.getCoffeeMo()[1]);
        timeslotitem.setTimeSlot(timeslot);
        timeslotitems.add(timeslotitem);
        timeSlotItemManager.create(timeslotitem);

        timeslotitem = new EdsTimeSlotItem();
        timeslotitem.setDay(2);
        timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 2 : weekStart.equals(FROM_SATURDAY) ? 3 : 1);
        timeslotitem.setStartTime(item.getTuesday()[0]);
        timeslotitem.setEndTime(item.getTuesday()[1]);
        timeslotitem.setLunchStart(item.getLunchTu()[0]);
        timeslotitem.setLunchEnd(item.getLunchTu()[1]);
        timeslotitem.setCoffeeStart(item.getCoffeeTu()[0]);
        timeslotitem.setCoffeeEnd(item.getCoffeeTu()[1]);
        timeslotitem.setTimeSlot(timeslot);
        timeslotitems.add(timeslotitem);
        timeSlotItemManager.create(timeslotitem);

        timeslotitem = new EdsTimeSlotItem();
        timeslotitem.setDay(3);
        timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 3 : weekStart.equals(FROM_SATURDAY) ? 4 : 2);
        timeslotitem.setStartTime(item.getWednesday()[0]);
        timeslotitem.setEndTime(item.getWednesday()[1]);
        timeslotitem.setLunchStart(item.getLunchWe()[0]);
        timeslotitem.setLunchEnd(item.getLunchWe()[1]);
        timeslotitem.setCoffeeStart(item.getCoffeeWe()[0]);
        timeslotitem.setCoffeeEnd(item.getCoffeeWe()[1]);
        timeslotitem.setTimeSlot(timeslot);
        timeslotitems.add(timeslotitem);
        timeSlotItemManager.create(timeslotitem);

        timeslotitem = new EdsTimeSlotItem();
        timeslotitem.setDay(4);
        timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 4 : weekStart.equals(FROM_SATURDAY) ? 5 : 3);
        timeslotitem.setStartTime(item.getThursday()[0]);
        timeslotitem.setEndTime(item.getThursday()[1]);
        timeslotitem.setLunchStart(item.getLunchTh()[0]);
        timeslotitem.setLunchEnd(item.getLunchTh()[1]);
        timeslotitem.setCoffeeStart(item.getCoffeeTh()[0]);
        timeslotitem.setCoffeeEnd(item.getCoffeeTh()[1]);
        timeslotitem.setTimeSlot(timeslot);
        timeslotitems.add(timeslotitem);
        timeSlotItemManager.create(timeslotitem);

        timeslotitem = new EdsTimeSlotItem();
        timeslotitem.setDay(5);
        timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 5 : weekStart.equals(FROM_SATURDAY) ? 6 : 4);
        timeslotitem.setStartTime(item.getFriday()[0]);
        timeslotitem.setEndTime(item.getFriday()[1]);
        timeslotitem.setLunchStart(item.getLunchFr()[0]);
        timeslotitem.setLunchEnd(item.getLunchFr()[1]);
        timeslotitem.setCoffeeStart(item.getCoffeeFr()[0]);
        timeslotitem.setCoffeeEnd(item.getCoffeeFr()[1]);
        timeslotitem.setTimeSlot(timeslot);
        timeslotitems.add(timeslotitem);
        timeSlotItemManager.create(timeslotitem);

        timeslotitem = new EdsTimeSlotItem();
        timeslotitem.setDay(6);
        timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 6 : weekStart.equals(FROM_SATURDAY) ? 0 : 5);
        timeslotitem.setStartTime(item.getSaturday()[0]);
        timeslotitem.setEndTime(item.getSaturday()[1]);
        timeslotitem.setLunchStart(item.getLunchSa()[0]);
        timeslotitem.setLunchEnd(item.getLunchSa()[1]);
        timeslotitem.setCoffeeStart(item.getCoffeeSa()[0]);
        timeslotitem.setCoffeeEnd(item.getCoffeeSa()[1]);
        timeslotitem.setTimeSlot(timeslot);
        timeslotitems.add(timeslotitem);
        timeSlotItemManager.create(timeslotitem);

        Set<EdsTimeSlotItem> exceptionalCaseTimeSlotItems = createExceptionalCaseTimeSlotItem(item.getExceptionalCases(), timeslot);
        timeslot.setExceptionalCaseItems(exceptionalCaseTimeSlotItems);

        timeslot.setType("WEEKLY");
        timeslot.setItems(timeslotitems);
        if (item.getAdditionalLeaveDays() != null) {
            timeslot.setAdditionalLeaveDays(item.getAdditionalLeaveDays());
        }
        if (item.getSelectedLeaveReasons() != null && item.getSelectedLeaveReasons().size() > 0) {
            HashSet<EdsLeaveReason> selectedReasons = new HashSet<>();
            item.getSelectedLeaveReasons().forEach(r -> {
                EdsLeaveReason reason = leaveReasonManager.get(r.getId());
                selectedReasons.add(reason);
            });
            timeslot.setSelectedLeaveReasons(selectedReasons);
        }
        timeslot.setDeleted(false);
        if (item.getReferenceLocale() != null && checkReferenceLocale(item.getReferenceLocale())) {
            timeslot.setLocale(allInOneServiceLocal.saveEntityLocale(item.getReferenceLocale()));
        }
        timeSlotManager.create(timeslot);

        //create new one year if today's date is not in datejoin table
        dashboardService.lastEnteredDate();

        if (item.getSelectedEmployeeIds() != null && item.getSelectedEmployeeIds().length > 0) {
            for (Integer member1 : item.getSelectedEmployeeIds()) {
                EdsEmployee member = employeeManager.get(member1);
                if (member != null) {
                    member.setTimeSlot(timeslot);
                    employeeManager.update(member);
                }
            }
            saveAttendanceRawData(item.getSelectedEmployeeIds(), user, timeslot);
        }

        EdsEmployee employee = user.getEmployee();
        updateTimeSlotHistory("Created the Timeslot Effective Date: " + ServerUtils.shortDateFormat(item.getEffectiveDate(), user) + ", End Date: " + ServerUtils.shortDateFormat(ServerUtils.addDays(item.getEffectiveDate(), 730), user), timeslot, null);
        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(TimeslotEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, employee, user);
        event.setSourceID(timeslot.getObjectID());

        baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, employee, user);

    }

    @Override
    public void saveShiftSettings(ShiftSettingsItem item) {
        EdsUser user = employeeManager.getUser();

        EdsShiftSettings shiftSettings;
        if (item.getId() != null) {
            shiftSettings = shiftSettingsManager.get(item.getId());
        } else {
            shiftSettings = new EdsShiftSettings();
        }

        shiftSettings.setName(item.getName());
        shiftSettings.setShortName(item.getShortName());
        shiftSettings.setHexColor(item.getHexColor());
        shiftSettings.setDescription(item.getDescription());
        shiftSettings.setInterval(item.getInterval());

        shiftSettings.setStartTime(item.getTimes()[0]);
        shiftSettings.setEndTime(item.getTimes()[1]);

        shiftSettings.setCoffeeStart(item.getCoffeeTimes()[0]);
        shiftSettings.setCoffeeEnd(item.getCoffeeTimes()[1]);

        shiftSettings.setLunchStart(item.getLunchTimes()[0]);
        shiftSettings.setLunchEnd(item.getLunchTimes()[1]);

        shiftSettings.setIncludedDays(item.getExcludedDays());

        shiftSettings.setDeleted(false);
        if (item.getReferenceLocale() != null && checkReferenceLocale(item.getReferenceLocale())) {
            shiftSettings.setLocale(allInOneServiceLocal.saveEntityLocale(item.getReferenceLocale()));
        }
        shiftSettingsManager.create(shiftSettings);
        updateTimeSlotHistory((item.getId() != null ? "Updated" : "Created") + " Shift Settings", null, shiftSettings);
    }

    @Override
    public ShiftSettingsItem getShiftSettings(Integer id) {
        ShiftSettingsItem item = new ShiftSettingsItem();
        EdsShiftSettings edsShiftSettings = shiftSettingsManager.get(id);
        item.setId(edsShiftSettings.getObjectID());
        item.setName(edsShiftSettings.getName());
        item.setShortName(edsShiftSettings.getShortName());
        item.setHexColor(edsShiftSettings.getHexColor());
        item.setDescription(edsShiftSettings.getDescription());
        item.setInterval(edsShiftSettings.getInterval());
        item.setExcludedDays(edsShiftSettings.getIncludedDays());
        item.setReferenceLocale(edsShiftSettings.getLocale() != null ? edsShiftSettings.getLocale().toRPC() : null);
        item.setTimes(new int[]{edsShiftSettings.getStartTime(), edsShiftSettings.getEndTime()});
        item.setCoffeeTimes(new int[]{edsShiftSettings.getCoffeeStart(), edsShiftSettings.getCoffeeEnd()});
        item.setLunchTimes(new int[]{edsShiftSettings.getLunchStart(), edsShiftSettings.getLunchEnd()});
        return item;
    }

    @Override
    public String[] getTimeSlotInterval(Integer id) {
        return new String[]{shiftSettingsManager.get(id).getInterval(), shiftSettingsManager.get(id).getIncludedDays()};
    }

    private boolean checkReferenceLocale(ReferenceLocale referenceLocale) {
        return (referenceLocale.getUzbek() != null ||
                referenceLocale.getRussian() != null ||
                referenceLocale.getEnglish() != null ||
                referenceLocale.getArabic() != null);
    }

    private void updateTimeSlotHistory(String message, EdsTimeSlot timeSlot, EdsShiftSettings shiftSettings) {
        EdsTimeSlotHistory timeSlotHistory = new EdsTimeSlotHistory();
        timeSlotHistory.setCreationTime(new Date());
        timeSlotHistory.setUpdater(userManager.getUser());
        timeSlotHistory.setSuperUser(ServerUtils.isSuperUser());
        timeSlotHistory.setTimeSlot(timeSlot);
        timeSlotHistory.setShiftSettings(shiftSettings);
        timeSlotHistory.setMessage(message);
        timeSlotHistoryManager.create(timeSlotHistory);
    }

    private void updateHolidayHistory(String message, EdsHoliday holiday) {
        EdsHolidayHistory holidayHistory = new EdsHolidayHistory();
        holidayHistory.setCreationTime(new Date());
        holidayHistory.setUpdater(userManager.getUser());
        holidayHistory.setSuperUser(ServerUtils.isSuperUser());
        holidayHistory.setHoliday(holiday);
        holidayHistory.setMessage(message);
        holidayHistoryManager.create(holidayHistory);
    }

    private Set<EdsTimeSlotItem> createExceptionalCaseTimeSlotItem(ArrayList<ExceptionalTimeSlotItem> exceptionalCases, EdsTimeSlot timeslot) {
        EdsTimeSlotItem timeslotitem;
        Set<EdsTimeSlotItem> exceptionalCaseTimeSlotItems = new HashSet<>();
        if (exceptionalCases != null && exceptionalCases.size() > 0) {

            for (ExceptionalTimeSlotItem exTsI : exceptionalCases) {
                Date nonConvertedDate = exTsI.getExceptionalDate().getNonConvertedDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(nonConvertedDate);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;

                timeslotitem = new EdsTimeSlotItem();
                timeslotitem.setExceptionalDate(nonConvertedDate);
                timeslotitem.setDay(/*6*/dayOfWeek);
                timeslotitem.setStartTime(exTsI.getWeekDay()[0]);
                timeslotitem.setEndTime(exTsI.getWeekDay()[1]);
                timeslotitem.setLunchStart(exTsI.getLunch()[0]);
                timeslotitem.setLunchEnd(exTsI.getLunch()[1]);
                timeslotitem.setCoffeeStart(exTsI.getCoffee()[0]);
                timeslotitem.setCoffeeEnd(exTsI.getCoffee()[1]);

                timeslotitem.setTimeSlot(timeslot);
                exceptionalCaseTimeSlotItems.add(timeslotitem);
                timeSlotItemManager.create(timeslotitem);
            }
        }
        return exceptionalCaseTimeSlotItems;
    }

    private void saveAttendanceRawData(Integer[] employeeIdArray, EdsUser user, EdsTimeSlot timeSlot) {
        if (employeeIdArray == null || employeeIdArray.length == 0) {
            return;
        }
        String ids = Arrays.stream(employeeIdArray).map(Object::toString).collect(Collectors.joining(","));
        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(TimeslotEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, null, user);
        event.setSourceID(timeSlot.getObjectID());
        event.setCustomStringField(ids);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimeslotItem getTimeslot(Integer objectID) {
        EdsUser user = userManager.getUser();
        TimeslotItem timeslotitem = new TimeslotItem();
        EdsTimeSlot timeslot = timeSlotManager.get(objectID);
        timeslotitem.setName(timeslot.getName());
        timeslotitem.setShortName(timeslot.getShortName() != null ? timeslot.getShortName() : "");
        timeslotitem.setHexColor(timeslot.getHexColor() != null ? timeslot.getHexColor() : "");
        timeslotitem.setDescription(timeslot.getDescription());
        timeslotitem.setEffectiveDate(timeslot.getEffectiveFrom());
        timeslotitem.setAdditionalLeaveDays(timeslot.getAdditionalLeaveDays());
        timeslotitem.setSelectedLeaveReasons(timeslot.getLeaveReasonsAsSelectItem());
        timeslotitem.setReasons(getReasons(user.getObjectID(), true));
        timeslotitem.setLateMinutes(timeslot.getLateMinutes());
        timeslotitem.setEarlyMinutes(timeslot.getEarlyLeaveMinutes());
        timeslotitem.setValidInStart(timeslot.getValidInStart());
        timeslotitem.setValidInEnd(timeslot.getValidInEnd());
        timeslotitem.setValidOutStart(timeslot.getValidOutStart());
        timeslotitem.setValidOutEnd(timeslot.getValidOutEnd());
        timeslotitem.setAutoInOutEnabled(timeslot.isAutoInOutEnabled());
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
        timeslotitem.setWeekStart(companySystemSettings.getOverallDatePickerWeekStart());
        Set<EdsTimeSlotItem> items = timeslot.getItems();

        for (EdsTimeSlotItem item : items) {
            switch (item.getDay()) {
                case 0 -> {
                    timeslotitem.setSunday(new int[]{item.getStartTime(), item.getEndTime()});
                    timeslotitem.setLunchSu(new int[]{item.getLunchStart(), item.getLunchEnd()});
                    timeslotitem.setCoffeeSu(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                }
                case 1 -> {
                    timeslotitem.setMonday(new int[]{item.getStartTime(), item.getEndTime()});
                    timeslotitem.setLunchMo(new int[]{item.getLunchStart(), item.getLunchEnd()});
                    timeslotitem.setCoffeeMo(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                }
                case 2 -> {
                    timeslotitem.setTuesday(new int[]{item.getStartTime(), item.getEndTime()});
                    timeslotitem.setLunchTu(new int[]{item.getLunchStart(), item.getLunchEnd()});
                    timeslotitem.setCoffeeTu(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                }
                case 3 -> {
                    timeslotitem.setWednesday(new int[]{item.getStartTime(), item.getEndTime()});
                    timeslotitem.setLunchWe(new int[]{item.getLunchStart(), item.getLunchEnd()});
                    timeslotitem.setCoffeeWe(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                }
                case 4 -> {
                    timeslotitem.setThursday(new int[]{item.getStartTime(), item.getEndTime()});
                    timeslotitem.setLunchTh(new int[]{item.getLunchStart(), item.getLunchEnd()});
                    timeslotitem.setCoffeeTh(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                }
                case 5 -> {
                    timeslotitem.setFriday(new int[]{item.getStartTime(), item.getEndTime()});
                    timeslotitem.setLunchFr(new int[]{item.getLunchStart(), item.getLunchEnd()});
                    timeslotitem.setCoffeeFr(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                }
                case 6 -> {
                    timeslotitem.setSaturday(new int[]{item.getStartTime(), item.getEndTime()});
                    timeslotitem.setLunchSa(new int[]{item.getLunchStart(), item.getLunchEnd()});
                    timeslotitem.setCoffeeSa(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                }
            }
        }

        Map<Integer, String[]> departments = new HashMap<>();
        List<EdsEmployee> emps = employeeManager.getEmployees(timeslot.getObjectID());
        ArrayList<TimeslotEmployeeItem> arrayTimeslotEmployeeItem = new ArrayList<>();
        for (EdsEmployee e : emps) {
            Integer deptId = e.getEmployeeTeam() != null && e.getEmployeeTeam().getTeam() != null && e.getEmployeeTeam().getTeam().getObjectID() != null ? e.getEmployeeTeam().getTeam().getObjectID() : null;
            String deptName = e.getEmployeeTeam() != null && e.getEmployeeTeam().getTeam() != null && e.getEmployeeTeam().getTeam().getName() != null ? e.getEmployeeTeam().getTeam().getName() : null;
            String empName = e.getName();
            String[] names;
            names = departments.get(deptId);
            if (names == null) {
                names = new String[2];
                names[0] = deptName;
                names[1] = e.getName();
                if (deptId != null) {
                    departments.put(e.getEmployeeTeam().getTeam().getObjectID(), names);
                }
            } else {
                if ("".equals(names[1])) {
                    names[1] = names[1] + empName;
                } else {
                    names[1] = names[1] + ", " + empName;
                }
                departments.put(e.getEmployeeTeam().getTeam().getObjectID(), names);
            }
            TimeslotEmployeeItem timeslotEmployeeItem = new TimeslotEmployeeItem();
            timeslotEmployeeItem.setObjectID(e.getObjectID());
            timeslotEmployeeItem.setEmployeeFullName(e.getFullName());
            if (e.getTeam() != null && e.getTeam().getName() != null) {
                timeslotEmployeeItem.setEmployeeDepartment(e.getTeam().getName());
            }
            timeslotEmployeeItem.setEmployeeStatus(referenceWfmMessageSource.localize(e.getAccountStatus().getCode(), e.getAccountStatus().getName()));
            if (e.getPosition() != null) {
                timeslotEmployeeItem.setEmployeePosition(e.getPosition().getName());
            }
            arrayTimeslotEmployeeItem.add(timeslotEmployeeItem);
        }

        Departments[] depts = new Departments[departments.size()];
        int j = 0;
        for (Map.Entry<Integer, String[]> entry : departments.entrySet()) {
            Departments d = new Departments();
            d.setDeptName(entry.getValue()[0]);
            d.setEmployees(entry.getValue()[1]);
            depts[j] = d;
            j++;
        }
        timeslotitem.setDepartments(depts);
        timeslotitem.setDepartmentsAsString(depts);

        ArrayList<ExceptionalTimeSlotItem> dailyTimeSlotItems = timeslot.getDailyTimeSlotItems();
        dailyTimeSlotItems.sort(Comparator.comparing(ExceptionalTimeSlotItem::getDayNo));

        timeslotitem.setDailyItems(dailyTimeSlotItems);

        ArrayList<ExceptionalTimeSlotItem> exceptionalCases = timeslot.getExceptionalCaseTimeSlotItems();
        timeslotitem.setExceptionalCases(exceptionalCases);
        timeslotitem.setLocaleItem(timeslot.getLocale() != null ? timeslot.getLocale().toRPC() : null);
        arrayTimeslotEmployeeItem.sort(Comparator.comparing(TimeslotEmployeeItem::getEmployeeFullName));
        timeslotitem.setTimeslotEmployeeItems(arrayTimeslotEmployeeItem);
        return timeslotitem;
    }

    public void updateTimeslot(TimeslotItem item) {
        EdsUser user = timeSlotManager.getUser();
        EdsTimeSlot timeslot = timeSlotManager.get(item.getObjectID());
        Set<EdsTimeSlotItem> timeslotitems = timeslot.getItems();
        timeslot.setName(item.getName());
        timeslot.setShortName(item.getShortName());
        timeslot.setHexColor(item.getHexColor());
        timeslot.setDescription(item.getDescription());
        timeslot.setLateMinutes(item.getLateMinutes());
        timeslot.setEarlyLeaveMinutes(item.getEarlyMinutes());
        timeslot.setValidInStart(item.getValidInStart());
        timeslot.setValidInEnd(item.getValidInEnd());
        timeslot.setValidOutStart(item.getValidOutStart());
        timeslot.setValidOutEnd(item.getValidOutEnd());
        timeslot.setAutoInOutEnabled(item.isAutoInOutEnabled());

        SettingsData companySettings = profileService.getCompanySettings(false);
        Integer weekStart = companySettings.getOverallDatePickerWeekStart();
        for (EdsTimeSlotItem timeslotitem : timeslotitems) {
            if (timeslotitem.getDay() == 0) {
                timeslotitem.setStartTime(item.getSunday()[0]);
                timeslotitem.setEndTime(item.getSunday()[1]);
                timeslotitem.setLunchStart(item.getLunchSu()[0]);
                timeslotitem.setLunchEnd(item.getLunchSu()[1]);
                timeslotitem.setCoffeeStart(item.getCoffeeSu()[0]);
                timeslotitem.setCoffeeEnd(item.getCoffeeSu()[1]);
                timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 0 : weekStart.equals(FROM_SATURDAY) ? 1 : 6);
                timeSlotItemManager.update(timeslotitem);
            } else if (timeslotitem.getDay() == 1) {
                timeslotitem.setStartTime(item.getMonday()[0]);
                timeslotitem.setEndTime(item.getMonday()[1]);
                timeslotitem.setLunchStart(item.getLunchMo()[0]);
                timeslotitem.setLunchEnd(item.getLunchMo()[1]);
                timeslotitem.setCoffeeStart(item.getCoffeeMo()[0]);
                timeslotitem.setCoffeeEnd(item.getCoffeeMo()[1]);
                timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 1 : weekStart.equals(FROM_SATURDAY) ? 2 : 0);
                timeSlotItemManager.update(timeslotitem);
            } else if (timeslotitem.getDay() == 2) {
                timeslotitem.setStartTime(item.getTuesday()[0]);
                timeslotitem.setEndTime(item.getTuesday()[1]);
                timeslotitem.setLunchStart(item.getLunchTu()[0]);
                timeslotitem.setLunchEnd(item.getLunchTu()[1]);
                timeslotitem.setCoffeeStart(item.getCoffeeTu()[0]);
                timeslotitem.setCoffeeEnd(item.getCoffeeTu()[1]);
                timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 2 : weekStart.equals(FROM_SATURDAY) ? 3 : 1);
                timeSlotItemManager.update(timeslotitem);
            } else if (timeslotitem.getDay() == 3) {
                timeslotitem.setStartTime(item.getWednesday()[0]);
                timeslotitem.setEndTime(item.getWednesday()[1]);
                timeslotitem.setLunchStart(item.getLunchWe()[0]);
                timeslotitem.setLunchEnd(item.getLunchWe()[1]);
                timeslotitem.setCoffeeStart(item.getCoffeeWe()[0]);
                timeslotitem.setCoffeeEnd(item.getCoffeeWe()[1]);
                timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 3 : weekStart.equals(FROM_SATURDAY) ? 4 : 2);
                timeSlotItemManager.update(timeslotitem);
            } else if (timeslotitem.getDay() == 4) {
                timeslotitem.setStartTime(item.getThursday()[0]);
                timeslotitem.setEndTime(item.getThursday()[1]);
                timeslotitem.setLunchStart(item.getLunchTh()[0]);
                timeslotitem.setLunchEnd(item.getLunchTh()[1]);
                timeslotitem.setCoffeeStart(item.getCoffeeTh()[0]);
                timeslotitem.setCoffeeEnd(item.getCoffeeTh()[1]);
                timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 4 : weekStart.equals(FROM_SATURDAY) ? 5 : 3);
                timeSlotItemManager.update(timeslotitem);
            } else if (timeslotitem.getDay() == 5) {
                timeslotitem.setStartTime(item.getFriday()[0]);
                timeslotitem.setEndTime(item.getFriday()[1]);
                timeslotitem.setLunchStart(item.getLunchFr()[0]);
                timeslotitem.setLunchEnd(item.getLunchFr()[1]);
                timeslotitem.setCoffeeStart(item.getCoffeeFr()[0]);
                timeslotitem.setCoffeeEnd(item.getCoffeeFr()[1]);
                timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 5 : weekStart.equals(FROM_SATURDAY) ? 6 : 4);
                timeSlotItemManager.update(timeslotitem);
            } else if (timeslotitem.getDay() == 6) {
                timeslotitem.setStartTime(item.getSaturday()[0]);
                timeslotitem.setEndTime(item.getSaturday()[1]);
                timeslotitem.setLunchStart(item.getLunchSa()[0]);
                timeslotitem.setLunchEnd(item.getLunchSa()[1]);
                timeslotitem.setCoffeeStart(item.getCoffeeSa()[0]);
                timeslotitem.setCoffeeEnd(item.getCoffeeSa()[1]);
                timeslotitem.setDayOfWeek(weekStart.equals(FROM_SUNDAY) ? 6 : weekStart.equals(FROM_SATURDAY) ? 0 : 5);
                timeSlotItemManager.update(timeslotitem);
            }
        }
        //exceptional cases
        timeSlotItemManager.deleteExceptionalCaseTimeSlotItems(timeslot.getObjectID());
        Set<EdsTimeSlotItem> exceptionalCaseTimeSlotItem = createExceptionalCaseTimeSlotItem(item.getExceptionalCases(), timeslot);
        timeslot.setExceptionalCaseItems(exceptionalCaseTimeSlotItem);

        timeslot.setEffectiveFrom(item.getEffectiveDate());
        timeslot.setItems(timeslotitems);
        if (item.getAdditionalLeaveDays() != null) {
            timeslot.setAdditionalLeaveDays(item.getAdditionalLeaveDays());
        }
        if (item.getSelectedLeaveReasons() != null && item.getSelectedLeaveReasons().size() > 0) {
            Set<EdsLeaveReason> selectedReasons = new HashSet<>();
            item.getSelectedLeaveReasons().forEach(r -> {
                EdsLeaveReason reason = leaveReasonManager.get(r);
                selectedReasons.add(reason);
            });
            timeslot.setSelectedLeaveReasons(selectedReasons);
        }
        if (item.getReferenceLocale() != null && checkReferenceLocale(item.getReferenceLocale())) {
            timeslot.setLocale(allInOneServiceLocal.saveEntityLocale(item.getReferenceLocale()));
        }

        timeSlotManager.update(timeslot);

        updateTimeSlotHistory("Updated the Timeslot Effective Date: " + item.getEffectiveDate() + ", End Date: " + ServerUtils.addDays(item.getEffectiveDate(), 730), timeslot, null);

        //create new one year if today's date is not in datejoin table
        dashboardService.lastEnteredDate();
        List<EdsEmployee> employeeList = employeeManager.getEmployees(timeslot.getObjectID());

        boolean hasAssignEmployees = item.getSelectedEmployeeIds() != null && item.getSelectedEmployeeIds().length > 0;
        List<Integer> newMembersID = new ArrayList<>();
        if (hasAssignEmployees) {
            for (Integer member1 : item.getSelectedEmployeeIds()) {
                EdsEmployee member = employeeManager.get(member1);
                if (member != null) {
                    newMembersID.add(member.getObjectID());
                    member.setTimeSlot(timeslot);
                    employeeManager.update(member);
                    employeeManager.flush();
                }
            }
            saveAttendanceRawData(item.getSelectedEmployeeIds(), user, timeslot);
        }

        List<Integer> ids = new ArrayList<>();
        EdsTimeSlot defaultTimeslot = user.getCompany().getDefaultTimeSlot();
        for (EdsEmployee empl : employeeList) {
            if (!empl.getDeleted() && !newMembersID.contains(empl.getObjectID())) {
                ids.add(empl.getObjectID());
                spentEmployeeDailyLoadUpdateTimeSlot(empl);
                empl.setTimeSlot(defaultTimeslot);
                employeeManager.update(empl);
                employeeManager.flush();
            }
        }
        if (ids.size() > 0) {
            saveAttendanceRawData(ids.toArray(new Integer[]{}), user, defaultTimeslot);
        }

        EdsEmployee employee = user.getEmployee();
        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(TimeslotEventListenerImpl.TYPE, EVENT_TYPE_EDIT, employee, user);
        event.setSourceID(timeslot.getObjectID());
        baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, EVENT_TYPE_EDIT, employee, user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimeSlotHistoryList[] getTimeSlotHistories(Integer objectID, boolean isShift) {
        List<TimeSlotHistoryList> histories = new ArrayList<>();
        List<EdsTimeSlotHistory> timeSlotHistory = timeSlotHistoryManager.historyList(objectID, isShift);
        if (timeSlotHistory != null) {
            for (EdsTimeSlotHistory history : timeSlotHistory) {
                histories.add(getHistoryAsRPC(history));
            }
        }
        return histories.toArray(new TimeSlotHistoryList[]{});
    }

    private TimeSlotHistoryList getHistoryAsRPC(EdsTimeSlotHistory timeSlotHistory) {
        TimeSlotHistoryList historyList = new TimeSlotHistoryList();
        historyList.setObjectID(timeSlotHistory.getEntityID());
        historyList.setUpdaterID(timeSlotHistory.getUpdater() != null ? timeSlotHistory.getUpdater().getObjectID() : 0);
        if (timeSlotHistory.isSuperUser()) {
            historyList.setUpdater(Constants.defaultSupportName);
        } else {
            historyList.setUpdater(timeSlotHistory.getUpdater() != null ? timeSlotHistory.getUpdater().getName() : "Anonymous");
        }
        historyList.setCreationTime(timeSlotHistory.getCreationTime());
        String message = "";
        if (timeSlotHistory.getMessage() != null) {
            message = timeSlotHistory.getMessage().equals("Created the Timeslot") ? commonLocalizer.localize(PdfLocalizationName.timeslotCreated, "Created the Timeslot") : timeSlotHistory.getMessage();
            message = timeSlotHistory.getMessage().equals("Updated the Timeslot") ? commonLocalizer.localize(PdfLocalizationName.timeslotUpdated, "Updated the Timeslot") : message;
            message = timeSlotHistory.getMessage().equals("Timeslot deleted") ? commonLocalizer.localize(PdfLocalizationName.timeslotDeleted, "Timeslot deleted") : message;
        }
        historyList.setMessage(message);
        historyList.setUpdaterImageURL(timeSlotHistory.getUpdater() != null && timeSlotHistory.getUpdater().getPhoto() != null ? getImageUrl(timeSlotHistory.getUpdater().getPhoto().getObjectID()) : null);
        return historyList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HolidayHistoryList[] getHolidayHistories(Integer objectID) {
        List<HolidayHistoryList> histories = new ArrayList<>();
        List<EdsHolidayHistory> holidayHistories = holidayHistoryManager.historyList(objectID);
        if (holidayHistories != null) {
            for (EdsHolidayHistory history : holidayHistories) {
                histories.add(getHolidayHistoryAsRPC(history));
            }
        }
        return histories.toArray(new HolidayHistoryList[]{});
    }

    private HolidayHistoryList getHolidayHistoryAsRPC(EdsHolidayHistory holidayHistory) {
        HolidayHistoryList historyList = new HolidayHistoryList();
        historyList.setObjectID(holidayHistory.getEntityID());
        historyList.setUpdaterID(holidayHistory.getUpdater() != null ? holidayHistory.getUpdater().getObjectID() : 0);
        if (holidayHistory.isSuperUser()) {
            historyList.setUpdater(Constants.defaultSupportName);
        } else {
            historyList.setUpdater(holidayHistory.getUpdater() != null ? holidayHistory.getUpdater().getName() : "Anonymous");
        }
        historyList.setCreationTime(holidayHistory.getCreationTime());
        String message = "";
        if (holidayHistory.getMessage() != null) {
            message = holidayHistory.getMessage().equals("Created the Holiday") ? commonLocalizer.localize(PdfLocalizationName.holidayCreated, "Created the Holiday") : holidayHistory.getMessage();
            message = holidayHistory.getMessage().equals("Updated the Holiday") ? commonLocalizer.localize(PdfLocalizationName.holidayUpdated, "Updated the Holiday") : message;
            message = holidayHistory.getMessage().equals("Holiday deleted") ? commonLocalizer.localize(PdfLocalizationName.holidayDeleted, "Holiday deleted") : message;
        }
        historyList.setMessage(message);
        historyList.setUpdaterImageURL(holidayHistory.getUpdater() != null && holidayHistory.getUpdater().getPhoto() != null ? getImageUrl(holidayHistory.getUpdater().getPhoto().getObjectID()) : null);
        return historyList;
    }

    private String getImageUrl(Integer id) {
        EdsUpload upload = (EdsUpload) uploadManager.get(id);
        return uploadManager.getFileURL(upload);
    }

    public EdsTimeSlotItem[] getTimeslotMinutesArray(EdsTimeSlot timeSlot) {
        EdsTimeSlotItem[] timeslotMinutes = new EdsTimeSlotItem[timeSlot.getItems().size()];
        for (EdsTimeSlotItem timeSlotItem : timeSlot.getItems()) {
            timeslotMinutes[timeSlotItem.getDay()] = timeSlotItem;
        }
        return timeslotMinutes;

    }

    private void spentEmployeeDailyLoadUpdateTimeSlot(EdsEmployee employee) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
            return;
        }
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(employee.getObjectID());
        fp.setSickRequestStartDate(new java.sql.Date(new Date().getYear(), new Date().getMonth(), new Date().getDate()));
        fp.setSickRequestEndDate(new java.sql.Date(new Date().getYear(), new Date().getMonth(), new Date().getDate()));
        List<EdsTask> taskList = taskManager.list(fp);
        EdsEmployeeTask employeeTask = null;
        Set<EdsTimeSlotItem> timeSlotItem = employee.getTimeSlot().getItems();
        Map<Integer, Integer> available = new HashMap<>();
        for (EdsTimeSlotItem item : timeSlotItem) {
            available.put(item.getDay(), item.getEndTime() - item.getStartTime());
        }
        boolean p = false;
        for (EdsTask task : taskList) {
            if (task.getStatus().getCode().equals(EdsTask.COMPLETED) || task.getStatus().getCode().equals(EdsTask.CLOSED) || formatDate.format(task.getDueDate()).equals(formatDate.format(fp.getSickRequestEndDate()))) {
                continue;
            }
            Set<EdsEmployeeTask> empTaskList = task.getAssignments();
            for (EdsEmployeeTask et : empTaskList) {
                if (et.getProjectEmployee().getEmployeeDepartment().getEmployee().equals(employee)) {
                    employeeTask = et;
                    p = true;
                    break;
                }
            }
            if (employeeTask.getStatus().getCode().equals(EdsTask.COMPLETED) && employeeTask.getEstimatedTime() != null && employeeTask.getEstimatedTime() != 0) {
                continue;
            }
            if (employeeTask.getStatus().getCode().equals(EdsTask.NOT_STARTED) && p) {
                Calendar startDate = Calendar.getInstance();
                Calendar dueDate = Calendar.getInstance();
                startDate.setTime(task.getStartDate());
                dueDate.setTime(task.getDueDate());
                int k = 0;
                ArrayList<Calendar> availableDays = new ArrayList<>();
                while (formatDate.format(dueDate.getTime()).compareTo(formatDate.format(startDate.getTime())) >= 0) {
                    if (available.containsKey(startDate.get(Calendar.DAY_OF_WEEK) - 1) && available.get(startDate.get(Calendar.DAY_OF_WEEK) - 1) != null && available.get(startDate.get(Calendar.DAY_OF_WEEK) - 1) != 0) {
                        k++;
                        Calendar nonDate = Calendar.getInstance();
                        nonDate.setTime(startDate.getTime());
                        ServerUtils.setBeginningOfTheDay(nonDate);
                        availableDays.add(nonDate);
                    }
                    startDate.add(Calendar.DAY_OF_MONTH, 1);

                }
                if (k == 0) {
                    k = 1;
                }
                int dailyLoad = employeeTask.getEstimatedTime() != null ? employeeTask.getEstimatedTime() : 0 / k;
                int dailyLoadQ = employeeTask.getEstimatedTime() != null ? employeeTask.getEstimatedTime() : 0 % k;
                employeeTask.setDailyLoad(dailyLoad);
                employeeTaskManager.update(employeeTask);
                if (/*dailyLoad != 0*/dailyLoad >= 0) {
                    availabilityCircularResolver.createOrUpdateTimeSheetDataWithDailyEstimatedTime(employee, employeeTask, availableDays, dailyLoad, dailyLoadQ);
                }
                return;
            }
            int x = (fp.getSickRequestStartDate().getDate() - task.getStartDate().getDate()) * 100 / ((task.getDueDate().getDate() - task.getStartDate().getDate()) == 0 ? 1 : task.getDueDate().getDate() - task.getStartDate().getDate()); // how much percent ready
            int z = (employeeTask.getEstimatedTime() != null ? employeeTask.getEstimatedTime() : 0) / (employeeTask.getDailyLoad() != null && employeeTask.getDailyLoad() != 0 ? employeeTask.getDailyLoad() : 1); //  task work days
            int y = z / 100 * x;
            Calendar startDate = Calendar.getInstance();
            Calendar dueDate = Calendar.getInstance();
            startDate.setTime(fp.getSickRequestStartDate());
            dueDate.setTime(task.getDueDate());
            int k = 0;
            ArrayList<Calendar> availableDays = new ArrayList<>();
            while (formatDate.format(dueDate.getTime()).compareTo(formatDate.format(startDate.getTime())) >= 0) {
                if (available.containsKey(startDate.get(Calendar.DAY_OF_WEEK) - 1) && available.get(startDate.get(Calendar.DAY_OF_WEEK) - 1) != null && available.get(startDate.get(Calendar.DAY_OF_WEEK) - 1) != 0) {
                    k++;
                    Calendar nonDate = Calendar.getInstance();
                    nonDate.setTime(startDate.getTime());
                    ServerUtils.setBeginningOfTheDay(nonDate);
                    availableDays.add(nonDate);
                }
                startDate.add(Calendar.DAY_OF_MONTH, 1);

            }
            k = k + y;
            if (k == 0) {
                k = 1;
            }
            int dailyLoad = employeeTask.getEstimatedTime() != null ? employeeTask.getEstimatedTime() : 0 / k;
            int dailyLoadQ = employeeTask.getEstimatedTime() != null ? employeeTask.getEstimatedTime() : 0 % k;
            employeeTask.setDailyLoad(dailyLoad);
            if (/*dailyLoad != 0*/dailyLoad >= 0) {
                availabilityCircularResolver.createOrUpdateTimeSheetDataWithDailyEstimatedTime(employee, employeeTask, availableDays, dailyLoad, dailyLoadQ);
            }
            employeeTaskManager.update(employeeTask);
            p = false;
        }
    }

    public void deleteTimeslot(Integer objectID) {
        try {
            EdsUser user = timeSlotManager.getUser();
            EdsTimeSlot timeslot = timeSlotManager.get(objectID);
            timeslot.setDeleted(true);
            updateTimeSlotHistory("Timeslot deleted", timeslot, null);

            EdsEmployee member = (EdsEmployee) user;
//            member.setTimeSlot(timeslot);
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(TimeslotEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, member, user);
            event.setSourceID(timeslot.getObjectID());
            baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, user, user);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void deleteShiftSettings(Integer objectID) {
        try {
            EdsEmployee user = (EdsEmployee) timeSlotManager.getUser();
            EdsShiftSettings shiftSettings = shiftSettingsManager.get(objectID);
            shiftSettings.setDeleted(true);
            updateTimeSlotHistory("Timeslot deleted", null, shiftSettings);

            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(TimeslotEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, user, user);
            event.setSourceID(shiftSettings.getObjectID());
        } catch (Exception ignored) {

        }
    }

    public void deleteTimeslot(Integer objectID, Integer toTimeSlot) {
        //ATTENDANCERAWDATA >> TIMESLOT DELETE
        //create new one year if today's date is not in datejoin table
        dashboardService.lastEnteredDate();
        EdsUser user = timeSlotManager.getUser();
        try {
            EdsTimeSlot timeslot = timeSlotManager.get(objectID);
            if (toTimeSlot != null) {
                EdsTimeSlot toTimeslot = timeSlotManager.get(toTimeSlot);
                if (timeslot != null) {
                    List<EdsEmployee> employees = employeeManager.getEmployees(objectID);
                    if (employees != null && employees.size() > 0) {
                        for (EdsEmployee employee : employees) {
                            employee.setTimeSlot(toTimeslot);
                        }
                        List<Integer> employeeList = employees.stream().map(EdsUser::getObjectID).toList();
                        saveAttendanceRawData(employeeList.toArray(new Integer[]{}), user, toTimeslot);
                    }
                }
            }
            if (employeeManager.getEmployees(objectID).size() == 0) {
                assert timeslot != null;
                timeslot.setDeleted(true);
            }
            updateTimeSlotHistory("Timeslot deleted", timeslot, null);

            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(TimeslotEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, (EdsEmployee) user, user);
            event.setSourceID(timeslot.getObjectID());
            baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, user, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimeslotItem getEmpTimeslot(Integer employeeID) {
        EdsEmployee employee = employeeID != null
                ? employeeManager.get(employeeID)
                : employeeManager.getUser().getEmployee();
        TimeslotItem timeslotitem = new TimeslotItem();
        EdsTimeSlot timeslot = getTimeSlot(employee);
        timeslotitem.setObjectID(timeslot.getObjectID());
        timeslotitem.setName(timeslot.getName());
        timeslotitem.setDescription(timeslot.getDescription());
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(employee.getCompany().getObjectID());
        timeslotitem.setWeekStart(companySystemSettings.getOverallDatePickerWeekStart());
        timeslotitem.setEarlyMinutes(timeslot.getEarlyLeaveMinutes());
        timeslotitem.setLateMinutes(timeslot.getLateMinutes());
        timeslotitem.setValidInStart(timeslot.getValidInStart());
        timeslotitem.setValidInEnd(timeslot.getValidInEnd());
        timeslotitem.setValidOutStart(timeslot.getValidOutStart());
        timeslotitem.setValidOutEnd(timeslot.getValidOutEnd());
        timeslotitem.setAutoInOutEnabled(timeslot.isAutoInOutEnabled());
        for (EdsTimeSlotItem item : getTimeSlotItems(employee)) {
            if (item.getDay() == 0) {
                timeslotitem.setSunday(new int[]{item.getStartTime(), item.getEndTime()});
                timeslotitem.setCoffeeSu(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                timeslotitem.setLunchSu(new int[]{item.getLunchStart(), item.getLunchEnd()});
            } else if (item.getDay() == 1) {
                timeslotitem.setMonday(new int[]{item.getStartTime(), item.getEndTime()});
                timeslotitem.setCoffeeMo(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                timeslotitem.setLunchMo(new int[]{item.getLunchStart(), item.getLunchEnd()});
            } else if (item.getDay() == 2) {
                timeslotitem.setTuesday(new int[]{item.getStartTime(), item.getEndTime()});
                timeslotitem.setCoffeeTu(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                timeslotitem.setLunchTu(new int[]{item.getLunchStart(), item.getLunchEnd()});
            } else if (item.getDay() == 3) {
                timeslotitem.setWednesday(new int[]{item.getStartTime(), item.getEndTime()});
                timeslotitem.setCoffeeWe(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                timeslotitem.setLunchWe(new int[]{item.getLunchStart(), item.getLunchEnd()});
            } else if (item.getDay() == 4) {
                timeslotitem.setThursday(new int[]{item.getStartTime(), item.getEndTime()});
                timeslotitem.setCoffeeTh(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                timeslotitem.setLunchTh(new int[]{item.getLunchStart(), item.getLunchEnd()});
            } else if (item.getDay() == 5) {
                timeslotitem.setFriday(new int[]{item.getStartTime(), item.getEndTime()});
                timeslotitem.setCoffeeFr(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                timeslotitem.setLunchFr(new int[]{item.getLunchStart(), item.getLunchEnd()});
            } else if (item.getDay() == 6) {
                timeslotitem.setSaturday(new int[]{item.getStartTime(), item.getEndTime()});
                timeslotitem.setCoffeeSa(new int[]{item.getCoffeeStart(), item.getCoffeeEnd()});
                timeslotitem.setLunchSa(new int[]{item.getLunchStart(), item.getLunchEnd()});
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsTimeSlot.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(timeslot.getObjectID());
        ServerUtils.kpiLog(LOGGER, kpiLog, "View employee timeslot");
        return timeslotitem;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TimeslotItem> getEmpShiftTimeslotList() {
        List<EdsShiftSettings> shiftSettings = shiftSettingsManager.getShiftSettings(new ListingFilterParameter());
        List<TimeslotItem> timeslotItems = new ArrayList<>();
        for (EdsShiftSettings shiftSetting : shiftSettings) {
            TimeslotItem item = new TimeslotItem();
            item.setName(shiftSetting.getName());
            item.setShiftStartTime(shiftSetting.getStartTime());
            item.setShiftEndTime(shiftSetting.getEndTime());
            item.setShortName(shiftSetting.getShortName());
            item.setHexColor(shiftSetting.getHexColor());
            timeslotItems.add(item);
        }
        return timeslotItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<HolidayItem> getHolidays(ListingFilterParameter filterParametrs) {
        EdsUser user = holidayManager.getUser();
        EdsLocation location = user.getLocation();
        List<EdsHoliday> holidays;
        int totalCount;
        if (filterParametrs == null) {
            holidays = holidayManager.getHolidays(location, filterParametrs);
            totalCount = holidays.size();
        } else { // if admin or director
            holidays = holidayManager.list(filterParametrs, false);
            totalCount = holidayManager.list(filterParametrs, true).size();
        }

        holidays = ListUtils.getSublist(holidays, filterParametrs.getStart(), filterParametrs.getLimit());
        ArrayList<HolidayItem> result = new ArrayList<>();
        for (EdsHoliday holiday : holidays) {
            result.add(holiday.getRPC());
        }
        return new ListResult<>(result, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HolidayItem getHoliday(Integer holidayID) {
        HolidayItem hol = new HolidayItem();
        if (holidayID != null) {
            EdsHoliday holiday = holidayManager.get(holidayID);
            hol.setDescription(holiday.getDescription() != null && holiday.getDescription().length() > 0 ? holiday.getDescription() : "N/A");
            hol.setFrom(holiday.getStartDate() != null ? new DateNonConvertable(new Date(holiday.getStartDate().getTime())) : null);
            hol.setName(holiday.getName());
            hol.setDayOff(holiday.isDayOff());
            hol.setTo(holiday.getEndDate() != null ? new DateNonConvertable(new Date(holiday.getEndDate().getTime())) : null);
            hol.setTakenFromAnnual(holiday.isTakeAnnual());
            if (holiday.getRecurrenceID() != null) {
                EdsRecurrence recurrence = recurrenceManager.get(holiday.getRecurrenceID());
                if (recurrence != null) {
                    hol.setRepeat(true);
                    hol.setRepeatId(recurrence.getType());
                }
            }
            if (holiday.getLocations() != null && holiday.getLocations().size() > 0) {
                StringBuilder locationName = new StringBuilder();
                for (EdsLocation location : holiday.getLocations()) {
                    if (location != null && !hol.getLocationIds().contains(location.getObjectID())) {
                        hol.getLocationIds().add(location.getObjectID());
                        locationName.append(!Objects.equals(locationName.toString(), "") ? ", " : "").append(location.getName());
                    }
                }
                hol.setLocationName(locationName.toString());
            }
        }
        return hol;
    }

    public void createOrUpdateHoliday(HolidayItem holidayItem) {
        EdsHoliday holiday = new EdsHoliday();
        long startDiff = 0;
        if (holidayItem.getObjectID() != null) {
            holiday = holidayManager.get(holidayItem.getObjectID());
            startDiff = holidayItem.getFrom().getNonConvertedDate().getTime() - holiday.getStartDate().getTime();
        }

        //ATTENDANCERAWDATA >> HOLIDAY CREATE/UPDATE
        //check if locations changed
        if (holidayItem.getObjectID() != null) {
            List<EdsLocation> newLocations = new ArrayList<>();
            if (holidayItem.getLocationIds() != null && holidayItem.getLocationIds().size() > 0) {
                for (Integer locationID : holidayItem.getLocationIds()) {
                    newLocations.add(locationManager.get(locationID));
                }
            }
            //in case holiday is being edited and its location or start date or end date is changed then we should reset related attendance raw data records
            if ((!new HashSet<>(holiday.getLocations()).containsAll(newLocations) || !new HashSet<>(newLocations).containsAll(holiday.getLocations())) || (holidayItem.getFrom().getNonConvertedDate().getTime() != holiday.getStartDate().getTime() || holidayItem.getTo().getNonConvertedDate().getTime() != holiday.getEndDate().getTime()) || !holiday.isDayOff().equals(holidayItem.isDayOff()) || !holiday.isTakeAnnual().equals(holidayItem.isTakenFromAnnual())) {

                updateAttendanceRawdata(holiday, false, false);
            }
        }

        wrapHolidayItemToEdsHoliday(holidayItem, holiday);

        if (holiday.getLocations() != null) {
            holiday.getLocations().clear();
        }
        if (holidayItem.getLocationIds() == null || holidayItem.getLocationIds().size() == 0) {
            List<EdsLocation> locations = locationManager.getLocations(new ListingFilterParameter());
            for (EdsLocation location : locations) {
                holiday.getLocations().add(location);
            }
        } else if (holidayItem.getLocationIds() != null && holidayItem.getLocationIds().size() > 0) {
            for (Integer locationID : holidayItem.getLocationIds()) {
                EdsLocation location = locationManager.get(locationID);
                if (location != null && !holiday.getLocations().contains(location)) {
                    holiday.getLocations().add(location);
                }
            }
        }

        //set new attendance raw data records based on the holiday being edited/added
        updateAttendanceRawdata(holiday, holidayItem.isDayOff(), holidayItem.isTakenFromAnnual());

        boolean isNew = holidayManager.createOrUpdate(holiday);
        EdsUser user = userManager.getUser();
        if (isNew) {
            updateHolidayHistory(commonLocalizer.localize(PdfLocalizationName.holidayCreated, "Created the Holiday"), holiday);
            baseEventPostProcessor.registerEvent(HolidayEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, holiday, user);
            baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, EVENT_TYPE_EDIT, user, user);
        } else {
            updateHolidayHistory(commonLocalizer.localize(PdfLocalizationName.holidayUpdated, "Updated the Holiday"), holiday);
            baseEventPostProcessor.registerEvent(HolidayEventListenerImpl.TYPE, EVENT_TYPE_EDIT, holiday, user);
            baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, EVENT_TYPE_EDIT, user, user);
        }

        if (holidayItem.isRepeat()) {
            //Save recurring instances
            long dateDiff = holiday.getEndDate().getTime() - holiday.getStartDate().getTime();
            EdsRecurrence recurrence;
            if (holidayItem.getObjectID() == null) {
                saveRecurrenceAndHolidays(holidayItem, holiday);
            } else {
                if (holiday.getRecurrenceID() != null) {
                    recurrence = recurrenceManager.get(holiday.getRecurrenceID());
                    if (recurrence != null) {
                        //
                        if ((holidayItem.getRepeatId() != null && holidayItem.getRepeatId() == RECURRENCE_TYPE_MONTHLY && Integer.valueOf(120).equals(recurrence.getOccurrence())) || (holidayItem.getRepeatId() != null && holidayItem.getRepeatId() == RECURRENCE_TYPE_YEARLY && Integer.valueOf(10).equals(recurrence.getOccurrence()))) {
                            ArrayList<EdsHoliday> holidays = holidayManager.getHolidaysByRecurrenceID(recurrence.getObjectID());
                            if (holidays != null && !holidays.isEmpty()) {
                                for (EdsHoliday edsHoliday : holidays) {
                                    if (edsHoliday.getObjectID().equals(holiday.getObjectID())) {
                                        continue;
                                    }
                                    updateAttendanceRawdata(edsHoliday, false, false);
                                    Date startDate = (Date) edsHoliday.getStartDate().clone();
                                    wrapHolidayItemToEdsHoliday(holidayItem, edsHoliday);
                                    edsHoliday.setStartDate(new Date(startDate.getTime() + startDiff));
                                    edsHoliday.setEndDate(new Date(edsHoliday.getStartDate().getTime() + dateDiff));
                                    if (holidayItem.getLocationIds() != null && holidayItem.getLocationIds().size() > 0) {
                                        for (Integer locationID : holidayItem.getLocationIds()) {
                                            EdsLocation location = locationManager.get(locationID);
                                            if (location != null && !edsHoliday.getLocations().contains(location)) {
                                                edsHoliday.getLocations().add(location);
                                            }
                                        }
                                    }
                                    ////////////////
                                    updateAttendanceRawdata(edsHoliday, edsHoliday.isDayOff(), edsHoliday.isTakeAnnual());
                                    ////////////////
                                    holidayManager.update(edsHoliday);
                                }
                                //update recurrence date
                                if (holidayItem.getFrom() != null && holidayItem.getFrom().getNonConvertedDate() != null) {
                                    Calendar startCalendar = new GregorianCalendar();
                                    startCalendar.setTime(holidayItem.getFrom().getNonConvertedDate());
                                    startCalendar.set(Calendar.SECOND, 0);
                                    startCalendar.set(Calendar.MILLISECOND, 0);
                                    recurrence.setStartDate(startCalendar.getTime());

                                    recurrence.setMonthlyOrYearlyDay(holidayItem.getFrom().getNonConvertedDate().getDate());
                                    recurrence.setYearlyMonth(holidayItem.getFrom().getNonConvertedDate().getMonth() + 1);
                                }
                                if (holidayItem.getTo() != null && holidayItem.getTo().getNonConvertedDate() != null) {
                                    Calendar endCalendar = new GregorianCalendar();
                                    endCalendar.setTime(holidayItem.getTo().getNonConvertedDate());
                                    endCalendar.set(Calendar.SECOND, 0);
                                    endCalendar.set(Calendar.MILLISECOND, 0);
                                    recurrence.setEndDate(endCalendar.getTime());
                                } else {
                                    recurrence.setEndDate(null);
                                }
                            }
                        } else {
                            ArrayList<EdsHoliday> holidaysByRecurrenceID = holidayManager.getHolidaysByRecurrenceID(holiday.getRecurrenceID());
                            if (holidaysByRecurrenceID != null && holidaysByRecurrenceID.size() > 0) {
                                for (EdsHoliday edsHoliday : holidaysByRecurrenceID) {
                                    updateAttendanceRawdata(edsHoliday, false, false);
                                }
                            }
                            holidayManager.deleteHoliday(holiday.getObjectID());
                            //
                            saveRecurrenceAndHolidays(holidayItem, holiday);
                        }
                    } else {
                        saveRecurrenceAndHolidays(holidayItem, holiday);
                    }
                } else {
                    saveRecurrenceAndHolidays(holidayItem, holiday);
                }
            }
        } else {
            if (holiday.getRecurrenceID() != null) {
                holidayManager.removeRecurrenceIDFromRecurringHoliday(holiday.getRecurrenceID());
                recurrenceService.updateRecurrence(holiday.getRecurrenceID(), true, true);
            }
        }
    }

    private void updateAttendanceRawdata(EdsHoliday holiday, boolean isHoliday, boolean takenFromAllowance) {
        List<Integer> locationIds = null;
        if (holiday.getLocations() != null && holiday.getLocations().size() > 0) {
            locationIds = holiday.getLocations().stream().map(EdsLocation::getObjectID).collect(Collectors.toList());
        }

        attendanceRawDataManager.updateHolidays(holiday.getStartDate(), holiday.getEndDate(), locationIds, isHoliday, takenFromAllowance);
    }

    private void wrapHolidayItemToEdsHoliday(HolidayItem holidayItem, EdsHoliday holiday) {
        String description = holidayItem.getDescription();
        holiday.setDescription(description.length() > 3000 ? description.substring(0, 3000) : description);
        holiday.setEndDate(holidayItem.getTo().getNonConvertedDate());
        holiday.setTakeAnnual(holidayItem.isTakenFromAnnual());
        holiday.setStartDate(holidayItem.getFrom().getNonConvertedDate());
        holiday.setAllDay(holidayItem.isAllDay());
        holiday.setName(holidayItem.getName());
        holiday.setDayOff(holidayItem.isDayOff());
    }

    private void saveRecurrenceAndHolidays(HolidayItem holidayItem, EdsHoliday holiday) {
        RecurrenceJobItem jobItem = new RecurrenceJobItem();
        jobItem.setEnabled(true);
        jobItem.setBusObjectId(holiday.getObjectID());
        jobItem.setStartDate(holidayItem.getFrom().getNonConvertedDate());
        jobItem.setEndDate(holidayItem.getTo().getNonConvertedDate());
        jobItem.setEndType(END_AFTER_OCCURRENCES);
        if (holidayItem.getRepeatId() == RECURRENCE_TYPE_YEARLY) {
            jobItem.setType(RECURRENCE_TYPE_YEARLY);
        } else {
            jobItem.setType(RECURRENCE_TYPE_MONTHLY);
        }
        jobItem.setJobType(RECURRING_HOLIDAY);
        jobItem.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
        jobItem.setMonthlyOrYearlyDay(holidayItem.getFrom().getNonConvertedDate().getDate());
        jobItem.setYearlyMonth(holidayItem.getFrom().getNonConvertedDate().getMonth() + 1);
        jobItem.setInterval(1);
        if (holidayItem.getRepeatId() != null && holidayItem.getRepeatId() == RECURRENCE_TYPE_MONTHLY) {
            jobItem.setOccurrence(120); // 10 years
        } else {
            if (holidayItem.getRepeatId() != null && holidayItem.getRepeatId() == RECURRENCE_TYPE_YEARLY) {
                jobItem.setOccurrence(10); // 10 years
            }
        }

        long dateDiff = holiday.getEndDate().getTime() - holiday.getStartDate().getTime();
        Integer recurrenceID = recurrenceService.saveRecurrenceJob(jobItem);
        EdsRecurrence recurrence = recurrenceManager.get(recurrenceID);
        recurrence.setChanged(false);
        EdsRecurrence tempRecurrence = recurrence.cloneShallow();
        holiday.setRecurrenceID(recurrenceID);
        tempRecurrence.setEndDate(null);
        List<Date> recurringDates = recurrenceService.getRecurringDates(tempRecurrence);
        if (recurringDates != null && !recurringDates.isEmpty()) {
            List<Date> recDates;
            if (recurringDates.size() > CREATE_EVENT_LIMIT) {
                recDates = recurringDates.subList(1, CREATE_EVENT_LIMIT);
                recurrence.setBusObjectParams(String.valueOf(recurringDates.size() > CREATE_EVENT_LIMIT ? recurringDates.size() - CREATE_EVENT_LIMIT : recurringDates.size()));
                recurrence.setExtendDate(recurringDates.get(CREATE_EVENT_LIMIT - CREATE_EVENT_INDEX));
                recurrence.setEndDate(recurringDates.get(CREATE_EVENT_LIMIT));
            } else {
                recDates = recurringDates.subList(1, recurringDates.size());
            }
            recurrenceManager.update(recurrence);

            for (Date date : recDates) {
                EdsHoliday tempHoliday = holiday.cloneShallow();
                tempHoliday.setObjectID(null);
                tempHoliday.setStartDate(date);
                tempHoliday.setEndDate(new Date(date.getTime() + dateDiff));
                tempHoliday.setLocations(new ArrayList<>());
                holidayManager.create(tempHoliday);
                if (holidayItem.getLocationIds() != null && holidayItem.getLocationIds().size() > 0) {
                    for (Integer locationID : holidayItem.getLocationIds()) {
                        EdsLocation location = locationManager.get(locationID);
                        tempHoliday.getLocations().add(location);
                    }
                }
                ////////////////
                updateAttendanceRawdata(tempHoliday, tempHoliday.isDayOff(), tempHoliday.isTakeAnnual());
                ////////////////
            }
        }
    }

    public void deleteHoliday(Integer holidayID) {
        EdsHoliday holiday = holidayManager.get(holidayID);
        updateAttendanceRawdata(holiday, false, false);

        if (holiday.getRecurrenceID() != null) {
            ArrayList<EdsHoliday> holidaysByRecurrenceID = holidayManager.getHolidaysByRecurrenceID(holiday.getRecurrenceID());
            if (holidaysByRecurrenceID != null && holidaysByRecurrenceID.size() > 0) {
                for (EdsHoliday edsHoliday : holidaysByRecurrenceID) {
                    updateAttendanceRawdata(edsHoliday, false, false);
                }
            }
        }
        EdsUser user = userManager.getUser();
        updateHolidayHistory("Holiday deleted", holiday);
        baseEventPostProcessor.registerEvent(HolidayEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, holiday, user);
        baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, EVENT_TYPE_EDIT, user, user);
        holidayManager.deleteHoliday(holidayID);
    }

    @Transactional
    public void createRecurringHoliday() {
        ArrayList<EdsRecurrence> eventRecurrences = recurrenceManager.getFeaturedItemsRecurrences(RECURRING_HOLIDAY);
        if (eventRecurrences != null && !eventRecurrences.isEmpty()) {
            for (EdsRecurrence recurrence : eventRecurrences) {
                ServerSecurityContext.getInstance().setCompanyId(recurrence.getCompanyID());
                ServerSecurityContext.getInstance().setDatabase(jdbcSpringManager.getCompanyClusterType(recurrence.getCompanyID()));
                try {
                    EdsHoliday holiday = holidayManager.get(recurrence.getBusObjectId());
                    Integer userID = recurrence.getUserID();
                    if (holiday != null && userID != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(userID);

                        EdsRecurrence tempRecurrence = recurrence.cloneShallow();
                        tempRecurrence.setStartDate(recurrence.getEndDate());
                        tempRecurrence.setEndDate(null);
                        tempRecurrence.setOccurrence(Integer.valueOf(tempRecurrence.getBusObjectParams()));
                        List<Date> recurringDates = recurrenceService.getRecurringDates(tempRecurrence);
                        if (recurringDates != null && !recurringDates.isEmpty()) {
                            long dateDiff = holiday.getEndDate().getTime() - holiday.getStartDate().getTime();
                            List<Date> recDates = recurringDates;
                            if (recurringDates.size() > CREATE_EVENT_LIMIT) {
                                recDates = recurringDates.subList(0, CREATE_EVENT_LIMIT);
                                recurrence.setBusObjectParams(String.valueOf(recurringDates.size() > CREATE_EVENT_LIMIT ? recurringDates.size() - CREATE_EVENT_LIMIT : recurringDates.size()));
                                recurrence.setExtendDate(recurringDates.get(CREATE_EVENT_LIMIT - CREATE_EVENT_INDEX));
                                recurrence.setEndDate(recurringDates.get(CREATE_EVENT_LIMIT));
                            }
                            List<EdsLocation> locations = holiday.getLocations();
                            for (Date date : recDates) {
                                EdsHoliday tempHoliday = holiday.cloneShallow();
                                tempHoliday.setStartDate(date);
                                tempHoliday.setEndDate(new Date(date.getTime() + dateDiff));
                                tempHoliday.setLocations(new ArrayList<>());
                                holidayManager.create(tempHoliday);
                                if (locations != null) {
                                    for (EdsLocation location : locations) {
                                        tempHoliday.getLocations().add(location);
                                    }
                                }
                            }
                            holidayManager.flush();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ServerSecurityContext.getInstance().setStaticUserID(null);
                ServerSecurityContext.getInstance().removeCompanyId();
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTimeslotList() {
        List<EdsTimeSlot> list = timeSlotManager.getTimeslots(new ListingFilterParameter());
        SelectItem[] items = new SelectItem[list.size()];
        int i = 0;
        for (EdsTimeSlot timeslot : list) {
            items[i] = new SelectItem();
            items[i].setId(timeslot.getObjectID());
            items[i].setName(timeslot.getName());
            items[i].setDescription(ServerUtils.getDailyAverageTimeslotMinutes(timeslot.getItems()).toString());
            i++;
        }
        return items;
    }

    public LeaveSettingsItem getLeaveRequestSettingsData() {
        LeaveSettingsItem result = new LeaveSettingsItem();
        result.setTimeSlots(getTimeslotList());
        result.setLrSettingsItem(profileService.getLrSettingsItem());
        result.setCurrentDate(new Date().getTime());
        EdsLeaveReason reason = leaveReasonManager.findByCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
        result.setProrata(reason != null && reason.hasProrata());
        return result;
    }

    @Override
    public void updateEmpBenefitAllowance(AnnualLeaveItem item) {
        EdsEmployee employee = employeeManager.get(item.getEmployeeId());
        EdsEmployeeBenefitAllowance benefitAllowance = employeeBenefitAllowanceManager.getBenefitAllowance(item.getAllowanceYear(), employee.getObjectID(), item.getObjectID());
        if (benefitAllowance == null) {
            benefitAllowance = new EdsEmployeeBenefitAllowance();
            benefitAllowance.setAllowanceYear(item.getAllowanceYear());
            benefitAllowance.setEmployee(employee);
            benefitAllowance.setAllowance(item.getAnnualallowancedays());
            benefitAllowance.setBenefit(item.getObjectID() != null ? benefitManager.get(item.getObjectID()) : null);
            employee.getBenefitAllowance().add(benefitAllowance);
        } else {
            benefitAllowance.setAllowance(item.getAnnualallowancedays());
        }
        employeeManager.update(employee);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CalendarItems getCalendarItems(Integer employeeID, String reasonCode, DateNonConvertable displayFirstDay, DateNonConvertable displayLastDay) {

        List<Object> employeeCalendarItems = sickRequestManager.getEmployeeCalendarItems(employeeID, displayFirstDay.getNonConvertedDate(), displayLastDay.getNonConvertedDate());

        ArrayList<CalendarItemRpc> offDaysList = new ArrayList<>();
        ArrayList<CalendarItemRpc> unAvaList = new ArrayList<>();
        ArrayList<CalendarItemRpc> defaultHolidaysList = new ArrayList<>();
        ArrayList<CalendarItemRpc> leaveReqByMoneyList = new ArrayList<>();

        if (employeeCalendarItems != null) {
            for (Object object : employeeCalendarItems) {
                Object[] data = (Object[]) object;

                Date sDate = (Date) (data[0]);
                Boolean dayOff = (Boolean) (data[2] != null ? data[2] : Boolean.FALSE);
                Boolean isHoliday = (Boolean) (data[3] != null ? data[3] : Boolean.FALSE);
                Boolean isLeaveR = (Boolean) (data[4] != null ? data[4] : Boolean.FALSE);
                Boolean holidayfromannualleave = (Boolean) (data[7] != null ? data[7] : Boolean.FALSE);
                Boolean isLeaveReqByMoney = (Boolean) (data[5] != null ? data[5] : Boolean.FALSE);
                String color = (String) (data[8] != null && (data[4] != null || data[5] != null) ? data[8] : null);
                String leaveReasonName = (String) (data[9] != null && (data[4] != null || data[5] != null) ? data[9] : null);
                String leaveReasonCode = (String) (data[10] != null && (data[4] != null || data[5] != null) ? data[10] : null);
                Boolean markAsDraft = (Boolean) (data[11] != null && (data[4] != null || data[5] != null) ? data[11] : Boolean.FALSE);

                //
                if (sDate != null) {
                    //day off
                    if (dayOff && !isHoliday) {
                        CalendarItemRpc dayOffCalendarItemRpc = new CalendarItemRpc();
                        dayOffCalendarItemRpc.setNonConvertable(new DateNonConvertable(sDate));
                        offDaysList.add(dayOffCalendarItemRpc);
                    }
                    //isHoliday
                    if (isHoliday && (!isLeaveR || !holidayfromannualleave)) {
                        CalendarItemRpc isHolidayCalendarItemRpc = new CalendarItemRpc();
                        isHolidayCalendarItemRpc.setNonConvertable(new DateNonConvertable(sDate));
                        isHolidayCalendarItemRpc.setSelected(holidayfromannualleave);
                        defaultHolidaysList.add(isHolidayCalendarItemRpc);
                    } else if (isLeaveR && (isHoliday ? holidayfromannualleave : true) && !isLeaveReqByMoney && !markAsDraft) {
                        CalendarItemRpc isLeaveRCalendarItemRpc = new CalendarItemRpc();
                        isLeaveRCalendarItemRpc.setNonConvertable(new DateNonConvertable(sDate));
                        isLeaveRCalendarItemRpc.setColorHex(color.replace("#", ""));
                        isLeaveRCalendarItemRpc.setName(commonLocalizer.localize(leaveReasonCode, leaveReasonName));
                        isLeaveRCalendarItemRpc.setSelected(holidayfromannualleave);
                        unAvaList.add(isLeaveRCalendarItemRpc);
                    }
                    //isLeaveReqByMoney.
                    if (isLeaveReqByMoney && !isHoliday && !markAsDraft) {
                        CalendarItemRpc leaveReqByMoneyCalendarItemRpc = new CalendarItemRpc();
                        leaveReqByMoneyCalendarItemRpc.setNonConvertable(new DateNonConvertable(sDate));
                        leaveReqByMoneyCalendarItemRpc.setColorHex(color.replace("#", ""));
                        leaveReqByMoneyCalendarItemRpc.setName(commonLocalizer.localize(leaveReasonCode, leaveReasonName));
                        leaveReqByMoneyList.add(leaveReqByMoneyCalendarItemRpc);
                    }
                }
            }
        }

        EdsTimeSlot empTimeSlot = employeeManager.getEmployeeTimeSlot(employeeID);
        CalendarItems calendarItems = new CalendarItems();
        calendarItems.setOffDays(offDaysList.toArray(new CalendarItemRpc[]{}));
        calendarItems.setUnAva(unAvaList.toArray(new CalendarItemRpc[]{}));
        calendarItems.setDefaultHolidayDays(defaultHolidaysList.toArray(new CalendarItemRpc[]{}));
        calendarItems.setDaysToCountAsLeave(empTimeSlot.getAdditionalLeaveDays());
        if (empTimeSlot.getSelectedLeaveReasons() != null && empTimeSlot.getSelectedLeaveReasons().size() > 0) {
            empTimeSlot.getSelectedLeaveReasons().forEach(lr -> {
                if (reasonCode != null && reasonCode.equals(lr.getCode())) {
                    calendarItems.setExceptionalTimeSlot(true);
                }
            });
        }
        calendarItems.setLeaveReqByMoney(leaveReqByMoneyList.toArray(new CalendarItemRpc[]{}));

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsHoliday.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(LOGGER, kpiLog, "Get calendar");

        return calendarItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyEmployeesAsAdmin() {
        EdsUser user = employeeManager.getUser();
        ListingFilterParameter fpE = new ListingFilterParameter();
        fpE.setViewAsId(EdsRole.DR);
        fpE.setResignedEmployeesIncluded(false);
        List<EdsEmployee> employees = employeeManager.list(fpE);
        SelectItem[] result = new SelectItem[employees.size()];
        int i = 0;
        for (EdsEmployee employee : employees) {
            if (!employee.getDeleted()) {
                String departmentIdAndName = employee.getTeam() != null ? (employee.getTeam().getObjectID() + "#" + employee.getTeam().getName()) : "";
                if (!employee.equals(user)) {
                    result[i] = new SelectItem(employee.getObjectID(), employee.getFullName(), departmentIdAndName);
                } else {
                    result[i] = new SelectItem(employee.getObjectID(), employee.getFullName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"), departmentIdAndName);
                }
                i++;
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTeamsList() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(EdsRole.DR);
        List<EdsDepartment> teams = departmentManager.list(fp);
        SelectItem[] result = new SelectItem[teams.size() + 1];
        result[0] = new SelectItem(-1, "All");
        int i = 1;
        for (EdsDepartment team : teams) {
            result[i] = new SelectItem();
            result[i].setId(team.getObjectID());
            result[i].setName(team.getName());
            i++;
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<TimeslotSetting> getEmployeesAndTimeslot(ListingFilterParameter filterParameters) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsTimeSlot.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(LOGGER, kpiLog, "Get Annual leave allowance list");
        filterParameters.setDoNotExportToQB(true);
        ListResult<EmployeeListItem> employeeList = employeeService.getEmployeeList(filterParameters);
        List<EmployeeListItem> list = employeeList.getList();
        TimeslotSetting[] results = new TimeslotSetting[list.size()];
        int i = 0;
        HashMap<Integer, EdsAnnualLeaveAllowance> allowanceHashMap = new HashMap<>();
        EdsReference approvedStatus = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);
        if (list.size() > 0) {
            List<Integer> employeeIDs = new ArrayList<>();
            for (EmployeeListItem employee : list) {
                employeeIDs.add(employee.getObjectID());
            }

            List<EdsAnnualLeaveAllowance> leaveAllowances = annualLeaveAllowanceManager.getLeaveAllowancesByReason(filterParameters.getYear(), employeeIDs, EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
            for (EdsAnnualLeaveAllowance annualLeaveAllowance : leaveAllowances) {
                allowanceHashMap.put(annualLeaveAllowance.getEmployee().getObjectID(), annualLeaveAllowance);
            }

            ListingFilterParameter fpCurrent = new ListingFilterParameter();
            fpCurrent.setYear(filterParameters.getYear());
            fpCurrent.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
            fpCurrent.setStatusID(approvedStatus.getObjectID());
            fpCurrent.setAnnualLeave(true);
            fpCurrent.setAvoidZero(true);

            //we get last year allowance and subtract last year spent allowance,
            //and put it into lastYearLeft >> after this loop, lasYearLeft will contain Left hours from last year allowance
        }
        EdsTimeSlot defaultTimeSlot = employeeManager.getUser().getCompany().getDefaultTimeSlot();

        DecimalFormat df = new DecimalFormat("0.#");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        boolean isSickLeaveSettingsCalculationEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SICK_LEAVE_SETTINGS_CALCULATION);
        LRSettingsItem lrSettingsItem = profileService.getLrSettingsItem();
        boolean isPreviousYearAllowanceAvailable = isSickLeaveSettingsCalculationEnabled && lrSettingsItem != null && lrSettingsItem.getCopyPreviousYearAllowances() && lrSettingsItem.getUsageDeadline() != null;
        if (isPreviousYearAllowanceAvailable) {
            ListingFilterParameter fpCurrent = new ListingFilterParameter();
            fpCurrent.setYear(filterParameters.getYear());
            fpCurrent.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
            fpCurrent.setStatusID(approvedStatus.getObjectID());
            fpCurrent.setAnnualLeave(true);
            fpCurrent.setDate(new Date(lrSettingsItem.getUsageDeadline()));
        }
        for (EmployeeListItem employee : list) {
            results[i] = new TimeslotSetting();
            results[i].setDepartmentName(employee.getDepartment());
            results[i].setEmployeeName(employee.getFullName());
            Integer employeeId = employee.getObjectID();
            results[i].setEmployeeID(employeeId);
            EdsTimeSlot empTimeSlot = employeeManager.getEmployeeTimeSlot(employeeId);
            if (empTimeSlot != null) {
                results[i].setTimeslotID(empTimeSlot.getObjectID());
                results[i].setTimeslotName(empTimeSlot.getName());
            } else {
                results[i].setTimeslotID(defaultTimeSlot.getObjectID());
                results[i].setTimeslotName(defaultTimeSlot.getName());
            }
            //current year total allowance
            if (allowanceHashMap.containsKey(employeeId)) {
                EdsAnnualLeaveAllowance allowance = allowanceHashMap.get(employeeId);
                results[i].setAnnualAllowance(df.format(allowance.getAllowanceDays()));
            } else {
                results[i].setAnnualAllowance("0.0");
            }
            i++;
        }
        return new ListResult<>(new ArrayList<>(Arrays.asList(results)), employeeList.getTotal());
    }

    @Override
    public ListResult<AnnualLeaveItem> getEmployeeBenefitList(ListingFilterParameter filterParametrs) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsTimeSlot.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(LOGGER, kpiLog, "Get Employee benefit allowance list");
        ListResult<EmployeeListItem> employeeList = employeeService.getEmployeeList(filterParametrs);
        List<EmployeeListItem> list = employeeList.getList();
        AnnualLeaveItem[] results = new AnnualLeaveItem[list.size()];
        int i = 0;
        Map<Integer, Double> benefitItems = new HashMap<>();
        if (list.size() > 0) {
            List<EdsBenefit> benefits = benefitManager.getBenefitList(filterParametrs);
            for (EdsBenefit benefit : benefits) {
                benefitItems.put(benefit.getObjectID(), 0.0);
            }
            HashMap<Integer, Double> b;
            List<EdsEmployeeBenefitAllowance> annualAllowances;
            for (EmployeeListItem employee : list) {
                b = new HashMap<>(benefitItems);
                results[i] = new AnnualLeaveItem();
                results[i].setDepartmentName(employee.getDepartment());
                results[i].setEmployeeName(employee.getFullName());
                results[i].setEmployeeId(employee.getObjectID());
                annualAllowances = employeeBenefitAllowanceManager.getBenefitAllowanceByEmpID(filterParametrs.getYear(), employee.getObjectID());
                if (annualAllowances != null && annualAllowances.size() > 0) {
                    for (EdsEmployeeBenefitAllowance allowance : annualAllowances) {
                        results[i].setObjectID(allowance.getBenefit().getObjectID());
                        b.put(allowance.getBenefit().getObjectID(), allowance.getAllowance());
                    }
                }
                results[i].setAllowanceByBenefit(b);
                i++;
            }
        }
        return new ListResult<>(new ArrayList<>(Arrays.asList(results)), employeeList.getTotal());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmployeeViewItem getEmployee(Integer objectID) {
        EdsEmployee employee = employeeManager.get(objectID);
        EmployeeViewItem result = new EmployeeViewItem();
        ListingFilterParameter fp = new ListingFilterParameter();
        if (employee != null) {
            fp.setEmployeeId(objectID);
            fp.setViewAsId(EdsRole.DR);
            List<EdsProject> projects = projectManager.list(fp);
            if (projects.size() > 0) {
                EmployeeProjectsListItem[] employeeprojects = new EmployeeProjectsListItem[projects.size()];
                int i = 0;
                for (EdsProject project : projects) {
                    employeeprojects[i] = new EmployeeProjectsListItem();
                    employeeprojects[i].setName(project.getName());
                    employeeprojects[i].setDescription(project.getDescription());
                    i++;
                }
                result.setProjects(employeeprojects);
            }
            if (employee.getEmployeeTeam() != null && employee.getEmployeeTeam().getTeam() != null) {
                EmployeeManagedDepartment department = new EmployeeManagedDepartment();
                department.setName(employee.getEmployeeTeam().getTeam().getName());
                department.setDescription(employee.getEmployeeTeam().getTeam().getDescription());
                Set<EdsEmployeeDepartment> employeeDepartments = employee.getEmployeeTeam().getTeam().getMembers();
                int i = 0;
                for (EdsEmployeeDepartment emplDep : employeeDepartments) {
                    if (!emplDep.getDeleted()) {
                        i++;
                    }
                }
                department.setEmployees(i);
                result.setDepartments(department);
            }
            result.setObjectID(employee.getObjectID());
            if (employee.getPosition() != null) {
                result.setPosition(employee.getPosition().getName());
            }
            result.setStartDate(employee.getStartDate() != null ? new DateNonConvertable(employee.getStartDate()) : null);
            result.setUserName(employee.getEmail());
            result.setFirstName(employee.getFirstName() != null ? employee.getFirstName() : "N/A");
            result.setLastName(employee.getLastName() != null ? employee.getLastName() : "N/A");
            result.setMiddleName(employee.getMiddleName() != null ? employee.getMiddleName() : "N/A");
            result.setEmail(employee.getEmail() != null ? employee.getEmail() : "N/A");
            result.setEmployeeCode(employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null ? employee.getProfile().getEmployeeCode() : "N/A");
            result.setSupervisor(employee.getProfile() != null && employee.getProfile().getReportsTo() != null ? employee.getProfile().getReportsTo().getName() : "N/A");
            result.setSupervisorCode(employee.getProfile() != null && employee.getProfile().getReportsTo() != null && employee.getProfile().getReportsTo().getProfile() != null && employee.getProfile().getReportsTo().getProfile().getEmployeeCode() != null ? employee.getProfile().getReportsTo().getProfile().getEmployeeCode() : "N/A");

            if (employee.getProfile() != null && employee.getProfile().getContact() != null && employee.getProfile().getContact().getAddresses() != null) {
                List<EdsAddress> addresses = employee.getProfile().getContact().getAddresses();
                if (addresses != null && addresses.size() > 0) {
                    EdsAddress address = addresses.get(0);
                    if (address != null) {
                        result.setHomeAddress(address.getAddress() != null ? address.getAddress() : "N/A");
                        result.setCityTown(address.getCity() != null ? address.getCity() : "N/A");
                        result.setCountry(address.getCountry() != null ? address.getCountry().getName() : "N/A");
                        result.setRegion(address.getState() != null ? address.getState().getName() : "N/A");
                        result.setHomeAddress(address.getZipCode() != null ? address.getZipCode() : "N/A");
                    }
                }
            } else {
                result.setHomeAddress("N/A");
                result.setCityTown("N/A");
                result.setCountry("N/A");
                result.setRegion("N/A");
                result.setPostCode("N/A");
            }
            result.setHomePhone(employee.getHomePhoneFirst() != null ? employee.getHomePhoneFirst() : "N/A");
            result.setMobilePhone(employee.getMobilePhoneFirst() != null ? employee.getMobilePhoneFirst() : "N/A");
            result.setWorkPhone(employee.getWorkPhoneFirst() != null ? employee.getWorkPhoneFirst() : "N/A");
            result.setEndDate(employee.getEndDate() != null ? new DateNonConvertable(employee.getEndDate()) : null);

            result.setTrainingNeeds(employee.getTrainingNeeds());
            result.setGrade(employee.getGrade() != null ? wfmMessageSource.localizeRef(employee.getGrade()) : "N/A");
            result.setTimeSlot(employee.getTimeSlot() != null ? employee.getTimeSlot().getName() : "N/A");
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InOutItem getDailyInTimePerUser(Integer int_employeeID, Date startDate, Date endDate) {
        InOutItem inOutItem = new InOutItem();
        List<Object> objects = attendanceRawDataManager.getDailyInTimes(int_employeeID, startDate, endDate);
        HashMap<Date, Double> dailyInOutHours = new HashMap<>();
        for (Object object : objects) {
            Object[] data = (Object[]) object;
            Date date = data[0] != null ? (Date) data[0] : null;          //date
            Double inTime = data[1] != null ? (Double) data[1] : 0;     //int time
            if (date != null) {
                dailyInOutHours.put(date, inTime);
            }
        }
        inOutItem.setEmployeeId(int_employeeID);
        inOutItem.setDailyInOutHour(dailyInOutHours);

        return inOutItem;
    }

    private Map<Integer, Integer> getTimeSlotItemDays(EdsTimeSlot mainTimeSlot) {
        HashMap<Integer, Integer> param = new HashMap<>();
        if (mainTimeSlot != null && mainTimeSlot.getDeleted() != null && !mainTimeSlot.getDeleted()) {
            Set<EdsTimeSlotItem> items = mainTimeSlot.getItems();
            for (EdsTimeSlotItem slot : items) {
                param.put(slot.getDay(), Math.abs(slot.getEndTime() - slot.getStartTime()));
            }
            return param;
        } else {
            return null;
        }
    }

    private HashMap<Integer, Map<Integer, Integer>> getTimeSlotItemsMap() {
        HashMap<Integer, Map<Integer, Integer>> result = new HashMap<>();
        List<EdsTimeSlot> timeSlots = timeSlotManager.getTimeslots();
        for (EdsTimeSlot timeSlot : timeSlots) {
            result.put(timeSlot.getObjectID(), getTimeSlotItemDays(timeSlot));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmployeeAttendanceReport getEmployeeAttendanceReport(ListingFilterParameter filter, int daysInMonth) {
        long begin1 = System.currentTimeMillis();

        String startString = filter.getStartDateNC();
        String endString = filter.getEndDateNC();
        Date startDate = new Date(Integer.parseInt(startString.split("-")[0]) - 1900, Integer.parseInt(startString.split("-")[1]) - 1, Integer.parseInt(startString.split("-")[2]), 0, 0, 0);
        Date endDate = new Date(Integer.parseInt(endString.split("-")[0]) - 1900, Integer.parseInt(endString.split("-")[1]) - 1, Integer.parseInt(endString.split("-")[2]), 23, 59, 59);
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);

        //change the method to return holidays for specified start/end date period in comma separeted list format ie 1,5,7
        //OR Get location list and generate timeStart map which stores locations and holidays using HolidayManager.getCalendarHolidays

        List<EdsLocation> locations = locationManager.getLocations(new ListingFilterParameter());
        Map<EdsLocation, ArrayList<Holiday>> locHoliday = new HashMap<>();
        if (locations != null)
            for (EdsLocation loc : locations) {
                locHoliday.put(loc, availabilityCircularResolver.getHolidaysListByLocation(loc, startDate, endDate));
            }

        //List objects = sickRequestManager.getEmployeeAttendanceReport(filter);
        //Integer count = sickRequestManager.getEmployeeAttendanceReportCount(filter);
        ListResult<Object> employeeAttendanceReeport = sickRequestManager.getEmployeeAttendanceReport(filter);

        Map<Integer, Integer> comTimeSlot = availabilityCircularResolver.getCompanyTimeSlot();
        HashMap<Integer, Map<Integer, Integer>> tsiMap = getTimeSlotItemsMap();
        int totalMonthSum = 0, withLeaveSum = 0, withoutLevaeSum = 0, waitingApprovalSum = 0;
        int[] totalAbsent = new int[daysInMonth + 1];
        int[] withoutLR = new int[daysInMonth + 1];
        int[] withLR = new int[daysInMonth + 1];
        int[] waitingForAppRoval = new int[daysInMonth + 1];

        ArrayList<Holiday> companyHolidayDays = availabilityCircularResolver.getHolidaysListByLocation(null, startDate, endDate);

        HolidayIndicator[] monthlyHoliday2 = availabilityCircularResolver.getMonthlyHoliday(comTimeSlot, startDate, daysInMonth, companyHolidayDays, false);

        Map<Integer, HolidayIndicator[]> employeeMonthlyHoliday = new HashMap<>();
        HashMap<Integer, EdsEmployee> employeesMap = new HashMap<>();
        if (filter.isFromExcelPDF()) {
            String companyId = ServerSecurityContext.getInstance().getCompanyId();
            EdsCompany company = companyManager.getCompany(Integer.valueOf(companyId));

            List<EdsEmployee> employees = employeeManager.getEmployeesForPayroll(company);
            employees.forEach(t -> {
                HolidayIndicator[] monthlyHoliday = availabilityCircularResolver.getMonthlyHoliday(tsiMap.get(t.getTimeSlot().getObjectID()), startDate, daysInMonth, locHoliday.get(t.getLocation()), false);
                employeeMonthlyHoliday.put(t.getObjectID(), monthlyHoliday);
                employeesMap.put(t.getObjectID(), t);
            });
        }

        Set<Integer> employeeIds = new HashSet<>();
        for (Object object : employeeAttendanceReeport.getList()) {
            Object[] data = (Object[]) object;
            employeeIds.add((Integer) data[0]);
        }
        Map<Integer, BigDecimal> employeeSalaryMap = salaryHistoryManager.getEmployeeLastSalaryHistoryMap(new ArrayList<>(employeeIds), endDate);

        EmployeeAttendanceReport attendanceReport = new EmployeeAttendanceReport(filter.getDepartmentId(), filter.getLocationId(), filter.getDepartmentId() != null ? departmentManager.get(filter.getDepartmentId()).getName() : "");
        List<EdsLeaveReason> leaveTypes = leaveReasonManager.listActiveReasonsForAR();
        HashMap<String, ReasonItem> leaveMap = new HashMap<>();
        leaveTypes.add(leaveReasonManager.getReasonByName("", "LR_TYPE_HOLIDAY"));
        leaveTypes.add(leaveReasonManager.getReasonByName("", "LR_TYPE_DAY_OFF"));
        EdsLeaveReason lrTypeUnauthorizedLeave = leaveReasonManager.getReasonByName("", "LR_TYPE_UNAUTHORIZED_LEAVE");
        if (lrTypeUnauthorizedLeave != null) {
            leaveTypes.add(lrTypeUnauthorizedLeave);
        }

        if (leaveTypes != null && leaveTypes.size() > 0) {
            for (EdsLeaveReason leaveType : leaveTypes) {
                ReasonItem item = leaveType.toRPC();
                item.setName(commonLocalizer.localizeRef(leaveType));
                leaveMap.put(item.getCode(), item);
            }
        }
        String ABSENT_LEAVE = "ABSENT_LEAVE";
        ReasonItem ri = new ReasonItem();
        ri.setCode(ABSENT_LEAVE);
        ri.setName(commonLocalizer.localize("LR_TYPE_UNAUTHORIZED_LEAVE"));
        ri.setShortName(commonLocalizer.localize("AB", "AB"));
        ri.setHexColor("d32f2f");
        leaveMap.put(ABSENT_LEAVE, ri);
        Map<Integer, EmployeeReport> list = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        EmployeeReport employeeReport = null;
        Integer emplId = null;

        EdsReference approved = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);
        EdsReference denied = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.DENIED);

        for (Object object : employeeAttendanceReeport.getList()) {
            Object[] data = (Object[]) object;
            Integer id = (Integer) data[0];
            Date start = (Date) data[1];
            Date end = (Date) data[2];
            Integer statusid = (Integer) data[3];
            Integer position = (Integer) data[4];
            boolean unauthLeave = EdsSickRequest.LR_TYPE_UNAUTHORIZED_LEAVE.equals(data[5]);
            String code = (String) data[5];
            Boolean includingDayOffs = data[6] != null ? (Boolean) data[6] : false;
            Integer leaveRequestId = (Integer) data[8];
            Boolean markAsDraft = data[9] != null ? (Boolean) data[9] : false;
            Boolean hourly = data[10] != null && data[10].equals(UnitType.HOURLY.name());
            Boolean hasShift = data[11] != null ? (Boolean) data[11] : false;
            Integer deptId = data[12] != null ? (Integer) data[12] : null;
            int[] absentleave = new int[daysInMonth + 1];
            Integer[] hourlyLRs = new Integer[daysInMonth + 1];
            String[] leaveCodes = new String[daysInMonth + 1];

            int totalmonth = 0, waitingCount = 0, withLRCount = 0, withoutLRCount = 0, beginDay = 0, endDay = 0;

            if (emplId != null && emplId.equals(id)) {
                absentleave = employeeReport.getAl();
                hourlyLRs = employeeReport.getHourlyLRs();
                leaveCodes = employeeReport.getLeaveCodes();
            }

            EdsEmployee edsEmployee;
            if (filter.isFromExcelPDF()) {
                edsEmployee = employeesMap.get(id);
            } else {
                edsEmployee = employeeManager.get(id);
            }
            ArrayList<Holiday> employeeHolidayDays = null;
            if (edsEmployee.getLocation() != null) {
                employeeHolidayDays = locHoliday.get(edsEmployee.getLocation());
            }
            Map<Integer, Integer> tSlot = tsiMap.get(edsEmployee.getTimeSlot().getObjectID());
            EdsSickRequest request = null;
            if (leaveRequestId != null) {
                request = sickRequestManager.get(leaveRequestId);
            }
            ArrayList<Integer> additionalLeaveDays = request != null
                    && request.getOverallStatus() != null && !"DRAFT".equals(request.getOverallStatus().getCode()) && request.getLeaveReason() != null && request.getLeaveReason().getExceptionalTimeslot()
                    && edsEmployee.getTimeSlot() != null && edsEmployee.getTimeSlot().getAdditionalLeaveDays() != null ? edsEmployee.getTimeSlot().getAdditionalLeaveDays() : new ArrayList<>();

            HolidayIndicator[] monthlyHoliday;
            if (filter.isFromExcelPDF()) {
                monthlyHoliday = employeeMonthlyHoliday.get(id);
            } else {
                monthlyHoliday = availabilityCircularResolver.getMonthlyHoliday(tSlot, startDate, daysInMonth, employeeHolidayDays, false);

            }
            HashMap<String, Integer> monthHolidaysByPeriod = new HashMap<>();
            if (filter.getFromTerminal() != null && filter.getFromTerminal()) {
                monthHolidaysByPeriod = availabilityCircularResolver.getMonthHolidaysByPeriod(comTimeSlot, startDate, endDate, daysInMonth, employeeHolidayDays);
            }
            HolidayIndicator[] monthlyHolidayWithOne = new HolidayIndicator[monthlyHoliday.length];
            for (int i = 0; i < monthlyHoliday.length; i++) {
                if (monthlyHoliday[i] == null) {
                    monthlyHoliday[i] = new HolidayIndicator(null, 0);
                }
                if (monthlyHolidayWithOne[i] == null) {
                    monthlyHolidayWithOne[i] = new HolidayIndicator(null, 0);
                }
                if (monthlyHoliday[i].getIndicator() == 3) {
                    monthlyHolidayWithOne[i].setIndicator(1);
                    monthlyHolidayWithOne[i].setHoliday(monthlyHoliday[i].getHoliday());
                }
            }
            HolidayIndicator[] withHoliday = Arrays.copyOf(monthlyHolidayWithOne, monthlyHolidayWithOne.length);
            ArrayList<Holiday> emplHolidayDays = locHoliday.get(edsEmployee.getLocation());

            LinkedHashMap<Integer, Integer> idsOfLeaveRequests = new LinkedHashMap<>();
            if (start != null && end != null && !markAsDraft) {
                int startDay = start.compareTo(startDate) >= 0 ? start.getDate() : startDate.getDate();
                int enddDay = end.compareTo(endDate) >= 0 ? endDate.getDate() : end.getDate();
                for (int i = 1; i <= daysInMonth; i++) {
                    if (i >= startDay && i <= enddDay) {
                        idsOfLeaveRequests.put(i, leaveRequestId);
                    }
                }
            }
            if (emplId == null || !emplId.equals(id)) {
                emplId = id;
                String employeePosition = edsEmployee.getPosition() != null && edsEmployee.getPosition().getName() != null ? edsEmployee.getPosition().getName() : "";
                String employeePositionType = edsEmployee.getPosition() != null && edsEmployee.getPosition().getType() != null ? edsEmployee.getPosition().getType().getCode() : "";
                String employeePositionUzbek = edsEmployee.getPosition() != null && edsEmployee.getPosition().getLocale() != null && edsEmployee.getPosition().getLocale().getUzbek() != null ? edsEmployee.getPosition().getLocale().getUzbek() : "";
                String employeeDepartmentOrPosition = null;
                if (filter.isOrderByDepartment()) {
                    EdsDepartment department = deptId != null ? departmentManager.get(deptId) : edsEmployee.getEmployeeDepartment() != null && edsEmployee.getEmployeeDepartment().getTeam() != null ? edsEmployee.getEmployeeDepartment().getTeam() : null;
                    employeeDepartmentOrPosition = department != null ? department.getName() : "n/a";
                } else if (filter.isOrderByPosition()) {
                    employeeDepartmentOrPosition = edsEmployee.getPosition() != null && edsEmployee.getPosition().getName() != null ? edsEmployee.getPosition().getName() : "n/a";
                }
                employeeReport = new EmployeeReport(id, edsEmployee.getName(), (daysInMonth + 1), idsOfLeaveRequests, edsEmployee.getProfile().getEmployeeCode(), employeePosition, employeePositionType, employeePositionUzbek, employeeDepartmentOrPosition, hasShift);
                calculateDailyWorkMinutes(employeeReport, edsEmployee, startDate, daysInMonth);
                employeeReport.setSalary(employeeSalaryMap.get(id));
                employeeReport.setDepartmentId(deptId != null ? deptId : edsEmployee.getTeam() != null ? edsEmployee.getTeam().getObjectID() : null);
                list.put(id, employeeReport);

                if (edsEmployee.getPhoto() != null) {
                    employeeReport.setPhotoUrl(commonService.getImageUrl(edsEmployee.getPhoto().getObjectID()));
                } else {
                    employeeReport.setPhotoUrl("");
                }

                if (edsEmployee.getEndDate() != null) {
                    Date resignationDate = edsEmployee.getEndDate();
                    employeeReport.setResignationDay(resignationDate);
                }

            } else if (list.get(id) != null) {
                EmployeeReport existingEmployeeReport = list.get(id);

                if (edsEmployee.getEndDate() != null) {
                    Date resignationDate = edsEmployee.getEndDate();
                    existingEmployeeReport.setResignationDay(resignationDate);
                }

                if (start != null && end != null) {
                    LinkedHashMap<Integer, Integer> existingIdsOfLeaveRequests = existingEmployeeReport.getIdsOfLeaveRequests();
                    int startDay = start.compareTo(startDate) >= 0 ? start.getDate() : startDate.getDate();
                    int enddDay = end.compareTo(endDate) >= 0 ? endDate.getDate() : end.getDate();
                    for (int i = 1; i <= daysInMonth; i++) {
                        if (i >= startDay && i <= enddDay) {
                            existingIdsOfLeaveRequests.put(i, leaveRequestId);
                        }
                    }
                    existingEmployeeReport.setIdsOfLeaveRequests(existingIdsOfLeaveRequests);
                }
                list.put(emplId, existingEmployeeReport);
            }

            if (startDate != null && start != null && startDate.compareTo(start) > 0) {
                start = startDate;
            }
            if (start != null && end != null) {
                //beginmethod
                List<EdsTimeSlotItem> employeeTimeSlotItems = getTimeSlotItems(edsEmployee);

                Calendar startDateCalendar = new GregorianCalendar();
                startDateCalendar.setTime(start);
                Calendar endDateCalendar = new GregorianCalendar();
                endDateCalendar.setTime(end);
                int index = 2; //2 = Tuesday, index (1 = monday,...6 = saturday, 0 = sunday)

                if (employeeTimeSlotItems.size() > index) {
                    int defStartDay = employeeTimeSlotItems.get(index).getStartTime();
                    int defEndDay = employeeTimeSlotItems.get(index).getEndTime();
                    if (defStartDay > 0 && defEndDay > 0) {
                        if (startDateCalendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE) > defEndDay) {
                            startDateCalendar.add(Calendar.DAY_OF_YEAR, 1);

                            int minute = employeeTimeSlotItems.get(index).getStartTime() % 60;
                            int hour = (employeeTimeSlotItems.get(index).getStartTime() - employeeTimeSlotItems.get(index).getStartTime() % 60) / 60;

                            startDateCalendar.set(Calendar.HOUR_OF_DAY, hour);
                            startDateCalendar.set(Calendar.MINUTE, minute);
                            start.setTime(startDateCalendar.getTimeInMillis());
                        }

//                        if (endDateCalendar.get(Calendar.HOUR_OF_DAY) * 60 + endDateCalendar.get(Calendar.MINUTE) < defStartDay) {
//                            endDateCalendar.add(Calendar.DAY_OF_YEAR, -1);
//
//                            int minute = employeeTimeSlotItems.get(index).getEndTime() % 60;
//                            int hour = (employeeTimeSlotItems.get(index).getEndTime() - employeeTimeSlotItems.get(index).getEndTime() % 60) / 60;
//
//                            endDateCalendar.set(Calendar.HOUR_OF_DAY, hour);
//                            endDateCalendar.set(Calendar.MINUTE, minute);
//                            end.setTime(endDateCalendar.getTimeInMillis());
//                        }
                    }
                }
                //endmethod

                if (position != null && position == 1) {
                    beginDay = start.getDate();
                    if (end.after(endDate)) {
                        endDay = daysInMonth;
                    } else {
                        endDay = end.getDate();
                    }
                    if (beginDay > endDay) {
                        endDay = beginDay;
                    }

                }
                if (position != null && position == -1) {
                    beginDay = 1;
                    endDay = end.getDate();
                    if (beginDay > endDay) {
                        endDay = beginDay;
                    }
                }

            }
            calendar.setTime(startDate);
            boolean approvedStatus = approved.getObjectID().equals(statusid);
            boolean deniedStatus = denied.getObjectID().equals(statusid);
//            boolean notDefinedStatus = (statusid == null) ? false : notDefined.getObjectID().equals(statusid);
            if (emplHolidayDays != null)
                for (Holiday emplHolidayDay : emplHolidayDays) {
                    withHoliday[emplHolidayDay.getCalendar().get(Calendar.DAY_OF_MONTH)].setIndicator(1);
                    withHoliday[emplHolidayDay.getCalendar().get(Calendar.DAY_OF_MONTH)].setHoliday(emplHolidayDay);
                }
            HolidayIndicator[] leaveRequestHoliday = Arrays.copyOf(monthlyHoliday, monthlyHoliday.length);
            HashMap<Date, EdsTimeSlotItem> exceptionalTimeSlotItem = edsEmployee.getTimeSlot().getExceptionalTimeSlotItem();
            Map<Date, Integer> exceptionalDates = new HashMap<>();
            exceptionalTimeSlotItem.forEach((key, value) -> exceptionalDates.put(key, value.getMinutes()));
            Map<Integer, Integer[]> timeSlotItemMap = getTimeslotMinutes(edsEmployee, false, null);
            if (endDay > 0 || (emplHolidayDays != null && emplHolidayDays.size() > 0))
                for (int day = 1; day <= daysInMonth; day++) {
                    int current = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    if (filter.getFromTerminal() != null && filter.getFromTerminal()) {
                        day = calendar.getTime().getDate();
                    }
                    HolidayIndicator dayOfHoliday = withHoliday[day];
                    boolean isHoliday = dayOfHoliday != null && dayOfHoliday.getIndicator() == 1;
                    boolean isTakenFromAllowance = dayOfHoliday != null && dayOfHoliday.getHoliday() != null && dayOfHoliday.getHoliday().isTakenFromAllowance();
                    if (beginDay <= day && day <= endDay && statusid != null && !markAsDraft) {
                        if (tSlot != null && tSlot.containsKey(current) && (hasShift || tSlot.get(current) != 0 || includingDayOffs || (exceptionalTimeSlotItem.containsKey(calendar.getTime()) && exceptionalTimeSlotItem.get(calendar.getTime()).getMinutes() > 0) || (additionalLeaveDays.size() > 0 && additionalLeaveDays.contains(current)))) {
                            if (includingDayOffs) {
                                leaveRequestHoliday[day].setIndicator(0);
                            }
                            if (approvedStatus) {
                                if (absentleave[day] != 0 || includingDayOffs) {
                                    if (absentleave[day] == -1) {
                                        waitingForAppRoval[day]--;
                                        waitingCount--;
                                    } else if (absentleave[day] == -2 || absentleave[day] == 2) {
                                        withoutLR[day]--;
                                        withoutLRCount--;
                                    } else {
                                        if (absentleave[day] == 1) {
                                            withLR[day]--;
                                            withLRCount--;
                                        }
                                    }
                                    if (!(absentleave[day] != 0 || includingDayOffs)) {
                                        totalmonth--;
                                        totalAbsent[day]--;
                                    }
                                }
                                if (hasShift && tSlot.get(current) == 0 && (additionalLeaveDays.size() == 0 || !additionalLeaveDays.contains(current))) {
                                    absentleave[day] = 5;
                                } else if (hourly) {
                                    absentleave[day] = 3;
                                    if (DateUtils.areOnTheSameDay(start, end)) {
                                        Calendar startC = new GregorianCalendar();
                                        Calendar endC = new GregorianCalendar();
                                        startC.setTime(start);
                                        endC.setTime(end);
                                        boolean isExceptionalDate = false;
                                        int exceptionalStartDay = 0;
                                        int exceptionalEndDay = 0;
                                        int exceptionalLunchStartDay = 0;
                                        int exceptionalLunchEndDay = 0;
                                        int exceptionalCoffeeStartDay = 0;
                                        int exceptionalCoffeeEndDay = 0;

                                        if (exceptionalTimeSlotItem.containsKey(calendar.getTime())) {
                                            isExceptionalDate = true;
                                            exceptionalStartDay = exceptionalTimeSlotItem.get(calendar.getTime()).getStartTime();
                                            exceptionalEndDay = exceptionalTimeSlotItem.get(calendar.getTime()).getEndTime();
                                            exceptionalLunchStartDay = exceptionalTimeSlotItem.get(calendar.getTime()).getLunchStart();
                                            exceptionalLunchEndDay = exceptionalTimeSlotItem.get(calendar.getTime()).getLunchEnd();
                                            exceptionalCoffeeStartDay = exceptionalTimeSlotItem.get(calendar.getTime()).getCoffeeStart();
                                            exceptionalCoffeeEndDay = exceptionalTimeSlotItem.get(calendar.getTime()).getCoffeeEnd();
                                        }
                                        int dayOfWeek = startC.get(Calendar.DAY_OF_WEEK) - 1;
                                        int timeSlotStartTime = isExceptionalDate ? exceptionalStartDay : timeSlotItemMap.get(dayOfWeek)[timeslotStart];
                                        int timeSlotEndTime = isExceptionalDate ? exceptionalEndDay : timeSlotItemMap.get(dayOfWeek)[timeslotEnd];
                                        int lunchStartTime = isExceptionalDate ? exceptionalLunchStartDay : timeSlotItemMap.get(dayOfWeek)[lunchStart];
                                        int lunchEndTime = isExceptionalDate ? exceptionalLunchEndDay : timeSlotItemMap.get(dayOfWeek)[lunchEnd];
                                        int coffeeStartTime = isExceptionalDate ? exceptionalCoffeeStartDay : timeSlotItemMap.get(dayOfWeek)[coffeeStart];
                                        int coffeeEndTime = isExceptionalDate ? exceptionalCoffeeEndDay : timeSlotItemMap.get(dayOfWeek)[coffeeEnd];

                                        Calendar timeSlotStart = (Calendar) startC.clone();
                                        timeSlotStart.set(Calendar.HOUR_OF_DAY, timeSlotStartTime / 60);
                                        timeSlotStart.set(Calendar.MINUTE, timeSlotStartTime % 60);

                                        Calendar timeSlotEnd = (Calendar) startC.clone();
                                        timeSlotEnd.set(Calendar.HOUR_OF_DAY, timeSlotEndTime / 60);
                                        timeSlotEnd.set(Calendar.MINUTE, timeSlotEndTime % 60);

                                        Calendar lunchStart = (Calendar) startC.clone();
                                        lunchStart.set(Calendar.HOUR_OF_DAY, lunchStartTime / 60);
                                        lunchStart.set(Calendar.MINUTE, lunchStartTime % 60);
                                        Calendar lunchEnd = (Calendar) startC.clone();
                                        lunchEnd.set(Calendar.HOUR_OF_DAY, lunchEndTime / 60);
                                        lunchEnd.set(Calendar.MINUTE, lunchEndTime % 60);

                                        Calendar coffeeStart = (Calendar) startC.clone();
                                        coffeeStart.set(Calendar.HOUR_OF_DAY, coffeeStartTime / 60);
                                        coffeeStart.set(Calendar.MINUTE, coffeeStartTime % 60);
                                        Calendar coffeeEnd = (Calendar) startC.clone();
                                        coffeeEnd.set(Calendar.HOUR_OF_DAY, coffeeEndTime / 60);
                                        coffeeEnd.set(Calendar.MINUTE, coffeeEndTime % 60);

                                        int minutes = getLeaveMinutes(startC, timeSlotStart, timeSlotEnd, lunchStart, lunchEnd, coffeeStart, coffeeEnd, endC);
                                        hourlyLRs[day] = -1 * minutes;
                                    } else {
                                        hourlyLRs[day] = leaveRequestId;
                                    }
                                } else {
                                    absentleave[day] = unauthLeave ? 2 : 1;
                                }
                                leaveCodes[day] = code;
                                withLR[day]++;
                                withLRCount++;
                                if (withHoliday[day] != null && withHoliday[day].getIndicator() == 1) {
                                    withHoliday[day].setIndicator(2);
                                }

                            } else {
                                if (deniedStatus) {
                                    totalmonth--;
                                    totalAbsent[day]--;
                                } else {
                                    if (absentleave[day] != 0) {
                                        if (absentleave[day] == -1) {
                                            waitingForAppRoval[day]--;
                                            waitingCount--;
                                        } else if (absentleave[day] == -2 || absentleave[day] == 2) {
                                            withoutLR[day]--;
                                            withoutLRCount--;
                                        } else {
                                            if (absentleave[day] == 1) {
                                                withLR[day]--;
                                                withLRCount--;
                                            }
                                        }
                                        totalmonth--;
                                        totalAbsent[day]--;
                                    }
                                    if (!unauthLeave) {
//                                        absentleave[day] = -1;
//                                        leaveCodes[day] = code;
                                        waitingForAppRoval[day]++;
                                        waitingCount++;
                                    } else {
//                                        absentleave[day] = -2;
//                                        leaveCodes[day] = ABSENT_LEAVE; //UnAuthorized leave
                                        withoutLR[day]++;
                                        withoutLRCount++;
                                    }

                                }
                            }
                            totalmonth++;
                            totalAbsent[day]++;
                        } else {
                            if (tSlot == null && current != 0) {
                                if (approvedStatus) {
                                    if (absentleave[day] != 0) {
                                        if (absentleave[day] == -1) {
                                            waitingForAppRoval[day]--;
                                            waitingCount--;
                                        } else if (absentleave[day] == -2 || absentleave[day] == 2) {
                                            withoutLR[day]--;
                                            withoutLRCount--;
                                        } else {
                                            if (absentleave[day] == 1) {
                                                withLR[day]--;
                                                withLRCount--;
                                            }
                                        }
                                        totalmonth--;
                                        totalAbsent[day]--;
                                    }
                                    if (hourly) {
                                        absentleave[day] = 3;
                                        hourlyLRs[day] = DateUtils.areOnTheSameDay(start, end) ? -1 : leaveRequestId;
                                    } else {
                                        absentleave[day] = unauthLeave ? 2 : 1;
                                    }
                                    leaveCodes[day] = code;
                                    withLR[day]++;
                                    withLRCount++;
                                } else {
                                    if (deniedStatus) {
                                        totalmonth--;
                                        totalAbsent[day]--;
                                    } else {
                                        if (absentleave[day] != 0) {
                                            if (absentleave[day] == -1) {
                                                waitingForAppRoval[day]--;
                                                waitingCount--;
                                            } else if (absentleave[day] == -2 || absentleave[day] == 2) {
                                                withoutLR[day]--;
                                                withoutLRCount--;
                                            } else {
                                                if (absentleave[day] == 1) {
                                                    withLR[day]--;
                                                    withLRCount--;
                                                }
                                            }
                                            totalmonth--;
                                            totalAbsent[day]--;
                                        }
                                        if (!unauthLeave) {
//                                            absentleave[day] = -1;
//                                            leaveCodes[day] = code;
                                            waitingForAppRoval[day]++;
                                            waitingCount++;
                                        } else {
//                                            absentleave[day] = -2;
//                                            leaveCodes[day] = "ABSENT_LEAVE"; //UnAuthorized leave
                                            withoutLR[day]++;
                                            withoutLRCount++;
                                        }
                                    }
                                }
                                totalmonth++;
                                totalAbsent[day]++;
                            }
                        }

                        if (isHoliday) {
                            if (approvedStatus) {
                                if (absentleave[day] != 0) {
                                    if (absentleave[day] == -1) {
                                        waitingForAppRoval[day]--;
                                        waitingCount--;
                                    } else if (absentleave[day] == -2 || absentleave[day] == 2) {
                                        withoutLR[day]--;
                                        withoutLRCount--;
                                    } else {
                                        if (absentleave[day] == 1) {
                                            withLR[day]--;
                                            withLRCount--;
                                        }
                                    }
                                    totalmonth--;
                                    totalAbsent[day]--;
//                                    withHoliday[day].setIndicator(2);
                                }
                                if (!isTakenFromAllowance) {
                                    absentleave[day] = -3;
                                }
                            } else {
                                if (!deniedStatus) {
                                    if (absentleave[day] != 0) {
                                        if (absentleave[day] == -1) {
                                            waitingForAppRoval[day]--;
                                            waitingCount--;
                                        } else if (absentleave[day] == -2 || absentleave[day] == 2) {
                                            withoutLR[day]--;
                                            withoutLRCount--;
                                        } else {
                                            if (absentleave[day] == 1) {
                                                withLR[day]--;
                                                withLRCount--;
                                            }
                                        }
                                        totalmonth--;
                                        totalAbsent[day]--;
                                    }
                                    absentleave[day] = 0;
                                }
                            }
                        }
                    }

                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            employeeReport.setLeaveCodes(leaveCodes);
            employeeReport.setAl(absentleave);
            employeeReport.setHourlyLRs(hourlyLRs);
            employeeReport.setWithHoliday(ServerUtils.extractArrayFromHolidayIndicator(withHoliday));
            employeeReport.setWithLR(employeeReport.getWithLR() + withLRCount);
            employeeReport.setWithoutLR(employeeReport.getWithoutLR() + withoutLRCount);
            employeeReport.setWaitingForApproval(employeeReport.getWaitingForApproval() + waitingCount);
            employeeReport.setTotalmonth(employeeReport.getTotalmonth() + totalmonth);
            employeeReport.setLeaveRequestHolidays(ServerUtils.extractArrayFromHolidayIndicator(leaveRequestHoliday));
            employeeReport.setMonthHolidaysByPeriod(monthHolidaysByPeriod);
            employeeReport.setExceptionalTimeSlotDates(exceptionalDates);
        }

        for (int j = 1; j <= daysInMonth; j++) {
            withLeaveSum += withLR[j];
            waitingApprovalSum += waitingForAppRoval[j];
            withoutLevaeSum += withoutLR[j];
            totalMonthSum += totalAbsent[j];
        }
        List<EdsLeaveReason> dayOffReasons = leaveReasonManager.getLRHolidayIncludedReasons();
        attendanceReport.setHolidayIncluded(dayOffReasons.size() > 0);
        attendanceReport.setWithLR(withLR);
        attendanceReport.setWithoutLR(withoutLR);
        attendanceReport.setWaitingForAppRoval(waitingForAppRoval);
        attendanceReport.setTotalAbsent(totalAbsent);
        attendanceReport.setWithLRSum(withLeaveSum);
        attendanceReport.setWithoutLRSum(withoutLevaeSum);
        attendanceReport.setWaitingForApprovalSum(waitingApprovalSum);
        attendanceReport.setTotalAbsentSum(totalMonthSum);
        attendanceReport.setMonthHoliday(ServerUtils.extractArrayFromHolidayIndicator(monthlyHoliday2));
        attendanceReport.setTotalCount(employeeAttendanceReeport.getTotal());

        //Leave request reasons
        attendanceReport.setReasons(leaveReasonManager.getAttendanceLRReasons(true));

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSickRequest.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        ServerUtils.kpiLog(LOGGER, kpiLog, "Get employee attendance report");
        Map<Integer, EmployeeReport> sortedMap = list.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(EmployeeReport::getName)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        EmployeeAttendanceReport res = getEmployeeInHours(attendanceReport, sortedMap, daysInMonth, startDate, endDate, leaveMap, filter.getDepartmentIds(), Boolean.TRUE.equals(filter.getFromTerminal()));
        res.setLeaveTypes(leaveMap);
        LOGGER.info("Load time  " + (System.currentTimeMillis() - begin1));
        return res;
    }

    private void calculateDailyWorkMinutes(EmployeeReport employeeReport, EdsEmployee employee, Date startDate, int daysInMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        int[] timeslotMins = new int[daysInMonth + 1];
        int[] timeslotStartMins = new int[daysInMonth + 1];
        int[] timeslotEndMins = new int[daysInMonth + 1];
        int[] timeslotLunchMins = new int[daysInMonth + 1];

        if (employee == null || employee.getTimeSlot() == null) {
            employeeReport.setTimeslotOverallMins(timeslotMins);
            employeeReport.setTimeslotStartMins(timeslotStartMins);
            employeeReport.setTimeslotEndMins(timeslotEndMins);
            employeeReport.setTimeslotLunchMins(timeslotLunchMins);
            return;
        }

        Map<Integer, Integer[]> timeSlotItemMap = getTimeslotMinutes(employee, false, null);
        Map<Date, EdsTimeSlotItem> exceptionalTimeSlotItems = employee.getTimeSlot().getExceptionalTimeSlotItem();

        for (int day = 1; day <= daysInMonth; day++) {
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            int startMin = 0;
            int endMin = 0;
            int lunchStartMin = 0;
            int lunchEndMin = 0;
            int coffeeStartMin = 0;
            int coffeeEndMin = 0;


            Integer[] slot = timeSlotItemMap.get(dayOfWeek);

            if (slot != null) {
                startMin = slot[timeslotStart] != null ? slot[timeslotStart] : 0;
                endMin = slot[timeslotEnd] != null ? slot[timeslotEnd] : 0;
                lunchStartMin = slot[lunchStart] != null ? slot[lunchStart] : 0;
                lunchEndMin = slot[lunchEnd] != null ? slot[lunchEnd] : 0;
                coffeeStartMin = slot[coffeeStart] != null ? slot[coffeeStart] : 0;
                coffeeEndMin = slot[coffeeEnd] != null ? slot[coffeeEnd] : 0;
            }


            if (exceptionalTimeSlotItems != null) {
                EdsTimeSlotItem exceptionalItem = exceptionalTimeSlotItems.get(calendar.getTime());
                if (exceptionalItem != null) {
                    startMin = exceptionalItem.getStartTime();
                    endMin = exceptionalItem.getEndTime();
                    lunchStartMin = exceptionalItem.getLunchStart();
                    lunchEndMin = exceptionalItem.getLunchEnd();
                    coffeeStartMin = exceptionalItem.getCoffeeStart();
                    coffeeEndMin = exceptionalItem.getCoffeeEnd();
                }
            }


            int lunchMinutes = 0;
            if (lunchEndMin > lunchStartMin) {
                lunchMinutes = lunchEndMin - lunchStartMin;
            }

            int coffeeMinutes = 0;
            if (coffeeEndMin > coffeeStartMin) {
                coffeeMinutes = coffeeEndMin - coffeeStartMin;
            }

            int workMinutes = 0;
            if (startMin != endMin) {
                workMinutes = endMin - startMin;
                workMinutes -= lunchMinutes;
                workMinutes -= coffeeMinutes;
            }

            timeslotStartMins[day] = startMin;
            timeslotEndMins[day] = endMin;
            timeslotLunchMins[day] = lunchMinutes;
            timeslotMins[day] = Math.max(workMinutes, 0);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }


        employeeReport.setTimeslotOverallMins(timeslotMins);
        employeeReport.setTimeslotStartMins(timeslotStartMins);
        employeeReport.setTimeslotEndMins(timeslotEndMins);
        employeeReport.setTimeslotLunchMins(timeslotLunchMins);
    }

    public EmployeeAttendanceReport getEmployeeInHours(EmployeeAttendanceReport attendanceReport, Map<Integer, EmployeeReport> map, int daysInMonth, Date startDate, Date endDate, Map<String, ReasonItem> reasons, String departmentIds, boolean isFromTerminal) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dashboardService.lastEnteredDate();
        boolean withLunchTime = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ATTENDANCE_LUNCH_AND_COFFEE_TIME);

        List<Object[]> timeTracks = timeSlotManager.getInUotReportCustom(attendanceReport.getId(), attendanceReport.getLocationId(), null, null, startDate, endDate, withLunchTime, map.keySet().stream().map(Object::toString).collect(Collectors.joining(",")), departmentIds);

        // ============ 1. DUPLICATE FIX: datejoin duplicate larni olib tashlash ============
        Map<String, Object[]> uniqueTimeTracks = new LinkedHashMap<>();
        if (timeTracks != null) {
            for (Object timeTrack : timeTracks) {
                Object[] objects = (Object[]) timeTrack;
                Integer employeeId = (Integer) objects[2];
                String dateStr = (String) objects[3];

                if (employeeId != null && dateStr != null) {
                    String key = employeeId + "|" + dateStr;
                    if (!uniqueTimeTracks.containsKey(key)) {
                        uniqueTimeTracks.put(key, objects);
                    }
                }
            }
        }
        // ================================================================================

        Map<Integer, Map<Integer, FingerprintTimeDto>> fingerprintData = timeSlotManager.getFingerprintData(attendanceReport.getId(), map.keySet().stream().map(Object::toString).collect(Collectors.joining(",")), null, startDate, endDate);
        int totalOvertime = 0;
        int totalInHours = 0;
        Date currentDate = new Date();
        currentDate.setHours(0);
        currentDate.setMinutes(0);
        currentDate.setSeconds(0);

        if (!uniqueTimeTracks.isEmpty()) {
            HashMap<Integer, List<Date>> dailyWorkLRDays = new HashMap<>();

            for (Object[] objects : uniqueTimeTracks.values()) {
                Integer id = (Integer) objects[2];
                Date date = null;
                try {
                    date = objects[3] != null ? format.parse((String) objects[3]) : null;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Double time = objects[6] != null ? Double.valueOf((Integer) objects[6]) : null;
                Double timeslot = objects[13] != null ? Double.valueOf((Integer) objects[13]) : null;
                String timeslotName = objects[14] != null ? (String) objects[14] : null;
                Double overtime = objects[16] != null ? objects[16] instanceof Double ? (Double) objects[16] : ((BigDecimal) objects[16]).doubleValue() : null;
                AttendanceHoursType type = objects[17] != null ? AttendanceHoursType.valueOf((String) objects[17]) : AttendanceHoursType.MANUAL_OR_SHIFT;
                Integer shift_start = objects[18] != null ? (Integer) objects[18] : null;
                Integer shift_end = objects[19] != null ? (Integer) objects[19] : null;
                String shift_color = objects[20] != null ? (String) objects[20] : null;
                if (time == 0 && map.get(id) != null && !map.get(id).isHasShift()) {
                    time = timeslot;
                }
                if (overtime != null && overtime > 0 && map.get(id) != null) {
                    if (map.get(id) != null && map.get(id).isHasShift()) {
                        time = overtime;
                    } else {
                        time = time != null ? time + overtime : overtime;
                    }
                    if (map.get(id) != null && map.get(id).getOvertimeHour() == null) {
                        map.get(id).setOvertimeHour(new int[daysInMonth]);
                    }
                    map.get(id).getOvertimeHour()[date.getDate()] = overtime.intValue();
                }

                if (id != null && map.containsKey(id)) {
                    if (time != null && date != null) {
                        if (daysInMonth >= date.getDate()) {
                            boolean hasShift = map.get(id).isHasShift();
                            if (hasShift) {
                                if (time > 0 && !type.equals(AttendanceHoursType.DUTY) && !type.equals(AttendanceHoursType.OVERTIME)) {
                                    map.get(id).setPlannedHours(map.get(id).getPlannedHours() + time.longValue());
                                    if (map.get(id).getPlannedDaysSet() == null) {
                                        map.get(id).setPlannedDaysSet(new HashSet<>());
                                    }
                                    map.get(id).getPlannedDaysSet().add(date.getDate());
                                }
                            } else if (timeslot > 0) {
                                map.get(id).setPlannedDays(map.get(id).getPlannedDays() + 1);
                            }
                            Integer lrId = map.get(id).getHourlyLRs()[date.getDate()];
                            if (lrId != null) {
                                if (lrId <= 0) {
                                    time = (double) -1 * lrId;
                                } else {
                                    EdsSickRequestDuration duration = sickRequestDurationManager.getSickRequestDurationT(date, lrId, null, Constants.DAY);
                                    time = duration.getDurationTime().doubleValue();
                                }
                            }
                            EmployeeReport report = map.get(id);
                            int al = report.getAl()[date.getDate()];
                            boolean isLeave = al == 1 || al == -1 || al == 2 || al == -2 || al == 5;
                            int intTime = time.intValue();
                            String existingTimeslot = map.get(id).getTimeSlotId()[date.getDate()] != null ? map.get(id).getTimeSlotId()[date.getDate()] + "," : "";
                            if (map.get(id).getInOutHour()[date.getDate()] != null) {
                                intTime = map.get(id).getInOutHour()[date.getDate()] + time.intValue();
                            }
                            if (!(al == 5)) {
                                map.get(id).getInOutHour()[date.getDate()] = intTime;
                            }
                            if (timeslotName != null) {
                                map.get(id).getTimeSlotId()[date.getDate()] = existingTimeslot + timeslotName;
                            }
                            if (shift_start != null) {
                                if (map.get(id).getShiftStartTime() == null) {
                                    map.get(id).setShiftStartTime(new int[daysInMonth + 1]);
                                }
                                map.get(id).getShiftStartTime()[date.getDate()] = shift_start;
                            }
                            if (shift_end != null) {
                                if (map.get(id).getShiftEndTime() == null) {
                                    map.get(id).setShiftEndTime(new int[daysInMonth + 1]);
                                }
                                map.get(id).getShiftEndTime()[date.getDate()] = shift_end;
                            }
                            if (shift_color != null) {
                                if (map.get(id).getShiftColor() == null) {
                                    map.get(id).setShiftColor(new String[daysInMonth + 1]);
                                }
                                map.get(id).getShiftColor()[date.getDate()] = shift_color;
                            }
                            date.setHours(0);
                            date.setMinutes(0);
                            date.setSeconds(0);
                            if (isLeave && !(al == 5)) {
                                UnitType unitType = reasons.get(report.getLeaveCodes()[date.getDate()]) != null ? reasons.get(report.getLeaveCodes()[date.getDate()]).getUnitType() : null;
                                boolean isMarkAsDraft = reasons.get(report.getLeaveCodes()[date.getDate()]) != null ? reasons.get(report.getLeaveCodes()[date.getDate()]).isMarkAsDraft() : false;
                                isLeave = !isMarkAsDraft && (unitType == null || !unitType.equals(UnitType.DAILY_WORK));
                                if (unitType != null && unitType.equals(UnitType.DAILY_WORK)) {
                                    time = timeslot;
                                    dailyWorkLRDays.putIfAbsent(id, new ArrayList<>());
                                    if (dailyWorkLRDays.get(id).contains(date)) {
                                        time = 0d;
                                    } else {
                                        dailyWorkLRDays.get(id).add(date);
                                    }
                                }
                            }
                            if ((date.before(currentDate) || date.equals(currentDate)) && !isLeave) {
                                map.get(id).setInhour(map.get(id).getInhour() + (al == 3 ? (timeslot.intValue() - time.intValue()) : time.intValue()));
                                if (time > 0) {
                                    if (overtime != null) {
                                        map.get(id).setOvertimeHours(map.get(id).getOvertimeHours() + overtime);
                                    }
                                    if (hasShift) {
                                        if (type.equals(AttendanceHoursType.DUTY)) {
                                            map.get(id).setDayOffHours(map.get(id).getDayOffHours() + time.intValue());
                                            map.get(id).setOvertimeDays(map.get(id).getOvertimeDays() + 1);
                                        } else {
                                            if (map.get(id).getWorkedDaysSet() == null) {
                                                map.get(id).setWorkedDaysSet(new HashSet<>());
                                            }
                                            map.get(id).getWorkedDaysSet().add(date.getDate());
                                        }
                                    } else {
                                        if (time - (overtime != null ? overtime : 0d) > timeslot) {
                                            map.get(id).setOvertimeDays(map.get(id).getOvertimeDays() + 1);
                                            int dayOfMonth = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
                                            if (report.getLeaveRequestHolidays()[dayOfMonth] == 1
                                                    || report.getLeaveRequestHolidays()[dayOfMonth] == 3) {
                                                map.get(id).setDayOffHours(map.get(id).getDayOffHours() + time - timeslot);
                                            } else {
                                                map.get(id).setOvertimeHours(map.get(id).getOvertimeHours() + time - timeslot);
                                            }
                                        } else {
                                            if (al != 3 || time.intValue() < timeslot.intValue()) {
                                                map.get(id).setWorkedDays(map.get(id).getWorkedDays() + 1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        int k = 0;
        HashMap<String, ArrayList<EmployeeReport>> employeeReportMap = new HashMap<>();
        List<EmployeeReport> employeeReports = new ArrayList<>();
        EmployeeReport employeeReport = new EmployeeReport();
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTime(startDate);

        Map<Integer, Double[]> lateEarlyBatch = isFromTerminal ? getLateAndEarlyPercentageBatch(startDate, map.keySet()) : Collections.emptyMap();

        for (Integer id : map.keySet()) {
            EdsTimeSlot timeSlot = employeeManager.get(id).getTimeSlot();
            TimeslotItem timeslotItem = timeSlot.toRpc();
            employeeReport = map.get(id);

            Map<Integer, int[]> timeSlotMap = new HashMap<>();
            for (EdsTimeSlotItem item : timeSlot.getItems()) {
                timeSlotMap.put(item.getDay(), new int[]{item.getStartTime(), item.getEndTime()});
            }
            employeeReport.setTimeSlotItems(timeSlotMap);
            employeeReport.setLateMinutes(timeSlot.getLateMinutes());
            employeeReport.setEarlyMinutes(timeSlot.getEarlyLeaveMinutes());
            //get planned hours from attendance raw data only when employee doesnt have shift
            if (isFromTerminal) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                Date firstDateOfMonth = cal.getTime();
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date lastDateOfMonth = cal.getTime();
                employeeReport.setLateEarlyPercent(lateEarlyBatch.getOrDefault(id, new Double[]{0d, 0d, 0d}));
                employeeReport.setPlannedHours(attendanceRawDataManager.getPlannedHoursForEmployee(firstDateOfMonth, lastDateOfMonth, id));
                employeeReport.setPlannedDays(attendanceRawDataManager.getPlannedDaysForEmployee(firstDateOfMonth, lastDateOfMonth, id));
            } else {
                if (!employeeReport.isHasShift()) {
                    employeeReport.setPlannedHours(attendanceRawDataManager.getPlannedHoursForEmployee(startDate, endDate, id));
                    employeeReport.setPlannedDays(attendanceRawDataManager.getPlannedDaysForEmployee(startDate, endDate, id));
                } else {
                    employeeReport.setWorkedDays(employeeReport.getWorkedDaysSet() != null ? employeeReport.getWorkedDaysSet().size() : 0);
                    employeeReport.setPlannedDays(employeeReport.getPlannedDaysSet() != null ? employeeReport.getPlannedDaysSet().size() : 0);
                }
            }

            if (employeeReport.getDepartmentOrPosition() != null) {
                employeeReportMap.computeIfAbsent(employeeReport.getDepartmentOrPosition(), x -> new ArrayList<>()).add(employeeReport);
            }
            employeeReport.setTimeslotItem(timeslotItem);
            employeeReports.add(employeeReport);
            employeeReport.setOvertime(employeeReport.getInhour() - getMonthWorkDaysSum(startDate, employeeReport.getAl(), employeeReport.getWithHoliday(), employeeReport.getId(), daysInMonth));
            totalInHours += employeeReport.getInhour();
            totalOvertime += employeeReport.getOvertime();
        }
        // employeeReports.sort(Comparator.comparing(EmployeeReport::getName));
        attendanceReport.setEmplReports(employeeReportMap);
        attendanceReport.setEmployeeReports(employeeReports.toArray(new EmployeeReport[]

                {
                }));
        attendanceReport.setOverTime(totalOvertime);
        attendanceReport.setTotalInHour(totalInHours);
        attendanceReport.setFingerprintTimeDtoMap(fingerprintData);

        return attendanceReport;
    }

    @Deprecated
    public EmployeeLeaveStatusListItem getEmployeeLeaveBalanceBase(Integer employeeID, String reasonCode, Date startDate, Date endDate) {
        if (employeeID == null) {
            employeeID = userManager.getUser().getObjectID();
        }
        if (reasonCode == null) {
            reasonCode = EdsSickRequest.LR_TYPE_ANNUAL_LEAVE;
        }
        if (startDate == null) {
            startDate = new Date();
        }
        if (endDate == null) {
            endDate = new Date();
        }

        int startYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(startDate));

        boolean isAnnualLeave = CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(reasonCode);
        //Initialize filter
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(employeeID);
        fp.setReasonCode(reasonCode);
        fp.setStatusID(referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED).getObjectID());
        fp.setAnnualLeave(isAnnualLeave);

        Double annualAllowanceMinutes = 0d;
        Double annualAllowanceDays = 0d;

        Integer dailyAverageTimeslotMinutes = ServerUtils.getDailyAverageTimeslotMinutes(employeeManager.get(employeeID).getTimeSlot().getItems());
        EdsLeaveReason reason = leaveReasonManager.findByCode(reasonCode);
        boolean isProrataBased = false;
        if (reason.hasProrata() && isAnnualLeave) {

            return getEmployeeBalanceFromAnnualReport(startDate, endDate, reason, fp);

        } else if (reason != null && reason.hasProrata()) {
            /*ProrateBased: Allowance Leave will be increased depending on how many days Employee worked.
             * Like eg. if employment started today he cant take LR tomorrow for 30 days, he will be able after some time
             * */
            isProrataBased = true;
            annualAllowanceDays = getEmployeeAnnualDaysForDate(employeeID, startDate, reasonCode);
            annualAllowanceMinutes = annualAllowanceDays == 0d ? annualAllowanceDays : annualAllowanceDays * dailyAverageTimeslotMinutes;
        } else if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CUSTOM_LEAVE_CALCULATION)) {
            /*CUSTOM_LEAVE_CALCULATION: Was done for specific Customer before ProrateBased option. Now even such companies can switch to ProrateBased
            but we are still handling such compnies for now.
            * */
            annualAllowanceDays = annualLeaveAllowanceManager.getLeaveAllowanceCustom(employeeID);
            annualAllowanceMinutes = annualAllowanceDays == 0d ? annualAllowanceDays : annualAllowanceDays * dailyAverageTimeslotMinutes;
        } else {
            fp.setYear(startYear);
            EdsAnnualLeaveAllowance annualLeaveAllowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(startYear, employeeID, reasonCode, null);
            annualAllowanceMinutes = annualLeaveAllowance != null ? annualLeaveAllowance.getAnnualAllowanceMinutes() : 0d;
            annualAllowanceDays = annualAllowanceMinutes == 0d ? 0d : ((double) annualAllowanceMinutes / dailyAverageTimeslotMinutes);
        }

        //Get Spent Allowances
        fp.setYear(startYear);
        HashMap<Integer, Double[]> allowanceSpentMap = sickRequestDurationManager.getAllowanceSpent(fp);
        Double allowanceHoursSpent = null;
        Double allowanceDaysSpent = null;
        Double[] allowanceSpent = allowanceSpentMap.get(employeeID);

        if (allowanceSpent != null) {
            allowanceHoursSpent = allowanceSpent[1];
            allowanceDaysSpent = allowanceSpent[2];
        }

        EmployeeLeaveStatusListItem employeeLeaveStatusListItem = new EmployeeLeaveStatusListItem();
        employeeLeaveStatusListItem.setReasonID(reason.getObjectID());
        employeeLeaveStatusListItem.setProrataBased(isProrataBased);

        DecimalFormat df = new DecimalFormat("0.#");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);

        String stat = (allowanceDaysSpent == null ? 0 : df.format(allowanceDaysSpent)) + "||" + (allowanceHoursSpent == null ? 0 : df.format(allowanceHoursSpent));
        employeeLeaveStatusListItem.setTotalUsedRequest(stat);

        //We need to add Pending LRs if its ProrataBased
        if (employeeLeaveStatusListItem.isProrataBased()) {
            Double allowanceHoursPending = null;
            Double allowanceDaysPending = null;
            //Include NOT_DEFINED
            fp.setStatusID(referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.NOT_DEFINED).getObjectID());
            //And Get Spent Allowances
            HashMap<Integer, Double[]> allowancePendingMap = sickRequestDurationManager.getAllowanceSpent(fp);
            Double[] allowancePending = allowancePendingMap.get(employeeID);
            if (allowancePending != null) {
                allowanceHoursPending = allowancePending[1];
                allowanceDaysPending = allowancePending[2];
                String statPending = (allowanceDaysPending == null ? 0 : df.format(allowanceDaysPending)) + "||" + (allowanceHoursPending == null ? 0 : df.format(allowanceHoursPending));
                employeeLeaveStatusListItem.setTotalPendingRequest(statPending);
            }
            if (allowanceHoursPending != null) {
                allowanceHoursSpent = allowanceHoursSpent != null ? allowanceHoursSpent + allowanceHoursPending : allowanceHoursPending;
            }
            if (allowanceDaysPending != null) {
                allowanceDaysSpent = allowanceDaysSpent != null ? allowanceDaysSpent + allowanceDaysPending : allowanceDaysPending;
            }
        }

        employeeLeaveStatusListItem.setTotalLeaveRequest(df.format(annualAllowanceDays) + "||" + df.format(annualAllowanceMinutes / 60));
        double annualAllowanceDaysLeft = 0;
        double annualAllowanceHoursLeft = 0;
        //Calculate Exceeded LRs
        if (allowanceHoursSpent == null) {
            employeeLeaveStatusListItem.setTotalLeftRequest(employeeLeaveStatusListItem.getTotalLeaveRequest());
            annualAllowanceDaysLeft = Double.parseDouble(df.format(annualAllowanceDays));
        } else {
            if (annualAllowanceMinutes / 60.0 <= allowanceHoursSpent) {
                employeeLeaveStatusListItem.setTotalLeftRequest("0||0");
                double annualAllowanceHoursExceeded = allowanceHoursSpent - annualAllowanceMinutes / 60.0;
                double annualAllowanceDaysExceeded = allowanceDaysSpent == null ? 0 : allowanceDaysSpent - annualAllowanceDays;
                employeeLeaveStatusListItem.setTotalExceededRequest(df.format(annualAllowanceDaysExceeded) + "||" + df.format(annualAllowanceHoursExceeded));
            } else {
                annualAllowanceHoursLeft = annualAllowanceMinutes / 60.0 - allowanceHoursSpent;
                annualAllowanceDaysLeft = annualAllowanceDays - allowanceDaysSpent;
                employeeLeaveStatusListItem.setTotalLeftRequest(df.format(annualAllowanceDaysLeft) + "||" + df.format(annualAllowanceHoursLeft));
            }
        }
        /* Retrieves "Current Leave Request Paid Days" and "Current Leave Request Non-Paid Days"
         * */
        if (startDate != null && endDate != null) {
            fp.setStartDate(startDate);
            fp.setEndDate(endDate);
            Integer availableDays = attendanceRawDataManager.getWorkingDays(fp).size();
            if (reason != null && reason.getIncludeDayOffs()) {
                availableDays = attendanceRawDataManager.getAllDaysWithIntervel(fp).size();
            }
            if (annualAllowanceDaysLeft >= availableDays) {
                employeeLeaveStatusListItem.setCurrentPaidDays(String.valueOf(availableDays));
                employeeLeaveStatusListItem.setCurrentNonPaidDays("0");
            } else {
                employeeLeaveStatusListItem.setCurrentPaidDays(df.format(annualAllowanceDaysLeft));
                employeeLeaveStatusListItem.setCurrentNonPaidDays(df.format(availableDays - annualAllowanceDaysLeft));
            }
        }
        if ((allowanceDaysSpent != null && annualAllowanceDays != null && (allowanceDaysSpent - annualAllowanceDays) > 0)) {
            employeeLeaveStatusListItem.setTotalUsedRequest(employeeLeaveStatusListItem.getTotalLeaveRequest());
        }
        return employeeLeaveStatusListItem;
    }

    @Deprecated
    private EmployeeLeaveStatusListItem getEmployeeBalanceFromAnnualReport(Date startDate, Date endDate, EdsLeaveReason reason, ListingFilterParameter fp) {
        EmployeeLeaveStatusListItem employeeLeaveStatusListItem = new EmployeeLeaveStatusListItem();
        employeeLeaveStatusListItem.setReasonID(reason.getObjectID());
        Double annualAllowanceDays = 0d;
        Double annualAllowanceMinutes = 0d;
        Double allowanceDaysSpent = 0d;
        Double allowanceHoursSpent = 0d;
        Double annualAllowanceDaysLeft = 0d;
        Double annualAllowanceHoursLeft = 0d;

        fp.setDate(startDate);
        fp.setLimit(1);
        ArrayList<LeaveBalanceReport> reportList = annualLeaveAllowanceManager.getAnnnualLeaveBalanceReport(fp);
        if (reportList != null && reportList.size() > 0) {
            annualAllowanceDays = reportList.get(0).getCurrentBalance() + reportList.get(0).getTakenDays();
            annualAllowanceMinutes = annualAllowanceDays;
            allowanceDaysSpent = reportList.get(0).getTakenDays();
            allowanceHoursSpent = allowanceDaysSpent / 60;
        }

        employeeLeaveStatusListItem.setTotalLeaveRequest(df.format(annualAllowanceDays) + "||" + df.format(annualAllowanceMinutes / 60));
        employeeLeaveStatusListItem.setTotalUsedRequest(df.format(allowanceDaysSpent) + "||" + df.format(allowanceHoursSpent));


        if (allowanceHoursSpent == 0d) {
            employeeLeaveStatusListItem.setTotalLeftRequest(employeeLeaveStatusListItem.getTotalLeaveRequest());
            annualAllowanceDaysLeft = Double.parseDouble(df.format(annualAllowanceDays));
        } else {
            if (annualAllowanceMinutes / 60.0 <= allowanceHoursSpent) {
                employeeLeaveStatusListItem.setTotalLeftRequest("0||0");
                double annualAllowanceHoursExceeded = allowanceHoursSpent - annualAllowanceMinutes / 60.0;
                double annualAllowanceDaysExceeded = allowanceDaysSpent - annualAllowanceDays;
                employeeLeaveStatusListItem.setTotalExceededRequest(df.format(annualAllowanceDaysExceeded) + "||" + df.format(annualAllowanceHoursExceeded));
            } else {
                annualAllowanceHoursLeft = annualAllowanceMinutes / 60.0 - allowanceHoursSpent;
                annualAllowanceDaysLeft = annualAllowanceDays - allowanceDaysSpent;
                employeeLeaveStatusListItem.setTotalLeftRequest(df.format(annualAllowanceDaysLeft) + "||" + df.format(annualAllowanceHoursLeft));
            }
        }
        if (startDate != null && endDate != null) {
            fp.setStartDate(startDate);
            fp.setEndDate(endDate);
            Integer availableDays = attendanceRawDataManager.getWorkingDays(fp).size();
            if (reason.getIncludeDayOffs()) {
                availableDays = attendanceRawDataManager.getAllDaysWithIntervel(fp).size();
            }
            if (annualAllowanceDaysLeft >= availableDays) {
                employeeLeaveStatusListItem.setCurrentPaidDays(String.valueOf(availableDays));
                employeeLeaveStatusListItem.setCurrentNonPaidDays("0");
            } else {
                employeeLeaveStatusListItem.setCurrentPaidDays(df.format(annualAllowanceDaysLeft));
                employeeLeaveStatusListItem.setCurrentNonPaidDays(df.format(availableDays - annualAllowanceDaysLeft));
            }
        }
        if ((allowanceDaysSpent - annualAllowanceDays) > 0) {
            employeeLeaveStatusListItem.setTotalUsedRequest(employeeLeaveStatusListItem.getTotalLeaveRequest());
        }
        return employeeLeaveStatusListItem;
    }

    private double getEmployeeAnnualDaysForDate(Integer employeeID, Date asOfDate, String reasonCode) {
        EdsEmployee employee = employeeManager.get(employeeID);
        Date thisYearStart = new Date(asOfDate == null ? new Date().getYear() : asOfDate.getYear(), 0, 1);
        Date nextYearStart = new Date((asOfDate == null ? new Date().getYear() : asOfDate.getYear()) + 1, 0, 1);
        Date startCalculateDate = employee.getStartDate() != null && thisYearStart.before(employee.getStartDate()) ? employee.getStartDate() : thisYearStart;
        int workedDays = DateUtils.differenceInDays(asOfDate == null ? nextYearStart : asOfDate, startCalculateDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        EdsAnnualLeaveAllowance annualLeaveAllowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(Integer.parseInt(dateFormat.format(asOfDate == null ? thisYearStart : asOfDate)), employeeID, reasonCode, null);
        double tempAnnualAllowanceDays = annualLeaveAllowance.getAllowanceDays();
        double totalAnnualDays = tempAnnualAllowanceDays / 365 * workedDays;
        double openingBalanceDays = employee.getOpeningBalanceDays() != null ? employee.getOpeningBalanceDays() : 0.0d;
        return openingBalanceDays + totalAnnualDays;
    }

    private int getMonthWorkDaysSum(Date startDate, int[] absentleave, int[] withHoliday, Integer id, int daysInMonth) {
        Map<Integer, Integer> employeeWeekTime = getEmployeeTimeSlotItem(id);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int employeeTimeWork = 0;
        for (int day = 1; day <= daysInMonth; day++) {
            int current = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if ((employeeWeekTime != null && employeeWeekTime.containsKey(current) && employeeWeekTime.get(current) != 0 && absentleave[day] == 0) && (withHoliday[day] == 0)) {
                employeeTimeWork += employeeWeekTime.get(current);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return employeeTimeWork;
    }

    @Override
    public int getEmployeeWorkDaysCountInMonth(Integer employeeId, Date startDate, Date endDate) {
        Map<Integer, Integer> employeeWeekTime = getEmployeeTimeSlotItem(employeeId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        int toDate = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(startDate);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int workDays = 0;
        while (day <= toDate) {
            int currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (employeeWeekTime.containsKey(currentDay) && employeeWeekTime.get(currentDay) > 0) {
                workDays++;
            }
            calendar.add(Calendar.DATE, 1);
            day++;
        }
        return workDays;
    }

    private Map<Integer, Integer> getEmployeeTimeSlotItem(Integer id) {
        EdsEmployee employee = employeeManager.get(id);
        Map<Integer, Integer> time = new HashMap<>();
        Set<EdsTimeSlotItem> items = employee.getTimeSlot().getItems();
        for (EdsTimeSlotItem item : items) {
            time.put(item.getDay(), Math.abs(item.getEndTime() - item.getStartTime()));
        }
        return time;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getSickStatusList() {
        List<EdsReference> statusList = referenceManager.listReferences(EdsSickRequest._SICK_STATUS);
        SelectItem[] result = new SelectItem[statusList.size()];
        int i = 0;
        for (EdsReference reference : statusList) {
            String value = wfmMessageSource.localize(reference.getCode(), reference.getName());
            result[i] = new SelectItem(reference.getObjectID(), value, reference.getCode());
            i++;
        }
        return result;
    }

    @Override
    public TimeSlot getEmployeeTimeSlot(Integer employeeId, DateNonConvertable date) {
        return commonService.getEmployeeTimeSlot(employeeId, date != null ? date.getNonConvertedDate() : null);
    }

    @Override
    public String getLeaveDaysCount(ListingFilterParameter fp, DateNonConvertable date, DateNonConvertable enddate) {
        Map<Integer, Double> duration = getLeaveDoubleMap(fp, date, enddate);
        return df.format(duration.values().stream().mapToDouble(Double::valueOf).sum());
    }

    public List<BackupEmployeeItem> getBackupEmployeesForLeaveRequest(Integer sickRequestId) {
        if (sickRequestId == null) {
            return null;
        }
        List<EdsBackupEmployee> backupEmployeesList = backupEmployeeManager.getBackupEmployeesBySickRequestId(sickRequestId);

        if (CollectionUtils.isEmpty(backupEmployeesList)) {
            return null;
        }

        Map<Integer, BackupEmployeeItem> backupEmployeeMap = new HashMap<>();
        for (EdsBackupEmployee backupEmployee : backupEmployeesList) {
            BackupEmployeeItem backupEmployeeItem = null;

            ApproverItemMini item = getApproverItemByBackupEmployee(backupEmployee);
            if (backupEmployee.getParentId() == null) {
                backupEmployeeItem = backupEmployeeMap.computeIfAbsent(backupEmployee.getObjectID(), V -> new BackupEmployeeItem());
                backupEmployeeItem.setDutyPercentage(backupEmployee.getDutyPercentage());
                backupEmployeeItem.setParentBackupEmployee(item);
            } else {
                backupEmployeeItem = backupEmployeeMap.computeIfAbsent(backupEmployee.getParentId(), V -> new BackupEmployeeItem());
            }
            backupEmployeeItem.getChildList().add(item);
        }
        return new ArrayList<BackupEmployeeItem>(backupEmployeeMap.values());
    }

    @Override
    public Double getLeaveDays(ListingFilterParameter fp, DateNonConvertable date, DateNonConvertable enddate) {
        Map<Integer, Double> duration = getLeaveDoubleMap(fp, date, enddate);
        return duration.values().stream().mapToDouble(Double::valueOf).sum();
    }

    private Map<Integer, Double> getLeaveDoubleMap(ListingFilterParameter fp, DateNonConvertable date, DateNonConvertable enddate) {
        NewLeaveRequest request = new NewLeaveRequest();
        request.setStartDate(date.getNonConvertedDate());
        request.setEndDate(enddate.getNonConvertedDate());
        request.setAllDay(fp.isAllDay());

        return getLeaveDuration(request, fp);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployeesByTeamsList(Integer timeslotID) {
        if (timeslotID != null) {
            return hrmsServiceLocal.getEmployeesWithTeams(timeslotID);
        } else {
            return hrmsServiceLocal.getEmployeesByTeamsList();
        }
    }

    /**
     * @param employeeID     - employee ID
     * @param yearDifference - this is used to create data for next years,
     *                       e.g. current year is 2012 and it is needed to create data for 2012, 2013 and 2014
     *                       needed yearOffset will be 2 (2014 - 2012)
     */
    @Override
    public void createAttendaceRawDataRecords(Integer employeeID, Integer yearDifference) {
        Date lastDate = attendanceRawDataManager.getLastEnteredDateForEmployee(employeeID);
        Calendar to = Calendar.getInstance();
        to.set(Calendar.MONTH, 11);
        to.set(Calendar.DAY_OF_MONTH, 31);
        ServerUtils.setBeginningOfTheDay(to);

        Calendar from = Calendar.getInstance();
        from.set(Calendar.DAY_OF_YEAR, 1);
        ServerUtils.setBeginningOfTheDay(from);

        EdsEmployee employee = employeeManager.get(employeeID);

        Calendar endDateToCompare = Calendar.getInstance();
        endDateToCompare.setTime(ServerUtils.getYearStartDate(endDateToCompare.get(Calendar.YEAR)));

        if (yearDifference != null && yearDifference > 0) {
            endDateToCompare.add(Calendar.YEAR, yearDifference);
            to.add(Calendar.YEAR, yearDifference);
            if (lastDate != null) {
                from.setTime(lastDate);
            }
        }
        if (lastDate == null || !lastDate.after(endDateToCompare.getTime()) && employee != null) {
            insertARD(from, to, employee);
            /*List<Integer> employeesIds = employeeManager.getEmployeeIds();
            for (Integer employeeId : employeesIds) {
                createEmployeeAttendanceData(SecurityContext.getCompanyID(), from, to, employeeId);
            }*/
        }
    }

    /**
     * Inserts the ARD only if there is no record in the database
     *
     * @param from
     * @param to
     * @param employee
     */
    private void insertARD(Calendar from, Calendar to, EdsEmployee employee) {
        Integer employeeID = employee.getObjectID();
        copyLeaveAllowanceForNextYear(employeeID);

        LOGGER.info("COMPANY ID: " + employee.getCompany().getObjectID() + " >>STARTED ARD CREATION FOR EMPLOYEE ID: " + employeeID);

        HashMap<Date, EdsTimeSlotItem> exceptionalTimeSlotItem = employee.getTimeSlot().getExceptionalTimeSlotItem();

        Integer[] timeslotMinutes = new Integer[7];
        for (EdsTimeSlotItem timeSlotItem : employee.getTimeSlot().getItems()) {
            timeslotMinutes[timeSlotItem.getDay()] = timeSlotItem.getMinutes();
        }

        List<EdsDate> dates = dateManager.getDatesByDates(from.getTime(), to.getTime());
        List<EdsHoliday> holidays = holidayManager.getHolidaysByDatesAndLocation(from.getTime(), to.getTime(), employee.getLocation());

        for (EdsDate date : dates) {
            from.setTime(date.getFromDate());
            EdsAttendanceRawData attendanceRawData = attendanceRawDataManager.getAttendanceRawDataByDate(from.getTime(), employeeID);
            if (attendanceRawData == null) {
                attendanceRawData = new EdsAttendanceRawData();
            }
            attendanceRawData.setDate(date.getFromDate());

            if (exceptionalTimeSlotItem.containsKey(date.getFromDate())) {
                EdsTimeSlotItem edsTimeSlotItem = exceptionalTimeSlotItem.get(date.getFromDate());
                int timeSlotItemMinutes = edsTimeSlotItem.getMinutes();
                attendanceRawData.setTimeSlot(timeSlotItemMinutes);
                attendanceRawData.setDayOff(!(timeSlotItemMinutes > 0));
            } else {
                attendanceRawData.setTimeSlot(timeslotMinutes[from.get(Calendar.DAY_OF_WEEK) - 1]);
                attendanceRawData.setDayOff(!(timeslotMinutes[from.get(Calendar.DAY_OF_WEEK) - 1] > 0));
            }
            attendanceRawData.setEmployee(employee);
            updateHolidaysDetails(holidays, attendanceRawData);
            attendanceRawDataManager.createOrUpdate(attendanceRawData);
        }
        LOGGER.info(">>CREATED ARD DATA FOR EMPLOYEE ID: " + employeeID);
    }

    private void copyLeaveAllowanceForNextYear(Integer id) {
        List<EdsLeaveReason> reasons = leaveReasonManager.listActiveReasons();
        if (reasons == null || reasons.size() == 0) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        Integer allowanceYear = calendar.get(Calendar.YEAR);
        for (EdsLeaveReason reason : reasons) {
            if (reason == null) {
                reason = leaveReasonManager.findByCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
            }
            EdsAnnualLeaveAllowance oldAllowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(allowanceYear, id, reason.getCode(), null);
            EdsAnnualLeaveAllowance allowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(allowanceYear + 1, id, reason.getCode(), null);
            if (allowance != null)
                return;
            allowance = new EdsAnnualLeaveAllowance();
            if (oldAllowance != null) {
                allowance.setAllowanceDays(oldAllowance.getAllowanceDays());
            } else {
                allowance.setAllowanceDays(reason.getLeaveDays());
            }
            allowance.setAllowanceYear(allowanceYear + 1);
            allowance.setEmployee(employeeManager.get(id));
            allowance.setReasonCode(reason.getCode());
            annualLeaveAllowanceManager.create(allowance);
        }
    }

    private void updateHolidaysDetails(List<EdsHoliday> holidays, EdsAttendanceRawData attendanceRawData) {
        Calendar holidayStartDate = Calendar.getInstance();
        Calendar holidayEndDate = Calendar.getInstance();
        for (EdsHoliday holiday : holidays) {
            holidayStartDate.setTime(holiday.getStartDate());
            holidayEndDate.setTime(holiday.getEndDate());
            if (holidayStartDate.getTime().getTime() <= attendanceRawData.getDate().getTime() && attendanceRawData.getDate().getTime() <= holidayEndDate.getTime().getTime()) {
                if (holiday.getLocations().size() == 0) {
                    attendanceRawData.setHoliday(holiday.isDayOff());
                    attendanceRawData.setHolidayFromAnnualLeave(holiday.isTakeAnnual());
                } else {
                    for (EdsLocation location : holiday.getLocations()) {
                        if (location.getObjectID().equals(attendanceRawData.getEmployee().getLocation().getObjectID())) {
                            attendanceRawData.setHoliday(holiday.isDayOff());
                            attendanceRawData.setHolidayFromAnnualLeave(holiday.isTakeAnnual());
                        }
                    }
                }
            }
        }
    }

    /**
     * This is for API
     *
     * @return SelectItemTO list
     */
    public List<SelectItem> getLeaveRequrestTypes() {
        List<EdsReference> types = referenceManager.listReferences(EdsSickRequest._SICK_TYPE);
        List<SelectItem> items = new ArrayList<>();
        if (types != null) {
            for (EdsReference reference : types) {
                items.add(new SelectItem(reference.getObjectID(), wfmMessageSource.localize(reference.getCode(), reference.getName()), reference.getCode()));
            }
        }
        return items;
    }

    @Override
    public BenefitRequestItem getBenefitRequests(Integer objectID) {
        BenefitRequestItem item;
        EdsBenefitRequest request = new EdsBenefitRequest();
        EdsUser user = userManager.getUser();
        if (objectID != null) {
            request = benefitRequestManager.get(objectID);
            item = request.toRequestItem(true);
        } else {
            item = new BenefitRequestItem();
        }
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.BenefitRequestList);
        item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(request.getCustomFields(), customFieldsItems));
        String code = user.getEmployee().getProfile().getEmployeeCode();
        item.setUser((code != null && !"".equals(code.trim()) ? code + " - " : "") + user.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
        item.setUserID(user.getObjectID());
        return item;
    }

    @Override
    public SelectItem[] getBenefitListAsSelectItems(ListingFilterParameter fp) {
        List<EdsBenefit> benefits = benefitManager.getBenefitList(fp);
        int i = 0;
        SelectItem[] items = new SelectItem[benefits.size()];
        for (EdsBenefit edsBenefit : benefits) {
            items[i++] = edsBenefit.getAsSelectItem();
        }
        return items;
    }

    @Override
    public Integer saveBenefitRequest(BenefitRequestItem item) {
        System.out.println("SAVE BENEFIT Request id : " + item.getObjectID());
        EdsUser user = userManager.getUser();
        EdsBenefitRequest benefitRequest = new EdsBenefitRequest();
        if (item.getObjectID() != null) {
            benefitRequest = benefitRequestManager.get(item.getObjectID());
        } else {
            benefitRequest.setCreator(user);
            benefitRequest.setCreatedDate(new Date());
        }
        benefitRequest.setStatus(referenceManager.findReference(EdsBenefitRequest._BENEFIT_REQUEST_STATUSES, item.getStatus().getCode()));
        benefitRequest.setRequester(employeeManager.get(item.getRequesterID()));
        benefitRequest.setApprover(employeeManager.get(item.getApproverID()));
        benefitRequest.setBenefit(benefitManager.get(item.getBenefitID()));
        benefitRequest.setDate(item.getDate().getNonConvertedDate());
        benefitRequest.setDescription(item.getDescription());
        benefitRequest.setRequestedQuantity(item.getRequestedQuantity());
        benefitRequest.setCustomFields(createBenefitRequestCustomFields(item.getCustomFields()));
        benefitRequest.setLastUpdateTime(new Date());
        benefitRequestManager.createOrUpdate(benefitRequest);
        if (EdsBenefitRequest.WAITING_FOR_APPROVAL.equals(item.getStatus().getCode())) {
            changeBenefitRequestStatus(benefitRequest.getObjectID(), EdsBenefitRequest.WAITING_FOR_APPROVAL, null, item.getRequestedQuantity());
        }
        return benefitRequest.getObjectID();
    }

    private EdsBenefitRequestCustomFields createBenefitRequestCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsBenefitRequestCustomFields benefitRequestCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                benefitRequestCustomFields = benefitRequestCFManager.get(customFieldItems.get(0).getObjectId());
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
                benefitRequestCustomFields = new EdsBenefitRequestCustomFields();
                benefitRequestCFManager.create(benefitRequestCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(benefitRequestCustomFields, customFieldItems);
            return benefitRequestCustomFields;
        }
        return null;
    }

    @Override
    public EmployeeLeaveStatusListItem getTotalAndLeftRequest(Integer employeeID, Integer benefitID, DateNonConvertable date) {
        EmployeeLeaveStatusListItem item = new EmployeeLeaveStatusListItem();
        int year = ServerUtils.getYear(date.getNonConvertedDate());
        EdsBenefit benefit = benefitManager.get(benefitID);
        if (benefit != null) {
            if (benefit.getQtytype() != null) {
                if (EdsBenefit._CURRENCY.equals(benefit.getQtytype().getCode())) {
                    item.setQtyType(benefit.getCurrency() != null ? benefit.getCurrency().getName() : "");
                } else {
                    item.setQtyType(benefit.getQtytype().getName());
                }
            }

            Double totalAllowance = 0.0;
            EdsEmployeeBenefitAllowance benefitAllowance = employeeBenefitAllowanceManager.getBenefitAllowance(year, employeeID, benefitID);
            if (benefitAllowance != null) {
                totalAllowance = benefitAllowance.getAllowance();
            }
            Double usedAllowance = benefitRequestManager.getEmployeeUsedBenefitAllowance(ServerUtils.getYearStartDate(year), ServerUtils.getYearEndDate(year), employeeID, benefitID);
            Double left = totalAllowance - usedAllowance;
            item.setTotalUsedRequest(String.valueOf(usedAllowance));
            item.setTotalLeaveRequest(String.valueOf(totalAllowance));
            item.setTotalLeftRequest(String.valueOf(left));
        } else {
            item.setTotalUsedRequest(String.valueOf(0));
            item.setTotalLeaveRequest(String.valueOf(0));
            item.setTotalLeftRequest(String.valueOf(0));
        }
        return item;
    }

    @Override
    public ListResult<BenefitRequestItem> getBenefitRequestList(ListingFilterParameter fp) {
        FacetFilterRpc employeeFacetFilter = fp.getFacetFilter();
        if (employeeFacetFilter != null && !employeeFacetFilter.isFilterChanges()) {
            employeeFacetFilter = commonServiceLocal.getUserFacetFilter(employeeFacetFilter);
        }
        return benefitRequestManager.getBenefitRequestList(fp);
    }


    @Override
    public void deleteBenefitRequest(Integer objectID) {
        EdsBenefitRequest benefitRequest = benefitRequestManager.get(objectID);
        if (benefitRequest != null) {
            benefitRequest.setLastUpdateTime(new Date());
            benefitRequest.setDeleted(true);
        }
    }

    @Override
    public FacetFilterRpc getBenefitRequestsFacetFilterData(FacetFilterRpc benefitRequestsData) {
        if (!benefitRequestsData.isFilterChanges()) {
            benefitRequestsData = commonServiceLocal.getUserFacetFilter(benefitRequestsData);
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(benefitRequestsData.getSearchKey());
        fp.setViewType(benefitRequestsData.getName());

        ListResult<BenefitRequestItem> benefitRequests = benefitRequestManager.getBenefitRequestList(fp);
        return fillFacetFilterDataWithNA(benefitRequests, benefitRequestsData);
    }

    public FacetFilterRpc fillFacetFilterDataWithNA(ListResult<BenefitRequestItem> benefitRequestsDataListResult, FacetFilterRpc facetFilter, String... codeNameList) {

        if (facetFilter == null) {
            return null;
        }

        Set<String> keySet = (codeNameList != null && codeNameList.length != 0)
                ? new HashSet<>(Arrays.asList(codeNameList))
                : facetFilter.getFacetContentMap().keySet();

        CommonServiceLocal serviceLocal = StaticContextAccessor.getBean(CommonServiceLocal.class);

        keySet.stream()
                .filter(facetFilter.getShowSolrFieldMap()::containsKey)
                .forEach(key -> {
                    FacetSolrField facetSolrField = facetFilter.getShowSolrFieldMap().get(key);
                    if (facetSolrField != null && facetFilter.getFacetContentMap().containsKey(key)) {
                        Map<Integer, SelectItem> itemMap = createItemMap(benefitRequestsDataListResult, key);
                        facetFilter.getFacetContentMap().get(key).setFacetItems(itemMap.values().toArray(new SelectItem[0]));
                    }
                });

        Map<Integer, BenefitRequestItem> productCategoryCustomFieldsMap = new HashMap<>();

        benefitRequestsDataListResult.getList()
                .stream()
                .filter(i -> i.getCustomFields() != null)
                .forEach(i -> {
                    //   BenefitRequestItem customFields = i.getCustomFields();
                    //  productCategoryCustomFieldsMap.put(customFields.getObjectID(), customFields);
                });

        List<CompanyCustomFieldItem> customFieldItems = facetFilter.getType() != null
                ? serviceLocal.getCompanyCustomFields(facetFilter.getType().getViewName())
                : new ArrayList<>();

        customFieldItems.stream()
                .filter(i -> facetFilter.getShowSolrFieldMap().containsKey(i.getColumnCode()))
                .forEach(key -> {
                    FacetSolrField facetSolrField = facetFilter.getShowSolrFieldMap().get(key.getColumnCode());
                    if (facetSolrField != null && facetFilter.getFacetContentMap().containsKey(key.getColumnCode())) {
                        Map<Integer, SelectItem> itemMap = createCustomFieldItemMap(productCategoryCustomFieldsMap, key.getColumnCode());
                        facetFilter.getFacetContentMap().get(key.getColumnCode()).setFacetItems(itemMap.values().toArray(new SelectItem[0]));
                    }
                });

        return facetFilter;
    }

    private Map<Integer, SelectItem> createCustomFieldItemMap(Map<Integer, BenefitRequestItem> productCategoryCustomFieldsMap, String filterKey) {
        Map<String, Integer> itemCountMap = new LinkedHashMap<>();
        Map<Integer, SelectItem> itemMap = new LinkedHashMap<>();

        productCategoryCustomFieldsMap.forEach((k, v) -> {
            // String stringValue = v.getStringValue(filterKey);

//            if (stringValue != null && !stringValue.isEmpty()) {
//                itemCountMap.compute(stringValue, (key, count) -> (count == null) ? 1 : count + 1); // Group by label
//            }
        });

        itemCountMap.forEach((label, count) -> {
            StringBuilder fieldName = new StringBuilder();
            fieldName.append(label);
            fieldName.append(" ( <b>").append(count).append("</b> )");

            int fieldId = label.hashCode();
            itemMap.put(fieldId, new SelectItem(fieldId, null, fieldName.toString()));

        });

        return itemMap;
    }

    private Map<Integer, SelectItem> createItemMap(ListResult<BenefitRequestItem> benefitRequestItemListResult, String filterKey) {
        Map<Integer, Integer> itemCountMap = new LinkedHashMap<>();
        Map<Integer, SelectItem> itemMap = new LinkedHashMap<>();

        benefitRequestItemListResult.getList().forEach(g -> {
            switch (filterKey) {
                case "requester":
                    int requesterId = (g.getRequester() == null) ? -1 : g.getRequesterID();
                    itemCountMap.compute(requesterId, (key, count) -> (count == null) ? 1 : count + 1);

                    StringBuilder requesterName = new StringBuilder();
                    if (requesterId == -1) {
                        requesterName.append("N/A");
                    } else {
                        requesterName.append(g.getRequester());
                    }

                    requesterName.append(" ( <b>").append(itemCountMap.get(requesterId)).append("</b> )");
                    itemMap.put(requesterId, new SelectItem(requesterId, null, requesterName.toString()));

                    break;
                case "status":
                    int parentId = (g.getStatus() == null) ? -1 : g.getStatus().getId();
                    itemCountMap.compute(parentId, (key, count) -> (count == null) ? 1 : count + 1);

                    StringBuilder parentName = new StringBuilder();
                    if (parentId == -1) {
                        parentName.append("N/A");
                    } else {
                        parentName.append(g.getStatus().getName());
                    }

                    parentName.append(" ( <b>").append(itemCountMap.get(parentId)).append("</b> )");
                    itemMap.put(parentId, new SelectItem(parentId, null, parentName.toString()));

                    break;
                case "approver":
                    int approverId = (g.getApprover() == null) ? -1 : g.getApproverID();
                    itemCountMap.compute(approverId, (key, count) -> (count == null) ? 1 : count + 1);

                    StringBuilder approverName = new StringBuilder();
                    if (approverId == -1) {
                        approverName.append("N/A");
                    } else {
                        approverName.append(g.getApprover());
                    }

                    approverName.append(" ( <b>").append(itemCountMap.get(approverId)).append("</b> )");
                    itemMap.put(approverId, new SelectItem(approverId, null, approverName.toString()));

                    break;
                case "type":
                    int typeId = (g.getBenefitName() == null) ? -1 : g.getBenefitID();
                    itemCountMap.compute(typeId, (key, count) -> (count == null) ? 1 : count + 1);

                    StringBuilder typeName = new StringBuilder();
                    if (typeId == -1) {
                        typeName.append("N/A");
                    } else {
                        typeName.append(g.getBenefitName());
                    }

                    typeName.append(" ( <b>").append(itemCountMap.get(typeId)).append("</b> )");
                    itemMap.put(typeId, new SelectItem(typeId, null, typeName.toString()));

                    break;
            }
        });

        return itemMap;
    }

    @Override
    public Integer changeBenefitRequestStatus(Integer objectID, String status, String note, Double requestedQuantity) {
        try {
            EdsUser user = userManager.getUser();
            EdsBenefitRequest benefitRequest = benefitRequestManager.get(objectID);

            if (!Constants.BR_REJECTED.equals(status)) {
                EmployeeLeaveStatusListItem benefitRequestInfo = getTotalAndLeftRequest(benefitRequest.getRequester().getObjectID(), benefitRequest.getBenefit().getObjectID(), new DateNonConvertable(benefitRequest.getDate()));
                if (requestedQuantity - Double.valueOf(benefitRequestInfo.getTotalLeftRequest()) > 0) {
                    return -1;
                }
            }
            String oldStatusCode = benefitRequest.getStatus().getCode();
            benefitRequest.setStatus(referenceManager.findReference(EdsBenefitRequest._BENEFIT_REQUEST_STATUSES, status));
            benefitRequest.setRejectionReason(note);
            benefitRequest.setLastUpdateTime(new Date());
            benefitRequest.setRequestedQuantity(Constants.BR_REJECTED.equals(status) ? 0d : requestedQuantity);
            benefitRequestManager.update(benefitRequest);

            if (benefitRequest != null && benefitRequest.getBenefit() != null && benefitRequest.getRequester() != null && benefitRequest.getBenefit().getCode().equals(EdsBenefit._ANNUAL_LEAVE_ALLOWANCE_INCREASE)) {
                Integer currentyear = ServerUtils.getYear(new Date());
                Double leaveDays = 0d;
                EdsAnnualLeaveAllowance leaveAllowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(currentyear, benefitRequest.getRequester().getObjectID(), EdsSickRequest.LR_TYPE_ANNUAL_LEAVE, null);
                if (leaveAllowance == null) {
                    leaveAllowance = new EdsAnnualLeaveAllowance();
                    leaveAllowance.setAddPrevious(false);
                } else {
                    leaveDays = leaveAllowance.getAllowanceDays();
                }
                leaveAllowance.setEmployee(benefitRequest.getRequester());
                leaveAllowance.setAllowanceYear(currentyear);
                leaveAllowance.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
                if (EdsBenefitRequest.APPROVED.equals(status)) {
                    leaveDays = leaveDays + benefitRequest.getRequestedQuantity();
                } else if (EdsBenefitRequest.REJECTED.equals(status) && oldStatusCode.equals(EdsBenefitRequest.APPROVED)) {
                    leaveDays = leaveDays - benefitRequest.getRequestedQuantity();
                }
                leaveAllowance.setLastYearDay(leaveDays);
                annualLeaveAllowanceManager.createOrUpdate(leaveAllowance);
            }

            String statusCode = "";
            if (EdsBenefitRequest.WAITING_FOR_APPROVAL.equals(status)) {
                statusCode = BenefitRequestEventListenerImpl.BR_SUBMITTED;
            } else if (EdsBenefitRequest.APPROVED.equals(status)) {
                statusCode = BenefitRequestEventListenerImpl.BR_APPROVED;
            } else if (EdsBenefitRequest.REJECTED.equals(status)) {
                statusCode = BenefitRequestEventListenerImpl.BR_REJECTED;
            }
            baseEventPostProcessor.registerEvent(BenefitRequestEventListenerImpl.TYPE, statusCode, benefitRequest, user);
            return 1;
        } catch (NumberFormatException e) {
            LOGGER.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public SelectItem getCurrentUser(Integer employeeID) {
        EdsEmployee employee;
        SelectItem currentUser;
        EdsUser user = userManager.getUser();
        if (employeeID != null) {
            employee = employeeManager.get(employeeID);
        } else if (employeeManager.getUser() instanceof EdsEmployee) {
            employee = (EdsEmployee) employeeManager.getUser();
        } else {
            return new SelectItem(-1, "");
        }
        String code = employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null && !"".equals(employee.getProfile().getEmployeeCode()) ? employee.getProfile().getEmployeeCode() : "";
        if (user.getObjectID().equals(employee.getObjectID())) {
            currentUser = new SelectItem(employee.getObjectID(), (!"".equals(code.trim()) ? code + " - " : "") + employee.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
        } else {
            currentUser = new SelectItem(employee.getObjectID(), (!"".equals(code.trim()) ? code + " - " : "") + employee.getName());
        }
        return currentUser;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEmployeeAttendanceData(Integer companyID, Calendar from, Calendar to, Integer employeeId) {
        EdsEmployee employee = employeeManager.get(employeeId);
        insertARD(from, to, employee);
    }

    private static class AttendanceKey {
        private final Date date;
        private final Integer employeeId;

        @Override
        public int hashCode() {
            return (this.getDate().toString() + "|" + this.getEmployeeId().toString()).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof AttendanceKey && this.getDate().equals(((AttendanceKey) obj).getDate()) && this.getEmployeeId().equals(((AttendanceKey) obj).getEmployeeId());
        }

        private AttendanceKey(Date date, Integer employeeId) {
            this.date = date;
            this.employeeId = employeeId;
        }

        public Date getDate() {
            return date;
        }

        public Integer getEmployeeId() {
            return employeeId;
        }
    }

    @Override
    public ListResult<LeaveBalanceReport> getEmployeeLeaveBalanceReport(ListingFilterParameter fp) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(LOGGER, kpiLog, "Get Employee list for leave balance report");

        fp.setModule(PermissionConstants.HRMS_CONTEXT);
        fp.setCheckNumber(employeeManager.isIntegerEmployeeCodeEnabled());
        ServerUtils.kpiLog(LOGGER, kpiLog, fp.isCheckNumber() ? "Employee code INTEGER" : "Employee code STRING");

        FacetFilterRpc employeeFacetFilter = fp.getFacetFilter();
        if (employeeFacetFilter != null && !employeeFacetFilter.isFilterChanges()) {
            employeeFacetFilter = commonServiceLocal.getUserFacetFilter(employeeFacetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        StringBuilder solrQuery = new StringBuilder();

        boolean showAllEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEE_LIST);
        List<Integer> departmentList = Lists.newArrayList();
        if (!showAllEmployees && ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST)) {
            List<EdsDepartment> edsDepartments = departmentManager.getTeamsByEmployeeId(edsUser.getObjectID());
            departmentList.addAll(edsDepartments.stream().map(EdsDepartment::getObjectID).toList());
        }

        solrQuery.append(QueryBuilderForSolr.getEmployeeSolrQuery(fp, edsUser, departmentList));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(employeeFacetFilter, edsUser.getCompany(), null, null));

        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EMPLOYEE_CORE);
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(fp.getStart());

        query.setFields(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID, SolrEmployeeRepresenter.FIELD_EMPLOYEE_NAME, SolrEmployeeRepresenter.FIELD_EMPLOYEE_NUMBER, SolrEmployeeRepresenter.FIELD_EMPLOYEE_INTEGER_NUMBER, SolrEmployeeRepresenter.FIELD_DEPARTMENT_ID, SolrEmployeeRepresenter.FIELD_HIRE_DATE, SolrEmployeeRepresenter.FIELD_END_DATE, SolrEmployeeRepresenter.FIELD_STATUS_NAME, SolrEmployeeRepresenter.FIELD_OPENING_BALANCE_DAYS);

        query.setParam(CommonParams.ROWS, fp.getLimit() > 0 ? String.valueOf(fp.getLimit()) : "50");
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                boolean desc = !fp.isAscending();
                if (EmployeeListItem.EMPLOYEE_NUMBER.equals(fp.getSortField())) {
                    if (fp.isCheckNumber()) {
                        query.setSort(SolrEmployeeRepresenter.FIELD_EMPLOYEE_INTEGER_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    } else {
                        query.setSort(SolrEmployeeRepresenter.SORTABLE_EMPLOYEE_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    }
                } else if (EmployeeListItem.EMPLOYEE_NAME.equals(fp.getSortField())) {
                    query.setSort(SolrEmployeeRepresenter.SORTABLE_EMPLOYEE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                }
            } else {
                query.setSort(SolrEmployeeRepresenter.SORTABLE_EMPLOYEE_NAME, SolrQuery.ORDER.asc);
            }
        } else {
            if (fp.isCheckNumber()) {
                query.setSort(SolrEmployeeRepresenter.FIELD_EMPLOYEE_INTEGER_NUMBER, SolrQuery.ORDER.asc);
            }
        }

        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getEmployeeFromSolrResult(resp, edsUser, fp);

    }

    private void getLeaveMinutesMap(EdsSickRequest sickRequest, HashMap<AttendanceKey, AttendanceValue> attendanceMap, Calendar sickRequestStart, Integer timeSlot, int leaveMinutes) {
        //we need to update timeslot because timeslot is updated till the end of current year, bu LR could be left for the next year, so adjust timeslot too.
        Integer leave = 0;
        Integer leavePending = 0;
        Integer leaveDenied = 0;
        Integer fromAnnualLeaveTime = 0;
        Integer paidTime = 0;

        if (isOk(sickRequest.getOverallStatus()) && EdsSickRequest.NOT_DEFINED.equals(sickRequest.getOverallStatus().getCode())) {
            //pending
            leavePending = leaveMinutes;
        } else if (isOk(sickRequest.getOverallStatus()) && EdsSickRequest.APPROVED.equals(sickRequest.getOverallStatus().getCode())) {
            //approved
            leave = leaveMinutes;
            fromAnnualLeaveTime = (sickRequest.getToTakeFromAllowance() ? leaveMinutes : 0);
            paidTime = (sickRequest.getType() != null && EdsSickRequest.PAID.equals(sickRequest.getType().getCode()) ? leaveMinutes : 0);
        } else if (isOk(sickRequest.getOverallStatus()) && EdsSickRequest.DENIED.equals(sickRequest.getOverallStatus().getCode())) {
            //rejected
            leaveDenied = leaveMinutes;
        }

        AttendanceKey attendanceKey = new AttendanceKey(ServerUtils.getDayStartTime(sickRequestStart.getTime()), sickRequest.getEmployee().getObjectID());
        AttendanceValue attendanceValue = new AttendanceValue(timeSlot, leave, leavePending, leaveDenied, fromAnnualLeaveTime, paidTime, true);
        if (attendanceMap.containsKey(attendanceKey)) {
            attendanceMap.get(attendanceKey).add(attendanceValue);
        } else {
            attendanceMap.put(attendanceKey, attendanceValue);
        }
    }

    public void copyLastYearLeaveAllowanceMinutes(Integer currentYear) {
        try {
            boolean leaveCustomisationEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SICK_LEAVE_SETTINGS_CALCULATION);
            if (leaveCustomisationEnabled) {
                LRSettingsItem lrSettingsItem = profileService.getLrSettingsItem();
                if (lrSettingsItem != null && lrSettingsItem.getCopyPreviousYearAllowances()) {
                    EdsReference approvedStatus = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);
                    BigDecimal coefficient = lrSettingsItem.getPrevYearAllowanceCopyPercent() != null ? lrSettingsItem.getPrevYearAllowanceCopyPercent().divide(new BigDecimal("100.00"), 5, RoundingMode.FLOOR) : BigDecimal.ONE;
                    ListingFilterParameter fpLast = new ListingFilterParameter();
                    fpLast.setYear(currentYear - 1);
                    fpLast.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
                    fpLast.setStatusID(approvedStatus.getObjectID());
                    fpLast.setAnnualLeave(true);
                    HashMap<Integer, Double[]> lastYearSpent = sickRequestDurationManager.getAllowanceSpent(fpLast);
                    List<Integer> employeeIds = employeeManager.getEmployeeIds();
                    List<EdsAnnualLeaveAllowance> annualLeaveAllowance = annualLeaveAllowanceManager.getLeaveAllowancesByReason(currentYear - 1, employeeIds, CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
                    for (EdsAnnualLeaveAllowance leaveAllowance : annualLeaveAllowance) {
                        Integer allowance = leaveAllowance.getAnnualAllowanceMinutes() != null ? leaveAllowance.getAnnualAllowanceMinutes() : 0;
                        Integer employeeId = leaveAllowance.getEmployee() != null ? leaveAllowance.getEmployee().getObjectID() : null;
                        if (employeeId != null) {
                            Double[] values = lastYearSpent.get(employeeId);
                            Double spentMinutes = ((values != null) && (values[0] != null)) ? values[0] : 0.0;
                            Integer minutesLeft = allowance - spentMinutes.intValue();
                            System.out.println("Previous year Allowance is being calculated for employee" + employeeId + " of " + currentYear + " in company " + ServerSecurityContext.getInstance().getCompanyId() + " total minutes are " + minutesLeft);
                            if (minutesLeft > 0) {
                                Integer copiableMinutes = new BigDecimal(minutesLeft).multiply(coefficient).intValue();
                                EdsAnnualLeaveAllowance annualLeaveAllowance_ = annualLeaveAllowanceManager.getLeaveAllowanceByReason(currentYear, employeeId, EdsSickRequest.LR_TYPE_ANNUAL_LEAVE, null);
                                if (annualLeaveAllowance_ == null) {
                                    annualLeaveAllowance_ = new EdsAnnualLeaveAllowance();
                                    annualLeaveAllowance_.setAnnualAllowanceMinutes(allowance);
                                    annualLeaveAllowance_.setEmployee(employeeManager.get(employeeId));
                                    annualLeaveAllowance_.setAllowanceYear(currentYear);
                                    annualLeaveAllowance_.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
                                }
                                annualLeaveAllowance_.setLastYearMinutes(copiableMinutes);
                                annualLeaveAllowanceManager.createOrUpdate(annualLeaveAllowance_);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ListResult<LeaveBalanceReport> getEmployeeFromSolrResult(QueryResponse resp, EdsUser user, ListingFilterParameter filterParameter) {
        ArrayList<LeaveBalanceReport> itemList = new ArrayList<>();
        int totalNumber = 0;
        SolrDocumentList resultList = resp != null ? resp.getResults() : null;
        if (resultList == null) {
            return new ListResult<>(itemList, totalNumber);
        }
        List<Integer> idList = resultList.stream().map(x -> SolrUtils.asInteger(x, SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID)).collect(Collectors.toList());

        Integer year = filterParameter.getYear();
        if (filterParameter.getYear() == null) {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        filterParameter.setEmployeeIDs(idList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        filterParameter.setStatusCode(Constants.LR_STATUS_SS_APPROVED);
        filterParameter.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
        filterParameter.setPaid(true);

        HashMap<Integer, Double> duration = sickRequestDurationManager.getEmployeeLeaveDurations(filterParameter);
        Map<Integer, EdsAnnualLeaveAllowance> allowance = annualLeaveAllowanceManager.getAllowancesMapByYearAndReasonAndEmployee(year, EdsSickRequest.LR_TYPE_ANNUAL_LEAVE, idList);


        totalNumber = (int) resp.getResults().getNumFound();
        EdsHRSettings hrSettings = hrSettingsManager.findOne();
        boolean hasOpeningBalance = hasOpeningBalance(hrSettings, filterParameter.getYear());

        String userLang = ServerUtils.getUserLocale().getLanguage();
        for (SolrDocument relevantDoc : resultList) {
            LeaveBalanceReport item = new LeaveBalanceReport();
            Integer employeeID = SolrUtils.asInteger(relevantDoc, SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID);
            item.setEmployeeID(employeeID);
            item.setEmployeeName(SolrUtils.asString(relevantDoc, SolrEmployeeRepresenter.FIELD_EMPLOYEE_NAME));
            item.setEmployeeNumber(filterParameter.isCheckNumber() ? String.valueOf(relevantDoc.getFieldValue(SolrEmployeeRepresenter.FIELD_EMPLOYEE_INTEGER_NUMBER)) : SolrUtils.asString(relevantDoc, SolrEmployeeRepresenter.FIELD_EMPLOYEE_NUMBER));
            String departmentNameSolrCode = switch (userLang) {
                case "uz" -> SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME_UZ;
                case "ru" -> SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME_RU;
                case "en" -> SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME_EN;
                case "ar" -> SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME_AR;
                default -> SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME;
            };
            item.setDepartment(SolrUtils.asString(relevantDoc, departmentNameSolrCode) != null ? SolrUtils.asString(relevantDoc, departmentNameSolrCode) : SolrUtils.asString(relevantDoc, SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME));
            Date hireDate = SolrUtils.asDate(relevantDoc, SolrEmployeeRepresenter.FIELD_HIRE_DATE);
            Date endDate = SolrUtils.asDate(relevantDoc, SolrEmployeeRepresenter.FIELD_END_DATE);
            item.setHireDate(hireDate != null ? new DateNonConvertable(hireDate) : null);
            item.setResignDate(hireDate != null ? new DateNonConvertable(endDate) : null);
            item.setStatus(SolrUtils.asString(relevantDoc, SolrEmployeeRepresenter.FIELD_STATUS_NAME));
            item.setOpeningBalance(SolrUtils.asDouble(relevantDoc, SolrEmployeeRepresenter.FIELD_OPENING_BALANCE_DAYS));

            Double dayTaken = 0d;
            Double allowanceDays = 0d;
            Double durationArray = duration.get(employeeID);
            if (durationArray != null) {
                dayTaken = durationArray;
            }
            EdsAnnualLeaveAllowance edsLeaveAllowance = allowance.get(employeeID);
            if (edsLeaveAllowance != null) {
                allowanceDays = hasOpeningBalance ? item.getOpeningBalance() : edsLeaveAllowance.getAllowanceDays();
            }
            item.setTakenDays(dayTaken);
            item.setLeaveAllowanceDays(allowanceDays);
            item.setCurrentBalance(allowanceDays - dayTaken);
            itemList.add(item);
        }
        return new ListResult<>(itemList, totalNumber);
    }

    @Override
    public String getLeaveRequestSolrQuery(ListingFilterParameter fp, EdsUser edsUser) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String startDateStr = null;
        String endDateStr = null;
        if (fp.getYear() != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(ServerUtils.getYearStartDate(fp.getYear()));
            startDateStr = dateFormat.format(calendar.getTime());

            calendar = new GregorianCalendar();
            calendar.setTime(ServerUtils.getYearEndDate(fp.getYear()));
            endDateStr = dateFormat.format(calendar.getTime());
        }

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrLeaveRequestConst.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());

        if (!edsUser.hasEitherRoles(EdsRole.ADMIN_CODE) && fp.getEmployeeId() == null) {
            boolean showAllEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEE_LIST);
            if (!showAllEmployees) {
                boolean showTeamEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST);
                boolean showLocationEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_LOCATION_EMPLOYEE_LIST);
                boolean showSupervisedEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_SUPERVISED_EMPLOYEE_LIST);
                boolean showProjectEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_PROJECT_EMPLOYEE_LIST);

                List<Integer> employeeIDs = null;
                if (showProjectEmployees) {
                    employeeIDs = projectManager.getPMManagedProjectsEmployeeIDs(edsUser.getObjectID());
                }

                Integer locationID = edsUser.getLocation() != null ? edsUser.getLocation().getObjectID() : null;
                Integer employeeID = edsUser.getObjectID();

                boolean hasOneOfPermission = showTeamEmployees || showLocationEmployees || showSupervisedEmployees || showProjectEmployees;

                solrQuery.append(" AND (").append(SolrLeaveRequestConst.FIELD_APPROVER_ID).append(":").append(employeeID);
                if (hasOneOfPermission) {
                    solrQuery.append(" OR ").append(SolrLeaveRequestConst.FIELD_EMPLOYEE_ID).append(":").append(employeeID);
                    if (showTeamEmployees) {
                        List<EdsDepartment> edsDepartments = departmentManager.getTeamsByEmployeeId(edsUser.getObjectID());
                        if (edsDepartments != null && edsDepartments.size() > 0) {
                            List<Integer> departmentIDs = edsDepartments.stream().map(EdsDepartment::getObjectID).collect(Collectors.toList());
                            solrQuery.append(" OR ").append(SolrLeaveRequestConst.FIELD_DEPARTMENT_ID).append(":(").append(ServerUtils.getAsCommoDelimited(departmentIDs, "0", " ")).append(")");
                        }
                    }
                    if (showLocationEmployees) {
                        solrQuery.append(" OR ").append(SolrLeaveRequestConst.FIELD_LOCATION_ID).append(":").append(locationID);
                    }
                    if (showSupervisedEmployees) {
                        solrQuery.append(" OR ").append(SolrLeaveRequestConst.FIELD_SUPERVISOR_ID).append(":").append(employeeID);
                    }
                    if (showProjectEmployees && employeeIDs != null) {
                        solrQuery.append(" OR ").append(SolrLeaveRequestConst.FIELD_EMPLOYEE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(employeeIDs, "0", " ")).append(")");
                    }
                } else {
                    solrQuery.append(" OR ").append(SolrLeaveRequestConst.FIELD_EMPLOYEE_ID).append(":").append(employeeID).append(" ");
                }
                solrQuery.append(")");
            }
        }

        if (fp.getEmployeeId() != null) {
            solrQuery.append(" AND ").append(SolrLeaveRequestConst.FIELD_EMPLOYEE_ID).append(":").append(fp.getEmployeeId());
        }
        if (fp.getName() != null) {
            solrQuery.append(" AND ").append(SolrLeaveRequestConst.FIELD_EMPLOYEE_NAME).append(":").append(fp.getEmployeeId());
        }
        if (fp.getObjectId() != null) {
            solrQuery.append(" AND -").append(SolrLeaveRequestConst.FIELD_OBJECT_ID).append(":").append(fp.getObjectId());
        }
        if (fp.getReasonCode() != null) {
            solrQuery.append(" AND ").append(SolrLeaveRequestConst.FIELD_REASON_CODE).append(":").append(fp.getReasonCode());
        }
        if (fp.getReasonID() != null) {
            solrQuery.append(" AND ").append(SolrLeaveRequestConst.FIELD_REASON_ID).append(":").append(fp.getReasonID());
        }
        if (fp.getStatusCode() != null) {
            solrQuery.append(" AND ").append(SolrLeaveRequestConst.FIELD_STATUS_CODE).append(":").append(fp.getStatusCode());
        }
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            solrQuery.append(" AND (");
            solrQuery.append(SolrLeaveRequestConst.FIELD_COMPOSITE).append(":").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey())).append(")");
        }

        if (startDateStr != null && endDateStr != null) {
            solrQuery.append(" AND ((").append(SolrLeaveRequestConst.FIELD_START_DATE).append(":[ * TO ").append(endDateStr).append(" ]) AND ");
            solrQuery.append(" (").append(SolrLeaveRequestConst.FIELD_END_DATE).append(":[ ").append(startDateStr).append(" TO * ]))");
        } else if (fp.getStartDate() != null && fp.getEndDate() != null) {
            solrQuery.append(" AND ((").append(SolrLeaveRequestConst.FIELD_START_DATE).append(":[ * TO ").append(dateFormat.format(fp.getEndDate())).append(" ]) AND ");
            solrQuery.append(" (").append(SolrLeaveRequestConst.FIELD_END_DATE).append(":[ ").append(dateFormat.format(fp.getStartDate())).append(" TO * ]))");
        } else if (fp.getDate() != null) {
            Date startDate = ServerUtils.getEndDate(fp.getDate());
            Date endDate = ServerUtils.getStartDate(fp.getDate());
            solrQuery.append(" AND (( ").append(SolrLeaveRequestConst.FIELD_START_DATE).append(":[ * TO ").append(dateFormat.format(startDate)).append(" ]) AND");
            solrQuery.append("  (").append(SolrLeaveRequestConst.FIELD_END_DATE).append(":[ ").append(dateFormat.format(endDate)).append(" TO * ").append(" ]) )");
        } else if (endDateStr != null) {
            solrQuery.append(" AND (").append(SolrLeaveRequestConst.FIELD_START_DATE).append(":[ * TO ").append(endDateStr).append(" ]) ");
        }
        return solrQuery.toString();
    }

    private boolean hasOpeningBalance(EdsHRSettings settings, int year) {
        EdsHRSettings hrSettings = settings == null ? hrSettingsManager.findOne() : settings;

        if (hrSettings != null && hrSettings.getOpeningBalanceDate() != null) {
            Calendar c2 = Calendar.getInstance();
            c2.setTime(hrSettings.getOpeningBalanceDate());

            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.YEAR, year);
            boolean afterOpening = c1.getTime().getTime() > c2.getTime().getTime();

            return afterOpening && c2.get(Calendar.YEAR) == year;
        }
        return false;
    }

    @Override
    public ListResult<LeaveBalanceReport> getAnnnualLeaveBalanceReportProrataBased(ListingFilterParameter fp) {
        EdsLeaveReason reason = leaveReasonManager.findByCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
        return reason.hasProrata() ? annualLeaveAllowanceManager.getAnnnualLeaveBalanceReportProrataBased(fp) : new ListResult<>();
    }


    @Override
    public ListResult<LeaveBalanceReport> getAnnnualLeaveBalanceReport(ListingFilterParameter fp) {
        EdsLeaveReason reason = leaveReasonManager.findByCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
        ArrayList<LeaveBalanceReport> reportList = new ArrayList<>();
        Integer count = 0;
        if (reason.hasProrata()) { //prorata based bo'lmasa annual leave report ishlamidi ask Munir aka
            reportList = annualLeaveAllowanceManager.getAnnnualLeaveBalanceReport(fp);
            count = annualLeaveAllowanceManager.getLeaveBalanceCount(fp);
        }
        return new ListResult<>(reportList, count);
    }

    public TestRPC saveLeaveBalanceReportData(LeaveBalanceReport leaveBalanceReport) {
        TestRPC result = new TestRPC();

        if (leaveBalanceReport != null && leaveBalanceReport.getEmployeeID() != null) {
            if (leaveBalanceReport.getOpeningBalance() != null) {
                EdsEmployee employee = employeeManager.get(leaveBalanceReport.getEmployeeID());
                if (employee != null) {
                    employee.setOpeningBalanceDays(leaveBalanceReport.getOpeningBalance());
                    employeeManager.update(employee);
                }
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<LeaveRequestLisItem> getLeaveRequestList(ListingFilterParameter fp) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSickRequest.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(LOGGER, kpiLog, "Get leave request list");
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        FacetFilterRpc facetFilter = fp.getFacetFilter();
        if (facetFilter != null && !facetFilter.isFilterChanges()) {
            facetFilter = commonServiceLocal.getUserFacetFilter(facetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(getLeaveRequestSolrQuery(fp, edsUser));

        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(facetFilter, edsUser.getCompany(), null, null));
        return getLeaveRequestListResponse(fp, solrQuery.toString(), edsUser);
    }

    private ListResult<LeaveRequestLisItem> getLeaveRequestListResponse(ListingFilterParameter filterParameter, String solrQuery, EdsUser edsUser) {
        Page<LeaveRequestSolrDoc> leaveRequestSolrDocPage = leaveRequestSolrComponent.getList(filterParameter, solrQuery);
        return getLeaveRequestFromSolrResult(leaveRequestSolrDocPage, edsUser, filterParameter);
    }


    private ListResult<LeaveRequestLisItem> getLeaveRequestFromSolrResult(Page<LeaveRequestSolrDoc> leaveRequestSolrDocPage, EdsUser edsUser, ListingFilterParameter filterParameter) {
        ArrayList<LeaveRequestLisItem> itemList = new ArrayList<>();
        int totalNumber = 0;
        List<LeaveRequestSolrDoc> resultList = leaveRequestSolrDocPage != null ? leaveRequestSolrDocPage.getContent() : null;
        if (resultList == null) {
            return new ListResult<>(itemList, totalNumber);
        }
        String idList = resultList.stream()
                .map(x -> String.valueOf(x.getObjectId()))
                .collect(Collectors.joining(","));

        ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();

        Map<Integer, Double[]> durationMap = sickRequestDurationManager.getLeaveRequestsDurationByIds(idList);

        totalNumber = (int) leaveRequestSolrDocPage.getTotalElements();
        for (LeaveRequestSolrDoc relevantDoc : resultList) {
            LeaveRequestLisItem item = new LeaveRequestLisItem();
            Integer id = relevantDoc.getObjectId();

            Double[] dd = durationMap != null ? durationMap.get(id) : null;

            item.setObjectId(id);
            item.setEmployeeName(relevantDoc.getEmployeeName());
            item.setEmployeeId(relevantDoc.getEmployeeId());
            item.setLeaveRequestCode(relevantDoc.getNumberData());
            item.setApproverName(relevantDoc.getApproverName());
            item.setApproverId(relevantDoc.getApproverId());
            if (edsUser.getObjectID().equals(item.getApproverId())) {
                item.setUserIsCurrentApprover(true);
            }
            item.setStatus(commonLocalizer.localize(relevantDoc.getStatusName() != null ? relevantDoc.getStatusName().toLowerCase() : "", relevantDoc.getStatusName(), ServerUtils.getUserLocale()));
            item.setStatusCode(relevantDoc.getStatusCode());
            if (relevantDoc.getReasonId() != null) {
                EdsLeaveReason reason = leaveReasonManager.get(relevantDoc.getReasonId());
                if (reason != null) {
                    item.setReasonCode(reason.getCode());
                }
            }
            item.setReason(commonLocalizer.localize(relevantDoc.getReasonName() != null ? relevantDoc.getReasonName().toLowerCase() : "", relevantDoc.getReasonName(), ServerUtils.getUserLocale()));
            item.setDescription(relevantDoc.getDescription());

            item.setAllDay(dd != null && dd[1] != null && (dd[1] % 1) == 0 && dd[1] != 0d);
            item.setPaid(ServerUtils.getLeaveDayFormat(dd, paid));
            item.setNonPaid(ServerUtils.getLeaveDayFormat(dd, non_paid));
            item.setType(commonLocalizer.localize(relevantDoc.getTypeName(), relevantDoc.getTypeName()));

            String paidDays = ServerUtils.getLeaveDayFormat(dd, paid);
            String nonPaid = ServerUtils.getLeaveDayFormat(dd, non_paid);
            String leaveDays = "";
            if (StringUtils.isNotBlank(paidDays)) {
                leaveDays = paidDays;
                item.setType(commonLocalizer.localize("paid", "Paid"));
            }
            if (StringUtils.isNotBlank(nonPaid)) {
                if (StringUtils.isNotBlank(leaveDays)) {
                    leaveDays = leaveDays + "/" + nonPaid;
                    item.setType(commonLocalizer.localize("paidNonPaid", "Paid/Non-Paid"));
                } else {
                    leaveDays = nonPaid;
                    item.setType(commonLocalizer.localize("nonPaid", "Non-Paid"));
                }
            }
            item.setLeaveDays(leaveDays);

            Date startDate = relevantDoc.getStartDate();
            item.setStartDate(startDate != null ? new DateNonConvertable(startDate) : null);
            Date endDate = relevantDoc.getEndDate();
            item.setEndDate(endDate != null ? new DateNonConvertable(endDate) : null);
            item.setCreatedDate(relevantDoc.getCreatedDate());
            item.setCreator(relevantDoc.getCreatorName());

            item.setPositionId(relevantDoc.getPositionId());
            EdsPosition position = positionManager.get(item.getPositionId());
            item.setPosition(position != null && !Boolean.TRUE.equals(position.isDeleted()) ? position.getName() : null);

            item.setDepartmentId(relevantDoc.getDepartmentId());
            EdsDepartment department = departmentManager.get(item.getDepartmentId());
            item.setDepartment(department != null ? department.getName() : null);

            if (panelSettings != null) {
                item.setCustomFields(CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, panelSettings.getColumnCodeName()));
            }
            itemList.add(item);
        }

        return new ListResult<>(itemList, totalNumber);
    }


    private static class AttendanceValue {
        private Integer timeSlot = 0;
        private Integer leaveApproved = 0;
        private Integer leavePending = 0;
        private Integer leaveDenied = 0;
        private Integer fromAnnualLeaveTime = 0;
        private Integer paidTime = 0;
        private final Boolean setTimeSlot;

        private AttendanceValue(Integer timeSlot, Integer leaveApproved, Integer leavePending, Integer leaveDenied, Integer fromAnnualLeaveTime, Integer paidTime, Boolean setTimeSlot) {
            this.timeSlot = getNumber(timeSlot);
            this.leaveApproved = getNumber(leaveApproved);
            this.leavePending = getNumber(leavePending);
            this.leaveDenied = getNumber(leaveDenied);
            this.fromAnnualLeaveTime = getNumber(fromAnnualLeaveTime);
            this.paidTime = getNumber(paidTime);
            this.setTimeSlot = setTimeSlot;
        }

        private Integer getNumber(Integer number) {
            if (number == null) {
                return 0;
            }
            if (number < 0) {
                return 0;
            }
            return number;
        }

        public Integer getTimeSlot() {
            return timeSlot;
        }

        Integer getLeaveApproved() {
            return leaveApproved;
        }

        Integer getLeavePending() {
            return leavePending;
        }

        Integer getLeaveDenied() {
            return leaveDenied;
        }

        Integer getFromAnnualLeaveTime() {
            return fromAnnualLeaveTime;
        }

        Integer getPaidTime() {
            return paidTime;
        }

        public Boolean getSetTimeSlot() {
            return setTimeSlot;
        }

        public void add(AttendanceValue attendanceValue) {
            if (this.timeSlot.equals(0) && !this.setTimeSlot) {
                this.timeSlot = getNumber(attendanceValue.getTimeSlot());
            }
            this.leaveApproved += attendanceValue.getLeaveApproved();
            this.leaveDenied += attendanceValue.getLeaveDenied();
            this.leavePending += attendanceValue.getLeavePending();
            this.fromAnnualLeaveTime += attendanceValue.getFromAnnualLeaveTime();
            this.paidTime += attendanceValue.getPaidTime();
        }

    }

    private SolrQuery getLeaveSolrQuery(ListingFilterParameter filterParameter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setParam(CommonParams.ROWS, filterParameter.getLimit() > 0 ? String.valueOf(filterParameter.getLimit()) : "1000");
        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                boolean desc = !filterParameter.isAscending();
                if (LeaveRequestLisItem.EMPLOYEE_NAME.equals(filterParameter.getSortField())) {
                    query.setSort(SolrLeaveRequestConst.FIELD_SORTABLE_EMPLOYEE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (LeaveRequestLisItem.REASON.equals(filterParameter.getSortField())) {
                    query.setSort(SolrLeaveRequestConst.FIELD_SORTABLE_REASON_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (LeaveRequestLisItem.CODE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrLeaveRequestConst.FIELD_SORTABLE_NUMBER_DATA, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (LeaveRequestLisItem.CREATED_DATE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrLeaveRequestConst.FIELD_CREATED_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (LeaveRequestLisItem.FROM_DATE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrLeaveRequestConst.FIELD_START_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (LeaveRequestLisItem.TO_DATE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrLeaveRequestConst.FIELD_END_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (LeaveRequestLisItem.STATUS.equals(filterParameter.getSortField())) {
                    query.setSort(SolrLeaveRequestConst.FIELD_STATUS_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (LeaveRequestLisItem.APPROVER.equals(filterParameter.getSortField())) {
                    query.setSort(SolrLeaveRequestConst.FIELD_APPROVER_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (LeaveRequestLisItem.POSITION.equals(filterParameter.getSortField())) {
                    query.setSort(SolrLeaveRequestConst.SORTABLE_POSITION_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (LeaveRequestLisItem.DEPARTMENT.equals(filterParameter.getSortField())) {
                    query.setSort(SolrLeaveRequestConst.SORTABLE_DEPARTMENT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                }
            } else {
                query.setSort(SolrLeaveRequestConst.FIELD_CREATED_DATE, SolrQuery.ORDER.desc);
            }
        } else {
            query.setSort(SolrLeaveRequestConst.FIELD_CREATED_DATE, SolrQuery.ORDER.desc);
        }
        return query;
    }

    public void indexLeaveRequests(SolrReindexRpc rpc) {
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // do not chane this limit

        List<EdsSickRequest> list = sickRequestManager.getLeaveRequestListForSolr(rpc, startat, limit);
        while (!list.isEmpty()) {
            try {
                leaveRequestSolrComponent.indexConcurrently(list);
            } catch (InterruptedException e) {
                LOGGER.error("Error Leave Request Index. Company ID : {} , Message : {} ", rpc.getCompanyId(), e.getMessage());
            }
            sickRequestManager.flushAndClear();
            startat++;
            list = sickRequestManager.getLeaveRequestListForSolr(rpc, (startat * limit), limit);
        }
        sickRequestManager.flushAndClear();
    }

    public Date getEndDateForLeaveRequest(ListingFilterParameter fp) {
        return getEndDate(fp.getEmployeeId(), fp.getStartDate(), Double.valueOf(fp.getLimit()), null);
    }

    @Override
    public Date getEndDate(Integer employeeID, Date startDate, Double overAllLeaveDays, String reasonCode) {
        EdsTimeSlot timeSlot = employeeManager.getEmployeeTimeSlot(employeeID);
        EdsLeaveReason reason = null;
        ArrayList<Integer> countAsWorkingDays = null;
        boolean includeDayOff = false;
        if (reasonCode != null) {
            reason = leaveReasonManager.getReasonByName(null, reasonCode);
        }
        if (reason != null && timeSlot != null && timeSlot.getSelectedLeaveReasons() != null && timeSlot.getSelectedLeaveReasons().contains(reason) && timeSlot.getAdditionalLeaveDays() != null) {
            countAsWorkingDays = timeSlot.getAdditionalLeaveDays();
            includeDayOff = reason.getIncludeDayOffs();
        }

        Calendar calendar = Calendar.getInstance();
        Date dueDate = startDate;
        int i = 0;
        while (i < overAllLeaveDays) {
            Object[] dayStatement = attendanceRawDataManager.getWorkingDate(employeeID, dueDate);
            Boolean holiday = (Boolean) dayStatement[0];
            Boolean dayOff = (Boolean) dayStatement[1];
            Integer weekDay = (Integer) dayStatement[2];
            Boolean holidayFromAnnualLeave = (Boolean) dayStatement[3];
                if ((holiday && !holidayFromAnnualLeave) || (!includeDayOff && dayOff.equals(true) && !holiday) || (countAsWorkingDays != null && !countAsWorkingDays.contains(weekDay))) {
                    calendar.setTime(dueDate);
                    calendar.add(Calendar.DATE, 1);
                    dueDate = new Date(calendar.getTimeInMillis());
                } else {
                i++;
                calendar.setTime(dueDate);
                if (i != overAllLeaveDays) {
                    calendar.add(Calendar.DATE, 1);
                }
                dueDate = new Date(calendar.getTimeInMillis());
            }
        }
        return dueDate;
    }

    public ArrayList<MultiLeaveDTO> setStartDueDates(ArrayList<MultiLeaveDTO> list, Date startDate, Integer employeeID, String reasonCode) {
        EdsEmployee employee = employeeManager.get(employeeID);
        Set<EdsTimeSlotItem> timeSlotItems = employee.getTimeSlot().getItems();
        ArrayList<MultiLeaveDTO> dtoList = new ArrayList<>();
        Date fromDate = setStartTime(employeeID, startDate);
        Date dueDate = null;
        for (MultiLeaveDTO dto : list) {
            MultiLeaveDTO leaveDTO = new MultiLeaveDTO();
            if (dto.getSickRequestType().equals(Constants.DAY)) {
                dueDate = setEndTime(timeSlotItems, employeeID, fromDate, dto, reasonCode);

                leaveDTO.setSickRequestStartDate(fromDate);
                leaveDTO.setSickRequestEndDate(dueDate);
                fromDate = setStartTime(employeeID, icrementDay(dueDate));
            }

            leaveDTO.setPeriodId(dto.getPeriodId());
            leaveDTO.setSickRequestType(dto.getSickRequestType());
            leaveDTO.setSickRequestDuration(dto.getSickRequestDuration());
            leaveDTO.setSickRequestLeftDays(dto.getSickRequestLeftDays());
            leaveDTO.setMinLeaveDays(dto.getMinLeaveDays());
            leaveDTO.setLaborPeriod(dto.getLaborPeriod());
            dtoList.add(leaveDTO);
        }
        return dtoList;
    }

    public Date icrementDay(Date oldDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public Date setStartTime(Integer employeeID, Date startDate) {
        EdsEmployee employee = employeeManager.get(employeeID);
        Set<EdsTimeSlotItem> timeSlotItems = employee.getTimeSlot().getItems();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        timeSlotItems.forEach(item -> {
            if (dayOfWeek == item.getDay()) {
                calendar.set(Calendar.HOUR_OF_DAY, item.getStartTime() / 60);
                calendar.set(Calendar.MINUTE, item.getStartTime() % 60);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 0);
            }
        });
        return calendar.getTime();
    }

    public Date setEndTime(Integer employeeID, Date date) {
        EdsEmployee employee = employeeManager.get(employeeID);
        Set<EdsTimeSlotItem> timeSlotItems = employee.getTimeSlot().getItems();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        timeSlotItems.forEach(item -> {
            if (dayOfWeek == item.getDay()) {
                calendar.set(Calendar.HOUR_OF_DAY, item.getEndTime() / 60);
                calendar.set(Calendar.MINUTE, item.getEndTime() % 60);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, 18);
                calendar.set(Calendar.MINUTE, 0);
            }
        });
        return calendar.getTime();
    }

    public Date setEndTime(Set<EdsTimeSlotItem> timeSlotItems, Integer employeeID, Date fromDate, MultiLeaveDTO dto, String reasonCode) {
        Calendar dueDateCalendar = Calendar.getInstance();
        dueDateCalendar.setTime(getEndDate(employeeID, fromDate, (double) dto.getSickRequestDuration(), reasonCode));

        int weekDay = dueDateCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        boolean hasTimeSlotOnThisDay = false;
        for (EdsTimeSlotItem item : timeSlotItems) {
            if (weekDay == item.getDay()) {
                dueDateCalendar.set(Calendar.HOUR_OF_DAY, item.getEndTime() != 0 ? item.getEndTime() / 60 : 18);
                dueDateCalendar.set(Calendar.MINUTE, item.getEndTime() != 0 ? item.getEndTime() % 60 : 0);
                hasTimeSlotOnThisDay = true;
            }
        }
        if (!hasTimeSlotOnThisDay) {
            dueDateCalendar.set(Calendar.HOUR_OF_DAY, 18);
            dueDateCalendar.set(Calendar.MINUTE, 0);
        }
        return dueDateCalendar.getTime();
    }

    public Date setEndTime(Integer employeeID, Date fromDate, Double overAllLeaveDays, String reasonCode) {
        EdsEmployee employee = employeeManager.get(employeeID);
        Set<EdsTimeSlotItem> timeSlotItems = employee.getTimeSlot().getItems();

        Calendar dueDateCalendar = Calendar.getInstance();
        dueDateCalendar.setTime(getEndDate(employeeID, fromDate, overAllLeaveDays, reasonCode));

        int weekDay = dueDateCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        boolean hasTimeSlotOnThisDay = false;
        for (EdsTimeSlotItem item : timeSlotItems) {
            if (weekDay == item.getDay()) {
                dueDateCalendar.set(Calendar.HOUR_OF_DAY, item.getEndTime() != 0 ? item.getEndTime() / 60 : 18);
                dueDateCalendar.set(Calendar.MINUTE, item.getEndTime() != 0 ? item.getEndTime() % 60 : 0);
                hasTimeSlotOnThisDay = true;
            }
        }
        if (!hasTimeSlotOnThisDay) {
            dueDateCalendar.set(Calendar.HOUR_OF_DAY, 18);
            dueDateCalendar.set(Calendar.MINUTE, 0);
        }
        return dueDateCalendar.getTime();
    }

    @Override
    @Transactional
    public void restoreLeave(Integer employeeID, Integer sickID, DateNonConvertable startNonConverted, DateNonConvertable endNonConverted, String reasonCode) {
        ListingFilterParameter fp = new ListingFilterParameter();
        Date startDate = setStartTime(employeeID, startNonConverted.getNonConvertedDate());
        fp.setAllDay(true);
        fp.setEmployeeId(employeeID);
        fp.setIncludeDayOff(false);
        fp.setReasonCode(reasonCode);
        EdsSickRequest sickRequest;
        Double daysToRestore = getLeaveDays(fp, new DateNonConvertable(startDate), endNonConverted);
        boolean hasDaysToRestore = true;
        List<EdsLabourPeriod> list = labourPeriodManager.sickRequestPeriods(sickID, true);
        for (EdsLabourPeriod period : list) {
            List<EdsMultiLeave> childList = multiLeaveManager.getMultiLeaveListBySickForPeriodID(period.getObjectID());
            if (childList != null && !childList.isEmpty()) {
                for (EdsMultiLeave child : childList) {
                    if (daysToRestore <= 0) {
                        break;
                    }
                    if (!child.getSickRequestType().equals(Constants.MONEY)) {
                        Double totalDaysFromChild = child.getSickRequestDuration();
                        if (totalDaysFromChild > daysToRestore) {
                            EdsMultiLeave leave = multiLeaveManager.get(child.getObjectID());
                            leave.setSickRequestDuration(totalDaysFromChild - daysToRestore);
                            EdsSickRequest childSickRequest = sickRequestManager.get(child.getChildSickRequest().getObjectID());
                            childSickRequest.setRecallDate(setEndTime(employeeID, childSickRequest.getStartDate(), totalDaysFromChild - daysToRestore, reasonCode));
                            multiLeaveManager.createOrUpdate(leave);
                            sickRequestManager.createOrUpdate(childSickRequest);
                            hasDaysToRestore = false;
                            break;
                        } else {
                            daysToRestore = daysToRestore - totalDaysFromChild;
                            EdsMultiLeave leave = multiLeaveManager.get(child.getObjectID());
                            leave.setSickRequestDuration(0d);
                            EdsSickRequest childSickRequest = sickRequestManager.get(child.getChildSickRequest().getObjectID());
                            childSickRequest.setRecallDate(childSickRequest.getStartDate());
                            multiLeaveManager.createOrUpdate(leave);
                            sickRequestManager.createOrUpdate(childSickRequest);
                            hasDaysToRestore = true;
                        }
                    }
                }
                if (!hasDaysToRestore) {
                    break;
                }
            }

        }
        sickRequest = sickRequestManager.get(sickID);
        sickRequest.setRecallDate(ServerUtils.addDays(startNonConverted.getNonConvertedDate(), -1));

        List<EdsSickRequest> childRequests = sickRequestManager.getLeaveRequestByParentId(sickID);

        StringBuilder sickRequestIds = new StringBuilder();
        sickRequestIds.append(sickID);
        childRequests.forEach(child -> sickRequestIds.append(", ").append(child.getObjectID()));

        sickRequestDurationManager.restoreDuration(sickRequestIds.toString(), startNonConverted.getNonConvertedDate(), endNonConverted.getNonConvertedDate());
        attendanceRawDataManager.restoreAttendanceRawData(employeeID, startNonConverted.getNonConvertedDate(), endNonConverted.getNonConvertedDate());

        HashMap<Integer, Integer> removedBackUpEmployeeMap = new HashMap<>();
        Date recallDate = sickRequest.getRecallDate();
        List<EdsBackupEmployee> backupEmployees = backupEmployeeManager.getBackupEmployeesBySickRequestId(sickRequest.getObjectID());
        if (!CollectionUtils.isEmpty(backupEmployees)) {
            for (EdsBackupEmployee backupEmployee : backupEmployees) {
                if (backupEmployee.getStartDate() == null || backupEmployee.getStartDate().after(recallDate)) {
                    if (backupEmployee.getEmployee() != null) {
                        Integer employeeId = backupEmployee.getEmployee().getObjectID();
                        if (removedBackUpEmployeeMap.get(employeeId) == null) {
                            removedBackUpEmployeeMap.put(employeeId, 1);
                        } else {
                            removedBackUpEmployeeMap.replace(employeeId, removedBackUpEmployeeMap.get(employeeId) + 1);
                        }
                    }
                    backupEmployeeManager.delete(backupEmployee);
                }
            }
        }

        if (!CollectionUtils.isEmpty(sickRequest.getApprovers())) {
            EdsReference submittedStatus = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.NOT_DEFINED);
            List<EdsApprover> oldApprovers = new ArrayList<>();
            if (!CollectionUtils.isEmpty(sickRequest.getApprovers())) {
                oldApprovers.addAll(sickRequest.getApprovers());
            }
            sickRequest.setCurrentApprover(null);
            sickRequest.setPrevApprover(null);
            for (EdsApprover edsApprover : sickRequest.getApprovers()) {
                approverManager.delete(edsApprover);
            }
            sickRequest.setApprovers(null);
            sickRequestManager.update(sickRequest);

            Integer index = 0;
            for (EdsApprover oldApprover : oldApprovers) {
                Integer employeeId = oldApprover.getExactEmployee().getEmployee().getObjectID();
                if (oldApprover.getBackup() != null && oldApprover.getBackup() && removedBackUpEmployeeMap != null && removedBackUpEmployeeMap.get(employeeId) != null && removedBackUpEmployeeMap.get(employeeId) > 0) {
                    Integer countApprover = removedBackUpEmployeeMap.get(employeeId) - 1;
                    if (countApprover == 0) {
                        removedBackUpEmployeeMap.remove(employeeId);
                    } else {
                        removedBackUpEmployeeMap.replace(employeeId, countApprover);
                    }
                } else {
                    EdsApprover edsApprover = oldApprover.cloneShallow();
                    edsApprover.setApproverRoles(new HashSet<>());
                    edsApprover.setApproverEmployees(new HashSet<>());
                    edsApprover.setDynamicQueries(new HashSet<>());
                    edsApprover.setObjectID(null);
                    edsApprover.setApproverHistory(new HashSet<>());
                    edsApprover.setEntityID(sickRequest.getObjectID());
                    edsApprover.setIs_default(false);
                    edsApprover.setApproverOrder(index);
                    edsApprover.setStatus(submittedStatus);
                    if (index == 0) {
                        sickRequest.setCurrentApprover(edsApprover);
                        sickRequest.setEntityStatus(submittedStatus);
                    }
                    approverManager.createOrUpdate(edsApprover);
                    sickRequest.getApprovers().add(edsApprover);

                    index++;
                }
            }
        }

        sickRequestManager.createOrUpdate(sickRequest);
        try {
            leaveRequestSolrComponent.index(sickRequest);
        } catch (InterruptedException | SolrServerException | IOException e) {
            e.printStackTrace();
        }

        try {
            NewLeaveRequest leaveRequest = new NewLeaveRequest();
            leaveRequest.setEmployee(sickRequest.getEmployee().getObjectID());
            leaveRequest.setDescription("RECALL");
            leaveRequest.setStartNonConverable(startNonConverted);
            leaveRequest.setEndNonConverable(endNonConverted);
            EdsLeaveReason reason = leaveReasonManager.findByCode("LR_TYPE_RECALL_LEAVE");
            if (reason != null) {
                leaveRequest.setReasonId(reason.getObjectID());
            }
            leaveRequest.setType("ST_PAID");
            NumberData numberData = generateLeaveRequestNumber();
            leaveRequest.setNumberData(numberData);
            leaveRequest.setLeaveRequestCode(numberData.getNumberString());
            leaveRequest.setTakeByMoney(false);
            leaveRequest.setFrom(LayoutRPC.LEAVE_REQUEST_FORM);
            leaveRequest.setStatusCode(Constants.DRAFT);
            leaveRequest.setRecalculate(false);

            EdsUser currentUser = employeeManager.getUser();
            final List<EdsApprover> settingApprovers = approverManager.list(RelationItem.TYPE_LEAVE_REQUEST, null);
            ArrayList<ApproverItemMini> approvers = new ArrayList<>();
            ApproverItemMini appr = new ApproverItem();
            appr.setClonedFrom(settingApprovers.get(0).getObjectID());
            appr.setApproverOrder(1);
            appr.setExactEmployee(new SelectItem(currentUser.getObjectID(), currentUser.getName()));
            approvers.add(appr);
            leaveRequest.setApprovers(approvers);
            createLeaveRequest(leaveRequest);
        } catch (Exception e) {

        }

        /* add workflow event to start workflow rule... */
        EdsBusinessEvent workflowEvent2 = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), sickRequest, sickRequest.getEmployee());
        workflowEvent2.setEntityType(RelationItem.TYPE_LEAVE_REQUEST);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSickRequest.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(sickRequest.getObjectID());
        ServerUtils.kpiLog(LOGGER, kpiLog, "Recall employee from leave");
    }

    @Override
    public void saveBackupEmployeesFromSummary(Integer leaveRequestId, ArrayList<BackupEmployeeItem> backupEmployeeItemList) {
        if (leaveRequestId == null || CollectionUtils.isEmpty(backupEmployeeItemList)) {
            return;
        }

        EdsSickRequest sickRequest = sickRequestManager.get(leaveRequestId);
        EdsReference submittedStatus = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.NOT_DEFINED);

        List<EdsApprover> oldApprovers = extractOldApprovers(sickRequest);
        EdsApprover template = findTemplate(sickRequest);

        deleteExistingApprovers(sickRequest);
        List<ApproverItemMini> approvers = generateApproversList(backupEmployeeItemList);

        createNewApprovers(sickRequest, approvers, oldApprovers, template, submittedStatus);

        sickRequest.setEntityStatus(submittedStatus);
        sickRequestManager.update(sickRequest);

        backupEmployeeManager.deleteBySickRequestId(leaveRequestId);
        if (isOk(backupEmployeeItemList)) {
            saveBackupEmployees(sickRequest, backupEmployeeItemList);
        }

        addWorkflowEvent(sickRequest);

        addToIndex(sickRequest);
    }

    private List<EdsApprover> extractOldApprovers(EdsSickRequest sickRequest) {
        List<EdsApprover> oldApprovers = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sickRequest.getApprovers())) {
            oldApprovers.addAll(sickRequest.getApprovers());
        }
        return oldApprovers;
    }

    private EdsApprover findTemplate(EdsSickRequest sickRequest) {
        boolean haveTemp = false;
        EdsApprover template = null;
        if (!CollectionUtils.isEmpty(sickRequest.getApprovers())) {
            for (EdsApprover edsApprover : sickRequest.getApprovers()) {
                if (!haveTemp && edsApprover.getBackup()) {
                    template = edsApprover;
                    haveTemp = true;
                }
            }
            if (template == null) {
                template = sickRequest.getApprovers().get(0);
            }
        }
        return template;
    }

    private void deleteExistingApprovers(EdsSickRequest sickRequest) {
        for (EdsApprover edsApprover : sickRequest.getApprovers()) {
            approverManager.delete(edsApprover);
        }
        sickRequest.setApprovers(null);
        sickRequest.setCurrentApprover(null);
        sickRequest.setPrevApprover(null);
    }

    private List<ApproverItemMini> generateApproversList(ArrayList<BackupEmployeeItem> backupEmployeeItemList) {
        List<ApproverItemMini> approvers = new ArrayList<>();
        int index = 1;
        for (BackupEmployeeItem backupEmployeeItem : backupEmployeeItemList) {
            ApproverItemMini parentBackupEmployee = backupEmployeeItem.getParentBackupEmployee();
            parentBackupEmployee.setBackup(true);
            parentBackupEmployee.setApproverOrder(index);
            approvers.add(parentBackupEmployee);
            index++;
            if (!CollectionUtils.isEmpty(backupEmployeeItem.getChildList())) {
                for (ApproverItemMini childBackupEmployeeItem : backupEmployeeItem.getChildList()) {
                    childBackupEmployeeItem.setBackup(true);
                    childBackupEmployeeItem.setApproverOrder(index);
                    approvers.add(childBackupEmployeeItem);
                    index++;
                }
            }
        }
        return approvers;
    }

    private void createNewApprovers(EdsSickRequest sickRequest, List<ApproverItemMini> approvers, List<EdsApprover> oldApprovers, EdsApprover template, EdsReference submittedStatus) {
        boolean isFirstApprover = true;
        int index = 1;
        for (ApproverItemMini approverItem : approvers) {
            EdsApprover edsApprover = template.cloneShallow();
            edsApprover.setOnApprovedAction(1);
            edsApprover.setObjectID(null);
            edsApprover.setBackup(approverItem.isBackup());
            edsApprover.setApproverHistory(new HashSet<>());
            edsApprover.setEntityID(sickRequest.getObjectID());
            edsApprover.setIs_default(false);
            edsApprover.setApproverOrder(approverItem.getApproverOrder());

            if (isFirstApprover) {
                edsApprover.setStatus(submittedStatus);
                sickRequest.setEntityStatus(submittedStatus);
                isFirstApprover = false;
            } else {
                edsApprover.setStatus(submittedStatus);
            }
            if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                edsApprover.setExactEmployee(user_);
            }
            edsApprover.setApproverRoles(new HashSet<>());
            edsApprover.setApproverEmployees(new HashSet<>());
            edsApprover.setDynamicQueries(new HashSet<>());
            approverManager.createOrUpdate(edsApprover);

            for (EdsApproverRoles roleapp : template.getApproverRoles()) {
                edsApprover.getApproverRoles().add(roleapp);
            }

            for (final EdsApproverEmployees ucerapp : template.getApproverEmployees()) {
                edsApprover.getApproverEmployees().add(ucerapp);
            }

            if (sickRequest.getCurrentApprover() == null) {
                sickRequest.setCurrentApprover(edsApprover);
            }
            sickRequest.getApprovers().add(edsApprover);
        }

        for (EdsApprover oldApprover : oldApprovers) {
            if (!oldApprover.getBackup()) {
                EdsApprover edsApprover = oldApprover.cloneShallow();
                edsApprover.setApproverRoles(new HashSet<>());
                edsApprover.setApproverEmployees(new HashSet<>());
                edsApprover.setDynamicQueries(new HashSet<>());
                edsApprover.setObjectID(null);
                edsApprover.setBackup(false);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(sickRequest.getObjectID());
                edsApprover.setIs_default(false);
                edsApprover.setApproverOrder(index);
                edsApprover.setStatus(submittedStatus);
                approverManager.createOrUpdate(edsApprover);

                sickRequest.getApprovers().add(edsApprover);

                index++;
            }
        }
    }

    private void addWorkflowEvent(EdsSickRequest sickRequest) {
        EdsBusinessEvent workflowEvent2 = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), sickRequest, sickRequest.getEmployee());
        workflowEvent2.setEntityType(RelationItem.TYPE_LEAVE_REQUEST);
    }

    @Override
    public boolean saveLREditCellValue(LeaveRequestLisItem rowValue, String columnCodeName) {
        try {
            EdsSickRequest edsSickRequest = sickRequestManager.get(rowValue.getObjectId());
            edsSickRequest.clear();
            EdsSickRequestCustomFields edsSickRequestCustomFields = edsSickRequest.getCustomFields();
            if (edsSickRequestCustomFields == null) {
                edsSickRequestCustomFields = new EdsSickRequestCustomFields();
                sickRequestCFManager.create(edsSickRequestCustomFields);
                edsSickRequest.setCustomFields(edsSickRequestCustomFields);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(edsSickRequestCustomFields, rowValue.getCustomFields(), columnCodeName);

            solrManager.addleaveRequestToIndex(edsSickRequest);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, EVENT_TYPE_EDIT, edsSickRequest, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_LEAVE_REQUEST);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public SelectItem[] getTimeSlotShortNameForLookUp(ListingFilterParameter filterParameter) {
        List<EdsShiftSettings> timeslots = shiftSettingsManager.getShiftSettings(filterParameter);
        ArrayList<SelectItem> timeSlotArray = new ArrayList<>();
        for (int i = 0; i < timeslots.size(); i++) {
            timeSlotArray.add(new SelectItem(timeslots.get(i).getObjectID(), timeslots.get(i).getShortName()));
        }

        return timeSlotArray.toArray(new SelectItem[]{});
    }

    @Override
    public SelectItem[] getBrigadasForLookUp(ListingFilterParameter filterParameter) {
        Integer objectID = userManager.getUser().getObjectID();
        List<EdsBrigada> list = null;
        if (ServerUtils.hasPermission(PermissionConstants.HRMS_BRIGADA_SEE_ALL)) {
            list = brigadaManager.getList(filterParameter, null);
        } else {
            list = brigadaManager.getList(new ListingFilterParameter(), objectID);
        }
        ArrayList<SelectItem> brigadaArray = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            brigadaArray.add(new SelectItem(list.get(i).getObjectID(), list.get(i).getNumber() + " - " + list.get(i).getName()));

        }
        return brigadaArray.toArray(new SelectItem[]{});
    }

    public Integer createGoalHistory(Integer goalId, HistoryListItem hisItem) {
        if (goalId != null && hisItem != null) {
            EdsUser user = userManager.getUser();
            if (user instanceof EdsEmployee) {
                user = userManager.get(user.getObjectID());
            }
            EdsGoalHistory goalHistory = new EdsGoalHistory();
            goalHistory.setGoal(goalManager.get(goalId));
            goalHistory.setCreationDate(new Date());
            goalHistory.setUser(user);
            goalHistory.setSuperUser(ServerUtils.isSuperUser());
            goalHistory.setText(hisItem.getComment());

            goalHistoryManager.create(goalHistory);
            return goalHistory.getObjectID();
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HistoryNote> loadGoalHistory(Integer reportId) {
        List<EdsGoalHistory> historyList = goalHistoryManager.getGoalHistoryList(reportId);
        if (historyList == null) {
            historyList = new ArrayList<>();
        }

        List<HistoryNote> noteItemsList = new ArrayList<>();
        for (EdsGoalHistory item : historyList) {
            if (StringUtils.isNotBlank(item.getText())) {
                HistoryListItem historyListItem = new HistoryListItem();
                historyListItem.setObjectID(item.getObjectID());
                if (item.isSuperUser()) {
                    historyListItem.setEmployee(Constants.defaultSupportName);
                } else {
                    historyListItem.setEmployee(item.getUser().getFullName());
                }
                historyListItem.setEmployeeID(item.getUser().getObjectID());
                if (item.getText().split(":").length > 1 && item.getText().split(":")[0].equals("rejectionReason")) { // For: Rejection Reason
                    historyListItem.setComment(commonLocalizer.localize(PdfLocalizationName.rejectionReason, "Rejection Reason:") + item.getText().split(":")[1]);
                } else {
                    try {
                        historyListItem.setComment(commonLocalizer.localize(item.getText().toLowerCase()));
                    } catch (Exception e) {
                        historyListItem.setComment(item.getText());
                    }
                }
                historyListItem.setEventDate(item.getCreationDate());

                noteItemsList.add(historyListItem);
            }
        }
        return noteItemsList;
    }

    @Override
    public void deleteGoalHistory(Integer goalID) {
        EdsGoalHistory history = goalHistoryManager.get(goalID);
        goalHistoryManager.delete(history);
    }

    @Override
    public ProjectSingleItem getProject(Integer objectId) {
        ProjectSingleItem item = new ProjectSingleItem();
        EdsProject project = projectManager.get(objectId);
        item.setObjectID(project.getObjectID());
        item.setStartDate(project.getStartDate());
        item.setEndDate(project.getEndDate());
        return item;
    }

    @Override
    public ArrayList<LaborPeriodRequest> getEmployeeAdditonalAllowances(Integer employeeID, Integer sickRequestID, Date startDate, Boolean isSummary, Boolean isRecalculate) {
        Double experienceDays = getaExperienceDays(employeeID, startDate);
        ArrayList<LaborPeriodRequest> employeePeriods = getPeriodList(employeeID, sickRequestID, EdsSickRequest.LR_TYPE_ANNUAL_LEAVE, isSummary, isRecalculate);
        for (LaborPeriodRequest periodRequest : employeePeriods) {
            if (new Date().after(periodRequest.getEndDate())) {
                List<EdsSickRequest> labourPeriodLeaves = labourPeriodManager.getSickRequestByPeriods(periodRequest.getObjectID());
                if (periodRequest.getAllowance() - periodRequest.getApprovedTakenDays() > 0 || (labourPeriodLeaves != null && !labourPeriodLeaves.isEmpty())) {
                    periodRequest.setExperienceDays(experienceDays);
                }
            } else {
                periodRequest.setExperienceDays(experienceDays);
            }
        }

        return employeePeriods;
    }

    private Double getaExperienceDays(Integer employeeID, Date startDate) {
        startDate = startDate.before(new Date()) ? new Date() : startDate;
        EdsDynamicQuery dynamicQuery = dynamicQueryManager.getQueryByName("_employee_experience_" + ServerSecurityContext.getInstance().getCompanyId());
        if (dynamicQuery == null) dynamicQuery = dynamicQueryManager.getQueryByName("_employee_experience_default");
        if (dynamicQuery != null) {
            String query = dynamicQuery.getQuery_text()
                    .replaceAll("anv", ServerSecurityContext.getInstance().getCompanyId())
                    .replaceAll("_employee_ID", String.valueOf(employeeID))
                    .replaceAll("_leave_start_date", new SimpleDateFormat("yyyy-MM-dd").format(startDate));
            Double employeeExperience = (Double) dynamicQueryManager.findNativeSingle(query);
            if (employeeExperience == null) {
                return 0d;
            } else if (employeeExperience >= 20) {
                return 8d;
            } else if (employeeExperience >= 18) {
                return 7d;
            } else if (employeeExperience >= 15) {
                return 6d;
            } else if (employeeExperience >= 13) {
                return 5d;
            } else if (employeeExperience >= 10) {
                return 4d;
            } else if (employeeExperience >= 8) {
                return 3d;
            } else if (employeeExperience >= 5) {
                return 2d;
            }
        }
        return 0d;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<AttendanceMarkListItem> getAttendanceMarkList(ListingFilterParameter fp) {
        var attendanceMarkData = fp.getFacetFilter();
        if (!attendanceMarkData.isFilterChanges()) {
            attendanceMarkData = commonServiceLocal.getUserFacetFilter(attendanceMarkData);
        }
        attendanceMarkData.setApplyFilter(true);
        fp.setFacetFilter(attendanceMarkData);
        ListResult<AttendanceMarkListItem> result = userFingerPrintmanager.getAttendanceMarkList(fp);
        result.getList().forEach(item -> {
            if (item.getAdjustmentId() != null) {
                ArrayList<FileResource> fileResources = documentsService.getFileResources(F_EMPLOYEE_ATTENDANCE, item.getAdjustmentId(), item.getAdjustmentId());
                if (fileResources != null && !fileResources.isEmpty()) {
                    item.setPictureUrl(fileResources.get(0).getDownloadUrl());
                }
            }
            if (item.getPhotoId() != null) {
                item.setProfilePictureUrl(commonService.getImageUrl(item.getPhotoId()));
            }
        });
        return result;
    }

    public void approveAttendanceMarks(ArrayList<Integer> fingerprintIds) {
        userFingerPrintmanager.approveAttendanceMarks(fingerprintIds);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getAttendanceMarkFacetData(FacetFilterRpc attendanceMarkData) {
        if (!attendanceMarkData.isFilterChanges()) {
            FacetFilterRpc original = attendanceMarkData;
            attendanceMarkData = commonServiceLocal.getUserFacetFilter(attendanceMarkData);
            original.getFacetContentMap().forEach(attendanceMarkData.getFacetContentMap()::putIfAbsent);
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(attendanceMarkData.getSearchKey());
        fp.setLimit(10000);
        attendanceMarkData.setApplyFilter(true);
        fp.setFacetFilter(attendanceMarkData);
        ListResult<AttendanceMarkListItem> allItems = userFingerPrintmanager.getAttendanceMarkList(fp);
        return fillAttendanceMarkFacetData(allItems, attendanceMarkData);
    }

    private FacetFilterRpc fillAttendanceMarkFacetData(ListResult<AttendanceMarkListItem> listResult, FacetFilterRpc facetFilter) {
        if (facetFilter == null) return null;
        Set<String> keySet = facetFilter.getFacetContentMap().keySet();
        keySet.stream()
                .filter(facetFilter.getShowSolrFieldMap()::containsKey)
                .forEach(key -> {
                    FacetSolrField solrField = facetFilter.getShowSolrFieldMap().get(key);
                    if (solrField != null && facetFilter.getFacetContentMap().containsKey(key)) {
                        Map<Integer, SelectItem> itemMap = createAttendanceMarkFacetItemMap(listResult, key);
                        facetFilter.getFacetContentMap().get(key).setFacetItems(itemMap.values().toArray(new SelectItem[0]));
                    }
                });
        return facetFilter;
    }

    private Map<Integer, SelectItem> createAttendanceMarkFacetItemMap(ListResult<AttendanceMarkListItem> listResult, String filterKey) {
        Map<Integer, Integer> countMap = new LinkedHashMap<>();
        Map<Integer, SelectItem> itemMap = new LinkedHashMap<>();
        listResult.getList().forEach(item -> {
            switch (filterKey) {
                case "employee":
                    buildAttendanceFacetEntry(countMap, itemMap,
                            item.getEmployeeId(), item.getEmployeeName());
                    break;
                case "department":
                    buildAttendanceFacetEntry(countMap, itemMap,
                            item.getDepartmentId(), item.getDepartment());
                    break;
                case "position":
                    buildAttendanceFacetEntry(countMap, itemMap,
                            item.getPositionId(), item.getPosition());
                    break;
                case "location":
                    buildAttendanceFacetEntry(countMap, itemMap,
                            item.getLocationId(), item.getLocation());
                    break;
                case "timeslot":
                    buildAttendanceFacetEntry(countMap, itemMap,
                            item.getTimeslotId(), item.getTimeslotName());
                    break;
                case "supervisor":
                    buildAttendanceFacetEntry(countMap, itemMap,
                            item.getSupervisorId(), item.getSupervisor());
                    break;
                case "terminal":
                    buildAttendanceFacetEntry(countMap, itemMap,
                            item.getTerminalId(), item.getTerminal());
                    break;
                case "status":
                    buildAttendanceFacetEntry(countMap, itemMap,
                            item.getEmployeeStatusId(), item.getEmployeeStatus());
                    break;
                case "role":
                    if (item.getRoleIds() != null && item.getRole() != null) {
                        String[] roleIdArr = item.getRoleIds().split(",");
                        String[] roleNameArr = item.getRole().split(", ");
                        for (int i = 0; i < roleIdArr.length && i < roleNameArr.length; i++) {
                            try {
                                int roleId = Integer.parseInt(roleIdArr[i].trim());
                                buildAttendanceFacetEntry(countMap, itemMap, roleId, roleNameArr[i].trim());
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                    break;
                case "source":
                    FingerprintSource fs = FingerprintSource.from(item.getSource());
                    String dtName = switch (fs) {
                        case MOBILE -> "Mobile";
                        case FACE_ID -> "Face ID";
                        case WEB -> "Web";
                        default -> "Unknown";
                    };
                    buildAttendanceFacetEntry(countMap, itemMap, fs.ordinal(), dtName, fs.name());
                    break;
                case "isAuto":
                    boolean isAutoVal = Boolean.TRUE.equals(item.getIsAuto());
                    buildAttendanceFacetEntry(countMap, itemMap, isAutoVal ? 1 : 0,
                            isAutoVal ? "Yes" : "No", isAutoVal ? "YES" : "NO");
                    break;
            }
        });
        return itemMap;
    }

    private void buildAttendanceFacetEntry(Map<Integer, Integer> countMap, Map<Integer, SelectItem> itemMap, Integer id, String name) {
        buildAttendanceFacetEntry(countMap, itemMap, id, name, null);
    }

    private void buildAttendanceFacetEntry(Map<Integer, Integer> countMap, Map<Integer, SelectItem> itemMap, Integer id, String name, String code) {
        int safeId = id != null ? id : -1;
        String safeName = (name != null && !name.trim().isEmpty()) ? name : "N/A";
        countMap.compute(safeId, (k, v) -> (v == null) ? 1 : v + 1);
        String label = safeName + " ( <b>" + countMap.get(safeId) + "</b> )";
        SelectItem si = new SelectItem(safeId, null, label);
        if (code != null) si.setCode(code);
        itemMap.put(safeId, si);
    }

    @Override
    public List<InOutPairDto> getEmployeeInOutRecords(Integer employeeID, DateNonConvertable date) {
//        List<InOutPairDto> userInOutPairs = userFingerPrintmanager.getUserInOutPairs(employeeID, date.getNonConvertedDate());
//        List<InOutPairDto> userInOutPairs = userFingerPrintmanager.getUserInOutPairs(employeeID, date.getNonConvertedDate(), date.getNonConvertedDate());
//        appendSnapshots(userInOutPairs);
        return userFingerPrintmanager.getUserInOutPairs(employeeID, date.getNonConvertedDate());
    }

    private Map<Integer, Double[]> getLateAndEarlyPercentageBatch(Date startDate, Set<Integer> employeeIds) {
        Map<Integer, Double[]> result = new HashMap<>();
        if (employeeIds == null || employeeIds.isEmpty()) return result;

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = cal.getTime();

        Calendar endCal = Calendar.getInstance();
        Date today = new Date();
        if (cal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR)
                && cal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH)) {
            endCal.setTime(today);
        } else {
            endCal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        Date endDate = endCal.getTime();

        Map<Integer, Integer[]> lateEarlyMap = userFingerPrintmanager.getLateAndEarlyCountBatch(firstDayOfMonth, endDate, employeeIds);
        Map<Integer, Integer> plannedDaysMap = attendanceRawDataManager.getPlannedDaysForEmployeeBatch(firstDayOfMonth, endDate, employeeIds);
        Map<Integer, Integer> workedDaysMap = userFingerPrintmanager.getUserDailySummaryCountBatch(firstDayOfMonth, endDate, employeeIds);
        Map<Integer, Integer> leaveDaysMap = attendanceRawDataManager.getLeaveDatesCountBatch(firstDayOfMonth, endDate, employeeIds);

        for (Integer id : employeeIds) {
            Integer[] lateAndEarlyCount = lateEarlyMap.getOrDefault(id, new Integer[]{0, 0});
            int plannedDays = plannedDaysMap.getOrDefault(id, 0);
            int workedDays = workedDaysMap.getOrDefault(id, 0);
            int leaveDays = leaveDaysMap.getOrDefault(id, 0);

            Double latePercentage = (lateAndEarlyCount[0] != null && plannedDays != 0) ? Math.round((lateAndEarlyCount[0].doubleValue() / (double) plannedDays) * 100d) : 0d;
            Double earlyPercentage = (lateAndEarlyCount[1] != null && plannedDays != 0) ? Math.round((lateAndEarlyCount[1].doubleValue() / (double) plannedDays) * 100d) : 0d;
            Double attendanceRate = (plannedDays - leaveDays) != 0 ? ((double) workedDays / (plannedDays - leaveDays)) * 100 : 0d;
            result.put(id, new Double[]{latePercentage, earlyPercentage, attendanceRate});
        }
        return result;
    }

    public Double[] getLateAndEarlyPercentage(DateNonConvertable fromDate, Integer employeeID) {
        Date originalDate = fromDate.getNonConvertedDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(originalDate);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = cal.getTime();

        Calendar endCal = Calendar.getInstance();
        Date today = new Date();

        if (cal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR)
                && cal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH)) {
            endCal.setTime(today);
        } else {
            endCal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        Date endDate = endCal.getTime();

        Integer[] lateAndEarlyCount = userFingerPrintmanager.getLateAndEarlyCount(firstDayOfMonth, endDate, employeeID);
        int plannedDays = attendanceRawDataManager.getPlannedDaysForEmployee(firstDayOfMonth, endDate, employeeID);
        List<InOutPairDto> userDailySummary = userFingerPrintmanager.getUserDailySummary(employeeID, firstDayOfMonth, endDate);
        List<Date> leaveDates = attendanceRawDataManager.getLeaveDates(firstDayOfMonth, endDate, employeeID);

        Double latePercentage = (lateAndEarlyCount[0] != null && plannedDays != 0) ? Math.round((lateAndEarlyCount[0].doubleValue() / (double) plannedDays) * 100d) : 0d;
        Double earlyPercentage = (lateAndEarlyCount[1] != null && plannedDays != 0) ? Math.round((lateAndEarlyCount[1].doubleValue() / (double) plannedDays) * 100d) : 0d;
        Double attendaceRate = (plannedDays - leaveDates.size()) != 0 ? ((double) userDailySummary.size() / (plannedDays - leaveDates.size())) * 100 : 0d;

        return new Double[]{latePercentage, earlyPercentage,attendaceRate};
    }

    @Override
    public List<InOutPairDto> getEmployeeInOutRecords(Integer employeeID, DateNonConvertable fromDate, DateNonConvertable toDate, Boolean includeSnapshots) {
        List<InOutPairDto> userInOutPairs = userFingerPrintmanager.getUserInOutPairsForApi(employeeID, fromDate.getNonConvertedDate(), toDate.getNonConvertedDate());
        if (Boolean.TRUE.equals(includeSnapshots)) {
            userInOutPairs = appendSnapshots(userInOutPairs);
        }
        return userInOutPairs;
    }

    @Override
    public List<InOutPairDto> getEmployeeInOutDailySummary(Integer employeeID, DateNonConvertable fromDate, DateNonConvertable toDate, Boolean includeSnapshots) {
        List<InOutPairDto> userDailySummary = userFingerPrintmanager.getUserDailySummary(employeeID, fromDate.getNonConvertedDate(), toDate.getNonConvertedDate());
        if (Boolean.TRUE.equals(includeSnapshots)) {
            userDailySummary = appendSnapshots(userDailySummary);
        }
        return userDailySummary;
    }

    private List<InOutPairDto> appendSnapshots(List<InOutPairDto> userInOutPairs) {
        if (userInOutPairs.isEmpty()) return List.of();
        Integer[] inIds = userInOutPairs.stream().map(InOutPairDto::getInId).filter(Objects::nonNull).toArray(Integer[]::new);
        Integer[] outIds = userInOutPairs.stream().map(InOutPairDto::getOutId).filter(Objects::nonNull).toArray(Integer[]::new);

        Integer[] eventIds = ArrayUtils.addAll(inIds, outIds);
        List<EdsUsersFingerPrintAdjustment> adjustments = userFingerPrintAdjustmentManager.getByFingerprint(eventIds);
        Map<Integer, List<String>> snapshots = new HashMap<>();
        Map<Integer, EdsUsersFingerPrintAdjustment> adjustmentMap = new HashMap<>();
        for (EdsUsersFingerPrintAdjustment adjustment : adjustments) {
            if (adjustment == null) {
                continue;
            }
            ArrayList<FileResource> fileResources = documentsService.getFileResources(F_EMPLOYEE_ATTENDANCE, adjustment.getObjectID(), adjustment.getObjectID());
            snapshots.put(adjustment.getFingerprint().getObjectID(), fileResources.stream().map(FileResource::getDownloadUrl).toList());
            adjustmentMap.put(adjustment.getFingerprint().getObjectID(), adjustment);
        }

        List<Integer> allFingerprintIds = Arrays.asList(eventIds);
        List<EdsUsersFingerPrint> fingerprints = userFingerPrintmanager.get(allFingerprintIds);

        Map<Integer, String> fingerprintDeviceUuidMap = fingerprints.stream()
                .filter(fp -> fp.getDeviceUUID() != null)
                .collect(Collectors.toMap(EdsUsersFingerPrint::getObjectID, EdsUsersFingerPrint::getDeviceUUID, (a, b) -> a));
        List<String> deviceUuids = fingerprintDeviceUuidMap.values().stream().distinct().collect(Collectors.toList());
        Map<String, EdsAttendanceTerminal> terminalByUuid = new HashMap<>();
        if (!deviceUuids.isEmpty()) {
            attendanceTerminalManager.getAll(deviceUuids, null)
                    .forEach(t -> terminalByUuid.put(t.getCompanyUniqueID(), t));
        }

        EdsUsersFingerPrintAdjustment defaultValue = new EdsUsersFingerPrintAdjustment();
        List<InOutPairDto> list = new ArrayList<>();
        for (InOutPairDto u : userInOutPairs) {
            if (u.getInId() != null) {
                u.setInSnapshotLinks(snapshots.get(u.getInId()));
                u.setInLatitude(adjustmentMap.getOrDefault(u.getInId(), defaultValue).getLatitude());
                u.setInLongitude(adjustmentMap.getOrDefault(u.getInId(), defaultValue).getLongitude());
                EdsProject inProject = adjustmentMap.getOrDefault(u.getInId(), defaultValue).getProject();
                u.setInProject(inProject != null ? new SelectItem(inProject.getObjectID(), inProject.getName()) : null);
                EdsTask inTask = adjustmentMap.getOrDefault(u.getInId(), defaultValue).getTask();
                u.setInTask(inTask != null ? new SelectItem(inTask.getObjectID(), inTask.getName()) : null);
                FingerprintSource inSource = adjustmentMap.getOrDefault(u.getInId(), defaultValue).getSource();
                u.setInSource(inSource != null ? inSource.name() : "UNKNOWN");
                EdsAttendanceTerminal inTerminal = terminalByUuid.get(fingerprintDeviceUuidMap.get(u.getInId()));
                if (inTerminal != null) {
                    u.setInTerminalName(inTerminal.getCompanyBranchName());
                    EdsLocation inLocation = inTerminal.getLocation();
                    if (inLocation != null) u.setInLocationName(inLocation.getLocationRealName());
                }
            }
            if (u.getOutId() != null) {
                u.setOutSnapshotLinks(snapshots.get(u.getOutId()));
                u.setOutLatitude(adjustmentMap.getOrDefault(u.getOutId(), defaultValue).getLatitude());
                u.setOutLongitude(adjustmentMap.getOrDefault(u.getOutId(), defaultValue).getLongitude());
                EdsProject outProject = adjustmentMap.getOrDefault(u.getOutId(), defaultValue).getProject();
                u.setOutProject(outProject != null ? new SelectItem(outProject.getObjectID(), outProject.getName()) : null);
                EdsTask outTask = adjustmentMap.getOrDefault(u.getOutId(), defaultValue).getTask();
                u.setOutTask(outTask != null ? new SelectItem(outTask.getObjectID(), outTask.getName()) : null);
                FingerprintSource outSource = adjustmentMap.getOrDefault(u.getOutId(), defaultValue).getSource();
                u.setOutSource(outSource != null ? outSource.name() : "UNKNOWN");
                EdsAttendanceTerminal outTerminal = terminalByUuid.get(fingerprintDeviceUuidMap.get(u.getOutId()));
                if (outTerminal != null) {
                    u.setOutTerminalName(outTerminal.getCompanyBranchName());
                    EdsLocation outLocation = outTerminal.getLocation();
                    if (outLocation != null) u.setOutLocationName(outLocation.getLocationRealName());
                }
            }
            list.add(u);
        }
        return list;
    }

    @Override
    public void updateOrRemoveLabourPeriod(Integer leaveRequestID) {
        EdsSickRequest sickRequest = sickRequestManager.get(leaveRequestID);
        boolean isRecall = sickRequest.getRecallDate() != null;
        if (!sickRequest.getRecalculateLabourPeriod() || isRecall) {
            sickRequest.setRecalculateLabourPeriod(true);
            sickRequestManager.update(sickRequest);
            Date startDate = sickRequest.getStartDate();
            Date endDate = isRecall ? sickRequest.getRecallDate() : sickRequest.getEndDate();
            Integer employeeID = sickRequest.getEmployee() != null ? sickRequest.getEmployee().getObjectID() : null;
            List<EdsLabourPeriod> labourPeriodList = labourPeriodManager.periodListByEmployee(employeeID);
            if (!CollectionUtils.isEmpty(labourPeriodList)) {
                boolean findPeriod = false;
                Date labourEndDate = null;
                for (EdsLabourPeriod edsLabourPeriod : labourPeriodList) {

                    if (findPeriod) {
                        if (labourEndDate != null) {
                            Calendar startCalendar = Calendar.getInstance();
                            startCalendar.setTime(labourEndDate);
                            startCalendar.add(Calendar.DATE, 1);
                            ServerUtils.setBeginningOfTheDay(startCalendar);
                            edsLabourPeriod.setStartDate(startCalendar.getTime());

                            Calendar endCalendar = Calendar.getInstance();
                            endCalendar.setTime(labourEndDate);
                            endCalendar.add(Calendar.YEAR, 1);
                            ServerUtils.setBeginningOfTheDay(endCalendar);
                            edsLabourPeriod.setEndDate(endCalendar.getTime());

                            edsLabourPeriod.setAllowance(24d);
                            edsLabourPeriod.setActualAllowanceDays(24d);
                            labourEndDate = endCalendar.getTime();
                            labourPeriodManager.update(edsLabourPeriod);
                        } else if (endDate.before(edsLabourPeriod.getEndDate())) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(endDate);
                            ServerUtils.setBeginningOfTheDay(calendar);
                            labourEndDate = calendar.getTime();
                            edsLabourPeriod.setEndDate(calendar.getTime());
                            edsLabourPeriod.setAllowance(Double.valueOf(0));
                            edsLabourPeriod.setActualAllowanceDays(Double.valueOf(0));
                            labourPeriodManager.update(edsLabourPeriod);
                        } else {
                            edsLabourPeriod.setAllowance(Double.valueOf(0));
                            edsLabourPeriod.setActualAllowanceDays(Double.valueOf(0));
                            labourPeriodManager.update(edsLabourPeriod);
                        }
                    }

                    if ((startDate.equals(edsLabourPeriod.getStartDate()) || startDate.after(edsLabourPeriod.getStartDate()))
                            && (startDate.equals(edsLabourPeriod.getEndDate()) || startDate.before(edsLabourPeriod.getEndDate()))) {
                        findPeriod = true;
                        if (endDate.before(edsLabourPeriod.getEndDate())) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(endDate);
                            ServerUtils.setBeginningOfTheDay(calendar);
                            labourEndDate = calendar.getTime();
                            edsLabourPeriod.setEndDate(calendar.getTime());
                            edsLabourPeriod.setAllowance(Double.valueOf(0));
                            edsLabourPeriod.setActualAllowanceDays(Double.valueOf(0));
                            labourPeriodManager.update(edsLabourPeriod);
                        } else {
                            edsLabourPeriod.setAllowance(Double.valueOf(0));
                            edsLabourPeriod.setActualAllowanceDays(Double.valueOf(0));
                            labourPeriodManager.update(edsLabourPeriod);
                        }
                    }
                }
            }
        }
    }
}
