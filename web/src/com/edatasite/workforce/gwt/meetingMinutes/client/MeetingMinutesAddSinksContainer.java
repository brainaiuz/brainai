package com.edatasite.workforce.gwt.meetingMinutes.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.meetingMinutes.client.ui.AddMeetingMinutesView;

import java.util.LinkedList;

/**
 * User: developer
 * Date: 4/18/12
 * Time: 4:01 PM
 */
public class MeetingMinutesAddSinksContainer extends SinksContainer {

    public MeetingMinutesAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.isHRMS() ? Utils.hasPermission(PermissionConstants.ADD_MEETING_MINUTES) : Utils.hasPermission(PermissionConstants.ADD_MEETING_MINUTES_WORKSPACE)) {
            if (params.length > 2 && "copyMeeting".equals(params[1])) {
                addView(new AddMeetingMinutesView(Integer.valueOf(params[2])));
            } else {
                addView(new AddMeetingMinutesView());
            }
        }
    }
}