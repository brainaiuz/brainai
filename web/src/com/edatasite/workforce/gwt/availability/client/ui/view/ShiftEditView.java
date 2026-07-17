package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.BrigadaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpForShift;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TimeSlotShortNameLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.factory.HrmsSinksContainerFactory;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItems;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftTeamsItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;
import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.BRIGADA_ID;

public class ShiftEditView extends CustomForm2 implements Colapse {

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private Integer objectId;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private ChosenApproversWidget approvers;
    private EditableTable editableTable;
    private EditableTable brigadaEmployeesTable;
    private ShiftItem data;
    private DatePicker monthPicker;
    private DatePicker endDatePicker;
    private FlexTable flexTable = new FlexTable();
    private FlexTable backupManagerFlexTable = new FlexTable();
    private Div brigadaTableContainer;
    private Div shiftTableContainer;
    private WfmButton2 draftButton;
    private WfmButton2 approveButton;
    private WfmButton2 submitButton;
    private SplitButton printPdfSplitButton;
    private int monthMaxDay = 0;
    private FormHasCustomField customFieldUtil;
    private HashMap<Integer, ArrayList<SelectItem>> teamsEmployees;
    private NumberData numberData;
    private Numbering numbering;
    private TextBox manager;
    private TextBox backUpManager;
    private DataListBox selectItemListBox;
    private MultiSelectEmployeeLookUp employeeLookUp;
    private AtomicBoolean firstClick = new AtomicBoolean(true);
    private DepartmentLookUp departmentLookUp;
    private BrigadaLookUp brigadaLookUp;
    private boolean isCustom = false;
    private InputGroup monthWidget;
    String periodType = "month";

    public ShiftEditView() {
        super("addShift", "shiftAdd ");
    }


    public ShiftEditView(Integer objectId) {
        super("addShift", "shiftAdd ");
        this.objectId = objectId;
    }

    public ShiftEditView(String periodType, boolean byPeriod) {
        super("addShift", "shiftAdd ");
        this.isCustom = true;
        this.periodType = periodType;
    }

    public ShiftEditView(Integer objectId, String periodType, boolean byPeriod) {
        super("addShift", "shiftAdd ");
        this.objectId = objectId;
        this.isCustom = true;
        this.periodType = periodType;
    }


    @Override
    protected void registerFields() {

        monthPicker = new DatePicker();
        monthPicker.setWidth("200px");
        monthPicker.setOnlyMonthFormat(!periodType.equals("week"));
        if ("week".equals(periodType)) {
            monthPicker.setOnlyYearFormat(true);
            monthPicker.setDateTimeFormat(DateTimeFormat.getFormat("yyyy"));
        } else {
            monthPicker.setOnlyMonthFormat(true);
            monthPicker.setDateTimeFormat(DateTimeFormat.getFormat("MMM yyyy"));
        }
        monthPicker.addChangeHandler(ch -> {
            if (monthPicker.getDate() != null && firstClick.get()) {
                firstClick.set(false);
                if (!"week".equals(periodType) && DateUtil.compare(new Date(), monthPicker.getDate()) && !DateUtil.equalByMonths(new Date(), monthPicker.getDate())) {
                    final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo,
                            hrmsStrings.selectedPeriodHasPassed(), new CloseHandler() {
                        @Override
                        public void onCancel() {
                            LoadingPanel.loading(false);
                            monthPicker.setDate(null);
                        }

                        @Override
                        public void onSubmit() {
                            onChangePeriod();
                        }
                    });
                    wfmMessageBox.setTitle(wfmStrings.warning());
                    wfmMessageBox.open();
                } else {
                    onChangePeriod();
                }
            }
        });

        TextBox name = new TextBox();
        name.ensureDebugId("employee_firstName");
        name.addStyleName("employee_" + "first_name");
        name.setWidth("200px");

        numbering = new Numbering();
        numbering.setEnabled(false);
        if (objectId == null) {
            generateNewNumber();
        }

        brigadaEmployeesTable = new EditableTable(getBrigadaColumns(), false, false);
        brigadaTableContainer = new Div();
        brigadaTableContainer.setStyleName("brigada_employees_container");
        brigadaTableContainer.add(brigadaEmployeesTable);

        approvers = new ChosenApproversWidget(RelationItem.TYPE_SHIFT, objectId);
        approvers.setWidth("200px");

        manager = new TextBox();
        manager.ensureDebugId("manager");
        manager.addStyleName(DEFAULT_WIDTH);
        manager.setEnabled(false);

        backUpManager = new TextBox();
        backUpManager.ensureDebugId("backUpManager");
        backUpManager.addStyleName(DEFAULT_WIDTH);
        backUpManager.setEnabled(false);

        selectItemListBox = new DataListBox();
        selectItemListBox.setWithoutNullLabel(true);
        selectItemListBox.setItems(getSelectItemTypes());
        if ("week".equals(periodType)) {
            selectItemListBox.setSelected(new SelectItem(LookUpConstants.BRIGADA_ID, wfmStrings.team()));
            selectItemListBox.setEnabled(false);
        } else {
            selectItemListBox.setSelected(new SelectItem(LookUpConstants.EMPLOYEE_ID, wfmStrings.duty()));
        }
        selectItemListBox.setWidth("200px");
        selectItemListBox.addValueChangeHandler(e -> {
            editableTable.removeFromParent();
            shiftTableContainer.remove(editableTable);
            editableTable = null;
            editableTable = new EditableTable(getColumns(), true, true);
            editableTable.setListener(new EditableTableListener() {
                @Override
                public void addRow() {
                    editableTable.addRow(getWidgets(selectItemListBox.getSelectedId()));

                }

                @Override
                public void removeRow() {
                    addBrigadaEmployees();
                }
            });
            editableTable.addStyleName("brigada_table");
            //Sticky table header need this
            editableTable.addStyleName("tbl-StickyHead tbl-fCellHide");

            shiftTableContainer.add(editableTable);
            editableTable.removeAllRows();
            brigadaEmployeesTable.removeAllRows();
            editableTable.addRow(getWidgets(e.getValue().getId()));
        });

        employeeLookUp = new MultiSelectEmployeeLookUp();
        employeeLookUp.getFilterParametrs().setType(LookUpConstants.SHIFT_ID);
        employeeLookUp.addStyleName(DEFAULT_WIDTH);

        departmentLookUp = new DepartmentLookUp();
        departmentLookUp.addStyleName(DEFAULT_WIDTH);
        departmentLookUp.setEnabled(false);

