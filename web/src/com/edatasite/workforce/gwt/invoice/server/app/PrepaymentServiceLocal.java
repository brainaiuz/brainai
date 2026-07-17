package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PrePaymentData;

public interface PrepaymentServiceLocal {
    Integer savePrePayment(PaymentData prePaymentData, boolean isCashRefund);

    PrePaymentData getPrePaymentData(Integer prePaymentID, Integer customerID, Boolean isReceivable, Boolean isCopy, Params params);

    Integer deletePrePayment(Integer objectID);

    BankTransferNumberData generatePrepaymentNumber(String transferType);

}
