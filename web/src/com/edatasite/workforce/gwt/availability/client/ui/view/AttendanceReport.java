package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableBeta;
import com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.uploadPopup.AttendanceUploadPopup;
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
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.paging.PagingWidget;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiBrigadaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectDepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
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

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Omonullo on 3/15/2017.
 */
public class AttendanceReport extends Composite implements Constants {

    String PDF_VERSION = "PDF_VERSION";

    interface AttendanceReportUiBinder extends UiBinder<HTMLPanel, AttendanceReport> {
    }

    private static final AttendanceReportUiBinder ourUiBinder = GWT.create(AttendanceReportUiBinder.class);

    public static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final DateTimeFormat monthNameFormatter = DateTimeFormat.getFormat("MMMM");
    private static final DateTimeFormat monthYearFormatter = DateTimeFormat.getFormat("MM/yyyy");

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
    private MultiBrigadaLookUp brigada;
    private DataListBox timeslot;
    private KpiModal employeeFilter;
    private KpiCheckBox active, inActive, noAccess, ess, pending, resigned;
    private MultiSelectLookUp leaveReasons;
    private PagingWidget paging;
    private RadioButton byPosition, byDepartment;
    private String brigadaIds;
    private Date shiftPeriod;

    @UiField
    HTML tableHeader;
    @UiField
    HTMLPanel tablePanel;
    @UiField
    DatePicker monthPicker;
    @UiField
    Span monthPickerLabel;
    @UiField
    MaterialDropDown paginationMenu;
    @UiField
    MaterialLink filter;
    @UiField
    MaterialLink resetButton;
    @UiField
    MaterialMenuBar menuBar;
    @UiField
    TextBox nameSearch;
    @UiField
    Span dispalying;
    @UiField
    Button searchButton;
    @UiField
    Div pagingWrapper;

