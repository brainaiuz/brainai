package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.ui.AddMultiLeadView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 07-Jul-2009
 * Time: 17:33:02
 * To change this template use File | Settings | File Templates.
 */
public class MultiContactAddSinksContainer extends SinksContainer {
    private static final CrmStrings crmStrings = CrmStrings.App.get();

    public MultiContactAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        AddMultiLeadView view;
        if (params != null && params.length > 2) {
            view = new AddMultiLeadView(false, "addmulticontact", crmStrings.addMultiContact(), Integer.valueOf(params[1]), params[2]);
        } else {
            view = new AddMultiLeadView(false, "addmulticontact", crmStrings.addMultiContact(), null);
        }
        addView(view);
    }
}