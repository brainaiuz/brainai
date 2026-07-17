package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User: Ilhombek
 * Date: 7/3/12
 * Time: 3:41 PM
 */
public interface PlacementManager extends Manager<EdsPlacement> {

    List<EdsPlacement> getPlacementList(ListingFilterParameter fp, EdsUser user);

    EdsPlacement getPlacementByCandidateId(Integer candidateId);

    Integer getPlacementLastIntNumber();

    List<EdsPlacement> getPlacementByGroupPlacement(Integer id);

}