package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSubscriptionType;

import java.util.List;


public interface SubscriptionManager extends Manager<EdsSubscriptionType> {

    List<EdsSubscriptionType> getSubscriptionType();

    void deleteSubscriptionType(Integer objectID);

    EdsSubscriptionType getId(Integer objectID);
}
