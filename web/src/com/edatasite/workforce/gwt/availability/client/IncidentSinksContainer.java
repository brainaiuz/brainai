package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.EditIncidentForm;
import com.edatasite.workforce.gwt.availability.client.ui.view.ViewIncidentForm;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Sherzod
 * Date: May 25, 2009
 * Time: 2:36:24 PM
 */
public class IncidentSinksContainer extends SinksContainer {

    public IncidentSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_SUMMARY_INCIDENT)) {
            addView(new ViewIncidentForm(id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_INCIDENT)) {
            addView(new EditIncidentForm(id));
        }
    }
}