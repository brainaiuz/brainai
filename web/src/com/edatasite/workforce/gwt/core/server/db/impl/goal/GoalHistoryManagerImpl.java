package com.edatasite.workforce.gwt.core.server.db.impl.goal;

import com.edatasite.workforce.core.domain.goal.EdsGoalHistory;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.impl.AttachmentSupportManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("goalHistoryManager")
public class GoalHistoryManagerImpl extends AttachmentSupportManager<EdsGoalHistory> implements GoalHistoryManager {
    public GoalHistoryManagerImpl() {
        super(EdsGoalHistory.class);
    }

    @Override
    public List<EdsGoalHistory> getGoalHistoryList(Integer goalId) {
        if (goalId == null) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select gh.* from ").append(getCompanyId()).append(".goal_history gh ")
                .append("join ").append(getCompanyId()).append(".goal g on gh.goal_id=g.id ")
                .append("where g.id=").append(goalId).append(" order by gh.creation_date desc");
        return (ArrayList<EdsGoalHistory>) findNative(sql.toString(), EdsGoalHistory.class);
    }

    @Override
    public void delete(EdsGoalHistory obj) {
        if (obj != null) {
            updateNative("delete from " + getCompanyId() + ".goal_history  where id = " + obj.getObjectID());
        }
    }
}
