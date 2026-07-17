package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBillOfMaterial;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsProjectEmployeeWageClientRateHistory;
import com.edatasite.workforce.core.domain.EdsProjectPosition;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ExportToExcelItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.resourceUtil.ResourceUtilReportConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.ProjectIndexRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.project.client.rpc.NearbyProjectDto;
import com.google.api.client.util.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA. User: Date: 07.01.2008 Time: 15:27:54 To change
 * this template use File | Settings | File Templates.
 */
@Repository("projectManager")
public class ProjectManagerImpl extends AttachmentSupportManager<EdsProject> implements ProjectManager {

    @Autowired
    private RoleManager roleManager;
    @Autowired
    private ProjectIndexRbacManager projectIndexRbacManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private CommonService commonService;

    public ProjectManagerImpl() {
        super(EdsProject.class);
    }

    public List<EdsProject> list() throws NullPointerException {
        ListingFilterParameter fp = new ListingFilterParameter();
        return list(fp);
    }

    /**
     * Returns the list of clients filtered by the given params and the viewer Role.
     * You can filter by Project, Department and Employee, if you don't want to filter them, just supply null values.
     * viewAsFilter - EdsRole.DR, ADMIN, TL, PM, MEM, CLIENT values can be supplied. if supplied value, it will isolate
     * the results for that role only. Supplying null will show only the related results for the current user.
     * Mostly null should be used for the viewer, but in reports.
     */

    public List<EdsProject> list(ListingFilterParameter fp) {
        return list(fp, getUser());
    }

