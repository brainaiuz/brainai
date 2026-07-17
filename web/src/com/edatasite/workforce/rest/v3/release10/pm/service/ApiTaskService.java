package com.edatasite.workforce.rest.v3.release10.pm.service;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsTaskCustomFields;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.TaskCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResponse;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.pm.TaskDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_TASK;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.REQUIRED;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.SERVER_ERROR;

@Service
public class ApiTaskService implements Constants {

    private final TaskService taskService;
    private final AttachmentUtilsManager attachmentUtilsManager;
    private final AllInOneService allInOneService;
    private final TaskManager taskManager;
    private final ReferenceManager referenceManager;
    private final ProjectManager projectManager;
    private final EmployeeManager employeeManager;
    private final WorkStreamManager workStreamManager;
    private final CommonService commonService;
    private final UserManager userManager;
    private final ProjectEmployeeManager projectEmployeeManager;
    private final CompanyCustomFieldsManager companyCustomFieldsManager;
    private final TaskCFManager taskCFManager;
    private final TimeSheetManager timeSheetManager;

    @Autowired
    public ApiTaskService(TaskService taskService, AttachmentUtilsManager attachmentUtilsManager, AllInOneService allInOneService, TaskManager taskManager, ReferenceManager referenceManager, ProjectManager projectManager, EmployeeManager employeeManager, WorkStreamManager workStreamManager, CommonService commonService, UserManager userManager, ProjectEmployeeManager projectEmployeeManager, CompanyCustomFieldsManager companyCustomFieldsManager, TaskCFManager taskCFManager, TimeSheetManager timeSheetManager) {
        this.taskService = taskService;
        this.attachmentUtilsManager = attachmentUtilsManager;
        this.allInOneService = allInOneService;
        this.taskManager = taskManager;
        this.referenceManager = referenceManager;
        this.projectManager = projectManager;
        this.employeeManager = employeeManager;
        this.workStreamManager = workStreamManager;
        this.commonService = commonService;
        this.userManager = userManager;
        this.projectEmployeeManager = projectEmployeeManager;
        this.companyCustomFieldsManager = companyCustomFieldsManager;
        this.taskCFManager = taskCFManager;
        this.timeSheetManager = timeSheetManager;
    }

    public ListResponse<TaskDto> getTasksList(ListingFilterParameter fp) {
        TaskList taskList = taskService.getTaskList(fp);

        ListResponse<TaskDto> tasks = new ListResponse<>();
        if (taskList != null) {
            List<Integer> ids = taskList.getList().stream().map(doc -> Objects.requireNonNull(doc.getObjectID())).toList();
            tasks.setTotalNumber(ids.size());
            tasks.setTotalRecords(taskList.getTotal());
            ArrayList<TaskDto> items = new ArrayList<>();
            ids.forEach(id -> {
                TaskSingleItem item = taskService.getTask(id, false);
                List<FileResource> files = attachmentUtilsManager.getAttachments(F_TASK, id, id);
                item.setNotes(allInOneService.getNotes(id, TYPE_TASK));
                items.add(ConvertUtils.toDto(item, files));
            });
            tasks.setItems(items);
        }
        return tasks;
    }

    @Transactional(readOnly = true)
    public TaskDto getById(final Integer id, boolean disableHtml) throws RestException {
        Optional.ofNullable(taskManager.get(id)).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Task with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
        TaskSingleItem task = taskService.getTask(id, false);
        List<HistoryListItem> notes = allInOneService.getNotes(id, TYPE_TASK);
        if (disableHtml) {
            notes = notes.stream().peek(n -> n.setComment(StringUtil.htmlToTxt(n.getComment()))).toList();
        }
        List<FileResource> files = attachmentUtilsManager.getAttachments(F_TASK, task.getProjectID(), id);
        return ConvertUtils.toDto(task, files, notes);
    }

