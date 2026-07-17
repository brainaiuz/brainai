package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.CustomisedPdfSettingsView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 18, 2011
 * Time: 6:00:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomisedPDFSettingsAddSinksContainer extends SinksContainer {

	public CustomisedPDFSettingsAddSinksContainer(String name, String description, String[] params) {
		super(name, description, params);
	}

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
		addView(new CustomisedPdfSettingsView(Integer.parseInt(params[1])));
	}
}