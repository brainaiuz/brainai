package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportMPPFilePopup;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.ganttchart.client.connector.CalculatorFactory;
import com.edatasite.workforce.gwt.ganttchart.client.enums.LoadItemType;
import com.edatasite.workforce.gwt.ganttchart.client.geometry.Point;
import com.edatasite.workforce.gwt.ganttchart.client.geometry.Rectangle;
import com.edatasite.workforce.gwt.ganttchart.client.model.PredecessorType;
import com.edatasite.workforce.gwt.ganttchart.client.rpc.GanttItem;
import com.edatasite.workforce.gwt.ganttchart.client.treetable.GanttChartTree;
import com.edatasite.workforce.gwt.ganttchart.client.treetable.GanttTreeItem;
import com.edatasite.workforce.gwt.ganttchart.client.widget.SVGDefs;
import com.edatasite.workforce.gwt.ganttchart.client.widget.SVGMarker;
import com.edatasite.workforce.gwt.ganttchart.client.widget.SVGPanel;
import com.edatasite.workforce.gwt.ganttchart.client.widget.SVGPath;
import com.edatasite.workforce.gwt.task.client.localization.TaskMessages;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.security.ClientSecurityContext;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Jul 29, 2011
 * Time: 8:16:00 AM
 * To change this template use File | Settings | File Templates.
 */

public abstract class GanttChart extends Composite implements Constants, com.edatasite.workforce.gwt.core.client.ui.Constants {
    protected static GanttChartServiceAsync ganttChartService = GanttChartService.App.get();
    public static WfmMessages wfmMessages = WfmMessages.App.get();
    public static WfmStrings wfmStrings = WfmStrings.App.get();
    public static TaskMessages taskMessages = TaskMessages.App.get();

    public String ganttchartType = ganttTypeDaily;
    protected int collapseWidth = 210;

    private FlowPanel mainPanel;
    private FlowPanel mainContent;
    private DataListBox employees;
    private KpiCheckBox showActualCheckBox;
    public GanttChartTree ganttChartTree;
    private FlowPanel leftPanel, rightPanel, leftScrollPanel;
    private FlowPanel leftContent, buttonsBar;
    private FlexTable leftHeaderPanel;
    private FlowPanel rightTopPanelDiv;
    protected FlexTable rightHeaderPanel;
    protected FlexTable rightBackgroundPanel;
    private FlowPanel rightChartPanel;
    protected ScrollPanel rightScrollPanel;
    private SVGDefs svgDefs;
    public SVGPanel svgPanel;
    private WfmButton2 zoomInButton, zoomOutButton, settingsButton;
    private WfmButton2 filterButton;
    private WfmButton2 refreshButton;
    public SVGMarker svgArrowMarker;
    public AbsolutePanel boundaryPanel;
    public HashMap<String, TaskWidget> taskWidgets;
    public HashMap<String, GanttTreeItem> taskItems;
    public HashMap<Integer, ArrayList<TaskSingleItem>> workstreamTasks;
    public ArrayList<TaskSingleItem> tasks;
    protected Date startDate;
    protected Date endDate;
    private Date shiftingDate;
    private String columnNames;
    private int rowIndex = 0;
    public Integer projectID, horizontalScroll = 0, verticalScroll = 0;
    public GanttChart ganttChart;
    public GanttChartFilterPopup filterPopup;
    private GanttChartSettingsPopup settingsPopup;
    public boolean hasEditAccess = Utils.hasPermission(PermissionConstants.PM_PROJECT_GANTT_CHART);
    public HashMap<Integer, HashSet<String>> permissionMap = new HashMap<>();
    private int leftPanelWidth = 20, cindex = 0, rindex = 0, columnsCount = 0;
    private SelectItem[] priorities;
    private String locale;
    protected String orderBy = "startDate asc";
    protected GanttItem ganttItem;
    private int startDateIndex;
    private int endDateIndex;

    protected GanttChart() {
    }

    public GanttChart(String ganttchartType) {
        this.ganttchartType = ganttchartType;
    }

    public GanttChart(String ganttchartType, GanttItem ganttItem) {
        this.ganttItem = ganttItem;
        this.startDate = ganttItem.getStartDate();
        this.endDate = ganttItem.getEndDate();
        this.projectID = ganttItem.getProjectID();
        this.ganttchartType = ganttchartType;
        this.columnNames = ganttItem.getColumnNames();
        this.locale = ganttItem.getLocale();
        this.priorities = ganttItem.getPriorities();
        onInitialize();
    }

