package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.DepartmentGoalChartSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.DepartmentGoalEmployeeHistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColorWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Credits;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.PlotLine;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.labels.PlotLineLabel;
import org.moxieapps.gwt.highcharts.client.plotOptions.BarPlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.ColumnPlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PiePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DepartmentGoalChart extends Composite {

    private static final String PERIOD_ALL = "ALL";
    private static final String PERIOD_DAY = "DAY";
    private static final String PERIOD_WEEK = "WEEK";
    private static final String PERIOD_MONTH = "MONTH";
    private static final String PERIOD_QUARTER = "QUARTER";
    private static final String PERIOD_YEAR = "YEAR";
    private static final String PERIOD_CUSTOM = "CUSTOM";

    private static final String CHART_VERTICAL = "VERTICAL";
    private static final String CHART_HORIZONTAL = "HORIZONTAL";
    private static final String CHART_PIE = "PIE";
    private static final String CHART_SEMICIRCLEDONUT = "SEMICIRCLEDONUT";
    private static final String CHART_BULLET = "BULLET";

    private static final long DAY_MS = 24L * 60L * 60L * 1000L;

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final MaterialPanel mainPanel;
    private final MaterialPanel chartContainer;
    private final DataListBox chartTypeDropdown;
    private final DataListBox periodDropdown;
    private final DatePicker customFromPicker;
    private final DatePicker customToPicker;

    private final DateTimeFormat dayLabel = DateTimeFormat.getFormat("dd MMM");
    private final DateTimeFormat monthLabel = DateTimeFormat.getFormat("MMM yyyy");

    private GoalItem currentItem;
    private List<DepartmentGoalEmployeeHistoryItem> history;
    private boolean historyLoaded;
    private Chart chart;

    // Suppresses change handlers while programmatically setting dropdowns in drawChart()
    private boolean initializing;

    // True only when the user actively picks "Custom" (so loadHistory should reopen its
    // popup afterwards). The restore path leaves it false so a saved Custom range renders
    // without popping the picker open.
    private boolean pendingCustomPopup;

    // Optional custom range window applied to dated entries (time-series views only)
    private Date customFrom;
    private Date customTo;

    // Current chart settings (visual + selection); always populated with defaults after drawChart()
    private DepartmentGoalChartSettingsItem chartSettings;

    // Whether the current user may change chart settings (type/period/visual). Viewers see a read-only chart.
    private final boolean settingsEditable;

    // ----------------------------------------------------------------------
    // Per-chart-type visual settings. The Settings dialog is built from this
    // registry, filtered by the current chart type — adding a setting = add an
    // enum constant here + apply it in the relevant render method.
    // ----------------------------------------------------------------------

    private enum ChartVisualSetting {
        TARGET_COLOR(CHART_VERTICAL, CHART_HORIZONTAL, CHART_SEMICIRCLEDONUT),
        ACTUAL_COLOR(CHART_VERTICAL, CHART_HORIZONTAL, CHART_BULLET, CHART_SEMICIRCLEDONUT),
        LEGEND_POSITION(CHART_VERTICAL, CHART_HORIZONTAL, CHART_PIE, CHART_SEMICIRCLEDONUT),
        LABEL_FORMAT(CHART_VERTICAL, CHART_HORIZONTAL, CHART_PIE, CHART_SEMICIRCLEDONUT),
        SHOW_PIE(CHART_VERTICAL, CHART_HORIZONTAL),
        SHOW_SERIES(CHART_VERTICAL, CHART_HORIZONTAL),
        PIE_STYLE(CHART_PIE);

        private final String[] types;

        ChartVisualSetting(String... types) {
            this.types = types;
        }

        boolean appliesTo(String chartType) {
            for (String type : types) {
                if (type.equals(chartType)) {
                    return true;
                }
            }
            return false;
        }
    }

    private String settingLabel(ChartVisualSetting s) {
        switch (s) {
            case TARGET_COLOR: return hrmsStrings.dgTargetColor();
            case ACTUAL_COLOR: return hrmsStrings.dgActualColor();
            case LEGEND_POSITION: return hrmsStrings.dgLegendPosition();
            case LABEL_FORMAT: return hrmsStrings.dgLabelFormat();
            case SHOW_PIE: return hrmsStrings.dgShowPie();
            case SHOW_SERIES: return hrmsStrings.dgShowSeries();
            case PIE_STYLE: default: return hrmsStrings.dgChartStyle();
        }
    }

    private SelectItem[] legendPositionOptions() {
        return new SelectItem[]{
                new SelectItem(0, wfmStrings.none(), "NONE"),
                new SelectItem(1, wfmStrings.top(), "TOP"),
                new SelectItem(2, wfmStrings.right(), "RIGHT"),
                new SelectItem(3, wfmStrings.bottom(), "BOTTOM")
        };
    }

    private SelectItem[] labelFormatOptions() {
        return new SelectItem[]{
                new SelectItem(0, wfmStrings.value(), "VALUE"),
                new SelectItem(1, wfmStrings.percentage(), "PERCENTAGE"),
                new SelectItem(2, hrmsStrings.dgOptValuePercentage(), "VALUE_PERCENTAGE")
        };
    }

    private SelectItem[] pieStyleOptions() {
        return new SelectItem[]{
                new SelectItem(0, hrmsStrings.dgChartPie(), "PIE"),
                new SelectItem(1, hrmsStrings.dgOptDonut(), "DONUT")
        };
    }

    public DepartmentGoalChart() {
        mainPanel = new MaterialPanel();
        this.settingsEditable = Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_GOAL_CHART_SETTINGS);

        // Header holding the period + chart-type dropdowns on the right side
        MaterialPanel headerPanel = new MaterialPanel("form-row");
        headerPanel.getElement().getStyle().setProperty("display", "flex");
        headerPanel.getElement().getStyle().setProperty("justifyContent", "flex-end");
        headerPanel.getElement().getStyle().setProperty("gap", "12px");
        headerPanel.getElement().getStyle().setProperty("paddingBottom", "15px");

        // Period filter: groups the dated "actual" entries by time bucket.
        // Offered granularities are narrowed in drawChart() based on the goal's span.
        periodDropdown = new DataListBox();
        periodDropdown.setItems(defaultPeriods());
        periodDropdown.setSelectedByDescription(PERIOD_ALL); // Default: per-employee breakdown
        periodDropdown.setWidth("140px");
        periodDropdown.setChangeEvent(() -> {
            if (initializing) return;
            if (currentItem != null) {
                onPeriodChanged();
            }
        });

        chartTypeDropdown = new DataListBox();
        chartTypeDropdown.setItems(new SelectItem[]{
                new SelectItem(1, hrmsStrings.dgChartVerticalBarCombo(), CHART_VERTICAL),
                new SelectItem(2, hrmsStrings.dgChartHorizontalBarCombo(), CHART_HORIZONTAL),
                new SelectItem(3, hrmsStrings.dgChartPie(), CHART_PIE),
                new SelectItem(4, hrmsStrings.dgChartSemicircleDonut(), CHART_SEMICIRCLEDONUT),
                new SelectItem(5, hrmsStrings.dgChartBullet(), CHART_BULLET)
        });
        chartTypeDropdown.setSelectedByDescription(CHART_VERTICAL); // Default view
        chartTypeDropdown.setWidth("220px");
        // Re-render in the currently selected period mode when orientation changes,
        // and persist the choice so it is remembered on the next visit.
        chartTypeDropdown.setChangeEvent(() -> {
            if (initializing) return;
            if (currentItem != null) {
                periodDropdown.setVisible(settingsEditable && isPeriodApplicable());
                renderChart();
                saveSettings();
            }
        });

        headerPanel.add(periodDropdown);
        headerPanel.add(chartTypeDropdown);
        // Viewers (no chart-settings permission) get a read-only chart: hide the selectors.
        chartTypeDropdown.setVisible(settingsEditable);
        periodDropdown.setVisible(settingsEditable);

        // Chart-settings gear icon — only shown to users with the chart-settings permission
        WfmButton2 settingsBtn = new WfmButton2("", new SvgIcon(SvgEnum.settings));
        settingsBtn.addClickHandler(clickEvent -> openSettingsPopup());
        settingsBtn.setVisible(settingsEditable);
        headerPanel.add(settingsBtn);

        // Custom range pickers live inside a popup that opens when the "Custom"
        // period is selected (see openCustomRangePopup()).
        customFromPicker = new DatePicker();
        customFromPicker.setWidth("100%");
        customToPicker = new DatePicker();
        customToPicker.setWidth("100%");

        // Container where Highcharts will inject the SVG
        chartContainer = new MaterialPanel();

        mainPanel.add(headerPanel);
        mainPanel.add(chartContainer);

        initWidget(mainPanel);

        customizeChartMenu();
    }

    public void drawChart(GoalItem item) {
        this.currentItem = item;
        // Invalidate the cached dated history whenever the goal changes
        this.history = null;
        this.historyLoaded = false;
        // Restoring a previously configured view must NOT auto-open the Custom popup
        this.pendingCustomPopup = false;

        initializing = true;

        // Load saved chart settings (visual + selection), merged with visual defaults
        DepartmentGoalChartSettingsItem saved = item != null ? item.getChartSettings() : null;
        this.chartSettings = DepartmentGoalChartSettingsItem.withDefaults(saved);
        String savedChartType = saved != null ? saved.getChartType() : null;
        String savedPeriod = saved != null ? saved.getPeriod() : null;

        // Narrow the offered granularities to those that make sense for this goal's span
        SelectItem[] periods = availablePeriods(goalSpanDays());
        periodDropdown.setItems(periods);

        // Restore the previously saved chart type (without triggering a save)
        chartTypeDropdown.setSelectedByDescription(savedChartType != null ? savedChartType : CHART_VERTICAL);

        // Pie / Semicircle Donut / Bullet are aggregate views — the period is always "All"
        // and the period selector is hidden for them.
        boolean periodApplicable = isPeriodApplicable();
        periodDropdown.setVisible(periodApplicable && settingsEditable);

        // Restore the previously saved period (+ custom window) when it is still offered
        // for this goal's span; otherwise fall back to "All".
        boolean needsHistory = false;
        if (periodApplicable && savedPeriod != null && isPeriodAvailable(savedPeriod, periods)) {
            periodDropdown.setSelectedByDescription(savedPeriod);
            if (PERIOD_CUSTOM.equals(savedPeriod)) {
                customFrom = saved.getCustomFrom();
                customTo = saved.getCustomTo();
            }
            needsHistory = !PERIOD_ALL.equals(savedPeriod);
        } else {
            periodDropdown.setSelectedByDescription(PERIOD_ALL);
        }

        initializing = false;

        // A restored time-series/custom period needs the dated history before rendering
        if (needsHistory && !historyLoaded) {
            loadHistory(); // finishHistoryLoad() renders (popup suppressed via pendingCustomPopup)
        } else {
            renderChart();
        }
    }

    private boolean isVertical() {
        return CHART_VERTICAL.equals(selectedChartType());
    }

    /** The period (range) selector only applies to the bar combo views; the aggregate
     *  views (Pie, Semicircle Donut, Bullet) are always "All". */
    private boolean isPeriodApplicable() {
        String t = selectedChartType();
        return CHART_VERTICAL.equals(t) || CHART_HORIZONTAL.equals(t);
    }

    private String selectedChartType() {
        SelectItem selected = chartTypeDropdown.getSelectedItem(true);
        return selected != null && selected.getDescription() != null ? selected.getDescription() : CHART_VERTICAL;
    }

    private String selectedPeriod() {
        SelectItem selected = periodDropdown.getSelectedItem(true);
        return selected != null && selected.getDescription() != null ? selected.getDescription() : PERIOD_ALL;
    }

    /**
     * The bucketing granularity used for the time-series. "Custom" is itself just a
     * user-chosen window rendered day by day, so it always buckets at DAY granularity.
     */
    private String effectiveBucketPeriod() {
        return PERIOD_CUSTOM.equals(selectedPeriod()) ? PERIOD_DAY : selectedPeriod();
    }

    private boolean isPeriodAvailable(String period, SelectItem[] periods) {
        if (period == null) return false;
        for (SelectItem p : periods) {
            if (period.equals(p.getDescription())) {
                return true;
            }
        }
        return false;
    }

    private void onPeriodChanged() {
        String period = selectedPeriod();

        // "Custom" opens a popup to pick a [from, to] window; the dated history
        // is fetched lazily first, then the popup is shown over the rendered range.
        if (PERIOD_CUSTOM.equals(period)) {
            if (!historyLoaded) {
                pendingCustomPopup = true;
                loadHistory();
                return;
            }
            ensureDefaultCustomRange();
            renderChart();
            openCustomRangePopup();
            return;
        }

        // Non-Custom period is committed now — persist it (custom window is cleared)
        saveSettings();

        // Other time-series modes need the dated history entries; fetch them once (lazily)
        if (!PERIOD_ALL.equals(period) && !historyLoaded) {
            loadHistory();
            return;
        }
        renderChart();
    }

    private void loadHistory() {
        if (currentItem == null || currentItem.getObjectId() == null) {
            historyLoaded = true;
            history = null;
            finishHistoryLoad();
            return;
        }

        LoadingPanel.loading(true);

        // Fetch only the (date, actual) pairs the chart needs — the dedicated chart
        // RPC avoids the full toRpc() mapping (which lazy-loads the assignee per row)
        // and skips the unused count query.
        HrmsService.App.get().getDepartmentGoalChartData(currentItem.getObjectId(),
                new AbstractAsyncCallback<List<DepartmentGoalEmployeeHistoryItem>>() {
                    @Override
                    public void success(List<DepartmentGoalEmployeeHistoryItem> result) {
                        LoadingPanel.loading(false);
                        history = result;
                        historyLoaded = true;
                        finishHistoryLoad();
                    }

                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        history = null;
                        historyLoaded = true;
                        finishHistoryLoad();
                    }
                });
    }

    private void finishHistoryLoad() {
        if (PERIOD_CUSTOM.equals(selectedPeriod())) {
            // History was loaded while in Custom mode — render the range, and only reopen
            // the picker popup when the user actively chose Custom (not on restore).
            ensureDefaultCustomRange();
            renderChart();
            if (pendingCustomPopup) {
                openCustomRangePopup();
            }
            pendingCustomPopup = false;
        } else {
            renderChart();
        }
    }

    private void renderChart() {
        chartContainer.clear();

        // Safety check: nothing to draw without data or assignees
        if (currentItem == null || currentItem.getGoalAssigneeItem() == null
                || currentItem.getGoalAssigneeItem().length == 0) {
            return;
        }

        // Department target: use the goal's own target, but fall back to the sum of
        // per-employee targets when the department target is not set.
        double sumEmpTarget = sumEmployeeTargets();
        double totalTarget = (currentItem.getTargetGoal() != null && currentItem.getTargetGoal() > 0)
                ? currentItem.getTargetGoal() : sumEmpTarget;

        switch (selectedChartType()) {
            case CHART_PIE:
                renderPie();
                break;
            case CHART_SEMICIRCLEDONUT:
                renderSemiCircleDonut(totalTarget, computeTotalActual());
                break;
            case CHART_BULLET:
                renderBullet(totalTarget, computeTotalActual());
                break;
            default: // VERTICAL / HORIZONTAL combo
                renderCombo(totalTarget);
                break;
        }

        chartContainer.add(chart);
    }

    private double sumEmployeeTargets() {
        double sum = 0.0;
        GoalAssigneeItem[] assignees = currentItem.getGoalAssigneeItem();
        if (assignees != null) {
            for (GoalAssigneeItem assignee : assignees) {
                sum += assignee.getTarget() != null ? assignee.getTarget() : 0.0;
            }
        }
        return sum;
    }

    private double sumAssigneeActuals() {
        double sum = 0.0;
        GoalAssigneeItem[] assignees = currentItem.getGoalAssigneeItem();
        if (assignees != null) {
            for (GoalAssigneeItem assignee : assignees) {
                sum += assignee.getActual() != null ? assignee.getActual() : 0.0;
            }
        }
        return sum;
    }

    /**
     * Department total achieved for the currently selected period — used by the
     * summary chart types (gauge / semicircle donut / bullet). "All" sums the
     * per-assignee actuals; any time-series period uses the cumulative actual across
     * the (optionally windowed) dated history.
     */
    private double computeTotalActual() {
        if (!isPeriodApplicable() || PERIOD_ALL.equals(selectedPeriod())) {
            return sumAssigneeActuals();
        }
        return bucketHistory(effectiveBucketPeriod()).grandTotal;
    }

    /**
     * Default ("All") view: one Target and one Actual bar per assignee.
     * Returns the sum of all assignee actuals (the department total achieved).
     */
    private double renderPerEmployeeBars() {
        GoalAssigneeItem[] assignees = currentItem.getGoalAssigneeItem();
        String[] categories = new String[assignees.length];
        Number[] targets = new Number[assignees.length];
        Number[] actuals = new Number[assignees.length];

        double totalActual = 0.0;
        for (int i = 0; i < assignees.length; i++) {
            categories[i] = assignees[i].getName();

            double empTarget = assignees[i].getTarget() != null ? assignees[i].getTarget() : 0.0;
            double empActual = assignees[i].getActual() != null ? assignees[i].getActual() : 0.0;

            targets[i] = empTarget;
            actuals[i] = empActual;

            totalActual += empActual;
        }

        chart.getXAxis().setCategories(categories);
        final double[] categoryTotals = new double[assignees.length];
        for (int i = 0; i < assignees.length; i++) {
            categoryTotals[i] =
                    (targets[i] != null ? targets[i].doubleValue() : 0);
        }

        DataLabels barLabels = barLabelsFor(categories, categoryTotals);
        boolean vertical = isVertical();
        PlotOptions targetOpts = vertical
                ? new ColumnPlotOptions().setColor(chartSettings.getTargetColor()).setDataLabels(barLabels)
                : new BarPlotOptions().setColor(chartSettings.getTargetColor()).setDataLabels(barLabels);
        PlotOptions actualOpts = vertical
                ? new ColumnPlotOptions().setColor(chartSettings.getActualColor()).setDataLabels(barLabels)
                : new BarPlotOptions().setColor(chartSettings.getActualColor()).setDataLabels(barLabels);
        chart.addSeries(chart.createSeries().setName(wfmStrings.target()).setPoints(targets).setPlotOptions(targetOpts));
        chart.addSeries(chart.createSeries().setName(wfmStrings.actual()).setPoints(actuals).setPlotOptions(actualOpts));

        return totalActual;
    }

    /**
     * Time-series view: cumulative actual over time (one bar per period bucket)
     * with a flat department-target reference line. Returns the grand total achieved.
     */
    private double renderTimeSeries(double totalTarget) {
        TimeSeriesData data = bucketHistory(effectiveBucketPeriod());

        String[] labels = data.labels.toArray(new String[0]);
        chart.getXAxis().setCategories(labels);
        double grand = data.grandTotal > 0 ? data.grandTotal : 1;
        final double[] totals = new double[labels.length];
        for (int i = 0; i < totals.length; i++) {
            totals[i] = grand;
        }
        DataLabels barLabels = barLabelsFor(labels, totals);
        boolean vertical = isVertical();
        chart.addSeries(chart.createSeries()
                .setName(wfmStrings.actual())
                .setPoints(data.cumulative.toArray(new Number[0]))
                .setPlotOptions(vertical
                        ? new ColumnPlotOptions().setColor(chartSettings.getActualColor()).setDataLabels(barLabels)
                        : new BarPlotOptions().setColor(chartSettings.getActualColor()).setDataLabels(barLabels)));

        // Department target reference line
        addTargetLine(totalTarget);

        return data.grandTotal;
    }

    /**
     * Combo pie of department totals: two literal slices — "Total Target" and "Achieved".
     */
    private void addComboPie(double totalTarget, double totalActual, boolean vertical) {
        if (totalTarget <= 0 && totalActual <= 0) {
            return; // nothing meaningful to render
        }

        Series pieSeries = chart.createSeries();
        pieSeries.setType(Series.Type.PIE);
        pieSeries.setName(hrmsStrings.dgDepartmentTotal());
        // Keep the legend to just Target/Actual (from the bar series) — the pie's
        // own Target/Actual slices would otherwise duplicate those two entries.
        pieSeries.setOption("showInLegend", false);
        pieSeries.setOption("size", "25%"); // Responsive scaling
        pieSeries.setOption("innerSize", "60%"); // Donut (ring) overlay

        if (vertical) {
            pieSeries.setOption("center", new String[]{"15%", "18%"});
        } else {
            pieSeries.setOption("center", new String[]{"82%", "22%"});
        }

        pieSeries.addPoint(new Point(wfmStrings.target(), totalTarget).setColor(chartSettings.getTargetColor()));
        pieSeries.addPoint(new Point(wfmStrings.actual(), totalActual).setColor(chartSettings.getActualColor()));

        chart.addSeries(pieSeries);
    }

    /**
     * Default combo: per-employee (or time-series) Target/Actual bars with a small
     * Target/Actual pie overlay of the department totals.
     */
    /**
     * Department-target reference line — styled by the "Line style" + "Target color"
     * settings. Shown in both combo views so the Line style setting is always visible.
     */
    private void addTargetLine(double totalTarget) {
        if (totalTarget <= 0) {
            return;
        }
        chart.getYAxis().setPlotLines(
                chart.getYAxis().createPlotLine()
                        .setValue(totalTarget)
                        .setColor(chartSettings.getTargetColor())
                        .setWidth(2)
                        .setDashStyle(PlotLine.DashStyle.DASH)
                        .setLabel(new PlotLineLabel().setText(wfmStrings.target()))
        );
    }

    private void renderCombo(double totalTarget) {
        boolean vertical = isVertical();

        chart = new Chart()
                .setType(vertical ? Series.Type.COLUMN : Series.Type.BAR)
                .setHeight(400)
                .setChartTitleText(hrmsStrings.dgTitleAssigneeBreakdown())
                .setCredits(new Credits().setEnabled(false));
        applyLegend(chart);

        // Headroom so the combo pie overlay does not overlap the bars
        chart.getYAxis().setMaxPadding(0.35);

        if (PERIOD_ALL.equals(selectedPeriod())) {
            double totalActual = renderPerEmployeeBars();
            addTargetLine(totalTarget);
            if (Boolean.TRUE.equals(chartSettings.getShowPie())) {
                addComboPie(totalTarget, totalActual, vertical);
            }
        } else {
            double totalActual = renderTimeSeries(totalTarget);
            if (Boolean.TRUE.equals(chartSettings.getShowPie())) {
                addComboPie(totalTarget, totalActual, vertical);
            }
        }
    }

    /**
     * Standalone pie: one slice per assignee, sized by that assignee's actual
     * contribution to the total achieved.
     */
    private void renderPie() {
        chart = new Chart()
                .setType(Series.Type.PIE)
                .setHeight(400)
                .setChartTitleText(hrmsStrings.dgTitleAchievedByAssignee())
                .setCredits(new Credits().setEnabled(false));
        applyLegend(chart);

        Series pieSeries = chart.createSeries();
        pieSeries.setType(Series.Type.PIE);
        pieSeries.setName(hrmsStrings.dgAchieved());
        PiePlotOptions pieOpts = new PiePlotOptions()
                .setShowInLegend(true)
                .setDataLabels(new DataLabels().setEnabled(true).setFormat(formatFrom(chartSettings.getLabelFormat())));
        if ("DONUT".equals(chartSettings.getPieStyle())) {
            pieOpts.setInnerSize(0.5);
        }
        pieSeries.setPlotOptions(pieOpts);

        GoalAssigneeItem[] assignees = currentItem.getGoalAssigneeItem();
        if (assignees != null) {
            for (GoalAssigneeItem assignee : assignees) {
                if (assignee == null) {
                    continue;
                }
                double actual = assignee.getActual() != null ? assignee.getActual() : 0.0;
                if (actual <= 0) {
                    continue; // skip assignees who have not contributed
                }
                String name = assignee.getName() != null ? assignee.getName() : "—";
                pieSeries.addPoint(new Point(name, actual));
            }
        }

        chart.addSeries(pieSeries);
    }

    /**
     * Half-donut: two slices — Target and Achieved — laid out as a semicircle.
     */
    private void renderSemiCircleDonut(double target, double actual) {
        chart = new Chart()
                .setType(Series.Type.PIE)
                .setHeight(400)
                .setChartTitleText(hrmsStrings.dgTitleTargetVsAchieved())
                .setCredits(new Credits().setEnabled(false));
        applyLegend(chart);

        Series pieSeries = chart.createSeries();
        pieSeries.setType(Series.Type.PIE);
        pieSeries.setName(hrmsStrings.dgTitleTargetVsAchieved());
        pieSeries.setPlotOptions(new PiePlotOptions()
                .setStartAngle(-90)
                .setOption("endAngle", 90)
                .setInnerSize(0.75)
                .setCenter(0.5, 1)
                .setSize(1.4)
                .setShowInLegend(true)
                .setDataLabels(new DataLabels().setEnabled(true).setFormat(formatFrom(chartSettings.getLabelFormat()))));

        pieSeries.addPoint(new Point(hrmsStrings.dgAchieved(), Math.max(actual, 0)).setColor(chartSettings.getActualColor()));
        pieSeries.addPoint(new Point(wfmStrings.target(), Math.max(target, 0)).setColor(chartSettings.getTargetColor()));

        chart.addSeries(pieSeries);
    }

    /**
     * Bullet graph (simulated — Highcharts has no native bullet series): a single
     * bar for the achieved measure, a target threshold line, and qualitative
     * background bands (poor / fair / good) relative to the target.
     */
    private void renderBullet(double target, double actual) {
        chart = new Chart()
                .setType(Series.Type.BAR)
                .setHeight(260)
                .setChartTitleText(hrmsStrings.dgTitleAchievedVsTarget())
                .setCredits(new Credits().setEnabled(false));

        String category = currentItem.getTitle() != null && !currentItem.getTitle().isEmpty()
                ? currentItem.getTitle() : wfmStrings.department();
        chart.getXAxis().setCategories(new String[]{category});

        double safeMax = Math.max(target, actual);
        if (safeMax <= 0) {
            safeMax = 1;
        }
        chart.getYAxis().setMin(0).setMax(safeMax);
        chart.getYAxis().setGridLineWidth(0);

        // Qualitative ranges + target marker only make sense against a target
        if (target > 0) {
            chart.getYAxis().setPlotBands(
                    chart.getYAxis().createPlotBand().setFrom(0).setTo(target * 0.5).setColor("#f5b7b1"),          // light red
                    chart.getYAxis().createPlotBand().setFrom(target * 0.5).setTo(target * 0.9).setColor("#fde9a6"), // light amber
                    chart.getYAxis().createPlotBand().setFrom(target * 0.9).setTo(safeMax).setColor("#b5e7b0")       // light green
            );
            chart.getYAxis().setPlotLines(
                    chart.getYAxis().createPlotLine()
                            .setValue(target)
                            .setColor("#333333")
                            .setWidth(3)
                            .setLabel(new PlotLineLabel().setText(wfmStrings.target()))
            );
        }

        chart.addSeries(chart.createSeries()
                .setType(Series.Type.BAR)
                .setName(hrmsStrings.dgAchieved())
                .setPoints(new Number[]{actual})
                .setPlotOptions(new BarPlotOptions().setPointWidth(40).setColor(chartSettings.getActualColor())));
    }

    // ----------------------------------------------------------------------
    // Client-side time bucketing of dated actual entries.
    // Uses the deprecated java.util.Date getters, which ARE emulated by GWT
    // (java.util.Calendar is not, so it is intentionally avoided).
    // ----------------------------------------------------------------------

    private TimeSeriesData bucketHistory(String period) {
        TreeMap<Long, Double> buckets = new TreeMap<>(); // key = period start millis (keeps chronological order)
        // The [from, to] window only filters entries while in "Custom" period mode
        boolean limitToWindow = PERIOD_CUSTOM.equals(selectedPeriod());

        if (history != null) {
            for (DepartmentGoalEmployeeHistoryItem entry : history) {
                if (entry == null || entry.getDate() == null || entry.getActual() == null) {
                    continue;
                }
                Date entryDate = entry.getDate();
                if (limitToWindow) {
                    if (customFrom != null && entryDate.before(customFrom)) {
                        continue;
                    }
                    if (customTo != null && entryDate.after(customTo)) {
                        continue;
                    }
                }
                long start = bucketStart(entryDate, period);
                Double current = buckets.get(start);
                buckets.put(start, (current != null ? current : 0.0) + entry.getActual());
            }
        }

        TimeSeriesData data = new TimeSeriesData();
        double running = 0.0;
        for (Map.Entry<Long, Double> entry : buckets.entrySet()) {
            running += entry.getValue();
            data.labels.add(bucketLabel(entry.getKey(), period));
            data.cumulative.add(running);
        }
        data.grandTotal = running;
        return data;
    }

    private long bucketStart(Date date, String period) {
        switch (period) {
            case PERIOD_DAY:
                return floorToMidnight(date);
            case PERIOD_WEEK: {
                int daysSinceMonday = (date.getDay() + 6) % 7; // date.getDay(): 0=Sun..6=Sat
                long monday = floorToMidnight(date) - daysSinceMonday * DAY_MS;
                return floorToMidnight(new Date(monday)); // re-floor to absorb DST hour shifts
            }
            case PERIOD_QUARTER: {
                int quarterFirstMonth = (date.getMonth() / 3) * 3;
                return new Date(date.getYear(), quarterFirstMonth, 1).getTime();
            }
            case PERIOD_YEAR:
                return new Date(date.getYear(), 0, 1).getTime();
            case PERIOD_MONTH:
            default:
                return new Date(date.getYear(), date.getMonth(), 1).getTime();
        }
    }

    private long floorToMidnight(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate()).getTime();
    }

    private String bucketLabel(long startMillis, String period) {
        Date date = new Date(startMillis);
        switch (period) {
            case PERIOD_DAY:
                return dayLabel.format(date);
            case PERIOD_WEEK:
                return "wk " + dayLabel.format(date);
            case PERIOD_QUARTER:
                return "Q" + (date.getMonth() / 3 + 1) + " " + (date.getYear() + 1900);
            case PERIOD_YEAR:
                return String.valueOf(date.getYear() + 1900);
            case PERIOD_MONTH:
            default:
                return monthLabel.format(date);
        }
    }

    // ----------------------------------------------------------------------
    // Granularity auto-filtering based on the goal's period span.
    // Each granularity is offered only if it produces a reasonable number of
    // buckets across the goal's span (Day up to ~2 months, Week up to ~1 year,
    // Month/Quarter/Year up to 40). So a 1-month goal offers Day/Week/Month,
    // and a 1-year goal offers Week/Month/Quarter/Year.
    // ----------------------------------------------------------------------

    private SelectItem[] defaultPeriods() {
        return new SelectItem[]{
                new SelectItem(1, wfmStrings.all(), PERIOD_ALL),
                new SelectItem(2, wfmStrings.day(), PERIOD_DAY),
                new SelectItem(3, wfmStrings.week(), PERIOD_WEEK),
                new SelectItem(4, wfmStrings.month(), PERIOD_MONTH),
                new SelectItem(5, hrmsStrings.dgPeriodQuarter(), PERIOD_QUARTER),
                new SelectItem(6, wfmStrings.year(), PERIOD_YEAR),
                new SelectItem(7, wfmStrings.custom(), PERIOD_CUSTOM)
        };
    }

    private SelectItem[] availablePeriods(long spanDays) {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(1, wfmStrings.all(), PERIOD_ALL));

        if (spanDays <= 0) {
            // Unknown span (no dates) — offer every granularity
            items.add(new SelectItem(2, wfmStrings.day(), PERIOD_DAY));
            items.add(new SelectItem(3, wfmStrings.week(), PERIOD_WEEK));
            items.add(new SelectItem(4, wfmStrings.month(), PERIOD_MONTH));
            items.add(new SelectItem(5, hrmsStrings.dgPeriodQuarter(), PERIOD_QUARTER));
            items.add(new SelectItem(6, wfmStrings.year(), PERIOD_YEAR));
            items.add(new SelectItem(7, wfmStrings.custom(), PERIOD_CUSTOM));
            return items.toArray(new SelectItem[0]);
        }

        if (bucketCountInRange(spanDays, 60)) {            // Day: up to ~2 months
            items.add(new SelectItem(2, wfmStrings.day(), PERIOD_DAY));
        }
        if (bucketCountInRange(spanDays / 7, 53)) {        // Week: up to ~1 year
            items.add(new SelectItem(3, wfmStrings.week(), PERIOD_WEEK));
        }
        if (bucketCountInRange(spanDays / 30, 40)) {       // Month
            items.add(new SelectItem(4, wfmStrings.month(), PERIOD_MONTH));
        }
        if (bucketCountInRange(spanDays / 90, 40)) {       // Quarter
            items.add(new SelectItem(5, hrmsStrings.dgPeriodQuarter(), PERIOD_QUARTER));
        }
        if (bucketCountInRange(spanDays / 365, 40)) {      // Year
            items.add(new SelectItem(6, wfmStrings.year(), PERIOD_YEAR));
        }
        items.add(new SelectItem(7, wfmStrings.custom(), PERIOD_CUSTOM)); // Always offered
        return items.toArray(new SelectItem[0]);
    }

    private boolean bucketCountInRange(long bucketCount, long maxBuckets) {
        return bucketCount >= 1 && bucketCount <= maxBuckets;
    }

    private long goalSpanDays() {
        if (currentItem == null) {
            return 0;
        }
        Date from = toDate(currentItem.getFromDate());
        Date to = toDate(currentItem.getToDate());
        if (from == null || to == null) {
            return 0;
        }
        long ms = to.getTime() - from.getTime();
        return ms > 0 ? ms / DAY_MS : 0;
    }

    private Date toDate(DateNonConvertable dateNonConvertable) {
        return dateNonConvertable != null ? dateNonConvertable.getNonConvertedDate() : null;
    }

    // ----------------------------------------------------------------------
    // Custom range window (the "Custom" period opens this as a popup).
    // ----------------------------------------------------------------------

    private void openCustomRangePopup() {
        final KpiModal modal = new KpiModal();
        modal.setTitle(hrmsStrings.dgCustomRange());
        modal.setWidth(360);

        // The pickers are reused across opens; detach from any previous popup first.
        customFromPicker.removeFromParent();
        customToPicker.removeFromParent();
        syncCustomPickers();

        modal.addWidget(customFromPicker, wfmStrings.from());
        modal.addWidget(customToPicker, wfmStrings.to());

        modal.addButton(new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            applyCustomRange();
            modal.close();
        }));
        modal.drawCloseBtn(clickEvent -> modal.close());

        modal.open();
    }

    /**
     * Falls back to the goal's own [fromDate, toDate] when no custom window has been
     * set yet, so "Custom" always has a bounded range to render.
     */
    private void ensureDefaultCustomRange() {
        if (currentItem == null) {
            return;
        }
        if (customFrom == null) {
            customFrom = toMidnight(toDate(currentItem.getFromDate()));
        }
        if (customTo == null) {
            customTo = toEndOfDay(toDate(currentItem.getToDate()));
        }
    }

    private void syncCustomPickers() {
        if (customFrom != null) {
            customFromPicker.setDate(customFrom);
        } else {
            customFromPicker.clearSelected();
        }
        if (customTo != null) {
            customToPicker.setDate(customTo);
        } else {
            customToPicker.clearSelected();
        }
    }

    private void applyCustomRange() {
        customFrom = toMidnight(customFromPicker.getDate());
        customTo = toEndOfDay(customToPicker.getDate());
        // Avoid an unbounded (potentially huge) render if applied with empty pickers
        if (customFrom == null && customTo == null) {
            ensureDefaultCustomRange();
        }
        renderChart();
        saveSettings(); // persist the committed Custom window
    }

    private Date toMidnight(Date date) {
        return date != null ? new Date(date.getYear(), date.getMonth(), date.getDate()) : null;
    }

    private Date toEndOfDay(Date date) {
        return date != null ? new Date(date.getYear(), date.getMonth(), date.getDate(), 23, 59, 59) : null;
    }

    // ----------------------------------------------------------------------
    // Chart settings persistence (chart type + range/period + custom window).
    // ----------------------------------------------------------------------

    private void saveSettings() {
        if (currentItem == null || currentItem.getObjectId() == null) {
            return;
        }
        if (chartSettings == null) {
            chartSettings = new DepartmentGoalChartSettingsItem();
        }
        boolean periodApplicable = isPeriodApplicable();
        String period = periodApplicable ? selectedPeriod() : PERIOD_ALL;
        chartSettings.setChartType(selectedChartType());
        chartSettings.setPeriod(period);
        // The custom [from, to] window only matters in Custom mode (combo views only)
        chartSettings.setCustomFrom(periodApplicable && PERIOD_CUSTOM.equals(period) ? customFrom : null);
        chartSettings.setCustomTo(periodApplicable && PERIOD_CUSTOM.equals(period) ? customTo : null);
        HrmsService.App.get().saveDepartmentGoalChartSettings(
                currentItem.getObjectId(), chartSettings,
                new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void failure(Throwable throwable) {
                        // Persisting the preference is best-effort; stay silent on failure
                    }

                    @Override
                    public void success(Boolean result) {
                        // No UI feedback needed
                    }
                });
    }

    // ----------------------------------------------------------------------
    // Settings dialog (gear icon) — built from the ChartVisualSetting registry,
    // filtered to the current chart type. Apply writes back into chartSettings,
    // persists it, and re-renders.
    // ----------------------------------------------------------------------

    private static Widget toggleRow(String label1, Widget switch1, String label2, Widget switch2) {
        FlowPanel row = new FlowPanel();
        row.getElement().getStyle().setProperty("display", "flex");
        row.getElement().getStyle().setProperty("gap", "28px");
        row.add(inlineSwitch(label1, switch1));
        row.add(inlineSwitch(label2, switch2));
        return row;
    }

    private static Widget inlineSwitch(String label, Widget switchWidget) {
        FlowPanel group = new FlowPanel();
        group.getElement().getStyle().setProperty("display", "flex");
        group.getElement().getStyle().setProperty("flexDirection", "column");
        group.getElement().getStyle().setProperty("gap", "4px");
        group.add(new Label(label));
        group.add(switchWidget);
        return group;
    }

    private void openSettingsPopup() {
        if (chartSettings == null) {
            chartSettings = new DepartmentGoalChartSettingsItem();
        }
        final KpiModal modal = new KpiModal();
        modal.setTitle(hrmsStrings.dgChartSettingsTitle());
        modal.setWidth(420);

        final String type = selectedChartType();
        final Map<ChartVisualSetting, Widget> controls = new HashMap<>();
        boolean combinePieAndSeries = ChartVisualSetting.SHOW_PIE.appliesTo(type)
                && ChartVisualSetting.SHOW_SERIES.appliesTo(type);
        for (ChartVisualSetting setting : ChartVisualSetting.values()) {
            if (!setting.appliesTo(type)) {
                continue;
            }
            if (combinePieAndSeries && setting == ChartVisualSetting.SHOW_PIE) {
                Widget pieSwitch = buildSettingControl(ChartVisualSetting.SHOW_PIE);
                Widget seriesSwitch = buildSettingControl(ChartVisualSetting.SHOW_SERIES);
                controls.put(ChartVisualSetting.SHOW_PIE, pieSwitch);
                controls.put(ChartVisualSetting.SHOW_SERIES, seriesSwitch);
                modal.add(toggleRow(settingLabel(ChartVisualSetting.SHOW_PIE), pieSwitch,
                        settingLabel(ChartVisualSetting.SHOW_SERIES), seriesSwitch));
                continue;
            }
            if (combinePieAndSeries && setting == ChartVisualSetting.SHOW_SERIES) {
                continue; // already added together with Show pie, side by side
            }
            Widget control = buildSettingControl(setting);
            controls.put(setting, control);
            modal.addWidget(control, settingLabel(setting));
        }

        modal.addButton(new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            for (Map.Entry<ChartVisualSetting, Widget> entry : controls.entrySet()) {
                applyControlToSettings(entry.getKey(), entry.getValue());
            }
            modal.close();
            saveSettings();
            renderChart();
        }));
        modal.drawCloseBtn(clickEvent -> modal.close());
        modal.open();
    }

    private Widget buildSettingControl(ChartVisualSetting setting) {
        switch (setting) {
            case TARGET_COLOR:
                return colorControl(chartSettings.getTargetColor());
            case ACTUAL_COLOR:
                return colorControl(chartSettings.getActualColor());
            case SHOW_PIE: {
                KpiSwitcher sw = new KpiSwitcher();
                sw.setValue(Boolean.TRUE.equals(chartSettings.getShowPie()));
                return sw;
            }
            case SHOW_SERIES: {
                KpiSwitcher sw = new KpiSwitcher();
                sw.setValue(Boolean.TRUE.equals(chartSettings.getShowSeries()));
                return sw;
            }
            case LEGEND_POSITION:
                return dropdown(legendPositionOptions(), chartSettings.getLegendPosition());
            case LABEL_FORMAT:
                return dropdown(labelFormatOptions(), chartSettings.getLabelFormat());
            case PIE_STYLE:
                return dropdown(pieStyleOptions(), chartSettings.getPieStyle());
            default:
                return new HTML();
        }
    }

    private void applyControlToSettings(ChartVisualSetting setting, Widget control) {
        switch (setting) {
            case TARGET_COLOR:
                chartSettings.setTargetColor(((ColorWidget) control).getColor());
                break;
            case ACTUAL_COLOR:
                chartSettings.setActualColor(((ColorWidget) control).getColor());
                break;
            case SHOW_PIE:
                chartSettings.setShowPie(((KpiSwitcher) control).getValue());
                break;
            case SHOW_SERIES:
                chartSettings.setShowSeries(((KpiSwitcher) control).getValue());
                break;
            case LEGEND_POSITION:
                chartSettings.setLegendPosition(selectedDesc((DataListBox) control));
                break;
            case LABEL_FORMAT:
                chartSettings.setLabelFormat(selectedDesc((DataListBox) control));
                break;
            case PIE_STYLE:
                chartSettings.setPieStyle(selectedDesc((DataListBox) control));
                break;
            default:
        }
    }

    private static ColorWidget colorControl(String initialColor) {
        ColorWidget w = new ColorWidget();
        if (initialColor != null) {
            w.setColor(initialColor);
        }
        return w;
    }

    private static String selectedDesc(DataListBox list) {
        SelectItem selected = list.getSelectedItem(true);
        return selected != null && selected.getDescription() != null ? selected.getDescription() : null;
    }

    private DataListBox dropdown(SelectItem[] options, String selectedDesc) {
        DataListBox list = new DataListBox();
        list.setWithoutNullLabel(true);
        list.setItems(options);
        if (selectedDesc != null) {
            list.setSelectedByDescription(selectedDesc);
        }
        return list;
    }

    // ---- render-time setting helpers ----

    private void applyLegend(Chart chart) {
        String pos = chartSettings.getLegendPosition();
        if ("TOP".equals(pos)) {
            chart.setLegend(new Legend()
                    .setLayout(Legend.Layout.HORIZONTAL).setAlign(Legend.Align.LEFT)
                    .setVerticalAlign(Legend.VerticalAlign.TOP).setBorderWidth(0));
        } else if ("RIGHT".equals(pos)) {
            chart.setLegend(new Legend()
                    .setLayout(Legend.Layout.VERTICAL).setAlign(Legend.Align.RIGHT)
                    .setVerticalAlign(Legend.VerticalAlign.MIDDLE).setBorderWidth(0));
        } else if ("NONE".equals(pos)) {
            chart.setLegend(new Legend().setEnabled(false));
        } else { // BOTTOM / default
            chart.setLegend(new Legend()
                    .setLayout(Legend.Layout.HORIZONTAL).setAlign(Legend.Align.CENTER)
                    .setVerticalAlign(Legend.VerticalAlign.BOTTOM).setBorderWidth(0));
        }
    }

    private static String formatFrom(String fmt) {
        if ("PERCENTAGE".equals(fmt)) {
            return "{point.percentage:.1f}%";
        } else if ("VALUE_PERCENTAGE".equals(fmt)) {
            return "{point.y} ({point.percentage:.1f}%)";
        }
        return "{point.y}"; // VALUE / default
    }

    /**
     * Bar data labels. Column/bar series don't compute {point.percentage} (that's a pie
     * thing), so for percentage formats we compute it here as y / category-total * 100.
     */
    private DataLabels barLabelsFor(final String[] cats, final double[] totals) {
        final String fmt = chartSettings.getLabelFormat();
        DataLabels labels = new DataLabels().setEnabled(Boolean.TRUE.equals(chartSettings.getShowSeries()));
        if ("PERCENTAGE".equals(fmt) || "VALUE_PERCENTAGE".equals(fmt)) {
            labels.setFormatter(point -> {
                int idx = indexOf(cats, point.getXAsString());
                double total = idx >= 0 ? totals[idx] : point.getYAsDouble();
                double pct = total > 0 ? (point.getYAsDouble() / total) * 100.0 : 0.0;
                String pctStr = fmtNum(pct);
                return "PERCENTAGE".equals(fmt) ? pctStr + "%" : fmtNum(point.getYAsDouble()) + " (" + pctStr + "%)";
            });
        } else {
            labels.setFormat("{point.y}");
        }
        return labels;
    }

    private static int indexOf(String[] arr, String value) {
        if (value == null) {
            return -1;
        }
        for (int i = 0; i < arr.length; i++) {
            if (value.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }
    

    private static String fmtNum(double v) {
        if (v == Math.floor(v) && !Double.isInfinite(v)) {
            return Long.toString((long) v);
        }

        double rounded = Math.round(v * 10.0) / 10.0;
        return Double.toString(rounded);
    }

    private static class TimeSeriesData {
        private final List<String> labels = new ArrayList<>();
        private final List<Number> cumulative = new ArrayList<>();
        private double grandTotal = 0.0;
    }

    /**
     * Globally removes both 'View in full screen' and 'Print chart' options
     * from the Highcharts export context menus.
     */
    private native void customizeChartMenu() /*-{
        var wnd = $wnd || window;
        var hc = wnd.Highcharts || (window && window.Highcharts);

        if (hc) {
            try {
                var opts = hc.getOptions();
                if (opts && opts.exporting && opts.exporting.buttons && opts.exporting.buttons.contextButton) {
                    var defaultItems = opts.exporting.buttons.contextButton.menuItems;

                    if (defaultItems) {
                        // Filter out both full screen and print buttons
                        var filteredItems = defaultItems.filter(function(item) {
                            return item !== 'viewFullscreen' && item !== 'printChart';
                        });

                        // Clean up the top divider/separator line if it's left at the very top
                        if (filteredItems.length > 0 && filteredItems[0] === 'separator') {
                            filteredItems.shift();
                        }

                        hc.setOptions({
                            exporting: {
                                buttons: {
                                    contextButton: {
                                        menuItems: filteredItems
                                    }
                                }
                            }
                        });
                    }
                } else {
                    // Fallback directly to just the download options if options aren't fully ready
                    hc.setOptions({
                        exporting: {
                            buttons: {
                                contextButton: {
                                    menuItems: ['downloadPNG', 'downloadJPEG', 'downloadPDF', 'downloadSVG']
                                }
                            }
                        }
                    });
                }
            } catch (e) {
                console.log("Could not modify Highcharts menu: ", e);
            }
        }
    }-*/;
}
