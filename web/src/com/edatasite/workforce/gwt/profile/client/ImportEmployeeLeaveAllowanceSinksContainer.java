package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.ImportEmployeeAllowanceView;

import java.util.LinkedList;

public class ImportEmployeeLeaveAllowanceSinksContainer extends SinksContainer {

    public ImportEmployeeLeaveAllowanceSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new ImportEmployeeAllowanceView(Integer.valueOf(params[1])));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
