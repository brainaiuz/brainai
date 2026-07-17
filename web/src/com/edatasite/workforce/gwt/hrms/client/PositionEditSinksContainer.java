package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.EditPositionForm;

import java.util.LinkedList;


/**
 * User: Admin
 * Date: 14.11.2009
 * Time: 16:22:09
 */
public class PositionEditSinksContainer extends SinksContainer {

    public PositionEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_EDIT) || Utils.isSettings()) {
            super.addView(new EditPositionForm(id));
        }
    }
}