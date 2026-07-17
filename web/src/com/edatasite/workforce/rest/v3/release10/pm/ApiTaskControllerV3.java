package com.edatasite.workforce.rest.v3.release10.pm;

import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResponse;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.pm.TaskDto;
import com.edatasite.workforce.rest.v3.release10.pm.service.ApiTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PM_CONTEXT;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.INVALID;

/**
 * User: Akhror
 * Date: 10.03.2021 20:45
 */
@Tag(name = "Task", description = "Task Public API")
@RestController
@RequestMapping(value = "/task", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiTaskControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiTaskControllerV3.class);

    private final ApiTaskService apiTaskService;
    private final TaskService taskService;
    private final IPostPDFHandler taskViewPDFHandler;

    public ApiTaskControllerV3(ApiTaskService apiTaskService,
                               TaskService taskService,
                               @Qualifier("taskViewPDFHandler") IPostPDFHandler taskViewPDFHandler) {
        this.apiTaskService = apiTaskService;
        this.taskService = taskService;
        this.taskViewPDFHandler = taskViewPDFHandler;
    }


    @Operation(summary = "Get Tasks list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Tasks"))
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ListResponse<TaskDto>> getTasksList(@RequestBody ListParamsDTO params,
                                                        @RequestParam(value = "brief", required = false) Boolean brief,
                                                        @RequestParam(value = "simple", required = false) Boolean simple) {
        log.info("REST request to get task list brief: {} and simple: {}", brief, simple);
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.TaskListPanel);
        ListResponse<TaskDto> tasks;
        if (simple != null && simple) {
            fp.setBriefly(brief == null || brief);
            tasks = apiTaskService.getSimpleTaskList(fp);
        } else {
            tasks = apiTaskService.getTasksList(fp);
        }
        return ResultTO.success(tasks);
    }

    @Operation(summary = "Get existing task by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Task"))
    @GetMapping(path = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<TaskDto> getTaskById(@PathVariable Integer taskId,
                                         @RequestParam(value = "disableHtml", required = false) Boolean disableHtml) throws RestException {
        log.info("REST request to get task by id: {}", taskId);
        TaskDto taskById = apiTaskService.getById(taskId, disableHtml != null && disableHtml);
        return ResultTO.success(taskById);
    }

    @Operation(summary = "Create new Task")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Task"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<TaskDto> createTask(@RequestBody TaskDto taskDto) throws RestException {
        log.info("REST request to create task: {}", taskDto);
        if (taskDto.getId() != null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        if (taskDto.getEndDate().before(taskDto.getStartDate())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "End Date cannot be before Start Date", INVALID, HttpStatus.BAD_REQUEST);
        }
        apiTaskService.save(taskDto, true);
        return ResultTO.success(taskDto);
    }

    @Operation(summary = "Update existing task")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Task"))
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<TaskDto> updateTask(@RequestBody TaskDto taskDto) throws RestException {
        log.info("REST request to update task: {}", taskDto);
        if (taskDto.getId() == null || taskDto.getId() < 1) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        if (taskDto.getEndDate().before(taskDto.getStartDate())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "End Date cannot be before Start Date", INVALID, HttpStatus.BAD_REQUEST);
        }
        apiTaskService.save(taskDto, false);
        return ResultTO.success(taskDto);
    }

    @Operation(summary = "Patch Update existing task")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Task"))
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<TaskDto> patchUpdateTask(@RequestBody TaskDto taskDto) throws RestException {
        log.info("REST request to patch task: {}", taskDto);
        if (taskDto.getId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        apiTaskService.savePatch(taskDto);
        return ResultTO.success(taskDto);
    }

    @Operation(summary = "Delete existing task by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Task"))
    @DeleteMapping(value = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> deleteTask(@PathVariable Integer taskId) {
        log.info("REST request to delete task: {}", taskId);
        taskService.deleteTask(taskId, PM_CONTEXT);
        return ResultTO.success();
    }

    @Operation(summary = "Delete existing tasks by ids")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Deleted"))
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> deleteTaskList(@RequestBody ArrayList<Integer> taskIds) {
        log.info("REST request to delete tasks by ids: {}", taskIds);
        taskService.deleteTasks(taskIds, PM_CONTEXT);
        return ResultTO.success();
    }

    @GetMapping(value = "/number", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<String> generateTaskNumber(@RequestParam(value = "projectId", required = false) Integer projectId,
                                               @RequestParam(value = "startDate", required = false) String startDate) throws ParseException, RestException {
        log.info("REST request to generate task number by project: {} and date: {}", projectId, startDate);
        NumberData numberData = apiTaskService.generateTaskNumber(projectId, startDate);
        return ResultTO.success(numberData.getNumberString());
    }

    @PostMapping(path = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf(@PathVariable("id") Integer taskId) {
        log.info("REST request to generate pdf for task: {}", taskId);
        ByteArrayOutputStream pdfStream = taskViewPDFHandler.getPDFStream(new RequestObject(taskId));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfStream.toByteArray());
    }
}
