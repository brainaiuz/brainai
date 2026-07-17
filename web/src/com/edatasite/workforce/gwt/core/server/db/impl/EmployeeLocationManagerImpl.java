package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeLocation;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.EmployeeLocationManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("employeeLocationManager")
public class EmployeeLocationManagerImpl extends BaseManager<EdsEmployeeLocation> implements EmployeeLocationManager {

    public EmployeeLocationManagerImpl() {
        super(EdsEmployeeLocation.class);
    }

    @Override
    public void removeLocationHistory(Integer objectID) {
        update("update EdsEmployeeLocation el set deleted = true where el.objectID = ?", objectID);
    }

    @Override
    public void removeLocationHistory(EdsEmployee employee) {
        update("update EdsEmployeeLocation el set deleted = true where el.user.objectID = ?", employee.getObjectID());
    }

    @Override
    public void removeLocationHistory(EdsEmployee employee, EdsLocation location) {
        update("update EdsEmployeeLocation el set deleted = true where el.user.objectID = ? and el.location.objectID = ?", employee.getObjectID(), location.getObjectID());
    }

    @Override
    public List<EdsEmployeeLocation> getByEmployee(ListingFilterParameter filterParameter) {
        return find("select el from EdsEmployeeLocation el where el.deleted <> true and el.user.objectID = ? order by el.id desc", filterParameter.getEntityID());
    }
}
