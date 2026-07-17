package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.AddDependentView;

import java.util.LinkedList;

/**
 * User: unni
 * Date: Oct 21, 2009
 * Time: 9:58:38 PM
 */
public class DependentAddSinksContainer extends SinksContainer {

    public DependentAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPENDENT)) {
            String empID = null;
            boolean isFromCandidate = false;
            if (params.length > 1) {
                empID = params[1];
            }
            if (params.length > 2) {
                isFromCandidate = Boolean.valueOf(params[2]);
            }

            if (isFromCandidate) {
                addView(new AddDependentView(Integer.valueOf(empID), isFromCandidate));
            } else {
                if (empID != null) {
                    Integer employeeId = Integer.valueOf(empID);
                    addView(new AddDependentView(null, employeeId));

                } else {
                    addView(new AddDependentView(null));
                }
            }
        }
    }
}