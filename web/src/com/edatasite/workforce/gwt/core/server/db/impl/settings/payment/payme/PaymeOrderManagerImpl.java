package com.edatasite.workforce.gwt.core.server.db.impl.settings.payment.payme;

import com.edatasite.workforce.core.domain.settings.payment.payme.EdsPaymeOrder;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.payment.payme.PaymeOrderManager;
import org.springframework.stereotype.Repository;

@Repository("paymeOrderManager")
public class PaymeOrderManagerImpl extends BaseManager<EdsPaymeOrder> implements PaymeOrderManager {
    public PaymeOrderManagerImpl() {
        super(EdsPaymeOrder.class);
    }
}



