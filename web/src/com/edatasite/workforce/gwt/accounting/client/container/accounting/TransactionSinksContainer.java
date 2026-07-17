package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.CheckListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer.BankPaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer.BankReceiptListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer.CashPaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer.CashReceiptsListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment.CustomerPrepaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment.SupplierPrepaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.ManualEntryListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.PayInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.ReceivePaymentListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GoodsDeliveredNotesListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GoodsReceivedNotesListView;

import java.util.LinkedList;

/**
 * Created by Dilshod Madrahimov on 6/3/15 11:10 AM
 */
public class TransactionSinksContainer extends SinksContainer implements PermissionConstants, AccountingConstants {

    public TransactionSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews() {
        initMainViews();
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    private void initMainViews() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CASH_RECEIPTS_AND_PAYMENTS) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT)) {
            addView(new CashReceiptsListView());
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CASH_RECEIPTS_AND_PAYMENTS) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT)) {
            addView(new CashPaymentListView());
        }
        if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE)) {
            addView(new BankReceiptListView());
        }
        if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND)) {
            addView(new BankPaymentListView());
        }
        if (Utils.hasPermission(ACCOUNTING_PREPAYMENT_LIST)) {
            addView(new CustomerPrepaymentListView());
        }
        if (Utils.hasPermission(ACCOUNTING_SUPPLIER_CREDIT_LIST)) {
            addView(new SupplierPrepaymentListView());
        }
        if (Utils.hasPermission(ACCOUNTING_CHECK_LIST)) {
            addView(new CheckListView());
        }
        if (Utils.hasPermission(ACCOUNTING_MANUAL_JOURNAL_LIST)) {
            addView(new ManualEntryListView());
        }
        if (Utils.hasPermission(ACCOUNTING_RECEIVE_PAYMENT_LIST)) {
            addView(new ReceivePaymentListView());
        }
        if (Utils.hasPermission(ACCOUNTING_PAY_BILL_LIST)) {
            addView(new PayInvoiceListView());
        }
        if (Utils.hasPermission(ACCOUNTING_GRN_LIST) ) {
            addView(new GoodsReceivedNotesListView());
        }
        if (Utils.hasPermission(ACCOUNTING_GDN_LIST)) {
            addView(new GoodsDeliveredNotesListView());
        }
    }
}
