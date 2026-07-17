package com.edatasite.workforce.rest.v3.release10.pm.service;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsProjectCustomFields;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.CloneTaskItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ProjectCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.project.client.rpc.CloneProjectItem;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.EmployeeTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.ReminderDto;
import com.edatasite.workforce.rest.v3.release10.core.to.pm.project.ProjectEmployeeListDTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.CloneProjectDto;
import com.edatasite.workforce.rest.v3.release10.pm.dto.ProjectDTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.ProjectEmployeeDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_PROJECT;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;

/**
 * User: Akhror
 * Date: 07.07.2021
 */
@Service
public class ApiProjectService implements Constants {
    private final ProjectService projectService;
    private final AttachmentUtilsManager attachmentUtilsManager;
    private final AllInOneService allInOneService;
    private final ProjectManager projectManager;
    private final ReferenceManager referenceManager;
    private final CrmAccountManager accountManager;
    private final EmployeeManager employeeManager;
    private final LocationManager locationManager;
    private final UserManager userManager;
    private final CommonService commonService;
    private final ProjectCFManager projectCFMananger;
    private final CompanyCustomFieldsManager companyCustomFieldsManager;

    @Autowired
    public ApiProjectService(ProjectService projectService, AttachmentUtilsManager attachmentUtilsManager, AllInOneService allInOneService, ProjectManager projectManager, ReferenceManager referenceManager, CrmAccountManager accountManager, EmployeeManager employeeManager, LocationManager locationManager, UserManager userManager, CommonService commonService, ProjectCFManager projectCFMananger, CompanyCustomFieldsManager companyCustomFieldsManager) {
        this.projectService = projectService;
        this.attachmentUtilsManager = attachmentUtilsManager;
        this.allInOneService = allInOneService;
        this.projectManager = projectManager;
        this.referenceManager = referenceManager;
        this.accountManager = accountManager;
        this.employeeManager = employeeManager;
        this.locationManager = locationManager;
        this.userManager = userManager;
        this.commonService = commonService;
        this.projectCFMananger = projectCFMananger;
        this.companyCustomFieldsManager = companyCustomFieldsManager;
    }

    public ListResultTO<ProjectDTO> getProjectList(ListingFilterParameter fp) {
        ListResult<ProjectListItem> projectList = projectService.getProjectList(fp);

        ListResultTO<ProjectDTO> projects = new ListResultTO<>();
        EdsUser user = userManager.getUser();
        if (projectList != null) {
            List<Integer> ids = projectList.getList().stream().map(doc -> Objects.requireNonNull(doc.getObjectId())).toList();
            projects.setTotalNumber(ids.size());
            ArrayList<ProjectDTO> items = new ArrayList<>();
            ids.forEach(id -> {
                ProjectViewItem item = projectService.viewProject(id);
                List<FileResource> files = attachmentUtilsManager.getAttachments(F_PROJECT, id, id);
                List<HistoryListItem> notes = allInOneService.getNotes(id, TYPE_PROJECT);
                items.add(ConvertUtils.toDto(item, files, notes, user));
            });
            projects.setItems(items);
        }
        return projects;
    }

    public ListResultTO<ProjectDTO> getSimpleProjectList(ListingFilterParameter fp) {
        ListResult<ProjectListItem> projectList = projectService.getProjectList(fp);

        ListResultTO<ProjectDTO> projects = new ListResultTO<>();
        EdsUser user = userManager.getUser();
        if (projectList != null) {
            ArrayList<ProjectDTO> items = projectList.getList().stream()
                    .map(e -> ConvertUtils.toDto(e, user))
                    .collect(Collectors.toCollection(ArrayList::new));
            projects.setTotalNumber(items.size());
            projects.setItems(items);
        }
        return projects;
    }

