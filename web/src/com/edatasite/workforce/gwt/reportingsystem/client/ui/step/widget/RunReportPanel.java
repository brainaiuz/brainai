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
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiDoubleContentSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.JsTooltipsterOptions;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CommonLookup;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.reportingWidgets.KpiReportingWidget;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.AddWorkflowTelegramAlert;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml.RpcConvertToXml;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.ExportFormPanel;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.HandlerUtils;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.SaveReportPopup;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.SaveReportSchedulePopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTab;
import gwt.material.design.client.ui.MaterialTabItem;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.JQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.edatasite.workforce.gwt.core.client.ui.components.tooltip.JsTooltipster.$;

/**
 * Created by Virus on 9/3/14.
 */
public class RunReportPanel extends AsyncWidget {

    public static final String TAB_CREATE_CHART = "TAB_CREATE_CHART";
    public static final String TAB_CREATE_WIDGET = "TAB_CREATE_WIDGET";
    public static final String SHARE_EMAIL = "SHARE_EMAIL";
    public static final String SHARE_TELEGRAM = "SHARE_TELEGRAM";
    private static final String TAB_FILTER = "TAB_FILTER";
    private static final String TAB_REPORT_TYPE = "TAB_REPORT_TYPE_FILTER";
    private static final String TAB_GROUPING = "TAB_GROUPING";
    private static final String TAB_SUMMARIES = "TAB_SUMMARIES";
    private static final String COLUMN_GROUPING = "COLUMN_GROUPING";
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
    MaterialMenuBar shareButton;
    @UiField
    MaterialMenuBar importExportMenu;
    @UiField
    HTMLPanel exportPanel;
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
    @UiField
    WfmButton2 deleteButton;
    private ReportingStepControlView view;
    private ExportFormPanel xls;
    private ExportFormPanel csv;
    private ExportFormPanel pdf;
    private SaveReportSchedulePopup reportSchedulePopup;
    private AddWorkflowTelegramAlert reportScheduleView;
    private MaterialLink email;
    private MaterialLink telegram;
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
    private SaveReportPopup saveReportPopup;
    private MaterialComboBox<FolderRpc> folder;
    private MaterialDropDown menuContainer;
    private boolean chartLoadProcessBusy = false;

    public RunReportPanel() {
        super(null, "run_report");
    }

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
        saveReportPopup(view.getReport().getId() != null);
        if (!view.getReport().isLibrary() && Utils.hasPermission(PermissionConstants.REPORTING_SAVE_BUTTON)) {
            LoadingPanel.loading(false);
            splitButtonItems.add(new SplitButtonItem("SAVE", wfmStrings.save(), () -> {
                loadingData("save");
            }, true));
        }
        if (!view.getReport().isLibrary() && Utils.hasPermission(PermissionConstants.REPORTING_SAVE_AS_BUTTON)) {
            LoadingPanel.loading(false);
//            saveReportPopup(true);
            splitButtonItems.add(new SplitButtonItem("SAVE_AS", reportingStrings.saveAs(), () -> {
                loadingData("save_as");
            }, false));
        }
        deleteButton.setText(wfmStrings.delete());
        splitButton.addItemList(splitButtonItems);
        displayItems = new Span();
        displayItems.setText(nowPosition + " - " + ((nowPosition + step) > allCount ? allCount : (nowPosition + step - 1)) + wfmStrings.of() + allCount);
        current.setValue(1);

        shareButton.setClass("dropdown-kit--arrow--below");

        MaterialLink shareLink = new MaterialLink();
        shareLink.setText(wfmStrings.schedule());
        shareLink.setHref("#");

        menuContainer = new MaterialDropDown(shareLink);
        menuContainer.setClass("dropdown-content--2 dropdown-nested-left");
        menuContainer.setBelowOrigin(true);
        shareLink.add(menuContainer);

        menuContainer.add(getEmailVersion());
        menuContainer.add(getTelegramVersion());

