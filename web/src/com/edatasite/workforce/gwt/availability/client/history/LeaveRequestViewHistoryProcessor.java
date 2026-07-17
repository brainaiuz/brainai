package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.availability.client.LeaveRequestViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: unni
 * Date: Aug 25, 2009
 * Time: 5:17:52 PM
 */
public class LeaveRequestViewHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new LeaveRequestViewSinksContainer(containerName + strings[0], hrmsStrings.leaveRequestView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}