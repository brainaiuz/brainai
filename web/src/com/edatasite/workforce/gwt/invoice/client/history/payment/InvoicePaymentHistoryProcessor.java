package com.edatasite.workforce.gwt.invoice.client.history.payment;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.payment.InvoicePaymentViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Akmal
 * Date: 12-Mar-2009
 * Time: 17:23:38
 * To change this template use File | Settings | File Templates.
 */
public class InvoicePaymentHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        boolean isCashRefund = false;
        String viewName = accountingStrings.paymentView();
        if (strings != null && strings.length >= 2) {
            if (strings[1] != null && "cashRefund".equals(strings[1])){
                viewName = accountingStrings.refundView();
            }
            if (!isCashRefund){
                if (strings[1] != null && "prepayment".equals(strings[1])){
                    viewName = accountingStrings.prepaymentView();
                }
            }
        }
        return new InvoicePaymentViewSinksContainer(containerName + strings[0], viewName, strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
