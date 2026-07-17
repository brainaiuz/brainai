package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.CompanyDocumentsListView;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeeDocumentsListView;

import java.util.LinkedList;

/**
 * User: admin
 * Date: Oct 31, 2009
 * Time: 7:56:56 PM
 */
public class HrmsDocumentsSinksContainer extends SinksContainer implements PermissionConstants {

    public HrmsDocumentsSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    public HrmsDocumentsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
    }

    @Override
    protected void initViews() {

        if (Utils.hasPermission(PermissionConstants.EMPLOYEE_DOCUMENTS_LIST)) {
            addView(new EmployeeDocumentsListView(id));
        }
        if (Utils.hasPermission(PermissionConstants.EMPLOYEE_INSURANCE_DOCUMENTS_LIST)) {
            addView(new EmployeeDocumentsListView(null, INSURANCE, "Insurance Document"));
        }
        if (Utils.hasPermission(PermissionConstants.COMPANY_DOCUMENTS_LIST)) {
            addView(new CompanyDocumentsListView());
        }
    }
}