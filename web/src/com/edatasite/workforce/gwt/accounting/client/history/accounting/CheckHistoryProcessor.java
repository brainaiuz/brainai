package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.CheckAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.CheckViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/16/12
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class CheckHistoryProcessor implements HistoryProcessor {

    private static AccountingStrings accountingStrings = AccountingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new CheckViewSinksContainer(containerName + strings[0], accountingStrings.writeCheck(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new CheckAddSinksContainer("checkadd", accountingStrings.writeCheck(), params);
    }
}
