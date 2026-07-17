package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableBeta;
import com.edatasite.workforce.gwt.availability.client.ui.view.editFingerprint.FingerprintAdjustmentPopup;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.paging.PagingWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.BrigadaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectDepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Omonullo on 3/15/2017.
 */
public class TerminalAttendanceView extends Composite implements Constants {

    String PDF_VERSION = "PDF_VERSION";

    interface TerminalAttendanceViewUiBinder extends UiBinder<HTMLPanel, TerminalAttendanceView> {
    }

    private static final TerminalAttendanceViewUiBinder ourUiBinder = GWT.create(TerminalAttendanceViewUiBinder.class);

    public static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private static final DateTimeFormat monthNameFormatter = DateTimeFormat.getFormat("MMMM");
    private static final DateTimeFormat monthYearFormatter = DateTimeFormat.getFormat("MM/yyyy");
    private static final long ONE_DAY_MILLIS = 24L * 60L * 60L * 1000L;

    private AttendanceTableBeta table;
    /*private Integer teamId;*/
    private Integer locationId;
    private Integer projectID;
    private Integer timeslotId;
    private Integer brigadaId;
    private Integer positionId;
    private ListingFilterParameter fp;
    private MaterialLink xlsVersion;

    private MaterialLink pdfVersion;

    private MaterialLink portrait;

    private MaterialLink landscape;
    private CustomFormItemPdfTemplateList templateList;
    private SplitButton printPdfSplitButton;
    private CRMLookUp department;
    private MultiSelectDepartmentLookUp departmentLookUp;
    private DataListBox location;
    private CRMLookUp position;
    private CRMLookUp project;
    private BrigadaLookUp brigada;
    private DataListBox timeslot;
    private KpiModal employeeFilter;
    private KpiCheckBox active, inActive, noAccess, ess, pending, resigned;
    private MultiSelectLookUp leaveReasons;
    private PagingWidget paging;
    private RadioButton byPosition, byDepartment;
    private String brigadaIds;
    private Date shiftPeriod;
    private Date startDate;
    private Date endDate;
    private final ActionButton search = new ActionButton("", "searchForm__btn");
    private Integer employeeId;

    @UiField
    HTML tableHeader;
    @UiField
    HTMLPanel tablePanel;

    @UiField
    FlowPanel plusButton;
    @UiField
    FlowPanel searchPanel;
    @UiField
    TextBox searchBox;
    @UiField
    MaterialLink resetButton;
    @UiField
    MaterialLink filter;
    @UiField
    HTMLPanel weeks;
    @UiField
    DatePicker monthPicker;
    @UiField
    MaterialDropDown paginationMenu;
    @UiField
    Div pagingWrapper;
    @UiField
    Span dispalying;

    @UiField
    MaterialMenuBar menuBar;

