package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Jan 21, 2008
 * Time: 3:04:27 PM
 * To change this template use File | Settings | File Templates.
 */

public class ListLoadConfig implements IsSerializable {

    private int start;
    private int limit;
    private String sortField;
    private int sortDir;

    public ListLoadConfig() {
    }

    public ListLoadConfig(int limit) {
        this.limit = limit;
    }

    public ListLoadConfig(KpiLoadConfig loadConfig) {
        start = loadConfig.start;
        limit = loadConfig.limit;
        sortField = loadConfig.sortField;
        sortDir = loadConfig.sortDir;
    }

    public ListLoadConfig(ListingFilterParameter filterParametrs) {
        if (filterParametrs != null) {
            if (filterParametrs.getStart() != null && !"".equals(filterParametrs.getStart())) {
                start = filterParametrs.getStart();
            }
            if (filterParametrs.getLimit() != null && !"".equals(filterParametrs.getLimit())) {
                limit = filterParametrs.getLimit();
            }
            if (filterParametrs.getSortField() != null && !"".equals(filterParametrs.getSortField())) {
                sortField = filterParametrs.getSortField();
            }
            if (filterParametrs.getSortDir() != null && !"".equals(filterParametrs.getSortDir())) {
                sortDir = filterParametrs.getSortDir();
            }
        }
    }
    public Integer getStart_old(){
        return start;
    }

    public int getStart() {
        return start;
    }

    public int getLimit() {
        return limit;
    }

    public String getSortField() {
        return sortField;
    }

    public int getSortDir() {
        return sortDir;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public void setSortDir(int sortDir) {
        this.sortDir = sortDir;
    }

}
