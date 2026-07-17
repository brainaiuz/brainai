package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "container_item")
public class EdsContainerItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moduleID")
    private EdsModule module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propertyID")
    private EdsProperty property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "containerId")
    private EdsContainer container;

    @Column(name = "sorder")
    private Integer sorder;

    private String moduleCode;

    @Column(name = "isActive", columnDefinition = "boolean default true")
    private Boolean active = true;


    public Boolean getActive() {
        if (active == null) {
            active = true;
        }
        return active;
    }

    public PropertyItem toItem() {
        PropertyItem item = new PropertyItem();
        if (getProperty() != null) {
            item = getProperty().toItem(true);
        }
        item.setContainerItemId(getObjectID());
        item.setModule(getModuleCode());
        item.setSorder(getSorder());
        item.setActiveModule(getActive());
        if (getContainer() != null) {
            SelectItem container = new SelectItem();
            container.setId(getContainer().getObjectID());
            container.setCode(getContainer().getCode());
            item.setContainer(container);
        }

        return item;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsModule getModule() {
        return module;
    }

    public void setModule(EdsModule module) {
        this.module = module;
    }

    public EdsProperty getProperty() {
        return property;
    }

    public void setProperty(EdsProperty property) {
        this.property = property;
    }

    public EdsContainer getContainer() {
        return container;
    }

    public void setContainer(EdsContainer container) {
        this.container = container;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

