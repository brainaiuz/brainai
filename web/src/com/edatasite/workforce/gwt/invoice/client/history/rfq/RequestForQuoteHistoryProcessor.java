package com.edatasite.workforce.gwt.invoice.client.history.rfq;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.rfq.RequestForQuoteAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.rfq.RequestForQuoteViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestForQuoteHistoryProcessor implements HistoryProcessor{
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new RequestForQuoteViewSinksContainer(containerName + strings[0], Property.get(Constants.REQUEST_FOR_QUOTE, wfmStrings.requestForQuote()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new RequestForQuoteAddSinksContainer("requestforquoteadd", Property.get(wfmStrings.addMess(), wfmStrings.requestForQuote()), params);
    }
}
