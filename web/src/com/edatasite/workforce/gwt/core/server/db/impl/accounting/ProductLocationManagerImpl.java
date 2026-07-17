package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProductLocation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductLocationManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/5/12
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("productLocationManager")
public class ProductLocationManagerImpl extends BaseManager<EdsProductLocation> implements ProductLocationManager{
    public ProductLocationManagerImpl() {
        super(EdsProductLocation.class);
    }

    @Override
    public List<EdsProductLocation> getProductLocations(ListingFilterParameter filterParameter) {
        return find("from EdsProductLocation where warehouse.objectID = ?", filterParameter.getWarehouseID());
    }
}
