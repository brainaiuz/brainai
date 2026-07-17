package com.edatasite.workforce.gwt.dashboardwidget.server.app;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.document.ContactSolrDoc;
import com.edatasite.workforce.gwt.accounting.client.rpc.BudgetManagerItems;
import com.edatasite.workforce.gwt.accounting.client.rpc.PnLFilter;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.enums.StackedEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetItem;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.AvailableLeaveRequest;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.LeaveRequestChartRpc;
import com.edatasite.workforce.gwt.core.client.rpc.funnel.Color;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrNewsRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.TaskPermissionEnum;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ContactCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.HolidayManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestDurationManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipPaymentsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.CompanySettingsItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardBirthdayItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardComboItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardContactItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardMyCalendarCarouselItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardMyCalendarDetailItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardNewsItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardPMItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWeatherItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.HolidayRPC;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.ImportGuideItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.LRPC;
import com.edatasite.workforce.gwt.dashboardwidget.server.app.helper.WeatherResponse;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.server.ReportingSerivceLocal;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoList;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoListItem;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoListRpc;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CommonParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Dilshod Madrahimov on 1/25/2016.
 */
@Transactional
@Service("dashboardWidgetService")
public class DashboardWidgetServiceImpl implements DashboardWidgetService, Constants {

    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private MyUpdateTypeManager myUpdateTypeManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ReportingSerivceLocal reportingSerivce;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private AccountingServiceLocal accountingService;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private HolidayManager holidayManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private SickRequestDurationManager sickRequestDurationManager;
    @Autowired
    private GoogleCalendarServiceLocal googleCalendarServiceLocal;
    @Autowired
    private PayslipPaymentsManager payslipPaymentsManager;
    @Autowired
    private PayrollCategoryManager payrollCategoryManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private NewsManager newsManager;
    @Autowired
    private EmailSettingsManager emailSettingsManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private ContactSolrComponent contactSolrComponent;

    private final Boolean hasPermissionToReport = false;

