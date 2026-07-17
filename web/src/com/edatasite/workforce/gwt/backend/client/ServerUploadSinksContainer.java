package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.AddServerUploadView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;


/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Apr 27, 2011
 * Time: 2:25:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerUploadSinksContainer extends SinksContainer {

    public ServerUploadSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AddServerUploadView());
    }
}
