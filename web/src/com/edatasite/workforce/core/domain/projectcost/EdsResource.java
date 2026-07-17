package com.edatasite.workforce.core.domain.projectcost;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 28.04.2010
 * Time: 17:18:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "resource")
public class EdsResource extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resourcePoolId")
    private EdsResourcePool resourcePool;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resourceTypeId")
    private EdsReference resourceType;

    @Column(name = "name")
    private String name;

    @Column(name = "rate")
    private Float rate = 0.0f;

    @Column(name = "isHourly")
    private Boolean hourly = false;

    @Column(name = "isByItems")
    private Boolean byItems = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsResourcePool getResourcePool() {
        return resourcePool;
    }

    public void setResourcePool(EdsResourcePool resourcePool) {
        this.resourcePool = resourcePool;
    }

    public EdsReference getResourceType() {
        return resourceType;
    }

    public void setResourceType(EdsReference resourceType) {
        this.resourceType = resourceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Boolean getHourly() {
        return hourly;
    }

    public void setHourly(Boolean hourly) {
        this.hourly = hourly;
    }

    public Boolean getByItems() {
        return byItems;
    }

    public void setByItems(Boolean byItems) {
        this.byItems = byItems;
    }
}
