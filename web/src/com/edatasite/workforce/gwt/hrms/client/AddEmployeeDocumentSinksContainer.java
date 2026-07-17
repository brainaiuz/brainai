package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.AddEmployeeDocumentsView;

import java.util.LinkedList;

/**
 * Created by Djuraev on 9/23/15.
 */
public class AddEmployeeDocumentSinksContainer extends SinksContainer {

    public AddEmployeeDocumentSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.UPLOAD_EMPLOYEE_DOCUMENTS)) {
            if (params != null && params[1] != null && !params[1].equals("") && !params[1].equals("null")) {
                if (params.length > 2 && params[2] != null) {
                    super.addView(new AddEmployeeDocumentsView(id, Integer.parseInt(params[1]), params[2]));
                } else {
                    super.addView(new AddEmployeeDocumentsView(id, Integer.parseInt(params[1])));
                }
            } else {
                if (params.length > 2 && params[2] != null) {
                    super.addView(new AddEmployeeDocumentsView(id, null, params[2]));
                } else {
                    super.addView(new AddEmployeeDocumentsView(id, null));
                }
            }
        }
    }
}
