package com.workforcetrack.api.presenter;


import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.workforcetrack.api.base.RestServiceUtils;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;
import com.workforcetrack.mobile.rpc.project.MProjectMember;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: shahob
 * Date: 29/08/12
 * Time: 15:14
 */
public class ProjectApiPresenter extends BaseApiPresenter {

    public Map<String, Object> convertToMapItem(EditProject item) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (item != null) {
            map.put(OBJECT_ID, item.getObjectId());
            map.put(PARENT_ID, item.getParentId());
            map.put(NUMBER, "N/A");
            if (item.getNumberData() != null) {
                MNumberData number = new MNumberData(item.getNumberData().getNumberString(), item.getNumberData().getIntNumber());
                map.put(NUMBER, number);
            }
            map.put(NAME, item.getName());
            map.put(DESCRIPTION, item.getDescription());
            map.put(COMPLETE, item.getComplete());
            map.put(CLIENT, item.getClientName());
            map.put(CLIENT_ID, item.getClientId());
            map.put(START_DATE, item.getStartDate());
            map.put(END_DATE, item.getEndDate());
            map.put(DUE_DATE, item.getDueDate());
            map.put(LAST_UPDATE, item.getLastUpdate());
            map.put(MANAGER_ID, item.getManagerId());
            map.put(MANAGER_NAME, item.getManagerName());
            map.put(BACKUP_MANAGER_ID, item.getBackupManagerId());
            map.put(BACKUP_MANAGER_IDS, item.getBackupManagerIDs());
            map.put(BACKUP_MANAGER_NAME, item.getBackupManagerName());
            map.put(STATUS_ID, item.getStatusId());
            map.put(LOCATION_ID, item.getLocationId());
            map.put(IS_COPY_NEW_EMPLOYEES_TO_PROJECT_TASKS, item.isCopyNewEmployeesToProjectTasks());
            map.put(COPY_NEW_EMPLOYEES_TO_PROJECT_TASKS_FIELD, Utils.hasGenericAccess(GenericSettingsEnum.IS_COPY_NEW_EMPLOYEES_TO_PROJECT_TASKS));
            map.put(MEMBERS, item.getMembers());
            map.put(CHANGE_TASK_STATUS, item.isChangeTaskStatus());

            if (item.getAttachments() != null && item.getAttachments().length > 0) {
                ArrayList<Map<String, Object>> list = new ArrayList<>();
                for (FileItem file : item.getAttachments()) {
                    Map<String, Object> attachment = new LinkedHashMap<>();
                    attachment.put(OBJECT_ID, file.getId());
                    attachment.put(NAME, file.getFileName());
                    attachment.put(DESCRIPTION, file.getDescription());
                    attachment.put(CONTENT_TYPE, file.getContentType());
                    attachment.put(FILE_SIZE, ServerUtils.getSizeAsString(file.getSize()));
                    attachment.put(URL, file.getDownloadUrl());
                    list.add(attachment);
                }
                map.put(ATTACHMENTS, list);
            }
        }
        return map;
    }

    public Map<String, Object> convertToMapListing(ProjectListItem item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, item.getObjectId());
        map.put(NUMBER, item.getNumber());
        map.put(NAME, item.getName());
        map.put(DESCRIPTION, item.getDescription());
        map.put(CLIENT, item.getClient());
        map.put(START_DATE, item.getStartDate());
        map.put(END_DATE, item.getEndDate());
        map.put(DUE_DATE, item.getDueDate());
        map.put(LAST_UPDATE, item.getLastUpdate());
        map.put(ESTIMATED_TIME, item.getEstimatedTime());
        map.put(BACKUP_MANAGER_NAME, item.getBackupManager());
        map.put(BACKUP_MANAGER_ID, item.getBackupManagerId());
        map.put(BACKUP_MANAGER_IDS, item.getBackupManagerIDs());
        map.put(MANAGER_NAME, item.getManager());
        map.put(MANAGER_ID, item.getManagerId());
        map.put(COMPLETE, item.getComplete());
        map.put(DEFAULT_PROJECTID, item.getDefaultProjectId());
        map.put(TEAMS, item.getTeams());
        map.put(TASK_COUNT, item.getTaskCount());
        map.put(STATUS_ID, item.getStatusId());
        map.put(STATUS_NAME, item.getStatus());
        map.put(PROJECT_LOCATION, item.getProjectLocation());
        return map;
    }

    public Map<String, Object> convertToMapListing(ListResult<ProjectListItem> items) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        if (items.getTotal() != null && items.getTotal() > 0) {
            for (ProjectListItem projectItem : items.getList()) {
                list.add(convertToMapListing(projectItem));
            }
        }
        map.put(TOTAL_COUNT, items.getTotal());
        map.put(ITEMS, list);
        return map;
    }

    public ProjectSingleItem convertToProjectSingleItem(Map<String, Object> map) throws ParseException, ClassCastException, NumberFormatException {
        ProjectSingleItem projectListItem = new ProjectSingleItem();
        projectListItem.setName((String) map.get(NAME));

        projectListItem.setClientId(map.get(CLIENT_ID) == null ? 0 : (Integer) map.get(CLIENT_ID));

        SimpleDateFormat dateFormat = new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT);
        projectListItem.setStartDate((map.get(START_DATE) != null) ? dateFormat.parse((String) map.get(START_DATE)) : null);
        projectListItem.setEndDate((map.get(END_DATE) != null) ? dateFormat.parse((String) map.get(END_DATE)) : null);

        if (map.get(MANAGER_ID) != null) {
            projectListItem.setManagerId((Integer) map.get(MANAGER_ID));
        }
        if (map.get(STATUS_ID) != null) {
            projectListItem.setStatusId((Integer) map.get(STATUS_ID));
        }
        projectListItem.setDescription((String) map.get(DESCRIPTION));
        if (map.get(BACKUP_MANAGER_ID) != null) {
            projectListItem.setBackupManagerId((Integer) map.get(BACKUP_MANAGER_ID));
            ArrayList<Integer> Ids = new ArrayList<>();
            Ids.add((Integer) map.get(BACKUP_MANAGER_ID));
            projectListItem.setBackupManagerIDs(Ids);
        }
        projectListItem.setParentId((Integer) map.get(PARENT_ID));
        if (map.get(MEMBERS) != null) {
            List<Map<String, Object>> projectMembers = (List<Map<String, Object>>) map.get(MEMBERS);
            if (projectMembers.size() > 0) {
                ArrayList<ProjectMember> members = new ArrayList<>();
                MProjectMember t;
                for (Map<String, Object> member : projectMembers) {
                    ProjectMember item = new ProjectMember();
                    item.setId((Integer) member.get("id"));
                    item.setName((String) member.get(NAME));
                    item.setClientChargeRate((Double) member.get("clientChargeRate"));
                    item.setDepartmentId((Integer) member.get("departmentId"));
                    item.setWageRate((Double) member.get("wageRate"));
                    if (member.get("workloadPercentage") != null) {
                        item.setWorkloadPercentage((Float) member.get("workloadPercentage"));
                    }
                    members.add(item);
                }
                projectListItem.setProjectMembers(members.toArray(new ProjectMember[]{}));
            }
        }

        return projectListItem;
    }

    public boolean convertToEditProject(Map<String, Object> map, EditProject projectItem) {
        try {
            if (map != null && !map.isEmpty()) {
                projectItem.setObjectId((Integer) map.get(OBJECT_ID));
                projectItem.setParentId((Integer) map.get(PARENT_ID));
                projectItem.setName((String) map.get(NAME));
                projectItem.setManagerName((String) map.get(MANAGER_NAME));
                projectItem.setManagerId((Integer) map.get(MANAGER_ID));
                projectItem.setBackupManagerId((Integer) map.get(BACKUP_MANAGER_ID));
                projectItem.setBackupManagerIDs((ArrayList) map.get(BACKUP_MANAGER_IDS));
                projectItem.setBackupManagerName((String) map.get(BACKUP_MANAGER_NAME));
                projectItem.setClientId((Integer) map.get(CLIENT_ID));
                projectItem.setClientName((String) map.get(CLIENT));
                projectItem.setComplete((String) map.get(COMPLETE));
                projectItem.setStatusId((Integer) map.get(STATUS_ID));
                projectItem.setDescription((String) map.get(DESCRIPTION));
                projectItem.setLocationId((Integer) map.get(LOCATION_ID));

                SimpleDateFormat dateFormat = new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT);
                projectItem.setStartDate((map.get(START_DATE) != null) ? dateFormat.parse((String) map.get(START_DATE)) : null);
                projectItem.setEndDate((map.get(END_DATE) != null) ? dateFormat.parse((String) map.get(END_DATE)) : null);
                projectItem.setDueDate((map.get(DUE_DATE) != null) ? dateFormat.parse((String) map.get(DUE_DATE)) : null);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}