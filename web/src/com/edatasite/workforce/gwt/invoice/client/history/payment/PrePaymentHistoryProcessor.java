package com.edatasite.workforce.gwt.invoice.client.history.payment;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.payment.PrePaymentAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.payment.PrePaymentViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/2/11
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrePaymentHistoryProcessor implements HistoryProcessor{
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final boolean isReceivable;

    public PrePaymentHistoryProcessor(boolean isReceivable) {
        this.isReceivable = isReceivable;
    }

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PrePaymentViewSinksContainer(containerName + strings[0], (isReceivable ? Property.get(Constants.CUSTOMER_PREPAYMENT, wfmStrings.prepayments()) : Property.get(Constants.SUPPLIER_LIST, accountingStrings.addSupplierCredit(), wfmStrings.supplier())), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new PrePaymentAddSinksContainer((isReceivable ? "prepaymentadd" : "supplierCreditadd"), (isReceivable ? Property.get(Constants.CUSTOMER_PREPAYMENT, wfmStrings.addMess(), accountingStrings.customerPrepayment()) : Property.get(Constants.SUPPLIER_LIST, accountingStrings.addSupplierCredit(), wfmStrings.supplier())), params);
    }
}
