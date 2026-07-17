package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSubscriptionPayment;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionPaymentManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 25.12.2008
 * Time: 16:36:37
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SubscriptionPaymentManagerImpl extends BaseManager<EdsSubscriptionPayment> implements SubscriptionPaymentManager {
    public SubscriptionPaymentManagerImpl() {
        super(EdsSubscriptionPayment.class);
    }

    public EdsSubscriptionPayment getPPByTxnID(String txID) {
        return (EdsSubscriptionPayment)findSingle("SELECT pp FROM EdsSubscriptionPayment pp WHERE pp.txn_id = ?", txID);
    }

    public EdsSubscriptionPayment getByUsageplanUID(String usagePlanUID) {
        return (EdsSubscriptionPayment)findSingle("SELECT pp FROM EdsSubscriptionPayment pp WHERE pp.usageplan_guid = ? AND pp.apiSubscrId IS NOT NULL ORDER BY pp.objectID DESC", usagePlanUID);
    }
}
