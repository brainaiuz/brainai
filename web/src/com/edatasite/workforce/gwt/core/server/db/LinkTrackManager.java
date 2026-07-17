package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsLinkTrack;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 10.06.2013
 * Time: 18:44:53
 * To change this template use File | Settings | File Templates.
 */

public interface LinkTrackManager extends Manager<EdsLinkTrack> {

    EdsLinkTrack getByEntityAndMessageID(Integer messageID, Integer entityID, Integer linkID);

    Long getClickCountByMessageID(Integer messageID);

    List<Object[]> getClickedEntitiesList(ListingFilterParameter fp);

    Integer getClickedEntitiesCount(ListingFilterParameter fp);
}
