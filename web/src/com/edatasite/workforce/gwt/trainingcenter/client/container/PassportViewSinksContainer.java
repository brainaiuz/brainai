package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AddEditPassportView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.PassportSummaryView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 11/06/14
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public class PassportViewSinksContainer extends SinksContainer {

    public PassportViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AddEditPassportView(id));
        addView(new PassportSummaryView(id));
    }
}
