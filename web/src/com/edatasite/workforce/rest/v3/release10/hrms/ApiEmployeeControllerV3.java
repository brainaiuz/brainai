package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.employee.EmployeeSaveDto;
import com.edatasite.workforce.rest.v3.release10.hrms.service.ApiEmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Employees", description = "Employee Public API")
@RestController
@RequestMapping(path = "/employee", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiEmployeeControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiEmployeeControllerV3.class);

    private final ApiEmployeeService apiEmployeeService;

    public ApiEmployeeControllerV3(ApiEmployeeService apiEmployeeService) {
        this.apiEmployeeService = apiEmployeeService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer createEmployee(@RequestBody EmployeeSaveDto request) {
        log.info("REST request to create employee: {}", request);
        return apiEmployeeService.create(request);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateEmployee(@RequestBody EmployeeSaveDto request) {
        log.info("REST request to update employee: {}", request);
        return apiEmployeeService.update(request);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteEmployee(@PathVariable("id") Integer id) {
        log.info("REST request to delete employee: {}", id);
        apiEmployeeService.delete(id);
        return true;
    }

    @GetMapping(path = "/number", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object generateNumber(@RequestParam(value = "id", required = false) Integer id) {
        log.info("REST request to generate number: {}", id);
        Object number = apiEmployeeService.generateNumber(id);
        return number;
    }
}
