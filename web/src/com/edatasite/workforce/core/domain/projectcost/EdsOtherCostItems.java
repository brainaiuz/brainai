package com.edatasite.workforce.core.domain.projectcost;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;

import javax.persistence.*;

/**
 * User: Dilsh0d
 * Date: 13-May-2010
 * Time: 16:08:23
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "othercostitems")
public class EdsOtherCostItems extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resourceTypeId")
    private EdsReference resourceType;

    @Column(name = "percentCharge")
    private Float percentCharge = 0.0f;

    @Column(name = "amountCharge")
    private Float amountCharge = 0.0f;

    @Column(name = "ispercent")
    private Boolean percent;

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

    public EdsReference getResourceType() {
        return resourceType;
    }

    public void setResourceType(EdsReference resourceType) {
        this.resourceType = resourceType;
    }

    public Float getPercentCharge() {
        return percentCharge;
    }

    public void setPercentCharge(Float percentCharge) {
        this.percentCharge = percentCharge;
    }

    public Float getAmountCharge() {
        return amountCharge;
    }

    public void setAmountCharge(Float amountCharge) {
        this.amountCharge = amountCharge;
    }

    public Boolean isPercent() {
        return percent;
    }

    public void setPercent(Boolean percent) {
        this.percent = percent;
    }
}
