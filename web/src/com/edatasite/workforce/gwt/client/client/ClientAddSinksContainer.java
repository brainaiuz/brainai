package com.edatasite.workforce.gwt.client.client;

import com.edatasite.workforce.gwt.client.client.ui.view.AddClientView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_CUSTOMER_ADD;

public class ClientAddSinksContainer extends SinksContainer {

    public ClientAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        boolean skipValidation = Utils.isSettings() || Utils.isTrainingCenter();
        if (skipValidation || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CUSTOMER_ADD)) || ((Utils.isPM() || Utils.isHRMS()) &&
                (Utils.hasPermission(PermissionConstants.PM_CUSTOMER_ADD_CLIENT) || Utils.hasPermission(PermissionConstants.HRMS_CUSTOMER_ADD_CLIENT)))) {
            addView(new AddClientView(null, params));
        }
    }

}
