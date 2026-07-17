package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 07.01.2008
 * Time: 14:32:29
 * To change this template use File | Settings | File Templates.
 */


public class ProjectList implements IsSerializable {

    private ProjectListItem[] results;
    private int totalCount;
    private String params;

    public ProjectList() {
    }

    public ProjectList(ProjectListItem[] results, int totalCount) {
        this.results = results;
        this.totalCount = totalCount;
    }

    public void setResults(ProjectListItem[] results) {
        this.results = results;
    }

    public ProjectListItem[] getResults() {
        return this.results;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public ListData getListData() {
        return new ListData(results, totalCount);
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

}