    @Transactional(readOnly = true)
    public ProjectDTO getById(Integer id) throws RestException {
        EdsProject value = projectManager.get(id);
        if (value == null)
            throw new RestException(GENERAL_ERROR_MESSAGE, "Project with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        ProjectViewItem item = projectService.viewProject(id);
        List<FileResource> files = attachmentUtilsManager.getAttachments(F_PROJECT, id, id);
        List<HistoryListItem> notes = allInOneService.getNotes(id, TYPE_PROJECT);
        EdsUser user = userManager.getUser();
        return ConvertUtils.toDto(item, files, notes, user);
    }

    @Transactional
    public ProjectDTO save(final ProjectDTO dto) throws NumberExistingException {
        if (dto.isCloneProject()) {
            return clone(dto);
        }
        return create(dto);
    }

    private ProjectDTO create(ProjectDTO dto) throws NumberExistingException {
        ProjectSingleItem newProject = new ProjectSingleItem();
        newProject.setName(dto.getName());
        newProject.setDescription(dto.getDescription());
        newProject.setStartDate(DateUtils.resetTime(dto.getStartDate()));
        newProject.setEndDate(DateUtils.resetTime(dto.getDueDate()));
        newProject.setNumberData(dto.getNumber() != null ? new NumberData(dto.getNumber()) : projectService.generateProjectNumber(new Date(), null, null));
        if (CollectionUtils.isNotEmpty(dto.getNotes())) {
            List<HistoryListItem> historyListItems = dto.getNotes().stream().map(e -> new HistoryListItem(e.getText())).collect(Collectors.toList());
            newProject.setNotes(historyListItems);
        }
        if (CollectionUtils.isNotEmpty(dto.getAttachments())) {
            FileItem[] fileItems = requestToFileItem(dto);
            newProject.setAttachments(fileItems);
        }
        if (dto.getStatus() != null) {
            EdsReference status = null;
            if (dto.getStatus().getId() != null) {
                status = referenceManager.get(dto.getStatus().getId());
            } else if (dto.getStatus().getCode() != null) {
                status = referenceManager.getByCode(dto.getStatus().getCode());
            } else if (dto.getStatus().getName() != null) {
                status = referenceManager.getByName(dto.getStatus().getName());
            }
            if (status != null) {
                newProject.setStatusID(status.getObjectID());
                newProject.setStatusId(status.getObjectID());
            }
        }

        if (dto.getCustomer() != null) {
            EdsCrmAccount customer = null;
            if (dto.getCustomer().getId() != null) {
                customer = accountManager.get(dto.getCustomer().getId());
            } else if (dto.getCustomer().getName() != null) {
                customer = accountManager.getCrmAccountByName(dto.getCustomer().getName());
            } else if (dto.getCustomer().getCode() != null) {
                customer = accountManager.getCrmAccountByNumber(dto.getCustomer().getCode());
            }
            if (customer != null) {
                newProject.setClientId(customer.getObjectID());
            }
        }

        if (CollectionUtils.isNotEmpty(dto.getDueDateReminder())) {
            ArrayList<CalendarEventReminder> eventReminders = dto.getDueDateReminder().stream()
                    .filter(Objects::nonNull)
                    .filter(r -> r.getTimes() != null)
                    .filter(r -> r.getTimes() > 0)
                    .map(reminder -> new CalendarEventReminder(reminder.getType(), reminder.getTimes()))
                    .collect(Collectors.toCollection(ArrayList::new));
            newProject.setReminder(eventReminders);
        }

        ArrayList<ProjectMember> members = new ArrayList<>();
        for (ProjectEmployeeDTO info : dto.getEmployees()) {
            ProjectMember member = new ProjectMember();
            EdsEmployee employee = null;
            if (info.getEmployee().getId() != null) {
                employee = employeeManager.get(info.getEmployee().getId());
            } else if (info.getEmployee().getName() != null) {
                employee = employeeManager.getEmployeeByFirstNameViaLastName(info.getEmployee().getName());
            } else if (info.getEmployee().getCode() != null) {
                employee = employeeManager.getEmployeeByNumber(info.getEmployee().getCode());
            }
            if (employee != null) {
                member.setId(employee.getObjectID());
                member.setName(employee.getFullName());
                member.setCheck(true);
                member.setWageRate(info.getWageRate());
                member.setClientChargeRate(info.getCustomerChargeRate());
                member.setWorkloadPercentage(info.getWorkload());
                members.add(member);
            }
        }
        newProject.setProjectMembers(members.toArray(new ProjectMember[0]));

        if (dto.getManager() != null) {
            EdsEmployee manager = null;
            if (dto.getManager().getId() != null) {
                manager = employeeManager.get(dto.getManager().getId());
            } else if (dto.getManager().getName() != null) {
                manager = employeeManager.getEmployeeByFirstNameViaLastName(dto.getManager().getName());
            } else if (dto.getManager().getCode() != null) {
                manager = employeeManager.getEmployeeByNumber(dto.getManager().getCode());
            }

            if (manager != null) {
                newProject.setManagerId(manager.getObjectID());
            }
        }

        if (dto.getLocation() != null) {
            EdsLocation location = null;
            if (dto.getLocation().getId() != null) {
                location = locationManager.get(dto.getLocation().getId());
            } else if (dto.getLocation().getName() != null) {
                location = locationManager.getLocationByName(dto.getLocation().getName());
            }

            if (location != null) {
                newProject.setLocationId(location.getObjectID());
            }
        }
        newProject.setBillable(dto.isBillable());
        if (dto.getCustomFields() != null) {
            newProject.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Project), null));
        }

        if (dto.getBackupManagers() != null && !dto.getBackupManagers().isEmpty()) {
            ArrayList<Integer> backupManagers = new ArrayList<>();
            for (ItemDto backupManager : dto.getBackupManagers()) {
                EdsEmployee backup = null;
                if (backupManager.getId() != null) {
                    backup = employeeManager.get(backupManager.getId());
                } else if (backupManager.getName() != null) {
                    backup = employeeManager.getEmployeeByFirstNameViaLastName(backupManager.getName());
                } else if (backupManager.getCode() != null) {
                    backup = employeeManager.getEmployeeByNumber(backupManager.getCode());
                }
                if (backup != null) {
                    backupManagers.add(backup.getObjectID());
                }
            }
            newProject.setBackupManagerIDs(backupManagers);
        }
        Integer id = projectService.saveProject(newProject);
        dto.setNumber(newProject.getNumberData().getNumberString());
        dto.setId(id);

        return dto;
    }

    public ProjectDTO clone(final ProjectDTO dto) throws NumberExistingException {
        CloneProjectItem cloneProject = new CloneProjectItem();
        cloneProject.setProjectName(dto.getName());
        cloneProject.setProjectDescription(dto.getDescription());
        cloneProject.setStartDate(DateUtils.resetTime(dto.getStartDate()));
        cloneProject.setDueDate(DateUtils.resetTime(dto.getDueDate()));
        if (dto.getStatus() != null) {
            EdsReference status = null;
            if (dto.getStatus().getId() != null) {
                status = referenceManager.get(dto.getStatus().getId());
            } else if (dto.getStatus().getCode() != null) {
                status = referenceManager.getByCode(dto.getStatus().getCode());
            } else if (dto.getStatus().getName() != null) {
                status = referenceManager.getByName(dto.getStatus().getName());
            }
            if (status != null) {
                cloneProject.setStatusId(status.getObjectID());
            }
        }

        if (dto.getCustomer() != null) {
            EdsCrmAccount customer = null;
            if (dto.getCustomer().getId() != null) {
                customer = accountManager.get(dto.getCustomer().getId());
            } else if (dto.getCustomer().getName() != null) {
                customer = accountManager.getCrmAccountByName(dto.getCustomer().getName());
            } else if (dto.getCustomer().getCode() != null) {
                customer = accountManager.getCrmAccountByNumber(dto.getCustomer().getCode());
            }
            if (customer != null) {
                cloneProject.setClientId(customer.getObjectID());
            }
        }

        if (dto.getCopyExisting() != null) {
            CloneProjectDto cloneProjectDto = dto.getCopyExisting();
            if (cloneProjectDto.getProject() != null) {
                EdsProject project = null;
                if (cloneProjectDto.getProject().getId() != null) {
                    project = projectManager.get(cloneProjectDto.getProject().getId());
                } else if (cloneProjectDto.getProject().getName() != null) {
                    List<EdsProject> projects = projectManager.getProjectByName(cloneProjectDto.getProject().getName());
                    if (projects != null && !projects.isEmpty()) {
                        project = projects.get(0);
                    }
                } else if (cloneProjectDto.getProject().getCode() != null) {
                    project = projectManager.getProjectByNumber(cloneProjectDto.getProject().getCode());
                }
                if (project != null) {
                    cloneProject.setProjectId(project.getObjectID());
                    cloneProject.setProjectSource(PROJECT_SOURCE_COPY_FROM_PROJECT + project.getObjectID());
                }
            }
            cloneProject.setCopyAssignmentsToAllProjectMembers(cloneProjectDto.isCopyProjectAssignments());
            cloneProject.setCopyWorkstream(cloneProjectDto.isCopyWorkStream());
            cloneProject.setCopyClient(cloneProjectDto.isCopyClient());
            cloneProject.setCopyProjectLocation(cloneProjectDto.isCopyLocation());
            cloneProject.setCopyAssignments(cloneProjectDto.isCopyProjectAssignments());
            cloneProject.setCopyTasks(cloneProjectDto.isCopyTasks());

            CloneTaskItem cloneTaskItem = new CloneTaskItem();
            cloneTaskItem.setAdjustByProjectStartDate(cloneProjectDto.isCopyTaskDates());
            cloneTaskItem.setCopyTaskAssignments(cloneProjectDto.isCopyTaskAssignments());
            if (cloneProjectDto.isResetTaskStatuses()) {
                EdsReference status = null;
                if (cloneProjectDto.getStatus().getId() != null) {
                    status = referenceManager.get(cloneProjectDto.getStatus().getId());
                } else if (cloneProjectDto.getStatus().getName() != null) {
                    status = referenceManager.getByName(cloneProjectDto.getStatus().getName());
                } else if (cloneProjectDto.getStatus().getCode() != null) {
                    status = referenceManager.getByCode(cloneProjectDto.getStatus().getCode());
                }
                if (status != null) {
                    cloneTaskItem.setStatus(status.getObjectID());
                }
            }
            cloneProject.setTaskItem(cloneTaskItem);
        }
        ArrayList<ProjectMember> members = new ArrayList<>();
        for (ProjectEmployeeDTO info : dto.getEmployees()) {
            ProjectMember member = new ProjectMember();
            EdsEmployee employee = null;
            if (info.getEmployee().getId() != null) {
                employee = employeeManager.get(info.getEmployee().getId());
            } else if (info.getEmployee().getName() != null) {
                employee = employeeManager.getEmployeeByFirstNameViaLastName(info.getEmployee().getName());
            } else if (info.getEmployee().getCode() != null) {
                employee = employeeManager.getEmployeeByNumber(info.getEmployee().getCode());
            }
            if (employee != null) {
                member.setId(employee.getObjectID());
                member.setName(employee.getFullName());
                member.setCheck(true);
                member.setWageRate(info.getWageRate());
                member.setClientChargeRate(info.getCustomerChargeRate());
                member.setWorkloadPercentage(info.getWorkload());
                members.add(member);
            }
        }
        cloneProject.setMembers(members.toArray(new ProjectMember[0]));

        if (dto.getManager() != null) {
            EdsEmployee manager = null;
            if (dto.getManager().getId() != null) {
                manager = employeeManager.get(dto.getManager().getId());
            } else if (dto.getManager().getName() != null) {
                manager = employeeManager.getEmployeeByFirstNameViaLastName(dto.getManager().getName());
            } else if (dto.getManager().getCode() != null) {
                manager = employeeManager.getEmployeeByNumber(dto.getManager().getCode());
            }

            if (manager != null) {
                cloneProject.setManager(manager.getObjectID());
            }
        }

        if (dto.getLocation() != null) {
            EdsLocation location = null;
            if (dto.getLocation().getId() != null) {
                location = locationManager.get(dto.getLocation().getId());
            } else if (dto.getLocation().getName() != null) {
                location = locationManager.getLocationByName(dto.getLocation().getName());
            }

            if (location != null) {
                cloneProject.setLocationId(location.getObjectID());
            }
        }

        cloneProject.setBillable(dto.isBillable());
        if (dto.getCustomFields() != null) {
            cloneProject.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Project), null));
        }

        if (dto.getBackupManagers() != null && !dto.getBackupManagers().isEmpty()) {
            ArrayList<Integer> backupManagers = new ArrayList<>();
            for (ItemDto backupManager : dto.getBackupManagers()) {
                EdsEmployee backup = null;
                if (backupManager.getId() != null) {
                    backup = employeeManager.get(backupManager.getId());
                } else if (backupManager.getName() != null) {
                    backup = employeeManager.getEmployeeByFirstNameViaLastName(backupManager.getName());
                } else if (backupManager.getCode() != null) {
                    backup = employeeManager.getEmployeeByNumber(backupManager.getCode());
                }
                if (backup != null) {
                    backupManagers.add(backup.getObjectID());
                }
            }
            cloneProject.setBackupManagerIDs(backupManagers);
        }

        Integer id = projectService.saveCloneProject(cloneProject);
        dto.setNumber(cloneProject.getNumberData().getNumberString());
        dto.setId(id);
        return dto;
    }

    @Transactional
    public ProjectDTO update(ProjectDTO dto) throws NumberExistingException, RestException {
        EdsProject edsProject = null;
        if (dto.getId() != null) {
            edsProject = projectManager.get(dto.getId());
        } else if (dto.getNumber() != null) {
            edsProject = projectManager.getProjectByNumber(dto.getNumber());
            if (edsProject != null) {
                dto.setId(edsProject.getObjectID());
            }
        }
        if (edsProject == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Project with this id is not found", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        EditProject editProject = projectService.getProjectForEdit(dto.getId(), null, null);
        editProject.setNumber(dto.getNumber());
        editProject.setName(dto.getName());
        editProject.setDescription(dto.getDescription());
        editProject.setStartDate(DateUtils.resetTime(dto.getStartDate()));
        editProject.setDueDate(DateUtils.resetTime(dto.getDueDate()));
        editProject.setBillable(dto.isBillable());
        if (dto.getAttachments() != null) {
            FileItem[] fileItems = requestToFileItem(dto);
            editProject.setAttachments(fileItems);
        }
        if (dto.getNotes() != null) {
            List<HistoryListItem> historyListItems = dto.getNotes().stream().map(e -> new HistoryListItem(e.getText())).collect(Collectors.toList());
            editProject.setNotes(historyListItems);
        }

        if (dto.getCustomer() != null) {
            EdsCrmAccount customer = null;
            if (dto.getCustomer().getId() != null) {
                customer = accountManager.get(dto.getCustomer().getId());
            } else if (dto.getCustomer().getName() != null) {
                customer = accountManager.getCrmAccountByName(dto.getCustomer().getName());
            } else if (dto.getCustomer().getCode() != null) {
                customer = accountManager.getCrmAccountByNumber(dto.getCustomer().getCode());
            }
            if (customer != null) {
                editProject.setClientId(customer.getObjectID());
            }
        }

        ArrayList<CalendarEventReminder> eventReminders = new ArrayList<>();
        if (dto.getDueDateReminder() != null) {
            for (ReminderDto reminder : dto.getDueDateReminder()) {
                if (reminder != null && reminder.getTimes() != null && reminder.getTimes() > 0) {
                    CalendarEventReminder cer = new CalendarEventReminder();
                    cer.setValue(reminder.getType());

                    cer.setReminderTimes(reminder.getTimes());
                    eventReminders.add(cer);
                }
            }
        }
        if (!eventReminders.isEmpty()) {
            editProject.setReminders(eventReminders);
        }

        ArrayList<ProjectMember> members = new ArrayList<>();
        for (ProjectEmployeeDTO info : dto.getEmployees()) {
            ProjectMember member = new ProjectMember();
            EdsEmployee employee = null;
            if (info.getEmployee().getId() != null) {
                employee = employeeManager.get(info.getEmployee().getId());
            } else if (info.getEmployee().getName() != null) {
                employee = employeeManager.getEmployeeByFirstNameViaLastName(info.getEmployee().getName());
            } else if (info.getEmployee().getCode() != null) {
                employee = employeeManager.getEmployeeByNumber(info.getEmployee().getCode());
            }
            if (employee != null) {
                member.setId(employee.getObjectID());
                member.setName(employee.getFullName());
                member.setCheck(true);
                member.setWageRate(info.getWageRate());
                member.setClientChargeRate(info.getCustomerChargeRate());
                member.setWorkloadPercentage(info.getWorkload());
                member.setProjectEmployeeId(info.getProjectEmployeeId());
                members.add(member);
            }
        }
        editProject.setMembers(members.toArray(new ProjectMember[0]));

        if (dto.getManager() != null) {
            EdsEmployee manager = null;
            if (dto.getManager().getId() != null) {
                manager = employeeManager.get(dto.getManager().getId());
            } else if (dto.getManager().getName() != null) {
                manager = employeeManager.getEmployeeByFirstNameViaLastName(dto.getManager().getName());
            } else if (dto.getManager().getCode() != null) {
                manager = employeeManager.getEmployeeByNumber(dto.getManager().getCode());
            }

            if (manager != null) {
                editProject.setManagerId(manager.getObjectID());
            }
        }

        if (dto.getLocation() != null) {
            EdsLocation location = null;
            if (dto.getLocation().getId() != null) {
                location = locationManager.get(dto.getLocation().getId());
            } else if (dto.getLocation().getName() != null) {
                location = locationManager.getLocationByName(dto.getLocation().getName());
            }

            if (location != null) {
                editProject.setLocationId(location.getObjectID());
            }
        }

        if (dto.getCustomFields() != null) {
            editProject.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Project), null));
        }

        if (dto.getBackupManagers() != null && !dto.getBackupManagers().isEmpty()) {
            ArrayList<Integer> backupManagers = new ArrayList<>();
            for (ItemDto backupManager : dto.getBackupManagers()) {
                EdsEmployee backup = null;
                if (backupManager.getId() != null) {
                    backup = employeeManager.get(backupManager.getId());
                } else if (backupManager.getName() != null) {
                    backup = employeeManager.getEmployeeByFirstNameViaLastName(backupManager.getName());
                } else if (backupManager.getCode() != null) {
                    backup = employeeManager.getEmployeeByNumber(backupManager.getCode());
                }
                if (backup != null) {
                    backupManagers.add(backup.getObjectID());
                }
            }
            editProject.setBackupManagerIDs(backupManagers);
        }

        projectService.updateProject(editProject);
        return dto;
    }

    private static FileItem[] requestToFileItem(ProjectDTO dto) {
        FileItem[] fileItems = new FileItem[dto.getAttachments().size()];
        for (int i = 0; i < dto.getAttachments().size(); i++) {
            AttachmentTO attachment = dto.getAttachments().get(i);
            FileItem fileItem = new FileItem();
            fileItem.setFileName(attachment.getFile_name());
            fileItems[i] = fileItem;
        }
        return fileItems;
    }

    public ProjectDTO savePatch(final ProjectDTO dto) throws RestException, NumberExistingException {
        EdsProject edsProject = null;
        if (dto.getId() != null) {
            edsProject = projectManager.get(dto.getId());
        } else if (dto.getNumber() != null) {
            edsProject = projectManager.getProjectByNumber(dto.getNumber());
            if (edsProject != null) {
                dto.setId(edsProject.getObjectID());
            }
        }
        if (edsProject == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Project with this id is not found", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        EditProject editProject = projectService.getProjectForEdit(dto.getId(), null, null);
        Optional.ofNullable(dto.getNumber()).ifPresent(editProject::setNumber);
        Optional.ofNullable(dto.getName()).ifPresent(editProject::setName);
        Optional.ofNullable(dto.getDescription()).ifPresent(editProject::setDescription);
        Optional.ofNullable(dto.getStartDate()).ifPresent(s -> editProject.setStartDate(DateUtils.resetTime(dto.getStartDate())));
        Optional.ofNullable(dto.getDueDate()).ifPresent(s -> editProject.setDueDate(DateUtils.resetTime(dto.getDueDate())));

        if (editProject.getEndDate().before(editProject.getStartDate())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "End Date cannot be before Start Date", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        Optional.of(dto.isBillable()).ifPresent(editProject::setBillable);
        Optional.ofNullable(dto.getCustomer()).ifPresent(c -> {
            EdsCrmAccount customer = null;
            if (dto.getCustomer().getId() != null) {
                customer = accountManager.get(dto.getCustomer().getId());
            } else if (dto.getCustomer().getName() != null) {
                customer = accountManager.getCrmAccountByName(dto.getCustomer().getName());
            } else if (dto.getCustomer().getCode() != null) {
                customer = accountManager.getCrmAccountByNumber(dto.getCustomer().getCode());
            }
            if (customer != null) {
                editProject.setClientId(customer.getObjectID());
            }
        });

        Optional.ofNullable(dto.getDueDateReminder()).ifPresent(d -> {
            ArrayList<CalendarEventReminder> eventReminders = new ArrayList<>();
            for (ReminderDto reminder : dto.getDueDateReminder()) {
                if (reminder != null && reminder.getTimes() != null && reminder.getTimes() > 0) {
                    CalendarEventReminder cer = new CalendarEventReminder();
                    cer.setValue(reminder.getType());

                    cer.setReminderTimes(reminder.getTimes());
                    eventReminders.add(cer);
                }
            }
            if (!eventReminders.isEmpty()) {
                editProject.setReminders(eventReminders);
            }
        });

        Optional.ofNullable(dto.getEmployees()).ifPresent(e -> {
            ArrayList<ProjectMember> members = new ArrayList<>();
            for (ProjectEmployeeDTO info : dto.getEmployees()) {
                ProjectMember member = new ProjectMember();
                EdsEmployee employee = null;
                if (info.getEmployee().getId() != null) {
                    employee = employeeManager.get(info.getEmployee().getId());
                } else if (info.getEmployee().getName() != null) {
                    employee = employeeManager.getEmployeeByFirstNameViaLastName(info.getEmployee().getName());
                } else if (info.getEmployee().getCode() != null) {
                    employee = employeeManager.getEmployeeByNumber(info.getEmployee().getCode());
                }
                if (employee != null) {
                    member.setId(employee.getObjectID());
                    member.setName(employee.getFullName());
                    member.setCheck(true);
                    member.setWageRate(info.getWageRate());
                    member.setClientChargeRate(info.getCustomerChargeRate());
                    member.setWorkloadPercentage(info.getWorkload());
                    member.setProjectEmployeeId(info.getProjectEmployeeId());
                    members.add(member);
                }
            }
            editProject.setMembers(members.toArray(new ProjectMember[0]));
        });

        Optional.ofNullable(dto.getManager()).ifPresent(m -> {
            EdsEmployee manager = null;
            if (dto.getManager().getId() != null) {
                manager = employeeManager.get(dto.getManager().getId());
            } else if (dto.getManager().getName() != null) {
                manager = employeeManager.getEmployeeByFirstNameViaLastName(dto.getManager().getName());
            } else if (dto.getManager().getCode() != null) {
                manager = employeeManager.getEmployeeByNumber(dto.getManager().getCode());
            }

            if (manager != null) {
                editProject.setManagerId(manager.getObjectID());
            }
        });

        Optional.ofNullable(dto.getLocation()).ifPresent(l -> {
            EdsLocation location = null;
            if (dto.getLocation().getId() != null) {
                location = locationManager.get(dto.getLocation().getId());
            } else if (dto.getLocation().getName() != null) {
                location = locationManager.getLocationByName(dto.getLocation().getName());
            }

            if (location != null) {
                editProject.setLocationId(location.getObjectID());
            }
        });

        Optional.ofNullable(dto.getBackupManagers()).ifPresent(b -> {
            if (dto.getBackupManagers() != null && !dto.getBackupManagers().isEmpty()) {
                ArrayList<Integer> backupManagers = new ArrayList<>();
                for (ItemDto backupManager : dto.getBackupManagers()) {
                    EdsEmployee backup = null;
                    if (backupManager.getId() != null) {
                        backup = employeeManager.get(backupManager.getId());
                    } else if (backupManager.getName() != null) {
                        backup = employeeManager.getEmployeeByFirstNameViaLastName(backupManager.getName());
                    } else if (backupManager.getCode() != null) {
                        backup = employeeManager.getEmployeeByNumber(backupManager.getCode());
                    }
                    if (backup != null) {
                        backupManagers.add(backup.getObjectID());
                    }
                }
                editProject.setBackupManagerIDs(backupManagers);
            }
        });

        if (dto.getStatus() != null) {
            if (dto.getStatus().getId() != null) {
                Optional.ofNullable(referenceManager.get(dto.getStatus().getId()))
                        .map(EdsReference::getObjectID)
                        .ifPresent(editProject::setStatusId);
            } else if (dto.getStatus().getCode() != null) {
                Optional.ofNullable(referenceManager.getByCode(dto.getStatus().getCode()))
                        .map(EdsReference::getObjectID)
                        .ifPresent(editProject::setStatusId);
            } else if (dto.getStatus().getName() != null) {
                Optional.ofNullable(referenceManager.getByName(dto.getStatus().getName()))
                        .map(EdsReference::getObjectID)
                        .ifPresent(editProject::setStatusId);
            }
        }

        projectService.updateProject(editProject);
        return dto;
    }

    public List<ProjectEmployeeListDTO> getProjectEmployees(Integer projectId, String employeeName) {
        List<EdsProjectEmployee> employeesByProject = projectManager.getEmployeesByProjectAndEmployee(projectId, employeeName);
        Map<String, List<EmployeeTO>> departmentMap = new HashMap<>();
        for (EdsProjectEmployee edsProjectEmployee : employeesByProject) {
            EdsDepartment employeeDepartment = edsProjectEmployee.getEmployeeDepartment().getTeam();
            List<EmployeeTO> list = departmentMap.getOrDefault(employeeDepartment.getName(), new ArrayList<>());
            list.add(toEmployeeTo(edsProjectEmployee));
            departmentMap.put(employeeDepartment.getName(), list);
        }
        List<ProjectEmployeeListDTO> response = new ArrayList<>();
        for (String key : departmentMap.keySet()) {
            ProjectEmployeeListDTO dto = new ProjectEmployeeListDTO();
            dto.setDepartmentName(key);
            dto.setEmployees(departmentMap.get(key));
            response.add(dto);
        }
        return response;
    }

    private EmployeeTO toEmployeeTo(EdsProjectEmployee edsProjectEmployee) {
        EmployeeTO employeeTO = new EmployeeTO();
        employeeTO.setId(edsProjectEmployee.getEmployeeDepartment().getEmployee().getObjectID());
        employeeTO.setName(edsProjectEmployee.getName());
        return employeeTO;
    }

    @Transactional
    public void saveProjectCustomField(Integer itemId, String alias, FileResource fileResource) {
        ArrayList<CompanyCustomFieldItem> customFieldsValue = getCustomFieldsValue(companyCustomFieldsManager.getByAliasName(ViewName.Project.name(), alias), toFileItem(fileResource));
        EdsProject edsTask = projectManager.get(itemId);
        EdsProjectCustomFields taskCustomFields = createTaskCustomFields(edsTask.getProjectCustomFields(), customFieldsValue);
        edsTask.setProjectCustomFields(taskCustomFields);
        projectManager.update(edsTask);
    }

    private static FileItem[] toFileItem(FileResource fileResource) {
        List<FileResource> fileResources = List.of(fileResource);
        if (fileResources == null || fileResources.isEmpty()) {
            return null;
        }
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

    @Transactional
    public EdsProjectCustomFields createTaskCustomFields(EdsProjectCustomFields edsProjectCustomField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems == null || customFieldItems.size() == 0) {
            return null;
        }
        if (edsProjectCustomField == null) {
            boolean isEmpty = customFieldItems.stream().noneMatch(fieldItem -> (fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                    || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                    || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0));
            if (isEmpty) {
                return null;
            }
            edsProjectCustomField = new EdsProjectCustomFields();
            projectCFMananger.create(edsProjectCustomField);
        }
        CustomFieldsUtils.setDomenObjectCustomFields(edsProjectCustomField, customFieldItems);
        return edsProjectCustomField;
    }
}
