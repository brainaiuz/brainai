package com.edatasite.workforce.gwt.project.client.ui.view.customWidgets;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeEntriesItem;
import com.edatasite.workforce.gwt.task.client.ui.view.TaskLogToTimeSheetPopup;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.USED_IN_INVOICE;

/**
 * Created by Normurod on 10/15/2016.
 */
public class LoggedTimeWidget extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private KpiDataGrid<TaskTimeEntriesItem> dataGrid;
    private ListDataProvider<TaskTimeEntriesItem> dataProvider;
    private final int WIDGET_MAX_HEIGHT = 400;

    private final Integer projectId;
    public static final ProvidesKey<TaskTimeEntriesItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectID();

    public LoggedTimeWidget(Integer projectId) {
        this.projectId = projectId;
        onInitialize();
    }

    private void initTableColumns() {
        //employee
        Column<TaskTimeEntriesItem, String> employeeName = new Column<TaskTimeEntriesItem, String>(new TextCell()) {
            @Override
            public String getValue(TaskTimeEntriesItem object) {
                return object.getEmloyee();
            }
        };
        dataGrid.addColumn(employeeName, SafeHtmlUtils.fromString(wfmStrings.assignee()), HtmlTemplates.getInstance().bold("Total"));
        dataGrid.setColumnWidth(employeeName, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        //date
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(Utils.getShortDateFormat());
        Column<TaskTimeEntriesItem, Date> dateColumn = new Column<TaskTimeEntriesItem, Date>(new DatePickerCell(dateFormat)) {
            @Override
            public Date getValue(TaskTimeEntriesItem object) {
                return object.getDate().getNonConvertedDate();
            }
        };
        dateColumn.setFieldUpdater((i, object, date) -> {
            if (!"Invoiced".equals(object.getStatus())) {
                TaskListItem tlItem = new TaskListItem();
                tlItem.setObjectID(object.getTaskId());
                tlItem.setStartDate(date);
                TaskService.App.get().saveTaskEditCellValue(tlItem, TaskListItem.START_DATE, new AbstractAsyncCallback<Boolean>() {
                });

                if (!object.isFixed()) {
                    TimesheetService.App.get().updateTimesheetDate(object.getObjectID(), new DateNonConvertable(date), new AbstractAsyncCallback<Void>() {
                    });
                }
            }
        });
        dataGrid.addColumn(dateColumn, wfmStrings.date());
        dataGrid.setColumnWidth(dateColumn, 11, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<TaskTimeEntriesItem, String> taskName = new Column<TaskTimeEntriesItem, String>(new EditTextCell()) {
            @Override
            public String getValue(TaskTimeEntriesItem object) {
                return object.getTaskName();
            }
        };
        dataGrid.addColumn(taskName, wfmStrings.taskName());
        dataGrid.setColumnWidth(taskName, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        taskName.setFieldUpdater((i, object, s) -> {
            TaskListItem rowValue = new TaskListItem();
            rowValue.setObjectID(object.getTaskId());
            rowValue.setName(s);
            TaskService.App.get().saveTaskEditCellValue(rowValue, TaskListItem.NAME, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(Boolean aBoolean) {

                }
            });
        });


        //	Time Spent
        Column<TaskTimeEntriesItem, String> time = new Column<TaskTimeEntriesItem, String>(new TextCell()) {
            @Override
            public String getValue(TaskTimeEntriesItem object) {
                return Utils.formatMinutes(object.getTimeSpent());
            }
        };
        dataGrid.addColumn(time, SafeHtmlUtils.fromString(wfmStrings.timeSpentOnly()), HtmlTemplates.getInstance().totalTime(Utils.formatMinutes(totalTime), Utils.formatMinutes(totalNotBillableTime)));
        dataGrid.setColumnWidth(time, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        //Rate(h)
        Column<TaskTimeEntriesItem, String> rate = new Column<TaskTimeEntriesItem, String>(new TextCell()) {
            @Override
            public String getValue(TaskTimeEntriesItem object) {
                if (object.isFixed()) {
                    return wfmStrings.fixed();
                }
                return object.getRate() != null ? Utils.getNumberFormat().format(object.getRate()) : "";
            }
        };
        dataGrid.addColumn(rate, "Rate(h)");
        dataGrid.setColumnWidth(rate, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        //Rate(h)
        Column<TaskTimeEntriesItem, String> discount = new Column<TaskTimeEntriesItem, String>(new TextCell()) {
            @Override
            public String getValue(TaskTimeEntriesItem object) {
                if (object.isFixed()) {
                    return wfmStrings.fixed();
                }
                return object.getDiscount() != null ? Utils.getNumberFormat().format(object.getDiscount()) : "";
            }
        };
        dataGrid.addColumn(discount, wfmStrings.discount() + "(%)");
        dataGrid.setColumnWidth(discount, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        //Amount
        Column<TaskTimeEntriesItem, String> amount = new Column<TaskTimeEntriesItem, String>(new TextCell()) {
            @Override
            public String getValue(TaskTimeEntriesItem object) {

                if (object.isFixed()) {
                    return object.getRate() != null ? Utils.getNumberFormat().format(object.getRate()) : "";
                }

                BigDecimal hours = new BigDecimal(object.getTimeSpent()).divide(new BigDecimal(60), 5, RoundingMode.HALF_UP);
                BigDecimal discount = object.getDiscount().divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);

                return object.getRate() != null ? Utils.getNumberFormat().format(!object.isBillable() ? BigDecimal.ZERO : object.getRate().multiply(hours).multiply(new BigDecimal(1).subtract(discount)).setScale(2, RoundingMode.HALF_UP)) : "";
            }
        };
        dataGrid.addColumn(amount, SafeHtmlUtils.fromString(wfmStrings.amount()), HtmlTemplates.getInstance().blueValue(Utils.getNumberFormat().format(total)));
        dataGrid.setColumnWidth(amount, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        //status
        Column<TaskTimeEntriesItem, SafeHtml> statusColorize = new Column<TaskTimeEntriesItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(TaskTimeEntriesItem object) {
                String statusColorizeF = object.getStatus();
                if ("Waiting".equals(object.getStatus())) {//not localization  --  Waiting
                    statusColorizeF = "<b style='color:#cc6e00;'>" + wfmStrings.waitingForApproval() + "</b>";
                } else if ("Approve".equals(object.getStatus())) {//not localization  --  Approve
                    statusColorizeF = "<b class=redTitle>" + (object.isBillable() ? projectStrings.notInvoiced() : projectStrings.notBillable()) + "</b>";
                } else if ("Reject".equals(object.getStatus())) {//not localization  --  Reject
                    statusColorizeF = "<b class=redTitle>" + wfmStrings.rejected() + "</b>";
                } else if ("Invoiced".equals(object.getStatus())) {
                    statusColorizeF = "<b>" + object.getInvoiceNumber() + "</b>";
                } else {
                    statusColorizeF = "<b>" + object.getStatus() + "</b>";
                }
                return SafeHtmlUtils.fromTrustedString(statusColorizeF);
            }
        };
        dataGrid.addColumn(statusColorize, SafeHtmlUtils.fromString(wfmStrings.status()), HtmlTemplates.getInstance().total(Utils.getNumberFormat().format(totalInvoiced), Utils.getNumberFormat().format(totalToBeInvoiced)));
        dataGrid.setColumnWidth(statusColorize, 15, Style.Unit.PCT);


        Column<TaskTimeEntriesItem, String> action = new Column<TaskTimeEntriesItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(TaskTimeEntriesItem object) {
                return (!"Invoiced".equals(object.getStatus()) && (Utils.hasRole(Utils.TIMESHEET_EDITOR) || Utils.getUserID().equals(object.getEmployeeId()))) ? wfmStrings.delete() : "";
            }
        };
        action.setFieldUpdater((i, object, s) -> {

            if (!"Invoiced".equals(object.getStatus()) && (Utils.hasRole(Utils.TIMESHEET_EDITOR) || Utils.getUserID().equals(object.getEmployeeId()))) {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                //message.setSize(300, 150);
                message.setTitle(wfmStrings.warning());
                message.setMessage(wfmStrings.sureYouWantToDelete()  + Property.get(Constants.TASK, projectStrings.taskDelete(), wfmStrings.task()));
                message.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        String context = PermissionConstants.PM_CONTEXT;
                        TaskService.App.get().deleteTask(object.getTaskId(), context, new AbstractAsyncCallback<String>() {
                            public void failure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.error(), Info.Type.WARNING);
                            }

                            public void success(String result) {
                                LoadingPanel.loading(false);

                                if (USED_IN_INVOICE.equals(result)) {
                                    Info.show(Property.get(Constants.TASK, wfmStrings.thisIsInvoicedTask(), wfmStrings.task()) + Property.get(Constants.TASK, wfmStrings.isInvoicedYouCannotDelete(), wfmStrings.task()), Info.Type.WARNING);
                                } else {
                                    Info.show(Property.get(Constants.TASK, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.task()), Info.Type.INFO);
                                    loadData();
                                }
                            }
                        });
                    }
                });
                message.open();
            }
        });
        dataGrid.addColumn(action, wfmStrings.action());
        dataGrid.setColumnWidth(action, 7, Style.Unit.PCT);

        Column<TaskTimeEntriesItem, String> amendTime = new Column<TaskTimeEntriesItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(TaskTimeEntriesItem object) {
                return (!"Invoiced".equals(object.getStatus()) && (Utils.hasRole(Utils.TIMESHEET_EDITOR) || Utils.getUserID().equals(object.getEmployeeId()))) ? "Amend Time&Date" : "";
            }
        };
        amendTime.setFieldUpdater((i, object, s) -> {
            if (!"Invoiced".equals(object.getStatus()) && (Utils.hasRole(Utils.TIMESHEET_EDITOR) || Utils.getUserID().equals(object.getEmployeeId()))) {
                TaskLogToTimeSheetPopup tlt = new TaskLogToTimeSheetPopup(object.getTaskId(), object.getDate().getNonConvertedDate(), object.getEmployeeId(), true);

                tlt.setLogTimeCmd(() -> loadData());
            }
        });
        dataGrid.addColumn(amendTime, "");
        dataGrid.setColumnWidth(amendTime, 7, Style.Unit.PCT);

    }

    public void addDataDisplay(HasData<TaskTimeEntriesItem> display) {
        dataProvider.addDataDisplay(display);
    }

    protected Widget onInitialize() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.addStyleName("logged-time-edit-cell");
        dataGrid.setPageSize(1000);
        dataGrid.setWidth("100%");
        dataGrid.setHeight(WIDGET_MAX_HEIGHT + "px");
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noTimeEntiesYet(), "", null));
        dataGrid.getEmptyTableWidget().getElement().getStyle().setMarginTop(100, com.google.gwt.dom.client.Style.Unit.PX);
        dataProvider.addDataDisplay(dataGrid);

        initWidget(dataGrid);
        initTableColumns();
        loadData();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOAD_LOGGED_TIMES, LoggedTimeWidget.this, (sender, args) -> loadData());
        return null;
    }

    Integer totalTime = 0, totalNotBillableTime = 0;
    BigDecimal total = BigDecimal.ZERO, totalInvoiced = BigDecimal.ZERO, totalToBeInvoiced = BigDecimal.ZERO;

    private void loadData() {
        ProjectService.App.get().getProjectTimesheets(projectId, new AsyncCallback<TaskTimeEntriesItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(TaskTimeEntriesItem[] taskTimeEntriesItems) {
                initTotals(taskTimeEntriesItems);
                dataGrid.clearTable();
                initTableColumns();
                supplyProvider(taskTimeEntriesItems);
            }
        });
    }

    private void initTotals(TaskTimeEntriesItem[] items) {
        totalTime = 0;
        totalNotBillableTime = 0;
        total = BigDecimal.ZERO;
        totalInvoiced = BigDecimal.ZERO;
        totalToBeInvoiced = BigDecimal.ZERO;

        if (items != null) {
            for (TaskTimeEntriesItem object : items) {

                totalTime += object.isBillable() ? object.getTimeSpent() : 0;
                totalNotBillableTime += !object.isBillable() ? object.getTimeSpent() : 0;

                BigDecimal hours = new BigDecimal(object.getTimeSpent()).divide(new BigDecimal(60), 5, RoundingMode.HALF_UP);
                BigDecimal discount = object.getDiscount().divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);

                if (object.getRate() == null) {
                    object.setRate(new BigDecimal("0"));
                }
                BigDecimal amount = object.isFixed() ? object.getRate() : object.getRate().multiply(hours).multiply(new BigDecimal(1).subtract(discount)).setScale(2, RoundingMode.HALF_UP);

                total = total.add(!object.isBillable() && ("Approve".equals(object.getStatus()) || "Invoiced".equals(object.getStatus())) ? BigDecimal.ZERO : amount);
                totalInvoiced = totalInvoiced.add("Invoiced".equals(object.getStatus()) ? amount : BigDecimal.ZERO);
                totalToBeInvoiced = totalToBeInvoiced.add("Approve".equals(object.getStatus()) ? amount : BigDecimal.ZERO);
            }
        }
    }

    private void supplyProvider(TaskTimeEntriesItem[] taskTimeEntriesItems) {
        List<TaskTimeEntriesItem> tableses = dataProvider.getList();
        tableses.clear();
        Collections.addAll(tableses, taskTimeEntriesItems);

        if (taskTimeEntriesItems != null && taskTimeEntriesItems.length > 0) {
            int height = taskTimeEntriesItems.length * 60 + 50;

            if (height > WIDGET_MAX_HEIGHT) {
                height = WIDGET_MAX_HEIGHT;
            }
            dataGrid.setHeight(height + "px");

            if (height == WIDGET_MAX_HEIGHT) {
                dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
            }
        }
        dataProvider.refresh();
    }

    static class HtmlTemplates {

        interface HtmlTemplatesInterface extends SafeHtmlTemplates {
            @Template("<div style=\"width: 100%; color:red;\"><b>{0}</b></div>")
            SafeHtml redValue(String value);

            @Template("<div style=\"width: 100%; color:blue;\"><b>{0}</b></div>")
            SafeHtml blueValue(String value);

            @Template("<div style=\"width: 100%; color:green;\"><b>{0}</b></div>")
            SafeHtml greenValue(String value);

            @Template("<div style=\"width: 100%;\"><b style=\"color:green;\">Billable: {0}</b><br/>" +
                    "<b style=\"color:red\">Not Billable: {1}</b></div>")
            SafeHtml totalTime(String billableTimes, String notBillableTimes);


            @Template("<div style=\"width: 100%;\"><b style=\"color:green;\">Invoiced: {0}</b><br/>" +
                    "<b style=\"color:red\">To be Invoiced: {1}</b></div>")
            SafeHtml total(String invoiced, String toBeInvoiced);

            @Template("<div style=\"width: 100%;\"><b>{0}</b></div>")
            SafeHtml bold(String value);

        }

        private static HtmlTemplatesInterface instance;

        public static HtmlTemplatesInterface getInstance() {
            if (instance == null) {
                instance = GWT.create(HtmlTemplatesInterface.class);
            }

            return instance;
        }
    }
}
