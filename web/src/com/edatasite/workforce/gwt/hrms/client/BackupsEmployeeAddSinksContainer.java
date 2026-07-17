package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.BackupsEmployeeAddEditView;

import java.util.LinkedList;

public class BackupsEmployeeAddSinksContainer extends SinksContainer {
    public BackupsEmployeeAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new BackupsEmployeeAddEditView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
    }
}
