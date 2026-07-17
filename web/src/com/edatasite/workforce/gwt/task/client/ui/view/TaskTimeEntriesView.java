package com.edatasite.workforce.gwt.task.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.MoveTimeEntriesPopup;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeEntriesItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import java.util.ArrayList;
import java.util.Set;

public class TaskTimeEntriesView extends BaseListView implements Constants {

    private final Integer taskID;
    private ListingPanel<TaskTimeEntriesItem> listPanel;

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private KpiDataGrid<TaskTimeEntriesItem> dataGrid;
    private ListDataProvider<TaskTimeEntriesItem> dataProvider;
    private ColumnSortEvent.ListHandler<TaskTimeEntriesItem> listHandler;
    protected ContextMenu actions;
    protected ContextMenu actionsEmpty;
    private Set<TaskTimeEntriesItem> selectedRows;

    public TaskTimeEntriesView(Integer taskID) {
        super("entries", projectStrings.timeEntries());
        this.taskID = taskID;
    }

    public String getDescription() {
        return projectStrings.timeEntries();
    }

    @Override
    public String getIconStyle() {
        return "timeEnt time-entiries";
    }

    @Override
    protected Widget onInitialize() {
        ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
        listingFilterParameter.setTaskID(taskID);
        if (Utils.hasEitherRole(TIMESHEET_EDITOR_CODE)) {
            listPanel = new ListingPanel<>(ListPanelType.TaskTimeEntriesPanel, getColumnConfig(), getListProvider(), getListDesign(),
                    SelectionGrid.SelectionPolicy.CHECKBOX, listingFilterParameter);
        } else {
            listPanel = new ListingPanel<>(ListPanelType.TaskTimeEntriesPanel, getColumnConfig(), getListProvider(), getListDesign(), listingFilterParameter);
        }

        listPanel.addSelectionRowHandler(selected -> selectedRows = selected);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ISSUE_ADD, TaskTimeEntriesView.this, (sender, args) -> listPanel.reloadPage());
        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        final ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig columnConfig;
        if (Utils.hasEitherRole(TIMESHEET_EDITOR_CODE)) {
            columnConfig = new ColumnDefinitionConfig<Object, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
                @Override
                public Anchor getCellValue(final Object object) {
                    final TaskTimeEntriesItem item = (TaskTimeEntriesItem) object;
                    final MenuBar actions = new MenuBar(true);

                    //move time entry
                    final MenuPopItem moveTimeEntry = new MenuPopItem(wfmStrings.moveTime(), "removeItemStyle-profile");
                    moveTimeEntry.setCommand(() -> {
                        ArrayList<TaskTimeEntriesItem> selectedTimeEntry = new ArrayList<>();
                        selectedTimeEntry.add(item);
                        new MoveTimeEntriesPopup(selectedTimeEntry, o -> listPanel.reloadPage());
                    });

                    actions.addItem(moveTimeEntry);

                    ToolItem toolItem = new ToolItem(1);
                    toolItem.setWidget(actions);
                    Anchor anchor = toolItem.getAction();
                    anchor.addClickHandler(clickEvent -> {
                        moveTimeEntry.setVisible(Utils.hasPermission(PermissionConstants.PM_ISSUE_REMOVE));
                    });
                    moveTimeEntry.ensureDebugId("moveTimeEntry");
                    return toolItem.getAction();
                }
            };
            columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
            columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
            columnConfig.setColumnSortable(false);
            columns.add(columnConfig);
        }

        //EmployeeCode
        columnConfig = new ColumnDefinitionConfig<TaskTimeEntriesItem, String>(wfmStrings.employeeCode(), TaskTimeEntriesItem.EMPLOYEE_CODE, 120) {
            @Override
            public String getCellValue(TaskTimeEntriesItem rowValue) {
                return rowValue.getEmloyeeCode();
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //assignee
        columnConfig = new ColumnDefinitionConfig<TaskTimeEntriesItem, String>(wfmStrings.assignee(), TaskTimeEntriesItem.EMPLOYEE, 200) {
            @Override
            public String getCellValue(TaskTimeEntriesItem rowValue) {
                return rowValue.getEmloyee();
            }
        };
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //date
        columnConfig = new ColumnDefinitionConfig<TaskTimeEntriesItem, String>(wfmStrings.date(), TaskTimeEntriesItem.DATE, 120) {
            @Override
            public String getCellValue(TaskTimeEntriesItem rowValue) {
                return rowValue.getDate() != null ? DateUtils.format(rowValue.getDate().getNonConvertedDate()) : "";
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //employee comment
        columnConfig = new ColumnDefinitionConfig<TaskTimeEntriesItem, String>(projectStrings.employeesComment(), TaskTimeEntriesItem.EMPLOYEE_COMMENT, 200) {
            @Override
            public String getCellValue(TaskTimeEntriesItem rowValue) {
                return rowValue.getComment();
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.addStyleAttribute("wrap", "true");
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //Manager comment
        columnConfig = new ColumnDefinitionConfig<TaskTimeEntriesItem, String>(wfmStrings.managersComment(), TaskTimeEntriesItem.MANAGER_COMMENT, 200) {
            @Override
            public String getCellValue(TaskTimeEntriesItem rowValue) {
                return rowValue.getManagerComment();
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //time spend
        columnConfig = new ColumnDefinitionConfig<TaskTimeEntriesItem, String>(wfmStrings.timeSpentOnly(), TaskTimeEntriesItem.TIMESPENT, 80) {
            @Override
            public String getCellValue(TaskTimeEntriesItem rowValue) {
                return Utils.formatMinutes(rowValue.getTimeSpent());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        if ("true".equals(Utils.userSettings.get(SHOW_HOUR_TYPE_DROPDOWN))) {
            //hour type
            columnConfig = new ColumnDefinitionConfig<TaskTimeEntriesItem, String>(projectStrings.hourType(), TaskTimeEntriesItem.HOURS_TYPE, 100) {
                @Override
                public String getCellValue(TaskTimeEntriesItem rowValue) {
                    return rowValue.getHourType();
                }
            };
            columnConfig.setMinimumColumnWidth(50);
            columnConfig.setColumnSortable(false);
            columns.add(columnConfig);
        }

        //status
        columnConfig = new ColumnDefinitionConfig<TaskTimeEntriesItem, String>(wfmStrings.status(), TaskTimeEntriesItem.STATUS, 100) {
            @Override
            public String getCellValue(TaskTimeEntriesItem rowValue) {
                String status = rowValue.getStatus();
                if ("Waiting".equals(rowValue.getStatus())) {//not localization  --  Waiting
                    status = wfmStrings.waitingForApproval();
                } else if ("Approve".equals(rowValue.getStatus())) {//not localization  --  Approve
                    status = wfmStrings.approved();
                } else if ("Reject".equals(rowValue.getStatus())) {//not localization  --  Reject
                    status = wfmStrings.rejected();
                }
                return status;
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private ListingRequestProvider<TaskTimeEntriesItem> getListProvider() {

        return (filterParametrs, callback) -> {
            filterParametrs.setTaskID(taskID);
            if (!Utils.hasPermission(PermissionConstants.SEE_ALL_TIME_ENTRIES)) {
                filterParametrs.setUserID(Utils.getUserID());
            }
            TaskService.App.get().getTaskTimeEntriesList(filterParametrs, new AsyncCallback<ListResult<TaskTimeEntriesItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<TaskTimeEntriesItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }


    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>();
                        fields.add(ListingChooseFilter.TASK_EMPLOYEES);
                        return fields;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ChooseFilter.TIME_ENTRIES_FILTER;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarMore() {
                ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), ActionButton.Type.TOOLMENU);
                more.ensureDebugId("Time_entry_list_more_button");
                more.addDomHandler(event -> {
                    MenuBar menu = getActionsForSelections();
                    menu.setAutoOpen(true);
                    more.setMenu(menu);
                }, MouseOverEvent.getType());
                return more;
            }
            @Override
            public ActionButton initTopToolBarNew() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.PM_ISSUE_LIST_CUSTOMIZE_BUTTON);
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noTimeEntiesYet());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private MenuBar getActionsForSelections() {
        if (Utils.hasEitherRole(TIMESHEET_EDITOR_CODE)) {
            if ((selectedRows != null && selectedRows.size() > 0)) {
                ArrayList<TaskTimeEntriesItem> selectedTimeEntry = new ArrayList<>();
                selectedTimeEntry.addAll(selectedRows);
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(true);

                MenuItem changeWorkstream = new MenuItem("<span>" + wfmStrings.moveTimeEntriesToAnother() + "</span>", true, (Command) () -> {
                    new MoveTimeEntriesPopup(selectedTimeEntry, o -> listPanel.reloadPage());
                });
                actions.getMenuBar().addItem(changeWorkstream);
                return actions.getMenuBar();
            }
        }
        return null;
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
}
