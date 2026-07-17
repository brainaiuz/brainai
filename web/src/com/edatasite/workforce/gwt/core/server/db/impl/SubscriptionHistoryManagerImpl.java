package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsSubscriptionHistory;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Apr 3, 2009
 * Time: 2:41:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("subscriptionHistoryManager")
public class SubscriptionHistoryManagerImpl extends BaseManager<EdsSubscriptionHistory> implements SubscriptionHistoryManager {

    public SubscriptionHistoryManagerImpl() {
        super(EdsSubscriptionHistory.class);
    }

    public EdsSubscriptionHistory getLastSubscriptionHistory(EdsUsagePlan usagePlan) {
        return (EdsSubscriptionHistory) findSingle("from EdsSubscriptionHistory up where up.usagePlan.objectID =? ORDER BY up.id desc", usagePlan.getObjectID());
    }

    @Override
    public List<EdsSubscriptionHistory> getAllSubscriptionHistoryList(EdsReference freeTrial, EdsReference expired) {
        return find("SELECT sh FROM EdsSubscriptionHistory sh WHERE (sh.usagePlan.deleted<>true OR sh.usagePlan.deleted IS NOT NULL) AND sh.usagePlan.periodType <> ? AND sh.usagePlan.status <> ?", freeTrial, expired);
    }
}
