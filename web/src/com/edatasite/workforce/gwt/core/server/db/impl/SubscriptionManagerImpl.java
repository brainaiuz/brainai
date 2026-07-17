package com.edatasite.workforce.gwt.core.server.db.impl;


import com.edatasite.workforce.core.domain.EdsSubscriptionType;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubscriptionManagerImpl extends BaseManager<EdsSubscriptionType> implements
        SubscriptionManager {

    public SubscriptionManagerImpl() {
        super(EdsSubscriptionType.class);

    }

    public List<EdsSubscriptionType> getSubscriptionType() {
        return find("select c from EdsSubscriptionType c ");
    }


    public void deleteSubscriptionType(Integer objectID) {
        update("delete from EdsSubscriptionType Subscription where Subscription.objectID= ?", objectID);
    }

    public EdsSubscriptionType getId(Integer objectID) {
        return (EdsSubscriptionType) findSingle("select Subscription from " +
                "EdsSubscriptionType Subscription where Subscription.objectID= ?", objectID);

    }


}