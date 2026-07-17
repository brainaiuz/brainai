package com.edatasite.workforce.gwt.backend.client.container;

import com.edatasite.workforce.gwt.backend.client.ui.view.BackendManagementListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/23/12
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackendManagementListViewSinksContainer extends SinksContainer {

	public BackendManagementListViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
	protected void initViews() {
		addView(new BackendManagementListView(id, params[1]));
	}
}