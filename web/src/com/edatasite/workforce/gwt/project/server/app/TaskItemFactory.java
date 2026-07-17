package com.edatasite.workforce.gwt.project.server.app;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmTreeItemFactory;
import org.springframework.context.support.WfmMessageSource;

public class TaskItemFactory extends WfmTreeItemFactory<EdsTask> {

    public WbsItem createItem(EdsTask task) {
        WbsItem item = new WbsItem(task.getObjectID(), task.getName(), WbsItem.TASK);
        String[] assignees = new String[task.getUnDeletedAssignments().size()];
        int i = 0;
        for (EdsEmployeeTask assignee : task.getUnDeletedAssignments()) {
            if (assignee.getProjectEmployee() != null && assignee.getProjectEmployee().getEmployeeDepartment() != null &&
                    assignee.getProjectEmployee().getEmployeeDepartment().getEmployee() != null) {
                EdsEmployee employee = assignee.getProjectEmployee().getEmployeeDepartment().getEmployee();
                assignees[i] = employee.getName();
                i++;
            }
        }
        item.setAssignees(assignees);
//        item.setPriorityName(ServerUtils.getName(task.getPriority()));
//		item.setStatusName(ServerUtils.getName(task.getStatus()));
        item.setStartDate(ServerUtils.getDate(task.getStartDate()));
        item.setEndDate(ServerUtils.getDate(task.getDueDate()));
        item.setDescription(task.getDescription());
        item.setNumberData(task.getNumber());
        return item;
    }

    public WfmTreeItem createItem(EdsTask task, WfmMessageSource wfmMessageSource) {
        WbsItem item = new WbsItem(task.getObjectID(), task.getName(), WbsItem.TASK);
        String[] assignees = new String[task.getUnDeletedAssignments().size()];
        int i = 0;
        for (EdsEmployeeTask assignee : task.getUnDeletedAssignments()) {
            if (assignee.getProjectEmployee() != null && assignee.getProjectEmployee().getEmployeeDepartment() != null &&
                    assignee.getProjectEmployee().getEmployeeDepartment().getEmployee() != null) {
                EdsEmployee employee = assignee.getProjectEmployee().getEmployeeDepartment().getEmployee();
                assignees[i] = employee.getName();
                i++;
            }
        }
        item.setAssignees(assignees);
        item.setPriorityName(task.getPriority() != null ? wfmMessageSource.localize(task.getPriority().getCode(), task.getPriority().getName()) : "N/A");
        item.setStatusName(task.getStatus() != null ? wfmMessageSource.localize(task.getStatus().getCode(), task.getStatus().getName()) : "N/A");
        item.setTaskPercent(task.getPercent());
        item.setEstimated(task.getEstimatedTime());
        item.setStartDate(ServerUtils.getDate(task.getStartDate()));
        item.setEndDate(ServerUtils.getDate(task.getDueDate()));
        item.setDescription(task.getDescription());
        item.setTimeSpent(ServerUtils.getTimeSpentHM(task.getOverAllTimeSpent(task)));
        return item;
    }
}