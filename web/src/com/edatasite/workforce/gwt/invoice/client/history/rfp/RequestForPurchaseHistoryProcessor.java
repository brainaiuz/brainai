package com.edatasite.workforce.gwt.invoice.client.history.rfp;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.rfp.RequestForPurchaseAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.rfp.RequestForPurchaseViewSinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: Murad
 * Date: 4/8/13
 * Time: 8:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestForPurchaseHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new RequestForPurchaseViewSinksContainer(containerName + strings[0], Property.get(Constants.REQUEST_FOR_PURCHASE, wfmStrings.requestForPurchase()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new RequestForPurchaseAddSinksContainer("requestforpurchaseadd", Property.get(Constants.REQUEST_FOR_PURCHASE, wfmStrings.requestForPurchase()), params);
    }
}