    public Widget onInitialize() {
        LoadingPanel.loading(true);
        ganttChart = this;
        taskWidgets = new HashMap<>();
        taskItems = new HashMap<>();
        workstreamTasks = new HashMap<>();
        startDate = DateUtil.getCompanyFirstDateOfWeek(startDate);
        shiftingDate = (Date) startDate.clone();
        endDate = DateUtil.getCompanyLastDateOfWeek(endDate);
        final int daysCount = DateUtil.getDaysCount(startDate, endDate);

        //LeftPanel initialization
        leftHeaderPanel = new FlexTable();
        leftHeaderPanel.setCellPadding(0);
        leftHeaderPanel.setCellSpacing(0);
        leftHeaderPanel.getElement().getStyle().setProperty("borderLeft", "1px solid black");
        int index = 1;

        leftPanel = new FlowPanel();
        leftPanel.getElement().setId("leftPanel");
        leftPanel.setStyleName("leftPanelStyle");

        rightPanel = new FlowPanel();
        rightPanel.getElement().setId("rightPanel");
        rightPanel.setStyleName("rightPanelStyle");

        drawLeftHeader();

        ganttChartTree = new GanttChartTree(ganttChart);
        ganttChartTree.setCellPadding(0);
        ganttChartTree.setCellSpacing(0);
        ganttChartTree.getElement().getStyle().setProperty("whiteSpace", "nowrap");
        ganttChartTree.getElement().getStyle().setProperty("borderLeft", "1px solid black");
        ganttChartTree.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        ganttChartTree.addClickHandler(event -> {
            int newCindex = ganttChartTree.getCellForEvent(event).getCellIndex();
            int newRindex = ganttChartTree.getCellForEvent(event).getRowIndex();
            Widget widget = ganttChartTree.getWidget(newRindex, newCindex);
            if (widget instanceof Label) {
                final Label label = (Label) widget;
                final Integer taskID = Integer.valueOf(label.getElement().getId().split("_")[1]);
                if (workstreamTasks.get(taskID) == null) { //Check for selected item is task
                    String[] columns = getColumnNames().split(",");
                    final String columnName = columns[newCindex - 1];
                    Widget requiredWidget = getRequiredWidget(label.getText(), columnName);
                    if (newCindex > 0 && requiredWidget != null) { // 0 - indexda Name column turadi
                        if (newCindex != cindex || newRindex != rindex) {
                            if (ganttChartTree.getWidget(rindex, cindex) instanceof Label) {
                            } else {
                                if (ganttChartTree.getWidget(rindex, cindex) instanceof TextBox) {
                                    TextBox textBox2 = (TextBox) ganttChartTree.getWidget(rindex, cindex);
                                    Label label1 = new Label(textBox2.getText() + (TaskListItem.COMPLETE.equals(columns[newCindex]) ? "%" : ""));
                                    label1.getElement().setId("r" + rindex + "c" + cindex + "_" + taskID);
                                    ganttChartTree.setWidget(rindex, cindex, label1);
                                } else if (ganttChartTree.getWidget(rindex, cindex) instanceof DataListBox) {
                                    DataListBox listBox = (DataListBox) ganttChartTree.getWidget(rindex, cindex);
                                    Label label1 = new Label(listBox.getSelectedItem() != null ? listBox.getSelectedItem().getName() : "");
                                    label1.getElement().setId("r" + rindex + "c" + cindex + "_" + taskID);
                                    ganttChartTree.setWidget(rindex, cindex, label1);
                                } else if (ganttChartTree.getWidget(rindex, cindex) instanceof DatePicker) {
                                    DatePicker textBox2 = (DatePicker) ganttChartTree.getWidget(rindex, cindex);
                                    Label label1 = new Label(textBox2.getDate() != null ? DateUtil.shortDateFormat.format(textBox2.getDate()) : "");
                                    label1.getElement().setId("r" + rindex + "c" + cindex + "_" + taskID);
                                    ganttChartTree.setWidget(rindex, cindex, label1);
                                } else if (ganttChartTree.getWidget(rindex, cindex) instanceof KpiCheckBox) {
                                    KpiCheckBox textBox2 = (KpiCheckBox) ganttChartTree.getWidget(rindex, cindex);
                                    Label label1 = new Label(textBox2.getValue() ? wfmStrings.yes() : wfmStrings.no());
                                    label1.getElement().setId("r" + rindex + "c" + cindex + "_" + taskID);
                                    ganttChartTree.setWidget(rindex, cindex, label1);
                                }
                            }
                            rindex = newRindex;
                            cindex = newCindex;
                        }
                        rindex = newRindex;
                        cindex = newCindex;

                        if (requiredWidget instanceof TextBox) {
                            final TextBox textBox = (TextBox) requiredWidget;
                            textBox.addKeyDownHandler(event14 -> {
                                int code = event14.getNativeKeyCode();
                                if (KeyCodes.KEY_ENTER == code) {
                                    Label label1 = new Label(textBox.getText() + (label.getText().endsWith("%") ? "%" : ""));
                                    label1.getElement().setId("r" + rindex + "c" + cindex + "_" + taskID);
                                    if (TaskListItem.ESTIMATED.equals(columnName)) {
                                        label1.setText(Utils.formatMinutes(Integer.valueOf(textBox.getText()) * 60));
                                    } else if (TaskListItem.COMPLETE.equals(columnName)) {
                                        TaskWidget taskWidget = taskWidgets.get("t_" + taskID);
                                        if (taskWidget != null) {
                                            boundaryPanel.remove(taskWidget);
                                            TaskSingleItem userObject = (TaskSingleItem) taskItems.get("t" + taskID).getUserObject();
                                            userObject.setPercent(Float.valueOf(textBox.getText()));
                                            taskItems.get("t" + taskID).setUserObject(userObject);
                                            attachTaskWidget(rindex, userObject);
                                        }
                                    }
                                    ganttChartTree.setWidget(rindex, cindex, label1);
                                    ganttChartService.saveCellValues(taskID, columnName, textBox.getText(), new AbstractAsyncCallback<Void>() {
                                        @Override
                                        public void success(Void result) {

                                        }
                                    });
                                }
                            });
                        } else if (requiredWidget instanceof DataListBox) {
                            final DataListBox listBox = (DataListBox) requiredWidget;
                            listBox.addValueChangeHandler(event13 -> {
                                Label label13 = new Label(listBox.getSelectedItem() != null ? listBox.getSelectedItem().getName() : "");
                                label13.getElement().setId("r" + rindex + "c" + cindex + "_" + taskID);
                                ganttChartTree.setWidget(rindex, cindex, label13);
                                if (listBox.getSelectedItem() != null) {
                                    ganttChartService.saveCellValues(taskID, columnName, listBox.getSelectedId().toString(), new AbstractAsyncCallback<Void>() {
                                        @Override
                                        public void success(Void result) {

                                        }
                                    });
                                }
                            });
                        } else if (requiredWidget instanceof DatePicker) {
                            final DatePicker datePicker = (DatePicker) requiredWidget;
                            datePicker.addChangeHandler(event12 -> {
                                TaskSingleItem userObject = (TaskSingleItem) taskItems.get("t" + taskID).getUserObject();
                                Date startDate = userObject.getStartDate();
                                Date dueDate = userObject.getEndDate();
                                if (TaskListItem.START_DATE.equals(columnName)) {
                                    startDate = datePicker.getDate();
                                } else if (TaskListItem.END_DATE.equals(columnName)) {
                                    dueDate = datePicker.getDate();
                                }
                                if (dueDate.before(startDate)) {

                                    final Label label1 = new Label(DateTimeFormat.getFormat(Utils.getShortDateFormat()).format(datePicker.getDate()));
                                    label1.getElement().setId("r" + rindex + "c" + startDateIndex + "_" + taskID);
                                    ganttChartTree.setWidget(rindex, startDateIndex, label1);

                                    final Label label2 = new Label(DateTimeFormat.getFormat(Utils.getShortDateFormat()).format(datePicker.getDate()));
                                    label2.getElement().setId("r" + rindex + "c" + endDateIndex + "_" + taskID);
                                    ganttChartTree.setWidget(rindex, endDateIndex, label2);

                                    if (label1.getText() != null && !label1.getText().isEmpty()
                                            && label2.getText() != null && !label2.getText().isEmpty()) {
                                        updateTaskStartAndEndDates(taskID, datePicker);
                                    }
                                } else {
                                    final Label label1 = new Label(DateTimeFormat.getFormat(Utils.getShortDateFormat()).format(datePicker.getDate()));
                                    label1.getElement().setId("r" + rindex + "c" + cindex + "_" + taskID);
                                    ganttChartTree.setWidget(rindex, cindex, label1);
                                    if (label1.getText() != null && !label1.getText().isEmpty()) {
                                        updateTaskDate(taskID, columnName, datePicker);
                                    }
                                }
                            });
                        } else if (requiredWidget instanceof KpiCheckBox) {
                            final KpiCheckBox checkBox = (KpiCheckBox) requiredWidget;
                            checkBox.addValueChangeHandler(event1 -> {
                                Label label12 = new Label(checkBox.getValue() ? wfmStrings.yes() : wfmStrings.no());
                                label12.getElement().setId("r" + rindex + "c" + cindex + "_" + taskID);
                                ganttChartTree.setWidget(rindex, cindex, label12);
                                ganttChartService.saveCellValues(taskID, columnName, checkBox.getValue().toString(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void success(Void result) {

                                    }
                                });
                            });
                        }
                        ganttChartTree.setWidget(rindex, cindex, requiredWidget);
                    }
                }
            }
        });

        leftContent = new FlowPanel();
        leftContent.getElement().setId("leftContent");
        leftContent.getElement().getStyle().setProperty("border", "1px 1px 0 1px");
        leftContent.add(ganttChartTree);

        leftScrollPanel = new FlowPanel();
        leftScrollPanel.getElement().setId("leftScrollPanel");
        leftScrollPanel.setStyleName("leftScrollPanelStyle");
        leftScrollPanel.setHeight((Window.getClientHeight() - 121 - headerHeight - 73) + "px");
        leftScrollPanel.add(leftContent);

        FlowPanel leftTopPanelDiv = new FlowPanel();
        leftTopPanelDiv.getElement().setId("leftTopPanelDiv");
        leftTopPanelDiv.add(leftHeaderPanel);

        leftPanel.add(leftTopPanelDiv);
        leftPanel.add(leftScrollPanel);

        //RightPanel initialization
        rightHeaderPanel = new FlexTable();
        rightHeaderPanel.setCellPadding(0);
        rightHeaderPanel.setCellSpacing(0);
        rightHeaderPanel.setSize(daysCount * getCellSize() + "px", "73px");
        rightHeaderPanel.getElement().getStyle().setTableLayout(Style.TableLayout.FIXED);

        FlowPanel rightTopPanelDiv = new FlowPanel();
        rightTopPanelDiv.getElement().setId("rightTopPanelDiv");
        rightTopPanelDiv.setStyleName("rightTopPanelStyle");
        rightTopPanelDiv.add(rightHeaderPanel);
        rightTopPanelDiv.setWidth(daysCount * getCellSize() + "px");

        rightBackgroundPanel = new FlexTable();
        rightBackgroundPanel.getElement().setId("rightBackgroundPanel");
        rightBackgroundPanel.getElement().getStyle().setProperty("borderRight", "1px solid black");
        rightBackgroundPanel.setCellPadding(0);
        rightBackgroundPanel.setCellSpacing(0);
        rightBackgroundPanel.setWidth(daysCount * getCellSize() + "px");

        boundaryPanel = new AbsolutePanel();
        boundaryPanel.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        boundaryPanel.setWidth(daysCount * getCellSize() + "px");

        initSVG(daysCount);

        rightChartPanel = new FlowPanel();
        rightChartPanel.getElement().setId("rightChartPanel");
        rightChartPanel.add(rightBackgroundPanel);
        rightChartPanel.add(boundaryPanel);
        rightChartPanel.add(svgPanel);

        rightScrollPanel = new ScrollPanel();
        rightScrollPanel.getElement().setId("rightScrollPanel");
        rightScrollPanel.setStyleName("rightScrollPanelStyle");
        rightScrollPanel.setHeight(Window.getClientHeight() - 111 - headerHeight - 73 + "px");
        rightScrollPanel.addScrollHandler(scrollEvent -> {
            verticalScroll = -1 * rightScrollPanel.getVerticalScrollPosition();
            ganttChartTree.getElement().getStyle().setTop(verticalScroll, Style.Unit.PX);

            horizontalScroll = -1 * rightScrollPanel.getHorizontalScrollPosition();
            if ("ar".equals(locale)) {
                rightHeaderPanel.getElement().getStyle().setMarginRight((-1) * horizontalScroll, Style.Unit.PX);
            } else {
                rightHeaderPanel.getElement().getStyle().setMarginLeft(horizontalScroll, Style.Unit.PX);
            }
        });
        rightScrollPanel.setWidget(rightChartPanel);

        rightPanel.add(rightTopPanelDiv);
        rightPanel.add(rightScrollPanel);

        employees = new DataListBox();
        employees.setItems(ganttItem.getEmployees().toArray(new SelectItem[]{}));
        employees.addValueChangeHandler(changeEvent -> redrawTasks(false));

        drawRightContent(startDate, daysCount);
        getTaskItems(startDate, endDate);

        //MainPanel initialization
        mainPanel = new FlowPanel();
        mainPanel.getElement().setId("mainPanel");
        mainPanel.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        if ("he".equals(locale)) {
            mainPanel.getElement().getStyle().setProperty("direction", "ltr");
        }
        mainPanel.setHeight((Window.getClientHeight() - 112) + "px");

        buttonsBar = new FlowPanel();

        if (hasEditAccess) {
            if (Utils.hasPermission(PermissionConstants.PM_TASKS_ADD)) {
                WfmButton2 addTaskButton = new WfmButton2(Property.get(TASK, wfmStrings.addMess(), wfmStrings.task()), WfmButton2.BTN_DEFAULT);
                addTaskButton.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("task|add/add" + "/" + projectID));
                buttonsBar.add(addTaskButton);
            }
        }

