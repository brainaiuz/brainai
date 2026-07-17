package com.edatasite.workforce.core.domain.projectcost;

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
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 28.04.2010
 * Time: 17:19:09
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "costSheet")
public class EdsCostSheet extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectCostItemId")
    private EdsProjectCostItem projectCostItem;

    @Column(name = "date")
    private Date date;

    @Column(name = "plannedUnit")
    private Float plannedUnit = 0.0f;

    @Column(name = "actualUnit")
    private Float actualUnit = 0.0f;

    @Column(name = "plannedRate")
    private Float plannedRate = 0.0f;

    @Column(name = "actualRate")
    private Float actualRate = 0.0f;

    @Column(name = "plannedPercentCharge")
    private Float plannedPercentCharge = 0.0f;

    @Column(name = "actualPercentCharge")
    private Float actualPercentCharge = 0.0f;

    @Column(name = "plannedPercentCompleted")
    private Float plannedPercentCompleted = 0.0f;

    @Column(name = "actualPercentCompleted")
    private Float actualPercentCompleted = 0.0f;

    @Column(name = "plannedCost")
    private Float plannedCost = 0.0f;

    @Column(name = "actualCost")
    private Float actualCost = 0.0f;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsProjectCostItem getProjectCostItem() {
        return projectCostItem;
    }

    public void setProjectCostItem(EdsProjectCostItem projectCostItem) {
        this.projectCostItem = projectCostItem;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getPlannedUnit() {
        return plannedUnit;
    }

    public void setPlannedUnit(Float plannedUnit) {
        this.plannedUnit = plannedUnit;
    }

    public Float getActualUnit() {
        return actualUnit;
    }

    public void setActualUnit(Float actualUnit) {
        this.actualUnit = actualUnit;
    }

    public Float getPlannedRate() {
        return plannedRate;
    }

    public void setPlannedRate(Float plannedRate) {
        this.plannedRate = plannedRate;
    }

    public Float getActualRate() {
        return actualRate;
    }

    public void setActualRate(Float actualRate) {
        this.actualRate = actualRate;
    }

    public Float getPlannedPercentCharge() {
        return plannedPercentCharge;
    }

    public void setPlannedPercentCharge(Float plannedPercentCharge) {
        this.plannedPercentCharge = plannedPercentCharge;
    }

    public Float getActualPercentCharge() {
        return actualPercentCharge;
    }

    public void setActualPercentCharge(Float actualPercentCharge) {
        this.actualPercentCharge = actualPercentCharge;
    }

    public Float getPlannedPercentCompleted() {
        return plannedPercentCompleted;
    }

    public void setPlannedPercentCompleted(Float plannedPercentCompleted) {
        this.plannedPercentCompleted = plannedPercentCompleted;
    }

    public Float getActualPercentCompleted() {
        return actualPercentCompleted;
    }

    public void setActualPercentCompleted(Float actualPercentCompleted) {
        this.actualPercentCompleted = actualPercentCompleted;
    }

    public Float getPlannedCost() {
        return plannedCost;
    }

    public void setPlannedCost(Float plannedCost) {
        this.plannedCost = plannedCost;
    }

    public Float getActualCost() {
        return actualCost;
    }

    public void setActualCost(Float actualCost) {
        this.actualCost = actualCost;
    }
}
