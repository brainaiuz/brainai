package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.GaugeChartConfig;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieColumn;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;

public class GaugeChartOpt extends Composite {
    private static final GaugeChartOptUiBinder ourUiBinder = GWT.create(GaugeChartOptUiBinder.class);
    private final Div minFieldItemContainer;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    @UiField(provided = true)
    FormGroup minValueField;

    @UiField(provided = true)
    FormGroup maxValueField;
    @UiField(provided = true)
    ChartSerie serieField;


    private DataListBox dwMinTypeList;
    private DataListBox dwMinColumnList;
    private TextBox txtMinValue;

    private DataListBox dwMaxTypeList;
    private DataListBox dwMaxColumnList;
    private TextBox txtMaxValue;
    private final Div maxFieldItemContainer;
    private final SelectItem[] columns;
    private final ChartConfItem chartConfigItem;
    private SelectItem[] columnsForMinMax;
    private final AdvancedSerieColorWidget advancedSerieColorWidget;
    private Command cmdChangeGaugeConfig;

    public GaugeChartOpt(ChartConfItem chartConfigItem, SelectItem[] columns/*, AdvancedSerieColorWidget advancedSerieColorWidget*/) {
        this.chartConfigItem = chartConfigItem;
        this.columns = columns;
        this.advancedSerieColorWidget = new AdvancedSerieColorWidget(() -> {
            if (cmdChangeGaugeConfig != null) {
                cmdChangeGaugeConfig.execute();
            }
        }, false);
        advancedSerieColorWidget.setActiveChartType(ChartTypeEnum.GAUGE_CHART);
        if (chartConfigItem != null && chartConfigItem.getGaugeConfig() != null) {
            advancedSerieColorWidget.setData(chartConfigItem.getGaugeConfig().getGaugeSerie());
        }
        initColumnsForMinMax();

        minFieldItemContainer = new Div();
        maxFieldItemContainer = new Div();

        minValueField = new FormGroup("Min. Value", minFieldItemContainer);
        maxValueField = new FormGroup("Max. Value", maxFieldItemContainer);
        serieField = new ChartSerie(columns, cmdChangeGaugeConfig, null, null, advancedSerieColorWidget);

        initWidget(ourUiBinder.createAndBindUi(this));
        serieField.hideSerieName();
//        serieField.getSerieContainer().setClass("col-12");

        initialization();
        loadConfig();
    }


    public ChartConfItem getGaugeChartOpt(ChartConfItem chartConfigItem) {
        GaugeChartConfig config = chartConfigItem.getGaugeConfig();

        if (config == null) {
            config = new GaugeChartConfig();
        }

        if (FROM_SYSTEM == dwMinTypeList.getSelectedId()) {
            config.setGaugeMinColumn(getSerieColumn(dwMinColumnList.getSelectedItem()));
            config.setGaugeMinValue(null);
        } else {
            config.setGaugeMinValue(!Utils.isNullOrEmpty(txtMinValue.getText()) ? Double.valueOf(txtMinValue.getText()) : 0d);
            config.setGaugeMinColumn(null);
        }

        if (ENTER_MANUALLY == dwMaxTypeList.getSelectedId()) {
            config.setGaugeMaxValue(!Utils.isNullOrEmpty(txtMaxValue.getText()) ? Double.valueOf(txtMaxValue.getText()) : 0d);
            config.setGaugeMaxColumn(null);
        } else {
            config.setGaugeMaxColumn(getSerieColumn(dwMaxColumnList.getSelectedItem()));
            config.setGaugeMaxValue(null);
        }

        config.setGaugeSerie(serieField.getSerieConf());
//        if (config.getGaugeSerie() != null){
//            config.getGaugeSerie().setColorList(advancedSerieColorWidget.getData(serieField));
//        }

        if (config.getGaugeSerie() != null) {
            chartConfigItem.setGaugeConfig(config);
        } else {
            chartConfigItem.setGaugeConfig(null);
        }
        return chartConfigItem;
    }

    public void setColorData(ChartConfItem chartConf) {
        if (chartConf != null && chartConf.getGaugeConfig() != null && chartConf.getGaugeConfig().getGaugeSerie() != null) {
            advancedSerieColorWidget.setData(serieField, chartConf.getGaugeConfig().getGaugeSerie());
        }
    }

    private void initialization() {
        dwMinTypeList = new DataListBox();
        dwMinTypeList.setWithoutNullLabel(true);
        dwMinTypeList.setItems(getTypes());
        dwMinTypeList.setSelected(new SelectItem(ENTER_MANUALLY));
        dwMinTypeList.addValueChangeHandler(vch -> {
            onChangeMinValueTypes();
        });


        dwMinColumnList = new DataListBox();
        dwMinColumnList.setWithoutNullLabel(true);
        dwMinColumnList.setItems(columnsForMinMax);

        txtMinValue = new TextBox();
        txtMinValue.setText("0");
        if (Utils.getAccountingCalculationScale() != null) {
            Validation.addNumericKeyboardListener(txtMinValue, Utils.getAccountingCalculationScale());
        }
        dwMaxTypeList = new DataListBox();
        dwMaxTypeList.setWithoutNullLabel(true);
        dwMaxTypeList.setItems(getTypes());
        dwMaxTypeList.setSelected(new SelectItem(FROM_SYSTEM));
        dwMaxTypeList.addValueChangeHandler(vch -> {
            onChangeMaxValueTypes();
        });

        dwMaxColumnList = new DataListBox();
        dwMaxColumnList.setWithoutNullLabel(true);
        dwMaxColumnList.setItems(columnsForMinMax);

        txtMaxValue = new TextBox();
        if (Utils.getAccountingCalculationScale() != null) {
            Validation.addNumericKeyboardListener(txtMaxValue, Utils.getAccountingCalculationScale());
        }

        if (columnsForMinMax != null && columnsForMinMax.length > 0) {
            dwMaxColumnList.setSelected(columnsForMinMax[0]);
        }

        initHandlers();
    }

