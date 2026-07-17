package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsCityDistrict;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.gwt.assessment.server.app.AssessmentServiceLocal;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.NewPosition;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.AttendanceTerminalManager;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CityOrRegionManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.ContractManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FeaturesManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.UserBankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.location.server.LocationServiceLocal;
import com.edatasite.workforce.gwt.news.server.NewsServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service("reportService")
public class ReportServiceImpl implements ReportService, Constants, Errors {
    private static final String EMAIL_TEMPLATE_CATEGORY = "_EMAIL_TEMPLATE_CATEGORY";

    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource wfmMessageSource;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    @Qualifier("locationService")
    private LocationServiceLocal locationServiceLocal;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private CommonServiceLocal commonService;
    @Autowired
    private AttendanceTerminalManager attendanceTerminalManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private FeaturesManager featuresManager;
    @Autowired
    @Qualifier("availabilityService")
    AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private AssessmentServiceLocal assessmentService;
    @Autowired
    private NewsServiceLocal newsServiceLocal;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private UserBankAccountManager userBankAccountManager;
    @Autowired
    private ContractManager contractManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    @Qualifier("cityDistrictManager")
    private CityOrRegionManager cityOrRegionManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getClientsList(Integer projectId, Integer departmentId,
                                       Integer employeeId, Integer viewAsId) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(viewAsId);
        fp.setProjectId(projectId);
        fp.setDepartmentId(departmentId);
        fp.setEmployeeId(employeeId);
        SelectItem[] clients = clientManager.list(fp);
        Arrays.sort(clients, Comparator.comparing(SelectItem::getName));
        return clients;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPriorities() {
        return commonService.convertReference2SelectItem(EdsTask.TASK_PRIORITY, false, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getClientList() {
        List<EdsCrmAccount> clients = crmAccountManager.getList(null, EdsCrmAccount.CUSTOMER);

        clients.sort(Comparator.comparing(EdsCrmAccount::getName));

        SelectItem[] result = new SelectItem[clients.size()];

        int i = 0;
        for (EdsCrmAccount client : clients) {
            result[i] = new SelectItem(client.getObjectID(), client.getName());
            i++;
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getDepartmentList(Integer clientId, Integer projectId,
                                          Integer employeeId, Integer viewAsId) {
        EdsUser user = employeeManager.getUser();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(viewAsId);
        fp.setClientId(clientId);
        fp.setProjectId(projectId);
        fp.setEmployeeId(employeeId);

        if (fp.getViewAsId() == null || fp.getViewAsId() == 0) {
            EdsRole maximumRole = user.getRolesSortedByPattern().get(0);
            fp.setViewAsId(maximumRole.getObjectID());
        }
        List<EdsDepartment> teams = departmentManager.list(fp);
        SelectItem[] result = new SelectItem[teams.size()];

        int i = 0;
        for (EdsDepartment team : teams) {
            result[i] = new SelectItem();
            result[i].setId(team.getObjectID());
            result[i].setName(team.getName());
            i++;
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmployeesList(Integer clientId, Integer projectId, Integer departmentId, Integer viewAsId) {

        boolean isTimesheetReportFieldsWithDropdown = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TIMESHEET_REPORT_FIELDS_WITH_DROPDOWN);
        EdsCrmAccount client = null;
        if (clientId != null) {
            client = crmAccountManager.get(clientId);
        }
        EdsProject project = null;
        if (projectId != null) {
            project = projectManager.get(projectId);
        }
        EdsDepartment department = null;
        if (departmentId != null) {
            department = departmentManager.get(departmentId);
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(viewAsId);
        fp.setClientId(clientId);
        fp.setProjectId(projectId);
        fp.setDepartmentId(departmentId);

        List<EdsEmployee> employees = null;
        if (fp.getViewAsId() != null && EdsRole.MEM.equals(fp.getViewAsId())) {
            EdsUser user = employeeManager.getUser();
            employees = new ArrayList<>();
            employees.add(employeeManager.get(user.getObjectID()));
        } else {
            employees = employeeManager.list(fp);
        }

        SelectItem[] empList = new SelectItem[employees.size()];
        int i = 0;
        for (EdsEmployee employee : employees) {
            if (employee != null) {
                empList[i] = new SelectItem();
                empList[i].setId(employee.getObjectID());
                if (employee.getProfile() != null && !isTimesheetReportFieldsWithDropdown) {
                    empList[i].setName((employee.getProfile().getEmployeeCode() != null ? employee.getProfile().getEmployeeCode() + " - " : "") + employee.getName());
                } else {
                    empList[i].setName(employee.getName());
                }
            }
            i++;
        }
        return empList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmployeesListForRU(Integer clientId, Integer projectId, Integer departmentId) {
        EdsCrmAccount client = null;
        if (clientId != null) {
            client = crmAccountManager.get(clientId);
        }
        EdsProject project = null;
        if (projectId != null) {
            project = projectManager.get(projectId);
        }
        EdsDepartment department = null;
        if (departmentId != null) {
            department = departmentManager.get(departmentId);
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setClientId(clientId);
        fp.setProjectId(projectId);
        fp.setDepartmentId(departmentId);

        List<EdsEmployee> employees = employeeManager.list(fp, true);

        SelectItem[] empList = new SelectItem[employees.size()];
        int i = 0;
        for (EdsEmployee employee : employees) {
            if (employee != null) {
                empList[i] = new SelectItem();
                empList[i].setId(employee.getObjectID());
                empList[i].setName(employee.getName());
            }
            i++;
        }
        return empList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmployeesList(Integer companyId) {
        List<EdsEmployee> employees = employeeManager.empList(companyId);
        SelectItem[] empList = new SelectItem[employees.size()];
        int i = 0;
        for (EdsEmployee employee : employees) {
            if (employee != null) {
                empList[i] = new SelectItem();
                empList[i].setId(employee.getObjectID());
                empList[i].setName(employee.getName());
            }
            i++;
        }
        return empList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTaskEmployeesList(Integer taskId) {
        if (taskId != null) {
            TaskInvolvedMember[] members = timeSheetManager.getSumTimeSheets(taskId).toArray(new TaskInvolvedMember[]{});
            SelectItem[] empList = new SelectItem[members.length];
            int i = 0;
            for (TaskInvolvedMember member : members) {
                if (member != null) {
                    empList[i] = new SelectItem();
                    empList[i].setId(member.getAssignEmployeeID());
                    empList[i].setName(member.getEmployeeNumber() + " - " + member.getEmployee());
                }
                i++;
            }
            return empList;
        }
        return new SelectItem[0];
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getProjectStatusIDAll() {
        EdsReference all = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ALL);
        if (all != null) {
            return all.getObjectID();
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProjectListForReport(Integer clientId, Integer departmentId, Integer employeeId, Integer viewAsId, Integer statusId) {

        boolean isTimesheetReportFieldsWithDropdown = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TIMESHEET_REPORT_FIELDS_WITH_DROPDOWN);
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setClientId(clientId);
        fp.setDepartmentId(departmentId);
        fp.setEmployeeId(employeeId);
        fp.setViewAsId(viewAsId);
        fp.setProjectStatusId(statusId);

        List<EdsProject> projects = projectManager.list(fp);

        SelectItem[] result = new SelectItem[projects.size()];

        int i = 0;
        for (EdsProject project : projects) {
            if (isTimesheetReportFieldsWithDropdown && project.getNumber() != null && !"null".equalsIgnoreCase(project.getNumber())) {
                result[i] = new SelectItem(project.getObjectID(), project.getName());
            } else {
                result[i] = new SelectItem(project.getObjectID(), project.getNumber() + " - " + project.getName());
            }
            i++;
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProjectListForReport(Integer projectId, String startDateS, String endDateS, Integer clientId,
                                                Integer departmentId, Integer employeeId, Integer statusId) {
        Date startDate = new Date(Integer.parseInt(startDateS.split("-")[0]) - 1900, Integer.parseInt(startDateS.split("-")[1]) - 1, Integer.parseInt(startDateS.split("-")[2]), 0, 0, 0);
        Date endDate = new Date(Integer.parseInt(endDateS.split("-")[0]) - 1900, Integer.parseInt(endDateS.split("-")[1]) - 1, Integer.parseInt(endDateS.split("-")[2]),
                Integer.parseInt(endDateS.split("-")[3]), Integer.parseInt(endDateS.split("-")[4]), Integer.parseInt(endDateS.split("-")[5]));
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setClientId(clientId);
        fp.setDepartmentId(departmentId);
        fp.setEmployeeId(employeeId);
        fp.setProjectStatusId(statusId);
        fp.setStartDate(startDate);
        fp.setEndDate(endDate);
        fp.setProjectId(projectId);

        List<EdsProject> projects = projectManager.projectsList(fp);

        SelectItem[] result = new SelectItem[projects.size()];

        int i = 0;
        for (EdsProject project : projects) {
            result[i] = new SelectItem(project.getObjectID(), project.getName());
            i++;
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRoleList() {
        EdsUser user = referenceManager.getUser();
        if (user != null) {
            List<EdsRole> roleList = new LinkedList<>(user.getRolesSorted());
            SelectItem[] result = new SelectItem[roleList.size()];
            int i = 0;
            for (EdsRole role : roleList) {
                result[i] = new SelectItem();
                result[i].setId(role.getObjectID());
                result[i].setName(referenceWfmMessageSource.localize(role.getCode(), role.getName()));
                i++;
            }
            return result;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getUserMaxRolesWithMEM() {
        EdsUser user = referenceManager.getUser();
        List<EdsRole> userRoles = new LinkedList<>(user.getRoles());
        List<Integer> userMaxRoleIDs = new ArrayList<>();
        for (EdsRole roleIds : userRoles) {
            userMaxRoleIDs.add(roleIds.getObjectID());
        }
        Integer userMaxRoleID = ServerUtils.getUserRolesSorted(userMaxRoleIDs).get(0);
        List<EdsRole> userMaxRoles = new LinkedList<>();
        EdsRole ro = roleManager.get(userMaxRoleID);
        EdsRole mem = roleManager.get(EdsRole.MEM);
        boolean hasMemRole = user.getRoles().contains(mem);
        userMaxRoles.add(ro);
        if (hasMemRole && !mem.getObjectID().equals(ro.getObjectID())) {
            userMaxRoles.add(mem);
        }

        SelectItem[] result = new SelectItem[userMaxRoles.size()];
        int i = 0;
        for (EdsRole maxRo : userMaxRoles) {
            result[i] = new SelectItem();
            result[i].setId(maxRo.getObjectID());
            result[i].setName(maxRo.getName());
            i++;
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getStatuses(Integer projectId) {
//        List<EdsReference> statuses = referenceManager
//                .listReferences(EdsProject.PROJECT_STATUS, false);
//        SelectItem[] result = new SelectItem[statuses.size()];
//        int i = 0;
//        for (EdsReference status : statuses) {
//            String value = status.getCode() != null ? wfmMessageSource.getMessage(status.getCode()) : status.getName();
//            result[i] = new SelectItem(status.getObjectID(), value);
//            i++;
//        }
//        return result;
        return commonService.convertReference2SelectItem(EdsProject.PROJECT_STATUS, false, null);

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getReccuringInvoiceStatuses() {
        List<EdsReference> statuses = referenceManager.listReferences(Constants.INVOICE_STATUS);
        SelectItem[] result = new SelectItem[3];
        int i = 0;
        for (EdsReference item : statuses) {
            String title = "";
            if (DRAFT.equals(item.getCode())) {
                title = "Invoice will be draft";
            } else if (APPROVE.equals(item.getCode())) {
                title = "Invoice will be approved";
            } else if (OPEN.equals(item.getCode())) {
                title = "Invoice will be sent";
            }
            if (!"".equals(title)) {
                result[i] = new SelectItem(item.getObjectID(), title);
                i++;
            }
        }

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getContractList() {
        List<EdsCrmAccount> clientContracts = contractManager.getClientContract();
        if (clientContracts == null || clientContracts.size() <= 0) {
            return null;
        }
        clientContracts.removeIf(clientContract -> clientContract == null || clientContract.getObjectID() == null);
        SelectItem[] result = new SelectItem[clientContracts.size()];
        int i = 0;
        for (EdsCrmAccount item : clientContracts) {
            result[i] = new SelectItem(item.getObjectID(), item.getName());
            i++;
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getReccuringInvoiceRepeats() {
        List<EdsReference> statuses = referenceManager.listReferences(Constants.INVOICE_STATUS);
        SelectItem[] result = new SelectItem[4];
        int i = 0;
        for (EdsReference item : statuses) {
            if (DRAFT.equals(item.getCode()) || APPROVE.equals(item.getCode()) || OPEN.equals(item.getCode())) {
                String value = item.getCode() != null ? wfmMessageSource.localizeRef(item) : item.getName();
                result[i] = new SelectItem(item.getObjectID(), value);
                i++;
            }
        }


        return result;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTimesheetApprovalStatusList() {
        EdsReference approved = referenceManager.findReference(EdsTimeSheetApprovalSession.TIME_SHEET_APPROVAL_SESSION_STATUS, EdsTimeSheetApprovalSession.APPROVED);
        EdsReference waiting = referenceManager.findReference(EdsTimeSheetApprovalSession.TIME_SHEET_APPROVAL_SESSION_STATUS, EdsTimeSheetApprovalSession.WAITING_FOR_APPROVAL);
        List<EdsReference> approvalStatus = new ArrayList<>();
        approvalStatus.add(approved);
        approvalStatus.add(waiting);
        return getReferenceItems(approvalStatus);
    }

    private SelectItem[] getReferenceItems(List<EdsReference> references) {
        SelectItem[] r = new SelectItem[references.size()];
        int i = 0;
        for (EdsReference stat : references) {
            r[i] = new SelectItem();
            r[i].setId(stat.getObjectID());
            r[i].setName(wfmMessageSource.localizeRef(stat));
            i++;
        }
        SelectItem[] ss = new SelectItem[i];
        System.arraycopy(r, 0, ss, 0, i);
        return ss;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTimesheetApprovers() {
        List<String> roleCodes = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.PM_APPROVE_REJECT);
        if (roleCodes.isEmpty()) {
            roleCodes.add(EdsRole.ADMIN_CODE);
        }
        roleCodes.add(Constants.BMOFPR);
        EdsEmployee currentEmployee = employeeManager.get(userManager.getUser().getObjectID());
        //approvers list
        List<EdsEmployee> timeSheetApprovers = employeeManager.getApprovers(currentEmployee, roleCodes);

        SelectItem[] approvers = new SelectItem[timeSheetApprovers.size()];
        int i = 0;
        for (EdsUser manager : timeSheetApprovers) {
            approvers[i] = new SelectItem();
            approvers[i].setId(manager.getObjectID());
            approvers[i].setName(manager.getName());
            i++;
        }
        Arrays.sort(approvers, Comparator.comparing(SelectItem::getName));
        return approvers;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getBugAssigneeList() {
        List<EdsEmployee> employeesForOldWFT = projectEmployeeManager.getEmployeesByProject(46);
        List<EdsEmployee> employeesForNewWFT2011 = projectEmployeeManager.getEmployeesByProject(12728);
        List<EdsEmployee> empList = new ArrayList<>();
        empList.addAll(employeesForOldWFT);
        empList.addAll(employeesForNewWFT2011);

        SelectItem[] r = new SelectItem[empList.size()];
        int i = 0;
        for (EdsEmployee projectEmployee : empList) {
            EdsEmployee employee = projectEmployee.getEmployeeDepartment().getEmployee();
            r[i] = new SelectItem();
            r[i].setId(employee.getObjectID());
            r[i].setName(employee.getName());
            i++;
        }
        SelectItem[] ss = new SelectItem[i];
        System.arraycopy(r, 0, ss, 0, i);
        Arrays.sort(ss, Comparator.comparing(SelectItem::getName));
        return ss;
    }

    private BaseEventsPostProcessor baseEventPostProcessor;

    public void setBaseEventPostProcessor(BaseEventsPostProcessor baseEventPostProcessor) {
        this.baseEventPostProcessor = baseEventPostProcessor;
    }

    public void deleteAttachment(Integer attachmentId) {
        commonService.deleteAttachment(attachmentId);
    }

    public void deleteAttachment(Integer attachmentId, Integer companyId) {
        commonService.deleteAttachment(attachmentId, companyId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getInvoiceQuoteStatuses(String type) {
        EdsUser user = referenceManager.getUser();
        List<EdsReference> statuses = referenceManager.listReferences(INVOICE_STATUS);
        Map<String, SelectItem> temp = new HashMap<>();
        for (EdsReference s : statuses) {
            if (CLIENT_APPROVE.equals(s.getCode()) && user.isClientContact()) {
                temp.put(s.getCode(), new SelectItem(s.getObjectID(), "Approved"));
            } else {
                temp.put(s.getCode(), new SelectItem(s.getObjectID(), wfmMessageSource.localize(s.getCode(), s.getName())));
            }
        }
        List<SelectItem> statusList = new LinkedList<>();
        if (SALE_INVOICE.equals(type) || PURCHASE_INVOICE.equals(type)) {
            if (!user.isClientContact()) {
                statusList.add(temp.get(DRAFT));
                statusList.add(temp.get(APPROVE));
                statusList.add(temp.get(OVER_DUE));
                statusList.add(temp.get(PAID));
            }
            statusList.add(temp.get(OPEN));
        } else if (RECURRING_INVOICE.equals(type)) {
            if (!user.isClientContact()) {
                statusList.add(temp.get(DRAFT));
                statusList.add(temp.get(APPROVE));
                statusList.add(temp.get(OPEN));
            }
        } else if (SALE_QUOTE.equals(type) || PURCHASE_ORDER.equals(type)) {
            if (!user.isClientContact()) {
                statusList.add(temp.get(DRAFT));
                statusList.add(temp.get(APPROVE));
                statusList.add(temp.get(CONVERTED));
                statusList.add(temp.get(OPEN));
            }
            if (SALE_QUOTE.equals(type)) {
                statusList.add(temp.get(CLIENT_APPROVE));
                statusList.add(temp.get(REJECT));
            }
        }
        return statusList.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getStatusesForFilterDrop() {
        List<EdsReference> statuses = referenceManager.listReferences(EdsTask.TASK_STATUS);
        EdsReference onHold = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.ON_HOLD);
        statuses.remove(onHold);
        SelectItem[] selectStatuses = new SelectItem[statuses.size() + 1];
        int i = 0;
        for (EdsReference status : statuses) {

            selectStatuses[i] = new SelectItem();
            selectStatuses[i].setId(status.getObjectID());
            selectStatuses[i].setName(wfmMessageSource.localizeRef(status));
            i++;
        }
        SelectItem allDue = new SelectItem(ALL_DUE_TASKS, "All Due Tasks");
        selectStatuses[selectStatuses.length - 1] = allDue;
        return selectStatuses;
    }

    public void setCampaignManager(CampaignManager campaignManager) {
        this.campaignManager = campaignManager;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCampaignsNameList() {
        List<EdsCampaign> campaigns = campaignManager.getCampaignList(new ListingFilterParameter());
        SelectItem[] result = new SelectItem[campaigns.size()];
        int i = 0;
        for (EdsCampaign campaign : campaigns) {
            result[i] = new SelectItem(campaign.getObjectID(), campaign.getName());
            i++;
        }
        return result;
    }

    public void setDepartmentManager(DepartmentManager departmentManager) {
        this.departmentManager = departmentManager;
    }

    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public void setReferenceManager(ReferenceManager referenceManager) {
        this.referenceManager = referenceManager;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTimesheetFilterStatusesDrop() {
        List<EdsReference> statuses = referenceManager.listReferences(EdsTask.TASK_STATUS);
        EdsReference onHold = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.ON_HOLD);
        EdsReference cancelled = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CANCELLED);
        EdsReference notStarted = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
        EdsReference completed = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED);
        EdsReference closed = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CLOSED);

        statuses.remove(onHold);
        statuses.remove(cancelled);
        statuses.remove(notStarted);
        statuses.remove(completed);
        statuses.remove(closed);
        SelectItem[] selectStatuses = new SelectItem[statuses.size() + 1];
        int i = 0;
        for (EdsReference status : statuses) {

            selectStatuses[i] = new SelectItem();
            selectStatuses[i].setId(status.getObjectID());
            selectStatuses[i].setName(wfmMessageSource.localizeRef(status));
            i++;
        }
        SelectItem allDue = new SelectItem(9999, "All Due Tasks");
        selectStatuses[selectStatuses.length - 1] = allDue;
        return selectStatuses;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileItem[] getExpenseAttachments(Integer expenseId) {
        return expenseServiceLocal.getAttachments(expenseId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getSupplier() {
        return invoiceService.getSuppliers(null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer[] getEmployeesMaxCount(Integer exceptEmployee) {
        return employeeService.getAllEmployeesMaxCount(null, exceptEmployee);
    }

    // add employee " Add location popup Location "
    public Integer saveLocation(CompLocationRpc compLocationRpc) {
        return locationServiceLocal.saveLocation(compLocationRpc);
    }

    public CompLocationRpc getLocation(Integer locationId) {
        return locationServiceLocal.getLocation(locationId);
    }

    public Integer updateLocation(CompLocationRpc locationRpc) {
        return locationServiceLocal.updateLocation(locationRpc);
    }

    @Override
    public SelectItem[] getCityOrDistrictByRegionId(Integer regionId) {
        if (regionId == null) return new SelectItem[0];
        List<EdsCityDistrict> districts = cityOrRegionManager.getCityOrDistrictByRegionId(regionId);

        return districts.stream().map(item -> new SelectItem(item.getObjectID(), item.getName(), item.getAlias())).toList().toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountryList() {
        return commonService.getCountries();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getLocationList() {
        EdsUser user = userManager.getUser();
        if (roleManager.hasRole(user, EdsRole.ADMIN_LOCATION) && !roleManager.hasRole(user, EdsRole.ADMIN) && !roleManager.hasRole(user, EdsRole.DR) && user.getLocation() != null) {
            SelectItem[] items = new SelectItem[1];
            items[0] = new SelectItem(user.getLocation().getObjectID(), user.getLocation().getName());
            return items;
        }
        List<EdsLocation> list = locationManager.list(new ListingFilterParameter());
        SelectItem[] items = new SelectItem[list.size()];
        int j = 0;
        for (EdsLocation location : list) {
            items[j] = new SelectItem(location.getObjectID(), location.getName());
            j++;
        }
        return ServerUtils.sortSelectItem(items);
    }

    @Override
    public SelectItem[] getProjectList() {
        return new SelectItem[0];
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getLeadStatuses() {
        return commonService.convertReference2SelectItem(EdsCrmContact._LEAD_STATUS, false, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getActivityStatuses() {
        return commonService.convertReference2SelectItem(EdsTask._CRM_TASK_STATUS, false, EdsTask.NOT_STARTED);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCampaignStatus() {
        return commonService.convertReference2SelectItem(EdsCampaign._CAMPAIGN_STATUS, false, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getOpportunityStages() {
        return commonService.convertReference2SelectItem(EdsOpportunity._OPPORTUNITY_STAGE, false, null);

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getIssuePriorities() {
        return commonService.convertReference2SelectItem(EdsIssue.ISSUE_PRIORITY, false, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getIssueStatuses() {
        return commonService.convertReference2SelectItem(EdsIssue.ISSUE_STATUS, false, null);

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getSalaryGradeListItems() {
        return hrmsServiceLocal.getSalaryGradeListItems();
    }

    public FileResource[] getRelatedFiles(Integer employeeId) {
        return hrmsServiceLocal.getRelatedFiles(employeeId);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPositionsList(ListingFilterParameter fp) {
        return hrmsServiceLocal.getPositionsListForEmployeeEdit(fp);
    }

    public Integer createPosition(NewPosition position) {
        return employeeServiceLocal.createPosition(position);
    }

    @Transactional
    public SelectItem[] getCompanyTimeSlots() {
        return availabilityServiceLocal.getTimeslotList();
    }

    public Boolean isFeatureShown(String message_code) {
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        return featuresManager.isFeatureShown(message_code, user.getObjectID());
    }

    @Override
    public Boolean isEmployeeNumberExists(String empCode, Integer objectID, String from) {
        if (empCode == null || "".equals(empCode)) {
            return false;
        } else {
            EdsEmployeeProfile profile = null;
            if (objectID != null) {
                profile = profileManager.getProfile(objectID);
            } else {
                if (!TC_INSTRUCTOR_ADD_FORM.equals(from)) {
                    profile = profileManager.getProfile();
                }
            }
            if (profile == null || TC_INSTRUCTOR_ADD_FORM.equals(from)) {
                return false;
            } else {
                return profileManager.isEmployeeCodeExists(empCode, profile.getObjectID());
            }
        }
    }

    @Override
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
                item.setId(entry.getKey().getObjectID());
                item.setName(localize != null ? localize : categoryName != null ? categoryName : categoryCode + " (" + entry.getValue() + ")");
                item.setDescription(categoryCode);
            }
            categorySelectItems.add(item);
        }
        return categorySelectItems.toArray(new SelectItem[]{});
    }

    @Override
    public SelectItem[] getUsersByNews() {
        return newsServiceLocal.getUsersByNews();
    }

    @Override
    public SelectItem[] getNewsCategories() {
        return newsServiceLocal.getNewsCategories();
    }

    @Override
    public SelectItem[] getEmplyeePositionList() {
        return hrmsService.getPositionsList();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmployeeStatusList() {
        return commonService.convertReference2SelectItem(EMPLOYEE_STATUS, false, null);
    }

    @Override
    public SelectItem[] getAttendanceTerminalSelectItems() {
        return attendanceTerminalManager.getAll().stream()
                .map(t -> new SelectItem(t.getObjectID(), t.getCompanyBranchName(), t.getCompanyUniqueID()))
                .toArray(SelectItem[]::new);
    }

    @Override
    public SelectItem[] getCertificateTypes() {
        return hrmsService.getCertificateTypes();
    }

    @Override
    public HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> getEmployeesMap(ListingFilterParameter fp, String formType) {
        HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> allEmployees = new HashMap<>();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setAllEmployees(true);
        fp.setAllByFilter(true);
        fp.setIDsOnly(true);
        fp.setResignedEmployeesIncluded(fp.isResignedEmployeesIncluded());
        ListResult<EmployeeListItem> employeeList = null;
        Integer projectID = fp.getProjectId();

        try {
            employeeList = employeeService.getEmployeeList(fp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int type = fp.getType() != null ? fp.getType() : 0;
        if (employeeList != null) {
            for (EmployeeListItem employee : employeeList.getList()) {
                WfmTreeItem treeItem = null;
                Integer objectID = type == 0 ? employee.getDepartmentId() : type == 1 ? employee.getPositionId() : employee.getLocationId();
                String name = type == 0 ? employee.getDepartment() : type == 1 ? employee.getPosition() : employee.getLocation();
                if (name != null) {
                    if (allEmployees.keySet().size() > 0) {
                        for (WfmTreeItem existTreeItem : allEmployees.keySet()) {
                            if (existTreeItem.getId().equals(objectID)) {
                                treeItem = existTreeItem;
                                break;
                            } else {
                                treeItem = new WfmTreeItem();
                                treeItem.setId(objectID);
                                treeItem.setName(name);
                                treeItem.setChecked(false);
                            }
                        }
                    } else {
                        treeItem = new WfmTreeItem();
                        treeItem.setId(objectID);
                        treeItem.setName(name);
                        treeItem.setChecked(false);
                    }
                } else {
                    treeItem = new WfmTreeItem();
                    treeItem.setId(0);
                    treeItem.setName(wfmMessageSource.localize("companyEmployees", "Company Employees"));
                    treeItem.setChecked(false);
                }
                //employees
                WfmTreeItem employeeT = new WfmTreeItem();
                employeeT.setId(employee.getObjectID());
                String code = employee.getEmployeeNumber() != null && !"".equals(employee.getEmployeeNumber()) ? employee.getEmployeeNumber() : "";
                employeeT.setName((!"".equals(code.trim()) ? code + " - " : "") + employee.getFullName());
                Integer id = fp.getRelationToID();
                boolean isSameCurrency = true;
                if (LayoutRPC.LOCATION_FORM.equals(formType)) {
                    employeeT.setChecked(id != null && id.equals(employee.getLocationId()));
                } else if (LayoutRPC.DEPARTMENT_FORM.equals(formType)) {
                    employeeT.setChecked(id != null && id.equals(employee.getDepartmentId()));
                } else if (LayoutRPC.PAYROLL_BATCH_FORM.equals(formType)) {
                    if (fp.getCurrencyID() != null && employee.getCurrency().getId() != null) {
                        isSameCurrency = employee.getCurrency().getId().equals(fp.getCurrencyID());
                    }
                    if (id == null && projectID != null) {
                        employeeT.setChecked(isSameCurrency);
                    } else {
                        employeeT.setChecked(id != null && employee.getPayrolBatchIDs().contains(id) && isSameCurrency);
                    }
                }
                //
                allEmployees.computeIfAbsent(treeItem, k -> new LinkedList<>());
                if (allEmployees.get(treeItem) != null && isSameCurrency) {
                    allEmployees.get(treeItem).add(employeeT);
                }
            }
        }
        return allEmployees;
    }

    @Override
    public HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> getTeamsMap(ListingFilterParameter fp, String formType) {
        HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> allTeams = new HashMap<>();
        List<EdsDepartment> departmentByLocationID = departmentManager.getDepartmentByLocationID(null);
        WfmTreeItem wfmTreeItem = new WfmTreeItem();
        wfmTreeItem.setName("N/A");
        LinkedList<WfmTreeItem> teams = new LinkedList<>();
        for (EdsDepartment department : departmentByLocationID) {
            WfmTreeItem teamT = new WfmTreeItem();
            teamT.setName(department.getName());
            teamT.setId(department.getObjectID());
            teams.add(teamT);
        }
        allTeams.put(wfmTreeItem, teams);
        return allTeams;
    }


    @Override
    public void saveEmployeeLocation(HashSet<Integer> locationMembers, Integer objectID, boolean isChecked) {
        if (locationMembers == null) {
            locationMembers = new HashSet<>();
        }
        if (objectID != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setAllEmployees(true);
            fp.setLocationId(objectID);
            fp.setLimit(10000);
            fp.setBriefly(true);
            ListResult<EmployeeListItem> employeeList = employeeService.getEmployeeList(fp);
            if (employeeList != null && employeeList.getList() != null && !employeeList.getList().isEmpty()) {
                locationMembers.addAll(employeeList.getList()
                        .stream()
                        .map(EmployeeListItem::getObjectID)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()));
            }

            locationServiceLocal.saveEmployeeLocation(locationMembers, objectID, isChecked);
        }
    }

    @Override
    public EmployeeListItem[] getEmployeesForGrid(ListingFilterParameter fp) {
        fp.setResignedEmployeesIncluded(false);
        ListResult<EmployeeListItem> employees = employeeService.getEmployeeList(fp);
        return employees.getList().toArray(new EmployeeListItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getAssessmentTypeList() {
        SelectItem[] selectItems = commonService.convertReference2SelectItem(EdsAssessment._ASSESSMENT_TYPE, false, null);
        ArrayList<SelectItem> selectItemArrayList = new ArrayList<>();
        for (SelectItem selectItem : selectItems) {
            if (EdsAssessment.ASSESSMENT_SIMPLE.equals(selectItem.getDescription())) {
                selectItemArrayList.add(selectItem);
            }
        }
        return selectItemArrayList.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getAssessmentStatusList() {
        return commonService.convertReference2SelectItem(Constants.ASSESSMENT_STATUS, false, null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getAgentIDs() {
        SelectItem[] item = null;
        List<EdsUserBankAccount> bankAccounts = userBankAccountManager.getUserBankAccountList();
        if (bankAccounts != null && bankAccounts.size() > 0) {
            item = new SelectItem[bankAccounts.size()];
            int i = 0;
            for (EdsUserBankAccount userBankAccount : bankAccounts) {
                item[i] = new SelectItem(userBankAccount.getObjectID(), userBankAccount.getAgentID());
                i++;
            }
        }
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getValidityPeriodList() {
        return assessmentService.getValidityPeriods(ValidityPeriodItem.VALIDITY_PERIOD_APPRAISAL);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getBugStatusList() {
        SelectItem[] selectItems = new SelectItem[6];
        selectItems[0] = new SelectItem(5, "New", BUG_STATUS_NEW);
        selectItems[1] = new SelectItem(6, "Resolved", BUG_STATUS_RESOLVED);
        selectItems[2] = new SelectItem(7, "Under Investigation", BUG_STATUS_UNDER_INVESTIGATION);
        selectItems[3] = new SelectItem(8, "In Progress", BUG_STATUS_IN_PROGRESS);
        selectItems[4] = new SelectItem(9, "Ignored", BUG_STATUS_IGNORED);
        selectItems[5] = new SelectItem(10, "Done", BUG_STATUS_DONE);
        return selectItems;
    }
}
