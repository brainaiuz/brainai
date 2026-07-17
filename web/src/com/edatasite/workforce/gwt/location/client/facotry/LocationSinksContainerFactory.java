package com.edatasite.workforce.gwt.location.client.facotry;

import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.location.client.history.LocationHistoryProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 18:03:02
 * To change this template use File | Settings | File Templates.
 */
public class LocationSinksContainerFactory extends SinksContainerFactory {
    public LocationSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
    }

    public void initDefaultContainers() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void registerProcessors() {
        registerHistoryProcessor("location", new LocationHistoryProcessor());
    }

    public void registerMenuItems() {

    }
}
