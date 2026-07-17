package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;

public interface SalaryHistoryLocal {

    Integer save(SalaryHistory salaryHistory);

    Boolean delete(Integer relationId, String relationtype);
}
