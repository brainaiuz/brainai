package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsMessageUnsubscribers;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by Azazello on 7/10/2017.
 */
public interface MessageUnsubscribersManager extends Manager<EdsMessageUnsubscribers> {
    EdsMessageUnsubscribers getByMsgAndEntity(Integer msgID, Integer entityID);

    void insertUnsubscriber(Integer subscriberID, Integer msgID, Integer mailListID);

    Long getEntitiesCountByMessageID(ListingFilterParameter filterParameter);

    List<EdsCrmContact> getEntitiesByMessageID(ListingFilterParameter filterParameter);

    Long getUnsubscribersCountByMessageID(Integer msgID);
}
