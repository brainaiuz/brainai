package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 14, 2010
 * Time: 4:06:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WarehouseManager extends Manager<EdsWarehouse> {

    List<EdsWarehouse> getWarehouseList(ListingFilterParameter filterParameter);

    Integer getWarehouseListCount(ListingFilterParameter filterParameter);

    EdsWarehouse getDefaultWarehouse();

    EdsWarehouse getByName(String name);

    List<EdsWarehouse> getWarehousesByOwner(Integer userId);

    boolean hasAccessToWarehouse(Integer userId, Integer warehouseId);
}
