package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365DriveQuota extends Office365BaseResource {
    public static final String NORMAL = "normal";
    public static final String NEARING = "nearing";
    public static final String CRITICAL = "critical";
    public static final String EXCEEDED = "exceeded";

    private Long used;
    private Long total;
    private Long remaining;
    private Long deleted;

    private String state;

    public Office365DriveQuota() {
    }

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getRemaining() {
        return remaining;
    }

    public void setRemaining(Long remaining) {
        this.remaining = remaining;
    }

    public Long getDeleted() {
        return deleted;
    }

    public void setDeleted(Long deleted) {
        this.deleted = deleted;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonIgnore
    public boolean isNormal() {
        return NORMAL.equalsIgnoreCase(this.state);
    }

    @JsonIgnore
    public boolean isNearing() {
        return NEARING.equalsIgnoreCase(this.state);
    }

    @JsonIgnore
    public boolean isCritical() {
        return CRITICAL.equalsIgnoreCase(this.state);
    }

    @JsonIgnore
    public boolean isExceeded() {
        return EXCEEDED.equalsIgnoreCase(this.state);
    }
}
