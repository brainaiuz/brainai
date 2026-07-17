package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;

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
 * User: Sher(sherali.pirnafaosov@gmail.com)
 * Date: 9/18/12
 * Time: 12:35 PM
 * Finnet Technologies
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employee_bonus_item")
public class EdsEmployeeBonusItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EdsEmployee employee;

    private Double bonusAmount;

    private Double redistributedBonusAmount;

    private Double averageScore;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Double getRedistributedBonusAmount() {
        return redistributedBonusAmount;
    }

    public void setRedistributedBonusAmount(Double redistributedBonusAmount) {
        this.redistributedBonusAmount = redistributedBonusAmount;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }
}
