package com.edatasite.workforce.gwt.backend.client.ui;

import com.edatasite.workforce.gwt.backend.client.ui.view.AddEditHelpDocumentForm;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Dilshod Madrahimov
 * Date: 2/28/13
 * Time: 12:31 PM
 */
public class HelpDocumentAddFromSinksContainer extends SinksContainer {

    public HelpDocumentAddFromSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer objectID = params.length > 1 && params[1] != null && params[1].matches(Constants.REGEX_INTEGER) ? Integer.valueOf(params[1]) : null;
        addView(new AddEditHelpDocumentForm(objectID));
    }
}
