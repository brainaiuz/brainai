package com.edatasite.workforce.gwt.core.server.db.impl.rbac.history;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.core.domain.rbac.history.EdsTaskRbacHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.history.TaskRbacHistoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Abdulaziz
 * Date: Feb 11, 2010
 * Time: 1:32:58 PM
 */
@Repository("taskRbacHistoryManager")
public class TaskRbacHistoryManagerImpl extends BaseManager<EdsTaskRbacHistory> implements TaskRbacHistoryManager, Constants {
    public TaskRbacHistoryManagerImpl() {
        super(EdsTaskRbacHistory.class);
    }

    @Autowired
    private TrusteeManager trusteeManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private TaskManager taskManager;

    public EdsTaskRbacHistory createHistory(EdsTaskRbac taskRbac) {
        EdsTaskRbacHistory taskHistory = new EdsTaskRbacHistory();
        taskHistory.setTask(taskRbac.getTask());
        taskHistory.setUser(taskRbac.getUser());
        taskHistory.setGroup(taskRbac.getGroup());
        taskHistory.setEntryType(taskRbac.getEntryType());
        taskHistory.setRelationship(taskRbac.getRelationship());
        taskHistory.setEntryType(taskRbac.getEntryType());
        taskHistory.setProject(taskRbac.getProject());
        taskHistory.setClient(taskRbac.getClient());
        taskHistory.setDepartment(taskRbac.getDepartment());
        taskHistory.setPercent(taskRbac.getPercent());
        taskHistory.setStatus(taskRbac.getStatus());
        taskHistory.setEstimatedTime(taskRbac.getEstimatedTime());
        taskHistory.setActualStartDate(taskRbac.getActualStartDate());
        taskHistory.setActualEndDate(taskRbac.getActualEndDate());
        taskHistory.setDate(new Date());
        create(taskHistory);
        return taskHistory;
    }

    /**
     * Retreives the list of due tasks against EdsTaskIndexRBACHistory
     * Ussed to display timesheet view task list
     *
     * @param employee
     * @param startOfWeek
     * @param endOfWeek
     * @param fp
     * @return
     */
    public List<EdsTaskRbacHistory> getDueTasks(EdsEmployee employee, Date startOfWeek, Date endOfWeek, ListingFilterParameter fp) {
        Map<String, Object> paramMap = new HashMap<>();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (fp.getTaskStatusId() == null) {
            fp.setTaskStatusId(ALL_DUE_TASKS);
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT et FROM EdsTaskIndexRbacHistory et");
        sql.append(" WHERE et.trustee = :trustee");
        if (fp.getClientId() != null) {
            sql.append(" AND et.client.objectID=:clientid ");
            paramMap.put("clientid", fp.getClientId());
        }

        if (fp.getProjectId() != null) {
            sql.append(" AND et.project.objectID=:projectid");
            paramMap.put("projectid", fp.getProjectId());
        }

        if (fp.getTaskStatusId() != null) {
            if (fp.getTaskStatusId().intValue() != ALL_DUE_TASKS.intValue()) {
                sql.append(" AND (et.status.objectID=:status) ");
                paramMap.put("status", fp.getTaskStatusId());
            } else {
                sql.append(" AND (et.status.code=:inProgress OR et.status.code=:notStarted OR et.status.code=:waitingFor) ");
            }
        }
        sql.append(" AND ((et.actualStartDate IS NULL OR et.actualEndDate IS NULL OR (et.actualStartDate<=:endOfWeek AND et.actualEndDate>=:startOfWeek))");
        sql.append(" AND et.task.startDate<=:endOfWeek AND et.task.dueDate>=:startOfWeek)))");
        sql.append(" AND et.task.startDate<=:endOfWeek ");
        sql.append(" ORDER BY et.task.id DESC");

        if (fp.getTaskStatusId() != null) {
            if (fp.getTaskStatusId().intValue() == ALL_DUE_TASKS.intValue()) {
                paramMap.put("inProgress", EdsTask.IN_PROGRESS);
                paramMap.put("notStarted", EdsTask.NOT_STARTED);
                paramMap.put("waitingFor", EdsTask.WAITING_FOR_SOMEONE_ELSE);
            }
        }
        paramMap.put("startOfWeek", startOfWeek);
        paramMap.put("endOfWeek", endOfWeek);
        return findByNamedParams(sql.toString(), paramMap);
    }

    /**
     * Retreives all timsheet entries of the task
     *
     * @param task
     * @return
     */
    public List<EdsTimeSheet> getEmployeeTaskTimesheets(EdsTask task) {
        return find("SELECT tsh FROM EdsTimeSheet tsh WHERE tsh.taskHistory.task=:task");
    }
}
