package com.edatasite.workforce.gwt.googlecalendar.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.googlecalendar.client.EventEditSinksContainer;
import com.edatasite.workforce.gwt.googlecalendar.client.EventViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 29, 2009
 * Time: 3:00:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventHistoryProcessor implements HistoryProcessor {
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new EventViewSinksContainer(containerName + strings[0], Property.get(Constants.EVENT_LIST, wfmStrings.summaryView(), wfmStrings.event()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new EventEditSinksContainer("eventadd", Property.get(Constants.EVENT_LIST, wfmStrings.event()), params);
	}
}
