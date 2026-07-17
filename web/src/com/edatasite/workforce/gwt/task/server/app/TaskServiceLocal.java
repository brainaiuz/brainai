package com.edatasite.workforce.gwt.task.server.app;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.ExistingAndNewTaskMembers;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueItem;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.client.rpc.PositionProjectEmployeeIdTime;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamSingleItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import net.sf.mpxj.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskServiceLocal {

    Integer createIssueItem(IssueItem item);

    Integer indexProjectTasks(Integer projectID, Integer start, Integer limit);

    ProjectItem[] getProjects();

    EditTask getTaskForEdit(Integer objectId);

    PositionsSelectItem[] getAssigneesWithPositions1(Integer projectId);

    PositionsSelectItem[] getAssigneesWithPositionsForMobile(Integer projectId);

    SelectItem[] getPriorities();

    TaskSingleItem getTask(Integer objectId, Boolean isFromCRM);

    Integer[] saveTask(TaskSingleItem newTask) throws NumberExistingException;

    ExistingAndNewTaskMembers getAssigneesWithPositions(Integer taskId, Integer projectId);

    void addNewProjectMembersAndAssignTasks(Integer projectId, ProjectMember[] members);

    Integer indexCompanyProjects(SolrReindexRpc solrReindex, Integer start, int limit);

    void calculateTaskBudgets(EdsTask task);

    void calculateWorkStreamBudgets(Integer objectID);

    NumberData generateTaskNumber(Integer projectID, Date startDate, Integer objectId);

    void updateTask(EditTask task) throws NumberExistingException;

    String deleteTask(Integer taskId, String context);

//    TaskSelectItem getTaskItemByQBTaskID(String qbTaskID);
//
//    void updateTaskByQB(EditTask task, String externalGUID, Integer synchItemId);

    Integer[] saveMultipleTask(MultiTaskList multiTaskList);

    List<String> getAggregatePermissions(List<EdsTaskRbac> rbacEntries);

    void importDataFromMPPFile(LinkedList<Task> listTasks, HashMap projectId) throws NumberExistingException;

    void updateTaskDates(boolean isDontKeepDelays, EdsUser user, EdsTask task, Date taskOldDueDate);

    void shiftAllSuccessors(EdsTask task, EdsTask precedingTask, EdsUser user, boolean dontKeepDelays, long delayInMilliseconds, Date firstTaskOldDate, Map<Integer, Integer> defaultTimeslot);

    TaskList getTaskList(ListingFilterParameter filterParameter);

    String deleteTask(EdsTask task, EdsUser user, String context);

    void createTaskRecurringInstances(EdsTask task, EdsRecurrence recurrence, List<Date> recurringDates);

    void createRecurringTask();

    void sendEmailNotification(Integer taskID);

    void sendWorkstreamEmailNotification(Integer workstreamID);

    EdsUser checkForArtificateRoles(int taskID);

    List<Object[]> getProjectMembers(Integer projectId, EdsUser user, boolean isManager);

    List<Object[]> getProjectMembers(Integer projectId, EdsUser user, String permission);

    void removeDeletedEmployeeRbacks(Integer employeeID);

    NumberData generateWorkstreamNumber(Integer projectID, Date startdate, Integer objectID);

    EdsProjectEmployee addMembers(EdsProject project, EdsEmployee employee);

    EdsTask saveTaskDetailed(TaskSingleItem taskSingleItem, EdsUser user) throws NumberExistingException;

    void updateTaskDailyLoad(EdsTask edsTask);

    void deleteWorkstream(Integer workstreamID, Integer defaultWorkstreamID, boolean withAllTasksAndSUBW);

    Integer createWorkstream(WorkstreamSingleItem newWorkstream, PositionProjectEmployeeIdTime assignees) throws NumberExistingException;

    void updateTaskCellPercent(final EdsTask task, Float complete);

    void findPredecessorParentWS(EdsTask task, Set<EdsWorkStream> workStreams);

    void findSuccessorParentWS(EdsTask task, Set<EdsWorkStream> workStreams);

    void updateWorkStreamDateRange(EdsTask task, EdsWorkStream workStream);

    void refreshTaskDependencies(Set<EdsTask> newTasks, Set<EdsTask> oldTasks);

    void createTaskRecurringInstancesBg(Integer taskID, Integer recurrenceID);

    Boolean setTimeToTimesheet(TimesheetDataItem dataItem);

    SelectItem getProjectByTask(Integer taskID);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getTaskMembersWithTreeInfo(Integer taskID);

    Boolean checkAccess(Integer taskID, String permission, String context);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getAssigneesWithTreeInfoLinkedHashMapWithParams(LinkedHashMap<Integer, Integer> userIDs, Integer projectId, Integer basicTaskID, boolean selectMeOrOne);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getAssigneeListOnlyAvailableEmployees(List<Integer> userIDs, Integer projectId, Date startDate, Date endDate);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getAssigneesWithTreeInfoLinkedHashMap(LinkedHashMap<Integer, Integer> userIDs);

    boolean saveTaskEditCellValue(TaskListItem rowValue, String columnCodeName);

//    Float updatePercentCompleted(Integer employeeTaskId, float percentCompleted, boolean solrUpdate);

    SelectItem[] getEditTaskStatusDrop(Integer taskId);

    TaskInvolvedMember[] getAssignments(Integer taskID);

    SelectItem[] getAddTaskStatuses();

    void createTaskInstance(Integer taskId, Integer employeeId);

    void mergeTaskAccounts(Integer objectID, ArrayList<Integer> otherObjectIDs);
}
