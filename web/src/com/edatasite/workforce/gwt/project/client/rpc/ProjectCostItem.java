package com.edatasite.workforce.gwt.project.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Lochin
 * Date: 29-Apr-2010
 * Time: 17:34:43
 * To change this template use File | Settings | File Templates.
 */
public class ProjectCostItem implements IsSerializable {

    private Integer costItemId;
    private Integer resourceId;
    private Integer resourcePoolId;
    private Float plannedQuantity = 0.0f;
    private Float plannedDaily = 0.0f;
    private Float plannedRate = 0.0f;
    private Float actualQuantity = 0.0f;
    private Float actualDaily = 0.0f;
    private Float actualRate = 0.0f;
    private Float plannedCost = 0.0f;
    private Float actualCost = 0.0f;
    // other over head data
    private Integer otherCostItemId;
    private Float otherPercent = 0.0f;
    private Float otherAmount = 0.0f;
    private Float otherPlannedAmount = 0.0f;
    private boolean percent = true;
    private boolean planned = true;

    public ProjectCostItem(Integer costItemId, Integer resourceId, Integer resourcePoolId, Float plannedQuantity, Float plannedDaily, Float plannedRate, Float plannedCost) {
        this.costItemId = costItemId;
        this.resourceId = resourceId;
        this.resourcePoolId = resourcePoolId;
        this.plannedQuantity = plannedQuantity != null ? plannedQuantity : 0.0f;
        this.plannedDaily = plannedDaily != null ? plannedDaily : 0.0f;
        this.plannedRate = plannedRate != null ? plannedRate : 0.0f;
        this.plannedCost = plannedCost != null ? plannedCost : 0.0f;
    }

    public ProjectCostItem(Integer costItemId, Integer resourceId, Integer resourcePoolId) {
        this.costItemId = costItemId;
        this.resourceId = resourceId;
        this.resourcePoolId = resourcePoolId;
    }

    public ProjectCostItem(Integer costItemId, Integer otherCostItemId, Float otherAmount, Float otherPercent, Float otherPlannedAmount, boolean percent) {
        this.costItemId = costItemId;
        this.otherCostItemId = otherCostItemId;
        this.otherAmount = otherAmount != null ? otherAmount : 0.0f;
        this.otherPercent = otherPercent != null ? otherPercent : 0.0f;
        this.otherPlannedAmount = otherPlannedAmount != null ? otherPlannedAmount : 0.0f;
        this.percent = percent;
    }

    public ProjectCostItem() {
    }

    public Integer getCostItemId() {
        return costItemId;
    }

    public void setCostItemId(Integer costItemId) {
        this.costItemId = costItemId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getResourcePoolId() {
        return resourcePoolId;
    }

    public void setResourcePoolId(Integer resourcePoolId) {
        this.resourcePoolId = resourcePoolId;
    }

    public Float getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(Float plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    public Float getPlannedRate() {
        return plannedRate;
    }

    public void setPlannedRate(Float plannedRate) {
        this.plannedRate = plannedRate;
    }

    public Float getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Float actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public Float getActualRate() {
        return actualRate;
    }

    public void setActualRate(Float actualRate) {
        this.actualRate = actualRate;
    }

    public Float getPlannedDaily() {
        return plannedDaily;
    }

    public void setPlannedDaily(Float plannedDaily) {
        this.plannedDaily = plannedDaily;
    }

    public Float getActualDaily() {
        return actualDaily;
    }

    public void setActualDaily(Float actualDaily) {
        this.actualDaily = actualDaily;
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

    public Integer getOtherCostItemId() {
        return otherCostItemId;
    }

    public void setOtherCostItemId(Integer costItemId) {
        this.otherCostItemId = costItemId;
    }

    public Float getOtherPercent() {
        return otherPercent;
    }

    public void setOtherPercent(Float otherPercent) {
        this.otherPercent = otherPercent;
    }

    public Float getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(Float otherAmount) {
        this.otherAmount = otherAmount;
    }

    public Float getOtherPlannedAmount() {
        return otherPlannedAmount;
    }

    public void setOtherPlannedAmount(Float otherPlannedAmuntl) {
        this.otherPlannedAmount = otherPlannedAmuntl;
    }

    public boolean isPercent() {
        return percent;
    }

    public void setPercent(boolean percent) {
        this.percent = percent;
    }

    public boolean isPlanned() {
        return planned;
    }

    public void setPlanned(boolean planned) {
        this.planned = planned;
    }
}