        refreshButton = new WfmButton2(wfmStrings.refresh(), WfmButton2.BTN_DEFAULT);
        refreshButton.addClickHandler(clickEvent -> {
            refreshButton.setEnabled(false);
            redrawTasks(false);
        });
        buttonsBar.add(refreshButton);

        filterButton = new WfmButton2("", WfmButton2.BTN_DEFAULT, "ficon--filter");
        filterButton.addClickHandler(e -> {
            filterPopup = new GanttChartFilterPopup(ganttChart, 0, 0);
            filterPopup.setLeft(filterButton.getAbsoluteLeft());
            filterPopup.setTop(filterButton.getAbsoluteTop() + filterButton.getOffsetHeight() + 1);
            filterPopup.setStartDate(startDate);
            filterPopup.setEndDate(endDate);
            filterPopup.show();
        });

        buttonsBar.add(filterButton);
        buttonsBar.add(employees);

        WfmButton2 exportButton = new WfmButton2(wfmStrings.export(), WfmButton2.BTN_DEFAULT, "ficon--file-excel");
        exportButton.addClickHandler(event -> sendExcelPdfRequest("/gantChartExportExcelReport", buttonsBar));
        buttonsBar.add(exportButton);

        if (hasEditAccess || Utils.hasPermission(PermissionConstants.PM_TASK_LIST_IMPORT_BUTTON)) {
            WfmButton2 importButton = new WfmButton2(wfmStrings.importString(), WfmButton2.BTN_DEFAULT, "ficon--upload");
            importButton.addClickHandler(event -> new ImportMPPFilePopup("/MSProjectUploadHandler", projectID));
            buttonsBar.add(importButton);
        }
        showActualCheckBox.setValue(false);
        showActualCheckBox.addValueChangeHandler(event -> redrawTasks(false));
        buttonsBar.add(showActualCheckBox);

