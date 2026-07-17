package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget;

import com.edatasite.workforce.gwt.chart.client.charts.AbstractChart;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetItem;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiDoubleContentSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.reportingWidgets.KpiReportingWidget;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml.RpcConvertToXml;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.HandlerUtils;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.SaveReportPopup;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.SaveReportSchedulePopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.html.*;
import gwt.material.design.jquery.client.api.JQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Virus on 9/3/14.
 */
public class BudgetRunReportPanel extends AsyncWidget {

    public static final String TAB_CREATE_CHART = "TAB_CREATE_CHART";
    private static final String TAB_FILTER = "TAB_FILTER";
    private static final String TAB_REPORT_TYPE = "TAB_REPORT_TYPE_FILTER";
    private static final String TAB_GROUPING = "TAB_GROUPING";
    private static final String TAB_SUMMARIES = "TAB_SUMMARIES";
    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final String LINE = "-";
    private static final RunReportPanelUiBinder ourUiBinder = GWT.create(RunReportPanelUiBinder.class);
    @UiField
    DivElement content;
    @UiField
    MaterialTab tabs;
    @UiField
    Div filterContent;
    @UiField
    Div runReportButton;
    @UiField
    Div runReportButtonForMember;
    @UiField
    LIElement previous;
    @UiField
    IntegerBox current;
    @UiField
    LIElement next;
    @UiField
    Div totalPanel;
    @UiField
    Div customizePanel;
    @UiField
    SplitButton splitButton;
    @UiField
    Div hideDetailsButton;
    @UiField
    Div chartContainer;
    @UiField
    Div kpiWidgetContainer;
    @UiField
    Div hideShowOptionsButton;
    private ReportingStepControlView view;
    private SaveReportSchedulePopup reportSchedulePopup;
    private ReportingSelectColumns columns;
    private KpiDoubleContentSideNavBox settingsBox = new KpiDoubleContentSideNavBox(true, 900, true);
    private int nowPosition;
    private int lastPosition;
    private int allCount;
    private int step = 20;
    private Boolean tabsOpened = false;
    private Span hideShowButtonText;
    private KpiCheckBox advanced;
    private SvgIcon showHideFilterSwitcher;
    private ActionButton kanbanViewSwither;
    private Span displayItems;
    private final HashMap<String, ReportTabWidget> filterWidgets = new HashMap<>(); //TAB_CODE and widget
    private ReportingAdvancedFilter advancedFilter;
    private boolean chartLoadProcessBusy = false;

    public static native void table__frame_affix_init() /*-{
        $wnd.table__frame_affix_init();
    }-*/;

