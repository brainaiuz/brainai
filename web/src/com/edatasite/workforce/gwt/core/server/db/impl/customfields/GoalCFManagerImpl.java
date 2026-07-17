package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsGoalCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.GoalCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Aziz
 * Date: 11-July-2012
 * Time: 17:23:19
 */
@Repository("goalCFManager")
public class GoalCFManagerImpl extends BaseManager<EdsGoalCustomFields> implements GoalCFManager {
    public GoalCFManagerImpl() {
        super(EdsGoalCustomFields.class);
    }
}
