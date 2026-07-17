package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.ProjectIndexRbacManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Ilhombek
 * Date: 19.08.2010
 * Time: 17:26:09
 */
@Transactional
public class ProjectCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsProject> TYPE = new WfmType<>(EventTypes.projectCustomEventListener);
    public static String EVENT_PROJECT_ADD_TO_SOLR = "EVENT_PROJECT_ADD_TO_SOLR";
    public static String EVENT_CRMACTIVITY_PROJECT_EMPLOYEE_ADD = "CRMACTIVITY_PROJECT_EMPLOYEE_ADD";
    public static String EVENT_CRMACTIVITY_PROJECT_EMPLOYEE_REMOVE = "CRMACTIVITY_PROJECT_EMPLOYEE_REMOVE";

    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ProjectIndexRbacManager projectIndexRbacManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_PROJECT_ADD_TO_SOLR.equals(event.getEventType())) {
            onAdd(event);
        }
        if (EVENT_CRMACTIVITY_PROJECT_EMPLOYEE_ADD.equals(event.getEventType()) || EVENT_CRMACTIVITY_PROJECT_EMPLOYEE_REMOVE.equals(event.getEventType())) {
            addOrRemoveEmployeeFromProject(event, EVENT_CRMACTIVITY_PROJECT_EMPLOYEE_ADD.equals(event.getEventType()));
        }
    }

    private void addOrRemoveEmployeeFromProject(EdsBusinessEvent event, boolean add) {
        EdsProject project = projectManager.get(event.getEntityID());
        if (project != null && (project.getDeleted() == null || !project.getDeleted()) && event.getCustomStringField() != null && event.getCustomStringField().matches(Constants.REGEX_INTEGER)) {
            Integer employeeID = Integer.valueOf(event.getCustomStringField());
            EdsEmployee employee = employeeManager.get(employeeID);
            if (employee != null && !employee.getDeleted()) {
                EditProject projectRPC = projectService.getProjectForEdit(project.getObjectID(), project.getStartDate(), project.getClient().getObjectID());
                ArrayList<ProjectMember> members = new ArrayList<>(Arrays.asList(employeeService.getProjectEmployees(project.getObjectID())));
                boolean containsInMembers = isExistingMember(members, employee.getObjectID());
                List<ProjectMember> newProjectMembers = new ArrayList<>();
                boolean changed = false;
                for (ProjectMember member : members) {
                    if (!add && containsInMembers && !employeeID.equals(member.getId())) {
                        newProjectMembers.add(member);
                    } else {
                        changed = true;
                    }
                }
                if (add && !containsInMembers) {
                    ProjectMember pm = employeeService.getProjectMemberByEmployee(employee.getObjectID());
                    if (pm != null) {
                        newProjectMembers.add(pm);
                        changed = true;
                    }
                }
                if (changed) {
                    projectRPC.setMembers(newProjectMembers.toArray(new ProjectMember[]{}));
                    try {
                        projectService.updateProject(projectRPC);
                    } catch (NumberExistingException e) {
                        e.printStackTrace();
                    }
                }
            }
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    private boolean isExistingMember(ArrayList<ProjectMember> members, Integer objectID) {
        if (members != null && members.size() > 0) {
            if (objectID != null) {
                for (ProjectMember member : members) {
                    if (objectID.equals(member.getId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void onAdd(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        if (project!=null) {
            projectIndexRbacManager.indexProject(project);
            if (!event.isSolrIndexed()) {
                try {
                    projectSolrComponent.index(project);
                    event.setSolrIndexed(true);
                } catch (Exception e) {
                    event.setSolrIndexed(false);
                }
            }
            if (event.isSolrIndexed()) {
                event.setStatus(EventStatus.COMPLETED.name());
            }
        } else {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
