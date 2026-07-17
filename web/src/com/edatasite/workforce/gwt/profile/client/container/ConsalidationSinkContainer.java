package com.edatasite.workforce.gwt.profile.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.ConsalidationCompanyListView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 05/10/12
 * Time: 20:38
 * To change this template use File | Settings | File Templates.
 */
public class ConsalidationSinkContainer extends SinksContainer {

    public ConsalidationSinkContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ConsalidationCompanyListView());
    }
}
