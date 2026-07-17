package com.edatasite.workforce.gwt.core.client.factory;

import com.edatasite.workforce.gwt.core.client.PermissionDeniedSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;

/**
 * User: Fathulla
 * Date: 15.04.13
 * Time: 12:53
 */
public class PermissionDenyContainerFactory extends SinksContainerFactory {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public PermissionDenyContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
    }

    @Override
    public void initDefaultContainers() {
        SinksContainer deny = new PermissionDeniedSinksContainer("denied", wfmStrings.permissionDenied());
        deny.setPreparedView("denied");
        setSinksContainer(deny);
    }

    @Override
    public void registerProcessors() {
    }

    @Override
    public void registerMenuItems() {
    }
}