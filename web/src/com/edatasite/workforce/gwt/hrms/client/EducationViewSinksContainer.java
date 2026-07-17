package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.talentprofile.EducationEditView;
import com.edatasite.workforce.gwt.hrms.client.ui.talentprofile.EducationSummaryView;

import java.util.LinkedList;

/**
 * User: unni
 * Date: Dec 2, 2009
 * Time: 3:50:29 PM
 */
public class EducationViewSinksContainer extends SinksContainer {

    public EducationViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_VIEW)) {
            addView(new EducationSummaryView(id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_EDIT)) {
            addView(new EducationEditView(id));
        }
    }
}