package com.edatasite.workforce.gwt.availability.client.ui;

import com.edatasite.workforce.gwt.availability.client.ui.view.AddBenefitRequestView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by Djuraev on 8/7/15.
 */
public class AddBenefitRequestSinksContainer extends SinksContainer {

    public AddBenefitRequestSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.ADD_BENEFIT_REQUEST)) {
            super.addView(new AddBenefitRequestView(id));
        }
    }
}
