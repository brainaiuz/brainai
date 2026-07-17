package com.edatasite.workforce.gwt.timesheet.server.app;

import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.timesheet.client.rpc.*;
import com.edatasite.workforce.rest.base.to.TimesheetWeeklyEntryTO;
import com.workforcetrack.mobile.rpc.timesheet.ProjectTaskForMobile;
import com.workforcetrack.mobile.rpc.timesheet.ProjectTreeForMobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: Sher
 * Date: 11/8/12 3:55 PM
 */
public interface TimesheetServiceLocal {

    ArrayList<ProjectTreeForMobile> getDataForMobile(Date date);

    void updateTimesheetForMobile(ProjectTaskForMobile[] items);

    SelectItem[] getEditTaskStatusDrop(Integer taskId);

    void sendMailToAccountants(Map<String, List<TimesheetItem>> data, EdsUser employeeId, Date startDate, Date endDate);

    Map<String, List<TimesheetItem>> getTimesheetData(Date startDate, Date endDate, EdsUser employeeId);

    void updateAttendanceForDeletedTask(EdsTask task);

    FastTimesheetData getFastTimesheetData(DateNonConvertable selectedDate, DateNonConvertable clientsDate, Integer weekOffset, ListingFilterParameter filterParameters);

    TimesheetSettings getTimesheetSettings();

    ArrayList<TimesheetWeeklyEntryTO> getDailyStatistics(DateNonConvertable selectedDate);

    Integer getWeekOffset(DateNonConvertable clientsCurrenDate, DateNonConvertable choosenDate);

    void fillTimesheetFromResUtil(Integer companyId);

    Float updatePercentCompleted(Integer employeeTaskId, float percentCompleted, boolean solrUpdate);

    List<TimeSheetReportTO> getDailyTimesheets(Integer companyId);

    Integer applyUpdates(TimesheetDataItem update, Integer synchItemId);
}
