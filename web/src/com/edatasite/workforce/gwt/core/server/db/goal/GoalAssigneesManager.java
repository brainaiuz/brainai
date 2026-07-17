package com.edatasite.workforce.gwt.core.server.db.goal;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.goal.EdsGoalAssignees;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 29, 2009
 * Time: 4:27:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GoalAssigneesManager extends Manager<EdsGoalAssignees> {

    Double getEmployeeWeightSum(EdsEmployee employee, Integer validityPeriodId);

    void deleteGoalAssignees(Integer goalId);

    EdsGoalAssignees getGoalAssigneeByDepartmentGoalIdAndEmployeeId(Integer departmentGoalId, Integer objectID);

    List<EdsEmployee> getGoalAssignees(Integer departmentGoalId);
}
