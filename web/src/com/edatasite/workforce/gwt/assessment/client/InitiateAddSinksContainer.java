package com.edatasite.workforce.gwt.assessment.client;

import com.edatasite.workforce.gwt.assessment.client.ui.view.ui.AddInitiateSimpleAppraisalView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class InitiateAddSinksContainer extends SinksContainer {

    public InitiateAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer empId = null;
        Integer shiftItemId = null;
        int length = params.length;
        if (length > 2) {
            empId = Integer.valueOf(params[1]);
            shiftItemId = Integer.valueOf(params[2]);
        } else if (length > 1) {
            empId = Integer.valueOf(params[1]);
        }
        if (shiftItemId != null) {
            addView(new AddInitiateSimpleAppraisalView(empId,shiftItemId));
        } else if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_APPRAISALS)) {
            addView(new AddInitiateSimpleAppraisalView(empId));
        }
    }

}
