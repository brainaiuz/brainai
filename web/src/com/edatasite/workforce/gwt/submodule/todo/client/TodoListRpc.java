package com.edatasite.workforce.gwt.submodule.todo.client;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/19/11
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class TodoListRpc implements IsSerializable {

    private SelectItem currentUser;
    private SelectItem[] priorities;
    private SelectItem mediumPriority;
    private Integer selectedEmployee;
    private Integer projectId;

    public SelectItem getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(SelectItem currentUser) {
        this.currentUser = currentUser;
    }

    public SelectItem[] getPriorities() {
        return priorities;
    }

    public void setPriorities(SelectItem[] priorities) {
        this.priorities = priorities;
    }

    public Integer getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Integer selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public SelectItem getMediumPriority() {
        return mediumPriority;
    }

    public void setMediumPriority(SelectItem mediumPriority) {
        this.mediumPriority = mediumPriority;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
