package com.edatasite.workforce.gwt.project.server.app;

import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmTreeItemFactory;

public class WorkStreamItemFactory extends WfmTreeItemFactory<EdsWorkStream> {

    public WbsItem createItem(EdsWorkStream workstream) {
        WbsItem item = new WbsItem(workstream.getObjectID(), workstream.getName(), WbsItem.WORKSTREAM);
        item.setChildren(!workstream.getSubWorkStreams().isEmpty() || !workstream.getTasks().isEmpty());
//          String[] assignees = new String[workstream.getAssignees().size()];
//                int i = 0;
//                for (EdsEmployeeWorkstream assignee : workstream.getAssignees()) {
//                    if (assignee.getProjectEmployee() != null && assignee.getProjectEmployee().getEmployeeDepartment() != null &&
//                            assignee.getProjectEmployee().getEmployeeDepartment().getEmployee() != null) {
//                        EdsEmployee employee = assignee.getProjectEmployee().getEmployeeDepartment().getEmployee();
//                        assignees[i] = employee.getName();
//                    }
//                    i++;
//                }
//        item.setAssignees(assignees);
        item.setStartDate(ServerUtils.getDate(workstream.getStartDate()));
        item.setEndDate(ServerUtils.getDate(workstream.getEndDate()));
        return item;
    }
}