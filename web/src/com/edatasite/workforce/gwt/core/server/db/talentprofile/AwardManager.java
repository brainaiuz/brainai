package com.edatasite.workforce.gwt.core.server.db.talentprofile;

import com.edatasite.workforce.core.domain.EdsAward;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileListItem;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Dec 2, 2009
 * Time: 11:56:05 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AwardManager extends Manager<EdsAward> {
    ListResult<TalentProfileListItem> getTalentProfileList(ListingFilterParameter filterParameter);

    List<Object[]> getTalentProfileData(Integer employeeId);
}
