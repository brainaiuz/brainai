package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.SerieAggrTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieColumn;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieConfItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;

public class ChartSerie extends Composite {
    interface ChartSerie2UiBinder extends UiBinder<Widget, ChartSerie> {
    }

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    private static final ChartSerie2UiBinder ourUiBinder = GWT.create(ChartSerie2UiBinder.class);

    @UiField(provided = true)
    FormGroup serieField;
    @UiField(provided = true)
    FormGroup serieNameField;
    @UiField
    Div serieContainer;
    @UiField
    Div serieNameContainer;

    private WfmDropdown serieColumnList;
    private DataListBox aggrFuncList;
    private Command changeSerieCommand;

    private TextBox txtSerieName;
    private KpiCheckBox isUnique;
    private MaterialLink btnAddSerie;
    private MaterialLink btnRemoveSerie;
    private final Command removeSerieCommand;
    private final Command addMoreSerieCommand;
    private final SelectItem[] columns;
    private Div colorDiv;
    private SerieConfItem serieConf; //selected column
    private AdvancedSerieColorWidget advancedSerieColorWidget;

    public ChartSerie(SelectItem[] columns, Command changeSerieCommand, Command removeSerieCommand, Command addMoreSerieCommand) {
        this(columns, null, changeSerieCommand, removeSerieCommand, addMoreSerieCommand);
    }

    public ChartSerie(SelectItem[] columns, Command changeSerieCommand, Command removeSerieCommand, Command addMoreSerieCommand, AdvancedSerieColorWidget advancedSerieColorWidget) {
        this.columns = columns;
        this.serieConf = null;
        this.changeSerieCommand = changeSerieCommand;
        this.removeSerieCommand = removeSerieCommand;
        this.addMoreSerieCommand = addMoreSerieCommand;
        this.advancedSerieColorWidget = advancedSerieColorWidget;

        initialize();
    }

    public ChartSerie(SelectItem[] columns, SerieConfItem serieConf, Command changeSerieCommand, Command removeSerieCommand, Command addMoreSerieCommand) {

        this.columns = columns;
        this.serieConf = serieConf;
        this.changeSerieCommand = changeSerieCommand;
        this.removeSerieCommand = removeSerieCommand;
        this.addMoreSerieCommand = addMoreSerieCommand;

        initialize();
    }

    public void setAdvancedSerieColorWidget(AdvancedSerieColorWidget advancedSerieColorWidget) {
        this.advancedSerieColorWidget = advancedSerieColorWidget;
//        if (ChartTypeEnum.GAUGE_CHART.equals(advancedSerieColorWidget.getActiveChartType())) {
//            colorDiv.setVisible(false);
//        }
    }

    public void disebledGradient(Boolean b) {
        if (advancedSerieColorWidget != null) {
            advancedSerieColorWidget.disebleGradientWidget(b);
        }
    }

    public SerieConfItem getSerieConf() {
        serieConf = new SerieConfItem();
        serieConf.setSerieName(txtSerieName.getText());
        serieConf.setUnique(isUnique.getValue());
        if (advancedSerieColorWidget != null) {
            serieConf.setColorList(advancedSerieColorWidget.getData(this));
        }

        if (serieColumnList.getSelectedItem() != null) {
            SelectItem selectedColumn = serieColumnList.getSelectedItem();

            SerieColumn column = new SerieColumn();
            column.setColumnTitle(selectedColumn.getName());

            //code is using for SQL query {t.total, t.paidAmount, t.customerName ...}
            column.setColumn(selectedColumn.getCode());

            //category is a column type {number, money, time ...}
            column.setColumnType(selectedColumn.getCategory());

            //description is a column format {percent, money, double, time, string ...}
            column.setColumnFormat(selectedColumn.getDescription());

            //serie name isn't set to config
            if (Utils.isNullOrEmpty(serieConf.getSerieName())) {
                serieConf.setSerieName(selectedColumn.getName());
            }
            serieConf.setSerieColumn(column);
        }

        if (aggrFuncList.getSelectedId() != null) {
            serieConf.setAggrType(SerieAggrTypeEnum.getById(aggrFuncList.getSelectedId()));
        }

        if (serieConf.getSerieColumn() != null && serieConf.getAggrType() != null) {
            return serieConf;
        }

        return null;
    }

