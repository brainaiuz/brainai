package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployeeWageClientRateHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeEntriesItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeSheetSuggestItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository("timeSheetManager")
public class TimeSheetManagerImpl extends
        AttachmentSupportManager<EdsTimeSheet> implements TimeSheetManager, Constants {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private ReferenceManager referenceManager;

    public TimeSheetManagerImpl() {
        super(EdsTimeSheet.class);
    }

    public List<EdsTimeSheet> getTimeSheets(EdsEmployeeTask employeeTask) {
        return find("SELECT ts FROM EdsTimeSheet ts WHERE ts.employeeTask=?", employeeTask);
    }

    public List<EdsTimeSheet> getTimeSheets(Integer taskID, Integer employeeID) {
        return find("SELECT ts FROM EdsTimeSheet ts WHERE " +
                "ts.employeeTask.projectEmployee.employeeDepartment.employee.objectID=? AND ts.employeeTask.task.objectID=?", employeeID, taskID);
    }

    @Override
    public List<TaskTimeSheetSuggestItem> getTimeSheetsForSuggest(Integer taskID, Integer employeeID) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT t.id, t.comment, t.entrydate, t.taskid, t.timespent, ta.number, ta.name, ta.description, ta.startdate, ta.duedate ")
                .append("FROM ").append(getCompanyId()).append(".timesheet t ")
                .append("LEFT JOIN ").append(getCompanyId()).append(".task ta ON t.taskid = ta.id ")
                .append("WHERE t.employeeid = ").append(employeeID).append(" ")
                .append("AND t.taskid = ").append(taskID).append(" ")
                .append("ORDER BY t.entrydate DESC LIMIT 40 ");
        return jdbcSpringManager.getSimpleJdbcTemplate().query(query.toString(), BeanPropertyRowMapper.newInstance(TaskTimeSheetSuggestItem.class));
    }


    public List<EdsTimeSheet> getTimeSheetByTaskID(Integer taskID, Integer employeeTaskID) {
        return find("SELECT ts FROM EdsTimeSheet ts WHERE ts.timeSpent>0 AND " +
                "ts.employeeTask.objectID=? AND ts.employeeTask.task.objectID=?", employeeTaskID, taskID);
    }

    public List<EdsTimeSheet> getTimeSheets(Integer taskID, Integer employeeID, Date date) {
        return find("SELECT ts FROM EdsTimeSheet ts WHERE " +
                "ts.employeeTask.projectEmployee.employeeDepartment.employee.objectID=? AND ts.employeeTask.task.objectID=? AND ts.date=?", employeeID, taskID, date);
    }

    @Override
    public List<EdsTimeSheet> getNotFilledTimesheetForToday(Date date) {
        return find("SELECT tsh FROM EdsTimeSheet tsh " +
                "WHERE tsh.date=? and tsh.dailyEstimatedTime>0 and tsh.timeSpent=0  and tsh.employeeTask.deleted is not true", date);
    }

    public Integer getTotalTimeSheets(EdsEmployeeTask employeeTask) {
        Long result = (Long) findSingle("SELECT SUM(ts.timeSpent) FROM EdsTimeSheet ts WHERE ts.employeeTask=?", employeeTask);
        if (result == null) {
            result = 0L;
        }
        return result.intValue();
    }

    public Integer getTotalDailyEstimatedTimesTimeSheet(EdsEmployeeTask employeeTask) {
        Long result = (Long) findSingle("SELECT SUM(CASE WHEN ts.dailyEstimatedTime is not null THEN ts.dailyEstimatedTime ELSE 0 END) FROM EdsTimeSheet ts WHERE ts.employeeTask=?", employeeTask);
        if (result == null) {
            result = 0L;
        }
        return result.intValue();
    }

    public List getEmployeeTaskTotalTimeSheets(String taskIds, Integer employeeId) {
        return find("SELECT ts.taskID, SUM(ts.timeSpent) FROM EdsTimeSheet ts WHERE ts.taskID IN(" + taskIds + ") and ts.employeeID = " + employeeId + " GROUP BY ts.taskID");
    }

    public Integer getEmployeeTaskTotalTimeSheet(String taskIds, Integer employeeId, Date date) {
        Long result = Long.valueOf("0");
        if (date == null) {
            result = (Long) findSingle("SELECT SUM(ts.timeSpent) FROM EdsTimeSheet ts WHERE ts.taskID IN(" + taskIds + ") and ts.employeeID = " + employeeId + " GROUP BY ts.taskID");
        } else {
            result = (Long) findSingle("SELECT SUM(ts.timeSpent) FROM EdsTimeSheet ts WHERE ts.taskID IN(" + taskIds + ") and ts.employeeID = " + employeeId + " and ts.date = '" + date + "' GROUP BY ts.taskID");
        }
        return result != null ? result.intValue() : 0;
    }

    public Integer getTotalTimeSheetHours(Integer employeeTaskID, Integer statusID) {
        Long result = (Long) findSingle("SELECT SUM(ts.timeSpent) FROM EdsTimeSheet ts WHERE ts.employeeTask.objectID =? and ts.status.objectID =?", employeeTaskID, statusID);
        return (result != null) ? result.intValue() : 0;
    }

    public List<TaskInvolvedMember> getSumTimeSheets(Integer taskID) {
        Map<String, Object> map = new HashMap<>();
        EdsReference completed = referenceManager.getByCode(EdsTask.COMPLETED);
        EdsReference closed = referenceManager.getByCode(EdsTask.CLOSED);
        EdsReference inactiveSts = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_RESIGNED);

        //map.put("completed", completed);
        //map.put("closed", closed);
        map.put("inactive", inactiveSts);
        map.put("taskId", taskID);
        map.put("isDeleted", Boolean.TRUE);

        List<TaskInvolvedMember> taskInvolvedMembers = findByNamedParams(
                "SELECT NEW com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember"
                        + "(et.objectID, et.projectEmployee.objectID, "
                        + "et.projectEmployee.employeeDepartment.department.objectID, "
                        + "et.projectEmployee.employeeDepartment.department.name, "
                        + "et.projectEmployee.employeeDepartment.employee.firstName || ' ' || coalesce(et.projectEmployee.employeeDepartment.employee.lastName, '') " +
                        " || ' ' || coalesce (case when (et.projectEmployee.employeeDepartment.employee.accountStatus=:inactive) THEN '(resigned)' ELSE '' END), "
                        + "et.estimatedTime, "
                        + "et.status.objectID, "
                        + "et.status.name, "
                        + "et.percent, "
                        + "et.timeSpent, et.projectEmployee.employeeDepartment.employee.objectID, "
                        + "et.projectEmployee.employeeDepartment.employee.profile.employeeCode, "
                        + "et.projectEmployee.employeeDepartment.employee.objectID) "
                        + "FROM EdsEmployeeTask et "
                        + "WHERE et.task.objectID=:taskId AND et.deleted <>:isDeleted " +
                        " and (et.projectEmployee.employeeDepartment.employee.deleted<>true " +
                        " OR (et.projectEmployee.employeeDepartment.employee.accountStatus=:inactive)) "
                        + "GROUP BY et.objectID, et.projectEmployee.objectID, et.projectEmployee.employeeDepartment.department.objectID, et.projectEmployee.employeeDepartment.employee.objectID, " +
                        "et.projectEmployee.employeeDepartment.department.name, et.projectEmployee.employeeDepartment.employee.firstName, " +
                        "et.projectEmployee.employeeDepartment.employee.lastName, et.estimatedTime, et.status.objectID, et.status.name, " +
                        "et.percent, et.timeSpent, et.projectEmployee.employeeDepartment.employee.profile.employeeCode, " +
                        "et.projectEmployee.employeeDepartment.employee.deleted, et.task.status, et.projectEmployee.employeeDepartment.employee.accountStatus", map
        );

        if (taskInvolvedMembers != null && taskInvolvedMembers.size() > 0) {
            for (TaskInvolvedMember taskInvolvedMember : taskInvolvedMembers) {
                Double[] timeSpent = getEmployeeTimeSpent(taskID, taskInvolvedMember.getAssignEmployeeID());
                if (timeSpent != null) {
                    taskInvolvedMember.setTimeSpent(timeSpent[0] != null ? timeSpent[0].intValue() : 0);
                    taskInvolvedMember.setActualTime(timeSpent[1] != null ? timeSpent[1].intValue() : 0);
                }
                taskInvolvedMember.setEstimateTimeInString(taskInvolvedMember.getEstimatedTime() != null ?
                        (taskInvolvedMember.getEstimatedTime() / 60 > 9 ? "" : "0") + taskInvolvedMember.getEstimatedTime() / 60
                                + ":"
                                + (taskInvolvedMember.getEstimatedTime() % 60 > 9 ? "" : "0") + taskInvolvedMember.getEstimatedTime() % 60
                        : "00:00");

            }
        }
        return taskInvolvedMembers;
    }

    private Double[] getEmployeeTimeSpent(Integer taskID, Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select sum(t.timespent) as timespent, sum(t.actualTimespent) as actual from ");
        sql.append("(select sum(cast(tsh.timeSpent as double precision)) as timespent, case when ref.code='_APPROVE' then sum(cast(tsh.timeSpent as double precision)) end actualTimespent ");
        sql.append("from ").append(companyID).append(".timesheet tsh ");
        sql.append("left join ").append(companyID).append(".reference ref on ref.id = tsh.statusid ");
        sql.append(" where tsh.taskid=").append(taskID).append(" and tsh.employeeid=").append(employeeID);
        sql.append(" group by tsh.taskid, ref.code ) t");
        Object[] listResult = (Object[]) findNativeSingle(sql.toString());
        return listResult != null ? Arrays.copyOf(listResult, listResult.length, Double[].class) : null;
    }

    @Override
    public Integer getSumTimeSpentEmployeeInTheTask(Integer taskID, Integer employeeID) {
        String sql = "SELECT SUM(et.timeSpent) FROM EdsEmployeeTask et " +
                "left join et.projectEmployee pe " +
                "left join pe.employeeDepartment te " +
                "left join te.employee e " +
                "WHERE et.task.objectID = '" + taskID + "' " +
                "AND e.objectID = '" + employeeID + "' ";
        Object object = findSingle(sql);
        if (object == null) {
            return 0;
        }
        return ((Long) object).intValue();
    }

    public List<EdsTimeSheet> getTimeEntries(Integer taskID) {
        return find("SELECT ts FROM EdsTimeSheet ts WHERE " +
                "ts.employeeTask.task.objectID=? and ts.timeSpent > 0 order by ts.date asc", taskID);
    }

    public void updateTimeEntries(Integer taskID, Integer projectID) {
        updateNative("update " + getCompanyId() + ".timesheet set projectID = " + projectID +
                " where employeetaskId in (select id from " + getCompanyId() + ".employeeTask where taskid = " + taskID + ") and timespent > 0");
        updateNative("update " + getCompanyId() + ".taskrbachistory set projectid = " + projectID + "where taskid = " + taskID);
    }

    public void updateDailyEstimatedTimeByEmployeeTask(EdsEmployeeTask employeeTask) {
        update("UPDATE EdsTimeSheet ts SET ts.dailyEstimatedTime=null WHERE ts.employeeTask=?", employeeTask);
    }

    public List<EdsTimeSheet> list(EdsEmployee employee, Date startDate, Date endDate) {
        Map<String, Object> map = new HashMap<>();
        map.put("employeeID", employee.getObjectID());
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return findByNamedParams("SELECT t FROM EdsTimeSheet t JOIN FETCH t.employeeTask "
                + "WHERE t.employeeID=:employeeID "
                + "AND t.date >= :startDate AND t.date <= :endDate ORDER BY t.entryDate DESC", map);

    }

    public Long getTodayEntered(EdsUser user) {
        Calendar calendar = new GregorianCalendar(user.getCompany()
                .getTimeZone());
        calendar.setTime(user.getCompany().getCompanyDate());
        Calendar start = new GregorianCalendar();
        start.setTime(calendar.getTime());
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        Date from = start.getTime();
        Date to = calendar.getTime();
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("from", from);
        map.put("to", to);
        return (Long) findSingleByNamedParams("select sum(t.timeSpent) from EdsTimeSheet t where t.employeeTask.projectEmployee.employeeDepartment.employee=:user "
                + "and t.date between :from and :to", map);
    }

    public Long getThisWeekEntered(EdsUser user) {
        Calendar calendar = new GregorianCalendar(user.getCompany()
                .getTimeZone());
        calendar.setTime(user.getCompany().getCompanyDate());
        Calendar start = new GregorianCalendar();
        start.setTime(calendar.getTime());
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        int week = start.get(Calendar.WEEK_OF_YEAR);
        if (start.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            start.add(Calendar.WEEK_OF_YEAR, -1);
        }
        start.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        if (start.get(Calendar.WEEK_OF_YEAR) > week) {
            start.add(Calendar.WEEK_OF_YEAR, -1);
        } else {
            start.add(Calendar.WEEK_OF_YEAR, 1);
        }
        Date from = start.getTime();
        Date to = calendar.getTime();

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("from", from);
        map.put("to", to);

        return (Long) findSingleByNamedParams("select sum(t.timeSpent) from EdsTimeSheet t where t.employeeTask.projectEmployee.employeeDepartment.employee=:user "
                + "and t.date between :from and :to", map);

    }

    public Long getThisMonthEntered(EdsUser user) {
        Calendar calendar = new GregorianCalendar(user.getCompany()
                .getTimeZone());
        calendar.setTime(user.getCompany().getCompanyDate());
        Calendar start = new GregorianCalendar();
        start.setTime(calendar.getTime());
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        start.set(Calendar.DAY_OF_MONTH, start
                .getMinimum(Calendar.DAY_OF_MONTH));
        Date from = start.getTime();
        Date to = calendar.getTime();
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("from", from);
        map.put("to", to);
        return (Long) findSingleByNamedParams("select sum(t.timeSpent) from EdsTimeSheet t where t.employeeTask.projectEmployee.employeeDepartment.employee=:user "
                + "and t.date between :from and :to", map);

    }

    public Long getCompanyEntered(EdsCompany company, String startDateFormat, String endDateFormat) {
        Map<String, Object> map = new HashMap<>();
        map.put("start", startDateFormat);
        map.put("end", endDateFormat);
        return (Long) findSingleByNamedParams("select sum(t.timeSpent) from EdsTimeSheet t where " +
                "to_char(t.date,'yyyy-MM-dd') >= :start and to_char(t.date,'yyyy-MM-dd') <= :end", map);
    }

    public List<Object[]> getCompanyTeamsByCompanyId(Integer companyID, String startDate, String endDate) {
        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append(" select t.name, sum(ts.timespent)/60");
        sql.append(" from " + companyID + ".timesheet ts ");
        sql.append(" left outer join " + getCompanyId() + ".employeetask et on (et.id=ts.employeetaskId )");
        sql.append(" left outer join " + getCompanyId() + ".task ta on (et.taskid=ta.id )");
        sql.append(" left outer join " + getCompanyId() + ".projectemployee pe on(et.projectemployeeid=pe.id ) ");
        sql.append(" left outer join " + getCompanyId() + ".project p on (pe.projectid=p.id ) ");
        sql.append(" left outer join " + getCompanyId() + ".teamemployee te on (pe.employeedepartmentid=te.id ) ");
        sql.append(" left outer join " + getCompanyId() + ".myuser mu on (te.employeeid=mu.id) ");
        sql.append(" left outer join " + getCompanyId() + ".employee e on (e.id=te.employeeid) ");
        sql.append(" left outer join " + getCompanyId() + ".team t on (t.id=te.teamid) ");
        sql.append(" where ts.date between to_date('" + startDate + "','yyyy-mm-dd') and to_date('" + endDate + "','yyyy-mm-dd')");
        sql.append(" and ta.deleted<>true ");
        sql.append(" group by t.id, t.name");
        sql.append(" order by sum(ts.timespent)/60 desc");
        sql.append(" limit 6");
        return findNative(sql.toString());
    }

    public List getTimeSheetSummaryData(String range, Integer companyID, String startDate, String endDate) {
        EdsUser user = getUser();

        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append(" select substring(cast(ts.date as varchar) from 1 for "
                + range
                + ") as dateRange, sum(ts.timespent)/60 ");
        sql.append(" from " + getCompanyId() + ".timesheet ts ");
        sql.append(" left outer join " + getCompanyId() + ".employeetask et on (et.id=ts.employeetaskId )");
        sql.append(" left outer join " + getCompanyId() + ".task ta on (et.taskid=ta.id )");
        sql.append(" left outer join " + getCompanyId() + ".projectemployee pe on(et.projectemployeeid=pe.id ) ");
        sql.append(" left outer join " + getCompanyId() + ".project p on (pe.projectid=p.id ) ");
        sql.append(" left outer join " + getCompanyId() + ".teamemployee te on (pe.employeedepartmentid=te.id ) ");
        sql.append(" left outer join " + getCompanyId() + ".myuser mu on (te.employeeid=mu.id) ");
        sql.append(" left outer join " + getCompanyId() + ".employee e on (e.id=te.employeeid) ");
        sql.append(" left outer join " + getCompanyId() + ".team t on (t.id=te.teamid) ");
        sql.append(" where ts.date between to_date('" + startDate + "','yyyy-mm-dd') and to_date('" + endDate + "','yyyy-mm-dd')");
        sql.append(" and ta.deleted<>true ");
        sql.append(" group by dateRange order by dateRange ");

        return findNative(sql.toString());
    }

    public List getClientSummaryData(Integer companyID, String startDate, String endDate) {
        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append(" select c.name, sum(ts.timespent)/60");
        sql.append(" from  " + getCompanyId() + ".timesheet ts ");
        sql.append(" left outer join " + getCompanyId() + ".employeetask et on (et.id=ts.employeetaskId )");
        sql.append(" left outer join " + getCompanyId() + ".task ta on (et.taskid=ta.id )");
        sql.append(" left outer join " + getCompanyId() + ".projectemployee pe on(et.projectemployeeid=pe.id ) ");
        sql.append(" left outer join " + getCompanyId() + ".project p on (pe.projectid=p.id ) ");
        sql.append(" left outer join " + getCompanyId() + ".teamemployee te on (pe.employeedepartmentid=te.id ) ");
        sql.append(" left outer join " + getCompanyId() + ".crmaccount c on (c.id=p.clientid ) ");
        sql.append(" left outer join " + getCompanyId() + ".myuser mu on (te.employeeid=mu.id) ");
        sql.append(" left outer join " + getCompanyId() + ".employee e on (e.id=te.employeeid) ");
        sql.append(" left outer join " + getCompanyId() + ".team t on (t.id=te.teamid) ");
        sql.append(" where ts.date between to_date('" + startDate + "','yyyy-mm-dd') and to_date('" + endDate + "','yyyy-mm-dd')");
        sql.append(" and ta.deleted<>true ");
        sql.append(" group by c.id, c.name order by sum(ts.timespent)/60 desc limit 5");

        return findNative(sql.toString());
    }

    @Deprecated
    public List<Integer> getCompaniesByTSDate(Date sTime, Date eTime) {
        return find("select ts.employeeTask.task.project.company.objectID FROM EdsTimeSheet ts where ts.date between '" + sTime + "' and '" + eTime + "' group by ts.employeeTask.task.project.company.objectID");
    }

    @Deprecated
    public List<Integer> getTimeSheetTasksByRegDate(Date sTime, Date eTime) {
        return find("select ts.employeeTask.objectID FROM EdsTimeSheet ts where (ts.date between '" + sTime + "' and '" + eTime + "') group by ts.employeeTask.objectID");
    }

    @Deprecated
    public Integer getCompanyTimeSpentByDate(EdsCompany company, Date sTime, Date eTime) {
        Long result = (Long) findSingle("select sum(ts.timeSpent) FROM EdsTimeSheet ts where " +
                "(ts.date between '" + sTime + "' and '" + eTime + "')");
        return result != null ? result.intValue() : 0;
    }

    public List<EdsEmployee> getTimesheetEmployeesByProject(EdsProject project, Date sTime, Date eTime) {
        return find("select distinct ts.employeeTask.projectEmployee.employeeDepartment.employee FROM EdsTimeSheet ts" +
                " where ts.employeeTask.task.project=? and (ts.date between '" + sTime + "' and '" + eTime + "')", project);
    }

    public List<EdsTimeSheet> getTimeSheetDataByProjectAndEmployee(EdsProject project, EdsEmployee employee, Date sTime, Date eTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        map.put("project", project);
        return findByNamedParams("select distinct ts FROM EdsTimeSheet ts where (ts.employeeTask.projectEmployee.employeeDepartment.employee=:employee and " +
                " ts.employeeTask.task.project=:project and ts.timeSpent>0 and (ts.date between '" + sTime + "' and '" + eTime + "'))", map);
    }

    public List<EdsTimeSheet> getTimesheetEntriesForApproval(List<Integer> projectIDs, EdsEmployee employee, Date sTime, Date eTime, EdsReference waiting, EdsReference approved) {
        Map<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        map.put("projects", projectIDs);
        map.put("waiting", waiting);
        map.put("approved", approved);
        return findByNamedParams("select distinct ts FROM EdsTimeSheet ts where (ts.employeeTask.projectEmployee.employeeDepartment.employee=:employee and " +
                " ts.employeeTask.task.project.objectID in (:projects) and ts.employeeTask.deleted<>true and ts.timeSpent>0 and (ts.date between '" + sTime + "' and '" + eTime + "') and ((ts.status is null) or (ts.status <>:waiting and ts.status <> :approved)))", map);
    }

    @Override
    public List<EdsTimeSheet> getTimesheetEntriesForApprovalByProjectAndTaskIds(LinkedHashMap<String, String> projectTasks, EdsEmployee employee, Date sTime, Date eTime, EdsReference waiting, EdsReference approved) {
        Map<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        map.put("waiting", waiting);
        map.put("approved", approved);
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ts FROM EdsTimeSheet ts where (ts.employeeTask.projectEmployee.employeeDepartment.employee=:employee and ts.employeeTask.deleted<>true and ts.timeSpent>0 and (ts.date between '").append(sTime).append("' and '").append(eTime).append("') and ((ts.status is null) or (ts.status <>:waiting and ts.status <> :approved)))");
        if (projectTasks != null && projectTasks.size() > 0) {
            sql.append(" and (");
            String orStr = "";
            String andStr = "";
            boolean needAndStr;
            for (String key : projectTasks.keySet()) {
                needAndStr = false;
                sql.append(orStr);
                sql.append(" (");
                String[] part = key.split(":");
                orStr = " or  ";
                andStr = " and ";
                if (!part[0].equals("0")) {
                    sql.append("ts.employeeTask.task.project.objectID=").append(part[0]);
                    needAndStr = true;
                }
                if (!part[1].equals("0")) {
                    if (needAndStr) {
                        sql.append(andStr);
                    }
                    sql.append("ts.employeeTask.task.objectID=").append(part[1]);
                }
                sql.append(") ");
            }
            sql.append(") ");
        }
        return findByNamedParams(sql.toString(), map);
    }

    public List<EdsEmployee> getTimeSheetEmployeesByDepartment(EdsDepartment department, Date sTime, Date eTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("department", department);
        return findByNamedParams("select distinct ts.employeeTask.projectEmployee.employeeDepartment.employee FROM EdsTimeSheet ts where (ts.employeeTask.projectEmployee.employeeDepartment.department=:department" +
                " and (ts.date between '" + sTime + "' and '" + eTime + "'))", map);
    }

    public List<EdsTimeSheet> getTimeSheetDataByDepartmentAndEmployee(EdsDepartment department, EdsEmployee employee, Date sTime, Date eTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        map.put("department", department);
        return findByNamedParams("select distinct ts FROM EdsTimeSheet ts where (ts.employeeTask.projectEmployee.employeeDepartment.employee=:employee and " +
                " ts.employeeTask.projectEmployee.employeeDepartment.department=:department and (ts.date between '" + sTime + "' and '" + eTime + "'))", map);
    }

    public Date getTimeSheetMaxDateByTaskID(EdsEmployeeTask employeeTask) {
        Map<String, Object> map = new HashMap<>();
        map.put("employeeTask", employeeTask);
        return (Date) findSingleByNamedParams("SELECT max(ts.date) FROM EdsTimeSheet ts WHERE ts.employeeTask=:employeeTask", map);
    }

    public Integer getProjectActualTime(EdsProject project, Integer type) {
        int actualTime = 0;
        Date sTime, eTime;
        Calendar calendar = new GregorianCalendar();
        if (type.equals(1)) {
            sTime = new Date();
            eTime = new Date();
            sTime = setStartTime(sTime);
            eTime = setEndTime(eTime);
            actualTime = calculateActualTime(project, sTime, eTime);
        } else if (type.equals(2)) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            sTime = calendar.getTime();
            eTime = new Date();
            sTime = setStartTime(sTime);
            eTime = setEndTime(eTime);
            actualTime = calculateActualTime(project, sTime, eTime);
        } else if (type.equals(3)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            sTime = calendar.getTime();
            eTime = new Date();
            sTime = setStartTime(sTime);
            eTime = setEndTime(eTime);
            actualTime = calculateActualTime(project, sTime, eTime);
        }
        return actualTime;
    }

    public Integer getDepartmentActualTime(EdsDepartment department, Integer type) {
        int actualTime = 0;
        Date sTime, eTime;
        Calendar calendar = new GregorianCalendar();
        if (type.equals(1)) {
            sTime = new Date();
            eTime = new Date();
            sTime = setStartTime(sTime);
            eTime = setEndTime(eTime);
            actualTime = calculateActualTime(department, sTime, eTime);
        } else if (type.equals(2)) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            sTime = calendar.getTime();
            eTime = new Date();
            sTime = setStartTime(sTime);
            eTime = setEndTime(eTime);
            actualTime = calculateActualTime(department, sTime, eTime);
        } else if (type.equals(3)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            sTime = calendar.getTime();
            eTime = new Date();
            sTime = setStartTime(sTime);
            eTime = setEndTime(eTime);
            actualTime = calculateActualTime(department, sTime, eTime);
        }
        return actualTime;
    }

    private Integer calculateActualTime(EdsProject project, Date sTime, Date eTime) {
        int at = 0;
        List<EdsEmployee> empls = getTimesheetEmployeesByProject(project, sTime, eTime);
        for (EdsEmployee emp : empls) {
            List<EdsTimeSheet> tsList = getTimeSheetDataByProjectAndEmployee(project, emp, sTime, eTime);
            for (EdsTimeSheet ts : tsList) {
                at += ts.getTimeSpent();
            }
        }
        return at;
    }

    private Integer calculateActualTime(EdsDepartment department, Date sTime, Date eTime) {
        int at = 0;
        List<EdsEmployee> empls = getTimeSheetEmployeesByDepartment(department, sTime, eTime);
        for (EdsEmployee emp : empls) {
            List<EdsTimeSheet> tsList = getTimeSheetDataByDepartmentAndEmployee(department, emp, sTime, eTime);
            for (EdsTimeSheet ts : tsList) {
                at += ts.getTimeSpent();
            }
        }
        return at;
    }

    private Date setStartTime(Date sTime) {
        sTime.setHours(0);
        sTime.setMinutes(0);
        sTime.setSeconds(0);
        return sTime;
    }

    private Date setEndTime(Date eTime) {
        eTime.setHours(23);
        eTime.setMinutes(59);
        eTime.setSeconds(59);
        return eTime;
    }

    public List<EdsTimeSheet> getTimeSheets(EdsEmployee employeeFilter, EdsProject projectFilter, EdsCrmAccount clientFilter, EdsDepartment departmentFilter, Integer viewAsFilter, Date from, Date to) {
        Map<String, Object> paramMap = new HashMap<>();

        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append(" select distinct ts from EdsTimeSheet ts");
        sql.append(" where ts.date between :startDate and :endDate ");
        paramMap.put("startDate", from);
        paramMap.put("endDate", to);
        if (departmentFilter != null) {
            sql.append(" and ts.employeeTask.projectEmployee.employeeDepartment.department =:pDepartmentFilter ");
            paramMap.put("pDepartmentFilter", departmentFilter);
        }
        if (employeeFilter != null) {
            sql.append(" and ts.employeeTask.projectEmployee.employeeDepartment.employee=:pEmployeeFilter ");
            paramMap.put("pEmployeeFilter", employeeFilter);
        }
        return findByNamedParams(sql.toString(), paramMap);
    }

    public List<EdsTimeSheet> getTimeSheetIdIn(String ids) {
        return (List<EdsTimeSheet>) find("SELECT ts FROM EdsTimeSheet ts WHERE ts.id in (" + ids + ")");
    }

    public List<EdsTimeSheet> getEmployeeTaskTimeEntries(EdsEmployeeTask eTask) {
        return find("select t from EdsTimeSheet t where t.employeeTask =?", eTask);
    }

    public String getEmployeeLastDepartment(Integer employeeId) {
        List<Object[]> emplDep = null;
        String teamName = "";
        String companyID = getCompanyId();
        emplDep = findNative("select id, name from " + companyID + ".team where id = " +
                "(select teamid from  " + companyID + ".teamemployee where startdate = " +
                "(select max(startdate) from  " + companyID + ".teamemployee where employeeid = " +
                "(select distinct te.employeeid from  " + companyID + ".projectemployee pe " +
                "left join " + companyID + ".teamemployee te on (pe.employeedepartmentid = te.id) " +
                "where pe.id=" + employeeId + ") and isdeleted = false) " +
                "and employeeid = " +
                "(select te.employeeid from " + companyID + ".projectemployee pe " +
                "left join " + companyID + ".teamemployee te on (pe.employeedepartmentid = te.id) " +
                "where pe.id=" + employeeId + ") and isdeleted = false)");
        if (emplDep != null && emplDep.size() > 0) {
            for (Object[] team : emplDep) {
                teamName = (String) team[1];
            }
        }
        return teamName;
    }

    public Integer getWorkedTimeForMobile(Integer employeeID, Integer taskID, Date date) {
        Long result;
        if (date == null) {
            result = (Long) findSingle("select sum(t.timeSpent) from EdsTimeSheet t where t.employeeID = ? and t.taskID = ?", employeeID, taskID);
        } else {
            result = (Long) findSingle("select sum(t.timeSpent) from EdsTimeSheet t where t.employeeID = ? and t.taskID = ? and t.date = ?", employeeID, taskID, date);
        }

        if (result == null) {
            result = 0L;
        }
        return result.intValue();
    }

    public EdsTimeSheet getTimeshetForMobile(EdsEmployeeTask employeeTask, Date date) {
        return (EdsTimeSheet) findSingle("from EdsTimeSheet t where t.employeeTask = ? and t.date = ?", employeeTask, date);
    }

    public EdsTimeSheet getTimeshetForMobile(Integer employeeID, Integer taskID, Date date) {
        return (EdsTimeSheet) findSingle("from EdsTimeSheet t where t.employeeID = ? and t.taskID = ? and t.date = ?", employeeID, taskID, date);
    }

    @Override
    public List<EdsTimeSheet> getTimesheetsByNotExportToQB(Integer limit) {

        return findInterval("SELECT t FROM EdsTimeSheet  t " +
                "WHERE t.quickbookTimesheetId is null " +
                "AND t.timeSpent > 0 " +
                "AND t.employeeTask.task.deleted = false", 0, limit);
    }

    public Date getFirstTimesheetDateForTask(EdsTask task) {
        return (Date) findSingle("select ts.date from EdsTimeSheet ts where ts.employeeTask.task=? and ts.timeSpent>0 order by ts.date asc", task);
    }

    public Date getLastTimesheetDateForTask(EdsTask task) {
        return (Date) findSingle("select ts.date from EdsTimeSheet ts where ts.employeeTask.task=? and ts.timeSpent>0 order by ts.date desc", task);
    }

    public EdsTimeSheet getTimeSheet(EdsEmployeeTask employeeTask, Date timesheetDate) {
        return (EdsTimeSheet) findSingle("select ts from EdsTimeSheet ts where ts.employeeTask=? and ts.date=?", employeeTask, timesheetDate);
    }

    public EdsTimeSheet getTimeSheet(Integer employeeID, Integer taskID, Date date) {
        return (EdsTimeSheet) findSingle("SELECT tsh FROM EdsTimeSheet tsh WHERE tsh.employeeID=? AND tsh.employeeTask.task.objectID=? AND tsh.date=?", employeeID, taskID, date);
    }

    public String getSumEmployeeSpentToTaskInterval(EdsEmployeeTask employeeTask, Date from, Date to) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("employeeTask", employeeTask);
        paramMap.put("from", from);
        paramMap.put("to", to);
        List<String> result = findByNamedParams("SELECT sum(ts.timeSpent) FROM EdsTimeSheet ts WHERE ts.employeeTask=:employeeTask AND ts.date BETWEEN :from and :to", paramMap);
        if (result != null && result.size() > 0 && result.get(0) != null) {
            return String.valueOf(result.get(0));
        }
        return null;
    }

    public BigDecimal getApprovedTimeSpentInterval(Integer employeeID, Date from, Date to, Integer projectId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("employeeID", employeeID);
        paramMap.put("approved", "_APPROVE");
        paramMap.put("from", from);
        paramMap.put("to", to);
        if (projectId != null) {
            paramMap.put("projectId", projectId);
        }
        Double result = ((Double) findNativeSingleByNamedParams("SELECT (coalesce(sum(ts.timeSpent*coalesce(ts.wageRate,e.wageRate)), 0)/60) AS totaltime " +
                "FROM " + getCompanyId() + ".timesheet ts " +
                " left join " + getCompanyId() + ".employee e on ts.employeeid =e.id " +
                " left join " + getCompanyId() + ".reference st on ts.statusid =st.id " +
                " WHERE ts.timeSpent>0 and ts.employeeid=:employeeID  AND st.code=:approved AND ts.date >= :from AND ts.date<=:to"
                + (projectId != null ? " and ts.projectID = :projectId" : ""), paramMap));

        return result != null ? new BigDecimal(result) : BigDecimal.ZERO;
    }

    public Long getMonthlyApprovedTimeSpents(EdsUser user) {
        Calendar calendar = new GregorianCalendar(user.getCompany().getTimeZone());
        calendar.setTime(user.getCompany().getCompanyDate());

        Calendar start = new GregorianCalendar();
        start.setTime(calendar.getTime());
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        start.set(Calendar.DAY_OF_MONTH, start.getMinimum(Calendar.DAY_OF_MONTH));

        Date from = start.getTime();
        Date to = calendar.getTime();

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("parentStatus", "_TIME_SHEET_ENTRY_STATUS");
        map.put("approved", "_APPROVE");
        map.put("from", from);
        map.put("to", to);

        return (Long) findSingleByNamedParams("SELECT SUM(t.timeSpent) FROM EdsTimeSheet t WHERE " +
                "t.employeeTask.projectEmployee.employeeDepartment.employee=:user " +
                "AND (t.status.parent.code=:parentStatus AND t.status.code=:approved) " +
                "AND t.date BETWEEN :from AND :to", map);
    }

    @Override
    public EdsProjectEmployeeWageClientRateHistory getProjectEmployeeWageClientRateByDate(Date date, Integer projectEmployeeId) {
        return (EdsProjectEmployeeWageClientRateHistory) findNativeSingle("select x.* from " + getCompanyId() + ".ProjectEmployeeWageClientRateHistory x " +
                "join (select coalesce(max(case '" + date + "'>=x.changeDate when true then x.id else null end), min(case x.changeDate>='" + date + "' when true then x.id else null end)) x " +
                "from " + getCompanyId() + ".ProjectEmployeeWageClientRateHistory x where x.projectemployeeid=" + projectEmployeeId + ") " +
                "tsh on x.id=tsh.x", EdsProjectEmployeeWageClientRateHistory.class);
    }

    /**
     * @param projectID
     * @return [0] - PROJECT ACTUAL COST
     * [1] - PROJECT ACTUAL CLIENT CHARGE
     * [2] - PROJECT ACTUAL TIME SPENT
     * [3] - PROJECT ESTIMATED COST
     * [4] - PROJECT ESTIMATED CLIENT CHARGE
     * [5] - PROJECT ESTIMATED TIME SPENT
     * [6] - PROJECT TIME SPENT
     */

    public Double[] getProjectCostAndTimeSpent(Integer projectID, Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append(" select max(actualCost) actualCost, max(actualClientCharge) actualClientCharge, sum(actualTimeSpent) actualTimeSpent, ");
        sql.append(" max(estimatedCost) estimatedCost, max(estimatedClientCharge) estimatedClientCharge,  max(estimatedTimeSpent) estimatedTimeSpent, sum(hourspent) hourspent ");
        sql.append(" from ");

        sql.append(" (select ");
        sql.append(" sum(cast(et.estimatedtime as double precision)) as estimatedTimeSpent, ");
        sql.append(" sum(pe.wagerate*et.estimatedtime)/ cast(60 as double precision) as estimatedCost, ");
        sql.append(" sum(pe.clientchargerate*et.estimatedtime)/ cast(60 as double precision) as estimatedClientCharge, ");
        sql.append(" t.projectid ");
        sql.append(" from ").append(companyID).append(".employeetask et ");
        sql.append(" left join ").append(companyID).append(".projectemployee pe on pe.id = et.projectemployeeid ");
        sql.append(" left join ").append(companyID).append(".teamEmployee te on te.id = pe.employeeDepartmentId ");
        sql.append(" left join ").append(companyID).append(".employee e on e.id = te.employeeId ");
        sql.append(" left join ").append(companyID).append(".task t on et.taskid = t.id ");
        sql.append(" left join ").append(companyID).append(".project p on t.projectid = p.id ");
        if (employeeID != null)
            sql.append(" where p.id = ").append(projectID).append(" and e.id= ").append(employeeID).append(" and p.isdeleted = false and et.deleted = false and pe.isdeleted is not true and t.deleted = false group by t.projectid) as estimated ");
        else
            sql.append(" where p.id = ").append(projectID).append(" and p.isdeleted = false and et.deleted = false and pe.isdeleted is not true and t.deleted = false group by t.projectid) as estimated ");

        sql.append(" full outer join ");
        sql.append(" (select ");
        sql.append(" case when r1.code='_APPROVE' then sum(cast(ts.timeSpent as double precision)) end actualTimeSpent, ");
        sql.append(" sum(cast(ts.timeSpent as double precision)) hourspent, ");
        sql.append(" sum((case when r1.code='_APPROVE' then ts.wageRate when r2.code = '_TIME_SHEET_ENTRY_STATUS' then ts.wageRate else 0 end)*ts.timespent )/ cast(60 as double precision) as actualCost, ");
        sql.append(" sum((case when r1.code='_APPROVE' then ts.clientchargerate when r2.code = '_TIME_SHEET_ENTRY_STATUS' then ts.clientchargerate else 0 end )*ts.timespent)/ cast(60 as double precision) as actualClientCharge, ");
        sql.append(" projectid ");
        sql.append(" from ").append(companyID).append(".timesheet ts ");
        sql.append(" left join ").append(companyID).append(".reference r1 on r1.id = ts.statusid ");
        sql.append(" left join ").append(companyID).append(".reference r2 on r1.parentid = r2.id ");
        if (employeeID != null) {
            sql.append(" where ts.projectid=").append(projectID).append(" and ts.employeeid=").append(employeeID);
        } else {
            sql.append(" where ts.projectid = ").append(projectID);
        }
        sql.append(" group by ts.projectid, r1.code) as actual on estimated.projectid = actual.projectid ");
        sql.append(" group by estimated.projectid");

        Object[] listResult = (Object[]) findNativeSingle(sql.toString());
        return listResult != null ? Arrays.copyOf(listResult, listResult.length, Double[].class) : null;
    }

    public HashMap<Integer, Double[]> getCostAndTimeSpentOnProjects(String projectIDs) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();

        sql.append(" select estimated.projectid, max(actualCost) actualCost, max(actualClientCharge) actualClientCharge, sum(actualTimeSpent) actualTimeSpent, ");
        sql.append(" max(estimatedCost) estimatedCost, max(estimatedClientCharge) estimatedClientCharge,  max(estimatedTimeSpent) estimatedTimeSpent, sum(hourspent) hourspent ");
        sql.append(" from ");

        sql.append(" (select ");
        sql.append(" sum(cast(et.estimatedtime as double precision)) as estimatedTimeSpent, ");
        sql.append(" sum(pe.wagerate*et.estimatedtime)/ cast(60 as double precision) as estimatedCost, ");
        sql.append(" sum(pe.clientchargerate*et.estimatedtime)/ cast(60 as double precision) as estimatedClientCharge, ");
        sql.append(" t.projectid ");
        sql.append(" from ").append(companyID).append(".employeetask et ");
        sql.append(" left join ").append(companyID).append(".projectemployee pe on pe.id = et.projectemployeeid ");
        sql.append(" left join ").append(companyID).append(".task t on et.taskid = t.id ");
        sql.append(" left join ").append(companyID).append(".project p on t.projectid = p.id ");
        sql.append(" where p.id IN (").append(projectIDs).append(") and p.isdeleted = false and et.deleted = false and pe.isdeleted is not true and t.deleted = false group by t.projectid) as estimated ");

        sql.append(" full outer join ");
        sql.append(" (select ");
        sql.append(" case when r1.code='_APPROVE' then sum(cast(ts.timeSpent as double precision)) end actualTimeSpent, ");
        sql.append(" sum(cast(ts.timeSpent as double precision)) hourspent, ");
        sql.append(" sum((case when r1.code='_APPROVE' then ts.wageRate when r2.code = '_TIME_SHEET_ENTRY_STATUS' then ts.wageRate else 0 end)*ts.timespent )/ cast(60 as double precision) as actualCost, ");
        sql.append(" sum((case when r1.code='_APPROVE' then ts.clientchargerate when r2.code = '_TIME_SHEET_ENTRY_STATUS' then ts.clientchargerate else 0 end )*ts.timespent)/ cast(60 as double precision) as actualClientCharge, ");
        sql.append(" projectid ");
        sql.append(" from ").append(companyID).append(".timesheet ts ");
        sql.append(" left join ").append(companyID).append(".reference r1 on r1.id = ts.statusid ");
        sql.append(" left join ").append(companyID).append(".reference r2 on r1.parentid = r2.id ");
        sql.append(" where ts.projectid IN (").append(projectIDs).append(") ");
        sql.append(" group by ts.projectid, r1.code) as actual on estimated.projectid = actual.projectid ");
        sql.append(" group by estimated.projectid");

        List<Object[]> listResult = findNative(sql.toString());
        HashMap<Integer, Double[]> resultMap = new HashMap<>();
        Double[] costAndTimeSpent;
        for (Object[] resultRow : listResult) {
            costAndTimeSpent = new Double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d};
            costAndTimeSpent[0] = (Double) resultRow[1];
            costAndTimeSpent[1] = (Double) resultRow[2];
            costAndTimeSpent[2] = (Double) resultRow[3];
            costAndTimeSpent[3] = (Double) resultRow[4];
            costAndTimeSpent[4] = (Double) resultRow[5];
            costAndTimeSpent[5] = (Double) resultRow[6];
            costAndTimeSpent[6] = (Double) resultRow[7];
            resultMap.put((Integer) resultRow[0], costAndTimeSpent);
        }
        return resultMap;
    }

    @Override
    public HashMap<Integer, Double> getProjectTimeSpents(String projectIds, String status) {
        String sql = "SELECT tsh.projectID, sum(tsh.timeSpent) FROM " + getCompanyId() + ".timesheet tsh \n" +
                "INNER JOIN " + getCompanyId() + ".reference s ON s.id = tsh.statusId \n" +
                "WHERE s.code = '" + status + "' \n" +
                "AND tsh.projectID IN (" + projectIds + ") \n" +
                "GROUP BY tsh.projectID \n";

        List list = findNative(sql);

        HashMap<Integer, Double> map = new HashMap<>();

        if (list != null && !list.isEmpty()) {
            for (Object item : list) {
                Object[] object = (Object[]) item;
                map.put((Integer) object[0], ((BigInteger) object[1]).doubleValue());
            }
        }
        return map;
    }

    @Override
    public HashMap<Integer, Double> getTaskTimeSpents(String taskIds, String status) {
        String sql = "SELECT tsh.taskID, sum(tsh.timeSpent) FROM " + getCompanyId() + ".timesheet tsh \n" +
                "INNER JOIN " + getCompanyId() + ".reference s ON s.id = tsh.statusId \n" +
                "WHERE s.code = '" + status + "' \n" +
                "AND tsh.taskID IN (" + taskIds + ") \n" +
                "GROUP BY tsh.taskID \n";

        List list = findNative(sql);

        HashMap<Integer, Double> map = new HashMap<>();

        if (list != null && !list.isEmpty()) {
            for (Object item : list) {
                Object[] object = (Object[]) item;
                map.put((Integer) object[0], ((BigInteger) object[1]).doubleValue());
            }
        }
        return map;
    }

    public Double[] getTimeSpentByEmployee(Integer taskID, Integer employeeId) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();

        sql.append(" select sum(actualTimeSpent) actualTimeSpent, max(estimatedTimeSpent) estimatedTimeSpent, sum(hourspent) hourspent ");
        sql.append(" from ");
        sql.append(" (select ");
        sql.append(" sum(cast(et.estimatedtime as double precision)) as estimatedTimeSpent, t.id ");
        sql.append(" from ").append(companyID).append(".employeetask et ");
        sql.append(" left join ").append(companyID).append(".projectemployee pe on pe.id = et.projectemployeeid ");
        sql.append(" left join ").append(companyID).append(".teamEmployee te on te.id = pe.employeeDepartmentId ");
        sql.append(" left join ").append(companyID).append(".employee e on e.id = te.employeeId ");
        sql.append(" left join ").append(companyID).append(".task t on et.taskid = t.id ");
        sql.append(" left join ").append(companyID).append(".project p on t.projectid = p.id ");
        if (employeeId != null) {
            sql.append(" where t.id=").append(taskID).append(" and e.id= ").append(employeeId).append(" and et.deleted = false and t.deleted = false group by t.id) as estimated ");
        } else {
            sql.append(" where t.id=").append(taskID).append(" and et.deleted = false and t.deleted = false group by t.id) as estimated ");
        }

        sql.append(" full outer join ");
        sql.append(" (select ");
        sql.append(" case when r1.code='_APPROVE' then sum(cast(ts.timeSpent as double precision)) end actualTimeSpent, ");
        sql.append(" sum(cast(ts.timeSpent as double precision)) hourspent, ");
        sql.append(" sum((case when r1.code='_APPROVE' then ts.wageRate when r2.code = '_TIME_SHEET_ENTRY_STATUS' then ts.wageRate else 0 end)*ts.timespent )/ cast(60 as double precision) as actualCost, ");
        sql.append(" sum((case when r1.code='_APPROVE' then ts.clientchargerate when r2.code = '_TIME_SHEET_ENTRY_STATUS' then ts.clientchargerate else 0 end )*ts.timespent)/ cast(60 as double precision) as actualClientCharge, ");
        sql.append(" taskid ");
        sql.append(" from ").append(companyID).append(".timesheet ts ");
        sql.append(" left join ").append(companyID).append(".reference r1 on r1.id = ts.statusid ");
        sql.append(" left join ").append(companyID).append(".reference r2 on r1.parentid = r2.id ");
        if (employeeId != null) {
            sql.append(" where ts.taskid=").append(taskID).append(" and ts.employeeid=").append(employeeId);
        } else {
            sql.append(" where ts.taskid=").append(taskID);
        }

        sql.append(" group by ts.taskid, r1.code) as actual on estimated.id = actual.taskid ");
        sql.append(" group by estimated.id");

        Object[] listResult = (Object[]) findNativeSingle(sql.toString());
        return listResult != null ? Arrays.copyOf(listResult, listResult.length, Double[].class) : null;
    }

    public HashMap<String, Double[]> getEmployeeCostAndTimeSpentOnProjects(Integer projectID) {
        String companyID = getCompanyId();
        EdsModule resourcePlanning = moduleManager.getModuleByCode(PermissionConstants.RESOURCE_PLANNING);
        StringBuilder sql = new StringBuilder();

        sql.append(" select employeeid, sum(actualCost) as actualCost, sum(actualClientCharge) as actualClientCharge, sum(actualTimeSpent) as actualTimeSpent, sum(estimatedCost) as estimatedCost, sum(estimatedClientCharge) as estimatedClientCharge");
        sql.append(" from ");

        if (resourcePlanning != null) {
            sql.append(" (select ");
            sql.append(" sum(ts.wageRate*ts.dailyestimatedtime)/cast(60 as double precision) as estimatedCost, ");
            sql.append(" sum(ts.clientchargerate*ts.dailyestimatedtime)/cast(60 as double precision) as estimatedClientCharge, ");
            sql.append(" 0 as actualTimeSpent, ");
            sql.append(" 0 as actualCost, ");
            sql.append(" 0 as actualClientCharge, ");
            sql.append(" employeeid ");
            sql.append(" from ").append(companyID).append(".timesheet ts ");
            sql.append(" left join ").append(companyID).append(".task t on t.id = ts.taskid ");
            sql.append(" left join ").append(companyID).append(".employeetask et on ts.employeetaskid = et.id ");
            sql.append(" where ts.projectid = ").append(projectID).append(" and t.deleted is not true and et.deleted is not true ");
            sql.append(" group by employeeid ");
        } else {
            sql.append(" (select ");
            sql.append(" sum(pe.wagerate*et.estimatedtime)/ cast(60 as double precision) as estimatedCost, ");
            sql.append(" sum(pe.clientchargerate*et.estimatedtime)/ cast(60 as double precision) as estimatedClientCharge, ");
            sql.append(" 0 as actualTimeSpent, ");
            sql.append(" 0 as actualCost, ");
            sql.append(" 0 as actualClientCharge, ");
            sql.append(" te.employeeid ");
            sql.append(" from ").append(companyID).append(".employeetask et ");
            sql.append(" left join ").append(companyID).append(".projectemployee pe on pe.id = et.projectemployeeid ");
            sql.append(" left join ").append(companyID).append(".teamemployee te on te.id = pe.employeedepartmentid ");
            sql.append(" left join ").append(companyID).append(".task t on et.taskid = t.id ");
            sql.append(" left join ").append(companyID).append(".project p on t.projectid = p.id ");
            sql.append(" where p.id = ").append(projectID).append(" and p.isdeleted = false and et.deleted = false and t.deleted = false group by te.employeeid ");
        }

        sql.append(" union ");

        sql.append(" select ");
        sql.append(" 0 as estimatedCost, ");
        sql.append(" 0 as estimatedClientCharge, ");
        sql.append(" sum(cast(ts.timeSpent as double precision)) as actualTimeSpent, ");
        sql.append(" sum(ts.wageRate*ts.timespent)/ cast(60 as double precision) as actualCost, ");
        sql.append(" sum(ts.clientchargerate*ts.timespent)/ cast(60 as double precision) as actualClientCharge, ");
        sql.append(" employeeid ");
        sql.append(" from ").append(companyID).append(".timesheet ts ");
        sql.append(" left join ").append(companyID).append(".task t on t.id = ts.taskid ");
        sql.append(" left join ").append(companyID).append(".reference r1 on r1.id = ts.statusid ");
        sql.append(" left join ").append(companyID).append(".reference r2 on r1.parentid = r2.id ");
        sql.append(" left join ").append(companyID).append(".employeetask et on ts.employeetaskid = et.id ");
        sql.append(" join ").append(companyID).append(".projectemployee pe on et.projectemployeeid = pe.id ");
        sql.append(" left join (select rh.clientchargerate clientChargeRate, rh.wagerate wagerate, ");
        sql.append(" rhdates.ddate tshdate, rhdates.peid projectemployeeid ");
        sql.append(" from (select date(tsh.date) ddate,ph.projectemployeeid peid, ");
        sql.append(" max(date(ph.changedate)) realdate ");
        sql.append(" from ").append(companyID).append(".projectemployeewageclientratehistory ph ");
        sql.append(" join ").append(companyID).append(".projectemployee pte on ph.projectemployeeid=pte.id ");
        sql.append(" join ").append(companyID).append(".project p on pte.projectid=p.id ");
        sql.append(" join ").append(companyID).append(".employeetask etk on pte.id=etk.projectemployeeid ");
        sql.append(" join ").append(companyID).append(".timesheet tsh on etk.id=tsh.employeetaskid and date(tsh.date)>=date(ph.changedate) ");
        sql.append(" where tsh.timespent != 0 and p.isdeleted is not true ");
        sql.append(" and p.id= ").append(projectID).append(" ");
        sql.append(" group by date(tsh.date), ph.projectemployeeid) rhdates ");
        sql.append(" join ").append(companyID).append(".projectemployeewageclientratehistory rh on rhdates.peid=rh.projectemployeeid and date(rhdates.realdate)=date(rh.changedate) ");
        sql.append(" ) cr on pe.id=cr.projectemployeeid and date(ts.date)=date(cr.tshdate) ");
        sql.append(" where ts.projectid = ").append(projectID).append(" and t.deleted is not true ");
        sql.append(" and r1.code = '_APPROVE' ");
        sql.append(" and r2.code = '_TIME_SHEET_ENTRY_STATUS' group by ts.employeeid ");

        sql.append(" union ");
        sql.append(" select ");
        sql.append(" 0 as estimatedCost, ");
        sql.append(" 0 as estimatedClientCharge, ");
        sql.append(" 0 as actualTimeSpent, ");
        sql.append(" sum(pti.total)  as actualCost, ");
        sql.append(" 0 as actualClientCharge, ");
        sql.append(" pti.employee_id as employeeid");
        sql.append(" from ").append(companyID).append(".payslipTableItem pti");
        sql.append(" left join ").append(companyID).append(".reference r3 on pti.status_id = r3.id");
        sql.append(" where (pti.deleted <> true or pti.deleted is null) ");
        sql.append(" and pti.projectid = ").append(projectID).append(" ");
        sql.append(" and r3.code = 'PY_APPROVED' ");
        sql.append(" group by pti.employee_id");
        sql.append(" ) t group by employeeid ");

        List<Object[]> listResult = findNative(sql.toString());
        HashMap<String, Double[]> resultMap = new HashMap<>();
        Double[] costAndTimeSpent;
        for (Object[] resultRow : listResult) {
            costAndTimeSpent = new Double[]{0d, 0d, 0d, 0d, 0d};
            costAndTimeSpent[0] = (Double) resultRow[1];
            costAndTimeSpent[1] = (Double) resultRow[2];
            costAndTimeSpent[2] = (Double) resultRow[3];
            costAndTimeSpent[3] = (Double) resultRow[4];
            costAndTimeSpent[4] = (Double) resultRow[5];
            resultMap.put(resultRow[0] + "/" + projectID, costAndTimeSpent);
        }
        return resultMap;
    }

    public Integer getEstimatedTime(Integer projectEmployeeId, Integer projectId) {
        String companyID = getCompanyId();
        String sql = "select cast(sum(et.estimatedtime) as integer) from " + companyID + ".employeetask et " + "inner join " + companyID + ".task t on t.id=et.taskid where t.projectid = " + projectId + " and projectemployeeid = " + projectEmployeeId +
                " and et.deleted is not true ";
        Integer amount = (Integer) findNativeSingle(sql);
        return amount;
    }

    /**
     * @param taskID
     * @return [0] - TASK ACTUAL COST
     * [1] - TASK ACTUAL CLIENT CHARGE
     * [2] - TASK ACTUAL TIME SPENT
     * [3] - TASK ESTIMATED COST
     * [4] - TASK ESTIMATED CLIENT CHARGE
     * [5] - TASK ESTIMATED TIME SPENT
     * [6] - TASK TIME SPENT
     */

    public Double[] getTaskCostAndTimeSpent(Integer taskID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append(" select max(actualCost) actualCost, max(actualClientCharge) actualClientCharge, sum(actualTimeSpent) actualTimeSpent, ");
        sql.append("max(estimatedCost) estimatedCost, max(estimatedClientCharge) estimatedClientCharge,  max(estimatedTimeSpent) estimatedTimeSpent, sum(hourspent) hourspent ");
        sql.append(" from ");

        sql.append(" (select ");
        sql.append(" sum(cast(et.estimatedtime as double precision)) as estimatedTimeSpent, ");
        sql.append(" sum(pe.wagerate*et.estimatedtime)/ cast(60 as double precision) as estimatedCost, ");
        sql.append(" sum(pe.clientchargerate*et.estimatedtime)/ cast(60 as double precision) as estimatedClientCharge, ");
        sql.append(" t.id ");
        sql.append(" from ").append(companyID).append(".employeetask et ");
        sql.append(" left join ").append(companyID).append(".projectemployee pe on pe.id = et.projectemployeeid ");
        sql.append(" left join ").append(companyID).append(".task t on et.taskid = t.id ");
        sql.append(" left join ").append(companyID).append(".project p on t.projectid = p.id ");
        sql.append(" where t.id = ").append(taskID).append(" and et.deleted = false and t.deleted = false group by t.id) as estimated ");

        sql.append(" full outer join ");
        sql.append(" (select ");
        sql.append(" case when r1.code='_APPROVE' then sum(cast(ts.timeSpent as double precision)) end actualTimeSpent, ");
        sql.append(" sum(cast(ts.timeSpent as double precision)) hourspent, ");
        sql.append(" sum((case when r1.code='_APPROVE' then ts.wageRate when r2.code = '_TIME_SHEET_ENTRY_STATUS' then ts.wageRate else 0 end)*ts.timespent)/ cast(60 as double precision) as actualCost, ");
        sql.append(" sum((case when r1.code='_APPROVE' then ts.clientchargerate when r2.code = '_TIME_SHEET_ENTRY_STATUS' then ts.clientchargerate else 0 end)*ts.timespent)/ cast(60 as double precision) as actualClientCharge, ");
        sql.append(" taskid ");
        sql.append(" from ").append(companyID).append(".timesheet ts ");
        sql.append(" left join ").append(companyID).append(".reference r1 on r1.id = ts.statusid ");
        sql.append(" left join ").append(companyID).append(".reference r2 on r1.parentid = r2.id ");
        sql.append(" where ts.taskid = ").append(taskID);
        sql.append(" group by ts.taskid, r1.code) as actual on estimated.id = actual.taskid ");
        sql.append(" group by estimated.id");

        Object[] listResult = (Object[]) findNativeSingle(sql.toString());
        return listResult != null ? Arrays.copyOf(listResult, listResult.length, Double[].class) : null;
    }

    public HashMap<Integer, Double[]> getCostAndTimeSpentOnTasks(String taskIDs) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append(" select estimated.id, max(actualCost) actualCost, max(actualClientCharge) actualClientCharge, sum(actualTimeSpent) actualTimeSpent, ");
        sql.append("max(estimatedCost) estimatedCost, max(estimatedClientCharge) estimatedClientCharge,  max(estimatedTimeSpent) estimatedTimeSpent, sum(hourspent) hourspent ");
        sql.append(" from ");

        sql.append(" (select ");
        sql.append(" sum(cast(et.estimatedtime as double precision)) as estimatedTimeSpent, ");
        sql.append(" sum(pe.wagerate*et.estimatedtime)/ cast(60 as double precision) as estimatedCost, ");
        sql.append(" sum(pe.clientchargerate*et.estimatedtime)/ cast(60 as double precision) as estimatedClientCharge, ");
        sql.append(" t.id ");
        sql.append(" from ").append(companyID).append(".employeetask et ");
        sql.append(" left join ").append(companyID).append(".projectemployee pe on pe.id = et.projectemployeeid ");
        sql.append(" left join ").append(companyID).append(".task t on et.taskid = t.id ");
        sql.append(" left join ").append(companyID).append(".project p on t.projectid = p.id ");
        sql.append(" where t.id IN (").append(taskIDs).append(") and et.deleted = false and t.deleted = false group by t.id) as estimated ");

        sql.append(" full outer join ");
        sql.append(" (select ");
        sql.append(" case when r1.code='_APPROVE' then sum(cast(ts.timeSpent as double precision)) end actualTimeSpent, ");
        sql.append(" sum(cast(ts.timeSpent as double precision)) hourspent, ");
        sql.append(" sum((case when r1.code='_APPROVE' then ts.wageRate when r2.code = '_TIME_SHEET_ENTRY_STATUS' then ts.wageRate else 0 end)*ts.timespent)/ cast(60 as double precision) as actualCost, ");
        sql.append(" sum((case when r1.code='_APPROVE' then ts.clientchargerate when r2.code = '_TIME_SHEET_ENTRY_STATUS' then ts.clientchargerate else 0 end)*ts.timespent)/ cast(60 as double precision) as actualClientCharge, ");
        sql.append(" taskid ");
        sql.append(" from ").append(companyID).append(".timesheet ts ");
        sql.append(" left join ").append(companyID).append(".reference r1 on r1.id = ts.statusid ");
        sql.append(" left join ").append(companyID).append(".reference r2 on r1.parentid = r2.id ");
        sql.append(" where ts.timespent>0 and ts.taskid IN (").append(taskIDs).append(") ");
        sql.append(" group by ts.taskid, r1.code) as actual on estimated.id = actual.taskid ");
        sql.append(" group by estimated.id");

        List<Object[]> listResult = findNative(sql.toString());
        HashMap<Integer, Double[]> resultMap = new HashMap<>();
        Double[] costAndTimeSpent;
        for (Object[] resultRow : listResult) {
            costAndTimeSpent = new Double[]{0d, 0d, 0d, 0d, 0d, 0d, 0d};
            costAndTimeSpent[0] = (Double) resultRow[1];
            costAndTimeSpent[1] = (Double) resultRow[2];
            costAndTimeSpent[2] = (Double) resultRow[3];
            costAndTimeSpent[3] = (Double) resultRow[4];
            costAndTimeSpent[4] = (Double) resultRow[5];
            costAndTimeSpent[5] = (Double) resultRow[6];
            costAndTimeSpent[6] = (Double) resultRow[7];
            resultMap.put((Integer) resultRow[0], costAndTimeSpent);
        }
        return resultMap;
    }

    public void deletePayslipIDsFromTimeSheet(Integer payslipID, Integer employeeID) {
        update("UPDATE  EdsTimeSheet tsh SET  tsh.payslipID = null WHERE tsh.payslipID = ?", payslipID);
    }

    public List<EdsTimeSheet> getTimeSheetsByPayslipIDs(Integer payslipID, Integer employeeID) {
        return find("SELECT tsh FROM EdsTimeSheet tsh WHERE tsh.payslipID=? AND  tsh.employeeID=?", payslipID, employeeID);
    }

    public Integer getProjectApprovedTimesheetHours(Integer projectID) {
        String companyID = getCompanyId();
        BigInteger timeSpent = (BigInteger) findNativeSingle("select sum(tsh.timeSpent) from " + companyID + ".timesheet tsh left join " + companyID + ".project p on tsh.projectID=p.id left join " + companyID + ".reference r on tsh.statusId = r.id " +
                "where p.id=" + projectID + " and r.code='" + EdsTimeSheet._APPROVE + "'");
        return timeSpent == null ? 0 : timeSpent.intValue();
    }

    @Override
    public Integer getTimesheetCount(EdsEmployeeTask employeeTask, Date from, Date to) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("employeeTask", employeeTask);
        paramMap.put("from", from);
        paramMap.put("to", to);
        List<Long> result = findByNamedParams("SELECT COUNT(tsh) FROM EdsTimeSheet tsh " +
                "WHERE tsh.employeeTask = :employeeTask " +
                "and tsh.date between :from and :to ", paramMap);

        if (result != null && result.size() > 0 && result.get(0) != null) {
            Long aLong = result.get(0);
            return aLong.intValue();
        }
        return null;
    }

    @Override
    public void updateDailyEstimatedTime(Integer id, Date start, Date end, Integer dailyEstimate) {
        update("UPDATE EdsTimeSheet SET dailyestimatedtime = ? WHERE employeetaskid = ? and date between ? and ?", dailyEstimate, id, start, end);
    }

    @Override
    public void updateDailyEstimatedTime(Integer employeeTaskid) {
        update("UPDATE EdsTimeSheet SET dailyestimatedtime = null WHERE employeetaskid = ?", employeeTaskid);
    }

    @Override
    public ArrayList<Date> getTimesheetOldDates(Integer employeeId, EdsEmployeeTask edsEmployeeTask) {
        return (ArrayList<Date>) find("SELECT tsh.date FROM EdsTimeSheet tsh WHERE tsh.employeeID=? AND  tsh.employeeTask=?", employeeId, edsEmployeeTask);
    }

    @Override
    public void updateTimeSheetOldDataWithDailyEstimatedTime(Integer employeeTaskId, Integer dailyEstimatedTime, List<Date> dates, boolean fromResourceUtil) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("dailyEstimatedTime", dailyEstimatedTime);
        paramMap.put("employeeTaskId", employeeTaskId);
        paramMap.put("dates", dates);


        String sql = "UPDATE EdsTimeSheet SET dailyestimatedtime = :dailyEstimatedTime " +
                "WHERE employeetaskid = :employeeTaskId and date in :dates";

        if (!fromResourceUtil) {
            sql += " and dailyestimatedtime is null";
        }

        updateByNamedParams(sql, paramMap);
    }

    @Override
    public void updateWageRate(Integer empID, Double wageRate, Date applyFrom) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("wageRate", wageRate);
        paramMap.put("employeeID", empID);
        paramMap.put("applyFrom", applyFrom);


        String sql = "UPDATE EdsTimeSheet SET wageRate = :wageRate " +
                " WHERE employeeid = :employeeID and timespent > 0 and date >=:applyFrom";

        updateByNamedParams(sql, paramMap);
        sql = "update EdsProjectEmployee pe set pe.wageRate=:wageRate where pe.deleted = false and pe.creationdate >= :applyFrom and pe.id in (select p.objectID from EdsProjectEmployee p where p.employeeDepartment.employee.objectID=:employeeID)";
        updateByNamedParams(sql, paramMap);

    }

    @Override
    public void updateClientChargeRate(Integer empID, Double clientChargeRate, Date applyFrom) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("clientChargeRate", clientChargeRate);
        paramMap.put("employeeID", empID);
        paramMap.put("applyFrom", applyFrom);


        String sql = "UPDATE EdsTimeSheet SET clientChargeRate = :clientChargeRate " +
                " WHERE employeeid = :employeeID and timespent > 0 and date >=:applyFrom";

        updateByNamedParams(sql, paramMap);

        sql = "update EdsProjectEmployee pe set pe.clientChargeRate=:clientChargeRate where pe.deleted = false and pe.creationdate >= :applyFrom and pe.id in (select p.objectID from EdsProjectEmployee p where p.employeeDepartment.employee.objectID=:employeeID)";
        updateByNamedParams(sql, paramMap);

    }

    @Override
    public List<TaskTimeEntriesItem> getProjectTimeEntiries(Integer projectID) {
        boolean doNotShowDeletedTaskInInvoice = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DO_NOT_SHOW_DELETED_TASKS_IN_INVOICE);
        String taskDiscountField = genericSettingsManager.getValueByKey(GenericSettingsEnum.TASK_DISCOUNT_FIELD);

        StringBuilder sql = new StringBuilder();
        sql.append("select * from (");
        sql.append("select tsh.id objectID, tsh.date entryDate, u.firstname||' '||u.lastname as emloyee, u.id as employeeId, t.name as taskName, tsh.timeSpent, (case when t.taskAmount is not null and t.taskAmount > 0 then t.taskAmount else clientchargerate end) as rate, (case when tsh.usedInInvoice then 'Invoiced' else coalesce(s.name,'Draft') end) as status," +
                " t.id taskId, inv.id as invoiceID, inv.number as invoiceNumber, t.billable, " +
                " (case when t.taskAmount is not null and t.taskAmount > 0 then true else false end) as fixed ");
        if (taskDiscountField != null && !taskDiscountField.isEmpty()) {
            sql.append(", tc." + taskDiscountField + " as discount \n");
        }
        sql.append("from ").append(getCompanyId()).append(".timesheet  tsh \n");
        sql.append("inner join ").append(getCompanyId()).append(".task t on t.id = tsh.taskid \n");
        sql.append("inner join ").append(getCompanyId()).append(".myuser u on u.id = tsh.employeeid \n");
        sql.append("left join ").append(getCompanyId()).append(".reference s on s.id = tsh.statusid \n");
        sql.append("left join ").append(getCompanyId()).append(".invoiceitem ii on ii.id = tsh.invoiceItemID \n");
        sql.append("left join ").append(getCompanyId()).append(".invoice inv on inv.id = ii.invoice_id \n");
        sql.append("left join ").append(getCompanyId()).append(".taskcustomfields tc on tc.id = t.taskcustomfieldsid \n");
        sql.append("where tsh.timespent > 0 and tsh.projectID = " + projectID);
        sql.append(doNotShowDeletedTaskInInvoice ? " and t.deleted is not true " : "");

        /*sql.append("union all \n");

        sql.append("select et.id objectID, t.startDate entryDate, u.firstname||' '||u.lastname as emloyee, u.id as employeeId, t.name as taskName, 0 timespent, et.taskAmount as rate, 'Approve' status, " +
                " t.id taskId, null invoiceID, '' invoiceNumber, t.billable, true as fixed \n");
        sql.append(" from ").append(getCompanyId()).append(".employeetask et \n");
        sql.append(" join ").append(getCompanyId()).append(".task t on t.id = et.taskId \n");
        sql.append(" join ").append(getCompanyId()).append(".projectemployee pe on pe.id = et.projectEmployeeId \n");
        sql.append(" join ").append(getCompanyId()).append(".teamEmployee te on te.id = pe.employeeDepartmentId \n");
        sql.append(" join ").append(getCompanyId()).append(".myuser u on u.id = te.employeeid \n");
        sql.append("where et.taskAmount > 0 and pe.projectid = " + projectID + " \n");
        sql.append(" and et.deleted is not true and t.deleted is not true \n");*/
        sql.append(") t order by entryDate desc \n");

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(TaskTimeEntriesItem.class));
    }

    @Override
    public boolean isTaskUsedInInvoice(Integer taskID) {
        String sql = "select count(tsh.id) from " + getCompanyId() + ".timesheet tsh \n" +
                "where tsh.invoiceItemID is not null and tsh.taskID = " + taskID;

        BigInteger count = (BigInteger) findNativeSingle(sql);
        return count != null && count.intValue() > 0;
    }

    public List<EdsTimeSheet> getTimesheetForTimeEntries(ListingFilterParameter fp) {
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        sql.append("select * from " + companyId + ".timesheet tsh \n");
        sql.append("where tsh.employeetaskid in ( \n");
        sql.append("select et.id from " + companyId + ".employeetask et \n");
        sql.append("inner join " + companyId + ".task t on et.taskid=t.id \n");
        sql.append("inner join " + companyId + ".projectemployee pe on et.projectemployeeid=pe.id \n");
        sql.append("inner join " + companyId + ".teamEmployee te on pe.employeeDepartmentId=te.id \n");
        sql.append("inner join " + companyId + ".myuser mu on te.employeeId=mu.id \n");
        sql.append("inner join " + companyId + ".employee em on te.employeeId=em.id \n");
        sql.append("left join " + companyId + ".employeeprofile empr on em.profileid=empr.id \n");
        sql.append("where t.id=").append(fp.getTaskID()).append(" ");
        if (fp.getEmployeeId() != null) {
            sql.append("and mu.id=").append(fp.getEmployeeId()).append(" \n");
        }
        if (fp.getSearchKey() != null) {
            sql.append("and (lower(mu.firstName) like '%").append(fp.getSearchKey()).append("%' \n");
            sql.append("or lower(mu.lastName) like '%").append(fp.getSearchKey()).append("%' \n");
            sql.append("or lower(empr.employeeCode) like '%").append(fp.getSearchKey()).append("%' )\n");
        }
        sql.append(") \n");
        sql.append("and tsh.taskid=").append(fp.getTaskID()).append(" \n");
        sql.append("and (tsh.timeSpent is not null and tsh.timeSpent>0) \n");
        if (fp.getUserID() != null) {
            sql.append("and tsh.employeeid=").append(fp.getUserID()).append(" \n");
        }
        if (fp.getStartDate() != null) {
            sql.append("and tsh.date >= '").append(formatter.format(fp.getStartDate())).append("' \n");
        }
        if (fp.getEndDate() != null) {
            sql.append("and tsh.date <= '").append(formatter.format(fp.getEndDate())).append("' \n");
        }
        sql.append("order by tsh.date desc ");

        return findNative(sql.toString(), EdsTimeSheet.class);
    }

    @Override
    public List<EdsTimeSheet> getDailyTimesheets(Date startDate, Date endDate) {
        String companyId = getCompanyId();
        String sql = "select t.* from " + companyId + ".timesheet t" +
                "  where t.employeetaskId is not null" +
                "      and (t.timeSpent is not null and t.timeSpent > 0)" +
                "      and t.date >=:startDate" +
                "      and t.date <=:endDate";
        return this.slaveEntityManager.createNativeQuery(sql, EdsTimeSheet.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

//    @Override
//    public List<EdsAttendanceRawData> getWorkedHoursForPayroll(ListingFilterParameter fp) {
//        String sql = "select a.* from " + getCompanyId() + ".attendancerawdata a" +
//                "  where a.employeeid = :employeeId" +
//                "  and a.date between :startDate and :endDate";
//        return this.entityManager.createNativeQuery(sql, EdsAttendanceRawData.class)
//                .setParameter("employeeId", fp.getEmployeeId())
//                .setParameter("startDate", fp.getStartDate())
//                .setParameter("endDate", fp.getEndDate())
//                .getResultList();
//    }

    @Override
    public List<Object[]> getTimesheetForWeeklyRate(ListingFilterParameter fp) {
        String sql = "select " +
                "sum(case when regularovertimehours <= regulartimeslot then regularovertimehours else regulartimeslot end) workedhours, " +
                "sum(case when regularovertimehours > regulartimeslot then regularovertimehours - regulartimeslot else 0 end) regularovertime, " +
                "sum(holidayovertimehours) holidayovertime, " +
                "sum(dayoffovertimehours + leaveovertimehours) dayoffovertime " +
                "from (select to_char(a.date, 'week') week, " +
                "sum(case when a.dayoff is not true and a.holiday is not true and a.leave = 0 then " + (fp.isBasedOnTimesheet() ? " coalesce(t.timespent,0) " : "(SELECT EXTRACT(epoch from (t.endDate - t.startDate))/60)") + " else 0 end) regularovertimehours, " +
                "sum(case when a.leave > 0 then " + (fp.isBasedOnTimesheet() ? " coalesce(t.timespent,0) " : "(SELECT EXTRACT(epoch from (t.endDate - t.startDate))/60)") + " else 0 end) leaveovertimehours, " +
                "sum(case when a.dayoff is not true and a.holiday is not true then a.timeslot else 0 end) regulartimeslot, " +
                "sum(case when a.holiday is true and a.dayoff is true then " + (fp.isBasedOnTimesheet() ? " coalesce(t.timespent,0) " : "(SELECT EXTRACT(epoch from (t.endDate - t.startDate))/60)") + " else 0 end) holidayovertimehours, " +
                "sum(case when a.dayoff is true then " + (fp.isBasedOnTimesheet() ? " coalesce(t.timespent,0) " : "(SELECT EXTRACT(epoch from (t.endDate - t.startDate))/60)") + " else 0 end) dayoffovertimehours " +
                "  from " + getCompanyId() + ".attendancerawdata a" +
                (fp.isBasedOnTimesheet() ? "  left join " + getCompanyId() + ".timesheet t on a.date = t.date and a.employeeid = t.employeeid " : "  left join " + getCompanyId() + ".timetrack t on to_char(a.date, 'yyyy-MM-dd') = to_char(t.startDate, 'yyyy-MM-dd')  and a.employeeid = t.employeeid ") +
                "left join " + getCompanyId() + ".reference r on t.statusid = r.id" +
                "  where a.employeeid = " + fp.getEmployeeId() +
                "  and a.date between '" + fp.getStartDate() + "' and '" + fp.getEndDate() +
                (fp.isBasedOnTimesheet() ? "'  and (r.code = '_APPROVE' or r.code is null)" : "'  and (r.code = 'AVAILABLE' or r.code is null)") +
                " group by week) wd ";
        return (List<Object[]>) findNative(sql);
    }

    @Override
    public List<EdsAttendanceRawData> getHoursForPayrun(ListingFilterParameter fp) {
        String sql = "select " + (fp.isBasedOnTimesheet() ? "coalesce(sum(coalesce(t.timespent,0)),0) " : "coalesce((SELECT EXTRACT(epoch from (max(t.endDate) - min(t.startDate)))/60),0) ") + " as timesheet, a.* from " +
                getCompanyId() + ".attendancerawdata a left join " + getCompanyId() +
                (fp.isBasedOnTimesheet() ? ".timesheet t on a.date = t.date " :
                        ".timetrack t on to_char(a.date, 'yyyy-MM-dd') = to_char(t.startDate, 'yyyy-MM-dd') ") +
                "left join " + getCompanyId() + ".reference r on t.statusid = r.id " +
                " where a.date between '" + fp.getStartDate() + "' and '" + fp.getEndDate() +
                "' and t.employeeId = " + fp.getEmployeeId() + " and a.employeeId = " + fp.getEmployeeId() +
                (fp.isBasedOnTimesheet() ? " and r.code = '_APPROVE' group by a.id" :
                        " and r.code = 'AVAILABLE' group by a.id");

        return findNative(sql, EdsAttendanceRawData.class);
    }

    @Override
    public BigDecimal getTimeslotHours(ListingFilterParameter fp) {
        Integer result = (Integer) findNativeSingle("select cast(coalesce(sum(timeslot),0) as integer)  from " + getCompanyId() + ".attendancerawdata " +
                "where employeeid=" + fp.getEmployeeId() + " and date between '" + fp.getStartDate() + "' and '" + fp.getEndDate() + "' and holiday is not true");

        return result != null ? new BigDecimal(result) : BigDecimal.ZERO;
    }

    @Override
    public List<EdsTimeSheet> getProjectTimsheets(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select tsh.* from ").append(getCompanyId()).append(".timesheet tsh ")
                .append("where tsh.projectid = ").append(fp.getProjectId());
        if (fp.getEmployeeId() != null) {
            sql.append(" and tsh.employeeid = ").append(fp.getEmployeeId());
        }
        sql.append(" and tsh.date between '")
                .append(fp.getStartDate()).append("' and '").append(fp.getEndDate()).append("' and tsh.statusid is null");
        if (!ServerUtils.isNullOrEmpty(fp.getObjectsIds())) {
            sql.append(" and tsh.id in (").append(fp.getObjectsIds()).append(") ");

        }
        sql.append(" order by tsh.entrydate desc");
        return findNative(sql.toString(), EdsTimeSheet.class);
    }
}