    TerminalAttendanceView() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        onInitialize();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVE_REQUEST_STATUS_CHANGED, (sender, args) -> getDataAttendanceReport(paging.getOffset()));
    }

    public TerminalAttendanceView(Integer employeeId) {
        this.employeeId = employeeId;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        onInitialize();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVE_REQUEST_STATUS_CHANGED, (sender, args) -> getDataAttendanceReport(paging.getOffset()));

    }

    TerminalAttendanceView(String brigadaIds, String period) {
        this.brigadaIds = brigadaIds;
        DateTimeFormat dateTimeFormatForFilter = DateTimeFormat.getFormat("MMM yyyy");
        this.shiftPeriod = dateTimeFormatForFilter.parse(period);
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        onInitialize();
    }

    protected Widget onInitialize() {
        initializeWidgets();
        addButtonListeners();
        getAttendanceReportByFilter();
        return null;
    }


    private void getTeemData() {
        CommonService.App.get().getTeamList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SelectItem[] item) {
                if (item != null && item.length > 1) {
                    employeeFilter.addWidget(department, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
                }
            }
        });
    }

    private void getLocationData() {
        ReportService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SelectItem[] item) {
                if (item != null && item.length > 1) {
                    location.setItems(item);
                }
            }
        });
    }

    private void getPdfTemplates(Div wrapper) {
        HrmsService.App.get().getTerminalAttendancePDFTemplates(new AbstractAsyncCallback<CustomFormItemPdfTemplateList>() {
            @Override
            public void failure(Throwable caught) {

            }

            @Override
            public void success(CustomFormItemPdfTemplateList result) {
                templateList = result;
                if (result != null && result.getItems() != null && result.getItems().length != 0) {
                    MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
                    mdp.setHover(true);
                    mdp.setHoverable(true);
                    pdfTool(mdp);
                    wrapper.add(mdp);
                } else {
                    pdfVersion.addClickHandler(event -> generatePDF(tablePanel, null, true));
                    wrapper.add(pdfVersion);
                }
            }
        });
    }

    private void getPositionData() {
        ReportService.App.get().getEmplyeePositionList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SelectItem[] item) {
                if (item != null && item.length > 0) {
                    employeeFilter.addWidget(position, wfmStrings.position());
                }
            }
        });
    }

    private void getTimeSlotData() {
        AvailabilityService.App.get().getTimeslotList(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] timeslotItems) {
                if (timeslotItems != null && timeslotItems.length > 1) {
                    timeslot.setItems(timeslotItems);
                }
            }
        });
    }

    private void getLeaveReasonData() {
        AllInOneService.App.get().getReasons(Utils.getUserID(), false, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] reasons) {
                if (reasons != null && reasons.length > 1) {
                    leaveReasons.setItems(wfmStrings.status(), reasons);
                }
            }
        });
    }

    private void addButtonListeners() {
        //next button listener
//        monthPicker.addChangeHandler(changeEvent -> {
//            table.removeAttendanceTable();
//            table.setDate(monthPicker.getDate());
//            getDataAttendanceReport(0);
//        });
    }

    private ListingFilterParameter getFilterParams(int start) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        fp.setReasonIds(leaveReasons.getSelectedItemsIdsAsString());
        fp.setName(searchBox.getValue());
        fp.setDepartmentIds(departmentLookUp.getSelectedItemsIdsAsString());
        fp.setOrderByPosition(byPosition.getValue());
        fp.setOrderByDepartment(byDepartment.getValue());
        fp.setLocationId(locationId);
        fp.setProjectId(projectID);
        fp.setTimeSlotID(timeslotId);
        fp.setPositionID(positionId);
        fp.setBrigadaID(brigada.getSelectedItemID());
        fp.setStartDateNC(DateUtils.getDateAndTimeFormatWithDash(this.startDate));
        fp.setEndDateNC(DateUtils.getDateAndTimeFormatWithDash(this.endDate));
        fp.setStart(start);
        fp.setFromTerminal(true);
        fp.setEmployeeId(employeeId);
        return fp;
    }

    private void getDataAttendanceReport(int offset_) {
        LoadingPanel.loading(true);
        AvailabilityService.App.get().getEmployeeAttendanceReport(getFilterParams(offset_), DateUtil.countDays(monthPicker.getDate()), new AbstractAsyncCallback<EmployeeAttendanceReport>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(EmployeeAttendanceReport employeeAttendance) {
                LoadingPanel.loading(false);
                Date date = monthPicker.getDate();
                paging.setTotalCount(employeeAttendance.getTotalCount());
                table.setEmployeeAttendance(employeeAttendance);
                table.setMonthMaxDay(DateUtil.countDays(monthPicker.getDate()));
                table.setMonthName(monthNameFormatter.format(monthPicker.getDate()));
                table.setCurrentDay(new Date().getDate());
                table.setMonthYear(monthYearFormatter.format(monthPicker.getDate()));
                table.generateTerminalTable(startDate, endDate);
                String orderBy = null;
                if (byDepartment.getValue()) {
                    orderBy = wfmStrings.department();
                } else if (byPosition.getValue()) {
                    orderBy = wfmStrings.position();
                }
                tableHeader.setHTML(table.getTerminalTableHeaderHtml(orderBy, startDate, endDate));
                table.setTerminalDate(date, new Date(date.getYear(), date.getMonth(), 1), new Date(date.getYear(), date.getMonth() + 1, 0));
                table.drawTableBodyHtml(true, startDate, endDate);

                getFilterParams(0).setVisableAll(false);
            }
        });
    }

    private void initializeWidgets() {
        plusButton.add(getActionButton());

        MaterialIcon hideIcon = new MaterialIcon();
        hideIcon.setClass("ficon--chevron-left");
        hideIcon.setVisible(false);
        hideIcon.addClickHandler(e -> {
            hideIcon.setVisible(false);
            if (searchPanel.getStyleName().contains("active")) {
                searchPanel.removeStyleName("active");
            } else {
                searchPanel.addStyleName("active");
                searchBox.setFocus(true);
            }
        });

        searchPanel.addStyleName("searchForm");
        search.ensureDebugId("search_button");
        search.add(new SvgIcon(SvgEnum.search));
        search.addClickHandler(event -> {
            hideIcon.setVisible(true);
            searchPanel.addStyleName("active");
            searchBox.setFocus(true);
        });
        searchBox.setMaxLength(255);
        searchBox.ensureDebugId("searchBox");
        searchBox.setPlaceHolder(wfmStrings.search());
        searchBox.addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                getFilterParams(0).setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
                if (getFilterParams(0).getFacetFilter() != null) {
                    getFilterParams(0).getFacetFilter().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
                }
                getDataAttendanceReport(0);
            }

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
            searchBox.setValue("");
            getFilterParams(0).setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
            if (getFilterParams(0).getFacetFilter() != null) {
                getFilterParams(0).getFacetFilter().setSearchKey(searchBox.getText() != null ? searchBox.getText().trim() : searchBox.getText());
            }
            getDataAttendanceReport(0);

        });
        searchPanel.add(xSearch);
        searchPanel.add(hideIcon);

        monthPicker.setDateTimeFormat(DateTimeFormat.getFormat("MMMM yyyy"));
        Date newDate = new Date();
        monthPicker.setDate(newDate);
        monthPicker.setOnlyMonthFormat(true);

        AtomicReference<WfmButton2[]> weekButtons = new AtomicReference<>(new WfmButton2[6]);
        List<WeekInfo> weeksOfMonth = getWeeksOfMonth(newDate);

        WfmButton2 monthButton = new WfmButton2();
        monthButton.setStyle("margin-right: 2px;");
        monthButton.setText(wfmStrings.currentMonth());
        monthButton.addClickHandler(clickEvent -> {
            Date date = new Date();
            table.removeAttendanceTable();
            table.setDate(date);
            monthPicker.setDate(date);

            startDate = new Date(date.getYear(), date.getMonth(), 1);
            endDate = new Date(date.getYear(), date.getMonth() + 1, 0);

            for (WfmButton2 btn : weekButtons.get()) {
                btn.setStyleName(WfmButton2.BTN_DEFAULT);
            }

            monthButton.setStyleName(WfmButton2.BTN_PRIMARY);
            weeks.clear();
            weeks.add(monthButton);

            List<WeekInfo> newWeeksOfMonth = getWeeksOfMonth(date);
            for (int i = 0; i < newWeeksOfMonth.size() && i < weekButtons.get().length; i++) {
                WeekInfo weekInfo = newWeeksOfMonth.get(i);
                weekButtons.get()[i].setText(weekInfo.weekOfYear + "-" + wfmStrings.weekShort());
                weekButtons.get()[i].setTitle(weekInfo.weekOfYear + "");
                weekButtons.get()[i].setStyleName(WfmButton2.BTN_DEFAULT);
                weeks.add(weekButtons.get()[i]);
            }

            getDataAttendanceReport(0);
        });
        weeks.add(monthButton);

        for (int i = 0; i < weekButtons.get().length; i++) {
            weekButtons.get()[i] = new WfmButton2();
            weekButtons.get()[i].setStyle("margin-right: 2px;");
            int idx = i;
            final int finalI = i;
            weekButtons.get()[i].addClickHandler(clickEvent -> {
                monthButton.setStyleName(WfmButton2.BTN_DEFAULT);
                for (WfmButton2 btn : weekButtons.get()) {
                    btn.setStyleName(WfmButton2.BTN_DEFAULT);
                }
                Date[] weekStartEnd = getWeekMondayAndSunday(monthPicker.getDate().getYear() + 1900, Integer.parseInt(weekButtons.get()[finalI].getTitle()));
                startDate = weekStartEnd[0];
                endDate = weekStartEnd[1];
                getDataAttendanceReport(0);
                weekButtons.get()[idx].setStyleName(WfmButton2.BTN_PRIMARY);
            });
        }

        for (int i = 0; i < weeksOfMonth.size() && i < weekButtons.get().length; i++) {
            WeekInfo weekInfo = weeksOfMonth.get(i);
            weekButtons.get()[i].setText(weekInfo.weekOfYear + "-" + wfmStrings.weekShort());
            weekButtons.get()[i].setTitle(weekInfo.weekOfYear + "");
            if (!newDate.before(weekInfo.startDate) && !newDate.after(weekInfo.endDate)) {
                weekButtons.get()[i].setStyleName(WfmButton2.BTN_PRIMARY);
                startDate = weekInfo.startDate;
                endDate = weekInfo.endDate;
            }
            weeks.add(weekButtons.get()[i]);
        }

        monthPicker.addChangeHandler(changeEvent -> {
            table.removeAttendanceTable();
            Date date = monthPicker.getDate();
            table.setDate(date);
            for (WfmButton2 btn : weekButtons.get()) {
                btn.setStyleName(WfmButton2.BTN_DEFAULT);
            }
            weeks.clear();
            startDate = new Date(date.getYear(), date.getMonth(), 1);
            endDate = new Date(date.getYear(), date.getMonth() + 1, 0);
            List<WeekInfo> newWeeksOfMonth = getWeeksOfMonth(date);
            monthButton.setStyleName(WfmButton2.BTN_DEFAULT);
            weeks.add(monthButton);
            for (int i = 0; i < newWeeksOfMonth.size() && i < weekButtons.get().length; i++) {
                WeekInfo weekInfo = newWeeksOfMonth.get(i);
                weekButtons.get()[i].setText(weekInfo.weekOfYear + "-" + wfmStrings.weekShort());
                weekButtons.get()[i].setTitle(weekInfo.weekOfYear + "");
                weekButtons.get()[i].setStyleName(WfmButton2.BTN_DEFAULT);
                weeks.add(weekButtons.get()[i]);

            }

            getDataAttendanceReport(0);
        });

        if (shiftPeriod != null) {
            monthPicker.setDate(shiftPeriod);
        }

        departmentLookUp = new MultiSelectDepartmentLookUp();
        departmentLookUp.ensureDebugId("attendance_report_department");
        departmentLookUp.getSuggestBox().addFocusListener(new FocusListener() {
            @Override
            public void onFocus(Widget widget) {
                ListingFilterParameter filterParameter = new ListingFilterParameter();
                if (departmentLookUp.getLastSelectedItemId() != null) {
                    filterParameter.setDepartmentId(departmentLookUp.getLastSelectedItemId());
                    filterParameter.setFromMultiDepartment(true);
                    departmentLookUp.onLookUpService(filterParameter);
                }
            }

            @Override
            public void onLostFocus(Widget widget) {
            }
        });

        location = new DataListBox();
        location.ensureDebugId("attendance_report_view_locations");
        location.addValueChangeHandler(widget -> {
            if (location.getSelectedIndex() != 0 && location.isSomethingSelected()) {
                locationId = location.getSelectedItem().getId();
            } else if (location.getSelectedIndex() == 0) {
                locationId = null;
            }
        });

        timeslot = new DataListBox();
        timeslot.addValueChangeHandler(changeEvent -> {
            timeslotId = timeslot.getSelectedId();
        });

        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.setFullSearch(true);
        project.showClearButton();
        project.ensureDebugId("project_dropdown");
        project.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (project.isSelected()) {
                projectID = project.getSelectedItem().getId();
            } else {
                projectID = null;
            }
        });
        project.setClearCommand(() -> {
            project.refreshOracle(true);
            projectID = null;
        });

        position = new CRMLookUp(LookUpConstants.POSITION);
        position.setFullSearch(true);
        position.showClearButton();
        position.ensureDebugId("attendance_report_view_positions");
        position.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (position.isSelected()) {
                positionId = position.getSelectedItem().getId();
            } else {
                positionId = null;
            }
        });
        position.setClearCommand(() -> {
            position.refreshOracle(true);
            positionId = null;
        });

        brigada = new BrigadaLookUp();
        if (brigadaIds != null) {
//            brigada.setSelected(Integer.valueOf(brigadaIds.substring(0,1)));
        }

        MaterialLink newestLink = new MaterialLink();
        MaterialLink oldestLink = new MaterialLink();

        newestLink.setText(wfmStrings.newest());
        oldestLink.setText(wfmStrings.oldest());

        paginationMenu.add(newestLink);
        paginationMenu.add(oldestLink);

        searchBox.addKeyPressHandler(event -> {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                getDataAttendanceReport(0);
            }
        });
        paging = new PagingWidget();
        paging.setLimit(30);
        paging.setPaging((start, limit) -> getDataAttendanceReport(start));
        pagingWrapper.add(paging);

        employeeFilter = new KpiModal();
        employeeFilter.setCloseButton(true);
        employeeFilter.setDismissible(false);
        employeeFilter.setTitle(wfmStrings.filter());
        employeeFilter.setWidth(400);

        leaveReasons = new MultiSelectLookUp() {
            @Override
            public boolean onCondition(String text) {
                return false;
            }
        };
        getLeaveReasonData();

        byPosition = new RadioButton("byPostion", wfmStrings.position());
        byPosition.addClickHandler(clickEvent -> {
            byDepartment.setValue(false);
        });
        byDepartment = new RadioButton("byDepartment", wfmStrings.department());
        byDepartment.addClickHandler(clickEvent -> {
            byPosition.setValue(false);
        });

