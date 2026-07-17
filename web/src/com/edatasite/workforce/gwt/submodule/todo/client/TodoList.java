package com.edatasite.workforce.gwt.submodule.todo.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/19/11
 * Time: 8:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class TodoList implements IsSerializable {

    private Integer projectID;
    private List<TodoListItem> todoListItems;
    private String locale;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public List<TodoListItem> getTodoListItems() {
        return todoListItems;
    }

    public void setTodoListItems(List<TodoListItem> todoListItems) {
        this.todoListItems = todoListItems;
    }
}
