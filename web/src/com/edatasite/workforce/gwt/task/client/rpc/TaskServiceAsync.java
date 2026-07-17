package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 26, 2007
 * Time: 6:37:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TaskServiceAsync {

    void updateTask(EditTask task, AsyncCallback<Void> async);

    void saveTaskWithNewProjectEmployees(TaskSingleItem newTask, AsyncCallback<Integer[]> callback);

    void saveTask(TaskSingleItem newTask, AsyncCallback<Integer[]> callback);

    void setAssignees(Integer taskID, WfmTreeItem[] assignees, AsyncCallback<String> async);

    void getPriorities(AsyncCallback<SelectItem[]> async);

    void getTaskTypes(AsyncCallback<SelectItem[]> async);

    Request getTaskList(ListingFilterParameter filterParameter, AsyncCallback<TaskList> async);

    Request getNewKanbanTasks(ListingFilterParameter filterParameter, SelectItem columnMetadata, AsyncCallback<ListResult<TaskListItem>> async);

    void changeTaskKanbanOrder(SelectItem columnLayoutData, Integer taskID, Integer task, Integer afterItem, AsyncCallback<Void> async);
    void getNewKanbanTasksCounts(ArrayList<Integer> columnIds,AsyncCallback<LinkedHashMap<Integer, Long>> async);

    void getTaskBudget(Integer taskId, AsyncCallback<BudgetItem[]> async);

    Request getLatestTasks(Integer projectId, ListLoadConfig config, Integer[] taskIds, boolean includeParentExistSubtasks, AsyncCallback<TaskSelectItemList> async);

    Request searchTasks(Integer projectId, String keyword, ListingFilterParameter fp, ListLoadConfig config, Integer[] taskIds, boolean includeParentExistSubtasks, AsyncCallback<TaskSelectItemList> async);

    void getAssignees(Integer projectId, AsyncCallback<SelectItem[]> async);

    void getAssigneesWithPositions1(Integer projectId, AsyncCallback<PositionsSelectItem[]> async);

    void getProjectAssigneesWithPositions(Integer projectID, AsyncCallback<HashMap<Integer, LinkedList<WfmTreeItem>>> callback);

    void getTaskMembers(Integer taskID, AsyncCallback<HashMap<Integer, LinkedList<WfmTreeItem>>> callback);

    void getOnlyAvailableAssigneesWithPosition1(Integer projectId, Date startDate, Date endDate, AsyncCallback<PositionsSelectItem[]> async);

    void getAssigneesWithPositions(Integer taskId, Integer projectId, AsyncCallback<ExistingAndNewTaskMembers> async);

    void getTask(Integer objectId, Boolean isFromCRM, AsyncCallback<TaskSingleItem> async);

    void getTaskForEdit(Integer objectId, AsyncCallback<EditTask> async);

    void getAssignments(Integer taskID, AsyncCallback<TaskInvolvedMember[]> async);

    void getTaskTimeEntries(Integer taskId, AsyncCallback<TaskTimeEntriesItem[]> async);

    void getTaskTimeEntriesList(ListingFilterParameter fp, AsyncCallback<ListResult<TaskTimeEntriesItem>> async);

    void checkAccess(Integer taskID, String permission, String context, AsyncCallback<Boolean> async);

    void getPermissions(Integer taskID, String context, AsyncCallback<HashSet<String>> async);

    void getPermissions(Integer taskID, String context, Integer userID, AsyncCallback<HashSet<String>> async);

    void createWorkstream(WorkstreamSingleItem newWorkstream, PositionProjectEmployeeIdTime assignees, AsyncCallback<Integer> async) throws NumberExistingException;

    void updateParentWorkstream(Integer workstreamID, Integer parentWorkstreamID, AsyncCallback<Integer> async);

    void getWorkstream(Integer objectId, AsyncCallback<WorkstreamSingleItem> async);

    void getWorkstreamSummary(Integer objectId, Boolean isWSSummary, AsyncCallback<WorkstreamSingleItem> async);

    void getTaskPositionsAsPSI(Integer taskID, AsyncCallback<PositionsSelectItem[]> async);

    void getRecursivelyPredecessors(Integer rootTaskId, AsyncCallback<String[]> async);

    void getRecursivelySuccessors(Integer rootTaskId, AsyncCallback<String[]> async);

    void deleteTask(Integer taskId, String context, AsyncCallback<String> async);

    void moveTimeEntries(ArrayList<TaskTimeEntriesItem> selectedTimeEntry, Integer projectId, Integer taskId, AsyncCallback<Void> async);

    void deleteTasks(ArrayList<Integer> taskIds, String context, AsyncCallback<String> async);

    void saveMultipleTask(MultiTaskList multiTaskList, AsyncCallback<Integer[]> async);

    void saveAttachments(Attachments attachments, AsyncCallback<Void> async);

    void getTaskNotes(Integer taskID, AsyncCallback<HistoryListItem[]> async);

    void getFirstLevelWorkstreams(Integer projectId, Integer workStreamID, AsyncCallback<WbsItem> callback);

    void getEditTaskStatusDrop(Integer taskId, AsyncCallback<SelectItem[]> async);

    void getTaskNoteComments(Integer noteID, AsyncCallback<NewsComment[]> callback);

    void saveTaskNoteComments(NewsComment data, AsyncCallback<NewsComment> callback);

    void deleteWorkstream(Integer workstreamID, Integer defaultWorkstreamID, boolean withAllTasksAndSUBW, AsyncCallback<Void> callback);

    void getWorkstreamsSomeParent(Integer parentWorkstreamID, AsyncCallback<SelectItem[]> callback);

    void sendEmailNotification(Integer taskID, AsyncCallback<Void> callback);

    void updateTaskAssignees(Integer taskId, IdTime[] assignees, AsyncCallback<Void> async);

    void deleteTask(Integer employeeID, Integer taskID, String deleteType, AsyncCallback<Boolean> callback);

    void calculateTaskBudgets(Integer taskID, AsyncCallback<Void> callback);

    void deleteNote(Integer id, AsyncCallback<Void> callback);

    void generateTaskNumber(Integer projectID, Date startdate, Integer objectID, AsyncCallback<NumberData> callback);

    void saveTaskEditCellValue(TaskListItem rowValue, String columnCodeName, AsyncCallback<Boolean> callback);

    void updateParentTask(Integer taskID, Integer parentWorkstreamID, AsyncCallback<Integer> async);

    void saveTaskAssignees(Integer taskId, IdTime[] assignees, AsyncCallback<Integer[]> async);

    void getProjectID(Integer taskID, AsyncCallback<Integer> async);

    void getProjectByTask(Integer taskID, AsyncCallback<SelectItem> async);

    void getTaskAttachments(Integer taskID, AsyncCallback<FileResource[]> async);

    void projectStartedAlready(Integer projectID, AsyncCallback<Boolean> async);

    void updateTasksStatus(HashSet<TaskListItem> tasks, SelectItem status, AsyncCallback<Void> async);

    void updateTaskStatus(Integer taskID, Integer statusID, String note, AsyncCallback<Void> async);

    void updateTasksPriority(HashSet<TaskListItem> tasks, SelectItem priority, AsyncCallback<Void> async);

    void changeWorkstream(ArrayList<Integer> tasks, Integer workstreamID, AsyncCallback<Void> async);

    void getAssigneesWithTreeInfoLinkedHashMapWithParams(LinkedHashMap<Integer, Integer> userIDs, Integer projectId, Integer basicTaskID, boolean selectMeOrOne, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> async);

    void getAssigneesWithTreeInfoLinkedHashMap(LinkedHashMap<Integer, Integer> userIDsWithStatus, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> callback);

    void getTaskMembersWithTreeInfo(Integer taskID, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> callback);

    void getTaskEditablePermission(Integer taskID, AsyncCallback<Integer> callback);

    void addAssigneesToTask(ArrayList<Integer> taskIDs, ArrayList<IdTime> assignees, AsyncCallback<Void> callback);

    void generateWorkstreamNumber(Integer projectID, Date startdate, Integer objectID, AsyncCallback<NumberData> callback);

    void getAssignEmployeeToProject(Integer projectId, String permission, AsyncCallback<Boolean> callback);

    void getTaskName(Integer taskId, AsyncCallback<String> callback);

    void setTimeToTimesheet(TimesheetDataItem dataItem, AsyncCallback<Boolean> callback);

    void getValidationData(Integer taskId, DateNonConvertable selectedDate, Integer employeeId, AsyncCallback<TimesheetDataItem> callback);

    void setTaskBillable(ArrayList<Integer> taskIDs, boolean b, AsyncCallback<Void> abstractAsyncCallback);

    void updateTasksProject(ArrayList<Integer> taskIds, Integer projectId, AsyncCallback<Void> callback) throws NumberExistingException;

    void updateTasksStartDate(ArrayList<Integer> taskIDs, Date date, AsyncCallback<Void> abstractAsyncCallback);

    void updateTasksDueDate(ArrayList<Integer> taskIDs, Date date, AsyncCallback<ArrayList<String>> async);

    void saveTask(TaskSingleItem newTask, Integer userID, AsyncCallback<Integer[]> callback);

    void getTaskLookUpItems(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    void getAllStatusHistories(Integer id, AsyncCallback<LogHistoryItem[]> callback);

    void getAllLogHistories(Integer id, AsyncCallback<LogHistoryItem[]> callback);

    void getTaskUpdatesList(ListingFilterParameter fp, AsyncCallback<ListResult<HistoryItem>> async);

}