    public boolean isGradient() {
        return advancedSerieColorWidget != null && advancedSerieColorWidget.isGradient();
    }

    public void setSerieConf(SerieConfItem confItem) {
        this.serieConf = confItem;
        initData();
    }

    private void initialize() {

        //column list for serie
        serieColumnList = new WfmDropdown();
        serieColumnList.setWidth(Constants.NORMAL_WIDTH);
        serieColumnList.addItems("Serie", columns);
        serieColumnList.addValueChangeHandler(e -> {
            aggrFuncList.clear();
            txtSerieName.setText(null);
            isUnique.setValue(false);
//            colorDiv.setEnabled(true);

            if (serieColumnList.getSelectedId() != null) {
                String columnType = serieColumnList.getSelectedItem().getCategory();
                aggrFuncList.setItems(getAggrFList(columnType));

                if ("number".equals(columnType) || "money".equals(columnType) || "time".equals(columnType)) {
                    aggrFuncList.setSelected(SerieAggrTypeEnum.SUM.getId());
                } else {
                    aggrFuncList.setSelected(SerieAggrTypeEnum.COUNT.getId());
                }

                if (serieColumnList.getSelectedIndex() >= 0) {
                    txtSerieName.setText(serieColumnList.getValues().get(serieColumnList.getSelectedIndex()).getName());
                }

                if (serieConf != null && serieConf.getAggrType() != null) {
                    aggrFuncList.setSelected(serieConf.getAggrType().getId());
                }
            }

            if (changeSerieCommand != null) {
                changeSerieCommand.execute();
            }
        });

        //aggregation function list
        aggrFuncList = new DataListBox();
        aggrFuncList.setWithoutNullLabel(true);
        aggrFuncList.setWidth(Constants.SHORT_WIDTH);
        aggrFuncList.addValueChangeHandler(e -> {

            if (changeSerieCommand != null) {
                changeSerieCommand.execute();
            }
        });

        //serie name box
        txtSerieName = new TextBox();
        isUnique = new KpiCheckBox();
        isUnique.setTitle("Group duplicates");
        isUnique.addValueChangeHandler(e -> {

            if (changeSerieCommand != null) {
                changeSerieCommand.execute();
            }
        });
        //add serie widget
        btnAddSerie = new MaterialLink();
        Icon addIcon = new Icon();
        addIcon.setClass("ficon--plus");
        btnAddSerie.add(addIcon);
        btnAddSerie.setTooltip(reportingStrings.addMoreSeries());
        //btnAddSerie.setVisible(false); //this option handled by ReportingChart class
        btnAddSerie.addClickHandler(ch -> {

            if (addMoreSerieCommand != null) {
                addMoreSerieCommand.execute();
            }
        });

        //delete serie widget from DOM
        btnRemoveSerie = new MaterialLink();
        Icon rmIcon = new Icon();
        rmIcon.setClass("ficon--minus");
        btnRemoveSerie.add(rmIcon);
        btnRemoveSerie.setTooltip(reportingStrings.removeSerie());
        //btnRemoveSerie.setVisible(false); //this option handled by ReportingChart class
        btnRemoveSerie.addClickHandler(ch -> {
            ChartSerie.this.removeFromParent();

            if (removeSerieCommand != null) {
                removeSerieCommand.execute();
                advancedSerieColorWidget.removeSerieColor(this);
            }
        });

        Div btnsGroup = new Div("btns-group");
        btnsGroup.add(btnAddSerie);
        btnsGroup.add(btnRemoveSerie);

        colorDiv = new Div();
        colorDiv.setEnabled(false);
        colorDiv.add(getSeriesColorWidget());
        serieField = new FormGroup(wfmStrings.serie(), new InputGroup(serieColumnList, aggrFuncList, colorDiv));
        serieNameField = new FormGroup(reportingStrings.serieName(), new AdvancedInputGroup(new AdvancedInputGroup(txtSerieName, isUnique), btnsGroup));

        initWidget(ourUiBinder.createAndBindUi(this));

        initData();
    }

