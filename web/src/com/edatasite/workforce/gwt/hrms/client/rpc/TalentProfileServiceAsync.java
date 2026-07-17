package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: Dilshod Madrahimov
 * Date: 6/22/12
 * Time: 4:38 PM
 */

public interface TalentProfileServiceAsync {

    Request getTalentProfileList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<TalentProfileListItem>> asyncCallback);

    void getEducation(Integer objectId, AsyncCallback<EducationItem> asyncCallback);

    void saveEducation(EducationItem item, AsyncCallback<Void> asyncCallback);

    void deleteTalentProfileItem(Integer objectID, TalentProfileEnum type, AsyncCallback<Boolean> asyncCallback);

    void getAward(Integer objectId, AsyncCallback<AwardItem> asyncCallback);

    void saveAward(AwardItem item, AsyncCallback<Void> callback);

}
