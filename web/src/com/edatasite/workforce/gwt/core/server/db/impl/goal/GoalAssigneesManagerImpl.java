package com.edatasite.workforce.gwt.core.server.db.impl.goal;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.goal.EdsGoalAssignees;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalAssigneesManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Sherali
 * Date: Oct 29, 2009
 * Time: 4:28:13 PM
 */
@Repository("goalAssigneesManager")
public class GoalAssigneesManagerImpl extends BaseManager<EdsGoalAssignees> implements GoalAssigneesManager {
    public GoalAssigneesManagerImpl() {
        super(EdsGoalAssignees.class);
    }

    @Override
    public Double getEmployeeWeightSum(EdsEmployee employee, Integer validityPeriodId) {
        if (validityPeriodId != null) {
            return (Double) findSingle("SELECT sum(ga.weight)from EdsGoalAssignees ga where ga.assignee=? and  ga.goal.validityPeriod is not null and ga.goal.validityPeriod.objectID=? and ga.deleted<>true", employee, validityPeriodId);
        }
        return null;
    }

    public void deleteGoalAssignees(Integer goalId) {
        update("update EdsGoalAssignees ga set ga.deleted = true where ga.goal.objectID=? and ga.deleted <> true", goalId);
    }

    @Override
    public EdsGoalAssignees getGoalAssigneeByDepartmentGoalIdAndEmployeeId(Integer departmentGoalId, Integer assigneeId) {
        return (EdsGoalAssignees) findSingle("SELECT ga from EdsGoalAssignees ga " +
                        "where ga.goal.objectID=? and ga.assignee.objectID=? and ga.deleted<>true",
                departmentGoalId, assigneeId);
    }

    @Override
    public List<EdsEmployee> getGoalAssignees(Integer departmentGoalId) {
        List<EdsEmployee> assginees = find("SELECT ga.assignee from EdsGoalAssignees ga where ga.goal.id=? and ga.deleted<>true", departmentGoalId);
        return assginees != null ? assginees : new ArrayList<>();
    }
}