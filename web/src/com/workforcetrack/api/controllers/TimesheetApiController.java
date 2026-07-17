package com.workforcetrack.api.controllers;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.timesheet.client.rpc.*;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.base.RestServiceUtils;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.api.presenter.BaseApiPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 25/09/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/timesheet")
public class TimesheetApiController {

    @Autowired
    private TimesheetService timesheetService;

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object search(@RequestParam(value = "selectedDate") Date selectedDate,
                         @RequestParam(value = "clientsDate") Date clientsDate,
                         @RequestParam(value = "weekOffset", defaultValue = "0") int weekOffset,
                         @RequestParam(value = "clientId", required = false, defaultValue = "0") int clientId,
                         @RequestParam(value = "projectId", required = false, defaultValue = "0") int projectId,
                         @RequestParam(value = "workStreamId", required = false, defaultValue = "0") int workStreamId) throws BaseApiException {
        try {

            ListingFilterParameter fp = new ListingFilterParameter();
            if (clientId != 0) {
                fp.setClientId(clientId);
            }
            if (projectId != 0) {
                fp.setProjectId(projectId);
            }
            if (workStreamId != 0) {
                fp.setWorkstreamID(workStreamId);
            }

            DateNonConvertable _selectedDate = new DateNonConvertable(selectedDate);
            DateNonConvertable _clientDate = new DateNonConvertable(clientsDate);

            FastTimesheetData searchResult = timesheetService.getFastTimesheetData(_selectedDate, _clientDate, weekOffset, fp);
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put(BaseApiPresenter.TOTAL_COUNT, searchResult != null ? 1 : 0);
            resultMap.put(BaseApiPresenter.ITEMS, searchResult);
            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "applyUpdates", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object applyUpdates(@RequestBody Map<String, Object> params) throws BaseApiException {
        try {
            Map<String, Object> saveDataMap = (Map<String, Object>) params.get(APIConstants.SAVE_DATA);
            if (saveDataMap == null || saveDataMap.isEmpty()) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }

            TimesheetDataItem item = new TimesheetDataItem();
            item.setId((Integer) saveDataMap.get("id"));
            item.setEmployeeTaskID((Integer) saveDataMap.get("employeeTaskID"));
            item.setQuickbookTimesheetID((String) saveDataMap.get("quickbookTimesheetID"));
            item.setQuickbookEditSequence((String) saveDataMap.get("quickbookEditSequence"));

            SimpleDateFormat dateFormat = new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT);
            DateNonConvertable date = new DateNonConvertable();
            if (saveDataMap.get("dateNonConvertable") != null) {
                date.setDate(dateFormat.parse((String) ((Map<String, Object>) saveDataMap.get("dateNonConvertable")).get("date")));
                item.setDateNonConvertable(date);
            }
            if (saveDataMap.get("date") != null)
                item.setDate(dateFormat.parse((String) saveDataMap.get("date")));

            item.setMinutes(saveDataMap.get("minutes") != null ? (Integer) saveDataMap.get("minutes") : 0);
            item.setComment((String) saveDataMap.get("comment"));
            if (saveDataMap.get("status") != null)
                item.setStatus((Integer) saveDataMap.get("status"));
            if (saveDataMap.get("teamID") != null)
                item.setTeamID((Integer) saveDataMap.get("teamID"));
            if (saveDataMap.get("employeeID") != null)
                item.setEmployeeID((Integer) saveDataMap.get("employeeID"));

            if (saveDataMap.get("projectID") != null)
                item.setProjectID((Integer) saveDataMap.get("projectID"));
            if (saveDataMap.get("taskID") != null)
                item.setTaskID((Integer) saveDataMap.get("taskID"));
            if (saveDataMap.get("oldEmployeeTaskID") != null)
                item.setOldEmployeeTaskID((Integer) saveDataMap.get("oldEmployeeTaskID"));
            if (saveDataMap.get("timeslotMinutes") != null)
                item.setTimeslotMinutes((Integer) saveDataMap.get("timeslotMinutes"));
            if (saveDataMap.get("leaveRequestMinutes") != null)
                item.setLeaveRequestMinutes((Integer) saveDataMap.get("leaveRequestMinutes"));
            if (saveDataMap.get("timesheetMinutes") != null)
                item.setTimesheetMinutes((Integer) saveDataMap.get("timesheetMinutes"));
            if (saveDataMap.get("hourTypeID") != null)
                item.setHourTypeID((Integer) saveDataMap.get("hourTypeID"));
            if (saveDataMap.get("holiday") != null)
                item.setHoliday((Boolean) saveDataMap.get("holiday"));
            if (saveDataMap.get("taskEnd") != null)
                item.setTaskEnd(dateFormat.parse((String) saveDataMap.get("taskEnd")));
            if (saveDataMap.get("fromQuickbooks") != null)
                item.setFromQuickbooks((Boolean) saveDataMap.get("fromQuickbooks"));

            return timesheetService.applyUpdates(item, null);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "settings", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object getTimesheetSettings() throws BaseApiException {
        try {
            TimesheetSettings searchResult = timesheetService.getTimesheetSettings();
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put(BaseApiPresenter.TOTAL_COUNT, searchResult != null ? 1 : 0);
            resultMap.put(BaseApiPresenter.ITEMS, searchResult);
            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "weekOffset", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object getWeekOffset(@RequestParam(value = "selectedDate") Date selectedDate,
                                @RequestParam(value = "clientsDate") Date clientsDate) throws BaseApiException {
        try {
            DateNonConvertable _selectedDate = new DateNonConvertable(selectedDate);
            DateNonConvertable _clientDate = new DateNonConvertable(clientsDate);
            return timesheetService.getWeekOffset(_clientDate, _selectedDate);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "updatePercentCompleted", method = RequestMethod.PUT, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object updatePercentCompleted(@RequestParam(value = "employeeTaskId") int employeeTaskId,
                                         @RequestParam(value = "percentCompleted") float percentCompleted) throws BaseApiException {
        try {
            return timesheetService.updatePercentCompleted(employeeTaskId, percentCompleted, true);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "updateStatus", method = RequestMethod.PUT, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public void updateStatus(@RequestParam(value = "employeeTaskId") int employeeTaskId,
                             @RequestParam(value = "statusId") int statusId) throws BaseApiException {
        try {
            TaskStatus status = new TaskStatus(employeeTaskId, statusId);
            status.setEmployeeTaskId(employeeTaskId);
            timesheetService.updateStatus(status);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-dd-MM", Locale.ROOT);
        simpleDateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, false));
    }
}
