package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by Djuraev on 8/1/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "positionbenefit")
public class EdsPositionBenefitAllowance extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qtytype_id")
    private EdsReference qtytype;

    @Column(name = "annualallowance", nullable = false, columnDefinition = "Decimal(10,2) default 0.00")
    private Double annualallowance = 0.00;

    @Column(name = "allowanceyear", nullable = false, columnDefinition = "int4 default 0")
    private Integer allowanceYear = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionID")
    private EdsPosition position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefitID")
    private EdsBenefit benefit;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsReference getQtytype() {
        return qtytype;
    }

    public void setQtytype(EdsReference qtytype) {
        this.qtytype = qtytype;
    }

    public Double getAnnualallowance() {
        return annualallowance;
    }

    public void setAnnualallowance(Double annualallowance) {
        this.annualallowance = annualallowance;
    }

    public Integer getAllowanceYear() {
        return allowanceYear;
    }

    public void setAllowanceYear(Integer allowanceYear) {
        this.allowanceYear = allowanceYear;
    }

    public EdsPosition getPosition() {
        return position;
    }

    public void setPosition(EdsPosition position) {
        this.position = position;
    }

    public EdsBenefit getBenefit() {
        return benefit;
    }

    public void setBenefit(EdsBenefit benefit) {
        this.benefit = benefit;
    }
}
