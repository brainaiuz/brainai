package com.edatasite.workforce.gwt.timesheet.client.ui.view;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.hijri.HijriUtils;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.resourceUtil.MonthDay;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**

 */
public class MonthlyTimesheetView extends View implements Constants {

    protected static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private MonthDay monthDayObject;
    private GBoxRow toolBar;
    private CRMLookUp projectLookup;
    private WfmButton2 prev;
    private WfmButton2 saveButton;
    private WfmButton2 next;
//    private WfmButton2 currentMonth;
    private WfmButton2 monthName;
    private GBoxItem importButtonItemBox;
    private Integer projectId;
    private KpiCheckBox notFilledTimesheet;
    boolean firstPreviousPressed = false;
    private ListDataProvider<MonthlyTimesheetItem> dataProvider = null;
    private EmployeeLookUpWithCode projectEmployeeLookUp;
    private HashMap<Integer, Integer[]> employeeContractedHoursMap = null;
    private final String viewName = "monthly_timesheet_view_";
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private HTMLPanel exportPanel;

    protected KpiDataGrid<MonthlyTimesheetItem> cellTable = null;
    private final ProvidesKey<MonthlyTimesheetItem> KEY_PROVIDER = new ProvidesKey<MonthlyTimesheetItem>() {
        public Object getKey(MonthlyTimesheetItem item) {
            return item == null ? null : item.getEmployeeID() + "_" + item.getProjectEmployeeID() + "_" + monthDayObject.getMonth() + "_" + monthDayObject.getYear() + "_" + (new Date().getTime());
        }
    };

    public MonthlyTimesheetView() {
        super(MONTHLYTIMESHEET);
        setDescription(property.getPlural(wfmStrings.monthlyTimeSheet()));
    }

    @Override
    public String getPropertyCode() {
        return MONTHLYTIMESHEET;
    }

    @Override
    public String getIconStyle() {
        return "bgMark icon-fasttimesheet";
    }

    @Override
    protected Widget onInitialize() {
        initialization();
        addButtonListeners();
        getMonthlyTimesheet();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MONTHLY_TIMESHEET_ADD, MonthlyTimesheetView.this, (sender, args) -> {
            getMonthlyTimesheet();
            initEmployeesContractedHours();
        });

