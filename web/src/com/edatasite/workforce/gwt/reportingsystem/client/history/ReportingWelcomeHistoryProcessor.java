package com.edatasite.workforce.gwt.reportingsystem.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.reportingsystem.client.sinkscontainer.ReportingHomeSinksContainer;

/**
 * Created by Virus on 9/11/14.
 */
public class ReportingWelcomeHistoryProcessor implements HistoryProcessor, Constants {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ReportingHomeSinksContainer(containerName + strings[0], "Reporting System", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
