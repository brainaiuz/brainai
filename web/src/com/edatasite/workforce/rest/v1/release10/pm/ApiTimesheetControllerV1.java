package com.edatasite.workforce.rest.v1.release10.pm;

import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetSettings;
import com.edatasite.workforce.gwt.timesheet.server.app.TimesheetServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.TimesheetTO;
import com.edatasite.workforce.rest.base.to.TimesheetTaskEntryTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Umidbek on 21.02.2015.
 */
@Tag(name = "Timesheet", description = "Timesheet API")
@RestController
@RequestMapping(value = "/timesheet", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiTimesheetControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private TimesheetServiceLocal timesheetServiceLocal;
    @Autowired
    private TimeSheetManager timeSheetManager;

    @RequestMapping(value = "/daily/{date}", method = RequestMethod.GET)
    public Object getDailyTimesheet(@PathVariable(value = "date") Long date) {
        DateNonConvertable selectedDateNonConvertable = new DateNonConvertable(WrapUtils.longToDate(date));
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setUseSelectedDate(true);
        filter.setFromMobile(true);
        filter.setShortList(true);//returns given date data

        Calendar selectedDateCal = new GregorianCalendar();
        selectedDateCal.setTime(WrapUtils.longToDate(date));
        selectedDateCal.set(Calendar.AM_PM, 0);
        selectedDateCal.set(Calendar.HOUR, 0);
        selectedDateCal.set(Calendar.MINUTE, 0);
        selectedDateCal.set(Calendar.SECOND, 0);
        selectedDateCal.set(Calendar.MILLISECOND, 0);

        int weekOffset = timesheetServiceLocal.getWeekOffset(new DateNonConvertable(), selectedDateNonConvertable);
        FastTimesheetData data = timesheetServiceLocal.getFastTimesheetData(selectedDateNonConvertable, new DateNonConvertable(), weekOffset, filter);
        return successResponse(new TimesheetTO(data, true, WrapUtils.dateToLong(selectedDateCal.getTime())));
    }

    @RequestMapping(value = "/weekly/{date}", method = RequestMethod.GET)
    public Object getWeeklyTimesheet(@PathVariable(value = "date") Long date) {
        DateNonConvertable selectedDateNonConvertable = new DateNonConvertable(WrapUtils.longToDate(date));
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setUseSelectedDate(true);
        int weekOffset = timesheetServiceLocal.getWeekOffset(new DateNonConvertable(), selectedDateNonConvertable);
        FastTimesheetData data = timesheetServiceLocal.getFastTimesheetData(selectedDateNonConvertable, new DateNonConvertable(), weekOffset, filter);

        Calendar selectedDateCal = new GregorianCalendar();
        selectedDateCal.setTime(WrapUtils.longToDate(date));
        selectedDateCal.set(Calendar.AM_PM, 0);
        selectedDateCal.set(Calendar.HOUR, 0);
        selectedDateCal.set(Calendar.MINUTE, 0);
        selectedDateCal.set(Calendar.SECOND, 0);
        selectedDateCal.set(Calendar.MILLISECOND, 0);

        return successResponse(new TimesheetTO(data, false, WrapUtils.dateToLong(selectedDateCal.getTime())));
    }

    @RequestMapping(value = "/weekly/daytotals/{date}", method = RequestMethod.GET)
    public Object getTimesheetWeeklyDayTotals(@PathVariable(value = "date") Long date) {
        return successResponse(timesheetServiceLocal.getDailyStatistics(new DateNonConvertable(new Date(date))));
    }


    @RequestMapping(value = "/{date}/{taskId}", method = RequestMethod.GET)
    public Object getTime(@PathVariable(value = "date") Long date,
                          @PathVariable(value = "taskId") Integer taskId) {

        return successResponse(getTaskTimesheet(taskId, date));
    }

    @RequestMapping(value = "/{date}/{taskId}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object addTime(@RequestBody TimesheetTaskEntryTO taskEntry,
                          @PathVariable(value = "date") Long date,
                          @PathVariable(value = "taskId") Integer taskId) {

        TimesheetSettings timesheetSettings = timesheetServiceLocal.getTimesheetSettings();
        if (timesheetSettings.isTimesheetCommentRequired()) {
            if (ServerUtils.isNullOrEmpty(taskEntry.getComment())) {
                return this.errorResponse("Timesheet comment is compulsory for this account.");
            } else {
                return this.saveTime(taskEntry, date, taskId);
            }
        }
        return this.saveTime(taskEntry, date, taskId);
    }


    @RequestMapping(value = "/{date}/{taskId}", method = RequestMethod.DELETE)
    public Object removeTime(@PathVariable(value = "date") Long date,
                             @PathVariable(value = "taskId") Integer taskId) {
        TimesheetTaskEntryTO entry = getTaskTimesheet(taskId, date);
        if (entry != null) {
            entry.setMinutes(0);
            entry.setComment("");
            return this.saveTime(entry, date, taskId);
        }
        return errorResponse(ERROR_FAILED_UPDATE);
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public Object getSettings() {
        return successResponse(timesheetServiceLocal.getTimesheetSettings());
    }

    private Object saveTime(TimesheetTaskEntryTO taskEntry, Long date, Integer taskId) {
        TimesheetDataItem item = new TimesheetDataItem();

        item.setDate(new Date(date));
        item.setTaskID(taskId);

        item.setMinutes(taskEntry.getMinutes());
        item.setComment(taskEntry.getComment());

        try {
            if (taskServiceLocal.setTimeToTimesheet(item)) {
                return this.successResponse(SUCCESS_SAVE);
            } else {
                return this.errorResponse(ERROR_FAILED_SAVE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAILED_SAVE);
        }
    }

    private TimesheetTaskEntryTO getTaskTimesheet(Integer taskId, Long date) {
        TimesheetTaskEntryTO entry = new TimesheetTaskEntryTO();
        EdsUser user = timeSheetManager.getUser();
        List<EdsTimeSheet> list = timeSheetManager.getTimeSheets(taskId, user.getObjectID(), ServerUtils.getStartDate(new Date(date)));

        if (list != null && !list.isEmpty()) {
            EdsTimeSheet timeSheet = list.get(0);

            entry.setComment(timeSheet.getComment());
            entry.setMinutes(timeSheet.getTimeSpent());
        }
        return entry;
    }
}
