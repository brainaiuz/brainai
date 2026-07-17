package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.contact.client.ui.AddCandidateView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: hayot
 * Date: 7/3/12
 * Time: 4:57 PM
 */
public class CandidateAddSinksContainer extends SinksContainer {
    public CandidateAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_CANDIDATE)) {
            addView(new AddCandidateView());
        }
    }
}