    private void initHandlers() {
        dwMinTypeList.addValueChangeHandler(ch -> {

            if (cmdChangeGaugeConfig != null) {
                cmdChangeGaugeConfig.execute();
            }
        });
        dwMinColumnList.addValueChangeHandler(ch -> {

            if (cmdChangeGaugeConfig != null) {
                cmdChangeGaugeConfig.execute();
            }
        });
        txtMinValue.addValueChangeHandler(ch -> {

            if (cmdChangeGaugeConfig != null) {
                cmdChangeGaugeConfig.execute();
            }
        });

        dwMaxTypeList.addValueChangeHandler(ch -> {

            if (cmdChangeGaugeConfig != null) {
                cmdChangeGaugeConfig.execute();
            }
        });
        dwMaxColumnList.addValueChangeHandler(ch -> {

            if (cmdChangeGaugeConfig != null) {
                cmdChangeGaugeConfig.execute();
            }
        });
        txtMaxValue.addValueChangeHandler(ch -> {

            if (cmdChangeGaugeConfig != null) {
                cmdChangeGaugeConfig.execute();
            }
        });
    }

    private void loadConfig() {

        if (chartConfigItem != null && chartConfigItem.getGaugeConfig() != null) {
            GaugeChartConfig gaugeChartConfig = chartConfigItem.getGaugeConfig();

            if (gaugeChartConfig.getGaugeMinColumn() != null) {
                dwMinTypeList.setSelected(new SelectItem(FROM_SYSTEM));
                dwMinColumnList.setSelectedByCode(gaugeChartConfig.getGaugeMinColumn().getColumn());
            } else {
                dwMinTypeList.setSelected(new SelectItem(ENTER_MANUALLY));
                txtMinValue.setText(gaugeChartConfig.getGaugeMinValue() != null ? gaugeChartConfig.getGaugeMinValue()+"" : "0");
            }
            onChangeMinValueTypes();

            if (gaugeChartConfig.getGaugeMaxValue() != null) {
                dwMaxTypeList.setSelected(new SelectItem(FROM_SYSTEM));
                txtMaxValue.setText(gaugeChartConfig.getGaugeMaxValue()+"");
            } else {
                dwMaxTypeList.setSelected(new SelectItem(ENTER_MANUALLY));

                if (gaugeChartConfig.getGaugeMaxColumn() != null) {
                    dwMaxColumnList.setSelectedByCode(gaugeChartConfig.getGaugeMaxColumn().getColumn());
                }
            }
            onChangeMaxValueTypes();

            if (gaugeChartConfig.getGaugeSerie() != null) {
                serieField.setSerieConf(gaugeChartConfig.getGaugeSerie());
            }
        } else {
            onChangeMinValueTypes();
            onChangeMaxValueTypes();
        }
    }

    private SerieColumn getSerieColumn(SelectItem selectedColumn) {
        SerieColumn column = new SerieColumn();
        if (selectedColumn != null) {
            column.setColumnTitle(selectedColumn.getName());

            //code is using for SQL query {t.total, t.paidAmount, t.customerName ...}
            column.setColumn(selectedColumn.getCode());

            //category is a column type {number, money, time ...}
            column.setColumnType(selectedColumn.getCategory());

            //description is a column format {percent, money, double, time, string ...}
            column.setColumnFormat(selectedColumn.getDescription());
        }

        return column;
    }

    private void initColumnsForMinMax() {
        ArrayList<SelectItem> list = new ArrayList<>();
        for (SelectItem item : columns) {
            String columnType = item.getCategory();

            if ("number".equals(columnType) || "money".equals(columnType) || "time".equals(columnType)) {
                list.add(item);
            }
        }

        if (!list.isEmpty()) {
            columnsForMinMax = list.toArray(new SelectItem[]{});
        } else {
            columnsForMinMax = new SelectItem[0];
        }
    }

    private void onChangeMinValueTypes() {
        minFieldItemContainer.clear();

        if (dwMinTypeList.getSelectedId() != null && dwMinTypeList.getSelectedId().intValue() == FROM_SYSTEM) {
            minFieldItemContainer.add(new InputGroup(dwMinTypeList, dwMinColumnList));
        } else {
            minFieldItemContainer.add(new InputGroup(dwMinTypeList, txtMinValue));
        }
    }

    private void onChangeMaxValueTypes() {
        maxFieldItemContainer.clear();

        if (dwMaxTypeList.getSelectedId() != null && dwMaxTypeList.getSelectedId().intValue() == ENTER_MANUALLY) {
            maxFieldItemContainer.add(new InputGroup(dwMaxTypeList, txtMaxValue));
        } else {
            maxFieldItemContainer.add(new InputGroup(dwMaxTypeList, dwMaxColumnList));
        }
    }

    private static final int FROM_SYSTEM = 1;

    private static final int ENTER_MANUALLY = 2;
    private SelectItem[] getTypes() {
        SelectItem[] items = new SelectItem[2];

        items[0] = new SelectItem(FROM_SYSTEM, wfmStrings.fromSystem());
        items[1] = new SelectItem(ENTER_MANUALLY, wfmStrings.enterManually());

        return items;
    }

    public void setCmdChangeGaugeConfig(Command cmdChangeGaugeConfig) {
        serieField.setChangeSerieCommand(cmdChangeGaugeConfig);
        this.cmdChangeGaugeConfig = cmdChangeGaugeConfig;
    }

    interface GaugeChartOptUiBinder extends UiBinder<Widget, GaugeChartOpt> {

    }
}