    AttendanceReport() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        onInitialize();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVE_REQUEST_STATUS_CHANGED, (sender, args) -> getDataAttendanceReport(paging.getOffset()));
    }

    AttendanceReport(String brigadaIds, String period) {
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
        HrmsService.App.get().getAttendanceReportPDFTemplates(new AbstractAsyncCallback<CustomFormItemPdfTemplateList>() {
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
        monthPicker.addChangeHandler(changeEvent -> {
            table.removeAttendanceTable();
            table.setDate(monthPicker.getDate());
            getDataAttendanceReport(0);
        });
    }

    private ListingFilterParameter getFilterParams(int start) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        fp.setReasonIds(leaveReasons.getSelectedItemsIdsAsString());
        fp.setName(nameSearch.getValue());
        fp.setDepartmentIds(departmentLookUp.getSelectedItemsIdsAsString());
        /*fp.setDepartmentId(teamId);*/
        fp.setOrderByPosition(byPosition.getValue());
        fp.setOrderByDepartment(byDepartment.getValue());
        fp.setLocationId(locationId);
        fp.setProjectId(projectID);
        fp.setTimeSlotID(timeslotId);
        fp.setPositionID(positionId);
        fp.setBrigadaIDs(brigadaIds != null ? brigadaIds : brigada.getSelectedItemsIdsAsString());
        String startDate = DateUtils.getDateAndTimeFormatWithDash(DateUtil.getMonthFirstDay(monthPicker.getDate()));
        String endDate = DateUtils.getDateAndTimeFormatWithDash(DateUtil.getMonthLastDateWithTime(monthPicker.getDate()));
        fp.setStartDateNC(startDate);
        fp.setEndDateNC(endDate);
        fp.setStart(start);
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

                paging.setTotalCount(employeeAttendance.getTotalCount());
                table.setEmployeeAttendance(employeeAttendance);
                table.setMonthMaxDay(DateUtil.countDays(monthPicker.getDate()));
                table.setMonthName(monthNameFormatter.format(monthPicker.getDate()));
                table.setCurrentDay(new Date().getDate());
                table.setMonthYear(monthYearFormatter.format(monthPicker.getDate()));
                table.generateTable();
                String orderBy = null;
                if (byDepartment.getValue()) {
                    orderBy = wfmStrings.department();
                } else if (byPosition.getValue()) {
                    orderBy = wfmStrings.position();
                }
                tableHeader.setHTML(table.getTableHeaderHtml(orderBy));
                table.drawTableBodyHtml(false,null,null);
                table.setDate(monthPicker.getDate());
                getFilterParams(0).setVisableAll(false);
            }
        });
    }

    private void initializeWidgets() {
        searchButton.addClickHandler(clickEvent -> getDataAttendanceReport(paging.getOffset()));
        monthPicker.setDateTimeFormat(DateTimeFormat.getFormat("MMM yyyy"));

        monthPicker.setDate(new Date());
        monthPickerLabel.setText(wfmStrings.month());
        monthPicker.setOnlyMonthFormat(true);

        /*department = new CRMLookUp(LookUpConstants.DEPARTMENT);
        department.setFullSearch(true);
        department.showClearButton();
        department.ensureDebugId("attendance_report_view_departments");
        department.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (department.isSelected()) {
                teamId = department.getSelectedItem().getId();
            } else {
                teamId = null;
            }
        });
        department.setClearCommand(() -> {
            department.refreshOracle(true);
            teamId = null;
        });*/

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

        brigada = new MultiBrigadaLookUp();
        if (brigadaIds != null) {
//            brigada.setSelected(Integer.valueOf(brigadaIds.substring(0,1)));
        }
        brigada.getSuggestBox().addFocusListener(new FocusListener() {
            @Override
            public void onFocus(Widget widget) {
                ListingFilterParameter filterParameter = new ListingFilterParameter();
                if (brigada.getLastSelectedItemId() != null) {
                    filterParameter.setDepartmentId(brigada.getLastSelectedItemId());
                    filterParameter.setFromMultiDepartment(true);
                    brigada.onLookUpService(filterParameter);
                }
            }

            @Override
            public void onLostFocus(Widget widget) {
            }
        });

        MaterialLink newestLink = new MaterialLink();
        MaterialLink oldestLink = new MaterialLink();

        newestLink.setText(wfmStrings.newest());
        oldestLink.setText(wfmStrings.oldest());

        paginationMenu.add(newestLink);
        paginationMenu.add(oldestLink);

        nameSearch.addKeyPressHandler(event -> {
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

        GRow row = new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup(byDepartment)), new GColumn(GColumnEnum.COL_6, new FormGroup(byPosition)));
        employeeFilter.addWidget(row, wfmStrings.groupBy());

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

        table = new AttendanceTableBeta(monthPicker.getDate(),false);
        tablePanel.add(table);

    }

    private void getAttendanceReportByFilter() {
        filter.setEnabled(true);
        table.removeAttendanceTable();
        employeeFilter.close();
        table.removeAttendanceTable();
        getDataAttendanceReport(0);
    }

    private void reset() {

        nameSearch.setText("");
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
        MaterialLink ieLink = new MaterialLink();//import/export button link for listing top panel
        ieLink.setTooltip(wfmStrings.importExport());
        ieLink.setTooltipPosition(Position.TOP);
        ieLink.setHref("#");
        ieLink.setClass("btn btn--icon btn--white");

        Icon ieIcon = new Icon();//import/export icon for listing top panel
        ieIcon.setClass("ficon--download-cloud");
        ieLink.add(ieIcon);

        MaterialDropDown menuContainer = new MaterialDropDown(ieLink);
        menuContainer.setClass("dropdown-content--2");
        menuContainer.setBelowOrigin(true);
        ieLink.add(menuContainer);

        menuBar.add(ieLink);

        ImportFileActionLink link = new ImportFileActionLink();
        link.addClickHandler(ch -> new AttendanceUploadPopup(departmentLookUp.getSelectedItemsIdsAsString(), locationId, projectID));
        link.ensureDebugId("import_button");
        if (Utils.hasPermission(PermissionConstants.HRMS_IMPORT_ATTENDANCE_DATA)) {
            menuContainer.add(link);
        }

        xlsVersion = getXlsVersion();
        xlsVersion.ensureDebugId("excel_button");
        if (Utils.hasPermission(PermissionConstants.HRMS_EXPORT_ATTENDANCE_DATA)) {
            menuContainer.add(xlsVersion);
        }


        if (Utils.hasPermission(PermissionConstants.HRMS_EXPORT_PDF_ATTENDANCE_DATA)) {
            Div wrapper = new Div("java-wrap");
            menuContainer.add(wrapper);
            pdfVersion = getPdfVersion();
            wrapper.add(pdfVersion);
            pdfVersion.ensureDebugId("pdf_button");
            getPdfTemplates(wrapper);
        }

    }

    public MaterialLink getXlsVersion() {
        if (xlsVersion == null) {
            xlsVersion = new MaterialLink();
            MaterialIcon xlsIcon = new MaterialIcon();
            xlsIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
            xlsVersion.add(xlsIcon);
            xlsVersion.setText(wfmStrings.excel());
            xlsVersion.addClickHandler(event -> {
                String action = CommandConstants.COMMON_URL + "/attendanceReportExcelHandler";
                Utils.sendPDFOrExcelRequest(tablePanel, action, getRequestParams(), "_blank");
            });
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
        parametersMap.put("brigadaIDs", fp.getBrigadaIDs());
        parametersMap.put("projectId", projectID == null ? null : String.valueOf(projectID));
        parametersMap.put("startDate_nc", fp.getStartDateNC());
        parametersMap.put("endDate_nc", fp.getEndDateNC());
        parametersMap.put("params", String.valueOf(DateUtil.countDays(monthPicker.getDate())));//
        parametersMap.put("monthName", String.valueOf(monthNameFormatter.format(monthPicker.getDate())));//
//        parametersMap.put("isEssUser", String.valueOf(ess.getValue()));
        parametersMap.put("statusValues", fp.getStatusValues());
        parametersMap.put("name", fp.getName());
        parametersMap.put("day", String.valueOf(new Date().getDate()));
        parametersMap.put("isOrderByPosition", String.valueOf(byPosition.getValue()));
        parametersMap.put("isOrderByDepartment", String.valueOf(byDepartment.getValue()));
        parametersMap.put("reasonIds", leaveReasons.getSelectedItemsIdsAsString());
        return parametersMap;
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

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        HashMap<String, String> parameters = getRequestParams();
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.COMMON_URL + "/attendanceReportPdfHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

}
