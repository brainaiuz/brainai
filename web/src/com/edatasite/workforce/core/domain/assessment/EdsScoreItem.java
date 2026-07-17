package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.assessment.client.rpc.ScoreItem;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * User: Sher
 * Date: 8/4/12
 * Time: 3:55 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "score_item")
public class EdsScoreItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "bonusDistribution", precision = 11, scale = 2)
    private BigDecimal bonusDistribution;

    @Column(name = "fromScore", precision = 11, scale = 2)
    private BigDecimal fromScore;

    @Column(name = "toScore", precision = 11, scale = 2)
    private BigDecimal toScore;

    @Column(name = "employeePercentage", precision = 11, scale = 2)
    private BigDecimal employeePercentage;

    @Column(name = "remainderBonusDistribution", precision = 11, scale = 2)
    private BigDecimal remainderBonusDistribution;

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

    public BigDecimal getBonusDistribution() {
        return bonusDistribution;
    }

    public void setBonusDistribution(BigDecimal bonusDistribution) {
        this.bonusDistribution = bonusDistribution;
    }

    public BigDecimal getFromScore() {
        return fromScore;
    }

    public void setFromScore(BigDecimal fromScore) {
        this.fromScore = fromScore;
    }

    public BigDecimal getToScore() {
        return toScore;
    }

    public void setToScore(BigDecimal toScore) {
        this.toScore = toScore;
    }

    public BigDecimal getEmployeePercentage() {
        return employeePercentage;
    }

    public void setEmployeePercentage(BigDecimal employeePercentage) {
        this.employeePercentage = employeePercentage;
    }

    public BigDecimal getRemainderBonusDistribution() {
        return remainderBonusDistribution;
    }

    public void setRemainderBonusDistribution(BigDecimal remainderBonusDistribution) {
        this.remainderBonusDistribution = remainderBonusDistribution;
    }

    public ScoreItem getDTO() {
        ScoreItem item = new ScoreItem();
        item.setObjectId(objectID);
        item.setName(name);
        item.setBonusDistribution(bonusDistribution);
        item.setFromScore(fromScore);
        item.setToScore(toScore);
        item.setEmployeePercentage(employeePercentage);
        item.setRemainderBonusDistribution(remainderBonusDistribution);
        return item;
    }
}
