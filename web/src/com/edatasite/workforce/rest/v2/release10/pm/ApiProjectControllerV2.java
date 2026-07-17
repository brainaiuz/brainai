package com.edatasite.workforce.rest.v2.release10.pm;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TaskReminderManager;
import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.note.server.NoteServiceLocal;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.server.app.TimesheetServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.ModelFieldLocalizer;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.EntityCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.PojectTasksEmployeeListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.ProjectTasksTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.TaskEmployeesListTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Anvar Akramov on 02/03/2019.
 */

@Tag(name = "Project", description = "Project API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiProjectControllerV2 extends BaseApiControllerV2 {

    @Autowired
    private TimesheetServiceLocal timesheetServiceLocal;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private TaskReminderManager taskReminderManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private ModelFieldLocalizer modelFieldLocalizer;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private CoreServiceLocal coreServiceLocal;


    private static final Logger log = LoggerFactory.getLogger(ApiProjectControllerV2.class);

    @Operation(summary = "Get Project Employes list", description = "Request a list of Project Employes for a specific project.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Project Employes")})
    @RequestMapping(value = "/project/employes", method = RequestMethod.GET)
    public Object getProjectEmployesList(@RequestParam(value = "project_id") Integer projectId) throws RestException {

        if (projectId == null || projectId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "projectId is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsProject project = projectManager.get(projectId);
        if (project == null) {
            throw new RestException("Project not found", "project not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        List<EdsProjectEmployee> projectEmployees = projectEmployeeManager.getProjectEmployees(project);
        List<EmployeeTO> prjEmpTOs = projectEmployees.stream().map(pe -> {
            EdsEmployee employee = pe.getEmployeeDepartment().getEmployee();
            EmployeeTO employeeTO = new EmployeeTO();
            employeeTO.setName(employee.getFullName());
            employeeTO.setId(pe.getObjectID());//Set EdsProjectEmployee.getObjectID()
            if (employee.getEmployeeDepartment() != null) {
                EdsDepartment department = employee.getEmployeeDepartment().getTeam();
                if (department != null) {
                    employeeTO.setDepartment(department.getName());
                }
            }
            return employeeTO;
        }).collect(Collectors.toList());

        return successResponse(new EmployeeListTO(prjEmpTOs));
    }

    @Operation(summary = "Get Projects List", description = "Get Projects List")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the categories list of tasks")})
    @RequestMapping(value = "/projects/list", method = RequestMethod.GET)
    public Object getEntityFieldCategories(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;
        EdsUser user = userManager.getUser();
        EntityCategoryTO projectsResult = new EntityCategoryTO();

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setCategory(Constants.TASK);
        filterParameter.setCRM(true);
        ArrayList<EdsProject> projects;
        try {
            projects = (ArrayList) projectManager.list(filterParameter, user);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (projects != null) {
            if (StringUtils.isNotBlank(query)) {
                projects = (ArrayList) projects.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
            }
            projectsResult.setTotal_count(projects.size());
            if (projects.size() < (maxLimit + start)) {
                projectsResult.setLeft(0);
            } else {
                projectsResult.setLeft(projects.size() - (start + maxLimit));
            }

            ArrayList<EdsProject> subList = ListUtils.getSublistSmart(projects, start, maxLimit);
            projectsResult.setCount(subList.size());
            projectsResult.setOffset(start);
            if (subList != null) {
                projectsResult.setList(subList.stream().map(prj -> {
                    CategoryTO category = new CategoryTO();
                    category.setId(prj.getObjectID());
                    category.setTitle(prj.getName());
                    return category;
                }).collect(Collectors.toList()));
            }
        }

        return successResponse(projectsResult);
    }

   /* @Operation(summary = "Create a project", description = "Request to create a new project")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/project/create", method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Integer createProject(@RequestParam(name = "body") String jsonString) throws RestException {

        ProjectAddTO newProject;

        ObjectMapper mapper = new ObjectMapper();
        try {
            newProject = mapper.readValue(jsonString, ProjectAddTO.class);
        }catch (Exception e){
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.".concat(e.getMessage()), REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (newProject.getProject() == null) {
            throw new RestException("Please be sure you entered all required data", "task details is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (!ServerUtils.hasPermission(PermissionConstants.PM_PROJECT_ADD)){
            throw new RestException(PERMISSION_MESSAGE, PERMISSION_MESSAGE, ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        if (newProject.getProject().getName() == null || newProject.getProject().getWhen().getStart_date() == null || newProject.getProject().getWhen().getEnd_date() == null || newProject.getProject().getManager() == null) {
            throw new RestException("Please be sure you entered all required data", "task details is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Date startDate;
        Date endDate;
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        if (newProject.getProject().getWhen() != null) {

            if (StringUtils.isNotBlank(newProject.getProject().getWhen().getStart_date())) {
                try {
                    startDate = longDateTimezoneFormat.parse(newProject.getProject().getWhen().getStart_date());
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException("Invalid date format", "Invalid date format. Acceptable format is ".concat(longDateTimezoneFormat.toPattern()), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } else {
                throw new RestException("Please be sure you entered all required data", "\"start_date\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (StringUtils.isNotBlank(newProject.getProject().getWhen().getEnd_date())) {
                try {
                    endDate = longDateTimezoneFormat.parse(newProject.getProject().getWhen().getEnd_date());
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } else {
                throw new RestException("Please be sure you entered all required data", "\"end_date\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new RestException("Please be sure you entered all required data", "\"when\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        //End date must be after start date
        if (startDate.getTime() > endDate.getTime() || (startDate.getTime() == endDate.getTime() && Boolean.FALSE.equals(newProject.getProject().getWhen().getAll_day()))) {
            throw new RestException("Please enter correct date and time range for your task", "start_date after end_date", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ProjectSingleItem singleItem = new ProjectSingleItem();
        singleItem.setManagerId(newProject.getProject().getManager().getManager_id());
        singleItem.setManagerName(newProject.getProject().getManager().getManager_name());
        singleItem.setStatusId(newProject.getProject().getStatus());
        singleItem.setStartDate(startDate);
        singleItem.setEndDate(endDate);
        singleItem.setProjectMembers(newProject.getProject().getProject_members());
        singleItem.setName(newProject.getProject().getName());
        singleItem.setDescription(newProject.getProject().getDescription());
        singleItem.setBillable(newProject.getProject().isBillable());
        singleItem.setFromImport(newProject.getProject().isFrom_import());
        singleItem.setBackupManagerIDs(newProject.getProject().getBackup_manager_ids());
        singleItem.setProjectMembersId(newProject.getProject().getProject_members_ids());

        try {
            Integer result = projectService.saveProject(singleItem);
            return result;
        } catch (NumberExistingException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Project Detail Info")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have project details", response = ProjectDetailsItemTO.class),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/project/{id}/details", method = RequestMethod.GET)
    public Object getProjectDetailsInfo(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ProjectViewItem projectItem;

        try {
            projectItem = projectService.viewProject(id);
        }catch (Exception e){
            log.error("",e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (projectItem == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Project item has not been found with this project_id", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        ProjectDetailsItemTO projectDetails = new ProjectDetailsItemTO();

        //Base info
        ProjectBaseInfo baseInfo = new ProjectBaseInfo();
        baseInfo.setName(projectItem.getName());

        if (projectItem.getStartDate() != null) {
            baseInfo.setStart_date(projectItem.getStartDate());
        }

        if (projectItem.getEndDate() != null) {
            baseInfo.setEnd_date(projectItem.getEndDate());
        }

        baseInfo.setNumber(projectItem.getNumberData());

        if (projectItem.getStatus() != null) {
            baseInfo.setStatus(projectItem.getStatus());
        }

        if (projectItem.getDescription() != null) {
            baseInfo.setDescription(projectItem.getDescription());
        }

        baseInfo.setCustomer(projectItem.getClient());

        if (projectItem.getComplete() != null) {
            baseInfo.setCompleted(projectItem.getComplete());
        }

        projectDetails.setBase_info(baseInfo);

        //share link
        String share_link = EdsContextParams.getFullHost().concat(Constants.HRMS_URL).concat("#").concat("project|summary/").concat(projectItem.getObjectID().toString()).concat("/undefined/true");
        projectDetails.setShare_link(share_link);
        //project members
        if (projectItem.getProjectEmployees()!= null){
            ArrayList<ProjectAssigneeTO> assignees = new ArrayList<>();
            for (PositionsSelectItem positionSelectItem: projectItem.getProjectEmployees()) {
                if (positionSelectItem != null) {
                    ProjectAssigneeTO assignee = new ProjectAssigneeTO();
                    assignee.setId(positionSelectItem.getId());
                    assignee.setName(positionSelectItem.getName());
                    try {
                        assignee.setAvatar_image(hrmsServiceLocal.getEmployeeImageURL(positionSelectItem.getExactEmployeeId()));
                    }catch (Exception e) {
                        log.error("", e);
                    }

                    if (positionSelectItem.getDepartmentId() != null) {
                        assignee.setDepartment(new IdNameTO(positionSelectItem.getDepartmentId(), positionSelectItem.getDepartmentName()));
                    }

                    if (positionSelectItem.getPositionId() != null) {
                        assignee.setPosition(new IdNameTO(positionSelectItem.getPositionId(), positionSelectItem.getPositionName()));
                    }

                    if (positionSelectItem.getTime() != null && positionSelectItem.getTime() > 0 ) {
                        TimeTO estimate = new TimeTO();
                        int hours = positionSelectItem.getTime()/60;
                        int minutes = positionSelectItem.getTime() - hours*60;
                        estimate.setHour(hours);
                        estimate.setMinute(minutes);
                        assignee.setEstimate(estimate);
                    }

                    if (positionSelectItem.getTimeSpent() != null && positionSelectItem.getTimeSpent() > 0) {
                        TimeTO spent = new TimeTO();
                        int hours = positionSelectItem.getTimeSpent() / 60;
                        int minutes = positionSelectItem.getTimeSpent() - hours * 60;
                        spent.setHour(hours);
                        spent.setMinute(minutes);
                        assignee.setSpent(spent);
                    }

                    if (positionSelectItem.getActualTime() != null && positionSelectItem.getActualTime() > 0) {
                        TimeTO actual = new TimeTO();
                        int hours = positionSelectItem.getActualTime() /60 ;
                        int minutes = positionSelectItem.getActualTime() - hours * 60;
                        actual.setHour(hours);
                        actual.setMinute(minutes);
                        assignee.setActual(actual);
                    }

                    if (positionSelectItem.getPercent() != null) {
                        assignee.setComplete(positionSelectItem.getPercent());
                    }
                    assignees.add(assignee);
                }
            }
            projectDetails.setProject_members(assignees);
        }

        //manager
        if (projectItem.getManager() != null) {
            projectDetails.setManager_name(projectItem.getManager());
        }

        //backup managers
        if (projectItem.getBackupManagers() != null) {
            List<String> backupManagers = new ArrayList<>();
            for (int i = 0; i < projectItem.getBackupManagers().size(); i++) {
                    String backUpManagerName = projectItem.getBackupManagers().get(i).getName();
                    backupManagers.add(backUpManagerName);
            }
            projectDetails.setBackup_managers_name(backupManagers);
        }

        //attachments
        if (projectItem.getAttachments() != null && projectItem.getAttachments().length > 0) {
            List<FileItem> attachments = new ArrayList<>();
                for (FileItem item : projectItem.getAttachments()) {
                    FileItem attachment = new FileItem();
                    attachment.setFileName(item.getFileName());
                    attachment.setId(item.getId());
                    if (item.getUploadType() != null) {
                        attachment.setUploadType(item.getUploadType());
                    }
                    if (item.getDescription() != null) {
                        attachment.setDescription(item.getDescription());
                    }
                    if (item.getSize() != null) {
                        attachment.setSize(item.getSize());
                    }
                    attachments.add(attachment);
                }
            projectDetails.setAttachments(attachments);
        }

        projectDetails.setCan_edit(ServerUtils.hasPermission(PermissionConstants.PM_PROJECT_EDIT));

        return successResponse(new ProjectDetailsInfoResultTO(projectDetails));
    }

    @Operation(summary = "Delete Project", description = "Delete particular entity like Lead, Opportunity, Company, Contact etc. Particular entity is described in path, like other requests. Server should check if current user has permissions to delete this particular item, and if no give user message: You don't have permissions to delete this entry. Please contact your administrator")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/project/{item_id}/delete", method = RequestMethod.DELETE)
    @CheckPermission(permissions = {PermissionConstants.PM_MAIN_MENU, PermissionConstants.PM_PROJECT_LIST, PermissionConstants.PM_PROJECT_REMOVE})
    public Object deleteProject(@PathVariable(value = "item_id") Integer item_id) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsProject edsProject = projectManager.get(item_id);

        if (edsProject == null || edsProject.getDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Project with id" + item_id + "is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (ServerUtils.hasPermission(PermissionConstants.PM_PROJECT_REMOVE)) {
            try {
                projectService.deleteProject(item_id);
            }catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            throw new RestException(PERMISSION_MESSAGE, PERMISSION_MESSAGE, ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }

        return successResponse(new ResponseData());
    }


    @Operation(summary = "Edit a project", description = "Request to update a project")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(path = "/project/edit", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object updateProject(MultipartRequest multipartRequest, @RequestParam("body") String jsonString) throws RestException {

        ProjectAddTO addTO;

        ObjectMapper mapper = new ObjectMapper();

        try {
            addTO = mapper.readValue(jsonString, ProjectAddTO.class);
        }catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "json String is wrong".concat(e.getMessage()), REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EditProject editProject = projectService.getProjectForEdit(addTO.getProject().get)
    }*/

    @Operation(summary = "Projects LookUp", description = "Projects LookUp")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have the projects look-up items ")})
    @RequestMapping(value = "/projects/look-up", method = RequestMethod.GET)
    public Object searchProject(@RequestParam(required = false) String searchKey) throws BaseApiException {
        try {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setPM(true);
            fp.setLookUp(true);
            fp.setSearchKey(searchKey);
            fp.setLimit(20);
            SelectItem[] projects = allInOneServiceLocal.getLookUpItems(fp, CrmConstants.PM_PROJECT_ID, null);
            ArrayList<SelectItemTO> projectItems = new ArrayList<>();
            for (SelectItem it : projects) {
                projectItems.add(new SelectItemTO(it));
            }
            return successResponse(new ResponseListData<>(projectItems));
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @Operation(summary = "Task LookUp", description = "Task LookUp")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have the task look-up items ")})
    @RequestMapping(value = "/task/look-up", method = RequestMethod.GET)
    public Object searchTask(@RequestParam(value = "projectId", required = false) Integer projectId,
                             @RequestParam(required = false) String searchKey) throws BaseApiException {
        try {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setPM(true);
            fp.setLookUp(true);
            fp.setProjectId(projectId);
            fp.setSearchKey(searchKey);
            fp.setLimit(20);
            SelectItem[] tasks = allInOneServiceLocal.getLookUpItems(fp, CrmConstants.PM_TASK_ID, null);
            ArrayList<SelectItemTO> taskItems = new ArrayList<>();
            if (tasks != null) {
                for (SelectItem it : tasks) {
                    taskItems.add(new SelectItemTO(it));
                }
            }
            return successResponse(new ResponseListData<>(taskItems));
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @Operation(summary = "Get Project All Task Employees", description = "Request a list of Project Employes for a specific project.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Project Employes")})
    @RequestMapping(value = "/project/projectTasksEmployeeList", method = RequestMethod.GET)
    public Object getProjectTasksEmployesList(@RequestParam(value = "projectid", required = false) Integer projectId,
                                              @RequestParam(value = "taskid", required = false) Integer taskid) throws RestException {

        PojectTasksEmployeeListTO listTO = getPojectTasksEmployeeListTO(projectId, taskid);

        return successResponse(listTO);
    }

    private PojectTasksEmployeeListTO getPojectTasksEmployeeListTO(@RequestParam(value = "projectid", required = false) Integer projectId, @RequestParam(value = "taskid", required = false) Integer taskid) throws RestException {
        if (projectId == null || projectId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "projectId is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        List<ProjectTasksTO> projects = new ArrayList<>();
        ArrayList<TaskEmployeesListTO> tasks = new ArrayList<>();
        if (taskid != null) {
            EdsTask task = taskManager.get(taskid);
            if (task == null || task.getDeleted()) {
                throw new RestException("Task not found", "task not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            EdsProject project = task.getProject();

            ProjectTasksTO projectTO = new ProjectTasksTO();
            projectTO.setProjectId(project.getObjectID());
            projectTO.setProjectNumber(project.getNumber());
            projectTO.setProjectName(project.getName());
            projectTO.setCustomerId(project.getClient() != null ? project.getClient().getObjectID() : null);
            projectTO.setCustomerName(project.getClient() != null ? project.getClient().getName() : "");

            TaskEmployeesListTO taskEmployeesListTO = new TaskEmployeesListTO();
            taskEmployeesListTO.setTaskId(task.getObjectID());
            taskEmployeesListTO.setTaskName((task.getNumber() != null && !task.getNumber().isEmpty() ? task.getNumber() + " -> " : "") + task.getName());
            taskEmployeesListTO.setAssignees(taskServiceLocal.getAssignments(taskid));
            tasks.add(taskEmployeesListTO);
            projectTO.setTasks(tasks);
            projects.add(projectTO);
        } else {
            EdsProject project = projectManager.get(projectId);
            if (project == null) {
                throw new RestException("Project not found", "project not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            ProjectTasksTO projectTO = new ProjectTasksTO();
            projectTO.setProjectId(project.getObjectID());
            projectTO.setProjectNumber(project.getNumber());
            projectTO.setProjectName(project.getName());
            projectTO.setCustomerId(project.getClient() != null ? project.getClient().getObjectID() : null);
            projectTO.setCustomerName(project.getClient() != null ? project.getClient().getName() : "");

            for (EdsTask task : project.getUndeletedTasks()) {
                TaskEmployeesListTO taskEmployeesListTO = new TaskEmployeesListTO();
                taskEmployeesListTO.setTaskId(task.getObjectID());
                taskEmployeesListTO.setTaskName((task.getNumber() != null && !task.getNumber().isEmpty() ? task.getNumber() + " -> " : "") + task.getName());
                taskEmployeesListTO.setAssignees(taskServiceLocal.getAssignments(task.getObjectID()));
                if (taskEmployeesListTO.getAssignees() != null && taskEmployeesListTO.getAssignees().length > 0) {
                    tasks.add(taskEmployeesListTO);
                }
            }
            projectTO.setTasks(tasks);
            projects.add(projectTO);
        }

        PojectTasksEmployeeListTO listTO = new PojectTasksEmployeeListTO();
        listTO.setProjects(projects);
        return listTO;
    }

    @Operation(summary = "Insert timesheets", description = "Insert timesheet batch entries")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Timesheets")})
    @RequestMapping(value = "/project/insert-timesheet-entries", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<String> patchUpdateNews(@RequestBody PojectTasksEmployeeListTO listTO) throws RestException {
        for (ProjectTasksTO projectItem : listTO.getProjects()) {
            for (TaskEmployeesListTO taskItem : projectItem.getTasks()) {
                for (TaskInvolvedMember assingee : taskItem.getAssignees()) {
                    TimesheetDataItem timesheetItem = new TimesheetDataItem();
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                        timesheetItem.setDate(dateFormat.parse(listTO.getDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    timesheetItem.setEmployeeTaskID(assingee.getEmployeeTaskID());
                    timesheetItem.setTaskID(taskItem.getTaskId());
                    Integer timesheetMinutes;
                    try {
                        timesheetMinutes = getTimesheetMinutesFromString(assingee.getEstimateTimeInString());
                    } catch (Exception e) {
                        throw new RestException("Timesheet hours incorrect format\"", "Timesheet hours incorrect format", SERVER_ERROR, HttpStatus.BAD_REQUEST);
                    }
                    timesheetItem.setTimesheetMinutes(timesheetMinutes);
                    timesheetItem.setMinutes(timesheetMinutes);
                    timesheetServiceLocal.applyUpdates(timesheetItem, null);
                }
            }
        }
        return ResultTO.success("Timesheet batch entries saved successfully");
    }

    private Integer getTimesheetMinutesFromString(String estimateTimeInString) {
        Integer timesheetMinutes = 0;
        if (estimateTimeInString == null || estimateTimeInString.isEmpty()) {
            return timesheetMinutes;
        }
        String[] timeparts;
        if (estimateTimeInString.contains(":")) {
            timeparts = estimateTimeInString.split(":");
        } else if (estimateTimeInString.contains(".")) {
            timeparts = estimateTimeInString.split("\\.");
        } else if (estimateTimeInString.contains(",")) {
            timeparts = estimateTimeInString.split(",");
        } else {
            timeparts = estimateTimeInString.split(estimateTimeInString);
        }
        timesheetMinutes += Integer.valueOf(timeparts[0]) * 60;
        if (timeparts[1] != null) {
            timesheetMinutes += Integer.valueOf(timeparts[1]);
        }
        return timesheetMinutes;
    }


    @Operation(summary = "Project And Tasks Employes For Timesheet App", description = "Request a list of Project Employes for a specific timesheet app.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Project Employes")})
    @RequestMapping(value = "/project/projectAndTasksEmployesForTimesheetApp", method = RequestMethod.GET)
    public Object getProjectTasksEmployesForTimesheetApp(@RequestParam(value = "queryName") String queryName) {

        SelectItem[] projectItems = coreServiceLocal.dynamicLookUpResult(queryName, "", 64000);

        List<ProjectTasksTO> projects = new ArrayList<>();
        for (SelectItem projectItem : projectItems) {
            ProjectTasksTO projectTO = new ProjectTasksTO();
            EdsProject project = projectManager.get(projectItem.getId());
            projectTO.setProjectId(project.getObjectID());
            projectTO.setProjectNumber(project.getNumber());
            projectTO.setProjectName(project.getName());
            projectTO.setCustomerId(project.getClient() != null ? project.getClient().getObjectID() : null);
            projectTO.setCustomerName(project.getClient() != null ? project.getClient().getName() : "");

            ArrayList<TaskEmployeesListTO> tasks = new ArrayList<>();
            for (EdsTask task : project.getUndeletedTasks()) {
                TaskEmployeesListTO taskEmployeesListTO = new TaskEmployeesListTO();
                taskEmployeesListTO.setTaskId(task.getObjectID());
                taskEmployeesListTO.setTaskName((task.getNumber() != null && !task.getNumber().isEmpty() ? task.getNumber() + " -> " : "") + task.getName());
                taskEmployeesListTO.setAssignees(taskServiceLocal.getAssignments(task.getObjectID()));
                if (taskEmployeesListTO.getAssignees() != null && taskEmployeesListTO.getAssignees().length > 0) {
                    tasks.add(taskEmployeesListTO);
                }
            }
            projectTO.setTasks(tasks);
            if (projectTO.getTasks() != null && projectTO.getTasks().size() > 0) {
                projects.add(projectTO);
            }
            if (projects.size() > 20) {
                break;
            }
        }

        PojectTasksEmployeeListTO listTO = new PojectTasksEmployeeListTO();
        listTO.setProjects(projects);

        return successResponse(listTO);
    }

}
