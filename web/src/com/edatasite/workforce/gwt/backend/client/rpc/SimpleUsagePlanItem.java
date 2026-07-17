package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 24.08.2010
 * Time: 20:02:13
 */
public class SimpleUsagePlanItem extends UsagePlanItem {

    private Date paymentStartDate;
    private Date paymentEndDate;
    private Boolean messageSended;
    private Boolean deleted;
    private Integer usersFree;
    private Integer storageFree;
    private Boolean isUpgrade;
    private Float upgradePayable;
    private float totalpayable;
    private Integer paymentStatusId;
    private Integer periodTypeId;
    private Integer serviceTypeId;

    public Date getPaymentStartDate() {
        return paymentStartDate;
    }

    public void setPaymentStartDate(Date paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
    }

    public Date getPaymentEndDate() {
        return paymentEndDate;
    }

    public void setPaymentEndDate(Date paymentEndDate) {
        this.paymentEndDate = paymentEndDate;
    }

    public Boolean isMessageSended() {
        return messageSended;
    }

    public void setMessageSended(Boolean messageSended) {
        this.messageSended = messageSended;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getUsersFree() {
        return usersFree;
    }

    public void setUsersFree(Integer usersFree) {
        this.usersFree = usersFree;
    }

    public Integer getStorageFree() {
        return storageFree;
    }

    public void setStorageFree(Integer storageFree) {
        this.storageFree = storageFree;
    }

    public Boolean isUpgrade() {
        return isUpgrade;
    }

    public void setUpgrade(Boolean upgrade) {
        isUpgrade = upgrade;
    }

    public Float getUpgradePayable() {
        return upgradePayable;
    }

    public void setUpgradePayable(Float upgradePayable) {
        this.upgradePayable = upgradePayable;
    }

    public float getTotalpayable() {
        return totalpayable;
    }

    public void setTotalpayable(float totalpayable) {
        this.totalpayable = totalpayable;
    }

    public Integer getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(Integer paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public Integer getPeriodTypeId() {
        return periodTypeId;
    }

    public void setPeriodTypeId(Integer periodTypeId) {
        this.periodTypeId = periodTypeId;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
}
