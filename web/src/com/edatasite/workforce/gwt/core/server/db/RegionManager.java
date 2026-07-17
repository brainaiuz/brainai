package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface RegionManager extends Manager<EdsRegion> {

    List<EdsRegion> list();

    List<EdsRegion> list(ListingFilterParameter fp);

    List<EdsRegion> listByCountry(Integer countryId);

    SelectItem[] listRegionByCountry(Integer countryId, ListingFilterParameter filterParameter);

    EdsRegion getRegionByName(String name);

    String getStateTimeZoneAndPhoneCode(EdsCountry country, EdsRegion state);

    List<EdsRegion> listBySaudiArabia();

    EdsRegion getRegion(Integer countryId, String state_code);
}
