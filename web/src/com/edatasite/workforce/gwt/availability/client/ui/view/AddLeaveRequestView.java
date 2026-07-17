package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.availability.client.localization.AvailabilityMessages;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityServiceAsync;
import com.edatasite.workforce.gwt.availability.client.rpc.NewLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.TimeSlot;
import com.edatasite.workforce.gwt.availability.client.ui.view.customTabs.EmployeeLeaveRequestChart;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.UnitType;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.BackupEmployeeNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiAppendedRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.laborPeriod.LaborPeriodWidget;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableForLeaveRequest;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.view.CalendarTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AddLeaveRequestView extends CustomForm2 implements FormHasCustomFieldInterface, Constants, Colapse {

    private static final AvailabilityMessages availabilityMessages = AvailabilityMessages.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final AvailabilityServiceAsync availabilityService = AvailabilityService.App.get();

    private static final String unauthorized_leave = "LR_TYPE_UNAUTHORIZED_LEAVE";

    private Integer employeeID;
    private Integer reasonID;
    private Numbering leaveRequestCode;
    private NewLeaveRequest leaveRequest;
    private SelectPanel employeesPanel;
    private TextArea2 description;
    private FlexTable takenDays;
    public MultiTableForLeaveRequest backupEmployeeTable;
    private ChosenApproversWidget approver;
    private DataListBox reason;
    private LaborPeriodWidget labourPeriodWidget;
    private EmployeeLookUpWithCode employees;
    private boolean addEmployee;
    private boolean recalculate = false;
    private CalendarTabWidget calendarTabWidget;
    private EmployeeLeaveRequestChart chart;
    private WfmButton2 addButton;
    private WfmButton2 draftButton;
    private DateTimePicker dateTime;
    private String timeslotStartTime, timeslotEndTime;

    private KpiAppendedRadioButton paid;
    private KpiAppendedRadioButton takenLaveBy;
    private FormHasCustomField customFieldUtil;
    private final Span balance = new Span(wfmStrings.leaveDays() + ": 0.00");

    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy");
    private Integer selectedYear = Integer.parseInt(dateFormat.format(new Date()));
    private boolean hourly;
    private boolean includeDayOffs;
    private FooterUploadPanel footerUploadPanel;
    public LinkedHashMap<String, FormProperty> formPropertyMap;
    private Integer duration;
    private boolean fromLink;
    private Integer objectId;
    private FormGroup leavePeriodForm;
    private FormGroup leaveBy;
    private Date employeeStartDate;
    private BackupEmployeeNavBox backupEmployeeNavBox;

    public AddLeaveRequestView(Integer employeeID, String[] params) {
        super("availabilityadd", wfmStrings.addRequest());
        this.employeeID = employeeID;
        if (params != null && params.length > 2) {
            this.reasonID = Integer.parseInt(params[2]);
            this.fromLink = true;
        }
    }

    public AddLeaveRequestView(Integer employeeID, String[] params, Integer objectId) {
        super("availabilityadd", hrmsStrings.editLeaveRequest());
        this.employeeID = employeeID;
        this.objectId = objectId;
        if (params != null && params.length > 3) {
            this.reasonID = Integer.parseInt(params[3]);
            this.fromLink = true;
        }
    }

    public AddLeaveRequestView(Integer employeeID, boolean recalculate, Integer objectId) {
        super("availabilityadd", hrmsStrings.editLeaveRequest());
        this.employeeID = employeeID;
        this.objectId = objectId;
        this.recalculate = recalculate;
        this.fromLink = true;
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    public String getIconStyle() {
        return "hrms add-leave-request";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.LEAVE_REQUEST_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVE_DAYS_INSERTED, AddLeaveRequestView.this, (sender, args) -> onLeaveDaysInserted(true));

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.LeaveRequest, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                GWT.log("", throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddLeaveRequestView.super.onInitialize();
                if (objectId == null) {
                    setDefaultValuesByFormProperty();
                }
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        String add_leave_request_view = "add_leave_request_view_";
        addEmployee = Utils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUESTS_ADD_FOR_OTHERS);
        employees = new EmployeeLookUpWithCode();
        employees.setContextCode(PermissionConstants.HRMS_CONTEXT);
        employees.ensureDebugId(add_leave_request_view + "employees");
        employees.addStyleName(DEFAULT_WIDTH);
        employees.getSuggestBox().addSelectionHandler(stringValueChangeEvent -> onEmployeeChange());
        employees.setEnabled(!fromLink);

        if (addEmployee) {
            addField(CustomFormConstants.EMPLOYEES, employees, formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYEES) != null &&
                    formPropertyMap.get(CustomFormConstants.EMPLOYEES).isChanged() ? getTitle(formPropertyMap.get(CustomFormConstants.EMPLOYEES).getTitle(), formPropertyMap.get(CustomFormConstants.EMPLOYEES).isRequired()) : getTitle(wfmStrings.employee(), true,
                    formPropertyMap.get(CustomFormConstants.EMPLOYEES).isInformation()));
            if (formPropertyMap.get(CustomFormConstants.EMPLOYEES).isInformation()) {
                new KpiToolTip(employees, formPropertyMap.get(CustomFormConstants.EMPLOYEES).getInformationText());
            }
        } else {
            TextBox textBox = new TextBox();
            textBox.setText(Utils.getFullName());
            textBox.setEnabled(false);
            addField(CustomFormConstants.EMPLOYEES, textBox, formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYEES) != null &&
                    formPropertyMap.get(CustomFormConstants.EMPLOYEES).isChanged() ? getTitle(formPropertyMap.get(CustomFormConstants.EMPLOYEES).getTitle(), formPropertyMap.get(CustomFormConstants.EMPLOYEES).isRequired()) : getTitle(wfmStrings.employee(), true,
                    formPropertyMap.get(CustomFormConstants.EMPLOYEES).isInformation()));
            if (formPropertyMap.get(CustomFormConstants.EMPLOYEES).isInformation()) {
                new KpiToolTip(textBox, formPropertyMap.get(CustomFormConstants.EMPLOYEES).getInformationText());
            }
        }


        leaveRequestCode = new Numbering(false);
        leaveRequestCode.addStyleName(Constants.DEFAULT_WIDTH);
        leaveRequestCode.ensureDebugId(add_leave_request_view + "leave_request_code");
