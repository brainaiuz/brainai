package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.AccountAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.AccountViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 25.02.2009
 * Time: 15:54:04
 * To change this template use File | Settings | File Templates.
 */
public class AddAccountHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmString = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        return new AccountViewSinksContainer(containerName + strings[0], wfmString.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new AccountAddSinksContainer("accountadd", wfmString.addAccount());
    }
}
