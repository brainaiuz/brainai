package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProductWarehouseLocation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Anvar Akramov
 * Date: Apr 15, 2010
 * Time: 1:53:47 AM
 */
public interface ProductWarehouseLocationManager extends Manager<EdsProductWarehouseLocation> {

    List<EdsProductWarehouseLocation> getProductWarehouseLocations(ListingFilterParameter filterParameter);

    List<Object> getLocationsByWarehouseID(Integer warehouseID, ListingFilterParameter filterParametrs);

    void deleteProductLocations(Integer productID);

}
