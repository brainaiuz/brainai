package com.edatasite.workforce.core.domain.dashboard;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.reporting.EdsKpiWidget;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardDefaultComponentItem;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 10.04.2018 21:33
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "default_components")
public class EdsDefaultComponents extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_code", referencedColumnName = "code")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsReport report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportWidget_id")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsKpiWidget kpiWidget;

    private String componentName;

    @Column(unique = true)
    private String componentCode;

    @Column(length = 500)
    private String modules;

    @Column(columnDefinition = " int default 4")
    private int width = 4;
    @Column(columnDefinition = " int default 4")
    private int height = 4;
    @Column(columnDefinition = " int default 2")
    private int minHeight = 2;
    @Column(columnDefinition = " int default 2")
    private int minWidth = 2;

    //in seconds
    @Column(name = "caching_time")
    private Integer cachingTime;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public EdsReport getReport() {
        return report;
    }

    public void setReport(EdsReport report) {
        this.report = report;
    }

    public EdsKpiWidget getKpiWidget() {
        return kpiWidget;
    }

    public void setKpiWidget(EdsKpiWidget kpiWidget) {
        this.kpiWidget = kpiWidget;
    }

    private String getModules() {
        return modules;
    }

    private void setModules(String modules) {
        this.modules = modules;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public Integer getCachingTime() {
        return cachingTime;
    }

    public void setCachingTime(Integer cachingTime) {
        this.cachingTime = cachingTime;
    }

    public ArrayList<String> getModuleList() {
        return WfmJsonUtils.jsonStringConvertToObject(getModules(), ArrayList.class);
    }

    public void setModules(List<String> moduleList) {
        setModules(WfmJsonUtils.objectConvertToJsonString(moduleList));
    }

    public DashboardDefaultComponentItem getShortRPC() {
        DashboardDefaultComponentItem item = new DashboardDefaultComponentItem();
        item.setObjectId(getObjectID());
        String componentName = getComponentName();
        if (getReport() != null) {
            componentName = componentName + " (" + getReport().getName() + ")";
        }
        item.setComponentName(componentName);
        item.setComponentCode(getComponentCode());
        if (getReport() != null) {
            item.setReportId(getReport().getObjectID());
            if (getReport().getKpiWidget() != null) {
                item.setReportWidgetId(getReport().getKpiWidget().getObjectID());
            }
        }
        item.setWidth(getWidth());
        item.setMinWidth(getMinWidth());
        item.setHeight(getHeight());
        item.setMinHeight(getMinHeight());
        return item;
    }

    public DashboardDefaultComponentItem getRPC() {
        DashboardDefaultComponentItem item = new DashboardDefaultComponentItem();
        item.setObjectId(getObjectID());
        item.setComponentName(getComponentName());
        item.setComponentCode(getComponentCode());
        item.setModules(getModuleList());
        if (getReport() != null) {
            item.setReportId(getReport().getObjectID());
            if (getReport().getKpiWidget() != null) {
                item.setReportWidgetId(getReport().getKpiWidget().getObjectID());
            }
        }
        item.setWidth(getWidth());
        item.setMinWidth(getMinWidth());
        item.setHeight(getHeight());
        item.setMinHeight(getMinHeight());
        return item;
    }
}
