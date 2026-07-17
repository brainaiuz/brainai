package com.edatasite.workforce.gwt.accounting.client.history;

import com.edatasite.workforce.gwt.accounting.client.ClientImportSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: Aug 5, 2009
 * Time: 7:45:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientImportHistoryProcessor implements HistoryProcessor {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new ClientImportSinksContainer("importclientadd", accountingStrings.importClient(), params);
    }
}