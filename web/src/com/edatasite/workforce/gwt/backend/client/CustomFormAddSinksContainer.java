package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.AddCustomFormView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class CustomFormAddSinksContainer extends SinksContainer {

    public CustomFormAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer objectID = params.length > 1 && params[1] != null && params[1].matches(Constants.REGEX_INTEGER) ? Integer.valueOf(params[1]) : null;
        Integer companyID = params.length > 2 && params[2] != null && params[2].matches(Constants.REGEX_INTEGER) ? Integer.valueOf(params[2]) : null;
        addView(new AddCustomFormView(objectID, companyID));
    }
}