package com.edatasite.workforce.gwt.timesheet.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.timesheet.client.TimesheetApprovalSinksContainer;
import com.edatasite.workforce.gwt.timesheet.client.TimesheetSubmitForApprovalSinksContainer;
/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Dec 17, 2010
 * Time: 9:13:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class TimesheetHistoryProcessor implements HistoryProcessor {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new TimesheetApprovalSinksContainer(containerName + strings[0], Property.getPluralWithObjectCode(Constants.TIMESHEET_APPROVAL_LIST, projectStrings.approveAllTimesheets()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new TimesheetSubmitForApprovalSinksContainer("timesheetadd", wfmStrings.submitForApproval(), params);
    }
}
