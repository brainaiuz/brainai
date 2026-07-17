package com.edatasite.workforce.gwt.backend.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 12/5/12
 * Time: 9:32 PM
 */
public class ReportingBackendSinksContainer extends SinksContainer implements Constants {

    public ReportingBackendSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        //register something UIs
//        addView(new CompanyListView(true));
    }
}