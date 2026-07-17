package com.edatasite.workforce.rest.v2.release10.pm;

import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.server.app.TimesheetServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingItemsResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.timesheet.FirstDayWeekTO;
import com.edatasite.workforce.rest.v2.release10.core.to.timesheet.TotalTimesheetsResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.timesheet.TotalTimesheetsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.timesheet.WeekTimesheetsResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.timesheet.WeekTimesheetsTO;
import com.edatasite.workforce.rest.v2.release10.enums.RelevanceIndicator;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Abdurakhmonov Farrukh on 12/18/2017.
 */

@Tag(name = "Timesheet", description = "Timesheets API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiTimesheetControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiTimesheetControllerV2.class);
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    @Autowired
    private TimesheetServiceLocal timesheetServiceLocal;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private ProjectServiceLocal projectServiceLocal;

    @Operation(summary = "Get Weekly Timesheets", description = "Retrieves list of weekly timesheets")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of weekly timesheets")})
    @RequestMapping(value = "/timesheets/week", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.PM_TIMESHEET})
    public Object getWeekTimesheets(@RequestParam(value = "start_date") String start_date,
                                    @RequestParam(value = "end_date") String end_date,
                                    @RequestParam(value = "project_id", required = false) Integer project_id,
                                    @RequestParam(value = "employee_id", required = false) Integer employee_id) throws RestException {

        Date startDate;
        Date endDate;
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        if (StringUtils.isNotBlank(start_date)) {
            try {
                startDate = longDateTimezoneFormat.parse(start_date);
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, "start_date format should be " + longDateTimezoneFormat.toPattern() + " (eg." + longDateTimezoneFormat.format(new Date()) + ").", INVALID, HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            startDate = ServerUtils.getStartDate(new Date());
        }
        if (StringUtils.isNotBlank(end_date)) {
            try {
                endDate = longDateTimezoneFormat.parse(end_date);
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, "end_date format should be " + longDateTimezoneFormat.toPattern() + " (eg." + longDateTimezoneFormat.format(new Date()) + ").", INVALID, HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            endDate = ServerUtils.getEndDate(new Date());
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setUseSelectedDate(true);
        filterParameter.setProjectId(project_id);
        filterParameter.setEmployeeId(employee_id);

        FastTimesheetData timesheetData = timesheetServiceLocal.getFastTimesheetData(new DateNonConvertable(startDate), new DateNonConvertable(endDate), null, filterParameter);

        ArrayList<WeekTimesheetsTO> weekTimesheetsResult = new ArrayList<>();

        for (FastTaskTransfer fastTaskTransfer : timesheetData.getTransferTasks()) {
            WeekTimesheetsTO weekTimesheet = new WeekTimesheetsTO();
            weekTimesheet.setId(fastTaskTransfer.getTaskId());
            weekTimesheet.setTitle(fastTaskTransfer.getEmplTaskName());
            if (fastTaskTransfer.getTaskStatus() != null) {
                weekTimesheet.setStatus(fastTaskTransfer.getTaskStatus().getStatusName());
                if (StringUtils.isNotBlank(fastTaskTransfer.getTaskStatus().getPriority())) {
                    weekTimesheet.setPriority(fastTaskTransfer.getTaskStatus().getPriority().toUpperCase());
                }
            }
            if (ServerUtils.dateEqual(ServerUtils.getDayStartTime(new Date()), fastTaskTransfer.getTaskEndDate())) {
                weekTimesheet.setRelevance_indicator(RelevanceIndicator.YELLOW.getName());
            } else if (new Date().after(fastTaskTransfer.getTaskEndDate())) {
                weekTimesheet.setRelevance_indicator(RelevanceIndicator.RED.getName());
            } else {
                weekTimesheet.setRelevance_indicator(RelevanceIndicator.GREEN.getName());
            }
            double[] days_info = new double[fastTaskTransfer.getDataItems().length];
            int i = 0;
            for (TimesheetDataItem timesheetDataItem : fastTaskTransfer.getDataItems()) {
                days_info[i++] = Double.valueOf(decimalFormat.format((timesheetDataItem.getMinutes() / 60.0)));
            }
            weekTimesheet.setDays_info(days_info);

            weekTimesheetsResult.add(weekTimesheet);
        }


        return successResponse(new WeekTimesheetsResultTO(weekTimesheetsResult));
    }

    @Operation(summary = "Get First Day of the Week", description = "Retrieves the first Day of the Week")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the first day of the week")})
    @RequestMapping(value = "/timesheets/first_day", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.PM_TIMESHEET})
    public Object getFirstDayWeek() throws RestException {
        Integer weekStart;
        try {
            EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
            weekStart = (numberingSettings == null || numberingSettings.getTimesheetWeekStart() == null) ? 2 : numberingSettings.getTimesheetWeekStart();
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        FirstDayWeekTO firstDay = new FirstDayWeekTO();
        switch (weekStart) {
            case 1 -> firstDay.setFirst_day("SUNDAY");
            case 2 -> firstDay.setFirst_day("MONDAY");
            case 3 -> firstDay.setFirst_day("TUESDAY");
            case 4 -> firstDay.setFirst_day("WEDNESDAY");
            case 5 -> firstDay.setFirst_day("THURSDAY");
            case 6 -> firstDay.setFirst_day("FRIDAY");
            case 7 -> firstDay.setFirst_day("SATURDAY");
        }
        return successResponse(firstDay);
    }

    @Operation(summary = "Get Total Timesheets", description = "Retrieves list of total timesheets")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of total timesheets")})
    @RequestMapping(value = "/timesheets/total", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.PM_TIMESHEET})
    public Object getTotalTimesheets(@RequestParam(value = "project_id", required = false) Integer project_id,
                                     @RequestParam(value = "employee_id", required = false) Integer employee_id) throws RestException {

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setUseSelectedDate(true);
        filterParameter.setProjectId(project_id);
        filterParameter.setEmployeeId(employee_id);

        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        Integer firstDayOfWeek = (numberingSettings == null || numberingSettings.getTimesheetWeekStart() == null) ? 2 : numberingSettings.getTimesheetWeekStart();

        Calendar startDateCal = new GregorianCalendar();
        startDateCal.setFirstDayOfWeek(firstDayOfWeek);

        Date endDate = ServerUtils.addDays(startDateCal.getTime(), 6);

        int weekOffset = timesheetServiceLocal.getWeekOffset(new DateNonConvertable(startDateCal.getTime()), new DateNonConvertable(endDate));

        FastTimesheetData timesheetData;
        try {
            timesheetData = timesheetServiceLocal.getFastTimesheetData(new DateNonConvertable(startDateCal.getTime()), new DateNonConvertable(endDate), weekOffset, filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<TotalTimesheetsTO> totalTimesheetsResult = new ArrayList<>();

        for (FastTaskTransfer fastTaskTransfer : timesheetData.getTransferTasks()) {

            TotalTimesheetsTO totalTimesheet = new TotalTimesheetsTO();
            totalTimesheet.setId(fastTaskTransfer.getTaskId());
            totalTimesheet.setTitle(fastTaskTransfer.getEmplTaskName());
            if (fastTaskTransfer.getTaskStatus() != null) {
                totalTimesheet.setStatus(fastTaskTransfer.getTaskStatus().getStatusName());
                if (StringUtils.isNotBlank(fastTaskTransfer.getTaskStatus().getPriority())) {
                    totalTimesheet.setPriority(fastTaskTransfer.getTaskStatus().getPriority().toUpperCase());
                }
            }

            if (ServerUtils.dateEqual(ServerUtils.getDayStartTime(new Date()), fastTaskTransfer.getTaskEndDate())) {
                totalTimesheet.setRelevance_indicator(RelevanceIndicator.YELLOW.getName());
            } else if (new Date().after(fastTaskTransfer.getTaskEndDate())) {
                totalTimesheet.setRelevance_indicator(RelevanceIndicator.RED.getName());
            } else {
                totalTimesheet.setRelevance_indicator(RelevanceIndicator.GREEN.getName());
            }

            totalTimesheet.setCompletion_percentage(fastTaskTransfer.getPercentCompleted());
            totalTimesheet.setEstimate(Double.valueOf(decimalFormat.format((fastTaskTransfer.getEstimatedTime() / 60.0))));
            totalTimesheet.setTotal(Double.valueOf(decimalFormat.format((fastTaskTransfer.getTotalMinutes() / 60.0))));

            totalTimesheetsResult.add(totalTimesheet);

        }

        totalTimesheetsResult.sort(new TotalTimesheetComparator().thenComparing(TotalTimesheetsTO::getCompletion_percentage));


        return successResponse(new TotalTimesheetsResultTO(totalTimesheetsResult));
    }

    @Operation(summary = "Get Timesheets Projects", description = "Request for a list of projects for filtering in the Timesheets section.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the timesheet projects")})
    @RequestMapping(value = "/timesheets/projects", method = RequestMethod.GET)
    public Object getProjects(@RequestParam(value = "query") String query,
                              @RequestParam(value = "limit", required = false) Integer limit,
                              @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        if (StringUtils.isBlank(query)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "query is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        /*if (limit == null || limit < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (offset == null || offset < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "offset is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (offset.equals(limit) && offset == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "limit and offset cannot be zero at the same time", REQUIRED, HttpStatus.BAD_REQUEST);
        }*/

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;

        query = query.replace("%20", " ").trim();

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(start);
        filterParameter.setLimit(maxLimit);
        filterParameter.setSearchKey(query);
        filterParameter.setPM(true);
        filterParameter.setLookUp(true);
        filterParameter.setSearchButton(true);

        ListResult<SelectItem> result;
        try {
            result = projectServiceLocal.getProjectLookUp(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PagingItemsResultTO<CategoryTO> pagingResult = new PagingItemsResultTO<>();
        pagingResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (maxLimit + start)) {
            pagingResult.setLeft(0);
        } else {
            pagingResult.setLeft(result.getTotal() - (start + maxLimit));
        }
        pagingResult.setCount(result.getList() != null ? result.getList().size() : 0);
        pagingResult.setOffset(start);

        ArrayList<CategoryTO> projects = new ArrayList<>();
        result.getList().forEach(project -> projects.add(new CategoryTO(project.getId(), (project.getCode() != null ? project.getCode() + " " : "") + project.getName())));

        pagingResult.setItems(projects);

        return successResponse(pagingResult);

    }

    private class TotalTimesheetComparator implements Comparator<TotalTimesheetsTO> {
        @Override
        public int compare(TotalTimesheetsTO o1, TotalTimesheetsTO o2) {

            if (o1 == null || StringUtils.isBlank(o1.getRelevance_indicator())) {
                return -1;
            }
            if (o2 == null || StringUtils.isBlank(o2.getRelevance_indicator())) {
                return 1;
            }

            if (o1.getRelevance_indicator().equals(o2.getRelevance_indicator())) {
                return 0;
            } else if (RelevanceIndicator.RED.getName().equalsIgnoreCase(o1.getRelevance_indicator())) {
                return -1;
            } else if (RelevanceIndicator.YELLOW.getName().equalsIgnoreCase(o1.getRelevance_indicator())) {
                if (!RelevanceIndicator.RED.getName().equalsIgnoreCase(o2.getRelevance_indicator())) {
                    return -1;
                } else {
                    return 1;
                }
            } else if (RelevanceIndicator.GREEN.getName().equalsIgnoreCase(o1.getRelevance_indicator())) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
