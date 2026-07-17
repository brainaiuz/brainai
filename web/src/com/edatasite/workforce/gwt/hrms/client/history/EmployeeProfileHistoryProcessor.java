package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.EmployeeProfileViewSinksContainer;

/**
 * User: unni
 * Date: Oct 26, 2009
 * Time: 8:49:25 PM
 */
public class EmployeeProfileHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new EmployeeProfileViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}