package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.BenefitSummaryView;

import java.util.LinkedList;

/**
 * Created by Khasan on 29.09.14.
 */
public class BenefitViewSinksContainer extends SinksContainer {
    public BenefitViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.BENEFIT_TYPE)) {
            addView(new BenefitSummaryView(id));
        }
    }
}
