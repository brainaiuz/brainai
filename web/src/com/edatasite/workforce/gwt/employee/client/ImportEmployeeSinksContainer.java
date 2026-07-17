package com.edatasite.workforce.gwt.employee.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeImportView;

import java.util.LinkedList;

/**
 * User: Dilshod Madrahimov
 * Date: Aug 18, 2015
 * Time: 17:00:00 PM
 */
public class ImportEmployeeSinksContainer extends SinksContainer {

    public ImportEmployeeSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.SHOW_IMPORT_EMPLOYEE) || Utils.hasPermission(PermissionConstants.SHOW_IMPORT_EMPLOYEE_HRMS)) {
            String objectId;
//        Integer maxNoAccessCount = 0;
            if (params.length > 1) {
                objectId = params[1];
//            maxNoAccessCount = (params.length > 3 && params[3] != null) && "".equals(params[3]) ? Integer.parseInt(params[3]) : 0;
                addView(new EmployeeImportView(Integer.valueOf(objectId)/*, maxNoAccessCount*/));
            }
        }
    }
}
