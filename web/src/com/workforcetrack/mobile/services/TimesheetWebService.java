package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.timesheet.MProjectTree;
import com.workforcetrack.mobile.rpc.timesheet.MProjectTreeList;
import com.workforcetrack.mobile.rpc.timesheet.MTaskStatus;
import com.workforcetrack.mobile.rpc.timesheet.MTimesheetDataItem;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/1/11
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TimesheetWebService {

    MProjectTreeList getData(Date date);

    Boolean updateTimesheet(MProjectTree projectTree);

    Integer addUpdates(MTimesheetDataItem item);

    Integer applyUpdates(MTimesheetDataItem item);

    Boolean updateStatus(MTaskStatus status);

    Boolean updatePercentCompleted(Integer employeeTaskId, Float percentCompleted);
}