    public List<EdsProject> list(ListingFilterParameter fp, EdsUser user) {

        boolean hasEmployeeIds = false;
        if (fp.getEmployeeIDs() != null && !fp.getEmployeeIDs().isEmpty()) {
            hasEmployeeIds = true;
        }

        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        if (hasEmployeeIds) {
            sql.append(" select p.id, count(p.id), p.*, 0 as clazz_ ");
        } else {
            sql.append(" select p.id, p.*, 0 as clazz_ ");
        }
        sql.append(" from ").append(companyId).append(".project p ");
        sql.append(" left outer join ").append(companyId).append(".projectemployee pe on(p.id=pe.projectid ) ");
        sql.append(" left outer join ").append(companyId).append(".teamemployee te on (pe.employeedepartmentid=te.id ) ");
        sql.append(" left outer join ").append(companyId).append(".myuser mu on (te.employeeid=mu.id) ");
        sql.append(" left outer join ").append(companyId).append(".team t on (t.id=te.teamid) ");
        sql.append(" left outer join ").append(companyId).append(".reference re on (re.id = p.statusid) ");
        sql.append(" left outer join ").append(companyId).append(".project_clients pc on pc.projectid = p.id ");
        sql.append(" where ");
        sql.append(" pe.isdeleted is not true ");
        sql.append(" and p.isdeleted is not true ");

        if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and (p.clientid=").append(fp.getClientId()).append(" or pc.clientid =  ").append(fp.getClientId()).append(") ");
            if (fp.hasOnlyClientAccess()) {
                sql.append(" or (p.clientid is null and pc.clientid is null) ");
            }
        }
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > 0) {
            sql.append(" and t.id=").append(fp.getDepartmentId()).append(" ");
        }
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" and mu.id=").append(fp.getEmployeeId()).append(" ");
        }
        if (hasEmployeeIds) {
            sql.append(" and mu.id in (").append(fp.getEmployeeIDs()).append(") ");
        }
        if (fp.getLocationId() != null && fp.getLocationId() > 0) {
            sql.append(" and p.projectLocationId=").append(fp.getLocationId()).append(" ");
        }
        if (fp.getViewAsId() == null || EdsRole.DEFAULT.equals(fp.getViewAsId())) {
            if (user.isClientContact()) {
                sql.append(" and p.clientid=").append(user.getClientContact().getClientID()).append(" ");
            } else {
                if (!(roleManager.hasRole(user, EdsRole.DR) || roleManager.hasRole(user, EdsRole.ADMIN))) {
                    sql.append(" and (t.leaderid=").append(user.getObjectID()).append(" or p.managerid=").append(user.getObjectID()).append(" or p.backup_managerid=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid2=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid3=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid4=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid5=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid6=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid7=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid8=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid9=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid10=").append(user.getObjectID());
                    sql.append(" or mu.id=").append(user.getObjectID()).append(")");
                }
            }
        } else {
            if (!(EdsRole.DR.equals(fp.getViewAsId()) || EdsRole.ADMIN.equals(fp.getViewAsId()))) {
                if (EdsRole.ADMIN_LOCATION.equals(fp.getViewAsId())) {
                    EdsLocation location = user.getLocation();
                    sql.append(" and (mu.locationId is not null)  and mu.locationId=").append(location != null ? location.getObjectID() : null);
                } else {
                    if (EdsRole.TL.equals(fp.getViewAsId())) {
                        sql.append(" and (t.leaderid=").append(user.getObjectID()).append(") ");
                    } else {
                        if (EdsRole.PM.equals(fp.getViewAsId())) {
                            sql.append(" and (p.managerid=").append(user.getObjectID()).append(" or p.backup_managerid=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid2=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid3=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid4=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid5=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid6=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid7=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid8=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid9=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid10=").append(user.getObjectID()).append(") ");
                        } else {
                            if (EdsRole.MEM.equals(fp.getViewAsId())) {
                                sql.append(" and mu.id=").append(user.getObjectID());
                            } else {
                                if (EdsRole.CLIENT.equals(fp.getViewAsId()) || user.isClientContact()) {
                                    sql.append(" and (p.clientid=").append(user.getClientContact().getClientID()).append(") ");
                                }
                            }
                        }
                    }
                }
            }
        }

        EdsReference notStarted = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
        EdsReference ongoing = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ONGOING);
        EdsReference completed = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.COMPLETED);
        EdsReference closed = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.CLOSED);
        EdsReference all = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ALL);//ongoing and notstarted, not completed
        boolean lockClosedProjectItems = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.LOCK_COMPLETED_PROJECT_ITEMS);
        boolean hideComplatedProjects = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.HIDE_COMPLETED_PROJECT_FROM_LOOCKUP);

        if (fp.getProjectStatusId() == null || Integer.valueOf(0).equals(fp.getProjectStatusId())) {
            sql.append(" and (p.statusid=").append(notStarted.getObjectID());
            sql.append(" or p.statusid=").append(ongoing.getObjectID());
            if (!lockClosedProjectItems && closed != null) {
                sql.append(" or p.statusid=").append(closed.getObjectID()).append(" ");
            }
            if (!hideComplatedProjects && completed != null) {
                sql.append(" or p.statusid=").append(completed.getObjectID()).append(" ");
            }

            sql.append(" or (re.isSystemReference<>true AND re.deleted<>true) ");
            sql.append(" ) ");

        } else {
            if (fp.getProjectStatusId().equals(all.getObjectID())) {
                sql.append(" and (p.statusid=").append(notStarted.getObjectID());
                sql.append(" or p.statusid=").append(ongoing.getObjectID());
                if (!lockClosedProjectItems && closed != null) {
                    sql.append(" or p.statusid=").append(closed.getObjectID()).append(" ");
                }
                if (!hideComplatedProjects && completed != null) {
                    sql.append(" or p.statusid=").append(completed.getObjectID()).append(" ");
                }

                sql.append(" or (re.isSystemReference<>true AND re.deleted<>true) ");
                sql.append(")");

            } else {
                sql.append(" and p.statusid=").append(fp.getProjectStatusId());
            }
        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append("   lower(p.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(p.number) like '").append(fp.getSqlSearchKey()).append("' ");
            if (!fp.isLookUp()) {
                sql.append("or lower(p.description) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("or lower(mu.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("or lower(mu.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            }
            sql.append(") ");
        }
        if (hasEmployeeIds) {
            Integer count = fp.getEmployeeIDs().split(",").length;
            sql.append(" group by p.id having count(p.id)=").append(count).append(" ");
        } else {
            sql.append(" group by p.id ");
        }
        // Sort by Project Name
        sql.append(" ORDER BY p.name ");

        if (fp.getLimit() != null && fp.getLimit() > 0) {
            sql.append("limit ").append(fp.getLimit());
        }

        return findNative(sql.toString(), EdsProject.class);
    }

    public List<EdsProject> projectsList(ListingFilterParameter fp) {
        return projectsList(fp, getUser());
    }

    public List<EdsProject> projectsList(ListingFilterParameter fp, EdsUser user) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String companyId = getCompanyId();


        boolean projectMultiClientEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT);

        String projectAndTaskNotDeleted = " WHERE pe.isdeleted is not true AND p.isdeleted is not true \n" +
                " AND te.isdeleted is not true \n";
        String taskStartDateCheck = "";
        if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.RU_SHOW_PROJECTS_WITHOUT_TASKS)) {
            projectAndTaskNotDeleted += " AND t.deleted is not true \n" +
                    "AND et.deleted is not true \n" +
                    "AND t.isissue is not true \n";

            taskStartDateCheck += " AND t.startdate <= '" + dateFormat.format(fp.getEndDate()) + "' \n";
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id, p.*, 0 as clazz_ ");
        sql.append(" from ").append(companyId).append(".project p ");
        sql.append(" left outer join ").append(companyId).append(".projectemployee pe on(p.id=pe.projectid ) \n");
        sql.append(" left outer join ").append(companyId).append(".employeeTask et on (et.projectEmployeeId=pe.id) \n");
        sql.append(" left outer join ").append(companyId).append(".teamemployee te on (pe.employeedepartmentid=te.id ) \n");
        sql.append(" left outer join ").append(companyId).append(".task t ON (et.taskId=t.id) \n");
        sql.append(" left outer join ").append(companyId).append(".reference re on (re.id = p.statusid) \n");
        sql.append(" left outer join ").append(companyId).append(".reference re2 ON (re2.id = et.statusid) \n");

        if (projectMultiClientEnable) {
            sql.append(" left outer join ").append(companyId).append(".project_clients pc ON pc.projectid = p.id \n");
        }
        sql.append(" ").append(projectAndTaskNotDeleted).append(" ");
        sql.append(employeeManager.getRolePermissionForPoject());


        if (fp.getClientId() != null && fp.getClientId() > 0) {
            if (projectMultiClientEnable) {
                sql.append(" and pc.clientid = ").append(fp.getClientId()).append(" ");
            } else {
                sql.append(" and p.clientid=").append(fp.getClientId()).append(" ");
            }
        }
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > 0) {
            sql.append(" and te.teamId=").append(fp.getDepartmentId()).append(" ");
        }
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" and te.employeeid=").append(fp.getEmployeeId()).append(" ");
        }
        if (fp.getLocationId() != null && fp.getLocationId() > 0) {
            sql.append(" and p.projectLocationId=").append(fp.getLocationId()).append(" ");
        }
        if (fp.getProjectId() != null && fp.getProjectId() > 0) {
            sql.append(" and p.id=").append(fp.getProjectId()).append(" ");
        }

        EdsReference notStarted = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
        EdsReference ongoing = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ONGOING);
        EdsReference completed = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.COMPLETED);
        EdsReference all = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ALL);//ongoing and notstarted, not completed

        if (fp.getProjectStatusId() == null || Integer.valueOf(0).equals(fp.getProjectStatusId())) {
            sql.append(" and (p.statusid=").append(notStarted.getObjectID()).append(" or p.statusid=").append(ongoing.getObjectID()).append(" ");
            sql.append(" OR (p.statusid=").append(completed.getObjectID());
            List<Integer> projectIds = getProjectIds(companyId, completed.getObjectID(), dateFormat.format(fp.getStartDate()));
            String ids = ServerUtils.integerListToString(projectIds);
            if (ServerUtils.isNullOrEmpty(ids)) {
                ids = "0";
            }
            sql.append(" and p.id in (").append(ids).append("))");
            sql.append("or (re.isSystemReference<>true and re.deleted<>true)) ");
        } else {
            if (fp.getProjectStatusId().equals(all.getObjectID())) {
                sql.append(" and (p.statusid=").append(notStarted.getObjectID()).append(" or p.statusid=").append(ongoing.getObjectID()).append(" or p.statusid=").append(completed.getObjectID()).append(" ");
                sql.append("or (re.isSystemReference<>true and re.deleted<>true) ");
                sql.append(")");
            } else {
                sql.append(" and p.statusid=").append(fp.getProjectStatusId());
            }
        }
        sql.append("AND (re2.code='" + EdsTask.IN_PROGRESS + "' OR re2.code='" + EdsTask.NOT_STARTED + "' OR re2.code='" + EdsTask.WAITING_FOR_SOMEONE_ELSE + "' " +
                "OR (re2.isSystemReference is not true and re2.deleted is not true)) ");
        sql.append("AND (p.startdate<='").append(dateFormat.format(fp.getEndDate())).append("' ) ").append(taskStartDateCheck);
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append("AND (lower(p.name) like lower('%").append(fp.getSearchKey()).append("%') " +
                    "OR lower(p.number) like lower('%").append(fp.getSearchKey()).append("%')) ");
        }
        // Sort by Project Name
        sql.append(" GROUP BY p.id ");
        sql.append(" ORDER BY p.name ");
        if (fp.getLimit() != null && fp.getLimit() > 0) {
            sql.append("limit ").append(fp.getLimit());
        }
        return findNative(sql.toString(), EdsProject.class);
    }

    private List<Integer> getProjectIds(String companyId, Integer pStatusId, String date) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct p.id from ").append(companyId).append(".project p ");
        sql.append(" left join ").append(companyId).append(".task t on p.id = t.projectId ");
        sql.append("where p.statusid=").append(pStatusId).append(" and p.isdeleted is not true and ");
        sql.append(" t.deleted is not true and t.actualEndDate >= '").append(date).append("' and t.actualEndDate is not null  order by p.id");
        return findNative(sql.toString());
    }

    public List<EdsProject> getEmployeeManagedProjects(EdsEmployee employee) {
        Map<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pe.project from EdsProjectEmployee pe where");
        sql.append(" pe.deleted is not true and pe.project.deleted is not true and");
        sql.append(" pe.employeeDepartment.employee=:employee and (pe.project.manager=:employee or");
        sql.append(" pe.project.backupManager=:employee or pe.project.backupManager2=:employee or pe.project.backupManager3=:employee or");
        sql.append(" pe.project.backupManager4=:employee or pe.project.backupManager5=:employee or pe.project.backupManager6=:employee or");
        sql.append(" pe.project.backupManager7=:employee or pe.project.backupManager8=:employee or pe.project.backupManager9=:employee or pe.project.backupManager10=:employee)");
        return findByNamedParams(sql.toString(), map);
    }

    public List<Object[]> getProjectEmployees(Integer projectId) {
        EdsProject project = get(projectId);
        return getProjectEmployees(project, null);
    }

    public List<Object[]> getProjectEmployees(Integer projectId, Integer userMaxRole) {
        EdsProject project = get(projectId);
        return getProjectEmployees(project, userMaxRole);
    }

    public List<Object[]> getProjectEmployees(EdsProject projectFilter, Integer viewAsFilter) {
        EdsUser user = getUser();
        return getProjectEmployees(projectFilter, viewAsFilter, user);
    }

    public List<EdsEmployee> getProjectUnavailableEmployees(EdsProject project, Date startDate, Date endDate) {

        Map<String, Object> paramMap = new HashMap<>();
        String checkProject = "";
        StringBuilder sql = new StringBuilder();
        paramMap.put("pStartDate", startDate);
        paramMap.put("pEndDate", endDate);
        if (project != null) {
            checkProject = " and et.projectEmployee.project.objectID=" + project.getObjectID().toString();
        }

        sql.append("select distinct et.projectEmployee.employeeDepartment.employee from EdsEmployeeTask et where " + "et.task.startDate <= :pEndDate and et.task.dueDate >= :pStartDate " + "and et.deleted = false " + "and et.task.deleted = false and et.projectEmployee.employeeDepartment.employee.deleted = false").append(checkProject);
        return findByNamedParams(sql.toString(), paramMap);
    }

    public List<Object[]> getProjectEmployees(EdsProject projectFilter, Integer viewAsFilter, EdsUser user) {

        String query = "select distinct pe,e,t from EdsProjectEmployee pe join pe.project p join pe.employeeDepartment te join te.department t join te.employee e, EdsUser mu " +
                " where e.objectID=mu.objectID and (pe.deleted is null or pe.deleted<>true) and (mu.deleted =null or mu.deleted<>true) ";
        HashMap<String, Object> paramMap = new HashMap<>();

        Integer pEmployeeID = user.isClientContact() ? user.getClientContact().getClientID() : user.getObjectID();
        if (viewAsFilter == null && !roleManager.hasEitherRoles(user, EdsRole.DR, EdsRole.ADMIN, EdsRole.CLIENT)) {
            query += " and (t.leaderId =:emp or p.managerid=:emp or p.backupManager=:emp or p.backupManager2=:emp or p.backupManager3=:emp " +
                    " or p.backupManager4=:emp or p.backupManager5=:emp or p.backupManager6=:emp or p.backupManager7=:emp or p.backupManager8=:emp " +
                    " or p.backupManager9=:emp or p.backupManager10=:emp or e.objectID=:emp or p.clientId=:emp) ";
            paramMap.put("emp", pEmployeeID);

        } else {
            if (EdsRole.DR.equals(viewAsFilter) || EdsRole.ADMIN.equals(viewAsFilter)) {
                // if he is director or admin should see
                // all the projects of the company
            } else {
                if (EdsRole.TL.equals(viewAsFilter)) {
                    query += " and t.leaderId =:uid ";
                    paramMap.put("uid", user.getObjectID());
                } else {
                    if (EdsRole.PM.equals(viewAsFilter)) {
                        query += " and (p.managerid=:uid or p.backupManager=:uid or p.backupManager2=:uid or p.backupManager3=:uid " +
                                " or p.backupManager4=:uid or p.backupManager5=:uid  or p.backupManager6=:uid or p.backupManager7=:uid " +
                                " or p.backupManager8=:uid or p.backupManager9=:uid or p.backupManager10=:uid or e.objectID=:uid ) ";
                        paramMap.put("uid", user.getObjectID());
                    } else {
                        if (EdsRole.MEM.equals(viewAsFilter)) {
                            query += " and e.objectID=:uid ";
                            paramMap.put("uid", user.getObjectID());
                        } else {
                            if (EdsRole.CLIENT.equals(viewAsFilter) || user.isClientContact()) {
                                query += "  and p.clientId=:cid ";
                                paramMap.put("cid", user.getClientContact().getClientID());
                            }
                        }
                    }
                }
            }
        }

        if (projectFilter != null && projectFilter.getObjectID() != null) {
            query += " and p.objectID=:pid ";
            paramMap.put("pid", projectFilter.getObjectID());
        }

        return findByNamedParams(query, paramMap);
    }

    public SelectItem[] getProjectEmployeeList(String projectIds, Integer maxRole, EdsUser user) {
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append(" select e.id, (mu.firstname || ' ' || mu.lastname) as name, pe.positionid ");
        sql.append(" from ").append(companyId).append(".employee e");
        sql.append(" inner join ").append(companyId).append(".employeeprofile ep on e.profileid = ep.id \n");
        sql.append(" inner join ").append(companyId).append(".myuser mu on mu.id = e.id \n");
        sql.append(" inner join ").append(companyId).append(".teamEmployee te on te.employeeId=e.id \n");
        sql.append(" inner join ").append(companyId).append(".team t on te.teamId = t.id ");
        sql.append(" inner join ").append(companyId).append(".projectEmployee pe on pe.employeeDepartmentId=te.id \n");
        sql.append(" inner join ").append(companyId).append(".project p on p.id=pe.projectid \n");
        sql.append(" where pe.isDeleted is not true ");
        sql.append(" and mu.deleted is not true ");

        Integer pEmployeeID = user.isClientContact() ? user.getClientContact().getClientID() : user.getObjectID();
        if (maxRole == null && !roleManager.hasRoles(user, EdsRole.DR, EdsRole.ADMIN, EdsRole.CLIENT)) {
            sql.append(" and (");
            sql.append(" ep.reportsTo=").append(user.getObjectID()).append(" or ");  // if he is department leader for the project
            sql.append(" t.leaderId=").append(pEmployeeID).append(" or ");  // if he is department leader for the project
            sql.append(" p.managerid=").append(pEmployeeID).append(" or ");                       // if he is project manager or backup manager
            sql.append(" p.backup_ManagerId=").append(pEmployeeID).append(" or ");
            sql.append(" p.backup_managerid2=").append(pEmployeeID).append(" or ");
            sql.append(" p.backup_managerid3=").append(pEmployeeID).append(" or ");
            sql.append(" p.backup_managerid4=").append(pEmployeeID).append(" or ");
            sql.append(" p.backup_managerid5=").append(pEmployeeID).append(" or ");
            sql.append(" p.backup_managerid6=").append(pEmployeeID).append(" or ");
            sql.append(" p.backup_managerid7=").append(pEmployeeID).append(" or ");
            sql.append(" p.backup_managerid8=").append(pEmployeeID).append(" or ");
            sql.append(" p.backup_managerid9=").append(pEmployeeID).append(" or ");
            sql.append(" p.backup_managerid10=").append(pEmployeeID).append(" or ");
            sql.append("  e.id=").append(pEmployeeID).append(" or");              // if he is member of the project
            sql.append("  p.clientId=").append(pEmployeeID);                             //  if he is client
            sql.append(") ");

        } else {
            if (EdsRole.DR.equals(maxRole) || EdsRole.ADMIN.equals(maxRole)) {
                // if he is director or admin should see
                // all the projects of the company
            } else {
                if (EdsRole.TL.equals(maxRole)) {
                    sql.append(" and (t.leaderId=").append(user.getObjectID()).append(" ");   // if he is viewing only as a department leader
                    sql.append("  or ep.reportsTo=").append(user.getObjectID()).append(") ");  // if he is department leader for the project  // if he is viewing only as a department leader
                } else {
                    if (EdsRole.PM.equals(maxRole)) {
                        sql.append(" and (p.managerid=").append(pEmployeeID);                        // if he is viewing only as a project manager
                        sql.append(" or ep.reportsTo=").append(user.getObjectID());
                        sql.append(" or p.backup_ManagerId=").append(user.getObjectID());
                        sql.append(" or p.backup_managerid2=").append(user.getObjectID());
                        sql.append(" or p.backup_managerid3=").append(user.getObjectID());
                        sql.append(" or p.backup_managerid4=").append(user.getObjectID());
                        sql.append(" or p.backup_managerid5=").append(user.getObjectID());
                        sql.append(" or p.backup_managerid6=").append(user.getObjectID());
                        sql.append(" or p.backup_managerid7=").append(user.getObjectID());
                        sql.append(" or p.backup_managerid8=").append(user.getObjectID());
                        sql.append(" or p.backup_managerid9=").append(user.getObjectID());
                        sql.append(" or p.backup_managerid10=").append(user.getObjectID()).append(") ");
                    } else {
                        if (EdsRole.MEM.equals(maxRole)) {
                            sql.append(" and (e.id=").append(user.getObjectID());
                            sql.append(" or ep.reportsTo=").append(user.getObjectID()).append(") ");
                            // if he is viewing only as a member
                        } else {
                            if (EdsRole.CLIENT.equals(maxRole) || user.isClientContact()) {
                                sql.append(" and p.clientId=").append(user.getClientContact().getClientID()).append(" ");                             // if he is viewing only as a client
                            }
                        }
                    }
                }
            }
        }
        sql.append(" and (p.id in ").append(projectIds).append(")");
        sql.append(" group by e.id,mu.firstname,mu.lastname, pe.positionid ");
        sql.append(" order by name ");

        List<Object[]> empoyeeObjects = findNative(sql.toString());

        SelectItem[] employeeItems = new SelectItem[empoyeeObjects.size()];
        int i = 0;
        for (Object[] employee : empoyeeObjects) {
            employeeItems[i] = new SelectItem((Integer) employee[0], (String) employee[1]);
            employeeItems[i].setDescription(String.valueOf(employee[2]));
            i++;
        }

        return employeeItems;
    }

    public SelectItem[] getTasksCountByProject(Integer projectID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append(" select count(t.id), r.code ");
        sql.append(" from ").append(companyID).append(".task t ");
        sql.append(" left join ").append(companyID).append(".project p on p.id=t.projectid ");
        sql.append(" left join ").append(companyID).append(".reference r on t.statusid=r.id ");
        sql.append(" where p.id = ").append(projectID).append(" and (t.isIssue is null or t.isIssue is not true) and t.deleted is not true ");
        sql.append(" group by r.code ");
        List<Object[]> statusLists = findNative(sql.toString());
        SelectItem[] statusItems = new SelectItem[statusLists.size()];
        int i = 0;
        for (Object[] status : statusLists) {
            statusItems[i++] = new SelectItem(status[0] != null ? (Integer.valueOf(status[0].toString())) : 0, (String) status[1]);
        }
        return statusItems;
    }

    public List<EdsProjectEmployee> getProjectInvolvedEmployees(EdsProject project) {
        return find("SELECT pe FROM EdsProjectEmployee pe WHERE pe.deleted <> true AND pe.project = ?", project);
    }

    public List<Object[]> getResult(EdsCrmAccount clientFilter, EdsProject projectFilter,
                                    EdsDepartment departmentFilter, EdsEmployee employeeFilter, Integer viewAsFilter,
                                    String groupByName, Date from, Date to, boolean showClient,
                                    boolean showProject, boolean showDepartment, boolean showEmployee,
                                    boolean showTask, boolean showDate, boolean showComment, boolean showDescription, boolean showPercentCompleted,
                                    boolean showApprovedHours, boolean showNonApprovedHours, boolean showStatus, boolean showTimesheetStatus,
                                    boolean showEstimatedTime, boolean showManagerComment) {
        List<Integer> clientIDs = new ArrayList<>();
        List<Integer> projectIDs = new ArrayList<>();
        List<Integer> employeeIDs = new ArrayList<>();
        if (clientFilter != null && clientFilter.getObjectID() != null) {
            clientIDs.add(clientFilter.getObjectID());
        }
        if (projectFilter != null && projectFilter.getObjectID() != null) {
            projectIDs.add(projectFilter.getObjectID());
        }
        if (employeeFilter != null && employeeFilter.getObjectID() != null) {
            employeeIDs.add(employeeFilter.getObjectID());
        }
        return getResults(clientIDs, projectIDs, departmentFilter, employeeIDs, viewAsFilter,
                groupByName, from, to, showClient, showProject, showDepartment, showEmployee,
                showTask, showDate, showComment, showDescription, showPercentCompleted, showApprovedHours, showNonApprovedHours, showStatus, showTimesheetStatus, null,
                showEstimatedTime, showManagerComment, false);
    }

    public List<Object[]> getResult(EdsCrmAccount clientFilter, EdsProject projectFilter,
                                    EdsDepartment departmentFilter, EdsEmployee employeeFilter, Integer viewAsFilter,
                                    String groupByName, Date from, Date to, boolean showClient,
                                    boolean showProject, boolean showDepartment, boolean showEmployee,
                                    boolean showTask, boolean showDate, boolean showComment, boolean showDescription, boolean showPercentCompleted,
                                    boolean showApprovedHours, boolean showNonApprovedHours, boolean showStatus, boolean showTimesheetStatus,
                                    boolean showEstimatedTime, boolean showManagerComment, boolean skipRoleChecking) {
        List<Integer> clientIDs = new ArrayList<>();
        List<Integer> projectIDs = new ArrayList<>();
        List<Integer> employeeIDs = new ArrayList<>();
        if (clientFilter != null && clientFilter.getObjectID() != null) {
            clientIDs.add(clientFilter.getObjectID());
        }
        if (projectFilter != null && projectFilter.getObjectID() != null) {
            projectIDs.add(projectFilter.getObjectID());
        }
        if (employeeFilter != null && employeeFilter.getObjectID() != null) {
            employeeIDs.add(employeeFilter.getObjectID());
        }
        return getResults(clientIDs, projectIDs, departmentFilter, employeeIDs, viewAsFilter,
                groupByName, from, to, showClient, showProject, showDepartment, showEmployee,
                showTask, showDate, showComment, showDescription, showPercentCompleted, showApprovedHours, showNonApprovedHours, showStatus, showTimesheetStatus, null,
                showEstimatedTime, showManagerComment, skipRoleChecking);
    }


    public List<Object[]> getResults(List<Integer> clientFilter, List<Integer> projectFilter,
                                     EdsDepartment departmentFilter, List<Integer> employeeFilter, Integer viewAsFilter,
                                     String groupByName, Date from, Date to, boolean showClient,
                                     boolean showProject, boolean showDepartment, boolean showEmployee,
                                     boolean showTask, boolean showDate, boolean showComment, boolean showDescription, boolean showPercentCompleted,
                                     boolean showApprovedHours, boolean showNonApprovedHours, boolean showStatus, boolean showTimesheetStatus, Integer formType,
                                     boolean showEstimatedTime, boolean showManagerComment, boolean skipRoleChecking) {

        EdsReference waiting = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_WAITING");
        Map<String, Object> paramMap = new HashMap<>();
        if (clientFilter != null && clientFilter.size() > 0) {
            paramMap.put("clients", clientFilter);
        }
        if (projectFilter != null && projectFilter.size() > 0) {
            paramMap.put("projects", projectFilter);
        }
        if (employeeFilter != null && employeeFilter.size() > 0) {
            paramMap.put("employees", employeeFilter);
        }
        EdsUser user = getUser();
        String companyId = getCompanyId();

        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(ts.timeSpent),");
        if (showClient || "Client".equals(groupByName)) {
            sql.append(" c.name as name1, c.id as client_id,");
        }
        if (showProject || "Project".equals(groupByName)) {
            sql.append(" p.number ||' '|| p.name  as name2, p.id as project_id,");
        }
        if (showDepartment || "Department".equals(groupByName)) {
            sql.append(" t.name as name3, t.id as department_id,");
        }
        if (showEmployee || "Employee".equals(groupByName)) {
            sql.append(" mu.firstName ||' ' || mu.lastName, mu.id as employee_id,");
        }
        if (showDate || "Date".equals(groupByName)) {
            sql.append(" ts.date,");
        }
        if (showTask) {
            sql.append(" ta.name as name4, et.id as task_id, ts.id as timesheetID, ta.billable as task_billable,");
        }
        if (showStatus) {
            if (showEmployee) {
                sql.append(" (select r.name from ").append(companyId).append(".reference as r where id=et.statusId) as status,");
            } else {
                sql.append(" (select r.name from ").append(companyId).append(".reference as r where id=ta.statusid) as status,");
            }
        }
        if (showTimesheetStatus) {
            sql.append(" (select tr.name from ").append(companyId).append(".reference as tr where id=ts.statusid) as tshstatus,");
        }
        if (showDescription) {
            sql.append(" ta.description,");
        }
        if (showEstimatedTime) {
            sql.append(" et.estimatedtime,");
        }
        if (showManagerComment) {
            sql.append(" ts.managerComment,");
        }
        if (showComment) {
            sql.append(" ts.comment,");
        }
        if (showPercentCompleted) {
            sql.append(" ta.percent,");
        }
        if ("Client".equals(groupByName)) {
            sql.append(" c.id,");
        }
        if ("Project".equals(groupByName)) {
            sql.append(" p.id,");
        }
        if ("Department".equals(groupByName)) {
            sql.append(" t.id,");
        }
        if ("Employee".equals(groupByName)) {
            sql.append(" e.id,");
        }
        if ("Date".equals(groupByName)) {
            sql.append(" ts.date as timesheetDate,");
        }

        sql.append(" ts.typeId,");
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" from ").append(companyId).append(".timesheet ts ");
        sql.append(" left join ").append(companyId).append(".employeetask et on (ts.employeetaskId=et.id)");
        sql.append(" left join ").append(companyId).append(".task ta on (et.taskid=ta.id )");
        sql.append(" left join ").append(companyId).append(".project p on (ts.projectid=p.id )");
        sql.append(" left join ").append(companyId).append(".crmaccount c on (c.id=p.clientid )");
        sql.append(" left join ").append(companyId).append(".myuser mu on (ts.employeeid=mu.id)");
        sql.append(" left join ").append(companyId).append(".employee e on (e.id=ts.employeeid)");
        sql.append(" left join ").append(companyId).append(".employeeprofile ep on e.profileid = ep.id \n");
        sql.append(" left join ").append(companyId).append(".team t on (t.id=ts.teamid)");
        sql.append(" left join ").append(companyId).append(".reference rf on (rf.id=ts.statusId)");
        sql.append(" where ts.timespent>0");
        sql.append(" and (ts.date between '").append(from).append("' and '").append(to).append("')");

        if (showApprovedHours) {
            sql.append(" and rf.code='" + EdsTimeSheet._APPROVE + "' ");
        } else {
            if (showNonApprovedHours) {
                sql.append(" and (rf.code<>'" + EdsTimeSheet._APPROVE + "' or ts.statusId is null) ");
            }
        }
        sql.append(" and p.isdeleted is not true ");
        sql.append(" and ta.deleted is not true ");

        if (formType != null) {
            if (Constants.TIMESHEET_SUBMIT_FOR_APPROVAL_FORM.equals(formType)) {
                sql.append(" and ts.statusid is null ");
            } else {
                if (Constants.TIMESHEET_APPROVAL_FORM.equals(formType)) {
                    sql.append(" and ts.statusid=").append(waiting.getObjectID().toString()).append(" ");
                }
            }
        }

        // Filter by client, department, employee
        if (clientFilter != null && clientFilter.size() > 0) {
            sql.append(" and c.id in (:clients) ");
        }
        if (projectFilter != null && projectFilter.size() > 0) {
            sql.append(" and p.id in (:projects) ");
        }
        if (departmentFilter != null) {
            sql.append(" and t.id=").append(departmentFilter.getObjectID()).append(" ");
        }
        if (employeeFilter != null && employeeFilter.size() > 0) {
            sql.append(" and e.id in (:employees) ");
        }

        if (!skipRoleChecking) {
            if ((viewAsFilter == null || Integer.valueOf(0).equals(viewAsFilter))
                    && !roleManager.hasEitherRoles(user, EdsRole.DR, EdsRole.ADMIN, EdsRole.CLIENT)) {
                sql.append(" and (");
                sql.append(" ep.reportsTo=").append(user.getObjectID()).append(" or ");  // if he is department leader for the project
                sql.append(" t.leaderid=").append(user.getObjectID()).append(" or ");
                sql.append(" p.managerid=").append(user.getObjectID()).append(" or p.backup_managerid=").append(user.getObjectID());
                sql.append(" or p.backup_managerid2=").append(user.getObjectID());
                sql.append(" or p.backup_managerid3=").append(user.getObjectID());
                sql.append(" or p.backup_managerid4=").append(user.getObjectID());
                sql.append(" or p.backup_managerid5=").append(user.getObjectID());
                sql.append(" or p.backup_managerid6=").append(user.getObjectID());
                sql.append(" or p.backup_managerid7=").append(user.getObjectID());
                sql.append(" or p.backup_managerid8=").append(user.getObjectID());
                sql.append(" or p.backup_managerid9=").append(user.getObjectID());
                sql.append(" or p.backup_managerid10=").append(user.getObjectID());
                sql.append(" or e.id=").append(user.getObjectID()).append(")");
                if (user.isClientContact()) {
                    sql.append(" and p.clientid=").append(user.getClientContact().getClientID()).append(" ");
                }
            } else {
                if (EdsRole.DR.equals(viewAsFilter) || EdsRole.ADMIN.equals(viewAsFilter)) {
                    // if he is director or admin should see
                    // all the projects of the company
                } else {
                    if (EdsRole.TL.equals(viewAsFilter)) {
                        sql.append(" and (t.leaderid=").append(user.getObjectID());
                        sql.append("  or ep.reportsTo=").append(user.getObjectID()).append(") ");
                    } else {
                        if (EdsRole.PM.equals(viewAsFilter)) {
                            sql.append(" and (p.managerid=").append(user.getObjectID());
                            sql.append(" or ep.reportsTo=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid2=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid3=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid4=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid5=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid6=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid7=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid8=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid9=").append(user.getObjectID());
                            sql.append(" or p.backup_managerid10=").append(user.getObjectID()).append(") ");
                        } else {
                            if (EdsRole.MEM.equals(viewAsFilter) && (employeeFilter == null || employeeFilter.size() == 0)) {
                                sql.append(" and ( e.id= ").append(user.getObjectID());
                                sql.append(" or ep.reportsTo=").append(user.getObjectID()).append(") ");
                            } else {
                                if (EdsRole.CLIENT.equals(viewAsFilter) || user.isClientContact()) {
                                    sql.append(" and (p.clientid=").append(user.getClientContact().getClientID()).append(") ");
                                }
                            }
                        }
                    }
                }
            }
        }
        sql.append(" GROUP BY");
        if (showClient || "Client".equals(groupByName)) {
            sql.append(" name1, c.id,");
        }
        if (showProject || "Project".equals(groupByName)) {
            sql.append(" name2, project_id,");
        }
        if (showDepartment || "Department".equals(groupByName)) {
            sql.append(" name3, department_id,");
        }
        if (showEmployee || "Employee".equals(groupByName)) {
            sql.append(" mu.firstName ||' ' || mu.lastName, employee_id,");
        }
        if (showDate || "Date".equals(groupByName)) {
            sql.append(" ts.date,");
        }
        if (showTask) {
            sql.append(" name4, task_id, timesheetID, task_billable,");
        }
        if (showStatus) {
            sql.append(" status,");

        }
        if (showTimesheetStatus) {
            sql.append(" tshstatus,");

        }
        if (showDescription) {
            sql.append(" ta.description,");
        }

        if (showEstimatedTime) {
            sql.append(" et.estimatedtime,");
        }
        if (showManagerComment) {
            sql.append(" ts.managerComment,");
        }
        if (showComment) {
            sql.append(" ts.comment,");
        }
        if (showPercentCompleted) {
            sql.append(" ta.percent,");
        }
        if ("Client".equals(groupByName)) {
            sql.append(" c.id,");
        }
        if ("Project".equals(groupByName)) {
            sql.append(" p.id,");
        }
        if ("Department".equals(groupByName)) {
            sql.append(" t.id,");
        }
        if ("Employee".equals(groupByName)) {
            sql.append(" e.id,");
        }
        if ("Date".equals(groupByName)) {
            sql.append(" ts.date,");
        }

        sql.append(" ts.typeId,");
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ORDER BY");
        if (showDate) {
            sql.append(" ts.date,");
        }
        if (showClient || "Client".equals(groupByName)) {
            sql.append(" name1,");
        }
        if (showProject || "Project".equals(groupByName)) {
            sql.append(" name2,project_id,");
        }
        if (showDepartment || "Department".equals(groupByName)) {
            sql.append(" name3,");
        }
        if (showEmployee || "Employee".equals(groupByName)) {
            sql.append(" mu.firstName ||' ' || mu.lastName, employee_id,");
        }
        if (showTask) {
            sql.append(" name4, task_id, timesheetID, task_billable,");
        }
        if (showStatus) {
            sql.append(" status,");
        }
        if (showStatus) {
            sql.append(" tshstatus,");
        }
        if (showDescription) {
            sql.append(" ta.description,");
        }
        if (showEstimatedTime) {
            sql.append(" et.estimatedtime,");
        }
        if (showManagerComment) {
            sql.append(" ts.managerComment,");
        }
        if (showComment) {
            sql.append(" ts.comment,");
        }
        if (showPercentCompleted) {
            sql.append(" ta.percent,");
        }
        if ("Client".equals(groupByName)) {
            sql.append(" c.id,");
        }
        if ("Project".equals(groupByName)) {
            sql.append(" p.id,");
        }
        if ("Department".equals(groupByName)) {
            sql.append(" t.id,");
        }
        if ("Employee".equals(groupByName)) {
            sql.append(" e.id,");
        }
        if ("Date".equals(groupByName)) {
            sql.append(" ts.date,");
        }
        sql.deleteCharAt(sql.length() - 1);

        sql.append(" limit ").append(commonService.getDefaultDescriptionCharacterLimit());

        if (clientFilter != null && clientFilter.size() == 0 && projectFilter != null && projectFilter.size() == 0 && employeeFilter != null && employeeFilter.size() == 0) {
            return findNative(sql.toString());
        } else {
            return findNativeByNamedParams(sql.toString(), paramMap);
        }
    }


    public List<EdsProject> getLastProjects() {
        return findLimited("from EdsProject p where " +
                "(p.deleted=false or p.deleted is null) " +
                "order by p.objectID DESC", 15);
    }

    @Deprecated
    public List<Integer> getCompaniesByProjectRegDate(Date sTime, Date eTime) {
        return find("select p.company.objectID FROM EdsProject p where (p.creationTime between '" + sTime + "' and '" + eTime + "') and p.company.objectID<>1 group by p.company.objectID");
    }

    @Deprecated
    public List<EdsProject> getProjectsByRegDate(Date sTime, Date eTime, EdsCompany company, boolean includeUpdateTime) {
        List list;
        if (company == null) {
            list = find("FROM EdsProject p where (p.creationTime between '" + sTime + "' and '" + eTime + "') and p.company.objectID<>1");
        } else {
            if (includeUpdateTime) {
                list = find("FROM EdsProject p where ((p.creationTime between '" + sTime + "' and '" + eTime + "') or (p.lastUpdateTime between '" + sTime + "' and '" + eTime + "')) and p.company=? and p.company.objectID<>1", company);
            } else {
                list = find("FROM EdsProject p where (p.creationTime between '" + sTime + "' and '" + eTime + "') and p.company=? and p.company.objectID<>1", company);

            }
        }
        return list;
    }

    @Override
    public List<EdsProjectEmployee> getEmployeesByProject(Integer projectId) {
        return getEmployeesByProject(projectId, null);
    }

    @Override
    public ArrayList<EdsEmployee> getEmployeesObjectByProject(Integer projectId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT e.*,u.* ");
        query.append("FROM ").append(getCompanyId()).append(".projectEmployee pe ");
        query.append("LEFT JOIN ").append(getCompanyId()).append(".teamEmployee te ON pe.employeeDepartmentId = te.id ");
        query.append("LEFT JOIN ").append(getCompanyId()).append(".employee e ON te.employeeId = e.id ");
        query.append("LEFT JOIN ").append(getCompanyId()).append(".myuser u ON e.id = u.id ");
        query.append("WHERE pe.projectid = ").append(projectId);

        return (ArrayList<EdsEmployee>) findNative(query.toString(), EdsEmployee.class);
    }

    public List<EdsProjectEmployee> getEmployeesByProject(Integer projectId, Integer employeeId) {
        return find("select distinct pe from EdsProjectEmployee pe where pe.project.objectID=? and pe.deleted<>true " + (employeeId != null ? " and pe.employeeDepartment.employee.objectID = " + employeeId : ""), projectId);
    }

    public ArrayList<EdsProjectEmployee> getEmployeesByProjectAndEmployee(Integer projectId, String employeeName) {
        StringBuilder query = new StringBuilder();
        query.append("select pe.* from ").append(getCompanyId()).append(".projectEmployee pe ");
        query.append("left join ").append(getCompanyId()).append(".project pp on pe.projectid = pp.id ");
        query.append("left join ").append(getCompanyId()).append(".teamEmployee te on pe.employeeDepartmentId = te.id ");
        query.append("left join ").append(getCompanyId()).append(".myuser my on te.employeeId=my.id ");
        query.append("where pe.isDeleted <> true ");
        query.append("AND pp.id = ").append(projectId);
        if (employeeName != null) {
            query.append(" AND (my.firstname ilike '%").append(employeeName).append("%'");
            query.append(" OR my.lastname ilike '%").append(employeeName).append("%'");
            query.append(" OR my.middlename ilike '%").append(employeeName).append("%')");
        }

        return (ArrayList<EdsProjectEmployee>) findNative(query.toString(), EdsProjectEmployee.class);
    }

    public List<EdsProjectEmployee> getProjectEmployeesHistoryByProject(Integer projectID) {
        return find("SELECT DISTINCT peH FROM EdsProjectEmployee peH WHERE peH.historical = true AND peH.project.objectID =? ORDER BY peH.auditInfo.modificationDate desc", projectID);
    }

    public List<EdsProjectEmployee> getEmployeesByProjectAll(Integer projectId) {
        return (List<EdsProjectEmployee>) findNative("select pe.id, pe.*, ep.employeeCode " +
                "from " + getCompanyId() + ".projectEmployee pe " +
                "left join " + getCompanyId() + ".teamEmployee ed on ed.id=pe.employeeDepartmentId " +
                "left join " + getCompanyId() + ".employee e on e.id=ed.employeeid " +
                "left join " + getCompanyId() + ".employeeprofile ep on ep.id=e.profileId " +
                "where pe.projectid=" + projectId + " order by ep.employeeCode", EdsProjectEmployee.class);
    }

    public List<EdsCrmAccount> getPMClients() {
        Map<String, Object> map = new HashMap<>();
        map.put("employee", getUser() != null ? getUser().getObjectID() : 0);
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pe.project.client from EdsProjectEmployee pe where ");
        sql.append(" pe.employeeDepartment.employee.objectID=:employee and (pe.project.manager.objectID=:employee or ");
        sql.append(" pe.project.backupManager.objectID=:employee or pe.project.backupManager2.objectID=:employee or pe.project.backupManager3.objectID=:employee or ");
        sql.append(" pe.project.backupManager4.objectID=:employee or pe.project.backupManager5.objectID=:employee or pe.project.backupManager6.objectID=:employee or ");
        sql.append(" pe.project.backupManager7.objectID=:employee or pe.project.backupManager8.objectID=:employee or pe.project.backupManager9.objectID=:employee or pe.project.backupManager10.objectID=:employee) ");
        return findByNamedParams(sql.toString(), map);
    }

    public List<EdsProject> getProjectClients(Integer clientId) {
        return find("select p from EdsProject p where p.client.objectID=?", clientId);
    }

    public EdsProject getProject(Integer objectID) {
        Map<String, Object> map = new HashMap<>();
        map.put("objectID", objectID);
        return (EdsProject) findSingleByNamedParams("select p from EdsProject p where p.objectID =:objectID", map);
    }

    public List<EdsProject> getUserProjects(EdsUser user) {
        if (user instanceof EdsEmployee) {
            return (List<EdsProject>) find("select pe.project from EdsProjectEmployee pe" +
                    " where pe.employeeDepartment.employee=? and pe.project.deleted<>true and pe.deleted<>true order by pe.project.name", user);
        } else {
            return (List<EdsProject>) find("select p from EdsProject p" +
                    " where p.client=? and p.deleted<>true order by p.name", user.getClientContact().getClientID());
        }
    }

    @SuppressWarnings({"unchecked"})
    public List<EdsProject> getCompanyProjects() {
        return (List<EdsProject>) find("select p FROM EdsProject p where p.deleted is not true");
    }

    /**
     * <b> This method get all deleted project by lastUpdateTime  </b>
     * <p/>
     * <i> Write by Dilshod.T </i>
     *
     * @param solrReindex
     * @return
     */
    public List<Integer> getCompanyDeleteProjectsForSolr(SolrReindexRpc solrReindex) {
        StringBuilder projectSqlQuery = new StringBuilder("select p.id from " + getCompanyId() + ".project p where p.isdeleted=true ");
        projectSqlQuery.append(" and p.lastUpdateTime>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            projectSqlQuery.append(" and p.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) findNative(projectSqlQuery.toString());
    }

    /**
     * <b> This is method uses in project solr reindex.
     * Please not changed this is logic,
     * becaus project solr reindex working with error ... </b>
     * <p/>
     * <i> Write by Dilshod.T </i>
     *
     * @param solrReindex
     * @param start
     * @param limit
     * @return
     */
    public List<EdsProject> getCompanyProjectsForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder projectSqlQuery = new StringBuilder("select p from EdsProject p where p.deleted <> true ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            projectSqlQuery.append(" and p.lastUpdateTime >= :modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                projectSqlQuery.append(" and p.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        projectSqlQuery.append(" order by p.id asc ");
        return findIntervalByNamedParams(projectSqlQuery.toString(), start, limit, params);
    }

    public List<EdsProjectEmployee> getEmployeeNotStartedOnGoingProjects(EdsEmployee employee) {
        return (List<EdsProjectEmployee>) find("select projectEmployee from EdsProjectEmployee projectEmployee left join fetch projectEmployee.project p left join fetch p.client " +
                        "where projectEmployee.employeeDepartment.employee=? and projectEmployee.deleted<>true and projectEmployee.project.deleted<>true " +
                        "and (projectEmployee.project.status.code=? or projectEmployee.project.status.code=? " +
                        "or (projectEmployee.project.status.isSystemReference<>true and projectEmployee.project.status.deleted<>true)) ", employee,
                EdsProject.NOT_STARTED, EdsProject.ONGOING
        );
    }

    public ArrayList<SelectItem> getEmployeeNotStartedOnGoingProjectsAsSelectItem(EdsEmployee employee) {
        return (ArrayList<SelectItem>) find("select distinct new com.edatasite.workforce.gwt.core.client.rpc.SelectItem" +
                        "(projectEmployee.project.objectID, projectEmployee.project.name) " +
                        "from EdsProjectEmployee projectEmployee where projectEmployee.employeeDepartment.employee=? and projectEmployee.deleted<>true " +
                        "and projectEmployee.project.deleted<>true " +
                        "and (projectEmployee.project.status.code=? or projectEmployee.project.status.code=? " +
                        "or (projectEmployee.project.status.isSystemReference<>true and projectEmployee.project.status.deleted<>true)) " +
                        "order by projectEmployee.project.name asc", employee,
                EdsProject.NOT_STARTED, EdsProject.ONGOING
        );
    }

    public void deleteProject(EdsProject project) {
        update("update EdsProject p set p.deleted=true " +
                "where p=? and p.deleted<>true", project);
    }

    public List<EdsProject> getProjectsByClientID(Integer clientID) {
        //return find("select distinct p from EdsProject p where p.client.objectID = ? and p.deleted<>true and p.status.code<>?", clientID, EdsProject.COMPLETED);
        return find("select p from EdsProject p where p.client.objectID = ? and p.deleted<>true", clientID);
    }

    public EdsProjectEmployee getProjectEmployee(Integer projectEmployeeId) {
        return (EdsProjectEmployee) findSingle("select p from EdsProjectEmployee p where p.objectID =?", projectEmployeeId);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<EdsProject> getChoosesProjectDashboard(Integer projectId) {
        return find("select p from EdsProject p " +
                "where p.objectID=? and p.deleted<>true", projectId);
    }

    public List getProjectsForDashboard(String projectIds) {

        EdsReference approvedExpense = referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_APPROVED);
        EdsReference paidExpense = referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_PAID);

        Integer saleOrderStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SALE_ORDER).getObjectID();
        Integer pickedStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PICKED).getObjectID();
        Integer packedStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PACKED).getObjectID();
        Integer shippedStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.SHIPPED).getObjectID();


        Integer approvedStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.APPROVE).getObjectID();
        Integer paidStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.PAID).getObjectID();
        Integer openStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.OPEN).getObjectID();
        Integer overDueStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.OVER_DUE).getObjectID();

        Integer convertedStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.CONVERTED).getObjectID();
        Integer receivedStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.RECEIVED).getObjectID();
        Integer invoicedStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.INVOICED).getObjectID();

        Integer clientApproveStatusId = referenceManager.findReference(Constants.INVOICE_STATUS, Constants.CLIENT_APPROVE).getObjectID();

        StringBuilder sb = new StringBuilder();
        sb.append("select p.id as id, ");
        sb.append("p.name as name, ");
        sb.append("p.managerid as manager, ");
        sb.append("p.startdate as start, ");
        sb.append("p.duedate as end, ");

        //PLANNED COST
        sb.append("(");
        sb.append("select ");
        sb.append("(");
        //purchase order
        sb.append(" (CASE WHEN (sum(CASE WHEN sqpo.total is not null THEN sqpo.total ELSE 0 END)) is null THEN 0 ELSE (sum(CASE WHEN sqpo.total is not null THEN sqpo.total ELSE 0 END)) END) + ");
        //employeeTask planned wageAmount
        sb.append(" (CASE WHEN (sum(CASE WHEN et.plannedwageamount is not null THEN et.plannedwageamount ELSE 0 END)) is null THEN 0 ELSE (sum(CASE WHEN et.plannedwageamount is not null THEN et.plannedwageamount ELSE 0 END)) END) ");
        sb.append(") from ").append(getCompanyId()).append(".quote sqpo where sqpo.relatedproject_id = p.id ");
        sb.append(" and sqpo.type = '").append(Constants.PAYABLE).append("' ");
        sb.append("and sqpo.status_id in (").append(convertedStatusId).append(", ").append(receivedStatusId).append(", ").append(invoicedStatusId).append(") ");
        sb.append(") as plannedCost, ");

        //ACTUAL COST
        sb.append("(");
        //purchase invoices
        sb.append("(select (CASE WHEN (sum(CASE WHEN sipi.exchangeRate is not null and sipi.exchangeRate != 0 THEN (CASE WHEN inp.amount is not null THEN inp.amount ELSE 0 END)/sipi.exchangeRate END)) is null THEN 0 ELSE");
        sb.append(" (sum(CASE WHEN sipi.exchangeRate is not null and sipi.exchangeRate != 0 THEN (CASE WHEN inp.amount is not null THEN inp.amount ELSE 0 END)/sipi.exchangeRate END)) END) ");
        sb.append(" from ").append(getCompanyId()).append(".invoice sipi ");
        sb.append("left outer join ").append(getCompanyId()).append(".invoicePayments inp on inp.invoiceid = sipi.id where sipi.relatedproject_id = p.id ");
        sb.append(" and sipi.type = '").append(Constants.PAYABLE).append("' ");
        sb.append(" and sipi.status_id in (").append(approvedStatusId).append(", ").append(paidStatusId).append(", ").append(openStatusId).append(", ").append(overDueStatusId).append(") ");
        sb.append(") + ");
        //employeeTask actual wageAmount
        sb.append(" (CASE WHEN sum(CASE WHEN et.actualwageammount is not null THEN et.actualwageammount ELSE 0 END) is not null THEN sum(CASE WHEN et.actualwageammount is not null THEN et.actualwageammount ELSE 0 END) ELSE 0 END) + ");
        //expense reports
        sb.append("(select (CASE WHEN sum(CASE WHEN exp.amount is not null THEN exp.amount ELSE 0 END) is not null THEN sum(CASE WHEN exp.amount is not null THEN exp.amount ELSE 0 END) ELSE 0 END) ");
        sb.append(" from ").append(getCompanyId()).append(".expenseReport exr ");
        sb.append("left outer join").append(getCompanyId()).append(".expensePayments exp on exp.expenseReportId = exr.id ");
        sb.append("where exr.projectid = p.id and exr.statusid in ").append("(").append(approvedExpense != null ? approvedExpense.getObjectID().toString() : String.valueOf(0)).append(",").append(paidExpense != null ? paidExpense.getObjectID().toString() : String.valueOf(0)).append(") ");
        sb.append(")) as actualCost, ");

        //quote - PLANNED REVENUE
        sb.append("(select (CASE WHEN sum(CASE WHEN quo.total is not null THEN quo.total ELSE 0 END) is not null THEN sum(CASE WHEN quo.total is not null THEN quo.total ELSE 0 END) ELSE 0 END) ");
        sb.append(" from ").append(getCompanyId()).append(".quote quo where quo.type = '").append(Constants.RECEIVABLE).append("'").append(" and quo.total is not null and ");
        sb.append(" quo.status_id in (").append(convertedStatusId).append(", ").append(clientApproveStatusId).append(", ").append(invoicedStatusId).append(", ").append(saleOrderStatusId).append(", ").append(pickedStatusId).append(", ").append(packedStatusId).append(", ").append(shippedStatusId).append(") and ");
        sb.append(" quo.relatedproject_id = p.id) as plannedRevenue, ");

        //invoice - ACTUAL REVENUE
        sb.append("(select (CASE WHEN (sum(CASE WHEN sipi.exchangeRate is not null and sipi.exchangeRate != 0 and sipi.type = '").append(Constants.RECEIVABLE).append("'").append(" THEN ");
        sb.append("(CASE WHEN inp.amount is not null THEN inp.amount ELSE 0 END)/(sipi.exchangeRate) END)) is null THEN 0 ELSE ");
        sb.append("(sum(CASE WHEN sipi.exchangeRate is not null and sipi.exchangeRate != 0 and sipi.type = '").append(Constants.RECEIVABLE).append("'").append(" THEN ");
        sb.append("(CASE WHEN inp.amount is not null THEN inp.amount ELSE 0 END)/sipi.exchangeRate END)) END) ");
        sb.append(" from ").append(getCompanyId()).append(".invoice sipi ");
        sb.append("left outer join ").append(getCompanyId()).append(".invoicePayments inp on inp.invoiceid = sipi.id where sipi.relatedproject_id = p.id ");
        sb.append(" and sipi.status_id in (").append(approvedStatusId).append(", ").append(paidStatusId).append(", ").append(openStatusId).append(", ").append(overDueStatusId).append(") ");
        sb.append(") as actualRevenue ");

        sb.append("from ").append(getCompanyId()).append(".project p ");
        sb.append("left outer join ").append(getCompanyId()).append(".projectemployee pe on p.id = pe.projectid ");
        sb.append("left outer join ").append(getCompanyId()).append(".employeetask et on pe.id = et.projectemployeeid ");

        sb.append("where p.id in (").append(projectIds).append(") ");
        sb.append("and p.isdeleted<>true and pe.isdeleted<>true ");
        sb.append("group by p.id,p.name,p.managerid,p.startdate,p.duedate ");
        sb.append("order by p.id\n ");
        sb.append("limit 5 ");
        return findNative(sb.toString());
    }

    public ListingObjectItem getpurchaseOrderList(Integer projectId, ListingFilterParameter fp) {
        boolean projectInLineItemEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);

        Long totalCount = Long.valueOf(0);
        if (!projectInLineItemEnabled) {
            totalCount = (Long) findSingle("select count(po.objectID) from EdsPurchaseOrder po where " +
                            ServerUtils.checkForDeleted("po.deleted") + " and (po.relatedProject.objectID =? or po.relatedProject.parent.objectID =?)",
                    projectId, projectId
            );
        } else {
            totalCount = (Long) findSingle("select count(distinct po.objectID) from EdsPurchaseOrder po " +
                            "left join po.quoteItems as qi where " +
                            ServerUtils.checkForDeleted("po.deleted") + "  and (qi.project.objectID =? or qi.project.parent.objectID =?)",
                    projectId, projectId
            );
        }

        List<EdsPurchaseOrder> list = Lists.newArrayList();
        if (totalCount != null && totalCount > 0) {
            StringBuilder sql = new StringBuilder();
            sql.append("select po from EdsPurchaseOrder po ");
            sql.append(" where po.objectID ");
            sql.append(" in (select distinct po.objectID from EdsQuoteItem item ");
            sql.append(" join item.quote po ");
            sql.append(" where ").append(ServerUtils.checkForDeleted("po.deleted"));
            if (projectInLineItemEnabled) {
                sql.append(" and item.project.objectID =?");
            } else {
                sql.append(" and (po.relatedProject.objectID =? or po.relatedProject.parent.objectID =?)");
            }
            sql.append(") order by ");

            if (fp.getSortField() != null) {
                switch (fp.getSortField()) {
                    case "invoiceNumber" -> sql.append("po.number ");
                    case "invoiceDate" -> sql.append("po.invoiceDate ");
                    case "dueDate" -> sql.append("po.dueDate ");
                    case "client" -> sql.append("po.supplier.name ");
                    case "currency" -> sql.append("po.currency.name ");
                    case "prospectAmount" -> sql.append("po.total ");
                    case "status" -> sql.append("po.status.name ");
                    case "quoteNumber" -> sql.append("po.total ");
                    case "creator" -> sql.append("po.creator.firstName, po.creator.lastName ");
                    case "manager" -> sql.append("po.approver.firstName, po.approver.lastName ");
                    case "subTotal" -> sql.append("po.subtotal ");
                    case "taxTotal" -> sql.append("po.totalTaxes ");
                    default -> sql.append("po.objectID ");
                }
                if (fp.isAscending()) {
                    sql.append("asc ");
                } else {
                    sql.append("desc ");
                }
            } else {
                sql.append("po.objectID desc ");
            }

            if (projectInLineItemEnabled) {
                list = findInterval(sql.toString(), fp.getStart(), fp.getLimit(), projectId);
            } else {
                list = findInterval(sql.toString(), fp.getStart(), fp.getLimit(), projectId, projectId);
            }
        }

        return new ListingObjectItem(list, totalCount);
    }

    /**
     * For mothod count department or employee p number
     */
    public List getDepartmentInvolvedProjectCounted(Integer departmentId, Integer employeeId, boolean status) {
        return findNative("select  p.id from " + getCompanyId() + ".project p " +
                "left outer join " + getCompanyId() + ".projectemployee pe on pe.projectid=p.id " +
                "left outer join " + getCompanyId() + ".teamEmployee te on te.id=pe.employeeDepartmentId " +
                "left outer join " + getCompanyId() + ".team t on t.id=te.teamId  " +
                "where p.isdeleted<>true " +
                (departmentId != null ? "and t.id=" + departmentId + " " : " ") +// get for department projects
                (employeeId != null ? "and te.employeeId=" + employeeId + " " : " ") +// get for employee projects
                (status ? "and p.statusid<>" + 75 + " " : " ") +
                " group by p.id "); // get for Completed projects
    }

    public ListingObjectItem getPurchaseInvoiceList(Integer projectId, ListingFilterParameter fp) {
        StringBuilder sqlCount = new StringBuilder();
        sqlCount.append("select count(distinct inv.objectID) from EdsInvoiceItem item ");
        sqlCount.append("join item.invoice inv where ");
        sqlCount.append(ServerUtils.checkForDeleted("inv.deleted"));
        sqlCount.append(" and inv.type = '").append(Constants.PAYABLE).append("'");

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sqlCount.append(" and (item.project.objectID=").append(projectId);
            sqlCount.append(" or item.project.parent.objectID=").append(projectId).append(") ");
        } else {
            sqlCount.append(" and (inv.relatedProject.objectID=").append(projectId);
            sqlCount.append(" or inv.relatedProject.parent.objectID=").append(projectId).append(") ");
        }

        Long totalCount = (Long) findSingle(sqlCount.toString());

        List<EdsPurchaseInvoice> list = Lists.newArrayList();
        if (totalCount != null && totalCount > 0) {
            StringBuilder sql = new StringBuilder();
            sql.append("select inv from EdsPurchaseInvoice inv ");
            sql.append("where inv.objectID in ( ");
            sql.append("select distinct inv.objectID from EdsInvoiceItem item ");
            sql.append("join item.invoice inv where ");
            sql.append(ServerUtils.checkForDeleted("inv.deleted"));
            sql.append(" and inv.type = '").append(Constants.PAYABLE).append("'");

            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append(" and (item.project.objectID=").append(projectId);
                sql.append(" or item.project.parent.objectID=").append(projectId).append(") ");
            } else {
                sql.append(" and (inv.relatedProject.objectID=").append(projectId);
                sql.append(" or inv.relatedProject.parent.objectID=").append(projectId).append(") ");
            }
            sql.append(" ) order by ");
            if (fp.getSortField() != null) {
                switch (fp.getSortField()) {
                    case "invoiceNumber" -> sql.append("inv.number ");
                    case "invoiceDate" -> sql.append("inv.invoiceDate ");
                    case "dueDate" -> sql.append("inv.dueDate ");
                    case "client" -> sql.append("inv.supplier.name ");
                    case "currency" -> sql.append("inv.currency.name ");
                    case "dueAmount" -> sql.append("inv.totalInInvoiceCurrency ");
                    case "status" -> sql.append("inv.status.name ");
                    case "paidAmuount" -> sql.append("inv.total ");
                    case "creator" -> sql.append("inv.creator.firstName, inv.creator.lastName ");
                    case "taxTotal" -> sql.append("inv.totalTaxes ");
                    default -> sql.append("inv.objectID ");
                }
                if (fp.isAscending()) {
                    sql.append("asc ");
                } else {
                    sql.append("desc ");
                }
            } else {
                sql.append("inv.objectID desc ");
            }
            list = findInterval(sql.toString(), fp.getStart(), fp.getLimit());
        }

        return new ListingObjectItem(list, totalCount);
    }

    public ListingObjectItem getExpenseReportList(Integer projectId, ListingFilterParameter fp) {
//        boolean hasProjectInLine = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);

        Integer[] statusIDs = new Integer[4];
        statusIDs[0] = referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_APPROVED).getObjectID();
        statusIDs[1] = referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_PAID).getObjectID();
        statusIDs[2] = referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_SUBMITTED).getObjectID();
        statusIDs[3] = referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_CLOSED).getObjectID();
        StringBuilder sql = new StringBuilder();
        sql.append(" from EdsExpenseReport ex where ").append(ServerUtils.checkForDeleted("ex.isDeleted"));

        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (lower(ex.number) like '").append(fp.getSqlSearchKey()).append("' OR ");
            sql.append("lower(ex.title) like '").append(fp.getSqlSearchKey()).append("' OR ");
            sql.append("lower(ex.description) like '").append(fp.getSqlSearchKey()).append("' ) ");
        }

        if (projectId != null) {
            sql.append(" and (ex.project.objectID=").append(projectId).append(" or ex.project.parent.objectID=").append(projectId).append(") ");
        }

