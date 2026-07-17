package com.edatasite.workforce.rest.v4.hrms.controller;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v4.hrms.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Goal", description = "Goal management API")
@RestController
@RequestMapping(
        value = "/goal",
        headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
)
public class GoalController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @Operation(summary = "Get Department Goal List by department")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Get Department Goal List by department"))
    @RequestMapping(path = "department/{department}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<SelectItem>> getDepartmentGoals(@PathVariable(name = "department") Integer departmentId) {
        log.info("Get all goals of the department: {}", departmentId);
        List<SelectItem> goals = goalService.getDepartmentGoals(departmentId);
        return ResultTO.success(goals);
    }

    @Operation(summary = "Delete Goal")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Delete Goal"))
    @RequestMapping(path = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<SelectItem>> deleteGoal(@RequestParam(name = "goalId") Integer goalId, @RequestParam(name = "type") String type) {
        log.info("Delete goal: {}", goalId);
        goalService.deleteGoal(goalId, type);
        return ResultTO.success(new ArrayList<>());
    }
}
