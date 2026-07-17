package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityCircularResolver;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * User: iskan
 * Date: Jan 13, 2008
 * Time: 10:09:18 PM
 */

@Repository("projectEmployeeManager")
public class ProjectEmployeeManagerImpl extends BaseManager<EdsProjectEmployee> implements ProjectEmployeeManager, Constants {

    public ProjectEmployeeManagerImpl() {
        super(EdsProjectEmployee.class);
    }

    @Autowired
    private AvailabilityCircularResolver availabilityCircularResolver;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private ReferenceManager referenceManager;

    public List<EdsProjectEmployee> findByEmployeeAndProject(EdsEmployee employee, EdsProject project) {
        return find(
                "select pe from EdsProjectEmployee pe where pe.employeeDepartment.employee = ?" +
                        " and pe.project = ?", employee, project
        );
    }

    public void deleteByProjectAndTeamemployee(Integer projectId, Integer employeeId) {
        update("update EdsProjectEmployee pe set pe.deleted=true where pe.project.objectID=? " +
                " and pe.employeeDepartment.employee.objectID=?", projectId, employeeId);
    }

    public void deleteAndCreateProjectEmployee(EdsEmployeeDepartment employeeDepartment, EdsEmployeeDepartment newEmployeeDepartment) {
        /*Get List*/
        if (employeeDepartment == null || newEmployeeDepartment == null || employeeDepartment.getObjectID().equals(newEmployeeDepartment.getObjectID())) {
            return;
        }
        List<EdsProjectEmployee> proEmployeeList = find("select pe from EdsProjectEmployee pe where pe.employeeDepartment = ?"
                + " and pe.deleted<>true", employeeDepartment);

        /*Update List*/
        EdsEmployee employee = employeeDepartment.getEmployee();
        EdsCompany employeeDepartmentEmployeeCompany = employee.getCompany();
        EdsModule resourcePlanning = moduleManager.getModuleByCode(PermissionConstants.RESOURCE_PLANNING);
        boolean ruEnabled = resourcePlanning != null;

        update("update EdsProjectEmployee pe set pe.deleted=true,pe.endDate=? where (pe.deleted = false or pe.deleted is null) " +
                " and pe.employeeDepartment = ?", employeeDepartmentEmployeeCompany.getCompanyDate(), employeeDepartment);

        /*Create new ProjectEmployee*/
        System.out.println("---> Started department change for Employee -> " + employee.getFullName());
        long begin = System.currentTimeMillis();
        int i = proEmployeeList.size();
        for (EdsProjectEmployee iprojectEmployee : proEmployeeList) {
            long beginEmployeeTask = System.currentTimeMillis();
            EdsProjectEmployee projectEmployee = get(iprojectEmployee.getObjectID());
            projectEmployee.setDeleted(true);

            EdsProjectEmployee pe = new EdsProjectEmployee();
            pe.setProject(projectEmployee.getProject());
            pe.setStartDate(employeeDepartmentEmployeeCompany.getCompanyDate());
            pe.setEmployeeDepartment(newEmployeeDepartment);
            pe.setWageRate(projectEmployee.getWageRate());
            pe.setClientChargeRate(projectEmployee.getClientChargeRate());
            pe.setWorkloadPercentage(projectEmployee.getWorkloadPercentage());
            pe.setPosition(projectEmployee.getPosition());
            pe.setContractStartDate(projectEmployee.getContractStartDate());
            pe.setContractEndDate(projectEmployee.getContractEndDate());
            create(pe);

            for (EdsProjectEmployeeWageClientRateHistory history : projectEmployee.getWageClientRatesHistory()) {
                EdsProjectEmployeeWageClientRateHistory prate = new EdsProjectEmployeeWageClientRateHistory();
                prate.setChangeDate(history.getChangeDate());
                prate.setClientChargeRate(history.getClientChargeRate());
                prate.setWageRate(history.getWageRate());
                prate.setWorkloadPercentage(history.getWorkloadPercentage());
                prate.setProjectEmployee(history.getProjectEmployee());
                pe.getWageClientRatesHistory().add(prate);
            }

            /*Get List EmployeeTask*/
            List<EdsEmployeeTask> employeeTask = find("select etask from EdsEmployeeTask etask " +
                    "where etask.projectEmployee=? and etask.deleted<>true and (etask.task.status.code='NOT_STARTED'  or etask.task.status.code='IN_PROGRESS')", projectEmployee);
            if (!ruEnabled) {
                update("insert into EdsEmployeeTask (task, startDate, status, timeSpent,dailyLoad,newTask,estimatedTime,deleted) " +
                        "select task, startDate, status, timeSpent,dailyLoad,newTask,estimatedTime,deleted from EdsEmployeeTask etask " +
                        "where etask.projectEmployee=? and etask.deleted<>true and (etask.task.status.code='NOT_STARTED'  or etask.task.status.code='IN_PROGRESS')", projectEmployee);
                update("update EdsEmployeeTask etask set etask.projectEmployee = ?" +
                        "where etask.projectEmployee is null", pe);
            }

            for (EdsEmployeeTask iet : employeeTask) {
                EdsEmployeeTask et = employeeTaskManager.get(iet.getObjectID());
                et.setDeleted(true);
                if (ruEnabled) {
                    EdsEmployeeTask eTask = new EdsEmployeeTask();
                    eTask.setProjectEmployee(pe);
                    eTask.setTask(et.getTask());
                    eTask.setStartDate(employeeDepartmentEmployeeCompany.getCompanyDate());
                    eTask.setStatus(et.getStatus());
                    eTask.setTimeSpent(et.getTimeSpent());
                    eTask.setDailyLoad(et.getDailyLoad());
                    eTask.setNewTask(et.isNewTask());
                    eTask.setEstimatedTime(et.getEstimatedTime());
                    employeeTaskManager.create(eTask);
                    //create or update daily timeSheet estimated time
                    //if (ServerUtils.isSettingEnabled(Constants.ENABLED_MANNUAL_RU_ESTIMATE)) {
                        availabilityCircularResolver.createOrUpdateTimeSheetDataForEmployeeAndEmployeeTask(employee, eTask, et);
                    /*} else {
                        availabilityCircularResolver.createOrUpdateTimeSheetDataForEmployeeAndEmployeeTask(employee, eTask, null);
                    }*/
                }
            }

            jpaTemplate.flush();
            System.out.println("---> Time spent for recreation of employee tasks - " + (System.currentTimeMillis() - beginEmployeeTask));
            System.out.println("Employee Project Processed #" + i--);


        }
        System.out.println("---> Ended department change, time spent:" + (System.currentTimeMillis() - begin));

    }

