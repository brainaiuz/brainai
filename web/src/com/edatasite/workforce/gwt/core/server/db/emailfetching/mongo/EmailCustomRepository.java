package com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo;

import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface EmailCustomRepository {

    Long getLastFetchedMessagesUID(Integer folderId);

    EdsEmail findLastByTrackerId(Integer trackerId);

    List<EdsEmail> getEmailList(ListingFilterParameter fp);

    Integer getEmailCount(ListingFilterParameter fp);
}
