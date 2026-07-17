package com.edatasite.workforce.gwt.core.client.ui.resourceUtil;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ResourceUtilItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.Date;

import static gwt.material.design.jquery.client.api.JQuery.$;

/**
 * User: Ilhombek
 * Date: 5/16/12
 * Time: 2:23 PM
 */
public class ResourceUtilizationView extends View implements ResourceUtilReportConstants, Constants, FittedContent {

    private Integer departmentID;
    private CRMLookUp departmentLookUp;
    private CRMLookUp employeesLookUp;
    private CRMLookUp projectsLookUp;
    private boolean fromProjectSummary = false;
    private Integer projectID;
    private Integer employeeID;
    private MonthDay monthDay;
    private Date date;
    private ResourceUtilReportTable reportTable;
    private KpiCheckBox timeSlotHours;
    private KpiCheckBox inOutHours;
    private KpiCheckBox timeSheetHours;
    private KpiCheckBox leaveRequestHours;

    private WfmButton2 nextButton;
    private Element monthNameElement;
    private WfmButton2 previousButton;
    private WfmButton2 currentButton;
    public WfmButton2 filterButton;
    private ResourceUtilFilterPopup filterPopup;
    private ResourceUtilizationView utilizationView;
    private GBoxItem expandCollapsItem;
    private boolean isShowOnlyFilledCells = false;
    private boolean isShowActiveUsers = false;
    private String employeeIds = "";
    private String positionIds = "";
    private boolean noPosition = false;
    private boolean enableExpand = true;

    private Boolean fromProject = false;

    @UiField
    HTMLPanel contentTop;
    @UiField
    HTMLPanel content;
    @UiField
    HTMLPanel contentBottom;
    @UiField
    SpanElement spanOptimallyAllocated;
    @UiField
    SpanElement wordOptimallyAllocated;
    @UiField
    SpanElement spanUnderAllocated;
    @UiField
    SpanElement wordUnderAllocated;
    @UiField
    SpanElement spanHoliday;
    @UiField
    SpanElement wordHoliday;
    @UiField
    SpanElement spanOverAllocated;
    @UiField
    SpanElement wordOverAllocated;
    @UiField
    SpanElement spanLeaveRequest;
    @UiField
    SpanElement wordLeaveRequest;
    @UiField
    SpanElement spanUnauthorizedLeaveRequest;
    @UiField
    SpanElement wordUnauthorizedLeaveRequest;
    @UiField
    SpanElement spanTaskDuration;
    @UiField
    SpanElement wordTaskDuration;
    @UiField
    HTMLPanel generalContent;
    @UiField
    HTMLPanel exportPanel;

    interface ResourceUtilizationViewUiBinder extends UiBinder<HTMLPanel, ResourceUtilizationView> {
    }

    public ResourceUtilizationView(Integer projectID) {
        super(RESOURCE_UTIL);
        setDescription(property.getSingular(wfmStrings.resourceUtilization()));
        if (projectID != null) {
            this.fromProjectSummary = true;
        }
        this.projectID = projectID;
    }

    @Override
    public String getIconStyle() {
        return "bgMark resource-work-load";//after change - icon style
    }

