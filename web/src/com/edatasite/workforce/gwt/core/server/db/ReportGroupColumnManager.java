package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsReportGroupColumn;

import java.util.List;

public interface ReportGroupColumnManager extends Manager<EdsReportGroupColumn> {
    List<EdsReportGroupColumn> getGroupColumnByReportCode(String reportCode);

    void deleteByReportCode(String reportCode);

}
