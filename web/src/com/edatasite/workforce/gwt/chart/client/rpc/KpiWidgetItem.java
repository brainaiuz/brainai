package com.edatasite.workforce.gwt.chart.client.rpc;


import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class KpiWidgetItem implements IsSerializable {

    private Integer objectID;

    private ChartTypeEnum type;

    private String kpiWidgetTitle;

    private String kpiWidgetScale;
    private String kpiWidgetSuffix;
    private String kpiWidgetTitleColor;

    private SerieConfItem kpiWidgetMetric;

    private KpiWidgetFilterItem kpiWidgetFilterItemOne;

    private String comparisionText;

    private KpiWidgetFilterItem kpiWidgetFilterItemTwo;

    private ArrayList<ModuleEnum> modules;
    private String increaseColor;
    private SerieColumn groupingColumn;
    private String sortBy;
    private String sortType;
    private String dateSortPeriodType;
    private Integer pageSizeType;
    private Integer pageSize;
    private Integer customPageSize;
    private boolean otherItems;
    private CustomFormLocalization localization;
    private CustomFormLocalization oldLocalization;
    private String negAndPosType;
    private String differentTitle;
    private boolean isShowDifferent;
    private CustomFormLocalization suffixLocalization;
    private CustomFormLocalization differenceLocalization;
    private CustomFormLocalization comparisonLocalization;

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

    public String getNegAndPosType() {
        return negAndPosType;
    }

    public void setNegAndPosType(String negAndPosType) {
        this.negAndPosType = negAndPosType;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getKpiWidgetScale() {
        return kpiWidgetScale;
    }

    public void setKpiWidgetScale(String kpiWidgetScale) {
        this.kpiWidgetScale = kpiWidgetScale;
    }

    public void setKpiWidgetTitle(String kpiWidgetTitle) {
        this.kpiWidgetTitle = kpiWidgetTitle;
    }

    public String getKpiWidgetTitle() {
        return kpiWidgetTitle;
    }

    public String getKpiWidgetSuffix() {
        return kpiWidgetSuffix;
    }

    public void setKpiWidgetSuffix(String kpiWidgetSuffix) {
        this.kpiWidgetSuffix = kpiWidgetSuffix;
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

    public String getKpiWidgetTitleColor() {
        return kpiWidgetTitleColor;
    }

    public void setKpiWidgetTitleColor(String kpiWidgetTitleColor) {
        this.kpiWidgetTitleColor = kpiWidgetTitleColor;
    }

    public SerieConfItem getKpiWidgetMetric() {
        return kpiWidgetMetric;
    }

    public void setKpiWidgetMetric(SerieConfItem kpiWidgetMetric) {
        this.kpiWidgetMetric = kpiWidgetMetric;
    }

    public KpiWidgetFilterItem getKpiWidgetFilterItemOne() {
        if (kpiWidgetFilterItemOne == null) {
            kpiWidgetFilterItemOne = new KpiWidgetFilterItem();
        }
        return kpiWidgetFilterItemOne;
    }

    public void setKpiWidgetFilterItemOne(KpiWidgetFilterItem kpiWidgetFilterItemOne) {
        this.kpiWidgetFilterItemOne = kpiWidgetFilterItemOne;
    }

    public KpiWidgetFilterItem getKpiWidgetFilterItemTwo() {
        if (kpiWidgetFilterItemTwo == null) {
            kpiWidgetFilterItemTwo = new KpiWidgetFilterItem();
        }
        return kpiWidgetFilterItemTwo;
    }

    public void setKpiWidgetFilterItemTwo(KpiWidgetFilterItem kpiWidgetFilterItemTwo) {
        this.kpiWidgetFilterItemTwo = kpiWidgetFilterItemTwo;
    }


    public ArrayList<ModuleEnum> getModules() {
        return modules;
    }

    public void setModules(ArrayList<ModuleEnum> modules) {
        this.modules = modules;
    }

    public String getComparisionText() {
        return comparisionText;
    }

    public void setComparisionText(String comparisionText) {
        this.comparisionText = comparisionText;
    }

    public void setIncreaseColor(String increaseColor) {
        this.increaseColor = increaseColor;
    }

    public String getIncreaseColor() {
        return increaseColor;
    }

    public ChartTypeEnum getType() {
        return type;
    }

    public void setType(ChartTypeEnum type) {
        this.type = type;
    }

    public void setGroupingColumn(SerieColumn groupingColumn) {
        this.groupingColumn = groupingColumn;
    }

    public SerieColumn getGroupingColumn() {
        return groupingColumn;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortType() {
        return sortType;
    }

    public void setDateSortPeriodType(String dateSortPeriodType) {
        this.dateSortPeriodType = dateSortPeriodType;
    }

    public String getDateSortPeriodType() {
        return dateSortPeriodType;
    }

    public void setPageSizeType(Integer pageSizeType) {
        this.pageSizeType = pageSizeType;
    }

    public Integer getPageSizeType() {
        return pageSizeType;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setCustomPageSize(Integer customPageSize) {
        this.customPageSize = customPageSize;
    }

    public Integer getCustomPageSize() {
        return customPageSize;
    }

    public Integer getPageSizeWithCustom() {
        return pageSize == null ? 20 : pageSize.equals(-1) ? getCustomPageSize() : pageSize;
    }

    public boolean isOtherItems() {
        return this.otherItems;
    }

    public void setOtherItems(final boolean otherItems) {
        this.otherItems = otherItems;
    }

    public void setLocalization(CustomFormLocalization localization) {
        this.localization = localization;
    }

    public CustomFormLocalization getLocalization() {
        return localization;
    }

    public void setOldLocalization(CustomFormLocalization oldLocalization) {
        this.oldLocalization = oldLocalization;
    }

    public CustomFormLocalization getOldLocalization() {
        return oldLocalization;
    }

}
