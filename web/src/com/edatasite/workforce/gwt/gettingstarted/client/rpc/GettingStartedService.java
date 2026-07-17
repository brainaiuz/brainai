package com.edatasite.workforce.gwt.gettingstarted.client.rpc;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface GettingStartedService extends RemoteService {

    void createDepartment(NewDepartment department);

    SelectItem[] getLastEmployees();

    SelectItem[] getLastDepartments();

    SelectItem[] getLastProjects();

    ProjectItem[] getProjects();

    SelectItem[] getPriorities();

    PositionsSelectItem[] getAssigneesWithPositions(Integer projectId);

    void createProject(ProjectSingleItem item) throws NumberExistingException;

    Integer[] getEmpMaxCount();

    void activateCompany();

    void saveMultiTasks(MultiTaskList multiTaskList);

    class App {
        public static GettingStartedServiceAsync get() {
            ServiceDefTarget target = GWT.create(GettingStartedService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/gettingstarted");
            return (GettingStartedServiceAsync) target;
        }
    }
}
