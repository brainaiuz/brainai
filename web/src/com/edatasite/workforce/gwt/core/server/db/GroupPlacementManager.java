package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacement;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface GroupPlacementManager extends Manager<EdsGroupPlacement> {

    Integer getGroupPlacementLastIntNumber();

    List<EdsGroupPlacement> getList(ListingFilterParameter fp);

    Integer getTotalCount(ListingFilterParameter fp);

    boolean isGroupPlacementNumberExist(String numberString, Integer objectID);
}
