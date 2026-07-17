package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GettingStartedItem implements IsSerializable {
    public interface State {
        String PASSED = "PASSED";
        String ENABLED = "ENABLED";
        String DISABLED = "DISABLED";
    }

    private Integer id;
    private String title;
    private String description;
    private String state;
    private String type;
    private Integer dashboardId;

    public GettingStartedItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Integer dashboardId) {
        this.dashboardId = dashboardId;
    }

    public void setEnabled(boolean enabled) {
        if (isEnabled()) {
            if (enabled) {
                setState(State.ENABLED);
            } else {
                setState(State.DISABLED);
            }
        }
    }

    public boolean isEnabled() {
        return State.ENABLED.equals(getState());
    }

    public boolean isDisabled() {
        return State.DISABLED.equals(getState());
    }

    public boolean isPassed() {
        return State.PASSED.equals(getState());
    }
}
