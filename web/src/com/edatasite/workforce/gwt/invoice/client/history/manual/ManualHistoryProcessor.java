package com.edatasite.workforce.gwt.invoice.client.history.manual;

import com.edatasite.workforce.gwt.accounting.client.container.report.ManualViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.manual.ManualAddSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: sasna
 * Date: 16.04.2009
 * Time: 21:41:44
 * To change this template use File | Settings | File Templates.
 */
public class ManualHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new ManualViewSinksContainer(containerName + strings[0], accountingStrings.manualEntryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new ManualAddSinksContainer("manualadd", accountingStrings.addManualEntry(), params);
    }
}
