package com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;


public class MultiCashAdvanceViewSinksContainer extends SinksContainer {

    public MultiCashAdvanceViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.PAYROLL_MULTI_CASH_ADVANCE_VIEW)) {
            addView(new MultiCashAdvanceSummaryView(id, params[1]));
        }
    }
}
