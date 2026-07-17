package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 17.06.2009
 * Time: 17:59:29
 * To change this template use File | Settings | File Templates.
 */
public class ProjectDashboardResult implements IsSerializable {

    private String projectIds;
    private ProjectsReportResult[] projectReport;
    private int total;

    public ProjectDashboardResult() {
    }

    public ProjectDashboardResult(String projectIds, ProjectsReportResult[] projectReport) {
        this.projectIds = projectIds;
        this.projectReport = projectReport;
    }

    public String getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(String projectIds) {
        this.projectIds = projectIds;
    }

    public ProjectsReportResult[] getProjectReport() {
        return projectReport;
    }

    public void setProjectReport(ProjectsReportResult[] projectReport) {
        this.projectReport = projectReport;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ListData getListData() {
        return new ListData(projectReport, total);
    }
}
