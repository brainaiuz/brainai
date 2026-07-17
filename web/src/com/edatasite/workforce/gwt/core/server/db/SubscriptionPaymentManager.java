package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSubscriptionPayment;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 25.12.2008
 * Time: 16:35:02
 * To change this template use File | Settings | File Templates.
 */
public interface SubscriptionPaymentManager extends Manager<EdsSubscriptionPayment> {

    EdsSubscriptionPayment getPPByTxnID(String txnID);

    EdsSubscriptionPayment getByUsageplanUID(String usagePlanUID);

}