        mainPanel.add(buttonsBar);

        zoomInButton = new WfmButton2(wfmStrings.zoomIn(), WfmButton2.BTN_DEFAULT);
        if (ganttchartType.equals(ganttTypeDaily)) {
            zoomInButton.setEnabled(false);
        }
        zoomInButton.addClickHandler(event -> {
            if (!ganttchartType.equals(ganttTypeDaily)) {
                LoadingPanel.loading(true);
                rightHeaderPanel.removeAllRows();
                rightHeaderPanel.clear();
                rightBackgroundPanel.removeAllRows();
                rightBackgroundPanel.clear();
                int daysCount12 = DateUtil.getDaysCount(startDate, endDate);
                if (ganttchartType.equals(ganttTypeWeekly)) {
                    ganttchartType = ganttTypeDaily;
                    GanttChartDayImpl daily = new GanttChartDayImpl(ganttChart);
                    daily.drawRightContent(startDate, daysCount12);
                    daily.setColumnNames(ganttChart.getColumnNames());
                    ganttChart.setGanttchartType(ganttTypeDaily);
                } else if (ganttchartType.equals(ganttTypeMonthly)) {
                    ganttchartType = ganttTypeWeekly;
                    GanttChartWeekImpl weekly = new GanttChartWeekImpl(ganttChart);
                    weekly.drawRightContent(startDate, daysCount12);
                    weekly.setColumnNames(ganttChart.getColumnNames());
                    ganttChart.setGanttchartType(ganttTypeWeekly);
                }
                ArrayList<TaskSingleItem> tasks = new ArrayList<>();
                shiftingDate = (Date) endDate.clone();
                for (String taskIDStr : taskWidgets.keySet()) {
                    TaskWidget taskWidget = taskWidgets.get(taskIDStr);
                    String[] split = taskIDStr.split("_");
                    Integer taskID = Integer.valueOf(split[1]);
                    TaskSingleItem task = (TaskSingleItem) taskItems.get("w".equals(split[0]) ? "w" + taskID : "t" + taskID).getUserObject();
                    int length = DateUtil.getDaysCount(task.getStartDate(), task.getEndDate());
                    taskWidget.setWidth(length * getCellSize());
                    int left = DateUtil.getDaysCount(startDate, task.getStartDate()) - 1;
                    taskWidget.setLeft(left * getCellSize());
                    resizeActualBar(task);
                    taskWidgets.put(taskIDStr, taskWidget);
                    tasks.add(task);
                    if (task.getStartDate().before(shiftingDate)) {
                        shiftingDate = task.getStartDate();
                    }
                }
                reDrawDependencies(tasks);
                rightHeaderPanel.setSize((daysCount12 * getCellSize()) + "px", "73px");
                rightBackgroundPanel.setWidth((daysCount12 * getCellSize()) + "px");
                boundaryPanel.setWidth(daysCount12 * getCellSize() + "px");
                svgPanel.setWidth((daysCount12 * getCellSize()) + "px");
                int dateCount = DateUtil.getDaysCount(startDate, shiftingDate);
                if (startDate.after(shiftingDate)) {
                    dateCount *= -1;
                }
                rightScrollPanel.setHorizontalScrollPosition(dateCount * getCellSize());
                if (ganttchartType.equals(ganttTypeDaily)) {
                    zoomInButton.setEnabled(false);
                }
                if (!ganttchartType.equals(ganttTypeMonthly)) {
                    zoomOutButton.setEnabled(true);
                }
                LoadingPanel.loading(false);
            }
        });
        FlowPanel buttonsPanel = new FlowPanel();
        buttonsPanel.setStyleName("buttonsPanelStyle");
        buttonsPanel.add(zoomInButton);
        zoomOutButton = new WfmButton2(wfmStrings.zoomOut(), WfmButton2.BTN_DEFAULT);
        zoomOutButton.addClickHandler(event -> {
            if (!ganttchartType.equals(ganttTypeMonthly)) {
                int daysCount1 = DateUtil.getDaysCount(startDate, endDate);
                LoadingPanel.loading(true);
                rightHeaderPanel.removeAllRows();
                rightHeaderPanel.clear();
                rightBackgroundPanel.removeAllRows();
                rightBackgroundPanel.clear();
                GanttChart chart = null;
                if (ganttchartType.equals(ganttTypeDaily)) {
                    ganttchartType = ganttTypeWeekly;
                    GanttChartWeekImpl weekly = new GanttChartWeekImpl(ganttChart);
                    weekly.drawRightContent(startDate, daysCount1);
                    weekly.setColumnNames(ganttChart.getColumnNames());
                    ganttChart.setGanttchartType(ganttTypeWeekly);
                    chart = weekly;
                } else if (ganttchartType.equals(ganttTypeWeekly)) {
                    ganttchartType = ganttTypeMonthly;
                    GanttChartMonthImpl monthly = new GanttChartMonthImpl(ganttChart);
                    monthly.drawRightContent(startDate, daysCount1);
                    monthly.setColumnNames(ganttChart.getColumnNames());
                    ganttChart.setGanttchartType(ganttTypeMonthly);
                    chart = monthly;
                }

                Integer divWidth = daysCount1 * getCellSize();
                rightHeaderPanel.setSize(divWidth + "px", "73px");
                rightBackgroundPanel.setWidth(divWidth + "px");
                boundaryPanel.setWidth(divWidth + "px");

                ArrayList<TaskSingleItem> tasks = new ArrayList<>();
                shiftingDate = (Date) endDate.clone();
                for (String taskIDStr : taskWidgets.keySet()) {
                    TaskWidget taskWidget = taskWidgets.get(taskIDStr);
                    TaskSingleItem task = taskWidget.getTask();
                    int plannedWidth = DateUtil.getDaysCount(task.getStartDate(), task.getEndDate());
                    taskWidget.setWidth(plannedWidth * getCellSize());
                    int left = DateUtil.getDaysCount(startDate, task.getStartDate()) - 1;
                    taskWidget.setLeft(left * getCellSize());
                    resizeActualBar(task);
                    taskWidgets.put(taskIDStr, taskWidget);
                    tasks.add(task);
                    if (task.getStartDate().before(shiftingDate)) {
                        shiftingDate = task.getStartDate();
                    }
                }
                reDrawDependencies(tasks);
                svgPanel.setWidth(divWidth + "px");
                int dateCount = DateUtil.getDaysCount(startDate, shiftingDate);
                if (startDate.after(shiftingDate)) {
                    dateCount *= -1;
                }
                rightScrollPanel.setHorizontalScrollPosition(dateCount * getCellSize());
                LoadingPanel.loading(false);
            }
            if (ganttchartType.equals(ganttTypeMonthly)) {
                zoomOutButton.setEnabled(false);
            }
            if (!ganttchartType.equals(ganttTypeDaily)) {
                zoomInButton.setEnabled(true);
            }
        });
        buttonsPanel.add(zoomOutButton);

