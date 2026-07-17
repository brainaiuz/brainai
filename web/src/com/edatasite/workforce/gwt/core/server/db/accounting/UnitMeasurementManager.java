package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 14, 2010
 * Time: 10:26:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UnitMeasurementManager extends Manager<EdsUnitMeasurement> {
    List<EdsUnitMeasurement> getUnitMeasurements(ListingFilterParameter filterParametrs, Integer companyID);

    HashMap<String, Integer> isUnitMeasurementUsed(Integer objectId);

    EdsUnitMeasurement getByName(String name);

    Map<String, EdsUnitMeasurement> getAsMap();
}
