package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.ui.view.AccessTokenListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.DatabaseActionsView;
import com.edatasite.workforce.gwt.backend.client.ui.view.ReportingDBUrlListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.SchemaListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.SolrIndexView;
import com.edatasite.workforce.gwt.backend.client.ui.view.SolrMonitorView;
import com.edatasite.workforce.gwt.backend.client.ui.view.WhiteLabelListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Nov 3, 2010
 * Time: 11:39:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminManagementSinksContainer extends SinksContainer {
    private static final BackendStrings backendStrings = BackendStrings.App.get();

    AdminManagementSinksContainer(String name, String description) {
        super(name, description, null, NONE);

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new SchemaListView());
        addView(new SolrIndexView());
        addView(new SolrMonitorView());
        addView(new DatabaseActionsView());
        addView(new ReportingDBUrlListView(backendStrings.dynamicDBUrl()));
        addView(new AccessTokenListView());
        addView(new WhiteLabelListView());
    }


}