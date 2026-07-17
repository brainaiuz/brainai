package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.HelpDocumentListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Dilshod Madrahimov
 * Date: 2/27/13
 * Time: 2:48 PM
 */
public class HelpDocumentsSinksContainer extends SinksContainer {

    public HelpDocumentsSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new HelpDocumentListView());
    }
}
