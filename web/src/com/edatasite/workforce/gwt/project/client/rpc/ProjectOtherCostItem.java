package com.edatasite.workforce.gwt.project.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Dilsh0d
 * Date: 16-May-2010
 * Time: 20:51:37
 */
public class ProjectOtherCostItem implements IsSerializable {

    private String name;
    private Integer resourceTypeId;
    private Float percentCharge;
    private Float amountCharge;
    private boolean percent = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Integer resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
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

    public boolean isPercent() {
        return percent;
    }

    public void setPercent(boolean percent) {
        this.percent = percent;
    }
}
