package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.utils.ImportAdditionalPaymentSinksContainer;

/**
 * Created by Shohruh on 07 Nov 2016.
 */
public class ImportAdditionalPaymentHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportAdditionalPaymentSinksContainer("importAdditionalPaymentadd", "Import Additional Payment", params);
    }
}