//        sql.append(" and ( ");
//        sql.append(" ex.overallStatus.objectID=").append(statusIDs[0]).append(" ");
//        for (int i = 1; i < statusIDs.length; i++) {
//            sql.append(" OR ex.overallStatus.objectID=").append(statusIDs[i]).append(" ");
//        }
//        sql.append(" ) ");
        StringBuilder sortQuery = new StringBuilder(" order by ");
        if (fp.getSortField() != null) {
            switch (fp.getSortField()) {
                case "title" -> sortQuery.append("ex.title ");
                case "reportPeriod" -> sortQuery.append("ex.startDate ");
                case "relatedProject" -> sortQuery.append("ex.project.name ");
                case "reporter" -> sortQuery.append("ex.reporter.firstName, ex.reporter.lastName ");
                case "approver" ->
                        sortQuery.append("ex.currentApprover.exactEmployee.firstName, ex.currentApprover.exactEmployee.lastName ");
                case "status" -> sortQuery.append("ex.overallStatus.name ");
                default -> sortQuery.append("ex.objectID ");
            }
            if (fp.isAscending()) {
                sortQuery.append("asc ");
            } else {
                sortQuery.append("desc ");
            }
        } else {
            sortQuery.append("ex.objectID desc ");
        }
        Long totalCount = (Long) findSingle("select count(ex.objectID) " + sql.toString());

        List<EdsExpenseReport> list = Lists.newArrayList();

        if (totalCount != null && totalCount > 0) {
            list = findInterval("select ex " + sql.toString() + sortQuery.toString(), fp.getStart(), fp.getLimit());
        }
        return new ListingObjectItem(list, totalCount);
    }

    public List<EdsProject> getProjects(String projectIds) {
        return find("select p from EdsProject p where (p.deleted<>true or p.deleted is null) and p.objectID in (" + projectIds + ")");
    }

    @Override
    public List<Integer> getProjectIdList(String ids) {
        return find("select p.objectID from EdsProject p where (p.deleted<>true or p.deleted is null) and p.objectID in (" + ids + ")");
    }

    public List<EdsProject> getProjectManagersByEmployeeId(Integer employeeId, boolean activeOnly) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p from EdsProject p where ");
        if (activeOnly) {
            sql.append(" p.status.code!='" + EdsProject.COMPLETED + "' and ");
        }
        sql.append(" (p.manager.objectID=? or p.backupManager.objectID=? ");
        sql.append(" or p.backupManager2.objectID=? ");
        sql.append(" or p.backupManager3.objectID=? ");
        sql.append(" or p.backupManager4.objectID=? ");
        sql.append(" or p.backupManager5.objectID=? ");
        sql.append(" or p.backupManager6.objectID=? ");
        sql.append(" or p.backupManager7.objectID=? ");
        sql.append(" or p.backupManager8.objectID=? ");
        sql.append(" or p.backupManager9.objectID=? ");
        sql.append(" or p.backupManager10.objectID=? ");
        sql.append(" ) and (p.deleted<>true or p.deleted is null) order by p.startDate desc ");
        return find(sql.toString(), employeeId, employeeId, employeeId, employeeId, employeeId, employeeId, employeeId, employeeId, employeeId, employeeId, employeeId);
    }

    public List<EdsProject> getCalendarProjects(List<Integer> employeeIDs, Date start, Date end) {
        Map params = new HashMap();
        params.put("employeeIDs", employeeIDs);
        params.put("start", start);
        params.put("end", end);
        return (List<EdsProject>) findByNamedParams("select distinct pe.project from EdsProjectEmployee pe" +
                " where pe.employeeDepartment.employee.objectID in (:employeeIDs) and (pe.project.startDate<=:end and pe.project.dueDate>=:start)" +
                " and pe.project.deleted<>true and pe.deleted<>true", params);
    }

    public ListingObjectItem getInvoiceList(Integer projectId, ListingFilterParameter fp) {
        boolean projectInLineItemEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        StringBuilder sql = new StringBuilder();
        sql.append("select inv from EdsSaleInvoice inv ");
        sql.append(" where inv.objectID ");
        sql.append(" in (select distinct inv.objectID from EdsInvoiceItem item ");
        sql.append("     join item.invoice inv ");
        sql.append("     where ").append(ServerUtils.checkForDeleted("inv.deleted"));
        if (projectInLineItemEnabled) {
            sql.append("    and item.project.objectID =?");
        } else {
            sql.append("    and (inv.relatedProject.objectID =? or inv.relatedProject.parent.objectID =?)");
        }
        sql.append(") order by ");
        if (fp.getSortField() != null) {
            if ("invoiceNumber".equals(fp.getSortField())) {
                sql.append("inv.number ");
            } else if ("invoiceDate".equals(fp.getSortField())) {
                sql.append("inv.invoiceDate ");
            } else if ("dueDate".equals(fp.getSortField())) {
                sql.append("inv.dueDate ");
            } else if ("client".equals(fp.getSortField())) {
                sql.append("inv.client.name ");
            } else if ("paidAmuount".equals(fp.getSortField())) {
                sql.append("inv.total ");
            } else if ("orginalAmount".equals(fp.getSortField())) {
                sql.append("inv.totalInInvoiceCurrency ");
            } else if ("status".equals(fp.getSortField())) {
                sql.append("inv.status.name ");
            } else if ("currency".equals(fp.getSortField())) {
                sql.append("inv.currency.name ");
            } else if ("creator".equals(fp.getSortField())) {
                sql.append("inv.creator.firstName, inv.creator.lastName ");
            } else if ("poNumber".equals(fp.getSortField())) {
                sql.append("inv.poNumber  ");
            } else {
                sql.append("inv.objectID ");
            }
            if (fp.isAscending()) {
                sql.append("asc");
            } else sql.append("desc");
        } else {
            sql.append("inv.objectID desc");
        }
        List<EdsSaleInvoice> byNamedParams;
        if (projectInLineItemEnabled) {
            byNamedParams = findInterval(sql.toString(), fp.getStart(), fp.getLimit(), projectId);
        } else {
            byNamedParams = findInterval(sql.toString(), fp.getStart(), fp.getLimit(), projectId, projectId);
        }
        if (byNamedParams != null) {
            return new ListingObjectItem(byNamedParams, byNamedParams.size());
        } else {
            return new ListingObjectItem();
        }
    }

    public List<EdsPurchaseInvoice> getPurchaseInvoiceList(Integer projectId, Date startDate, Date dueDate) {
        Map params = new HashMap();
        params.put("projectID", projectId);
        return findByNamedParams("select distinct pi from EdsPurchaseInvoice pi where pi.relatedProject.objectID =:projectID or pi.relatedProject.parent.objectID =:projectID", params);
    }

    public List<EdsExpenseReport> getExpenseReportList(Integer projectId) {
        EdsUser user = getUser();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ex from EdsExpenseReport ex where ").append(ServerUtils.checkForDeleted("ex.isDeleted")).append(" and ex.project.objectID = ").append(projectId).append(" or ex.project.parent.objectID = ").append(projectId);
        if (!user.hasEitherRoles(EdsRole.DR, EdsRole.ADMIN)) {
            sql.append(" and (ex.creator.objectID = ").append(user.getObjectID()).append(" or ex.reporter.objectID = ").append(user.getObjectID()).append(")");
        }
        return find(sql.toString());
    }

    public Integer getProjectLastIntNumber() {
        return (Integer) findSingle("select p.intNumber from EdsProject p where p.deleted = false and p.intNumber is not null order by p.intNumber desc");
    }

    @Override
    public boolean isProjectNumberExists(String number, Integer projectID) {
        if (projectID != null) {
            return find("select p from EdsProject p where (p.deleted = false or p.deleted is null) and p.number = ? and p.objectID != ?", number.trim(), projectID).size() > 0;
        } else {
            return find("select p from EdsProject p where (p.deleted = false or p.deleted is null) and p.number = ?", number.trim()).size() > 0;
        }
    }

    public EdsSaleInvoice getProjectLastInvoice(Integer projectId) {
        return (EdsSaleInvoice) findSingle("select si from EdsSaleInvoice si where si.relatedProject.objectID = ? and " + ServerUtils.checkForDeleted("si.deleted") + " order by si.objectID desc", projectId);
    }

    @Override
    public String getProjectLastInvoiceNumber(Integer projectId) {
        return (String) findSingle("select si.number from EdsSaleInvoice si where si.relatedProject.objectID = ? and " + ServerUtils.checkForDeleted("si.deleted") + " order by si.objectID desc", projectId);
    }

    @Override
    public List<Integer> getCompanyProjectIdList(Integer companyID, Integer start, Integer limit) {
        String company = "\"" + companyID + "\"";
        String query = "select id from " + company + ".project where id>" + start + " and (isdeleted<>true or isdeleted is null) order by id asc limit " + limit;
        return (List<Integer>) findNative(query);
    }

    public List<EdsProjectEmployeeWageClientRateHistory> getEmployeeWageClientRateHistory(Integer employeeId, Integer projectId) {

        EdsProjectEmployee epe = getProjectEmployeeOb(employeeId, projectId);

        return (epe != null) ? find("select distinct pe from EdsProjectEmployeeWageClientRateHistory pe where pe.projectEmployee = ? order by pe.changeDate", epe) : null;

    }

    public EdsProjectEmployee getProjectEmployeeOb(Integer employeeId, Integer projectId) {
        return (EdsProjectEmployee) findSingle("select pe from EdsProjectEmployee pe where pe.employeeDepartment.employee.objectID = ? and pe.project.objectID = ? and pe.deleted<>true", employeeId, projectId);
    }

    public void updateEmployeeWageClientRateHistory(EdsProjectEmployeeWageClientRateHistory hist) {

        Integer objId = hist.getObjectID();

        update("UPDATE EdsProjectEmployeeWageClientRateHistory p SET p.clientChargeRate = ?, p.wageRate = ?, p.changeDate = ? WHERE p.objectID = ? ", hist.getClientChargeRate(), hist.getWageRate(), hist.getChangeDate(), objId);
    }


    public void updateEmployeeWageClientRateHistorybyDate(EdsProjectEmployeeWageClientRateHistory hist) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(hist.getChangeDate());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        hist.setChangeDate(cal.getTime());

        Integer objId = hist.getProjectEmployee().getObjectID();

        EdsProjectEmployeeWageClientRateHistory his = null;

        if (objId != null) {
            his = getEdsProjectEmployeeWageClientRateHistory(objId, hist.getChangeDate());
        }

        if (his != null && his.getObjectID() != null) {
            update("UPDATE EdsProjectEmployeeWageClientRateHistory p SET p.clientChargeRate = ?, p.wageRate = ? WHERE p.projectEmployee.objectID = ? AND p.changeDate = ? ", hist.getClientChargeRate(), hist.getWageRate(), objId, hist.getChangeDate());
        } else {
            persist(hist);
        }
    }

    public EdsProjectEmployeeWageClientRateHistory getEdsProjectEmployeeWageClientRateHistory(Integer projectEmployeeId, Date changeDate) {

        return (EdsProjectEmployeeWageClientRateHistory) findSingle("select p from EdsProjectEmployeeWageClientRateHistory p where p.projectEmployee.objectID = ? AND p.changeDate = ?", projectEmployeeId, changeDate);
    }


    public void updateProjectEmployeeOb(Double WR, Double CChR, Integer employeeId, Integer projectId) {
        EdsProjectEmployee epe = getProjectEmployeeOb(employeeId, projectId);
        update("UPDATE EdsProjectEmployee pe SET pe.clientChargeRate = ?, pe.wageRate = ? WHERE pe.objectID = ?", CChR, WR, epe.getObjectID());
    }

    public List<EdsExpenseReport> getAllExpenseReports(ListingFilterParameter filterParametrs) {
        Map<String, Object> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct er from EdsExpenseReport er ");
        sql.append("left join fetch er.project p ");
        sql.append("left join fetch er.expenses e ");

        StringBuilder filterSql = new StringBuilder();
        filterSql.append(" where ").append(ServerUtils.checkForDeleted("er.isDeleted"));
        if (filterParametrs.getProjectId() != null && filterParametrs.getProjectId() != 0) {
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                filterSql.append(" and (e.project.objectID in (:projectIDs) or e.project.parent.objectID=").append(filterParametrs.getProjectId()).append(") ");
            } else {
                filterSql.append(" and (er.project.objectID in (:projectIDs) or er.project.parent.objectID=").append(filterParametrs.getProjectId()).append(") ");
            }
            if (filterParametrs.getProjectIdList() != null && filterParametrs.getProjectIdList().size() > 0) {
                map.put("projectIDs", filterParametrs.getProjectIdList());
            } else {
                map.put("projectIDs", filterParametrs.getProjectId());
            }
        }
        if (filterParametrs != null && filterParametrs.getEmployeeId() != null && filterParametrs.getEmployeeId() != 0) {
            filterSql.append(" and er.reporter.objectID=").append(filterParametrs.getEmployeeId()).append(" ");
        }
        if (filterParametrs.getStatusIDs() != null && filterParametrs.getStatusIDs().length > 0) {
            filterSql.append(" and ( ");
            filterSql.append(" er.overallStatus.objectID=").append(filterParametrs.getStatusIDs()[0]).append(" ");
            for (int i = 1; i < filterParametrs.getStatusIDs().length; i++) {
                filterSql.append(" OR er.overallStatus.objectID=").append(filterParametrs.getStatusIDs()[i]).append(" ");
            }
            filterSql.append(" )");
        }
        if (filterParametrs != null && filterParametrs.isValidSearchKey()) {
            String key = filterParametrs.getSqlSearchKey();
            filterSql.append(" and (er.reporter.firstName like '").append(key).append("' or er.reporter.lastName like '").append(key).append("' or er.title like'").append(key).append("' or er.project.name like '").append(key).append("') ");
        }
        sql.append(filterSql);

        return findByNamedParams(sql.toString(), map);
    }

    public EdsProjectEmployeeWageClientRateHistory getProjectEmployeeWageClientRateHistory(Integer wageHistoryID) {
        return (EdsProjectEmployeeWageClientRateHistory) findSingle("select distinct pe from EdsProjectEmployeeWageClientRateHistory pe where pe.objectID = ?", wageHistoryID);
    }

    @Override
    public EdsProjectEmployeeWageClientRateHistory getNextProjectEmployeeWageClientRateHistory(Integer wageRateHistoryID) {
        return (EdsProjectEmployeeWageClientRateHistory) findSingle("select distinct pe from EdsProjectEmployeeWageClientRateHistory pe " +
                "where pe.id > ? and pe.projectEmployee.objectID = (select pe.projectEmployee.objectID from EdsProjectEmployeeWageClientRateHistory pe where pe.id = ?) order by id", wageRateHistoryID, wageRateHistoryID);
    }

    @Override
    public void updateTimesheetWageRates(EdsProjectEmployeeWageClientRateHistory nextWageRate, EdsProjectEmployeeWageClientRateHistory previousWageRate) {
        updateTimesheetWageRates(nextWageRate.getWageRate(), nextWageRate.getClientChargeRate(), previousWageRate.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID(), previousWageRate.getProjectEmployee().getProject().getObjectID(), previousWageRate.getChangeDate(), nextWageRate.getChangeDate());
    }

    @Override
    public void updateTimesheetWageRates(Double wageRate, Double clientChargeRate, Integer employeeID, Integer projectID, Date from, Date to) {
        if (to == null) {
            update("update EdsTimeSheet set wagerate = ?, clientChargeRate = ? where employeeid = ? and projectid = ? and date >= ?",
                    wageRate, clientChargeRate, employeeID, projectID, from);
        } else {
            update("update EdsTimeSheet set wagerate = ?, clientChargeRate = ? where employeeid = ? and projectid = ? and date >= ? and date < ?",
                    wageRate, clientChargeRate, employeeID, projectID, from, to);
        }
    }

    @Override
    public void updateTimesheetWageRates(Integer employeeID, Integer projectID) {
        update("update EdsTimeSheet set wagerate = 0, clientChargeRate = 0 WHERE employeeid = ? and projectid = ? ", employeeID, projectID);
    }

    public void deleteEmployeeWageClientRateHistory(Integer historyId) {
        update("DELETE EdsProjectEmployeeWageClientRateHistory p WHERE p.objectID = ? ", historyId);
    }

    @Override
    public void clearProjectBudgetCalculatedItems(Integer projectID) {
        StringBuilder sql;

        //clear work stream calculated items
        sql = new StringBuilder();
        sql.append("UPDATE EdsWorkStream ws SET ");
        sql.append("ws.estimatedTime = '0', ");
        sql.append("ws.actualTime = '0', ");
        sql.append("ws.wageAmmount = '0', ");
        sql.append("ws.clientChargeAmmount = '0', ");
        sql.append("ws.actualWageAmount = '0', ");
        sql.append("ws.actualClientChargeAmount = '0', ");
        sql.append("ws.plannedWageAmount = '0', ");
        sql.append("ws.plannedClientChargeAmount = '0' ");
        sql.append("WHERE ws.project.objectID = '").append(projectID).append("' ");
        update(sql.toString());

        //reset default of task
        sql = new StringBuilder();
        sql.append("UPDATE EdsTask t SET ");
        sql.append("t.changedCalculationFields = true, ");
        sql.append("t.calculated = false ");
        sql.append("WHERE t.project.objectID = '").append(projectID).append("' ");
        update(sql.toString());
    }

    /**
     * <h1>... This is method read in database projects parent is null ...</h1>
     * <br/>
     * <h2>... Write developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {16:28 24/05/2001} ...</h3>
     *
     * @return
     */
    @Override
    public List<EdsProject> getProjectsParentIsNull(Integer projectId) {
        EdsUser user = getUser();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct project from EdsProjectEmployee pe ");
        sql.append("inner join pe.project project ");
        sql.append("left join project.parent parent ");
        sql.append("inner join pe.employeeDepartment ed ");
        sql.append("inner join ed.employee employee ");
        sql.append("where parent is null  and (project.deleted is null or  project.deleted=false) ");
        if (projectId != null) {
            sql.append("and project.objectID<>").append(projectId);
        }
        Map param = new HashMap();
        if (!user.getRoleIds().contains(EdsRole.ADMIN) && !user.getRoleIds().contains(EdsRole.DR) && user.getRoleIds().contains(EdsRole.ADMIN_LOCATION)) {// checked for user location role
            EdsLocation location = user.getLocation();
            if (location != null) {
                param.put("userLocation", location);
                sql.append(" and employee.location=:userLocation");
            }
        } else {
            if (user.isClientContact()) {// checked for user role client
                param.put("clientID", user.getClientContact().getClientID());
                sql.append(" and project.client.objectID=:clientID");
            } else {
                boolean seeAllProjectList = ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS);
                if (!(user.getRoleIds().contains(EdsRole.ADMIN) || user.getRoleIds().contains(EdsRole.DR)) && !seeAllProjectList) {
                    param.put("employee", user);
                    sql.append(" and employee=:employee ");
                }
            }
        }
        sql.append(" order by project.name ");
        return findByNamedParams(sql.toString(), param);
    }

    @Override
    public List<EdsProject> getSubProjects(EdsProject parent) {
        return find("select project from EdsProject project where project.parent=? and (project.deleted is null or project.deleted=false)", parent);
    }

    @Override
    public List<Integer> getSubProjectIDs(Integer parentProjectID) {
        return find("select project.objectID from EdsProject project where project.parent.objectID=? and (project.deleted is null or project.deleted=false)", parentProjectID);
    }


    public List<Integer> getPMManagedProjectsEmployeeIDs(Integer employeeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pe.employeeDepartment.employee.objectID from EdsProjectEmployee pe join pe.project p where ");
        sql.append(" (p.manager.objectID=? or p.backupManager.objectID=? ");
        sql.append(" or p.backupManager2.objectID=? ");
        sql.append(" or p.backupManager3.objectID=? ");
        sql.append(" or p.backupManager4.objectID=? ");
        sql.append(" or p.backupManager5.objectID=? ");
        sql.append(" or p.backupManager6.objectID=? ");
        sql.append(" or p.backupManager7.objectID=? ");
        sql.append(" or p.backupManager8.objectID=? ");
        sql.append(" or p.backupManager9.objectID=? ");
        sql.append(" or p.backupManager10.objectID=? ");
        sql.append("  ) and (p.deleted<>true or p.deleted is null) and (pe.employeeDepartment.employee.deleted<>true or pe.employeeDepartment.employee.deleted is null) ");
        return find(sql.toString(), employeeId, employeeId, employeeId, employeeId, employeeId, employeeId, employeeId, employeeId, employeeId, employeeId, employeeId);
    }

    @Override
    public EdsProject getCrmProject() {
        return (EdsProject) findSingle("select project from EdsProject project where project.crmActivityProject is true and deleted is not true");
    }

    @Override
    public List<Integer> getProjectIDsByProjectIDs(List<Integer> arrayList) {
        return (List<Integer>) find("select p.objectID from EdsProject p where p.objectID in (" + ServerUtils.getAsCommoDelimited(arrayList, "0", ",") + ") and " + ServerUtils.checkForDeleted("p.deleted"));
    }

    @Override
    public Map<Integer, Long> getProjectTaskCounts(List<Integer> projectIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.projectid, count(t.id) from \"");
        sql.append(ServerSecurityContext.getInstance().getCompanyId());
        sql.append("\"");
        sql.append(".task t where t.projectid in (");
        sql.append(ServerUtils.getAsCommoDelimited(projectIds, "0", ","));
        sql.append(") and (t.isIssue is null or t.isIssue is not true) and t.deleted is not true group by t.projectid");
        List<Object[]> idList = findNative(sql.toString());
        Map<Integer, Long> idMap = new HashMap<>();
        for (Object[] ids : idList) {
            idMap.put(Integer.valueOf(String.valueOf(ids[0])), Long.valueOf(String.valueOf(ids[1])));
        }
        return idMap;
    }

    @Override
    public List<Integer> getCustomerIDsByProjectManager(EdsUser edsUser) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct p.client.objectID from EdsProject p where (p.manager.objectID = ? ");
        sql.append(" or p.backupManager.objectID = ? ");
        sql.append(" or p.backupManager2.objectID = ? ");
        sql.append(" or p.backupManager3.objectID = ? ");
        sql.append(" or p.backupManager4.objectID = ? ");
        sql.append(" or p.backupManager5.objectID = ? ");
        sql.append(" or p.backupManager6.objectID = ? ");
        sql.append(" or p.backupManager7.objectID = ? ");
        sql.append(" or p.backupManager8.objectID = ? ");
        sql.append(" or p.backupManager9.objectID = ? ");
        sql.append(" or p.backupManager10.objectID = ?) and p.client is not null");
        return (List<Integer>) find(sql.toString(), edsUser.getObjectID(), edsUser.getObjectID(), edsUser.getObjectID(), edsUser.getObjectID(),
                edsUser.getObjectID(), edsUser.getObjectID(), edsUser.getObjectID(), edsUser.getObjectID(), edsUser.getObjectID(), edsUser.getObjectID(), edsUser.getObjectID());
    }

    @Override
    public ListingObjectItem getSaleQuoteList(Integer projectId, ListingFilterParameter fp) {
        boolean projectInLineItemEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        StringBuilder sql = new StringBuilder();
        sql.append("select sq from EdsSaleQuote sq ");
        sql.append(" where sq.objectID ");
        sql.append(" in (select distinct sq.objectID from EdsQuoteItem item ");
        sql.append(" join item.quote sq ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("sq.deleted"));
        if (projectInLineItemEnabled) {
            sql.append(" and item.project.objectID =?");
        } else {
            sql.append(" and (sq.relatedProject.objectID =? or sq.relatedProject.parent.objectID =?)");
        }
        sql.append(") ");
        if (fp.isSaleOrder()) {
            sql.append(" and sq.isSalesOrder is true ");
        } else {
            sql.append(" and (sq.isSalesOrder is null or sq.isSalesOrder is false) ");
        }
        sql.append(" order by ");

        if (fp.getSortField() != null) {
            if ("invoiceNumber".equals(fp.getSortField())) {
                sql.append("sq.number ");
            } else if ("invoiceDate".equals(fp.getSortField())) {
                sql.append("sq.invoiceDate ");
            } else if ("dueDate".equals(fp.getSortField())) {
                sql.append("sq.dueDate ");
            } else if ("client".equals(fp.getSortField())) {
                sql.append("sq.client.name ");
            } else if ("paidAmuount".equals(fp.getSortField())) {
                sql.append("sq.total ");
            } else if ("status".equals(fp.getSortField())) {
                sql.append("sq.status.name ");
            } else if ("currency".equals(fp.getSortField())) {
                sql.append("sq.currency.name ");
            } else if ("creator".equals(fp.getSortField())) {
                sql.append("sq.creator.firstName, sq.creator.lastName ");
            } else if ("manager".equals(fp.getSortField())) {
                sql.append("sq.currentApprover.exactEmployee.firstName, sq.currentApprover.exactEmployee.lastName ");
            } else if ("reference".equals(fp.getSortField())) {
                sql.append("sq.reference  ");
            } else if ("poNumber".equals(fp.getSortField())) {
                sql.append("sq.poNumber  ");
            } else if ("taxTotal".equals(fp.getSortField())) {
                sql.append("sq.totalTaxes  ");
            } else {
                sql.append("sq.objectID ");
            }
            if (fp.isAscending()) {
                sql.append("asc");
            } else sql.append("desc");
        } else {
            sql.append("sq.objectID desc");
        }

        List<EdsSaleQuote> byNamedParams;
        if (projectInLineItemEnabled) {
            byNamedParams = findInterval(sql.toString(), fp.getStart(), fp.getLimit(), projectId);
        } else {
            byNamedParams = findInterval(sql.toString(), fp.getStart(), fp.getLimit(), projectId, projectId);
        }

        //List<EdsSaleQuote> list = findInterval(sql.toString(), fp.getStart(), fp.getLimit(), projectId, projectId);

        sql.setLength(0);
        if (!projectInLineItemEnabled) {
            sql.append("select count(sq.objectID) from EdsSaleQuote sq where ");
            sql.append(ServerUtils.checkForDeleted("sq.deleted") + " and (sq.relatedProject.objectID =? or sq.relatedProject.parent.objectID =?)");
        } else {
            sql.append("select count(distinct sq.objectID) from EdsSaleQuote sq ");
            sql.append("left join sq.quoteItems as qi where ");
            sql.append(ServerUtils.checkForDeleted("sq.deleted") + "  and (qi.project.objectID =? or qi.project.parent.objectID =?)");
        }
        if (fp.isSaleOrder()) {
            sql.append(" and sq.isSalesOrder is true ");
        } else {
            sql.append(" and (sq.isSalesOrder is null or sq.isSalesOrder is false) ");
        }
        Long totalCount = (Long) findSingle(sql.toString(), projectId, projectId);

        return new ListingObjectItem(byNamedParams, totalCount);
    }

    public boolean isDepartmentLeaderOfProject(Integer projectID, Integer userID) {
        List<Long> projects = (List<Long>) findInterval("select distinct(prs.project.objectID) from EdsDepartment dept left join dept.members mem left join mem.projects prs where dept.leader.objectID = ? and prs.project.objectID = ?", 0, 0, userID, projectID);
        if (projects != null) {
            return (projects.size() > 0);
        }
        return false;

    }

    @Override
    public List<Object[]> getProjectNumberById(Integer projectID) {
        return (List<Object[]>) find("select p.number,c.objectID from EdsProject p left join p.client c where p.deleted = false and p.objectID = ?", projectID);
    }

    @Override
    public String getSavedNumberformat(Integer objectID) {
        return (String) findSingle("select p.savedNumberFormula from EdsProject p where p.objectID =" + objectID);
    }

    public List getResourceUtilProjectReport(Date startDate, Date endDate, Integer start, ListingFilterParameter filterParameter) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EdsReference notStarted = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
        EdsReference ongoing = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ONGOING);
        EdsReference complete = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.COMPLETED);

        String projectAndTaskNotDeleted = " WHERE pe.isdeleted is not true AND p.isdeleted is not true \n";
        String taskStartDateCheck = "";
        if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.RU_SHOW_PROJECTS_WITHOUT_TASKS)) {
            projectAndTaskNotDeleted += " AND t.deleted is not true \n" +
                    "AND et.deleted is not true \n" +
                    "AND t.isissue is not true \n";

            taskStartDateCheck += " AND t.startdate <= '" + dateFormat.format(endDate) + "' \n";
        }
        String sb = ("SELECT dj.from_date, " + filterParameter.getEmployeeId() + " as eid,p.id as pid,p.name as pname,p.description as pdescription,p.startdate,p.enddate, \n" +
                "SUM(CASE WHEN tsh.dailyestimatedtime is not null and t.startdate <= '" + dateFormat.format(endDate) + "'  and et.deleted is not true THEN tsh.dailyestimatedtime ELSE 0 END) as dailyLoadT, \n" +
                "atraw.dayoff, \n" +
                "(CASE WHEN atraw.dayoff is true OR atraw.holiday is true THEN 0 ELSE (CASE WHEN atraw.leave > 0 THEN atraw.timeslot - atraw.leave ELSE atraw.timeslot END) END) as timeSlotT, \n" +
                "atraw.timeSheet,atraw.holiday,atraw.leave \n" +
                "FROM " + getPublic() + ".datejoin dj \n" +
                "CROSS JOIN (SELECT p.id, max(p.name) as name, max(p.description) as description, max(p.startdate) as startdate, max(p.enddate) as enddate, max(te.employeeid) as employeeid  FROM " + getCompanyId() + ".project p \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".projectEmployee pe ON (p.id = pe.projectid) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et ON (et.projectEmployeeId=pe.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".teamEmployee te ON (te.id = pe.employeeDepartmentId) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".task t ON (et.taskId=t.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".reference re ON (re.id = p.statusid) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".reference re2 ON (re2.id = et.statusid) \n" +
                projectAndTaskNotDeleted +
                employeeManager.getRolePermissionForPoject() + " \n" +
                (filterParameter.getEmployeeId() != null ? ("AND te.employeeid=" + filterParameter.getEmployeeId() + " \n") : "") +
                ("AND (p.statusid=" + notStarted.getObjectID()) +
                (" OR p.statusid=" + ongoing.getObjectID()) +
                (" OR (p.statusid=" + complete.getObjectID() + " and p.completedDate is not null AND p.completedDate>='" + dateFormat.format(startDate) + "') ") +
                (" OR (re.isSystemReference is not true and re.deleted is not true)) \n") +
                "AND (p.startdate<='" + dateFormat.format(endDate) + "') \n" +
                taskStartDateCheck +
                "AND ((et.completedDate>='" + dateFormat.format(startDate) + "' AND re2.code='" + EdsTask.COMPLETED + "' ) " +
                "OR (et.closeddate is not null AND et.closeddate>='" + dateFormat.format(startDate) + "' AND re2.code='" + EdsTask.CLOSED + "' ) " +
                "OR re2.code='" + EdsTask.IN_PROGRESS + "' OR re2.code='" + EdsTask.NOT_STARTED + "' OR re2.code='" + EdsTask.WAITING_FOR_SOMEONE_ELSE + "' " +
                "OR (re2.isSystemReference is not true and re2.deleted is not true)) \n" +
                (filterParameter.getProjectId() != null ? ("AND p.id=" + filterParameter.getProjectId() + " \n") : "") +
                "GROUP BY p.id ORDER BY name \n" +
                (start != null ? "OFFSET " + start + " LIMIT " + ResourceUtilReportConstants.PAGE_SIZE : "") + ") as p \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".attendancerawdata atraw ON (atraw.date=dj.from_date AND atraw.employeeid= p.employeeid) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".timesheet tsh ON (tsh.employeeid=p.employeeid AND dj.from_date=tsh.date and tsh.projectid = p.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".employeetask et ON et.id = tsh.employeetaskid \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".task t ON (t.id=tsh.taskid)  \n" +
                "WHERE  (dj.from_date between '" + dateFormat.format(startDate) + "' AND '" + dateFormat.format(endDate) + "') \n" +
                (filterParameter.isShowFilledCells() ? (" AND tsh.dailyestimatedtime > 0 \n") : "") +
                "GROUP BY p.id,dj.from_date,p.name,p.description,p.startdate,p.enddate,atraw.dayoff,atraw.timeSlot,atraw.timeSheet,atraw.holiday,atraw.leave \n" +
                "ORDER BY p.name");
        return findNative(sb);
    }

    public ExportToExcelItem getResourceUtilEmployeeExcel(ListingFilterParameter fp, Boolean projectSelected) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EdsReference notStarted = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
        EdsReference ongoing = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ONGOING);
        EdsReference complete = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.COMPLETED);
        String timeZoneCurrentUser = getUser().getUserTimezone().getID();
        EdsUser user = getUser();

        // for filter by positionIds
        String positionSql = " ";
        if (fp.getPositionIDs() != null && !fp.getPositionIDs().isEmpty()) {
            if (fp.getNoPosition()) {
                positionSql = " and (e.positionid in (" + fp.getPositionIDs() + ") ";
                positionSql += " or e.positionid is null) \n";
            } else {
                positionSql = " AND e.positionid in (" + fp.getPositionIDs() + ") \n";
            }
        } else if (fp.getNoPosition()) {
            positionSql = " AND e.positionid is null \n";
        }

        int activeEmployeeStsId = 0;
        if (fp.isShowActive()) {
            EdsReference reference = referenceManager.getByCode(Constants.EMPLOYEE_STATUS_ACTIVE);
            if (reference != null && reference.getObjectID() != null) {
                activeEmployeeStsId = reference.getObjectID();
            }
        }

        String employeeDetailsQuery = ("select e.id, cr.*, cr2.*, cr3.*\n" +
                "from " + getCompanyId() + ".employee e \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".myuser mu ON (mu.id=e.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te ON (te.employeeId=e.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".team tem ON (tem.id=te.teamId) \n" +
                "\n" +
                "left join (\n" +
                "SELECT \"employeeID\" emplId1,\n" +
                "sum(\"1\") leave1,sum(\"2\") leave2,sum(\"3\") leave3,sum(\"4\") leave4,sum(\"5\") leave5,sum(\"6\") leave6,sum(\"7\") leave7,sum(\"8\") leave8,sum(\"9\") leave9,sum(\"10\") leave10,\n" +
                "sum(\"11\") leave11,sum(\"12\") leave12,sum(\"13\") leave13,sum(\"14\") leave14,sum(\"15\") leave15,sum(\"16\") leave16,sum(\"17\") leave17,sum(\"18\") leave18,sum(\"19\") leave19,sum(\"20\") leave20,\n" +
                "sum(\"21\") leave21,sum(\"22\") leave22,sum(\"23\") leave23,sum(\"24\") leave24,sum(\"25\") leave25,sum(\"26\") leave26,sum(\"27\") leave27,sum(\"28\") leave28,sum(\"29\") leave29,sum(\"30\") leave30,\n" +
                "sum(\"31\") leave31,\n" +
                "sum(coalesce(\"1\",0)+coalesce(\"2\",0)+coalesce(\"3\",0)+coalesce(\"4\",0)+coalesce(\"5\",0)+coalesce(\"6\",0)+coalesce(\"7\",0)+coalesce(\"8\",0)+coalesce(\"9\",0)+coalesce(\"10\",0)\n" +
                "+coalesce(\"11\",0)+coalesce(\"12\",0)+coalesce(\"13\",0)+coalesce(\"14\",0)+coalesce(\"15\",0)+coalesce(\"16\",0)+coalesce(\"17\",0)+coalesce(\"18\",0)+coalesce(\"19\",0)+coalesce(\"20\",0)+\n" +
                "coalesce(\"21\",0)+coalesce(\"22\",0)+coalesce(\"23\",0)+coalesce(\"24\",0)+coalesce\n" +
                "(\"25\",0)+coalesce(\"26\",0)+coalesce(\"27\",0)+coalesce(\"28\",0)+coalesce(\"29\",0)+coalesce(\"30\",0)+coalesce(\"31\",0)) sumAmount\n" +
                "FROM crosstab(\n" +
                "$$ select mu.id, extract(day from dj.from_date) as d, atraw.leave \n" +
                "\n" +
                "val\n" +
                "FROM " + getPublic() + ".datejoin dj\n" +
                "CROSS JOIN " + getCompanyId() + ".employee e\n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".myuser mu ON (mu.id=e.id)\n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".attendancerawdata atraw ON (atraw.date=dj.from_date AND atraw.employeeid=e.id)\n" +
                "WHERE (dj.from_date between '" + dateFormat.format(fp.getStartDate()) + "' AND '" + dateFormat.format(fp.getEndDate()) + "')\n" +
                "AND mu.deleted is not true\n" +
                "$$,\n" +
                "$$ SELECT m FROM generate_series(1,31) m $$\n" +
                ") AS (\n" +
                "\"employeeID\" int\n" +
                ", \"1\" bigint, \"2\" bigint, \"3\" bigint, \"4\" bigint, \"5\" bigint, \"6\" bigint, \"7\" bigint, \"8\" bigint, \"9\" bigint, \"10\" bigint\n" +
                ",\"11\" bigint, \"12\" bigint, \"13\" bigint, \"14\" bigint, \"15\" bigint, \"16\" bigint, \"17\" bigint, \"18\" bigint, \"19\" bigint, \"20\" bigint\n" +
                ", \"21\" bigint, \"22\" bigint, \"23\" bigint, \"24\" bigint, \"25\" bigint, \"26\" bigint, \"27\" bigint, \"28\" bigint, \"29\" bigint, \"30\" bigint\n" +
                ",\"31\" bigint\n" +
                ")\n" +
                "group by \"employeeID\"\n" +
                "\n" +
                ") cr on emplId1 = mu.id\n" +
                "\n" +
                "left join (\n" +
                "SELECT \"employeeID\" emplId2,\n" +
                "sum(\"1\") tsl1,sum(\"2\") tsl2,sum(\"3\") tsl3,sum(\"4\") tsl4,sum(\"5\") tsl5,sum(\"6\") tsl6,sum(\"7\") tsl7,sum(\"8\") tsl8,sum(\"9\") tsl9,sum(\"10\") tsl10,\n" +
                "sum(\"11\") tsl11,sum(\"12\") tsl12,sum(\"13\") tsl13,sum(\"14\") tsl14,sum(\"15\") tsl15,sum(\"16\") tsl16,sum(\"17\") tsl17,sum(\"18\") tsl18,sum(\"19\") tsl19,sum(\"20\") tsl20,\n" +
                "sum(\"21\") tsl21,sum(\"22\") tsl22,sum(\"23\") tsl23,sum(\"24\") tsl24,sum(\"25\") tsl25,sum(\"26\") tsl26,sum(\"27\") tsl27,sum(\"28\") tsl28,sum(\"29\") tsl29,sum(\"30\") tsl30,\n" +
                "sum(\"31\") tsl31, \n" +
                "sum(coalesce(\"1\",0)+coalesce(\"2\",0)+coalesce(\"3\",0)+coalesce(\"4\",0)+coalesce(\"5\",0)+coalesce(\"6\",0)+coalesce(\"7\",0)+coalesce(\"8\",0)+coalesce(\"9\",0)+coalesce(\"10\",0)\n" +
                "+coalesce(\"11\",0)+coalesce(\"12\",0)+coalesce(\"13\",0)+coalesce(\"14\",0)+coalesce(\"15\",0)+coalesce(\"16\",0)+coalesce(\"17\",0)+coalesce(\"18\",0)+coalesce(\"19\",0)+coalesce(\"20\",0)+\n" +
                "coalesce(\"21\",0)+coalesce(\"22\",0)+coalesce(\"23\",0)+coalesce(\"24\",0)+coalesce\n" +
                "(\"25\",0)+coalesce(\"26\",0)+coalesce(\"27\",0)+coalesce(\"28\",0)+coalesce(\"29\",0)+coalesce(\"30\",0)+coalesce(\"31\",0)) sumTimeslot1\n" +
                "FROM crosstab(\n" +
                "$$ select mu.id, extract(day from dj.from_date) as d, \n" +
                "(CASE WHEN atraw.dayoff is true OR atraw.holiday is true THEN 0 ELSE (CASE WHEN atraw.leave > 0 THEN atraw.timeslot - atraw.leave ELSE atraw.timeslot END) END)\n" +
                "\n" +
                "val \n" +
                "FROM " + getPublic() + ".datejoin dj \n" +
                "CROSS JOIN " + getCompanyId() + ".employee e \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".myuser mu ON (mu.id=e.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".attendancerawdata atraw ON (atraw.date=dj.from_date AND atraw.employeeid=e.id) \n" +
                "WHERE (dj.from_date between '" + dateFormat.format(fp.getStartDate()) + "' AND '" + dateFormat.format(fp.getEndDate()) + "') \n" +
                "AND mu.deleted is not true \n" +
                "$$,\n" +
                "$$ SELECT m FROM generate_series(1,31) m $$ \n" +
                ") AS (\n" +
                "\"employeeID\" int\n" +
                ", \"1\" bigint, \"2\" bigint, \"3\" bigint, \"4\" bigint, \"5\" bigint, \"6\" bigint, \"7\" bigint, \"8\" bigint, \"9\" bigint, \"10\" bigint\n" +
                ",\"11\" bigint, \"12\" bigint, \"13\" bigint, \"14\" bigint, \"15\" bigint, \"16\" bigint, \"17\" bigint, \"18\" bigint, \"19\" bigint, \"20\" bigint\n" +
                ", \"21\" bigint, \"22\" bigint, \"23\" bigint, \"24\" bigint, \"25\" bigint, \"26\" bigint, \"27\" bigint, \"28\" bigint, \"29\" bigint, \"30\" bigint\n" +
                ",\"31\" bigint\n" +
                ")\n" +
                "group by \"employeeID\" \n" +
                "\n" +
                ") cr2 on cr2.emplId2 = mu.id \n" +
                "left join (\n" +
                "SELECT \"employeeID\" emplId3,\n" +
                "sum(\"1\") h1,sum(\"2\") h2,sum(\"3\") h3,sum(\"4\") h4,sum(\"5\") h5,sum(\"6\") h6,sum(\"7\") h7,sum(\"8\") h8,sum(\"9\") h9,sum(\"10\") h10,\n" +
                "sum(\"11\") h11,sum(\"12\") h12,sum(\"13\") h13,sum(\"14\") h14,sum(\"15\") h15,sum(\"16\") h16,sum(\"17\") h17,sum(\"18\") h18,sum(\"19\") h19,sum(\"20\") h20,\n" +
                "sum(\"21\") h21,sum(\"22\") h22,sum(\"23\") h23,sum(\"24\") h24,sum(\"25\") h25,sum(\"26\") h26,sum(\"27\") h27,sum(\"28\") h28,sum(\"29\") h29,sum(\"30\") h30,\n" +
                "sum(\"31\") h31, \n" +
                "sum(coalesce(\"1\",0)+coalesce(\"2\",0)+coalesce(\"3\",0)+coalesce(\"4\",0)+coalesce(\"5\",0)+coalesce(\"6\",0)+coalesce(\"7\",0)+coalesce(\"8\",0)+coalesce(\"9\",0)+coalesce(\"10\",0)\n" +
                "+coalesce(\"11\",0)+coalesce(\"12\",0)+coalesce(\"13\",0)+coalesce(\"14\",0)+coalesce(\"15\",0)+coalesce(\"16\",0)+coalesce(\"17\",0)+coalesce(\"18\",0)+coalesce(\"19\",0)+coalesce(\"20\",0)+\n" +
                "coalesce(\"21\",0)+coalesce(\"22\",0)+coalesce(\"23\",0)+coalesce(\"24\",0)+coalesce\n" +
                "(\"25\",0)+coalesce(\"26\",0)+coalesce(\"27\",0)+coalesce(\"28\",0)+coalesce(\"29\",0)+coalesce(\"30\",0)+coalesce(\"31\",0)) sumTimeslot2\n" +
                "FROM crosstab(\n" +
                "$$ select mu.id, extract(day from dj.from_date) as d, \n" +
                "(CASE WHEN atraw.dayoff is true THEN -2 ELSE (CASE WHEN atraw.holiday is true THEN -1 ELSE 0 END) END)\n" +
                "\n" +
                "val \n" +
                "FROM " + getPublic() + ".datejoin dj \n" +
                "CROSS JOIN " + getCompanyId() + ".employee e \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".myuser mu ON (mu.id=e.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".attendancerawdata atraw ON (atraw.date=dj.from_date AND atraw.employeeid=e.id) \n" +
                "WHERE (dj.from_date between '" + dateFormat.format(fp.getStartDate()) + "' AND '" + dateFormat.format(fp.getEndDate()) + "') \n" +
                "AND mu.deleted is not true \n" +
                "$$,\n" +
                "$$ SELECT m FROM generate_series(1,31) m $$ \n" +
                ") AS (\n" +
                "\"employeeID\" int\n" +
                ", \"1\" bigint, \"2\" bigint, \"3\" bigint, \"4\" bigint, \"5\" bigint, \"6\" bigint, \"7\" bigint, \"8\" bigint, \"9\" bigint, \"10\" bigint\n" +
                ",\"11\" bigint, \"12\" bigint, \"13\" bigint, \"14\" bigint, \"15\" bigint, \"16\" bigint, \"17\" bigint, \"18\" bigint, \"19\" bigint, \"20\" bigint\n" +
                ", \"21\" bigint, \"22\" bigint, \"23\" bigint, \"24\" bigint, \"25\" bigint, \"26\" bigint, \"27\" bigint, \"28\" bigint, \"29\" bigint, \"30\" bigint\n" +
                ",\"31\" bigint\n" +
                ")\n" +
                "group by \"employeeID\" \n" +
                "\n" +
                ") cr3 on cr3.emplId3 = mu.id \n" +
                "where mu.deleted = false " +
                (fp.isShowActive() ? "and mu.accountstatusid =" + activeEmployeeStsId + " \n" : "") +
                (fp.getEmployeeIDs() != null ? ("AND mu.id in (" + fp.getEmployeeIDs() + ") \n") : "") +

                employeeManager.getRolePermission() +

                positionSql +
                "order by mu.firstname, mu.lastname, mu.id\n");
        List<Object[]> employeeDetails = findNative(employeeDetailsQuery);

        LinkedHashMap<String, BigDecimal[][]> leaveSummary = new LinkedHashMap<>();
        for (Object[] row : employeeDetails) {
            String employeeLrKey = row[1] + "";

            int leaveSheetStart = 2;
            int leaveSheetSummary = 33;
            int timeslotStart = 35;
            int timeslotSummary = 66;
            int dayoffStart = 68;

            BigDecimal[][] employeeLeaveSummary = new BigDecimal[3][fp.getDay() + 1];// fp.getDay() = month days count
            if (leaveSummary.containsKey(employeeLrKey)) {
                employeeLeaveSummary = leaveSummary.get(employeeLrKey);
            } else {
                leaveSummary.put(employeeLrKey, employeeLeaveSummary);
            }
            for (int i = 0; i < fp.getDay(); i++) {// fp.getDay() = month days count
                //leaveSheet
                if (row[leaveSheetStart] != null) {
                    employeeLeaveSummary[0][i] = (BigDecimal) row[leaveSheetStart];
                }
                //timeslots
                if (row[timeslotStart] != null) {
                    employeeLeaveSummary[1][i] = (BigDecimal) row[timeslotStart];
                }
                //dayoff and holidays
                if (row[dayoffStart] != null) {
                    employeeLeaveSummary[2][i] = (BigDecimal) row[dayoffStart];
                }
                leaveSheetStart++;
                timeslotStart++;
                dayoffStart++;
            }
            //leaveSheet total
            if (row[leaveSheetSummary] != null) {
                employeeLeaveSummary[0][fp.getDay()] = (BigDecimal) row[leaveSheetSummary];
            }
            //timeslots total
            if (row[timeslotSummary] != null) {
                employeeLeaveSummary[1][fp.getDay()] = (BigDecimal) row[timeslotSummary];
            }

            leaveSummary.put(employeeLrKey, employeeLeaveSummary);
        }


        String rolePermissionProj = "";
        if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_ALL_EMPLOYEES)) {
            //
        } else if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_DEPARTMENT_EMPLOYEES)) {
            Integer myTeamId = user.getEmployee().getEmployeeDepartment().getTeam().getObjectID();
            rolePermissionProj += "AND p.id in \n" +
                    "(SELECT distinct p.id from " + getCompanyId() + ".task t\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".project p ON (p.id = t.projectid) \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et ON (et.taskid = t.id) \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".projectemployee pe ON (et.projectemployeeId=pe.id)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te ON (pe.employeeDepartmentId=te.id)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".team tem ON (tem.id=te.teamid)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employee e ON (te.employeeid=e.id)\n" +
                    "WHERE tem.id = " + myTeamId + ") ";
        } else if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_PROJECT_EMPLOYEES)) {
            rolePermissionProj += "AND p.id in \n" +
                    "(SELECT distinct p.id from " + getCompanyId() + ".task t\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".project p ON (p.id = t.projectid) \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et ON (et.taskid = t.id) \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".projectemployee pe ON (et.projectemployeeId=pe.id)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te ON (pe.employeeDepartmentId=te.id)\n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employee e ON (te.employeeid=e.id)\n" +
                    "WHERE e.id = " + user.getObjectID() + ") ";
        } else if (ServerUtils.hasPermission(PermissionConstants.PM_SHOW_SUPERVISED_EMPLOYEES)) {
            rolePermissionProj += " AND (te.employeeid IN \n" +
                    "(SELECT e.id from " + getCompanyId() + ".employee e \n" +
                    "LEFT OUTER JOIN " + getCompanyId() + ".employeeprofile ep on (ep.employeeid=e.id) where ep.reportsto=" + user.getObjectID() + ")\n" +
                    " or te.employeeid = " + user.getObjectID() + ")";
        } else {
            rolePermissionProj += " AND (te.employeeid = " + user.getObjectID() + ") ";
        }
        String projectDetailsQuery = "select us.firstname, us.lastname, us.id emp_id, t.* from " + getCompanyId() + ".employee e \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".myuser us  ON (us.id=e.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te ON (te.employeeId=e.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".team tem ON (tem.id=te.teamId) \n" +
                "LEFT JOIN (" +
                "select mu.id employee_id, p.name project_name, p.id project_id, t.name task_name, t.id task_id, cr.*, cr2.*, te.isdeleted tdel, pe.isdeleted pdel \n" +
                "FROM " + getCompanyId() + ".task t \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et ON (et.taskId=t.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".projectEmployee pe ON (et.projectemployeeId=pe.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".teamEmployee te ON (pe.employeeDepartmentId=te.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".team tem ON (te.teamid=te.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".employee e ON te.employeeid = e.id \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".myuser mu ON mu.id = e.id \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".project p ON p.id = t.projectId \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".reference re ON (re.id=p.statusid) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".reference re2 ON (re2.id = et.statusid) \n" +
                "\n" +
                "left join (\n" +
                "SELECT \"taskId\", \"projectID\", \"employeeID\", \n" +
                "sum(\"1\") tsh1,sum(\"2\") tsh2,sum(\"3\") tsh3,sum(\"4\") tsh4,sum(\"5\") tsh5,sum(\"6\") tsh6,sum(\"7\") tsh7,sum(\"8\") tsh8,sum(\"9\") tsh9,sum(\"10\") tsh10,\n" +
                "sum(\"11\") tsh11,sum(\"12\") tsh12,sum(\"13\") tsh13,sum(\"14\") tsh14,sum(\"15\") tsh15,sum(\"16\") tsh16,sum(\"17\") tsh17,sum(\"18\") tsh18,sum(\"19\") tsh19,sum(\"20\") tsh20,\n" +
                "sum(\"21\") tsh21,sum(\"22\") tsh22,sum(\"23\") tsh23,sum(\"24\") tsh24,sum(\"25\") tsh25,sum(\"26\") tsh26,sum(\"27\") tsh27,sum(\"28\") tsh28,sum(\"29\") tsh29,sum(\"30\") tsh30,\n" +
                "sum(\"31\") tsh31,\n" +
                "sum(coalesce(\"1\",0)+coalesce(\"2\",0)+coalesce(\"3\",0)+coalesce(\"4\",0)+coalesce(\"5\",0)+coalesce(\"6\",0)+coalesce(\"7\",0)+coalesce(\"8\",0)+coalesce(\"9\",0)+coalesce(\"10\",0)\n" +
                "+coalesce(\"11\",0)+coalesce(\"12\",0)+coalesce(\"13\",0)+coalesce(\"14\",0)+coalesce(\"15\",0)+coalesce(\"16\",0)+coalesce(\"17\",0)+coalesce(\"18\",0)+coalesce(\"19\",0)+coalesce(\"20\",0)+\n" +
                "coalesce(\"21\",0)+coalesce(\"22\",0)+coalesce(\"23\",0)+coalesce(\"24\",0)+coalesce\n" +
                "(\"25\",0)+coalesce(\"26\",0)+coalesce(\"27\",0)+coalesce(\"28\",0)+coalesce(\"29\",0)+coalesce(\"30\",0)+coalesce(\"31\",0)) sumAmountTimesheet\n" +
                "FROM crosstab(\n" +
                "$$ select sh.employeeID || '\\\\' || sh.projectID || '\\\\' || t.id,  t.id, sh.projectID, sh.employeeID, extract(day from sh.date) as d, sh.timespent \n" +
                "\n" +
                "val\n" +
                "from " + getCompanyId() + ".task t left join " + getCompanyId() + ".timesheet sh on t.id=sh.taskId where t.deleted = false\n" +
                /*"and sh.employeeid = " + employeeID + "\n" +*/
                "and sh.date between '" + dateFormat.format(fp.getStartDate()) + "' and '" + dateFormat.format(fp.getEndDate()) + "'\n" +
                " group by sh.employeeid, sh.projectid, t.id, sh.date, sh.timespent " +
                "$$,\n" +
                "$$ SELECT m FROM generate_series(1,31) m $$\n" +
                ") AS (\n" +
                "\"name\" varchar, \"taskId\" int ,\"projectID\" int, \"employeeID\" int\n" +
                ", \"1\" bigint, \"2\" bigint, \"3\" bigint, \"4\" bigint, \"5\" bigint, \"6\" bigint, \"7\" bigint, \"8\" bigint, \"9\" bigint, \"10\" bigint\n" +
                ",\"11\" bigint, \"12\" bigint, \"13\" bigint, \"14\" bigint, \"15\" bigint, \"16\" bigint, \"17\" bigint, \"18\" bigint, \"19\" bigint, \"20\" bigint\n" +
                ", \"21\" bigint, \"22\" bigint, \"23\" bigint, \"24\" bigint, \"25\" bigint, \"26\" bigint, \"27\" bigint, \"28\" bigint, \"29\" bigint, \"30\" bigint\n" +
                ",\"31\" bigint\n" +
                ")\n" +
                "group by  \"taskId\", \"projectID\", \"employeeID\"\n" +
                "\n" +
                ") cr on \"taskId\" = t.id and \"projectID\" = p.id and \"employeeID\" = mu.id\n" +
                "left join (\n" +
                "SELECT \"taskId2\", \"projectID2\", \"employeeID2\", \n" +
                "sum(\"1\") est1,sum(\"2\") est2,sum(\"3\") est3,sum(\"4\") est4,sum(\"5\") est5,sum(\"6\") est6,sum(\"7\") est7,sum(\"8\") est8,sum(\"9\") est9,sum(\"10\") est10,\n" +
                "sum(\"11\") est11,sum(\"12\") est12,sum(\"13\") est13,sum(\"14\") est14,sum(\"15\") est15,sum(\"16\") est16,sum(\"17\") est17,sum(\"18\") est18,sum(\"19\") est19,sum(\"20\") est20,\n" +
                "sum(\"21\") est21,sum(\"22\") est22,sum(\"23\") est23,sum(\"24\") est24,sum(\"25\") est25,sum(\"26\") est26,sum(\"27\") est27,sum(\"28\") est28,sum(\"29\") est29,sum(\"30\") est30,\n" +
                "sum(\"31\") est31,\n" +
                "sum(coalesce(\"1\",0)+coalesce(\"2\",0)+coalesce(\"3\",0)+coalesce(\"4\",0)+coalesce(\"5\",0)+coalesce(\"6\",0)+coalesce(\"7\",0)+coalesce(\"8\",0)+coalesce(\"9\",0)+coalesce(\"10\",0)\n" +
                "+coalesce(\"11\",0)+coalesce(\"12\",0)+coalesce(\"13\",0)+coalesce(\"14\",0)+coalesce(\"15\",0)+coalesce(\"16\",0)+coalesce(\"17\",0)+coalesce(\"18\",0)+coalesce(\"19\",0)+coalesce(\"20\",0)+\n" +
                "coalesce(\"21\",0)+coalesce(\"22\",0)+coalesce(\"23\",0)+coalesce(\"24\",0)+coalesce\n" +
                "(\"25\",0)+coalesce(\"26\",0)+coalesce(\"27\",0)+coalesce(\"28\",0)+coalesce(\"29\",0)+coalesce(\"30\",0)+coalesce(\"31\",0)) sumEstimate\n" +
                "FROM crosstab(\n" +
                "$$ select sh.employeeID || '\\\\' || sh.projectID || '\\\\' || t.id, t.id, sh.projectID, sh.employeeID, extract(day from sh.date) as d, sh.dailyestimatedtime \n" +
                "\n" +
                "val\n" +
                "from " + getCompanyId() + ".task t left join " + getCompanyId() + ".timesheet sh on t.id=sh.taskId \n" +
                " left join " + getCompanyId() + ".employeetask et on et.id=sh.employeetaskId where t.deleted is not true and et.deleted is not true \n" +
                /*"and sh.employeeid = '" + employeeID + "'\n" +*/
                "and sh.date between '" + dateFormat.format(fp.getStartDate()) + "' and '" + dateFormat.format(fp.getEndDate()) + "'\n" +
                " group by sh.employeeid, sh.projectid, t.id, sh.date, sh.dailyestimatedtime " +
                "$$,\n" +
                "$$ SELECT m FROM generate_series(1,31) m $$\n" +
                ") AS (\n" +
                " \"name\" varchar, \"taskId2\" int ,\"projectID2\" int, \"employeeID2\" int\n" +
                ", \"1\" bigint, \"2\" bigint, \"3\" bigint, \"4\" bigint, \"5\" bigint, \"6\" bigint, \"7\" bigint, \"8\" bigint, \"9\" bigint, \"10\" bigint\n" +
                ",\"11\" bigint, \"12\" bigint, \"13\" bigint, \"14\" bigint, \"15\" bigint, \"16\" bigint, \"17\" bigint, \"18\" bigint, \"19\" bigint, \"20\" bigint\n" +
                ", \"21\" bigint, \"22\" bigint, \"23\" bigint, \"24\" bigint, \"25\" bigint, \"26\" bigint, \"27\" bigint, \"28\" bigint, \"29\" bigint, \"30\" bigint\n" +
                ",\"31\" bigint\n" +
                ")\n" +
                "GROUP by \"taskId2\", \"projectID2\", \"employeeID2\"\n" +
                "\n" +
                ") cr2 on cr2.\"taskId2\" = t.id and cr2.\"projectID2\" = p.id and cr2.\"employeeID2\" = mu.id \n" +
                "where mu.deleted = false  \n" +
                rolePermissionProj +
                "AND t.deleted is not true \n" +
                "AND et.deleted is not true \n" +
                "AND t.isissue is not true \n" +
                "AND (p.statusid=" + notStarted.getObjectID() + " OR p.statusid=" + ongoing.getObjectID() +
                (" OR (p.statusid=" + complete.getObjectID() + " and p.completedDate is not null AND p.completedDate >= '" + dateFormat.format(fp.getStartDate()) + "')") +
                " OR (re.isSystemReference is not true and re.deleted is not true)) \n" +
                "AND (p.startdate<='" + dateFormat.format(fp.getEndDate()) + "') \n" +
                "AND t.startdate<='" + dateFormat.format(fp.getEndDate()) + "' \n" +
                "AND ((et.completedDate>='" + dateFormat.format(fp.getStartDate()) + "' AND re2.code='" + EdsTask.COMPLETED + "' ) " +
                "OR (et.closeddate is not null AND et.closeddate>='" + dateFormat.format(fp.getStartDate()) + "' AND re2.code='" + EdsTask.CLOSED + "' ) " +
                "OR re2.code='" + EdsTask.IN_PROGRESS + "' OR re2.code='" + EdsTask.NOT_STARTED + "' OR re2.code='" + EdsTask.WAITING_FOR_SOMEONE_ELSE + "' OR (re2.isSystemReference is not true and re2.deleted is not true))" +
                ") t on t.employee_id = us.id\n" +
                "where us.deleted = false\n" +
                (fp.isShowActive() ? "and us.accountstatusid =" + activeEmployeeStsId + " \n" : "") +
                (fp.getEmployeeIDs() != null ? ("AND us.id in (" + fp.getEmployeeIDs() + ") \n") : "") +
                employeeManager.getRolePermission() +
                (fp.isShowFilledCells() ? ("AND sumEstimate > 0  \n") : "") +
                positionSql +
                "order by us.firstname, us.lastname, us.id, t.project_name, t.task_name";


        List<Object[]> projectDetails = findNative(projectDetailsQuery);

        List<Object[]> tasklessProjects = new ArrayList<>();
        HashMap<String, List<NameValuePair>> projectsWithoutTasks = new HashMap<>();
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.RU_SHOW_PROJECTS_WITHOUT_TASKS)) {
            String tasklessProjectsQuery = "SELECT distinct  te.employeeid, p.id as pid, p.name as pname \n" +
                    " FROM " + getCompanyId() + ".project p  \n" +
                    " LEFT OUTER JOIN " + getCompanyId() + ".projectemployee pe on(p.id=pe.projectid ) \n" +
                    " LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et on (et.projectEmployeeId=pe.id) \n" +
                    " LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te on (pe.employeedepartmentid=te.id ) \n" +
                    " LEFT OUTER JOIN " + getCompanyId() + ".task t ON (et.taskId=t.id) \n" +
                    " LEFT OUTER JOIN " + getCompanyId() + ".reference re on (re.id = p.statusid) \n" +
                    " LEFT OUTER JOIN " + getCompanyId() + ".reference re2 ON (re2.id = et.statusid) \n" +
                    " WHERE pe.isdeleted is not true \n" +
                    " AND p.isdeleted is not true \n" +
                    " AND te.isdeleted is not true \n" +
                    (projectSelected && fp.getProjectIds() != null ? " AND p.id in (" + fp.getProjectIds() + ")" :
                            (fp.getEmployeeIDs() != null ? ("AND p.id in (SELECT p.id from " + getCompanyId() + ".project p\n" +
                                                            " LEFT OUTER JOIN " + getCompanyId() + ".projectemployee pe on(p.id=pe.projectid ) \n" +
                                                            " LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et on (et.projectEmployeeId=pe.id) \n" +
                                                            " LEFT OUTER JOIN " + getCompanyId() + ".teamemployee te on (pe.employeedepartmentid=te.id ) \n" +
                                                            " where te.employeeid in (" + fp.getEmployeeIDs() + ")) \n") : "")
                    ) +
                    (fp.getEmployeeIDs() != null ? ("AND te.employeeid in (" + fp.getEmployeeIDs() + ") \n") : "") +

                    employeeManager.getRolePermission() +

                    " AND (p.statusid=" + notStarted.getObjectID() + " or p.statusid=" + ongoing.getObjectID() + " or (re.isSystemReference<>true and re.deleted<>true) ) \n" +
                    " AND (re2.code='" + EdsTask.IN_PROGRESS + "' OR re2.code='" + EdsTask.NOT_STARTED + "' OR re2.code='" + EdsTask.WAITING_FOR_SOMEONE_ELSE + "' OR (re2.isSystemReference is not true and re2.deleted is not true)) \n" +
                    " AND t.id is null \n" +
                    " AND (p.startdate<='" + dateFormat.format(fp.getEndDate()) + "' ) ORDER BY te.employeeid \n";

            tasklessProjects = findNative(tasklessProjectsQuery);
            String empIdKey = "";
            String projectKey = "";
            for (Object[] projects : tasklessProjects) {
                if (projects[0] != null) {
                    empIdKey = projects[0].toString();
                }
                projectKey = empIdKey + "||" + projects[1];
                String projectName = projects[2] != null ? projects[2].toString() : "";
                if (projectsWithoutTasks.containsKey(empIdKey)) {
                    projectsWithoutTasks.get(empIdKey).add(new BasicNameValuePair(projectKey, projectName));
                } else {
                    List<NameValuePair> pKeys = new ArrayList<>();
                    pKeys.add(new BasicNameValuePair(projectKey, projectName));
                    projectsWithoutTasks.put(empIdKey, pKeys);
                }
            }
        }

        LinkedHashMap<String, BigDecimal[][]> tasksSummary = new LinkedHashMap<>();
        LinkedHashMap<String, BigDecimal[]> projectSum = new LinkedHashMap<>();
        LinkedHashMap<String, BigDecimal[][]> employeeSum = new LinkedHashMap<>();

        LinkedHashMap<String, String> printOrderWithNames = new LinkedHashMap<>();

        String lastEmployeeId = "";

        for (Object[] row : projectDetails) {
            String key = row[2] + "||" + row[5] + "||" + row[7];
            if (tasksSummary.containsKey(key)) {
                continue;
            }
            String projectKey = row[2] + "||" + row[5];
            String employeeKey = row[2] + "";
            Boolean teamEmployeeDeleted = (Boolean) row[78];
            Boolean projectEmployeeDeleted = (Boolean) row[79];

            int timesheetStart = 11;
            int timesheetSummary = 42;
            int estimateStart = 46;
            int estimateSummary = 77;

            BigDecimal[] projectSummary = new BigDecimal[fp.getDay() + 1]; // fp.getDay = month days count
            if (projectSum.containsKey(projectKey)) {
                projectSummary = projectSum.get(projectKey);
            } else {
                projectSum.put(projectKey, projectSummary);
            }

            BigDecimal[][] employeeSummary = new BigDecimal[5][fp.getDay() + 1];
            if (employeeSum.containsKey(employeeKey)) {
                employeeSummary = employeeSum.get(employeeKey);
            } else {
                employeeSum.put(employeeKey, employeeSummary);
            }

            BigDecimal[][] summary = new BigDecimal[2][fp.getDay() + 1];
            BigDecimal taskEstimateTotal = BigDecimal.ZERO;
            for (int i = 0; i < fp.getDay(); i++) {
                //timesheets
                if (row[timesheetStart] != null) {

                    summary[0][i] = (BigDecimal) row[timesheetStart];

                    employeeSummary[0][i] = employeeSummary[0][i] == null ? BigDecimal.ZERO : employeeSummary[0][i];
                    employeeSummary[0][i] = employeeSummary[0][i].add((BigDecimal) row[timesheetStart]);
                }

                BigDecimal[][] employeeLeaveSummary = new BigDecimal[3][fp.getDay() + 1];

                boolean dayOff = false;
                if (leaveSummary.containsKey(employeeKey)) {
                    BigDecimal[][] leaveTimeslotHolidaySummary = leaveSummary.get(employeeKey);
                    employeeSummary[2] = leaveTimeslotHolidaySummary[0];//Leave
                    employeeSummary[3] = leaveTimeslotHolidaySummary[1];//Timeslot
                    employeeSummary[4] = leaveTimeslotHolidaySummary[2];//Holiday & Dayoff

                    if (employeeSummary[4][i] != null && employeeSummary[4][i].intValue() == -2) {
                        dayOff = true;
                    }
                }

                //estimates
                if ((row[estimateStart] != null || dayOff) && teamEmployeeDeleted != null && projectEmployeeDeleted != null && !teamEmployeeDeleted && !projectEmployeeDeleted) {
                    summary[1][i] = dayOff ? new BigDecimal(-2) : (BigDecimal) row[estimateStart];
                    BigDecimal currentEstimate = row[estimateStart] == null ? BigDecimal.ZERO : (BigDecimal) row[estimateStart];

                    taskEstimateTotal = taskEstimateTotal.add(currentEstimate);

                    projectSummary[i] = projectSummary[i] == null ? BigDecimal.ZERO : projectSummary[i];
                    projectSummary[i] = projectSummary[i].add(currentEstimate);

                    employeeSummary[1][i] = employeeSummary[1][i] == null ? BigDecimal.ZERO : employeeSummary[1][i];
                    employeeSummary[1][i] = employeeSummary[1][i].add(currentEstimate);
                }
                timesheetStart++;
                estimateStart++;
            }
            //timesheet total
            if (row[timesheetSummary] != null) {
                BigDecimal currentTimesheet = (BigDecimal) row[timesheetSummary];
                BigDecimal currentEmployeeTimesheet = employeeSummary[0][fp.getDay()] == null ? BigDecimal.ZERO : employeeSummary[0][fp.getDay()];

                summary[0][fp.getDay()] = currentTimesheet;
                employeeSummary[0][fp.getDay()] = currentEmployeeTimesheet.add(currentTimesheet);

            }
            //estimate total
            if (row[estimateSummary] != null && teamEmployeeDeleted != null && projectEmployeeDeleted != null && !teamEmployeeDeleted && !projectEmployeeDeleted) {
                //BigDecimal currentEstimate = (BigDecimal) row[estimateSummary];
                BigDecimal currentProjectEstimate = projectSummary[fp.getDay()] == null ? BigDecimal.ZERO : projectSummary[fp.getDay()];
                BigDecimal currentEmployeeEstimate = employeeSummary[1][fp.getDay()] == null ? BigDecimal.ZERO : employeeSummary[1][fp.getDay()];

                summary[1][fp.getDay()] = taskEstimateTotal;
                projectSummary[fp.getDay()] = currentProjectEstimate.add(taskEstimateTotal);

                employeeSummary[1][fp.getDay()] = currentEmployeeEstimate.add(taskEstimateTotal);
            }

            if (!tasksSummary.containsKey(key) && ((teamEmployeeDeleted == null || projectEmployeeDeleted == null) || (!teamEmployeeDeleted && !projectEmployeeDeleted))) {
                if (!printOrderWithNames.containsKey(employeeKey)) {
                    if (printOrderWithNames.size() > 0) {
                        if (projectsWithoutTasks.containsKey(lastEmployeeId)) {
                            List<NameValuePair> tasklessProjectKeys = projectsWithoutTasks.get(lastEmployeeId);
                            for (NameValuePair tasklesProjectKey : tasklessProjectKeys) {
                                printOrderWithNames.put(tasklesProjectKey.getName(), tasklesProjectKey.getValue());
                            }
                        }
                    }
                    printOrderWithNames.put(employeeKey, row[0] + " " + row[1]);
                    lastEmployeeId = employeeKey;
                }
                if (!printOrderWithNames.containsKey(projectKey)) {
                    printOrderWithNames.put(projectKey, (String) row[4]);
                }
                if (!printOrderWithNames.containsKey(key)) {
                    printOrderWithNames.put(key, (String) row[6]);
                }
            } else {
//                System.out.println("Duplicates: " + key);
            }

            if (((teamEmployeeDeleted == null || projectEmployeeDeleted == null) || (!teamEmployeeDeleted && !projectEmployeeDeleted))) {
                tasksSummary.put(key, summary);
            }
            employeeSum.put(employeeKey, employeeSummary);
        }

        //oxirgi id boyicha, tasklessProjectMapdan project listni olib, har biridan ayalanib, printOrderWithNames
        if (printOrderWithNames.size() > 0) {
            if (projectsWithoutTasks.containsKey(lastEmployeeId)) {
                List<NameValuePair> tasklessProjectKeys = projectsWithoutTasks.get(lastEmployeeId);
                for (NameValuePair tasklesProjectKey : tasklessProjectKeys) {
                    printOrderWithNames.put(tasklesProjectKey.getName(), tasklesProjectKey.getValue());
                }
            }
        }

        for (Map.Entry<String, List<NameValuePair>> employeeEntry : projectsWithoutTasks.entrySet()) {
            List<NameValuePair> employeeProjects = employeeEntry.getValue();
            for (NameValuePair project : employeeProjects) {
                projectSum.put(project.getName(), new BigDecimal[fp.getDay() + 1]);
            }
        }

        ExportToExcelItem item = new ExportToExcelItem();
        item.setEmployeeSum(employeeSum);
        item.setLeaveTimeslotSum(leaveSummary);
        item.setPrintOrderWithNames(printOrderWithNames);
        item.setProjectSum(projectSum);
        item.setTasksSummary(tasksSummary);
        return item;
    }

    @Override
    public List<EdsProject> getProjectByName(String projectName) {
        if (projectName != null && !"".equals(projectName)) {
            return (List<EdsProject>) find("select distinct p from EdsProject p where trim(p.name)=?  and p.deleted<>true", projectName);
        }
        return null;
    }

    public List getResourceUtilTaskReport(Date startDate, Date endDate, Integer start, ListingFilterParameter filterParameter) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeZoneCurrentUser = getUser().getUserTimezone().getID();
        List<Integer> taskIdsList = new ArrayList<>();
        String taskIds = "";
        if (filterParameter.isShowFilledCells()) {
            taskIdsList = getFilledTaskIds(filterParameter, startDate, endDate);
            taskIds = ServerUtils.integerListToString(taskIdsList);
        }
        String sc = ("SELECT dj.from_date, " + filterParameter.getEmployeeId() + " as eid,t.id as tid,t.name,t.description, \n" +
                "(CASE WHEN (0!=(SELECT (CASE WHEN atraw.dayoff is true OR atraw.holiday is true THEN 0 ELSE (CASE WHEN atraw.leave > 0 THEN atraw.timeslot - atraw.leave ELSE atraw.timeslot END) END)) AND dj.from_date BETWEEN to_timestamp(to_char(t.startdate at time zone '" + timeZoneCurrentUser + "', 'yyyy-MM-dd'), 'yyyy-MM-dd HH:mm:ss') AND (t.duedate at time zone '" + timeZoneCurrentUser + "')) THEN 1 ELSE -1 END) as workingDay, \n" +
                "(CASE WHEN tsh.dailyEstimatedTime is not null and t.startdate<='" + dateFormat.format(endDate) + "' and et.deleted is not true THEN tsh.dailyEstimatedTime ELSE -1 END) as dailyLoadT, \n" +
                "t.estimatedTime,t.startDate,t.dueDate, \n" +
                "(SELECT (CASE WHEN tsh.timeSpent is not null THEN tsh.timeSpent ELSE 0 END)) as timeSpentT, \n" +
                "atraw.dayoff, \n" +
                "(SELECT (CASE WHEN atraw.dayoff is true OR atraw.holiday is true THEN 0 ELSE (CASE WHEN atraw.leave > 0 THEN atraw.timeslot - atraw.leave ELSE atraw.timeslot END) END)) as timeSlotT, \n" +
                "atraw.timeSheet,atraw.holiday,atraw.leave, \n" +
                "false as issue \n" +
                "FROM " + getPublic() + ".datejoin dj \n" +
                "CROSS JOIN (SELECT t.id, max(te.employeeid) as employeeid, max(t.name) as name, max(t.description) as description, max(t.startdate) as startdate, max(t.duedate) as duedate, max(et.estimatedTime) as estimatedTime FROM " + getCompanyId() + ".task t \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".employeeTask et ON (et.taskId=t.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".projectEmployee pe ON (et.projectemployeeId=pe.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".teamEmployee te ON (pe.employeeDepartmentId=te.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".reference re ON (re.id=et.statusid) \n" +
                "WHERE t.deleted is not true \n" +
                "AND et.deleted is not true \n" +
                "AND t.isissue is not true \n" +
                "AND pe.isdeleted is not true \n" +
                "AND (re.code='" + EdsTask.IN_PROGRESS + "' OR re.code='" + EdsTask.NOT_STARTED + "' " +
                "OR re.code='" + EdsTask.WAITING_FOR_SOMEONE_ELSE + "' " +
                "OR (re.isSystemReference is not true and re.deleted is not true) ) \n" +
                "AND (t.startdate<='" + dateFormat.format(endDate) + "') \n" +
                (filterParameter.getEmployeeId() != null ? ("AND te.employeeid=" + filterParameter.getEmployeeId() + " \n") : "") +
                (filterParameter.getProjectId() != null ? ("AND pe.projectid=" + filterParameter.getProjectId() + " \n") : "") +
                "GROUP BY t.id ORDER BY name \n" +
                (start != null ? "OFFSET " + start + " LIMIT " + ResourceUtilReportConstants.PAGE_SIZE : "") + ") as t \n" +

                "LEFT OUTER JOIN " + getCompanyId() + ".attendancerawdata atraw ON (atraw.date=dj.from_date AND atraw.employeeid=t.employeeid) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".timesheet tsh ON (tsh.employeeid=t.employeeid AND dj.from_date=tsh.date and tsh.taskid = t.id) \n" +
                "LEFT OUTER JOIN " + getCompanyId() + ".employeetask et ON et.id=tsh.employeetaskid \n" +

                "WHERE (dj.from_date between '" + dateFormat.format(startDate) + "' AND '" + dateFormat.format(endDate) + "') \n" +
                (filterParameter.isShowFilledCells() ? "AND t.id in (" + taskIds + ") " : "\n") +
                "GROUP BY dj.from_date, t.id, t.name,t.description,tsh.dailyEstimatedTime,t.estimatedTime,t.startDate,t.dueDate,tsh.timeSpent,atraw.dayoff,atraw.timeSlot,atraw.timeSheet,atraw.holiday,atraw.leave,et.deleted \n" +
                "ORDER BY t.id");
        return findNative(sc);
    }

    private List<Integer> getFilledTaskIds(ListingFilterParameter filterParameter, Date startDate, Date endDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder sql = new StringBuilder();
        sql.append("select tsh.taskId from ").append(getCompanyId()).append(".timesheet tsh \n");
        sql.append("where tsh.employeeid=").append(filterParameter.getEmployeeId()).append(" \n");
        sql.append("and tsh.projectid=").append(filterParameter.getProjectId()).append(" \n");
        sql.append("and tsh.date between '").append(dateFormat.format(startDate)).append("' AND '").append(dateFormat.format(endDate)).append("'\n");
        sql.append("and tsh.dailyestimatedtime>0 ");
        return findNative(sql.toString());
    }

    @Override
    public List<EdsPickList> getProjectRelatedPickLists(Integer projectID) {
        return find("select pl from EdsPickList pl where pl.saleQuote.relatedProject.objectID=? and " + ServerUtils.checkForDeleted("pl.saleQuote.deleted"), projectID);
    }

    @Override
    public List<EdsSaleQuote> getProjectRelatedSalesQuotes(Integer projectID) {
        return find("select sq from EdsSaleQuote sq where sq.relatedProject.objectID=? and " + ServerUtils.checkForDeleted("sq.deleted"), projectID);
    }

    @Override
    public List<EdsPurchaseOrder> getProjectRelatedPurchaseOrders(Integer projectID) {
        return find("select po from EdsPurchaseOrder po where po.relatedProject.objectID=? and " + ServerUtils.checkForDeleted("po.deleted"), projectID);
    }

    @Override
    public List<EdsSaleInvoice> getProjectRelatedSalesInvoices(Integer projectID) {
        return find("select si from EdsSaleInvoice si where si.relatedProject.objectID=? and si.status.code!=? and " + ServerUtils.checkForDeleted("si.deleted"), projectID, Constants.REVERSED);
    }

    @Override
    public List<EdsPurchaseInvoice> getProjectRelatedPurchaseInvoices(Integer projectID) {
        return find("select pi from EdsPurchaseInvoice pi where pi.relatedProject.objectID=? and pi.status.code!=? and " + ServerUtils.checkForDeleted("pi.deleted"), projectID, Constants.REVERSED);
    }

    @Override
    public List<EdsExpenseReport> getProjectRelatedExpenseReports(Integer projectID) {
        return find("select er from EdsExpenseReport er where er.project.objectID = ? and " + ServerUtils.checkForDeleted("er.isDeleted"), projectID);
    }

    public Date getProjectActualStartDate(Integer projectID) {
        return (Date) findSingle("select min(t.actualStartDate) from EdsTask t where t.deleted is not true and t.actualStartDate is not null and t.project.objectID=?", projectID);
    }

    public Date getProjectActualEndDate(Integer projectID) {
        return (Date) findSingle("select max(t.actualEndDate) from EdsTask t where t.deleted is not true and t.actualEndDate is not null and t.project.objectID=?", projectID);
    }

    @Override
    public Float getProjectActualPercentCompleted(Integer projectID) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT sum(estimatedtime) estimatedtime, sum(actualtime) actualtime FROM (");
        sql.append("SELECT sum(coalesce(et.estimatedtime,0)) estimatedtime, 0 actualtime FROM ").append(getCompanyId()).append(".task t \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".employeetask et ON et.taskid = t.id \n");
        sql.append("WHERE t.deleted is not true and et.deleted is not true and t.projectid = ").append(projectID).append(" \n");

        sql.append("UNION \n");

        sql.append("SELECT 0 estimatedtime, sum(coalesce(tsh.timespent,0)) actualtime FROM ").append(getCompanyId()).append(".project p \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".timesheet tsh on tsh.projectid = p.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference ts on ts.id = tsh.statusid \n");
        sql.append("WHERE ts.code = '_APPROVE' \n");
        sql.append("AND p.id = ").append(projectID).append(" \n");
        sql.append(") t ");


        Object[] object = (Object[]) findNativeSingle(sql.toString());

        if (object == null) {
            return 0f;
        }

        BigDecimal estimatedTime = (BigDecimal) object[0];
        BigDecimal actualTime = (BigDecimal) object[1];

        if (estimatedTime == null || estimatedTime.intValue() == 0 || actualTime == null || actualTime.intValue() == 0) {
            return 0f;
        }

        Float percent = (actualTime.floatValue() / estimatedTime.floatValue()) * 100;

        return (percent > 100f && !genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) ? 100f : percent;
    }

    @Override
    public List<EdsProject> getProjectByCompanyID(Integer companyID) {
        String company = "\"" + companyID + "\"";
        String query = "select p.*, 0 as clazz_  from " + company + ".project p where (p.isdeleted<>true or p.isdeleted is null)";
        return findNative(query, EdsProject.class);
    }

    @Override
    public List<EdsProjectPosition> getProjectPositions(Integer projectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pp.*, 0 as clazz_ FROM ").append(getCompanyId()).append(".projectPostion pp \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p on p.id = pp.projectid \n");
        sql.append("WHERE pp.deleted is not true \n");
        sql.append("AND p.id = ").append(projectID);

        return findNative(sql.toString(), EdsProjectPosition.class);
        /*return find("SELECT pp FROM EdsProjectPosition pp join pp.project p where p.objectID = ?", projectID);*/
    }

    @Override
    public EdsProject getProjectByNumber(String parentNumber) {
        return (EdsProject) findSingle("select p from EdsProject p where (p.deleted is false or p.deleted is null) and lower(p.number) = ?", parentNumber.toLowerCase());
    }

    @Override
    public Map<String, Integer> getProjectAsMapByNumber() {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.number, p.id from ").append(getCompanyId()).append(".project p where p.isDeleted is not true");
        List<Object[]> list = findNative(sql.toString());

        Map<String, Integer> map = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (Object[] objects : list) {
                if (objects[0] != null && objects[1] != null) {
                    map.put((String) objects[0], (Integer) objects[1]);
                }
            }

            return map;
        }
        return null;
    }

    @Override
    public Map<String, Integer> getProjectAsMap() {
        StringBuilder sql = new StringBuilder();
        sql.append("select trim(pp.name)||'_'||trim(p.name), p.id from ").append(getCompanyId()).append(".project p  inner join ").append(getCompanyId()).append(".project pp on pp.id = p.parnetid where p.isDeleted is not true and p.parnetid is not null ");
        List<Object[]> dataList = findNative(sql.toString());

        Map<String, Integer> map = new HashMap<>();
        for (Object[] data : dataList) {
            if (data.length > 1 && data[0] != null && data[1] != null) {
                map.put(((String) data[0]).trim().toLowerCase(), (Integer) data[1]);
            }
        }
        return map;
    }

    @Override
    public ArrayList<Integer> getProjectClientsByID(Integer projectID) {
        boolean projectMultiClientEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT);
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id from ").append(getCompanyId()).append(".project p ");
        if (projectMultiClientEnable) {
            sql.append("left outer join ").append(getCompanyId()).append(".project_clients pc on  pc.projectid=p.id ");
            sql.append("left outer join ").append(getCompanyId()).append(".crmaccount c on c.id=pc.clientid ");
        } else {
            sql.append("left outer join ").append(getCompanyId()).append(".crmaccount c on (c.id=p.clientid) ");
        }
        sql.append("where p.id=").append(projectID);
        return (ArrayList<Integer>) findNative(sql.toString());
    }


    @Override
    public List getEmployeeSalaryReport(Integer employeeID, String period, Integer payslipItemId) { //copied from Monthly Employee rates report
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct p.number projectnumber, p.name projectname, po.name catname, COALESCE(wrate.basicSalary,0), ");
        sql.append("COALESCE(((Case when e.payment_method='Min Salary' then COALESCE(po.minSalary,0) ");
        sql.append("when e.payment_method='Mid Salary' then COALESCE(po.midSalary,0) ");
        sql.append("when e.payment_method='Max Salary' then COALESCE(po.maxSalary,0) end)-wrate.basicSalary),0) sitedif, ");
        sql.append("cast((case when con.isfood is not true then COALESCE(fa.paymentamount, 0) else 0 end) as double precision) foodmount, ");
        sql.append("COALESCE(mt.total_days_worked,0) wdays, mt.month_year monthyear, COALESCE(mt.overtime,0) overtimehours, ");
        sql.append("COALESCE(mt.weekend_overtime,0) weekendovertimehours, COALESCE(mt.holiday_overtime, 0) holidayovertimehours, ");
        sql.append("COALESCE(wrate.regularovertimerate,0) regularovertimerate, COALESCE(wrate.weekendovertimerate, 0) weekendovertimerate, COALESCE(wrate.holidayovertimerate, 0) holidayovertimerate, con.number ");
        sql.append("from ").append(getCompanyId()).append(".myuser mu ");
        sql.append("inner join ").append(getCompanyId()).append(".employee e on mu.id=e.id ");
        sql.append("left join (select pd.employeeID,pd.paymentamount from ").append(getCompanyId()).append(".PaymentDeduction pd ");
        sql.append("left join ").append(getCompanyId()).append(".category c on pd.categoryID = c.id ");
        sql.append("left join ").append(getCompanyId()).append(".payslip_payments pp on pp.payment_deduction_id = pd.id ");
        sql.append("where c.code='FOOD_ALLOWANCE' and pd.deleted is not true and pp.payslip_item_id = ").append(payslipItemId).append(" ) fa on fa.employeeID=e.id ");
        sql.append("inner join ").append(getCompanyId()).append(".employeeprofile ep on ep.id=e.profileid ");
        sql.append("inner join ").append(getCompanyId()).append(".teamEmployee te on e.id=te.employeeId ");
        sql.append("inner join ").append(getCompanyId()).append(".projectemployee pe on te.id=pe.employeeDepartmentId ");
        sql.append("left join ").append(getCompanyId()).append(".position po on po.id=pe.positionid ");
        sql.append("inner join ").append(getCompanyId()).append(".project p on p.id=pe.projectid ");
        sql.append("inner join ").append(getCompanyId()).append(".contract con on con.projectid=p.id ");
        sql.append("inner join ").append(getCompanyId()).append(".monthly_timesheet mt on pe.id=mt.project_employee_id and mt.month_year='").append(period).append("' ");
        sql.append("left join (select distinct employeeid,cast((Case when array_to_string(array_agg(case when key='" + Constants.REGULAR_OVERTIME_RATE + "'  then coalesce(value,'0') else null end ),'&lt;br&gt;')='' then '0' ");
        sql.append("else array_to_string(array_agg(case when key='" + Constants.REGULAR_OVERTIME_RATE + "'  then coalesce(value,'0') else null end ),'&lt;br&gt;') end) as double precision)/100 regularovertimerate, ");
        sql.append("cast((Case when array_to_string(array_agg(case when key='" + Constants.WEEKEND_OVERTIME_RATE + "'  then coalesce(value,'0') else null end ),'&lt;br&gt;')='' then '0' ");
        sql.append("else array_to_string(array_agg(case when key='" + Constants.WEEKEND_OVERTIME_RATE + "'  then coalesce(value,'0') else null end ),'&lt;br&gt;') end) as double precision)/100 weekendovertimerate, ");
        sql.append("cast((Case when array_to_string(array_agg(case when key='" + Constants.HOLIDAY_OVERTIME_RATE + "'  then coalesce(value,'0') else null end ),'&lt;br&gt;')='' then '0' ");
        sql.append("else array_to_string(array_agg(case when key='" + Constants.HOLIDAY_OVERTIME_RATE + "'  then coalesce(value,'0') else null end ),'&lt;br&gt;') end) as double precision)/100 holidayovertimerate, ");
        sql.append("cast((Case when array_to_string(array_agg(case when key='" + Constants.SALARY + "' then coalesce(value,'0') else null end ),'&lt;br&gt;')='' then '0' ");
        sql.append("else array_to_string(array_agg(case when key='" + Constants.SALARY + "'  then coalesce(value,'0') else null end),'&lt;br&gt;') end) as double precision) basicSalary ");
        sql.append("from  ").append(getCompanyId()).append(".employeepayrollsettings group by employeeid) wrate on wrate.employeeid=e.id ");
        sql.append("where mu.deleted is not true and pe.isDeleted is not true and e.id=").append(employeeID).append(" order by p.number ");
        return findNative(sql.toString());
    }

    @Override
    public void deleteProjectBillOfItems(Integer projectID) {
        updateByNamedParams("DELETE FROM EdsBillOfMaterial bm WHERE bm.project.objectID = :projetID", preparing(new Entry("projetID", projectID)));
    }

    @Override
    public List<Object[]> getCountByStatus() {
        return find("select count(p.id), p.status.name from EdsProject p where (p.deleted<>true or p.deleted is null) group by p.status.name");
    }

    @Override
    public Long getTotalTimeSpent() {
        return (Long) findSingle("select sum(t.timeSpent)/60 from EdsTimeSheet t");
    }

    @Override
    public Long getTotalEstimated() {
        return (Long) findSingle("select sum(t.estimatedTime)/60 from EdsTask t where t.deleted is false");
    }

    @Override
    public Long getTotalCount() {
        return (Long) findSingle("select count(p.id) from EdsProject p where (p.deleted<>true or p.deleted is null)");
    }

    @Override
    public List<EdsProject> getProjectsByDate(ListingFilterParameter fp) {
        String sql = "select distinct p from EdsProjectEmployee pe left join pe.project p where p.endDate between :startDate and :endDate and (p.deleted<>true or p.deleted is null) " +
                "and p.status.code not in ('PS_COMPLETED', 'PS_CLOSED') ";
        if (fp.getEmployeeId() != null) {
            sql += " and pe.employeeDepartment.employee.objectID=:employeID and pe.deleted is false ";
        }
        sql += "order by p.endDate desc";
        Query query = slaveEntityManager.createQuery(sql, EdsProject.class);
        query.setParameter("startDate", fp.getStartDate());
        query.setParameter("endDate", fp.getEndDate());
        if (fp.getEmployeeId() != null) {
            query.setParameter("employeID", fp.getEmployeeId());
        }
        query.setFirstResult(fp.getStart());
        query.setMaxResults(fp.getLimit());
        return query.getResultList();
    }

    @Override
    public Map<Integer, EdsBillOfMaterial> getBomAsMap(Integer projectID) {
        List<EdsBillOfMaterial> objects = find("SELECT bm FROM EdsBillOfMaterial bm WHERE bm.project.objectID = ?", projectID);

        return objects.stream()
                .collect(Collectors
                        .toMap(EdsBillOfMaterial::getObjectID, obj -> obj));
    }

    @Override
    public void deleteBillOfItems(List<Integer> ids) {
        updateByNamedParams("DELETE FROM EdsBillOfMaterial bm WHERE bm.objectID in (:ids)", preparing(new Entry("ids", ids)));
    }

    @Override
    public EdsCrmContact getRelationContact(String projectNumber) {
        if (StringUtil.isEmpty(projectNumber)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select con.* from ").append(getCompanyId()).append(".crmcontact con ");
        sql.append("left join ").append(getCompanyId()).append(".project pr on pr.isdeleted is not true and ");
        sql.append("pr.number = '").append(projectNumber).append("' ");
        sql.append("left join ").append(getCompanyId()).append(".relation re on ");
        sql.append("(pr.id = re.fromid and re.fromtype='PROJECT' and re.totype='contact') ");
        sql.append("where re.toid =con.id");
        return (EdsCrmContact) findNativeSingle(sql.toString(), EdsCrmContact.class);
    }

    @Override
    public List<NearbyProjectDto> getNearbyProjects(Double latitude, Double longitude, Integer radius, List<Integer> assigneeIds, List<String> statusCodes, List<String> excludeStatusCodes) {
        StringBuilder query = new StringBuilder("""
                select p.id,
                       p.name,
                       cil.latitude,
                       cil.longitude,
                       (6371 * 2 * asin(sqrt(
                               power(sin(radians(cil.latitude - :latitude) / 2), 2) +
                               cos(radians(:latitude)) * cos(radians(cil.latitude)) *
                               power(sin(radians(cil.longitude - :longitude) / 2), 2)
                                        ))) AS distance_km,
                       cil.radius
                from EdsCheckInLocation cil
                         left join cil.project p
                where p.deleted is not true
                  and cil.latitude between (:latitude - (:radius / 111.0)) AND (:latitude + (:radius / 111.0))
                  and cil.longitude between (:longitude - ((:radius / 111.0) / cos(radians(:latitude)))) AND (:longitude + ((:radius / 111.0) / cos(radians(:latitude))))
                  and (6371 * 2 * asin(sqrt(
                        power(sin(radians(cil.latitude - :latitude) / 2), 2) +
                        cos(radians(:latitude)) * cos(radians(cil.latitude)) *
                        power(sin(radians(cil.longitude - :longitude) / 2), 2)
                                       ))) <= :radius""");
        HashMap<String, Object> params = new HashMap<>();
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("radius", radius / 1000d);
        if (statusCodes != null && !statusCodes.isEmpty()) {
            query.append("\n  and p.status.code in :statusCodes");
            params.put("statusCodes", statusCodes);
        } else if (excludeStatusCodes != null && !excludeStatusCodes.isEmpty()) {
            query.append("\n  and p.status.code not in :excludeStatusCodes");
            params.put("excludeStatusCodes", excludeStatusCodes);
        }
        if (assigneeIds != null && !assigneeIds.isEmpty()) {
            query.append("\n  and p.objectID in (select pe.project.objectID from EdsProjectEmployee pe join pe.employeeDepartment ed join ed.employee e where pe.deleted <> true and pe.historical <> true and e.objectID in :assigneeIds)");
            params.put("assigneeIds", assigneeIds);
        }
        query.append("\norder by distance_km");
        List<Object[]> byNamedParams = findByNamedParams(query.toString(), params);
        List<NearbyProjectDto> response = new ArrayList<>();
        for (Object[] row : byNamedParams) {
            NearbyProjectDto dto = new NearbyProjectDto();
            dto.setId(row[0] != null ? (Integer) row[0] : null);
            dto.setName(row[1] != null ? (String) row[1] : null);
            dto.setLatitude(row[2] != null ? (Double) row[2] : null);
            dto.setLongitude(row[3] != null ? (Double) row[3] : null);
            dto.setDistance(row[4] != null ? (Double) row[4] * 1000d : null);
            dto.setRadius(row[5] != null ? (Integer) row[5] : null);
            response.add(dto);
        }
        return response;
    }
}
