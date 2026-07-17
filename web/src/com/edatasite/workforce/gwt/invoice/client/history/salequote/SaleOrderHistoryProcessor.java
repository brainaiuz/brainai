package com.edatasite.workforce.gwt.invoice.client.history.salequote;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.salequote.SaleQuoteAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.salequote.SaleQuoteViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 19, 2010
 * Time: 7:35:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaleOrderHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new SaleQuoteViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new SaleQuoteAddSinksContainer("saleorderadd", Property.get(Constants.SALE_ORDER_CODE, wfmStrings.addMess(), accountingStrings.salesOrder()), params);
    }
}
