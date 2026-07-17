package com.edatasite.workforce.rest.v3.release10.trainingcenter;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.StudentDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.service.ApiStudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: Iqboliddin
 * Date: 19.03.2025
 */

@Tag(name = "Student", description = "Student API")
@RestController
@RequestMapping(value = "/student",
        headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiStudentV3 {
    private final ApiStudentService studentService;

    private static final Logger log = LoggerFactory.getLogger(ApiStudentV3.class);

    public ApiStudentV3(ApiStudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Create Student For Existing Crm Account")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Student created successfully"))
    @RequestMapping(value = "/create", method = RequestMethod.POST,  consumes = {MediaType.APPLICATION_JSON_VALUE}, produces ={MediaType.APPLICATION_JSON_VALUE})
    public Integer createStudent(@RequestBody StudentDto studentDto) {
        try {
            log.info("Creating student with CRM Account ID: {}, Contact ID: {}", studentDto.getCrmAccountId(), studentDto.getContactId());
            Integer studentId = studentService.create(studentDto);
            return studentId;
        } catch (Exception e) {
            log.error("Error while creating student: {}", e.getMessage(), e);
            return null;
        }
    }
}
