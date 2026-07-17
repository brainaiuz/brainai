package com.edatasite.workforce.gwt.location.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.location.client.LocationAddSinksContainer;
import com.edatasite.workforce.gwt.location.client.LocationViewSinksContainer;

/**
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 17:54:55
 */
public class LocationHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new LocationViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {

        String permission;
        if (params.length > 1) {
            permission = PermissionConstants.HRMS_EDIT_LOCATION;
        } else {
            permission = PermissionConstants.HRMS_ADD_NEW_LOCATION;
        }
        if (Utils.hasPermission(permission)) {
            return new LocationAddSinksContainer("locationadd", Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), params);
        }
        return null;
    }
}