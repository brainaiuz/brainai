package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 30, 2009
 * Time: 5:15:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimesheetProjectItem extends SelectItem {
    private String projectManager;
    private String backupManager;

    public TimesheetProjectItem() {
        super();
    }

    public TimesheetProjectItem(Integer id, String name) {
        super(id, name);
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getBackupManager() {
        return backupManager;
    }

    public void setBackupManager(String backupManager) {
        this.backupManager = backupManager;
    }
}
