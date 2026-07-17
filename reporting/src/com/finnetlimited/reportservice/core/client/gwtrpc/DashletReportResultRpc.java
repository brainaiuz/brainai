package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek
 * Date: 4/24/12
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DashletReportResultRpc implements IsSerializable {
    private DashletRpc dashletRpc;
    private ReportGenerateTableRpc reportResult;

    public DashletReportResultRpc() {
    }

    public DashletReportResultRpc(DashletRpc dashlet, ReportGenerateTableRpc reportGenerateTableRpc) {
        dashletRpc = dashlet;
        reportResult = reportGenerateTableRpc;
    }


    public DashletRpc getDashletRpc() {
        return dashletRpc;
    }

    public void setDashletRpc(DashletRpc dashletRpc) {
        this.dashletRpc = dashletRpc;
    }

    public ReportGenerateTableRpc getReportResult() {
        return reportResult;
    }

    public void setReportResult(ReportGenerateTableRpc reportResult) {
        this.reportResult = reportResult;
    }
}
