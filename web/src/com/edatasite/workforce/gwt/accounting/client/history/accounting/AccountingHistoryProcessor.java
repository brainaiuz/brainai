package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.AccountingSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 20.02.2009
 * Time: 18:13:05
 * To change this template use File | Settings | File Templates.
 */
public class AccountingHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new AccountingSinksContainer(containerName + strings[0], wfmStrings.accounting());
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
