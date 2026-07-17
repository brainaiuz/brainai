package com.edatasite.workforce.gwt.core.server.rabbitmq.data;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

public class DeprecationItemMQ implements Serializable {
    private Integer user;
    private String company;
    private String database;
    private Date period;
    private Integer statusEntityId;
    private LinkedList<Integer> ids;
    private boolean lastStream = false;

    public DeprecationItemMQ(Integer user, String company, String database, Date period, Integer statusEntityId, LinkedList<Integer> ids,boolean lastStream) {
        this.user = user;
        this.company = company;
        this.database = database;
        this.period = period;
        this.statusEntityId = statusEntityId;
        this.ids = ids;
        this.lastStream = lastStream;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getCompanyId() {
        return company;
    }

    public void setCompanyId(String company) {
        this.company = company;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Integer getStatusEntityId() {
        return statusEntityId;
    }

    public void setStatusEntityId(Integer statusEntityId) {
        this.statusEntityId = statusEntityId;
    }

    public LinkedList<Integer> getIds() {
        return ids;
    }

    public void setIds(LinkedList<Integer> ids) {
        this.ids = ids;
    }

    public boolean isLastStream() {
        return lastStream;
    }

    public void setLastStream(boolean lastStream) {
        this.lastStream = lastStream;
    }

    @Override
    public String toString() {
        return "DeprecationItemMQ{" +
                "user=" + user +
                ", company='" + company + '\'' +
                ", database='" + database + '\'' +
                ", period=" + period +
                ", statusEntityId=" + statusEntityId +
                ", ids=" + ids +
                '}';
    }
}
