package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "forceShowGuidePanelSettings",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"panelType"})})
public class EdsForceShowGuidePanelSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name="forceittoshow")
    private Boolean forceItToShow;

    @Column(name = "panelType")
    private String panelType;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Boolean getForceItToShow() {
        return forceItToShow;
    }

    public void setForceItToShow(Boolean forceItToShow) {
        this.forceItToShow = forceItToShow;
    }

    public String getPanelType() {
        return panelType;
    }

    public void setPanelType(String panelType) {
        this.panelType = panelType;
    }
}
