package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.LegendPositionEnum;
import com.edatasite.workforce.gwt.chart.client.enums.SerieAggrTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.StackedEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationCFModal;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.JQuery;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ChartAdvancedOpt extends Composite {
    private static final ChartAdvancedOptUiBinder ourUiBinder = GWT.create(ChartAdvancedOptUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();

    private static final String MANUALLY = "MANUALLY";
    private static final String AUTOMATICALLY = "AUTOMATICALLY";
    private final TextBox txtChartTitle;

    @UiField
    Heading advancedOptLabel;
    @UiField(provided = true)
    FormGroup chartTitleField;
    @UiField(provided = true)
    FormGroup pageSizeField;
    private final DataListBox benchmarkBy;
    @UiField(provided = true)
    FormGroup benchmarField;
    @UiField(provided = true)
    FormGroup viewOption;
    @UiField(provided = true)
    FormGroup viewOptionType;
    @UiField(provided = true)
    FormGroup chartTypeField;
    private final TextBox txtChartScale;
    @UiField
    Span chartLabelOpt;
    @UiField
    Span seriesLabelOpt;
    @UiField
    HTMLPanel labelOptContainer;
    @UiField
    HTMLPanel stackedOptContainer;
    @UiField
    Span stackedOpt;
    @UiField
    KpiSwitcher stackedSwitcher;
    @UiField
    DataListBox seriesOptList;
    @UiField
    DataListBox stackedOptList;
    @UiField(provided = true)
    FormGroup legendOptField;
    @UiField
    Div gaugeChartAdvanceOptContaner;
    @UiField
    HTMLPanel advancedOptContainer;
    private final TextBox referenceFieldTextBox;
    private final DataListBox aggregateFunctionList;
    @UiField(provided = true)
    FormGroup chartScaleField;

    private GaugeChartOpt gaugeChartOpt;
    private final DataListBox pageSizeList;
    private final DataListBox viewOptions;
    private final DataListBox chartTypeOptions;
    private final DataListBox legendPosOptoin;
    private final DataListBox viewOptionTypeItems;
    private final DataListBox benchmarkAggFunc;
    private final TextBox benchmarkValueTextBox;
    @UiField(provided = true)
    FormGroup benchmarkType;
    @UiField(provided = true)
    FormGroup referenceFieldName;
    @UiField(provided = true)
    FormGroup aggregateFunctionValue;
    LocalizationCFModal localizationCFModal;
    private CustomFormLocalization localizationCF;

    @UiField
    HTMLPanel pieChartOptContainer;
    @UiField
    Span pieChartOptLabel;
    @UiField
    KpiSwitcher pieChartSwitcher;

    @UiField(provided = true)
    FormGroup pieChartPositionField;
    private final DataListBox pieChartPositionList;

    public ChartAdvancedOpt() {
        txtChartTitle = new TextBox();
        chartTitleField = new FormGroup(txtChartTitle);
        txtChartTitle.addKeyUpHandler(ch -> {
            if (cmdChangeTitle != null) {
                cmdChangeTitle.execute();
            }
        });
        initChartTitleToolTip(chartTitleField);

        //page size field config
        pageSizeList = new DataListBox();
        pageSizeList.setWithoutNullLabel(true);
        pageSizeList.setItems(getPagesizeList());
        pageSizeList.setSelected(5); //default value of the page size

        pageSizeField = new FormGroup(reportingStrings.showItems(), pageSizeList);

        benchmarkBy = new DataListBox();
        benchmarkBy.setItems(getBenchmarkTypeItems());
        benchmarkBy.setSelectedByDescription(MANUALLY);
        benchmarkType = new FormGroup(reportingStrings.benchmarkforyaxis(), benchmarkBy);


        benchmarkValueTextBox = new TextBox();
        benchmarkValueTextBox.setVisible(true);
        Validation.addNumericKeyboardListener(benchmarkValueTextBox, 3);
        benchmarkAggFunc = new DataListBox();
        benchmarkAggFunc.setWithoutNullLabel(false);
        benchmarkAggFunc.setVisible(false);
        benchmarkAggFunc.setItems(getAggrFList());
        Div div = new Div();
        div.add(benchmarkAggFunc);
        div.add(benchmarkValueTextBox);
        benchmarField = new FormGroup(wfmStrings.value(), div);

        txtChartScale = new TextBox();
        chartScaleField = new FormGroup(wfmStrings.decimalPlaces(), txtChartScale);
        Validation.addNumericKeyboardListener(txtChartScale);
        viewOptions = new DataListBox();
        viewOptions.setSelectedByValue("2D", true);
        viewOptions.setWithoutNullLabel(true);
        viewOptions.setItems(new SelectItem[]{new SelectItem(1, "2D"), new SelectItem(2, "3D")});
        viewOption = new FormGroup("View Options", viewOptions);
        viewOption.setVisible(false);

        chartTypeOptions = new DataListBox();
        chartTypeOptions.setWithoutNullLabel(true);
        chartTypeOptions.setItems(new SelectItem[]
                {new SelectItem(ChartTypeEnum.FUNNEL_CHART.getId(), ChartTypeEnum.FUNNEL_CHART.getTitle()),
                        new SelectItem(ChartTypeEnum.PYRAMID_CHART.getId(), ChartTypeEnum.PYRAMID_CHART.getTitle())});
        chartTypeField = new FormGroup("Chart Type", chartTypeOptions);
        chartTypeField.setVisible(true);

        legendPosOptoin = new DataListBox();
        legendPosOptoin.setWithoutNullLabel(false);
        legendPosOptoin.setItems(getLegendPositionList());
        legendOptField = new FormGroup(reportingStrings.showLegend(), legendPosOptoin);
        legendOptField.setVisible(true);

        aggregateFunctionList = new DataListBox();
        aggregateFunctionList.setWithoutNullLabel(false);
        aggregateFunctionList.setItems(getAggrFList());
        aggregateFunctionList.setSelectedByDescription(SerieAggrTypeEnum.SUM.getFunction());
        referenceFieldTextBox = new TextBox();
        referenceFieldName = new FormGroup(wfmStrings.reference(), referenceFieldTextBox);
        aggregateFunctionValue = new FormGroup(wfmStrings.operationType(), aggregateFunctionList);

        viewOptionTypeItems = new DataListBox();
        viewOptionTypeItems.setWithoutNullLabel(true);
        viewOptionTypeItems.setItems(getOptionValues(false));
        viewOptionTypeItems.setSelectedIndex(1);
        viewOptionType = new FormGroup(wfmStrings.options(), viewOptionTypeItems);
        viewOptionType.setVisible(false);
        viewOptionType.setStyleName("col-4");

        pieChartPositionList = new DataListBox();
        pieChartPositionList.setWithoutNullLabel(true);
        pieChartPositionField = new FormGroup("Combo " + wfmStrings.location(), pieChartPositionList);
        pieChartPositionField.setVisible(false);

        initWidget(ourUiBinder.createAndBindUi(this));

        advancedOptLabel.setText(wfmStrings.advancedOptions());
        chartLabelOpt.setText(reportingStrings.showLabel());
        seriesLabelOpt.setText(reportingStrings.showSerie());
        stackedOpt.setText(reportingStrings.stacked());

        labelOptContainer.addStyleName("active");
        pieChartOptLabel.setText("Combo");

        initDefaultLists();
        initCommandHandlers();
    }

    private SelectItem[] getBenchmarkTypeItems() {

        return new SelectItem[]{
                new SelectItem(1, wfmStrings.enterManually(), MANUALLY),
                new SelectItem(2, wfmStrings.automatically(), AUTOMATICALLY)
        };
    }

    private Command cmdChangeOptions;
    private Command cmdChangeTitle;
    private SelectItem[] columns;
    private ChartTypeEnum chartTypeEnum;


    public ChartConfItem getAdvancedOptData(ChartConfItem chartConfItem) {
        chartConfItem.setTitle(txtChartTitle.getText());
        if (ChartTypeEnum.GAUGE_CHART.equals(chartConfItem.getType())) {
            pageSizeField.setVisible(false);
            pageSizeList.setVisible(false);
            chartScaleField.removeStyleName("col-6");
            chartScaleField.setStyleName("col-12", true);
        } else {
            pageSizeField.setVisible(true);
            pageSizeList.setVisible(true);
            chartScaleField.removeStyleName("col-12");
            chartScaleField.setStyleName("col-6", true);
        }
        chartConfItem.setPageSize(pageSizeList.getSelectedId());
        chartConfItem.setChartViewOption(viewOptions.getSelectedItem(true).getName());
        chartConfItem.setChartViewOptionType(viewOptionTypeItems.getSelectedItem() != null ? viewOptionTypeItems.getSelectedItem().getDescription() : null);
        if (MANUALLY.equals(benchmarkBy.getSelectedItem(true).getDescription())) {
            if (benchmarkValueTextBox.getText() != null && !benchmarkValueTextBox.getText().isEmpty()) {
                chartConfItem.setBenchmarkValue(Utils.parseToBigDecimal(benchmarkValueTextBox.getText()));
            } else {
                chartConfItem.setBenchmarkValue(BigDecimal.ZERO);
            }
            chartConfItem.setBenchmarkAggFuncVal(null);
        } else if (AUTOMATICALLY.equals(benchmarkBy.getSelectedItem(true).getDescription()) && !getAreaCharts()) {
            if (benchmarkAggFunc.getSelectedItem() != null && !benchmarkAggFunc.getSelectedId(true).equals(-1)) {
                chartConfItem.setBenchmarkAggFuncVal(benchmarkAggFunc.getSelectedItem().getDescription());
            } else {
                chartConfItem.setBenchmarkAggFuncVal(null);
            }
            chartConfItem.setBenchmarkValue(BigDecimal.ZERO);
        } else {
            chartConfItem.setBenchmarkValue(BigDecimal.ZERO);
            chartConfItem.setBenchmarkAggFuncVal(null);
        }
        if (txtChartScale.getText() != null && !txtChartScale.getText().isEmpty()) {
            chartConfItem.setScale(txtChartScale.getText());
        } else {
            chartConfItem.setScale(BigInteger.ZERO.toString());
        }


        if (ChartTypeEnum.GAUGE_CHART.equals(chartConfItem.getType()) && gaugeChartOpt != null) {
            chartConfItem = gaugeChartOpt.getGaugeChartOpt(chartConfItem);
        } else {
            if (stackedOptList.getSelectedId() != null && stackedOptList.getSelectedId() != -1) {
                chartConfItem.setStacked(StackedEnum.getById(stackedOptList.getSelectedId()));
            } else {
                chartConfItem.setStacked(null);
            }
            chartConfItem.setShowSerie(seriesOptList.getSelectedId() != null && seriesOptList.getSelectedId() != 1);
            chartConfItem.setLegend(legendPosOptoin.getSelectedId() != null ? LegendPositionEnum.getById(legendPosOptoin.getSelectedId()) : null);
            chartConfItem.setShowStacked(stackedSwitcher.getValue());
            chartConfItem.setShowPieChart(pieChartSwitcher.getValue());
        }
        // Save the position if the list has a valid selection
        if (pieChartPositionList.getSelectedItem(true) != null) {
            chartConfItem.setPieChartPosition(pieChartPositionList.getSelectedItem(true).getDescription());
        }
        ChartTypeEnum chartType = chartConfItem.getType();
        if ((ChartTypeEnum.PIE_CHART.equals(chartType) || ChartTypeEnum.DONUT_CHART.equals(chartType)
                || ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartType) || ChartTypeEnum.FUNNEL_CHART.equals(chartType))) {
            chartConfItem.setTotalFieldName(referenceFieldTextBox.getText());
            if (aggregateFunctionList.getSelectedItem(false) != null) {
                chartConfItem.setAgrigateItemCode(aggregateFunctionList.getSelectedItem(false).getDescription());
            } else {
                chartConfItem.setAgrigateItemCode("not_show");
            }
            referenceFieldName.setVisible(true);
            aggregateFunctionValue.setVisible(true);
        } else {
            chartConfItem.setTotalFieldName(null);
            chartConfItem.setAgrigateItemCode("not_show");
            referenceFieldName.setVisible(false);
            aggregateFunctionValue.setVisible(false);
        }


        if (ChartTypeEnum.FUNNEL_CHART.equals(chartType) || ChartTypeEnum.DONUT_CHART.equals(chartType)) {
            if (chartConfItem.getAgrigateItemCode() == null || chartConfItem.getAgrigateItemCode().isEmpty()) {
                chartConfItem.setBenchmarkAggFuncVal(SerieAggrTypeEnum.SUM.getFunction());
            }
        }
        chartConfItem.setSelectedchartTypeId(chartTypeOptions.getSelectedId() != null ? chartTypeOptions.getSelectedId() : -1);
        return chartConfItem;
    }

    private SelectItem[] getPagesizeList() {
        SelectItem[] items = new SelectItem[4];

        items[0] = new SelectItem(0, wfmStrings.all(), "");
        items[1] = new SelectItem(5, wfmStrings.top() + " 5", "5", true);
        items[2] = new SelectItem(10, wfmStrings.top() + " 10", "10");
        items[3] = new SelectItem(20, wfmStrings.top() + " 20", "20");
        return items;
    }

    public void setChartConfig(ChartConfItem configItem, ChartTypeEnum chartType) {
        this.chartTypeEnum = chartType;
        if (ChartTypeEnum.GAUGE_CHART.equals(chartType)) {
            gaugeChartAdvanceOptContaner.setVisible(true);
            advancedOptContainer.setVisible(false);
            visibleFields(false);
            if (gaugeChartOpt == null) {
                gaugeChartOpt = new GaugeChartOpt(configItem, columns);
                gaugeChartOpt.setCmdChangeGaugeConfig(cmdChangeOptions);
                gaugeChartAdvanceOptContaner.add(gaugeChartOpt);
                gaugeChartOpt.setColorData(configItem);
            } else {
                gaugeChartOpt.setCmdChangeGaugeConfig(cmdChangeOptions);
            }
            return;
        } else {
            visibleFields(true);
            advancedOptContainer.setVisible(true);
            gaugeChartAdvanceOptContaner.setVisible(false);
        }

        benchmarField.setVisible(!(ChartTypeEnum.PIE_CHART.equals(chartType) || ChartTypeEnum.DONUT_CHART.equals(chartType)
                || ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartType) || ChartTypeEnum.FUNNEL_CHART.equals(chartType)
                || ChartTypeEnum.GAUGE_CHART.equals(chartType)));

        chartTypeField.setVisible(ChartTypeEnum.FUNNEL_CHART.equals(chartType));
        if (configItem != null) {
            txtChartTitle.setText(configItem.getTitle());
            labelOptContainer.addStyleName("active");
            stackedOptList.setSelected(configItem.getStacked() != null && configItem.getStacked().getId() != -1 ? configItem.getStacked().getId() : -1);
            seriesOptList.setSelected(configItem.isShowSerie() ? 2 : 1);
            legendPosOptoin.setSelected(configItem.getLegend() != null ? configItem.getLegend().getId() : null);
            pageSizeList.setSelected(configItem.getPageSize());
            if (configItem.getBenchmarkValue() != null && !configItem.getBenchmarkValue().setScale(3, RoundingMode.HALF_UP).equals(BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP))) {
                benchmarkValueTextBox.setVisible(true);
                benchmarkAggFunc.setVisible(false);
                benchmarkValueTextBox.setText(Utils.formatDouble(configItem.getBenchmarkValue().doubleValue()));
                benchmarkBy.setSelectedByDescription(MANUALLY);
            } else if (!Utils.isNullOrEmpty(configItem.getBenchmarkAggFuncVal())) {
                benchmarkValueTextBox.setVisible(false);
                benchmarkAggFunc.setVisible(true);
                benchmarkAggFunc.setSelectedByDescription(configItem.getBenchmarkAggFuncVal());
                benchmarkBy.setSelectedByDescription(AUTOMATICALLY);
            }


            if (configItem.getChartViewOption() != null) {
                viewOptions.setSelectedByValue(configItem.getChartViewOption(), true);
            }

            if (configItem.getChartViewOptionType() != null) {
                viewOptionTypeItems.setSelectedByDescription(configItem.getChartViewOptionType());
            }

            if (configItem.getScale() != null) {
                txtChartScale.setValue(configItem.getScale());
            }

            if (ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartType) || ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartType)) {
                viewOption.setVisible(true);
                if ("3D".equals(configItem.getChartViewOption())) {
                    viewOptionTypeItems.setItems(getOptionValues(true));
                    viewOptionType.setVisible(true);
                } else {
                    viewOptionType.setVisible(false);
                }
            } else if (ChartTypeEnum.AREA_CHART.equals(chartType) ||
                    ChartTypeEnum.PIE_CHART.equals(chartType) ||
                    ChartTypeEnum.DONUT_CHART.equals(chartType) ||
                    ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartType)) {
                viewOption.setVisible(true);
                viewOptionType.setVisible(false);
            } else {
                viewOption.setVisible(false);
                viewOptionType.setVisible(false);
            }
            if (ChartTypeEnum.PIE_CHART.equals(chartType) ||
                    ChartTypeEnum.DONUT_CHART.equals(chartType) ||
                    ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartType) ||
                    ChartTypeEnum.FUNNEL_CHART.equals(chartType)) {
                benchmarkType.setVisible(false);
                benchmarField.setVisible(false);
            } else {
                benchmarkType.setVisible(true);
                benchmarField.setVisible(true);
            }

            stackedOptContainer.removeStyleName("active");
            stackedOptContainer.setVisible(
                    ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartType)
                            || ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartType)
                            || ChartTypeEnum.AREA_CHART.equals(chartType)
            );

            if (configItem.isShowStacked() &&
                    (ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartType)
                            || ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartType)
                            || ChartTypeEnum.AREA_CHART.equals(chartType))) {

                stackedSwitcher.setValue(Boolean.TRUE);
            } else {
                stackedSwitcher.setValue(Boolean.FALSE);
            }

            pieChartOptContainer.setVisible(
                    ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartType)
                            || ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartType));

            if (configItem.isShowPieChart() &&
                    (ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartType)
                            || ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartType))) {
                pieChartSwitcher.setValue(Boolean.TRUE);
            } else {
                pieChartSwitcher.setValue(Boolean.FALSE);
            }

            if ((ChartTypeEnum.PIE_CHART.equals(chartType) || ChartTypeEnum.DONUT_CHART.equals(chartType)
                    || ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartType) || ChartTypeEnum.FUNNEL_CHART.equals(chartType))) {
                seriesOptList.setEnabled(false);
                referenceFieldTextBox.setText(configItem.getTotalFieldName() != null ? configItem.getTotalFieldName() : "");
                aggregateFunctionList.setSelectedByDescription(configItem.getTotalFieldName());
                referenceFieldTextBox.setVisible(true);
                aggregateFunctionList.setVisible(true);
            } else {
                seriesOptList.setEnabled(true);
                referenceFieldTextBox.setVisible(false);
                aggregateFunctionList.setVisible(false);
            }
        } else {

            if (!(ChartTypeEnum.AREA_CHART.equals(chartType) || ChartTypeEnum.LINE_CHART.equals(chartType))) {
                labelOptContainer.addStyleName("active");
                stackedOptList.setSelected(StackedEnum.BY_VALUE.getId());
            }
            seriesOptList.setSelected(1);
            stackedOptContainer.setVisible(
                    ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartType)
                            || ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartType)
                            || ChartTypeEnum.AREA_CHART.equals(chartType)
            );

            pieChartOptContainer.setVisible(
                    ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartType)
                            || ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartType)
            );
            pieChartSwitcher.setValue(Boolean.FALSE);

        }

        if (ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartType)) {
            pieChartPositionList.setItems(new SelectItem[]{
                    new SelectItem(1, wfmStrings.left(), "LEFT"),
                    new SelectItem(2, wfmStrings.center(), "CENTER"),
                    new SelectItem(3, wfmStrings.right(), "RIGHT")
            });
        } else if (ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartType)) {
            pieChartPositionList.setItems(new SelectItem[]{
                    new SelectItem(1, wfmStrings.top(), "TOP"),
                    new SelectItem(2, wfmStrings.middle(), "MID"),
                    new SelectItem(3, wfmStrings.bottom(), "BOTTOM")
            });
        }

        pieChartPositionField.setVisible(configItem.isShowPieChart() && pieChartSwitcher.getValue());

        if (configItem.getPieChartPosition() != null) {
            pieChartPositionList.setSelectedByDescription(configItem.getPieChartPosition());
        } else {
            pieChartPositionList.setSelectedByDescription(ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartType) ? "LEFT" : "TOP");
        }

        viewOption.setVisible(!(ChartTypeEnum.LINE_CHART.equals(chartType) || ChartTypeEnum.FUNNEL_CHART.equals(chartType)));
    }

    private void visibleFields(boolean visible) {
        viewOption.setVisible(visible);
        viewOptionType.setVisible(visible);
        benchmarkType.setVisible(visible);
        benchmarField.setVisible(visible);
        referenceFieldName.setVisible(visible);
        aggregateFunctionValue.setVisible(visible);
    }

    private void initCommandHandlers() {

        txtChartScale.addValueChangeHandler(ch -> {
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });

        stackedSwitcher.addValueChangeHandler(ch -> {

            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });
        stackedOptList.addValueChangeHandler(ch -> {

            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });

        seriesOptList.addValueChangeHandler(ch -> {

            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });

        pageSizeList.addValueChangeHandler(ch -> {

            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });
        benchmarkValueTextBox.addKeyUpHandler(ch -> {
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });
        benchmarkAggFunc.addValueChangeHandler(ch -> {
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });

        pieChartSwitcher.addValueChangeHandler(ch -> {
            pieChartPositionField.setVisible(ch.getValue()); // Show/Hide Combo Position
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });

        pieChartPositionList.setChangeEvent(() -> {
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });

        benchmarkBy.addValueChangeHandler(ch -> {
            if (MANUALLY.equals(benchmarkBy.getSelectedItem(true).getDescription())) {
                benchmarkValueTextBox.setVisible(true);
                benchmarkAggFunc.setVisible(false);
                benchmarkAggFunc.resetSelectedItem();
                benchmarkAggFunc.setSelectedIndex(-1);
            } else if (AUTOMATICALLY.equals(benchmarkBy.getSelectedItem(true).getDescription())) {
                benchmarkValueTextBox.setVisible(false);
                benchmarkAggFunc.setVisible(true);
                benchmarkValueTextBox.setText(Utils.formatDouble(0.0));
            } else {
                benchmarkValueTextBox.setVisible(true);
                benchmarkAggFunc.setVisible(false);
                benchmarkAggFunc.resetSelectedItem();
                benchmarkAggFunc.setSelectedIndex(-1);
                benchmarkValueTextBox.setText(Utils.formatDouble(0.0));
            }
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });

        viewOptions.setChangeEvent(() -> {
            if (cmdChangeOptions != null) {
                viewOptionChanged(viewOptions.getSelectedItem().getName());
                cmdChangeOptions.execute();
            }
        });

        viewOptionTypeItems.setChangeEvent(() -> {
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });
        aggregateFunctionList.setChangeEvent(() -> {
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });

        referenceFieldTextBox.addValueChangeHandler((handler) -> {
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });

        chartTypeOptions.setChangeEvent(() -> {
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });

        legendPosOptoin.setChangeEvent(() -> {
            if (cmdChangeOptions != null) {
                cmdChangeOptions.execute();
            }
        });
    }

    public void setVisibleBenchmark(boolean visible) {
        benchmarField.setVisible(visible);
    }

    public void setCmdChangeOptions(Command cmd) {
        cmdChangeOptions = cmd;
    }

    public void setCmdChangeTitle(Command cmdChangeTitle) {
        this.cmdChangeTitle = cmdChangeTitle;
    }

    public void setChartTitle(String chartTitle) {
        txtChartTitle.setText(chartTitle);
    }

    public TextBox getChartTitleBox() {
        return txtChartTitle;
    }

    public void setColumns(SelectItem[] columns) {
        this.columns = columns;
    }

    interface ChartAdvancedOptUiBinder extends UiBinder<Widget, ChartAdvancedOpt> {

    }

    private void initDefaultLists() {

        SelectItem[] showSerieList = new SelectItem[]{
                new SelectItem(1, wfmStrings.no()),
                new SelectItem(2, wfmStrings.yes())
        };
        seriesOptList.setWithoutNullLabel(true);
        seriesOptList.setItems(showSerieList);


        SelectItem[] stackedList = new SelectItem[]{
                new SelectItem(StackedEnum.BY_VALUE.getId(), StackedEnum.BY_VALUE.getTitle()),
                new SelectItem(StackedEnum.BY_PERCENT.getId(), StackedEnum.BY_PERCENT.getTitle()),
                new SelectItem(StackedEnum.BY_PERCENTANDVALUE.getId(), StackedEnum.BY_PERCENTANDVALUE.getTitle())
        };
        stackedOptList.setWithoutNullLabel(false);
        stackedOptList.setItems(stackedList);
    }

    private Span tooltipWrapper;

    private void initChartTitleToolTip(FormGroup chartTitleField) {
        Span adAsDashboardTitle = new Span(wfmStrings.title());
        tooltipWrapper = new Span();

        setTooltipClass();
        Window.addResizeHandler(e -> {
            setTooltipClass();
        });


        Icon iInfo = new Icon();
        iInfo.setClass("ficon--info");
        MaterialLink iconLink = new MaterialLink();
        iconLink.add(iInfo);
        String activation = "infoDropDown2";
        iconLink.setActivates(activation);

        MaterialDropDown dropDown = new MaterialDropDown(activation);
        dropDown.addStyleName("dropdown-content dropdown-content-tooltip tooltip-long-text");
        dropDown.getElement().setInnerHTML(wfmStrings.chartTitleDescription());
        dropDown.setHover(true);

        MaterialLink localeLink = new MaterialLink(wfmStrings.localization());
        Span localization = new Span();
        localization.add(localeLink);
        localeLink.addClickHandler(event -> {
            localizationCFModal = new LocalizationCFModal(localizationCF != null ? localizationCF : null, LocalizationTypeEnum.DASHBOARD_COMPONENT);
            localizationCFModal.center();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCALIZATION_ADD, ChartAdvancedOpt.this, (sender, args) -> {
            if (args != null) {
                localizationCF = (CustomFormLocalization) args;
            }
        });

        tooltipWrapper.add(iconLink);
        tooltipWrapper.add(dropDown);

        chartTitleField.getGroupLabel().add(adAsDashboardTitle);
        chartTitleField.getGroupLabel().add(new Span(" "));
        chartTitleField.getGroupLabel().add(tooltipWrapper);
        chartTitleField.getGroupLabel().add(new Span(" "));
        chartTitleField.getGroupLabel().add(localeLink);
    }

    private void setTooltipClass() {
        int frameWidth = JQuery.$(".frame__content__body.scroll-content").outerWidth();

        if (frameWidth < 960) {
            tooltipWrapper.setStyleName("dropdown-kit--arrow--right");
        } else {
            tooltipWrapper.setStyleName("dropdown-kit--arrow--left");
        }
    }

    public Integer getStackedSelected() {
        return stackedSwitcher.getValue() ? stackedOptList.getSelectedItem(true).getId() : null;
    }

    private SelectItem[] getOptionValues(boolean withItem) {
        int i = 0;
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        selectItems.add(new SelectItem(++i, wfmStrings.line(), "LINE"));
        selectItems.add(new SelectItem(++i, reportingStrings.lineStacking(), "LINE_STACKING"));
        if (ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartTypeEnum)) {
            selectItems.add(new SelectItem(++i, wfmStrings.cylinder(), "CYLINDER"));
            selectItems.add(new SelectItem(++i, reportingStrings.cylinderStacking(), "CYLINDER_STACKING"));
        }
        if (withItem) {
            return selectItems.toArray(new SelectItem[]{});
        } else {
            return new SelectItem[]{};
        }
    }

    private boolean getAreaCharts() {
        return ChartTypeEnum.PIE_CHART.equals(chartTypeEnum)
                || ChartTypeEnum.DONUT_CHART.equals(chartTypeEnum)
                || ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartTypeEnum)
                || ChartTypeEnum.FUNNEL_CHART.equals(chartTypeEnum)
                || ChartTypeEnum.GAUGE_CHART.equals(chartTypeEnum);
    }

    private void viewOptionChanged(String selected) {
        viewOptionTypeItems.clear();
        if ((ChartTypeEnum.HORIZONTAL_BAR_CHART.equals(chartTypeEnum)
                || ChartTypeEnum.VERTICAL_BAR_CHART.equals(chartTypeEnum))) {
            viewOptionTypeItems.setItems(getOptionValues(true));
            viewOptionType.setVisible("3D".equals(selected));
        } else {
            viewOption.setVisible(!(ChartTypeEnum.LINE_CHART.equals(chartTypeEnum) || ChartTypeEnum.FUNNEL_CHART.equals(chartTypeEnum)));
            viewOptionTypeItems.setItems(getOptionValues(false));
            viewOptionType.setVisible(false);
//            viewOptionTypeItems.setSelectedIndex(-1);
        }
    }

    public CustomFormLocalization getLocalization() {
        return localizationCF;
    }

    public void setLocalization(CustomFormLocalization localizationCF) {
        this.localizationCF = localizationCF;
    }

    private SelectItem[] getAggrFList() {

        SelectItem[] aggregateFuncListItems = new SelectItem[5];
        aggregateFuncListItems[0] = new SelectItem(SerieAggrTypeEnum.SUM.getId(), wfmStrings.sum(), SerieAggrTypeEnum.SUM.getFunction());
        aggregateFuncListItems[1] = new SelectItem(SerieAggrTypeEnum.COUNT.getId(), wfmStrings.count(), SerieAggrTypeEnum.COUNT.getFunction());
        aggregateFuncListItems[2] = new SelectItem(SerieAggrTypeEnum.AVG.getId(), wfmStrings.average(), SerieAggrTypeEnum.AVG.getFunction());
        aggregateFuncListItems[3] = new SelectItem(SerieAggrTypeEnum.MAX.getId(), wfmStrings.max(), SerieAggrTypeEnum.MAX.getFunction());
        aggregateFuncListItems[4] = new SelectItem(SerieAggrTypeEnum.MIN.getId(), wfmStrings.min(), SerieAggrTypeEnum.MIN.getFunction());

        return aggregateFuncListItems;
    }

    private SelectItem[] getLegendPositionList() {
        SelectItem[] legendPositionList = new SelectItem[]{
                new SelectItem(LegendPositionEnum.TOP.getId(), LegendPositionEnum.TOP.getTitle()),
                new SelectItem(LegendPositionEnum.RIGHT.getId(), LegendPositionEnum.RIGHT.getTitle()),
                new SelectItem(LegendPositionEnum.BOTTOM.getId(), LegendPositionEnum.BOTTOM.getTitle())
        };
        return legendPositionList;
    }

}