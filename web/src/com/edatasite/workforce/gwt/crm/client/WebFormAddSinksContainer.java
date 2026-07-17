package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddWebFormView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:33:02
 * To change this template use File | Settings | File Templates.
 */
public class WebFormAddSinksContainer extends SinksContainer {

    public WebFormAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params != null && params.length > 1) {
            if (params[1] != null) {
                super.addView(new AddWebFormView(null, params[1]));
            }

        }
    }
}