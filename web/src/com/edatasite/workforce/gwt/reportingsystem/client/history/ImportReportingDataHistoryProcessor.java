package com.edatasite.workforce.gwt.reportingsystem.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.ImportReportDataSinksContainer;

/**
 * Created by Faxriddin Taslimov on 13/08/19.
 */
public class ImportReportingDataHistoryProcessor implements HistoryProcessor, Constants {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportReportDataSinksContainer("importReportDataadd", "Import Report Data", params);
    }
}
