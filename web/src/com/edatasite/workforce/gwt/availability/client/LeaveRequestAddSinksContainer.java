package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AddLeaveRequestView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class LeaveRequestAddSinksContainer extends SinksContainer {

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    public LeaveRequestAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    protected void initViews() {
        Integer employeeID = Utils.getUserID();
        Integer objectId = null;
        boolean recalculate = false;

        if (params != null && params.length > 3 && "recalculate".equals(params[1])) {
            employeeID = Integer.parseInt(params[2]);
            if (params[3] != null && !"null".equals(params[3])) {
                objectId = Integer.parseInt(params[3]);
            }
            if (params[1] != null && "recalculate".equals(params[1])) {
                recalculate = true;
            }
            addView(new AddLeaveRequestView(employeeID, recalculate, objectId));
        } else if (params != null && params.length > 2) {
            employeeID = Integer.parseInt(params[1]);
            if (params[2] != null && !"null".equals(params[2])) {
                objectId = Integer.parseInt(params[2]);
            }
            addView(new AddLeaveRequestView(employeeID, params, objectId));
        } else if (params != null && params.length > 1) {
            employeeID = Integer.parseInt(params[1]);
            addView(new AddLeaveRequestView(employeeID, params));
        } else {
            addView(new AddLeaveRequestView(employeeID, params));
        }
    }

}
