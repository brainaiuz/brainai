package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.ActiveChatView;
import com.edatasite.workforce.gwt.backend.client.ui.view.ClientPermissionView;
import com.edatasite.workforce.gwt.backend.client.ui.view.SolrMonitorView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: Jan 4, 2011
 * Time: 9:53:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SolrManagementSinksContainer extends SinksContainer {

    public SolrManagementSinksContainer(String name, String description) {
        super(name, description, null, 0);

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new SolrMonitorView());
        addView(new ClientPermissionView());
        addView(new ActiveChatView());
    }
}
