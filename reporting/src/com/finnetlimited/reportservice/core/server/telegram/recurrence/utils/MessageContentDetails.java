package com.finnetlimited.reportservice.core.server.telegram.recurrence.utils;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;

import java.sql.ResultSet;

public class MessageContentDetails {
    private final ReportRpc reportRpc;
    private final EdsReport edsReport;
    private final Integer companyId;
    private final EdsUser edsUser;
    private final ResultSet resultSet;

    public MessageContentDetails(ReportRpc reportRpc, EdsReport edsReport, Integer companyId, EdsUser user, ResultSet resultSet) {
        this.reportRpc = reportRpc;
        this.edsReport = edsReport;
        this.companyId = companyId;
        this.edsUser = user;
        this.resultSet = resultSet;
    }

    public ReportRpc getReportRpc() {
        return reportRpc;
    }

    public EdsReport getEdsReport() {
        return edsReport;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public EdsUser getEdsUser() {
        return edsUser;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }
}
