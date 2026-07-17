package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.BugListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 09.06.2009
 * Time: 15:36:20
 * To change this template use File | Settings | File Templates.
 */
public class BugListPerEmployeeSinksContainer extends SinksContainer {
	public BugListPerEmployeeSinksContainer(String name, String description, String[] strings) {
		super(name, description, strings);
	}

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
		String statusId = null;
		if (params[1] != null) {
			statusId = params[1];
		}
		addView(new BugListView(id, statusId));
	}
}