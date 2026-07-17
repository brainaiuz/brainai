package com.edatasite.workforce.rest.v3.release10.pm;

import com.edatasite.workforce.gwt.timesheet.client.rpc.SuggestionResponseDTO;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.SubmitForApprovalDto;
import com.edatasite.workforce.rest.v3.release10.pm.dto.TimesheetDTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.TimesheetFilterDTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.TimesheetSettingsDto;
import com.edatasite.workforce.rest.v3.release10.pm.service.ApiTimesheetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.INVALID;

/**
 * User: Akhror
 * Date: 26.07.2021
 */
@Tag(name = "Timesheet", description = "Timesheet Public API")
@RestController
@RequestMapping(value = "/timesheet", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiTimesheetControllerV3 {

    @Autowired
    private ApiTimesheetService apiTimesheetService;

    @Autowired
    private TimesheetService timesheetService;

    @Operation(summary = "Insert timesheet")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Timesheet"))
    @RequestMapping(path = "/insert", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<Integer> insertTimesheet(@Validated @RequestBody TimesheetDTO timesheetDTO) throws RestException {
        return ResultTO.success(apiTimesheetService.save(timesheetDTO));
    }

    @Operation(summary = "Get timesheet")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Timesheet"))
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<List<TimesheetDTO>> getTimesheet(@Validated @RequestBody TimesheetFilterDTO filterDTO) throws RestException {
        if ((filterDTO.getStartDate() != null && filterDTO.getEndDate() == null) || (filterDTO.getStartDate() == null && filterDTO.getEndDate() != null)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Either start date or end date is missing", INVALID, HttpStatus.BAD_REQUEST);
        }
        if (filterDTO.getStartDate() != null && filterDTO.getStartDate().after(filterDTO.getEndDate())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start Date cannot be after End Date", INVALID, HttpStatus.BAD_REQUEST);
        }
        return ResultTO.success(apiTimesheetService.getTimesheet(filterDTO));
    }

    @Operation(summary = "Get Project timesheets")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Get Project timesheets"))
    @RequestMapping(path = "/projectTimesheets", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<List<TimesheetDTO>> getProjectTimesheets(@Validated @RequestBody TimesheetFilterDTO filterDTO) throws RestException {
        if ((filterDTO.getStartDate() != null && filterDTO.getEndDate() == null) || (filterDTO.getStartDate() == null && filterDTO.getEndDate() != null)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Either start date or end date is missing", INVALID, HttpStatus.BAD_REQUEST);
        }
        if (filterDTO.getStartDate() != null && filterDTO.getStartDate().after(filterDTO.getEndDate())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start Date cannot be after End Date", INVALID, HttpStatus.BAD_REQUEST);
        }
        return ResultTO.success(apiTimesheetService.getProjectTimesheets(filterDTO));
    }

    @Operation(summary = "Submit for Approval")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Submit for Approval"))
    @RequestMapping(path = "/submitForApproval", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<String> approveOrReject(@Validated @RequestBody SubmitForApprovalDto filterDTO) throws RestException {
        String massage;
        if (filterDTO.getProject() == null || filterDTO.getProject().getId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Project is missing", INVALID, HttpStatus.BAD_REQUEST);
        } else if (filterDTO.getEmployee() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Employee is missing", INVALID, HttpStatus.BAD_REQUEST);
        }
        return ResultTO.success(apiTimesheetService.submitForApproval(filterDTO));
    }

    @Operation(summary = "Timesheet settings")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Timesheet settings"))
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public ResultTO<TimesheetSettingsDto> timesheetSettings() throws RestException {
        return ResultTO.success(apiTimesheetService.getTimesheetSettings());
    }

    @Operation(summary = "Timesheet Comment Suggest")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Timesheet Comment Suggest"))
    @RequestMapping(value = "/suggestion", method = RequestMethod.GET)
    public ResultTO<ArrayList<SuggestionResponseDTO>> getSuggestion(
            @RequestParam Integer userId,
            @RequestParam Integer taskId) {
        return ResultTO.success(timesheetService.getTimesheetCommentSuggestion(userId, taskId));
    }
}
