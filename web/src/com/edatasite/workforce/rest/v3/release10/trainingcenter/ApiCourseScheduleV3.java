package com.edatasite.workforce.rest.v3.release10.trainingcenter;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.trainingcenter.server.TCServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseScheduleDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.service.ApiCourseScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: Abdulkhamid
 * Date: 14.02.2025
 */

@Tag(name = "Course Schedule", description = "Course Schedule API")
@RestController
@RequestMapping(value = "/course_schedule",
        headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCourseScheduleV3 {
    @Autowired
    private TCServiceLocal tcService;
    @Autowired
    private ApiCourseScheduleService apiCourseScheduleService;



    private static final Logger log = LoggerFactory.getLogger(ApiCourseScheduleV3.class);


    @Operation(summary = "Get Course Schedules list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Course Schedules"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, produces ={MediaType.APPLICATION_JSON_UTF8_VALUE} )
    public ListResultTO<CourseScheduleDto> getCourseSchedules(@RequestBody ListParamsDTO params) {
        log.info("REST request to get course schedule list");
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.ScheduledCourseListPanel);

        return apiCourseScheduleService.getCourseScheduleFromSolr(fp);
    }

    @Operation(summary = "Get existing course schedule by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "course schedule"))
    @RequestMapping(path = "/{scheduleId}", method = RequestMethod.GET)
    public ResultTO<CourseScheduleDto> getScheduleById(@PathVariable final Integer scheduleId) throws RestException {
        log.info("REST request to get schedule by id: {}", scheduleId);
        return ResultTO.success(apiCourseScheduleService.getById(scheduleId));
    }


    @Operation(summary = "Create new course schedule")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Course Schedule"))
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CourseScheduleDto> createCourseSchedule(@Validated @RequestBody CourseScheduleDto scheduleDto) throws RestException {
        log.info("REST request to create course schedule");
        if (scheduleDto.getId() != null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Course Schedule ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiCourseScheduleService.save(scheduleDto);
        return ResultTO.success(scheduleDto);
    }

    @Operation(summary = "Get Course Schedules list by studentId")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Course Schedules by studentId"))
    @RequestMapping(value = "/student_id_by/{studentId}", method = RequestMethod.GET,  produces ={MediaType.APPLICATION_JSON_UTF8_VALUE} )
    public ListResultTO<CourseScheduleDto> getCourseSchedules(@RequestParam Integer studentId, @RequestParam String sortAs) throws RestException {
        log.info("REST request to get course schedule list by studentid");

        return apiCourseScheduleService.getCourseScheduleByStudentId(studentId, sortAs);
    }


}
