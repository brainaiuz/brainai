package com.edatasite.workforce.gwt.profile.client.container;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 7/6/2020
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.AsteriskEmployeeListView;
import com.google.gwt.core.client.GWT;

import java.util.LinkedList;

public class AsteriskEmployeeListSinksContainer extends SinksContainer {

    public AsteriskEmployeeListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length > 0) {
            GWT.log("id of asterisk setttings is: " + id);
            addView(new AsteriskEmployeeListView(id));
        } else {
            GWT.log("id of asterisk setting is not found: " + id);
        }
    }
}