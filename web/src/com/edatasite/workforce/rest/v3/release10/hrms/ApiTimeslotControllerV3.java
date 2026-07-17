package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AttendanceStatusDto;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.PermissionManager;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.HolidayTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.BaseApiControllerV3;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.TimeslotDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.timeslot.TimeslotDeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 10.02.2020 18:58
 */
@Tag(name = "Timeslot", description = "Timeslot Public API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiTimeslotControllerV3 extends BaseApiControllerV3 {

    private static final Logger log = LoggerFactory.getLogger(ApiTimeslotControllerV3.class);

    private final EmployeeManager employeeManager;
    private final CrmServiceLocal crmServiceLocal;
    private final AvailabilityServiceLocal availabilityServiceLocal;
    private final AttendanceRawDataManager attendanceRawDataManager;
    private final PermissionManager permissionManager;

    public ApiTimeslotControllerV3(EmployeeManager employeeManager,
                                   CrmServiceLocal crmServiceLocal,
                                   AvailabilityServiceLocal availabilityServiceLocal,
                                   AttendanceRawDataManager attendanceRawDataManager,
                                   PermissionManager permissionManager) {
        this.employeeManager = employeeManager;
        this.crmServiceLocal = crmServiceLocal;
        this.availabilityServiceLocal = availabilityServiceLocal;
        this.attendanceRawDataManager = attendanceRawDataManager;
        this.permissionManager = permissionManager;
    }

    @PostMapping(value = "/driver_timeslot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult create(@RequestBody TimeslotDTO dto) throws RestException {
        if (dto == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (dto.getDriver_id() == null || (!dto.hasTimeslot() && !dto.hasEvent())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Data is not found", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsEmployee edsEmployee = employeeManager.getEmployeeByDriverNumber(dto.getDriver_id());
        if (edsEmployee == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "User with driver ID ".concat(dto.getDriver_id().toString()).concat(" does not exist in the system"), NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        TimeslotItem item = null;
        if (dto.hasTimeslot()) {
            item = new TimeslotItem();
            item.setName(dto.getName());
            item.setEffectiveDate(new Date());
            item.setSunday(dto.getSunday());
            item.setLunchSu(new int[]{0, 0});
            item.setCoffeeSu(new int[]{0, 0});
            item.setMonday(dto.getMonday());
            item.setLunchMo(new int[]{0, 0});
            item.setCoffeeMo(new int[]{0, 0});
            item.setTuesday(dto.getTuesday());
            item.setLunchTu(new int[]{0, 0});
            item.setCoffeeTu(new int[]{0, 0});
            item.setWednesday(dto.getWednesday());
            item.setLunchWe(new int[]{0, 0});
            item.setCoffeeWe(new int[]{0, 0});
            item.setThursday(dto.getThursday());
            item.setLunchTh(new int[]{0, 0});
            item.setCoffeeTh(new int[]{0, 0});
            item.setFriday(dto.getFriday());
            item.setLunchFr(new int[]{0, 0});
            item.setCoffeeFr(new int[]{0, 0});
            item.setSaturday(dto.getSaturday());
            item.setLunchSa(new int[]{0, 0});
            item.setCoffeeSa(new int[]{0, 0});
            item.setSelectedEmployees(new Integer[]{edsEmployee.getObjectID()});
        }
        try {
            if (dto.hasTimeslot()) {
                availabilityServiceLocal.createTimeslot(item);
            }
            if (dto.hasEvent()) {
                crmServiceLocal.addEmployeeToEvent(dto.getKpi_event_id(), edsEmployee.getObjectID());
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }

    @PostMapping(value = "/holidays", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ArrayList<HolidayTO>> getHolidays(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.LeaveRequestApprove);
        int year = params.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        fp.setYear(year);
        ListResult<HolidayItem> holidayResult = availabilityServiceLocal.getHolidays(fp);
        ArrayList<HolidayTO> result = new ArrayList<>();
        if (holidayResult.getList() == null) {
            return ResultTO.success(result);
        }
        for (HolidayItem item : holidayResult.getList()) {
            result.add(new HolidayTO(item));
        }
        return ResultTO.success(result);
    }

    @Operation(summary = "Get employee timeslot")
    @GetMapping(path = "/employees/timeslot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<TimeslotItem> getTimeslot(@RequestParam(required = false) Integer userId) throws RestException {
        EdsUser currentUser = permissionManager.getUser();
        if (userId != null && !currentUser.hasRole(Constants.ADMIN_CODE)) {
            throw new RestException("User not found", "User not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        TimeslotItem empTimeslot = availabilityServiceLocal.getEmpTimeslot(userId != null ? userId : currentUser.getObjectID());
        return ResultTO.success(empTimeslot);
    }

    @Operation(summary = "Get employee shift timeslot")
    @GetMapping(path = "/employees/shift/timeslot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<TimeslotItem>> getShiftTimeslot() {
        List<TimeslotItem> empShiftTimeslotList = availabilityServiceLocal.getEmpShiftTimeslotList();
        return ResultTO.success(empShiftTimeslotList);
    }



    @GetMapping(path = "/employees/daily-leave-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<AttendanceStatusDto>> getEmployeeDailyLeaveStatus(@RequestParam Integer userId,
                                                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        var employeeDailyLeaveStatus = attendanceRawDataManager.getEmployeeDailyLeaveStatus(userId, fromDate, toDate);
        return ResultTO.success(employeeDailyLeaveStatus);
    }

    @GetMapping(path = "/timeslot/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<TimeslotDeDTO> getTimeslotById(@PathVariable Integer id) {
        TimeslotItem timeslot = availabilityServiceLocal.getTimeslot(id);
        return ResultTO.success(new TimeslotDeDTO(timeslot));
    }
}
