package com.edatasite.workforce.gwt.timesheet.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.SelectItemCell;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkAndTextCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTimesheetModel;
import com.edatasite.workforce.gwt.timesheet.client.rpc.StatData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetReport;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetServiceAsync;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetSettings;
import com.edatasite.workforce.gwt.timesheet.client.ui.TimesheetEditPopupCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TaskDatabase implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final TimesheetServiceAsync timesheetService = TimesheetService.App.get();
    private KpiDataGrid<FastTaskTransfer> dataGrid;
    private final TimesheetEditPopupCell editPopupCell = new TimesheetEditPopupCell();
    private TimesheetSettings timesheetSettings;
    private FastTimesheetModel model;
    private final SelectItem[] statuses = new SelectItem[4];
    private final List<SelectItem> statusNames;
    private final HashMap<Integer, FastTaskTransfer> statDataMap = new HashMap<>();
    private Date dateboxDate;
    private int totalWeeklyActual = 0, totalMonthlyActual = 0;
    private int totalWeeklyPlanned = 0, totalMonthlyPlanned = 0;

    /**
     * The singleton instance of the database.
     */
    private static TaskDatabase instance;
    private Column<FastTaskTransfer, String[]> taskNameColumn;
    private Column<FastTaskTransfer, SafeHtml> totalColumn;
    private Column<FastTaskTransfer, SelectItem> statusColumn;
    private Column<FastTaskTransfer, String> estimatedColumn;
    private Column<FastTaskTransfer, String> completedColumn;
    private Column<FastTaskTransfer, SafeHtml> autoMaticCompletedColumn;
    private List<Column<FastTaskTransfer, TimesheetDataItem>> timesheetColumns;

    interface Template extends SafeHtmlTemplates {

        @Template("<dl class=\"statAttrib\">{0}</dl>")
        SafeHtml dl(SafeHtml contents);

        @Template("<dt>{0}</dt>")
        SafeHtml dt(SafeHtml contents);

        @Template("<dd class=\"hovertooltip\">{0}</dd>")
        SafeHtml dd(SafeHtml contents);

    }

    static Template template;

    /**
     * Get the singleton instance of the contact database.
     *
     * @return the singleton instance
     */
    public static TaskDatabase get() {
        if (instance == null) {
            instance = new TaskDatabase();
        }
        return instance;
    }

    /**
     * The provider that holds the list of contacts in the database.
     */
    private final ListDataProvider<FastTaskTransfer> dataProvider = new ListDataProvider<>();


    /**
     * Construct a new contact database.
     */
    @Deprecated
    //TODO there is no guarantee that 173 is waiting or 79 is completed status id of the time sheet in DB
    private TaskDatabase() {
        statuses[0] = new SelectItem(2, wfmStrings.notStarted());
        statuses[1] = new SelectItem(3, wfmStrings.inProgress());
        statuses[2] = new SelectItem(79, wfmStrings.completed());
        statuses[3] = new SelectItem(173, wfmStrings.waitingForSomeone());
        statusNames = new ArrayList<>();
        Collections.addAll(statusNames, statuses);
        if (template == null) {
            template = GWT.create(Template.class);
        }
    }

    /**
     * Add a display to the database. The current range of interest of the display
     * will be populated with data.
     *
     * @param display a {@Link com.google.gwt.view.client.HasData}.
     */
    public void addDataDisplay(HasData<FastTaskTransfer> display) {
        dataProvider.addDataDisplay(display);
        this.dataGrid = (KpiDataGrid<FastTaskTransfer>) display;
        dataGrid.setTableBuilder(new CustomTableBuilder());
    }

    public void generateTasks(FastTimesheetModel model) {
        this.model = model;
        calculateTotalHours();
        initTableColumns();
        supplyProvider();
    }

    private void calculateTotalHours() {

        int totalWeek = 0, totalMonth = 0;
        final TimesheetReport[] weeklyReport = model.getWeeklyStatistics();
        final TimesheetReport[] monthlyReport = model.getMonthlyStatistics();

        for (TimesheetReport aWeeklyReport : weeklyReport) {
            totalWeek += aWeeklyReport.getSum();
        }

        for (TimesheetReport aMonthlyReport : monthlyReport) {
            totalMonth += aMonthlyReport.getSum();
        }

        totalWeeklyActual = totalWeek;
        totalMonthlyActual = totalMonth;

        int weeklyPlan = 0;
        for (int i = 0; i < model.getDates().length; i++) {
            weeklyPlan += model.getTimeslotItem().getWeekDaysPlannedTime()[i];
        }
        totalWeeklyPlanned = weeklyPlan;

        totalMonthlyPlanned = model.getTimeslotItem().getMonthlyPlannedTime();
    }

    private void supplyProvider() {
        List<FastTaskTransfer> tasks = dataProvider.getList();
        tasks.clear();
        for (FastTaskTransfer taskTransfer : model.getTransferTasks()) {
            tasks.add(taskTransfer);
            if (taskTransfer.getStatData() != null) {
                statDataMap.put(taskTransfer.getProjectId(), taskTransfer);
            }
        }
    }

    private void updateDailyTimesheetMinutes(TimesheetDataItem currentItem, int day) {
        for (FastTaskTransfer taskTransfer : model.getTransferTasks()) {
            taskTransfer.getDataItems()[day].setTimesheetMinutes(currentItem.getTimesheetMinutes());
        }
    }

    public void clear() {
        dataProvider.getList().clear();
    }

    /**
     * Refresh all displays.
     */
    public void refreshDisplays() {
        dataProvider.refresh();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {
        int columnCount = dataGrid.getColumnCount();
        for (int i = columnCount; i > 0; --i) {
            dataGrid.removeColumn(i - 1);
        }

        // Task name.
        taskNameColumn = new Column<FastTaskTransfer, String[]>(new SimpleLinkAndTextCell()) {
            @Override
            public String[] getValue(FastTaskTransfer object) {
                String[] values = new String[2];
                values[0] = object.getTaskNumber() + " - " + object.getEmplTaskName();
                String related = object.getRelations() != null ? object.getRelations() : "";
                if (!"".equals(related)) {
                    related = "Related to: " + related;
                }
                values[1] = related;
                return values;
            }
        };

        taskNameColumn.setFieldUpdater((index, object, value) -> SinksContainerFactory.entryPoint.onHistoryChanged((object.isIssue() ? "issue/" : "task/") + object.getTaskId().toString(), object.getTaskNumber(), object.getEmplTaskName()));
        dataGrid.addColumn(taskNameColumn, SafeHtmlUtils.fromSafeConstant(Property.get(Constants.TASK, wfmStrings.task())), SafeHtmlUtils.fromTrustedString("<span>" + wfmStrings.dailyTotal() + "</span></br><span>" + wfmStrings.dailyTotalPlanned() + "</span>"));
        dataGrid.setColumnWidth(taskNameColumn, 18, Style.Unit.PCT);

        timesheetColumns = new ArrayList<>();
        editPopupCell.setTimesheetSettings(getTimesheetSettings());
        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(Utils.getTimesheetDateFormat() != null ? Utils.getTimesheetDateFormat() : "EEE, MM/d");
        for (int i = 0; i < model.getDates().length; i++) {

            final int t = i;
            final Header<SafeHtml> totalFooter = new Header<SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue() {
                    String stringBuilder = "<div style=\"width: 100%;\" class=\"mod_text--center\">" +
                            Utils.formatMinutes(model.getDailyStatistics()[t]) +
                            "</br>" +
                            Utils.formatMinutes(model.getTimeslotItem().getWeekDaysPlannedTime()[t]) +
                            "</div>";

                    return SafeHtmlUtils.fromTrustedString(stringBuilder);
                }
            };

            Column<FastTaskTransfer, TimesheetDataItem> timesheetValues = new Column<FastTaskTransfer, TimesheetDataItem>(editPopupCell) {

                @Override
                public TimesheetDataItem getValue(FastTaskTransfer object) {
                    return object.getDataItems()[t];
                }
            };
            final String dateHeaderText;
            String date = dateTimeFormat.format(model.getDates()[t].getNonConvertedDate());
            if (model.getDates()[t].getNonConvertedDate().compareTo(model.getToday().getNonConvertedDate()) == 0) {
                dateHeaderText = "<b>" + date + "</b>";
            } else {
                dateHeaderText = date;
            }
            final StringBuilder stringBuilder = new StringBuilder();
            if (model.getTimeslotItem().getHoliday()[t]) {
                stringBuilder.append("<div style=\"width: 100%; text-align: center; color: red;\">");
            } else if (model.getTimeslotItem().getLR()[t]) {
                stringBuilder.append("<div style=\"width: 100%; text-align: center; color: #0077BF;\">");
            } else {
                stringBuilder.append("<div style=\"width: 100%; text-align: center;\">");
            }
            stringBuilder.append(dateHeaderText);
            stringBuilder.append("</div>");

            Header<SafeHtml> dateHeader = new Header<SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue() {
                    return SafeHtmlUtils.fromTrustedString(stringBuilder.toString());
                }
            };

            timesheetValues.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            dataGrid.addColumn(timesheetValues, dateHeader, totalFooter);
            timesheetValues.setFieldUpdater((index, object, value) -> {
                // Called when the user changes the value.
                timesheetService.applyUpdates(object.getDataItems()[t], null, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void failure(Throwable throwable) {
                        value.setStatus(TIMESHEET_ENTRY_FAILED);
                        refreshDisplays();
                    }

                    @Override
                    public void success(Integer result) {
                        updateDailyTimesheetMinutes(object.getDataItems()[t], t);
                        value.setStatus(TIMESHEET_ENTRY_NOTSUBMITTED);
                        value.setOldEmployeeTaskID(0);//after updating timesheet hour oldEmployeeTaskID is not needed
                        value.getOldEmployeeTaskIDList().clear();//after updating timesheet hour oldEmployeeTaskIDs not needed
                        if ("true".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL)) && value.getMinutes() != 0) {//this needed to make the cell "green" - approved if automatic approval is chosen
                            value.setStatus(TIMESHEET_ENTRY_APPROVED);
                            value.setAutoApproved(true);
                        }
                        if ("true".equals(Utils.userSettings.get(ISAUTOMATICWAITINGFORAPPROVAL))) {
                            value.setStatus(TIMESHEET_ENTRY_WAITING);
                        }
                        updateTaskStatus(object, statuses[1]);
                        object.setTotalMinutes(object.getTotalMinutes() + value.getDifference());
                        model.getDailyStatistics()[t] = model.getDailyStatistics()[t] + value.getDifference();
                        calculateStatData(object.getProjectId(), value.getDifference(), t);
                        refreshDisplays();
                        dataGrid.redrawFooters();
                    }
                });

            });
            dataGrid.setColumnWidth(timesheetValues, 8, Style.Unit.PCT);
            timesheetColumns.add(timesheetValues);
        }

        // Completed.
        if ("true".equals(Utils.userSettings.get(ISAUTOMATIC))) {
            autoMaticCompletedColumn = new Column<FastTaskTransfer, SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue(FastTaskTransfer object) {
                    final StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("<div style=\"width: 100%; text-align: center;\">");
                    if (!"".equals(object.getPercentCompleted())) {
                        stringBuilder.append(Float.valueOf(object.getPercentCompleted())).append("%");
                    } else {
                        stringBuilder.append("0%");
                    }
                    stringBuilder.append("</div>");
                    return SafeHtmlUtils.fromTrustedString(stringBuilder.toString());
                }

                @Override
                public void render(Cell.Context context, FastTaskTransfer object, SafeHtmlBuilder sb) {
                    if (object != null) {
                        sb.append(SafeHtmlUtils.fromTrustedString(validatePercentComplate2(object.getPercentCompleted() + "%")));
                    }
                }
            };

            Header<SafeHtml> totalHeader = new Header<SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue() {
                    String stringBuilder = "<div style=\"width: 100%; text-align: center;\">" +
                            wfmStrings.completed() + "%" +
                            "</div>";
                    return SafeHtmlUtils.fromTrustedString(stringBuilder);
                }
            };

            dataGrid.addColumn(autoMaticCompletedColumn, totalHeader, new Header<SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue() {
                    return SafeHtmlUtils.fromTrustedString("<span>" + wfmStrings.weekly() + ":</span><br/><span>" + wfmStrings.weekly() + ":</span>");
                }
            });
            dataGrid.setColumnWidth(autoMaticCompletedColumn, 6, Style.Unit.PCT);
        } else {
            SafeHtmlRenderer<String> renderer = new SafeHtmlRenderer<String>() {
                @Override
                public SafeHtml render(String object) {
                    object = validatePercentComplate(object);
                    if (!"".equals(object)) {
                        return SafeHtmlUtils.fromString(validatePercentComplate2(String.valueOf(Float.valueOf(object))) + "%");
                    }
                    return SafeHtmlUtils.fromString("0%");
                }

                @Override
                public void render(String object, SafeHtmlBuilder builder) {
                }
            };
            completedColumn = new Column<FastTaskTransfer, String>(new EditTextCell(renderer)) {

                @Override
                public String getValue(FastTaskTransfer object) {
                    return String.valueOf(object.getPercentCompleted());
                }
            };

            completedColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            dataGrid.addColumn(completedColumn, new Header<SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue() {
                    return SafeHtmlUtils.fromSafeConstant(wfmStrings.completed() + "%");
                }
            }, new Header<SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue() {
                    return SafeHtmlUtils.fromTrustedString("<span>" + wfmStrings.weekly() + ":</span><br/><span>" + wfmStrings.weekly() + ":</span>");
                }
            });
            completedColumn.setFieldUpdater((index, object, value) -> {
                value = validatePercentComplate(value);
                if (!"".equals(value)) {
                    float percent = Float.parseFloat(value);
                    if (percent != object.getPercentCompleted()) {
                        object.setPercentCompleted(0);
                        timesheetService.updatePercentCompleted(object.getEmplTaskId(), percent, true, new AbstractAsyncCallback<Float>() {
                            public void failure(Throwable throwable) {
                                refreshDisplays();
                            }

                            public void success(Float o) {
                                object.setPercentCompleted(o);
                                refreshDisplays();
                            }
                        });
                    }

                } else {
                    refreshDisplays();
                }
            });
            dataGrid.setColumnWidth(completedColumn, 6, Style.Unit.PCT);
        }

        // Estimated time.
        estimatedColumn = new Column<FastTaskTransfer, String>(new TextCell()) {
            @Override
            public String getValue(FastTaskTransfer object) {
                return Utils.formatMinutes(object.getEstimatedTime());
            }
        };

        SafeHtml weeklyFooter = () -> "<div class=\"weeklyTotal\"><span>" + Utils.formatMinutes(getTotalWeeklyActual()) + "</span><br/><span>" +
                Utils.formatMinutes(totalWeeklyPlanned) + "</span></div>";

        estimatedColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        dataGrid.addColumn(estimatedColumn, SafeHtmlUtils.fromSafeConstant(wfmStrings.estimatedTime()), weeklyFooter);
        dataGrid.setColumnWidth(estimatedColumn, 6, Style.Unit.PCT);

        // Total time.
        totalColumn = new Column<FastTaskTransfer, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(FastTaskTransfer object) {
                String stringBuilder = "<div style=\"width: 100%; text-align: center;\">" +
                        Utils.formatMinutes(object.getTotalMinutes()) +
                        "</div>";
                return SafeHtmlUtils.fromTrustedString(stringBuilder);
            }
        };

        Header<SafeHtml> totalHeader = new Header<SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue() {
                String stringBuilder = "<div style=\"width: 100%; text-align: center;\">" +
                        wfmStrings.total() +
                        "</div>";
                return SafeHtmlUtils.fromTrustedString(stringBuilder);
            }
        };

        dataGrid.addColumn(totalColumn, totalHeader, new Header<SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue() {
                return SafeHtmlUtils.fromTrustedString("<span>" + wfmStrings.monthly() + ":</span><br/><span>" + wfmStrings.monthly() + ":</span>");
            }
        });
        dataGrid.setColumnWidth(totalColumn, 6, Style.Unit.PCT);

        //Status
        SelectItemCell selectionCell = new SelectItemCell(statusNames);
        statusColumn = new Column<FastTaskTransfer, SelectItem>(selectionCell) {
            @Override
            public SelectItem getValue(FastTaskTransfer object) {
                return new SelectItem(object.getTaskStatus().getStatus(), object.getTaskStatus().getStatusName());
            }
        };

        SafeHtml monthlyFooter = () -> "<div class=\"monthlyTotal\"><span>" + Utils.formatMinutes(getTotalMonthlyActual()) + "</span><br/><span>" +
                Utils.formatMinutes(this.totalMonthlyPlanned) + "</span></div>";

        dataGrid.addColumn(statusColumn, SafeHtmlUtils.fromSafeConstant(wfmStrings.status()), monthlyFooter);
        statusColumn.setFieldUpdater((index, object, value) -> updateTaskStatus(object, value, true));
        dataGrid.setColumnWidth(statusColumn, 8, Style.Unit.PCT);
    }

    private void updateTaskStatus(FastTaskTransfer object, SelectItem value) {
        updateTaskStatus(object, value, false);
    }

    private void updateTaskStatus(FastTaskTransfer object, SelectItem value, boolean isForceChangeStatus) {
        if (object.getTaskStatus().getStatus() != value.getId()) {
            object.getTaskStatus().setStatus(value.getId());
            object.getTaskStatus().setStatusName(value.getName());
            object.getTaskStatus().setForceChangeStatus(isForceChangeStatus);
            timesheetService.updateStatus(object.getTaskStatus(), new AbstractAsyncCallback<Void>() {
                @Override
                public void success(Void result) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESHEET_TASK_STATUS_CHANGED, result, dataGrid);
                }
            });
        }
    }

    public void setTotal(int itemMinutes, boolean addToMonth) {
        setTotalWeeklyActual(getTotalWeeklyActual() + itemMinutes);
        if (addToMonth) {
            setTotalMonthlyActual(getTotalMonthlyActual() + itemMinutes);
        }
    }

    private void calculateStatData(Integer projectId, int itemMinutes, int dateIndex) {
        boolean addToMonth = true;
        if (model.getDate(0).getDate().getDate() > model.getDate(6).getDate().getDate()) {
            addToMonth = model.getDate(dateIndex).getDate().getMonth() == dateboxDate.getMonth();
        }
        setTotal(itemMinutes, addToMonth);
        FastTaskTransfer firstTask = statDataMap.get(projectId);
        if (firstTask != null && firstTask.getStatData() != null) {
            firstTask.getStatData().setWeekly(firstTask.getStatData().getWeekly() != null ? (firstTask.getStatData().getWeekly() + itemMinutes) : itemMinutes);
            if (addToMonth) {
                firstTask.getStatData().setMonthly(firstTask.getStatData().getMonthly() != null ? (firstTask.getStatData().getMonthly() + itemMinutes) : itemMinutes);
            }
        }
    }

    private String validatePercentComplate2(String value) {
        if (!value.equals("")) {
            if (value.endsWith("%")) {
                value = value.substring(0, value.length() - 1);
            }
            try {
                float percent = Float.parseFloat(value);
                String[] st = value.split("\\.");
                if (Integer.valueOf(st[0]) > 100 && !Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                    return "100";
                } else {
                    BigDecimal bigDecimal = new BigDecimal(percent);
                    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN); // Number 6 is RoundingMode.HALF_EVEN. is manually set to avoid excessive import
                    percent = bigDecimal.floatValue();
                    return String.valueOf(percent);
                }
            } catch (NumberFormatException e) {
                return "";
            }
        }
        return "";
    }

    private String validatePercentComplate(String value) {
        if (!value.equals("")) {
            if (value.endsWith("%")) {
                value = value.substring(0, value.length() - 1);
            }
            try {
                float percent = Float.parseFloat(value);
                String[] st = value.split("\\.");
                if (Integer.valueOf(st[0]) > 100) {
                    return value = "100";
                } else {
                    BigDecimal bigDecimal = new BigDecimal(percent);
                    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN); // Number 6 is RoundingMode.HALF_EVEN. is manually set to avoid excessive import
                    percent = bigDecimal.floatValue();
                    return String.valueOf(percent);
                }
            } catch (NumberFormatException e) {
                return "";
            }
        }
        return "";
    }

    private class CustomTableBuilder extends AbstractCellTableBuilder<FastTaskTransfer> {

        private final String rowStyle;
        private final String oddRowStyle;
        private final String cellStyle;
        private final String oddCellStyle;

        @SuppressWarnings("deprecation")
        CustomTableBuilder() {
            super(dataGrid);

            // Cache styles for faster access.
            AbstractCellTable.Style style = dataGrid.getResources().style();
            rowStyle = style.evenRow();
            oddRowStyle = style.oddRow();
            cellStyle = style.cell() + " " + style.evenRowCell();
            oddCellStyle = style.cell() + " " + style.oddRowCell();
        }

        @SuppressWarnings("deprecation")
        @Override
        public void buildRowImpl(FastTaskTransfer taskTransfer, int absRowIndex) {
            StatData statData = taskTransfer.getStatData();
            if (taskTransfer.isShowStat() && statData != null) {
                SafeHtml safeHtml = buildStat(statData);
                TableRowBuilder row = startRow();
                TableCellBuilder td = row.startTD().colSpan(12);
                td.className("cellBasedWidget-mod__marked-cell");
                td.html(safeHtml).endTD();
                row.endTR();

            }
            buildContactRow(taskTransfer, absRowIndex);

        }

        private SafeHtml buildStat(StatData statData) {
            SafeHtmlBuilder statBuilder = new SafeHtmlBuilder();

            if (statData.getProject() != null) {
                String titleProject = Property.get(Constants.PROJECT, wfmStrings.project()).concat(":");
                statBuilder.append(template.dt(SafeHtmlUtils.fromString(titleProject)));
                if (statData.getProjectFullName().length() != statData.getProject().length()) {
                    statBuilder.append(template.dd(SafeHtmlUtils.fromSafeConstant(statData.getProject() + "<span class=\"tooltiptext\">" + statData.getProjectFullName() + "</span>")));
                } else {
                    statBuilder.append(template.dd(SafeHtmlUtils.fromSafeConstant(statData.getProject())));
                }
            }

            if (statData.getClient() != null) {
                statBuilder.append(template.dt(SafeHtmlUtils.fromString(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()).concat(":"))));
                statBuilder.append(template.dd(SafeHtmlUtils.fromSafeConstant(statData.getClient())));
            }

            statBuilder.append(template.dt(SafeHtmlUtils.fromString(wfmStrings.weekly().concat(":"))));
            statBuilder.append(template.dd(SafeHtmlUtils.fromString(Utils.formatMinutes(statData.getWeekly()))));

            statBuilder.append(template.dt(SafeHtmlUtils.fromString(wfmStrings.monthly().concat(":"))));
            statBuilder.append(template.dd(SafeHtmlUtils.fromString(Utils.formatMinutes(statData.getMonthly()))));

            return template.dl(statBuilder.toSafeHtml());
        }


        /**
         * Build a row.
         *
         * @param task        the contact info
         * @param absRowIndex the absolute row index
         */
        @SuppressWarnings("deprecation")
        private void buildContactRow(FastTaskTransfer task, int absRowIndex) {
            int column = 0;

            TableRowBuilder row = startRow();

            TableCellBuilder td = row.startTD();
            renderCell(td, createContext(column++), taskNameColumn, task);
            td.endTD();

            for (Column<FastTaskTransfer, TimesheetDataItem> column1 : timesheetColumns) {
                td = row.startTD();
                renderCell(td, createContext(column++), column1, task);
                td.endTD();
            }

            if ("true".equals(Utils.userSettings.get(ISAUTOMATIC))) {
                td = row.startTD();
                renderCell(td, createContext(column++), autoMaticCompletedColumn, task);
                td.endTD();
            } else {
                td = row.startTD();
                renderCell(td, createContext(column++), completedColumn, task);
                td.endTD();
            }

            td = row.startTD();
            renderCell(td, createContext(column++), estimatedColumn, task);
            td.endTD();

            td = row.startTD();
            renderCell(td, createContext(column++), totalColumn, task);
            td.endTD();

            td = row.startTD();
            renderCell(td, createContext(column++), statusColumn, task);
            td.endTD();

            row.endTR();
        }
    }

    public TimesheetSettings getTimesheetSettings() {
        return timesheetSettings;
    }

    public void setTimesheetSettings(TimesheetSettings timesheetSettings) {
        this.timesheetSettings = timesheetSettings;
    }

    public void setModel(FastTimesheetModel model) {
        this.model = model;
    }

    public void setDateboxDate(Date dateboxDate) {
        this.dateboxDate = dateboxDate;
    }

    private int getTotalWeeklyActual() {
        return totalWeeklyActual;
    }

    private void setTotalWeeklyActual(int totalWeeklyActual) {
        this.totalWeeklyActual = totalWeeklyActual;
    }

    private int getTotalMonthlyActual() {
        return totalMonthlyActual;
    }

    private void setTotalMonthlyActual(int totalMonthlyActual) {
        this.totalMonthlyActual = totalMonthlyActual;
    }
}
