package com.edatasite.workforce.gwt.timesheet.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.timesheet.client.NewTimesheetApprovalSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 19.01.2011
 * Time: 18:25:15
 * To change this template use File | Settings | File Templates.
 */
public class TimesheetApprovalHistoryProcessor implements HistoryProcessor {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new NewTimesheetApprovalSinksContainer(containerName + strings[0], Property.getPluralWithObjectCode(Constants.TIMESHEET_APPROVAL_LIST, projectStrings.approveAllTimesheets()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
