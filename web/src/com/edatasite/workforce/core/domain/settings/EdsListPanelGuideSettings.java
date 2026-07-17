package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelGuideSettingsRPC;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "listPanelGuideSettings",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"panelType"})})
public class EdsListPanelGuideSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "instanceName")
    private String instanceName;

    @Column(name = "youtubeUrl", length = 300)
    private String youtubeUrl;

    @Column(name = "wikiUrl", length = 300)
    private String wikiUrl;

    @Column(name = "panelType")
    private String panelType;
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name="forceittoshow")
    private Boolean forceItToShow;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

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

    public String getPanelType() {
        return panelType;
    }

    public void setPanelType(String panelType) {
        this.panelType = panelType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getForceItToShow() {
        return forceItToShow != null && forceItToShow;
    }

    public void setForceItToShow(Boolean forceItToShow) {
        this.forceItToShow = forceItToShow;
    }

    public ListPanelGuideSettingsRPC getRPC() {
        ListPanelGuideSettingsRPC rpc = new ListPanelGuideSettingsRPC();
        rpc.setInstanceName(getInstanceName());
        rpc.setWikiUrl(getWikiUrl());
        rpc.setYoutubeUrl(getYoutubeUrl());
        rpc.setPhoneNumber(getPhoneNumber());
        rpc.setForceItToShow(getForceItToShow());
        rpc.setDemoURL("https://calendly.com/kpicom/demo-request");
        return rpc;
    }

}