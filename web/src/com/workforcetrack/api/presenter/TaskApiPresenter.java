package com.workforcetrack.api.presenter;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 06/09/12
 * Time: 19:29
 * To change this template use File | Settings | File Templates.
 */
public class TaskApiPresenter extends BaseApiPresenter {

    public Map<String, Object> convertToMap(ArrayList<Appointment> items) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        if (items != null && items.size() > 0) {
            for (Appointment appointment : items) {
                list.add(convertToMap(appointment));
            }
            map.put(TOTAL_COUNT, list.size());
            map.put(ITEMS, list);
        }
        return map;
    }

    public Map<String, Object> convertToMap(Appointment item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, item.getObjectID());
        map.put(NAME, item.getSubject());
        map.put(DESCRIPTION, item.getDescription());
        map.put(START_DATE, item.getStartDate());
        map.put(END_DATE, item.getEndDate());
        map.put(COMPANY_NAME, item.getOrganizationName());

        return map;
    }

    public Map<String, Object> convertToMapItem(EditTask item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, item.getObjectID());
        map.put(QUICKBOOK_TASK_ID, item.getQuickbookTaskID());
        map.put(QUICKBOOK_EDIT_SEQUENCE, item.getQuickbookEditSequence());
        map.put(NUMBER_DATA, item.getNumberData());
        map.put(NUMBER, item.getNumberData() != null? item.getNumberData().getNumberString(): "");
        map.put(NAME, item.getName());
        map.put(DESCRIPTION, item.getDescription());
        map.put(PRIORITY_ID, item.getPriorityId());
        map.put(PERCENT, item.getPercent());
        map.put(START_DATE, item.getStartDate());
        map.put(END_DATE, item.getEndDate());
        map.put(DUE_DATE, item.getDueDate());
        map.put(ESTIMATED_TIME, item.getEstimatedTime());
        map.put(STATUS_ID, item.getStatusId());
        map.put(ASSIGNEES, item.getAssignees());
        map.put(MY_SELF, item.isMySelf());
        map.put(PARENT_WS_ITEM, item.getParentWSItem());
        map.put(PROJECT_ID, item.getProjectId());
        map.put(PROJECT_NAME, item.getProjectName());
        map.put(PERMISSION, item.getPermission());
        map.put(IS_EMPLOYEE_TASK, item.isEmployeeTask());
        map.put(EMPLOYEE_TASK_ID, item.getEmployeeTaskID());
        map.put(EMPLOYEE_ID, item.getEmployeeID());
        map.put(BILLABLE, item.getBillable());
        map.put(FOLDER_ID, item.getFolderId());
        map.put(ALL_DAY, item.isAllDay());
        map.put(PROJECTS, toMSelectItemList(item.getProjects()));

        return map;
    }

    public Map<String, Object> convertToMapListing(List<TaskListItem> items) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (TaskListItem taskItem : items) {
            list.add(convertToMapListing(taskItem));
        }
        map.put(TOTAL_COUNT, list.size());
        map.put(ITEMS, list);
        return map;
    }

    public Map<String, Object> convertToMapListing(TaskListItem item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, item.getObjectID());
        map.put(NAME, item.getName());
        map.put(NUMBER, item.getNumber());
        map.put(DESCRIPTION, item.getDescription());
        map.put(STATUS_ID, item.getTaskStatusId());
        map.put(STATUS_NAME, item.getStatusName());
        map.put(PRIORITY_NAME, item.getPriorityName());
        map.put(PROJECT_ID, item.getProjectId());
        map.put(PROJECT_NAME, item.getProjectName());
        map.put(LAST_MODIFIED_BY, item.getLastModifiedBy());
        map.put(LAST_MODIFIED, item.getLastModified());
        map.put(START_DATE, item.getStartDate());
        map.put(END_DATE, item.getEndDate());
        map.put(DUE_DATE, item.getDueDate());
        map.put(ASSIGNED_TO, item.getAssignedTo());
        map.put(COMPLETE, item.getComplete());
        map.put(CLIENT, item.getClient());
        map.put(PRIORITY_ID, item.getPriorityId());
        map.put(ESTIMATED, item.getEstimated());
        map.put(BILLABLE, item.isBillable());
        map.put(PARENT_WORKSTREAM_NAME, item.getParentWorkstreamName());
        map.put(MANAGER_ID, item.getProjectManagerID());
        map.put(BACKUP_MANAGER_ID, item.getProjectBackupManagerID());
        map.put(BACKUP_MANAGER_IDS, item.getProjectBackupManagerIDs());
        map.put(TASK_CREATOR_ID, item.getTaskCreatorID());
        return map;
    }

    public Map<String, Object> convertToMap(List<TaskListItem> items) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (TaskListItem taskItem : items) {
            list.add(convertToMap(taskItem));
        }
        map.put(TOTAL_COUNT, list.size());
        map.put(ITEMS, list);
        return map;
    }

    public Map<String, Object> convertToMap(TaskListItem item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, item.getObjectID());
        map.put(NAME, item.getName());
        map.put(NUMBER, item.getNumber());
        map.put(DESCRIPTION, item.getDescription());
        map.put(STATUS_NAME, item.getStatusName());
        map.put(PRIORITY_NAME, item.getPriorityName());
        map.put(PROJECT_ID, item.getProjectId());
        map.put(PROJECT_NAME, item.getProjectName());
        map.put(LAST_MODIFIED_BY, item.getLastModifiedBy());
        map.put(LAST_MODIFIED, item.getLastModified());
        map.put(START_DATE, item.getStartDate());
        map.put(END_DATE, item.getEndDate());
        map.put(DUE_DATE, item.getDueDate());
        map.put(ASSIGNED_TO, item.getAssignedTo());
        map.put(COMPLETE, item.getComplete());
        map.put(ACTUAL_HOURS_SPENT, item.getActualHoursSpent());
        map.put(NEW_TASK, item.isMarked());
        map.put(CLIENT, item.getClient());
        map.put(HIGH_LITE, item.getHighlite());
        map.put(STATUS_ID, item.getTaskStatusId());
        map.put(PRIORITY_ID, item.getPriorityId());
        map.put(ESTIMATED, item.getEstimated());
        map.put(BILLABLE, item.isBillable());
        map.put(GOOGLE_ID, item.getGoogleID());
        map.put(PARENT_WORKSTREAM_NAME, item.getParentWorkstreamName());
        map.put(ALL_DAY, item.isAllDay());
        map.put(MANAGER_ID, item.getProjectManagerID());
        map.put(BACKUP_MANAGER_ID, item.getProjectBackupManagerID());
        map.put(BACKUP_MANAGER_IDS, item.getProjectBackupManagerIDs());
        map.put(TASK_CREATOR_ID, item.getTaskCreatorID());
        return map;
    }

}
