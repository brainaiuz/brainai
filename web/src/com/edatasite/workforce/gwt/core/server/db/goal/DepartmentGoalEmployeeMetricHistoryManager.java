package com.edatasite.workforce.gwt.core.server.db.goal;

import com.edatasite.workforce.core.domain.goal.EdsDepartmentGoalEmployeeMetricHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

public interface DepartmentGoalEmployeeMetricHistoryManager extends Manager<EdsDepartmentGoalEmployeeMetricHistory> {

    void deleteEmployeeMetricHistoryById(Integer id);

    void deleteEmployeeMetricHistoriesByDepartmentGaolId(Integer departmentGoalId);


    Integer getTotalCount(ListingFilterParameter filterParameter);

    EdsDepartmentGoalEmployeeMetricHistory getEmployeeMetricHistoryById(Integer id);

    List<EdsDepartmentGoalEmployeeMetricHistory> getList(ListingFilterParameter filterParameter);

    Double getActualTotalByGoalAssigneeIdAndEmployeeId(Integer goalAssigneesObjectID, Integer employeeId);

    Date getMinEntryDateByGoalId(Integer goalId);

    Date getMaxEntryDateByGoalId(Integer goalId);

    /**
     * Lightweight projection (date, actual) of all metric-history entries for a goal.
     * Used by the department-goal chart: avoids hydrating full entities and the
     * per-row lazy fetch of the assignee that the full toRpc() mapping triggers.
     */
    List<Object[]> getChartDataForGoal(Integer goalId);
}
