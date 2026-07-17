package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.ImportOpportunityView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Sep 24, 2012
 * Time: 11:39:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrmOpportunityImportSinksContainer extends SinksContainer {

    public CrmOpportunityImportSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String objectId = null;
        if (params.length > 1) {
            objectId = params[1];
            addView(new ImportOpportunityView(Integer.valueOf(objectId)));
        }

    }
}
