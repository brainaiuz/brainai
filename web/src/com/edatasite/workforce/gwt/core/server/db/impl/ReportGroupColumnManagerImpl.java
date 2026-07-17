package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReportGroupColumn;
import com.edatasite.workforce.gwt.core.server.db.ReportGroupColumnManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("reportGroupColumnManager")
public class ReportGroupColumnManagerImpl extends BaseManager<EdsReportGroupColumn> implements ReportGroupColumnManager {

    public ReportGroupColumnManagerImpl() {
        super(EdsReportGroupColumn.class);
    }

    public List<EdsReportGroupColumn> getGroupColumnByReportCode(String reportCode) {
        return find("FROM EdsReportGroupColumn where reportCode =?", reportCode);
    }

    @Override
    public void deleteByReportCode(String reportCode) {
        update("delete from EdsReportGroupColumn reportGroup where reportGroup.reportCode = ?", reportCode);
    }
}
