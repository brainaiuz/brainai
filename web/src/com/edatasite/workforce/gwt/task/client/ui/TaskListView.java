package com.edatasite.workforce.gwt.task.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.localization.Reference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportMPPFilePopup;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.TaskPermissionEnum;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.ui.view.kanban.TaskMaterialCard;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoard;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoardDesign;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanDataLoader;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanDataRenderer;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanService;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.edatasite.workforce.gwt.workstream.client.ui.WorkstreamChooser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Paragraph;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Jan 11, 2008 Time: 9:15:25 PM To
 * change this template use File | Settings | File Templates.
 */

public class TaskListView extends BaseListView implements Constants {
    protected static final CoreMessages coreMessages = CoreMessages.App.get();
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final Reference reference = Reference.App.get();
    protected final ProjectStrings projectStrings = ProjectStrings.App.get();
    protected final TaskServiceAsync taskService = TaskService.App.get();
    protected final AllInOneServiceAsync allInOneService = AllInOneService.App.get();

    protected ListingPanel<TaskListItem> listingTable;
    protected ContextMenu actions;
    protected ContextMenu actionsEmpty;
    protected Integer relationID;
    protected String relationType;
    protected String relationName;
    protected boolean hasAccessToChange = true;
    protected int actionItemCount;
    boolean hasChild = false;
    private HashSet<TaskListItem> selectedRows;
    private int totalCount = 0;
    private Boolean atLeastOneTimerIsRunning = false;
    private String relatedContactName;
    private String relatedAccountName;
    private final HashMap<Integer, HashSet<String>> permissionMap = new HashMap<>();
    protected boolean isFromCase;
    private ChangeProjectsPopup changeProjectsPopup;
    //private SelectItem[] projectWorkstreamList;

    public TaskListView() {
        super(TASK_LIST);
        setDescription(property.getPlural(wfmStrings.tasks(), wfmStrings.task()));
        if (hasQuickAddPermission()) {
            setAddNew(() -> {
                Integer temp = null;
                new TaskQuickAddView(null, temp, Utils.isCRM());
            });
        }
    }