//        getTeemData();
        employeeFilter.addWidget(departmentLookUp, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
//        getPositionData();
        employeeFilter.addWidget(position, wfmStrings.position());
        getLocationData();
        employeeFilter.addWidget(location, Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));

        employeeFilter.addWidget(brigada, wfmStrings.team());

        if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            employeeFilter.addWidget(project, Property.get(Constants.PROJECT, wfmStrings.project()));
        }

        getTimeSlotData();
        GRow gRow = new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.status(), leaveReasons)), new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.timeslot(), timeslot)));
        employeeFilter.addWidget(gRow, "");


        WfmButton2 apply = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
        apply.addClickHandler(clickEvent -> {
            getAttendanceReportByFilter();
        });

        WfmButton2 filterResetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT);
        filterResetButton.addClickHandler(clickEvent -> {
            reset();
        });

        MaterialLink reset = new MaterialLink();
        reset.addClickHandler(clickEvent -> reset());

        employeeFilter.addButton(reset);
        employeeFilter.addButton(apply);
        employeeFilter.addButton(filterResetButton);

        Icon filterIcon = new Icon();
        filterIcon.addStyleName("ficon--filter");

        filter.add(filterIcon);
        filter.ensureDebugId("filter_button");
        filter.setTooltip(wfmStrings.filter());
        filter.setTooltipPosition(Position.TOP);
        final boolean[] opened = {false};
        filter.addClickHandler(event -> {
            filter.setEnabled(true);
            employeeFilter.open();
            opened[0] = true;
        });

        employeeFilter.addCloseHandler(event -> {
            filter.setEnabled(true);
        });

        Icon restIcon = new Icon();
        restIcon.addStyleName("ficon--repeat");
        resetButton.add(restIcon);
        resetButton.setTooltip(wfmStrings.reset());
        resetButton.setTooltipPosition(Position.TOP);
        resetButton.addClickHandler(event -> {
            reset();
        });

        generateImportExportTool();

        table = new AttendanceTableBeta(monthPicker.getDate(), true);
        tablePanel.add(table);

    }

    private ActionButton getActionButton() {
        ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
        MenuBar menu = new MenuBar(true);

        MenuPopItem adjustmentPopup = new MenuPopItem(projectStrings.timeEntry());
        adjustmentPopup.setCommand(() -> new FingerprintAdjustmentPopup().open());

        MenuPopItem leaveRequest = new MenuPopItem(wfmStrings.leaveRequest());
        leaveRequest.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("availability|add/add/", wfmStrings.leaveRequest()));

        MenuPopItem cashAdvance = new MenuPopItem(wfmStrings.cashAdvance());
        cashAdvance.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("cashAdvance|add/add/", wfmStrings.cashAdvance()));

        MenuPopItem incident = new MenuPopItem(wfmStrings.incident());
        incident.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("incident|add/add/", wfmStrings.incident()));

        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_ATTENDANCE_TABLE_DATA)) {
            menu.addItem(adjustmentPopup);
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST)) {
            menu.addItem(leaveRequest);
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_ADD)) {
            menu.addItem(cashAdvance);
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_INCIDENT)) {
            menu.addItem(incident);
        }

        newItem.setMenu(menu);
        return newItem;
    }

    private ActionButton getAddNewButton(ActionButton.Type type) {
        ActionButton actionButton = new ActionButton("<div class=\"btn btn--new btn--circle\"><svg class=\"icon--plus\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#plus\"></use></svg></div>", type);
        return actionButton;
    }

    public static Date[] getWeekMondayAndSunday(int year, int weekNumber) {
        Date jan4 = new Date(year - 1900, 0, 4);

        int dow = jan4.getDay();
        int offsetFromMonday = (dow + 6) % 7;

        int mondayDayOfMonth = 4 - offsetFromMonday;
        Date week1Monday = new Date(year - 1900, 0, mondayDayOfMonth);
        int mondayDay = week1Monday.getDate() + (weekNumber - 1) * 7;
        Date monday = new Date(
                week1Monday.getYear(),
                week1Monday.getMonth(),
                mondayDay
        );

        Date sunday = new Date(
                monday.getYear(),
                monday.getMonth(),
                monday.getDate() + 6
        );

        return new Date[]{monday, sunday};
    }

    public List<WeekInfo> getWeeksOfMonth(Date date) {
        List<WeekInfo> weeks = new ArrayList<>();

        DateTimeFormat yFmt = DateTimeFormat.getFormat("yyyy");
        DateTimeFormat mFmt = DateTimeFormat.getFormat("MM");
        DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
        int year = Integer.parseInt(yFmt.format(date));
        int month = Integer.parseInt(mFmt.format(date));
        int daysInMonth = getDaysInMonth(year, month);

        for (int day = 1; day <= daysInMonth; ) {
            Date start = dtf.parse(year + "-" + month + "-" + day);
            int dow = start.getDay();
            int isoDow = (dow == 0) ? 7 : dow;

            int diff = isoDow - 1;
            if (diff > 0) {
                int newDay = day - diff;
                if (newDay < 1) newDay = 1;
                start = dtf.parse(year + "-" + month + "-" + newDay);
            }

            int endDay = day + (7 - isoDow);
            if (endDay > daysInMonth) endDay = daysInMonth;
            Date end = dtf.parse(year + "-" + month + "-" + endDay);
            end.setHours(23);
            end.setMinutes(59);
            end.setSeconds(59);
            int weekOfYear = getISOWeekNumber(start);

            weeks.add(new WeekInfo(start, end, weekOfYear));

            day = endDay + 1;
        }

        return weeks;
    }

    public int getISOWeekNumber(Date date) {
        DateTimeFormat yFmt = DateTimeFormat.getFormat("yyyy");
        DateTimeFormat mFmt = DateTimeFormat.getFormat("MM");
        DateTimeFormat dFmt = DateTimeFormat.getFormat("dd");

        int year = Integer.parseInt(yFmt.format(date));
        int month = Integer.parseInt(mFmt.format(date));
        int day = Integer.parseInt(dFmt.format(date));

        int dayOfYear = getDayOfYear(year, month, day);

        int dow = date.getDay();
        int isoDow = (dow == 0) ? 7 : dow;

        return (int) Math.floor((dayOfYear - isoDow + 10) / 7);
    }

    public int getDayOfYear(int year, int month, int day) {
        int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) days[1] = 29;
        int doy = day;
        for (int i = 0; i < month - 1; i++) doy += days[i];
        return doy;
    }

    public int getDaysInMonth(int year, int month) {
        int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (month == 2 && (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))) days[1] = 29;
        return days[month - 1];
    }


    public static class WeekInfo {
        public Date startDate;
        public Date endDate;
        public int weekOfYear;

        public WeekInfo(Date startDate, Date endDate, int weekOfYear) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.weekOfYear = weekOfYear;
        }
    }


    private void getAttendanceReportByFilter() {
        filter.setEnabled(true);
        table.removeAttendanceTable();
        employeeFilter.close();
        table.removeAttendanceTable();
        getDataAttendanceReport(0);
    }

    private void reset() {

        searchBox.setText("");
        monthPicker.setDate(new Date());

        project.clear();
        project.refreshOracle(true);
        /*department.clearAndClearItems();
        department.refreshOracle(true);*/
        departmentLookUp.clear();
        location.clearSelected();
        timeslot.clearSelected();
        position.clear();
        brigada.clear();
        position.refreshOracle(true);
        brigada.refreshOracle(true);
        byPosition.setValue(false);
        byDepartment.setValue(false);

        projectID = null;
        /*teamId = null;*/
        locationId = null;
        timeslotId = null;
        positionId = null;
        brigadaId = null;
        leaveReasons.clear();
        getDataAttendanceReport(0);
    }

    private void generateImportExportTool() {
        if (Utils.hasPermission(PermissionConstants.HRMS_EXPORT_EXCEL_TERMINAL_REPORT) || Utils.hasPermission(PermissionConstants.HRMS_EXPORT_PDF_TERMINAL_REPORT)) {

            MaterialLink ieLink = new MaterialLink();
            ieLink.setTooltip(wfmStrings.importExport());
            ieLink.setTooltipPosition(Position.TOP);
            ieLink.setHref("#");
            ieLink.setClass("btn btn--icon btn--white");

            Icon ieIcon = new Icon();
            ieIcon.setClass("ficon--download-cloud");
            ieLink.add(ieIcon);

            MaterialDropDown menuContainer = new MaterialDropDown(ieLink);
            menuContainer.setClass("dropdown-content--2");
            menuContainer.setBelowOrigin(true);
            ieLink.add(menuContainer);
            menuBar.add(ieLink);


            if (Utils.hasPermission(PermissionConstants.HRMS_EXPORT_EXCEL_TERMINAL_REPORT)) {
                Div wrapper = new Div("java-wrap");
                menuContainer.add(wrapper);
                xlsVersion = getXlsVersion();
                wrapper.add(xlsVersion);
                xlsVersion.ensureDebugId("excel_button");

                MaterialDropDown mdp = new MaterialDropDown(xlsVersion);
                mdp.setHover(true);
                mdp.setHoverable(true);
                excelTool(mdp);
                wrapper.add(mdp);
            }

            if (Utils.hasPermission(PermissionConstants.HRMS_EXPORT_PDF_TERMINAL_REPORT)) {
                Div wrapper = new Div("java-wrap");
                menuContainer.add(wrapper);
                pdfVersion = getPdfVersion();
                wrapper.add(pdfVersion);
                pdfVersion.ensureDebugId("pdf_button");
                getPdfTemplates(wrapper);
            }
        }
    }

