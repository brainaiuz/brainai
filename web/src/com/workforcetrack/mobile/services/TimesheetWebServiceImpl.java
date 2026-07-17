package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskStatus;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.server.app.TimesheetServiceLocal;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.timesheet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/** User: sancho  Date: 6/1/11
 */
@Service("timesheetWebService")
public class TimesheetWebServiceImpl implements TimesheetWebService {

    @Autowired
    TimesheetService timesheetService;
    @Autowired
    @Qualifier("timesheetService")
    TimesheetServiceLocal timesheetServiceLocal;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;

    @Override
    public MProjectTreeList getData(Date date) {
        ArrayList<ProjectTreeForMobile> resData = timesheetServiceLocal.getDataForMobile(date);

        return new MProjectTreeList(resData);

    }

    @Override
    public Boolean updateTimesheet(MProjectTree projectTree) {

        if (projectTree == null || projectTree.getTasks() == null)
            return null;

        try {
            List<ProjectTaskForMobile> items = new ArrayList<>();
            for (MProjectTask mProjectTask : projectTree.getTasks()) {
                ProjectTaskForMobile projectTaskForMobile = new ProjectTaskForMobile();
                if (MProjectTask.convert(mProjectTask, projectTaskForMobile, true)) {
                    items.add(projectTaskForMobile);
                }
            }
            ProjectTaskForMobile[] itemsArray = items.toArray(new ProjectTaskForMobile[]{});
            timesheetServiceLocal.updateTimesheetForMobile(itemsArray);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }


    public Integer applyUpdates(MTimesheetDataItem item) {
        TimesheetDataItem mItem = MTimesheetDataItem.convertFromMobile(item);
        return timesheetService.applyUpdates(mItem, null);
    }

    @Transactional
    public Integer addUpdates(MTimesheetDataItem item) {
        if (item.getMinutes() < 0) {
            return null;
        }
        Integer result = result = -1;
        try {
            Date currentDate = item.getDate() != null ? item.getDate() : new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(currentDate);
            ServerUtils.setBeginningOfTheDay(calendar);
            TimesheetDataItem timesheetItem = item.convert(null);
            Integer todays = item.getMinutes();
            EdsEmployeeTask employeeTask = employeeTaskManager.get(!WebServiceUtils.isEmptyOrNull(item.getOldEmployeeTaskID()) ? item.getOldEmployeeTaskID() : item.getEmployeeTaskID());
            List<EdsTimeSheet> timeSheetList = timeSheetManager.getTimeSheets(employeeTask.getTask().getObjectID(), employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID(), calendar.getTime());
            Integer sumEmployeeSpentToTaskInterval = 0;
            timesheetItem.getOldEmployeeTaskIDList().clear();
            for (EdsTimeSheet timeSheet : timeSheetList) {
                sumEmployeeSpentToTaskInterval += timeSheet.getTimeSpent() != null ? timeSheet.getTimeSpent() : 0;
                if (timeSheet.getEmployeeTask().getDeleted() && timeSheet.getTimeSpent() > 0) {
                    timesheetItem.getOldEmployeeTaskIDList().add(timeSheet.getEmployeeTask().getObjectID());
                }
            }

            todays += sumEmployeeSpentToTaskInterval;
            timesheetItem.setDate(calendar.getTime());
            timesheetItem.setMinutes(todays);
            timesheetService.applyUpdates(timesheetItem, null);
            result = todays;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Boolean updateStatus(MTaskStatus status) {
        try {
            TaskStatus mStatus = MTaskStatus.convertFromMobile(status);
            timesheetService.updateStatus(mStatus);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public Boolean updatePercentCompleted(Integer employeeTaskId, Float percentCompleted) {
        try {
            timesheetService.updatePercentCompleted(employeeTaskId, percentCompleted, true);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }
}
