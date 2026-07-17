package com.edatasite.workforce.gwt.timesheet.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.timesheet.client.ui.TimesheetApprovalViewPanel;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 19.01.2011
 * Time: 18:29:22
 * To change this template use File | Settings | File Templates.
 */
public class NewTimesheetApprovalSinksContainer extends SinksContainer {

    public NewTimesheetApprovalSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new TimesheetApprovalViewPanel(id));
    }
}
