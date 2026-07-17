package com.edatasite.workforce.core.domain;


import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "container")
public class EdsContainer extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String code;
    private String defaultName;
    private String moduleCode;
    private String preparedView;

    @Column(name = "sorder")
    private Integer sorder;

    private boolean changed;

    @Column(name = "isCustom", columnDefinition = "boolean default false")
    private Boolean custom = Boolean.FALSE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localizationId")
    private EdsCustomFormLocalization localization;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getPreparedView() {
        return preparedView;
    }

    public void setPreparedView(String preparedView) {
        this.preparedView = preparedView;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public EdsCustomFormLocalization getLocalization() {
        return localization;
    }

    public void setLocalization(EdsCustomFormLocalization localization) {
        this.localization = localization;
    }
}

