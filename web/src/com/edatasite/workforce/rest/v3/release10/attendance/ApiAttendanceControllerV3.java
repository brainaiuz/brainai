package com.edatasite.workforce.rest.v3.release10.attendance;

import com.edatasite.workforce.gwt.availability.client.rpc.InOutPairDto;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.server.app.StatusServiceLocal;
import com.edatasite.workforce.gwt.core.server.rpc.FingerPrintItem;
import com.edatasite.workforce.gwt.hrms.server.app.UserFingerPrintAdjustmentServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.core.BaseApiControllerV3;
import com.edatasite.workforce.rest.v3.release10.core.to.AttendanceDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Tag(name = "Attendance", description = "Employee Attendance API")
@RestController
@RequestMapping(value = "/attendance", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiAttendanceControllerV3 extends BaseApiControllerV3 {

    private final StatusServiceLocal statusServiceLocal;
    private final UserFingerPrintAdjustmentServiceLocal userFingerPrintAdjustmentServiceLocal;
    private final AvailabilityServiceLocal availabilityServiceLocal;

    public ApiAttendanceControllerV3(StatusServiceLocal statusServiceLocal,
                                     UserFingerPrintAdjustmentServiceLocal userFingerPrintAdjustmentServiceLocal,
                                     AvailabilityServiceLocal availabilityServiceLocal) {
        this.statusServiceLocal = statusServiceLocal;
        this.userFingerPrintAdjustmentServiceLocal = userFingerPrintAdjustmentServiceLocal;
        this.availabilityServiceLocal = availabilityServiceLocal;
    }


    @Operation(summary = "Import Employee Attendance")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Attendance"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<AttendanceDto> createCase(@RequestBody AttendanceDto caseDto) {
        FingerPrintItem printItem = new FingerPrintItem();
        printItem.setUsers(caseDto.getData());
        printItem.setProjectId(caseDto.getProjectId());
        printItem.setTaskId(caseDto.getTaskId());
        CompanyDomain companyDomain = new CompanyDomain();
        companyDomain.setFingerprintDateFormat(caseDto.getDateFormat());
        companyDomain.setDynamicStatus(caseDto.getWithStatus());
        companyDomain.setCompanyUniqueID(caseDto.getDeviceUid());
        companyDomain.setCompanyBranchName(caseDto.getDeviceName());
        statusServiceLocal.addFingerPrintItemsToTimeTrackAll(printItem, companyDomain);
        return ResultTO.success(caseDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<AttendanceDto> getAttendance(@RequestParam Integer attendanceId) {
        AttendanceDto byAttendanceId = userFingerPrintAdjustmentServiceLocal.getByAttendanceId(attendanceId);
        return ResultTO.success(byAttendanceId);
    }

    @GetMapping(path = "/report", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<List<InOutPairDto>>> getAttendanceInOutPair(@RequestParam Integer employeeId,
                                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                                     @RequestParam @Schema(description = "Type of report", allowableValues = {"ALL_PAIRS", "DAILY_SUMMARY"}) String calculationType,
                                                                     @RequestParam(required = false) Boolean includeSnapshots) {
        List<InOutPairDto> employeeInOutRecords;
        if ("ALL_PAIRS".equals(calculationType)) {
            employeeInOutRecords = availabilityServiceLocal.getEmployeeInOutRecords(employeeId, new DateNonConvertable(fromDate), new DateNonConvertable(toDate), includeSnapshots);
        } else if ("DAILY_SUMMARY".equals(calculationType)) {
            employeeInOutRecords = availabilityServiceLocal.getEmployeeInOutDailySummary(employeeId, new DateNonConvertable(fromDate), new DateNonConvertable(toDate), includeSnapshots);
        } else {
            employeeInOutRecords = new ArrayList<>();
        }
        List<List<InOutPairDto>> dailyLists = employeeInOutRecords.stream()
                .filter(p -> p.getInTime() != null || p.getOutTime() != null)
                .sorted(Comparator.comparing(
                        this::recordTime,
                        Comparator.reverseOrder()
                ))
                .collect(Collectors.groupingBy(
                        p -> atStartOfDay(recordTime(p)),
                        () -> new TreeMap<>(Collections.reverseOrder()),
                        Collectors.toList()
                ))
                .values()
                .stream()
                .toList();

        return ResultTO.success(dailyLists);
    }

    private Date recordTime(InOutPairDto p) {
        return p.getInTime() != null
                ? p.getInTime().getNonConvertedDate()
                : p.getOutTime().getNonConvertedDate();
    }

    private Date atStartOfDay(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}
