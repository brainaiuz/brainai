package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.ReportingTemplatesAddView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Sep 19, 2011
 * Time: 7:06:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingTemplatesAddSinksContainer extends SinksContainer {
    public ReportingTemplatesAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length > 1)
            addView(new ReportingTemplatesAddView(Integer.parseInt(params[0]), Integer.parseInt(params[1])));
        else
        addView(new ReportingTemplatesAddView(Integer.parseInt(params[0])));
    }
}
