package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.CaseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.LeadListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.WebFormSummaryView;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 10, 2009
 * Time: 5:20:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebFormViewSinksContainer extends SinksContainer implements WebFormConstants {

    public WebFormViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        super.addView(new WebFormSummaryView(this.id));
        Integer objectId = null;
        String type = "";
        if (params.length > 1 && !params[1].equals("")) {
            objectId = Integer.parseInt(params[1]);
            if (params.length > 2 && !params[2].equals("")) {
                type = params[2];
            }
        }
        if (type.equals(LEAD_FORM) && Utils.hasPermission(PermissionConstants.CRM_LEADS_LIST)) {
            super.addView(new LeadListView(objectId));
        } else if (type.equals(CASE_FORM) && Utils.hasPermission(PermissionConstants.CRM_CASES_LIST)) {
            super.addView(new CaseListView(objectId));
        }
    }
}