package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.BackendSummaryView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: JavaZone
 * Date: Sep 27, 2011
 * Time: 7:00:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackendViewSinksContainer extends SinksContainer {

    public BackendViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new BackendSummaryView(id));
    }
}