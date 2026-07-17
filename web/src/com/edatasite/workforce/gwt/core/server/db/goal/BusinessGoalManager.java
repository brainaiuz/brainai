package com.edatasite.workforce.gwt.core.server.db.goal;

import com.edatasite.workforce.core.domain.goal.EdsBusinessGoal;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 26, 2009
 * Time: 3:08:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BusinessGoalManager extends Manager<EdsBusinessGoal> {
    List<EdsBusinessGoal> list(ListingFilterParameter fp);

    void deleteCompanyGoal(Integer goalId);

    SelectItem[] getListAsSelectItems(ListingFilterParameter fp);
}
