package com.edatasite.workforce.gwt.core.client.ui.reportingWidgets;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.DD;
import gwt.material.design.client.ui.html.DT;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Label;

import java.math.BigDecimal;

public class KpiReportingWidget extends AsyncWidget {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    interface KpiReportingWidgetUiBinder extends UiBinder<Widget, KpiReportingWidget> {
    }

    private static final KpiReportingWidgetUiBinder ourUiBinder = GWT.create(KpiReportingWidgetUiBinder.class);

    @UiField(provided = true)
    Label tableTitle;
    @UiField(provided = true)
    DT currentAmountField;
    @UiField(provided = true)
    DD increasePersontField;
    @UiField
    DD comparisionField;
    @UiField
    FlexTable tableWidget;
    @UiField
    Div tableWidgets;
    @UiField
    Div areaWidgets;


    private KpiWidgetData kpiWidgetData;
    private final boolean isReporting;
    private String increaseStyle;
    private String decreaseStyle;
    private String backgroundIncreaseStyle;
    private String backgroundDecreaseStyle;

    public KpiReportingWidget(boolean isReporting) {
        this.isReporting = isReporting;
    }

    @Override
    protected Widget onInitialize() {
        currentAmountField = new DT();
        currentAmountField.addStyleName("widget-kpi__current");
        tableTitle = new Label();
        increasePersontField = new DD();

        add(ourUiBinder.createAndBindUi(this));

        if (ChartTypeEnum.RANKING_KPI.equals(kpiWidgetData.getType())) {
            tableWidgets.addStyleName("widget-ranking widget-content");
            areaWidgets.removeFromParent();
            if (isReporting) {
                tableTitle.setText(kpiWidgetData.getChartDataTitle());
                tableTitle.getElement().setAttribute("style", "color:" + kpiWidgetData.getChartDataTitleColor());
            } else {
                tableTitle.removeFromParent();
            }
            createWidgetForRanking();
            return null;
        }
        tableWidgets.removeFromParent();


        areaWidgets.setStyleName("widget-kpi");
        increasePersontField.addStyleName("kpi-indicator widget-kpi__indicator");

        increaseStyle = "kpi-positive--up";
        decreaseStyle = "kpi-negative--down";
        backgroundIncreaseStyle = "widget-kpi--positive";
        backgroundDecreaseStyle = "widget-kpi--negative";

        if ("RED".equals(kpiWidgetData.getIncreaseColor())) {
            increaseStyle = "kpi-negative--up";
            decreaseStyle = "kpi-positive--down";
            backgroundIncreaseStyle = "widget-kpi--negative";
            backgroundDecreaseStyle = "widget-kpi--positive";
        }


        String suffix = kpiWidgetData.getChartDataSuffix() != null ? kpiWidgetData.getChartDataSuffix() : "";


        String titleString = isReporting ? kpiWidgetData.getChartDataTitle() + ": " : "";
        currentAmountField.setText(titleString + Utils.formatWithScale(kpiWidgetData.getCurrent(), kpiWidgetData.getChartDataScale() != null ? Integer.valueOf(kpiWidgetData.getChartDataScale()) : 0) + " " + suffix);
        currentAmountField.getElement().setAttribute("style", "color:" + kpiWidgetData.getChartDataTitleColor());

        if (kpiWidgetData.getCurrent() != null) {
            if (ChartTypeEnum.BASIC_KPI.equals(kpiWidgetData.getType())) {
                createWidgetForBasic();
            } else if (ChartTypeEnum.STANDARD_KPI.equals(kpiWidgetData.getType())) {
                createWidgetForStandard();
            } else if (ChartTypeEnum.GROWTH_KPI.equals(kpiWidgetData.getType())) {
                createWidgetForGrowth();
            }
        }
        VerticalPanel comparisionVerticalField = new VerticalPanel();
        comparisionField.add(comparisionVerticalField);
        comparisionField.addStyleName("widget-kpi__past");

        DD comparision = new DD();
        comparision.addStyleName("widget-kpi__past");
        comparision.getElement().setInnerText(kpiWidgetData.getComparisionText() + ": " + Utils.formatWithScale(kpiWidgetData.getComparision(), Integer.valueOf(kpiWidgetData.getChartDataScale())));
        comparisionVerticalField.add(comparision);

        DD different = new DD();
        different.addStyleName("widget-kpi__past");
        different.getElement().setInnerText(
                (
                  kpiWidgetData.getDifferentTitle() != null && !kpiWidgetData.getDifferentTitle().isEmpty()
                  ? kpiWidgetData.getDifferentTitle()
                  : wfmStrings.difference()) + ": " + Utils.formatWithScale(kpiWidgetData.getDifference(), Integer.valueOf(kpiWidgetData.getChartDataScale()))
        );
        different.setVisible(kpiWidgetData.isShowDifferent());
        comparisionVerticalField.add(different);
        return null;
    }