    protected boolean hasPermissionToAdd() {
        return Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_ADD : PermissionConstants.CRM_TASKS_ADD);
    }

    private boolean hasQuickAddPermission() {
        return Utils.hasPermission(PermissionConstants.PM_TASKS_QUICK_ADD);
    }

    public ListingFilterParameter getFiterParametrs() {
        return new ListingFilterParameter();
    }

    public FlowPanel getHelpContainer() {
        if (helpPanel == null) {
            helpPanel = HelpPanelGenerator.getHelpPanel(PermissionConstants.PM_CONTEXT, PermissionConstants.PM_TASKS_LIST);
        }
        return helpPanel;
    }

    protected Widget onInitialize() {
        onInit();
        return null;
    }

    private Widget onInit() {
        listingTable = new GuideListingPanel(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        if (canEditCustomFields()) {
            listingTable.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveTaskEditCellValue((TaskListItem) rowValue, columnCodeName));
        }
        initExporters();
        if (relationName == null) {
            getRelationName(relationID, relationType);
        }
        if (getFiterParametrs() != null) {
            if (!RelationItem.TYPE_CONTACT.equals(getFiterParametrs().getRelationType()) && !RelationItem.TYPE_LEAD.equals(getFiterParametrs().getRelationType())) {
                getRelationName(getFiterParametrs().getContactID(), RelationItem.TYPE_CONTACT);
            }
            if (!RelationItem.TYPE_CRM_ACCOUNT.equals(getFiterParametrs().getRelationType())) {
                getRelationName(getFiterParametrs().getAccountID(), RelationItem.TYPE_CRM_ACCOUNT);
            }
        }
        initListeners();
        add(listingTable);

        changeProjectsPopup = new ChangeProjectsPopup();
        changeProjectsPopup.setListRefresh(() -> listingTable.reloadPage());

        KanbanBoard<TaskListItem> kanbanBoard = new KanbanBoard<TaskListItem>(ListPanelType.TaskKanbanPanel, getKanbanDataLoader(), getKanbanBoardDesign()) {
            @Override
            public Widget getColumnAddButton(SelectItem columnMetadata) {
                if (hasQuickAddPermission()) {
                    MaterialLink addTaskLink = new MaterialLink();
                    addTaskLink.setStyleName("wg_canban__add-card");
                    Icon plus = new Icon();
                    plus.setStyleName("ficon--plus");
                    addTaskLink.add(plus);
                    Integer projectId = getFiterParametrs() != null ? getFiterParametrs().getProjectId() : null;
                    addTaskLink.addClickHandler(click -> new TaskQuickAddView(projectId, columnMetadata.getId(), Utils.isCRM()));
                    return addTaskLink;
                }
                return super.getColumnAddButton(columnMetadata);
            }
        };
        kanbanBoard.setKanbanItemSettingsType(KanbanItemSettingEnum.TASK_ITEM_SETTINGS);
        listingTable.setKanbanBoardView(kanbanBoard);
        return null;
    }

    protected boolean canEditCustomFields() {
        return true;
    }

    protected boolean hasAdditionalInformation() {
        return true;
    }

    protected void initExporters() {
        final boolean hasPermission = Utils.hasPermission(PermissionConstants.PM_TASKS_PDF_EXCEL_EXPORT);
        listingTable.setExcelListener(clickEvent -> {
            if (hasPermission) {
                String excelURL = CommandConstants.COMMON_URL + "/downloadTaskListExcel";
                ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
                if (getFiterParametrs() != null) {
                    if (getFiterParametrs().getProjectId() != null) {
                        filterParametrs.setProjectId(getFiterParametrs().getProjectId());
                    }
                    if (getFiterParametrs().getDepartmentId() != null) {
                        filterParametrs.setDepartmentId((getFiterParametrs().getDepartmentId()));
                    }
                    filterParametrs.setPropertyCode(getPropertyCode());
                    filterParametrs.setRelationID(getFiterParametrs().getRelationID());
                    filterParametrs.setRelationType(getFiterParametrs().getRelationType());
                    filterParametrs.setCrmTaskList(getFiterParametrs().isCrmTaskList());
                }
                listingTable.callListExcel(excelURL, filterParametrs);
            } else {
                LoadingPanel.loading(false);
                Info.show("You don't have enough permissions", Info.Type.WARNING);
            }
        });
        listingTable.setPDFListener(clickEvent -> {
            if (hasPermission) {
                if (totalCount > 1000) {
                    Window.alert(wfmStrings.CurrentlyLimitedContactExport());
                }
                String pdfURL = CommandConstants.PDF_URL + "/taskListPDFHandler";
                ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
                if (getFiterParametrs() != null) {
                    if (getFiterParametrs().getProjectId() != null) {
                        filterParametrs.setProjectId(getFiterParametrs().getProjectId());
                    }
                    if (getFiterParametrs().getDepartmentId() != null) {
                        filterParametrs.setDepartmentId((getFiterParametrs().getDepartmentId()));
                    }
                    filterParametrs.setRelationID(getFiterParametrs().getRelationID());
                    filterParametrs.setRelationType(getFiterParametrs().getRelationType());
                    filterParametrs.setCrmTaskList(getFiterParametrs().isCrmTaskList());
                    filterParametrs.setPropertyCode(getPropertyCode());
                }
                listingTable.callListPDF(pdfURL, filterParametrs);
            } else {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        });
        listingTable.addSelectionRowHandler(selected -> selectedRows = selected);
    }

    protected void initListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, TaskListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_EDIT, TaskListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_DELETE, TaskListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKSTREAM_DELETED, TaskListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_MEMBERS_EDIT, TaskListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_TASK_STATUS_CHANGED, TaskListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_LIST_EDIT_CELL, TaskListView.this, (sender, args) -> refresh());
    }

    protected void refresh() {
        if (listingTable.isListingPage()) {
            listingTable.reloadPage();
        } else {
            listingTable.requestKanbanData();
        }
    }

    protected ColumnDefinitionConfig[] getColumnConfigs() {
        boolean taskEditPermission = Utils.hasRoles(DR, ADMIN, PM, HR, TL);
        boolean taskEditPermission2 = Utils.hasPermission(Utils.isCRM() ? PermissionConstants.CRM_TASKS_EDIT : PermissionConstants.PM_TASKS_EDIT);
        ColumnDefinitionConfig[] columnConfigs = new ColumnDefinitionConfig[!taskEditPermission ? 39 : 41];
        int i = 0;
        columnConfigs[i] = new ColumnDefinitionConfig<TaskListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final TaskListItem rowValue) {
                actionItemCount = 0;
                hasAccessToChange = !Utils.isLockCompletedProjecItems() || !PS_CLOSED.equals(rowValue.getProjectStatusCode());
                boolean deletePermission = (Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_REMOVE : PermissionConstants.CRM_TASKS_REMOVE));
                boolean editPermission = (Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_EDIT : PermissionConstants.CRM_TASKS_EDIT));

                MenuBar menuBar = new MenuBar(true);
                final MenuPopItem taskSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                taskSummary.ensureDebugId("taskView");
                taskSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + rowValue.getObjectID() + "/" + hasAccessToChange, rowValue.getNumber(), rowValue.getName()));
                actionItemCount++;
                menuBar.addItem(taskSummary);
                //}
                //boolean isAuditorToWissamStouhi = true;

                final MenuPopItem taskEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                taskEdit.ensureDebugId("editTask");
                taskEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("task|edit/" + rowValue.getObjectID(), rowValue.getNumber(), rowValue.getName()));

                if (hasAccessToChange && editPermission) {
                    actionItemCount++;
                    menuBar.addItem(taskEdit);
                }

                final MenuPopItem timer = new MenuPopItem(wfmStrings.timer(), rowValue.timerIsStarted() ? "icon-clock-active" : "icon-clock");
                if (rowValue.isShowTimer() && hasAccessToChange && Utils.hasGenericAccess(GenericSettingsEnum.SHOW_TIMER)) {
                    if (!atLeastOneTimerIsRunning ||
                            "true".equals(Utils.userSettings.get(ENABLE_MULTIPLE_TIMER_INTSTANCES)) ||
                            ("false".equals(Utils.userSettings.get(ENABLE_MULTIPLE_TIMER_INTSTANCES)) && rowValue.timerIsStarted())) {
                        timer.ensureDebugId("wfmTimer");
//                        timer.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("wfmTimer|summary/" + rowValue.getObjectID().toString() + "/" + rowValue.getProjectId().toString()));
                        timer.setCommand(() -> MainLayout.get().setTimerData(rowValue.getObjectID(), Constants.PM_TASK, rowValue.getProjectId()));
                        actionItemCount++;
                        menuBar.addItem(timer);
                    }
                }
                // Copy Task
                if (Utils.hasPermission(PermissionConstants.COPY_TASK)) {
                    if (hasAccessToChange) {
                        final MenuPopItem copyTask = new MenuPopItem(wfmStrings.copy(), "list-action-menu-icon icon-copy");
                        copyTask.ensureDebugId("copyTask");
                        copyTask.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("task|add/add/copytask/" + rowValue.getObjectID().toString()));
                        actionItemCount++;
                        menuBar.addItem(copyTask);
                    }
                }

                final MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                removeItem.ensureDebugId("delete");
                removeItem.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            String context = PermissionConstants.PM_CONTEXT;
                            if (getFiterParametrs() != null && getFiterParametrs().isCrmTaskList()) {
                                context = PermissionConstants.CRM_CONTEXT;
                            }
                            taskService.deleteTask(rowValue.getObjectID(), context, new AbstractAsyncCallback<String>() {
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(String result) {
                                    LoadingPanel.loading(false);

                                    if (USED_IN_INVOICE.equals(result)) {
                                        Info.show(property.getSingular(wfmStrings.thisIsInvoicedTask(), wfmStrings.task()) + property.getSingular(wfmStrings.isInvoicedYouCannotDelete(), wfmStrings.task()), Info.Type.WARNING);
                                    } else if (PermissionConstants.DENY.equals(result)) {
                                        Info.show(property.getSingular(projectStrings.youDonTHaveEnoughPermissionToDeleteThisTask(), wfmStrings.task()), Info.Type.WARNING);
                                    } else {
                                        Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.task()), Info.Type.INFO);
                                        listingTable.reloadPage();
                                    }
                                }
                            });
                        }
                    });
                    message.open();
                });

                if (hasAccessToChange && deletePermission) {
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);


                Anchor anchor = toolItem.getAction();
                anchor.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {

                        if (Utils.hasRole(ADMIN)) {
                            return;
                        }
                        taskEdit.setVisible(false);
                        timer.setVisible(false);
                        removeItem.setVisible(false);
                        String context = PermissionConstants.PM_CONTEXT;

                        if (Utils.isCRM()) {
                            context = PermissionConstants.CRM_CONTEXT;
                        }
                        final Integer objectID = rowValue.getObjectID();
                        if (permissionMap.containsKey(objectID)) {
                            Utils.setUserPermissions(permissionMap.get(objectID));
                            showContextMenu();
                        } else {
                            TaskService.App.get().getPermissions(objectID, context, new AsyncCallback<HashSet<String>>() {
                                @Override
                                public void onFailure(Throwable throwable) {

                                }

                                @Override
                                public void onSuccess(HashSet<String> result) {
                                    Utils.setUserPermissions(result);
                                    showContextMenu();
                                    permissionMap.put(objectID, result);
                                }
                            });
                        }
                    }

                    private void showContextMenu() {
                        taskEdit.setVisible(Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_EDIT : PermissionConstants.CRM_TASKS_EDIT));
                        timer.setVisible(Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_TIMER : PermissionConstants.CRM_TASKS_TIMER));
                        removeItem.setVisible(Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_REMOVE : PermissionConstants.CRM_TASKS_REMOVE));
                    }
                });
                return anchor;
            }
        };
        columnConfigs[i].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs[i].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs[i].setColumnSortable(false);
        // Task Number
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, Widget>(wfmStrings.number(), TaskListItem.NUMBER, 60) { //1
            @Override
            public Widget getCellValue(TaskListItem item) {
                Label label = new Label(item.getNumber());
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + item.getObjectID(), item.getNumber(), item.getName()));
                return label;
            }
        };
        columnConfigs[i].setMinimumColumnWidth(20);

        // Task Name
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, SimpleLink>(wfmStrings.name(), TaskListItem.NAME, 140) {//2
            @Override
            public SimpleLink getCellValue(TaskListItem rowValue) {
                hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(rowValue.getProjectStatusCode()));
                return new SimpleLink(rowValue.getName(), "task|summary/" + rowValue.getObjectID() + "/" + hasAccessToChange, rowValue.getName(), rowValue.getNumber());
            }
        };
        columnConfigs[i].setMinimumColumnWidth(135);
        columnConfigs[i].setShowPopup(true);

        // Project Name
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.PROJECT, wfmStrings.project()), TaskListItem.PROJECT_NAME, 120) {//5

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getProjectName();
            }
        };
        columnConfigs[i].setMinimumColumnWidth(115);


        // Start Date
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.startDate(), TaskListItem.START_DATE, 100) {  //13

            @Override
            public String getCellValue(TaskListItem rowValue) {
                if (rowValue.isAllDay() != null && rowValue.isAllDay()) {
                    return DateUtils.format(rowValue.getStartDate());
                } else {
                    return DateUtils.formatInternal(rowValue.getStartDate());
                }
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                try {
                    if ((cellValue.contains("AM") || cellValue.contains("PM") || cellValue.contains(":"))
                            && rowValue.getDueDate() != null && rowValue.getDueDate().getTime() >= DateUtils.parseLongFormat(cellValue).getTime()) {
                        rowValue.setStartDate(DateUtils.parseLongFormat(cellValue));
                        rowValue.setAllDay(false);
                        saveCellValue(rowValue);
                    } else if (!cellValue.contains("AM") && !cellValue.contains("PM") && !cellValue.contains(":")
                            && rowValue.getDueDate() != null && rowValue.getDueDate().getTime() >= DateUtils.parse(cellValue).getTime()) {
                        rowValue.setStartDate(DateUtils.parse(cellValue));
                        rowValue.setAllDay(true);
                        saveCellValue(rowValue);
                    } else {
                        Info.show(projectStrings.endDateCanNotBeBeforeStartDate(), Info.Type.WARNING);
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        columnConfigs[i].setMinimumColumnWidth(90);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        // End Date
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.dueDate(), TaskListItem.DUE_DATE, 100) {//14

            @Override
            public String getCellValue(TaskListItem rowValue) {
                if (rowValue.isAllDay() != null && rowValue.isAllDay()) {
                    return DateUtils.format(rowValue.getDueDate());
                } else {
                    return DateUtils.formatInternal(rowValue.getDueDate());
                }
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                try {
                    if ((cellValue.contains("AM") || cellValue.contains("PM") || cellValue.contains(":"))
                            && rowValue.getStartDate() != null && rowValue.getStartDate().getTime() <= DateUtils.parseLongFormat(cellValue).getTime() + 5 * 60 * 60 * 1000) {//add 5 hours hack
                        rowValue.setDueDate(DateUtils.parseLongFormat(cellValue));
                        rowValue.setAllDay(false);
                        saveCellValue(rowValue);
                    } else if (!cellValue.contains("AM") && !cellValue.contains("PM") && !cellValue.contains(":")
                            && rowValue.getStartDate() != null && rowValue.getStartDate().getTime() <= DateUtils.parse(cellValue).getTime()) {
                        rowValue.setDueDate(DateUtils.parse(cellValue));
                        rowValue.setAllDay(true);
                        saveCellValue(rowValue);
                    } else {
                        Info.show(projectStrings.endDateCanNotBeBeforeStartDate(), Info.Type.WARNING);
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        columnConfigs[i].setMinimumColumnWidth(90);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        // Duration in Days (Total Duration)
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.duration(), TaskListItem.DURATION, 100) {

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.calculateDueDays() + " " + wfmStrings.days();
            }

        };
        columnConfigs[i].setMinimumColumnWidth(90);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        // Assign To
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.assignees(), TaskListItem.ASSIGNED_TO, 150) {//7

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getAssignedTo();
            }
        };
        columnConfigs[i].setMinimumColumnWidth(120);
        columnConfigs[i].setColumnSortable(false);

        if (taskEditPermission) {
            columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.overAllStatus(), TaskListItem.OVERALL_STATUS_NAME, 100) {
                @Override
                public String getCellValue(TaskListItem item) {
                    return item.getOverallStatusName();
                }

                @Override
                public void setCellValue(TaskListItem rowValue, String cellValue) {
                    if (rowValue.getPermissions().hasPermission(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode())) {
                        rowValue.setOverallStatusName(cellValue);
                        rowValue.setStatusName(cellValue);
                        saveCellValue(rowValue);
                    } else {
                        Info.show(projectStrings.youDontHaveEnoughPrivilegesToChangeStatus(), Info.Type.WARNING);
                    }
                }
            };
            columnConfigs[i].setShow(false);
            columnConfigs[i].setMinimumColumnWidth(40);
            columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        }

        // Task Description
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.description(), TaskListItem.DESCRIPTION, 150) {//3

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getDescription();
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                rowValue.setDescription(cellValue);
                saveCellValue(rowValue);
            }
        };
        columnConfigs[i].setMinimumColumnWidth(120);
        columnConfigs[i].setShow(false);
        // Client Name
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), TaskListItem.CLIENT, 120) {//4

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getClient();
            }
        };
        columnConfigs[i].setMinimumColumnWidth(80);
        columnConfigs[i].setShow(false);

        // Project Number
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.number(), TaskListItem.PROJECT_NUMBER, 60) {  //6
            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getProjectNumber();
            }
        };
        columnConfigs[i].setMinimumColumnWidth(40);
        columnConfigs[i].setShow(false);

        // Priority
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.priority(), TaskListItem.PRIORITY_NAME, 80) { //8
            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getPriorityName();
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                rowValue.setPriorityName(cellValue);
                saveCellValue(rowValue);
            }
        };
        columnConfigs[i].setMinimumColumnWidth(40);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs[i].setShow(false);

        // Assignee Status
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, SelectItem>(projectStrings.asigneeStatus(), TaskListItem.STATUS_NAME, 80) {   //9

            @Override
            public SelectItem getCellValue(TaskListItem rowValue) {
                return rowValue.getStatus();
            }

            @Override
            public void setCellValue(TaskListItem rowValue, SelectItem cellValue) {
                if (rowValue.getPermissions().hasPermission(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode()) || taskEditPermission2) {
                    rowValue.setStatus(cellValue);
                    if (Utils.isDoubleMessageEnable()) {
                        WfmMessageBox changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        changeStatusMessageBox.setMessage(wfmMessages.doYouWantToChangeStatusTo(cellValue.getName()));
                        changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                saveCellValue(rowValue);
                            }

                            @Override
                            public void onCancel() {
                                listingTable.reloadPage();
                            }
                        });
                        changeStatusMessageBox.setTitle(wfmStrings.warning());
                        changeStatusMessageBox.open();
                    } else {
                        saveCellValue(rowValue);
                    }
                } else {
                    Info.show(projectStrings.youDontHaveEnoughPrivilegesToChangeStatus(), Info.Type.WARNING);
                }
            }
        };
        columnConfigs[i].addColor(new ColumnColor(reference.WAITING_FOR_SOMEONE_ELSE(), "r", "2BBF57"));
        columnConfigs[i].addColor(new ColumnColor(reference.COMPLETED(), "r", "007DE7"));
        columnConfigs[i].addColor(new ColumnColor(reference.NOT_STARTED(), "c", "DC0C0C"));
        columnConfigs[i].setShow(true);
        columnConfigs[i].setMinimumColumnWidth(40);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        int assigneeStatusRowIndex = i;


        // Completed
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.percent(), TaskListItem.COMPLETE, 80) {     //10

            @Override
            public String getCellValue(TaskListItem rowValue) {
                if (rowValue.getComplete() == null || "".equals(rowValue.getComplete()) || "null".equals(rowValue.getComplete()) || "0.0".equals(rowValue.getComplete())) {
                    return "0.00%";
                } else {
                    BigDecimal bigDecimal = new BigDecimal(rowValue.getComplete());
                    return bigDecimal.setScale(2, RoundingMode.HALF_UP) + "%";
                }
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                if (rowValue.getPermissions().hasPermission(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode())) {
                    String value = cellValue.replace("%", "");
                    if (Double.valueOf(value) > 100.00 && !Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                        cellValue = "100";
                    } else {
                        cellValue = value;
                    }
                    rowValue.setComplete(cellValue);
                    saveCellValue(rowValue);
                } else {
                    Info.show(projectStrings.youDontHaveEnoughPrivilegesToChangeComplete(), Info.Type.WARNING);
                }
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(40);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        // Actual Hour spent
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.actualTimeSpent(), TaskListItem.ACTUAL_HOURS_SPENT, 80) {//11

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getActualHoursSpent();
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(75);
        columnConfigs[i].setColumnSortable(false);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        // Hour spent
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.timeSpentOnly(), TaskListItem.HOUR_SPENT, 80) {//12

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getHoursSpent();
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(75);
        columnConfigs[i].setColumnSortable(false);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.startDate(), TaskListItem.ACTUAL_START_DATE, 100) {//15

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return DateUtils.format(rowValue.getActualStartDate());
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(100);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(projectStrings.completedDateActual(), TaskListItem.END_DATE, 100) {//16

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return DateUtils.format(rowValue.getEndDate());
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(100);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, Widget>(wfmStrings.estimatedTime(), TaskListItem.ESTIMATED, 110) {//17
            @Override
            public Widget getCellValue(final TaskListItem rowValue) {
                SimpleLink anchor = new SimpleLink(Utils.formatMinutes(rowValue.getEstimated()));
                anchor.getElement().getStyle().setTextDecoration(Style.TextDecoration.NONE);
                anchor.addDoubleClickHandler(doubleClickEvent -> {
                    if (Utils.hasPermission(PermissionConstants.PM_ADD_ASSIGNEES_TO_PROJECT)) {
                        TaskEstimatePopup estimatePopup = new TaskEstimatePopup(rowValue.getObjectID());
                        estimatePopup.open();
                    }

                });
                return anchor;
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(105);
        columnConfigs[i].setColumnSortable(false);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        // Created By
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.createdBy(), TaskListItem.CREATED_BY, 110) {//18

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getCreatedBy();
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(105);
        columnConfigs[i].setColumnSortable(false);

        // Last Update User
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.modifiedBy(), TaskListItem.LAST_MODIFIED_BY, 110) {

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getLastModifiedBy();
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(105);
        columnConfigs[i].setColumnSortable(false);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.modifiedDate(), TaskListItem.LAST_MODIFIED, 110) {

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return DateUtils.formatInternal(rowValue.getLastModified());
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(105);

        // Created date
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.createdDate(), TaskListItem.CREATION_DATE, 110) {
            @Override
            public String getCellValue(TaskListItem rowValue) {
                return DateUtils.formatInternal(rowValue.getCreationDate());
            }
        };

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.workStream(), TaskListItem.PARENT_WORKSTREAM_NAME, 100) {

            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getParentWorkstreamName();
            }

            /*@Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                rowValue.setParentWorkstreamName(cellValue);
                saveCellValue(rowValue);
            }*/
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);

        //Project Manager
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.projectManager(), TaskListItem.PROJECT_MANAGER_NAME, 100) {
            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getProjectManagerName();
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(90);

        //task billable option
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.billable(), TaskListItem.BILLABLE, 60) {
            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.isBillable() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(50);
        columnConfigs[i].setColumnSortable(false);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //related to columns
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.Contacts, crmStrings.relatedContact(), wfmStrings.contact()), RelationItem.TYPE_CONTACT, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CONTACT);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.LEADS, wfmStrings.relatedLead(), wfmStrings.lead()), RelationItem.TYPE_LEAD, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_LEAD);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.relatedCrmAccount(), RelationItem.TYPE_CRM_ACCOUNT, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.CASE_LIST, crmStrings.relatedCase(), wfmStrings.crmCase()), RelationItem.TYPE_CASE, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CASE);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.Opportunities, wfmStrings.relatedToOpportunity(), wfmStrings.opportunity()), RelationItem.TYPE_OPPORTUNITY, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.EVENT_LIST, wfmStrings.relatedEvent(), wfmStrings.event()), RelationItem.TYPE_EVENT, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_EVENT);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.PROJECT, wfmStrings.relatedToProject(), wfmStrings.project()), RelationItem.TYPE_PROJECT, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_PROJECT);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);
        //related issue
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.ISSUE, wfmStrings.relatedIssue(), wfmStrings.issue()), RelationItem.TYPE_ISSUE, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_ISSUE);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);
        //related employee
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.relatedEmployee(), RelationItem.TYPE_EMPLOYEE, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);
        //related department
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.relatedDepartment(), wfmStrings.department()), RelationItem.TYPE_DEPARTMENT, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);
        //related client
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.relatedClient(), wfmStrings.customer()), TaskListItem.TASK_RELATED_CLIENT, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CLIENT);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);
        //related supplier
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.relatedSupplier(), wfmStrings.supplier()), RelationItem.TYPE_SUPPLIER, 100) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(95);
        columnConfigs[i].setColumnSortable(false);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.waitingHours(), TaskListItem.WAITING_HOURS, 90) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getWaitingHours();
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(85);
        columnConfigs[i].setColumnSortable(false);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(projectStrings.rejectedHours(), TaskListItem.REJECTED_HOURS, 90) {
            @Override
            public String getCellValue(TaskListItem item) {
                return item.getRejectedHours();
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(85);
        columnConfigs[i].setColumnSortable(false);

        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(property.getSingular(wfmStrings.taskAmount(), wfmStrings.task()), TaskListItem.TASK_AMOUNT, 90) {
            @Override
            public String getCellValue(TaskListItem item) {
                return Utils.formatDouble(item.getTaskAmount() != null ? item.getTaskAmount().doubleValue() : 0d);
            }
        };
        columnConfigs[i].setShow(false);
        columnConfigs[i].setMinimumColumnWidth(85);
        columnConfigs[i].setColumnSortable(false);
        columnConfigs[i].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        //Task Type
        columnConfigs[++i] = new ColumnDefinitionConfig<TaskListItem, String>(wfmStrings.type(), TaskListItem.TYPE_NAME, 80) { //8
            @Override
            public String getCellValue(TaskListItem rowValue) {
                return rowValue.getTypeName();
            }

            @Override
            public void setCellValue(TaskListItem rowValue, String cellValue) {
                rowValue.setTypeName(cellValue);
                saveCellValue(rowValue);
            }
        };
        columnConfigs[i].setMinimumColumnWidth(40);
        columnConfigs[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs[i].setShow(false);

        initCellEdit(columnConfigs, taskEditPermission, taskEditPermission2, assigneeStatusRowIndex);
        return columnConfigs;
    }

    private void initCellEdit(ColumnDefinitionConfig[] columnConfigs, boolean taskEditPermission, boolean taskEditPermission2, int assigneeStatusRowIndex) {
        if (taskEditPermission) {
            // Task Priority Cell Edit
            final DropDownCellEditor<String> priorityCellEditor = new DropDownCellEditor<String>(80) {
                @Override
                protected String getValue() {
                    return getListBox().getSelectedItem().getName();
                }

                @Override
                protected void setValue(String cellValue) {
                    getListBox().setSelectedByValue(cellValue);
                }
            };
            priorityCellEditor.getListBox().setWithoutNullLabel(true);
            columnConfigs[11].setCellEditor(priorityCellEditor);
            columnConfigs[11].setCellChangesSave((CellChange<TaskListItem>) (rowValue, columnCodeName) -> {
                rowValue.setPriorityId(priorityCellEditor.getSelectItem().getId());
                saveTaskEditCellValue(rowValue, columnCodeName);
            });

            // Task Assignee Status Cell Editor
            final DropDownCellEditor<SelectItem> statusCellEditor = new DropDownCellEditor<SelectItem>() {
                @Override
                protected SelectItem getValue() {
                    SelectItem item = getListBox().getSelectedItem();
                    return item;
                }

                @Override
                protected void setValue(SelectItem cellValue) {
                    getListBox().setWithoutNullLabel(true);
                    getListBox().setSelectedIndex(0);
                    if (cellValue == null || cellValue.getId() == null) {
                        if (cellValue != null && cellValue.getName() != null) {
                            getListBox().setSelectedByValue(cellValue.getName());
                        } else {
                            getListBox().setSelectedNullLabel();
                        }
                    } else {
                        getListBox().setSelected(cellValue.getId());
                    }
                }
            };
            statusCellEditor.getListBox().setWithoutNullLabel(true);
            columnConfigs[assigneeStatusRowIndex].setCellEditor(statusCellEditor);
            columnConfigs[assigneeStatusRowIndex].setCellChangesSave((CellChange<TaskListItem>) (rowValue, columnCodeName) -> {
                rowValue.setTaskStatusId(statusCellEditor.getSelectItem().getId());
                saveTaskEditCellValue(rowValue, columnCodeName);
            });

            if (!Utils.userSettings.get(ISAUTOMATIC).equals("true")) {
                //Task Percent Cell Editor
                final TextBoxCellEditor<String> percentCellEditor = new TextBoxCellEditor<String>(80) {
                    @Override
                    protected String getValue() {
                        return getText();
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        setText(cellValue);
                    }
                };
                percentCellEditor.addNumberValidation(false);
                columnConfigs[13].setCellEditor(percentCellEditor);
                columnConfigs[13].setCellChangesSave((CellChange<TaskListItem>) (rowValue, columnCodeName) -> saveTaskEditCellValue(rowValue, columnCodeName));

            }

            // Task StartDate Cell Edit
            DateTimePickerCellEditor<String> startDateTimePickerCellEditor = new DateTimePickerCellEditor<String>() {
                @Override
                protected String getValue() {
                    return !getDateTimePicker().isAllDay() ? DateUtils.formatInternal1(getDate()) : DateUtils.format1(getDate());
                }

                @Override
                protected void setValue(String cellValue) {
                    if (cellValue.contains("AM") || cellValue.contains("PM") || cellValue.contains(":")) {
                        try {
                            setDate(DateUtils.parseLongFormat(cellValue), true);
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            setDate(DateUtils.parse(cellValue), false);
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            columnConfigs[4].setCellEditor(startDateTimePickerCellEditor);
            columnConfigs[4].setCellChangesSave((CellChange<TaskListItem>) (rowValue, columnCodeName) -> saveTaskEditCellValue(rowValue, columnCodeName));

            // Task DueDate Cell Edit
            DateTimePickerCellEditor<String> duetDateTimePickerCellEditor = new DateTimePickerCellEditor<String>() {
                @Override
                protected String getValue() {
                    return (!getDateTimePicker().isAllDay() ? DateUtils.formatInternal1(getDate()) : DateUtils.format1(getDate()));
                }

                @Override
                protected void setValue(String cellValue) {
                    if (cellValue.contains("AM") || cellValue.contains("PM") || cellValue.contains(":")) {
                        try {
                            setDate(DateUtils.parseLongFormat(cellValue), true);
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            setDate(DateUtils.parse(cellValue), false);
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            columnConfigs[5].setCellEditor(duetDateTimePickerCellEditor);
            columnConfigs[5].setCellChangesSave((CellChange<TaskListItem>) (rowValue, columnCodeName) -> saveTaskEditCellValue(rowValue, columnCodeName));

            /*DropDownCellEditor<String> workstreamEditor;
            if (getTaskParentId() != null) {
                workstreamEditor = new DropDownCellEditor<String>() {
                    @Override
                    protected String getValue() {
                        if (getListBox().getSelectedIndex() != 0) {
                            return getListBox().getSelectedItem().getName();
                        }
                        return null;
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        getListBox().setSelectedByValue(cellValue);
                    }
                };

                final DropDownCellEditor<String> workstreamCellEditor = workstreamEditor;
                columnConfigs[22].setCellEditor(workstreamEditor);
                columnConfigs[22].setCellChangesSave((CellChange<TaskListItem>) (rowValue, columnCodeName) -> {
                    if (workstreamCellEditor.getSelectItem() != null) {
                        rowValue.setParentWorkstreamId(workstreamCellEditor.getSelectItem().getId());
                        boolean t = false;
                        String workstream = workstreamCellEditor.getSelectItem().getName();
                        while (!t) {
                            if (workstream.contains("-")) {
                                workstream = workstream.substring(1);
                            } else {
                                t = true;
                            }
                        }
                        rowValue.setParentWorkstreamName(workstream);
                    } else {
                        rowValue.setParentWorkstreamId(null);
                    }
                    saveTaskEditCellValue(rowValue, columnCodeName);
                });
                workstreamEditor.getListBox().setItems(projectWorkstreamList);
            }*/

            DropDownCellEditor<String> overalStatusCellEditor = new DropDownCellEditor<String>() {
                @Override
                protected String getValue() {
                    return getListBox().getSelectedItem().getName();
                }

                @Override
                protected void setValue(String cellValue) {
                    getListBox().setSelectedByValue(cellValue);
                }
            };

            overalStatusCellEditor.getListBox().setWithoutNullLabel(true);
            final DropDownCellEditor<String> finalOveralStatusCellEditor = overalStatusCellEditor;

            columnConfigs[7].setCellEditor(overalStatusCellEditor);
            columnConfigs[7].setCellChangesSave((CellChange<TaskListItem>) (rowValue, columnCodeName) -> {
                rowValue.setTaskStatusId(finalOveralStatusCellEditor.getSelectItem().getId());
                saveTaskEditCellValue(rowValue, columnCodeName);
            });


            // Get Priority Cell List
            taskService.getPriorities(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(SelectItem[] result) {
                    priorityCellEditor.getListBox().setItems(result);
                }
            });

            // Get Status Cell List
            final DropDownCellEditor<String> finalOveralStatusCellEditor1 = overalStatusCellEditor;

            CommonService.App.get().getAddTaskStatusDrop(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    statusCellEditor.getListBox().setItems(result);
                    finalOveralStatusCellEditor1.getListBox().setItems(result);
                }
            });

            DropDownCellEditor<String> taskTypeCellEditor = new DropDownCellEditor<String>() {
                @Override
                protected String getValue() {
                    return getListBox().getSelectedItem().getName();
                }

                @Override
                protected void setValue(String cellValue) {
                    getListBox().setSelectedByValue(cellValue);
                }
            };

            taskTypeCellEditor.getListBox().setWithoutNullLabel(true);
            final DropDownCellEditor<String> finalTaskTypeCellEditor = taskTypeCellEditor;
            columnConfigs[40].setCellEditor(taskTypeCellEditor);
            columnConfigs[40].setCellChangesSave((CellChange<TaskListItem>) (rowValue, columnCodeName) -> {
                rowValue.setTypeId(finalTaskTypeCellEditor.getSelectItem().getId());
                saveTaskEditCellValue(rowValue, columnCodeName);
            });
            // Get Priority Cell List
            taskService.getTaskTypes(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(SelectItem[] result) {
                    taskTypeCellEditor.getListBox().setItems(result);
                }
            });

        } else if (taskEditPermission2) {
            // Task Status Cell Editor
            DropDownCellEditor<String> statusCellEditor = new DropDownCellEditor<String>() {
                @Override
                protected String getValue() {
                    return getListBox().getSelectedItem().getName();
                }

                @Override
                protected void setValue(String cellValue) {
                    getListBox().setSelectedByValue(cellValue);
                }
            };
            statusCellEditor.getListBox().setWithoutNullLabel(true);
            columnConfigs[assigneeStatusRowIndex].setCellEditor(statusCellEditor);
            columnConfigs[assigneeStatusRowIndex].setCellChangesSave((CellChange<TaskListItem>) (rowValue, columnCodeName) -> {
                rowValue.setTaskStatusId(statusCellEditor.getSelectItem().getId());
                saveTaskEditCellValue(rowValue, columnCodeName);
            });


            CommonService.App.get().getAddTaskStatusDrop(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    statusCellEditor.getListBox().setItems(result);
                }
            });

        }

    }

    private void saveTaskEditCellValue(TaskListItem rowValue, String columnCodeName) {
        boolean hasAcess = !Utils.isLockCompletedProjecItems() || !PS_CLOSED.equals(rowValue.getProjectStatusCode());
        if (!hasAcess) {
            Info.warn(wfmStrings.youDontHavePermission());
        }
        if (columnCodeName != null) {
            if (TaskListItem.STATUS_NAME.equals(columnCodeName) && rowValue.getStatus() != null && rowValue.getStatus().isSelected()) {
                new TaskChangingStatusNoteModal(rowValue.getObjectID(), rowValue.getStatus().getId(), false);
            } else {
                taskService.saveTaskEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DASHBOARD_TASK_REFRESH, result, TaskListView.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_LIST_EDIT_CELL, result, TaskListView.this);

                    }
                });
            }
        }
    }

    private void getRelationName(final Integer relationID, final String relationType) {
        if (relationID != null && relationType != null) {
            allInOneService.getRelationName(relationID, relationType, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(String result) {
                    if (result != null) {
                        if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                            if (RelationItem.TYPE_CRM_ACCOUNT.equals(TaskListView.this.relationType)) {
                                relationName = result;
                            } else {
                                relatedAccountName = result;
                            }
                        } else if (RelationItem.TYPE_CONTACT.equals(relationType)) {
                            if (RelationItem.TYPE_CONTACT.equals(TaskListView.this.relationType)) {
                                relationName = result;
                            } else {
                                relatedContactName = result;
                            }
                        } else {
                            relationName = result;
                        }
                    }
                }
            });
        }
    }

    protected GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? TaskListView.this::addNewTask : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return hasToPermissionImport() ? TaskListView.this::importNewFile : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            if (getFiterParametrs() != null) {
                                data.setCustomDataPut(FacetFilterRpc.PROJECTID, String.valueOf(getFiterParametrs().getProjectId()));
                                data.setCustomDataPut(FacetFilterRpc.EMPLOYEEID, String.valueOf(getFiterParametrs().getEmployeeId()));
                                data.setCustomDataPut(FacetFilterRpc.DEPARTMENTID, String.valueOf(getFiterParametrs().getDepartmentId()));
                            }
                            if (relationID != null) {
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_ID, relationID.toString());
                            }
                            if (relationType != null) {
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_TYPE, relationType);
                            }
                            RbacService.App.get().getTaskFacetFilterData(data, false, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc result) {
                                    callback.onSuccess(result);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                boolean hasChild = false;
                ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                MenuBar menu = new MenuBar(true);

                if (hasPermissionToAdd()) {
                    hasChild = true;

                    MenuPopItem addNewTask = new MenuPopItem(wfmStrings.task());
                    addNewTask.ensureDebugId("Task_list_add_task_button");
                    addNewTask.setScheduledCommand(() -> {
                        addNewTask();
                    });
                    menu.addItem(addNewTask);
                }
                if (hasQuickAddPermission()) {
                    MenuPopItem quickTask = new MenuPopItem(wfmStrings.quickAdd());
                    quickTask.ensureDebugId("new_task");
                    quickTask.setCommand(() -> showAddForm());
                    menu.addItem(quickTask);
                }
                if ((getFiterParametrs() == null || !getFiterParametrs().isCrmTaskList()) &&
                        (Utils.hasPermission(!Utils.isCRM() ? PermissionConstants.PM_TASKS_ADD_MULTI : PermissionConstants.CRM_TASKS_ADD_MULTI))) {

                    MenuPopItem addNewMultiTask = new MenuPopItem("<span class='list-action-menu-icon'>" + property.getSingular(wfmStrings.multiTask(), wfmStrings.task()) + "</span>");
                    addNewMultiTask.ensureDebugId("Task_list_add_multiTask_button");
                    addNewMultiTask.setCommand(() -> {
                        if (getFiterParametrs() != null && getFiterParametrs().getProjectId() != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("multitask|add/add" + "/" + getFiterParametrs().getProjectId());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("multitask|add/add");
                        }
                    });
                    menu.addItem(addNewMultiTask);
                    hasChild = true;
                }
                newItem.setMenu(menu);
                if (hasChild) {
                    return newItem;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.isCRM() ? Utils.hasPermission(PermissionConstants.CRM_TASK_LIST_MORE_BUTTON) : Utils.hasPermission(PermissionConstants.PM_TASK_LIST_FACET_FILTER_OVERALL_STATUS)) {
                    final ActionButton more = new ActionButton(ActionButton.getMoreString(), ActionButton.Type.TOOLMENU);
                    more.ensureDebugId("Task_list_more_button");
                    more.addDomHandler(event -> {
                        MenuBar menu = getActionsForSelections();
                        menu.setAutoOpen(true);
                        more.setMenu(menu);
                    }, MouseOverEvent.getType());
                    return more;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption option, MaterialDropDown menuContainer) {

                if (hasToPermissionImport()) {
                    ImportFileActionLink link = new ImportFileActionLink();
                    link.setText(projectStrings.msProjectFile());
                    link.addClickHandler(ch -> {
                        importNewFile();
                    });
                    menuContainer.add(link);
                }
                option.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(wfmStrings.noTasksText(), wfmStrings.tasks()));
                if (hasQuickAddPermission()) {
                    message.setHref(clickEvent -> showAddForm());
                    message.setTextBeforeLink(property.getPlural(wfmStrings.noTasksLink(), wfmStrings.tasks()));
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.PM_TASK_LIST_CUSTOMIZE_BUTTON);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return (Utils.adminOrDirector() || Utils.hasRole(PM));
            }

            @Override
            public Integer getTypeParentId() {
                return getTaskParentId();
            }
        };
    }

    private void importNewFile() {
        ImportMPPFilePopup imp = new ImportMPPFilePopup("/MSProjectUploadHandler");
        if (getFiterParametrs() != null) {
            imp.setProjectID(getFiterParametrs().getProjectId());
        }
    }

    private void addNewTask() {
        String historyToken = "task|add/add";
        if (getFiterParametrs() != null) {
            if (getFiterParametrs().getRelationType() != null && getFiterParametrs().getRelationID() != null) {
                historyToken += "/" + CrmConstants.CRM_TASK + "/" + getFiterParametrs().getRelationID() + "/" + getFiterParametrs().getRelationType() + "/" +
                        relationName.replace("\\/", "\\ ") + "///" + getFiterParametrs().getContactID() + "/" + relatedContactName + "/" + getFiterParametrs().getAccountID() + "/" + relatedAccountName;
            } else if (getFiterParametrs().isCrmTaskList()) {
                historyToken += "/" + CrmConstants.CRM_TASK;
            } else {
                if (getFiterParametrs().getProjectId() != null) {
                    historyToken += "/" + getFiterParametrs().getProjectId().toString();
                }
            }
        }
        SinksContainerFactory.entryPoint.onHistoryChanged(historyToken);
    }

    protected boolean hasToPermissionImport() {
        return Utils.hasPermission(PermissionConstants.PM_TASK_LIST_IMPORT_BUTTON);
    }

    private void showAddForm() {
        RelationItem eventItem = null;
        if (relationID != null) {
            eventItem = RelationItem.newEventRelation(relationType, relationID, relationName);
        }
        RelationItem contactItem = null;
        if (relatedContactName != null) {
            contactItem = RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, getFiterParametrs().getContactID(), relatedContactName);
        }
        RelationItem crmAccountItem = null;
        if (relatedAccountName != null) {
            crmAccountItem = RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, getFiterParametrs().getAccountID(), relatedAccountName);
        }

        Integer projectId = getFiterParametrs() != null ? getFiterParametrs().getProjectId() : null;
        Integer statusId = getFiterParametrs() != null ? getFiterParametrs().getStatusID() : null;

        new TaskQuickAddView(projectId, statusId, Utils.isCRM(), eventItem, contactItem, crmAccountItem);
        //new TaskQuickAddView(projectId, statusId, Utils.isCRM());
    }

    private void deleteSelection(final MenuItem removeItem) {
        if (selectedRows != null && selectedRows.size() != 0) {
            final ArrayList<Integer> taskIds = new ArrayList<>();
            for (TaskListItem item : selectedRows) {
                if (Utils.hasRole(ADMIN) || item.isPMorBackupPM() ||
                        (item.getTaskCreatorID() != null && item.getTaskCreatorID().equals(Utils.getUserID()))
                        || item.getPermissions().hasPermission(TaskPermissionEnum.DELETE.getCode())) {
                    taskIds.add(item.getObjectID());
                }
            }
            final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
            message.setTitle(wfmStrings.warning());
            message.setMessage(wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords());
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    if (!taskIds.isEmpty()) {
                        removeItem.setEnabled(false);
                        LoadingPanel.loading(true);
                        String context = PermissionConstants.PM_CONTEXT;
                        if (Utils.isCRM()) {
                            context = PermissionConstants.CRM_CONTEXT;
                        }
                        taskService.deleteTasks(taskIds, context, new AbstractAsyncCallback<String>() {
                            public void failure(Throwable throwable) {
                                removeItem.setEnabled(true);
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            public void success(String result) {
                                removeItem.setEnabled(true);
                                LoadingPanel.loading(false);
                                if (result == PermissionConstants.ALLOW) {
                                    Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.task()), Info.Type.INFO);
                                } else {
                                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                                }
                                listingTable.reloadPage();
                            }
                        });
                    } else {
                        Info.show(coreMessages.youDontHaveEnoughPermissionToSomethingSelectedTasks(wfmStrings.delete()));
                    }
                }
            });
            message.open();
        } else {
            Info.show(wfmMessages.pleaseSelectOneRow(property.getSingular(wfmStrings.task())), Info.Type.WARNING);
        }
    }

    private FacetContentConfigure getContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[0], wfmStrings.projects(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.FIELD_TASK_PROJECT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.FIELD_TASK_PROJECT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[1], Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT) ? SolrTaskRepresenter.FIELD_TASK_PROJECT_MULTI_CLIENT_ID : SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT) ? SolrTaskRepresenter.FIELD_TASK_PROJECT_MULTI_CLIENT_ID_NAME : SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[5], wfmStrings.assignees(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.FIELD_USER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.FIELD_USER_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[19], wfmStrings.manager(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.FIELD_TASK_PROJECT_MANAGER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.FIELD_TASK_PROJECT_MANAGER_ID_NAME;
            }
        });