        departmentLookUp.getSuggestBox().addSelectionHandler(event -> {
            selectItemListBox.setSelected(new SelectItem(LookUpConstants.EMPLOYEE_ID, wfmStrings.duty()));
            shiftTableContainer.remove(editableTable);
            editableTable = null;
            editableTable = new EditableTable(getColumns(), true, true);
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            String startDate = DateUtils.getDateAndTimeFormatWithDash(DateUtil.getMonthFirstDay(monthPicker.getDate()));
            String endDate = DateUtils.getDateAndTimeFormatWithDash(DateUtil.getMonthLastDateWithTime(monthPicker.getDate()));
            filterParameter.setStartDateNC(startDate);
            filterParameter.setEndDateNC(endDate);
            filterParameter.setDepartmentIds(departmentLookUp.getSelectedItemID().toString());
            filterParameter.setFromShift(true);
            HrmsService.App.get().getDepartmentEmployeesSelectItem(filterParameter,monthMaxDay, new AsyncCallback<EmployeeAttendanceReport>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(EmployeeAttendanceReport employeeAttendanceReport) {
                    editableTable.removeAllRows();
                     setEmployeeeToTableByDepartment(employeeAttendanceReport);
                }
            });
            editableTable.addStyleName("brigada_table");
            //Sticky table header need this
            editableTable.addStyleName("tbl-StickyHead tbl-fCellHide");
            shiftTableContainer.add(editableTable);
            editableTable.removeAllRows();
            brigadaEmployeesTable.removeAllRows();
            editableTable.addRow(getWidgets(LookUpConstants.EMPLOYEE_ID));
        });

        brigadaLookUp = new BrigadaLookUp(null);
        brigadaLookUp.addStyleName(DEFAULT_WIDTH);
        brigadaLookUp.setEnabled(false);

        brigadaLookUp.getSuggestBox().addSelectionHandler(event -> {
            shiftTableContainer.remove(editableTable);
            editableTable = null;
            editableTable = new EditableTable(getColumns(), true, true);
            LinkedList<Integer> groupIds = new LinkedList<>();
            groupIds.add(brigadaLookUp.getSelectedItemID());
            HrmsService.App.get().getProjectEmployeesSelectItem(groupIds,monthPicker.getDate(),BRIGADA_ID,new AsyncCallback<LinkedHashMap<String, List<EmployeeReport>>> () {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(LinkedHashMap<String, List<EmployeeReport>> employeeAttendanceReports) {
                    setEmployeeeToTableByBrigada(employeeAttendanceReports);
                }
            });

            editableTable.addStyleName("brigada_table");
            editableTable.addStyleName("tbl-StickyHead tbl-fCellHide");
            shiftTableContainer.add(editableTable);
            editableTable.removeAllRows();
            brigadaEmployeesTable.removeAllRows();
            editableTable.addRow(getWidgets(LookUpConstants.EMPLOYEE_ID));
        });


        this.getCustomFieldUtil().drawCustomFields(this, this.objectId);
        setTeamsEmployees();
        drawFields();
        show();
    }


    private InputGroup inputGroupPrependedRadioCheckbox() {
        isCustom = true;
        monthPicker = new DatePicker();
        monthPicker.setEnabled(true);
        monthPicker.setOnlyMonthFormat(false);
        monthPicker.setDateTimeFormat(DateTimeFormat.getFormat(Utils.getShortDateFormat()));
        monthPicker.setTitle(wfmStrings.startDate());

        endDatePicker = new DatePicker();
        endDatePicker.setEnabled(true);
        endDatePicker.setDateTimeFormat(DateTimeFormat.getFormat(Utils.getShortDateFormat()));
        endDatePicker.setOnlyMonthFormat(false);
        endDatePicker.setTitle(wfmStrings.endDate());

        endDatePicker.addChangeHandler(changeEvent -> {
            endDatePicker.setEnabled(false);
            monthPicker.setEnabled(false);
            if (monthPicker.getDate() == null) {
                Validation.validateDate(monthPicker);
                Info.warn(wfmStrings.fillRequiredField());
            } else {
                onChangePeriod();
            }
        });

        InputGroup period = new InputGroup(monthPicker, endDatePicker);

        return period;
    }

    private void onChangePeriod() {
        if ("custom".equals(periodType)) {
            monthMaxDay = DateUtil.countDays(monthPicker.getDate(),DateUtil.getMonthLastDate(monthPicker.getDate()));
        } else if ("week".equals(periodType)) {
            monthMaxDay = getISOWeeksInYear(monthPicker.getDate().getYear() + 1900);
        } else {
            monthMaxDay = DateUtil.countDays(monthPicker.getDate());
        }
        departmentLookUp.setEnabled(true);
        brigadaLookUp.setEnabled(true);
        editableTable = new EditableTable(getColumns(), true, true);
        editableTable.addRow(getWidgets(selectItemListBox.getSelectedId()));
        editableTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                editableTable.addRow(getWidgets(selectItemListBox.getSelectedId()));

            }

            @Override
            public void removeRow() {
                addBrigadaEmployees();
            }
        });

        editableTable.addStyleName("brigada_table");
        editableTable.addStyleName("tbl-StickyHead tbl-fCellHide");
        shiftTableContainer = new Div();
        shiftTableContainer.add(editableTable);
        monthPicker.setEnabled(false);

        addField(SHIFT, shiftTableContainer, null);
    }


    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {

    }

    private SelectItem[] getSelectItemTypes() {
        return new SelectItem[]{
                new SelectItem(LookUpConstants.BRIGADA_ID, wfmStrings.team()),
                new SelectItem(LookUpConstants.EMPLOYEE_ID, wfmStrings.duty()),
                new SelectItem(LookUpConstants.OVERTIME, wfmStrings.overtime())
        };
    }

    protected ColumnConfig[] getColumns() {
        Integer type = objectId == null ? selectItemListBox.getSelectedId() : data.getLookUpType();
        ColumnConfig[] columns = new ColumnConfig[monthMaxDay + 1];
        String lookupName = null;
        if (type == null || type.equals(LookUpConstants.BRIGADA_ID)) {
            lookupName = wfmStrings.team();
        } else if (type.equals(LookUpConstants.EMPLOYEE_ID)) {
            lookupName = wfmStrings.duty();
        } else if (type.equals(LookUpConstants.OVERTIME)) {
            lookupName = wfmStrings.overtime();
        }
        columns[0] = new ColumnConfig(LookUpCell.class, "group","<span class=\"frame_affix_top\">"+ lookupName+"</span>", 200, false);
        DateTimeFormat weekDayFormat = DateTimeFormat.getFormat("E");
        DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMM");
        DateTimeFormat dayFormat = DateTimeFormat.getFormat("dd");
        if ("week".equals(periodType)) {
            for (int i = 1; i <= monthMaxDay; i++) {
                columns[i] = new ColumnConfig(LookUpCell.class, "" + i, "W" + i, 50, false);
            }
        } else if (!isCustom) {
            for (int i = 1; i <= monthMaxDay; i++) {
                Date tempDate = new Date(monthPicker.getDate().getTime());
                tempDate.setDate(i);
                String monthString = monthFormat.format(tempDate);
                String dateString = dayFormat.format(tempDate);
                String weekDay = weekDayFormat.format(tempDate);

                monthString = monthString.substring(0, 3);
                String span = "<span class='attendance-report-header-weekday-frame_affix_top'" + (weekDay.equals("Sat") || weekDay.equals("Sun") ? " style = \"color:red\"" : "") + ">";
                columns[i] = new ColumnConfig(LookUpCell.class,
                        "" + i,
                        monthString + ".-" + dateString + "</br>" + span + weekDay + "</span>", 50, false);
            }
        } else {
            Date tempDate = new Date(monthPicker.getDate().getTime());
            Date endDate = new Date(endDatePicker.getDate().getTime());
            int counter = 1;
            while (!tempDate.after(endDate)) {
                String monthString = monthFormat.format(tempDate);
                String dateString = dayFormat.format(tempDate);
                String weekDay = weekDayFormat.format(tempDate);

                String span = "<span class='attendance-report-header-weekday-frame_affix_top'" + (weekDay.equals("Sat") || weekDay.equals("Sun") ? " style = \"color:red\"" : "") + ">";
                columns[counter] = new ColumnConfig(LookUpCell.class,
                        "" + counter,
                        monthString + ".-" + dateString + "</br>" + span + weekDay + "</span>", 50, false);
                counter++;

                tempDate.setDate(tempDate.getDate() + 1);
            }
        }

        return columns;
    }

    protected ColumnConfig[] getBrigadaColumns() {
        ArrayList<ColumnConfig> columnConfigs = new ArrayList<>();
        ColumnConfig columnConfig1 = new ColumnConfig(LookUpCell.class, "team", "<b>" + wfmStrings.team() + "</b>", 12);
        ColumnConfig columnConfig2 = new ColumnConfig(CustomCell.class, "employeeCode", "<b>" + wfmStrings.employeeCode() + "</b>", 12);
        ColumnConfig columnConfig3 = new ColumnConfig(CustomCell.class, "fullName", "<b>" + wfmStrings.fullName() + "</b>", 25);
        ColumnConfig columnConfig4 = new ColumnConfig(CustomCell.class, "positon", "<b>" + wfmStrings.position() + "</b>", 25);
        ColumnConfig columnConfig5 = new ColumnConfig(CustomCell.class, "department", "<b>" + wfmStrings.department() + "</b>", 25);
        ColumnConfig columnConfig6 = new ColumnConfig(CustomCell.class, "label", "<b>" + wfmStrings.note() + "</b>", 12);

        columnConfig1.setForceWidthInPercent(true);
        columnConfig2.setForceWidthInPercent(true);
        columnConfig3.setForceWidthInPercent(true);
        columnConfig4.setForceWidthInPercent(true);
        columnConfig5.setForceWidthInPercent(true);
        columnConfig6.setForceWidthInPercent(true);

        columnConfigs.add(columnConfig1);
        columnConfigs.add(columnConfig2);
        columnConfigs.add(columnConfig3);
        columnConfigs.add(columnConfig4);
        columnConfigs.add(columnConfig5);
        columnConfigs.add(columnConfig6);

        return columnConfigs.toArray(new ColumnConfig[]{});
    }


    protected Widget[] getWidgets(Integer lookupType) {
        Widget[] rowWidgets = new Widget[getColumns().length];
        LookUp projectLookUp = lookupType == null || lookupType.equals(LookUpConstants.BRIGADA_ID) ? new BrigadaLookUp(LookUpConstants.PROJECT) : new EmployeeLookUpForShift();
        projectLookUp.getSuggestBox().addSelectionHandler(t -> {
            addBrigadaEmployees();
        });

        rowWidgets[0] = projectLookUp;
        Date date = monthPicker.getDate();
        if ("week".equals(periodType)) {
            int year = date.getYear() + 1900;
            for (int i = 1; i <= monthMaxDay; i++) {
                rowWidgets[i] = new TimeSlotShortNameLookUp(getFirstDayOfISOWeek(year, i));
                TimeSlotShortNameLookUp rowWidget = (TimeSlotShortNameLookUp) rowWidgets[i];
                int finalI = i;
                rowWidget.getSuggestBox().addSelectionHandler(event -> {
                    popupForTimeSlot(rowWidget, finalI);
                });
            }
        } else if (!isCustom) {
            for (int i = 1; i <= monthMaxDay; i++) {
                rowWidgets[i] = new TimeSlotShortNameLookUp(new Date(date.getYear(), date.getMonth(), i));
                TimeSlotShortNameLookUp rowWidget = (TimeSlotShortNameLookUp) rowWidgets[i];
                int finalI = i;
                rowWidget.getSuggestBox().addSelectionHandler(event -> {
                    popupForTimeSlot(rowWidget, finalI);
                });
            }
        } else {
            int startDate = monthPicker.getDate().getDate();
            int endDate = DateUtil.getMonthLastDate(monthPicker.getDate()).getDate();
            int counter = 1;
            for (int i = startDate; i <= endDate; i++) {
                rowWidgets[counter] = new TimeSlotShortNameLookUp(new Date(date.getYear(), date.getMonth(), i));
                TimeSlotShortNameLookUp rowWidget = (TimeSlotShortNameLookUp) rowWidgets[counter];
                int finalI = counter;
                rowWidget.getSuggestBox().addSelectionHandler(event -> {
                    popupForTimeSlot(rowWidget, finalI);
                });
            }
        }


        return rowWidgets;
    }

    protected void setEmployeeeToTableByBrigada(LinkedHashMap<String, List<EmployeeReport>> employeeAttendanceReports) {
        editableTable.removeAllRows();
        Date date = monthPicker.getDate();
            for (String s : employeeAttendanceReports.keySet()) {
                for (EmployeeReport employee : employeeAttendanceReports.get(s)) {
                    Widget[] rowWidgets = new Widget[getColumns().length];
                    LookUp employeeLookup = new EmployeeLookUpForShift();
                    employeeLookup.setSelected(new SelectItem(employee.getId(), employee.getName()));
                    rowWidgets[0] = employeeLookup;
                    int counter = 1;
                    if ("week".equals(periodType)) {
                        int year = date.getYear() + 1900;
                        for (int i = 1; i <= monthMaxDay; i++) {
                            rowWidgets[counter] = new TimeSlotShortNameLookUp(getFirstDayOfISOWeek(year, i));
                            TimeSlotShortNameLookUp rowWidget = (TimeSlotShortNameLookUp) rowWidgets[counter];
                            int finalI = counter;
                            rowWidget.getSuggestBox().addSelectionHandler(event -> {
                                popupForTimeSlot(rowWidget, finalI);
                            });
                            counter++;
                        }
                    } else if (!isCustom) {
                        int startDate = monthPicker.getDate().getDate();
                        int endDate = DateUtil.getMonthLastDate(monthPicker.getDate()).getDate();
                        for (int i = startDate; i <= endDate; i++) {
                            rowWidgets[counter] = new TimeSlotShortNameLookUp(new Date(date.getYear(), date.getMonth(), i));
                            TimeSlotShortNameLookUp rowWidget = (TimeSlotShortNameLookUp) rowWidgets[counter];
                            int finalI = counter;
                            rowWidget.getSuggestBox().addSelectionHandler(event -> {
                                popupForTimeSlot(rowWidget, finalI);
                            });
                            counter++;
                        }
                    } else {
                        Date tempDate = new Date(monthPicker.getDate().getTime());
                        Date endDate1 = endDatePicker.getDate();
                        while (!tempDate.after(endDate1)) {
                            rowWidgets[counter] = new TimeSlotShortNameLookUp(new Date(tempDate.getTime()));
                            TimeSlotShortNameLookUp rowWidget = (TimeSlotShortNameLookUp) rowWidgets[counter];
                            rowWidget.hideIconAndEnableFocusFetch();
                            int finalI = counter;
                            rowWidget.getSuggestBox().addSelectionHandler(event -> {
                                popupForTimeSlot(rowWidget, finalI);
                            });
                            counter++;

                            tempDate.setDate(tempDate.getDate() + 1);
                        }
                    }

                    editableTable.addRow(rowWidgets);
                }
            }
    }

    protected void setEmployeeeToTableByDepartment(EmployeeAttendanceReport employees) {
        editableTable.removeAllRows();
        HashMap<String, ReasonItem> leaveTypes = employees.getLeaveTypes();
        Date date = monthPicker.getDate();
        for (EmployeeReport employee : employees.getEmployeeReports()) {
            Widget[] rowWidgets = new Widget[getColumns().length];
            LookUp employeeLookup = new EmployeeLookUpForShift();
            employeeLookup.setSelected(new SelectItem(employee.getId(), employee.getName()));
            rowWidgets[0] = employeeLookup;
            int[] al = employee.getAl();
            if ("week".equals(periodType)) {
                int year = date.getYear() + 1900;
                for (int i = 1; i <= monthMaxDay; i++) {
                    rowWidgets[i] = new TimeSlotShortNameLookUp(getFirstDayOfISOWeek(year, i));
                    TimeSlotShortNameLookUp rowWidget = (TimeSlotShortNameLookUp) rowWidgets[i];
                    int finalI = i;
                    rowWidget.getSuggestBox().addSelectionHandler(event -> {
                        popupForTimeSlot(rowWidget, finalI);
                    });
                }
            } else if (!isCustom) {
                for (int i = 1; i <= monthMaxDay; i++) {
                    rowWidgets[i] = new TimeSlotShortNameLookUp(new Date(date.getYear(), date.getMonth(), i));
                    TimeSlotShortNameLookUp rowWidget = (TimeSlotShortNameLookUp) rowWidgets[i];
                    int finalI = i;
                    rowWidget.getSuggestBox().addSelectionHandler(event -> {
                        popupForTimeSlot(rowWidget, finalI);
                    });
                    ReasonItem reasonItem = leaveTypes.get(employee.getLeaveCodes()[i]);
                    if ((al[i] == 1 || al[i] == 4 || al[i] == 2) && reasonItem != null && !reasonItem.isMarkAsDraft()) {
                        rowWidget.setEnabled(false);
                        rowWidget.setSelected(new SelectItem(999,reasonItem.getShortName()));
                    }

                }
            } else {
                Date tempDate = new Date(monthPicker.getDate().getTime());
                Date endDate1 = endDatePicker.getDate();
                int counter = 1;
                while (!tempDate.after(endDate1)) {
                    rowWidgets[counter] = new TimeSlotShortNameLookUp(new Date(tempDate.getTime()));
                    TimeSlotShortNameLookUp rowWidget = (TimeSlotShortNameLookUp) rowWidgets[counter];
                    int finalI = counter;
                    rowWidget.getSuggestBox().addSelectionHandler(event -> {
                        popupForTimeSlot(rowWidget, finalI);
                    });
                    ReasonItem reasonItem = leaveTypes.get(employee.getLeaveCodes()[counter]);
                    if ((al[counter] == 1 || al[counter] == 4 || al[counter] == 2) && reasonItem != null && !reasonItem.isMarkAsDraft()) {
                        rowWidget.setEnabled(false);
                        rowWidget.setSelected(new SelectItem(999, reasonItem.getShortName()));
                    }
                    counter++;

                    tempDate.setDate(tempDate.getDate() + 1);
                }
            }

            editableTable.addRow(rowWidgets);
        }

        addBrigadaEmployees();
        editableTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                editableTable.addRow(getWidgets(selectItemListBox.getSelectedId()));
            }

            @Override
            public void removeRow() {
                addBrigadaEmployees();
            }
        });
    }

    private void setTeamsEmployees() {
        HrmsService.App.get().getTeamsEmployeesInfo(new AsyncCallback<HashMap<Integer, ArrayList<SelectItem>>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(HashMap<Integer, ArrayList<SelectItem>> result) {
                teamsEmployees = result;
            }
        });
    }

    private void popupForTimeSlot(TimeSlotShortNameLookUp rowWidget, Integer columnNumber) {
        final KpiModal box = new KpiModal();
        box.addStyleName("attendance_report_modal");
        box.setTitle(wfmStrings.apply());
        box.setWidth(400);
        HorizontalPanel vp = new HorizontalPanel();

        TextBox textBox = new TextBox();
        textBox.setTitle("A");
        TextBox textBox2 = new TextBox();
        textBox2.setTitle("B");
        AvailabilityService.App.get().getTimeSlotInterval(rowWidget.getSelectedItemID(), new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(String[] result) {
                textBox.setText(result[0]);
                textBox.getElement().setAttribute("placeholder", "Interval");
                textBox.addKeyUpHandler(event1 -> {
                    Validation.numberValidation(textBox);
                    Validation.addNumericKeyboardListener(textBox);
                });
                vp.add(textBox);
                if ("week".equals(periodType)) {
                    textBox2.getElement().setAttribute("placeholder", "Skip");
                    textBox2.addKeyUpHandler(event1 -> {
                        Validation.numberValidation(textBox2);
                        Validation.addNumericKeyboardListener(textBox2);
                    });
                    vp.add(textBox2);
                }
                box.add(vp);
                box.open();
                WfmButton2 btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
                btnSave.addClickHandler(clickEvent -> {
                    if (!textBox.getValue().isEmpty()) {
                        box.close();
                        int skip = textBox2.getValue().isEmpty() ? 0 : Integer.parseInt(textBox2.getValue());
                        updateTimeSlotValues(columnNumber, rowWidget.getSelectedItem(), Integer.valueOf(textBox.getValue()), result[1], skip);
                    } else {
                        textBox.addStyleName(ERROR_FORM_STYLE);
                        Info.warn(wfmStrings.fillAllRequiredFields());
                    }

                });
                WfmButton2 btnClose = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
                btnClose.addClickHandler(clickEvent -> {
                    box.close();
                });
                box.addButton(btnSave);
                box.addButton(btnClose);
            }
        });

    }

    private void updateTimeSlotValues(Integer columnOrder, SelectItem timeSlot, Integer tillWhichrow, String excludedDays, int skip) {
        int maxColumns = editableTable.getGrid().getModel().getColumns().length;
        if ("week".equals(periodType) && skip > 0) {
            int filledCount = 0;
            int col = columnOrder;
            while (filledCount < tillWhichrow && col < maxColumns) {
                TimeSlotShortNameLookUp lookUp = (TimeSlotShortNameLookUp) editableTable.getColumnById(editableTable.getGrid().getCurrentRow(), String.valueOf(col));
                if (lookUp != null) {
                    lookUp.setSelected(timeSlot);
                    editableTable.getGrid().getModel().update(editableTable.getGrid().getCurrentRow(), col + 1, lookUp);
                    filledCount++;
                }
                col += skip + 1;
            }
        } else if (excludedDays != null) {
            int currentColumn = columnOrder;
            Set<Integer> excludedDaySet = new HashSet<>();
            if (!excludedDays.trim().isEmpty()) {
                for (String d : excludedDays.split(",")) {
                    excludedDaySet.add(Integer.parseInt(d.trim()));
                }
            }
            int filledCount = 0;
            while (filledCount < tillWhichrow && currentColumn < maxColumns) {
                TimeSlotShortNameLookUp lookUp = (TimeSlotShortNameLookUp) editableTable.getColumnById(editableTable.getGrid().getCurrentRow(), String.valueOf(currentColumn));
                if (lookUp != null) {
                    Date date = lookUp.getDate();
                    int jsDay = date.getDay();
                    int dayOfWeek = (jsDay + 6) % 7;
                    if (excludedDaySet.contains(dayOfWeek)) {
                        lookUp.setSelected(timeSlot);
                        editableTable.getGrid().getModel().update(editableTable.getGrid().getCurrentRow(), currentColumn + 1, lookUp);
                        filledCount++;
                    }
                }
                currentColumn++;
            }
        } else {
            for (int i = columnOrder; i < tillWhichrow + columnOrder; i++) {
                int column = i + 1;
                TimeSlotShortNameLookUp lookUp = (TimeSlotShortNameLookUp) editableTable.getColumnById(editableTable.getGrid().getCurrentRow(), "" + i);
                if (lookUp != null) {
                    lookUp.setSelected(timeSlot);
                    editableTable.getGrid().getModel().update(editableTable.getGrid().getCurrentRow(), column, lookUp);
                }
            }
        }
    }

    private int getISOWeeksInYear(int year) {
        Date jan1 = new Date(year - 1900, 0, 1);
        int dayOfWeek = jan1.getDay();
        int isoDay = dayOfWeek == 0 ? 7 : dayOfWeek;
        if (isoDay == 4) return 53;
        if (isoDay == 3 && isLeapYear(year)) return 53;
        return 52;
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    private Date getFirstDayOfISOWeek(int year, int weekNumber) {
        Date jan4 = new Date(year - 1900, 0, 4);
        int dayOfWeek = jan4.getDay();
        int isoDay = dayOfWeek == 0 ? 7 : dayOfWeek;
        Date mondayWeek1 = new Date(year - 1900, 0, 4 - (isoDay - 1));
        mondayWeek1.setDate(mondayWeek1.getDate() + (weekNumber - 1) * 7);
        return mondayWeek1;
    }

    private void addBrigadaEmployees() {
        brigadaEmployeesTable.removeFromParent();
        brigadaTableContainer.remove(brigadaEmployeesTable);
        brigadaEmployeesTable = null;
        brigadaEmployeesTable = new EditableTable(getBrigadaColumns(), false, false);
        brigadaTableContainer.add(brigadaEmployeesTable);
        brigadaEmployeesTable.removeAllRows();
        LinkedList<Integer> selectedGroupsId = getSelectedGroupsId();
        getGroupsEmployees(selectedGroupsId, monthPicker.getDate());
    }

    private void getTeamsInfo(LinkedHashMap<Integer, List<ShiftTeamsItem>> shiftTeamsItems) {
        shiftTeamsItems.forEach((k, v) -> {
            for (ShiftTeamsItem shiftTeamsItem : v) {
                brigadaEmployeesTable.addRow(getWidgetForEmployeeTableForEditView(shiftTeamsItem));
            }
        });
    }

    protected Widget[] getWidgetsWithData(ShiftItems shiftItems) {
        ArrayList<Widget> rowWidgets = new ArrayList<>();
        if (shiftItems != null) {
            LookUp projectLookUp = data.getLookUpType() == null || data.getLookUpType().equals(LookUpConstants.BRIGADA_ID) ? new BrigadaLookUp(null) : new EmployeeLookUpForShift();
            projectLookUp.setSelected(shiftItems.getSelectedGroup());
            projectLookUp.getSuggestBox().addSelectionHandler(t -> {
                addBrigadaEmployees();
            });
            rowWidgets.add(projectLookUp);
            HashMap<String, SelectItem> dayAndSelectedTimeSlot = shiftItems.getDayAndSelectedTimeSlotS();
            HashMap<Integer, String> leaveDays = shiftItems.getLeaveDays();
            Date date = monthPicker.getDate();
            if ("week".equals(periodType)) {
                int year = date.getYear() + 1900;
                for (int j = 1; j <= monthMaxDay; j++) {
                    TimeSlotShortNameLookUp timeSlotShortNameLookUp = new TimeSlotShortNameLookUp(getFirstDayOfISOWeek(year, j));
                    timeSlotShortNameLookUp.setSelected(dayAndSelectedTimeSlot.get("" + j));
                    int finalJ = j;
                    timeSlotShortNameLookUp.getSuggestBox().addSelectionHandler(event -> {
                        popupForTimeSlot(timeSlotShortNameLookUp, finalJ);
                    });
                    rowWidgets.add(timeSlotShortNameLookUp);
                }
            } else if (!isCustom) {
                for (int j = 1; j <= monthMaxDay; j++) {
                    TimeSlotShortNameLookUp timeSlotShortNameLookUp = new TimeSlotShortNameLookUp(new Date(date.getYear(), date.getMonth(), j));
                    if (leaveDays.get(j) != null) {
                        timeSlotShortNameLookUp.setSelected(new SelectItem(999, leaveDays.get(j)));
                        timeSlotShortNameLookUp.setEnabled(false);
                    } else {
                        timeSlotShortNameLookUp.setSelected(dayAndSelectedTimeSlot.get("" + j));
                    }
                    int finalJ = j;
                    timeSlotShortNameLookUp.getSuggestBox().addSelectionHandler(event -> {
                        popupForTimeSlot(timeSlotShortNameLookUp, finalJ);
                    });
                    rowWidgets.add(timeSlotShortNameLookUp);
                }
            } else {
                Date tempDate = new Date(monthPicker.getDate().getTime());
                Date endDate = new Date(endDatePicker.getDate().getTime());
                int counter = 1;
                while (!tempDate.after(endDate)) {
                    TimeSlotShortNameLookUp timeSlotShortNameLookUp = new TimeSlotShortNameLookUp(new Date(tempDate.getTime()));
                    timeSlotShortNameLookUp.setSelected(dayAndSelectedTimeSlot.get(DateUtils.getDateFormatShort(tempDate)));
                    timeSlotShortNameLookUp.hideIconAndEnableFocusFetch();
                    final int finalInt =  counter;
                    timeSlotShortNameLookUp.getSuggestBox().addSelectionHandler(event -> {
                        popupForTimeSlot(timeSlotShortNameLookUp, finalInt);
                    });
                    rowWidgets.add(timeSlotShortNameLookUp);
                    counter++;
                    tempDate.setDate(tempDate.getDate() + 1);
                }

            }

        }
        return rowWidgets.toArray(new Widget[]{});

    }

    protected Widget[] getWidgetForEmployeeTable(EmployeeReport employee) {
        ArrayList<Widget> rowWidgets = new ArrayList<>();
        BrigadaLookUp team = new BrigadaLookUp();
        CustomCellTextBox employeeCode = new CustomCellTextBox();
        EmployeeBox employeeBox = new ShiftEditView.EmployeeBox(employee, employee.getLeaveCount());
        employeeBox.setEnabled(true);
        employeeBox.setReadOnly(true);
        employeeBox.addStyleName(DEFAULT_WIDTH);
        employeeBox.setStyleName("file--AdditionalPaymentUIBinder");

        EmployeeLookUp employeeFullName = new EmployeeLookUp(true, false);
        CustomCellTextBox employeePosition = new CustomCellTextBox();
        CustomCellTextBox employeeDepartment = new CustomCellTextBox();
        CustomCellTextBox employeeLabel = new CustomCellTextBox();
        team.setSelected(employee.getTeam() != null ? employee.getTeam() : null);
        employeeCode.setText(employee.getCode());
        employeeFullName.setSelected(employee.getEmployeeName());
        employeePosition.setText(employee.getDepartmentOrPosition());
        employeeDepartment.setText(employee.getPosition());
        employeeLabel.setText(employee.getUnit());
        team.setEnabled(false);
        employeeCode.setEnabled(false);
        employeeFullName.setEnabled(false);
        employeeDepartment.setEnabled(false);
        employeePosition.setEnabled(false);
        rowWidgets.add(team);
        rowWidgets.add(employeeCode);
        rowWidgets.add(employeeBox);
        rowWidgets.add(employeeDepartment);
        rowWidgets.add(employeePosition);
        rowWidgets.add(employeeLabel);
        return rowWidgets.toArray(new Widget[]{});
    }

    protected Widget[] getWidgetForEmployeeTableForEditView(ShiftTeamsItem employee) {
        ArrayList<Widget> rowWidgets = new ArrayList<>();
        BrigadaLookUp team = new BrigadaLookUp();
        CustomCellTextBox employeeCode = new CustomCellTextBox();

        EmployeeBoxForEdit employeeBox = new ShiftEditView.EmployeeBoxForEdit(employee, employee.getLeaveCount());
        employeeBox.setEnabled(true);
        employeeBox.setReadOnly(true);
        employeeBox.addStyleName(DEFAULT_WIDTH);
        employeeBox.setStyleName("file--AdditionalPaymentUIBinder");

        EmployeeLookUp employeeFullName = new EmployeeLookUp(true, false);
        CustomCellTextBox employeePosition = new CustomCellTextBox();
        CustomCellTextBox employeeDepartment = new CustomCellTextBox();
        CustomCellTextBox employeeLabel = new CustomCellTextBox();
        team.setSelected(employee.getTeam() != null ? employee.getTeam() : null);
        employeeCode.setText(employee.getEmployeeCode());
        employeeFullName.setSelected(employee.getFullName());
        employeePosition.setText(employee.getDepartment());
        employeeDepartment.setText(employee.getPosition());
        employeeLabel.setText(employee.getLabel());
        team.setEnabled(false);
        employeeCode.setEnabled(false);
        employeeFullName.setEnabled(false);
        employeeDepartment.setEnabled(false);
        employeePosition.setEnabled(false);
        rowWidgets.add(team);
        rowWidgets.add(employeeCode);
        rowWidgets.add(employeeBox);
        rowWidgets.add(employeeDepartment);
        rowWidgets.add(employeePosition);
        rowWidgets.add(employeeLabel);
        return rowWidgets.toArray(new Widget[]{});
    }

    private void getGroupsEmployees(LinkedList<Integer> groupsId, Date period) {

        HrmsService.App.get().getProjectEmployeesSelectItem(groupsId, period, selectItemListBox.getSelectedItem().getId(), new AsyncCallback<LinkedHashMap<String, List<EmployeeReport>>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(LinkedHashMap<String, List<EmployeeReport>> result) {

                result.forEach((k, v) -> {
                    for (EmployeeReport employeeReport : v) {
                        employeeReport.setStatus(k);
                        brigadaEmployeesTable.addRow(getWidgetForEmployeeTable(employeeReport));
                    }

                });
                if (selectItemListBox.getSelectedId().equals(LookUpConstants.BRIGADA_ID) || selectItemListBox.getSelectedId() == null) {
                    HrmsService.App.get().getTeamsManagers(groupsId, new AsyncCallback<ArrayList<SelectItem>>() {
                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(ArrayList<SelectItem> result) {
                            manager.setValue(null);
                            backUpManager.setValue(null);
                            flexTable.removeAllRows();
                            backupManagerFlexTable.removeAllRows();
                            drawManagerFields(result);

                        }
                    });
                }
            }
        });
    }

    private void drawManagerFields(ArrayList<SelectItem> result) {
        int index = 0;
        StringBuilder text2 = new StringBuilder();
        StringBuilder text3 = new StringBuilder();
        for (SelectItem item : result) {
            TextBox managerBox = new TextBox();
            TextBox bcManagerBox = new TextBox();
            managerBox.setText(item.getDescription());
            if (item.getCategory() != null) {
                bcManagerBox.setText(item.getCategory());
                backupManagerFlexTable.setWidget(index++, 0, bcManagerBox);
            }
            flexTable.setWidget(index++, 0, managerBox);
            text2.append(managerBox.getText()).append("-:-");
            text3.append(bcManagerBox.getText()).append("".equals(bcManagerBox.getText()) ? "" : "-:-");
        }
        manager.setValue(text2.toString());
        backUpManager.setValue(text3.toString());
    }

    private LinkedList<Integer> getSelectedGroupsId() {
        LinkedList<Integer> groupsId = new LinkedList<>();
        EditableGrid grid = editableTable.getGrid();
        for (int i = 0; i < grid.getRowCount(); i++) {
            LookUp projectLookUp = selectItemListBox.getSelectedItem().getId().equals(LookUpConstants.BRIGADA_ID) ? (BrigadaLookUp) editableTable.getColumnById(i, "group") : (EmployeeLookUpForShift) editableTable.getColumnById(i, "group");
            if (projectLookUp.getSelectedItem() == null) {
                continue;
            }
            groupsId.add(projectLookUp.getSelectedItem().getId());
            for (int j = 1; j <= monthMaxDay; j++) {
            }

        }
        return groupsId;
    }


    @Override
    protected void getDataToFillFields() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(objectId);
        HrmsService.App.get().getShiftItem(objectId, false, new AsyncCallback<ShiftItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ShiftItem result) {
                data = result;
                if (result.getPeriodType() != null) {
                    periodType = result.getPeriodType();
                }
                if (result.getCreatorDepartment() == null) {
                    ShiftEditView.this.getCustomFieldUtil().setCompanyCustomFieldItems(result.getCustomFieldItems());
                    monthPicker.setDate(result.getPeriod());
                    if ("week".equals(periodType)) {
                        monthMaxDay = getISOWeeksInYear(result.getPeriod().getYear() + 1900);
                    } else if (data.getEndDate() != null) {
                        isCustom = true;
                        monthMaxDay = DateUtil.countDays(data.getPeriod(), data.getEndDate().getDate());
                    } else {
                        monthMaxDay = DateUtil.countDays(result.getPeriod());
                    }
                    if (result.getDepartment() != null) {
                        departmentLookUp.setSelected(result.getDepartment());
                    }

                    setDataToFields();
                    getTeamsInfo(result.getShiftTeams());
                }
                initButtonsPanel();
                pdfTool(result);
            }
        });
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.SHIFT_FORM;
    }

    @Override
    protected String getFormType() {
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private void drawFields() {

        addTitleField(BASIC_INFORMATION, wfmStrings.basicDetails());
        if ("week".equals(periodType)) {
            if (formPropertyMap != null && formPropertyMap.get("monthPicker") != null) {
                addField("monthPicker", monthPicker, getTitle(formPropertyMap.get("monthPicker").isChanged() ? formPropertyMap.get("monthPicker").getTitle() : wfmStrings.year(), formPropertyMap.get("monthPicker").isRequired()));
                monthPicker.setEnabled(!formPropertyMap.get("monthPicker").isDisabled());
            } else {
                addField("monthPicker", monthPicker, wfmStrings.year());
            }
        } else if ("custom".equals(periodType)) {
            monthWidget = inputGroupPrependedRadioCheckbox();
            if (formPropertyMap != null && formPropertyMap.get("monthPicker") != null) {
                addField("monthPicker", monthWidget, getTitle(formPropertyMap.get("monthPicker").isChanged() ? formPropertyMap.get("monthPicker").getTitle() : wfmStrings.month(), formPropertyMap.get("monthPicker").isRequired()));
                monthPicker.setEnabled(!formPropertyMap.get("monthPicker").isDisabled());
            } else {
                addField("monthPicker", monthWidget, wfmStrings.period());
            }
        } else {
            if (formPropertyMap != null && formPropertyMap.get("monthPicker") != null) {
                addField("monthPicker", monthPicker, getTitle(formPropertyMap.get("monthPicker").isChanged() ? formPropertyMap.get("monthPicker").getTitle() : wfmStrings.month(), formPropertyMap.get("monthPicker").isRequired()));
                monthPicker.setEnabled(!formPropertyMap.get("monthPicker").isDisabled());
            } else {
                addField("monthPicker", monthPicker, wfmStrings.month());
            }
        }


        if (formPropertyMap != null && formPropertyMap.get(APPROVERS) != null) {
            addField(APPROVERS, approvers, getTitle(formPropertyMap.get(APPROVERS).isChanged() ? formPropertyMap.get(APPROVERS).getTitle() : wfmStrings.approvers(), formPropertyMap.get(APPROVERS).isRequired()));
            approvers.setEnabled(!formPropertyMap.get(APPROVERS).isDisabled());
        } else {
            addField(APPROVERS, approvers, wfmStrings.approvers());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.MANAGER, flexTable, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getTitle() : wfmStrings.manager(), formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isRequired()));
        } else {
            addField(CustomFormConstants.PROJECT.MANAGER, flexTable, getTitle(wfmStrings.manager()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerFlexTable, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getTitle() : wfmStrings.backupManagers(), formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isRequired()));
        } else {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerFlexTable, getTitle(wfmStrings.backupManagers()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null) {
            addField(CustomFormConstants.DEPARTMENT, departmentLookUp, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT).getTitle() : wfmStrings.department(), formPropertyMap.get(CustomFormConstants.DEPARTMENT).isRequired()));
        } else {
            addField(CustomFormConstants.DEPARTMENT, departmentLookUp, getTitle(wfmStrings.department()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BRIGADA) != null) {
            addField(CustomFormConstants.BRIGADA, brigadaLookUp, getTitle(formPropertyMap.get(CustomFormConstants.BRIGADA).isChanged() ? formPropertyMap.get(CustomFormConstants.BRIGADA).getTitle() : wfmStrings.brigadas(), formPropertyMap.get(CustomFormConstants.BRIGADA).isRequired()));
        } else {
            addField(CustomFormConstants.BRIGADA, brigadaLookUp, getTitle(wfmStrings.brigadas()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null) {
            addField(CustomFormConstants.TYPE, selectItemListBox, getTitle(formPropertyMap.get(CustomFormConstants.TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(CustomFormConstants.TYPE).isRequired()));
            selectItemListBox.setEnabled(!formPropertyMap.get(CustomFormConstants.TYPE).isDisabled());
        } else {
            addField(CustomFormConstants.TYPE, selectItemListBox, wfmStrings.type());
        }
        addField(CustomFormConstants.OWNER, employeeLookUp, wfmStrings.owners());
        addTitleField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES, wfmStrings.involvedEmployees());
//        addField("shiftContainer", brigadaTableContainer, null);
    }

    private void save(String status) {
        if (!validation()) {
            Info.warn(wfmStrings.fillRequiredField());
            return;
        }

        ArrayList<ShiftItems> shiftTableValues = new ArrayList<>();

        EditableGrid grid = editableTable.getGrid();

        if (!isCustom || "week".equals(periodType)) {
            for (int i = 0; i < grid.getRowCount(); i++) {
                ShiftItems shiftItems = new ShiftItems();
                HashMap<String, SelectItem> dayAndSelectedTimeSlot = new HashMap<>();
                HashMap<String, Integer> dayAndShiftId = new HashMap<>();
                LookUp projectLookUp = selectItemListBox.getSelectedItem().getId().equals(LookUpConstants.BRIGADA_ID) ? (BrigadaLookUp) editableTable.getColumnById(i, "group") : (EmployeeLookUpForShift) editableTable.getColumnById(i, "group");
                if (projectLookUp != null && projectLookUp.getSelectedItem() == null) {
                    continue;
                }
                for (int j = 1; j <= monthMaxDay; j++) {
                    TimeSlotShortNameLookUp selectedTimeSheet = (TimeSlotShortNameLookUp) editableTable.getColumnById(i, "" + j);
                    dayAndSelectedTimeSlot.put("" + j, selectedTimeSheet.getSelectedItem());
                    if (data.getShiftItems() != null && data.getShiftItems().size() > i) {
                        dayAndShiftId.put("" + j, data.getShiftItems().get(i).getDayAndShiftItemId().get("" + j));
                    }
                }
                shiftItems.setDayAndSelectedTimeSlotS(dayAndSelectedTimeSlot);
                shiftItems.setDayAndShiftItemId(dayAndShiftId);
                shiftItems.setSelectedGroup(projectLookUp.getSelectedItem());
                shiftItems.setRowId(i);
                shiftTableValues.add(shiftItems);

            }
        } else {
            HashMap<String, Integer> dayAndShiftId = new HashMap<>();
            for (int i = 0; i < grid.getRowCount(); i++) {
                ShiftItems shiftItems = new ShiftItems();
                HashMap<String, SelectItem> dayAndSelectedTimeSlot = new HashMap<>();
                Date tempDate = new Date(monthPicker.getDate().getTime());
                Date endDate = endDatePicker.getDate();
                LookUp projectLookUp = selectItemListBox.getSelectedItem().getId().equals(LookUpConstants.BRIGADA_ID) ? (BrigadaLookUp) editableTable.getColumnById(i, "group") : (EmployeeLookUpForShift) editableTable.getColumnById(i, "group");
                if (projectLookUp != null && projectLookUp.getSelectedItem() == null) {
                    continue;
                }
                int j = 1;
                while (!tempDate.after(endDate)) {
                    TimeSlotShortNameLookUp selectedTimeSheet = (TimeSlotShortNameLookUp) editableTable.getColumnById(i, "" + j);

                    dayAndSelectedTimeSlot.put(DateUtils.getDateFormatShort(selectedTimeSheet.getDate()), selectedTimeSheet.getSelectedItem());
                    if (data.getShiftItems() != null && data.getShiftItems().size() > i) {
                        dayAndShiftId.put(DateUtils.getDateFormatShort(selectedTimeSheet.getDate()), data.getShiftItems().get(i).getDayAndShiftItemId().get(DateUtils.getDateFormatShort(selectedTimeSheet.getDate())));
                    }
                    j++;
                    tempDate.setDate(tempDate.getDate() + 1);
                }
                shiftItems.setDayAndSelectedTimeSlotS(dayAndSelectedTimeSlot);
                shiftItems.setDayAndShiftItemId(dayAndShiftId);
                shiftItems.setSelectedGroup(projectLookUp.getSelectedItem());
                shiftItems.setRowId(i);
                shiftTableValues.add(shiftItems);

            }
        }

        data.setShiftItems(shiftTableValues);
        data.setPeriod(DateUtil.getMonthLastDate(monthPicker.getDate()));
        data.setPeriodType(periodType);
        data.setOwnersId(employeeLookUp.getSelectedItemsIdsAsString());
        data.setStatusCode(status);
        data.setApprovers(approvers.getChosenApprovers());
        data.setId(objectId);
        data.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        data.setNumberData(numbering.getNumberData(false));
        data.setShiftTeams(saveShiftTeams());
        data.setManager(manager.getValue());
        data.setBackupManager(backUpManager.getValue());
        data.setLookUpType(selectItemListBox.getSelectedValue().getId());
        data.setDepartment(departmentLookUp.getSelectedItem());
        data.setBirgada(brigadaLookUp.getSelectedItem());
        data.setStartDate(monthPicker.getDateAsNonConvertable());
        data.setEndDate(endDatePicker != null ? endDatePicker.getDateAsNonConvertable() : null);
        validateForSameBrigadaPeriod();
    }


    private void initButtonsPanel() {
        if (data == null) {
            data = new ShiftItem();
        }
        draftButton = new WfmButton2(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE);
        draftButton.addClickHandler(event -> {
            save(Constants.SHIFT_DRAFT);
        });
        addRightButton(draftButton);

        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
        approveButton.addClickHandler(clickEvent -> save(Constants.SHIFT_APPROVED));
        approveButton.setVisible(false);
        addRightButton(approveButton);

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        if (Utils.hasPermission(PermissionConstants.HRMS_SHIFT_PDF)) {
            addRightButton(printPdfSplitButton);
        }

        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.addClickHandler(clickEvent -> save(Constants.SHIFT_SUBMITTED));
        submitButton.setVisible(false);
        addRightButton(submitButton);

        approvers = new ChosenApproversWidget(RelationItem.TYPE_SHIFT, data.getApproverEmployee() != null ? objectId : null);

        if (data.isApprover() != null && data.isApprover()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVERS) != null) {
                addField(CustomFormConstants.APPROVERS, approvers, getTitle(formPropertyMap.get(CustomFormConstants.APPROVERS).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVERS).getTitle() : wfmStrings.approver(), formPropertyMap.get(CustomFormConstants.APPROVERS).isRequired()));
                approvers.setEnabled(!formPropertyMap.get(CustomFormConstants.APPROVERS).isDisabled());
            } else {
                addField(APPROVERS, approvers, getTitle(wfmStrings.approver(), true));
            }
            if (objectId != null) {
                if (Constants.SHIFT_DRAFT.equals(data.getStatusCode())) {
                    draftButton.setVisible(true);
                } else if (Constants.SHIFT_SUBMITTED.equals(data.getStatusCode()) ||
                        Constants.SHIFT_APPROVED.equals(data.getStatusCode())) {
                    draftButton.setVisible(false);
                }
            } else {
                draftButton.setVisible(true);
            }
        } else {
            approveButton.setVisible(hasAccessToApprove());
            if (Constants.SHIFT_SUBMITTED.equals(data.getStatusCode()) ||
                    Constants.SHIFT_APPROVED.equals(data.getStatusCode())) {
                draftButton.setVisible(false);
            }
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, ShiftEditView.this, (sender, args) -> {
            if (approvers.getFirstApproverLookUp() != null) {
                approvers.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    Integer itemId = item != null ? item.getId() : null;
                    if (itemId != null && Utils.getUserID().equals(itemId)) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        submitButton.setVisible(hasAccessToSubmit());
                        approveButton.setVisible(false);
                    }
                });
                if (approveButton != null && submitButton != null && approvers.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approvers.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitButton.setVisible(hasAccessToSubmit());
                    }
                }
            }
        });

    }

    private void generateNewNumber() {
        HrmsService.App.get().generateShiftCode(new AbstractAsyncCallback<NumberData>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(NumberData result) {
                if (result != null) {
                    numberData = result;
                    if (numbering != null) {
                        numbering.setNumberData(numberData);
                    }
                }
            }
        });
    }


    private boolean validation() {
        int error = 0;
        if (formPropertyMap != null && formPropertyMap.get(APPROVER) != null && formPropertyMap.get(APPROVER).isRequired() && !approvers.isValid()) {
            error++;
        }

        if (formPropertyMap != null && formPropertyMap.get("monthPicker") != null && formPropertyMap.get("monthPicker").isRequired()) {
            error += markAsError(monthPicker, !Validation.validateDate(monthPicker));
        }

        EditableGrid grid = editableTable.getGrid();
        ArrayList<Integer> selectedGroup = new ArrayList<>();
        selectedGroup.clear();
        for (int i = 0; i < grid.getRowCount(); i++) {
            LookUp projectLookUp = selectItemListBox.getSelectedItem().getId().equals(LookUpConstants.BRIGADA_ID) ? (BrigadaLookUp) editableTable.getColumnById(i, "group") : (EmployeeLookUpForShift) editableTable.getColumnById(i, "group");
            if (selectedGroup.contains(projectLookUp.getSelectedItemID())) {
                Info.warn(projectLookUp.getSelectedItem().getName() + " " + wfmStrings.isAlreadySelected());
                projectLookUp.addStyleName(ERROR_FORM_STYLE);
                error++;
            } else if (projectLookUp.getSelectedItem() == null && i == 0) {
                error++;
                editableTable.addStyleName(ERROR_FORM_STYLE);
            } else if (projectLookUp.getSelectedItem() == null) {
                grid.removeRow(i);
            } else {
                selectedGroup.add(projectLookUp.getSelectedItemID());
            }
        }
        return error == 0;
    }


    private void setDataToFields() {
        if (data.getEndDate() != null) {
            isCustom = true;
            monthWidget.remove(monthPicker);
            monthWidget.remove(endDatePicker);
            monthPicker = new DatePicker();
            monthPicker.setEnabled(false);
            monthPicker.setOnlyMonthFormat(false);
            monthPicker.setDate(data.getPeriod());
            monthPicker.setDateTimeFormat(DateTimeFormat.getFormat(Utils.getShortDateFormat()));
            monthPicker.setTitle(wfmStrings.startDate());

            endDatePicker = new DatePicker();
            endDatePicker.setEnabled(false);
            endDatePicker.setDateTimeFormat(DateTimeFormat.getFormat(Utils.getShortDateFormat()));
            endDatePicker.setOnlyMonthFormat(false);
            endDatePicker.setDate(data.getEndDate().getDate());
            endDatePicker.setTitle(wfmStrings.endDate());
            monthWidget.add(monthPicker);
            monthWidget.add(endDatePicker);
        }

        editableTable = new EditableTable(getColumns(), true, true);
        editableTable.addRow(getWidgets(data.getLookUpType()));

        editableTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                editableTable.addRow(getWidgets(data.getLookUpType()));
            }

            @Override
            public void removeRow() {
                addBrigadaEmployees();
            }
        });

        editableTable.addStyleName("brigada_table");
        //Sticky table header need this
        editableTable.addStyleName("tbl-StickyHead tbl-fCellHide");
        addField(SHIFT, editableTable, null);
        editableTable.removeAllRows();

        if (data != null && data.getShiftItems() != null) {
            for (ShiftItems shiftItem : data.getShiftItems()) {
                editableTable.addRow(getWidgetsWithData(shiftItem));
            }
            monthPicker.setDate(data.getPeriod());
            monthPicker.setEnabled(false);
        }
        if (data.getBirgada() != null) {
            brigadaLookUp.setSelected(data.getBirgada());
        }
        if (data.getLookUpType() != null) {
            String lookUpText = null;
            if (data.getLookUpType().equals(LookUpConstants.BRIGADA_ID)) {
                lookUpText = wfmStrings.team();
            } else if (data.getLookUpType().equals(LookUpConstants.EMPLOYEE_ID)) {
                lookUpText = wfmStrings.duty();
            } else if (data.getLookUpType().equals(LookUpConstants.OVERTIME)) {
                lookUpText = wfmStrings.overtime();
            }
            selectItemListBox.setSelected(new SelectItem(data.getLookUpType(), lookUpText));
        }
        if ("week".equals(periodType)) {
            selectItemListBox.setEnabled(false);
        }

        manager.setValue(data.getManager());
        backUpManager.setValue(data.getBackupManager());
        employeeLookUp.setSelectedItems(data.getOwnersSelectItem());
        if (data.getLookUpType() == null || data.getLookUpType().equals(LookUpConstants.BRIGADA_ID)) {
            drawManagerFields(data.getShiftItems().stream().map(ShiftItems::getSelectedGroup).collect(Collectors.toCollection(ArrayList::new)));
        }
        getCustomFieldUtil().fillCustomFieldsWithData(data.getCustomFieldItems());
    }


    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.ShiftList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (getCustomFieldUtil() != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
                ShiftEditView.super.onInitialize();
            }
        });
        return null;
    }

    public LinkedHashMap<Integer, List<ShiftTeamsItem>> saveShiftTeams() {
        LinkedHashMap<Integer, List<ShiftTeamsItem>> items = new LinkedHashMap<>();
        List<ShiftTeamsItem> teamsItems = new ArrayList<>();
        EditableGrid grid = brigadaEmployeesTable.getGrid();
        for (int i = 0; i < grid.getRowCount(); i++) {
            BrigadaLookUp team = (BrigadaLookUp) brigadaEmployeesTable.getColumnById(i, "team");
            CustomCellTextBox employeeCode = (CustomCellTextBox) brigadaEmployeesTable.getColumnById(i, "employeeCode");
//            EmployeeLookUp fullName = (EmployeeLookUp) brigadaEmployeesTable.getColumnById(i, "fullName");
            CustomCellTextBox positon = (CustomCellTextBox) brigadaEmployeesTable.getColumnById(i, "positon");
            CustomCellTextBox department = (CustomCellTextBox) brigadaEmployeesTable.getColumnById(i, "department");
            CustomCellTextBox label = (CustomCellTextBox) brigadaEmployeesTable.getColumnById(i, "label");
            ShiftTeamsItem shiftTeamsItem = new ShiftTeamsItem();
            shiftTeamsItem.setTeam(team.getSelectedItem());
            shiftTeamsItem.setEmployeeCode(employeeCode.getValue());
//            shiftTeamsItem.setFullName(fullName.getSelectedItem());
            shiftTeamsItem.setPosition(positon.getValue());
            shiftTeamsItem.setDepartment(department.getValue());
            shiftTeamsItem.setLabel(label.getValue());
            teamsItems.add(shiftTeamsItem);
        }
        items.put(1, teamsItems);
        return items;
    }

    private static class EmployeeBox extends Div implements CustomCellInterface {
        EmployeeReport employee;
        Integer leaveCount;
        CustomCellTextBox textBox = new CustomCellTextBox();

        EmployeeBox(EmployeeReport employee, Integer leaveCount) {
            super();
            this.leaveCount = employee.getLeave().size();
            this.employee = employee;


            textBox.setText(employee.getEmployeeName().getName());
            textBox.setWidth("100%");
            textBox.setStyleName("width: 100%;");
            textBox.setEnabled(false);
            add(textBox);
            if (leaveCount != null && leaveCount > 0) {
                for (StatisticsLeaveRequest statisticsLeaveRequest : employee.getLeave()) {
                    Span reson = new Span();
                    reson.setStyleName("tab-label");
                    reson.setText(statisticsLeaveRequest.getReason());
                    reson.addClickHandler(focusEvent -> {
                        HrmsSinksContainerFactory.entryPoint.onHistoryChanged("leaverequest/" + statisticsLeaveRequest.getObjectID());
                    });
                    String text = new String();
                    text = text + statisticsLeaveRequest.getReason() + " <br> " + View.wfmStrings.from() + " " + statisticsLeaveRequest.getFromDate() + " <br> " + View.wfmStrings.to() + " " + statisticsLeaveRequest.getToDate();

                    KpiToolTip kpiToolTip = new KpiToolTip(reson, text, Position.TOP);
                    kpiToolTip.setMinWidth(100);
                    kpiToolTip.setMaxWidth(100);
                    if (leaveCount == 1) {
                        reson.setStyle("cursor: pointer; margin: 1px; max-width: 50%; max-height: 50px !important; background-color:" + statisticsLeaveRequest.getDescription() + ";");
                    } else {
                        reson.setStyle("cursor: pointer; margin: 1px; background-color:" + statisticsLeaveRequest.getDescription() + ";");

                    }
                    add(reson);
                }
            }

        }

        public void setReadOnly(boolean readOnly) {
            textBox.setReadOnly(readOnly);
        }


        @Override
        public String getDisplayValue() {
            if (leaveCount != null && leaveCount > 0) {
                return textBox.getText() + " <span class='tab-label'>" + leaveCount + "</span>";
            }
            return textBox.getDisplayValue();
        }

        @Override
        public void setItemValue(Object value) {
            textBox.setItemValue(value);
        }

        @Override
        public void setItemFocus(boolean focused) {
            textBox.setItemFocus(focused);
        }


        public EmployeeReport getEmployee() {
            return employee;
        }
    }

    private static class EmployeeBoxForEdit extends Div implements CustomCellInterface {
        ShiftTeamsItem employee;
        Integer leaveCount;
        Span span = new Span();
        CustomCellTextBox textBox = new CustomCellTextBox();

        public EmployeeBoxForEdit(ShiftTeamsItem employee, Integer leaveCount) {
            super();
            this.leaveCount = employee.getLeave().size();
            this.employee = employee;


            textBox.setText(employee.getFullName().getName());
            textBox.setWidth("100%");
            textBox.setStyleName("width: 100%;");
            textBox.setEnabled(false);
            add(textBox);
            if (leaveCount != null && leaveCount > 0) {
                for (StatisticsLeaveRequest statisticsLeaveRequest : employee.getLeave()) {
                    leaveCount++;
                    Span reson = new Span();
                    reson.setStyleName("tab-label");
                    reson.setText(statisticsLeaveRequest.getReason());
                    reson.addClickHandler(focusEvent -> {
                        HrmsSinksContainerFactory.entryPoint.onHistoryChanged("leaverequest/" + statisticsLeaveRequest.getObjectID());
                    });
                    String text = new String();
                    text = text + statisticsLeaveRequest.getReason() + " <br> " + View.wfmStrings.from() + " " + statisticsLeaveRequest.getFromDate() + " <br> " + View.wfmStrings.to() + " " + statisticsLeaveRequest.getToDate();

                    KpiToolTip kpiToolTip = new KpiToolTip(reson, text, Position.TOP);
                    kpiToolTip.setMinWidth(100);
                    kpiToolTip.setMaxWidth(100);
                    if (leaveCount == 1) {
                        reson.setStyle("cursor: pointer; margin: 1px; max-width: 50%; max-height: 50px !important; background-color:" + statisticsLeaveRequest.getDescription() + ";");
                    } else {
                        reson.setStyle("cursor: pointer; margin: 1px; background-color:" + statisticsLeaveRequest.getDescription() + ";");

                    }
                    add(reson);
                }
            }

        }

        public void setReadOnly(boolean readOnly) {
            textBox.setReadOnly(readOnly);
        }


        @Override
        public String getDisplayValue() {
            if (leaveCount != null && leaveCount > 0) {
                return textBox.getText() + " <span class='tab-label'>" + leaveCount + "</span>";
            }
            return textBox.getDisplayValue();
        }

        @Override
        public void setItemValue(Object value) {
            textBox.setItemValue(value);
        }

        @Override
        public void setItemFocus(boolean focused) {
            textBox.setItemFocus(focused);
        }


        public ShiftTeamsItem getEmployee() {
            return employee;
        }
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    private void validateForSameBrigadaPeriod() {
        final boolean[] isValid = {true};
        DateTimeFormat datePattern = DateTimeFormat.getFormat("yyyy-MM");
        Date date = monthPicker.getDate();
        String period = datePattern.format(date);
        EditableGrid grid = editableTable.getGrid();
        ArrayList<Integer> selectedGroup = new ArrayList<>();
        selectedGroup.clear();
        for (int i = 0; i < grid.getRowCount(); i++) {
            LookUp projectLookUp = selectItemListBox.getSelectedItem().getId().equals(LookUpConstants.BRIGADA_ID) ? (BrigadaLookUp) editableTable.getColumnById(i, "group") : (EmployeeLookUpForShift) editableTable.getColumnById(i, "group");
            selectedGroup.add(projectLookUp.getSelectedItemID());
        }
        HashMap<Integer, ArrayList<String>> employeeAndSelectedDates = new HashMap<>();
        if (selectItemListBox.getSelectedItem().getId().equals(LookUpConstants.OVERTIME) || selectItemListBox.getSelectedItem().getId().equals(LookUpConstants.EMPLOYEE_ID)) {
            for (ShiftItems shiftItem : data.getShiftItems()) {
                HashMap<String, SelectItem> dayAndSelectedTimeSlotS = shiftItem.getDayAndSelectedTimeSlotS();
                dayAndSelectedTimeSlotS.forEach((k, v) -> {
                    if (v != null && v.getId() != 999) {
                        if (!employeeAndSelectedDates.containsKey(shiftItem.getSelectedGroup().getId())) {
                            employeeAndSelectedDates.put(shiftItem.getSelectedGroup().getId(), new ArrayList<String>());
                        }
                        employeeAndSelectedDates.get(shiftItem.getSelectedGroup().getId()).add(k);
                    }
                });
            }
        }
        HrmsService.App.get().getSelectedTeamsByPeriodAndIds(period, employeeAndSelectedDates, selectedGroup, objectId, selectItemListBox.getSelectedId(), new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                if (result.size() == 0) {
                    draftButton.setEnabled(false);
                    approveButton.setEnabled(false);
                    submitButton.setEnabled(false);
                    HrmsService.App.get().saveShiftItem(data, new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Info.warn(wfmStrings.sorrySomethingWentWrong());
                            draftButton.setEnabled(true);
                            approveButton.setEnabled(hasAccessToApprove());
                            submitButton.setEnabled(hasAccessToSubmit());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            LoadingPanel.loading(false);
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.shift()));
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SHIFT_ADD, null, ShiftEditView.this);
                            closeTab();
                        }
                    });
                } else {
                    for (String s : result) {
                        Info.warn(s + " " + wfmStrings.isAlreadySelected());
                    }
                }
            }
        });
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    public void pdfTool(ShiftItem result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectId);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        if (data.getPeriod() != null) {
            parameters.put("startDate_nc", String.valueOf(data.getPeriod().getTime()));
        }
        parameters.put("SHIFT_TYPE", data.getLookUpType().equals(LookUpConstants.BRIGADA_ID) ? "TEAM" : "DUTY");
        String pdfURL = CommandConstants.PDF_URL + "/shiftViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private boolean hasAccessToSubmit() {
        return Utils.hasPermission(PermissionConstants.HRMS_SHIFT_SUBMIT);
    }

    private boolean hasAccessToApprove() {
        return Utils.hasPermission(PermissionConstants.HRMS_SHIFT_APPROVE);
    }
}