    @Override
    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.PM_CONTEXT, PermissionConstants.PM_RESOURCE_UTILIZATION_LIST);
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    boolean isFromProjectSummary() {
        return fromProjectSummary;
    }

    KpiCheckBox getTimeSlotHours() {
        return timeSlotHours;
    }

    public KpiCheckBox getInOutHours() {
        return inOutHours;
    }

    KpiCheckBox getTimeSheetHours() {
        return timeSheetHours;
    }

    KpiCheckBox getLeaveRequestHours() {
        return leaveRequestHours;
    }

    @Override
    protected Widget onInitialize() {
        utilizationView = this;
        ResourceUtilizationViewUiBinder ourUiBinder = GWT.create(ResourceUtilizationViewUiBinder.class);
        add(ourUiBinder.createAndBindUi(this));
        drawInitialize();
        return null;
    }

    private void drawInitialize() {
        //register something code
        monthDay = new MonthDay(0);
        date = monthDay.getDate();
        reportTable = new ResourceUtilReportTable(this, monthDay, date);

        drawInitializeTop();
        drawInitializeContentListeners();
        drawInitializeBottom();

        getOverAllData();
    }

    private void drawInitializeBottom() {
        //register optimally allocated element
        spanOptimallyAllocated.setInnerHTML(" ");
        wordOptimallyAllocated.setInnerHTML(" - " + wfmStrings.optimallyAllocated());
        //register under allocated element
        spanUnderAllocated.setInnerHTML(" ");
        wordUnderAllocated.setInnerHTML(" - " + wfmStrings.underAllocated());
        //register holiday element
        spanHoliday.setInnerHTML(wfmStrings.holidayLetter());
        wordHoliday.setInnerHTML(" " + wfmStrings.holiday());
        //register over allocated element
        spanOverAllocated.setInnerHTML(" ");
        wordOverAllocated.setInnerHTML(" - " + wfmStrings.overAllocated());
        //register leave request element
        spanLeaveRequest.setInnerHTML(wfmStrings.lRLetter());
        wordLeaveRequest.setInnerHTML(" - " + wfmStrings.leave());
        //register unauthorized leave request element
        spanUnauthorizedLeaveRequest.setInnerHTML(wfmStrings.absentLetter());
        wordUnauthorizedLeaveRequest.setInnerHTML(" - " + wfmStrings.absent());
        //register task duration element
        spanTaskDuration.setInnerHTML(" ");
        wordTaskDuration.setInnerHTML(" - " + wfmStrings.taskDuration());

        //exportPanel.add(getExportButton());
    }

    private void drawInitializeContentListeners() {
        //register content logic
        content.add(reportTable);

        //register UI register updates
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_EDIT, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_DELETE, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_MEMBERS_EDIT, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_APPROVAL, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_SUBMIT_FOR_APPROVAL, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_ADD, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_DELETE, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUB_PROJECT_EDIT, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_EDIT, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_MEMBER_ADD, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUB_PROJECT_MEMBER_ADD, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_DELETE, ResourceUtilizationView.this, (sender, args) -> {
            //register selected month button listener logic
            monthDay.dateGenerate(0);
            getOverAllData();
        });

    }

    private void drawInitializeTop() {
        if (projectID != null) {
            fromProject = true;
        }

        GBoxItem navigationButtonsItem = generateNavigationButtons();

        //Department lookup
        departmentLookUp = new CRMLookUp(LookUpConstants.DEPARTMENT);
        departmentLookUp.setFullSearch(true);
        departmentLookUp.showClearButton();
        departmentLookUp.ensureDebugId("Res_utilization_department");
        Integer viewAsId = Utils.getUserMaxRoleID();
        if (viewAsId != null) {
            departmentLookUp.getFilterParametrs().setViewAsId(viewAsId);
        }
        departmentLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (departmentLookUp.getSelectedItemID() != null) {
                departmentID = departmentLookUp.getSelectedItem().getId();
                if (projectID != null) {
                    enableExpand = true;
                }
            } else {
                departmentID = null;
            }
            employeeID = null;
            employeesLookUp.clearOracleItems();
            getOverAllData();
        });
        departmentLookUp.setClearCommand(() -> {
            enableButtons(false);
            departmentID = null;
            refreshView(departmentLookUp);
            if (employeeID == null) {
                refreshView(employeesLookUp);
            }
            if (projectID == null) {
                refreshView(projectsLookUp);
                enableExpand = true;
            }
            getOverAllData();
        });

        //Employee lookup
        employeesLookUp = new CRMLookUp(LookUpConstants.EMPLOYEE);
        employeesLookUp.setFullSearch(true);
        employeesLookUp.showClearButton();
        employeesLookUp.ensureDebugId("Res_utilization_employee");
        employeesLookUp.getFilterParametrs().setNewType(true);
        employeesLookUp.setBeforeSearch(() -> {
            if (departmentID != null) {
                employeesLookUp.getFilterParametrs().setDepartmentId(departmentID);
            }
            if (projectID != null) {
                employeesLookUp.getFilterParametrs().setProjectId(projectID);
            }
        });
        employeesLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (employeesLookUp.getSelectedItemID() != null) {
                employeeID = employeesLookUp.getSelectedItemID();
            } else {
                employeeID = null;
            }
            if (projectID == null) {
                projectsLookUp.clearOracleItems();
            } else {
                enableExpand = true;
            }
            getOverAllData();
        });
        employeesLookUp.setClearCommand(() -> {
            enableButtons(false);
            employeeID = null;
            refreshView(employeesLookUp);
            if (projectID == null) {
                refreshView(projectsLookUp);
            } else {
                enableExpand = true;
            }
            getOverAllData();
        });

        //project lookUp
        projectsLookUp = new CRMLookUp(LookUpConstants.PROJECT);
        projectsLookUp.setFullSearch(true);
        projectsLookUp.showClearButton();
        projectsLookUp.ensureDebugId("Res_utilization_project");
        projectsLookUp.getFilterParametrs().setNewType(true);
        projectsLookUp.getFilterParametrs().setClientId(null);
        projectsLookUp.getFilterParametrs().setStatusID(null);

        projectsLookUp.setBeforeSearch(() -> {
            projectsLookUp.getFilterParametrs().setStartDate(monthDay.getStartDate());
            projectsLookUp.getFilterParametrs().setEndDate(monthDay.getEndDate());
            projectsLookUp.getFilterParametrs().setDepartmentId(departmentID);
            projectsLookUp.getFilterParametrs().setEmployeeId(employeeID);
        });
        projectsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (projectsLookUp.getSelectedItem() != null) {
                projectID = projectsLookUp.getSelectedItemID();
                enableExpand = true;
                expandCollapsItem.setVisible(true);
            } else {
                projectID = null;
                expandCollapsItem.setVisible(false);
            }
            if (employeeID == null) {
                employeesLookUp.clearOracleItems();
            }
            getOverAllData();
        });
        projectsLookUp.setClearCommand(() -> {
            enableButtons(false);
            projectID = null;
            expandCollapsItem.setVisible(false);
            refreshView(projectsLookUp);
            if (employeeID == null) {
                refreshView(employeesLookUp);
            }
            getOverAllData();
        });

        // Filter employees by position and show all filled hours filter
        filterButton = new WfmButton2("", WfmButton2.BTN_WHITE, "ficon--filter");
        filterButton.removeHasiconLeftStyle();
        filterButton.addStyleName("btn--icon");
        filterButton.setTitle(wfmStrings.filterBy() + " " + wfmStrings.position());

        //register timeSlot hours checkBox
        timeSlotHours = new KpiCheckBox(getCustomCheckBoxStyle(wfmStrings.timeSlotHoursOnly()), true);
        timeSlotHours.addValueChangeHandler(booleanValueChangeEvent -> {
            //register show/hide option timeSlot hours
            reportTable.getResourceUtilReportData().showHideTimeSlotHours(booleanValueChangeEvent.getValue(), null);

        });
        //register inOut hours checkBox
        inOutHours = new KpiCheckBox(getCustomCheckBoxStyle(wfmStrings.inHours()), true);
        inOutHours.addValueChangeHandler(booleanValueChangeEvent -> {
            //register show/hide option timeSlot hours
            reportTable.getResourceUtilReportData().showHideInOutHours(booleanValueChangeEvent.getValue(), null);

        });
        //register timeSheet hours checkBox
        timeSheetHours = new KpiCheckBox(getCustomCheckBoxStyle(wfmStrings.timesheetHours()), true);
        timeSheetHours.addValueChangeHandler(booleanValueChangeEvent -> {
            //register show/hide option timeSheet hours
            reportTable.getResourceUtilReportData().showHideOverallTimeSheetHours(booleanValueChangeEvent.getValue(), null);
            reportTable.getResourceUtilReportData().showHideTimeSheetHours(booleanValueChangeEvent.getValue(), null, null);
        });

        //register timeSheet hours checkBox
        leaveRequestHours = new KpiCheckBox(getCustomCheckBoxStyle(wfmStrings.leaveHoursOnly()), true);
        leaveRequestHours.addValueChangeHandler(booleanValueChangeEvent -> {
            //register show/hide option timeSheet hours
            reportTable.getResourceUtilReportData().showHideOverallLeaveRequestHours(booleanValueChangeEvent.getValue(), null);
        });

        //filter
        filterPopup = new ResourceUtilFilterPopup(utilizationView);
        filterButton.addClickHandler(e -> {
            filterButton.setEnabled(false);
            filterPopup.open();
        });
        filterPopup.addCloseHandler(event -> filterButton.setEnabled(true));

        MaterialMenuBar showMenuBar = new MaterialMenuBar();
        showMenuBar.setClass("dropdown-kit--arrow--below");

        MaterialLink showLink = new MaterialLink(wfmStrings.show());
        showLink.addStyleName("btn btn--default");
        showLink.setDataAttribute("alignment", "right");

        MaterialDropDown showMenuContainer = new MaterialDropDown(showLink);
        showMenuContainer.setClass("dropdown-content--2");
        showMenuContainer.setBelowOrigin(true);
        showMenuContainer.setDataAttribute("alignment", "right");

        $(showMenuContainer.getElement()).click(event -> {
            event.stopPropagation();
            return true;
        });

        MaterialLink timeSlotLink = new MaterialLink();
        timeSlotLink.add(timeSlotHours);

        MaterialLink inOutLink = new MaterialLink();
        inOutLink.add(inOutHours);

        MaterialLink timesheetLink = new MaterialLink();
        timesheetLink.add(timeSheetHours);

        MaterialLink leaveRequestLink = new MaterialLink();
        leaveRequestLink.add(leaveRequestHours);

        showLink.add(showMenuContainer);
        showMenuContainer.add(timeSlotLink);
        showMenuContainer.add(inOutLink);
        showMenuContainer.add(timesheetLink);
        showMenuContainer.add(leaveRequestLink);
        showMenuBar.add(showLink);

        expandCollapsItem = new GBoxItem();
        expandCollapsItem.setStyleWidthFree(true);
        expandCollapsItem.setStyleNoBorder(true);
        WfmButton2 expandCollapsButton = new WfmButton2(wfmStrings.expandCollaps(), WfmButton2.BTN_DEFAULT);
        expandCollapsButton.addClickHandler(event -> {
            //register collaps or expand all rows
            reportTable.generateExpandCollapsRows(enableExpand);
            reportTable.generateTOP(monthNameElement);
            reportTable.generateBottom();
            if (enableExpand) {
                enableExpand = false;
            } else {
                enableExpand = true;
            }
        });
        expandCollapsItem.setComponent(expandCollapsButton);
        expandCollapsItem.setVisible(false);

        GBox topPanel = new GBox();
        topPanel.setStyleNoPadding(true);
        topPanel.addStyleName("group-box--united");

        GBoxRow topPanelRow = new GBoxRow();
        topPanelRow.add(navigationButtonsItem);

        topPanelRow.add(new GBoxItem(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), departmentLookUp));
        topPanelRow.add(new GBoxItem(wfmStrings.employee(), employeesLookUp));

        GBoxItem projectItem = new GBoxItem(Property.get(Constants.PROJECT, wfmStrings.project()), projectsLookUp);
        projectItem.addStyleName("group-box__item--split-right");
        topPanelRow.add(projectItem);

        MaterialPanel pnlButtons = new MaterialPanel("operPanel__btn-groups");

        exportPanel.add(getExportButton());
        pnlButtons.add(filterButton);
        pnlButtons.add(exportPanel);

        GBoxItem filterExportPanel = new GBoxItem("", pnlButtons);
        filterExportPanel.setStyleWidthFree(true);
        filterExportPanel.setStyleNoBorder(true);
        filterExportPanel.setStyleSplitRight(true);

        topPanelRow.add(filterExportPanel);

        GBoxItem showMenuItem = new GBoxItem("", showMenuBar);
        showMenuItem.setStyleNoBorder(true);
        showMenuItem.setStyleWidthFree(true);
        topPanelRow.add(showMenuItem);
        topPanelRow.add(expandCollapsItem);
        topPanel.add(topPanelRow);

        contentTop.add(topPanel);

    }

    private MaterialLink getExportButton() {
        MaterialLink exportLink = new MaterialLink();
        exportLink.ensureDebugId("excel");
        exportLink.addStyleName("btn btn--icon btn--white");

        MaterialIcon exportIcon = new MaterialIcon();
        exportIcon.setStyleName("ficon--download-cloud");
        exportLink.add(exportIcon);
        exportLink.setTooltip(wfmStrings.excel());
        exportLink.addClickHandler(event -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadResourceUtilizationExcel";
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setDepartmentId(departmentID);
            filterParameter.setEmployeeId(employeeID);
            filterParameter.setProjectId(projectID);
            filterParameter.setStartDateNC(DateUtils.getDateAndTimeFormatWithDash(monthDay.getStartDate()));
            filterParameter.setEndDateNC(DateUtils.getDateAndTimeFormatWithDash(monthDay.getEndDate()));
            String summsParams = String.valueOf(monthDay.getMaxMonthDay());
            summsParams = summsParams + (timeSlotHours.getValue() ? "@true" : "@false");
            summsParams = summsParams + (inOutHours.getValue() ? "@true" : "@false");
            summsParams = summsParams + (timeSheetHours.getValue() ? "@true" : "@false");
            summsParams = summsParams + (leaveRequestHours.getValue() ? "@true" : "@false");
            summsParams = summsParams + ("@" + monthDay.getMonthNameWithYear());
            filterParameter.setParams(summsParams);
            //from filter popup
            filterParameter.setPositionIDs(positionIds);
            filterParameter.setNoPosition(noPosition);
            filterParameter.setShowFilledCells(isShowOnlyFilledCells);
            filterParameter.setShowActive(isShowActiveUsers);

            Utils.sendPDFOrExcelRequest(contentTop, excelURL, filterParameter.getRequestParams(), "_blank");
        });
        return exportLink;
    }

    private void refreshView(CRMLookUp lookUp) {
        lookUp.clearAndClearItems();
        lookUp.refreshOracle(true);
    }

    private void enableDataListBoxes(boolean e) {
        if (departmentLookUp != null) {
            departmentLookUp.setEnabled(e);
        }
        if (employeesLookUp != null) {
            employeesLookUp.setEnabled(e);
        }
        if (projectsLookUp != null && fromProject) {
            projectsLookUp.setEnabled(false);
        } else if (projectsLookUp != null) {
            projectsLookUp.setEnabled(e);
        }
    }

    void getOverAllData() {
        //register content logic
        reportTable.removeResourceUtilTable();
        //register over all resource utilization data
        enableDataListBoxes(false);
        if (generalContent.getParent() != null) {
            generalContent.getParent().getElement().getStyle().setCursor(Style.Cursor.WAIT);
        } else {
            generalContent.getElement().getStyle().setCursor(Style.Cursor.WAIT);
        }
        LoadingPanel.loading(true);
        String startDate = DateUtils.getDateAndTimeFormatWithDash(monthDay.getStartDate());
        String endDate = DateUtils.getDateAndTimeFormatWithDash(monthDay.getEndDate());

        ListingFilterParameter fp = new ListingFilterParameter();
        Integer viewAsId = Utils.getUserMaxRoleID();
        if (viewAsId == null) {
            viewAsId = Constants.MEM;
        }
        fp.setDepartmentId(departmentID);
        fp.setEmployeeId(employeeID);
        fp.setProjectId(projectID);
        fp.setStartDateNC(startDate);
        fp.setEndDateNC(endDate);
        fp.setSelectedMonth(monthDay.getMaxMonthDay());
        fp.setViewAsId(viewAsId);
        fp.setPositionIDs(positionIds);
        fp.setNoPosition(noPosition);
        fp.setShowFilledCells(isShowOnlyFilledCells);
        fp.setShowActive(isShowActiveUsers);
        AllInOneService.App.get().getResourceUtilization(fp, new AbstractAsyncCallback<ResourceUtilItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableDataListBoxes(true);
                if (generalContent.getParent() != null) {
                    generalContent.getParent().getElement().getStyle().clearCursor();
                } else {
                    generalContent.getElement().getStyle().clearCursor();
                }
            }

            @Override
            public void success(ResourceUtilItem result) {
                //
                LoadingPanel.loading(false);
                enableDataListBoxes(true);
                employeeIds = result.getEmployeeIds();
                if (generalContent.getParent() != null) {
                    generalContent.getParent().getElement().getStyle().clearCursor();
                } else {
                    generalContent.getElement().getStyle().clearCursor();
                }

                reportTable.setResourceUtilItem(result);
                reportTable.setMonthMaxDay(monthDay.getMaxMonthDay());
                reportTable.setMonthName(monthDay.getMonthNameWithYear());
                reportTable.setCurrentDay(monthDay.getCurrentDate());
                reportTable.setDate(monthDay.getDate());
                reportTable.generateTable();
                reportTable.generateTOP(monthNameElement);
                reportTable.generateBottom();

                monthNameElement.setInnerHTML(reportTable.getMonthName());
                enableButtons(true);
            }
        });
    }

    private GBoxItem generateNavigationButtons() {
        MaterialPanel btnGroup = new MaterialPanel("btn-group");

        previousButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--chevron-left");
        previousButton.removeStyleName("hasicon--left");
        previousButton.addStyleName("btn--icon");
        previousButton.setTitle(wfmStrings.previous());
        previousButton.addClickHandler(event -> {
            //register previous button listener logic
            enableButtons(false);
            enableExpand = true;
            monthDay.dateGenerate(-1);
            date = monthDay.getDate();
            getOverAllData();


        });
        btnGroup.add(previousButton);

        currentButton = new WfmButton2(wfmStrings.currentMonth(), WfmButton2.BTN_WHITE);
        currentButton.addClickHandler(event -> {
            //register currenct button listener logic
            enableButtons(false);
            enableExpand = true;
            monthDay.dateGenerate(0);
            date =monthDay.getDate();
            getOverAllData();

        });
        btnGroup.add(currentButton);

        nextButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--chevron-right");
        nextButton.removeStyleName("hasicon--left");
        nextButton.addStyleName("btn--icon");
        nextButton.setTitle(wfmStrings.nextstr());
        nextButton.addClickHandler(event -> {
            //register next button listener logic
            enableButtons(false);
            enableExpand = true;
            monthDay.dateGenerate(1);
            date = monthDay.getDate();
            getOverAllData();
        });
        btnGroup.add(nextButton);

        monthNameElement = DOM.createSpan();
        monthNameElement.addClassName(CLASS_RESOURCE_REPORT_HRight_MONTH);

        GBoxItem gBoxItem = new GBoxItem(btnGroup);
        gBoxItem.setStyleSplitRight(true);
        gBoxItem.setStyleWidthFree(true);
        return gBoxItem;
    }

    private String getCustomCheckBoxStyle(String text) {
        return (true ? "&nbsp;" : "") + "<span>" + text + "</span>";
    }

    private void enableButtons(boolean enable) {
        previousButton.setEnabled(enable);
        currentButton.setEnabled(enable);
        nextButton.setEnabled(enable);
        departmentLookUp.setEnabled(enable);
        employeesLookUp.setEnabled(enable);
        projectsLookUp.setEnabled(enable);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        RootPanel.get().addStyleName("has-reporting-filters-panel");
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        RootPanel.get().removeStyleName("has-reporting-filters-panel");
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

    boolean isShowOnlyFilledCells() {
        return isShowOnlyFilledCells;
    }

    void setShowOnlyFilledCells(boolean isShowOnlyFilledCells) {
        this.isShowOnlyFilledCells = isShowOnlyFilledCells;
    }

    void setShowActiveUsers(boolean isShowActiveUsers) {
        this.isShowActiveUsers = isShowActiveUsers;
    }

    public String getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(String employeeIds) {
        this.employeeIds = employeeIds;
    }

    void setPositionIds(String positionIds) {
        this.positionIds = positionIds;
    }

    void setNoPosition(boolean noPosition) {
        this.noPosition = noPosition;
    }

    @Override
    public String getPropertyCode() {
        return RESOURCE_UTIL;
    }
}
