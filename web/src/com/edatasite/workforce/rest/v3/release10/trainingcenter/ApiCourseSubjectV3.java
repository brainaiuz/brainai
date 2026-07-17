package com.edatasite.workforce.rest.v3.release10.trainingcenter;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseSubjectsDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.service.ApiCourseBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: Iqboliddin
 * Date: 04.06.2025
 */

@Tag(name = "Course Subjects", description = "Course Subjects API")
@RestController
@RequestMapping(value = "/course_subjects",
        headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_VALUE})
public class ApiCourseSubjectV3 {
    @Autowired
    private ApiCourseBookingService apiCourseBookingService;

    private static final Logger log = LoggerFactory.getLogger(ApiCourseSubjectV3.class);

    @Operation(summary = "Get Course Subjects list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Course Schedules"))
    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ListResultTO<CourseSubjectsDto> getCourseSubjects() {
        log.info("REST request to get course subjects list");
        return apiCourseBookingService.getCourseSubjects();
    }

    @Operation(summary = "Create new Course Subject")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Integer> createCourseSubject(@RequestBody CourseSubjectsDto request) {
        log.info("REST request to Create new Course Subject");
        Integer id = apiCourseBookingService.createCourseSubject(request);
        return ResultTO.success(id);
    }

    @Operation(summary = "Update Course Subject")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Integer> updateCourse(@RequestBody CourseSubjectsDto request) {
        log.info("REST request to update Course Subject");
        if (request.getId() == null) return ResultTO.failure("Subject id is required", HttpStatus.BAD_REQUEST.value());
        Integer id = apiCourseBookingService.createCourseSubject(request);
        return ResultTO.success(id);
    }

    @Operation(summary = "Delete existing Course Subject  by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "deleted"))
    @DeleteMapping(path = "{subjectId}")
    public ResultTO<Integer> deleteCourseById(@PathVariable final Integer subjectId) throws RestException {
        log.info("REST request to delete  by id: {}", subjectId);
        Integer id = apiCourseBookingService.deleteCourseSubject(subjectId);
        return ResultTO.success(id);
    }
}