    public static String getSerializedRpc(ReportRpc reportRpc) {
        SerializationStreamFactory factory = GWT.create(ReportingService.class);
        SerializationStreamWriter writer = factory.createStreamWriter();
        try {
            writer.writeObject(reportRpc);
            return URL.encode(writer.toString().replace("+", "%2B").replace("&", "%26").replace("?", "%3F"));
        } catch (SerializationException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected Widget onInitialize() {
        HTMLPanel rootPanel = ourUiBinder.createAndBindUi(this);
        add(rootPanel);
        advanced = new KpiCheckBox();
        advanced.getElement().getStyle().setVerticalAlign(-25, Style.Unit.PX);
        advanced.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        advanced.addValueChangeHandler((event) -> advancedFilter.setElementVisible(advanced.getValue()));
        advanced.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NOWRAP);
        advanced.setText(reportingStrings.advancedFilter());

        kanbanViewSwither = new ActionButton("", "btn btn--icon");
        kanbanViewSwither.getElement().getStyle().setVerticalAlign(-30, Style.Unit.PX);
        kanbanViewSwither.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        kanbanViewSwither.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NOWRAP);
        kanbanViewSwither.ensureDebugId("kanban_switcher");

        showHideFilterSwitcher = new SvgIcon(SvgEnum.plus);
        MaterialTooltip switchTooltip = new MaterialTooltip(kanbanViewSwither, wfmStrings.showHide() + " " + wfmStrings.filter());
        switchTooltip.setPosition(Position.TOP);
        kanbanViewSwither.add(showHideFilterSwitcher);

        Icon hideShowButtonIcon = new Icon();
        hideShowButtonIcon.setStyleName("ficon--list-open");
        hideShowOptionsButton.add(hideShowButtonIcon);
        hideShowButtonText = new Span();
        hideShowOptionsButton.add(hideShowButtonText);
        hideShowOptionsButton.addClickHandler(event -> tabsVisibleState());


        List<SplitButtonItem> splitButtonItems = new ArrayList<>();
        final boolean savedReport = view.getReport().getId() != null;
        if (!view.getReport().isLibrary() && Utils.hasPermission(PermissionConstants.REPORTING_SAVE_BUTTON)) {
            splitButtonItems.add(new SplitButtonItem("SAVE", wfmStrings.save(), () -> saveReportPopup(!savedReport), true));
        }
        splitButton.addItemList(splitButtonItems);
        displayItems = new Span();
        displayItems.setText(nowPosition + " - " + ((nowPosition + step) > allCount ? allCount : (nowPosition + step - 1)) + wfmStrings.of() + allCount);
        current.setValue(1);
        runReportButton.getElement().setInnerHTML(reportingStrings.runReport());
        runReportButtonForMember.getElement().setInnerHTML(reportingStrings.runReport());

        if (!(Utils.hasPermission(PermissionConstants.REPORTING_FILTER_SHOW) || Utils.hasPermission(PermissionConstants.REPORTING_SUMMARY_TAB_SHOW))) {
            runReportButton.setMarginTop(16);
            runReportButton.setMarginBottom(16);
            runReportButtonForMember.setMarginTop(16);
            runReportButtonForMember.setMarginBottom(16);
        }
        runReportButtonForMember.setVisible(false);
        runReportButton.addClickHandler((event) -> {
            showWidgetByTabCode(TAB_FILTER);
            updateReport();
        });
        runReportButtonForMember.addClickHandler((event) -> {
            showWidgetByTabCode(TAB_FILTER);
            updateReport();
        });
        current.addStyleName("gwt-TextBox");
        current.addKeyPressHandler(e -> {
            if (e.getUnicodeCharCode() == KeyCodes.KEY_ENTER) {
                if (current.getValue() > allCount / step + 1) {
                } else {
                    moveToPage((current.getValue() - 1) * step + 1, step);
                }
            }
        });

        registrationFormHandlers();
        Date currentDate = new Date();
        currentDate = DateUtil.getMonthLastDate(currentDate);
        DateUtils.format(currentDate);

        hideDetailsButton.getElement().setInnerHTML(view.getReport().getIsDetailed() ? reportingStrings.hideDetails() : wfmStrings.showDetails());
        view.runReport();

        initTotalPanel();
        if (Utils.hasPermission(PermissionConstants.REPORTING_SUMMARY_TAB_SHOW)) {
            initTabs();
        }
        if (Utils.hasPermission(PermissionConstants.REPORTING_FILTER_SHOW)) {
            if (Utils.hasPermission(PermissionConstants.REPORTING_SUMMARY_TAB_SHOW)) {
                filterContent.add(advancedFilter);
            } else {
                advancedFilter = new ReportingAdvancedFilter();
                advancedFilter.setView(view);
                advancedFilter.setBudgetFilterReport(BudgetRunReportPanel.this);
                advancedFilter.init();
                advancedFilter.setElementVisible(false);
                filterContent.add(advancedFilter);
                tabs.add(createTabItem(TAB_FILTER, wfmStrings.filters(), advancedFilter));
                tabs.add(advanced);
                tabs.add(kanbanViewSwither);
                showWidgetByTabCode(TAB_FILTER);
                initColumnsDrawer();
            }
        }
        initHandlers();
        return null;
    }

    private void setSwitchStyle() {
        String switcherClassName = advancedFilter.getStyleName();
        GWT.log(switcherClassName);
        if (switcherClassName != null && switcherClassName.contains("show_filter")) {
            showHideFilterSwitcher.removeFromParent();
            showHideFilterSwitcher = new SvgIcon(SvgEnum.plus);
            kanbanViewSwither.add(showHideFilterSwitcher);
            advancedFilter.setStyleName("hide_filter");
        } else {
            showHideFilterSwitcher.removeFromParent();
            showHideFilterSwitcher = new SvgIcon(SvgEnum.minus);
            kanbanViewSwither.add(showHideFilterSwitcher);
            advancedFilter.setStyleName("show_filter");
        }

    }

