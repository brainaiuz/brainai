package com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PermissionDeniedView;

import java.util.LinkedList;


public class MultiCashAdvanceAddSinksContainer extends SinksContainer {

    public MultiCashAdvanceAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {

        Integer objectId = null;
        if (params.length == 3) {
            objectId = Integer.valueOf(params[2]);
        }

        if (params.length == 1 && !(Utils.hasPermission(PermissionConstants.PAYROLL_MULTI_CASH_ADVANCE_ADD))) {
            addView(new PermissionDeniedView("You do not have permission to add Rental Order"));
        } else {
            if (Constants.POSTED.equals(params[1])) {
                addView(new MultiCashAdvancePostView(objectId));
            } else {
                addView(new MultiCashAdvanceAddEditView(params[1], objectId));
            }
        }
    }
}
