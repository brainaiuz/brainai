package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.task.client.rpc.EstimateTimeSpentItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.AttendanceTO;
import com.edatasite.workforce.rest.base.to.EmployeeTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.TaskMiniTO;
import com.edatasite.workforce.rest.base.to.TimesheetEntryTO;
import com.edatasite.workforce.rest.base.to.TimesheetRowItemTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository("employeeTaskManager")
public class EmployeeTaskManagerImpl extends BaseManager<EdsEmployeeTask> implements EmployeeTaskManager, Constants {

    @Autowired
    private GenericSettingsManager genericSettingsManager;


    public EmployeeTaskManagerImpl() {
        super(EdsEmployeeTask.class);
    }

    @SuppressWarnings("unchecked")
    public List<EdsEmployeeTask> getTaskListForMobile(EdsEmployee employee, Date date, Integer projectID) {
        HashMap<String, Object> params = new HashMap<>();
        StringBuilder hql = new StringBuilder();
        hql.append("from EdsEmployeeTask et where et.projectEmployee.employeeDepartment.employee = :employee ");
        hql.append("and et.projectEmployee.deleted <> true and et.task.deleted <> true ");

        if (projectID != null) {
            hql.append("and et.projectEmployee.project.objectID = :projectID");
            params.put("projectID", projectID);
        }

        hql.append("and (et.status.code = :inProgress or et.status.code = :notStarted or et.status.code = :waitingFor ");
        hql.append("or (et.status.isSystemReference<>true and et.status.deleted<>true)");
        hql.append(")");
        hql.append("and ((");
        hql.append("(et.status.code = :inProgress or et.status.code = :notStarted or et.status.code = :waitingFor ");
        hql.append("or (et.status.isSystemReference<>true and et.status.deleted<>true)");
        hql.append(")");

        hql.append("or ((et.actualStartDate is null or et.actualEndDate is null ");
        hql.append("or (et.actualStartDate <= :date and et.actualEndDate >= :date)) and ");
        hql.append("et.task.startDate <= :date and et.task.dueDate >= :date)))");
        hql.append(" and et.task.startDate <= :date ");
        hql.append(" and et.deleted <> true order by et.task.id desc");

        params.put("employee", employee);
        params.put("date", date);
        params.put("inProgress", EdsTask.IN_PROGRESS);
        params.put("notStarted", EdsTask.NOT_STARTED);
        params.put("waitingFor", EdsTask.WAITING_FOR_SOMEONE_ELSE);

        return findByNamedParams(hql.toString(), params);
    }

    public List<EdsEmployeeTask> listDueTasks(EdsEmployee employee, Date startOfWeek, Date endOfWeek, ListingFilterParameter fp) {
        return listDueTasks(employee, startOfWeek, endOfWeek, null, fp);
    }

