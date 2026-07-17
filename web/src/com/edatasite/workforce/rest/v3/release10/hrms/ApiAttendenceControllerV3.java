package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StatusServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.AttendenceDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.AttendenceListDTO;
import com.edatasite.workforce.rest.v3.release10.enums.AttendanceGroupBy;
import com.edatasite.workforce.rest.v3.release10.enums.AttendenceStatusEnum;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.AttendanceDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.AttendanceReportRequestDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.EmployeeAttendanceDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.enums.UnitType.DAILY_WORK;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
@Tag(name = "Attendences", description = "Attendences Public API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiAttendenceControllerV3 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiAttendenceControllerV3.class);

    @Autowired
    private StatusServiceLocal statusServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private AvailabilityService availabilityService;


    @Operation(summary = "Log Attendences Batch Data", description = "Log Attendences Batch Data. Users in/out data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code. Accabtable statuses are CHECK_IN, ON_BREAK, ON_DUTY, OFF_DUTY"),
            @ApiResponse(responseCode = "404", description = "Attendence data is not found"),
            @ApiResponse(responseCode = "400", description = "Status, user or date is required"),
            @ApiResponse(responseCode = "422", description = "Invalid date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/attendences_batch", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody AttendenceListDTO attendenceListDTO) throws RestException {

        if (attendenceListDTO == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Attendence data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (attendenceListDTO.getAttendences() == null || attendenceListDTO.getAttendences().isEmpty()) {
            throw new RestException("Attendences data are required", "Attendences data are required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        HashMap<Long, EdsEmployee> employeesMap = new HashMap<>();

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        for (AttendenceDTO attendenceDTO : attendenceListDTO.getAttendences()) {

            if (AttendenceStatusEnum.getStatus(attendenceDTO.getStatus()) == null) {
                throw new RestException("Invalid Attendence status", "Invalid Attendence status " + attendenceDTO.getStatus(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (attendenceDTO.getDriver_id() == null) {
                throw new RestException("Driver ID is required", "Driver ID is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (StringUtils.isBlank(attendenceDTO.getDate())) {
                throw new RestException("Attendence date is not provided", "Attendence date is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

            DateNonConvertable date;
            try {
                date = new DateNonConvertable(longDateTimezoneFormat.parse(attendenceDTO.getDate()));
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException("Invalid Attendence date format", "Invalid Attendence date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            EdsEmployee employee;
            if (employeesMap.get(attendenceDTO.getDriver_id()) != null) {
                employee = employeesMap.get(attendenceDTO.getDriver_id());
            } else {
                employee = employeeManager.getEmployeeByDriverNumber(attendenceDTO.getDriver_id());
                if (employee == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "User with driver ID ".concat(attendenceDTO.getDriver_id().toString()).concat(" does not exist in the system"), NOT_FOUND, HttpStatus.NOT_FOUND);
                }
                employeesMap.put(attendenceDTO.getDriver_id(), employee);
            }

            try {
                statusServiceLocal.insertEmployeeTimeTrack(employee.getObjectID(), AttendenceStatusEnum.getKPIStatus(attendenceDTO.getStatus()), date, attendenceDTO.getLocation());
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return successResponse(new ResponseData());
    }

    /*@Operation(summary = "Log Attendence Data", description = "Log Attendence Data. User in/out data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code. Accabtable statuses are CHECK_IN, ON_BREAK, ON_DUTY, OFF_DUTY"),
            @ApiResponse(responseCode = "404", description = "Attendence data is not found"),
            @ApiResponse(responseCode = "400", description = "Status, user or date is required"),
            @ApiResponse(responseCode = "422", description = "Invalid date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/attendences", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody AttendenceDTO attendenceTO) throws RestException {

        if (attendenceTO == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Attendence data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (attendenceTO.getStatus() == null || StringUtils.isBlank(attendenceTO.getStatus())) {
            throw new RestException("Attendence status is required", "Attendence status is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (AttendenceStatusEnum.getStatus(attendenceTO.getStatus()) == null) {
            throw new RestException("Invalid Attendence status", "Invalid Attendence status " + attendenceTO.getStatus(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (attendenceTO.getDriver_id() == null) {
            throw new RestException("Driver ID is required", "Driver ID is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(attendenceTO.getDate())) {
            throw new RestException("Attendence date is not provided", "Attendence date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        DateNonConvertable date;
        try {
            date = new DateNonConvertable(longDateTimezoneFormat.parse(attendenceTO.getDate()));
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid Attendence date format", "Invalid Attendence date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        EdsEmployee employee = employeeManager.getEmployeeByDriverNumber(attendenceTO.getDriver_id());
        if (employee == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "User with driver ID ".concat(attendenceTO.getDriver_id().toString()).concat(" does not exist in the system"), NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        EdsReference attendenceStatus;
        switch (AttendenceStatusEnum.valueOf(attendenceTO.getStatus())) {
            case CHECK_IN:
                attendenceStatus = referenceManager.findReference(Constants.TIME_TRACK_STATUS, Constants.AVAILABLE);
                break;
            case ON_BREAK:
                attendenceStatus = referenceManager.findReference(Constants.TIME_TRACK_STATUS, Constants.BREAK);
                break;
            case ON_DUTY:
                attendenceStatus = referenceManager.findReference(Constants.TIME_TRACK_STATUS, Constants.ON_DUTY);
                break;
            case OFF_DUTY:
                attendenceStatus = referenceManager.findReference(Constants.TIME_TRACK_STATUS, Constants.NOT_AVAILABLE);
                break;
            default:
                throw new RestException("Invalid status", "Invalid status. Accabtable  statuses are CHECK_IN, ON_BREAK, ON_DUTY, CHECK_OUT", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            statusServiceLocal.insertTimeTrack(employee.getObjectID(), attendenceStatus, date, attendenceTO.getLocation());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return successResponse(new ResponseData());
    }*/

    @Operation(summary = "Get Attendance Report Data")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Attendance Report Data"))
    @RequestMapping(value = "/attendance-report", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<EmployeeAttendanceDto> getAttendanceReport(@RequestBody AttendanceReportRequestDto request) throws ParseException {
        ListingFilterParameter fp = new ListingFilterParameter();
        SimpleDateFormat format;
        if (request.getPeriod().length()>7) {
             format = new SimpleDateFormat("yyyy-MM-dd");
        }else {
          format =  new SimpleDateFormat("yyyy-MM");
        }
        Date period = format.parse(request.getPeriod());

        Date start = ServerUtils.getMonthStartDate(period);
        Date end = ServerUtils.getMonthEndDate(period);
        Integer daysCount = DateUtil.countDays(start);

        format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        fp.setParams(String.valueOf(daysCount));
        fp.setStartDate(start);
        fp.setEndDate(end);
        fp.setStartDateNC(format.format(start));
        fp.setEndDateNC(format.format(end));
        SimpleDateFormat monthNameFormatter = new SimpleDateFormat("MMMM");
        fp.setMonthName(monthNameFormatter.format(period));

        if (request.getReasons() != null && !request.getReasons().isEmpty()) {
            ArrayList<EdsLeaveReason> reasons = new ArrayList<>();
            for (ItemDto reason : request.getReasons()) {
                EdsLeaveReason edsLeaveReason = null;
                if (reason.getId() != null) {
                    edsLeaveReason = leaveReasonManager.get(reason.getId());
                }
                if (reason.getCode() != null && edsLeaveReason == null) {
                    edsLeaveReason = leaveReasonManager.getReasonByName(null, reason.getCode());
                }
                if (reason.getName() != null && edsLeaveReason == null) {
                    edsLeaveReason = leaveReasonManager.getReasonByName(reason.getName(), null);
                }
                if (edsLeaveReason != null) {
                    reasons.add(edsLeaveReason);
                }
            }
            fp.setReasonIds(reasons.stream().map(r -> r.getObjectID().toString()).collect(Collectors.joining(",")));
        }

        if (request.getDepartments() != null && !request.getDepartments().isEmpty()) {
            ArrayList<EdsDepartment> departments = new ArrayList<>();
            for (ItemDto dept : request.getDepartments()) {
                EdsDepartment department = null;
                if (dept.getId() != null) {
                    department = departmentManager.get(dept.getId());
                }
                if (dept.getCode() != null && department == null) {
                    List<EdsDepartment> depts = departmentManager.getDepartmentByCode(dept.getCode());
                    if (depts != null && !depts.isEmpty()) {
                        department = depts.get(0);
                    }
                }
                if (dept.getName() != null && department == null) {
                    List<EdsDepartment> depts = departmentManager.getDepartmentByName(dept.getName());
                    if (depts != null && !depts.isEmpty()) {
                        department = depts.get(0);
                    }
                }
                if (department != null) {
                    departments.add(department);
                }
            }
            fp.setDepartmentIds(departments.stream().map(r -> r.getObjectID().toString()).collect(Collectors.joining(",")));
        }

        fp.setName(request.getSearchText());
        if (request.getGroupBy() != null) {
            fp.setOrderByDepartment(request.getGroupBy().equals(AttendanceGroupBy.DEPARTMENT));
            fp.setOrderByPosition(request.getGroupBy().equals(AttendanceGroupBy.POSITION));
        }

        if (request.getLocation() != null) {
            EdsLocation location = null;
            if (request.getLocation().getId() != null) {
                location = locationManager.get(request.getLocation().getId());
            }
            if (request.getLocation().getCode() != null && location == null) {
                location = locationManager.getLocationByCode(request.getLocation().getCode(), null);
            }
            if (request.getLocation().getName() != null && location == null) {
                location = locationManager.getLocationByName(request.getLocation().getName());
            }
            fp.setLocationId(location != null ? location.getObjectID() : null);
        }

        if (request.getTimeslot() != null) {
            EdsTimeSlot timeSlot = null;
            if (request.getTimeslot().getId() != null) {
                timeSlot = timeSlotManager.get(request.getTimeslot().getId());
            }
            if (request.getLocation().getCode() != null && timeSlot == null) {
                timeSlot = timeSlotManager.getTimeSlotByShortName(request.getTimeslot().getCode());
            }
            if (request.getLocation().getName() != null && timeSlot == null) {
                timeSlot = timeSlotManager.getTimeSlotByShortName(request.getTimeslot().getName());
            }
            fp.setTimeSlotID(timeSlot != null ? timeSlot.getObjectID() : null);
        }

        if (request.getPosition() != null) {
            EdsPosition position = null;
            if (request.getPosition().getId() != null) {
                position = positionManager.get(request.getPosition().getId());
            }
            if (request.getPosition().getCode() != null && position == null) {
                position = positionManager.getPositionByCode(request.getPosition().getCode(), null);
            }
            if (request.getPosition().getName() != null && position == null) {
                position = positionManager.getByName(request.getPosition().getName());
            }
            fp.setPositionID(position != null ? position.getObjectID() : null);
        }
        fp.setStart(request.getStart());

        EmployeeAttendanceReport report = availabilityService.getEmployeeAttendanceReport(fp, daysCount);
        List<EmployeeAttendanceDto> result = new ArrayList<>();
        if (report != null && report.getEmployeeReports() != null) {
            HashMap<String, ReasonItem> reasons = report.getLeaveTypes();
            for (EmployeeReport item : report.getEmployeeReports()) {
                EmployeeAttendanceDto dto = new EmployeeAttendanceDto();
                dto.setId(item.getId());
                dto.setName(item.getName());
                dto.setCode(item.getCode());

                dto.setWorkedHours(item.getInhour() / 60);
                dto.setWorkedDays(item.getWorkedDays());
                dto.setPlannedHours(item.getPlannedHours() / 60);
                dto.setPlannedDays(item.getPlannedDays());

                int[] al = item.getAl();
                ArrayList<AttendanceDto> attendances = new ArrayList<>();
                for (int i = 1; i < daysCount + 1; i++) {
                    long tempHour = ((item.getInOutHour()[i] == null ? 0 : item.getInOutHour()[i]) - item.getOvertimeHour()[i]) / 60;
                    long overtime = item.getOvertimeHour()[i] / 60;
                    int[] withHoliday = item.getWithHoliday();
                    String timeslot = item.getTimeSlotId()[i] != null ? item.getTimeSlotId()[i] : "";

                    AttendanceDto attendanceDto = new AttendanceDto();
                    attendanceDto.setDay(i);
                    if (al[i] == 1 || al[i] == 4 || al[i] == -1 || al[i] == 2 || al[i] == -2 || al[i] == 3) {
                        ReasonItem reasonItem = reasons.get(item.getLeaveCodes()[i]);
                        attendanceDto.setLeave(new ItemDto(item.getIdsOfLeaveRequests().get(i), reasonItem.getName(), reasonItem.getShortName()));
                        if (al[i] == 3 || (reasonItem.getUnitType() != null && reasonItem.getUnitType().equals(DAILY_WORK))) {
                            attendanceDto.setHours(tempHour);
                            attendanceDto.setOvertime(overtime);
                            attendanceDto.setTimeslot(timeslot);
                        }
                    } else if (item.isHasShift()) {
                        if (withHoliday[i] == 2) {
                            attendanceDto.setHoliday(true);
                        } else if (tempHour == 0 && overtime == 0) {
                            if (withHoliday[i] == 1 || item.getLeaveRequestHolidays()[i] == 3) {
                                attendanceDto.setHoliday(true);
                            } else {
                                attendanceDto.setDayOff(true);
                            }
                        } else {
                            attendanceDto.setHours(tempHour);
                            attendanceDto.setOvertime(overtime);
                            attendanceDto.setTimeslot(timeslot);
                        }
                    } else if (item.getLeaveRequestHolidays()[i] == 1) {
                        if (tempHour == 0 && overtime == 0) {
                            attendanceDto.setDayOff(true);
                        } else {
                            attendanceDto.setHours(tempHour);
                            attendanceDto.setOvertime(overtime);
                            attendanceDto.setTimeslot(timeslot);
                        }
                    } else if (item.getLeaveRequestHolidays()[i] == 3) {
                        if (tempHour == 0 && overtime == 0) {
                            attendanceDto.setHoliday(true);
                        } else {
                            attendanceDto.setHours(tempHour);
                            attendanceDto.setOvertime(overtime);
                            attendanceDto.setTimeslot(timeslot);
                        }
                    } else if (withHoliday[i] == 1) {
                        if (tempHour == 0 && overtime == 0) {
                            attendanceDto.setHoliday(true);
                        } else {
                            attendanceDto.setHours(tempHour);
                            attendanceDto.setOvertime(overtime);
                            attendanceDto.setTimeslot(timeslot);
                        }
                    } else {
                        attendanceDto.setHours(tempHour);
                        attendanceDto.setOvertime(overtime);
                        attendanceDto.setTimeslot(timeslot);
                    }
                    attendances.add(attendanceDto);
                }
                if (request.getPeriod().length() > 7) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(period);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    dto.setAttendances(new ArrayList<>(attendances.stream().filter(a -> a.getDay() == day).collect(Collectors.toList())));
                }else {
                    dto.setAttendances(attendances);
                }
                result.add(dto);
            }
        }

        return result;
    }

}
