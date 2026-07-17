package com.edatasite.workforce.gwt.meetingMinutes.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by Djuraev on 9/16/15.
 */
public interface MeetingMinutesService extends RemoteService {

    ListResult<MeetingMinutesItem> getMeetingMinutes(ListingFilterParameter filterParameter);

    MeetingMinutesItem getMeetingMinutesData(Integer meetingMinutesId);

    Integer saveMeetingMinutes(MeetingMinutesItem meetingMinutesItem);

    NumberData generateMeetingMinutesNumber();

    void deleteMeetingMinutes(Integer meetingID);

    Integer convertMeetingMinutesToProject(Integer meetingID);

    Boolean getMeetingConvertedStatus(Integer meetingID);

    class App {
        public static MeetingMinutesServiceAsync get() {
            ServiceDefTarget target = GWT.create(MeetingMinutesService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/meetingMinutes");
            return (MeetingMinutesServiceAsync) target;
        }
    }
}
