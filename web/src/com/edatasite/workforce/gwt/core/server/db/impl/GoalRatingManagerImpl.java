package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.assessment.EdsGoalRating;
import com.edatasite.workforce.gwt.core.server.db.GoalRatingManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 3/30/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("goalRatingManager")
public class GoalRatingManagerImpl extends BaseManager<EdsGoalRating> implements GoalRatingManager{

	public GoalRatingManagerImpl() {
		super(EdsGoalRating.class);
	}
}
