package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddEditEmailTemplatesView;

import java.util.LinkedList;

/**
 * User: Admin
 * Date: 15.03.2010
 * Time: 18:19:11
 */
public class EmailTemplatesAddSinksContainer extends SinksContainer {
    public EmailTemplatesAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        super.addView(new AddEditEmailTemplatesView());
    }
}
