package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsOverPayment;

/**
 * User: Dilsh0d Madrahimov
 * Date: 7/31/17
 */
public interface OverPaymentManager extends Manager<EdsOverPayment> {

    EdsOverPayment getOverPaymentByBatchPayment(Integer batchPaymentID);
}
