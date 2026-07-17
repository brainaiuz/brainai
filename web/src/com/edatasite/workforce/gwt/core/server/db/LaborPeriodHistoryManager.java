package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLaborPeriodHistory;

import java.util.List;

public interface LaborPeriodHistoryManager extends Manager<EdsLaborPeriodHistory> {
    List<EdsLaborPeriodHistory> getComments(Integer periodId);

    void clearHistory(Integer employeeId);
}
