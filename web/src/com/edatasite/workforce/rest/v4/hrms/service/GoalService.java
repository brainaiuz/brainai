package com.edatasite.workforce.rest.v4.hrms.service;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.goal.DepartmentGoalEmployeeMetricHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalAssigneesManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GoalService {

    private static final Logger log = LoggerFactory.getLogger(GoalService.class);

    private final GoalManager goalManager;
    private final DepartmentManager departmentManager;
    private final GoalAssigneesManager goalAssigneesManager;
    private final DepartmentGoalEmployeeMetricHistoryManager employeeMetricHistoryManager;

    public GoalService(GoalManager goalManager, DepartmentManager departmentManager, GoalAssigneesManager goalAssigneesManager, DepartmentGoalEmployeeMetricHistoryManager employeeMetricHistoryManager) {
        this.goalManager = goalManager;
        this.departmentManager = departmentManager;
        this.goalAssigneesManager = goalAssigneesManager;
        this.employeeMetricHistoryManager = employeeMetricHistoryManager;
    }

    @Transactional(readOnly = true)
    public List<SelectItem> getDepartmentGoals(Integer departmentId) {
        Optional.of(departmentManager.get(departmentId)).orElseThrow(() -> {
            log.error("=========== Department {} is not found! ===========", departmentId);
            return new RuntimeException("=========== Department is not found! ===========");
        });
        List<EdsGoal> goals = goalManager.getDepartmentGoalsByDepartments(Set.of(departmentId));
        return goals.stream().map(EdsGoal::getAsSelectItem).toList();
    }

    @Transactional
    public void deleteGoal(Integer goalId, String type) {
        EdsGoal goal = Optional.of(goalManager.get(goalId)).orElseThrow(() -> {
            log.error("=========== Goal {} is not found! ===========", goalId);
            return new RuntimeException("=========== Goal is not found! ===========");
        });
        this.goalManager.deleteGoal(goal);
        this.goalAssigneesManager.deleteGoalAssignees(goal.getObjectID());
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(goalId);
        if (Constants.PERSONAL_GOAL.equals(type)) {
            kpiLog.setEntityType(EdsGoal.PERSONAL_GOAL);
            ServerUtils.kpiLog(GoalService.log, kpiLog, "Delete personal goal");
        } else if (Constants.DEPARTMENT_GOAL.equals(type)) {
            employeeMetricHistoryManager.deleteEmployeeMetricHistoriesByDepartmentGaolId(goal.getObjectID());
            kpiLog.setEntityType(EdsGoal.DEPARTMENT_GOAL);
            ServerUtils.kpiLog(GoalService.log, kpiLog, "Delete department goal");
        } else if (Constants.PROJECT_GOAL.equals(type)) {
            kpiLog.setEntityType(EdsGoal.PROJECT_GOAL);
            ServerUtils.kpiLog(GoalService.log, kpiLog, "Delete project goal");
        } else if (Constants.BUSINESS_GOAL.equals(type)) {
            kpiLog.setEntityType(EdsGoal.BUSINESS_GOAL);
            ServerUtils.kpiLog(GoalService.log, kpiLog, "Delete business goal");
        }
    }
}
