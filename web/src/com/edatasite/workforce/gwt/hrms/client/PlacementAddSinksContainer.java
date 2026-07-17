package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.AddPlacementView;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 7/5/12
 * Time: 7:46 PM
 */
public class PlacementAddSinksContainer extends SinksContainer {

    public PlacementAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        Integer candidateID = params.length > 1 && params.length != 4 && params[1] != null ? Integer.valueOf(params[1]) : null;
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_PLACEMENT)) {
            addView(new AddPlacementView(null, candidateID));
        }
        if (params != null && params.length == 4 && "CONVERT".equals(params[1])) {
            String formType = params[2];
            Integer convertFormId = params[3] != null && params[3].matches(Constants.REGEX_INTEGER_POSITIVE) ? Integer.valueOf(params[3]) : null;
            addView(new AddPlacementView(convertFormId, formType));
        }
    }
}