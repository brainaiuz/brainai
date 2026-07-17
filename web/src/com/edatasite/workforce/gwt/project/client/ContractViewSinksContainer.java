package com.edatasite.workforce.gwt.project.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ui.view.ContractEditView;
import com.edatasite.workforce.gwt.project.client.ui.view.ContractSummaryView;

import java.util.LinkedList;

public class ContractViewSinksContainer extends SinksContainer {

    public ContractViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }


    protected void initViews() {
        ContractSummaryView contractSummaryView = new ContractSummaryView(this.id);
        contractSummaryView.ensureDebugId("ContractSummaryView");
        addView(contractSummaryView);

        if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_ADD_EDIT)) {
            ContractEditView contractEditView = new ContractEditView(id);
            contractEditView.ensureDebugId("contractEditView");
            addView(contractEditView);
        }

    }
}
