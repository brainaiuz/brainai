package com.edatasite.workforce.gwt.client.client;

import com.edatasite.workforce.gwt.client.client.factory.ClientSinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;

/**
 * Created by IntelliJ IDEA.
 * User: mansur
 * Date: Jan 8, 2008
 * Time: 1:15:39 PM
 * To change this template use File | Settings | File Templates.
 */

public class Client extends WorkforceEntryPoint {

    public void initSinksContainerFactory() {

        containerFactory = new ClientSinksContainerFactory(this);
    }

}