//    public MaterialLink getXlsVersion() {
//        if (xlsVersion == null) {
//            xlsVersion = new MaterialLink();
//            MaterialIcon xlsIcon = new MaterialIcon();
//            xlsIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
//            xlsVersion.add(xlsIcon);
//            xlsVersion.setText(wfmStrings.excel());
//            xlsVersion.addClickHandler(event -> {
//                String action = CommandConstants.COMMON_URL + "/terminalAttendanceExcelHandler";
//                Utils.sendPDFOrExcelRequest(tablePanel, action, getRequestParams(), "_blank");
//            });
//        }
//        return xlsVersion;
//    }

    public MaterialLink getXlsVersion() {
        if (xlsVersion == null) {
            xlsVersion = new MaterialLink();
            MaterialIcon xlsIcon = new MaterialIcon();
            xlsIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
            xlsVersion.add(xlsIcon);
            xlsVersion.setText(wfmStrings.excel());
        }
        return xlsVersion;
    }

    public MaterialLink getPdfVersion() {
        if (pdfVersion == null) {
            pdfVersion = new MaterialLink();
            MaterialIcon pdfIcon = new MaterialIcon();
            pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
            pdfVersion.add(pdfIcon);
            pdfVersion.setText(wfmStrings.pdf());
        }
        return pdfVersion;
    }

    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();
        ListingFilterParameter fp = getFilterParams(paging.getOffset());
        parametersMap.put("departmentIds", departmentLookUp.getSelectedItemsIdsAsString());
        parametersMap.put("locationID", locationId == null ? null : String.valueOf(locationId));
        parametersMap.put("positionID", positionId != null ? String.valueOf(positionId) : null);
        parametersMap.put("brigada", String.valueOf(fp.getBrigadaID()));
        parametersMap.put("brigadaIDs", fp.getBrigadaIDs());
        parametersMap.put("projectId", projectID == null ? null : String.valueOf(projectID));
        parametersMap.put("startDate_nc", fp.getStartDateNC());
        parametersMap.put("endDate_nc", fp.getEndDateNC());
        parametersMap.put("params", String.valueOf(DateUtil.countDays(monthPicker.getDate())));
        parametersMap.put("limit", String.valueOf(DateUtil.countDays(startDate, endDate)));

        parametersMap.put("monthName", String.valueOf(monthNameFormatter.format(monthPicker.getDate())));//
