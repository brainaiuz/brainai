package com.edatasite.workforce.gwt.gettingstarted.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GettingStartedServiceAsync {

    void createDepartment(NewDepartment department, AsyncCallback<Void> callback);

    void getLastEmployees(AsyncCallback<SelectItem[]> callback);

    void getLastDepartments(AsyncCallback<SelectItem[]> callback);

    void getLastProjects(AsyncCallback<SelectItem[]> callback);

    void getProjects(AsyncCallback<ProjectItem[]> callback);

    void getPriorities(AsyncCallback<SelectItem[]> callback);

    void getAssigneesWithPositions(Integer projectId, AsyncCallback<PositionsSelectItem[]> callback);

    void createProject(ProjectSingleItem item, AsyncCallback<Void> callback);

    void getEmpMaxCount(AsyncCallback<Integer[]> callback);

    void activateCompany(AsyncCallback<Void> callback);

    void saveMultiTasks(MultiTaskList multiTaskList, AsyncCallback<Void> callback);

}
