package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReportingExecuteTime;
import com.edatasite.workforce.gwt.core.server.db.ReportingExecuteTimeManager;
import org.springframework.stereotype.Repository;

@Repository("reportingExecuteTimeManager")
public class ReportingExecuteTimeImpl extends BaseManager<EdsReportingExecuteTime> implements ReportingExecuteTimeManager {
    public ReportingExecuteTimeImpl() {
        super(EdsReportingExecuteTime.class);
    }
}
