package com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/14/16
 * Time: 11:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceLevelBBItem implements IsSerializable {

    private SelectItem brand;
    private Integer effectType;
    private Double percentage;

    public SelectItem getBrand() {
        return brand;
    }

    public void setBrand(SelectItem brand) {
        this.brand = brand;
    }

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
