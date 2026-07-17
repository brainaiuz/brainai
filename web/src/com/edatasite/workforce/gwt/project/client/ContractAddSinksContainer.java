package com.edatasite.workforce.gwt.project.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ui.AddContractView;

import java.util.LinkedList;

public class ContractAddSinksContainer extends SinksContainer {

    public ContractAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_ADD_EDIT)) {
            addView(new AddContractView());
        }
    }

}
