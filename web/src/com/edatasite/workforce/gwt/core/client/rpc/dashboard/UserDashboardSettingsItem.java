package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 18.04.2018 19:49
 */
public class UserDashboardSettingsItem implements IsSerializable {

    private Integer dashboardId;
    private ArrayList<DashboardComponentItem> activeComponents = new ArrayList<>();
    private ArrayList<DashboardComponentItem> inactiveComponents = new ArrayList<>();
    private boolean applyForAll = false;

    public Integer getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Integer dashboardId) {
        this.dashboardId = dashboardId;
    }

    public ArrayList<DashboardComponentItem> getActiveComponents() {
        return activeComponents;
    }

    public void setActiveComponents(ArrayList<DashboardComponentItem> activeComponents) {
        this.activeComponents = activeComponents;
    }

    public ArrayList<DashboardComponentItem> getInactiveComponents() {
        return inactiveComponents;
    }

    public boolean isApplyForAll() {
        return applyForAll;
    }

    public void setApplyForAll(boolean applyForAll) {
        this.applyForAll = applyForAll;
    }
}
