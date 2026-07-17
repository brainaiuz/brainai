package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.GlobalEmployeeLeaveRequestView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 17.03.2018 14:39
 */
public class EmployeeLeaveRequestSinksContainer extends SinksContainer {



    public EmployeeLeaveRequestSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_LIVE_REQUEST)) {
            String selectedYear = null;
            if (params.length > 1) {
                selectedYear = params[1];
            }
            String description = (wfmStrings.leaveRequests());
            String name = "hrmsLeaveRequests";

            addView(new GlobalEmployeeLeaveRequestView(null, id, selectedYear, name, description, null));
        }
    }
}