    public void deleteByEmployeedepartment(EdsEmployeeDepartment edsEmployeeDepartment) {
        update("update EdsProjectEmployee pe set pe.deleted=true where pe.employeeDepartment=? " +
                " and pe.deleted<>true", edsEmployeeDepartment);

    }

    public void updateProjectEmployee(EdsEmployeeDepartment edsEmployeeDepartment) {
        update("update EdsProjectEmployee pe set pe.deleted=true where pe.objectID in " +
                "(select pe.objectID from EdsProjectEmployee pe where pe.deleted<>true and pe.project.status.code!='" + EdsProject.COMPLETED + "' and pe.employeeDepartment=?)", edsEmployeeDepartment);
    }

    public List<EdsEmployee> getEmployeesByProject(Integer projectId) {
        return (List<EdsEmployee>) find("select pe.employeeDepartment.employee from  EdsProjectEmployee pe" +
                " where pe.project.objectID=? and pe.deleted<>true", projectId);
    }

    public ArrayList<Integer> getEmployeeIDsByProject(Integer projectId) {
        return (ArrayList<Integer>) find("select pe.employeeDepartment.employee.objectID from  EdsProjectEmployee pe" +
                " where pe.project.objectID=? and pe.deleted<>true", projectId);
    }

    public List<EdsEmployee> getEmployeesByProject(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pe.employeeDepartment.employee ");
        sql.append("FROM EdsProjectEmployee pe ");
        sql.append("WHERE pe.deleted is not true ");
        if (fp.getProjectId() != null) {
            sql.append("AND pe.project.objectID ='" + fp.getProjectId() + "' ");
        }
        if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
            sql.append(" AND (");
            sql.append("LOWER(pe.employeeDepartment.employee.firstName) LIKE '").append(fp.getSqlSearchKey()).append("' OR ");
            sql.append("LOWER(pe.employeeDepartment.employee.lastName) LIKE '").append(fp.getSqlSearchKey()).append("' OR ");
            sql.append("LOWER(pe.employeeDepartment.employee.profile.employeeCode) LIKE '").append(fp.getSqlSearchKey()).append("'");
            sql.append(")");
        }
        return (List<EdsEmployee>) findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    public List<EdsEmployee> getEmployeesByProjectWithDeletedEmployees(Integer projectId) {
        return (List<EdsEmployee>) find("select DISTINCT pe.employeeDepartment.employee from  EdsProjectEmployee pe" +
                " where pe.project.objectID=?", projectId);
    }

