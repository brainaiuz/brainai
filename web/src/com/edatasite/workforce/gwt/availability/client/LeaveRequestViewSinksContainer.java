package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.LeaveRequestView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: unni
 * Date: Aug 25, 2009
 * Time: 5:20:22 PM
 */
public class LeaveRequestViewSinksContainer extends SinksContainer {

    public LeaveRequestViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        super.addView(new LeaveRequestView(id));
    }
}