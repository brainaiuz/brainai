package com.workforcetrack.mobile.rpc.timesheet;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.timesheet.ProjectTaskForMobile;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Apr 30, 2010
 * Time: 5:19:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectTreeForMobile extends SelectItem {

    private ArrayList<ProjectTaskForMobile> tasks;

    public ProjectTreeForMobile() {

    }

    public ProjectTreeForMobile(Integer id, String name, ArrayList<ProjectTaskForMobile> tasks) {
        super(id, name);
        this.tasks = tasks;
    }

    public ArrayList<ProjectTaskForMobile> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<ProjectTaskForMobile> tasks) {
        this.tasks = tasks;
    }
}