    public List<EdsProject> getEmployeeProjects() {
        if (!(getUser() instanceof EdsEmployee)) {
            return new ArrayList<>();
        }
        return getEmployeeProjects((EdsEmployee) getUser());
    }

    public List<EdsProject> getEmployeeProjects(EdsEmployee employee) {
        return find("select pe.project from EdsProjectEmployee pe where pe.employeeDepartment.employee=? and pe.deleted<>true", employee);
    }

    public Integer getProjectPlannedTime(Integer projectId, Integer type) {
        Calendar calendar = new GregorianCalendar();
        int plannedTime = 0;
        List<EdsEmployee> employees = getEmployeesByProject(projectId);
        for (EdsEmployee e : employees) {
            if (e.getTimeSlot() != null) {
                Set<EdsTimeSlotItem> items = e.getTimeSlot().getItems();
                if (type.equals(1)) {
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    for (EdsTimeSlotItem item : items) {
                        if (getRealDayOfWeek(item.getDay()).equals(dayOfWeek)) {
                            plannedTime += calculatePlannedTime(item);
                        }
                    }
                } else if (type.equals(2)) {
                    for (EdsTimeSlotItem item : items) {
                        plannedTime += calculatePlannedTime(item);
                    }
                } else if (type.equals(3)) {
                    int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 1; i <= max; i++) {
                        calendar.set(Calendar.DAY_OF_MONTH, i);
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        for (EdsTimeSlotItem item : items) {
                            if (getRealDayOfWeek(item.getDay()).equals(dayOfWeek)) {
                                plannedTime += calculatePlannedTime(item);
                            }
                        }
                    }
                }
            }
        }
        return plannedTime;
    }

    private int calculatePlannedTime(EdsTimeSlotItem item) {
        int coffeeTime = 0, lunchTime = 0, totalTime;
        if (item.getCoffeeStart() != null && item.getCoffeeEnd() != null) {
            coffeeTime = item.getCoffeeEnd() - item.getCoffeeStart();
        }
        if (item.getLunchStart() != null && item.getLunchEnd() != null) {
            lunchTime = item.getLunchEnd() - item.getLunchStart();
        }
        totalTime = item.getEndTime() - item.getStartTime();
        return totalTime - (coffeeTime + lunchTime);
    }

    private Integer getRealDayOfWeek(Integer day) {
        day += 2;
        if (day.equals(8)) {
            day = 1;
        }
        return day;
    }

    public void deleteProjectInPE(EdsProject project) {
        update("update EdsProjectEmployee pe set pe.deleted=true " +
                "where pe.project=? and pe.deleted<>true", project);
    }

    public List<EdsProjectEmployee> getProjectEmployees(EdsProject project) {
        return find("select pe from EdsProjectEmployee pe where pe.project=? and (pe.deleted is null or pe.deleted is false)", project);
    }

    @Override
    public List<EdsEmployee> getProjectEmployees2(EdsProject project) {
        return find("select e from EdsProjectEmployee pe join pe.employeeDepartment ed join ed.employee e where pe.project=? and pe.deleted is not true", project);
    }

    public EdsProjectEmployee getProjectEmployee(EdsEmployee employee, EdsProject edsProject) {
        return (EdsProjectEmployee) findSingle(
                "select pe from EdsProjectEmployee pe where pe.employeeDepartment.employee = ?" +
                        " and pe.project = ? and (pe.deleted is false or pe.deleted is null)", employee, edsProject
        );
    }

    @Override
    public EdsProjectEmployee getProjectEmployee(EdsEmployee employee, EdsProject edsProject, Date contractStartDate) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        map.put("project", edsProject);
        map.put("date", contractStartDate);

        return (EdsProjectEmployee) findSingleByNamedParams(
                "select pe from EdsProjectEmployee pe where pe.employeeDepartment.employee = :employee" +
                        " and pe.project = :project and (pe.deleted is false or pe.deleted is null) " +
                        " and (pe.contractStartDate = :date OR date_trunc('month', pe.contractStartDate) = :date OR pe.contractStartDate <= :date) " +
                        " and (pe.contractEndDate is null or pe.contractEndDate >= :date)", map);
        /*return (EdsProjectEmployee) findSingle(
                "select pe from EdsProjectEmployee pe where pe.employeeDepartment.employee = ?" +
                        " and pe.project = ? and (pe.deleted is false or pe.deleted is null) and pe.contractStartDate = ?", employee, edsProject, contractStartDate
        );*/
    }

    public EdsProjectEmployee findProjectEmployeeByEmployeeName(Integer projectId, String firstName, String lastName) {
        return (EdsProjectEmployee) findSingle("select distinct pe from EdsProjectEmployee pe where pe.project.objectID=? " +
                "and pe.employeeDepartment.employee.firstName = ? and pe.employeeDepartment.employee.lastName = ? and " +
                "pe.deleted=false and pe.project.deleted=false and pe.employeeDepartment.employee.deleted=false", projectId, firstName, lastName);
    }

    @Override
    public EdsProjectEmployee getEmployeeLastAssignedProject(Integer projectId, Integer employeeId) {
        return (EdsProjectEmployee) findSingle("select distinct pe from EdsProjectEmployee pe where " +
                " pe.employeeDepartment.employee.objectID = ? and pe.contractStartDate is not null " +
                "and pe.deleted=false and pe.project.deleted=false and pe.employeeDepartment.employee.deleted=false " +
                /*(projectId != null ? " and pe.project.objectID != "+projectId : "") +*/
                " order by pe.contractStartDate desc ", employeeId);
    }

    public List<EdsProjectEmployee> getProjectEmployees(EdsEmployee employee) {
        List<EdsProjectEmployee> result = new ArrayList<>();
        List<EdsProject> employeeProjects = getEmployeeProjects(employee);
        for (EdsProject employeeProject : employeeProjects) {
            EdsProjectEmployee projectEmployee = getProjectEmployee(employee, employeeProject);
            result.add(projectEmployee);
        }
        return result;
    }

    public List<EdsProjectEmployee> getDeleteProjectEmployees(EdsProject project) {
        return (List<EdsProjectEmployee>) find("select pe from EdsProjectEmployee pe where pe.project=? and pe.deleted = false", project);
    }

    public List<EdsProjectEmployeeWageClientRateHistory> getProjectEmployeeWageClientRateHistory(Integer projectEmployeeId) {
        return find("select distinct pe from EdsProjectEmployeeWageClientRateHistory pe where pe.projectEmployee.objectID = ? order by pe.changeDate", projectEmployeeId);
    }

    public List<ProjectMember> getProjectEmployeesInfo(Integer projectID) {
        List<ProjectMember> projectMembers = new ArrayList<>();

        EdsReference completed = referenceManager.getByCode(EdsProject.COMPLETED);
        EdsReference inactive = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_RESIGNED);

        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append(" select");
        sql.append(" estimated.id as employeeid, max(fullname) as fullname, max(department) as departmentname, max(positionname) as positionname, ");
        sql.append(" coalesce(sum(actualTimeSpent),0) actualTimeSpent,  coalesce(max(estimatedTimeSpent), 0) estimatedTimeSpent, ");
        sql.append(" coalesce(sum(hourspent), 0) hourspent, coalesce(max(taskcount), 0) taskcount, coalesce(max(sumpercent), 0) sumpercent, ");
        sql.append(" coalesce(max(employeeCode), '') as employeeCode, ");
        sql.append(" projectEmployeeId ");
        sql.append(" from ");
        sql.append(" (select");
        sql.append(" e.id, max(tm.name) as department, max(pn.name) as positionname, sum(et.estimatedtime) as estimatedTimeSpent,");
        sql.append(" count(t.id) taskcount, sum(et.percent) sumpercent, ");
        if (inactive != null) {
            sql.append("max(mu.firstname||' '||mu.lastname || ' ' || " +
                    "coalesce (case when (mu.accountStatusid=" + inactive.getObjectID() +") THEN '(resigned)' ELSE '' END)) as fullname, ");
        } else {
            sql.append("max(mu.firstname||' '||mu.lastname) as fullname, ");
        }

        sql.append("coalesce(max(ep.employeeCode), '') as employeeCode, ");
        sql.append(" pe.id as projectEmployeeId ");
        sql.append(" from ").append(companyID).append(".projectemployee pe ");
        sql.append(" left join ").append(companyID).append(".teamEmployee te on pe.employeeDepartmentId=te.id ");
        sql.append(" left join ").append(companyID).append(".team tm on tm.id=te.teamId ");
        sql.append(" left join ").append(companyID).append(".employee e on te.employeeId=e.id ");
        sql.append(" left join ").append(companyID).append(".employeeprofile ep on e.profileId=ep.id ");
        sql.append(" left join ").append(companyID).append(".myuser mu on mu.id=e.id ");
        sql.append(" left join ").append(companyID).append(".position pn on e.positionid=pn.id ");
        sql.append(" left join ").append(companyID).append(".employeetask et on pe.id = et.projectemployeeid and et.deleted is not true ");
        sql.append(" left join ").append(companyID).append(".task t on et.taskid=t.id and t.deleted is not true ");
        sql.append(" left join ").append(companyID).append(".project p on p.id=pe.projectid ");
        sql.append(" where p.id = ").append(projectID);
        if (inactive != null) {
            sql.append(" and ((pe.isdeleted is not true and mu.deleted is not true) OR (mu.accountStatusid=" + inactive.getObjectID() +")) ");
        } else {
            sql.append(" and pe.isdeleted is not true and and mu.deleted is not true ");
        }
        sql.append(" group by e.id, pe.id) as estimated  ");
        sql.append(" left join ");
        sql.append(" (select case when r.code='_APPROVE' then sum(ts.timeSpent) end actualTimeSpent, sum(ts.timeSpent) hourspent, employeeid ");
        sql.append(" from ").append(companyID).append(".timesheet ts ");
        sql.append(" left join ").append(companyID).append(".reference r on r.id = ts.statusid ");
        sql.append(" where ts.projectid = ").append(projectID);
        sql.append(" group by ts.employeeid, r.code) as actual on estimated.id = actual.employeeid ");
        sql.append(" group by estimated.id, estimated.projectEmployeeId order by fullname");

        List<Object[]> listResult = findNative(sql.toString());
        ProjectMember projectMember;
        for (Object[] resultRow : listResult) {
            projectMember = new ProjectMember();
            projectMember.setId((Integer) resultRow[0]);
            projectMember.setName((String) resultRow[1]);
            projectMember.setTeamName((String) resultRow[2]);
            projectMember.setPosititon((String) resultRow[3]);
            projectMember.setActualTime(Integer.valueOf(resultRow[4].toString()));
            projectMember.setEstimatedTime(Integer.valueOf(resultRow[5].toString()));
            projectMember.setTimeSpent(Integer.valueOf(resultRow[6].toString()));
            projectMember.setTaskCount(Integer.valueOf(resultRow[7].toString()));
            projectMember.setPercentSum(Float.valueOf(resultRow[8].toString()));
            projectMember.setEmployeeNumber((String) resultRow[9]);
            projectMember.setProjectEmployeeId(Integer.valueOf(resultRow[10].toString()));
            projectMembers.add(projectMember);
        }
        return projectMembers;
    }

    @Override
    public void create(EdsProjectEmployee obj) {
        if (!obj.getHistorical()) {
            EdsAuditInfo info = new EdsAuditInfo();
            if (info.getCreationDate() == null) {
                info.setCreationDate(new Date());
            }
            info.setModificationDate(new Date());
            obj.setAuditInfo(info);
            super.create(obj);
        } else {
            super.create(obj);
        }
    }

    @Override
    public void update(EdsProjectEmployee obj) {
        EdsProjectEmployee clonedProjectEmployee = null;
        EdsAuditInfo info = obj.getAuditInfo();
        if (info != null) {
            info.setModificationDate(new Date());
            info.setModifiedBy(getUser());
        } else {
            info = new EdsAuditInfo();
            if (info.getCreatedBy() == null) {
                info.setCreatedBy(getUser());
            }
            if (info.getCreationDate() == null) {
                info.setCreationDate(new Date());
            }
            info.setModificationDate(new Date());
            info.setModifiedBy(getUser());
            obj.setAuditInfo(info);
        }
//        }
        super.update(obj);
    }

    private void cloneProjectEmployee(EdsProjectEmployee obj, EdsProjectEmployee clonedProjectEmployee) {
        if (clonedProjectEmployee != null) {
            clonedProjectEmployee.setDeleted(true);
            clonedProjectEmployee.setHistorical(true);
            clonedProjectEmployee.setHistoricalParent(obj);
            clonedProjectEmployee.setSubProjectEmployees(new ArrayList<>());
            clonedProjectEmployee.setAppliedJobFunctions(new HashSet<>());
            clonedProjectEmployee.setEmployeeTasks(new HashSet<>());
            clonedProjectEmployee.setWageClientRatesHistory(new ArrayList<>());
            this.create(clonedProjectEmployee);
        }
    }

    @Override
    public List<ProjectMember> getProjectEmployeesByContract(Integer employeeId, Date contractStart, Date contractEnd, Integer projectID) {
        Map<String, Object> map = new HashMap<>();
        map.put("employeeId", employeeId);
        map.put("contractStart", contractStart);
        map.put("contractEnd", contractEnd);

        StringBuilder sql = new StringBuilder();
        sql.append("select te.employeeid as id, pe.id as projectEmployeeId, pe.projectid, pe.contractStartDate as contractStart, pe.contractEndDate as contractEnd  from ").append(getCompanyId()).append(".projectemployee pe \n");
        sql.append("inner join ").append(getCompanyId()).append(".teamEmployee te on te.id = pe.employeeDepartmentId \n");
        sql.append("inner join ").append(getCompanyId()).append(".projectPostion pp on pp.projectid = pe.projectid and pp.position_id = pe.positionid \n");
        sql.append("where pe.isdeleted is not true and te.employeeId = :employeeId \n");
        sql.append("and ((pe.contractStartDate between :contractStart and :contractEnd) or (pe.contractEndDate is not null and pe.contractEndDate between :contractStart and :contractEnd) \n");
        sql.append("      or (pe.contractStartDate < :contractStart and (pe.contractEndDate is null or pe.contractEndDate > :contractEnd))) \n");

        if (projectID != null) {
            sql.append(" and pe.projectid != ").append(projectID).append("\n");
        }

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), map, (rs, rowNum) -> {
                    ProjectMember pm = new ProjectMember();
                    pm.setId(rs.getInt("id"));
                    pm.setProjectEmployeeId(rs.getInt("projectEmployeeId"));
                    pm.setProjectId(rs.getInt("projectid"));
                    Date startTs = rs.getTimestamp("contractStart");
                    Date endTs = rs.getTimestamp("contractEnd");
                    pm.setContractStart(startTs != null ? new DateNonConvertable(startTs) : null);
                    pm.setContractEnd(endTs != null ? new DateNonConvertable(endTs) : null);
                    return pm;
                }
        );
    }

    @Override
    public void updateProjectWageRates(Integer empID, Double wageRate, Double clientChargeRate, Date applyDate) {
        StringBuilder sql = new StringBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        String companyID = getCompanyId();
        sql.append(" update ").append(companyID).append(".ProjectEmployeeWageClientRateHistory set wagerate = ")
                .append(wageRate).append(" , clientChargeRate =").append(clientChargeRate);
        sql.append(" where ");
        sql.append(" projectemployeeid in (select pe.id from ").append(companyID).append(".projectemployee pe  ");
        sql.append(" left join  ").append(companyID).append(".teamEmployee te on te.id = pe.employeeDepartmentId");
        sql.append(" left join  ").append(companyID).append(".project p on p.id = pe.projectid");
        sql.append(" where ");
        sql.append(" p.isDeleted = false and pe.isDeleted = false and te.isdeleted = false and te.employeeid = ").append(empID).append(")");
        sql.append(" and changeDate >= '").append(dateFormatter.format(applyDate)).append("'");

        updateNative(sql.toString());

    }

    @Override
    public HashMap<Integer, EdsProjectEmployee> getProjectEmployeesAsMap(EdsProject project) {
        HashMap<Integer, EdsProjectEmployee> map = new HashMap<>();
        List<EdsProjectEmployee> projectEmployees = getProjectEmployees(project);

        for (EdsProjectEmployee pe : projectEmployees) {
            map.put(pe.getObjectID(), pe);
        }
        return map;
    }
}
