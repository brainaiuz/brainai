package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * User: Dilshod Madrahimov
 * Date: 6/22/12
 * Time: 4:38 PM
 */

public interface TalentProfileService extends RemoteService {

    ListResult<TalentProfileListItem> getTalentProfileList(ListingFilterParameter filterParameter);

    EducationItem getEducation(Integer objectId);

    void saveEducation(EducationItem item);

    Boolean deleteTalentProfileItem(Integer objectID, TalentProfileEnum type);

    AwardItem getAward(Integer objectId);

    void saveAward(AwardItem item);

    class App {
        public static TalentProfileServiceAsync get() {
            ServiceDefTarget target = GWT.create(TalentProfileService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/talentProfile");
            return (TalentProfileServiceAsync) target;
        }
    }
}
