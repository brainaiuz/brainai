package com.edatasite.workforce.gwt.chart.client.rpc;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

public class KpiWidgetData implements IsSerializable {

    private String chartDataTitle;
    private String chartDataSuffix;
    private String chartDataScale;
    private String chartDataTitleColor;
    private String comparisionText;
    private String increaseColor;
    private String negAndPosType;
    private BigDecimal current;
    private BigDecimal percentVal;
    private BigDecimal comparision;
    private boolean isRoseUp;
    private ChartTypeEnum type;
    private ArrayList<SelectItem> tableData;
    private LinkedList<ColumnColor> colorList;
    private Integer objectId;
    private BigDecimal difference;
    private String differentTitle;
    private boolean isShowDifferent;
    private CustomFormLocalization localization;
    private CustomFormLocalization suffixLocalization;
    private CustomFormLocalization differenceLocalization;
    private CustomFormLocalization comparisonLocalization;


    public BigDecimal getPercentVal() {
        return percentVal;
    }

    public void setPercentVal(BigDecimal percentVal) {
        this.percentVal = percentVal;
    }

    public LinkedList<ColumnColor> getColorList() {
        return colorList;
    }

    public void setColorList(LinkedList<ColumnColor> colorList) {
        this.colorList = colorList;
    }

    public String getChartDataTitle() {
        return chartDataTitle;
    }

    public void setChartDataTitle(String chartDataTitle) {
        this.chartDataTitle = chartDataTitle;
    }

    public String getChartDataScale() {
        return chartDataScale;
    }

    public void setChartDataScale(String chartDataScale) {
        this.chartDataScale = chartDataScale;
    }

    public String getChartDataSuffix() {
        return chartDataSuffix;
    }

    public void setChartDataSuffix(String chartDataSuffix) {
        this.chartDataSuffix = chartDataSuffix;
    }

    public String getChartDataTitleColor() {
        return chartDataTitleColor;
    }

    public void setChartDataTitleColor(String chartDataTitleColor) {
        this.chartDataTitleColor = chartDataTitleColor;
    }

    public String getComparisionText() {
        return comparisionText;
    }

    public void setComparisionText(String comparisionText) {
        this.comparisionText = comparisionText;
    }

    public String getIncreaseColor() {
        return increaseColor;
    }

    public void setIncreaseColor(String increaseColor) {
        this.increaseColor = increaseColor;
    }

    public BigDecimal getCurrent() {
        return current;
    }

    public void setCurrent(BigDecimal current) {
        this.current = current;
    }

    public BigDecimal getComparision() {
        return comparision;
    }

    public void setComparision(BigDecimal comparision) {
        this.comparision = comparision;
    }

    public boolean isRoseUp() {
        return isRoseUp;
    }

    public void setRoseUp(boolean roseUp) {
        isRoseUp = roseUp;
    }

    public ChartTypeEnum getType() {
        return type;
    }

    public void setType(ChartTypeEnum type) {
        this.type = type;
    }

    public String getNegAndPosType() {
        return negAndPosType;
    }

    public void setNegAndPosType(String negAndPosType) {
        this.negAndPosType = negAndPosType;
    }

    public void setTableData(ArrayList<SelectItem> tableData) {
        this.tableData = tableData;
    }

    public ArrayList<SelectItem> getTableData() {
        return tableData;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public String getDifferentTitle() {
        return differentTitle;
    }

    public void setDifferentTitle(String differentTitle) {
        this.differentTitle = differentTitle;
    }

    public boolean isShowDifferent() {
        return isShowDifferent;
    }

    public void setShowDifferent(boolean showDifferent) {
        isShowDifferent = showDifferent;
    }

    public CustomFormLocalization getLocalization() {
        return localization;
    }

    public void setLocalization(CustomFormLocalization localization) {
        this.localization = localization;
    }

    public CustomFormLocalization getSuffixLocalization() {
        return suffixLocalization;
    }

    public void setSuffixLocalization(CustomFormLocalization suffixLocalization) {
        this.suffixLocalization = suffixLocalization;
    }

    public CustomFormLocalization getDifferenceLocalization() {
        return differenceLocalization;
    }

    public void setDifferenceLocalization(CustomFormLocalization differenceLocalization) {
        this.differenceLocalization = differenceLocalization;
    }

    public CustomFormLocalization getComparisonLocalization() {
        return comparisonLocalization;
    }

    public void setComparisonLocalization(CustomFormLocalization comparisonLocalization) {
        this.comparisonLocalization = comparisonLocalization;
    }
}