        return null;
    }

    private void addButtonListeners() {

        notFilledTimesheet.addClickHandler(event -> getMonthlyTimesheet());
        saveButton.addClickHandler(event -> save());
        //next button listener
        next.addClickHandler(event -> {
            monthDayObject.dateGenerate(1);
            getMonthlyTimesheet();
            initEmployeesContractedHours();
        });
        //previous button listener

        prev.addClickHandler(event -> {
            if (!firstPreviousPressed) {
                firstPreviousPressed = true;
            }
            monthDayObject.dateGenerate(-1);
            getMonthlyTimesheet();
            initEmployeesContractedHours();
        });
        //current month button listener
//        currentMonth.addClickHandler(event -> {
//            monthDayObject.dateGenerate(0);
//            getMonthlyTimesheet();
//            initEmployeesContractedHours();
//        });
    }

    private void save() {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);
        TimesheetService.App.get().saveMonthlyTimesheetData(new ArrayList<MonthlyTimesheetItem>(cellTable.getVisibleItems()), new DateNonConvertable(monthDayObject.getStartDate()), projectLookup.getSelectedItemID(), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MONTHLY_TIMESHEET_ADD, result, MonthlyTimesheetView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.hours()), Info.Type.INFO);
            }
        });

    }

    private void autoSavePerObject(MonthlyTimesheetItem item) {
        ArrayList<MonthlyTimesheetItem> items = new ArrayList<>();
        items.add(item);

        TimesheetService.App.get().saveMonthlyTimesheetData(items, new DateNonConvertable(monthDayObject.getStartDate()), projectLookup.getSelectedItemID(), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                //Info.show("", wfmStrings.error(), Info.Type.ERROR);
            }

            @Override
            public void success(Void result) {
                //getMonthlyTimesheet();
            }
        });
    }

    private void importData(Integer importingFileID) {
        LoadingPanel.loading(true);
        TimesheetService.App.get().importMonthlyTimesheetData(importingFileID, new DateNonConvertable(monthDayObject.getStartDate()), projectLookup.getSelectedItemID(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MONTHLY_TIMESHEET_ADD, null, MonthlyTimesheetView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyImported(), wfmStrings.project(), projectStrings.timeEntries()), Info.Type.INFO);
            }
        });
    }

    private boolean validate() {
        int errors = 0;

        StringBuilder employeesName = new StringBuilder();
        StringBuilder employeesNameForWorkedDays = new StringBuilder();

        for (MonthlyTimesheetItem item : cellTable.getList()) {
            Double workedHoursInMinuts = (item.getWorkedHours() != null ? item.getWorkedHours() : 0) * 60;
            Double workedDays = item.getTotalWorkedDays() != null ? item.getTotalWorkedDays() : 0;

            if (employeeContractedHoursMap.get(item.getProjectEmployeeID()) != null && Double.valueOf(employeeContractedHoursMap.get(item.getProjectEmployeeID())[0]).compareTo(workedHoursInMinuts) < 0) {
                if (!employeesName.toString().isEmpty()) {
                    employeesName.append(", ");
                }

                employeesName.append(item.getEmployeeName());
                errors++;
            }

            if (employeeContractedHoursMap.get(item.getProjectEmployeeID()) != null && Double.valueOf(employeeContractedHoursMap.get(item.getProjectEmployeeID())[1]).compareTo(workedDays) < 0) {
                if (!employeesNameForWorkedDays.toString().isEmpty()) {
                    employeesNameForWorkedDays.append(", ");
                }

                employeesNameForWorkedDays.append(item.getEmployeeName());
                errors++;
            }

        }

        if (errors > 0 && !employeesName.toString().isEmpty()) {
            Info.show(wfmStrings.followingEmployeesMoreThanContractedHours() + ": " + employeesName.toString(), Info.Type.WARNING);
            return false;
        }

        if (errors > 0 && !employeesNameForWorkedDays.toString().isEmpty()) {
            Info.show(wfmStrings.followingEmployeesMoreThanContractedDays() + ": " + employeesNameForWorkedDays.toString(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private void getMonthlyTimesheet() {
        //String startDate = DateUtils.getDateAndTimeFormatWithDash(monthDayObject.getStartDate());
        //String endDate = DateUtils.getDateAndTimeFormatWithDash(monthDayObject.getEndDate());

        loading(monthDayObject.getMonthNameWithYear());
        LoadingPanel.loading(true);
        TimesheetService.App.get().getMonthlyTimesheetData(new DateNonConvertable(monthDayObject.getStartDate()), projectLookup.getSelectedItemID(), "".equals(projectEmployeeLookUp.getText()) ? null : projectEmployeeLookUp.getSelectedItemID(), notFilledTimesheet.getValue(), new AbstractAsyncCallback<ArrayList<MonthlyTimesheetItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ArrayList<MonthlyTimesheetItem> result) {
                //dataProvider.getList().clear();
                //dataProvider.getList().addAll(result);
                //cellTable.redraw();
                supplyProvider(result);
                LoadingPanel.loading(false);
            }
        });
    }

    private void initEmployeesContractedHours() {
        if (projectLookup.getSelectedItemID() != null)
            TimesheetService.App.get().getEmployeeContractedHours(new DateNonConvertable(monthDayObject.getStartDate()), projectLookup.getSelectedItemID(), projectEmployeeLookUp.getSelectedItemID(), new AsyncCallback<HashMap<Integer, Integer[]>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(HashMap<Integer, Integer[]> map) {
                    employeeContractedHoursMap.clear();

                    if (map != null) {
                        employeeContractedHoursMap = map;
                    }
                }
            });
    }

    private void supplyProvider(List<MonthlyTimesheetItem> result) {
        dataProvider.getList().clear();
        dataProvider.getList().addAll(result);

        if (projectLookup.getSelectedItemID() != null && (result == null || result.isEmpty())) {
            cellTable.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.thereAreNoEmployeeByContractInThisPeriod(), "", null));
        } else {
            cellTable.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.pleaseSelectProjectToFillTimeSheetHoursForEmployee(), "", null));
        }
        cellTable.getScrollPanel().getElement().getParentElement().setAttribute("style", "height: 100% !important");
        dataProvider.refresh();
    }

    private void initialization() {
        employeeContractedHoursMap = new HashMap<>();

        notFilledTimesheet = new KpiCheckBox();
        notFilledTimesheet.setText(wfmStrings.notFilledTimesheet());
        notFilledTimesheet.addStyleName("TimesheetFilterPanel__checkbox");

        //save button
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId(viewName + "save_button");

        Div btnGroup = new Div("btn-group");
        prev = new WfmButton2("", WfmButton2.BTN_WHITE, "ficon--chevron-left");
        prev.ensureDebugId(viewName + "previous_button");
        prev.setTitle(wfmStrings.previous());
        btnGroup.add(prev);

        //month day
        monthDayObject = new MonthDay(0);

        //month name button
        monthName = new WfmButton2("", WfmButton2.BTN_WHITE);
        monthName.ensureDebugId(viewName + "month_name");
        btnGroup.add(monthName);

        //next button
        next = new WfmButton2("", WfmButton2.BTN_WHITE, "ficon--chevron-right");
        next.ensureDebugId(viewName + "next_button");
        next.setTitle(wfmStrings.nextstr());
        btnGroup.add(next);

        projectLookup = new CRMLookUp(LookUpConstants.PROJECT);
        projectLookup.setDefaultText(Property.get(Constants.PROJECT, wfmStrings.selectProject(), wfmStrings.project()));
        projectLookup.showClearButton();
        projectLookup.setFullSearch(true);
        projectLookup.getSuggestBox().addSelectionHandler(event -> {
            projectEmployeeLookUp.setEnabled(true);
            projectEmployeeLookUp.clearAndClearItems();
            projectEmployeeLookUp.refreshOracle(true);

            importButtonItemBox.setVisible(true);
            getMonthlyTimesheet();
            initEmployeesContractedHours();
        });
        projectLookup.setClearCommand(() -> {
            projectLookup.refreshOracle(true);

            projectEmployeeLookUp.setEnabled(true);
            projectEmployeeLookUp.clearAndClearItems();
            projectEmployeeLookUp.refreshOracle(true);

            importButtonItemBox.setVisible(true);
            getMonthlyTimesheet();
            initEmployeesContractedHours();
        });

        projectEmployeeLookUp = new EmployeeLookUpWithCode();
        projectEmployeeLookUp.setDefaultText(wfmStrings.select() + " " + wfmStrings.employee());
        projectEmployeeLookUp.showClearButton();
        projectEmployeeLookUp.setEnabled(false);
        projectEmployeeLookUp.setBeforeSearch(() -> {
            projectEmployeeLookUp.getFilterParametrs().setProjectId(projectLookup.getSelectedItemID());
            projectEmployeeLookUp.getFilterParametrs().setIDsOnly(true);
        });
        projectEmployeeLookUp.getSuggestBox().addSelectionHandler(event -> getMonthlyTimesheet());
        projectEmployeeLookUp.setClearCommand(() -> {
            projectEmployeeLookUp.clearAndClearItems();
            projectEmployeeLookUp.refreshOracle(true);
            getMonthlyTimesheet();
        });

        Event.addNativePreviewHandler(event -> {
            NativeEvent ne = event.getNativeEvent();
            final int eventType = event.getTypeInt();
            boolean keyHandled = false;
            switch (eventType) {
                case Event.ONKEYDOWN:
                case Event.ONKEYPRESS:
                    if (ne.getCtrlKey()) {
                        switch (ne.getKeyCode()) {
                            case 'e':
                            case 'E':
                                ne.preventDefault();
                                Scheduler.get().scheduleDeferred(() -> {
                                    projectEmployeeLookUp.clear();
                                    projectEmployeeLookUp.getSuggestBox().setFocus(true);
                                });
                                break;
                        }
                    }
            }
        });
        final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.MONTHLY_TIMESHEET, null);
        imp.setSubmitCompleted(() -> {
            if (imp.getObjectId() != null) {
                importData(imp.getObjectId());
            }
        });
        WfmButton2 importButton = new WfmButton2(wfmStrings.importString(), WfmButton2.BTN_DEFAULT, "markExcel");
        importButton.addClickHandler(event -> imp.open());

        toolBar = new GBoxRow();

        GBoxItem btnGroupItemBox = new GBoxItem(btnGroup);
        btnGroupItemBox.addStyleName("group-box__item--width-free group-box__item--split-right");
        btnGroupItemBox.getgBoxItemLabel().removeFromParent();
        toolBar.add(btnGroupItemBox);

        GBoxItem saveItemBox = new GBoxItem(saveButton);
        saveItemBox.addStyleName("group-box__item--width-free group-box__item--split-right");
        saveItemBox.getgBoxItemLabel().removeFromParent();
        toolBar.add(saveItemBox);