        if (view.getReport().getRuleNames() != null) {
            view.getReport().getRuleNames().stream().forEach(rule -> menuContainer.add(addRuleNamesLink(rule)));
        }
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TELEGRAM_REPORTING_RULE_SAVE, RunReportPanel.this, (sender, args) -> {
            List<String> ruleNames = (List<String>) args;
            menuContainer.clear();
            menuContainer.add(getEmailVersion());
            menuContainer.add(getTelegramVersion());
            ruleNames.forEach(ruleName -> menuContainer.add(addRuleNamesLink(ruleName)));

            telegram.addClickHandler((event) -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("savereportalert|add/add/" + view.getReport().getId() + "/" + wfmStrings.telegramAlert());
            });

            email.addClickHandler((event) -> {
                if (reportSchedulePopup == null) {// TODO RESTORE
                    reportSchedulePopup = new SaveReportSchedulePopup(view);// TODO RESTORE
                }// TODO RESTORE
                reportSchedulePopup.open();// TODO RESTORE
            });
        });

        shareButton.add(shareLink);

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
        exportData();
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
                advancedFilter.setFilterReport(RunReportPanel.this);
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

    private MaterialLink addRuleNamesLink(String ruleName) {
        MaterialLink rule = new MaterialLink();
        rule.addClickHandler(event -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("savereportalert|add/add/" + view.getReport().getId() + "/" + ruleName);
        });
        rule.setText(ruleName);
        return rule;
    }

    private MaterialLink getTelegramVersion() {
        telegram = new MaterialLink();
        telegram.setText(wfmStrings.telegram());
        return telegram;
    }

    private MaterialLink getEmailVersion() {
        email = new MaterialLink();
        email.setText(wfmStrings.email());
        return email;
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
        if (saveReportPopup == null)
            saveReportPopup = new SaveReportPopup(view, clone);
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
        advancedFilter.setFilterReport(RunReportPanel.this);
        advancedFilter.init();
        advancedFilter.setElementVisible(false);
        ReportType type = null;
        ReportingGrouping grouping = new ReportingGrouping();
        grouping.setView(view);
        grouping.init();
        ReportTabWidget groupingTab = createTabItem(TAB_GROUPING, reportingStrings.grouping(), grouping);

        if ("SUMMARY".equals(view.getReport().getTableType())) {
            type = ReportType.SUMMARY;
            hideDetailsButton.setVisible(true);
        } else if ("TABULAR".equals(view.getReport().getTableType())) {
            type = ReportType.TABULAR;
            hideDetailsButton.setVisible(false);
        }
        hideShowGrouping(type);
        ReportTypeWidget reportTypeWidget = new ReportTypeWidget(type);
        reportTypeWidget.setValueChangeCommand(() -> {
            view.getReport().setTableType(reportTypeWidget.getValue().name());
            ReportTabWidget groupingWidget = filterWidgets.get(TAB_GROUPING);
            if (ReportType.TABULAR.equals(reportTypeWidget.getValue())) {
                groupingWidget.addStyleName("hide");
                hideDetailsButton.setVisible(false);
            } else if (ReportType.SUMMARY.equals(reportTypeWidget.getValue())) {
                groupingWidget.removeStyleName("hide");
                hideDetailsButton.setVisible(true);
            }
        });
        tabs.add(createTabItem(TAB_REPORT_TYPE, reportingStrings.reportType(), reportTypeWidget));


        tabs.add(groupingTab);
        ReportingSummaryColumns summaryColumns = new ReportingSummaryColumns();
        summaryColumns.setView(view);
        summaryColumns.init();
        tabs.add(createTabItem(TAB_SUMMARIES, reportingStrings.summaries(), summaryColumns));

        ReportingChart chart = new ReportingChart();
        chart.setView(view);
        chart.init();
        tabs.add(createTabItem(TAB_CREATE_CHART, reportingStrings.createChart(), chart));

        ReportingWidget widget = new ReportingWidget();
        widget.setView(view);
        widget.init();
        tabs.add(createTabItem(TAB_CREATE_WIDGET, reportingStrings.createWidget(), widget));

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
            boolean b = columns.getColumnsReorder().validateGroups();
            if (b) {
                settingsBox.hide();
            }
            updateReport();
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REPORTING_COLUMN_CHANGE, null, RunReportPanel.this);
        });
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, (event) -> settingsBox.hide());


        WfmButton2 addGroup = new WfmButton2(wfmStrings.addGroup(), WfmButton2.BTN_PRIMARY, (event) -> addGroupPopup());
        GColumn column = new GColumn(GColumnEnum.COL_12, addGroup);
        column.setFlexAlignItems(FlexAlignItems.END);
        column.setTextAlign(TextAlign.RIGHT);


        customizeButton.addClickHandler(event -> {
            settingsBox = new KpiDoubleContentSideNavBox(true, 900, true);
            settingsBox.addHeader(getSettingsBoxHeader());
            settingsBox.addToSecondHeader(getSecondHeader());
            settingsBox.addBody(columns);
            settingsBox.addSecondBody(columns.getColumnsReorder());
            settingsBox.addFooter(applyChanges);
            settingsBox.addFooter(cancelButton);
            if (!view.getReport().getTableType().equals(ReportType.SUMMARY.name())) {
                settingsBox.addToSecondHeader(column);
            }
            settingsBox.show();
        });
    }

    private void addGroupPopup() {
        KpiModal addGroupModal = new KpiModal();
        addGroupModal.setTitle(wfmStrings.addGroup());
        addGroupModal.setWidth(350);
        TextBox textBox = new TextBox();
        textBox.setMaxLength(30);
        textBox.addKeyDownHandler(event -> textBox.removeStyleName(Constants.ERROR_FORM_STYLE));

        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        Div conditionContainer = new Div();
        DataListBox condition = new DataListBox();
        condition.setItems(getConditions());

        GColumn groupColumn = new GColumn(GColumnEnum.COL_12, condition);
        groupColumn.setDisplay(Display.FLEX);
        groupColumn.setAlignItems("center");
        groupColumn.setPadding(0);

        conditionContainer.add(groupColumn);

        CommonLookup lookup = new CommonLookup(COLUMN_GROUPING, true);
        lookup.setItems("Columns", getSelectedItems());
        lookup.getSuggestBox().addSelectionHandler(e -> {
            lookup.getSuggestBox().removeStyleName(Constants.ERROR_FORM_STYLE);
        });


        GColumn columnLookUp = new GColumn(GColumnEnum.COL_12, lookup);

        addGroupModal.addWidget(textBox, wfmStrings.name());
        addGroupModal.addWidget(conditionContainer, wfmStrings.select());
        addGroupModal.addWidget(columnLookUp, wfmStrings.column());

        columnLookUp.getParent().getParent().setVisible(false);
        columnLookUp.setPadding(0);

        condition.addValueChangeHandler(change -> {
            condition.removeStyleName(Constants.ERROR_FORM_STYLE);
            lookup.setSelected(0);
            if (condition.isSomethingSelected()) {
                SelectItem selectedItem = lookup.getSelectedItem();
                if (selectedItem.getId() == 0) {
                    lookup.setWithoutNullLabel(null);
                }
                switch (condition.getSelectedItem().getDescription()) {
                    case "BEFORE":
                        columnLookUp.getParent().getParent().setVisible(true);
                        break;
                    case "AFTER":
                        columnLookUp.getParent().getParent().setVisible(true);

                        break;
                    default:
                        columnLookUp.getParent().getParent().setVisible(false);
                        break;
                }
            }
        });

        save.addClickHandler(clickEvent -> {
            int error = 0;

            String groupLabel = textBox.getText();
            if (Utils.isNullOrEmpty(groupLabel)) {
                textBox.addStyleName(Constants.ERROR_FORM_STYLE);
                error++;
            }
            if (!condition.isSomethingSelected()) {
                condition.addStyleName(Constants.ERROR_FORM_STYLE);
                error++;
            } else if (("BEFORE".equals(condition.getSelectedItem().getDescription()) || "AFTER".equals(condition.getSelectedItem().getDescription())) && lookup.isSelected() && lookup.getSelectedItem().getId() < 1) {
                lookup.addStyleName(Constants.ERROR_FORM_STYLE);
                error++;
            }

            if (error == 0) {
                String cond = condition.getSelectedItem().getDescription();
                boolean b = lookup.isSelected() && lookup.getSelectedItem().getId() > 0;
                String column = b ? lookup.getSelectedItem().getDescription() : null;
                boolean isValidGroup = columns.getColumnsReorder().createGroupSelector(groupLabel, cond, column);
                if (isValidGroup) {
                    addGroupModal.close();
                }
            } else {
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            }
        });

        WfmButton2 close = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        close.addClickHandler(x -> addGroupModal.close());

        addGroupModal.addButton(close);
        addGroupModal.addButton(save);
        addGroupModal.open();
    }

    private SelectItem[] getSelectedItems() {
        List<SelectItem> types = new ArrayList<>();
        AtomicReference<Integer> index = new AtomicReference<>(1);
        view.getReport().getSelectedColumns().forEach(i -> {
            types.add(new SelectItem(index.get(), i.getTitle(), i.getName()));
            index.getAndSet(index.get() + 1);
        });
        return types.toArray(new SelectItem[]{});
    }

    private SelectItem[] getConditions() {
        List<SelectItem> types = new ArrayList<>();
        types.add(new SelectItem(1, wfmStrings.up(), "BEGINNING"));
        types.add(new SelectItem(2, wfmStrings.down(), "END"));
        types.add(new SelectItem(3, wfmStrings.beforeAt(), "BEFORE"));
        types.add(new SelectItem(4, wfmStrings.after(), "AFTER"));
        return types.toArray(new SelectItem[]{});
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
            } else if (!TAB_FILTER.equals(code)) {
                filterContent.add(widget);
            }
        }
    }

    private void exportData() {

        importExportMenu.setClass("dropdown-kit--arrow--below menu-bar");

        MaterialLink ieLink = new MaterialLink();//import/export button link for listing top panel
        ieLink.setTooltip(wfmStrings.export());
        ieLink.setTooltipPosition(Position.TOP);
        ieLink.setHref("#");
        ieLink.setClass("btn btn--icon btn--white");

        Icon ieIcon = new Icon();//import/export icon for listing top panel
        ieIcon.setClass("ficon--download-cloud");
        ieLink.add(ieIcon);

        MaterialDropDown menuContainer = new MaterialDropDown(ieLink);
        Div wrapper = new Div("java-wrap");
        menuContainer.setClass("dropdown-content--2 dropdown-nested-left");
        menuContainer.add(wrapper);
        menuContainer.setBelowOrigin(true);
        ieLink.add(menuContainer);

        menuContainer.add(getXlsVersion());
        menuContainer.add(getCSVVersion());
        MaterialLink pdfLink = getPdfVersion();
        wrapper.add(pdfLink);

        MaterialDropDown mdp = new MaterialDropDown(pdfLink);
        mdp.setHover(true);
        mdp.setHoverable(true);
        MaterialLink portrait = new MaterialLink();
        portrait.setText(wfmStrings.portrait());
        MaterialLink landscape = new MaterialLink();
        landscape.setText(wfmStrings.landscape());
        mdp.add(portrait);
        mdp.add(landscape);
        portrait.addClickHandler((event) -> {
            if (allCount > 10000) {
                Info.warn(wfmStrings.youCannotExportMoreThan10000Rows(), Info.Position.BOTTOM_RIGHT);
                return;
            }
            view.getReport().setLandscape(false);
            if (view.getReport() != null && view.getReport().getName() != null) {
                view.getReport().setViewName(view.getReport().getName());
            }
            pdf.setParam(getXmlString());
            pdf.submit();
        });
        landscape.addClickHandler((event) -> {
            if (allCount > 10000) {
                Info.warn(wfmStrings.youCannotExportMoreThan10000Rows(), Info.Position.BOTTOM_RIGHT);
                return;
            }
            view.getReport().setLandscape(true);
            if (view.getReport() != null && view.getReport().getName() != null) {
                view.getReport().setViewName(view.getReport().getName());
            }
            pdf.setParam(getXmlString());
            pdf.submit();
        });
//        <ma:menubar.MaterialMenuBar>
//         <m:MaterialLink text="Link 1" activates="drop" />
//         <m:MaterialDropDown activator="drop" >
//             <m:MaterialLink text="Link 1.1" />
//             <m:MaterialLink text="Link 1.2" />
//         </m:MaterialDropDown>
//       </ma:menubar.MaterialMenuBar>
        wrapper.add(mdp);
        importExportMenu.add(ieLink);
        xls = new ExportFormPanel("XLS", "operPanel markExcel left", "common/reportExcel");
        csv = new ExportFormPanel("CSV", "operPanel markCSV left", "common/reportCsv");

        pdf = new ExportFormPanel("PDF", "operPanel markPDF left", "common/runtimeReportPdf");

        exportPanel.add(pdf);
        pdf.setVisible(false);
        exportPanel.add(xls);
        exportPanel.add(csv);
    }

    private MaterialLink getXlsVersion() {

        MaterialLink xlsVersion = new MaterialLink();
        xlsVersion.addClickHandler(clickEvent -> {
            view.getReport().setLandscape(true);
            xls.setParam(getXmlString());
            xls.submit();
        });
        MaterialIcon xlsIcon = new MaterialIcon();
        xlsIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
        xlsVersion.add(xlsIcon);
        xlsVersion.setText(wfmStrings.excel());
        return xlsVersion;
    }

    private MaterialLink getCSVVersion() {

        MaterialLink xlsVersion = new MaterialLink();
        xlsVersion.addClickHandler(clickEvent -> {
            csv.setParam(getXmlString());
            csv.submit();
        });
        MaterialIcon xlsIcon = new MaterialIcon();
        xlsIcon.setStylePrimaryName("ficon--file-csv hasicon--left");
        xlsVersion.add(xlsIcon);
        xlsVersion.setText(wfmStrings.csv());
        return xlsVersion;
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
        email.addClickHandler((event) -> {
            if (reportSchedulePopup == null) {// TODO RESTORE
                reportSchedulePopup = new SaveReportSchedulePopup(view);// TODO RESTORE
            }// TODO RESTORE
            reportSchedulePopup.open();// TODO RESTORE
        });
        telegram.addClickHandler((event) -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("savereportalert|add/add/" + view.getReport().getId() + "/" + wfmStrings.telegramAlert());
        });
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
        deleteButton.addClickHandler(event -> {
            WfmMessageBox deleteMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, reportingStrings.areYouSureWantoDeleteThisReport()
                    , new CloseHandler() {
                @Override
                public void onSubmit() {
                    boolean hasWidget = (view.getReport().getChartConf() != null && view.getReport().getChartConf().getModules() != null && view.getReport().getChartConf().getModules().size() > 0)
                            || (view.getReport().getKpiWidgetItem() != null && view.getReport().getKpiWidgetItem().getModules() != null && view.getReport().getKpiWidgetItem().getModules().size() > 0);

                    if (hasWidget) {
                        WfmMessageBox deleteMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, reportingStrings.areYouSureWantoDeleteThisReportAndWidget()
                                , new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                deleteRport();

                            }
                        });
                        deleteMessageBox.setTitle(wfmStrings.confirmation());
                        deleteMessageBox.open();
                    } else {
                        deleteRport();
                    }

                }
            });
            deleteMessageBox.setTitle(wfmStrings.confirmation());
            deleteMessageBox.open();
        });
    }

    private void deleteRport() {
        ReportingService.App.get().deleteReport(view.getReport().getId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Boolean result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.REMOVE_REPORT, view.getReport().getId(), RunReportPanel.this);
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
        if (view.getReport().isLibrary() || !Utils.hasPermission(PermissionConstants.REPORTING_DELETE_BUTTON)) {
            deleteButton.removeFromParent();
        }
        if (view.getReport().getId() == null || !Utils.hasPermission(PermissionConstants.REPORTING_SHARE_BUTTON)) {
            shareButton.removeFromParent();
        }
        if (!Utils.hasPermission(PermissionConstants.REPORTING_EXPORT_BUTTON)) {
            importExportMenu.removeFromParent();
        }
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


            if (view.getReport() != null) {
                view.getReport().setFromRunButton(true);
            }
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

        if (kpiWidgetItem != null && !ChartTypeEnum.NONE.equals(kpiWidgetItem.getType())) {
            if (kpiWidgetItem.getKpiWidgetMetric() != null || ChartTypeEnum.RANKING_KPI.equals(kpiWidgetItem.getType())) {
                ReportingService.App.get().getKpiWidgetData(view.getReport(), true, new AbstractAsyncCallback<KpiWidgetData>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(KpiWidgetData kpiWidgetData) {
                        KpiReportingWidget widget = new KpiReportingWidget(true);
                        widget.setData(kpiWidgetData);
                        widget.init();
                        kpiWidgetContainer.add(widget);
                    }
                });
            }
        }
    }

    private void runReportData() {
        view.runReport(new AbstractAsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
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

    private static native NodeList<Element> getElementsByClassName(String level) /*-{
        return $wnd.document.getElementsByClassName(level)
    }-*/;

    private void setAttribute(com.google.gwt.user.client.Element element, String newClass) {
        if ("collapsed".equals(newClass) && element != null) {
            element.setAttribute("style", "display: none;");
        } else if ("expanded".equals(newClass) && element != null) {
            element.setAttribute("style", "display: inline-block;");
        }
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
                    GWT.log("testt" + tBody.getClassName());
                    tBody.removeClassName("collapsed");
                    tBody.addClassName("expanded");
                } else {
                    tBody.removeClassName("expanded");
                    tBody.addClassName("collapsed");
                }
            }
            if (DOM.eventGetType(event) == Event.ONCLICK && element.getId().startsWith("theadercollase")) {
                String oldClass = element.getClassName().contains("collapsed") ? "collapsed" : "expanded";
                String newClass = "collapsed".equals(oldClass) ? "expanded" : "collapsed";

                element.removeClassName(oldClass);
                element.addClassName(newClass);

                String trIndex = element.getId().split(LINE)[1];
                expandCollapseByLevel(Integer.parseInt(trIndex) + 1, oldClass, newClass);

                if ("0".equals(trIndex)) {
                    com.google.gwt.user.client.Element secondLevel = DOM.getElementById("theadercollase-1");
                    com.google.gwt.user.client.Element thirdLevel = DOM.getElementById("theadercollase-2");

                    setAttribute(secondLevel, newClass);

                    if ("collapsed".equals(newClass) && thirdLevel != null) {
                        thirdLevel.setAttribute("style", "display: none;");
                    } else if ("expanded".equals(newClass) && thirdLevel != null && secondLevel.getClassName().contains("expanded")) {
                        thirdLevel.setAttribute("style", "display: inline-block;");
                    }
                } else if ("1".equals(trIndex)) {
                    com.google.gwt.user.client.Element thirdLevel = DOM.getElementById("theadercollase-2");
                    setAttribute(thirdLevel, newClass);
                }
            }
            if (DOM.eventGetType(event) == Event.ONMOUSEOVER && element.getId().startsWith("theadercollase")) {
                JsTooltipsterOptions defaultOptions = getToolTipOptions();
                defaultOptions.content = JQuery.$("<span>Expand/Collapse</span>");
                $(element).tooltipster(defaultOptions);
            }
            if (DOM.eventGetType(event) == Event.ONCLICK && element.getId().startsWith("group_id")) {
                String groupName = element.getId().split(LINE)[1];
                boolean collapsed = element.hasClassName("collapsed");

                if (collapsed) {
                    element.addClassName("expanded");
                    element.removeClassName("collapsed");
                } else {
                    element.addClassName("collapsed");
                    element.removeClassName("expanded");
                }

                NodeList<Element> thgroups = getElementsByClassName("thg-" + groupName);
                NodeList<Element> theaders = getElementsByClassName("th-" + groupName);
                NodeList<Element> tds = getElementsByClassName("td-" + groupName);

                toggleTableElements(thgroups, "thg-" + groupName, collapsed);
                toggleTableElements(theaders, "th-" + groupName, null);
                toggleTableElements(tds, "td-" + groupName, null);
            }
            return true;
        });
    }

    private void toggleTableElements(NodeList<Element> tElements, String groupName, Boolean collapsed) {
        for (int i = 0; i < tElements.getLength(); i++) {
            if (
                    tElements.getItem(i).getClassName().contains("groupCol")
                            && !tElements.getItem(i).getClassName().contains("groupCol--start")
                            && !tElements.getItem(i).getClassName().contains("groupCol--off")
                            && tElements.getItem(i).getClassName().contains(groupName)
            ) {
                Element td = tElements.getItem(i);
                td.removeClassName("groupCol");
                td.addClassName("groupCol--off");
                if (groupName.startsWith("thg-")) {
                    td.getStyle().setDisplay(Style.Display.NONE);
                }
            } else if (
                    tElements.getItem(i).getClassName().contains("groupCol--off")
                            && tElements.getItem(i).getClassName().contains(groupName)
            ) {
                Element td = tElements.getItem(i);
                td.removeClassName("groupCol--off");
                td.addClassName("groupCol");
                if (groupName.startsWith("thg-")) {
                    td.getStyle().setDisplay(Style.Display.NONE);
                }
            } else if (tElements.getItem(i).getClassName().contains("groupCol")
                    && tElements.getItem(i).getClassName().contains("groupCol--start")
                    && groupName.startsWith("thg-")
            ) {
                TableCellElement th = (TableCellElement) tElements.getItem(i);
                th.setColSpan(0);
                if (collapsed != null && collapsed) {
                    th.setColSpan(tElements.getLength());
                }
            }
        }
    }

    private void expandCollapseByLevel(Integer level, String removeClass, String addClass) {
        String levelClass = "tf--level-" + level;
        NodeList<Element> tables = getElementsByClassName(levelClass);
        for (int i = 0; i < tables.getLength(); i++) {
            Element chTable = tables.getItem(i);
            if (chTable.hasClassName(levelClass)) {
                toggleTableClasses(chTable, removeClass, addClass);
            }
        }
    }

    private JsTooltipsterOptions getToolTipOptions() {
        JsTooltipsterOptions result = new JsTooltipsterOptions();
        result.contentAsHTML = true;
        result.contentCloning = false;
        result.interactive = true;
        result.delay = new int[]{0, 300};
        result.delayTouch = new int[]{0, 500};
        return result;
    }

    private void toggleTableClasses(Element element, String removeClass, String addClass) {
        element.removeClassName(removeClass);
        element.addClassName(addClass);
    }

    private void loadingData(String type) {
        ReportRpc report = view.getReport();
        MaterialComboBox<FolderRpc> folderRpc = saveReportPopup.getFolder();
        if (type.equals("save_as")) {
            report.setClonable(true);
            report.setId(null);
            report.setCode(null);
            folderRpc.setSingleValue(null);
            report.setSaveAs(true);
        }
        if (type.equals("save") && report.getId() == null) {
            folderRpc.setSingleValue(null);
        }
        if (!folderRpc.getValues().isEmpty()) {
            saveReportPopup.loadingData();
        } else {
            Info.show("Please waiting datas are loading !!! ", Info.Type.INFO);
            return;
        }
        GWT.log(folderRpc.getSingleValue() != null ? folderRpc.getSingleValue().toString() : "single value is null");
        saveReportPopup.open();
        GWT.log(folderRpc.getSingleValue() != null ? folderRpc.getSingleValue().toString() : "single value is null");
    }

    interface RunReportPanelUiBinder extends UiBinder<HTMLPanel, RunReportPanel> {
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
