package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Farhod
 * Date: 28/10/11
 * Time: 12:16
 * To change this template use File | Settings | File Templates.
 */
public class DashboardDownloadLinkRpc implements IsSerializable {
    private Integer id;
    private ReportRpc report;
    private FolderRpc folder;
    private DashboardRpc dashboard;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ReportRpc getReport() {
        return report;
    }

    public void setReport(ReportRpc report) {
        this.report = report;
    }

    public FolderRpc getFolder() {
        return folder;
    }

    public void setFolder(FolderRpc folder) {
        this.folder = folder;
    }

    public DashboardRpc getDashboard() {
        return dashboard;
    }

    public void setDashboard(DashboardRpc dashboard) {
        this.dashboard = dashboard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
