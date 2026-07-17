package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 18-Aug-2010
 * Time: 14:24:51
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "listPanelSettings")
public class EdsListPanelSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "panelType")
    private String panelType;

    @Column(name = "settingsJSONData")
    @Type(type = "text")
    private String settingsJSONData;

    /**
     * Through  current variable the user can choose a
     * column that all data has to be sorted according
     * to chosen column name. This variable stores the
     * name of the column.
     */
    @Column(name = "sortBy")
    private String sortBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @Column(name = "parentid")
    private Integer parentID;

    @Column(name = "isDefault", columnDefinition = " boolean default false")
    private Boolean defaultSetting = false;

    public Integer getObjectID() {
        return objectID;
    }

    public String getPanelType() {
        return panelType;
    }

    public void setPanelType(String panelType) {
        this.panelType = panelType;
    }

    public String getSettingsJSONData() {
        return settingsJSONData;
    }

    public void setSettingsJSONData(String settingsJSONData) {
        this.settingsJSONData = settingsJSONData;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public Boolean getDefaultSetting() {
        return defaultSetting;
    }

    public void setDefaultSetting(Boolean defaultSetting) {
        this.defaultSetting = defaultSetting;
    }
}
