package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddAsteriskEmployeeView;

import java.util.LinkedList;

/**
 * Created by Anvar Akramov on 7/6/2020.
 */
public class AsteriskEmployeeAddSinksContainer extends SinksContainer {
    public AsteriskEmployeeAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {

        Integer employeeId = Integer.parseInt(params[0]);
        Integer asteriskSettingsId = Integer.parseInt(params[1]);

        addView(new AddAsteriskEmployeeView(employeeId, asteriskSettingsId));
    }
}
