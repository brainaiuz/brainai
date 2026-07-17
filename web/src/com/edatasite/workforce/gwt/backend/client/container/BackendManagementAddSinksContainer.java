package com.edatasite.workforce.gwt.backend.client.container;

import com.edatasite.workforce.gwt.backend.client.ui.view.AddEditBackendManagementView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/23/12
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackendManagementAddSinksContainer extends SinksContainer {

    public BackendManagementAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String objectID = params.length > 3 ? params[3] : null;
        addView(new AddEditBackendManagementView(params[1], params[2], objectID));
    }
}