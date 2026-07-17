package com.edatasite.workforce.gwt.task.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskSelectItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskSelectItemList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TasksChooser extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final String NOT_SET_LABEL = wfmStrings.pleaseSelect();
    private final KpiDataGrid dataGrid;
    private final SimplePager simplePager;
    private TaskSelectItem task;
    private TaskSelectItem[] tasks;
    private Integer projectId;
    private Integer taskId;
    private String projectName;
    private Label projectLabel;
    private final TextBox taskNameBox = new TextBox();
    public KpiModal resultsShell;
    private TextBox searchBox = new TextBox();
    private Request request;
    private boolean shellOpen = false;
    private DataListBox projects;
    private int totalLength;
    private FlexTable table;
    private final boolean includeParentExistSubtasks;
    private Integer[] taskDependencies;
    private String type;
    public static final String SUCCESSOR = "successor";
    public static final String PREDECESSOR = "predecessor";
    final TaskServiceAsync taskService = TaskService.App.get();
    private Boolean isFirst = true;
    private boolean isChanged = false;
    private final ListLoadConfig listLoadConfig;
    private final ColumnSortEvent.ListHandler<TaskSelectItem> listHandler;
    /*
    Predecessor filter (tasks not allowed to be selected as predecessors):
    All selected successors and all their successors (in a tree)

    Successor filter (tasks not allowed to be selected as successors):
    All selected predecessors and all their predecessors (in a tree)
     */
    Command deletePredOrSuccTask;
    Command selectionChange;

    public void setDeletePredOrSuccTask(Command deletePredOrSuccTask) {
        this.deletePredOrSuccTask = deletePredOrSuccTask;
    }

    public void setSelectionChange(Command selectionChange) {
        this.selectionChange = selectionChange;
    }

    private TasksChooser succ_predChooser; //this is used to link to success or predecessor task chooser, to allow filter out tasks that will cause loops
    //selectedTasksMap is used to store subtree of successors or predecessors that should be selectedTasksMap
    protected Map<Integer, String[]> selectedTasksMap = new HashMap<>();

    public TasksChooser(boolean includeParentExistSubtasks, String type) {
        this(includeParentExistSubtasks);
        this.type = type;
    }

    public static final ProvidesKey<TaskSelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    public TasksChooser(boolean includeParentExistSubtasks) {
        super();
        dataGrid = new KpiDataGrid(KEY_PROVIDER);
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        simplePager = new SimplePager(SimplePager.TextLocation.LEFT, pagerResources, false, 0, true);
        simplePager.setDisplay(dataGrid);
        dataGrid.setHeight("300px");
        dataGrid.addStyleName("cellBasedWidget-mod cellBasedWidget-mod--static-body");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        simplePager.setPageSize(20);
        listLoadConfig = new ListLoadConfig();
        dataGrid.addRangeChangeHandler(event -> {
            Range range = dataGrid.getVisibleRange();
            int start = range.getStart();
            int length = range.getLength();

            listLoadConfig.setStart(start);
            listLoadConfig.setLimit(length);
        });
        listHandler = new ColumnSortEvent.ListHandler<>(dataGrid.getList());
        dataGrid.addColumnSortHandler(listHandler);

        //task number
        Column<TaskSelectItem, String> taskNumberColumn = new Column<TaskSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(TaskSelectItem object) {
                return object.getTaskNumber();
            }
        };
        taskNumberColumn.setSortable(true);
        listHandler.setComparator(taskNumberColumn, (o1, o2) -> o1.getTaskNumber().compareToIgnoreCase(o2.getTaskNumber()));
        dataGrid.addColumn(taskNumberColumn, wfmStrings.number());
        dataGrid.setColumnWidth(taskNumberColumn, 20, Style.Unit.PCT);
        //task name
        Column<TaskSelectItem, String> taskNameColumn = new Column<TaskSelectItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(TaskSelectItem object) {
                return object.getName();
            }
        };
        taskNameColumn.setSortable(true);
        listHandler.setComparator(taskNameColumn, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        dataGrid.addColumn(taskNameColumn, wfmStrings.taskName());
        dataGrid.setColumnWidth(taskNameColumn, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        taskNameColumn.setFieldUpdater((index, object, value) -> {

            task = object;
            refreshNameBox();
            projectId = task.getProjectId();
            dataGrid.getList().remove(object);
            dataGrid.refresh();
            //eleminate loops with succ and pred
            //1. get task subtree
            //2. add subtree to selectedTasksMap that will be used to filter out not allowed taskIds
            if (PREDECESSOR.equals(type)) {
                getRecursivelyPredecessors(task.getId());
            } else if (SUCCESSOR.equals(type)) {
                getRecursivelySuccessors(task.getId());
            }
            refreshAddedTasks();
            resultsShell.close();
            selectionChange.execute();
        });
        //start date
        Column<TaskSelectItem, String> taskStartDateColumn = new Column<TaskSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(TaskSelectItem object) {
                if (object.isAllDay() != null && object.isAllDay()) {
                    return DateUtils.format(object.getTaskStartDate());
                }
                return DateUtils.formatInternal(object.getTaskStartDate());
            }
        };
        taskStartDateColumn.setSortable(true);
        listHandler.setComparator(taskStartDateColumn, (o1, o2) -> DateUtils.format(o1.getTaskStartDate()).compareToIgnoreCase(DateUtils.format(o2.getTaskStartDate())));
        dataGrid.addColumn(taskStartDateColumn, wfmStrings.startDate());
        dataGrid.setColumnWidth(taskStartDateColumn, 20, Style.Unit.PCT);
        //due date
        Column<TaskSelectItem, String> taskDueDateColumn = new Column<TaskSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(TaskSelectItem object) {
                if (object.isAllDay() != null && object.isAllDay()) {
                    return DateUtils.format(object.getTaskDueDate());
                }
                return DateUtils.formatInternal(object.getTaskDueDate());
            }
        };
        taskDueDateColumn.setSortable(true);
        listHandler.setComparator(taskDueDateColumn, (o1, o2) -> DateUtils.format(o1.getTaskDueDate()).compareToIgnoreCase(DateUtils.format(o2.getTaskDueDate())));
        dataGrid.addColumn(taskDueDateColumn, wfmStrings.dueDate());
        dataGrid.setColumnWidth(taskDueDateColumn, 20, Style.Unit.PCT);

        this.includeParentExistSubtasks = includeParentExistSubtasks;
        taskNameBox.setText(NOT_SET_LABEL);
        taskNameBox.addKeyPressHandler(event -> taskNameBox.cancelKey());

        taskNameBox.addClickHandler(sender -> showShell());

        table = new FlexTable();
        table.setCellPadding(2);
        table.setStyleName("taskListStyle");
        VerticalPanel vp = new VerticalPanel();
        vp.add(taskNameBox);
        vp.add(table);
        initWidget(vp);
    }


    private AbstractAsyncCallback<String[]> callbackFilted(final Integer selectedTaskId) {
        return new AbstractAsyncCallback<String[]>() {
            public void success(String[] taskIdList) {
                if (taskIdList != null) {
                    String[] elderTasks = selectedTasksMap.get(selectedTaskId);
                    if (elderTasks == null) {
                        selectedTasksMap.put(selectedTaskId, taskIdList);
                    } else {
                        String[] resultTasks = new String[elderTasks.length + taskIdList.length];
                        System.arraycopy(taskIdList, 0, resultTasks, 0, taskIdList.length);
                        System.arraycopy(elderTasks, 0, resultTasks, taskIdList.length, elderTasks.length);
                        selectedTasksMap.put(selectedTaskId, resultTasks);
                    }
                }
            }
        };
    }

    public void getRecursivelyPredecessors(Integer selectedTaskId) {
        taskService.getRecursivelyPredecessors(selectedTaskId, callbackFilted(selectedTaskId));
    }

    public void getRecursivelySuccessors(Integer selectedTaskId) {
        taskService.getRecursivelySuccessors(selectedTaskId, callbackFilted(selectedTaskId));
    }

    public TaskSelectItem getTask() {
        return task;
    }

    public void clearSelection() {
        task = null;
        if (!shellOpen) {
            refreshNameBox();
        }
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
        if (task != null && !projectId.equals(task.getProjectId())) {
            clearSelection();
        }
        if (shellOpen) {
            refreshResults();
        }
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void refreshResults() {
        loadResults(listLoadConfig);
    }

    private void loadResults(final ListLoadConfig listLoadConfig) {
        if (request != null) {
            request.cancel();
        }
        dataGrid.getList().clear();
        AsyncCallback<TaskSelectItemList> asyncCallback = new AbstractAsyncCallback<TaskSelectItemList>() {

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                request = null;
            }

            public void success(TaskSelectItemList list) {

                //filter not allowed tasks
                Iterator<Map.Entry<Integer, String[]>> filterList = succ_predChooser.selectedTasksMap.entrySet().iterator();
                java.util.List<TaskSelectItem> toRemove = new java.util.ArrayList<>();
                while (filterList.hasNext()) {
                    Map.Entry<Integer, String[]> taskIds = filterList.next(); //tasks selected as predec or successor
                    if (taskIds != null) {
                        for (int k = 0; k < list.getResults().size(); k++) {
                            for (String taskId1 : taskIds.getValue()) {
                                if (list.getResults().get(k).getId().equals(Integer.valueOf(taskId1))) {
                                    toRemove.add(list.getResults().get(k));
                                }
                            }
                        }
                    }
                }
                if (toRemove.size() > 0) {
                    list.getResults().removeAll(toRemove);
                }
                dataGrid.supplyProvider(list.getResults());
                dataGrid.refresh();
                request = null;
                LoadingPanel.loading(false);
            }

        };

        taskDependencies = selectedTasksMap.keySet().toArray(new Integer[]{});

        if (searchBox.getText() == null || "".equals(searchBox.getText())) {
            request = taskService.getLatestTasks(projectId, listLoadConfig, taskDependencies, includeParentExistSubtasks, asyncCallback);
            return;
        }
        request = taskService.searchTasks(projectId, searchBox.getText(), new ListingFilterParameter(), listLoadConfig, taskDependencies, includeParentExistSubtasks, asyncCallback);
    }

    private Integer getFilterProjectId() {
        return projects.getSelectedItem() != null ? projects.getSelectedItem().getId() : null;
    }

    private void refreshNameBox() {
        if (task == null) {
            taskNameBox.setText(NOT_SET_LABEL);
        }
        addTableItem();
    }

    private void addTableItem() {
        CellFormatter cf = table.getCellFormatter();
        final HTML label = new HTML(task.getName());
        if (label.getText().length() > 37) {
            label.setText(label.getText().substring(0, 36) + "...");
        }
        final int r = table.getRowCount();
        table.setWidget(r, 0, label);
        cf.setWidth(r, 0, "215px");
        final SimpleDeleteLink deleteLink = new SimpleDeleteLink(wfmStrings.delete(), task);
        deleteLink.setClickListener(sender -> {
            table.remove(label);
            table.remove(deleteLink);
            selectedTasksMap.remove(deleteLink.getTaskItem().getId());
            refreshAddedTasks();
//                refreshTaskDependencies(deleteLink.getTaskItem(), "delete");
            deletePredOrSuccTask.execute();
            setTableVisibility();
            refreshResults();
            isChanged = true;
        });
        deleteLink.addClickListener(deleteLink.getClickListener());
        table.setWidget(r, 1, deleteLink);
        cf.setWidth(r, 1, "35px");
        isChanged = true;
        setTableVisibility();
    }
    /*
    Adds task to selected successor/predecessor tasklist and adds Remove link to it
     */

    public void addTableItem(final TaskSelectItem item) {
        final CellFormatter cf = table.getCellFormatter();
        final HTML label = new HTML(item.getName());
        if (label.getText().length() > 37) {
            label.setText(label.getText().substring(0, 36) + "...");
        }
        final int r = table.getRowCount();
        table.setWidget(r, 0, label);
        cf.setWidth(r, 0, "215px");
        final SimpleDeleteLink deleteLink = new SimpleDeleteLink(wfmStrings.delete(), item);
        deleteLink.setClickListener(sender -> {
            table.remove(label);
            table.remove(deleteLink);
            selectedTasksMap.remove(deleteLink.getTaskItem().getId());
            refreshAddedTasks();
            setTableVisibility();
            deletePredOrSuccTask.execute();
            refreshResults();

        });
        deleteLink.addClickListener(deleteLink.getClickListener());
        table.setWidget(r, 1, deleteLink);
        cf.setWidth(r, 1, "35px");
        setTableVisibility();
        String[] items = {item.getId().toString()};
        selectedTasksMap.put(item.getId(), items);
    }

    private void setTableVisibility() {
        boolean hasWidgets = false;
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getWidget(i, 1) != null) {
                hasWidgets = true;
            }
        }
        if (hasWidgets) {
            table.setVisible(true);
        } else {
            table.setVisible(false);
            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                table.removeRow(i);
            }
        }
    }

    public void showShell() {
        if (isFirst && taskId != null) {
            if (!PREDECESSOR.equals(type)) {
                for (Integer i : selectedTasksMap.keySet()) {
                    getRecursivelyPredecessors(i);
                }
                isFirst = false;
            } else if (!SUCCESSOR.equals(type)) {
                for (Integer i : selectedTasksMap.keySet()) {
                    getRecursivelySuccessors(i);
                }
                isFirst = false;
            }
            selectedTasksMap.put(taskId, new String[]{taskId.toString()});
        }
        if (resultsShell == null) {
            createResultsShell();
        }
        projectLabel.setText(wfmStrings.project() + ": " + getProjectName());
        resultsShell.open();
    }

    private void createResultsShell() {
        searchBox.setFocus(true);
        shellOpen = true;
        CommonService.App.get().getProjects(false, new AbstractAsyncCallback<ProjectItem[]>() {
            public void failure(Throwable throwable) {
                refreshResults();
            }

            public void success(final ProjectItem[] object) {
                DeferredCommand.addCommand(() -> {
                    projects.setItems(object);
                    projects.setSelected(projectId);
//                        filterNotAllowedTasks();
                    refreshResults();
                    if (task == null) {
                        searchBox.setText("");
                    }
                });
            }
        });

        resultsShell = new KpiModal();
        resultsShell.setCloseButton(true);
        resultsShell.addCloseHandler(popupPanelCloseEvent -> shellOpen = false);

        Div topRow = new Div("panel-box");

        VerticalPanelDiv container = new VerticalPanelDiv();

        searchBox = new TextBox();
        searchBox.addKeyPressHandler(event -> {
            if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                refreshResults();
            }
        });
        Div searchPanel = new Div("panel-box__item");
        MaterialPanel searchDiv = new MaterialPanel("searchForm searchForm--border");
        Span searchBtn = new Span();
        searchBtn.setStyleName("searchForm__btn");
        searchBtn.addClickHandler(event -> refreshResults());

        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT);
        resetButton.addClickHandler(event -> {
            searchBox.setText("");
            refreshResults();
        });

        searchDiv.add(searchBox);
        searchDiv.add(searchBtn);
        searchDiv.add(resetButton);
        searchPanel.add(searchDiv);
        topRow.add(searchPanel);

        container.add(topRow);

        Div projectPanel = new Div("panel-box__item");
        projectLabel = new Label(wfmStrings.project() + ": " + getProjectName());
        projectPanel.add(projectLabel);
        topRow.add(projectPanel);

        projects = new DataListBox();
        projects.setNullLabel(projectStrings.allProjects());
        projects.addValueChangeHandler(sender -> refreshResults());

        listLoadConfig.setStart(0);
        listLoadConfig.setLimit(20);
        container.add(dataGrid);
        container.add(simplePager);
        resultsShell.add(container);
        simplePager.getDisplay().fireEvent(new RangeChangeEvent(new Range(listLoadConfig.getStart(), listLoadConfig.getLimit())) {
            @Override
            protected void dispatch(Handler handler) {
                super.dispatch(handler);
            }
        });
    }

    public boolean getRemoteSort() {

        return false;
    }

    public int getSortDir() {

        return 0;
    }

    public String getSortField() {

        return null;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setSortDir(int dir) {


    }

    public void setSortField(String field) {


    }

    public void sort(String sortField, int sortDir) {


    }

    public void reload() {


    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void clearTable() {
        for (int i = table.getRowCount() - 1; i >= 0; i--) {
            table.removeRow(i);
        }
        tasks = new TaskSelectItem[0];
        selectedTasksMap.clear();
        resultsShell = null;
    }

    public class SimpleDeleteLink extends SimpleLink {
        TaskSelectItem taskItem;
        ClickListener clickListener;

        public SimpleDeleteLink(String linkname, TaskSelectItem taskItem) {
            super(linkname);
            this.taskItem = taskItem;
        }

        public TaskSelectItem getTaskItem() {
            return taskItem;
        }

        public void setTaskItem(TaskSelectItem taskItem) {
            this.taskItem = taskItem;
        }

        public ClickListener getClickListener() {
            return clickListener;
        }

        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

    }

    public void refreshAddedTasks() {
        tasks = new TaskSelectItem[table.getRowCount()];
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getWidget(i, 1) != null) {
                tasks[i] = ((SimpleDeleteLink) table.getWidget(i, 1)).getTaskItem();
            }
        }
    }

    public TaskSelectItem[] getTasks() {
        return tasks;
    }

    public FlexTable getTable() {
        return table;
    }

    public void setTable(FlexTable table) {
        this.table = table;
    }

    public void setTaskDependencies(Integer[] taskDependencies) {
        this.taskDependencies = taskDependencies;
    }

    public Integer[] getExistingTasks() {
        Integer[] t = new Integer[table.getRowCount()];
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getWidget(i, 1) != null) {
                t[i] = ((SimpleDeleteLink) table.getWidget(i, 1)).getTaskItem().getId();
            }
        }
        return t;
    }

    public void refreshTaskDependencies(TaskSelectItem task, String s) {
        if (taskDependencies == null) {
            taskDependencies = new Integer[0];
        }
        boolean included = false;
        for (Integer taskDependency1 : taskDependencies) {
            if (taskDependency1 != null && taskDependency1.equals(task.getId())) {
                included = true;
            }
        }
        Integer[] td;
        if (s.equals("add") && !included) {
            td = new Integer[taskDependencies.length + 1];
            int inc = 0;
            for (Integer taskDependency : taskDependencies) {
                td[inc] = taskDependency;
                inc++;
            }
            td[inc] = task.getId();
            taskDependencies = td;
        }
        if (s.equals("delete") && included) {
            td = new Integer[taskDependencies.length - 1];
            int inc = 0;
            int i = 0;
            while (i < taskDependencies.length) {
                if (!task.getId().equals(taskDependencies[i])) {
                    td[inc] = taskDependencies[i];
                    inc++;
                }
                i++;
            }
            taskDependencies = td;
        }
    }

    public void setEnabled(boolean enabled) {
        taskNameBox.setEnabled(enabled);
    }

    public void setSucc_predChooser(TasksChooser succ_predChooser) {
        this.succ_predChooser = succ_predChooser;
    }

    public void setSelectedTasksMap(HashMap<Integer, String[]> selectedTasksMap) {
        this.selectedTasksMap = selectedTasksMap;
    }

    public boolean isChanged() {
        return isChanged;
    }
}
