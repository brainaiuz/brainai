package com.edatasite.workforce.gwt.core.server.db.goal;

import com.edatasite.workforce.core.domain.goal.EdsGroupGoal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface GroupGoalManager extends Manager<EdsGroupGoal> {

    List<EdsGroupGoal> getList(ListingFilterParameter filterParametrs);

    Integer getTotalCount(ListingFilterParameter filterParametrs);

}
