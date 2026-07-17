package com.edatasite.workforce.gwt.dashboardwidget.client.view.todolist;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.TextUtils;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoList;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoListItem;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Small;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TodoListContentWidget extends Composite {
    private MaterialPanel contentPanel;
    private SelectItem priority = new SelectItem();
    private Command orderingHandler;
    private Command showEmptyMessageHandle;
    private String locale;

    private final TodoListWidget todoListPanel;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final TextUtils textUtils = TextUtils.get();

    public TodoListContentWidget(TodoListWidget todoListPanel) {
        this.todoListPanel = todoListPanel;
        drawContent();
    }

    public void addOrderingHandler(Command command) {
        this.orderingHandler = command;
    }

    public TodoListWidget getTodoListPanel() {
        return todoListPanel;
    }

    public SelectItem getPriority() {
        return priority;
    }

    public void setPriority(SelectItem mediumPriority) {
        priority = mediumPriority;
    }

    public void refreshTable(TodoList todoList) {
        contentPanel.clear();
        locale = todoList.getLocale();
        if (todoList != null && todoList.getTodoListItems() != null) {
            for (TodoListItem item : todoList.getTodoListItems()) {
                addRow(item);
            }
        }
    }

    private void drawContent() {
        contentPanel = new MaterialPanel();
        contentPanel.setStyleName("widget-content widget-list");
        addOrderingHandler(() -> todoListPanel.saveTodoListNumbering(getOrderingRows()));
        initWidget(contentPanel);
    }

    private void clickStatusImage(TodoListItem listItem, Widget... widgets) {
        final TextBox hiddenBox = (TextBox) widgets[0];
        final Span taskNameText = (Span) widgets[1];

        Integer employeeId = getTodoListPanel().getEmployeeListBox().getSelectedItemID();
        if (employeeId == null) {
            employeeId = Utils.getUserID();
        }
        Integer projectEmployeeId = null;
        if (getTodoListPanel().getEmployeeListBox().getSelectedItem() != null && getTodoListPanel().getEmployeeListBox().getSelectedItem().getDescription() != null) {
            projectEmployeeId = Integer.valueOf(getTodoListPanel().getEmployeeListBox().getSelectedItem().getDescription());
        }

        Integer priorityId = priority.getId();
        //save status task
        saveTodoData(listItem, taskNameText.getText(), employeeId, projectEmployeeId, priorityId);
        todoListPanel.returnTodoTaskID(taskID -> {
            hiddenBox.setName(String.valueOf(taskID));
            runOrderingHandler();
        });
    }

    private TextBox getHiddenBox(int index) {
        return (TextBox) ((Div) ((Div) contentPanel.getWidget(index)).getWidget(0)).getWidget(1);
    }

    private String getHTMLTaskName(int index) {
        Widget widget = ((Div) contentPanel.getWidget(index)).getWidget(1);
        String taskName = "";
        if (widget instanceof Div) {
            taskName = ((Span) ((Div) widget).getWidget(0)).getText();
        } else if (widget instanceof Span) {
            taskName = ((Span) widget).getText();
        }
        return taskName;
    }

    private int getContentRowCount() {
        return contentPanel.getWidgetCount();
    }

    public HashMap<Integer, Integer> getOrderingRows() {
        HashMap<Integer, Integer> taskIdsWithOrdering = new HashMap<>();
        int rowOrder = 1;
        for (int i = 0; i < getContentRowCount(); i++) {
            final TextBox textBox = getHiddenBox(i);
            final String getTaskName = getHTMLTaskName(i);
            String name = textBox.getName();
            if (!(Utils.isNullOrEmpty(getTaskName) && Utils.isNullOrEmpty(name))) {
                try {
                    Integer taskID = Integer.valueOf(name);
                    taskIdsWithOrdering.put(taskID, rowOrder);
                    rowOrder++;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return taskIdsWithOrdering;
    }

    private KpiCheckBox drawCheckBox(boolean isTick, TodoListItem listItem, Div widgetRow, Widget... widgets) {
        final KpiCheckBox checkbox = new KpiCheckBox();
        checkbox.setValue(isTick);
        checkbox.addClickHandler(clickEvent -> {
            if (listItem.getTaskID() == null) {
                checkbox.setValue(!checkbox.getValue());
            } else {
                if (checkbox.getValue()) {
                    widgetRow.addStyleName("widget-row--checked");
                } else {
                    widgetRow.removeStyleName("widget-row--checked");
                }
                if (checkbox.getValue()) {
                    listItem.setStatusID(Constants.COMPLETED);
                } else {
                    listItem.setStatusID(Constants.NOT_STARTED);
                }
                clickStatusImage(listItem, widgets);
            }
        });
        return checkbox;
    }

    public void addRow(final TodoListItem listItem) {
        RowTodo rowTodo = listItem != null ? new RowTodo(listItem) : new RowTodo();

        contentPanel.add(rowTodo);
    }

    public RowTodo addNewRow(final TodoListItem listItem) {
        RowTodo rowTodo = listItem != null ? new RowTodo(listItem) : new RowTodo();

        if (contentPanel.getWidgetCount() > 0) {
            List<Widget> widgets = new ArrayList<>();
            for (int i = 0; i < contentPanel.getWidgetCount(); i++) {
                widgets.add(contentPanel.getWidget(i));
            }
            contentPanel.clear();
            contentPanel.add(rowTodo);
            for (Widget widget : widgets) {
                contentPanel.add(widget);
            }
        } else {
            contentPanel.add(rowTodo);
        }
        return rowTodo;
    }

    private void removeRowTable(Integer taskID) {
        LoadingWidgets.get(Constants.DASHBOARD_WIDGET_CODE.TODO_LIST).show();
        DashboardWidgetService.App.get().deleteTodoTask(taskID, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingWidgets.get(Constants.DASHBOARD_WIDGET_CODE.TODO_LIST).hide();
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void aVoid) {
                LoadingWidgets.get(Constants.DASHBOARD_WIDGET_CODE.TODO_LIST).hide();
                Info.show(Property.get(Constants.TASK, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.task()), Info.Type.INFO);
                if (getContentRowCount() <= 0) {
                    showEmptyMessageHandle.execute();
                }
            }
        });
    }

    private void runOrderingHandler() {
        if (orderingHandler != null) {
            orderingHandler.execute();
        }
    }

    public class RowTodo extends Div implements HasClickHandlers, HasMouseOverHandlers, HasMouseOutHandlers {
        private TextBox taskIdHiddenBox;
        private KpiCheckBox checkBox;
        private Div taskNamePanel;
        private Div rowEndPanel;
        private Span rowTaskName;
        private Small rowTaskAuthor;
        private WfmButton2 removeBtn;
        private TodoListItem todoListItem;

        public RowTodo() {
            draw();
        }

        public RowTodo(TodoListItem listItem) {
            this.todoListItem = listItem;
            draw();
        }

        @Override
        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }

        @Override
        public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
            return addDomHandler(handler, MouseOutEvent.getType());
        }

        @Override
        public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
            return addDomHandler(handler, MouseOverEvent.getType());
        }

        public void setTaskId(Integer taskId) {
            todoListItem.setTaskID(taskId);
            taskIdHiddenBox.setName(String.valueOf(taskId));
        }

        private void draw() {
            String priorityName = null;
            String creatorName = null;
            String taskName = null;

            if (todoListItem == null) {
                todoListItem = new TodoListItem();
            }

            initializeWidgets(this);

            taskName = todoListItem.getTaskName();
            if (todoListItem.getTaskID() != null) {
                taskIdHiddenBox.setName(String.valueOf(todoListItem.getTaskID()));
            }

            if (taskName != null) {
                rowTaskName.setText(taskName);
            }
            if (todoListItem.getReporterName() != null) {
                if (("uz").equals(locale)){
                    rowTaskAuthor.setText(todoListItem.getReporterName() + " tomonidan");
                } else {
                    rowTaskAuthor.setText(" by " + todoListItem.getReporterName());
                }
            }

            removeBtn = new WfmButton2(null, null, WfmButton2.ICON_TRASH);
            removeBtn.setStyleName("btn--icon");
            removeBtn.addClickHandler(clickEvent -> {
                Integer hiddenTaskID = null;
                if (taskIdHiddenBox.getName() != null && !"".equals(taskIdHiddenBox.getName())) {
                    hiddenTaskID = Integer.parseInt(taskIdHiddenBox.getName());
                }
                if (hiddenTaskID != null) {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    final Integer finalHiddenTaskID = hiddenTaskID;
                    message.addCloseHandler(new com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler() {
                        @Override
                        public void onSubmit() {
                            RowTodo.this.removeFromParent();
                            removeRowTable(finalHiddenTaskID);
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                    message.open();
                }
            });

            setStyleName("widget-row");
            if (todoListItem != null && todoListItem.getStatusID() != null && todoListItem.getStatusID() == Constants.COMPLETED) {
                addStyleName("widget-row--checked");
            }

            Div imagePanel = new Div("widget-row__icon");
            imagePanel.add(checkBox);
            imagePanel.add(taskIdHiddenBox);

            taskNamePanel.setStyleName("widget-row__text");
            taskNamePanel.add(rowTaskName);
            taskNamePanel.add(rowTaskAuthor);

            Span prioritySpan = new Span();
            prioritySpan.setStyleName("todo-indicator");
            if (todoListItem.getPriorityName() != null) {
                Element element = DOM.createElement("em");
                element.setInnerHTML(todoListItem.getPriorityName());
                prioritySpan.getElement().appendChild(element);
            }
            Div todoCatPanel = new Div();
            if (TodoListItem.HIGH.equals(todoListItem.getPriorityCode())) {
                todoCatPanel.setStyleName("todo-action todo-cat--3");
            } else if (TodoListItem.MEDIUM.equals(todoListItem.getPriorityCode())) {
                todoCatPanel.setStyleName("todo-action todo-cat--2");
            } else if (TodoListItem.LOW.equals(todoListItem.getPriorityCode())) {
                todoCatPanel.setStyleName("todo-action todo-cat--1");
            } else {
                todoCatPanel.setStyleName("todo-action todo-cat--0");
            }
            todoCatPanel.add(prioritySpan);
            todoCatPanel.add(removeBtn);

            rowEndPanel.add(todoCatPanel);
            rowEndPanel.setStyleName("widget-row__end");

            add(imagePanel);// 0
            add(taskNamePanel);// 1
            add(rowEndPanel);    // 2
        }

        private void initializeWidgets(Div widgetRow) {
            boolean isTicked = todoListItem != null && todoListItem.getStatusID() != null && todoListItem.getStatusID() == Constants.COMPLETED;
            rowTaskName = new Span();
            rowTaskAuthor = new Small();
            rowTaskAuthor.setStyleName("todo-author");
            taskNamePanel = new Div();
            rowEndPanel = new Div();

            taskIdHiddenBox = new TextBox();
            taskIdHiddenBox.getElement().getStyle().setDisplay(Style.Display.NONE);
            taskIdHiddenBox.setWidth("1px");

            checkBox = drawCheckBox(isTicked, todoListItem, widgetRow, taskIdHiddenBox, rowTaskName);
        }
    }

    private void saveTodoData(TodoListItem listItem, String todoName, Integer employeeId, Integer peEmployeeId, Integer priorityId) {
        todoListPanel.saveTodoListItem(listItem, todoName, employeeId, peEmployeeId, priorityId, null, false);
    }

    private class MyPanel extends HorizontalPanelDiv implements HasClickHandlers {
        @Override
        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }
    }

    public void setShowEmptyMessageHandle(Command showEmptyMessageHandle) {
        this.showEmptyMessageHandle = showEmptyMessageHandle;
    }
}