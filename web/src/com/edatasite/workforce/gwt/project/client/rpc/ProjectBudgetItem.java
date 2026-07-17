package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 8/3/11
 * Time: 12:28 PM
 */
public class ProjectBudgetItem implements IsSerializable {
    private String name;
    private BigDecimal plannedWageAmount = new BigDecimal(0);
    private BigDecimal actualWageAmount = new BigDecimal(0);
    private BigDecimal variancePerCent = new BigDecimal(0);
    private BigDecimal varianceAmount = new BigDecimal(0);
    private String action;
    private SelectItem vendor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getVariancePercentItem() {
        if (getVariancePerCent() != null && getVariancePerCent().compareTo(BigDecimal.ZERO) != 0) {
            return getVariancePerCent().multiply(new BigDecimal(100));
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getPlannedWageAmount() {
        return plannedWageAmount;
    }

    public BigDecimal getPlannedWageAmount(int scale) {
        return plannedWageAmount.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public void setPlannedWageAmount(BigDecimal plannedWageAmount) {
        this.plannedWageAmount = plannedWageAmount;
    }

    public BigDecimal getActualWageAmount() {
        return actualWageAmount;
    }

    public BigDecimal getActualWageAmount(int scale) {
        return actualWageAmount.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public void setActualWageAmount(BigDecimal actualWageAmount) {
        this.actualWageAmount = actualWageAmount;
    }

    public BigDecimal getVarianceAmount() {
        return varianceAmount;
    }

    public void setVarianceAmount(BigDecimal varianceAmount) {
        this.varianceAmount = varianceAmount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BigDecimal getVariancePerCent() {
        return variancePerCent;
    }

    public void setVariancePerCent(BigDecimal variancePerCent) {
        this.variancePerCent = variancePerCent;
    }

    public SelectItem getVendor() {
        return vendor;
    }

    public void setVendor(SelectItem vendor) {
        this.vendor = vendor;
    }
}
