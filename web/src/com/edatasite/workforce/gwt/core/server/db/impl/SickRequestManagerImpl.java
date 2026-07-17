package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BrigadaEmployeesManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.rpc.AttendanceItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS_RESIGNED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SHIFT_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.BRIGADA_ID;
import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.EMPLOYEE_ID;

@Repository("sickRequestManager")
public class SickRequestManagerImpl extends BaseManager<EdsSickRequest> implements SickRequestManager {

    @Autowired
    private RoleManager roleManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private BrigadaEmployeesManager brigadaEmployeesManager;
    @Autowired
    private HrmsService hrmsService;


    public SickRequestManagerImpl() {
        super(EdsSickRequest.class);
    }

    @Override
    public List<EdsSickRequest> getList(ListingFilterParameter fp) {
        EdsLeaveReason reason = fp.getReasonID() != null ? leaveReasonManager.get(fp.getReasonID()) : null;
        StringBuilder sql = new StringBuilder();
        sql.append("select lr from EdsSickRequest lr ");
        sql.append("left join lr.employee e ");
        sql.append("left join lr.employee.accountStatus status ");
        sql.append(" where lr.parent is null and (e.deleted<>true or (e.deleted = true and status.code = '").append(EMPLOYEE_STATUS_RESIGNED).append("'))");
        if (reason != null) {
            sql.append(" and lr.leaveReason='").append(reason.getCode()).append("'");
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and ( ");
            sql.append(" lower(e.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(e.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") ");
        }
        String sortOrder = fp.isAscending() ? "" : " Desc";
        if (fp.getSortField() != null) {
            sql.append("ORDER BY ");
            if ("employeeName".equals(fp.getSortField())) {
                sql.append(" e.firstname ");
            }
            sql.append(sortOrder);
        } else {
            sql.append(" order by lr.createdDate desc");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public List<EdsSickRequest> findApprovedLeaveRequestsByUserId(Integer userId, Date startDate, Date endDate) {
        String sql = "select sr from EdsSickRequest sr where ((sr.startDate>=:startDate and coalesce(sr.recallDate, sr.endDate)<=:endDate) " +
                "or (sr.startDate>=:startDate and sr.startDate<=:endDate) " +
                "or (coalesce(sr.recallDate, sr.endDate)>=:startDate and coalesce(sr.recallDate, sr.endDate)<=:endDate) " +
                "or (sr.startDate<=:startDate and coalesce(sr.recallDate, sr.endDate)>=:endDate)) " +
                "and sr.overallStatus.code != '" + EdsSickRequest.DENIED + "' " +
                "and sr.overallStatus.code != '" + EdsSickRequest.DRAFT + "' and sr.employee.objectID = " + userId + " ";

        return findByNamedParams(sql, preparing(new Entry("startDate", startDate), new Entry("endDate", endDate)));
    }

    @Override
    public List<EdsSickRequest> findSameLeaveRequests(Integer leaveId, Integer userId, Date startDate, Date endDate, Integer reasonId) {
        String sql = "select sr from EdsSickRequest sr where sr.objectID != :leaveId and sr.startDate = :startDate and coalesce(sr.recallDate, sr.endDate) = :endDate and sr.leaveReason.objectID = :reasonId and sr.employee.objectID = " + userId;

        return findByNamedParams(sql, preparing(new Entry("leaveId", leaveId), new Entry("startDate", startDate), new Entry("endDate", endDate), new Entry("reasonId", reasonId)));
    }


    @Override
    public List<Integer> getLeaveRequestListForSolr(SolrReindexRpc solrReindex) {
        String sql = "select s.objectID from EdsSickRequest s where s.createdDate>= :createdDate"
                + (solrReindex.getLastUpdateEndTime() != null ? " and s.createdDate<='" + solrReindex.getLastUpdateEndTime() + "'" : "");
        Query query = slaveEntityManager.createQuery(sql, Integer.class)
                .setParameter("createdDate", solrReindex.getLastUpdateTime());
        return query.getResultList();
    }

    @Override
    public Integer getLeaveRequestLastIntNumber() {
        return (Integer) findSingle("select s.intNumber from EdsSickRequest s where s.intNumber is not null order by s.intNumber desc");
    }

    @Override
    public Boolean getLeaveRequestByCode(String code, Integer objectID) {
        return findSingle("select s from EdsSickRequest s where s.numberData='" + code + "'") != null;
    }

    @Override
    public List<Integer> getRequestIdsByIds(String ids) {
        return find("select s.objectID from EdsSickRequest s where s.objectID in (" + ids + ")");
    }

    @Override
    public List<Integer> getIdsWithLimit(int startat, int limit) {
        return findInterval("select s.objectID from EdsSickRequest s", startat, limit);
    }

    public List<EdsSickRequest> getRequestListByStartDate(EdsCrmAccount clientFilter, EdsProject projectFilter,
                                                          EdsDepartment departmentFilter, EdsEmployee employeeFilter, Integer viewAsFilter,
                                                          String groupByName, Date from, Date to) {
        EdsUser user = getUser();
        Map<String, Object> paramMap = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct sr from EdsSickRequest sr, EdsProjectEmployee pe left join pe.project p");
        sql.append(" where sr.startDate >= :startDate ");          // first isolate by the users company
        paramMap.put("startDate", from);
//        paramMap.put("endDate", to);
        if (viewAsFilter == null && !roleManager.hasRoles(user, EdsRole.DR, EdsRole.ADMIN, EdsRole.CLIENT)) {
            sql.append(" and (");
            sql.append("  sr.employee.employeeDepartment.department.leader.objectID=:pEmployeeID or ");  // if he is department leader for the project
            sql.append("  pe.project.manager.objectID=:pEmployeeID or ");                       // if he is project manager or backup manager
            sql.append("  p.backupManager.objectID=:pEmployeeID or ");
            sql.append("  p.backupManager2.objectID=:pEmployeeID or ");
            sql.append("  p.backupManager3.objectID=:pEmployeeID or ");
            sql.append("  p.backupManager4.objectID=:pEmployeeID or ");
            sql.append("  p.backupManager5.objectID=:pEmployeeID or ");
            sql.append("  p.backupManager6.objectID=:pEmployeeID or ");
            sql.append("  p.backupManager7.objectID=:pEmployeeID or ");
            sql.append("  p.backupManager8.objectID=:pEmployeeID or ");
            sql.append("  p.backupManager9.objectID=:pEmployeeID or ");
            sql.append("  p.backupManager10.objectID=:pEmployeeID or ");
            sql.append("  sr.employee.objectID=:pEmployeeID ");   // if he is member of the project
            sql.append(") ");
            paramMap.put("pEmployeeID", user.getObjectID());
        } else if (EdsRole.DR.equals(viewAsFilter) || EdsRole.ADMIN.equals(viewAsFilter)) {
            // if he is director or admin should see
            // all the projects of the company
        } else if (EdsRole.TL.equals(viewAsFilter)) {
            sql.append(" and sr.employee.employeeDepartment.department.leader.objectID=:pEmployeeID ");   // if he is viewing only as a department leader
            paramMap.put("pEmployeeID", user.getObjectID());
        } else if (EdsRole.PM.equals(viewAsFilter)) {
            sql.append(" and (pe.project.manager.objectID=:pEmployeeID and pe.employeeDepartment.employee=sr.employee or ");                        // if he is viewing only as a project manager
            sql.append(" p.backupManager.objectID=:pEmployeeID or ");
            sql.append(" p.backupManager2.objectID=:pEmployeeID or ");
            sql.append(" p.backupManager3.objectID=:pEmployeeID or ");
            sql.append(" p.backupManager4.objectID=:pEmployeeID or ");
            sql.append(" p.backupManager5.objectID=:pEmployeeID or ");
            sql.append(" p.backupManager6.objectID=:pEmployeeID or ");
            sql.append(" p.backupManager7.objectID=:pEmployeeID or ");
            sql.append(" p.backupManager8.objectID=:pEmployeeID or ");
            sql.append(" p.backupManager9.objectID=:pEmployeeID or ");
            sql.append(" p.backupManager10.objectID=:pEmployeeID) ");
            paramMap.put("pEmployeeID", user.getObjectID());
        } else if (EdsRole.MEM.equals(viewAsFilter)) {
            sql.append(" and sr.employee.employeeDepartment.employee.objectID=:pEmployeeID ");    // if he is viewing only as a member
            paramMap.put("pEmployeeID", user.getObjectID());
        } else if (EdsRole.CLIENT.equals(viewAsFilter) || user.isClientContact()) {
            sql.append(" and pe.project.client.objectID=:pClientID ");                             // if he is viewing only as a client
            paramMap.put("pClientID", user.getClientContact().getClientID());
        }
        // Filter by client, department, employee
        if (clientFilter != null) {
            sql.append(" and pe.project.client.objectID=:pClientFilterID ");
            paramMap.put("pClientFilterID", clientFilter.getObjectID());
        }
        if (projectFilter != null) {
            sql.append(" and pe.project.objectID=:pProjectFilterID ");
            paramMap.put("pProjectFilterID", projectFilter.getObjectID());
        }
        if (departmentFilter != null) {
            sql.append(" and sr.employee.employeeDepartment.department.objectID =:pDepartmentFilterID ");
            paramMap.put("pDepartmentFilterID", departmentFilter.getObjectID());
        }
        if (employeeFilter != null) {
            sql.append(" and sr.employee.objectID=:pEmployeeFilterID ");
            paramMap.put("pEmployeeFilterID", employeeFilter.getObjectID());
        }
        return findByNamedParams(sql.toString(), paramMap);
    }

    public Date getStartDate() {
        return getStartDate(getUser().getCompany().getCompanyDate());
    }

    public Date getStartDate(Date date) {
        Calendar calendar = new GregorianCalendar(getUser().getCompany().getTimeZone());
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date start = calendar.getTime();
        Date regDate = getUser().getCompany().getRegistrationDate();
        if (regDate != null && regDate.after(start)) {
            // This company have recently signed up, and start date should be its reg date
            start = regDate;
        }
        return start;
    }

    public List<EdsSickRequest> getCalendarSickRequests(List<Integer> employeeIDs, Date start, Date end) {
        Map params = new HashMap();
        params.put("employee", employeeIDs);
        params.put("start", start);
        params.put("end", end);
        return findByNamedParams("select sr from EdsSickRequest sr where sr.employee.objectID in (:employee)" +
                " and (sr.startDate <= :end and sr.endDate >= :start) order by sr.startDate desc", params);
    }

    public ListResult<Object> getEmployeeAttendanceReport(ListingFilterParameter fp) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EdsRole essUser = null;
        if (fp.isEssUser()) {
            essUser = roleManager.getByCode(Constants.ESS_USER_CODE);
        }

        EdsUser edsUser = employeeManager.getUser();

        StringBuilder sqlEmployeeData = getEmployeeData(fp, dateFormat, essUser, edsUser);

        if (!fp.isFromShift() && fp.getStart() != -1) {
            sqlEmployeeData.append(" offset ").append(fp.getStart());
        }

        if (fp.isFromShift()) { // temporary solution
            sqlEmployeeData.append(" limit 100 ");
        } else if (!fp.isVisableAll()) {
            sqlEmployeeData.append(" limit 30 ");
        }

        StringBuilder baseSql = getAttendanceBaseSql(fp, dateFormat, sqlEmployeeData);
        baseSql.append(" order by emp.id,s.overallstatus desc \n");
        /*if (fp.getStart() != -1) {
            baseSql.append(" offset ").append(fp.getStart());
        }
        if (!fp.isVisableAll()) {
            baseSql.append(" limit 30 ");
        }*/
        ArrayList<Object> result = (ArrayList<Object>) findNative(baseSql.toString());

        String sql = "select count(id) from (" +
                getEmployeeData(fp, dateFormat, essUser, edsUser) +
                ") report ";
        Integer resultTotal = ((BigInteger) findNativeSingle(sql)).intValue();
        return new ListResult<>(result, resultTotal);
    }

    private StringBuilder getEmployeeData(ListingFilterParameter fp, DateFormat dateFormat, EdsRole essUser, EdsUser edsUser) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select e.id, te.id as teid, t.id as tid \n");
        sql.append(" from ").append(getCompanyId()).append(".employee e \n");
        sql.append(" inner join ").append(getCompanyId()).append(".myuser m on m.id=e.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".userEmailSettings ues on ues.userid = e.id \n");
        sql.append(" join ").append(getCompanyId()).append(".employeeProfile ep on e.profileId = ep.id \n");
        sql.append(" left outer join ").append(getCompanyId()).append(".teamEmployee te on te.employeeid = e.id \n");
        sql.append(" left outer join ").append(getCompanyId()).append(".projectemployee pe on (te.id=pe.employeedepartmentid and pe.isdeleted=false) \n");
        sql.append(" left outer join ").append(getCompanyId()).append(".project p on (p.id=pe.projectid) \n");
        sql.append(" left outer join ").append(getCompanyId()).append(".team t on t.id = te.teamid \n");
        sql.append(" left outer join ").append(getCompanyId()).append(".reference re on re.id = m.accountstatusid \n");
        sql.append(" left outer join ").append(getCompanyId()).append(".myuser_role roles on (roles.users_id = m.id) \n");
        sql.append(" left outer join ").append(getCompanyId()).append(".position pos on (pos.id=e.positionid) \n");
        sql.append(" left outer join ").append(getCompanyId()).append(".reference ptype on pos.type = ptype.id \n");
        sql.append(" where ((m.deleted<>true and (e.startDate is null or e.startDate <= '").append(dateFormat.format(fp.getEndDate()));
        sql.append(" ') and re.code != '" + EMPLOYEE_STATUS_RESIGNED + "') or (re.code = '" + EMPLOYEE_STATUS_RESIGNED + "' and \n");
        sql.append(" (e.endDate >= '").append(dateFormat.format(fp.getStartDate())).append("' or e.endDate<>null))) \n");
        sql.append(" and (ptype.code != 'TYPE_EXTERNAL' or ptype.code is null) \n");
        sql.append(" and timesheetrequired is true \n");

        if (!edsUser.hasEitherRoles(EdsRole.ADMIN_CODE)) {
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

                List<Integer> departmentIDs = null;
                if (edsUser.hasRole(EdsRole.TL_CODE) && ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST)) {
                    List<EdsDepartment> edsDepartments = departmentManager.getTeamsByEmployeeId(edsUser.getObjectID());
                    departmentIDs = edsDepartments.stream().map(EdsDepartment::getObjectID).collect(Collectors.toList());
                }

                Integer teamID = edsUser.getEmployee().getTeam().getObjectID();
                Integer locationID = edsUser.getLocation() != null ? edsUser.getLocation().getObjectID() : null;
                Integer employeeID = edsUser.getObjectID();

                boolean hasOneOfPermission = showTeamEmployees || showLocationEmployees || showSupervisedEmployees || showProjectEmployees;

                if (hasOneOfPermission) {
                    sql.append("AND (e.id=").append(employeeID).append("\n");
                    sql.append(" OR ");
                    boolean or = false;
                    if (showTeamEmployees) {
                        if (departmentIDs != null && departmentIDs.size() > 0) {
                            sql.append("t.id in (").append(ServerUtils.getAsCommoDelimited(departmentIDs, "0", ",")).append(") \n");
                        } else {
                            sql.append("t.id = ").append(teamID);
                        }
                        or = true;
                    }
                    if (showLocationEmployees) {
                        if (or) {
                            sql.append(" OR ");
                        }
                        sql.append("m.locationId = ").append(locationID);
                        or = true;
                    }
                    if (showSupervisedEmployees) {
                        if (or) {
                            sql.append(" OR ");
                        }
                        sql.append("ep.reportsTo=").append(employeeID);
                        or = true;
                    }
                    if (showProjectEmployees && employeeIDs != null) {
                        if (or) {
                            sql.append(" OR ");
                        }
                        sql.append("e.id in (").append(ServerUtils.getAsCommoDelimited(employeeIDs, "0", ",")).append(") \n");
                    }
                    sql.append(" ) ");
                } else {
                    sql.append("and e.id=").append(employeeID).append(" \n");
                }
            }
        }

        if (fp.getBrigadaIDs() != null && (fp.getBrigadaIDs().contains("B") || fp.getBrigadaIDs().contains("E"))) {
            int type = fp.getBrigadaIDs().contains("E") ? EMPLOYEE_ID : BRIGADA_ID;
            String teamsIdsForAttendanceLink = hrmsService.getTeamsIdsForAttendanceLink(Integer.valueOf(fp.getBrigadaIDs().substring(0, fp.getBrigadaIDs().length() - 1)), type);
            sql.append("and e.id in (").append(teamsIdsForAttendanceLink).append(")   \n");
        } else if (fp.getBrigadaIDs() != null) {
            String empIds = ServerUtils.getAsCommoDelimited(brigadaEmployeesManager.getBrigadaEmployees(fp.getBrigadaIDs()), "0", ",");
            sql.append("and e.id in (").append(empIds).append(")   \n");
        } else if (fp.getBrigadaID() != null) {
            String empIds = ServerUtils.getAsCommoDelimited(brigadaEmployeesManager.getBrigadaEmployees(fp.getBrigadaID()), "0", ",");
            sql.append("and e.id in (").append(empIds).append(")   \n");
        }

        if (fp.getEmployeeId() != null) {
            sql.append("and e.id=").append(fp.getEmployeeId()).append(" \n");
        }
        if (StringUtils.isNotBlank(fp.getName())) {
            String name = fp.getName();
            if (fp.getName().contains("'")) {
                name = name.replace("'", "''");
            }
            sql.append("and (m.firstname ||")
                    .append("' '").append("|| m.lastname || ' ' || m.middlename ilike '%")
                    .append(name.trim())
                    .append("%' or m.lastname ||").append("' '").append("|| m.firstname || ' ' || m.middlename ilike '%")
                    .append(name.trim()).append("%' or ep.employeeCode ilike '%").append(name.trim()).append("%') \n");
        }
        if (fp.getLocationId() != null) {
            sql.append("and m.locationId=").append(fp.getLocationId()).append(" \n");
        }
        if (fp.getProjectId() != null) {
            sql.append("and p.id=").append(fp.getProjectId()).append(" \n");
        }
        if (fp.isEssUser() && essUser != null) {
            sql.append("and roles.roles_id=").append(essUser.getObjectID()).append(" \n");
        }
        if (StringUtils.isNotBlank(fp.getStatusValues())) {
            sql.append("and re.code in (").append(fp.getStatusValues()).append(") \n");
        }
        if (fp.getDepartmentIds() != null) {
            sql.append("and t.id in (").append(fp.getDepartmentIds()).append(") and te.startdate <= '").append(dateFormat.format(fp.getEndDate())).append("'\n");
            sql.append("and ((te.enddate is null and te.isdeleted is false) or te.enddate >= '").append(dateFormat.format(fp.getStartDate())).append("') ");
        } else {
            sql.append(" and ").append(ServerUtils.checkForDeleted("te.isdeleted"));
        }
        if (fp.getTimeSlotID() != null) {
            sql.append(" and e.timeslotid=").append(fp.getTimeSlotID()).append(" \n");
        }
        if (fp.getPositionID() != null) {
            sql.append(" and pos.id=").append(fp.getPositionID()).append(" \n");
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" and e.id=").append(fp.getEmployeeId()).append(" \n");
        }
        if (fp.isFromShift()) {
            sql.append(" order by pos.name, m.firstName, m.lastName, m.middleName \n");
            return sql;
        }
        sql.append(" group by e.id, te.id, t.id \n");
        return sql;
    }

    private StringBuilder getAttendanceBaseSql(ListingFilterParameter fp, DateFormat dateFormat, StringBuilder employeeData) {
        String from = dateFormat.format(fp.getStartDate());
        String to = dateFormat.format(fp.getEndDate());
        StringBuilder sql = new StringBuilder();
        sql.append("with employeeData as ( ").append(employeeData).append(" ), \n");
//        sql.append(" shiftData as (select shit.groupid as id from ").append(getCompanyId()).append(".shift sh \n");
//        sql.append(" left join ").append(getCompanyId()).append(".reference r on sh.overallstatus = r.id \n");
//        sql.append(" left join ").append(getCompanyId()).append(".shift_items shit on shit.shift_id = sh.id \n");
//        sql.append(" where sh.lookuptype = ").append(EMPLOYEE_ID).append(" and to_char(period, 'yyyy-MM-dd') = '").append(new SimpleDateFormat("yyyy-MM-dd").format(fp.getEndDate())).append("' \n");
//        sql.append(" and ").append(ServerUtils.checkForDeleted("sh.deleted")).append(" and r.code = '").append(SHIFT_APPROVED).append("' group by shit.groupid \n");
//        sql.append(" UNION ALL ");
        sql.append(" shiftData as (select be.empid as id from ").append(getCompanyId()).append(".shift sh \n");
        sql.append(" left join ").append(getCompanyId()).append(".reference r on sh.overallstatus = r.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".shift_teams_data be on be.shift_id = sh.id ");
        sql.append(" where sh.lookuptype = ").append(BRIGADA_ID).append(" and to_char(period, 'yyyy-MM') = '").append(new SimpleDateFormat("yyyy-MM").format(fp.getEndDate())).append("' \n");
        sql.append(" and ").append(ServerUtils.checkForDeleted("sh.deleted")).append(" and r.code = '").append(SHIFT_APPROVED).append("' group by be.empid) \n");
        sql.append("select emp.id,");
        if (StringUtils.isNotBlank(fp.getDepartmentIds())) {
            sql.append(" case when te.startdate between '").append(from).append("' and '").append(to).append("' \n ");
            sql.append(" and te.startdate > s.startdate then te.startdate else s.startdate end, \n");
            sql.append(" case when te.enddate between '").append(from).append("' and '").append(to).append("' \n ");
            sql.append(" and te.enddate < s.enddate then te.enddate else COALESCE (s.recall_date, s.enddate) end  as enddate\n");
        } else {
            sql.append("s.startdate, COALESCE (s.recall_date, s.enddate) as enddate");
        }
        sql.append(",s.overallstatus, \n");
        sql.append("CASE WHEN ((s.startdate between '").append(from).append("' and '").append(to)
                .append("') or (s.startdate < '").append(from).append("' and COALESCE (s.recall_date, s.enddate) > '").append(to).append("')) THEN 1 ELSE \n");
        sql.append("CASE WHEN ((COALESCE (s.recall_date, s.enddate) between '").append(from).append("' and '").append(to)
                .append("') or (s.startdate < '").append(from).append("' and COALESCE (s.recall_date, s.enddate) > '").append(to)
                .append("')) THEN -1 END END as position, s.reason_code, s.includedayoff, false, s.id sid, r.mark_as_draft, r.unitType, shd.id is not null as hasShift, emp.tid  \n");
        sql.append("from employeeData emp \n");
        if (StringUtils.isNotBlank(fp.getDepartmentIds())) {
            sql.append("left join ").append(getCompanyId()).append(".teamemployee te on emp.teid = te.id \n");
            sql.append("left join ").append(getCompanyId()).append(".team t on te.teamid = t.id and t.isdeleted is not true \n");
        }
        sql.append("left join ").append(getCompanyId()).append(".sickrequest s on s.employeeid=emp.id \n");
        sql.append("and s.overallstatus = (select id from ").append(getCompanyId()).append(".reference where code = '").append(EdsSickRequest.APPROVED).append("') \n");

        StringBuilder dateValidation = new StringBuilder().append(" ((s.startdate between '").append(from).append("' and '").append(to).append("') or \n")
                .append("(COALESCE (s.recall_date, s.enddate) between '").append(from).append("' and '").append(to).append("') or ( s.startdate < '")
                .append(from).append("' and COALESCE (s.recall_date, s.enddate) > '").append(to).append("')) \n");
        if (StringUtils.isNotBlank(fp.getDepartmentIds())) {
            sql.append(" and case when te.startdate between '").append(from).append("' and '").append(to).append("' then \n");
            sql.append(" ((s.startdate between te.startdate and '").append(to).append("' or (COALESCE(s.recall_date,s.enddate) between te.startdate and '").append(to).append("'))) \n");
            sql.append(" else case when te.enddate between '").append(from).append("' and '").append(to).append("' then \n");
            sql.append(" ((s.startdate between '").append(from).append("' and te.enddate) or (COALESCE(s.recall_date, s.enddate) between '").append(from).append("' and te.enddate)) \n");
            sql.append(" else ").append(dateValidation).append(" end end \n");
        } else {
            sql.append("and ").append(dateValidation);
        }
        sql.append("left outer join ").append(getCompanyId()).append(".leave_reason r on r.code = s.reason_code \n");
        sql.append("left outer join ").append("shiftdata shd on shd.id = emp.id \n");

        if (fp.getReasonIds() != null) {
            sql.append(" and r.id in (").append(fp.getReasonIds()).append(") \n");
        }

      /*  if (!edsUser.hasEitherRoles(EdsRole.ADMIN_CODE)) {
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

                List<Integer> departmentIDs = null;
                if (edsUser.hasRole(EdsRole.TL_CODE) && ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST)) {
                    List<EdsDepartment> edsDepartments = departmentManager.getTeamsByEmployeeId(edsUser.getObjectID());
                    departmentIDs = edsDepartments.stream().map(EdsDepartment::getObjectID).collect(Collectors.toList());
                }

                Integer teamID = edsUser.getEmployee().getTeam().getObjectID();
                Integer locationID = edsUser.getLocation() != null ? edsUser.getLocation().getObjectID() : null;
                Integer employeeID = edsUser.getObjectID();

                boolean hasOneOfPermission = showTeamEmployees || showLocationEmployees || showSupervisedEmployees || showProjectEmployees;

                if (hasOneOfPermission) {
                    sql.append("AND (e.id=").append(employeeID).append("\n");
                    sql.append(" OR ");
                    boolean or = false;
                    if (showTeamEmployees) {
                        if (departmentIDs != null && departmentIDs.size() > 0) {
                            sql.append("t.id in (").append(ServerUtils.getAsCommoDelimited(departmentIDs, "0", ",")).append(") \n");
                        } else {
                            sql.append("t.id = ").append(teamID);
                        }
                        or = true;
                    }
                    if (showLocationEmployees) {
                        if (or) {
                            sql.append(" OR ");
                        }
                        sql.append("m.locationId = ").append(locationID);
                        or = true;
                    }
                    if (showSupervisedEmployees) {
                        if (or) {
                            sql.append(" OR ");
                        }
                        sql.append("ep.reportsTo=").append(employeeID);
                        or = true;
                    }
                    if (showProjectEmployees && employeeIDs != null) {
                        if (or) {
                            sql.append(" OR ");
                        }
                        sql.append("e.id in (").append(ServerUtils.getAsCommoDelimited(employeeIDs, "0", ",")).append(") \n");
                    }
                    sql.append(" ) ");
                } else {
                    sql.append("and e.id=").append(employeeID).append(" \n");
                }
            }
        }

        if (fp.getEmployeeId() != null) {
            sql.append("and e.id=").append(fp.getEmployeeId()).append(" \n");
        }
        if (StringUtils.isNotBlank(fp.getName())) {
            String name = fp.getName();
            if (fp.getName().contains("'")) {
                name = name.replaceAll("'", "''");
            }
            sql.append("and (m.firstname ||")
                    .append("' '").append("|| m.lastname || ' ' || m.middlename ilike '%")
                    .append(name.trim())
                    .append("%' or m.lastname ||").append("' '").append("|| m.firstname || ' ' || m.middlename ilike '%")
                    .append(name.trim()).append("%' or ep.employeeCode ilike '%").append(name.trim()).append("%') \n");
        }
        if (fp.getLocationId() != null) {
            sql.append("and m.locationId=").append(fp.getLocationId()).append(" \n");
        }
        if (fp.getProjectId() != null) {
            sql.append("and p.id=").append(fp.getProjectId()).append(" \n");
        }
        if (fp.isEssUser() && essUser != null) {
            sql.append("and roles.roles_id=").append(essUser.getObjectID()).append(" \n");
        }
        if (StringUtils.isNotBlank(fp.getStatusValues())) {
            sql.append("and re.code in (").append(fp.getStatusValues()).append(") \n");
        }
        if (fp.getDepartmentIds() != null) {
            sql.append("and t.id in (").append(fp.getDepartmentIds()).append(") \n");
        }
        if (fp.getTimeSlotID() != null) {
            sql.append(" and e.timeslotid=").append(fp.getTimeSlotID()).append(" \n");
        }
        if (fp.getPositionID() != null) {
            sql.append(" and pos.id=").append(fp.getPositionID()).append(" \n");
        }*/

        sql.append(" group by emp.id,s.startDate,COALESCE (s.recall_date, s.enddate),s.overallstatus, s.reason_code, s.includedayoff, s.id, r.mark_as_draft, r.unitType, shd.id, emp.tid \n");
        if (StringUtils.isNotBlank(fp.getDepartmentIds())) {
            sql.append(",te.startdate, te.enddate ");
        }

        return sql;
    }

    /*public List getEmployeeAttendanceReport(ListingFilterParameter fp) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EdsRole ess = null;
        if (fp.isEssUser()) {
            ess = roleManager.getByCode(Constants.ESS_USER_CODE);
        }
        return findNative("select e.id,s.startDate,s.endDate,s.overallstatus, \n"
                + "CASE WHEN (s.startdate between '" + format.format(fp.getStartDate()) + "' and '" + format.format(fp.getEndDate()) + "') THEN 1 ELSE  \n"
                + "CASE WHEN (s.enddate between '" + format.format(fp.getStartDate()) + "' and '" + format.format(fp.getEndDate()) + "') THEN -1 END END as position, s.reason_code, s.includedayoff, false, s.id sid  \n"
                + "from " + getCompanyId() + ".employee e \n"
                + "inner join " + getCompanyId() + ".myuser m on m.id=e.id \n"
                + "join " + getCompanyId() + ".employeeProfile ep on e.profileId = ep.id "
                + "left outer join " + getCompanyId() + ".sickrequest s on s.employeeid=e.id \n"
                + "and ((s.startdate between '" + format.format(fp.getStartDate()) + "' and '" + format.format(fp.getEndDate()) + "') or \n"
                + "(s.enddate between '" + format.format(fp.getStartDate()) + "' and '" + format.format(fp.getEndDate()) + "')) \n"
                + "left outer join " + getCompanyId() + ".teamEmployee te on te.id = e.employeeDepartmentId\n"
                + "left outer join " + getCompanyId() + ".projectemployee pe on (te.id=pe.employeedepartmentid and pe.isdeleted=false) \n"
                + "left outer join " + getCompanyId() + ".project p on (p.id=pe.projectid) \n"
                + "left outer join " + getCompanyId() + ".team t on t.id = te.teamid \n"
                + "left outer join " + getCompanyId() + ".reference re on re.id = m.accountstatusid \n"
                + "left outer join " + getCompanyId() + ".myuser_role roles on (roles.users_id = m.id) \n" //Attention. Islom's request. Showing deleted employees those who have resigned date this month logic removed
                + " where (m.deleted<>true or (m.deleted=true and re.code = 'RESIGNED_EMPLOYEE')) \n"
                + (fp.getEmployeeId() != null ? (" and e.id=" + fp.getEmployeeId()) : "")
                + (fp.getName() != null ? (" and (m.firstname ilike '%" + fp.getName() + "%' or m.lastname ilike '%" + fp.getName() + "%' or ep.employeeCode ilike '%" + fp.getName() + "%') ") : " ")
                + (fp.getLocationId() != null ? (" and m.locationId=" + fp.getLocationId()) : " ")
                + (fp.getProjectId() != null ? (" and p.id=" + fp.getProjectId()) : " ")
                + (fp.isEssUser() && ess != null ? (" and roles.roles_id=" + ess.getObjectID()) : " ")
                + (fp.getStatusValues() != null && !"".equals(fp.getStatusValues()) ? (" and re.code in (" + fp.getStatusValues() + ") ") : " ")
                + (fp.getDepartmentId() != null ? (" and t.id=" + fp.getDepartmentId()) : " ")
                + (fp.getTimeSlotID() != null ? (" and e.timeslotid = " + fp.getTimeSlotID()) : " ")
                + "group by e.id,s.startDate,s.endDate,s.overallstatus, s.reason_code, s.includedayoff, s.id \n"
                + "order by e.id,s.overallstatus desc \n"
                + (fp.getStart() != -1 ? "offset  " + fp.getStart() + " limit 200 \n" : ""));
    }*/

    /*public Integer getEmployeeAttendanceReportCount(ListingFilterParameter fp) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EdsRole ess = null;
        if (fp.isEssUser()) {
            ess = roleManager.getByCode(Constants.ESS_USER_CODE);
        }
        return ((BigInteger) findNativeSingle("select count(*) from (select e.id,s.startDate,s.endDate,s.overallstatus, \n"
                + "CASE WHEN (s.startdate between '" + format.format(fp.getStartDate()) + "' and '" + format.format(fp.getEndDate()) + "') THEN 1 ELSE  \n"
                + "CASE WHEN (s.enddate between '" + format.format(fp.getStartDate()) + "' and '" + format.format(fp.getEndDate()) + "') THEN -1 END END as position, s.reason_code, s.includedayoff, false, s.id sid  \n"
                + "from " + getCompanyId() + ".employee e \n"
                + "inner join " + getCompanyId() + ".myuser m on m.id=e.id \n"
                + "left outer join " + getCompanyId() + ".sickrequest s on s.employeeid=e.id \n"
                + "and ((s.startdate between '" + format.format(fp.getStartDate()) + "' and '" + format.format(fp.getEndDate()) + "') or \n"
                + "(s.enddate between '" + format.format(fp.getStartDate()) + "' and '" + format.format(fp.getEndDate()) + "')) \n"
                + "left outer join " + getCompanyId() + ".teamEmployee te on te.id = e.employeeDepartmentId\n"
                + "left outer join " + getCompanyId() + ".projectemployee pe on (te.id=pe.employeedepartmentid and pe.isdeleted=false) \n"
                + "left outer join " + getCompanyId() + ".project p on (p.id=pe.projectid) \n"
                + "left outer join " + getCompanyId() + ".team t on t.id = te.teamid \n"
                + "left outer join " + getCompanyId() + ".reference re on re.id = m.accountstatusid \n"
                + "left outer join " + getCompanyId() + ".myuser_role roles on (roles.users_id = m.id) \n"
                + "where (m.deleted<>true or (m.deleted=true and re.code = 'RESIGNED_EMPLOYEE')) \n"
                + (fp.getEmployeeId() != null ? (" and e.id=" + fp.getEmployeeId()) : "")
                + (fp.getName() != null ? (" and (m.firstname ilike '%" + fp.getName() + "%' or m.lastname ilike '%" + fp.getName() + "%') ") : " ")
                + (fp.getLocationId() != null ? (" and m.locationId=" + fp.getLocationId()) : " ")
                + (fp.getProjectId() != null ? (" and p.id=" + fp.getProjectId()) : " ")
                + (fp.isEssUser() && ess != null ? (" and roles.roles_id=" + ess.getObjectID()) : " ")
                + (fp.getStatusValues() != null && !"".equals(fp.getStatusValues()) ? (" and re.code in (" + fp.getStatusValues() + ") ") : " ")
                + (fp.getDepartmentId() != null ? (" and t.id=" + fp.getDepartmentId()) : " ")
                + (fp.getTimeSlotID() != null ? (" and e.timeslotid = " + fp.getTimeSlotID()) : " ")
                + "group by e.id,s.startDate,s.endDate,s.overallstatus, s.reason_code, s.includedayoff,  s.id \n"
                + "order by e.id,s.overallstatus desc) rep \n")).intValue();
    }*/

    public List<EdsSickRequest> getSickRequestByEmployeeAndPeriod(EdsEmployee employee, Date from, Date to) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("employee", employee);
        return findByNamedParams("select sr from EdsSickRequest sr where ((sr.startDate between :from and :to) or (sr.endDate between :from and :to) or (sr.startDate <= :from and sr.endDate >= :to)) and " +
                "sr.employee = :employee", params);
    }

    @Override
    public LinkedHashMap<Integer, List<StatisticsLeaveRequest>> getSickRequestByEmployeeAndPeriod(Date from, Date to) {
        LinkedHashMap<Integer, List<StatisticsLeaveRequest>> empsLeaves = new LinkedHashMap<>();
        List<Object[]> list = findNative("select employeeid as eid,sr.id,startDate,endDate,sr.overallStatus,lr.mark_as_draft,r.code,lr.color,rl.russian from " + getCompanyId() + ".Sickrequest sr left join " + getCompanyId() + ".leave_reason lr on (sr.reason_code = lr.code) left join " + getCompanyId() + ".Reference r  on (sr.overallStatus = r.id ) left join " + getCompanyId() + ".reference_locale rl on (lr.localeId = rl.id) where ((sr.startDate between '" + from + "' and '" + to + "') or (sr.endDate between '" + from + "' and '" + to + "') or (sr.startDate <= '" + from + "' and sr.endDate >= '" + to + "')) order by eid");
        if (!CollectionUtils.isEmpty(list)) {
            List<StatisticsLeaveRequest> items = new ArrayList<>();
            Integer itemId = null;
            for (Object[] objects : list) {
                if (itemId != null && !itemId.equals(objects[0])) {
                    items = new ArrayList<>();
                }
                itemId = (Integer) objects[0];
                StatisticsLeaveRequest shift = new StatisticsLeaveRequest();
                shift.setObjectID((Integer) objects[1]);
                shift.setStartDate((Date) objects[2]);
                shift.setEndDate((Date) objects[3]);
                shift.setDescription((String) objects[7]);
                shift.setReason((String) objects[8]);
                shift.setStatus((Integer) objects[4]);
                shift.setMarkAsDraft((Boolean) objects[5]);
                shift.setLeaveRrequestCode((String) objects[6]);
                items.add(shift);
                empsLeaves.put(itemId, items);
            }
        }
        return empsLeaves;
    }

    public List<Object> getEmployeeCalendarItems(Integer employeeID, Date startDateT, Date endDateT) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return findNative("with usedanotherleavedays as (select sr.employeeid,s.id,s.date\n" +
                "from " + getCompanyId() + ".sickrequest sr  \n" +
                "JOIN " + getCompanyId() + ".sickrequestduration s on sr.id=s.sickrequestid and s.daytype ='" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "')," +
                "durations as (select id from usedanotherleavedays\n" +
                "union\n" +
                "select s.id\n" +
                "from " + getCompanyId() + ".sickrequest sr  \n" +
                "JOIN " + getCompanyId() + ".sickrequestduration s on sr.id=s.sickrequestid \n" +
                "left join usedanotherleavedays ud on sr.employeeid=ud.employeeid and s.date=ud.date\n" +
                "where ud.date is null) " +
                "SELECT dj.from_date, sd.timeslot, a.dayoff, a.holiday, sd.leaveT, " +
                "                sd.takebymoney, sd.enddate, a.holidayfromannualleave, sd.color, sd.lrname, sd.reason_code, sd.mark_as_draft " +
                "FROM " + getPublic() + ".datejoin dj \n" +
                "LEFT JOIN (select s.timeslot, s.date, \n" +
                "       (CASE WHEN s.durationtime <= s.timeslot and s.day !=0 THEN true ELSE false END) as leaveT,  \n" +
                "       sr.takebymoney, sr.enddate, s.holidayfromannualleave, " +
                "       (case when s.daytype ='" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' then '42F5E9' else lr.color end) color, " +
                "       (case when s.daytype ='" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' then 'Mixed' else lr.name end ) lrname, " +
                "       (case when s.daytype ='" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' then '_SYTEM_MIXED_' else sr.reason_code end ) reason_code, " +
                "       lr.mark_as_draft  \n" +
                "       from durations ds  \n" +
                "       join " + getCompanyId() + ".sickrequestduration s on ds.id=s.id  \n" +
                "       LEFT JOIN " + getCompanyId() + ".sickrequest sr on s.sickrequestid=sr.id \n" +
                "       LEFT JOIN " + getCompanyId() + ".reference ref ON sr.overallstatus=ref.id \n" +
                "       LEFT JOIN " + getCompanyId() + ".leave_reason lr ON lr.code = sr.reason_code \n" +
                "       where s.daytype <> '" + Constants.MONEY + "' and sr.employeeid=" + employeeID + " and (ref.code<>'" + EdsSickRequest.DENIED + "' or ref.code=null)) sd on date(sd.date)=date(dj.from_date) \n" +
                "LEFT JOIN " + getCompanyId() + ".attendancerawdata a on dj.from_date=a.date and a.employeeid=" + employeeID + " \n" +
                "where dj.from_date between '" + dateFormat.format(startDateT) + "' AND '" + dateFormat.format(endDateT) + "' \n" +
                "ORDER BY dj.from_date \n");
    }

    @Override
    public List<EdsSickRequest> getNonPaidLeaveRequests(ListingFilterParameter fp) {
        final HashMap<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        EdsReference status = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);
        EdsReference type = fp.getType() != null && fp.getType() == 0 ? referenceManager.findReference(EdsSickRequest._SICK_TYPE, EdsSickRequest.NON_PAID) : referenceManager.findReference(EdsSickRequest._SICK_TYPE, EdsSickRequest.PAID);
        params.put("employeeID", fp.getEmployeeId());
        params.put("status", status);
        params.put("type", type);
        sql.append("select sr from EdsSickRequest sr ");
        if (fp.getReasonCode() != null && !fp.getReasonCode().isEmpty()) {
            sql.append(" join sr.leaveReason reas ");
        }
        sql.append(" where sr.employee.objectID=:employeeID and sr.overallStatus=:status and sr.type=:type ");
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            if (fp.isActualDue()) {
                params.put("endDate", DateUtil.getDayLastTime(fp.getEndDate()));
                params.put("startDate", fp.getStartDate());
                sql.append(" and sr.startDate < :startDate and ((sr.endDate between :startDate and :endDate) or sr.endDate > :endDate) ")
                        .append("and (sr.takeByMoney is null or sr.takeByMoney != true) ");
            } else {
                params.put("endDate", DateUtil.getDayLastTime(fp.getEndDate()));
                params.put("startDate", fp.getStartDate());
                if (fp.getType() != null) {
                    sql.append("and sr.startDate<=:endDate and sr.endDate>=:startDate ");
                } else {
                    sql.append(" and sr.startDate between :startDate and :endDate ");
                }
            }
        }
        if (fp.getReasonCode() != null && !fp.getReasonCode().isEmpty()) {
            sql.append(" and reas.code = '").append(fp.getReasonCode()).append("' ");
        }
        if (Constants.LEAVE_REQUEST_TYPE.equals(fp.getExcludedType())) {
            sql.append(" and sr.paymentDeduction is null or sr.paymentDeduction.objectID not in (select paymentDeductionID from EdsPayslipPayments)");
        }
        return findByNamedParams(sql.toString(), params);
    }

    @Override
    public void delete(EdsSickRequest obj) {
        if (obj != null) {
            if (obj.getSickRequestComments() != null && obj.getSickRequestComments().size() > 0) {
                updateNative("delete from " + getCompanyId() + ".SickRequestComment  where sickRequestId = " + obj.getObjectID());
            }
            updateNative("delete from " + getCompanyId() + ".SickRequestDuration where sickRequestID = " + obj.getObjectID());
            updateNative("delete from " + getCompanyId() + ".backup_employees where sickrequest_id = " + obj.getObjectID());
            updateNative("delete from " + getCompanyId() + ".sickrequest where id = " + obj.getObjectID());
        }
    }

    @Override
    @Deprecated
    public List<EdsSickRequest> getLeaveRequestList(ListingFilterParameter fp) {
        return getEdsSickRequests(fp, false);

    }

    @Deprecated
    private List<EdsSickRequest> getEdsSickRequests(ListingFilterParameter fp, boolean isCount) {
        Map<StringBuilder, HashMap<String, Object>> query = sickRequestQuery(fp, false);
        StringBuilder sql = query.keySet().toArray(new StringBuilder[]{})[0];
        HashMap<String, Object> map = query.get(sql);
        if (!isCount) {
            sql.append("order by sr.startDate");
            return findIntervalByNamedParams(sql.toString(), fp.getStart(), fp.getLimit(), map);
        } else {
            return findByNamedParams(sql.toString(), map);
        }
    }

    @Override
    public List<EdsDepartment> getSickRequestDepartments(ListingFilterParameter fp) {
        Map<StringBuilder, HashMap<String, Object>> query = sickRequestQuery(fp, true);
        StringBuilder sql = query.keySet().toArray(new StringBuilder[]{})[0];
        HashMap<String, Object> map = query.get(sql);
        sql.append("order by sr.startDate");
        return findIntervalByNamedParams(sql.toString(), fp.getStart(), fp.getLimit(), map);
    }

    private Map<StringBuilder, HashMap<String, Object>> sickRequestQuery(ListingFilterParameter fp, boolean isDepartment) {
        Integer year = fp.getSelectedYear();

        HashMap<String, Object> map = new HashMap<>();
        if (year != null) {
            Date startYearDate = ServerUtils.getYearStartDate(year);
            Date endYearDate = ServerUtils.getYearEndDate(year);
            map.put("startYearDate", startYearDate);
            map.put("endYearDate", endYearDate);
        }

        EdsReference status = null;
        if (!ServerUtils.isNullOrEmpty(fp.getStatusCode())) {
            status = referenceManager.findReference(EdsSickRequest._SICK_STATUS, fp.getStatusCode());
        } else if (fp.getStatusID() != null) {
            status = referenceManager.get(fp.getStatusID());
        }
        EdsLeaveReason reason = fp.getReasonID() != null ? leaveReasonManager.get(fp.getReasonID()) : null;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        StringBuilder sql = new StringBuilder("select " + (isDepartment ? "sr.employee.employeeDepartment.department" : "sr") + " from EdsSickRequest sr ");
        sql.append("where sr.employee.deleted is not true ");
        sql.append("and sr.startDate >= '" + simpleDateFormat.format(fp.getStartDate()) + "' ");
        if (reason != null) {
            sql.append("and sr.leaveReason=:reason ");
            map.put("reason", reason);
        }
        if (status != null) {
            sql.append("and sr.overallStatus=:status ");
            map.put("status", status);
        }
        if (fp.getObjectIDs() != null && fp.getObjectIDs().size() > 0) {
            sql.append("and sr.objectID in (:objectIDs) ");
            map.put("objectIDs", ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", ","));
        }
        if (fp.getEmployeeId() != null && fp.isFromMobile()) {
            sql.append("and sr.employee.objectID =:employeeID ");
            map.put("employeeID", fp.getEmployeeId());

        } else if (fp.getEmployeeId() != null && !fp.isFromMobile()) {
            ArrayList<Integer> supervisorEmployeeIDs = employeeManager.getSupervisorIDsByEmployee(fp.getEmployeeId());
            ArrayList<Integer> departmentEmployeeIDs = departmentManager.getEmployeeIDsByTeamLeader(fp.getEmployeeId());
            supervisorEmployeeIDs.removeAll(departmentEmployeeIDs);
            supervisorEmployeeIDs.addAll(departmentEmployeeIDs);
            ArrayList<Integer> employeesIDs = supervisorEmployeeIDs;
            ArrayList<Integer> emptyIDs = new ArrayList<>();
            emptyIDs.add(0);
            employeesIDs = employeesIDs.size() > 0 ? employeesIDs : emptyIDs;
            sql.append("and sr.employee.objectID in (:employeesIDs) ");
            map.put("employeesIDs", employeesIDs);
        }
        if (fp.getApproverID() != null && !getUser().hasRole(EdsRole.ADMIN_CODE)) {
            sql.append("and sr.currentApprover.exactEmployee.objectID =:currentApproverID ");
            map.put("currentApproverID", fp.getApproverID());
        }
        if (year != null) {
            sql.append("and ((sr.startDate>= :startYearDate and sr.endDate<= :endYearDate) ");
            sql.append("or ((:startYearDate between sr.startDate and sr.endDate) and :startYearDate != sr.endDate) ");
            sql.append("or ((:endYearDate between sr.startDate and sr.endDate) and :endYearDate != sr.startDate)) ");
        }
        if (fp.getMonthId() != null) {
            map.put("month", fp.getMonthId() + 1);
            sql.append(" and (extract(month from sr.startDate) = :month or extract(month from sr.endDate) = :month) ");
        }
        if (!ServerUtils.isNullOrEmpty(fp.getName())) {
            sql.append(" and (lower(sr.employee.firstName) like '%" + fp.getName().toLowerCase() + "%' or ");
            sql.append(" lower(sr.employee.lastName) like '%" + fp.getName().toLowerCase() + "%') ");
        }
        if (!ServerUtils.isNullOrEmpty(fp.getSearchKey())) {
            sql.append(" and (lower(sr.employee.firstName) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower(sr.employee.lastName) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (sr.overallStatus.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (sr.leaveReason.name) like '" + fp.getSqlSearchKey() + "') ");
        }
        Map<StringBuilder, HashMap<String, Object>> response = new HashMap<>();
        response.put(sql, map);
        return response;
    }

    @Override
    @Deprecated
    public Integer getLeaveRequestListCount(ListingFilterParameter fp) {
        try {
            return getEdsSickRequests(fp, true).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public EdsSickRequest getEmployeeLastApprovedLeaveRequest(Integer employeeId, String statusCode) {
        return (EdsSickRequest) findSingle("select sr from EdsSickRequest sr " +
                "    where sr.employee.objectID=? and sr.overallStatus.code=? " +
                "        order by sr.startDate DESC ", employeeId, statusCode);
    }

    public List<AttendanceItem> getEmployeeDurationItems(Integer employeeID, Date startDateT, Date endDateT) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        /*List<Object[]> objList = findNative("SELECT distinct dj.from_date, coalesce(a.timeslot,0) as timeslot, " +
                "coalesce(sd.durationtime,0) as durationtime, sd.dayoff, a.holiday, a.timeSheet, a.timesheetpending " +
                "FROM datejoin dj \n" +
                "LEFT JOIN (select s.date, sum(s.durationtime) as durationtime, bool_and(s.dayoff) as dayoff \n" +
                "       from " + getCompanyId() + ".sickrequestduration s  \n" +
                "       LEFT JOIN " + getCompanyId() + ".sickrequest sr on sr.id=s.sickrequestid \n" +
                "       LEFT JOIN " + getCompanyId() + ".reference ref ON ref.id=sr.overallstatus \n" +
                "       where sr.employeeid=" + employeeID + " and ref.code='" + EdsSickRequest.APPROVED + "' group by s.date) sd on date(sd.date)=date(dj.from_date) \n" +
                "left join " + getCompanyId() + ".attendancerawdata a on a.date=dj.from_date and a.employeeid=" + employeeID + " \n" +
                "where dj.from_date between '" + dateFormat.format(startDateT) + "' AND '" + dateFormat.format(endDateT) + "' \n" +
                "ORDER BY dj.from_date \n");*/

        String sql = "select distinct on (from_date) dj.from_date, coalesce(a.timeslot,0) timeslot, coalesce(sd.durationtime,0) durationtime, \n" +
                " (case when sd.dayoff then true else false end) as dayoff,\n" +
                " (case when a.holiday then true else false end ) as holiday,\n" +
                " coalesce(a.timeSheet,0) timeSheet, coalesce(a.timesheetpending,0) timesheetpending \n" +
                " from " + getPublic() + ".datejoin dj \n" +
                " left join (\n" +
                " select s.date, sum(s.durationtime) as durationtime , bool_and(s.dayoff) as dayoff \n" +
                " from " + getCompanyId() + ".sickrequestduration s \n" +
                " left join " + getCompanyId() + ".sickrequest sr on sr.id = s.sickrequestid \n" +
                " left join " + getCompanyId() + ".sickrequest srpr on sr.parentid = srpr.id \n" +
                " left join " + getCompanyId() + ".sickrequest srorg on COALESCE(srpr.id,sr.id) = srorg.id \n" +
                " left join " + getCompanyId() + ".reference ref ON ref.id = srorg.overallstatus \n" +
                " where s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' and srorg.employeeid = " + employeeID + " and ref.code = '" + EdsSickRequest.APPROVED + "' group by s.date \n" +
                " ) \n sd on date(sd.date) = date(dj.from_date) \n" +
                " left join " + getCompanyId() + ".attendancerawdata a on a.date = dj.from_date and a.employeeid = " + employeeID + "\n" +
                " where dj.from_date between '" + dateFormat.format(startDateT) + "' and '" + dateFormat.format(endDateT) + "' \n" +
                " order by dj.from_date";

        List<Object[]> objList = findNative(sql);

        List<AttendanceItem> list = new ArrayList<>();
        for (Object[] obj : objList) {
            AttendanceItem item = new AttendanceItem();
            item.setTimeslot(Integer.parseInt(obj[1].toString()));
            item.setLeave(Integer.parseInt(obj[2].toString()));
            item.setDayOff(obj[3] != null && Boolean.parseBoolean(obj[3].toString()));
            item.setHoliday(obj[4] != null && Boolean.parseBoolean(obj[4].toString()));
            item.setTimeSheet(Integer.parseInt(obj[5].toString()));
            item.setTimeSheetPending(Integer.parseInt(obj[6].toString()));
            list.add(item);
        }
        return list;
    }

    @Override
    public List<EdsSickRequest> getLeaveRequestListByEmployee(Integer employeeID) {
        return (List<EdsSickRequest>) find("select lr from EdsSickRequest lr where lr.employee.objectID = ? ", employeeID);
    }

    @Override
    public List<EdsSickRequest> getDailyLeaveRequests(Date startDate, Date endDate) {
        String companyId = getCompanyId();
        String sql = "select sr.* from " + companyId + ".sickRequest sr" +
                "  left join " + companyId + ".sickRequestDuration sd on sd.sickRequestID = sr.id" +
                "  where sr.employeeId is not null and sd.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' " +
                "      and (sd.day is not null and sd.day = 1)" +
                "      and sd.date >=:startDate" +
                "      and sd.date <=:endDate";
        return this.slaveEntityManager.createNativeQuery(sql, EdsSickRequest.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    public List<EdsSickRequest> getLeaveRequestByParentId(Integer parentID) {
        return (List<EdsSickRequest>) find("select lr from EdsSickRequest lr where lr.parent.objectID = ? ", parentID);
    }

    @Override
    public List<EdsSickRequest> findApprovedLeavesByExcludeSick(Integer objectID, int userId, Date startDate, Date endDate) {
        String sql = "select sr from EdsSickRequest sr where ((sr.startDate>=:startDate and coalesce(sr.recallDate, sr.endDate)<=:endDate) \n" +
                "or (sr.startDate>=:startDate and sr.startDate<=:endDate) \n" +
                "or (coalesce(sr.recallDate, sr.endDate)>=:startDate and coalesce(sr.recallDate, sr.endDate)<=:endDate) \n" +
                "or (sr.startDate<=:startDate and coalesce(sr.recallDate, sr.endDate)>=:endDate)) \n" +
                "and sr.overallStatus.code != '" + EdsSickRequest.DENIED + "' \n" +
                "and sr.overallStatus.code != '" + EdsSickRequest.DRAFT + "' and sr.employee.objectID = " + userId + " and sr.objectID !=:objectID";

        return findByNamedParams(sql, preparing(new Entry("startDate", startDate), new Entry("endDate", endDate), new Entry("objectID", objectID)));
    }


    @Override
    public Object getLeaveStats(String startDate, String endDate) {
        String queryString = "select r.name, count(sr) from EdsAnnualLeaveAllowance sr" +
                " left join sr.reason r" +
                " where sr.employee = " + getUser().getObjectID() +
                " and sr.effectiveDate > '" + startDate + "'" +
                " and sr.effectiveDate < '" + endDate + "'" +
                " group by r";
        return find(queryString);
    }

    @Override
    public List<EdsSickRequest> getLeaveRequestListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select cs from EdsSickRequest cs where 1=1");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            sqlQuery.append(" and cs.createdDate >= :modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sqlQuery.append(" and cs.createdDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sqlQuery.append(" order by cs.objectID asc ");
        return findIntervalByNamedParams(sqlQuery.toString(), start, limit, params);
    }
}
