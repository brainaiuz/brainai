package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.factory.AvailabilitySinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;

public class Availability extends WorkforceEntryPoint {

    public void initSinksContainerFactory() {
        containerFactory = new AvailabilitySinksContainerFactory(this);
    }
}
