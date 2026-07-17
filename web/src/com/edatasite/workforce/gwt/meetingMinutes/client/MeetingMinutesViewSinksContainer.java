package com.edatasite.workforce.gwt.meetingMinutes.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.meetingMinutes.client.ui.EditMeetingMinutesForm;
import com.edatasite.workforce.gwt.meetingMinutes.client.ui.ViewMeetingMinutesForm;

import java.util.LinkedList;

/**
 * User: developer
 * Date: 4/18/12
 * Time: 3:22 PM
 */
public class MeetingMinutesViewSinksContainer extends SinksContainer {

    public MeetingMinutesViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ViewMeetingMinutesForm(id));
        if (Utils.isHRMS() ? Utils.hasPermission(PermissionConstants.EDIT_MEETING_MINUTES) : Utils.hasPermission(PermissionConstants.EDIT_MEETING_MINUTES_WORKSPACE)){
            addView(new EditMeetingMinutesForm(id));
        }
    }
}