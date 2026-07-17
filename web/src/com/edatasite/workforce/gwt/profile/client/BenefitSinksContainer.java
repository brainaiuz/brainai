package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddEditBenefitView;

import java.util.LinkedList;

/**
 * Created by Khasan on 08.09.14.
 */
public class BenefitSinksContainer extends SinksContainer {

    public BenefitSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length == 1) {
            addView(new AddEditBenefitView());
        } else if (params.length == 2) {
            addView(new AddEditBenefitView(Integer.valueOf(params[1])));
        }
    }
}
