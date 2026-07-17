package com.edatasite.workforce.gwt.task.client.ui.view;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.treetable.TreeItem;
import com.edatasite.workforce.gwt.core.client.ui.treetable.*;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.localization.TaskMessages;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamSingleItem;
import com.edatasite.workforce.gwt.workstream.client.rpc.WbsService;
import com.edatasite.workforce.gwt.workstream.client.rpc.WbsServiceAsync;
import com.edatasite.workforce.gwt.workstream.client.ui.CopyWorkstreamToOtherProjectPopup;
import com.edatasite.workforce.gwt.workstream.client.ui.WorkstreamChooser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDialogContent;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ProjectWBSView extends BaseListView {

    private static final TaskMessages taskMessages = TaskMessages.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final TaskServiceAsync taskService = TaskService.App.get();
    private static final WbsServiceAsync wbsService = WbsService.App.get();
    private final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    private WorkstreamChooser parentWorkstream;
    private final Integer projectId;
    private Integer workstreamID;
    private Integer taskID = null;
    private KpiModal shell;
    private DataListBox listBox;
    private WfmButton2 cancel;
    private WfmButton2 ok;
    private WfmTreeTable treeTable;
    private KpiCheckBox withAllTasksAndSUBW;
    private FlexTable grid;
    private HorizontalPanel postFormPanel;
    private boolean hasAccessToChange = true;
    private boolean isCompleted = false;
    private final ListingFilterParameter filterParameter = new ListingFilterParameter();

    public ProjectWBSView(Integer projectId, boolean hasAccessToChange) {
        super("projectWBS", projectStrings.workBreakdownStructure());
        this.projectId = projectId;
        this.hasAccessToChange = hasAccessToChange;
    }

    @Override
    public String getIconStyle() {
        return "casesList cases-list";
    }

    @Override
    protected Widget onInitialize() {
        postFormPanel = new HorizontalPanel();
        treeTable = new WfmTreeTable(getColumn(), getProvider(), getChildProvider(), getDesigner());
        add(treeTable);
        add(postFormPanel);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKSTREAM_ADD, ProjectWBSView.this, (sender, args) -> treeTable.refresh());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, ProjectWBSView.this, (sender, args) -> treeTable.refresh());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_DELETE, ProjectWBSView.this, (sender, args) -> treeTable.refresh());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_EDIT, ProjectWBSView.this, (sender, args) -> treeTable.refresh());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_MEMBERS_EDIT, ProjectWBSView.this, (sender, args) -> treeTable.refresh());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKSTREAM_DELETED, ProjectWBSView.this, (sender, args) -> treeTable.refresh());

        return null;
    }

    /**
     * Tree Table Top Bar and Bottom Bar
     *
     * @return - wfm tree table designer
     */
    private WfmTreeTableDesigner getDesigner() {
        return new WfmTreeTableDesigner() {
            public void treeTableTopPanel(final WfmToolBar topPanel) {
                ProjectService.App.get().getProjectSpecificPermissions(projectId, new AbstractAsyncCallback<HashSet<String>>() {
                    @Override
                    public void failure(Throwable throwable) {
                    }

                    @Override
                    public void success(HashSet<String> result) {
                        Utils.setUserPermissions(result);
                        boolean hasChild = false;

                        ActionButton actionButton = getAddNewButton(ActionButton.Type.TOOLMENU);
                        MenuBar menu = new MenuBar(true);

                        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_WORKSTREAM_ADD) && hasAccessToChange) {
                            hasChild = true;
                            MenuPopItem addWorkstream = new MenuPopItem(wfmStrings.workStream());
                            addWorkstream.setCommand(() -> {
                                if (!isCompleted) {
                                    String action = "workstream|add/add/" + ((projectId != null && projectId > 0) ? projectId.toString() : "");
                                    SinksContainerFactory.entryPoint.onHistoryChanged(action);
                                } else {
                                    Info.show(projectStrings.newWorkstreamCannotProjects(), Info.Type.WARNING);
                                }
                            });
                            menu.addItem(addWorkstream);
                        }

                        if (Utils.hasPermission(PermissionConstants.PM_TASKS_ADD) && hasAccessToChange && !isCompleted) {
                            hasChild = true;
                            MenuPopItem addTask = new MenuPopItem(Property.get(Constants.TASK, wfmStrings.task()));
                            addTask.setCommand(() -> {
                                String action = "task|add/add/" + ((projectId != null && projectId > 0) ? projectId.toString() : "");
                                SinksContainerFactory.entryPoint.onHistoryChanged(action);
                            });
                            menu.addItem(addTask);
                        }

                        if (Utils.hasPermission(PermissionConstants.PM_TASKS_ADD_MULTI) && hasAccessToChange && !isCompleted) {
                            hasChild = true;
                            MenuPopItem addMultiTask = new MenuPopItem(Property.get(Constants.TASK, wfmStrings.multiTask(), wfmStrings.task()));
                            addMultiTask.setCommand(() -> {
                                String action = "multitask|add/add/" + ((projectId != null && projectId > 0) ? projectId.toString() : "");
                                SinksContainerFactory.entryPoint.onHistoryChanged(action);
                            });
                            menu.addItem(addMultiTask);
                        }

                        actionButton.setMenu(menu);

                        FlowPanel pnlOperActions = new FlowPanel();
                        pnlOperActions.setStyleName("operPanel__actions");
                        if (hasChild) {
                            pnlOperActions.add(actionButton);
                        }

                        FlowPanel searchPanel = new FlowPanel();
                        searchPanel.addStyleName("searchForm");
                        pnlOperActions.add(searchPanel);

                        TextBox searchBox = new TextBox();
                        searchBox.setMaxLength(255);
                        searchBox.ensureDebugId("searchBox");
                        searchBox.setPlaceHolder(wfmStrings.search());
                        searchBox.addKeyUpHandler(event -> {
                            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                                filterParameter.setSearchKey(searchBox.getValue());
                                treeTable.refresh();
                            }
                        });

                        ActionButton search = new ActionButton("", "searchForm__btn");
                        search.ensureDebugId("search_button");
                        search.add(new SvgIcon(SvgEnum.search));
                        search.addClickHandler(event -> {
                            if (searchPanel.getStyleName().contains("active")) {
                                filterParameter.setSearchKey(searchBox.getValue());
                                treeTable.refresh();
                            }
                            searchPanel.addStyleName("active");
                            searchBox.setFocus(true);
                        });

                        Span inputWrapper = new Span();
                        inputWrapper.addStyleName("searchForm__control");
                        inputWrapper.add(searchBox);
                        searchPanel.add(search);
                        searchPanel.add(inputWrapper);

                        Span xSearch = new Span();
                        xSearch.addStyleName("searchForm__x");
                        xSearch.add(new SvgIcon(SvgEnum.x));
                        xSearch.addClickHandler(e -> {
                            searchPanel.removeStyleName("active");
                            searchBox.setValue("");
                        });
                        searchPanel.add(xSearch);

                        //reset button
                        MaterialPanel pnlButtons = new MaterialPanel("operPanel__btn-groups");
                        ActionButton resetBtn = new ActionButton("", "btn btn--icon");
                        resetBtn.getElement().setId("reset_button");
                        resetBtn.add(new SvgIcon(SvgEnum.repeat));
                        resetBtn.addClickHandler(event -> {
                            searchBox.setText("");
                            filterParameter.setSearchKey(null);
                            filterParameter.setSortField(null);
                            filterParameter.setSortDir(null);
                            treeTable.refresh();
                        });
                        new MaterialTooltip(resetBtn, wfmStrings.reset()).setPosition(Position.TOP);
                        pnlButtons.add(resetBtn);

                        //refresh button
                        ActionButton refreshBtn = new ActionButton("", "btn btn--icon");
                        refreshBtn.getElement().setId("reload_button");
                        refreshBtn.addClickHandler(event -> {
                            filterParameter.setSearchKey(searchBox.getValue());
                            treeTable.refresh();
                        });
                        refreshBtn.add(new SvgIcon(SvgEnum.rotateCw));
                        new MaterialTooltip(refreshBtn, wfmStrings.refresh()).setPosition(Position.TOP);
                        pnlButtons.add(refreshBtn);

                        pnlOperActions.add(pnlButtons);

                        topPanel.addStyleName("operPanel--header");
                        topPanel.add(pnlOperActions);
                    }
                });
            }

            public void treeTableBottomPanel(final WfmToolBar bottomPanel) {
                ActionButton pdfVersion = new ActionButton("icon-pdf", ActionButton.Type.BUTTON);
                pdfVersion.addClickHandler(clickEvent -> {
                    RequestObject ro = new RequestObject();
                    ro.setObjectID(projectId);
                    Utils.sendPDFOrExcelRequest(bottomPanel, CommandConstants.PDF_URL + "/workStreamListPDFHandler", ro.getRequestParams(), "_blank");
                });
            }

            @Override
            public void initDataEmptyTable(WfmTreeTableEmptyDataMessage widget) {

            }
        };
    }

    private WfmTreeTableChildProvider getChildProvider() {
        return new WfmTreeTableChildProvider() {
            public boolean isHaveChilds(Object object) {
                WbsItem item = (WbsItem) object;
                return item.hasChildren();
            }

            public Object[] getChilds(Object object) {
                return null;
            }
        };
    }

    /**
     * TreeTable Get Data Provider
     *
     * @return - wfm tree table callback provider
     */
    private WfmTreeTableCallbackProvider getProvider() {
        return (treeItem, item, callback) -> {
            wbsService.getSelectCompilitedStatus(projectId, new AbstractAsyncCallback<WbsItem>() {
                @Override
                public void onSuccess(final WbsItem result) {
                    isCompleted = Constants.PS_COMPLETED.equals(result.getProjectStatus());
                }
            });
            final WbsItem objItem = (WbsItem) treeItem;
            if (item == null) {
                LoadingPanel.loading(true);
                filterParameter.setProjectId(projectId);
                initProjectWBSLists(filterParameter, item, callback, null);
            } else {
                LoadingPanel.loading(true);
                filterParameter.setWorkstreamID(objItem.getId());
                wbsService.getSubItems(filterParameter, new AbstractAsyncCallback<WbsItem[]>() {
                    public void success(WbsItem[] result) {
                        ArrayList<Integer> treeItemIds = new ArrayList<>();
                        if (result != null) {
                            for (WbsItem wbsItem : result) {
                                treeItemIds.add(wbsItem.getId());
                            }
                        }
                        LoadingPanel.loading(false);
                        callback.onSuccess(result, item, objItem.getId(), treeItemIds);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                        LoadingPanel.loading(false);
                    }
                });
            }
        };
    }

    private void initProjectWBSLists(ListingFilterParameter filterParameter, TreeItem item, TreeListDataCallback callback, Span container) {
        wbsService.getItems(filterParameter, new AbstractAsyncCallback<WbsItem[]>() {
            public void success(WbsItem[] result) {
                ArrayList<Integer> treeItemIds = new ArrayList<>();
                if (result != null) {
                    for (WbsItem wbsItem : result) {
                        treeItemIds.add(wbsItem.getId());
                    }
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result != null && result.length > 0) {
                        statisticShortcut.setText(countFormat(Integer.valueOf(result.length)) + "");
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
                LoadingPanel.loading(false);
                if (callback != null) {
                    callback.onSuccess(result, item, null, treeItemIds);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                LoadingPanel.loading(false);
            }
        });
    }

    /**
     * Create Tree Table Columns
     */
    private WfmTreeTableColumn[] getColumn() {
        WfmTreeTableColumn[] treeTableColumns = new WfmTreeTableColumn[9];
        //name
        treeTableColumns[0] = new WfmTreeTableColumn(wfmStrings.name(), 300, object -> {
            WbsItem item = (WbsItem) object;
            String name;
            if (WbsItem.TASK == item.getNodeType()) {
                name = (item.getNumberData() != null && !"".equals(item.getNumberData()) ? item.getNumberData() + " - " : "") + item.getName();
                return name;
            } else {
                name = item.getName();
            }
            HTML nameHTML = new HTML();
            if (name != null && name.length() > 35) {
                nameHTML.setTitle(name);
                name = name.substring(0, 35) + "...";
            }
            nameHTML.setHTML(name);

            return nameHTML;

        }, sortDirection -> sortItemTable(WbsItem.NAME, sortDirection));

        //action
        treeTableColumns[1] = new WfmTreeTableColumn(wfmStrings.action(), 50, new WfmTreeTableCellWidget() {
            int actionItemCount = 0;

            public Object getTreeTableCell(Object object) {
                final WbsItem item = (WbsItem) object;
                if (WbsItem.WORKSTREAM == item.getNodeType()) {
                    actionItemCount = 0;
                    MenuBar menuBar = new MenuBar(true);

                    MenuPopItem workSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-project-small");
                    workSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("workstream|summary/" + item.getId()));
                    actionItemCount++;
                    menuBar.addItem(workSummary);

                    if (hasAccessToChange && !isCompleted && Utils.hasPermission(PermissionConstants.PM_PROJECT_WORKSTREAM_EDIT)) {
                        MenuPopItem workEdit = new MenuPopItem(wfmStrings.edit(), "icon-project-edit-small");
                        workEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("workstream|edit/" + item.getId()));

                        actionItemCount++;
                        menuBar.addItem(workEdit);
                    }

                    if (hasAccessToChange) {
                        MenuPopItem workSetParent = new MenuPopItem(wfmStrings.setParent(), "icon-setparent-small");
                        workSetParent.setCommand(() -> taskService.getWorkstream(item.getId(), new AbstractAsyncCallback<WorkstreamSingleItem>() {
                            public void failure(Throwable throwable) {
                                throwable.getMessage();
                            }

                            public void success(final WorkstreamSingleItem workstream) {
                                taskID = null;
                                workstreamID = item.getId();
                                parentWorkstream = new WorkstreamChooser();
                                parentWorkstream.setSelectedCommand(ProjectWBSView.this::save);
                                if (projectId != null) {
                                    parentWorkstream.setProjectName(workstream.getProjectName());
                                    parentWorkstream.setSelectedWorkstreamId(workstreamID);
                                    parentWorkstream.setProjectId(projectId);
                                    parentWorkstream.publicShowShell();
                                }
                            }
                        }));

                        actionItemCount++;
                        menuBar.addItem(workSetParent);
                    }

                    if (hasAccessToChange) {
                        MenuPopItem workAddChildWorkstream = new MenuPopItem(projectStrings.addChild(), "icon-add-subworkstream-small");
                        workAddChildWorkstream.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("workstream|add/add/" + projectId + "/" + item.getId()));

                        actionItemCount++;
                        menuBar.addItem(workAddChildWorkstream);
                    }


                    if (hasAccessToChange && !isCompleted) {
                        MenuPopItem workAddTask = new MenuPopItem(Property.get(Constants.TASK, wfmStrings.addMess(), wfmStrings.task()), "icon-addtask-small");
                        workAddTask.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("task|add/add/" + projectId + "/" + item.getId()));
                        actionItemCount++;
                        menuBar.addItem(workAddTask);
                    }

                    if (hasAccessToChange && !isCompleted) {
                        MenuPopItem workAddMultiTask = new MenuPopItem(Property.get(Constants.TASK, wfmStrings.addMultiTask(), wfmStrings.task()), "icon-addmultitask-small");
                        workAddMultiTask.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("multitask|add/add/" + projectId + "/" + item.getId()));
                        actionItemCount++;
                        menuBar.addItem(workAddMultiTask);
                    }


                    if (hasAccessToChange) {
                        MenuPopItem moveWSToOtherProject = new MenuPopItem(wfmStrings.copy(), "list-action-menu-icon icon-copy");
                        moveWSToOtherProject.setCommand(() -> new CopyWorkstreamToOtherProjectPopup(item.getId()));
                        actionItemCount++;
                        menuBar.addItem(moveWSToOtherProject);
                    }

                    if ((Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.PM)) && hasAccessToChange) {
                        MenuPopItem removeWorkstream = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile file--ProjectWDSView");
                        removeWorkstream.setCommand(() -> taskService.getWorkstream(item.getId(), new AbstractAsyncCallback<WorkstreamSingleItem>() {
                            public void failure(Throwable throwable) {
                                throwable.getMessage();
                            }

                            public void success(final WorkstreamSingleItem workstream) {
                                if (workstream != null) {
                                    workstreamID = item.getId();
                                    onShellPopup(workstream);
                                }
                            }
                        }));
                        actionItemCount++;
                        menuBar.addItem(removeWorkstream);


                    }
                    ToolItem toolItem = new ToolItem(actionItemCount);
                    toolItem.setWidget(menuBar);
                    return toolItem.getAction();

                } else if (WbsItem.TASK == item.getNodeType()) {
                    actionItemCount = 0;
                    MenuBar menuBar = new MenuBar(true);
                    MenuPopItem taskSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    taskSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + item.getId()));
                    actionItemCount++;
                    menuBar.addItem(taskSummary);

                    if (hasAccessToChange && (Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_EDIT : PermissionConstants.CRM_TASKS_EDIT))) {
                        MenuPopItem taskEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                        taskEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("task|edit/" + item.getId()));
                        actionItemCount++;
                        menuBar.addItem(taskEdit);
                    }

                    if ((Utils.hasRole(Constants.ADMIN) || Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_REMOVE : PermissionConstants.CRM_TASKS_REMOVE)) && hasAccessToChange) {
                        MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                        removeItem.setCommand(() -> {
                            WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            message.setTitle(wfmStrings.warning());
                            message.setMessage(wfmStrings.sureYouWantToDelete());
                            message.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    LoadingPanel.loading(true);
                                    taskService.deleteTask(item.getId(), PermissionConstants.PM_CONTEXT, new AbstractAsyncCallback<String>() {
                                        public void failure(Throwable throwable) {
                                            LoadingPanel.loading(false);
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        public void success(String result) {
                                            LoadingPanel.loading(false);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_DELETE, result, ProjectWBSView.this);
                                            Info.show(Property.get(Constants.TASK, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.task()), Info.Type.INFO);
                                        }
                                    });
                                }
                            });
                            message.open();
                        });
                        actionItemCount++;
                        menuBar.addItem(removeItem);
                    }

                    MenuPopItem pdfItem = new MenuPopItem(wfmStrings.pdf(), "icon-document-pdf");
                    pdfItem.setCommand(() -> {
                        String pdfURL = CommandConstants.PDF_URL + "/taskViewPDFHandler";
                        RequestObject requestObject = new RequestObject(item.getId());
                        HashMap<String, String> parametrs = requestObject.getRequestParams();
                        Utils.sendPDFOrExcelRequest(postFormPanel, pdfURL, parametrs, "_blank");
                    });
                    actionItemCount++;
                    menuBar.addItem(pdfItem);

                    if (hasAccessToChange) {
                        MenuPopItem moveTo = new MenuPopItem(wfmStrings.moveTo(), "icon-setparent-small");
                        moveTo.setCommand(() -> {
                            workstreamID = null;
                            taskID = item.getId();
                            parentWorkstream = new WorkstreamChooser(projectId);
                            parentWorkstream.setSelectedCommand(() -> save());
                            if (projectId != null) {
                                parentWorkstream.setProjectId(projectId);
                                parentWorkstream.setProjectName(" ");
                                parentWorkstream.publicShowShell();
                            }
                        });
                        actionItemCount++;
                        menuBar.addItem(moveTo);
                    }

                    ToolItem toolItem = new ToolItem(actionItemCount);
                    toolItem.setWidget(menuBar);
                    return toolItem.getAction();
                }
                return "";
            }
        });
        treeTableColumns[1].setAlignment(HorizontalPanel.ALIGN_CENTER);
        //assigned to
        treeTableColumns[2] = new WfmTreeTableColumn(wfmStrings.assignedTo(), 200, object -> {
            WbsItem item = (WbsItem) object;
            return Utils.getAssigneesCommaSep(item.getAssignees());
        });

        //start date
        treeTableColumns[3] = new WfmTreeTableColumn(wfmStrings.startDate(), 100, object -> {
            WbsItem item = (WbsItem) object;
            return item.getStartDate() != null ? DateUtils.format(item.getStartDate()) : "";
        }, sortDirection -> sortItemTable(WbsItem.START_DATE, sortDirection));

        //end date
        treeTableColumns[4] = new WfmTreeTableColumn(wfmStrings.endDate(), 100, object -> {
            WbsItem item = (WbsItem) object;
            return item.getEndDate() != null ? DateUtils.format(item.getEndDate()) : "";
        }, sortDirection -> sortItemTable(WbsItem.END_DATE, sortDirection));

        //status
        treeTableColumns[5] = new WfmTreeTableColumn(wfmStrings.status(), 100, object -> {
            WbsItem item = (WbsItem) object;
            if (WbsItem.TASK == item.getNodeType()) {
                return item.getStatusName() != null ? item.getStatusName() : "";
            }
            return "";
        });
        //percent completed
        treeTableColumns[6] = new WfmTreeTableColumn(wfmStrings.percent(), 115, object -> {
            WbsItem item = (WbsItem) object;
            if (WbsItem.TASK == item.getNodeType()) {
                return getCompletedPercent(item.getTaskPercent() != null ? item.getTaskPercent().toString() : "0");
            }
            return "";
        });

        treeTableColumns[7] = new WfmTreeTableColumn(wfmStrings.estimatedTime(), 100, object -> {
            WbsItem item = (WbsItem) object;
            if (WbsItem.TASK == item.getNodeType()) {
                return Utils.formatMinutes(item.getEstimated() != null ? item.getEstimated() : 0);
            }
            return "";
        });

        treeTableColumns[8] = new WfmTreeTableColumn(wfmStrings.timeSpentOnly(), 100, object -> {
            WbsItem item = (WbsItem) object;
            if (WbsItem.TASK == item.getNodeType()) {
                return item.getTimeSpent();
            }
            return "";
        });
        return treeTableColumns;
    }

    /**
     * complete tasks percent view <br/>
     *
     * @param percent - percent this completed background color width
     */
    private HorizontalPanel getCompletedPercent(String percent) {
        HorizontalPanel panelBackground = new HorizontalPanel();
        HorizontalPanel panelPercent = new HorizontalPanel();
        panelBackground.setStyleName("completed_back");
        panelBackground.setWidth("104px");
        panelBackground.setHeight("100%");
        panelPercent.setStyleName("completed_percent");
        panelPercent.setHeight("17px");
        panelPercent.setWidth(percent + "px");
        panelBackground.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        if (!percent.equals("0.0")) {
            panelPercent.add(new HTML("&nbsp;&nbsp;" + formatToDouble(percent) + "%"));
            panelPercent.setTitle(formatToDouble(percent) + "% compele");
            panelPercent.getElement();
        } else {
            panelBackground.add(new HTML("&nbsp;&nbsp;" + "0.00" + "%"));
            panelBackground.setTitle("0" + "% compele");
            panelBackground.getElement();
        }
        panelBackground.add(panelPercent);
        return panelBackground;
    }

    private String formatToDouble(String text) {
        return numberFormat.format(parseToDouble(text));
    }

    private double parseToDouble(String text) {
        return Double.parseDouble(text.replace(",", ""));
    }


    private void sortItemTable(String sortField, String sortDirection) {
        filterParameter.setSortField(sortField);
        filterParameter.setSortDir(Constants.ASC_STR.equals(sortDirection) ? Constants.ASC : Constants.DESC);
        treeTable.refresh();
    }

    private void save() {
        if (parentWorkstream != null && parentWorkstream.getWorkstream().getId() != null) {
            if (workstreamID != null) {
                try {
                    LoadingPanel.loading(true);

                    taskService.updateParentWorkstream(workstreamID, parentWorkstream.getWorkstream().getId(), getCallbackUpdateParent(projectStrings.workstreamSucParentAdded()));
                } catch (Throwable t) {
                    t.getMessage();
                }
            } else if (taskID != null) {
                LoadingPanel.loading(true);

                taskService.updateParentTask(taskID, parentWorkstream.getWorkstream().getId(), getCallbackUpdateParent(projectStrings.taskSucParentAdded()));
            }
        }

    }

    private AbstractAsyncCallback<Integer> getCallbackUpdateParent(final String message) {
        return new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer result) {
                LoadingPanel.loading(false);
                moveToWorkStreamOrTask();
                Info.show(message, Info.Type.INFO);
            }
        };
    }

    private void moveToWorkStreamOrTask() {
        if (workstreamID != null) {//move workStream to parent(other) workStream
            try {
                treeTable.refresh2(workstreamID, parentWorkstream.getWorkstream().getId());
            } catch (Throwable t) {
                t.getMessage();
            }
        } else if (taskID != null) {//move task to workStream
            treeTable.refresh2(taskID, parentWorkstream.getWorkstream().getId());
        }
    }

    private void onShellPopup(final WorkstreamSingleItem workstream) {
        shell = new KpiModal();
        shell.setTitle(wfmStrings.warning());
        shell.setWidth(400);

        HTML message = new HTML(taskMessages.areYouSureYouWantToDeleteWorkstream(workstream.getName()));
        HTML listBoxLabel = new HTML(projectStrings.moveSubworkstreamsAndTasksTo());

        listBox = new DataListBox();
        taskService.getWorkstreamsSomeParent(workstreamID, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                listBox.setItems(result);
                deleteWorkstream();
            }
        });

        ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);
        cancel = new WfmButton2(wfmStrings.no());

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
        grid.setWidget(0, 0, message);
        grid.setWidget(1, 0, checkBoxPanel);
        grid.getFlexCellFormatter().setColSpan(1, 0, 2);
        grid.setHTML(2, 0, projectStrings.orAlternatively());
        grid.getFlexCellFormatter().setColSpan(2, 0, 2);
        grid.setWidget(3, 0, listBoxLabel);
        grid.setWidget(3, 1, listBox);
        cont.add(grid);
        shell.addButton(buttonPanel);
    }

    private void deleteWorkstream() {
        if (listBox.getItems().length > 0) {
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
        taskService.deleteWorkstream(workstreamID, defaultWorkstreamID, withAllTasksAndSUBW.getValue(), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.workStream()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKSTREAM_DELETED, result, ProjectWBSView.this);
                if (shell != null) {
                    shell.close();
                }
            }
        });
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setProjectId(parentId);
        filterParameter.setLimit(1);
        initProjectWBSLists(filterParameter, null, null, container);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        MainLayout.get().considerBodyHasOperPanel(true);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        MainLayout.get().considerBodyHasOperPanel(false);
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
