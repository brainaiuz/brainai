package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.subscription.EdsSubscription;
import com.edatasite.workforce.gwt.core.server.db.HrmsSubscriptionManager;
import org.springframework.stereotype.Repository;

/**
 * User : Akhror
 * Date : 03.11.2023
 */
@Repository("hrmsSubscriptionManager")
public class HrmsSubscriptionManagerImpl extends BaseManager<EdsSubscription> implements HrmsSubscriptionManager {
    public HrmsSubscriptionManagerImpl() {
        super(EdsSubscription.class);
    }
}
