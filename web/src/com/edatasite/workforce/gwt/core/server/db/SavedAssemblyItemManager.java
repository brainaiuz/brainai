package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSavedAssemblyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface SavedAssemblyItemManager extends Manager<EdsSavedAssemblyItem> {

    Integer getAssemblyLastIntNumber();

    List<EdsSavedAssemblyItem> getList(ListingFilterParameter fp);

    Integer getTotalCount(ListingFilterParameter fp);

    Boolean isSavedAssemblyItemExist(String code, Integer objectID);

}
