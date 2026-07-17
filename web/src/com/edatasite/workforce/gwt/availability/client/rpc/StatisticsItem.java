package com.edatasite.workforce.gwt.availability.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StatisticsItem implements IsSerializable {

    private String name;
    private Integer available;
    private Integer unAvailable;
    private Integer pending;

    public StatisticsItem() {
    }

    public StatisticsItem(String name, Integer available, Integer unAvailable, Integer pending) {
        this.name = name;
        this.available = available;
        this.unAvailable = unAvailable;
        this.pending = pending;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getUnAvailable() {
        return unAvailable;
    }

    public void setUnAvailable(Integer unAvailable) {
        this.unAvailable = unAvailable;
    }

    public Integer getPending() {
        return pending;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
    }

}
