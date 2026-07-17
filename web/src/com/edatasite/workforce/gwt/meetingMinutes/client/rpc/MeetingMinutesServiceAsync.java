package com.edatasite.workforce.gwt.meetingMinutes.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by Djuraev on 9/16/15.
 */
public interface MeetingMinutesServiceAsync {

    void getMeetingMinutes(ListingFilterParameter filterParameter, AsyncCallback<ListResult<MeetingMinutesItem>> callback);

    void getMeetingMinutesData(Integer meetingMinutesId, AsyncCallback<MeetingMinutesItem> async);

    void saveMeetingMinutes(MeetingMinutesItem meetingMinutesItem, AsyncCallback<Integer> async);

    void generateMeetingMinutesNumber(AsyncCallback<NumberData> callback);

    void deleteMeetingMinutes(Integer meetingID, AsyncCallback<Void> callback);

    void convertMeetingMinutesToProject(Integer objectID, AsyncCallback<Integer> callback);

    void getMeetingConvertedStatus(Integer objectID, AsyncCallback<Boolean> asyncCallback);
}
