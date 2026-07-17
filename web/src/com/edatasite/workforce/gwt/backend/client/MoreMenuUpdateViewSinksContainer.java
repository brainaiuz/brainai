package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.MoreMenuUpdateView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/22/11
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoreMenuUpdateViewSinksContainer extends SinksContainer {

	public MoreMenuUpdateViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
	protected void initViews() {
		addView(new MoreMenuUpdateView(id));
	}
}