package com.edatasite.workforce.rest.v3.release10.trainingcenter;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseCreateDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.service.ApiCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * User: Abdulkhamid
 * Date: 28.02.2025
 */

@Tag(name = "Courses", description = "Course API")
@RestController
@RequestMapping(value = "/course",
        headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCourseV3 {
    @Autowired
    private ApiCourseService apiCourseService;

    private static final Logger log = LoggerFactory.getLogger(ApiCourseV3.class);

    @Operation(summary = "Get Course list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Course Schedules"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces ={MediaType.APPLICATION_JSON_VALUE} )
    public ListResultTO<CourseDto> getCourses(@RequestBody ListParamsDTO params) {
        log.info("REST request to get course  list");
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.CourseListPanel);
        return apiCourseService.getCourses(fp);
    }

    @Operation(summary = "Get existing course  by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "course"))
    @RequestMapping(path = "/{courseId}", method = RequestMethod.GET)
    public ResultTO<CourseDto> getCourseById(@PathVariable final Integer courseId) throws RestException {
        log.info("REST request to get  by id: {}", courseId);
        return ResultTO.success(apiCourseService.getById(courseId));
    }

    @Operation(summary = "Create new course")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Boolean> createCourse(@RequestBody CourseCreateDto request) {
        log.info("REST request to create course");
        apiCourseService.createCourse(request);
        return ResultTO.success(true);
    }

    @Operation(summary = "Update course")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Boolean> updateCourse(@RequestBody CourseCreateDto request) {
        log.info("REST request to update course");
        apiCourseService.createCourse(request);
        return ResultTO.success(true);
    }

    @Operation(summary = "Delete existing course  by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "deleted"))
    @DeleteMapping(path = "/{courseId}")
    public ResultTO<Boolean> deleteCourseById(@PathVariable final Integer courseId) throws RestException {
        log.info("REST request to delete  by id: {}", courseId);
        return ResultTO.success(apiCourseService.deleteById(courseId));
    }

}
