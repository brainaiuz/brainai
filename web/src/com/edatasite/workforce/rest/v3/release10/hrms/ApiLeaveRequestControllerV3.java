package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.enums.UnitType;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.LeaveRequestDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.MultiLeaveRequestDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.service.ApiLeaveRequestService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;

/**
 * User : Akhror
 * Date : 10.07.2021
 */
@Tag(name = "Leave Request", description = "Leave Request Public API")
@RestController
@RequestMapping(value = "/leave", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiLeaveRequestControllerV3 {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private final ApiLeaveRequestService apiLeaveRequestService;
    private final AvailabilityService availabilityService;
    private final SickRequestManager sickRequestManager;
    private final AllInOneService allInOneService;

    public ApiLeaveRequestControllerV3(ApiLeaveRequestService apiLeaveRequestService,
                                       AvailabilityService availabilityService,
                                       SickRequestManager sickRequestManager,
                                       AllInOneService allInOneService) {
        this.apiLeaveRequestService = apiLeaveRequestService;
        this.availabilityService = availabilityService;
        this.sickRequestManager = sickRequestManager;
        this.allInOneService = allInOneService;
    }

    @Operation(summary = "Get Leave Requests list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Leave Requests"))
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ListResultTO<LeaveRequestDTO>> getLeaveRequests(@RequestBody ListParamsDTO params,
                                                                    @RequestParam(value = "simple", required = false) Boolean simple) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.LeaveRequestApprove);

        ListResultTO<LeaveRequestDTO> leaveRequests;
        if (simple != null && simple) {
            leaveRequests = apiLeaveRequestService.getSimpleLeaveRequests(fp);
        } else {
            leaveRequests = apiLeaveRequestService.getLeaveRequestsList(fp);
        }
        return ResultTO.success(leaveRequests);
    }

    @Operation(summary = "Get existing Leave Request by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Leave Request"))
    @GetMapping(path = "/{leaveId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<LeaveRequestDTO> getLeaveRequestById(@PathVariable final Integer leaveId) throws RestException {
        return ResultTO.success(apiLeaveRequestService.getById(leaveId));
    }

    @Operation(summary = "Delete existing Leave Request by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Leave Request"))
    @DeleteMapping(path = "/{leaveId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> deleteLeaveRequest(@PathVariable Integer leaveId) throws RestException {
        var leaveRequestById = sickRequestManager.get(leaveId);
        if (leaveRequestById == null)
            throw new RestException(GENERAL_ERROR_MESSAGE, "Leave Request with this id is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);

        availabilityService.deleteRequest(leaveId);
        return ResultTO.success();
    }

    @Operation(summary = "Create Leave Request")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Leave Request"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Integer> createLeaveRequest(@Validated @RequestBody LeaveRequestDTO leaveRequestDTO) throws RestException {
        if (leaveRequestDTO.getId() != null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Leave Request ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        var newLeaveRequestId = apiLeaveRequestService.save(leaveRequestDTO);
        return ResultTO.success(newLeaveRequestId);
    }

    @Operation(summary = "Update Leave Request")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Leave Request Updated"))
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Integer> updateLeaveRequest(@Validated @RequestBody LeaveRequestDTO leaveRequestDTO) throws RestException {
        if (leaveRequestDTO.getId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Leave Request ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        var updatedLeaveRequestId = apiLeaveRequestService.save(leaveRequestDTO);
        return ResultTO.success(updatedLeaveRequestId);
    }

    @Operation(summary = "Create Multi Leave Request")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Multi Leave Request"))
    @PostMapping(path = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<Integer>> createMultiLeaveRequest(@Validated @RequestBody MultiLeaveRequestDTO multiLeaveRequestDTO) throws RestException {
        var newLeaveRequestIds = apiLeaveRequestService.createBulkLrs(multiLeaveRequestDTO);
        return ResultTO.success(newLeaveRequestIds);
    }

    @Operation(summary = "Get Leave Reasons list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Leave Reasons"))
    @GetMapping(value = "/reason/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<SelectItem[]> getLeaveRequests(@RequestParam(required = false) String searchText,
                                                   @RequestParam(required = false) Integer limit) {
        var reasons = availabilityService.getReasonsWithLimit(null, true, searchText, limit);
        return ResultTO.success(reasons);
    }

    @Operation(summary = "Calculate leave request days")
    @GetMapping(path = "/calculate-days", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<String> calculateDays(@RequestParam(value = "reasonId", required = false) Integer reasonId,
                                          @RequestParam("startDate") String startDate,
                                          @RequestParam("endDate") String endDate) throws RestException {
        DateNonConvertable startDateNonConvertable;
        DateNonConvertable endDateNonConvertable;
        try {
            startDateNonConvertable = new DateNonConvertable(dateFormat.parse(startDate));
            endDateNonConvertable = new DateNonConvertable(dateFormat.parse(endDate));
        } catch (ParseException e) {
            throw new RestException("Date is not parseable", "Date is not parseable", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        var fp = new ListingFilterParameter();
        if (reasonId != null) {
            ReasonItem reason = allInOneService.getReason(reasonId);
            fp.setIncludeDayOff(reason.isIncludeDayOffs());
            fp.setReasonCode(reason.getCode());
            fp.setAllDay(!UnitType.HOURLY.equals(reason.getUnitType()));
        }
        fp.setEmployeeId(((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID());
        var leaveDaysCount = availabilityService.getLeaveDaysCount(fp, startDateNonConvertable, endDateNonConvertable);
        return ResultTO.success(leaveDaysCount);
    }

    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Collection<LeaveRequestLisItem>> getLeaveRequestStatistics(@RequestParam("startDate") String startDate,
                                                                               @RequestParam("endDate") String endDate) {
        var leaveStats = apiLeaveRequestService.getLeaveStats(startDate, endDate);
        return ResultTO.success(leaveStats);
    }

    @PostMapping(path = "/approve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> approveMultipleLeaveRequests(@RequestBody ArrayList<Integer> ids) {
        availabilityService.updateMultipleRequests(Constants.LR_STATUS_SS_APPROVED, ids, null);
        return ResultTO.success();
    }

    @PostMapping(path = "/reject", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> rejectMultipleLeaveRequests(@RequestBody ArrayList<Integer> ids) {
        availabilityService.updateMultipleRequests(Constants.LR_STATUS_SS_DENIED, ids, null);
        return ResultTO.success();
    }
}
