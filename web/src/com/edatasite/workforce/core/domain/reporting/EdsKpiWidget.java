package com.edatasite.workforce.core.domain.reporting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetFilterItem;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetItem;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieColumn;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieConfItem;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "kpi_widget")
public class EdsKpiWidget extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String title;

    private String scale;
    private String suffix;
    private String titleColor;

    private String comparisionText;
    private String increaseColor;

    @Type(type = "text")
    @Column(name = "serieconfs_json")
    private String serieConfsJson;

    @Type(type = "text")
    @Column(name = "modules_json")
    private String modulesJson;

    @OneToMany(mappedBy = "kpiWidgetId", fetch = FetchType.LAZY)
    @ForeignKey(name = "none")
    private List<EdsKpiWidgetFilter> widgetFilterList;

    @Enumerated(value = EnumType.STRING)
    private ChartTypeEnum type;

    @Type(type = "text")
    @Column(name = "groupingColumn")
    private String groupingColumn;

    @Type(type = "text")
    @Column(name = "sortBy")
    private String sortBy;

    @Type(type = "text")
    @Column(name = "sortType")
    private String sortType;

    @Type(type = "text")
    @Column(name = "dateSortPeriodType")
    private String dateSortPeriodType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customFormLocalizationId")
    private EdsCustomFormLocalization customFormLocalization;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suffixLocalizationId")
    private EdsCustomFormLocalization suffixLocalization;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "differenceLocalizationId")
    private EdsCustomFormLocalization differenceLocalization;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comparisonLocalizationId")
    private EdsCustomFormLocalization comparisonLocalization;

    @Column(name = "pageSize")
    private Integer pageSize;
    private Integer pageSizeType;
    private Integer customPageSize;
    private String negAndPosType;
    private String differentTitle;
    @Column(name = "isShowDifferent", columnDefinition = "boolean default false")
    private boolean isShowDifferent = Boolean.FALSE;

    @Column(name = "otherItems", columnDefinition = "boolean default false")
    private Boolean otherItems = Boolean.FALSE;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }


    public String getTitle() {
        return title;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
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

    public LinkedList<SerieConfItem> getSerieConfs() {
        return StringUtils.isNotEmpty(serieConfsJson) ? new Gson().fromJson(serieConfsJson, new TypeToken<LinkedList<SerieConfItem>>() {
        }.getType()) : null;
    }

    public void setSerieConfs(LinkedList<SerieConfItem> serieConfs) {

        if (serieConfs != null && !serieConfs.isEmpty()) {
            this.serieConfsJson = new Gson().toJson(serieConfs);
        } else {
            this.serieConfsJson = null;
        }
    }

    public ArrayList<ModuleEnum> getModules() {
        return StringUtils.isNotEmpty(modulesJson) ? new Gson().fromJson(modulesJson, new TypeToken<ArrayList<ModuleEnum>>() {
        }.getType()) : null;
    }

    public void setModules(ArrayList<ModuleEnum> modules) {

        if (modules != null && !modules.isEmpty()) {
            this.modulesJson = new Gson().toJson(modules);
        } else {
            this.modulesJson = null;
        }
    }

    public List<EdsKpiWidgetFilter> getWidgetFilterList() {
        return widgetFilterList;
    }

    public void setWidgetFilterList(List<EdsKpiWidgetFilter> widgetFilterList) {
        this.widgetFilterList = widgetFilterList;
    }

    public ChartTypeEnum getType() {
        return type;
    }

    public void setType(ChartTypeEnum type) {
        this.type = type;
    }

    public KpiWidgetItem getRPC(ReportRpc report) {
        KpiWidgetItem kpiWidgetItem = new KpiWidgetItem();
        kpiWidgetItem.setObjectID(getObjectID());
        kpiWidgetItem.setKpiWidgetTitle(getTitle());
        kpiWidgetItem.setKpiWidgetScale(getScale());
        kpiWidgetItem.setKpiWidgetSuffix(getSuffix());
        kpiWidgetItem.setKpiWidgetTitleColor(getTitleColor());
        kpiWidgetItem.setGroupingColumn(getGroupingColumn());
        kpiWidgetItem.setSortBy(getSortBy());
        kpiWidgetItem.setSortType(getSortType());
        kpiWidgetItem.setDateSortPeriodType(getDateSortPeriodType());
        kpiWidgetItem.setPageSizeType(getPageSizeType());
        kpiWidgetItem.setPageSize(getPageSize());
        kpiWidgetItem.setCustomPageSize(getCustomPageSize());
        kpiWidgetItem.setOtherItems(isOtherItems());
        kpiWidgetItem.setComparisionText(getComparisionText());
        kpiWidgetItem.setIncreaseColor(getIncreaseColor());
        kpiWidgetItem.setModules(getModules());
        kpiWidgetItem.setType(getType());
        kpiWidgetItem.setNegAndPosType(getNegAndPosType());
        kpiWidgetItem.setDifferentTitle(getDifferentTitle());
        kpiWidgetItem.setShowDifferent(isShowDifferent());
        if (getSerieConfs() != null && getSerieConfs().size() > 0) {
            kpiWidgetItem.setKpiWidgetMetric(getSerieConfs().get(0));
        }
        if (getWidgetFilterList() != null && getWidgetFilterList().size() > 0) {
            for (EdsKpiWidgetFilter edsKpiWidgetFilter : getWidgetFilterList()) {
                KpiWidgetFilterItem kpiWidgetFilterItem = edsKpiWidgetFilter.toRPC(report);
                if (kpiWidgetFilterItem.getFilterType().equals(1)) {
                    kpiWidgetItem.setKpiWidgetFilterItemOne(kpiWidgetFilterItem);
                } else {
                    kpiWidgetItem.setKpiWidgetFilterItemTwo(kpiWidgetFilterItem);
                }
            }
        }
        if (getCustomFormLocalization() != null) {
            kpiWidgetItem.setLocalization(getCustomFormLocalization().getRPC());
            kpiWidgetItem.setOldLocalization(getCustomFormLocalization().getRPC());
        }
        if (getSuffixLocalization() != null) {
            kpiWidgetItem.setSuffixLocalization(getSuffixLocalization().getRPC());
        }
        if (getDifferenceLocalization() != null) {
            kpiWidgetItem.setDifferenceLocalization(getDifferenceLocalization().getRPC());
        }
        if (getComparisonLocalization() != null) {
            kpiWidgetItem.setComparisonLocalization(getComparisonLocalization().getRPC());
        }
        return kpiWidgetItem;
    }

    public EdsKpiWidget getNew(EdsKpiWidget kpiWidget) {
        kpiWidget = kpiWidget != null ? kpiWidget : new EdsKpiWidget();
        kpiWidget.title = title;
        kpiWidget.scale = scale;
        kpiWidget.suffix = suffix;
        kpiWidget.groupingColumn = groupingColumn;
        kpiWidget.sortBy = sortBy;
        kpiWidget.sortType = sortType;
        kpiWidget.dateSortPeriodType = dateSortPeriodType;
        kpiWidget.pageSizeType = pageSizeType;
        kpiWidget.pageSize = pageSize;
        kpiWidget.customPageSize = customPageSize;
        kpiWidget.comparisionText = comparisionText;
        kpiWidget.increaseColor = increaseColor;
        kpiWidget.serieConfsJson = serieConfsJson;
        kpiWidget.modulesJson = modulesJson;
        kpiWidget.widgetFilterList = widgetFilterList;
        kpiWidget.type = type;
        kpiWidget.negAndPosType = negAndPosType;
        return kpiWidget;
    }

    public void setGroupingColumn(SerieColumn groupingColumn) {
        if (groupingColumn != null) {
            this.groupingColumn = new Gson().toJson(groupingColumn);
        } else {
            this.groupingColumn = null;
        }
    }

    public SerieColumn getGroupingColumn() {
        return StringUtils.isNotEmpty(groupingColumn) ? new Gson().fromJson(groupingColumn, SerieColumn.class) : null;
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

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPageSizeType() {
        return pageSizeType;
    }

    public void setPageSizeType(Integer pageSizeType) {
        this.pageSizeType = pageSizeType;
    }

    public Integer getCustomPageSize() {
        return customPageSize;
    }

    public void setCustomPageSize(Integer customPageSize) {
        this.customPageSize = customPageSize;
    }

    public boolean isOtherItems() {
        return this.otherItems;
    }

    public void setOtherItems(final boolean otherItems) {
        this.otherItems = otherItems;
    }

    public EdsCustomFormLocalization getCustomFormLocalization() {
        return customFormLocalization;
    }

    public void setCustomFormLocalization(EdsCustomFormLocalization customFormLocalization) {
        this.customFormLocalization = customFormLocalization;
    }

    public EdsCustomFormLocalization getSuffixLocalization() {
        return suffixLocalization;
    }

    public void setSuffixLocalization(EdsCustomFormLocalization suffixLocalization) {
        this.suffixLocalization = suffixLocalization;
    }

    public EdsCustomFormLocalization getDifferenceLocalization() {
        return differenceLocalization;
    }

    public void setDifferenceLocalization(EdsCustomFormLocalization differenceLocalization) {
        this.differenceLocalization = differenceLocalization;
    }

    public EdsCustomFormLocalization getComparisonLocalization() {
        return comparisonLocalization;
    }

    public void setComparisonLocalization(EdsCustomFormLocalization comparisonLocalization) {
        this.comparisonLocalization = comparisonLocalization;
    }

    public String getNegAndPosType() {
        return negAndPosType;
    }

    public void setNegAndPosType(String negAndPosType) {
        this.negAndPosType = negAndPosType;
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
}
