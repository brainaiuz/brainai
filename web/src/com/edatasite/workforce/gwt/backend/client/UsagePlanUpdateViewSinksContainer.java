package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.UsagePlanUpdateView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 24.08.2010
 * Time: 19:07:18
 */
public class UsagePlanUpdateViewSinksContainer extends SinksContainer {

    public UsagePlanUpdateViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String usagePlanId = null;
        String companyId = null;
        if (params.length > 1) {
            usagePlanId = params[0];
            companyId = params[1];
        }
        addView(new UsagePlanUpdateView(usagePlanId, companyId));
    }
}