    private void initData() {

        if (serieConf != null) {
            int selectedIndex = 0;
            int i = 0;
            for (SelectItem column : serieColumnList.getValues()) {
                if (serieConf.getSerieColumn() != null &&
                        serieConf.getSerieColumn().getColumn() != null &&
                        serieConf.getSerieColumn().getColumn().equals(column.getCode())) {
                    selectedIndex = i;
                    break;
                }
                i++;
            }
            serieColumnList.setSelectedIndex(selectedIndex);

            aggrFuncList.clear();
            aggrFuncList.setItems(getAggrFList(serieConf.getSerieColumn().getColumnType()));
            aggrFuncList.setSelected(serieConf.getAggrType().getId());

            txtSerieName.setText(serieConf.getSerieName());
            isUnique.setValue(serieConf.getUnique());
            if (advancedSerieColorWidget != null && getAreaCharts(advancedSerieColorWidget.getActiveChartType())) {
                advancedSerieColorWidget.setData(this, serieConf);
            }
        }
    }


    private SelectItem[] getAggrFList(String columnType) {
        ArrayList<SelectItem> items = new ArrayList<>();

        if ("number".equals(columnType) || "money".equals(columnType) || "time".equals(columnType)) {
            for (SerieAggrTypeEnum aggrType : SerieAggrTypeEnum.values()) {
                items.add(new SelectItem(aggrType.getId(), aggrType.getTitle()));
            }
        } else {
            items.add(new SelectItem(SerieAggrTypeEnum.COUNT.getId(), SerieAggrTypeEnum.COUNT.getTitle()));
        }

        return items.toArray(new SelectItem[]{});
    }

    public void addStyleNameToSerieColumnList(String style) {
        serieColumnList.addStyleName(style);
    }

    public void addStyleNameToAggrFuncList(String style) {
        aggrFuncList.addStyleNameToInput(style);
    }

    public void addStyleNameToTxtSerieName(String style) {
        txtSerieName.addStyleName(style);
    }

    public void removeStyleNameFromSerieColumnList(String style) {
        serieColumnList.removeStyleName(style);
    }

    public void removeStyleNameFromAggrFuncList(String style) {
        aggrFuncList.removeStyleNameFromInput(style);
    }

    public void removeStyleNameFromTxtSerieName(String style) {
        txtSerieName.removeStyleName(style);
    }

    public void hideSerieName() {
        txtSerieName.setEnabled(true);
        isUnique.setEnabled(true);
        btnAddSerie.setEnabled(false);
        btnRemoveSerie.setEnabled(false);
    }

    public void setRomovable(boolean removable) {
        btnRemoveSerie.setVisible(removable);
    }

    public void canAddMoreSerie(boolean canAdd) {
        btnAddSerie.setVisible(canAdd);
    }

    public Div getSerieContainer() {
        return serieContainer;
    }

    private WfmButton2 getSeriesColorWidget() {
        WfmButton2 colorSettingButton = new WfmButton2("<i class='icon-colors'></i>", " ");
        colorSettingButton.addStyleName("btn--icon");
        colorSettingButton.setTooltip(reportingStrings.conditionalFormating());
        colorSettingButton.addClickHandler(event -> {
            if (advancedSerieColorWidget != null) {
                advancedSerieColorWidget.addChartSerie(this);
            }
        });
        return colorSettingButton;
    }

    private boolean getAreaCharts(ChartTypeEnum chartType) {
        return ChartTypeEnum.PIE_CHART.equals(chartType)
                || ChartTypeEnum.DONUT_CHART.equals(chartType)
                || ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART.equals(chartType)
                || ChartTypeEnum.FUNNEL_CHART.equals(chartType);
    }

    public void setEnabledAdvancedCollerWidget(boolean enabled) {
        if (enabled) {
            colorDiv.setDisplay(Display.INLINE_BLOCK);
        } else {
            colorDiv.setDisplay(Display.NONE);
        }
    }

    public void setChangeSerieCommand(Command changeSerieCommand) {
        this.changeSerieCommand = changeSerieCommand;
    }
}
