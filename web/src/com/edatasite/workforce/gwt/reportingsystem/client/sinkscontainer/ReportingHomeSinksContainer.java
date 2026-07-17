package com.edatasite.workforce.gwt.reportingsystem.client.sinkscontainer;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.reportingsystem.client.ReportingSystem;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCategoryRPC;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingHome;

import java.util.LinkedList;

/**
 * Created by Virus on 9/10/14.
 */
public class ReportingHomeSinksContainer extends SinksContainer {

    public ReportingHomeSinksContainer(String name, String description, String[] strings) {
        super(name, description, strings, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        for (ReportingCategoryRPC category : ReportingSystem.categories) {
            addView(new ReportingHome(category));
        }
    }
}