    @Override
    public List<EdsEmployeeTask> listDueTasks(EdsEmployee employee, Date startOfWeek, Date endOfWeek, LinkedHashMap<String, String> projectTasks, ListingFilterParameter fp) {
        Map<String, Object> paramMap = new HashMap<>();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        if (fp.getTaskStatusId() == null) {
            fp.setTaskStatusId(ALL_DUE_TASKS);
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select et from EdsEmployeeTask et join fetch et.projectEmployee pe join fetch et.task ta join fetch ta.project pr left join fetch ta.edsIssue iss left join fetch pr.client cl left join fetch pr.clients cls join fetch et.status st left join fetch ta.parent tap where ");
        sql.append("pe.employeeDepartment.employee=:employee and pe.deleted<>true and ta.deleted<>true ");

        if (fp.getClientId() != null) {
            sql.append("and (cl.objectID=:pClientFilter or cls.objectID=:pClientFilter) ");
            paramMap.put("pClientFilter", fp.getClientId());
        }

        if (fp.getProjectId() != null) {
            sql.append("and pr.objectID=:pProjectFilter ");
            paramMap.put("pProjectFilter", fp.getProjectId());
        }
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
                    sql.append("pr.objectID=").append(part[0]);
                    needAndStr = true;
                }
                if (!part[1].equals("0")) {
                    if (needAndStr) {
                        sql.append(andStr);
                    }
                    sql.append("ta.objectID=").append(part[1]);
                }
                sql.append(") ");
            }
            sql.append(") ");
        }

        if (fp.getWorkstreamID() != null && fp.getWorkstreamName() != null) {
            sql.append("and tap.name=:parentws and tap.deleted <> true ");
            paramMap.put("parentws", fp.getWorkstreamName());
        }

        if (fp.getTaskStatusId() != null) {
            if (fp.getTaskStatusId().equals(ALL_DUE_TASKS)) {
                sql.append("and ((");
                sql.append("(st.code=:inProgress or st.code=:notStarted or st.code=:waitingFor ");
                sql.append("or (st.isSystemReference<>true and st.deleted<>true)");
                sql.append(")");
            } else {
                sql.append("(st.objectID=:pStatus) ");
                paramMap.put("pStatus", fp.getTaskStatusId());
            }
        }
        if (fp.isShowCompletedTasks()) {
            sql.append(" or (et.completedDate>=:startOfWeek))) ");
            paramMap.put("startOfWeek", startOfWeek);
        } else {
            sql.append(" ))");
        }
        sql.append(" and ( ta.startDate<=:endOfWeek or ");
        sql.append(" st.code=:inProgress ) ");
        if (fp.isDoNotIncludeTasksFromToDoList()) {
            sql.append(" and ta.showInTimesheet = true ");
        }
        //issue enable timeSheet
        if (!fp.isOnlyIssueTasks()) {
            sql.append(" and ((ta.isIssue is null or ta.isIssue=false) or (ta.isIssue is true and iss.enableTimesheet is true)) ");
        } else {
            sql.append(" and (ta.isIssue is true and iss.enableTimesheet is true) ");
        }
        sql.append(" and (et.deleted <> true or et.deleted IS NULL) order by ta.id desc");

        if (fp.getTaskStatusId() != null) {
            if (fp.getTaskStatusId().equals(ALL_DUE_TASKS)) {
                paramMap.put("inProgress", EdsTask.IN_PROGRESS);
                paramMap.put("notStarted", EdsTask.NOT_STARTED);
                paramMap.put("waitingFor", EdsTask.WAITING_FOR_SOMEONE_ELSE);
            }
        }
        paramMap.put("employee", employee);
        paramMap.put("endOfWeek", endOfWeek);
        return findByNamedParams(sql.toString(), paramMap);
    }

    public List<EdsEmployeeTask> listTimesheetFilterData(EdsEmployee employee, Date startOfWeek, Date endOfWeek) {
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select et from EdsEmployeeTask et join fetch et.projectEmployee pe join fetch et.task ta " +
                "join fetch ta.project pr left join fetch pr.client cl join fetch et.status st left join fetch st.parent pa where ");
        sql.append("pe.employeeDepartment.employee=:employee and pe.deleted<>true and ta.deleted<>true ");
        sql.append("and (st.code=:inProgress or st.code=:notStarted or st.code=:waitingFor ");
        sql.append("or (st.isSystemReference<>true and st.deleted<>true)");
        sql.append(")");
        sql.append("and ((");
        sql.append("(st.code=:inProgress or st.code=:notStarted or st.code=:waitingFor ");
        sql.append("or (st.isSystemReference<>true and st.deleted<>true)");
        sql.append(")");
        sql.append("or ((et.actualStartDate is null or et.actualEndDate is null ");
        sql.append("or (et.actualStartDate<=:endOfWeek and et.actualEndDate>=:startOfWeek)) and ");
        sql.append("et.task.startDate<=:endOfWeek and et.task.dueDate>=:startOfWeek)))");
        sql.append(" and et.task.startDate<=:endOfWeek ");
        sql.append(" and (et.deleted <> false or et.deleted IS NULL) order by et.task.id desc");

        paramMap.put("inProgress", EdsTask.IN_PROGRESS);
        paramMap.put("notStarted", EdsTask.NOT_STARTED);
        paramMap.put("waitingFor", EdsTask.WAITING_FOR_SOMEONE_ELSE);
        paramMap.put("employee", employee);
        paramMap.put("startOfWeek", startOfWeek);
        paramMap.put("endOfWeek", endOfWeek);
        return findByNamedParams(sql.toString(), paramMap);
    }

    public List<EdsEmployeeTask> listDueTasksForRecurringReminders(EdsEmployee employee, Date startOfWeek, Date endOfWeek) {
        Map<String, Object> paramMap = new HashMap<>();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setTaskStatusId(ALL_DUE_TASKS);

        StringBuilder sql = new StringBuilder();
        sql.append("select et from EdsEmployeeTask et join fetch et.projectEmployee join fetch et.task join fetch et.task.project left join fetch et.task.project.client join fetch et.status where ");
        sql.append("et.projectEmployee.employeeDepartment.employee=:employee and et.projectEmployee.deleted<>true and et.task.deleted<>true ");
        sql.append("(et.status.code=:inProgress or et.status.code=:notStarted or et.status.code=:waitingFor) ");
        sql.append("or ((et.actualStartDate is null or et.actualEndDate is null ");
        sql.append("or (et.actualStartDate<=:endOfWeek and et.actualEndDate>=:startOfWeek)) and ");
        sql.append("et.task.startDate<=:endOfWeek and et.task.dueDate>=:startOfWeek)))");
        sql.append(" and et.task.startDate<=:endOfWeek ");
        sql.append(" and (et.deleted <> true or et.deleted IS NULL) group by et.id order by et.task.id desc");

        if (fp.getTaskStatusId() != null) {
            if (fp.getTaskStatusId().equals(ALL_DUE_TASKS)) {
                paramMap.put("inProgress", EdsTask.IN_PROGRESS);
                paramMap.put("notStarted", EdsTask.NOT_STARTED);
                paramMap.put("waitingFor", EdsTask.WAITING_FOR_SOMEONE_ELSE);
            }
        }
        paramMap.put("employee", employee);
        paramMap.put("startOfWeek", startOfWeek);
        paramMap.put("endOfWeek", endOfWeek);
        return findByNamedParams(sql.toString(), paramMap);
    }

    public List<EdsEmployeeTask> getEmployeeTasks(Integer employeeID, EdsTask task) {
        return find("SELECT et FROM EdsEmployeeTask et WHERE et.deleted<>true " +
                (employeeID != null ? "et.projectEmployee.employeeDepartment.employee.objectID= " + employeeID : "") +
                "AND et.task=?", task);
    }

    public void deleteEmployeeTask(Integer employeeTaskID) {
        update("update EdsEmployeeTask emTask set emTask.deleted='true' " +
                "where emTask.objectID=? and emTask.deleted<>true", employeeTaskID);
    }

    public void deleteEmployeeTasksByEmployee(Integer employeeID) {
        update("update EdsEmployeeTask emTask set emTask.deleted='true' " +
                "where emTask.projectEmployee.objectID in (select pe.objectID from EdsProjectEmployee pe where pe.project.status.code!='" + EdsProject.COMPLETED + "' and pe.employeeDepartment.employee.objectID = ?) and emTask.deleted<>true", employeeID);
    }


    public List<EdsEmployeeTask> sort(Collection<EdsEmployeeTask> assignments) {
        List<EdsEmployeeTask> result = new LinkedList<>(assignments);
        result.sort(Comparator.comparing(EdsObject::getName));
        return result;
    }

    /**
     * Will return non-deleted employee task for the given task and employee Ids
     *
     * @param taskId
     * @param projectEmployeeId
     * @return
     */
    public EdsEmployeeTask getEmployeeTask(Integer taskId, Integer projectEmployeeId) {
        return getEmployeeTask(taskId, projectEmployeeId, false);
    }

    /**
     * Will return  non-deleted or deleted employee task for the given task and employee Ids
     *
     * @param taskId
     * @param employeeId
     * @param includingDeleted - indicates whether deleted employeeTask record should be retrieved
     * @return
     */
    public EdsEmployeeTask getEmployeeTask(Integer taskId, Integer employeeId, boolean includingDeleted) {
        Map<String, Object> map = new HashMap<>();
        map.put("empID", employeeId);
        map.put("tID", taskId);
        return (EdsEmployeeTask) findSingleByNamedParams("select et from EdsEmployeeTask et where " +
                "et.projectEmployee.employeeDepartment.employee.objectID =:empID" +
                " and et.task.objectID =:tID" +
                (includingDeleted ? " and et.deleted<>true" : ""), map);
    }

    public List<EdsEmployeeTask> getProjectEmployeeTasks(Integer employeeId, Integer projectID) {
        Map<String, Object> map = new HashMap<>();
        map.put("pEmployeeID", employeeId);
        map.put("pProjectID", projectID);
        map.put("cancelled", EdsTask.CANCELLED);
        return (List<EdsEmployeeTask>) findByNamedParams("from EdsEmployeeTask et where et.projectEmployee.employeeDepartment.employee.objectID =:pEmployeeID " +
                "and et.projectEmployee.project.objectID=:pProjectID" +
                " and et.status.code != :cancelled and et.task.deleted <> true and et.deleted <> true ", map);

    }

    public List<EdsTimeSheet> getAllTimeSpent(Integer projectId){
        Map<String, Object> map = new HashMap<>();
        map.put("pProjectID", projectId);
        return(List<EdsTimeSheet>) findByNamedParams("FROM EdsTimeSheet ts where ts.projectID =:pProjectID", map);

    }

    public List<EdsEmployeeTask> getEmployeeInvolvedTasks(EdsEmployee employee) {
        return find("from EdsEmployeeTask et where et.projectEmployee.employeeDepartment.employee=? and et.task is not null and " +
                "(et.status.code=? or et.status.code= ? or et.status.code=? or et.status.code = ?) and " +
                "(et.deleted = null or et.deleted = false)", employee, EdsTask.IN_PROGRESS, EdsTask.NOT_STARTED, EdsTask.ON_HOLD, EdsTask.WAITING_FOR_SOMEONE_ELSE);
    }

    public List<EdsEmployeeTask> getProjectEmployeeInvolvedTasks(EdsProjectEmployee pemployee) {
        return find("select et from EdsEmployeeTask et where et.projectEmployee=? and et.task is not null and (et.deleted is null or et.deleted=false) and " +
                "(et.status.code=? or et.status.code= ? or et.status.code=? or et.status.code = ?)", pemployee, EdsTask.IN_PROGRESS, EdsTask.NOT_STARTED, EdsTask.ON_HOLD, EdsTask.WAITING_FOR_SOMEONE_ELSE);

    }

    public EdsEmployeeTask getEmployeeTaskByProjectEmployee(Integer taskId, Integer projectEmployeeId) {
        return (EdsEmployeeTask) findSingle("select employeeTask from EdsEmployeeTask employeeTask " +
                "where employeeTask.task.objectID = ? and employeeTask.projectEmployee.objectID=? and employeeTask.deleted<>true", taskId, projectEmployeeId);

    }

    public void realRemoveAssignee(Integer taskId, Integer projectEmployeeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", taskId);
        map.put("peId", projectEmployeeId);
        map.put("cancelled", EdsTask.CANCELLED);
        updateByNamedParams("delete from EdsEmployeeTask et where et.task.objectID=:taskId and et.projectEmployee.objectID=:peId " +
                "and et.status!=(select r from EdsReference r where r.code=:cancelled)", map);
    }

    public Integer getNewTasksCount(Integer employeeID) {
        return ((Long) findSingle("select count(id) from EdsEmployeeTask et" +
                " where et.projectEmployee.employeeDepartment.employee.objectID=?" +
                " and newTask=true and deleted <> true", employeeID)).intValue();
    }

    public List<EdsEmployeeTask> getEmployeeTask(EdsProjectEmployee projectEmployee) {
        return (List<EdsEmployeeTask>) find("select employeeTask  from EdsEmployeeTask employeeTask " +
                "where employeeTask.projectEmployee.objectID=? and employeeTask.deleted<>true", projectEmployee.getObjectID());
    }

    public List<EdsEmployeeTask> getEstimatedEmployeeTasks(EdsProjectEmployee projectEmployee) {
        return (List<EdsEmployeeTask>) find("select employeeTask  from EdsEmployeeTask employeeTask " +
                "where employeeTask.projectEmployee.objectID=? and employeeTask.deleted<>true and employeeTask.estimatedTime > 0 and (employeeTask.task.status.code = ? or employeeTask.task.status.code = ?)", projectEmployee.getObjectID(), EdsTask.NOT_STARTED, EdsTask.IN_PROGRESS);
    }

    public EdsEmployeeTask getEmployeeRelatedTask(EdsTask task, EdsEmployee employee) {
        return (EdsEmployeeTask) findSingle("SELECT et FROM EdsEmployeeTask et WHERE et.task = ? AND et.projectEmployee.employeeDepartment.employee=? AND et.deleted<>true", task, employee);
    }

    public EdsEmployeeTask getEmployeeRelatedTask(Integer taskID, Integer employeeID) {
        return (EdsEmployeeTask) findSingle("SELECT et FROM EdsEmployeeTask et WHERE et.task.objectID = ? AND et.projectEmployee.employeeDepartment.employee.objectID=? AND et.deleted<>true", taskID, employeeID);
    }

    public void deleteEmployeeTask(EdsEmployeeTask employeeTask) {
        deleteEmployeeTask(employeeTask.getObjectID());
    }

    public List getProjectResourceLoad(ListingFilterParameter fp) {
        String companyId = getCompanyId();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder();

        sql.append("select   e.id, \n");
        sql.append("CASE WHEN  (et.statusid=2) THEN count(*) END as not_startdted,  \n");
        sql.append("CASE WHEN  (et.statusid=3) THEN count(*) END as in_progress,  \n");
        sql.append("CASE WHEN  (et.statusid=79) THEN count(*) END as completed,  \n");
        sql.append("CASE WHEN  (et.statusid=173) THEN count(*) END as waiting,  \n");
        sql.append("CASE WHEN  (et.statusid=174) THEN count(*) END as closed  \n");
        sql.append("from " + companyId + ".employeetask et \n");
        sql.append("left outer join " + companyId + ".projectemployee pe on pe.id = et.projectemployeeid \n");
        sql.append("left outer join " + companyId + ".task t on (t.id=et.taskid) \n");
        sql.append("left outer join " + companyId + ".teamemployee te on te.id=pe.employeeDepartmentId \n");
        sql.append("left outer join " + companyId + ".employee e on e.id = te.employeeid \n");
        sql.append("where (t.isissue is null or t.isissue=false) \n");

        if (fp.getProjectId() != null) {
            sql.append("and  pe.projectid =" + fp.getProjectId() + " ");
            sql.append("and pe.isdeleted<>true \n");
        }
        if (fp.getDepartmentId() != null) {
            sql.append("and te.teamId=" + fp.getDepartmentId() + " ");
            sql.append("and te.isdeleted<>true \n");
        }
        if (fp.getEmployeeId() != null) {
            sql.append("and e.id=" + fp.getEmployeeId() + " ");
        }

        sql.append("and t.deleted<>true \n");
        sql.append("and e.id is not null \n");
        sql.append("group by e.id, et.statusid \n");
        sql.append("order by e.id \n");

        return findNative(sql.toString());
    }

    public List getProjectTasksResourceLoad(ListingFilterParameter fp) {
        String companyId = getCompanyId();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select   t.id, \n");
        sql.append("CASE WHEN  (t.statusid=2) THEN count(*) END as not_startdted,  \n");
        sql.append("CASE WHEN  (t.statusid=3) THEN count(*) END as in_progress,  \n");
        sql.append("CASE WHEN  (t.statusid=79) THEN count(*) END as completed,  \n");
        sql.append("CASE WHEN  (t.statusid=173) THEN count(*) END as waiting,  \n");
        sql.append("CASE WHEN  (t.statusid=174) THEN count(*) END as closed  \n");
        sql.append("from " + companyId + ".task t \n");
        sql.append("inner join " + companyId + ".project p on (p.id = t.projectid) \n");
        sql.append("where (t.deleted = false) and (t.isissue = false or t.isissue is NULL) \n");

        if (fp.getProjectId() != null) {
            sql.append("and  p.id =" + fp.getProjectId() + " ");
            sql.append("and p.isdeleted = false \n");
        }
        sql.append("group by t.id, t.statusid \n");
        sql.append("order by t.id \n");

        return findNative(sql.toString());
    }

    public List<EdsEmployeeTask> getEmployeeTasks(EdsEmployee employee) {
        return getEmployeeTasks(employee, false);
    }

    public List<EdsEmployeeTask> getEmployeeTasks(EdsEmployee employee, Boolean withRecurrence) {
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select et from EdsEmployeeTask et join fetch et.projectEmployee pe join fetch et.task ta join fetch ta.project pr left join fetch pr.client cl " +
                " join fetch et.status st where ");
        sql.append("pe.employeeDepartment.employee=:employee and pe.deleted<>true and ta.deleted<>true ");
        /*only shown in progress, not started, waiting for statuses*/
        sql.append("and (st.code=:inProgress or st.code=:notStarted or st.code=:waitingFor ");
        sql.append("or (st.isSystemReference<>true and st.deleted<>true)");
        sql.append(")");
        if (withRecurrence) {
            sql.append("and ta.recurrenceID is not null");
        }
        sql.append(" and (et.deleted <> true or et.deleted IS NULL) order by ta.id desc");
        paramMap.put("employee", employee);
        paramMap.put("inProgress", EdsTask.IN_PROGRESS);
        paramMap.put("notStarted", EdsTask.NOT_STARTED);
        paramMap.put("waitingFor", EdsTask.WAITING_FOR_SOMEONE_ELSE);
        return findByNamedParams(sql.toString(), paramMap);
    }

    public void removeGoogleIDFromEmployeeTasks(EdsEmployee employee) {
        StringBuilder sql = new StringBuilder("update ");
        sql.append(getCompanyId()).append(".employeetask set googleid = null where projectemployeeid in (select pe.id from ").append(getCompanyId()).append(".projectemployee pe ");
        sql.append("inner join ").append(getCompanyId()).append(".teamemployee te on te.id = pe.employeedepartmentid where te.employeeid=").append(employee.getObjectID() + ")");
        sql.append(" and (deleted <> true or deleted IS NULL)");
        updateNative(sql.toString());
    }

    public void removeOfficeIDFromEmployeeTasks(EdsEmployee employee) {
        StringBuilder sql = new StringBuilder("update ");
        sql.append(getCompanyId()).append(".employeetask set officeid = null where projectemployeeid in (select pe.id from ").append(getCompanyId()).append(".projectemployee pe ");
        sql.append("inner join ").append(getCompanyId()).append(".teamemployee te on te.id = pe.employeedepartmentid where te.employeeid=").append(employee.getObjectID() + ")");
        sql.append(" and (deleted <> true or deleted IS NULL)");
        updateNative(sql.toString());
    }

    public void removeOfficeIDFromTasks(EdsEmployee employee) {
        String schemaName = getCompanyId();
        String sql = "update " + schemaName + ".task set officeid = null where id in (select ta.id from " + schemaName + ".task ta " +
                "        left outer join " + schemaName + ".employeetask et on et.taskid=ta.id " +
                "        left outer join " + schemaName + ".projectemployee pe on(et.projectemployeeid=pe.id )         " +
                "        left outer join " + schemaName + ".project p on (pe.projectid=p.id )                          " +
                "        left outer join " + schemaName + ".teamemployee te on (pe.employeedepartmentid=te.id)         " +
                "        left outer join " + schemaName + ".employee e on (e.id=te.employeeid) where e.id=" + employee.getObjectID() +
                "        and p.isdeleted<>true" +
                "        and ta.deleted<>true )";
        updateNative(sql);
    }

    /**
     * Timesheet plugun
     *
     * @param taskIds
     * @param date
     * @return
     */
    @Override
    public ArrayList<TimesheetRowItemTO> getEmployeesAndTasks(String taskIds, Date date) {
        ArrayList<TimesheetRowItemTO> result = new ArrayList<>();
        boolean fingerprintEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FINGERPRINT_DEVICE_ENABLED);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct mu.id employeeid, mu.firstname ||' '||mu.lastname employeename, empr.employeecode, po.id as positionId, po.name as positionname, tt.startdate checkin, tt.enddate checkout,");
        sql.append("t.id taskid, t.name taskname, t.number tasknumber, t.description taskdescription,tsh.id as timesheetId, tsh.timespent, tsh.comment, tsh.reference ");
        sql.append("from " + getCompanyId() + ".employeetask et ");
        sql.append("inner join " + getCompanyId() + ".task t on et.taskid=t.id ");
        sql.append("inner join " + getCompanyId() + ".projectemployee pe on et.projectemployeeid=pe.id ");
        sql.append("inner join " + getCompanyId() + ".teamEmployee te on pe.employeeDepartmentId=te.id ");
        sql.append("inner join " + getCompanyId() + ".myuser mu on te.employeeId=mu.id ");
        sql.append("inner join " + getCompanyId() + ".employee em on te.employeeId=em.id ");
        sql.append("left join " + getCompanyId() + ".employeeprofile empr on em.profileid=empr.id ");
        sql.append("left join " + getCompanyId() + ".position po on em.positionid=po.id ");
        sql.append("left join " + getCompanyId() + ".timesheet tsh on tsh.employeetaskid=et.id and tsh.date is not null and to_char(tsh.date,'yyyy-MM-dd')='" + dateFormat.format(date) + "' ");
        sql.append("left join (select min(tt.startdate) startdate,max(tt.enddate) enddate," + (fingerprintEnabled ? "fd.userId" : "tt.employeeid") + " employeeid, date(tt.startdate) ttdate ");
        if (fingerprintEnabled) {
            sql.append("from " + getCompanyId()).append(".fingerprint tt ");
            sql.append(" join  " + getCompanyId()).append(".userfingerprintdevice fd on  tt.fingerprintId=fd.fingerprint_id and tt.deviceuuid=fd.device_id ");
        } else {
            sql.append("from " + getCompanyId()).append(".timetrack tt ");
        }
        sql.append(" group by " + (fingerprintEnabled ? "fd.userId" : "tt.employeeid") + ",date(tt.startdate)) tt on tt.employeeid=mu.id and tt.ttdate = '" + dateFormat.format(date) + "' ");

        sql.append("where et.taskid in(" + taskIds + ") and et.deleted is not true ");

        HashMap<Integer, ArrayList<TaskMiniTO>> taskMap = new HashMap<>();
        HashMap<Integer, EmployeeTO> employeeMap = new HashMap<>();

        List<Object[]> objects = findNative(sql.toString());
        for (Object[] objs : objects) {
            Integer employeeID = (Integer) objs[0];
            String employeeName = (String) objs[1];
            String employeeNumber = (String) objs[2];
            Integer positionId = (Integer) objs[3];
            String positionName = (String) objs[4];
            Date checkIn = (Date) objs[5];
            Date checkOut = (Date) objs[6];
            Integer taskId = (Integer) objs[7];
            String taskName = (String) objs[8];
            String taskNumber = (String) objs[9];
            String taskDescription = (String) objs[10];
            Integer timesheetId = (Integer) objs[11];
            Integer timespent = (Integer) objs[12];
            String comment = (String) objs[13];
            String reference = (String) objs[14];

            if (!taskMap.containsKey(employeeID)) {
                EmployeeTO employee = new EmployeeTO();
                employee.setId(employeeID);
                employee.setNumber(employeeNumber);
                employee.setName(employeeName);
                employee.setPosition(new SelectItemTO(positionId, positionName));
                employee.setAttendance(new AttendanceTO(employeeID, WrapUtils.dateToLong(checkIn), WrapUtils.dateToLong(checkOut)));

                employeeMap.put(employeeID, employee);
                taskMap.put(employeeID, new ArrayList<>());

                TimesheetRowItemTO timesheetRowItemTO = new TimesheetRowItemTO();
                timesheetRowItemTO.setEmployee(employeeMap.get(employeeID));
                timesheetRowItemTO.setTasks(taskMap.get(employeeID));
                result.add(timesheetRowItemTO);

            }

            TaskMiniTO task = new TaskMiniTO();
            task.setId(taskId);
            task.setNumber(taskNumber);
            task.setName(taskName);
            task.setDescription(taskDescription == null ? "" : taskDescription);

            TimesheetEntryTO timesheetEntry = new TimesheetEntryTO();
            timesheetEntry.setId(timesheetId);
            timesheetEntry.setMinutes(timespent == null ? 0 : timespent);
            timesheetEntry.setComment(comment == null ? "" : comment);
            timesheetEntry.setReference(reference == null ? "" : reference);
            task.setTimesheetEntry(timesheetEntry);

            taskMap.get(employeeID).add(task);
        }

        return result;

    }

    public void setEmployeeTasksModifiedDate(EdsTask task, Date sharedDate) {
        update("update EdsEmployeeTask et set et.lastModifiedDate = ? where et.task = ?", sharedDate, task);
    }

    @Override
    public EstimateTimeSpentItem getEstimatedTimeSpent(Integer taskID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(eth.estimatedTime - eth.oldEstimatedTime), SUM(eth.timespent - eth.oldTimespent) ");
        sql.append("FROM EdsTaskEstimateTimeSpentHistory eth ");
        sql.append("LEFT JOIN eth.task t ");
        sql.append("WHERE t.deleted = false ");
        sql.append("  AND t.objectID = ? ");

        Object[] object = (Object[]) findSingle(sql.toString(), taskID);
        if (object != null && object.length > 0) {
            return new EstimateTimeSpentItem(object[0] != null ? ((Long) object[0]).intValue() : 0, object[1] != null ? ((Long) object[1]).intValue() : 0);
        }

        return new EstimateTimeSpentItem();
    }

    @Override
    public void deleteEmployeeTaskHistory(Integer taskID) {
        update("DELETE FROM EdsTaskEstimateTimeSpentHistory eth WHERE eth.objectID IN (SELECT h.objectID FROM EdsTaskEstimateTimeSpentHistory h WHERE h.task.objectID = ? ) ", taskID);
    }

    @Override
    public void updateTaskForReCalculationPE(List<Integer> projectEmployeeIds) {
        boolean isFirst = true;
        StringBuilder ids = new StringBuilder();
        for (Integer id : projectEmployeeIds) {
            if (isFirst) {
                ids.append(id);
                isFirst = false;
            } else {
                ids.append(", ").append(id);
            }
        }

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE EdsTask t SET t.changedCalculationFields = true ");
        sql.append("WHERE t.objectID IN (SELECT et.task.objectID FROM EdsEmployeeTask et WHERE et.deleted = false AND et.projectEmployee.objectID IN (" + ids + "))");
        update(sql.toString());
    }

    @Override
    public List<Object> getETStatisticByWS(Integer parentID) {
        return find("SELECT et.projectEmployee.objectID, SUM(et.estimatedTime), SUM(et.timeSpent), SUM(et.percent), COUNT(et.objectID) FROM EdsEmployeeTask et" +
                " join et.task t join t.parent p WHERE et.deleted = false AND t.deleted = false AND p.objectID = ? GROUP BY et.projectEmployee.objectID ", parentID);
    }

    @Override
    public List<EdsEmployeeTask> getDeletedEmployeeTask(Integer taskID, Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT et FROM EdsEmployeeTask et ");
        sql.append("left join et.projectEmployee pe ");
        sql.append("left join pe.employeeDepartment te ");
        sql.append("left join te.employee e ");
        sql.append("WHERE et.deleted = true AND et.task.objectID = '" + taskID + "' ");
        sql.append("AND e.objectID = '" + employeeID + "' ");
        return find(sql.toString());
    }

    public void deleteEmployeeTasks(Integer taskID) {
        update("update EdsEmployeeTask et set et.deleted = true where et.task.objectID = ? and et.deleted = false", taskID);
    }

    public void updateEmployeeTasksStatus(EdsReference status, String taskIds) {
        if (status.getCode().equals(EdsTask.CLOSED)) {
            Date closedDate = new Date();
            update("UPDATE EdsEmployeeTask et SET et.status.objectID =?, et.closedDate = ? WHERE et.task.objectID IN (" + taskIds + ")", status.getObjectID(), closedDate);
        } else {
            update("UPDATE EdsEmployeeTask et SET et.status.objectID =? WHERE et.task.objectID IN (" + taskIds + ")", status.getObjectID());
        }
    }

    public Object[] getEmployeeWageCostByProject(Integer projectEmployeeID) {
        //1-actualWageRate  2-plannedWageRate
        return (Object[]) findSingle("select sum(et.actualWageAmmount) , sum(et.plannedWageAmount) FROM EdsEmployeeTask et where et.projectEmployee.objectID =? and (et.task.deleted <> true or et.task.deleted is null) ", projectEmployeeID);
    }

    public Object[] getEmployeeCostClientChargeByProject(Integer projectEmployeeID) {
        //1-actualClientChargeAmmount  2-plannedClientChargeAmount
        return (Object[]) findSingle("select sum(et.actualClientChargeAmmount), sum(et.plannedClientChargeAmount) FROM EdsEmployeeTask et where et.projectEmployee.objectID =? and (et.task.deleted <> true or et.task.deleted is null)", projectEmployeeID);
    }

    @Override
    public void updateEmployeeTasks(Integer taskID, ArrayList<Integer> projectEmployeeIDs) {
        if (!projectEmployeeIDs.isEmpty() && projectEmployeeIDs.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Integer id : projectEmployeeIDs) {
                sb.append(id).append(",");
            }
            String projectEmployeeIDsString = sb.substring(0, sb.length() - 1);
            updateNative("update " + getCompanyId() + ".employeetask et set deleted = true where et.taskId =" + taskID + " and et.projectEmployeeId not in (" + projectEmployeeIDsString + ")");
        } else {
            updateNative("update " + getCompanyId() + ".employeetask et set deleted = true where et.taskId =" + taskID + " ");
        }
    }

    @Override
    public Float getEmployeeTaskActualPercentCompleted(Integer objectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT max(et.estimatedtime) estimatedtime, sum(coalesce(tsh.timespent,0)) actualtime FROM ").append(getCompanyId()).append(".employeetask et \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".timesheet tsh on tsh.employeetaskId = et.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference ts on ts.id = tsh.statusid \n");
        sql.append("WHERE ts.code = '_APPROVE' \n");
        sql.append("AND et.id = ").append(objectID).append(" \n");
        sql.append("GROUP BY et.id ");

        Object[] object = (Object[])findNativeSingle(sql.toString());

        if (object == null) {
            return 0f;
        }

        Integer estimatedTime = (Integer)object[0];
        BigInteger actualTime = (BigInteger)object[1];

        if (estimatedTime == null || estimatedTime == 0 || actualTime == null || actualTime.intValue() == 0) {
            return 0f;
        }

        Float percent = (actualTime.floatValue() / estimatedTime.floatValue()) * 100;

        return (percent > 100f && !genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) ? 100f : percent;
    }

    @Override
    public EdsEmployeeTask getByOfficeID(String id) {
        List<EdsEmployeeTask> list = (List<EdsEmployeeTask>) find("select task from EdsEmployeeTask task where task.officeID = ? order by  task.lastModifiedDate desc", id);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
