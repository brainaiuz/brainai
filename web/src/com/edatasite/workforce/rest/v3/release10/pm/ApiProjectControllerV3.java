package com.edatasite.workforce.rest.v3.release10.pm;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.project.client.rpc.NearbyProjectDto;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.TimesheetTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.pm.project.ProjectEmployeeListDTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.ProjectDTO;
import com.edatasite.workforce.rest.v3.release10.pm.service.ApiProjectService;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;

/**
 * User: Akhror
 * Date: 07.07.2021
 */
@Tag(name = "Project", description = "Project Public API")
@RestController
@RequestMapping(value = "/project", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiProjectControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiProjectControllerV3.class);

    @Autowired
    private ApiProjectService apiProjectService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TimesheetService timesheetService;


    @Operation(summary = "Get Projects list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Projects"))
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ListResultTO<ProjectDTO>> getProjectsList(@RequestBody ListParamsDTO params,
                                                              @RequestParam(value = "simple", required = false) Boolean simple) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.ProjectListPanel);

        ListResultTO<ProjectDTO> projectList;
        if (simple != null && simple) {
            projectList = apiProjectService.getSimpleProjectList(fp);
        } else {
            projectList = apiProjectService.getProjectList(fp);
        }
        return ResultTO.success(projectList);
    }

    @Operation(summary = "Get existing project by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Project"))
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ProjectDTO> getProjectById(@PathVariable Integer id) throws RestException {
        return ResultTO.success(apiProjectService.getById(id));
    }

    @Operation(summary = "Create new Project")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Project"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) throws RestException, NumberExistingException {
        if (projectDTO.getId() != null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Project ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        if (projectDTO.getDueDate().before(projectDTO.getStartDate())) {
            throw new RestException("End Date cannot be before Start Date", "End Date cannot be before Start Date", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiProjectService.save(projectDTO);
        return ResultTO.success(projectDTO);
    }

    @Operation(summary = "Update existing task")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Project"))
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ProjectDTO> updateProject(@RequestBody ProjectDTO projectDTO) throws RestException, NumberExistingException {
        if ((projectDTO.getId() == null || projectDTO.getId() < 1) && projectDTO.getNumber() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Project ID and Number is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        if (projectDTO.getDueDate().before(projectDTO.getStartDate())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "End Date cannot be before Start Date", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiProjectService.update(projectDTO);
        return ResultTO.success(projectDTO);
    }

    @Operation(summary = "Patch Update existing project")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Project"))
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ProjectDTO> patchUpdateProject(@RequestBody ProjectDTO projectDTO) throws RestException, NumberExistingException {
        if (projectDTO.getId() == null && projectDTO.getNumber() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Project ID and number are not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        apiProjectService.savePatch(projectDTO);
        return ResultTO.success(projectDTO);
    }

    @Operation(summary = "Delete existing project by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Project"))
    @DeleteMapping(value = "/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> deleteProject(@PathVariable final Integer projectId) {
        projectService.deleteProject(projectId);
        return ResultTO.success();
    }

    @Operation(summary = "Get project employees")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Employees"))
    @GetMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<ProjectEmployeeListDTO>> getProjectEmployee(@RequestParam("projectId") int projectId, @RequestParam(required = false) String employeeName) {
        log.info("REST request to get project employees by project id: {}", projectId);
        List<ProjectEmployeeListDTO> projectEmployees = apiProjectService.getProjectEmployees(projectId, employeeName);
        return ResultTO.success(projectEmployees);
    }

    @GetMapping(value = "/number", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<String> generateNumber(@RequestParam(value = "startDate", required = false) String startDate) throws ParseException {
        log.info("REST request to generate project number");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        NumberData numberData = projectService.generateProjectNumber(startDate != null ? dateFormat.parse(startDate) : new Date(), null, null);
        return ResultTO.success(numberData.getNumberString());
    }

    @GetMapping(value = "/timesheet", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<TimesheetTO> getWeeklyTimesheet(@RequestParam(value = "date") String date, @RequestParam(value = "userId") Integer userId) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        DateNonConvertable selectedDateNonConvertable = new DateNonConvertable(dateFormat.parse(date));
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setUseSelectedDate(true);
        filter.setEmployeeId(userId);

        int weekOffset = timesheetService.getWeekOffset(new DateNonConvertable(), selectedDateNonConvertable);
        FastTimesheetData data = timesheetService.getFastTimesheetData(selectedDateNonConvertable, new DateNonConvertable(), weekOffset, filter);

        Calendar selectedDateCal = new GregorianCalendar();
        selectedDateCal.setTime(dateFormat.parse(date));
        selectedDateCal.set(Calendar.AM_PM, 0);
        selectedDateCal.set(Calendar.HOUR, 0);
        selectedDateCal.set(Calendar.MINUTE, 0);
        selectedDateCal.set(Calendar.SECOND, 0);
        selectedDateCal.set(Calendar.MILLISECOND, 0);

        return ResultTO.success(new TimesheetTO(data, false, WrapUtils.dateToLong(selectedDateCal.getTime())));
    }

    @GetMapping(value = "/nearby", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<NearbyProjectDto>> getNearbyProjects(@RequestParam Double latitude,
                                                              @RequestParam Double longitude,
                                                              @RequestParam Integer radius,
                                                              @RequestParam(required = false) List<Integer> assigneeIds,
                                                              @RequestParam(required = false) List<String> statusCodes,
                                                              @RequestParam(required = false) List<String> excludeStatusCodes) {
        log.info("REST request to get nearby projects");
        List<NearbyProjectDto> nearbyProjects = projectService.getNearbyProjects(latitude, longitude, radius, assigneeIds, statusCodes, excludeStatusCodes);
        return ResultTO.success(nearbyProjects);
    }
}
