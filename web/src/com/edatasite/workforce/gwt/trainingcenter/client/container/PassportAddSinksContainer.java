package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AddEditPassportView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 11/06/14
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class PassportAddSinksContainer extends SinksContainer {

    public PassportAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AddEditPassportView());
    }
}
