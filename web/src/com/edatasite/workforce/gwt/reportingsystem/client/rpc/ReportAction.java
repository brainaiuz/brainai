package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Apr 25, 2011
 * Time: 1:55:19 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ReportAction implements IsSerializable {

    private String name;
    private String title;
    private String url;

    private String onClick;
    private String target;

    private ArrayList<ReportAction> columns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOnClick() {
        return onClick;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ArrayList<ReportAction> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<ReportAction> columns) {
        this.columns = columns;
    }
}
