package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTaskChanges;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface TaskChangesManager extends Manager<EdsTaskChanges> {

    List<HistoryItem> changeList(ListingFilterParameter fp);

    Long getChangesCount(ListingFilterParameter fp);
}
