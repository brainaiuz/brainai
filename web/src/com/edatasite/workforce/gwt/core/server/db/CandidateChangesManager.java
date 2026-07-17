package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCandidateChanges;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface CandidateChangesManager extends Manager<EdsCandidateChanges> {

    List<HistoryItem> changeList(ListingFilterParameter fp);

    Long getChangesCount(ListingFilterParameter fp);
}
