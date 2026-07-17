package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.ContactPrivelegiesView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 13.08.2010
 * Time: 15:45:23
 * To change this template use File | Settings | File Templates.
 */
public class ContactPrivelegiesSinksContainer extends SinksContainer {
	public ContactPrivelegiesSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
		super.addView(new ContactPrivelegiesView(id));
	}
}