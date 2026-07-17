package com.edatasite.workforce.gwt.reportingsystem.client.history;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.ReportingSinksContainer;
import com.google.gwt.http.client.URL;

/**
 * Created by Virus on 9/11/14.
 */
public class ReportingHistoryProcessor implements HistoryProcessor, Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        if (strings.length > 2) {
            strings[2] = Utils.decrypt(URL.decodeQueryString(strings[2]));
        }
        return new ReportingSinksContainer(containerName + strings[0], strings.length > 2 ? strings[2] : wfmStrings.reportingSystem(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
