package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.AddEditPositionView;

import java.util.LinkedList;

/**
 * User: Ilhom Lutfullaev
 * Date: 12.12.2009
 * Time: 16:46:42
 */
public class AddPositionSinksContainer extends SinksContainer {

    public AddPositionSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_POSITION) || Utils.isSettings()) {
            if (params.length > 2 && "copyPosition".equals(params[1])) {
                super.addView(new AddEditPositionView(Integer.valueOf(params[2])));
            } else {
                super.addView(new AddEditPositionView());
            }
        }
    }
}