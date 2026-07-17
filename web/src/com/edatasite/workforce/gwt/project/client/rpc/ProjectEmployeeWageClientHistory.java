package com.edatasite.workforce.gwt.project.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: iabdullo
 * Date: 15.09.14 11:59
 */
public class ProjectEmployeeWageClientHistory implements IsSerializable {
    private ProjectEmployeeWageClientHistoryItem[] hist;
    private Integer projectEmployeeId;
    private Integer projectId;

    public ProjectEmployeeWageClientHistoryItem[] getHist() {
        return hist;
    }

    public void setHist(ProjectEmployeeWageClientHistoryItem[] hist) {
        this.hist = hist;
    }

    public Integer getProjectEmployeeId() {
        return projectEmployeeId;
    }

    public void setProjectEmployeeId(Integer projectEmployeeId) {
        this.projectEmployeeId = projectEmployeeId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
