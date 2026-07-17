package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;

public class PseudoMenuItem implements Serializable, IsSerializable {
    private String name;
    private String url;
    private boolean isDashboard;
    private boolean isPage;

    private ArrayList<PseudoMenuItem> children;

    public PseudoMenuItem() {
    }

    public PseudoMenuItem(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public PseudoMenuItem(String name, String url, boolean isPage) {
        this.name = name;
        this.url = url;
        this.isPage = isPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDashboard() {
        return isDashboard;
    }

    public void setDashboard(boolean dashboard) {
        isDashboard = dashboard;
    }

    public ArrayList<PseudoMenuItem> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<PseudoMenuItem> children) {
        this.children = children;
    }

    public boolean isPage() {
        return this.isPage;
    }

    public void setPage(final boolean page) {
        this.isPage = page;
    }
}
