package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.*;

import java.util.LinkedList;

public class ModulesCustomFieldsSinksContainer  extends SinksContainer {


    public ModulesCustomFieldsSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new CrmCustomFieldsListView());
        addView(new PMCustomFieldsListView());
        addView(new HrmsCustomFieldsListView());
        addView(new AccountingCustomFieldsListView());
        addView(new PayrollCustomFieldsListView());
        addView(new SettingsCustomFieldsListView());
    }
}
