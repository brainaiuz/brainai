package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.ImportChartOfAccountsSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 4, 2011
 * Time: 11:25:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportChartOfAccountsHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportChartOfAccountsSinksContainer("importchartofaccountsadd", accountingStrings.importChartOfAccounts(), params);
    }
}
