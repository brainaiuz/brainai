package com.edatasite.workforce.core.domain.dashboard;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "dashboard_components", uniqueConstraints = @UniqueConstraint(columnNames = {"dashboard_id", "component_id"}))
public class EdsDashboardComponents extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id")
    private EdsDashboard dashboard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private EdsDefaultComponents component;

    @Column(columnDefinition = " int default 0")
    private int x = 0; //X coordinate
    @Column(columnDefinition = " int default 0")
    private int y = 0; //Y coordinate
    @Column(columnDefinition = " int default 4")
    private int width = 4;
    @Column(columnDefinition = " int default 4")
    private int height = 4;
    @Column(columnDefinition = " int default 2")
    private int minHeight = 2;
    @Column(columnDefinition = " int default 2")
    private int minWidth = 2;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsDashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(EdsDashboard dashboard) {
        this.dashboard = dashboard;
    }

    public EdsDefaultComponents getComponent() {
        return component;
    }

    public void setComponent(EdsDefaultComponents component) {
        this.component = component;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public DashboardComponentItem getRPC() {
        DashboardComponentItem item = new DashboardComponentItem();
        item.setId(getObjectID());
        if (getComponent() != null) {
            item.setName(getComponent().getComponentName());
            item.setComponentCode(getComponent().getComponentCode());

            if (getComponent().getReport() != null) {
                item.setReportId(getComponent().getReport().getObjectID());
                if (getComponent().getKpiWidget() != null) {
                    item.setReportWidgetId(getComponent().getKpiWidget().getObjectID());
                }
            }
        }
        item.setWidth(getWidth());
        item.setMinWidth(getMinWidth());
        item.setHeight(getHeight());
        item.setMinHeight(getMinHeight());
        item.setX(getX());
        item.setY(getY());
        return item;
    }
}
