package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: ${Dilsh0d}
 * Date: 30.10.2009
 * Time: 13:27:32
 * To change this template use File | Settings | File Templates.
 */
public class PMOverallRps implements IsSerializable {

    private String projectIds;
    private ProjectCostRps projectCost;
    private ProjectsReportResult[] projectReport;

    public String getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(String projectIds) {
        this.projectIds = projectIds;
    }

    public ProjectCostRps getProjectCost() {
        return projectCost;
    }

    public void setProjectCost(ProjectCostRps projectCost) {
        this.projectCost = projectCost;
    }

    public ProjectsReportResult[] getProjectReport() {
        return projectReport;
    }

    public void setProjectReport(ProjectsReportResult[] projectReport) {
        this.projectReport = projectReport;
    }
}
