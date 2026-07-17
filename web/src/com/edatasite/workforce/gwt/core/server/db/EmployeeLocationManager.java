package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeLocation;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface EmployeeLocationManager extends Manager<EdsEmployeeLocation> {
    void removeLocationHistory(Integer objectID);

    void removeLocationHistory(EdsEmployee employee);
    void removeLocationHistory(EdsEmployee employee, EdsLocation location);

    List<EdsEmployeeLocation> getByEmployee(ListingFilterParameter filterParameter);
}