//        if (objectId != null) {
//            leaveRequestCode.getTxtPrefix().setWidth("100%");
//        }

        addField(CustomFormConstants.LEAVE_REQUEST_NUMBER, leaveRequestCode, getTitle(wfmStrings.code()));

        backupEmployeeNavBox = new BackupEmployeeNavBox();


        backupEmployeeTable = new MultiTableForLeaveRequest(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgets(null);

            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> widgetsMap : backupEmployeeTable.getWidgets()) {
                    EmployeeLookUpWithCode backupEmployee = (EmployeeLookUpWithCode) widgetsMap.get(MultiTable.LOOK_UP_BOX);
                    if (backupEmployee.getSelectedItem() != null) {
                        return true;
                    }
                }
                return false;
            }


        }, false, backupEmployeeNavBox);

        backupEmployeeTable.setViewMode(objectId != null);
        backupEmployeeTable.setAddForm(true);
        backupEmployeeTable.removeAllRows();
        backupEmployeeTable.setDeleteBtnListener(() -> {
            buttonChanges();
        });

        employees.getSuggestBox().addSelectionHandler(stringValueChangeEvent -> {
            if (backupEmployeeNavBox != null) {
                backupEmployeeNavBox.getEmployeeFromLeave(employees.getSelectedItemID());
            }
        });

        approver = new ChosenApproversWidget(RelationItem.TYPE_LEAVE_REQUEST, objectId, employeeID);
        approver.ensureDebugId("approver-list");
        if (employeeID != null) {
            approver.updateLookUps(employeeID);
        }
        approver.ensureDebugId(add_leave_request_view + "approver1");
        approver.addStyleName("AddLeaveRequestView-approver");
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddLeaveRequestView.this, (sender, args) -> {
            Timer timer = new Timer() {
                @Override
                public void run() {
                    if (approver.getFirstApproverLookUp() != null) {
                        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                        if (addButton != null) {
                            if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                                addButton.setText(wfmStrings.approveAndClose());
                            } else if (recalculate) {
                                addButton.setText(wfmStrings.saveAndRecalculate());
                            } else {
                                addButton.setText(wfmStrings.submit());
                            }
                        }
                        approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                            buttonChanges();
                        });
                    }
                }
            };
            timer.schedule(1000);
        });


        Div inputGroup = new Div("input-group");
        inputGroup.add(approver);
        Div append = new Div("input-group-append");
        inputGroup.add(append);
        WfmButton2 button1 = new WfmButton2(wfmStrings.backup());
        new KpiToolTip(button1, wfmStrings.addBackupEmployee());
        append.add(button1);
        button1.addClickHandler(event -> {
            backupEmployeeTable.onAddLinkClicked(backupEmployeeTable.getWidgets().size());
            Widget widget = backupEmployeeTable.getWidget(backupEmployeeTable.getWidgets().size() - 1);

        });

        WfmButton2 button2 = new WfmButton2("", "btn btn--icon", WfmButton2.ICON_TRASH);
        append.add(button2);
        FormGroup formGroup22 = new FormGroup(inputGroup);
        button2.addClickHandler(event -> {
            backupEmployeeTable.removeAllRows();
            backupEmployeeNavBox.removeAllMapValues();
            buttonChanges();
        });


        MaterialPanel calendarTabBar = new MaterialPanel("pg_leave__calendar box-bg--1 box-radius");
        calendarTabWidget = new CalendarTabWidget(employeeID, selectedYear, null, new Date(), null);
        calendarTabBar.add(calendarTabWidget);

        MaterialPanel chartTabBar = new MaterialPanel("pg_leave__calendar box-bg--1 box-radius");
        chart = new EmployeeLeaveRequestChart(employeeID == null ? Utils.getUserID() : employeeID, selectedYear);
        chartTabBar.add(chart);

        description = new TextArea2(255, wfmStrings.description());
        description.ensureDebugId(add_leave_request_view + "rich_text_description");
        description.addStyleName("AddLeaveRequestView-description");
        final TableColumn[] assignColumns = new TableColumn[2];
        assignColumns[0] = new TableColumn(wfmStrings.employee(), wfmStrings.employee());
        assignColumns[1] = new TableColumn(wfmStrings.delete(), wfmStrings.action());
        employeesPanel = new SelectPanel(assignColumns);
        employeesPanel.setDefaultSettings();

        takenDays = new FlexTable();
        takenDays.setStyleName("leave-reason-stats-table");
        takenDays.setVisible(false);

        paid = new KpiAppendedRadioButton(wfmStrings.payable(), hrmsStrings.nonPaid());
        paid.ensureDebugId(add_leave_request_view + "type_paid");

        takenLaveBy = new KpiAppendedRadioButton(wfmStrings.day(), wfmStrings.money());
        takenLaveBy.ensureDebugId(add_leave_request_view + "type_by_day");
        takenLaveBy.setActive(true);

        paid.addSelectionHandler(valueChangeEvent -> {
            if (!paid.isActive()) {
                takenLaveBy.setActive(true);
            }
        });

        reason = new DataListBox();
        reason.ensureDebugId(add_leave_request_view + "reason");
        reason.addStyleName(DEFAULT_WIDTH);
        reason.addValueChangeHandler(sender -> {
            dateTime.dueDate.setDefaultFormat();
            takenDays.setVisible(reason.isSomethingSelected());
            if (reason.getSelectedItem() != null) {
                paid.setActive(!unauthorized_leave.equals(reason.getSelectedItem().getDescription()));
                if (!unauthorized_leave.equals(reason.getSelectedItem().getDescription())) {
                    takenLaveBy.setActive(true);
                }
                if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && "LR_TYPE_ANNUAL_LEAVE".equals(reason.getSelectedItem().getDescription())) {
                    hideFieldsForAnnual(true);
                    getEmployeeAdditonalAllowances();
                }
            }
            reasonID = reason.getSelectedId();
            refreshReason(reasonID);
        });

        dateTime = new DateTimePicker(false, true);
        dateTime.setAllDay(false);
        dateTime.setAutoValue(false);
        dateTime.startDate.ensureDebugId("leave_start_date");
        dateTime.startTime.ensureDebugId("leave_start_time");
        dateTime.dueDate.ensureDebugId("leave_due_date");
        dateTime.endTime.ensureDebugId("leave_end_time");
        dateTime.startDate.addValueChangeHandler(change -> {
            backupEmployeeNavBox.leaveStartDate = dateTime.getStartDate();
            if (dateTime.getDueDate() != null) {
                backupEmployeeNavBox.leaveDueDate = dateTime.getDueDate();
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && "LR_TYPE_ANNUAL_LEAVE".equals(reason.getSelectedItem().getDescription())) {
                getDueDate();
            } else {
                getDueDateByPeriod();
                getEmployeeAdditonalAllowances();
            }
        });

        dateTime.dueDate.addValueChangeHandler(change -> {
            backupEmployeeNavBox.leaveStartDate = dateTime.getStartDate();
            backupEmployeeNavBox.leaveDueDate = dateTime.getDueDate();
            onDateChange(false);
        });

        dateTime.startTime.getListBox().addChangeHandler(change -> {
            if (dateTime.startTime.getItemIndex(dateTime.getStartTime().getValue()) < dateTime.startTime.getItemIndex(timeslotStartTime)) {
                dateTime.setStartTime(timeslotStartTime);
            }
            getLeaveDuration();
        });
        dateTime.endTime.getListBox().addChangeHandler(change -> {
            if (dateTime.endTime.getItemIndex(dateTime.getEndTime().getValue()) > dateTime.endTime.getItemIndex(timeslotEndTime)) {
                dateTime.setEndTime(timeslotEndTime);
            }
            getLeaveDuration();
        });
        dateTime.startTime.addValueChangeHandler(valueChangeEvent -> {
            if (dateTime.startTime.getItemIndex(dateTime.getStartTime().getValue()) < dateTime.startTime.getItemIndex(timeslotStartTime)) {
                dateTime.setStartTime(timeslotStartTime);
            }
            getLeaveDuration();
        });
        dateTime.endTime.addValueChangeHandler(valueChangeEvent -> {
            if (dateTime.endTime.getItemIndex(dateTime.getEndTime().getValue()) > dateTime.endTime.getItemIndex(timeslotEndTime)) {
                dateTime.setEndTime(timeslotEndTime);
            }
            getLeaveDuration();
        });

        dateTime.startTime.setWidth("55px");
        dateTime.startTime.setEnabled(false);

        dateTime.endTime.setWidth("55px");
        dateTime.endTime.setEnabled(false);

        MaterialPanel datePanel = new MaterialPanel("form-row");
        Div div1 = new Div("col-6");
        div1.add(new InputGroup(dateTime.startDate, dateTime.startTime));

        Div div2 = new Div("col-6");
        div2.add(new InputGroup(dateTime.dueDate, dateTime.endTime));
        datePanel.add(div1);
        datePanel.add(div2);

        //init add fields
        addTitleField(GENERAL_INFORMATION, wfmStrings.generalInformation());
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        FormGroup formGroup = new FormGroup(wfmStrings.leavePeriod() + ":", datePanel);
        Div dateLabel = formGroup.getGroupLabel();
        dateLabel.addStyleName("label-group");
        dateLabel.add(balance);

        if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION)) {
            labourPeriodWidget = new LaborPeriodWidget(employeeID, false, recalculate);

            MaterialPanel leavePeriodPanel = new MaterialPanel("form-row");
            Div periodHeader = new Div("col-12");
            periodHeader.add(labourPeriodWidget);
            leavePeriodPanel.add(periodHeader);

            leavePeriodForm = new FormGroup(leavePeriodPanel);
            leavePeriodForm.setVisible(false);
            addField(LEAVE_FOR_PERIOD, leavePeriodForm, null);
        }

        addField(DATE_PERIOD, formGroup, null);

        addField(REASON, reason, formPropertyMap != null && formPropertyMap.get(REASON) != null &&
                formPropertyMap.get(REASON).isChanged() ? getTitle(formPropertyMap.get(REASON).getTitle(), formPropertyMap.get(REASON).isRequired())
                : getTitle(wfmStrings.reason(), true));
        addField(DESCRIPTION, description, null);
        addField(CALENDAR, calendarTabBar, null);
        addField(CustomFormConstants.CHART, chartTabBar, null);


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddLeaveRequestView.this, (sender, args) -> {
            if (objectId == null && Utils.hasPermission(PermissionConstants.BACKUP_EMPLOYEE_BUTTON)) {
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVER) != null) {
                    addField(CustomFormConstants.APPROVER, formGroup22, getTitle(formPropertyMap.get(CustomFormConstants.APPROVER).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVER).getTitle() : wfmStrings.approver(),
                                    formPropertyMap.get(CustomFormConstants.APPROVER).isRequired()), false,
                            formPropertyMap.get(CustomFormConstants.APPROVER).isInformation());
                    approver.setEnabled(!formPropertyMap.get(CustomFormConstants.APPROVER).isDisabled());
                    if (formPropertyMap.get(CustomFormConstants.APPROVER).isInformation()) {
                        new KpiToolTip(formGroup22, formPropertyMap.get(CustomFormConstants.APPROVER).getInformationText());
                    }
                } else {
                    addField(CustomFormConstants.APPROVER, approver, getTitle(wfmStrings.approver(), true));
                }
            } else {
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVER) != null) {
                    addField(CustomFormConstants.APPROVER, approver, getTitle(formPropertyMap.get(CustomFormConstants.APPROVER).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVER).getTitle() : wfmStrings.approver(),
                                    formPropertyMap.get(CustomFormConstants.APPROVER).isRequired()), false,
                            formPropertyMap.get(CustomFormConstants.APPROVER).isInformation());
                    approver.setEnabled(!formPropertyMap.get(CustomFormConstants.APPROVER).isDisabled());
                    if (formPropertyMap.get(CustomFormConstants.APPROVER).isInformation()) {
                        new KpiToolTip(approver, formPropertyMap.get(CustomFormConstants.APPROVER).getInformationText());
                    }
                } else {
                    addField(CustomFormConstants.APPROVER, approver, getTitle(wfmStrings.approver(), true));
                }
            }
        });

        FormGroup paidGroup = new FormGroup(formPropertyMap != null && formPropertyMap.get(TYPE) != null &&
                formPropertyMap.get(TYPE).isChanged() ? formPropertyMap.get(TYPE).getTitle()
                : wfmStrings.type() + ":", paid);

        leaveBy = new FormGroup(formPropertyMap != null && formPropertyMap.get(TAKE_LIVE_TYPE) != null &&
                formPropertyMap.get(TAKE_LIVE_TYPE).isChanged() ? formPropertyMap.get(TAKE_LIVE_TYPE).getTitle()
                : hrmsStrings.takeLeaveBy() + ":", takenLaveBy);

        addField(TAKE_LIVE_TYPE, leaveBy, null);
        addField(TYPE, paidGroup, null);
        addField(BACKUP_EMPLOYEE, backupEmployeeTable, Utils.hasPermission(PermissionConstants.BACKUP_EMPLOYEE_BUTTON) ? wfmStrings.backupEmployee() : null);


        if (Utils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUEST_SEND_NOTIFICATION)) {
            addTitleField(SEND_NOTIFICATION, wfmStrings.notifications());
            addField(ASSIGNEES, employeesPanel, null);

            addRowsLeave();

            empoyeeListScrollDownEvent(AddLeaveRequestView.this);
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYE_TREE_WIDGET_REFRESH, AddLeaveRequestView.this, (sender, args) -> addRowsLeave(true));
        }

        getCustomFieldUtil().drawCustomFields(this, null);
        show();
    }

    private void buttonChanges() {
        WfmButton2 saveButton2 = (WfmButton2) getButton(SAVE_AND_CLOSE);
        String buttonName = wfmStrings.submit();
        if (saveButton2 != null) {
            List<WidgetsMap> allBackupEmployeeData = backupEmployeeTable != null ? backupEmployeeTable.getWidgetsMaps() : null;
            SelectItem item2 = approver != null && approver.getFirstApproverLookUp() != null ? approver.getFirstApproverLookUp().getSelectedItem() : null;

            EmployeeLookUpWithCode employeeLookUpWithCode = allBackupEmployeeData != null && allBackupEmployeeData.size() > 0 && allBackupEmployeeData.get(0) != null && allBackupEmployeeData.get(0).getWidget("employee") != null ? (EmployeeLookUpWithCode) allBackupEmployeeData.get(0).getWidget("employee") : null;

            if (employeeLookUpWithCode != null && employeeLookUpWithCode.getSelectedItemID() != null) {
                if (Utils.getUserID().equals(employeeLookUpWithCode)) {
                    buttonName = wfmStrings.approveAndClose();
                }
            } else if (item2 != null && item2.getId() != null && Utils.getUserID().equals(item2.getId())) {
                buttonName = wfmStrings.approveAndClose();
            }
            addButton.setText(buttonName);
        }
    }

    private void getDueDateByPeriod() {
        selectedYear = Integer.parseInt(dateFormat.format(dateTime.getStartDate()));
        if (duration != null) {
            if (includeDayOffs) {
                dateTime.dueDate.setDate(DateUtils.addDays(dateTime.getStartDate(), duration - 1));
                onDateChange(true);
                dateTime.dueDate.setMonth(dateTime.startDate.getDate());
            } else {
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setEmployeeId(employeeID);
                fp.setStartDate(dateTime.getStartDate());
                fp.setLimit(duration);
                availabilityService.getEndDateForLeaveRequest(fp, new AsyncCallback<Date>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Date date) {
                        dateTime.setDueDate(date);
                        onDateChange(true);
                        dateTime.dueDate.setMonth(dateTime.startDate.getDate());
                    }
                });
            }
        } else {
            onDateChange(true);
            dateTime.dueDate.setMonth(dateTime.startDate.getDate());
        }
    }

    private void hideFieldsForAnnual(boolean hide) {
        labourPeriodWidget.setEmployeeID(employeeID, reason.getSelectedItem().getDescription(), dateTime.startDate.getDate(), objectId);
        leavePeriodForm.setVisible(hide);
    }

    private void hideLeaveByForm(boolean hide) {
        takenLaveBy.setEnabled(!hide);
        takenLaveBy.setVisible(!hide);
        leaveBy.setVisible(!hide);
    }


    @Override
    protected void addButtons() {
        if (draftButton == null) {
            draftButton = addButton(wfmStrings.draft(), BTN_DEFAULT_OUTLINE, SAVE_TO_DRAFT,
                    "add_leave_request_view_draft_button", event -> save(Constants.DRAFT));
        }
        String buttonText = wfmStrings.submit();
        if (addButton == null) {
            if (approver.getFirstApproverLookUp() != null) {
                SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                    buttonText = wfmStrings.approveAndClose();
                }
            }

            if (Utils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUEST_SUBMIT_BUTTON)) {
                addButton = addButton(buttonText, WfmButton2.BTN_PRIMARY, SAVE_AND_CLOSE,
                        "add_leave_request_view_save_button", event -> save(null));
            }
        }
    }

    private void refreshReason(Integer reasonId) {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getReason(reasonId, new AbstractAsyncCallback<ReasonItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(ReasonItem reasonItem) {
                LoadingPanel.loading(false);
                hourly = UnitType.HOURLY.equals(reasonItem.getUnitType());
                includeDayOffs = reasonItem.isIncludeDayOffs();
                duration = reasonItem.getDuration() != null ? reasonItem.getDuration().intValue() : null;

                dateTime.startTime.setEnabled(hourly);
                dateTime.endTime.setEnabled(hourly);
                if (duration != null) {
                    if (includeDayOffs) {
                        dateTime.dueDate.setDate(DateUtils.addDays(dateTime.getStartDate(), duration - 1));
                        onDateChange(true);
                    } else {
                        ListingFilterParameter fp = new ListingFilterParameter();
                        fp.setEmployeeId(employeeID);
                        fp.setStartDate(dateTime.getStartDate());
                        fp.setLimit(duration);
                        availabilityService.getEndDateForLeaveRequest(fp, new AsyncCallback<Date>() {
                            @Override
                            public void onFailure(Throwable throwable) {

                            }

                            @Override
                            public void onSuccess(Date date) {
                                dateTime.setDueDate(date);
                                onDateChange(true);
                            }
                        });
                    }
                    dateTime.dueDate.setEnabled(false);
                    dateTime.dueDate.setTextBoxEnabled(false);
                    dateTime.endTime.setEnabled(false);
                } else {
                    if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && "LR_TYPE_ANNUAL_LEAVE".equals(reason.getSelectedItem().getDescription())) {
                        dateTime.dueDate.setEnabled(false);
                        dateTime.dueDate.setTextBoxEnabled(false);
                    } else {
                        dateTime.dueDate.setEnabled(true);
                        dateTime.dueDate.setTextBoxEnabled(true);
                    }
                    onDateChange(true);
                }
            }
        });
    }

    private void refreshChart() {
        chart.setSelectedYear(selectedYear);
        chart.setEmployeeID(employeeID);
        chart.setStartDate(dateTime.getStartDate());
        chart.setReasonID(reasonID);
        chart.drawTab();
    }

    private void onDateChange(boolean start) {
        if (employeeID != null) {
            if (timeslotStartTime == null || timeslotEndTime == null) {
                updateTimeSlot(start);
            } else {
                dateTime.setStartTime(timeslotStartTime);
                dateTime.setEndTime(timeslotEndTime);
            }
            if (start) {
                refreshChart();
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && "LR_TYPE_ANNUAL_LEAVE".equals(reason.getSelectedItem().getDescription())) {
                Integer leaveDays = labourPeriodWidget.getLeaveDays();
                balance.setText(wfmStrings.leaveDays() + ": " + leaveDays);
            } else {
                getLeaveDuration();
            }
        }
    }

    private void onLeaveDaysInserted(boolean start) {
        getDueDate();
        onDateChange(start);
        hideLeaveByForm(labourPeriodWidget.getMultiLeaveValues() != null && labourPeriodWidget.getMultiLeaveValues().size() > 0);
    }

    private void getLeaveDuration() {
        if (dateTime.getStartDate() != null && dateTime.getDueDate() != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setIncludeDayOff(includeDayOffs);
            fp.setEmployeeId(employeeID);
            fp.setAllDay(!hourly);
            if (reason.getSelectedItem() != null) {
                fp.setReasonCode(reason.getSelectedItem().getDescription());
            }

            AvailabilityService.App.get().getLeaveDaysCount(fp, new DateNonConvertable(dateTime.getStartDate()), new DateNonConvertable(dateTime.getDueDate()), new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {
                    balance.setText(wfmStrings.leaveDays() + ": 0.00");
                }

                @Override
                public void onSuccess(String duration) {
                    balance.setText(wfmStrings.leaveDays() + ": " + duration);
                }
            });

        } else {
            balance.setText(wfmStrings.leaveDays() + ": 0.00");
        }
        refreshCalendar();
    }

    @Override
    protected void getDataToFillFields() {
        if (objectId != null) {
            getFillDataOnEdit();
        } else {
            getFillDataOnAdd();
        }
    }

    private void getFillDataOnAdd() {
        availabilityService.getLeaveRequestRPC(employeeID, new AbstractAsyncCallback<NewLeaveRequest>() {
            public void failure(Throwable throwable) {
            }

            public void success(NewLeaveRequest leaveRequest) {
                if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_LEAVE_REQUEST_POLICY)) {
                    WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.OkCancel, true);
                    message.setTitle("Leave Request Policy");
                    message.setMessage(leaveRequest.getPolicy() != null ? leaveRequest.getPolicy() : "N/A");
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                            closeTab();
                        }
                    });
                    message.open();
                }
                dateTime.setStartDate(new Date());
                dateTime.setStartTime(leaveRequest.getTimeSlot().getStartHour() + ":" + leaveRequest.getTimeSlot().getStartMin());
                dateTime.setEndTime(leaveRequest.getTimeSlot().getEndHour() + ":" + leaveRequest.getTimeSlot().getEndMin());

                if (leaveRequest.getEmployee() != null) {
                    employeeID = leaveRequest.getEmployee();
                    employees.setSelected(leaveRequest.getEmployee(), leaveRequest.getEmployeeName());
                }
                if (leaveRequest.getNumberData() != null) {
                    leaveRequestCode.setNumberData(leaveRequest.getNumberData());
                }
                if (leaveRequest.getReasons() != null) {
                    reason.setItems(leaveRequest.getReasons());

                }
                if (reasonID != null) {
                    reason.setSelected(reasonID);
                    reason.setEnabled(false);
                    refreshReason(reasonID);
                }
                getCustomFieldUtil().fillCustomFieldsWithData(leaveRequest.getCustomFields());
                footerUploadPanel = new FooterUploadPanel(Constants.F_LEAVE_REQUEST, null, true);
                footer.addToLeftSide(footerUploadPanel);
                employeeStartDate = leaveRequest.getEmployeeStartDate();
                setDefaultValues();
            }
        });
    }

    private void getFillDataOnEdit() {
        AvailabilityService.App.get().getLeaveRequest(objectId, new AbstractAsyncCallback<StatisticsLeaveRequest>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(StatisticsLeaveRequest result) {
                AddLeaveRequestView.this.employeeID = result.getEmployeeId();
                if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_LEAVE_REQUEST_POLICY)) {
                    WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.OkCancel, true);
                    message.setTitle("Leave Request Policy");
                    message.setMessage(leaveRequest.getPolicy() != null ? leaveRequest.getPolicy() : "N/A");
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                            closeTab();
                        }
                    });
                    message.open();
                }
                dateTime.setStartDate(result.getStartDDate().getNonConvertedDate());
                dateTime.setStartTime(result.getStartDDate().getNonConvertedDate().getHours() + ":" + result.getStartDDate().getNonConvertedDate().getMinutes());
                dateTime.setDueDate(result.getEndDDate().getNonConvertedDate());
                dateTime.setEndTime(result.getEndDDate().getNonConvertedDate().getHours() + ":" + result.getEndDDate().getNonConvertedDate().getMinutes());
                backupEmployeeNavBox.getDateFromLeave(result.getStartDDate().getNonConvertedDate(), result.getEndDDate().getNonConvertedDate());
                if (result.getTakeByMoney()) {
                    paid.setActive(true);
                } else {
                    takenLaveBy.setActive(true);
                }
                if (result.getNumberData() != null) {
                    leaveRequestCode.setNumberData(result.getNumberData());
                }
                if (result.getEmployeeId() != null) {
                    employeeID = result.getEmployeeId();
                    employees.setSelected(result.getEmployeeId(), result.getEmployee());
                }
                if (result.getBackupEmployee() != null) {
                    backupEmployeeNavBox.setLeaveRequestSummaryValues(result.getBackupEmployee());
                    backupEmployeeTable.removeAllRows();
                    int index = 0;
                    for (BackupEmployeeItem item : result.getBackupEmployee()) {
                        ApproverItemMini parent = item.getParentBackupEmployee();
                        EmployeeLookUpWithCode withCode = new EmployeeLookUpWithCode();
                        WidgetsMap widgetsMap = new WidgetsMap();
                        widgetsMap.addWidgets(withCode);
                        widgetsMap.add("employee", withCode);
                        backupEmployeeTable.addWidgets(widgetsMap);
                        EmployeeLookUpWithCode c = (EmployeeLookUpWithCode) backupEmployeeTable.getWidgetsMaps().get(index).getWidget("employee");
                        c.setSelected(parent.getExactEmployee());
                        index++;
                    }
                }
                if (result.getReasons() != null) {
                    reason.setItems(result.getReasons());
                }
                if (result.getReasonId() != null) {
                    reason.setSelected(result.getReasonId());
                    refreshReason(result.getReasonId());
                }
                if (result.getDuration() != null) {
                    getLeaveDuration();
                }
                takenLaveBy.setActive(!result.getTakeByMoney());
                if (result.getDescription() != null) {
                    description.setText(result.getDescription());
                }
                if (result.getTypeCode() != null) {
                    paid.setActive("ST_PAID".equals(result.getTypeCode()));
                }
                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFields());
                if (footerUploadPanel == null) {
                    footerUploadPanel = new FooterUploadPanel(Constants.F_LEAVE_REQUEST, objectId, true);
                    footer.addToLeftSide(footerUploadPanel);
                }
                employeeStartDate = result.getEmployeeStartDate();
                if ("LR_TYPE_ANNUAL_LEAVE".equals(reason.getSelectedItem().getDescription())) {
                    hideFieldsForAnnual(true);
                    labourPeriodWidget.setMultiLeaveItems(result.getMultiLeaveList());
                }
                setDefaultValues();
                if (objectId != null && result.getOverallStatus() != null && result.getOverallStatus().getCode().equals(DRAFT)) {
                    addPdfButton().addClickHandler(click -> new PDFTemplateSelector(AccountingConstants.LEAVE_REQUEST, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            LeaveRequestObject requestObject = new LeaveRequestObject(objectId, employeeID, id);
                            String pdfUrl = CommandConstants.PDF_URL + "/employeeLeaveRequestViewPDFHandler";
                            HashMap<String, String> requestParams = requestObject.getRequestParams();
                            String svg = chart.getSVG();
                            if (chart != null && svg != null) {
                                requestParams.put("svg", svg);
                            }
                            Utils.sendPDFOrExcelRequest(panel, pdfUrl, requestParams, "_blank");
                        }
                    }));
                }
            }
        });
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    public static native void empoyeeListScrollDownEvent(AddLeaveRequestView view) /*-{
        var timerID;
        $wnd.jQuery(".treePanel-class").scroll(function () {
            clearTimeout(timerID);
            if ($wnd.jQuery(this).scrollTop() + $wnd.jQuery(this).innerHeight() + 100 >= $wnd.jQuery(this)[0].scrollHeight) {
                timerID = setTimeout(function () {
                    view.@com.edatasite.workforce.gwt.availability.client.ui.view.AddLeaveRequestView::addRowsLeave()();
                }, 200)
            }
        });
    }-*/;

    private void getEmployeeAdditonalAllowances() {
        availabilityService.getEmployeeAdditonalAllowances(employeeID, objectId, dateTime.getStartDate(), false, recalculate, new AbstractAsyncCallback<ArrayList<LaborPeriodRequest>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.show("There are no dates in attendance table. Please check!", Info.Type.WARNING);
                refreshCalendar();
            }

            @Override
            public void onSuccess(ArrayList<LaborPeriodRequest> labourPeriods) {
                if (labourPeriods != null) {
                    labourPeriodWidget.setLabourPeriods(labourPeriods);

                } else {
                    Info.show(wfmStrings.hireDateOrReasonSettingsIsWrong(), Info.Type.WARNING);
                }
            }
        });
    }

    public void addRowsLeave() {
        addRowsLeave(false);
    }

    private int offset = 0, limit = 200, employeeCount = 0;
    private boolean isEmpty = false;

    private void addRowsLeave(boolean search) {
        employeeCount = 0;
        if (!isEmpty && limit < 5000) {
            ListingFilterParameter fp = new ListingFilterParameter();
            if (search) {
                limit = 5000;
                fp.setLookUp(true);
            }
            fp.setStart(offset);
            fp.setLimit(limit);
            fp.setCorporate(true);
            LoadingPanel.loading(true);
            availabilityService.getEmployeesMap(fp, new AbstractAsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    GWT.log("", caught);
                }

                @Override
                public void success(HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> items) {
                    LoadingPanel.loading(false);
                    if (items.size() > 0) {
                        TreeSelect.setTickAllVisible(true);
                        for (WfmTreeItem departmentItem : items.keySet()) {
                            LinkedList<WfmTreeItem> employeesItems = items.get(departmentItem);
                            employeesPanel.addTreeItem(departmentItem, employeesItems);
                            employeeCount += employeesItems.size();
                        }
                        employeesPanel.expandTreeView();
                        isEmpty = employeeCount < 200;
                    } else {
                        isEmpty = true;
                    }
                }
            });
            offset += limit;
        }
    }

    private void onEmployeeChange() {
        if (employees.getSelectedItemID() != null) {
            LoadingPanel.loading(true);
            employeeID = employees.getSelectedItemID();
            approver.reloadApproverWidgets(RelationItem.TYPE_LEAVE_REQUEST, objectId, employeeID);
            balance.setText(wfmStrings.leaveDays() + ": 0.00");
            dateTime.dueDate.setDefaultFormat();
            availabilityService.getReasons(employeeID, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    GWT.log("", caught);
                }

                @Override
                public void success(SelectItem[] result) {
                    LoadingPanel.loading(false);
                    reason.clear();
                    reason.setItems(result);

                    availabilityService.getEmployee(employeeID, new AsyncCallback<EmployeeViewItem>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            GWT.log("", throwable);
                        }

                        @Override
                        public void onSuccess(EmployeeViewItem employeeViewItem) {
                            employeeStartDate = employeeViewItem.getStartDate().getNonConvertedDate();
                        }
                    });
                }
            });
            updateTimeSlot(true);
            refreshChart();
            calendarTabWidget.setEmployeeID(employeeID);
            calendarTabWidget.setCalendarOffAndAppDays();
            if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && "LR_TYPE_ANNUAL_LEAVE".equals(reason.getSelectedItem().getDescription())) {
                labourPeriodWidget.clearValues();
                labourPeriodWidget.removeStyleName(ERROR_FORM_STYLE);
                labourPeriodWidget.setEmployeeID(employeeID, reason.getSelectedItem().getDescription(), dateTime.startDate.getDate(), objectId);
                leavePeriodForm.setVisible(true);
            } else {
                leavePeriodForm.setVisible(false);
            }
        }
    }

    private void updateTimeSlot(boolean start) {
        AvailabilityService.App.get().getEmployeeTimeSlot(employeeID, new DateNonConvertable(start ? dateTime.getStartDate() : dateTime.getDueDate()), new AsyncCallback<TimeSlot>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log("", throwable);
            }

            @Override
            public void onSuccess(TimeSlot timeSlot) {
                timeslotStartTime = timeSlot.getStartHour() + ":" + timeSlot.getStartMin();
                timeslotEndTime = timeSlot.getEndHour() + ":" + timeSlot.getEndMin();
                dateTime.setStartTime(timeslotStartTime);
                dateTime.setEndTime(timeslotEndTime);
            }
        });
    }

    private void save(String status) {
        if (!validate(status)) {
            return;
        }
        enableButton(false);
        setLeaveRequestValues();

        Integer employeeId = employees != null ? employees.getSelectedItemID() : null;

        if (Constants.DRAFT.equals(status)) {
            leaveRequest.setStatusCode(Constants.DRAFT);
        } else if (approver.getFirstApproverLookUp() != null) {
            SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();

            if (leaveRequest.getBackupEmployee() != null && leaveRequest.getBackupEmployee().size() > 0) {
                BackupEmployeeItem firstBackUpEmp = leaveRequest.getBackupEmployee().get(0);
                if (firstBackUpEmp != null && firstBackUpEmp.getParentBackupEmployee() != null && firstBackUpEmp.getParentBackupEmployee().getExactEmployee() != null && Utils.getUserID().equals(firstBackUpEmp.getParentBackupEmployee().getExactEmployee().getId())) {
                    leaveRequest.setStatusCode(Constants.APPROVED);
                } else {
                    leaveRequest.setStatusCode(Constants.LR_STATUS_NOT_DEFINED);
                }
            } else {
                if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                    leaveRequest.setStatusCode(Constants.APPROVED);
                } else {
                    leaveRequest.setStatusCode(Constants.LR_STATUS_NOT_DEFINED);
                }
            }
        }
        if (objectId != null) {
            leaveRequest.setObjectID(objectId);
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && leaveRequest.getMultiLeaveValues() != null && leaveRequest.getMultiLeaveValues().size() > 0) {
            hasAccessInsertSickRequestsByPeriod(employeeId, leaveRequest, recalculate);
        } else {
            hasAccessInsertRequest(employeeId, leaveRequest, leaveRequest.getStartNonConverable(), leaveRequest.getEndNonConverable(), recalculate);
        }
    }

    private void hasAccessInsertSickRequestsByPeriod(Integer employeeId, NewLeaveRequest leaveRequest, boolean recalculate) {
        AvailabilityService.App.get().hasAccessInsertSickRequestsByPeriod(employeeId, leaveRequest, recalculate, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                enableButton(true);
                GWT.log("", throwable);
            }

            @Override
            public void onSuccess(String result) {
                if (Constants.HAS_THE_SAME_LR.equals(result)) {
                    enableButton(true);
                    Info.show(hrmsStrings.youHaveTheSameLeaveRequest(), Info.Type.WARNING);
                } else if (Constants.TRUE.equals(result)) {
                    insertLeaveRequest();
                } else {
                    enableButton(true);
                    Info.show(result, Info.Type.WARNING);
                }
            }
        });
    }

    private void hasAccessInsertRequest(Integer employeeID, NewLeaveRequest newLeaveRequest, DateNonConvertable startDate, DateNonConvertable endDate, boolean recalculate) {
        AvailabilityService.App.get().hasAccessInsertRequest(employeeID, newLeaveRequest, startDate, endDate, recalculate, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable caught) {
                enableButton(true);
                GWT.log("", caught);
            }

            @Override
            public void success(String result) {
                if (Constants.TRUE.equals(result)) {
                    if (reason.getSelectedItem() != null && !unauthorized_leave.equals(reason.getSelectedItem().getDescription()) && paid.isActive()) {
                        validateAllowanceLimit(newLeaveRequest);
                    } else {
                        insertLeaveRequest();
                    }
                } else if (Constants.HAS_THE_SAME_LR.equals(result)) {
                    enableButton(true);
                    Info.show(hrmsStrings.youHaveTheSameLeaveRequest(), Info.Type.WARNING);
                } else {
                    if (reason.getSelectedItem() != null && CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(result) && CustomFormConstants.LR_TYPE_SICK_LEAVE.equals(reason.getSelectedItem().getDescription()) && paid.isActive()) {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage("Are you sure want to return leave request duration to these days?");
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                insertLeaveRequest();
                            }
                        });
                        messageBox.open();
                    } else {
                        enableButton(true);
                        Info.show(result, Info.Type.WARNING);
                    }
                }
            }
        });
    }

    private void insertLeaveRequest() {
        LoadingPanel.loading(true);
        AvailabilityService.App.get().createLeaveRequest(leaveRequest, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false);

                enableButton(true);
                if (result == VALIDATION) {
                    Info.warn(hrmsStrings.probationPeriodNotPassedForThisEmployee());
                } else if (result.equals(Errors.THIS_NAME_IS_ALREADY_EXIST)) {
                    Info.show(hrmsStrings.thisLeaveRequestAlreadyExist(), Info.Type.WARNING);
                } else {
                    if (reason.getSelectedItem().getCategory() != null && !reason.getSelectedItem().getCategory().isEmpty()) {
                        Utils.openURLCommon(reason.getSelectedItem().getCategory());
                    }
                    closeTab();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVER_REQUEST_ADD, null, AddLeaveRequestView.this);
                }
            }
        });
    }


    private WidgetsMap getWidgets(ApproverItemMini employee) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final TextBox id = new TextBox();
        EmployeeLookUpWithCode backupEmployees = new EmployeeLookUpWithCode();
        backupEmployees.getSuggestBox().addSelectionHandler(selectionEvent -> {

            if (employees != null && employees.getSelectedItemID() == null) {
                backupEmployees.clear();
                Info.warn(wfmStrings.pleaseSelect() + wfmStrings.employee()); //todo localization
            } else if (employees != null && employees.getSelectedItemID() != null && backupEmployees.getSelectedItemID().equals(employees.getSelectedItemID())) {
                backupEmployees.clear();
                Info.warn("Backup Employee cannot be employee!!!"); //todo localization
            }

            backupEmployeeNavBox.getEmployeeFromLeave(employees.getSelectedItemID());
            WfmButton2 saveButton2 = (WfmButton2) getButton(SAVE_AND_CLOSE);
            String buttonName = wfmStrings.submit();

            if (saveButton2 != null) {
                List<WidgetsMap> allBackupEmployeeData = backupEmployeeTable != null ? backupEmployeeTable.getWidgetsMaps() : null;
                SelectItem item2 = approver != null && approver.getFirstApproverLookUp() != null ? approver.getFirstApproverLookUp().getSelectedItem() : null;

                EmployeeLookUpWithCode employeeLookUpWithCode = allBackupEmployeeData != null && allBackupEmployeeData.size() > 0 && allBackupEmployeeData.get(0) != null && allBackupEmployeeData.get(0).getWidget("employee") != null ? (EmployeeLookUpWithCode) allBackupEmployeeData.get(0).getWidget("employee") : null;

                if (employeeLookUpWithCode != null && employeeLookUpWithCode.getSelectedItemID() != null) {
                    if (Utils.getUserID().equals(employeeLookUpWithCode)) {
                        buttonName = wfmStrings.approveAndClose();
                    }
                } else if (item2 != null && item2.getId() != null && Utils.getUserID().equals(item2.getId())) {
                    buttonName = wfmStrings.approveAndClose();
                }
                addButton.setText(buttonName);
            }
        });
        id.setVisible(false);
        if (employee != null) {
            backupEmployees.setSelected(employee.getExactEmployee().getId());
            id.setText(employee.getObjectID().toString());
        }
        widgetsMap.add("id", id);
        widgetsMap.add("employee", backupEmployees);
        return widgetsMap;
    }


    private void getDueDate() {
        Integer overAllLeaveDays = labourPeriodWidget.getLeaveDays();
        availabilityService.getEndDate(employeeID, dateTime.getStartDate(), Double.valueOf(overAllLeaveDays), reason.getSelectedItem().getDescription(), new AsyncCallback<Date>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.show("There are no dates in attendance table. Please check!", Info.Type.WARNING);
                refreshCalendar();
            }

            @Override
            public void onSuccess(Date date) {
                if (date != null) {
                    labourPeriodWidget.removeStyleName(ERROR_FORM_STYLE);
                    dateTime.dueDate.setDate(date);
                    backupEmployeeNavBox.leaveStartDate = dateTime.getStartDate();
                    backupEmployeeNavBox.leaveDueDate = dateTime.getDueDate();
                    refreshCalendar();
                } else {
                    Info.show("There are no dates in attendance table. Please check!", Info.Type.WARNING);
                }
            }
        });
    }

    private void refreshCalendar() {
        if (dateTime.getStartDate() != null) {
            calendarTabWidget.setEmployeeID(employeeID);
            calendarTabWidget.setReasonCode(reason.getSelectedItem().getDescription());
            calendarTabWidget.setCalendarOffAndAppDays();
            calendarTabWidget.getCalendarView().setLrPeriod1(dateTime.getStartDate());
            calendarTabWidget.getCalendarView().setLrPeriod2(dateTime.getDueDate());
            calendarTabWidget.getCalendarView().setIncludeDaysOff(includeDayOffs);
            calendarTabWidget.getCalendarView().changeMonth(dateTime.getStartDate());
        }
    }

    private void setLeaveRequestValues() {
        leaveRequest = new NewLeaveRequest();
        if (objectId != null) {
            leaveRequest.setObjectID(objectId);
        }
        if (addEmployee) {
            leaveRequest.setEmployee(employees.getSelectedItem().getId());
        }
        leaveRequest.setAllDay(!hourly);
        leaveRequest.setRecalculate(recalculate);
        leaveRequest.setApprovers(approver.getChosenApprovers());

        ArrayList<BackupEmployeeItem> allBackupEmployeeData = backupEmployeeNavBox.getAllBackupEmployeeData();
        if (allBackupEmployeeData != null && allBackupEmployeeData.size() > 0) {
            leaveRequest.setBackupEmployee(allBackupEmployeeData);
        }
        leaveRequest.setNumberData(leaveRequestCode.getNumberData(true));
        leaveRequest.setLeaveRequestCode(leaveRequestCode.getNumberData(true).getNumberString());

        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
        if (approver.getApproversSize() == 1 && item != null && item.getId() != null && Utils.getUserID().equals(item.getId()) && (leaveRequest.getBackupEmployee() == null || leaveRequest.getBackupEmployee() != null && leaveRequest.getBackupEmployee().size() == 0)) {
            leaveRequest.setSelfApprover(true);
        }

        String[] start = dateTime.getStartTime().time.split(":");
        String[] endTime = dateTime.getEndTime().time.split(":");
        leaveRequest.setStartHour(Integer.parseInt(start[0]));
        leaveRequest.setStartMinut(Integer.parseInt(start[1]));
        leaveRequest.setEndHour(Integer.parseInt(endTime[0]));
        leaveRequest.setEndMinut(Integer.parseInt(endTime[1]));

        leaveRequest.setStartNonConverable(new DateNonConvertable(dateTime.getStartDate()));
        leaveRequest.setEndNonConverable(new DateNonConvertable(dateTime.getDueDate()));
        leaveRequest.setAttachments(footerUploadPanel.getAttachedFiles());
        leaveRequest.setType(paid.isActive() ? "ST_PAID" : "NON_PAID");
        leaveRequest.setTakeByMoney(!takenLaveBy.isActive());
        leaveRequest.setFrom(LayoutRPC.LEAVE_REQUEST_FORM);
        if (reason.getSelectedItem() != null) {
            leaveRequest.setReasonId(reason.getSelectedItem().getId());
        }
        if (description.getText() != null) {
            leaveRequest.setDescription(description.getText());
        }
        ArrayList<Integer> employeeIds = new ArrayList<>();
        if (employeesPanel != null && employeesPanel.getTreeSelect() != null && employeesPanel.getTreeSelect().getCheckedItems() != null && employeesPanel.getTreeSelect().getCheckedItems().length > 0) {
            WfmTreeItem[] checkedItems = employeesPanel.getTreeSelect().getCheckedItems();
            for (WfmTreeItem wfmTreeItem : checkedItems) {
                Integer employeeID = wfmTreeItem.getId();
                employeeIds.add(employeeID);
            }
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION)) {
            leaveRequest.setPeriodList(labourPeriodWidget.getValues());
            leaveRequest.setMultiLeaveValues(labourPeriodWidget.getMultiLeaveValues());
            leaveRequest.setUsedExperienceDays(labourPeriodWidget.getUsedExperienceDays());
        }
        leaveRequest.setEmployeeIds(employeeIds);
        leaveRequest.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
    }


    private void validateAllowanceLimit(NewLeaveRequest leaveRequest) {
        AvailabilityService.App.get().validateAllowanceLimit(leaveRequest, new AbstractAsyncCallback<NewLeaveRequest>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                enableButton(true);
            }

            @Override
            public void success(NewLeaveRequest result) {
                LoadingPanel.loading(false);
                if (result.getValid()) {
                    insertLeaveRequest();
                } else {
                    enableButton(true);
                    if (result.getErrorMessage() != null && result.getErrorMessage().length() > 0) {
                        Info.warn(result.getErrorMessage(), 5000);
                    } else {
                        String reasonName = reason.isSomethingSelected() ? reason.getSelectedItem().getName() : "";
                        Info.warn(availabilityMessages.leaveRequestLimitExceeded(
                                reasonName,
                                selectedYear + "",
                                result.getDay()
                        ), 5000);
                    }
                }
            }
        });
    }

    private boolean validate(String status) {
        int errors = 0;
        clearErrorStyle();
        if (formPropertyMap != null && formPropertyMap.get(EMPLOYEE) != null && formPropertyMap.get(EMPLOYEE).isRequired()) {
            errors += markAsError(employees, !Validation.validateLookUpRequired(employees));
        }

        if (employees.getSelectedItem() == null) {
            errors += markAsError(employees, !Validation.validateLookUpRequired(employees));
        }

        boolean isBackupEmployeeValid = true;
        if (formPropertyMap != null && formPropertyMap.get(BACKUP_EMPLOYEE) != null && formPropertyMap.get(BACKUP_EMPLOYEE).isRequired() && !validateBEtoEmpty()) {
            errors += markAsError(backupEmployeeTable, true);
            isBackupEmployeeValid = false;
        }

        if (!validateBackupEmployee()) {
            return false;
        }

        if (Constants.DRAFT.equals(status)) {
            if (employees.getSelectedItem() == null) {
                markAsError(employees, !Validation.validateLookUpRequired(employees));
                return false;
            }
            if (formPropertyMap != null && formPropertyMap.get(DATE_PERIOD) != null && formPropertyMap.get(DATE_PERIOD).isRequired()) {
                if (dateTime.getStartDate() == null || dateTime.getDueDate() == null) {
                    markAsError(DATE_PERIOD, dateTime.startDate, dateTime.getStartDate() == null);
                    markAsError(DATE_PERIOD, dateTime.dueDate, dateTime.getDueDate() == null);
                    return false;
                }
            }
            if (!isBackupEmployeeValid) {
                return false;
            }
            return !Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) || !"LR_TYPE_ANNUAL_LEAVE".equals(reason.getSelectedItem().getDescription()) || labourPeriodWidget.validate();
        }

        if (formPropertyMap != null && formPropertyMap.get(APPROVER) != null && formPropertyMap.get(APPROVER).isRequired() && !approver.isValid()) {
            errors++;
        }

        if (formPropertyMap != null && formPropertyMap.get(REASON) != null && formPropertyMap.get(REASON).isRequired() && reason.getSelectedItem() == null) {
            errors += markAsError(reason, !Validation.validateListBoxRequired(reason, new HTML(), wfmStrings.pleaseSpecifyReason()));
        }

        if (formPropertyMap != null && formPropertyMap.get(DATE_PERIOD) != null && formPropertyMap.get(DATE_PERIOD).isRequired()) {
            errors += markAsError(DATE_PERIOD, dateTime.startDate, dateTime.getStartDate() == null);
            errors += markAsError(DATE_PERIOD, dateTime.dueDate, dateTime.getDueDate() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null && formPropertyMap.get(DESCRIPTION).isRequired()) {
            errors += markAsError(DESCRIPTION, description, !Validation.validateTextAreaRequired(description));

        }
        if ("LR_TYPE_ANNUAL_LEAVE".equals(reason.getSelectedItem().getDescription())) {
            if (objectId != null && DateUtil.compare(employeeStartDate, dateTime.getStartDate())) {
                dateTime.startDate.setStyleName("x-form-invalid");
                errors++;
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && !labourPeriodWidget.validate()) {
                labourPeriodWidget.setStyleName("x-form-invalid");
                errors++;
            }
        }

        if (dateTime.getStartDate() != null && dateTime.getDueDate() != null) {
            errors += markAsError(DATE_PERIOD, dateTime.startDate, !Validation.validateDateOrder(dateTime.getStartDate(), dateTime.getDueDate(), null, !hourly));

            String[] start = dateTime.getStartTime().time.split(":");
            String[] end = dateTime.getEndTime().time.split(":");

            Integer startHour = null;
            Integer startMin = null;
            try {
                startHour = Integer.parseInt(start[0]);
                startMin = Integer.parseInt(start[1]);
            } catch (NumberFormatException e) {
                dateTime.startTime.setStyleName("x-form-invalid");
                errors++;
            }

            Integer endHour = null;
            Integer endMin = null;
            try {
                endHour = Integer.parseInt(end[0]);
                endMin = Integer.parseInt(end[1]);
            } catch (NumberFormatException e) {
                dateTime.endTime.setStyleName("x-form-invalid");
                errors++;
            }

            if (!isValidateHour(startHour) || !isValidateMin(startMin)) {
                dateTime.startTime.setStyleName("x-form-invalid");
                errors++;
            }
            if (!isValidateHour(endHour) || !isValidateMin(endMin)) {
                dateTime.endTime.setStyleName("x-form-invalid");
                errors++;
            }
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        if (Utils.isAttendanceLocked() && dateTime.getStartDate() != null && com.edatasite.workforce.gwt.core.client.DateUtils.getTransactionLockDate().after(dateTime.getStartDate())) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.leaveRequest(), Utils.getTransactionLockDate()), Info.Type.WARNING);
        }

        return true;
    }

    private boolean isValidateHour(Integer hour) {
        return hour != null && (hour >= 0 && hour <= 23);
    }

    private boolean isValidateMin(Integer min) {
        return min != null && (min >= 0 && min <= 59);
    }


    private Long getTimeAsLongFromString(String dateAsStr, String hourAsStr, String minutAsStr) {
        return Long.parseLong(dateAsStr) + (Long.parseLong(hourAsStr) * 60 + Long.parseLong(minutAsStr)) * 60 * 1000;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYEES) != null && formPropertyMap.get(CustomFormConstants.EMPLOYEES).getDefaultValue() != null) {
            employees.setSelected(formPropertyMap.get(CustomFormConstants.EMPLOYEES).getSelectedId(), formPropertyMap.get(CustomFormConstants.EMPLOYEES).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(REASON) != null && formPropertyMap.get(REASON).getDefaultValue() != null) {
            reason.setSelected(new SelectItem(formPropertyMap.get(REASON).getSelectedId(), formPropertyMap.get(REASON).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null && formPropertyMap.get(DESCRIPTION).getDefaultValue() != null) {
            description.setText(formPropertyMap.get(DESCRIPTION).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(TAKE_LIVE_TYPE) != null && formPropertyMap.get(TAKE_LIVE_TYPE).getDefaultValue() != null) {
            takenLaveBy.setActive(Constants.DAY.equals(formPropertyMap.get(TAKE_LIVE_TYPE).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(TYPE) != null && formPropertyMap.get(TYPE).getDefaultValue() != null) {
            paid.setActive("ST_PAID".equals(formPropertyMap.get(TYPE).getDefaultValue()));
        }
    }

    private boolean validateBackupEmployee() {
        boolean validated = true;
        if (backupEmployeeTable != null) {
            for (HashMap<String, Widget> widget : backupEmployeeTable.getWidgets()) {
                EmployeeLookUpWithCode employee = (EmployeeLookUpWithCode) widget.get("employee");
                if (employee != null && employee.getSelectedItemID() != null) {
                    if (!backupEmployeeNavBox.validatePeriodDate()) {
                        validated = false;
                    }
                }
            }
        }
        return validated;
    }

    private boolean validateBEtoEmpty() { //validate backup employee to empty
        boolean validated = false;
        if (backupEmployeeTable != null) {
            for (HashMap<String, Widget> widget : backupEmployeeTable.getWidgets()) {
                EmployeeLookUpWithCode employee = (EmployeeLookUpWithCode) widget.get("employee");
                if (employee != null && employee.getSelectedItemID() != null) {
                    validated = true;
                } else if (employee != null && employee.getSelectedItemID() == null) {
                    validated = false;
                    break;
                }
            }
        }
        return validated;
    }


    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