    private void createWidgetForRanking() {
        tableWidget.setStyleName("widget-ranking__table");
        int i = 0;
        Double totalAmount = (double) 0;
        for (SelectItem rankingItem : kpiWidgetData.getTableData()) {
            if (i == 0) {
                tableWidget.setWidget(i, 0, new HTML(""));
                tableWidget.setWidget(i, 1, new HTML("<span>" + rankingItem.getName() + "</span>"));
                tableWidget.setWidget(i, 2, new HTML("<span>" + rankingItem.getDescription() + "</span>"));
            } else {
                tableWidget.setWidget(i, 0, new HTML(String.valueOf(i)));
                tableWidget.setWidget(i, 1, new HTML(rankingItem.getName()));
                if (rankingItem.getTotalAmount() != null) {
                    tableWidget.setWidget(i, 2, new HTML(Utils.setTextInCenter(rankingItem.getTotalAmount())));
                    totalAmount += rankingItem.getTotalAmount();
                } else {
                    tableWidget.setWidget(i, 2, new HTML(""));
                }
            }
            i++;
        }

        if (ChartTypeEnum.RANKING_KPI.equals(kpiWidgetData.getType())) {
            if (!totalAmount.equals(0d)) {
                tableWidget.setWidget(i, 0, new HTML(""));
                tableWidget.setWidget(i, 1, new HTML("<span><b>" + wfmStrings.total() + "<b/></span>"));
                tableWidget.setWidget(i, 2, new HTML("<span><b>" + Utils.setTextInCenter(totalAmount) + "<b/></span>"));
            }
        } else {
            tableWidget.setWidget(i, 0, new HTML(""));
            tableWidget.setWidget(i, 1, new HTML("<span><b>" + wfmStrings.total() + "<b/></span>"));
            tableWidget.setWidget(i, 2, new HTML("<span><b>" + Utils.setTextInCenter(totalAmount) + "<b/></span>"));
        }

    }

    private void createWidgetForGrowth() {
        setPersontColorStyles();
        currentAmountField.setText(kpiWidgetData.getPercentVal() + "%");
        HTML html = new HTML("<i class=\"kpi-indicator__sign\">" +
                "<svg class=\" icon--brokenArrowUp\"> " +
                "<use href=\"" + "mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#brokenArrowUp \"></use>" +
                "</svg>" +
                "</i>" +
                "<span>" + Utils.setTextInCenter(kpiWidgetData.getCurrent()) + "</span>");
        increasePersontField.add(html);

    }

    private void createWidgetForStandard() {
        setPersontColorStyles();
        HTML html = new HTML("<i class=\"kpi-indicator__sign\">" +
                "<svg class=\" icon--brokenArrowUp\"> " +
                "<use href=\"" + "mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#brokenArrowUp \"></use>" +
                "</svg>" +
                "</i>" +
                "<span>" + kpiWidgetData.getPercentVal() + "%" + "</span>");
        increasePersontField.add(html);
    }

    private void setPersontColorStyles() {
        if (kpiWidgetData.isRoseUp()) {
            areaWidgets.addStyleName(backgroundIncreaseStyle);
            increasePersontField.addStyleName(increaseStyle);
//            if (POSITIVE.equals(kpiWidgetData.getNegAndPosType())) {
//            } else {
//                areaWidgets.addStyleName(backgroundDecreaseStyle);
//                increasePersontField.addStyleName(decreaseStyle);
//            }
        } else {
//            if (POSITIVE.equals(kpiWidgetData.getNegAndPosType())) {
//                areaWidgets.addStyleName(backgroundIncreaseStyle);
//                increasePersontField.addStyleName(increaseStyle);
//            } else {
//            }
            areaWidgets.addStyleName(backgroundDecreaseStyle);
            increasePersontField.addStyleName(decreaseStyle);
        }
    }

    private void createWidgetForBasic() {
        if (kpiWidgetData.getPercentVal().compareTo(BigDecimal.ZERO) < 0) {
            increasePersontField.addStyleName(decreaseStyle);
        } else {
            increasePersontField.addStyleName(increaseStyle);
        }

        increasePersontField.setVisible(false);
        comparisionField.setVisible(false);
    }

    public void setData(KpiWidgetData kpiWidgetData) {
        this.kpiWidgetData = kpiWidgetData;
    }

    public KpiWidgetData getData() {
        return kpiWidgetData;
    }


}
