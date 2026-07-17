package com.edatasite.workforce.rest.v3.release10.pm;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.TaskTimerDto;
import com.edatasite.workforce.rest.v3.release10.pm.service.TaskTimerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.INVALID;

/**
 * User: Mukhammadrizo
 * Date: 06.09.2023
 */
@Tag(name = "Task Timer", description = "Task Timer Public API")
@RestController
@RequestMapping(value = "/task_timer", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiTaskTimerControllerV3 {

    private final TaskTimerService taskTimerService;

    @Autowired
    public ApiTaskTimerControllerV3(TaskTimerService taskTimerService) {
        this.taskTimerService = taskTimerService;
    }


    @Operation(summary = "Start Task Timer")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Timer started"))
    @RequestMapping(value = "/start", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public Object startTaskTimer(@RequestBody TaskTimerDto timerDto) {
        try {
            taskTimerService.startTimer(timerDto);
            return ResultTO.success(Boolean.TRUE);
        } catch (RuntimeException e) {
            return ResultTO.failure(e.getMessage(), INVALID);
        } catch (Exception ex) {
            return ResultTO.failure("Something went wrong Timer did not start", INVALID);
        }
    }

    @Operation(summary = "Stop Task Timer")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Timer stoped"))
    @RequestMapping(value = "/stop", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public Object stopTaskTimer(@RequestBody TaskTimerDto timerDto) {
        try {
            taskTimerService.stopTaskTimer(timerDto);
            return ResultTO.success(Boolean.TRUE);
        } catch (Exception ex) {
            return ResultTO.failure("Something went wrong Timer did not stop", INVALID);
        }
    }

    @Operation(summary = "Reset Task Timer")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Timer reseted"))
    @RequestMapping(value = "/reset", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public Object resetTaskTimer(@RequestBody TaskTimerDto timerDto) {
        try {
            taskTimerService.resetTaskTimer(timerDto);
            return ResultTO.success(Boolean.TRUE);
        } catch (Exception ex) {
            return ResultTO.failure("Something went wrong Timer did not stop", INVALID);
        }
    }

//    @Operation(summary = "Get Task Timer")
//    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Task Timer"))
//    @RequestMapping(value = "/get", method = RequestMethod.GET, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
//    public Object getTaskTimer(Integer taskID) {
//        try {
//            return ResultTO.success(taskTimerService.getTaskTimer(taskID));
//        } catch (Exception ex) {
//            return ResultTO.failure("Something went wrong Timer did not stop", INVALID);
//        }
//    }

    @Operation(summary = "Get User Active Timers")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Get User Active Timers"))
    @RequestMapping(value = "/active_timers", method = RequestMethod.GET, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public Object getuserActiveTimers() {
        try {
            return ResultTO.success(taskTimerService.getActiveTimer());
        } catch (Exception ex) {
            return ResultTO.failure("Something went wrong Timer did not stop", INVALID);
        }
    }

    @Operation(summary = "Log Time")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Logged Time"))
    @RequestMapping(value = "/logtime", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public Object logTime(@RequestBody TaskTimerDto timerDto) {
        try {
            taskTimerService.logTime(timerDto);
            return ResultTO.success(Boolean.TRUE);
        } catch (RuntimeException e) {
            return ResultTO.failure(e.getMessage(), INVALID);
        } catch (Exception ex) {
            return ResultTO.failure("Something went wrong Timer did not stop", INVALID);
        }
    }

}