//        if (Utils.hasPermission(PermissionConstants.PM_TASK_LIST_FACET_FILTER_OVERALL_STATUS)) {
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[3], wfmStrings.overAllStatus(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.FIELD_TASK_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.FIELD_TASK_STATUS_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
//        }
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[2], wfmStrings.workStream(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[18], projectStrings.asigneeStatus(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        //assignee task status
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[18], projectStrings.asigneeStatus(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[4], wfmStrings.priority(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.FIELD_TASK_PRIORITY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.FIELD_TASK_PRIORITY_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[6], Property.get(Constants.Contacts, crmStrings.relatedContact(), wfmStrings.contact()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CONTACT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CONTACT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[7], wfmStrings.relatedCrmAccount(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CRM_ACCOUNT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CRM_ACCOUNT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[8], Property.get(Constants.LEADS, wfmStrings.relatedLead(), wfmStrings.lead()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_LEAD;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_LEAD;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[9], Property.get(Constants.CASE_LIST, crmStrings.relatedCase(), wfmStrings.crmCase()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CASE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CASE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[10], Property.get(Constants.Opportunities, wfmStrings.relatedToOpportunity(), wfmStrings.opportunity()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_OPPORTUNITY;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_OPPORTUNITY;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[11], Property.get(Constants.PROJECT, wfmStrings.relatedToProject(), wfmStrings.project()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_PROJECT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_PROJECT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[12], Property.get(Constants.EVENT_LIST, wfmStrings.relatedToEvent(), wfmStrings.event()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EVENT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EVENT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[13], Property.get(Constants.ISSUE, wfmStrings.relatedIssue(), wfmStrings.issue()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_ISSUE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_ISSUE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[14], wfmStrings.relatedEmployee(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EMPLOYEE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EMPLOYEE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[15], Property.get(Constants.DEPARTMENT_LIST, wfmStrings.relatedDepartment(), wfmStrings.department()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_DEPARTMENT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_DEPARTMENT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[16], Property.get(Constants.CLIENT_LIST, wfmStrings.relatedClient()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CLIENT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CLIENT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[17], Property.get(Constants.SUPPLIER_LIST, wfmStrings.relatedSupplier()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SUPPLIER;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SUPPLIER;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.TaskFacetFilter.getContentCode()[20], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.FIELD_TASK_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.FIELD_TASK_TYPE_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigureDateListBox(SolrTaskRepresenter.FIELD_START_DATE, property.getSingular(projectStrings.estimatedStartDate(), wfmStrings.task()));
        contentConfigure.addContentConfigureDateListBox(SolrTaskRepresenter.FIELD_DUE_DATE, property.getSingular(projectStrings.estimatedEndDate(), wfmStrings.task()));
        contentConfigure.addContentConfigureDateListBox(SolrTaskRepresenter.FIELD_ACTUAL_START_DATE, property.getSingular(projectStrings.taskActualStartDate(), wfmStrings.task()));
        contentConfigure.addContentConfigureDateListBox(SolrTaskRepresenter.FIELD_END_DATE, property.getSingular(projectStrings.taskActualEndDate(), wfmStrings.task()));

        return contentConfigure;
    }

    protected ListingRequestProvider<TaskListItem> getListingRequestProvider() {
        return (filterParameter, callback) -> {
            if (getFiterParametrs() != null) {
                filterParameter.setProjectId(getFiterParametrs().getProjectId());
                filterParameter.setDepartmentId(getFiterParametrs().getDepartmentId());
                filterParameter.setEmployeeId(getFiterParametrs().getEmployeeId());
                filterParameter.setRelationID(getFiterParametrs().getRelationID());
                filterParameter.setRelationType(getFiterParametrs().getRelationType());
                filterParameter.setWorflowTaskList(getFiterParametrs().isWorkflowTaskList());
                filterParameter.setWorkflowID(getFiterParametrs().getWorkflowID());
                filterParameter.setFromCase(isFromCase);
            }
            initTaskList(filterParameter, callback, null);
        };
    }

    private KanbanDataLoader<TaskListItem> getKanbanDataLoader() {
        return new KanbanDataLoader<TaskListItem>() {
            @Override
            public void loadData(ListingFilterParameter filterParameter, KanbanDataRenderer<TaskListItem> dataRenderer) {
                LoadingPanel.loading(true);
                if (filterParameter != null) {
                    filterParameter.setProjectId(getFiterParametrs().getProjectId());
                    filterParameter.setDepartmentId(getFiterParametrs().getDepartmentId());
                    filterParameter.setEmployeeId(getFiterParametrs().getEmployeeId());
                    filterParameter.setRelationID(getFiterParametrs().getRelationID());
                    filterParameter.setRelationType(getFiterParametrs().getRelationType());
                    filterParameter.setWorflowTaskList(getFiterParametrs().isWorkflowTaskList());
                    filterParameter.setWorkflowID(getFiterParametrs().getWorkflowID());
                    filterParameter.setFromCase(isFromCase);
                }
                taskService.getNewKanbanTasks(filterParameter, dataRenderer.getColumnMetadata(), new AsyncCallback<ListResult<TaskListItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(ListResult<TaskListItem> result) {
                        dataRenderer.setResults(result);
                        LoadingPanel.loading(false);
                    }
                });
            }

            @Override
            public void onDropKanbanItem(Object sourceColumnLayoutData, Object targetColumnLayoutData, Object taskListItem,
                                         Integer widgetIndex, Object prevItem, Object afterItem, KanbanBoard kanbanBoard,
                                         KanbanBoard.OnDropCard onDropCard) {

                if (Utils.isDoubleMessageEnable() && targetColumnLayoutData != sourceColumnLayoutData) {
                    WfmMessageBox changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    changeStatusMessageBox.setMessage(wfmMessages.doYouWantToChangeStatusTo(((SelectItem) targetColumnLayoutData).getName()));
                    changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            if (((SelectItem) targetColumnLayoutData).isSelected()) {
                                new TaskChangingStatusNoteModal((SelectItem) targetColumnLayoutData, (Integer) taskListItem, widgetIndex, (Integer) prevItem, (Integer) afterItem, onDropCard);
                            } else {
                                taskService.changeTaskKanbanOrder((SelectItem) targetColumnLayoutData, (Integer) taskListItem, (Integer) prevItem, (Integer) afterItem, new AsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (onDropCard != null) {
                                            onDropCard.onDropCard();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancel() {
                            kanbanBoard.reloadColumn(((SelectItem) targetColumnLayoutData).getId());
                            kanbanBoard.reloadColumn(((SelectItem) sourceColumnLayoutData).getId());
                        }
                    });

                    changeStatusMessageBox.setTitle(wfmStrings.warning());
                    changeStatusMessageBox.open();
                } else {
                    if (((SelectItem) targetColumnLayoutData).isSelected()) {
                        new TaskChangingStatusNoteModal((SelectItem) targetColumnLayoutData, (Integer) taskListItem, widgetIndex, (Integer) prevItem, (Integer) afterItem, onDropCard);
                    } else {
                        taskService.changeTaskKanbanOrder((SelectItem) targetColumnLayoutData, (Integer) taskListItem, (Integer) prevItem, (Integer) afterItem, new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void onSuccess(Void aVoid) {
                                if (onDropCard != null) {
                                    onDropCard.onDropCard();
                                }
                            }
                        });
                    }
                }
            }
        };
    }

    private KanbanBoardDesign<TaskListItem> getKanbanBoardDesign() {
        return new KanbanBoardDesign<TaskListItem>() {
            @Override
            public Widget getBoardItem(TaskListItem kanbanItem, KanbanBoard<TaskListItem> kanbanBoard, Object... obj) {
                //Return card item
                MaterialPanel p = new MaterialPanel();
                if (obj != null && obj.length > 0 && (obj[0] instanceof HashMap)) {
                    HashMap<String, KanbanItemColumnConfigs> strMap = (HashMap) obj[0];
                    p.add(new TaskMaterialCard(kanbanItem, strMap));
                } else {
                    p.add(new TaskMaterialCard(kanbanItem));
                }
                p.setLayoutData(kanbanItem.getObjectID());
                return p;
            }

            @Override
            public void loadDefaultColumns(AbstractAsyncCallback callback) {
                KanbanService.App.get().getKanbanDefaultColumns(ReferenceParentEnum._TASK_STATUS, new AsyncCallback<ArrayList<SelectItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        callback.failure(throwable);
                    }

                    @Override
                    public void onSuccess(ArrayList<SelectItem> selectItems) {
                        LoadingPanel.loading(false);
                        callback.success(selectItems);
                    }
                });
            }

            @Override
            public boolean canDnD(TaskListItem kanbanItem) {
                return true;/*Utils.hasRoles(DR, ADMIN, PM, HR, TL) && kanbanItem.getPermissions().hasPermission(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode());*/
            }
        };
    }

    public void initTaskList(ListingFilterParameter filterParameter, ListingCallback<TaskListItem> callback, Span container) {
        taskService.getTaskList(filterParameter, new AbstractAsyncCallback<TaskList>() {
            @Override
            public void failure(Throwable caught) {
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            @Override
            public void success(TaskList result) {
                totalCount = result.getTotal();
                if (callback != null) {
                    atLeastOneTimerIsRunning = result.getAtLeastOneTimerIsRunning();
                    callback.onSuccess(result);
                    if (listingTable.getPagingScrollTable() != null && listingTable.getPagingScrollTable().isShowPopups()) {
                        getRelationsAsPopup(result.getObjectIDs());
                    }
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (totalCount > 0) {
                        statisticShortcut.setText(countFormat(totalCount));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private void getRelationsAsPopup(final ArrayList<Integer> sortedIDs) {
        if (sortedIDs != null && sortedIDs.size() > 0) {
            allInOneService.getRelations(RelationItem.TYPE_TASK, sortedIDs, new AbstractAsyncCallback<HashMap<Integer, ArrayList<RelationItem>>>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(HashMap<Integer, ArrayList<RelationItem>> result) {
                    LoadingPanel.loading(false);
                    listingTable.setPopupWidgets(new ArrayList<>());
                    listingTable.setPopupWidgets(HasLinks.relationToListingPopup(result, sortedIDs));
                }
            });
        }
    }

    protected Integer getTaskParentId() {
        return null;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.TaskListPanel;
    }

    public String getIconStyle() {
        return "bgMark icon-task";
    }

    public ImageResource getIconImage() {
        return null;
    }

    private MenuBar getActionsForSelections() {

        actions = new ContextMenu();
        actions.getMenuBar().setAutoOpen(true);

        if ((selectedRows != null && selectedRows.size() > 0) && hasAccessToChange) {
            hasChild = true;
            //STATUS
            final MenuBar statuses = new MenuBar(true);
            statuses.setAutoOpen(true);
            statuses.addStyleName("my-menu");
            CommonService.App.get().getAddTaskStatusDrop(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(final SelectItem[] result) {
                    int i = 0;
                    for (final SelectItem status : result) {
                        SafeHtml safeHtml = () -> "<span class='list-action-menu-icon'>" + status.getName() + "</span>";
                        MenuItem item = new MenuItem(safeHtml);
                        item.ensureDebugId("changeStatus" + i++);
                        item.setCommand(() -> updateStatus(selectedRows, status));
                        statuses.addItem(item);
                    }
                }
            });

            MenuItem statusBar = new MenuItem("<span>" + wfmStrings.changeStatus() + "</span>", true, statuses);
            statusBar.ensureDebugId("changeStatus");

            actions.getMenuBar().addItem(statusBar);
            //PRIORITY
            final MenuBar changePriority = new MenuBar(true);
            changePriority.setAutoOpen(true);
            changePriority.addStyleName("my-menu");
            taskService.getPriorities(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    int i = 0;
                    for (final SelectItem apriority : result) {
                        SafeHtml safeHtml = () -> "<span class='list-action-menu-icon'>" + apriority.getName() + "</span>";
                        MenuItem item = new MenuItem(safeHtml);
                        item.ensureDebugId("changePriority" + i++);
                        item.setCommand(() -> {
                            HashSet<TaskListItem> selectItems = new HashSet<>();
                            for (TaskListItem selectItem : selectedRows) {
                                if (Utils.hasRole(ADMIN) || selectItem.isPMorBackupPM() ||
                                        (selectItem.getTaskCreatorID() != null && selectItem.getTaskCreatorID().equals(Utils.getUserID()))
                                        || selectItem.getPermissions().hasPermission(TaskPermissionEnum.DELETE.getCode())) {
                                    selectItems.add(selectItem);
                                }
                            }
                            updatePriority(selectItems, apriority);
                        });
                        changePriority.addItem(item);
                    }
                }
            });

            MenuItem priorityBar = new MenuItem("<span>" + crmStrings.changePriority() + "</span>", true, changePriority);
            priorityBar.ensureDebugId("changePriority");

            actions.getMenuBar().addItem(priorityBar);


            MenuItem changeStartDateBar = new MenuItem("<span>" + crmStrings.setStartDate() + "</span>", true, () -> {
                ArrayList<Integer> taskIDs = new ArrayList<>();
                for (TaskListItem selectItem : selectedRows) {
                    if (Utils.hasRole(ADMIN) || selectItem.isPMorBackupPM() ||
                            (selectItem.getTaskCreatorID() != null && selectItem.getTaskCreatorID().equals(Utils.getUserID()))
                            || selectItem.getPermissions().hasPermission(TaskPermissionEnum.DELETE.getCode())) {
                        taskIDs.add(selectItem.getObjectID());
                    }
                }

                ChangeStartDatePopup datePopup = new ChangeStartDatePopup();
                datePopup.onSubmit(new Command() {
                    @Override
                    public void execute() {
                        LoadingPanel.loading(true);
                        TaskService.App.get().updateTasksStartDate(taskIDs, datePopup.getDate(), new AbstractAsyncCallback<Void>() {
                            @Override
                            public void failure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                Info.warn(wfmStrings.sorrySomethingWentWrong());
                            }

                            @Override
                            public void success(Void result) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, null, datePopup);
                                datePopup.close();
                                LoadingPanel.loading(false);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()));
                            }
                        });
                    }
                });
                datePopup.open();
            });

            actions.getMenuBar().addItem(changeStartDateBar);

            //Change Due Date Bar
            MenuItem changeDueDateBar = new MenuItem("<span>" + wfmStrings.setDueDate() + "</span>", true, () -> {
                ArrayList<Integer> taskIDs = new ArrayList<>();
                for (TaskListItem selectItem : selectedRows) {
                    if (Utils.hasRole(ADMIN) || selectItem.isPMorBackupPM() ||
                            (selectItem.getTaskCreatorID() != null && selectItem.getTaskCreatorID().equals(Utils.getUserID()))
                            || selectItem.getPermissions().hasPermission(TaskPermissionEnum.DELETE.getCode())) {
                        taskIDs.add(selectItem.getObjectID());
                    }
                }

                ChangeStartDatePopup datePopup = new ChangeStartDatePopup();
                datePopup.onSubmit(new Command() {
                    @Override
                    public void execute() {
                        LoadingPanel.loading(true);
                        TaskService.App.get().updateTasksDueDate(taskIDs, datePopup.getDate(), new AbstractAsyncCallback<ArrayList<String>>() {
                            @Override
                            public void failure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                Info.warn(wfmStrings.sorrySomethingWentWrong());
                            }

                            @Override
                            public void success(ArrayList<String> result) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, null, datePopup);
                                datePopup.close();
                                LoadingPanel.loading(false);
                                if (result.isEmpty()) {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()));
                                } else {
                                    Div taskslist = new Div();
                                    for (String taskName : result) {
                                        taskslist.add(new Paragraph(taskName));
                                    }
                                    Info.show(projectStrings.endDateCanNotBeBeforeStartDate(), Info.Type.WARNING);
                                    KpiModal omittedTasksPopUp = new KpiModal();
                                    omittedTasksPopUp.setTitle(projectStrings.endDateCanNotBeBeforeStartDate() + " for the following tasks.");
                                    omittedTasksPopUp.add(taskslist);
                                    omittedTasksPopUp.setWidth(500);
                                    omittedTasksPopUp.setMaxHeight("400px");
                                    omittedTasksPopUp.setScrollable(true);
                                    omittedTasksPopUp.setCloseButton(true);
                                    omittedTasksPopUp.open();
                                }
                            }
                        });
                    }
                });
                datePopup.open();
            });

            actions.getMenuBar().addItem(changeDueDateBar);

            final MenuBar changeBillable = new MenuBar(true);
            changeBillable.setAutoOpen(true);
            changeBillable.addStyleName("my-menu");

            MenuItem billableBar = new MenuItem("<span>" + projectStrings.setBillable() + "</span>", true, changeBillable);
            priorityBar.ensureDebugId("changeBillable");
            {
                SafeHtml safeHtml = () -> "<span class='list-action-menu-icon'>" + wfmStrings.yes() + "</span>";
                MenuItem yesItem = new MenuItem(safeHtml);
                yesItem.setCommand(() -> {
                    ArrayList<Integer> taskIDs = new ArrayList<>();
                    for (TaskListItem selectItem : selectedRows) {
                        if (Utils.hasRole(ADMIN) || selectItem.isPMorBackupPM()) {
                            taskIDs.add(selectItem.getObjectID());
                        }
                    }
                    setBillable(taskIDs, true);
                });
                changeBillable.addItem(yesItem);

                safeHtml = () -> "<span class='list-action-menu-icon'>" + wfmStrings.no() + "</span>";
                MenuItem noItem = new MenuItem(safeHtml);
                noItem.setCommand(() -> {
                    ArrayList<Integer> taskIDs = new ArrayList<>();
                    for (TaskListItem selectItem : selectedRows) {
                        if (Utils.hasRole(ADMIN) || selectItem.isPMorBackupPM()) {
                            taskIDs.add(selectItem.getObjectID());
                        }
                    }
                    setBillable(taskIDs, false);
                });
                changeBillable.addItem(noItem);
                actions.getMenuBar().addItem(billableBar);
            }
            //Add Assignee
            MenuItem addAssigneeBar = new MenuItem("<span>" + projectStrings.addAssignee() + "</span>", true, (Command) () -> {
                ArrayList<Integer> taskIDs = new ArrayList<>();
                for (TaskListItem selectItem : selectedRows) {
                    if (Utils.hasRole(ADMIN) || selectItem.isPMorBackupPM() ||
                            (selectItem.getTaskCreatorID() != null && selectItem.getTaskCreatorID().equals(Utils.getUserID()))
                            || selectItem.getPermissions().hasPermission(TaskPermissionEnum.DELETE.getCode())) {
                        taskIDs.add(selectItem.getObjectID());
                    }
                }
                if (!taskIDs.isEmpty()) {
                    TaskAssigneePopup assigneePopup = new TaskAssigneePopup(taskIDs);
                    assigneePopup.open();
                }
            });
            actions.getMenuBar().addItem(addAssigneeBar);

            //Change Projects
            if (Utils.hasGenericAccess(GenericSettingsEnum.IS_CHANGE_TASKS_PROJECT) && Utils.hasRole(ADMIN)) {
                MenuItem changeProjects = new MenuItem("<span>" + projectStrings.changeProjects() + "</span>", true, () -> {
                    for (TaskListItem selectItem : selectedRows) {
                        changeProjectsPopup.getItemIDs().add(selectItem.getObjectID());
                    }
                    changeProjectsPopup.open();
                });
                actions.getMenuBar().addItem(changeProjects);
            }

            if (getFiterParametrs() != null && getFiterParametrs().getProjectId() != null) {
                MenuItem changeWorkstream = new MenuItem("<span>" + projectStrings.changeWorkstream() + "</span>", true, (Command) () -> {
                    ArrayList<Integer> taskIDs = new ArrayList<>();
                    for (TaskListItem selectItem : selectedRows) {
                        if (Utils.hasRole(ADMIN) || selectItem.isPMorBackupPM()) {
                            taskIDs.add(selectItem.getObjectID());
                        }
                    }
                    showWorkstreamPanel(taskIDs);
                });
                actions.getMenuBar().addItem(changeWorkstream);
            }

            //REMOVE
            if (Utils.hasRole(ADMIN)) {
                SafeHtml safeHtml = () -> "<span>" /*+ new Image(addCaseImageBundles.remove().getSafeUri()) + "&nbsp;&nbsp;"*/ + wfmStrings.delete() + "</span>";
                final MenuItem remove = new MenuItem(safeHtml);
                remove.ensureDebugId("remove_button");
                remove.setCommand(() -> {
                    actions.hide();
                    deleteSelection(remove);
                });
                actions.getMenuBar().addItem(remove);
            }
        }

        MenuItem menuItem = new MenuItem("<span>" + wfmStrings.pdf() + "</span", true, (Command) () -> {
            for (TaskListItem task : selectedRows) {
                Scheduler.get().scheduleFixedPeriod(() -> {
                    HashMap<String, String> parametersMap = new HashMap<>();
                    parametersMap.put("objectID", task.getObjectID().toString());
                    Utils.sendPDFOrExcelRequest(this, CommandConstants.PDF_URL + "/taskViewPDFHandler", parametersMap, "_blank");
                    return false;
                }, 2000);
            }
        });
        actions.getMenuBar().addItem(menuItem);

        if (hasChild) {
            actions.getMenuBar().setAutoOpen(true);
            return actions.getMenuBar();
        } else {
            if (actionsEmpty == null) {
                actionsEmpty = new ContextMenu();
                actionsEmpty.getMenuBar().setAutoOpen(true);
                String text = "<span>" + wfmStrings.selectAnyItemToActivateBatchActions() + "</span>";
                MenuItem alertItem = new MenuItem(text, true, () -> {
                });
                actionsEmpty.getMenuBar().addItem(alertItem);
            }
            actionsEmpty.getMenuBar().setAutoOpen(true);
            return actionsEmpty.getMenuBar();
        }
    }

    private void setBillable(ArrayList<Integer> taskIDs, boolean b) {
        taskService.setTaskBillable(taskIDs, b, new AbstractAsyncCallback<Void>() {
            @Override
            public void success(Void result) {
                listingTable.reloadPage();
                actions.getMenuBar().removeFromParent();
                Info.show(property.getPlural(wfmStrings.messSuccessfullyUpdated(), wfmStrings.tasks()));
            }
        });

    }

    private void updateStatus(HashSet<TaskListItem> tasks, SelectItem status) {
        LoadingPanel.loading(true);
        taskService.updateTasksStatus(tasks, status, new AbstractAsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(Void result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, result, TaskListView.this);
                listingTable.reloadPage();
                actions.getMenuBar().removeFromParent();
                LoadingPanel.loading(false);
                Info.show(property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.task()));
            }
        });
    }

    private void updatePriority(HashSet<TaskListItem> tasks, SelectItem priority) {
        if (tasks.size() > 0) {
            LoadingPanel.loading(true);
            taskService.updateTasksPriority(tasks, priority, new AbstractAsyncCallback<Void>() {
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void onSuccess(Void result) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_EDIT, result, TaskListView.this);
                    listingTable.reloadPage();
                    actions.getMenuBar().removeFromParent();
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.tasks()));
                }
            });
        } else {
            Info.show(coreMessages.youDontHaveEnoughPermissionToSomethingSelectedTasks(wfmStrings.edit()), Info.Type.WARNING);
        }
    }

    private void showWorkstreamPanel(final ArrayList<Integer> tasks) {
        if (tasks.size() > 0) {
            final WorkstreamChooser workstreamChooser = new WorkstreamChooser();
            workstreamChooser.setProjectId(getFiterParametrs().getProjectId());
            workstreamChooser.setSelectedCommand(() -> taskService.changeWorkstream(tasks, workstreamChooser.getWorkstream().getId(), new AbstractAsyncCallback<Void>() {
                @Override
                public void success(Void result) {
                    listingTable.reloadPage();
                    Info.show("Workstream \"" + workstreamChooser.getWorkstream().getName() + "\" is successfully set to selected tasks");
                    tasks.clear();
                }
            }));
            workstreamChooser.publicShowShell();
        } else {
            Info.show(coreMessages.youDontHaveEnoughPermissionToSomethingSelectedTasks(wfmStrings.edit()), Info.Type.WARNING);
        }
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public void restoreState() {
        if (listingTable == null) {
            return;
        }
        if (listingTable.isListingPage()) {
            super.restoreState();
        } else {
            listingTable.getKanbanBoardView().resetScrollPositions();
        }
    }

    @Override
    public String getPropertyCode() {
        return TASK;
    }
}
