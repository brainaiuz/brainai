package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsMailMessageTrack;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 14:38:49
 * To change this template use File | Settings | File Templates.
 */
public interface MailMessageTrackManager extends Manager<EdsMailMessageTrack> {

    List<EdsMailMessageTrack> getViewList(ListingFilterParameter fp);

    Integer getViewListCount(ListingFilterParameter fp);

    List<EdsMailMessageTrack> getViewByMessageID(Integer messageID);

    Long getViewCountByMessage(Integer messageID);

    EdsMailMessageTrack getByEntityAndMessage(Integer entityid, Integer messageID);
}
