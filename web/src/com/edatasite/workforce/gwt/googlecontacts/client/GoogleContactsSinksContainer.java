package com.edatasite.workforce.gwt.googlecontacts.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;
//import com.edatasite.workforce.gwt.googlecontacts.client.ui.AddGoogleContactsView;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.11.2008
 * Time: 19:57:40
 * To change this template use File | Settings | File Templates.
 */
public class GoogleContactsSinksContainer extends SinksContainer {

    public GoogleContactsSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    public GoogleContactsSinksContainer(String name, String description, String iconStyle, String d) {
        super(name, description);
    }

    protected void initViews() {
//        addView(new AddGoogleContactsView());
    }
}