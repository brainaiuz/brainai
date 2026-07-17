package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.ui.AddMultiLeadView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:33:02
 * To change this template use File | Settings | File Templates.
 */
public class MultiLeadAddSinksContainer extends SinksContainer {
    private static final CrmStrings crmStrings = CrmStrings.App.get();

    public MultiLeadAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        AddMultiLeadView addMultiLeadView;
        if (params != null && params.length > 2) {
            addMultiLeadView = new AddMultiLeadView(true, "addmultilead", Property.getPluralWithObjectCodeWithReplace(Constants.LEADS, crmStrings.addMultiLead(), wfmStrings.leads()), Integer.valueOf(params[1]), params[2]);
        } else {
            addMultiLeadView = new AddMultiLeadView(true, "addmultilead", Property.getPluralWithObjectCodeWithReplace(Constants.LEADS, crmStrings.addMultiLead(), wfmStrings.leads()));
        }
        addView(addMultiLeadView);
    }
}