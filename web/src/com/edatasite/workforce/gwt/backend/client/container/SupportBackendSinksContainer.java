package com.edatasite.workforce.gwt.backend.client.container;

import com.edatasite.workforce.gwt.backend.client.ui.view.AccountManagementListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.BlackListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.GenericSettingsListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.HelpDocumentListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.SubscriptionsListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 4/13/12
 * Time: 5:35 PM
 */
public class SupportBackendSinksContainer extends SinksContainer {


    public SupportBackendSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        //register something UIs
        addView(new SubscriptionsListView());
        addView(new AccountManagementListView());
        addView(new GenericSettingsListView());
        addView(new BlackListView());
        addView(new HelpDocumentListView());
    }
}