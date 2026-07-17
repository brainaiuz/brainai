package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.db.impl.TaskManagerImpl.TaskSearchResult;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 27, 2007
 * Time: 12:49:34 PM
 * To change this template use File | Settings | File Templates.
 */

public interface TaskManager extends Manager<EdsTask>/*, SearchHelper*/ {

    List<EdsTask> list();

    void update(EdsTask task, boolean addToSolr);

    List<EdsTask> getProjectTasks(Integer projectID, Integer start, Integer limit);

    List<EdsTask> getProjectTasksByIntervalWithoutWS(Integer projectID, Integer start, Integer limit);

    List<EdsTask> getWorkStreamTasksByInterval(Integer workStreamID, Integer start, Integer limit);

    List<Integer> getCompanyDeleteTasksForSolr(SolrReindexRpc solrReindex);

    List<EdsTask> getCompanyTasksForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<EdsTask> list(ListingFilterParameter fp);

    List<EdsTask> findOrphanTasks(Integer projectId);

    List<EdsTask> findOrphanTasks(ListingFilterParameter filterParameter);

    List<EdsTask> findOrphanTasksForGanttChart(Integer projectId, Integer employeeId, Date from, Date to, String sortBy);

    List<EdsTask> findOrphanTasksForGanttChart(Integer projectId, Integer employeeId, Date from, Date to, String sortBy, Integer start, Integer limit);

    LinkedHashMap<Integer, List<EdsTask>> findTasksForGanttChart(Integer projectId, Integer employeeId, Date from, Date to);

    List<EdsTask> getTodoListTasks(ListingFilterParameter filterParametrs);

    List<EdsTask> listByParentId(Integer parentId);

    TaskSearchResult findByKeyword(Integer projectId, String[] fields, String keyword, ListingFilterParameter fp, ListLoadConfig config) throws EdsDbException;

    List<EdsProjectEmployee> getTaskAssignees(Integer taskId);

    EdsTask getTaskByIssueId(Integer issueId);

    EdsEmployeeTask getEmployeeTask(Integer employeeId, Integer taskId);

    List<Integer> getCompaniesByTaskRegDate(Date sTime, Date eTime);

    List<EdsTask> getTasksByRegDate(Date sTime, Date eTime, EdsCompany company, boolean includeUpdateTime);

    List<EdsTask> getTaskByIds(String Ids);

    List getRecursivelyPredecessors(Integer rootTaskId);

    List getRecursivelySuccessors(Integer rootTaskId);

    void deleteTask(EdsEmployeeTask employeeTask);

    void deleteProjectTasks(EdsProject project);

    void deleteTask(EdsTask task);

    void deleteEmployeesTask(EdsTask task);

    List<EdsTask> listByProjectAndEmployee(Integer projectId);

    Date getFirstProjectTask(Integer projectID);

    Date getLastProjectTask(Integer projectID);

    Date getLastExistingProjectTask(Integer projectID);

    List<EdsTask> getProjectTasks(EdsProject project);

    List<EdsTask> getProjectTasksOrderByDate(EdsProject project);

    EdsTask getProjectTaskByName(Integer projectID, String name);

    List<EdsTask> getProjectTasksOrderBySDate(EdsProject project);

    List<EdsEmployeeTask> getCalendarTasks(List<Integer> employeeIDs, Date start, Date end, boolean fromAgenda);

    List<EdsEmployeeTask> getUserTasks(List<Integer> employeeIDs, Date startDate, Date endDate, boolean fromAgenda);

    List<EdsEmployeeTask> getEmployeeOverdueTasks(Integer employeeId, Date currentDate);

    List<Integer> getOverdueTasksByIDs(List<Integer> taskIDs);

    List<Integer> getTasksByIDs(List<Integer> taskIDs);

    EdsTask getFirstOrLastTaskInRecurringSeries(Integer recurrenceID, boolean isFirst);

    EdsTask getTaskInstance(Integer recurrenceID, Date fireTime);

    List<EdsTask> getAllTaskInstances(Integer recurrenceID);

    List<EdsTask> getAllTaskInstancesAfter(Integer recurrenceID, Date afterFireTime);

    void updateTask(EdsTask task);

    EdsMyUpdate registerTaskAllUpdates(EdsTask task, EdsUser creator, Date time, String updateType);

    List<EdsTask> getUndeletedTasksIn(String ids);

    Integer getProjectTasksLastIntNumber(Integer projectID, boolean isUnique);

    boolean isTaskNumberExists(String number, Integer projectId, Integer objectID);

    void removeRecurrenceFromTask(Integer recurrenceID, Integer companyID);

    List<Integer> getTaskIDsByIDs(Integer companyID, String ids);

    List<Integer> getTaskIdsWithLimit(Integer companyID, int startat, int limit);

    List<EdsTask> getOrderByTask(ListingFilterParameter filterParameter);

    List<EdsTask> getWorkStreamTasksOrderBy(Integer workStreamID, String sortBy);

	List<EdsTask> getWorkStreamTasksByEmployee(Integer workStreamID, Integer employeeID, String sortBy);

    List getTasksStatisticByWS(Integer parentID);

    Date getRecurringTaskFirstOrLastDate(Integer recurrenceID, Date currentTaskFireTime, boolean isFirst);

    Long getAllTaskInstancesSize(Integer recurrenceID);

    void updateTasksStatus(EdsReference status, Integer projectID);

    Map<Integer, List<String>> getTaskAssigneeUserList(List<Integer> taskIds);
    List<String> getTaskAssigneeUsers(Integer taskId);

    Map<Integer, List<Integer>> getTaskProjectManagerAndBManagerMap(String taskIds);

    void removeTaskPredecessors(Integer taskID);

    String getSavedNumberformat(Integer objectID);

    Date getTaskPredecessorsMaxLastDueDate(Integer taskID);

    List<EdsTask> workflowTaskList(ListingFilterParameter filterParameter);

    Date getLastModifiedTaskDateByEmployee(Integer employeeID);

    Float getTaskActualPercentCompleted(Integer objectID);

    Double getTaskFieldValue(Integer objectID, String customFieldCode);

    EdsTask getSiblingTaskByPrevItem(Integer prevTaskId, Integer statusId);

    Long getTaskCountByStatus(Integer statusID);

    Long getTaskCountByStatus(Long prev, EdsReference status);

    List<EdsTask> getTasksByStatus(Long prev, EdsReference status, int start, int limit);
    List<EdsTask> getTasksByStatus(Integer statusID, int start, int limit);

    List getTasksByDate(ListingFilterParameter filterParameter);

    List<EdsEmployee> getTasksAssigneeByDate(ListingFilterParameter filterParameter);

    Long getMinKanbanOrder(Integer statusId);

    List<Object[]> getMyCalendarDayFirstEvents(Date day, Integer userId);

    List<Object[]> getMyCalendarDayEvents(Date day, Integer userId);
}
