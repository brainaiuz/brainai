package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * User : Akhror
 * Date : 14.03.2024
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "wage_rate_item")
public class EdsWageRateItem extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wage_rate_id")
    private EdsWageRate wageRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private EdsReference position;

    @Column(precision = 25, scale = 5)
    private BigDecimal rate;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsWageRate getWageRate() {
        return wageRate;
    }

    public void setWageRate(EdsWageRate wageRate) {
        this.wageRate = wageRate;
    }

    public EdsReference getPosition() {
        return position;
    }

    public void setPosition(EdsReference position) {
        this.position = position;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
