package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.chart.client.charts.KpiGanttChart;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
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
import com.edatasite.workforce.gwt.project.client.ProjectViewSinksContainer;
import com.edatasite.workforce.gwt.project.client.ui.view.ganttchart.ProjectGanttChart;
import com.edatasite.workforce.gwt.project.client.ui.view.ganttchart.ProjectGanttChartCustomizeSideNav;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ProjectGwtGanttChartView extends View {

    interface ProjectGwtGanttChartUIBinder extends UiBinder<HTMLPanel, ProjectGwtGanttChartView> {
    }

    private static final UiBinder uiBinder = GWT.create(ProjectGwtGanttChartUIBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final GanttChartServiceAsync ganttChartService = GanttChartService.App.get();
    private ProjectGanttChartCustomizeSideNav customizeSideNav;
    private ProjectGanttChart ganttChart;
    private List<SelectItem> columns;
    private final Integer projectID;

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

    private final ProjectViewSinksContainer sinksContainer;

    public ProjectGwtGanttChartView(Integer id, ProjectViewSinksContainer sinksContainer) {
        super("gwtganttchart", wfmStrings.ganttChart());
        this.projectID = id;
        this.sinksContainer = sinksContainer;

        HTMLPanel rootElement = (HTMLPanel) uiBinder.createAndBindUi(this);
        add(rootElement);

        initInternal();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, ProjectGwtGanttChartView.this, (sender, args) -> onInitialize());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GANTT_CHART_COLUMN_SETTINGS_CHANGE, ProjectGwtGanttChartView.this, (sender, args) -> onInitialize());

    }

    public String getIconStyle() {
        return "bgMark project-gant-chart";
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
        ieLink.addClickHandler(clickEvent -> new ImportMPPFilePopup("/MSProjectUploadHandler", projectID));

        customizeSideNav = new ProjectGanttChartCustomizeSideNav();
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

        if ("gwtganttchart".equals(sinksContainer.getWorkarea().getCurrentView().getName())) {
            this.getGanttChartData();
        }

        return null;
    }

    private void getGanttChartData() {
        HashSet<String> activeColumns = new HashSet<>();
        for (SelectItem column : this.columns) {
            activeColumns.add(column.getDescription());
        }
        ganttChartService.getGanttChartData(this.projectID, activeColumns, new AbstractAsyncCallback<ChartData>() {
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
                        contentPanel.add(new KpiGanttChart(chartData));
                    }
                } else if (chartData.getGanttSerieData().size() > 0) {
                    ganttChart = new ProjectGanttChart(chartData, columns);
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
        confItem.setTitle("Project");
        confItem.setShowLabel(false);
        chartData.setConf(confItem);

        SerieData data1 = new SerieData();
        data1.setId("task_1");
        data1.setName("Task 1");
        data1.setDependency(null);
        data1.setParent(null);
        data1.setStart(DateUtil.addDays(today, -2));
        data1.setEnd(DateUtil.addDays(today, 2));
        data1.setMilestone(false);
        data1.setPercent(new BigDecimal("0.35"));
        chartData.getSeries().add(data1);

        SerieData data2 = new SerieData();
        data2.setId("task_2");
        data2.setName("Task 2");
        data2.setDependency(data1.getId());
        data2.setParent(null);
        data2.setStart(DateUtil.addDays(today, 1));
        data2.setEnd(DateUtil.addDays(today, 6));
        data2.setMilestone(false);
        data2.setPercent(new BigDecimal("0.35"));
        chartData.getSeries().add(data2);

        SerieData data3 = new SerieData();
        data3.setId("task_3");
        data3.setName("Task 3");
        data3.setDependency(data2.getId());
        data3.setParent(null);
        data3.setStart(DateUtil.addDays(today, 5));
        data3.setEnd(DateUtil.addDays(today, 5));
        data3.setMilestone(true);
        data3.setPercent(null);
        chartData.getSeries().add(data3);

        SerieData data4 = new SerieData();
        data4.setId("task_4");
        data4.setName("Task 4");
        data4.setDependency(data3.getId());
        data4.setParent(null);
        data4.setStart(DateUtil.addDays(today, 7));
        data4.setEnd(DateUtil.addDays(today, 11));
        data4.setMilestone(false);
        data4.setPercent(null);
        chartData.getSeries().add(data4);

        SerieData data5 = new SerieData();
        data5.setId("task_5");
        data5.setName("Task 5");
        data5.setDependency(data4.getId());
        data5.setParent(null);
        data5.setStart(DateUtil.addDays(today, 10));
        data5.setEnd(DateUtil.addDays(today, 15));
        data5.setMilestone(false);
        data5.setPercent(new BigDecimal("1"));
        chartData.getSeries().add(data5);

        SerieData sprint = new SerieData();
        sprint.setId("sprint_1");
        sprint.setName("Sprint 1");
        sprint.setDependency(null);
        sprint.setParent(null);
        sprint.setStart(DateUtil.addDays(today, 2));
        sprint.setEnd(DateUtil.addDays(today, 16));
        sprint.setMilestone(false);
        sprint.setPercent(null);
        chartData.getSeries().add(sprint);

        SerieData data6 = new SerieData();
        data6.setId("task_6");
        data6.setName("Task 6");
        data6.setDependency(null);
        data6.setParent("sprint_1");
        data6.setStart(DateUtil.addDays(today, 2));
        data6.setEnd(DateUtil.addDays(today, 4));
        data6.setMilestone(false);
        data6.setPercent(new BigDecimal("0.8"));
        chartData.getSeries().add(data6);

        SerieData data7 = new SerieData();
        data7.setId("task_7");
        data7.setName("Task 7");
        data7.setDependency(data6.getId());
        data7.setParent("sprint_1");
        data7.setStart(DateUtil.addDays(today, 6));
        data7.setEnd(DateUtil.addDays(today, 6));
        data7.setMilestone(true);
        data7.setPercent(null);
        chartData.getSeries().add(data7);

        SerieData data8 = new SerieData();
        data8.setId("task_8");
        data8.setName("Task 8");
        data8.setDependency(data7.getId());
        data8.setParent("sprint_1");
        data8.setStart(DateUtil.addDays(today, 6));
        data8.setEnd(DateUtil.addDays(today, 10));
        data8.setMilestone(false);
        data8.setPercent(new BigDecimal("0.5"));
        chartData.getSeries().add(data8);

        SerieData data9 = new SerieData();
        data9.setId("task_9");
        data9.setName("Task 9");
        data9.setDependency(data8.getId());
        data9.setParent("sprint_1");
        data9.setStart(DateUtil.addDays(today, 9));
        data9.setEnd(DateUtil.addDays(today, 13));
        data9.setMilestone(false);
        data9.setPercent(new BigDecimal("0.9"));
        chartData.getSeries().add(data9);

        SerieData data10 = new SerieData();
        data10.setId("task_10");
        data10.setName("Task 10");
        data10.setDependency(data9.getId());
        data10.setParent("sprint_1");
        data10.setStart(DateUtil.addDays(today, 13));
        data10.setEnd(DateUtil.addDays(today, 16));
        data10.setMilestone(false);
        data10.setPercent(new BigDecimal("0.1"));
        chartData.getSeries().add(data10);

        if (columns.size() > 0) {
            ganttChart = new ProjectGanttChart(chartData, null);
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

}
