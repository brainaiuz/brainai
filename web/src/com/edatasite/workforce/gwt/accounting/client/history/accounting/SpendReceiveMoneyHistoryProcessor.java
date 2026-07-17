package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.SpendReceiveMoneyAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.SpendReceiveMoneyViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.07.2010
 * Time: 19:07:29
 * To change this template use File | Settings | File Templates.
 */
public class SpendReceiveMoneyHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] params) {
        String viewType = params.length > 1 ? params[1] : "";
        String description = getDescription(viewType);
        return new SpendReceiveMoneyViewSinksContainer(containerName + params[0], description, params);
    }

    public SinksContainer processAdd(String[] params) {
        String viewType = params.length > 1 ? params[1] : "";
        String description = getDescription(viewType);

        return new SpendReceiveMoneyAddSinksContainer("spendreceivemoneyadd", description, params);
    }

    private String getDescription(String viewType) {
        String description = Property.get(Constants.BANKACCOUNT, accountingStrings.bankAccountTransactions(), wfmStrings.bankAccount());

        if (AccountingConstants.SPEND_MONEY_STR.equals(viewType)) {
            description = accountingStrings.bankPayments();
        } else if (AccountingConstants.RECEIVE_MONEY_STR.equals(viewType)) {
            description = accountingStrings.bankReceipts();
        } else if (AccountingConstants.CASH_RECEIPT_STR.equals(viewType)) {
            description = wfmStrings.cashReceipt();
        } else if (AccountingConstants.CASH_PAYMENT_STR.equals(viewType)) {
            description = wfmStrings.cashPayment();
        }
        return description;
    }
}
