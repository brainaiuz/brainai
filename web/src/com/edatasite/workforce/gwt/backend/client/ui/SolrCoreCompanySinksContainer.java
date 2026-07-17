package com.edatasite.workforce.gwt.backend.client.ui;

import com.edatasite.workforce.gwt.backend.client.ui.view.SolrCoreCompanyView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 15/03/12
 * Time: 20:45
 * To change this template use File | Settings | File Templates.
 */
public class SolrCoreCompanySinksContainer extends SinksContainer {
    public SolrCoreCompanySinksContainer(String name, String description,String[] params) {
        super(name, description, params, CLOSE);

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new SolrCoreCompanyView(params[1]));
    }
}
