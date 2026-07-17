package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.DependentSummaryView;
import com.edatasite.workforce.gwt.hrms.client.ui.EditDependentForm;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA. .
 * User: unni
 * Date: Oct 21, 2009
 * Time: 10:02:45 PM
 */
public class DependentViewSinksContainer extends SinksContainer {

    public DependentViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_DEPENDENT_SUMMARY)) {
            super.addView(new DependentSummaryView(id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_DEPENDENT)) {
            super.addView(new EditDependentForm(id));
        }
    }
}