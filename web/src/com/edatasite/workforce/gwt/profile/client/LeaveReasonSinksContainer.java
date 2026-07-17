package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddLeaveReasonView;

import java.util.LinkedList;

/**
 * @author Hurshid on 12/17/2018
 */
public class LeaveReasonSinksContainer extends SinksContainer implements Constants {

    public LeaveReasonSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Boolean isView = null;
        if (params.length > 0) {
            id = params[0].equals("") ? null : Integer.valueOf(params[0]);
            isView = params[1] != null && "true".equals(params[1]);
        }
        addView(new AddLeaveReasonView(id, Boolean.TRUE.equals(isView)));
    }
}
