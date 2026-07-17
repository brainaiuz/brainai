package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.AddBankSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.BankAccountViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: java
 * Date: 26.02.2009
 * Time: 18:00:27
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrigns = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        return new BankAccountViewSinksContainer(containerName + strings[0], Property.get(Constants.BANKACCOUNT, wfmStrigns.summaryView(), wfmStrigns.bankAccount()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new AddBankSinksContainer("bankadd", Property.get(Constants.BANKACCOUNT, accountingStrings.addbankaccount()));
    }
}