//        GBoxItem currentMonthItemBox = new GBoxItem(currentMonth);
//        currentMonthItemBox.addStyleName("group-box__item--width-free group-box__item--split-right");
//        currentMonthItemBox.getgBoxItemLabel().removeFromParent();
//        toolBar.add(currentMonthItemBox);

        GBoxItem exportItemBox = new GBoxItem(exportButton());
        exportItemBox.getgBoxItemLabel().removeFromParent();
        exportItemBox.setStyleWidthFree(true);
        exportItemBox.setStyleSplitRight(true);
        exportItemBox.setStyleNoBorder(true);
        toolBar.add(exportItemBox);

        GBoxItem projectItemBox = new GBoxItem(projectLookup);
        projectItemBox.getgBoxItemLabel().removeFromParent();
        toolBar.add(projectItemBox);

        GBoxItem employeeItemBox = new GBoxItem(projectEmployeeLookUp);
        employeeItemBox.getgBoxItemLabel().removeFromParent();
        employeeItemBox.setStyleSplitRight(true);
        toolBar.add(employeeItemBox);

        GBoxItem checkBoxItemBox = new GBoxItem(notFilledTimesheet);
        checkBoxItemBox.getgBoxItemLabel().removeFromParent();
        checkBoxItemBox.setStyleSplitRight(true);
        checkBoxItemBox.setStyleWidthFree(true);
        checkBoxItemBox.setStyleNoBorder(true);
        toolBar.add(checkBoxItemBox);

        importButtonItemBox = new GBoxItem(importButton);
        importButtonItemBox.getgBoxItemLabel().removeFromParent();
        importButtonItemBox.setStyleSplitRight(true);
        importButtonItemBox.setStyleWidthFree(true);
        importButtonItemBox.setStyleNoBorder(true);
        importButtonItemBox.setVisible(false);
        toolBar.add(importButtonItemBox);

        GBox groupBox = new GBox();
        groupBox.addStyleName("group-box--united group-box--no-padding");
        groupBox.add(toolBar);
        add(groupBox);

        setWidth("100%");
        dataProvider = new ListDataProvider<>();
        cellTable = new KpiDataGrid<>(KEY_PROVIDER, true);
        cellTable.addStyleName("cellBasedWidget-mod cellBasedWidget-attachment box-radius--top cellBasedWidget-mod--static-body cellBasedWidget-mod--cell-not-overflow");
        cellTable.setWidth("100%");
        cellTable.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.pleaseSelectProjectToFillTimeSheetHoursForEmployee(), "", null));
        addDataDisplay(cellTable);
        add(cellTable);

        initsializationStructure();

        Element elem = monthName.getElement();
        DOM.setElementAttribute(elem, "id", "prev-next-month");
    }

    private Div exportButton() {
        Div exportPanel = new Div();
        exportPanel.setStyleName("operPanel__actions");

        MaterialMenuBar importExportMenu = new MaterialMenuBar();
        importExportMenu.setClass("btn-group dropdown-kit--arrow--below");
        exportPanel.add(importExportMenu);

        MaterialLink ieLink = new MaterialLink();//import/export button link for listing top panel
        ieLink.setTooltip(wfmStrings.importExport());
        ieLink.setTooltipPosition(Position.TOP);
        ieLink.setHref("#");
        ieLink.setClass("btn btn--icon btn--white");
        importExportMenu.add(ieLink);

        Icon ieIcon = new Icon();//import/export icon for listing top panel
        ieIcon.setClass("ficon--download-cloud");
        ieLink.add(ieIcon);

        MaterialDropDown menuContainer = new MaterialDropDown(ieLink);
        menuContainer.setClass("dropdown-content--2");
        menuContainer.setBelowOrigin(true);
        ieLink.add(menuContainer);
        importExportMenu.add(ieLink);

        pdfVersion = getPdfVersion();
        pdfVersion.ensureDebugId("pdf_button");
        if (pdfVersion.isVisible()) {
            Div wrapper = new Div("java-wrap");
            menuContainer.add(wrapper);

            MaterialLink pdfVersion = getPdfVersion();
            wrapper.add(pdfVersion);

            MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
            mdp.setHover(true);
            mdp.setHoverable(true);


            mdp.add(MonthlyTimesheetView.this::getPortraitLink);
            mdp.add(MonthlyTimesheetView.this::getLandscapeLink);
            wrapper.add(mdp);
        }
        setPDFListener();

        //excel button
        MaterialLink xlsVersion = new MaterialLink();
        MaterialIcon xlsIcon = new MaterialIcon();
        xlsIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
        xlsVersion.add(xlsIcon);
        xlsVersion.setText(wfmStrings.excel());
        menuContainer.add(xlsVersion);
        xlsVersion.addClickHandler(event -> {
            String excelURL = CommandConstants.COMMON_URL + "/monthlyTimesheetViewExcelHandler";
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setShowYTD(notFilledTimesheet.getValue());
            filterParameter.setEmployeeId("".equals(projectEmployeeLookUp.getText()) ? null : projectEmployeeLookUp.getSelectedItemID());
            filterParameter.setProjectId(projectLookup.getSelectedItemID());
            filterParameter.setStartDateNC(DateUtils.getDateAndTimeFormatWithDash(monthDayObject.getStartDate()));
            Utils.sendPDFOrExcelRequest(toolBar, excelURL, filterParameter.getRequestParams(), "_blank");
        });
        return exportPanel;
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

    public void setPDFListener() {
        getPortraitLink().addClickHandler((event) -> {
            sendPdfRequest(false);
        });
        getLandscapeLink().addClickHandler((event) -> {
            sendPdfRequest(true);
        });
    }

    private void sendPdfRequest(boolean landscape) {
        String pdfURL = CommandConstants.PDF_URL + "/monthlyTimesheetViewPDFHandler";
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setLandscape(landscape);
        filterParameter.setShowYTD(notFilledTimesheet.getValue());
        filterParameter.setEmployeeId("".equals(projectEmployeeLookUp.getText()) ? null : projectEmployeeLookUp.getSelectedItemID());
        filterParameter.setProjectId(projectLookup.getSelectedItemID());
        filterParameter.setStartDateNC(DateUtils.getDateAndTimeFormatWithDash(monthDayObject.getStartDate()));
        Utils.sendPDFOrExcelRequest(toolBar, pdfURL, filterParameter.getRequestParams(), "_blank");
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.portrait());
        }
        return portrait;
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }


    public void addDataDisplay(HasData<MonthlyTimesheetItem> display) {
        dataProvider.addDataDisplay(display);
    }

    public void loading(String name) {
        //String beginMonth = name.replaceAll(" ", " 1,");
        //String endMonth = name.replaceAll(" ", " " + monthDayObject.getMaxMonthDay() + ",");
        Element elem = DOM.getElementById("prev-next-month");
        elem.setAttribute("style", "min-width:0px;");
        DOM.setInnerText(elem, name + HijriUtils.getHijriDateForAttendance(monthDayObject.getStartDate())/* + " - " + endMonth + HijriUtils.getHijriDateForAttendance(monthDayObject.getEndDate())*/);
    }

    private void initsializationStructure() {
        Column<MonthlyTimesheetItem, SafeHtml> firstNameColumn = new Column<MonthlyTimesheetItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(MonthlyTimesheetItem item) {
                SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
                htmlBuilder.appendEscaped(item.getEmployeeName());

                return htmlBuilder.toSafeHtml();
            }
        };
        final SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendHtmlConstant(wfmStrings.employee());

        cellTable.addColumn(firstNameColumn, sb.toSafeHtml());
        cellTable.setColumnWidth(firstNameColumn, 250, Style.Unit.PX);

        Column<MonthlyTimesheetItem, SafeHtml> contractStartColumn = new Column<MonthlyTimesheetItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(MonthlyTimesheetItem item) {
                SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
                htmlBuilder.appendEscaped(item.getContractStart());
                return htmlBuilder.toSafeHtml();
            }
        };
        cellTable.addColumn(contractStartColumn, wfmStrings.contractStart());
        cellTable.setColumnWidth(contractStartColumn, 130, Style.Unit.PX);

        Column<MonthlyTimesheetItem, SafeHtml> contractEndDateColumn = new Column<MonthlyTimesheetItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(MonthlyTimesheetItem item) {
                SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
                htmlBuilder.appendEscaped(item.getContractEnd());
                return htmlBuilder.toSafeHtml();
            }
        };
        cellTable.addColumn(contractEndDateColumn, wfmStrings.contractEnd());
        cellTable.setColumnWidth(contractEndDateColumn, 130, Style.Unit.PX);

        Column<MonthlyTimesheetItem, String> workedHoursColumn = new Column<MonthlyTimesheetItem, String>(new TextInputCell()) {
            @Override
            public String getValue(MonthlyTimesheetItem item) {
                return item.getWorkedHours() != null ? item.getWorkedHours().toString() : "0";
            }
        };
        workedHoursColumn.setFieldUpdater((index, object, value) -> {
            if (value != null && !"".equals(value)) {
                Double workedHours = 0d;

                try {
                    workedHours = Double.valueOf(value);
                } catch (NumberFormatException e) {
                    workedHours = 0d;
                }

                Double workedHoursInMinuts = workedHours * 60;
                if (employeeContractedHoursMap.get(object.getProjectEmployeeID()) != null && Double.valueOf(employeeContractedHoursMap.get(object.getProjectEmployeeID())[0]).compareTo(workedHoursInMinuts) < 0) {
                    Integer hours = employeeContractedHoursMap.get(object.getProjectEmployeeID())[0] / 60;
                    Integer minuts = employeeContractedHoursMap.get(object.getProjectEmployeeID())[0] % 60;

                    Info.show("You can't enter more than " + hours + " hours " + (minuts > 0 ? "" + minuts + " minutes" : "") + " for this employee", Info.Type.WARNING);
                } else {
                    object.setWorkedHours(workedHours);
                    autoSavePerObject(object);
                }
            } else {
                object.setWorkedHours(0d);
                autoSavePerObject(object);
            }
        });

        cellTable.addColumn(workedHoursColumn, wfmStrings.workedHours());
        cellTable.setColumnWidth(workedHoursColumn, 100, Style.Unit.PX);

        Column<MonthlyTimesheetItem, String> workedDaysColumn = new Column<MonthlyTimesheetItem, String>(new TextInputCell()) {
            @Override
            public String getValue(MonthlyTimesheetItem item) {
                return item.getTotalWorkedDays() != null ? item.getTotalWorkedDays().toString() : "0";
            }
        };
        workedDaysColumn.setFieldUpdater((index, object, value) -> {
            if (value != null && !"".equals(value)) {
                Double workedDays = 0d;

                try {
                    workedDays = Double.valueOf(value);
                } catch (NumberFormatException e) {
                    workedDays = 0d;
                }

                if (employeeContractedHoursMap.get(object.getProjectEmployeeID()) != null && Double.valueOf(employeeContractedHoursMap.get(object.getProjectEmployeeID())[1]).compareTo(workedDays) < 0) {
                    Integer maxDays = employeeContractedHoursMap.get(object.getProjectEmployeeID())[1];

                    Info.show("You can't enter more than " + maxDays + " days  for this employee", Info.Type.WARNING);
                } else {
                    object.setTotalWorkedDays(workedDays.compareTo((double) monthDayObject.getMaxMonthDay()) > 0 ? monthDayObject.getMaxMonthDay() : workedDays);
                    autoSavePerObject(object);
                }
            } else {
                object.setTotalWorkedDays(0d);
                autoSavePerObject(object);
            }

        });

        cellTable.addColumn(workedDaysColumn, wfmStrings.totalWorkedDays());
        cellTable.setColumnWidth(workedDaysColumn, 100, Style.Unit.PX);

        Column<MonthlyTimesheetItem, String> overtimeHoursColumn = new Column<MonthlyTimesheetItem, String>(new TextInputCell()) {
            @Override
            public String getValue(MonthlyTimesheetItem item) {
                return item.getOvertimeHours() != null ? item.getOvertimeHours().toString() : "0";
            }
        };
        overtimeHoursColumn.setFieldUpdater((index, object, value) -> {
            if (value != null && !"".equals(value)) {
                object.setOvertimeHours(Double.valueOf(value));
                autoSavePerObject(object);
            }
        });

        cellTable.addColumn(overtimeHoursColumn, wfmStrings.overtimeRate());
        cellTable.setColumnWidth(overtimeHoursColumn, 100, Style.Unit.PX);

        Column<MonthlyTimesheetItem, String> weekendOvertimeHoursColumn = new Column<MonthlyTimesheetItem, String>(new TextInputCell()) {
            @Override
            public String getValue(MonthlyTimesheetItem item) {
                return item.getWeekendOvertimeHours() != null ? item.getWeekendOvertimeHours().toString() : "0";
            }
        };
        weekendOvertimeHoursColumn.setFieldUpdater((index, object, value) -> {
            if (value != null && !"".equals(value)) {
                object.setWeekendOvertimeHours(Double.valueOf(value));
                autoSavePerObject(object);
            }
        });

        cellTable.addColumn(weekendOvertimeHoursColumn, wfmStrings.weekendOvertimeHours());
        cellTable.setColumnWidth(weekendOvertimeHoursColumn, 100, Style.Unit.PX);

        Column<MonthlyTimesheetItem, String> holidayOvertimeHoursColumn = new Column<MonthlyTimesheetItem, String>(new TextInputCell()) {
            @Override
            public String getValue(MonthlyTimesheetItem item) {
                return item.getHolidayOvertimeHours() != null ? item.getHolidayOvertimeHours().toString() : "0";
            }
        };
        holidayOvertimeHoursColumn.setFieldUpdater((index, object, value) -> {
            if (value != null && !"".equals(value)) {
                object.setHolidayOvertimeHours(Double.valueOf(value));
                autoSavePerObject(object);
            }
        });


        holidayOvertimeHoursColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        cellTable.addColumn(holidayOvertimeHoursColumn, wfmStrings.holidayOvertimeHours());
        cellTable.setColumnWidth(holidayOvertimeHoursColumn, 150, Style.Unit.PX);

        cellTable.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);
    }


    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
