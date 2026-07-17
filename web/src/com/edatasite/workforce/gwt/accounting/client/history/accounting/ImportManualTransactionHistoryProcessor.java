package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by dilshod on 23-Mar-16.
 */
public class ImportManualTransactionHistoryProcessor implements HistoryProcessor {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportManualTransactionSinksContainer("importmanualtransactionadd", WfmStrings.App.get().importManualEntry(), params);
    }
}
