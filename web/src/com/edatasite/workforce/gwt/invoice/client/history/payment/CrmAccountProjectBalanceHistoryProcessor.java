package com.edatasite.workforce.gwt.invoice.client.history.payment;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.payment.CrmAccountProjectBalanceSinksContainer;
/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/28/11
 * Time: 8:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountProjectBalanceHistoryProcessor implements HistoryProcessor{

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new CrmAccountProjectBalanceSinksContainer(containerName + strings[0], accountingStrings.customerProjectBalance(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
