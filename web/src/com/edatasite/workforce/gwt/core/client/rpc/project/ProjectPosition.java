package com.edatasite.workforce.gwt.core.client.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by Normurod on 8/18/15.
 */
public class ProjectPosition implements IsSerializable {

    private Integer objectID;
    private Integer positionId;
    private DateNonConvertable contractStart;
    private DateNonConvertable contractEnd;
    private BigDecimal wageRate;
    private BigDecimal clientChargeRate;
    private Integer numberOfWorker;
    private Integer priceType;
    private String priceTypeString;
    private BigDecimal unitPrice;
    private BigDecimal unitQTY;
    private BigDecimal totalCharge;

    private BigDecimal overtimeRate;
    private BigDecimal weekendOvertimeRate;
    private BigDecimal holidayOvertimeRate;

    private ProjectMember[] members;
    private String positionName;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public DateNonConvertable getContractStart() {
        return contractStart;
    }

    public void setContractStart(DateNonConvertable contractStart) {
        this.contractStart = contractStart;
    }

    public DateNonConvertable getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(DateNonConvertable contractEnd) {
        this.contractEnd = contractEnd;
    }

    public BigDecimal getWageRate() {
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
    }

    public Integer getNumberOfWorker() {
        return numberOfWorker;
    }

    public void setNumberOfWorker(Integer numberOfWorker) {
        this.numberOfWorker = numberOfWorker;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public String getPriceTypeString() {
        return priceTypeString;
    }

    public void setPriceTypeString(String priceTypeString) {
        this.priceTypeString = priceTypeString;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice != null ? unitPrice : BigDecimal.ZERO;
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

    public ProjectMember[] getMembers() {
        return members;
    }

    public void setMembers(ProjectMember[] members) {
        this.members = members;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public BigDecimal getOvertimeRate() {
        return overtimeRate != null ? overtimeRate : BigDecimal.ZERO;
    }

    public void setOvertimeRate(BigDecimal overtimeRate) {
        this.overtimeRate = overtimeRate;
    }

    public BigDecimal getWeekendOvertimeRate() {
        return weekendOvertimeRate != null ? weekendOvertimeRate : BigDecimal.ZERO;
    }

    public void setWeekendOvertimeRate(BigDecimal weekendOvertimeRate) {
        this.weekendOvertimeRate = weekendOvertimeRate;
    }

    public BigDecimal getHolidayOvertimeRate() {
        return holidayOvertimeRate != null ? holidayOvertimeRate : BigDecimal.ZERO;
    }

    public void setHolidayOvertimeRate(BigDecimal holidayOvertimeRate) {
        this.holidayOvertimeRate = holidayOvertimeRate;
    }
}
