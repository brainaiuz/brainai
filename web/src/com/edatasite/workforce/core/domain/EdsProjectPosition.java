package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;

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
import java.util.Date;

/**
 * Created by Normurod on 8/5/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "projectPostion")
public class EdsProjectPosition extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private EdsPosition position;

    private Date contractStartDate;
    private Date contractEndDate;

/*    @Column(precision = 11, scale = 4)
    private BigDecimal wageRate;

    @Column(precision = 11, scale = 4)
    private BigDecimal clientChargeRate;*/

    @Column(precision = 11, scale = 4)
    private BigDecimal overtimeRate;

    @Column(precision = 11, scale = 4)
    private BigDecimal weekendOvertimeRate;

    @Column(precision = 11, scale = 4)
    private BigDecimal holidayOvertimeRate;


    private Integer numberOfWorker;

    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractid")
    private EdsContract contract;

    private Integer priceType;

    @Column(precision = 11, scale = 4)
    private BigDecimal unitPrice;

    @Column(precision = 11, scale = 4)
    private BigDecimal unitQTY;

    @Column(precision = 11, scale = 4)
    private BigDecimal totalCharge;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsPosition getPosition() {
        return position;
    }

    public void setPosition(EdsPosition position) {
        this.position = position;
    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

/*    public BigDecimal getWageRate() {
        return wageRate;
    }

    public void setWageRate(BigDecimal wageRate) {
        this.wageRate = wageRate;
    }

    public BigDecimal getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(BigDecimal clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }*/

    public Integer getNumberOfWorker() {
        return numberOfWorker;
    }

    public void setNumberOfWorker(Integer numberOfWorker) {
        this.numberOfWorker = numberOfWorker;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setContract(EdsContract contract) {
        this.contract = contract;
    }

    public EdsContract getContract() {
        return contract;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitQTY() {
        return unitQTY;
    }

    public void setUnitQTY(BigDecimal unitQTY) {
        this.unitQTY = unitQTY;
    }

    public BigDecimal getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }

    public BigDecimal getOvertimeRate() {
        return overtimeRate;
    }

    public void setOvertimeRate(BigDecimal overtimeRate) {
        this.overtimeRate = overtimeRate;
    }

    public BigDecimal getWeekendOvertimeRate() {
        return weekendOvertimeRate;
    }

    public void setWeekendOvertimeRate(BigDecimal weekendOvertimeRate) {
        this.weekendOvertimeRate = weekendOvertimeRate;
    }

    public BigDecimal getHolidayOvertimeRate() {
        return holidayOvertimeRate;
    }

    public void setHolidayOvertimeRate(BigDecimal holidayOvertimeRate) {
        this.holidayOvertimeRate = holidayOvertimeRate;
    }

    public ProjectPosition getRPC() {
        ProjectPosition item = new ProjectPosition();
        item.setObjectID(item.getObjectID());

        if (getPosition() != null) {
            item.setPositionId(getPosition().getObjectID());
            item.setPositionName(getPosition().getName());
        }
        item.setContractStart(new DateNonConvertable(getContractStartDate()));
        item.setContractEnd(getContractEndDate() != null ? new DateNonConvertable(getContractEndDate()) : null);
        item.setPriceType(getPriceType());
        item.setUnitPrice(getUnitPrice());
        item.setOvertimeRate(getOvertimeRate());
        item.setWeekendOvertimeRate(getWeekendOvertimeRate());
        item.setHolidayOvertimeRate(getHolidayOvertimeRate());
        item.setNumberOfWorker(getNumberOfWorker());
        item.setUnitQTY(getUnitQTY());

        return item;
    }
}
