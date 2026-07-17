package com.edatasite.workforce.gwt.core.client.rpc.module;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 10-May-2011
 * Time: 20:35:38
 */
public class WfmContainer implements IsSerializable {

    private String historyName;
    private boolean showLeftMenu = true;
    private boolean showAllView = true;
    private ArrayList<String> sectionHistoryName;

    public String getHistoryName() {
        return historyName;
    }

    public void setHistoryName(String historyName) {
        this.historyName = historyName;
    }

    public boolean isShowLeftMenu() {
        return showLeftMenu;
    }

    public void setShowLeftMenu(String showLeftMenu) {
        if (showLeftMenu == null) {
            showLeftMenu = "true";
        }
        this.showLeftMenu = Boolean.valueOf(showLeftMenu);
    }

    public boolean isShowAllView() {
        return showAllView;
    }

    public void setShowAllView(boolean showAllView) {
        this.showAllView = showAllView;
    }

    public ArrayList<String> getSectionHistoryName() {
        if (sectionHistoryName == null) {
            sectionHistoryName = new ArrayList<>();
        }
        return sectionHistoryName;
    }

    public void setSectionHistoryName(ArrayList<String> sectionHistoryName) {
        this.sectionHistoryName = sectionHistoryName;
    }
}
