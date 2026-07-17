package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.CandidatesListView;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.EditVacancyForm;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.ViewVacancyForm;

import java.util.LinkedList;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/22/12
 * Time: 5:31 PM
 */

public class VacancySinksContainer extends SinksContainer {

    public VacancySinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params != null && params.length == 3 && "CONVERT".equals(params[0])) {
            addView(new EditVacancyForm(params[1], Integer.valueOf(params[2])));
        }
        addView(new ViewVacancyForm(id));
        addView(new CandidatesListView(id));
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_VACANCY)) {
            addView(new EditVacancyForm(id));
        }
        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(id, RelationItem.TYPE_VACANCY));
        }
    }
}