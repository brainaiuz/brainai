package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 08.06.2010
 * Time: 16:24:32
 * To change this template use File | Settings | File Templates.
 */
public class TaxComponentData implements Serializable {
    private Integer objectID;
    private String name;
    private boolean compound;
    private BigDecimal rate;
    private SelectItem account;

    public TaxComponentData() {
    }

    public TaxComponentData(String name, boolean compound, BigDecimal rate) {
        this.name = name;
        this.compound = compound;
        this.rate = rate;
    }

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

    public boolean isCompound() {
        return compound;
    }

    public void setCompound(boolean compound) {
        this.compound = compound;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }
}
