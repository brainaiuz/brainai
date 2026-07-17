package com.edatasite.workforce.gwt.invoice.client.history.payment;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.payment.PrepaymentListSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/23/11
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrepaymentListHistoryProcessor implements HistoryProcessor {

    private static WfmStrings wfmStrings = WfmStrings.App.get();

    private boolean isReceivable;

    public PrepaymentListHistoryProcessor(boolean isReceivable) {
        this.isReceivable = isReceivable;
    }

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PrepaymentListSinksContainer(containerName + strings[0], (isReceivable ? Property.get(Constants.CUSTOMER_PREPAYMENT, wfmStrings.prepayments()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplierCredits(), wfmStrings.supplier())), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
