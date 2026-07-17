package com.edatasite.workforce.gwt.issue.client;


import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.issue.client.factory.IssueSinksContainerFactory;

/**
 * User: Acer
 * Date: 06-Jan-2008
 * Time: 22:05:02
 */
public class Issue extends WorkforceEntryPoint {

    public void initSinksContainerFactory() {
        containerFactory = new IssueSinksContainerFactory(this);
    }
}