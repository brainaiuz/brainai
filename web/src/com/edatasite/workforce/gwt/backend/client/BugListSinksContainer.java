package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.BugListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 2:23:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class BugListSinksContainer extends SinksContainer {
	public BugListSinksContainer(String name, String description, String[] strings) {
		super(name, description, strings);
	}

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
		addView(new BugListView());
	}
}