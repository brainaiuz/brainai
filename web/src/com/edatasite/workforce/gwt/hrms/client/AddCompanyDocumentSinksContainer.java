package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.AddCompanyDocumentsView;

import java.util.LinkedList;

/**
 * Created by Djuraev on 9/29/15.
 */
public class AddCompanyDocumentSinksContainer extends SinksContainer {

    public AddCompanyDocumentSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.UPLOAD_COMPANY_DOCUMENTS)) {
            super.addView(new AddCompanyDocumentsView(id, null));
        }
    }
}
