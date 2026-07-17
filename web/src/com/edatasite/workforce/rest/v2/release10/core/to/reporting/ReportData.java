package com.edatasite.workforce.rest.v2.release10.core.to.reporting;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import org.json.simple.JSONObject;

public class ReportData extends ResponseData {
    private Integer reportId;
    private String reportType;
    private String reportLink;
    private JSONObject reportData;

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getReportLink() {
        return reportLink;
    }

    public void setReportLink(String reportLink) {
        this.reportLink = reportLink;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public JSONObject getReportData() {
        return reportData;
    }

    public void setReportData(JSONObject reportData) {
        this.reportData = reportData;
    }
}
