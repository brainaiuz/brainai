package com.edatasite.workforce.gwt.location.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.hrms.client.ui.PositionListView;
import com.edatasite.workforce.gwt.location.client.ui.AddLocationView;
import com.edatasite.workforce.gwt.location.client.ui.LocationDetailView;
import com.edatasite.workforce.gwt.team.client.ui.view.DepartmentListView;

import java.util.LinkedList;

/**
 * User: Dilshod
 * Date: 03.12.2009
 * Time: 14:28:18
 */
public class LocationViewSinksContainer extends SinksContainer {

    public LocationViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new LocationDetailView(id));

        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_LOCATION)) {
            addView(new AddLocationView(id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT)) {
            addView(new DepartmentListView(id));
        }

        addView(new EmployeeListView(id, true));
//
//        if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_LIST)) {
//            addView(new PositionListView(id));
//        }


    }
}