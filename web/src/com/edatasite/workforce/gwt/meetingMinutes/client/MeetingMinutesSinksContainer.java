package com.edatasite.workforce.gwt.meetingMinutes.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.meetingMinutes.client.ui.MeetingMinutesListView;

import java.util.LinkedList;

/**
 * User: developer
 * Date: 4/26/12
 * Time: 9:49 AM
 */
public class MeetingMinutesSinksContainer extends SinksContainer implements Colapse {
    public MeetingMinutesSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new MeetingMinutesListView(false));
    }
}
