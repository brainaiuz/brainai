package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.EmployeeBenefitAllowanceView;

import java.util.LinkedList;

/**
 * Created by Djuraev on 8/5/15.
 */
public class EmployeeBenefitSinksContainer extends SinksContainer {

    public EmployeeBenefitSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new EmployeeBenefitAllowanceView(Integer.valueOf(params[0]), Integer.valueOf(params[1])));
    }
}
