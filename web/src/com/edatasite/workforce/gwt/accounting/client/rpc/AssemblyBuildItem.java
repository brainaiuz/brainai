package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 6/5/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssemblyBuildItem implements Serializable {

    private Integer objectID;
    private Integer assemblyID;
    private Integer transactionID;
    private DateNonConvertable date;
    private String warehouse;
    private BigDecimal qty;
    private boolean hasOutTransactions;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getAssemblyID() {
        return assemblyID;
    }

    public void setAssemblyID(Integer assemblyID) {
        this.assemblyID = assemblyID;
    }

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public boolean isHasOutTransactions() {
        return hasOutTransactions;
    }

    public void setHasOutTransactions(boolean hasOutTransactions) {
        this.hasOutTransactions = hasOutTransactions;
    }
}
