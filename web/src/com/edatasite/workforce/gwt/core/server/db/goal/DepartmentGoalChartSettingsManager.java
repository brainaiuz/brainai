package com.edatasite.workforce.gwt.core.server.db.goal;

import com.edatasite.workforce.core.domain.goal.EdsDepartmentGoalChartSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface DepartmentGoalChartSettingsManager extends Manager<EdsDepartmentGoalChartSettings> {

    /**
     * The active chart-settings row for a goal (soft-deleted rows excluded), or null.
     */
    EdsDepartmentGoalChartSettings getByGoalId(Integer goalId);
}
