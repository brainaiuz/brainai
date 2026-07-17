package com.edatasite.workforce.gwt.invoice.client.history.payment;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.payment.BatchPaymentSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.payment.BatchPaymentViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/29/11
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchPaymentHistoryProcessor implements HistoryProcessor{
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        boolean isReceivable = true;
        if (strings != null && strings.length >= 2 && Constants.PAYABLE.equals(strings[1])) {
            isReceivable = false;
        }
        return new BatchPaymentViewSinksContainer(containerName + strings[0], (isReceivable ? accountingStrings.receivePayment() : Property.get(Constants.PAYBILLS_LIST, accountingStrings.payBill())), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new BatchPaymentSinksContainer("receivepaymentadd", getTabName(params), params);
    }

    private String getTabName(String[] params) {
        if (params.length > 1 && params[1] != null)
            return Constants.RECEIVABLE.equals(params[1]) ? accountingStrings.receivePayment() : Property.get(Constants.PAYBILLS_LIST, accountingStrings.payBill());
        return "";
    }
}
