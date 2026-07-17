package com.edatasite.workforce.gwt.dashboardwidget.client.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectEmployeesLookUp;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.todolist.TodoListWidget;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoListRpc;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

public class DashboardToDoListComponent extends DashboardBaseWidget {

    private ProjectEmployeesLookUp employeeLookUp;
    private Date resetDate = DateUtil.resetTime(new Date());
    private TodoListRpc todoListRpc = new TodoListRpc();

    public DashboardToDoListComponent(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void initInternal() {
        setTitle(wfmStrings.todoList());
        if (!Utils.hasPermission(PermissionConstants.PM_ADD_ASSIGNEES_TO_PROJECT)){
            filterPanel.removeFromParent();
        }
        employeeLookUp = new ProjectEmployeesLookUp();
        employeeLookUp.getSuggestBox().addSelectionHandler(changeEvent -> drawTodoList());
        employeeLookUp.getSuggestBox().addDomHandler(event -> {
            if (employeeLookUp.getSelectedItem() != null) {
                employeeLookUp.clear();
                employeeLookUp.getTextBox().setText("");
                employeeLookUp.getTextBox().getElement().setAttribute("style", "color:#536677 !important");
            }
        }, ClickEvent.getType());

        mainPanel.addStyleName("widget--todo");
    }

    @Override
    protected void getData() {
        LoadingWidgets.get(getCode()).show();
        DashboardWidgetService.App.get().getTodoListRpc(new AsyncCallback<TodoListRpc>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingWidgets.get(getCode()).hide();
            }

            @Override
            public void onSuccess(TodoListRpc result) {
                todoListRpc = result;
                if (result.getCurrentUser() != null) {
                    employeeLookUp.addItem(result.getCurrentUser());
                } else {
                    employeeLookUp.setSelected(Utils.getUserID(), Utils.getUserFullName());
                }
                if (result.getProjectId() != null) {
                    employeeLookUp.setProjectId(result.getProjectId());
                }
                filterPanel.add(employeeLookUp);
                LoadingWidgets.get(getCode()).hide();
                drawTodoList();
            }
        });

    }

    @Override
    protected void getSampleData(boolean nodata) {
        clearPanel();
        contentPanel.clear();
        contentPanel.add(new TodoListWidget(employeeLookUp, todoListRpc.getPriorities(), enableToShowSample, true));
    }

    private void drawTodoList() {
        clearPanel();
        contentPanel.clear();

        TodoListWidget widget = new TodoListWidget(employeeLookUp, todoListRpc.getPriorities(), enableToShowSample, false);
        widget.setShowEmptyMessageHandle(() -> {
            getEmptyMessagePanel().getElement().setInnerText(getEmptyText());
            widget.getContentPanel().add(getEmptyMessagePanel());
        });
        widget.setHideEmptyMessageHandle(() -> hideEmptyMessage());
        contentPanel.add(widget);
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.TODO_LIST;
    }

    @Override
    protected String getEmptyText() {
        return wfmStrings.currentlyYouDontHaveAnyTasks();
    }
}
