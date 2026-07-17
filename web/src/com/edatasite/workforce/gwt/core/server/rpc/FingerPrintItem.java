package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 8/31/15 6:26 PM
 */
public class FingerPrintItem implements Serializable {
    private String companyUniqueKey;
    private Integer projectId;
    private Integer taskId;
    private List<FingerPrintUserItem> users;

    public String getCompanyUniqueKey() {
        return companyUniqueKey;
    }

    public void setCompanyUniqueKey(String companyUniqueKey) {
        this.companyUniqueKey = companyUniqueKey;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public List<FingerPrintUserItem> getUsers() {
        return users;
    }

    public void setUsers(List<FingerPrintUserItem> users) {
        this.users = users;
    }
}
