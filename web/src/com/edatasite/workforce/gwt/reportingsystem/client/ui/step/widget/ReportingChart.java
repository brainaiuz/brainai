package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget;

import com.edatasite.workforce.gwt.chart.client.charts.AbstractChart;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieColumn;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DateRangeType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.AdvancedSerieColorWidget;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.ChartAdvancedOpt;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.ChartSerie;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.UnorderedList;
import gwt.material.design.jquery.client.api.JQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportingChart extends AsyncWidget {
    interface ReportingChart2UiBinder extends UiBinder<Widget, ReportingChart> {
    }

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();


    private static final ReportingChart2UiBinder ourUiBinder = GWT.create(ReportingChart2UiBinder.class);

    @UiField
    UnorderedList chartTypeList;
    @UiField(provided = true)
    FormGroup xAxisField;
    @UiField(provided = true)
    FormGroup splitField;
    @UiField(provided = true)
    FormGroup drillField;
    @UiField(provided = true)
    FormGroup customSorderField;
    @UiField(provided = true)
    FormGroup sortingField;
    @UiField(provided = true)
    FormGroup modulesField;
    @UiField
    Div previewChartContainer;
    @UiField
    Div seriesContainer;
    @UiField
    ChartAdvancedOpt chartAdvancedOpt;
    @UiField
    Div chartConfigDiv;

    @UiField
    Div mainConfigRow;

    private WfmDropdown xAxisList;
    private WfmDropdown drillxAxisList;
    private ChartSerie axisValues;
    private DataListBox sortByList;
    private DataListBox sortTypeList;
    private DataListBox dateSortPeriodType;
    private WfmDropdown splitBy;
    private WfmDropdown customSortBy;
    private ReportingStepControlView view;
    private final LinkedList<SelectItem> dateTypeColumns = new LinkedList<>();
    private final LinkedList<SelectItem> dateTypeColumnsForDrill = new LinkedList<>();
    private SelectItem[] columns;
    private final LinkedList<SelectItem> otherTypeColumns = new LinkedList<>();
    private final LinkedList<SelectItem> otherTypeColumnsForDrill = new LinkedList<>();
    private AdvancedSerieColorWidget advancedSerieColorWidget;
    private Command cmdChangeSerie;
    private Command cmdRemoveSerie;
    private Command cmdAddMoreSerie;
    private ChartConfItem chartConf;
    private ChartTypeEnum chartType;
    private ChartSerie chartSerie;


    /**
     * this map contains all of the chart type widgets
     */
    private HashMap<ChartTypeEnum, ListItem> mapCTWidgets;

    /**
     * Modules that chart widget will shown there
     */
    private ArrayList<ModuleEnum> sharedModules;


    ReportingChart() {
        super(null);
    }

    public void setView(ReportingStepControlView view) {
        this.view = view;
    }

    public ReportRpc getReport(ReportRpc report) {

        if (ChartTypeEnum.NONE.equals(chartType)) {
            report.setChartConf(null);
        }

        chartConf = report.getChartConf();

        if (chartConf == null) {
            chartConf = new ChartConfItem();
        }
        chartConf.setType(chartType);

        if (xAxisList.getSelectedId() != null) {
            SerieColumn xAxisColumn = new SerieColumn();

            xAxisColumn.setColumnTitle(xAxisList.getSelectedItem().getName());
            xAxisColumn.setColumn(xAxisList.getSelectedItem().getCode());
            xAxisColumn.setColumnType(xAxisList.getSelectedItem().getCategory());
            xAxisColumn.setColumnFormat(xAxisList.getSelectedItem().getDescription());
            chartConf.setxAxis(xAxisColumn);
        } else {
            chartConf.setxAxis(null);
        }

        if (drillxAxisList.getSelectedId() != null) {
            SerieColumn drillxAxisColumn = new SerieColumn();

            drillxAxisColumn.setColumnTitle(drillxAxisList.getSelectedItem().getName());
            drillxAxisColumn.setColumn(drillxAxisList.getSelectedItem().getCode());
            drillxAxisColumn.setColumnType(drillxAxisList.getSelectedItem().getCategory());
            drillxAxisColumn.setColumnFormat(drillxAxisList.getSelectedItem().getDescription());
            chartConf.setDrillxAxis(drillxAxisColumn);
        } else {
            chartConf.setDrillxAxis(null);
        }

        boolean isGradient = true;
        LinkedList<SerieConfItem> serieConfItems = new LinkedList<>();
        if (getAreaCharts() && axisValues.getSerieConf() != null) {

            serieConfItems.add(axisValues.getSerieConf());
            isGradient = axisValues.isGradient();
        } else if (seriesContainer.getWidgetCount() > 0) {

            for (int i = 0; i < seriesContainer.getWidgetCount(); i++) {
                if (seriesContainer.getWidget(i) instanceof ChartSerie && ((ChartSerie) seriesContainer.getWidget(i)).getSerieConf() != null) {
                    serieConfItems.add(((ChartSerie) seriesContainer.getWidget(i)).getSerieConf());
                    isGradient &= ((ChartSerie) seriesContainer.getWidget(i)).isGradient();
                }
            }
        } else {
            isGradient = false;
        }
        chartConf.setGradientColor(isGradient);

        chartConf.setSeries(!serieConfItems.isEmpty() ? serieConfItems : null);
        chartConf.setModules(sharedModules);
        chartConf.setSortBy(sortByList.getSelectedItem().getDescription());
        chartConf.setSortType(sortTypeList.getSelectedItem().getDescription());

        if (xAxisList.getSelectedIndex() >= 0) {
            dateSortPeriodType.setEnabled("date".equals(xAxisList.getValues().get(xAxisList.getSelectedIndex()).getCategory()));
        }
        if (dateSortPeriodType.getSelectedItem() != null) {
            chartConf.setDateSortPeriodType(dateSortPeriodType.getSelectedItem().getDescription());
        }

        if (splitBy.getSelectedId() != null) {
            SerieColumn splitByColumn = new SerieColumn();

            splitByColumn.setColumnTitle(splitBy.getSelectedItem().getName());
            splitByColumn.setColumn(splitBy.getSelectedItem().getCode());
            splitByColumn.setColumnType(splitBy.getSelectedItem().getCategory());
            splitByColumn.setColumnFormat(splitBy.getSelectedItem().getDescription());
            chartConf.setSplitBy(splitByColumn);
//            chartSerie.setEnabledAdvancedCollerWidget(false);
        } else {
//            chartSerie.setEnabledAdvancedCollerWidget(true);
            chartConf.setSplitBy(null);
        }

        if (customSortBy.getSelectedId() != null) {
            SerieColumn customSortColumn = new SerieColumn();

            customSortColumn.setColumnTitle(customSortBy.getSelectedItem().getName());
            customSortColumn.setColumn(customSortBy.getSelectedItem().getCode());
            customSortColumn.setColumnType(customSortBy.getSelectedItem().getCategory());
            customSortColumn.setColumnFormat(customSortBy.getSelectedItem().getDescription());
            chartConf.setCustomSortColumn(customSortColumn);
        } else {
            chartConf.setCustomSortColumn(null);
        }

        customSortBy.setEnabled(sortByList.getSelectedIndex() == 2);

        chartConf = chartAdvancedOpt.getAdvancedOptData(chartConf);
        report.setChartConf(chartConf);

        return report;
    }

    @Override
    protected Widget onInitialize() {
        chartConf = view.getReport().getChartConf();
        sharedModules = new ArrayList<>();
        createAdvancedSerieColorWidget();
        //first of all need to configure columns
        configureColumns();

        //init series add/remove command handler
        initSerieCommands();

        initSplitColumns();

        initDrillDownColumns();

        //X Axis field config
        xAxisList = initGroupingWidget();
        dateSortPeriodType = initDateRangeWidget();
        xAxisField = new FormGroup(reportingStrings.grouping(), new InputGroup(xAxisList, dateSortPeriodType));

        drillxAxisList = new WfmDropdown();
        drillxAxisList.setEnabled(true);
        drillxAxisList = initDrillGroupingWidget();
        drillField = new FormGroup(reportingStrings.drillDown(), drillxAxisList);    //MunirUpdated
        drillField.setVisible(true);

        //Split By field config
        splitBy = new WfmDropdown();
        splitBy.getElement().getStyle().setProperty("width", Constants.MIN_DEFAULT_WIDTH + " !important ");
        splitBy.addItems(wfmStrings.date().toUpperCase(), dateTypeColumns.toArray(new SelectItem[0]));
        splitBy.addItems(reportingStrings.dimensions().toUpperCase(), otherTypeColumns.toArray(new SelectItem[0]));
        splitBy.addValueChangeHandler(vch -> {
            getReport(view.getReport());
            colorButtonEnabled(splitBy.getSelectedId() == null);
        });
        splitField = new FormGroup(wfmStrings.splitby(), new InputGroup(splitBy));

        //sorting
        sortByList = initSortByWidget();
        sortTypeList = initSortTypeWidget();
        sortingField = new FormGroup(wfmStrings.sort(), new InputGroup(sortByList, sortTypeList));

        //Widget For Custom Sorting
        customSortBy = new WfmDropdown();
        customSortBy.getElement().getStyle().setProperty("width", Constants.MIN_DEFAULT_WIDTH + " !important ");
        customSortBy.setEnabled(false);
        customSortBy.addItems(wfmStrings.date().toUpperCase(), dateTypeColumns.toArray(new SelectItem[0]));
        customSortBy.addItems(reportingStrings.dimensions().toUpperCase(), otherTypeColumns.toArray(new SelectItem[0]));
        customSortBy.addValueChangeHandler(vch -> getReport(view.getReport()));
        customSorderField = new FormGroup(wfmStrings.custom() + " " + wfmStrings.sortBy(), customSortBy);

        //Axis values field config, this field for only {Pie, Donut, Funnel} type of charts
        axisValues = createChartSeries(columns, null, cmdChangeSerie, null, null);
        axisValues.hideSerieName();


        modulesField = initSharingModules();

        add(ourUiBinder.createAndBindUi(this));

//        chartAdvancedOpt.setAdvancedSerieColorWidget(advancedSerieColorWidget);
        chartAdvancedOpt.setColumns(columns);

        initChartTypeList();

        chartAdvancedOpt.setCmdChangeOptions(this::previewChart);
        chartAdvancedOpt.setCmdChangeTitle(() -> {
            chartAdvancedOpt.setTitle(chartAdvancedOpt.getAdvancedOptData(new ChartConfItem()).getTitle());
            JQuery.$(".highcharts-title").find("tspan").html(chartAdvancedOpt.getTitle());
            getReport(view.getReport());
        });

        if (chartConf != null && !Utils.isNullOrEmpty(chartConf.getTitle())) {
            chartAdvancedOpt.setChartTitle(chartConf.getTitle());
        }

        if (chartConf != null && chartConf.getLocalization() != null) {
            chartAdvancedOpt.setLocalization(chartConf.getLocalization());
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCALIZATION_ADD, ReportingChart.this, (sender, args) -> {
            if (args != null && view.getReport().getChartConf() != null) {
                view.getReport().getChartConf().setLocalization((CustomFormLocalization) args);
            }
        });

        loadConfig();

        return null;
    }

    private void colorButtonEnabled(boolean b) {
//        if (axisValues != null) {
//            axisValues.disebledGradient(b);
//        }
        for (int i = 0; i < seriesContainer.getWidgetCount(); i++) {
            if (seriesContainer.getWidget(i) instanceof ChartSerie) {
                ((ChartSerie) seriesContainer.getWidget(i)).disebledGradient(b);
            }
        }
    }

    private DataListBox initSortTypeWidget() {
        DataListBox sortTypeList = new DataListBox();
        sortTypeList.setWithoutNullLabel(true);
        sortTypeList.setWidth(Constants.SHORT_WIDTH);
        sortTypeList.setItems(getSortItems());
        sortTypeList.setSelected(1);
        sortTypeList.addValueChangeHandler(vch -> getReport(view.getReport()));
        return sortTypeList;
    }

    private DataListBox initSortByWidget() {
        DataListBox sortByList = new DataListBox();
        sortByList.setWithoutNullLabel(true);
        sortByList.setItems(getSortByItems());
        sortByList.setSelected(1);
        sortByList.addValueChangeHandler(vch -> getReport(view.getReport()));
        return sortByList;
    }

    private DataListBox initDateRangeWidget() {
        DataListBox dateSortPeriodType = new DataListBox();
        dateSortPeriodType.setEnabled(false);
        dateSortPeriodType.setWithoutNullLabel(true);
        dateSortPeriodType.setWidth(Constants.SHORT_WIDTH);
        dateSortPeriodType.setItems(getDateSortTypes());
        dateSortPeriodType.setSelectedIndex(2);
        dateSortPeriodType.addValueChangeHandler(vch -> getReport(view.getReport()));
        return dateSortPeriodType;
    }

    private WfmDropdown initGroupingWidget() {
        WfmDropdown xAxisList = new WfmDropdown(false, true);
        xAxisList.setWidth(Constants.NORMAL_WIDTH);
        xAxisList.addItems(wfmStrings.date().toUpperCase(), dateTypeColumns.toArray(new SelectItem[0]));
        xAxisList.addItems(reportingStrings.dimensions().toUpperCase(), otherTypeColumns.toArray(new SelectItem[0]));
        xAxisList.addValueChangeHandler(ch -> getReport(view.getReport()));
        return xAxisList;
    }

    private WfmDropdown initDrillGroupingWidget() {
        WfmDropdown drillxAxisList = new WfmDropdown(false, false);
        drillxAxisList.setWidth(Constants.NORMAL_WIDTH);
        drillxAxisList.addItems(wfmStrings.date().toUpperCase(), dateTypeColumnsForDrill.toArray(new SelectItem[0]));
        drillxAxisList.addItems(reportingStrings.dimensions().toUpperCase(), otherTypeColumnsForDrill.toArray(new SelectItem[0]));
        drillxAxisList.addValueChangeHandler(ch -> getReport(view.getReport()));
        return drillxAxisList;
    }

    private void initSplitColumns() {
        for (SelectItem column : columns) {
            if ("date".equals(column.getCategory())) dateTypeColumns.add(column);
            else otherTypeColumns.add(column);
        }
    }

    private void initDrillDownColumns() {
        for (SelectItem column : columns) {
            if ("date".equals(column.getCategory())) dateTypeColumnsForDrill.add(column);
            else otherTypeColumnsForDrill.add(column);
        }
    }

    private void loadConfig() {
        if (chartConf != null) {
            chartType = chartConf.getType();
            advancedSerieColorWidget.setActiveChartType(chartType);

            if (ChartTypeEnum.NONE.equals(chartType)) {
                mapCTWidgets.get(chartType).addStyleName("active");
                chartConfigDiv.setDisplay(Display.NONE);
                return;
            }

            if (!ChartTypeEnum.GAUGE_CHART.equals(chartType)) {
                for (int i = 0; i < xAxisList.getValues().size(); i++) {
                    if (xAxisList.getValues().get(i).getCode().equals(chartConf.getxAxis().getColumn())) {
                        xAxisList.setSelectedIndex(i);
                        break;
                    }
                }
                if (drillxAxisList != null && chartConf.getDrillxAxis() != null) {
                    for (int i = 1; i < drillxAxisList.getValues().size(); i++) {
                        if (drillxAxisList.getValues().get(i).getCode().equals(chartConf.getDrillxAxis().getColumn())) {
                            drillxAxisList.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                if (chartConf.getxAxis() != null) {
                    dateSortPeriodType.setEnabled("date".equals(chartConf.getxAxis().getColumnType()));
                } else {
                    dateSortPeriodType.setEnabled("date".equals(xAxisList.getValues().get(0).getCategory()));
                }

                if (chartConf.getCustomSortColumn() != null) {
                    for (SelectItem column : columns) {
                        if (column.getCode().equals(chartConf.getCustomSortColumn().getColumn())) {
                            customSortBy.setSelected(column.getId());
                            break;
                        }
                    }
                }

                if (chartConf.getSplitBy() != null) {
                    for (SelectItem column : columns) {
                        if (column.getCode().equals(chartConf.getSplitBy().getColumn())) {
                            splitBy.setSelected(column.getId());
                            break;
                        }
                    }
                }
            }

            //clear series container to set proper series by type
            seriesContainer.clear();
            if (getAreaCharts()) {
                seriesContainer.add(axisValues);
                axisValues.setSerieConf(chartConf.getSeries().get(0));
            } else if (chartConf.getSeries() != null) {
                for (SerieConfItem serieConf : chartConf.getSeries()) {
                    seriesContainer.add(createChartSeries(columns, serieConf, cmdChangeSerie, cmdRemoveSerie, cmdAddMoreSerie));
                }
                onChangeSerieSize();
            }

            sortByList.setSelected(1);
            if (ChartConfItem.BY_CATEGORY.equals(chartConf.getSortBy())) {
                sortByList.setSelected(0);
            } else if (ChartConfItem.BY_CUSTOM.equals(chartConf.getSortBy())) {
                sortByList.setSelected(2);
                customSortBy.setEnabled(true);
            }

            if (ChartConfItem.ASC.equals(chartConf.getSortType())) {
                sortTypeList.setSelected(0);
            } else {
                sortTypeList.setSelected(1);
            }

            for (SelectItem item : dateSortPeriodType.getItems()) {
                if (item.getDescription().equals(chartConf.getDateSortPeriodType())) {
                    dateSortPeriodType.setSelectedIndex(item.getId());
                }
            }

            if (chartConf.getModules() != null) {
                sharedModules.addAll(chartConf.getModules());
            }
        }
        chartType = chartType != null ? chartType : ChartTypeEnum.NONE;
        onChangeChart(chartType);
    }

    /**
     * Initialize chart type list widgets
     */
    private void initChartTypeList() {
        mapCTWidgets = new HashMap<>();
        HashMap<String, String> chartNames = new HashMap<>();
        chartNames.put(ChartTypeEnum.NONE.name(), reportingStrings.noChartTitle());
        chartNames.put(ChartTypeEnum.VERTICAL_BAR_CHART.name(), reportingStrings.verticalBarChartTitle());
        chartNames.put(ChartTypeEnum.HORIZONTAL_BAR_CHART.name(), reportingStrings.horizontalBarChartTitle());
        chartNames.put(ChartTypeEnum.LINE_CHART.name(), reportingStrings.lineChartTitle());
        chartNames.put(ChartTypeEnum.AREA_CHART.name(), reportingStrings.areaChartTitle());
        chartNames.put(ChartTypeEnum.PIE_CHART.name(), reportingStrings.PieChartTitle());
        chartNames.put(ChartTypeEnum.DONUT_CHART.name(), reportingStrings.DonutChartTitle());
        chartNames.put(ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.name(), reportingStrings.semiCircleDonutChartTitle());
        chartNames.put(ChartTypeEnum.FUNNEL_CHART.name(), reportingStrings.funnelChartTitle());
        chartNames.put(ChartTypeEnum.GAUGE_CHART.name(), wfmStrings.gaugeChartTitle());
        for (ChartTypeEnum type : ChartTypeEnum.values()) {
            if (!chartNames.containsKey(type.name())) continue;
            ListItem item = new ListItem();
            item.getElement().setInnerHTML("<div class=\"chart-view__icon\">" +
                    "<svg class=\" icon--" + type.getStyleName() + "\"> " +
                    "<use href=\"" + "mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#" + type.getStyleName() + "\"></use></svg>" +
                    "</div><div class=\"chart-view__caption\">" + chartNames.get(type.name()) + "</div>");
            item.addClickHandler(ch -> {
                chartType = type;
                onChangeChart(chartType);
            });
            mapCTWidgets.put(type, item);
            chartTypeList.add(item);
        }
    }

    private static final String MODULE_SWITCH_ON = "btn--toggle-on";
    private static final String MODULE_SWITCH_OFF = "btn--toggle-off";

    private FormGroup initSharingModules() {

        Div btnsGroup = new Div("btns-group");

        WfmButton2 accounting = cretaButton(wfmStrings.accounts(), ModuleEnum.ACCOUNTING);
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            btnsGroup.add(accounting);
        }
        WfmButton2 hrms = cretaButton(wfmStrings.hrms(), ModuleEnum.HRMS);
        if (Utils.hasPermission(PermissionConstants.HRMS_MAIN_MENU)) {
            btnsGroup.add(hrms);
        }
        WfmButton2 projects = cretaButton(wfmStrings.projects(), ModuleEnum.PM);
        if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            btnsGroup.add(projects);
        }
        WfmButton2 sales = cretaButton(wfmStrings.crm(), ModuleEnum.CRM);
        if (Utils.hasPermission(PermissionConstants.CRM_MAIN_MENU)) {
            btnsGroup.add(sales);
        }
        WfmButton2 payroll = cretaButton(wfmStrings.payroll(), ModuleEnum.PAYROLL);
        if (Utils.hasPermission(PermissionConstants.PAYROLL_MAIN_MENU)) {
            btnsGroup.add(payroll);
        }

        FormGroup modulesFld = new FormGroup(btnsGroup);

        Span adAsDashboardTitle = new Span(reportingStrings.addAsDashboard());
        Span tooltipWrapper = new Span();

        setTooltipClass(tooltipWrapper);
        Window.addResizeHandler(e -> setTooltipClass(tooltipWrapper));

        Icon iInfo = new Icon();
        iInfo.setClass("ficon--info");
        MaterialLink iconLink = new MaterialLink();
        iconLink.add(iInfo);
        String activation = "infoDropDown";
        iconLink.setActivates(activation);

        MaterialDropDown dropDown = new MaterialDropDown(activation);
        dropDown.addStyleName("dropdown-content dropdown-content-tooltip tooltip-long-text");
        dropDown.getElement().setInnerHTML("<li>" + "<span>" + wfmMessages.setDashboardConfigINFO(Utils.getProductName(), "</span>" + wfmStrings.settings() + "&nbsp;→&nbsp;" + wfmStrings.dashboard()) + "</li>");
        dropDown.setHover(true);

        tooltipWrapper.add(iconLink);
        tooltipWrapper.add(dropDown);

        modulesFld.getGroupLabel().add(adAsDashboardTitle);
        modulesFld.getGroupLabel().add(new Span(" "));
        modulesFld.getGroupLabel().add(tooltipWrapper);

        return modulesFld;
    }

    private WfmButton2 cretaButton(String buttonName, ModuleEnum moduleName) {
        WfmButton2 moduleButton = new WfmButton2(buttonName, WfmButton2.BTN_SUCCESS, "ficon--remove-circle");
        moduleButton.removeStyleName("hasicon--left");
        if (chartConf != null && chartConf.getModules() != null && chartConf.getModules().contains(moduleName)) {
            moduleButton.addStyleName(MODULE_SWITCH_ON);
        } else {
            moduleButton.addStyleName(MODULE_SWITCH_OFF);
        }
        moduleButton.addClickHandler(ch -> onSwitchOnOffModule(moduleName, moduleButton));
        return moduleButton;
    }

    private void setTooltipClass(Span tooltipWrapper) {
        int frameWidth = JQuery.$(".frame__content__body.scroll-content").outerWidth();
        if (frameWidth < 960) {
            tooltipWrapper.setStyleName("dropdown-kit--arrow--right");
        } else {
            tooltipWrapper.setStyleName("dropdown-kit--arrow--left");
        }
    }

    private void onSwitchOnOffModule(ModuleEnum module, WfmButton2 btn) {

        if (btn.getStyleName().contains(MODULE_SWITCH_ON)) {
            btn.removeStyleName(MODULE_SWITCH_ON);
            btn.addStyleName(MODULE_SWITCH_OFF);
            sharedModules.remove(module);
            getReport(view.getReport());
        } else {
            xAxisList.removeStyleName(Constants.ERROR_FORM_STYLE);
            for (Widget widget : seriesContainer.getChildrenList()) {
                if (widget instanceof ChartSerie) {
                    ((ChartSerie) widget).removeStyleNameFromSerieColumnList(Constants.ERROR_FORM_STYLE);
                    ((ChartSerie) widget).removeStyleNameFromAggrFuncList(Constants.ERROR_FORM_STYLE);
                    ((ChartSerie) widget).removeStyleNameFromTxtSerieName(Constants.ERROR_FORM_STYLE);
                }
            }
            boolean isValidated = true;

            if (ChartTypeEnum.GAUGE_CHART.equals(chartType)) {

                if (!Validation.validateTextBoxRequired(chartAdvancedOpt.getChartTitleBox())) {
                    isValidated = false;
                }
            } else {
                if (chartConf == null) {
                    xAxisList.addStyleName(Constants.ERROR_FORM_STYLE);
                    for (Widget widget : seriesContainer.getChildrenList()) {
                        if (widget instanceof ChartSerie) {
                            ((ChartSerie) widget).addStyleNameToSerieColumnList(Constants.ERROR_FORM_STYLE);
                            ((ChartSerie) widget).addStyleNameToAggrFuncList(Constants.ERROR_FORM_STYLE);
                            ((ChartSerie) widget).addStyleNameToTxtSerieName(Constants.ERROR_FORM_STYLE);
                        }
                    }
                    isValidated = false;
                }

                if (chartConf.getSeries() == null || chartConf.getSeries().isEmpty()) {
                    for (Widget widget : seriesContainer.getChildrenList()) {
                        if (widget instanceof ChartSerie) {
                            ((ChartSerie) widget).addStyleNameToSerieColumnList(Constants.ERROR_FORM_STYLE);
                            ((ChartSerie) widget).addStyleNameToAggrFuncList(Constants.ERROR_FORM_STYLE);
                            ((ChartSerie) widget).addStyleNameToTxtSerieName(Constants.ERROR_FORM_STYLE);
                        }
                    }
                    isValidated = false;
                }

                if (chartConf.getxAxis() == null) {
                    xAxisList.addStyleName(Constants.ERROR_FORM_STYLE);
                    isValidated = false;
                }

                chartAdvancedOpt.getChartTitleBox().removeStyleName(Constants.ERROR_FORM_STYLE);
                if (chartAdvancedOpt.getChartTitleBox().getText() == null || chartAdvancedOpt.getChartTitleBox().getText().isEmpty()) {
                    chartAdvancedOpt.getChartTitleBox().addStyleName(Constants.ERROR_FORM_STYLE);
                    isValidated = false;
                }
            }

            if (isValidated) {
                btn.removeStyleName(MODULE_SWITCH_OFF);
                btn.addStyleName(MODULE_SWITCH_ON);
                sharedModules.add(module);
                getReport(view.getReport());
            } else {
                Info.show(reportingStrings.chartCategoryAndSerieRequired(), Info.Type.WARNING);
            }
        }
    }

    private void initSerieCommands() {
        cmdChangeSerie = () -> getReport(view.getReport());

        cmdRemoveSerie = () -> {
            getReport(view.getReport());

            onChangeSerieSize();
        };
        cmdAddMoreSerie = () -> {
            seriesContainer.add(createChartSeries(columns, null, cmdChangeSerie, cmdRemoveSerie, cmdAddMoreSerie));
            onChangeSerieSize();
        };
    }

    private void configureColumns() {
        List<ColumnRpc> columnsCollection = new ArrayList<>(view.getReport().getSelectedColumns() != null && !view.getReport().getSelectedColumns().isEmpty() ? view.getReport().getSelectedColumns() : new ArrayList<>(view.getReport().getColumnMap().values()));
        columnsCollection = columnsCollection.stream().filter(o -> o.getTitle() != null).sorted(Comparator.comparing(ColumnRpc::getTitle)).collect(Collectors.toList());
        columns = new SelectItem[columnsCollection.size()];

        int index = 0;
        for (ColumnRpc column : columnsCollection) {
            columns[index++] = new SelectItem(index, column.getTitle(), column.getName(), column.getColumnFormat(), column.getType());
        }
    }

    private void previewChart() {
        ReportRpc reportRpc = getReport(view.getReport());
        previewChartContainer.clear();

        AbstractChart chart = null;
        ChartData chartData = null;


        if (ChartTypeEnum.LINE_CHART.equals(chartType)) {
            chartData = ChartUtils.getDefaultLineChartData();
        } else if (ChartTypeEnum.AREA_CHART.equals(chartType)) {
            chartData = ChartUtils.getDefaultAreaChartData();
        } else if (ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartType)) {
            chartData = ChartUtils.getDefaultBarChartData(chartType, chartAdvancedOpt.getStackedSelected());
        } else if (ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartType)) {
            chartData = ChartUtils.getDefaultBarChartData(chartType, chartAdvancedOpt.getStackedSelected());
        } else if (ChartTypeEnum.PIE_CHART.equals(chartType)) {
            chartData = ChartUtils.getDefaultDataForOtherCharts(chartType);
        } else if (ChartTypeEnum.DONUT_CHART.equals(chartType)) {
            chartData = ChartUtils.getDefaultDataForOtherCharts(chartType);
        } else if (ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartType)) {
            chartData = ChartUtils.getDefaultDataForOtherCharts(chartType);
        } else if (ChartTypeEnum.FUNNEL_CHART.equals(chartType)) {
            chartData = ChartUtils.getDefaultDataForOtherCharts(chartType);
        } else if (ChartTypeEnum.GAUGE_CHART.equals(chartType)) {
            chartData = ChartUtils.getDefaultGaugeChartData();
        } else if (ChartTypeEnum.PYRAMID_CHART.equals(chartType)) {
            chartData = ChartUtils.getDefaultDataForOtherCharts(chartType);
        }
        if (reportRpc.getChartConf().getBenchmarkValue() != null && reportRpc.getChartConf().getBenchmarkValue().compareTo(BigDecimal.ZERO) != 0) {
            Number[] values = new Number[chartData.getCategories().size()];
            int i = 0;
            for (String ignored : chartData.getCategories()) {
                values[i] = reportRpc.getChartConf().getBenchmarkValue();
                i++;
            }
            SerieData serieData = new SerieData();
            serieData.setName("Benchmark for y-axis");
            serieData.setValues(values);
            serieData.setSerieType(ChartTypeEnum.LINE_CHART);

            chartData.getSeries().add(serieData);
        } else if (reportRpc.getChartConf().getBenchmarkAggFuncVal() != null && !reportRpc.getChartConf().getBenchmarkAggFuncVal().isEmpty()) {
            ChartUtils.createCustomBenchmarkSeria(reportRpc.getChartConf().getBenchmarkAggFuncVal(), chartData);
        }
        if (chartData != null) {
            chartData.setConf(chartConf);

            chart = ChartUtils.generateChart(chartData);
        }

        if (chart != null) {
            previewChartContainer.add(chart);
        }
    }

    public void createAdvancedSerieColorWidget() {
        if (advancedSerieColorWidget != null) {
            advancedSerieColorWidget.removeFromParent();
        }
        if (!ChartTypeEnum.GAUGE_CHART.equals(chartType)) {
            advancedSerieColorWidget = new AdvancedSerieColorWidget(() -> {
                getReport(view.getReport());
            }, chartType, chartConf != null && chartConf.getGradientColor());
        }
    }

    private void onChangeChart(ChartTypeEnum chart) {
        mapCTWidgets.values().forEach(li -> li.removeStyleName("active"));
        mapCTWidgets.get(chart).addStyleName("active");
        createAdvancedSerieColorWidget();

        if (chart.equals(ChartTypeEnum.NONE)) {
            getReport(view.getReport());
            JQuery.$(chartConfigDiv.getElement()).slideUp(500);
        } else {
            seriesContainer.clear();
            chartAdvancedOpt.setChartConfig(chartConf, chart);

            if (ChartTypeEnum.GAUGE_CHART.equals(chart)) {
                mainConfigRow.setVisible(false);
                seriesContainer.setVisible(false);
            } else {
                mainConfigRow.setVisible(true);
                seriesContainer.setVisible(true);
            }
            chartAdvancedOpt.setVisibleBenchmark(!(getAreaCharts() || ChartTypeEnum.GAUGE_CHART.equals(chart)));
            ChartConfItem confItem = view.getReport().getChartConf();
            if (getAreaCharts()) {
                seriesContainer.add(axisValues);
                if (chartConf.getSeries() != null) {
                    axisValues.setSerieConf(chartConf.getSeries().get(0));
                    advancedSerieColorWidget.setData(axisValues, chartConf.getSeries().get(0));
                }
                axisValues.setAdvancedSerieColorWidget(advancedSerieColorWidget);
            } else if (confItem != null && confItem.getSeries() != null) {
                for (SerieConfItem serieConf : confItem.getSeries()) {
                    seriesContainer.add(createChartSeries(columns, serieConf, cmdChangeSerie, cmdRemoveSerie, cmdAddMoreSerie));
                }
                onChangeSerieSize();
            } else {
                seriesContainer.add(createChartSeries(columns, null, cmdChangeSerie, cmdRemoveSerie, cmdAddMoreSerie));
                onChangeSerieSize();
            }
            previewChart();

            if (ChartTypeEnum.PIE_CHART.equals(chart)) {
                drillxAxisList.setEnabled(true);
            } else {
                drillxAxisList.setEnabled(false);
                drillxAxisList.setSelected(null);
            }

            if (getAreaCharts()) {
                splitBy.setEnabled(false);
                customSortBy.setEnabled(ChartTypeEnum.FUNNEL_CHART.equals(chartType));
                sortByList.setEnabled(ChartTypeEnum.FUNNEL_CHART.equals(chartType));
                sortTypeList.setEnabled(ChartTypeEnum.FUNNEL_CHART.equals(chartType));
            } else {
                splitBy.setEnabled(true);
                if (ChartConfItem.BY_CUSTOM.equals(chartConf.getSortBy())) {
                    customSortBy.setEnabled(true);
                }
                sortByList.setEnabled(true);
                sortTypeList.setEnabled(true);
            }
            JQuery.$(chartConfigDiv.getElement()).slideDown(500);
        }

    }

    public ChartSerie createChartSeries(SelectItem[] columns, SerieConfItem serieConf, Command changeSerieCommand, Command removeSerieCommand, Command addMoreSerieCommand) {
        if (serieConf != null) {
            chartSerie = new ChartSerie(columns, serieConf, changeSerieCommand, removeSerieCommand, addMoreSerieCommand);
            advancedSerieColorWidget.setData(chartSerie, serieConf);
        } else {
            chartSerie = new ChartSerie(columns, changeSerieCommand, removeSerieCommand, addMoreSerieCommand);
        }
        chartSerie.setAdvancedSerieColorWidget(advancedSerieColorWidget);
        return chartSerie;
    }

    private boolean getAreaCharts() {
        return ChartTypeEnum.PIE_CHART.equals(chartType)
                || ChartTypeEnum.DONUT_CHART.equals(chartType)
                || ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartType)
                || ChartTypeEnum.FUNNEL_CHART.equals(chartType);
    }

    private void onChangeSerieSize() {

        if (seriesContainer.getWidgetCount() == 1) {
            ((ChartSerie) seriesContainer.getWidget(0)).canAddMoreSerie(!getAreaCharts());
            ((ChartSerie) seriesContainer.getWidget(0)).setRomovable(false);

        } else if (seriesContainer.getWidgetCount() > 1) {
            for (int i = 0; i < seriesContainer.getWidgetCount(); i++) {
                ((ChartSerie) seriesContainer.getWidget(i)).setRomovable(true);
                int MAX_SERIE_COUNT = 3;
                ((ChartSerie) seriesContainer.getWidget(i)).canAddMoreSerie(seriesContainer.getWidgetCount() < MAX_SERIE_COUNT);
            }
        }
    }


    private SelectItem[] getSortByItems() {
        SelectItem[] items = new SelectItem[3];

        items[0] = new SelectItem(0, wfmStrings.byCategory(), ChartConfItem.BY_CATEGORY);
        items[1] = new SelectItem(1, reportingStrings.bySeries(), ChartConfItem.BY_SERIES, true);
        items[2] = new SelectItem(2, wfmStrings.custom(), ChartConfItem.BY_CUSTOM);

        return items;
    }

    private SelectItem[] getSortItems() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, "A-Z", ChartConfItem.ASC);
        items[1] = new SelectItem(1, "Z-A", ChartConfItem.DESC, true);
        return items;
    }

    private SelectItem[] getDateSortTypes() {
        SelectItem[] items = new SelectItem[DateRangeType.values().length];
        int k = 0;
        for (DateRangeType rangeType : DateRangeType.values()) {
            items[k] = new SelectItem(k, rangeType.name(), rangeType.name());
            k++;
        }
        return items;
    }

}
