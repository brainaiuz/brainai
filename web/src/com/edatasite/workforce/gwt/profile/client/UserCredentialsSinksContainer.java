package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.UserCredentials;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 17.03.2010
 * Time: 16:20:07
 */
public class UserCredentialsSinksContainer extends SinksContainer {

    public UserCredentialsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new UserCredentials());
    }
}
