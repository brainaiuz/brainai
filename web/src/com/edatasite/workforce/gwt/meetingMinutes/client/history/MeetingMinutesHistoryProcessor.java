package com.edatasite.workforce.gwt.meetingMinutes.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.meetingMinutes.client.MeetingMinutesAddSinksContainer;
import com.edatasite.workforce.gwt.meetingMinutes.client.MeetingMinutesViewSinksContainer;
//import com.edatasite.workforce.gwt.meetingMinutes.client.localization.MeetingMinutesString;

/**
 * User: developer
 * Date: 4/18/12
 * Time: 3:18 PM
 */
public class MeetingMinutesHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new MeetingMinutesViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new MeetingMinutesAddSinksContainer("meetingMinutesadd", wfmStrings.add(), params);
    }
}