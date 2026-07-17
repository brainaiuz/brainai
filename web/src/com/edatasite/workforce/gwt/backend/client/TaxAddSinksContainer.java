package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.BackendAddTaxView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 25.02.2009
 * Time: 16:16:19
 * To change this template use File | Settings | File Templates.
 */
public class TaxAddSinksContainer extends SinksContainer {
	public TaxAddSinksContainer(String name, String description) {
		super(name, description);
	}

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
		addView(new BackendAddTaxView());
	}
}