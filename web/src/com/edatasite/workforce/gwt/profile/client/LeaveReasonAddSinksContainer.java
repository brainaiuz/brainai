package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddLeaveReasonView;

import java.util.LinkedList;

/**
 * @author Hurshid on 12/17/2018
 */
public class LeaveReasonAddSinksContainer extends SinksContainer {

    public LeaveReasonAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        Boolean isView = null;
        if (id == null && params != null && params.length > 1) {
            id = params[1].equals("") ? null : Integer.valueOf(params[1]);
            isView = params[2] != null && "true".equals(params[2]);
        }
        addView(new AddLeaveReasonView(id, Boolean.TRUE.equals(isView)));
    }
}
