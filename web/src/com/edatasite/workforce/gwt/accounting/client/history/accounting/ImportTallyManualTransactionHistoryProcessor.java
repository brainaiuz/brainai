package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by dilshod madrahimov on 28-Nov-16.
 */
public class ImportTallyManualTransactionHistoryProcessor implements HistoryProcessor {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportTallyManualTransactionSinksContainer("importtallymanualtransactionadd", WfmStrings.App.get().importManualEntry(), params);
    }
}
