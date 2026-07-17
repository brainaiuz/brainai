package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsSavedAssemblyItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Feb 10, 2011
 * Time: 7:47:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "inventory_transaction")
public class EdsInventoryTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private EdsItem inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_assembly_id")
    private EdsSavedAssemblyItem buildAssembly;

    @Column
    private Integer transactionType;

    public EdsItem getInventory() {
        return inventory;
    }

    public void setInventory(EdsItem inventory) {
        this.inventory = inventory;
    }

    public EdsSavedAssemblyItem getBuildAssembly() {
        return buildAssembly;
    }

    public void setBuildAssembly(EdsSavedAssemblyItem buildAssembly) {
        this.buildAssembly = buildAssembly;
    }

    public Integer getKeyId() {
        return getInventory().getObjectID();
    }

    public String getKeyType() {
        return INVENTORY_TRANSACTION;
    }

    public Integer getTransactionType() {
        return transactionType == null ? TT_STOCK_ADJUSTMENT : transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }
}
