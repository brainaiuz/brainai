package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsOverPayment;
import com.edatasite.workforce.gwt.core.server.db.OverPaymentManager;
import org.springframework.stereotype.Repository;

/**
 * User: Dilsh0d Madrahimov
 * Date: 7/31/17
 */
@Repository("overPaymentManager")
public class OverPaymentManagerImpl extends BaseManager<EdsOverPayment> implements OverPaymentManager {

    public OverPaymentManagerImpl() {
        super(EdsOverPayment.class);
    }

    @Override
    public EdsOverPayment getOverPaymentByBatchPayment(Integer batchPaymentID) {
        return (EdsOverPayment) findSingle("select oup from EdsOverPayment oup where (oup.deleted = false or oup.deleted is null) and oup.batchPayment.objectID=? ", batchPaymentID);
    }
}
