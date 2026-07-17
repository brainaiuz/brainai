package com.edatasite.workforce.core.domain.dashboard;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.ModuleDashboardListItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "module_dashboards")
public class EdsDashboard extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    @Enumerated(EnumType.STRING)
    private ModuleEnum module;

    @Column(name = "is_active", columnDefinition = " boolean DEFAULT true")
    private boolean isActive;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name = "is_system")
    private Boolean isSystem;

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "dashboard_accesses",
            joinColumns = {@JoinColumn(name = "dashboard_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<EdsRole> accesses = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id")
    private EdsUser updator;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localizationId")
    private EdsCustomFormLocalization localization;

    @Column(name = "numberOfWidgets")
    private String numberOfWidgets;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModuleEnum getModule() {
        return module;
    }

    public void setModule(ModuleEnum module) {
        this.module = module;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getSystem() {
        return isSystem != null ? isSystem : false;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }

    public List<EdsRole> getAccesses() {
        return accesses;
    }

    public void setAccesses(List<EdsRole> accesses) {
        this.accesses = accesses;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public EdsUser getUpdator() {
        return updator;
    }

    public void setUpdator(EdsUser updator) {
        this.updator = updator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public EdsCustomFormLocalization getLocalization() {
        return localization;
    }

    public void setLocalization(EdsCustomFormLocalization localization) {
        this.localization = localization;
    }

    @Override
    public SelectItem getAsSelectItem() {
        return new SelectItem(getObjectID(), getName(), null, isDefault());
    }

    public ModuleDashboardListItem getRPC() {
        ModuleDashboardListItem item = new ModuleDashboardListItem();
        item.setObjectId(getObjectID());
        item.setName(getName());
        item.setNumberOfWidgets(getNumberOfWidgets());
        item.setModule(getModule());
        item.setActive(isActive());
        item.setDefault(isDefault());
        item.setSystem(getSystem());
        if (getCreator() != null) {
            item.setCreator(getCreator().getAsSelectItem());
        }
        item.setCreationDate(getCreationDate());
        if (getUpdator() != null) {
            item.setUpdator(getUpdator().getAsSelectItem());
        }
        item.setUpdatedDate(getUpdatedDate());
        return item;
    }

    public String getNumberOfWidgets() {
        return numberOfWidgets;
    }

    public void setNumberOfWidgets(String numberOfWidgets) {
        this.numberOfWidgets = numberOfWidgets;
    }
}
