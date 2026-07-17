package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.subscription.EdsSubscriptionUsage;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionUsageManager;
import org.springframework.stereotype.Repository;

/**
 * User : Akhror
 * Date : 03.11.2023
 */
@Repository("subscriptionUsageManager")
public class SubscriptionUsageManagerImpl extends BaseManager<EdsSubscriptionUsage> implements SubscriptionUsageManager {
    public SubscriptionUsageManagerImpl() {
        super(EdsSubscriptionUsage.class);
    }
}
