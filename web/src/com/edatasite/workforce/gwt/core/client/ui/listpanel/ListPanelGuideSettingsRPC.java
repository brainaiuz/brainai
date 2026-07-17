package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ListPanelGuideSettingsRPC implements IsSerializable {
    private String youtubeUrl;
    private String wikiUrl;
    private String phoneNumber;
    private String instanceName;
    private String demoURL;
    private boolean showScheduleDemo = true;
    private boolean showPhoneNumber = true;
    private boolean showWiki = true;
    private boolean forceItToShow;
    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public boolean isForceItToShow() {
        return forceItToShow;
    }

    public void setForceItToShow(boolean forceItToShow) {
        this.forceItToShow = forceItToShow;
    }

    public String getDemoURL() {
        return demoURL;
    }

    public void setDemoURL(String demoURL) {
        this.demoURL = demoURL;
    }

    public boolean isShowScheduleDemo() {
        return showScheduleDemo;
    }

    public void setShowScheduleDemo(boolean showScheduleDemo) {
        this.showScheduleDemo = showScheduleDemo;
    }

    public boolean isShowPhoneNumber() {
        return showPhoneNumber;
    }

    public void setShowPhoneNumber(boolean showPhoneNumber) {
        this.showPhoneNumber = showPhoneNumber;
    }

    public boolean isShowWiki() {
        return showWiki;
    }

    public void setShowWiki(boolean showWiki) {
        this.showWiki = showWiki;
    }
}
