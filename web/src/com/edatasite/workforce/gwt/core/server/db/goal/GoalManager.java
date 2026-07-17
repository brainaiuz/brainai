package com.edatasite.workforce.gwt.core.server.db.goal;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 26, 2009
 * Time: 3:08:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GoalManager extends Manager<EdsGoal> {
    List<EdsGoal> list(ListingFilterParameter fp);

    void deleteGoal(EdsGoal goal);

    List<EdsGoal> getOwnGoalList(ListingFilterParameter fp, EdsReference ref);

    List<EdsGoal> getGoalsPeerAssign(ListingFilterParameter fp);

    List<EdsGoal> getGoalsPeerAssignOutValidity(ListingFilterParameter fp);

    List<EdsGoal> getGoalListByYear(ListingFilterParameter fp, Date startYearDate, Date endYearDate);

    List<EdsGoal> getDepartmentGoalsByDepartments(Set<Integer> ids);

    List<EdsGoal>  getDepartmentGoalsByDepartment(Integer ids);

    Boolean isUsedValidityPeriod(EdsValidityPeriod validityPeriod);

    SelectItem[] getListAsSelectItems(ListingFilterParameter fp);

    Integer getGoalLastIntNumber(String categoryType);

    Boolean getGoalByNumberData(String numberString);

    Integer getDepartmentGoalAvailableWeight(Integer projectId);
}