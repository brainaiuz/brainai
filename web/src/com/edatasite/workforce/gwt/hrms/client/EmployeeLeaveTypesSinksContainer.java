package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeeLeaveTypesView;

import java.util.LinkedList;

/**
 * User: Ilhom Lutfullaev
 * Date: 16.12.2009
 * Time: 16:42:25
 */
public class EmployeeLeaveTypesSinksContainer extends SinksContainer {

    public EmployeeLeaveTypesSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new EmployeeLeaveTypesView(Integer.valueOf(params[0]), Integer.valueOf(params[1])));
    }
}