    @Override
    public void deleteTodoTask(Integer taskID) {
        taskServiceLocal.deleteTask(taskID, null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TodoListRpc getTodoListRpc() {
        TodoListRpc todoListRpc = new TodoListRpc();
        EdsUser edsUser = userManager.getUser();
        if (edsUser != null) {
            SelectItem currentUserItem = new SelectItem(edsUser.getObjectID(), (edsUser.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")")));
            EdsProject edsProject = edsUser.getCompany().getDefaultProject();
            if (edsProject != null) {
                todoListRpc.setProjectId(edsProject.getObjectID());

                if (edsUser.isEmployee()) {
                    EdsProjectEmployee edsProjectEmployee = projectEmployeeManager.getProjectEmployee(edsUser.getEmployee(), edsProject);
                    if (edsProjectEmployee != null) {
                        currentUserItem.setDescription(String.valueOf(edsProjectEmployee.getObjectID()));
                    }
                }
            }
            todoListRpc.setCurrentUser(currentUserItem);
        }
        todoListRpc.setPriorities(taskServiceLocal.getPriorities());
        EdsReference reference = referenceManager.getByCode(EdsTask.MEDIUM);
        if (reference != null) {
            SelectItem mediumPriority = new SelectItem();
            mediumPriority.setId(reference.getObjectID());
            mediumPriority.setName(reference.getName());
            mediumPriority.setDescription(reference.getCode());
            todoListRpc.setMediumPriority(mediumPriority);
        }
        return todoListRpc;
    }

    @Override
    @Transactional
    public TodoList getTodoLists(Integer userID) {
        TodoList result = new TodoList();
        EdsUser user = userManager.getUser();
        EdsProject project = user.getCompany().getDefaultProject();
        if (project == null) {
            return result;
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(userID);
        fp.setProjectId(project.getObjectID());
        List<EdsTask> edsTasks = taskManager.getTodoListTasks(fp);
        List<TodoListItem> items = Lists.newArrayList();
        for (EdsTask task : edsTasks) {
            TodoListItem item = new TodoListItem();
            item.setReporterName(task.getCreator().getName());
            item.setTaskID(task.getObjectID());
            item.setTaskName(task.getName());
            item.setPriorityName(referenceWfmMessageSource.localizeRef(task.getPriority()));
            if (task.getPriority() != null) {
                item.setPriorityCode(task.getPriority().getCode());
            }
            item.setStatusID(task.getStatus().getObjectID());
            for (EdsEmployeeTask et : task.getUnDeletedAssignments()) {
                if (et.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID().equals(userID)) {
                    item.setStatusID(et.getStatus() != null ? et.getStatus().getObjectID() : null);
                    break;
                }
            }
            items.add(item);
        }
        result.setTodoListItems(items);
        result.setProjectID(project.getObjectID());
        result.setLocale(user.getLocale() != null ? user.getLocale().getLanguageCode() : "en");
        return result;
    }

    @Override
    public void saveTodoListOrdering(HashMap<Integer, Integer> taskIdsWithOrdering) {
        for (Integer taskId : taskIdsWithOrdering.keySet()) {
            EdsTask task = taskManager.get(taskId);
            if (task != null) {
                task.setTodoListOrder(taskIdsWithOrdering.get(taskId));
                taskManager.update(task);
            }
        }
    }

    @Override
    public Integer saveMultipleTask(MultiTaskList multiTaskList) {
        for (TaskSingleItem ts : multiTaskList.getTaskSingleItems()) {
            if (ts.getObjectID() != null) {
                EdsTask edsTask = taskManager.get(ts.getObjectID());
                EdsUser updater = userManager.getUser();
                try {
                    EditTask editTask = new EditTask();
                    editTask.setCreatedFrom(MultiTaskList.FROM_TODO_LIST);

                    List<EdsTaskRbac> entries = taskRbacManager.getEntriesForUserOrHisMemberGoups(edsTask, updater, updater.getMembershipGroups());
                    if (entries == null || entries.size() == 0) {
                        return ts.getObjectID();
                    }

                    List<String> permissions = taskServiceLocal.getAggregatePermissions(entries);

                    if (permissions.contains(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode()) && permissions.contains(TaskPermissionEnum.STATUS_EDIT.getCode())) {
                        editTask.setUpdateTaskStatusForAll(true);
                        editTask.setUpdateAssignmentTaskStatus(true);
                    } else if (permissions.contains(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode()) && !permissions.contains(TaskPermissionEnum.STATUS_EDIT.getCode())) {
                        editTask.setUpdateTaskStatusForAll(true);
                        editTask.setUpdateAssignmentTaskStatus(false);
                    } else {
                        editTask.setUpdateTaskStatusForAll(false);
                        editTask.setUpdateAssignmentTaskStatus(true);
                    }
                    NumberData numberData = taskServiceLocal.generateTaskNumber(edsTask.getProject().getObjectID(), ts.getStartDate(), null);
                    numberData.setNumberString(edsTask.getNumber());
                    numberData.setIntNumber(edsTask.getIntNumber());
                    editTask.setNumberData(numberData);

                    editTask.setNumber(edsTask.getNumber());
                    editTask.setDescription(edsTask.getDescription());
                    if (Integer.valueOf(Constants.COMPLETED).equals(ts.getStatusID()) && !genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                        editTask.setPercent(100f);
                    } else if (Integer.valueOf(Constants.NOT_STARTED).equals(ts.getStatusID())) {
                        editTask.setPercent(0f);
                    } else {
                        editTask.setPercent(edsTask.getPercent());
                    }
                    editTask.setBillable(edsTask.getBillable());
                    editTask.setStartDate(edsTask.getStartDate());
                    editTask.setDueDate(edsTask.getDueDate());

                    editTask.setObjectID(ts.getObjectID());
                    editTask.setName(ts.getName());
                    editTask.setAllDay(ts.isAllDay());
                    editTask.setProjectId(multiTaskList.getProjectID());
                    editTask.setStatusId(ts.getStatusID());
                    editTask.setPriorityId(ts.getPriorityID());
                    editTask.setEmployeeID(ts.getEmployeeID());

                    taskServiceLocal.updateTask(editTask);
                    return ts.getObjectID();
                } catch (NumberExistingException e) {
                    e.printStackTrace();
                }
            }
        }
        Integer[] taskIdsArray = taskServiceLocal.saveMultipleTask(multiTaskList);
        return taskIdsArray[0];
    }

    @Override
    public ListResult<MyUpdateItem> getMyLatestUpdates(ListingFilterParameter fp, DateNonConvertable startDate) {
        ArrayList<MyUpdateItem> result = Lists.newArrayList();

        EdsUser user = userManager.getUser();
        List<Integer> userRoleIds = Lists.newArrayList(user.getRoleIds());
        if (userRoleIds.isEmpty()) {
            return new ListResult<>(result, 0);
        }
        List<Integer> sortedRoleIds = ServerUtils.getUserRolesSorted(userRoleIds);
        Integer userMaxRoleID = sortedRoleIds.get(0);
        fp.setViewAsId(userMaxRoleID);
        fp.setSearchType(0);
        fp.setGroupById(0);
        if (startDate != null) {
            fp.setStartDate(startDate.getNonConvertedDate());
        }

        Set<Integer> ignoringDuplicateTaskStatusComplete = new HashSet<>();
        int ignoreCount = 0;
        Integer count = myUpdateManager.getListCount(fp);
        if (count != null && count > 0) {
            List<EdsMyUpdate> myupdats = myUpdateManager.getAllUpdatesList(fp, true);
            for (EdsMyUpdate mupdate : myupdats) {
                if (MyUpdateTypeManager.TASK_STATUS_COMPELETED.equals(mupdate.getTypeCode())) {
                    if (ignoringDuplicateTaskStatusComplete.contains(mupdate.getAffectedID())) {
                        ignoreCount++;
                        continue;
                    } else {
                        ignoringDuplicateTaskStatusComplete.add(mupdate.getAffectedID());
                    }
                }
                MyUpdateItem item = new MyUpdateItem();
                item.setType(mupdate.getEventType());
                item.setEventDate(mupdate.getDate());
                item.setLink(myUpdateTypeManager.getUpdatesLink(mupdate));
                myUpdateTypeManager.getMyUpdateMessage(mupdate, item, false);
                item.setSectionURL(myUpdateTypeManager.getSectionURL(mupdate));
                result.add(item);
            }
        }
        return new ListResult<>(result, count - ignoreCount);
    }

    @Override
    public ListResult<MyUpdateItem> getMyPeersUpdates(ListingFilterParameter fp, DateNonConvertable startDate) {
        ArrayList<MyUpdateItem> result = Lists.newArrayList();

        EdsUser user = userManager.getUser();
        List<Integer> userRoleIds = Lists.newArrayList(user.getRoleIds());
        if (userRoleIds.isEmpty()) {
            return new ListResult<>(result, 0);
        }
        List<Integer> sortedRoleIds = ServerUtils.getUserRolesSorted(userRoleIds);
        Integer userMaxRoleID = sortedRoleIds.get(0);
        fp.setViewAsId(userMaxRoleID);
        fp.setSearchType(1);
        fp.setGroupById(0);
        if (startDate != null) {
            fp.setStartDate(startDate.getNonConvertedDate());
        }

        Integer count = myUpdateManager.getListCount(fp);
        if (count != null && count > 0) {
            List<EdsMyUpdate> edsMyUpdates = myUpdateManager.getAllUpdatesList(fp, true);
            for (EdsMyUpdate edsMyUpdate : edsMyUpdates) {
                MyUpdateItem item = new MyUpdateItem();
                item.setType(edsMyUpdate.getEventType());

                item.setEventDate(edsMyUpdate.getDate());
                myUpdateTypeManager.getMyUpdateMessage(edsMyUpdate, item, true);
                result.add(item);
            }
        }
        return new ListResult<>(result, count);
    }

    @Override
    public ListResult<DashboardContactItem> getMyContacts(ListingFilterParameter fp, ListLoadConfig config) {
//        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(crmServiceLocal.getSolrQueryForContact(fp, config, userManager.getUser()), SolrRequest.METHOD.POST);
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
        Page<ContactSolrDoc> contactSolrDocPage = contactSolrComponent.getList(fp, config, userManager.getUser());
        ArrayList<DashboardContactItem> result = Lists.newArrayList();
        long count = 0;
        if (contactSolrDocPage != null && contactSolrDocPage.getContent() != null && contactSolrDocPage.getContent().size() > 0) {
            count = contactSolrDocPage.getTotalElements();
            for (ContactSolrDoc contactSolrDoc : contactSolrDocPage.getContent()) {
                DashboardContactItem item = new DashboardContactItem();
                item.setObjectId(contactSolrDoc.getContactId());
                item.setFirstName(contactSolrDoc.getFirstName());
                item.setLastName(contactSolrDoc.getLastName());
                item.setCompanyName(contactSolrDoc.getAccountName());
                item.setPrimaryEmail(contactSolrDoc.getPrimaryEmail());
                item.setPrimaryPhone(contactSolrDoc.getPrimaryPhone());
                item.setFavourited(contactSolrDoc.getFavourited());
                item.setEmailOptOut(contactSolrDoc.getEmailAllowed());
                EdsCrmContact contact = crmContactManager.get(item.getObjectId());
                if (contact != null && contact.getPhoto() != null) {
                    item.setImageUrl(commonServiceLocal.getImageUrl(contact.getPhoto().getObjectID()));
                }
                item.setCrmAccountId(contactSolrDoc.getAccountId());
                result.add(item);
            }
        }
        return new ListResult<>(result, (int) count);
    }

    @Override
    public ChartData getDynamicComponentData(DashboardComponentItem gridItemConfig) {
        return reportingSerivce.getReportChartData(gridItemConfig);
    }

    @Override
    public void updateContactFavourite(Integer contactId, boolean isFavourited) {
        EdsCrmContact edsCrmContact = crmContactManager.get(contactId);

        if (edsCrmContact == null) {
            return;
        }
        edsCrmContact.setFavourited(isFavourited);
        try {
            crmContactManager.update(edsCrmContact);
            contactSolrComponent.index(edsCrmContact);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DashboardContactItem getContactData() {
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null) {
            return null;
        }
        List<EdsContactCategory> edsCategories = contactCategoryManager.getAllCategories(edsUser.getObjectID());

        SelectItem[] categories = edsCategories.stream()
                .map(item -> new SelectItem(item.getObjectID(), referenceWfmMessageSource.localize(item.getConstantName() != null ? item.getConstantName().replace(" ", "_") : item.getName().replace(" ", "_"), item.getName())))
                .toArray(SelectItem[]::new);
        DashboardContactItem result = new DashboardContactItem();
        result.setCategories(categories);
        if (edsUser.isEmployee()) {
            EdsCrmContact edsCrmContact = edsUser.getEmployee().getContact();
            if (edsCrmContact != null) {
                result.setObjectId(edsCrmContact.getObjectID());
                result.setFirstName(edsCrmContact.getFirstName());
                result.setLastName(edsCrmContact.getLastName());
                if (edsCrmContact.getCrmAccount() != null) {
                    result.setCompanyName(edsCrmContact.getCrmAccount().getName());
                }
                if (edsCrmContact.getPhoto() != null) {
                    result.setImageUrl(commonServiceLocal.getImageUrl(edsCrmContact.getPhoto().getObjectID()));
                }
            }
        }
        return result;
    }

    @Override
    public DashboardContactItem getMyContactDetails(Integer contactId) {
        EdsCrmContact edsCrmContact = crmContactManager.get(contactId);
        if (edsCrmContact == null) {
            return new DashboardContactItem();
        }
        DashboardContactItem item = new DashboardContactItem();
        item.setObjectId(edsCrmContact.getObjectID());
        item.setJobTitle(edsCrmContact.getJobTitles());

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCreatedFrom(Appointment.FROM_CRM);
        fp.setRelationID(contactId);
        fp.setRelationType(RelationItem.TYPE_CONTACT);
        fp.setStart(0);
        fp.setLimit(4);
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(crmServiceLocal.getEventCoreSolrQuery(userManager.getUser(), null, fp));
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EVENT_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(crmServiceLocal.getEventSolrQuery(fp, solrQuery.toString()), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        if (resp != null && resp.getResults() != null) {
            for (SolrDocument document : resp.getResults()) {
                MyUpdateItem event = new MyUpdateItem();
                event.setTitle(SolrUtils.asString(document, SolrEventRepresenter.FIELD_SUBJECT, ""));
                event.setMessage(SolrUtils.asString(document, SolrEventRepresenter.FIELD_DESCRIPTION, ""));
                event.setStartDate(SolrUtils.asDate(document, SolrEventRepresenter.FIELD_START_DATE));
                event.setEndDate(SolrUtils.asDate(document, SolrEventRepresenter.FIELD_END_DATE));
                event.setUpdateID(Integer.valueOf(SolrUtils.asString(document, SolrEventRepresenter.FIELD_EVENT_ID)));
                event.setType(MyUpdateItem.ADD);
                event.setSubType(MyUpdateItem.ADD);
                item.getLastEvents().add(event);
            }
        }
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ChartData geProjectOverviewData() {

        ChartData chartData = new ChartData();
        LinkedList<String> categories = new LinkedList<>();
        LinkedList<SerieData> serieList = new LinkedList<>();

        List<Object[]> projectsByStatus = projectManager.getCountByStatus();
        if (projectsByStatus == null || projectsByStatus.size() < 1) {
            return null;
        }
        Long count = projectManager.getTotalCount();

        Number[] serie = new Number[projectsByStatus.size()];
        final int[] i = {0};
        projectsByStatus.forEach(objects -> {
            serie[i[0]] = (Number) objects[0];
            categories.add(objects[1] + " (" + serie[i[0]] + ")");
            i[0]++;
        });

        SerieData serieData = new SerieData();
        serieData.setValues(serie);
        serieList.add(serieData);

        chartData.setSeries(serieList);
        chartData.setCategories(categories);

        ChartConfItem confItem = new ChartConfItem();
        confItem.setType(ChartTypeEnum.DONUT_CHART);
        confItem.setLegend(LegendPositionEnum.BOTTOM);
        confItem.setShowLabel(false);
        chartData.setConf(confItem);

        chartData.setTitle(count);

        return chartData;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<DashboardPMItem> getProjectsByDate(ListingFilterParameter fp) {
        ArrayList<DashboardPMItem> list = new ArrayList<>();
        if (!fp.isShowProject()) {
            fp.setEmployeeId(userManager.getUser().getObjectID());
        }
        List<EdsProject> projects = projectManager.getProjectsByDate(fp);
        projects.forEach(project -> {
            DashboardPMItem item = new DashboardPMItem(project.getObjectID());
            item.setObjectID(project.getObjectID());
            item.setName(project.getName());
            item.setNumber(project.getNumber());
            item.setManager(project.getManager() != null ? project.getManager().getName() : "");
            item.setDeadLine(project.getEndDate());
            if (project.getStatus() != null) {
                item.setStatus(project.getStatus().getName());
                item.setReferenecColor(project.getStatus().getReferenceColor() != null ? project.getStatus().getReferenceColor().getHex() : "#f1c974");
            }

            list.add(item);
        });
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<DashboardPMItem> getTasksByDate(ListingFilterParameter fp) {
        ArrayList<DashboardPMItem> list = new ArrayList<>();
        if (!fp.isShowTasks()) {
            fp.setEmployeeId(userManager.getUser().getObjectID());
        }
        List<EdsTask> tasks = taskManager.getTasksByDate(fp);
        tasks.forEach(task -> {
            DashboardPMItem item = new DashboardPMItem(task.getObjectID());
            item.setObjectID(task.getObjectID());
            item.setName(task.getName());
            item.setNumber(task.getNumber());
            item.setDeadLine(task.getDueDate());
            item.setStatus(task.getStatus() != null ? task.getStatus().getName() : "");
            item.setProjectName(task.getProject() != null ? task.getProject().getName() : "");
            if (task.getPriority() != null) {
                item.setPriority(task.getPriority().getName());
                item.setReferenecColor(task.getPriority().getReferenceColor() != null ? task.getPriority().getReferenceColor().getHex() : "#f1c974");
            }

            list.add(item);
        });
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ChartData geProjectTime() {

        Long timeSpent = projectManager.getTotalTimeSpent();
        Long estimated = projectManager.getTotalEstimated();
        if (estimated.equals(0L)) {
            estimated = 1L;
        }
        Long overTime = 0L;
        if (estimated < timeSpent) {
            overTime = timeSpent - estimated;
        }

        ChartData chartData = new ChartData();
        LinkedList<String> categories = new LinkedList<>();
        LinkedList<SerieData> serieList = new LinkedList<>();

        categories.add("Time Spent (" + timeSpent + ")");
        SerieData serieData = new SerieData();
        if (overTime.equals(0L)) {
            serieData.setValues(new Number[]{timeSpent, estimated});
            categories.add("Estimated (" + estimated + ")");
        } else {
            serieData.setValues(new Number[]{timeSpent, overTime});
            categories.add("Overtime (" + overTime + ")");
        }
        serieList.add(serieData);

        chartData.setSeries(serieList);
        chartData.setCategories(categories);

        ChartConfItem confItem = new ChartConfItem();
        confItem.setType(ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART);
        confItem.setLegend(LegendPositionEnum.BOTTOM);
        confItem.setShowLabel(false);
        chartData.setConf(confItem);
//        chartData.setTitle((100 * timeSpent / estimated) + " %");

        return chartData;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ChartData geProjectBudget() {

        ChartData chartData = new ChartData();
        LinkedList<String> categories = new LinkedList<>();
        LinkedList<SerieData> serieList = new LinkedList<>();

        Long actual = projectManager.getTotalTimeSpent();
        Long budget = projectManager.getTotalEstimated();

        SerieData serieData = new SerieData();
        serieData.setName("Project");
        serieData.setValues(new Number[]{actual, budget});
        serieList.add(serieData);

        categories.add("Actual (" + actual + ")");
        categories.add("Budget (" + budget + ")");

        chartData.setSeries(serieList);
        chartData.setCategories(categories);

        ChartConfItem confItem = new ChartConfItem();
        confItem.setType(ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART);
        confItem.setLegend(LegendPositionEnum.BOTTOM);
        confItem.setShowLabel(false);
        chartData.setConf(confItem);
//        chartData.setTitle((100 * 1 / 2) + " %");

        return chartData;
    }

    public ArrayList<LRPC> getLeaveRequestList(ListingFilterParameter fp) {
        ArrayList<LRPC> result = new ArrayList<>();
        ListResult<LeaveRequestLisItem> listResult = availabilityServiceLocal.getLeaveRequestList(fp);
        if (CollectionUtils.isEmpty(listResult.getList())) {
            return new ArrayList<>();
        }

        String ids = listResult.getList()
                .stream()
                .map(x -> String.valueOf(x.getObjectId()))
                .collect(Collectors.joining(", "));

        Map<Integer, Double[]> map = sickRequestDurationManager.getLeaveRequestsDurationByIds(ids);
        for (LeaveRequestLisItem obj : listResult.getList()) {
            LRPC temp = new LRPC();
            temp.setObjectID(obj.getObjectId());
            temp.setEmployeeName(obj.getEmployeeName());
            temp.setEmployeePhotoUrl(hrmsServiceLocal.getEmployeeImageURL(obj.getEmployeeId()));
            temp.setStartDate(obj.getStartDate());
            temp.setEndDate(obj.getEndDate());
            temp.setReason(obj.getReason());
            temp.setEmployeeId(obj.getEmployeeId());
            Double[] dd = map.get(obj.getObjectId());
            String duration = ServerUtils.getLeaveDayFormat(dd, total);
            temp.setDuration(StringUtils.isNotBlank(duration) ? duration : "0");
            result.add(temp);
        }
        return result;
    }

    public SelectItem[] getCustomReasons() {
        return availabilityServiceLocal.getReasons(null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CompanySettingsItem getCompanySettingsGettingStarted() {
        CompanySettingsItem result = new CompanySettingsItem();
        EdsUser loggedUser = userManager.getUser();
        EdsCompany company = loggedUser.getCompany();
        result.setId(company.getObjectID());
        result.setName(company.getName());
        result.setEmail(company.getEmail());
        result.setPhone(company.getPhone());

        if (company.getBillingAddress() != null) {
            result.setAddress(company.getBillingAddress().getRPC());
        }
        return result;
    }

    public void updateCompanyInfoGettingStarted(CompanySettingsItem item) {
        EdsCompany company = userManager.getUser().getCompany();
        company.setName(item.getName());
        company.setPhone(item.getPhone());
        if (item.getEmail() != null) {
            company.setEmail(item.getEmail());
        }

        Address[] addresses = new Address[1];
        addresses[0] = item.getAddress();
        crmServiceLocal.updateAddresses(addresses, company, EdsAddress.BILLING_ADDRESS, false);

        SettingsData settingsData = new SettingsData();

        Calendar endOfYear = new GregorianCalendar();
        endOfYear.set(Calendar.MONTH, 12);
        endOfYear.set(Calendar.DAY_OF_MONTH, endOfYear.getActualMaximum(Calendar.MONTH));
        ServerUtils.setEndOfTheDay(endOfYear);
        settingsData.setFinancialYearEnd(new DateNonConvertable(endOfYear.getTime()));

        EdsCountry country = company.getCountryZone().getCountry();
        if (financialSettingsManager.getFinancialSettings().getCurrency() != null) {
            settingsData.setCurrencyID(financialSettingsManager.getFinancialSettings().getCurrency().getObjectID());
        } else if (country.getCurrency() != null) {
            settingsData.setCurrencyID(country.getCurrency().getObjectID());
        } else {
            settingsData.setCurrencyID(currencyManager.getCurrency(CurrencyManager.USD).getObjectID());
        }
        settingsData.setConversionDate(new Date());
        settingsData.setOverallDatePickerWeekStart(2);
        accountingService.completeAccountingGettingStarted(settingsData);

        companyManager.update(company);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmployeeProfileItem getEmployeeProfileGettingStarted() {
        EmployeeProfileItem profileItem = new EmployeeProfileItem();

        EdsUser user = userManager.getUser();
        EdsEmployee employee = employeeManager.get(user.getObjectID());

        if (employee != null) {
            EdsEmployeeProfile profile = profileManager.getProfile(employee.getObjectID());

            profileItem.setUserId(employee.getObjectID());
            profileItem.setFirstName(employee.getFirstName());
            profileItem.setLastName(employee.getLastName());
            profileItem.setEmail(employee.getEmail());
            profileItem.setPhone(employee.getPrimaryPhone());
            profileItem.setDob(employee.getBirthDay() != null ? new DateNonConvertable(employee.getBirthDay()) : null);
            profileItem.setHireDate(employee.getStartDate() != null ? new DateNonConvertable(employee.getStartDate()) : null);

            if (profile.getContact() != null) {
                for (EdsAddress address : profile.getContact().getAddresses()) {
                    if (address.getRelationType() != null) {
                        if (EdsCrmContactItemParams.HOME == address.getRelationType()) {
                            profileItem.setHomeAddress(address.getRPC());
                            break;
                        }
                    }
                }
            }
        }
        return profileItem;
    }

    public void saveEmployeeProfileGettingStarted(EmployeeProfileItem data) {
        EdsUser user = userManager.getUser();
        EdsEmployee employee = employeeManager.get(user.getObjectID());

        ProfileItem profileItem = new ProfileItem();
        profileItem.setEmployeeId(employee.getObjectID());
        profileItem.setFirstName(data.getFirstName());
        profileItem.setLastName(data.getLastName());
        profileItem.setDob(data.getDob());
        profileItem.setHireDate(data.getHireDate());

        profileItem.setContactType(ContactListItem.EMPLOYEE_CONTACT);
        profileItem.setBirthDate(data.getDob());

        //email
        profileItem.setEmails();
        profileItem.addParam(Constants.CONTACT_EMAILS, Constants.G_WORK, data.getEmail());
        profileItem.setPrimaryEmail(data.getEmail());
        profileItem.getCrmAccount().setEmail(data.getEmail());

        //phone
        profileItem.setPhones();
        profileItem.addParam(Constants.CONTACT_PHONES, Constants.G_WORK, data.getPhone());
        profileItem.setPrimaryPhone(data.getPhone());
        profileItem.getCrmAccount().setPhone(data.getPhone());

        //address
        profileItem.setAddresses(new ArrayList<>());
        profileItem.getAddresses().add(data.getHomeAddress());

        //roles
        profileItem.setRoleId(employee.getRoleIds().toArray(new Integer[]{}));

        hrmsServiceLocal.updateProfile(profileItem);
    }

    public ArrayList<ImportGuideItem> getDataImportGettingStarted() {
        ArrayList<ImportGuideItem> result = new ArrayList<>();
        HashSet<String> modules = moduleManager.getEnabledModuleCodes();

        boolean accountingModuleEnabled = modules.contains(PermissionConstants.ACCOUNTING_MODULE);
        boolean hrmsModuleEnabled = modules.contains(PermissionConstants.HRMS_MODULE);
        boolean crmModuleEnabled = modules.contains(PermissionConstants.CRM_MODULE);

        ImportGuideItem employeeImport = new ImportGuideItem();
        employeeImport.setType(ImportTypeEnum.EMPLOYEE);
        employeeImport.setEnabled(hrmsModuleEnabled);
        result.add(employeeImport);

        ImportGuideItem customerImport = new ImportGuideItem();
        customerImport.setType(ImportTypeEnum.CUSTOMER);
        customerImport.setEnabled(crmModuleEnabled);
        result.add(customerImport);

        ImportGuideItem supplierImport = new ImportGuideItem();
        supplierImport.setType(ImportTypeEnum.SUPPLIER);
        supplierImport.setEnabled(crmModuleEnabled);
        result.add(supplierImport);

        ImportGuideItem productImport = new ImportGuideItem();
        productImport.setType(ImportTypeEnum.PRODUCT);
        productImport.setEnabled(accountingModuleEnabled);
        result.add(productImport);

        ImportGuideItem chartOfAccoutnsImport = new ImportGuideItem();
        chartOfAccoutnsImport.setType(ImportTypeEnum.CHART_OF_ACCOUNTS);
        chartOfAccoutnsImport.setEnabled(accountingModuleEnabled);
        result.add(chartOfAccoutnsImport);

        ImportGuideItem contactImport = new ImportGuideItem();
        contactImport.setType(ImportTypeEnum.CONTACT);
        contactImport.setEnabled(crmModuleEnabled);
        result.add(contactImport);

        ImportGuideItem leadImport = new ImportGuideItem();
        leadImport.setType(ImportTypeEnum.LEAD);
        leadImport.setEnabled(crmModuleEnabled);
        result.add(leadImport);

        ImportGuideItem opprotunityImport = new ImportGuideItem();
        opprotunityImport.setType(ImportTypeEnum.OPPORTUNITY);
        opprotunityImport.setEnabled(crmModuleEnabled);
        result.add(opprotunityImport);

        return result;
    }

    public ChartData getEmployeesGenderRatio() {
        Long[] data = employeeServiceLocal.getEmployeesGenderRatio();
        if (data.length == 0) {
            return null;
        }
        ChartData chartData = new ChartData();
        chartData.setTitle(data[3]);
        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setTitle(commonLocalizer.localize("genderRatio", "Gender Ratio"));
        chartConf.setType(ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartConf.setStacked(StackedEnum.BY_PERCENTANDVALUE);
        chartData.setConf(chartConf);

        SerieData genderData = new SerieData();
        genderData.setName(commonLocalizer.localize("genderRatio"));
        genderData.setColor("#FBA800");
        data[0] = data[0] == null ? 0 : data[0];
        data[1] = data[1] == null ? 0 : data[1];
        data[2] = data[2] == null ? 0 : data[2];
        genderData.setValues(new Number[]{data[0], data[1], data[2]});
        LinkedList<SerieData> series = new LinkedList<>();
        series.add(genderData);
        chartData.setSeries(series);

        LinkedList<String> categories = new LinkedList<>();
        categories.add(commonLocalizer.localize("male", "Male") + " (" + data[0] + ")");
        categories.add(commonLocalizer.localize("female", "Female") + " (" + data[1] + ")");
        categories.add(commonLocalizer.localize("other", "Other") + " (" + data[2] + ")");
        chartData.setCategories(categories);

        return chartData;
    }

    @Override
    public ChartData getTopExpenses(FromToDate fromToDate) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(fromToDate.getFrom().getNonConvertedDate());
        fp.setEndDate(fromToDate.getTo().getNonConvertedDate());
        fp.setAccountType(EXPENSES);
        fp.setStart(0);

        LinkedHashMap<String, BigDecimal> expenseMap = accountingService.getTopExpensesMap(fp);

        LinkedList<String> categories = new LinkedList<>();
        LinkedList<Number> serieValues = new LinkedList<>();

        int limit = 5, index = 0;
        Integer calculationScale = ServerUtils.getCalculationScale();

        String additionalCategory = commonLocalizer.localize("other", "Other");
        BigDecimal additionExpenseTotal = BigDecimal.ZERO;

        for (String account : expenseMap.keySet()) {
            index++;

            if (index > limit) {
                additionExpenseTotal = additionExpenseTotal.add(expenseMap.get(account));
            } else {
                categories.add(account);
                serieValues.add(expenseMap.get(account).setScale(calculationScale, RoundingMode.HALF_UP).abs());
            }
        }

        if (additionExpenseTotal.compareTo(BigDecimal.ZERO) != 0) {
            categories.add(additionalCategory);
            serieValues.add(additionExpenseTotal.setScale(calculationScale, RoundingMode.HALF_UP).abs());
        }

        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setType(ChartTypeEnum.DONUT_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);

        ChartData chartData = new ChartData();
        chartData.setConf(chartConf);

        chartData.setCategories(categories);

        SerieData topExpensesData = new SerieData();
        topExpensesData.setName(commonLocalizer.localize("expense", "Expense"));
        topExpensesData.setValues(serieValues.toArray(new Number[]{}));

        LinkedList<SerieData> series = new LinkedList<>();
        series.add(topExpensesData);
        chartData.setSeries(series);

        return chartData;
    }

    @Override
    public ChartData getEmployeeTopExpenses(FromToDate fromToDate) {
        HashMap<String, BigDecimal> expensesMap = new HashMap<>();
        List<Object[]> expenses = expenseReportManager.getEmployeeTopExpenses(null, fromToDate.getFrom().getNonConvertedDate(), fromToDate.getTo().getNonConvertedDate());

        for (Object[] objects : expenses) {

            BigDecimal total = (BigDecimal) objects[1];

            if (total.intValue() != 0) {
                expensesMap.put((String) objects[0], total);
            }
        }

        LinkedHashMap<String, BigDecimal> map = expensesMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));


        LinkedList<String> categories = new LinkedList<>();
        LinkedList<Number> serieValues = new LinkedList<>();

        int limit = 5, index = 0;
        Integer calculationScale = ServerUtils.getCalculationScale();

        String additionalCategory = commonLocalizer.localize("other", "Other");
        BigDecimal additionExpenseTotal = BigDecimal.ZERO;

        for (String account : map.keySet()) {
            index++;

            if (index > limit) {
                additionExpenseTotal = additionExpenseTotal.add(map.get(account));
            } else {
                categories.add(account);
                serieValues.add(map.get(account).setScale(calculationScale, RoundingMode.HALF_UP).abs());
            }
        }

        if (additionExpenseTotal.compareTo(BigDecimal.ZERO) != 0) {
            categories.add(additionalCategory);
            serieValues.add(additionExpenseTotal.setScale(calculationScale, RoundingMode.HALF_UP).abs());
        }

        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setType(ChartTypeEnum.DONUT_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);

        ChartData chartData = new ChartData();
        chartData.setConf(chartConf);

        chartData.setCategories(categories);

        SerieData topExpensesData = new SerieData();
        topExpensesData.setName(commonLocalizer.localize("expense", "Expense"));
        topExpensesData.setValues(serieValues.toArray(new Number[]{}));

        LinkedList<SerieData> series = new LinkedList<>();
        series.add(topExpensesData);
        chartData.setSeries(series);
        return chartData;
    }

    @Override
    public ChartData getAgingData(DateNonConvertable date) {

        ChartData chartData = new ChartData();

        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setTitle(commonLocalizer.localize("agedReports", "Aged Reports"));
        chartConf.setType(ChartTypeEnum.VERTICAL_BAR_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        categories.add(commonLocalizer.localize("current", "Current"));
        categories.add("1 - 30");
        categories.add("31 - 60");
        categories.add("61 - 90");
        categories.add("> 90");
        chartData.setCategories(categories);

        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setAccountType(Constants.RECEIVABLE);
        filter.setClientId(null);
        filter.setExcludePrePayments(false);
        filter.setDate(date.getNonConvertedDate());
        filter.setShowBudget(false);
        filter.setInterval(30);
        filter.setIntervalLimit(90);
        ListResult<AgingSummaryItem> receivable = invoiceServiceLocal.getOverdueInvoiceByCrmAccount(filter);
        filter.setAccountType(Constants.PAYABLE);
        ListResult<AgingSummaryItem> payable = invoiceServiceLocal.getOverdueInvoiceByCrmAccount(filter);
        LinkedHashMap<String, BigDecimal> receivableList = getReportAsMap(receivable);
        LinkedHashMap<String, BigDecimal> payableList = getReportAsMap(payable);

        Map<String, BigDecimal> sortedReceivable = receivableList.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Map<String, BigDecimal> sortedPayable = payableList.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        LinkedList<SerieData> series = new LinkedList<>();
        SerieData serieRec = new SerieData();
        serieRec.setName(commonLocalizer.localize("receivable", "Receivable"));
        serieRec.setValues(sortedReceivable.values().toArray(new Number[]{}));

        if (!sortedReceivable.values().isEmpty()) {
            series.add(serieRec);
        }

        SerieData seriePay = new SerieData();
        seriePay.setName(commonLocalizer.localize("payable", "Payable"));
        seriePay.setValues(sortedPayable.values().toArray(new Number[]{}));

        if (!sortedPayable.values().isEmpty()) {
            series.add(seriePay);
        }

        chartData.setSeries(series);

        chartData.getConf().setTitle(null);
        return chartData;
    }

    @Override
    public ChartData getSalesPurchaseData(DateNonConvertable date) {
        DateNonConvertable fsYearNonConvertable = commonServiceLocal.getFinancialYearStart();
        Date financialStartYear = fsYearNonConvertable != null ? fsYearNonConvertable.getNonConvertedDate() : ServerUtils.getCompanyDate(new Date(), userManager.getUser().getCompany());

        Calendar calToDate = Calendar.getInstance();
        calToDate.setTime(date.getNonConvertedDate());

        Calendar calFSYear = Calendar.getInstance();
        calFSYear.setTime(ServerUtils.getStartDate(financialStartYear));
        calFSYear.set(Calendar.YEAR, calToDate.get(Calendar.YEAR));

        while (calFSYear.getTime().after(date.getNonConvertedDate())) {
            calFSYear.add(Calendar.YEAR, -1);
        }

        HashMap<String, BigDecimal> salesDatas = invoiceManager.getInvoiceTransactionsForChart(calFSYear.getTime(), date.getNonConvertedDate(), false);
        HashMap<String, BigDecimal> purchaseDatas = invoiceManager.getInvoiceTransactionsForChart(calFSYear.getTime(), date.getNonConvertedDate(), true);

        if (salesDatas.isEmpty() && purchaseDatas.isEmpty()) {
            return null;
        }

        LinkedList<String> categories = new LinkedList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yy");
        while (calFSYear.getTime().compareTo(date.getNonConvertedDate()) <= 0) {
            categories.add(dateFormat.format(calFSYear.getTime()));
            calFSYear.add(Calendar.MONTH, 1);
        }

        Integer calculationScale = ServerUtils.getCalculationScale();
        LinkedList<Number> saleSerieValues = new LinkedList<>();
        LinkedList<Number> purchaseSerieValues = new LinkedList<>();

        for (String category : categories) {

            if (salesDatas.get(category) != null) {
                saleSerieValues.add(salesDatas.get(category).setScale(calculationScale, RoundingMode.HALF_UP));
            } else {
                saleSerieValues.add(0);
            }

            if (purchaseDatas.get(category) != null) {
                purchaseSerieValues.add(purchaseDatas.get(category).setScale(calculationScale, RoundingMode.HALF_UP));
            } else {
                purchaseSerieValues.add(0);
            }
        }

        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setType(ChartTypeEnum.LINE_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);

        ChartData chartData = new ChartData();
        chartData.setConf(chartConf);

        chartData.setCategories(categories);

        SerieData salesSerieData = new SerieData();
        salesSerieData.setName(commonLocalizer.localize("sales", "Sales"));
        salesSerieData.setValues(saleSerieValues.toArray(new Number[]{}));

        SerieData purchaseSerieData = new SerieData();
        purchaseSerieData.setName(commonLocalizer.localize("purchases", "Purchases"));
        purchaseSerieData.setValues(purchaseSerieValues.toArray(new Number[]{}));

        LinkedList<SerieData> series = new LinkedList<>();
        series.add(salesSerieData);
        series.add(purchaseSerieData);
        chartData.setSeries(series);

        return chartData;
    }

    @Override
    public ChartData getIncomeExpensesData(DateNonConvertable date) {
        DateNonConvertable fsYearNonConvertable = commonServiceLocal.getFinancialYearStart();
        Date financialStartYear = fsYearNonConvertable != null ? fsYearNonConvertable.getNonConvertedDate() : ServerUtils.getCompanyDate(new Date(), userManager.getUser().getCompany());

        Calendar calToDate = Calendar.getInstance();
        calToDate.setTime(date.getNonConvertedDate());

        Calendar calFSYear = Calendar.getInstance();
        calFSYear.setTime(ServerUtils.getStartDate(financialStartYear));
        calFSYear.set(Calendar.YEAR, calToDate.get(Calendar.YEAR));

        while (calFSYear.getTime().after(date.getNonConvertedDate())) {
            calFSYear.add(Calendar.YEAR, -1);
        }

        Date currentMonthStart = ServerUtils.getMonthStartDate(date.getNonConvertedDate());
        FromToDate main = new FromToDate(new DateNonConvertable(currentMonthStart), date);
        LinkedList<FromToDate> compareDates = new LinkedList<>();

        Calendar calPrevMS = Calendar.getInstance(); //prev month start
        calPrevMS.setTime(currentMonthStart);

        do {
            calPrevMS.set(Calendar.MONTH, calPrevMS.get(Calendar.MONTH) - 1);

            Calendar calPrevME = Calendar.getInstance();
            calPrevME.setTime(ServerUtils.getMonthEndDate(calPrevMS.getTime()));

            if (calFSYear.getTime().compareTo(calPrevMS.getTime()) < 0) {
                compareDates.add(new FromToDate(new DateNonConvertable(calPrevMS.getTime()), new DateNonConvertable(calPrevME.getTime())));
            } else if (calPrevMS.getTime().compareTo(calFSYear.getTime()) <= 0 && calFSYear.getTime().compareTo(calPrevME.getTime()) <= 0) {
                compareDates.add(new FromToDate(new DateNonConvertable(calFSYear.getTime()), new DateNonConvertable(calPrevME.getTime())));
            }

        } while (calFSYear.getTime().compareTo(calPrevMS.getTime()) < 0);


        PnLFilter pnLFilter = new PnLFilter();
        pnLFilter.setMain(main);
        pnLFilter.setCompareTo(compareDates.toArray(new FromToDate[]{}));
        pnLFilter.setShowBudget(false);
        pnLFilter.setCosolidation(false);
        EdsFinancialSettings edsFinancialSettings = financialSettingsManager.getFinancialSettings();
        if (edsFinancialSettings != null && edsFinancialSettings.getCurrency() != null) {
            pnLFilter.setCurrencyId(edsFinancialSettings.getCurrency().getObjectID());
        }
        BudgetManagerItems result = accountingService.getProfitAndLoss(pnLFilter);

        Number[] incomes = new Number[compareDates.size() + 1];
        Number[] expenses = new Number[compareDates.size() + 1];
        Number[] netProfit = new Number[compareDates.size() + 1];
        LinkedList<String> categories = new LinkedList<>();
        LinkedList<SerieData> series = new LinkedList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-yy");

        for (int index = compareDates.size(); index >= 0; index--) {
            incomes[compareDates.size() - index] = ServerUtils.getTotal(result.getRevenue().getActualTotal().get(index).getValue())
                    .add(ServerUtils.getTotal(result.getSale().getActualTotal().get(index).getValue()))
                    .add(ServerUtils.getTotal(result.getOtherIncome().getActualTotal().get(index).getValue()));

            expenses[compareDates.size() - index] = ServerUtils.getTotal(result.getExpense().getActualTotal().get(index).getValue())
                    .add(ServerUtils.getTotal(result.getDepreciation().getActualTotal().get(index).getValue()))
                    .add(ServerUtils.getTotal(result.getDirectCosts().getActualTotal().get(index).getValue()))
                    .add(ServerUtils.getTotal(result.getOverhead().getActualTotal().get(index).getValue()));

            netProfit[compareDates.size() - index] = ServerUtils.getTotal(result.getNetProfit().get(index).getValue());

            if (index > 0) {
                categories.add(dateFormat.format(compareDates.get(index - 1).getTo().getNonConvertedDate()));
            }
        }
        boolean nodata = true;
        for (int i = 0; i < incomes.length; i++) {

            if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(incomes[i].doubleValue())) != 0) {
                nodata = false;
                break;
            }
            if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(expenses[i].doubleValue())) != 0) {
                nodata = false;
                break;
            }
            if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(netProfit[i].doubleValue())) != 0) {
                nodata = false;
                break;
            }
        }
        if (nodata) {
            return null;
        }
        categories.add(dateFormat.format(main.getTo().getNonConvertedDate()));

        SerieData incomeData = new SerieData();
        incomeData.setName(commonLocalizer.localize("income", "Income"));
        incomeData.setColor("#8ACD00");
        incomeData.setValues(incomes);

        SerieData expenseData = new SerieData();
        expenseData.setName(commonLocalizer.localize("expense", "Expense"));
        expenseData.setColor("#FF0022");
        expenseData.setValues(expenses);

        SerieData netData = new SerieData();
        netData.setName(commonLocalizer.localize("profit", "Profit"));
        netData.setColor("#FBA800");
        netData.setSerieType(ChartTypeEnum.LINE_CHART);
        netData.setValues(netProfit);

        series.add(incomeData);
        series.add(expenseData);
        series.add(netData);

        ChartData chartData = new ChartData();
        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setTitle(commonLocalizer.localize("incomeVsExpenceYTD", "Income vs Expenses YTD"));
        chartConf.setType(ChartTypeEnum.VERTICAL_BAR_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartData.setConf(chartConf);

        chartData.setCategories(categories);
        chartData.setSeries(series);
        return chartData;
    }

    private LinkedHashMap<String, BigDecimal> getReportAsMap(ListResult<AgingSummaryItem> listResult) {
        LinkedHashMap<String, BigDecimal> agingMap = new LinkedHashMap<>();
        if (listResult != null && listResult.getList() != null && listResult.getList().size() > 0) {
            for (AgingSummaryItem aging : listResult.getList()) {
                for (AgingSummaryInvoiceItem item : aging.getInvoiceList()) {
                    String key = item.getAging() > 90 ? ">90" : String.valueOf(item.getAging());
                    if (agingMap.get(key) != null) {
                        BigDecimal newValue = agingMap.get(key).add(item.getAmount());
                        agingMap.put(key, newValue);
                    } else {
                        agingMap.put(key, item.getAmount());
                    }
                }
            }
        }
        return agingMap;
    }

    private LinkedHashMap<Integer, BigDecimal> getReportAsMap(Map<Integer, List<AgingSummaryInvoiceItem>> map) {
        LinkedHashMap<Integer, BigDecimal> agingMap = new LinkedHashMap<>();
        int in, start, interval = 30, intervalLimit = 90, columnCount = 7;
        BigDecimal total;
        BigDecimal balance;
        for (Map.Entry<Integer, List<AgingSummaryInvoiceItem>> m : map.entrySet()) {
            List<AgingSummaryInvoiceItem> items = (ArrayList<AgingSummaryInvoiceItem>) m.getValue();
            in = interval;
            total = BigDecimal.ZERO;
            for (int i = 1, j = -1; i < columnCount - 1; i++, j++) {
                balance = BigDecimal.ZERO;
                start = j * interval;
                in = (j + 1) * interval;
                if (items != null && !items.isEmpty()) {
                    for (AgingSummaryInvoiceItem inv : items) {
                        if (start > intervalLimit) {
                            start = intervalLimit;
                        }
                        if (in > intervalLimit) {
                            in = intervalLimit;
                        }
                        if (inv.getAging() == null || inv.getAmount() == null) {
                            continue;
                        }
                        if ((inv.getAging() > j * interval && inv.getAging() <= in) ||
                                (inv.getAging() > intervalLimit && i == columnCount - 2) ||
                                (i == 1 && inv.getAging() == 0)) {
                            balance = balance.add(inv.getAmount());
                        }
                    }
                    agingMap.put(i, getColumnBalance(agingMap, i).add(balance));
                }
            }
            agingMap.put(columnCount - 1, getColumnBalance(agingMap, columnCount - 1).add(total));
        }

        return agingMap;
    }

    private BigDecimal getColumnBalance(LinkedHashMap<Integer, BigDecimal> agingMap, Integer columnIndex) {
        BigDecimal balance;
        if (agingMap.get(columnIndex) == null) {
            balance = BigDecimal.ZERO;
        } else {
            balance = agingMap.get(columnIndex);
        }
        return balance;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HolidayRPC getCompanyHolidaysForUser() {
        Calendar calendar = Calendar.getInstance();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setYear(calendar.get(Calendar.YEAR));
        EdsUser user = userManager.getUser();
        if (user != null) {
            fp.setLocationId(user.getLocation() != null ? user.getLocation().getObjectID() : null);
        }
        HolidayRPC result = new HolidayRPC();
        List<EdsHoliday> holidayList = holidayManager.list(fp, false);
        ArrayList<AvailableLeaveRequest> holidaysList = new ArrayList<>();
        if (holidayList != null && holidayList.size() > 0) {
            AvailableLeaveRequest holli;
            for (EdsHoliday holObj : holidayList) {
                holli = new AvailableLeaveRequest();
                holli.setHolidayName(holObj.getName());
                holli.setFromNonConvertable(holObj.getStartDate() != null ? new DateNonConvertable(new Date(holObj.getStartDate().getTime())) : null);
                holli.setToNonConvertable(holObj.getEndDate() != null ? new DateNonConvertable(new Date(holObj.getEndDate().getTime())) : null);
                holidaysList.add(holli);
            }
        }
        result.setHolidays(holidaysList);
        result.setServerDate(new DateNonConvertable(new Date()));
        return result;
    }

    @Override
    public DashboardMyCalendarCarouselItem getMyCalendarDays(DateNonConvertable date) {
        DashboardMyCalendarCarouselItem result = new DashboardMyCalendarCarouselItem();
        EdsUser edsUser = userManager.getUser();

        Calendar monthCalendar = new GregorianCalendar();
        monthCalendar.setTime(date.getNonConvertedDate());
        monthCalendar.set(Calendar.AM_PM, 0);
        monthCalendar.set(Calendar.HOUR, 0);
        monthCalendar.set(Calendar.MINUTE, 0);
        monthCalendar.set(Calendar.SECOND, 0);
        monthCalendar.set(Calendar.MILLISECOND, 0);

        List<DashboardMyCalendarCarouselItem> days = Lists.newArrayList();
        int currentYear = monthCalendar.get(Calendar.YEAR);
        int currentMonth = monthCalendar.get(Calendar.MONTH);
        int monthDays = monthCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= monthDays; i++) {
            DashboardMyCalendarCarouselItem item = new DashboardMyCalendarCarouselItem();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(Calendar.YEAR, currentYear);
            calendar.set(Calendar.MONTH, currentMonth);
            calendar.set(Calendar.DATE, i);
            calendar.set(Calendar.AM_PM, 0);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (edsUser != null) {
                List<Object[]> list = taskManager.getMyCalendarDayFirstEvents(calendar.getTime(), edsUser.getObjectID());
                if (list != null) {
                    for (Object[] objects : list) {
                        if (objects == null || objects.length <= 1) {
                            continue;
                        }
                        if (DashboardMyCalendarCarouselItem.TASK.equals(objects[0])) {
                            item.setTask(true);
                        } else if (DashboardMyCalendarCarouselItem.MEETING.equals(objects[0])) {
                            item.setMeeting(true);
                        } else if (DashboardMyCalendarCarouselItem.CALL.equals(objects[0])) {
                            item.setCall(true);
                        }
                    }
                }
            }
            item.setDate(new DateNonConvertable(calendar.getTime()));
            item.setYear(calendar.get(Calendar.YEAR));
            item.setMonth(calendar.get(Calendar.MONTH));
            item.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            item.setWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            days.add(item);
        }
        result.setDays(days.stream().sorted(Comparator.comparing(DashboardMyCalendarCarouselItem::getDay)).collect(Collectors.toCollection(ArrayList::new)));

        return result;
    }

    @Override
    public DashboardMyCalendarCarouselItem getMyCalendarSampleDays(DateNonConvertable date) {
        Calendar monthCalendar = new GregorianCalendar();
        monthCalendar.setTime(date.getNonConvertedDate());
        monthCalendar.set(Calendar.AM_PM, 0);
        monthCalendar.set(Calendar.HOUR, 0);
        monthCalendar.set(Calendar.MINUTE, 0);
        monthCalendar.set(Calendar.SECOND, 0);
        monthCalendar.set(Calendar.MILLISECOND, 0);

        List<DashboardMyCalendarCarouselItem> days = Lists.newArrayList();
        int currentYear = monthCalendar.get(Calendar.YEAR);
        int currentMonth = monthCalendar.get(Calendar.MONTH);
        int monthDays = monthCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= monthDays; i++) {
            DashboardMyCalendarCarouselItem item = new DashboardMyCalendarCarouselItem();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(Calendar.YEAR, currentYear);
            calendar.set(Calendar.MONTH, currentMonth);
            calendar.set(Calendar.DATE, i);
            calendar.set(Calendar.AM_PM, 0);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            item.setCall(true);
            item.setTask(true);
            item.setMeeting(true);
            item.setDate(new DateNonConvertable(calendar.getTime()));
            item.setYear(calendar.get(Calendar.YEAR));
            item.setMonth(calendar.get(Calendar.MONTH));
            item.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            item.setWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            days.add(item);
        }
        DashboardMyCalendarCarouselItem result = new DashboardMyCalendarCarouselItem();
        result.setDays(days.stream().sorted(Comparator.comparing(DashboardMyCalendarCarouselItem::getDay)).collect(Collectors.toCollection(ArrayList::new)));

        return result;
    }

    @Override
    public ArrayList<DashboardMyCalendarDetailItem> getMyCalendarDetailData(DateNonConvertable day) {
        ArrayList<DashboardMyCalendarDetailItem> result = Lists.newArrayList();

        EdsUser edsUser = userManager.getUser();
        if (edsUser == null) {
            return result;
        }
        Calendar monthCalendar = new GregorianCalendar();
        monthCalendar.setTime(day.getNonConvertedDate());
        monthCalendar.set(Calendar.AM_PM, 0);
        monthCalendar.set(Calendar.HOUR, 0);
        monthCalendar.set(Calendar.MINUTE, 0);
        monthCalendar.set(Calendar.SECOND, 0);
        monthCalendar.set(Calendar.MILLISECOND, 0);

        List<Object[]> list = taskManager.getMyCalendarDayEvents(monthCalendar.getTime(), edsUser.getObjectID());
        if (list == null || list.size() <= 0) {
            return result;
        }
        List<DashboardMyCalendarDetailItem> tasks = new ArrayList<>();
        List<DashboardMyCalendarDetailItem> events = new ArrayList<>();
        List<DashboardMyCalendarDetailItem> meetings = new ArrayList<>();

        for (Object[] objects : list) {
            if (objects == null || objects.length <= 1) {
                continue;
            }
            DashboardMyCalendarDetailItem item = new DashboardMyCalendarDetailItem();
            item.setName((String) objects[0]);
            item.setDescription((String) objects[1]);
            item.setStartDate((Date) objects[2]);
            item.setEndDate((Date) objects[3]);
            item.setType((String) objects[4]);
            item.setObjectId((Integer) objects[5]);
            if (DashboardMyCalendarCarouselItem.CALL.equals(item.getType())) {
                events.add(item);
            } else if (DashboardMyCalendarCarouselItem.TASK.equals(item.getType())) {
                tasks.add(item);
            } else if (DashboardMyCalendarCarouselItem.MEETING.equals(item.getType())) {
                meetings.add(item);
            }
        }
        if (tasks.size() > 0 && events.size() > 0 && meetings.size() > 0) {
            result.add(tasks.get(0));
            result.add(events.get(0));
            result.add(meetings.get(0));
        } else if (events.size() > 0 && meetings.size() > 0) {
            calendarEventResult(events, meetings, result);
        } else if (tasks.size() > 0 && meetings.size() > 0) {
            calendarEventResult(tasks, meetings, result);
        } else if (tasks.size() > 0 && events.size() > 0) {
            calendarEventResult(tasks, events, result);
        } else {
            result.addAll(tasks);
            result.addAll(events);
            result.addAll(meetings);
        }
        if (result.size() > 0) {
            result = result.stream().sorted(Comparator.comparing(DashboardMyCalendarDetailItem::getStartDate)).collect(Collectors.toCollection(ArrayList::new));
        }
        if (result.size() > 3) {
            result = Lists.newArrayList(Lists.partition(result, 3).get(0));
        }
        return result;
    }

    private void calendarEventResult(List<DashboardMyCalendarDetailItem> first,
                                     List<DashboardMyCalendarDetailItem> second,
                                     List<DashboardMyCalendarDetailItem> result) {
        if (first.size() <= 2) {
            result.addAll(first);
        } else {
            result.add(first.get(0));
            result.add(first.get(1));
        }
        if (result.size() < 2) {
            if (second.size() <= 2) {
                result.addAll(second);
            } else if (second.size() == 3) {
                result.add(second.get(0));
                result.add(second.get(1));
            } else {
                result.add(second.get(0));
            }
        } else {
            result.add(second.get(0));
        }
    }

    @Override
    public Integer saveMyCalendarItem(DashboardMyCalendarDetailItem item) {
        if (item == null || item.getType() == null) {
            return 0;
        }
        EdsUser edsUser = userManager.getUser();
        // crm event
        if (DashboardMyCalendarCarouselItem.CALL.equals(item.getType())
                || DashboardMyCalendarCarouselItem.MEETING.equals(item.getType())) {

            Appointment appointment = new Appointment();
            appointment.setAction(Appointment.ADD_NEW_EVENT);
            if (DashboardMyCalendarCarouselItem.CALL.equals(item.getType())) {
                appointment.setActivityType(Appointment.CALL_LOG);
            } else {
                appointment.setActivityType(Appointment.EVENT);
                appointment.setInboundCall(false);
            }
            appointment.setSubject(item.getName());
            appointment.setAllDay(false);
            appointment.setStartDate(item.getStartDate());
            appointment.setEndDate(item.getEndDate());
            appointment.setRegisterNestedWorkflowEvents(false);
            appointment.setCreatedFrom(Appointment.FROM_CRM);
            googleCalendarServiceLocal.saveCalendarEvent(null, appointment, false);
        } else {
            //task

            TaskSingleItem[] taskItem = new TaskSingleItem[1];
            taskItem[0] = new TaskSingleItem();
            taskItem[0].setName(item.getName());
            taskItem[0].setStartDate(item.getStartDate());
            taskItem[0].setDueDate(item.getEndDate());
            taskItem[0].setStatusID(NOT_STARTED);
            taskItem[0].setAllDay(true);
            taskItem[0].setBillable(true);
            if (edsUser != null) {
                taskItem[0].setEmployeeID(edsUser.getObjectID());
            }
            EdsReference mediumStatus = referenceManager.findReference(EdsTask.TASK_PRIORITY, EdsTask.MEDIUM);
            if (mediumStatus != null) {
                taskItem[0].setPriorityID(mediumStatus.getObjectID());
            }
            MultiTaskList multiTaskList = new MultiTaskList();
            if (edsUser != null) {
                EdsProject edsProject = edsUser.getCompany().getDefaultProject();
                if (edsProject != null) {
                    multiTaskList.setProjectID(edsProject.getObjectID());
                    taskItem[0].setProjectID(edsProject.getObjectID());
                }
            }
            multiTaskList.setTaskSingleItems(taskItem);
            taskServiceLocal.saveMultipleTask(multiTaskList);
        }
        return 1;
    }

    public ChartData getCompanyPaymentDeductionData() {
        DateNonConvertable fsYearNonConvertable = commonServiceLocal.getFinancialYearStart();
        Date financialStartYear = fsYearNonConvertable != null ? fsYearNonConvertable.getNonConvertedDate() : ServerUtils.getCompanyDate(new Date(), userManager.getUser().getCompany());

        DateNonConvertable toDate = new DateNonConvertable();
        Calendar calToDate = Calendar.getInstance();
        calToDate.setTime(toDate.getNonConvertedDate());

        Calendar calFSYear = Calendar.getInstance();
        calFSYear.setTime(ServerUtils.getStartDate(financialStartYear));
        calFSYear.set(Calendar.YEAR, calToDate.get(Calendar.YEAR));

        while (calFSYear.getTime().after(toDate.getNonConvertedDate())) {
            calFSYear.add(Calendar.YEAR, -1);
        }

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSelectedMonth(calFSYear.get(Calendar.MONTH));
        fp.setSelectedYear(calFSYear.get(Calendar.YEAR));

        LinkedHashMap<Integer, BigDecimal> payments = payslipPaymentsManager.getPayrollYTDChartData(fp, true);
        LinkedHashMap<Integer, BigDecimal> deductions = payslipPaymentsManager.getPayrollYTDChartData(fp, false);
        LinkedHashMap<Integer, BigDecimal> expenses = payslipPaymentsManager.getPayrollYTDChartDataExpenses(fp);

        ArrayList<Number> paymentNumbers = new ArrayList<>();
        ArrayList<Number> deductionNumbers = new ArrayList<>();
        ArrayList<Number> netNumbers = new ArrayList<>();

        if (payments.isEmpty() && deductions.isEmpty()) {
            return null;
        }

        ChartData chartData = new ChartData();

        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setType(ChartTypeEnum.VERTICAL_BAR_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");

        while (calFSYear.getTime().compareTo(toDate.getNonConvertedDate()) <= 0) {
            categories.add(dateFormat.format(calFSYear.getTime()));

            Integer monthId = calFSYear.get(Calendar.MONTH);

            BigDecimal paymentsAmount = BigDecimal.ZERO;
            if (payments.get(monthId) != null) {
                paymentsAmount = payments.get(monthId);
            }

            if (expenses.get(monthId) != null) {
                paymentsAmount = paymentsAmount.add(expenses.get(monthId));
            }

            BigDecimal deductionsAmount = BigDecimal.ZERO;
            if (deductions.get(monthId) != null) {
                deductionsAmount = deductions.get(monthId);
            }

            paymentNumbers.add(paymentsAmount);
            deductionNumbers.add(deductionsAmount);
            netNumbers.add(paymentsAmount.subtract(deductionsAmount));

            calFSYear.add(Calendar.MONTH, 1);
        }
        chartData.setCategories(categories);

        LinkedList<SerieData> series = new LinkedList<>();

        //Payments
        SerieData pserie = new SerieData();
        pserie.setName(commonLocalizer.localize("payments", "Payments"));
//        pserie.setColor("#85CA40");
        pserie.setValues(paymentNumbers.toArray(new Number[]{}));
        series.add(pserie);

        //Deductions
        SerieData dserie = new SerieData();
        dserie.setName(commonLocalizer.localize("deductions", "Deductions"));
//        dserie.setColor("#8D9BA8");
        dserie.setValues(deductionNumbers.toArray(new Number[]{}));
        series.add(dserie);

        //Net
        SerieData netSerie = new SerieData();
        netSerie.setName("Net");
//        netSerie.setColor("#f5a623");
        netSerie.setSerieType(ChartTypeEnum.LINE_CHART);
        netSerie.setValues(netNumbers.toArray(new Number[]{}));
        series.add(netSerie);

        chartData.setSeries(series);

        return chartData;
    }

    public ChartData getCompanyPaymentDeductionSampleData() {
        DateNonConvertable fsYearNonConvertable = commonServiceLocal.getFinancialYearStart();
        Date financialStartYear = fsYearNonConvertable != null ? fsYearNonConvertable.getNonConvertedDate() : ServerUtils.getCompanyDate(new Date(), userManager.getUser().getCompany());

        DateNonConvertable toDate = new DateNonConvertable();
        Calendar calToDate = Calendar.getInstance();
        calToDate.setTime(toDate.getNonConvertedDate());

        Calendar calFSYear = Calendar.getInstance();
        calFSYear.setTime(ServerUtils.getStartDate(financialStartYear));
        calFSYear.set(Calendar.YEAR, calToDate.get(Calendar.YEAR));

        while (calFSYear.getTime().after(toDate.getNonConvertedDate())) {
            calFSYear.add(Calendar.YEAR, -1);
        }

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSelectedMonth(calFSYear.get(Calendar.MONTH));
        fp.setSelectedYear(calFSYear.get(Calendar.YEAR));

        ArrayList<Number> paymentNumbers = new ArrayList<>();
        ArrayList<Number> deductionNumbers = new ArrayList<>();
        ArrayList<Number> netNumbers = new ArrayList<>();

        ChartData chartData = new ChartData();

        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setType(ChartTypeEnum.VERTICAL_BAR_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");

        while (calFSYear.getTime().compareTo(toDate.getNonConvertedDate()) <= 0) {
            categories.add(dateFormat.format(calFSYear.getTime()));

            BigDecimal paymentsAmount = BigDecimal.ZERO;
            BigDecimal pAmount = randomizeChartData(4500);
            paymentsAmount = paymentsAmount.add(pAmount);
            paymentNumbers.add(pAmount);

            BigDecimal deductionsAmount = BigDecimal.ZERO;
            BigDecimal dAmount = randomizeChartData(2000);
            deductionsAmount = deductionsAmount.add(dAmount);
            deductionNumbers.add(dAmount);

            netNumbers.add(paymentsAmount.subtract(deductionsAmount));

            calFSYear.add(Calendar.MONTH, 1);
        }
        chartData.setCategories(categories);

        LinkedList<SerieData> series = new LinkedList<>();

        //Payments
        SerieData pserie = new SerieData();
        pserie.setName(commonLocalizer.localize("payments", "Payments"));
//        pserie.setColor("#85CA40");
        pserie.setValues(paymentNumbers.toArray(new Number[]{}));
        series.add(pserie);

        //Deductions
        SerieData dserie = new SerieData();
        dserie.setName(commonLocalizer.localize("deductions", "Deductions"));
//        dserie.setColor("#8D9BA8");
        dserie.setValues(deductionNumbers.toArray(new Number[]{}));
        series.add(dserie);

        //Net
        SerieData netSerie = new SerieData();
        netSerie.setName("Net");
//        netSerie.setColor("#f5a623");
        netSerie.setSerieType(ChartTypeEnum.LINE_CHART);
        netSerie.setValues(netNumbers.toArray(new Number[]{}));
        series.add(netSerie);

        chartData.setSeries(series);

        return chartData;
    }

    public ChartData getEmployeePaymentDeductionData() {

        EdsUser user = userManager.getUser();

        DateNonConvertable fsYearNonConvertable = commonServiceLocal.getFinancialYearStart();
        Date financialStartYear = fsYearNonConvertable != null ? fsYearNonConvertable.getNonConvertedDate() : ServerUtils.getCompanyDate(new Date(), userManager.getUser().getCompany());

        DateNonConvertable toDate = new DateNonConvertable();
        Calendar calToDate = Calendar.getInstance();
        calToDate.setTime(toDate.getNonConvertedDate());

        Calendar calFSYear = Calendar.getInstance();
        calFSYear.setTime(ServerUtils.getStartDate(financialStartYear));
        calFSYear.set(Calendar.YEAR, calToDate.get(Calendar.YEAR));

        while (calFSYear.getTime().after(toDate.getNonConvertedDate())) {
            calFSYear.add(Calendar.YEAR, -1);
        }

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSelectedMonth(calFSYear.get(Calendar.MONTH));
        fp.setSelectedYear(calFSYear.get(Calendar.YEAR));
        fp.setEmployeeId(userManager.getUser().getObjectID());

        LinkedHashMap<String, BigDecimal> payments = payslipPaymentsManager.getEmployeePayrollYTDChartData(fp, true);
        LinkedHashMap<String, BigDecimal> deductions = payslipPaymentsManager.getEmployeePayrollYTDChartData(fp, false);
        LinkedHashMap<Integer, BigDecimal> expenses = payslipPaymentsManager.getEmployeePayrollYTDChartDataExpenses(fp);

        HashMap<String, ArrayList<String>> allCategories = payslipPaymentsManager.getPayrollYTDChartDataCategories(fp);

        LinkedHashMap<String, ArrayList<Number>> paymentNumbers = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<Number>> deductionNumbers = new LinkedHashMap<>();
        ArrayList<Number> netNumbers = new ArrayList<>();

        if (payments.isEmpty() && deductions.isEmpty() || user.hasRole(EdsRole.ESS_USER_CODE)) {
            return null;
        }

        ChartData chartData = new ChartData();

        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setType(ChartTypeEnum.VERTICAL_BAR_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartConf.setStacked(StackedEnum.BY_VALUE);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");

        while (calFSYear.getTime().compareTo(toDate.getNonConvertedDate()) <= 0) {
            categories.add(dateFormat.format(calFSYear.getTime()));

            Integer monthId = calFSYear.get(Calendar.MONTH);

            BigDecimal paymentsAmount = BigDecimal.ZERO;
            if (allCategories.get(EdsPayrollCategory.PAYMENT) != null) {
                for (String categoryCode : allCategories.get(EdsPayrollCategory.PAYMENT)) {
                    ArrayList<Number> numbers = paymentNumbers.getOrDefault(categoryCode, new ArrayList<>());

                    BigDecimal amount = BigDecimal.ZERO;
                    if (payments.get(categoryCode + monthId) != null) {
                        amount = payments.get(categoryCode + monthId);
                        paymentsAmount = paymentsAmount.add(amount);
                    }

                    numbers.add(amount);
                    paymentNumbers.put(categoryCode, numbers);
                }
            }

            BigDecimal deductionsAmount = BigDecimal.ZERO;
            if (allCategories.get(EdsPayrollCategory.DEDUCTION) != null) {
                for (String categoryCode : allCategories.get(EdsPayrollCategory.DEDUCTION)) {
                    ArrayList<Number> numbers = deductionNumbers.getOrDefault(categoryCode, new ArrayList<>());

                    BigDecimal amount = BigDecimal.ZERO;
                    if (deductions.get(categoryCode + monthId) != null) {
                        amount = deductions.get(categoryCode + monthId);
                        deductionsAmount = deductionsAmount.add(amount);
                    }

                    numbers.add(amount);
                    deductionNumbers.put(categoryCode, numbers);
                }
            }

            BigDecimal expense = BigDecimal.ZERO;
            if (expenses.get(monthId) != null) {
                expense = expenses.get(monthId);
            }

            netNumbers.add(paymentsAmount.add(expense).subtract(deductionsAmount));

            calFSYear.add(Calendar.MONTH, 1);
        }
        chartData.setCategories(categories);

        //Payments
        List<SerieData> pSeries = new ArrayList<>();
        Color color = new Color(0x85CA40);
        for (Map.Entry<String, ArrayList<Number>> paymentNumber : paymentNumbers.entrySet()) {
            SerieData serie = new SerieData();
            serie.setName(payrollCategoryManager.getCategoryByCode(paymentNumber.getKey()).getName());
            serie.setColor(color.getHexColor());
            serie.setValues(paymentNumber.getValue().toArray(new Number[]{}));
            serie.setStack("payment");

            color = color.brighter();
            pSeries.add(serie);
        }
        Collections.reverse(pSeries);

        LinkedList<SerieData> series = new LinkedList<>(pSeries);

        //Deductions
        List<SerieData> dSeries = new ArrayList<>();
        color = new Color(0x8D9BA8);
        for (Map.Entry<String, ArrayList<Number>> deductionNumber : deductionNumbers.entrySet()) {
            SerieData serie = new SerieData();
            serie.setName(payrollCategoryManager.getCategoryByCode(deductionNumber.getKey()).getName());
            serie.setColor(color.getHexColor());
            serie.setValues(deductionNumber.getValue().toArray(new Number[]{}));
            serie.setStack("deduction");

            color = color.brighter();
            dSeries.add(serie);
        }
        Collections.reverse(dSeries);

        series.addAll(dSeries);

        //Net
        SerieData netSerie = new SerieData();
        netSerie.setName("Net");
        netSerie.setColor("#f5a623");
        netSerie.setSerieType(ChartTypeEnum.LINE_CHART);
        netSerie.setValues(netNumbers.toArray(new Number[]{}));

        series.add(netSerie);

        chartData.setSeries(series);

        return chartData;
    }

    public ChartData getEmployeePaymentDeductionSampleData() {
        DateNonConvertable fsYearNonConvertable = commonServiceLocal.getFinancialYearStart();
        Date financialStartYear = fsYearNonConvertable != null ? fsYearNonConvertable.getNonConvertedDate() : ServerUtils.getCompanyDate(new Date(), userManager.getUser().getCompany());

        DateNonConvertable toDate = new DateNonConvertable();
        Calendar calToDate = Calendar.getInstance();
        calToDate.setTime(toDate.getNonConvertedDate());

        Calendar calFSYear = Calendar.getInstance();
        calFSYear.setTime(ServerUtils.getStartDate(financialStartYear));
        calFSYear.set(Calendar.YEAR, calToDate.get(Calendar.YEAR));

        while (calFSYear.getTime().after(toDate.getNonConvertedDate())) {
            calFSYear.add(Calendar.YEAR, -1);
        }

        HashMap<String, BigDecimal> paymentCategories = new HashMap<>();
        paymentCategories.put(commonLocalizer.localize("basicSalary", "Basic Salary"), new BigDecimal(4500));
        paymentCategories.put(commonLocalizer.localize("disbursement", "Disbursement"), new BigDecimal(1200));

        HashMap<String, BigDecimal> deductionCategories = new HashMap<>();
        deductionCategories.put(commonLocalizer.localize("loans", "Loans"), new BigDecimal(1000));
        deductionCategories.put(commonLocalizer.localize("cashAdvance", "Cash Advance"), new BigDecimal(1500));

        LinkedHashMap<String, ArrayList<Number>> paymentNumbers = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<Number>> deductionNumbers = new LinkedHashMap<>();
        ArrayList<Number> netNumbers = new ArrayList<>();

        ChartData chartData = new ChartData();
        ChartConfItem chartConf = new ChartConfItem();
        chartConf.setType(ChartTypeEnum.VERTICAL_BAR_CHART);
        chartConf.setLegend(LegendPositionEnum.BOTTOM);
        chartConf.setStacked(StackedEnum.BY_VALUE);
        chartData.setConf(chartConf);

        LinkedList<String> categories = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");

        boolean leaveSalaryAdded = false;
        while (calFSYear.getTime().compareTo(toDate.getNonConvertedDate()) <= 0) {
            categories.add(dateFormat.format(calFSYear.getTime()));

            BigDecimal paymentsAmount = BigDecimal.ZERO;
            for (Map.Entry<String, BigDecimal> category : paymentCategories.entrySet()) {
                ArrayList<Number> numbers = paymentNumbers.getOrDefault(category.getKey(), new ArrayList<>());

                BigDecimal amount = randomizeChartData(category.getValue().doubleValue());

                paymentsAmount = paymentsAmount.add(amount);
                numbers.add(amount);

                paymentNumbers.put(category.getKey(), numbers);
            }

            //Munir's decision
            if (!leaveSalaryAdded) {
                String leaveSalary = commonLocalizer.localize("leaveSalary", "Leave Salary");
                ArrayList<Number> numbers = paymentNumbers.getOrDefault(leaveSalary, new ArrayList<>());

                BigDecimal amount = randomizeChartData(1500);

                paymentsAmount = paymentsAmount.add(amount);
                numbers.add(amount);

                paymentNumbers.put(leaveSalary, numbers);

                leaveSalaryAdded = true;
            }

            BigDecimal deductionsAmount = BigDecimal.ZERO;
            for (Map.Entry<String, BigDecimal> category : deductionCategories.entrySet()) {
                ArrayList<Number> numbers = deductionNumbers.getOrDefault(category.getKey(), new ArrayList<>());

                BigDecimal amount = randomizeChartData(category.getValue().doubleValue());

                deductionsAmount = deductionsAmount.add(amount);
                numbers.add(amount);

                deductionNumbers.put(category.getKey(), numbers);
            }

            netNumbers.add(paymentsAmount.subtract(deductionsAmount));

            calFSYear.add(Calendar.MONTH, 1);
        }
        chartData.setCategories(categories);

        //Payments
        List<SerieData> pSeries = new ArrayList<>();
        Color color = new Color(0x85CA40);
        for (Map.Entry<String, ArrayList<Number>> paymentNumber : paymentNumbers.entrySet()) {
            SerieData serie = new SerieData();
            serie.setName(paymentNumber.getKey());
            serie.setColor(color.getHexColor());
            serie.setValues(paymentNumber.getValue().toArray(new Number[]{}));
            serie.setStack("payment");

            color = color.brighter();
            pSeries.add(serie);
        }
        Collections.reverse(pSeries);

        LinkedList<SerieData> series = new LinkedList<>(pSeries);

        //Deductions
        List<SerieData> dSeries = new ArrayList<>();
        color = new Color(0x8D9BA8);
        for (Map.Entry<String, ArrayList<Number>> deductionNumber : deductionNumbers.entrySet()) {
            SerieData serie = new SerieData();
            serie.setName(deductionNumber.getKey());
            serie.setColor(color.getHexColor());
            serie.setValues(deductionNumber.getValue().toArray(new Number[]{}));
            serie.setStack("deduction");

            color = color.brighter();
            dSeries.add(serie);
        }
        Collections.reverse(dSeries);

        series.addAll(dSeries);

        //Net
        SerieData netSerie = new SerieData();
        netSerie.setName("Net");
        netSerie.setColor("#f5a623");
        netSerie.setSerieType(ChartTypeEnum.LINE_CHART);
        netSerie.setValues(netNumbers.toArray(new Number[]{}));

        series.add(netSerie);

        chartData.setSeries(series);

        return chartData;
    }

    private BigDecimal randomizeChartData(double amount) {
        double d = ThreadLocalRandom.current().nextDouble(-1, 1);
        return new BigDecimal(amount + (amount * d) / 2);
    }

    @Override
    public DashboardComboItem getComboData() {
        DashboardComboItem result = new DashboardComboItem();
        result.setWeatherItem(getWeather(false));
        result.setNewsItems(getNewsList());
        result.setBirthdayItems(getBirthdayList());
        result.setMessageItem(getUnreadMessageItem());
        return result;
    }

    @Override
    public DashboardWeatherItem getWeather(boolean isSample) {
        DashboardWeatherItem item = new DashboardWeatherItem();

        EdsUser edsUser = userManager.getUser();
        if (edsUser == null) {
            return item;
        }
        String locationId;
        if (isSample) {
            locationId = "Newark,USA";
        } else {
            locationId = getUserWeatherLocation();
        }
        if (StringUtils.isEmpty(locationId)) {
            locationId = "Newark,USA";
        }
        EdsUserEmailSettings edsUserSettings = userEmailSettingsManager.getUserSettings(edsUser);
        boolean isFahrenheit = false;
        if (edsUserSettings != null && edsUserSettings.isFahrenheit() != null) {
            isFahrenheit = edsUserSettings.isFahrenheit();
        }
        System.out.println(" -----------Weather Location is ------------------------------" + locationId);
        String responseContent = getWeatherRequest(locationId);
        if (responseContent == null || responseContent.isEmpty()) {
            responseContent = getWeatherRequest(locationId);
        }
        if (responseContent == null || responseContent.isEmpty()) {
            return item;
        }
        Gson gson = new Gson();
        WeatherResponse weatherResponse = gson.fromJson(responseContent, WeatherResponse.class);
        item.setLocation(weatherResponse.getName());
        if (weatherResponse.getWeather() != null && weatherResponse.getWeather().length > 0) {
            item.setDescription(weatherResponse.getWeather()[0].getMain());
            item.setIcon(weatherResponse.getWeather()[0].getIcon());
        }
        if (weatherResponse.getMain() != null) {
            item.setHumidity(weatherResponse.getMain().getHumidity().toString());
            item.setTemperature(weatherResponse.getMain().getTemp().toString());
            item.setTempMax(weatherResponse.getMain().getTemp_max().toString());
        }
        item.setFahrenheit(isFahrenheit);
        return item;
    }

    private String getUserWeatherLocation() {
        EdsUser user = userManager.getUser();
        EdsCountry userCountry = null;
        String userCity = "";

        EdsCompany company = user.getCompany();
        if (user.isClientContact() && user.getClientContact().getClientID() != null) {
            EdsCrmAccount client = clientManager.get(user.getClientContact().getClientID());
            EdsAddress billingAddress = client.getBillingAddress();
            if (billingAddress != null) {
                userCountry = billingAddress.getCountry();
                userCity = billingAddress.getCity();
            } else {
                userCountry = company.getCountryZone().getCountry();
                userCity = company.getCity();
            }
        } else if (user.getLocation() != null) {
            userCountry = user.getLocation().getCountry();
            userCity = user.getLocation().getCity();
        } else {
            userCountry = company.getCountryZone().getCountry();
            userCity = company.getCity();
        }
        if (userCountry == null) {
            return null;
        }
        String code = "";
        if (StringUtils.isNotEmpty(userCountry.getCode())) {
            code = "," + userCountry.getCode();
        }
        if (StringUtils.isNotEmpty(userCity)) {
            code = userCity + code;
        } else if (StringUtils.isNotEmpty(userCountry.getName())) {
            code = userCountry.getName() + code;
        }
        return code;
    }

    private String getWeatherRequest(String cityName) {
        try {
            String url = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName
                    + "&APPID=a02e20de4230b8b8ee812dc8ef579416&units=imperial";

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> httpRequest = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpRequest, String.class);

            return response.getBody();
        } catch (RestClientException e) {
            System.out.printf("---- Weather widget network connection problem or invalid server address, %s ----\n", e);
        }
        return null;
    }

    private ArrayList<DashboardNewsItem> getNewsList() {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_NEWS_CORE);

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(10);
        EdsUser user = newsManager.getUser();
        EdsCompany company = user.getCompany();
        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getWorkspaceNewsListCore(fp, user, company));
        query.setStart(fp.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(fp.getLimit()));
        query.setSort(SolrNewsRepresenter.FIELD_CREATION_DATE, SolrQuery.ORDER.desc);

        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        ArrayList<DashboardNewsItem> result = Lists.newArrayList();
        if (resp != null && resp.getResults() != null) {
            for (SolrDocument doc : resp.getResults()) {
                DashboardNewsItem item = new DashboardNewsItem();
                item.setObjectId(SolrUtils.asInteger(doc.getFieldValue(SolrNewsRepresenter.FIELD_NEWS_ID)));
                item.setSubject(SolrUtils.asString(doc.getFieldValue(SolrNewsRepresenter.FIELD_SUBJECT)));
                item.setPostedBy(SolrUtils.asString(doc.getFieldValue(SolrNewsRepresenter.FIELD_USER)));
                result.add(item);
            }
        }
        return result;
    }

    private ArrayList<DashboardBirthdayItem> getBirthdayList() {
        ListingFilterParameter fp = new ListingFilterParameter();

        fp.setStartDate(new Date());
        fp.setEndDate(new Date());

        ArrayList<DashboardBirthdayItem> result = Lists.newArrayList();
        List<EdsEmployee> edsTodayEmployees = crmContactManager.getBirthdayEmployees(fp);

        result.addAll(getBirthdayData(edsTodayEmployees));

        if (edsTodayEmployees == null || edsTodayEmployees.size() < 10) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DAY_OF_YEAR, -3);
            fp.setStartDate(calendar.getTime());
            fp.setEndDate(null);
            if (edsTodayEmployees != null) {
                fp.setObjectIDs(edsTodayEmployees.stream().map(EdsEmployee::getObjectID).collect(Collectors.toCollection(ArrayList::new)));
            }
            List<EdsEmployee> edsEmployees = crmContactManager.getBirthdayEmployees(fp);
            result.addAll(getBirthdayData(edsEmployees));
        }
        Calendar calendar = GregorianCalendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        result.sort(((o1, o2) -> {
            int months = (o1.getDate().getMonth() + (12 - currentMonth)) % 12
                    - (o2.getDate().getMonth() + (12 - currentMonth)) % 12;
            if (months == 0) {
                return o1.getDate().getDate() - o2.getDate().getDate();
            } else {
                return months;
            }
        }));
        return result;
    }

    private List<DashboardBirthdayItem> getBirthdayData(List<EdsEmployee> edsEmployees) {
        List<DashboardBirthdayItem> result = Lists.newArrayList();
        if (edsEmployees == null || edsEmployees.size() <= 0) {
            return result;
        }
        for (EdsEmployee edsEmployee : edsEmployees) {
            DashboardBirthdayItem item = new DashboardBirthdayItem();
            if (edsEmployee.getContact() == null) {
                continue;
            }
            EdsCrmContact edsCrmContact = edsEmployee.getContact();
            if (edsCrmContact.getDateOfBirth() == null) {
                continue;
            }
            item.setObjectId(edsCrmContact.getObjectID());
            item.setDate(edsCrmContact.getDateOfBirth());
            item.setFistName(edsEmployee.getFirstName());
            item.setLastName(edsEmployee.getLastName());
            item.setCurrentUserId(userManager.getUser().getObjectID());
            if (edsEmployee.getPosition() != null) {
                item.setPosition(edsEmployee.getPosition().getName());
            }
            if (edsCrmContact.getPhoto() != null) {
                item.setImageUrl(commonServiceLocal.getImageUrl(edsCrmContact.getPhoto().getObjectID()));
            }
            result.add(item);
        }
        return result;
    }

    private EmailAccountItem getUnreadMessageItem() {
        EmailAccountItem item = new EmailAccountItem();
        EdsEmailSetting edsDefaultEmail = emailSettingsManager.getUserEmailAccount();
        if (edsDefaultEmail != null) {
            item.setUnreadCount(emailRepository.getUnreadInboxMessageCount(edsDefaultEmail.getObjectID(), SecurityContext.getInstance().getCompanyId()));
            item.setObjectID(edsDefaultEmail.getObjectID());
            item.setEmail(edsDefaultEmail.getEmail());
        }
        return item;
    }

    public ChartData getLeaveRequestDays(ListingFilterParameter fp) {
        if (fp.getEmployeeId() == null) {
            fp.setEmployeeId(userManager.getUser().getObjectID());
        }
        LeaveRequestChartRpc data = availabilityServiceLocal.getLeaveRequestChartData(fp);
        ChartData result = new ChartData();
        LinkedList<String> categories = new LinkedList<>();
        result.setTextInCenter(data.getName());
        ChartConfItem config = new ChartConfItem();
        config.setType(ChartTypeEnum.VERTICAL_BAR_CHART);
        config.setLegend(LegendPositionEnum.BOTTOM);
        config.setScale("2");
        result.setConf(config);
//        for (String category : data.getTopNames().keySet()) {
//            categories.add(commonLocalizer.localize(data.getTopNames().get(category), category));
//        }
        result.setCategories(new LinkedList<>(data.getTopNames().keySet()));
        config.setStacked(StackedEnum.BY_VALUE);
        LinkedList<SerieData> series = new LinkedList<>();

        Double[] exceeds = data.getExceeded();
        boolean showExceed = false;
        for (int i = 0; i < exceeds.length; i++) {
            if (exceeds[i] != null && exceeds[i] > 0) {
                showExceed = true;
                break;
            }
        }
        if (showExceed) {
            SerieData exceed = new SerieData();
            exceed.setName(commonLocalizer.localize("exceeded"));
            exceed.setValues(data.getExceeded());
            exceed.setColor("#FD5050");
            exceed.setStack("paid");
            series.add(exceed);
        }

        Double[] lefts = data.getLeft();
        boolean showLeft = false;
        for (int i = 0; i < lefts.length; i++) {
            if (lefts[i] != null && lefts[i] > 0) {
                showLeft = true;
                break;
            }
        }

        if (showLeft) {
            SerieData left = new SerieData();
            left.setName(commonLocalizer.localize("leftDays"));
            left.setValues(data.getLeft());
            left.setColor("#FBA800");
            left.setStack("paid");
            series.add(left);
        }

        Double[] takens = data.getPaid();
        boolean showTaken = false;
        for (int i = 0; i < takens.length; i++) {
            if (takens[i] != null && takens[i] > 0) {
                showTaken = true;
                break;
            }
        }

        if (showTaken) {
            SerieData taken = new SerieData();
            taken.setName(commonLocalizer.localize("approved"));
            taken.setValues(data.getPaid());
            taken.setColor("#00C836");
            taken.setStack("paid");
            series.add(taken);
        }
        Double[] nonPaids = data.getNonPaid();
        boolean showNonPaid = false;
        for (int i = 0; i < nonPaids.length; i++) {
            if (nonPaids[i] != null && nonPaids[i] > 0) {
                showNonPaid = true;
                break;
            }
        }
        if (showNonPaid) {
            SerieData nonPaid = new SerieData();
            nonPaid.setName(commonLocalizer.localize("nonPaid"));
            nonPaid.setValues(data.getNonPaid());
            nonPaid.setColor("#8d9ba8");
            nonPaid.setStack("nonPaid");
            series.add(nonPaid);
        }

        result.setSeries(series);
        return result;
    }

    @Override
    public DashboardWeatherItem saveWeatherSettings(boolean isFahrenheit) {
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null) {
            return new DashboardWeatherItem();
        }
        EdsUserEmailSettings edsUserSettings = userEmailSettingsManager.getUserSettings(edsUser);
        if (edsUserSettings != null) {
            edsUserSettings.setFahrenheit(isFahrenheit);
            userEmailSettingsManager.update(edsUserSettings);
        }
        DashboardWeatherItem result = getWeather(false);
        result.setFahrenheit(isFahrenheit);
        return result;
    }

    public TimeslotItem getEmpTimeslot() {
        return availabilityServiceLocal.getEmpTimeslot(userManager.getUser().getObjectID());
    }

    @Override
    public KpiWidgetData getDynamicWidgetComponentData(DashboardComponentItem gridItemConfig) {
        return reportingSerivce.getKpiWidgetData(gridItemConfig);
    }

    @Override
    public ReportRpc getDynamicWidgetTitle(Integer reportId) {
        ReportRpc report = reportingSerivce.getReport(reportId);
        if (report != null) {
            EdsUser user = userManager.getUser();
            KpiWidgetItem kpiWidgetItem = report.getKpiWidgetItem();
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);

            if (userSettings != null && userSettings.getInternationalization() != null) {
                String userl = userSettings.getInternationalization();
                if (kpiWidgetItem.getLocalization() != null) {
                    switch (userl) {
                        case "en" -> kpiWidgetItem.setKpiWidgetTitle(kpiWidgetItem.getLocalization().getEnglishName());
                        case "ar" -> kpiWidgetItem.setKpiWidgetTitle(kpiWidgetItem.getLocalization().getArabicName());
                        case "ru" -> kpiWidgetItem.setKpiWidgetTitle(kpiWidgetItem.getLocalization().getRussianName());
                        case "uz" -> kpiWidgetItem.setKpiWidgetTitle(kpiWidgetItem.getLocalization().getUzbekName());
                    }
                    report.setKpiWidgetItem(kpiWidgetItem);
                }
                if (kpiWidgetItem.getSuffixLocalization() != null) {
                    switch (userl) {
                        case "en" -> kpiWidgetItem.setKpiWidgetSuffix(kpiWidgetItem.getSuffixLocalization().getEnglishName());
                        case "ar" -> kpiWidgetItem.setKpiWidgetSuffix(kpiWidgetItem.getSuffixLocalization().getArabicName());
                        case "ru" -> kpiWidgetItem.setKpiWidgetSuffix(kpiWidgetItem.getSuffixLocalization().getRussianName());
                        case "uz" -> kpiWidgetItem.setKpiWidgetSuffix(kpiWidgetItem.getSuffixLocalization().getUzbekName());
                    }
                    report.setKpiWidgetItem(kpiWidgetItem);
                }
                if (kpiWidgetItem.getDifferenceLocalization() != null) {
                    switch (userl) {
                        case "en" -> kpiWidgetItem.setDifferentTitle(kpiWidgetItem.getDifferenceLocalization().getEnglishName());
                        case "ar" -> kpiWidgetItem.setDifferentTitle(kpiWidgetItem.getDifferenceLocalization().getArabicName());
                        case "ru" -> kpiWidgetItem.setDifferentTitle(kpiWidgetItem.getDifferenceLocalization().getRussianName());
                        case "uz" -> kpiWidgetItem.setDifferentTitle(kpiWidgetItem.getDifferenceLocalization().getUzbekName());
                    }
                    report.setKpiWidgetItem(kpiWidgetItem);
                }
                if (kpiWidgetItem.getComparisonLocalization() != null) {
                    switch (userl) {
                        case "en" -> kpiWidgetItem.setComparisionText(kpiWidgetItem.getComparisonLocalization().getEnglishName());
                        case "ar" -> kpiWidgetItem.setComparisionText(kpiWidgetItem.getComparisonLocalization().getArabicName());
                        case "ru" -> kpiWidgetItem.setComparisionText(kpiWidgetItem.getComparisonLocalization().getRussianName());
                        case "uz" -> kpiWidgetItem.setComparisionText(kpiWidgetItem.getComparisonLocalization().getUzbekName());
                    }
                    report.setKpiWidgetItem(kpiWidgetItem);
                }
            }
            if (kpiWidgetItem == null) {
                kpiWidgetItem = new KpiWidgetItem();
                kpiWidgetItem.setKpiWidgetTitle("No Widget Title");
            }
        }
        return report;
    }

    public ChartConfItem getDynamicComponentTitle(Integer reportId) {
        ChartConfItem chrtConf = reportingSerivce.getReport(reportId).getChartConf();

        EdsUser user = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);

        if (userSettings != null && userSettings.getInternationalization() != null) {
            String userl = userSettings.getInternationalization();
            if (chrtConf.getLocalization() != null) {
                String result = switch (userl) {
                    case "en" -> chrtConf.getLocalization().getEnglishName();
                    case "ar" -> chrtConf.getLocalization().getArabicName();
                    case "ru" -> chrtConf.getLocalization().getRussianName();
                    case "uz" -> chrtConf.getLocalization().getUzbekName();
                    default -> "";
                };
                if (!result.isEmpty()) {
                    chrtConf.setTitle(result);
                }
            }
        }
        if (chrtConf == null) {
            chrtConf = new ChartConfItem();
            chrtConf.setTitle("No Chart Title");
            chrtConf.setHasPermission(false);
        }

        return chrtConf;
    }

}
