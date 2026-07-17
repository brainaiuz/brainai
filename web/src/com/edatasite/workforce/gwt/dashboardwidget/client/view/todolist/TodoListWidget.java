package com.edatasite.workforce.gwt.dashboardwidget.client.view.todolist;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectEmployeesLookUp;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoList;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoListItem;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoListRpc;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 27.04.2018 23:05
 */
public class TodoListWidget extends Composite implements Constants {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TodoListContentWidget content;
    private Div contentPanel;
    private ProjectEmployeesLookUp employeeListBox;
    private DataListBox priorityListBox = new DataListBox();
    private TodoList todoList;
    private SaveTodoTaskItem saveTodoTaskItem;
    private boolean enableToShowSample;
    private boolean isSampleData;
    private Command showEmptyMessageHandle;
    private Command hideEmptyMessageHandle;

    public TodoListWidget(ProjectEmployeesLookUp employeeListBox,
                          SelectItem[] priorities,
                          boolean enableToShowSample,
                          boolean isSampleData) {

        this.employeeListBox = employeeListBox;
        this.enableToShowSample = enableToShowSample;
        this.isSampleData = isSampleData;
        if (priorities != null && priorities.length > 0) {
            setPriorityData(priorities);
        } else {
            getPriorityData();
        }
        drawPanel();
    }

    private void drawPanel() {
        contentPanel = new Div();
        contentPanel.setStyleName("gwt-wrapper");

        contentPanel.add(drawHeader());

        content = new TodoListContentWidget(this);
        contentPanel.add(content);

        if (isSampleData) {
            List<TodoListItem> items = new ArrayList<>();
            TodoListItem item = new TodoListItem();
            item.setTaskName("Fill out profile -Provide more information for your profile");
            item.setPriorityName("Medium");
            item.setPriorityCode(TodoListItem.MEDIUM);
            item.setReporterName("John Smith");
            items.add(item);

            item = new TodoListItem();
            item.setTaskName("Invite colleagues to your kpi.com");
            item.setPriorityName("High");
            item.setPriorityCode(TodoListItem.HIGH);
            item.setReporterName("John Smith");
            items.add(item);

            item = new TodoListItem();
            item.setTaskName("Download a copy of the Users Manual for product (https://www.kpi.com/presentations/)");
            item.setPriorityName("Low");
            item.setPriorityCode(TodoListItem.LOW);
            item.setReporterName("John Smith");
            items.add(item);

            TodoList todo = new TodoList();
            todo.setTodoListItems(items);
            content.refreshTable(todo);
        } else {
            refreshList(employeeListBox.getSelectedItemID() != null
                        ? employeeListBox.getSelectedItemID()
                        : Utils.getUserID());
        }
        initWidget(contentPanel);
    }

    private Div drawHeader() {
        Div headerPanel = new Div();
        headerPanel.addStyleName("widget-row widget-finder");

        TextBox nameBox = new TextBox();
        nameBox.setStyleName("form-control");
        nameBox.setPlaceHolder(wfmStrings.makeItHappen());
        if (!enableToShowSample) {
            nameBox.addKeyPressHandler(keyPressEvent -> {
                if (keyPressEvent.getNativeEvent().getKeyCode() == (char) KeyCodes.KEY_ENTER) {
                    if (nameBox.getText() != null && !"".equals(nameBox.getText().trim()) && priorityListBox.getSelectedId() != null) {
                        addNewItem(nameBox);
                    }
                }
            });
            nameBox.addClickHandler(clickEvent -> nameBox.removeStyleName(Constants.ERROR_FORM_STYLE));
        }
        Div nameDiv = new Div("widget-finder-search");
        nameDiv.add(nameBox);
        headerPanel.add(nameDiv);

        Div priorityDiv = new Div("widget-row__end");
        priorityListBox.addStyleName("form-control listBox-small listBox-cat todo-cat--2");
        priorityListBox.addValueChangeHandler(event -> {
            if (priorityListBox.getSelectedItem() != null) {
                if (TodoListItem.HIGH.equals(priorityListBox.getSelectedItem().getDescription())) {
                    priorityListBox.setStyleName("form-control listBox-small listBox-cat todo-cat--3 listbox-wrapper");
                } else if (TodoListItem.MEDIUM.equals(priorityListBox.getSelectedItem().getDescription())) {
                    priorityListBox.setStyleName("form-control listBox-small listBox-cat todo-cat--2 listbox-wrapper");
                } else if (TodoListItem.LOW.equals(priorityListBox.getSelectedItem().getDescription())) {
                    priorityListBox.setStyleName("form-control listBox-small listBox-cat todo-cat--1 listbox-wrapper");
                }
            }
        });
        priorityDiv.add(priorityListBox);
        headerPanel.add(priorityDiv);

        return headerPanel;
    }

    private void addNewItem(TextBox nameBox) {
        TodoListItem listItem = new TodoListItem();

        Integer employeeId = employeeListBox.getSelectedItemID() != null ? employeeListBox.getSelectedItemID() : Utils.getUserID();
        Integer projectEmployeeId = null;
        if (employeeListBox.getSelectedItem() != null && employeeListBox.getSelectedItem().getDescription() != null) {
            projectEmployeeId = Integer.valueOf(employeeListBox.getSelectedItem().getDescription());
        }
        Integer priorityId = priorityListBox.getSelectedId();
        String priorityName = "";
        if (priorityListBox.getSelectedItem() != null) {
            priorityName = priorityListBox.getSelectedItem().getName();
            listItem.setPriorityCode(priorityListBox.getSelectedItem().getDescription());
        }
        saveTodoListItem(listItem, nameBox.getText(), employeeId, projectEmployeeId, priorityId, priorityName, true);
        nameBox.setText("");
    }