//        parametersMap.put("isEssUser", String.valueOf(ess.getValue()));
        parametersMap.put("statusValues", fp.getStatusValues());
        parametersMap.put("name", fp.getName());
        parametersMap.put("day", String.valueOf(new Date().getDate()));
        parametersMap.put("isOrderByPosition", String.valueOf(byPosition.getValue()));
        parametersMap.put("isOrderByDepartment", String.valueOf(byDepartment.getValue()));
        parametersMap.put("reasonIds", leaveReasons.getSelectedItemsIdsAsString());
        parametersMap.put("startDate", String.valueOf(startDate));
        parametersMap.put("endDate", String.valueOf(endDate));
        return parametersMap;
    }

    public void excelTool(MaterialDropDown mdp) {
        MaterialLink template = new MaterialLink(wfmStrings.simple());
        template.addClickHandler(event -> generateExcel(false));
        mdp.add(template);

        MaterialLink template2 = new MaterialLink(wfmStrings.detailed());
        template2.addClickHandler(event -> generateExcel(true));
        mdp.add(template2);
    }

    public void pdfTool(MaterialDropDown mdp) {
        if (templateList == null) {
            return;
        }
        Integer defaultTemplateId = templateList.getDefaultTemplateID();
        if (templateList != null && templateList.getItems() != null && templateList.getItems().length > 0) {
            for (SelectItem pdfItem : templateList.getItems()) {
                MaterialLink widgets = new MaterialLink(pdfItem.getName());
                widgets.addClickHandler(event -> generatePDF(tablePanel, pdfItem.getId(), true));
                mdp.add(widgets);
            }
        }
        MaterialLink widgets = new MaterialLink(wfmStrings.pdfVersion());
        widgets.addClickHandler(event -> generatePDF(tablePanel, defaultTemplateId, true));
        mdp.add(widgets);
    }

    private void generateExcel(boolean isCustom) {
        String action = CommandConstants.COMMON_URL + "/terminalAttendanceExcelHandler";
        HashMap<String, String> requestParams = getRequestParams();

        if (isCustom) {
            requestParams.put("isCustom", "true");
            Utils.sendPDFOrExcelRequest(tablePanel, action, getRequestParams(), "_blank");
        } else {
            Utils.sendPDFOrExcelRequest(tablePanel, action, requestParams, "_blank");
        }
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        HashMap<String, String> parameters = getRequestParams();
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.COMMON_URL + "/terminalAttendancePdfHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }
}
