package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.ganttchart.client.treetable.GanttTreeItem;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.edatasite.workforce.gwt.task.client.ui.TasksChooser;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDialogContent;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 1/23/13
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */

public class GanttContextMenu extends Composite implements ContextMenuHandler, Constants {

	private final GanttChart ganttChart;
	private final ProjectStrings projectStrings = ProjectStrings.App.get();
	private final WfmStrings wfmStrings= WfmStrings.App.get();
	private final TaskServiceAsync taskService = TaskService.App.get();
	private final GanttChartServiceAsync ganttChartService = GanttChartService.App.get();
	private final TasksChooser predTasks = new TasksChooser(true, TasksChooser.PREDECESSOR);
	private final TasksChooser succTasks = new TasksChooser(true, TasksChooser.SUCCESSOR);
	private final HashMap<Integer, String[]> selectedTasks = new HashMap<>();
    private PopupPanel contextMenu;
    private final TaskSingleItem taskItem;
	private Button ok, cancel;
	private DataListBox listBox;
	private KpiModal shell;
	private KpiCheckBox withAllTasksAndSUBW;
	private FlexTable grid;

    public GanttContextMenu(final GanttChart ganttChart, final GanttTreeItem treeItem) {
		this.ganttChart = ganttChart;
        this.taskItem = (TaskSingleItem) treeItem.getUserObject();
        VerticalPanel main = new VerticalPanel();

		MenuBar menuBar = new MenuBar(true);
		menuBar.setAnimationEnabled(true);
		final MenuPopItem taskSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
		taskSummary.ensureDebugId("taskView");
		taskSummary.setCommand(() -> {
            contextMenu.hide();
            if (taskItem.isWorkstream()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("workstream|summary/" + taskItem.getObjectID());
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + taskItem.getObjectID());
            }
        });
		menuBar.addItem(taskSummary);

		final MenuPopItem taskEdit = new MenuPopItem(taskItem.isWorkstream() ? projectStrings.editWorkStream() : projectStrings.editTask(), "icon-employee-edit-profile");
		taskEdit.ensureDebugId("editTask");
		taskEdit.setCommand(() -> {
            contextMenu.hide();
            if (taskItem.isWorkstream()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("workstream|edit/" + taskItem.getObjectID());
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("task|edit/" + taskItem.getObjectID());
            }
        });
		menuBar.addItem(taskEdit);
		taskEdit.setVisible(false);

        /*final Label moveUpMenu = new Label("Move Up");
        moveUpMenu.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GanttTreeItem treeItem = GanttChart.taskItems.get(taskItem.getObjectID());
                treeItem.getTreeTable().moveSelectionUp(treeItem);
                contextMenu.hide();
            }
        });
        setStyleNamesToMenuItems(moveUpMenu);
        base.add(moveUpMenu);

        final Label moveDownMenu = new Label("Move Down");
        moveDownMenu.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                treeItem.getTreeTable().moveSelectionDown(treeItem, true);
                contextMenu.hide();
            }
        });
        setStyleNamesToMenuItems(moveDownMenu);
        base.add(moveDownMenu);*/

        /*final Label shiftRightMenu = new Label("Shift to Right");
        shiftRightMenu.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GanttTreeItem treeItem = GanttChart.taskItems.get(taskItem.getObjectID());
                treeItem.getTreeTable().shiftToRight(treeItem);
                contextMenu.hide();
            }
        });
        setStyleNamesToMenuItems(shiftRightMenu);
        base.add(shiftRightMenu);

        final Label shiftLeftMenu = new Label("Shift to Left");
        shiftLeftMenu.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GanttTreeItem treeItem = GanttChart.taskItems.get(taskItem.getObjectID());
                if (treeItem.getDepth() > 0) {
                    treeItem.getTreeTable().shiftToLeft(treeItem);
                }
                contextMenu.hide();
            }
        });
        setStyleNamesToMenuItems(shiftLeftMenu);
        if (treeItem.getDepth()<=0) {
            DOM.setElementProperty(shiftLeftMenu.getElement(), "disabled", "true");
        }
        base.add(shiftLeftMenu);*/