    public ProjectEmployeesLookUp getEmployeeListBox() {
        return employeeListBox;
    }

    private void refreshList(Integer employeeId) {
        DashboardWidgetService.App.get().getTodoLists(employeeId, new AbstractAsyncCallback<TodoList>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(TodoList todo) {
                todoList = todo;
                if (todoList.getTodoListItems() != null && todoList.getTodoListItems().size() > 0) {
                    hideEmptyMessageHandle.execute();
                } else {
                    showEmptyMessageHandle.execute();
                }
                content.refreshTable(todoList);
            }
        });
    }

    public void saveTodoListNumbering(HashMap<Integer, Integer> taskIdsWithOrdering) {
        DashboardWidgetService.App.get().saveTodoListOrdering(taskIdsWithOrdering, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(Void aVoid) {
            }
        });
    }

    public void saveTodoListItem(TodoListItem listItem,
                                 String todoName,
                                 Integer employeeId,
                                 Integer projectEmployeeId,
                                 Integer priorityId,
                                 String priorityName,
                                 boolean isNew) {

        final MultiTaskList savedTodoList = new MultiTaskList();
        savedTodoList.setCreatedFrom(MultiTaskList.FROM_TODO_LIST);

        final TaskSingleItem[] todoListItem = new TaskSingleItem[1];
        todoListItem[0] = new TaskSingleItem();
        todoListItem[0].setName(todoName);//task name
        todoListItem[0].setObjectID(listItem.getTaskID());
        todoListItem[0].setStatusID(listItem.getStatusID());
        if (projectEmployeeId != null) {
            IdTime[] employees = new IdTime[1];
            employees[0] = new IdTime();
            employees[0].setId(projectEmployeeId);
            todoListItem[0].setProjectEmployees(employees);
        }
        todoListItem[0].setStartDate(DateUtil.resetTime(new Date()));
        todoListItem[0].setDueDate(DateUtil.getDayLastTime(new Date()));
        todoListItem[0].setPriorityID(priorityId);
        todoListItem[0].setAllDay(true);
        todoListItem[0].setBillable(true);
        todoListItem[0].setEmployeeID(employeeId);
        todoListItem[0].setShowInTimesheet(false);
        if (todoList != null && todoList.getProjectID() != null) {
            todoListItem[0].setProjectID(todoList.getProjectID());
            savedTodoList.setProjectID(todoList.getProjectID());
        } else if (employeeListBox.getProjectId() != null) {
            todoListItem[0].setProjectID(employeeListBox.getProjectId());
            savedTodoList.setProjectID(employeeListBox.getProjectId());
        }
        savedTodoList.setTaskSingleItems(todoListItem);
        if (isNew) {
            listItem.setTaskName(todoName);
            listItem.setPriorityName(priorityName);
            hideEmptyMessageHandle.execute();
            TodoListContentWidget.RowTodo rowTodo = content.addNewRow(listItem);
            DashboardWidgetService.App.get().saveMultipleTask(savedTodoList, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    rowTodo.setStyleName("widget-row widget-row--unsaved");
                }

                @Override
                public void success(Integer taskId) {
                    rowTodo.setTaskId(taskId);
                    saveTodoListNumbering(content.getOrderingRows());
                }
            });
        } else {
            DashboardWidgetService.App.get().saveMultipleTask(savedTodoList, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(Integer integer) {
                }
            });
        }
    }

    private void getPriorityData() {
        DashboardWidgetService.App.get().getTodoListRpc(new AbstractAsyncCallback<TodoListRpc>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(TodoListRpc result) {
                setPriorityData(result.getPriorities());
            }
        });
    }

    private void setPriorityData(SelectItem[] priorities) {
        if (priorities != null && priorities.length > 0) {
            priorityListBox.setWithoutNullLabel(true);
            for (SelectItem priority : priorities) {
                if (TodoListItem.HIGH.equals(priority.getDescription())) {
                    priority.setStyleName("todo-cat--3");
                } else if (TodoListItem.MEDIUM.equals(priority.getDescription())) {
                    priority.setStyleName("todo-cat--2");
                } else if (TodoListItem.LOW.equals(priority.getDescription())) {
                    priority.setStyleName("todo-cat--1");
                } else {
                    priority.setStyleName("todo-cat--0");
                }
            }
            priorityListBox.setItems(priorities);
            priorityListBox.setSelectedByCode(TodoListItem.MEDIUM);
        }
    }

    public void returnTodoTaskID(SaveTodoTaskItem saveTodoTaskItem) {
        this.saveTodoTaskItem = saveTodoTaskItem;
    }

    public interface SaveTodoTaskItem {
        void getSavedTaskID(Integer taskID);
    }

    public void setShowEmptyMessageHandle(Command showEmptyMessageHandle) {
        this.showEmptyMessageHandle = showEmptyMessageHandle;
        this.content.setShowEmptyMessageHandle(showEmptyMessageHandle);
    }

    public void setHideEmptyMessageHandle(Command hideEmptyMessageHandle) {
        this.hideEmptyMessageHandle = hideEmptyMessageHandle;
    }

    public Div getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(Div contentPanel) {
        this.contentPanel = contentPanel;
    }
}
