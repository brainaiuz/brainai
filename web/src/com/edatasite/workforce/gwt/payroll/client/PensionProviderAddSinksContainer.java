package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PensionProviderView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 12, 2009
 * Time: 5:53:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionProviderAddSinksContainer extends SinksContainer {
    public PensionProviderAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params != null && params.length > 1) {
            addView(new PensionProviderView(Integer.parseInt(params[1])));
        } else {
            addView(new PensionProviderView());
        }
    }
}
