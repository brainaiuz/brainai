package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.ActivationLinkView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ActivationLinkSinksContainer extends SinksContainer {

	public ActivationLinkSinksContainer(String name, String description, String[] params) {
		super(name, description, params);
	}

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
		addView(new ActivationLinkView(id));
	}
}