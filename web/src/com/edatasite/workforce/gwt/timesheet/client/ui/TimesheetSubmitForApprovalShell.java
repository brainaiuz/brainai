package com.edatasite.workforce.gwt.timesheet.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReportResult;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SortableTable.SortableTable;
import com.edatasite.workforce.gwt.core.client.ui.SortableTable.TableHeader;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskTimeSheetEntry;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetEntriesPerPeriod;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetEntry;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetServiceAsync;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Dec 17, 2010
 * Time: 12:48:37 PM
 */

public class TimesheetSubmitForApprovalShell extends View implements Colapse, Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final TimesheetServiceAsync timesheetService = TimesheetService.App.get();

    private String groupByName = "Project";
    private WfmButton2 viewResult;
    private WfmButton2 sendForApproval;
    private WfmButton2 proceed;

    private int n;
    private MaterialPanel tablePanel;
    private MaterialPanel footerPanel;

    private ListingFilterParameter filterParameter;
    private Date startDate;
    private Date endDate;
    private DataListBox projects;
    private KpiCheckBox complitedProjects;
    private DataListBox employees;
    private DataListBox approvers;
    private DataListBox groupBy;
    private DataListBox cusDateRange;
    private DatePicker fromDate;
    private DatePicker toDate;
    private SelectItem[] columnNames;
    private ArrayList<Integer> projectIDs;
    private ArrayList<Integer> employeeIDs;
    private ArrayList<Integer> allEmployeeIDs;
    private Integer employeeID;
    private Map<Integer, TaskTimeSheetEntry> taskTimeSheetEntries;
    private final Integer formType;
    private String formHeader;
    private Integer resultSize = 0;
    private TimesheetSettings timesheetSettings;
    private final SelectItem allOption = new SelectItem(0, wfmStrings.all());
    private final SelectItem employeeOption = new SelectItem(2, wfmStrings.employee());
    private int columnOffset = 0;

    public TimesheetSubmitForApprovalShell(String employeeID) {
        super("addtimesheet", wfmStrings.submitForApproval());
        this.formType = Constants.TIMESHEET_SUBMIT_FOR_APPROVAL_FORM;

        if (employeeID != null && !employeeID.isEmpty()) {
            this.employeeID = Integer.valueOf(employeeID);
        }
    }

    public TimesheetSubmitForApprovalShell(Integer id) {
        super("edit");
        setDescription(property.getPlural(projectStrings.approveAllTimesheets()));
        this.formType = Constants.TIMESHEET_APPROVAL_FORM;
    }

    public String getIconStyle() {
        return null;
    }

    public ImageResource getIconImage() {
        return null;
    }

    public Widget onInitialize() {
        timesheetService.getTimesheetSettings(new AsyncCallback<TimesheetSettings>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(TimesheetSettings timesheetSettings_) {
                timesheetSettings = timesheetSettings_;
            }
        });
        initFormAndSetData();
        return null;
    }

    private void initFormAndSetData() {
        if (employeeID == null) {
            employeeID = Utils.getUserID();
        }
        if ("true".equals(Utils.userSettings.get(SHOW_HOUR_TYPE_DROPDOWN))) {
            columnOffset = 1;
        }
        columnNames = new SelectItem[13 + columnOffset];
        projectIDs = new ArrayList<>();
        employeeIDs = new ArrayList<>();
        allEmployeeIDs = new ArrayList<>();
        taskTimeSheetEntries = new HashMap<>();
        filterParameter = new ListingFilterParameter();

        fromDate = new DatePicker();
        fromDate.addStyleName("form-control");
        fromDate.ensureDebugId("Approve_timesheets_fromDate");
        fromDate.addChangeHandler(event -> {
            if (fromDate.getDate() != null) {
                startDate = fromDate.getDate();
            } else {
                startDate = null;
                fromDate.removeStyleName(fromDate.getStyleName());
                fromDate.addStyleName("form-control disabledDataPicker");
            }
        });

        toDate = new DatePicker();
        toDate.addStyleName("form-control");
        toDate.ensureDebugId("Approve_timesheets_toDate");
        toDate.addChangeHandler(event -> {
            if (toDate.getDate() != null) {
                endDate = toDate.getDate();
            } else {
                endDate = null;
                toDate.removeStyleName(toDate.getStyleName());
                toDate.addStyleName("form-control disabledDataPicker");
            }
        });

        cusDateRange = new DataListBox();
        cusDateRange.ensureDebugId("Approve_timesheets_datePeriod");
        cusDateRange.setNullLabel(wfmStrings.customisedDateRange());
        cusDateRange.addListItem(new SelectItem(1, wfmStrings.previousWeek()));
        cusDateRange.addListItem(new SelectItem(2, wfmStrings.previousMonth()));
        cusDateRange.addListItem(new SelectItem(4, wfmStrings.thisMonth()));
        cusDateRange.addListItem(new SelectItem(3, wfmStrings.thisWeek()));
        cusDateRange.setAllowFirstItem(true);
        cusDateRange.addValueChangeHandler(changeEvent -> {
            if (cusDateRange.getSelectedItem() != null && cusDateRange.getSelectedItem().getId() > 0) {
                fromDate.setEnabled(false);
                toDate.setEnabled(false);
                if (cusDateRange.getSelectedItem().getId() == 1) {
                    Integer days = 0;
                    if (new Date().getDay() != 0) {
                        days = -6;
                    } else {
                        days = -13;
                    }
                    startDate = DateUtil.addDays(DateUtil.getWeekFirstDay(new Date()), days);
                    Date end = (Date) startDate.clone();
                    endDate = DateUtil.addDays(end, 6);
                } else if (cusDateRange.getSelectedItem().getId() == 2) {
                    startDate = DateUtil.addMonths(DateUtil.getMonthFirstDay(new Date()), -1);
                    Date end = (Date) startDate.clone();
                    endDate = DateUtil.getMonthLastDate(end);
                } else if (cusDateRange.getSelectedItem().getId() == 3) {
                    if (new Date().getDay() != 0) {
                        startDate = DateUtil.addDays(DateUtil.getWeekFirstDay(new Date()), 1);
                        endDate = DateUtil.addDays(DateUtil.getWeekLastDay(new Date()), 1);
                    } else {
                        startDate = DateUtil.addDays(DateUtil.getWeekFirstDay(new Date()), -6);
                        Date end = (Date) startDate.clone();
                        endDate = DateUtil.addDays(end, 6);
                    }
                } else if (cusDateRange.getSelectedItem().getId() == 4) {
                    startDate = DateUtil.getMonthFirstDay(new Date());
                    endDate = DateUtil.getMonthLastDate(new Date());
                }
                fromDate.setDate(startDate);
                toDate.setDate(endDate);
            } else {
                fromDate.setEnabled(true);
                toDate.setEnabled(true);
                if (fromDate.getDate() != null) {
                    startDate = fromDate.getDate();
                } else {
                    startDate = null;
                }
                if (toDate.getDate() != null) {
                    endDate = toDate.getDate();
                } else {
                    endDate = null;
                }
            }
            if (startDate != null) {
                if (fromDate.getStyleName() != null && !"".equals(fromDate.getStyleName())) {
                    fromDate.removeStyleName(fromDate.getStyleName());
                    fromDate.addStyleName("form-control disabledDataPicker");
                }
            }
            if (endDate != null) {
                if (toDate.getStyleName() != null && !"".equals(toDate.getStyleName())) {
                    toDate.removeStyleName(toDate.getStyleName());
                    toDate.addStyleName("form-control disabledDataPicker");
                }
            }
        });


        projects = new DataListBox();
        projects.ensureDebugId("Approve_timesheets_project");
        projects.setNullLabel(wfmStrings.all());
        projects.setAllowFirstItem(true);
        projects.addValueChangeHandler(arg0 -> {
            LoadingPanel.loading(true);
//                clientList();
            employeeList();
            if (projects.getSelectedItem().getId() != null) {
                projectIDs.clear();
                projectIDs.add(projects.getSelectedItem().getId());
            }
            LoadingPanel.loading(false);
        });

        complitedProjects = new KpiCheckBox(projectStrings.includeCompletedProjects());
        complitedProjects.ensureDebugId("complitedProjects");
        complitedProjects.addValueChangeHandler(booleanValueChangeEvent -> getprojectsAndEmployees(complitedProjects.getValue()));

        MaterialPanel complitedCheckBoxPanel = new MaterialPanel("group-box__item-outside-component");
        complitedCheckBoxPanel.add(complitedProjects);

        MaterialPanel projectPanel = new MaterialPanel();
        projectPanel.add(projects);
        projectPanel.add(complitedCheckBoxPanel);

        groupBy = new DataListBox();
        groupBy.ensureDebugId("Approve_timesheets_groupBy");
        groupBy.setNullLabel(Property.get(Constants.PROJECT, wfmStrings.project()));
        groupBy.setWithoutNullLabel(true);
        groupBy.addListItem(new SelectItem(1, wfmStrings.customer()));
        if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
            groupBy.addListItem(employeeOption);
        }
        groupBy.addListItem(new SelectItem(3, wfmStrings.date()));
        groupBy.addValueChangeHandler(changeEvent -> {
            if (groupBy.getSelectedItem() == null) {
                groupByName = "Project";
            } else {
                if (Integer.valueOf(1).equals(groupBy.getSelectedItem().getId())) {
                    groupByName = "Client";
                } else if (Integer.valueOf(2).equals(groupBy.getSelectedItem().getId())) {
                    groupByName = "Employee";
                } else if (Integer.valueOf(3).equals(groupBy.getSelectedItem().getId())) {
                    groupByName = "Date";
                }
            }
        });

        employees = new DataListBox();
        employees.ensureDebugId("Approve_timesheets_employees");
        employees.setNullLabel(wfmStrings.all());
        employees.setAllowFirstItem(true);
        employees.addValueChangeHandler(arg0 -> {
            if (employees.getSelectedItem().getId() > 0) {
                if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                    groupBy.removeListItem(employeeOption);
                }
                employeeIDs.clear();
                employeeIDs.add(employees.getSelectedItem().getId());
            } else {
                if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                    groupBy.addListItem(employeeOption);
                }
                employeeIDs.clear();
                employeeIDs.addAll(allEmployeeIDs);
            }
        });

        //approver
        approvers = new DataListBox();
        approvers.ensureDebugId("approve_timeSheet_approvers");
        approvers.setAllowFirstItem(true);

        viewResult = new WfmButton2(wfmStrings.viewResult(), WfmButton2.BTN_PRIMARY);
        viewResult.ensureDebugId("Approve_timesheets_viewResultbutton");

        sendForApproval = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        sendForApproval.ensureDebugId("submitForApproval");
        proceed = new WfmButton2(wfmStrings.proceed());
        proceed.ensureDebugId("proceed");

        GBoxItem groupItemPanel = null;
        GBoxItem employeeItemPanel = null;
        GBoxItem approverItemPanel = null;

        if (formType.equals(TIMESHEET_SUBMIT_FOR_APPROVAL_FORM)) {

            groupItemPanel = new GBoxItem(wfmStrings.groupBy(), groupBy);

            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TIMESHEET_APPROVERS_DROPDOWN)) {
                approverItemPanel = new GBoxItem(wfmStrings.approver(), approvers);
                approverItemPanel.addStyleName("group-box__item--split-right");
            }
            formHeader = projectStrings.submitTimesheetForApproval();
        } else {
            groupItemPanel = new GBoxItem(wfmStrings.groupBy(), groupBy);
            employeeItemPanel = new GBoxItem(wfmStrings.employee(), employees);
            employeeItemPanel.addStyleName("group-box__item--split-right");

            formHeader = property.getSingular(wfmStrings.timesheetApproval());
        }
        proceed.addClickHandler(clickEvent -> approveRejectTimesheet());
        getprojectsAndEmployees(false);

        viewResult.addClickHandler(sender -> {
            tablePanel.clear();
            applyFilter();
            if (validate()) {
                return;
            }
            n = 0;
            groupByName = filterParameter.getGroupByName();
//                }
            if (groupBy.getSelectedItem() == null) {
                columnNames[n] = new SelectItem();
                columnNames[n].setId(n);
                columnNames[n].setName(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
                n++;

                columnNames[n] = new SelectItem();
                columnNames[n].setId(n);
                columnNames[n].setName(wfmStrings.employee());
                n++;

                columnNames[n] = new SelectItem();
                columnNames[n].setId(n);
                columnNames[n].setName(wfmStrings.date());
                n++;
            } else {
                if (Integer.valueOf(1).equals(groupBy.getSelectedItem().getId())) {
                    columnNames[n] = new SelectItem();
                    columnNames[n].setId(n);
                    columnNames[n].setName(Property.get(Constants.PROJECT, wfmStrings.project()));
                    n++;

                    columnNames[n] = new SelectItem();
                    columnNames[n].setId(n);
                    columnNames[n].setName(wfmStrings.employee());
                    n++;

                    columnNames[n] = new SelectItem();
                    columnNames[n].setId(n);
                    columnNames[n].setName(wfmStrings.date());
                    n++;
                }

                if (Integer.valueOf(2).equals(groupBy.getSelectedItem().getId())) {
                    columnNames[n] = new SelectItem();
                    columnNames[n].setId(n);
                    columnNames[n].setName(Property.get(Constants.PROJECT, wfmStrings.project()));
                    n++;

                    columnNames[n] = new SelectItem();
                    columnNames[n].setId(n);
                    columnNames[n].setName(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
                    n++;

                    columnNames[n] = new SelectItem();
                    columnNames[n].setId(n);
                    columnNames[n].setName(wfmStrings.date());
                    n++;
                }

                if (Integer.valueOf(3).equals(groupBy.getSelectedItem().getId())) {
                    columnNames[n] = new SelectItem();
                    columnNames[n].setId(n);
                    columnNames[n].setName(wfmStrings.employee());
                    n++;

                    columnNames[n] = new SelectItem();
                    columnNames[n].setId(n);
                    columnNames[n].setName(Property.get(Constants.PROJECT, wfmStrings.project()));
                    n++;

                    columnNames[n] = new SelectItem();
                    columnNames[n].setId(n);
                    columnNames[n].setName(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
                    n++;
                }
            }

            columnNames[n] = new SelectItem();
            columnNames[n].setId(n);
            columnNames[n].setName(wfmStrings.task());
            n++;

            columnNames[n] = new SelectItem();
            columnNames[n].setId(n);
            columnNames[n].setName(wfmStrings.comments());
            n++;

            columnNames[n] = new SelectItem();
            columnNames[n].setId(n);
            columnNames[n].setName(wfmStrings.estimatedTime());
            n++;

            columnNames[n] = new SelectItem();
            columnNames[n].setId(n);
            columnNames[n].setName(wfmStrings.approved());
            n++;

            columnNames[n] = new SelectItem();
            columnNames[n].setId(n);
            columnNames[n].setName(wfmStrings.hoursSpent());
            n++;

            if ("true".equals(Utils.userSettings.get(SHOW_HOUR_TYPE_DROPDOWN))) {
                columnNames[n] = new SelectItem();
                columnNames[n].setId(n);
                columnNames[n].setName(projectStrings.hourType());
                n++;
            }

            columnNames[n] = new SelectItem();
            columnNames[n].setId(n);
            columnNames[n].setName("");
            n++;

            if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                columnNames[n] = new SelectItem();
                columnNames[n].setId(n);
                columnNames[n].setName("");
                n++;
            }

            tablePanel.setVisible(true);
            contenPanels();
        });

        sendForApproval.addClickHandler(clickEvent -> {
            Integer approverID = null;
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_TIMESHEET_APPROVERS_DROPDOWN)) {
                approverID = approvers.getSelectedItem() != null ? approvers.getSelectedItem().getId() : null;
                if (!Validation.validateListBoxRequired(approvers, new HTML(), "")) {
                    return;
                }
            }
            if (taskTimeSheetEntries != null && taskTimeSheetEntries.size() > 0) {
                sendForApproval.setEnabled(false);
                LoadingPanel.loading(true);
                final TimeSheetEntriesPerPeriod entriesPerPeriod = new TimeSheetEntriesPerPeriod();
                DateUtil.resetTime(startDate);
                DateUtil.getDayLastTime(endDate);
                entriesPerPeriod.setFromDate(new DateNonConvertable(startDate));
                entriesPerPeriod.setToDate(new DateNonConvertable(endDate));
                entriesPerPeriod.setEntries(taskTimeSheetEntries.values().toArray(new TaskTimeSheetEntry[taskTimeSheetEntries.size()]));
                entriesPerPeriod.setApproverID(approverID);
                entriesPerPeriod.setEmployeeID(employeeID);
                timesheetService.submitTimesheetForApproval(entriesPerPeriod, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        sendForApproval.setEnabled(true);
                        LoadingPanel.loading(false);
                        Info.show(projectStrings.errorOccuredSendingTimesheetApproval(), Info.Type.WARNING);

                    }

                    @Override
                    public void success(Void aVoid) {
                        sendForApproval.setEnabled(true);
                        LoadingPanel.loading(false);
                        Info.show(projectStrings.timesheetSubmittedForApproval(), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESHEET_SUBMIT_FOR_APPROVAL, aVoid, TimesheetSubmitForApprovalShell.this);
                        closeTab();
                    }
                });
            } else {
                Info.show(projectStrings.plSelectTaskToSubmitForApproval(), Info.Type.WARNING);
            }
        });

        MaterialPanel mainPanel = new MaterialPanel("submitApproval file--TimesheetSubmitForApprovalShell");
        MaterialPanel sectionBoxPanel = new MaterialPanel("submitApproval__sorting");
        MaterialPanel sectionBoxContentPanel = new MaterialPanel("section-box__content");

        GBox groupBoxPanel = new GBox();
        groupBoxPanel.addStyleName("group-box--united");

        GBoxRow groupBoxRow = new GBoxRow();

        GBoxItem projectItemPanel = new GBoxItem(Property.get(Constants.PROJECT, wfmStrings.project()), projectPanel);
        projectItemPanel.addStyleName("group-box__item--double-bottom-gap");
        groupBoxRow.add(projectItemPanel);
        if (groupItemPanel != null) {
            groupBoxRow.add(groupItemPanel);
        }
        if (employeeItemPanel != null) {
            groupBoxRow.add(employeeItemPanel);
        }
        if (approverItemPanel != null) {
            groupBoxRow.add(approverItemPanel);
        }

        groupBoxRow.add(new GBoxItem(wfmStrings.datePeriod(), cusDateRange));
        groupBoxRow.add(getPeriodDatePanel(fromDate, toDate));
        GBoxItem gBoxItem = new GBoxItem(viewResult);
        gBoxItem.setStyleNoBorder(true);
        groupBoxRow.add(gBoxItem);

        groupBoxPanel.add(getGroupBoxTitlePanel(formHeader));
        groupBoxPanel.add(groupBoxRow);

        sectionBoxContentPanel.add(groupBoxPanel);
        sectionBoxPanel.add(sectionBoxContentPanel);

        tablePanel = new MaterialPanel("submitApproval__results");

        footerPanel = new MaterialPanel("submitApproval__footer");

        mainPanel.add(sectionBoxPanel);
        mainPanel.add(tablePanel);
        mainPanel.add(footerPanel);
        add(mainPanel);
    }

    private void getprojectsAndEmployees(final Boolean isOldProjects) {
        LoadingPanel.loading(true);
        timesheetService.getProjectsAndClients(employeeID, formType, isOldProjects, new AsyncCallback<TimesheetData>() {

            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(TimesheetData timesheetData) {
                LoadingPanel.loading(false);
                if (timesheetData != null) {
                    if (timesheetData.getProjects() != null && timesheetData.getProjects().length > 0) {
                        if (projects.getItems() != null) {
                            projects.clear();
                        }
                        projects.setItems(timesheetData.getProjects());
                        for (SelectItem item : timesheetData.getProjects()) {
                            projectIDs.add(item.getId());
                        }
                    }
                    if (formType.equals(TIMESHEET_APPROVAL_FORM)) {
                        if (timesheetData.getEmployees() != null && timesheetData.getEmployees().length > 0) {
                            employees.setItems(timesheetData.getEmployees());
                            allEmployeeIDs.clear();
                            for (SelectItem item : timesheetData.getEmployees()) {
                                employeeIDs.add(item.getId());
                                allEmployeeIDs.add(item.getId());
                            }
                        }
                    }
                    if (!isOldProjects) {
                        //approver
                        if (timesheetData.getApprovers() != null && timesheetData.getApprovers().length > 0) {
                            approvers.setItems(timesheetData.getApprovers());
                        }
                    }
                }
            }
        });
    }

    private void projectList() {
        timesheetService.getProjectsList(null, employees.getSelectedItem() != null ? employees.getSelectedItem().getId() : null, new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] items) {
                projects.removeListItems();
                projects.setItems(items);
                projects.setSelected(0);
                projectIDs.clear();
                for (SelectItem item : items) {
                    projectIDs.add(item.getId());
                }
//            enableButtons();
            }

            public void failure() {
//            enableButtons();
            }
        });
    }

    private void employeeList() {
        timesheetService.getEmployeesList(null, projects.getSelectedItem() != null ? projects.getSelectedItem().getId() : null, formType, new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] items) {
                employees.removeListItems();
                employees.addListItem(allOption);
                employees.setItems(items);
                employees.setSelected(0);
                employeeIDs.clear();
                allEmployeeIDs.clear();
                for (SelectItem item : items) {
                    employeeIDs.add(item.getId());
                    allEmployeeIDs.add(item.getId());
                }
//                enableButtons();
            }

            public void failure() {
//                enableButtons();
            }
        });
    }

    private void approveRejectTimesheet() {
        if (taskTimeSheetEntries != null && taskTimeSheetEntries.size() > 0) {
            proceed.setEnabled(false);
            TaskTimeSheetEntry[] timesheetEntries = taskTimeSheetEntries.values().toArray(new TaskTimeSheetEntry[]{});
            DateUtil.resetTime(startDate);
            DateUtil.getDayLastTime(endDate);
            timesheetService.approveRejectTimesheetHours(timesheetEntries, employeeIDs, new AbstractAsyncCallback<Boolean>() {
                public void failure(Throwable caught) {
                    proceed.setEnabled(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(Boolean result) {
                    proceed.setEnabled(true);
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.approvalProcess()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESHEET_APPROVAL, result, TimesheetSubmitForApprovalShell.this);
                    closeTab();
                }
            });
        } else {
            Info.show(projectStrings.plSelectTaskToApprove(), Info.Type.WARNING);
        }
    }

    public ListingFilterParameter applyFilter() {
        filterParameter = new ListingFilterParameter();

        if (projects.getSelectedItem() != null) {
            filterParameter.setProjectId(projects.getSelectedItem().getId());
        }
        filterParameter.setGroupByName(groupByName);
        if (fromDate != null) {
            if (startDate != null) {
                filterParameter.setStartDate(startDate);
            }
        }
        if (endDate != null) {
            filterParameter.setEndDate(endDate);
        }
        return filterParameter;
    }


    private HTML getTitle(String title) {
        return new HTML("<b class=biggerTitle>" + title + "</b>");
    }

    private SelectItem[] items;

    private void contenPanels() {
        viewResult.setEnabled(false);
        LoadingPanel.loading(true);
        if (validate()) {
            LoadingPanel.loading(false);
            return;
        }
        if (projects.getSelectedItem() != null) {
            if (projects.getSelectedItem().getId() > 0) {
                projectIDs.clear();
                projectIDs.add(projects.getSelectedItem().getId());
            }
        } else {
            projectIDs.clear();
            projectIDs.add(0);
        }
        if (employees.getSelectedItem() != null) {
            if (employees.getSelectedItem().getId() > 0) {
                employeeIDs.clear();
                employeeIDs.add(employees.getSelectedItem().getId());
            }
        }
        if (TIMESHEET_APPROVAL_FORM.equals(formType) && Utils.hasRole(PM)) {
            /*if (!"Employee".equals(groupByName)) {
                pmRoleId = PM;
            } else {
                pmRoleId = MEM;
            }*/
        }
        if (TIMESHEET_SUBMIT_FOR_APPROVAL_FORM.equals(formType)) {
            employeeIDs.clear();
            employeeIDs.add(employeeID);
        }

        DateUtil.resetTime(startDate);
        DateUtil.getDayLastTime(endDate);
        timesheetService.getTimesheetReport(/*clientIDs*/null, projectIDs, employeeIDs, MEM, null, groupByName, new DateNonConvertable(startDate), new DateNonConvertable(endDate), true, true,
                false, true, true, true, true, true, false, false, false, false, formType, new AbstractAsyncCallback<ReportResult[]>() {
                    public void failure(Throwable caught) {
                        viewResult.setEnabled(true);
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(final ReportResult[] results) {
                        taskTimeSheetEntries.clear();
                        resultSize = results.length;
                        LoadingPanel.loading(false);
                        viewResult.setEnabled(true);
                        items = null;
                        LinkedHashMap<Object, String> mapGroup = new LinkedHashMap<>();
                        for (ReportResult result : results) {
                            if (!mapGroup.containsKey(result.getGroupId())) {
                                if ("Client".equals(groupByName)) {
                                    mapGroup.put(result.getGroupId(), result.getClientName());
                                    continue;
                                }
                                if ("Project".equals(groupByName)) {
                                    mapGroup.put(result.getGroupId(), result.getProjectName());
                                    continue;
                                }
                                if ("Employee".equals(groupByName)) {
                                    mapGroup.put(result.getGroupId(), result.getEmployeeName());
                                    continue;
                                }
                                if ("Date".equals(groupByName)) {
                                    mapGroup.put(result.getGroupId(), DateUtils.format(result.getCreatDate().getNonConvertedDate()));
                                }
                            }
                        }
                        items = new SelectItem[mapGroup.size()];
                        int j = 0;
                        if ("Date".equals(groupByName)) {
                            for (Object key : mapGroup.keySet()) {
                                items[j++] = new SelectItem(Integer.parseInt(key.toString()), mapGroup.get(key));
                            }
                        } else {
                            for (Object key : sortByValue(mapGroup)) {
                                items[j++] = new SelectItem(Integer.parseInt(key.toString()), mapGroup.get(key));
                            }
                        }
                        int totals = 0;
                        int sortInx = 3;
                        if (items != null) {
                            int approveRejectedItemsCount = 0;
                            for (SelectItem item : items) {
                                final SortableTable flexTable = new SortableTable();
                                flexTable.addStyleName("rprt-rslt");
                                TableHeader header = new TableHeader("Hours spent for all ( Total: 0", n);
                                flexTable.setWidget(0, 0, header);
                                flexTable.getRowFormatter().setStyleName(0, "rprt-rslt__total");
                                flexTable.getFlexCellFormatter().setColSpan(0, 0, n);

                                final KpiCheckBox allApproveRB = new KpiCheckBox(wfmStrings.selectAll());
                                allApproveRB.addStyleName("gwt-debug_timesheet_selectAll");
                                final KpiCheckBox allRejectRB = new KpiCheckBox(wfmStrings.rejectAll());
                                allRejectRB.addStyleName("gwt-debug_timesheet_rejectAll");
                                allApproveRB.setName(item.getId().toString());
                                allRejectRB.setName(item.getId().toString());

                                allApproveRB.addClickHandler(clickEvent -> {
                                    batchSelect(flexTable, allApproveRB, null);
                                    if (allApproveRB.getValue() && timesheetSettings.isTimesheetApprovalCommentRequired()) {
                                        showApproveCommentPopup(flexTable, null);
                                    }
                                    allRejectRB.setValue(false);
                                });
                                allRejectRB.addClickHandler(clickEvent -> {
                                    batchSelect(flexTable, allRejectRB, null);
                                    if (allRejectRB.getValue() && timesheetSettings.isTimesheetApprovalCommentRequired()) {
                                        showRejectCommentPopup(flexTable, null);
                                    }
                                    allApproveRB.setValue(false);
                                });
                                if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                                    allApproveRB.setText(wfmStrings.approveAll());
                                }

                                for (int i = 0; i < n; i++) {
                                    if (i == 8 + columnOffset) {
                                        flexTable.addColumnHeader(columnNames[i].getName(), i, allApproveRB);
                                    } else if ((i == 9 + columnOffset) && TIMESHEET_APPROVAL_FORM.equals(formType)) {
                                        flexTable.addColumnHeader(columnNames[i].getName(), i, allRejectRB);
                                    } else {
                                        flexTable.addColumnHeader(columnNames[i].getName(), i, null);
                                    }
                                    flexTable.getRowFormatter().addStyleName(1, "rprt-rslt__header");
//                                    flexTable.getFlexCellFormatter().setStyleName(1, i, "my-tbl-col tbl-hdr");
                                }

                                int total = 0;
                                int rowIdx = 2;
                                for (final ReportResult result : results) {
                                    if ((result.getGroupId()).equals(item.getId())) {
                                        int colIndex = 0;

                                        if (groupBy.getSelectedItem() == null) {
                                            flexTable.setValue(rowIdx, colIndex++, result.getClientName() != null ? result.getClientName() : " ");
                                            flexTable.setValue(rowIdx, colIndex++, result.getEmployeeName() != null ? result.getEmployeeName() : " ");
                                            flexTable.setValue(rowIdx, colIndex++, result.getCreatDate() != null ? DateUtils.format(result.getCreatDate().getNonConvertedDate()) : " ");
                                        } else {
                                            if (Integer.valueOf(1).equals(groupBy.getSelectedItem().getId())) {
                                                flexTable.setValue(rowIdx, colIndex++, result.getProjectName() != null ? result.getProjectName() : " ");
                                                flexTable.setValue(rowIdx, colIndex++, result.getEmployeeName() != null ? result.getEmployeeName() : " ");
                                                flexTable.setValue(rowIdx, colIndex++, result.getCreatDate() != null ? DateUtils.format(result.getCreatDate().getNonConvertedDate()) : " ");
                                            } else if (Integer.valueOf(2).equals(groupBy.getSelectedItem().getId())) {
                                                flexTable.setValue(rowIdx, colIndex++, result.getProjectName() != null ? result.getProjectName() : " ");
                                                flexTable.setValue(rowIdx, colIndex++, result.getClientName() != null ? result.getClientName() : " ");
                                                flexTable.setValue(rowIdx, colIndex++, result.getCreatDate() != null ? DateUtils.format(result.getCreatDate().getNonConvertedDate()) : " ");
                                            } else if (Integer.valueOf(3).equals(groupBy.getSelectedItem().getId())) {
                                                flexTable.setValue(rowIdx, colIndex++, result.getEmployeeName() != null ? result.getEmployeeName() : " ");
                                                flexTable.setValue(rowIdx, colIndex++, result.getProjectName() != null ? result.getProjectName() : " ");
                                                flexTable.setValue(rowIdx, colIndex++, result.getClientName() != null ? result.getClientName() : " ");
                                            }
                                        }
                                        final Integer rowIndex = rowIdx;
                                        final KpiCheckBox approveRB = new KpiCheckBox("");
                                        approveRB.addStyleName("gwt-debug_timesheet_approval");
                                        final KpiCheckBox rejectRB = new KpiCheckBox(wfmStrings.reject());
                                        rejectRB.addStyleName("gwt-debug_timesheet_reject");

                                        approveRB.addClickHandler(clickEvent -> {
                                            if (approveRB.getValue() && timesheetSettings.isTimesheetApprovalCommentRequired()) {
                                                showApproveCommentPopup(flexTable, rowIndex);
                                            }
                                            runRadioButtonsAction(flexTable, rowIndex, approveRB);
                                            rejectRB.setValue(false);
                                        });
                                        rejectRB.addClickHandler(clickEvent -> {
                                            if (rejectRB.getValue() && timesheetSettings.isTimesheetApprovalCommentRequired()) {
                                                showRejectCommentPopup(flexTable, rowIndex);
                                            }
                                            runRadioButtonsAction(flexTable, rowIndex, rejectRB);
                                            approveRB.setValue(false);
                                        });

                                        if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                                            if (result.getApproveReject() == EDIT) {
                                                approveRejectedItemsCount++;
                                                approveRB.setText(wfmStrings.approve());
                                            }
                                        }
                                        flexTable.setValue(rowIdx, colIndex++, result.getTaskName());
                                        flexTable.setValue(rowIdx, colIndex++, result.getComment());
                                        flexTable.setValue(rowIdx, colIndex++, Utils.formatMinutes(result.getEstimatedTime()));
                                        flexTable.setValue(rowIdx, colIndex++, Utils.formatMinutes(Integer.valueOf(result.getApprovedHours())));
                                        flexTable.setValue(rowIdx, colIndex++, Utils.formatMinutes(Integer.valueOf(result.getSum())));
                                        if ("true".equals(Utils.userSettings.get(SHOW_HOUR_TYPE_DROPDOWN))) {
                                            flexTable.setValue(rowIndex, colIndex++, result.getHourType());
                                        }
                                        flexTable.setWidget(rowIdx, colIndex++, approveRB);
                                        if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                                            flexTable.setWidget(rowIdx, colIndex++, rejectRB);
                                        }
                                        flexTable.setValue(rowIdx, colIndex++, result.getTaskID().toString());
                                        flexTable.setValue(rowIdx, colIndex++, result.isBillable());
                                        flexTable.setValue(rowIdx, colIndex++, result.getTimesheetID().toString());
                                        flexTable.setValue(rowIdx, colIndex++, result.getProjectID().toString());
                                        flexTable.setValue(rowIdx, colIndex, result.getEmployeeID().toString());

                                        if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                                            flexTable.getFlexCellFormatter().setVisible(rowIdx, 10 + columnOffset, false);
                                            flexTable.getFlexCellFormatter().setVisible(rowIdx, 11 + columnOffset, false);
                                            flexTable.getFlexCellFormatter().setVisible(rowIdx, 12 + columnOffset, false);
                                            flexTable.getFlexCellFormatter().setVisible(rowIdx, 13 + columnOffset, false);
                                            flexTable.getFlexCellFormatter().setVisible(rowIdx, 14 + columnOffset, false);
                                        } else {
                                            flexTable.getFlexCellFormatter().setVisible(rowIdx, 9 + columnOffset, false);
                                            flexTable.getFlexCellFormatter().setVisible(rowIdx, 10 + columnOffset, false);
                                            flexTable.getFlexCellFormatter().setVisible(rowIdx, 11 + columnOffset, false);
                                            flexTable.getFlexCellFormatter().setVisible(rowIdx, 12 + columnOffset, false);
                                            flexTable.getFlexCellFormatter().setVisible(rowIdx, 13 + columnOffset, false);
                                        }

                                        total += result.getSum();
                                        rowIdx++;
                                    }
                                }
                                totals += total;
                                header.setText(0, 0, ("Employee".equals(groupByName) ? projectStrings.hoursSpentBy() : "Date".equals(groupByName) ? projectStrings.hoursSpentOn() : projectStrings.hoursSpentFor()) +
                                        " " + item.getName() + " ( " + wfmStrings.total() + ": " + Utils.formatMinutes(total) + " )");
                                flexTable.sortRow(sortInx);
                                tablePanel.add(flexTable);
                            }
                            footerPanel.clear();
                            footerPanel.addStyleName("submitApproval__footer--cleared");
                            footerPanel.add(new Label(projectStrings.totalHoursSpent() + ": " + Utils.formatMinutes(totals)));
                            if (resultSize > 0) {
                                if (formType.equals(TIMESHEET_SUBMIT_FOR_APPROVAL_FORM)) {
                                    footerPanel.add(sendForApproval);
                                } else {
                                    footerPanel.add(proceed);
                                }
                            }
                            proceed.setVisible(approveRejectedItemsCount != 0);
                        }
                    }
                });
    }

    private void showApproveCommentPopup(final SortableTable flexTable, final Integer rowIndex) {
        final KpiModal popup = new KpiModal();
        popup.setCloseButton(true);
        popup.setWidth(400);
        final TextArea2 comment = new TextArea2();
        comment.getTextArea().setVisibleLines(5);
        comment.setWidth("100%");
        VerticalPanel commentPanel = new VerticalPanel();
        comment.getTextArea().addKeyPressHandler(event -> {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (rowIndex != null) {
                    if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                        flexTable.setValue(rowIndex, 15 + columnOffset, comment.getText());
                    } else {
                        flexTable.setValue(rowIndex, 14 + columnOffset, comment.getText());
                    }
                } else {
                    if (flexTable.getRowCount() > 0) {
                        for (int i = 2; i < flexTable.getRowCount(); i++) {
                            String taskName = flexTable.getHTML(i, 2);
                            Integer hoursSpent = Utils.parseMinutes(flexTable.getHTML(i, 7));
                            Integer employeeTaskId = null;
                            String billable = null;
                            Integer timesheetID = null;
                            Integer projID = null;
                            Integer emplID = null;
                            if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                                employeeTaskId = Integer.valueOf(flexTable.getHTML(i, 10 + columnOffset));
                                billable = flexTable.getHTML(i, 11 + columnOffset);
                                timesheetID = Integer.valueOf(flexTable.getHTML(i, 12 + columnOffset));
                                projID = Integer.valueOf(flexTable.getHTML(i, 13 + columnOffset));
                                emplID = Integer.valueOf(flexTable.getHTML(i, 14 + columnOffset));
                            } else {
                                employeeTaskId = Integer.valueOf(flexTable.getHTML(i, 9 + columnOffset));
                                billable = flexTable.getHTML(i, 10 + columnOffset);
                                timesheetID = Integer.valueOf(flexTable.getHTML(i, 11 + columnOffset));
                                projID = Integer.valueOf(flexTable.getHTML(i, 12 + columnOffset));
                                emplID = Integer.valueOf(flexTable.getHTML(i, 13 + columnOffset));
                            }
                            TaskTimeSheetEntry timeSheetEntry = new TaskTimeSheetEntry();
                            timeSheetEntry.setTaskName(taskName);
                            timeSheetEntry.setBillable("true".equals(billable));
                            TimeSheetEntry[] sheetEntry = new TimeSheetEntry[1];
                            sheetEntry[0] = new TimeSheetEntry();
                            sheetEntry[0].setTimeSheetId(timesheetID);
                            sheetEntry[0].setTimeSpent(hoursSpent);
                            sheetEntry[0].setEmployeeId(emplID);
                            timeSheetEntry.setProjectId(projID);
                            timeSheetEntry.setTotalTimeSpent(hoursSpent);
                            timeSheetEntry.setApproved(true);
                            timeSheetEntry.setRejected(false);
                            timeSheetEntry.setEntries(sheetEntry);
                            timeSheetEntry.setManagerApproveComment(comment.getText());
                            taskTimeSheetEntries.put(timesheetID, timeSheetEntry);
                        }
                    }
                }
                popup.close();
            }
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                popup.close();
            }
        });
        commentPanel.add(new Label(projectStrings.approvalComment()));
        commentPanel.add(comment);
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(sender -> {
            if (rowIndex != null) {
                Integer timesheetID = null;
                if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                    timesheetID = Integer.valueOf(flexTable.getHTML(rowIndex, 12 + columnOffset));
                } else {
                    timesheetID = Integer.valueOf(flexTable.getHTML(rowIndex, 11 + columnOffset));
                }
                TaskTimeSheetEntry entry = taskTimeSheetEntries.get(timesheetID);
                entry.setManagerApproveComment(comment.getText());
                entry.setApproved(true);
                entry.setRejected(false);
            } else {
                if (flexTable.getRowCount() > 0) {
                    for (int i = 2; i < flexTable.getRowCount(); i++) {
                        String taskName = flexTable.getHTML(i, 2);
                        Integer hoursSpent = Utils.parseMinutes(flexTable.getHTML(i, 7));
                        Integer employeeTaskId = null;
                        String billable = null;
                        Integer timesheetID = null;
                        Integer projID = null;
                        Integer emplID = null;
                        if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                            employeeTaskId = Integer.valueOf(flexTable.getHTML(i, 10 + columnOffset));
                            billable = flexTable.getHTML(i, 11 + columnOffset);
                            timesheetID = Integer.valueOf(flexTable.getHTML(i, 12 + columnOffset));
                            projID = Integer.valueOf(flexTable.getHTML(i, 13 + columnOffset));
                            emplID = Integer.valueOf(flexTable.getHTML(i, 14 + columnOffset));
                        } else {
                            employeeTaskId = Integer.valueOf(flexTable.getHTML(i, 9 + columnOffset));
                            billable = flexTable.getHTML(i, 10 + columnOffset);
                            timesheetID = Integer.valueOf(flexTable.getHTML(i, 11 + columnOffset));
                            projID = Integer.valueOf(flexTable.getHTML(i, 12 + columnOffset));
                            emplID = Integer.valueOf(flexTable.getHTML(i, 13 + columnOffset));
                        }
                        TaskTimeSheetEntry timeSheetEntry = new TaskTimeSheetEntry();
                        timeSheetEntry.setTaskName(taskName);
                        timeSheetEntry.setBillable("true".equals(billable));
                        TimeSheetEntry[] sheetEntry = new TimeSheetEntry[1];
                        sheetEntry[0] = new TimeSheetEntry();
                        sheetEntry[0].setTimeSheetId(timesheetID);
                        sheetEntry[0].setTimeSpent(hoursSpent);
                        sheetEntry[0].setEmployeeId(emplID);
                        timeSheetEntry.setProjectId(projID);
                        timeSheetEntry.setTotalTimeSpent(hoursSpent);
                        timeSheetEntry.setApproved(true);
                        timeSheetEntry.setRejected(false);
                        timeSheetEntry.setEntries(sheetEntry);
                        timeSheetEntry.setManagerApproveComment(comment.getText());
                        taskTimeSheetEntries.put(timesheetID, timeSheetEntry);
                    }
                }
            }
            popup.close();
        });
        popup.add(commentPanel);
        popup.addButton(saveButton);
        popup.open();
    }

    private void showRejectCommentPopup(final SortableTable flexTable, final Integer rowIndex) {
        final KpiModal popup = new KpiModal();
        popup.setCloseButton(true);
        popup.setWidth(400);
        final TextArea2 comment = new TextArea2();
        comment.getTextArea().setVisibleLines(5);
        comment.setWidth("100%");
        VerticalPanel commentPanel = new VerticalPanel();
        comment.getTextArea().addKeyPressHandler(event -> {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (rowIndex != null) {
                    if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                        flexTable.setValue(rowIndex, 15 + columnOffset, comment.getText());
                    } else {
                        flexTable.setValue(rowIndex, 14 + columnOffset, comment.getText());
                    }
                } else {
                    if (flexTable.getRowCount() > 0) {
                        for (int i = 2; i < flexTable.getRowCount(); i++) {
                            String taskName = flexTable.getHTML(i, 2);
                            Integer hoursSpent = Utils.parseMinutes(flexTable.getHTML(i, 7));
                            Integer employeeTaskId = null;
                            String billable = null;
                            Integer timesheetID = null;
                            Integer projID = null;
                            Integer emplID = null;
                            if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                                employeeTaskId = Integer.valueOf(flexTable.getHTML(i, 10 + columnOffset));
                                billable = flexTable.getHTML(i, 11 + columnOffset);
                                timesheetID = Integer.valueOf(flexTable.getHTML(i, 12 + columnOffset));
                                projID = Integer.valueOf(flexTable.getHTML(i, 13 + columnOffset));
                                emplID = Integer.valueOf(flexTable.getHTML(i, 14 + columnOffset));
                            } else {
                                employeeTaskId = Integer.valueOf(flexTable.getHTML(i, 9 + columnOffset));
                                billable = flexTable.getHTML(i, 10 + columnOffset);
                                timesheetID = Integer.valueOf(flexTable.getHTML(i, 11 + columnOffset));
                                projID = Integer.valueOf(flexTable.getHTML(i, 12 + columnOffset));
                                emplID = Integer.valueOf(flexTable.getHTML(i, 13 + columnOffset));
                            }
                            TaskTimeSheetEntry timeSheetEntry = new TaskTimeSheetEntry();
                            timeSheetEntry.setTaskName(taskName);
                            timeSheetEntry.setBillable("true".equals(billable));
                            TimeSheetEntry[] sheetEntry = new TimeSheetEntry[1];
                            sheetEntry[0] = new TimeSheetEntry();
                            sheetEntry[0].setTimeSheetId(timesheetID);
                            sheetEntry[0].setTimeSpent(hoursSpent);
                            sheetEntry[0].setEmployeeId(emplID);
                            timeSheetEntry.setProjectId(projID);
                            timeSheetEntry.setTotalTimeSpent(hoursSpent);
                            timeSheetEntry.setApproved(false);
                            timeSheetEntry.setRejected(true);
                            timeSheetEntry.setEntries(sheetEntry);
                            timeSheetEntry.setManagerComment(comment.getText());
                            taskTimeSheetEntries.put(timesheetID, timeSheetEntry);
                        }
                    }
                }
                popup.close();
            }
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                popup.close();
            }
        });
        commentPanel.add(new Label(wfmStrings.rejectionReason()));
        commentPanel.add(comment);
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(sender -> {
            if (rowIndex != null) {
                Integer timesheetID = null;
                if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                    timesheetID = Integer.valueOf(flexTable.getHTML(rowIndex, 12 + columnOffset));
                } else {
                    timesheetID = Integer.valueOf(flexTable.getHTML(rowIndex, 11 + columnOffset));
                }
                TaskTimeSheetEntry entry = taskTimeSheetEntries.get(timesheetID);
                entry.setManagerComment(comment.getText());
                entry.setApproved(false);
                entry.setRejected(true);
            } else {
                if (flexTable.getRowCount() > 0) {
                    for (int i = 2; i < flexTable.getRowCount(); i++) {
                        String taskName = flexTable.getHTML(i, 2);
                        Integer hoursSpent = Utils.parseMinutes(flexTable.getHTML(i, 7));
                        Integer employeeTaskId = null;
                        String billable = null;
                        Integer timesheetID = null;
                        Integer projID = null;
                        Integer emplID = null;
                        if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                            employeeTaskId = Integer.valueOf(flexTable.getHTML(i, 10 + columnOffset));
                            billable = flexTable.getHTML(i, 11 + columnOffset);
                            timesheetID = Integer.valueOf(flexTable.getHTML(i, 12 + columnOffset));
                            projID = Integer.valueOf(flexTable.getHTML(i, 13 + columnOffset));
                            emplID = Integer.valueOf(flexTable.getHTML(i, 14 + columnOffset));
                        } else {
                            employeeTaskId = Integer.valueOf(flexTable.getHTML(i, 9 + columnOffset));
                            billable = flexTable.getHTML(i, 10 + columnOffset);
                            timesheetID = Integer.valueOf(flexTable.getHTML(i, 11 + columnOffset));
                            projID = Integer.valueOf(flexTable.getHTML(i, 12 + columnOffset));
                            emplID = Integer.valueOf(flexTable.getHTML(i, 13 + columnOffset));
                        }
                        TaskTimeSheetEntry timeSheetEntry = new TaskTimeSheetEntry();
                        timeSheetEntry.setTaskName(taskName);
                        timeSheetEntry.setBillable("true".equals(billable));
                        TimeSheetEntry[] sheetEntry = new TimeSheetEntry[1];
                        sheetEntry[0] = new TimeSheetEntry();
                        sheetEntry[0].setTimeSheetId(timesheetID);
                        sheetEntry[0].setTimeSpent(hoursSpent);
                        sheetEntry[0].setEmployeeId(emplID);
                        timeSheetEntry.setProjectId(projID);
                        timeSheetEntry.setTotalTimeSpent(hoursSpent);
                        timeSheetEntry.setApproved(false);
                        timeSheetEntry.setRejected(true);
                        timeSheetEntry.setEntries(sheetEntry);
                        timeSheetEntry.setManagerComment(comment.getText());
                        taskTimeSheetEntries.put(timesheetID, timeSheetEntry);
                    }
                }
            }
            popup.close();
        });
        popup.add(commentPanel);
        popup.addButton(saveButton);
        popup.open();
    }

    private void batchSelect(SortableTable flexTable, KpiCheckBox checkBox, String rejectionComment) {
        Integer projectID = Integer.valueOf(checkBox.getName());
        if (checkBox.getValue()) {
            if (!projectIDs.contains(projectID)) {
                projectIDs.add(projectID);
            }
        } else {
            projectIDs.remove(projectID);
        }
        if (flexTable.getRowCount() > 0) {
            for (int i = 2; i < flexTable.getRowCount(); i++) {
                String taskName = flexTable.getHTML(i, 1);
                Integer hoursSpent = Utils.parseMinutes(flexTable.getHTML(i, 7));
                KpiCheckBox approveCheckBox = (KpiCheckBox) flexTable.getWidget(i, 8 + columnOffset);
                KpiCheckBox rejectCheckBox = null;
                if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                    rejectCheckBox = (KpiCheckBox) flexTable.getWidget(i, 9 + columnOffset);
                }
                Integer employeeTaskId = null;
                String billable = null;
                Integer timesheetID = null;
                Integer projID = null;
                Integer emplID = null;
                if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                    employeeTaskId = Integer.valueOf(flexTable.getHTML(i, 10 + columnOffset));
                    billable = flexTable.getHTML(i, 11 + columnOffset);
                    timesheetID = Integer.valueOf(flexTable.getHTML(i, 12 + columnOffset));
                    projID = Integer.valueOf(flexTable.getHTML(i, 13 + columnOffset));
                    emplID = Integer.valueOf(flexTable.getHTML(i, 14 + columnOffset));
                } else {
                    employeeTaskId = Integer.valueOf(flexTable.getHTML(i, 9 + columnOffset));
                    billable = flexTable.getHTML(i, 10 + columnOffset);
                    timesheetID = Integer.valueOf(flexTable.getHTML(i, 11 + columnOffset));
                    projID = Integer.valueOf(flexTable.getHTML(i, 12 + columnOffset));
                    emplID = Integer.valueOf(flexTable.getHTML(i, 13 + columnOffset));
                }
                if (checkBox.getValue()) {
                    if (wfmStrings.selectAll().equals(checkBox.getText()) || wfmStrings.approveAll().equals(checkBox.getText())) {
                        approveCheckBox.setValue(true);
                        if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                            rejectCheckBox.setValue(false);
                        }
                    } else {
                        approveCheckBox.setValue(false);
                        if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                            rejectCheckBox.setValue(true);
                        }
                    }
                    TaskTimeSheetEntry timeSheetEntry = new TaskTimeSheetEntry();
                    timeSheetEntry.setTaskName(taskName);
                    timeSheetEntry.setBillable("true".equals(billable));
                    TimeSheetEntry[] sheetEntry = new TimeSheetEntry[1];
                    sheetEntry[0] = new TimeSheetEntry();
                    sheetEntry[0].setTimeSheetId(timesheetID);
                    sheetEntry[0].setTimeSpent(hoursSpent);
                    sheetEntry[0].setEmployeeId(emplID);
                    timeSheetEntry.setProjectId(projID);
                    timeSheetEntry.setTotalTimeSpent(hoursSpent);
                    timeSheetEntry.setEntries(sheetEntry);
                    if (approveCheckBox.getValue() != null && approveCheckBox.getValue()) {
                        timeSheetEntry.setApproved(true);
                        timeSheetEntry.setRejected(false);
                    }
                    if (TIMESHEET_APPROVAL_FORM.equals(formType) && rejectCheckBox.getValue() != null && rejectCheckBox.getValue()) {
                        timeSheetEntry.setApproved(false);
                        timeSheetEntry.setRejected(true);
                    }
                    taskTimeSheetEntries.put(timesheetID, timeSheetEntry);
                } else {
                    approveCheckBox.setValue(false);
                    if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                        rejectCheckBox.setValue(false);
                    }
                    taskTimeSheetEntries.remove(timesheetID);
                }
            }
        }
    }

    private void runRadioButtonsAction(SortableTable flexTable, Integer rowIndex, KpiCheckBox checkBox) {
        if (checkBox.getValue() != null) {
            Integer timesheetID = null;
            if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                timesheetID = Integer.valueOf(flexTable.getHTML(rowIndex, 12 + columnOffset));
            } else {
                timesheetID = Integer.valueOf(flexTable.getHTML(rowIndex, 11 + columnOffset));
            }
            if (!checkBox.getValue()) {
                taskTimeSheetEntries.remove(timesheetID);
            } else {
                String taskName = flexTable.getHTML(rowIndex, 1);
                Integer hoursSpent = Utils.parseMinutes(flexTable.getHTML(rowIndex, 6));
                String billable = null;
                Integer projID = null;
                Integer emplID = null;
                if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
                    billable = flexTable.getHTML(rowIndex, 11 + columnOffset);
                    projID = Integer.valueOf(flexTable.getHTML(rowIndex, 13 + columnOffset));
                    emplID = Integer.valueOf(flexTable.getHTML(rowIndex, 14 + columnOffset));
                } else {
                    billable = flexTable.getHTML(rowIndex, 10 + columnOffset);
                    projID = Integer.valueOf(flexTable.getHTML(rowIndex, 12 + columnOffset));
                    emplID = Integer.valueOf(flexTable.getHTML(rowIndex, 13 + columnOffset));
                }

                TaskTimeSheetEntry timeSheetEntry = new TaskTimeSheetEntry();
                timeSheetEntry.setTaskName(taskName);
                timeSheetEntry.setBillable("true".equals(billable));
                TimeSheetEntry[] sheetEntry = new TimeSheetEntry[1];
                sheetEntry[0] = new TimeSheetEntry();
                sheetEntry[0].setTimeSheetId(timesheetID);
                sheetEntry[0].setTimeSpent(hoursSpent);
                sheetEntry[0].setEmployeeId(emplID);
                timeSheetEntry.setProjectId(projID);
                timeSheetEntry.setTotalTimeSpent(hoursSpent);
                timeSheetEntry.setEntries(sheetEntry);
                if (wfmStrings.approve().equals(checkBox.getText())) {
                    if (checkBox.getValue()) {
                        timeSheetEntry.setApproved(true);
                        timeSheetEntry.setRejected(false);
                    }
                } else {
                    if (checkBox.getValue()) {
                        timeSheetEntry.setApproved(false);
                        timeSheetEntry.setRejected(true);
                    }
                }
                taskTimeSheetEntries.put(timesheetID, timeSheetEntry);
            }
        }
    }

    public boolean validate() {
        int i = 0;

        if (!Validation.validateDate(fromDate, new HTML(""), true)) {
            i++;
        }
        if (!Validation.validateDate(toDate, new HTML(""), true)) {
            i++;
        }
        if (groupBy.getSelectedItem() != null && groupBy.getSelectedItem().getId() == 0) {
            i++;
        }
        return i != 0;
    }


    private List sortByValue(final Map m) {
        List keys = new ArrayList();
        keys.addAll(m.keySet());
        keys.sort((o1, o2) -> {
            Object v1 = m.get(o1);
            Object v2 = m.get(o2);
            if (v1 == null) {
                return (v2 == null) ? 0 : 1;
            } else if (v1 instanceof Comparable) {
                return ((Comparable) v1).compareTo(v2);
            } else {
                return 0;
            }
        });
        return keys;
    }

    private MaterialPanel getGroupBoxTitlePanel(String text) {
        MaterialPanel panel = new MaterialPanel("group-box__title");

        Span title = new Span(text);
        panel.add(title);

        return panel;
    }

    private MaterialPanel getPeriodDatePanel(DatePicker fromDate, DatePicker toDate) {
        MaterialPanel groupBoxItemPanel = new MaterialPanel("group-box__item invoice__date-due-date group-box__item--split-right");

        GBoxItem invoiceDateItem = new GBoxItem(wfmStrings.from(), fromDate);
        GBoxItem invoiceDueDateItem = new GBoxItem(wfmStrings.to(), toDate);

        invoiceDateItem.setStyleName("invoice__date");
        invoiceDueDateItem.setStyleName("invoice__due-date");

        groupBoxItemPanel.add(invoiceDateItem);
        groupBoxItemPanel.add(invoiceDueDateItem);
        return groupBoxItemPanel;
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

    @Override
    public String getPropertyCode() {
        return TIMESHEET_APPROVAL_LIST;
    }
}
