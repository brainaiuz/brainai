package com.edatasite.workforce.rest.v4.hrms.controller;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.dto.EmployeeItem;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v4.hrms.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Employee", description = "Employee Lookup API")
@RestController
@RequestMapping(
        value = "/employee",
        headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
)
public class EmployeeController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Get Employee List by department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Employee List by department"))
    @RequestMapping(path = "department/{department}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<EmployeeItem>> getEmployeesByDepartment(@PathVariable(name = "department") Integer departmentId) {
        log.info("Get all employees of the department: {}", departmentId);
        List<EmployeeItem> employees = employeeService.getEmployeesByDepartment(departmentId);
        return ResultTO.success(employees);
    }

    @Operation(summary = "Get Vacant List by department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Vacant List by department"))
    @RequestMapping(path = "department/vacant/{department}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<EmployeeItem>> getVacantsByDepartment(@PathVariable(name = "department") Integer departmentId) {
        log.info("Get all vacant of the department: {}", departmentId);
        List<EmployeeItem> employees = employeeService.getVacantsByDepartment(departmentId);
        return ResultTO.success(employees);
    }

    @Operation(summary = "Get Employee List by position")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Employee List by position"))
    @RequestMapping(path = "position/{position}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<EmployeeItem>> getEmployeesByPosition(@PathVariable(name = "position") Integer positionId) {
        log.info("Get all employees of the position: {}", positionId);
        List<EmployeeItem> employees = employeeService.getEmployeesByPosition(positionId);
        return ResultTO.success(employees);
    }

    @Operation(summary = "Get All Employee List")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Employee All List"))
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<EmployeeItem>> getEmployeeList() {
        log.info("Get all employees");
        List<EmployeeItem> employees = employeeService.getAllEmployees();
        return ResultTO.success(employees);
    }

    @Operation(summary = "Assign a manager to the department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Assign a manager to the department"))
    @RequestMapping(path = "/assign", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<EmployeeItem>> assignManager(
            @RequestParam(name = "employee") Integer employeeId,
            @RequestParam(name = "department") Integer departmentId) {
        if (employeeId == null || departmentId == null)
            throw new RuntimeException("Employee ID and Department ID are required");
        log.info("Assign an employee {} as a manager for the department of {}", employeeId, departmentId);

        List<EmployeeItem> employees = employeeService.assignManager(employeeId, departmentId);
        return ResultTO.success(employees);
    }

    @Operation(summary = "Unassign a manager to the department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Unassign a manager to the department"))
    @RequestMapping(path = "/unassign", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<EmployeeItem>> unAssignManager(
            @RequestParam(name = "employee") Integer employeeId,
            @RequestParam(name = "department") Integer departmentId) {
        if (employeeId == null || departmentId == null)
            throw new RuntimeException("Employee ID and Department ID are required");
        log.info("Unassign an employee {} as a manager for the department of {}", employeeId, departmentId);

        List<EmployeeItem> employees = employeeService.unAssignManager(employeeId, departmentId);
        return ResultTO.success(employees);
    }

    @Operation(summary = "Remove an employee from the department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Remove an employee from the department"))
    @RequestMapping(path = "/department/removeFromDepartment", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<EmployeeItem>> removeFromDepartment(
            @RequestParam(name = "employee") Integer employeeId,
            @RequestParam(name = "department") Integer departmentId,
            @RequestParam(name = "vacant") Boolean isVacant) {
        if (employeeId == null || departmentId == null)
            throw new RuntimeException("Employee ID and Department ID are required");
        log.info("Remove an employee {} from the department of {}", employeeId, departmentId);

        List<EmployeeItem> employees = employeeService.removeFromDepartment(employeeId, departmentId, isVacant);
        return ResultTO.success(employees);
    }

    @Operation(summary = "Add an employee to the department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Add an employee to the department"))
    @RequestMapping(path = "/department/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<EmployeeItem>> addEmployeeToDepartment(
            @RequestParam(name = "employee") Integer employeeId,
            @RequestParam(name = "department") Integer departmentId
    ) {
        if (employeeId == null || departmentId == null)
            throw new RuntimeException("Employee ID and Department ID are required");
        log.info("Add an employee {} to the department of {}", employeeId, departmentId);

        List<EmployeeItem> employees = employeeService.addEmployeeToDepartment(employeeId, departmentId);
        return ResultTO.success(employees);
    }

    @Operation(summary = "Add a vacant to the department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Add a vacant to the department"))
    @RequestMapping(path = "/department/vacant/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<EmployeeItem>> addVacantToDepartment(
            @RequestParam(name = "employee") Integer employeeId,
            @RequestParam(name = "department") Integer departmentId
    ) {
        if (employeeId == null || departmentId == null)
            throw new RuntimeException("Employee ID and Department ID are required");
        log.info("Add an employee {} to the department of {}", employeeId, departmentId);

        List<EmployeeItem> employees = employeeService.addVacantToDepartment(employeeId, departmentId);
        return ResultTO.success(employees);
    }
}
