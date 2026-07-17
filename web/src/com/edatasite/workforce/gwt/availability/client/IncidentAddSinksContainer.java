package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AddIncidentView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Sherzod
 * Date: May 25, 2009
 * Time: 2:31:15 PM
 */
public class IncidentAddSinksContainer extends SinksContainer {

    public IncidentAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_INCIDENT)) {
            Integer int_employeeID;
        if (params != null && params.length > 1) {
            int_employeeID = Integer.valueOf(params[1]);

            addView(new AddIncidentView(int_employeeID));
        } else {
            addView(new AddIncidentView());
        }}
    }
}