    private void initHandlers() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.REPORT_FILTER_CHANGED, ((sender, args) -> {
            nowPosition = 1;
            view.getReport().setPosition(1);
        }));
    }

    private void saveReportPopup(boolean clone) {
        SaveReportPopup saveReportPopup = new SaveReportPopup(view, clone);
        saveReportPopup.open();
    }

    private void initTotalPanel() {
        MaterialLink totalLink = new MaterialLink();
        Div totalLabel = new Div("pagingStat");
        totalLabel.add(displayItems);
        totalLink.add(totalLabel);

        MaterialDropDown flPages = new MaterialDropDown(totalLink);
        flPages.setBelowOrigin(true);
        totalLink.add(flPages);

        MaterialLink first = new MaterialLink(wfmStrings.newest());
        MaterialLink last = new MaterialLink(wfmStrings.oldest());
        flPages.add(first);
        flPages.add(last);
        first.addClickHandler((event) -> moveToFirst());
        last.addClickHandler((event) -> moveToLast());

        totalPanel.add(totalLink);
    }

    private void initTabs() {
        advancedFilter = new ReportingAdvancedFilter();
        advancedFilter.setView(view);
        advancedFilter.setBudgetFilterReport(BudgetRunReportPanel.this);
        advancedFilter.init();
        advancedFilter.setElementVisible(false);
        ReportType type = ReportType.SUMMARY;
        ReportingGrouping grouping = new ReportingGrouping();
        grouping.setView(view);
        grouping.init();
        ReportTabWidget groupingTab = createTabItem(TAB_GROUPING, reportingStrings.grouping(), grouping);

        hideDetailsButton.setVisible(true);
        hideShowGrouping(type);

        tabs.add(groupingTab);
        ReportingSummaryColumns summaryColumns = new ReportingSummaryColumns();
        summaryColumns.setView(view);
        summaryColumns.init();
        tabs.add(createTabItem(TAB_SUMMARIES, reportingStrings.summaries(), summaryColumns));

        tabs.add(createTabItem(TAB_FILTER, wfmStrings.filters(), advancedFilter));

        if (Utils.hasPermission(PermissionConstants.REPORTING_FILTER_SHOW)) {
            tabs.add(advanced);
            tabs.add(kanbanViewSwither);
        }

        showWidgetByTabCode(TAB_FILTER);

        initColumnsDrawer();
    }

    private void tabsVisibleState() {
        if (tabsOpened) {
            hideShowButtonText.setText(wfmStrings.showOptions());
            hideShowOptionsButton.removeStyleName("mod--opened");
            JQuery.$(".create-report .panel").slideUp(500);
            tabsOpened = false;
        } else {
            hideShowButtonText.setText(wfmStrings.hideOptions());
            hideShowOptionsButton.addStyleName("mod--opened");
            onShow();
            JQuery.$(".create-report .panel").slideDown(500);
            tabsOpened = true;
        }
    }

    private native void onShow() /*-{
        $wnd.jQuery('.frame__content__body.scroll-content').animate({scrollTop: 0}, "slow");
    }-*/;

    private void hideShowGrouping(ReportType reportType) {
        ReportTabWidget groupingWidget = filterWidgets.get(TAB_GROUPING);
        if (ReportType.TABULAR.equals(reportType)) {
            groupingWidget.addStyleName("hide");
            hideDetailsButton.setVisible(false);
        } else if (ReportType.SUMMARY.equals(reportType)) {
            groupingWidget.removeStyleName("hide");
            hideDetailsButton.setVisible(true);
        }
    }

    private void initColumnsDrawer() {
        setStyleName(settingsBox.getElement(), "quick-add", true);

        columns = new ReportingSelectColumns();
        columns.setView(view);
        columns.init();

        ActionButton customizeButton = new ActionButton("", "btn btn--icon btn--white");
        customizeButton.ensureDebugId("customise_button");
        Element iSettingsTag = DOM.createElement("i");
        iSettingsTag.setClassName("ficon--equalizer");
        customizeButton.getElement().appendChild(iSettingsTag);
        new KpiToolTip(customizeButton, wfmStrings.customize());
        customizePanel.add(customizeButton);
        WfmButton2 applyChanges = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY, (event) -> {
            settingsBox.hide();
            updateReport();
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REPORTING_COLUMN_CHANGE, null, BudgetRunReportPanel.this);
        });
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, (event) -> settingsBox.hide());

        customizeButton.addClickHandler(event -> {
            settingsBox = new KpiDoubleContentSideNavBox(true, 900, true);
            settingsBox.addHeader(getSettingsBoxHeader());
            settingsBox.addToSecondHeader(getSecondHeader());
            settingsBox.addBody(columns);
            settingsBox.addSecondBody(columns.getColumnsReorder());
            settingsBox.addFooter(applyChanges);
            settingsBox.addFooter(cancelButton);
            settingsBox.show();
        });
    }

    private Div getSettingsBoxHeader() {
        Div result = new Div();
        Div titleDiv = new Div("side-nav__title");
        Heading h1 = new Heading(HeadingSize.H1);
        h1.addStyleName("hasicon--left");
        MaterialIcon icon = new MaterialIcon();
        icon.setInitialClasses("ficon--equalizer");
        Span span = new Span();
        span.setText(wfmStrings.availableColumns());

        h1.add(icon);
        h1.add(span);
        titleDiv.add(h1);

        result.add(titleDiv);

        Div controlsDiv = new Div("controls-row columns-filter");
        Div formGroupDiv = new Div("form-group");
        Label pageSizeLabel = new Label(wfmStrings.pageSize());
        pageSizeLabel.addStyleName("form-group__label");
        Div dropdownDiv = new Div("dropdown-select--arrow dropdown-kit--below");
        DataListBox pageSizeList = new DataListBox();
        pageSizeList.setWithoutNullLabel(true);
        pageSizeList.setItems(getPageSizeList());
        pageSizeList.setSelected(step);
        pageSizeList.addValueChangeHandler(changeEvent -> {
            step = pageSizeList.getSelectedItem().getId();
            view.getReport().setLimit(step);
            view.getReport().setPosition(1);
        });

        formGroupDiv.add(pageSizeLabel);
        dropdownDiv.add(pageSizeList);
        formGroupDiv.add(dropdownDiv);
        controlsDiv.add(formGroupDiv);

        result.add(controlsDiv);

        Div buttonDiv = new Div("btn-group");
        WfmButton2 selectAllButton = new WfmButton2(wfmStrings.selectAll(), (event) -> columns.selectAll(true));
        WfmButton2 deselectAllButton = new WfmButton2(wfmStrings.deselectAll(), (event) -> columns.selectAll(false));

        buttonDiv.add(selectAllButton);
        buttonDiv.add(deselectAllButton);

        controlsDiv.add(buttonDiv);
        return result;
    }

    private Div getSecondHeader() {
        Div titleDiv = new Div("side-nav__title");
        Heading h1 = new Heading(HeadingSize.H1);
        h1.addStyleName("hasicon--left");
        MaterialIcon icon = new MaterialIcon();
        icon.setInitialClasses("ficon--equalizer");
        Span span = new Span();
        span.setText(reportingStrings.selectedColumns());
        h1.add(icon);
        h1.add(span);
        titleDiv.add(h1);
        return titleDiv;
    }

    private SelectItem[] getPageSizeList() {
        SelectItem[] items = new SelectItem[8];

        items[0] = new SelectItem(10, "10");
        items[1] = new SelectItem(20, "20");
        items[2] = new SelectItem(30, "30");
        items[3] = new SelectItem(40, "40");
        items[4] = new SelectItem(50, "50");
        items[5] = new SelectItem(100, "100");
        items[6] = new SelectItem(150, "150");
        items[7] = new SelectItem(200, "200");

        return items;
    }

    private ReportTabWidget createTabItem(String code, String name, Widget widget) {
        ReportTabWidget tabitem = new ReportTabWidget(widget);
        MaterialLink link = new MaterialLink();
        link.setText(name);
        link.setHref("#" + code);
        tabitem.add(link);
        tabitem.addClickHandler(clickEvent -> showWidgetByTabCode(code));
        filterWidgets.put(code, tabitem);
        return tabitem;
    }

    public void showWidgetByTabCode(String code) {
        ReportTabWidget tab = filterWidgets.get(code);
        if (tab != null) {
            filterContent.clear();
            Widget widget = tab.getViewWidget();
            tabs.selectTab(code);
            if (Utils.hasPermission(PermissionConstants.REPORTING_FILTER_SHOW)) {
            advanced.setVisible(TAB_FILTER.equals(code));
                filterContent.add(widget);
            }else if (!TAB_FILTER.equals(code)){
                filterContent.add(widget);
            }
        }
    }

    private void moveToFirst() {
        if (nowPosition > 1) {
            moveToPage(1, step);
        }
    }

    private void moveToLast() {
        if (nowPosition / step != allCount / step) {
            if (allCount % step == 0) {
                moveToPage(allCount - step + 1, step);
            } else {
                moveToPage(allCount - allCount % step + 1, step);
            }
        }
    }

    public MaterialLink getPdfVersion() {

        MaterialLink pdfVersion = new MaterialLink();
        MaterialIcon pdfIcon = new MaterialIcon();
        pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
        pdfVersion.add(pdfIcon);
        pdfVersion.setText(wfmStrings.pdf());
        return pdfVersion;
    }

    public String getXmlString() {
        RpcConvertToXml rpcToXml = new RpcConvertToXml(view.getReport());
        return rpcToXml.generate();
    }

    private void registrationFormHandlers() {
        kanbanViewSwither.addClickHandler(event -> {
            setSwitchStyle();
        });
        final boolean savedReport = view.getReport().getId() != null;
        HandlerUtils.click(previous, event -> {
            if (1 != nowPosition) {
                moveToPage(nowPosition - step, step);
            }
        });
        HandlerUtils.click(next, event -> {
            if ((allCount / step + 1) != (nowPosition / step + 1)) {
                moveToPage(nowPosition + step, step);
            }
        });
        hideDetailsButton.addClickHandler(event -> {
            view.getReport().setIsDetailed(!view.getReport().getIsDetailed());
            hideDetailsButton.getElement().setInnerHTML(view.getReport().getIsDetailed() ? reportingStrings.hideDetails() : wfmStrings.showDetails());
            view.updateReportTable();
        });
    }

    private void deleteRport() {
        ReportingService.App.get().deleteReport(view.getReport().getId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Boolean result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.REMOVE_REPORT, view.getReport().getId(), BudgetRunReportPanel.this);
                view.closeTab();
            }
        });
    }

    private void moveToPage(int beganPositon, int step) {
        ReportRpc reportRpc = view.getReport();
        reportRpc.setLimit(step);
        reportRpc.setPosition(beganPositon);
        nowPosition = beganPositon;
        lastPosition = beganPositon + step - 1;
        displayItems.setText(nowPosition + " - " + (lastPosition > allCount ? allCount : lastPosition) + wfmStrings.of() + allCount);

        current.setValue((nowPosition / step + 1));

        view.updateReportTable();
    }

    public void setView(ReportingStepControlView view) {
        this.view = view;
    }

    public void runReport() {
        if (!Utils.hasPermission(PermissionConstants.REPORTING_SHOW_HIDE_OPTIONS_BUTTON)) {
            tabsOpened = !tabsOpened;
            tabsVisibleState();
            hideShowOptionsButton.removeFromParent();
            runReportButtonForMember.setVisible(true);
        } else {
            tabsVisibleState();
        }
        if (!Utils.hasPermission(PermissionConstants.REPORTING_SHOW_HIDE_DETAILS_BUTTON)) {
            hideDetailsButton.removeFromParent();
        }
        if (!Utils.hasPermission(PermissionConstants.REPORTING_CUSTOMIZE_COLUMNS_BUTTON)) {
            customizePanel.removeFromParent();
        }

        setUpdatedParameters();
        view.getReport().setLimit(20);
        step = 20;
        loadReportStucture();

        loadTotalQuery();

        runReportData();

        loadChartPanel();

        loadKpiWidgetPanel();
    }

    private void loadReportStucture() {
        view.getStructure(new AbstractAsyncCallback<ReportRpc>() {

            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ReportRpc result) {
                step = result.getLimit();
            }
        });
    }

    private void loadTotalQuery() {
        view.getQueryTotalResult(new AbstractAsyncCallback<ReportRpc>() {

            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ReportRpc result) {
                nowPosition = result.getNowPosition();
                lastPosition = result.getNowLastPosition();
                allCount = result.getAllCount();
                displayItems.setText(nowPosition + " - " + ((nowPosition + step) > allCount ? allCount : (nowPosition + step - 1)) + " of " + allCount);

                current.setValue((nowPosition / step + 1));


            }
        });
    }

    public void updateReport() {
        setUpdatedParameters();
        if (view.getReport().getSelectedColumns().size() < 1) {
            Info.show(reportingStrings.selectColumnToIncludeInYourReport(), Info.Type.WARNING);
            return;
        }
        loadTotalQuery();
        runReportData();
        loadChartPanel();

        loadKpiWidgetPanel();
    }

    public void updateReportTable() {
        setUpdatedParameters();
        runReportData();
    }

    private void setUpdatedParameters() {
        view.includeGroupingChanges();
        view.includeFilters();
    }

    private void loadChartPanel() {

        if (chartLoadProcessBusy) {
            return;
        }
        final ChartConfItem chartConfig = view.getReport().getChartConf();

        if (chartConfig != null && !ChartTypeEnum.NONE.equals(chartConfig.getType())
                && (chartConfig.getxAxis() != null && chartConfig.getSeries() != null
                || ChartTypeEnum.GAUGE_CHART.equals(chartConfig.getType()))) {

            chartLoadProcessBusy = true;
            chartContainer.clear();

            ReportingService.App.get().getReportChartData(view.getReport(), false, new AbstractAsyncCallback<ChartData>() {
                @Override
                public void onFailure(Throwable caught) {
                    chartLoadProcessBusy = false;
                }

                @Override
                public void onSuccess(ChartData chartData) {
                    chartLoadProcessBusy = false;
                    if (chartData != null && ChartTypeEnum.GAUGE_CHART.equals(chartData.getConf().getType())) {
                        chartContainer.add(ChartUtils.generateChart(chartData));
                    } else if (chartData != null && chartData.getSeries() != null && !chartData.getSeries().isEmpty()) {
                        AbstractChart chart = ChartUtils.generateChart(chartData);
                        chartContainer.add(chart);
                    }
                }
            });
        }
    }


    private void loadKpiWidgetPanel() {

        KpiWidgetItem kpiWidgetItem = view.getReport().getKpiWidgetItem();
        kpiWidgetContainer.clear();

        if (kpiWidgetItem != null && kpiWidgetItem.getKpiWidgetMetric() != null) {
            ReportingService.App.get().getKpiWidgetData(view.getReport(), false, new AbstractAsyncCallback<KpiWidgetData>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(KpiWidgetData kpiWidgetData) {
                    kpiWidgetContainer.clear();
                    KpiReportingWidget widget = new KpiReportingWidget(true);
                    widget.setData(kpiWidgetData);
                    widget.init();
                    kpiWidgetContainer.add(widget);
                }
            });
        }
    }

    private void runReportData() {
        view.runReport(new AbstractAsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(String result) {
                content.setInnerHTML(result);
                registerEventHandler();
                table__frame_affix_init();
            }

        });
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        MainLayout.get().considerBodyHasOperPanel(true);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        MainLayout.get().considerBodyHasOperPanel(false);
        settingsBox.close();
    }

    private void registerEventHandler() {
        DOM.addEventPreview(event -> {
            Element element = DOM.eventGetTarget(event);
            if (DOM.eventGetType(event) == Event.ONCLICK && element.getId().endsWith("sort")) {
                if (view.getReport().getSortTableByColumnType() == null || "".equals(view.getReport().getSortTableByColumnType()) || "DESC".equals(view.getReport().getSortTableByColumnType())) {
                    view.getReport().setSortTableByColumnType("ASC");
                } else {
                    view.getReport().setSortTableByColumnType("DESC");
                }
                view.getReport().setSortTableByColumn(element.getId().split(LINE)[0]);
                view.updateReportTable();
            }
            if (DOM.eventGetType(event) == Event.ONCLICK && element.getId().startsWith("tbodycollase")) {
                String trIndex = element.getId().split(LINE)[1];
                Element tBody = DOM.getElementById("trIndex-" + trIndex);
                if (tBody.getClassName().contains("collapsed")) {
                    tBody.removeClassName("collapsed");
                    tBody.addClassName("expanded");
                } else {
                    tBody.removeClassName("expanded");
                    tBody.addClassName("collapsed");
                }
            }
            return true;
        });
    }

    interface RunReportPanelUiBinder extends UiBinder<HTMLPanel, BudgetRunReportPanel> {
    }

    private class ReportTabWidget extends MaterialTabItem {
        private final Widget viewWidget;

        ReportTabWidget(Widget viewWidget) {
            super();
            this.viewWidget = viewWidget;
        }

        Widget getViewWidget() {
            return viewWidget;
        }
    }

}