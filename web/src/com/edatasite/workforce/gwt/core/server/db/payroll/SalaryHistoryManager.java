package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsSalaryHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SalaryHistoryManager extends Manager<EdsSalaryHistory> {

    List<SalaryHistory> getByEmployee(ListingFilterParameter filterParameter);

    EdsSalaryHistory getEmployeeExactSalaryHistory(SalaryHistory salaryHistory);

    BigDecimal getEmployeeLastSalaryHistory(Integer employeeId, Date date);

    List<SalaryHistory> getEmployeeSalaryHistory(ListingFilterParameter lp);

    List<EdsSalaryHistory> getHistories(Integer relationId, String relationType);

    void deleteHistory(Integer relationId, String relationType);

    default HashMap<Integer, List<SalaryHistory>> getEmployeeSalaryHistoryMap(ListingFilterParameter lp){
        final HashMap<Integer, List<SalaryHistory>> result = new HashMap<>();
        final List<SalaryHistory> list = this.getEmployeeSalaryHistory(lp);

        for (SalaryHistory history : list) {

            if (history.getEmployeeId() == null) {
                continue;
            }
            List<SalaryHistory> histories = result.getOrDefault(history.getEmployeeId(), new ArrayList<>());
            histories.add(history);
            result.put(history.getEmployeeId(), histories);
        }
        return result;
    }

    HashMap<Integer, BigDecimal> getEmployeeSalaryMap(List<Integer> employeeIds, Date date);

    Map<Integer, BigDecimal> getEmployeeLastSalaryHistoryMap(List<Integer> employeeIds, Date date);

}
