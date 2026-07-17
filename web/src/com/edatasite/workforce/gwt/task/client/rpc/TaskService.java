package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 26, 2007
 * Time: 6:37:14 PM
 */
@RemoteServiceRelativePath("rpc/task")
public interface TaskService extends RemoteService {

    void updateTask(EditTask task) throws NumberExistingException;

    Integer[] saveTaskWithNewProjectEmployees(TaskSingleItem newTask) throws NumberExistingException;

    Integer[] saveTask(TaskSingleItem newTask) throws NumberExistingException;

    String setAssignees(Integer taskID, WfmTreeItem[] assignees);

    FileResource[] getTaskAttachments(Integer taskID);

    SelectItem[] getPriorities();

    SelectItem[] getTaskTypes();

    TaskList getTaskList(ListingFilterParameter filterParameter);

    ListResult<TaskListItem> getNewKanbanTasks(ListingFilterParameter filterParameter, SelectItem columnMetadata);

    LinkedHashMap<Integer, Long> getNewKanbanTasksCounts(ArrayList<Integer> columnIds);

    void changeTaskKanbanOrder(SelectItem columnLayoutData, Integer item, Integer prevItem, Integer afterItem);

    BudgetItem[] getTaskBudget(Integer taskId);

    TaskSelectItemList getLatestTasks(Integer projectId, ListLoadConfig config, Integer[] taskIds, boolean includeParentExistSubtasks);

    TaskSelectItemList searchTasks(Integer projectId, String keyword, ListingFilterParameter fp, ListLoadConfig config, Integer[] taskIds, boolean includeParentExistSubtasks);

    SelectItem[] getAssignees(Integer projectId);

    PositionsSelectItem[] getAssigneesWithPositions1(Integer projectId);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getAssigneesWithTreeInfoLinkedHashMapWithParams(LinkedHashMap<Integer, Integer> userIDs, Integer projectId, Integer basicTaskID, boolean selectMeOrOne);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getAssigneesWithTreeInfoLinkedHashMap(LinkedHashMap<Integer, Integer> userIDsWithStatus);

    Integer getTaskEditablePermission(Integer taskID);

    void addAssigneesToTask(ArrayList<Integer> taskIDs, ArrayList<IdTime> assignees);

    HashMap<Integer, LinkedList<WfmTreeItem>> getProjectAssigneesWithPositions(Integer projectID);

    HashMap<Integer, LinkedList<WfmTreeItem>> getTaskMembers(Integer taskID);

    PositionsSelectItem[] getOnlyAvailableAssigneesWithPosition1(Integer projectId, Date startDate, Date endDate);

    ExistingAndNewTaskMembers getAssigneesWithPositions(Integer taskId, Integer projectId);

    TaskSingleItem getTask(Integer objectId, Boolean isFromCrm);

    EditTask getTaskForEdit(Integer objectId);

    TaskInvolvedMember[] getAssignments(Integer taskID);

    Boolean checkAccess(Integer taskID, String permission, String context);

    HashSet<String> getPermissions(Integer taskID, String context);

    HashSet<String> getPermissions(Integer taskID, String context, Integer userID);

    TaskTimeEntriesItem[] getTaskTimeEntries(Integer taskId);

    ListResult<TaskTimeEntriesItem> getTaskTimeEntriesList(ListingFilterParameter fp);

    Integer createWorkstream(WorkstreamSingleItem newWorkstream, PositionProjectEmployeeIdTime assignees) throws NumberExistingException;

    Integer updateParentTask(Integer taskID, Integer parentWorkstreamID);

    Integer updateParentWorkstream(Integer workstreamID, Integer parentWorkstreamID);

    WorkstreamSingleItem getWorkstream(Integer objectId);

    WorkstreamSingleItem getWorkstreamSummary(Integer objectId, Boolean isWSSummary);

    PositionsSelectItem[] getTaskPositionsAsPSI(Integer taskID);

    Integer[] saveTaskAssignees(Integer taskId, IdTime[] assignees) throws NumberExistingException;

    void updateTaskAssignees(Integer taskId, IdTime[] assignees);

    String[] getRecursivelyPredecessors(Integer rootTaskId);

    String[] getRecursivelySuccessors(Integer rootTaskId);

    String deleteTask(Integer taskId, String context);

    void moveTimeEntries(ArrayList<TaskTimeEntriesItem> selectedTimeEntry, Integer projectId, Integer taskId);

    String deleteTasks(ArrayList<Integer> taskIds, String context);

    Integer[] saveMultipleTask(MultiTaskList multiTaskList);

    void saveAttachments(Attachments attachments);

    HistoryListItem[] getTaskNotes(Integer taskID);

    WbsItem getFirstLevelWorkstreams(Integer projectId, Integer workStreamID);

    SelectItem[] getEditTaskStatusDrop(Integer taskId);

    NewsComment[] getTaskNoteComments(Integer noteID);

    NewsComment saveTaskNoteComments(NewsComment data);

    void deleteWorkstream(Integer workstreamID, Integer defaultWorkstreamID, boolean withAllTasksAndSUBW);

    SelectItem[] getWorkstreamsSomeParent(Integer parentWorkstreamID);

    void sendEmailNotification(Integer taskID);

    Boolean deleteTask(Integer employeeID, Integer taskID, String deleteType);

    void calculateTaskBudgets(Integer taskID);

    void deleteNote(Integer id);

    NumberData generateTaskNumber(Integer projectID, Date startdate, Integer objectID);

    boolean saveTaskEditCellValue(TaskListItem rowValue, String columnCodeName);

    Integer getProjectID(Integer taskID);

    SelectItem getProjectByTask(Integer taskID);

    Boolean projectStartedAlready(Integer projectID);

    void updateTasksStatus(HashSet<TaskListItem> tasks, SelectItem status);

    void updateTaskStatus(Integer taskID, Integer statusID, String note);

    void updateTasksPriority(HashSet<TaskListItem> tasks, SelectItem priority);

    void changeWorkstream(ArrayList<Integer> tasks, Integer workstreamID);

    NumberData generateWorkstreamNumber(Integer projectID, Date startdate, Integer objectID);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getTaskMembersWithTreeInfo(Integer taskID);

    Boolean getAssignEmployeeToProject(Integer projectId, String permission);

    String getTaskName(Integer taskID);

    Boolean setTimeToTimesheet(TimesheetDataItem dataItem);

    TimesheetDataItem getValidationData(Integer taskID, DateNonConvertable selectedDate, Integer employeeId);

    void setTaskBillable(ArrayList<Integer> taskIDs, boolean b);

    void updateTasksProject(ArrayList<Integer> taskIds, Integer projectId) throws NumberExistingException;

    void updateTasksStartDate(ArrayList<Integer> taskIDs, Date date);

    ArrayList<String> updateTasksDueDate(ArrayList<Integer> taskIDs, Date date);

    Integer[] saveTask(TaskSingleItem newTask, Integer userID) throws NumberExistingException;

    SelectItem[] getTaskLookUpItems(ListingFilterParameter filterParameter);

    LogHistoryItem[] getAllStatusHistories(Integer id);

    LogHistoryItem[] getAllLogHistories(Integer id);

    ListResult<HistoryItem> getTaskUpdatesList(ListingFilterParameter fp);

    class App {
        public static TaskServiceAsync get() {
            ServiceDefTarget target = GWT.create(TaskService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/task");
            return (TaskServiceAsync) target;
        }
    }

}
