package com.edatasite.workforce.rest.v4.hrms.controller;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.team.client.rpc.request.CreateDepartmentReq;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v4.hrms.service.DepartmentTreeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Department", description = "Department Public API")
@RestController
@RequestMapping(
        value = "/hr/department",
        headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
public class DepartmentController {

    private final DepartmentTreeService departmentTreeService;

    public DepartmentController(DepartmentTreeService service) {
        this.departmentTreeService = service;
    }

    @Operation(summary = "Create Department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Department"))
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<DepartmentNode> createDepartment(@Validated @RequestBody CreateDepartmentReq departmentReq) {
        DepartmentNode tree = departmentTreeService.createDapartment(departmentReq);
        return ResultTO.success(tree);
    }

    @Operation(summary = "Update Department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Update Department"))
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<DepartmentNode> updateDepartment(@Validated @RequestBody DepartmentNode department) {
        DepartmentNode tree = departmentTreeService.updateDepartment(department);
        return ResultTO.success(tree);
    }

    @Operation(summary = "Get Department by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Department"))
    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<DepartmentNode> getDepartment(@PathVariable(name = "id") Integer departmentId) {
        DepartmentNode tree = departmentTreeService.getDepartment(departmentId);
        return ResultTO.success(tree);
    }

    @Operation(summary = "Delete Department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Department"))
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<DepartmentNode> deleteDepartment(@PathVariable(name = "id") Integer departmentId) {
        DepartmentNode tree = departmentTreeService.deleteDepartment(departmentId);
        return ResultTO.success(tree);
    }

    @Operation(summary = "Get Department Tree")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Department Tree"))
    @RequestMapping(path = "/tree", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<DepartmentNode> getDepartmentTree() {
        DepartmentNode tree = departmentTreeService.getDepartmentTree();
        return ResultTO.success(tree);
    }

    @Operation(summary = "Sort Child Departments")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Sort Child Departments"))
    @RequestMapping(path = "/sort", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<DepartmentNode> sortDepartmentTree(@Validated @RequestBody DepartmentNode parent) {
        DepartmentNode tree = departmentTreeService.sortDepartmentTree(parent);
        return ResultTO.success(tree);
    }

    @Operation(summary = "Move a department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Sort Child Departments"))
    @RequestMapping(path = "/move", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<DepartmentNode> moveDepartment(
            @RequestParam(name = "child") Integer currentDep,
            @RequestParam(name = "parent") Integer parentDep,
            @RequestParam(name = "inherit") boolean shouldInheritColor) {
        DepartmentNode tree = departmentTreeService.moveDepartment(currentDep, parentDep, shouldInheritColor);
        return ResultTO.success(tree);
    }
}
