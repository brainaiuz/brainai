package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.talentprofile.AwardEditView;
import com.edatasite.workforce.gwt.hrms.client.ui.talentprofile.AwardSummaryView;

import java.util.LinkedList;

/**
 * User: unni
 * Date: Dec 3, 2009
 * Time: 2:30:38 PM
 */
public class AwardViewSinksContainer extends SinksContainer {

    public AwardViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_EDIT) && params[0].equals("education")) {
            addView(new AwardEditView(id));
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_VIEW) && params.length == 1) {
            addView(new AwardSummaryView(id));
        }
    }
}