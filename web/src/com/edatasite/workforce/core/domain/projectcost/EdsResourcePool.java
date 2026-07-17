package com.edatasite.workforce.core.domain.projectcost;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 28.04.2010
 * Time: 17:18:35
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "resourcePool")
public class EdsResourcePool extends EdsObject {

    public static final String RESOURCE_TYPE = "_RESOURCE_TYPE";

    public static final String EMPLOYEE = "Employee";
    public static final String MATERIAL = "Material";
    public static final String EQUIPMENT = "Equipment";
    public static final String SUBCONTRACTOR = "Subcontractor";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "rateAvg")
    private Float rateAvg = 0.0f;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resourceTypeId")
    private EdsReference resourceType;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "resourcePoolId")
    private Set<EdsResource> reasources = new HashSet<>();

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRateAvg() {
        return rateAvg;
    }

    public void setRateAvg(Float rateAvg) {
        this.rateAvg = rateAvg;
    }

    public EdsReference getResourceType() {
        return resourceType;
    }

    public void setResourceType(EdsReference resourceType) {
        this.resourceType = resourceType;
    }

    public Set<EdsResource> getReasources() {
        return reasources;
    }

    public void setReasources(Set<EdsResource> reasources) {
        this.reasources = reasources;
    }
}