		final MenuPopItem removeItem = new MenuPopItem(GanttChart.wfmStrings.delete(), "removeItemStyle-profile");
		removeItem.ensureDebugId("delete");
		removeItem.setCommand(() -> {
            final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
            message.setTitle(GanttChart.wfmStrings.warning());
            if (taskItem.isWorkstream()) {
                message.setMessage(GanttChart.taskMessages.areYouSureYouWantToDeleteWorkstream(taskItem.getName()));
            } else {
                message.setMessage(GanttChart.wfmStrings.sureYouWantToDelete());
            }
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    final GanttTreeItem treeItem1 = ganttChart.taskItems.get(taskItem.isWorkstream() ? "w"+taskItem.getObjectID() : "t"+taskItem.getObjectID());
                    final TaskSingleItem task = (TaskSingleItem) treeItem1.getUserObject();
                    if (taskItem.isWorkstream()) {
                        onShellPopup();
                    } else {
                        taskService.deleteTask(taskItem.getObjectID(), PermissionConstants.PM_CONTEXT, new AbstractAsyncCallback<String>() {
                            public void failure(Throwable throwable) {
                                Info.show(GanttChart.wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
}

                            public void success(String result) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_DELETE, result, GanttContextMenu.this);
                                Info.show(Property.get(com.edatasite.workforce.gwt.core.client.ui.Constants.TASK, GanttChart.wfmStrings.messSuccessfulyyDeleted(), GanttChart.wfmStrings.task()), Info.Type.INFO);
                            }
                        });
                    }
                }
            });
            message.open();
        });
		menuBar.addItem(removeItem);
		removeItem.setVisible(false);

		/*final MenuPopItem setPredTask = new MenuPopItem("Set predecessor");
		setPredTask.ensureDebugId("setPredTask");
		setPredTask.setCommand(new Command() {
			@Override
			public void execute() {
				*//*GanttItem ganttItem = new GanttItem();
				ganttItem.setProjectID(ganttChart.getProjectID());
				ganttItem.setStartDate(ganttChart.getStartDate());
				ganttItem.setEndDate(ganttChart.getEndDate());
				ganttItem.setEmployeeID(ganttChart.getEmployeeID());
				ganttItem.setSortBy(ganttChart.getSortBy());*//*

				predTasks.setSelectedTasksMap(selectedTasks);
				predTasks.setSucc_predChooser(succTasks);
				predTasks.setProjectId(taskItem.getProjectID());
				predTasks.setTaskId(taskItem.getObjectID());
				predTasks.setProjectName(taskItem.getProjectName());
				predTasks.clearTable();
				if (taskItem.getPredecessorTasks() != null && taskItem.getPredecessorTasks().length > 0) {
					for (int i = 0; i < taskItem.getPredecessorTasks().length; i++) {
						if (taskItem.getPredecessorTasks()[i] != null) {
							GanttTreeItem item1 = ganttChart.taskItems.get(taskItem.getPredecessorTasks()[i].getId());
							TaskSingleItem taskSingleItem = (TaskSingleItem) item1.getUserObject();
							TaskSelectItem taskSelectItem = new TaskSelectItem();
							taskSelectItem.setId(taskSingleItem.getObjectID());
							taskSelectItem.setName(taskSingleItem.getName());
							taskSelectItem.setTaskNumber(taskSingleItem.getNumberData().getNumberString());
							taskSelectItem.setTaskStartDate(taskSingleItem.getStartDate());
							taskSelectItem.setTaskDueDate(taskSingleItem.getDueDate());
							predTasks.addTableItem(taskSelectItem);
						}
					}
					predTasks.refreshAddedTasks();
				}
				predTasks.showShell();
				predTasks.setSelectionChange(new Command() {
					@Override
					public void execute() {
						LoadingPanel.get().show(ganttChart.wfmStrings.savingTaskPleaseWait());
						ganttChartService.saveTaskDependency(taskItem.getObjectID(), predTasks.getTasks(), SET_PREDECESSOR, new AbstractAsyncCallback<Void>() {
							@Override
							public void failure(Throwable throwable) {
								LoadingPanel.loading(false);
								Info.error("", ganttChart.wfmStrings.errorOnSavingPredecessor());
							}

							@Override
							public void success(Void result) {
								Info.show("", ganttChart.wfmStrings.taskPredecessorSuccSaved());
								predTasks.resultsShell.close();
								LoadingPanel.loading(false);
								WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_PREDECESSOR_CHANGE, null, GanttContextMenu.this);
							}
						});
					}
				});
//				SetPredecessorPopup popup = new SetPredecessorPopup(taskItem, ganttItem, SET_PREDECESSOR);
//				popup.center();
//				popup.show();
			}
		});
		menuBar.addItem(setPredTask);
		setPredTask.setVisible(false);

		final MenuPopItem setSuccTask = new MenuPopItem("Set successor");
		setSuccTask.ensureDebugId("setSuccTask");
		setSuccTask.setCommand(new Command() {
			@Override
			public void execute() {
				succTasks.setSelectedTasksMap(selectedTasks);
				succTasks.setSucc_predChooser(predTasks);
				succTasks.setProjectId(taskItem.getProjectID());
				succTasks.setTaskId(taskItem.getObjectID());
				succTasks.setProjectName(taskItem.getProjectName());
				succTasks.clearTable();
				if (taskItem.getSuccessorTasks() != null && taskItem.getSuccessorTasks().length > 0) {
					for (int i = 0; i < taskItem.getSuccessorTasks().length; i++) {
						if (taskItem.getSuccessorTasks()[i] != null) {
							GanttTreeItem item1 = ganttChart.taskItems.get(taskItem.getSuccessorTasks()[i].getId());
							TaskSingleItem taskSingleItem = (TaskSingleItem) item1.getUserObject();
							TaskSelectItem taskSelectItem = new TaskSelectItem();
							taskSelectItem.setId(taskSingleItem.getObjectID());
							taskSelectItem.setName(taskSingleItem.getName());
							taskSelectItem.setTaskNumber(taskSingleItem.getNumberData().getNumberString());
							taskSelectItem.setTaskStartDate(taskSingleItem.getStartDate());
							taskSelectItem.setTaskDueDate(taskSingleItem.getDueDate());
							succTasks.addTableItem(taskSelectItem);
						}
					}
					succTasks.refreshAddedTasks();
				}
				succTasks.showShell();
				succTasks.setSelectionChange(new Command() {
					@Override
					public void execute() {
						LoadingPanel.get().show(ganttChart.wfmStrings.savingTaskPleaseWait());
						ganttChartService.saveTaskDependency(taskItem.getObjectID(), succTasks.getTasks(), SET_SUCCESSOR, new AbstractAsyncCallback<Void>() {
							@Override
							public void failure(Throwable throwable) {
								LoadingPanel.loading(false);
								Info.error("", ganttChart.wfmStrings.err());
							}

							@Override
							public void success(Void result) {
								Info.show("", ganttChart.wfmStrings.taskSuccessorSuccSaved());
								succTasks.resultsShell.close();
								LoadingPanel.loading(false);
								WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_PREDECESSOR_CHANGE, null, GanttContextMenu.this);
							}
						});
					}
				});

				*//*GanttItem ganttItem = new GanttItem();
				ganttItem.setProjectID(ganttChart.getProjectID());
				ganttItem.setStartDate(ganttChart.getStartDate());
				ganttItem.setEndDate(ganttChart.getEndDate());
				ganttItem.setEmployeeID(ganttChart.getEmployeeID());
				ganttItem.setSortBy(ganttChart.getSortBy());
				SetPredecessorPopup popup = new SetPredecessorPopup(taskItem, ganttItem, SET_SUCCESSOR);
				popup.center();
				popup.show();*//*
			}
		});
		menuBar.addItem(setSuccTask);
		setSuccTask.setVisible(false);*/

		if (!taskItem.isWorkstream()) {
			if (ganttChart.permissionMap.containsKey(taskItem.getObjectID())) {
				Utils.setUserPermissions(ganttChart.permissionMap.get(taskItem.getObjectID()));
				taskEdit.setVisible(Utils.hasPermission(PermissionConstants.PM_TASKS_EDIT));
//				setPredTask.setVisible(Utils.hasPermission(PermissionConstants.PM_TASKS_EDIT));
//				setSuccTask.setVisible(Utils.hasPermission(PermissionConstants.PM_TASKS_EDIT));
				removeItem.setVisible(Utils.hasPermission(PermissionConstants.PM_TASKS_REMOVE));
			} else {
				taskService.getPermissions(taskItem.getObjectID(), PermissionConstants.PM_CONTEXT, new AsyncCallback<HashSet<String>>() {
					@Override
					public void onFailure(Throwable throwable) {
						//To change body of implemented methods use File | Settings | File Templates.
					}

					@Override
					public void onSuccess(HashSet<String> result) {
						Utils.setUserPermissions(result);
						taskEdit.setVisible(Utils.hasPermission(PermissionConstants.PM_TASKS_EDIT));
//						setPredTask.setVisible(Utils.hasPermission(PermissionConstants.PM_TASKS_EDIT));
//						setSuccTask.setVisible(Utils.hasPermission(PermissionConstants.PM_TASKS_EDIT));
						removeItem.setVisible(Utils.hasPermission(PermissionConstants.PM_TASKS_REMOVE));
						ganttChart.permissionMap.put(taskItem.getObjectID(), result);
					}
				});
			}
		} else {
			if (Utils.hasPermission(PermissionConstants.PM_PROJECT_WORKSTREAM_ADD)) {
				taskEdit.setVisible(true);
			}
			if (Utils.hasRole(com.edatasite.workforce.gwt.core.client.ui.Constants.ADMIN) || Utils.hasRole(com.edatasite.workforce.gwt.core.client.ui.Constants.PM)) {
				removeItem.setVisible(true);
			}
		}

		contextMenu = new PopupPanel(true);
        contextMenu.add(menuBar);
        this.contextMenu.hide();

        initWidget(main);

        // of course it would be better if base would implement HasContextMenuHandlers, but the effect is the same
        addDomHandler(this, ContextMenuEvent.getType());
    }

	private void onShellPopup() {
		shell = new KpiModal();
		shell.setTitle(projectStrings.deleteWorkstream());
		shell.setWidth(350);

		HTML message = new HTML(GanttChart.taskMessages.areYouSureYouWantToDeleteWorkstream(taskItem.getName()));
		HTML icon = new HTML("<span class='my-mbox-icon my-mbox-question' style='height:50px;width:40px;display:block'></span>");
		HTML listBoxLabel = new HTML(projectStrings.moveSubworkstreamsAndTasksTo());

		listBox = new DataListBox();
		listBox.setWidth("150px");
		taskService.getWorkstreamsSomeParent(taskItem.getObjectID(), new AbstractAsyncCallback<SelectItem[]>() {
			@Override
			public void failure(Throwable caught) {
			}

			@Override
			public void onSuccess(SelectItem[] result) {
				listBox.setItems(result);
				deleteWorkstream();
			}
		});

		ok = new Button(GanttChart.wfmStrings.ok());
		cancel = new Button(GanttChart.wfmStrings.no());
		withAllTasksAndSUBW = new KpiCheckBox();
		withAllTasksAndSUBW.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                listBox.setSelectedNullLabel();
                listBox.setStyleName("");
                listBox.setEnabled(false);

            } else {
                listBox.setEnabled(true);
            }
        });

		final HorizontalPanelDiv buttonPanel = new HorizontalPanelDiv();
		buttonPanel.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		buttonPanel.setMarginRight(30);
		buttonPanel.setMarginBottom(10);
		buttonPanel.setStyleName("workforce");
		buttonPanel.add(10, ok, cancel);

		final HorizontalPanelDiv checkBoxPanel = new HorizontalPanelDiv();
		HTML dltW = new HTML(projectStrings.deleteWithAllSubworkstreamsAndTasksInIt());
		dltW.getElement().getStyle().setVerticalAlign(com.google.gwt.dom.client.Style.VerticalAlign.TOP);
		checkBoxPanel.add(3, withAllTasksAndSUBW, dltW);

		final MaterialDialogContent cont = shell.getContent();
		grid = new FlexTable();
		grid.setCellSpacing(7);
		grid.setCellPadding(5);
		grid.setWidget(0, 0, icon);
		grid.setWidget(0, 1, message);
		grid.setWidget(1, 0, checkBoxPanel);
		grid.getFlexCellFormatter().setColSpan(1, 0, 2);
		grid.setHTML(2, 0, projectStrings.orAlternatively());
		grid.getFlexCellFormatter().setColSpan(2, 0, 2);
		grid.setWidget(3, 0, listBoxLabel);
		grid.getFlexCellFormatter().setWidth(3, 0, "115px");
		grid.setWidget(3, 1, listBox);
		cont.add(grid);
		cont.add(buttonPanel);
	}

	private void deleteWorkstream() {
		if (listBox != null && listBox.getItems().length > 0) {
			listBox.setVisible(true);
			grid.getFlexCellFormatter().setVisible(2, 0, true);
			grid.getFlexCellFormatter().setVisible(3, 0, true);
			grid.getFlexCellFormatter().setVisible(3, 1, true);
		} else {
			listBox.setVisible(false);
			grid.getFlexCellFormatter().setVisible(2, 0, false);
			grid.getFlexCellFormatter().setVisible(3, 0, false);
			grid.getFlexCellFormatter().setVisible(3, 1, false);
		}
		shell.open();

		listBox.addValueChangeHandler(event -> {
            listBox.setStyleName("");
            if (listBox.isSomethingSelected()) {
                withAllTasksAndSUBW.setEnabled(false);
                withAllTasksAndSUBW.setValue(false);
            } else {
                withAllTasksAndSUBW.setEnabled(true);
            }
        });

		ok.addClickHandler(event -> {
            if (withAllTasksAndSUBW.getValue()) {
                ok.setEnabled(false);
                cancel.setEnabled(false);
                removedWorkstream(null);
            } else {
                if (listBox.getItems().length > 0) {
                    if (listBox.isSomethingSelected()) {
                        ok.setEnabled(false);
                        cancel.setEnabled(false);
                        removedWorkstream(listBox.getSelectedItem().getId());
                    } else {
                        listBox.setStyleName("x-form-invalid");
                    }
                } else {
                    removedWorkstream(null);
                }
            }
        });
		cancel.addClickHandler(event -> {
            shell.close();
            shell.clear();
        });
	}

	private void removedWorkstream(Integer defaultWorkstreamID) {
        LoadingPanel.loading(true);
        taskService.deleteWorkstream(taskItem.getObjectID(), defaultWorkstreamID, withAllTasksAndSUBW.getValue(), new AbstractAsyncCallback<Void>() {
			@Override
			public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

			@Override
			public void success(Void result) {
				LoadingPanel.loading(false);
                Info.show(Utils.textFormat(GanttChart.wfmStrings.messSuccessfulyyDeleted(), wfmStrings.workStream()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKSTREAM_DELETED, result, GanttContextMenu.this);
				if (shell != null) {
					shell.close();
				}
			}
		});
	}

    private void setStyleNamesToMenuItems(final Label menuItem) {
        menuItem.setStyleName("contextMenuItem");
        menuItem.addMouseOverHandler(event -> menuItem.setStyleName("contextMenuItemOver"));
        menuItem.addMouseOutHandler(event -> menuItem.setStyleName("contextMenuItem"));
    }

    @Override
    public void onContextMenu(ContextMenuEvent event) {
        // stop the browser from opening the context menu
        event.preventDefault();
        event.stopPropagation();


        this.contextMenu.setPopupPosition(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
        this.contextMenu.show();
    }

    public PopupPanel getContextMenu() {
        return contextMenu;
    }
}
