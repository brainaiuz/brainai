package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 17:15:29
 */
public interface LocationManager extends Manager<EdsLocation> {

    List<EdsLocation> list(ListingFilterParameter fp);

    List<EdsLocation> getLocations(ListingFilterParameter fp);

    List<EdsEmployee> getLocationEmployee(Integer locationId);

    int getLocationUsedSize(Integer locationID);

    EdsLocation getLocationByName(String locationName);

    EdsLocation getLocationByName(String locationName, Integer locationID);

    EdsLocation getLocationByCode(String code, Integer id);

    SelectItem[] getLocationsAsSelectItems(ListingFilterParameter listingFilterParameter);

    SelectItem[] getLocationsAsSelecItem();

    List<Object[]> getList();

    List<Object[]> getListByCode();

    Integer getLastIntNumber();

    EdsLocation getRootLocation();
}