package com.edatasite.workforce.rest.v3.release10.trainingcenter;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseBookingDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseSubjectsDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.service.ApiCourseBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: Iqboliddin
 * Date: 27.02.2025
 */

@Tag(name = "Course Booking", description = "Course Booking API")
@RestController
@RequestMapping(value = "/course_booking",
        headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_VALUE})
public class ApiCourseBookingV3 {
    @Autowired
    private ApiCourseBookingService apiCourseBookingService;

    private static final Logger log = LoggerFactory.getLogger(ApiCourseBookingV3.class);

    @Operation(summary = "Create Course Booking")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Course Booking"))
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Integer createCourseBooking(@RequestBody CourseBookingDto courseBooking) {
        log.info("REST request to create course booking");
        return apiCourseBookingService.create(courseBooking);
    }

    @Operation(summary = "Cancel Student Course Booking")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Course Booking"))
    @RequestMapping(value = "/cancel", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public boolean cancelStudentCourseBooking(@RequestParam Integer scheduleCourseId, @RequestParam Integer crmAccountId) {
        log.info("REST request to cancel student course booking for crmAccountId: {}, scheduleCourseId: {}", crmAccountId, scheduleCourseId);
        return apiCourseBookingService.cancelStudentCourseBooking(crmAccountId, scheduleCourseId);
    }

    @Deprecated
    @Operation(summary = "Get Course Subjects list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Course Schedules"))
    @RequestMapping(value = "/course-subjects", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ListResultTO<CourseSubjectsDto> getCourseSubjects() {
        log.info("REST request to get course subjects list");
        return apiCourseBookingService.getCourseSubjects();
    }
}
