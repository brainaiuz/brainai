package com.edatasite.workforce.gwt.core.server.db.impl.goal;

import com.edatasite.workforce.core.domain.goal.EdsDepartmentGoalChartSettings;
import com.edatasite.workforce.gwt.core.server.db.goal.DepartmentGoalChartSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("departmentGoalChartSettingsManager")
public class DepartmentGoalChartSettingsManagerImpl extends BaseManager<EdsDepartmentGoalChartSettings> implements DepartmentGoalChartSettingsManager {

    public DepartmentGoalChartSettingsManagerImpl() {
        super(EdsDepartmentGoalChartSettings.class);
    }

    @Override
    public EdsDepartmentGoalChartSettings getByGoalId(Integer goalId) {
        return (EdsDepartmentGoalChartSettings) findSingle(
                "SELECT s FROM EdsDepartmentGoalChartSettings s WHERE (s.deleted is null or s.deleted <> true) AND s.goalId = ?",
                goalId);
    }
}
