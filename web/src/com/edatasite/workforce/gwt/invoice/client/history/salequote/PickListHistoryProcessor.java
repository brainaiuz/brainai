package com.edatasite.workforce.gwt.invoice.client.history.salequote;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.salequote.PickListViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 23, 2010
 * Time: 7:47:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class PickListHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new PickListViewSinksContainer(containerName + strings[0], accountingStrings.pickListView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new PickListViewSinksContainer("picklistedit", accountingStrings.pickListView(), params);
    }
}
