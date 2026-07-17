package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProductLocation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/5/12
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductLocationManager extends Manager<EdsProductLocation>{
    List<EdsProductLocation> getProductLocations(ListingFilterParameter filterParameter);
}
