package com.edatasite.workforce.gwt.googlecontacts.client;

import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.googlecontacts.client.factory.GoogleContactsSinksContainerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.11.2008
 * Time: 19:57:20
 * To change this template use File | Settings | File Templates.
 */
public class GoogleContacts extends WorkforceEntryPoint {

    public void initSinksContainerFactory() {
        containerFactory = new GoogleContactsSinksContainerFactory(this);
    }
}