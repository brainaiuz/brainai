package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.task.client.rpc.EstimateTimeSpentItem;
import com.edatasite.workforce.rest.base.to.TimesheetRowItemTO;

import java.util.*;

public interface EmployeeTaskManager extends Manager<EdsEmployeeTask> {

    List<EdsEmployeeTask> getTaskListForMobile(EdsEmployee employee, Date date, Integer projectID);

    List<EdsEmployeeTask> listDueTasks(EdsEmployee employee, Date startOfWeek, Date endOfWeek, ListingFilterParameter fp);

    List<EdsEmployeeTask> listDueTasks(EdsEmployee employee, Date startOfWeek, Date endOfWeek, LinkedHashMap<String, String> projectTasks, ListingFilterParameter fp);

    List<EdsEmployeeTask> listTimesheetFilterData(EdsEmployee employee, Date startOfWeek, Date endOfWeek);

    List<EdsEmployeeTask> getEmployeeTasks(Integer employeeID, EdsTask task);

    void deleteEmployeeTask(Integer employeeTaskID);

    void deleteEmployeeTasksByEmployee(Integer employeeID);

    void deleteEmployeeTasks(Integer taskID);

    void updateEmployeeTasksStatus(EdsReference status, String taskIds);

    List<EdsEmployeeTask> sort(Collection<EdsEmployeeTask> assignments);

    EdsEmployeeTask getEmployeeTask(Integer taskId, Integer projectEmployeeId);

    EdsEmployeeTask getEmployeeTask(Integer taskId, Integer projectEmployeeId, boolean includingDeleted);

    List<EdsEmployeeTask> getProjectEmployeeTasks(Integer employeeId, Integer projectID);

    List<EdsTimeSheet> getAllTimeSpent(Integer projectID);

    void realRemoveAssignee(Integer taskId, Integer projectEmployeeId);

    List<EdsEmployeeTask> getEmployeeInvolvedTasks(EdsEmployee employee);

    List<EdsEmployeeTask> getProjectEmployeeInvolvedTasks(EdsProjectEmployee pemployee);

    EdsEmployeeTask getEmployeeTaskByProjectEmployee(Integer taskId, Integer projectEmployeeId);

    Integer getNewTasksCount(Integer employeeId);

    List<EdsEmployeeTask> getEmployeeTask(EdsProjectEmployee projectEmployee);

    List<EdsEmployeeTask> getEstimatedEmployeeTasks(EdsProjectEmployee projectEmployee);

    EdsEmployeeTask getEmployeeRelatedTask(EdsTask task, EdsEmployee employee);

    EdsEmployeeTask getEmployeeRelatedTask(Integer taskID, Integer employeeID);

    void deleteEmployeeTask(EdsEmployeeTask employeeTask);

    List getProjectResourceLoad(ListingFilterParameter fp);

    List getProjectTasksResourceLoad(ListingFilterParameter fp);

    List<EdsEmployeeTask> getEmployeeTasks(EdsEmployee employee);

    List<EdsEmployeeTask> getEmployeeTasks(EdsEmployee employee, Boolean withRecurrence);

    void removeGoogleIDFromEmployeeTasks(EdsEmployee employee);

    void setEmployeeTasksModifiedDate(EdsTask task, Date sharedDate);

    EstimateTimeSpentItem getEstimatedTimeSpent(Integer taskID);

    void deleteEmployeeTaskHistory(Integer taskID);

    /*
    * Update task for the recalculate by Project Employees
    */
    void updateTaskForReCalculationPE(List<Integer> projectEmployeeIds);

    List<Object> getETStatisticByWS(Integer parentID);

    List<EdsEmployeeTask> getDeletedEmployeeTask(Integer taskID, Integer employeeID);

    Object[] getEmployeeWageCostByProject(Integer projectEmployee);

    Object[] getEmployeeCostClientChargeByProject(Integer projectEmployeeID);

    void updateEmployeeTasks(Integer taskID, ArrayList<Integer> projectEmployeeIDs);

    Float getEmployeeTaskActualPercentCompleted(Integer objectID);

    EdsEmployeeTask getByOfficeID(String id);

    void removeOfficeIDFromEmployeeTasks(EdsEmployee employee);

    void removeOfficeIDFromTasks(EdsEmployee employee);

    ArrayList<TimesheetRowItemTO> getEmployeesAndTasks(String taskIds, Date date);
}