        settingsButton = new WfmButton2("", WfmButton2.BTN_DEFAULT, "ficon--settings");
        settingsButton.setTitle(wfmStrings.customize());
        settingsButton.addClickHandler(event -> {
            settingsPopup = new GanttChartSettingsPopup(ganttChart);
            settingsPopup.open();
        });
        buttonsPanel.add(settingsButton);
        buttonsBar.add(buttonsPanel);

        mainContent = new FlowPanel();
        mainContent.add(leftPanel);
        mainContent.add(rightPanel);
        mainPanel.add(mainContent);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKSTREAM_ADD, GanttChart.this, (sender, args) -> redrawTasks(false));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKSTREAM_DELETED, GanttChart.this, (sender, args) -> redrawTasks(false));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, GanttChart.this, (sender, args) -> redrawTasks(false));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_EDIT, GanttChart.this, (sender, args) -> redrawTasks(false));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_DELETE, GanttChart.this, (sender, args) -> redrawTasks(false));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_PREDECESSOR_CHANGE, GanttChart.this, (sender, args) -> redrawTasks(false));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SELECTION_CHANGE_TAB, GanttChart.this, (sender, args) -> {
            ganttChartTree.getElement().getStyle().setTop((-1) * verticalScroll, Style.Unit.PX);
            rightScrollPanel.setVerticalScrollPosition((-1) * horizontalScroll);
            if ("ar".equals(locale)) {
                rightHeaderPanel.getElement().getStyle().setMarginRight((-1) * horizontalScroll, Style.Unit.PX);
                rightScrollPanel.setHorizontalScrollPosition((-1) * horizontalScroll);
            } else {
                rightHeaderPanel.getElement().getStyle().setMarginLeft(horizontalScroll, Style.Unit.PX);
                rightScrollPanel.setHorizontalScrollPosition((-1) * horizontalScroll);
            }
        });
        return mainPanel;
    }

    private void updateTaskStartAndEndDates(final Integer taskID, final DatePicker datePicker) {
        ganttChartService.updateTaskDates(taskID, String.valueOf(datePicker.getDate().getTime()), String.valueOf(datePicker.getDate().getTime()), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Void aVoid) {
                TaskSingleItem userObject = (TaskSingleItem) taskItems.get("t" + taskID).getUserObject();
                userObject.setStartDate(datePicker.getDate());
                userObject.setEndDate(datePicker.getDate());
                taskItems.get("t" + taskID).setUserObject(userObject);
                TaskWidget taskWidget = taskWidgets.get("t_" + taskID);
                if (taskWidget != null) {
                    boundaryPanel.remove(taskWidget);
                    attachTaskWidget(rindex, userObject);
                }
                if (!SVGPanel.linesById.isEmpty()) {
                    ArrayList<TaskSingleItem> tasks = new ArrayList<>();
                    for (GanttTreeItem item : taskItems.values()) {
                        tasks.add((TaskSingleItem) item.getUserObject());
                    }
                    reDrawDependencies(tasks);
                }
            }
        });
    }

    private void updateTaskDate(final Integer taskID, final String columnName, final DatePicker datePicker) {
        ganttChartService.saveCellValues(taskID, columnName, String.valueOf(datePicker.getDate().getTime()), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(Void result) {
                TaskSingleItem userObject = (TaskSingleItem) taskItems.get("t" + taskID).getUserObject();
                if (TaskListItem.START_DATE.equals(columnName)) {
                    userObject.setStartDate(datePicker.getDate());
                } else if (TaskListItem.END_DATE.equals(columnName)) {
                    userObject.setEndDate(datePicker.getDate());
                }
                taskItems.get("t" + taskID).setUserObject(userObject);
                TaskWidget taskWidget = taskWidgets.get("t_" + taskID);
                if (taskWidget != null) {
                    boundaryPanel.remove(taskWidget);
                    attachTaskWidget(rindex, userObject);
                }
                if (!SVGPanel.linesById.isEmpty()) {
                    ArrayList<TaskSingleItem> tasks = new ArrayList<>();
                    for (GanttTreeItem item : taskItems.values()) {
                        tasks.add((TaskSingleItem) item.getUserObject());
                    }
                    reDrawDependencies(tasks);
                }
            }
        });
    }

    private Widget getRequiredWidget(String value, String columnName) {
        if (TaskListItem.START_DATE.equals(columnName) || TaskListItem.END_DATE.equals(columnName)) {
            DateTimeFormat fmt = DateTimeFormat.getFormat(Utils.getShortDateFormat());
            return new DatePicker(fmt.parse(value));
        } else if (TaskListItem.COMPLETE.equals(columnName)/* || TaskListItem.ESTIMATED.equals(columnName)*/) {
            TextBox textBox = new TextBox();
            textBox.setText(value.replace("%", ""));
            Validation.addNumericKeyboardListener(textBox);
            return textBox;
        } else if (TaskListItem.PRIORITY_NAME.equals(columnName)) {
            DataListBox dataListBox = new DataListBox();
            dataListBox.setItems(getPriorities());
            return dataListBox;
        } else if (TaskListItem.BILLABLE.equals(columnName)) {
            KpiCheckBox checkBox = new KpiCheckBox();
            checkBox.setValue(value.equals(wfmStrings.yes()));
            return checkBox;
        }
        return null;
    }

    private void drawLeftHeader() {
        int index = 1;
        leftPanelWidth = 20;
        FlexTable.FlexCellFormatter leftHeaderPanelFormatter = leftHeaderPanel.getFlexCellFormatter();
        leftHeaderPanel.setHTML(0, 0, wfmStrings.name());
        leftHeaderPanelFormatter.setWidth(0, 0, "15%");
        leftHeaderPanelFormatter.getElement(0, 0).getStyle().setProperty("maxWidth", "170px");
        leftHeaderPanelFormatter.getElement(0, 0).getStyle().setProperty("minWidth", "170px");
        leftHeaderPanelFormatter.setStyleName(0, 0, "headerStyle");

        if (ganttChart.getColumnNames() == null || ganttChart.getColumnNames().isEmpty()) {
            ganttChart.setColumnNames("");
        }
        String[] columns = ganttChart.getColumnNames().split(",");
        columnsCount = columns.length;
        for (String column : columns) {
            column = column.replace(" ", "");
            if (TaskListItem.START_DATE.equals(column)) {
                startDateIndex = index;
                index = drawCell(index, 8, 80, wfmStrings.startDate(), leftHeaderPanelFormatter);
            } else if (TaskListItem.END_DATE.equals(column)) {
                endDateIndex = index;
                index = drawCell(index, 8, 80, wfmStrings.dueDate(), leftHeaderPanelFormatter);
            } else if (TaskListItem.ASSIGNED_TO.equals(column)) {
                index = drawCell(index, 10, 130, wfmStrings.assignedTo(), leftHeaderPanelFormatter);
            } else if (TaskListItem.OVERALL_STATUS_NAME.equals(column)) {
                index = drawCell(index, 8, 90, wfmStrings.overAllStatus(), leftHeaderPanelFormatter);
            } else if (TaskListItem.PRIORITY_NAME.equals(column)) {
                index = drawCell(index, 6, 70, wfmStrings.priority(), leftHeaderPanelFormatter);
            } else if (TaskListItem.ESTIMATED.equals(column)) {
                index = drawCell(index, 6, 60, wfmStrings.estimatedTime(), leftHeaderPanelFormatter);
            } else if (TaskListItem.ACTUAL_TIME.equals(column)) {
                index = drawCell(index, 7, 60, wfmStrings.actualTimeSpent(), leftHeaderPanelFormatter);
            } else if (TaskListItem.ACTUAL_START_DATE.equals(column)) {
                index = drawCell(index, 7, 70, wfmStrings.actualStartDate(), leftHeaderPanelFormatter);
            } else if (TaskListItem.ACTUAL_END_DATE.equals(column)) {
                index = drawCell(index, 7, 70, wfmStrings.actualEndDate(), leftHeaderPanelFormatter);
            } else if (TaskListItem.BILLABLE.equals(column)) {
                index = drawCell(index, 4, 45, wfmStrings.billable(), leftHeaderPanelFormatter);
            }
        }
        leftPanel.getElement().getStyle().setProperty("width", leftPanelWidth + "%");
        rightPanel.getElement().getStyle().setProperty("width", leftPanelWidth < 100 ? 100 - leftPanelWidth + "%" : "10%");
    }

    private int drawCell(int index, int percentWidth, int numberWidth, String value, FlexTable.FlexCellFormatter leftHeaderPanelFormatter) {
        leftHeaderPanel.setHTML(0, index, value);
        leftHeaderPanelFormatter.setWidth(0, index, percentWidth + "%");
        leftHeaderPanelFormatter.getElement(0, index).getStyle().setProperty("maxWidth", numberWidth + "px");
        leftHeaderPanelFormatter.getElement(0, index).getStyle().setProperty("minWidth", numberWidth + "px");
        leftHeaderPanelFormatter.setStyleName(0, index++, "headerStyle");
        leftPanelWidth += percentWidth;
        return index;
    }

    private void resizeActualBar(TaskSingleItem task) {
        if (showActualCheckBox.getValue()) {
            Element actual = DOM.getElementById("ap_" + task.getObjectID().toString());
            if (task.getActualStartDate() != null && task.getActualEndDate() != null && actual != null) {
                int actualWidth = DateUtil.getDaysCount(task.getActualStartDate(), task.getActualEndDate());
                if (actualWidth == 0) {
                    actualWidth = 1;
                }
                int actualLeft = DateUtil.getDaysCount(startDate, task.getActualStartDate()) - 1;
                actual.getStyle().setWidth(actualWidth * getCellSize(), Style.Unit.PX);
                actual.getStyle().setLeft(actualLeft * getCellSize(), Style.Unit.PX);
            }
        }
    }

    private void reloadGanttChart() {
        SinksContainerFactory.entryPoint.onHistoryChanged("project|gwtganttchart/" + projectID);
    }

    private void sendExcelPdfRequest(String excelURL, FlowPanel buttonsPanel) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("projectID", projectID.toString());
        parametersMap.put("sessionId", ClientSecurityContext.get().getSessionId());
        parametersMap.put("from", DateUtil.format2.format(startDate));
        parametersMap.put("to", DateUtil.format2.format(endDate));
        parametersMap.put("sortBy", orderBy);
        parametersMap.put("employeeID", employees.getSelectedItem() != null ? employees.getSelectedItem().getId().toString() : "");
        parametersMap.put("showActual", isShowActualBox() ? "true" : "false");
        Utils.sendPDFOrExcelRequest(buttonsPanel, CommandConstants.COMMON_URL + excelURL, parametersMap, "_blank");
    }

    public void redrawTasks(final boolean fromSettingsPopup) {
        horizontalScroll = rightScrollPanel.getHorizontalScrollPosition();
        taskItems.clear();

        startDate = DateUtil.getCompanyFirstDateOfWeek(startDate);
        endDate = DateUtil.getCompanyLastDateOfWeek(endDate);
        if (startDate.equals(endDate) || startDate.after(endDate)) {
            endDate = DateUtil.addMonths(startDate, 1);
        } else if (endDate.before(startDate)) {
            startDate = DateUtil.addMonths(endDate, -1);
        }
        LoadingPanel.loading(true);
        Integer employeeID = employees.getSelectedItem() != null ? employees.getSelectedId() : null;
        final Date finalStartDate = startDate;


        if (fromSettingsPopup) {
            leftHeaderPanel.removeAllRows();
            drawLeftHeader();
        }
        ganttChartTree.removeItems();
        ganttChartTree.clear();
        boundaryPanel.clear();
        int daysCount = DateUtil.getDaysCount(finalStartDate, endDate);
        boundaryPanel.clear();

        DOM.setStyleAttribute(boundaryPanel.getElement(), "marginTop", "-" + rightBackgroundPanel.getElement().getStyle().getHeight());
        svgPanel.setWidth((daysCount * getCellSize()) + "px");
        LoadingPanel.loading(false);

        rowIndex = 0;
        loadData(projectID, employeeID, startDate, endDate, orderBy, showActualCheckBox.getValue(), LoadItemType.WORKSTREAM, 0);
        rightScrollPanel.setHorizontalScrollPosition(horizontalScroll);
    }

    public void reDrawDependencies(ArrayList<TaskSingleItem> result) {
        svgPanel.clear();
        svgDefs = new SVGDefs();
        svgDefs.add(svgArrowMarker);
        svgPanel.add(svgDefs);
        drawDependencies(result);
    }

    protected abstract void drawRightContent(Date startDate, int daysCount);

    public void drawDependencies(ArrayList<TaskSingleItem> tasks) {
        for (TaskSingleItem task : tasks) {
            Point[] path = null;
            TaskWidget taskWidget = taskWidgets.get("t_" + task.getObjectID());
            if (taskWidget != null) {
                if (task.getPredecessorTasks() != null && task.getPredecessorTasks().length > 0) {
                    Rectangle toRect = new Rectangle(taskWidget.getLeft(), taskWidget.getTop(), taskWidget.getWidth(), taskWidgetHeight);
                    for (int k = 0; k < task.getPredecessorTasks().length; k++) {
                        GanttTreeItem wsItem2 = taskItems.get("t" + task.getPredecessorTasks()[k].getId());
                        if (tasks.contains(wsItem2.getUserObject())) {
                            TaskWidget fromTask = taskWidgets.get("t_" + task.getPredecessorTasks()[k].getId());
                            Rectangle fromRect = new Rectangle(fromTask.getLeft(), fromTask.getTop(), fromTask.getWidth(), taskWidgetHeight);
                            PredecessorType type = PredecessorType.FS;
                            if (fromRect != null && toRect != null) {
                                path = CalculatorFactory.get(type).calculateWithOffset(fromRect, toRect);
                                String key = task.getObjectID() + "-" + task.getPredecessorTasks()[k].getId();
                                renderConnector(path, "p_" + key);
                                GanttChartDragDropController.predecessorPaths.put(key, getSVGPath(path));
                            }
                        }
                    }
                }
            }
        }
    }

    private void drawTasks(ArrayList<TaskSingleItem> tasks) {
        if (tasks.size() > 0) {
            shiftingDate = tasks.get(0).getStartDate();
        }
        for (TaskSingleItem task : tasks) {
            attachTaskItem(rowIndex++, task);
            if (task.getStartDate().before(shiftingDate)) {
                shiftingDate = task.getStartDate();
            }
        }
        this.tasks = tasks;
        if (tasks.size() > 0) {
            String height = rowIndex * (cellSize + 1) + "px";
            leftScrollPanel.setHeight((Window.getClientHeight() - 121 - headerHeight - 73) + "px");
            rightScrollPanel.setHeight((Window.getClientHeight() - 121 - headerHeight - 73) + "px");
            rightChartPanel.setHeight(height);
            rightBackgroundPanel.setHeight(height);
            rightBackgroundPanel.getFlexCellFormatter().setHeight(0, 0, (tasks.size() * (cellSize + 1) - 1) + "px");
            leftContent.setHeight(height);
            DOM.setStyleAttribute(boundaryPanel.getElement(), "marginTop", "-" + height);
            boundaryPanel.setHeight(height);
            svgPanel.setHeight(height);

            int dateCount = DateUtil.getDaysCount(startDate, shiftingDate);
            if (startDate.after(shiftingDate)) {
                dateCount *= -1;
            }
            rightScrollPanel.setHorizontalScrollPosition(dateCount * getCellSize());
        }
    }

    public void initSVG(int daysCount) {
        svgPanel = new SVGPanel(this.ganttChart);
        svgPanel.setStyleName("svgPanelStyle");
        svgPanel.setPointerEvents("none");
        svgPanel.setShapeRendering("crispEdges");
        svgPanel.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        svgPanel.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
        svgPanel.getElement().getStyle().setTop(0, Style.Unit.PX);
        svgPanel.getElement().getStyle().setLeft(0, Style.Unit.PX);
        svgPanel.setSize(daysCount * getCellSize() + "px", "504px");
        svgPanel.getElement().getStyle().setMarginTop(0d, Style.Unit.PX);

        // add the arrow marker to the svg panel
        svgArrowMarker = new SVGMarker("Triangle", .5, "black", "black");
        svgArrowMarker.setViewBox("0 0 8 8");
        svgArrowMarker.setRefX(0);
        svgArrowMarker.setRefY(4);
        svgArrowMarker.setMarkerWidth(8);
        svgArrowMarker.setMarkerHeight(8);
        svgArrowMarker.add(new SVGPath("M 0 0 L 8 4 L 0 8 z"));
        svgArrowMarker.setOrient("auto");
        svgArrowMarker.setMarkerUnits("strokeWidth");

        svgDefs = new SVGDefs();
        svgDefs.add(svgArrowMarker);
        svgPanel.add(svgDefs);
    }

    public SVGPath renderConnector(Point[] path, String id) {
        SVGPath svgPath = getSVGPath(path);
        svgPath.getElement().setId(id);
        renderConnector(svgPath);
        return svgPath;
    }

    public void renderConnector(SVGPath svgPath) {
        svgPanel.addWidget(svgPath);
    }

    public void removeConnector(SVGPath svgPath) {
        if (svgPath != null) {
            svgPanel.removePath(svgPath);
        }
    }

    public SVGPath getSVGPath(Point[] path) {
        SVGPath line = new SVGPath();
        line.setValue(path);
        line.setStroke("black");
        line.setFill("none");
        line.setStrokeWidth(1);
        line.setMarkerEnd(svgArrowMarker);
        return line;
    }

    public SVGMarker getSvgArrowMarker() {
        return svgArrowMarker;
    }

    public void attachTaskItem(int i, TaskSingleItem task) {
        final GanttTreeItem treeItem = new GanttTreeItem(task, columnsCount);
//        treeItem.setRow(i);
        taskItems.put(task.isWorkstream() ? "w" + task.getObjectID() : "t" + task.getObjectID(), treeItem);
        ganttChartTree.addItem(treeItem);

        if (task.getWorkstreamID() != null) {
            Object parent = taskItems.get("w" + task.getWorkstreamID());
            if (parent != null) {
                GanttTreeItem parentItem = (GanttTreeItem) parent;
                parentItem.addItem(treeItem, true);
                parentItem.setState(true);
            }
            if (!workstreamTasks.containsKey(task.getWorkstreamID())) {
                ArrayList<TaskSingleItem> items = new ArrayList<>();
                items.add(task);
                workstreamTasks.put(task.getWorkstreamID(), items);
            } else {
                ArrayList<TaskSingleItem> items = workstreamTasks.get(task.getWorkstreamID());
                items.add(task);
            }
        }
        attachTaskWidget(treeItem.getRow(), task);
    }

    public TaskWidget getTaskWidget(int i, TaskSingleItem task) {
        return new TaskWidget(this, task, i);
    }

    public TaskWidget attachTaskWidget(int i, TaskSingleItem task) {
        TaskWidget svgPanel1 = getTaskWidget(i, task);
        boundaryPanel.add(svgPanel1);
        if (!task.isWorkstream()) {
            taskWidgets.put("t_" + task.getObjectID(), svgPanel1);
        } else {
            taskWidgets.put("w_" + task.getObjectID(), svgPanel1);
        }
        return svgPanel1;
    }

    public int getCellSize() {
        int cellSize = Constants.cellSize;
        if (ganttchartType.equals(ganttTypeWeekly)) {
            cellSize = cellSize / 2;
        } else if (ganttchartType.equals(ganttTypeMonthly)) {
            cellSize = cellSize / 4;
        }
        return cellSize + 1;
    }

    protected String getDayName(int i) {
        int dayNumber = i % 7;
        String dayName = "";
        if ("1".equals(Utils.userSettings.get(TIMESHEET_WEEK_START))) { //if week starts from Sunday
            dayName = DateUtil.daysName1[dayNumber];
        } else if ("7".equals(Utils.userSettings.get(TIMESHEET_WEEK_START))) { //if week starts from Saturday
            dayName = DateUtil.daysName2[dayNumber];
        } else {// week starts from Monday
            dayName = DateUtil.daysName[dayNumber];
        }
        return dayName;
    }

    protected String getDayStyle(int i, boolean isTop) {
        int dayNumber = i % 7;
        if (dayNumber == 5 || dayNumber == 6) {
            if (isTop) {
                return "weekEndStyle";
            } else {
                return "weekEndtableRows";
            }
        } else {
            return "tableCellStyle";
        }
    }

    public void getTaskItems(Date startDate, Date endDate) {
        LoadingPanel.loading(true);
        if (showActualCheckBox == null) {
            showActualCheckBox = new KpiCheckBox(wfmStrings.showActual());
        }
        rowIndex = 0;
        loadData(projectID, (employees != null && employees.getSelectedItem() != null) ? employees.getSelectedItem().getId() : null, startDate, endDate, orderBy, showActualCheckBox.getValue(), LoadItemType.WORKSTREAM, 0);
    }

    /**
     * This method fetches workstreams and tasks list as partial
     * for workstreams, it fetches workstream per 5 count
     * for tasks, it fetched task per 10 count
     *
     * @param projectID
     * @param employeeID
     * @param from
     * @param to
     * @param sortBy
     * @param showActual
     * @param loadType
     * @param start
     */
    private void loadData(final Integer projectID, final Integer employeeID, final Date from, final Date to, final String sortBy, final boolean showActual, final LoadItemType loadType, final int start) {
        ganttChartService.getGanttChartTasks(projectID, employeeID, from, to, sortBy, showActual, loadType, start, new AbstractAsyncCallback<ListResult<TaskSingleItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                super.failure(throwable);
            }

            @Override
            public void success(ListResult<TaskSingleItem> result) {
                LoadingPanel.loading(false);
                if (result.getList() != null && !result.getList().isEmpty()) {
                    drawTasks(new ArrayList<TaskSingleItem>(result.getList()));
                    drawDependencies(new ArrayList<TaskSingleItem>(result.getList()));
                    loadData(projectID, employeeID, from, to, sortBy, showActual, loadType, start + result.getTotal());
                    refreshButton.setEnabled(true);
                } else if ((result.getList() == null || result.getList().isEmpty()) && !loadType.equals(LoadItemType.TASK)) {
                    loadData(projectID, employeeID, from, to, sortBy, showActual, LoadItemType.TASK, 0);
                }
            }
        });
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = DateUtil.getCompanyFirstDateOfWeek(startDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = DateUtil.getCompanyLastDateOfWeek(endDate);
    }

    public boolean isShowActualBox() {
        return showActualCheckBox.getValue();
    }

    public FlowPanel getMainPanel() {
        return mainPanel;
    }

    public String getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }

    public boolean isHasEditAccess() {
        return hasEditAccess;
    }

    public void setGanttchartType(String ganttchartType) {
        this.ganttchartType = ganttchartType;
    }

    public SelectItem[] getPriorities() {
        return priorities;
    }

    public void setPriorities(SelectItem[] priorities) {
        this.priorities = priorities;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getEmployeeID() {
        return employees.getSelectedItem() != null ? employees.getSelectedId() : null;
    }

    public String getSortBy() {
        return orderBy;
    }
}
