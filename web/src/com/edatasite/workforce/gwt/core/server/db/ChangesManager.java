package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsChanges;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by Hurshid on 8/16/2017.
 */
public interface ChangesManager extends Manager<EdsChanges> {

    List<HistoryItem> changeList(ListingFilterParameter fp);

    Long getChangesCount(ListingFilterParameter fp);

    String changedFieldByDate(ListingFilterParameter fp);
}
