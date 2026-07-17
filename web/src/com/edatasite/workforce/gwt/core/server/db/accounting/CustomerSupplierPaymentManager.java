package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsCustomerSupplierPayment;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/30/11
 * Time: 5:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CustomerSupplierPaymentManager extends Manager<EdsCustomerSupplierPayment> {

    List<EdsCustomerSupplierPayment> getPayments(Integer customerSupplierID, boolean isClient);

    BigDecimal getPaidAmount(Integer customerSupplierID, boolean isClient);

    boolean isPaymentsExists(Integer customerSupplierID, boolean isClient);


    BigDecimal getManualPaymentsAmount(Integer manualJournalID, Integer clientSupplierID, boolean isClient, Integer accountID);

    List<EdsCustomerSupplierPayment> getBatchPaymentItems(Integer batchPaymentID);

    List<PaymentData> getPaymentItems(Integer batchPaymentID);

    void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID);

    EdsCustomerSupplierPayment getUnderPayment(Integer objectID);
}