    @Transactional
    public void save(final TaskDto dto, boolean isNew) throws RestException {
        LinkedHashMap<String, FormProperty> formProperties = Optional.ofNullable(commonService.getCompanyCustomFieldsAndFormProperties(ViewName.Task, LayoutRPC.TASK_MAX_FORM))
                .map(CompanyCfAndPropertyItems::getFormPropertyMap)
                .orElse(new LinkedHashMap<>());
        TaskSingleItem item;
        if (!isNew) {
            item = taskService.getTask(dto.getId(), false);
        } else {
            item = new TaskSingleItem();
        }

        item.setName(dto.getName());
        if (dto.getStatus() == null) {
            Optional.of(EdsTask.NOT_STARTED)
                    .map(referenceManager::getByCode)
                    .map(EdsReference::getObjectID)
                    .ifPresent(item::setStatusID);
        } else if (dto.getStatus().getId() != null) {
            Optional.ofNullable(dto.getStatus())
                    .map(ItemDto::getId)
                    .map(referenceManager::get)
                    .map(EdsReference::getObjectID)
                    .ifPresent(item::setStatusID);
        } else if (StringUtils.isNotBlank(dto.getStatus().getCode())) {
            Optional.ofNullable(dto.getStatus())
                    .map(ItemDto::getCode)
                    .map(referenceManager::getByCode)
                    .map(EdsReference::getObjectID)
                    .ifPresent(item::setStatusID);
        }

        item.setDescription(dto.getDescription());
        if (dto.getProject().getId() != null) {
            Optional.ofNullable(dto.getProject())
                    .map(ItemDto::getId)
                    .map(projectManager::get)
                    .map(EdsProject::getObjectID)
                    .ifPresent(item::setProjectID);
        } else if (dto.getProject().getName() != null) {
            Optional.ofNullable(dto.getProject())
                    .map(ItemDto::getName)
                    .map(projectManager::getProjectByName)
                    .map(e -> e.get(0))
                    .map(EdsProject::getObjectID)
                    .ifPresent(item::setProjectID);
        } else if (dto.getProject().getCode() != null) {
            Optional.ofNullable(dto.getProject())
                    .map(ItemDto::getCode)
                    .map(projectManager::getProjectByNumber)
                    .map(EdsProject::getObjectID)
                    .ifPresent(item::setProjectID);
        }

        NumberData numberData = Optional.ofNullable(dto.getNumber())
                .map(NumberData::new)
                .orElseGet(() -> taskService.generateTaskNumber(item.getProjectID(), new Date(), null));
        item.setNumberData(numberData);

        EdsUser user = userManager.getUser();
        item.setStartDate(ServerUtils.convertUserDateToServerDate(dto.getStartDate(), user.getUserTimezone()));
        item.setDueDate(ServerUtils.convertUserDateToServerDate(dto.getEndDate(), user.getUserTimezone()));

        item.setAllDay(dto.isAllDay());

        if (dto.isAllDay()) {
            dto.setStartDate(ServerUtils.convertUserDateToServerDate(DateUtils.resetTime(dto.getStartDate()), user.getUserTimezone()));
            dto.setEndDate(ServerUtils.convertUserDateToServerDate(DateUtils.resetTime(dto.getEndDate()), user.getUserTimezone()));
        }

        if (dto.getPriority() == null) {
            Optional.of(EdsTask.MEDIUM)
                    .map(referenceManager::getByCode)
                    .map(EdsReference::getObjectID)
                    .ifPresent(item::setPriorityID);
        } else if (!dto.getPriority().idIsNull()) {
            Optional.ofNullable(dto.getPriority())
                    .map(IdCode::getId)
                    .map(referenceManager::get)
                    .map(EdsReference::getObjectID)
                    .ifPresent(item::setPriorityID);
        } else if (!dto.getPriority().codeIsBlank()) {
            Optional.ofNullable(dto.getPriority())
                    .map(IdCode::getCode)
                    .map(referenceManager::getByCode)
                    .map(EdsReference::getObjectID)
                    .ifPresent(item::setPriorityID);
        }

        item.setBillable(dto.isBillable());
        List<IdTime> assignees = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getAssignees())) {
            dto.getAssignees().forEach(assignee -> {
                EdsEmployee emp = null;
                if (assignee.getId() != null) {
                    emp = employeeManager.get(assignee.getId());
                } else if (assignee.getName() != null) {
                    emp = employeeManager.getEmployeeByFirstNameViaLastName(assignee.getName());
                } else if (assignee.getCode() != null) {
                    emp = employeeManager.getEmployeeByNumber(assignee.getCode());
                }
                if (emp != null) {
                    EdsProject project = projectManager.get(item.getProjectID());
                    EdsProjectEmployee pe = projectEmployeeManager.getProjectEmployee(emp, project);
                    if (pe != null) {
                        assignees.add(new IdTime(pe.getObjectID(), null));
                    }
                } else {
                    Optional.ofNullable(projectEmployeeManager.get(assignee.getProjectEmployeeId()))
                            .map(a -> new IdTime(a.getObjectID(), null))
                            .ifPresent(assignees::add);
                }
            });
        } else {
            EdsUser currentUser = employeeManager.getUser();
            if (item.getProjectID() == null && formProperties.get(CustomFormConstants.TASK.PROJECT) != null && formProperties.get(CustomFormConstants.TASK.PROJECT).getSelectedId() != null) {
                item.setProjectID(formProperties.get(CustomFormConstants.TASK.PROJECT).getSelectedId());
            }
            EdsProject project = projectManager.get(item.getProjectID());
            EdsEmployee emp = employeeManager.get(currentUser.getObjectID());
            EdsProjectEmployee pe = projectEmployeeManager.getProjectEmployee(emp, project);
            if (pe != null) {
                assignees.add(new IdTime(pe.getObjectID(), null));
            }
        }
        IdTime[] itemsArray = new IdTime[assignees.size()];
        itemsArray = assignees.toArray(itemsArray);
        item.setProjectEmployees(itemsArray);

        if (dto.getWorkStream() != null && !dto.getWorkStream().idIsNull()) {
            Optional.ofNullable(workStreamManager.get(dto.getWorkStream().getId())).ifPresent(edsWorkStream -> item.setWorkstreamID(edsWorkStream.getObjectID()));
        }

        if (dto.getPredecessors() != null && !dto.getPredecessors().isEmpty()) {
            SelectItem[] predecessors = dto.getPredecessors().stream()
                    .map(ItemDto::getId)
                    .map(taskManager::get)
                    .map(EdsTask::getObjectID)
                    .map(SelectItem::new)
                    .toArray(SelectItem[]::new);

            item.setPredecessorTasks(predecessors);
        }

        if (dto.getSuccessors() != null && !dto.getSuccessors().isEmpty()) {
            SelectItem[] successors = new SelectItem[dto.getSuccessors().size()];
            for (int i = 0; i < dto.getPredecessors().size(); i++) {
                EdsTask task = taskManager.get(dto.getSuccessors().get(i).getId());
                successors[i] = new SelectItem(task.getObjectID());
            }
            item.setSuccessorTasks(successors);
        }

        if (CollectionUtils.isNotEmpty(dto.getDueDateReminder())) {
            ArrayList<CalendarEventReminder> reminders = dto.getDueDateReminder().stream()
                    .map(r -> new CalendarEventReminder(r.getType(), r.getTimes()))
                    .collect(Collectors.toCollection(ArrayList::new));
            item.setReminder(reminders);
        }

        if (CollectionUtils.isNotEmpty(dto.getNotes())) {
            String currentUserName = user.getName();
            ArrayList<HistoryListItem> notes = dto.getNotes().stream()
                    .map(n -> ConvertUtils.toEntity(n, currentUserName))
                    .collect(Collectors.toCollection(ArrayList::new));
            item.setNotes(notes);
        }
        if (dto.getAttachments() != null) {
            FileItem[] fileItems = ConvertUtils.toFileItem(dto.getAttachments());
            item.setAttachments(fileItems);
        }

        EdsTask edsTask = null;
        if (!isNew) {
            edsTask = taskManager.get(dto.getId());
        }
        item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Task), !isNew ? edsTask.getTaskCustomFields() : null));

        try {
            taskService.saveTask(item);
        } catch (NumberExistingException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getDetailedMessage(), SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        Optional.ofNullable(item.getNumberData())
                .map(NumberData::getNumberString)
                .ifPresent(dto::setNumber);
        dto.setCreatedAt(item.getTaskCreationTime());
        dto.setUpdatedAt(item.getLastModified());
        dto.setCreatedBy(item.getTaskCreator());
        dto.setUpdatedBy(item.getLastModifiedBy());
        dto.setId(item.getObjectID());
    }

    @Transactional
    public TaskDto savePatch(final TaskDto taskDto) throws RestException {
        Optional.ofNullable(taskManager.get(taskDto.getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Task with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));

        TaskSingleItem item = taskService.getTask(taskDto.getId(), false);
        EdsUser user = userManager.getUser();
        Optional.ofNullable(taskDto.getStartDate()).ifPresent(s -> item.setStartDate(ServerUtils.convertUserDateToServerDate(taskDto.getStartDate(), user.getUserTimezone())));
        Optional.ofNullable(taskDto.getEndDate()).ifPresent(s -> {
            item.setEndDate(ServerUtils.convertUserDateToServerDate(taskDto.getEndDate(), user.getUserTimezone()));
            item.setDueDate(ServerUtils.convertUserDateToServerDate(taskDto.getEndDate(), user.getUserTimezone()));
        });
        if (item.getEndDate().before(item.getStartDate())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "End date cannot be before start date", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }

        Optional.ofNullable(taskDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(taskDto.getStatus()).ifPresent(status -> {
            if (status.getId() != null) {
                Optional.ofNullable(referenceManager.get(status.getId())).ifPresent(s -> item.setStatusID(s.getObjectID()));
            } else if (StringUtils.isNotBlank(status.getCode())) {
                Optional.ofNullable(referenceManager.getByCode(status.getCode())).ifPresent(s -> item.setStatusID(s.getObjectID()));
            }
        });

        Optional.of(taskDto.isAllDay()).ifPresent(item::setAllDay);
        Optional.of(taskDto.isBillable()).ifPresent(item::setBillable);

        Optional.ofNullable(taskDto.getPriority()).ifPresent(priority -> {
            if (!priority.idIsNull()) {
                Optional.ofNullable(referenceManager.get(priority.getId())).ifPresent(s -> item.setPriorityID(s.getObjectID()));
            } else if (!priority.codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(priority.getCode())).ifPresent(s -> item.setPriorityID(s.getObjectID()));
            }
        });

        Optional.ofNullable(taskDto.getAssignees()).ifPresent(as -> {
            List<IdTime> assignees = new ArrayList<>();
            as.forEach(assignee -> {
                EdsEmployee emp = null;
                if (assignee.getId() != null) {
                    emp = employeeManager.get(assignee.getId());
                } else if (assignee.getName() != null) {
                    emp = employeeManager.getEmployeeByFirstNameViaLastName(assignee.getName());
                } else if (assignee.getCode() != null) {
                    emp = employeeManager.getEmployeeByNumber(assignee.getCode());
                }
                if (emp != null) {
                    EdsProject project = projectManager.get(item.getProjectID());
                    EdsProjectEmployee pe = projectEmployeeManager.getProjectEmployee(emp, project);
                    if (pe != null) {
                        assignees.add(new IdTime(pe.getObjectID(), null));
                    }
                } else {
                    Optional.ofNullable(projectEmployeeManager.get(assignee.getProjectEmployeeId()))
                            .map(a -> new IdTime(a.getObjectID(), null))
                            .ifPresent(assignees::add);
                }
            });
            IdTime[] itemsArray = new IdTime[assignees.size()];
            itemsArray = assignees.toArray(itemsArray);
            item.setProjectEmployees(itemsArray);

        });

        Optional.ofNullable(taskDto.getWorkStream()).flatMap(w -> Optional.ofNullable(workStreamManager.get(w.getId()))).ifPresent(edsWorkStream -> item.setWorkstreamID(edsWorkStream.getObjectID()));
        Optional.ofNullable(taskDto.getPredecessors()).ifPresent(pres -> {
            SelectItem[] predecessors = new SelectItem[pres.size()];
            for (int i = 0; i < pres.size(); i++) {
                EdsTask task = taskManager.get(pres.get(i).getId());
                predecessors[i] = new SelectItem(task.getObjectID());
            }
            item.setPredecessorTasks(predecessors);
        });

        Optional.ofNullable(taskDto.getSuccessors()).ifPresent(successors -> {
            SelectItem[] successorsItems = new SelectItem[successors.size()];
            for (int i = 0; i < successors.size(); i++) {
                EdsTask task = taskManager.get(successors.get(i).getId());
                successorsItems[i] = new SelectItem(task.getObjectID());
            }
            item.setSuccessorTasks(successorsItems);
        });

        Optional.ofNullable(taskDto.getDueDateReminder()).ifPresent(reminderDtos -> {
            ArrayList<CalendarEventReminder> reminders = new ArrayList<>();
            reminderDtos.forEach(rem -> reminders.add(new CalendarEventReminder(rem.getType(), rem.getTimes())));
            item.setReminder(reminders);
        });

        Optional.ofNullable(taskDto.getNotes()).ifPresent(noteDtos -> {
            ArrayList<HistoryListItem> notes = new ArrayList<>();
            for (NoteDto noteDto : noteDtos) {
                notes.add(ConvertUtils.toEntity(noteDto, employeeManager.getUser().getName()));
            }
            item.setNotes(notes);
        });

        Optional.ofNullable(taskDto.getCustomFields()).ifPresent(customFields -> {
            EdsTask edsTask = taskManager.get(taskDto.getId());
            item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(customFields, commonService.getCompanyCustomFields(ViewName.Task), edsTask.getTaskCustomFields()));
        });

        try {
            taskService.saveTask(item);
        } catch (NumberExistingException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        taskDto.setNumber(item.getNumberData().getNumberString());
        taskDto.setCreatedAt(item.getTaskCreationTime());
        taskDto.setUpdatedAt(item.getLastModified());
        taskDto.setCreatedBy(item.getTaskCreator());
        taskDto.setUpdatedBy(item.getLastModifiedBy());
        taskDto.setId(item.getObjectID());
        return taskDto;
    }

    public ListResponse<TaskDto> getSimpleTaskList(ListingFilterParameter fp) {
        if (fp.isBriefly()) fp.setListPanelTool(getListPanelToolRpc(fp));
        TaskList taskList = taskService.getTaskList(fp);
        if (taskList == null) return new ListResponse<>();

        ArrayList<TaskDto> items = taskList.getList().stream()
                .map(ConvertUtils::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
        if (fp.isBriefly()) {
            List<Integer> taskIds = taskList.getList().stream().map(TaskListItem::getObjectID).toList();
            HashMap<Integer, Double[]> taskCostAndTimeMap = timeSheetManager.getCostAndTimeSpentOnTasks(ServerUtils.getAsCommoDelimited(taskIds, "0", ","));
            items = items.stream()
                    .peek(taskItem -> {
                        Double[] taskCostAndTime = taskCostAndTimeMap.get(taskItem.getId());
                        taskItem.setSpentTime(taskCostAndTime != null && taskCostAndTime[TASK_HOUR_SPENT] != null ? taskCostAndTime[TASK_HOUR_SPENT].intValue() : 0);
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        ListResponse<TaskDto> tasks = new ListResponse<>();
        tasks.setTotalNumber(items.size());
        tasks.setTotalRecords(taskList.getTotal());
        tasks.setItems(items);
        return tasks;

    }

    private static ListPanelToolRpc getListPanelToolRpc(ListingFilterParameter fp) {
        ListPanelToolRpc listPanelTool = fp.getListPanelTool();
        if (listPanelTool == null) {
            listPanelTool = ListPanelToolRpc.createIntance();
        }
        ArrayList<String> listPanelColumnCodes = Optional.ofNullable(listPanelTool.getColumnCodeName()).orElse(new ArrayList<>());
        listPanelColumnCodes.addAll(new ArrayList<>(Arrays.asList(TaskListItem.NUMBER,
                TaskListItem.NAME,
                TaskListItem.PROJECT_NAME,
                TaskListItem.CLIENT,
                TaskListItem.PRIORITY_NAME,
                TaskListItem.STATUS_NAME,
                TaskListItem.START_DATE,
                TaskListItem.DUE_DATE,
                TaskListItem.COMPLETE,
                TaskListItem.DESCRIPTION,
                TaskListItem.LAST_MODIFIED,
                TaskListItem.ASSIGNED_TO)));
        listPanelTool.setColumnCodeName(listPanelColumnCodes);
        return listPanelTool;
    }

    public NumberData generateTaskNumber(Integer projectId, String startDate) throws ParseException, RestException {
        if (projectId == null) {
            LinkedHashMap<String, FormProperty> formProperties = Optional.ofNullable(commonService.getCompanyCustomFieldsAndFormProperties(ViewName.Task, LayoutRPC.TASK_MAX_FORM))
                    .map(CompanyCfAndPropertyItems::getFormPropertyMap)
                    .orElse(new LinkedHashMap<>());
            if (projectId == null && formProperties.get(CustomFormConstants.TASK.PROJECT) != null && formProperties.get(CustomFormConstants.TASK.PROJECT).getSelectedId() != null) {
                projectId = formProperties.get(CustomFormConstants.TASK.PROJECT).getSelectedId();
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Project id is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return taskService.generateTaskNumber(projectId, startDate != null ? dateFormat.parse(startDate) : new Date(), null);
    }

    @Transactional
    public void saveTaskCustomField(Integer itemId, String alias, FileResource fileResource) {
        ArrayList<CompanyCustomFieldItem> customFieldsValue = getCustomFieldsValue(companyCustomFieldsManager.getByAliasName(ViewName.Task.name(), alias), toFileItem(List.of(fileResource)));
        EdsTask edsTask = taskManager.get(itemId);
        EdsTaskCustomFields taskCustomFields = createTaskCustomFields(edsTask.getTaskCustomFields(), customFieldsValue);
        edsTask.setTaskCustomFields(taskCustomFields);
        taskManager.updateTask(edsTask);
    }

    @Transactional
    public EdsTaskCustomFields createTaskCustomFields(EdsTaskCustomFields edsTaskCustomField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems == null || customFieldItems.size() == 0) {
            return null;
        }
        if (edsTaskCustomField == null) {
            boolean isEmpty = customFieldItems.stream().noneMatch(fieldItem -> (fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                    || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                    || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0));
            if (isEmpty) {
                return null;
            }
            edsTaskCustomField = new EdsTaskCustomFields();
            taskCFManager.create(edsTaskCustomField);
        }
        CustomFieldsUtils.setDomenObjectCustomFields(edsTaskCustomField, customFieldItems);
        return edsTaskCustomField;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldsValue(EdsCompanyCustomFieldsSettings companyCustomFieldItem, FileItem[] fileItems) {
        CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
        resultItem.setObjectId(companyCustomFieldItem.getObjectID());
        resultItem.setDataType(companyCustomFieldItem.getDataType());
        resultItem.setColumnCode(companyCustomFieldItem.getColumnCode());
        resultItem.setFieldName(companyCustomFieldItem.getFieldName());
        resultItem.setAliasName(companyCustomFieldItem.getAliasName());
        resultItem.setFileUploadFieldId(companyCustomFieldItem.getObjectID());
        resultItem.setUiType(companyCustomFieldItem.getUiType());
        resultItem.setEntityCategoryName(companyCustomFieldItem.getEntityCategoryName());
        resultItem.setPrefix(companyCustomFieldItem.getPrefix());
        resultItem.setScale(companyCustomFieldItem.getScale());
        resultItem.setAttachments(fileItems);
        return new ArrayList<CompanyCustomFieldItem>(List.of(resultItem));
    }

    public static FileItem[] toFileItem(List<FileResource> fileResources) {
        if (fileResources == null || fileResources.isEmpty()) return null;
        FileItem[] fileItems = new FileItem[fileResources.size()];
        for (int i = 0; i < fileResources.size(); i++) {
            FileResource attachment = fileResources.get(i);
            FileItem fileItem = new FileItem();
            fileItem.setFileName(attachment.getFileName());
            fileItem.setAttachmentId(attachment.getEmailAttachmentID());
            fileItem.setUploadType(attachment.getUploadType());
            fileItem.setId(attachment.getObjectId());
            fileItem.setAddedBy(attachment.getCreatedBy());
            fileItem.setContentType(attachment.getContentType());
            fileItem.setDescription(attachment.getDescription());
            fileItem.setSize(attachment.getContentLength());
            fileItems[i] = fileItem;
        }
        return fileItems;
    }

}
