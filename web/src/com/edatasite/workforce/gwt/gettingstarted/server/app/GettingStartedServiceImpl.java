package com.edatasite.workforce.gwt.gettingstarted.server.app;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.DepartmentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EmployeeDepartmentEventListenerImpl;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.GettingStartedService;
import com.edatasite.workforce.gwt.gettingstarted.client.rpc.NewDepartment;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("gettingStartedService")
public class GettingStartedServiceImpl implements GettingStartedService {

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private EmployeeService employeeService;

    public void createDepartment(NewDepartment department) {
        EdsDepartment team = new EdsDepartment();
        EdsUser user = employeeManager.getUser();
        EdsEmployee teamLeader = employeeManager.get(department.getLeaderId());
        team.setName(department.getName());
        team.setDescription(department.getDescription());
        team.setCreationTime(user.getCompany().getCompanyDate());
        team.setStartDate(department.getStartDate());
        team.setLeader(teamLeader);
        team.setCreator(user);
        teamLeader.addRole(roleManager.get(Constants.TL));

        departmentManager.create(team);
        baseEventPostProcessor.registerEvent(DepartmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, team, user);
        createDepartmentEmployees(user, department.getMembersId(), team);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getLastEmployees() {
        List<EdsEmployee> employees = employeeManager.getLastEmployees();
        SelectItem[] result = new SelectItem[employees.size()];
        int i = 0;
        for (EdsEmployee employee : employees) {
            result[i] = new SelectItem(employee.getObjectID(), employee.getFirstName() + " " + employee.getLastName());
            i++;
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getLastDepartments() {
        List<EdsDepartment> departments = departmentManager.getLastDepartments();
        SelectItem[] result = new SelectItem[departments.size()];
        int i = 0;
        for (EdsDepartment department : departments) {
            result[i] = new SelectItem(department.getObjectID(), department.getName());
            i++;
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getLastProjects() {
        List<EdsProject> projects = projectManager.getLastProjects();
        SelectItem[] result = new SelectItem[projects.size()];
        int i = 0;
        for (EdsProject project : projects) {
            result[i] = new SelectItem(project.getObjectID(), project.getName());
            i++;
        }
        return result;
    }

    private void createDepartmentEmployees(EdsUser user, Integer[] members, EdsDepartment team) {
        for (Integer employeeId : members) {
            EdsEmployee employee = employeeManager.get(employeeId);
            List<EdsEmployeeDepartment> empDepList = employeeDepartmentManager.getEmployeeDepartmentList(employee);
            for (EdsEmployeeDepartment employeeDepartment : empDepList) {
                employeeDepartmentManager.deleteEmployeeDepartment(employeeDepartment);
                baseEventPostProcessor.registerEvent(EmployeeDepartmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, employeeDepartment, user);
            }
            EdsEmployeeDepartment newEmployeeDepartment = new EdsEmployeeDepartment();
            newEmployeeDepartment.setStartDate(user.getCompany().getCompanyDate());
            newEmployeeDepartment.setTeam(team);
            newEmployeeDepartment.setEmployee(employee);
            employee.setEmployeeTeam(newEmployeeDepartment);
            for (EdsEmployeeDepartment employeeDepartment : empDepList) {
                projectEmployeeManager.deleteAndCreateProjectEmployee(employeeDepartment, employee.getEmployeeTeam());
            }
        }
        for (Integer memberId : members) {
            if (memberId != null) {
                EdsEmployee employee = employeeManager.get(memberId);
                if (employee != null && employee.getEmployeeTeam() != null) {
                    baseEventPostProcessor.registerEvent(EmployeeDepartmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, employee.getEmployeeTeam(), user);
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectItem[] getProjects() {
        EdsUser user = employeeManager.getUser();
        List<EdsProject> projects = projectManager.list();
        ProjectItem[] result = new ProjectItem[projects.size()];
        int i = 0;
        for (EdsProject pr : projects) {
            result[i] = new ProjectItem(pr.getObjectID(), pr.getName());
            result[i].setManager(user.equals(pr.getManager()) || pr.isUserBackupManager(user.getObjectID()));
            i++;
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPriorities() {
        return commonServiceLocal.convertReference2SelectItem(EdsTask.TASK_PRIORITY, false, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PositionsSelectItem[] getAssigneesWithPositions(Integer projectId) {
        return taskService.getAssigneesWithPositions1(projectId);
    }

    public void createProject(ProjectSingleItem item) throws NumberExistingException {
        projectService.saveProject(item);

        EdsUser user = employeeManager.getUser();
        if (!user.getCompany().getIsSetUp()) {
            user.getCompany().setIsSetUp(true);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer[] getEmpMaxCount() {
        return employeeService.getAllEmployeesMaxCount(null, null);
    }

    public void activateCompany() {
        EdsUser user = userManager.getUser();
        if (user.getCompany() != null) {
            user.getCompany().setIsSetUp(true);
        }
    }

    public void saveMultiTasks(MultiTaskList multiTaskList) {
        taskService.saveMultipleTask(multiTaskList);
    }

}
