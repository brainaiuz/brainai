package com.edatasite.workforce.gwt.timesheet.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.timesheet.client.ui.TimesheetSubmitForApprovalShell;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Dec 17, 2010
 * Time: 9:17:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class TimesheetSubmitForApprovalSinksContainer extends SinksContainer {

    public TimesheetSubmitForApprovalSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String employeeID = null;

        if (params.length > 1) {
            employeeID = params[1];
        }

        super.addView(new TimesheetSubmitForApprovalShell(employeeID));
    }
}
