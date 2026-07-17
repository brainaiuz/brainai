package com.edatasite.workforce.gwt.timesheet.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.timesheet.client.ui.TimesheetSubmitForApprovalShell;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Dec 28, 2010
 * Time: 12:38:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimesheetApprovalSinksContainer extends SinksContainer {

    public TimesheetApprovalSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new TimesheetSubmitForApprovalShell(id));
    }
}
