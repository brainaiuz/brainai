package com.edatasite.workforce.gwt.reportingsystem.client.ui.step;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingSearch;

import java.util.LinkedList;

/**
 * Created by Virus on 8/27/14.
 */
public class ReportingSearchSinksContainer extends SinksContainer {
    public ReportingSearchSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(ReportingSearch.getInstance());
    }
}
