package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.contact.client.ui.AddContactView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddLeadView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:33:02
 * To change this template use File | Settings | File Templates.
 */
public class LeadAddSinksContainer extends SinksContainer {

    public LeadAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        AddLeadView addLeadView = null;
        if (params != null && params.length > 1) {
            if (AddContactView.FROM_OUTLOOK.equals(params[2])) {
                addLeadView = new AddLeadView(params[1]);
            } else if (params.length > 2 && COPY.equals(params[2])) {
                addLeadView = new AddLeadView(Integer.valueOf(params[1]), false, COPY);
            } else if (params.length > 2 && AddContactView.FROM_INCOMING_CALL.equals(params[2])) {
                addLeadView = new AddLeadView(null, false, AddContactView.FROM_INCOMING_CALL);
                addLeadView.setDefaultPhoneNumber(params[1]);
            } else {
                addLeadView = new AddLeadView(Integer.valueOf(params[1]), params[2]);
            }
        } else {
            addLeadView = new AddLeadView((Integer) null);
        }

        addView(addLeadView);
    }
}