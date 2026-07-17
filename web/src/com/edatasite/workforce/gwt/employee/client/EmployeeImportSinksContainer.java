package com.edatasite.workforce.gwt.employee.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.ImportEmployeesView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 10, 2010
 * Time: 9:32:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeImportSinksContainer extends SinksContainer {

    public EmployeeImportSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        ImportEmployeesView importEmployeesView = new ImportEmployeesView(params.length > 1 ? params[1] : null);
        addView(importEmployeesView);
    }
}
