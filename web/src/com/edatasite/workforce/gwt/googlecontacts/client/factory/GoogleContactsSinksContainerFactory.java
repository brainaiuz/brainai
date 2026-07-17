package com.edatasite.workforce.gwt.googlecontacts.client.factory;

import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.googlecontacts.client.GoogleContactsSinksContainer;
import com.edatasite.workforce.gwt.googlecontacts.client.history.GoogleContactsHistoryProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.11.2008
 * Time: 20:05:15
 * To change this template use File | Settings | File Templates.
 */
public class GoogleContactsSinksContainerFactory extends SinksContainerFactory {

    public GoogleContactsSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer("googlecontacts");
    }

    public void initDefaultContainers() {
        SinksContainer google = new GoogleContactsSinksContainer("googlecontacts", "Google Talk"/*, "icon-googlecontacts"*/);
        google.setPreparedView("googlecontacts");
        setSinksContainer(google);
    }

    public void registerProcessors() {
        registerHistoryProcessor("googlecontacts", new GoogleContactsHistoryProcessor());
    }

    public void registerMenuItems() {

    }
}