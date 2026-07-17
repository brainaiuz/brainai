package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.ui.view.ganttchart.LeavePlannerGanttChart;
import com.edatasite.workforce.gwt.availability.client.ui.view.ganttchart.LeavePlannerGanttChartCustomizeSideNav;
import com.edatasite.workforce.gwt.chart.client.charts.KpiGanttChart;
import com.edatasite.workforce.gwt.chart.client.charts.KpiGanttChartLeave;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportMPPFilePopup;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.ganttchart.client.GanttChartService;
import com.edatasite.workforce.gwt.ganttchart.client.GanttChartServiceAsync;
import com.edatasite.workforce.gwt.hrms.client.factory.HrmsSinksContainerFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class LeavePlannerListView extends View {


    private static final LeaveGwtGanttChartUIBinder ourUiBinder = GWT.create(LeaveGwtGanttChartUIBinder.class);
    private static final UiBinder uiBinder = GWT.create(LeavePlannerListView.LeaveGwtGanttChartUIBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final GanttChartServiceAsync ganttChartService = GanttChartService.App.get();
    private final Integer userID;
    private final HrmsSinksContainerFactory sinksContainer;
    @UiField
    HTMLPanel mainPanel;
    @UiField
    HTMLPanel headerPanel;
    @UiField
    HTMLPanel headerRow;
    @UiField
    HTMLPanel title;
    @UiField
    HTMLPanel filterPanel;
    @UiField
    HTMLPanel actionPanel;
    @UiField
    HTMLPanel contentPanel;
    private LeavePlannerGanttChartCustomizeSideNav customizeSideNav;
    private LeavePlannerGanttChart ganttChart;
    private List<SelectItem> columns;

    public LeavePlannerListView(HrmsSinksContainerFactory sinksContainer) {
        super("gwtganttchart", wfmStrings.leavePlanner());
        this.userID = sinksContainer.id;
        this.sinksContainer = sinksContainer;

        HTMLPanel rootElement = (HTMLPanel) uiBinder.createAndBindUi(this);
        add(rootElement);

        initInternal();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, LeavePlannerListView.this, (sender, args) -> onInitialize());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GANTT_CHART_COLUMN_SETTINGS_CHANGE, LeavePlannerListView.this, (sender, args) -> onInitialize());

    }

    public String getIconStyle() {
        return "bgMark leave-gant-chart";
    }

    private void initInternal() {
        WfmButton2 resetButton = new WfmButton2(null, "btn btn--icon", "ficon--repeat");
        resetButton.removeHasiconLeftStyle();
        resetButton.addClickHandler(clickEvent -> onInitialize());

        Div resetPanel = new Div("widget-heading__action");
        resetPanel.add(resetButton);

        MaterialLink ieLink = new MaterialLink();
        new MaterialTooltip(ieLink, wfmStrings.importExport()).setPosition(Position.TOP);
        ieLink.setClass("btn btn--icon");
        ieLink.add(new SvgIcon(SvgEnum.uploadCloud));
        ieLink.ensureDebugId("import_export_button_id");
        ieLink.addClickHandler(clickEvent -> new ImportMPPFilePopup("/MSProjectUploadHandler", userID));

        customizeSideNav = new LeavePlannerGanttChartCustomizeSideNav();
        customizeSideNav.addClosingHandler(event -> {
            columns = customizeSideNav.getActiveColumns();
            if (columns.size() > 0 && ganttChart != null) {
                getGanttChartData();
            }
        });
        this.columns = customizeSideNav.getActiveColumns();

        ActionButton customizeButton = new ActionButton("", "btn btn--icon");
        new MaterialTooltip(customizeButton, wfmStrings.showAdditionalFields()).setPosition(Position.TOP);
        customizeButton.add(new SvgIcon(SvgEnum.sliders));
        customizeButton.addClickHandler(event -> {
            customizeSideNav.show();
        });

        actionPanel.add(customizeButton);
        actionPanel.add(ieLink);
        actionPanel.add(resetPanel);
    }

    protected Widget onInitialize() {
        contentPanel.clear();
        LoadingPanel.loading(true);


        //if ("gwtganttchart".equals(sinksContainer.getWorkarea().getCurrentView().getName())) {

        this.getGanttChartData();
        //}

        return null;
    }

    private void getGanttChartData() {
        HashSet<String> activeColumns = new HashSet<>();

        for (SelectItem column : this.columns) {

            activeColumns.add(column.getDescription());
        }
        LoadingPanel.loading(true);
        ganttChartService.getGanttChartDataLeave(this.userID, activeColumns, new AbstractAsyncCallback<ChartData>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ChartData chartData) {
                LoadingPanel.loading(false);
                if (activeColumns.size() == 0) {
                    if (chartData.getSeries().size() > 0) {
                        contentPanel.add(new KpiGanttChartLeave(chartData));
                    }
                } else if (chartData.getGanttSerieData().size() > 0) {
                    ganttChart = new LeavePlannerGanttChart(chartData, columns);
                    contentPanel.add(ganttChart);
                } else {
                    noData();
                }
            }
        });
    }

    public void setTitle(String title) {
        Heading heading = new Heading(HeadingSize.H3);
        heading.setText(title);

        this.title.clear();
        this.title.add(heading);
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

    private void noData() {
        contentPanel.clear();

        Div noDataContent = new Div("chart-no-data");
        noDataContent.getElement().setInnerHTML(wfmStrings.noDataAvailable());
        contentPanel.add(noDataContent);
        contentPanel.addStyleName("no-data--content");
        getSampleData();
    }

    private void getSampleData() {

        ChartData chartData = new ChartData();
        Date today = new Date();

        chartData.setGanttMinDate(DateUtil.addDays(today, -3));
        chartData.setGanttMaxDate(DateUtil.addDays(today, 16));

        ChartConfItem confItem = new ChartConfItem();
        confItem.setTitle("Leave Planner");

        confItem.setShowLabel(false);


        chartData.setConf(confItem);


        if (columns.size() > 0) {
            ganttChart = new LeavePlannerGanttChart(chartData, columns);
            contentPanel.add(ganttChart);
        } else {
            contentPanel.add(new KpiGanttChart(chartData));
        }
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

    interface LeaveGwtGanttChartUIBinder extends UiBinder<HTMLPanel, LeavePlannerListView> {
    }


}

