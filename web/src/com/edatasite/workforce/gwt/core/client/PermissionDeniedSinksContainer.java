package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PermissionDeniedView;

import java.util.LinkedList;

/**
 * User: Fathulla
 * Date: 15.04.13
 * Time: 12:59
 */
public class PermissionDeniedSinksContainer extends SinksContainer {

    private boolean redirect;

    public PermissionDeniedSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    public PermissionDeniedSinksContainer(String name, String description, boolean redirect) {
        super(name, description, null, NONE, -1, false);
        this.redirect = redirect;
        super.initialize();
    }

    @Override
    protected void initViews() {
        addView(new PermissionDeniedView(redirect));
    }
}