package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 3/12/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportTemplateCategoryRpc implements IsSerializable {
    private Integer id;
    private LinkedList<ListItem> reportingRpc;
    private String name;
    private boolean isLibrary = true;
    private boolean asStarred = false;

    public ReportTemplateCategoryRpc() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LinkedList<ListItem> getReportingRpc() {
        return reportingRpc;
    }

    public void setReportingRpc(LinkedList<ListItem> reportingRpc) {
        this.reportingRpc = reportingRpc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addReportRpc(ListItem reportingRpc) {
        if (this.reportingRpc == null) {
            this.reportingRpc = new LinkedList<>();
        }
        this.reportingRpc.add(reportingRpc);
    }

    public void setLibrary(boolean library) {
        this.isLibrary = library;
    }

    public boolean isLibrary() {
        return isLibrary;
    }

    public void setAsStarred(boolean asstarred) {
        this.asStarred = asstarred;
    }

    public Boolean getAsStarred() {
        return this.asStarred;
    }
}
