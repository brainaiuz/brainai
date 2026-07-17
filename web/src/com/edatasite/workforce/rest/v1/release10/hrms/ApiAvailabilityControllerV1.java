package com.edatasite.workforce.rest.v1.release10.hrms;

import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.InOutItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StatusServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.rest.base.enums.ApiActionEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.InOutItemTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dilsh0d Madrahimov on 10.03.2017.
 */
@Tag(name = "Availability", description = "Availability API")
@RestController
@RequestMapping(value = "/availability", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiAvailabilityControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private StatusServiceLocal statusServiceLocal;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private UserManager userManager;


    @RequestMapping(value = "/report/{employeeId}/{date}", method = RequestMethod.GET)
    public Object getInOutReport(@PathVariable(value = "employeeId") Integer employeeId, @PathVariable(value = "date") Long date) {
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.setTime(WrapUtils.longToDate(date));
        ServerUtils.setBeginningOfTheDay(startDateTime);

        Calendar endDateTime = Calendar.getInstance();
        endDateTime.setTime(WrapUtils.longToDate(date));
        ServerUtils.setEndOfTheDay(endDateTime);

        InOutItem[] result = allInOneServiceLocal.getInOutReport(null, null, null, employeeId, null, null, startDateTime.getTime(), endDateTime.getTime(),
                true, true, true, true, false, false, false, false, false, false);


        InOutItemTO inOutItem = new InOutItemTO();
        int actualRealTimeMinutes = 0;
        Date currentDate = ServerUtils.convertServerDateToUserDate(new Date(), userManager.getUser().getUserTimezone());

        if (result != null && result.length > 0) {
            inOutItem.setInHours(result[0].getInHours());
            inOutItem.setOutHours(result[0].getOutHours());
            int lastInIndex = result[0].getInHours().length;
            String lastInHour = result[0].getInHours()[lastInIndex - 1];
            String lastOutHour = result[0].getOutHours()[lastInIndex - 1];
            actualRealTimeMinutes = result[0].getActualInHour() != null ? Integer.valueOf(result[0].getActualInHour()) : 0;
            if (lastInHour != null && lastOutHour == null) {
                Integer lastInMinutes = ServerUtils.timeToMinutes(lastInHour);
                actualRealTimeMinutes = (currentDate.getHours() * 60 + currentDate.getMinutes()) - lastInMinutes + Integer.valueOf(result[0].getActualInHour() != null ? result[0].getActualInHour() : "0");
            }
        }
        int timeslotStartHour = 0;
        int timeslotEndHour = 0;

        TimeslotItem timeslotItem = availabilityServiceLocal.getEmpTimeslot(employeeId);
        switch (startDateTime.get(Calendar.DAY_OF_WEEK)) {
            case 0 -> {
                timeslotStartHour = timeslotItem.getSunday()[0];
                timeslotEndHour = timeslotItem.getSunday()[1];
            }
            case 1 -> {
                timeslotStartHour = timeslotItem.getMonday()[0];
                timeslotEndHour = timeslotItem.getMonday()[1];
            }
            case 2 -> {
                timeslotStartHour = timeslotItem.getTuesday()[0];
                timeslotEndHour = timeslotItem.getTuesday()[1];
            }
            case 3 -> {
                timeslotStartHour = timeslotItem.getWednesday()[0];
                timeslotEndHour = timeslotItem.getWednesday()[1];
            }
            case 4 -> {
                timeslotStartHour = timeslotItem.getThursday()[0];
                timeslotEndHour = timeslotItem.getThursday()[1];
            }
            case 5 -> {
                timeslotStartHour = timeslotItem.getFriday()[0];
                timeslotEndHour = timeslotItem.getFriday()[1];
            }
            case 6 -> {
                timeslotStartHour = timeslotItem.getSaturday()[0];
                timeslotEndHour = timeslotItem.getSaturday()[1];
            }
        }

        inOutItem.setTimeslotStartHour(timeslotStartHour);
        inOutItem.setTimeslotEndHour(timeslotEndHour);
        int timeSlotHourDiff = timeslotEndHour - timeslotStartHour;
        if (actualRealTimeMinutes - timeSlotHourDiff > 0) {
            inOutItem.setOvertimeHour(actualRealTimeMinutes - timeSlotHourDiff);
        }
        inOutItem.setActualInHour(actualRealTimeMinutes);

        return successResponse(inOutItem);
    }

    @RequestMapping(value = "/checkInOut/{employeeId}/{actionType}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object checkInOut(@PathVariable(value = "employeeId") Integer employeeId, @PathVariable(value = "actionType") String actionType) {
        if (employeeId == null || actionType == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        String status = ApiActionEnum.IN.getCode().equals(actionType.toUpperCase()) ? Constants.AVAILABLE : Constants.NOT_AVAILABLE;
        try {
            statusServiceLocal.setUserStatus(employeeId, status, false);
            return this.successResponse(SUCCESS_UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAILED_UPDATE);
        }
    }

}
