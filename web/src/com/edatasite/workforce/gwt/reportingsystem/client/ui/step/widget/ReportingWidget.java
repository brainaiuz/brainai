package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.SerieAggrTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetFilterItem;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetItem;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieColumn;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieConfItem;
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
import com.edatasite.workforce.gwt.core.client.ui.LocalizationCFModal;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColorWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DateRangeType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.AdvancedFilterRow;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.AdvancedSerieColorWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.UnorderedList;
import gwt.material.design.jquery.client.api.JQuery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportingWidget extends AsyncWidget {
    public static final String NEGATIVE = "NEGATIVE";
    public static final String POSITIVE = "POSITIVE";

    interface ReportingWidgetUiBinder extends UiBinder<Widget, ReportingWidget> {
    }

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();


    private static final ReportingWidgetUiBinder ourUiBinder = GWT.create(ReportingWidgetUiBinder.class);

    @UiField
    UnorderedList widgetTypeList;
    @UiField
    Div kpiWidgetContainer;
    @UiField
    Div mainWidgetConfigRow;
    @UiField
    Div negativeAndPosiveTypeDiv;

    @UiField(provided = true)
    FormGroup kpiWidgetTitleField;
    private final LinkedList<SelectItem> dateTypeColumns = new LinkedList<>();
    private final LinkedList<SelectItem> otherTypeColumns = new LinkedList<>();
    @UiField(provided = true)
    FormGroup groupingField;
    @UiField(provided = true)
    FormGroup widgetSortingField;
    @UiField(provided = true)
    FormGroup kpiWidgetMetricField;
    @UiField(provided = true)
    FormGroup negativeAndPosiveType;
    @UiField(provided = true)
    FormGroup widgetTitleColorField;
    @UiField(provided = true)
    FormGroup increaseColor;
    @UiField(provided = true)
    FormGroup differentForm;
    @UiField(provided = true)
    FormGroup comparisionTitle;
    @UiField(provided = true)
    FormGroup pageSizeField;

    @UiField(provided = true)
    FormGroup widgetModulesField;

    @UiField
    Div firstRowContainer;
    @UiField(provided = true)
    FormGroup filterContainer;
    @UiField(provided = true)
    FormGroup advancedFilter;

    @UiField
    Div seconRowContainer;
    @UiField(provided = true)
    FormGroup filterContainerComparision;
    @UiField(provided = true)
    FormGroup advancedFilterTwo;


    /**
     * If chart type contains {Pie, Donut, Funnel} then this field works as a axis categories,
     * otherwise this is a X axis
     */
    private WfmDropdown xAxisListForwidget;


    private ReportingStepControlView view;

    private SelectItem[] columns;
    @UiField(provided = true)
    FormGroup kpiWidgetScaleField;
    @UiField(provided = true)
    FormGroup kpiWidgetSuffixField;

    private KpiWidgetItem kpiWidgetItem;

    private ChartTypeEnum widgetType;


    private HashMap<ChartTypeEnum, ListItem> mapCTWidgets;


    private TextBox kpiWidgetTitle;
    private TextBox kpiWidgetScale;

    private TextBox kpiWidgetSuffix;

    private DataListBox sortByListForWidget;
    private DataListBox sortTypeListForWidget;
    private DataListBox dateSortPeriodTypeForWidget;
    private WfmDropdown metricColumnList;
    private DataListBox metricAggrFuncList;
    private DataListBox negativeAndPositiveList;
    private TextBox txtSerieName;
    private TextBox differentLabel;
    private KpiCheckBox checkDifferent;
    private SerieConfItem metricSerieConf;
    private KpiCheckBox isUnique;
    private KpiCheckBox withAnotherItems;
    private DataListBox colorTypeList;
    private TextBox comparisionText;
    private DataListBox pageSizeType;
    private DataListBox pageSizeList;
    private TextBox customPageSize;
    private Div colorLink;
    private ColorWidget kpiWidgetTitleColor;
    private Command cmdChangeTitle;
    LocalizationCFModal localizationCFModal;
    private CustomFormLocalization localizationCF;
    private CustomFormLocalization suffixLocalizationCF;
    private CustomFormLocalization comparisonLocalizationCF;
    private CustomFormLocalization differenceLocalizationCF;
    private AdvancedSerieColorWidget advancedSerieColorWidget;
    private ReportRpc reportRpc1;
    private KpiWidgetItem kpiWidgetItem1;
    private KpiWidgetAdvancedFilter kpiWidgetAdvancedFilter1;


    private ArrayList<ModuleEnum> widgetSharedModules;

    private static final String MODULE_SWITCH_ON = "btn--toggle-on";
    private static final String MODULE_SWITCH_OFF = "btn--toggle-off";

    ReportingWidget() {
        super(null);
    }

    public void setView(ReportingStepControlView view) {
        this.view = view;
    }

    public ReportRpc getReport(ReportRpc report) {
        if (ChartTypeEnum.NONE.equals(widgetType)) {
            report.setKpiWidgetItem(null);
        }
        kpiWidgetItem = report.getKpiWidgetItem() != null ? report.getKpiWidgetItem() : new KpiWidgetItem();
        kpiWidgetItem.setKpiWidgetTitle(kpiWidgetTitle.getText());
        kpiWidgetItem.setKpiWidgetScale(kpiWidgetScale.getText() != null && !kpiWidgetScale.getText().isEmpty() ? kpiWidgetScale.getText() : "2");
        kpiWidgetItem.setKpiWidgetSuffix(kpiWidgetSuffix.getText() != null && !kpiWidgetSuffix.getText().isEmpty() ? kpiWidgetSuffix.getText() : "");

        if (xAxisListForwidget.getSelectedIndex() >= 0) {
            dateSortPeriodTypeForWidget.setEnabled("date".equals(xAxisListForwidget.getValues().get(xAxisListForwidget.getSelectedIndex()).getCategory()));
        }
        if (dateSortPeriodTypeForWidget.getSelectedItem() != null) {
            kpiWidgetItem.setDateSortPeriodType(dateSortPeriodTypeForWidget.getSelectedItem().getDescription());
        }
        if (xAxisListForwidget.getSelectedId() != null) {
            SerieColumn groupingColumn = new SerieColumn();
            groupingColumn.setColumnTitle(xAxisListForwidget.getSelectedItem().getName());
            groupingColumn.setColumn(xAxisListForwidget.getSelectedItem().getCode());
            groupingColumn.setColumnType(xAxisListForwidget.getSelectedItem().getCategory());
            groupingColumn.setColumnFormat(xAxisListForwidget.getSelectedItem().getDescription());
            kpiWidgetItem.setGroupingColumn(groupingColumn);
        }

        kpiWidgetItem.setSortBy(sortByListForWidget.getSelectedItem().getDescription());
        kpiWidgetItem.setSortType(sortTypeListForWidget.getSelectedItem().getDescription());
        kpiWidgetItem.setPageSizeType(pageSizeType.getSelectedId());
        kpiWidgetItem.setPageSize(pageSizeList.getSelectedId());
        if (customPageSize.getText() != null && !customPageSize.getText().isEmpty()) {
            kpiWidgetItem.setCustomPageSize(Integer.valueOf(customPageSize.getText()));
        }
        kpiWidgetItem.setOtherItems(withAnotherItems != null && withAnotherItems.getValue());
        kpiWidgetItem.setKpiWidgetMetric(getSerieConf());
        kpiWidgetItem.setIncreaseColor(colorTypeList.getSelectedItem().getDescription());
        kpiWidgetItem.setComparisionText(comparisionText.getText());
        kpiWidgetItem.setModules(widgetSharedModules);
        kpiWidgetItem.setType(widgetType);
        kpiWidgetItem.setLocalization(localizationCF);
        kpiWidgetItem.setSuffixLocalization(suffixLocalizationCF);
        kpiWidgetItem.setDifferenceLocalization(differenceLocalizationCF);
        kpiWidgetItem.setComparisonLocalization(comparisonLocalizationCF);
        kpiWidgetItem.setNegAndPosType(negativeAndPositiveList.getSelectedItem() != null ? negativeAndPositiveList.getSelectedItem().getDescription() : null);
        kpiWidgetItem.setDifferentTitle(differentLabel.getText());
        kpiWidgetItem.setShowDifferent(checkDifferent.getValue());

        report.setKpiWidgetItem(kpiWidgetItem);

        return report;
    }

    private SerieConfItem getSerieConf() {
        metricSerieConf = new SerieConfItem();
        metricSerieConf.setUnique(isUnique.getValue());
        if (metricColumnList.getSelectedItem() != null) {
            SelectItem selectedColumn = metricColumnList.getSelectedItem();

            SerieColumn column = new SerieColumn();
            column.setColumnTitle(selectedColumn.getName());

            column.setColumn(selectedColumn.getCode());

            column.setColumnType(selectedColumn.getCategory());

            column.setColumnFormat(selectedColumn.getDescription());

            if (Utils.isNullOrEmpty(metricSerieConf.getSerieName())) {
                metricSerieConf.setSerieName(selectedColumn.getName());
            }
            metricSerieConf.setSerieColumn(column);
        }

        if (metricAggrFuncList.getSelectedId() != null) {
            metricSerieConf.setAggrType(SerieAggrTypeEnum.getById(metricAggrFuncList.getSelectedId()));
        }
        metricSerieConf.setSerieName(txtSerieName.getText());

        metricSerieConf.setColorList(advancedSerieColorWidget.getData());
        if (metricSerieConf.getSerieColumn() != null && metricSerieConf.getAggrType() != null) {
            return metricSerieConf;
        }

        return null;
    }

    @Override
    protected Widget onInitialize() {

        kpiWidgetItem = view.getReport().getKpiWidgetItem();
        widgetSharedModules = new ArrayList<>();


        configureColumns();

        initSplitColumns();

        //title
        kpiWidgetTitle = new TextBox();
        kpiWidgetTitle.setMaxLength(66);
        kpiWidgetTitle.addKeyUpHandler(handler -> getReport(view.getReport()));
        kpiWidgetTitleField = new FormGroup(kpiWidgetTitle);
        kpiWidgetTitle.addKeyUpHandler(ch -> {
            if (cmdChangeTitle != null) {
                cmdChangeTitle.execute();
            }
        });

        kpiWidgetScale = new TextBox();
        kpiWidgetScale.setMaxLength(66);
        kpiWidgetScale.addKeyUpHandler(handler -> getReport(view.getReport()));
        kpiWidgetScaleField = new FormGroup(wfmStrings.decimalPlaces(), kpiWidgetScale);
        Validation.addNumericKeyboardListener(kpiWidgetScale);

        negativeAndPositiveList = createTypeWidget();
        negativeAndPosiveType = new FormGroup(wfmStrings.type(), negativeAndPositiveList);
        kpiWidgetSuffix = new TextBox();
        kpiWidgetSuffix.setMaxLength(66);
        kpiWidgetSuffix.addKeyUpHandler(handler -> getReport(view.getReport()));
        kpiWidgetSuffixField = new FormGroup(kpiWidgetSuffix);

//        KpiModal colorBox = new KpiModal();
//        colorBox.setCloseButton(true);
//        colorBox.setWidth(340);
//        colorBox.setTitle(wfmStrings.widgetTitleColor());
//        kpiWidgetTitleColor = new ColorWidget();
//        colorBox.add(kpiWidgetTitleColor);
//        kpiWidgetTitleColor.setChangeHandler(() -> {
//            colorBox.close();
//            colorLink.getElement().getStyle().setBackgroundColor(kpiWidgetTitleColor.getColor());
//            getReport(view.getReport());
//        });

        colorLink = new Div();
//        colorLink.setMarginTop(20);
        advancedSerieColorWidget = new AdvancedSerieColorWidget(() -> {
            getReport(view.getReport());
        }, true);
        colorLink.add(this::getSeriesColorWidget);
//        colorLink.addClickHandler(event -> colorBox.open());

        widgetTitleColorField = new FormGroup(colorLink);


        xAxisListForwidget = initGroupingWidget();
        dateSortPeriodTypeForWidget = initDateRangeWidget();
        groupingField = new FormGroup(reportingStrings.grouping(), new InputGroup(xAxisListForwidget, dateSortPeriodTypeForWidget));

        //sorting
        sortByListForWidget = initSortByWidget();
        sortTypeListForWidget = initSortTypeWidget();
        widgetSortingField = new FormGroup(wfmStrings.sort(), new InputGroup(sortByListForWidget, sortTypeListForWidget));

        metricColumnList = new WfmDropdown();
        metricColumnList.setWidth(Constants.NORMAL_WIDTH);
        metricColumnList.addItems("Serie", columns);
        metricColumnList.addValueChangeHandler(e -> {
            metricAggrFuncList.clear();
            txtSerieName.setText(null);
            isUnique.setValue(false);

            if (metricColumnList.getSelectedId() != null) {
                String columnType = metricColumnList.getSelectedItem().getCategory();
                metricAggrFuncList.setItems(getAggrFList(columnType));

                if ("number".equals(columnType) || "money".equals(columnType) || "time".equals(columnType)) {
                    metricAggrFuncList.setSelected(SerieAggrTypeEnum.SUM.getId());
                } else {
                    metricAggrFuncList.setSelected(SerieAggrTypeEnum.COUNT.getId());
                }

                if (metricColumnList.getSelectedIndex() >= 0) {
                    txtSerieName.setText(metricColumnList.getValues().get(metricColumnList.getSelectedIndex()).getName());
                }
            }

            getReport(view.getReport());
        });

        metricAggrFuncList = new DataListBox();
        metricAggrFuncList.setWithoutNullLabel(true);
        metricAggrFuncList.setWidth(Constants.SHORT_WIDTH);
        metricAggrFuncList.addValueChangeHandler(e -> getReport(view.getReport()));

        txtSerieName = new TextBox();
        txtSerieName.setWidth(Constants.NORMAL_WIDTH);
        txtSerieName.addKeyUpHandler(handler -> getReport(view.getReport()));
        isUnique = new KpiCheckBox();
        isUnique.setTitle("Group duplicates");
        isUnique.addValueChangeHandler(e -> getReport(view.getReport()));


        kpiWidgetMetricField = new FormGroup(reportingStrings.widgetMetric(), new InputGroup(metricColumnList, new AdvancedInputGroup(new InputGroup(metricAggrFuncList, txtSerieName), isUnique)));
        differentLabel = new TextBox();
        differentLabel.addValueChangeHandler(event -> getReport(view.getReport()));
        differentLabel.setPlaceHolder(wfmStrings.difference());
        checkDifferent = new KpiCheckBox();
        checkDifferent.addValueChangeHandler(event -> getReport(view.getReport()));
        differentForm = new FormGroup(new InputGroup(checkDifferent, differentLabel));

        widgetModulesField = initSharingModules();


        colorTypeList = new DataListBox();
        colorTypeList.setWithoutNullLabel(true);
//        colorTypeList.setWidth(Constants.SHORT_WIDTH);
        colorTypeList.setItems(getColorItems());
        colorTypeList.setSelected(0);
        colorTypeList.addValueChangeHandler(vch -> getReport(view.getReport()));
        increaseColor = new FormGroup(colorTypeList);

        comparisionText = new TextBox();
        comparisionText.setMaxLength(25);
        comparisionText.addKeyUpHandler(handler -> getReport(view.getReport()));
        comparisionTitle = new FormGroup(comparisionText);

        //page size field config
        pageSizeType = new DataListBox();
        pageSizeType.setWidth(Constants.SHORT_WIDTH);
        pageSizeType.setWithoutNullLabel(true);
        pageSizeType.setItems(getPagesizeType());
        pageSizeType.setSelected(0); //default value of the page size
        pageSizeType.addValueChangeHandler(event -> getReport(view.getReport()));
        withAnotherItems = new KpiCheckBox();
        withAnotherItems.setTitle("Another items");
        withAnotherItems.addValueChangeHandler(e -> getReport(view.getReport()));

        //page size field config
        pageSizeList = new DataListBox();
        pageSizeList.setWithoutNullLabel(true);
        pageSizeList.setItems(getPagesizeList());
        pageSizeList.setSelected(5); //default value of the page size
        pageSizeList.addValueChangeHandler(event -> {
            getReport(view.getReport());
            customPageSize.setEnabled(pageSizeList.getSelectedId() == -1);
            if (pageSizeList.getSelectedId() == 0 && withAnotherItems != null) {
                withAnotherItems.setValue(false);
                withAnotherItems.setEnabled(false);
            } else if (withAnotherItems != null) {
                withAnotherItems.setEnabled(true);
            }
        });

        customPageSize = new TextBox();
        customPageSize.setWidth("50px");
        customPageSize.setEnabled(false);
        customPageSize.addKeyUpHandler(handler -> getReport(view.getReport()));
        Validation.addPhoneNumberKeyboardListener(customPageSize);

        pageSizeField = new FormGroup(reportingStrings.showItems(), new InputGroup(pageSizeType, new AdvancedInputGroup(new InputGroup(pageSizeList, customPageSize), withAnotherItems)));


        for (int i = 0; i < view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo().getFieldd().size(); i++) {
            if (view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo().getOperators().get(i).equals(DurationType.SamePeriodLastYear.toString())) {
                kpiWidgetItem1 = new KpiWidgetItem();
                KpiWidgetFilterItem kpiWidgetFilterItem = new KpiWidgetFilterItem();
                LinkedList<ColumnRpc> linkedList = new LinkedList<>();
                ArrayList<String> arrayList = new ArrayList<>();
                ArrayList<String> values = new ArrayList<>();
                ArrayList<Integer> setts = new ArrayList<>();
                ColumnRpc fieldd = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne().getFieldd().get(i);
                String operator = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne().getOperators().get(i);
                String value = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne().getValues().get(i);
                Integer sett = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne().getSett().get(i);
                linkedList.add(fieldd);
                arrayList.add(operator);
                values.add(value);
                setts.add(sett);
                kpiWidgetFilterItem.setFieldd(linkedList);
                kpiWidgetFilterItem.setOperators(arrayList);
                kpiWidgetFilterItem.setValues(values);
                kpiWidgetFilterItem.setSett(setts);
                kpiWidgetItem1.setKpiWidgetFilterItemTwo(kpiWidgetFilterItem);
                break;
            }
        }
        ///First Additional column to Compare
        kpiWidgetAdvancedFilter1 = new KpiWidgetAdvancedFilter(true);
        ReportingStepControlView reportingStepControlView = new ReportingStepControlView("", null, "", "");
        ReportRpc reportRpc = new ReportRpc();
        Map<String, ColumnRpc> dateColumns = view.getReport().getColumnMap().entrySet().stream()
                .filter(entry -> "date".equals(entry.getValue().getType()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        reportRpc.setColumnMap((HashMap<String, ColumnRpc>) dateColumns);
        reportRpc.setKpiWidgetItem(kpiWidgetItem1);
        reportingStepControlView.setReport(reportRpc);
        kpiWidgetAdvancedFilter1.setView(reportingStepControlView, false);
        kpiWidgetAdvancedFilter1.init();


        KpiWidgetAdvancedFilter thisAdvancedFilter = new KpiWidgetAdvancedFilter();
        view.getReport().setKpiWidgetItem(view.getReport().getKpiWidgetItem() != null ? view.getReport().getKpiWidgetItem() : new KpiWidgetItem());
        thisAdvancedFilter.setView(view, true);
        thisAdvancedFilter.init();
        thisAdvancedFilter.setElementVisible(false);


        filterContainer = new FormGroup(wfmStrings.current().toUpperCase(), kpiWidgetAdvancedFilter1, thisAdvancedFilter);

        KpiCheckBox advanced = getKpiCheckBoxPanel(thisAdvancedFilter);
        advancedFilter = new FormGroup(advanced);

        KpiWidgetAdvancedFilter previouseAdvancedFilter = new KpiWidgetAdvancedFilter();
        previouseAdvancedFilter.setView(view, false);
        previouseAdvancedFilter.init();
        previouseAdvancedFilter.setElementVisible(false);
        filterContainerComparision = new FormGroup(reportingStrings.comparisonFilter().toUpperCase(), previouseAdvancedFilter);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REPORT_FILTER_ADD, ReportingWidget.this, ((sender, args) -> {
            if (sender instanceof AdvancedFilterRow) {
                ReportingStepControlView args1 = (ReportingStepControlView) args;
                Integer samePeriodIndex = getSamePeriodIndex(view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo());
                KpiWidgetFilterItem kpiWidgetFilterItemOne = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne();
                KpiWidgetFilterItem kpiWidgetFilterItemTwo = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo();
                ColumnRpc fieldd = args1.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo().getFieldd().get(0);
                fieldd.setSameperiodlastyear(true);
                String operator = args1.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo().getOperators().get(0);
                String value = args1.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo().getValues().get(0);
                Integer sett = args1.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo().getSett().get(0);
                kpiWidgetFilterItemTwo.addFilter(samePeriodIndex, fieldd, DurationType.SamePeriodLastYear.toString(), null, sett);
                kpiWidgetFilterItemOne.addFilter(samePeriodIndex, fieldd, operator, value, sett);
                kpiWidgetFilterItemTwo.setFilterPattern(calculatePattern(kpiWidgetFilterItemTwo.getOperators()));
                kpiWidgetFilterItemOne.setFilterPattern(calculatePattern(kpiWidgetFilterItemOne.getOperators()));
                previouseAdvancedFilter.setView(view, false);
                thisAdvancedFilter.setView(view, true);
                previouseAdvancedFilter.init();
                thisAdvancedFilter.init();
            }

        }));


        KpiCheckBox advanced2 = getKpiCheckBoxPanel(previouseAdvancedFilter);
        advancedFilterTwo = new FormGroup(advanced2);

        add(ourUiBinder.createAndBindUi(this));


        initChartTypeList();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCALIZATION_ADD, ReportingWidget.this, (sender, args) -> {
            if (sender instanceof LocalizationCFModal && args != null && view.getReport().getKpiWidgetItem() != null) {
                LocalizationCFModal cfModal = (LocalizationCFModal) sender;
                switch (cfModal.getEnumType()) {
                    case DASHBOARD_COMPONENT:
                        localizationCF = (CustomFormLocalization) args;
                        view.getReport().getKpiWidgetItem().setLocalization((CustomFormLocalization) args);
                        break;
                    case DASHBOARD_SUFFIX_COMPONENT:
                        suffixLocalizationCF = (CustomFormLocalization) args;
                        view.getReport().getKpiWidgetItem().setSuffixLocalization((CustomFormLocalization) args);
                        break;
                    case DASHBOARD_DIFFERENCE_COMPONENT:
                        differenceLocalizationCF = (CustomFormLocalization) args;
                        view.getReport().getKpiWidgetItem().setDifferenceLocalization((CustomFormLocalization) args);
                        break;
                    case DASHBOARD_COMPARISON_COMPONENT:
                        comparisonLocalizationCF = (CustomFormLocalization) args;
                        view.getReport().getKpiWidgetItem().setComparisonLocalization((CustomFormLocalization) args);
                        break;
                }
            }
        });

        loadConfig();
        initWidgetTitleToolTip(kpiWidgetTitleField, reportingStrings.widgetTitle(), localizationCF, LocalizationTypeEnum.DASHBOARD_COMPONENT, true);
        initWidgetTitleToolTip(kpiWidgetSuffixField, wfmStrings.suffix(), suffixLocalizationCF, LocalizationTypeEnum.DASHBOARD_SUFFIX_COMPONENT, false);
        initWidgetTitleToolTip(differentForm, wfmStrings.difference(), differenceLocalizationCF, LocalizationTypeEnum.DASHBOARD_DIFFERENCE_COMPONENT, false);
        initWidgetTitleToolTip(comparisionTitle, reportingStrings.comparisonText(), comparisonLocalizationCF, LocalizationTypeEnum.DASHBOARD_COMPARISON_COMPONENT, false);
        return null;
    }

    private DataListBox createTypeWidget() {
        DataListBox listBox = new DataListBox();
        listBox.setWithoutNullLabel(false);
        listBox.setItems(getNegAndPosItems());
        listBox.addValueChangeHandler(handler -> {
            getReport(view.getReport());
        });
        return listBox;
    }

    private SelectItem[] getNegAndPosItems() {
        SelectItem[] selectItem = new SelectItem[]{
                new SelectItem(1, wfmStrings.negative(), NEGATIVE),
                new SelectItem(2, wfmStrings.positive(), POSITIVE)
        };
        return selectItem;
    }

    private Span tooltipWrapper;

    private WfmButton2 getSeriesColorWidget() {
        WfmButton2 colorSettingButton = new WfmButton2("<i class='icon-colors'></i>", " ");
        colorSettingButton.addStyleName("btn--icon");
        colorSettingButton.setTooltip(reportingStrings.conditionalFormating());
        colorSettingButton.addClickHandler(event -> {
            if (advancedSerieColorWidget != null) {
                advancedSerieColorWidget.initReportWidgetData(true);
            }
        });
        return colorSettingButton;
    }

    private void initWidgetTitleToolTip(FormGroup widget, String title, CustomFormLocalization customFormLocalization, LocalizationTypeEnum enumType, boolean isTooltipRequired) {
        Span adAsDashboardTitle = new Span(title);

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

        widget.getGroupLabel().add(adAsDashboardTitle);

        if (isTooltipRequired) {
            tooltipWrapper = new Span();

            setTooltipClass(tooltipWrapper);
            Window.addResizeHandler(e -> {
                setTooltipClass(tooltipWrapper);
            });

            tooltipWrapper.add(iconLink);
            tooltipWrapper.add(dropDown);
            widget.getGroupLabel().add(new Span(" "));
            widget.getGroupLabel().add(tooltipWrapper);
        }

        widget.getGroupLabel().add(new Span(" "));
        widget.getGroupLabel().add(getLocaleLink(customFormLocalization, enumType));
    }

    private SelectItem[] getPagesizeType() {
        SelectItem[] items = new SelectItem[2];

        items[0] = new SelectItem(0, wfmStrings.top(), wfmStrings.top(), true);
        items[1] = new SelectItem(1, reportingStrings.low(), reportingStrings.low());
        return items;
    }

    private SelectItem[] getPagesizeList() {
        SelectItem[] items = new SelectItem[5];

        items[0] = new SelectItem(0, wfmStrings.all(), "");
        items[1] = new SelectItem(5, "5", "5", true);
        items[2] = new SelectItem(10, "10", "10");
        items[3] = new SelectItem(20, "20", "20");
        items[4] = new SelectItem(-1, "Custom", "Custom");
        return items;
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

    private Integer getSamePeriodIndex(KpiWidgetFilterItem filterItem) {
        for (int i = 0; i < filterItem.getOperators().size(); i++) {
            if (filterItem.getOperators().get(i).equals(DurationType.SamePeriodLastYear.toString())) {
                return i;
            }
        }
        return null;
    }

    private WfmDropdown initGroupingWidget() {
        WfmDropdown xAxisList = new WfmDropdown(false, true);
        xAxisList.setWidth(Constants.NORMAL_WIDTH);
        xAxisList.addItems(wfmStrings.date().toUpperCase(), dateTypeColumns.toArray(new SelectItem[0]));
        xAxisList.addItems(reportingStrings.dimensions().toUpperCase(), otherTypeColumns.toArray(new SelectItem[0]));
        xAxisList.addValueChangeHandler(ch -> getReport(view.getReport()));
        return xAxisList;
    }

    private void initSplitColumns() {
        for (SelectItem column : columns) {
            if ("date".equals(column.getCategory())) dateTypeColumns.add(column);
            else otherTypeColumns.add(column);
        }
    }

    private KpiCheckBox getKpiCheckBoxPanel(KpiWidgetAdvancedFilter thisAdvancedFilter) {
        KpiCheckBox advanced = new KpiCheckBox();
        advanced.getElement().getStyle().setVerticalAlign(-25, Style.Unit.PX);
        advanced.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        advanced.addValueChangeHandler((event) -> thisAdvancedFilter.setElementVisible(advanced.getValue()));
        advanced.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NOWRAP);
        advanced.setText(wfmStrings.advanced());
        return advanced;
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

    /**
     * populate config widgets with existing data
     */
    private void loadConfig() {

        widgetType = ChartTypeEnum.NONE;
        if (kpiWidgetItem != null) {
            kpiWidgetTitle.setText(kpiWidgetItem.getKpiWidgetTitle());
            kpiWidgetScale.setText(kpiWidgetItem.getKpiWidgetScale());
            kpiWidgetSuffix.setText(kpiWidgetItem.getKpiWidgetSuffix());
            setLocalizationCF(kpiWidgetItem.getLocalization());
            suffixLocalizationCF = kpiWidgetItem.getSuffixLocalization();
            differenceLocalizationCF = kpiWidgetItem.getDifferenceLocalization();
            comparisonLocalizationCF = kpiWidgetItem.getComparisonLocalization();

            if (kpiWidgetItem.getGroupingColumn() != null) {
                for (int i = 0; i < xAxisListForwidget.getValues().size(); i++) {
                    if (xAxisListForwidget.getValues().get(i).getCode().equals(kpiWidgetItem.getGroupingColumn().getColumn())) {
                        xAxisListForwidget.setSelectedIndex(i);
                        break;
                    }
                }
            }
            pageSizeType.setSelected(kpiWidgetItem.getPageSizeType());
            pageSizeList.setSelected(kpiWidgetItem.getPageSize());
            if (pageSizeList.getSelectedId() == -1) {
                customPageSize.setEnabled(true);
                customPageSize.setText(String.valueOf(kpiWidgetItem.getCustomPageSize()));
            } else {
                customPageSize.setEnabled(false);
            }
            if (withAnotherItems != null) {
                withAnotherItems.setValue(kpiWidgetItem.isOtherItems());
            }
            for (SelectItem item : dateSortPeriodTypeForWidget.getItems()) {
                if (item.getDescription().equals(kpiWidgetItem.getDateSortPeriodType())) {
                    dateSortPeriodTypeForWidget.setSelectedIndex(item.getId());
                }
            }
            if (kpiWidgetItem.getGroupingColumn() != null) {
                dateSortPeriodTypeForWidget.setEnabled("date".equals(kpiWidgetItem.getGroupingColumn().getColumnType()));
            } else {
                dateSortPeriodTypeForWidget.setEnabled("date".equals(xAxisListForwidget.getValues().get(0).getCategory()));
            }

            sortByListForWidget.setSelected(1);
            if (ChartConfItem.BY_CATEGORY.equals(kpiWidgetItem.getSortBy())) {
                sortByListForWidget.setSelected(0);
            } else if (ChartConfItem.BY_CUSTOM.equals(kpiWidgetItem.getSortBy())) {
                sortByListForWidget.setSelected(2);
            }

            if (ChartConfItem.ASC.equals(kpiWidgetItem.getSortType())) {
                sortTypeListForWidget.setSelected(0);
            } else {
                sortTypeListForWidget.setSelected(1);
            }
            comparisionText.setText(kpiWidgetItem.getComparisionText());
            if (kpiWidgetItem.getIncreaseColor() == null || "GREEN".equals(kpiWidgetItem.getIncreaseColor())) {
                colorTypeList.setSelected(0);
            } else {
                colorTypeList.setSelected(1);
            }

            metricSerieConf = kpiWidgetItem.getKpiWidgetMetric();
            if (metricSerieConf != null) {
                isUnique.setValue(metricSerieConf.getUnique());
                txtSerieName.setText(metricSerieConf.getSerieName());
            }

            if (kpiWidgetItem.getModules() != null) {
                widgetSharedModules.addAll(kpiWidgetItem.getModules());
            }
            if (kpiWidgetItem.getType() != null) {
                widgetType = kpiWidgetItem.getType();
            }
            if (kpiWidgetItem.getNegAndPosType() != null) {
                negativeAndPositiveList.setSelectedByDescription(kpiWidgetItem.getNegAndPosType());
            }
            if (kpiWidgetItem.getDifferentTitle() != null) {
                differentLabel.setText(kpiWidgetItem.getDifferentTitle());
            }
            checkDifferent.setValue(kpiWidgetItem.isShowDifferent());
            initSerieData();
        }

        onChangeWidget(widgetType);
    }

    private void combinationWidgets(ChartTypeEnum widgetType) {
        if (ChartTypeEnum.BASIC_KPI.equals(widgetType)) {
            groupingField.setVisible(false);
            widgetSortingField.setVisible(false);
            pageSizeField.setVisible(false);
            increaseColor.setVisible(false);
            comparisionTitle.setVisible(false);
            seconRowContainer.setVisible(false);
            negativeAndPosiveTypeDiv.setVisible(false);
            differentLabel.setVisible(false);
            checkDifferent.setVisible(false);
            differentForm.setVisible(false);
            if (kpiWidgetAdvancedFilter1 != null) kpiWidgetAdvancedFilter1.setVisible(false);
        } else if (ChartTypeEnum.RANKING_KPI.equals(widgetType)) {
            groupingField.setVisible(true);
            widgetSortingField.setVisible(true);
            pageSizeField.setVisible(true);
            comparisionTitle.setVisible(false);
            increaseColor.setVisible(false);
            seconRowContainer.setVisible(false);
            negativeAndPosiveTypeDiv.setVisible(false);
            differentLabel.setVisible(false);
            checkDifferent.setVisible(false);
            differentForm.setVisible(false);
            if (kpiWidgetAdvancedFilter1 != null) kpiWidgetAdvancedFilter1.setVisible(false);
        } else {
            groupingField.setVisible(false);
            widgetSortingField.setVisible(false);
            pageSizeField.setVisible(false);
            comparisionTitle.setVisible(true);
            increaseColor.setVisible(true);
            seconRowContainer.setVisible(true);
            negativeAndPosiveTypeDiv.setVisible(true);
            differentLabel.setVisible(true);
            checkDifferent.setVisible(true);
            differentForm.setVisible(true);
            if (kpiWidgetAdvancedFilter1 != null) kpiWidgetAdvancedFilter1.setVisible(true);
        }
    }

    private void initSerieData() {

        if (metricSerieConf != null) {
            int selectedIndex = 0;
            int i = 0;
            for (SelectItem column : metricColumnList.getValues()) {
                if (metricSerieConf.getSerieColumn().getColumn().equals(column.getCode())) {
                    selectedIndex = i;
                    break;
                }
                i++;
            }
            metricColumnList.setSelectedIndex(selectedIndex);

            metricAggrFuncList.clear();
            metricAggrFuncList.setItems(getAggrFList(metricSerieConf.getSerieColumn().getColumnType()));
            metricAggrFuncList.setSelected(metricSerieConf.getAggrType().getId());
            if (advancedSerieColorWidget != null) {
                advancedSerieColorWidget.setData(metricSerieConf);
            }
        }
    }

    /**
     * Initialize chart type list widgets
     */
    private void initChartTypeList() {
        mapCTWidgets = new HashMap<>();
        HashMap<String, String> chartNames = new HashMap<>();
        chartNames.put(ChartTypeEnum.NONE.name(), reportingStrings.noWidgetTitle());
        chartNames.put(ChartTypeEnum.BASIC_KPI.name(), reportingStrings.basicKpi());
        chartNames.put(ChartTypeEnum.STANDARD_KPI.name(), wfmStrings.standardKpi());
        chartNames.put(ChartTypeEnum.GROWTH_KPI.name(), reportingStrings.growthKpi());
        chartNames.put(ChartTypeEnum.RANKING_KPI.name(), reportingStrings.rankingKpi());
        for (ChartTypeEnum type : ChartTypeEnum.values()) {
            if (!chartNames.containsKey(type.name())) continue;
            ListItem item = new ListItem();
            item.getElement().setInnerHTML("<div class=\"chart-view__icon\">" +
                    "<svg class=\" icon--" + type.getStyleName() + "\"> " +
                    "<use href=\"" + "mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#" + type.getStyleName() + "\"></use></svg>" +
                    "</div><div class=\"chart-view__caption\">" + chartNames.get(type.name()) + "</div>");
            item.addClickHandler(ch -> {
                widgetType = type;
                onChangeWidget(widgetType);
            });
            mapCTWidgets.put(type, item);
            widgetTypeList.add(item);
        }
    }


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
        if (kpiWidgetItem != null && kpiWidgetItem.getModules() != null && kpiWidgetItem.getModules().contains(moduleName)) {
            moduleButton.addStyleName(MODULE_SWITCH_ON);
        } else {
            moduleButton.addStyleName(MODULE_SWITCH_OFF);
        }
        moduleButton.addClickHandler(ch -> onSwitchOnOffWidgetModule(moduleName, moduleButton));

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

    private void onSwitchOnOffWidgetModule(ModuleEnum module, WfmButton2 btn) {
        if (btn.getStyleName().contains(MODULE_SWITCH_ON)) {
            btn.removeStyleName(MODULE_SWITCH_ON);
            btn.addStyleName(MODULE_SWITCH_OFF);
            widgetSharedModules.remove(module);
            getReport(view.getReport());
        } else {
            boolean isValidated = true;
            kpiWidgetTitle.removeStyleName(Constants.ERROR_FORM_STYLE);
            if (kpiWidgetTitle.getText() == null || kpiWidgetTitle.getText().isEmpty()) {
                kpiWidgetTitle.addStyleName(Constants.ERROR_FORM_STYLE);
                isValidated = false;
            }
            comparisionText.removeStyleName(Constants.ERROR_FORM_STYLE);
            if ((comparisionText.getText() == null || comparisionText.getText().isEmpty()) && !ChartTypeEnum.BASIC_KPI.equals(widgetType) && !ChartTypeEnum.RANKING_KPI.equals(widgetType)) {
                comparisionText.addStyleName(Constants.ERROR_FORM_STYLE);
                isValidated = false;
            }
            metricAggrFuncList.removeStyleName(Constants.ERROR_FORM_STYLE);
            if (metricAggrFuncList.getSelectedItem() == null) {
                metricAggrFuncList.addStyleName(Constants.ERROR_FORM_STYLE);
                isValidated = false;
            }

            if (isValidated) {
                btn.removeStyleName(MODULE_SWITCH_OFF);
                btn.addStyleName(MODULE_SWITCH_ON);
                widgetSharedModules.add(module);
                getReport(view.getReport());
            } else {
                Info.show(reportingStrings.kpiWidgetTitleAndSerieRequired(), Info.Type.WARNING);
            }
        }
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

    private void onChangeWidget(ChartTypeEnum kpiWidgetType) {
        mapCTWidgets.values().forEach(li -> li.removeStyleName("active"));
        mapCTWidgets.get(kpiWidgetType).addStyleName("active");

        if (!kpiWidgetType.equals(ChartTypeEnum.NONE)) {
            getReport(view.getReport());
            JQuery.$(kpiWidgetContainer.getElement()).slideDown(500);
            combinationWidgets(kpiWidgetType);
        } else {
            getReport(view.getReport());
            JQuery.$(kpiWidgetContainer.getElement()).slideUp(500);
        }
    }


    private SelectItem[] getSortByItems() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, wfmStrings.byCategory(), ChartConfItem.BY_CATEGORY);
        items[1] = new SelectItem(1, reportingStrings.bySeries(), ChartConfItem.BY_SERIES, true);
        return items;
    }

    private SelectItem[] getSortItems() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, "A-Z", ChartConfItem.ASC);
        items[1] = new SelectItem(1, "Z-A", ChartConfItem.DESC, true);
        return items;
    }

    private SelectItem[] getColorItems() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, "Green", "GREEN", true);
        items[1] = new SelectItem(1, "Red", "RED");
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


    public void setLocalizationCF(CustomFormLocalization localizationCF) {
        this.localizationCF = localizationCF;
    }


    private String calculatePattern(ArrayList<String> operators) {
        StringBuilder patt = new StringBuilder();
        for (int i = 0; i < operators.size(); i++) {
            if (i == 0) {
                patt.append("(").append(i + 1).append(" ");
            } else {
                patt.append(" and ").append(i + 1).append(" ");
            }
        }
        if (operators.size() > 0) {
            patt.append(")");
        }
        return patt.toString();
    }

    private MaterialLink getLocaleLink(CustomFormLocalization customFormLocalization, LocalizationTypeEnum enumType) {
        MaterialLink localeLink = new MaterialLink(wfmStrings.localization());
        Span localization = new Span();
        localization.add(localeLink);
        localeLink.addClickHandler(event -> {
            localizationCFModal = new LocalizationCFModal(customFormLocalization != null ? customFormLocalization : null, enumType);
            localizationCFModal.center();
        });

        return localeLink;
    }
}
