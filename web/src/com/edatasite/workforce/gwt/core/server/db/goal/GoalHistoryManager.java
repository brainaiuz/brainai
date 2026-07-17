package com.edatasite.workforce.gwt.core.server.db.goal;

import com.edatasite.workforce.core.domain.goal.EdsGoalHistory;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface GoalHistoryManager extends Manager<EdsGoalHistory> {
    List<EdsGoalHistory> getGoalHistoryList(Integer goalId);
}
